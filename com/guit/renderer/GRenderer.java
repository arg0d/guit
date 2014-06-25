package com.guit.renderer;

import com.guit.core.vec2;

public abstract class GRenderer {

	public static final String BAD_VERTEX_AMOUNT_MESSAGE = "The vertex array specified is incorrect. Length has to be 8 (4 vertices each having 2 components(x, y))";
	public static final String BAD_TEXTURE_AMOUNT_MESSAGE = "The texture array specified is incorrect. Length has to be 8 (4 textures each having 2 components(u, v))";
	public static final String BAD_COLOR_AMOUNT_MESSAGE = "The color array specified is incorrect. Length has to be 16 (4 colors each having 4 components(r, g, b, a))";

	private final int spriteAmount;

	private float[] vertices;

	private int position = 0;
	private String texture = "";

	protected boolean handleTextureSwitch = false;

	private static long fontID = 0;

	public GRenderer(int spriteAmount) {
		this.spriteAmount = spriteAmount;

		vertices = new float[spriteAmount * (4 + 4 + 4)];
	}

	protected abstract void flush(float[] vertices);

	protected abstract void bindTexture(String texture);

	protected abstract vec2 drawText(String textureID, String text, String font, String style, int size, float[] colors, float scale, float wrapWidth);

	protected abstract void deleteTex(String textureID);

	public final void flush() {
		flush(vertices);
		position = 0;
	}

	public final GTexture renderText(String text, String strFont, String style, int size, GColor color, float wrapWidth) {
		fontID++;

		String textureID = "font=" + fontID;

		vec2 dimensions = drawText(textureID, text, strFont, style, size, new float[] { color.r, color.g, color.b, color.a }, 1, wrapWidth);

		return new GTexture(textureID, dimensions.getX(), dimensions.getY());
	}

	public final void deleteTexture(String textureID) {
		deleteTex(textureID);
	}

	public final void draw(float x, float y, float width, float height, GSprite sprite) {
		this.draw(x, y, width, height, sprite, GColor.WHITE);
	}

	public final void draw(float x, float y, float width, float height, GSprite sprite, GColor color) {

		if (handleTextureSwitch) {
			if (!this.texture.equals(sprite.texture)) {
				flush();
				this.texture = sprite.texture;
				bindTexture(texture);
			}
		} else bindTexture(sprite.texture);

		if (position >= this.spriteAmount) flush();

		int pos = position * (4 + 4);

		int index = 0;

		vertices[pos + index++] = x;
		vertices[pos + index++] = y;
		vertices[pos + index++] = width;
		vertices[pos + index++] = height;

		vertices[pos + index++] = color.r;
		vertices[pos + index++] = color.g;
		vertices[pos + index++] = color.b;
		vertices[pos + index++] = color.a;

		position++;
	}

	protected final int getPosition() {
		return position;
	}

	public final int getCapacity() {
		return spriteAmount;
	}

}
