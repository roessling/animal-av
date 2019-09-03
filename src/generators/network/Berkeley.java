
/*
 * Berkeley.java
 * Sascha Dutschka, 2015 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */

package generators.network;

import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.Color;
import java.awt.Font;
import java.awt.PageAttributes.ColorType;
import java.util.Arrays;
import java.util.Locale;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.StringArray;
import algoanim.primitives.IntArray;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

import java.util.Hashtable;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;
import animal.variables.VariableRoles;

public class Berkeley implements Generator {
    private Language lang;
    private String[] stringArray;
    private SourceCodeProperties sourceCodeProp;
    private ArrayProperties arrayProp;

    public void init(){
        lang = new AnimalScript("Berkeley Algorithmus", "Sascha Dutschka", 800, 600);
        lang.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
    	sourceCodeProp = (SourceCodeProperties)props.getPropertiesByName("sourceCodeProp");
        arrayProp = (ArrayProperties)props.getPropertiesByName("arrayProp");
    	berkeley(primitives, sourceCodeProp, arrayProp);
        return lang.toString();
    }

    public String getName() {
        return "Berkeley Algorithmus";
    }

    public String getAlgorithmName() {
        return "Berkeley";
    }

    public String getAnimationAuthor() {
        return "Sascha Dutschka";
    }

    public String getDescription(){
        return "<h1>Berkeley Algorithmus</h1><br><br>"
 +"Der Berkeley Algorithmus ist eine Uhrzeit-Synchronisierungs-Methode in verteilten Systemen.<br>"
 + "Dabei wird angenommen, dass jedes Teilsystem Zugang zu einer genauen Zeitquelle hat.<br><br>"
 + "Zunaechst wird ein 'master' durch einen 'election process' - wie zum Beispiel <br>"
 + "Chang oder Robert Algorithmus - gewaehlt.<br><br>"
 + "Der master fragt alle 'slaves' an und diese Antworten mit ihrer aktuellen Zeit, <br>"
 + "worauf der master die Zeit anhand der round-trip-time fuer jeden slave abschaetzt (siehe Cristian's algorithm).<br>"
 + "Einzelne Werte, welche stark von den anderen Werten abweichen, werden verworfen.<br><br>"
 + "Von den restlichen Werten wird der Durchschnitt gebildet und der master teilt jedem slave<br>"
 + "den Abweichungs-Wert (positiv oder negativ) mit, um die dieser seine Uhrzeit anpassen muss.<br><br>"
 + "Hinweise:<br>"
 + "Das verwendete Zeitformat entspricht hh:mm:ss:fff<br>"
 + "maxDelta ist der Schwellwert in Millisekunden, ab dem stark abweichende Werte ignoriert werden.<br>"
 + "negError und posError definieren ein Intervall, zum Beispiel von -5 bis +5 in Millisekunden,<br>"
 + "in dem die erhaltenen bzw. geschaetzten Zeitwerte abweichen koennen,<br>"
 + "um die Netzwerkkommunikation bzw. die nicht absolut praezise Abschaetzung zu simulieren.";
    }

    public String getCodeExample(){
        return "Auswahl des Masters"
 +"\n"
 +"Anfrage an alle slaves"
 +"\n"
 +"Empfangen der Antworten"
 +"\n"
 +"Bestimmen der lokalen Zeit des slaves"
 +"\n"
 +"Ausschluss stark abweichender Werte"
 +"\n"
 +"Mittelwertbildung"
 +"\n"
 +"Sende Mitteilung mit Abweichung an alle slaves";
    }
    
    public int timeToInt(String time){
    	String[] t = time.split(":");
    	return Integer.parseInt(t[3]) + 1000 * (Integer.parseInt(t[2]) + 60 * (Integer.parseInt(t[1]) + 60 * (Integer.parseInt(t[0]))));
    }
    public String intToTime(int t){
    	int hours = t / (60*60*1000);
    	t = t % (60*60*1000);
    	int min = t / (60*1000);
    	t = t % (60*1000);
    	int sec = t / 1000;
    	t = t % 1000;
    	return hours + ":" + min + ":" + sec + ":" + t;
    }
    
    /**
     * Sort the int array passed in
     * @param a the array to be sorted
     */
    public void berkeley(Hashtable<String,Object> primitives, SourceCodeProperties sourceCodeProp, ArrayProperties arrayProp) {
    
    String[] timeArray = (String[])primitives.get("stringArray");
    //String[] stringArray = (String[])primitives.get("stringArray");
    	
    Color highlightCodeColor = new Color(255,0,0);
    Color defaultCodeColor = new Color(0,0,0);
    	
    ///////////////
    //FIRST PAGE://
    ///////////////
    // show the header with a heading surrounded by a rectangle
    TextProperties headerProps = new TextProperties();
    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 24));
    Text header = lang.newText(new Coordinates(20, 22), "Berkeley Algorithmus","header", null, headerProps);
    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(200,200,200));
    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    Rect headerRect = lang.newRect(new Offset(-8, -3, "header",
            AnimalScript.DIRECTION_NW), new Offset(8, 3, "header", "SE"), "headerRect",
            null, rectProps);
    
    TextProperties textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 16));
    Text descriptionText1 = lang.newText(new Coordinates(10, 50),
        "Der Berkeley Algorithmus ist eine Uhrzeit-Synchronisierungs-Methode in verteilten Systemen.",
        "description1", null, textProps);
    Text descriptionText2 = lang.newText(new Offset(0, 25, "description1",
        AnimalScript.DIRECTION_NW),
        "Dabei wird angenommen, dass jedes Teilsystem Zugang zu einer genauen Zeitquelle hat.",
        "description2", null, textProps);
    Text descriptionText3 = lang.newText(new Offset(0, 40, "description2",
        AnimalScript.DIRECTION_NW),
        "Zunaechst wird ein 'master' durch einen 'election process' - wie zum Beispiel Chang oder Robert Algorithmus - gewaehlt.",
        "description3", null, textProps);
    Text descriptionText4 = lang.newText(new Offset(0, 25, "description3",
            AnimalScript.DIRECTION_NW),
            "Der master fragt alle 'slaves' an und diese Antworten mit ihrer aktuellen Zeit, ",
            "description4", null, textProps);
    Text descriptionText5 = lang.newText(new Offset(0, 25, "description4",
            AnimalScript.DIRECTION_NW),
            "worauf der master die Zeit anhand der round-trip-time fuer jeden slave abschaetzt (siehe Cristian's algorithm).",
            "description5", null, textProps);
    Text descriptionText6 = lang.newText(new Offset(0, 40, "description5",
            AnimalScript.DIRECTION_NW),
            "Einzelne Werte, welche stark von den anderen Werten abweichen, werden verworfen.",
            "description6", null, textProps);
    Text descriptionText7 = lang.newText(new Offset(0, 25, "description6",
            AnimalScript.DIRECTION_NW),
            "Von den restlichen Werten wird der Durchschnitt gebildet und der master teilt jedem slave",
            "description7", null, textProps);
    Text descriptionText8 = lang.newText(new Offset(0, 25, "description7",
            AnimalScript.DIRECTION_NW),
            "den Abweichungs-Wert (positiv oder negativ) mit, um die dieser seine Uhrzeit anpassen muss.",
            "description8", null, textProps);
    Text descriptionText9 = lang.newText(new Offset(0, 40, "description8",
            AnimalScript.DIRECTION_NW),
            "Hinweise:",
            "description9", null, textProps);
    Text descriptionText10 = lang.newText(new Offset(0, 25, "description9",
            AnimalScript.DIRECTION_NW),
            "Das verwendete Zeitformat entspricht hh:mm:ss:fff",
            "description10", null, textProps);
    Text descriptionText11 = lang.newText(new Offset(0, 25, "description10",
            AnimalScript.DIRECTION_NW),
            "maxDelta ist der Schwellwert in Millisekunden, ab dem stark abweichende Werte ignoriert werden.",
            "description11", null, textProps);
    Text descriptionText12 = lang.newText(new Offset(0, 25, "description11",
            AnimalScript.DIRECTION_NW),
            "negError und posError definieren ein Intervall, zum Beispiel von -5 bis +5 in Millisekunden,",
            "description12", null, textProps);
    Text descriptionText13 = lang.newText(new Offset(0, 25, "description12",
            AnimalScript.DIRECTION_NW),
            "in dem die erhaltenen bzw. geschaetzten Zeitwerte abweichen koennen,",
            "description13", null, textProps);
    Text descriptionText14 = lang.newText(new Offset(0, 25, "description13",
            AnimalScript.DIRECTION_NW),
            "dum die Netzwerkkommunikation bzw. die nicht absolut praezise Abschaetzung zu simulieren.",
            "description14", null, textProps);
    
    
    lang.nextStep();
    lang.hideAllPrimitives();
	header.show();
	headerRect.show();
    
    //VARIABLES:
 	StringArray array = lang.newStringArray(new Coordinates(20, 150), timeArray, "stringArray", null, arrayProp);
	int systems = timeArray.length;
	int master = (int) Math.round(Math.random() * (systems-1));
	int maxDelta = (int)primitives.get("maxDelta");
	int negError = (int)primitives.get("negError");
	int posError = (int)primitives.get("posError");
	int iterations = (int)primitives.get("iterations");
	//Variable window:
	Variables varWindow = lang.newVariables();
    varWindow.declare("int", "systems", ""+systems , animal.variables.Variable.getRoleString(VariableRoles.FIXED_VALUE));
    varWindow.declare("int", "master", ""+master, animal.variables.Variable.getRoleString(VariableRoles.ORGANIZER));
    varWindow.declare("int", "maxDelta", ""+maxDelta, animal.variables.Variable.getRoleString(VariableRoles.ORGANIZER));
    varWindow.declare("int", "negError", ""+negError , animal.variables.Variable.getRoleString(VariableRoles.ORGANIZER));
    varWindow.declare("int", "posError", ""+posError, animal.variables.Variable.getRoleString(VariableRoles.ORGANIZER));
    varWindow.declare("int", "iterations", ""+iterations, animal.variables.Variable.getRoleString(VariableRoles.FIXED_VALUE));
    //Variable texts:
	Text systemsText = lang.newText(new Offset(30,-10,"header", AnimalScript.DIRECTION_NE), "Systeme: " + systems, "systemsText", null);
	Text masterText = lang.newText(new Offset(0,9,"systemsText",AnimalScript.DIRECTION_W), "Master: " + master, "masterText", null);
	Text iterationsText = lang.newText(new Offset(0,9,"masterText",AnimalScript.DIRECTION_W), "Iteration: 1 von " + iterations, "iterationsText", null);
	Text maxDeltaText = lang.newText(new Offset(40,0,"systemsText",AnimalScript.DIRECTION_NE), "maxDelta: " + maxDelta + "ms", "maxDeltaText", null);
	Text negErrorText = lang.newText(new Offset(0,9,"maxDeltaText", AnimalScript.DIRECTION_W), "negError: " + negError + "ms", "negErrorText", null);
	Text posErrorText = lang.newText(new Offset(0,9,"negErrorText",AnimalScript.DIRECTION_W), "posError: " + posError + "ms", "posErrorText", null);
	
	
	//Maker:
	// Array, current index, name, display options, properties
	ArrayMarkerProperties arrayIMProps = new ArrayMarkerProperties();
	arrayIMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "master");   
	arrayIMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
	ArrayMarker masterMarker = lang.newArrayMarker(array, master, "master", null, arrayIMProps);
	masterMarker.hide();
    
    for (int currentIteration = 1; currentIteration <= iterations ; currentIteration++) {
    	//cleanup:
    	if (currentIteration > 1){
    		lang.nextStep();
	    	lang.hideAllPrimitives();
	    	header.show();
	    	headerRect.show();
	    	systemsText.show();
	    	negErrorText.show();
	    	posErrorText.show();
	    	masterText.show();
	    	maxDeltaText.show();
	    	iterationsText.show();
	    	//Update Variables:
	    	master = (int) Math.round(Math.random() * (systems-1));
	    	array = lang.newStringArray(new Coordinates(20, 150), timeArray, "stringArray", null, arrayProp);
	    	 //Simulation der besseren Abschaetzung der lokalen Zeitwerte:
	    	negError = negError/2;
	    	posError = posError/2;
	    	maxDelta = (posError - negError) / 2;
	    	//Update Texts/Markers with Variables:
	    	masterMarker.move(master, null, null);
	    	masterText.setText("Master: " + master, null, null);
	    	iterationsText.setText("Iteration: " + currentIteration + " von " + iterations, null, null);
	    	negErrorText.setText("negError: " + negError + "ms", null, null);
	    	posErrorText.setText("posError: " + posError + "ms", null, null);
	    	maxDeltaText.setText("maxDelta: " + maxDelta + "ms", null, null);
	    	//Update VarWindow:
	    	varWindow.set("master", ""+master);
	    	varWindow.set("negError", ""+negError);
	    	varWindow.set("posError", ""+posError);
	    	varWindow.set("maxDelta", ""+maxDelta);
    	}
	    ///////////
	    //STEP 1://
	    ///////////
		Text codeLine1 = lang.newText(new Offset(0,25,"header", AnimalScript.DIRECTION_SW), "1. Auswahl des masters mit einem geeigneten Algorithmus (hier zufaellig)", "codeLine1", null);
		//STEP 1.1
		lang.nextStep();
		codeLine1.changeColor(null, highlightCodeColor, null, null);
		masterMarker.show();
		///////////
		//STEP 2://
		///////////
		lang.nextStep();
		codeLine1.changeColor(null, defaultCodeColor, null, null);
		Text codeLine2 = lang.newText(new Offset(0,15,"stringArray", AnimalScript.DIRECTION_SW), "2. Master fragt alle slaves an und schaetzt anhand der empfangenen Antworten deren lokale Uhrzeit ab (-> zufaelliger Fehler)", "codeLine2", null);
		codeLine2.changeColor(null, highlightCodeColor, null, null);
		String[] step2array = new String[systems-1];
		int j = 0;
		for (int i = 0; i < systems; i++) {
			if(i != master){
				step2array[j++] = intToTime(timeToInt(timeArray[i]) + ((int)Math.round((Math.random() * (posError - negError) + negError))));
			}
		}
		StringArray step2Array = lang.newStringArray(new Offset(0,15,"codeLine2", AnimalScript.DIRECTION_SW), step2array, "step2Array", null, arrayProp);
		
		///////////
		//STEP 3://
		///////////
		lang.nextStep();
		codeLine2.changeColor(null, defaultCodeColor, null, null);
		Text codeLine3 = lang.newText(new Offset(0,15,"step2Array", AnimalScript.DIRECTION_SW), "3. Ausschluss der Werte, die von allen anderen erhaltenen Werten um mehr als " + maxDelta + " abweichen.", "codeLine3", null);
		codeLine3.changeColor(null, highlightCodeColor, null, null);
		int[] step3arrayMiliseconds = new int[step2array.length];
		for (int i = 0; i < step2array.length; i++) {
			step3arrayMiliseconds[i] = timeToInt(step2array[i]);
		}
		int valueOKCounter = 0;
		for (int i = 0; i < step3arrayMiliseconds.length; i++) {
			for (int k = 0; k < step3arrayMiliseconds.length; k++) {
				if (i!=k && Math.abs(step3arrayMiliseconds[i] - step3arrayMiliseconds[k]) <= maxDelta){
					valueOKCounter++;
					break;
				}
			}
		}
		String[] step3arrayFiltered = new String[valueOKCounter];
		j = 0;
		for (int i = 0; i < step3arrayMiliseconds.length; i++) {
			for (int k = 0; k < step3arrayMiliseconds.length; k++) {
				if (i!=k && Math.abs(step3arrayMiliseconds[i] - step3arrayMiliseconds[k]) <= maxDelta){
					step3arrayFiltered[j++] = intToTime(step3arrayMiliseconds[i]);
					break;
				}
			}
		}
		StringArray step3ArrayFiltered = lang.newStringArray(new Offset(0,15,"codeLine3", AnimalScript.DIRECTION_SW), step3arrayFiltered, "step3ArrayFiltered", null, arrayProp);
		
		///////////
		//STEP 4://
		///////////
		lang.nextStep();
		codeLine3.changeColor(null, defaultCodeColor, null, null);
		Text codeLine4 = lang.newText(new Offset(0,15,"step3ArrayFiltered", AnimalScript.DIRECTION_SW), "4. Bilden des Mittelwertes:", "codeLine4", null);
		codeLine4.changeColor(null, highlightCodeColor, null, null);
		int avg = 0;
		for (int i = 0; i < step3arrayFiltered.length; i++) {
			avg += timeToInt(step3arrayFiltered[i]);
		}
		int newTime = Math.round(avg / step3arrayFiltered.length);
		String[] step4array = { intToTime(newTime) };
		StringArray step4Array = lang.newStringArray(new Offset(0,15,"codeLine4", AnimalScript.DIRECTION_SW), step4array, "step4Array", null, arrayProp);
		
		///////////
		//STEP 5://
		///////////
		lang.nextStep();
		codeLine4.changeColor(null, defaultCodeColor, null, null);
		Text codeLine5 = lang.newText(new Offset(0,15,"step4Array", AnimalScript.DIRECTION_SW), "5. Berechnung der Zeit-Abweichungen und Senden an jeden slave:", "codeLine5", null);
		codeLine5.changeColor(null, highlightCodeColor, null, null);
		
		int[] step5array = new int[systems];
		j = 0;
		for (int i = 0; i < systems; i++) {
			if(i == master){
				step5array[i] = newTime - timeToInt(timeArray[i]);
			} else{
				step5array[i] = newTime - timeToInt(step2array[j++]);
			}
		}
		IntArray step5Array = lang.newIntArray(new Offset(0,15,"codeLine5", AnimalScript.DIRECTION_SW), step5array, "step5Array", null, arrayProp);
	
		///////////
		//STEP 6://
		///////////
		lang.nextStep();
		codeLine5.changeColor(null, defaultCodeColor, null, null);
		Text codeLine6 = lang.newText(new Offset(0,15,"step5Array", AnimalScript.DIRECTION_SW), "6. Aktuallisierung der Zeitwerte", "codeLine6", null);
		codeLine6.changeColor(null, highlightCodeColor, null, null);
		String[] step6array = new String[systems];
		int minTime = 2000000000;
		int maxTime = 0;
		int minTimePos = 0;
		int maxTimePos = 0;
		for (int i = 0; i < systems; i++) {
			int updateTime = timeToInt(timeArray[i]) + step5array[i];
			if(updateTime < minTime){
				minTime = updateTime;
				minTimePos = i;
			}
			if(updateTime > maxTime){
				maxTime = updateTime;
				maxTimePos = i;
			}
			step6array[i] = intToTime(updateTime);
		}
		StringArray step6Array = lang.newStringArray(new Offset(0,15,"codeLine6", AnimalScript.DIRECTION_SW), step6array, "step6Array", null, arrayProp);
		///////////
		//STEP 7://
		///////////
		lang.nextStep();
		step6Array.highlightCell(minTimePos, null, null);
		step6Array.highlightCell(maxTimePos, null, null);
		codeLine6.changeColor(null, defaultCodeColor, null, null);
		Text codeLine7 = lang.newText(new Offset(0,15,"step6Array", AnimalScript.DIRECTION_SW), "Maximale Zeitdifferenz: " + (maxTime - minTime) + "ms", "codeLine7", null);
		Text codeLine8 = lang.newText(new Offset(0,0,"codeLine7", AnimalScript.DIRECTION_SW), "Diese entsteht durch die Abschaetzung der lokalen Zeitwerte. Bei einer erneuten Iteration", "codeLine8", null);
		Text codeLine9 = lang.newText(new Offset(0,0,"codeLine8", AnimalScript.DIRECTION_SW), "kann man versuchen, die Abschaetzung dieser Werte anhand der vorherigen Fehler zu verbessern.", "codeLine9", null);
		Text codeLine10 = lang.newText(new Offset(0,0,"codeLine9", AnimalScript.DIRECTION_SW), "Um die Synchronisation aufrecht zu erhalten, muss der Algorithmus regelmaessig ausgefuehrt werden.", "codeLine10", null);
		codeLine7.changeColor(null, highlightCodeColor, null, null);
		codeLine8.changeColor(null, highlightCodeColor, null, null);
		codeLine9.changeColor(null, highlightCodeColor, null, null);
		codeLine10.changeColor(null, highlightCodeColor, null, null);
		//change array for next iteration:
		for (int i = 0; i < systems; i++) {
			timeArray[i] = step6array[i];
		}
    }
		
	///////////////
	//FINAL PAGE://
	///////////////
	lang.nextStep();
	lang.hideAllPrimitives();
	header.show();
	headerRect.show();
	TextProperties textProps1 = new TextProperties();
    textProps1.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 16));
	Text finalText1 = lang.newText(new Coordinates(10, 70),
	    "Der Berkeley-Algorithmus sollte nur im Intranet verwendet werden, da die Abschaetzung",
	    "finalText1", null, textProps1);
	Text finalText2 = lang.newText(new Offset(0, 25, "finalText1",
	        AnimalScript.DIRECTION_NW),
	        "der lokalen Zeitwerte moeglichst praezise sein muss.",
	        "finalText2", null, textProps1);
	Text finalText3 = lang.newText(new Offset(0, 40, "finalText2",
	        AnimalScript.DIRECTION_NW),
	        "Das Veraendern der Uhrzeit, vor allem bei negativen Werten (=zurueckstellen) kann vatale Auswirkungen",
	        "finalText3", null, textProps1);
	Text finalText4 = lang.newText(new Offset(0, 25, "finalText3",
	        AnimalScript.DIRECTION_NW),
	        "auf andere Anwendungen verursachen und wird daher meist nicht schlagartig durchgefuehrt.",
	        "finalText4", null, textProps1);
	Text finalText5 = lang.newText(new Offset(0, 25, "finalText4",
	        AnimalScript.DIRECTION_NW),
	        "Vielmehr wird die Uhrzeit verlangsamt/beschleunigt, bis der richtige Wert erreicht wurde ('clock slew').",
	        "finalText5", null, textProps1);
    }
    
    

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_NETWORK);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }

}