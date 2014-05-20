package com.guit.renderer;

public class GColor {

	public static final GColor WHITE = new GColor(1, 1, 1);

	public float r, g, b, a;

	public GColor(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}

	public GColor(float r, float g, float b) {
		this(r, g, b, 1);
	}

}
