package com.ironalloygames.flameout.news;


public class Crash1 extends NewsStory {

	public Crash1(){
		headline = "Lander " + rStr(new String[]{"Obliterated", "Annihilated", "Bursted", "Smashed"});

		subheadline = rStr(new String[]{
				"Head of Space Program: " + rStr(new String[]{
						"It wasn't my fault.",
						"Screw this, I'm going home."
				})
		});

		text = "Today the space agency's lander CRASHED on landing.";

		tags.add(Tag.DAMAGED);
		tags.add(Tag.DESTROYED);
	}
}
