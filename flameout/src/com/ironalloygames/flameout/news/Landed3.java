package com.ironalloygames.flameout.news;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;


public class Landed3 extends NewsStory {

	static Texture img;

	public Landed3(){
		headline = "Alien Life Discovered";

		subheadline = "Chief Scientist: We're not alone";

		text = "Just one week after landing on an alien world, the space agency's lander has found definitive evidence of alien life.\n\n" +
				"Speculation has already begun that the alien race is preparing to attack. Some conspiracy theorists believe the aliens are already among us.";

		if (img == null){
			image = new Sprite(new Texture(Gdx.files.internal("lander_landed.png")));
		}

		tags.add(Tag.PERFECT);
	}
}
