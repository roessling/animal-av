/*
 * BlumBlumShub.java
 * Adrian Lumpe, Chi Viet Vu, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.maths;

import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.*;
import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.PolylineGenerator;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.Timing;

import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import translator.Translator;

public class BlumBlumShub implements ValidatingGenerator {
    private Translator translator;
    private Language lang;
    private int p;
    private int q;
    private int s;
    private int amountGenerated;

    private PolylineProperties graphProperties;
    private SourceCodeProperties sourceCodeProperties;
    private TextProperties textProperties;

    private Text header;
    private Text subHeader;
    private TextProperties subHeaderProps;
    private SourceCode src;

    public BlumBlumShub(String path, Locale locale) {
        translator = new Translator(path, locale);
    }

    public void init(){
        lang = new AnimalScript("Blum-Blum-Shub Zufallszahlengenerator", "Adrian Lumpe, Chi Viet Vu", 800, 600);
        lang.setStepMode(true);
    }

    public boolean validateInput(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        //p = (Integer)primitives.get("p");
        //q = (Integer)primitives.get("q");
        //s = (Integer)primitives.get("startValue");
        //amountGenerated = (Integer)primitives.get("amountGenerated");

        boolean tmp = true;
        if(((Integer)primitives.get("p")-3) % 4 != 0) tmp = false;
        if(((Integer)primitives.get("q")-3) % 4 != 0) tmp = false;
        return tmp;
    }

    public void start() {
        TextProperties headerProps = new TextProperties();
        headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.BOLD, 24));

        header = lang.newText(new Coordinates(20, 30), translator.translateMessage("header"),
                "header", null, headerProps);

        subHeader = lang.newText(new Coordinates(20, 60), translator.translateMessage("subHeaderOverview"),
                "subHeader", null, subHeaderProps);

        // setup the start page with the description
        lang.nextStep();
        lang.newText(new Coordinates(20, 100),
                translator.translateMessage("description00"),
                "description1", null, textProperties);
        lang.newText(new Offset(0, 25, "description1",
                        AnimalScript.DIRECTION_NW),
                translator.translateMessage("description01"),
                "description2", null, textProperties);

        lang.nextStep();
        showSteps();
    }

    private void showSteps() {
        int lineHeight = 20;
        int yOffset = 16;

        lang.newText(new Offset(0, 30, "description2",
                        AnimalScript.DIRECTION_NW),
                translator.translateMessage("description10"),
                "algo11", null, textProperties);
        lang.newText(new Offset(yOffset, lineHeight, "algo11", AnimalScript.DIRECTION_NW),
                translator.translateMessage("description11"),
                "algo12", null, textProperties);
        lang.newText(new Offset(0, lineHeight, "algo12", AnimalScript.DIRECTION_NW),
                translator.translateMessage("description12"), "algo13", null, textProperties);

        lang.nextStep();
        lang.newText(new Offset(-yOffset, lineHeight, "algo13", AnimalScript.DIRECTION_NW),
                translator.translateMessage("description20"),
                "algo21", null, textProperties);
        lang.newText(new Offset(yOffset, lineHeight, "algo21", AnimalScript.DIRECTION_NW),
                translator.translateMessage("description21"),
                "algo22", null, textProperties);
        lang.newText(new Offset(0, lineHeight, "algo22", AnimalScript.DIRECTION_NW),
                translator.translateMessage("description22"), "algo23", null, textProperties);
        lang.newText(new Offset(0, lineHeight, "algo23", AnimalScript.DIRECTION_NW),
                translator.translateMessage("description23"), "algo24", null, textProperties);
        lang.newText(new Offset(0, lineHeight, "algo24", AnimalScript.DIRECTION_NW),
                translator.translateMessage("description24"), "algo25", null, textProperties);
        lang.nextStep();
        lang.newText(new Offset(-yOffset, lineHeight, "algo25", AnimalScript.DIRECTION_NW),
                translator.translateMessage("description30"), "algo26", null, textProperties);
        lang.newText(new Offset(yOffset, lineHeight, "algo26", AnimalScript.DIRECTION_NW),
                translator.translateMessage("description31"), "algo27", null, textProperties);

        lang.nextStep();
        blum();
    }

    private void blum() {
        // This creates a blank page
        lang.hideAllPrimitives();
        header.show();

        subHeader.setText(translator.translateMessage("subHeaderAlgo"), null, null);
        subHeader.show();

        displayPrimitives();

        int currentIteration = 0;
        int currentS = s;

        Text currentIterationLabel = lang.newText(new Coordinates(20, 180), translator.translateMessage("labelIteration") + " " + Integer.toString(currentIteration),
                "header", null, subHeaderProps);

        Text currentSLabel = lang.newText(new Coordinates(20, 200), translator.translateMessage("labelCurrentS") + " " + Integer.toString(currentS),
                "header", null, subHeaderProps);

        TextProperties captionProps = new TextProperties();
        captionProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.BOLD, 24));

        Text caption = lang.newText(new Coordinates(500, 100), translator.translateMessage("labelGeneratedNumbers"),
                "header", null, subHeaderProps);

        lang.newPolyline(new Node[] {new Coordinates(500, 132), new Coordinates(850, 132)}, "pl", null);

        Text[] numLabels = new Text[amountGenerated];
        int[] numbers = new int[amountGenerated];

        displayCode();
        lang.nextStep();
        changeCodeHighlight(0, 0, true);
        changeCodeHighlight(0, 1, true);
        changeCodeHighlight(1, 2, true);
        src.unhighlight(2);

        for(int i = 0; i < amountGenerated; i++) {
            currentIterationLabel.setText(translator.translateMessage("labelIteration") + " " + ++currentIteration, null, null);
            changeCodeHighlight(8, 4, true);
            changeCodeHighlight(4, 5, true);
            changeCodeHighlight(5, 6, false);

            s = (int) (Math.pow(s, 2) % (p*q));
            currentSLabel.setText(translator.translateMessage("labelCurrentS") + " " + Integer.toString(s), null, null);
            numbers[i] = s;
            displayGeneratedNumbers(numLabels, numbers, i);
            lang.nextStep();

            changeCodeHighlight(6, 7, true);
            changeCodeHighlight(7, 8, true);
        }

        for(int i = 0; i < numLabels.length; i++) {
            if(numLabels[i] != null) numLabels[i].setText("", null, null);
        }

        showSummary(numbers);
    }

    private void changeCodeHighlight(int hide, int show, boolean step) {
        src.unhighlight(hide);
        src.highlight(show);
        if(step) lang.nextStep();
    }

    private void displayPrimitives() {
        int yCoordPrims = 130;
        int yCoordCaptions = 100;

        TextProperties valueProps = new TextProperties();
        valueProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.PLAIN, 20));

        lang.newPolyline(new Node[] {new Coordinates(20, yCoordCaptions + 32), new Coordinates(450, yCoordCaptions + 32)}, "pl", null);

        lang.newPolyline(new Node[] {new Coordinates(85, yCoordCaptions), new Coordinates(85, yCoordCaptions + 60)}, "pl", null);
        lang.newPolyline(new Node[] {new Coordinates(155, yCoordCaptions), new Coordinates(155, yCoordCaptions + 60)}, "pl", null);
        lang.newPolyline(new Node[] {new Coordinates(225, yCoordCaptions), new Coordinates(225, yCoordCaptions + 60)}, "pl", null);

        Text pCaption = lang.newText(new Coordinates(20, yCoordCaptions), "p",
                "header", null, subHeaderProps);
        Text qCaption = lang.newText(new Coordinates(90, yCoordCaptions), "q",
                "header", null, subHeaderProps);
        Text nCaption = lang.newText(new Coordinates(160, yCoordCaptions), "n (pq)",
                "header", null, subHeaderProps);
        Text iterationAmountCaption = lang.newText(new Coordinates(230, yCoordCaptions), translator.translateMessage("labelTotalIterations"),
                "header", null, subHeaderProps);

        Text pText = lang.newText(new Coordinates(20, yCoordPrims), Integer.toString(p),
                "header", null, valueProps);
        Text qText = lang.newText(new Coordinates(90, yCoordPrims), Integer.toString(q),
                "header", null, valueProps);
        Text nText = lang.newText(new Coordinates(160, yCoordPrims), Integer.toString(p*q),
                "header", null, valueProps);
        Text iterationAmountText = lang.newText(new Coordinates(230, yCoordPrims), Integer.toString(amountGenerated),
                "header", null, valueProps);
    }

    private void displayGeneratedNumbers(Text[] labels, int[] numbers, int i) {
        int yCoord = 130;

        TextProperties numProps = new TextProperties();
        numProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.PLAIN, 20));

        labels[i] = lang.newText(new Coordinates(500 + (i % 5)*70, yCoord + 30*(int)Math.floor(i/5)), Integer.toString(numbers[i]) + ",",
                    "header", null, numProps);
        labels[i].show();
    }

    private void displayCode() {
        src = lang.newSourceCode(new Coordinates(20, 230), "sourceCodeProperties",
                null, sourceCodeProperties);
        src.addCodeLine("int[] numbers = int[" + Integer.toString(amountGenerated) + "]", null, 0,
                null);
        src.addCodeLine("int counter = 0; // Iterationszähler", null, 0,
                null);
        src.addCodeLine("int s = " + Integer.toString(s) + "; // Startwert", null, 0,
                null);
        src.addCodeLine("", null, 0,
                null);
        src.addCodeLine("while(counter < " + Integer.toString(amountGenerated) + ") {", null, 0,
                null); // 0
        src.addCodeLine("s = Math.pow(s, 2) % " + Integer.toString(p*q) + ";", null, 1, null);
        src.addCodeLine("numbers[counter] = s;", null, 1, null);
        src.addCodeLine("counter++;", null, 1, null);
        src.addCodeLine("}", null, 0, null); // 18
    }

    private void showSummary(int[] nums) {
        // This creates a blank page
        lang.hideAllPrimitives();
        header.show();

        subHeader.setText(translator.translateMessage("subHeaderSummary"), null, null);
        subHeader.show();

        lang.newText(new Coordinates(20, 100), translator.translateMessage("summary00"),
                "summary00", null, textProperties);

        String acc = "";
        for(int i = 0; i < nums.length; i++) {
            acc = acc + Integer.toString(nums[i]);
            if(i != nums.length-1) acc = acc + ", ";
        }
        lang.newText(new Offset(0, 20, "summary00", AnimalScript.DIRECTION_NW), acc,
                "", null, textProperties);

        lang.newText(new Coordinates(20, 160), translator.translateMessage("subHeaderGraph"),
                "distHeader", null, subHeaderProps);

        lang.newText(new Offset(0, 30, "distHeader", AnimalScript.DIRECTION_NW), translator.translateMessage("summary10"),
                "", null, textProperties);

        int xOrigin = 20;
        int yOrigin = 400;
        int height = 150;
        int width = 500;

        int xOffset = width/amountGenerated;
        int yFactor = p*q/height;

        Node[] nodes = new Node[nums.length];

        lang.newPolyline(new Node[] {new Coordinates(xOrigin,  yOrigin), new Coordinates(xOrigin + width,  yOrigin)}, "pl", null);
        lang.newPolyline(new Node[] {new Coordinates(xOrigin,  yOrigin), new Coordinates(xOrigin,  yOrigin-height)}, "pl", null);

        for(int i = 0; i < nums.length; i++) {
            nodes[i] = new Coordinates(xOrigin + i*xOffset,  yOrigin - nums[i]/yFactor);
        }

        lang.newPolyline(nodes, "pl", null, graphProperties);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        p = (Integer)primitives.get("p");
        q = (Integer)primitives.get("q");
        s = (Integer)primitives.get("startValue");
        amountGenerated = (Integer)primitives.get("amountGenerated");

        graphProperties = (PolylineProperties)props.getPropertiesByName("graphProperties");
        sourceCodeProperties = (SourceCodeProperties)props.getPropertiesByName("sourceCodeProperties");
        textProperties = (TextProperties)props.getPropertiesByName("textProperties");

        subHeaderProps = new TextProperties();
        subHeaderProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.BOLD, 18));

        start();
        
        return lang.toString();
    }

    public String getName() {
        return "Blum-Blum-Shub Zufallszahlengenerator";
    }

    public String getAlgorithmName() {
        return "Blum-Blum-Shub";
    }

    public String getAnimationAuthor() {
        return "Adrian Lumpe, Chi Viet Vu";
    }

    public String getDescription(){
        return "Der Blum-Blum-Shub-Generator ist ein Pseudozufallszahlengenerator. Er wurde im Jahre 1986 entwickelt und findet u. a. in der Kryptologie Anwendung."
            +"\n"
            +"\n"
            +"Der Generator ist definiert als Folge von Quadratresten modulo einer schwer faktorisierbaren Zahl, auch Blum-Zahl genannt. Diese Blum-Zahl ergibt sich als Produkt zweier Primzahlen."
            +"\n"
            +"Abhängig von einem Startwert ist der Generator in der Lage eine pseudozufällige Zahlenfolge zu erzeugen. In der Anwendung werden u. a. Zufallsbits generiert, indem nur bestimmte Bits dieser Zahlen betrachtet werden."
            +"\n"
            +"\n"
            +"Quelle: https://de.wikipedia.org/wiki/Blum-Blum-Shub-Generator";
    }

    public String getCodeExample(){
        return "int n = p*q;"
             +"\n"
             +"int startValue;"
             +"\n"
             +"\n"
             +"//   returns the next pseudorandom value"
             +"\n"
             +"public int next(int startValue, int n) {"
             +"\n"
             +"    int result = (int) (Math.pow(startValue, 2) % n);"
             +"\n"
             +"    return result;"
             +"\n"
             +"}"
             +"\n";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }

    public static void main(String[] args) {
        Generator gen = new BlumBlumShub("resources/BlumBlumShub", Locale.US);
        Animal.startGeneratorWindow(gen);
    }
}

