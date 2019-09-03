package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;

import algoanim.primitives.Graph;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;

import java.util.Hashtable;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class HopcroftKarp implements ValidatingGenerator {
	private String[] descriptionText = {
			"The Hopcroft-Karp algorithm finds a maxium cardinality matching in a bipartite grah (see definitions below).",
			"It builds on the augmenting path techinque. However, instead of searching for paths one by one,",
			"it augments along a set of vertex-disjoint shortest augment paths simultaniously, which reduces the complexity.",
			"To find such a set, a combination of slightly modified Breadth-First and Depth-First Searches are used." };
	private String[] definitionsText = {
			"",
			"<b><u>Some definitions:</u></b>",
			"<b>Symmetric difference (⊕):</b> A ⊕ B = (A ∪ B) - (A ∩ B)",
			"<b>Bipartite Graph:</b> A graph G = (V, E) is bipartite if there exists a partition V = X ∪ Y with X ∩ Y = {} and E ⊆ X × Y",
			"<b>Matched Vertex:</b> We say that a vertex is matched if it is incident to some edge in M, otherwise it is free.",
			"<b>Alternating Path:</b> A path is alternating if its edges alternate between M and E - M.",
			"<b>Augmenting Path:</b> An alternating path is augmenting if both endpoints are free.",
			"<b>Matching:</b> A Matching is a subset M ⊆ E such that ∀v ∈ V, at most one edge in M is connected/incident upon v.",
			"<b>Perfect Matching:</b> A matching which matches all vertices of the graph.",
			"<b>Maximum Matching:</b> A Maximum Matching is a Matching M such that every other matching M' satisfies |M'| ≤ |M|"
			};
	private String[] instructionsText = {
			"<b><u>Input instructions (Read First!):</b></u>",
			"(Consider the 'Bipartite Graph' definion below)",
			"For a bipartite Graph we will use a BiAdjacency-Matrix as input. This works as follows:",
			"Each row represents a vertex in one partition of the Graph (X)",
			"Each column represents a vertex in the other partition of the Graph (Y)",
			"Connected Vertices will be represented by a 1 in the corresponding cell, and 0 otherwise.",
			"The example matrix below represents a graph with the vertices {x1, x2, y1, y2} and the edges {(x1-y1), (x1-y2), (x2-y1)}",
			"<table><tr><td><table style=\"border: 1px solid black;\"><tr><td>1</td><td>1</td></tr><tr><td>1</td><td>0</td></tr></table></td><td> = </td><td><table style=\"border: 1px solid black;\"><tr><td></td><td>x1</td><td>x2</td></tr><tr><td>y1</td><td>1</td><td>1</td></tr><tr><td>y2</td><td>1</td><td>0</td></tr></table></td></td></table>" };
	private String[] outro = {
			"Published in 1973 by John Hopcroft and Richard Karp, the algorithm provides a lower complexity than the previously used matching algorithms.",
			"It runs in O(√|V| * |E|) in the worst case, which is a big improvement over the older augmenting path techinque with O(|V| * |E|)",
			"",
			"Complexity:",
			"We know each phase can be solved in linear time O(|E|), as they each perform a sinle Breadth- and a single Deapth-First search.",
			"We know that at any given phase, any remaining augmentings path must be longer (the set of shortest augmenting paths found at each phase is maximal)",
			"This implies, that after the initial √|V| phases, the size of the shortest remaining augmenting path will be ≥ √|V|",
			"Because M contains only vertex disjoint augmenting paths, if each of the paths in M has length √|V|, there can be at most √|V| paths in M.",
			"The size of the maximal matching will then differ from the size of M by at most √|V| edges, meaning that there can be at most √|V| additional phases",
			"We are left with a maximum amount of phases of 2√|V| which together with the phase complexity gives us a complexity f O(√|V| * |E|)",
			"",
			"Other Matching Algorithms:",
			"Another bipartite matching algorithm worth mentioning is the Alt et al (1991) algorithm.",
			"It is worth mentioning because it is known to perform slightly better than the Hopcroft-Karp algorithm when dealing with dense graphs.",
			"The Hopcroft-Karp continues to have the best asymptotic upper bound for sparse graphs however.",
			"",
			"Finding maximal sets of shortest augmenting paths has also been implemented in finding maximum matching in non-bipartite graphs.",
			"An example would be the Micali-Vazirani algorithm.", "", "" };

	private SourceCode codeHK, codeMSP, codeBFS, codeDFS;
	private SourceCodeProperties codeProps;
	private Language lang;
	private TextProperties textProps, layerTextProps, varProps;
	private GraphProperties graphProps, graphPropsDirected;
	private int[][] biAdjacencyMatrix, adjacencyMatrix;
	private HashMap<String, Vertex> g1, g2;
	private Graph G, directedG;
	private Text textM, textP, textF;
	private ArrayList<Edge> M;
	private ArrayList<Vertex> hiddenVertices;
	private int pseudoSize;
	private Node[] nodes;
	private String[] labels;
	private Rect rectHK, rectMSP, rectBFS, rectDFS;

	private class Vertex {
		String name;
		ArrayList<Edge> edges;
		Node graphNode;
		boolean matched;
		int layer;
		Text layerText;

		Vertex(String n, Node gNode) {
			graphNode = gNode;
			name = n;
			matched = false;
			layer = -2;
			edges = new ArrayList<Edge>();
			Coordinates layerTextCoords = (Coordinates) gNode;
			layerText = lang.newText(new Coordinates(
					layerTextCoords.getX() + 5, layerTextCoords.getY() - 11),
					"", "layer_" + n, null, layerTextProps);
		}

		Edge addNeighbour(Vertex n, int p1, int p2) {
			Edge e = new Edge(this, n, p1, p2);
			edges.add(e);
			n.edges.add(e);
			return e;
		}

		void hide() {
			layerText.setText("", null, null);
			directedG.hideNode(graphNode, null, null);
		}
	}

	private class Edge {
		Vertex[] nodes;
		int pos1, pos2;

		Edge(Vertex n1, Vertex n2, int p1, int p2) {
			nodes = new Vertex[2];
			nodes[0] = n1;
			nodes[1] = n2;
			pos1 = p1;
			pos2 = p2;
		}
	}

	public void init() {
		lang = new AnimalScript("Hopcroft-Karp",
				"Nicolas Morew, Melissa Mendoza", 800, 600);
		lang.setStepMode(true);
	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		textProps = (TextProperties) props
				.getPropertiesByName("text");
		codeProps = (SourceCodeProperties) props
				.getPropertiesByName("pseudoCode");
		RectProperties hRectProps = (RectProperties) props.getPropertiesByName("titleBackground");
		Font pseudoFont = (Font) codeProps.get("font");
		boolean pseudoBold = (Boolean) codeProps.get("bold");
		pseudoSize = (Integer) codeProps.get("size");
		pseudoFont = new Font(pseudoFont.getFamily(), (pseudoBold) ? Font.BOLD
				: Font.PLAIN, pseudoSize);
		codeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, pseudoFont);
		varProps = new TextProperties();
		varProps.set(AnimationPropertiesKeys.FONT_PROPERTY, pseudoFont);
		// intro screen text
		layerTextProps = new TextProperties();
		layerTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.BOLD, 16));
		Font textFont = (Font) textProps.get("font");
		TextProperties headerProps = new TextProperties();
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				textFont.getFamily(), Font.BOLD, 28));
		headerProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
		Text header = lang.newText(new Coordinates(20, 35),
				"Hopcroft-Karp Algorithm", "header", null, headerProps);
		Rect hRect = lang.newRect(new Offset(-5, -5, "header",
				AnimalScript.DIRECTION_NW), new Offset(5, 5, "header", "SE"),
				"hRect", null, hRectProps);
		lang.newText(new Coordinates(20, 70), descriptionText[0],
				"description0", null, textProps);
		for (int i = 1; i < descriptionText.length; i++)
			lang.newText(new Offset(0, 25, "description" + (i - 1),
					AnimalScript.DIRECTION_NW), descriptionText[i].replaceAll(
					"<[^>]*>", ""), "description" + i, null, textProps);

		lang.newText(new Offset(0, 35, "description"
				+ (descriptionText.length - 1), AnimalScript.DIRECTION_NW),
				definitionsText[0], "definition0", null, textProps);
		for (int i = 1; i < definitionsText.length; i++)
			lang.newText(new Offset(0, 25, "definition" + (i - 1),
					AnimalScript.DIRECTION_NW), definitionsText[i].replaceAll(
					"<[^>]*>", ""), "definition" + i, null, textProps);
		lang.newRect(new Offset(-10, 5, "definition0",
				AnimalScript.DIRECTION_NW), new Offset(15, 10 + 12 * 12, "definition3", "SE"), "rectHK", null,
				new RectProperties());
		lang.nextStep("Start");
		lang.hideAllPrimitives();
		header.show();
		hRect.show();
		// initialize algorithm values
		graphProps = (GraphProperties) props
				.getPropertiesByName("graph");
		graphPropsDirected = new GraphProperties();
		for (String k : graphProps.getAllPropertyNames()) {
			graphPropsDirected.set(k, graphProps.get(k));
		}
		graphPropsDirected.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, true);
		biAdjacencyMatrix = (int[][]) primitives
				.get("bipartiteAdjacencyMatrix");
		int vCount = biAdjacencyMatrix.length + biAdjacencyMatrix[0].length;
		g1 = new HashMap<String, Vertex>();
		g2 = new HashMap<String, Vertex>();
		M = new ArrayList<Edge>();
		adjacencyMatrix = new int[vCount][vCount];
		nodes = new Node[vCount];
		labels = new String[vCount];
		for (int i = 0; i < biAdjacencyMatrix.length; i++) {
			String nodeName = "a" + i;
			Node gNode = new Coordinates(i * 85 + 80, 80);
			labels[i] = nodeName;
			nodes[i] = gNode;
			g1.put("a" + i, new Vertex(nodeName, gNode));
		}
		for (int i = 0; i < biAdjacencyMatrix[0].length; i++) {
			String nodeName = "b" + i;
			Node gNode = new Coordinates(i * 85 + 80, 220);
			labels[biAdjacencyMatrix.length + i] = nodeName;
			nodes[biAdjacencyMatrix.length + i] = gNode;
			g2.put("b" + i, new Vertex(nodeName, gNode));
		}
		// Show primitives
		// IntMatrix bipMatrix = lang.newIntMatrix(new Coordinates(80, 300),
		// biAdjacencyMatrix, "matrix", null, matrixProps);
		for (int i1 = 0; i1 < biAdjacencyMatrix.length; i1++) {
			Vertex g1Node = g1.get("a" + i1);
			for (int i2 = 0; i2 < biAdjacencyMatrix[0].length; i2++) {
				if (biAdjacencyMatrix[i1][i2] > 0) {
					g1Node.addNeighbour(g2.get("b" + i2), i1,
							biAdjacencyMatrix.length + i2);
					adjacencyMatrix[i1][biAdjacencyMatrix.length + i2] = 1;
					adjacencyMatrix[biAdjacencyMatrix.length + i2][i1] = 1;
				}
			}
		}
		G = lang.newGraph("graph", adjacencyMatrix, nodes, labels, null,
				graphProps);
		lang.newText(new Coordinates(50, 78), "U : ", "uTitle", null, textProps);
		lang.newText(new Coordinates(50, 222), "V : ", "vTitle", null,
				textProps);
		setPseudoCode();
		codeHK.show();
		rectHK.show();
		lang.nextStep("Algorithm Start");
		codeHK.highlight(0);
		lang.nextStep("M = {}");
		codeHK.highlight(1);
		textM = lang.newText(new Offset(0, pseudoSize * 5, "codeHK",
				AnimalScript.DIRECTION_SW), "M = {}", "textM", null, varProps);
		lang.nextStep("while (P = Maximal-Set-Of-Paths ∧ P ≠ ∅)");
		codeHK.unhighlight(1);
		codeHK.highlight(2);
		LinkedList<Edge> P = maximalSetOfPaths();
		while (!P.isEmpty()) {
			lang.nextStep("M = M ⊕ P");
			codeHK.highlight(3);
			Iterator<Edge> i = P.iterator();
			while (i.hasNext()) {
				Edge e = i.next();
				if (M.contains(e)) {
					unmatch(e);
					i.remove();
				}
			}
			for (Edge e : P) {
				if (!M.contains(e)) {
					match(e);
				}
			}
			textP.hide();
			lang.nextStep();
			codeHK.unhighlight(3);
			P = maximalSetOfPaths();
		}
		lang.nextStep("return M (P == ∅)");
		textP.hide();
		textM.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24), null, null);
		for (Edge e : M) {
			G.highlightEdge(e.nodes[0].graphNode, e.nodes[1].graphNode, null,
					null);
			G.highlightNode(e.nodes[0].graphNode, null, null);
			G.highlightNode(e.nodes[1].graphNode, null, null);
		}
		codeHK.unhighlight(2);
		codeHK.highlight(4);
		lang.nextStep();
		lang.hideAllPrimitives();
		codeMSP.hide();
		rectMSP.hide();
		header.show();
		hRect.show();
		codeHK.hide();
		rectHK.hide();
		lang.newText(new Coordinates(20, 70), outro[0], "outro0", null,
				textProps);
		for (int i = 1; i < outro.length; i++)
			lang.newText(new Offset(0, 25, "outro" + (i - 1),
					AnimalScript.DIRECTION_NW), outro[i], "outro" + i, null,
					textProps);
		return lang.toString();
	}

	LinkedList<Edge> maximalSetOfPaths() {
		LinkedList<Edge> P = new LinkedList<Edge>();
		codeMSP.show();
		rectMSP.show();
		codeMSP.highlight(0);
		lang.nextStep("P = {}");
		codeMSP.highlight(1);
		textP = lang.newText(new Offset(0, 10, "textM",
				AnimalScript.DIRECTION_SW), "P = {}", "textP", null, varProps);
		String pList = "";
		lang.nextStep("F = Alternating-Breadth-First-Search");
		codeMSP.unhighlight(1);
		codeMSP.highlight(2);
		codeBFS.show();
		rectBFS.show();
		codeBFS.highlight(0);
		ArrayList<Vertex> F = alternatingBFS();
		String fTextList = "F = {";
		codeBFS.unhighlight(0);
		codeBFS.unhighlight(7);
		codeBFS.unhighlight(8);
		for (Vertex v : F) {
			fTextList += ", " + v.name;
		}
		fTextList += "}";
		fTextList = fTextList.replaceFirst(", ", "");
		textF = lang
				.newText(new Offset(0, 10, "textP",
						AnimalScript.DIRECTION_SW), fTextList, "textF", null, varProps);
		codeBFS.hide();
		rectBFS.hide();
		lang.nextStep("foreach vertex in F");
		for (Vertex v : g2.values())
			directedG.unhighlightNode(v.graphNode, null, null);
		codeMSP.unhighlight(2);
		codeMSP.highlight(3);
		hiddenVertices = new ArrayList<Vertex>();
		if (!F.isEmpty()) {
			for (Vertex v : F) {
				lang.nextStep("P = P ∪ Depth-First-Search");
				codeMSP.highlight(4);
				codeDFS.show();
				rectDFS.show();
				codeDFS.highlight(0);
				ArrayList<Edge> path = disjointDFS(v, new ArrayList<Edge>());
				for (Edge e : path) {
					pList += ", (" + e.nodes[0].name + "-" + e.nodes[1].name
							+ ")";
					P.add(e);
				}
				textP.setText("P = {" + pList.replaceFirst(", ", "") + "}",
						null, null);
				lang.nextStep("hide the vertices on the path temporarily");
				codeDFS.unhighlight(4);
				codeDFS.highlight(5);
				for (Vertex u : hiddenVertices) {
					u.hide();
				}
				lang.nextStep();
				codeDFS.unhighlight(3);
				codeDFS.unhighlight(5);
				codeDFS.unhighlight(6);
				codeDFS.hide();
				rectDFS.hide();
				codeMSP.unhighlight(4);
			}
		}
		lang.nextStep("reset G");
		codeMSP.unhighlight(3);
		codeMSP.unhighlight(4);
		codeMSP.highlight(5);
		directedG.hide();
		textF.hide();
		for (Vertex v : g1.values())
			v.layerText.setText("", null, null);
		for (Vertex v : g2.values())
			v.layerText.setText("", null, null);
		G.show();
		lang.nextStep("return P");
		codeMSP.unhighlight(5);
		codeMSP.highlight(6);
		lang.nextStep();
		codeMSP.hide();
		rectMSP.hide();
		codeMSP.unhighlight(6);
		return P;
	}

	ArrayList<Vertex> alternatingBFS() {
		lang.nextStep();
		codeBFS.highlight(1);
		lang.nextStep("Create alternating paths");
		codeBFS.highlight(2);
		codeBFS.highlight(3);
		ArrayList<Edge> currentLayer = new ArrayList<Edge>();
		ArrayList<Edge> nextLayer = new ArrayList<Edge>();
		ArrayList<Vertex> augPathEnds = new ArrayList<Vertex>();
		int lvl = 0;
		for (Vertex v : g1.values()) {
			if (!v.matched) {
				currentLayer.addAll(v.edges);
			}
			v.layer = -2;
			v.layerText.setText("", null, null);
		}
		directedG = lang.newGraph("directedGraph", alternatingPathDirections(),
				nodes, labels, null, graphPropsDirected);
		G.hide();
		lang.nextStep("Perform Alternating-Breadth-First-Search from free vertices in U");
		codeBFS.unhighlight(1);
		codeBFS.unhighlight(2);
		codeBFS.unhighlight(3);
		codeBFS.highlight(4);
		for (Vertex v : g2.values()) {
			v.layer = -2;
			v.layerText.setText("", null, null);
		}
		for (Edge e : currentLayer)
			directedG.highlightNode(e.nodes[0].graphNode, null, null);
		boolean odd = false;
		while (!currentLayer.isEmpty()) {
			lang.nextStep("mark the layer in which the vertices are traversed");
			codeBFS.unhighlight(6);
			codeBFS.highlight(5);
			int startIndex = (odd) ? 1 : 0;
			int endIndex = (odd) ? 0 : 1;
			for (Edge e : currentLayer) {
				e.nodes[startIndex].layer = lvl;
				e.nodes[startIndex].layerText.setText("" + lvl, null, null);
				Vertex endV = e.nodes[endIndex];
				if (endV.layer < 0 && (M.contains(e) == odd)
						&& !nextLayer.containsAll(endV.edges)) {
					nextLayer.addAll(endV.edges);
				}
				if (!odd && !endV.matched && !augPathEnds.contains(endV)) {
					augPathEnds.add(endV);
				}
			}
			lang.nextStep("traverse edges leaving the vertices in the current layer");
			codeBFS.unhighlight(5);
			codeBFS.highlight(6);
			for (Edge e : currentLayer) {
				if (nextLayer.contains(e))
					directedG.highlightEdge(e.nodes[startIndex].graphNode,
							e.nodes[endIndex].graphNode, null, null);
			}
			lang.nextStep();
			for (Edge e : nextLayer) {

				directedG.unhighlightEdge(e.nodes[startIndex].graphNode,
						e.nodes[endIndex].graphNode, null, null);
				directedG.unhighlightNode(e.nodes[startIndex].graphNode, null,
						null);
				if (e.nodes[endIndex].layer < 0)
					directedG.highlightNode(e.nodes[endIndex].graphNode, null,
							null);
			}
			lang.nextStep();
			odd = !odd;
			currentLayer = new ArrayList<Edge>(nextLayer);
			nextLayer.clear();
			lvl++;
			if (!augPathEnds.isEmpty()) {
				codeBFS.unhighlight(6);
				codeBFS.highlight(5);
				for (Edge e : currentLayer) {
					e.nodes[1].layer = lvl;
					e.nodes[1].layerText.setText("" + lvl, null, null);
				}
				lang.nextStep("a free vertex in V was found");
				codeBFS.unhighlight(4);
				codeBFS.unhighlight(5);
				codeBFS.highlight(7);
				for (Vertex v : g2.values()) {
					if (v.matched) {
						directedG.unhighlightNode(v.graphNode, null, null);
					}
				}
				lang.nextStep("return set of all free edges at last layer");
				return augPathEnds;
			}
		}
		lang.nextStep("no free vertex in V was found: return {}");
		codeBFS.unhighlight(4);
		codeBFS.unhighlight(5);
		codeBFS.highlight(8);
		return augPathEnds;
	}

	ArrayList<Edge> disjointDFS(Vertex v, ArrayList<Edge> path) {
		directedG.highlightNode(v.graphNode, null, null);
		codeDFS.highlight(1);
		if (v.layer == 0) {
			lang.nextStep();
			codeDFS.highlight(3);
			lang.nextStep();
			codeDFS.highlight(4);
			hiddenVertices.add(v);
			return path;
		}
		int index = g1.containsValue(v) ? 1 : 0;
		for (Edge e : v.edges) {
			if ((index == 1) == M.contains(e)
					&& !hiddenVertices.contains(e.nodes[index])
					&& e.nodes[index].layer == v.layer - 1) {
				lang.nextStep();
				codeDFS.highlight(2);
				directedG.highlightEdge(e.nodes[index].graphNode,
						e.nodes[(index == 1) ? 0 : 1].graphNode, null, null);
				path.add(e);
				ArrayList<Edge> ret = disjointDFS(e.nodes[index], path);
				if (!ret.isEmpty()) {
					hiddenVertices.add(v);
					codeDFS.unhighlight(1);
					codeDFS.unhighlight(2);
					codeDFS.highlight(3);
					return ret;
				}
				path.remove(e);
				directedG.unhighlightEdge(e.nodes[index].graphNode,
						e.nodes[(index == 1) ? 0 : 1].graphNode, null, null);
			}
		}
		directedG.unhighlightNode(v.graphNode, null, null);
		codeDFS.unhighlight(1);
		codeDFS.unhighlight(2);
		codeDFS.highlight(6);
		return new ArrayList<Edge>();
	}

	void match(Edge e) {
		e.nodes[0].matched = true;
		e.nodes[1].matched = true;
		M.add(e);
		// G.highlightEdge(e.nodes[0].graphNode, e.nodes[1].graphNode, null,
		// null);
		updateMText();
	}

	void unmatch(Edge e) {
		e.nodes[0].matched = false;
		e.nodes[1].matched = false;
		M.remove(e);
		// G.unhighlightEdge(e.nodes[0].graphNode, e.nodes[1].graphNode, null,
		// null);
		updateMText();
	}

	private void updateMText() {
		String text = "M = {";
		for (Edge e : M) {
			text += ", (" + e.nodes[0].name + ", " + e.nodes[1].name + ")";
		}
		text = text.replaceFirst(", ", "");
		text += "}";
		textM.setText(text, null, null);
	}

	private int[][] alternatingPathDirections() {
		int[][] m = new int[adjacencyMatrix.length][adjacencyMatrix[0].length];
		for (int i = 0; i < adjacencyMatrix.length; i++)
			m[i] = adjacencyMatrix[i].clone();
		for (Vertex v : g1.values())
			for (Edge e : v.edges)
				if (M.contains(e))
					m[e.pos1][e.pos2] = 0;
				else
					m[e.pos2][e.pos1] = 0;
		return m;
	}

	public String getName() {
		return "Hopcroft-Karp";
	}

	public String getAlgorithmName() {
		return "Maximum Matching in Bipartite Graph";
	}

	public String getAnimationAuthor() {
		return "Nicolas Morew, Melissa Mendoza";
	}

	public String getDescription() {
		String returned = "";
		for (int i = 0; i < descriptionText.length; i++) {
			returned += descriptionText[i] + "<br>";
		}
		returned += "<br>";
		for (int i = 0; i < instructionsText.length; i++) {
			returned += instructionsText[i] + "<br>";
		}
		for (int i = 0; i < definitionsText.length; i++) {
			returned += definitionsText[i] + "<br>";
		}
		return returned;
	}

	public String getCodeExample() {
		return "M = {}"
				+ "\n"
				+ "do"
				+ "\n"
				+ "\tP = Maximal-Set-Of-Paths"
				+ "\n"
				+ "\tM = M ⊕ P"
				+ "\n"
				+ "while P ≠ ∅"
				+ "\nreturn M"
				+ "\n\n"
				+ "Maximal-Set-Of-Paths"
				+ "\n"
				+ "P = {}"
				+ "\n"
				+ "F = Alternating-Breadth-First-Search"
				+ "\n"
				+ "foreach vertex in F"
				+ "\n"
				+ "\tP = P ∪ Disjoint-Depth-First-Search"
				+ "\n"
				+ "return P"
				+ "\n\n"
				+ "Where MAXIMAL-SET-OF-PATHS uses an alternating breadth-first-search to layer and find the possible augmenting paths,"
				+ "\n"
				+ "and a depth-first-search to find the disjoint ones within those.";
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

	private void setPseudoCode() {
		codeHK = lang.newSourceCode(new Coordinates(40, 260), "codeHK", null,
				codeProps);
		codeHK.addCodeLine("Hopcroft-Karp", null, 0, null);
		codeHK.addCodeLine("M = {}", null, 0, null);
		codeHK.addCodeLine("while (P = Maximal-Set-Of-Paths ∧ P ≠ ∅)", null, 0,
				null);
		codeHK.addCodeLine("M = M ⊕ P", null, 1, null);
		codeHK.addCodeLine("return M", null, 0, null);
		codeHK.hide();
		rectHK = lang.newRect(new Offset(-5, -5, "codeHK",
				AnimalScript.DIRECTION_NW), new Offset(10, 10, "codeHK", "SE"),
				"rectHK", null, new RectProperties());
		rectHK.hide();
		// maximum-set-of-paths
		codeMSP = lang.newSourceCode(new Offset(30, -pseudoSize, "codeHK",
				AnimalScript.DIRECTION_NE), "codeMSP", null,
				codeProps);
		codeMSP.addCodeLine("Maximal-Set-Of-Paths", null, 0, null);
		codeMSP.addCodeLine("P = {}", null, 0, null);
		codeMSP.addCodeLine("F = Alternating-Breadth-First-Search", null, 0,
				null);
		codeMSP.addCodeLine("foreach vertex in F", null, 0, null);
		codeMSP.addCodeLine("P = P ∪ Depth-First-Search", null, 1, null);
		codeMSP.addCodeLine("reset G", null, 0, null);
		codeMSP.addCodeLine("return P", null, 0, null);
		codeMSP.hide();
		rectMSP = lang.newRect(new Offset(-5, -5, "codeMSP",
				AnimalScript.DIRECTION_NW), new Offset(5, 5, "codeMSP", "SE"),
				"rectMSP", null, new RectProperties());
		rectMSP.hide();
		// BFS
		codeBFS = lang.newSourceCode(new Offset(30, -pseudoSize, "codeMSP",
				AnimalScript.DIRECTION_NE), "codeBFS",
				null, codeProps);
		codeBFS.addCodeLine("Alternating-Breadth-First-Search", null, 0, null);
		codeBFS.addCodeLine("Create alternating paths:", null, 0, null);
		codeBFS.addCodeLine("direct unmatched edges from U to V", null, 1, null);
		codeBFS.addCodeLine("direct matched edges from V to U", null, 1, null);
		codeBFS.addCodeLine(
				"starting with unmatched Vertices in U, perform a Breadth-First-Search:",
				null, 0, null);
		codeBFS.addCodeLine("mark the layer in which each vertex is visited",
				null, 1, null);
		codeBFS.addCodeLine(
				"traverse edges leaving the vertices in the current layer (do not re-visit vertices)",
				null, 1, null);
		codeBFS.addCodeLine(
				"if a free vertex in V is found, return set of all free edges at layer",
				null, 0, null);
		codeBFS.addCodeLine("otherwise return {}", null, 0, null);
		codeBFS.hide();
		rectBFS = lang.newRect(new Offset(-5, -5, "codeBFS",
				AnimalScript.DIRECTION_NW), new Offset(5, 5, "codeBFS", "SE"),
				"rectBFS", null, new RectProperties());
		rectBFS.hide();
		// DFS
		codeDFS = lang.newSourceCode(new Offset(30, -pseudoSize, "codeMSP",
				AnimalScript.DIRECTION_NE), "codeDFS",
				null, codeProps);
		codeDFS.addCodeLine("Disjoint-Depth-First-Search", null, 0, null);
		codeDFS.addCodeLine(
				"starting with given vertex, perform a Deapth-First-Search:",
				null, 0, null);
		codeDFS.addCodeLine(
				"traverse only edges entering the currently viewed vertex (reverse) leading to a vertex in the previous layer",
				null, 1, null);
		codeDFS.addCodeLine("if a vertex at layer 0 is found:", null, 0, null);
		codeDFS.addCodeLine("return the edges on the path to the found vertex",
				null, 1, null);
		codeDFS.addCodeLine(
				"hide the vertices on the path temporarily (ensures disjoint paths)",
				null, 1, null);
		codeDFS.addCodeLine("otherwise return {}", null, 0, null);
		codeDFS.hide();
		rectDFS = lang.newRect(new Offset(-5, -5, "codeDFS",
				AnimalScript.DIRECTION_NW), new Offset(5, 5, "codeDFS", "SE"),
				"rectDFS", null, new RectProperties());
		rectDFS.hide();
	}

	public boolean validateInput(AnimationPropertiesContainer arg0,
			Hashtable<String, Object> arg1) throws IllegalArgumentException {
		int[][] matrix = (int[][]) arg1.get("bipartiteAdjacencyMatrix");
		for (int r = 0; r < matrix.length; r++)
			for (int c = 0; c < matrix[0].length; c++)
				if (matrix[r][c] != 0 && matrix[r][c] != 1) {
					throw new IllegalArgumentException(
							"The values of the matrix must be either 1 or 0. (See description)");
				}
		return true;
	}
}