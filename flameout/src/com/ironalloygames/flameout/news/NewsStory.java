package com.ironalloygames.flameout.news;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.badlogic.gdx.graphics.g2d.Sprite;

public abstract class NewsStory {
	public enum Tag {
		PERFECT,
		DAMAGED,
		DESTROYED,
		FLUFF
	}

	Random rand = new Random();

	public String headline = "", subheadline = "", text = "";

	public Sprite image;

	public Set<Tag> tags = new HashSet<Tag>();

	protected String rStr(String[] options){
		return options[rand.nextInt(options.length)];
	}
}
