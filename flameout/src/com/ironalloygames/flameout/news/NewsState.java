package com.ironalloygames.flameout.news;

import java.util.Random;

import com.ironalloygames.flameout.GameState;
import com.ironalloygames.flameout.news.NewsStory.Tag;

public class NewsState extends GameState {
	Random rand = new Random();

	public NewsState(Tag leadingStoryTag){
		NewsStory[] stories = {
				new Crash1(),
				new BillStalled()
		};

		int n = 1;

		for(NewsStory ns : stories){
			if(ns.getTags().contains(leadingStoryTag) && rand.nextInt(n++) == 0){
				leadingStory = ns;
			}
		}

		n = 1;

		for(NewsStory ns : stories){
			if(ns.getTags().contains(Tag.FLUFF) && rand.nextInt(n++) == 0){
				secondStory = ns;
			}
		}

		System.out.println("Newspaper: " + leadingStory + " " + secondStory);
	}

	NewsStory leadingStory;
	NewsStory secondStory;

	@Override
	public void update() {

	}
	@Override
	public void render() {

	}


}
