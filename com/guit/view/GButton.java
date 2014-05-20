package com.guit.view;

import java.util.ArrayList;
import java.util.List;

import com.guit.core.input.GInputHandler.GTouchEvent;

public class GButton extends GView {

	private List<Action> actions = new ArrayList<Action>();

	public void collideInternal(GTouchEvent event, float x, float y) {
		Rect rect = new Rect(x - getWidth() / 2, y - getHeight() / 2, getWidth(), getHeight());

		if (rect.collides(event.x, event.y)) {
			for (Action action : actions) {
				action.performAction();
			}
		}
	}

	public GButton addAction(Action action) {
		if (action == null) throw new IllegalArgumentException("Action cannot be null.");
		actions.add(action);
		return this;
	}
}
