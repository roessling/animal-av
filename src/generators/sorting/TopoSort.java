/*
 * TopoSort.java
 * Matthias Schultheis, 2015 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.sorting;

import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.Color;
import java.awt.Font;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import algoanim.primitives.generators.Language;

import java.util.Hashtable;

import generators.framework.properties.AnimationPropertiesContainer;
import generators.network.helper.ClassName;
import generators.sorting.ittopsort.ProgressBar;
import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.addons.CodeView;
import algoanim.animalscript.addons.Slide;
import algoanim.animalscript.addons.bbcode.NetworkStyle;
import algoanim.animalscript.addons.bbcode.Style;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.primitives.Graph;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

/**
 * @author Matthias Schultheis
 * 
 *         generates an animation showing the functionality of the iterative
 *         topological sorting algorithm
 */
public class TopoSort implements Generator {

	private Language lang;

	private Graph graph;
	private int[][] graphAdjacencyMatrix;

	private Style style;

	// handles text in different languages
	private ResourceBundle bundle;
	private Locale locale;

	// animation size
	private static final int ANIM_WIDTH = 1024;
	private static final int ANIM_HEIGHT = 768;

	// resource settings
	private static final String BUNDLE_STR_FILE = "generators.sorting.ittopsort.Strings";
	private String RESOURCES_PATH;

	// properties
	private TextProperties propHeader;
	private TextProperties propSubHeader;
	private TextProperties propRemark;
	private TextProperties propLabels;
	private TextProperties propText;
	private ArrayProperties propL;
	private ArrayProperties propS;
	private SourceCodeProperties propSourceCode;

	// primitives
	private StringArray lArray;
	private StringArray sArray;

	private Text status;

	private final String[] emptyStringArray = { " " };

	private Text textNumOuterIter;
	private Text textNumInnerIter;
	private Text textProgress;

	private ProgressBar progressBar;

	private int counterCreatedL;
	private int counterCreatedS;

	public TopoSort(Locale l) {
		RESOURCES_PATH = ClassName.getPackageAsPath(this) + "ittopsort/";

		this.locale = l;

		try {
			bundle = ResourceBundle.getBundle(BUNDLE_STR_FILE, locale);
		} catch (MissingResourceException e) {
		  System.err.println("Resource not found " +RESOURCES_PATH +"" +l);
		  //lang.addError("TopSort: Resource file not found");
		}
	}

	public TopoSort() {
		this(new Locale("en"));
	}

	/**
	 * initializes the animation
	 */
	public void init() {
		lang = new AnimalScript("Topological Sorting", "Matthias Schultheis",
				ANIM_WIDTH, ANIM_HEIGHT);
		lang.setStepMode(true);

		counterCreatedL = 0;
		counterCreatedS = 0;

		style = new NetworkStyle();
		initProperties();
	}

	private void initProperties() {

		// texts
		propText = new TextProperties();
		propText.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.PLAIN, 16));

		// labels
		propLabels = new TextProperties();
		propLabels.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.BOLD, 20));
	}

	/**
	 * starts the animation
	 */
	public void start() {

		graph.hide();

		int[][] am = graph.getAdjacencyMatrix();
		int numTrans = am.length;
		int numItems = am[0].length;

		graphAdjacencyMatrix = new int[numTrans][numItems];

		for (int i1 = 0; i1 < numTrans; i1++) {
			for (int j = 0; j < numItems; j++) {
				if (am[i1][j] > 0) {
					graphAdjacencyMatrix[i1][j] = 1;
				}
			}
		}

		// header
		lang.newText(new Coordinates(20, 30), bundle.getString("header"),
				"header", null, propHeader);

		// draw rectangle around header
		RectProperties rectProps = new RectProperties();
		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		lang.newRect(new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW),
				new Offset(5, 5, "header", "SE"), "hRect", null, rectProps);

		lang.nextStep(bundle.getString("chapIntro"));

		// show introduction
		Text subtitle = lang.newText(new Offset(0, 20, "header",
				AnimalScript.DIRECTION_SW), bundle.getString("titleIntro"),
				"subtitle", null, propSubHeader);

		Slide titleSlide = null;
		try {
			titleSlide = new Slide(lang, RESOURCES_PATH
					+ bundle.getString("introTextFile"), "subtitle", style);
		} catch (java.lang.NullPointerException e) {
			lang.addError("introduction text file \""
					+ bundle.getString("introTextFile")
					+ "\" could not be found in " + RESOURCES_PATH);
		}

		// show description
		subtitle.setText(bundle.getString("titleDescription"), null, null);

		if (titleSlide != null)
			titleSlide.hide();

		try {
			titleSlide = new Slide(lang, RESOURCES_PATH
					+ bundle.getString("descriptionTextFile"), "subtitle",
					style);
		} catch (java.lang.NullPointerException e) {
			lang.addError("description text file \""
					+ bundle.getString("descriptionTextFile")
					+ "\" could not be found in " + RESOURCES_PATH);
		}

		// next step: hide Slide
		subtitle.hide();
		if (titleSlide != null)
			titleSlide.hide();

		// show graph
		graph.show();

		// show sourcecode
		SourceCode source = lang.newSourceCode(new Offset(100, 0, "graph",
				AnimalScript.DIRECTION_NE), "sourceCode", null, propSourceCode);

		source.addCodeLine(bundle.getString("sc1"), null, 0, null);
		source.addCodeLine(bundle.getString("sc2"), null, 0, null);
		source.addCodeLine(bundle.getString("sc3"), null, 0, null);
		source.addCodeLine(bundle.getString("sc4"), null, 1, null);
		source.addCodeLine(bundle.getString("sc5"), null, 1, null);
		source.addCodeLine(bundle.getString("sc6"), null, 1, null);
		source.addCodeLine(bundle.getString("sc7"), null, 2, null);
		source.addCodeLine(bundle.getString("sc8"), null, 2, null);
		source.addCodeLine(bundle.getString("sc9"), null, 3, null);
		source.addCodeLine(bundle.getString("sc10"), null, 0, null);
		source.addCodeLine(bundle.getString("sc11"), null, 1, null);
		source.addCodeLine(bundle.getString("sc12"), null, 0, null);
		source.addCodeLine(bundle.getString("sc13"), null, 1, null);

		// initalize primitives

		// statusBox
		status = lang.newText(new Offset(20, 20, "graph",
				AnimalScript.DIRECTION_SW), "", "status", null, propText);

		// list L
		Text textL = lang.newText(new Offset(0, 35, "sourceCode",
				AnimalScript.DIRECTION_SW), "L =", "textL", null, propLabels);
		lArray = lang.newStringArray(new Offset(5, 0, "textL",
				AnimalScript.DIRECTION_NE), emptyStringArray, "arrayL", null,
				propL);
		textL.hide();
		lArray.hide();

		// list S
		Text textS = lang.newText(new Offset(0, 20, "textL",
				AnimalScript.DIRECTION_SW), "S =", "textS", null, propLabels);
		sArray = lang.newStringArray(new Offset(5, 0, "textS",
				AnimalScript.DIRECTION_NE), emptyStringArray, "arrayS", null,
				propS);
		textS.hide();
		sArray.hide();

		// Progress
		textProgress = lang.newText(new Offset(0, 30, "textS",
				AnimalScript.DIRECTION_SW), "", "textProgress", null,
				propRemark);

		progressBar = new ProgressBar(lang, new Offset(0, 10, "textProgress",
				AnimalScript.DIRECTION_SW), 250, 20, "progbar");
		showProgress(0, graphAdjacencyMatrix.length);

		// Number of iterations
		int counterOuterIter = 0;
		textNumOuterIter = lang.newText(new Offset(0, 60, "textProgress",
				AnimalScript.DIRECTION_SW), "", "textNumOuterIter", null,
				propRemark);
		showOuterIterationCounter(0);

		int counterInnerIter = 0;
		textNumInnerIter = lang.newText(new Offset(0, 10, "textNumOuterIter",
				AnimalScript.DIRECTION_SW), "", "textNumInnerIter", null,
				propRemark);
		showInnerIterationCounter(0);

		lang.nextStep(bundle.getString("chapRunAlgo"));

		// first line: initialize L
		// show first line
		source.highlight(0);

		// initialize and show L
		LinkedList<Integer> l = new LinkedList<Integer>();
		lArray.show();
		textL.show();
		lang.nextStep();

		// 2nd line: initialize S
		source.toggleHighlight(0, 1);

		// show S
		LinkedList<Integer> s = getNodesWithoutIncomingEdges(graphAdjacencyMatrix);
		showSArray(s);
		textS.show();
		lang.nextStep();

		// 3rd line: Outer loop
		source.toggleHighlight(1, 2);

		while (!s.isEmpty()) {

			setStatusText("stSNonEmpty");

			counterOuterIter++;
			showOuterIterationCounter(counterOuterIter);

			lang.nextStep(counterOuterIter + ". Iteration");

			// 4th line: remove node from s
			source.toggleHighlight(2, 3);
			sArray.highlightElem(0, 0, null, null);

			// remove node
			int n = s.removeFirst();

			showSArray(s);
			graph.highlightNode(n, null, null);
			setStatusText("stRemS", graph.getNodeLabel(n));

			lang.nextStep();

			// 5th line: add n to L
			source.toggleHighlight(3, 4);
			setStatusText("stAddNToTail", graph.getNodeLabel(n));

			// add node to L
			l.add(n);

			showLArray(l);
			lArray.highlightElem(lArray.getLength() - 1, null, null);
			lang.nextStep();

			// 6th line: successors
			List<Integer> succs = getSuccessors(graphAdjacencyMatrix, n);
			source.toggleHighlight(4, 5);

			for (int succ : succs) {
				// modify iteration counter and status text
				counterInnerIter++;
				showInnerIterationCounter(counterInnerIter);
				setStatusText("stConsiderSuccessor", graph.getNodeLabel(succ),
						graph.getNodeLabel(n));

				// highlight successor
				graph.highlightNode(graph.getNode(succ), null, null);
				graph.highlightEdge(graph.getNode(n), graph.getNode(succ),
						null, null);
				lang.nextStep();

				// 7th line: remove edge
				source.toggleHighlight(5, 6);
        setStatusText("stRemEdge", graph.getNodeLabel(n), graph.getNodeLabel(succ));

				// removing edges is durable for next run, so modify matrix copy
				graphAdjacencyMatrix[n][succ] = 0;
				// graph.setEdgeWeight(graph.getNode(n), graph.getNode(succ), 1,
				// null, null);
				graph.hideEdge(graph.getNode(n), graph.getNode(succ), null,
						null);
				graph.hideNode(graph.getNode(n), null, null);
				lang.nextStep();

				// 8th line: if no incoming edges
				source.toggleHighlight(6, 7);

				if (getNumberOfPredecessors(graphAdjacencyMatrix, succ) == 0) {
					setStatusText("stNoIncomingEdges", graph.getNodeLabel(succ));
					lang.nextStep();

					// 9th line: add successor to S
           setStatusText("stInsertIntoS", graph.getNodeLabel(succ));
					source.toggleHighlight(7, 8);

					s.add(succ);
					showSArray(s);
					lang.nextStep();

					source.unhighlight(8);
				} else {
					setStatusText("stIncomingEdges", graph.getNodeLabel(succ));
					lang.nextStep();

					source.unhighlight(7);
				}

				// tidy up for next iteration/end of for loop
				graph.unhighlightNode(graph.getNode(succ), null, null);
				graph.unhighlightEdge(graph.getNode(n), graph.getNode(succ),
						null, null);
				source.highlight(5);
			}

			// all successors have been worked through
			setStatusText("stNoSuccessors", graph.getNodeLabel(n));
			lang.nextStep();

			// tidy up for next iteration/end of while loop
			// -> unhighlight nodes and highlight line 3 again
			graph.unhighlightNode(graph.getNode(n), null, null);
			lArray.unhighlightElem(lArray.getLength() - 1, null, null);
			source.toggleHighlight(5, 2);

			// reset counter
			counterInnerIter = 0;
			showInnerIterationCounter(counterInnerIter);

			// update progress
			showProgress(l.size(), graphAdjacencyMatrix.length);
		}

		// we have finished so we can hide the graph
		graph.hide();

		setStatusText("stSEmpty");
		lang.nextStep();

		// finally check if successful <=> no edges left
		boolean success = !hasEdges(graphAdjacencyMatrix);
		source.toggleHighlight(2, 9);

		String conclusionText = null; // for later
		if (!success) {
			conclusionText = bundle.getString("conclusionFailure");
			setStatusText("stEndEdgesLeft");
			lang.nextStep();

			// show that it was a failure
			source.toggleHighlight(9, 10);
			setStatusText("stEndFailure");
		} else {
			// create conclusion text
			String result = "[";
			for (int n : l) {
				result += graph.getNodeLabel(n) + ", ";
			}
			if (result.length() > 1)
				result = result.substring(0, result.length() - 2);
			result = result + "]";

			conclusionText = getReplacedBundleText("conclusionSuccess", result);
			setStatusText("stEndNoEdgesLeft");
			lang.nextStep();

			// show line "else"
			source.toggleHighlight(9, 11);
			lang.nextStep();

			// show that it was a success
			setStatusText("stEndSuccess");
			source.toggleHighlight(11, 12);
		}

		lang.nextStep("Conclusion");

		// hide all animation objects
		graph.hide();
		source.hide();
		progressBar.hide();
		status.hide();
		lArray.hide();
		sArray.hide();
		textS.hide();
		textL.hide();
		textProgress.hide();
		textNumInnerIter.hide();
		textNumOuterIter.hide();

		// show conclusion
		subtitle.show();
		subtitle.setText(bundle.getString("titleConclusion"), null, null);
		lang.newText(new Offset(0, 20, "subtitle", AnimalScript.DIRECTION_SW),
				conclusionText, "conclusion", null, propText);
		lang.newText(
				new Offset(0, 20, "conclusion", AnimalScript.DIRECTION_SW),
				getReplacedBundleText("conclusionNumIterations",
						Integer.toString(counterOuterIter)), "conclusion2",
				null, propText);
    lang.newText(
				new Offset(0, 20, "conclusion2", AnimalScript.DIRECTION_SW),
				getReplacedBundleText("conclusionComplexity1"), "conclusion3",
				null, propText);
    lang.newText(
				new Offset(0, 5, "conclusion3", AnimalScript.DIRECTION_SW),
				getReplacedBundleText("conclusionComplexity2"), "conclusion4",
				null, propText);

		// the end
		lang.nextStep();
	}

	/**
	 * shows the progress of nodes worked through by updating the textfield and
	 * the progressbar
	 * 
	 * @param port
	 *            the portion/number of nodes worked through
	 * @param total
	 *            the total amount of nodes
	 */
	private void showProgress(int port, int total) {
		textProgress.setText(
				getReplacedBundleText("lblProgress", Integer.toString(port),
						Integer.toString(total)), null, null);
		progressBar.setProgress(port * ProgressBar.MAX_PROGRESS / total);
	}

	/**
	 * updates the iteration counter for the outer loop
	 * 
	 * @param count
	 *            the counter value
	 */
	private void showOuterIterationCounter(int count) {
		if (count == 0)
			textNumOuterIter.setText("", null, null);
		else
			textNumOuterIter.setText(
					getReplacedBundleText("lblOuterIteration",
							Integer.toString(count)), null, null);
	}

	/**
	 * updates the iteration counter for the inner loop
	 * 
	 * @param count
	 *            the counter value
	 */
	private void showInnerIterationCounter(int count) {
		if (count == 0)
			textNumInnerIter.setText("", null, null);
		else
			textNumInnerIter.setText(
					getReplacedBundleText("lblInnerIteration",
							Integer.toString(count)), null, null);
	}

	/**
	 * @param ident
	 *            the identifier for the bundle
	 * @param vars
	 *            the values for replacement
	 * @return the value of ident in the bundle where every ooccurence of "$i"
	 *         is replaced
	 */
	private String getReplacedBundleText(String ident, String... vars) {
		String text = bundle.getString(ident);
		for (int i = 0; i < vars.length; i++) {
			text = text.replaceAll("(?<!\\\\)\\$" + i, vars[i]);
		}

		return text;
	}

	/**
	 * sets the status in the status text field
	 * 
	 * @param ident
	 *            the identifier for the bundle
	 * @param vars
	 *            the values for replacement
	 */
	private void setStatusText(String ident, String... vars) {

		status.setText(getReplacedBundleText(ident, vars), null, null);
	}

	/**
	 * updates and shows the array L
	 * 
	 * @param l
	 *            the list of L
	 */
	private void showLArray(List<Integer> l) {
		String[] array;
		if (!l.isEmpty()) {
			LinkedList<String> ls = new LinkedList<String>();
			for (int n : l)
				ls.add(graph.getNodeLabel(n));

			array = ls.toArray(new String[0]);
		} else {
			array = emptyStringArray;
		}

		lArray.hide();
		lArray = lang.newStringArray(new Offset(5, 0, "textL",
				AnimalScript.DIRECTION_NE), array, "arrayL_" + counterCreatedL,
				null, propL);

		counterCreatedL++;
	}

	/**
	 * updates and shows the array S
	 * 
	 * @param s
	 *            the list of S
	 */
	private void showSArray(List<Integer> s) {
		String[] array;
		if (!s.isEmpty()) {
			LinkedList<String> ls = new LinkedList<String>();
			for (int n : s)
				ls.add(graph.getNodeLabel(n));

			array = ls.toArray(new String[0]);
		} else {
			array = emptyStringArray;
		}

		sArray.hide();
		sArray = lang.newStringArray(new Offset(5, 0, "textS",
				AnimalScript.DIRECTION_NE), array, "arrayS_" + counterCreatedS,
				null, propS);

		counterCreatedS++;
	}

	/**
	 * @param am
	 *            the graph adjacecy matrix
	 * @return a list of all nodes of G which don't have any incoming edges
	 */
	private LinkedList<Integer> getNodesWithoutIncomingEdges(int[][] am) {
		LinkedList<Integer> nodes = new LinkedList<Integer>();
		int size = am.length;
		for (int i = 0; i < size; i++) {
			boolean noIncomingEdge = true;
			for (int j = 0; j < size; j++) {
				if (am[j][i] > 0) {
					noIncomingEdge = false;
					break;
				}
			}

			if (noIncomingEdge)
				nodes.add(i);
		}

		return nodes;
	}

	/**
	 * @param am
	 *            the graph adjacency matrix
	 * @param node
	 *            the node
	 * @return a list of all successors of node in g
	 */
	private LinkedList<Integer> getSuccessors(int[][] am, int node) {
		LinkedList<Integer> successors = new LinkedList<Integer>();
		for (int n = 0; n < am.length; n++) {
			if (am[node][n] > 0) {
				successors.add(n);
			}
		}

		return successors;
	}

	/**
	 * @param am
	 *            the graph adjacecy matrix
	 * @param node
	 *            the node
	 * @return the number of predecessors of node in g
	 */
	private int getNumberOfPredecessors(int[][] am, int node) {
		int num = 0;
		for (int n = 0; n < am.length; n++) {
			if (am[n][node] > 0) {
				num++;
			}
		}

		return num;
	}

	/**
	 * @param am
	 *            the graph adjacecy matrix
	 * @return true iff g has any edges
	 */
	private boolean hasEdges(int[][] am) {
		boolean hasEdges = false;
		for (int n1 = 0; n1 < am.length && !hasEdges; n1++)
			for (int n2 = 0; n2 < am.length; n2++)
				if (am[n1][n2] > 0) {
					hasEdges = false;
					break;
				}

		return hasEdges;
	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> args) {

		propS = (ArrayProperties) props.getPropertiesByName("S");
		propL = (ArrayProperties) props.getPropertiesByName("L");

		propSourceCode = (SourceCodeProperties) props
				.getPropertiesByName("sourceCode");

		propHeader = (TextProperties) props.getPropertiesByName("header");
		propSubHeader = (TextProperties) props.getPropertiesByName("subHeader");
		propRemark = (TextProperties) props.getPropertiesByName("comments");

		propHeader.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.BOLD, 24));

		propSubHeader.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.BOLD, 20));

		propLabels.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.BOLD, 20));

		GraphProperties gp = (GraphProperties) props
				.getPropertiesByName("graphProps");

		Graph g = (Graph) args.get("graph");

		int[][] am = g.getAdjacencyMatrix();

		Node[] graphNodes = new Node[g.getSize()];
		String[] labels = new String[g.getSize()];
		for (int i = 0; i < g.getSize(); i++) {
			graphNodes[i] = g.getNode(i);
			labels[i] = g.getNodeLabel(i);
		}

		gp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		gp.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, true);
		gp.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, false);

		graph = lang.newGraph("graph", am, graphNodes, labels, null, gp);

		start();

		return lang.toString();
	}

	public String getName() {
		return bundle.getString("titleAlgo");
	}

	public String getAlgorithmName() {
		return "TopSort";
	}

	public String getAnimationAuthor() {
		return "Matthias Schultheis";
	}

	public String getDescription() {
		return Slide.getTeaser(RESOURCES_PATH
				+ bundle.getString("introTextFile"));
	}

	public String getCodeExample() {
		return CodeView.exampleFromFile(RESOURCES_PATH
				+ bundle.getString("sourceCodeFile"));
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return locale;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	/*
	 * TO REMOVE LATER
	 */
	public Graph getDefaultGraph(GraphProperties graphProps) {
		// define the edges of the graph
		int[][] graphAdjacencyMatrix = new int[7][7];
		// initialize adjacency matrix with zeros
		for (int i = 0; i < graphAdjacencyMatrix.length; i++)
			for (int j = 0; j < graphAdjacencyMatrix[0].length; j++)
				graphAdjacencyMatrix[i][j] = 0;
		
		// set the edges with the corresponding weights
		graphAdjacencyMatrix[0][2] = 1;
		graphAdjacencyMatrix[1][2] = 1;
		graphAdjacencyMatrix[3][5] = 1;
		graphAdjacencyMatrix[4][5] = 1;
		graphAdjacencyMatrix[5][6] = 1;
		graphAdjacencyMatrix[2][6] = 1;
		// graphAdjacencyMatrix[5][7] = 5;

		// define the nodes and their positions
		Node[] graphNodes = new Node[7];
		graphNodes[0] = new Coordinates(40, 100);
		graphNodes[1] = new Coordinates(40, 250);
		graphNodes[2] = new Coordinates(190, 100);
		graphNodes[3] = new Coordinates(190, 250);
		graphNodes[4] = new Coordinates(340, 100);
		graphNodes[5] = new Coordinates(340, 250);
		graphNodes[6] = new Coordinates(340, 400);

		// define the names of the nodes
		String[] labels = { "A", "B", "C", "D", "E", "F", "G" };

		Graph g = lang.newGraph("graph", graphAdjacencyMatrix, graphNodes,
				labels, null, graphProps);

		return g;
	}
}