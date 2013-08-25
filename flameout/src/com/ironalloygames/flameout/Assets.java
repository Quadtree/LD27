package com.ironalloygames.flameout;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Assets {
	static TextureAtlas atlas;

	public static ArrayList<Sprite> landerEngineOff = new ArrayList<Sprite>();
	public static ArrayList<Sprite> landerEngineLow = new ArrayList<Sprite>();
	public static ArrayList<Sprite> landerEngineHigh = new ArrayList<Sprite>();
	public static Sprite landerOutline;

	public static BitmapFont mono13;
	public static BitmapFont mono16;

	public static Sprite pebble;

	public static Sprite tooltip;

	public static Sprite controller;

	public static Sprite controllerHighlighted;

	public static Sprite newspaperBackground;

	public static Texture[] launchScreen;
	public static Texture[] cruiseScreen;

	public static BitmapFont borderedLarge;
	public static BitmapFont borderedSmall;

	public static Sprite solid;

	public static Texture starfield;

	public static Sprite fragment;

	public static Sound launchSound;

	public static Sound explosionSound;

	public static Sprite landerSpinClockwise;
	public static Sprite landerSpinCounterclockwise;

	public static void load(){
		atlas = new TextureAtlas(Gdx.files.internal("atlas.atlas"));

		for(int i=0;i<8;i++){
			int j = (i * 4) + 1;

			System.out.println("X " + j);

			landerEngineOff.add(atlas.createSprite("lander_engine_off" + j));
			landerEngineLow.add(atlas.createSprite("lander_engine_low" + j));
			landerEngineHigh.add(atlas.createSprite("lander_engine_high" + j));
		}

		landerOutline = atlas.createSprite("lander_outline");

		pebble = atlas.createSprite("pebble");
		tooltip = atlas.createSprite("tooltip");
		solid = atlas.createSprite("solid");
		controller = atlas.createSprite("controller");
		controllerHighlighted = atlas.createSprite("controller_glow");
		fragment = atlas.createSprite("fragment");

		starfield = new Texture(Gdx.files.internal("starfield.png"));

		newspaperBackground = new Sprite(new Texture(Gdx.files.internal("newspaper-background.png")));

		mono13 = new BitmapFont(Gdx.files.internal("dsm-13-white.fnt"), Gdx.files.internal("dsm-13-white_00.png"), false);
		mono16 = new BitmapFont(Gdx.files.internal("dsm-16-white.fnt"), Gdx.files.internal("dsm-16-white_00.png"), false);

		launchScreen = new Texture[]{
				new Texture(Gdx.files.internal("launch0.png")),
				new Texture(Gdx.files.internal("launch1.png")),
				new Texture(Gdx.files.internal("launch2.png"))
		};

		cruiseScreen = new Texture[]{
				new Texture(Gdx.files.internal("cruise0.png")),
				new Texture(Gdx.files.internal("cruise1.png"))
		};

		borderedLarge = new BitmapFont(Gdx.files.internal("large-bordered.fnt"), Gdx.files.internal("large-bordered_00.png"), false);
		borderedSmall = new BitmapFont(Gdx.files.internal("small-bordered.fnt"), Gdx.files.internal("small-bordered_00.png"), false);

		launchSound = Gdx.audio.newSound(Gdx.files.internal("launch.ogg"));
		explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.wav"));

		landerSpinClockwise = atlas.createSprite("lander_spin_clockwise");
		landerSpinCounterclockwise = atlas.createSprite("lander_spin_counterclockwise");

		System.out.println("Load done");
	}
}
