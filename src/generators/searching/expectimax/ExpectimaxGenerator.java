package generators.searching.expectimax;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Circle;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.Triangle;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.TriangleProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class ExpectimaxGenerator implements ValidatingGenerator {
	private Language lang;

	private int nodeWidth; // 30
	private int lineOffset;
	private SourceCodeProperties sourceCodeProperties;
	private TextProperties descriptionTextProperties;
	private TextProperties headerTextProperties;
	private RectProperties headerRectangleProperties;
	private Color textHighlightColor;
	private Color nodeHighlightColor;
	private String tree;
	private TextProperties animationTextProperties;
	private Color pathHighlightColor;
	private int layerHeight;
	private final int ANIM_WIDTH = 800; // 640;
	private final int ANIM_HEIGHT = 600; // 480;
	private int leafPadding;

	private int nodeXOffset;
	private int nodeYOffset;
	private static final int CHANCE_MARGIN = 2;

	private Text headerText;
	private Rect headerRect;
	private SourceCode codeEval;
	private SourceCode codeMax;
	private SourceCode codeMin;
	private SourceCode codeChance;

	private int bMax;
	private int bMin;
	private int mMax;
	private int mMin;
	private int mCur;
	private Double maxChoice;
	private int nodeCount;

	private int nextLeafPos;

	/*
	 * Auto-generated functions
	 */

	public void init() {
		lang = new AnimalScript("Expectiminimax", "Simon Bunten,Martin Oehler",
				ANIM_WIDTH, ANIM_HEIGHT);
		lang.setStepMode(true);
		headerText = null;
		headerRect = null;
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		tree = (String) primitives.get("tree");
		try {
			createTree(tree);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Tree syntax error: "
					+ e.getMessage() + "\n" + tree);
		}
		
		if ((Integer) primitives.get("nodeWidth") < 0){
			throw new IllegalArgumentException("The argument nodeWidth is " +(Integer) primitives.get("nodeWidth") + " but must be nonnegative");
		}
		if ((Integer) primitives.get("lineOffset") < 0){
			throw new IllegalArgumentException("The argument nodeWidth is " +(Integer) primitives.get("lineOffset") + " but must be nonnegative");
		}
		if ((Integer) primitives.get("layerHeight") < 0){
			throw new IllegalArgumentException("The argument nodeWidth is " +(Integer) primitives.get("layerHeight") + " but must be nonnegative");
		}
		if ((Integer) primitives.get("leafPadding") < 0){
			throw new IllegalArgumentException("The argument nodeWidth is " +(Integer) primitives.get("leafPadding") + " but must be nonnegative");
		}
				
		return true;
	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		nodeWidth = (Integer) primitives.get("nodeWidth");
		lineOffset = (Integer) primitives.get("lineOffset");
		headerTextProperties = (TextProperties) props
				.getPropertiesByName("headerTextProperties");
		headerRectangleProperties = (RectProperties) props
				.getPropertiesByName("headerRectangleProperties");
		sourceCodeProperties = (SourceCodeProperties) props
				.getPropertiesByName("sourceCodeProperties");
		descriptionTextProperties = (TextProperties) props
				.getPropertiesByName("descriptionTextProperties");
		textHighlightColor = (Color) primitives.get("textHighlightColor");
		nodeHighlightColor = (Color) primitives.get("nodeHighlightColor");
		tree = (String) primitives.get("tree");
		animationTextProperties = (TextProperties) props
				.getPropertiesByName("animationTextProperties");
		leafPadding = (Integer) primitives.get("leafPadding");
		pathHighlightColor = (Color) primitives.get("pathHighlightColor");
		layerHeight = (Integer) primitives.get("layerHeight");

		nodeXOffset = nodeWidth / 2;
		nodeYOffset = (int) Math.round(1.7321 * nodeXOffset);

		animate(tree);

		return lang.toString();
	}

	public String getName() {
		return "Expectiminimax";
	}

	public String getAlgorithmName() {
		return "Expectiminimax";
	}

	public String getAnimationAuthor() {
		return "Simon Bunten,Martin Oehler";
	}

	public String getDescription() {
		return "<i>Expectiminimax</i> (kurz Expectimax) entstammt der Spieletheorie und ist eine Erweiterung des <i>Minimax</i> um "
				+ "Erwartungswerte. <i>Minimax</i> dient zur Ermittlung der optimalen Spielstrategie für Nullsummenspiele mit "
				+ "perfekter Information. Zwei gegnerische Spieler führen (in der Regel, aber nicht ausschließlich) "
				+ "abwechselnd Züge aus. Der MAX-Spieler versucht den Wert des aktuellen Knotens zu maximieren, der MIN-Spieler "
				+ "hingegen versucht den Wert zu minimieren."
				+ "<br> <br>"
				+ "Der <i>Expectiminimax</i>-Algorithmus erweitert dieses Schema um Zufallsknoten, womit auch Spiele mit "
				+ "Zufallseinfluss modelliert werden können. Als Grundlage dazu dient der Erwartungswert der möglichen "
				+ "Ergebnisse."
				+ "<br> <br>"
				+ "<a href='http://de.wikipedia.org/wiki/Minimax-Algorithmus'>http://de.wikipedia.org/wiki/Minimax-Algorithmus</a>"
				+ "<br>"
				+ "<a href='http://de.wikipedia.org/wiki/Expectiminimax-Algorithmus'>http://de.wikipedia.org/wiki/Expectiminimax-Algorithmus</a>"
				+ "<br> <br>"
				+ "Folgende Grammatik gibt die Syntax in EBNF für die Erstellung eines Baumes an:"
				+ "<br> <br>"
				+ "<table>"
				+ "<tr> <td>node</td> 		<td>= max_node | min_node | chance_node | leaf </td></tr>"
				+ "<tr>	<td>max_node</td> 	<td>= 'max{', node,{node} '}'</td> </tr>"
				+ "<tr>	<td>min_node</td> 	<td>= 'min{', node, {node}, '}'</td> </tr>"
				+ "<tr>	<td>chance_node</td><td>= '%{', chance, '*', node, {chance, '*', node}, '}'</td> </tr>"
				+ "<tr>	<td>leaf</td> 		<td>= ['-'], number, {number}</td> </tr>"
				+ "<tr>	<td>chance</td>		<td>= '0.', number, {number}</td> </tr>"
				+ "<tr>	<td>number</td> 	<td>= '0' | .. | '9'</td> </tr>"
				+ "</table>";
	}

	public String getCodeExample() {
		return stringCodeEval + "\n" + "\n" + stringCodeMax + "\n" + "\n"
				+ stringCodeMin + "\n" + "\n" + stringCodeChance;
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.GERMAN;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_SEARCH);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	/*
	 * Generator code
	 */

	public void animate(String tree) {
		// parse tree
		TreeReturn returnTree = null;
		try {
			returnTree = createTree(tree);
		} catch (IllegalArgumentException e) {
			return;
		}
		ExpectimaxNode root = returnTree.getNode();

		// Introduction
		showHeader();
		showDescription();

		// main algorithm

		// debugging anim size
		// Node[] nodes = { new Coordinates(0, 0),
		// new Coordinates(0, ANIM_HEIGHT),
		// new Coordinates(ANIM_WIDTH, ANIM_HEIGHT),
		// new Coordinates(ANIM_WIDTH, 0) };
		// lang.newPolyline(nodes, "frame", null);

		expectimax(root);
		lang.nextStep("Optimale Strategie");
		lang.hideAllPrimitives();

		// final slides
		showHeader();
		showSummary();
		lang.nextStep("Zusammenfassung");

	}

	private TreeReturn createTree(String tree) throws IllegalArgumentException {
		if (tree.equals("")) {
			throw new IllegalArgumentException("Tree is empty");
		}
		ExpectimaxNode node;
		if (Character.isAlphabetic(tree.charAt(0))) { // min or max node
			// max or min node
			if (tree.startsWith("max")) { // max node
				node = new ExpectimaxNode(NodeType.MAX);
			} else {
				if (tree.startsWith("min")) { // min node
					node = new ExpectimaxNode(NodeType.MIN);
				} else {
					throw new IllegalArgumentException(
							"Invalid node identifier.");
				}
			}
			// parse sub trees
			int openBracketPos = tree.indexOf('{');
			int closeBracketPos = findClosingBracket(openBracketPos, tree);
			if (closeBracketPos == -1) {
				throw new IllegalArgumentException("Unbalanced brackets");
			}
			String subtrees = tree.substring(openBracketPos + 1,
					closeBracketPos).trim();
			while (!subtrees.equals("")) {
				TreeReturn treeReturn = createTree(subtrees); // recursive call
																// on subtrees
				node.addChild(treeReturn.getNode());
				subtrees = subtrees.substring(treeReturn.getLength()).trim();
			}
			return new TreeReturn(node, closeBracketPos + 1);
		}
		if (Character.isDigit(tree.charAt(0)) || tree.charAt(0) == '-') { // leaf
			int end_pos = 1;
			while (end_pos < tree.length()
					&& Character.isDigit(tree.charAt(end_pos))) {
				end_pos++;
			}
			int number = Integer.parseInt(tree.substring(0, end_pos));
			node = new ExpectimaxNode(number);
			return new TreeReturn(node, end_pos);
		}
		if (tree.charAt(0) == '%') { // chance node
			node = new ExpectimaxNode(NodeType.CHANCE);
			int openBracketPos = tree.indexOf('{');
			int closeBracketPos = findClosingBracket(openBracketPos, tree);
			if (closeBracketPos == -1) {
				throw new IllegalArgumentException("Unbalanced brackets");
			}
			String subtrees = tree.substring(openBracketPos + 1,
					closeBracketPos).trim();
			Double totalChance = 0.0;
			while (!subtrees.equals("")) {
				int starPos = subtrees.indexOf('*');
				if (starPos == -1) {
					throw new IllegalArgumentException(
							"Expected '*' after chance.");
				}
				Double chance;
				try {
					chance = Double.parseDouble(subtrees.substring(0, starPos));
				} catch (NumberFormatException e) {
					throw new IllegalArgumentException(
							"Invalid number as chance.");
				}
				totalChance += chance;
				subtrees = subtrees.substring(starPos + 1);
				TreeReturn treeReturn = createTree(subtrees);
				node.addChildWithChance(chance, treeReturn.getNode());
				subtrees = subtrees.substring(treeReturn.getLength()).trim();
			}
			if (totalChance == 1.0) {
				return new TreeReturn(node, closeBracketPos + 1);
			} else {
				throw new IllegalArgumentException(
						"Probabilities of a chance node have to add up to 1.");
			}

		}
		throw new IllegalArgumentException("Unknown token.");
	}

	private int findClosingBracket(int openBracketPos, String tree) {
		int counter = 1;
		int position = openBracketPos;
		while (counter != 0 && position < tree.length()) {
			position++;
			if (tree.charAt(position) == '{') {
				counter++;
			} else {
				if (tree.charAt(position) == '}') {
					counter--;
				}
			}
		}
		if (counter == 0) {
			return position;
		}
		return -1; // unbalanced brackets
	}

	private void expectimax(ExpectimaxNode root) {
		int leafCount = countLeaves(root);
		int leafSpace = nodeWidth + leafPadding;
		int treeWidth = leafSpace * leafCount;

		drawSourceCode(treeWidth + nodeWidth + leafPadding);
		drawTree(root, leafSpace);
		lang.nextStep("Initialer Baum");
		bMin = Integer.MAX_VALUE;
		bMax = 0;
		mMax = 0;
		mMin = Integer.MAX_VALUE;
		mCur = 0;
		maxChoice = 0.0;
		nodeCount = 0;
		evaluate(null, root);
	}

	private double evaluate(ExpectimaxNode source, ExpectimaxNode node) {
		++nodeCount;
		Polyline evalPath = null;
		if (source != null) {
			source.changeFillColor(Color.WHITE);
		}
		if (node.getChildren() != null && node.getChildren().size() > bMax)
			bMax = node.getChildren().size();
		if (node.getChildren() != null && node.getChildren().size() < bMin)
			bMin = node.getChildren().size();
		node.changeFillColor(nodeHighlightColor);
		//lang.nextStep("Auswertung eines " + node.getType().toString()
		//		+ "-Knotens.");
		Double result = 0.0;
		switch (node.getType()) {
		case MAX:
			codeEval.highlight(2);
			lang.nextStep();
			codeEval.unhighlight(2);
			++mCur;
			result = max(node);
			--mCur;
			codeEval.highlight(2);
			lang.nextStep();
			codeEval.unhighlight(2);
			break;
		case MIN:
			codeEval.highlight(3);
			lang.nextStep();
			codeEval.unhighlight(3);
			++mCur;
			result = min(node);
			--mCur;
			codeEval.highlight(3);
			lang.nextStep();
			codeEval.unhighlight(3);
			break;
		case CHANCE:
			codeEval.highlight(4);
			lang.nextStep();
			codeEval.unhighlight(4);
			++mCur;
			result = chance(node);
			--mCur;
			codeEval.highlight(4);
			lang.nextStep();
			codeEval.unhighlight(4);
			break;
		case LEAF:
			codeEval.highlight(5);
			lang.nextStep();
			if (mCur > mMax)
				mMax = mCur;
			if (mCur < mMin)
				mMin = mCur;
			result = node.getValue();
			node.changeFillColor(Color.WHITE);
			codeEval.unhighlight(5);
		}
		node.changeValueTextColor(textHighlightColor);
		node.changeFillColor(Color.WHITE);
		if (mCur == 0)
			maxChoice = result;
		return result;
	}

	private Double chance(ExpectimaxNode node) {
		node.setValue(0);
		codeChance.highlight(1);
		drawValueText(node);
		codeChance.unhighlight(1);
		for (int i = 0; i < node.childCount(); i++) {
			codeChance.highlight(3);
			Polyline evalPath = drawEvalPath(node, node.getChild(i));
			lang.nextStep();
			codeChance.unhighlight(3);
			node.changeFillColor(Color.WHITE);
			node.getChild(i).changeFillColor(nodeHighlightColor);
			Double nextEval = evaluate(node, node.getChild(i));
			codeChance.highlight(4);
			node.changeFillColor(nodeHighlightColor);
			Text multText = drawMultText(node, node.getChance(i), nextEval);
			node.setValue(node.getValue() + nextEval * node.getChance(i));
			drawValueText(node);
			multText.hide();
			codeChance.unhighlight(4);
			evalPath.changeColor("color", Color.BLACK, null, null);
		}
		return node.getValue();
	}

	private Double min(ExpectimaxNode node) {
		if (node.childCount() < 1) {
			return 0.0; // max must have at least 1 child
		}
		codeMin.highlight(1);
		node.setValue(Double.MAX_VALUE);
		drawValueText(node);
		codeMin.unhighlight(1);
		for (int i = 0; i < node.childCount(); i++) {
			codeMin.highlight(3);
			Polyline evalPath = drawEvalPath(node, node.getChild(i));
			lang.nextStep();
			codeMin.unhighlight(3);
			node.changeFillColor(Color.WHITE);
			Double newValue = evaluate(node, node.getChild(i));
			codeMin.highlight(4);
			node.changeFillColor(nodeHighlightColor);
			Text compText = drawComparisonText(node, newValue);
			codeMin.unhighlight(4);
			if (newValue < node.getValue()) {
				codeMin.highlight(5);
				node.setValue(newValue);
				drawValueText(node);
				codeMin.unhighlight(5);
			}
			compText.hide();
			evalPath.changeColor("color", Color.BLACK, null, null);
		}
		return node.getValue();
	}

	private Double max(ExpectimaxNode node) {
		if (node.childCount() < 1) {
			return 0.0; // max must have at least 1 child
		}
		codeMax.highlight(1);
		node.setValue(Double.MIN_VALUE);
		drawValueText(node);
		codeMax.unhighlight(1);
		for (int i = 0; i < node.childCount(); i++) {
			codeMax.highlight(3);
			Polyline evalPath = drawEvalPath(node, node.getChild(i));
			lang.nextStep();
			codeMax.unhighlight(3);
			node.changeFillColor(Color.WHITE);
			Double newValue = evaluate(node, node.getChild(i));
			codeMax.highlight(4);
			node.changeFillColor(nodeHighlightColor);
			Text compText = drawComparisonText(node, newValue);
			codeMax.unhighlight(4);
			if (newValue > node.getValue()) {
				codeMax.highlight(5);
				node.setValue(newValue);
				drawValueText(node);
				codeMax.unhighlight(5);
			}
			evalPath.changeColor("color", Color.BLACK, null, null);
			compText.hide();
		}
		return node.getValue();
	}

	private Polyline drawEvalPath(ExpectimaxNode node1, ExpectimaxNode node2) {
		int index = node1.getChildIndex(node2);
		if (index != -1) {
			Polyline line = node1.getLine(index);
			line.changeColor("color", pathHighlightColor, null, null);
			return line;
		}
		return null;
	}

	private String formatDouble(Double d) {
		String formatString = "";
		if (d == Double.MIN_VALUE) {
			formatString = "-∞";
		} else if (d == Double.MAX_VALUE) {
			formatString = "∞";
		} else {
			formatString = new java.text.DecimalFormat("#.##").format(d);
		}
		return formatString;
	}

	private void drawValueText(ExpectimaxNode node) {
		if (node.getValueText() != null) {
			node.getValueText().hide();
		}
		node.setValueText(drawText(formatDouble(node.getValue()), node
				.getPosition().getX() - nodeXOffset,
				node.getPosition().getY() - 10, animationTextProperties)[0]);
		int textWidth = getTextWidth(node.getValueText());
		node.getValueText().moveBy("translate", -textWidth - 5, 0, null, null);
		lang.nextStep();
	}

	private Text drawComparisonText(ExpectimaxNode node, Double newValue) {
		String compareSign = "";
		if (node.getType() == NodeType.MAX) {
			compareSign = ">";
		} else if (node.getType() == NodeType.MIN) {
			compareSign = "<";
		}
		Text text = drawText(formatDouble(newValue) + " " + compareSign + " "
				+ formatDouble(node.getValue()) + "?", node.getPosition()
				.getX() - nodeXOffset, node.getPosition().getY() - 30,
				animationTextProperties)[0];
		int textWidth = getTextWidth(text);
		text.moveBy("translate", -textWidth, 0, null, null);
		lang.nextStep();
		return text;
	}

	private Text drawMultText(ExpectimaxNode node, Double chance, Double eval) {
		DecimalFormat decimalFormat = new java.text.DecimalFormat("#.##");
		String chanceString = decimalFormat.format(chance);
		String evalString = decimalFormat.format(eval);
		String valueString = decimalFormat.format(node.getValue());

		Text text = drawText(valueString + " + " + chanceString + " * "
				+ evalString, node.getPosition().getX() - nodeXOffset, node
				.getPosition().getY() - 30, animationTextProperties)[0];
		int textWidth = getTextWidth(text);
		text.moveBy("translate", -textWidth, 0, null, null);
		lang.nextStep();
		return text;
	}

	private void drawTree(ExpectimaxNode root, int leafSpace) {
		nextLeafPos = 50;
		drawNode(root, leafSpace, 1);

	}

	private int drawNode(ExpectimaxNode node, int leaf_space, int depth) {
		// if leaf, draw at nextLeafPos and increase
		if (node.getType() == NodeType.LEAF) {
			node.setPosition(nextLeafPos, depth * layerHeight);
			node.setPrimitive(drawLeaf(node));
			nextLeafPos += leaf_space;
			return nextLeafPos - leaf_space;
		}
		// calculate child positions
		int pos1 = 0;
		int pos2 = 0;
		int[] childPos = new int[node.childCount()];
		for (int i = 0; i < node.childCount(); i++) {
			childPos[i] = drawNode(node.getChild(i), leaf_space, depth + 1);
			if (i == 0) {
				pos1 = childPos[i];
				pos2 = pos1;
			} else {
				pos2 = childPos[i];
			}
		}
		// own position in middle of children
		int pos = (pos1 + pos2) / 2;
		// calculate offset
		int yOffset = nodeYOffset / 2;
		// draw lines
		PolylineProperties lineProperties = new PolylineProperties();
		lineProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		for (int i = 0; i < childPos.length; i++) {
			node.addLine(drawLine(pos, depth * layerHeight + yOffset,
					childPos[i], (depth + 1) * layerHeight, lineProperties));
		}
		node.setPosition(pos, depth * layerHeight + yOffset);
		// draw node (after line to override it)
		if (node.getType() == NodeType.MAX) {
			node.setPrimitive(drawMax(pos, depth));
		} else if (node.getType() == NodeType.MIN) {
			node.setPrimitive(drawMin(pos, depth));
		} else if (node.getType() == NodeType.CHANCE) {
			node.setPrimitive(drawChance(node, childPos, pos, depth));
		}
		// return own position
		return pos;
	}

	private Primitive drawLeaf(ExpectimaxNode node) {
		CircleProperties circleProperties = new CircleProperties();
		circleProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		circleProperties
				.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		int x = node.getPosition().getX();
		int y = node.getPosition().getY() + 10;

		Circle circle = lang.newCircle(new Coordinates(x, y), nodeXOffset,
				"circle-" + Integer.toString(x) + "-" + Integer.toString(y),
				null, circleProperties);

		TextProperties properties = new TextProperties();
		properties.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
		node.setValueText(drawText(
				new java.text.DecimalFormat("#").format(node.getValue()), x,
				y - 8, properties)[0]);

		return circle;
	}

	private Circle drawChance(ExpectimaxNode node, int[] childPos, int pos,
			int depth) {
		CircleProperties circleProperties = new CircleProperties();
		circleProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		circleProperties
				.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);

		int x = pos; // x1
		int y = depth * layerHeight + nodeXOffset; // y1

		double childHeight = layerHeight * (depth + 1); // y2
		double chanceY = childHeight - (layerHeight - nodeXOffset) / 2; // y
		double chanceX = 0; // x
		double xOffset = 0;
		TextProperties properties = new TextProperties();
		properties.set(AnimationPropertiesKeys.CENTERED_PROPERTY, false);
		for (int i = 0; i < node.childCount(); i++) {
			chanceX = ((chanceY - y) * (childPos[i] - x)) / (childHeight - y)
					+ x;

			Text text = drawText(
					new java.text.DecimalFormat("#.##").format(node
							.getChance(i)), (int) Math.round(chanceX)
							- CHANCE_MARGIN, (int) Math.round(chanceY),
					properties)[0];
			text.moveBy("translate", -getTextWidth(text), 0, null, null);

			// compensate for diagonal lines
			xOffset = ((chanceY + getTextHeight(text) - y) * (childPos[i] - x))
					/ (childHeight - y) + x - CHANCE_MARGIN;
			if (xOffset < chanceX) {
				text.moveBy("translate", (int) Math.round(xOffset - chanceX),
						0, null, null);
			}
		}
		return lang.newCircle(new Coordinates(x, y), nodeXOffset, "circle-"
				+ Integer.toString(pos), null, circleProperties);
	}

	private int getTextWidth(Text text) {
		Font font = (Font) text.getProperties().get(
				AnimationPropertiesKeys.FONT_PROPERTY);
		AffineTransform affinetransform = new AffineTransform();
		FontRenderContext frc = new FontRenderContext(affinetransform, true,
				true);
		return (int) (font.getStringBounds(text.getText(), frc).getWidth());
	}

	private int getTextHeight(Text text) {
		Font font = (Font) text.getProperties().get(
				AnimationPropertiesKeys.FONT_PROPERTY);
		AffineTransform affinetransform = new AffineTransform();
		FontRenderContext frc = new FontRenderContext(affinetransform, true,
				true);
		return (int) (font.getStringBounds(text.getText(), frc).getHeight());
	}

	private Triangle drawMin(int pos, int depth) {
		TriangleProperties triangleProperties = new TriangleProperties();
		triangleProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		triangleProperties.set(AnimationPropertiesKeys.FILL_PROPERTY,
				Color.WHITE);

		int x = pos;
		int y = depth * layerHeight;
		return lang.newTriangle(new Coordinates(x - nodeXOffset, y),
				new Coordinates(x + nodeXOffset, y), new Coordinates(x, y
						+ nodeYOffset), "triangle-" + Integer.toString(pos)
						+ "-" + Integer.toString(depth), null,
				triangleProperties);
	}

	private Triangle drawMax(int pos, int depth) {
		TriangleProperties triangleProperties = new TriangleProperties();
		triangleProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		triangleProperties.set(AnimationPropertiesKeys.FILL_PROPERTY,
				Color.WHITE);

		int x = pos;
		int y = depth * layerHeight;
		return lang.newTriangle(
				new Coordinates(x, y),
				new Coordinates(x - nodeXOffset, y + nodeYOffset),
				new Coordinates(x + nodeXOffset, y + nodeYOffset),
				"triangle-" + Integer.toString(pos) + "-"
						+ Integer.toString(depth), null, triangleProperties);
	}

	private Polyline drawLine(int x1, int y1, int x2, int y2) {
		PolylineProperties properties = new PolylineProperties();
		return drawLine(x1, y1, x2, y2, properties);
	}

	private Polyline drawLine(int x1, int y1, int x2, int y2,
			PolylineProperties properties) {
		Coordinates[] nodes = { new Coordinates(x1, y1),
				new Coordinates(x2, y2) };
		String name = "line-" + Integer.toString(x1) + ","
				+ Integer.toString(y1) + "-" + Integer.toString(x2) + ","
				+ Integer.toString(y2);
		return lang.newPolyline(nodes, name, null, properties);
	}

	private Text[] drawText(String string, int x, int y) {
		// set standard text properties
		TextProperties properties = new TextProperties();
		return drawText(string, x, y, properties);
	}

	private Text[] drawText(String string, int x, int y,
			TextProperties properties) {
		Node position = new Coordinates(x, y);
		return drawText(string, position, properties);
	}

	private int countLeaves(ExpectimaxNode node) {
		if (node.getType() == NodeType.LEAF) {
			return 1;
		}
		int sum = 0;
		for (int i = 0; i < node.childCount(); i++) {
			sum += countLeaves(node.getChild(i));
		}
		return sum;
	}

	private Text[] drawText(String string, Node position,
			TextProperties properties) {
		String[] lines = string.split("\n");
		Text[] texts = new Text[lines.length];
		for (int i = 0; i < lines.length; ++i) {
			texts[i] = lang.newText(position, lines[i],
					"text-" + Integer.toString(i), null, properties);
			position = new Offset(0, lineOffset, texts[i], "SW");
		}
		return texts;
	}

	private void showHeader() {
		if (headerText != null && headerRect != null) {
			headerText.show();
			headerRect.show();
			return;
		}
		// Header
		headerTextProperties.set(AnimationPropertiesKeys.FONT_PROPERTY,
				new Font(Font.SANS_SERIF, Font.BOLD, 24));
		headerText = lang.newText(new Coordinates(20, 30), "Expectiminimax",
				"header", null, headerTextProperties);
		headerRectangleProperties
				.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		headerRect = lang.newRect(new Offset(-5, -5, "header",
				AnimalScript.DIRECTION_NW), new Offset(5, 5, "header",
				AnimalScript.DIRECTION_SE), "hRect", null,
				headerRectangleProperties);
	}

	final String stringCodeEval = "function expectimax(node)\n"
			+ "\tswitch (node.type)\n" + "\t\tcase (max): return max(node)\n"
			+ "\t\tcase (min): return min(node)\n"
			+ "\t\tcase (chance): return chance(node)\n"
			+ "\t\tcase (leaf): return node.value";

	final String stringCodeMax = "function max(node)\n" + "\tvalue = -∞\n"
			+ "\tfor i = 0 to node.children.length\n"
			+ "\t\tchildValue = expectimax(node.children[i])\n"
			+ "\t\tif childValue > node.value\n" + "\t\t\tvalue = childValue\n"
			+ "\treturn value";

	final String stringCodeMin = "function min(node)\n" + "\tvalue = ∞\n"
			+ "\tfor i = 0 to node.children.length\n"
			+ "\t\tchildValue = expectimax(node.children[i])\n"
			+ "\t\tif childValue < node.value\n" + "\t\t\tvalue = childValue\n"
			+ "\treturn value";

	final String stringCodeChance = "function chance(node)\n" + "\tvalue = 0\n"
			+ "\tfor i = 0 to node.children.length\n"
			+ "\t\tchild = node.children[i]\n"
			+ "\t\tvalue = value + child.chance * expectimax(child)\n"
			+ "\treturn value";

	private void drawSourceCode(int position) {
		codeEval = lang.newSourceCode(new Coordinates(position, 10), "code",
				null, sourceCodeProperties);
		codeEval.addMultilineCode(stringCodeEval, "code", null);
		codeMax = lang.newSourceCode(new Coordinates(position, 132), "code",
				null, sourceCodeProperties);
		codeMax.addMultilineCode(stringCodeMax, "code", null);
		codeMin = lang.newSourceCode(new Coordinates(position, 270), "code",
				null, sourceCodeProperties);
		codeMin.addMultilineCode(stringCodeMin, "code", null);
		codeChance = lang.newSourceCode(new Coordinates(position, 408), "code",
				null, sourceCodeProperties);
		codeChance.addMultilineCode(stringCodeChance, "code", null);

	}

	final String DESCRIPTION_1 = "Expectiminimax (kurz Expectimax) entstammt der Spieletheorie und ist eine Erweiterung des Minimax um \n"
			+ "Erwartungswerte. Minimax dient zur Ermittlung der optimalen Spielstrategie für Nullsummenspiele mit \n"
			+ "perfekter Information. Zwei gegnerische Spieler führen (in der Regel, aber nicht ausschließlich) \n"
			+ "abwechselnd Züge aus. Der MAX-Spieler versucht den Wert des aktuellen Knotens zu maximieren, der MIN-Spieler \n"
			+ "hingegen versucht den Wert zu minimieren.\n"
			+ "\n"
			+ "Der Expectiminimax-Algorithmus erweitert dieses Schema um Zufallsknoten, womit auch Spiele mit \n"
			+ "Zufallseinfluss modelliert werden können. Als Grundlage dazu dient der Erwartungswert der möglichen \n"
			+ "Ergebnisse.\n"
			+ "\n"
			+ "http://de.wikipedia.org/wiki/Minimax-Algorithmus"
			+ "\n"
			+ "http://de.wikipedia.org/wiki/Expectiminimax-Algorithmus";

	final String DESCRIPTION_2 = "Expectiminimax wird auf einen Suchbaum angewendet, in dem jede Kante einen möglichen Zug repräsentiert.\n"
			+ "Die Knoten geben an, welcher Spieler am Zug ist. Ein Pfad von Wurzel bis Blatt gibt einen möglichen\n"
			+ "Spielverlauf an, dessen Ergebnisbewertung dem Wert des Blattknotens entspricht.\n"
			+ "\n"
			+ "Beide Spieler entscheiden ihre Züge nach einer festen Regel. Ein nach oben gerichtetes Dreieck (▲) steht für den \n"
			+ "den MAX-Spieler, der versucht, den Wert des Knotens zu maximieren. Ein nach unten gerichtetes Dreieck (▼)\n"
			+ "steht für den MIN-Spieler, der den Wert entsprechend minimiert. Zufallsentscheidungen werden durch einen\n"
			+ "Kreis repräsentiert. Die Wahrscheinlichkeiten der Ergebnisse werden an den entsprechenden Kanten\n"
			+ "dargestellt.\n"
			+ "\n"
			+ "Nach Durchlauf des Algorithmus enthält der Wurzelknoten das Ergebnis des Spiels bei perfekter Spielweise\n"
			+ "beider Spieler.";

	final String DESCRIPTION_3 = "Folgende Bedingungen müssen erfüllt sein, damit Expectimax für ein Spiel genutzt werden kann:\n"
			+ "- Das Spiel wird von zwei gegnerischen Spielern gespielt.\n"
			+ "- Es handelt sich um ein Nullsummenspiel. Das heißt, die Gewinne und Verluste beider Spieler summieren sich\n"
			+ "auf Null. Das bedeutet, der Gewinn eines Spielers muss gleichzeitig der Verlust des anderen sein.\n"
			+ "- Es wird mit perfekter Information gespielt. Ein Gegenbeispiel sind Kartenspiele, bei denen man die Karten\n"
			+ "seines Gegners nicht kennt.\n"
			+ "\n"
			+ "Neben Spielen wie Dame, Mühle oder Schach, für die der Minimax-Algorithmus ausreichend ist, kann\n"
			+ "Expectiminimax auch für Spiele mit Zufallsereignissen wie Backgammon verwendet werden.";

	private void showDescription() {
		Node position = new Offset(-5, 50, "hRect", "SW");
		Text[] description1 = drawText(DESCRIPTION_1, position,
				descriptionTextProperties);
		lang.nextStep("Beschreibung Teil 1");
		for (Text t : description1) {
			t.hide();
		}
		Text[] description2 = drawText(DESCRIPTION_2, position,
				descriptionTextProperties);
		lang.nextStep("Beschreibung Teil 2");
		for (Text t : description2) {
			t.hide();
		}
		Text[] description3 = drawText(DESCRIPTION_3, position,
				descriptionTextProperties);
		lang.nextStep("Beschreibung Teil 3");
		for (Text t : description3) {
			t.hide();
		}

	}

	private void showSummary() {
		NumberFormat f = NumberFormat.getInstance(Locale.GERMAN);
		
		final String SUMMARY = "Zusammenfassung:\n"
				+ "\n"
				+ "Es wurden "
				+ nodeCount
				+ " Nodes durchsucht.\n"
				+ branches()
				+ "\n"
				+ depth()
				+ "\n"
				+ "Damit gilt für die Ausführungszeit des Algorithmus O(b^m) = "
				+ f.format(Math.pow(bMax, mMax)) + "\n"
				+ "und für den Platzbedarf O(bm) = " + f.format((bMax * mMax)) + "\n"
				+ "\n" + "Der beste Zug hat den Wert: " + f.format(maxChoice);
		drawText(SUMMARY, 10, 100, descriptionTextProperties);
	}

	private String branches() {
		if (bMax == bMin)
			return "Der branching Faktor ist b = " + bMin;
		else
			return "Für den branching Faktor b gilt: " + bMin + " <= b <= "
					+ bMax;
	}

	private String depth() {
		if (mMax == mMin)
			return "Die Baumtiefe ist m = " + mMin;
		else
			return "Für die Baumtiefe m gilt: " + mMin + " <= m <= " + mMax;
	}
}