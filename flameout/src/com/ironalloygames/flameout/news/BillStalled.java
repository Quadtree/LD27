package com.ironalloygames.flameout.news;


public class BillStalled extends NewsStory {

	String house = null;

	public BillStalled(){
		house = rStr(new String[]{"Congress", "Parlament", "the Dread Council"});

		headline = "Bill Stalled in " + house;

		text = "Today the bill to " + rStr(new String[]{
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
		}) + " when it failed a crucial vote. The bill has come under criticism from " +
		rStr(new String[]{
				"communist groups",
				"environmentalist groups",
				"persons"
		}) + ".";

		tags.add(Tag.FLUFF);
	}

}
