package generators.searching;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.QuestionGroupModel;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Graph;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

/**
 * Generator for IDA* algorithm.
 * 
 * See https://en.wikipedia.org/wiki/IDA*
 * 
 * @author Johannes Simon <johannes.simon@gmail.com>
 */
public class IDAStar implements Generator {
	Language m_l;

	// Input graph
	Graph m_g;
	private GraphProperties G_Props;
	private TextProperties Header_Props;
	private TextProperties Counter_Props;
	private SourceCodeProperties Explanation_Props;
	private SourceCodeProperties Source_Props;

	// Start node for IDA*
	private int m_root;
	// Target node for IDA*
	private int m_target;

	// Whether to show each step from each pseudo-code line when
	// done with first iteration. If false, only one step is added
	// per node visit.
	boolean m_showSubStepsAfterFirstIteration;
	// Set to false after first iteration if m_showSubStepsAfterFirstIteration is false
	boolean m_showSubSteps;

	// Explanation text
	SourceCode m_exp;
	// Pseudo code
	SourceCode m_source;

	// Basic layout
	final static Coordinates POS_COUNTER1 = new Coordinates(600, 20);
	final static Coordinates POS_COUNTER2 = new Coordinates(POS_COUNTER1.getX(), POS_COUNTER1.getY() + 20);
	final static Coordinates POS_COUNTER3 = new Coordinates(POS_COUNTER2.getX(), POS_COUNTER2.getY() + 20);
	final static Coordinates POS_COUNTER4 = new Coordinates(POS_COUNTER3.getX(), POS_COUNTER3.getY() + 20);
	final static Coordinates POS_COUNTER5 = new Coordinates(POS_COUNTER4.getX(), POS_COUNTER4.getY() + 20);
	final static Coordinates POS_COUNTER6 = new Coordinates(POS_COUNTER5.getX(), POS_COUNTER5.getY() + 20);
	final static Coordinates POS_SOURCE = new Coordinates(POS_COUNTER6.getX(), POS_COUNTER6.getY() + 30);
	final static Coordinates POS_EXP = new Coordinates(50, 50);
	final static Coordinates POS_G = new Coordinates(POS_EXP.getX(), POS_EXP.getY() + 140);

	// Precision of displayed floating point numbers
	static final int NUM_PREC = 2;

	private int m_nodeCounter[];
	private int m_edgeCounter[][];
	private int m_iteration;
	Text m_counterVisitedNodes;
	Text m_counterDepth;
	Text m_counterMaxDepth;
	Text m_counterIteration;
	Text m_rootText;
	Text m_targetText;
	int m_counterVisitedNodesValue;

	/**
	 * Initializes this generator. By calling this method, the same instance of this generator can be re-used several times.
	 */
	public void init(){
		m_iteration = 1;
		m_showSubSteps = true;
		m_counterVisitedNodesValue = 0;
		m_g = null;
		m_exp = null;
		m_source = null;
		m_l = new AnimalScript(getName(), getAnimationAuthor(), 1100, 800);
		m_l.setStepMode(true);
		m_l.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
	}

	public String getName() {
		return "Iterative-Deepening A* (IDA*)";
	}

	public String getAlgorithmName() {
		return "IDA*";
	}

	public String getAnimationAuthor() {
		return "Johannes Simon";
	}

	public String getDescription() {
		return "Very briefly put, iterative-deepening A* (IDA*) is a <b>modified depth-first search (DFS)</b>.\n" +
				"In every iteration, a DFS is performed, but is limited to a maximum path cost. While in the\n" +
				"classical iterative-deepening DFS (IDDFS), the search is limited by the <i>depth</i> of the path from the\n" +
				"root node to the current node, IDA* iterations are limited by the total path <i>cost</i> from the root to the current node.\n" +
				"<br/><br/>" +
				"<b>The idea comes from the A* algorithm</b>: Given a heuristic cost function that returns the\n" +
				"minimum cost for a possible path between two nodes, we can first visit nodes that are\n" +
				"more \"promosing\" according to this heuristic, and thus leave a lot of unpromising\n" +
				"nodes out, which can reduce runtime significantly." +
				"<br/>" +
				"While the original A* algorithm keeps two lists of nodes (open and closed list) to\n" +
				"keep track of which nodes it has already processed and to avoid re-visiting the\n" +
				"same nodes, <b>IDA* is nearly memory-less</b>, i.e. it does not keep list of nodes.\n" +
				"This has the downside that it potentially re-visits some nodes, but has the advantage\n" +
				"of a very low memory footprint, which is useful if A* would run out of memory.\n" +
				"<br/>" +
				"Note that this version of the IDA* algorithm does not return a path between root and\n" +
				"target node. It is only used to find out whether the target node can be reached from\n" +
				"the root node. Instead of findind a specific target node, one could also use it to find\n" +
				"an \"acceptable\" node in the graph, e.g. a possible solution in a solution space." +
				"<br/><br/>" +
				"While for graphs containing many cycles, IDA* will perform rather bad, it is best suited\n" +
				"for tree structures for which a heuristic cost function exists. Since most nodes in a tree\n" +
				"are leaves, re-running the DFS often with an increased maximum depth has only little effect on\n" +
				"the runtime. The last iteration will always consume most time. At the same time, the tree\n" +
				"structure guarantees that in every iteration, each node is visited at most one time. Therefore,\n" +
				"no memory about which nodes have been visited already is required.\n";
	}
	public String getCodeExample() {
		return "function ida_star(from, to)\n" + // 0
				"	bound = h(from, to)\n" + // 1
				"	loop\n" + // 2
				"		t == search(from, to, 0, bound)\n" + // 3
				"		if t == 0 then return FOUND\n" + // 4
				"		if t == ∞ then return NOT_FOUND\n" + // 5
				"		bound = t\n" + // 6
				"\n" + // 7
				"function search(from, to, g, bound)\n" + // 8
				"	f = g + h(from, to)\n" + // 9
				"	if f > bound then return f\n" + // 10
				"	if from == to then return 0\n" + // 11
				"	min = ∞\n" + // 12
				"	foreach succ in successors(from)\n" + // 13
				"		t = search(succ, to, g + cost(from, succ), bound)\n" + // 14
				"		if t == 0 then return 0\n" + // 15
				"		if t < min then min := t\n" + // 16
				"	return min"; // 17
	}

	public String getFileExtension(){
		return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	public Locale getContentLocale() {
		return Locale.US;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_SEARCH);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	private void showHeader() {
		Text header = m_l.newText(new Coordinates(200, 30), getName(), "text", null, Header_Props);

		RectProperties rectProps = new RectProperties();
		rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.green);

		m_l.newRect(new Offset(-5, -5, header, "NW"), new Offset(5, 5, header, "SE"), "headerRect", null, rectProps);
	}

	private List<Integer> m_sourceLineElements = new ArrayList<Integer>();
	private void showPseudoCode() {
		// *** MCL Pseudo Code ***
		clearExplanation();
		addExplanationLine("Here's the pseudocode of the IDA* algorithm.");

		m_source = m_l.newSourceCode(POS_SOURCE, "pseudocode", null, Source_Props);
		String code = getCodeExample();
		String codeLines[] = code.split("\n");
		for(String line : codeLines) {
			int indentation = line.replaceAll("[^\t]", "").length();
			String[] codeElements = line.split("then");
			if (codeElements.length == 1)
				m_source.addCodeLine(line, null, indentation, null);
			else {
				m_source.addCodeLine(codeElements[0], null, indentation, null);
				m_source.addCodeElement("then" + codeElements[1], null, true, 0, null);
			}
			m_sourceLineElements.add(codeElements.length);
		}
		m_l.nextStep("IDA* Pseudocode");

		clearExplanation();
		addExplanationLine("As you can see, there's two different functions that are");
		addExplanationLine("relevant to us: ida_star() and search().");
		m_l.nextStep();

		clearExplanation();
		addExplanationLine("ida_star() contains the outer loop of the algorithm: it");
		addExplanationLine("iteratively deepens the depth (variable \\\"bound\\\") of the DFS search.");
		highlightCodeLines(0, 6);
		m_l.nextStep();
		unhighlightCodeLines(0, 6);

		clearExplanation();
		addExplanationLine("search() performs the DFS. Once the total minimum cost, f, to the");
		addExplanationLine("target node(\\\"to\\\") is higher than \\\"bound\\\", the current path is");
		addExplanationLine("abandoned. If the target node cannot be found this way, search()");
		addExplanationLine("ultimately returns a positive cost, which equals the minimum cost f");
		addExplanationLine("at which search() abandoned paths. ida_star() then repeats the DFS");
		addExplanationLine("with this as new maximum depth.");
		highlightCodeLines(8, 17);
		m_l.nextStep();
		unhighlightCodeLines(8, 17);
	}

	private void showCounters() {
		clearExplanation();
		addExplanationLine("Here's a few counters that help you keep track of the current state,");
		addExplanationLine("as well as the root and target node you specified. Also make sure");
		addExplanationLine("you have the variable window and table of contents window open.");

		m_counterVisitedNodesValue = 0;
		m_counterVisitedNodes = m_l.newText(POS_COUNTER1, "# visited nodes: " + m_counterVisitedNodesValue, "numVisNodes", null, Counter_Props);
		m_counterMaxDepth = m_l.newText(POS_COUNTER2, "Max. cost: -", "maxCost", null, Counter_Props);
		m_counterDepth = m_l.newText(POS_COUNTER3, "Current cost: -", "currCost", null, Counter_Props);
		m_counterIteration = m_l.newText(POS_COUNTER4, "Iteration:", "currIteration", null, Counter_Props);
		m_rootText = m_l.newText(POS_COUNTER5, "Root: " + m_g.getNodeLabel(m_root), "rootNode", null, Counter_Props);
		m_targetText = m_l.newText(POS_COUNTER6, "Target: " + m_g.getNodeLabel(m_target), "targetNode", null, Counter_Props);
		m_l.nextStep("Introduce counters");
	}

	private void highlightCodeLines(int first, int last) {
		for (int i = first; i <= last; i++) {
			int numCols = m_sourceLineElements.get(i);
			for (int j = 0; j < numCols; j++)
				m_source.highlight(i, j, false);
		}
	}

	private void unhighlightCodeLines(int first, int last) {
		for (int i = first; i <= last; i++) {
			int numCols = m_sourceLineElements.get(i);
			for (int j = 0; j < numCols; j++)
				m_source.unhighlight(i, j, false);
		}
	}

	private void showIntroduction() {
		clearExplanation();
		addExplanationLine("Very briefly put, iterative-deepening A* (IDA*) is a modified depth-first search (DFS).");
		addExplanationLine("In every iteration, a DFS is performed, but is limited to a maximum path cost. While in");
		addExplanationLine("the classical iterative-deepening DFS (IDDFS), the search is limited by the depth of the");
		addExplanationLine("path from the root node to the current node, IDA* iterations are limited by the total");
		addExplanationLine("path cost from the root to the current node.");
		m_l.nextStep("Introduction");

		clearExplanation();
		addExplanationLine("The idea comes from the A* algorithm: Given a heuristic cost function that returns the");
		addExplanationLine("minimum cost for a possible path between two nodes, we can first visit nodes that are");
		addExplanationLine("more \\\"promosing\\\" according to this heuristic, and thus leave a lot of unpromising");
		addExplanationLine("nodes out, which can reduce runtime significantly.");
		m_l.nextStep();

		clearExplanation();
		addExplanationLine("While the original A* algorithm keeps two lists of nodes (open and closed list) to");
		addExplanationLine("keep track of which nodes it has already processed and to avoid re-visiting the");
		addExplanationLine("same nodes, IDA* is nearly memory-less, i.e. it does not keep list of nodes.");
		addExplanationLine("This has the downside that it potentially re-visits some nodes, but has the advantage");
		addExplanationLine("of a very low memory footprint, which is useful if A* would run out of memory.");
		m_l.nextStep();

		clearExplanation();
		addExplanationLine("Note that this version of the IDA* algorithm does not return a path between root and");
		addExplanationLine("target node. It is only used to find out whether the target node can be reached from");
		addExplanationLine("the root node. Instead of findind a specific target node, one could also use it to find");
		addExplanationLine("an \\\"acceptable\\\" node in the graph, e.g. a possible solution in a solution space.");
		m_l.nextStep();
	}

	private void showInitialization() {
		clearExplanation();
		if (G_Props.get(AnimationPropertiesKeys.WEIGHTED_PROPERTY).equals(false)) {
			G_Props.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, true);
			addExplanationLine("In this demonstration, for simplicity, the cost to travel between");
			addExplanationLine("two nodes is always the euclidean distance between them. This is");
			addExplanationLine("of course rarely the case in real applications of the algorithm.");
			for (int i = 0; i < m_g.getSize(); i++) {
				List<Integer> outNodes = getOutNodes(i);
				for (int j : outNodes) {
					int c = (int)Math.round(h(i, j));
					m_g.setEdgeWeight(i, j, c, null, null);
				}
			}
			updateGraph();
		}
		addExplanationLine("The heuristic cost function h(a,b) returns the euclidean");
		addExplanationLine("distance as an approximation of the cost from a to b.");
		m_l.nextStep();
	}

	/**
	 * Returns list of nodes that <code>node</code> points to
	 */
	private List<Integer> getOutNodes(int node) {
		List<Integer> outNodes = new ArrayList<Integer>();
		int[] outEdges = m_g.getEdgesForNode(node);
		for (int i = 0; i < outEdges.length; i++) {
			if (outEdges[i] > 0) {
				outNodes.add(i);
			}
		}
		return outNodes;
	}

	/**
	 * Called by Animal framework to generate AnimalScript code for user-specified properties and primitives
	 */
	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		G_Props = (GraphProperties)props.getPropertiesByName("G_Props");
		Header_Props = (TextProperties)props.getPropertiesByName("Header_Props");
		Font headerFont = (Font)Header_Props.get(AnimationPropertiesKeys.FONT_PROPERTY);
		Header_Props.set(AnimationPropertiesKeys.FONT_PROPERTY, headerFont.deriveFont(20.0f));
		Counter_Props = (TextProperties)props.getPropertiesByName("Counter_Props");
		Explanation_Props = (SourceCodeProperties)props.getPropertiesByName("Explanation_Props");
		Font expFont = (Font)Explanation_Props.get(AnimationPropertiesKeys.FONT_PROPERTY);
		Explanation_Props.set(AnimationPropertiesKeys.FONT_PROPERTY, expFont.deriveFont(16.0f));
		Source_Props = (SourceCodeProperties)props.getPropertiesByName("Source_Props");
		Font sourceFont = (Font)Source_Props.get(AnimationPropertiesKeys.FONT_PROPERTY);
		Source_Props.set(AnimationPropertiesKeys.FONT_PROPERTY, sourceFont.deriveFont(14.0f));
		m_showSubStepsAfterFirstIteration = (Boolean)primitives.get("showSubStepsAfterFirstIteration");
		Graph g = (Graph) primitives.get("graph");
		int from = getNodeIndex(g, g.getStartNode());
		int to = getNodeIndex(g, g.getTargetNode());
		m_root = from;
		m_target = to;

		int n = g.getSize();
		Node[] nodes = new Node[n];
		String[] labels = new String[n];
		for(int i = 0; i < n; i++) {
			nodes[i] = g.getNode(i);
			labels[i] = g.getNodeLabel(i);
		}
		m_nodeCounter = new int[n];
		m_edgeCounter = new int[n][n];

		m_g = m_l.newGraph("G1", g.getAdjacencyMatrix(), nodes, labels, null, G_Props);
		m_g.moveTo(null, null, POS_G, null, null);

		showHeader();
		showIntroduction();
		showPseudoCode();
		showCounters();
		showInitialization();
		idaStar(from, to);

		m_l.finalizeGeneration();

		return m_l.toString();
	}

	/**
	 * Returns index of a given node in given graph, or -1 if not found
	 */
	private int getNodeIndex(Graph g, Node n) {
		for (int i = 0; i < g.getSize(); i++) {
			if (g.getNode(i) == n)
				return i;
		}
		return -1;
	}

	private int getNodeY(Node n) {
		if (n instanceof Offset)
			return ((Offset)n).getY();
		return ((Coordinates)n).getY();
	}

	private int getNodeX(Node n) {
		if (n instanceof Offset)
			return ((Offset)n).getX();
		return ((Coordinates)n).getX();
	}

	/**
	 * Admissable heuristic function for IDA* algorithm returning
	 * minimum cost for path from a to b
	 */
	private int h(int a, int b) {
		Node nodeA = m_g.getNode(a);
		Node nodeB = m_g.getNode(b);
		double dx = getNodeX(nodeB) - getNodeX(nodeA);
		double dy = getNodeY(nodeB) - getNodeY(nodeA);
		return (int)Math.round(Math.sqrt(dx*dx + dy*dy));
	}

	/**
	 * Cost function returning cost for direct path from a to b,
	 * if one exists
	 */
	private int cost(int a, int b) {
		return m_g.getEdgeWeight(a, b);
	}

	/**
	 * Outer loop of IDA* algorithm that iteratively increases depth (=maximum cost)
	 */
	private void idaStar(int root, int to) {
		String rootLabel = m_g.getNodeLabel(root);
		String toLabel = m_g.getNodeLabel(to);
		clearExplanation();
		addExplanationLine("Let's start with calling ida_star(" + rootLabel + ", " + toLabel + ")");
		//highlightRootAndTarget(root, to);
		Variables vars = m_l.newVariables();
		vars.openContext();
		switchCodeLine(1);
		m_l.nextStep();
		int bound = h(root, to);
		vars.declare("int", "\"bound\"", Integer.toString(bound));
		vars.declare("String", "\"from\"", rootLabel);
		vars.declare("String", "\"to\"", toLabel);
		Variables loopVars = m_l.newVariables();
		while(true) {
			if (m_iteration > 1 && !m_showSubStepsAfterFirstIteration) {
				m_showSubSteps = false;
			}
			loopVars.openContext();
			clearExplanation();
			m_counterIteration.setText("Iteration: " + m_iteration, null, null);
			switchCodeLine(3);
			m_l.nextStep("Iteration " + m_iteration + ", max. depth = " + bound);

			highlightNode(root);
			vars.closeContext();
			int t = search(root, to, 0, bound);
			vars.openContext();
			vars.declare("int", "\"bound\"", Integer.toString(bound));
			vars.declare("String", "\"from\"", rootLabel);
			vars.declare("String", "\"to\"", toLabel);
			unhighlightNode(root);
			loopVars.declare("int", "\"t\"", Integer.toString(t));

			switchCodeElement(4, 0);
			m_l.nextStep();
			if (t == 0) {
				switchCodeElement(4, 1);
				m_l.nextStep();
				System.out.println("Found!");
				showConclusion(true);
				return;
			}
			switchCodeElement(5, 0);
			m_l.nextStep();
			if (t == Integer.MAX_VALUE) {
				switchCodeElement(5, 1);
				m_l.nextStep();
				System.out.println("Not found!");
				showConclusion(false);
				return;
			}
			switchCodeLine(6);
			m_l.nextStep();
			bound = t;
			vars.set("\"bound\"", Integer.toString(bound));
			m_iteration++;
		}
	}

	/**
	 * DFS method of IDA* algorithm, called from idaStar()
	 */
	private int search(int from, int to, int g, int bound) {
		incrementNumVisitedNodes();
		updateCurrDepth(g, bound);
		String fromLabel = m_g.getNodeLabel(from);
		String toLabel = m_g.getNodeLabel(to);
		Variables vars = m_l.newVariables();
		vars.openContext();
		// Line 8
		switchCodeLine(8);
		clearExplanation();
		vars.declare("String", "\"from\"", fromLabel);
		vars.declare("String", "\"to\"", toLabel);
		vars.declare("int", "\"g\"", Integer.toString(g));
		vars.declare("int", "\"bound\"", Integer.toString(bound));
		// This is the only step we do show
		//if (m_showSubSteps) {
		m_l.nextStep();
		//}

		// Line 9
		if (m_showSubSteps) {
			//int oldWeight1 = m_g.getEdgeWeight(m_root, from);
			//m_g.setEdgeWeight(m_root, from, g, null, null);
			//highlightEdge(m_root, from, false);
			int oldWeight2 = m_g.getEdgeWeight(from, to);
			m_g.setEdgeWeight(from, to, h(from, to), null, null);
			highlightEdge(from, to, false);
			switchCodeLine(9);
			clearExplanation();
			addExplanationLine("The total minimum cost remaining from " + fromLabel + " to " + toLabel + " is");
			addExplanationLine("determined by h(" + fromLabel + ", " + toLabel + ")=" + h(from, to) + ".");
			m_l.nextStep();
			clearExplanation();
			//m_g.setEdgeWeight(m_root, from, oldWeight1, null, null);
			//unhighlightEdge(m_root, from, false);
			m_g.setEdgeWeight(from, to, oldWeight2, null, null);
			unhighlightEdge(from, to, false);
			updateGraph();
		}
		int f = g + h(from, to);

		// Line 10
		if (m_showSubSteps) {
			vars.declare("int", "\"f\"", Integer.toString(f));
			switchCodeElement(10, 0);
			clearExplanation();
			addExplanationLine("If h(" + fromLabel + ", " + toLabel + ") added on top of the current cost (in total f=" + f + ")");
			addExplanationLine("exceeds the maximum cost (bound=" + bound + "), then abandon this path.");
			m_l.nextStep();
			clearExplanation();
		}
		if (f > bound) {
			// Line 10
			if (m_showSubSteps) {
				switchCodeElement(10, 1);
				clearExplanation();
				addExplanationLine("f=" + f + " > bound=" + bound);
				m_l.nextStep();
				clearExplanation();
			}
			showQuestion1(fromLabel);
			vars.closeContext();
			return f;
		}

		// Line 11
		if (m_showSubSteps) {
			switchCodeElement(11, 0);
			m_l.nextStep();
		}
		if (from == to) {
			// Line 11
			if (m_showSubSteps) {
				switchCodeElement(11, 1);
				m_l.nextStep();
			}
			vars.closeContext();
			return 0;
		}

		// Line 12
		if (m_showSubSteps) {
			switchCodeLine(12);
			m_l.nextStep();
			vars.declare("String", "\"min\"", "inf");
		}
		int min = Integer.MAX_VALUE;

		List<Integer> successors = getOutNodes(from);

		Variables loopVars = m_l.newVariables();
		int bestSucc = -1;
		for (int succ : successors) {
			String succLabel = m_g.getNodeLabel(succ);

			// Line 14
			if (m_showSubSteps) {
				loopVars.openContext();
				loopVars.declare("String", "\"succ\"", succLabel);
				switchCodeLine(14);
				m_l.nextStep();
				vars.closeContext();
				loopVars.closeContext();
			}
			unhighlightNode(from);
			highlightNode(succ);
			highlightEdge(from, succ, false);
			int t = search(succ, to, g + cost(from, succ), bound);
			updateCurrDepth(g, bound);
			unhighlightEdge(from, succ, false);
			unhighlightNode(succ);
			highlightNode(from);

			// Line 15
			// Show step here independent of m_showSubSteps to allow visual backtracking
			vars.openContext();
			vars.declare("String", "\"from\"", fromLabel);
			vars.declare("String", "\"to\"", toLabel);
			vars.declare("int", "\"g\"", Integer.toString(g));
			vars.declare("int", "\"bound\"", Integer.toString(bound));
			if (m_showSubSteps) {
				loopVars.openContext();
				loopVars.declare("String", "\"succ\"", succLabel);
				loopVars.declare("int", "\"t\"", Integer.toString(t));
				vars.declare("int", "\"f\"", Integer.toString(f));
				vars.declare("String", "\"min\"", "inf");
				switchCodeElement(15, 0);
			}
			// Show step here independent of m_showSubSteps to allow visual backtracking
			m_l.nextStep();
			if (t == 0.0) {
				// Line 15
				if (m_showSubSteps) {
					switchCodeElement(15, 1);
					m_l.nextStep();
					loopVars.closeContext();
				}
				vars.closeContext();
				return 0;
			}

			// Line 16
			if (m_showSubSteps) {
				switchCodeElement(16, 0);
				m_l.nextStep();
			}
			if (t < min) {
				// Line 16
				if (m_showSubSteps) {
					switchCodeElement(16, 1);
					m_l.nextStep();
				}
				bestSucc = succ;
				min = t;
				if (m_showSubSteps) {
					vars.set("\"min\"", Integer.toString(min));
				}
			}
			if (m_showSubSteps) {
				loopVars.closeContext();
			}
		}

		if (bestSucc != -1)
			showQuestion2(fromLabel, m_g.getNodeLabel(bestSucc));
		// Line 17
		if (m_showSubSteps) {
			switchCodeLine(17);
			m_l.nextStep();
		}
		vars.closeContext();
		if (min == Integer.MAX_VALUE) {
			System.out.println("INF! " + fromLabel + "->" + toLabel + "; succs: " + successors.size());
		}
		if (fromLabel.equals("A")) {
			System.out.println(min);
		}
		return min;
	}
	
	private void showConclusion(boolean found) {
		m_source.hide();
		m_counterVisitedNodes.hide();
		m_counterMaxDepth.hide();
		m_counterDepth.hide();
		m_counterIteration.hide();
		m_rootText.hide();
		m_targetText.hide();
		clearExplanation();
		addExplanationLine(m_g.getNodeLabel(m_target) + (found ? " has" : " has not") + " been found!");
		addExplanationLine("IDA* required " + m_iteration + " iterations and visited " + m_counterVisitedNodesValue + " nodes. In every iteration, O(n) nodes are visited. Therefore, worst case");
		addExplanationLine("runtime complexity is rather bad, especially if the graph contains cycles. However, if used for a tree structure for");
		addExplanationLine("which a heuristic function exists, IDA* turns out to perform well, especially when only limited memory is available.");
		addExplanationLine("The tree structure guarantees that within one iteration, every node is visited no more than one time. Since it is a");
		addExplanationLine("tree, the number of nodes grows exponentially with the depth. IDA* limits this depth effectively with the maximum cost.");
	}
	
	int m_questionCounter = 0;
	private void showQuestion1(String fromLabel) {
		QuestionGroupModel group = new QuestionGroupModel("q1-group", 1);
		m_l.addQuestionGroup(group);
		MultipleChoiceQuestionModel q1 = new MultipleChoiceQuestionModel("q" + m_questionCounter++);
		q1.setPrompt("Why was the path via " + fromLabel + " just abandoned and not followed?");
		q1.setGroupID("q1-group");
		String wrongAnswerNote = "That is incorrect :( The minimum total path cost surpassed bound (f > bound)";
		q1.addAnswer("The minimum remaining path cost surpassed bound", 0, wrongAnswerNote);
		q1.addAnswer("The minimum total path cost surpassed bound", 1, "That is correct!");
		q1.addAnswer("The current path cost surpassed bound", 0, wrongAnswerNote);
		q1.addAnswer("The current path depth surpassed bound", 0, wrongAnswerNote);
		m_l.addMCQuestion(q1);
	}
	private void showQuestion2(String fromLabel, String succLabel) {
		QuestionGroupModel group = new QuestionGroupModel("q2-group", 1);
		m_l.addQuestionGroup(group);
		FillInBlanksQuestionModel q2 = new FillInBlanksQuestionModel("q" + m_questionCounter++);
		q2.setPrompt("All successors of " + fromLabel + " have been visited, but the target node has not been found yet. " +
				"From which successor does the returned bound value originate?");
		q2.setGroupID("q2-group");
		q2.addAnswer(succLabel, 1, "From " + fromLabel + ", the successor with the most promising path is " + succLabel + ".");
		m_l.addFIBQuestion(q2);
	}

	int __gCounter = 2;
	private void updateGraph() {
		if (m_g != null) {
			m_g.hide();
		}

		int[][] adjMatrix = m_g.getAdjacencyMatrix();
		int n = m_g.getSize();
		Node[] nodes = new Node[n];
		String[] labels = new String[n];
		for(int i = 0; i < n; i++) {
			nodes[i] = m_g.getNode(i);
			labels[i] = m_g.getNodeLabel(i);
		}
		m_g = m_l.newGraph("_G" + __gCounter, adjMatrix, nodes, labels, null, G_Props);
		//m_g.moveTo(null, null, POS_G, null, null);
		//highlightClusterEdges(m_g, probs);
		__gCounter++;

		for (int i = 0; i < n; i++) {
			if (m_nodeCounter[i] > 0)
				m_g.highlightNode(i, null, null);
		}
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (m_edgeCounter[i][j] > 0)
					m_g.highlightEdge(i, j, null, null);
			}
		}

		m_g.moveTo(null, null, POS_G, null, null);
	}

	private int _lastHighlightedLine = -1;
	/**
	 * Helper method that remembers which code line to unhighlight
	 * with next call of switchCodeLine()
	 */
	private void switchCodeLine(int line) {
		if (_lastHighlightedLine != -1) {
			unhighlightCodeLines(_lastHighlightedLine, _lastHighlightedLine);
		}
		highlightCodeLines(line, line);
		_lastHighlightedLine = line;
	}

	/**
	 * Helper method that remembers which code line to unhighlight
	 * with next call of switchCodeLine()
	 */
	private void switchCodeElement(int line, int col) {
		if (_lastHighlightedLine != -1) {
			unhighlightCodeLines(_lastHighlightedLine, _lastHighlightedLine);
		}
		m_source.highlight(line, col, false);
		_lastHighlightedLine = line;
	}

	/**
	 * Helper method that remembers which nodes are not to be
	 * unhighlighted with call of unhighlightNode() because they were
	 * already highlighted when highlightNode() was called
	 */
	private void highlightNode(int node) {
		if (m_nodeCounter[node] == 0) {
			m_g.highlightNode(node, null, new TicksTiming(5));
		}
		m_nodeCounter[node]++;
	}

	/**
	 * @see switchNode
	 */
	private void unhighlightNode(int node) {
		m_nodeCounter[node]--;
		if (m_nodeCounter[node] == 0) {
			m_g.unhighlightNode(node, null, null);
		}
	}

	/**
	 * Draws a highlighting circle behind root and target node
	 */
	/*
	private void highlightRootAndTarget(int root, int target) {
		CircleProperties cp1 = new CircleProperties();
		cp1.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
		cp1.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		cp1.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.green);
		CircleProperties cp2 = new CircleProperties();
		cp2.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
		cp2.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		cp2.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.blue);
		m_l.newCircle(m_g.getNode(root), 20, "c1", null, cp1);
		m_l.newCircle(m_g.getNode(target), 20, "c2", null, cp2);
	}*/

	/**
	 * Helper method that remembers which edges are not to be
	 * unhighlighted with call of unhighlightEdge() because they were
	 * already highlighted when highlightEdge() was called
	 */
	private void highlightEdge(int from, int to, boolean highlightNodes) {
		if (highlightNodes) {
			highlightNode(from);
			highlightNode(to);
		}
		if (m_edgeCounter[from][to] == 0) {
			m_g.highlightEdge(from, to, null, new TicksTiming(5));
		}
		m_edgeCounter[from][to]++;
	}

	/**
	 * @see highlightEdge
	 */
	private void unhighlightEdge(int from, int to, boolean highlightNodes) {
		if (highlightNodes) {
			unhighlightNode(from);
			unhighlightNode(to);
		}
		m_edgeCounter[from][to]--;
		if (m_edgeCounter[from][to] == 0) {
			m_g.unhighlightEdge(from, to, null, new TicksTiming(5));
		}
	}

	/**
	 * Removes any existing text from the explanation text area (under header)
	 */
	private void clearExplanation() {
		if (m_exp != null)
			m_exp.hide();
		m_exp = null;
	}

	/**
	 * Adds a line of text to the explanation text area (under header)
	 */
	private void addExplanationLine(String line) {
		if (m_exp == null) {
			m_exp = m_l.newSourceCode(POS_EXP, "exp", null, Explanation_Props);
		}
		m_exp.addCodeLine(line, null, 0, null);
	}

	private void incrementNumVisitedNodes() {
		m_counterVisitedNodesValue++;
		m_counterVisitedNodes.setText("# visited nodes: " + m_counterVisitedNodesValue, null, null);
	}

	private void updateCurrDepth(int g, int bound) {
		m_counterDepth.setText("Current cost (g): " + g, null, null);
		m_counterMaxDepth.setText("Max. cost (bound): " + bound, null, null);
	}

	/*
	private String nodeListToString(List<Integer> nodes) {
		String res = "";
		for (int n : nodes) {
			if (!res.isEmpty())
				res += ", ";
			res += m_g.getNodeLabel(n);
		}
		return "{" + res + "}";
	}*/

	/**
	 * Convenience main method to generate AnimalScript code for IDA* directly with pre-set parameters
	 */
	/*
	public static void main(String[] argv) throws FileNotFoundException {
		IDAStar ida = new IDAStar();
		ida.init();
		Language l = ida.m_l;

		int[][] adjacencyMatrix = new int[][] { {0, 1, 1, 1, 0, 0}, {0, 0, 1, 0, 1, 0}, {0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 1, 0}, {0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0} };
		Point graphPos = l.newPoint(POS_G, "graphPos", null, new PointProperties());
		Node[] nodes = new Node[] {
				new Offset(420, 152, graphPos, "SW"),
				new Offset(30, 30, graphPos, "SW"),
				new Offset(420, 30, graphPos, "SW"),
				new Offset(332, 200, graphPos, "SW"),
				new Offset(30, 200, graphPos, "SW"),
				new Offset(420, 300, graphPos, "SW") };
		String[] labels = new String[] { "A", "B", "C", "D", "E", "F" };

		AnimationPropertiesContainer aps = new AnimationPropertiesContainer();
		GraphProperties gProps = new GraphProperties("G_Props");
		gProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.white);
		gProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.red);
		gProps.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, true);
		gProps.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, false);
		aps.add(gProps);

		Hashtable<String, Object> primitives = new Hashtable<String, Object>();

		GraphProperties graphProps = new GraphProperties();
		Graph g = l.newGraph("G", adjacencyMatrix, nodes, labels, null, graphProps);
		g.setStartNode(g.getNode(0));
		g.setTargetNode(g.getNode(5));
		g.hide();
		primitives.put("graph", g);
		primitives.put("showSubStepsAfterFirstIteration", new Boolean(false));


		PrintWriter out = new PrintWriter(argv[0]);
		out.println(ida.generate(aps, primitives));
		out.close();
	}*/
}