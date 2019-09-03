package generators.compression.shannon_fano.guielements.tree;

import generators.compression.shannon_fano.style.ShannonFanoStyle;

import java.util.ArrayList;
import java.util.Collections;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Rect;
import algoanim.properties.RectProperties;
import algoanim.util.MsTiming;
import algoanim.util.Offset;
import algoanim.util.Timing;

/**
 * This class represents a set, which includes several nodes.
 * 
 * @author Sebastian Fach
 */
public class NodeSet extends AbstractNode {

	/**
	 * The id of this set
	 */
	private int id;

	/**
	 * The primitive for the shape
	 */
	private Rect rect;

	/**
	 * The nodes in the set
	 */
	private ArrayList<AbstractNode> nodes;

	private AbstractNode leftmostNode;
	private AbstractNode rightmostNode;

	/**
	 * @param nodes
	 * 
	 * @param tree
	 * 
	 */
	public NodeSet(ArrayList<AbstractNode> nodes, Tree tree) {
		super(tree.nextSetId(), tree);
		this.nodes = nodes;
		for (AbstractNode node : nodes) {
			frequency += node.getFrequency();
			probability += node.getProbability();
		}
		determineBoundingNodes();
		determineCenterCoordinates();
		createPrimitives();
		createBoundingRect();
	}

	/**
	 * Searches for the leftmost and the rightmost node.
	 */
	private void determineBoundingNodes() {
		leftmostNode = null;
		rightmostNode = null;
		for (AbstractNode node : nodes) {
			if (leftmostNode == null || node.getCenterX() < leftmostNode.getCenterX()) {
				leftmostNode = node;
			}
			if (rightmostNode == null || node.getCenterX() > rightmostNode.getCenterX()) {
				rightmostNode = node;
			}
		}
	}

	/**
	 * Calculates and sets the center coordinates based on the leftmost and the
	 * rightmost node.
	 */
	private void determineCenterCoordinates() {
		centerX = (leftmostNode.getCenterX() + rightmostNode.getCenterX()) / 2;
		centerY = leftmostNode.getCenterY();
	}

	public int getSize() {
		return nodes.size();
	}
	
	public ArrayList<AbstractNode> getNodes() {
		return nodes;
	}

	/**
	 * Splits this set of nodes S into two sets S1 and S2, so that the summed
	 * frequencies are nearly equal. Before calling this method, you have to
	 * make sure that there are at least two elements in the set.
	 */
	public AbstractNode[] split() {

		ArrayList<AbstractNode> nodesS1 = new ArrayList<AbstractNode>();
		ArrayList<AbstractNode> nodesS2 = new ArrayList<AbstractNode>();
		int sumFrequencyS1 = 0;
		int sumFrequencyS2 = 0;

		// element i is the first element after the first part, while
		// j is the first element of the second part
		int i = 0;
		int j = nodes.size();

		// repeat this until the two pointers meet each other
		while (i != j) {
			// add the element at position i / at position j - 1 to the
			// smaller part
			if (sumFrequencyS1 <= sumFrequencyS2) {
				nodesS1.add(nodes.get(i));
				sumFrequencyS1 += nodes.get(i).getFrequency();
				i++;
			} else {
				nodesS2.add(nodes.get(j - 1));
				sumFrequencyS2 += nodes.get(j - 1).getFrequency();
				j--;
			}
		}
		
		// s2 is the wrong way around, so correct it
		Collections.reverse(nodesS2);

		// hide and remove the old set
		hide();
		tree.getNodes().remove(this);

		// create, add and show the new sets
		AbstractNode s1, s2;
		if (nodesS1.size() == 1) {
			s1 = nodesS1.get(0);
		} else {
			s1 = new NodeSet(nodesS1, tree);
		}
		if (nodesS2.size() == 1) {
			s2 = nodesS2.get(0);
		} else {
			s2 = new NodeSet(nodesS2, tree);
		}
		
		tree.add(s1);
		tree.add(s2);
		s1.show();
		s2.show();

		AbstractNode[] sets = new AbstractNode[] { s1, s2 };
		return sets;
	}

	@Override
	protected void createPrimitives() {
		Offset rectOffsetUpperLeft = new Offset(-5, -5, leftmostNode.getBoundingRect(), AnimalScript.DIRECTION_NW);
		Offset rectOffsetLowerRight = new Offset(5, 5, rightmostNode.getBoundingRect(), AnimalScript.DIRECTION_SE);
		RectProperties rectProp = (RectProperties) tree.getStyle().getProperties(ShannonFanoStyle.NODE_SET);
		rect = tree.getLang().newRect(rectOffsetUpperLeft, rectOffsetLowerRight, "node" + id + "SetRect", null,
				rectProp);
	}

	@Override
	protected void createBoundingRect() {
		Offset upperLeft = new Offset(0, 0, rect, AnimalScript.DIRECTION_NW);
		Offset lowerRight = new Offset(0, 0, rect, AnimalScript.DIRECTION_SE);
		RectProperties rectProp = (RectProperties) tree.getStyle().getProperties(ShannonFanoStyle.NODE_SET);
		boundingRect = tree.getLang().newRect(upperLeft, lowerRight, "node" + id + "BoundingRect", null, rectProp);
		boundingRect.hide();
	};

	@Override
	public void hide() {
		for (AbstractNode node : nodes) {
			node.hide();
		}
		if (leftChildEdge != null) {
			leftChildEdge.hide();
		}
		if (rightChildEdge != null) {
			rightChildEdge.hide();
		}
		rect.hide();
	}

	@Override
	public void hide(int delay) {
		for (AbstractNode node : nodes) {
			node.hide(delay);
		}
		if (leftChildEdge != null) {
			leftChildEdge.hide(delay);
		}
		if (rightChildEdge != null) {
			rightChildEdge.hide(delay);
		}
		rect.hide(new MsTiming(delay));
	}

	@Override
	public void show() {
		for (AbstractNode node : nodes) {
			node.show();
		}
		if (leftChildEdge != null) {
			leftChildEdge.show();
		}
		if (rightChildEdge != null) {
			rightChildEdge.show();
		}
		rect.show();
	}

	@Override
	public void show(int delay) {
		for (AbstractNode node : nodes) {
			node.show(delay);
		}
		if (leftChildEdge != null) {
			leftChildEdge.show(delay);
		}
		if (rightChildEdge != null) {
			rightChildEdge.show(delay);
		}
		rect.show(new MsTiming(delay));
	}

	@Override
	public void moveBy(int dx, int dy, Timing duration) {
		super.moveBy(dx, dy, duration);
		rect.moveBy(null, dx, dy, null, duration);
		for (AbstractNode node : nodes) {
			node.moveBy(dx, dy, duration);
		}
	}
}
