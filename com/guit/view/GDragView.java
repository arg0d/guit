package com.guit.view;

import com.guit.core.input.GTouchEventType;
import com.guit.core.input.GInputHandler.GTouchEvent;
import com.guit.renderer.GRenderer;
import com.guit.view.GListView.GOrientation;

public class GDragView extends GView {

	public float offset = 0;

	public GOrientation orientation = GOrientation.HORIZONTAL;

	private GTouchEvent lastEvent = null;
	private float lastX, lastY;

	public GDragView() {
	}

	protected void drawChildren(GRenderer renderer, float x, float y) {
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
				if (rect.collides(event.x, event.y)) {
					if (orientation == GOrientation.HORIZONTAL) offset += event.x - lastX;
					if (orientation == GOrientation.VERTICAL) offset += event.y - lastY;
					lastX = event.x;
					lastY = event.y;
				} else lastEvent = null;
			}
		}

		if (event.type == GTouchEventType.UP) {
			lastEvent = null;
		}
		
		

		if (orientation == GOrientation.HORIZONTAL) super.collideChildren(event, x + offset, y);
		else if (orientation == GOrientation.VERTICAL) super.collideChildren(event, x, y + offset);
	}

}
