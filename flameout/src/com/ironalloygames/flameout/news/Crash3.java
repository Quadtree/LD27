package com.ironalloygames.flameout.news;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;


public class Crash3 extends NewsStory {

	static Texture img;

	public Crash3(){
		headline = "Interplanetary " + rStr(new String[]{"Catastrophe", "Disaser", "Catacylsm", "Debacle"});

		subheadline = rStr(new String[]{
				"Spacecraft Encounters Unexpected Giant Object"

		});

		text = "Today the space agency's spacecraft collided with a mysterious giant object.\n\n" +
				"Scientists are baffled as to the identity of the object.\n\n" +
				"Some have claimed it was a \"Planet\". Agency sources would not comment on the record on these allegations.";

		if (img == null){
			image = new Sprite(new Texture(Gdx.files.internal("lander_destroyed.png")));
		}

		tags.add(Tag.DESTROYED);
	}
}
