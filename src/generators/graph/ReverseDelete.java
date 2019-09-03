package generators.graph;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.TrueFalseQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Graph;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;

/**
 * 
 * @author Haouami Farouk,El bouabidi Fethi
 * 
 */

// REMARK!!!!
// interaction : always an incorrect answer!!

public class ReverseDelete implements Generator {
	private TextProperties titleProps;
	private SourceCodeProperties pseudoCodeProps;
	private SourceCodeProperties introProps;
	private SourceCodeProperties edgesListProps;
	private SourceCodeProperties helpTextProps;
	private SourceCodeProperties ComplexityExpProps;
	private Language lang;

	private GraphProperties gprops = getDefaultGraphProperties();
	private int[][] adjMatrix;
	private Graph graph;

	/**
	 * Constructor
	 */
	public ReverseDelete() {
		lang = new AnimalScript("Reverse Delete",
				"El Fethi Bouabidi,Haouami Farouk", 800, 600);

	}

	/**
	 * Initialization
	 */
	public void init() {
		lang = new AnimalScript("Reverse Delete ",
				"El Fethi Bouabidi,Haouami Farouk", 800, 600);
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
	}

	/**
 * 
 */
	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {

		graph = (Graph) primitives.get("graph");
		gprops = (GraphProperties) props.getPropertiesByName("graphProps");
		introProps = (SourceCodeProperties) props
				.getPropertiesByName("introProp");
		introProps.set("font", new Font("SansSerif", 1, 14));
		pseudoCodeProps = (SourceCodeProperties) props
				.getPropertiesByName("pseudoCodeProp");
		pseudoCodeProps.set("font", new Font("SansSerif", 1, 14));

		edgesListProps = (SourceCodeProperties) props
				.getPropertiesByName("edgesListProp");
		
		helpTextProps = (SourceCodeProperties) props
				.getPropertiesByName("helpTextProps");
		
		ComplexityExpProps= (SourceCodeProperties) props
				.getPropertiesByName("ComplexityExpProps");
		
		
	titleProps = (TextProperties) props
				.getPropertiesByName("titleProps");
		
		
		start();
		reverseDelete(graph);

		return lang.toString();
	}

	/**
	 * Name
	 */
	public String getName() {
		return "Reverse Delete";
	}

	/**
	 * return Name Algorithm
	 */
	public String getAlgorithmName() {
		return "Reverse Delete";
	}

	/**
	 * Author Namen
	 */

	public String getAnimationAuthor() {
		return "El Fethi Bouabidi,Farouk Haouami";
	}

	/**
	 * Description of Algorithm
	 */
	private static final String PESENTATION = "The reverse-delete algorithm is an algorithm in graph theory used to"
			+ "obtain a minimum spanning tree from a given connected, edge-weighted graph. "
			+ "It first appeared in Kruskal (1956), but it should not be confused with Kruskal's #"
			+ "algorithm which appears in the same paper. If the graph is disconnected, this algorithm will "
			+ "find a minimum spanning tree for each disconnected part of the graph. "
			+ "The set of these minimum spanning trees is called a minimum spanning forest, "
			+ "which contains every vertex in the graph.";

	public static final String PRESENTATION2 = " "
			+ "This algorithm is a greedy algorithm, choosing the best choice given any situation."
			+ " It is the reverse of Kruskal's algorithm, which is another greedy algorithm to find a "
			+ "minimum spanning tree. Kruskal’s algorithm starts with an empty graph and adds edges while "
			+ "the Reverse-Delete algorithm starts with the original graph and deletes edges from it."
			+ " The algorithm works as follows:";

	private static final String PSEUDO_CODE = "function ReverseDelete(edges[] E) "
			+ "\r"
			+ "2    sort E in decreasing order"
			+ "\r "
			+ "3    Define an index i ? 0"
			+ "\r"
			+ "4    while i <- size(E)"
			+ "\r"
			+ " 5       Define edge temp <- E[i] "
			+ "\r"
			+ " 6         delete E[i]"
			+ "\r "
			+ "7         if temp.v1 is not connected to temp.v2"
			+ "\r"
			+ " 8             E[i] <- temp"
			+ "\r "
			+ "9         i <- i + 1"
			+ "\r" + " 10  return edges[] E";

	private static final String CONCLUSION = " The algorithm can be shown to run in O(E log V (log log V)3) time, where E is the number"
			+ " of edges and V is the number of vertices. ";

	public String getDescription() {
		return "The reverse-delete algorithm is an algorithm in graph theory used to"
				+ "obtain a minimum spanning tree from a given connected, edge-weighted graph. "
				+ "It first appeared in Kruskal (1956), but it should not be confused with Kruskal's #"
				+ "algorithm which appears in the same paper. If the graph is disconnected, this algorithm will "
				+ "find a minimum spanning tree for each disconnected part of the graph. "
				+ "The set of these minimum spanning trees is called a minimum spanning forest, ";
	}

	/**
	 * code Example
	 */
	public String getCodeExample() {
		return "function ReverseDelete(edges[] E) " + "\r"
				+ "2 sort E in decreasing order" + "\r"
				+ "3   Define an index i ? 0" + "\r"
				+ "4     while i <- size(E)" + "\r"
				+ "5       Define edge temp <- E[i] " + "\r"
				+ "6         delete E[i]" + "\r"
				+ "7         if temp.v1 is not connected to temp.v2" + "\r"
				+ "8             E[i] <- temp" + "\r" + "9         i <- i + 1"
				+ "\r" + "10 return edges[] E";
	}

	/**
	 * FileExtension
	 */
	public String getFileExtension() {
		return "asu";
	}

	/**
	 * ContentLocale
	 */
	public Locale getContentLocale() {
		return Locale.ENGLISH;
	}

	/**
	 * Generator Type
	 */
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPH);
	}

	/**
	 * OutPut Language
	 */
	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	/**
	 * start the algorithm
	 */
	private void start() {

		lang.setStepMode(true);
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

		Text title = lang.newText(new Coordinates(40, 30), "Reverse Delete:",
				"title", null, titleProps);
		
		title.show();

		SourceCode intro1 = lang.newSourceCode(new Coordinates(50, 70),
				"Presnetation1", null, introProps);

		intro1.addCodeLine("", null, 0, null);

		intro1.addCodeLine(
				"The reverse-delete algorithm is an algorithm in graph theory used to",
				null, 0, null);
		intro1.addCodeLine(
				"obtain a minimum spanning tree from a given connected, edge-weighted graph. .",
				null, 0, null);
		intro1.addCodeLine(
				"It first appeared in Kruskal (1956), but it should not be confused with Kruskal's #",
				null, 0, null);
		intro1.addCodeLine(
				"algorithm which appears in the same paper. If the graph is disconnected, this algorithm will",
				null, 0, null);
		intro1.addCodeLine(
				"find a minimum spanning tree for each disconnected part of the graph",
				null, 0, null);
		intro1.addCodeLine(
				"The set of these minimum spanning trees is called a minimum spanning forest,",
				null, 0, null);

		intro1.addCodeLine("which contains every vertex in the graph.", null,
				0, null);
		lang.nextStep();
		intro1.hide();
		// ///////////////////////////////////////////////////////////////////////
		SourceCode intro2 = lang.newSourceCode(new Coordinates(50, 70),
				"sourceCode", null, introProps);

		intro2.addCodeLine(" ", null, 0, null);

		intro2.addCodeLine(
				"This algorithm is a greedy algorithm, choosing the best choice given any situation..",
				null, 0, null);
		intro2.addCodeLine(" ", null, 0, null);
		intro2.addCodeLine(
				"It is the reverse of Kruskal's algorithm, which is another greedy algorithm to find a",
				null, 0, null);

		intro2.addCodeLine(
				"minimum spanning tree. Kruskal’s algorithm starts with an empty graph and adds edges while",
				null, 0, null);
		intro2.addCodeLine(
				"the Reverse-Delete algorithm starts with the original graph and deletes edges from it..",
				null, 0, null); // 0
		intro2.addCodeLine("The algorithm works as follows", null, 0, null); // 0

		lang.nextStep();
		intro2.hide();

		lang.nextStep();

	}

	/**
	 * /** find minimum spanning tree
	 * 
	 * @param props
	 * @param primitives
	 */
	private void reverseDelete(Graph graph) {

		SourceCode pseudoCode = lang.newSourceCode(new Coordinates(40, 70),
				"sourceCode", null, pseudoCodeProps);

		pseudoCode.addCodeLine("", null, 0, null);
		pseudoCode.addCodeLine("", null, 0, null);
		pseudoCode.addCodeLine("1 function ReverseDelete(List<edges> E)", null,
				0, null);
		pseudoCode.addCodeLine("2      sort E in decreasing order.", "sort", 0,
				null);
		pseudoCode.addCodeLine("3   for each edge in E", "loop", 0, null);
		pseudoCode
				.addCodeLine(
						"4           If there is another path connecting the two vertices of the edge",
						"if", 0, null);
		pseudoCode.addCodeLine("5             delete E[i]", "delete", 0, null);
		pseudoCode.addCodeLine("6             else keep it", "keepIt", 0, null);
		pseudoCode.addCodeLine("7  return E", "return", 0, null);

		Graph g = getDefaultGraph();

		if (graph != null) {
			g = graph;
		}

		// recreate the graph
		int size = g.getSize();
		Node[] nodes = new Node[size];
		String[] nodeLabels = new String[size];
		for (int i = 0; i < size; i++) {

			nodes[i] = g.getNode(i);
			nodeLabels[i] = g.getNodeLabel(i);

		}
		g = lang.newGraph("", g.getAdjacencyMatrix(), nodes, nodeLabels,
				g.getDisplayOptions(), gprops);

		g.moveBy(null, 560, 200, null, null);

		ArrayList<Integer> edgesList = new ArrayList<Integer>();
		int[][] matrix = g.getAdjacencyMatrix();

		for (int i = matrix.length - 1; i >= 0; i--) {
			for (int j = 0; j < matrix.length; j++)

			{
				if (matrix[i][j] != 0)
					edgesList.add(matrix[i][j]);

			}
		}

		Collections.sort(edgesList);
		Collections.reverse(edgesList);


	

		SourceCode helpText = lang.newSourceCode(new Coordinates(1050, 70),
				"sourceCode", null, helpTextProps);

		ArrayList<String> verticesList = new ArrayList<>();
		int temp1;
		String reverse = "";
		String s = "";
		StringBuilder sb = new StringBuilder();
		for (int q = 0; q < edgesList.size(); q++) {

			temp1 = edgesList.get(q);

			for (int i = 0; i < matrix.length; i++) {
				for (int j = 0; j < matrix.length; j++) {

					if (temp1 == matrix[i][j]) {

						s = (Integer.toString(i) + Integer.toString(j));
						reverse = sb.append(s).reverse().toString();

						if (!verticesList.contains(s)
								&& !verticesList.contains(reverse)) {
							verticesList.add(Integer.toString(i)
									+ Integer.toString(j));

						}
					}
				}

			}
		}
		
		
		// visualization of the  Edges List
		SourceCode E = lang.newSourceCode(new Coordinates(40, 500), "E", null,
				edgesListProps);
		
		E.addCodeLine("E : { ", "", 0, null);
		for (int i = 0; i < edgesList.size(); i++) {

			if (i == edgesList.size() - 1) {
				E.addCodeElement(
						"[ "
								+ getAsChar(Character
										.getNumericValue(verticesList.get(i)
												.charAt(0)))
								+ ","
								+ (getAsChar(Character
										.getNumericValue(verticesList.get(i)
												.charAt(1))))  + ","
								+ Integer.toString(edgesList.get(i))+ "]", Integer
								.toString(edgesList.get(i)), 0, null);

			} else {
				E.addCodeElement(
						 "[ "
								+ getAsChar(Character
										.getNumericValue(verticesList.get(i)
												.charAt(0)))
								+ ","
								+ (getAsChar(Character
										.getNumericValue(verticesList.get(i)
												.charAt(1))))  + ","
								+ Integer.toString(edgesList.get(i)) + "]"+ " , ",
						Integer.toString(edgesList.get(i)), 0, null);

			}
		}

		E.addCodeElement(" }", "", 0, null);
		E.hide();
		
		
		
		// Begin 

		int c = 0;

		g.show();

		lang.nextStep("begin");
		pseudoCode.highlight("sort");
		E.show();
		helpText.hide();
		lang.nextStep();
		// Interaction

		TrueFalseQuestionModel deleteOrNot = new TrueFalseQuestionModel("don");

		while (c < verticesList.size()) {
			lang.nextStep("Edge [ "
					+ getAsChar(Character.getNumericValue(verticesList.get(c)
							.charAt(0)))
					+ ","
					+ (getAsChar(Character.getNumericValue(verticesList.get(c)
							.charAt(1)))) + "]");
			pseudoCode.unhighlight("sort");
			pseudoCode.highlight("loop");
			pseudoCode.unhighlight("keepIt");

			if (doesItDisconnect(edgesList, verticesList, c)) {

				helpText.addCodeLine(
						"The next  Edge  to check is ["
								+ getAsChar(Character
										.getNumericValue(verticesList.get(c)
												.charAt(0)))
								+ ","
								+ getAsChar(Character
										.getNumericValue(verticesList.get(c)
												.charAt(1))) + "]", null, 0,
						null);

				g.highlightEdge(Character.getNumericValue(verticesList.get(c)
						.charAt(0)), Character.getNumericValue(verticesList
						.get(c).charAt(1)), null, null);

				E.highlight(Integer.toString(edgesList.get(c)));
				g.highlightNode(
						Character.getNumericValue(verticesList.get((c)).charAt(
								0)), null, null);
				g.highlightNode(Character.getNumericValue(verticesList.get(c)
						.charAt(1)), null, null);

				helpText.addCodeLine(
						"Since it would disconnect the graph it will not be deleted",
						null, 0, null);
				pseudoCode.highlight("keepIt");

				lang.nextStep();
				E.unhighlight(Integer.toString(edgesList.get(c)));
				pseudoCode.unhighlight("keepIt");

				helpText.hide();
				helpText = lang.newSourceCode(new Coordinates(1050, 70),
						"sourceCode", null, helpTextProps);

			}

			else if (!doesItDisconnect(edgesList, verticesList, c)) {

				// prompt a question
				if (c == 3) {
					deleteOrNot
							.setPrompt("would the Edge "
									+ "[ "
									+ getAsChar(Character
											.getNumericValue(verticesList
													.get(c).charAt(0)))
									+ ","
									+ (getAsChar(Character
											.getNumericValue(verticesList
													.get(c).charAt(1)))) + "]"
									+ " be deleted?");

					deleteOrNot.setCorrectAnswer(doesItDisconnect(edgesList,
							verticesList, c));
					lang.addTFQuestion(deleteOrNot);
				}
				helpText.addCodeLine(

						"The next edge to check is [ "
								+ getAsChar(Character
										.getNumericValue(verticesList.get(c)
												.charAt(0)))
								+ ","
								+ (getAsChar(Character
										.getNumericValue(verticesList.get(c)
												.charAt(1)))) + "]", null, 0,
						null);

				g.highlightEdge(Character.getNumericValue(verticesList.get(c)
						.charAt(0)), Character.getNumericValue(verticesList
						.get(c).charAt(1)), null, null);

				E.highlight(Integer.toString(edgesList.get(c)));
				g.highlightNode(
						Character.getNumericValue(verticesList.get((c)).charAt(
								0)), null, null);
				g.highlightNode(Character.getNumericValue(verticesList.get(c)
						.charAt(1)), null, null);

				lang.nextStep();

				pseudoCode.highlight("delete");
				pseudoCode.highlight("if");
				E.unhighlight(Integer.toString(edgesList.get(c)));
				g.hideEdge(Character.getNumericValue(verticesList.get(c)
						.charAt(0)), Character.getNumericValue(verticesList
						.get(c).charAt(1)), null, null);

				helpText.addCodeLine(
						"Deleting this edge will not further disconnect the graph",
						"", 0, null);

				helpText.addCodeLine(
						"Edge [ "
								+ getAsChar(Character
										.getNumericValue(verticesList.get(c)
												.charAt(0)))
								+ ","
								+ (getAsChar(Character
										.getNumericValue(verticesList.get(c)
												.charAt(1)))) + "]", null, 0,
						null);
				helpText.addCodeLine("will be DELETED!! ", "delete", 0, null);
				helpText.highlight("delete");

				lang.nextStep();

				pseudoCode.unhighlight("if");
				pseudoCode.unhighlight("delete");
				pseudoCode.unhighlight("delete");
				pseudoCode.unhighlight("if");
				g.unhighlightNode(Character.getNumericValue(verticesList.get(c)
						.charAt(0)), null, null);
				g.unhighlightNode(Character.getNumericValue(verticesList.get(c)
						.charAt(1)), null, null);
				g.unhighlightEdge(Character.getNumericValue(verticesList.get(c)
						.charAt(0)), Character.getNumericValue(verticesList
						.get(c).charAt(1)), null, null);

				helpText.hide();
				helpText = lang.newSourceCode(new Coordinates(1050, 70),
						"sourceCode", null, helpTextProps);

				edgesList.remove(c);
				verticesList.remove(c);
				c--;
			}
			c++;
			lang.nextStep();

		}

		pseudoCode.unhighlight("keepIt");
		pseudoCode.unhighlight("loop");
		pseudoCode.highlight("return");

		lang.nextStep();
		// Conclusion
		pseudoCode.hide();
		E.hide();
		int some = 0;
		for (int i = 0; i < edgesList.size(); i++) {
			some += edgesList.get(i);
		}

		SourceCodeProperties resultProps = new SourceCodeProperties();
		resultProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
		SourceCode result = lang.newSourceCode(new Coordinates(100, 100),
				"source", null, resultProps);

		result.addCodeLine(
				"the minmum spanning tree of the given Graph  has the weight : "
						+ some, "result", 0, null);
		result.highlight("result");

		// Complexity:

		SourceCode complexity = lang.newSourceCode(new Coordinates(1000, 100),
				"complexity", null, ComplexityExpProps);

		complexity
				.addCodeLine(
						"The algorithm can be shown to run in O(E log V (log log V)3) time,",
						"1", 0, null);
		complexity.addCodeLine("", "1", 0, null);
		complexity.addCodeLine("This bound is achieved as follows:,", "1", 0,
				null);
		complexity
				.addCodeLine(
						"* sorting the edges by weight using a comparison sort in O(E log E) time",
						"1", 0, null);
		complexity.addCodeLine("* E iterations of loop", "1", 0, null);
		complexity.addCodeLine("* deleting in O(1) time", "1", 0, null);

		lang.nextStep("Fazit");

		lang.finalizeGeneration();

	}

	/**
	 * 
	 * @param verticesList
	 * @param vertix
	 * @param index
	 * @return List of vertices connected to the given vertex
	 */
	private ArrayList<Character> getConnectedVertices(
			ArrayList<String> verticesList, char vertix, int index) {

		ArrayList<Character> connectedToVertex = new ArrayList<>();

		for (int c = 0; c < verticesList.size(); c++) {

			if (c != index) {

				if (verticesList.get(c).indexOf(Character.toString(vertix)) == 1)
					connectedToVertex.add(verticesList.get(c).charAt(0));
				else if (verticesList.get(c)
						.indexOf(Character.toString(vertix)) == 0)
					connectedToVertex.add(verticesList.get(c).charAt(1));
			}
		}

		return connectedToVertex;

	}

	/**
	 * 
	 * @param verticesList
	 * @param vertex
	 * @return reachable vertices from the given vertex
	 */
	private ArrayList<Character> getReachableVertices(
			ArrayList<String> verticesList, char vertex, int index) {

		ArrayList<Character> connectedToVertex = getConnectedVertices(
				verticesList, vertex, index);

		ArrayList<Character> reachableFromVertex = new ArrayList<>();

		reachableFromVertex.addAll(connectedToVertex);

		ArrayList<Character> temp = new ArrayList<>();

		for (char v : connectedToVertex) {
			temp = getConnectedVertices(verticesList, v, -1);

			for (char x : temp)
				if (!reachableFromVertex.contains(x))
					reachableFromVertex.add(x);

		}

		return reachableFromVertex;

	}

	/**
	 * 
	 * @param edgesList
	 * @param verticesList
	 * @param index
	 *            of the selected edge
	 * @return true if the graph will disconnect if we delete the selected
	 *         edge,false otherwise
	 */

	private boolean doesItDisconnect(ArrayList<Integer> edgesList,
			ArrayList<String> verticesList, int index) {

		boolean disconnect = true;
		char firstVertix = verticesList.get(index).charAt(0);
		char secondVertix = verticesList.get(index).charAt(1);

		ArrayList<Character> reachableFromFV = getReachableVertices(
				verticesList, firstVertix, index);
		ArrayList<Character> reachableFromSV = getReachableVertices(
				verticesList, secondVertix, index);

		if (reachableFromFV.isEmpty() || reachableFromSV.isEmpty()) {
			disconnect = true;
			return disconnect;
		}

		for (char cc : reachableFromFV) {
			if (reachableFromSV.contains(cc)) {
				disconnect = false;
				return disconnect;
			}
		}

		return disconnect;

	}

	/**
	 * get char
	 * 
	 * @param ind
	 * @return retChar
	 */
	public char getAsChar(int ind) {
		char retChar = (char) (65 + ind);
		return retChar;
	}

	/**
	 * main method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		ReverseDelete b = new ReverseDelete();

		if (b.gprops == null)
			b.gprops = getDefaultGraphProperties();

		Hashtable<String, Object> primitives = new Hashtable<String, Object>();
		AnimationPropertiesContainer props = new AnimationPropertiesContainer();

		b.generate(props, primitives);

		System.out.println(b.lang);

	}

	/**
	 * 
	 * @return
	 */
	public static GraphProperties getDefaultGraphProperties() {

		GraphProperties graphProps = new GraphProperties();
		graphProps.set(AnimationPropertiesKeys.EDGECOLOR_PROPERTY, Color.black);
		graphProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
				Color.green);
		graphProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.white);
		graphProps
				.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.red);

		graphProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
				Color.green);
		graphProps.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, true);

		return graphProps;

	}

	/**
	 * 
	 * @param matrix
	 * @return
	 */
	public int getNumEdges(int[][] matrix) {
		int counter = 0;

		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				if (i != j && matrix[i][j] != 0 && i < j) {
					counter++;
				}

			}
		}
		return counter;
	}

	/**
	 * @param
	 * @return AdjMatrix (Example in Wikipedia)
	 */
	private Graph getDefaultGraph() {
		int[][] adjMatrix = null;

		adjMatrix = new int[7][7];
		for (int i = 0; i < adjMatrix.length; i++) {
			for (int j = 0; j < adjMatrix.length; j++) {

				adjMatrix[i][j] = 0;

			}
		}

		adjMatrix[0][1] = 7;
		adjMatrix[0][3] = 5;

		adjMatrix[1][2] = 8;
		adjMatrix[1][3] = 9;
		adjMatrix[1][4] = 7;

		adjMatrix[2][4] = 5;

		adjMatrix[3][4] = 15;
		adjMatrix[3][5] = 6;

		adjMatrix[4][5] = 8;
		adjMatrix[4][6] = 9;

		adjMatrix[5][6] = 11;

		Node[] graphNodes = new Node[adjMatrix.length];
		String[] nodeLabels = new String[] { "A", "B", "C", "D", "E", "F", "G" };

		graphNodes[0] = new Coordinates(100, 100);
		graphNodes[1] = new Coordinates(300, 150);
		graphNodes[2] = new Coordinates(450, 100);
		graphNodes[3] = new Coordinates(120, 300);
		graphNodes[4] = new Coordinates(400, 250);
		graphNodes[5] = new Coordinates(250, 340);
		graphNodes[6] = new Coordinates(340, 370);

		Graph g = lang.newGraph("graph", adjMatrix, graphNodes, nodeLabels,
				null, getDefaultGraphProperties());
		g.hide();

		return g;

	}
}
