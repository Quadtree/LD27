package com.ironalloygames.flameout.news;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;


public class Landed1 extends NewsStory {

	static Texture img;

	public Landed1(){
		headline = "Lander Lands Perfectly";

		subheadline = rStr(new String[]{
				"Head of Space Program: " + rStr(new String[]{
						"I trained them well.",
						"Perfect planning paid off."
				})
		});

		text = "Today the space agency's lander touched down on an alien world perfectly. Over the next few days the lander will deploy a variety of scientific instruments.\n\n" +
				"According to the space agency, searching for " + rStr(new String[]{"alien life", "minerals", "fresh meat"}) + " will be the main focus of the research.\n\n";

		if (img == null){
			image = new Sprite(new Texture(Gdx.files.internal("lander_landed.png")));
		}

		tags.add(Tag.PERFECT);
	}
}
