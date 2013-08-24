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

	protected String rStr(String[] options){
		return options[rand.nextInt(options.length)];
	}

	public String getHeadline(){ return ""; }
	public String getSubheadline(){ return ""; }
	public String getText(){ return ""; }
	public Sprite getImage(){ return null; }
	public Set<Tag> getTags(){ return new HashSet<Tag>(); }
}
