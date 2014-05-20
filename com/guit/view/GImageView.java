package com.guit.view;

import com.guit.core.GJsonObject;
import com.guit.renderer.GColor;
import com.guit.renderer.GRenderer;
import com.guit.renderer.GSprite;

public class GImageView extends GView {

	public GColor color = GColor.WHITE;

	private int xs, ys;

	public GImageView() {
	}

	protected void drawInternal(GRenderer renderer, float x, float y) {
		super.drawInternal(renderer, x, y);

		GView viableContainer = this;
		while (true) {
			if (viableContainer == null) break;

			if (viableContainer.getWidth() != 0 && viableContainer.getHeight() != 0) {
				break;
			}

			viableContainer = viableContainer.parent;
		}

		if (viableContainer != null) {
			float width = viableContainer.getWidth();
			float height = viableContainer.getHeight();

			renderer.draw(x - width / 2, y - height / 2, width, height, new GSprite("image1.png", xs, ys, 50, 50), color);
		}
	}

	protected void loadInternal(GJsonObject json) {
		super.loadInternal(json);

		if (json.name.equals("Color")) {
			float[] values = json.toFloatArray();

			color = new GColor(values[0], values[1], values[2], values[3]);
		}
	}

}
