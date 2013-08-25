package com.ironalloygames.flameout;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.ironalloygames.flameout.Lander.Subsystem;
import com.ironalloygames.flameout.news.NewsState;
import com.ironalloygames.flameout.news.NewsStory;


public class InGameState extends GameState implements ContactListener {
	public OrthographicCamera camera;
	public OrthographicCamera uiCamera;

	static boolean firstTimeSeen = true;

	boolean helpScreenVisible = false;

	public int ticksToRun = 180;
	int ticksRun = (86400 * 18 + 39584) * 60;
	int ticks = 0;

	Lander lander;

	ArrayList<Pebble> pebbles = new ArrayList<Pebble>();

	Body ground = null;

	int[] controllerPos = {3,4,5};

	int controllerSelected = -1;

	public void addPebble(Vector2 pos, float size){
		Pebble p = new Pebble();
		p.pos = pos.cpy();
		p.size = size;
		pebbles.add(p);

		//CircleShape cs = new CircleShape();
		//cs.setPosition(pos.cpy());
		//cs.setRadius(size);

		PolygonShape ps = new PolygonShape();
		ps.setAsBox(size, size, pos.cpy(), 0);

		ground.createFixture(ps, 0);
	}

	public InGameState(){

	}

	Vector2 lastIntersection;

	@Override
	public GameState created(){
		world = new World(new Vector2(0, -5.4f), false);

		world.setContactListener(this);

		BodyDef bd = new BodyDef();
		ground = world.createBody(bd);

		PolygonShape ps = new PolygonShape();
		ps.setAsBox(800, 1, new Vector2(100, -6), 0);

		ground.createFixture(ps, 0);

		for(int i=0;i<400;i++){
			Vector2 rayStart;

			if (MathUtils.randomBoolean())
				rayStart = new Vector2(0,50 * (4.f / 7.f));
			else
				rayStart = new Vector2(200 * (4.f / 7.f),50 * (4.f / 7.f));

			Vector2 rayEnd = new Vector2(MathUtils.random(0, 200), -6);

			Vector2 delta = rayEnd.cpy().sub(rayStart).nor();

			System.out.println(delta);

			world.rayCast(new RayCastCallback(){

				@Override
				public float reportRayFixture(Fixture fixture, Vector2 point,
						Vector2 normal, float fraction) {
					lastIntersection = point;
					return fraction;
				}
			}, rayStart, rayEnd);

			float size = MathUtils.random(0.7f, 1.5f);

			System.out.println("PICK'D " + lastIntersection);

			addPebble(lastIntersection.cpy().add(delta.scl(-size)), size);
		}

		camera = new OrthographicCamera(1,1);
		lander = (Lander)new Lander().created(this);
		actors.add(lander);

		uiCamera = new OrthographicCamera(800,600);

		addMessage("13 seconds of fuel.\nSet us down quickly.", Speaker.GREEN);

		lander.thrusterPower = new Vector2(0, 0.5f);

		FlameoutGame.game.setMusic(Assets.spaceMusic, 0.5f);



		return super.created();
	}

	@Override
	public void update() {
		if(lander.subsystemStatus.get(Lander.Subsystem.COMMS) == 2) ticksToRun = 10;

		ticks++;

		if(ticks == 30) addMessage("This area looks flat\nenough. Set us down.", Speaker.BLUE);
		if(ticks == 40) addMessage("Got it!", Speaker.RED);
		if(ticks == 65){ addMessage("Extending legs.", Speaker.GREEN); lander.commandedLegPosition = true; }
		if(ticks == 120) addMessage("Looks like we have this one in the bag.", Speaker.RED);
		if(ticks == 180){
			addMessage("10 seconds of fuel,\njust saying.", Speaker.GREEN);

			if(firstTimeSeen){
				helpScreenVisible = true;
				firstTimeSeen = false;
			}
		}

		if(gameOverTime > 0){
			gameOverTime--;
			ticksToRun = 10;

			if(gameOverTime == 1){
				beginFade();
			}
		}

		if(lander.destroyed && gameOverTime == 0) gameOverTime = 180;

		if(lander.destroyed || this.fadeStatus > 0) ticksToRun = 10;

		if(ticksToRun == 1){
			lander.rebuildGhostPositions();
		}

		if(ticksToRun > 0){
			super.update();

			if(!lander.destroyed){
				tickMessages();
				tickMessages();
			}

			ticksToRun--;
			ticksRun++;
		}

		if(ticksToRun == 0){
			float moveMod = 1;

			Vector2 oldThrusterPower = lander.thrusterPower.cpy();

			if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT)) moveMod = 0.1f;

			if (Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.UP)) lander.thrusterPower.y = Math.min(lander.thrusterPower.y + 0.01f * moveMod, 1);
			if (Gdx.input.isKeyPressed(Keys.S) || Gdx.input.isKeyPressed(Keys.DOWN)) lander.thrusterPower.y = Math.max(lander.thrusterPower.y - 0.01f * moveMod, 0);

			if (Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.LEFT)) lander.thrusterPower.x = Math.min(lander.thrusterPower.x + 0.04f * moveMod, 1);
			if (Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.RIGHT)) lander.thrusterPower.x = Math.max(lander.thrusterPower.x - 0.04f * moveMod, -1);

			if(oldThrusterPower.sub(lander.thrusterPower).len2() > 0) lander.rebuildGhostPositions();
		}
	}

	@Override
	public void render() {
		camera.setToOrtho(false, 800 / 7, 600 / 7);

		batch.setProjectionMatrix(uiCamera.combined);
		batch.begin();

		batch.draw(Assets.starfield, -400, -300, 800, 600);

		batch.end();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		super.render();

		for(Pebble p : pebbles){
			batch.draw(Assets.pebble, p.pos.x, p.pos.y, p.size * 2, p.size * 2);
		}

		batch.end();

		if(lander.destroyed) return;

		batch.setProjectionMatrix(uiCamera.combined);
		batch.begin();

		batch.draw(Assets.tooltip, 150, 150, 300, 200);

		StringBuilder sb = new StringBuilder();

		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(1);

		sb.append(String.format("Mission Time: %02d:%02d:%02d:%02d\n", (ticksRun / 60 / 60 / 60 / 24) % 30, (ticksRun / 60 / 60 / 60) % 24, (ticksRun / 60 / 60) % 60, (ticksRun / 60) % 60));

		sb.append("Vel: " + nf.format(lander.body.getLinearVelocity().len()) + " (" + nf.format(lander.body.getLinearVelocity().x) + ", " + nf.format(lander.body.getLinearVelocity().y) + ") m/s\n");
		sb.append("Vel +1s: " + nf.format(lander.ghost1Velocity.len()) + " (" + nf.format(lander.ghost1Velocity.x) + ", " + nf.format(lander.ghost1Velocity.y) + ") m/s\n");
		sb.append("Gravity: " + nf.format(world.getGravity().y) + "m/s/s\n");

		sb.append("\nEngines: " + (int)(lander.thrusterPower.x*100) + "%, " + (int)(lander.thrusterPower.y*100) + "%\n");

		sb.append("Fuel: " + nf.format(lander.fuel) + "s @ 50%\n");

		Assets.mono13.drawMultiLine(batch, sb, 160, 280);

		if (lander.maxGroundSpeed > 0){
			if(Lander.getImpactResult(lander.maxGroundSpeed) == 0) Assets.mono13.setColor(Color.GREEN);
			if(Lander.getImpactResult(lander.maxGroundSpeed) == 1) Assets.mono13.setColor(Color.YELLOW);
			if(Lander.getImpactResult(lander.maxGroundSpeed) == 2) Assets.mono13.setColor(Color.RED);
			Assets.mono13.draw(batch, "Est Contact Speed: " + nf.format(lander.maxGroundSpeed) + "m/s", 160, 173);
			Assets.mono13.setColor(Color.WHITE);
		}

		int yMod = 15;

		int i = 0;

		int ind = (Gdx.input.getY() - 180 + yMod) / 16;

		int selCon = -1;

		for(Entry<Subsystem, Integer> ent : lander.subsystemStatus.entrySet()){
			for(int j=0;j<controllerPos.length;j++){
				if(controllerPos[j] == ent.getKey().ind){
					switch(j){
					case 0: batch.setColor(Color.RED); break;
					case 1: batch.setColor(Color.GREEN); break;
					case 2: batch.setColor(Color.BLUE); break;
					}

					if(controllerSelected != j){

						if(controllerSelected == -1 && ent.getKey().ind == ind){
							batch.draw(Assets.controllerHighlighted, 156, 123 - ((i + 1) * 16) + yMod, 32, 16);
							selCon = j;
						}else
							batch.draw(Assets.controller, 156, 123 - ((i + 1) * 16) + yMod, 32, 16);

						batch.setColor(Color.WHITE);
					}
				}
			}

			if (ent.getValue() == 1) Assets.mono13.setColor(Color.YELLOW);
			if (ent.getValue() == 2) Assets.mono13.setColor(Color.RED);
			Assets.mono13.draw(batch, ent.getKey().name, 190, 120 - ((i++) * 16) + yMod);
			Assets.mono13.setColor(Color.WHITE);
		}

		if(ticksToRun <= 0){

			if (controllerSelected == -1){
				if(selCon != -1 && Gdx.input.isButtonPressed(Buttons.LEFT) && mouseHasBeenUp){
					controllerSelected = selCon;
					mouseHasBeenUp = false;
				} else {

					if (Gdx.input.getX() > 190 + 400 && ind >= 0 && ind < Lander.Subsystem.values().length){
						int tlx = (Gdx.input.getX() - 400);
						int tly = (300 - Gdx.input.getY());

						batch.draw(Assets.tooltip, tlx, tly - 190, 150, 190);

						Assets.mono16.draw(batch, Lander.Subsystem.values()[ind].name, tlx + 10, tly - 10);

						Assets.mono13.setColor(Color.YELLOW);
						Assets.mono13.drawWrapped(batch, "Yellow: " + Lander.Subsystem.values()[ind].descriptionAtYellow, tlx + 10, tly - 30, 130);

						Assets.mono13.setColor(Color.RED);
						Assets.mono13.drawWrapped(batch, "Red: " + Lander.Subsystem.values()[ind].descriptionAtRed, tlx + 10, tly - 110, 130);

						Assets.mono13.setColor(Color.WHITE);
					}

					if (Gdx.input.getX() > 160 + 400 && Gdx.input.getX() < 190 + 400 && ind >= 0 && ind < Lander.Subsystem.values().length){
						for(int j=0;j<controllerPos.length;j++){
							if(controllerPos[j] == ind){

							}
						}
					}
				}
			} else {
				if(Gdx.input.isButtonPressed(Buttons.RIGHT)){
					controllerSelected = -1;
				} else if(Gdx.input.isButtonPressed(Buttons.LEFT) && mouseHasBeenUp){
					if(ind >= 0 && ind < lander.subsystemStatus.size()){

						boolean occupied = false;

						for(int ix : controllerPos){
							if(ix == ind) occupied = true;
						}

						if(!occupied)
							controllerPos[controllerSelected] = ind;
					}
					mouseHasBeenUp = false;
					controllerSelected = -1;
				} else {
					int tlx = (Gdx.input.getX() - 400);
					int tly = (300 - Gdx.input.getY());

					switch(controllerSelected){
					case 0: batch.setColor(Color.RED); break;
					case 1: batch.setColor(Color.GREEN); break;
					case 2: batch.setColor(Color.BLUE); break;
					}

					batch.draw(Assets.controllerHighlighted, tlx - 16, tly - 8, 32, 16);

					batch.setColor(Color.WHITE);
				}
			}
		}

		if(!Gdx.input.isButtonPressed(Buttons.LEFT)) mouseHasBeenUp = true;

		if(helpScreenVisible){
			drawHelpScreen();
		}

		batch.end();
	}

	private void drawHelpScreen() {
		batch.draw(Assets.solid, -750 / 2, -550 / 2, 750, 350);

		Assets.mono13.drawWrapped(batch,
				"The object of this game is to get the lander down onto the grey terrain at the bottom of the screen, and bring it to a total stop. " +
						"The \"ghosts\" of your lander indicate where it will be in the next two turns, assuming that you cut all power after the first turn. " +
						"\n\nAs you descend, shoddy lander construction will cause systems to fail. On the middle right is a display of your six subsystems: Thrusters, Engines, Comm, Control, Legs and Fuel. " +
						"\n\nThe colored markers to their left indicate that a ground crew member is monitoring that system. As long as a system is monitored, it will not fail further, and the crew member will repair it over time. " +
						"To move a crewmember to a different system, click on them and click on the system you want them to monitor. " +
						"\n\nMouseover a system to find out what happens if it fails." +
						"\n\nKey Commands" +
						"\nWASD/Cursor Keys...............Change Thrust" +
						"\nShift + Thrust Key.............Minor Thrust Change" +
						"\nX..............................Stop All Thrust" +
						"\nEnter..........................End Turn" +
						"\nF1.............................View This Screen" +
						"\n\nPress any Key to Continue",
						-750 / 2 + 15, -550 / 2 + 350 - 15, 720);
	}

	@Override
	public void gameOver() {
		beginFade();
		super.gameOver();
	}

	@Override
	public void fullyFaded() {

		System.out.println("fully faded");

		if(lander.destroyed) FlameoutGame.game.setGameState(new NewsState(NewsStory.Tag.DESTROYED));
		else if(lander.damaged) FlameoutGame.game.setGameState(new NewsState(NewsStory.Tag.DAMAGED));
		else FlameoutGame.game.setGameState(new NewsState(NewsStory.Tag.PERFECT));

		super.fullyFaded();
	}

	int gameOverTime = 0;

	boolean mouseHasBeenUp = true;

	@Override
	public boolean keyDown(int keycode) {
		if(helpScreenVisible){
			helpScreenVisible = false;
			return super.keyDown(keycode);
		}

		if(keycode == Keys.F1){
			helpScreenVisible = true;
		}

		if (ticksToRun != 0) return false;

		System.out.println("X");

		if (keycode == Keys.X){ lander.thrusterPower.set(0,0); lander.rebuildGhostPositions(); }

		if (keycode == Keys.ENTER) ticksToRun = 60;

		return super.keyDown(keycode);
	}

	@Override
	public boolean keyUp(int keycode) {

		return super.keyUp(keycode);
	}

	@Override
	public boolean canSubsystemBreak(Subsystem sub) {

		if(ticks < 180) return false;

		for(int ts : controllerPos){
			if(ts == sub.ind) return false;
		}

		return true;
	}

	@Override
	public void beginContact(Contact contact) {
		super.beginContact(contact);
	}

	@Override
	public void endContact(Contact contact) {
		super.endContact(contact);
	}

	@Override
	public Vector2 getMessagePos() {
		return new Vector2(-350, 250);
	}
}
