package generators.misc.arithconvert;

import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

import algoanim.primitives.Graph;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

/**
 * A simple literal expression (numbers, variables)
 * 
 * @author Jannis Weil, Hendrik Wuerz
 */
public class Literal extends Expression {

	String literal;

	/**
	 * Create a literal with a String representation of the given object.
	 * 
	 * @param obj
	 *            the object which is used to represent this literal.
	 */
	public Literal(Object obj) {
		this.literal = String.valueOf(obj);
	}

	@Override
	public String toString() {
		return literal;
	}

	@Override
	public Text createTexts(Language lang, TextProperties textProperties,
			Node position) {
		Text t = lang.newText(position, literal, getMainTextName(), null,
				textProperties);
		textNW = new Offset(-textHighlightPadding, -textHighlightPadding, t,
				"NW");
		;
		textSE = new Offset(textHighlightPadding, textHighlightPadding, t, "SE");
		return t;
	}

	@Override
	public int getDepth() {
		return 1;
	}

	@Override
	public Coordinates getLeftLeafCoordinates(Coordinates from) {
		return from;
	}

	@Override
	public void createNodes(Coordinates position, LinkedList<Node> nodes,
			LinkedList<String> labels, HashMap<Node, Set<Node>> connections) {
		// depth is 0, just draw this node at the intended position
		graphNode = position;
		nodes.add(graphNode);
		labels.add(literal);
	}

	@Override
	protected void setGraph(Graph g) {
		graph = g;
	}

	@Override
	public void highlightTree(TreehighlightMode mode) {
		if (mode == TreehighlightMode.Edges) {
			return;
		}
		highlightNode();
	}

	@Override
	public void unhighlightTree(TreehighlightMode mode) {
		if (mode == TreehighlightMode.Edges) {
			return;
		}
		unhighlightNode();
	}

	@Override
	public Text convertToLRPostfix(Language lang, Node position,
			Node stackPosition, TextProperties textProperties,
			RectProperties rp, RectProperties rpOp, RectProperties rpNoLiteral,
			RectProperties rpPushPop) {
		// highlight the text and the node of this literal
		highlightTree(TreehighlightMode.Nodes);
		highlightText(lang, rp);

		// only for code highlighting
		ArithConverter.unHighlightLR();
		ArithConverter.doCodeStep(lang, ArithConverter.LR_L_START);
		ArithConverter.unHighlightLR(ArithConverter.LR_L_START);
		ArithConverter.doCodeStep(lang, ArithConverter.LR_L_HANDLE);

		// create a text representation of this literal in the LR output
		Text t = lang.newText(position, literal, getMainTextName() + "_LR",
				null, textProperties);
		setTreeHighlightColor((Color) rpOp.get("color"));
		lang.nextStep("> " + this);

		// unhighlight the text of this literal (the node may change color later
		// in a BinaryExpression)
		unhighlightText();
		ArithConverter.unHighlightLR(ArithConverter.LR_L_HANDLE);
		return t;
	}

	@Override
	public void setTreeHighlightColor(Color color) {
		if (graph == null) {
			return;
		}
		graph.setNodeHighlightFillColor(graphNode, color, null, null);
	}

	@Override
	protected void convertToPostfix(ConvertInformation info, boolean lr,
			int currentStackSize) {
		info.postOrder += literal;
		info.numberOfLiterals++;
	}

	@Override
	public int getSubExpressionsCount() {
		return 1;
	}

}
