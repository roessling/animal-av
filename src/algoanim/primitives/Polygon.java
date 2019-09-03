package algoanim.primitives;

import algoanim.exceptions.NotEnoughNodesException;
import algoanim.primitives.generators.PolygonGenerator;
import algoanim.properties.PolygonProperties;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;

/**
 * Represents a polygon defined by an arbitrary number of <code>Node</code>s.
 * In contrast to a <code>Polyline</code> the figure is closed. The first and
 * the last <code>Node</code> are automatically connected.
 * 
 * @author Stephan Mehlhase
 */
public class Polygon extends Primitive {
	private PolygonGenerator generator = null;

	private Node[] nodes;

	private PolygonProperties properties;

	/**
	 * Instantiates the <code>Polygon</code> and calls the create() method of
	 * the associated <code>PolygonGenerator</code>.
	 * 
	 * @param pg
	 *          the appropriate code <code>Generator</code>.
	 * @param theNodes
	 *          the nodes of this <code>Polygon</code>.
	 * @param name
	 *          the name of this <code>Polygon</code>.
	 * @param display
	 *          [optional] the <code>DisplayOptions</code> of this
	 *          <code>Polygon</code>.
	 * @param pp
	 *          [optional] the properties of this <code>Polygon</code>.
	 */
	public Polygon(PolygonGenerator pg, Node[] theNodes, String name,
			DisplayOptions display, PolygonProperties pp)
			throws NotEnoughNodesException {
		super(pg, display);

		if (theNodes.length < 2) {
			throw new NotEnoughNodesException("A polygon needs at least 2 points.");
		}
		nodes = theNodes;
		properties = pp;
		generator = pg;
		setName(name);
		generator.create(this);
	}

	/**
	 * Returns the nodes of this <code>Polygon</code>.
	 * 
	 * @return the nodes of this <code>Polygon</code>.
	 */
	public Node[] getNodes() {
		return nodes;
	}

	/**
	 * Returns the properties of this <code>Polygon</code>.
	 * 
	 * @return the properties of this <code>Polygon</code>.
	 */
	public PolygonProperties getProperties() {
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
