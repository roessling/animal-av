/*
 * EulerianCycleCodeGenerator.java
 * Markus Lehamnn, Martin M�ller, 2016 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.graph.euleriancyclecode;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

import algoanim.primitives.generators.Language;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.ArcProperties;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Locale;
import java.util.Hashtable;

import algoanim.primitives.Graph;
import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.AnimationType;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.primitives.Variables;
import algoanim.util.Coordinates;
import algoanim.util.Node;

public class EulerianCycleCodeGenerator implements ValidatingGenerator {
    
	private Language lang;
	private Graph graph;
	private Graph bggraph;
	private SourceCode sc;
	
	//int [][] adjMatrix= new int[nodes][nodes];
	private int[][] startMatrix/*= {
    		{0,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    		{1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    		{0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    		{0,0,0,0,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    		{0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    		{1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    		{0,0,0,0,0,0,0,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0},
    		{0,0,1,0,0,0,1,0,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0},
    		{0,0,0,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    		{0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0},
    		{0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0},
    		{0,0,0,0,0,0,1,0,0,0,1,0,1,0,0,0,0,1,0,0,0,0,0,0,0},
    		{0,0,0,0,0,0,0,1,0,0,0,1,0,1,0,0,0,1,0,0,0,0,0,0,0},
    		{0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,0,0,0,0},
    		{0,0,0,0,0,0,0,0,0,1,0,0,0,1,0,0,0,0,1,1,0,0,0,0,0},
    		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,1,0,0,0,0},
    		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,0},
    		{0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,1,0,1,0,0,0,0,0,0},
    		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,1,0,0,0,0,0,0,0},
    		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1},
    		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0},
    		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,0,0},
    		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,0},
    		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1},
    		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,1,0}}*/;
	private  int[][] adjMatrix;
	String[] labels;
	private SourceCodeProperties scProps;
	private int sNode;
	private Node southAnchor=new Coordinates(20, 390);
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	private int recursion = 1;
	private int nodes;
	private Variables vars;

	
    public void init(){
        lang = new AnimalScript("Classical eulerian cycle algorithm", "Markus Lehmann, Martin M\u00fcller", 800, 600);
		lang.setStepMode(true);
	}

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {

		sNode = (Integer) primitives.get("startingNode");
		
		startMatrix = (int[][])primitives.get("intMatrix");
		adjMatrix = new int[startMatrix.length][];
		nodes = startMatrix.length;
		for (int i = 0; i < startMatrix.length; i++){
			adjMatrix[i] = startMatrix[i].clone();
		}
		
		scProps =(SourceCodeProperties)props.getPropertiesByName("sourceCode");
		
		if (validateInput(props, primitives)){
			//EULER CONSTRUCTOR
			vars = lang.newVariables(); // Anlegen des Variablen-Containers
			vars.declare("String", "path"); 
			initAlgo();
			LinkedList<Integer> path=runEulerAlg(sNode);
			if (path!=null){
				for(int i=0;i<path.size()-1;i++){
					bggraph.setEdgeWeight(path.get(i), path.get(i+1), i, null, null);
					bggraph.setEdgeWeight(path.get(i+1), path.get(i), i, null, null);
					bggraph.unhighlightEdge(path.get(i), path.get(i+1), null, null);
				}
				sc.hide();
				lang.newText(southAnchor, "The algorithm is finished now. The found eulerian cycle has length "+path.size()+" and is shown using weight attributes, starting at the edge with weight 0.", "endText", null, new TextProperties());
			}
			else {
				sc.hide();
				lang.newText(southAnchor, "No eulerian cycle was found! There is no free edge to exit node x.", "endText", null, new TextProperties());
				lang.nextStep("Ending "+recursion+". recursive call");
			}
			//EULER CONSTRUCTOR END
		}
		return lang.toString();
    }
	
	private void initAlgo(){
		/*
		 * Caption
		 */
		TextProperties cp=new TextProperties();
		cp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font( Font.SANS_SERIF, Font.PLAIN, 20 ));
		lang.newText(new Coordinates(10,10), "Classical eulerian cycle algorithm", "caption", null, cp);
		
		/*
		 * Introduction text
		 */
		SourceCodeProperties itp=new SourceCodeProperties();
		SourceCode introText= lang.newSourceCode(new Coordinates(10,30),"introText", null, itp);
		introText.addCodeLine("In graph theory, an Eulerian trail (or Eulerian path) is a trail in a graph which visits every edge exactly once.", null, 0, null);
		introText.addCodeLine("Similarly, an Eulerian circuit or Eulerian cycle is an Eulerian trail which starts and ends on the same vertex.", null, 0, null);
		introText.addCodeLine("They were first discussed by Leonhard Euler while solving the famous Seven Bridges of K\u00f6nigsberg problem in 1736. (see: https://en.wikipedia.org/wiki/Eulerian_path)", null, 0, null);
		introText.addCodeLine("Hereafter, the classical eulerian cycle algorithm will be presented which returns an eulerian cycle for solvable graphs.", null, 0, null);
		
		lang.nextStep();
		introText.hide();
		
		/*
		 * Graph
		 */
		labels= new String[nodes];
		Node[] nodeList= new Node[nodes];

		int columns=(int) Math.round(Math.sqrt(nodes));
		int rows=nodes/columns;
		if(rows*columns<nodes){
			rows++;
		}
	    
		for (int i=0;i<nodes;i++){
			nodeList[i]= new Coordinates(20+100*(i/rows),30+75*(i%rows));
			labels[i]=String.format("%02d", i);
			//labels[i]="";	
		}
		
		GraphProperties graphProps=new GraphProperties();
		graphProps.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, false);
		graphProps.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, false);
		graphProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);//Highlightfarbe Knoten, Highlightfarbe Kante
		graphProps.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY, Color.BLACK);//Textfarbe des Knoten
		graphProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);//Hintergrundfarbe des Knoten
		graph=lang.newGraph("graph", adjMatrix, nodeList, labels, null, graphProps);
		
		GraphProperties bggraphProps = new GraphProperties();
		bggraphProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, new Color(192,192,192));
		bggraphProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);//Bug: bei Depth=2 wird der bgGraph vor graph(Depth=1) gezeichnet
		bggraphProps.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, true);

		
		bggraph=lang.newGraph("background-graph", adjMatrix, nodeList, labels, null, bggraphProps);
		for (int i=0;i<nodes;i++){
			for (int j=0;j<nodes;j++){
				bggraph.highlightEdge(i, j, null, null);
			}
		}
		
		/*
		 * Code
		 */
		sc = lang.newSourceCode(southAnchor, "sourceCode", null, scProps);

		sc.addCodeLine("Let p be a (dynamically growing) path, represented in p as an alternating sequence of nodes and edges.", null, 0, null); // 0
		sc.addCodeLine("Initialize p so as to contain s and nothing else.", null, 0, null);//1
		sc.addCodeLine("Set x:=s. (x is highlighted as red circle)", null, 0, null);//2
		sc.addCodeLine("", null, 0, null); // 3
		sc.addCodeLine("While there are edges leaving x:", null, 0, null); // 4
		sc.addCodeLine("Choose one such edge (x,y).", null, 1, null); // 5
		sc.addCodeLine("Remove (x,y) from the graph.", null, 1, null); // 6
		sc.addCodeLine("Append (x,y) and then y to p.", null, 1, null); // 7
		sc.addCodeLine("Set x:=y.", null, 1, null); // 8
		sc.addCodeLine("If x != s, terminate the algorithm with the statement that no eulerian cycle exists.", null, 0, null); // 9
		sc.addCodeLine("Otherwise: For each node v on p that still has leaving edges:", null, 0, null); // 10
		sc.addCodeLine("Call the procedure recursively with v as the start node, giving path p'.", null, 1, null); // 11
		sc.addCodeLine("Replace v in p by p'.", null, 1, null); // 12
	}
	
	private LinkedList<Integer> runEulerAlg(int sNode){
		sc.highlight(0);
		sc.highlight(1);
		sc.highlight(2);
		LinkedList<Integer> path=new LinkedList<>();
		List<Integer> unfinishedNodes=new LinkedList<Integer>();
		path.add(sNode);
		showPath(path);
		int xNode=sNode;
		
		graph.highlightNode(xNode, null, null);
		lang.nextStep("Starting "+recursion+". recursive call");
		int[][] adjMatrix = graph.getAdjacencyMatrix();
		
		sc.unhighlight(0);
		sc.unhighlight(1);
		sc.unhighlight(2);
		sc.highlight(4);
		lang.nextStep();
		
		while (Arrays.stream(adjMatrix[xNode]).sum()>0){//While there are edges leaving x
			if (Arrays.stream(adjMatrix[xNode]).sum()>1 && xNode!=sNode){
				unfinishedNodes.add(xNode);
			}
			int yNode;
			for (yNode=0;adjMatrix[xNode][yNode]==0;yNode++){};//Choose one such edge (x,y) .
			graph.highlightEdge(xNode, yNode, null, null);
			sc.unhighlight(4);
			sc.highlight(5);
			lang.nextStep();
			
			adjMatrix[xNode][yNode]=0;
			adjMatrix[yNode][xNode]=0;
			path.add(yNode);
			showPath(path);
			graph.highlightNode(yNode, null, null);
			graph.unhighlightNode(xNode, null, null);
			xNode=yNode;
			sc.unhighlight(5);
			sc.highlight(6);
			sc.highlight(7);
			sc.highlight(8);
			lang.nextStep();
			
			sc.unhighlight(6);
			sc.unhighlight(7);
			sc.unhighlight(8);
			sc.highlight(4);
			lang.nextStep();
		}
		
		sc.unhighlight(4);
		sc.unhighlight(6);
		sc.unhighlight(7);
		sc.unhighlight(8);
		sc.highlight(9);
		if (xNode!=sNode){
			return null;
		}
		lang.nextStep();
		graph.unhighlightNode(xNode, null, null);
		labels[xNode]=String.format("%02d", xNode);
		//gefundenen Zyklus entfernen
		for (int i=0;i<path.size()-1;i++){
			graph.unhighlightEdge(path.get(i),path.get(i+1), null, null);
			graph.hideEdge(path.get(i),path.get(i+1), null, null);
			graph.hideEdge(path.get(i+1),path.get(i), null, null);
		}
		sc.toggleHighlight(9,10);
		unfinishedNodes= unfinishedNodes.stream().filter(n-> (Arrays.stream(adjMatrix[n/*-1*/]).sum()>0)).collect(Collectors.toList());//�berschuss kann zwischenzeitlich aufgel�st worden sein
		for (int uNode : unfinishedNodes){
			if(Arrays.stream(adjMatrix[uNode]).sum()>0){//�berschuss kann zwischenzeitlich aufgel�st worden sein
				graph.highlightNode(uNode, null, null);
				//graph.setName(String.format("%02d", uNode)+" (x)");
				sc.toggleHighlight(9,10);
				lang.nextStep();
				sc.toggleHighlight(10,11);
				lang.nextStep();
				sc.highlight(11,0,true);
				recursion++;
				LinkedList<Integer> addPath = runEulerAlg(path.get(path.indexOf(uNode)));
				recursion--;
				if (addPath==null){
					lang.nextStep("Ending "+recursion+". recursive call");
					return null;
				}
				path.addAll(path.indexOf(uNode)+1,addPath);
				showPath(path);
				sc.unhighlight(11);
				sc.highlight(12);
				path.remove(path.indexOf(uNode));
			}
		}
		if (unfinishedNodes.isEmpty()){
			lang.nextStep();
			sc.unhighlight(10);
		}
		lang.nextStep("Ending "+recursion+". recursive call");
		sc.unhighlight(12);
		return path;
	}
	
	
    public String getName() {
        return "Classical eulerian cycle algorithm";
    }

    public String getAlgorithmName() {
        return "Classical eulerian cycle algorithm";
    }

    public String getAnimationAuthor() {
        return "Markus Lehmann, Martin M\u00fcller";
    }

    public String getDescription(){
        return "If possible generate an eulerian cycle for a given graph.";
    }

    public String getCodeExample(){
        return "Choose a start node s."
 +"\n"
 +"	Let p be a (dynamically growing) path, represented in p as an alternating sequence of nodes and edges/arcs."
 +"\n"
 +"	Initialize p so as to contain s and nothing else."
 +"\n"
 +"	Set x:=s."
 +"\n"
 +"	While there are edges/arcs leaving x:"
 +"\n"
 +"		Choose one such edge (x,y) ."
 +"\n"
 +"		Remove (x,y) from the graph."
 +"\n"
 +"		Append (x,y) and then y to p."
 +"\n"
 +"		Set x:=y."
 +"\n"
 +"	If x != s, terminate the algorithm with the statement that no eulerian cycle exists."
 +"\n"
 +"	Otherwise: For each node v on p that still has leaving edges:"
 +"\n"
 +"		Call the procedure recursively with v as the start node, giving path p'."
 +"\n"
 +"		Replace v in p by p'."
 +"\n"
 +"\n"
 +"Source: http://wiki.algo.informatik.tu-darmstadt.de/Classical_eulerian_cycle_algorithm#Recursive_step"
 +"\n";
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
        return Generator.JAVA_OUTPUT;
    }
	
	public boolean validateInput(AnimationPropertiesContainer arg0,
			Hashtable<String, Object> arg1) throws IllegalArgumentException {
		sNode = (Integer) arg1.get("startingNode");
		
		int[][] adjMatrix = (int[][])arg1.get("intMatrix");
		nodes = adjMatrix.length;
		if (nodes != adjMatrix[0].length){
			throw new IllegalArgumentException("Adjacency matrix must be a square.");
		}
		for (int i=0;i<nodes;i++){
			for (int j=0;j<i;j++){
				if (adjMatrix[i][j]!=adjMatrix[j][i]){
					throw new IllegalArgumentException("This is an undirected graph. Adjacency matrix must equal its transpose.");
				}
			}
		}
		if (0>sNode || sNode>=nodes){
			throw new IllegalArgumentException("starting Node must be =>0 and <"+nodes);
		}
		return true;
	}
	
	private void showPath(LinkedList<Integer> p){
    	StringBuilder sb = new StringBuilder();
    	for (int i=0;i<p.size()-1;i++){
    		sb.append(p.get(i)).append("-");
    	}
    	sb.append(p.get(p.size()-1));
    	vars.set("path", sb.toString());
    }

}