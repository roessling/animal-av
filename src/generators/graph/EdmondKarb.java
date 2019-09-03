/*
 * EdmondsKarb.java
 * Dirk Kleiner, Philipp Sch�nig, 2016 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.graph;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;
import java.util.Queue;

import algoanim.primitives.Graph;
import algoanim.primitives.IntArray;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.ArrayDisplayOptions;
import algoanim.util.Coordinates;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class EdmondKarb implements Generator {
    private Language lang;
    private ArrayProperties arrayprop;
    private RectProperties rectprop;
    private Graph StartGraph;
    
    
    static DisplayOptions display;
	static ArrayDisplayOptions arraydisplay;
	
	static GraphProperties graphprop;
	static TextProperties textprops;
	static TextProperties propsBeschreibung;
	static TextProperties propsFlow;

	static SourceCodeProperties descprop;
	static SourceCodeProperties pseudoprop;

	
	static Rect titelrect;
	static Text titel;
	static Text currMaxFlowText;
	static Text maxFlowPathText;
	static SourceCode desc;
	static SourceCode pseudo;
	
	static IntArray currMaxFlow;
	static IntArray maxFlowPath;
	
	
	static int[][] adjacencMatrix;
	static Node[] graphNodes;
	static String[] labels;

	
	public final static Timing defaultDuration = new TicksTiming(30);


	private static final String DESCRIPTION = "Der Edmonds-Karp-Algorithmus ist in der Informatik und der Graphentheorie eine Implementierung der Ford-Fulkerson-Methode zur Berechnung des maximalen s-t-Flusses in Netzwerken mit positiven reellen Kapazitäten";

	private static final String SOURCE_CODE = "algorithm EdmondsKarp\r\n" 
			+ "    input:\r\n"
			+ "        C[1..n, 1..n] (Capacity matrix)\r\n" 
			+ "        E[1..n, 1..?] (Neighbour lists)\r\n"
			+ "        s             (Source)\r\n" 
			+ "        t             (Sink)\r\n" 
			+ "    output:\r\n"
			+ "        f             (Value of maximum flow)\r\n"
			+ "        F             (A matrix giving a legal flow with the maximum value)\r\n"
			+ "    f := 0 (Initial flow is zero)\r\n"
			+ "    F := array(1..n, 1..n) (Residual capacity from u to v is C[u,v] - F[u,v])\r\n" 
			+ "    forever\r\n"
			+ "        m, P := BreadthFirstSearch(C, E, s, t, F)\r\n" 
			+ "        if m = 0\r\n" 
			+ "            break\r\n"
			+ "        f := f + m\r\n" 
			+ "        (Backtrack search, and write flow)\r\n" 
			+ "        v := t\r\n"
			+ "        while v ≠ s\r\n" 
			+ "            u := P[v]\r\n" 
			+ "            F[u,v] := F[u,v] + m\r\n"
			+ "            F[v,u] := F[v,u] - m\r\n" 
			+ "            v := u\r\n" 
			+ "    return (f, F)"; // 22
	
	private final String PSEUDOCODE = "while there is an augmenting path \n"
			+ "find an augmenting path using BFS \n"
			+ "\t for each edge u->v in the path \n"
			+ "\t"
			+ "\t decrease capacity u->v by bottleneck \n"
			+ "\t"
			+ "\t increase capacity v->u by bottlenek \n"
			+ "\t"
			+ "\t increase maxflow by bottleneck";
	
	
    public void init(){
        lang = new AnimalScript("Edmonds Karb", "Dirk Kleiner, Philipp Schönig", 800, 600);
        lang.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        arrayprop = (ArrayProperties)props.getPropertiesByName("arrayprop");
        rectprop = (RectProperties)props.getPropertiesByName("rectprop");
        StartGraph = (Graph)primitives.get("StartGraph");
        
        init();
        
        initializeParameter();
        
        Graph g = getGraph(lang, StartGraph);
        g.hide();
        
        showdescription(g, lang);
        
        maxflow(g, 0, 6);
        
        return lang.toString();
    }
    
    /**
	 * 
	 * @param g:
	 *            Graph
	 * @param fromNode:
	 *            Startknoten
	 * @param toNode:
	 *            Endknoten
	 * 
	 *            Berechnung des Max-Flows mithilfe des Endmonds-Karb
	 *            Algorithmus
	 */
	public void maxflow(Graph g, int fromNode, int toNode) {
		
		
		arraydisplay = new ArrayDisplayOptions(Timing.FAST,Timing.SLOW, true);
		arrayprop = new ArrayProperties();
		propsFlow = new TextProperties();
		
		// System.out.println("Arrayprop: "+arrayprop.getAllPropertyNames().toString());
//		arrayprop.set("fillColor", Color.WHITE);
//		arrayprop.set("filled", false);
//		arrayprop.set("color", Color.BLUE);
//		//arrayprop.set("elemHighlight", Color.red);
//		arrayprop.set("cellHighlight", Color.red);
		
		arrayprop.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		arrayprop.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
		arrayprop.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.RED);


		
		propsFlow.set("font", new Font(Font.SERIF, Font.PLAIN, 15));

	

		currMaxFlowText = lang.newText(new Coordinates(350,400), "Max Flow:", "MaxFlowPfadText", display,propsFlow);
		maxFlowPathText = lang.newText(new Coordinates(350,420), "MaxFlow Pfad:", "MaxFlowText", display,propsFlow);
		
		int[] currmaxflow = new int[1];
		currmaxflow[0] = 0;
		currMaxFlow = lang.newIntArray(new Coordinates(450,400), currmaxflow, "MaxFlow", arraydisplay, arrayprop);
		
		int[] maxPathflow = new int[1];
		maxPathflow[0] = 0;
		maxFlowPath = lang.newIntArray(new Coordinates(450,420), maxPathflow, "MaxFlowPath", arraydisplay, arrayprop);
		
		
//		currMaxFlow.highlightCell(0, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
//		currMaxFlow.unhighlightCell(0, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
//		
//		maxFlowPath.highlightCell(0, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
//		maxFlowPath.unhighlightCell(0, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

		
//		currMaxFlow.show();
//		maxFlowPath.show();
		Graph currGraph = g;
		
		g.show();

		int[][] flowMatrix = new int[g.getAdjacencyMatrix().length][g.getAdjacencyMatrix()[0].length];

		
		while (bfs(currGraph, fromNode, toNode).size() > 0) {
			
			lang.nextStep();
			pseudo.unhighlight(3);
			pseudo.unhighlight(4);
			pseudo.toggleHighlight(5, 0);
			lang.nextStep();
			pseudo.toggleHighlight(0, 1);
			lang.nextStep();
			
			
			ArrayList<Integer> path = bfs(currGraph, fromNode, toNode);
		
			
			
			int minEdgeWight = -1;
			int currEdgeWight = 0;
			// Finde Minimalen Fluss
			for (int i = path.size() - 1; i > 0; i--) {

				currEdgeWight = currGraph.getEdgeWeight(path.get(i), path.get(i - 1));

				if (minEdgeWight > currEdgeWight || minEdgeWight == -1) {
					minEdgeWight = currEdgeWight;
				}
				g.highlightNode(path.get(i), Timing.INSTANTEOUS, Timing.VERY_SLOW);
				g.highlightEdge(path.get(i), path.get(i-1), Timing.INSTANTEOUS, Timing.VERY_SLOW);
				g.highlightNode(path.get(i-1), Timing.INSTANTEOUS, Timing.VERY_SLOW);
				lang.nextStep();

			}
		
			
			pseudo.toggleHighlight(1, 2);
			lang.nextStep();
			//maxPathflow[0] = minEdgeWight;
			
			maxFlowPath.put(0, minEdgeWight, Timing.FAST, Timing.FAST);

			lang.nextStep();
			//maxFlowPath = lang.newIntArray(new Coordinates(450,420), maxPathflow, "MaxFlowPath", arraydisplay, arrayprop);
			
			maxFlowPath.highlightCell(0, Timing.FAST, Timing.FAST);
			lang.nextStep();
			maxFlowPath.unhighlightCell(0, Timing.FAST, Timing.FAST);
//			lang.nextStep();

			//lang.nextStep();
			// Erstelle neue Adjazenzmatrix
			int[][] newAdjacencMatrix = currGraph.getAdjacencyMatrix();
			// Update die FlowMatrix und die neue Adjazenzmatrix
			
			ArrayList<int[]> increasePath = new ArrayList<>();
			ArrayList<int[]> decreasePath = new ArrayList<>();
			for (int i = path.size() - 1; i > 0; i--) { /// >1?

				// // System.out.println("Matrixinsert: "+path.get(i)+ " :
				// "+path.get(i-1)+" : "+minEdgeWight);
				flowMatrix[path.get(i)][path.get(i - 1)] = flowMatrix[path.get(i)][path.get(i - 1)] + minEdgeWight;
				newAdjacencMatrix[path.get(i)][path.get(i - 1)] = newAdjacencMatrix[path.get(i)][path.get(i - 1)]
						- minEdgeWight;
		
		
				int[] dec = new int[2];
				dec[0] = path.get(i);
				dec[1] = path.get(i-1);
				
				
				decreasePath.add(dec);
			}
			
			lang.nextStep();
			pseudo.unhighlight(2);
			pseudoprop.set("highlightColor", Color.CYAN);
			pseudo = lang.newSourceCode(new Coordinates(50,370), "Pseudocode", display, pseudoprop);
			pseudo.addMultilineCode(PSEUDOCODE, "Pseudocode", Timing.INSTANTEOUS);
			pseudo.highlight(3);
			
			g.hide();
			lang.nextStep();
			graphprop.set("elemHighlight", Color.CYAN);
			graphprop.set("highlightColor", Color.CYAN);
			g = lang.newGraph("StartGraph", newAdjacencMatrix,graphNodes , labels, display, graphprop);
			
			//lang.nextStep();
			
	
			for(int i = 0; i < decreasePath.size(); i++){
				g.highlightNode(decreasePath.get(i)[0], Timing.INSTANTEOUS, Timing.VERY_SLOW);
				g.highlightNode(decreasePath.get(i)[1], Timing.INSTANTEOUS, Timing.VERY_SLOW);
				g.highlightEdge(decreasePath.get(i)[0], decreasePath.get(i)[1], Timing.INSTANTEOUS, Timing.VERY_SLOW);
			}
			

			lang.nextStep();
		
			
			for (int i = path.size() - 1; i > 0; i--) { /// >1?

				// // System.out.println("Matrixinsert: "+path.get(i)+ " :
				// "+path.get(i-1)+" : "+minEdgeWight);
				flowMatrix[path.get(i)][path.get(i - 1)] = flowMatrix[path.get(i)][path.get(i - 1)] + minEdgeWight;
			
				// augmented path
				newAdjacencMatrix[path.get(i - 1)][path.get(i)] = newAdjacencMatrix[path.get(i - 1)][path.get(i)]
						+ minEdgeWight;
				int[] inc = new int[2];
				inc[0] = path.get(i-1);
				inc[1] = path.get(i);
		
				
				
				increasePath.add(inc);
			}
			
			pseudoprop.set("highlightColor", Color.MAGENTA);
			pseudo = lang.newSourceCode(new Coordinates(50,370), "Pseudocode", display, pseudoprop);
			pseudo.addMultilineCode(PSEUDOCODE, "Pseudocode", Timing.INSTANTEOUS);
			pseudo.highlight(4);
			lang.nextStep();
			
			g.hide();
			lang.nextStep();
			graphprop.set("elemHighlight", Color.MAGENTA);
			graphprop.set("highlightColor", Color.MAGENTA);
			g = lang.newGraph("StartGraph", newAdjacencMatrix,graphNodes , labels, display, graphprop);
			
			//lang.nextStep();
			for(int i = 0; i < increasePath.size(); i++){
				g.highlightNode(increasePath.get(i)[0], Timing.INSTANTEOUS, Timing.VERY_SLOW);
				g.highlightNode(increasePath.get(i)[1], Timing.INSTANTEOUS, Timing.VERY_SLOW);
				g.highlightEdge(increasePath.get(i)[0], increasePath.get(i)[1], Timing.INSTANTEOUS, Timing.VERY_SLOW);
			}
			lang.nextStep();
			
			

			g.hide();
			lang.nextStep();
			graphprop.set("elemHighlight", Color.RED);
			graphprop.set("highlightColor", Color.RED);
			g = lang.newGraph("StartGraph", newAdjacencMatrix,graphNodes , labels, display, graphprop);
			
			pseudoprop.set("highlightColor", Color.RED);
			pseudo = lang.newSourceCode(new Coordinates(50,370), "Pseudocode", display, pseudoprop);
			pseudo.addMultilineCode(PSEUDOCODE, "Pseudocode", Timing.INSTANTEOUS);
			
			pseudo.unhighlight(4);
			
			pseudo.highlight(5);
			currMaxFlow.put(0, currMaxFlow.getData(0)+minEdgeWight, Timing.FAST, Timing.FAST);

			lang.nextStep();
			//maxFlowPath = lang.newIntArray(new Coordinates(450,420), maxPathflow, "MaxFlowPath", arraydisplay, arrayprop);
			
			currMaxFlow.highlightCell(0, Timing.FAST, Timing.FAST);
			lang.nextStep();
			currMaxFlow.unhighlightCell(0, Timing.FAST, Timing.FAST);

		
		}
		pseudo.unhighlight(5);

	}

	/**
	 * 
	 * @param g:
	 *            Graph
	 * @param fromNode:
	 *            Startknoten
	 * @param toNode:
	 *            Endknoten
	 * @return Liste mit besuchten Knoten
	 */
	public ArrayList<Integer> bfs(Graph g, int fromNode, int toNode) {

		int[][] adjMatrix = g.getAdjacencyMatrix();

		Queue<Integer> queue = new LinkedList<>();
		ArrayList<Integer> besucht = new ArrayList<>();

		queue.add(fromNode);

		while (queue.size() > 0) {

			int currNode = queue.poll();

			besucht.add(currNode);
			if (currNode == toNode) {
				return getPfad(g, besucht, fromNode, toNode);
			}

			for (int i = 0; i < adjMatrix[currNode].length; i++) {
				if (adjMatrix[currNode][i] > 0 && !besucht.contains(i) && !queue.contains(i)) {
					queue.add(i);
				}
			}

		}

		return getPfad(g, besucht, fromNode, toNode);

	}

	/**
	 * 
	 * @param g:
	 *            Graph
	 * @param besucht:
	 *            Besuchte Knoten von der Breitensuche
	 * @param fromNode:
	 *            Startknoten
	 * @param toNode:
	 *            Endknoten
	 * @return Pfad von Endknoten zu Startknoten
	 */
	private ArrayList<Integer> getPfad(Graph g, ArrayList<Integer> besucht, int fromNode, int toNode) {

		if (!besucht.contains(fromNode) || !besucht.contains(toNode)) {
			//// System.out.println("Kein Pfad gefunden" + besucht.toString());
			return new ArrayList<Integer>();
		}

		ArrayList<Integer> pfad = new ArrayList<>();

		int currNode = toNode;
		while (currNode != fromNode) {

			pfad.add(currNode);
			for (int i = 0; i < besucht.size(); i++) {

				int[] currEdges = g.getEdgesForNode(besucht.get(i));

				if (currEdges[currNode] > 0) {
					currNode = besucht.get(i);
					break;
				}

			}
		}

		pfad.add(currNode);

		return pfad;
	}


	private void showdescription(Graph g, Language lang) {

    	String beschreibung = "Edmonds-Karb Algorithmus ist eine implementierung der Ford-Fulkerson-Methode \n"
				+ "zur Berechnung des maximalen s-t-Flusses in Netzwerken mit positiven reelen Kapazitäten. \n"
				+ "Der Algorithmus sucht in jedem Schritt den kürzesten, augmentierenden Pfad, wodurch eine polynomielle Laufzeit sichergestellt wird.\n"
				+ "Meisten (wie auch in diesem Beispiel) wird der kürzeste Pfad durch eine Breitensuhe ermittelt.\n"
				+ "Der Algorithmus wurde zuerst 1970 vom russischen Wissenschaftler Yefim Dinitz publiziert \n"
				+ "und später unabhängig von Jack Edmonds und Richard Karb, die ihn 1972 publizierten, entdeckt.\n"
				+ "Der Unterschied liegt bei Dinics Algorithmus in zusätzlichen Tehniken zur Reduzierung der Laufzeit\n";
		
		String pseudocode = "while there is an augmenting path \n"
				+ "find an augmenting path using BFS \n"
				+ "\t for each edge u->v in the path \n"
				+ "\t"
				+ "\t decrease capacity u->v by bottleneck \n"
				+ "\t"
				+ "\t increase capacity v->u by bottlenek \n"
				+ "\t"
				+ "\t increase maxflow by bottleneck";
		
		titelrect = lang.newRect(new Coordinates(50, 50), new Coordinates(350, 120), "Titel_Rechteck", display, rectprop);
		lang.addItem(titelrect);
		
		titel = lang.newText(new Coordinates(55, 75), "Edmonds-Karb Algorithmus", "Titel", display, textprops);
	
		desc = lang.newSourceCode(new Coordinates(50, 150), "Beschreibung", display, descprop);
		desc.addMultilineCode(beschreibung, "Beschreibungslabel", Timing.INSTANTEOUS);
		
		
		pseudo = lang.newSourceCode(new Coordinates(50,370), "Pseudocode", display, pseudoprop);
		pseudo.addMultilineCode(pseudocode, "Pseudocode", Timing.INSTANTEOUS);
		
//		ArrayDisplayOptions maxflowoptions = new ArrayDisplayOptions(Timing.INSTANTEOUS, Timing.FAST, true);
//		ArrayProperties maxflowprops = new ArrayProperties("MaxflowProps");
//		int[] a = new int [1];
//		a[0] = 3;
//		IntArray b = lang.newIntArray(new Coordinates(100,100),a,"MaxFlow", maxflowoptions, maxflowprops);
		
//		// System.out.println("Arrayprops: "+maxflowprops.getAllPropertyNames().toString());
//		lang.addItem(b);
		
		lang.nextStep();
		desc.hide();
		lang.nextStep();
	}

	private Graph getGraph(Language lang, Graph g) {
	
    	adjacencMatrix = g.getAdjacencyMatrix();
    	
    	ArrayList<Node> tmpNodes = new ArrayList<>();
        ArrayList<String> tmpLabes = new ArrayList<>();
        for( int i = 0; i < g.getAdjacencyMatrix().length; i++){
        	tmpNodes.add(g.getNode(i));
        	tmpLabes.add(g.getNodeLabel(i));
        }
        
        graphNodes = new Node[tmpNodes.size()];
        tmpNodes.toArray(graphNodes);
        
        labels =new String[tmpLabes.size()];
        tmpLabes.toArray(labels);



	return lang.newGraph("StartGraph", adjacencMatrix, graphNodes, labels, display, graphprop);
    	
	}

	private static void initializeParameter() {

		display = new DisplayOptions() {
		};

	
		graphprop = new GraphProperties();
		textprops = new TextProperties();
		propsBeschreibung = new TextProperties();
		//rectprop = new RectProperties();
		descprop = new SourceCodeProperties();
		pseudoprop = new SourceCodeProperties();
		
		//rectprop.set("fillColor", Color.lightGray);
		//rectprop.set("filled", true);
		//rectprop.set("depth", 2);

		textprops.set("font", new Font(Font.SERIF, Font.PLAIN, 25));

		descprop.set("font", new Font(Font.SERIF, Font.PLAIN, 15));
		pseudoprop.set("font", new Font(Font.SERIF, Font.PLAIN, 15));
		pseudoprop.set("highlightColor", Color.RED);
			
		graphprop.set("directed", true);
		graphprop.set("filled", false);
		graphprop.set("fillColor", Color.white);
		graphprop.set("weighted", true);
		graphprop.set("elemHighlight", Color.red);
		graphprop.set("highlightColor", Color.red);
		

		// Output ParameterNames
		// System.out.println(graphprop.getAllPropertyNames());
		// System.out.println("Textprops: " + propsBeschreibung.getAllPropertyNames());
		// System.out.println("Rectprops: " + rectprop.getAllPropertyNames());
		// System.out.println("Sourcecodeprops: " + descprop.getAllPropertyNames());

	}

    public String getName() {
        return "Edmonds Karb";
    }

    public String getAlgorithmName() {
        return "Edmonds Karb Algorithmus";
    }

    public String getAnimationAuthor() {
        return "Dirk Kleiner, Philipp Schönig";
    }

    public String getDescription(){
        return DESCRIPTION;
    }

    public String getCodeExample(){
        return SOURCE_CODE;
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
        return Generator.JAVA_OUTPUT;
    }

}