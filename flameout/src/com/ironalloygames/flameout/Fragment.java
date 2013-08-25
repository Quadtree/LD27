package com.ironalloygames.flameout;

import com.badlogic.gdx.math.MathUtils;

public class Fragment extends Actor {

	@Override
	protected float getHalfSize() {
		return 0.5f;
	}

	@Override
	public Actor created(GameState state) {
		super.created(state);

		body.setLinearVelocity(MathUtils.random(-30, 30), MathUtils.random(-30, 30));
		body.setAngularVelocity(MathUtils.random(-10, 10));

		return this;
	}

	@Override
	public void render() {
		state.batch.draw(Assets.fragment, body.getPosition().x, body.getPosition().y, 0.5f, 0.5f, 1, 1, 2, 2, body.getAngle() * (180 / MathUtils.PI) - 90);
		super.render();
	}

}
