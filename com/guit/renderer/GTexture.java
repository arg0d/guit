package com.guit.renderer;

public class GTexture {

	public String textureID;
	public float width, height;

	public GTexture(String textureID, float width, float height) {
		this.textureID = textureID;
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return (int) width;
	}

	public int getHeight() {
		return (int) height;
	}

}
