package com.guit.core;

import com.guit.renderer.GRenderer;

public class GuitHook {

	private static boolean registered = false;

	private static GRenderer renderer;

	public static final void register(GRenderer renderer) {
		if (registered) throw new IllegalStateException("Already registered.");

		GuitHook.renderer = renderer;
		registered = true;
	}

	
	
	public static GRenderer getRenderer() {
		return renderer;
	}

}
