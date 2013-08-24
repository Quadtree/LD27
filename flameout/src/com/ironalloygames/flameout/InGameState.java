package com.ironalloygames.flameout;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.ironalloygames.flameout.Lander.Subsystem;


public class InGameState extends GameState {
	public OrthographicCamera camera;
	public OrthographicCamera uiCamera;

	public int ticksToRun = 60;
	int ticksRun = (86400 * 18 + 39584) * 60;

	Lander lander;

	ArrayList<Pebble> pebbles = new ArrayList<Pebble>();

	Body ground = null;

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

		sb.append("Vel: (" + nf.format(lander.body.getLinearVelocity().x) + ", " + nf.format(lander.body.getLinearVelocity().x) + ") m/s\n");
		sb.append("Vel +1s: (" + nf.format(lander.ghost1Velocity.x) + ", " + nf.format(lander.ghost1Velocity.x) + ") m/s\n");
		sb.append("Gravity: " + nf.format(world.getGravity().y) + "m/s/s\n");

		sb.append("\nThruster: " + (int)(lander.thrusterPower.x*100) + "%, " + (int)(lander.thrusterPower.y*100) + "%\n");

		Assets.mono13.drawMultiLine(batch, sb, 160, 250);

		int i = 0;

		for(Entry<Subsystem, Integer> ent : lander.subsystemStatus.entrySet()){
			Assets.mono13.draw(batch, ent.getKey().name, 190, 120 + -(i++ * 16));
		}

		int ind = ((Gdx.input.getY()) - 180) / 16;

		if (Gdx.input.getX() > 190 && ind >= 0 && ind < Lander.Subsystem.values().length){
			System.out.println(ind);
		}

		batch.end();
	}

	@Override
	public boolean keyDown(int keycode) {
		if (ticksToRun != 0) return false;

		if (keycode == Keys.ENTER) ticksToRun = 60;

		return super.keyDown(keycode);
	}

	@Override
	public boolean keyUp(int keycode) {

		return super.keyUp(keycode);
	}


}
