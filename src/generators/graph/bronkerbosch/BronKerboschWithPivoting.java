/*
 * BronKerboschWithPivoting.java Marco Casili, Igor Cherepanov, 2014 for the
 * Animal project at TU Darmstadt. Copying this file for educational purposes is
 * permitted without further authorization.
 */
package generators.graph.bronkerbosch;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Font;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Graph;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.GraphProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;

// test
public class BronKerboschWithPivoting implements Generator {

	private Language lang;
	private GraphProperties graphProps;
	private Graph graph;
	private SourceCodeProperties sourceCodeProps;
	private SourceCode src;
	private SourceCode recursionTree;
	private SourceCode initialText;
	private int recursionTreeCounter = 0;
	private Text header;
	private TextProperties textProps;
	private Text nachbarschaftLabel;
	private Text mengePLabel;
	private Text mengeRLabel;
	private Text mengeXLabel;
	private Text infoLine1Text;
	private Text infoLine2Text;
	private Text infoLine3Text;
	private Text infoLine4Text;
	private Text infotext;
	private Text resultLabel;
	private Text resultText;
	private HashSet<String> results = new HashSet<String>();
	private Object vertexLabel;
	private Text vertexValue;
	private Text pivotLabel;
	private Text pivotValue;
	private Text endSite;
	private int statisticRecursionCounter;
	private long endtime;
	private long starttime;
	private int statisticsTotalCliquesFound;
	private int differentCliques;
	private Text counterSchnittmengeLabel;
	private Text counterVereinigungLabel;
	private Text counterDifferenzLabel;
	private Text counterSchnittmengeValue;
	private Text counterVereinigungValue;
	private Text counterDifferenzValue;
	private int counterSchnittmenge;
	private int counterVereinigung;
	private int counterDifferenz;
	private SourceCodeProperties recursionTreeProp;
	private SourceCodeProperties infoPageProp;
	private TextProperties statisticProp;


	public void init() {
		lang = new AnimalScript("Bron-Kerbosch", "Marco Casili, Igor Cherepanov", 800, 600);

		TextProperties headerProps = new TextProperties();
		headerProps.set("font", new Font("SansSerif", 1, 24));
		this.header = this.lang.newText(new Coordinates(20, 30), getName(),
				"header", null, headerProps);

		this.lang.setStepMode(true);
	}


	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		graphProps = (GraphProperties) props.getPropertiesByName("graphProps");
		graph = (Graph) primitives.get("graph");

		this.sourceCodeProps = ((SourceCodeProperties) props.getPropertiesByName("sourceCode"));
		this.recursionTreeProp = ((SourceCodeProperties) props.getPropertiesByName("recursionTree"));
		this.infoPageProp = ((SourceCodeProperties) props.getPropertiesByName("infoPage"));
		this.statisticProp = ((TextProperties) props.getPropertiesByName("statistic"));
		this.textProps = ((TextProperties) props.getPropertiesByName("text"));
		TextProperties textProps2 = (TextProperties) props.getPropertiesByName("infoText");

		buildUndirectedGraph();

		showInitPage();
		this.lang.nextStep("Init Page");
		this.initialText.hide();

		showSourceCode();
		showGraph();

		this.nachbarschaftLabel = this.lang.newText(new Coordinates(50, 400),
				"Neighborhood", "Neighborhood", null, this.textProps);
		this.mengePLabel = this.lang.newText(new Coordinates(50, 425), "Set P:",
				"Set P", null, this.textProps);
		this.mengeRLabel = this.lang.newText(new Coordinates(50, 450), "Set R:",
				"Set R", null, this.textProps);
		this.mengeXLabel = this.lang.newText(new Coordinates(50, 475), "Set X:",
				"Set X", null, this.textProps);
		this.resultLabel = this.lang.newText(new Coordinates(50, 550), "found cliques:",
				"found cliques", null, this.textProps);
		this.vertexLabel = this.lang.newText(new Coordinates(50, 500), "vertex v:",
				"vertex v", null, this.textProps);
		this.pivotLabel = this.lang.newText(new Coordinates(50, 525), "pivot element:",
				"pivot element", null, this.textProps);

		this.infoLine1Text = this.lang.newText(new Coordinates(200, 400), "",
				"infoLine1Text", null, this.textProps);
		this.infoLine2Text = this.lang.newText(new Coordinates(200, 425), "",
				"infoLine2Text", null, this.textProps);
		this.infoLine3Text = this.lang.newText(new Coordinates(200, 450), "",
				"infoLine3Text", null, this.textProps);
		this.infoLine4Text = this.lang.newText(new Coordinates(200, 475), "",
				"infoLine4Text", null, this.textProps);
		this.resultText = this.lang.newText(new Coordinates(200, 550), "",
				"infoLine4Text", null, this.textProps);
		this.vertexValue = this.lang.newText(new Coordinates(200, 500), "",
				"vertexValue", null, this.textProps);
		this.pivotValue = this.lang.newText(new Coordinates(200, 525), "",
				"pivotValue", null, this.textProps);

		this.counterSchnittmengeLabel = this.lang.newText(new Coordinates(700, 400), "intersection operation: ",
				"", null, this.textProps);
		this.counterVereinigungLabel = this.lang.newText(new Coordinates(700, 425), "union operation: ",
				"", null, this.textProps);
		this.counterDifferenzLabel = this.lang.newText(new Coordinates(700, 450), "difference operation:",
				"", null, this.textProps);

		this.counterSchnittmengeValue = this.lang.newText(new Coordinates(950, 400), "0",
				"", null, this.textProps);
		this.counterVereinigungValue = this.lang.newText(new Coordinates(950, 425), "0",
				"", null, this.textProps);
		this.counterDifferenzValue = this.lang.newText(new Coordinates(950, 450), "0",
				"", null, this.textProps);


		this.infotext = this.lang.newText(new Coordinates(50, 350), "", "info", null,
				textProps2);

		this.recursionTree = this.lang.newSourceCode(new Coordinates(400, 50), "recursionTree", null,
				this.recursionTreeProp);
		this.recursionTree.addCodeLine("Rekursionsaufrufe:", null, recursionTreeCounter, null);
		this.lang.nextStep("\t show Textfields");

		this.nachbarschaftLabel.show();
		this.mengePLabel.show();
		this.mengeRLabel.show();
		this.mengeXLabel.show();
		this.infoLine1Text.show();
		this.infoLine2Text.show();
		this.infoLine3Text.show();
		this.infoLine4Text.show();
		this.infotext.show();
		initialSets();

		return lang.toString();
	}


	private void showInitPage() {
		this.initialText = this.lang.newSourceCode(new Coordinates(50, 50), "initialText", null,
				this.infoPageProp);

		initialText.addCodeLine("Bron-Kerbosch with pivoting", null, 0, null);
		initialText.addCodeLine("", null, 0, null);
		initialText.addCodeLine("In order to understand functionality of Bron-Kerbosch with pivoting read please the explanation of simple impelentation of this algorithm (without pivoting).", null, 0, null);
		initialText.addCodeLine("", null, 0, null);
		initialText.addCodeLine("The basic form of the algorithm, described above, is inefficient in the case of graphs with many non-maximal cliques: it makes a recursive call for every clique, maximal or not.  ", null, 0, null);
		initialText.addCodeLine("To save time and allow the algorithm to backtrack more quickly in branches of the search that contain no maximal cliques, Bron and Kerbosch introduced a variant of ", null, 0, null);
		initialText.addCodeLine("the algorithm involving a pivot vertex u, chosen from P (or more generally, as later investigators realized, from P ⋃ X). Any maximal clique must include either u or one of its non-neighbors,", null, 0, null);
		initialText.addCodeLine("for otherwise the clique could be augmented by adding u to it. ", null, 0, null);
		initialText.addCodeLine("", null, 0, null);
		initialText.addCodeLine("Therefore, only u and its non-neighbors need to be tested as the choices for the vertex v that is added to R in each recursive call to the algorithm.", null, 0, null);
		initialText.addCodeLine("The challenge of Bron-Kerbosch with pivoting consists in finding good pivot selection strategies. In our case implemention is v selected randomly. To save time and allow the algorithm to backtrack more ", null, 0, null);
		initialText.addCodeLine("quickly in branches of the search that contain no maximal cliques, Bron and Kerbosch introduced a variant of the algorithm involving a pivot vertex u, chosen from P. Any maximal clique must ", null, 0, null);
		initialText.addCodeLine("include either u or one of its non-neighbors, for otherwise the clique could be augmented by adding u to it. Therefore, only u and its non-neighbors need to be tested as the choices for the vertex v ", null, 0, null);
		initialText.addCodeLine("that is added to R in each recursive call to the algorithm.", null, 0, null);
		initialText.addCodeLine("", null, 0, null);
		initialText.addCodeLine("Complexity", null, 0, null);
		initialText.addCodeLine("The worst-case running time is O(3^(n/3))", null, 1, null);
	}


	private void showEndPage() {
		this.lang.hideAllPrimitives();
		this.lang.hideAllPrimitives();
		this.nachbarschaftLabel.hide();
		this.mengePLabel.hide();
		this.mengeRLabel.hide();
		this.mengeXLabel.hide();
		this.pivotLabel.hide();
		this.infoLine1Text.hide();
		this.infoLine2Text.hide();
		this.infoLine3Text.hide();
		this.infoLine4Text.hide();
		this.infotext.hide();
		this.pivotValue.hide();
		this.graph.hide();

		this.endSite = this.lang.newText(new Coordinates(50, 50), "Statistics", "Statistics", null, this.statisticProp);
		this.endSite = this.lang.newText(new Coordinates(70, 100), "recursive calls: ", "Statistics", null, this.statisticProp);
		this.endSite = this.lang.newText(new Coordinates(70, 130), "calculation time: ", "Statistics", null, this.statisticProp);
		this.endSite = this.lang.newText(new Coordinates(70, 160), "found cliques total:", "Statistics", null, this.statisticProp);
		this.endSite = this.lang.newText(new Coordinates(70, 190), "found different cliques:", "Statistics", null, this.statisticProp);
		this.endSite = this.lang.newText(new Coordinates(330, 100), "intersection operation: ", "Statistics", null, this.textProps);
		this.endSite = this.lang.newText(new Coordinates(330, 130), "union operation: ", "Statistics", null, this.textProps);
		this.endSite = this.lang.newText(new Coordinates(330, 160), "difference operation:", "Statistics", null, this.textProps);

		this.endSite = this.lang.newText(new Coordinates(230, 100), String.valueOf(statisticRecursionCounter), "", null, this.statisticProp);
		this.endSite = this.lang.newText(new Coordinates(230, 130), String.valueOf(endtime - starttime) + " ms", "", null, this.statisticProp);
		this.endSite = this.lang.newText(new Coordinates(230, 160), String.valueOf(statisticsTotalCliquesFound), "", null, this.statisticProp);
		this.endSite = this.lang.newText(new Coordinates(230, 190), String.valueOf(differentCliques), "", null, this.statisticProp);
		this.endSite = this.lang.newText(new Coordinates(490, 100), String.valueOf(counterSchnittmenge), "", null, this.statisticProp);
		this.endSite = this.lang.newText(new Coordinates(490, 130), String.valueOf(counterVereinigung), "", null, this.statisticProp);
		this.endSite = this.lang.newText(new Coordinates(490, 160), String.valueOf(counterDifferenz), "", null, this.statisticProp);

		this.endSite = this.lang.newText(new Coordinates(50, 250), "Information", "Information", null, this.statisticProp);
		this.endSite = this.lang.newText(new Coordinates(70, 300), "Complexity: The worst-case running time is O(3^(n/3))", "Information", null, this.statisticProp);

		this.endSite = this.lang.newText(new Coordinates(50, 400), "Alternatives", "Information", null, this.statisticProp);
		this.endSite = this.lang.newText(new Coordinates(70, 450), "Bron-Kerbosch without pivoting", "", null, this.statisticProp);
		this.endSite = this.lang.newText(new Coordinates(70, 490), "Bron-Kerbosch with vertex ordering", "", null, this.statisticProp);

		this.lang.nextStep("End Page");
	}


	private void showGraph() {
		int size = this.graph.getSize();
		Node[] nodes = new Node[size];
		String[] nodeLabels = new String[size];
		for (int i = 0; i < size; i++) {
			nodes[i] = this.graph.getNode(i);
			nodeLabels[i] = String.valueOf((char) ('a' + i));
		}
		this.graph = this.lang.newGraph(this.graph.getName(), this.graph.getAdjacencyMatrix(), nodes,
				nodeLabels, this.graph.getDisplayOptions(), this.graphProps);
		this.graph.show();
	}


	private void showSourceCode() {
		this.lang.newRect(new Coordinates(690, 90), new Coordinates(1030, 330), "rahmen", null);

		this.src = this.lang.newSourceCode(new Coordinates(700, 100), "sourceCode", null,
				this.sourceCodeProps);
		this.src.addCodeLine("BronKerbosch(R, P, X):", null, 0, null);
		this.src.addCodeLine("if P and X are both empty:", null, 1, null);
		this.src.addCodeLine("add set R to solution set", null, 2, null);
		this.src.addCodeLine("choose a pivot vertex u in P ⋃ X", null, 1, null);
		this.src.addCodeLine("for each vertex v in P \\\\ N(u)::", null, 1, null);
		this.src.addCodeLine("newP := P ⋂ N(v);", null, 2, null);
		this.src.addCodeLine("newR := R ⋃ {v};", null, 2, null);
		this.src.addCodeLine("newX := X ⋂ N(v);", null, 2, null);
		this.src.addCodeLine("BronKerboschWithPivoting(newR, newP, newX)", null, 2, null);
		this.src.addCodeLine("P := P \\\\ {v}", null, 2, null);
		this.src.addCodeLine("X := X ⋃ {v}", null, 2, null);
		this.src.addCodeLine("Algorithm finished!", null, 0, null);

		this.lang.nextStep("\t show Sourcecode");
	}


	// public void printAM(){
	// int [][] ad1matrix = graph.getAdjacencyMatrix();
	//
	// for(int i = 0; i < ad1matrix.length; i++){
	// for(int j = 0; j < ad1matrix[0].length; j++){
	// System.out.print(ad1matrix[i][j] + ".");
	// }
	// System.out.println("\n");
	// }
	// }

	public void buildUndirectedGraph() {
		int[][] admatrix = graph.getAdjacencyMatrix();

		for (int i = 0; i < admatrix.length; i++) {
			for (int j = 0; j < admatrix[0].length; j++) {
				if (admatrix[i][j] == 1) {
					admatrix[j][i] = 1;
				}
			}
		}
		graph.setAdjacencyMatrix(admatrix);
	}


	private String setToString(TreeSet<Integer> t) {
		String string = "( ";
		for (Integer integer : t) {
			string += (char) ('a' + integer) + " ";
		}
		string += ")";
		return string;
	}


	private void showSetText(TreeSet<Integer> t, Text text) {
		String string = "{ ";
		for (Integer integer : t) {
			string += (char) ('a' + integer) + " ";
		}
		string += "}";
		text.setText(string, null, null);
		this.lang.nextStep();
	}


	private void showResult(HashSet<String> hs) {
		String result = "";
		for (String string : hs) {
			result += "( " + string + ") ";
		}
		differentCliques = hs.size();
		this.resultText.setText(result, null, null);
		this.lang.nextStep("Found result " + result);
	}


	private String generateTabs(int i) {
		String result = "\t ";
		while (i > 0) {
			result += "\t ";
			i--;
		}
		return result;
	}


	public void initialSets() {
		starttime = System.currentTimeMillis();
		int s = graph.getSize();
		TreeSet<Integer> P = new TreeSet<Integer>();
		for (int i = 0; i < s; i++) {
			P.add(i);
		}
		TreeSet<Integer> R = new TreeSet<Integer>();
		TreeSet<Integer> X = new TreeSet<Integer>();

		this.infotext.setText("Initialize set P with all nodes", null, null);
		showSetText(P, infoLine2Text);
		this.infotext.setText("Initialize set R as empty set", null, null);
		showSetText(R, infoLine3Text);
		this.infotext.setText("Initialize set X as empty set", null, null);
		showSetText(X, infoLine4Text);
		bronKerBoschWithPivoting(R, P, X);

		this.src.highlight(11);
		this.infotext.setText("Algorithm finishd!", null, null);
		this.lang.nextStep("Algorithm finished");

		endtime = System.currentTimeMillis();
		this.showEndPage();
	}


	/**
	 * R - max Clique P - The set of vertices that are connected to all vertices
	 * in R and can be added to C to make a larger clique X - The set of
	 * vertices connected to all vertices in R but excluded from being added to
	 * R P X are disjoint
	 */
	public TreeSet<Integer> bronKerBoschWithPivoting(TreeSet<Integer> R, TreeSet<Integer> P, TreeSet<Integer> X) {
		this.src.unhighlight(8);
		this.src.highlight(1);

		this.lang.nextStep();
		if (P.isEmpty() && X.isEmpty()) {
			this.src.highlight(2);
			Iterator iterator;
			iterator = R.iterator();
			String clique = "";
			for (Integer integer : R) {
				clique += (char) ('a' + integer) + " ";
				this.graph.highlightNode(integer, null, null);
			}
			this.infotext.setText("Found a new clique: (" + clique + ")", null, null);
			statisticsTotalCliquesFound++;
			results.add(clique);
			showResult(results);
			for (Integer integer : R) {
				clique += integer + " ";
				this.graph.unhighlightNode(integer, null, null);
			}
			this.src.unhighlight(1);
			this.lang.nextStep();
			this.src.unhighlight(2);
			return R;
		}
		this.src.unhighlight(1);
		this.src.highlight(3);

		TreeSet<Integer> PuX = (TreeSet<Integer>) union(P, X);
		this.counterVereinigung++;
		this.counterVereinigungValue.setText(String.valueOf(counterVereinigung), null, null);

		Random rnd = new Random();
		int r = rnd.nextInt(PuX.size());

		int u = 0;
		for (Integer v : PuX) {
			if (u == r) {
				u = v;
				break;
			}
			u++;
		}
		// pivotelement u
		this.infotext.setText("The pivot element is: " + (char) ('a' + u), null, null);
		this.pivotValue.setText(String.valueOf((char) ('a' + u)), null, null);
		this.lang.nextStep("\t \t \t \t \t pivot element " + (char) ('a' + u));
		this.src.unhighlight(3);
		TreeSet<Integer> Neigh = (TreeSet) N(u, true).clone();
		TreeSet<Integer> PwithNofU = (TreeSet<Integer>) difference(P, Neigh);
		// for each vertex v in P
		this.src.highlight(4);
		this.lang.nextStep();
		for (Integer v : PwithNofU) {
			this.src.unhighlight(4);
			this.vertexValue.setText(String.valueOf((char) ('a' + v)), null, null);
			TreeSet<Integer> Rnew = (TreeSet) R.clone();
			TreeSet<Integer> Pnew = new TreeSet<Integer>();
			TreeSet<Integer> Xnew = new TreeSet<Integer>();

			Rnew.add(v);
			TreeSet<Integer> N = (TreeSet) N(v, false).clone();
			Pnew = (TreeSet<Integer>) intersection(P, N);
			Xnew = (TreeSet<Integer>) intersection(X, N);

			this.infotext.setText("Updated set P", null, null);
			this.counterSchnittmenge++;
			this.counterSchnittmengeValue.setText(String.valueOf(counterSchnittmenge), null, null);
			this.src.highlight(5);
			showSetText(Pnew, infoLine2Text);

			this.infotext.setText("Updated set R", null, null);
			this.counterVereinigung++;
			this.counterVereinigungValue.setText(String.valueOf(counterVereinigung), null, null);
			this.src.unhighlight(5);
			this.src.highlight(6);
			showSetText(Rnew, infoLine3Text);

			this.infotext.setText("Updated set X", null, null);
			this.counterSchnittmenge++;
			this.counterSchnittmengeValue.setText(String.valueOf(counterSchnittmenge), null, null);
			this.src.unhighlight(6);
			this.src.highlight(7);
			showSetText(Xnew, infoLine4Text);
			this.src.unhighlight(7);

			this.src.highlight(8);
			this.src.unhighlight(3);

			this.infotext.setText("recursive call", null, null);
			recursionTreeCounter++;
			statisticRecursionCounter++;
			this.recursionTree.addCodeLine("BronKerbos( " + setToString(R) + ", " + setToString(P) + ", " + setToString(X) + "):", null, recursionTreeCounter, null);
			this.lang.nextStep(generateTabs(recursionTreeCounter) + "recursive Call");

			int oldV = v;
			bronKerBoschWithPivoting(Rnew, Pnew, Xnew);

			recursionTreeCounter--;
			this.infotext.setText("restoring values after recursion return - restoring v", null, null);
			this.vertexValue.setText(String.valueOf((char) ('a' + oldV)), null, null);
			this.infotext.setText("restoring values after recursion return - restoring P", null, null);
			showSetText(P, infoLine2Text);
			this.infotext.setText("restoring values after recursion return - restoring R", null, null);
			showSetText(R, infoLine3Text);
			this.infotext.setText("restoring values after recursion return - restoring X", null, null);
			showSetText(X, infoLine4Text);

			this.src.highlight(9);
			this.infotext.setText("Updated set P -> P := P \\ {v}", null, null);
			this.counterDifferenz++;
			this.counterDifferenzValue.setText(String.valueOf(counterDifferenz), null, null);
			this.lang.nextStep();

			this.src.unhighlight(9);
			this.src.highlight(10);
			this.infotext.setText("Updated set X -> X := X ⋃ {v}", null, null);
			this.counterVereinigung++;
			this.counterVereinigungValue.setText(String.valueOf(counterVereinigung), null, null);
			this.lang.nextStep();

			X.add(v);

			this.src.unhighlight(10);
		}
		return null;
	}


	// gibt die Nachbarn eines Knotens
	public TreeSet<Integer> N(int Node, boolean pivot) {
		if (!pivot) {
			this.infotext.setText("Current node is: " + (char) ('a' + Node), null, null);
		}
		this.graph.highlightNode(Node, null, null);
		this.lang.nextStep();
		int[] numbers = graph.getEdgesForNode(Node);
		TreeSet<Integer> outSet = new TreeSet<Integer>();
		String neighbors = "N(" + (char) ('a' + Node) + ") = ";

		for (int i = 0; i < numbers.length; i++) {
			if (numbers[i] == 1) {
				outSet.add(i);
				neighbors += (char) ('a' + i) + " ";
				this.graph.highlightNode(i, null, null);
			}
			if (!pivot) {
				this.infotext.setText("Neighbors of node " + (char) ('a' + Node) + " found!", null, null);
			}
			else
				this.infotext.setText("Neighbors of pivot " + (char) ('a' + Node) + " found!", null, null);
			this.infoLine1Text.setText(neighbors, null, null);
		}
		this.lang.nextStep();
		this.graph.unhighlightNode(Node, null, null);
		for (Integer integer : outSet) {
			this.graph.unhighlightNode(integer, null, null);
		}
		this.lang.nextStep();
		return outSet;
	}


	public String getName() {
		return "Bron-Kerbosch with pivoting";
	}


	public String getAlgorithmName() {
		return "Bron-Kerbosch";
	}


	public String getAnimationAuthor() {
		return "Marco Casili, Igor Cherepanov";
	}


	public String getDescription() {
		return "The basic form of the algorithm (see Bron Kerbosch without pivoting), is inefficient in the case of graphs with many non-maximal cliques: "
				+ "it makes a recursive call for every clique, maximal or not. To save time and allow the algorithm to backtrack more quickly in branches of the search that contain no maximal cliques, "
				+ "Bron and Kerbosch introduced a variant of the algorithm involving a pivot vertex u, chosen from P (or more generally, as later investigators realized, from P ⋃ X). "
				+ "Any maximal clique must include either u or one of its non-neighbors, for otherwise the clique could be augmented by adding u to it. "
				+ "Therefore, only u and its non-neighbors need to be tested as the choices for the vertex v that is added to R in each recursive call to the algorithm.";
	}


	public String getCodeExample() {
		return "   BronKerbosch(R, P, X):"
				+ "\n \t if P and X are both empty:"
				+ "\n \t \t report R as a maximal clique"
				+ "\n \t choose a pivot vertex u in P ⋃ X"
				+ "\n \t for each vertex v in P \\ N(u):"
				+ "\n \t \t BronKerbosch(R ⋃ {v}, P ⋂ N(v), X ⋂ N(v))"
				+ "\n \t \t P := P \\ {v}"
				+ "\n \t \t X := X ⋃ {v}";
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


	public static <T> Set<T> intersection(Set<T> setA, Set<T> setB) {
		Set<T> tmp = new TreeSet<T>();
		for (T x : setA)
			if (setB.contains(x))
				tmp.add(x);
		return tmp;
	}


	public static <T> Set<T> union(Set<T> setA, Set<T> setB) {
		Set<T> tmp = new TreeSet<T>(setA);
		tmp.addAll(setB);
		return tmp;
	}


	public static <T> Set<T> difference(Set<T> setA, Set<T> setB) {
		Set<T> tmp = new TreeSet<T>(setA);
		tmp.removeAll(setB);
		return tmp;
	}
}