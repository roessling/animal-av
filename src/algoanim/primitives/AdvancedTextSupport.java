/*
 * Created on 11.04.2007 by Guido Roessling (roessling@acm.org>
 */
package algoanim.primitives;

import java.awt.Font;

import algoanim.primitives.generators.GeneratorInterface;
import algoanim.util.DisplayOptions;
import algoanim.util.Timing;

public abstract class AdvancedTextSupport extends Primitive {
	
	/**
	 * @param g
	 *          the appropriate code <code>Generator</code> for this
	 *          <code>Primitive</code>.
	 * @param d
	 *          [optional] the <code>DisplayOptions</code> for this
	 *          <code>Primitive</code>.
	 */
	protected AdvancedTextSupport(GeneratorInterface g, DisplayOptions d) {
		super(g, d);
	}

	/**
	 * updates the text of this text component (not supported by all primitives!).
	 * 
	 * @param newText the new text to be used
	 * @param delay the delay until the operation starts
	 * @param duration the duration for the operation
	 */
	public void setText(String newText, Timing delay, Timing duration) {
		if (gen instanceof AdvancedTextGeneratorInterface)
			((AdvancedTextGeneratorInterface)gen).setText(this, newText, 
					delay, duration);
		else
			throw new IllegalArgumentException("gen is not an AdvancedTextGeneratorInterface -- " +gen);
	}
	
	/**
	 * updates the font of this text component (not supported by all primitives!).
	 * 
	 * @param newFont the new text to be used
	 * @param delay the delay until the operation starts
	 * @param duration the duration for the operation
	 */
	public void setFont(Font newFont, Timing delay, Timing duration) {
		if (gen instanceof AdvancedTextGeneratorInterface)
			((AdvancedTextGeneratorInterface)gen).setFont(this, newFont, 
					delay, duration);
		else
			throw new IllegalArgumentException("gen is not an AdvancedTextGeneratorInterface -- " +gen);

	}

}
