package generators.searching.alphabeta;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.searching.helpers.Node;
import generators.searching.helpers.TreeParser;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.TrueFalseQuestionModel;

import java.awt.Color;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Random;

import javax.swing.JOptionPane;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.Triangle;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.TriangleProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

/**
 * @author Andrej Felde (andrej.felde@stud.tu-darmstadt.de)
 * @author Thomas Hesse (thomas.hesse@stud.tu-darmstadt.de)
 * @version 2.0
 * @since 2013
 */
public class AlphaBeta implements ValidatingGenerator {


	// @formatter:off

	private static final String DESCRIPTION = "Der Alpha-Beta-Algorithmus "
			+ "(Alpha-Beta-Pruning) basiert auf dem MiniMax-Algorithmus oder auch MinMax-Algorithmus und "
			+ "bestimmt innerhalb eines Spielbaums einen optimalen Zug. Zum "
			+ "Auffinden eines optimalen Spielzugs werden zwei Grenzen alpha "
			+ "(untere Grenze) und beta (obere Grenze) im Baum weitergereicht. "
			+ "Der Alpha-Wert zeigt dem Max-Spieler an welchen Wert dieser "
			+ "minimal erreichen kann. Der Beta-Wert zeigt dem Max-Spieler an "
			+ "welchen Wert er maximal errreichen kann. Durch diese Eingrenzung "
			+ "werden bestimmte Teilbäume abgeschnitten (pruned), da diese das "
			+ "Ergebnis nicht beeinflussen. Das äußert sich darin, dass zum "
			+ "Beispiel bei einem Schachspiel die abgeschnittenen (pruned) "
			+ "Sub-Bäume nicht zum Sieg im Spiel beigetragen hätten. Durch das "
			+ "Abschneiden erhält man ein Performanz Zugewinn.";

	private static final String SOURCE_CODE = "public int run(Node state, int alpha, int beta) {" // 0
			+ "\n  if (state.isTerminal()) {" // 1
			+ "\n    return state.getValue();" // 2
			+ "\n  }" // 3
			+ "\n  if (state.isMax()) {" // 4
			+ "\n    for (Node childState : state.getChildren()) {" // 5
			+ "\n      alpha = Math.max(alpha, run(childState, alpha, beta));" // 6
			+ "\n      if (alpha >= beta) {" // 7
			+ "\n        break; // Beta-Cutoff" // 8
			+ "\n      }" // 9
			+ "\n    }" // 10
			+ "\n    return alpha;" // 11
			+ "\n  } else {" // 12
			+ "\n    for (Node childState : state.getChildren()) {" // 13
			+ "\n      beta = Math.min(beta, run(childState, alpha, beta));" // 14
			+ "\n      if (beta <= alpha) {" // 15
			+ "\n        break; // Alpha-Cutoff" // 16
			+ "\n      }" // 17
			+ "\n    }" // 18
			+ "\n    return beta;" // 19
			+ "\n  }" // 20
			+ "\n}"; // 21

	// @formatter:on

	private Language					lang;

	private SourceCode					sc;

	/**
	 * Contains mapping between internal representation and the animal
	 * primitives.
	 */
	private HashMap<String, Primitive>	pMap;

	/**
	 * Contains mapping for different explanations.
	 */
	private HashMap<String, String>		eMap;

	private Node						preState;

	private Node						root;

	private int							currentLine;

	private int							nodeSize;

	private int							leafXPadding;

	private int							leafYPadding;

	private int							result;

	private Random						rnd;

	private int							expandedNodes;

	/**
	 * Extra distance from the source code
	 */
	private int							sourceCodeDistance;

	/*****************************
	 * GENERATED PROPERTY VALUES *
	 ****************************/

	private String						textualTree;

	private Color						treeHighlightColor;

	private Text						explanationText;

	private TextProperties				titleTextProp;

	private SourceCodeProperties		scProps;

	private Color						borderHighlightColor;

	private Color						cutlineTextColor;

	private Color						visitedNodeColor;

	private Color						nodeHighlightColor;

	private Color						nodeFillColor;

	/**
	 * 0-100
	 */
	private int							possibilityQuestion;

	private int							questionCounter;

	private int							labelLeaf		= 0;

	private int							labelAlphaCut	= 0;

	private int							labelBetaCut	= 0;

	private int							leftBorder;

	public AlphaBeta() {
	}

	public String getName() {
		return "Alpha-Beta Suche";
	}

	public String getDescription() {
		return DESCRIPTION;
	}

	public String getCodeExample() {
		return SOURCE_CODE;
	}

	@Override
	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		init();

		titleTextProp = (TextProperties) props.getPropertiesByName("titleTextProp");
		borderHighlightColor = (Color) primitives.get("borderHighlightColor");
		textualTree = (String) primitives.get("Tree");
		possibilityQuestion = (Integer) primitives.get("possibilityQuestions (0-100)");
		treeHighlightColor = (Color) primitives.get("treeHighlightColor");
		nodeFillColor = (Color) primitives.get("nodeFillColor");
		cutlineTextColor = (Color) primitives.get("cutlineTextColor");
		scProps = (SourceCodeProperties) props.getPropertiesByName("scProps");
		visitedNodeColor = (Color) primitives.get("visitedNodeColor");
		nodeHighlightColor = (Color) primitives.get("nodeHighlightColor");

		start();
		run();
		end();

		lang.finalizeGeneration();
		return lang.toString();
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		String errorMessage = "";
		boolean error = false;
		String tree = (String) primitives.get("Tree");
		TreeParser p = new TreeParser();
		p.parseText(tree);
		if (!p.isValid()) {
			errorMessage += "Fehler beim Parsen des Baumes.\n" + tree;
			error = true;
		}
		if ((Integer) primitives.get("possibilityQuestions (0-100)") > 100
				|| (Integer) primitives.get("possibilityQuestions (0-100)") < 0) {
			if (error)
				errorMessage += "\n\n";
			else
				error = true;
			errorMessage += "Der Wert von possibilityQuestions (0-100) muss zwischen 0 und 100 liegen.";
		}
		if (error) {
			showErrorWindow(errorMessage);
		}
		return !error; // no error found
	}

	private void showErrorWindow(String message) {
		JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), message, "Fehler", JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public String getAlgorithmName() {
		return "Alpha-Beta-Suche";
	}

	@Override
	public String getAnimationAuthor() {
		return "Andrej Felde, Thomas Hesse";
	}

	@Override
	public Locale getContentLocale() {
		return Locale.GERMANY;
	}

	@Override
	public String getFileExtension() {
		return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_SEARCH);
	}

	@Override
	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

	@Override
	public void init() {
		lang = new AnimalScript("Alpha-Beta Suche", "Andrej Felde, Thomas Hesse", 800, 600);
		lang.setStepMode(true);
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

		pMap = new HashMap<String, Primitive>();
		eMap = new HashMap<String, String>();
		eMap.put("clear", "");
		eMap.put("start", "Der Algorithmus wird aufgerufen mit: run(root, -∞, ∞)");
		eMap.put("isMax", "Überprüfe ob der expandierte Knoten ein Max Knoten (Max Spieler) ist.");
		eMap.put("isMin", "Der expandierte Knoten ist kein Max Knoten, er ist daher ein Min Knoten (Min Spieler).");
		eMap.put("leaf", "Der expandierte Knoten ist ein Blatt Knoten.");
		eMap.put("expand", "Expandiere neuen Knoten.");
		eMap.put("a-update", "Die untere Grenze wird angepasst.");
		eMap.put("b-update", "Die obere Grenze wird angepasst.");
		eMap.put("a-cut", "Nun wird ein Alpha Cut-off auf den unbesuchten Teilbäumen ausgeführt.");
		eMap.put("b-cut", "Nun wird ein Beta Cut-off auf den unbesuchten Teilbäumen ausgeführt.");
		eMap.put("finish", "Der Algorithmus terminiert mit dem Alpha Wert als Ergebnis.");
		preState = null;
		currentLine = 0;
		nodeSize = 40;
		leafXPadding = 25;
		leafYPadding = 60;
		expandedNodes = 0;
		questionCounter = 0;
		sourceCodeDistance = 15;
		leftBorder = 0;
		rnd = new Random();
	}

	protected String getAlgorithmDescription() {
		return DESCRIPTION;
	}

	protected String getAlgorithmCode() {
		return SOURCE_CODE;
	}

	protected void start() {
		/* AS: 1 */
		Text title = lang.newText(new Coordinates(20, 26), "Alpha Beta Algorithmus", "header", null, titleTextProp);
		RectProperties rectProperties = new RectProperties();
		rectProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		rectProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		pMap.put("hRect", lang.newRect(new Offset(-5, -5, title, "NW"), new Offset(5, 5, title, "SE"), "hRect", null,
				rectProperties));
		next("Vorspann");

		/* AS: 2 */
		sc = lang.newSourceCode(new Offset(0, 45, title, null), "initT", null, scProps);
		sc.addCodeLine("Der Alpha-Beta-Algorithmus (Alpha-Beta-Pruning) basiert", "initT", 0, null);
		sc.addCodeLine("auf dem MiniMax-Algorithmus oder auch MinMax-Algorithmus und bestimmt innerhalb", "initT", 0,
				null);
		sc.addCodeLine("eines Spielbaums einen optimalen Zug. Zum Auffinden", "initT", 0, null);
		sc.addCodeLine("eines optimalen Spielzugs werden zwei Grenzen", "initT", 0, null);
		sc.addCodeLine("alpha (untere Grenze) und beta (obere Grenze) im Baum weitergereicht.", "initT", 0, null);
		sc.addCodeLine("Der Alpha-Wert zeigt dem Max-Spieler an welchen Wert dieser minimal", "initT", 0, null);
		sc.addCodeLine("erreichen kann. Der Beta-Wert zeigt dem Max-Spieler an", "initT", 0, null);
		sc.addCodeLine("welchen Wert er maximal errreichen kann.", "initT", 0, null);
		sc.addCodeLine("Durch diese Eingrenzung werden bestimmte Teilbäume abgeschnitten (pruned),", "initT", 0, null);
		sc.addCodeLine("da diese das Ergebnis nicht beeinflussen.", "initT", 0, null);
		sc.addCodeLine("Das äußert sich darin, dass zum Beispiel bei einem Schachspiel", "initT", 0, null);
		sc.addCodeLine("die abgeschnittenen (pruned) Sub-Bäume nicht zum Sieg im Spiel", "initT", 0, null);
		sc.addCodeLine("beigetragen hätten. Durch das Abschneiden erhält man einen", "initT", 0, null);
		sc.addCodeLine("Performanz Zugewinn.", "initT", 0, null);

		TriangleProperties nodeProps = new TriangleProperties();
		nodeProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		nodeProps.set(AnimationPropertiesKeys.FILL_PROPERTY, nodeFillColor);

		pMap.put("legendeMaxText", lang.newText(new Offset(0, 100, "initT", "SW"), "Max-Knoten:", "descrMax", null));
		pMap.put("legendeMaxNode", lang.newTriangle(new Offset(30, -15, "descrMax", "E"), new Offset(10, 15,
				"descrMax", "E"), new Offset(50, 15, "descrMax", "E"), "maxNode", null, nodeProps));
		pMap.put("legendeMinText", lang.newText(new Offset(75, 0, "descrMax", "NE"), "Min-Knoten:", "descrMin", null));
		pMap.put("legendeMinNode", lang.newTriangle(new Offset(30, 15, "descrMin", "E"), new Offset(10, -15,
				"descrMin", "E"), new Offset(50, -15, "descrMin", "E"), "minNode", null, nodeProps));

		explanationText = lang.newText(new Offset(0, 50, "descrMax", "SW"), "", "eText", null);

		next();

		/* AS: 3 */
		sc.hide();
		sc = lang.newSourceCode(new Offset(0, 45, title, null), "code", null, scProps);
		sc.addCodeLine("public int run(Node state, int alpha, int beta) {", null, 0, null); // 0
		sc.addCodeLine("if (state.isTerminal())", null, 1, null); // 1
		sc.addCodeLine("return state.getValue();", null, 2, null); // 2
		sc.addCodeLine("if (state.isMax()) {", null, 1, null); // 3
		sc.addCodeLine("for (Node childState : state.getChildren()) {", null, 2, null); // 4
		sc.addCodeLine("alpha = Math.max(alpha, run(childState, alpha, beta));", null, 3, null); // 5
		sc.addCodeLine("if (alpha >= beta)", null, 3, null); // 6
		sc.addCodeLine("break; // Beta-Cutoff", null, 4, null); // 7
		sc.addCodeLine("}", null, 2, null); // 8
		sc.addCodeLine("return alpha;", null, 2, null); // 9
		sc.addCodeLine("} else {", null, 1, null); // 10
		sc.addCodeLine("for (Node childState : state.getChildren()) {", null, 2, null); // 11
		sc.addCodeLine("beta = Math.min(beta, run(childState, alpha, beta));", null, 3, null); // 12
		sc.addCodeLine("if (beta <= alpha)", null, 3, null); // 13
		sc.addCodeLine("break; // Alpha-Cutoff", null, 4, null); // 14
		sc.addCodeLine("}", null, 2, null); // 15
		sc.addCodeLine("return beta;", null, 2, null); // 16
		sc.addCodeLine("}", null, 1, null); // 17
		sc.addCodeLine("}", null, 0, null); // 18

		next();

		/* AS: 4 */
		TreeParser p = new TreeParser();
		root = p.parseText(textualTree);
		drawTree(root);
		sc.highlight(0, 0, false);
		next("Start Algorithmus");
		// run method is triggered after this
	}

	protected void run() {

		if (rnd.nextInt(100) < possibilityQuestion) {
			MultipleChoiceQuestionModel mc = new MultipleChoiceQuestionModel("startAlgorithm" + (questionCounter++));
			mc.setPrompt("Mit welchen Werten wird der Alpha-Beta-Algorithmus standardmäßig aufgerufen?");
			mc.addAnswer("1", "run(root, ∞, ∞)", -1, "Falsch.");
			mc.addAnswer("2", "run(root, -∞, ∞)", 1, "Richtig!");
			mc.addAnswer("3", "run(root, ∞, -∞)", -1, "Falsch.");
			mc.addAnswer("4", "run(5, -∞, ∞)", -1, "Falsch.");
			mc.addAnswer("5", "run(root, -∞, -∞)", -1, "Falsch.");
			lang.addMCQuestion(mc);
			next();
		}

		updateText("start");
		next();

		result = run(root, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}

	protected int run(Node state, int alpha, int beta) {
		/** 0 */
		toggleStateHighlighting(state);
		int alpha2 = alpha;
    int beta2 = beta;
    updateBorders(state, alpha2, beta2);
		expandedNodes++;

		if (state.isLeaf())
			next(1, "Blattknoten " + (++labelLeaf) + " expandiert");
		else
			next(1);
		if (state.isLeaf()) {
			/** 1 */
			updateText("leaf");

			next(2);
			// memorize previous state to uncolor it in recursion
			preState = state;
			colorPrimitive("t" + state.getId(), treeHighlightColor);
			return state.getValue();
			/** 2 */
		}
		int childCount = 0;

		next(3);
		updateText("isMax");
		if (state.isMax()) {
			/** 3 */

			next(4);
			for (Node childState : state.getChildren()) {
				/** 4 */
				childCount++;

				next(5);
				// preparation for recursion
				preState = state;
				colorPrimitive("p" + childState.getId(), treeHighlightColor);

				next(0);
				colorPrimitive("p" + childState.getId(), Color.BLACK);
				alpha2 = Math.max(alpha2, run(childState, alpha2, beta2));
				/** 5 */

				next(5);
				toggleStateHighlighting(state);

				// Quiz: "Wie sehen die Grenzen im folgenden Schritt aus?"
				if (rnd.nextInt(100) < possibilityQuestion && alpha2 != beta2) {
					String sa = (alpha2 == Integer.MIN_VALUE) ? "-∞" : String.valueOf(alpha2);
					String sb = (beta2 == Integer.MAX_VALUE) ? "∞" : String.valueOf(beta2);
					MultipleChoiceQuestionModel mc1 = new MultipleChoiceQuestionModel("alphaBorder"
							+ (questionCounter++));
					mc1.setPrompt("Wie sehen die Grenzen im folgenden Schritt aus?");
					mc1.addAnswer("[" + sb + "," + sb + "]", -1, "Falsch.");
					mc1.addAnswer("[" + sa + "," + sb + "]", 1, "Richtig!");
					mc1.addAnswer("[" + sa + "," + sa + "]", -1, "Falsch.");
					mc1.addAnswer("[" + sb + "," + sa + "]", -1, "Falsch.");
					lang.addMCQuestion(mc1);
					next();
				}

				updateBorders(state, alpha2, beta2);
				visitedToGray(childState);

				if (alpha2 >= beta2)
					next(6, "Beta Cut-Off " + (++labelBetaCut)); // Beta cut-off
				else
					next(6);
				colorPrimitive("questionCounter" + state.getId(), Color.BLACK);

				// Quiz: Wird jetzt ein Cut geschehen?
				if (rnd.nextInt(100) < possibilityQuestion) {
					TrueFalseQuestionModel tfm = new TrueFalseQuestionModel("b-cut" + (questionCounter++),
							alpha2 >= beta2, 1);
					tfm.setPrompt("Wird jetzt ein Cut geschehen?");
					lang.addTFQuestion(tfm);
					next();
				}

				// beta cut-off
				if (alpha2 >= beta2) {
					/** 6 */
					updateText("b-cut");

					next(7);
					for (int i = childCount; i < state.getChildren().size(); i++) {
						drawCutline(state.getChildren().get(i).getId(), false, i == childCount);
					}
					break;
					/** 7 */
				}
			}
			/** 8 */

			next(9);
			preState = state;
			return alpha2;
			/** 9 */

		} else {
			/** 10 */

			next(10);
			updateText("isMin");
			next(11);

			for (Node childState : state.getChildren()) {
				/** 11 */
				childCount++;

				next(12);
				// preparation for recursion
				preState = state;
				colorPrimitive("p" + childState.getId(), treeHighlightColor);

				next(0);
				colorPrimitive("p" + childState.getId(), Color.BLACK);
				beta2 = Math.min(beta2, run(childState, alpha2, beta2));
				/** 12 */

				next(12);
				toggleStateHighlighting(state);

				// Quiz: "Wie sehen die Grenzen im folgenden Schritt aus?"
				if (rnd.nextInt(100) < possibilityQuestion && alpha2 != beta2) {
					String sa = (alpha2 == Integer.MIN_VALUE) ? "-∞" : String.valueOf(alpha2);
					String sb = (beta2 == Integer.MAX_VALUE) ? "∞" : String.valueOf(beta2);
					MultipleChoiceQuestionModel mc1 = new MultipleChoiceQuestionModel("betaBorder"
							+ (questionCounter++));
					mc1.setPrompt("Wie sehen die Grenzen im folgenden Schritt aus?");
					mc1.addAnswer("[" + sb + "," + sb + "]", -1, "Falsch.");
					mc1.addAnswer("[" + sa + "," + sb + "]", 1, "Richtig!");
					mc1.addAnswer("[" + sa + "," + sa + "]", -1, "Falsch.");
					mc1.addAnswer("[" + sb + "," + sa + "]", -1, "Falsch.");
					lang.addMCQuestion(mc1);
					next();
				}

				updateBorders(state, alpha2, beta2);
				visitedToGray(childState);

				if (beta2 <= alpha2)
					next(13, "Alpha Cut-Off " + (++labelAlphaCut));
				else
					next(13);
				colorPrimitive("questionCounter" + state.getId(), Color.BLACK);

				// Quiz: Wird jetzt ein Cut geschehen?
				if (rnd.nextInt(100) < possibilityQuestion) {
					TrueFalseQuestionModel tfm = new TrueFalseQuestionModel("a-cut" + (questionCounter++),
							beta2 <= alpha2, 1);
					tfm.setPrompt("Wird jetzt ein Cut geschehen?");
					lang.addTFQuestion(tfm);
					next();
				}

				// alpha cut-off
				if (beta2 <= alpha2) {
					/** 13 */
					updateText("a-cut");

					next(14);
					for (int i = childCount; i < state.getChildren().size(); i++) {
						drawCutline(state.getChildren().get(i).getId(), true, i == childCount);
					}
					break;
					/** 14 */
				}
			}
			/** 15 */

			next(16);
			preState = state;
			return beta2;
			/** 16 */
		}
		/** 17 */
	}

	protected void end() {
		/* last AS */
		next();
		sc.unhighlight(9);
		updateText("finish");
		pMap.get(root.getId()).changeColor("", Color.GRAY, null, null);
		pMap.get(root.getId()).changeColor("fillColor", Color.WHITE, null, null);
		next("Ende Algorithmus");
		hideEverything();
		next("Abspann");
		writeEpilog();
	}

	private void writeEpilog() {
		SourceCode epilog = lang.newSourceCode(new Offset(0, 50, pMap.get("hRect"), "SW"), "epilog", null);
		epilog.addCodeLine("Das Ergebnis der Spielbaum Suche mit dem Alpha-Beta-Algorithmus", "", 0, null);
		epilog.addCodeLine("ergab den Wert " + result + ". Dieses Ergebnis resultiert aus dem eben", "", 0, null);
		epilog.addCodeLine("ausgeführten Algorithmus mit der dargestellten Baumstruktur.", "", 0, null);
		epilog.addCodeLine("", "", 0, null);
		epilog.addCodeLine("Die Größe des Baumes ergibt sich aus dem Verzweigungsfaktor", "", 0, null);
		epilog.addCodeLine("(branching factor b) und der Tiefe (depth d). Es gibt also", "", 0, null);
		epilog.addCodeLine("in der Summe von 0 bis d genau b^i viele Knoten, wobei folglich ", "", 0, null);
		epilog.addCodeLine("durch das 'prunen' nicht alle Knoten durchsucht werden.", "", 0, null);
		epilog.addCodeLine("Damit ergibt sich eine Komplexität von O(b^d) in der Landau-", "", 0, null);
		epilog.addCodeLine("Notation.", "", 0, null);
		epilog.addCodeLine("", "", 0, null);
		epilog.addCodeLine("Um den Algorithmus weiter zu optimieren, verwendet man z.Bsp.:", "", 0, null);
		epilog.addCodeLine("- Beschränkung der Suchtiefe (iterative-deepening)", "", 1, null);
		epilog.addCodeLine("- Vorsortieren der Spielzüge", "", 1, null);
		epilog.addCodeLine("- Minimal-Window Search (Heurisitk)", "", 1, null);
		epilog.addCodeLine("- diverse andere Anwendungen von Heuristiken", "", 1, null);
		epilog.addCodeLine("", "", 0, null);
		epilog.addCodeLine("Während der Alpha-Beta-Algorithmus nur " + expandedNodes + " Knoten expandiert hat,", "",
				0, null);
		epilog.addCodeLine("hätte der MiniMax-Algorithmus alle Knoten expandiert.", "", 0, null);
		next();
		epilog.hide();
		SourceCode nodecode = lang.newSourceCode(new Offset(0, 50, pMap.get("hRect"), "SW"), "nodecode", null);
		nodecode.addCodeLine("Hier ist noch der Code zur Hilfsklasse Node:", "", 0, null);
		nodecode.addCodeLine("public class Node {", "", 0, null);
		nodecode.addCodeLine("private List<Node> children;", "", 1, null);
		nodecode.addCodeLine("private Node parent;", "", 1, null);
		nodecode.addCodeLine("private Integer value;", "", 1, null);
		nodecode.addCodeLine("private boolean isMax;", "", 1, null);
		nodecode.addCodeLine("", "", 0, null);

		nodecode.addCodeLine("public Node(Node parent) {", "", 1, null);
		nodecode.addCodeLine("this(parent, null);", "", 2, null);
		nodecode.addCodeLine("}", "", 1, null);

		nodecode.addCodeLine("public Node(Node parent, Integer value) {", "", 1, null);
		nodecode.addCodeLine("this.children = new ArrayList<Node>();", "", 2, null);
		nodecode.addCodeLine("this.value = value;", "", 2, null);

		nodecode.addCodeLine("if (parent == null) {", "", 2, null);
		nodecode.addCodeLine("this.isMax = true;", "", 3, null);
		nodecode.addCodeLine("this.depth = 0;", "", 3, null);
		nodecode.addCodeLine("} else {", "", 2, null);
		nodecode.addCodeLine("this.isMax = !parent.isMax;", "", 3, null);
		nodecode.addCodeLine("parent.addChild(this);", "", 3, null);
		nodecode.addCodeLine("}", "", 2, null);
		nodecode.addCodeLine("}", "", 1, null);

		nodecode.addCodeLine("private void addChild(Node node) {", "", 1, null);
		nodecode.addCodeLine("children.add(node);", "", 2, null);
		nodecode.addCodeLine("}", "", 1, null);

		nodecode.addCodeLine("public boolean isMax() {", "", 1, null);
		nodecode.addCodeLine("return isMax;", "", 2, null);
		nodecode.addCodeLine("}", "", 1, null);

		nodecode.addCodeLine("public boolean isLeaf() {", "", 1, null);
		nodecode.addCodeLine("return children.isEmpty();", "", 2, null);
		nodecode.addCodeLine("}", "", 1, null);

		nodecode.addCodeLine("public Integer getValue() {", "", 1, null);
		nodecode.addCodeLine("return value;", "", 2, null);
		nodecode.addCodeLine("}", "", 1, null);

		nodecode.addCodeLine("public List<Node> getChildren() {", "", 1, null);
		nodecode.addCodeLine("return children;", "", 2, null);
		nodecode.addCodeLine("}", "", 1, null);

		nodecode.addCodeLine("", "", 1, null);
		nodecode.addCodeLine("}", "", 0, null);
		next();
		nodecode.hide();
		SourceCode thanks = lang.newSourceCode(new Offset(0, 50, pMap.get("hRect"), "SW"), "thanks", null);
		thanks.addCodeLine("Vielen Dank für das Benutzen dieser Animation!", "", 0, null);
	}

	private void next() {
		lang.nextStep();
	}

	private void next(String label) {
		lang.nextStep(label);
	}

	private void next(int nextLine) {
		lang.nextStep();
		sc.toggleHighlight(currentLine, nextLine);
		currentLine = nextLine;
		updateText("clear");
	}

	private void next(int nextLine, String label) {
		lang.nextStep(label);
		sc.toggleHighlight(currentLine, nextLine);
		currentLine = nextLine;
		updateText("clear");
	}

	private void hideEverything() {
		for (Primitive p : pMap.values()) {
			p.hide();
		}
		pMap.get("hRect").show();
		updateText("clear");
		sc.hide();
	}

	private void updateBorders(Node state, int alpha, int beta) {
		if (state.isLeaf())
			return;

		String a = alpha == Integer.MIN_VALUE ? "-∞" : String.valueOf(alpha);
		String b = beta == Integer.MAX_VALUE ? "∞" : String.valueOf(beta);

		String in = "questionCounter" + state.getId();
		if (pMap.get(in) == null) {
			pMap.put(in, lang.newText(new Offset(5, 0, pMap.get(state.getId()), "NE"), "[" + a + "," + b + "]",
					"questionCounter" + state.getId(), null));
			updateText("expand");
		} else {
			Text new_borders = (Text) pMap.get(in);
			new_borders.setText("[" + a + "," + b + "]", null, null);
			new_borders.changeColor("", borderHighlightColor, null, null);
			updateText(state.isMax() ? "a-update" : "b-update");
		}
	}

	private void updateText(String text) {
		explanationText.setText(eMap.get(text), null, null);
	}

	/********************
	 * COLORING METHODS *
	 *******************/

	private void toggleStateHighlighting(Node state) {
		if (preState != null) {
			String id = preState.getId();
			pMap.get(id).changeColor("", Color.BLACK, null, null);
			pMap.get(id).changeColor("fillColor", nodeFillColor, null, null);
			if (preState.isLeaf()) {
				pMap.get("t" + id).changeColor("", Color.BLACK, null, null);
			}
		}
		pMap.get(state.getId()).changeColor("", nodeHighlightColor, null, null);
		pMap.get(state.getId()).changeColor("fillColor", nodeHighlightColor, null, null);
	}

	private void colorPrimitive(String node, Color color) {
		pMap.get(node).changeColor("", color, null, null);
	}

	private void visitedToGray(Node childState) {
		pMap.get(childState.getId()).changeColor("", visitedNodeColor, null, null);
		pMap.get(childState.getId()).changeColor("fillColor", Color.WHITE, null, null);
		pMap.get("p" + childState.getId()).changeColor("", visitedNodeColor, null, null);
		if (childState.isLeaf())
			pMap.get("t" + childState.getId()).changeColor("", visitedNodeColor, null, null);
	}

	/****************
	 * DRAW METHODS *
	 ****************/

	private void drawCutline(String id, boolean isAlpha, boolean drawLabel) {
		Primitive p = pMap.get("p" + id);
		Offset[] o = new Offset[] { new Offset(-15, 15, p, "C"), new Offset(15, -15, p, "C") };
		Polyline pl = lang.newPolyline(o, "c" + id, null);
		pMap.put("c" + id, pl);
		if (!drawLabel)
			return;
		Text tc;
		if (isAlpha)
			tc = lang.newText(new Offset(7, -15, pl, "NE"), "Alpha-Cutoff", "tc" + id, null);
		else
			tc = lang.newText(new Offset(7, -15, pl, "NE"), "Beta-Cutoff", "tc" + id, null);
		tc.changeColor("", cutlineTextColor, null, null);
		pMap.put("tc" + id, tc);
	}

	private void drawTree(Node node) {
		int y = node.getDepth() * (leafYPadding + nodeSize);
		int lastLeftBorder = leftBorder;
		if (node.isLeaf()) {
			drawLeaf(leftBorder + sourceCodeDistance, y, node);
			leftBorder += nodeSize + leafXPadding;
			return;
		}
		for (Node child : node.getChildren()) {
			drawTree(child);
		}
		int x = lastLeftBorder + (leftBorder - lastLeftBorder - leafXPadding) / node.getChildren().size();
		drawMinMax(x + sourceCodeDistance, y, node);
		// drawPolyline
		Primitive from = pMap.get(node.getId());
		for (Node child : node.getChildren()) {
			Primitive to = pMap.get(child.getId());
			drawPolyLine(from, to, child.getId());
		}
	}

	private void drawMinMax(int x, int y, Node node) {
		int yS = node.isMax() ? y : (y + nodeSize);
		int yD = node.isMax() ? y + nodeSize : y;
		Offset s = new Offset(x, yS, sc, "NE");
		Offset dL = new Offset(x - (nodeSize / 2), yD, sc, "NE");
		Offset dR = new Offset(x + (nodeSize / 2), yD, sc, "NE");

		TriangleProperties nodeProps = new TriangleProperties();
		nodeProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		nodeProps.set(AnimationPropertiesKeys.FILL_PROPERTY, nodeFillColor);

		Triangle tNode = lang.newTriangle(s, dL, dR, node.getId(), null, nodeProps);
		pMap.put(node.getId(), tNode);
	}

	private void drawLeaf(int x, int y, Node node) {
		Offset uL = new Offset(x, y, sc, "NE");
		Offset lR = new Offset(x + nodeSize, y + nodeSize, sc, "NE");

		RectProperties nodeProps = new RectProperties();
		nodeProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		nodeProps.set(AnimationPropertiesKeys.FILL_PROPERTY, nodeFillColor);

		Rect rect = lang.newRect(uL, lR, node.getId(), null, nodeProps);
		int valLength = String.valueOf(node.getValue()).length();
		Offset tO = new Offset(-valLength * 3, -nodeSize / 10, rect, "C");
		Text tVal = lang.newText(tO, String.valueOf(node.getValue()), "t" + node.getId(), null);
		pMap.put(node.getId(), rect);
		pMap.put("t" + node.getId(), tVal);
	}

	private void drawPolyLine(Primitive a, Primitive b, String id) {
		Offset o1 = new Offset(0, 0, a, "S");
		Offset o2 = new Offset(0, 0, b, "N");
		Offset[] offsets = new Offset[] { o1, o2 };
		Polyline p = lang.newPolyline(offsets, "p" + id, null);
		pMap.put("p" + id, p);
	}

}