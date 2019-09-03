/*
 * PageRank.java
 * Jan Ulrich Schmitt, Dennis Juckwer, 2015 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.graph;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import interactionsupport.models.AnswerModel;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.MultipleSelectionQuestionModel;
import interactionsupport.models.QuestionGroupModel;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import algoanim.primitives.Circle;
import algoanim.primitives.Graph;
import algoanim.primitives.Primitive;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.GraphGenerator;
import algoanim.primitives.generators.Language;
import algoanim.properties.*;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.Timing;



import algoanim.variables.Variable;
import algoanim.variables.VariableTypes;

import java.util.Hashtable;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalCircleGenerator;
import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.AnimalStringMatrixGenerator;


import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.TwoValueView;







///*
import java.util.Locale;
import java.awt.Color;

import algoanim.properties.SourceCodeProperties;
import animal.variables.VariableRoles;
//*/

public class PageRank implements Generator {
    private Language lang;
    private Graph g;
    Circle graphCircles[];
    Text graphText[];
    
    private int adjacencymatrix[][];
    private int numberOfOutgoingEdges[];
    private ArrayList<float[]> results;
    private float initValue;
    private float difference=100.0f;
    private int iterations = 0;
    private double dampingFactor;
    private double breakValue;
    PageRankGraph p;
    
    
    private Color color_for_lowest_PRValue;
    private SourceCodeProperties sourceCode;
    private Color nodehighlightcolor;
    private Color color_of_headertext;
    private Color color_for_highest_PRValue;
    private Color color_of_edges;
    private Color color_of_nodetext;
    private Color edgehighlightcolor;
    private Color color_of_dangling_nodes;
    
    private String qgName01 = "NextValueQuestion";
    private String qgName02 = "DanglingNodeQuestions";
    
    private String dampingFactorName = "dampingFactor";
    private String breakValueName = "breakValue";
    private String currentIterationChangeName = "currentIterationChange";
    private String currentNodeName = "currentNode";
    private String currentPredecessorName = "currentPredecessor";
    private String currentDanglingNodeName = "currentDanglingNode";
    private String tempValueName = "tempValue";
    private String numberOfNodesName = "numberOfNodes";
    private float currentResults[];
    
    private String MOST_RECENT_HOLDER = animal.variables.Variable.getRoleString(VariableRoles.MOST_RECENT_HOLDER);
    private String FIXED_VALUE = animal.variables.Variable.getRoleString(VariableRoles.FIXED_VALUE);
    private String STEPPER = animal.variables.Variable.getRoleString(VariableRoles.STEPPER);
    private String TEMPORARY = animal.variables.Variable.getRoleString(VariableRoles.TEMPORARY);

    
    private Variables vars;
    
    private int nqvId = 0;
    
    private boolean askQuestions = true;
    
    public PageRank(){

    }

    public void init(){
        lang = new AnimalScript("PageRank", "Jan Ulrich Schmitt, Dennis Juckwer", 800, 600);
        lang.setStepMode(true);
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
        vars = lang.newVariables();
        
    }
    
    
    // Adjazenmatrix +  Anzahl ausgehender Kanten von Knoten setzen
    private void initalValues(int [][] adjacencymatrix){
    	    	
        results = new ArrayList<float[]>();
        iterations = 1;
    	difference = 100;
    	this.adjacencymatrix = new int [adjacencymatrix.length][adjacencymatrix.length];
    	numberOfOutgoingEdges = new int[adjacencymatrix.length];
    	initValue = 1.0f / (float)g.getSize();
    	results.add(new float[adjacencymatrix.length]);
    	for(int to = 0; to < adjacencymatrix.length; to++){
    		float[] temparray = (float[])results.get(0);
    		temparray[to] = initValue;
    		results.set(0, temparray);
    		for(int from = 0; from < adjacencymatrix.length; from++){
    			if(adjacencymatrix[from][to] != 0){
    				this.adjacencymatrix[from][to] = 1;
    				numberOfOutgoingEdges[from] +=1;
    			}
    		}
    	}
    }
    

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {

        color_for_lowest_PRValue = (Color)primitives.get("color_for_lowest_PRValue");
        sourceCode = (SourceCodeProperties)props.getPropertiesByName("sourceCode");
        nodehighlightcolor = (Color)primitives.get("nodehighlightcolor");
        color_of_headertext = (Color)primitives.get("color_of_headertext");
        color_for_highest_PRValue = (Color)primitives.get("color_for_highest_PRValue");
        color_of_edges = (Color)primitives.get("color_of_edges");
        color_of_nodetext = (Color)primitives.get("color_of_nodetext");
        edgehighlightcolor = (Color)primitives.get("edgehighlightcolor");
        color_of_dangling_nodes = (Color)primitives.get("color_of_dangling_nodes");
        dampingFactor = (double)primitives.get("dampingFactor");
        breakValue = (double)primitives.get("breakValue");
        int maxRepeatQuestions = (int)primitives.get("maxNumberOfCorrectAnsweredQuestions");
         
        
        setupQuestions(maxRepeatQuestions);
        //Überschrift einfügen    
    	Text headertext = setHeader();
    	//Infotext zu Beginn einfügen
    	SourceCode informationText = setInformationText(headertext, 0, 50);
    	lang.nextStep("Einleitung");
    	informationText.hide();
    	
    	// Test auf unzulässigen Wert des Dämpfungsfaktors und ggf. auf default-Wert setzen
    	if(dampingFactor > 1.0f){
    		dampingFactor = 0.85f;
    		showWarningMessageForDamp(headertext, 0, 100);
    	}
    	
    	g = (Graph)primitives.get("graph");
    	setupVars(g);
    	
    	vars.set(dampingFactorName, String.valueOf(dampingFactor));
    	vars.set(breakValueName, String.valueOf(breakValue));
    	
    	

    	initalValues(g.getAdjacencyMatrix());
    	//PageRankGraph (eigene Visualiserung) wird erzeugt
    	p = setupGraph(nodehighlightcolor, color_of_edges,color_of_nodetext);
    	
    
    	for(int i = 0; i < g.getSize(); i++)
    	{
    		vars.set("Node"+g.getNodeLabel(i), String.valueOf(initValue));
    	}
    	
    	p.setAllDangingEdgeBaseColor(color_of_dangling_nodes);
    	
    	int srcXPos = p.getMaxX();
    	if(srcXPos < 600)
    		srcXPos = 600;
    	
        SourceCode src = setSourceCode(sourceCode, srcXPos, ((Coordinates)headertext.getUpperLeft()).getY());


        // Matritzen und zugehörige Texte zur Darstellung von Zwischenschritten werden erzeugt
        Text lastText = setCounter(src, 0 , 20, "Die PageRank-Werte nach der Initialisierung:");
        StringMatrix smat = setupMatrix(lastText, 0,20, initValue);
        Text currentText = setCounter(smat, 0, 20,  "Die PageRank-Werte von Iteration 1:");
        StringMatrix actMat = setupMatrix(currentText, 0, 20, 0.0f);
        
        TwoValueCounter counter = lang.newCounter(smat);
        CounterProperties cp = new CounterProperties();
        cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);
        TwoValueView view = lang.newCounterView(counter,
       		new Offset(0, 20, actMat, "SW"), cp, true, true);

        // Text zur Darstellung von Formeln wird erzeugt
        Text formulaV = setCounter(50, p.getMaxY() + 5, "");
        Text formulaC = setCounter(formulaV, 0, 20, "");
        Text dnSelfReferenceText = setCounter(formulaC, 0, 20, "");
        dnSelfReferenceText.hide();
        
        startDanglingNodeQuestion(p, g);
        lang.nextStep("Aufruf und Initialisierung");
        src.unhighlight(0);
        
        Random rg = new Random();

        int nodeToQuestion = rg.nextInt(adjacencymatrix.length-1);
        nodeToQuestion++;
        
        
        vars.declare("double", currentIterationChangeName,String.valueOf(difference),MOST_RECENT_HOLDER);
        
		while(difference > breakValue){ // Counter fuer Iterationen
			formulaV.setText("Manhattan-Distanz zwischen letzter und vorletzter Iteration: " + new DecimalFormat("#.#####").format(difference)+".", null, null);
			formulaC.setText("Der Algorithmus stoppt, wenn die Distanz kleiner als "+ new DecimalFormat("#.#####").format(breakValue)+" wird.", null, null);
			iterations += 1;
			src.highlight(1);
			int chapterIntCorrect = iterations - 1;
			String outputChapter = chapterIntCorrect + ". Iteration";
			lang.nextStep(outputChapter);
			src.unhighlight(1);
			currentResults = new float[adjacencymatrix.length];
			src.highlight(2);
			lang.nextStep();
			src.unhighlight(2);
			formulaV.setText("", null, null);
			
			
			vars.declare("string", currentNodeName,"",STEPPER);

			// for all nodes in G do:
			for(int to = 0; to < adjacencymatrix.length; to++){
				
				
				vars.set(currentNodeName, "Node"+g.getNodeLabel(to));
				vars.declare("double", tempValueName+g.getNodeLabel(to), "0", TEMPORARY);
				
				
				if(nodeToQuestion == to)
				{
					float nextValueResult = getNextValue(to, p);
					startNextValueQuestion(nextValueResult, to, g);
					nodeToQuestion = rg.nextInt(adjacencymatrix.length);
				}
				
				//PageRank of current node n <- (1-d)/|G| 
				String fV = "PR(" + g.getNodeLabel(to) +") = (1-d)/|G| "; 
				formulaV.setText(fV, null, null);
				currentResults[to] = (float) ((1.0f - dampingFactor) / adjacencymatrix.length);
				vars.set(tempValueName+g.getNodeLabel(to),String.valueOf(currentResults[to]));
				String fC = "PR(" + g.getNodeLabel(to) + ") = " + new DecimalFormat("#.##").format((1.0 - dampingFactor) )+ " /" + g.getSize() + " = " + new DecimalFormat("#.#####").format(currentResults[to]);
				formulaC.setText(fC, null, null);
				p.highlightNode(to);
				actMat.highlightCell(0, to, null, null);
				src.highlight(3);
				
				actMat.put(1, to, new DecimalFormat("#.#####").format(currentResults[to]), null, null);
				p.setNodeSize(to, this.calcNodeSize(currentResults[to], p.getmaxRadius(), p.getminRadius(), g));
				lang.nextStep();
				
				src.unhighlight(3);
				src.highlight(4);
				lang.nextStep();
				src.unhighlight(4);
				float[] predecValues = (float[]) results.get(results.size()-1);
				vars.declare("string", currentPredecessorName,"", STEPPER);
				// for each predecessor p of current node n do
				for(int from = 0; from < adjacencymatrix.length; from++){
					if(adjacencymatrix[from][to] == 1){
						// PR of n <- PR of n ü d * ((PR of p in the last step/ outgoing edges from p))
						vars.set(currentPredecessorName, g.getNodeLabel(from));
						fV = "PR(" + g.getNodeLabel(to) + ") = PR(" +  g.getNodeLabel(to) + ") + d * PR(" + g.getNodeLabel(from) + ")/"  + "outgoing edges from " + g.getNodeLabel(from) + " ";
						smat.getElement(0, 0); //// Hilfsabfrage fuer Counter
						formulaV.setText(fV, null, null);
						float tempResult = currentResults[to];
						currentResults[to] = (float) (currentResults[to] + dampingFactor* (predecValues[from]/numberOfOutgoingEdges[from]));
						vars.set(tempValueName+g.getNodeLabel(to),String.valueOf(currentResults[to]));
						fC = "PR(" + g.getNodeLabel(to) + ") = " + new DecimalFormat("#.#####").format(tempResult)  + " + " + new DecimalFormat("#.##").format(dampingFactor )   + " * " + new DecimalFormat("#.#####").format(results.get(results.size()-1)[from]) + "/" + numberOfOutgoingEdges[from] + " = " + new DecimalFormat("#.#####").format(currentResults[to]) ;
						formulaC.setText(fC, null, null);
						src.highlight(5);
						p.highlightEdge(from, to, null, null);
						p.setNodeSize(to, this.calcNodeSize(currentResults[to], p.getmaxRadius(), p.getminRadius(), g));
						smat.highlightElem(1, from, null, null);
						actMat.put(1, to, new DecimalFormat("#.#####").format(currentResults[to]), null, null);
						lang.nextStep();
						smat.unhighlightElem(1, from, null, null);
						src.unhighlight(5);
						p.unhighlightEdge(from, to, null, null);
						
					}
					
				}
				vars.discard(currentPredecessorName);
				src.highlight(6);
				lang.nextStep();
				src.unhighlight(6);
	
				vars.declare("String", currentDanglingNodeName, "", STEPPER);
				// for each dangling node dn do:
				for(Integer dangNode : p.getAllDanglingNodeNrs())
				{
					if(dangNode == to)
					{
						dnSelfReferenceText.setText("Ein Dangling Node hat auch Einfluss auf sich selber!", null, null);
						dnSelfReferenceText.show();
					}else
					{
						dnSelfReferenceText.hide();
					}
					// PR of n <- Pr  of n + d * (1/|G|)
					vars.set(currentDanglingNodeName, g.getNodeLabel(dangNode));
					src.highlight(7);
					smat.highlightElem(0, dangNode, null, null);
					fV = "PR( " + g.getNodeLabel(to)+ ") = PR(" + g.getNodeLabel(to) + ") + d * 1/|G| ";
					formulaV.setText(fV, null, null);
					float tempResult = currentResults[to];
					vars.set(tempValueName+g.getNodeLabel(to),String.valueOf(currentResults[to]));
					currentResults[to] = (float) (currentResults[to] + dampingFactor * (predecValues[dangNode]/adjacencymatrix.length));
					vars.set(tempValueName+g.getNodeLabel(to),String.valueOf(currentResults[to]));
					fC = "PR(" + g.getNodeLabel(to) + ") = " + new DecimalFormat("#.#####").format(tempResult) + " + " + dampingFactor + " * 1/" + g.getSize() + " = " + new DecimalFormat("#.#####").format(currentResults[to]);
					formulaC.setText(fC, null, null);
					p.setNodeHighlightColor(dangNode, color_of_dangling_nodes);
					p.highlightNode(dangNode);
					p.showEdge(dangNode, to);
					p.hideEdge(to, dangNode);
					p.setNodeSize(to, this.calcNodeSize(currentResults[to], p.getmaxRadius(), p.getminRadius(), g));
					actMat.put(1, to, new DecimalFormat("#.#####").format(currentResults[to]), null, null);
					p.setNodeFillColor(to, colorLin(color_for_lowest_PRValue, color_for_highest_PRValue, (float)(1.0f - dampingFactor)/g.getSize(), (float)1, currentResults[to]));
					lang.nextStep();
					src.unhighlight(7);
					p.unhighlightNode(dangNode);
					p.setNodeHighlightColor(dangNode, nodehighlightcolor);
					p.showEdge(to, dangNode);
					p.hideEdge(dangNode, to);
					smat.unhighlightElem(0, dangNode, null, null);
				}
				dnSelfReferenceText.hide();
				vars.discard(currentDanglingNodeName);
				
				p.unhighlightNode(to);
				actMat.unhighlightCell(0, to, null, null);
			}
			
			//Update der Werte in den Infomatritzen
			for(int i = 0; i< adjacencymatrix.length; i++){
				smat.put(1, i, new DecimalFormat("#.#####").format(currentResults[i]), null, null);
				actMat.put(1,i,"0.0",null,null);
				vars.set("Node"+g.getNodeLabel(i), String.valueOf(currentResults[i]));
				vars.discard(tempValueName+g.getNodeLabel(i));
			}
			results.add(currentResults);
			difference = getDifference((float[])results.get(results.size()-2), (float[])results.get(results.size()-1));
			vars.set(currentIterationChangeName, String.valueOf(difference));
			//iterations += 1;
			lastText.setText("Die Werte von Iteration " + (iterations -1) + ":" , null, null);
			currentText.setText("Die Werte von Iteration " + iterations + ":", null, null);
			
			vars.discard(currentNodeName);
		}
        

		formulaV.setText("Manhattan-Distanz zwischen letzter und vorletzter Iteration: " + new DecimalFormat("#.#####").format(difference)+".", null, null);
		formulaC.setText("Der Algorithmus stoppt, wenn die Distanz kleiner als "+ new DecimalFormat("#.#####").format(breakValue)+" wird.", null, null);
		src.highlight(1);
		currentText.hide();
        actMat.hide(); 
        SourceCode stopInformation = showStopInformation(smat, 0, 50);
		lang.nextStep("Abbruch des Algorithmus");
        
		src.hide();
		formulaC.hide();
        formulaV.hide();
		stopInformation.hide();
		SourceCode endText = showEndText(counter, smat, currentResults, headertext, 0, 50);
        p.hideAllDanglingEdges();
        p.hideGraph();
        view.hide();
        smat.hide();
        lastText.hide();
        
        lang.nextStep("Fazit");
        
		lang.finalizeGeneration();
		return lang.toString();
    }
    
    private float getNextValue(int node, PageRankGraph p)
    {
    		
		float[] predecValues = (float[]) results.get(results.size()-1);
		
		float result = 0.15f / adjacencymatrix.length;
		
		for(int from = 0; from < adjacencymatrix.length; from++){
			if(adjacencymatrix[from][node] == 1){
				result = (float) (result + 0.85f* (predecValues[from]/numberOfOutgoingEdges[from]));				
			}
		}


		for(Integer dangNode : p.getAllDanglingNodeNrs())
		{
			
			result = (float) (result + 0.85f * (predecValues[dangNode]/adjacencymatrix.length));		
		}
			
		return result;
		
    }
    



// Berechnung der Größe eines Knotens in Abhängigkeit von seinem PageRank
	private int calcNodeSize(float prValue, int max, int min, Graph g){
    	float minPRValue = (float)(1.0f - dampingFactor)/(float)(g.getAdjacencyMatrix().length);
    	float newSize = ((prValue-(float)minPRValue)/(float)(1.0f - minPRValue)) * ((float)(max - min)) + (float)min;
    	return (int) newSize;
    }
	
	private void setupVars(Graph g)
	{
		VariableTypes.FLOAT.toString();
		VariableRoles.UNKNOWN.toString();
		
		vars.declare("int", numberOfNodesName, String.valueOf(g.getSize()),FIXED_VALUE);
		vars.declare("double",dampingFactorName, "0",FIXED_VALUE);
		vars.declare("double",breakValueName, "0",FIXED_VALUE);
		
		for(int i = 0; i < g.getSize(); i++)
		{
			vars.declare("double","Node"+g.getNodeLabel(i), "0",MOST_RECENT_HOLDER );
		}
	}
    
    private void setupQuestions(int maxNumberOfRepeats)
    {
    	
    	int onlyOnceQuestions = 1;
    	
    	if(maxNumberOfRepeats <= 0)
    	{
    		maxNumberOfRepeats = 0;
    		onlyOnceQuestions = 0;
    		askQuestions = false;
    	}else
    	{
    		askQuestions = true;
    	}
    		
    	
    	
    	
    	QuestionGroupModel questionGroup = new QuestionGroupModel(qgName01);
    	questionGroup.setNumberOfRepeats(maxNumberOfRepeats);
    	lang.addQuestionGroup(questionGroup);
    	
    	questionGroup = new QuestionGroupModel(qgName02);
    	questionGroup.setNumberOfRepeats(onlyOnceQuestions);
    	lang.addQuestionGroup(questionGroup);
    	
    }
    
    
    private void startNextValueQuestion(float nextValue,int node ,Graph g)
    {
    	if(!askQuestions)
    		return;
    
    	Random randomGenerator = new Random();
    	nqvId ++;
    	float roundedValue = nextValue * 1000;
    	roundedValue = Math.round(roundedValue);
    	roundedValue = roundedValue/ 1000;
    	
    	MultipleChoiceQuestionModel question = new MultipleChoiceQuestionModel("Nächter Wert " +nqvId);
    	question.setPrompt("Welchen Wert hat Knoten "+ g.getNodeLabel(node) + " am Ende der kompletten Iteration? (gerundet auf die dritte Nachkommastelle)");

        
        LinkedList<Float> values = new LinkedList<Float>();
        values.add(roundedValue);
        
        for(int i =0; i < 3; i++)
        {
        	float newValue;
        	int maxPercent = 100;
        	int minPercent = 10;
        	
        	do
        	{
        		float randomValue = randomGenerator.nextInt(maxPercent-minPercent);
        		randomValue += minPercent;
            	randomValue -= ((float)(maxPercent-minPercent))/2.f;
            	
            	randomValue = randomValue/100;
            	
            	
            	if(randomValue == 0)
            	{
            		randomValue += 0.01;
            	}
            	
            	newValue = roundedValue+ 1.0f * randomValue;
            	
            	
            	newValue = newValue * 1000;
            	newValue = Math.round(newValue);
            	newValue = newValue/ 1000;
            	
            	if(newValue >= 1)
            		newValue = 1.f;
            	
            	if(newValue <= 0)
            		newValue = 0.001f;
            	
        	}while(values.contains(newValue));
        	
        	values.add(newValue);       	
        }
        
        while(!values.isEmpty())
        {
        	int index = randomGenerator.nextInt(values.size());
        	float questionValue = values.remove(index);
        	        	
        	if(questionValue == roundedValue)
        	{
        		question.addAnswer(float2String(questionValue),5,"Richtig.");
        		
        	}else
        	{
        		question.addAnswer(float2String(questionValue),-5,"Falsch. Die richtige Antwort wäre "+ float2String(roundedValue)+".");
        	}
        }
        
        question.setGroupID(qgName01);
        
        lang.addMCQuestion(question);
         
    }
    
    
    private void startDanglingNodeQuestion(PageRankGraph prg, Graph g)
    {
    	if(!askQuestions)
    		return;
    	
    	MultipleSelectionQuestionModel question = new MultipleSelectionQuestionModel("Dangling Nodes");
    	question.setPrompt("Welche Knoten sind Dangling Nodes?");
    	question.setGroupID(qgName02);
    	
    	List<Integer> danglingNodes = prg.getAllDanglingNodeNrs();
    	StringBuilder danglingNodeString = new StringBuilder();
    	
    	int counter = 0;
    	for(int danglingNode : danglingNodes)
    	{
    		danglingNodeString.append(g.getNodeLabel(danglingNode));
    		counter++;
    		if(counter != danglingNodes.size())
    		{
    			danglingNodeString.append(", ");
    		}
    	}
    		
    	
    	for(int nodeId = 0; nodeId < g.getSize(); nodeId++)
    	{
    		String nodeLabel = g.getNodeLabel(nodeId);
    	
    		if(danglingNodes.contains(nodeId))
    		{
    			question.addAnswer(nodeLabel, 5, "Knoten "+ nodeLabel+" ist ein dangling node.\n");
    		}else
    		{
    			question.addAnswer(nodeLabel, -5, "Knoten "+ nodeLabel+" ist kein dangling node. Dangling nodes wären: "+danglingNodeString.toString()+".\n");
    		}
    	}
    	
    	if(danglingNodes.isEmpty())
    	{
    		question.addAnswer("Keiner", 5, "Es gibt keine Dangling Nodes in diesem Graphen.");
    	}else
    	{
    		question.addAnswer("Keiner",-5, "Es gibt Dangling Nodes in diesem Graphen. Dangling nodes wären: "+danglingNodeString.toString()+".\n");
    	}
    	
    	
    	
    	lang.addMSQuestion(question);
    	
    	
    }
    
    private String float2String(float value)
    {
    	String stringValue = String.valueOf(value);
    	return stringValue;
    }

    public String getName() {
        return "PageRank";
    }

    public String getAlgorithmName() {
        return "PageRank";
    }

    public String getAnimationAuthor() {
        return "Jan Ulrich Schmitt, Dennis Juckwer";
    }

    public String getDescription(){
        return "Der PageRank-Algorithmus ist ein Algorithmus zur Bewertung von Knoten in einem Netzwerk."
 +" Larry Page und Sergei Brin entwickelten ihn an der Stanford University zur Bewertung von"
 +" Webseiten im Rahmen ihrer mittlerweile weltweit bekannnten Suchmaschine Google. Das Bewertungsprinzip"
 +" sieht dabei vor, dass der Rank einer Seite umso größer ist, je mehr andere Seiten auf sie verweisen."
 +" Der Effekt wird dabei von dem Rank der auf diese Seite verweisenden Seiten verstärkt."
 +"\n"
 +"\nEine mögliche Interpretation des PageRanks liefert das sogenannte Random Surfer Modell. Im Rahmen dieses"
 +" Modells repräsentiert der PageRank eines Knotens bzw. einer Webseite (bei einer Normierung der Summe der PageRanks auf 1) die"
 +" Wahrscheinlichkeit, mit der sich ein sogenannter Zufallssurfer auf einer bestimmten Webseite befindet. Hierbei gilt, dass"
 +" der Zufallssurfer mit einer Wahrscheinlichkeit von d den Links auf der Webseite folgt, auf der er sich gerade befindet."
 +" Mit einer Wahrscheinlichkeit von 1-d ruft er manuell in seinem Browser eine der Webseiten auf. Diese Wahrscheinlichkeit d"
 +" wird im Allgemeinen als Dämpfungsfaktor bezeichnet und kann im Rahmen dieser Visualisierung konfiguriert werden."
 +"\n\nEbenfalls als besonders hervorzuheben sind die sogenannten Dangling Nodes. Bei Ihnen handelt es sich um Knoten bzw. Webseiten,"
 +" welche keine Verweise besitzen.";
    }

    public String getCodeExample(){
        return "PageRank (Graph G, dampingfactor d) \n"
      + "    while PageRankValues change signifficantly \n"
      + "            for all nodes in G do \n"
      + "                    PageRank of current node n - (1-d)/|G| \n"
      + "            for each predecessor p of current node n do \n"
      + "                    PR of n - PR of n + d*((PR of p in the last step)/outgoing edges from p) \n"
      + "            for each dangling node dn do// dangling nodes are nodes with no successors \n"
      + "                    PR of n - PR of n + d * (1/|G|)";
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
    
    /////////////////////////////////////Helper Functions///////////////////////////////
   
    // Farbe eines Knoten in Abhängigkeit vom PageRank berechnen
    private Color colorLin(Color startColor, Color endColor, float percent)
    {
    	int[] color1 = new int[3];
    	color1[0] = startColor.getRed();
    	color1[1] = startColor.getGreen();
    	color1[2] = startColor.getBlue();
    	int[]color2 = new int[3];
    	color2[0] = endColor.getRed();
    	color2[1] = endColor.getGreen();
    	color2[2] = endColor.getBlue();

    	for(int i = 0; i < color1.length; i++)
    	{    		
    		color1[i] = color1[i] +(int)((float)(color2[i]-color1[i]) * percent);
    		
    		if(color1[i] > 255) color1[i] = 255;
    		if(color1[i] < 0) color1[i] = 0;
    	}
    	
    	Color result = new Color(color1[0],color1[1],color1[2]);
    	
    	return result;   
    }
    
    private Color colorLin(Color startColor, Color endColor, float minValue, float maxValue, float value)
    {
    	if(minValue == maxValue)
    		return endColor;
    	
    	return colorLin(startColor, endColor,((value-minValue)/(maxValue-minValue)));
    }
    
    
    // Methode zur Erstellung der Überschrift
    private Text setHeader(){
    	TextProperties headerProps = new TextProperties();
    	headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF,Font.BOLD, 24));
    	headerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, color_of_headertext);
    	return lang.newText(new Coordinates(20,30), "Der PageRank-Algorithmus", "header", null, headerProps);
    }
    
    //Infotext zu Beginn
    private SourceCode setInformationText(Primitive currentPrimitive, int deltax, int deltay){
    	SourceCodeProperties infoProps = new SourceCodeProperties();
    	infoProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 20));
    	SourceCode infoText = lang.newSourceCode(new Offset(deltax, deltay, currentPrimitive, "SW"), "InfoText", null, infoProps);
    	infoText.addCodeLine("Der PageRank-Algorithmus ist ein Algorithmus zur Bewertung von Knoten in einem Netzwerk.", "Line0", 0, null);
    	infoText.addCodeLine("Larry Page und Sergei Brin entwickelten ihn an der Stanford University zur Bewertung von", "Line1", 0, null);
    	infoText.addCodeLine("Webseiten im Rahmen ihrer mittlerweile weltweit bekannnten Suchmaschine Google. Das Bewertungsprinzip", "Line2", 0, null);
    	infoText.addCodeLine("sieht dabei vor, dass der Rank einer Seite umso größer ist, je mehr andere Seiten auf sie verweisen.", "Line3", 0, null);
    	infoText.addCodeLine("Der Effekt wird dabei von dem Rank der auf diese Seite verweisenden Seiten verstärkt.", "Line4", 0, null);
    	infoText.addCodeLine("", "Line5", 0, null);
    	infoText.addCodeLine("Eine mögliche Interpretation des PageRanks liefert das sogenannte Random Surfer Modell. Im Rahmen dieses", "Line6", 0, null);
    	infoText.addCodeLine("Modells repräsentiert der PageRank eines Knotens bzw. einer Webseite (bei einer Normierung der Summe der PageRanks auf 1) die", "Line7", 0, null);
    	infoText.addCodeLine("Wahrscheinlichkeit mit der sich ein sogenannter Zufallssurfer auf einer bestimmten Webseite befindet. Hierbei gilt, dass", "Line8", 0, null);
    	infoText.addCodeLine("der Zufallssurfer mit einer Wahrscheinlichkeit von d den Links auf der Webseite folgt, auf der er sich gerade befindet.", "Line9", 0, null);
    	infoText.addCodeLine("Mit einer Wahrscheinlichkeit von 1-d ruft er manuell in seinem Browser eine der anderen Webseiten auf.", "Line10", 0, null);
    	infoText.addCodeLine("Dieser Wahrscheinlichkeit d wird im Allgemeinen als Dämpfungsfaktor bezeichnet.", "Line11", 0, null);
    	infoText.addCodeLine("", "Line12", 0, null);
    	infoText.addCodeLine("Ebenfalls als besonders hervorzugeben sind die sogenannten Dangling Nodes. Bei Ihnen handelt es sich um Knoten bzw. ", "Line13", 0, null);
    	infoText.addCodeLine("Webseiten, welche keine Verweise besitzen.", "Line14", 0, null);
    	return infoText;
    }
    
    private Text setCounter(Primitive currentPrimitive, int deltax, int deltay, String Text){

    	TextProperties counterProps = new TextProperties();
    	counterProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 16));
    	return lang.newText(new Offset(deltax, deltay, currentPrimitive, "SW"), Text, "Counter", null, counterProps);
    	
    }
    
    private Text setCounter(int x, int y, String Text){

    	TextProperties counterProps = new TextProperties();
    	counterProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 16));
    	return lang.newText(new Coordinates(x, y), Text, "Counter", null, counterProps);
    	
    }
    
    
    // Erstellung des Sourcecodes
    private SourceCode setSourceCode(SourceCodeProperties sProb, int x, int y){
        
        SourceCode src = lang.newSourceCode(new Coordinates(x + 70 , y + 20), "SourceCode", null, sProb);
        src.addCodeLine("1. PageRank (Graph G, dampingfactor d)", "Code0", 0, null);
        src.addCodeLine("2. while PageRankValues change signifficantly", "Code1", 0, null);
        src.addCodeLine("3.     for all nodes in G do", "Code2", 0, null);
        src.addCodeLine("4.         PageRank of current node n <- (1-d)/|G|", "Code3", 0, null);
        src.addCodeLine("5.         for each predecessor p of current node n do", "Code4", 0, null);
        src.addCodeLine("6.             PR of n <- PR of n + d*((PR of p in the last step)/outgoing edges from p)", "Code5", 0, null);
        src.addCodeLine("7.         for each dangling node dn do", "Code6", 0, null);
        src.addCodeLine("8.             PR of n <- PR of n + d * (1/|G|)", "Code7", 0, null);
        src.highlight(0);
        return src;
    }
    
    // Erzeugung des Graphen in unserer Datenstruktur
    private PageRankGraph setupGraph(Color nodehighlightcolor,Color color_of_edges,Color color_of_nodetext){
    	GraphProperties gProps = new GraphProperties("graphprop");
        gProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        gProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, nodehighlightcolor);
        gProps.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, true);
        gProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY,0);
        g = lang.addGraph(g, null, gProps);
        g.hide();
        PageRankGraph p = new PageRankGraph(g,lang);
        p.setAllNodeHighlightColor(nodehighlightcolor);
        p.setAllEdgesBaseColor(color_of_edges);
        p.setAllTextColor(color_of_nodetext);
        p.setAllEdgesHighlightColor(edgehighlightcolor);
        return  p;
    }
    
    private StringMatrix setupMatrix(Primitive currentPrimitive,int deltax, int deltay, float initValue){
    	
    	AnimalStringMatrixGenerator matrixGenerator = new AnimalStringMatrixGenerator(
				(AnimalScript) lang);
    	
    	String[][] strValues = new String[2][g.getSize()];

    	for(int i = 0; i < g.getSize(); i++){
    		strValues[0][i] = g.getNodeLabel(i);
    		strValues[1][i] = "" + initValue;
    	}
    	
        MatrixProperties matProp = new MatrixProperties();
        matProp.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
        matProp.set(AnimationPropertiesKeys.CELL_HEIGHT_PROPERTY, 30);
        matProp.set(AnimationPropertiesKeys.CELL_WIDTH_PROPERTY, 80);
        StringMatrix smat = new StringMatrix(matrixGenerator,
				new Offset(deltax, deltay, currentPrimitive, "SW"), strValues, "Matrix", null,
				matProp);

    	return smat;
    }
    
    // Unterschied zwischen zwei Iterationen (breakValue)
    private float getDifference(float[] lastValues, float[] actualValues){
    	float difference = 0.0f;
    	for(int i = 0; i < lastValues.length; i++){
    		difference += Math.abs((Math.abs(lastValues[i]) - Math.abs(actualValues[i])));
    	}
    	return difference;
    }
    
    //Fehlermeldung bei unzulässigen Dämpfungsfaktor
    private void showWarningMessageForDamp(Primitive currentPrimitive, int deltax, int deltay) {
    	SourceCodeProperties infoProps = new SourceCodeProperties();
    	infoProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 14));
    	infoProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
    	SourceCode warningMessage = lang.newSourceCode(new Offset(deltax, deltay, currentPrimitive, "SW"), "InfoText", null, infoProps);
    	warningMessage.addCodeLine("Der von Ihnen eingegebene Wert des Dämpfungsfaktors liegt nicht", "Line0", 0, null);
    	warningMessage.addCodeLine("zwischen 0 und 1! Er wurde daher auf den üblichen Wert von 0.85" , "Line1", 0, null);
    	warningMessage.addCodeLine("gesetzt!", "Line3", 0, null);
    	lang.nextStep();
    	warningMessage.hide();
		
	}
    
	// Informationen zu Ende (Anzahl Zugriffe etc..)
    private SourceCode showEndText(TwoValueCounter counter, StringMatrix smat, float[] currentResults, Primitive currentPrimitive, int deltax, int deltay) {
    	int actualCount = iterations - 1;
		SourceCodeProperties infoProps = new SourceCodeProperties();
    	infoProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 14));
    	SourceCode endText = lang.newSourceCode(new Offset(deltax, deltay, currentPrimitive, "SW"), "InfoText", null, infoProps);
    	endText.addCodeLine("Informationen zu dem zuvor angezeigten Ablauf des Algorithmus:", "Line0", 0, null);
    	endText.addCodeLine("", "Line1", 0, null);
    	endText.addCodeLine("Anzahl Iterationen: " + actualCount, "Line2", 0, null);
    	endText.addCodeLine("Anzahl Schreibzugriffe: " + counter.getAssigments(), "Line3", 0, null);
    	endText.addCodeLine("Anzahl Lesezugriffe: " + counter.getAccess(), "Line4", 0, null);
    	int tempPosition = 0;
    	
    	ArrayList<Integer> maxposition = new ArrayList<Integer>();
    	
    	
    	
    	
    	for(int i = 0; i < adjacencymatrix.length; i++){
   		double maxValue = (double)Integer.MIN_VALUE;
    		for(int inneri = 0; inneri < adjacencymatrix.length; inneri++){
    			if(currentResults[inneri] >= maxValue && maxposition.contains(inneri)==false){
      				maxValue = currentResults[inneri];
    				tempPosition = inneri;
    				
    			}
    		}
    		maxposition.add(tempPosition);
    		
   	}
    	
    	for(int i =0; i < adjacencymatrix.length;i++){
    		endText.addCodeLine((i+1) + ". PR(" + g.getNodeLabel(maxposition.get(i)) + ") = " + smat.getElement(1, (Integer) maxposition.get(i)), "Line" + (i + 3), 0, null);	
    	}
    	
    	boolean helpbool = true;
    	String bestNodes = "";
    	int i = 0;
    	
    	
    	while(helpbool ){
    		bestNodes = bestNodes + g.getNodeLabel(maxposition.get(i)) + " ";
    		if((i < maxposition.size() && maxposition.get(i)==maxposition.get(0)&& i !=0)||(i < maxposition.size() - 1 && (maxposition.get(i) == maxposition.get(i+1)))){
    			helpbool = true;
    		}else{
    			helpbool = false;
    		}
    		i++;
    	}
    	endText.addCodeLine("", "Liney", 0, null);
    	endText.addCodeLine("Der bzw. die Knoten mit dem höchsten PageRank: " + bestNodes, "Linex", 0, null);
    	endText.addCodeLine("", "Line14", 0, null);
    	endText.addCodeLine("Knoten mit dem höchsten PageRank haben die höchste Wahrscheinlichkeit von einem Surfer", "Line15", 0, null);
    	endText.addCodeLine("im Netz besucht zu werden und würden bei einer Suchmaschine an oberster Stelle erscheinen.", "Line16", 0, null);
    	
    	return endText;
	}
	
	// Info im Falle von Konvergenz des Algorithmus
    private SourceCode showStopInformation(Primitive currentPrimitive, int deltax, int deltay){
    	int actualCount = iterations - 1;
		SourceCodeProperties infoProps = new SourceCodeProperties();
    	infoProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 14));
    	infoProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
    	SourceCode stopInformation = lang.newSourceCode(new Offset(deltax, deltay, currentPrimitive, "SW"), "InfoText", null, infoProps);
    	stopInformation.addCodeLine("Der Algorithmus endet an dieser Stelle nach", "Line0", 0, null);
    	stopInformation.addCodeLine(actualCount + " Iterationen, da die Manhattan-Distanz kleiner als der Abbruchwert ist!", "Line1", 0, null);
    	return stopInformation;
	}
    
    
    
    

}