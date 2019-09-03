/*
 * Netzplan.java
 * Jan Ulrich Schmitt & Dennis Juckwer, 2015 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleSelectionQuestionModel;
import interactionsupport.models.QuestionGroupModel;

import java.awt.Color;
import java.awt.Font;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import algoanim.primitives.Graph;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CounterProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

import java.util.Hashtable;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.AnimalStringMatrixGenerator;
import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.TwoValueView;
import animal.variables.VariableRoles;


public class Netzplan implements Generator {
    private Language lang;
    private Graph graph;
    NetzplanGraph n;
    SourceCode src1;
    SourceCode src2;
    private SourceCodeProperties sourceCodeStyle;
    StringMatrix smat;
    LinkedList<Integer> critcalPathNodes;
    
    Random rg = new Random();
    
    
    String qg01 = "firstDirectionQuestions";
    String qg02 = "secondDirectionQuestions";
    String qg03 = "criticalPathQuestion";
    String qg04 = "delayQuestion";
    
    private String MOST_RECENT_HOLDER = animal.variables.Variable.getRoleString(VariableRoles.MOST_RECENT_HOLDER);
    private String FIXED_VALUE = animal.variables.Variable.getRoleString(VariableRoles.FIXED_VALUE);
    private String STEPPER = animal.variables.Variable.getRoleString(VariableRoles.STEPPER);
    private String TEMPORARY = animal.variables.Variable.getRoleString(VariableRoles.TEMPORARY);
    private String MOST_WANTED_HOLDER = animal.variables.Variable.getRoleString(VariableRoles.MOST_WANTED_HOLDER);
    private String WALKER = animal.variables.Variable.getRoleString(VariableRoles.WALKER);
    
    private Variables vars;
    
    String nodePreName = "process";
	String ptPreName = "processTime";
	String estPreName = "earliestStartTime";
	String eetPreName = "earliestEndTime";
	String lstPreName = "latestStartTime";
	String letPreName = "latestEndTime";
	String currentNodeName = "currentNode";
	String currentPredecessorName = "currentPredecessor";
	String currentSuccessorName = "currentSuccessor";
	
	int fdQuestionCounter = 0;
	int sdQuestionCounter = 0;
	
	private boolean askQuestions = true;

    public void init(){
        lang = new AnimalScript("Netzplantechnik", "Jan Ulrich Schmitt & Dennis Juckwer", 800, 600);        
        lang.setStepMode(true);
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
        vars = lang.newVariables();
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {

    	// set up generation
        graph = (Graph)primitives.get("graph");
        sourceCodeStyle = (SourceCodeProperties) props.getPropertiesByName("SourcecodeStyle");
        Color edgeColor = (Color)primitives.get("EdgeColor");
        Color edgeHighlightColor = (Color)primitives.get("EdgeHighlightColor");
        Color headerColor = (Color)primitives.get("headerColor");
        MatrixProperties matrixProperties = (MatrixProperties) props.getPropertiesByName("NodeStyle");
        int maxRepeatQuestions = (int)primitives.get("maxNumberOfCorrectAnsweredQuestions");
        
        setupQuestions(maxRepeatQuestions);
        
        // set and shows the beginning page with description
        Text headerText = setHeader(headerColor);
        setInformationText(new Offset(0,20,headerText,"SW"));
        
        // set up the netzplan graph
        n = new NetzplanGraph((AnimalScript)lang, graph,matrixProperties);
        n.setAllEdgeBaseColor(edgeColor);
        n.setAllEdgeHightlightColor(edgeHighlightColor);
        
        // setup the algorithm informations (source code, counter etc..)
        Node sourceCodePosition =  new Coordinates(n.getMaxX()+80,((Coordinates)headerText.getUpperLeft()).getY()+20);
        
        SourceCode firstAlgoInfo = setStartFirstAlgorithmInformation(sourceCodePosition);        
        
        src1 = setSourceCodeForward(sourceCodePosition); 
        src1.highlight(0);
        src1.hide();
        StringMatrix legend = setUpLegend(new Offset(0,40,src1,"SW"),matrixProperties);
       
        setUpVars(n);
        
        
        // Zähler Anfang
        AnimalStringMatrixGenerator matrixGenerator = new AnimalStringMatrixGenerator((AnimalScript) lang);
		MatrixProperties matProp = new MatrixProperties();
		String[][] strValues = new String[2][3];
		for(int i = 0; i < 3; i++){
    		strValues[0][i] = "-1";
    		strValues[1][i] = "-1";
    	}
		
		smat = new StringMatrix(matrixGenerator,
				new Coordinates(700, 490), strValues, "Values", null,
				matProp);
		smat.hide();
        TwoValueCounter counter = lang.newCounter(smat);
        CounterProperties cp = new CounterProperties();
        cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);
        TwoValueView view = lang.newCounterView(counter,  
        		new Offset(0,40,legend,"SW"), cp, true, true);

        //Zähler Ende
        
        // error handling
        if(n.hasLoops())
        {
        	headerText.hide();
        	TextProperties warningProp = new TextProperties();
        	warningProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF,Font.BOLD, 24));
        	lang.newText(new Coordinates(20,30), "Der Algorithmus kann nicht ausgeführt werden, da der Graph Loops hat.", "loopwaring", null, warningProp);
        	lang.finalizeGeneration();
        	return lang.toString();
        }
        
        if(n.hasNegativeProcessTime())
        {
        	headerText.hide();
        	TextProperties warningProp = new TextProperties();
        	warningProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF,Font.BOLD, 24));
        	lang.newText(new Coordinates(20,30), "Der Algorithmus kann nicht ausgeführt werden, da der Graph negative Prozesszeiten hat.", "loopwaring", null, warningProp);
        	lang.finalizeGeneration();
        	lang.finalizeGeneration();
        	return lang.toString();
        }
        
        lang.nextStep("Beginn der Vorwärtsrechnung");
       
        firstAlgoInfo.hide(); 
        src1.show();
        lang.nextStep();
        
        // starts the algorithm for the first direction. 
        // It calculates the earliest start timer and the earliest end time
        src1.unhighlight(0);
        List<Integer> nodesToProcess = n.getEndNodes();
        vars.declare("string", currentNodeName, "",STEPPER);
        for(Integer currentNode: nodesToProcess){
        	src1.highlight(1);
        	this.calculateFirstDirection(currentNode);
        	
        }
        
        vars.discard(currentNodeName);
        
        
        // show info text for the change to the second direction
        nodesToProcess = n.getStartNodes();
        src1.hide();
        SourceCode algorithmChange = setChangeAlgorithmInformation(src1.getUpperLeft());
        lang.nextStep("Wechsel Rückwärts- zu Rückwärtsrechnung");
        
        // change the source code for the algorithm
        algorithmChange.hide();
        src2 = this.setSourceCodeBackward(sourceCodePosition);
        src2.highlight(2);
        lang.nextStep("Beginn der Rückwärtsrechnung");
        
        // start the second algorithm, It calculates the latest start and end time.
        src2.unhighlight(2);
        vars.declare("string", currentNodeName, "",STEPPER);
        for(Integer currentNode: nodesToProcess){
        	src2.highlight(3);
        	this.calculateSecondDirection(currentNode);
        }
        vars.discard(currentNodeName);
             
        // calculate and show critical path
        this.startCriticalPathQuestion(n);
           
        lang.nextStep();
        this.startDelayQuestion(n);
        
        src2.hide();
        SourceCode criticalPathText = setCriticicalPathInformation(sourceCodePosition);
        for(Integer currentNode : n.getStartNodes()){
        	this.drawCriticalPath(currentNode);
        }
        lang.nextStep("Darstellung kritischer Pfad");
        
        // last page information
        criticalPathText.hide();
        n.hideGraph();
        legend.hide();
        SourceCode endInformation = this.showEndText(new Offset(0,40,headerText, "SW"),counter);
        lang.nextStep("Schlussinformationen");
                
        lang.finalizeGeneration();

        return lang.toString();
    }
    
    private void setUpVars(NetzplanGraph npg)
    {
    	
    	for(int nodeId: npg.getAllNodes())
    	{
    		vars.declare("int" , ptPreName+npg.getName(nodeId),String.valueOf(npg.getProcessTime(nodeId)), FIXED_VALUE);
    		vars.declare("int" , estPreName+npg.getName(nodeId),"-1", MOST_RECENT_HOLDER);
    		vars.declare("int" , eetPreName+npg.getName(nodeId),"-1", MOST_RECENT_HOLDER);
    		vars.declare("int" , lstPreName+npg.getName(nodeId),String.valueOf(-1), MOST_RECENT_HOLDER);
    		vars.declare("int" , letPreName+npg.getName(nodeId),String.valueOf(-1), MOST_RECENT_HOLDER);
    	}
	
    }

    private StringMatrix setUpLegend(Node position, MatrixProperties matProp)
    {
    	if(matProp == null)
    		matProp = new MatrixProperties();
		
        matProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
        
        matProp.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
        matProp.set(AnimationPropertiesKeys.CELL_HEIGHT_PROPERTY, 20);
        matProp.set(AnimationPropertiesKeys.CELL_WIDTH_PROPERTY, 125);
        String[][] data = new String[2][3];
        
        data[0][0] = "Name";
        data[1][0] = "Prozesszeit";
        data[0][1] = "Früheste Startzeit";
        data[0][2] = "Früheste Endzeit";
        data[1][1] = "Späteste Startzeit";
        data[1][2] = "Späteste Endzeit";
        
        return lang.newStringMatrix(position, data, "legend", null,matProp);
    }
    
    /**
     * The first part of the algorithm. It calculates the earliest end time and the earliest start time.
     * The algorithm is recursive.
     * Call this with each end node to calculate it for the whole graph.
     * @param node the node to calculate the earliest start time for.
     */
	private void calculateFirstDirection(Integer node){

		vars.set(currentNodeName, n.getName(node));
		
    	src1.highlight(5);
    	n.highlightNode(node);
    	lang.nextStep("Aufruf Vorwärtsrechnung Knoten " + graph.getNodeLabel(node));
    	src1.unhighlight(12);
    	src1.unhighlight(1);
    	src1.unhighlight(5);
		List<Integer> predecessors = n.getPredecessors(node);
		
		// if the node is a start node than the earliest start time is 0 and the earliest end time is 0+processTime
		if(n.isStartNode(node)){
    		smat.getElement(0, 0); // neu hinzugefuegt 2408
			src1.highlight(6);
    		lang.nextStep();
    		src1.unhighlight(6);
    		src1.highlight(7);
			n.setEarliestStartTime(node, 0);
			vars.set(estPreName+n.getName(node), "0");
			smat.put(0, 0, "-1", null, null);
			lang.nextStep();
			src1.unhighlight(7);
			src1.unhighlight(6);
			src1.highlight(8);
    		n.setEarliestEndTime(node, n.getProcessTime(node));
    		vars.set(eetPreName+n.getName(node), String.valueOf(n.getProcessTime(node)));
    		smat.put(0, 0, "-1", null, null);
    		smat.getElement(0, 0);
    		smat.getElement(0, 0); //neu hinzugefuegt 2408
    		lang.nextStep();
    		src1.unhighlight(8);
    		

    	}else{
    		
    		// if the node is not a start node than the earliest start time is 
    		// max(for all predeccesors : predecessor.earliestEndTime)
    		// and the earliest end time is earliestStartTime + processTime.
    		
    		src1.highlight(9);
    		smat.getElement(0,0); // neu hinzugefuegt 2408
    		for(Integer currentPredcessor: predecessors){
    			n.highlightEdge(currentPredcessor, node);
    		}
    		lang.nextStep();
    		

    		src1.unhighlight(9);
    		src1.highlight(10);
    		lang.nextStep();
    		
    		// calculates the earliest start time and end time of the predecessors if necessary.
    		for(Integer currentPredecessor: predecessors){
        		for(Integer innerPredecessors: predecessors){
        			n.unHighlightEdge(innerPredecessors, node);
        		}
        		src1.unhighlight(10);
        		// if the current entry for earliestEndTime of the predecessor is invalid (not set) call calculateFirstDirection with the predecessor.
    			if(n.hasValidEntry(currentPredecessor, NetzplanGraph.CellID.EarliestEndTime) == false){
    	    		smat.getElement(0,0); // neu hinzugefuegt 2408
    				n.highlightEdge(currentPredecessor, node);
    				src1.highlight(11);
    				lang.nextStep();
    				n.unHighlightEdge(currentPredecessor, node);
    				src1.unhighlight(11);
        			src1.highlight(12);
        			calculateFirstDirection(currentPredecessor);
        		}
        	}
    		src1.highlight(13);
    		for(Integer innerPredecessors: predecessors){
    			n.highlightEdge(innerPredecessors, node);
    		}
    		
    		startFirstDirectionQuestion(n, node);
    		
    		lang.nextStep();
    		
    		//calculate and sets the maximal earliest start time end earliest end time for the given node.
        	for(Integer currentPredecessor: predecessors){
        		for(Integer innerPredecessors: predecessors){
        			n.unHighlightEdge(innerPredecessors, node);
        		}
        		src1.unhighlight(13);
        		if(n.hasValidEntry(node, NetzplanGraph.CellID.EarliestEndTime)==false ||n.getEarliestEndTime(currentPredecessor) > n.getEarliestStartTime(node)){
        			if(n.hasValidEntry(node, NetzplanGraph.CellID.EarliestEndTime)==true){
        				smat.getElement(0, 0);
        	    		smat.getElement(0,0); // neu hinzugefuegt 2408
        			}
            		smat.getElement(0,0); // neu hinzugefuegt 2408
        			n.highlightEdge(currentPredecessor, node);
        			src1.highlight(14);
        			lang.nextStep();
        			n.setEarliestStartTime(node, n.getEarliestEndTime(currentPredecessor));
        			vars.set(estPreName+n.getName(node), String.valueOf(n.getEarliestEndTime(currentPredecessor)));
        			smat.getElement(0, 0);
        			smat.put(0, 0, "-1", null, null);
        			src1.highlight(15);
        			src1.unhighlight(14);
        			lang.nextStep();
        			src1.unhighlight(15);
        			src1.highlight(16);
        			n.setEarliestEndTime(node, n.getEarliestStartTime(node) + n.getProcessTime(node));
        			vars.set(eetPreName+n.getName(node), String.valueOf(n.getEarliestStartTime(node) + n.getProcessTime(node)));
        			smat.getElement(0, 0);
            		smat.getElement(0,0); // neu hinzugefuegt 2408
        			smat.put(0, 0, "-1", null, null);
        			lang.nextStep();
        			src1.unhighlight(16);
        			n.unHighlightEdge(currentPredecessor, node);
        		}
        	}
    		
    	}
		n.unhighlightNode(node);	
    }
	
	/**
	 * The second algorithm. It calculates the latest start and end time.
	 * The algorithm is recursive.
	 * Start at each start node to calculate the values for the hole graph.
	 * @param node the node to calculate the values for.
	 */
	private void calculateSecondDirection(Integer node) {
		vars.set(currentNodeName, n.getName(node));
    	src2.highlight(5);
    	n.highlightNode(node);
    	lang.nextStep("Aufruf Rückwärtsrechnung Knoten " + graph.getNodeLabel(node));
    	src2.unhighlight(12);
    	src2.unhighlight(3);
    	src2.unhighlight(5);
		List<Integer> successors = n.getSuccessors(node);
		
		
		// if the node is an end node the latest start time is the earliest start time and
		// the latest end time is the earliest end time.
    	if(n.isEndNode(node)){
    		smat.getElement(0,0); // neu hinzugefuegt 2408
    		src2.highlight(6);
    		lang.nextStep();
    		src2.unhighlight(6);
    		src2.highlight(7);
    		n.setLatestStartTime(node, n.getEarliestStartTime(node));
    		vars.set(lstPreName+n.getName(node), String.valueOf(n.getEarliestStartTime(node)));
    		smat.put(0, 0, "-1", null, null);
    		smat.getElement(0, 0);
    		lang.nextStep();
    		src2.unhighlight(7); 
			src2.unhighlight(6);
			src2.highlight(8);
    		n.setLatestEndTime(node, n.getEarliestEndTime(node));
    		vars.set(letPreName+n.getName(node), String.valueOf(n.getEarliestEndTime(node)));
    		smat.getElement(0,0); // neu hinzugefuegt 2408
    		smat.put(0, 0, "-1", null, null); // neu hinzugefuegt 2408
    		lang.nextStep();
    		src2.unhighlight(8);
    	}else{
    		
    		// if the node is not an end node than the latest start time is
    		// min(for each successor: successor.latestStartTime-node.processTime)
    		// latest end time = node.latestStartTime+processTime
    		
    		src2.highlight(9);
    		smat.getElement(0,0); // neu hinzugefuegt 2408
    		for(Integer currentSuccessor: successors){
    			n.highlightEdge(node, currentSuccessor);
    		}
    		lang.nextStep();
    		
    		src2.unhighlight(9);
    		src2.highlight(10);
    		lang.nextStep();
    		
    		// calculates the latest end and latest start time for each successor if necessary
        	for(Integer currentSuccessor: successors){
         		for(Integer innerSuccessors: successors){
        			n.unHighlightEdge(node, innerSuccessors);
        		}
         		src2.unhighlight(10);
        		if(n.hasValidEntry(currentSuccessor, NetzplanGraph.CellID.LatestEndTime) == false){
        			smat.getElement(0,0); // neu hinzugefuegt 2408
        			n.highlightEdge(node, currentSuccessor);
    				src2.highlight(11);
    				lang.nextStep();
    				n.unHighlightEdge(node, currentSuccessor);
    				src2.unhighlight(11);
        			src2.highlight(12);
        			calculateSecondDirection(currentSuccessor);
        		}
        	}

        	src2.highlight(13); 
        	lang.nextStep(); // neu hinzugefuegt 2408

        	//src2.highlight(13);
        	
        	startSecondDirectionQuestion(n, node);
        	

        	//calculates and sets the minimal latest end and start time for the given node
        	for(Integer currentSuccessor: successors){
        		for(Integer innerSuccessors: successors){
        			n.unHighlightEdge(node, innerSuccessors);
        		}
        		src2.unhighlight(13);
        		if(n.hasValidEntry(node, NetzplanGraph.CellID.LatestEndTime)==false || n.getLatestStartTime(currentSuccessor)< n.getLatestEndTime(node)){
        			if(n.hasValidEntry(node, NetzplanGraph.CellID.LatestEndTime)==true){
        				smat.getElement(0, 0);
        				smat.getElement(0,0); // neu hinzugefuegt 2408
        			}
        			smat.getElement(0, 0); // neu hinzugefuegt 2408
        			n.highlightEdge(node, currentSuccessor);
        			src2.highlight(14);
        			lang.nextStep();
        			n.setLatestStartTime(node, n.getLatestStartTime(currentSuccessor)- n.getProcessTime(node));
        			vars.set(lstPreName+n.getName(node), String.valueOf(n.getLatestStartTime(currentSuccessor)- n.getProcessTime(node)));
        			smat.put(0, 0, "-1", null, null);
        			smat.getElement(0, 0);
        			smat.getElement(0,0); // neu hinzugefuegt 2408
        			src2.highlight(15);
        			src2.unhighlight(14);
        			lang.nextStep();
          			src2.unhighlight(15);
        			src2.highlight(16);
        			n.setLatestEndTime(node, n.getLatestStartTime(node)+ n.getProcessTime(node));
        			vars.set(letPreName+n.getName(node), String.valueOf(n.getLatestStartTime(node)+ n.getProcessTime(node)));
        			smat.put(0, 0, "-1", null, null);
        			smat.getElement(0, 0);
        			smat.getElement(0,0); // neu hinzugefuegt 2408
        			lang.nextStep();
        			src2.unhighlight(16);
        			n.unHighlightEdge(node, currentSuccessor);
        		}
        	}
    	}
    	n.unhighlightNode(node);
    		
	}
	
	/**
	 * Recursive function to show the critical path beginning at the given node.
	 * @param actualNode the node to draw the critical path for.
	 * @return true if the current node is a nod on a critical path, false otherwise.
	 */
	private boolean drawCriticalPath(Integer actualNode){
		LinkedList<Integer> currentSuccessors = new LinkedList<Integer>();
		currentSuccessors.addAll(n.getSuccessors(actualNode));
		boolean isCriticalStep = false;
		for(Integer actualSuccessor: currentSuccessors){
			if(n.getEarliestStartTime(actualNode) == n.getLatestStartTime(actualNode) && (n.isEndNode(actualSuccessor)||drawCriticalPath(actualSuccessor) )){
				n.highlightEdge(actualNode, actualSuccessor);
				isCriticalStep = true;
			}
			
		}
				
		
		return isCriticalStep;
	}
	
	/**
	 * Puts all nodes that are on a critical path into the given list.
	 * Each node will be there only ones.
	 * @param actualNode
	 * @param criticalNodes
	 * @return true if the actualNode is on a critical path.
	 */
	private boolean getCriticalPath(Integer actualNode, List<Integer> criticalNodes)
	{
		
		if(n.isEndNode(actualNode))
		{
			if(!criticalNodes.contains(actualNode))
				criticalNodes.add(actualNode);
			return true;
		}
		
		if(n.getEarliestStartTime(actualNode) == n.getLatestStartTime(actualNode))
		{
			
			for(Integer successor: n.getSuccessors(actualNode))
			{
				if(getCriticalPath(successor, critcalPathNodes))
				{
					if(!criticalNodes.contains(actualNode))
					{
						criticalNodes.add(actualNode);
					}
					return true;
				}				
			}
		}
		
		return false;
	}
	
	
	
    private Text setHeader(Color headerColor){
    	TextProperties headerProps = new TextProperties();
    	headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF,Font.BOLD, 24));
    	headerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, headerColor);
    	return lang.newText(new Coordinates(20,30), "Die Netzplantechnik", "header", null, headerProps);

    	
    }
	
	private void setInformationText(Node textPosition){
    	SourceCodeProperties infoProps = new SourceCodeProperties();
    	infoProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 20));
    	SourceCode infoText = lang.newSourceCode(textPosition, "InfoText", null, infoProps);
    	
    	infoText.addCodeLine("Bei der Netzplantechnik handelt es sich um eine Methode, welche im Rahmen der Terminplanung bzw. des", "Line0", 0, null);
    	infoText.addCodeLine("Projektmanagements zum Einsatz kommt. Das Ziel besteht darin die Mindestdauer eines Projektes auf Basis", "Line1", 0, null);
    	infoText.addCodeLine("der einzelnen Arbeitsvorgänge und ihrer Beziehungen untereinander zu bestimmen. Die Beziehungen der  ", "Line2", 0, null);
    	infoText.addCodeLine("einzelnen Vorgänge werden dabei in Form eines gerichteten Graphen dargestellt.", "Line3", 0, null);
    	infoText.addCodeLine("Neben der minimalen Gesamtdauer, welche das zu untersuchende Projekt im Idealfall benötigt, werden", "Line4", 0, null);
    	infoText.addCodeLine("zudem für jeden Arbeitsvorgang sogenannte Pufferzeiten ermittelt. Diese geben an in welchem Ausmaß", "Line5", 0, null);
    	infoText.addCodeLine("Verzögerungen eines Arbeitsvorganges möglich sind, ohne dass sie sich negativ auf die Gesamtdauer des", "Line6", 0, null);
    	infoText.addCodeLine("Projektes auszuwirken.", "Line7", 0, null);
    	infoText.addCodeLine("", "Line8", 0, null);
    	infoText.addCodeLine("Es ist zu beachten, dass die Beziehungen zwischen den Arbeitsvorgängen eindeutig zu definieren sind.", "Line9", 0, null);
    	infoText.addCodeLine("Zyklen sind daher nicht zulässig!", "Line10", 0, null);


    	lang.nextStep("Einleitung");
    	infoText.hide();
    }
	
	
    private SourceCode setSourceCodeForward(Node codePosition){
        SourceCode src = lang.newSourceCode(codePosition, "SourceCode", null, sourceCodeStyle);
        src.addCodeLine("01. For all nodes without outgoing edges do", "Code0", 0, null);
        src.addCodeLine("02.     calculateFirstDirection(node)", "Code1", 0, null);
        src.addCodeLine("03. For all nodes without ingoing edges do", "Code2", 0, null);
        src.addCodeLine("04.     calculateSecendDirection(node)", "Code3", 0, null);
        src.addCodeLine("", "Code4", 0, null);
        src.addCodeLine("05. calculateFirstDirection(node)", "Code5", 0, null);
        src.addCodeLine("06.     if node has no ingoing edges do", "Code6", 0, null);
        src.addCodeLine("07.         EarliestStartTime of node = 0", "Code7", 0, null);
        src.addCodeLine("08.         EarliestEndTime of node = EarliestStartTime of Node + ProcessTime of node", "Code8", 0, null);
        src.addCodeLine("09.     if node has ingoing edges do:", "Code9", 0, null);
        src.addCodeLine("10.         for each predecessor of node do", "Code10", 0, null);
        src.addCodeLine("11.             if EarliestStartTime of Predecessor has not been set do", "Code11", 0, null);
        src.addCodeLine("12.                 calculateFirstDirection(currentPredecessor)", "Code12", 0, null);
        src.addCodeLine("13.     for each predecessor of node do:", "Code13", 0, null);
        src.addCodeLine("14.         if EarliestStartTime of node has not been set or EarliestEndTime of predecssor > EarliestStartTime of node", "Code14", 0, null);
        src.addCodeLine("15.             EarliestStartTime of node = EarliestEndTime of Predecessor", "Code15", 0, null);
        src.addCodeLine("16.             EarliestEndTime  of node = EarliestStartTime of node + ProcessTime of node", "Code16", 0, null);
        return src;
    }
    
    private SourceCode setSourceCodeBackward(Node position){
        SourceCode src = lang.newSourceCode(position, "SourceCode", null, sourceCodeStyle);
        src.addCodeLine("01. For all nodes without outgoing edges do", "Code0", 0, null);
        src.addCodeLine("02.     calculateFirstDirection(node)", "Code1", 0, null);
        src.addCodeLine("03. For all nodes without ingoing edges do", "Code2", 0, null);
        src.addCodeLine("04.     calculateSecendDirection(node)", "Code3", 0, null);
        src.addCodeLine("", "Code4", 0, null);
        src.addCodeLine("05. calculateSecondDirection(node)", "Code4", 0, null);
        src.addCodeLine("06.     if node has no outgoing edges do", "Code5", 0, null);
        src.addCodeLine("07.          LatestStartTime of node = EarliestStartTime of Node", "Code6", 0, null);
        src.addCodeLine("08.          LatestEndTime of node = EearliestEndTime of Node", "Code7", 0, null);
        src.addCodeLine("09.     if node has outgoing edges do:", "Code08", 0, null);
        src.addCodeLine("10.         for each successor of node do", "Code09", 0, null);
        src.addCodeLine("11.             if LatestStartTime of Successor has not been set do", "Code10", 0, null);
        src.addCodeLine("12.                 calculateSecondDirection(currentSuccessor)", "Code11", 0, null);
        src.addCodeLine("13.     for each successor of node do:", "Code12", 0, null);
        src.addCodeLine("14.         if LatestStartTime of node has not been set or LatestStartTime of successor < LatestEndTime of node", "Code13", 0, null);
        src.addCodeLine("15.             LatestStartTime of node = LatestStartTime of Successor - ProcessTime of node", "Code14", 0, null);
        src.addCodeLine("16.             LatestEndTime of node = LatestStartTime of node + ProcessTime of node", "Code15", 0, null);
        return src;
    }
    
    private SourceCode setChangeAlgorithmInformation(Node position){
    	SourceCodeProperties sProb = new  SourceCodeProperties();
        sProb.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font (Font.SANS_SERIF,Font.BOLD, 16));
        sProb.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
    	SourceCode infoText = lang.newSourceCode(position, "InfoText", null, sProb);
    	infoText.addCodeLine("Achtung es beginnt nun der zweite Teil des Verfahrens!", "line1", 0, null);
        infoText.addCodeLine("Der Algorithmus berechnet dabei die späteste Startzeit", "line2", 0, null);
        infoText.addCodeLine("und die späteste Endzeit der einzelnen Knoten", "line3", 0, null);
        return infoText;
    
    }
    
    private SourceCode setStartFirstAlgorithmInformation(Node position){
    	SourceCodeProperties sProb = new  SourceCodeProperties();
        sProb.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font (Font.SANS_SERIF,Font.BOLD, 16));
        sProb.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
    	SourceCode infoText = lang.newSourceCode(position, "InfoText", null, sProb);
    	infoText.addCodeLine("Es beginnt nun der erste Teil des Verfahrens!", "line1", 0, null);
        infoText.addCodeLine("Der Algorithmus berechnet dabei die früheste Startzeit", "line2", 0, null);
        infoText.addCodeLine("und die früheste Endzeit der einzelnen Knoten.", "line3", 0, null);
        return infoText;
    
    }
    
    private SourceCode setCriticicalPathInformation(Node position){
    	
    	SourceCodeProperties sProb = new  SourceCodeProperties();
        sProb.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font (Font.SANS_SERIF,Font.BOLD, 16));
        sProb.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
        SourceCode infoText = lang.newSourceCode(position, "InfoText", null, sProb);
        infoText.addCodeLine("Der kritische Pfad wird nun durch die", "line1", 0, null);
        infoText.addCodeLine("hervorgehobenen Kanten repräsentiert", "line2", 0, null);
        infoText.addCodeLine("auf ihm befinden sich alle Vorgänge,", "line3", 0, null);
        infoText.addCodeLine("deren Verzögerung eine Verzögerung des", "line4", 0, null);
        infoText.addCodeLine("gesamten Vorhabens verursacht!", "line5", 0, null);
        return infoText;
    
    }
    
	private SourceCode showEndText(Node position, TwoValueCounter counter) {
    	//int actualCount = iterations - 1;
		SourceCodeProperties infoProps = new SourceCodeProperties();
    	infoProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 14));
    	SourceCode endText = lang.newSourceCode(position, "InfoText", null, infoProps);
    	endText.addCodeLine("Informationen zu dem zuvor angzeigten Ablauf des Algorithmus:", "Line0", 0, null);
    	endText.addCodeLine("", "Line1", 0, null);
    	endText.addCodeLine("Anzahl Schreibzugriffe: " + counter.getAssigments(), "Line3", 0, null);
    	endText.addCodeLine("Anzahl Lesezugriffe: " + counter.getAccess(), "Line4", 0, null);
    	String criticalPathString = "";
    	for(int i = critcalPathNodes.size()-1; i >= 0; i--){
    		criticalPathString = criticalPathString + " " + n.getName(critcalPathNodes.get(i));
    	}
    	endText.addCodeLine("Knoten auf kritischem Pfad: " + criticalPathString, "Line 5", 0, null);
    	
    	int maxtime = -1;
    	for(int i = 0; i < n.getAllNodes().size(); i++){
    		if(n.isEndNode(i)){
    			if(n.getEarliestEndTime(i) > maxtime){
    				maxtime = n.getEarliestEndTime(i);
    			}
    		}
    	}
    	endText.addCodeLine("Die minimale Projektdauer betägt: " + maxtime + " Zeiteinheiten", "Line6", 0, null);
    	

    	return endText;
	}
    
	private void startCriticalPathQuestion(NetzplanGraph npg)
	{
		
		
		
		 critcalPathNodes = new LinkedList<Integer>(); // habe es zu gloabelen Vairable gemacht, um auf die Knoten im Pfad zuzuggreifen zu koennen
	         
	     for(Integer currentNode:npg.getStartNodes()){
	      	this.getCriticalPath(currentNode,critcalPathNodes);
	     }
	      
	     if(!askQuestions)
				return;
	     
		 MultipleSelectionQuestionModel question = new MultipleSelectionQuestionModel("Kritischer Pfad");
		 question.setPrompt("Welche Knoten gehören alles zu einem kritischen Pfad? (Es kann mehr als einen kritischen Pfad geben.)");
	     question.setGroupID(qg03);
	     
	     StringBuilder criticalPathNodesString = new StringBuilder();
	     
	     for(int i = 0; i<critcalPathNodes.size(); i++)
	     {
	    	 criticalPathNodesString.append(npg.getName(critcalPathNodes.get(i)));
	    	 
	    	 if(i < (critcalPathNodes.size()-1))
	    	 {
	    		 criticalPathNodesString.append(", ");
	    	 }
	     }
	       
	        
	     for(Integer currentNode : npg.getAllNodes())
	     {
	      	if(critcalPathNodes.contains(currentNode))
        	{
	      		question.addAnswer(npg.getName(currentNode), 5,npg.getName(currentNode) + " gehört zu einem kritischen Pfad.\n");
	        	}else
	        	{
	        		question.addAnswer(npg.getName(currentNode), -5,npg.getName(currentNode) + " gehört nicht zu einem kritischen Pfad. Richtige Antworten wären: " +criticalPathNodesString.toString()+ ".\n");
	        	}
	        	
	     }
	     
	     lang.addMSQuestion(question);
	}
	
	private void startDelayQuestion(NetzplanGraph npg)
	{
		int node;
		int delay;
			
		List<Integer> nodes = npg.getAllNodes();
		
		if(nodes.size() == 0)
		{
			return;
		}else if(nodes.size() == 1)
		{
			node = nodes.get(0);
		}else
		{
			Iterator<Integer> nodesIt = nodes.iterator();
			
			while(nodesIt.hasNext())
			{
				if(npg.isEndNode(nodesIt.next()))
				{
					nodesIt.remove();
				}
			}
			
			node = nodes.get(rg.nextInt(nodes.size()));
		}
		
		delay = npg.getLatestStartTime(node)- npg.getEarliestStartTime(node);
		
		int delta = 3;
		int maxDelay = delay+delta+1;
		int minDelay = 1;
		
		if(delay > delta)
			minDelay = delay - delta;
	
		delay = rg.nextInt(maxDelay-minDelay)+minDelay;
		
		
		startDelayQuestion(npg, node, delay);
	}
	
	private void startDelayQuestion(NetzplanGraph npg, int node, int delay)
	{
		
		if(!askQuestions)
			return;
		
		String nodeLabel =  npg.getName(node);
		int answer = getDelay(npg, node, delay);
		
		FillInBlanksQuestionModel question = new FillInBlanksQuestionModel("Delay Question");
		question.setPrompt("Angenommen der Startzeitpunkt von Knoten "+ nodeLabel+" verzögert sich um "+delay+" Einheiten, um wie viel verzögert sich maximal die Fertigstellung des Endproduktes?");
		question.setGroupID(qg04);
		
		question.addAnswer(""+answer, 5, answer+" war richtig.");

		lang.addFIBQuestion(question);
		
	}
	
	
	private void startFirstDirectionQuestion(NetzplanGraph npg, int nodeId)
	{
		if(!askQuestions)
			return;
		
		fdQuestionCounter++;
		int answer = calculateEST(npg, nodeId);
		String nodeLabel = npg.getName(nodeId);
		
		FillInBlanksQuestionModel question = new FillInBlanksQuestionModel("First Direction Question "+fdQuestionCounter);
		question.setGroupID(qg01);
		question.setPrompt("Welchen Wert für die 'Früheste Start Zeit' wird Knoten " + nodeLabel+" am Ende des Algorithmus haben.");
		question.addAnswer(String.valueOf(answer), 5, answer + " war richtig.");
		
		lang.addFIBQuestion(question);
	}
	
	
	private void startSecondDirectionQuestion(NetzplanGraph npg, int nodeId)
	{
		if(!askQuestions)
			return;
		
		sdQuestionCounter++;
		int answer = calculateLST(npg, nodeId);
		String nodeLabel = npg.getName(nodeId);
		
		FillInBlanksQuestionModel question = new FillInBlanksQuestionModel("Second Direction Question "+sdQuestionCounter);
		question.setGroupID(qg02);
		question.setPrompt("Welchen Wert für die 'Späteste Start Zeit' wird Knoten " + nodeLabel+" am Ende des Algorithmus haben.");
		question.addAnswer(String.valueOf(answer), 5, answer + " war richtig.");
		
		lang.addFIBQuestion(question);
	}
	
	private int getDelay(NetzplanGraph npg, int node, int delay)
	{
		if(npg.isEndNode(node))
		{
			return delay;
		}
		
		int currentDelay = delay - (npg.getLatestStartTime(node)- npg.getEarliestStartTime(node));
		
		if(currentDelay <= 0)
		{
			return 0;
		}
		
		return currentDelay;
			
	}
	
	/**
	 * Calculates the earliest start time for the the given node.
	 * It doesn't change any value.
	 * @param npg the graph of the node.
	 * @param nodeId the node to calculate the est for.
	 * @return the earliest start time.
	 */
	private int calculateEST(NetzplanGraph npg, int nodeId)
	{
		if(npg.isStartNode(nodeId))
		{
			return 0;
		}
		int est = 0;
		for(int predecessor: npg.getPredecessors(nodeId))
		{
			int tmp = calculateEST(npg, predecessor)+ npg.getProcessTime(predecessor);		
			if(tmp > est)
				est = tmp;
		}
		
		return est;
	}
	
	/**
	 * Calculates the latest start time for the given node.
	 * This doesn't change any value.
	 * The earliest start time and end time must be valid for the whole graph.
	 * @param npg the graph with the node.
	 * @param nodeId the node to calculate the latest start time for.
	 * @return the latest start time.
	 */
	private int calculateLST(NetzplanGraph npg, int nodeId)
	{
		
		if(npg.isEndNode(nodeId))
		{
			return npg.getEarliestStartTime(nodeId);
		}
		
		int lst = Integer.MAX_VALUE;
		for(int successor: npg.getSuccessors(nodeId))
		{
			int tmp = calculateLST(npg, successor)-npg.getProcessTime(nodeId);
			
			if(tmp < lst)
			{
				lst = tmp;
			}
		}
		
		return lst;
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
    	
    	QuestionGroupModel model;
      
        model = new QuestionGroupModel(qg01, maxNumberOfRepeats);
        lang.addQuestionGroup(model);
        
        model = new QuestionGroupModel(qg02, maxNumberOfRepeats);
        lang.addQuestionGroup(model);
        
        model = new QuestionGroupModel(qg03, onlyOnceQuestions);
        lang.addQuestionGroup(model);
        
        model = new QuestionGroupModel(qg04, onlyOnceQuestions);
        lang.addQuestionGroup(model);
        
        
    }

    

    

    public String getName() {
        return "Netzplantechnik";
    }

    public String getAlgorithmName() {
        return "Netzplantechnik";
    }

    public String getAnimationAuthor() {
        return "Jan Ulrich Schmitt & Dennis Juckwer";
    }

    public String getDescription(){
        return "Bei der Netzplantechnik handelt es sich um eine Methode, welche im Rahmen der Terminplanung bzw. des "
 +"Projektmanagements zum Einsatz kommt. Das Ziel besteht darin die Mindestdauer eines Projektes auf Basis "
 +"der einzelnen Arbeitsvorgänge und ihrer Beziehungen untereinander zu bestimmen. Die Beziehungen der "
 +"einzelnen Vorgänge werden dabei in Form eines gerichteten Graphen dargestellt. \n"
 +"Neben der minimalen Gesamtdauer, welche das zu untersuchende Projekt im Idealfall benötigt, werden "
 +"zudem für jeden Arbeitsvorgang sogenannte Pufferzeiten ermittelt. Diese geben an in welchem Ausmaß "
 +"Verzögerungen eines Arbeitsvorganges möglich sind, ohne dass sie sich negativ auf die Gesamtdauer des "
 +"Projektes auswirken."
 +"\n \n"
 +"Es ist zu beachten, dass die Beziehungen zwischen den Arbeitsvorgängen eindeutig zu definieren sind. "
 +"Zyklen sind daher nicht zulässig!"
 + "\n \n"
 + "Beim manuellen setzen des Graphprimitves müssen folgende Dinge beachtet werden:\n"
 + "Die Gewichte der Kanten repräsentieren die Prozesszeit des Knotens von dem die Kante ausgeht. "
 + "Achte deshalb darauf, dass alle ausgehende Kanten von einem Knoten die gleiche Gewichtung haben. \n"
 + "Um die Prozesszeit von den Endkoten festzulegen, wird ein weiterer Dummy-Knoten mit einer Kante vom Endknoten zum Dummy-Knoten benötigt. "
 + "Der Dummy-Knoten wird im Graphen nicht angezeigt und vom Algorithmus nicht beachtet.\n"
 + "Der Graph darf keine Loops und keine negativen Kanten haben.";
       
    }

    public String getCodeExample(){
        return
 "01. For all nodes without outgoing edges do \n"
 +"02.     calcualteFirstDirection(node) \n"
 +"03. For all nodes without ingoing edges do \n"
 +"04.     calculateSecondDirection(node) \n"
 +"\n"
 +"05. calculateFirstDirection(node) \n"
 +"06.     if node has no ingoing edges do: \n"
 +"07.         EarliestStartTime of node = 0 \n"
 +"08.         EarliestEndTime of node = EarliestStartTime of Node + ProcessTime of node \n"
 +"09.     if node has no ingoing edges do: \n"
 +"10.         for each predecessor of node do: \n"
 +"11.             if EarliestStartTime of Predecessor has not been set do: \n"
 +"12.                 calculateFirstDirection(currentPredecessor) \n"
 +"13.     for each predecessor of node do: \n"
 +"14.         if EearliestStartTime of node has not been set or EarliestEndTime of predecessor > EarliestStartTime of node \n"
 +"15.             EarliestStartTime of node = EarliestEndTime of predecessor \n"
 +"16.             EarliestEndTime of node = EarliestStartTime of node + ProcessTime of node \n"
 +"\n"
 +"17. calculateSecondDirection(node) \n"
 +"18.     if node has no outgoing edges do: \n"
 +"19.         LatestStartTime of node = EarliestStartTime of node \n"
 +"20.         LatestEndTime of node = EarliestEndTime of node \n"
 +"21.     if node has outgoing edges do: \n"
 +"22.         for each successor of node do: \n"
 +"23.             if LatestStartTime of successor has not been set do: \n"
 +"24.                 calculateSecondDirection(currentSuccessor) \n"
 +"25.     for each successor of node do: \n"
 +"26.         if LatestStartTime of node has not been set or LatestStartTime of successor < LatestEndTime of node: \n"
 +"27.             LatestStartTime of node = LatestStartTime of successor - ProcessTime of node \n"
 +"28.             LatestEndTime of node = LatestStartTime of node + ProcessTime of node \n";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

}