package algoanim.primitives;

import algoanim.primitives.generators.EllipseSegGenerator;
import algoanim.properties.EllipseSegProperties;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;

/**
 * Represents the segment of a ellipse. This is defined by a center, a radius and
 * an angle.
 * 
 * @author Guido Roessling
 */
public class EllipseSeg extends Primitive {
	private EllipseSegProperties properties;

	private EllipseSegGenerator generator;

	private Node radius;

	private Node center;

	/**
	 * Instantiates the <code>CircleSeg</code> and calls the create() method of
	 * the associated <code>CircleSegGenerator</code>.
	 * 
	 * @param esg
	 *          the appropriate code <code>Generator</code>.
	 * @param aCenter
	 *          the center of this <code>CircleSeg</code>.
	 * @param aRadius
	 *          the radius of this <code>CircleSeg</code>.
	 * @param name
	 *          the name of this <code>CircleSeg</code>.
	 * @param display
	 *          [optional] the <code>DisplayOptions</code> of this
	 *          <code>CircleSeg</code>.
	 * @param ep
	 *          [optional] the properties of this <code>CircleSeg</code>.
	 */
	public EllipseSeg(EllipseSegGenerator esg, Node aCenter, Node aRadius,
			String name, DisplayOptions display, EllipseSegProperties ep) {
		super(esg, display);

		center = aCenter;
		radius = aRadius;
		properties = ep;
		generator = esg;
		setName(name);
		generator.create(this);
	}

	/**
	 * Returns the center of this <code>EllipseSeg</code>.
	 * 
	 * @return the center of this <code>EllipseSeg</code>.
	 */
	public Node getCenter() {
		return center;
	}

	/**
	 * Returns the properties of this <code>EllipseSeg</code>.
	 * 
	 * @return the properties of this <code>EllipseSeg</code>.
	 */
	public EllipseSegProperties getProperties() {
		return properties;
	}

	/**
	 * Returns the radius of this <code>EllipseSeg</code>.
	 * 
	 * @return the radius of this <code>EllipseSeg</code>.
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
