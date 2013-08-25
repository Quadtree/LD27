package com.ironalloygames.flameout;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

public class CruiseScreen extends GameState {
	OrthographicCamera cam = new OrthographicCamera(800,600);

	int gr;
	int tm = 0;

	@Override
	public void update() {
		tm++;

		if(tm == 1) addMessage("2 seconds to cruise stage\nshutdown.", Speaker.BLUE);
		if(tm == 110) addMessage("Shutting down.", Speaker.RED);
		if(tm == 140) addMessage("Cruise engine shutdown\nsuccessful.", Speaker.GREEN);
		if(tm == 170) addMessage("Perfect! This mission\nis going great.", Speaker.RED);
		if(tm == 190) addMessage("Don't jinx it.", Speaker.GREEN);
		if(tm == 230) addMessage("Trajectory looks good. We're\non approach to the landing\nzone.", Speaker.BLUE);
		if(tm == 380) beginFade();

		if(tm == 120){
			gr = 1;
		}

		super.update();
	}

	@Override
	public void fullyFaded() {
		FlameoutGame.game.setGameState(new DescentScreen());
		super.fullyFaded();
	}

	@Override
	public void render() {
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		batch.draw(Assets.cruiseScreen[gr], -400, -300);
		batch.end();
		super.render();
	}

	@Override
	public Vector2 getMessagePos() {
		return new Vector2(-350, 250);
	}

}
