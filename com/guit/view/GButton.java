package com.guit.view;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.guit.core.GJsonObject;
import com.guit.core.GuitHook;
import com.guit.core.GuitHook.LogType;
import com.guit.core.input.GInputHandler.GTouchEvent;
import com.guit.core.input.GTouchEventType;

public class GButton extends GView {

	private Method method = null;
	private String methodName = null;

	public void collideInternal(GTouchEvent event, float x, float y) {

		if (event.type == GTouchEventType.UP) {

			Rect rect = new Rect(x - getWidth() / 2, y - getHeight() / 2, getWidth(), getHeight());

			if (rect.collides(event.x, event.y)) {
				callMethod();
			}

		}
	}

	private void callMethod() {
		if (method == null) {
			try {
				method = getRoot().getClass().getMethod(methodName);
			} catch (NoSuchMethodException e) {
				GuitHook.getInstance().log(LogType.ERROR, "GButton no method with name: " + methodName + " in class " + getRoot().getClass().getSimpleName());
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}

		try {
			method.invoke(getRoot());
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

	}

	protected void loadInternal(GJsonObject json) {
		super.loadInternal(json);

		if (json.name.equals("OnClick")) {
			methodName = json.data;
		}
	}

}
