package com.guit.view;

import com.guit.core.GJsonObject;
import com.guit.renderer.GColor;
import com.guit.renderer.GRenderer;
import com.guit.renderer.GSprite;

public class GImageView extends GView {

	public GColor color = GColor.WHITE;

	public String texture = null;

	public GImageView() {
	}

	protected void drawInternal(GRenderer renderer, float x, float y) {
		super.drawInternal(renderer, x, y);

		renderer.draw(x - getWidth() / 2, y - getHeight() / 2, getWidth(), getHeight(), new GSprite(texture), color);
	}

	protected void loadInternal(GJsonObject json) {
		super.loadInternal(json);

		if (json.name.equals("Color")) {
			float[] values = json.toFloatArray();

			color = new GColor(values[0], values[1], values[2], values[3]);
		} else if (json.name.equals("Path")) {
			texture = json.data;
		}
	}

}
