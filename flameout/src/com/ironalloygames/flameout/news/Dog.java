package com.ironalloygames.flameout.news;


public class Dog extends NewsStory {

	String house = null;

	private String randNation(){ return rStr(new String[]{"Bigholm", "Worgon", "Vorpid", "Zakarul", "Oppinix"}); }

	public Dog(){
		headline = "Its WAR!!!";

		String nation1 = randNation();

		String nation2 = null;

		do
		{
			nation2 = randNation();
		} while(nation1.equals(nation2));


		text = "War has broken out between " + nation1 + " and " + nation2 + " over " + rStr(new String[]{"mineral rights", "oil", "food", "water", "gold", "paper", "vague disagreements"}) +
				". Other states hope to somehow bring the warring parties into negotiations.";


		tags.add(Tag.FLUFF);
	}

}
