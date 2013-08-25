package com.ironalloygames.flameout;

import java.util.ArrayList;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL11;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.ironalloygames.flameout.Lander.Subsystem;

public class GameState implements InputProcessor, ContactListener {

	OrthographicCamera msgCam = new OrthographicCamera(800,600);

	public enum Speaker {
		RED,
		GREEN,
		BLUE
	}

	int fadeStatus = 0;

	class SpokenMessage {
		public String message;
		public Speaker speaker;
		public int ticks;
		public int lineBreaks;
	}

	protected void beginFade(){ fadeStatus = Math.max(fadeStatus, 1); }

	public Vector2 getMessagePos(){
		return new Vector2(250, 0);
	}

	public void addMessage(String message, Speaker speaker){
		SpokenMessage sm = new SpokenMessage();
		sm.message = message;
		sm.speaker = speaker;
		sm.ticks = (message.length()*4 + 30) * 3 / 2;
		sm.lineBreaks = message.split("\n").length;

		messages.add(sm);

		Assets.messageSound.setVolume(Assets.messageSound.play(), 0.05f);
	}

	ArrayList<SpokenMessage> messages = new ArrayList<SpokenMessage>();

	public ArrayList<Actor> actors = new ArrayList<Actor>();
	public World world;
	public SpriteBatch batch;

	public ArrayList<Actor> actorAddQueue = new ArrayList<Actor>();

	public boolean canSubsystemBreak(Lander.Subsystem sub){ return false; }

	public GameState created(){
		batch = new SpriteBatch();
		return this;
	}

	public void update(){
		if (world != null) world.step(0.016f, 4, 4);

		while(actorAddQueue.size() > 0){
			actors.add(actorAddQueue.get(0));
			actorAddQueue.remove(0);
		}

		for(int i=0;i<actors.size();i++){
			actors.get(i).update();
		}

		tickMessages();

		if(fadeStatus > 0) fadeStatus++;
	}

	protected void tickMessages() {
		if(messages.size() > 0){
			messages.get(0).ticks--;
			if(messages.get(0).ticks <= 0) messages.remove(0);
		}
	}

	public void renderMessages(){
		batch.setProjectionMatrix(msgCam.combined);
		batch.begin();

		float y = getMessagePos().y;

		for(int i=0;i<messages.size();i++){
			if(messages.get(i).speaker == Speaker.RED) Assets.borderedSmall.setColor(Color.RED);
			if(messages.get(i).speaker == Speaker.GREEN) Assets.borderedSmall.setColor(Color.GREEN);
			if(messages.get(i).speaker == Speaker.BLUE) Assets.borderedSmall.setColor(Color.BLUE);

			Assets.borderedSmall.drawMultiLine(batch, messages.get(i).message, getMessagePos().x, y);

			y -= messages.get(i).lineBreaks * 23;
		}
		batch.end();

		if(fadeStatus > 0){
			//System.out.print(Math.min((float)fadeStatus / 60, 1) + " ");

			int f1 = batch.getBlendSrcFunc();
			int f2 = batch.getBlendDstFunc();

			batch.setBlendFunction(GL11.GL_ONE, GL11.GL_ONE);
			batch.enableBlending();
			batch.begin();
			batch.setColor(1, 1, 1, 1 - Math.min((float)fadeStatus / 60, 1));
			batch.draw(Assets.solid, -400, -300, 800, 600);
			batch.end();

			batch.setBlendFunction(f1, f2);

			if(fadeStatus == 60){
				fullyFaded();
			}
		}
	}

	public void fullyFaded(){}

	public void render(){
		for(Actor a : actors){
			a.render();
		}
	}

	public void gameOver(){}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void beginContact(Contact contact) {
		// TODO Auto-generated method stub

	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}

	protected void drawTextCentered(BitmapFont font, String s, int x, int y, int wrapWidth){
		font.drawWrapped(batch, s, x - font.getWrappedBounds(s, wrapWidth).width / 2, y, wrapWidth);
	}

	public Speaker getController(Subsystem sub){
		return Speaker.GREEN;
	}
}
