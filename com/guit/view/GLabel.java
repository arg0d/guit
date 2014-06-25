package com.guit.view;

import com.guit.core.GJsonObject;
import com.guit.renderer.GColor;
import com.guit.renderer.GRenderer;
import com.guit.renderer.GSprite;
import com.guit.renderer.GTexture;

public class GLabel extends GView {

	public String text = null;
	private String lastText = "";

	private GTexture gtexture = null;

	private String strFont, fontStyle;
	private int size;

	protected void drawInternal(GRenderer renderer, float x, float y) {
		super.drawInternal(renderer, x, y);

		if (!lastText.equals(text)) {
			lastText = text;
			if (gtexture != null) renderer.deleteTexture(gtexture.textureID);
			gtexture = renderer.renderText(lastText, strFont, fontStyle, size, GColor.WHITE, 0);
		}

		renderer.draw(x - gtexture.getWidth() / 2, y - gtexture.getHeight() / 2, gtexture.getWidth(), gtexture.getHeight(), new GSprite(gtexture.textureID));
	}

	protected void loadInternal(GJsonObject json) {
		super.loadInternal(json);

		if (json.name.equals("Text")) {
			text = json.data;
		} else if (json.name.equals("Font")) {
			strFont = json.data;
		} else if (json.name.equals("Style")) {
			fontStyle = json.data;
		} else if (json.name.equals("Size")) {
			size = json.getInt();
		}
	}

}
