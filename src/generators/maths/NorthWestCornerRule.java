package generators.maths;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.maths.northwestcornerrule.Algorithm;
import generators.maths.northwestcornerrule.AnimProps;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.SourceCodeProperties;

public class NorthWestCornerRule implements Generator {
    private Language lang;
    private int[] demandArray;
    private SourceCodeProperties sourceCode;
    private int[] supplyArray;

    public void init(){
        lang = new AnimalScript("NWCR", "Appadurai und Müller", 800, 600);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        demandArray = (int[])primitives.get("demandArray");
        sourceCode = (SourceCodeProperties)props.getPropertiesByName("sourceCode");
        supplyArray = (int[])primitives.get("supplyArray");
        
        Algorithm myAlgorithm = new Algorithm(supplyArray, demandArray, lang);
        AnimProps.SC_PROPS = sourceCode;
		myAlgorithm.animate();
        
        return myAlgorithm.getMyAnimationScript();
    }

    public String getName() {
        return "NWCR";
    }

    public String getAlgorithmName() {
        return "Nordwest-Ecken-Regel";
    }

    public String getAnimationAuthor() {
        return "Daniel Appadurai, Benjamin Müller";
    }

    public String getDescription(){
        return "Das Nord-West-Ecken-Verfahren ist ein Verfahren aus dem Operations Research,<br>" +
		"das eine zulässige Anfangslösung für das Transportproblem liefern soll.<br> " +
		"Von dieser Lösung aus startet der Optimierungsalgorithmus des Transportproblems.<br>" +
		"Gegeben sind eine Menge an Anbietern (Supplier) mir der jeweiligen Liefermenge. <br>" +
		"Ebenso sind eine Menge an Nachfragern (Demander) gegeben mit einer gewissen Nachfrage. <br>" +
		"Ziel ist es die Angebotsmenge auf die Nachfrage zu verteilen. <br>" +
		"Vorsicht: Die gefundene Aufteilung ist nicht notwendigerweise eine optimale Lösung. <br>" +
		"Sie stellt eine mögliche Lösung (Basislösung) dar, auf der andere Verfahren aufbauen können,<br>" +
		"um eine optimale Lösung des Transportproblems zu kalkulieren.<br>" +
		"Voraussetzung: Außerdem gilt als Grundvoraussetzung, dass die kumulierte Nachfrage dem <br>" +
		"kumulierten Angebot gleichen muss.<br><br>";
    }

    public String getCodeExample(){
        return "public int nordwestEckenRegel <br>" +
        " (int[] angebot, int[] nachfrage){<br>" +
        "int i = 0;<br>" +
        "int j = 0;<br>" + 
        "while (i<= angebot.length <br>" +
        "&& j <=nachfrage.length) {<br>" +
        "x = Math.min(angebot[i],nachfrage[i]);<br>" +
        "saveToBasis(i, j, x);<br>" +
        "angebot[i] -= x;<br>" +
        "nachfrage[j] -= x;<br>" +
        "if(angebot[i] == 0)<br>" +
        "i++;<br>" +
        "else<br>" +
        "j++;<br>" +
        "}<br>" +
        "}<br>";
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