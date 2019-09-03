package generators.helpers;

import static algoanim.animalscript.AnimalScript.DIRECTION_SW;
import static algoanim.properties.AnimationPropertiesKeys.COLOR_PROPERTY;
import static algoanim.properties.AnimationPropertiesKeys.FONT_PROPERTY;

import java.awt.Color;
import java.awt.Font;

import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.Primitive;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.TextProperties;
import algoanim.util.MsTiming;
import algoanim.util.Offset;

public class Zahl {
	private static int id = 0;
	public int value;
	private final Text[] digits;
	private Language lang;
	private int yOffset;
	private Primitive base;
	private int highlightStelle = -1;

	public Zahl(int value, int length, Language lang) {
		this.digits = new Text[length];
		this.lang = lang;
		this.value = value;
	}

	public int getStelle(int stelle) {
		return ((value / (int) Math.pow(10, stelle)) % 10);
	}

	public String toString() {
		return String.valueOf(this.value);
	}

	private void display() {
		TextProperties labelProps = new TextProperties();
		labelProps.set(COLOR_PROPERTY, Color.BLACK);
		labelProps.set(FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 18));

		TextProperties highlightLabelProps = new TextProperties();
		highlightLabelProps.set(COLOR_PROPERTY, Color.BLUE);
		highlightLabelProps.set(FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 18));

		for (int i = 0; i < digits.length; i++) {
			int indexFromRight = this.digits.length - 1 - i;
			TextProperties props = (highlightStelle == indexFromRight) ? highlightLabelProps : labelProps;
			digits[i] = lang.newText(getCoords(i), String.valueOf(getStelle(indexFromRight)), "zahl_" + Zahl.id++, null, props);
		}
	}

	public void moveTo(Primitive base, int yOffset) {
		this.base = base;
		this.yOffset = yOffset;
		if (this.digits[0] == null) {
			display();
		} else {
			try {
				for (int i = 0; i < digits.length; i++) {
					digits[i].moveTo(null, "translate", getCoords(i), null, new MsTiming(400));
				}
			} catch (IllegalDirectionException e) {
				e.printStackTrace();
			}
		}
	}

	private Offset getCoords(int xOffset) {
		return new Offset(xOffset * 11, yOffset * 22, base, DIRECTION_SW);
	}

	public Primitive getPrimitive() {
		return this.digits[0];
	}

	public void setHighlight(int stelle) {
		this.highlightStelle = stelle;
		for (Text digit : digits)
			digit.hide();
		display();
	}
}
