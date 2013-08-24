package com.ironalloygames.flameout.news;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;


public class Crash1 extends NewsStory {

	static Texture img;

	public Crash1(){
		headline = "Lander " + rStr(new String[]{"Obliterated", "Annihilated", "Bursted", "Smashed"});

		subheadline = rStr(new String[]{
				"Head of Space Program: " + rStr(new String[]{
						"It wasn't my fault.",
						"Screw this, I'm going home."
				})
		});

		text = "Today the space agency's lander CRASHED on landing.";

		if (img == null){
			image = new Sprite(new Texture(Gdx.files.internal("lander_destroyed.png")));
		}

		tags.add(Tag.DAMAGED);
		tags.add(Tag.DESTROYED);
	}
}
