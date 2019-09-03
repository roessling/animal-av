package generators.compression.shannon_fano.guielements.tree;

import java.awt.Color;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Polyline;
import algoanim.primitives.Text;
import algoanim.util.MsTiming;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.Timing;

/**
 * This class represents a edge in the tree.
 * 
 * @author Sebastian Fach
 */
public class TreeEdge {

	/**
	 * The corresponding tree
	 */
	private Tree tree;

	/**
	 * The primitive for the edge
	 */
	private Polyline edge;

	/**
	 * The primitive for the label of the edge
	 */
	private Text labelTxt;

	/**
	 * The label of the edge
	 */
	private String label;

	private int shiftY;

	/**
	 * The start node of the edge
	 */
	private AbstractNode startNode;

	/**
	 * The end node of the edge
	 */
	private AbstractNode endNode;

	/**
	 * @param startNode
	 *            The start node of this edge
	 * @param endNode
	 *            The end node of this edge
	 * @param label
	 *            The text of the edge label
	 */
	public TreeEdge(AbstractNode startNode, AbstractNode endNode, String label, int shiftY, Tree tree) {
		this.startNode = startNode;
		this.endNode = endNode;
		this.label = label;
		this.shiftY = shiftY;
		this.tree = tree;
		createPrimitives();
	}

	/**
	 * Creates primitives for the new edge.
	 */
	public void createPrimitives() {
		// edge
		Offset o1 = new Offset(0, 0, startNode.getBoundingRect(), AnimalScript.DIRECTION_S);
		Offset o2 = new Offset(0, shiftY, endNode.getBoundingRect(), AnimalScript.DIRECTION_N);
		edge = tree.getLang().newPolyline(new Node[] { o1, o2 }, "edge" + startNode.getId() + "to" + endNode.getId(),
				null);

		// label
		int deltaX = endNode.getCenterX() - startNode.getCenterX();
		// manual shift correction depending on the char: seems to be a bug in
		// Animal
		int shiftUpY = (label.equals("0")) ? 20 : 24;

		labelTxt = tree.getLang().newText(
				new Offset(deltaX / 3 * 2, Tree.STEP_SIZE_Y / 3 * 2 - shiftUpY, startNode.getBoundingRect(),
						AnimalScript.DIRECTION_C), label, "edgeLabel" + startNode.getId() + "to" + endNode.getId(),
				null);
	}

	public void hide() {
		edge.hide();
		labelTxt.hide();
	}

	public void show() {
		edge.show();
		labelTxt.show();
	}

	public void hide(int delay) {
		MsTiming timing = new MsTiming(delay);
		edge.hide(timing);
		labelTxt.hide(timing);
	}

	public void show(int delay) {
		MsTiming timing = new MsTiming(delay);
		edge.show(timing);
		labelTxt.show(timing);
	}

	public void highlight() {
		edge.changeColor("color", Color.RED, null, null);
		labelTxt.changeColor("color", Color.RED, null, null);
	}

	public void unhighlight() {
		edge.changeColor("color", Color.BLACK, null, null);
		labelTxt.changeColor("color", Color.BLACK, null, null);
	}

	public void moveBy(int dx, int dy, Timing duration) {
		edge.moveBy(null, dx, dy, null, duration);
		labelTxt.moveBy(null, dx, dy, null, duration);
	}

	//
	// private void moveZeroEdgeTo(Circle lowerCircle, TreeNode lowerNode,
	// TreeNode upperNode, Language lang, Timing duration) {
	//
	// int relativeY = (upperNode.getLayer() - lowerNode.getLayer())
	// * Tree.Y_STEP_SIZE + 11;
	//
	// Circle alignDummy = lang.newCircle(new Offset(0, relativeY,
	// lowerCircle, AnimalScript.DIRECTION_C), 15, "alignDummy", null);
	// alignDummy.hide();
	//
	// edge.moveTo(null, null, new Offset(0, 0, alignDummy,
	// AnimalScript.DIRECTION_C), null, duration);
	//
	// int relativeXzero = (upperNode.getxCoordinate() - lowerNode
	// .getxCoordinate());
	// int relativeYzero = (upperNode.getLayer() - lowerNode.getLayer())
	// * Tree.Y_STEP_SIZE;
	//
	// edgeText.moveTo(null, null, new Offset((relativeXzero - 11) / 2 - 9,
	// (relativeYzero - 15 - 11) / 2 - 2, lowerCircle,
	// AnimalScript.DIRECTION_C), null, duration);
	// }
	//
	// private void moveOneEdgeTo(Circle lowerCircle, TreeNode lowerNode,
	// TreeNode upperNode, Language lang, Timing duration) {
	//
	// int relativeY = (upperNode.getLayer() - lowerNode.getLayer())
	// * Tree.Y_STEP_SIZE;
	// int relativeX = upperNode.getxCoordinate() - lowerNode.getxCoordinate();
	//
	// Circle alignDummy = lang.newCircle(new Offset(relativeX, relativeY,
	// lowerCircle, AnimalScript.DIRECTION_C), 15, "alignDummy", null);
	// alignDummy.hide();
	//
	// edge.moveTo(null, null, new Offset(11, 11, alignDummy,
	// AnimalScript.DIRECTION_C), null, duration);
	//
	// int relativeXOne = (upperNode.getxCoordinate() - lowerNode
	// .getxCoordinate());
	// int relativeYOne = (upperNode.getLayer() - lowerNode.getLayer())
	// * Tree.Y_STEP_SIZE;
	//
	// edgeText.moveTo(null, null, new Offset((relativeXOne - 11) / 2 + 11,
	// (relativeYOne - 15 - 11) / 2 - 3, lowerCircle,
	// AnimalScript.DIRECTION_C), null, duration);
	// }
}
