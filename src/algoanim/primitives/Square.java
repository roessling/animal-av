package algoanim.primitives;

import algoanim.primitives.generators.SquareGenerator;
import algoanim.properties.SquareProperties;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;

/**
 * Represents a square defined by an upper left corner and its width.
 * 
 * @author Stephan Mehlhase
 */
public class Square extends Primitive {
	private SquareGenerator generator = null;

	private Node upperLeft = null;

	private int width = 0;

	private SquareProperties properties = null;

	/**
	 * Instantiates the <code>Square</code> and calls the create() method of the
	 * associated <code>SquareGenerator</code>.
	 * 
	 * @param sg
	 *          the appropriate code <code>Generator</code>.
	 * @param upperLeftCorner
	 *          the upper left corner of this <code>Square</code>.
	 * @param theWidth
	 *          the width of this <code>Square</code>.
	 * @param name
	 *          the name of this <code>Square</code>.
	 * @param display
	 *          [optional] the <code>DisplayOptions</code> of this
	 *          <code>Square</code>.
	 * @param sp
	 *          [optional] the properties of this <code>Square</code>.
	 * @throws IllegalArgumentException
	 */
	public Square(SquareGenerator sg, Node upperLeftCorner, int theWidth,
			String name, DisplayOptions display, SquareProperties sp)
			throws IllegalArgumentException {
		super(sg, display);

		upperLeft = upperLeftCorner;
		width = theWidth;
		generator = sg;
		properties = sp;
		setName(name);
		generator.create(this);
	}

	/**
	 * Returns the width of this <code>Square</code>.
	 * 
	 * @return the width of this <code>Square</code>.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Returns the properties of this <code>Square</code>.
	 * 
	 * @return the properties of this <code>Square</code>.
	 */
	public SquareProperties getProperties() {
		return properties;
	}

	/**
	 * Returns the upper left corner of this <code>Square</code>.
	 * 
	 * @return the upper left corner of this <code>Square</code>.
	 */
	public Node getUpperLeft() {
		return upperLeft;
	}

	/**
	 * @see algoanim.primitives.Primitive#setName(java.lang.String)
	 */
	public void setName(String newName) {
		properties.setName(newName);
		super.setName(newName);
	}
}
