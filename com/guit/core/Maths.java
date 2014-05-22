package com.guit.core;

public class Maths {

	public static int dir(float f) {
		if (f < 0) return -1;
		if (f > 0) return 1;
		return 0;
	}

	public static boolean inRange(float f, float r0, float r1) {
		return f >= r0 && f < r1;
	}

	public static float abs(float f) {
		return f < 0 ? -f : f;
	}

}
