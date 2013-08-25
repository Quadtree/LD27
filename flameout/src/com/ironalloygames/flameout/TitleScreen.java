package com.ironalloygames.flameout;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

public class TitleScreen extends GameState {

	OrthographicCamera cam = new OrthographicCamera(800,600);

	int launchStatus = 0;

	int gr = 0;

	@Override
	public Vector2 getMessagePos(){
		return new Vector2(160, 0);
	}

	@Override
	public void update() {
		if(launchStatus == 1) addMessage("T minus 4 seconds...", Speaker.BLUE);

		if(launchStatus == 50) addMessage("Ignition sequence started.", Speaker.GREEN);

		if(launchStatus == 120) addMessage("2...", Speaker.BLUE);

		if(launchStatus == 180) addMessage("1st stage running steady...", Speaker.GREEN);

		if(launchStatus == 240) addMessage("Blastoff!", Speaker.RED);

		if(launchStatus == 340) addMessage("Rocket has cleared\nthe tower.", Speaker.GREEN);

		if(launchStatus > 0)
			launchStatus++;

		if(gr == 0 && launchStatus > 260) gr = 1;
		if(gr == 1 && launchStatus > 360) gr = 2;



		super.update();
	}

	@Override
	public void render() {
		batch.setProjectionMatrix(cam.combined);
		batch.begin();

		if(launchStatus > 500){
			this.beginFade();
		}

		/*if(launchStatus > 500){

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
		}*/

		batch.draw(Assets.launchScreen[gr], -400, -300);

		if(launchStatus == 0){
			Assets.borderedLarge.setColor(Color.WHITE);
			Assets.borderedSmall.setColor(Color.WHITE);
			drawTextCentered(Assets.borderedLarge, "Flameout", 0, 200, 1000);
			drawTextCentered(Assets.borderedSmall, "Made by Quadtree for Ludum Dare 27", 230, -280, 1000);
			drawTextCentered(Assets.borderedSmall, "Press any Key to Begin", -300, -280, 1000);
		}



		batch.end();

		super.render();
	}

	@Override
	public void fullyFaded() {
		FlameoutGame.game.setGameState(new InGameState());
		super.fullyFaded();
	}

	@Override
	public boolean keyDown(int keycode) {
		launchStatus = 1;
		return super.keyDown(keycode);
	}

}
