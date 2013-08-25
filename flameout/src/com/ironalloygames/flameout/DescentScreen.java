package com.ironalloygames.flameout;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class DescentScreen extends GameState {
	OrthographicCamera uiCamera = new OrthographicCamera(800,600);

	int tm;

	Sprite sp;

	@Override
	public GameState created() {
		sp = Assets.landerEngineOff.get(0);
		return super.created();
	}

	@Override
	public void update() {

		if(tm == 1) addMessage("Descent vector looks good.\nT minus 3 to final\nslowdown burn.", Speaker.BLUE);
		if(tm == 20) addMessage("Got it!", Speaker.RED);
		if(tm == 50) addMessage("Radar indicates the LZ is too rocky.\n", Speaker.GREEN);
		if(tm == 120){ addMessage("Ignition!", Speaker.RED); sp = Assets.landerEngineHigh.get(0); }
		if(tm == 90){ addMessage("We'll just keep flying\nuntil we find a new LZ.", Speaker.BLUE); }
		if(tm == 280){ addMessage("Engine cutoff!", Speaker.RED); sp = Assets.landerEngineOff.get(0); }
		if(tm == 400) this.beginFade();

		tm++;

		super.update();
	}

	@Override
	public void fullyFaded() {
		FlameoutGame.game.setGameState(new InGameState());
		super.fullyFaded();
	}

	@Override
	public void render() {
		batch.setProjectionMatrix(uiCamera.combined);
		batch.begin();

		batch.draw(Assets.starfield, -400, -300, 800, 600);

		batch.draw(sp, 0, 0, 0.5f, 0.5f, 1, 1, 64, 64, 300);

		batch.end();
		super.render();
	}

	@Override
	public Vector2 getMessagePos() {
		return new Vector2(-350, 250);
	}
}
