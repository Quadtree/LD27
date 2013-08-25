package com.ironalloygames.flameout;

public class Ut {
	public static String nf(float f){
		String s = "" + f;

		String[] parts = s.split("\\.");

		if(parts.length == 0) return "";

		if(parts.length == 1) return parts[0];

		return parts[0] + "." + parts[1].substring(0, 1);
	}

	public static String nf(int i){
		String s = "" + i;

		while(s.length() < 2) s = "0" + s;

		return s;
	}

	public static String getMissionTime(int ticksRun){
		return "Mission Time: " +
				nf((ticksRun / 60 / 60 / 60 / 24)) + ":" +
				nf((ticksRun / 60 / 60 / 60) % 24) + ":" +
				nf((ticksRun / 60 / 60) % 60) + ":" +
				nf((ticksRun / 60) % 60);
	}
}
