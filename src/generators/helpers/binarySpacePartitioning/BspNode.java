package generators.helpers.binarySpacePartitioning;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import algoanim.primitives.generators.Language;

/**
 * Auto-partitioned BSP tree node.
 * A BSP node contains a splitter and a front/back child.
 * Traversal and creation generate animal-script output.
 */
public class BspNode {

	private LinkedList<Polygon> _polygons = new LinkedList<Polygon>();
	private Polygon _splitter;
	private BspNode[] _children = new BspNode[2];
	private Plane _splittingPlane;
	private Node _node;

	/**
	 * A splitting polygon among the given set of polygons is chosen in a way
	 * that balancing requirements are satisfied if specified.
	 * 
	 * @param polygons The list of polygons to chose from
	 * @param balanced Number of polygons in both halfspaces will be balanced if true
	 * @return Splitter polygon. This polygons induced plane serves as partition plane.
	 */
	Polygon pickSplitter(LinkedList<Polygon> polygons, Boolean balanced) {
		
		if (!balanced) {
			// Random splitter:
			return polygons.get((int) Math.round(Math.random()*(polygons.size()-1)));
		
		} else {
			// Choose splitter with most balanced polygon count left / right:
			Polygon splitter = null;
			int balance = Integer.MAX_VALUE;
			
			for (Polygon candidate : polygons) {
				Plane candidatePlane = new Plane(candidate);
				int numBack = 0;
				int numFront = 0;
				
				for (Polygon polygon : polygons) {
					numFront += candidatePlane.classify(polygon) == HalfSpace.hsPositive ? 1 : 0;
					numBack += candidatePlane.classify(polygon) == HalfSpace.hsNegative ? 1 : 0;
				}
				if (Math.abs(numFront - numBack) < balance) {
					splitter = candidate;
					balance = Math.abs(numFront - numBack);
				}
			}
			
			return splitter;
		}
	}

	/**
	 * Recursively builds a BSP tree given a list of polygons. For educational
	 * purposes, the partition plane/polygon and the classification of the 
	 * remaining polygons are displayed in animal script code.
	 * 
	 * @param polygons The initial set of polygons
	 * @param lang	The animal script language object
	 * @param clippers Clipping away already partitioned space as well as space outside the clipping rectangle
	 * @param node The visual representation of an BSP tree node
	 * @param balanced The resulting bsp tree will be balanced if true
	 */
	public BspNode(LinkedList<Polygon> polygons, Language lang, LinkedList<Polygon> clippers, Node node, Boolean balanced) {

		// Pick splitter:
		_splitter = pickSplitter(polygons, balanced);
		_splittingPlane = new Plane(_splitter);
		_polygons.push(_splitter);
		
		// Classify remaining polygons according to partition plane / splitter:
		LinkedList<Polygon> front = new LinkedList<Polygon>();
		LinkedList<Polygon> back = new LinkedList<Polygon>();

		for (Polygon polygon : polygons) {

			if (polygon == _splitter)
				continue;

			switch (_splittingPlane.classify(polygon)) {
			case hsInsidePlane:
				_polygons.push(polygon);
				continue;
			case hsPositive:
				front.push(polygon);
				continue;
			case hsNegative:
				back.push(polygon);
				continue;
			case hsClipped:
				Polygon frontPoly = new Polygon();
				Polygon backPoly = new Polygon();
				_splittingPlane.split(polygon, frontPoly, backPoly);
				front.push(frontPoly);
				back.push(backPoly);
			}
		}
		
		// Educational output purposes only:
		if (!front.isEmpty() || !back.isEmpty()) {

			// Drawing children:
			if (!front.isEmpty()) {
				Color frontColor = new Color(255, 200, 200);
				
				for (Polygon polygon : front)
					polygon.draw(lang, frontColor);
			}
			
			if (!back.isEmpty()) {
				Color backColor = new Color(200, 255, 200);
				
				for (Polygon polygon : back)
					polygon.draw(lang, backColor);
			}
		}
		
		// Drawing splitting node:
		Color splitterColor = new Color((int) (Math.random()*255), (int) (Math.random()*255), (int) (Math.random()*255));
		node.draw(lang, splitterColor);
		_node = node;
		
		// Drawing splitting line:
		Vector direction = _splitter.getPoint(0).subtract(_splitter.getPoint(1)).calcDirection().multiply(1000000);
		Polygon line = new Polygon(new Vector[] {_splitter.getPoint(0).subtract(direction), _splitter.getPoint(0).add(direction)});
		
		for (Polygon clipper : clippers) { // Clipping splitting line
			Plane clippingPlane = new Plane(clipper);
			
			if (_splittingPlane.classify(clipper) == HalfSpace.hsClipped) {
				
				Polygon lineFront = new Polygon();
				Polygon lineBack = new Polygon();
				if (clippingPlane.split(line, lineFront, lineBack)) {
					line = lineBack;
					for (int i = 0; i <= 1; i++) 
						for (int j = 0; j <= 1; j++)
							if (clippingPlane.classify(_splitter.getPoint(i)) != HalfSpace.hsInsidePlane)
								if (clippingPlane.classify(lineFront.getPoint(j)) != HalfSpace.hsInsidePlane)
									if (clippingPlane.classify(_splitter.getPoint(i)) == clippingPlane.classify(lineFront.getPoint(j)))
										line = lineFront;
				}
			}
		}
		line.draw(lang, new Color(50, 50, 50), false);
		clippers.push(line);
		
		// Drawing splitting plane:
		_splitter.draw(lang, splitterColor);

		lang.nextStep();
		
		// Recursing:
		if (!front.isEmpty()) _children[0] = new BspNode(front, lang, clippers, node.createChild(0), balanced);
		else _children[0] = null;
		
		if (!back.isEmpty()) _children[1] = new BspNode(back, lang, clippers, node.createChild(1), balanced);
		else _children[1] = null;
		
	}

	public List<Polygon> getPolygons() {
		return _polygons;
	}

	public BspNode getChild(int i) {
		return _children[i];
	}

	public BspNode getFront() {
		return _children[0];
	}

	public BspNode getBack() {
		return _children[1];
	}
	
	public Polygon getSplitter() {
		return _splitter;
	}

	public Plane getSplittingPlane() {
		return _splittingPlane;
	}
	
	public Node getNode() {
		return _node;
	}

	/** 
	 * Traverse the BSP tree and display the traversed node's splitter polygon
	 * using animal script. Tree traversal results in an optimal drawing order, 
	 * seen from the camera position no polygon will be displayed before
	 * it's underlying polygons are (according to z-order).
	 * 
	 * @param camera Camera position
	 * @param lang Animal script language object 
	 */
	public void traverse(Vector camera, Language lang) {
		traverseRec(camera, this, lang);
	}

	public void traverseRec(Vector camera, BspNode bspNode, Language lang) {
		traverseRec(camera, bspNode, lang, 0);
	}
	
	public void traverseRec(Vector camera, BspNode bspNode, Language lang, int depth) {
		  int i = bspNode.getSplittingPlane().classify(camera) == HalfSpace.hsPositive ? 1 : 0;
		  
		  // Drawing:
		  Color color = new Color(255, 0, 0);
		  bspNode.getSplitter().draw(lang, color);
		  bspNode.getNode().highlight(lang, color);
		  lang.nextStep();
		  
		  if (bspNode.getChild(i) != null) traverseRec(camera, bspNode.getChild(i), lang, depth+1);
		  
		  // Drawing:
		  color = new Color(255, 255, 0);
		  bspNode.getSplitter().draw(lang, color);
		  bspNode.getNode().highlight(lang, color);
		  lang.nextStep();
		  
		  if (bspNode.getChild(i^1) != null) traverseRec(camera, bspNode.getChild(i^1), lang, depth+1);
	}

}
