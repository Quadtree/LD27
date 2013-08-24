package com.ironalloygames.flameout;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
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
		controller = atlas.createSprite("controller");
		controllerHighlighted = atlas.createSprite("controller_glow");

		newspaperBackground = new Sprite(new Texture(Gdx.files.internal("newspaper-background.png")));

		mono13 = new BitmapFont(Gdx.files.internal("dsm-13-white.fnt"), Gdx.files.internal("dsm-13-white_00.png"), false);
		mono16 = new BitmapFont(Gdx.files.internal("dsm-16-white.fnt"), Gdx.files.internal("dsm-16-white_00.png"), false);

		System.out.println("Load done");
	}
}
