/*
 * HoshenKopelman.java
 * Christopher Benz, Dennis Kuhn, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;

import algoanim.primitives.IntMatrix;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.FillInBlanksQuestionModel;
import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.TwoValueView;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CounterProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class HoshenKopelman implements Generator, ValidatingGenerator {
    private Language lang;
    private int[][] intMatrix;
    private MatrixProperties matrix;
    
    public static IntMatrix inputMatrix;
    public static IntMatrix originalMatrix;
    
    public static Text title;
    public static TextProperties titleProps;
    public static Rect titleFrame;
    public static RectProperties titleFrameProps;
    public static Text runText;
    public static TextProperties runTextProps;
    
    public static SourceCode description;
    public static SourceCode conclusion;
    
    public static SourceCode algoCode;
    public static SourceCodeProperties algoCodeProps;

    public void init(){
        lang = new AnimalScript("Hoshen-Kopelman", "Christopher Benz, Dennis Kuhn", 800, 600);    
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
    	intMatrix = (int[][])primitives.get("intMatrix");
    	matrix = (MatrixProperties)props.getPropertiesByName("matrix");
        
    	lang.setStepMode(true);
    	lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
       
    	runIntro(); 
       
    	inputMatrix = lang.newIntMatrix(new Coordinates(15, 75), intMatrix, "inputMatrix", null, matrix); //Matrix des Benutzers einlesen
       
    	addCounter(); //Z�hler aktivieren
       
    	runHoshenKopelman(); //Algorithmus ausf�hren
       
    	runOutro();
    	
    	lang.finalizeGeneration();
		 
    	return lang.toString();
    }
    
    public void runHoshenKopelman() {
    	runTextProps = new TextProperties();
    	runTextProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    	runText = lang.newText(new Coordinates(180, 75), "Der Algorithmus beginnt:", "runText", null, runTextProps);
    	
    	FillInBlanksQuestionModel clusterUnity = new FillInBlanksQuestionModel("twoNeighbors");
		
    	lang.nextStep("Start des Algorithmus");
    	
    	int largestLabel = 0; 
        for (int i=0; i < inputMatrix.getNrRows(); i++) {
 			for (int j=0; j < inputMatrix.getNrCols(); j++) {
 				
 				inputMatrix.highlightCell(i, j, null, null); 														//aktuelle Zelle immer anzeigen
 				if (occupied(inputMatrix.getElement(i, j))) { 														//nur Berechnungen ausf�hren wenn die Zelle besetzt ist
 															
 					boolean above;
 					if (i == 0) above = false;
 						else above = occupied(inputMatrix.getElement(i-1, j)); //Nachbar oben?
 					boolean left;
 					if (j == 0) left = false;
 						else left = occupied(inputMatrix.getElement(i, j-1)); //Nachbar links?						
 						
 					if ((left == false) && (above == false)) { 														//Kein Nachbar
 						runText.setText("Kein Nachbar links oder oben, neues Cluster anlegen!", null, null);
 						
 						algoCode.highlight(12);
 				    	algoCode.highlight(13);
 				    	 						
 						lang.nextStep();
 						
 						algoCode.unhighlight(12);
 				    	algoCode.unhighlight(13);
 						
 						largestLabel++;
 						inputMatrix.put(i, j, largestLabel, null, null);
 						inputMatrix.highlightElem(i, j, null, null);
 					}
 					else if (above == false) { 																		//Nachbar Links
 						runText.setText("Linker direkter Nachbar, zum Cluster hinzuf\u00fcgen!", null, null);

 						algoCode.highlight(15);
 				    	algoCode.highlight(16);
 				    	
 						lang.nextStep();
 						
 						algoCode.unhighlight(15);
 				    	algoCode.unhighlight(16);
 				    	
 						inputMatrix.put(i, j, inputMatrix.getElement(i, j-1), null, null);
 						inputMatrix.highlightElem(i, j, null, null);
 					}
 					else if (left == false) { 																		//Nachbar oben
 						runText.setText("Oberer direkter Nachbar, zum Cluster hinzuf\u00fcgen!", null, null);
 						
 						algoCode.highlight(18);
 				    	algoCode.highlight(19);
 				    	
 						lang.nextStep();
 						
 						algoCode.unhighlight(18);
 				    	algoCode.unhighlight(19);
 				    	 	
 						inputMatrix.put(i, j, inputMatrix.getElement(i-1, j), null, null);
 						inputMatrix.highlightElem(i, j, null, null);
 					}
 					else {																							//Nachbar links und oben
 						runText.setText("Nachbarn links und oben, Cluster der Nachbarn verbinden!", null, null);
 						
 						algoCode.highlight(21);
 						algoCode.highlight(22);
 				    	algoCode.highlight(23);
 				    	algoCode.highlight(24);
 				    	algoCode.highlight(25);
 				    	algoCode.highlight(26);
 				    	
 				    	double askQuestion = Math.random();			// Frage nur zu zu einem bestimmten Prozentsatz stellen
 				    	if (askQuestion > 0.4) {	
 				    	clusterUnity.setPrompt("Zu welcher Nummerierung werden beide Cluster vereint?");
 				    	clusterUnity.addAnswer(String.valueOf(Math.min(inputMatrix.getElement(i, j-1), inputMatrix.getElement(i-1, j))) , 1, "Richtig!");
 				    	lang.addFIBQuestion(clusterUnity);
 				    	}
 				    	
 						lang.nextStep();

 						algoCode.unhighlight(21);
 						algoCode.unhighlight(22);
 				    	algoCode.unhighlight(23);
 				    	algoCode.unhighlight(24);
 				    	algoCode.unhighlight(25);
 				    	algoCode.unhighlight(26);
 				    	
 						if ((inputMatrix.getElement(i, j-1) != inputMatrix.getElement(i-1, j))) {
 							largestLabel--;
 							if ((inputMatrix.getElement(i, j-1) < inputMatrix.getElement(i-1, j)))  largestLabel++;
 							int maxLabel = Math.max(inputMatrix.getElement(i, j-1), inputMatrix.getElement(i-1, j));
 							int minLabel = Math.min(inputMatrix.getElement(i, j-1), inputMatrix.getElement(i-1, j));
 							for (int x=0; x <= i; x++) {
 								for (int y=0; y < inputMatrix.getNrCols(); y++) {
 								
 									if (inputMatrix.getElement(x, y) == maxLabel)
 										inputMatrix.put(x, y, minLabel, null, null);
 								}
 							}
 						}
 							inputMatrix.put(i, j, inputMatrix.getElement(i-1, j), null, null);
 							inputMatrix.highlightElem(i, j, null, null);	
 					} 
 				}
 				
 				else runText.setText("Zelle ist nicht besetzt, springe zur n\u00e4chsten!", null, null);
 							
 				lang.nextStep();
 				inputMatrix.unhighlightCell(i, j, null, null);
 				}			
 			}
        lang.nextStep();
        inputMatrix.unhighlightCell(inputMatrix.getNrRows()-1, inputMatrix.getNrCols()-1, null, null);
        runText.setText("Cluster-Erkennung beendet!", null, null);
    }
    	
    public void addCounter() {
    	TwoValueCounter counter = lang.newCounter(inputMatrix); 
    	CounterProperties cp = new CounterProperties(); 
    	cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true); 
    	cp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    	
    	@SuppressWarnings("unused")
      TwoValueView view = lang.newCounterView(counter,
    			new Offset(20, 0, title, AnimalScript.DIRECTION_E), cp, true, false);
    }
	
	public static boolean occupied(int x) {
		if (x > 0)
		return true;
		else return false;
	}

    public String getName() {
        return "Hoshen-Kopelman";
    }

    public String getAlgorithmName() {
        return "Hoshen-Kopelman Algorithm";
    }

    public String getAnimationAuthor() {
        return "Christopher Benz, Dennis Kuhn";
    }

    public String getDescription(){
        return "Der Hoshen-Kopelman Algorithmus ist ein Algorithmus zur Cluster-Erkennung in Matrizen. Er erh�lt als Input eine Matrix mit besetzten(1) und unbesetzten(0) Feldern, durchl�uft diese und gibt die Matrix mit Benennung der einzelnen Cluster wieder aus.";
    }

    public String getCodeExample(){
        return "Methodik der Cluster-Erkennung:"
 +"\n"
 +"\n"
 +"1. Ist die Aktuelle Zelle besetzt?"
 +"\n"
 +"\n"
 +"2. Hat sie besetzte Nachbarn links oder oben?"
 +"\n"
 +"\n"
 +"3a. (Keine) "
 +"\n"
 +"-> F�ge Zelle zu neuem Cluster hinzu "
 +"\n"
 +"\n"
 +"3b. (links)"
 +"\n"
 +"-> F�ge Zelle zu Cluster des Nachbarn hinzu"
 +"\n"
 +"\n"
 +"3c. (oben)"
 +"\n"
 +"-> F�ge Zelle zu Cluster des Nachbarn hinzu"
 +"\n"
 +"\n"
 +"4c (links UND oben)"
 +"\n"
 +"-> F�ge Zelle zu Cluster des Nachbarn mit der dem niedrigeren label hinzu und f�ge alle Zellen des h�heren labels auch hinzu. Zwei Cluster werden zu einem mit einheitlicher Benennung.";
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
        return Generator.JAVA_OUTPUT;
    }
    
    public void runIntro() {
    	titleProps = new TextProperties();
    	titleProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    	titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 24));
    	title = lang.newText(new Coordinates(200, 10), "Hoshen-Kopelman Algorithmus", "Title", null, titleProps);
    	titleFrameProps = new RectProperties();
    	titleFrameProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    	titleFrame = lang.newRect(new Offset(-2, -2, title, AnimalScript.DIRECTION_NW), new Offset(2, 2, title, AnimalScript.DIRECTION_SE), "titleFrame", null, titleFrameProps);
    	    	   	
    	lang.nextStep("Einleitung");
    	
    	String intro = "Der Hoshen-Kopelman Algorithmus dient zur Cluster-Erkennung in Matrizen mit besetzten und unbesetzten Feldern."
    					+ "\n"
    					+ "Er durchl\u00e4uft die Matrix Zelle f\u00fcr Zelle und gruppiert die besetzten Felder zu Clustern. Besetzte Felder"
    					+ "\n"
    					+ "geh\u00f6ren zum gleichen Cluster wenn sie direkte Nachbarn sind, also entweder links/rechts oder oben/unten"
    					+ "\n"
    					+ "aneinander liegen. Diagonale Nachbarn geh\u00f6ren NICHT zum gleichen Cluster."
    					+ "\n"
    					+ "Die Cluster werden fortlaufend nummeriert und am Ende in Form der Matrix wieder ausgegeben";
    	
    	description = lang.newSourceCode(new Coordinates(40, 60), "0", null);
    	description.addMultilineCode(intro, "0", null);
    	   	
    	lang.nextStep();
    	
    	description.hide();
    	
    	algoCodeProps = new SourceCodeProperties();
    	algoCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.BLUE);
 
    	algoCode = lang.newSourceCode(new Coordinates(510, 75), "algoCode", null, algoCodeProps);
    	
    	algoCode.addCodeLine("public void hoshenKopelman(int[][] grid)", "", 0, null);
    	algoCode.addCodeLine("","", 0, null);
    	algoCode.addCodeLine("        for (int i=0; i < grid.length; i++)", "", 0, null);
    	algoCode.addCodeLine("        for (int j=0; j < grid[i].length; j++)", "", 0, null);
    	algoCode.addCodeLine("            if (occupied(grid[i][j]))", "", 0, null);
    	algoCode.addCodeLine("                boolean above;", "", 0, null);
    	algoCode.addCodeLine("                if (i == 0) above = false;", "", 0, null);
    	algoCode.addCodeLine("                    else above = occupied(grid[i-1][j]);", "", 0, null);
    	algoCode.addCodeLine("                boolean left;", "", 0, null);
    	algoCode.addCodeLine("                if (j == 0) left = false;", "", 0, null);
    	algoCode.addCodeLine("                    else left = occupied(grid[i][j-1]);", "", 0, null);
    	algoCode.addCodeLine("","", 0, null);
    	algoCode.addCodeLine("                if ((left == false) && (above == false))", "", 0, null);
    	algoCode.addCodeLine("                    grid[i][j] = largestLabel;", "", 0, null);
    	algoCode.addCodeLine("","", 0, null);
    	algoCode.addCodeLine("                else if (above == false)", "", 0, null);
    	algoCode.addCodeLine("                    grid[i][j] = grid[i][j-1];", "", 0, null);
    	algoCode.addCodeLine("","", 0, null);
    	algoCode.addCodeLine("                else if (left == false)", "", 0, null);
    	algoCode.addCodeLine("                    grid[i][j] = grid[i-1][j];", "", 0, null);
    	algoCode.addCodeLine("","", 0, null);
    	algoCode.addCodeLine("                else", "", 0, null);
    	algoCode.addCodeLine("                    for (int x=0; x <= i; x++)", "", 0, null);
    	algoCode.addCodeLine("                    for (int y=0; y <= j; y++)", "", 0, null);
    	algoCode.addCodeLine("                        if (grid[x][y] == Math.max(grid[i][j-1], grid[i-1][j])", "", 0, null);
    	algoCode.addCodeLine("                            grid[x][y] = Math.min(grid[i][j-1], grid[i-1][j]];", "", 0, null);
    	algoCode.addCodeLine("                        grid[i][j] = grid[i-1][j];", "", 0, null);
 
    }
    
    public void runOutro() {
    	lang.nextStep("Zusammenfassung");
    	
    	algoCode.hide();
    	runText.hide();
    	
    	originalMatrix = lang.newIntMatrix(new Coordinates(200, 75), intMatrix, "originalMatrix", null, matrix);
    	
    	String outro = "Hier das Ergebnis des Hoshen-Kopelman Algorithmus."
				+ "\n"
				+ "Rechts die urspr\u00fcngliche Matrix und links die Matrix nach der Cluster-Erkennung"
				+ "\n"
				+ "Der Algrotithmus wird z.B. bei der Segmentierung und Clusterung von bin\u00e4ren Bildern verwendet.";
				

    	conclusion = lang.newSourceCode(new Offset(0, 10, inputMatrix, AnimalScript.DIRECTION_SW), "0", null);
    	conclusion.addMultilineCode(outro, "0", null);
    }
    
    @Override
    public boolean validateInput(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) 
    				throws IllegalArgumentException {
    	
    	try {
    		intMatrix = (int[][])primitives.get("intMatrix");
    		for(int i = 0; i < intMatrix.length; i++) {
    			for(int j = 0; j < intMatrix[i].length; j++) {
    				if ((intMatrix[i][j] < 0) || (intMatrix[i][j] > 1)) throw new IllegalArgumentException();
    			}
    		}
    		return true;
    	}
    	catch (Exception e) {
    		return false;
    	}
    }
    
//    public static void main (String[] args) {
//    	Generator generator = new HoshenKopelman();
//    	Animal.startGeneratorWindow(generator);
//    }

}