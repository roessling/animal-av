package generators.graphics.helpers;
import generators.helpers.OffsetCoords;

import java.awt.Color;

import algoanim.primitives.Rect;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;

public class WSTRect {
	private int index;
	private int value;

	private WSTAnim anim;

	public WSTRect(int index, int value, WSTAnim anim) {
		super();
		this.index = index;
		this.value = value;
		this.anim = anim;
	}

	private Rect rect;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getValue() {
		return value;
	}

	public boolean setValue(int value) {
		if (value != this.value) {
			this.value = value;
			updateRect();
			return true;
		}
		return false;
	}

	public Rect getRect() {
		return rect;
	}

	public void setRect(Rect rect) {
		this.rect = rect;
	}

	public void updateRect() {
		if (rect == null) {
			initializeRect();
			return;
		}
		else {
			rect.hide();
			rect = initializeRect();
		}
	}

	private Rect initializeRect() {
		RectProperties rectProperties = new RectProperties();

		rectProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		rectProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		rectProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY,
				Boolean.TRUE);
		rectProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		rect = anim.getLang().newRect(
				new OffsetCoords(anim.getBottomLeft(), index * 26,
						-Tools.heightForValue(value, anim.getMax())),
				new OffsetCoords(anim.getBottomLeft(), 26 + index * 26, 0),
				"rect" + index, null, rectProperties);
		return rect;
	}
	
	public void hide() {
		rect.hide();
	}
}
