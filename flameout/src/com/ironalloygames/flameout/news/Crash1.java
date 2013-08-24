package com.ironalloygames.flameout.news;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class Crash1 extends NewsStory {

	@Override
	public String getHeadline() {
		return "Lander " + rStr(new String[]{"Obliterated", "Annihilated", "Bursted", "Smashed"}) + " in Crash";
	}

	@Override
	public String getSubheadline() {
		return rStr(new String[]{
				"Head of Space Program: " + rStr(new String[]{
						"It wasn't my fault.",
						"Screw this, I'm going home."
				})
		});
	}

	@Override
	public String getText() {
		return "Today the space agency's lander CRASHED on landing.";
	}

	@Override
	public Sprite getImage() {
		return super.getImage();
	}

	@Override
	public Set<Tag> getTags() {
		HashSet<Tag> tags = new HashSet<Tag>();
		tags.add(Tag.DAMAGED);
		tags.add(Tag.DESTROYED);
		return tags;
	}

}
