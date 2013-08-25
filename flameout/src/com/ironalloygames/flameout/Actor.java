package com.ironalloygames.flameout;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public abstract class Actor {
	protected Body body;
	protected GameState state;

	protected float getHalfSize(){
		return 4;
	}

	public Actor created(GameState state){
		this.state = state;

		BodyDef bd = new BodyDef();
		bd.allowSleep = false;
		bd.type = BodyDef.BodyType.DynamicBody;

		body = state.world.createBody(bd);

		FixtureDef fd = new FixtureDef();

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(getHalfSize(), getHalfSize());

		fd.shape = shape;
		fd.density = 1;
		fd.friction = 1;

		body.createFixture(fd);

		return this;
	}

	public void update(){}
	public void render(){}
	public boolean keep(){ return true; }
	public void destroyed(){}
}
