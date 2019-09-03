/*
 * FarthestInsertion.java
 * Patricia Heidt, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.graph;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import algoanim.primitives.generators.Language;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.primitives.Graph;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;

public class FarthestInsertion implements Generator {
    private Language lang;
    private SourceCodeProperties sourceCode;
    private Graph graph;

    public void init(){
        lang = new AnimalScript("Farthest Insertion", "Patricia Heidt", 800, 600);
        lang.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        sourceCode = (SourceCodeProperties)props.getPropertiesByName("sourceCode");
        graph = (Graph)primitives.get("graph");

        // somehow setting the font size in the xml does not inflict the actual font size
        Font previousFont = (Font) sourceCode.get(AnimationPropertiesKeys.FONT_PROPERTY);
		Font newFont = new Font(previousFont.getName(), previousFont.getStyle(), 18);
		sourceCode.set(AnimationPropertiesKeys.FONT_PROPERTY, newFont);
        
        initialize();
        link();
        endPage();
        
        return lang.toString();
    }

    public String getName() {
        return "Farthest Insertion";
    }

    public String getAlgorithmName() {
        return "Farthest Insertion";
    }

    public String getAnimationAuthor() {
        return "Patricia Heidt";
    }

    public String getDescription(){
        return "Farthest Insertion ist eine Heuristik, die auf das Problem " 
        		+ "des Handelsreisenden angewendet werden kann.\n"
        		+ "Es geht darum, die k&uuml;rzeste Route zu finden, "
        		+ "die alle gefragten Punkte erreicht und am gleichen Punkt "
        		+ "endet, wo sie begonnen hat.\n"
        		+ "\n"
        		+"Der Ansatz von Farthest Insertion ist immer den Punkt, der "
        		+ "am weitesten entfernt ist, so in die bestehende Route "
        		+ "einzuf&uuml;gen,\n"
        		+ "dass sich die L&auml;nge der Route nur minimal erh&ouml;ht.\n"
        		+"Am weitesten entfernt bedeutet hier, dass er die "
        		+ "gr&ouml;&szlig;te Entfernung zu dem von ihm aus n&auml;chsten "
        		+ "bereits verbundenen\n"
        		+"Knoten hat.";
    }

    public String getCodeExample(){
        return "1. Zuf&aumllligen Knoten w&auml;hlen\n"
        		+ "2. Knoten finden, der weiteste Distanz zum n&auml;chsten "
        		+ "Routenknoten hat\n"
        		+"3. Diesen Knoten so einf&uuml;gen, dass k&uuml;rzester Weg "
        		+ "entsteht\n"
        		+"4. Falls noch unverbundene Knoten &uuml;brig sind, "
        		+ "zur&uuml;ck zu Schritt 2";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPH);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }
    
    private int nodeNum;
    
    private double randomDistance;
	private double completeDistance = 0;
	
	private static final String[] DESCRIPTION = {"Farthest Insertion ist eine Heuristik "
			+ "die auf das Problem des Handelsreisenden angewendet werden kann.",
			"Es geht darum, die k�rzeste Route zu finden, die alle gefragten "
			+ "Punkte erreicht und am gleichen Punkt endet, wo sie begonnen hat.",
			"",
			"Der Ansatz von Farthest Insertion ist immer den Punkt, der am weitesten "
			+ "entfernt ist so in die bestehende Route einzuf�gen,",
			"dass sich die L�nge der Route nur minimal erh�ht.",
			"Am weitesten entfernt bedeutet hier, dass er die gr��te Entfernung zu "
			+ "dem von ihm aus n�chsten bereits verbundenen Knoten hat."};
	
	private static final String[] INIT = {"Die hier gezeigten Knoten werden nun in den",
			"folgenden Schritten mithilfe der Farthest Insertion",
			"Heuristik verbunden."};
	
	private static final String[] PSEUDO_CODE = {"1. Zuf�lligen Knoten w�hlen",
			"2. Knoten finden, der weiteste Distanz zum n�chsten Routenknoten hat",
			"3. Diesen Knoten so einf�gen, dass k�rzester Weg entsteht",
			"4. Falls noch unverbundene Knoten �brig sind, zur�ck zu Schritt 2"};
	
	private String[] endText() {
		String[] text = {"In jedem Schritt wurde der am weitesten"
			+ " entfernte Knoten ausgew�hlt und m�glichst g�nstig",
			"in die bereits bestehende Route eingef�gt.",
			"Hierf�r wurden " + (nodeNum - 1) + " Iterationen ben�tigt.",
			"Die entstandene Route hat eine L�nge von " + String.format("%.2f", completeDistance) + ".",
			"Zum Vergleich, eine Route, in die die Knoten in zuf�lliger Reihenfolge aneinandergereit sind,",
			"h�tte beispielsweise eine L�nge von " + String.format("%.2f", randomDistance) + ".",
			"Auch wenn die k�rzeste Verbindung in diesem Beispiel vielleicht offensichtlich war, ist das,",
			"besonders bei vielen Punkten, nicht immer der Fall.",
			"",
			"Die Punkte im Graphen haben die folgenden Koordinaten:"};
		return text;
	}
	
	private List<Primitive> keepShown = new LinkedList<>();
	private SourceCodeProperties textProp;
	private SourceCode pseudoCode;
	
	public FarthestInsertion(Language l) {
		lang = l;
		lang.setStepMode(true);
	}
	
	public FarthestInsertion() {
		super();
	}
	
	/**
	 * Creates the first few pages and the graph
	 * @param nodeNum the number of nodes the graph will be created with
	 */
	private void initialize() {
		/*Create the header, its rectangle, and the explanation on the first page*/
		TextProperties headerProp = new TextProperties();
		Font headerFont = new Font(Font.MONOSPACED, Font.BOLD, 24);
		headerProp.set(AnimationPropertiesKeys.FONT_PROPERTY, headerFont);
		Text header = lang.newText(new Coordinates(30,30), "Farthest Insertion", "header", null, headerProp);
		keepShown.add(header);
		
		Offset rectUpperLeft = new Offset(-10, -10, header, "NW");
		Offset rectLowerRight = new Offset(10, 10, header, "SE");
		RectProperties rectProp = new RectProperties();
		rectProp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
		rectProp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 5);
		Rect rect = lang.newRect(rectUpperLeft, rectLowerRight, "rect", null, rectProp);
		keepShown.add(rect);
		
		textProp = new SourceCodeProperties();
		Font textFont = new Font(Font.SANS_SERIF, Font.PLAIN, 18);
		textProp.set(AnimationPropertiesKeys.FONT_PROPERTY, textFont);
		SourceCode description = lang.newSourceCode(new Coordinates(30, 100), "description", null, textProp);
		for (String line : DESCRIPTION) {
			description.addCodeLine(line, null, 0, null);
		}
		lang.nextStep("Einleitung");
		lang.hideAllPrimitivesExcept(keepShown);
		
		/*Create the graph at the right position*/
		Node[] unalteredNodes = graph.getNodes();
		nodeNum = graph.getSize();
		Coordinates[] nodes = new Coordinates[nodeNum];
		String[] labels = new String[nodeNum];
		int greatestX = 0;
		int greatestY = 0;
		for(int i = 0; i < nodeNum; i++){
			Coordinates node = nodeToCoordinates(unalteredNodes[i]);
			int x = node.getX();
			int y = node.getY();
			if (x > greatestX) {
				greatestX = x;
			}
			if (y > greatestY) {
				greatestY = y;
			}
			nodes[i] = new Coordinates(x + 800, y + 50);
			labels[i] = graph.getNodeLabel(i);
		}
		
		int[][] edges = new int[nodeNum][nodeNum];
		GraphProperties graphProp = graph.getProperties();
		graphProp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		graph = lang.newGraph("graph2", edges, nodes, labels, null, graphProp);
		
		/*Compute length with random node order to compare to at the end*/
		randomDistance = 0;
		for(int i = 0; i < nodes.length; i++){
			if(i + 1 == nodes.length) {
				randomDistance += euclideanDistance(nodes[i], nodes[0]);
			} else {
				randomDistance += euclideanDistance(nodes[i], nodes[i+1]);
			}
		}

		keepShown.add(graph);
		
		/*Create a coordinate system to better show the graph positioning*/
		PolylineProperties lineProp = new PolylineProperties();
		lineProp.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
		Coordinates[] xAxis = {new Coordinates(800, 50), new Coordinates(greatestX + 900, 50)};
		Polyline xLine = lang.newPolyline(xAxis , "x axis", null, lineProp);
		keepShown.add(xLine);
		Coordinates[] yAxis = {new Coordinates(800, 50), new Coordinates(800, greatestY + 150)};
		Polyline yLine = lang.newPolyline(yAxis , "y axis", null, lineProp);
		keepShown.add(yLine);
		TextProperties legendTextProp = new TextProperties();
		Font legendFont = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
		legendTextProp.set(AnimationPropertiesKeys.FONT_PROPERTY, legendFont);
		Text xLabel = lang.newText(new Coordinates(greatestX + 890, 20), "x", "x", null, legendTextProp);
		keepShown.add(xLabel);
		Text yLabel = lang.newText(new Coordinates(780, greatestY + 120), "y", "y", null, legendTextProp);
		keepShown.add(yLabel);
		for (int i = 0; i < greatestX + 20; i += 100) {
			Coordinates[] markerPos = {new Coordinates(800 + i, 50), new Coordinates(800 + i, 45)};
			Polyline marker = lang.newPolyline(markerPos, i + "x", null);
			keepShown.add(marker);
			Text markerLabel = lang.newText(new Coordinates(800 + i, 30), Integer.toString(i),
					i + "x label", null, legendTextProp);
			keepShown.add(markerLabel);
		}
		for (int i = 0; i < greatestY + 20; i += 100) {
			Coordinates[] markerPos = {new Coordinates(795, 50 + i), new Coordinates(800, 50 + i)};
			Polyline marker = lang.newPolyline(markerPos, i + "y", null);
			keepShown.add(marker);
			Coordinates labelCoords = new Coordinates(770, 40 + i);
			if (i == 0) {
				labelCoords = new Coordinates(785, 40 + i);
			}
			Text markerLabel = lang.newText(labelCoords, Integer.toString(i), i + "y label", null, legendTextProp);
			keepShown.add(markerLabel);
		}
		
		/*Create the explanation on the second page*/
		SourceCodeProperties initProp = new SourceCodeProperties();
		Font initFont = new Font(Font.SANS_SERIF, Font.BOLD, 20);
		initProp.set(AnimationPropertiesKeys.FONT_PROPERTY, initFont);
		Offset pseudoCodePos = new Offset(0, 50, header, "SW");
		SourceCode init = lang.newSourceCode(pseudoCodePos, "init", null, initProp);
		for (String line : INIT) {
			init.addCodeLine(line, null, 0, null);
		}
		
		/*List the coordinates of all nodes - only works for up to 15 nodes*/
		Offset coordinatePos = new Offset(20, 20, init, "SW");
		SourceCode coordinates = lang.newSourceCode(coordinatePos, "coordinates", null, textProp);
		listNodes(coordinates, nodes);
		
		/*Create the legend*/
		Font lesserHeaderFont = new Font(Font.MONOSPACED, Font.BOLD, 20);
		TextProperties lesserHeaderProp = new TextProperties();
		lesserHeaderProp.set(AnimationPropertiesKeys.FONT_PROPERTY, lesserHeaderFont);
		Node legendHeaderPos = new Offset(-560, 80, graph, "SW");
		List<Primitive> keepForNow = new LinkedList<>(keepShown);
		Text legendHeader = lang.newText(legendHeaderPos, "Legende:", "legendHeader", null, lesserHeaderProp);
		keepForNow.add(legendHeader);
		String[] legendText = {"wurde noch nicht bearbeitet:",
				"ist in Route enthalten:",
				"wird als n�chstes eingef�gt:",
				"ist Teil der Route:",
				"wird im n�chsten Schritt ersetzt:"};
		Node[] legendNodes = new Offset[7];
		String[] legendLabels = new String[7];
		for (int i = 0; i < 7; i++) {
			if (i < 3) {
				legendNodes[i] = new Offset(-500 + 200 * i, 150, graph, "SW");
			} else {
				legendNodes[i] = new Offset(50 + 100 * (i - 3), 150, graph, "SW");
			}
			legendLabels[i] = "";
		}
		int[][] legendEdges = new int[7][7];
		legendEdges[3][4] = 1;
		legendEdges[5][6] = 1;
		Graph legendGraph = lang.newGraph("legend", legendEdges, legendNodes, legendLabels, null, graphProp);
		keepForNow.add(legendGraph);
		for (int i = 1; i < 7; i++) {
			legendGraph.setNodeHighlightFillColor(i, Color.RED, null, null);
			if (i == 2) {
				legendGraph.setNodeHighlightFillColor(i, Color.BLUE, null, null);
			}
			legendGraph.highlightNode(i, null, null);
		}
		legendGraph.setEdgeHighlightPolyColor(5, 6, Color.BLUE, null, null);
		legendGraph.highlightEdge(5, 6, null, null);
		Node[] legendTextPos = new Offset[5];
		for (int i = 0; i < 5; i++) {
			legendTextPos[i] = new Offset(-550 + 200 * i, 120, graph, "SW");
			Text legText = lang.newText(legendTextPos[i], legendText[i], "legend_" + i, null, legendTextProp);
			keepForNow.add(legText);
		}
		
		lang.nextStep();
		lang.hideAllPrimitivesExcept(keepForNow);
		
		/*Create the pseudo code*/
		pseudoCode = lang.newSourceCode(pseudoCodePos, "pseudoCode", null, sourceCode);
		for (String line : PSEUDO_CODE) {
			pseudoCode.addCodeLine(line, null, 0, null);
		}
		pseudoCode.highlight(0);
	}
	
	/**
	 * Converts a node to a {@link Coordinates} object.
	 * @param node
	 * @return
	 */
	private Coordinates nodeToCoordinates(Node node) {
		if (node == null) {
			System.err.println("Node is null");
		}
		if (node.getClass().equals(Offset.class)) {
			Offset offset = (Offset)node;
			Coordinates reference = nodeToCoordinates(offset.getNode());
			int referenceX = reference.getX();
			int referenceY = reference.getY();
			int x = offset.getX();
			int y = offset.getY();
			String direction = offset.getDirection();
			if (direction.equals("NE")) {
				return new Coordinates(referenceX - x, referenceY + y);
			} else if (direction.equals("SE")) {
				return new Coordinates(referenceX + x, referenceY + y);
			} else if (direction.equals("SW")) {
				return new Coordinates(referenceX + x, referenceY - y);
			} else {
				return new Coordinates(referenceX - x, referenceY - y);
			}
		} else if (node.getClass().equals(Coordinates.class)) {
			return (Coordinates) node;
		} else {
			System.err.println("Class " + node.getClass() + " is not supported for graph positioning.");
			return new Coordinates(0, 0);
		}
	}
	/**
	 * Computes the euclidean distance between two coordinates.
	 * @param node1 one coordinate
	 * @param node2 the other coordinate
	 * @return the euclidean distance between node1 and node2.
	 */
	private double euclideanDistance(Node node1, Node node2) {
		int xDist = ((Coordinates)node1).getX() - ((Coordinates)node2).getX();
		int yDist = ((Coordinates)node1).getY() - ((Coordinates)node2).getY();
		return Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist, 2));
	}
	
	/**
	 * Finds the farthest node that is not selected yet.
	 * @param graph the graph
	 * @param linked the indices of the nodes which already are linked
	 * @param distances a new HashMap that the farthest node's distances to the nodes in the graph will be written to
	 * @return the node which is farthest from the nodes already linked
	 */
	private Coordinates findFarthest(Graph graph, Set<Integer> linked) {
		int size = graph.getSize();
		Coordinates farthestNode = null;
		double farthestDistance = 0;
		for (int i = 0; i < size; i++) {
			if (linked.contains(i)) {
				continue;
			}
			Coordinates node = (Coordinates) graph.getNode(i);
			double shortestDistance = Double.MAX_VALUE;
			for (int j : linked) {
				Coordinates otherNode = (Coordinates) graph.getNode(j);
				double dist = euclideanDistance(node, otherNode);
				if (dist < shortestDistance) {
					shortestDistance = dist;
				}
			}
			if (shortestDistance > farthestDistance) {
				farthestDistance = shortestDistance;
				farthestNode = node;
			}
		}
		return farthestNode;
	}
	
	/**
	 * Finds the two nodes in the graph in between which the new node needs to be inserted to
	 * cause lowest additional distance.
	 * @param graph the graph in which the nodes are
	 * @param linked the indices of all nodes in the graph which are already linked
	 * @param newNode the node to be inserted next
	 * @return the indices of the nodes in between which the new node should ideally be inserted
	 */
	private int[] cheapestInsertion(Graph graph, Set<Integer> linked, Coordinates newNode) {
		int[] cheapestNodes = new int[2];
		double cheapestAddDist = Double.MAX_VALUE;
		for (int node1 : linked) {
			Coordinates node1Coords = (Coordinates) graph.getNodeForIndex(node1);
			int[] edges = graph.getEdgesForNode(node1);
			for (int i = 0; i < edges.length; i++) {
				if (node1 >= i || edges[i] == 0) {
					continue;
				}
				Coordinates node2Coords = (Coordinates) graph.getNodeForIndex(i);
				double oldDist = euclideanDistance(node1Coords, node2Coords);
				double newDist1 = euclideanDistance(newNode, node1Coords);
				double newDist2 = euclideanDistance(newNode, node2Coords);
				double addDist = newDist1 + newDist2 - oldDist;
				if (addDist < cheapestAddDist) {
					cheapestNodes[0] = node1;
					cheapestNodes[1] = i;
					cheapestAddDist = addDist;
				}
			}
		}
		return cheapestNodes;
	}
	
	/**
	 * Modifies adjacencyMatrix so that an edge from the node with the lower
	 * index to the node with the higher index is added.
	 * @param adjacencyMatrix the adjacency Matrix of the graph which should be modified 
	 * @param node1
	 * @param node2
	 */
	private void addEdge(int[][] adjacencyMatrix, int node1, int node2) {
		if (node1 < node2) {
			adjacencyMatrix[node1][node2] = 1;
		} else if (node2 < node1) {
			adjacencyMatrix[node2][node1] = 1;
		} else {
			System.err.println("!!! ERROR : same node appears twice !");
			System.err.println(node1);
		}
	}
	
	private void listNodes(SourceCode text, Coordinates[] coordinates) {
		String next = "";
		for (int i = 0; i < coordinates.length; i++) {
			Coordinates node = coordinates[i];
			String label = graph.getNodeLabel(node);
			next += label + ": " + "(" + (node.getX() - 750) + ","
					+ (node.getY() - 50) + ") ; ";
			if(i % 4 == 3) {
				text.addCodeLine(next, null, 0, null);
				next = "";
			}
		}
		if (next != "") {
			text.addCodeLine(next, null, 0, null);
		}
	}
	
	/**
	 * Performs the farthest insertion algorithm to link all nodes of the graph
	 */
	private void link() {
		lang.nextStep();
		TreeSet<Integer> linked = new TreeSet<>();
		/*Add starting node*/
		linked.add(0);
		graph.setNodeHighlightFillColor(0, Color.BLUE, null, null);
		graph.highlightNode(0, null, null);
		lang.nextStep("Iteration 1");
		graph.setNodeHighlightFillColor(0, Color.RED, null, null);
		lang.nextStep();
		pseudoCode.unhighlight(0);
		pseudoCode.highlight(1);
		int nodeNum = graph.getSize();
		while (linked.size() < nodeNum) {
			lang.nextStep();
			Coordinates newNode = findFarthest(graph, linked);
			int newNodeIndex = graph.getPositionForNode(newNode);
			graph.setNodeHighlightFillColor(newNode, Color.BLUE, null, null);
			graph.highlightNode(newNode, null, null);
			lang.nextStep("Iteration " + (linked.size() + 1));
			pseudoCode.unhighlight(1);
			pseudoCode.highlight(2);
			lang.nextStep();
			/* even though this adds a lot of nonsense to the animalscript code
			 we need it so we can later find out which nodes are linked to which*/
			int[][] adjacencyMatrix = graph.getAdjacencyMatrix();
			if (linked.size() > 1) {
				int[] insertionNodes = cheapestInsertion(graph, linked, newNode);
				if (linked.size() > 2) {
					graph.setEdgeHighlightPolyColor(insertionNodes[0], insertionNodes[1], Color.BLUE, null, null);;
					graph.highlightEdge(insertionNodes[0], insertionNodes[1], null, null);
					lang.nextStep();
					graph.hideEdge(insertionNodes[0], insertionNodes[1], null, null);
					adjacencyMatrix[insertionNodes[0]][insertionNodes[1]] = 0;
					adjacencyMatrix[insertionNodes[1]][insertionNodes[0]] = 0;
				}
				graph.setNodeHighlightFillColor(newNode, Color.RED, null, null);
				addEdge(adjacencyMatrix, newNodeIndex, insertionNodes[0]);
				addEdge(adjacencyMatrix, newNodeIndex, insertionNodes[1]);
				completeDistance += euclideanDistance(newNode, (Coordinates) graph.getNode(insertionNodes[0]));
				completeDistance += euclideanDistance(newNode, (Coordinates) graph.getNode(insertionNodes[1]));
				completeDistance -= euclideanDistance((Coordinates) graph.getNode(insertionNodes[0]), (Coordinates) graph.getNode(insertionNodes[1]));
			} else {
				int currentlySelected = linked.first();
				graph.setNodeHighlightFillColor(newNode, Color.RED, null, null);
				addEdge(adjacencyMatrix, newNodeIndex, currentlySelected);
				completeDistance = 2 * euclideanDistance(newNode, (Coordinates) graph.getNode(currentlySelected));
			}
			graph.setAdjacencyMatrix(adjacencyMatrix);
			linked.add(graph.getPositionForNode(newNode));
			lang.nextStep();
			pseudoCode.unhighlight(2);
			pseudoCode.highlight(3);
			if (linked.size() == nodeNum) {
				lang.nextStep("Endergebnis");
			} else {
				lang.nextStep();
			}
			pseudoCode.unhighlight(3);
			pseudoCode.highlight(1);
		}
	}
	
	/**
	 * Produces the end page by hiding everything except the header and its box and writing the end text.
	 */
	private void endPage() {
		lang.hideAllPrimitivesExcept(keepShown);
		SourceCode outro = lang.newSourceCode(new Coordinates(30,100), "outro", null, textProp);
		for (String line : endText()) {
			outro.addCodeLine(line, null, 0, null);
		}
		/*List all nodes in the graph*/
		Offset coordinatePos = new Offset(20, 20, outro, "SW");
		SourceCode coordinates = lang.newSourceCode(coordinatePos, "coordinates", null, textProp);
		Coordinates[] nodes = (Coordinates[]) graph.getNodes();
		listNodes(coordinates, nodes);
	}

}