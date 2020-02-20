/*
 * KahnsAlgo.java
 * Niclas Dobbertin and Niklas Adam, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.graph.kahn;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;
import java.util.stream.IntStream;

import algoanim.animalscript.AnimalGraphGenerator;
import algoanim.animalscript.AnimalRectGenerator;
import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.AnimalStringArrayGenerator;
import algoanim.animalscript.AnimalTextGenerator;
import algoanim.primitives.Graph;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.AnimationType;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

public class KahnsAlgo implements Generator {
	private Language lang;
	private SourceCodeProperties scProps;
	private ArrayProperties arrProps;

	private Graph protograph;
	private Graph graph;
	private int[][] adjacencyMatrix;
	private String[] nodeNames;
	private GraphProperties gprops;
	private StringArray startNodes;
	private StringArray sortedNodes;
	private SourceCode code;

	public KahnsAlgo() {
		lang = Language.getLanguageInstance(AnimationType.ANIMALSCRIPT,
				"Kahn's Algorithm in Pseudo", "Niclas Dobbertin and Niklas Adam", 800, 600);
	}

	public void init(){
		lang = new AnimalScript("Kahn's Algorithm in Pseudo", "Niclas Dobbertin and Niklas Adam", 800, 600);
	}

	public void start() {

		//Header
		TextProperties headProps = new TextProperties();
		headProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 24));
		headProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		AnimalTextGenerator textGen = new AnimalTextGenerator((AnimalScript) lang);
		Text header = new Text(textGen, new Coordinates(20, 30), "Khan's Algorithm", "header", null, headProps);

		//Rectangle
		RectProperties rectProps = new RectProperties();
		rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		AnimalRectGenerator rectGen = new AnimalRectGenerator((AnimalScript) lang);
		Rect rect = new Rect(rectGen, new Offset(-5, -5, header, "NW"), new Offset(5, 5, header, "SE"), "rect", null, rectProps);

		// Description
		TextProperties textProps = new TextProperties();
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 14));
		textProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		Text desc0 = new Text(textGen, new Offset(2, 4, rect, "SW"), "A topological sort of a directed graph is a linear", "desc0", null, textProps);
		Text desc1 = new Text(textGen, new Offset(0, 2, desc0, "SW"), "ordering of its nodes so that for every edge AB", "desc1", null, textProps);
		Text desc2 = new Text(textGen, new Offset(0, 2, desc1, "SW"), "from node A to node B, A comes before B in the", "desc2", null, textProps);
		Text desc3 = new Text(textGen, new Offset(0, 2, desc2, "SW"), "ordering. For example this means that nodes with", "desc3", null, textProps);
		Text desc4 = new Text(textGen, new Offset(0, 2, desc3, "SW"), "no incoming edges at all will be represented", "desc4", null, textProps);
		Text desc5 = new Text(textGen, new Offset(0, 2, desc4, "SW"), "earlier or at the beginning of the sort and nodes", "desc5", null, textProps);
		Text desc6 = new Text(textGen, new Offset(0, 2, desc5, "SW"), "with just incoming and no outgoing edges more to", "desc6", null, textProps);
		Text desc7 = new Text(textGen, new Offset(0, 2, desc6, "SW"), "the end.", "desc7", null, textProps);
		Text desc8 = new Text(textGen, new Offset(0, 2, desc7, "SW"), "One of these algorithms, first described by", "desc8", null, textProps);
		Text desc9 = new Text(textGen, new Offset(0, 2, desc8, "SW"), "Kahn (1962), works by choosing vertices in the", "desc9", null, textProps);
		Text desc10 = new Text(textGen, new Offset(0, 2, desc9, "SW"), "same order as the eventual topological sort.", "desc10", null, textProps);
		
		//SourceCode
		code = lang.newSourceCode(new Offset(120, 68	, rect, "NE"), "sourceCode", null, scProps);
		code.addCodeLine("find nodes with no incoming edges to initialize S", "1", 0, null);
		code.addCodeLine("while S is non-empty do", null, 0, null);
		code.addCodeLine("remove a node n from S", null, 1, null);
		code.addCodeLine("add n to tail of L", null, 1, null);
		code.addCodeLine("for each node m with an edge e from n to m do", null, 1, null);
		code.addCodeLine("remove edge e from the graph", null, 2, null);
		code.addCodeLine("if m has no other incoming edges then", null, 2, null);
		code.addCodeLine("insert m into S", null, 3, null);
		code.addCodeLine("if graph has edges then", null, 0, null);
		code.addCodeLine("return error   (graph has at least one cycle)", null, 1, null);
		code.addCodeLine("else", null, 0, null);
		code.addCodeLine("return L   (a topologically sorted order)", null, 1, null);

		lang.nextStep();
		

		desc0.hide();
		desc1.hide();
		desc2.hide();
		desc3.hide();
		desc4.hide();
		desc5.hide();
		desc6.hide();
		desc7.hide();
		desc8.hide();
		desc9.hide();
		desc10.hide();
		
		// Graph
		gprops = new GraphProperties();
		gprops.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, true);
		gprops.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.blue);
		gprops.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY, Color.white);
		gprops.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.white);
		AnimalGraphGenerator graphGen = new AnimalGraphGenerator((AnimalScript) lang);
		graph = new Graph(graphGen, protograph, null, gprops);
		adjacencyMatrix = graph.getAdjacencyMatrix();
		nodeNames = new String[graph.getSize()];
		for(int i = 0; i<graph.getSize(); i++)
			nodeNames[i] = graph.getNodeLabel(i);

		// Arrays
		AnimalStringArrayGenerator arrGen = new AnimalStringArrayGenerator(lang);
		String[] initValues = new String[graph.getNodes().length];
		for(int i = 0; i<initValues.length; i++)
			initValues[i] = " ";
		startNodes = new StringArray(arrGen, new Offset(0, 50, graph, "SW"), initValues, "startArray", null, arrProps);
		sortedNodes = new StringArray(arrGen, new Offset(0, 20, startNodes, "W"), initValues, "sortedArray", null, arrProps);
		Text startNodesLabel = new Text(textGen, new Offset(25, -11, startNodes, "E"), "S  -  List containing nodes with no incoming edges", "startNodesLabel", null, textProps);
		Text sortedNodesLabel = new Text(textGen, new Offset(25, -11, sortedNodes, "E"), "L  -  List containing sorted elements", "sortedNodesLabel", null, textProps);
		lang.nextStep();

		kahn();
	}

	private void kahn() {
		// Find nodes with no incoming edges  to initialize S
		ArrayList<Node> startNodeArr = getOrigins();
		code.highlight(0);
		int arrId = 0;
		for(Node n : startNodeArr) {
			graph.highlightNode(n, null, null);
			startNodes.put(arrId, graph.getNodeLabel(n), null, null);
			startNodes.highlightCell(arrId, null, null);
			arrId++;
		}

		lang.nextStep();

		// Init
		int[][] incomingEdges = new int[graph.getNodes().length][graph.getNodes().length];
		for(int i = 0; i<graph.getNodes().length; i++) {
			for(int j=0; j<graph.getNodes().length; j++) {
				if(graph.getAdjacencyMatrix()[j][i] != 0)
					incomingEdges[i][j] = 1;
			}
		}
		int currentSortedPos = 0;
		while(!startNodeArr.isEmpty()) {
			highlightSwitch(2);
			code.highlight(1);
			startNodes.highlightCell(0, startNodes.getLength()-1, null, null);
			for(Node n : graph.getNodes()) {
				graph.unhighlightNode(n, null, null);
			}

			lang.nextStep();
			// Remove a node from S
			code.unhighlight(1);
			code.highlight(2);
			Node n = startNodeArr.get(0);
			startNodeArr.remove(0);
			startNodes.unhighlightCell(1, startNodes.getLength()-1, null, null);
			startNodes.put(0, " ", null, null);
			graph.highlightNode(n, null, null);

			lang.nextStep();
			// Add n to tail of L
			code.unhighlight(2);
			code.highlight(3);
			startNodes.unhighlightCell(0, null, null);
			sortedNodes.highlightCell(currentSortedPos, null, null);
			sortedNodes.put(currentSortedPos, graph.getNodeLabel(n), null, null);
			currentSortedPos++;

			shuffleLeft();

			lang.nextStep();
			// For each node m with an edge e from n to m do
			code.unhighlight(3);
			sortedNodes.unhighlightCell(0, sortedNodes.getLength()-1, null, null);
			int[] nEdges = graph.getEdgesForNode(n);
			ArrayList<Node> mlist = new ArrayList<Node>();
			for(int i = 0; i< nEdges.length; i++) {
				//System.out.println("EDGES "+i+": "+nEdges[i]);
				if(nEdges[i] != 0)
					mlist.add(graph.getNode(i));
			}


			for(Node m : mlist) {
				graph.highlightNode(n, null, null);
				code.unhighlight(7);
				code.highlight(4);
				graph.highlightEdge(n, m, null, null);
				lang.nextStep();

				// remove edge e from the graph
				code.unhighlight(4);
				code.highlight(5);
				graph.hideEdge(n, m, null, null);
				incomingEdges[graph.getPositionForNode(m)][graph.getPositionForNode(n)] = 0;

				lang.nextStep();

				//if m has no other incoming edges
				code.unhighlight(5);
				code.highlight(6);
				graph.unhighlightNode(n, null, null);
				graph.highlightNode(m, null, null);
				boolean noIncomingE = true;
				for(int i = 0; i < graph.getNodes().length; i++) {
					if(incomingEdges[graph.getPositionForNode(m)][i] != 0) {
						graph.setEdgeHighlightPolyColor(i, graph.getPositionForNode(m), Color.RED, null, null);
						graph.highlightEdge(i, graph.getPositionForNode(m), null, null);
						noIncomingE = false;
					}
				}
				lang.nextStep();

				highlightSwitch(3);
				highlightSwitch(0);
				code.unhighlight(6);
				//insert m into S
				if(noIncomingE) {
					code.highlight(7);
					startNodeArr.add(m);
					startNodes.put(startNodeArr.size()-1, graph.getNodeLabel(m), null, null);
					startNodes.highlightCell(startNodeArr.size()-1, null, null);
					lang.nextStep();
				}
				graph.unhighlightNode(m, null, null);
			}

		}
		highlightSwitch(1);
		// if graph has edges then
		code.unhighlight(1);
		code.highlight(8);
		int[] temp = new int[graph.getSize()];
		for(int i = 0; i<graph.getSize(); i++)
			temp[i] = IntStream.of(incomingEdges[i]).sum();
		if(IntStream.of(temp).sum() != 0) {
			highlightSwitch(4);
			lang.nextStep();

			// return error (graph has at least on cycle)
			code.unhighlight(8);
			code.highlight(9);
			lang.nextStep();

		}else {
			// else
			code.unhighlight(8);
			code.highlight(10);
			lang.nextStep();
			// returnL
			code.unhighlight(10);
			code.highlight(11);
			sortedNodes.highlightCell(0, sortedNodes.getLength()-1, null, null);
			lang.nextStep();

		}

		// else 
	}

	private void highlightSwitch(int primitive) {
		switch(primitive) {
		case(0):	//Edges
			for(int i = 0; i<graph.getSize(); i++)
				for(int j = 0; j<graph.getSize(); j++)
					graph.unhighlightEdge(i, j, null, null);
		break;
		case(1):	//Nodes
			for(int i = 0; i<graph.getSize(); i++)
				graph.unhighlightNode(i, null, null);
		break;	
		case(2):	//Sourcecode
			for(int i=0; i < code.length(); i++) 
				code.unhighlight(i);
		break;
		case(3):	//BlueEdge
			for(int i = 0; i<graph.getSize(); i++)
				for(int j = 0; j<graph.getSize(); j++)
					graph.setEdgeHighlightPolyColor(i, j, Color.BLUE, null, null);
		break;
		case(4):	//RedEdge
			for(int i = 0; i<graph.getSize(); i++)
				for(int j = 0; j<graph.getSize(); j++) {
					graph.highlightEdge(i, j, null, null);
					graph.setEdgeHighlightPolyColor(i, j, Color.RED, null, null);
				}
		break;
		}
	}

	private void shuffleLeft() {
		for(int i = 0; i<startNodes.getLength()-1; i++)
			startNodes.swap(i, i+1, null, null);
	}

	private ArrayList<Node> getOrigins(){
		ArrayList<Node> origins = new ArrayList<Node>();
		Node[] nodes = graph.getNodes();
		for(Node n: nodes)
			origins.add(n);
		for(int i = 0; adjacencyMatrix.length>i; i++) {
			for(int j = 0; adjacencyMatrix.length>j; j++) {
				if(adjacencyMatrix[j][i]!=0) {
					origins.remove(nodes[i]);
				}
			}
		}
		return origins;
	}

	public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
		lang.setStepMode(true);

		scProps = (SourceCodeProperties)props.getPropertiesByName("sourceCode");
		arrProps = (ArrayProperties)props.getPropertiesByName("array");
		protograph = (Graph)primitives.get("graph");
		start();		
		System.out.println(lang);
		return lang.toString();
	}

	public String getName() {
		return "Kahn's Algorithm in Pseudo";
	}

	public String getAlgorithmName() {
		return "Kahn's Algorithm";
	}

	public String getAnimationAuthor() {
		return "Niclas Dobbertin and Niklas Adam";
	}

	public String getDescription(){
		return "A topological sort of a directed graph is a linear "
				+ "ordering of its nodes so that for every edge AB "
				+ "from node A to node B, A comes before B in the "
				+ "ordering. For example this means that nodes with "
				+ "no incoming edges at all will be represented "
				+ "earlier or at the beginning of the sort and nodes "
				+ "with just incoming and no outgoing edges more to "
				+ "the end. \n"
				+ "One of these algorithms, first described by "
				+ "Kahn (1962), works by choosing vertices in the "
				+ "same order as the eventual topological sort.";
	}

	public String getCodeExample(){
		return "find nodes with no incoming edges to initialize S"
				+"\nwhile S is non-empty do"
				+"\n        remove a node n from S"
				+"\n        add n to tail of L"
				+"\n        for each node m with an edge e from n to m do"
				+"\n                remove edge e from the graph"
				+"\n                if m has no other incoming edges then"
				+"\n                        insert m into S"
				+"\nif graph has edges then"
				+"\n        return error   (graph has at least one cycle"
				+"\nelse"
				+"\n        return L   (a topologically sorted order)";
	}

	public String getFileExtension(){
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


	public static void main(String[] args) {
		KahnsAlgo generator = new KahnsAlgo(); // Generator erzeugen
		Animal.startGeneratorWindow(generator); // Animal mit Generator starten
	}
}
