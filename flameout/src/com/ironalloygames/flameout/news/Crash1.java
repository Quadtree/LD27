package com.ironalloygames.flameout.news;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;


public class Crash1 extends NewsStory {

	static Texture img;

	public Crash1(){
		headline = "Lander " + rStr(new String[]{"Obliterated", "Annihilated", "Bursted", "Smashed"}) + " in Crash";

		subheadline = rStr(new String[]{
				"Head of Space Program: " + rStr(new String[]{
						"It wasn't my fault.",
						"Screw this, I'm going home."
				})
		});

		text = "Today the space agency's lander " + rStr(new String[]{"detonated", "crashed", "exploded", "blew up"}) + " on landing.\n\n" +
				"A spokesperson for the agency said that the cause was under investigation.\n\n" +
				"\"This " + rStr(new String[]{"cataclysm", "debacle", "fiasco"}) + " has called the space agency's whole budget into question,\" a government source was quoted as saying.\n\n" +
				"Three committees have already been formed to investigate the " + rStr(new String[]{"cataclysm", "debacle", "fiasco"}) + ".";

		if (img == null){
			image = new Sprite(new Texture(Gdx.files.internal("lander_destroyed.png")));
		}

		tags.add(Tag.DESTROYED);
	}
}
