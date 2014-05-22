package com.guit.view;

import java.util.ArrayList;
import java.util.List;

import com.guit.core.GJsonObject;
import com.guit.core.GuitHook;
import com.guit.core.input.GInputHandler.GTouchEvent;
import com.guit.renderer.GRenderer;

public class GView {

	public float x, y, width, height;
	public String tag = "GView default.";

	protected GView parent;
	protected List<GView> children = new ArrayList<GView>();

	protected float centerx, centery;

	private float minScale = 1f;
	private float maxScale = 1f;

	private List<Action> initRender = new ArrayList<Action>();

	public boolean absolute = false;

	public boolean visible = true;

	private GHorizontalPosition horizontalPosition = GHorizontalPosition.CENTER;
	private GVerticalPosition verticalPosition = GVerticalPosition.CENTER;

	protected float bufferx, buffery, bufferWidth, bufferHeight;

	public GView() {
	}

	public GView set(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		return this;
	}

	public void draw() {
		draw(GuitHook.getRenderer(), getX() + getWidth() / 2, getY() + getHeight() / 2);
	}

	protected final void draw(GRenderer renderer, float x, float y) {

		for (Action action : initRender) {
			action.performAction();
		}
		initRender.clear();

		if (visible) reposition(renderer, x, y);
	}

	protected void reposition(GRenderer renderer, float x, float y) {
		if (absolute) {
			// getX() and getY() are added further down this method.
			x = getWidth() / 2;
			y = getHeight() / 2;
		} else if (parent != null) {

			switch (horizontalPosition) {
			case CENTER:
				break;
			case LEFT:
				x = x - parent.getWidth() / 2 + getWidth() / 2;
				break;
			case RIGHT:
				x = x + parent.getWidth() / 2 - getWidth() / 2;
				break;
			}

			switch (verticalPosition) {
			case CENTER:
				break;
			case DOWN:
				y = y + parent.getHeight() / 2 - getHeight() / 2;
				break;
			case TOP:
				y = y - parent.getHeight() / 2 + getHeight() / 2;
				break;
			}
		}

		x += getX();
		y += getY();

		bufferx = x;
		buffery = y;
		bufferWidth = getWidth();
		bufferHeight = getHeight();

		drawInternal(renderer, x, y);

		drawChildren(renderer, x, y);

	}

	protected void drawChildren(GRenderer renderer, float x, float y) {
		for (GView child : children) {
			child.draw(renderer, x, y);
		}
	}

	protected void drawInternal(GRenderer renderer, float x, float y) {
	}

	public void collide(GTouchEvent event) {
		collide(event, x + getWidth() / 2, y + getHeight() / 2);
	}

	protected void collide(GTouchEvent event, float x, float y) {

		if (!visible) return;

		if (absolute) {
			// getX() and getY() are added further down this method.
			x = getWidth() / 2;
			y = getHeight() / 2;
		} else if (parent != null) {
			switch (horizontalPosition) {
			case CENTER:
				break;
			case LEFT:
				x = x - parent.getWidth() / 2 + getWidth() / 2;
				break;
			case RIGHT:
				x = x + parent.getWidth() / 2 - getWidth() / 2;
				break;
			}

			switch (verticalPosition) {
			case CENTER:
				break;
			case DOWN:
				y = y + parent.getHeight() / 2 - getHeight() / 2;
				break;
			case TOP:
				y = y - parent.getHeight() / 2 + getHeight() / 2;
				break;
			}
		}

		x += getX();
		y += getY();

		collideInternal(event, x, y);
		collideChildren(event, x, y);

	}

	protected void collideInternal(GTouchEvent event, float x, float y) {
	}

	protected void collideChildren(GTouchEvent event, float x, float y) {
		for (GView child : children) {
			child.collide(event, x, y);
		}
	}

	public void addSubView(GView view) {
		children.add(view);
		view.parent = this;
	}

	public void removeSubView(GView view) {
		children.remove(view);
	}

	public void load(GJsonObject json) {
		loadPrivate(json.getChildren().get(0));
	}

	private void loadPrivate(GJsonObject json) {
		removeSubViews();

		for (GJsonObject child : json.getChildren()) {

			{
				GJsonObject typeJson = child.getNode("Type");
				if (child.name.equals("ButtonsList")) {
				}

				if (typeJson != null) {

					// load required view
					{
						GView view = null;

						String strType = typeJson.data;

						if (strType.equals("View")) view = new GView();
						else if (strType.equals("ImageView")) view = new GImageView();
						else if (strType.equals("ListView")) view = new GListView();
						else if (strType.equals("DragView")) view = new GDragView();
						else if (strType.equals("Button")) view = new GButton();

						view.tag = child.name;
						view.loadPrivate(child);
						addSubView(view);
					}

				}
			}

			loadInternal(child);

		}

	}

	protected void loadInternal(GJsonObject json) {
		if (json.name.equals("Frame")) {
			float[] values = json.toFloatArray();

			x = values[0];
			y = values[1];
			width = values[2];
			height = values[3];
		} else if (json.name.equals("Rectangle")) {
			float[] values = json.toFloatArray();

			float x0 = values[0];
			float y0 = values[1];
			float x1 = values[2];
			float y1 = values[3];

			this.x = x0;
			this.y = y0;
			this.width = x1 - x0;
			this.height = y1 - y0;
		} else if (json.name.equals("Scale")) {
			minScale = json.getFloat();
			setScale(1f);
		} else if (json.name.equals("InitRender")) {

			for (final GJsonObject initRenderJson : json.getChildren()) {

				Action action = null;

				if (initRenderJson.name.equals("FlushScale")) {
					action = new Action() {
						public void performAction() {
							flushScale(initRenderJson.getFloat());
						}
					};
				}

				if (action != null) {
					initRender.add(action);
				}

			}

		} else if (json.name.equals("Absolute")) {
			this.absolute = json.getBoolean();
		} else if (json.name.equals("Visible")) {
			this.visible = json.getBoolean();
		} else if (json.name.equals("Vertical")) {
			verticalPosition = GVerticalPosition.getPosition(json.data);
		} else if (json.name.equals("Horizontal")) {
			horizontalPosition = GHorizontalPosition.getPosition(json.data);
		}
	}

	private void flushScale(float scale) {
		minScale *= scale;
		setScale(1f);

		for (GView child : children) {
			child.flushScale(scale);
		}
	}

	public GView findSubView(String tag) {
		if (this.tag.equals(tag)) return this;

		for (GView child : children) {
			GView childView = child.findSubView(tag);
			if (childView != null) return childView;
		}

		return null;
	}

	public GView getSubView(String tag) {
		for (GView child : children) {
			if (child.tag.equals(tag)) return child;
		}

		return null;
	}

	public void removeSubViews() {
		children.clear();
	}

	public float getX() {
		return maxScale * x;
	}

	public float getY() {
		return maxScale * y;
	}

	public float getWidth() {
		return maxScale * width;
	}

	public float getHeight() {
		return maxScale * height;
	}

	public float getScale() {
		return maxScale;
	}

	public GView setScale(float scale) {
		this.maxScale = minScale * scale;
		return this;
	}

	public List<GView> getAllSubviews() {
		return children;
	}
}
