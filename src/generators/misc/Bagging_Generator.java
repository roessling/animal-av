/*
 * Bagging_Generator.java
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
import generators.misc.helpersBagging.Bagging;
import algoanim.animalscript.AnimalScript;

public class Bagging_Generator implements ValidatingGenerator {
    private Language lang;
    private String[][] Testdatensatz;
    private int[][] Trainingssaetze;
    private String[][] Datensatz;
    private SourceCodeProperties sourceCode;
    private MatrixProperties Tabellen;
    private SourceCodeProperties Klassifizierer;
    
    public void init(){
        lang = new AnimalScript("Bagging [DE]", "Amir Mesbah", 800, 600);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        Testdatensatz = (String[][])primitives.get("Testdatensatz");
        Trainingssaetze = (int[][])primitives.get("Trainingssätze");
        Datensatz = (String[][])primitives.get("Datensatz");
     	
		sourceCode = (SourceCodeProperties)props.getPropertiesByName("sourceCode");
        Tabellen = (MatrixProperties)props.getPropertiesByName("Tabellen");
        Klassifizierer = (SourceCodeProperties)props.getPropertiesByName("Klassifizierer");
        
        String[] headers = new String[Datensatz[0].length];
        for(int i = 0; i < Datensatz[0].length; i++)
        		headers[i] = Datensatz[0][i];
        
        Bagging.main(lang, Testdatensatz.length, Trainingssaetze.length+1,Trainingssaetze[0].length, Datensatz[0].length, headers, Datensatz, Testdatensatz, Trainingssaetze, sourceCode, Tabellen, Klassifizierer);
        return lang.toString();
    }
    
    public String getName() {
        return "Bagging [DE]";
    }

    public String getAlgorithmName() {
        return "Bagging";
    }

    public String getAnimationAuthor() {
        return "Amir Mesbah";
    }

    public String getDescription(){
        return "Der Bragging-Algorithmus ermöglicht die Kombination der Ergebnisse von mehreren Klassifizierungsmodellen, sodass präzisere Vorhersagen getroffen werden können."
 +"\n"
 +"Umgesetzt wird dies indem m Trainingssätze (auch genannt bootstrap) mit x Fällen aus dem Datensatz durch zufälliges Ziehen mit zurücklegen gebildet werden. Auf jeden Trainingssatz wird ein eigener Klassifizierer trainiert."
 +"\n"
 +"Nun wird für eine Vorhersage die Ergebnisse aller m Klassifizier betracht, d.h. es werden m Vorhersagen gebildet. Die meist vorhergesagte Klasse ist die wahrscheinlichste Vorhersage und damit die Vorhersage des Algorithmus (bei unentschieden ist die Vorhersage 'Nein')."
 +"\n"
 +"Als Klassifizierer kann man ein beliebigen Algorithmus wählen. Hier wird der Separate-and-Conquer Algorithmus in der Variante des Top-Down Hill Climbing verwendet. Dabei wird ein Datensatz positiv klassifiziert, wenn es mehr erfüllte als unerfüllte Regeln vorhanden sind."
 +"\n"
 +"Dieser Algorithmus ist im Animal als eigenständiger Algorithmus näher erläutert.";
    }

    public String getCodeExample(){
        return "1. for m=1 to 'Anzahl der Trainingssätze'"
 +"\n"
 +"  a) fülle den Trainingssatz mit x Daten (zufälliges Auswählen mit Wiederholungen)"
 +"\n"
 +"  b) lerne auf den Trainingssatz ein Klassifizierer"
 +"\n"
 +"2. for m=1 to 'Anzahl der zu klassifizierenden Daten'"
 +"\n"
 +"  a) klassifiziere den Eintrag mit allen Klassifizierern"
 +"\n"
 +"  b) klassifiziere die Klasse die am meisten vorhergesagt wurde";
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
		String[][] testdatensatz = (String[][])primitives.get("Testdatensatz");
		int[][] trainingssaetze = (int[][])primitives.get("Trainingssätze");
		String[][] datensatz = (String[][])primitives.get("Datensatz");
        
		for(int i = 0; i < testdatensatz.length; i++)
        {
        		if(i != 0)
	        		if(!(testdatensatz[i][testdatensatz[0].length-1].equals("?")))
					throw new IllegalArgumentException("In der letzten Spalte vom Testdatensatz darf nur '?' enthalten (R: " + i + " C: " + (testdatensatz[i].length-1) + ")");
        }
        for(int i = 0; i < testdatensatz.length; i++)
        {
        		for(int j = 0; j < testdatensatz[i].length-1; j++)
        		{
        			if(testdatensatz[i][j].equals(""))
        				throw new IllegalArgumentException("Testdatensatz enthält ein leeres Feld (R: " + i + " C: " + j + ")");
        		}
        }
        
        if(datensatz[0].length != testdatensatz[0].length)
        		throw new IllegalArgumentException("Testdatensatz und Datensatz haben eine unterschiedliche Anzahl von Spalten");
        
    for(int j = 0; j < testdatensatz[0].length-1; j++)
		{
			if(!testdatensatz[0][j].equals(datensatz[0][j]))
				throw new IllegalArgumentException("Testdatensatz enthält eine andere Überschrifft (R: " + 0 + " C: " + j + ")");
		}
        
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
        
        for(int i = 0; i < trainingssaetze.length; i++)
        {
        		for(int j = 0; j < trainingssaetze[i].length; j++)
        		{
        			if(trainingssaetze[i][j] <= 0)
        				throw new IllegalArgumentException("Trainingssatz enthält eine Zahl kleiner 1 (R: " + i + " C: " + j + ")");
        			if(trainingssaetze[i][j] > datensatz.length-1)
        				throw new IllegalArgumentException("Trainingssatz enthält eine Zahl die größer ist als die Anzahl der Eintäge im Datensatz (R: " + i + " C: " + j + ")");
        		}
        }
        


        for(int i = 0; i < testdatensatz[0].length; i++) {
          if(!testdatensatz[0][i].equals(datensatz[0][i])) {
            throw new IllegalArgumentException("Trainingssatz Überschrift stimmt nicht mit Datensatz Überschrift überein an (R: " + 0 + " C: " + i + ")");
          }
        }
	        
        
		return true;
	}

}