package generators.misc.continousdoubleauction;

import java.awt.Color;
import java.awt.Font;

import algoanim.primitives.Rect;
import algoanim.primitives.Text;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class AuctionElement {

	private Rect rectangle;
	private Text text;

	private RectProperties prop;
	private Color rectColor;
	private Boolean isBuy;
	private Triple<Double, Double, String> order;

	public AuctionElement(Triple<Double, Double, String> order) {
		this.order = order;
	}

	public AuctionElement(Rect rectangle, Text text, RectProperties prop,
			Boolean isBuy, Triple<Double, Double, String> order) {
		this(rectangle, text, prop, isBuy);
		this.order = order;
	}

	public AuctionElement(Rect rectangle, Text text, RectProperties prop,
			Boolean isBuy) {
		this.rectangle = rectangle;
		this.text = text;
		this.prop = prop;
		this.isBuy = isBuy;
		this.rectColor = (Color) this.rectangle.getProperties().get(
				AnimationPropertiesKeys.COLOR_PROPERTY);
	}

	public Boolean isBuy() {
		return this.isBuy;
	}

	public void setPosition(Coordinates newPosition) {
		this.rectangle.moveTo(null, "translate", newPosition,
				Timing.INSTANTEOUS, new TicksTiming(100));
		this.text.moveTo(null, "translate", new Coordinates(
				newPosition.getX() + 5, newPosition.getY() + 15),
				Timing.INSTANTEOUS, new TicksTiming(100));
	}

	public Triple<Double, Double, String> getOrder() {
		return order;
	}

	public Text getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text.setText(text, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
	}

	public RectProperties getProp() {
		return this.prop;
	}

	public void setProp(RectProperties prop) {
		this.prop = prop;
	}

	public void setRectangle(Rect rectangle) {
		this.rectangle = rectangle;
	}

	public Coordinates getUpperLeft() {
		return (Coordinates) this.rectangle.getUpperLeft();
	}

	public Coordinates getLowerRight() {
		return (Coordinates) this.rectangle.getLowerRight();
	}

	public void setText(Text text) {
		this.text = text;
	}

	public void addHighlight() {
		this.text.setFont(new Font("SansSerif", Font.BOLD, 14),
				new TicksTiming(50), new TicksTiming(20));
		this.rectangle.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
				Color.RED, new TicksTiming(50), new TicksTiming(20));
	}

	public void removeHighlight() {
		this.text.setFont(new Font("SansSerif", Font.PLAIN, 12),
				new TicksTiming(50), new TicksTiming(20));
		this.rectangle.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
				this.rectColor, new TicksTiming(50), new TicksTiming(20));
	}

	public void hideElement() {
		this.rectangle.hide(new TicksTiming(50));
		this.text.hide(new TicksTiming(50));
	}

}
