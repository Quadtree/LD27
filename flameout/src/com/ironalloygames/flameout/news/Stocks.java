package com.ironalloygames.flameout.news;

import com.badlogic.gdx.math.MathUtils;


public class Stocks extends NewsStory {

	String house = null;

	public Stocks(){
		headline = "Stocks Plummet Again";

		text = "There has been another massive drop in the stock market, by about " + MathUtils.random(100, 1200) + " points just today. Experts are baffled by this market volatility.";


		tags.add(Tag.FLUFF);
	}

}
