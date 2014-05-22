package com.guit.view;

public enum GHorizontalPosition {
	LEFT, CENTER, RIGHT;

	public static GHorizontalPosition getPosition(String pos) {
		if (pos.equals("Left")) return LEFT;
		else if (pos.equals("Center")) return CENTER;
		else if (pos.equals("Right")) return RIGHT;
		return CENTER;
	}
}
