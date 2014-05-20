package com.guit.core.input;

import com.guit.view.GView;

public class GInputHandler {

	private GView updateView = null;
	private GTouchEvent[] events = new GTouchEvent[100];

	public GInputHandler() {
	}

	public final void touchDown(int x, int y, int pointer) {
		events[pointer] = new GTouchEvent(GTouchEventType.DOWN, x, y);
		if (updateView != null) updateView.collide(events[pointer]);
		else System.err.println("GinputHandler.updateView == null");
	}

	public final void touchUp(int x, int y, int pointer) {
		GTouchEvent event = events[pointer];

		event.x = x;
		event.y = y;
		event.type = GTouchEventType.UP;

		events[pointer] = null;

		if (updateView != null) updateView.collide(event);
		else System.err.println("GinputHandler.updateView == null");
	}

	public final void dragged(int x, int y, int pointer) {

		GTouchEvent event = events[pointer];

		event.x = x;
		event.y = y;
		event.type = GTouchEventType.DRAG;

		if (updateView != null) updateView.collide(event);
		else System.err.println("GinputHandler.updateView == null");
	}

	public GInputHandler setUpdateView(GView view) {
		this.updateView = view;
		return this;
	}

	public static class GTouchEvent {

		public GTouchEventType type;
		public float x, y;

		public GTouchEvent(GTouchEventType type, float x, float y) {
			this.type = type;
			this.x = x;
			this.y = y;
		}

	}

}
