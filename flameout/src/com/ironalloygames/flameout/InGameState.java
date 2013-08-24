package com.ironalloygames.flameout;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;


public class InGameState extends GameState {
	public OrthographicCamera camera;

	public int ticksToRun = 60;

	public InGameState(){

	}

	@Override
	public GameState created(){
		world = new World(new Vector2(0, -9.8f), false);

		camera = new OrthographicCamera(1,1);

		actors.add(new Lander().created(this));

		return super.created();
	}

	@Override
	public void update() {
		if(ticksToRun > 0){
			super.update();
			ticksToRun--;
		}
	}

	@Override
	public void render() {
		camera.setToOrtho(false, 800 / 4, 600 / 4);

		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		super.render();

		batch.end();
	}


}
