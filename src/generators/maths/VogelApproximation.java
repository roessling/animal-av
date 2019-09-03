package generators.maths;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;

import algoanim.primitives.generators.Language;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;

import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.maths.northwestcornerrule.AnimProps;
import generators.maths.vogelApprox.Algorithm;
import algoanim.animalscript.AnimalScript;

public class VogelApproximation implements Generator {
    private Language lang;
    private int[] demandArray;
    private int[] supplyArray;
    private int[][] costArray;
    private SourceCodeProperties sourceCode;
    private RectProperties headerProps;
    
    public void init(){
        lang = new AnimalScript("VogelApprox", "Appadurai und Müller", 800, 600);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        demandArray = (int[])primitives.get("demandArray");
        supplyArray = (int[])primitives.get("supplyArray");
        costArray = (int[][])primitives.get("costArray");
        sourceCode = (SourceCodeProperties)props.getPropertiesByName("sourceCode");
        headerProps = (RectProperties)props.getPropertiesByName("headerProps");
        
        Algorithm myAlgorithm = new Algorithm(supplyArray, demandArray, costArray, lang);
        AnimProps.SC_PROPS = sourceCode;
        AnimProps.RECT_HEADER_PROPS = headerProps;
		myAlgorithm.animate();
        
        return myAlgorithm.getMyAnimationScript();
    }

    public String getName() {
        return "VogelApprox";
    }

    public String getAlgorithmName() {
        return "Vogelsche Approximationsmethode";
    }

    public String getAnimationAuthor() {
        return "Daniel Appadurai, Benjamin Müller";
    }

    
	    
    public String getDescription(){
        return "Die Vogelsche Approximationsmethode ist ein heuristisches Verfahren aus dem Bereich<br>" +
        "des Operations Research zur Lösung eines Transportproblems.<br>" +
        "Diese Methode zeichnet sich dadurch aus, dass sie dem Optimum schon sehr nahe kommt.<br>" +
        "Der Aufwand ist allerdings gegenueber anderen Methoden, wie z. B. dem<br>" +
        "Nord-West-Ecken-Verfahren oder dem Matrixminimumverfahren, vergleichsweise hoch.<br><br>" +
        "Quelle: http://de.wikipedia.org/wiki/Vogelsche_Approximationsmethode<br><br>";

    }

    public String getCodeExample(){
        return 
        	"1.  Berechne für jede (aktive) Zeile und Spalte der Kostenmatrix <br>" +
        	"    die Differenz aus dem kleinsten und zweit-kleinsten <br>" +
        	"    Element der entsprechenden Zeile/ Spalte. <br>" +
        	"2.  Wähle die Zeile oder Spalte aus bei der sich die größte <br>" +
        	"	 Differenz ergab (grün). <br>" +
        	"3.  Das kleinste Element der entsprechenden Spalte <br>" +
        	"	 (bzw. Zeile) gibt nun die Stelle an, welche im <br>" +
        	"	 Transporttableau berechnet wird (blau). <br>" +
        	"4.  Nun wird der kleinere Wert von Angebots- und <br>" +
        	"	 Nachfragevektor im Tableau eingetragen. <br>" +
        	"5.  Anschließend wird der eingetragene Wert von den Rändern <br>" +
        	"	 abgezogen (mindestens einer muss 0 werden). <br>" +
        	"6.  Ist nun der Wert im Nachfragevektor Null so markiere <br>" +
        	"	 die entsprechende Spalte in der Kostenmatrix. Diese <br>" +
        	"	 wird nun nicht mehr beachtet (rot). Ist der Wert des <br>" +
        	"	 Angebotsvektors Null markiere die Zeile der Kostenmatrix. <br>" +
        	"7.  Der Algorithmus wird beendet, falls lediglich eine Zeile oder <br>" +
        	"	 Spalte der Kostenmatrix unmarkiert ist (eines reicht aus). <br>" +
        	"8 . Der entsprechenden Zeile bzw. Spalte im Transporttableau werden <br>" +    
        	"	 die restlichen Angebots- und Nachfragemengen zugeordnet. <br>";
    }

    public String getFileExtension(){
        return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
    }

    public Locale getContentLocale() {
        return Locale.GERMANY;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }

}