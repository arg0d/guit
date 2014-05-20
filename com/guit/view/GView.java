package com.guit.view;

import java.util.ArrayList;
import java.util.List;

import com.guit.core.GJsonObject;
import com.guit.core.GuitHook;
import com.guit.core.input.GInputHandler.GTouchEvent;
import com.guit.renderer.GRenderer;

public class GView {

	public enum ViewPosition {
		CENTER, NORTH, SOUTH, WEST, EAST
	}

	public float x, y, width, height;
	public String tag = "GView default.";

	public ViewPosition viewPosition = ViewPosition.CENTER;

	protected GView parent;
	protected List<GView> children = new ArrayList<GView>();

	protected float centerx, centery;

	private float minScale = 1f;
	private float maxScale = 1f;

	private List<Action> initRender = new ArrayList<Action>();

	public boolean absolute = false;

	public boolean visible = true;

	protected float bufferWidth, bufferHeight;

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

		bufferWidth = width;
		bufferHeight = height;

		if (visible) reposition(renderer, x, y);
	}

	protected void reposition(GRenderer renderer, float x, float y) {
		if (absolute) {
			// getX() and getY() are added further down this method.
			x = getWidth() / 2;
			y = getHeight() / 2;
		} else if (parent != null) {
			switch (viewPosition) {
			case CENTER:
				break;
			case NORTH:
				y -= parent.getHeight() / 2 + getHeight() / 2;
				break;
			case SOUTH:
				y += parent.getHeight() / 2 - getHeight() / 2;
				break;
			case WEST:
				x -= parent.getWidth() / 2 + getWidth() / 2;
				break;
			case EAST:
				x += parent.getWidth() / 2 - getWidth() / 2;
				break;
			default:
				break;
			}
		}

		x += getX();
		y += getY();

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
			switch (viewPosition) {
			case CENTER:
				break;
			case NORTH:
				y -= parent.getHeight() / 2 + getHeight() / 2;
				break;
			case SOUTH:
				y += parent.getHeight() / 2 - getHeight() / 2;
				break;
			case WEST:
				x -= parent.getWidth() / 2 + getWidth() / 2;
				break;
			case EAST:
				x += parent.getHeight() / 2 - getWidth() / 2;
				break;
			default:
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
		removeSubViews();

		for (GJsonObject child : json.getChildren()) {

			{
				GJsonObject typeJson = child.getNode("Type");

				if (typeJson != null) {

					// load required view
					{
						GView view = null;

						String strType = typeJson.data;

						if (strType.equals("View")) view = new GView();
						else if (strType.equals("ImageView")) view = new GImageView();
						else if (strType.equals("ListView")) view = new GListView();
						else if (strType.equals("DragView")) view = new GDragView();

						view.tag = child.name;
						view.load(child);
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
		} else if (json.name.equals("Position")) {
			if (json.data.equals("North")) this.viewPosition = ViewPosition.NORTH;
			else if (json.data.equals("South")) this.viewPosition = ViewPosition.SOUTH;
			else if (json.data.equals("West")) this.viewPosition = ViewPosition.WEST;
			else if (json.data.equals("East")) this.viewPosition = ViewPosition.EAST;
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
			GView childView = child.getSubView(tag);
			if (childView != null) return childView;
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
}
