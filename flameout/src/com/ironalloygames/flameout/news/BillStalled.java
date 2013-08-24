package com.ironalloygames.flameout.news;

import java.util.HashSet;
import java.util.Set;

public class BillStalled extends NewsStory {

	String house = null;

	public BillStalled(){
		house = rStr(new String[]{"Congress", "Parlament", "the Dread Council"});
	}

	@Override
	public String getHeadline() {
		return "Bill Stalled in " + house;
	}

	@Override
	public String getText() {
		return "Today the bill to " + rStr(new String[]{
				"ban the sale of " + rStr(new String[]{
						"tortoises",
						"grapefruit",
						"edibility enchancers",
						"face augmentations",
						"blocks",
						"rocks"
				}),
				"fund the new " + rStr(new String[]{
						"stadium",
						"space mission",
						"lander",
						"flying machine",
						"research"
				})
		}) + " " +
		rStr(new String[]{
				"foundered",
				"was crushed",
				"fell apart"
		}) + " when it failed a crucial vote. The bill has come under criticism from ...";
	}

	@Override
	public Set<Tag> getTags() {
		HashSet<Tag> tags = new HashSet<Tag>();
		tags.add(Tag.FLUFF);
		return tags;
	}

}
