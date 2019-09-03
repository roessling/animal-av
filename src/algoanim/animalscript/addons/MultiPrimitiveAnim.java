package algoanim.animalscript.addons;

import java.util.ArrayList;
import java.util.List;

import algoanim.primitives.Primitive;
import algoanim.primitives.generators.Language;

/**
 * @author Marc Werner <mwerner@rbg.informatik.tu-darmstadt.de>
 * 
 * The class provides management for a list of primitives which belong together.
 * This could i.e. be a headline and the corresponding text or a complete slide within an animation.
 */
public abstract class MultiPrimitiveAnim {
	/**
	 * The algoanim.primitives.generators.Language object to work on
	 */
	protected Language l;
	
	/**
	 * The java.util.List of primitives belonging together 
	 */
	protected List<Primitive> p;
	
	/**
	 * Constructor creates a new list.
	 */
	public MultiPrimitiveAnim() {
		p = new ArrayList<Primitive>();
	}

	/**
	 * 
	 * Constructor creates a new list.
	 * 
	 * @param lang The Language object to add the primitives to.
	 */
	public MultiPrimitiveAnim(Language lang) {
		this();
		l = lang;
	}
	
	/**
	 * Set the Language object before adding primitives.
	 * 
	 * @param lang The Language object
	 */
	public void setLanguage(Language lang) {
		l = lang;
	}

	/**
	 * Hide all primitives at once.
	 */
	public void hide() {
		for (Primitive thisPrimitive : p) {
			thisPrimitive.hide();
		}
	}
	
	/**
	 * Show all primitives at once.
	 */
	public void show() {
		for (Primitive thisPrimitive : p) {
			thisPrimitive.show();
		}
	}	
}
