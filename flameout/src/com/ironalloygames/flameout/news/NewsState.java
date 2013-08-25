package com.ironalloygames.flameout.news;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.ironalloygames.flameout.GameState;
import com.ironalloygames.flameout.news.NewsStory.Tag;

public class NewsState extends GameState {
	Random rand = new Random();

	OrthographicCamera camera = new OrthographicCamera(100000,100000);

	static BitmapFont headline, subheadline, regular;

	static Texture background;

	String newspaperName = "";

	float zoom = 0.01f;

	protected String rStr(String[] options){
		return options[rand.nextInt(options.length)];
	}

	public NewsState(Tag leadingStoryTag){
		NewsStory[] stories = {
				new Crash1(),
				new BillStalled()
		};

		int n = 1;

		for(NewsStory ns : stories){
			if(ns.tags.contains(leadingStoryTag) && rand.nextInt(n++) == 0){
				leadingStory = ns;
			}
		}

		n = 1;

		for(NewsStory ns : stories){
			if(ns.tags.contains(Tag.FLUFF) && rand.nextInt(n++) == 0){
				secondStory = ns;
			}
		}

		if (headline == null){
			headline = new BitmapFont(Gdx.files.internal("ns-headline.fnt"), Gdx.files.internal("ns-headline_00.png"), false);
			subheadline = new BitmapFont(Gdx.files.internal("ns-subheadline.fnt"), Gdx.files.internal("ns-subheadline_00.png"), false);
			regular = new BitmapFont(Gdx.files.internal("ns-regular.fnt"), Gdx.files.internal("ns-regular_00.png"), false);

			background = new Texture(Gdx.files.internal("newspaper-background.png"));
		}

		newspaperName = "The " + rStr(new String[]{"Daily", "Weekly", "Valiant", "Worldwide"}) + " " + rStr(new String[]{"Herald", "Devastator", "Truthifier", "Chronicle", "Surprise", "Ambush"});

		System.out.println("Newspaper: " + leadingStory + " " + secondStory);
	}

	NewsStory leadingStory;
	NewsStory secondStory;

	@Override
	public void update() {
		zoom *= 1.2f;
		zoom = Math.min(zoom, 1);

		camera = new OrthographicCamera(800,600);
		camera.zoom = 1 / zoom;

		if(zoom < 1)
			camera.rotate(MathUtils.random(0, 360));

		camera.update();
	}

	void drawTextCentered(BitmapFont font, String s, int x, int y, int wrapWidth){
		font.setColor(Color.BLACK);
		font.drawWrapped(batch, s, x - font.getWrappedBounds(s, wrapWidth).width / 2, y, wrapWidth);
	}


	@Override
	public void render() {
		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		batch.draw(background, -background.getWidth() / 2, -background.getHeight() / 2);


		drawTextCentered(subheadline, newspaperName, 0, 263, 544);

		drawTextCentered(headline, leadingStory.headline, 0, 230, 544);

		drawTextCentered(subheadline, leadingStory.subheadline, 0, 175, 544);

		regular.setColor(Color.BLACK);

		regular.drawWrapped(batch, leadingStory.text, -255, 95, 230);

		drawTextCentered(subheadline, secondStory.headline, -20 + (270 / 2), -90, 270);

		regular.drawWrapped(batch, secondStory.text, -20, -150, 270);

		batch.draw(leadingStory.image, -20, 95 - 178, 267, 178);

		batch.end();
	}


}
