package com.ironalloygames.flameout;

import java.util.EnumMap;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Lander extends Actor {

	enum Subsystem {
		THRUSTER ("Thruster", "Thruster power is reduced by 50%. ", "Thrusters no longer function."),
		ENGINE ("Engine", "Main engine power is reduced by 50%.", "The engine no longer functions."),
		COMMS ("Comms", "No loss.", "The lander becomes permenantly uncontrollable."),
		CONTROL ("Control", "50% chance control inputs are randomly modified.", "Control inputs are always randomly modified."),
		LEGS ("Legs", "Legs come down.", "No further effect."),
		FUEL ("Fuel", "5% of fuel wasted per second.", "10% of fuel wasted per second.");

		public String name;
		public String descriptionAtYellow;
		public String descriptionAtRed;

		private Subsystem(String s, String desc, String desc2){
			name = s;
			descriptionAtYellow = desc;
			descriptionAtRed = desc2;
		}
	}

	private static final float THRUSTER_MOD = 0.7f;
	private static final float TURN_MOD = 11.f;
	Vector2 ghost1Pos = null;
	Vector2 ghost2Pos = null;
	float ghost1Angle = 0;
	float ghost2Angle = 0;

	public Vector2 ghost1Velocity = new Vector2();
	public Vector2 ghost2Velocity = new Vector2();

	public Vector2 thrusterPower = new Vector2();

	public Vector2 actualThrusterPower = new Vector2();

	public EnumMap<Subsystem, Integer> subsystemStatus = new EnumMap<Subsystem, Integer>(Subsystem.class);

	@Override
	public void render() {
		super.render();

		List<Sprite> graphics = null;

		if (actualThrusterPower.y < 0.05f){
			graphics = Assets.landerEngineOff;
		} else if (actualThrusterPower.y > 0.75f){
			graphics = Assets.landerEngineHigh;
		} else {
			graphics = Assets.landerEngineLow;
		}

		state.batch.draw(
				graphics.get(graphics.size() - 1), body.getPosition().x, body.getPosition().y,
				0.5f, 0.5f, 1, 1, getHalfSize() * 2, getHalfSize() * 2, body.getAngle() * (180 / MathUtils.PI) - 90, false
				);

		state.batch.draw(
				Assets.landerOutline, ghost1Pos.x, ghost1Pos.y,
				0.5f, 0.5f, 1, 1, getHalfSize() * 2, getHalfSize() * 2, ghost1Angle * (180 / MathUtils.PI) - 90, false
				);

		state.batch.draw(
				Assets.landerOutline, ghost2Pos.x, ghost2Pos.y,
				0.5f, 0.5f, 1, 1, getHalfSize() * 2, getHalfSize() * 2, ghost2Angle * (180 / MathUtils.PI) - 90, false
				);


	}

	@Override
	public Actor created(GameState state) {
		super.created(state);

		for(Subsystem sub : Subsystem.values()){
			subsystemStatus.put(sub, 0);
		}

		body.setTransform(new Vector2(100 * (4.f / 7.f),110 * (4.f / 7.f)), -0.5f);
		body.setLinearVelocity(-6, 6);

		this.rebuildGhostPositions();

		return this;
	}

	@Override
	public void update() {
		this.applyEngineForce();

		this.actualThrusterPower.set(thrusterPower);

		super.update();
	}

	public void rebuildGhostPositions(){
		Vector2 pos = body.getPosition().cpy();
		float ang = body.getAngle();
		Vector2 linVel = body.getLinearVelocity().cpy();
		float angVel = body.getAngularVelocity();

		for(int i=0;i<60;i++){
			this.applyEngineForce();
			state.world.step(0.016f, 1, 1);
		}

		ghost1Pos = body.getPosition().cpy();
		ghost1Angle = body.getAngle();

		ghost1Velocity = body.getLinearVelocity().cpy();

		for(int i=0;i<60;i++){
			state.world.step(0.016f, 1, 1);
		}

		ghost2Pos = body.getPosition().cpy();
		ghost2Angle = body.getAngle();

		ghost2Velocity = body.getLinearVelocity().cpy();

		body.setTransform(pos, ang);
		body.setLinearVelocity(linVel);
		body.setAngularVelocity(angVel);
	}

	protected void applyEngineForce(){
		body.applyLinearImpulse(new Vector2(
				MathUtils.cos(body.getAngle() + MathUtils.PI / 2) * thrusterPower.y * THRUSTER_MOD * body.getMass(),
				MathUtils.sin(body.getAngle() + MathUtils.PI / 2) * thrusterPower.y * THRUSTER_MOD * body.getMass()),
				body.getWorldCenter(), true);

		body.applyAngularImpulse(thrusterPower.x * TURN_MOD, true);
	}
}
