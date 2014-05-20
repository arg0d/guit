package com.guit.renderer;

public abstract class GRenderer {

	public static final String BAD_VERTEX_AMOUNT_MESSAGE = "The vertex array specified is incorrect. Length has to be 8 (4 vertices each having 2 components(x, y))";
	public static final String BAD_TEXTURE_AMOUNT_MESSAGE = "The texture array specified is incorrect. Length has to be 8 (4 textures each having 2 components(u, v))";
	public static final String BAD_COLOR_AMOUNT_MESSAGE = "The color array specified is incorrect. Length has to be 16 (4 colors each having 4 components(r, g, b, a))";

	private final int spriteAmount;

	private float[] vertices;

	private int position = 0;
	private String texture = "";

	public GRenderer(int spriteAmount) {
		this.spriteAmount = spriteAmount;

		vertices = new float[spriteAmount * (4 + 4 + 4)];
	}

	protected abstract void flush(float[] vertices);

	protected abstract void bindTexture(String path);

	public final void flush() {
		flush(vertices);
		position = 0;
	}

	public final void draw(float x, float y, float width, float height, GSprite sprite) {
		this.draw(x, y, width, height, sprite, GColor.WHITE);
	}

	public final void draw(float x, float y, float width, float height, GSprite sprite, GColor color) {

		if (!this.texture.equals(sprite.texture)) {
			flush();
			this.texture = sprite.texture;
			bindTexture(texture);
		}

		if (position >= this.spriteAmount) flush();

		int pos = position * (4 + 4 + 4);

		int index = 0;

		vertices[pos + index++] = x;
		vertices[pos + index++] = y;
		vertices[pos + index++] = width;
		vertices[pos + index++] = height;

		vertices[pos + index++] = sprite.x;
		vertices[pos + index++] = sprite.y;
		vertices[pos + index++] = sprite.x + sprite.width;
		vertices[pos + index++] = sprite.y + sprite.height;

		vertices[pos + index++] = color.r;
		vertices[pos + index++] = color.g;
		vertices[pos + index++] = color.b;
		vertices[pos + index++] = color.a;

		position++;
	}

	protected int getPosition() {
		return position;
	}

	public int getCapacity() {
		return spriteAmount;
	}

}
