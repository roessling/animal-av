/*
 * ChiSquareGen.java
 * Karol Gotkowski, 2017 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;

import algoanim.primitives.generators.AnimationType;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.MultipleSelectionQuestionModel;
import interactionsupport.models.TrueFalseQuestionModel;
import algoanim.animalscript.AnimalScript;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import algoanim.primitives.Circle;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.AnimationType;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CircleProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import algorithm.animalTranslator.codeItems.Node;
import algorithm.animalTranslator.codeItems.Timing;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.ListIterator;

import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;

import algoanim.primitives.Polyline;
import algoanim.primitives.generators.Language;
import algoanim.util.Coordinates;

public class ChiSquareGen implements ValidatingGenerator {
    private Language lang;
    private int[] histogram1;
    private int[] histogram2;       
    private RectProperties histogram2Color;    
    private RectProperties histogram1Color;
    private RectProperties newHistogramRecColor3;
    private RectProperties newHistogramRecColor1;
    private RectProperties newHistogramRecColor2;

    public void init(){
        lang = new AnimalScript("Chi-Square Histogramm Vergleich", "Karol Gotkowski", 800, 600);
    }
    
    @Override
    public boolean validateInput(AnimationPropertiesContainer arg0, Hashtable<String, Object> arg1)
    		throws IllegalArgumentException {
    	boolean valid = true;
        histogram1 = (int[])arg1.get("histogram1");
        histogram2 = (int[])arg1.get("histogram2");
        
    	if (histogram1.length < 10) {
    		valid = false;
    		throw new IllegalArgumentException("Ein Histogramm darf nicht weniger als 10 Werte enthalten!");
    	}
    	if (histogram1.length > 20) {
    		valid = false;
    		throw new IllegalArgumentException("Ein Histogramm darf nicht mehr als 20 Werte enthalten!");
    	}
    	if (histogram1.length != histogram2.length) {
    		valid = false;
    		throw new IllegalArgumentException("Die Histogramme müssen gleich gross sein!");
    	}
    	
    	for (int i = 0; i < histogram1.length; i++) {
    		if (histogram1[i] < 0 || histogram2[i] < 0) {
    			valid = false;
    			throw new IllegalArgumentException("Die Histogramme dürfen keine negativen Werte enthalten!");
    		}
    		if (histogram1[i] == 0 && histogram2[i] == 0) {
    			valid = false;
    			throw new IllegalArgumentException("Der jeweils i-te Wert beider Histogramme dürfen nicht beide zusammen 0 sein!");
    		}
    	}
    	
    	return valid;
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        histogram1 = (int[])primitives.get("histogram1");
        histogram2 = (int[])primitives.get("histogram2");      
        histogram2Color = (RectProperties)props.getPropertiesByName("histogram2Color");        
        histogram1Color = (RectProperties)props.getPropertiesByName("histogram1Color");
        newHistogramRecColor3 = (RectProperties)props.getPropertiesByName("newHistogramRecColor3");
        newHistogramRecColor1 = (RectProperties)props.getPropertiesByName("newHistogramRecColor1");
        newHistogramRecColor2 = (RectProperties)props.getPropertiesByName("newHistogramRecColor2");
        
        ChiSquare chiSquareObject = new ChiSquare(lang);
        chiSquareObject.chiSquare(histogram1, histogram2);       
        
        return lang.toString();
    }

    public String getName() {
        return "Chi-Square Histogramm Vergleich";
    }

    public String getAlgorithmName() {
        return "Chi-Square";
    }

    public String getAnimationAuthor() {
        return "Karol Gotkowski";
    }

    public String getDescription(){
        return "Ein Problem der Computer Vision ist das Erkennen von Objekten auf Bildern."
 +"\n"
 +"Eine Möglichkeit dieses Problem zu lösen (Wenn auch nicht die beste), ist der Chi-Square Histogramm Vergleich."
 +"\n"
 +"Für den Chi-Square Histogramm Vergleich werden zwei Bilder benötigt."
 +"\n"
 +"Einmal das zu analysierende Bild auf dem ein bestimmtes Objekt gesucht wird und einmal ein Bild des gesuchten Objektes."
 +"\n"
 +"Die Grauwerte beider Bilder werden nun als Histogramm dargestellt und mittels der Chi-Square Formel verglichen."
 +"\n"
 +"Diese lautet wie folgt: \u03A3((q\u1D62 - v\u1D62)² / (q\u1D62 + v\u1D62))"
 +"\n"
 +"Wobei q\u1D62 der i-te Balken aus dem ersten Histogramm ist und v\u1D62 der i-te Balken aus dem zweiten Histogramm."
 +"\n"
 +"Die Chi-Square Formel bestimmt eine gewichtete Distanz zwischen beiden Bildern."
 +"\n"
 +"Ist die Distanz unterhalb eines gewissen Schwellenwertes befindet sich höchst wahrscheinlich das gesuchte Objekt auf dem analysierten Bild."
 +"\n"
 +"Der Schwellenwert muss je nach Anwendungsszenario definiert werden, so dass die Anwendung optimale Ergebnisse erzielt.";
    }

    public String getCodeExample(){
        return "public void chiSquare(int[] histogram1, int[] histogram2) {"
 +"\n"
 +"   int distance;"
 +"\n"
 +"   int squaredDistance;"
 +"\n"
 +"   int weightedDistance;"
 +"\n"
 +"   int chiSquare = 0;"
 +"\n"
 +"\n"
 +"   for (int i = 0; i < histogram1.length; i++) {"
 +"\n"
 +"      distance = histogram1[i] - histogram2[i];  "
 +"\n"
 +"      squaredDistance = distance * distance;"
 +"\n"
 +"      weightedDistance = squaredDistance / (histogram1[i] + histogram2[i]);"
 +"\n"
 +"      chiSquare += weightedDistance;"
 +"\n"
 +"   }"
 +"\n"
 +"}";
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



private class ChiSquare {
	
	private Language lang;
	private int distance = 0;
	private int squaredDistance;
	private int weightedDistance;
	private int chiSquare = 0;
	private Rect rectangle1; 
	private Rect rectangle2; 
	private Rect rectangle3; 
	private int columnGap = 40;
	private String text;
	private Text animText;
	private Text[] algoTextArray = new Text[3];
	private AnimalText animalText;
	private AnimalArrow animalArrow;
	private int startAxesX = 100;
	private int endAxesX;
	private int histogramFactor = 5;
    
	public ChiSquare(Language lang) {
		this.lang = lang;	
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
	}
    
    public void chiSquare(int[] histogram1, int[] histogram2) {
    	lang.setStepMode(true);
    	endAxesX = startAxesX + 40 * histogram1.length + 50;
    	animalText = new AnimalText(lang, new Coordinates(endAxesX,540));    	
    	printDescription();
    	lang.nextStep();
    	hideDescription();   	
    	printHistogramAxes(startAxesX, 500, histogram1.length, getMaxInt(histogram1, histogram2));
    	lang.newText(new Coordinates(startAxesX, 540), "Rot: Erstes Histogramm", "", null);    	    	
    	printHistogram(histogram1, startAxesX, 500, histogram1Color);
    	lang.nextStep();
    	lang.newText(new Coordinates(startAxesX, 560), "Blau: Zweites Histogramm", "", null);
    	printHistogram(histogram2, startAxesX + 7, 500, histogram2Color); 
    	lang.nextStep();
    	printHistogramNumbers(histogram1, histogram2, startAxesX, 500);
    	lang.nextStep();
    	questionOne();
    	lang.nextStep();
    	printHistogramAxes(endAxesX, 500, histogram1.length, getMaxInt(histogram1, histogram2));
    	printChiSquareFormula();
    	printNote();
    	lang.nextStep();
    	questionTwo();
    	lang.nextStep();
        animalArrow = new AnimalArrow(lang, new Coordinates(startAxesX + 15,510), 1);
        
        for (int i = 0; i < histogram1.length; i++) {
        	printDistance(histogram1, histogram2, i);
        	printSquaredDistance(histogram1, histogram2, i);
        	printWeightedDitance(histogram1, histogram2, i);
            
            // Sum weighted distance
            chiSquare += weightedDistance;
        } 
        
        animalText.removePart(animalText.getLastIndex());
        animalText.replacePart(animalText.getLastIndex(), " = " + chiSquare);
        makeAlgoTextBold(2, false);
        animalArrow.hide();
        lang.nextStep();
        questionThree();
        lang.nextStep();
        printEndScreen();        
        
        lang.setStepMode(false);
        lang.finalizeGeneration();
    }
    
    private void questionOne() {
    	MultipleChoiceQuestionModel q1 = new MultipleChoiceQuestionModel("q1");
    	q1.setPrompt("Das Ergebnis des Chi-Square Algorithmus ist was?");
    	q1.addAnswer("Eine Distanz", 0, "Leider falsch. Das Ergebnis des Chi-Square Algorithmus ist eine gewichtete Distanz.");
    	q1.addAnswer("Eine gewichtete Distanz", 1, "Richtig!");
    	q1.addAnswer("Eine prozentuale Ähnlichkeit", 0, "Leider falsch. Das Ergebnis des Chi-Square Algorithmus ist eine gewichtete Distanz.");
    	q1.addAnswer("Keine Ahnung", 0, "Leider falsch. Das Ergebnis des Chi-Square Algorithmus ist eine gewichtete Distanz.");
    	lang.addMCQuestion(q1);
    }
    
    private void questionTwo() {
    	TrueFalseQuestionModel q2 = new TrueFalseQuestionModel("q2");
    	q2.setPrompt("Ist das Ergebnis des Chi-Square Algorithmus kleiner gleich 1, wurde das gesuchte Bild höchst wahrscheinlich gefunden?");
    	q2.setCorrectAnswer(false);
    	lang.addTFQuestion(q2);
    }
    
    private void questionThree() {
    	MultipleSelectionQuestionModel q3 = new MultipleSelectionQuestionModel("q3");
    	q3.setPrompt("Welche Eingabedaten benötigt der Chi-Square Algorithmus?");
    	q3.addAnswer("Eine Gewichtung", 0, "Leider falsch. Es werden lediglich ein zu analysierendes Bild und ein Bild des Objekts benötigt.");
    	q3.addAnswer("Ein zu analysierendes Bild", 0, "Richtig!");
    	q3.addAnswer("Ein Bild des Objekt", 0, "Richtig!");
    	q3.addAnswer("Eine Tabelle der Binomialverteilung", 0, "Leider falsch. Es werden lediglich ein zu analysierendes Bild und ein Bild des Objekts benötigt.");
    	lang.addMSQuestion(q3);
    }
    
    private void printDescription() {
    	TextProperties prop = new TextProperties();
    	prop.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 24));    	
    	lang.newText(new Coordinates(50, 50), "Chi-Square Histogramm Vergleich:", "Desrciption0", null, prop);    	
    	printDescriptionLine(" ", 1);
    	printDescriptionLine("Ein Problem der Computer Vision ist das Erkennen von Objekten auf Bildern.", 2);
    	printDescriptionLine("Eine Möglichkeit dieses Problem zu lösen (Wenn auch nicht die beste), ist der Chi-Square Histogramm Vergleich.", 3);
    	printDescriptionLine("Für den Chi-Square Histogramm Vergleich werden zwei Bilder benötigt.", 4);
    	printDescriptionLine("Einmal das zu analysierende Bild auf dem ein bestimmtes Objekt gesucht wird und einmal ein Bild des gesuchten Objektes.", 5);
    	printDescriptionLine("Die Grauwerte beider Bilder werden nun als Histogramm dargestellt und mittels der Chi-Square Formel verglichen. ", 6);
    	printDescriptionLine("Diese lautet wie folgt: \u03A3((q\u1D62 - v\u1D62)² / (q\u1D62 + v\u1D62))", 7);
    	printDescriptionLine("Wobei q\u1D62 der i-te Balken aus dem ersten Histogramm ist und v\u1D62 der i-te Balken aus dem zweiten Histogramm.", 8);
    	printDescriptionLine("Die Chi-Square Formel bestimmt eine gewichtete Distanz zwischen beiden Bildern.", 9);
    	printDescriptionLine("Ist die Distanz unterhalb eines gewissen Schwellenwertes befindet sich höchst wahrscheinlich das gesuchte Objekt auf dem analysierten Bild.", 10);
    	printDescriptionLine("Der Schwellenwert muss je nach Anwendungsszenario definiert werden, so dass die Anwendung optimale Ergebnisse erzielt.", 11);
    }
    
    private void printDescriptionLine(String line, int index) {
    	lang.newText(new Coordinates(50, 50 + index * 20), line, "Desrciption" + index, null);
    }
    
    private void hideDescription() {
    	lang.hideAllPrimitives();
    	TextProperties prop = new TextProperties();
    	prop.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 24));    	
    	lang.newText(new Coordinates(50, 50), "Chi-Square Histogramm Vergleich:", "Desrciption0", null, prop);
    }
    
    private void printAlgoTextArray() {
    	int x = endAxesX - 200;
    	int y = 540;
    	algoTextArray[0] = lang.newText(new Coordinates(x, y), "1. Bestimme Distanz", "", null);
    	algoTextArray[1] = lang.newText(new Coordinates(x, y + 20), "2. Mache Distanz positiv", "", null);
    	algoTextArray[2] = lang.newText(new Coordinates(x, y + 40), "3. Gewichte die Distanz", "", null);
    }
    
    private void makeAlgoTextBold(int index, boolean setBold) {
    	if (setBold) {
    		algoTextArray[index].setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12), null, null);
    		algoTextArray[index].changeColor(null, Color.RED, null, null);
    	} else {
    		algoTextArray[index].setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12), null, null);
    		algoTextArray[index].changeColor(null, Color.BLACK, null, null);
    	}
    	
    }
    
    private void printChiSquareFormula() {
    	printAlgoTextArray();
    	animalText.appendText("\u03A3((q\u1D62 - v\u1D62)² / (q\u1D62 + v\u1D62)) = ");
    	animalText.appendText("...");
    }
    
    private void printNote() {
    	//new AnimalText(lang, new Coordinates(endAxesX,560)).appendText("Hinweis: Kommazahlen werden gerundet.");
    	lang.newText(new Coordinates(endAxesX, 560), "Hinweis: Kommazahlen werden gerundet.", "Note", null);
    }
    
    private void printDistance(int[] histogram1, int[] histogram2, int i) {
    	//RectProperties propGray = new RectProperties();
    	//propGray.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GRAY);
    	//propGray.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    	
    	// Calculate distance
        distance = histogram1[i] - histogram2[i];  
        makeAlgoTextBold(0, true);
        makeAlgoTextBold(2, false);
        animalText.replacePart(animalText.getLastIndex(), "(q\u1D62 - v\u1D62)² / (q\u1D62 + v\u1D62)");
        animalText.setTextPartBold(animalText.getLastIndex());
        animalText.setPartColor(animalText.getLastIndex(), Color.RED);
        animalText.appendText(" + ...");
        lang.nextStep();
        animalText.replacePart(animalText.getLastIndex() - 1, "(" + histogram1[i] + " - " + histogram2[i] + ")² / (q\u1D62 + v\u1D62)");
        animalText.setTextPartBold(animalText.getLastIndex() - 1);
        animalText.setPartColor(animalText.getLastIndex() - 1, Color.RED);
        lang.nextStep();
        animalText.replacePart(animalText.getLastIndex() - 1, "(" + distance + ")² / (q\u1D62 + v\u1D62)");
        animalText.setTextPartBold(animalText.getLastIndex() - 1);
        animalText.setPartColor(animalText.getLastIndex() - 1, Color.RED);
        rectangle1 = lang.newRect(
        		new Coordinates(endAxesX + i * columnGap, 500), 
        		new Coordinates(endAxesX + 20 + i * columnGap, 500 - Math.abs(distance) * histogramFactor), 
        		"Column" + i, 
        		null, 
        		newHistogramRecColor1);
        lang.nextStep();
    }
    
    private void printSquaredDistance(int[] histogram1, int[] histogram2, int i) {
    	//RectProperties propDarkGrey = new RectProperties();
    	//propDarkGrey.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.DARK_GRAY);
    	//propDarkGrey.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    	
    	// Make distance positive in every case
        squaredDistance = distance * distance;       
        makeAlgoTextBold(1, true);
        makeAlgoTextBold(0, false);  
        lang.nextStep();
        animalText.replacePart(animalText.getLastIndex() - 1, squaredDistance + " / (q\u1D62 + v\u1D62)"); 
        animalText.setTextPartBold(animalText.getLastIndex() - 1);
        animalText.setPartColor(animalText.getLastIndex() - 1, Color.RED);
        
        rectangle2 = lang.newRect(
        		new Coordinates(endAxesX + i * columnGap, 500), 
        		new Coordinates(endAxesX + 20 + i * columnGap, 500 - squaredDistance * histogramFactor), 
        		"Column" + i, 
        		null, 
        		newHistogramRecColor2);            
        rectangle1.hide();
        lang.nextStep();
    }
    
    private void printWeightedDitance(int[] histogram1, int[] histogram2, int i) {
    	//RectProperties propBlack = new RectProperties();
    	//propBlack.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLACK);
    	//propBlack.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    	
    	// Weight distance
        weightedDistance = squaredDistance / (histogram1[i] + histogram2[i]);         
        makeAlgoTextBold(2, true);
        makeAlgoTextBold(1, false);        
        animalText.replacePart(animalText.getLastIndex(), " + ");
        animalText.appendText("...");       
        lang.nextStep();
        animalText.replacePart(animalText.getLastIndex() - 2, squaredDistance + " / (" + histogram1[i] + " + " + histogram2[i] + ")");
        animalText.setTextPartBold(animalText.getLastIndex() - 2);
        animalText.setPartColor(animalText.getLastIndex() - 2, Color.RED);
        lang.nextStep();
        animalText.replacePart(animalText.getLastIndex() - 2, String.valueOf(weightedDistance));
        animalText.setTextPartBold(animalText.getLastIndex() - 2);
        animalText.setPartColor(animalText.getLastIndex() - 2, Color.RED);
        rectangle3 = lang.newRect(
        		new Coordinates(endAxesX + i * columnGap, 500), 
        		new Coordinates(endAxesX + 20 + i * columnGap, 500 - weightedDistance * histogramFactor), 
        		"Column" + i, 
        		null, 
        		newHistogramRecColor3);
        rectangle2.hide();
        AnimalText tempAnimalText = new AnimalText(lang, new Coordinates(endAxesX + 3 + i * 40, 500 - weightedDistance * histogramFactor - 20));
        tempAnimalText.appendText(String.valueOf(weightedDistance));
        lang.nextStep();
        animalText.setTextPartPlain(animalText.getLastIndex() - 2);
        animalText.setPartColor(animalText.getLastIndex() - 2, Color.BLACK);
        animalArrow.moveToNextColumn();
        
    }
    
    private void printHistogram(int[] histogram, int positionX, int positionY, RectProperties prop) {
    	int width = 20;
  
    	for (int i = 0; i < histogram.length; i++) {
    		lang.newRect(
    				new Coordinates(positionX + i * 40, positionY), 
    				new Coordinates(positionX + width + i * 40, positionY - histogram[i] * histogramFactor), 
    				"Column" + i, 
    				null, 
    				prop);  
    	} 
    }
    private void printHistogramNumbers(int[] histogram1, int[] histogram2, int positionX, int positionY) {
    	AnimalText tempAnimalText;
    	for (int i = 0; i < histogram1.length; i++) {
    		tempAnimalText = new AnimalText(lang, new Coordinates(positionX /*- 5*/ + i * 40 + 12, positionY - getMaxInt(histogram1[i], histogram2[i]) * histogramFactor - 20));
    		tempAnimalText.appendText(String.valueOf(histogram1[i]));
    		tempAnimalText.setPartColor(tempAnimalText.getLastIndex(), (Color) histogram1Color.get("fillColor"));
    		tempAnimalText.appendText(";");
    		tempAnimalText.appendText(String.valueOf(histogram2[i]));
    		tempAnimalText.setPartColor(tempAnimalText.getLastIndex(), (Color) histogram2Color.get("fillColor"));
    		tempAnimalText.centerText();
    	}
    }
    
    private int getMaxInt(int int1, int int2) {
    	return int1 > int2 ? int1 : int2;
    }
    
    private void printHistogramAxes(int positionX, int positionY, int lengthX, int lengthY) {
    	PolylineProperties prop = new PolylineProperties();
    	prop.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);     	
    	
    	lang.newPolyline(
    			new Coordinates[]{
    					new Coordinates(positionX - 10, positionY),
    					new Coordinates(positionX + 40 * lengthX + 20, positionY)}, 
    			"X-Achse", 
    			null, 
    			prop); 
    	lang.newPolyline(
    			new Coordinates[]{
    					new Coordinates(positionX - 10, positionY),
    					new Coordinates(positionX - 10, positionY - lengthY * histogramFactor - 20)}, 
    			"Y-Achse", 
    			null, 
    			prop);    	 
    }
    
    private int getMaxInt(int[] histogram1, int[] histogram2) {
    	int a = Arrays.stream(histogram1).max().getAsInt();
    	int b = Arrays.stream(histogram2).max().getAsInt();
    	
    	if (a > b) {
    		return a;
    	}

    	return b;
    }
    
    private void printEndScreen() {
    	hideDescription();
    	animalText.setPosition(new Coordinates(50, 150));
    	lang.newText(new Coordinates(50, 150 + 1 * 20), "Die beiden Histogramme wurden nun durch den Chi-Square Algorithmus miteinander verglichen.", "Desrciption", null);
    	lang.newText(new Coordinates(50, 150 + 2 * 20), "Die Distanz die durch diesen Vergleich ermittelt wurde ist: " + chiSquare, "Desrciption", null);
    	lang.newText(new Coordinates(50, 150 + 3 * 20), "Ob diese Distanz nun klein genug ist, sprich dass sich die Histogramme ähnlich genug sind, hängt vom Schwellenwert ab.", "Desrciption", null);
    	lang.newText(new Coordinates(50, 150 + 4 * 20), "Dieser kann je nach Anwendungsszenario unterschiedlich sein.", "Desrciption", null);
    	lang.newText(new Coordinates(50, 150 + 5 * 20), "Ist die Distanz kleiner als der Schwellenwert, befindet sich das gesuchte Objeckt wahrscheinlich auf dem analysierten Bild. ", "Desrciption", null);
    }
}

private class AnimalText {
	
	private Language lang;
	private Coordinates startingCoordinates;	
	private Coordinates endCoordinates;
	private ArrayList<Coordinates> animalCoordinateList = new ArrayList<Coordinates>();
	private ArrayList<Text> animalTextList = new ArrayList<Text>();
	private ArrayList<String> animalStringList = new ArrayList<String>();
	private ArrayList<Color> animalColorList = new ArrayList<Color>();
	private TextProperties prop;
	
	public AnimalText(Language lang, Coordinates startingCoordinates) {
		this.lang = lang;
		this.startingCoordinates = startingCoordinates;	
		endCoordinates = startingCoordinates;
		
		prop = new TextProperties();
    	prop.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.MONOSPACED, Font.PLAIN, 12));   
	}

	public int appendText(String text) {		
		animalTextList.add(lang.newText(endCoordinates, text, "", null, prop));	
		animalStringList.add(text);
		animalCoordinateList.add(endCoordinates);
		animalColorList.add(Color.BLACK);
		endCoordinates = new Coordinates(endCoordinates.getX() + text.length() * 7, endCoordinates.getY());
		
		return animalTextList.size() - 1;
	}
	
	public void setTextPartBold(int index) {
		animalTextList.get(index).setFont(new Font(Font.MONOSPACED, Font.BOLD, 12), null, null);
	}
	
	public void setTextPartPlain(int index) {
		animalTextList.get(index).setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12), null, null);
	}
	
	public void setTextPartItalic(int index) {
		animalTextList.get(index).setFont(new Font(Font.MONOSPACED, Font.ITALIC, 12), null, null);
	}
	
	public void replacePart(int index, String text) {	
		if (index < 0) {
			appendText(text);
		} else {
			endCoordinates = (Coordinates) animalTextList.get(index).getUpperLeft();
			animalTextList.get(index).hide();
			animalTextList.set(index, lang.newText(endCoordinates, text, "", null, prop));	
			animalStringList.add(index, text);
			animalStringList.remove(index+1);
			//animalColorList.add(index, Color.BLACK);
			//animalColorList.remove(index+1);
			endCoordinates = new Coordinates(endCoordinates.getX() + text.length() * 7, endCoordinates.getY());		
			
			String tempText;
			int counter = 0;		
			
			for (final ListIterator<Text> i = animalTextList.listIterator(); i.hasNext();) { 
				  final Text element = i.next();
				  
				  if (counter > index) {
					  tempText = element.getText();
					  element.hide();
					  i.set(lang.newText(endCoordinates, tempText, "", null, element.getProperties()));
					  animalStringList.add(counter, tempText);
					  animalStringList.remove(counter+1);
					  //animalColorList.add(counter, Color.BLACK);
					  //animalColorList.remove(counter+1);
					  animalCoordinateList.add(counter, endCoordinates);
					  endCoordinates = new Coordinates(endCoordinates.getX() + tempText.length() * 7, endCoordinates.getY());
				  }
				  counter++;
				}
		}		
	}
	
	public void removePart(int index) {
		replacePart(index, "");
		animalTextList.remove(index);
	}
	
	public void setPartColor(int index, Color color) {
		animalTextList.get(index).changeColor(null, color, null, null);
		animalColorList.add(index, color);
		animalColorList.remove(index+1);
	}
	
	public int getLastIndex() {
		return animalTextList.size() - 1;
	}
	
	public void setPosition(Coordinates coordinates) {
		ArrayList<String> animalStringListTemp = (ArrayList<String>) animalStringList.clone();
		for (int i = 0; i < animalTextList.size(); i++) {
			animalTextList.get(i).hide();
		}
		
		animalTextList.clear();
		animalCoordinateList.clear();
		animalStringList.clear();
		startingCoordinates = coordinates;
		endCoordinates = coordinates;
		
		for (int i = 0; i < animalStringListTemp.size(); i++) {
			appendText(animalStringListTemp.get(i));
			animalTextList.get(i).changeColor(null, animalColorList.get(i), null, null);
		}
	}
	
	public void centerText() {
		int x = startingCoordinates.getX() + ((startingCoordinates.getX() - endCoordinates.getX()) / 2);
		int y = startingCoordinates.getY() + ((startingCoordinates.getY() - endCoordinates.getY()) / 2);
		setPosition(new Coordinates(x, y));
	}
}

private class AnimalArrow {

	private Language lang;
	private Coordinates startingCoordinates;
	private int arrowSize;
	private Polyline[] animalArrow = new Polyline[3];
	
	public AnimalArrow(Language lang, Coordinates startingCoordinates, int arrowSize) {
		this.lang = lang;
		this.startingCoordinates = startingCoordinates;
		this.arrowSize = arrowSize;
		printArrow();
	}
	
	private void printArrow() {
		animalArrow[0] = lang.newPolyline(
				new Coordinates[]{startingCoordinates, new Coordinates(startingCoordinates.getX() - 5 * arrowSize, startingCoordinates.getY() + 5 * arrowSize)}, 
				"Left", null);
		animalArrow[1] = lang.newPolyline(
				new Coordinates[]{startingCoordinates, new Coordinates(startingCoordinates.getX(), startingCoordinates.getY() + 20 * arrowSize)}, 
				"Middle", null);
		animalArrow[2] = lang.newPolyline(
				new Coordinates[]{startingCoordinates, new Coordinates(startingCoordinates.getX() + 5 * arrowSize, startingCoordinates.getY() + 5 * arrowSize)}, 
				"Right", null);
	}
	
	public void hide() {
		animalArrow[0].hide();
		animalArrow[1].hide();
		animalArrow[2].hide();
	}
	
	public void moveTo(Coordinates newCoordinates) {
		startingCoordinates = newCoordinates;
		hide();
		printArrow();
	}
	
	public void moveToNextColumn() {
		moveTo(new Coordinates(startingCoordinates.getX() + 40, startingCoordinates.getY()));
	}
}


}
