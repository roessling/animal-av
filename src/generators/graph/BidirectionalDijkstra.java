/*
 * bidirectionaldijkstra.java
 * Marian Hieke, Tim Beringer, 2016 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.graph;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.*;
import algoanim.util.*;
import algoanim.primitives.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Set;

public class BidirectionalDijkstra implements Generator {
    private Language lang;
    private Text header;
		private TextProperties textprops;
		private SourceCode src;
		private SourceCodeProperties srcProps;
		
		private SourceCode src2;
		
		private SourceCode src3;
		

    public void init(){
        lang = new AnimalScript("Bidirektionaler Diskstra", "Marian Hieke, Tim Beringer", 800, 600);
    }

public BidirectionalDijkstra(Language lang) {
    this.lang = lang;
    // This initializes the step mode. Each pair of subsequent steps has to
    // be divdided by a call of lang.nextStep();
    this.lang.setStepMode(true);
  }

  public BidirectionalDijkstra() {
    this.lang = new AnimalScript("Direrektionlaer Dijkstra", "Marian Hieke, Tim Beringer",
        800, 600);
    lang.setStepMode(true);
  }

public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
	//this.lang.nextStep();
System.out.println("blub2");
//    this.srcProps = (SourceCodeProperties)props.getPropertiesByName("sourceCode"); //<------ error solved in xml
        Graph graph = (Graph) primitives.get("graph");
	//this.lang.setStepMode(false);
System.out.println("blub1");
		
	
		 
		
		 
System.out.print("blub3");		 
	 GraphProperties graphProps = (GraphProperties) props.getPropertiesByName("graph"); 
System.out.println("blub2");

			/* 
			      graph = getDefaultGraph(graphProps);
				System.out.print(graph.getSize());	
 for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < matrix[0].length; j++) {
        System.out.print(matrix[i][j] + "\t");
      }
      System.out.println();
    }	*/	
/*     
			    int size = graph.getSize();
			    Node[] nodes = new Node[size];
			    String[] nodeLabels = new String[size];
			    for (int i = 0; i < size; i++) {
			      nodes[i] = graph.getNode(i);
			      nodeLabels[i] = graph.getNodeLabel(i);
			    
			    graph = lang.newGraph(graph.getName(), graph.getAdjacencyMatrix(),
			        nodes, nodeLabels, graph.getDisplayOptions(), graphProps); */
			    srcProps = new SourceCodeProperties();
			    srcProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font( Font.SANS_SERIF, Font.PLAIN, 12));
			   srcProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.GREEN);
			   
	
			    
			   
				 
			    	
				
			    
			    this.lang.setStepMode(true);
			    //this.lang.nextStep();
			    start(graph, graphProps);
	

System.out.println("blub0");
			    return lang.toString();
    }

    

    public String getName() {
        return "Bidirektionaler Dijkstra";
    }

    public String getAlgorithmName() {
        return "Dijkstra";
    }

    public String getAnimationAuthor() {
        return "Marian Hieke, Tim Beringer";
    }

    public String getDescription(){
String description = "";       
description = description +  "Der Bidirektionale Dijkstra setzt sich aus zwei unidirektional verlaufende Dijkstra-Suchen zusammen. Eine Suche beginnt von Start- zu Zielknoten während die zweite von Ziel- zu Startknoten sucht. \r\n";
description = description +  "In dieser Implementierung alternieren die beiden Suchen in jeder Iteration, was ohne Verlust der Optimalität (Finden des kürzesten Weges) auch durch eine beliebig andere Strategie ersetzt werden kann. Motivation der bidirektionalen Suche ist die Verkürzung der Laufzeit, in dem beide Suchen sich im optimalen Fall bei der Hälfte des Weges treffen, sodass die Anzahl der zu expandierenden Knoten stark verringert wird. Nachdem sich die beiden Suchen treffen, müssen in einer zweiten Phase, der so genannten Postprocessing Phase, weitere Alternativrouten, die potentiell kürzer sind, überprüft werden.";

return description;
    }

    public String getCodeExample(){
	 String description = "// Vorwärts \r\n"
  		    +"Initialisiere für alle Knoten v ds(v) := +oo \r\n"
  		    +"Setze ds(s) := 0;\r\n"
  		   +"Für alle Folgeknoten v von s mit a=(s,v):\r\n" 
  		    
  		    +"ds(v) := l(a)\r\n" 
  		    +"Füge alle Knoten außer s in Qs.\r\n" 
  		    +"Solange Qs nicht leer ist:\r\n"
  		    +"  Entferne nächsten Knoten v aus Qs.\r\n"
  		    +"  Für alle ausgehenden Kanten a=(v,w) v:\r\n"
  		    +"    Wenn w in Qs liegt:\r\n"
  		    +"      Setze ds(w) := min{ ds(w), ds(v) + l(a) }\r\n" 
  		    +"Wenn v nicht in Qg ist:\r\n"
  		    +"Gehe zu Phase 2\r\n" 
  		    +"\r\n"
  			    +"// Rückwärts\r\n"
  			    +"Initialisiere für alle Knoten v ds(v) := +oo\r\n"
  			    +"Setze ds(s) := 0;\r\n"
  			    +"Für alle Folgeknoten v von s mit a=(s,v):\r\n" 
  			     +"ds(v) := l(a)\r\n"

  			    +"Füge alle Knoten außer s in Qs.\r\n" 
  			    +"Solange Qg nicht leer ist:\r\n" 
  			    +"  Entferne nächsten Knoten v aus Qg.\r\n"
  			    +"  Für alle ausgehenden Kanten a=(v,w) v:\r\n"
  			    +"    Wenn w in Qg liegt:\r\n"
  			    +"      Setze ds(w) := min{ ds(w), ds(v) + l(a) }\r\n"
  			    +"Wenn v nicht in Qs ist:\r\n" 
  			    +"Gehe zu Phase 2\r\n"
				+"\r\n"
  				    +"// Phase 2:\r\n"
  				       +"L := oo\r\n"
  				    +"Für alle Kanten (x,y) mit x nicht in Qs und y nicht in Qg:\r\n"
  				    +"  L := min { L, ds(x) + l(x,y) + dg(y) }\r\n"
  				    +"Kürzeste Distanz von Start zu Ziel = L\r\n";        

	return description;
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
public void start(Graph graph, GraphProperties graphProps) {
  		 
	
  		    // show the header with a heading surrounded by a rectangle
  		  TextProperties headerProps = new TextProperties(); 
		  this.textprops = new TextProperties();
		  
		
  		    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
  		        Font.SANS_SERIF, Font.BOLD, 24));
  		    header = lang.newText(new Coordinates(20, 30), "Bidirektionaler Dijkstra","header", null, headerProps);

  	//to work on	    
  	     // startpage
  		//    lang.nextStep();
  		    
  		    textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
  		        Font.SANS_SERIF, Font.PLAIN, 12));
  		    lang.newText(new Coordinates(10, 90),"Der Bidirektionale Dijkstra setzt sich aus zwei unidirektional verlaufende Dijkstra-Suchen zusammen.","descript1", null, textprops);
lang.newText(new Coordinates(10, 120),"Eine Suche beginnt von Start- zu Zielknoten während die zweite von Ziel- zu Startknoten sucht.","descript2", null, textprops);
 lang.newText(new Coordinates(10, 140),"In dieser Implementierung alternieren die beiden Suchen in jeder Iteration,","descript3", null, textprops);
lang.newText(new Coordinates(10, 160),"was ohne Verlust der Optimalität (Finden des kürzesten Weges) auch durch eine beliebig andere Strategie ersetzt werden kann.","descript4", null, textprops);
lang.newText(new Coordinates(10, 180),"Motivation der bidirektionalen Suche ist die Verkürzung der Laufzeit, in dem beide Suchen sich im optimalen Fall bei der Hälfte des Weges treffen,","descript5", null, textprops);
lang.newText(new Coordinates(10, 200),"sodass die Anzahl der zu expandierenden Knoten stark verringert wird.","descript6", null, textprops);
lang.newText(new Coordinates(10, 220),"Nachdem sich die beiden Suchen treffen, müssen in einer zweiten Phase, der so genannten Postprocessing Phase,","descript7", null, textprops);
lang.newText(new Coordinates(10, 240),"weitere Alternativrouten, die potentiell kürzer sind, überprüft werden.","descript8", null, textprops);
  		    

  		    //lang.nextStep();
  		   
  	//to work on 
  		    
  		    
  		    // end description
  		   
  		    // FIXME: this creates a blank page
  		    //lang.hideAllPrimitives();
  		    header.show();
  		    src = lang.newSourceCode(new Coordinates(10,360), "sourceCode",
  		        null, srcProps);
  		    src.addCodeLine("// Vorwärts", null, 0,
  		        null); // 0
  		    src.addCodeLine("Initialisiere für alle Knoten v ds(v) := +oo", null, 1, null);
  		    src.addCodeLine("Setze ds(s) := 0;", null, 1, null);
  		    src.addCodeLine("Für alle Folgeknoten v von s mit a=(s,v):", null, 1, null); 
  		    
  		    src.addCodeLine("ds(v) := l(a)", null, 1, null); 
  		    src.addCodeLine("Füge alle Knoten außer s in Qs.", null, 1, null); 
  		    src.addCodeLine("Solange Qs nicht leer ist:", null, 0, null); 
  		    src.addCodeLine("Entferne Knoten v aus Qs, welcher den niedrigsten Distanzwert hat.", null, 1, null); 
  		    src.addCodeLine("Für alle ausgehenden Kanten a=(v,w) v:", null, 2,null);
  		    src.addCodeLine("Wenn w in Qs liegt:", null, 3, null); 
  		    src.addCodeLine("Setze ds(w) := min{ ds(w), ds(v) + l(a) }", null, 4, null); 
  		    src.addCodeLine("Wenn v nicht in Qg ist:", null, 1, null); 
  		    src.addCodeLine("Gehe zu Phase 2", null, 2, null); 
  		    src.hide();
  		    src2 = lang.newSourceCode(new Coordinates(550,360), "sourceCode",
  			        null, srcProps);
  			    src2.addCodeLine("// Rückwärts", null, 0,
  			        null); // 0
  			    src2.addCodeLine("Initialisiere für alle Knoten v ds(v) := +oo", null, 1, null);
  			    src2.addCodeLine("Setze ds(s) := 0;", null, 1, null);
  			    src2.addCodeLine("Für alle Folgeknoten v von s mit a=(s,v):", null, 1, null); 
  			    
  			    src2.addCodeLine("ds(v) := l(a)", null, 1, null); 
  			    src2.addCodeLine("Füge alle Knoten außer s in Qs.", null, 1, null); 
  			    src2.addCodeLine("Solange Qg nicht leer ist:", null, 0, null); 
  			    src2.addCodeLine("Entferne Knoten v aus Qg, welcher den niedrigsten Distanzwert hat.", null, 1, null); 
  			    src2.addCodeLine("Für alle ausgehenden Kanten a=(v,w) v:", null, 2,null);
  			    src2.addCodeLine("Wenn w in Qg liegt:", null, 3, null); 
  			    src2.addCodeLine("Setze ds(w) := min{ ds(w), ds(v) + l(a) }", null, 4, null); 
  			    src2.addCodeLine("Wenn v nicht in Qs ist:", null, 1, null); 
  			    src2.addCodeLine("Gehe zu Phase 2", null, 2, null); 
			src2.hide();
  			src3 = lang.newSourceCode(new Coordinates(10,360), "sourceCode",null, srcProps);
  				    src3.addCodeLine("// Phase 2:", null, 0,
  				        null); // 0
  				    src3.addCodeLine("L := oo", null, 0, null);
  				    src3.addCodeLine("Für alle Kanten (x,y) mit x nicht in Qs und y nicht in Qg:", null, 0, null);
  				    
  				    src3.addCodeLine("L := min { L, ds(x) + l(x,y) + dg(y) }", null, 1, null); 
  				    
  				    src3.addCodeLine("Kürzeste Distanz von Start zu Ziel = L", null, 0, null); 
  				    
  				src3.hide();    
  			    
  		    lang.nextStep("Einleitung");
  		    

		     String[] data = new String[graph.getSize()];
			    int [] intData = new int[1];
			   
		   	    String[][] distData = new String[3][graph.getSize()];
				System.out.print(graph.getSize());
				 String[][] infoData = new String[2][4];
				 infoData[0][0] = "Start";
				infoData[0][1] = "Ziel";
				infoData[0][2] = "vs";
				infoData[0][3] = "vg";
				infoData[1][0] = "-";
				infoData[1][1] = "-";
				infoData[1][2] = "-";
				infoData[1][3] = "-";	
    
			    for(int i = 0; i < graph.getSize(); i++){
			    	data[i] = "--";
			    }
			    

			    for(int i = 0; i <  distData[0].length; i++){                
			    	distData[0][i] = graph.getNodeLabel(i);     
				System.out.print(graph.getNodeLabel(i));
			    }			    
			    for(int i = 1; i < distData.length; i++){
					 for(int j = 0; j < distData[0].length; j++){
						  distData[i][j] = "-";
						 }
					}
		    ArrayProperties arrayProps = new ArrayProperties();
   		    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
   	            arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    	            arrayProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		    arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.GREEN);
		     
				
				MatrixProperties matrixProps = new MatrixProperties();
   				matrixProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
   				matrixProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    				matrixProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
				matrixProps.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY,"table");
				matrixProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.GREEN);
			    Graph newGraph = lang.newGraph(graph.getName(), graph.getAdjacencyMatrix(),getNodes(graph), getLabels(graph), graph.getDisplayOptions(), graphProps);	
			    newGraph.setStartNode(graph.getStartNode());
    			    newGraph.setTargetNode(graph.getTargetNode());
				
				
				
			   StringMatrix dist = lang.newStringMatrix(new Coordinates(80, 110), distData, "Distanz", null, matrixProps);
  		   	 	StringMatrix info = lang.newStringMatrix(new Coordinates(80, 200), infoData, "Distanz", null, matrixProps);
			
			   biDijk( newGraph, dist,info,  arrayProps, data);
  		  }

  		private void biDijk(Graph graph, StringMatrix dist,StringMatrix info, ArrayProperties arrayProps, String[] data){
  			
			lang.hideAllPrimitives();
  			graph.show();
  		   //Ques noch anzulegen !
  			
  			
			
			info.show();
  			dist.show();
  			src.show();
  			src2.show();
  			src3.hide();
			 Text nameDist = this.lang.newText(new Coordinates(5, 110), dist.getName(), dist.getName(),null,this.textprops);
			nameDist.show();
			StringArray queueS = lang.newStringArray(new Coordinates(80, 50), data, "QueueS", null, arrayProps);
			    StringArray queueG = lang.newStringArray(new Coordinates(80, 80), data, "QueueG", null, arrayProps);
  			
			queueS.showIndices(false, null, null);
			queueG.showIndices(false, null, null);
			 Text nameQueueS = this.lang.newText(new Coordinates(5, 50), queueS.getName(), queueS.getName(),null,this.textprops);
			nameDist.show();
			 Text nameQueueG = this.lang.newText(new Coordinates(5, 80), queueG.getName(), queueG.getName(),null,this.textprops);
			nameDist.show();
			
			int [] intData = new int[1];
			intData[0] = 1;
			IntArray time = lang.newIntArray(new Coordinates(80, 270), intData, "Zeitschritt", null, arrayProps);
		        IntArray min = lang.newIntArray(new Coordinates(80, 300), intData, "L:= ", null, arrayProps);
			Text nameTime = this.lang.newText(new Coordinates(5,270), time.getName(), time.getName(),null,this.textprops);
			Text nameMin = this.lang.newText(new Coordinates(5,300), min.getName(), min.getName(),null,this.textprops);		
			time.showIndices(false, null, null);
			min.showIndices(false, null, null);
			nameDist.show();			
			nameMin.hide();
			min.hide();
			int[] edges = null;
					
			
			for(int i = 0; i<graph.getSize(); i++){
				edges = graph.getEdgesForNode(graph.getNode(i));
				for(int j = 0; j < edges.length; j++ ){
					if(edges[j] > 0){
						graph.showEdgeWeight(graph.getNode(i), graph.getNode(j), null, null);

					}

				}

			}
			
		
		 
			
  		    lang.nextStep("Initialisierung");
       		    src.highlight(0);
  		    time.put(0, (time.getData(0) +1), null, null);
		String startNodeString = graph.getNodeLabel(graph.getStartNode());
		    info.put(1,0,startNodeString,null,null);
  		    lang.nextStep();
  		    src.unhighlight(0);
  		    src.highlight(1);
  		    for(int i = 0; i < dist.getNrCols(); i++){
  		    	dist.put(1, i, "oo", null, null);
  		    	
  		    }
  		    lang.nextStep();
  		    src.unhighlight(1);
  		    src.highlight(2);
  		    dist.put(1, 0, "0", null, null);
  		    graph.highlightNode(graph.getStartNode(), null, null);
  		    
  		    lang.nextStep();
  		    graph.unhighlightNode(graph.getStartNode(), null, null);
  		    src.unhighlight(2);
  		    src2.highlight(0);
		String endNodeString = graph.getNodeLabel(graph.getTargetNode());
		    info.put(1,1,endNodeString,null,null);
  		    lang.nextStep();
  		    src2.unhighlight(0);
  		    src2.highlight(1);
  		    for(int i = 0; i < dist.getNrCols(); i++){
  		    	dist.put(2, i, "oo", null, null);
  		    	
  		    }
  		    lang.nextStep();
  		    src2.unhighlight(1);
  		    src2.highlight(2);
  		    dist.put(2, dist.getNrCols()-1, "0", null, null);
  		    graph.highlightNode(graph.getTargetNode(), null, null);
  		   
  		    lang.nextStep();
  		    src2.unhighlight(2);
  		    time.highlightCell(0, null, null);
  		    time.put(0, 2, null, null);
  		    
  		    lang.nextStep();
  		    time.unhighlightCell(0, null, null);
  		    graph.unhighlightNode(graph.getTargetNode(), null, null);
  		    src.highlight(3);
  		    src.highlight(4);
  		   
  		    int[][] adjacencyMatrix = graph.getAdjacencyMatrix();
  		    Node currentNodeS = graph.getStartNode();
			int[] wayS = new int[adjacencyMatrix.length];
			int[] wayG = new int[adjacencyMatrix.length];
  		   
			for(int i = 0; i< wayS.length;i++ ){
				
					wayS[i] = 0;
					wayG[i] = 0;
			
			}
		 
			int start = graph.getPositionForNode(currentNodeS);
  		     
  		    ArrayList<Integer> children = getChildren(adjacencyMatrix, start);
  		    
  		   //to work !!!
  		    
  		    int cost = 0;
  		    for(int i = 0; i < children.size();i++ ){
			lang.nextStep();
  		    	cost = graph.getEdgeWeight(start, children.get(i));
  		    	dist.put(1, children.get(i), Integer.toString(cost), null, null);
			wayS[children.get(i)] = start;
  		    	graph.highlightNode(children.get(i), null, null);
  		    	dist.highlightCell(1,children.get(i) , null, null);
			lang.nextStep();
			graph.unhighlightNode(children.get(i), null, null);
			dist.unhighlightCell(1,children.get(i) , null, null);
  		    }
  		    
  		    lang.nextStep();
  		    
  		    src.unhighlight(3);
  		    src.unhighlight(4);
  		    src.highlight(5);
  		    //put into que....
  		     
  		     
  		    
  		    for(int i = 1; i < graph.getSize();i++){
			System.out.print(i);
			
  		    	queueS.put(i, graph.getNodeLabel(i), null, null);
  		    }
  		    queueS.put(0, "-", null, null);
  		    int queueSCounter = queueS.getLength() -1; 
  		    
  		    
  		    
  		    src.unhighlight(5);
  		    src2.highlight(3);
  		    src2.highlight(4);
  		    Node currentNodeG = graph.getTargetNode();
  		    int start2 = graph.getPositionForNode(currentNodeG);
  		     
  		    ArrayList<Integer> children2 = getChildren(adjacencyMatrix, start2);
  		    
  		    //to work !!!
  		   
  		    
  		    for(int i = 0; i < children2.size();i++ ){
			lang.nextStep();
  		    	graph.highlightNode(children2.get(i), null, null);
  		    	cost = graph.getEdgeWeight(start2, children2.get(i));
  		    	dist.put(2, children2.get(i), Integer.toString(cost), null, null);
			wayG[children2.get(i)] = start2;  		    	
			graph.highlightNode(children2.get(i), null, null);
  		    	dist.highlightCell(2,children2.get(i) , null, null);
			lang.nextStep();
			graph.unhighlightNode(children2.get(i), null, null);
			dist.unhighlightCell(2,children2.get(i) , null, null);	  		    
 		 }
  		   
  		    lang.nextStep();
  		    src2.unhighlight(3);
  		    src2.unhighlight(4);
  		    src2.highlight(5);
  		    
  		    //put into que.... 
  		     
  		    
  		    for(int i = 0; i < graph.getSize()-1;i++){
  		    	
			queueG.put(i, graph.getNodeLabel(i), null, null);
  		    }
  		    
  		    queueG.put(graph.getSize()-1, "-", null, null);
  		    int queueGCounter = queueG.getLength() -1;
  		    
  		    lang.nextStep();
  		    src2.unhighlight(5);
  		    time.put(0, 3, null, null);
  		    time.highlightCell(0, null, null);
  		    
  		    lang.nextStep("Phase 1 (Wegsuche)");
		    time.unhighlightCell(0, null, null);
  		   
  		    //algo loop phase1
  		    boolean goOn = true;
  		    boolean part1 = true;
  		    boolean part2 = true;
  		    int next1;
  		    int next2;
		String next1Name= "";
		String next2Name= "";
  		    
  		    while(goOn){
  		    	lang.nextStep();
  		    	if(part1){
  		    		src.highlight(6);
  		    		
  		    		lang.nextStep();
  		    		src.unhighlight(6);
  		    		src.highlight(7);
  		    		next1 = getLowest(queueS, dist, 1); 
				dist.highlightCell(1,next1 , null, null);
				next1Name = graph.getNodeLabel(next1);
				info.put(1,2,next1Name, null, null);
  		    		queueS.highlightCell(next1, null, null);
				graph.highlightNode(next1, null, null);
  		    		
  		    		lang.nextStep();
				dist.unhighlightCell(1,next1 , null, null);
  		    		String node = queueS.getData(next1);
  		    		queueS.put(next1, "-", null, null);
  		    		queueSCounter = queueSCounter-1; 
  		    		queueS.unhighlightCell(next1, null, null);
				graph.unhighlightNode(next1, null, null);
  		    		
  		    		lang.nextStep();
  		    		src.unhighlight(7);
  		    		src.highlight(8);
  		    		children = getChildren(adjacencyMatrix, next1);
				
  		    
  		    		children = cleanChildren(children, queueS);

  		    		addCost(children, graph, next1, dist, queueS, 1, src, wayS );
  		    	
  		    		lang.nextStep();
  		    		src.unhighlight(8);
  		    		src.highlight(11);
  		    		
  		    		if(isInQueue(queueG, node)){
  		    			if(queueSCounter == 0){
  		    				part1 =  false;
  		    				}
  		    			lang.nextStep();
  		    			src.unhighlight(11);
  		    			
  		    		}else{
  		    			lang.nextStep();
  		    			src.unhighlight(11);
  		    			src.highlight(12);
  		    			part1 =  false;
  		    			part2 =  false;
  		    			
  		    			
  		    		}
  		    		
  		    	
  		    	}
  		    	//next to do <-----------------------------------------------------------------------------
  		    	if(part2){
  		    		
  		    		src2.highlight(6);
  		    		
  		    		lang.nextStep();
  		    		src2.unhighlight(6);
  		    		src2.highlight(7);
  		    		next2 = getLowest(queueG, dist, 2); 
				dist.highlightCell(2,next2 , null, null);
				next2Name = graph.getNodeLabel(next2);
				info.put(1,3,next2Name, null, null);
  		    		queueG.highlightCell(next2, null, null);
				graph.highlightNode(next2, null, null);
  		    		
  		    		lang.nextStep();
				dist.unhighlightCell(2,next2 , null, null);
  		    		String node = queueG.getData(next2);
  		    		queueG.put(next2, "-", null, null);
  		    		queueGCounter = queueGCounter-1; 
  		    		queueG.unhighlightCell(next2, null, null);
				graph.unhighlightNode(next2, null, null);
  		    		
  		    		lang.nextStep();
  		    		src2.unhighlight(7);
  		    		src2.highlight(8);
  		    		children2 = getChildren(adjacencyMatrix, next2);
  		    		children2 = cleanChildren(children2, queueG);
  		    		addCost(children2, graph, next2, dist, queueG, 2, src2, wayG );
  		    	
  		    		lang.nextStep();
  		    		src2.unhighlight(8);
  		    		src2.highlight(11);
  		    		
  		    		if(isInQueue(queueS, node)){
  		    			if(queueGCounter == 0){
  		    				part2 =  false;
  		    				}
  		    			lang.nextStep();
  		    			src2.unhighlight(11);
  		    			
  		    		}else{
  		    			lang.nextStep();
  		    			src2.unhighlight(11);
  		    			src2.highlight(12);
  		    			part1 =  false;
  		    			part2 =  false;
  		    		}
  		    	
  		    	}
  		    	
  		    	lang.nextStep();
  		    	time.getData(0);
  		    	time.put(0, (time.getData(0)+1), null, null);
  			    time.highlightCell(0, null, null);
  		    	lang.nextStep();
  		    	time.unhighlightCell(0, null, null);
  			    
  			    goOn = part1 | part2;
  		    	
  		    }
  		   
  		   //phase2 
  		    lang.nextStep();
  		        
  			src.hide();
			src2.hide();
  			src3.show();
  			min.show();
  			nameMin.show();
  			src3.highlight(1);
  			
  			
  			lang.nextStep("Phase 2 (Endverarbeitung)");
  			src3.unhighlight(1);
  			
  		
  			ArrayList<Integer> notInQNodes = new ArrayList<Integer>();
  			
  			for(int i = 0; i < queueS.getLength(); i++){
  				if(queueS.getData(i).equals("-") && queueG.getData(i).equals("-")){
  					notInQNodes.add(i);
  				}
  			}
  			
  			//phase2 loop
  			
  			int costMin;
  			int index = 0;
			Integer searchIndex;
  			
  			boolean notFound = true;
  			
  			while(!notInQNodes.isEmpty()){
  				src3.highlight(2);
  				costMin = Integer.parseInt(dist.getElement(1, notInQNodes.get(0)));
  				searchIndex =  notInQNodes.get(0);
				
				for(int i = 1; i < notInQNodes.size(); i++){
					
  					if(costMin > Integer.parseInt(dist.getElement(1, notInQNodes.get(i)))){
  						
  						searchIndex = notInQNodes.get(i);
						costMin = Integer.parseInt(dist.getElement(1, searchIndex)) ;
						
						
  					}
  				}
				index = searchIndex;
  				
  				
				
  				lang.nextStep();
  				src3.unhighlight(2);
  				src3.highlight(3);
  				graph.highlightNode(index, null, null);
				costMin = costMin + Integer.parseInt(dist.getElement(2, searchIndex));
				notInQNodes.remove(searchIndex);
  				if(costMin < min.getData(0) || notFound){
  					min.put(0, costMin, null, null);
					notFound = false;
  				}
  				
  				lang.nextStep();
  				src3.unhighlight(3);
  				
  				
  			}
			
			
  			
			lang.nextStep();
  			src3.highlight(4);
  			min.highlightCell(0, null, null);
			lang.nextStep();
			int i = index;
			String way = graph.getNodeLabel(i);			
			
			while( i != 0){
				graph.highlightEdge(i, wayS[i],null, null);
				graph.highlightNode(i,null,null);
							
				i = wayS[i];
				way = 	graph.getNodeLabel(i) + "-" +way;
			}
			graph.highlightNode(i,null,null);
			int j = index;
			 Node temp = graph.getTargetNode();
  		    	int end = graph.getPositionForNode(temp);
			while( j != end){
				graph.highlightEdge(j, wayG[j],null, null);
				graph.highlightNode(j,null,null);
					
				j = wayG[j];
				way = way + "-" +graph.getNodeLabel(j);
			}
			graph.highlightNode(j,null,null);
			src3.unhighlight(4);
			 lang.newText(new Coordinates(10, 480),"Da die beiden unidirektionalen Suchen aufeinander getroffen sind und weiterhin sämtliche ","enddescript1", null, textprops);
lang.newText(new Coordinates(10, 500),"Alternativrouten in der zweiten Phase untersucht wurden, ist mit L die Distanz des kürzesten Pfades gefunden.","enddescript2", null, textprops);
lang.newText(new Coordinates(10, 520),"Der gefundene Weg ist: " +way,"enddescript3", null, textprops);
lang.nextStep("Ende");
  		    
  		}
//returns the children of a node
  		public ArrayList<Integer> getChildren(int[][] adjacencyMatrix, int start){
  			ArrayList<Integer> children = new ArrayList<Integer>();
  			
  		for(int i = 0; i < adjacencyMatrix[start].length;i++  ){
  			if(adjacencyMatrix[start][i] != 0){
  				children.add(i);
  			}
  		}
  			
  		return children;
  		}
  		
  		//removes the visited children
  		public ArrayList<Integer> cleanChildren(ArrayList<Integer> children, StringArray queue){

  			for(int i = 0; i < queue.getLength();i++){
  				if(queue.getData(i).equals("-")){
					Integer index = new Integer (i);
  					children.remove(index);
  				}
  			}
  			
  		return children;
  		}
  		
  		// returns Index the lowest Node represented in the "Queue" (queue Index = graph Index )
  		
  		public int getLowest(StringArray queue, StringMatrix dist, int row){
  			int index = 0;
  			int temp;
  			int temp2 = Integer.MAX_VALUE;
  			boolean found = false;
  			
  			for(int i = 0; i < queue.getLength();i++){
  				if(!queue.getData(i).equals("-")){
  					if(!dist.getElement(row, i).equalsIgnoreCase("oo")){
  						temp = Integer.parseInt(dist.getElement(row, i));
  						if(found){
  							if(temp < temp2){
  								temp2 = temp;
  								index = i;
  							}
  						}else{
  							index = i;
  							temp2 = temp;
  							found = true;
  						}
  					}
  				}
  			}
  				
  			
  			return index;
  		}
  		
  		//adds the costs of a node
  		public void addCost(ArrayList<Integer> children, Graph graph, int start, StringMatrix dist, StringArray queue, int row, SourceCode source, int way[] ){
  			 for(int i = 0; i < children.size();i++ ){
  				 	lang.nextStep();
  				 	source.unhighlight(8);
  					int cost = graph.getEdgeWeight(start, children.get(i));
  			    	int costParent = Integer.parseInt(dist.getElement(row,start));
  			    	int costNew = cost + costParent;
  			    	if(dist.getElement(row,children.get(i)).equals("oo")){
  			    		dist.put(row, children.get(i), Integer.toString(costNew), null, null);
					way[children.get(i)] = start;
  			    	}else{	
  			    		int costOld = Integer.parseInt(dist.getElement(row,children.get(i)));
  			    		if(costNew < costOld ){
  			    			dist.put(row, children.get(i), Integer.toString(costNew), null, null);
						way[children.get(i)] = start;
  			    		}
  			    	}
  			    	
  			    	graph.highlightNode(children.get(i), null, null);
  			    	dist.highlightCell(row,children.get(i) , null, null);
  			    	source.highlight(9);
  			    	source.highlight(10);
  			    	lang.nextStep();
  			    	source.unhighlight(9);
  			    	source.unhighlight(10);
  			    	source.highlight(8);
  			    	graph.unhighlightNode(children.get(i), null, null);
  			    	dist.unhighlightCell(row,children.get(i) , null, null);
  				 
  				 
  			    }
  			
  		}

  		
  		public boolean isInQueue(StringArray queue, String node){
  			for(int i = 0; i < queue.getLength(); i++){
  				if(queue.getData(i).equals(node)){
  					return true;
  				}
  			}
  			
  			return false;
  		}
		
		public Node[] getNodes(Graph graph){
			int size = graph.getSize();
   		 	Node[] nodes = new Node[size];
    
    			for (int i = 0; i < size; i++){
      				nodes[i] = graph.getNode(i);
			}
			return nodes; 
		}
		public String[] getLabels(Graph graph){
			int size = graph.getSize();
   		 	String[] labels = new String[size];
    
    			for (int i = 0; i < size; i++){
      				labels[i] = graph.getNodeLabel(i);
			}
			return labels; 
		}
}
