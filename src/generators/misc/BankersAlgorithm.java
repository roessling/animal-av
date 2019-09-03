/*
 * BankersAlgorithm.java
 * Tomasz Gasiorowski, 2017 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;
import java.util.Random;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.maths.grid.Grid;
import generators.maths.grid.GridProperty;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.TrueFalseQuestionModel;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;


public class BankersAlgorithm implements Generator {
    private Language lang;
    private TextProperties textProps;
    private int[][] allocationMatrix;
    private SourceCodeProperties sourceCodeProps;
    private int[] availableResources;
    private int[][] requestMatrix;
    private int[][] needMatrix;
    boolean[] terminated;
    private Locale l;
    
    public BankersAlgorithm(Locale l){
    	this.l = l;
    }
    public boolean bankersAlgorithm() {
    	Random rn = new Random();
    	int one;
    	int two;
    	int three;
    	one = rn.nextInt(requestMatrix.length-2);
    	while(true){
    		two = rn.nextInt(requestMatrix.length-2);
    		if (two!= one){
    			break;
    		}
    	}
    	while(true){
    		three = rn.nextInt(requestMatrix.length-2);
    		if (three!= one && three!= two) {
				break;
			}
    	}
    	int[] TF = new int[3];
    	TF[0] = one;
    	TF[1] = two;
    	TF[2] = three;
    	Arrays.sort(TF);
    	boolean TFanswered = false;
    	int blankscount = 0;
    	
    	terminated = new boolean[requestMatrix.length];
    	needMatrix = new int[requestMatrix.length][availableResources.length];
    	TextProperties tp = textProps;
    	tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 20));
		RectProperties rp = new RectProperties();
		rp.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		rp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.green);
		rp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		RectProperties rp2 = new RectProperties();
		rp2.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		rp2.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.red);
		rp2.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		
		Text bankerstext = lang.newText(new Coordinates(10, 30), "Banker's algorithm", "BANKERS", null,tp);
		bankerstext.hide();
		Rect rect = lang.newRect(new Offset(-5, -5,bankerstext,"NW"), new Offset(5, 5, bankerstext, "SE"), "RECT", null,rp);
		bankerstext.show();
		Text desc;
		Text desc1;
		Text desc2;
		Text desc3;
		Text desc4;
		Text desc5;
		Text desc6;
		Text desc7;
		Text desc8;
		Text desc9;
		Text desc10;
		Text desc11;
		Text desc12;
		Text desc13;
		Text desc14 = null;
		Text desc15 = null;
		Text desc16 = null;
		
		if (l.equals(Locale.GERMANY)) {
			desc = lang.newText(new Coordinates(10, 120), "Beschreibung: ", "desc", null,tp);
			desc1 = lang.newText(new Coordinates(10, 160), "Der Banker's Algorithm wird von Betriebsystemen verwendet, um Deadlocks, die durch Allokation von shared Resourcen verursacht wurden, vorzubeugen.", "desc1", null,tp);
			desc2 = lang.newText(new Coordinates(10, 180), "Er simuliert die Allokation von Resourcen in einem System, basierend auf dem worst case Szenario, wo jeder Prozess immer seine maximale Anzahl an Resourcen anfordert.","desc2", null,tp);
			desc3 = lang.newText(new Coordinates(10, 200), "Falls eine solche Allokationsreihenfolge existiert, ist das System in einem deadlock-freiem Zustand (safe state). Ansonsten kann es Deadlocks geben (unsafe state).", "desc3", null,tp);
			desc4 = lang.newText(new Coordinates(10, 240), "Der Algorithmus nimmt 3 Eingabeparameter entgegen:", "desc4", null,tp);
			desc5 = lang.newText(new Coordinates(10, 260), "1. Request matrix: die maximale Anzahl an Resourcen, die jeder Prozess anfordern kann", "desc5", null,tp);
			desc6 = lang.newText(new Coordinates(10, 280), "2. Allocation matrix: die Anzahl an Resourcen, auf die jeder Prozess zugreift", "desc6", null,tp);
			desc7 = lang.newText(new Coordinates(10, 300), "3. Available resources: die Anzahl an Resourcen, die frei sind", "desc7", null,tp);
			desc8 = lang.newText(new Coordinates(10, 340), "Am Anfang berechnet der Algorithmus die Need matrix, die die verbleibende Anforderungen an Resourcen von jedem Prozess beschreibt:", "desc8",null,tp);
			desc9 = lang.newText(new Coordinates(10, 360), "Need[i][j] = Request[i][j] - Allocation[i][j]", "desc10", null,tp);
			desc10 = lang.newText(new Coordinates(10, 400), "Danach wird die Allokation von Resourcen simuliert. Der Algorithmus geht alle Prozesse durch und bestimmt ob es genug freie Resourcen gibt, um den Need eines gegebenen Prozesses", "desc11",null,tp);
			desc11 = lang.newText(new Coordinates(10, 420), "bereitzustellen. Falls ja, der Prozess wird als terminiert markiert und gibt alle allokierte Resourcen wieder frei. Wenn alle Prozesse terminieren ist das Ergbenis des Algorithmus true, was", "desc12", null,tp);
			desc12 = lang.newText(new Coordinates(10, 440), "bedeutet dass das System in einem safe state ist. Falls es zu irgendeinem Zeitpunkt der Allokation nicht genug Resourcen gibt, um einen der verbleibenden Prozesse zum Terminieren zu bringen, ist", "desc13", null,tp);
			desc13 = lang.newText(new Coordinates(10, 460), "der return Wert und das System ist in einem unsafe state.", "desc14", null,tp);
			//desc15 = lang.newText(new Coordinates(10, 480), "If at some point during allocation there is not enough available resources to satisfy any of the remaining processes, the algorithm", "desc15",null,tp);
			//desc16 = lang.newText(new Coordinates(10, 500), "will return false, which means the system is in an unsafe state.", "desc16", null,tp);		
		}
		else {
			desc = lang.newText(new Coordinates(10, 120), "Description: ", "desc", null,tp);
			desc1 = lang.newText(new Coordinates(10, 160), "The Banker's algorithm is used by operating systems in order to prevent deadlocks created by processes during resource allocation.", "desc1", null,tp);
			desc2 = lang.newText(new Coordinates(10, 180), "It simulates the allocation of resources in a system based on the worst case scenario, where each process always requests their maximum amount of resources.","desc2", null,tp);
			desc3 = lang.newText(new Coordinates(10, 200), "If such an allocation order is possible, then the system is in a deadlock-free state (safe state), otherwise deadlocks might occur (unsafe state).", "desc3", null,tp);
			desc4 = lang.newText(new Coordinates(10, 240), "The algorithm takes 3 input variables:", "desc4", null,tp);
			desc5 = lang.newText(new Coordinates(10, 260), "1. Request matrix: the maximum amount of resources each process could possibly request", "desc5", null,tp);
			desc6 = lang.newText(new Coordinates(10, 280), "2. Allocation matrix: the amount of each resource each process is currently holding", "desc6", null,tp);
			desc7 = lang.newText(new Coordinates(10, 300), "3. Available resources: the amount of resources the system currently has available", "desc7", null,tp);
			desc8 = lang.newText(new Coordinates(10, 340), "Given this information, the algorithm will start by calculating the Need matrix, which is the remaining resource", "desc8",null,tp);
			desc9 = lang.newText(new Coordinates(10, 360), "need of each process:", "desc9", null,tp);
			desc10 = lang.newText(new Coordinates(10, 380), "Need[i][j] = Request[i][j] - Allocation[i][j]", "desc10", null,tp);
			desc11 = lang.newText(new Coordinates(10, 420), "After this, the resource allocation will be attempted. The algorithm will iterate over all processes, checking if there", "desc11",null,tp);
			desc12 = lang.newText(new Coordinates(10, 440), "are enough available resources to satisfy the Need value of the process.", "desc12", null,tp);
			desc13 = lang.newText(new Coordinates(10, 460), "If yes, the process will be marked as terminated and it will return all of its resources.", "desc13", null,tp);
			desc14 = lang.newText(new Coordinates(10, 480), "When all processes terminate, the algorithm will return true, which means the system is in a safe state.", "desc14", null,tp);
			desc15 = lang.newText(new Coordinates(10, 500), "If at some point during allocation there is not enough available resources to satisfy any of the remaining processes, the algorithm", "desc15",null,tp);
			desc16 = lang.newText(new Coordinates(10, 520), "will return false, which means the system is in an unsafe state.", "desc16", null,tp);		
		}
		

		lang.nextStep("Description");
		desc.hide();
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
		desc11.hide();
		desc12.hide();
		desc13.hide();
		if (desc14!=null) {
			desc14.hide();
			desc15.hide();
			desc16.hide();
		}

		
		ArrayProperties arrayProps = new ArrayProperties();
		arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
		arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
		arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW);
		
	    GridProperty gp = new GridProperty();
		gp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		gp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		gp.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		gp.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
		gp.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
		gp.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.GREEN);
		gp.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "MATRIX");

		
		SourceCodeProperties scProps = sourceCodeProps;
	    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
	        new Font("Monospaced", Font.PLAIN, 16));
		
	    SourceCode sc = lang.newSourceCode(new Coordinates(10, 60), "sourceCode",
		        null, scProps);
		    sc.addCodeLine("public boolean bankersAlgorithm(int[] availableResources, int[][] allocationMatrix, int[][] requestMatrix) {", "l1", 0, null);
		    sc.addCodeLine("calculateNeedMatrix(); // Need[i][j] = Request[i][j] - Allocation[i][j]", "l2", 2, null);
		    sc.addCodeLine("int nr_of_processes = needMatrix.length;", "l3", 2, null);
		    sc.addCodeLine("boolean[] terminated = new boolean[requestMatrix.length]", "l4", 2, null);
		    sc.addCodeLine("terminated.initializeTo(false);", "l5", 2, null);
		    sc.addCodeLine("int i = 0;", "l6", 2, null);
		    sc.addCodeLine("while (i < nr_of_processes) {", "l7", 2, null);
		    sc.addCodeLine("boolean allocationPossible = false;", "l8", 4, null);
		    sc.addCodeLine("for (int j = 0; j < nr_of_processes; j++) {", "l9", 4, null);
		    sc.addCodeLine("if (terminated[j] == false && checkRow(j) == true) {", "20", 6, null);
		    sc.addCodeLine("allocateResources(j);", "l21", 8, null);
		    sc.addCodeLine("terminated[j] = true;", "l22", 8, null);
		    sc.addCodeLine("allocationPossible = true;", "l23", 8, null);
		    sc.addCodeLine("i++;", "l24", 8, null);
		    sc.addCodeLine("}", "l25", 6, null);
		    sc.addCodeLine("}", "l26", 4, null);
		    sc.addCodeLine("if (allocationPossible == false) {", "l27", 4, null);
		    sc.addCodeLine("// unsafe state", "l28", 6, null);
		    sc.addCodeLine("return false;", "l29", 6, null);
		    sc.addCodeLine("}", "l30", 4, null);
		    sc.addCodeLine("}", "l31", 2, null);
		    sc.addCodeLine("// safe state", "l32", 2, null);
		    sc.addCodeLine("return true;", "l33", 2, null);
		    sc.addCodeLine("}", "l34", 0, null);
		        
		    SourceCode sc2 = lang.newSourceCode(new Coordinates(10, 550), "sourceCode2",
			        null, scProps);
		    sc2.addCodeLine("public boolean checkRow(int process) {", "l1", 0, null);
		    sc2.addCodeLine("for (int i = 0; i < availableResources.length; i++) {", "l2", 2, null);
		    sc2.addCodeLine("if (needMatrix[process][i] > availableResources[i]) {", "l3", 4, null);
		    sc2.addCodeLine("return false;", "l4", 6, null);
		    sc2.addCodeLine("}", "l5", 4, null);
		    sc2.addCodeLine("}", "l6", 2, null);
		    sc2.addCodeLine("return true", "l7", 2, null);
		    sc2.addCodeLine("}", "l8", 0, null);
		    
		    Text availableText = lang.newText(new Coordinates(850, 130), "Available resources", "AV_RES", null,tp);
		    Text requestText = lang.newText(new Coordinates(750, 250), "Request matrix", "REQ_MAT", null,tp);
		    Text allocateText = lang.newText(new Coordinates(850 + requestMatrix[0].length*25, 250), "Allocation matrix", "ALL_MAT", null,tp);
		    Text needText = lang.newText(new Coordinates(750, 350 + requestMatrix.length*25), "Need matrix", "NEED_MAT", null,tp);
		    
		   
		    Grid availableGrid = new Grid(new Coordinates(850, 180), availableResources.length,1 , 25, lang, gp);
		    Grid requestGrid = new Grid(new Coordinates(750, 300), requestMatrix[0].length, requestMatrix.length, 25, lang, gp);
			Grid allocationGrid = new Grid(new Coordinates(850 + requestMatrix[0].length*25, 300), allocationMatrix[0].length, allocationMatrix.length, 25, lang, gp);
			Grid needGrid = new Grid(new Coordinates(750, 400 + requestMatrix.length*25), requestMatrix[0].length, requestMatrix.length, 25, lang, gp);
			for (int i = 0; i < availableResources.length; i++) {
				availableGrid.setLabel(i, 0, Integer.toString(availableResources[i]));
			}
			String[] topStrings1 = new String[availableResources.length];
			for (int i = 0; i < topStrings1.length; i++) {
				topStrings1[i] = "A"+i;
			}
			String[] leftStrings = new String[allocationMatrix.length];
			for (int i = 0; i < leftStrings.length; i++) {
				leftStrings[i] = "P"+ i;
			}
			availableGrid.setCaptionTop(topStrings1);
			requestGrid.setCaptionTop(topStrings1);
			requestGrid.setCaptionLeft(leftStrings);
			allocationGrid.setCaptionTop(topStrings1);
			allocationGrid.setCaptionLeft(leftStrings);
			needGrid.setCaptionTop(topStrings1);
			needGrid.setCaptionLeft(leftStrings);
			
			for (int i = 0; i < requestMatrix.length; i++) {
				for (int j = 0; j < requestMatrix[0].length; j++) {
					requestGrid.setLabel(j, i, Integer.toString(requestMatrix[i][j]));
				}
			}
			for (int i = 0; i < allocationMatrix.length; i++) {
				for (int j = 0; j < allocationMatrix[0].length; j++) {
					allocationGrid.setLabel(j, i, Integer.toString(allocationMatrix[i][j]));
				}
			}
			lang.nextStep();
			calculateNeedMatrix();
			for (int i = 0; i < needMatrix.length; i++) {
				for (int j = 0; j < needMatrix[0].length; j++) {
					needGrid.setLabel(j, i, Integer.toString(needMatrix[i][j]));
					needGrid.highlightCell(j, i, Color.YELLOW, 0);
				}
			}

			sc.highlight(1);
			lang.nextStep();
			sc.unhighlight(1);
			needGrid.unhighlightAll(0);
			sc.highlight(2);
			lang.nextStep();
			sc.unhighlight(2);
			sc.highlight(3);
			Text booleanTerminate= lang.newText(new Coordinates(850 + requestMatrix[0].length*25, 350 + requestMatrix.length*25), "Terminated processes", "TERMINATED", null,tp);
			String[] terminatedString = new String[requestMatrix.length];
			//StringArray terminatedArray = lang.newStringArray(new Coordinates(850 + requestMatrix[0].length*25, 400 + requestMatrix.length*25), terminatedString, "terminatedarray", null,arrayProps);
			Grid booleanGrid = new Grid(new Coordinates(850 + requestMatrix[0].length*25, 400 + requestMatrix.length*25), requestMatrix.length, 1, 25, lang, gp);
			Text terminatedText  = lang.newText(new Coordinates(850 + requestMatrix[0].length*25, 470 + requestMatrix.length*25), "Termination order", "Termination order", null,tp);
			Grid terminatedGrid = new Grid(new Coordinates(850 + requestMatrix[0].length*25,520 + requestMatrix.length*25), requestMatrix.length, 1, 25, lang, gp);

			
			lang.nextStep();
			for (int i = 0; i < requestMatrix.length; i++) {
				booleanGrid.setLabel(i, 0, "F");
			}
			String[] processStrings = new String[requestMatrix.length];
			for (int i = 0; i < requestMatrix.length; i++) {
				processStrings[i] = "P"+i;
			}
			booleanGrid.setCaptionTop(processStrings);
			sc.unhighlight(3);
			sc.highlight(4);
			lang.nextStep();
			sc.unhighlight(4);
			sc.highlight(5);
			lang.nextStep();
				
		int nr_of_processes = needMatrix.length;
		int i = 0;
		int iteration = 1;
		while (i < nr_of_processes) {
			sc.unhighlight(5);
			sc.highlight(6);
			lang.nextStep(iteration+".Iteration");
			boolean allocationPossible = false;
			sc.unhighlight(6);
			sc.highlight(7);
			lang.nextStep();
			sc.unhighlight(7);
			sc.highlight(8);
			lang.nextStep();
			for (int j = 0; j < nr_of_processes; j++) {
				sc.unhighlight(8);
				sc.highlight(9);
				if (!terminated[j]) {
					booleanGrid.highlightCell(j, 0, Color.GREEN, 0);	
				}
				else {
					booleanGrid.highlightCell(j, 0, Color.RED, 0);
				}
				needGrid.highlightRow(j, Color.YELLOW, 0);
				requestGrid.highlightRow(j, Color.YELLOW, 0);
				allocationGrid.highlightRow(j, Color.YELLOW, 0);
				checkGrid(j, availableGrid,needGrid);
				//availableGrid.highlightCell(j, 0, Color.YELLOW, 0);
				for (int j2 = 0; j2 < 8; j2++) {
					sc2.highlight(j2);
				}
				if (i > 0 && requestMatrix.length>4 && !terminated[j] && checkRow(j) && blankscount<4 && (j == TF[0] || j == TF[1] || j == TF[2])) {
					blankscount++;
					String f = "FIB"+blankscount;
					FillInBlanksQuestionModel fib = new FillInBlanksQuestionModel(f);
					if (l.equals(Locale.GERMANY)) {
						fib.setPrompt("Geben Sie den available resource array (in geschweiften Klammern) nach der Allokation:");
					}
					else {
						fib.setPrompt("Enter the available resource array (in curly brackets) after allocation:");	
					}
					
					int temp;
					String answer = "{";
					for (int k = 0; k < availableResources.length; k++) {
						temp = availableResources[k] + requestMatrix[j][k] - needMatrix[j][k];
						if (k<availableResources.length-1) {
							answer = answer+temp+",";
						}
						else {
							answer = answer+temp;
						}
						
					}
					answer = answer+"}";
					//System.out.println(answer);
					fib.addAnswer(answer, 1, answer);
					lang.addFIBQuestion(fib);
				}
				
				lang.nextStep();
				availableGrid.unhighlightAll(0);
				booleanGrid.unhighlightCell(j, 0, 0);
				if (terminated[j] == true) {
					needGrid.highlightRow(j,Color.DARK_GRAY, 0);
					requestGrid.highlightRow(j,Color.DARK_GRAY, 0);
					allocationGrid.highlightRow(j,Color.DARK_GRAY, 0);
				}
				else {
					needGrid.unhighlightRow(j, 0);
					requestGrid.unhighlightRow(j, 0);
					allocationGrid.unhighlightRow(j, 0);
				}

				for (int j2 = 0; j2 < 8; j2++) {
					sc2.unhighlight(j2);
				}
				if (terminated[j] == false && checkRow(j) == true) {
					sc.unhighlight(9);
					sc.highlight(10);
					availableGrid.unhighlightAll(0);					
					allocateResources(j,availableGrid, needGrid, allocationGrid,requestGrid);
					lang.nextStep();
					sc.unhighlight(10);
					sc.highlight(11);

					int j2 = 0;
					for (int j3=0; j3 < terminated.length;j3++) {
						if (terminated[j3] == true) {
							j2++;
						}
						
					}
					booleanGrid.setLabel(j, 0, "T");					
					booleanGrid.highlightCell(j, 0, Color.YELLOW, 0);
					terminatedGrid.setLabel(j2, 0, "P"+j);
					terminatedGrid.highlightCell(j2, 0, Color.YELLOW, 0);
					terminatedString[j2] = "P"+j2;
					lang.nextStep();
					sc.unhighlight(11);
					sc.highlight(12);
					terminatedGrid.unhighlightCell(j2, 0, 0);;
					booleanGrid.unhighlightCell(j, 0, 0);
					terminatedGrid.unhighlightCell(j, 0, 0);
					lang.nextStep();
					terminated[j] = true;
					booleanGrid.unhighlightCell(j, 0, 0);
					terminatedGrid.unhighlightAll(0);
					sc.unhighlight(12);
					sc.highlight(13);
					lang.nextStep();
					allocationPossible = true;
					sc.unhighlight(13);
					sc.highlight(14);
					i++;
					//System.out.println("terminated process " + j);
				}
				sc.unhighlight(9);
				sc.unhighlight(14);
				sc.highlight(8);
				
				
				if(!TFanswered && requestMatrix.length>4 && (j == TF[0] || j == TF[1] || j == 6)){
					if (j == TF[0]) {
						requestGrid.highlightRow(TF[0]+1, Color.YELLOW, 0);
						needGrid.highlightRow(TF[0]+1, Color.YELLOW, 0);
						allocationGrid.highlightRow(TF[0]+1, Color.YELLOW, 0);
						availableGrid.highlightRow(0, Color.YELLOW, 0);
				    	TrueFalseQuestionModel allocate1 = new TrueFalseQuestionModel("allocate1");
				    	if (l.equals(Locale.GERMANY)) {
				    		allocate1.setPrompt("Der markierte Prozess kann in dieser Iteration terminieren.");
						}
				    	else {
				    		allocate1.setPrompt("The highlighted process can terminate in this iteration.");	
						}
				    	
				    	boolean tf1 = (terminated[(TF[0]+1)] == false && checkRow((TF[0]+1)) == true);
				    	allocate1.setCorrectAnswer(tf1);
				    	allocate1.setPointsPossible(1);
				    	allocate1.setNumberOfTries(3);
				    	lang.addTFQuestion(allocate1);
					}
					if (j == TF[1]) {
						requestGrid.highlightRow(TF[1]+1, Color.YELLOW, 0);
						needGrid.highlightRow(TF[1]+1, Color.YELLOW, 0);
						allocationGrid.highlightRow(TF[1]+1, Color.YELLOW, 0);
						availableGrid.highlightRow(0, Color.YELLOW, 0);
				    	TrueFalseQuestionModel allocate2 = new TrueFalseQuestionModel("allocate2");
				    	if (l.equals(Locale.GERMANY)) {
				    		allocate2.setPrompt("Der markierte Prozess kann in dieser Iteration terminieren.");
						}
				    	else {
				    		allocate2.setPrompt("The highlighted process can terminate in this iteration.");	
						}
				    	boolean tf2 = (terminated[(TF[1]+1)] == false && checkRow((TF[1]+1)) == true);
				    	allocate2.setCorrectAnswer(tf2);
				    	allocate2.setPointsPossible(1);
				    	allocate2.setNumberOfTries(3);
				    	lang.addTFQuestion(allocate2);
					}
					if (j == 6 && requestMatrix.length>6) {
						requestGrid.highlightRow(7, Color.YELLOW, 0);
						needGrid.highlightRow(7, Color.YELLOW, 0);
						allocationGrid.highlightRow(7, Color.YELLOW, 0);
						availableGrid.highlightRow(0, Color.YELLOW, 0);
				    	TrueFalseQuestionModel allocate3 = new TrueFalseQuestionModel("allocate3");
				    	if (l.equals(Locale.GERMANY)) {
				    		allocate3.setPrompt("Der markierte Prozess kann in dieser Iteration terminieren.");
						}
				    	else {
				    		allocate3.setPrompt("The highlighted process can terminate in this iteration.");	
						}
				    	boolean tf3 = (terminated[7] == false && checkRow(7) == true);
				    	allocate3.setCorrectAnswer(tf3);
				    	allocate3.setPointsPossible(1);
				    	allocate3.setNumberOfTries(3);
				    	lang.addTFQuestion(allocate3);
				    	TFanswered = true;
					}
		    	
				}
				lang.nextStep();
			}
			sc.unhighlight(8);
			sc.highlight(16);
			lang.nextStep();
			if (allocationPossible == false) {
				sc.unhighlight(17);
				sc.highlight(18);
				String deutschUnsafe = "Nicht jeder Prozess konnte seine angeforderte Resourcen bekommen. Das System ist in einem unsafe state!";
				String englishUnsafe = "Not every process can acquire its maxmimum resources. The system is in a unsafe state!";
				String unsafeStr = "";
				if (l.equals(Locale.GERMANY)) {
					unsafeStr = deutschUnsafe;
				}
				else {
					unsafeStr = englishUnsafe;
				}
				Text unsafeState = lang.newText(new Coordinates(950 + 25*(requestMatrix[0].length), 150), unsafeStr, "unsafestate", null,tp);
				Rect rect2 = lang.newRect(new Offset(-5, -5,unsafeState,"NW"), new Offset(5, 5, unsafeState, "SE"), "RECT", null,rp2);
				for (int k = 0; k < terminated.length; k++) {
					booleanGrid.highlightCell(k, 0, Color.RED, 0);
				}
				for (int k = 0; k < terminatedString.length; k++) {
					terminatedGrid.highlightCell(k, 0, Color.RED, 0);
				}
				lang.nextStep("Result");
				return false;
			}
			sc.unhighlight(16);
		sc.highlight(6);
		iteration++;
		}
		sc.unhighlight(6);
		sc.highlight(22);
		String deutschSafe = "Alle Prozesse sind terminiert. Das System ist in einem safe state!";
		String englishSafe = "All processes have terminated. The system is in a safe state!";
		String safeStr = "";
		if (l.equals(Locale.GERMANY)) {
			safeStr = deutschSafe;
		}
		else {
			safeStr = englishSafe;
		}
		Text safeState = lang.newText(new Coordinates(950 + 25*(requestMatrix[0].length), 150), safeStr, "safestate", null,tp);
		Rect rect3 = lang.newRect(new Offset(-5, -5,safeState,"NW"), new Offset(5, 5, safeState, "SE"), "RECT", null,rp);
		for (int k = 0; k < terminated.length; k++) {
			booleanGrid.highlightCell(k, 0, Color.GREEN, 0);
		}
		for (int k = 0; k < terminatedString.length; k++) {
			terminatedGrid.highlightCell(k, 0, Color.GREEN, 0);
		}
		lang.nextStep("Result");
		return true;
	}
    
    public void allocateResources(int process, Grid g, Grid g2, Grid g3, Grid g4) {
		for (int j = 0; j < availableResources.length; j++) {
			availableResources[j] = availableResources[j] + requestMatrix[process][j] - needMatrix[process][j];
			g.setLabel(j, 0, Integer.toString(availableResources[j]));
			g2.setLabel(j, process, Integer.toString(0));
			g3.setLabel(j, process, Integer.toString(0));
			g4.setLabel(j, process, Integer.toString(0));
			g2.highlightRow(process, Color.DARK_GRAY, 0);
			g3.highlightRow(process, Color.DARK_GRAY, 0);
			g4.highlightRow(process, Color.DARK_GRAY, 0);
		}
	}
    
    public void checkGrid(int process, Grid g, Grid g2){
		for (int i = 0; i < availableResources.length; i++) {
			if (needMatrix[process][i] > availableResources[i]) {
				g.highlightCell(i, 0, Color.RED, 0);
				if (!terminated[process]) {
					g2.highlightCell(i, process, Color.RED, 0);	
				}
				
			}
			else {
				g.highlightCell(i, 0, Color.GREEN, 0);
				if (!terminated[process]) {
					g2.highlightCell(i, process, Color.GREEN, 0);	
				}
				
			}
		}

	}
    
    public boolean checkRow(int process) {
		for (int i = 0; i < availableResources.length; i++) {
			if (needMatrix[process][i] > availableResources[i]) {
				return false;
			}
		}
		return true;
	}
    
    public void calculateNeedMatrix() {
		for (int i = 0; i < needMatrix.length; i++) {
			for (int j = 0; j < availableResources.length; j++) {
				needMatrix[i][j] = requestMatrix[i][j] - allocationMatrix[i][j];
				
			}
		}
	}
    
    public void init(){
        lang = new AnimalScript("Banker's algorithm", "Tomasz Gasiorowski", 800, 600);
        lang.setStepMode(true);
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
    	textProps = (TextProperties)props.getPropertiesByName("textProps");
        allocationMatrix = (int[][])primitives.get("allocationMatrix");
        sourceCodeProps = (SourceCodeProperties)props.getPropertiesByName("sourceCodeProps");
        int[] src = (int[])primitives.get("availableResources");
        availableResources = new int[src.length];
        System.arraycopy( src, 0, availableResources, 0, src.length );
        requestMatrix = (int[][])primitives.get("requestMatrix");
        bankersAlgorithm();
        lang.finalizeGeneration();
        return lang.toString();
    }

    public String getName() {
    	if (l.equals(Locale.GERMANY)) {
			return "Banker's algorithm [DE]";
		}
        return "Banker's algorithm [EN]";
    }

    public String getAlgorithmName() {
        return "Banker's algorithm";
    }

    public String getAnimationAuthor() {
        return "Tomasz Gasiorowski";
    }

    public String getDescription(){
    	if(l .equals(Locale.GERMANY)) {
			return
				"Der Banker's Algorithm wird von Betriebsystemen verwendet, um Deadlocks, die durch Allokation"
 +"\n"
 +"von shared Resourcen verursacht wurden, vorzubeugen. Er simuliert die Allokation von Resourcen in einem System,"
 +"\n"		
 +"basierend auf dem worst case Szenario, wo jeder Prozess immer seine maximale Anzahl an Resourcen anfordert."   	
 +"\n"   	
 +"Falls eine solche Allokationsreihenfolge existiert, ist das System in einem deadlock-freiem Zustand (safe state)."   	
 +"\n" 
 +"Ansonsten k�nnten Deadlocks auftreten (unsafe state)."   	
 +"\n"   	
 +"\n"
 +"Der Algorithmus nimmt 3 Eingabeparameter entgegen:"
 +"\n" 
 +"1. Request matrix: die maximale Anzahl an Resourcen, die jeder Prozess anfordern kann"   	
 +"\n"    	
 +"2. Allocation matrix: die Anzahl an Resourcen, auf die jeder Prozess zugreift"   	
 +"\n"   	
 +"3. Available resources: die Anzahl an Resourcen, die frei sind"   	
 +"\n"   	  	
 +"\n"   	
 +"Am Anfang berechnet der Algorithmus die Need matrix, die die verbleibende Anforderungen an Resourcen von jedem Prozess beschreibt:" 
 +"\n"
 +"Need[i][j]= Request[i][j] - Allocation[i][j]"   	
 +"\n"   	
 +"\n"
 +"Danach wird die Allokation von Resourcen ausgef�hrt. Der Algorithmus iteriert �ber alle Prozesse und bestimmt ob es genug verf�gbare"
 +"\n"
 +"Resourcen gibt, um den Need eines gegebenen Prozesses zu erf�llen. Falls ja, der Prozess wird als terminiert markiert und gibt alle"
 +"\n"
 +"allokierte Resourcen zur�ck. Wenn alle Prozesse terminiert sind gibt der Algorithmus true zur�ck, was bedeutet dass das System in einem"
 +"\n"
 +"safe state ist. Falls es zu irgendeinem Zeitpunkt der Allokation nicht genug Resourcen gibt, um einen der verbleibenden Prozesse zum Terminieren"   	
 +"\n"
 +"zu bringen, gibt der Algorithmus false zur�ck und das System ist in einem unsafe state.";   	  	
    	
    	}
    	else{
        return "The Banker's algorithm is used by operating systems in order to prevent deadlocks created by processes"
 +"\n"
 +"during resource allocation. It simulates the allocation of resources in a system"
 +"\n"
 +"based on the worst case scenario, where each process always requests their maximum amount"
 +"\n"
 +"of resources. If such an allocation order is possible, then the system is in a deadlock-free state (safe state),"
 +"\n"
 +"otherwise deadlocks might occur (unsafe state)."
 +"\n"
 +"\n"
 +"The algorithm takes 3 input variables:"
 +"\n"
 +"1. Request matrix: the maximum amount of resources each process could possibly request"
 +"\n"
 +"2. Allocation matrix: the amount of each resource each process is currently holding"
 +"\n"
 +"3. Available resources: the amount of resources the system currently has available"
 +"\n"
 +"\n"
 +"Given this information, the algorithm will start by calculating the Need matrix, which is the remaining resource"
 +"\n"
 +"need of each process:"
 +"\n"
 +"Need[i][j] = Request[i][j] - Allocation[i][j]"
 +"\n"
 +"\n"
 +"After this, the resource allocation will be attempted. The algorithm will iterate over all processes, checking if there"
 +"\n"
 +"are enough available resources to satisfy the Need value of the process. If yes, the process will"
 +"\n"
 +"be marked as terminated and it will return all of its resources. When all processes terminate, the algorithm "
 +"\n"
 +"will return true, which means the system is in a safe state. If at some point during allocation there is not enough"
 +"\n"
 +"available resources to satisfy any of the remaining processes, the algorithm will return false, which means the system"
 +"\n"
 +"is in an unsafe state.  ";}

    }

    public String getCodeExample(){
        return "public boolean bankersAlgorithm(int[] availableResources, int[][] allocationMatrix, int[][] requestMatrix) {"
 +"\n"
 +"        calculateNeedMatrix(); // Need[i][j] = Request[i][j] - Allocation[i][j]"
 +"\n"
 +"        int nr_of_processes = needMatrix.length;"
 +"\n"
 +"        boolean[] terminated = new boolean[requestMatrix.length];"
 +"\n"
 +"        terminated.initializeTo(\"false\");"
 +"\n"
 +"        int i = 0;"
 +"\n"
 +"        while (i < nr_of_processes) {"
 +"\n"
 +"                boolean allocationPossible = false;"
 +"\n"
 +"                for (int j = 0; j < nr_of_processes; j++) {"
 +"\n"
 +"                        if (terminated[j] == false && checkRow(j) == true) {"
 +"\n"
 +"                                allocateResources(j);"
 +"\n"
 +"                                terminated[j] = true;"
 +"\n"
 +"                                allocationPossible = true;"
 +"\n"
 +"                                i++;      "
 +"\n"
 +"                        }"
 +"\n"
 +"                }                "
 +"\n"
 +"                if (allocationPossible == false) {"
 +"\n"
 +"                         // unsafe state"
 +"\n"
 +"                         return false;"
 +"\n"
 +"                }"
 +"\n"
 +"        }"
 +"\n"
 +"        // safe state"
 +"\n"
 +"        return true;"
 +"\n"
 +"}        	";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return l;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

}