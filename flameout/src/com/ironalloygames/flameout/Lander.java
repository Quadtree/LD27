package com.ironalloygames.flameout;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Lander extends Actor {

	@Override
	public void render() {
		super.render();

		state.batch.draw(
				Assets.landerEngineHigh.get(Assets.landerEngineHigh.size() - 1), body.getPosition().x, body.getPosition().y,
				0.5f, 0.5f, 1, 1, getHalfSize() * 3, getHalfSize() * 3, body.getAngle() * (180 / MathUtils.PI) - 90, false
				);
	}

	@Override
	public Actor created(GameState state) {
		super.created(state);

		body.setTransform(new Vector2(100,130), 0.5f);
		body.setLinearVelocity(-6, -1);

		return this;
	}

}
