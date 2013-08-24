package com.ironalloygames.flameout;

import com.badlogic.gdx.tools.imagepacker.TexturePacker2;

public class Pack {

	public static void main(String[] args) {
		TexturePacker2.process("../flameout/img_raw",
				"../flameout-android/assets", "atlas");
	}

}
