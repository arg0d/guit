package com.guit.core;

import com.guit.renderer.GRenderer;

public abstract class GuitHook {

	public enum LogType {
		NORMAL, ERROR;
	}

	private static boolean registered = false;
	private static GuitHook hook;

	private GRenderer renderer;

	public GuitHook(GRenderer renderer) {
		this.renderer = renderer;
	}

	public abstract void log(LogType type, String text);

	public GRenderer getRenderer() {
		return renderer;
	}

	public static void register(GuitHook hook) {
		if (registered) throw new IllegalStateException("Already registered.");
		GuitHook.hook = hook;
		registered = true;
	}

	public static GuitHook getInstance() {
		return hook;
	}

}
