package com.ironalloygames.flameout;

import java.util.ArrayList;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;

public class GameState implements InputProcessor {
	public ArrayList<Actor> actors = new ArrayList<Actor>();
	public World world;
	public SpriteBatch batch;

	public boolean canSubsystemBreak(Lander.Subsystem sub){ return false; }

	public GameState created(){
		batch = new SpriteBatch();
		return this;
	}

	public void update(){
		for(int i=0;i<actors.size();i++){
			actors.get(i).update();
		}

		world.step(0.016f, 4, 4);
	}

	public void render(){
		for(Actor a : actors){
			a.render();
		}
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}
