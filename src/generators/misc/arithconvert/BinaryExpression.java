package generators.misc.arithconvert;

import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import algoanim.primitives.Graph;
import algoanim.primitives.Rect;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

/**
 * Binary arithmetic expression which can be converted to String or printed to
 * Text.
 * 
 * @author Jannis Weil, Hendrik Wuerz
 */
public class BinaryExpression extends Expression {

	/**
	 * The left subexpression
	 */
	Expression left;

	/**
	 * The right subexpression
	 */
	Expression right;

	/**
	 * The operator of this expression
	 */
	Operator operator;

	/**
	 * The coordinates (NW) of the operator highlight rectangle
	 */
	Node textOpNW;

	/**
	 * The coordinates (SE) of the operator highlight rectangle
	 */
	Node textOpSE;

	/**
	 * The rectangle object used to highlight the operator
	 */
	Rect textHighlightOperator;

	/**
	 * Create a new binary expression
	 * 
	 * @param left
	 *            the left subexpression
	 * @param right
	 *            the right subexpression
	 * @param operator
	 *            the operator used in this expression
	 */
	public BinaryExpression(Expression left, Expression right, Operator operator) {
		this.left = left;
		this.right = right;
		this.operator = operator;
	}

	@Override
	public String toString() {
		return "(" + left.toString() + " " + getOperatorString(operator) + " "
				+ right.toString() + ")";
	}

	@Override
	public Text createTexts(Language lang, TextProperties textProperties,
			Node position) {
		Text leftParenthesisText = lang.newText(position, "(",
				getMainTextName() + "_leftpar", null);
		Text leftText = left.createTexts(lang, textProperties, new Offset(
				xOffset, 0, leftParenthesisText, "NE"));
		Text opText = lang.newText(new Offset(xOffset, 0, leftText, "NE"),
				getOperatorString(operator), getMainTextName() + "_op", null);
		Text rightText = right.createTexts(lang, textProperties, new Offset(
				xOffset, 0, opText, "NE"));

		Text rightParenthesisText = lang.newText(new Offset(xOffset, 0,
				rightText, "NE"), ")", getMainTextName() + "_rightpar", null);

		textNW = new Offset(-textHighlightPadding, -textHighlightPadding,
				leftParenthesisText, "NW");
		textSE = new Offset(textHighlightPadding, textHighlightPadding,
				rightParenthesisText, "SE");

		textOpNW = new Offset(-textOperatorHighlightPadding,
				-textOperatorHighlightPadding, opText, "NW");
		textOpSE = new Offset(textOperatorHighlightPadding,
				textOperatorHighlightPadding, opText, "SE");

		return rightParenthesisText;
	}

	@Override
	public int getDepth() {
		return 1 + Math.max(left.getDepth(), right.getDepth());
	}

	/**
	 * @return the coordinates of the left expression node.
	 */
	private Coordinates getLeftCoordinates(Coordinates from) {
		int newY = from.getY() + (int) (2.5 * nodeRadius);
		int leftNodeCount = left instanceof BinaryExpression ? 1 + ((BinaryExpression) left).right
				.getSubExpressionsCount() : 1;
		// per depth we go 1.5 left (0.25 margin)
		int elementSize = (int) (2 * nodeRadius);
		// there are two sides for each depth, plus the main distance
		int leftNodes = (int) (elementSize * leftNodeCount);
		// place the node in the center of the size
		return new Coordinates(from.getX() - leftNodes, newY);
	}

	/**
	 * @return the coordinates of the right expression node.
	 */
	private Coordinates getRightCoordinates(Coordinates from) {
		int newY = from.getY() + (int) (2.5 * nodeRadius);
		int elementSize = (int) (2 * nodeRadius);
		int rightNodeCount = right instanceof BinaryExpression ? 1 + ((BinaryExpression) right).left
				.getSubExpressionsCount() : 1;
		int rightSize = (int) (elementSize * rightNodeCount);
		return new Coordinates(from.getX() + rightSize, newY);
	}

	@Override
	public Coordinates getLeftLeafCoordinates(Coordinates from) {
		return left.getLeftLeafCoordinates(getLeftCoordinates(from));
	}

	@Override
	public void createNodes(Coordinates position, LinkedList<Node> nodes,
			LinkedList<String> labels, HashMap<Node, Set<Node>> connections) {
		graphNode = position;
		// add current node at the intended position
		nodes.add(graphNode);
		// add the current operator String to the list of nodes
		labels.add(getOperatorString(operator));

		// check where the next nodes should go
		Coordinates leftCoords = getLeftCoordinates(position);
		left.createNodes(leftCoords, nodes, labels, connections);

		// same for the right
		Coordinates rightCoords = getRightCoordinates(position);
		right.createNodes(rightCoords, nodes, labels, connections);

		// add the connections from this node to left and right nodes
		Set<Node> nextNodes = new HashSet<>();
		nextNodes.add(leftCoords);
		nextNodes.add(rightCoords);
		connections.put(graphNode, nextNodes);
	}

	@Override
	protected void setGraph(Graph g) {
		graph = g;
		left.setGraph(g);
		right.setGraph(g);
	}

	@Override
	public void highlightTree(TreehighlightMode mode) {
		if (mode != TreehighlightMode.Edges) {
			highlightNode();
		}
		if (graph != null && mode != TreehighlightMode.Nodes) {
			graph.highlightEdge(graphNode, left.graphNode, null, null);
			graph.highlightEdge(graphNode, right.graphNode, null, null);
		}
		left.highlightTree(mode);
		right.highlightTree(mode);
	}

	@Override
	public void unhighlightTree(TreehighlightMode mode) {
		if (mode != TreehighlightMode.Edges) {
			unhighlightNode();
		}
		if (graph != null && mode != TreehighlightMode.Nodes) {
			graph.unhighlightEdge(graphNode, left.graphNode, null, null);
			graph.unhighlightEdge(graphNode, right.graphNode, null, null);
		}
		left.unhighlightTree(mode);
		right.unhighlightTree(mode);
	}

	/**
	 * Highlight the (text) operator associated with this expression.
	 * 
	 * @param lang
	 *            the language used to draw a rectangle around the operator
	 * @param rp
	 *            the properties of this rectangle
	 */
	public void highlightTextOperator(Language lang, RectProperties rp) {
		if (textOpNW != null && textOpSE != null) {
			textHighlightOperator = lang.newRect(textOpNW, textOpSE,
					getMainTextName() + "_highrect_op", null, rp);
		}
	}

	/**
	 * Unhighlight the (text) operator of this expression.
	 */
	public void unhighlightTextOperator() {
		if (textHighlightOperator != null) {
			textHighlightOperator.hide();
		}
	}

	@Override
	public Text convertToLRPostfix(Language lang, Node position,
			Node stackPosition, TextProperties textProperties,
			RectProperties rp, RectProperties rpOp, RectProperties rpNoLiteral,
			RectProperties rpPushPop) {
		// highlight this subtree and the text
		highlightTree(TreehighlightMode.Nodes);
		highlightText(lang, rp);

		// do a step in the code
		ArithConverter.unHighlightLR();
		ArithConverter.doCodeStep(lang, ArithConverter.LR_B_START);
		// show the start
		lang.nextStep();
		ArithConverter.unHighlightLR(ArithConverter.LR_B_START);

		// we start with converting the left expression
		ArithConverter.doCodeStep(lang, ArithConverter.LR_B_LEFT);
		lang.nextStep();
		// unhighlight everything
		ArithConverter.unHighlightLR(ArithConverter.LR_B_LEFT);
		unhighlightTree(TreehighlightMode.Nodes);
		unhighlightText();

		// and start with the conversion of the left subexpression
		Text tLeft = left.convertToLRPostfix(lang, position, stackPosition,
				textProperties, rp, rpOp, rpNoLiteral, rpPushPop);
		Text tRight;

		// after left: highlight the left subtree and text green (done)
		left.setTreeHighlightColor((Color) rpOp.get("color"));
		left.unhighlightText();
		left.highlightText(lang, rpOp);
		highlightTree(TreehighlightMode.Nodes);

		// right is a literal -> we can directly op the right
		if (right instanceof Literal) {
			// highlight the right text
			right.highlightText(lang, rp);
			// and show that we did the if-case
			ArithConverter.doCodeStep(lang, ArithConverter.LR_B_IF);
			lang.nextStep();
			left.unhighlightText();
			unhighlightText();
			right.unhighlightText();
			ArithConverter.unHighlightLR(ArithConverter.LR_B_IF);

			// now prepare converting the right expression
			ArithConverter.doCodeStep(lang, ArithConverter.LR_B_T_RIGHT);
			highlightText(lang, rp);
			lang.nextStep();
			unhighlightText();
			unhighlightNode();

			// actually convert now
			tRight = right.convertToLRPostfix(lang, new Offset(xOffset, 0,
					tLeft, "NE"), stackPosition, textProperties, rp, rpOp,
					rpNoLiteral, rpPushPop);
		}
		// right is no literal -> we have to use stack ops
		else {
			// show that right is no literal
			right.highlightText(lang, rpNoLiteral);
			ArithConverter.doCodeStep(lang, ArithConverter.LR_B_ELSE);

			lang.nextStep();
			ArithConverter.unHighlightLR(ArithConverter.LR_B_ELSE);

			// we have to push now
			ArithConverter.doCodeStep(lang, ArithConverter.LR_B_F_PUSH);
			left.unhighlightText();
			left.highlightText(lang, rpPushPop);
			left.highlightTree(TreehighlightMode.Nodes);
			left.setTreeHighlightColor((Color) rpPushPop.get("color"));

			// actually push
			// add the push-string to the LR representation
			Text tPush = lang.newText(new Offset(xOffset, 0, tLeft, "NE"),
					push, getMainTextName() + "_LR_push", null, textProperties);
			Text tStackEntry = lang.newText(stackPosition, left.toString(),
					getMainTextName() + "_LR_push_stackentry", null,
					textProperties);

			lang.nextStep();
			ArithConverter.unHighlightLR(ArithConverter.LR_B_F_PUSH);
			left.unhighlightText();
			right.unhighlightText();
			ArithConverter.unHighlightLR(ArithConverter.LR_B_F_PUSH);

			// now look at the right expression
			ArithConverter.doCodeStep(lang, ArithConverter.LR_B_F_RIGHT);
			unhighlightText();
			highlightText(lang, rp);

			lang.nextStep();
			unhighlightText();
			unhighlightNode();
			// now handle the right expression
			tRight = right.convertToLRPostfix(lang, new Offset(xOffset, 0,
					tPush, "NE"), new Offset(0, 5, tStackEntry, "SW"),
					textProperties, rp, rpOp, rpNoLiteral, rpPushPop);
			right.highlightTree(TreehighlightMode.Nodes);

			// pop afterwards
			left.highlightText(lang, rpPushPop);
			highlightNode();
			ArithConverter.doCodeStep(lang, ArithConverter.LR_B_F_POP);
			Text tPop = lang.newText(new Offset(xOffset, 0, tRight, "NE"), pop,
					getMainTextName() + "_LR_pop", null, textProperties);
			left.setTreeHighlightColor((Color) rpOp.get("color"));
			tStackEntry.hide(); // hide the stack entry

			lang.nextStep();
			ArithConverter.unHighlightLR(ArithConverter.LR_B_F_POP);
			left.unhighlightText();

			// pop is the most right text
			tRight = tPop;
		}

		// highlight this expression again
		highlightText(lang, rp);
		// and do the correct step depending on whether we are in the if or in
		// the else case
		if (right instanceof Literal) {
			ArithConverter.doCodeStep(lang, ArithConverter.LR_B_T_APPLY);
		} else {
			ArithConverter.doCodeStep(lang, ArithConverter.LR_B_F_APPLY);
			right.unhighlightTree(TreehighlightMode.Nodes);
			left.unhighlightTree(TreehighlightMode.Nodes);
		}

		// in the last step, highlight the operator and the corresponding tree
		highlightTextOperator(lang, rpOp);
		highlightTree(TreehighlightMode.Nodes);
		// highlight the tree in the specific operator-color
		setTreeHighlightColor((Color) rpOp.get("color"));
		// add the op text to the LR representation
		Text t = lang.newText(new Offset(xOffset, 0, tRight, "NE"),
				getOperatorString(operator), getMainTextName() + "_LR", null,
				textProperties);

		// show that we handled the current expression and unhighlight
		// everything afterwards
		lang.nextStep("> " + this);
		unhighlightText();
		ArithConverter.unHighlightLR();
		unhighlightTextOperator();
		unhighlightTree(TreehighlightMode.Nodes);

		return t;
	}

	@Override
	public void setTreeHighlightColor(Color color) {
		if (graph == null) {
			return;
		}
		graph.setNodeHighlightFillColor(graphNode, color, null, null);
		left.setTreeHighlightColor(color);
		right.setTreeHighlightColor(color);
	}

	@Override
	protected void convertToPostfix(ConvertInformation info, boolean lr,
			int currentStackSize) {
		Expression first, second;
		if (lr) {
			first = left;
			second = right;
		} else {
			first = right;
			second = left;
		}

		first.convertToPostfix(info, lr, currentStackSize);
		if (second instanceof Literal) {
			info.postOrder += " "; // space to separe a bit
			second.convertToPostfix(info, lr, currentStackSize);
		} else {
			// push
			info.postOrder += push;
			int nextStackSize = currentStackSize + 1;
			info.stackOperations++;
			info.maxStackSize = Math.max(info.maxStackSize, nextStackSize);

			// do right
			second.convertToPostfix(info, lr, nextStackSize);

			// pop (we cannot get a bigger maxStackSize here)
			info.stackOperations++;
			info.postOrder += pop;
		}

		// apply op
		info.postOrder += getOperatorString(operator);
		info.numberOfExpressions++;
	}

	@Override
	public int getSubExpressionsCount() {
		return 1 + left.getSubExpressionsCount()
				+ right.getSubExpressionsCount();
	}

}
