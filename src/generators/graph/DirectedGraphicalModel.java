package generators.graph;
/*
 * DirectedGraphicelModel.java
 * Sebastian Jest�dt, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
//package generators.graph;



import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;

import org.apache.commons.math3.stat.descriptive.moment.SemiVariance.Direction;

import algoanim.primitives.Graph;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.graph.helpers.Pair;
import generators.helpers.OffsetCoords;
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

public class DirectedGraphicalModel implements Generator {
    private Language lang;
    private SourceCodeProperties sourceCode;
    private TextProperties text;
    private Graph graph;

    public void init(){
        lang = new AnimalScript("Directed Graphical Model", "Sebastian Jestädt", 800, 600);
        lang.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        sourceCode = (SourceCodeProperties)props.getPropertiesByName("sourceCode");
        text = (TextProperties)props.getPropertiesByName("text");
        graph = (Graph)primitives.get("graph");
        Graph g = lang.newGraph("graph", graph.getAdjacencyMatrix(), graph.getNodes(), getNodeLabelsAsArray(graph), null, getDefaultGraphProperties());
        g.hide();
        start(g);
        return lang.toString();
    }

    public String getName() {
        return "Directed Graphical Model";
    }

    public String getAlgorithmName() {
        return "Directed Graphical Model";
    }

    public String getAnimationAuthor() {
        return "Sebastian Jestädt";
    }

    public String getDescription(){
        return "Hier wird dargestellt, wie man aus einem gerichteten Graphen ein Wahrscheinilchkeitsmodell erstellt.";
    }

    public String getCodeExample(){
        return "1. Wähle einen Knoten aus dem Graphen aus."
 +"\n"
 +"2. Hat der Knoten, andere Knoten, die auf diesen Zeigen?"
 +"\n"
 +"  2.1. Ja"
 +"\n"
 +"    2.1.1. Füge den Knoten als bedingte Wahrscheinlichkeit in das formale Modell ein."
 +"\n"
 +"    2.1.2. Füge alle Knoten, die auf den Knoten zeigen als Bedingung ein."
 +"\n"
 +"  2.2. Nein"
 +"\n"
 +"    2.2.1. Trage den Knoten als A-Priori-Wahrscheinlichkeit in das formale Modell ein."
 +"\n"
 +"3. Wenn alle Knoten einmal angeschaut wurden, ist das formale Modell fertig.";
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
    public void start(Graph graph){
  	  
  	  lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
		
  		GraphProperties graphProps = getDefaultGraphProperties();
  		SourceCodeProperties scProps = getDefaultSourceCodeProperties();
  		  
  		Translator translator = getTranslator();
  		//create header 
  		TextProperties headerProps = new TextProperties();
  		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 24));
  		Text header = lang.newText(new Coordinates(10, 30), "Directed Graphical Model", "header", null, headerProps);
  		 
  		RectProperties rectProps = new RectProperties();
  		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
  		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
  		rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
  		Rect headRect = lang.newRect(new Offset(-5, -5, "header",
  				AnimalScript.DIRECTION_NW), new Offset(5, 5, "header", "SE"), "headerRect", null, rectProps);
  		  
  		SourceCode introSC = getIntro(translator,header);
  		//intro finish
  		lang.nextStep(translator.translateMessage("intro"));

  		
  		  
  		Text bm_header = lang.newText(new Offset(0, 25, header, "SW"), translator.translateMessage("basic_models_header"), "basic_models_header", null, headerProps);
  		  
  		SourceCode basicModelsSc = getBasicModelsSourceCodes(translator, scProps,bm_header);
  		
  		//create chain tree
  		Pair<Integer,Integer>[] chainEdges = new Pair[2];
  		chainEdges[0] = new Pair<Integer, Integer>(0, 1);
  		chainEdges[1] = new Pair<Integer, Integer>(1, 2);
  		Coordinates[] chainCoord = {new Coordinates(50, 230), new Coordinates(170, 230), new Coordinates(290,230)};
  		//create convergent tree
  		Pair<Integer,Integer>[] convergentEdges = new Pair[2];
  		convergentEdges[0] = new Pair<Integer, Integer>(0, 2);
  		convergentEdges[1] = new Pair<Integer, Integer>(1, 2);
  		Coordinates[] convergentCoord = {new Coordinates(50, 430), new Coordinates(170, 430), new Coordinates(110,490)};
  		String[] bmNodeLabels = {"A","B","C"};
  		Graph chainGraph = createGraph("chainGraph",graphProps, chainEdges, chainCoord, bmNodeLabels);
  		SourceCode bmChainSc = getBasicModelsChainSourceCodes(translator, scProps, chainGraph);
  		
  		Graph convergentGraph = createGraph("convergentGraph",graphProps, convergentEdges, convergentCoord, bmNodeLabels);
  		SourceCode bmConvergentSc = getBasicModelsConvergentSourceCodes(translator, scProps, convergentGraph);
  		
  		introSC.hide();
  		//basic models finish
  		lang.nextStep(translator.translateMessage("basics"));
  		
  		chainGraph.hide();
  		convergentGraph.hide();
  		bm_header.hide();
  		basicModelsSc.hide();
  		bmChainSc.hide();
  		bmConvergentSc.hide();

  		
  		Text result_txt = lang.newText(new Coordinates(30, 225), "", "result_txt", null,getDefaultTextProperties());
  		
  		SourceCode scodeSc = getSourceCode(translator, header);
  		graph.show();
  		//start algo
  		lang.nextStep(translator.translateMessage("algo"));
  		evaluateGraph(graph, scodeSc, lang, result_txt, translator);

  		getOutroSourceCode(translator, header);
  		
  		graph.hide();
  		scodeSc.hide();
  		result_txt.hide();
  		//intro finish
  		lang.nextStep(translator.translateMessage("outro"));
  		lang.finalizeGeneration();
      }
      
      private Translator getTranslator() {
    	  Locale locale = Locale.GERMANY;
    	  Translator translator = new Translator("/resources/DirectedGraphicalModel", locale);
    	  
    	  return translator;
      }
      
      private SourceCodeProperties getDefaultSourceCodeProperties() {
    	  
    	  SourceCodeProperties scProps = new SourceCodeProperties();
    	  scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    	  scProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
    			  new Font(Font.SANS_SERIF, Font.PLAIN, 18));
    	  scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    	  
    	  return this.sourceCode;
      }
      
      private GraphProperties getDefaultGraphProperties() {
    	  
    	  GraphProperties graphProps = new GraphProperties();
    	  
    	  graphProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    	  graphProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    	  graphProps.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, true);
    	  graphProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    	  graphProps.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY, Color.BLACK);
    	  graphProps.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, false);
    	  graphProps.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY, Color.BLUE);
    	  
    	  return graphProps;
    	  
      }
      
      private TextProperties getDefaultTextProperties() {
    	  TextProperties textProps = new TextProperties();
    	  textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    	  textProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
    			  new Font(Font.SANS_SERIF , Font.PLAIN, 18 ));

    	  
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
    	  sc.addCodeLine(translator.translateMessage("intro_l5"), "line4", 0, null);
    	  sc.addCodeLine(translator.translateMessage("intro_l6"), "line5", 0, null);

    	  return sc;
      }
      

      
      private SourceCode  getBasicModelsSourceCodes(Translator translator, SourceCodeProperties scProps, Text ref) {
    	  
    	  SourceCode basic_models = lang.newSourceCode(new Offset(0, 10, ref, "SW"), "basic_models", null, scProps);
    	  basic_models.addCodeLine(translator.translateMessage("basic_models_l0"), "basic_models_l0", 0, null);
    	  basic_models.addCodeLine(translator.translateMessage("basic_models_l1"), "basic_models_l1", 0, null);
    	  
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
      	  
    	  SourceCode bm_convergent = lang.newSourceCode(new Offset(-30, 20, ref, "SW"), "bm_convergent", null, scProps);
    	  bm_convergent.addCodeLine(translator.translateMessage("bm_convergent_l0"), "bm_convergent_l0", 0, null);
    	  bm_convergent.addCodeLine(translator.translateMessage("bm_convergent_l1"), "bm_convergent_l1", 0, null);
    	  bm_convergent.addCodeLine(translator.translateMessage("bm_convergent_l2"), "bm_convergent_l2", 0, null);
    	  bm_convergent.addCodeLine(translator.translateMessage("bm_convergent_l3"), "bm_convergent_l3", 0, null);
    	  
    	  return bm_convergent;
      }
      
      private SourceCode getSourceCode(Translator translator, Text ref) {
    	  
    	  //set properties for the source code
    	  SourceCodeProperties scProps = getDefaultSourceCodeProperties();
    	  
    	  SourceCode sc = lang.newSourceCode(new Offset(0, 250, ref, "SW"), "sourceCode",null, scProps);
    	  //add code lines
    	  sc.addCodeLine(translator.translateMessage("algo_l0"), "algo_l0", 0,null);
    	  sc.addCodeLine(translator.translateMessage("algo_l1"), "algo_l1", 0,null);
    	  sc.addCodeLine(translator.translateMessage("algo_l2"), "algo_l2", 1,null);
    	  sc.addCodeLine(translator.translateMessage("algo_l3"), "algo_l3", 2,null);
    	  sc.addCodeLine(translator.translateMessage("algo_l4"), "algo_l4", 2,null);
    	  sc.addCodeLine(translator.translateMessage("algo_l5"), "algo_l5", 1,null);
    	  sc.addCodeLine(translator.translateMessage("algo_l6"), "algo_l6", 2,null);
    	  sc.addCodeLine(translator.translateMessage("algo_l7"), "algo_l7", 0,null);
    	  
    	  return sc;
    	  
      }
      
      private SourceCode getOutroSourceCode(Translator translator, Text ref) {
    	  
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
    	  
    	  
    	  
    	  //Node[] nodes = new Node[numNodes];
    	  //for(int i = 0 ; i < nodes.length; i++) {
    	  //	  nodes[i] = new Coordinates(edges[i].getFirst(), edges[i].getSecond());
    	  //}
    	  
    	  Graph graph = lang.newGraph(id, graphAdjacencyMatrix, coord, nodeLabels, null, graphProps);
    	  
    	  return graph;
      }
      
      private FillInBlanksQuestionModel createQuestionModel(Language lang,String id, String question, String answer, int points, String feedback) {
    	  FillInBlanksQuestionModel qModel = new FillInBlanksQuestionModel(id);
    	  qModel.setPrompt(question);
    	  qModel.addAnswer(answer,points,feedback);
    	  
    	  lang.addFIBQuestion(qModel);
    	  
    	  return qModel;
      }
      

      
      /**
       * This Method goes threw the graph and calculates the graph model. Meanwhile it 
       * highlighted the source code steps and executes two questions
       * @param graph the graph to evaluate
       * @param sc the sourceCode object of the source code
       * @param lang language object
       * @param result_txt the text object, where the final model will be saved
       * @param translator translator object to translate the strings 
       */
      private void evaluateGraph(Graph graph, SourceCode sc, Language lang, Text result_txt, Translator translator) {
    	  
    	  boolean q1 = true;
    	  boolean q2 = false;

    	  int[][] adjacencyMatrix = graph.getAdjacencyMatrix();
    	  //create a stringbuilder to create the string for the model 
    	  String result = "p(" + getNodeLabelsAsString(graph) + ")" + " = ";
    	  StringBuilder sb = new StringBuilder();
    	  sb.append(result);
    	  sc.highlight(0);
    	  result_txt.setText(sb.toString(), null, null);
    	  //go threw the matrix and check if a node points to another node 
    	  for (int i = 0 ; i < adjacencyMatrix.length ; i++) {
    		  //create a substring so save the interim result
    		  StringBuilder sub_sb = new StringBuilder();
    		  graph.highlightNode(i, null, null);
    		  //get the nodes, which are pointing at the current node 
    		  ArrayList<Integer> nodes = getDependencies(adjacencyMatrix, i);
    		  sub_sb.append("p(").append(graph.getNodeLabel(i));
    		  //check if nodes exist, that point at the node
    		  if(nodes.size() > 0) {
    			  lang.nextStep();
    			  sc.unhighlight(0);
    			  sc.highlight(1);
    			  sc.highlight(2);
    			  highlightEdges(graph, i,nodes);
    			  
    			  
    			  lang.nextStep();
    			  sc.unhighlight(1);
    			  sc.unhighlight(2);
    			  sc.highlight(3);
    			  sub_sb.append("|");
    			  //set current state of the model 
    			  result_txt.setText(sb.toString(), null, null);
    			  //add all nodes to the string, which point at the current node
    			  for(int p = 0; p < nodes.size(); p++ ) {
    				  sub_sb.append(graph.getNodeLabel(nodes.get(p)));
    				  if(p < nodes.size() - 1) {
    					  sub_sb.append(",");
    				  }
    			  }
    			  sub_sb.append(")");
    			  //check if the question is activated
    			  if(q2) {
    				  createQuestionModel(lang, "q2", translator.translateMessage("q2"), sub_sb.toString(), 1, translator.translateMessage("at"));
    				  //deactivate the question 
    				  q2 = false;
    			  }
    			  //check if the question is activated 
    			  if(q1) {
    				  MultipleChoiceQuestionModel mcModel = new MultipleChoiceQuestionModel("q1");
    				  mcModel.setPrompt(translator.translateMessage("q1"));
    				  //check if we have a chain or a convergent model and set the answers
    				  if(nodes.size() > 1) {
    					  mcModel.addAnswer(translator.translateMessage("a1"), 0, translator.translateMessage("af"));
    					  mcModel.addAnswer(translator.translateMessage("a2"), 1, translator.translateMessage("at"));
    				  }
    				  else {
    					  mcModel.addAnswer(translator.translateMessage("a1"), 1, translator.translateMessage("at"));
    					  mcModel.addAnswer(translator.translateMessage("a2"), 0, translator.translateMessage("af"));
    				  }
    				  lang.addMCQuestion(mcModel);
    				  //deactivate the question an active the next question
    				  q1 = false;
    				  q2 = true;
    			  }

    			  
    			  lang.nextStep();
    			  sc.unhighlight(3);
    			  sc.highlight(4);
    			  sb.append(sub_sb.toString());
    			  result_txt.setText(sb.toString(), null, null);
    		  }
    		  //non nodes exist, that point at the current node
    		  else {
    			  lang.nextStep();
    			  sc.unhighlight(0);
    			  sc.highlight(1);
    			  sc.highlight(5);
    			  
    			  lang.nextStep();
    			  sc.unhighlight(1);
    			  sc.unhighlight(5);
    			  sc.highlight(6);
    			  sub_sb.append(")");
    			  sb.append(sub_sb.toString());
    			  result_txt.setText(sb.toString(), null, null);
    			  
    		  }
    		  //reset the highlights and start at the beginning of the algorithm
    		  lang.nextStep();
    		  sc.unhighlight(4);
    		  sc.unhighlight(6);
    		  sc.highlight(0);
    		  graph.unhighlightNode(i, null, null);
    		  unhighlightEdges(graph, i, nodes);
    	  }

    	  sc.unhighlight(0);
    	  sc.highlight(7);
      }
      
      private ArrayList<Integer> getDependencies(int[][] adjacencyMatrix, int node) {
    	  
    	  ArrayList<Integer> list = new ArrayList<Integer>();
    	  
    	  for (int j = 0; j < adjacencyMatrix.length ; j++){
    		  if(adjacencyMatrix[j][node] == 1) {
    			  list.add(j);
    		  }
    	  }
    	  
    	  return list;
      }
      
      private void highlightEdges(Graph graph, int endNode, ArrayList<Integer> startNodes) {
    	  for(Integer startNode:startNodes) {
    		  graph.highlightEdge(startNode, endNode, null, null);
    	  }
      }
      
      private void unhighlightEdges(Graph graph, int endNode, ArrayList<Integer> startNodes) {
    	  for(Integer startNode:startNodes) {
    		  graph.unhighlightEdge(startNode, endNode, null, null);
    	  }
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
      
     
    
    public static void main(String[] args) {
		Generator generator = new DirectedGraphicalModel(); // Generator erzeugen
		Animal.startGeneratorWindow(generator); // Animal mit Generator starten
	}

}