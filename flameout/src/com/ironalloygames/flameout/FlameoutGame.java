package com.ironalloygames.flameout;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ironalloygames.flameout.news.NewsState;
import com.ironalloygames.flameout.news.NewsStory;

public class FlameoutGame implements ApplicationListener {
	private SpriteBatch batch;
	private OrthographicCamera camera;

	public GameState currentGameState;

	private Sprite sprite;

	private Texture texture;

	long msElapsed;

	public static FlameoutGame game;

	@Override
	public void create() {
		game = this;

		/*float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera(1, h / w);
		batch = new SpriteBatch();

		texture = new Texture(Gdx.files.internal("data/libgdx.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		TextureRegion region = new TextureRegion(texture, 0, 0, 512, 275);

		sprite = new Sprite(region);
		sprite.setSize(0.9f, 0.9f * sprite.getHeight() / sprite.getWidth());
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		sprite.setPosition(-sprite.getWidth() / 2, -sprite.getHeight() / 2);*/

		Assets.load();

		//currentGameState = new InGameState().created();
		setGameState(new NewsState(NewsStory.Tag.DAMAGED));

		msElapsed = System.currentTimeMillis();
	}

	public void setGameState(GameState state){
		currentGameState = state.created();

		Gdx.input.setInputProcessor(currentGameState);
	}

	@Override
	public void dispose() {
		//batch.dispose();
		//texture.dispose();
	}

	@Override
	public void pause() {
	}

	@Override
	public void render() {
		/*Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		sprite.draw(batch);
		batch.end();*/

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		currentGameState.render();

		if(msElapsed < System.currentTimeMillis()){
			msElapsed += 16;
			currentGameState.update();
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void resume() {
	}
}
