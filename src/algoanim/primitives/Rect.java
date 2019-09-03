/*
 * Erstellung: 25.11.2004
 *
 */
package algoanim.primitives;

import algoanim.primitives.generators.RectGenerator;
import algoanim.properties.RectProperties;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;

/**
 * Represents a simple rectangle defined by its upper left and its lower right
 * corners.
 * 
 * @author Stephan Mehlhase
 */
public class Rect extends Primitive {
	private RectGenerator generator = null;

	private RectProperties properties = null;

	private Node upperLeft = null;

	private Node lowerRight = null;

	/**
	 * Instantiates the <code>Rect</code> and calls the create() method of the
	 * associated <code>RectGenerator</code>.
	 * 
	 * @param rg
	 *          the appropriate code <code>Generator</code>.
	 * @param upperLeftCorner
	 *          the upper left corner of this <code>Rect</code>.
	 * @param lowerRightCorner
	 *          the lower right corner of this <code>Rect</code>.
	 * @param name
	 *          the name of this <code>Rect</code>.
	 * @param display
	 *          [optional] the <code>DisplayOptions</code> of this
	 *          <code>Rect</code>.
	 * @param rp
	 *          [optional] the properties of this <code>Rect</code>.
	 * @throws IllegalArgumentException
	 */
	public Rect(RectGenerator rg, Node upperLeftCorner, Node lowerRightCorner,
			String name, DisplayOptions display, RectProperties rp)
			throws IllegalArgumentException {
		super(rg, display);

		if (upperLeftCorner == null || lowerRightCorner == null) {
			throw new IllegalArgumentException("The coordinates "
					+ "shouldn't be null");
		}

		generator = rg;
		upperLeft = upperLeftCorner;
		lowerRight = lowerRightCorner;
		properties = rp;
		setName(name);
		generator.create(this);
	}

	/**
	 * Returns the upper left corner of this <code>Rect</code>.
	 * 
	 * @return the upper left corner of this <code>Rect</code>.
	 */
	public Node getUpperLeft() {
		return this.upperLeft;
	}

	/**
	 * Returns the lower right corner of this <code>Rect</code>.
	 * 
	 * @return the lower right corner of this <code>Rect</code>.
	 */
	public Node getLowerRight() {
		return this.lowerRight;
	}

	/**
	 * Returns the properties of this <code>Rect</code>.
	 * 
	 * @return the properties of this <code>Rect</code>.
	 */
	public RectProperties getProperties() {
		return properties;
	}

	/**
	 * @see algoanim.primitives.Primitive#setName(java.lang.String)
	 */
	public void setName(String newName) {
		properties.setName(newName);
		super.setName(newName);
	}
}
