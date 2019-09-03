/*
 * Created on 11.04.2007 by Guido Roessling (roessling@acm.org>
 */
package algoanim.primitives;

import java.awt.Font;

import algoanim.primitives.generators.GeneratorInterface;
import algoanim.util.Timing;

public interface AdvancedTextGeneratorInterface extends GeneratorInterface {

	/**
	 * updates the font of this text component (not supported by all primitives!).
	 * 
	 * @param p the <code>Primitive</code> to change.
	 * @param newFont the new text to be used
	 * @param delay the delay until the operation starts
	 * @param duration the duration for the operation
	 */
	public void setFont(Primitive p, Font newFont, Timing delay, 
			Timing duration);
	
	/**
	 * updates the text of this text component (not supported by all primitives!).
	 * 
	 * @param p the <code>Primitive</code> to change.
	 * @param newText the new text to be used
	 * @param delay the delay until the operation starts
	 * @param duration the duration for the operation
	 */
	public void setText(Primitive p, String newText, Timing delay,
			Timing duration);
}
