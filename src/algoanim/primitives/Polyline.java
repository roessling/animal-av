package algoanim.primitives;

import algoanim.primitives.generators.PolylineGenerator;
import algoanim.properties.PolylineProperties;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;

/**
 * Represents a Polyline which consists of an arbitrary number of
 * <code>Node</code>s.
 * 
 * @author stephan
 */
public class Polyline extends Primitive {
	private PolylineGenerator generator = null;

	private Node[] nodes = null;

	private PolylineProperties properties = null;

	/**
	 * Instantiates the <code>Polyline</code> and calls the create() method of
	 * the associated <code>PolylineGenerator</code>.
	 * 
	 * @param pg
	 *          the appropriate code <code>Generator</code>.
	 * @param theNodes
	 *          the nodes of this <code>Polyline</code>.
	 * @param name
	 *          the name of this <code>Polyline</code>.
	 * @param display
	 *          [optional] the <code>DisplayOptions</code> of this
	 *          <code>Polyline</code>.
	 * @param pp
	 *          [optional] the properties of this <code>Polyline</code>.
	 */
	public Polyline(PolylineGenerator pg, Node[] theNodes, String name,
			DisplayOptions display, PolylineProperties pp) {
		super(pg, display);

		properties = pp;
		nodes = theNodes;
		generator = pg;

		setName(name);
		generator.create(this);
	}

	/**
	 * Returns the <code>Node</code>s of this <code>Polyline</code>.
	 * 
	 * @return the <code>Node</code>s of this <code>Polyline</code>.
	 */
	public Node[] getNodes() {
		return nodes;
	}

	/**
	 * Returns the properties of this <code>Polyline</code>.
	 * 
	 * @return the properties of this <code>Polyline</code>.
	 */
	public PolylineProperties getProperties() {
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
