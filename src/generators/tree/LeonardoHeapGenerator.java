/*
 * LeonardoHeapGenerator.java
 * Marko Dehmel, Dominik Hintersdorf, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.tree;

import generators.tree.helpersLeonardoHeap.LeonardoHeap;
import algoanim.primitives.*;
import algoanim.properties.*;
import algoanim.util.Coordinates;
import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.*;
import java.util.List;
import java.util.Locale;

import algoanim.primitives.generators.Language;

import java.util.Hashtable;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;

import generators.tree.helpersLeonardoHeap.*;

public class LeonardoHeapGenerator implements Generator {
    public static Language lang;
    private static ArrayProperties arrayProps;
    public static Color switchColor;
    public static Color selectColor;
    // own variables
    public static LeonardoHeap leoHeap;
    public static Coordinates rootCoords = new Coordinates(350, 150);
    private static IntArray ia;
    private static ArrayMarker arrayMarker;
    private static Text headerText;
    private static Rect headerBox;
    public static SourceCode threeCases;
    public static Variables variables;
    // own constants
    private static final int NODE_RADIUS = 23;
    private static final int NODE_GAP = 10;
    private static final int LEVEL_GAP = 100;

    public void init() {
        lang = new AnimalScript("Leonardo Heap", "Marko Dehmel, Dominik Hintersdorf", 800, 600);
        lang.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
        arrayProps = (ArrayProperties) props.getPropertiesByName("arrayProps");
        switchColor = (Color) primitives.get("switchColor");
        selectColor = (Color) primitives.get("selectColor");
        int[] intArray = (int[]) primitives.get("intArray");

        // declare and set the variables
        variables = lang.newVariables();
        variables.declare("int", "AnzahlSortierterElemente", Integer.toString(0), "STEPPER");

        variables.declare("int", "AnzahlDerHeaps", Integer.toString(0), "STEPPER");

        variables.declare("int", "TiefeDesErstenHeaps", Integer.toString(0), "STEPPER");

        leoHeap = new LeonardoHeap();

        ia = generateArray(intArray, arrayProps);
        arrayMarker = generateArrayMarker(ia);
        headerText = generateHeader();
        headerBox = generateHeaderBox();
        threeCases = generateThreeCases();
        generateLegend();

        Text[] introduction = generateIntroduction();

        lang.nextStep("Introduction");

        for (Text line : introduction) {
            line.hide();
        }

        buildLeoHeap(intArray);

        lang.nextStep("Built Heap");
        threeCases.hide();
        generateConclusion();

        lang.nextStep("Conclusion");

        return lang.toString();
    }

    private Text[] generateIntroduction() {
        TextProperties textProps = new TextProperties("description");
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 14));

        String stringLine1 = "Beim Leonardo Heap handelt es sich um eine spezielle Form eines Heaps. Im Gegensatz zu einem ";
        String stringLine12 = "gewöhnlichem Heap besteht der Leonardo Heap aus einer Liste von mehreren Heaps.";
        Text line1 = lang.newText(new Coordinates(350, 100), stringLine1, "description", null, textProps);
        Text line12 = lang.newText(new Coordinates(350, 117), stringLine12, "description", null, textProps);

        String stringLine2 = "Bei den einzelnen Heaps der Liste handelt es sich um Max-Heaps, weshalb das größte Elemente ";
        String stringLine22 = "des Heaps in der Wurzel steht. Desweiteren ist die Anzahl der Elemente eines Heaps an der ";
        Text line2 = lang.newText(new Coordinates(350, 134), stringLine2, "description", null, textProps);
        Text line22 = lang.newText(new Coordinates(350, 151), stringLine22, "description", null, textProps);

        String stringLine3 = "Position i in der Liste immer größer oder gleich der Anzahl der Elemente des Heaps an Position i-1.";
        Text line3 = lang.newText(new Coordinates(350, 168), stringLine3, "description", null, textProps);

        String stringLine4 = "Daraus folgt, dass das größte Element des Leonardo Heaps stehts die Wurzel des letzten Heaps ";
        String stringLine42 = "der Liste ist.";
        Text line4 = lang.newText(new Coordinates(350, 185), stringLine4, "description", null, textProps);
        Text line42 = lang.newText(new Coordinates(350, 202), stringLine42, "description", null, textProps);
        Text line43 = lang.newText(new Coordinates(350, 219), "", "description", null, textProps);

        String stringLine5 = "Beim Hinzufügen eines neuen Elements in den Leonardo Heap, wird zwischen den folgenden ";
        String stringLine52 = "zwei Fällen unterschieden:";
        Text line5 = lang.newText(new Coordinates(350, 236), stringLine5, "description", null, textProps);
        Text line52 = lang.newText(new Coordinates(350, 253), stringLine52, "description", null, textProps);
        Text line53 = lang.newText(new Coordinates(350, 270), "", "description", null, textProps);

        String stringLine6 = "1. Wenn sich die Ordnung der letzten zwei Heaps der Heapliste nur um eins oder weniger ";
        String stringLine62 = "unterscheidet, so werden diese zu einem Heap zusammengefügt mit dem neuen Knoten als Wurzel.";
        Text line6 = lang.newText(new Coordinates(360, 287), stringLine6, "description", null, textProps);
        Text line62 = lang.newText(new Coordinates(360, 304), stringLine62, "description", null, textProps);
        Text line63 = lang.newText(new Coordinates(360, 321), "", "description", null, textProps);

        String stringLine7 = "2. Ansonsten wird das neue Element als Heap der Heapliste hinzugefügt.";
        Text line7 = lang.newText(new Coordinates(360, 338), stringLine7, "description", null, textProps);
        Text line72 = lang.newText(new Coordinates(360, 355), "", "description", null, textProps);

        String stringLine8 = "Nach dem Einfügen des neuen Elements werden alle Heaps jeweils sortiert. Dadurch wird zum einen ";
        String stringLine82 = "gewährleistet, dass sich in der Wurzel der Heaps stehts das größte Element befinden und zum ";
        String stringLine83 = "anderen, dass die Ordnung der Heaps von gegen Ende der Heapliste immer kleiner wird.";
        Text line8 = lang.newText(new Coordinates(350, 372), stringLine8, "description", null, textProps);
        Text line82 = lang.newText(new Coordinates(350, 389), stringLine82, "description", null, textProps);
        Text line83 = lang.newText(new Coordinates(350, 406), stringLine83, "description", null, textProps);
        Text line84 = lang.newText(new Coordinates(350, 423), "", "description", null, textProps);

        String stringLine9 = "Im Folgenden soll das obige Array in einen Leonardo Heap umgewandelt werden.";
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 14));
        Text line9 = lang.newText(new Coordinates(350, 440), stringLine9, "description", null, textProps);

        String stringLine10 = "Zum besseren Verständnis wird der aktuell zutreffende Fall markiert.";
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 14));
        Text line10 = lang.newText(new Coordinates(350, 457), stringLine10, "description", null, textProps);

        return new Text[]{line1, line12, line2, line22, line3, line4, line42, line43, line5, line52, line53, line6, line62, line63, line7, line72, line8, line82, line83, line84, line9, line10};
    }

    private void generateConclusion() {
        TextProperties textProperties = new TextProperties("conclusion");
        textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 14));

        String lineString = "Das Array wurde nun in einen Leonardo";
        String lineString2 = "Heap umgewandelt und erfüllt die ";
        String lineString3 = "folgenden Bedingungen:";
        lang.newText(new Coordinates(10, 100), lineString, "conclusion", null, textProperties);
        lang.newText(new Coordinates(10, 117), lineString2, "conclusion", null, textProperties);
        lang.newText(new Coordinates(10, 134), lineString3, "conclusion", null, textProperties);
        lang.newText(new Coordinates(10, 151), "", "conclusion", null, textProperties);

        String firstBp = "- Ordnung der Heaps nimmt immer weiter ab";
        lang.newText(new Coordinates(10, 168), firstBp, "conclusion", null, textProperties);

        String secondBp = "- Größtes Element ist Wurzel des letzten Heaps";
        lang.newText(new Coordinates(10, 188), secondBp, "conclusion", null, textProperties);

        String thirdBp = "- Elemente in den Heaps werden immer größer";
        lang.newText(new Coordinates(10, 208), thirdBp, "conclusion", null, textProperties);
    }

    private static void generateLegend() {
        RectProperties greenRectProps = new RectProperties();
        greenRectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, arrayProps.get("cellHighlight"));
        greenRectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);

        RectProperties yellowRectProps = new RectProperties();
        yellowRectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, selectColor);
        yellowRectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);

        RectProperties lightGreenProps = new RectProperties();
        lightGreenProps.set(AnimationPropertiesKeys.FILL_PROPERTY, switchColor);
        lightGreenProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);

        lang.newRect(new Coordinates(255, 40), new Coordinates(265, 50), "greenBox", null, greenRectProps);
        lang.newRect(new Coordinates(255, 55), new Coordinates(265, 65), "yellowBox", null, yellowRectProps);
        lang.newRect(new Coordinates(255, 70), new Coordinates(265, 80), "lightGreenBox", null, lightGreenProps);

        Font font = new Font("SansSerif", Font.PLAIN, 13);

        TextProperties textProperties = new TextProperties("sortiert");
        textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, font);

        TextProperties textProperties1 = new TextProperties("ausgewählt");
        textProperties1.set(AnimationPropertiesKeys.FONT_PROPERTY, font);

        TextProperties textProperties2 = new TextProperties("getauscht");
        textProperties2.set(AnimationPropertiesKeys.FONT_PROPERTY, font);

        lang.newText(new Coordinates(270, 37), "sortiert", "legend", null, textProperties);
        lang.newText(new Coordinates(270, 52), "ausgewählt", "legend", null, textProperties1);
        lang.newText(new Coordinates(270, 67), "getauscht", "legend", null, textProperties2);
    }

    private SourceCode generateThreeCases() {
        SourceCodeProperties scProps = new SourceCodeProperties();
        scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.RED);
        scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 14));
        scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
        scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

        SourceCode sc = lang.newSourceCode(new Coordinates(10, 100), "threeCases", null, scProps);

        sc.addCodeLine("1. Merge Heaps: ", null, 0, null);
        sc.addCodeLine("Die letzten beiden Heaps werden mit dem", "firstCase", 1, null);
        sc.addCodeLine("neuem Element als Wurzel gemerged.", "firstCase1", 1, null);
        sc.addCodeLine("", null, 0, null);
        sc.addCodeLine("2. Add new Heap: ", null, 0, null);
        sc.addCodeLine("Neuer Heap wird hinzugefügt.", "secondCase", 1, null);
        sc.addCodeLine("", null, 0, null);
        sc.addCodeLine("", null, 0, null);
        sc.addCodeLine("Alle Heaps werden sortiert.", "thirdCase", 1, null);

        return sc;
    }

    private Text generateHeader() {
        TextProperties textProps = new TextProperties("Smooth Sort");
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 24));
        textProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
        return lang.newText(new Coordinates(15, 25), "Leonardo Heap", "Header", null, textProps);
    }

    private ArrayMarker generateArrayMarker(IntArray array) {
        ArrayMarkerProperties arrayMarkerProperties = new ArrayMarkerProperties();
        arrayMarkerProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        return lang.newArrayMarker(array, 0, "", null, arrayMarkerProperties);
    }

    private Rect generateHeaderBox() {
        RectProperties rectProperties = new RectProperties();
        rectProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.PINK);
        rectProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        rectProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
        return lang.newRect(new Coordinates(10, 20), new Coordinates(200, 60), "box", null, rectProperties);
    }

    private IntArray generateArray(int[] a, ArrayProperties arrayProp) {
        ArrayProperties arrayProps = new ArrayProperties();
        arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, arrayProp.get("color"));
        arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, arrayProp.get("fillColor"));
        arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
        arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, arrayProp.get("elementColor"));
        arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, arrayProp.get("cellHighlight"));
        arrayProps.set(AnimationPropertiesKeys.FONT_PROPERTY, arrayProp.get("font"));

        IntArray ia = lang.newIntArray(new Coordinates(350, 45), a, "intArray", null, arrayProps);

        ia.showIndices(false, null, null);
        return ia;
    }

    private void buildLeoHeap(int[] array) {

        for (int i = 0; i < array.length; i++) {
            LeonardoHeapHeap heap = new LeonardoHeapHeap(array[i]);
            lang.nextStep("Add " + array[i]);
            leoHeap.addHeap(heap);
            int heapWidth = getHeapWidth(leoHeap.getHeapList().get(0));
            rootCoords = new Coordinates(350 + heapWidth / 2, rootCoords.getY());
            drawLeoHeap(leoHeap.getHeapList(), rootCoords);
            lang.nextStep();
            threeCases.unhighlight("firstCase");
            threeCases.unhighlight("firstCase1");
            threeCases.unhighlight("secondCase");
            leoHeap.sortHeapListByRoot(leoHeap.getHeapList());
            threeCases.unhighlight("thirdCase");
            arrayMarker.move(i + 1, null, null);
            ia.highlightCell(i, null, null);
            variables.set("AnzahlSortierterElemente", Integer.toString(i + 1));
        }
    }

    public static void drawLeoHeap(List<LeonardoHeapHeap> heapList, Coordinates rootCoords) {
        lang.hideAllPrimitives();
        ia.show();
        arrayMarker.show();
        headerText.show();
        headerBox.show();
        threeCases.show();
        generateLegend();
        for (int i = 0; i < heapList.size() - 1; i++) {
            drawHeap(heapList.get(i), rootCoords);
            int width1 = getHeapWidth(heapList.get(i));
            int width2 = getHeapWidth(heapList.get(i + 1));
            rootCoords = new Coordinates(rootCoords.getX() + (width1 + width2) / 2 + 50, rootCoords.getY());
        }
        drawHeap(heapList.get(heapList.size() - 1), rootCoords);
    }

    public static int getHeapWidth(LeonardoHeapHeap heap) {
        int order = heap.getOrder();
        int numberOfNodes = (int) Math.pow(2, order);
        return (2 * NODE_RADIUS) * numberOfNodes + (NODE_GAP * (numberOfNodes - 1));
    }

    private static void drawHeap(LeonardoHeapHeap heap, Coordinates rootCoordinates) {
        int order = heap.getOrder();
        float numberOfGaps;
        float numberOfNodes;
        if (order > 1) {
            numberOfGaps = (int) (Math.pow(2, order - 2));
            numberOfNodes = (int) (Math.pow(2, order - 2));
        } else {
            numberOfGaps = 1;
            numberOfNodes = 0.5f;
        }
        drawHeapRecursive(heap.getRoot(), rootCoordinates, numberOfGaps, numberOfNodes);
    }

    private static void drawHeapRecursive(LeonardoHeapNode root, Coordinates coordinates, float numberOfGaps, float numberOfNodes) {
        if (root == null) {
            return;
        }

        // draw the root
        drawNode(root, coordinates);

        if (root.getLeftChild() == null || root.getRightChild() == null) {
            return;
        }

        // draw left node
        Coordinates leftChildCoords = new Coordinates(coordinates.getX() - (int) (numberOfGaps * NODE_GAP) - (int) (numberOfNodes * (NODE_RADIUS * 2)), coordinates.getY() + LEVEL_GAP);
        drawHeapRecursive(root.getLeftChild(), leftChildCoords, numberOfGaps / 2, numberOfNodes / 2);

        // draw right node
        Coordinates rightChildCoords = new Coordinates(coordinates.getX() + (int) (numberOfGaps * NODE_GAP) + (int) (numberOfNodes * (NODE_RADIUS * 2)), coordinates.getY() + LEVEL_GAP);
        drawHeapRecursive(root.getRightChild(), rightChildCoords, numberOfGaps / 2, numberOfNodes / 2);

        // draw the line to the left and the right node
        drawConnectionLine(coordinates, leftChildCoords);
        drawConnectionLine(coordinates, rightChildCoords);
    }

    private static void drawConnectionLine(Coordinates coord1, Coordinates coord2) {
        PolylineProperties polyProps = new PolylineProperties();
        polyProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
        lang.newPolyline(new Coordinates[]{coord1, coord2}, "treeLine", null, polyProps);
    }

    private static void drawNode(LeonardoHeapNode node, Coordinates coordinates) {
        CircleProperties circleProps = new CircleProperties();
        circleProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GRAY);
        circleProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        circleProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

        int value = node.getValue();

        node.circle = lang.newCircle(coordinates, NODE_RADIUS, Integer.toString(value), null, circleProps);

        TextProperties textProps = new TextProperties("CircleValue");
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 14));
        int newXCoord;
        if (value >= 0) {
            if (value <= 9) {
                newXCoord = coordinates.getX() - 4;
            } else if (value < 99) {
                newXCoord = coordinates.getX() - 8;
            } else if (value < 999) {
                newXCoord = coordinates.getX() - 12;
            } else {
                newXCoord = coordinates.getX() - 16;
            }
        } else {
            if (value > -9) {
                newXCoord = coordinates.getX() - 8;
            } else if (value > -99) {
                newXCoord = coordinates.getX() - 12;
            } else if (value > -999) {
                newXCoord = coordinates.getX() - 16;
            } else {
                newXCoord = coordinates.getX() - 20;
            }
        }
        lang.newText(new Coordinates(newXCoord, coordinates.getY() - 8), Integer.toString(value), "circleValue", null, textProps);
    }

    public String getName() {
        return "Leonardo Heap";
    }

    public String getAlgorithmName() {
        return "Aufbau eines Leonardo Heaps";
    }

    public String getAnimationAuthor() {
        return "Marko Dehmel, Dominik Hintersdorf";
    }

    public String getDescription() {
        return "Beim Leonardo Heap handelt es sich um eine spezielle Form eines Heaps. Im Gegensatz zu einem gewöhnlichem Heap besteht der Leonardo Heap aus einer Liste von mehreren Heaps."
                + "Bei den einzelnen Heaps der Liste handelt es sich um Max-Heaps, weshalb das größte Elemente des Heaps in der Wurzel steht."
                + "Desweiteren ist die Anzahl der Elemente eines Heaps an der Position i in der Liste immer größer oder gleich der Anzahl der Elemente des Heaps an Position i-1."
                + "Daraus folgt, dass das größte Element des Leonardo Heaps stehts die Wurzel des letzten Heaps der Liste ist. \n"
                + "\n"
                + "Beim Hinzufügen eines neuen Elements in den Leonardo Heap, wird zwischen den folgenden zwei Fällen unterschieden:"
                + "\n"
                + "\n"
                + "1. Wenn sich die Ordnung der letzten zwei Heaps der Heapliste nur um eins oder weniger unterscheidet, so werden diese zu einem Heap zusammengefügt mit dem neuen Knoten als Wurzel."
                + "\n"
                + "\n"
                + "2. Ansonsten wird das neue Element als Heap der Heapliste hinzugefügt."
                + "\n"
                + "\n"
                + "Nach dem Einfügen des neuen Elements werden alle Heaps jeweils sortiert. Dadurch wird zum einen gewährleistet, dass sich in der Wurzel der Heaps stehts das größte Element befinden und zum anderen, dass die Ordnung der Heaps von gegen Ende der Heapliste immer kleiner wird.";
    }

    public String getCodeExample() {
        return "Merge Heaps:"
                + "\n"
                + "    Heaps werden gemerged."
                + "\n"
                + "\n"
                + "Add new Heap:"
                + "\n"
                + "    Neuer Heap wird hinzugefügt."
                + "\n"
                + "\n"
                + "Sort Heap:"
                + "\n"
                + "    Heap wird sortiert.";
    }

    public String getFileExtension() {
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_TREE);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }
}