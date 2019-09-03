package algoanim.primitives;

import algoanim.primitives.generators.CircleSegGenerator;
import algoanim.properties.CircleSegProperties;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;

/**
 * Represents the segment of a circle. This is defined by a center, a radius and
 * an angle.
 * 
 * @author Stephan Mehlhase
 */
public class CircleSeg extends Primitive {
	private CircleSegProperties properties;

	private CircleSegGenerator generator;

	private int radius = 0;

	private Node center;

	/**
	 * Instantiates the <code>CircleSeg</code> and calls the create() method of
	 * the associated <code>CircleSegGenerator</code>.
	 * 
	 * @param csg
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
	 * @param cp
	 *          [optional] the properties of this <code>CircleSeg</code>.
	 */
	public CircleSeg(CircleSegGenerator csg, Node aCenter, int aRadius,
			String name, DisplayOptions display, CircleSegProperties cp) {
		super(csg, display);

		center = aCenter;
		radius = aRadius;
		properties = cp;
		generator = csg;
		setName(name);
		generator.create(this);
	}

	/**
	 * Returns the center of this <code>CircleSeg</code>.
	 * 
	 * @return the center of this <code>CircleSeg</code>.
	 */
	public Node getCenter() {
		return center;
	}

	/**
	 * Returns the properties of this <code>CircleSeg</code>.
	 * 
	 * @return the properties of this <code>CircleSeg</code>.
	 */
	public CircleSegProperties getProperties() {
		return properties;
	}

	/**
	 * Returns the radius of this <code>CircleSeg</code>.
	 * 
	 * @return the radius of this <code>CircleSeg</code>.
	 */
	public int getRadius() {
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
