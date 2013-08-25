package com.ironalloygames.flameout.news;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;


public class Damaged1 extends NewsStory {

	static Texture img;

	public Damaged1(){
		headline = "Hard Lander Landing";

		subheadline = rStr(new String[]{
				"Chief Science Officer: I can't work with this!",
				"Scientific Payload Destroyed",
				"Extremely Expensive Scientific Equipment Destroyed"
		});

		text = "The space agency's " + MathUtils.random(25, 320) + " million dollar lander crash-landed on its destination. " +
				"Most of its scientific instruments were destroyed, but officals said that " +
				rStr(new String[]{"", "it was possible that", "there was a small chance that"}) + " at least one " + rStr(new String[]{"instrument", "system", "component"}) + " survived." +
				"\n\nAn agency spokesperson declined to comment.";

		if (img == null){
			image = new Sprite(new Texture(Gdx.files.internal("lander_damaged.png")));
		}

		tags.add(Tag.DAMAGED);
	}
}
