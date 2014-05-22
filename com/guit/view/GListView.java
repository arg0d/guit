package com.guit.view;

import com.guit.core.GJsonObject;
import com.guit.core.input.GInputHandler.GTouchEvent;
import com.guit.renderer.GRenderer;

public class GListView extends GView {

	public enum GOrientation {
		VERTICAL, HORIZONTAL
	}

	public float spacing = 0;
	public GOrientation orientation = GOrientation.HORIZONTAL;

	public int direction = 1;

	public GListView() {
	}

	protected void drawChildren(GRenderer renderer, float x, float y) {

		switch (orientation) {
		case HORIZONTAL: {

			float pivot = 0;

			float totalWidth = getTotalWidth();
			bufferWidth = totalWidth;
			bufferHeight = height;

			if (this.direction == 0) {
				pivot = -totalWidth / 2;
			} else if (this.direction == -1) {
				pivot = -totalWidth;
			}
			bufferx = x + pivot;

			float widthSoFar = 0;

			for (int i = 0; i < children.size(); i++) {
				GView child = children.get(i);

				child.draw(renderer, x + pivot + widthSoFar + child.getWidth() / 2, y);

				widthSoFar += child.getWidth() + spacing * getScale();
			}
			break;
		}

		case VERTICAL: {

			float pivot = 0;

			float totalHeight = getTotalHeight();

			bufferWidth = width;
			bufferHeight = totalHeight;

			if (this.direction == 0) {
				pivot = -totalHeight / 2;
			} else if (this.direction == -1) {
				pivot = -totalHeight;
			}

			float heightSoFar = 0;

			for (int i = 0; i < children.size(); i++) {
				GView child = children.get(i);

				child.draw(renderer, x, y + pivot + heightSoFar + child.getHeight() / 2);

				heightSoFar += child.getWidth() + spacing * getScale();
			}
			break;
		}
		}

	}

	public void collide(GTouchEvent event, float x, float y) {

		switch (orientation) {
		case HORIZONTAL: {

			float pivot = 0;

			if (this.direction == 0) {
				pivot = -getTotalWidth() / 2;
			} else if (this.direction == -1) {
				pivot = -getTotalWidth();
			}

			float widthSoFar = 0;

			for (int i = 0; i < children.size(); i++) {
				GView child = children.get(i);

				child.collide(event, x + pivot + widthSoFar + child.getWidth() / 2, y);

				widthSoFar += child.getWidth() + spacing * getScale();
			}
			break;
		}

		case VERTICAL: {

			float pivot = 0;

			if (this.direction == 0) {
				pivot = -getTotalHeight() / 2;
			} else if (this.direction == -1) {
				pivot = -getTotalHeight();
			}

			float heightSoFar = 0;

			for (int i = 0; i < children.size(); i++) {
				GView child = children.get(i);

				child.collide(event, x, y + pivot + heightSoFar + child.getWidth() / 2);

				heightSoFar += child.getWidth() + spacing * getScale();
			}
			break;
		}
		}
	}

	public float getTotalWidth() {
		float totalWidth = 0;

		float spacing = this.spacing * getScale();

		for (int i = 0; i < children.size(); i++) {
			float space = spacing;
			if (i == children.size() - 1) space = 0;

			totalWidth += children.get(i).getWidth() + space;
		}

		return totalWidth;
	}

	public float getTotalHeight() {
		float totalHeight = 0;

		float spacing = this.spacing * getScale();

		for (int i = 0; i < children.size(); i++) {
			float space = spacing;
			if (i == children.size() - 1) space = 0;

			totalHeight += children.get(i).getHeight() + space;
		}

		return totalHeight;
	}

	protected void loadInternal(GJsonObject json) {
		super.loadInternal(json);

		if (json.name.equals("Orientation")) {
			String orientation = json.data;

			if (orientation.equals("Horizontal")) this.orientation = GOrientation.HORIZONTAL;
			else if (orientation.equals("Vertical")) this.orientation = GOrientation.VERTICAL;
		} else if (json.name.equals("Direction")) {

			int dir = json.getInt();

			if (dir < 0) this.direction = -1;
			else if (dir == 0) this.direction = 0;
			else if (dir > 0) this.direction = 1;

		} else if (json.name.equals("Margin")) {
			spacing = json.getFloat();
		}
	}

}
