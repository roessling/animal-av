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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
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
public class BronKerboschVertexOrdering implements Generator {

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
		this.resultLabel = this.lang.newText(new Coordinates(50, 525), "found cliques:",
				"found cliques", null, this.textProps);
		this.vertexLabel = this.lang.newText(new Coordinates(50, 500), "vertex v:",
				"found cliques", null, this.textProps);

		this.infoLine1Text = this.lang.newText(new Coordinates(200, 400), "",
				"infoLine1Text", null, this.textProps);
		this.infoLine2Text = this.lang.newText(new Coordinates(200, 425), "",
				"infoLine2Text", null, this.textProps);
		this.infoLine3Text = this.lang.newText(new Coordinates(200, 450), "",
				"infoLine3Text", null, this.textProps);
		this.infoLine4Text = this.lang.newText(new Coordinates(200, 475), "",
				"infoLine4Text", null, this.textProps);
		this.resultText = this.lang.newText(new Coordinates(200, 525), "",
				"infoLine4Text", null, this.textProps);
		this.vertexValue = this.lang.newText(new Coordinates(200, 500), "",
				"vertexValue", null, this.textProps);
		
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
		this.lang.nextStep();

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

		initialText.addCodeLine("Bron-Kerbosch With vertex ordering", null, 0, null);
		initialText.addCodeLine("", null, 0, null);
		initialText.addCodeLine("An alternative method for improving the basic form of the Bron–Kerbosch algorithm involves forgoing pivoting at the outermost level of recursion, and instead ", null, 0, null);
		initialText.addCodeLine("choosing the ordering of the recursive calls carefully in order to minimize the sizes of the sets P of candidate vertices within each recursive call.", null, 0, null);
		initialText.addCodeLine("The degeneracy of a graph G is the smallest number d such that every subgraph of G has a vertex with degree d or less. Every graph has a degeneracy ordering, ", null, 0, null);
		initialText.addCodeLine("an ordering of the vertices such that each vertex has d or fewer neighbors that come later in the ordering; a degeneracy ordering may be found in linear time by ", null, 0, null);
		initialText.addCodeLine("repeatedly selecting the vertex of minimum degree among the remaining vertices. If the order of the vertices v that the Bron–Kerbosch algorithm loops through ", null, 0, null);
		initialText.addCodeLine("is a degeneracy ordering, then the set P of candidate vertices in each call (the neighbors of v that are later in the ordering) will be guaranteed to have size at most d. ", null, 0, null);
		initialText.addCodeLine("", null, 0, null);
		initialText.addCodeLine("The set X of excluded vertices will consist of all earlier neighbors of v, and may be much larger than d. In recursive calls to the algorithm below the topmost level of ", null, 0, null);
		initialText.addCodeLine("the recursion, the pivoting version can still be used.", null, 0, null);
		initialText.addCodeLine("", null, 0, null);
		initialText.addCodeLine("Complexity", null, 0, null);
		initialText.addCodeLine("O(dn3^(d/3))", null, 1, null);
	}


	private void showEndPage() {
		this.lang.hideAllPrimitives();
		this.nachbarschaftLabel.hide();
		this.mengePLabel.hide();
		this.mengeRLabel.hide();
		this.mengeXLabel.hide();
		this.infoLine1Text.hide();
		this.infoLine2Text.hide();
		this.infoLine3Text.hide();
		this.infoLine4Text.hide();
		this.infotext.hide();
		this.graph.hide();
		
		this.endSite = this.lang.newText(new Coordinates(50, 50), "Statistics", "Statistics", null, this.statisticProp); 
		this.endSite = this.lang.newText(new Coordinates(70, 100), "recursive calls: ", "Statistics", null, this.statisticProp); 
		this.endSite = this.lang.newText(new Coordinates(70, 130), "calculation time: ", "Statistics", null, this.statisticProp); 
		this.endSite = this.lang.newText(new Coordinates(70, 160), "found cliques total:", "Statistics", null, this.statisticProp); 
		this.endSite = this.lang.newText(new Coordinates(70, 190), "found different cliques:", "Statistics", null, this.statisticProp); 
		this.endSite = this.lang.newText(new Coordinates(330, 100), "intersection operation: ", "Statistics", null, this.textProps);
		this.endSite = this.lang.newText(new Coordinates(330, 130), "union operation: ","Statistics", null, this.textProps);
		this.endSite = this.lang.newText(new Coordinates(330, 160), "difference operation:","Statistics", null, this.textProps);
		
		this.endSite = this.lang.newText(new Coordinates(230, 100), String.valueOf(statisticRecursionCounter), "", null, this.statisticProp); 
		this.endSite = this.lang.newText(new Coordinates(230, 130), String.valueOf(endtime - starttime) + " ms", "", null, this.statisticProp); 
		this.endSite = this.lang.newText(new Coordinates(230, 160), String.valueOf(statisticsTotalCliquesFound), "", null, this.statisticProp); 
		this.endSite = this.lang.newText(new Coordinates(230, 190), String.valueOf(differentCliques), "", null, this.statisticProp);
		this.endSite = this.lang.newText(new Coordinates(490, 100), String.valueOf(counterSchnittmenge), "", null, this.statisticProp); 
		this.endSite = this.lang.newText(new Coordinates(490, 130), String.valueOf(counterVereinigung), "", null, this.statisticProp); 
		this.endSite = this.lang.newText(new Coordinates(490, 160), String.valueOf(counterDifferenz), "", null, this.statisticProp);
		
		this.endSite = this.lang.newText(new Coordinates(50, 250), "Information", "Information", null, this.statisticProp); 
		this.endSite = this.lang.newText(new Coordinates(70, 300), "Complexity: O(dn3^(d/3))", "Information", null, this.statisticProp);
		
		this.endSite = this.lang.newText(new Coordinates(50, 400), "Alternatives", "Information", null, this.statisticProp);
		this.endSite = this.lang.newText(new Coordinates(70, 450), "Bron-Kerbosch with pivoting", "", null, this.statisticProp);
		this.endSite = this.lang.newText(new Coordinates(70, 490), "Bron-Kerbosch without pivoting", "", null, this.statisticProp);
		
		this.lang.nextStep("End page");
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

		this.src.addCodeLine("BronKerboschDEG(R, P, X):", null, 0, null);
		this.src.addCodeLine("if P and X are both empty:", null, 1, null);
		this.src.addCodeLine("P = V(G)", null, 2, null);
		this.src.addCodeLine("R = X = empty", null, 2, null);
		this.src.addCodeLine("for each vertex v in a degeneracy order of G:", null, 1, null);
		this.src.addCodeLine("newP := P ⋂ N(v);", null, 2, null);
		this.src.addCodeLine("newR := R ⋃ {v};", null, 2, null);
		this.src.addCodeLine("newX := X ⋂ N(v);", null, 2, null);
		this.src.addCodeLine("BronKerbosch(newR, newP, newX)", null, 2, null);
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

	private String generateTabs(int i){
		String result = "\t ";
		while(i > 0){
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


		LinkedList<Element> orderedList = giveListNodeD();
		Collections.sort(orderedList, new ComparatorForElements());
		
		this.infotext.setText("Initialize set P with all nodes", null, null);
		showSetText(P, infoLine2Text);
		this.infotext.setText("Order set P by numbers of links", null, null);
		this.mengePLabel.setText("V(P)", null, null);
		this.src.highlight(2);
		String orderedP = "";
		for (Element element : orderedList) {
			orderedP += String.valueOf((char) ('a' + element.getNodeNumber()) + " ");
		}
		this.infoLine2Text.setText("{ " + orderedP + "}", null, null);
		this.lang.nextStep();
		this.src.unhighlight(2);
		this.src.highlight(3);
		this.infotext.setText("Initialize set R as empty set", null, null);
		showSetText(R, infoLine3Text);
		this.infotext.setText("Initialize set X as empty set", null, null);
		showSetText(X, infoLine4Text);
		this.src.unhighlight(3);
		for (Element v : orderedList) {
			this.src.unhighlight(4);
			this.vertexValue.setText(String.valueOf((char) ('a' + v.getNodeNumber())), null, null);
			TreeSet<Integer> Rnew = (TreeSet) R.clone();
			TreeSet<Integer> Pnew = new TreeSet<Integer>();
			TreeSet<Integer> Xnew = new TreeSet<Integer>();

			Rnew.add(v.getNodeNumber());
			TreeSet<Integer> N = (TreeSet) N(v.getNodeNumber()).clone();
			Pnew = (TreeSet<Integer>) intersection(P, N);
			Xnew = (TreeSet<Integer>) intersection(X, N);

			this.mengePLabel.setText("Set P:", null, null);
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

			this.infotext.setText("Call (recursive) BronKerboschWithPivoting", null, null);
			recursionTreeCounter++;
			statisticRecursionCounter++;
			this.recursionTree.addCodeLine("BronKerbos( " + setToString(Rnew) + ", " + setToString(Pnew) + ", " + setToString(Xnew) + "):", null, recursionTreeCounter, null);
			this.lang.nextStep( generateTabs(recursionTreeCounter) + "recursive Call");

			int oldV = v.getNodeNumber();
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
			P.remove(v.getNodeNumber());
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

			this.src.unhighlight(10);
			X.add(v.getNodeNumber());
		}
		this.src.highlight(11);
		this.lang.nextStep();
		
		endtime = System.currentTimeMillis();
		showEndPage();

	}


	public LinkedList<Element> giveListNodeD() {
		LinkedList<Element> list = new LinkedList<Element>();

		int[] arr = new int[graph.getAdjacencyMatrix().length];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = 0;
		}
		int[][] adM = graph.getAdjacencyMatrix();
		for (int i = 0; i < adM.length; i++) {
			for (int j = 0; j < adM[i].length; j++) {
				if (adM[i][j] == 1) {
					arr[i] = arr[i] + 1;
				}
			}
			list.add(new Element(i, arr[i]));
		}
		return list;
	}


	/**
	 * R - max Clique P - The set of vertices that are connected to all vertices
	 * in R and can be added to C to make a larger clique X - The set of
	 * vertices connected to all vertices in R but excluded from being added to
	 * R P X are disjoint P union X =
	 */
	public TreeSet<Integer> bronKerBoschWithPivoting(TreeSet<Integer> R, TreeSet<Integer> P, TreeSet<Integer> X) {
		if (P.isEmpty() && X.isEmpty()) {
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
			this.lang.nextStep();
			return R;
		}
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
		TreeSet<Integer> Neigh = (TreeSet) N(u).clone();
		TreeSet<Integer> PwithNofU = (TreeSet<Integer>) difference(P, Neigh);
		// for each vertex v in P
		for (Integer v : PwithNofU) {
			TreeSet<Integer> Rnew = (TreeSet) R.clone();
			TreeSet<Integer> Pnew = new TreeSet<Integer>();
			TreeSet<Integer> Xnew = new TreeSet<Integer>();

			Rnew.add(v);
			TreeSet<Integer> N = (TreeSet) N(v).clone();
			Pnew = (TreeSet<Integer>) intersection(P, N);
			Xnew = (TreeSet<Integer>) intersection(X, N);

			this.counterSchnittmenge++;
			this.counterSchnittmengeValue.setText(String.valueOf(counterSchnittmenge), null, null);
			this.counterVereinigung++;
			this.counterVereinigungValue.setText(String.valueOf(counterVereinigung), null, null);
			this.counterSchnittmenge++;
			this.counterSchnittmengeValue.setText(String.valueOf(counterSchnittmenge), null, null);
			
			recursionTreeCounter++;
			statisticRecursionCounter++;
			this.recursionTree.addCodeLine("BronKerbos( " + setToString(Rnew) + ", " + setToString(Pnew) + ", " + setToString(Xnew) + "):", null, recursionTreeCounter, null);
			bronKerBoschWithPivoting(Rnew, Pnew, Xnew);
			recursionTreeCounter--;
			
			this.counterDifferenz++;
			this.counterDifferenzValue.setText(String.valueOf(counterDifferenz), null, null);
			this.counterVereinigung++;
			this.counterVereinigungValue.setText(String.valueOf(counterVereinigung), null, null);
			
			X.add(v);
		}
		return null;
	}


	public TreeSet<Integer> N(int Node) {
		this.infotext.setText("Current node is: " + (char) ('a' + Node), null, null);
		this.graph.highlightNode(Node, null, null);
		this.lang.nextStep();
		int[] numbers = graph.getEdgesForNode(Node);
		String neighbors = "N(" + (char) ('a' + Node) + ") = ";

		TreeSet<Integer> outSet = new TreeSet<Integer>();
		for (int i = 0; i < numbers.length; i++) {
			if (numbers[i] == 1) {
				outSet.add(i);
				neighbors += (char) ('a' + i) + " ";
				this.graph.highlightNode(i, null, null);
			}
			this.infotext.setText("Neighbors of node " + (char) ('a' + Node) + " found!", null, null);
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
		return "Bron-Kerbosch with vertex ordering";
	}


	public String getAlgorithmName() {
		return "Bron-Kerbosch";
	}


	public String getAnimationAuthor() {
		return "Marco Casili, Igor Cherepanov";
	}


	public String getDescription() {
		return "An alternative method for improving the basic form of the Bron–Kerbosch algorithm involves forgoing pivoting at the outermost level of recursion, and instead choosing the ordering of the recursive calls carefully in order to minimize the sizes of the sets P of candidate vertices within each recursive call."
				+ "The degeneracy of a graph G is the smallest number d such that every subgraph of G has a vertex with degree d or less. Every graph has a degeneracy ordering, an ordering of the vertices such that each vertex has d or fewer neighbors that come later in the ordering; a degeneracy ordering may be found in linear time by repeatedly selecting the vertex of minimum degree among the remaining vertices. If the order of the vertices v that the Bron–Kerbosch algorithm loops through is a degeneracy ordering, then the set P of candidate vertices in each call (the neighbors of v that are later in the ordering) will be guaranteed to have size at most d. The set X of excluded vertices will consist of all earlier neighbors of v, and may be much larger than d. In recursive calls to the algorithm below the topmost level of the recursion, the pivoting version can still be used";
	}


	public String getCodeExample() {
		return "BronKerboschDEG(R, P, X):"
		+ "\n \t if P and X are both empty:"
		+ "\n \t \t P = V(G)"
		+ "\n \t \t R = X = empty"
		+ "\n \t for each vertex v in a degeneracy ordering of G:"
		+ "\n \t \t BronKerbosch(R ∪ {v}, R ∩ N(v), X ∩ N(v))"
		+ "\n \t \t P := P \\\\ {v}"
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


	public class Element {

		private int nodeNumber;
		private int d;


		public Element(int _n, int _d) {
			nodeNumber = _n;
			d = _d;
		}


		public int getNodeNumber() {
			return nodeNumber;
		}


		public void setNodeNumber(int nodeNumber) {
			this.nodeNumber = nodeNumber;
		}


		public int getD() {
			return d;
		}


		public void setD(int d) {
			this.d = d;
		}
	}

	public class ComparatorForElements implements Comparator<Element> {

		@Override
		public int compare(Element arg0, Element arg1) {
			// TODO Auto-generated method stub
			int a = arg0.getD();
			int b = arg1.getD();

			if (a - b <= 0) {
				return 1;
			}
			else
				return -1;
		}
	}
}
