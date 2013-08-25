package com.ironalloygames.flameout;

import java.util.EnumMap;
import java.util.List;
import java.util.Map.Entry;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.ironalloygames.flameout.GameState.Speaker;

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

	int curLegStatus = 0;

	public float fuel = 13;

	public Vector2 ghost1Velocity = new Vector2();
	public Vector2 ghost2Velocity = new Vector2();

	public Vector2 thrusterPower = new Vector2();

	public Vector2 actualThrusterPower = new Vector2();

	public EnumMap<Subsystem, Integer> subsystemStatus = new EnumMap<Subsystem, Integer>(Subsystem.class);

	public boolean commandedLegPosition = false;

	public static int getImpactResult(float speed){
		if(speed > 15) return 2;
		if(speed > 6) return 1;
		return 0;
	}

	boolean thrownOffFragments = false;

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

		if(!thrownOffFragments && destroyed){
			thrownOffFragments = true;

			for(int i=0;i<50;i++){
				Actor a = new Fragment();
				state.actorAddQueue.add(a.created(state));

				a.body.setTransform(body.getPosition(), MathUtils.random(0, 7));
			}

			state.addMessage("Oh no!", Speaker.RED);
			state.addMessage("I knew this would happen!", Speaker.GREEN);
			state.addMessage("I think we did what we could...", Speaker.BLUE);

			Assets.explosionSound.play();
		}

		if(!destroyed){
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

	}

	@Override
	public Actor created(GameState state) {
		super.created(state);

		for(Subsystem sub : Subsystem.values()){
			subsystemStatus.put(sub, 0);
		}

		body.setTransform(new Vector2(150 * (4.f / 7.f),170 * (4.f / 7.f)), -0.5f);
		body.setLinearVelocity(-18, -18);

		this.rebuildGhostPositions();

		return this;
	}

	public int getContacts(){
		int con = 0;
		for(Contact c : state.world.getContactList()){
			if(c.getFixtureA().getBody() == body || c.getFixtureB().getBody() == body) con++;
		}

		return con;
	}

	@Override
	public void update() {
		if(destroyed) return;

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

				if(ent.getKey() == Subsystem.COMMS && ent.getValue() == 1) state.addMessage("I'm getting a worrying amount\nof static in the R/C system!", Speaker.GREEN);
				if(ent.getKey() == Subsystem.COMMS && ent.getValue() == 2) state.addMessage("I've got no control!!!", Speaker.RED);

				if(ent.getKey() == Subsystem.CONTROL && ent.getValue() == 1) state.addMessage("Whoa, something is wrong\nwith the control system!", Speaker.RED);
				if(ent.getKey() == Subsystem.CONTROL && ent.getValue() == 2) state.addMessage("I didn't do that! What's\nhappening?!?!", Speaker.RED);

				if(ent.getKey() == Subsystem.ENGINE && ent.getValue() == 1) state.addMessage("We're losing engine power.", Speaker.GREEN);
				if(ent.getKey() == Subsystem.ENGINE && ent.getValue() == 2) state.addMessage("Main engine is offline.", Speaker.GREEN);

				if(ent.getKey() == Subsystem.FUEL && ent.getValue() == 1) state.addMessage("Fuel leak.", Speaker.GREEN);
				if(ent.getKey() == Subsystem.FUEL && ent.getValue() == 2) state.addMessage("Fuel leak has gotten larger...", Speaker.GREEN);

				if(ent.getKey() == Subsystem.LEGS && ent.getValue() == 1) state.addMessage("The legs have retracted!", Speaker.GREEN);

				if(ent.getKey() == Subsystem.THRUSTER && ent.getValue() == 1) state.addMessage("RCS power down to 50%.", Speaker.GREEN);
				if(ent.getKey() == Subsystem.THRUSTER && ent.getValue() == 2) state.addMessage("We've lost all RCS power.", Speaker.GREEN);
			}
			if(!state.canSubsystemBreak(ent.getKey()) && MathUtils.randomBoolean(0.3f / 60) && ent.getValue() > 0){
				ent.setValue(ent.getValue() - 1);

				if(ent.getKey() == Subsystem.COMMS && ent.getValue() == 0) state.addMessage("R/C system in the green!", Speaker.GREEN);

				if(ent.getKey() == Subsystem.CONTROL && ent.getValue() == 0) state.addMessage("Whew, my controls are stable\nagain.", Speaker.RED);
				if(ent.getKey() == Subsystem.CONTROL && ent.getValue() == 1) state.addMessage("That's better... A bit.", Speaker.RED);

				if(ent.getKey() == Subsystem.ENGINE && ent.getValue() == 0) state.addMessage("Engine's back in the green.", Speaker.GREEN);
				if(ent.getKey() == Subsystem.ENGINE && ent.getValue() == 1) state.addMessage("I've restored some engine power.", Speaker.GREEN);

				if(ent.getKey() == Subsystem.FUEL && ent.getValue() == 0) state.addMessage("Fuel leak fixed.", Speaker.GREEN);
				if(ent.getKey() == Subsystem.FUEL && ent.getValue() == 1) state.addMessage("Fuel leak is smaller.", Speaker.GREEN);

				if(ent.getKey() == Subsystem.LEGS && ent.getValue() == 0) state.addMessage("Legs back out.", Speaker.GREEN);

				if(ent.getKey() == Subsystem.THRUSTER && ent.getValue() == 0) state.addMessage("RCS back to normal.", Speaker.GREEN);
				if(ent.getKey() == Subsystem.THRUSTER && ent.getValue() == 1) state.addMessage("I've restored half of the RCS\npower.", Speaker.GREEN);
			}
		}

		if(fuel <= 0){ fuel = 0; actualThrusterPower.set(0,0); thrusterPower.set(0,0); }

		if(getContacts() > 0){
			groundCollision();

			if (body.getLinearVelocity().len() < 0.01f){
				state.gameOver();
			}
		}



		super.update();
	}

	public float maxGroundSpeed = -1;

	public boolean destroyed = false;
	public boolean damaged = false;

	public void groundCollision(){
		if(Lander.getImpactResult(body.getLinearVelocity().len()) == 2){ destroyed = true; System.out.println("Destroyed at " + body.getLinearVelocity().len()); }
		if(Lander.getImpactResult(body.getLinearVelocity().len()) == 1){  damaged = true; System.out.println("Damaged at " + body.getLinearVelocity().len()); }

		float ang = body.getAngle();

		while(ang > MathUtils.PI2) ang -= MathUtils.PI2;
		while(ang < -MathUtils.PI2) ang += MathUtils.PI2;

		if(ang > MathUtils.PI / 2 || ang < -MathUtils.PI / 2) destroyed = true;

		if(curLegStatus < 50) destroyed = true;
	}

	public void rebuildGhostPositions(){
		if(destroyed) return;

		Vector2 pos = body.getPosition().cpy();
		float ang = body.getAngle();
		Vector2 linVel = body.getLinearVelocity().cpy();
		float angVel = body.getAngularVelocity();

		maxGroundSpeed = -1;

		for(int i=0;i<60;i++){

			if(this.getContacts() > 0) maxGroundSpeed = Math.max(maxGroundSpeed, body.getLinearVelocity().len());

			this.applyEngineForce();
			state.world.step(0.016f, 1, 1);
		}

		ghost1Pos = body.getPosition().cpy();
		ghost1Angle = body.getAngle();

		ghost1Velocity = body.getLinearVelocity().cpy();

		for(int i=0;i<60;i++){
			if(this.getContacts() > 0) maxGroundSpeed = Math.max(maxGroundSpeed, body.getLinearVelocity().len());

			state.world.step(0.016f, 1, 1);
		}

		ghost2Pos = body.getPosition().cpy();
		ghost2Angle = body.getAngle();

		ghost2Velocity = body.getLinearVelocity().cpy();

		body.setTransform(pos, ang);
		body.setLinearVelocity(linVel);
		body.setAngularVelocity(angVel);

		state.world.setContactListener(state);
	}

	protected void applyEngineForce(){
		body.applyLinearImpulse(new Vector2(
				MathUtils.cos(body.getAngle() + MathUtils.PI / 2) * thrusterPower.y * THRUSTER_MOD * body.getMass() * (1 - (subsystemStatus.get(Subsystem.ENGINE) * 0.5f)),
				MathUtils.sin(body.getAngle() + MathUtils.PI / 2) * thrusterPower.y * THRUSTER_MOD * body.getMass() * (1 - (subsystemStatus.get(Subsystem.ENGINE) * 0.5f))),
				body.getWorldCenter(), true);

		body.applyAngularImpulse(thrusterPower.x * TURN_MOD * (1 - (subsystemStatus.get(Subsystem.THRUSTER) * 0.5f)), true);
	}
}
