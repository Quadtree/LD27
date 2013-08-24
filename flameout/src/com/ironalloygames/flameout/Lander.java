package com.ironalloygames.flameout;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Lander extends Actor {

	private static final float THRUSTER_MOD = 0.7f;
	private static final float TURN_MOD = 3.0f;
	Vector2 ghost1Pos = null;
	Vector2 ghost2Pos = null;
	float ghost1Angle = 0;
	float ghost2Angle = 0;

	public Vector2 thrusterPower = new Vector2();

	@Override
	public void render() {
		super.render();

		state.batch.draw(
				Assets.landerEngineHigh.get(Assets.landerEngineHigh.size() - 1), body.getPosition().x, body.getPosition().y,
				0.5f, 0.5f, 1, 1, getHalfSize() * 3, getHalfSize() * 3, body.getAngle() * (180 / MathUtils.PI) - 90, false
				);

		state.batch.draw(
				Assets.landerOutline, ghost1Pos.x, ghost1Pos.y,
				0.5f, 0.5f, 1, 1, getHalfSize() * 3, getHalfSize() * 3, ghost1Angle * (180 / MathUtils.PI) - 90, false
				);

		state.batch.draw(
				Assets.landerOutline, ghost2Pos.x, ghost2Pos.y,
				0.5f, 0.5f, 1, 1, getHalfSize() * 3, getHalfSize() * 3, ghost2Angle * (180 / MathUtils.PI) - 90, false
				);


	}

	@Override
	public Actor created(GameState state) {
		super.created(state);

		body.setTransform(new Vector2(100 * (4.f / 7.f),110 * (4.f / 7.f)), -0.5f);
		body.setLinearVelocity(-6, 6);

		this.rebuildGhostPositions();

		return this;
	}

	@Override
	public void update() {
		this.applyEngineForce();
		super.update();
	}

	public void rebuildGhostPositions(){
		System.out.println("REBUILDING");

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

		for(int i=0;i<60;i++){
			state.world.step(0.016f, 1, 1);
		}

		ghost2Pos = body.getPosition().cpy();
		ghost2Angle = body.getAngle();

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
