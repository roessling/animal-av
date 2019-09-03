package util;

import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Timing;

/** 
 * An IntVariable gives the possibility to increment or decrement this integer value 
 * 
 * @author Timo Baehr
 * @author Alexander Jandouesek
 * */
public class IntVariable {

	/* variables */
	private int value;
	private Text text;
	private String name;
	
	private Timing DEFAULT_DURATION;

	
	/* constructors */
	
	public IntVariable(Language language, Coordinates coords, String name, String id, int value, Timing duration, TextProperties prop) {
		this.DEFAULT_DURATION = duration;
		this.text = language.newText(coords, name +" = " +value +";", id, null, prop);
		this.value = value;
		this.name = name;
	}
	
	
	/* getter and setter */

	public void increment() {
		value++;
		text.setText(name +" = " +value +";", null, DEFAULT_DURATION);
	}

	public void decrement() {
		value--;
		text.setText(name +" = " +value +";", null, DEFAULT_DURATION);
	}

	public void set(int value) {
		this.value = value;
		text.setText(name +" = " +value +";", null, DEFAULT_DURATION);
	}

	public int getIntegerValue() {
		return value;
	}

	public void hide() {
		text.hide();
	}

	public void show() {
		text.show();
	}
	
}
