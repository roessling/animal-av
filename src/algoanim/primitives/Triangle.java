package algoanim.primitives;

import algoanim.primitives.generators.TriangleGenerator;
import algoanim.properties.TriangleProperties;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;

/**
 * Represents a triangle defined by three <code>Node</code>s.
 * @author Stephan Mehlhase 
 */
public class Triangle extends Primitive {
	private TriangleGenerator generator = null;
	private Node[] nodes = null;
	private TriangleProperties properties = null;
	

	/**
     * Instantiates the <code>Triangle</code> and calls the create() method
     * of the associated <code>TriangleGenerator</code>.	 
	 * @param tg the appropriate code <code>Generator</code>.
	 * @param pointA the first <code>Node</code> of this
	 * <code>Triangle</code>.
	 * @param pointB the second <code>Node</code> of this 
	 * <code>Triangle</code>.
	 * @param pointC the third <code>Node</code> of this
	 * <code>Triangle</code>.
	 * @param name the name of this <code>Triangle</code>.
	 * @param display [optional] the <code>DisplayOptions</code> of this
	 * 	<code>Triangle</code>
	 * @param tp [optional] the properties of this <code>Triangle</code>.
	 */
	public Triangle(TriangleGenerator tg, Node pointA, Node pointB, 
			Node pointC, String name, DisplayOptions display, 
			TriangleProperties tp) 
		/*	throws PointNullException */ {
		super(tg, display);
		
		generator = tg;
		nodes = new Node[3];
	/*	if (pointA == null || pointB == null || pointC == null) {			
			throw new PointNullException("One of the given points was null.");
		} */
		nodes[0] = pointA;
		nodes[1] = pointB;
		nodes[2] = pointC;
		properties = tp;
		this.setName(name);
		this.generator.create(this);
	}

	/**
	 * Returns the <code>Node</code>s of this <code>Triangle</code>.
	 * @return the <code>Node</code>s of this <code>Triangle</code>.
	 */
	public Node[] getNodes() {
		return nodes;
	}

	/**
	 * Returns the properties of this <code>Triangle</code>.
	 * @return the properties of this <code>Triangle</code>.
	 */
	public TriangleProperties getProperties() {
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
