package generators.searching;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleSelectionQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.Locale;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import algoanim.animalscript.AnimalScript;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.TriangleProperties;
import algoanim.properties.RectProperties;
import algoanim.primitives.generators.Language;
import algoanim.primitives.Circle;
import algoanim.primitives.Text;
import algoanim.primitives.Polyline;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Graph;
import algoanim.primitives.Triangle;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Node;

/**
 * @author Carina Oberle <carina.oberle@stud.tu-darmstadt.de>
 * @version 2.0 2014-05-28
 */
public class TSPviaGBFGenerator implements ValidatingGenerator {
	/**
	 * concrete language object used for creating output
	 */
	private Language lang;

	/**
	 * user defined text properties
	 */
	private TextProperties textProps;

	/**
	 * the source code shown in the animation
	 */
	private SourceCode src;

	/**
	 * user defined source code properties
	 */
	private SourceCodeProperties srcProps;

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
	 * properties of the rectangle surrounding the animation description
	 */
	private RectProperties rectProps;

	/**
	 * user defined destination nodes
	 */
	private String[] destinations;

	/**
	 * straight-line distance between nodes
	 */
	private double[][] heuristics;

	/**
	 * contains number of visits for each node
	 */
	private int[] timesVisited;

	/**
	 * user defined color for the visited edges/nodes
	 */
	private Color routeColor;

	/**
	 * user defined color for the start node
	 */
	private Color startNodeColor;

	/**
	 * user defined color for the destination nodes
	 */
	private Color destNodeColor;

	/**
	 * the label of the start node
	 */
	private String startLabel;

	/**
	 * user defined value indicating whether questions shall be asked or not
	 */
	private boolean askQuestions;

	/**
	 * the graph used to demonstrate the algorithm
	 */
	private Graph graph;

	/**
	 * user defined graph properties
	 */
	private GraphProperties graphProps;

	/**
	 * directed copy of the actual graph
	 */
	private Graph dirGraph;

	/**
	 * the header text containing the title
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
	 * Indicates whether the description box shall be placed relative to the
	 * graph or not
	 */
	private boolean useOffsetFromGraph;

	/**
	 * List containing all destination nodes
	 */
	private LinkedList<Node> destNodes;

	/**
	 * the salesman's start place
	 */
	private Node startNode;

	/**
	 * the current costs for this part of the route
	 */
	private int currentCosts;

	/**
	 * Text displaying the current costs
	 */
	private Text costs;

	/**
	 * properties of the text labels
	 */
	private TextProperties labelTextProps;

	/**
	 * Text displaying the next destination to be visited
	 */
	private Text nextDestination;

	/**
	 * radius for the node highlighting circles
	 */
	private int radius;

	/**
	 * array containing the destination highlight circles
	 */
	private HashMap<Node, Circle> destCircs;

	/**
	 * array containing the source code indentations for each code line
	 */
	private int[] indent;

	/**
	 * the depth of the graph
	 */
	private int graphDepth;

	/**
	 * the depth of the directed graph
	 */
	private int dirGraphDepth;

	/**
	 * depth of the white circles for the edge cost labels
	 */
	private int whiteCircDepth;

	/**
	 * depth of the colored highlight circles
	 */
	private int highlightCircDepth;

	/**
	 * depth of the "textbox"
	 */
	private int rectDepth;

	/**
	 * Indicates whether a loop (probably) has been detected
	 */
	private boolean loop;

	/**
	 * the upper border for the graph (the graph must have its highest node
	 * beyond this border, otherwise it is moved downwards)
	 */
	private int upperBorder;
	
	private int threshold;

	/**
	 * Default constructor
	 * 
	 * @param l
	 *            the concrete language object used for creating output
	 */
	public TSPviaGBFGenerator(Language l) {
		lang = l;
		lang.setStepMode(true);
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
	}

	public TSPviaGBFGenerator() {
		lang = new AnimalScript(
				"Traveling Salesman Problem per Greedy Best-First Search [EN]",
				"Carina Oberle", 840, 700);
		lang.setStepMode(true);
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
	}

	public void init() {
		lang = new AnimalScript(
				"Traveling Salesman Problem per Greedy Best-First Search [EN]",
				"Carina Oberle", 840, 700);
		lang.setStepMode(true);
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
		title = "Traveling Salesman Problem per Greedy Best-First Search";
		moveDuration = new TicksTiming(10);
		useOffsetFromGraph = true;
		destNodes = new LinkedList<Node>();
		destCircs = new HashMap<Node, Circle>();
		radius = 20;
		labelTextProps = new TextProperties();
		labelTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.BOLD, 12));
		indent = new int[] { 0, 1, 1, 0, 0, 1, 2, 2, 1, 1, 1, 1, 0, 0, 1, 2, 1,
				1 };
		loop = false;
		upperBorder = 120;
		threshold = 325;

		// set depth values
		rectDepth = 2;
		whiteCircDepth = 2;
		graphDepth = 3;
		dirGraphDepth = 4;
		highlightCircDepth = 5;
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

		Node[] nodes = {
				new Offset(0, 10, "header", AnimalScript.DIRECTION_SW),
				new Offset(400, 10, "header", AnimalScript.DIRECTION_SE) };
		hLine = lang.newPolyline(nodes, "header", null, lineProps);

		// -----------------------------------------------
		// setup the start page with the description
		// -----------------------------------------------
		lang.nextStep();
		int n = 0;

		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.PLAIN, 16));

		lang.newText(
				new Offset(0, 20, "header", AnimalScript.DIRECTION_SW),
				"The traveling salesman problem (TSP) is a famous combinatorial optimization problem.",
				"intro[" + n + "]", null, textProps);
		lang.newText(
				new Offset(0, 3, "intro[" + (n++) + "]",
						AnimalScript.DIRECTION_SW),
				"It consists of a salesperson that has to visit a list of cities and then travel back to his starting place.",
				"intro[" + n + "]", null, textProps);
		lang.newText(new Offset(0, 3, "intro[" + (n++) + "]",
				AnimalScript.DIRECTION_SW),
				"The aim is to find a route with the least possible costs.",
				"intro[" + n + "]", null, textProps);
		lang.newText(
				new Offset(0, 3, "intro[" + (n++) + "]",
						AnimalScript.DIRECTION_SW),
				"TSP is NP-hard (Non-deterministic Polynomial-time hard), which means it is 'at least as hard",
				"intro[" + n + "]", null, textProps);
		lang.newText(
				new Offset(0, 3, "intro[" + (n++) + "]",
						AnimalScript.DIRECTION_SW),
				"as the hardest problems in complexity class NP' (see http://en.wikipedia.org/wiki/NP-hard).",
				"intro[" + n + "]", null, textProps);
		lang.newText(
				new Offset(0, 3, "intro[" + (n++) + "]",
						AnimalScript.DIRECTION_SW),
				"In this animation we use Greedy Best-First Search with straight-line distance as heuristics",
				"intro[" + n + "]", null, textProps);
		lang.newText(new Offset(0, 3, "intro[" + (n++) + "]",
				AnimalScript.DIRECTION_SW),
				"to get an approximative solution.", "intro[" + n + "]", null,
				textProps);

		// -----------------------------------------------
		// show setup header
		// -----------------------------------------------
		lang.nextStep("Intro");
		font = font.deriveFont(Font.BOLD, 20);
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, font);
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.BOLD, 20));
		lang.newText(new Offset(0, 20, "intro[" + n + "]",
				AnimalScript.DIRECTION_SW),
				"Notes on Greedy Best-First Search", "setupHeader", null,
				headerProps);
		// -----------------------------------------------
		// display first part of setup
		// -----------------------------------------------
		lang.nextStep();
		n = 0;
		lang.newText(
				new Offset(0, 10, "setupHeader", AnimalScript.DIRECTION_SW),
				"- Informed Search algorithm: uses heuristics to evaluate the 'desirability' of states",
				"setup[" + n + "]", null, textProps);
		// -----------------------------------------------
		// display second part of setup
		// -----------------------------------------------
		lang.nextStep();
		lang.newText(
				new Offset(0, 3, "setup[" + (n++) + "]",
						AnimalScript.DIRECTION_SW),
				"- Heuristics can informally be seen as 'rule of thumb': may be helpful, but may as well go wrong",
				"setup[" + n + "]", null, textProps);
		lang.newText(new Offset(0, 3, "setup[" + (n++) + "]",
				AnimalScript.DIRECTION_SW),
				"  (e.g. straight-line distance vs. real distance)", "setup["
						+ n + "]", null, textProps);
		// -----------------------------------------------
		// display third part of setup
		// -----------------------------------------------
		lang.nextStep();
		lang.newText(
				new Offset(0, 3, "setup[" + (n++) + "]",
						AnimalScript.DIRECTION_SW),
				"- GBF Search always expands the node that appears to be the best choice",
				"setup[" + n + "]", null, textProps);
		lang.newText(
				new Offset(0, 3, "setup[" + (n++) + "]",
						AnimalScript.DIRECTION_SW),
				"  (i.e. the node that hopefully leads to minimal costs for the salesman)",
				"setup[" + n + "]", null, textProps);
		// -----------------------------------------------
		// display fourth part of setup
		// -----------------------------------------------
		lang.nextStep();
		lang.newText(new Offset(0, 3, "setup[" + (n++) + "]",
				AnimalScript.DIRECTION_SW),
				"- Not complete: can get stuck in loops!", "setup[" + n + "]",
				null, textProps);
		// -----------------------------------------------
		// hide the description
		// -----------------------------------------------
		lang.nextStep("Greedy Best-First");
		lang.hideAllPrimitives();
		header.show();
		hLine.show();

		// -----------------------------------------------
		// move graph if necessary
		// -----------------------------------------------
		lang.nextStep();
		int graphSize = graph.getSize();
		Coordinates c;
		int currentY;
		int highestY = Integer.MAX_VALUE;
		int lowestY = Integer.MIN_VALUE;

		for (int i = 0; i < graphSize; i++) {
			c = (Coordinates) graph.getNodeForIndex(i);
			currentY = c.getY();

			if (currentY < highestY) {
				highestY = currentY;
			}

			if (currentY > lowestY) {
				lowestY = currentY;
			}
		}

		String[] graphLabels = new String[graphSize];

		for (int i = 0; i < graphSize; i++) {
			graphLabels[i] = graph.getNodeLabel(i);
		}

		if (highestY < upperBorder) {
			// graph.moveBy("translate", 0, upperBorder - highestY, null, null);
			// -> doesn't work properly
			Coordinates[] nodeCoords = new Coordinates[graphSize];
			Coordinates current;
			int dy = upperBorder - highestY;

			for (int i = 0; i < graphSize; i++) {
				current = (Coordinates) graph.getNodeForIndex(i);
				nodeCoords[i] = new Coordinates(current.getX(), current.getY()
						+ dy);
			}

			graph = lang.newGraph("graph", graph.getAdjacencyMatrix(),
					nodeCoords, graphLabels, null, graphProps);
		}

		// -----------------------------------------------
		// display graph
		// -----------------------------------------------
		graph.show();
		startNode = graph.getNodeForIndex(0);
		startLabel = graph.getNodeLabel(startNode);

		// hide standard edges weights
		// (only appear when re-generating the animation)
		for (int i = 0; i < graphSize; i++) {
			for (int j = 0; j < graphSize; j++) {
				graph.hideEdgeWeight(i, j, null, null);
			}
		}

		// -----------------------------------------------
		System.out.println("Start Node: " + startLabel);
		// -----------------------------------------------

		// show costs per edge
		Node current;
		Node to;
		Coordinates c1, c2;
		Node[] graphNodes = new Node[graphSize];

		for (int i = 0; i < graphSize; i++) {
			current = graph.getNodeForIndex(i);
			int[] edges = graph.getEdgesForNode(current);
			graphNodes[i] = current;
			graphLabels[i] = graph.getNodeLabel(current);

			for (int j = 0; j < edges.length; j++) {
				int w = edges[j];
				if (w != 0) {
					to = graph.getNode(j);
					c1 = (Coordinates) current;
					c2 = (Coordinates) to;
					int x = (c1.getX() + c2.getX()) / 2;
					int y = (c1.getY() + c2.getY()) / 2;

					lang.newText(new Coordinates(x, y), String.valueOf(w), "w"
							+ i, null, labelTextProps);
					CircleProperties circProps = new CircleProperties();
					circProps
							.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
					circProps.set(AnimationPropertiesKeys.FILL_PROPERTY,
							Color.WHITE);
					circProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
							Color.WHITE);
					circProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY,
							whiteCircDepth);

					lang.newCircle(new Coordinates(x + 4, y + 6), 5,
							"whiteCirc" + i + j, null, circProps);
				}
			}
		}

		timesVisited = new int[graphSize];

		for (int i = 0; i < graphSize; i++)
			timesVisited[i] = 0;

		// -----------------------------------------------
		// initialize heuristics
		// -----------------------------------------------
		heuristics = new double[graphSize][graphSize];

		for (int i = 0; i < graphSize; i++) {
			for (int j = 0; j < graphSize; j++) {
				Coordinates a = (Coordinates) graph.getNodeForIndex(i);
				Coordinates b = (Coordinates) graph.getNodeForIndex(j);

				int ax = a.getX();
				int ay = a.getY();

				int bx = b.getX();
				int by = b.getY();

				// straight-line distance
				heuristics[i][j] = Math.sqrt(Math.pow(ax - bx, 2)
						+ Math.pow(ay - by, 2));
			}
		}

		// -----------------------------------------------
		// display pseudo code
		// -----------------------------------------------
		lang.nextStep("Animation Start");
		int l = 0;
		int dx = 25;
		int dy = -15;

		if (lowestY > threshold) {
			dx += 110;
		}

		src = lang.newSourceCode(new Offset(dx, dy, "graph",
				AnimalScript.DIRECTION_NE), "src", null, srcProps);
		// -----------------------------------------------
		// add code lines to the SourceCode object
		// Line, name, indentation, display delay
		// -----------------------------------------------
		src.addCodeLine("function init(node)", null, indent[l++], null); // 0
		src.addCodeLine("startNode := node", null, indent[l++], null); // 1
		src.addCodeLine("costs := 0", null, indent[l++], null); // 2
		src.addCodeLine("", null, indent[l++], null); // 3
		src.addCodeLine("function travel(node, dests)", null, indent[l++], null); // 4
		src.addCodeLine("if dests is empty", null, indent[l++], null); // 5
		src.addCodeLine("costs += greedy-best-first(node, startNode)", null,
				indent[l++], null); // 6
		src.addCodeLine("return", null, indent[l++], null); // 7
		src.addCodeLine("next := probably-best(node, dests)", null,
				indent[l++], null); // 8
		src.addCodeLine("costs += greedy-best-first(node, next)", null,
				indent[l++], null); // 9
		src.addCodeLine("dests.remove(next)", null, indent[l++], null); // 10
		src.addCodeLine("travel(next, dests)", null, indent[l++], null); // 11
		src.addCodeLine("", null, indent[l++], null); // 12
		src.addCodeLine("function greedy-best-first(start, dest)", null,
				indent[l++], null); // 13
		src.addCodeLine("if start = dest", null, indent[l++], null); // 14
		src.addCodeLine("return 0", null, indent[l++], null); // 15
		src.addCodeLine("next := probably-best(start, dest)", null,
				indent[l++], null); // 16
		src.addCodeLine(
				"return costs(start, next) + greedy-best-first(next, dest)",
				null, indent[l++], null); // 17

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

		// -----------------------------------------------
		// highlight destination nodes
		// -----------------------------------------------
		lang.nextStep();

		CircleProperties circProps = new CircleProperties();
		circProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, destNodeColor);
		circProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		circProps.set(AnimationPropertiesKeys.FILL_PROPERTY, destNodeColor);
		circProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY,
				highlightCircDepth);

		Node dest;
		String d;
		LinkedList<String> enteredDests = new LinkedList<String>();

		for (int i = 0; i < destinations.length; i++) {
			enteredDests.add(destinations[i]);
		}

		for (int i = 0; i < graphSize; i++) {
			dest = graph.getNodeForIndex(i);
			d = graph.getNodeLabel(dest);

			if (enteredDests.contains(d) && !d.equals(startLabel)
					&& !destNodes.contains(dest)) {
				destNodes.add(dest);
			}
		}

		for (int i = 0; i < destNodes.size(); i++) {
			dest = destNodes.get(i);
			Coordinates coord = (Coordinates) dest;
			int x = coord.getX() + 4;
			int y = coord.getY() + 8;
			destCircs.put(destNodes.get(i), lang.newCircle(
					new Coordinates(x, y), radius, "C" + i, null, circProps));
		}

		// number of destination nodes
		int destCount = destNodes.size();

		// String containing the destination nodes
		String dests = getAltString(new LinkedList<Node>(destNodes));

		// -----------------------------------------------
		// show description box
		// -----------------------------------------------
		if (lowestY < threshold)
			useOffsetFromGraph = false;

		costs = lang.newText(new Offset(-122, 0, "src",
				AnimalScript.DIRECTION_SW), "", "costs", null, labelTextProps);
		costs.hide();

		if (useOffsetFromGraph)
			descr0 = lang.newText(new Offset(-10, 50, "graph",
					AnimalScript.DIRECTION_SW), "The salesman starts in place "
					+ startLabel + " and has to visit " + destCount
					+ " places: " + dests + ".", "descr[0]", null, textProps);
		else
			descr0 = lang.newText(new Coordinates(20, 450),
					"The salesman starts in place " + startLabel
							+ " and has to visit " + destCount + " places: "
							+ dests + ".", "descr[0]", null, textProps);

		descr1 = lang.newText(new Offset(0, 3, "descr[0]",
				AnimalScript.DIRECTION_SW),
				"But which route should he take to have minimal costs?",
				"descr[1]", null, textProps);

		rectProps = new RectProperties();
		rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				srcProps.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY));
		rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, rectDepth);

		lang.newRect(new Offset(-5, -5, "descr[0]", AnimalScript.DIRECTION_NW),
				new Offset(160, 30, "descr[0]", AnimalScript.DIRECTION_SE),
				"rect", null, rectProps);

		// -----------------------------------------------
		// initialization
		// -----------------------------------------------
		lang.nextStep();
		initialize();

		// make edges (really) unweighted
		adjustMatrix();

		// -----------------------------------------------
		// duplicate graph
		// -----------------------------------------------
		graphProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, dirGraphDepth);
		graphProps.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, true);
		graphProps.set(AnimationPropertiesKeys.EDGECOLOR_PROPERTY, routeColor);
		graphProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
				routeColor);

		dirGraph = lang.newGraph("dirGraph", graph.getAdjacencyMatrix(),
				graphNodes, graphLabels, null, graphProps);

		// hide all edges
		for (int i = 0; i < graphSize; i++) {
			for (int j = 0; j < graphSize; j++) {
				dirGraph.hideEdge(i, j, null, null);
				dirGraph.hideEdgeWeight(i, j, null, null);
			}
		}

		// -----------------------------------------------
		// call the TSP per GBF algorithm
		// -----------------------------------------------
		descr0.hide();
		descr1.hide();
		travel(startNode, destNodes);
		codeMarker.hide();

		// -----------------------------------------------
		// show result
		// -----------------------------------------------
		if (loop) {
			descr0.setText("Probably the salesman is stuck in a loop... ",
					null, null);
			descr0.show();
			descr1.setText(
					"This can happen because Greedy Best-First Search is not complete.",
					null, null);
			descr1.show();

			lang.nextStep();
			descr0.hide();
			descr1.hide();
			descr0.setText(
					"A better approach to avoid this is the A* algorithm.",
					null, null);
			descr0.show();
			descr1.setText(
					"See http://en.wikipedia.org/wiki/A*_search_algorithm",
					null, null);
			descr1.show();
		}

		else {
			descr0.setText("The algorithm terminated with costs = "
					+ currentCosts + ",", null, null);
			descr0.show();
			descr1.setText(
					"which means the salesman traveled a distance with overall cost "
							+ currentCosts + ".", null, null);
			descr1.show();
		}

		// -----------------------------------------------
		// display summary
		// -----------------------------------------------
		lang.nextStep("Summary");
		descr0.hide();
		descr1.hide();
		descr0.setText(
				"The optimality of this solution strongly depends on the used heuristics.",
				null, null);
		descr0.show();

		lang.nextStep();
		descr0.hide();
		descr0.setText(
				"Good heuristics can help finding solutions for extremely large problems",
				null, null);
		descr0.show();
		descr1.setText("(millions of cities) within a reasonable time,", null,
				null);
		descr1.show();

		lang.nextStep();
		descr0.hide();
		descr1.hide();
		descr0.setText(
				"which are with a high probability just 2–3% away from the optimal solution.",
				null, null);
		descr0.show();

		lang.nextStep();
		descr0.hide();
		descr1.hide();
		descr0.setText(
				"Bad heuristics, however, can yield the opposite: suboptimal paths with high costs.",
				null, null);
		descr0.show();

		lang.nextStep();
		descr0.hide();
		descr0.setText(
				"The crucial point for using heuristics instead of exact methods lies in the complexity.",
				null, null);
		descr0.show();

		lang.nextStep();
		descr0.hide();
		descr0.setText(
				"The running time for trying all possible paths (using brute force search) lies",
				null, null);
		descr0.show();
		descr1.setText("within a polynomial factor of O(n!).", null, null);
		descr1.show();

		lang.nextStep();
		descr0.hide();
		descr0.setText(
				"The Held-Karp algorithm solves the problem in O(n\u00B22\u207F) using dynamic programming.",
				null, null);
		descr0.show();
		descr1.hide();
		descr1.setText(
				"For exact algorithms, improving these time bounds seems to be difficult.",
				null, null);
		descr1.show();

		lang.nextStep();
		descr0.hide();
		descr0.setText(
				"Approximative algorithms have a significantly better running time",
				null, null);
		descr0.show();
		descr1.hide();
		descr1.setText(
				"and are therefore commonly preferred over exact methods.",
				null, null);
		descr1.show();

		lang.nextStep();
		descr0.hide();
		descr0.setText(
				"See http://en.wikipedia.org/wiki/Travelling_salesman_problem",
				null, null);
		descr0.show();
		descr1.hide();
		descr1.setText("and http://en.wikipedia.org/wiki/Greedy_algorithm",
				null, null);
		descr1.show();

		// -----------------------------------------------
		// show final multiple selection question
		// -----------------------------------------------
		if (askQuestions) {
			lang.nextStep();
			MultipleSelectionQuestionModel ms = new MultipleSelectionQuestionModel(
					"question1");
			ms.setPrompt("Please tick the correct answer(s)!");
			ms.addAnswer(
					"Greedy Best-First Search always finds an optimal solution.",
					-1, "False: GBF is non-optimal.");
			ms.addAnswer("Greedy Best-First Search can get stuck in loops.", 1,
					"Correct: GBF is incomplete.");
			ms.addAnswer("Greedy Best-First Search uses heuristics.", 1,
					"Correct: GBF uses heuristics.");
			ms.addAnswer("The Traveling Salesman Problem is NP-hard.", 1,
					"Correct: TSP is NP-hard.");

			lang.addMSQuestion(ms);
		}
	}

	private void initialize() {
		highlightCodeLine(0);
		descr0.hide();
		descr1.hide();
		descr0.setText("Call init(" + startLabel + ").", null, null);
		descr0.show();

		lang.nextStep();
		src.unhighlight(0);
		highlightCodeLine(1);
		descr0.hide();
		descr0.setText("Set " + startLabel + " as start node.", null, null);
		descr0.show();

		// -----------------------------------------------
		// highlight start Node
		// -----------------------------------------------
		Coordinates c = (Coordinates) startNode;
		int x = c.getX() + 4;
		int y = c.getY() + 8;
		CircleProperties startCircProps = new CircleProperties();
		startCircProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				startNodeColor);
		startCircProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		startCircProps.set(AnimationPropertiesKeys.FILL_PROPERTY,
				startNodeColor);
		startCircProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY,
				highlightCircDepth);
		lang.newCircle(new Coordinates(x, y), radius, "StartCirc", null,
				startCircProps);

		// -----------------------------------------------
		// display "shadow" rects
		// -----------------------------------------------
		RectProperties labelRectProps = new RectProperties();
		labelRectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		labelRectProps.set(AnimationPropertiesKeys.FILL_PROPERTY,
				Color.LIGHT_GRAY);

		lang.newRect(new Offset(12, 22, "header", AnimalScript.DIRECTION_SW),
				new Offset(22, 32, "header", AnimalScript.DIRECTION_SW),
				"startShadowRect", null, labelRectProps);
		lang.newRect(new Offset(2, 12, "startShadowRect",
				AnimalScript.DIRECTION_SW), new Offset(12, 22,
				"startShadowRect", AnimalScript.DIRECTION_SW),
				"destShadowRect", null, labelRectProps);

		// -----------------------------------------------
		// display color rects
		// -----------------------------------------------
		labelRectProps.set(AnimationPropertiesKeys.FILL_PROPERTY,
				startNodeColor);

		int dx = -2;
		int dy = -2;

		lang.newRect(new Offset(-2, -2, "startShadowRect",
				AnimalScript.DIRECTION_NW), new Offset(-2, -2,
				"startShadowRect", AnimalScript.DIRECTION_SE), "startLabelBox",
				null, labelRectProps);

		labelRectProps
				.set(AnimationPropertiesKeys.FILL_PROPERTY, destNodeColor);

		lang.newRect(new Offset(-2, -2, "destShadowRect",
				AnimalScript.DIRECTION_NW), new Offset(-2, -2,
				"destShadowRect", AnimalScript.DIRECTION_SE), "destLabelBox",
				null, labelRectProps);

		// -----------------------------------------------
		// display color labels
		// -----------------------------------------------
		dx = 15;
		dy = -7;

		lang.newText(new Offset(dx, dy, "startLabelBox",
				AnimalScript.DIRECTION_W), "Start node", "startLabelText",
				null, labelTextProps);

		lang.newText(new Offset(dx, dy, "destLabelBox",
				AnimalScript.DIRECTION_W), "Destination node", "destLabelText",
				null, labelTextProps);

		// -----------------------------------------------
		// costs := 0
		// -----------------------------------------------
		lang.nextStep();
		descr0.hide();
		src.unhighlight(1);
		highlightCodeLine(2);

		descr0.setText("By now the salesman hasn't got any costs.", null, null);
		descr0.show();

		nextDestination = lang.newText(new Offset(0, 0, "costs",
				AnimalScript.DIRECTION_SW), "", "nextDestination", null,
				labelTextProps);

		currentCosts = 0;
		costs.setText("Current costs: " + currentCosts, null, null);
		costs.show();

		lang.nextStep();
		src.unhighlight(2);
	}

	/**
	 * Calculates the potentially best route (i.e. the route with the smallest
	 * costs)
	 * 
	 * @param n
	 *            the current node
	 * @param dests
	 *            the destination node to be visited
	 */
	private void travel(Node n, LinkedList<Node> dests) {
		String currentLabel = graph.getNodeLabel(n);
		int currentPos = graph.getPositionForNode(n);

		// -----------------------------------------------
		System.out.println("travel(" + currentLabel + ", "
				+ getAltString(new LinkedList<Node>(dests)) + ")");
		// -----------------------------------------------

		// highlight first line of code
		highlightCodeLine(4);
		// highlight current node
		graph.highlightNode(n, null, null);
		timesVisited[currentPos]++;

		// check for possible loop
		if (timesVisited[currentPos] > 3) {
			loop = true;
			return;
		}
		String destString = "(";

		for (int i = 0; i < dests.size(); i++) {
			destString = destString.concat("'"
					+ graph.getNodeLabel(dests.get(i)));
			if (i == dests.size() - 1)
				destString = destString.concat("')");
			else
				destString = destString.concat("', ");
		}

		descr0.setText("Call travel(Node '" + currentLabel + "', Destinations "
				+ destString + ").", null, null);
		descr0.show();

		// -----------------------------------------------
		// if dests if empty
		// -----------------------------------------------
		lang.nextStep();
		descr0.hide();
		src.unhighlight(4);
		highlightCodeLine(5);

		descr0.setText("Are there any destinations left to visit?", null, null);
		descr0.show();

		lang.nextStep();

		if (dests.isEmpty()) {
			descr1.setText(
					"No! The salesman already visited all of his destinations.",
					null, null);
			descr1.show();

			lang.nextStep();
			descr0.hide();
			descr1.hide();
			src.unhighlight(5);

			// -----------------------------------------------
			// costs += greedy-best-first(node, startNode)
			// -----------------------------------------------
			highlightCodeLine(6);
			descr0.setText(
					"Now the salesman is ready to travel home again to place '"
							+ startLabel + "'.", null, null);
			descr0.show();

			lang.nextStep();
			descr0.hide();
			src.unhighlight(6);

			currentCosts += greedyBestFirst(n, startNode);

			if (loop)
				return;

			highlightCodeLine(6);
			descr0.setText("Update costs.", null, null);
			descr0.show();
			costs.hide();
			costs.setText("Current costs: " + currentCosts, null, null);
			costs.show();

			lang.nextStep();
			descr0.hide();
			src.unhighlight(6);

			return;
		}

		String p;
		if (dests.size() == 1)
			p = "place ";
		else
			p = "places ";

		String places = getAltString(new LinkedList<Node>(dests));
		descr1.setText("Yes! The salesman still has to visit " + p + places
				+ ".", null, null);
		descr1.show();

		lang.nextStep();
		descr0.hide();
		descr1.hide();
		src.unhighlight(5);

		// -----------------------------------------------
		// next := probably-best(dest)
		// -----------------------------------------------
		highlightCodeLine(8);
		Node nextDest = n;
		Node d;
		int destIndex;
		double min = Integer.MAX_VALUE;
		double h;

		for (int i = 0; i < dests.size(); i++) {
			d = dests.get(i);
			destIndex = graph.getPositionForNode(d);
			h = heuristics[currentPos][destIndex];
			if (h < min) {
				min = h;
				nextDest = d;
			}
		}

		String nextDestLabel = graph.getNodeLabel(nextDest);

		// display "next destination" label
		nextDestination.hide();
		nextDestination.setText("Next destination: " + nextDestLabel, null,
				null);
		nextDestination.show();

		descr0.setText(
				"According to the chosen heuristics (straight-line distance), it is probably",
				null, null);
		descr0.show();
		descr1.setText("the best decision to choose place '" + nextDestLabel
				+ "' as next destination.", null, null);
		descr1.show();

		// -----------------------------------------------
		// costs += greedy-best-first(node, next)
		// -----------------------------------------------
		lang.nextStep();
		descr0.hide();
		descr1.hide();
		src.unhighlight(8);
		highlightCodeLine(9);
		descr0.setText(
				"Now the salesman needs to find a good way of getting to his next destination '"
						+ nextDestLabel + "'.", null, null);
		descr0.show();
		descr1.setText(
				"The costs for this path are added to his current costs.",
				null, null);
		descr1.show();

		lang.nextStep();
		descr0.hide();
		descr1.hide();
		src.unhighlight(9);

		currentCosts += greedyBestFirst(n, nextDest);

		if (loop)
			return;

		costs.hide();
		costs.setText("Current costs: " + currentCosts, null, null);
		costs.show();
		highlightCodeLine(9);
		descr0.setText("Update costs.", null, null);
		descr0.show();

		// -----------------------------------------------
		// dests.remove(next)
		// -----------------------------------------------
		lang.nextStep();
		src.unhighlight(9);
		highlightCodeLine(10);
		descr0.hide();
		descr0.setText("Place '" + nextDestLabel
				+ "' can be removed from the list of destinations.", null, null);
		descr0.show();

		// unfill dest circle
		Circle c = destCircs.get(nextDest);
		c.hide();
		CircleProperties circProps = c.getProperties();
		circProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, false);

		destCircs.put(nextDest, lang.newCircle(c.getCenter(), radius,
				c.getName(), null, circProps));
		dests.remove(nextDest);
		nextDestination.hide();
		nextDestination.setText("Next destination: -", null, null);
		nextDestination.show();

		// -----------------------------------------------
		// travel(next, dests)
		// -----------------------------------------------
		lang.nextStep();
		src.unhighlight(10);
		highlightCodeLine(11);
		descr0.hide();
		descr0.setText(
				"Now the salesman is ready to visit his next destination.",
				null, null);
		descr0.show();
		descr1.setText(
				"The costs for his further travels are added to his current costs.",
				null, null);
		descr1.show();

		lang.nextStep();
		src.unhighlight(11);
		descr0.hide();
		descr1.hide();
		travel(nextDest, dests);
	}

	private int greedyBestFirst(Node start, Node dest) {
		int pathCosts = 0;
		String currentLabel = graph.getNodeLabel(start);
		String destLabel = graph.getNodeLabel(dest);

		highlightCodeLine(13);

		descr0.setText("Call greedy-best-first(Node '" + currentLabel
				+ "', Destination '" + destLabel + "').", null, null);
		descr0.show();

		// -----------------------------------------------
		// if start = dest
		// -----------------------------------------------
		lang.nextStep();
		descr0.hide();
		src.unhighlight(13);
		highlightCodeLine(14);

		descr0.setText("Is the salesman already at his current destination '"
				+ destLabel + "'?", null, null);
		descr0.show();

		lang.nextStep();

		if (currentLabel.equals(destLabel)) {
			descr1.setText("Yes! The salesman already is at place '"
					+ destLabel + "'.", null, null);
			descr1.show();

			// -----------------------------------------------
			// return 0
			// -----------------------------------------------
			lang.nextStep();
			descr0.hide();
			descr1.hide();
			src.unhighlight(14);
			highlightCodeLine(15);
			descr0.setText(
					"Return 0, as the salesman doesn't have to travel any further",
					null, null);
			descr0.show();
			descr1.setText("to reach his current destination '" + destLabel
					+ "'.", null, null);
			descr1.show();

			lang.nextStep();
			descr0.hide();
			descr1.hide();
			src.unhighlight(15);

			return 0;
		}

		descr1.setText("No! The salesman has not yet reached the destination.",
				null, null);
		descr1.show();

		// -----------------------------------------------
		// next := probably-best(start, dest)
		// -----------------------------------------------
		lang.nextStep();
		descr0.hide();
		descr1.hide();
		src.unhighlight(14);
		highlightCodeLine(16);
		Node next = start;

		int startIndex = graph.getPositionForNode(start);
		int destIndex = graph.getPositionForNode(dest);

		double min = Integer.MAX_VALUE;
		int w, n = 0;
		double h, v;

		LinkedList<Integer> neighbors = new LinkedList<Integer>();

		int[] edges = graph.getEdgesForNode(start);

		for (int i = 0; i < edges.length; i++) {
			if (edges[i] != 0) {
				neighbors.add(i);
			}
		}

		for (int i = 0; i < neighbors.size(); i++) {
			n = neighbors.get(i);
			h = heuristics[n][destIndex];
			w = graph.getEdgeWeight(startIndex, n);
			v = w + h;

			if (v < min) {
				min = v;
				next = graph.getNodeForIndex(n);
			}
		}

		String nextLabel = graph.getNodeLabel(next);

		descr0.setText(
				"According to the chosen heuristics (straight-line distance), the best way",
				null, null);
		descr0.show();
		descr1.setText("to reach place '" + destLabel
				+ "' is via neighbor place '" + nextLabel + "'.", null, null);
		descr1.show();

		// go to node "next"
		graph.unhighlightNode(start, null, null);
		graph.highlightNode(next, null, null);

		int nodePos = graph.getPositionForNode(next);

		timesVisited[nodePos]++;

		// check for possible loop
		if (timesVisited[nodePos] > 3) {
			loop = true;
			lang.nextStep();
			descr0.hide();
			descr1.hide();
			return Integer.MAX_VALUE;
		}

		dirGraph.showEdge(start, next, null, moveDuration);
		dirGraph.highlightEdge(start, next, null, moveDuration);

		// update costs
		pathCosts = graph.getEdgeWeight(start, next);
		currentCosts += pathCosts;

		// -----------------------------------------------
		// return costs(start, next) + greedy-best-first(next, dest)
		// -----------------------------------------------
		lang.nextStep();
		src.unhighlight(16);
		highlightCodeLine(17);
		descr0.hide();
		descr1.hide();
		descr0.setText(
				"Sum up costs for the last edge and the remaining distance.",
				null, null);
		descr0.show();

		lang.nextStep();
		src.unhighlight(17);
		descr0.hide();

		return pathCosts + greedyBestFirst(next, dest);
	}

	private void adjustMatrix() {
		int[][] matrix = graph.getAdjacencyMatrix();

		for (int i = 1; i < matrix.length; i++) {
			for (int j = 0; j < i; j++) {
				matrix[i][j] = matrix[j][i];
			}
		}

		graph.setAdjacencyMatrix(matrix);
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
	private String getAltString(LinkedList<Node> startAlts) {
		if (startAlts.isEmpty())
			return "";

		Node nextNode = startAlts.removeFirst();
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

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		textProps = (TextProperties) props.getPropertiesByName("text");
		headerProps = (TextProperties) props.getPropertiesByName("header");
		srcProps = (SourceCodeProperties) props
				.getPropertiesByName("sourceCode");
		graphProps = (GraphProperties) props
				.getPropertiesByName("graphProperties");
		destinations = (String[]) primitives.get("destinations");

		// -----------------------------------------------
		// Get User defined Node Colors
		// -----------------------------------------------
		routeColor = (Color) primitives.get("routeColor");
		startNodeColor = (Color) primitives.get("startNodeColor");
		destNodeColor = (Color) primitives.get("destNodeColor");

		askQuestions = (Boolean) primitives.get("askQuestions");
		graph = (Graph) primitives.get("graph");

		// set graph depth
		graphProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, graphDepth);

		graph = lang.addGraph(graph, null, graphProps);
		graph.hide();

		start();

		lang.finalizeGeneration();
		return lang.toString();
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		String[] dests = (String[]) primitives.get("destinations");
		Graph g = (Graph) primitives.get("graph");
		g.hide();
		int graphSize = g.getSize();
		Node dest;
		String d;
		String start = g.getNodeLabel(g.getStartNode());
		LinkedList<String> enteredDests = new LinkedList<String>();
		LinkedList<Node> destNodes = new LinkedList<Node>();

		for (int i = 0; i < dests.length; i++) {
			enteredDests.add(dests[i]);
		}

		for (int i = 0; i < graphSize; i++) {
			dest = g.getNodeForIndex(i);
			d = g.getNodeLabel(dest);

			if (enteredDests.contains(d) && !d.equals(start)
					&& !destNodes.contains(dest)) {
				destNodes.add(dest);
			}
		}

		int destNodesSize = destNodes.size();

		if (destNodesSize == 0) {
			showErrorWindow("No valid destination nodes entered.\n");
			return false;
		}

		return true; // no error found
	}

	private void showErrorWindow(String message) {
		JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), message,
				"Error", JOptionPane.ERROR_MESSAGE);
	}

	public String getName() {
		return "Traveling Salesman Problem per Greedy Best-First Search [EN]";
	}

	public String getAlgorithmName() {
		return "Traveling Salesman Problem per Greedy Best-First Search";
	}

	public String getAnimationAuthor() {
		return "Carina Oberle";
	}

	public String getDescription() {
		return "The traveling salesman problem (TSP) is a famous combinatorial optimization problem."
				+ "\n"
				+ "It consists of a salesperson that has to visit a list of cities and then travel back to his starting place."
				+ "\n"
				+ "The aim is to find a route with the least possible costs."
				+ "\n"
				+ "TSP is NP-hard (Non-deterministic Polynomial-time hard), which means it is 'at least as hard "
				+ "\n"
				+ "as the hardest problems in complexity class NP' (see <a>http://en.wikipedia.org/wiki/NP-hard</a>)."
				+ "\n"
				+ "In this animation we use Greedy Best-First Search with straight-line distance as heuristics"
				+ "\n"
				+ "to get an approximative solution to an instance of this problem.";
	}

	public String getCodeExample() {
		return "<b>function</b> init(n)"
				+ "\n"
				+ "    startNode := n"
				+ "\n"
				+ "    costs := 0"
				+ "\n\n"
				+ "<b>function</b> travel(currentNode, dests)"
				+ "\n"
				+ "    <b>if</b> dests is empty"
				+ "\n"
				+ "        costs += greedy-best-first(currentNode, startNode)"
				+ "\n"
				+ "        <b>return</b>"
				+ "\n"
				+ "    nextDest := probably-best(currentNode, dests)"
				+ "\n"
				+ "    costs += greedy-best-first(currentNode, nextDest)"
				+ "\n"
				+ "    dests.remove(nextDest)"
				+ "\n"
				+ "    travel(nextDest, dests)"
				+ "\n\n"
				+ "<b>function</b> greedy-best-first(start, dest)"
				+ "\n"
				+ "    <b>if</b> start = dest"
				+ "\n"
				+ "        <b>return</b> 0"
				+ "\n"
				+ "    nextNode := probably-best(start, dest)"
				+ "\n"
				+ "    <b>return</b> costs(start, nextNode) + greedy-best-first(nextNode, dest)"
				+ "\n";
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