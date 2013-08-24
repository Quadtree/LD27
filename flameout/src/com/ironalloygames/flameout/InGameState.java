package com.ironalloygames.flameout;

import java.text.NumberFormat;
import java.util.ArrayList;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;


public class InGameState extends GameState {
	public OrthographicCamera camera;
	public OrthographicCamera uiCamera;

	public int ticksToRun = 60;
	int ticksRun = (86400 * 67 + 39584) * 60;

	Lander lander;

	ArrayList<Pebble> pebbles = new ArrayList<Pebble>();

	Body ground = null;

	public void addPebble(Vector2 pos, float size){
		Pebble p = new Pebble();
		p.pos = pos.cpy();
		p.size = size;
		pebbles.add(p);

		CircleShape cs = new CircleShape();
		cs.setPosition(pos);
		cs.setRadius(size);

		ground.createFixture(cs, 0);
	}

	public InGameState(){

	}

	@Override
	public GameState created(){
		world = new World(new Vector2(0, -9.1f), false);

		BodyDef bd = new BodyDef();
		ground = world.createBody(bd);

		PolygonShape ps = new PolygonShape();
		ps.setAsBox(800, 1, new Vector2(100, -6), 0);

		ground.createFixture(ps, 0);

		for(int i=0;i<600;i++){
			world.rayCast(new RayCastCallback(){

				@Override
				public float reportRayFixture(Fixture fixture, Vector2 point,
						Vector2 normal, float fraction) {
					float size = MathUtils.random(2, 5);
					addPebble(point.cpy().add(normal.cpy().scl(size)), size);
					System.out.println(point);
					return 0;
				}
			}, new Vector2(MathUtils.random(0, 200), 400), new Vector2(MathUtils.random(0, 200), -200));
		}

		camera = new OrthographicCamera(1,1);
		lander = (Lander)new Lander().created(this);
		actors.add(lander);

		uiCamera = new OrthographicCamera(800,600);



		return super.created();
	}

	@Override
	public void update() {
		if(ticksToRun == 1){
			lander.rebuildGhostPositions();
		}

		if(ticksToRun > 0){
			super.update();
			ticksToRun--;
			ticksRun++;
		}

		if (increaseThrust) lander.thrusterPower.y = Math.min(lander.thrusterPower.y + 0.04f, 1);
		if (decreaseThrsut) lander.thrusterPower.y = Math.max(lander.thrusterPower.y - 0.04f, 0);

		if (turnLeft) lander.thrusterPower.x = Math.min(lander.thrusterPower.x + 0.04f, 1);
		if (turnRight) lander.thrusterPower.x = Math.max(lander.thrusterPower.x - 0.04f, -1);

		if(increaseThrust || decreaseThrsut || turnLeft || turnRight) lander.rebuildGhostPositions();
	}

	@Override
	public void render() {
		camera.setToOrtho(false, 800 / 7, 600 / 7);

		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		super.render();

		for(Pebble p : pebbles){
			batch.draw(Assets.pebble, p.pos.x, p.pos.y, p.size * 2, p.size * 2);
		}

		batch.end();

		batch.setProjectionMatrix(uiCamera.combined);
		batch.begin();

		StringBuilder sb = new StringBuilder();

		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(1);

		sb.append("Mission Time: " + (ticksRun / 60 / 60 / 60 / 24 / 30) + ":" + (ticksRun / 60 / 60 / 60 / 24) % 30 + ":" + (ticksRun / 60 / 60 / 60) % 24 + ":" + (ticksRun / 60 / 60) % 60 + ":" + (ticksRun / 60) % 60 + "\n\n");

		sb.append("Horiz Velocity: " + nf.format(lander.body.getLinearVelocity().x) + "m/s\n");
		sb.append("Verti Velocity: " + nf.format(lander.body.getLinearVelocity().y) + "m/s\n");
		sb.append("Gravity: " + nf.format(world.getGravity().y) + "m/s/s\n");

		sb.append("\nThruster: " + (int)(lander.thrusterPower.x*100) + "%, " + (int)(lander.thrusterPower.y*100) + "%\n");

		Assets.mono13.drawMultiLine(batch, sb, 160, 250);

		batch.end();
	}

	boolean increaseThrust = false;
	boolean decreaseThrsut = false;
	boolean turnLeft = false;
	boolean turnRight = false;

	@Override
	public boolean keyDown(int keycode) {
		if (ticksToRun != 0) return false;

		if (keycode == Keys.W) increaseThrust = true;
		if (keycode == Keys.S) decreaseThrsut = true;
		if (keycode == Keys.A) turnLeft = true;
		if (keycode == Keys.D) turnRight = true;

		if (keycode == Keys.ENTER) ticksToRun = 60;

		return super.keyDown(keycode);
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.W) increaseThrust = false;
		if (keycode == Keys.S) decreaseThrsut = false;
		if (keycode == Keys.A) turnLeft = false;
		if (keycode == Keys.D) turnRight = false;

		return super.keyUp(keycode);
	}


}
