package algoanim.primitives;

import algoanim.primitives.generators.CircleGenerator;
import algoanim.properties.CircleProperties;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;

/**
 * Represents a circle defined by a center and a radius.
 * @author Stephan Mehlhase 
 */
public class Circle extends Primitive {
    private CircleGenerator generator = null;
    private CircleProperties properties = null;
    private Node center = null;
    private int radius = 0;
    
    /**
     * Instantiates the <code>Circle</code> and calls the create() method
     * of the associated <code>CircleGenerator</code>.     
     * @param cg the appropriate code <code>Generator</code>.
     * @param aCenter the center of this <code>Circle</code>.
     * @param aRadius the radius of this <code>Circle</code>.
     * @param name	 the name of this <code>Circle</code>.
     * @param display [optional] the <code>DisplayOptions</code> of this
     * 	<code>Circle</code>.
     * @param cp [optional] the properties of this <code>Circle</code>.
     */
    public Circle(CircleGenerator cg, Node aCenter, int aRadius, String name, 
    		DisplayOptions display, CircleProperties cp) {
    	super(cg, display);
    	
    	center = aCenter;
    	radius = aRadius;
    	properties = cp;
    	setName(name);
      generator = cg;
      generator.create(this);
    }

    /**
     * Returns the center of this <code>Circle</code>.
     * @return the center of this <code>Circle</code>.
     */
	public Node getCenter() {
		return center;
	}

	/**
	 * Returns the properties of this <code>Circle</code>.
	 * @return the properties of this <code>Circle</code>.
	 */
	public CircleProperties getProperties() {
		return properties;
	}

	/**
	 * Returns the radius of this <code>Circle</code>.
	 * @return the radius of this <code>Circle</code>.
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
