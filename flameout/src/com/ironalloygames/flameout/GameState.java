package com.ironalloygames.flameout;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;

public class GameState {
	public ArrayList<Actor> actors = new ArrayList<Actor>();
	public World world;
	public SpriteBatch batch;

	public GameState created(){
		batch = new SpriteBatch();
		return this;
	}

	public void update(){
		for(int i=0;i<actors.size();i++){
			actors.get(i).update();
		}
	}

	public void render(){
		for(Actor a : actors){
			a.render();
		}
	}
}
