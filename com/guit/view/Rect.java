package com.guit.view;

public class Rect {

	public float x, y, width, height;

	public Rect(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public boolean collides(float x, float y) {
		return (x >= this.x && x < this.x + this.width && y >= this.y && y < this.y + this.height);
	}
}
