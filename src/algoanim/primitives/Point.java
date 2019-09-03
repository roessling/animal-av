package algoanim.primitives;

import algoanim.primitives.generators.PointGenerator;
import algoanim.properties.PointProperties;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;

/**
 * Represents a simple point on the animation screen.
 * 
 * @author Stephan Mehlhase
 */
public class Point extends Primitive {
	private PointGenerator generator = null;

	private PointProperties properties = null;

	private Node coords = null;

	/**
	 * Instantiates the <code>Point</code> and calls the create() method of the
	 * associated <code>PointGenerator</code>.
	 * 
	 * @param pg
	 *          the appropriate code <code>Generator</code>.
	 * @param theCoords
	 *          the <code>Node</code> where this <code>Point</code> shall be
	 *          located.
	 * @param name
	 *          the name of this <code>Point</code>.
	 * @param display
	 *          [optional] the <code>DisplayOptions</code> of this
	 *          <code>Point</code>.
	 * @param pp
	 *          [optional] properties for this <code>Point</code>.
	 */
	public Point(PointGenerator pg, Node theCoords, String name,
			DisplayOptions display, PointProperties pp) {
		super(pg, display);

		generator = pg;
		properties = pp;
		setName(name);
		coords = theCoords;
		generator.create(this);
	}

	/**
	 * Returns the properties of this <code>Point</code>.
	 * 
	 * @return the properties of this <code>Point</code>.
	 */
	public PointProperties getProperties() {
		return properties;
	}

	/**
	 * Returns the coordinates of this <code>Point</code>.
	 * 
	 * @return the coordinates of this <code>Point</code>.
	 */
	public Node getCoords() {
		return coords;
	}

	/**
	 * @see algoanim.primitives.Primitive#setName(java.lang.String)
	 */
	public void setName(String newName) {
		properties.setName(newName);
		super.setName(newName);
	}
}
