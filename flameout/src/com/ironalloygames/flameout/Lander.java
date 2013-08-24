package com.ironalloygames.flameout;

import java.util.EnumMap;
import java.util.List;
import java.util.Map.Entry;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Lander extends Actor {

	enum Subsystem {
		THRUSTER (0, "Thruster", "Thruster power is reduced by 50%. ", "Thrusters no longer function."),
		ENGINE (1, "Engine", "Main engine power is reduced by 50%.", "The engine no longer functions."),
		COMMS (2, "Comms", "No loss.", "The lander becomes permenantly uncontrollable."),
		CONTROL (3, "Control", "Random uncommanded actions.", "Strong random uncommanded actions."),
		LEGS (4, "Legs", "Legs come down.", "No further effect."),
		FUEL (5, "Fuel", "10% of fuel wasted per second.", "20% of fuel wasted per second.");

		public String name;
		public String descriptionAtYellow;
		public String descriptionAtRed;
		public int ind;

		private Subsystem(int ind, String s, String desc, String desc2){
			name = s;
			descriptionAtYellow = desc;
			descriptionAtRed = desc2;
			this.ind = ind;
		}
	}

	private static final float THRUSTER_MOD = 0.35f;
	private static final float TURN_MOD = 11.f;
	Vector2 ghost1Pos = null;
	Vector2 ghost2Pos = null;
	float ghost1Angle = 0;
	float ghost2Angle = 0;

	int curLegStatus = 60;

	public float fuel = 10;

	public Vector2 ghost1Velocity = new Vector2();
	public Vector2 ghost2Velocity = new Vector2();

	public Vector2 thrusterPower = new Vector2();

	public Vector2 actualThrusterPower = new Vector2();

	public EnumMap<Subsystem, Integer> subsystemStatus = new EnumMap<Subsystem, Integer>(Subsystem.class);

	public boolean commandedLegPosition = true;

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
				graphics.get(curLegStatus / 8), body.getPosition().x, body.getPosition().y,
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

		fuel -= thrusterPower.y / 60 * 2;

		fuel *= 1 - ((subsystemStatus.get(Subsystem.FUEL) * 0.1f) / 60);

		if (MathUtils.randomBoolean(0.05f)){

			float variation = 0;

			if (subsystemStatus.get(Subsystem.CONTROL) == 1) variation = 0.1f;
			if (subsystemStatus.get(Subsystem.CONTROL) == 2) variation = 0.5f;

			thrusterPower.x = MathUtils.clamp(thrusterPower.x + MathUtils.random(-variation, variation), -1, 1);
			thrusterPower.y = MathUtils.clamp(thrusterPower.y + MathUtils.random(-variation, variation), 0, 1);
		}

		boolean legPosition = commandedLegPosition;

		if(subsystemStatus.get(Subsystem.LEGS) != 0) legPosition = false;

		if (!legPosition){
			if(curLegStatus > 0) curLegStatus--;
		} else {
			if(curLegStatus < 59) curLegStatus++;
		}

		for(Entry<Subsystem, Integer> ent : subsystemStatus.entrySet()){
			if(state.canSubsystemBreak(ent.getKey()) && MathUtils.randomBoolean(0.15f / 60) && ent.getValue() < 2){
				ent.setValue(ent.getValue() + 1);
			}
		}

		if(fuel <= 0){ fuel = 0; actualThrusterPower.set(0,0); thrusterPower.set(0,0); }

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
				MathUtils.cos(body.getAngle() + MathUtils.PI / 2) * thrusterPower.y * THRUSTER_MOD * body.getMass() * (1 - (subsystemStatus.get(Subsystem.ENGINE) * 0.5f)),
				MathUtils.sin(body.getAngle() + MathUtils.PI / 2) * thrusterPower.y * THRUSTER_MOD * body.getMass() * (1 - (subsystemStatus.get(Subsystem.ENGINE) * 0.5f))),
				body.getWorldCenter(), true);

		body.applyAngularImpulse(thrusterPower.x * TURN_MOD * (1 - (subsystemStatus.get(Subsystem.THRUSTER) * 0.5f)), true);
	}
}
