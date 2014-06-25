package com.guit.view;

public enum GVerticalPosition {
	TOP, CENTER, DOWN;

	public static GVerticalPosition getPosition(String pos) {
		if (pos.equals("Top")) return TOP;
		else if (pos.equals("Center")) return CENTER;
		else if (pos.equals("Bottom")) return DOWN;
		return CENTER;
	}
}
