package generators.compression.shannon_fano.guielements.tree;

import generators.compression.shannon_fano.style.ShannonFanoStyle;
import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Circle;
import algoanim.primitives.Rect;
import algoanim.primitives.Text;
import algoanim.properties.CircleProperties;
import algoanim.properties.TextProperties;
import algoanim.util.MsTiming;
import algoanim.util.Offset;
import algoanim.util.Timing;

/**
 * This class represents a node in the tree.
 * 
 * @author Sebastian Fach
 */
public class TreeNode extends AbstractNode {

	/**
	 * The symbol this leaf represents
	 */
	private char symbol;

	/**
	 * Stores whether the node is a leaf or not
	 */
	protected boolean isLeaf;

	/**
	 * The primitives for this node
	 */
	private Circle circle;
	private Text idTxt;
	private Text freqTxt;
	private Rect symbolRect;
	private Text symbolTxt;

	public char getSymbol() {
		return symbol;
	}

	public boolean isLeaf() {
		return isLeaf;
	}

	/**
	 * Constructor for inner nodes
	 * 
	 * @param id
	 *            The id of this node
	 * @param frequency
	 *            The frequency count
	 * @param probability
	 *            The probability
	 * @param centerX
	 *            The x-coordinate of the node's center
	 * @param centerY
	 *            The y-coordinate of the node's center
	 * @param tree
	 */
	public TreeNode(int frequency, float probability, int centerX, int centerY, Tree tree) {
		super(tree.nextNodeId(), tree);
		this.frequency = frequency;
		this.probability = probability;
		this.centerX = centerX;
		this.centerY = centerY;
		this.tree = tree;
		isLeaf = false;
		createPrimitives();
		createBoundingRect();
	}

	/**
	 * Constructor for leafs
	 * 
	 * @param id
	 *            The id of this node
	 * @param frequency
	 *            The frequency count
	 * @param probability
	 *            The probability
	 * @param symbol
	 *            The symbol of this node (this node is a leaf)
	 * @param centerX
	 *            The x-coordinate of the node's center
	 * @param centerY
	 *            The y-coordinate of the node's center
	 * @param tree
	 */
	public TreeNode(int frequency, float probability, char symbol, int centerX, int centerY, Tree tree) {
		super(tree.nextNodeId(), tree);
		this.frequency = frequency;
		this.probability = probability;
		this.symbol = symbol;
		this.centerX = centerX;
		this.centerY = centerY;
		this.tree = tree;
		isLeaf = true;
		createPrimitives();
		createBoundingRect();
	}

	/**
	 * Creates primitives for the new node.
	 */
	@Override
	protected void createPrimitives() {
		// circle
		Offset circleOffset = new Offset(centerX + 15, centerY + 65, tree.getOffsetReference(), AnimalScript.DIRECTION_SW);
		CircleProperties circleProp = (CircleProperties) tree.getStyle().getProperties(ShannonFanoStyle.CIRCLE);
		circle = tree.getLang().newCircle(circleOffset, 15, "node" + id, null, circleProp);

		// frequency
		Offset nodeFreqOffset = new Offset(0, -8, circle, AnimalScript.DIRECTION_C);
		TextProperties textProp = (TextProperties) tree.getStyle().getProperties(ShannonFanoStyle.FREQUENCY);
		freqTxt = tree.getLang().newText(nodeFreqOffset, Integer.toString(frequency), "node" + id + "Freq", null,
				textProp);

		// id
		Offset nodeIdOffset = new Offset(0, -26, freqTxt, AnimalScript.DIRECTION_C);
		textProp = (TextProperties) tree.getStyle().getProperties(ShannonFanoStyle.NODE_ID);
		idTxt = tree.getLang().newText(nodeIdOffset, Integer.toString(id), "node" + id + "NodeId", null, textProp);

		if (isLeaf) {
			// rect
			Offset upperLeft = new Offset(0, 2, circle, AnimalScript.DIRECTION_SW);
			Offset lowerRight = new Offset(0, 22, circle, AnimalScript.DIRECTION_SE);
			symbolRect = tree.getLang().newRect(upperLeft, lowerRight, "node" + this.getId() + "SymbolRect", null);

			// symbol
			Offset symbolOffset = new Offset(0, -4, symbolRect, AnimalScript.DIRECTION_C);
			textProp = (TextProperties) tree.getStyle().getProperties(ShannonFanoStyle.SYMBOL);
			symbolTxt = tree.getLang().newText(symbolOffset, Character.toString(symbol),
					"node" + this.getId() + "Symbol", null, textProp);
		}
	}

	@Override
	protected void createBoundingRect() {
		Offset upperLeft = new Offset(0, 0, circle, AnimalScript.DIRECTION_NW);
		if (isLeaf) {
			Offset lowerRight = new Offset(0, 0, symbolRect, AnimalScript.DIRECTION_SE);
			boundingRect = tree.getLang().newRect(upperLeft, lowerRight, "node" + this.getId() + "BoundingRect", null);
		} else {
			Offset lowerRight = new Offset(0, 0, circle, AnimalScript.DIRECTION_SE);
			boundingRect = tree.getLang().newRect(upperLeft, lowerRight, "node" + this.getId() + "BoundingRect", null);
		}
		boundingRect.hide();
	}

	@Override
	public void hide() {
		if (leftChildEdge != null) {
			leftChildEdge.hide();
		}
		if (rightChildEdge != null) {
			rightChildEdge.hide();
		}
		circle.hide();
		idTxt.hide();
		freqTxt.hide();
		if (isLeaf) {
			symbolRect.hide();
			symbolTxt.hide();
		}
		this.isVisible = false;
	}

	@Override
	public void hide(int delay) {
		if (leftChildEdge != null) {
			leftChildEdge.hide(delay);
		}
		if (rightChildEdge != null) {
			rightChildEdge.hide(delay);
		}
		MsTiming timing = new MsTiming(1000);
		circle.hide(timing);
		idTxt.hide(timing);
		freqTxt.hide(timing);
		if (isLeaf) {
			symbolRect.hide(timing);
			symbolTxt.hide(timing);
		}
		this.isVisible = false;
	}

	@Override
	public void show() {
		if (leftChildEdge != null) {
			leftChildEdge.show();
		}
		if (rightChildEdge != null) {
			rightChildEdge.show();
		}
		circle.show();
		idTxt.show();
		freqTxt.show();
		if (isLeaf) {
			symbolRect.show();
			symbolTxt.show();
		}
		this.isVisible = true;
	}

	@Override
	public void show(int delay) {
		if (leftChildEdge != null) {
			leftChildEdge.show(delay);
		}
		if (rightChildEdge != null) {
			rightChildEdge.show(delay);
		}
		MsTiming timing = new MsTiming(1000);
		circle.show(timing);
		idTxt.show(timing);
		freqTxt.show(timing);
		if (isLeaf) {
			symbolRect.show(timing);
			symbolTxt.show(timing);
		}
		this.isVisible = true;
	}

	@Override
	public void moveBy(int dx, int dy, Timing duration) {
		super.moveBy(dx, dy, duration);
		circle.moveBy(null, dx, dy, null, duration);
		idTxt.moveBy(null, dx, dy, null, duration);
		freqTxt.moveBy(null, dx, dy, null, duration);
		if (isLeaf) {
			symbolRect.moveBy(null, dx, dy, null, duration);
			symbolTxt.moveBy(null, dx, dy, null, duration);
		}
	}

	// /**
	// *
	// * @param target The upper left corner's target
	// * @param parent
	// * @param duration
	// * @return
	// */
	// public Circle moveTo(Offset target, TreeNode parent, Timing duration) {
	//
	// Circle alignDummy = lang.newCircle(target, 15, "alignDummy", null);
	// alignDummy.hide();
	//
	// Offset circleOffset = new Offset(target.getX() - 15, target.getY() - 15,
	// target.getRef(),
	// target.getDirection());
	// circle.moveTo(null, null, circleOffset, null, duration);
	//
	// String freq = freqTxt.getText();
	// int centerCorrection = freq.length() * 5;
	// Offset nodeFreqOffset = new Offset(-centerCorrection, -8, alignDummy,
	// AnimalScript.DIRECTION_C);
	// freqTxt.moveTo(null, null, nodeFreqOffset, null, duration);
	//
	// String number = idTxt.getText();
	// centerCorrection = number.length() * 2;
	// Offset nodeNoOffset = new Offset(-centerCorrection, -14, alignDummy,
	// AnimalScript.DIRECTION_C);
	//
	// idTxt.moveTo(null, null, nodeNoOffset, null, duration);
	//
	// if (upwardEdge != null) {
	// upwardEdge.moveTo(alignDummy, this, parent, lang, duration);
	// }
	//
	// return alignDummy;
	// }

	// From LeafSymbol:

	// public void moveTo(Circle alignDummy, Timing duration, Language lang) {
	//
	// Offset rectOffset = new Offset(0, 2, alignDummy,
	// AnimalScript.DIRECTION_SW);
	//
	// nodeRect.moveTo(null, null, rectOffset, null, duration);
	//
	// Rect rectAlignDummy = lang.newRect(new Offset(0, 2, alignDummy,
	// AnimalScript.DIRECTION_SW), new Offset(0, 22, alignDummy,
	// AnimalScript.DIRECTION_SE), "alignDummy", null);
	// rectAlignDummy.hide();
	//
	// Offset nodeCharOffset = new Offset(-3, -10, rectAlignDummy,
	// AnimalScript.DIRECTION_C);
	//
	// symbolTxt.moveTo(null, null, nodeCharOffset, null, duration);
	//
	// }
}
