package algoanim.primitives;

import algoanim.primitives.generators.ArcGenerator;
import algoanim.properties.ArcProperties;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;

/**
 * Represents an arc defined by a center, a radius and an angle.
 * 
 * @author Stephan Mehlhase
 */
public class Arc extends Primitive {
	private ArcProperties properties;

	private ArcGenerator generator;

	private Node center;

	private Node radius;

	/**
	 * Instantiates the <code>Arc</code> and calls the create() method of the
	 * associated <code>ArcGenerator</code>.
	 * 
	 * @param ag
	 *          the appropriate code <code>Generator</code>.
	 * @param aCenter
	 *          the center of this <code>Arc</code>.
	 * @param aRadius
	 *          the radius of this <code>Arc</code>.
	 * @param name
	 *          the name of this <code>Arc</code>.
	 * @param display
	 *          [optional] the <code>DisplayOptions</code> for this
	 *          <code>Arc</code>.
	 * @param ep
	 *          [optional] the <code>ArcProperties</code>.
	 */
	public Arc(ArcGenerator ag, Node aCenter, Node aRadius, String name,
			DisplayOptions display, ArcProperties ep) {
		super(ag, display);

		center = aCenter;
		radius = aRadius;
		properties = ep;
		generator = ag;

		setName(name);
		generator.create(this);
	}

	/**
	 * Returns the center of this <code>Arc</code>.
	 * 
	 * @return the center of this <code>Arc</code>.
	 */
	public Node getCenter() {
		return center;
	}

	/**
	 * Returns the properties of this <code>Arc</code>.
	 * 
	 * @return the properties of this <code>Arc</code>.
	 */
	public ArcProperties getProperties() {
		return properties;
	}

	/**
	 * Returns the radius of this <code>Arc</code>.
	 * 
	 * @return the radius of this <code>Arc</code>.
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
