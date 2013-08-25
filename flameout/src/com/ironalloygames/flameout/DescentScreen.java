package com.ironalloygames.flameout;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class DescentScreen extends GameState {
	OrthographicCamera uiCamera = new OrthographicCamera(800,600);

	int tm;

	Sprite sp;

	boolean shake = false;

	int ticksRun = (86400 * 18 + 39584) * 60 - (60*60*15);

	@Override
	public GameState created() {
		sp = Assets.landerEngineOff.get(0);
		FlameoutGame.game.setMusic(Assets.spaceMusic, 0.5f);
		return super.created();
	}

	@Override
	public void update() {

		if(tm == 1) addMessage("Descent vector looks good.\nT minus 2 to final\nslowdown burn.", Speaker.BLUE);
		if(tm == 20) addMessage("Got it!", Speaker.RED);
		if(tm == 50) addMessage("Radar indicates the LZ is too rocky.\n", Speaker.GREEN);
		if(tm == 120){ addMessage("Ignition!", Speaker.RED); sp = Assets.landerEngineHigh.get(0); shake = true; }
		if(tm == 260){ addMessage("We'll just keep flying\nuntil we find a new LZ.", Speaker.BLUE); }
		if(tm == 280){ addMessage("Engine to hover power!", Speaker.RED); sp = Assets.landerEngineLow.get(0); shake = false; }
		if(tm == 400) this.beginFade();

		tm++;
		ticksRun++;

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

		batch.draw(sp, 0 + (shake ? MathUtils.random(-2, 2) : 0), 0 + (shake ? MathUtils.random(-2, 2) : 0), 0.5f, 0.5f, 1, 1, 64, 64, 300);

		StringBuilder sb = new StringBuilder();
		sb.append(String.format("Mission Time: %02d:%02d:%02d:%02d\n", (ticksRun / 60 / 60 / 60 / 24) % 30, (ticksRun / 60 / 60 / 60) % 24, (ticksRun / 60 / 60) % 60, (ticksRun / 60) % 60));

		Assets.mono13.drawMultiLine(batch, sb, 160, 280);

		batch.end();
		super.render();
	}

	@Override
	public Vector2 getMessagePos() {
		return new Vector2(-350, 250);
	}
}
