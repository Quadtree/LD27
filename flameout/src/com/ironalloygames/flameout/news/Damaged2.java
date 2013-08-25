package com.ironalloygames.flameout.news;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;


public class Damaged2 extends NewsStory {

	static Texture img;

	public Damaged2(){
		headline = rStr(new String[]{"Bad Bounce", "Faceplant"});

		subheadline = rStr(new String[]{
				"Expensive Lander Crash-Lands on Planet",
				"Official: Lander Basically Trashed"
		});

		text = "The space agency's much heralded lander touched down on an alien world... a bit too hard.\n\n" +
				"Officials claimed that the unplanned velocity had \"Some detrimental effects,\" on the scientific payload.";

		if (img == null){
			image = new Sprite(new Texture(Gdx.files.internal("lander_damaged.png")));
		}

		tags.add(Tag.DAMAGED);
	}
}
