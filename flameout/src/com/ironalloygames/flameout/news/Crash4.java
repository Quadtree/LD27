package com.ironalloygames.flameout.news;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;


public class Crash4 extends NewsStory {

	static Texture img;

	public Crash4(){
		headline = "Splat";

		subheadline = rStr(new String[]{
				"Space Agency Lander Slams into Planet",
				"Lander Collides with Destination"
		});

		text = "Today the space agency reported that their lander had contacted the destination planet.\n\n" +
				"\"Unfortunately, it was going a little fast. So now its in... a few pieces\", said an agency spokesperson, \"But we're still trying to salvage useful scientific information\"\n\n" +
				"A leading scientist claimed that they might get useful information on high-speed crash physics.";

		if (img == null){
			image = new Sprite(new Texture(Gdx.files.internal("lander_destroyed.png")));
		}

		tags.add(Tag.DESTROYED);
	}
}
