/*
 * undirectedGraphicalModel.java
 * Sebastian Jest�dt, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.graph;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;
import java.util.Map;

import algoanim.primitives.Graph;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.graph.helpers.Pair;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;
import translator.Translator;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import animal.main.Animal;

public class UndirectedGraphicalModel implements Generator {
    private Language lang;
    private SourceCodeProperties sourceCode;
    private TextProperties text;
    private Graph graph;

    public void init(){
        lang = new AnimalScript("Undirected Graphical Model", "Sebastian Jest�dt", 800, 600);
        lang.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        sourceCode = (SourceCodeProperties)props.getPropertiesByName("sourceCode");
        text = (TextProperties)props.getPropertiesByName("text");
        graph = (Graph)primitives.get("graph");
        
        graph.setAdjacencyMatrix(setAdjMatrixDoubled(graph.getAdjacencyMatrix()));
        
        Graph g = lang.newGraph("graph", graph.getAdjacencyMatrix(), graph.getNodes(), getNodeLabelsAsArray(graph), null, getDefaultGraphProperties());
        g.hide();
        graph = g;
        start();
        return lang.toString();
    }

    public String getName() {
        return "Undirected Graphical Model";
    }

    public String getAlgorithmName() {
        return "Undirected Graphical Model";
    }

    public String getAnimationAuthor() {
        return "Sebastian Jest�dt";
    }

    public String getDescription(){
        return "Undirected Graphical Model dient zur Aufstellung eines formalen Modells, das einen ungerichteten Graphen beschreibt. Anhand dieses Modells k�nnen dann Wahrscheinlichkeiten f�r Ereignisse berechnet werden.";
    }

    public String getCodeExample(){
        return "1. W�hle einen Knoten aus dem Graphen aus."
 +"\n"
 +"2. Betrachte alle mit dem Knoten verbundenen Knoten um daraus Cliquen zu bilden."
 +"\n"
 +"3. Sind die marktierten Knoten vollst�ndig miteinander verbunden?"
 +"\n"
 +"    3.1 Ja."
 +"\n"
 +"        3.1.1 Trage die maximale Clique in das Modell ein."
 +"\n"
 +"    3.2 Nein."
 +"\n"
 +"        3.2.1 Trage die einzelnen Cliquen in das Modell ein."
 +"\n"
 +"4. Sobald alle Cliquen gefunden wurden, ist das Modell fertig.";
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
    
@SuppressWarnings("unchecked")
public void start() {
		
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
		
		GraphProperties graphProps = getDefaultGraphProperties();
		SourceCodeProperties scProps = getDefaultSourceCodeProperties();
		  
		Translator translator = getTranslator();
		//create header 
		TextProperties headerProps = new TextProperties();
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 24));
		Text header = lang.newText(new Coordinates(10, 30), "Undirected Graphical Model", "header", null, headerProps);
		 
		RectProperties rectProps = new RectProperties();
		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		Rect headRect = lang.newRect(new Offset(-5, -5, "header",
				AnimalScript.DIRECTION_NW), new Offset(5, 5, "header", "SE"), "headerRect", null, rectProps);
		  
		SourceCode introSC = getIntro(translator,header);
		//intro
		lang.nextStep(translator.translateMessage("intro"));

		
		  
		Text bm_header = lang.newText(new Offset(0, 25, header, "SW"), translator.translateMessage("basic_models_header"), "basic_models_header", null, headerProps);
		  
		SourceCode basicModelsSc = getBasicModelsSourceCodes(translator, scProps,bm_header);
		
		//create chain tree
		Pair<Integer,Integer>[] chainEdges = new Pair[2];
		chainEdges[0] = new Pair<Integer, Integer>(0, 1);
		chainEdges[1] = new Pair<Integer, Integer>(1, 2);
		Coordinates[] chainCoord = {new Coordinates(50, 230), new Coordinates(170, 230), new Coordinates(290,230)};
		//create full tree
		Pair<Integer,Integer>[] fullEdges = new Pair[3];
		fullEdges[0] = new Pair<Integer, Integer>(0, 2);
		fullEdges[1] = new Pair<Integer, Integer>(1, 2);
		fullEdges[2] = new Pair<Integer, Integer>(1, 0);
		Coordinates[] fulltCoord = {new Coordinates(50, 430), new Coordinates(170, 430), new Coordinates(110,490)};
		String[] bmNodeLabels = {"A","B","C"};
		Graph chainGraph = createGraph("chainGraph",graphProps, chainEdges, chainCoord, bmNodeLabels);
		SourceCode bmChainSc = getBasicModelsChainSourceCodes(translator, scProps, chainGraph);
		
		Graph fullGraph = createGraph("convergentGraph",graphProps, fullEdges, fulltCoord, bmNodeLabels);
		SourceCode bmFullSc = getBasicModelsConvergentSourceCodes(translator, scProps, fullGraph);
		
		introSC.hide();
		//basic models
		lang.nextStep(translator.translateMessage("basics"));
		
		chainGraph.hide();
		fullGraph.hide();
		bm_header.hide();
		basicModelsSc.hide();
		bmChainSc.hide();
		bmFullSc.hide();
		//use input 
		//Graph graph = getGraph();

		
		Text result_txt = lang.newText(new Coordinates(30, 225), "", "result_txt", null,getDefaultTextProperties());
		Text info_txt = lang.newText(new Offset(5, 5, "result_txt", "SE"), "", "info_txt", null,getDefaultTextProperties());
		
		SourceCode scodeSc = getSourceCode(translator, header);
		graph.show();
		lang.nextStep(translator.translateMessage("algo"));	
		
		//********Start Animation**********
		executeAlgo(graph, result_txt, info_txt, scodeSc, translator);
		

			
		//*********step4**********
		scodeSc.unhighlight(0);
	    scodeSc.unhighlight(1);
	    scodeSc.unhighlight(2);
		scodeSc.unhighlight(3);
	    scodeSc.unhighlight(4);
	    scodeSc.unhighlight(5);
	    scodeSc.unhighlight(6);
	    info_txt.hide();
	    info_txt.setText("", null, null);
	    scodeSc.highlight(7);
	  	lang.nextStep();
	  			
		getOutroSourceCode(translator, header);
		
		graph.hide();
		scodeSc.hide();
		result_txt.hide();
		lang.nextStep(translator.translateMessage("outro"));
		lang.finalizeGeneration();
		
	}
	
	private HashMap<String, ArrayList<Integer>> getCliquesOfNode(Graph graph, int node){
		
		int[][] adjacencyMatrix = graph.getAdjacencyMatrix();
		HashMap<String, ArrayList<Integer>> allCiques = new HashMap<String, ArrayList<Integer>>();
		
		ArrayList<Integer> i_depend = getDependencies(adjacencyMatrix, node);
		ArrayList<Integer> i_max_candi = new ArrayList<Integer>();
		ArrayList<Integer> i_cliq = new ArrayList<Integer>();
		i_max_candi.add(node);
		for(int n : i_depend) {
			ArrayList<Integer> n_depend = getDependencies(adjacencyMatrix, n);
			if(containNodes(i_depend, n_depend)) {
				i_max_candi.add(n);
			}
			else {
				i_cliq.add(n);
				//adjacencyMatrix[node][n] = 0;
				//adjacencyMatrix[n][i] = 0;
			  }  
		  }
		
		boolean fullConnected = fullConnected(i_max_candi, adjacencyMatrix);

		if(i_max_candi.size() > 1 && fullConnected) {
			Collections.sort(i_max_candi);
			String key = listToKey(i_max_candi);
			allCiques.put(key, i_max_candi);
		}
		
		for(int p : i_cliq) {
			ArrayList<Integer> touple = new ArrayList<Integer>();
			touple.add(node);
			touple.add(p);
			Collections.sort(touple);
			String key = String.valueOf(touple.get(0)) + String.valueOf(touple.get(1));
			allCiques.put(key, touple);
		}
		
		
		
		return allCiques;
	}
	
	private void executeAlgo(Graph graph, Text result_txt, Text info_txt, SourceCode scodeSc, Translator translator) {
		
		boolean q2 = true;
		boolean q1 = true;
		
		String result = "p(" + getNodeLabelsAsString(graph) + ")" + " = ";
		StringBuilder model = new StringBuilder();
		model.append(result);
		result_txt.setText(model.toString(), null, null);
		Node[] nodes = graph.getNodes();
		HashMap<String, ArrayList<Integer>> curNodes = new HashMap<String, ArrayList<Integer>>();
		
		for(int n = 0; n < nodes.length; n++) {
			
			HashMap<String, ArrayList<Integer>>cliquesOfN = getCliquesOfNode(graph, n);
			ArrayList<Integer> neighbours = getDependencies(graph.getAdjacencyMatrix(), n);
			while(cliquesOfN.size() == 0 && neighbours.size() > 0) {
				n++;
				cliquesOfN = getCliquesOfNode(graph, n);
				neighbours = getDependencies(graph.getAdjacencyMatrix(), n);
			}
			
			//***********step1***********
			
			scodeSc.unhighlight(6);
		    scodeSc.unhighlight(4);
		    scodeSc.unhighlight(5);
		    scodeSc.unhighlight(3);
		    scodeSc.highlight(0);
    		info_txt.setText("", null, null);
    		info_txt.hide();
    		result_txt.setText(model.toString(), null, null);
		    graph.highlightNode(n, null, null);
		    lang.nextStep();
		    
		    
		    //***********step2***********
		    scodeSc.unhighlight(0);
		    scodeSc.highlight(1);
		    
		    for(int p = 0; p < neighbours.size(); p++) {
		    	graph.highlightNode(neighbours.get(p), null, null);
		    }
	
		    lang.nextStep();
		    
		    //***********step3***********
		    scodeSc.unhighlight(1);
		    scodeSc.highlight(2);
		    lang.nextStep();
		    
		    
		    //HashMap<String, ArrayList<Integer>>cliquesOfN = getCliquesOfNode(graph, n);
		    
		    /*
		    if(neighbours.size() > 1 && cliquesOfN.size() == 0) {
		    	int temp = 0;
		    	while(temp < neighbours.size()) {
		    		cliquesOfN = getCliquesOfNode(graph, neighbours.get(temp));
		    		temp++;
		    		if(cliquesOfN.size() > 2) {
		    			break;
		    		}
		    	}
		    }
		    */
		    
	    	for(int p = 0; p < neighbours.size(); p++) {
	    		graph.unhighlightNode(neighbours.get(p), null, null);
		    }
		    
		    for(Map.Entry<String, ArrayList<Integer>> entry : cliquesOfN.entrySet()) {
	    		String key = entry.getKey(); 
	    		ArrayList<Integer> pair = entry.getValue();
		    
	    		//System.out.println(graph.getNodeLabel(n));
	    		//System.out.println(pair);
	    		if(pair.size() > 2) {
		    	
			    	//***********3.1***********	    
				    scodeSc.unhighlight(1);
				    scodeSc.unhighlight(2);
				    scodeSc.unhighlight(5);
				    scodeSc.unhighlight(6);
				    scodeSc.highlight(3);
				    
			    	for(int p = 0; p < pair.size(); p++) {
			    		graph.highlightNode(pair.get(p), null, null);
				    }
			    	
			    	
		    		String clique = "f(" +  graph.getNodeLabel(pair.get(0));
		    		for(int i = 1; i < pair.size();i++) {
		    			clique = clique + "," + graph.getNodeLabel(pair.get(i));
		    		}
		    		clique = clique + ")";
		    		
		    		
		    		
		    		
		    		if(!curNodes.containsKey(key)) {
			    		model.append(clique);
			    		result_txt.setText(model.toString(), null, null);
			    		curNodes.put(key, pair);
			    		
			    		//########Questions########
		    			if(q1) {
		    				result_txt.setText("", null, null);
		    				createQuestionModel(lang, "q1", translator.translateMessage("q1f") , clique, 1,  translator.translateMessage("q1a"));
		    				q1 = false;
		    			}
		    			else {
			    			MultipleChoiceQuestionModel mcModel = new MultipleChoiceQuestionModel("q4");
		  				  	mcModel.setPrompt(translator.translateMessage("q34f"));
		  				  	//check if we have a chain or a convergent model and set the answers
		  				  	mcModel.addAnswer("Ja", 1, translator.translateMessage("at"));
		  				  	mcModel.addAnswer("Nein", 0, translator.translateMessage("af"));
		  				  	lang.addMCQuestion(mcModel);
		  				  	
		    			}
		    		}
		    		else {
		    			
		    			String info = translator.translateMessage("info1.1") 
		    					+ " " + clique 
		    					+ " " + translator.translateMessage("info1.2")  
		    					+ " " + graph.getNodeLabel(n)
		    					+ " " + translator.translateMessage("info1.3") ;
		    			info_txt.setText(info, null, null);
		    			info_txt.show();
		    		}
				    //***********step 3.1.1***********
				    scodeSc.highlight(4);
			    	lang.nextStep();
			    	
			    	graph.unhighlightNode(n, null, null);
			    	for(int p = 0; p < neighbours.size(); p++) {
			    		graph.unhighlightNode(neighbours.get(p), null, null);
				    }
		    	}
	    		else {
	    			

		    	
		    	//***********step3.2***********
			    scodeSc.unhighlight(1);
			    scodeSc.unhighlight(6);
			    scodeSc.highlight(5);
		   
			    //***********step 3.2.1***********
		    		 
		    	if(pair.size() == 2) {
		    				    		
		    		graph.highlightNode(pair.get(0), null, null);
		    		graph.highlightNode(pair.get(1), null, null);
		    		String clique = "f(" + graph.getNodeLabel(pair.get(0)) + "," +  graph.getNodeLabel(pair.get(1)) + ")";
		    		
		    		if(!curNodes.containsKey(key)) {
		    			model.append(clique);
			    		result_txt.setText(model.toString(), null, null);
			    		info_txt.setText("", null, null);
			    		info_txt.hide();
			    		curNodes.put(key, pair);
			    		//########Questions########
		    			if(q2) {
		    				result_txt.setText("", null, null);
		    				createQuestionModel(lang, "q5", translator.translateMessage("q2f"), clique, 1, translator.translateMessage("q2a"));
		    				q2 = false;
		    			}
		    			else {
			    			MultipleChoiceQuestionModel mcModel = new MultipleChoiceQuestionModel("q3");
		  				  	mcModel.setPrompt(translator.translateMessage("q34f"));
		  				  	//check if we have a chain or a convergent model and set the answers
		  				  	mcModel.addAnswer("Ja", 0, translator.translateMessage("af"));
		  				  	mcModel.addAnswer("Nein", 1, translator.translateMessage("at"));
		  				  	lang.addMCQuestion(mcModel);
		  				  	
		    			}
	  				  	
			    		
		    		}
		    		else {
		    			
		    			String info = translator.translateMessage("info1.1") 
		    					+ " " + clique 
		    					+ " " + translator.translateMessage("info1.2")  
		    					+ " " + graph.getNodeLabel(n)
		    					+ " " + translator.translateMessage("info1.3") ;
		    			info_txt.setText(info, null, null);
		    			info_txt.show();
		    		}
		    		
		    		
		    	}
		    	
	    		 scodeSc.unhighlight(2);
				 scodeSc.highlight(6);
	    		 lang.nextStep();
	    		 for(int value :pair ) {
	    			 graph.unhighlightNode(value, null, null);
	    		 }

		    	}
		    	
		    }
		}
		
	}
		
	
	private String listToKey(ArrayList<Integer> list) {
		StringBuilder sb = new StringBuilder();
		for(Integer p : list) {
			sb.append(p);
		}
		
		return sb.toString();
	}
	
	
	private boolean fullConnected(ArrayList<Integer> nodes, int[][] adjacencyMatrix) {
		
		  boolean fullConnected = true;
		  
		  for(int p1 = 0; p1 < nodes.size(); p1++) {
			  int p2 = 0;
			  while(p2 < nodes.size()) {
				  if(adjacencyMatrix[nodes.get(p1)][nodes.get(p2)] == 0 && adjacencyMatrix[nodes.get(p2)][nodes.get(p1)] == 0 && p1 != p2){
					  fullConnected = false;
				  }
				  p2++;
			  }
		  }
		  
		  return fullConnected;		
	}
	
	private boolean containNodes(ArrayList<Integer> nl1, ArrayList<Integer> nl2) {
		
		for(int p : nl1) {
			if(nl2.contains(p)) {
				return true;
			}
		}
		return false;
	}
		  
		  

	  private Translator getTranslator() {
		  Locale locale = Locale.GERMANY;
		  Translator translator = new Translator("/resources/UndirectedGraphicalModel", locale);
		  
		  return translator;
	  }
	  
	  private SourceCodeProperties getDefaultSourceCodeProperties() {
		  
		  return this.sourceCode;
	  }
	  
	  private GraphProperties getDefaultGraphProperties() {
		  
		  GraphProperties graphProps = new GraphProperties();
		  
		  graphProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		  graphProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		  graphProps.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, false);
		  graphProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		  graphProps.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY, Color.BLACK);
		  graphProps.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, false);
		  graphProps.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY, Color.BLUE);
		  
		  return graphProps;
		  
	  }
	  
	  private TextProperties getDefaultTextProperties() {
		  
		  return this.text;
	  }
	  
	  
	  private SourceCode getIntro(Translator translator, Text ref) {
		  
		  SourceCodeProperties introProps = getDefaultSourceCodeProperties();
		  
		  SourceCode sc = lang.newSourceCode(new Offset(0, 20, ref, "SW"), "intro", null,introProps);
		  
		  sc.addCodeLine(translator.translateMessage("intro_l0"), "line0", 0, null);
		  sc.addCodeLine(translator.translateMessage("intro_l1"), "line1", 0, null);
		  sc.addCodeLine(translator.translateMessage("intro_l2"), "line2", 0, null);
		  sc.addCodeLine(translator.translateMessage("intro_l3"), "line3", 0, null);
		  sc.addCodeLine(translator.translateMessage("intro_l4"), "line4", 0, null);
		  sc.addCodeLine(translator.translateMessage("intro_l5"), "line5", 0, null);
		  sc.addCodeLine(translator.translateMessage("intro_l6"), "line6", 0, null);
		  sc.addCodeLine(translator.translateMessage("intro_l7"), "line7", 0, null);

		  return sc;
	  }

	  
	  private SourceCode  getBasicModelsSourceCodes(Translator translator, SourceCodeProperties scProps, Text ref) {
		  
		  SourceCode basic_models = lang.newSourceCode(new Offset(0, 10, ref, "SW"), "basic_models", null, scProps);
		  basic_models.addCodeLine(translator.translateMessage("basic_models_l0"), "basic_models_l0", 0, null);
		  basic_models.addCodeLine(translator.translateMessage("basic_models_l1"), "basic_models_l1", 0, null);
		  basic_models.addCodeLine(translator.translateMessage("basic_models_l2"), "basic_models_l2", 0, null);
		  
		  return basic_models;
	  }
	  
	  private SourceCode  getBasicModelsChainSourceCodes(Translator translator, SourceCodeProperties scProps, Graph ref) {
		  	  
		  SourceCode bm_chain = lang.newSourceCode(new Offset(-30, 20, ref, "SW"), "basic_models", null, scProps);
		  bm_chain.addCodeLine(translator.translateMessage("bm_chain_l0"), "bm_chain_l0", 0, null);
		  bm_chain.addCodeLine(translator.translateMessage("bm_chain_l1"), "bm_chain_l1", 0, null);
		  bm_chain.addCodeLine(translator.translateMessage("bm_chain_l2"), "bm_chain_l2", 0, null);
		  bm_chain.addCodeLine(translator.translateMessage("bm_chain_l3"), "bm_chain_l3", 0, null);
		  bm_chain.addCodeLine(translator.translateMessage("bm_chain_l4"), "bm_chain_l4", 0, null);
		  
		  return bm_chain;
	  }
	  
	  private SourceCode  getBasicModelsConvergentSourceCodes(Translator translator, SourceCodeProperties scProps, Graph ref) {
	  	  
		  SourceCode bm_convergent = lang.newSourceCode(new Offset(-30, 20, ref, "SW"), "bm_full", null, scProps);
		  bm_convergent.addCodeLine(translator.translateMessage("bm_full_l0"), "bm_full_l0", 0, null);
		  bm_convergent.addCodeLine(translator.translateMessage("bm_full_l1"), "bm_full_l1", 0, null);
		  bm_convergent.addCodeLine(translator.translateMessage("bm_full_l2"), "bm_full_l2", 0, null);
		  bm_convergent.addCodeLine(translator.translateMessage("bm_full_l3"), "bm_full_l3", 0, null);
		  bm_convergent.addCodeLine(translator.translateMessage("bm_full_l4"), "bm_full_l4", 0, null);
		  
		  return bm_convergent;
	  }
	  
	  
	  
	  public SourceCode getSourceCode(Translator translator, Text ref) {
		  
		  //set properties for the source code
		  SourceCodeProperties scProps = getDefaultSourceCodeProperties();
		  
		  SourceCode sc = lang.newSourceCode(new Offset(0, 300, ref, "SW"), "sourceCode",null, scProps);
		  //add code lines
		  sc.addCodeLine(translator.translateMessage("algo_l0"), "algo_l0", 0,null);
		  sc.addCodeLine(translator.translateMessage("algo_l1"), "algo_l1", 0,null);
		  sc.addCodeLine(translator.translateMessage("algo_l2"), "algo_l2", 0,null);
		  sc.addCodeLine(translator.translateMessage("algo_l3"), "algo_l3", 1,null);
		  sc.addCodeLine(translator.translateMessage("algo_l4"), "algo_l4", 2,null);
		  sc.addCodeLine(translator.translateMessage("algo_l5"), "algo_l5", 1,null);
		  sc.addCodeLine(translator.translateMessage("algo_l6"), "algo_l6", 2,null);
		  sc.addCodeLine(translator.translateMessage("algo_l7"), "algo_l7", 0,null);
		  
		  return sc;
		  
	  }
	  
	  public SourceCode getOutroSourceCode(Translator translator, Text ref) {
		  
		  //set properties for the source code
		  SourceCodeProperties scProps = getDefaultSourceCodeProperties();
		  
		  SourceCode sc = lang.newSourceCode(new Offset(0, 20, ref, "SW"), "outro",null, scProps);
		  //add code lines
		  sc.addCodeLine(translator.translateMessage("outro_l0"), "outro_l0", 0, null);
		  sc.addCodeLine(translator.translateMessage("outro_l1"), "outro_l1", 0, null);
		  sc.addCodeLine(translator.translateMessage("outro_l2"), "outro_l2", 0, null);
		  
		  return sc;
		  
	  }
	  
	  
	  private Graph createGraph(String id, GraphProperties graphProps, Pair<Integer,Integer>[] edges, Coordinates[] coord, String[] nodeLabels) {
		  	  
		  int numNodes = nodeLabels.length;
		  
		  
		  int[][] graphAdjacencyMatrix = new int[numNodes][numNodes];
		  
		  for (int i = 0; i < graphAdjacencyMatrix.length; i++) {
			  for (int j = 0; j < graphAdjacencyMatrix[0].length; j++) {
				  graphAdjacencyMatrix[i][j] = 0;
			  }
		  }
		  
		  for (int i = 0; i < edges.length; i++) {
			  graphAdjacencyMatrix[edges[i].getFirst()][edges[i].getSecond()] = 1;
		  }
		  
		  Graph graph = lang.newGraph(id, graphAdjacencyMatrix, coord, nodeLabels, null, graphProps);
		  
		  return graph;
	  }
	  
	  public FillInBlanksQuestionModel createQuestionModel(Language lang,String id, String question, String answer, int points, String feedback) {
		  FillInBlanksQuestionModel qModel = new FillInBlanksQuestionModel(id);
		  qModel.setPrompt(question);
		  qModel.addAnswer(answer,points,feedback);
		  
		  lang.addFIBQuestion(qModel);
		  
		  return qModel;
	  }
	  
	  public MultipleChoiceQuestionModel createMcModel(Language lang,String id, String question, String answer, int points, String feedback) {
		  MultipleChoiceQuestionModel mcModel = new MultipleChoiceQuestionModel(id);
		  mcModel.addAnswer("Chain Modell", 1, "ist keins");
		  mcModel.addAnswer("convergent", 1, "ist eins");
		  
		  
		  lang.addMCQuestion(mcModel);
		  
		  return mcModel;
	  }
	  

	  
	  private ArrayList<Integer> getDependencies(int[][] adjacencyMatrix, int node) {
		  
		  ArrayList<Integer> list = new ArrayList<Integer>();
		  
		  for (int j = 0; j < adjacencyMatrix.length ; j++){
			  if(adjacencyMatrix[node][j] == 1) {
				  list.add(j);
			  }
		  }
		  
		  return list;
	  }
	  
	  
	  private String getNodeLabelsAsString(Graph graph) {
		  Node[] nodes = graph.getNodes();
		  String result = graph.getNodeLabel(nodes[0]);
		  for(int i = 1; i < nodes.length; i++) {
			  result = result + "," + graph.getNodeLabel(nodes[i]);
		  }
		  
		  return result;
	  }
	  
      private String[] getNodeLabelsAsArray(Graph graph) {
    	  Node[] nodes = graph.getNodes();
    	  
    	  String[] result = new String[nodes.length];
    	  for(int i = 0; i < nodes.length; i++) {
    		 
    		  result[i] = graph.getNodeLabel(nodes[i]);
    	  }
    	  
    	  return result;
      }
      
      private int[][] setAdjMatrixDoubled(int[][] matrix){
    	  for(int i = 0; i < matrix.length; i++) {
    		  for(int j = 0; j < matrix.length; j++) {
    			  if(matrix[i][j] == 1) {
    				  matrix[j][i] = 1;
    			  }
    		  }
    	  }
    	  return matrix;
      }
	  
	  public Graph getGraph() {
		  
		  GraphProperties graphProps = new GraphProperties();
		  
		  graphProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		  graphProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		  graphProps.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, false);
		  graphProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		  graphProps.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY, Color.BLACK);
		  graphProps.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, false);
		  graphProps.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY, Color.BLUE);
		  
		  int[][] graphAdjacencyMatrix = new int[9][9];
		  
		  for (int i = 0; i < graphAdjacencyMatrix.length; i++) {
			  for (int j = 0; j < graphAdjacencyMatrix[0].length; j++) {
				  graphAdjacencyMatrix[i][j] = 0;
			  }
		  }
		  
		  Node[] graphNodes = new Node[9];
		  graphNodes[0] = new Coordinates(50,100);
		  graphNodes[1] = new Coordinates(50,160);
		  graphNodes[2] = new Coordinates(110,130);
		  graphNodes[3] = new Coordinates(110,190);
		  graphNodes[4] = new Coordinates(170,100);
		  graphNodes[5] = new Coordinates(170,160);
		  graphNodes[6] = new Coordinates(230,160);
		  graphNodes[7] = new Coordinates(230,100);
		  graphNodes[8] = new Coordinates(290,130);
			  
		    // set the edges with the corresponding weights
		    graphAdjacencyMatrix[0][2] = 1;
		    graphAdjacencyMatrix[2][0] = 1;
		    
		    graphAdjacencyMatrix[1][2] = 1;
		    graphAdjacencyMatrix[2][1] = 1;
		    graphAdjacencyMatrix[1][3] = 1;
		    graphAdjacencyMatrix[3][1] = 1;
		    

		    graphAdjacencyMatrix[2][5] = 1;
		    graphAdjacencyMatrix[5][2] = 1;
		    
		    graphAdjacencyMatrix[3][5] = 1;
		    graphAdjacencyMatrix[5][3] = 1;
		    
		    graphAdjacencyMatrix[4][5] = 1;
		    graphAdjacencyMatrix[5][4] = 1;
		    graphAdjacencyMatrix[4][6] = 1;
		    graphAdjacencyMatrix[6][4] = 1;
		    
		    graphAdjacencyMatrix[5][6] = 1;
		    graphAdjacencyMatrix[6][5] = 1;
		    
		    graphAdjacencyMatrix[7][4] = 1;
		    graphAdjacencyMatrix[4][7] = 1;
		    graphAdjacencyMatrix[7][5] = 1;
		    graphAdjacencyMatrix[5][7] = 1;
		    
		    graphAdjacencyMatrix[8][6] = 1;
		    graphAdjacencyMatrix[6][8] = 1;
		    graphAdjacencyMatrix[7][8] = 1;
		    graphAdjacencyMatrix[8][7] = 1;

		  String[] nodeLabels = {"A","B","C","D","E","F","G","H", "I"};
		  
		  Graph graph = lang.newGraph("graph", graphAdjacencyMatrix, graphNodes, nodeLabels, null, graphProps);
		  
		  return graph;
	  }
	  
	    public static void main(String[] args) {
			Generator generator = new UndirectedGraphicalModel(); // Generator erzeugen
			Animal.startGeneratorWindow(generator); // Animal mit Generator starten
		}

}