package com.ironalloygames.flameout;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;


public class InGameState extends GameState {
	public OrthographicCamera camera;
	public OrthographicCamera uiCamera;

	public int ticksToRun = 60;

	Lander lander;

	public InGameState(){

	}

	@Override
	public GameState created(){
		world = new World(new Vector2(0, -9.8f), false);

		camera = new OrthographicCamera(1,1);
		lander = (Lander)new Lander().created(this);
		actors.add(lander);

		uiCamera = new OrthographicCamera(800,600);

		return super.created();
	}

	@Override
	public void update() {
		if(ticksToRun == 1){
			lander.rebuildGhostPositions();
		}

		if(ticksToRun > 0){
			super.update();
			ticksToRun--;
		}

		if (increaseThrust) lander.thrusterPower.y = Math.min(lander.thrusterPower.y + 0.01f, 1);
		if (decreaseThrsut) lander.thrusterPower.y = Math.min(lander.thrusterPower.y - 0.01f, 1);

		if (turnLeft) lander.thrusterPower.x = Math.min(lander.thrusterPower.x + 0.01f, 1);
		if (turnRight) lander.thrusterPower.x = Math.min(lander.thrusterPower.x - 0.01f, 1);



		if(increaseThrust && decreaseThrsut && turnLeft && turnRight) lander.rebuildGhostPositions();
	}

	@Override
	public void render() {
		camera.setToOrtho(false, 800 / 7, 600 / 7);

		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		super.render();

		batch.end();

		batch.setProjectionMatrix(uiCamera.combined);
		batch.begin();
		Assets.mono13.drawMultiLine(batch, "That GUY\nEeeeep!", 200, 250);
		Assets.mono13.setColor(Color.GREEN);
		batch.end();
	}

	boolean increaseThrust = false;
	boolean decreaseThrsut = false;
	boolean turnLeft = false;
	boolean turnRight = false;

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.W) increaseThrust = true;
		if (keycode == Keys.S) decreaseThrsut = true;
		if (keycode == Keys.A) turnLeft = true;
		if (keycode == Keys.D) turnRight = true;

		return super.keyDown(keycode);
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.W) increaseThrust = false;
		if (keycode == Keys.S) decreaseThrsut = false;
		if (keycode == Keys.A) turnLeft = false;
		if (keycode == Keys.D) turnRight = false;

		return super.keyUp(keycode);
	}


}
