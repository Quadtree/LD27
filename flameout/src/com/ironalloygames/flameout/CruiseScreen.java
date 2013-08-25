package com.ironalloygames.flameout;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

public class CruiseScreen extends GameState {
	OrthographicCamera cam = new OrthographicCamera(800,600);

	int gr;
	int tm = 0;
	int ticksRun = (86400 * 18 + 39584) * 60 - (60*60*60*5);

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

		ticksRun++;

		super.update();
	}

	@Override
	public void fullyFaded() {
		FlameoutGame.game.setGameState(new DescentScreen());
		super.fullyFaded();
	}

	@Override
	public GameState created() {
		FlameoutGame.game.setMusic(Assets.spaceMusic, 0.5f);
		return super.created();
	}

	@Override
	public void render() {
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		batch.draw(Assets.cruiseScreen[gr], 0, 0, 0.5f, 0.5f, 1, 1, 800, 600, 180, 0, 0, 800, 600, false, false);
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
