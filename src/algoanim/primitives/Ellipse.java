package algoanim.primitives;

import algoanim.primitives.generators.EllipseGenerator;
import algoanim.properties.EllipseProperties;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;

/**
 * Represents an ellipse defined by a center and a radius.
 * 
 * @author Stephan Mehlhase
 */
public class Ellipse extends Primitive {
	private EllipseProperties properties;

	private EllipseGenerator generator;

	private Node center;

	private Node radius;

	/**
	 * Instantiates the <code>Ellipse</code> and calls the create() method of
	 * the associated <code>EllipseGenerator</code>.
	 * 
	 * @param eg
	 *          the appropriate code <code>Generator</code>.
	 * @param aCenter
	 *          the center of this <code>Ellipse</code>.
	 * @param aRadius
	 *          the radius of this <code>Ellipse</code>.
	 * @param name
	 *          the name of this <code>Ellipse</code>.
	 * @param display
	 *          [optional] the <code>DisplayOptions</code> of this
	 *          <code>Ellipse</code>.
	 * @param ep
	 *          [optional] the properties of this <code>Ellipse</code>.
	 */
	public Ellipse(EllipseGenerator eg, Node aCenter, Node aRadius, String name,
			DisplayOptions display, EllipseProperties ep) {
		super(eg, display);

		center = aCenter;
		radius = aRadius;
		properties = ep;
		generator = eg;

		setName(name);
		generator.create(this);
	}

	/**
	 * Returns the center of this <code>Ellipse</code>.
	 * 
	 * @return the center of this <code>Ellipse</code>.
	 */
	public Node getCenter() {
		return center;
	}

	/**
	 * Returns the properties of this <code>Ellipse</code>.
	 * 
	 * @return the properties of this <code>Ellipse</code>.
	 */
	public EllipseProperties getProperties() {
		return properties;
	}

	/**
	 * Returns the radius of this <code>Ellipse</code>.
	 * 
	 * @return the radius of this <code>Ellipse</code>.
	 */
	public Node getRadius() {
		return radius;
	}

	/**
	 * @see algoanim.primitives.Primitive#setName(java.lang.String)
	 */
	public void setName(String newName) {
		properties.setName(newName);
		super.setName(newName);
	}
}
