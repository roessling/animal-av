package generators.searching.minmax;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.searching.helpers.Node;
import interactionsupport.models.MultipleChoiceQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.swing.JOptionPane;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Graph;
import algoanim.primitives.Polyline;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.Triangle;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.TriangleProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

/**
 * @author Carina Oberle <carina.oberle@stud.tu-darmstadt.de>
 * @version 2.1 2014-05-16
 */
public class MinMaxGenerator implements ValidatingGenerator {
	/**
	 * concrete language object used for creating output
	 */
	private Language lang;

	/**
	 * user defined graph structure
	 */
	private String graphStructure;

	/**
	 * user defined text properties
	 */
	private TextProperties textProps;

	/**
	 * user defined source code properties
	 */
	private SourceCodeProperties srcProps;

	/**
	 * user defined boolean, indicating whether the final question shall be
	 * asked or not
	 */
	private boolean askQuestion;

	/**
	 * the header text
	 */
	private Text header;

	/**
	 * header propteries
	 */
	private TextProperties headerProps;

	/**
	 * the title of the animation
	 */
	private String title;

	/**
	 * line separating the headline from the other contents
	 */
	private Polyline hLine;

	/**
	 * the source code shown in the animation
	 */
	private SourceCode src;

	/**
	 * the game tree graph
	 */
	private Graph graph;
    
    /**
     * user defined graph properties
     */
	private GraphProperties graphProps;

	/**
	 * triangle used for marking the current code line
	 */
	private Triangle codeMarker;

	/**
	 * duration of moves in ticks
	 */
	private TicksTiming moveDuration;

	/**
	 * first line of animation step description
	 */
	private Text descr0;

	/**
	 * second line of animation step description
	 */
	private Text descr1;

	/**
	 * labels containing the best values for each node
	 */
	private Text[] bestValues;

	/**
	 * strings containing the best values for each node
	 * 
	 * (for some reason, the values as strings can't be obtained by calling
	 * getText() on the corresponding Text object)
	 */
	private String[] bestValStrings;

	/**
	 * text properties of the node value labels
	 */
	private TextProperties valProps;

	/**
	 * properties of the rectangle surrounding the animation description
	 */
	private RectProperties rectProps;

	/**
	 * left border of the game tree
	 */
	private int leftBorder;

	/**
	 * upper border of the game tree
	 */
	private int upperBorder;

	/**
	 * distance on x-axis between the leaf nodes
	 */
	private int xLeafDist;

	/**
	 * distance on y-axis between the different node depth levels
	 */
	private int yNodeDist;

	/**
	 * array containing the nodes (coordinates) of the graph
	 */
	private algoanim.util.Coordinates[] graphNodes;

	/**
	 * the (maximum) depth of the tree
	 */
	private int treeDepth;

	/**
	 * x offset from the best value labels to the node centers
	 */
	private int xBestValOffset;

	/**
	 * y offset from the best value labels to the node centers
	 */
	private int yBestValOffset;

	/**
	 * array containing the source code indentations for each code line
	 */
	private int[] indent;

	/**
	 * Default constructor
	 * 
	 * @param l
	 *            the concrete language object used for creating output
	 */
	public MinMaxGenerator(Language l) {
		lang = l;
		lang.setStepMode(true);
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
	}

	public MinMaxGenerator() {
		lang = new AnimalScript("MinMax [EN]", "Carina Oberle", 800, 600);
		lang.setStepMode(true);
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
	}

	public void init() {
		lang = new AnimalScript("MinMax [EN]", "Carina Oberle", 800, 600);
		lang.setStepMode(true);
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
		title = "MinMax Algorithm";
		upperBorder = 90;
		leftBorder = 66;
		moveDuration = new TicksTiming(10);
		xLeafDist = 38;
		yNodeDist = 60;
		xBestValOffset = 0;
		yBestValOffset = 25;
		treeDepth = 0;
		indent = new int[] { 0, 1, 2, 1, 2, 2, 3, 3, 2, 1, 2, 2, 3, 3, 2 };
	}

	/**
	 * Initializes the animation. Shows a start page with a description. Then
	 * shows the graph and the source code and calls the MinMax algorithm.
	 */
	public void start() {

		// -----------------------------------------------
		// show the header underlined by a separation line
		// -----------------------------------------------
		Font font = (Font) headerProps
				.get(AnimationPropertiesKeys.FONT_PROPERTY);
		font = font.deriveFont(Font.BOLD, 24);
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, font);
		header = lang.newText(new Coordinates(20, 30), title, "header", null,
				headerProps);
		PolylineProperties lineProps = new PolylineProperties();
		lineProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				headerProps.get(AnimationPropertiesKeys.COLOR_PROPERTY));

		algoanim.util.Node[] nodes = {
				new Offset(0, 10, "header", AnimalScript.DIRECTION_SW),
				new Offset(400, 10, "header", AnimalScript.DIRECTION_SE) };
		hLine = lang.newPolyline(nodes, "header", null, lineProps);

		// -----------------------------------------------
		// setup the start page with the description
		// -----------------------------------------------
		lang.nextStep();

		Font f = (Font) textProps.get(AnimationPropertiesKeys.FONT_PROPERTY);
		f = f.deriveFont(Font.PLAIN, 16);
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, f);

		lang.newText(
				new Offset(0, 20, "header", AnimalScript.DIRECTION_SW),
				"MinMax (also known as Minimax) is an adversarial search algorithm.",
				"intro[0]", null, textProps);
		lang.newText(
				new Offset(0, 3, "intro[0]", AnimalScript.DIRECTION_SW),
				"In a two-player game visualized as a tree, it calculates the optimal strategy",
				"intro[1]", null, textProps);
		lang.newText(
				new Offset(0, 3, "intro[1]", AnimalScript.DIRECTION_SW),
				"for the player who is starting the game, given perfect play of his opponent.",
				"intro[2]", null, textProps);
		lang.newText(
				new Offset(0, 3, "intro[2]", AnimalScript.DIRECTION_SW),
				"The algorithm only works for deterministic, perfect-information games,",
				"intro[3]", null, textProps);
		lang.newText(
				new Offset(0, 3, "intro[3]", AnimalScript.DIRECTION_SW),
				"like for example Go, Tic-Tac-Toe, Othello, Checkers or Chess.",
				"intro[4]", null, textProps);

		// -----------------------------------------------
		// show Game Setup header
		// -----------------------------------------------
		lang.nextStep("Intro");
		font = font.deriveFont(Font.BOLD, 20);
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, font);
		lang.newText(new Offset(0, 20, "intro[4]", AnimalScript.DIRECTION_SW),
				"Game Setup", "gameSetupHeader", null, headerProps);
		// -----------------------------------------------
		// display first part of game setup
		// -----------------------------------------------
		lang.nextStep();
		lang.newText(new Offset(0, 10, "gameSetupHeader",
				AnimalScript.DIRECTION_SW), "- Two players: MAX and MIN",
				"gameSetup[0]", null, textProps);
		// -----------------------------------------------
		// display second part of game setup
		// -----------------------------------------------
		lang.nextStep();
		lang.newText(
				new Offset(0, 3, "gameSetup[0]", AnimalScript.DIRECTION_SW),
				"- MAX moves first and tries to maximize the evaluation of the current position",
				"gameSetup[1]", null, textProps);
		// -----------------------------------------------
		// display third part of game setup
		// -----------------------------------------------
		lang.nextStep();
		lang.newText(
				new Offset(0, 3, "gameSetup[1]", AnimalScript.DIRECTION_SW),
				"- Analogously, MIN tries to minimize the evaluation of the current position",
				"gameSetup[2]", null, textProps);
		// -----------------------------------------------
		// display fourth part of game setup
		// -----------------------------------------------
		lang.nextStep();
		lang.newText(
				new Offset(0, 3, "gameSetup[2]", AnimalScript.DIRECTION_SW),
				"- MIN and MAX take turns until the game is over",
				"gameSetup[3]", null, textProps);
		// -----------------------------------------------
		// hide the description
		// -----------------------------------------------
		lang.nextStep("Game Setup");
		lang.hideAllPrimitives();
		header.show();
		hLine.show();
		// -----------------------------------------------
		// display graph
		// -----------------------------------------------
		lang.nextStep();
		graph.show();
		// -----------------------------------------------
		// display MIN/MAX labels
		// -----------------------------------------------
		lang.nextStep("Animation Start");
		TextProperties labelProps = new TextProperties();
		labelProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.PLAIN, 12));

		lang.newText(new Coordinates(22, 85), "MAX", "labels[0]", null,
				labelProps);

		boolean max = false;
		String l;

		System.out.println(treeDepth);

		for (int i = 1; i < treeDepth; i++) {
			if (max)
				l = "MAX";
			else
				l = "MIN";

			lang.newText(new Offset(0, 60, "labels[" + (i - 1) + "]",
					AnimalScript.DIRECTION_NW), l, "labels[" + i + "]", null,
					labelProps);
			max = !max;
		}

		// -----------------------------------------------
		// display pseudo code
		// -----------------------------------------------
		lang.nextStep();
		int cl = 0;
		src = lang.newSourceCode(new Offset(15, -25, "graph",
				AnimalScript.DIRECTION_NE), "src", null, srcProps);
		// -----------------------------------------------
		// add code lines to the SourceCode object
		// Line, name, indentation, display delay
		// -----------------------------------------------
		src.addCodeLine("function minmax(node, maximizingPlayer)", null,
				indent[cl++], null); // 0
		src.addCodeLine("if node is a terminal node", null, indent[cl++], null); // 1
		src.addCodeLine("return the value of node", null, indent[cl++], null); // 2
		src.addCodeLine("if maximizingPlayer", null, indent[cl++], null); // 3
		src.addCodeLine("bestValue := -\u221E", null, indent[cl++], null); // 4
		src.addCodeLine("for each child of node", null, indent[cl++], null); // 5
		src.addCodeLine("val := minmax(child, FALSE)", null, indent[cl++], null); // 6
		src.addCodeLine("bestValue := max(bestValue, val)", null, indent[cl++],
				null); // 7
		src.addCodeLine("return bestValue", null, indent[cl++], null); // 8
		src.addCodeLine("else", null, indent[cl++], null); // 9
		src.addCodeLine("bestValue := +\u221E", null, indent[cl++], null); // 10
		src.addCodeLine("for each child of node", null, indent[cl++], null); // 11
		src.addCodeLine("val := minmax(child, TRUE)", null, indent[cl++], null); // 12
		src.addCodeLine("bestValue := min(bestValue, val)", null, indent[cl++],
				null); // 13
		src.addCodeLine("return bestValue", null, indent[cl++], null); // 14

		TriangleProperties markerProps = new TriangleProperties();
		markerProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		markerProps.set(AnimationPropertiesKeys.FILL_PROPERTY,
				srcProps.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY));
		// -----------------------------------------------
		// set code marker to initial position
		// -----------------------------------------------
		codeMarker = lang.newTriangle(new Offset(-14, 2, "src",
				AnimalScript.DIRECTION_NW), new Offset(-14, 12, "src",
				AnimalScript.DIRECTION_NW), new Offset(-4, 7, "src",
				AnimalScript.DIRECTION_NW), "codeMarker", null, markerProps);

		lang.nextStep();

		algoanim.util.Node root = graph.getNodeForIndex(0);
		String rootLabel = graph.getNodeLabel(root);
		// -----------------------------------------------
		System.out.println("Root: " + rootLabel);
		// -----------------------------------------------
		LinkedList<algoanim.util.Node> startAlts = new LinkedList<algoanim.util.Node>(
				Arrays.asList(children(root)));
		// number of alternatives at the beginning
		int altCount = startAlts.size();
		// String containing the alternatives at the beginning
		String alts = getAltString(startAlts);

		if (treeDepth > 3)
			descr0 = lang.newText(new Offset(0, 30, "graph",
					AnimalScript.DIRECTION_SW), "MAX starts in position "
					+ rootLabel + " and has to choose between " + altCount
					+ " alternatives: " + alts + ".", "descr[0]", null,
					textProps);
		else
			descr0 = lang.newText(new Coordinates(66, 340),
					"MAX starts in position " + rootLabel
							+ " and has to choose between " + altCount
							+ " alternatives: " + alts + ".", "descr[0]", null,
					textProps);

		descr1 = lang.newText(new Offset(0, 3, "descr[0]",
				AnimalScript.DIRECTION_SW),
				"But which one will maximize his result?", "descr[1]", null,
				textProps);

		rectProps = new RectProperties();
		rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				srcProps.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY));
		rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
        
		// -----------------------------------------------
		// show description box
		// -----------------------------------------------
		int dx;

		if (altCount < 3)
			dx = 150;
		else
			dx = 80;

		lang.newRect(new Offset(-5, -5, "descr[0]",
				AnimalScript.DIRECTION_NW), new Offset(dx, 30, "descr[0]",
				AnimalScript.DIRECTION_SE), "rect", null, rectProps);

		int graphSize = graph.getSize();
		bestValues = new Text[graphSize];
		bestValStrings = new String[graphSize];
		valProps = new TextProperties();
		valProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);

		// valProps.set(AnimationPropertiesKeys.BOLD_PROPERTY, true);
		// -> java.lang.IllegalArgumentException: The given key 'bold' was not
		// found!

		String name;
		Coordinates coords;

		for (int i = 0; i < bestValues.length; i++) {
			name = "val" + i;
			coords = graphNodes[i];
			int x = coords.getX() + xBestValOffset;
			int y = coords.getY() + yBestValOffset;
			bestValues[i] = lang.newText(new Coordinates(x, y), "", name, null,
					valProps);
		}
		// -----------------------------------------------
		// call the MinMax algorithm
		// -----------------------------------------------
		lang.nextStep();
		descr0.hide();
		descr1.hide();
		algoanim.util.Node startNode = graph.getNode(0);
		int result = minmax(startNode, true);

		codeMarker.hide();

		descr0.setText("The algorithm terminated returning value '" + result
				+ "'.", null, null);
		descr0.show();
		descr1.setText("This means MAX' score will be " + result
				+ ", if both he and MIN play optimally.", null, null);
		descr1.show();

		lang.nextStep();

		// -----------------------------------------------
		// show final multiple choice question
		// -----------------------------------------------
		if (askQuestion) {
			MultipleChoiceQuestionModel mc = new MultipleChoiceQuestionModel(
					"bestChoice");
			String question = "Back to our initial question: Which node is the best choice for MAX in start position "
					+ rootLabel + " ?";
			mc.setPrompt(question);

			startAlts = new LinkedList<algoanim.util.Node>(
					Arrays.asList(children(root)));
			altCount = startAlts.size();
			int[] score = new int[altCount];
			int maxScore = 0;
			int v;
			String res;

			for (int i = 0; i < altCount; i++) {
				int currentScore = Integer.parseInt(bestValStrings[graph
						.getPositionForNode(startAlts.get(i))]);
				score[i] = currentScore;
				if (currentScore > maxScore) {
					maxScore = currentScore;
				}
			}

			for (int i = 0; i < altCount; i++) {
				if (score[i] == maxScore) {
					v = 1;
					res = "Correct!";
				}

				else {
					v = -1;
					res = "Wrong.";
				}

				mc.addAnswer(graph.getNodeLabel(startAlts.get(i)), v, res);
			}
			lang.addMCQuestion(mc);

			lang.nextStep();
		}

		// -----------------------------------------------
		// show principal variation
		// -----------------------------------------------
		descr0.hide();
		descr1.hide();
		descr0.setText(
				"Now that we evaluated all of the nodes, it is quite easy to obtain",
				null, null);
		descr0.show();
		descr1.setText(
				"the 'principal variation' (optimal sequence of moves).", null,
				null);
		descr1.show();

		showPrincipalVariation(startNode, true);

		lang.nextStep("Principal Variation");

		// -----------------------------------------------
		// display summary
		// -----------------------------------------------
		descr0.hide();
		descr1.hide();
		descr0.setText(
				"The shown example 'game' involved only few sequential decisions,",
				null, null);
		descr0.show();
		descr1.setText(
				"yet it took quite a long time to evaluate all of the nodes in the game tree.",
				null, null);
		descr1.show();

		lang.nextStep("Summary");
		descr0.hide();
		descr1.hide();
		descr0.setText(
				"As you might remember from the introduction to this animation, the MinMax algorithm",
				null, null);
		descr0.show();
		descr1.setText(
				"theoretically works for games like chess, that potentially involve hundreds of decisions.",
				null, null);
		descr1.show();

		lang.nextStep();
		descr0.hide();
		descr1.hide();
		descr0.setText(
				"Due to exponential growth, the game tree for chess would be enormous.",
				null, null);
		descr0.show();

		lang.nextStep();
		descr0.hide();
		descr0.setText(
				"For that reason, MinMax usually only descends to a certain depth",
				null, null);
		descr0.show();
		descr1.setText("and then uses heuristics to evaluate the nodes.", null,
				null);
		descr1.show();

		lang.nextStep();
		descr0.hide();
		descr0.setText(
				"Additionally, the algorithm can be optimized by 'pruning' branches",
				null, null);
		descr0.show();
		descr1.setText(
				"that have no effect on the result and therefore don't need to be examined.",
				null, null);
		descr1.show();

		lang.nextStep();
		descr0.hide();
		descr0.setText(
				"An optimization that uses pruning is the 'Alpha-Beta Pruning' algorithm.",
				null, null);
		descr0.show();
		descr1.setText("See http://en.wikipedia.org/wiki/Alpha–beta_pruning",
				null, null);
		descr1.show();

	}

	/**
	 * Determines whether the given node is a terminal node (leaf)
	 * 
	 * @param node
	 *            the node to be examined
	 * @return true if the given node is a terminal node, else false
	 */
	private boolean isLeaf(algoanim.util.Node node) {
		int[] edges = graph.getEdgesForNode(node);
		int pos = graph.getPositionForNode(node);

		for (int i = pos; i < edges.length; i++) {
			if (edges[i] > 0)
				return false;
		}
		return true;
	}

	/**
	 * Returns all child nodes of a given node.
	 * 
	 * @param node
	 *            the current node to be examined
	 * @return array containing all child nodes of the given node
	 */
	private algoanim.util.Node[] children(algoanim.util.Node node) {
		List<algoanim.util.Node> children = new LinkedList<algoanim.util.Node>();

		int[] edges = graph.getEdgesForNode(node);
		int pos = graph.getPositionForNode(node);

		for (int i = pos; i < edges.length; i++) {
			if (edges[i] > 0)
				children.add(graph.getNode(i));
		}

		return children.toArray(new algoanim.util.Node[children.size()]);
	}

	/**
	 * Returns a String containing the nodes in the given list in the following
	 * format: "N1, N2, ... and Nx"
	 * 
	 * @param startAlts
	 *            the nodes (alternatives at start)
	 * @return String containing the nodes in the given list in the following
	 *         format: "N1, N2, ... and Nx"
	 */
	private String getAltString(LinkedList<algoanim.util.Node> startAlts) {
		if (startAlts.isEmpty())
			return "";

		algoanim.util.Node nextNode = startAlts.removeFirst();
		String result = graph.getNodeLabel(nextNode);

		if (startAlts.isEmpty()) {
			return result;
		}

		if (startAlts.size() == 1) {
			return result.concat(" and ").concat(getAltString(startAlts));
		}

		result = result.concat(", ").concat(getAltString(startAlts));

		return result;
	}

	/**
	 * highlights a code line and sets the code marker
	 * 
	 * @param line
	 *            the code line to be highlighted
	 */
	private void highlightCodeLine(int line) {
		moveMarker(line);
		src.highlight(line);
	}

	/**
	 * moves the code line marker to the given line
	 * 
	 * @param line
	 *            the currently highlighted code line
	 */
	private void moveMarker(int line) {

		if (line < indent.length) {
			Offset off = new Offset(-14 + indent[line] * 12, 2 + line * 16,
					"src", AnimalScript.DIRECTION_NW);

			codeMarker.moveTo(null, "translate", off, null, moveDuration);
		}
	}

	/**
	 * Evaluates the given node for the current player.
	 * 
	 * @param node
	 *            the current node to be evaluated
	 * @param maximizingPlayer
	 *            indicates whether the current player is the maximizing player
	 *            or not
	 * @return best achievable value for the current player in the current node
	 */
	private int minmax(algoanim.util.Node node, boolean maximizingPlayer) {

		String currentLabel = graph.getNodeLabel(node);
		int currentPos = graph.getPositionForNode(node);

		// highlight first line of code
		highlightCodeLine(0);
		// highlight current node
		graph.highlightNode(node, null, null);

		descr0.setText("Call minmax(Node '" + currentLabel + "', "
				+ maximizingPlayer + ")", null, null);
		descr0.show();

		// -----------------------------------------------
		// check if current node is terminal node
		// -----------------------------------------------
		lang.nextStep();
		descr0.hide();
		src.unhighlight(0);
		highlightCodeLine(1);

		// display message "is this a terminal node?"
		descr0.setText("Is this a terminal node?", null, null);
		descr0.show();

		lang.nextStep();

		if (isLeaf(node)) {
			descr1.setText("Yes! It has no child nodes.", null, null);
			descr1.show();

			lang.nextStep();
			descr0.hide();
			descr1.hide();
			src.unhighlight(1);

			// -----------------------------------------------
			// return value of leaf node
			// -----------------------------------------------
			highlightCodeLine(2);
			descr0.setText("Return value '" + currentLabel + "'.", null, null);
			descr0.show();
			bestValues[currentPos].hide();
			bestValues[currentPos].setText(currentLabel, null, null);
			bestValues[currentPos].show();
			bestValStrings[currentPos] = currentLabel;

			lang.nextStep();
			descr0.hide();
			src.unhighlight(2);
			graph.unhighlightNode(node, null, null);
			return Integer.parseInt(graph.getNodeLabel(node));
		}

		else {
			// display a message
			// "the node is no leaf node, it has X children" (or 1 child)
			algoanim.util.Node[] children = children(node);
			int num = children.length;

			if (num == 1)
				descr1.setText("No! It has 1 child node.", null, null);
			else
				descr1.setText("No! It has " + num + " child nodes.", null,
						null);

			descr1.show();
		}

		lang.nextStep();
		descr0.hide();
		descr1.hide();
		src.unhighlight(1);

		// -----------------------------------------------
		// check if current player is MAX
		// -----------------------------------------------
		highlightCodeLine(3);

		// display message "is it MAX' turn?"
		descr0.setText("Is it MAX' turn?", null, null);
		descr0.show();

		lang.nextStep();
		int bestValue;

		if (maximizingPlayer) {
			// display message notifying about max player's turn
			descr1.setText("Yes!", null, null);
			descr1.show();

			lang.nextStep();
			descr0.hide();
			descr1.hide();
			src.unhighlight(3);

			// -----------------------------------------------
			// bestValue := -infinity (modeled as smallest possible Integer)
			// -----------------------------------------------
			highlightCodeLine(4);

			descr0.setText(
					"Currently all MAX knows is that his score for this node will be -\u221E or possibly higher.",
					null, null);
			descr0.show();
			bestValues[currentPos].hide();
			bestValues[currentPos].setText("-\u221E", null, null);
			bestValues[currentPos].show();

			bestValue = Integer.MIN_VALUE;
			bestValStrings[currentPos] = String.valueOf(bestValue);

			// -----------------------------------------------
			// for each child of node
			// -----------------------------------------------
			lang.nextStep();
			descr0.hide();
			src.unhighlight(4);
			highlightCodeLine(5);
			// display message
			// "evaluate all child nodes to find out which one is the best choice"
			descr0.setText(
					"Evaluate all child nodes to find out which one is the best choice.",
					null, null);
			descr0.show();

			descr1.setText(
					"In MAX' case this means finding the child node with the highest value.",
					null, null);
			descr1.show();

			int val;

			lang.nextStep();
			descr0.hide();
			descr1.hide();
			src.unhighlight(5);

			for (algoanim.util.Node c : children(node)) {

				// -----------------------------------------------
				// val := minmax(child, FALSE)
				// -----------------------------------------------
				highlightCodeLine(6);
				String label = graph.getNodeLabel(c);

				descr0.setText("Evaluate child with label '" + label
						+ "' recursively.", null, null);
				descr0.show();

				lang.nextStep();
				descr0.hide();
				src.unhighlight(6);
				graph.unhighlightNode(node, null, null);

				val = minmax(c, false);

				// -----------------------------------------------
				// bestValue := max(bestValue, val)
				// -----------------------------------------------
				graph.highlightNode(node, null, null);
				highlightCodeLine(7);

				if (val > bestValue) {
					bestValue = val;

					descr0.setText(
							"Now MAX knows he can achieve a score of at least "
									+ bestValue + " in this position.", null,
							null);
					descr0.show();

					bestValues[currentPos].hide();
					bestValues[currentPos].setText(String.valueOf(bestValue),
							null, null);
					bestValues[currentPos].show();
					bestValStrings[currentPos] = String.valueOf(bestValue);
				} else {
					// bestValue doesn't change
					descr0.setText(
							"The examined child turned out to be no better alternative.",
							null, null);
					descr0.show();
					descr1.setText("MAX still can achieve a score of at least "
							+ bestValue + " in this position.", null, null);
					descr1.show();
				}

				lang.nextStep();
				descr0.hide();
				descr1.hide();
				src.unhighlight(7);
			}

			// -----------------------------------------------
			// return bestValue
			// -----------------------------------------------
			highlightCodeLine(8);
			descr0.setText(
					"Return value '" + bestValStrings[currentPos] + "'.", null,
					null);
			descr0.show();

			lang.nextStep();
			descr0.hide();
			src.unhighlight(8);
			graph.unhighlightNode(node, null, null);

			return bestValue;
		}

		else {
			// MIN player's turn
			// display message notifiying about max player's turn
			descr1.setText("No! It is MIN's turn.", null, null);
			descr1.show();

			src.unhighlight(3);
			highlightCodeLine(9);

			// -----------------------------------------------
			// bestValue := +infinity (modeled as largest possible Integer)
			// -----------------------------------------------
			lang.nextStep();
			descr0.hide();
			descr1.hide();
			src.unhighlight(9);
			highlightCodeLine(10);
			descr0.setText(
					"Currently all MIN knows is that his score for this node will be +\u221E or possibly lower.",
					null, null);
			descr0.show();
			bestValues[currentPos].hide();
			bestValues[currentPos].setText("+\u221E", null, null);
			bestValues[currentPos].show();

			bestValue = Integer.MAX_VALUE;
			bestValStrings[currentPos] = String.valueOf(bestValue);

			// -----------------------------------------------
			// for each child of node
			// -----------------------------------------------
			lang.nextStep();
			descr0.hide();
			src.unhighlight(10);
			highlightCodeLine(11);
			// display message
			// "evaluate all child nodes to find out which one is the best choice"
			descr0.setText(
					"Evaluate all child nodes to find out which one is the best choice.",
					null, null);
			descr0.show();

			descr1.setText(
					"In MIN's case this means finding the child node with the lowest value.",
					null, null);
			descr1.show();
			int val;

			lang.nextStep();
			descr0.hide();
			descr1.hide();
			src.unhighlight(11);

			for (algoanim.util.Node c : children(node)) {

				// -----------------------------------------------
				// val := minmax(child, TRUE)
				// -----------------------------------------------
				highlightCodeLine(12);
				String label = graph.getNodeLabel(c);
				descr0.setText("Evaluate child with label '" + label
						+ "' recursively.", null, null);
				descr0.show();

				lang.nextStep();
				descr0.hide();
				src.unhighlight(12);

				graph.unhighlightNode(node, null, null);

				val = minmax(c, true);

				// -----------------------------------------------
				// bestValue := min(bestValue, val)
				// -----------------------------------------------
				graph.highlightNode(node, null, null);
				src.unhighlight(12);
				highlightCodeLine(13);

				if (val < bestValue) {
					bestValue = val;
					descr0.setText(
							"Now MIN knows he can achieve a score of at most "
									+ bestValue + " in this position.", null,
							null);
					descr0.show();
					bestValues[currentPos].hide();
					bestValues[currentPos].setText(String.valueOf(bestValue),
							null, null);
					bestValues[currentPos].show();
					bestValStrings[currentPos] = String.valueOf(bestValue);
				} else {
					// bestValue doesn't change
					descr0.setText(
							"The examined child turned out to be no better alternative.",
							null, null);
					descr0.show();
					descr1.setText("MIN still can achieve a score of at most "
							+ bestValue + " in this position.", null, null);
					descr1.show();
				}

				lang.nextStep();
				descr0.hide();
				descr1.hide();
				src.unhighlight(13);
			}

			// -----------------------------------------------
			// return bestValue
			// -----------------------------------------------
			highlightCodeLine(14);
			descr0.setText(
					"Return value '" + bestValStrings[currentPos] + "'.", null,
					null);
			descr0.show();

			lang.nextStep();
			descr0.show();
			src.unhighlight(14);
			graph.unhighlightNode(node, null, null);

			return bestValue;
		}
	}

	/**
	 * calculates and shows the principal variation
	 * 
	 * @param node
	 *            the start node
	 * @param maximizingPlayer
	 *            determines whether it is MAX' turn or not
	 */
	private void showPrincipalVariation(algoanim.util.Node node,
			boolean maximizingPlayer) {
		algoanim.util.Node nextNode = null;
		int childPos;
		int childVal;
		int bestVal;

		// -----------------------------------------------
		// highlight current algoanim.util.Node
		// -----------------------------------------------
		graph.highlightNode(node, null, null);

		if (isLeaf(node))
			return;

		if (maximizingPlayer) {
			bestVal = Integer.MIN_VALUE;

			for (algoanim.util.Node c : children(node)) {
				childPos = graph.getPositionForNode(c);
				childVal = Integer.parseInt(bestValStrings[childPos]);

				if (childVal > bestVal) {
					nextNode = c;
					bestVal = childVal;
				}
			}

			showPrincipalVariation(nextNode, false);
		} else {
			bestVal = Integer.MAX_VALUE;

			for (algoanim.util.Node c : children(node)) {
				childPos = graph.getPositionForNode(c);
				childVal = Integer.parseInt(bestValStrings[childPos]);

				if (childVal < bestVal) {
					nextNode = c;
					bestVal = childVal;
				}
			}

			showPrincipalVariation(nextNode, true);
		}
	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		textProps = (TextProperties) props.getPropertiesByName("text");
		headerProps = (TextProperties) props.getPropertiesByName("header");
		askQuestion = (boolean) primitives.get("askQuestion");
		graphStructure = (String) primitives.get("treeStructure");
		// -----------------------------------------------
		System.out.println("Graph: " + graphStructure);
		// -----------------------------------------------
		graphProps = (GraphProperties) props
				.getPropertiesByName("graphProperties");
		srcProps = (SourceCodeProperties) props
				.getPropertiesByName("sourceCode");

		this.graph = getGraph(graphProps, graphStructure);
		graph.hide();

		start();

		lang.finalizeGeneration();
		return lang.toString();
	}

	/**
	 * Creates a graph with the specified structure and properties.
	 * 
	 * @param gp
	 *            the graph properties
	 * @param structure
	 *            the graph's structure
	 * @return graph with given structure and graph properties
	 */
	private Graph getGraph(GraphProperties gp, String structure) {
		TreeParserWithNodeNames parser = new TreeParserWithNodeNames();

		Node root = parser.parseText(structure);

		// -----------------------------------------------
		// return default graph if entered graph structure is invalid
		// -----------------------------------------------
		if (!parser.isValid()) {
			System.out.println("------PARSER INVALID------");
			return getDefaultGraph(gp);
		}

		List<Node> nodes = getFlattened(root);
		int size = nodes.size();

		int[][] graphAdjacencyMatrix = new int[size][size];

		// -----------------------------------------------
		// initialize adjacency matrix
		// -----------------------------------------------
		for (int i = 0; i < graphAdjacencyMatrix.length; i++)
			for (int j = 0; j < graphAdjacencyMatrix[0].length; j++) {
				if (connected(nodes.get(i), nodes.get(j)))
					graphAdjacencyMatrix[i][j] = 1;
				else
					graphAdjacencyMatrix[i][j] = 0;
			}

		// -----------------------------------------------
		// define the nodes and their positions
		// -----------------------------------------------
		graphNodes = new algoanim.util.Coordinates[size];

		String[] labels = new String[size];

		for (int i = 0; i < size; i++) {
			Node n = nodes.get(i);
			if (n.isLeaf())
				labels[i] = n.getValue().toString();
			else
				labels[i] = n.getId();
		}

		// set coordinates of all nodes
		setupTree(getIndexedTree(root, 0));

		Graph g = lang.newGraph("graph", graphAdjacencyMatrix, graphNodes,
				labels, null, gp);

		g.hide();

		return g;
	}

	/**
	 * sets the coordinates of all nodes contained in the tree defined by the
	 * given root node
	 * 
	 * @param node
	 *            root node of an indexed (!) tree, i.e. every node of the given
	 *            tree must have its index as value
	 */
	private void setupTree(Node node) {
		int y = upperBorder + node.getDepth() * yNodeDist;
		int depth = node.getDepth();

		if (depth > treeDepth)
			treeDepth = depth;

		if (node.isLeaf()) {
			setupNode(leftBorder, y, node);
			leftBorder += xLeafDist;
			return;
		}

		List<Node> children = node.getChildren();
		int childCount = children.size();

		for (Node child : children) {
			setupTree(child);
		}

		int x = 0;

		for (Node child : node.getChildren()) {
			x += graphNodes[child.getValue()].getX();
		}

		x = x / childCount;

		setupNode(x, y, node);
	}

	private void setupNode(int x, int y, Node n) {
		int i = (int) n.getValue();
		graphNodes[i] = new Coordinates(x, y);
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		String errorMessage = "";
		boolean error = false;
		String tree = (String) primitives.get("treeStructure");
		TreeParserWithNodeNames parser = new TreeParserWithNodeNames();
		parser.parseText(tree);
		if (!parser.isValid()) {
			errorMessage += "Error while parsing the tree.\n"
					+ parser.getMessage() + "\n\nEntered tree structure:\n"
					+ tree;
			error = true;
		}

		if (error) {
			showErrorWindow(errorMessage);
		}
		return !error; // no error found
	}

	private void showErrorWindow(String message) {
		JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), message,
				"Error", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Indicates whether two given nodes are connected by an edge.
	 * 
	 * @param a
	 *            a node
	 * @param b
	 *            another node
	 * @return true if the two nodes are connected, else false
	 */
	private boolean connected(Node a, Node b) {
		List<Node> childrenA = a.getChildren();
		List<Node> childrenB = b.getChildren();

		if (childrenA.contains(b) || childrenB.contains(a))
			return true;

		return false;
	}

	/**
	 * Flattens the tree specified by the given root node.
	 * 
	 * @param root
	 *            root node of the tree to be flattened
	 * @return list containing all nodes of the tree
	 */
	private List<Node> getFlattened(Node root) {
		List<Node> nodes = new LinkedList<Node>();

		if (root == null)
			return nodes;

		nodes.add(root);
		List<Node> children = root.getChildren();

		for (int i = 0; i < children.size(); i++) {
			nodes.addAll(getFlattened(children.get(i)));
		}

		return nodes;
	}

	private int getNodeCount(Node root) {
		return getFlattened(root).size();
	}

	/**
	 * Returns an indexed version of a given tree (defined by its root node)
	 * 
	 * @param root
	 *            the root node of the tree
	 * @param startIndex
	 *            the start index
	 * @return tree with indices as values
	 */
	private Node getIndexedTree(Node root, int startIndex) {
		int i = startIndex;
		root.setValue(i);

		ArrayList<Node> newChildren = new ArrayList<Node>();
		int acc = 0;

		List<Node> children = root.getChildren();

		for (int j = 0; j < children.size(); j++) {
			newChildren.add(getIndexedTree(children.get(j), acc + i + 1));
			acc += getNodeCount(children.get(j));
		}

		root.setChildren(newChildren);

		return root;
	}

	/**
	 * Returns a predefined default graph with the given graph properties.
	 * 
	 * @param gp
	 *            the graph properties
	 * @return default graph with predefined structure and values
	 */
	private Graph getDefaultGraph(GraphProperties gp) {
		int[][] graphAdjacencyMatrix = new int[22][22];

		// -----------------------------------------------
		// initialize adjacency matrix with zeros
		// -----------------------------------------------
		for (int i = 0; i < graphAdjacencyMatrix.length; i++)
			for (int j = 0; j < graphAdjacencyMatrix[0].length; j++)
				graphAdjacencyMatrix[i][j] = 0;

		// -----------------------------------------------
		// set edges
		// -----------------------------------------------
		graphAdjacencyMatrix[0][1] = 1;
		graphAdjacencyMatrix[1][0] = 1;
		graphAdjacencyMatrix[0][2] = 1;
		graphAdjacencyMatrix[2][0] = 1;
		graphAdjacencyMatrix[0][2] = 1;
		graphAdjacencyMatrix[2][0] = 1;
		graphAdjacencyMatrix[0][3] = 1;
		graphAdjacencyMatrix[3][0] = 1;
		graphAdjacencyMatrix[1][4] = 1;
		graphAdjacencyMatrix[4][1] = 1;
		graphAdjacencyMatrix[1][5] = 1;
		graphAdjacencyMatrix[5][1] = 1;
		graphAdjacencyMatrix[2][6] = 1;
		graphAdjacencyMatrix[6][2] = 1;
		graphAdjacencyMatrix[2][7] = 1;
		graphAdjacencyMatrix[7][2] = 1;
		graphAdjacencyMatrix[3][8] = 1;
		graphAdjacencyMatrix[8][3] = 1;
		graphAdjacencyMatrix[3][9] = 1;
		graphAdjacencyMatrix[9][3] = 1;
		graphAdjacencyMatrix[4][10] = 1;
		graphAdjacencyMatrix[10][4] = 1;
		graphAdjacencyMatrix[4][11] = 1;
		graphAdjacencyMatrix[11][4] = 1;
		graphAdjacencyMatrix[5][12] = 1;
		graphAdjacencyMatrix[12][5] = 1;
		graphAdjacencyMatrix[5][13] = 1;
		graphAdjacencyMatrix[13][5] = 1;
		graphAdjacencyMatrix[6][14] = 1;
		graphAdjacencyMatrix[14][6] = 1;
		graphAdjacencyMatrix[6][15] = 1;
		graphAdjacencyMatrix[15][6] = 1;
		graphAdjacencyMatrix[7][16] = 1;
		graphAdjacencyMatrix[16][7] = 1;
		graphAdjacencyMatrix[7][17] = 1;
		graphAdjacencyMatrix[17][7] = 1;
		graphAdjacencyMatrix[8][18] = 1;
		graphAdjacencyMatrix[18][8] = 1;
		graphAdjacencyMatrix[8][19] = 1;
		graphAdjacencyMatrix[19][8] = 1;
		graphAdjacencyMatrix[9][20] = 1;
		graphAdjacencyMatrix[20][9] = 1;
		graphAdjacencyMatrix[9][21] = 1;
		graphAdjacencyMatrix[21][9] = 1;

		// -----------------------------------------------
		// define the nodes and their positions
		// -----------------------------------------------
		graphNodes = new algoanim.util.Coordinates[22];
		graphNodes[0] = new Coordinates(275, 90); // A
		graphNodes[1] = new Coordinates(123, 150); // B
		graphNodes[2] = new Coordinates(275, 150); // C
		graphNodes[3] = new Coordinates(427, 150); // D
		graphNodes[4] = new Coordinates(85, 210); // E
		graphNodes[5] = new Coordinates(161, 210); // F
		graphNodes[6] = new Coordinates(237, 210); // G
		graphNodes[7] = new Coordinates(313, 210); // H
		graphNodes[8] = new Coordinates(389, 210); // I
		graphNodes[9] = new Coordinates(465, 210); // J
		graphNodes[10] = new Coordinates(66, 270); // K
		graphNodes[11] = new Coordinates(104, 270); // L
		graphNodes[12] = new Coordinates(142, 270); // M
		graphNodes[13] = new Coordinates(180, 270); // N
		graphNodes[14] = new Coordinates(218, 270); // O
		graphNodes[15] = new Coordinates(256, 270); // P
		graphNodes[16] = new Coordinates(294, 270); // Q
		graphNodes[17] = new Coordinates(332, 270); // R
		graphNodes[18] = new Coordinates(370, 270); // S
		graphNodes[19] = new Coordinates(408, 270); // T
		graphNodes[20] = new Coordinates(446, 270); // U
		graphNodes[21] = new Coordinates(484, 270); // V

		// -----------------------------------------------
		// define the names of the nodes
		// -----------------------------------------------
		String[] labels = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
				"5", "2", "7", "1", "6", "7", "9", "4", "3", "5", "9", "1" };

		Graph g = lang.newGraph("graph", graphAdjacencyMatrix, graphNodes,
				labels, null, gp);

		g.hide();

		return g;
	}

	public String getName() {
		return "MinMax [EN]";
	}

	public String getAlgorithmName() {
		return "MinMax";
	}

	public String getAnimationAuthor() {
		return "Carina Oberle";
	}

	public String getDescription() {
		return "MinMax (also known as Minimax) is an adversarial search algorithm."
				+ "\n"
				+ "In a two-player game visualized as a tree, it recursively calculates the"
				+ "\n"
				+ "optimal game strategy for the player who is starting the game,"
				+ "\n"
				+ "given perfect play of his opponent."
				+ "\n"
				+ "\n"
				+ "The algorithm only works for deterministic, perfect-information games,"
				+ "\n"
				+ "like for example:"
				+ "\n"
				+ "\n"
				+ "&bull Go"
				+ "\n\n"
				+ "&bull Tic-Tac-Toe"
				+ "\n\n"
				+ "&bull Othello"
				+ "\n\n"
				+ "&bull Checkers"
				+ "\n\n"
				+ "&bull Chess"
				+ "\n\n"
				+ "&bull ...";
	}

	public String getCodeExample() {
		return "<b>function</b> minmax(node, maximizingPlayer)" + "\n"
				+ "    <b>if</b> node is a terminal node" + "\n"
				+ "        <b>return</b> the value of node" + "\n"
				+ "    <b>if</b> maximizingPlayer" + "\n"
				+ "        bestValue := -&infin" + "\n\n"
				+ "        <b>for each</b> child of node" + "\n"
				+ "            val := minmax(child, FALSE)" + "\n"
				+ "            bestValue := max(bestValue, val)" + "\n"
				+ "        <b>return</b> bestValue" + "\n" + "    <b>else</b>"
				+ "\n" + "        bestValue := +&infin" + "\n\n"
				+ "        <b>for each</b> child of node" + "\n"
				+ "            val := minmax(child, TRUE)" + "\n"
				+ "            bestValue := min(bestValue, val)" + "\n"
				+ "        <b>return</b> bestValue";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.ENGLISH;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_SEARCH);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

}