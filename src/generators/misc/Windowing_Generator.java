/*
 * Windowing_Generator.java
 * Amir Mesbah, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;
import algoanim.primitives.generators.Language;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;

import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.misc.helpersWindowing.Windowing;
import algoanim.animalscript.AnimalScript;

public class Windowing_Generator implements ValidatingGenerator {
    private Language lang;
    private int[] Window;
    private String[][] Datensatz;
    private SourceCodeProperties sourceCode;
    private MatrixProperties Tabellen;
    private SourceCodeProperties Klassifizierer;
    
    public void init(){
        lang = new AnimalScript("Windowing [DE]", "Amir Mesbah", 800, 600);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        Window = (int[])primitives.get("Window");
        Datensatz = (String[][])primitives.get("Datensatz");
     	
		sourceCode = (SourceCodeProperties)props.getPropertiesByName("sourceCode");
        Tabellen = (MatrixProperties)props.getPropertiesByName("Tabellen");
        Klassifizierer = (SourceCodeProperties)props.getPropertiesByName("Klassifizierer");
        
        String[] headers = new String[Datensatz[0].length];
        for(int i = 0; i < Datensatz[0].length; i++)
        		headers[i] = Datensatz[0][i];
        
        Windowing.main(lang, Window.length+1,Window.length, Datensatz[0].length, headers, Datensatz, Window, sourceCode, Tabellen, Klassifizierer);
        return lang.toString();
    }
    
    public String getName() {
        return "Windowing [DE]";
    }

    public String getAlgorithmName() {
        return "Windowing";
    }

    public String getAnimationAuthor() {
        return "Amir Mesbah";
    }

    public String getDescription(){
        return "Der Windowing-Algorithmus ermöglicht das Erstellen von Klassifizieren ohne den gesamten Datensatz zu verwenden, sodass die Gefahr des Overfitting (Überanpassung an den Datensatz) reduziert wird."
 +"\n"
 +"Umgesetzt wird dies, indem nur x Einträge (Window) aus dem Datensatz betrachtet werden und darauf ein Klassifizierer trainiert wird."
 +"\n"
 +"Nun wird geschaut, ob die Vorhersage des erzeugten Klassifizierers mit den Daten im Datensatz übereinstimmen. Ist dies der Fall, ist der Algorithmus zu Ende."
 +"\n"
 +"Falls jedoch eine Vorhersage falsch ist, wird der Eintrag, der falsch klassifiziert wurde im Window aufgenommen und erneut ein Klassifizierer trainiert und überprüft."
 +"\n"
 +"Als Klassifizierer kann man einen beliebigen Algorithmus wählen. Hier wird der Separate-and-Conquer Algorithmus in der Variante des Top-Down Hill Climbing verwendet. Dabei wird ein Datensatz positiv klassifiziert, sobald eine Regel erfüllt ist.";
    }

    public String getCodeExample(){
        return "1. Initialiesiere den Window mit 'x' Daten (zufälliges Auswählen ohne Wiederholungen)"
 +"\n"
 +"2. Lerne auf dem Window einen Klassifizierer"
 +"\n"
 +"3. Wenn alle Einträge im Datensatz richtig klassifiziert werden -> return Klassifizierer"
 +"\n"
 +"4. Falls ein Eintrag falsch klassifiziert wird -> füge den Eintrag dem Window hinzu und gehe zu 2.";
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

	@Override
	public boolean validateInput(AnimationPropertiesContainer arg0, Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		int[] window = (int[])primitives.get("Window");
		String[][] datensatz = (String[][])primitives.get("Datensatz");
        
        for(int i = 0; i < datensatz.length; i++)
        {
        		if(i != 0)
	        		if(!(datensatz[i][datensatz[i].length-1].equals("Ja") || datensatz[i][datensatz[i].length-1].equals("Nein")))
					throw new IllegalArgumentException("In der letzten Spalte vom Datensatz darf nur 'Ja' und 'Nein' enthalten (R: " + i + " C: " + (datensatz[i].length-1) + ")");
        		for(int j = 0; j < datensatz[i].length-1; j++)
        		{
        			if(datensatz[i][j].equals(""))
        				throw new IllegalArgumentException("Datensatz enthält ein leeres Feld (R: " + i + " C: " + j + ")");
        		}
        }
        
        for(int i = 0; i < window.length; i++)
        {
        			for(int j = 0; j < window.length; j++)
        				if (i != j)
        					if(window[i] == window[j])
                				throw new IllegalArgumentException("Window enthält eine Widerholung 1 (P: " + i +  " und P: " + j + ")");
        			
        			if(window[i] <= 0)
        				throw new IllegalArgumentException("Trainingssatz enthält eine Zahl kleiner 1 (P: " + i + ")");
        			if(window[i] > datensatz.length-1)
        				throw new IllegalArgumentException("Trainingssatz enthält eine Zahl die größer ist als die Anzahl der Eintäge im Datensatz (P: " + i + ")");
        }
		return true;
	}

}