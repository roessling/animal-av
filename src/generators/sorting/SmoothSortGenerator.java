/*
 * generators.sorting.SmoothSortGenerator.java
 * Marko Dehmel, Dominik Hintersdorf, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */

package generators.sorting;

// all animal imports

import algoanim.primitives.*;
import algoanim.primitives.generators.Language;
import generators.sorting.helpersSmoothSort.SmoothSortLeonardoHeap;
import generators.sorting.helpersSmoothSort.SmoothSortHeap;
import algoanim.properties.*;
import algoanim.util.Coordinates;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;

// java imports
import java.awt.*;
import java.util.List;
import java.util.Locale;
import java.util.Hashtable;

// own class imports
import generators.sorting.helpersSmoothSort.SmoothSortNode;

public class SmoothSortGenerator implements Generator {
    private Language lang;
    private ArrayProperties arrayProp;
    private Color selectColor;

    private final int NODE_RADIUS = 23;
    private final int NODE_GAP = 10;

    public void init() {
        lang = new AnimalScript("Smooth Sort mit Leonardo Heaps", "Marko Dehmel, Dominik Hintersdorf", 800, 600);
        lang.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
        arrayProp = (ArrayProperties) props.getPropertiesByName("arrayProp");
        ArrayMarkerProperties arrayMarkerProp = (ArrayMarkerProperties) props.getPropertiesByName("arrayMarkerProp");
        SourceCodeProperties sourceCodeProp = (SourceCodeProperties) props.getPropertiesByName("sourceCodeProp");
        selectColor = (Color) primitives.get("selectColor");
        int[] intArray = (int[]) primitives.get("intArray");

        sortArray(intArray, arrayProp, arrayMarkerProp, sourceCodeProp);

        return lang.toString();
    }

    public String getName() {
        return "Smooth Sort mit Leonardo Heaps";
    }

    public String getAlgorithmName() {
        return "Smooth Sort";
    }

    public String getAnimationAuthor() {
        return "Marko Dehmel, Dominik Hintersdorf";
    }

    public String getDescription() {
        return "Bei Smoothsort handelt es sich um ein Verfahren zum sortieren von Arrays. " +
                "Hierzu werden intern Leanoardo Heaps anstatt normalen Heaps verwendet. " +
                "Der Algorithmus wurde von Edsger Dijkstra entwickelt und ist eine Variation von Heapsort. " +
                "Im Gegensatz zu Heapsort nutzt Smoothsort es aus, wenn das zu sortierende Array bereits bis zu einem gewissen Grad vorsortiert ist. " +
                "Im Best-Case führt dies dazu, dass die Laufzeit auf O(n) reduziert wird. " +
                "Jedoch bietet Smoothsort im Worst-Case und Average-Case keine Verbesserung der Laufzeit gegenüber dem herkömmlichen Heapsort mit O(n*log(n)), " +
                "weshalb der Algorithmus selten verwendet wird. ";
    }

    public String getCodeExample() {
        return "public void smoothSort(int[] array) {"
                + "\n"
                + "    generateLeonardoHeap(array);"
                + "\n"
                + "    "
                + "\n"
                + "    for(int i = array.length-1; i>=0; i--) {"
                + "\n"
                + "        array[i] = list.getLast().root;"
                + "\n"
                + "    }"
                + "\n"
                + "}";
    }

    public String getFileExtension() {
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }

    private Text[] generateIntroductionText() {
        TextProperties textProps = new TextProperties("description");
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 14));

        String line1String = "Im Folgenden soll das obige Array mit Hilfe eines Leonardo Heaps sortiert werden.";
        Text line1 = lang.newText(new Coordinates(350, 100), line1String, "description1", null, textProps);

        String line2String = "Dabei werden vorsortierte Abschnitte des Arrays ausgenutzt, um die Laufzeit zu";
        Text line2 = lang.newText(new Coordinates(350, 117), line2String, "description2", null, textProps);

        String line3String = "reduzieren. Hierfür werden die Elemente aus dem Leonardo SmoothSortHeap entnommen";
        Text line3 = lang.newText(new Coordinates(350, 134), line3String, "description3", null, textProps);

        String line4String = "und an der passenden Stelle des Arrays einsortiert.";
        Text line4 = lang.newText(new Coordinates(350, 151), line4String, "description4", null, textProps);

        return new Text[]{line1, line2, line3, line4};
    }

    private void sortArray(int[] a, ArrayProperties arrayProp, ArrayMarkerProperties arrayMarkerProp, SourceCodeProperties sourceCodeProp) {
        IntArray ia = generateArray(a, arrayProp);
        SourceCode sc = generateSourceCode(sourceCodeProp);
        generateHeaderBox();
        Text header = generateHeader();
        ArrayMarker arrayMarker = generateArrayMarker(ia, arrayMarkerProp);
        arrayMarker.hide();

        generateLegend();

        Text[] introduction = generateIntroductionText();

        lang.nextStep("Introduction");

        for (Text text : introduction) {
            text.hide();
        }

        sc.highlight(0, 0, false);

        lang.nextStep();

        sc.toggleHighlight(0, 0, false, 1, 0);
        SmoothSortLeonardoHeap leoHeap = generateLeonardoHeap(a);
        Coordinates heapCoords = new Coordinates(525, 150);

        drawLeoHeap(leoHeap, heapCoords, false);

        lang.nextStep("Generate Leonardo SmoothSortHeap");

        sc.toggleHighlight(1, 0, false, 3, 0);
        arrayMarker.show();
        ia.highlightCell(ia.getLength() - 1, null, null);

        Variables vars = lang.newVariables();
        // declare the loop variable
        vars.declare("int", "i");
        vars.setRole("i", "STEPPER");
        vars.set("i", Integer.toString(ia.getLength() - 1));

        // declare the length of the array
        vars.declare("int", "arraySize");
        vars.setRole("arraySize", "FIXED VALUE");
        vars.set("arraySize", Integer.toString(ia.getLength()));

        // declare the number of heaps in the leonardo heap
        vars.declare("int", "numberOfHeaps");
        vars.setRole("numberOfHeaps", "ORGANIZER");
        vars.set("numberOfHeaps", Integer.toString(leoHeap.getHeapList().size()));

        // declare number of switches in the array
        vars.declare("int", "numberOfSwitchedElements");
        vars.setRole("numberOfSwitchedElements", "GATHERER");

        lang.nextStep("Start sorting");

        for (int i = ia.getLength() - 1; i >= 0; i--) {
            sc.toggleHighlight(3, 0, false, 4, 0);
            drawLeoHeap(leoHeap, heapCoords, true);
            lang.nextStep("Iteration: " + "i=" + Integer.toString(i));
            int value = leoHeap.dequeueHighestValue();
            for (int j = 0; j < i; j++) {
                if (ia.getData(j) == value && ia.getData(j) > ia.getData(i)) {
                    ia.swap(j, i, null, null);
                    int swaps = Integer.parseInt(vars.get("numberOfSwitchedElements"));
                    vars.set("numberOfSwitchedElements", Integer.toString(swaps + 1));
                    break;
                }
            }
            lang.hideAllPrimitives();
            generateHeaderBox();
            header.show();
            sc.show();
            ia.show();
            generateLegend();
            arrayMarker.show();
            drawLeoHeap(leoHeap, heapCoords, false);
            ia.setHighlightFillColor(i, (Color) arrayProp.get("cellHighlight"), null, null);
            ia.highlightCell(i, null, null);
            vars.set("numberOfHeaps", Integer.toString(leoHeap.getHeapList().size()));
            lang.nextStep();
            sc.toggleHighlight(4, 0, false, 3, 0);
            if(i-1 >=0) {
                arrayMarker.move(i - 1, null, null);
                ia.highlightCell(i - 1, null, null);
            }
            vars.set("i", Integer.toString((i - 1) < 0 ? 0 : i - 1));
            lang.nextStep();
        }
        sc.toggleHighlight(3, 0, false, 7, 0);

        arrayMarker.hide();

        lang.nextStep("Sorted Array");

        // generate the conclusion
        TextProperties textProps = new TextProperties("conclusion");
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY,new Font("SansSerif", Font.PLAIN, 14) );

        String line1String = "Das unsortierte Array mit " + vars.get("arraySize") + " Elementen wurde mit " + vars.get("numberOfSwitchedElements") + " Vertauschungen sortiert.";
        lang.newText(new Coordinates(350, 100), line1String, "conclusion", null, textProps);

        lang.nextStep("Conclusion");
    }

    private void generateLegend() {
        RectProperties greenRectProps = new RectProperties();
        greenRectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, arrayProp.get("cellHighlight"));
        greenRectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);

        RectProperties yellowRectProps = new RectProperties();
        yellowRectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, selectColor);
        yellowRectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);

        lang.newRect(new Coordinates(255, 40), new Coordinates(265, 50), "greenBox", null, greenRectProps);
        lang.newRect(new Coordinates(255, 55), new Coordinates(265, 65), "yellowBox", null, yellowRectProps);

        Font font = new Font("SansSerif", Font.PLAIN, 11);
        TextProperties textProperties = new TextProperties("sorted");
        textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, font);
        TextProperties textProperties1 = new TextProperties("selected");
        textProperties1.set(AnimationPropertiesKeys.FONT_PROPERTY, font);

        lang.newText(new Coordinates(270, 40), "sorted", "legend", null, textProperties);
        lang.newText(new Coordinates(270, 55), "selected", "legend", null, textProperties1);
    }

    private IntArray generateArray(int[] a, ArrayProperties arrayProp) {
        ArrayProperties arrayProps = new ArrayProperties();
        arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, arrayProp.get("color"));
        arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, arrayProp.get("fillColor"));
        arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
        arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, arrayProp.get("elementColor"));
        arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
        arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, selectColor);
        arrayProps.set(AnimationPropertiesKeys.FONT_PROPERTY, arrayProp.get("font"));

        IntArray ia = lang.newIntArray(new Coordinates(350, 45), a, "intArray", null, arrayProps);

        ia.showIndices(false, null, null);
        return ia;
    }

    private Text generateHeader() {
        TextProperties textProps = new TextProperties("Smooth Sort");
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 24));
        textProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
        return lang.newText(new Coordinates(15, 25), "Smooth Sort", "Header", null, textProps);
    }

    private void generateHeaderBox() {
        RectProperties rectProperties = new RectProperties();
        rectProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.PINK);
        rectProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        rectProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
        lang.newRect(new Coordinates(10, 20), new Coordinates(180, 60), "box", null, rectProperties);
    }

    private SourceCode generateSourceCode(SourceCodeProperties sourceCodeProp) {
        SourceCodeProperties scProps = new SourceCodeProperties();
        scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, sourceCodeProp.get("contextColor"));
        int bold = (boolean) sourceCodeProp.get("bold") ? Font.BOLD : Font.PLAIN;
        int italic = (boolean) sourceCodeProp.get("italic") ? Font.ITALIC : Font.PLAIN;
        int style = bold + italic;
        scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(((Font) sourceCodeProp.get("font")).getName(), style, (int) sourceCodeProp.get("size")));

        scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, sourceCodeProp.get("highlightColor"));
        scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, sourceCodeProp.get("color"));

        // now, create the source code entity
        SourceCode sc = lang.newSourceCode(new Coordinates(15, 75), "sourceCode", null, scProps);

        sc.addCodeLine("public void smoothSort(int[] array) {", null, 0, null);
        sc.addCodeLine("generateLeonardoHeap(array);", null, 1, null);
        sc.addCodeLine("", null, 1, null);
        sc.addCodeLine("for (int i = array.length-1; i>=0; i--) {", null, 1, null);
        sc.addCodeLine("array[i] = list.getLast().root", null, 2, null);
        sc.addCodeLine("}", null, 1, null);
        sc.addCodeLine("", null, 0, null);
        sc.addCodeLine("}", null, 0, null);

        return sc;
    }

    private ArrayMarker generateArrayMarker(IntArray array, ArrayMarkerProperties arrayMarkerProp) {
        ArrayMarkerProperties arrayMarkerProperties = new ArrayMarkerProperties();
        //arrayMarkerProperties.set(AnimationPropertiesKeys.LABEL_PROPERTY, "");
        arrayMarkerProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, arrayMarkerProp.get("color"));
        return lang.newArrayMarker(array, array.getLength() - 1, "", null, arrayMarkerProperties);
    }

    private SmoothSortLeonardoHeap generateLeonardoHeap(int[] a) {
        SmoothSortLeonardoHeap leoHeap = new SmoothSortLeonardoHeap();

        for (int value : a) {
            SmoothSortHeap heap = new SmoothSortHeap(value);
            leoHeap.addHeap(heap);
        }
        return leoHeap;
    }

    private void drawNode(SmoothSortNode node, Coordinates coordinates, boolean highlightRoot) {
        CircleProperties circleProps = new CircleProperties();
        if (highlightRoot) {
            circleProps.set(AnimationPropertiesKeys.FILL_PROPERTY, selectColor);
        } else {
            circleProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GRAY);
        }
        circleProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        circleProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

        int value = node.getValue();

        lang.newCircle(coordinates, NODE_RADIUS, Integer.toString(value), null, circleProps);

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

    private void drawHeap(SmoothSortHeap heap, Coordinates rootCoordinates) {
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

    private void drawHeapRecursive(SmoothSortNode root, Coordinates coordinates, float numberOfGaps, float numberOfNodes) {
        if (root == null) {
            return;
        }

        // draw the root
        drawNode(root, coordinates, false);

        if (root.getLeftChild() == null || root.getRightChild() == null) {
            return;
        }

        // draw left node
        int LEVEL_GAP = 100;
        Coordinates leftChildCoords = new Coordinates(coordinates.getX() - (int) (numberOfGaps * NODE_GAP) - (int) (numberOfNodes * (NODE_RADIUS * 2)), coordinates.getY() + LEVEL_GAP);
        drawHeapRecursive(root.getLeftChild(), leftChildCoords, numberOfGaps / 2, numberOfNodes / 2);

        // draw right node
        Coordinates rightChildCoords = new Coordinates(coordinates.getX() + (int) (numberOfGaps * NODE_GAP) + (int) (numberOfNodes * (NODE_RADIUS * 2)), coordinates.getY() + LEVEL_GAP);
        drawHeapRecursive(root.getRightChild(), rightChildCoords, numberOfGaps / 2, numberOfNodes / 2);

        // draw the line to the left and the right node
        drawConnectionLine(coordinates, leftChildCoords);
        drawConnectionLine(coordinates, rightChildCoords);
    }

    private void drawLeoHeap(SmoothSortLeonardoHeap leoHeap, Coordinates coordinates, boolean highlightHighestValue) {
        if (leoHeap.getHeapList().size() == 0) {
            return;
        }

        Coordinates rootCoordinates = coordinates;
        List<SmoothSortHeap> heapList = leoHeap.getHeapList();
        for (int i = 0; i < heapList.size() - 1; i++) {
            drawHeap(heapList.get(i), rootCoordinates);
            int offset = (getHeapWidth(heapList.get(i)) + getHeapWidth(heapList.get(i + 1))) / 2 + 50;
            rootCoordinates = new Coordinates(rootCoordinates.getX() + offset, rootCoordinates.getY());
        }

        // draw the last heap
        drawHeap(heapList.get(heapList.size() - 1), rootCoordinates);

        // highlight the root if needed
        if (highlightHighestValue) {
            drawNode(heapList.get(heapList.size() - 1).getRoot(), rootCoordinates, true);
        }
    }

    private void drawConnectionLine(Coordinates coord1, Coordinates coord2) {
        PolylineProperties polyProps = new PolylineProperties();
        polyProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
        lang.newPolyline(new Coordinates[]{coord1, coord2}, "treeLine", null, polyProps);
    }

    private int getHeapWidth(SmoothSortHeap heap) {
        int order = heap.getOrder();
        int numberOfNodes = (int) Math.pow(2, order);
        return (2 * NODE_RADIUS) * numberOfNodes + (NODE_GAP * (numberOfNodes - 1));
    }
}
