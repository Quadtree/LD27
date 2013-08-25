package com.ironalloygames.flameout;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class TitleScreen extends GameState {

	OrthographicCamera cam = new OrthographicCamera(800,600);

	int launchStatus = 0;

	int gr = 0;

	@Override
	public void update() {
		if(launchStatus > 0)
			launchStatus++;

		if(gr == 0 && launchStatus > 240) gr = 1;
		if(gr == 1 && launchStatus > 360) gr = 2;

		super.update();
	}

	@Override
	public void render() {
		batch.setProjectionMatrix(cam.combined);
		batch.begin();

		if(launchStatus > 500){

			System.out.println(1 - ((float)launchStatus - 500) / 60);

			batch.setColor(
					1 - ((float)launchStatus - 500) / 60,
					1 - ((float)launchStatus - 500) / 60,
					1 - ((float)launchStatus - 500) / 60,
					1
					);
		}

		if(launchStatus > 558){
			FlameoutGame.game.setGameState(new InGameState());
		}

		batch.draw(Assets.launchScreen[gr], -400, -300);

		if(launchStatus == 0){
			drawTextCentered(Assets.borderedLarge, "Flameout", 0, 200, 1000);
			drawTextCentered(Assets.borderedSmall, "Made by Quadtree for Ludum Dare 27", 230, -280, 1000);
			drawTextCentered(Assets.borderedSmall, "Press any Key to Begin", -300, -280, 1000);
		}



		batch.end();

		super.render();
	}

	@Override
	public boolean keyDown(int keycode) {
		launchStatus = 1;
		return super.keyDown(keycode);
	}

	void drawTextCentered(BitmapFont font, String s, int x, int y, int wrapWidth){
		font.drawWrapped(batch, s, x - font.getWrappedBounds(s, wrapWidth).width / 2, y, wrapWidth);
	}

}
