package com.ironalloygames.flameout.news;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;


public class Crash2 extends NewsStory {

	static Texture img;

	public Crash2(){
		headline = "Investigative Committee Finished";

		subheadline = rStr(new String[]{
				"Lander disaster attributed to operator error: Crewmember tempted fate"
		});

		text = "A probe into the cause of the recent lander disaster has found that an error by one of the ground operators was the primary cause of the crash. " +
				"Despite many agency regulations to the contrary, he allegedly said, \"Looks like we have this one in the bag.\" moments before the crash. " +
				"\n\nAn agency spokesperson declined to comment.";

		if (img == null){
			image = new Sprite(new Texture(Gdx.files.internal("lander_destroyed.png")));
		}

		tags.add(Tag.DESTROYED);
	}
}
