package generators.searching;

/**
 * Abgabe für 7
 */
import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Graph;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.Timing;

/**
 * Die Aufgabe wurde als Gruppe bearbeitet
 * 1. Oksana Kolach 1169296
 * 2. Michael Drescher 1200744
 * 
 * @version 2
 */
public class AStarGenerator extends AnnotatedAlgorithm implements Generator {

	// Animal Objects
	private Language l; 
	private SourceCode source; 
	private static Map<SourceCode, int[]> lastHighlight = new HashMap<SourceCode, int[]>(); 
	private Timing arrayChangeDelay = new MsTiming(100); 
	private Timing arrayChangeDuration = new MsTiming(300); 
	
	// Algorithm Data	
	private int[] cameFrom; 
	private int[] gScore; 
	private int[] fScore; 
	private int[] hScore; 
	private int[][] adjacencyMatrix; 
	private Set<Integer> openSet = new HashSet<Integer>();
	private Set<Integer> closedSet= new HashSet<Integer>(); 
	private int startNode; 
	private int goalNode;
	private Graph graph; 
	
//	private StringMatrix arrayG;
//	private StringMatrix arrayH;
//	private StringMatrix arrayF;
	private String[] nodeLabels;
	private MatrixProperties mp;
	private TextProperties labelProps;
	private StringMatrix arrayOC;
	private StringMatrix arrayCameFrom; 
	
	// Animal Zeugs ============================================================
	
	private void highlighLineOnly(SourceCode source, int... line) {
		
		if (lastHighlight.get(source) != null)
			for (int h: lastHighlight.get(source))
				source.unhighlight(h); 
		
		lastHighlight.put(source, line); 
		
		if (line != null)
			for (int h: lastHighlight.get(source))
				source.highlight(h); 
	}
	
	public AStarGenerator() {
	}
	
	@Override
	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {

		init(); 
		
		arrayChangeDelay = new MsTiming((Integer)primitives.get("arrayChangeDelay"));
		arrayChangeDuration = new MsTiming((Integer)primitives.get("arrayChangeDuration"));
		
		Color nodeFillColor = (Color)primitives.get("nodeFillColor"); 
		Color nodeTextColor = (Color)primitives.get("nodeTextColor"); 
		Color nodeHighlightColor = (Color)primitives.get("nodeHighlightColor"); 
		
		Font headerFont = new Font("SansSerif", Font.BOLD, 20); 
		TextProperties headerProps = new TextProperties(); 
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, headerFont); 
		l.newText(new Coordinates(30, 30), "A* Suche", "header", null, headerProps);
		
		SourceCodeProperties sourceProps = new SourceCodeProperties(); 
		sourceProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED); 
		source = l.newSourceCode(new Offset(0, 300, "header", AnimalScript.DIRECTION_SW), "source", null, sourceProps); 
		source.addCodeLine("function A*(start,goal,h_score) @declare(\"int\"", "1", 0, null);
		source.addCodeLine("closedSet := the empty set                 // The set of nodes already evaluated.", "2", 1, null);
		source.addCodeLine("openSet := set containing the initial node // The set of tentative nodes to be evaluated.", "3", 1, null);
		source.addCodeLine("g_score[start] := 0                        // Distance from start along optimal path.", "4", 1, null);
		source.addCodeLine("f_score[start] := h_score[start]           // Estimated total distance from start to goal through y.", "5", 1, null);
		source.addCodeLine("while openSet is not empty", "6", 1, null);
		source.addCodeLine("x := the node in openSet having the lowest f_score[] value", "7", 2, null);
		source.addCodeLine("if x = goal", "8", 2, null);
		source.addCodeLine("return reconstruct_path(came_from[goal])", "9", 3, null);
		source.addCodeLine("remove x from openSet", "10", 2, null);
		source.addCodeLine("add x to closedSet", "11", 2, null);
		source.addCodeLine("foreach y in neighbor_nodes(x)", "12", 2, null);
		source.addCodeLine("if y in closedSet", "13", 3, null);
		source.addCodeLine("continue", "14", 4, null);
		source.addCodeLine("tentative_g_score := g_score[x] + dist_between(x,y)", "15", 3, null);
		source.addCodeLine("", "16", 3, null);
		source.addCodeLine("if y not in openSet", "17", 3, null);
		source.addCodeLine("add y to openSet", "18", 4, null);
		source.addCodeLine("tentative_is_better := true", "19", 4, null);
		source.addCodeLine("else", "22", 3, null);
		source.addCodeLine("tentative_is_better := tentative_g_score < g_score[y]", "23", 4, null);
		source.addCodeLine("", "23_0", 0, null);
		source.addCodeLine("if tentative_is_better = true", "24", 3, null);
		source.addCodeLine("came_from[y] := x", "25", 4, null);
		source.addCodeLine("g_score[y] := tentative_g_score", "26", 4, null);
		source.addCodeLine("f_score[y] := g_score[y] + h_score[y]", "27", 4, null);
		source.addCodeLine("return failure", "28", 1, null);
		source.addCodeLine("", "29", 0, null);
		source.addCodeLine("function reconstruct_path(current_node)", "30", 0, null);
		source.addCodeLine("if came_from[current_node] is set", "31", 1, null);
		source.addCodeLine("p = reconstruct_path(came_from[current_node])", "32", 2, null);
		source.addCodeLine("return (p + current_node)", "33", 2, null);
		source.addCodeLine("else", "34", 1, null);
		source.addCodeLine("return current_node", "35", 2, null);
		
		startNode = 0; 
		goalNode = 4; 
		adjacencyMatrix = new int[][] {
				new int[] {0, 3, 3, 0, 0}, 
				new int[] {3, 0, 0, 2, 0}, 
				new int[] {3, 0, 0, 0, 9}, 
				new int[] {0, 2, 0, 0, 4}, 
				new int[] {0, 0, 9, 4, 0}
		}; 
		hScore = new int[] {5, 3, 2, 2, 0}; 
		nodeLabels = new String[] {"A", "B", "C", "D", "E"}; 
		
		GraphProperties graphProps = new GraphProperties(); 
		graphProps.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, false); 
		graphProps.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, true); 
		graphProps.set(AnimationPropertiesKeys.FILL_PROPERTY, nodeFillColor); 
		graphProps.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY, nodeTextColor);
		graphProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, nodeHighlightColor);
		graph = l.newGraph("graph",   
				adjacencyMatrix, 
				new Node[] {
					new Offset(40, 150, "header", AnimalScript.DIRECTION_NW), 
					new Offset(160, 50, "header", AnimalScript.DIRECTION_NW), 
					new Offset(160, 250, "header", AnimalScript.DIRECTION_NW), 
					new Offset(340, 50, "header", AnimalScript.DIRECTION_NW), 
					new Offset(340, 250, "header", AnimalScript.DIRECTION_NW)
				}, 
				nodeLabels, null, graphProps); 
		//graph.highlightNode(1, arrayChangeDelay, arrayChangeDuration); 
		
		Font labelFont = new Font("SansSerif", Font.BOLD, 16); 
		labelProps = new TextProperties(); 
		labelProps.set(AnimationPropertiesKeys.FONT_PROPERTY, labelFont); 
		mp = new MatrixProperties(); 
		mp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE); 
		
		l.newText(new Offset(400, 30, "header", AnimalScript.DIRECTION_SW), "h[]  Werte", "h_values", null, labelProps);
//		arrayH = 
		l.newStringMatrix(new Offset(0, 10, "h_values", AnimalScript.DIRECTION_SW), 
				new String[][] {nodeLabels, toStringArray(hScore)}, "h_array", null, mp); 
		
		l.nextStep(); 
		doAStar(); 

		return l.toString(); 
	}
	
	private String[] toStringArray(int[] bla) {
		String[] blub = new String[bla.length]; 
		for (int i = 0; i < bla.length; i++) {
			switch (bla[i]) {
			case Integer.MAX_VALUE: 
				blub[i] = "MAX"; 
				break; 
			case -1: 
				blub[i] = "-";
				break; 
			default: 
				blub[i] = Integer.toString(bla[i]); 
			}
		}
		return blub; 
	}

	@Override
	public String getAlgorithmName() {
		return "A* Suche";
	}

	@Override
	public String getAnimationAuthor() {
		return "Oksana Kolach, Michael Drescher";
	}

	@Override
	public String getCodeExample() {
		return 
				"function A*(start,goal,h_score)\n" + 
				"    closedSet := the empty set                 // The set of nodes already evaluated.     \n" + 
				"    openSet := set containing the initial node // The set of tentative nodes to be evaluated.\n" + 
				"    g_score[start] := 0                        // Distance from start along optimal path.\n" + 
				"    f_score[start] := h_score[start]           // Estimated total distance from start to goal through y.\n" + 
				"    while openSet is not empty\n" + 
				"        x := the node in openSet having the lowest f_score[] value\n" + 
				"        if x = goal\n" + 
				"            return reconstruct_path(came_from[goal])\n" + 
				"        remove x from openSet\n" + 
				"        add x to closedSet\n" + 
				"        foreach y in neighbor_nodes(x)\n" + 
				"            if y in closedSet\n" + 
				"                continue\n" + 
				"            tentative_g_score := g_score[x] + dist_between(x,y)\n" + 
				"\n" + 
				"            if y not in openSet\n" + 
				"                add y to openSet\n" + 
				"                tentative_is_better := true\n" + 
				"            else\n" + 
				"                tentative_is_better := tentative_g_score < g_score[y]\n" + 
				"            if tentative_is_better = true\n" + 
				"                came_from[y] := x\n" + 
				"                g_score[y] := tentative_g_score\n" + 
				"                f_score[y] := g_score[y] + h_score[y]\n" + 
				"    return failure\n" + 
				"\n" + 
				"function reconstruct_path(current_node)\n" + 
				"    if came_from[current_node] is set\n" + 
				"        p = reconstruct_path(came_from[current_node])\n" + 
				"        return (p + current_node)\n" + 
				"    else\n" + 
				"        return current_node\n"; 
	}

	@Override
	public Locale getContentLocale() {
		return Locale.GERMANY;
	}

	@Override
	public String getDescription() {
		return "Der A*-Algorithmus (\"A Stern\" oder englisch \"A star\") geh&ouml;rt zur Klasse der informierten Suchalgorithmen. " + 
		    "Er dient in der Informatik der Berechnung eines k&uuml;rzesten Pfades zwischen zwei Knoten in einem Graphen mit " + 
		    "positiven Kantengewichten. Er wurde das erste Mal 1968 von Peter Hart, Nils J. Nilsson und Bertram Raphael " + 
		    "beschrieben.<br />" + 
		    "Im Gegensatz zu uninformierten Suchalgorithmen verwendet der A*-Algorithmus eine Sch&auml;tzfunktion (Heuristik), " + 
		    "um zielgerichtet zu suchen und damit die Laufzeit zu verringern. Der Algorithmus ist optimal. Das hei&szlig;t, es wird " + 
		    "immer die optimale L&ouml;sung gefunden, falls eine existiert.<br />Quelle: Wikipedia";
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
	public String getName() {
		return "A* Suche";
	}

	@Override
	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	@Override
	public void init() {
		l = new AnimalScript("A* Suche", "Oksana Kolach & Michael Drescher", 640, 480); 
		l.setStepMode(true); 		
	}
	
	// Implementierung =========================================================
	
	private List<Integer> doAStar() {
		
		int numNodes = adjacencyMatrix.length; 
		
		// TO DO dynamisch an knotenanzahl und startknoten anpassen
		highlighLineOnly(source, 1, 2); 
		closedSet = new HashSet<Integer>(); 
		openSet.add(startNode); 
		l.newText(new Offset(200, 0, "h_values", AnimalScript.DIRECTION_NE), "open (o) / closed (c)", "oc_values", null, labelProps);
		arrayOC = l.newStringMatrix(new Offset(0, 10, "oc_values", AnimalScript.DIRECTION_SW), 
				new String[][] {nodeLabels, new String[] {"o", "", "", "", ""}}, "oc_array", null, mp); 
		l.nextStep(); 

		// initialise g_score
		highlighLineOnly(source, 3); 
		gScore = new int[numNodes]; 
		for (int i = 0; i < numNodes; i++) {
			gScore[i] = Integer.MAX_VALUE; 
		}
		gScore[startNode] = 0; 
		l.newText(new Offset(0, 20, "h_array", AnimalScript.DIRECTION_SW), "g[]  Werte", "g_values", null, labelProps);
//		arrayG = 
		l.newStringMatrix(new Offset(0, 10, "g_values", AnimalScript.DIRECTION_SW), 
				new String[][] {nodeLabels, toStringArray(gScore)}, "g_array", null, mp); 
		l.nextStep(); 
		
		// initialise f_score
		highlighLineOnly(source, 4); 
		l.nextStep(); 
		fScore = new int[numNodes]; 
		for (int i = 0; i < numNodes; i++) {
			fScore[i] = Integer.MAX_VALUE; 
		}
		fScore[startNode] = hScore[startNode];
		l.newText(new Offset(0, 20, "g_array", AnimalScript.DIRECTION_SW), "f[]  Werte", "f_values", null, labelProps);
//		arrayF = 
		l.newStringMatrix(new Offset(0, 10, "f_values", AnimalScript.DIRECTION_SW), 
				new String[][] {nodeLabels, toStringArray(fScore)}, "f_array", null, mp); 
		l.nextStep(); 
		
		// init camefrom
		cameFrom = new int[numNodes]; 
		for (int i = 0; i < numNodes; i++) {
			cameFrom[i] = -1; 
		}
		l.newText(new Offset(0, 20, "oc_array", AnimalScript.DIRECTION_SW), "came_from Werte", "camefrom_values", null, labelProps);
		arrayCameFrom = l.newStringMatrix(new Offset(0, 10, "camefrom_values", AnimalScript.DIRECTION_SW), 
				new String[][] {nodeLabels, toStringArray(cameFrom)}, "camefrom_array", null, mp); 
		l.nextStep(); 
		
		while (!openSet.isEmpty()) {
			highlighLineOnly(source, 5); 
			l.nextStep(); 
			
			highlighLineOnly(source, 6); 
			l.nextStep(); 
			int x = getMinimalNodeFromOpenSet();
			graph.highlightNode(x, arrayChangeDelay, arrayChangeDuration); 
			
			highlighLineOnly(source, 7); 
			l.nextStep(); 
			if (x == goalNode) {
				highlighLineOnly(source, 8); 
				l.nextStep(); 
				return reconstructPath(x); 
			}
			
			highlighLineOnly(source, 9); 
			l.nextStep(); 
			openSet.remove(x);
			arrayOC.put(1, x, "", arrayChangeDelay, arrayChangeDuration); 
			l.nextStep(); 
			
			highlighLineOnly(source, 10);
			l.nextStep(); 
			closedSet.add(x); 
			arrayOC.put(1, x, "c", arrayChangeDelay, arrayChangeDuration); 
			l.nextStep(); 
			
			for (int y = 0; y < numNodes; y++) {
				if (adjacencyMatrix[x][y] <= 0) 
					continue; 
				
				highlighLineOnly(source, 11); 
				l.nextStep();
				
				highlighLineOnly(source, 12); 
				l.nextStep(); 
				if (closedSet.contains(y)) {
					highlighLineOnly(source, 13); 
					l.nextStep();
					continue; 
				}
				
				highlighLineOnly(source, 14); 
				l.nextStep(); 
				int tentativeGScore = gScore[x] + adjacencyMatrix[x][y]; 
				
				highlighLineOnly(source, 16); 
				l.nextStep(); 
				boolean tentativeIsBetter; 
				if (!openSet.contains(y)) {
					
					highlighLineOnly(source, 17); 
					l.nextStep(); 
					openSet.add(y); 
					arrayOC.put(1, y, "o", arrayChangeDelay, arrayChangeDuration); 
					l.nextStep(); 
					
					highlighLineOnly(source, 18); 
					l.nextStep(); 
					tentativeIsBetter = true; 
				} else {
					
					highlighLineOnly(source, 19); 
					l.nextStep(); 
					
					highlighLineOnly(source, 20); 
					l.nextStep(); 
					tentativeIsBetter = tentativeGScore < gScore[y]; 
				}
				
				highlighLineOnly(source, 22); 
				l.nextStep(); 
				if (tentativeIsBetter) {
					
					highlighLineOnly(source, 23); 
					l.nextStep(); 
					cameFrom[y] = x;
					arrayCameFrom.put(1, y, nodeLabels[x], arrayChangeDelay, arrayChangeDuration); 
					
					highlighLineOnly(source, 24); 
					l.nextStep(); 
					gScore[y] = tentativeGScore; 
					
					highlighLineOnly(source, 25); 
					l.nextStep(); 
					fScore[y] = gScore[y] + hScore[y]; 
				}
			}
		}
		
		highlighLineOnly(source, 26); 
		return null; 
	}
	
	private List<Integer> reconstructPath(int node) {
		List<Integer> l = null; 
		if (cameFrom[node] == -1) 
			l = new LinkedList<Integer>(); 
		else
			l = reconstructPath(cameFrom[node]); 
		l.add(node); 
		return l; 
	}
	
	private int getMinimalNodeFromOpenSet() {
		int bestNode = -1; 
		int bestScore = -1; 
		for (int node: openSet) {
			if (bestNode == -1 || fScore[node] < bestScore) {
				bestNode = node; 
				bestScore = fScore[node]; 
			}
		}
		return bestNode; 
	}

	@Override
	public String getAnnotatedSrc() {
		return 
		"function A*(start,goal,h_score)\n" + 
		"    closedSet := the empty set \n" + 
		"    openSet := set containing the initial node \n" + 
		"    g_score[start] := 0                        \n" + 
		"    f_score[start] := h_score[start]           \n" + 
		"    while openSet is not empty\n" + 
		"        x := the node in openSet having the lowest f_score[] value\n" + 
		"        if x = goal\n" + 
		"            return reconstruct_path(came_from[goal])\n" + 
		"        remove x from openSet\n" + 
		"        add x to closedSet\n" + 
		"        foreach y in neighbor_nodes(x)\n" + 
		"            if y in closedSet\n" + 
		"                continue\n" + 
		"            tentative_g_score := g_score[x] + dist_between(x,y)\n" + 
		"\n" + 
		"            if y not in openSet\n" + 
		"                add y to openSet\n" + 
		"                tentative_is_better := true\n" + 
		"            else\n" + 
		"                tentative_is_better := tentative_g_score < g_score[y]\n" + 
		"            if tentative_is_better = true\n" + 
		"                came_from[y] := x\n" + 
		"                g_score[y] := tentative_g_score\n" + 
		"                f_score[y] := g_score[y] + h_score[y]\n" + 
		"    return failure\n" + 
		"\n" + 
		"function reconstruct_path(current_node) @openContext\n" + 
		"    if came_from[current_node] is set\n" + 
		"        p = reconstruct_path(came_from[current_node])\n" + 
		"        return (p + current_node)\n @closeContext" + 
		"    else\n" + 
		"        return current_node\n @closeContext"; 
		
	}
	
}
