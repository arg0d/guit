package com.guit.view;

import com.guit.core.GJsonObject;
import com.guit.core.Maths;
import com.guit.core.input.GTouchEventType;
import com.guit.core.input.GInputHandler.GTouchEvent;
import com.guit.renderer.GRenderer;
import com.guit.view.GListView.GOrientation;

public class GDragView extends GView {

	public float offset = 0;
	public GOrientation orientation = GOrientation.HORIZONTAL;

	private GTouchEvent lastEvent = null;
	private float lastX, lastY;

	private String fit = null;

	private int count = 0;

	public GDragView() {
	}

	protected void drawChildren(GRenderer renderer, float x, float y) {

		if (count == 0) {
			offset = -getWidth() / 2;
			count++;
		}

		GView fitView = getSubView(fit);

		if (lastEvent == null) {

			if (offset > (x - getWidth() / 2) - x || fitView.bufferWidth < getWidth()) {
				float pos = (x - getWidth() / 2) - x;
				int dir = Maths.dir(pos - offset);

				offset += dir * 1 + dir * (Maths.abs(pos - offset) / 10);
			}

			if (offset < x + getWidth() / 2 - x - fitView.bufferWidth) {
				float pos = x + getWidth() / 2 - x - fitView.bufferWidth;

				int dir = Maths.dir(pos - offset);

				offset += dir * 1 + dir * (Maths.abs(pos - offset) / 10);
			}
		}

		if (orientation == GOrientation.HORIZONTAL) super.drawChildren(renderer, x + offset, y);
		else if (orientation == GOrientation.VERTICAL) super.drawChildren(renderer, x, y + offset);
	}

	protected void collideChildren(GTouchEvent event, float x, float y) {

		if (event.type == GTouchEventType.DOWN) {

			Rect rect = new Rect(x - getWidth() / 2, y - getHeight() / 2, getWidth(), getHeight());
			if (rect.collides(event.x, event.y)) {
				lastEvent = event;
				lastX = lastEvent.x;
				lastY = lastEvent.y;
			}
		}

		if (event.type == GTouchEventType.DRAG) {

			if (lastEvent == event) {
				Rect rect = new Rect(x - getWidth() / 2, y - getHeight() / 2, getWidth(), getHeight());
				if (rect.collides(event.x, rect.y + rect.height / 2)) {
					if (orientation == GOrientation.HORIZONTAL) offset += event.x - lastX;
					if (orientation == GOrientation.VERTICAL) offset += event.y - lastY;
					lastX = event.x;
					lastY = event.y;
				}
			}
		}

		if (event.type == GTouchEventType.UP) {
			lastEvent = null;
		}

		if (orientation == GOrientation.HORIZONTAL) super.collideChildren(event, x + offset, y);
		else if (orientation == GOrientation.VERTICAL) super.collideChildren(event, x, y + offset);
	}

	protected void loadInternal(GJsonObject json) {
		super.loadInternal(json);

		if (json.name.equals("Fit")) {
			this.fit = json.data;
		} else if (json.name.equals("Axis")) {
			if (json.data.equals("Vertical")) orientation = GOrientation.VERTICAL;
			else if (json.data.equals("Horizontal")) orientation = GOrientation.HORIZONTAL;
		}
	}

}
