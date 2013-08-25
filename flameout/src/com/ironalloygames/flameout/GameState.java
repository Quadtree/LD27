package com.ironalloygames.flameout;

import java.util.ArrayList;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

public class GameState implements InputProcessor, ContactListener {

	OrthographicCamera msgCam = new OrthographicCamera(800,600);

	public enum Speaker {
		RED,
		GREEN,
		BLUE
	}

	class SpokenMessage {
		public String message;
		public Speaker speaker;
		public int ticks;
		public int lineBreaks;
	}

	public Vector2 getMessagePos(){
		return new Vector2(250, 0);
	}

	public void addMessage(String message, Speaker speaker){
		SpokenMessage sm = new SpokenMessage();
		sm.message = message;
		sm.speaker = speaker;
		sm.ticks = message.length()*4 + 30;
		sm.lineBreaks = message.split("\n").length;

		messages.add(sm);
	}

	ArrayList<SpokenMessage> messages = new ArrayList<SpokenMessage>();

	public ArrayList<Actor> actors = new ArrayList<Actor>();
	public World world;
	public SpriteBatch batch;

	public boolean canSubsystemBreak(Lander.Subsystem sub){ return false; }

	public GameState created(){
		batch = new SpriteBatch();
		return this;
	}

	public void update(){
		if (world != null) world.step(0.016f, 4, 4);

		for(int i=0;i<actors.size();i++){
			actors.get(i).update();
		}

		for(int i=0;i<messages.size();i++){
			if(messages.get(i).ticks-- == 0){
				messages.remove(i--);
			}
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
	}

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
}
