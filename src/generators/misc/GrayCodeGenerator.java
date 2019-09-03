/*
 * GrayCodeGenerator.java
 * Simon Schmidt, 2017 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class GrayCodeGenerator implements Generator {
    private Language lang;
    private ArrayProperties arrayProps;
    private int[] intArray;
    private TextProperties textProps;
    private Text header;
    private static final String SOURCE_CODE     = "public void intToGray(int[] a)"             
    	      + "\n{"  
    	      + "\n  int[] gray=new int[a.length];"
    	      + "\n  int current, int shiftedCurrent, result;"
    	      + "\n for(int i=0; i< a.length; i++)"                                                                               
    	      + "\n{"                                                                                                                
    	      + "\n    current=a[i];"                                                                               
    	      + "\n    shiftedCurrent=current>>1;"                                                                 
    	      + "\n    result=current^shiftedCurrent; "                                                                                               
    	      + "\n    gray[i]=result;"
    	      + "\n  }"                                                                                                 
    	      + "\n}";                                                                                                  

    
    public void init(){
        lang = new AnimalScript("Gray Code", "Simon Schmidt", 800, 600);
        lang.setStepMode(true);
    	lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

        TextProperties headerProps=new TextProperties();
        headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.BOLD, 24));
        
        header=lang.newText(new Coordinates(20,20), "Gray Code", "header", null, headerProps);
        textProps = new TextProperties();
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
            Font.SANS_SERIF, Font.PLAIN, 16));
        lang.newText(new Coordinates(10, 100),
            "Bei Gray Code handelt es sich um einen Code, bei dem sich jedes Codewort",
            "description1", null, textProps);
        lang.newText(new Offset(0, 25, "description1",
            AnimalScript.DIRECTION_NW),
            "nur in einer Stelle vom vorherigen unterscheidet.",
            "description2", null, textProps);
        lang.newText(new Offset(0, 25, "description2",
            AnimalScript.DIRECTION_NW),
            "Diese Eigenschaft wird verwendet, um bei mehradrigen Kabelverbindungen Zwischenzustände",
            "description3", null, textProps);
        lang.newText(new Offset(0, 25, "description3",
            AnimalScript.DIRECTION_NW),
            "zu vermeiden, die auftreten wenn die einzelnen Leitungen, die an einer Zustandsänderung ",
            "description4", null, textProps);
        
        lang.newText(new Offset(0, 25, "description4",
                AnimalScript.DIRECTION_NW),
                "beteiligt sind, nicht komplett synchron umschalten. Gray Code verhindert dies, indem",
                "description5", null, textProps);
        lang.newText(new Offset(0, 25, "description5",
                AnimalScript.DIRECTION_NW),
                "immer nur eine Leitung verändert wird. ",
                "description6", null, textProps);
        lang.newText(new Offset(0, 25, "description6",
                AnimalScript.DIRECTION_NW),
                "Um für eine beliebige Zahl X die Darstellung im Gray Code zu erhalten, ",
                "description7", null, textProps);
        lang.newText(new Offset(0, 25, "description7",
                AnimalScript.DIRECTION_NW),
                "geht man wie folgt vor:",
                "description8", null, textProps);
        lang.newText(new Offset(0, 25, "description8",
                AnimalScript.DIRECTION_NW),
                "1. X in Binärdarstellung umwandeln ",
                "description9", null, textProps);
        lang.newText(new Offset(0, 25, "description9",
                AnimalScript.DIRECTION_NW),
                "2. Y durch Shift nach rechts um 1 von X erhalten",
                "description10", null, textProps);
        lang.newText(new Offset(0, 25, "description10",
                AnimalScript.DIRECTION_NW),
                "3. X und Y mit XOR Verknüpfen; das Ergebnis ist die Gray Code Darstellung von X",
                "description11", null, textProps);
        lang.nextStep();
        lang.hideAllPrimitivesExcept(header);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {

        arrayProps = (ArrayProperties)props.getPropertiesByName("arrayProps");
        arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
		arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
		arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW);
        intArray = (int[])primitives.get("intArray");
        IntArray iA= lang.newIntArray(new Coordinates(20,90), intArray, "ints",null, arrayProps);
        lang.nextStep();
        arrayToGray(iA);
        lang.hideAllPrimitivesExcept(header);
        lang.newText(new Coordinates(10, 100),
                "Um eine Zahl im Gray Code in eine Dezimalzahl umzurechnen, betrachtet man die Binärzahl  ",
                "description1", null, textProps);
            lang.newText(new Offset(0, 25, "description1",
                AnimalScript.DIRECTION_NW),
                " von links nach rechts und geht man wie folgt vor: ",
                "description2", null, textProps);
            lang.newText(new Offset(0, 25, "description2",
                    AnimalScript.DIRECTION_NW),
                    "1. Beginne bei der höchstwertigen Eins und verwerfe alle Stellen davor (links davon)",
                    "description3", null, textProps);
            lang.newText(new Offset(0, 25, "description3",
                    AnimalScript.DIRECTION_NW),
                    "2. Bestimme die höchste Zahl X_n, die sich mit der verbleibenden Bitbreite darstellen lässt",
                    "description4", null, textProps);
            lang.newText(new Offset(0, 25, "description4",
                    AnimalScript.DIRECTION_NW),
                    "3. Entferne die Eins und Wiederhole die Schritte 1 und 2 bis nurnoch Nullen übrig sind ",
                    "description5", null, textProps);
            lang.newText(new Offset(0, 25, "description5",
                    AnimalScript.DIRECTION_NW),
                    "4. Subtrahiere alle X_n voneinander; das Ergebnis ist die Dezimaldarstellung der Ursprungszahl",
                    "description6", null, textProps);
            lang.finalizeGeneration();

        return lang.toString();
    }
    /**
     * transforms an array of ints into Gray Code
     * 
     * @param ints the array to be transformed into Gray Code
     */
    private void arrayToGray(IntArray ints){
    	StringArray sA= lang.newStringArray(new Coordinates(20,260), new String[ints.getLength()], "gray",null, arrayProps);
    	
    	ArrayMarkerProperties currentP = new ArrayMarkerProperties();
		currentP.set(AnimationPropertiesKeys.SHORT_MARKER_PROPERTY, true);
		currentP.set(AnimationPropertiesKeys.LABEL_PROPERTY, "current");
		//create and display code
    	SourceCodeProperties scProps = new SourceCodeProperties();
    	scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
	    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
	        new Font("Monospaced", Font.PLAIN, 12));
	    
	    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
	    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
	    SourceCode sc = lang.newSourceCode(new Coordinates(200, 90), "sourceCode",
	            null, scProps);
	    sc.addCodeLine("public void intToGray(int[] a)", null, 0, null);
	    sc.addCodeLine("{", null, 0, null);
	    sc.addCodeLine("int[] gray=new int[a.length];", null, 1, null);
	    sc.addCodeLine("int current, int shiftedCurrent, result;", null, 1, null);
	    sc.addCodeLine("for(int i=0; i< a.length; i++)", null, 1, null);
	    sc.addCodeLine("{", null, 1, null);
	    sc.addCodeLine("current=a[i];", null, 2, null);
	    sc.addCodeLine("shiftedCurrent=current>>1;", null, 2, null);
	    sc.addCodeLine("result=current^shiftedCurrent;", null, 2, null);
	    sc.addCodeLine("gray[i]=result;", null, 2, null);
	    sc.addCodeLine("}", null, 1, null);
	    sc.addCodeLine("}", null, 0, null);
    	lang.nextStep();
    	//set array markers
    	ArrayMarker currentInt = lang.newArrayMarker(ints, 0, "current", null, currentP);
		ArrayMarker currentGray = lang.newArrayMarker(sA, 0, "current", null, currentP);
		sc.highlight(4);
		
    	Text binaryRep=lang.newText(new Coordinates(20,160), "" , "binaryRep", null);
    	
    	Text shiftedBinary=lang.newText(new Coordinates(20,190), "" , "shiftedBinary", null);
    	
    	
		for(int i=0; i< intArray.length;i++){
			//Set markers and highlight current positions
			currentInt.move(i, null, null);
			currentGray.move(i, null, null);
			ints.unhighlightElem(currentInt.getPosition()-1, null, null);
			sA.unhighlightElem(currentGray.getPosition()-1, null, null);
			ints.highlightElem(currentInt.getPosition(), null, null);
			sA.highlightElem(currentGray.getPosition(), null, null);
			lang.nextStep();
			sc.unhighlight(4);
			
			int current=ints.getData(i);
			
			int res=current^(current>>1);
			binaryRep.setText("Bin\u00e4r:         "+Integer.toBinaryString((1 << getWidth()) | current ).substring( 1 ), null, null);
			sc.highlight(6);
			lang.nextStep();
			shiftedBinary.setText("Nach Shift: "+Integer.toBinaryString((1 << getWidth()) | current>>1 ).substring( 1 ), null, null);
			sc.unhighlight(6);
			sc.highlight(7);
			lang.nextStep();
			String binary=Integer.toBinaryString( (1 << getWidth()) | res ).substring( 1 );
			sc.unhighlight(7);
			sc.highlight(8);
			sc.highlight(9);
			sA.put(i, binary, null, null);
			lang.nextStep();
			sc.unhighlight(8);
			sc.unhighlight(9);
			sc.highlight(4);
		}
		MultipleChoiceQuestionModel q1 = new MultipleChoiceQuestionModel("MC1");
        q1.setPrompt("Ist Gray Code linear?");
   
        q1.addAnswer("Ja", 1, "Richtig!");
        q1.addAnswer("Nein", 0, "Falsch!");
       
   
        q1.setNumberOfTries(1);
   
        lang.addMCQuestion(q1);
        lang.nextStep();
		
	}
    /**
     * Calculate the maximum width
     *  
     * @return the width of the highest value in the array
     */
    private int getWidth(){
    	int max=Integer.MIN_VALUE;
    	for(int i=0; i<intArray.length;i++){
    		if(intArray[i]>max) max=intArray[i];
    	}
    	return (int) Math.ceil((Math.log(max)/Math.log(2.0))+0.1);
    }
    public String getName() {
        return "Gray Code";
    }

    public String getAlgorithmName() {
        return "Gray Code";
    }

    public String getAnimationAuthor() {
        return "Simon Schmidt";
    }

    public String getDescription(){
        return "Bei Gray Code handelt es sich um einen Code, bei dem sich benachbarte Codewörter nur in einer"
 +"\n"
 +"Stelle unterscheiden. Hier wird entweder eine oder mehrere Ganzzahlen zu Gray Code umgewandelt.";
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
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }

}
