package com.ironalloygames.flameout.news;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;


public class Landed2 extends NewsStory {

	static Texture img;

	public Landed2(){
		headline = "Parade for Lander Crew Today";

		subheadline = rStr(new String[]{
				"President", "Prime Minister", "King", "Queen", "Emperor"
		}) + " to Meet Heroic Crew";

		text = "The parade for the ground crew of the lander that recently landed on an alien world is set for today.\n\n" +
				"The crew has been the talk of the nation for the last month after the landing.\n\n" +
				"Despite multiple system failures, they were able to bring the lander and its scientific payload safely to the ground.";

		if (img == null){
			image = new Sprite(new Texture(Gdx.files.internal("lander_landed.png")));
		}

		tags.add(Tag.PERFECT);
	}
}
