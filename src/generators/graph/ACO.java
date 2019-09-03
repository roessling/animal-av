/*
 * ACO.java
 * Laura Hofmann, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.graph;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;
import java.util.Random;

import algoanim.primitives.DoubleArray;
import algoanim.primitives.DoubleMatrix;
import algoanim.primitives.Ellipse;
import algoanim.primitives.Graph;
import algoanim.primitives.IntArray;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.TrueFalseQuestionModel;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import animal.main.Animal;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.EllipseProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.SourceCodeProperties;

@SuppressWarnings("unused")
public class ACO implements ValidatingGenerator {
	private Language lang;
	private TextProperties textProps;
	private ArrayProperties arrayProps;
	private double Q;
	private double pr;
	private int maxIterations;
	private MatrixProperties matrixProps;
	private EllipseProperties ellipseProps;
	private int alpha;
	private SourceCodeProperties scProps;
	private double evaporation;
	private double numAntFactor;
	private double beta;

	private int c = 1;
	public int numberOfTowns = 0; // # towns
	public int numberOfAnts = 0; // # ants
	private int graph[][] = null;
	private double trails[][] = null;
	private Ant ants[] = null;
	private Random rand = new Random();
	private double probs[] = null;

	private int currentIndex = 0;

	// things for visualisation:
	private Graph g;
	private GraphProperties graphProps;
	private int iteration;
	private Text iterationText;
	private DoubleMatrix trailsDisplay;
	private StringArray antsDisplay;
	private DoubleArray probsDisplay;
	private IntArray tourDisplay;
	private StringArray visitedDisplay;
	private Text contributionText;
	private Text bestTourText;
	private Text bestTourLengthText;
	private Text tourText;
	private Polyline line1;
	private Polyline line2;
	private Polyline line3;
	private Polyline line4;
	private Polyline line5;
	private Polyline line6;

	public int[] bestTour;
	public double bestTourLength;
	
	// things for header
	private Text header;
	private Text subheader;
	private Rect headerRect;
	private Rect shadowRect;
	private SourceCode mainCode;
	private Rect codeRect;

	public void init() {
		lang = new AnimalScript("Ant Colony Optimisation for TSP", "Laura Hofmann", 800, 600);
		this.lang.setStepMode(true);
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		textProps = (TextProperties) props.getPropertiesByName("textProps");
		arrayProps = (ArrayProperties) props.getPropertiesByName("arrayProps");
		Q = (double) primitives.get("Q");
		pr = (double) primitives.get("pr");
		maxIterations = (Integer) primitives.get("maxIterations");
		matrixProps = (MatrixProperties) props.getPropertiesByName("matrixProps");
		ellipseProps = (EllipseProperties) props.getPropertiesByName("ellipseProps");
		alpha = (Integer) primitives.get("alpha");
		scProps = (SourceCodeProperties) props.getPropertiesByName("scProps");
		evaporation = (double) primitives.get("evaporation");
		numAntFactor = (double) primitives.get("numAntFactor");
		beta = (double) primitives.get("beta");

		// enable questions
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
		// header to be visible at all times
		// show the header with subheader
		TextProperties headerProps = new TextProperties();
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 24));
		header = lang.newText(new Coordinates(20, 30), "Ant Colony Optimisation Algorithmus", "header", null,
				headerProps);
		TextProperties subheaderProps = new TextProperties();
		subheaderProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 14));
		subheader = lang.newText(new Offset(0, 5, "header", "SW"),
				"as by M. Dorigo, The Ant System: Optimization by a colony of cooperating agents", "subheader", null,
				subheaderProps);
		// surround with blue rectangle with shadow
		RectProperties rectProps = new RectProperties();
		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.decode("0x00c8ff"));
		rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		headerRect = lang.newRect(new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW),
				new Offset(5, 5, "subheader", "SE"), "hRect", null, rectProps);
		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.lightGray);
		rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
		shadowRect = lang.newRect(new Offset(5, 5, "header", AnimalScript.DIRECTION_NW),
				new Offset(5, 5, "hRect", "SE"), "hRect", null, rectProps);
		lang.nextStep("setup: show title");

		intro();
		makeGraph();
		solve();

		// finalise questions
		lang.finalizeGeneration();

		return lang.toString();
	}

	public String getName() {
		return "Ant Colony Optimisation for TSP";
	}

	public String getAlgorithmName() {
		return "ACO for TSP";
	}

	public String getAnimationAuthor() {
		return "Laura Hofmann";
	}

	public String getDescription() {
		return "Ant Colony Optimisation (ACO is inspired by the way ants find the shortest path from their nest to a food source. They use odour trails of pheromones: each ant leaves a trail and follows a strong existing trail when coming across it. Thus making it even stronger."
				+ "\n" + "\n"
				+ "ACO uses this technique to find the shortest path in a graph. Ants walk along the edges at first randomly and after a time following attractive edges (these where most other ants have traveled before). The trail left by an ant evaporates  over time preventing premature conversion or suboptimal solutions. "
				+ "\n" + "\n" + "Trough this method all ants will eventually walk along the same path.";
	}

	public String getCodeExample() {
		return "make or get graph" + "\n" + "allocate memory for all that is needed" + "\n"
				+ "for (t=0 to maxIterations){" + "\n" + "    setup ants and place them into random cities;" + "\n"
				+ "    for each ant {" + "\n" + "        while(ant has unvisited towns){" + "\n"
				+ "            pick next town for ant:" + "\n" + "                either: pick random unvisited town"
				+ "\n" + "                or: calculate probabilities of towns and pick the most attractive" + "\n"
				+ "            move ant\", \"10\", 3, null); // 10" + "\n" + "        }" + "\n" + "    }" + "\n"
				+ "    age trails (multiply with evaporation factor)" + "\n" + "    for each ant{" + "\n"
				+ "        calculate its contribution to the paths" + "\n"
				+ "        add the contribution to the pathes the ant walked" + "\n" + "    }" + "\n"
				+ "    compate routes of each ant (and old best route) and find best one" + "\n" + "}" + "\n"
				+ "return best route and its length";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.ENGLISH;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPH);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}
	
	
	//    my code                     ///////////////////
	
	private void intro() {
//		// write intro text
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 16));
		lang.newText(new Coordinates(10, 100),
				"Ant Colony Optimisation (ACO) after M. Dorigo (The Ant System: Optimization by a colony of",
				"description1", null, textProps);
		lang.newText(new Offset(0, 25, "description1", AnimalScript.DIRECTION_NW),
				"cooperating agents) is inspired by the way ants find the shortest path", "description2", null,
				textProps);
		lang.newText(new Offset(0, 25, "description2", AnimalScript.DIRECTION_NW),
				"from their nest to a food source. They use odour trails of pheromones: each ant leaves a trail",
				"description3", null, textProps);
		lang.newText(new Offset(0, 25, "description3", AnimalScript.DIRECTION_NW),
				"and follows a strong existing trail when coming across it. Thus making it even stronger.",
				"description4", null, textProps);
		lang.newText(new Offset(0, 25, "description4", AnimalScript.DIRECTION_NW), "", "description5", null, textProps);
		lang.newText(new Offset(0, 25, "description5", AnimalScript.DIRECTION_NW),
				"ACO uses this technique to find the shortest path in a graph. Ants walk along the edges",
				"description6", null, textProps);
		lang.newText(new Offset(0, 25, "description6", AnimalScript.DIRECTION_NW),
				"of the graph. At first randomly and after a time following attractive edges (these where most",
				"description7", null, textProps);
		lang.newText(new Offset(0, 25, "description7", AnimalScript.DIRECTION_NW),
				"other ants have traveled before). The trail left by an ant evaporates over", "description8", null,
				textProps);
		lang.newText(new Offset(0, 25, "description8", AnimalScript.DIRECTION_NW),
				"time preventing premature conversion or suboptimal solutions. ", "description9", null, textProps);
		lang.newText(new Offset(0, 25, "description9", AnimalScript.DIRECTION_NW),
				"Trough this method all ants will eventually walk along the same and optimal path. ", "description10",
				null, textProps);
		lang.newText(new Offset(0, 25, "description10", AnimalScript.DIRECTION_NW), "", "description11", null,
				textProps);
		lang.newText(new Offset(0, 25, "description11", AnimalScript.DIRECTION_NW),
				"For readability and animating reasons I can only allow one fixed graph.", "description11", null,
				textProps);
		lang.nextStep("intro text");

		lang.newText(new Coordinates(900, 30), "This is a pseudocode description of the algorithm:", "pseudoCDesc",
				null, textProps);

		// Create SourceCode
		// first, set the visual properties for the source code
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 15));

		mainCode = lang.newSourceCode(new Offset(10, 20, "pseudoCDesc", AnimalScript.DIRECTION_NW), "sourceCode", null,
				scProps);

		// Add the lines to the SourceCode object:(Line_Text, name, indentation,
		// display, delay)
		mainCode.addCodeLine("make or get graph", "1", 0, null); // 1
		mainCode.addCodeLine("allocate memory for all that is needed", "2", 0, null); // 2
		mainCode.addCodeLine("for (t=0 to maxIterations){", "3", 0, null); // 3
		mainCode.addCodeLine("setup ants and place them into random cities;", "4", 1, null); // 4
		mainCode.addCodeLine("for each ant {", "5", 1, null);// 5
		mainCode.addCodeLine("while(ant has unvisited towns){", "6", 2, null);// 6
		mainCode.addCodeLine("pick next town for ant:", "7", 3, null);// 7
		mainCode.addCodeLine("either: pick random unvisited town", "8", 4, null);// 8
		mainCode.addCodeLine("or: calculate probabilities of towns and pick the most attractive", "9", 4, null);// 9
		mainCode.addCodeLine("move ant", "10", 3, null); // 10
		mainCode.addCodeLine("}", "11", 2, null);// 11
		mainCode.addCodeLine("}", "12", 1, null);// 12
		mainCode.addCodeLine("age trails (multiply with evaporation factor)", "13", 1, null);// 13
		mainCode.addCodeLine("for each ant{", "14", 1, null);// 14
		mainCode.addCodeLine("calculate its contribution to the paths", "15", 2, null);// 15
		mainCode.addCodeLine("add the contribution to the pathes the ant walked", "16", 2, null);// 16
		mainCode.addCodeLine("}", "17", 1, null);// 17
		mainCode.addCodeLine("compate routes of each ant (and old best route) and find best one", "18", 1, null);// 18
		mainCode.addCodeLine("}", "19", 0, null);// 19
		mainCode.addCodeLine("return best route and its length", "20", 0, null);// 20

		// make blue square behind main code
		RectProperties rectProps = new RectProperties();
		// make blue square behind main code
		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.decode("0xb2eeff"));
		rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		codeRect = lang.newRect(new Offset(-5, -5, "sourceCode", AnimalScript.DIRECTION_NW),
				new Offset(40, 5, "sourceCode", "SE"), "hRect", null, rectProps);

		lang.nextStep("intro: show pseudo code");

		// cleanup for next step
		lang.hideAllPrimitives();
		header.show();
		subheader.show();
		headerRect.show();
		shadowRect.show();
		mainCode.show();
		codeRect.show();
	}

	// Ant class. Maintains tour and tabu list information.
	private class Ant {
		public int tour[] = new int[graph.length];
		// Maintain visited list for towns, much faster
		// than checking if in tour so far.
		public boolean visited[] = new boolean[graph.length];
		public String visitedString[] = new String[graph.length];// for display in animation
		public Ellipse ellipse;

		public Ant(int i) {
			ellipseProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
			ellipse = lang.newEllipse(new Coordinates(20, 600 + i * 10), new Coordinates(4, 7), "Ant" + i, null,
					ellipseProps);
		}

		// to animate
		public void visitTown(int town) {
			tour[currentIndex + 1] = town;
			tourDisplay.put(currentIndex + 1, town, null, null);// display it
			tourDisplay.highlightCell(currentIndex + 1, null, null);
			visited[town] = true;
			visitedString[town] = "true"; // for display reasons
			visitedDisplay.put(town, "true", null, null);// display it
			visitedDisplay.highlightCell(town, null, null);
			g.highlightNode(town, null, null);
			lang.nextStep();
			ellipse.moveTo("N", "translate", g.getNode(town), null, null);// nach next step damit die ant erst bewegt
																			// wird wenn move ant im code markiert ist
			tourDisplay.unhighlightCell(currentIndex + 1, null, null);
			visitedDisplay.unhighlightCell(town, null, null);
			g.unhighlightNode(town, null, null);
		}

		public boolean visited(int i) {
			return visited[i];
		}

		public double tourLength() {
			double length = graph[tour[numberOfTowns - 1]][tour[0]];
			for (int i = 0; i < numberOfTowns - 1; i++) {
				length += graph[tour[i]][tour[i + 1]];
			}
			return length;
		}

		// done animating
		public void clearVisited() {
			for (int i = 0; i < numberOfTowns; i++) {
				visited[i] = false;
				visitedString[i] = "false"; // to put ants data later
				visitedDisplay.put(i, "false", null, null); // display it
			}
		}
	}

	// make 7x7 graph with weights ranging from 1 to 10
	// Allocates all memory.
	private void makeGraph() {
		mainCode.toggleHighlight("1");

		// define the edges of the graph
		int rangeMin = 1;
		int rangeMax = 10;

		graph = new int[4][4];
		for (int i = 0; i < graph.length; i++) {
			for (int j = 0; j < graph[i].length; j++) {
				if (i == j)
					continue;
				graph[i][j] = (int) Math.floor(rangeMin + (rangeMax - rangeMin) * rand.nextDouble());
			}
		}

		// define the nodes and their positions
		Node[] graphNodes = new Node[4];
		graphNodes[0] = new Coordinates(40, 120);
		graphNodes[1] = new Coordinates(340, 120);
		graphNodes[2] = new Coordinates(40, 400);
		graphNodes[3] = new Coordinates(340, 400);

		// define the names of the nodes
		String[] labels = { "0", "1", "2", "3" };

		// set the properties
		graphProps = new GraphProperties();
		graphProps.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, false);
		graphProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.white); // Node
		graphProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.decode("0x00c8ff")); // Node Highlight UND
																									// Edge highlight
		graphProps.set(AnimationPropertiesKeys.EDGECOLOR_PROPERTY, Color.gray); // schrift an der Edge
		graphProps.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, true);

		// generate Graph
		g = lang.newGraph("graph", graph, graphNodes, labels, null, graphProps);

		lang.nextStep();

		mainCode.toggleHighlight("2");

		// show all
//		// for arrays
//		ArrayProperties arrayProps = new ArrayProperties();
//		arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
//		arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
//		arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
//		arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
//		arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW);

//		// for text
//		TextProperties textProps = new TextProperties();
//		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 16));

//		// for matrix
//		MatrixProperties matrixProps = new MatrixProperties();
//		matrixProps.set(AnimationPropertiesKeys.GRID_ALIGN_PROPERTY, "center");
//		matrixProps.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");

		// for rects of color table
		RectProperties rectProps = new RectProperties();
		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.decode("0xb2eeff"));

		// for lines of trails in graph
		PolylineProperties lineProps = new PolylineProperties();
		lineProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.decode("0xf8fcae"));
		lineProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

		iterationText = lang.newText(new Coordinates(450, 100), "Nr of Iterations: " + maxIterations, "iteration", null,
				textProps);

		numberOfTowns = graph.length;
    Text noTownsText = lang.newText(new Offset(0, 20, "iteration", "NW"), "Nr of Towns: " + numberOfTowns,
				"noTownsText", null, textProps);

		numberOfAnts = (int) (numberOfTowns * numAntFactor);
		Text noAntsText = lang.newText(new Offset(0, 20, "noTownsText", "NW"), "Nr of Ants: " + numberOfAnts,
				"noAntsText", null, textProps);

		Text evaporationText = lang.newText(new Offset(0, 20, "noAntsText", "NW"), "Evaporation factor: " + evaporation,
				"evaporationText", null, textProps);

		// all memory allocations done here

		bestTourText = lang.newText(new Coordinates(20, 500), "Best Route: ", "bestRouteText", null, textProps);
		bestTourLengthText = lang.newText(new Coordinates(20, 520), "Best Route Length: ", "bestRouteText", null,
				textProps);

		// trails array
		trails = new double[numberOfTowns][numberOfTowns];
		Text trailsDesc = lang.newText(new Coordinates(20, 550), "Trails: ", "trailsDesc", null, textProps);
		trailsDisplay = lang.newDoubleMatrix(new Offset(100, 0, "trailsDesc", AnimalScript.DIRECTION_NW), trails,
				"trailsDisplay", null, matrixProps);

		// ants array
		ants = new Ant[numberOfAnts];
		String[] antsInput = new String[numberOfAnts];
		for (int a = 0; a < antsInput.length; a++)
			antsInput[a] = "Ant" + a;
		for (int j = 0; j < numberOfAnts; j++)
			ants[j] = new Ant(j);
		Text antsDesc = lang.newText(new Offset(0, 50, "evaporationText", AnimalScript.DIRECTION_NW), "Ants: ",
				"antsDesc", null, textProps);
		antsDisplay = lang.newStringArray(new Offset(100, 0, "antsDesc", AnimalScript.DIRECTION_NW), antsInput,
				"antsDisplay", null, arrayProps);

		// tour array (each ant will use this so "empty" for now)
		tourText = lang.newText(new Offset(0, 50, "antsDesc", AnimalScript.DIRECTION_NW), "Tour of current ant: ",
				"tourDesc", null, textProps);
		tourDisplay = lang.newIntArray(new Offset(100, 20, "tourDesc", AnimalScript.DIRECTION_NW), ants[0].tour,
				"tourDisplay", null, arrayProps);

		// visited array (each ant will use this so "empty" for now)
		Text visitedDesc = lang.newText(new Offset(0, 80, "tourDesc", AnimalScript.DIRECTION_NW),
				"Visited of current ant: ", "visitedDesc", null, textProps);
		visitedDisplay = lang.newStringArray(new Offset(100, 20, "visitedDesc", AnimalScript.DIRECTION_NW),
				ants[0].visitedString, "visitedDisplay", null, arrayProps);

		// contribution of ant:
		contributionText = lang.newText(new Offset(0, 80, "visitedDesc", AnimalScript.DIRECTION_NW),
				"Contribution of current ant: ", "contributionDesc", null, textProps);

		// probs array (each ant will use this so "empty" for now)
		probs = new double[numberOfTowns];
		Text probsDesc = lang.newText(new Offset(0, 50, "contributionDesc", AnimalScript.DIRECTION_NW),
				"Probabilities for current ant: ", "probsDesc", null, textProps);
		probsDisplay = lang.newDoubleArray(new Offset(100, 20, "probsDesc", AnimalScript.DIRECTION_NW), probs,
				"probsDisplay", null, arrayProps);

		// farbskala
		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.red);
		lang.newRect(new Coordinates(1600, 60), new Coordinates(1660, 120), "vstrongTrail", null, rectProps);
		lang.newText(new Offset(10, 20, "vstrongTrail", "NE"), "very strong Trail", "vsTrail", null, textProps);

		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.decode("0xff9000"));
		lang.newRect(new Coordinates(1600, 60 + 60 * 1), new Coordinates(1660, 120 + 60 * 1), "strongTrail", null,
				rectProps);
		lang.newText(new Offset(10, 20, "strongTrail", "NE"), "strong Trail", "sTrail", null, textProps);

		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.yellow);
		lang.newRect(new Coordinates(1600, 60 + 60 * 2), new Coordinates(1660, 120 + 60 * 2), "weakTrail", null,
				rectProps);
		lang.newText(new Offset(10, 20, "weakTrail", "NE"), "weak Trail", "wTrail", null, textProps);

		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.decode("0xf8fcae"));
		lang.newRect(new Coordinates(1600, 60 + 60 * 3), new Coordinates(1660, 120 + 60 * 3), "vweakTrail", null,
				rectProps);
		lang.newText(new Offset(10, 20, "vweakTrail", "NE"), "very weak Trail", "vwTrail", null, textProps);

		Node[] lineNodes = new Node[2];
		lineNodes[0] = new Coordinates(40, 120);
		lineNodes[1] = new Coordinates(340, 120);
		line1 = lang.newPolyline(lineNodes, "line1", null, lineProps);
		lineNodes[0] = new Coordinates(40, 120);
		lineNodes[1] = new Coordinates(40, 400);
		line2 = lang.newPolyline(lineNodes, "line2", null, lineProps);
		lineNodes[0] = new Coordinates(40, 400);
		lineNodes[1] = new Coordinates(340, 400);
		line3 = lang.newPolyline(lineNodes, "line3", null, lineProps);
		lineNodes[0] = new Coordinates(340, 120);
		lineNodes[1] = new Coordinates(340, 400);
		line4 = lang.newPolyline(lineNodes, "line4", null, lineProps);
		lineNodes[0] = new Coordinates(40, 400);
		lineNodes[1] = new Coordinates(340, 120);
		line5 = lang.newPolyline(lineNodes, "line5", null, lineProps);
		lineNodes[0] = new Coordinates(40, 120);
		lineNodes[1] = new Coordinates(340, 400);
		line6 = lang.newPolyline(lineNodes, "line6", null, lineProps);

		lang.nextStep("generated all visibles");

		// 1. Frage
		MultipleChoiceQuestionModel whyFaintLines = new MultipleChoiceQuestionModel("faintLines");
		whyFaintLines.setPrompt("What are the faint lines in the graph for?");
		whyFaintLines.addAnswer("no Idea", 0,
				" -> they show the strength of the trail ... see the colors on the right? The stronger the trail the deeper the color");
		whyFaintLines.addAnswer("to visualise the strenght of the trails", 1, " -> correct");
		whyFaintLines.addAnswer("its a bug", 0,
				" -> not right,they show the strength of the trail corrsponding to the color talble on the right");

		lang.addMCQuestion(whyFaintLines);
	}

	// Store in probs array the probability of moving to each town
	// [1] describes how these are calculated.
	// In short: ants like to follow stronger and shorter trails more.
	private void probTo(Ant ant) {
		int i = ant.tour[currentIndex];

		double denom = 0.0;
		for (int l = 0; l < numberOfTowns; l++)
			if (!ant.visited(l))
				denom += Math.pow(trails[i][l], alpha) * Math.pow(1.0 / graph[i][l], beta);

		for (int j = 0; j < numberOfTowns; j++) {
			if (ant.visited(j)) {
				probs[j] = 0.0;
				probsDisplay.put(j, 0.0, null, null);
			} else {
				double numerator = Math.pow(trails[i][j], alpha) * Math.pow(1.0 / graph[i][j], beta);
				probs[j] = numerator / denom;
				probsDisplay.put(j, numerator / denom, null, null);
			}
		}
		lang.nextStep();
	}

	// Given an ant select the next town based on the probabilities
	// we assign to each town. With pr probability chooses
	// totally randomly (taking into account tabu list).
	private int selectNextTown(Ant ant) {
		mainCode.toggleHighlight("7");
		lang.nextStep();
		// sometimes just randomly select
		if (rand.nextDouble() < pr) {
			mainCode.toggleHighlight("8");
			lang.nextStep();
			int t = rand.nextInt(numberOfTowns - currentIndex); // random town
			int j = -1;
			for (int i = 0; i < numberOfTowns; i++) {
				if (!ant.visited(i))
					j++;
				if (j == t)
					return i;
			}

		}
		mainCode.toggleHighlight("9");
		// calculate probabilities for each town (stored in probs)
		probTo(ant);
		// randomly select according to probs
		double r = rand.nextDouble();
		double tot = 0;
		for (int i = 0; i < numberOfTowns; i++) {
			tot += probs[i];
			if (tot >= r)
				return i;
		}

		throw new RuntimeException("Not supposed to get here.");
	}

	// Update trails based on ants tours
	private void updateTrails() {
		// evaporation
		mainCode.toggleHighlight("13");
		for (int i = 0; i < numberOfTowns; i++)
			for (int j = 0; j < numberOfTowns; j++) {
				trails[i][j] *= evaporation;
				trailsDisplay.put(i, j, trails[i][j], null, null);
			}
		lang.nextStep("evaporation computed");
		// 3. Frage
		MultipleChoiceQuestionModel evaporation = new MultipleChoiceQuestionModel("evaporation");
		evaporation.setPrompt("What does aging trails do?");
		evaporation.addAnswer("nothing", 0, " -> this is not really right");
		evaporation.addAnswer(
				"it multiplies the trails left by the ants with the evaporation factor to make sure old trails are not as attractive as newly walked ones",
				1, " -> correct");
		evaporation.addAnswer("counts them up so that old trails get stronger", 0,
				" -> not right, old trails get weaker if they are not walked ");
		lang.addMCQuestion(evaporation);

		// each ants contribution
		for (int i = 0; i < numberOfAnts; i++) {
			mainCode.toggleHighlight("14");
			Ant a = ants[i];
			antsDisplay.highlightCell(i, null, null);
			inputAntsData(i);
			ants[i].ellipse.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, Color.yellow, null, null);
			lang.nextStep();
			mainCode.toggleHighlight("15");
			double contribution = Q / a.tourLength();
			contributionText.setText("Contribution of current ant: " + contribution, null, null);
			lang.nextStep();
			mainCode.toggleHighlight("16");
			double trailWeight;// here
			Polyline line = line1;// only set it cause you must... else ignore :)
			for (int j = 0; j < numberOfTowns - 1; j++) {
				trails[a.tour[j]][a.tour[j + 1]] += contribution;
				trailsDisplay.put(a.tour[j], a.tour[j + 1], trails[a.tour[j]][a.tour[j + 1]], null, null);
				// choose the right line
				if (a.tour[j] == 0 && a.tour[j + 1] == 1 || a.tour[j] == 1 && a.tour[j + 1] == 0)
					line = line1;
				if (a.tour[j] == 0 && a.tour[j + 1] == 2 || a.tour[j] == 2 && a.tour[j + 1] == 0)
					line = line2;
				if (a.tour[j] == 2 && a.tour[j + 1] == 3 || a.tour[j] == 3 && a.tour[j + 1] == 2)
					line = line3;
				if (a.tour[j] == 1 && a.tour[j + 1] == 3 || a.tour[j] == 3 && a.tour[j + 1] == 1)
					line = line4;
				if (a.tour[j] == 2 && a.tour[j + 1] == 1 || a.tour[j] == 1 && a.tour[j + 1] == 2)
					line = line5;
				if (a.tour[j] == 0 && a.tour[j + 1] == 3 || a.tour[j] == 3 && a.tour[j + 1] == 0)
					line = line6;
				trailWeight = trails[a.tour[j]][a.tour[j + 1]];
				if (trailWeight < 30) {
					line.changeColor("color", Color.decode("0xf8fcae"), null, null);
				}
				if (30 <= trailWeight && trailWeight < 60) {
					line.changeColor("color", Color.yellow, null, null);
				}
				if (60 <= trailWeight && trailWeight < 90) {
					line.changeColor("color", Color.decode("0xff9000"), null, null);
				}
				if (90 <= trailWeight) {
					line.changeColor("color", Color.red, null, null);
				}
				//////////////////
			}
			trails[a.tour[numberOfTowns - 1]][a.tour[0]] += contribution;
			trailsDisplay.put(numberOfTowns - 1, a.tour[0], trails[a.tour[numberOfTowns - 1]][a.tour[0]], null, null);
			lang.nextStep("contributions of ant put into trails and graph");
			antsDisplay.unhighlightCell(i, null, null);
			ants[i].ellipse.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, Color.decode("0xb2eeff"), null, null);
			normaliseAllEdges();
		}
	}

	// Choose the next town for all ants
	private void moveAnts() {
		// each ant follows trails...
		while (currentIndex < numberOfTowns - 1) {
			for (int i = 0; i < numberOfAnts; i++) {
				Ant a = ants[i];
				mainCode.toggleHighlight("5");
				antsDisplay.highlightCell(i, null, null);
				ants[i].ellipse.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, Color.yellow, null, null);
				inputAntsData(i);
				lang.nextStep();
				a.visitTown(selectNextTown(a));
				// display leg of journey
				g.highlightEdge(a.tour[currentIndex + 1], a.tour[currentIndex], null, null);
				g.highlightEdge(a.tour[currentIndex], a.tour[currentIndex + 1], null, null);
				mainCode.toggleHighlight("10");
				lang.nextStep("ant moved");
				antsDisplay.unhighlightCell(i, null, null);
				ants[i].ellipse.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, Color.decode("0xb2eeff"), null,
						null);
				normaliseAllEdges();
			}
			currentIndex++;
		}
	}

	// m ants with random start city
	// done animating
	private void setupAnts() {
		mainCode.toggleHighlight("4");
		currentIndex = -1;
		for (int i = 0; i < numberOfAnts; i++) {
			// show current ant
			antsDisplay.highlightCell(i, null, null);
			ants[i].ellipse.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW, null, null);
			inputAntsData(i);
			lang.nextStep("one ant set up");

			ants[i].clearVisited(); // faster than fresh allocations. -> animation in method
			ants[i].visitTown(rand.nextInt(numberOfTowns));// spread ants randomly over towns -> animations in method
			antsDisplay.unhighlightCell(i, null, null);
			ants[i].ellipse.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, Color.decode("0xb2eeff"), null, null);
			normaliseAllEdges();
		}
		currentIndex++;

	}

	private void updateBest() {
		mainCode.toggleHighlight("18");
		if (bestTour == null) {
			bestTour = ants[0].tour;
			bestTourText.setText("Best tour: " + tourToString(ants[0].tour), null, null);// display
			bestTourLength = ants[0].tourLength();
			bestTourLengthText.setText("Best tour length: " + ants[0].tourLength(), null, null);// display
		}
		for (int i = 0; i < numberOfAnts; i++) {
			Ant a = ants[i];
			antsDisplay.highlightCell(i, null, null);
			inputAntsData(i);
			ants[i].ellipse.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, Color.yellow, null, null);
			tourText.setText("Tour of current ant: (Length: " + a.tourLength() + ")", null, null);
			lang.nextStep();
			if (a.tourLength() < bestTourLength) {
				bestTourLength = a.tourLength();
				bestTourText.setText("Best tour: " + tourToString(ants[i].tour), null, null);// display
				bestTour = a.tour.clone();
				bestTourLengthText.setText("Best tour length: " + ants[i].tourLength(), null, null);// display
				lang.nextStep("best tour updated");
			}
			antsDisplay.unhighlightCell(i, null, null);
			ants[i].ellipse.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, Color.decode("0xb2eeff"), null, null);
			normaliseAllEdges();
		}
	}

	public static String tourToString(int tour[]) {
		String t = new String();
		for (int i : tour)
			t = t + " " + i;
		return t;
	}

	public int[] solve() {
		// clear trails
		for (int i = 0; i < numberOfTowns; i++)
			for (int j = 0; j < numberOfTowns; j++)
				trails[i][j] = c;

		iteration = 0;
		// run for maxIterations
		// preserve best tour
		while (iteration < maxIterations) {
			mainCode.toggleHighlight("3");
			iterationText.setText("Nr of Iterations: " + maxIterations + ". We are in iteration: " + iteration, null,
					null);
			lang.nextStep();
			setupAnts();
			// 2.Frage
			TrueFalseQuestionModel ellipseAsAnt = new TrueFalseQuestionModel("ellipse");
			ellipseAsAnt.setPrompt("The ellipses represent the ants?");
			ellipseAsAnt.setPointsPossible(1);
			ellipseAsAnt.setCorrectAnswer(true);
			lang.addTFQuestion(ellipseAsAnt);

			moveAnts();
			updateTrails();
			updateBest();
			iteration++;
			// System.out.println("Best tour length: " + (bestTourLength - numberOfTowns));
			// System.out.println("Best tour:" + tourToString(bestTour));
		}
		// // Subtract n because we added one to edges on load
		// System.out.println("///////////////////////////////////////////////////finished");
		// System.out.println("Best tour length: " + (bestTourLength - numberOfTowns));
		// System.out.println("Best tour:" + tourToString(bestTour));
		finish();
		return bestTour.clone();
	}

	private void finish() {
		// show result
		mainCode.toggleHighlight("20");
		lang.hideAllPrimitives();
		header.show();
		subheader.show();
		headerRect.show();
		shadowRect.show();
		mainCode.show();
		codeRect.show();
		bestTourText.show();
		bestTourLengthText.show();
		g.show();
		lang.nextStep();

		// finish text
		lang.hideAllPrimitives();
		header.show();
		subheader.show();
		headerRect.show();
		shadowRect.show();
		TextProperties textProps = new TextProperties();
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 16));

		lang.newText(new Coordinates(10, 100),
				"Ant Colony Optimisation (ACO) after M. Dorigo (The Ant System: Optimization by a colony of cooperating agents)",
				"description1", null, textProps);
		lang.newText(new Offset(0, 25, "description1", AnimalScript.DIRECTION_NW),
				"is not an easy algorithm that provided several chalenges during animation due to its complexity. I have for example",
				"description2", null, textProps);
		lang.newText(new Offset(0, 25, "description2", AnimalScript.DIRECTION_NW),
				"only allowed few iterations of the algorithm on a limited graph here as more than that prooved to be to much for the",
				"description3", null, textProps);
		lang.newText(new Offset(0, 25, "description3", AnimalScript.DIRECTION_NW),
				"animal framework. In reality the best path is usually found after close to 500 iterations.",
				"description4", null, textProps);
		lang.newText(new Offset(0, 25, "description4", AnimalScript.DIRECTION_NW),
				"Should you be interested in the formulas used to compute the different datas in this algorithm I strongly recomend ",
				"description5", null, textProps);
		lang.newText(new Offset(0, 25, "description5", AnimalScript.DIRECTION_NW),
				"reading the paper by M. Dorigo. I found it at http://www.cs.uleth.ca/~benkoczi/OR/read/ant-tsp-01.pdf or you could ",
				"description6", null, textProps);
		lang.newText(new Offset(0, 25, "description6", AnimalScript.DIRECTION_NW),
				"google for:  M. Dorigo, The Ant System: Optimization by a colony of cooperating agents.",
				"description7", null, textProps);
		lang.newText(new Offset(0, 25, "description7", AnimalScript.DIRECTION_NW),
				"A good java implementation (on which I based this animation) can be found here: https://github.com/lukedodd/ant-tsp.",
				"description8", null, textProps);
		lang.newText(new Offset(0, 25, "description8", AnimalScript.DIRECTION_NW),
				"Please beware that there are some things (especially with the infinite loop over solve!) that I canged to make it work for me here.",
				"description9", null, textProps);
		lang.newText(new Offset(0, 25, "description9", AnimalScript.DIRECTION_NW), " ", "description10", null,
				textProps);
		lang.newText(new Offset(0, 25, "description10", AnimalScript.DIRECTION_NW), "Thank you.", "description11", null,
				textProps);
		lang.nextStep("all done!");

	}

	private void inputAntsData(int i) {
		// visited array
		for (int c = 0; c < ants[i].visitedString.length; c++)
			visitedDisplay.put(c, ants[i].visitedString[c], null, null);
		// tour array
		for (int c = 0; c < ants[i].tour.length; c++)
			tourDisplay.put(c, ants[i].tour[c], null, null);

		// highlight tour on graph
		for (int c = 1; c <= currentIndex; c++) {
			g.highlightEdge(ants[i].tour[c - 1], ants[i].tour[c], null, null);
			g.highlightEdge(ants[i].tour[c], ants[i].tour[c - 1], null, null);
		}
	}

	private void normaliseAllEdges() {
		for (int i = 0; i < g.getSize(); i++) {
			for (int j = 0; j < g.getSize(); j++) {
				g.unhighlightEdge(i, j, null, null);
				g.unhighlightEdge(j, i, null, null);
			}
		}
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		Q = (double) primitives.get("Q");
		if(Q<100 ||Q > 700) return false;
		
		pr = (double) primitives.get("pr");
		if(pr<0 ||pr > 1) return false;
		
		maxIterations = (Integer) primitives.get("maxIterations");
		if(maxIterations<1 ||maxIterations > 50) return false;
		
		alpha = (Integer) primitives.get("alpha");
		if(alpha<1 ||alpha > 5) return false;		
		
		evaporation = (double) primitives.get("evaporation");
		if(evaporation<= 0 ||evaporation >1) return false;
		
		numAntFactor = (double) primitives.get("numAntFactor");
		if(numAntFactor<= 0 ||numAntFactor >1) return false;
		
		beta = (double) primitives.get("beta");
		if(beta< 1 ||beta >10) return false;
		
		return true;
	}

}