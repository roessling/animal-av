/*
 * SaintLagueAlgo.java
 * Dominik Rieder, Nicolas Schickert, 2017 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;

import algoanim.primitives.DoubleMatrix;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.AnimationType;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;
import algoanim.animalscript.AnimalScript;

public class SaintLagueAlgo implements Generator {
    private Language lang;
    private String[] parties;
    private int[] votes;
    private int seats;
    private StringArray beispiel;
    private StringArray beispiel1;
    private StringArray beispiel2;
    private MatrixProperties matrixProps;
    private SourceCodeProperties sourceProps;
/*
    public SaintLagueAlgo() {
        // Store the language object
        lang = l;
        // This initializes the step mode. Each pair of subsequent steps has to
        // be divdided by a call of lang.nextStep();
        lang.setStepMode(true);
	}*/
    
    private String description = "Das Sainte-Lague-Verfahren ist ein Algorithmus zur Verteilung von Sitzen in z.B einem Parlament aufgrund eines Wahlergebnisses.\nOder anders formuliert, "
    		 +"eine Umwandlung von Wählerstimmen in Abgeordnetenmandate.\nHierbei werden die Stimmen der Partei zunächst durch 0,5 geteilt und die Werte gespeichert.\n"
    		 +"Anschließend wird der Divisor um eins erhöht und alle Stimmen der Parteien werden wieder durch den Divisor geteilt und gespeichert.\nDiesen Vorgang wiederholt man maximal sooft, wie es Sitze zum verteilen gibt.\n"
    		 +"Den gespeicherten Werten werden Ränge zugeordnet und zwar dem höchsten Wert der 1. Rang dem zweit\nhöchsten Wert der 2. und so weiter.\n\nDabei wird ausser acht gelassen"
    		 +"mit welchem Divisor bei einem konkreten Wert gerechnet wurde.\nZuletzt bekommt jede Partei jeweils pro Rang einen Sitz.";
    
    private String fazit = "Das Saint Lague-Verfahren wird zur Verteilung von Sitzen in einem Parlament verwendet.\n"
    		+ "Anders als beim D'Hondt-Verfahren treten keine Verteilungsverzerrungen zu Gunsten großer Parteien auf.\n\n"
    		+ "Andere Verfahren zu Sitzverteilung sind das D'Hondt-Verfahren oder das Hare-Niemeyer-Verfahren.\n"
    		+ "Das Saint Lague-Verfahren wird beispielsweise bei der Bundestagswahl oder der StuPa-Wahl an der TU Darmstadt verwendet.";
    
    
	public void init(){
        lang = new AnimalScript("Saint Lague Verfahren", "Dominik Rieder, Nicolas Schickert", 800, 600);
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
        lang.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        parties = (String[])primitives.get("parties");
        votes = (int[])primitives.get("votes");
        seats = (Integer)primitives.get("seats");
        sourceProps  = (SourceCodeProperties) props.getPropertiesByName("sourceCodeProps");
        matrixProps  = (MatrixProperties) props.getPropertiesByName("matrixProps");
        sitdown(parties, votes, seats);
        return lang.toString();
    }

    public String getName() {
        return "Saint Lague Verfahren";
    }

    public String getAlgorithmName() {
        return "SaintLague";
    }

    public String getAnimationAuthor() {
        return "Dominik Rieder, Nicolas Schickert";
    }

    public String getDescription(){
        return "Das Sainte-Lague-Verfahren ist ein Algorithmus zur Verteilung von Sitzen in z.B einem Parlament aufgrund eines Wahlergebnisses. Oder anders formuliert, "
 +"\n"
 +"eine Umwandlung von Wählerstimmen in Abgeordnetenmandate. Hierbei werden die Stimmen der Partei zunächst durch 0,5 geteilt und die Werte gespeichert."
 +"\n"
 +"Anschließend wird der Divisor um eins erhöht und alle Stimmen der Parteien werden wieder durch den Divisor geteilt und gespeichert. Diesen Vorgang wiederholt man maximal sooft, wie es Sitze zum verteilen gibt."
 +"\n"
 +"Den gespeicherten Werten werden Ränge zugeordnet und zwar dem höchsten Wert der 1. Rang dem zweit höchsten Wert der 2. und so weiter. Dabei wird ausser acht gelassen"
 +"\n"
 +"mit welchem Divisor bei einem konkreten Wert gerechnet wurde. Zuletzt bekommt jede Partei jeweils pro Rang einen Sitz.";
    }

    public String getCodeExample(){
        return "	public static int[] saintLagueAlgo(String[] parties, int[] votes,int seats){"	//1
 +"\n"																								
 +"		float divisor = (float) 0.5; 			"													//2
 +"\n"
 +"		dividedValues = new float[seats][parties.length];"											//3
 +"\n"
 +"		for(int i = 0; i < seats; i++){					"											//4
 +"\n"
 +"			for(int j = 0; j < parties.length; j++){"												//5
 +"\n"
 +"				dividedValues[i][j] = votes[j]/divisor;"											//6
 +"\n"
 +"			}"																						//7
 +"\n"
 +"			divisor = divisor + 1;		"															//8
 +"\n"
 +"		}"																							//9
 +"\n"
 +"		int[] seatsPerParty = new int[parties.length];"												//10
 +"\n"
 +"		int counter = 0;"																			//11
 +"\n"
 +"		while(counter != seats){"																	//15
 +"\n"
 +"			int[] xy = findbiggest(dividedValues);"															//16
 +"\n"
 +"			dividedValues[xy[1]][xy[0]] = 0;"										//17
 +"\n"
 +"			biggestNumber = 0;"																		//18
 +"\n"
 +"			seatsPerParty[xy[0]]++;"														//19
 +"\n"
 +"			counter++;"																				//20
 +"\n"
 +"		}"																							//21
 +"\n"
 +"		return seatsPerParty;"																		//22
 +"\n"
 +"	}";																								//23
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.ENGLISH;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }
    
    public int[] sitdown(String[] parties, int[] votes,int seats){
    	
    	TextProperties t = new TextProperties();
    	t.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 20));
     	Text caption = lang.newText(new Coordinates(500,10), "Saint Lague Sitzverteilverfahren", "caption",null, t);

     	
        ArrayProperties arrayProps = new ArrayProperties();
        arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
        arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
        arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
        arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW); 
        
        MatrixProperties mP = new MatrixProperties();
        mP.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
        mP.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
        mP.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW); 
        mP.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table"); 
        matrixProps.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table"); 
    	
        
    	
    	SourceCodeProperties scProps = new SourceCodeProperties();
    	    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    	    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
    	        new Font("Monospaced", Font.PLAIN, 12));

    	    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    	    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

        	SourceCode descriptiontxt = lang.newSourceCode(new Coordinates(380, 50), "description", 
        			null, sourceProps);
        	String[] parts = description.split("\n");
        	for (String string : parts) {
    			descriptiontxt.addCodeLine(string, null, string.split("\\t").length, null);
    		}
        	lang.nextStep();
        	descriptiontxt.hide();
        	
    	    // now, create the source code entity
    	    SourceCode sc = lang.newSourceCode(new Coordinates(40, 70), "sourceCode",
    	        null, sourceProps);
    	parts = getCodeExample().split("\n");
    	for (String string : parts) {
			sc.addCodeLine(string, null, string.split("\\t").length, null);
		}
    	List<StringArray> a = new ArrayList<StringArray>();

    	//beispiel = lang.newStringArray(new Coordinates(800, 20), parties, "beispiel", null, arrayProps);

    	String d[][] = new String[seats+1][parties.length] ;
    	String spp[][] = new String[2][parties.length];
    	for(int n = 0; n < parties.length; n++){
    		d[0][n] = parties[n];
    		spp[0][n] = parties[n];
    		spp[1][n] = "0";
    	}
    	
    	
    	for(int m = 1; m < seats+1; m++){
    		for(int n = 0; n < parties.length; n++){
    			d[m][n] = "0.0000";
    		}
    	}
    
    	
    	
    	//DoubleMatrix dm = lang.newDoubleMatrix(new Coordinates(800, 40), d, "a", null);
    	StringMatrix sm = lang.newStringMatrix(new Coordinates(800, 60), d, "a", null, matrixProps);
    	StringMatrix sm2 = lang.newStringMatrix(new Coordinates(1300, 60), spp, "a", null, matrixProps);
    	
    	sc.highlight(0, 0, false);
    	lang.nextStep();
    	float divisor = (float) 0.7; 			//divisor wird initialisiert und im Verlauf immer um 1 erhöht
		float [][] dividedValues = new float[seats][parties.length];
		sc.unhighlight(0, 0, false);
		sc.highlight(1, 0, false);
		sc.highlight(2, 0, false);
		lang.nextStep();
		for(int i = 0; i < seats; i++){			//es muss sooft durch den divisor geteilt werden, wie es Sitze gibt	
			for(int j = 0; j < parties.length; j++){
			sc.unhighlight(1, 0, false);
			sc.unhighlight(7);
			sc.unhighlight(2, 0, false);
			sc.highlight(3, 0, false);
			sc.highlight(4, 0, false);
			lang.nextStep();
				dividedValues[i][j] = votes[j]/divisor;
				//dm.put(i, j, dividedValues[i][j], null, null);
				sm.put(i+1, j, String.format("%4.4f", dividedValues[i][j]), null, null);
				unhighlightall(sm, seats+1, parties.length);
				sm.highlightCell(i+1, j, null, null);
				sc.highlight(5);
				lang.nextStep();
			}
			sc.unhighlight(5);
			sc.unhighlight(4);
			sc.highlight(7);
			lang.nextStep();
			divisor = i==0?(float)0.5: divisor;
			divisor = divisor + 1;		//divisor wird um 1 erhöht
		}
		unhighlightall(sm, seats+1, parties.length);
		sc.unhighlight(3);
		sc.unhighlight(4);
		sc.unhighlight(5);
		sc.unhighlight(6);
		sc.unhighlight(7);
		sc.highlight(9);
		sc.highlight(10);
		lang.nextStep();
		
		
		int[] seatsPerParty = new int[parties.length];
		int counter = 0;
		
		sc.highlight(11);
		sc.unhighlight(9);
		sc.unhighlight(10);
		lang.nextStep();
		while(counter != seats){
			unhighlightall(sm, seats+1, parties.length);		
			sc.unhighlight(13);
			sc.unhighlight(14);
			sc.unhighlight(15);
			sc.unhighlight(16);
			sc.highlight(12);
			lang.nextStep();	
			unhighlightall(sm2, 2, parties.length);	
			int[] xy = findbiggest(dividedValues);
			sm.highlightCell(xy[1]+1, xy[0], null, null);
			sc.highlight(13);
			sc.highlight(14);
			sc.highlight(15);
			sc.highlight(16);
			sc.unhighlight(12);
			lang.nextStep();
			seatsPerParty[xy[0]]++;
			sm.put(xy[1]+1, xy[0], String.format("Sitz #%d", counter +1), null, null);
			sm2.highlightCell(1, xy[0], null, null);
			sm2.put(1, xy[0], String.format("%d", seatsPerParty[xy[0]]), null, null);
			counter++;
		}		
		unhighlightall(sm, seats +1, parties.length);		
		unhighlightall(sm2, 2, parties.length);		
		sc.unhighlight(11);
		sc.unhighlight(12);
		sc.unhighlight(13);
		sc.unhighlight(14);
		sc.unhighlight(15);
		sc.unhighlight(16);
		sc.highlight(18);
    	lang.nextStep();
    	
    	SourceCode fazittxt = lang.newSourceCode(new Coordinates(380, 50), "fazit", 
    			null, scProps);
    	parts = fazit.split("\n");
    	for (String string : parts) {
			fazittxt.addCodeLine(string, null, string.split("\\t").length, null);
		}
    	sm.hide();
    	sm2.hide();
    	sc.hide();
    	lang.nextStep();
    	MultipleChoiceQuestionModel q1 = new MultipleChoiceQuestionModel("q1");
    	q1.setPrompt("Wieviele Divisoren können maximal benötigt werden?");
    	q1.addAnswer("Man benötigt soviele Divisoren, wie Sitze vergeben werden.", 0, 
    			"Richtig!");
    	q1.addAnswer("Maximal wird die Hälfte der Sitze als Divisoren benötigt", 1, "Falsch, wenn alle Sitze an eine Partei gehen, benötigt man soviele Sitze wie Parteien.");
    	lang.addMCQuestion(q1);
    	
    	lang.nextStep();
    	
    	MultipleChoiceQuestionModel q2 = new MultipleChoiceQuestionModel("q2");
    	q2.setPrompt("Muss der erste Divisor größer oder kleiner (als 0,5) sein, um eine Hürde für kleinere Parteien zu schaffen?");
    	q2.addAnswer("Höher, zum Beispiel 0,6 1,5 2,5 ...", 0,
    			"richtig!");
    	q2.addAnswer("Niedriger, zum Beispiel 0,2 1,5 2,5 ...", 1, "Falsch!");
    	lang.addMCQuestion(q2);
    	
    	lang.nextStep();
    	
    	MultipleChoiceQuestionModel q3 = new MultipleChoiceQuestionModel("q3");
    	q3.setPrompt("Bei dem Saint Lague-Verfahren werden die Sitze fair nach den Stimmen verteilt.");
    	q3.addAnswer("Ja", 0
    			, "Richtig");
    	q3.addAnswer("Nein", 1, "Falsch!");
    	lang.addMCQuestion(q3);

    	lang.finalizeGeneration();
		return seatsPerParty;
    }
    
    public void unhighlightall(StringMatrix m, int lx, int ly){
    	for(int i = 0; i < lx; i++){
    		for(int j=0; j < ly; j++){
    			m.unhighlightCell(i, j, null, null);
    		}
    	}
    }
    
    public int[] findbiggest(float[][] dividedValues){
		float biggestNumber = 0;
		int valuePositionX = 3;
		int valuePositionY = 0;
		for(int y = 0; y < seats; y++){
			for(int x = 0; x < parties.length; x++){
				if(dividedValues[y][x] > biggestNumber){
					biggestNumber = dividedValues[y][x];
					valuePositionX = x;
					valuePositionY = y;
				}
			}
		}
		dividedValues[valuePositionY][valuePositionX] = 0;
		biggestNumber = 0;
		return new int[] {valuePositionX, valuePositionY};
    }
    
    
    /*
    public static void main(String[] args) {
        // Create a new language object for generating animation code
        // this requires type, name, author, screen width, screen height
        Language l = Language.getLanguageInstance(AnimationType.ANIMALSCRIPT,
            "Saint Lague", "Dominik Rieder, Nicolas Schickert", 640, 480);
        SaintLagueAlgo s = new SaintLagueAlgo(l);
        int[] a = { 7, 3, 2, 4, 1, 13, 52, 13, 5, 1 };
        String[] parties = {"SDS", "other"};
        int[] votes = {10, 11};
        s.sitdown(parties, votes, 31);
        System.out.println(l);
      }*/
}