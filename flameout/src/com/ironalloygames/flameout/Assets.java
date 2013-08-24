package com.ironalloygames.flameout;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
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

	public static Sprite pebble;

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

		mono13 = new BitmapFont(Gdx.files.internal("dsm-13-white.fnt"), Gdx.files.internal("dsm-13-white_00.png"), false);

		System.out.println("Load done");
	}
}
