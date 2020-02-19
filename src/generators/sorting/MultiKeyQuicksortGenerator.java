/*
 * MultiKeyQuicksortGenerator.java
 * Nossair Ouladali, Zarina Catar, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.sorting;

import algoanim.primitives.*;
import algoanim.primitives.generators.AnimationType;
import algoanim.properties.*;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;
import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.*;
import java.util.Arrays;
import java.util.Locale;

import algoanim.primitives.generators.Language;

import java.util.Hashtable;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;

public class MultiKeyQuicksortGenerator implements Generator {
    private Language lang;
    private ArrayProperties ap;
    private MatrixProperties mp;
    private ArrayMarkerProperties iP, leftP, rightP;
    private StringMatrix stringMatrix;
    private TextProperties infoProps, headerProps, descriptionProps;
    private Text headerText, swapText, comparisonText;
    private Text[] introDescription = new Text[this.getIntroDescription().length];
    private Timing defaultTiming = new TicksTiming(30);
    private SourceCode sourceCode;
    private int swapCounter = 0;
    private final String swapPrefix = "Swaps: ";
    private StringArray stringArray;

    public MultiKeyQuicksortGenerator() {
        lang = new AnimalScript("Multi-Key Quicksort", "Nossair Ouladali, Zarina Catar", 800, 600);
        lang.setStepMode(true);
    }

    public void init() {
        lang = new AnimalScript("Multi-Key Quicksort", "Nossair Ouladali, Zarina Catar", 800, 600);
        lang.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
        String[] input = (String[]) primitives.get("input");
        ArrayProperties arrayProperties = (ArrayProperties) props.getPropertiesByName("arrayProperties");

        this.start(input);

        return lang.toString();
    }

    private void start(String[] input) {

        // Einstellen der Füllfarbe des DoubleArrays
        this.ap = new ArrayProperties();
        ap.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        ap.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
        ap.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.ORANGE);

        this.mp = new MatrixProperties();
        mp.set(AnimationPropertiesKeys.GRID_HIGHLIGHT_BORDER_COLOR_PROPERTY, Color.BLACK);
        mp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 17));
        mp.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");

        //Properties für die beiden Zeiger top, current erstellen
        iP = new ArrayMarkerProperties();
        iP.set(AnimationPropertiesKeys.LONG_MARKER_PROPERTY, true);
        iP.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");

        //Properties für die beiden Zeiger top, current erstellen
        leftP = new ArrayMarkerProperties();
        leftP.set(AnimationPropertiesKeys.SHORT_MARKER_PROPERTY, true);
        leftP.set(AnimationPropertiesKeys.LABEL_PROPERTY, "left");

        //Properties für die beiden Zeiger top, current erstellen
        rightP = new ArrayMarkerProperties();
        rightP.set(AnimationPropertiesKeys.SHORT_MARKER_PROPERTY, true);
        rightP.set(AnimationPropertiesKeys.LABEL_PROPERTY, "right");

        infoProps = new TextProperties();
        infoProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 18));
        infoProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

        // properties for the header
        this.headerProps = new TextProperties();
        headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 16));
        headerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);

        // properties for the source code
        descriptionProps = new TextProperties();
        descriptionProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 13));

        this.intro();
        lang.nextStep();

        this.closeIntro();
        stringArray = this.lang.newStringArray(new Coordinates(20, 140), input, "array", null, ap);
        this.generateSourceCode();
        this.generateOverviewMatrix();
        this.swapText = lang.newText(new Offset(30, 0, "overview", "NE"), "Swaps: " + this.swapCounter, "swap counter", null, infoProps);
        this.sort(stringArray, 0, input.length - 1, 0);

        this.outro();
    }

    private void sort(StringArray input, int l, int r, int d) {

        lang.nextStep();
        sourceCode.highlight(2);
        sourceCode.unhighlight(18);
        sourceCode.unhighlight(19);
        sourceCode.unhighlight(20);

        lang.nextStep();
        sourceCode.unhighlight(2);

        if (r <= l) {
            sourceCode.highlight(3);

            lang.nextStep();
            sourceCode.unhighlight(3);
            return;
        }

        sourceCode.highlight(5);
        sourceCode.unhighlight(2);
        char pivot = input.getData(ThreadLocalRandom.current().nextInt(l, r)).charAt(d);
        this.actualizeOverview(pivot, d, null, null, null);

        lang.nextStep();
        sourceCode.unhighlight(5);
        ArrayMarker left = lang.newArrayMarker(input, r, "left", null, leftP);
        left.move(l, null, defaultTiming);

        this.actualizeOverview(pivot, d, null, l, null);
        sourceCode.highlight(6);

        lang.nextStep();
        sourceCode.unhighlight(6);
        sourceCode.highlight(7);

        ArrayMarker right = lang.newArrayMarker(input, r, "right", null, rightP);
        right.move(r, null, defaultTiming);
        this.actualizeOverview(pivot, d, null, l, r);

        lang.nextStep();
        sourceCode.unhighlight(7);
        sourceCode.highlight(8);

        ArrayMarker i = lang.newArrayMarker(input, r, "i", null, iP);
        i.move(l, null, defaultTiming);
        this.actualizeOverview(pivot, d, l, l, r);

        lang.nextStep();
        sourceCode.unhighlight(8);
        sourceCode.highlight(10);

        while (i.getPosition() <= right.getPosition()) {
            lang.nextStep();

            sourceCode.unhighlight(10);
            sourceCode.highlight(11);
            sourceCode.unhighlight(16);

            if (input.getData(i.getPosition()).charAt(d) < pivot) {
                lang.nextStep();

                input.highlightCell(left.getPosition(), null, defaultTiming);
                input.highlightCell(i.getPosition(), null, defaultTiming);
                sourceCode.highlight(12);
                sourceCode.unhighlight(11);
                lang.nextStep();

                input.swap(left.getPosition(), i.getPosition(), null, defaultTiming);
                this.swapCounter++;
                this.swapText.setText(this.swapPrefix + this.swapCounter, null, null);
                lang.nextStep();

                sourceCode.unhighlight(12);
                sourceCode.highlight(10);
                input.unhighlightCell(left.getPosition(), null, defaultTiming);
                input.unhighlightCell(i.getPosition(), null, defaultTiming);
                left.increment(null, defaultTiming);
                i.increment(null, defaultTiming);
                this.actualizeOverview(pivot, d, i.getPosition(), left.getPosition(), right.getPosition());

            } else if (input.getData(i.getPosition()).charAt(d) > pivot) {
                lang.nextStep();
                sourceCode.unhighlight(11);
                sourceCode.highlight(13);


                lang.nextStep();
                sourceCode.unhighlight(13);
                sourceCode.highlight(14);
                input.highlightCell(right.getPosition(), null, defaultTiming);
                input.highlightCell(i.getPosition(), null, defaultTiming);

                lang.nextStep();
                input.swap(i.getPosition(), right.getPosition(), null, defaultTiming);
                this.swapCounter++;
                this.swapText.setText(this.swapPrefix + this.swapCounter, null, null);

                lang.nextStep();
                input.unhighlightCell(right.getPosition(), null, defaultTiming);
                input.unhighlightCell(i.getPosition(), null, defaultTiming);
                sourceCode.unhighlight(14);
                right.decrement(null, defaultTiming);
                this.actualizeOverview(pivot, d, i.getPosition(), left.getPosition(), right.getPosition());
            } else {
                lang.nextStep();
                sourceCode.unhighlight(11);
                sourceCode.highlight(16);

                lang.nextStep();
                i.increment(null, defaultTiming);
                this.actualizeOverview(pivot, d, i.getPosition(), left.getPosition(), right.getPosition());
            }
        }

        lang.nextStep();
        sourceCode.unhighlight(16);
        sourceCode.unhighlight(10);
        sourceCode.highlight(18);
        left.hide();
        right.hide();
        i.hide();

        sort(input, l, left.getPosition(), d);

        lang.nextStep();
        sourceCode.unhighlight(18);
        sourceCode.highlight(19);

        sort(input, left.getPosition(), right.getPosition(), d + 1);

        lang.nextStep();
        sourceCode.unhighlight(19);
        sourceCode.highlight(20);

        sort(input, right.getPosition() + 1, r, d);

        lang.nextStep();
        sourceCode.unhighlight(20);
        left.hide();
        right.hide();
        i.hide();
    }

    private void generateSourceCode() {
        // properties for the source code
        SourceCodeProperties scProperties = new SourceCodeProperties();
        scProperties.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
        scProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 16));
        scProperties.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
        scProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        sourceCode = lang.newSourceCode(new Offset(0, 30, "array", "SW"), "sourceCode", null, scProperties);

        Scanner scanner = new Scanner(this.getCodeExample());
        while (scanner.hasNextLine()) {
            sourceCode.addCodeLine(scanner.nextLine(), null, 0, null);
        }
        scanner.close();
    }

    private void actualizeOverview(char pivot, Integer d, Integer i, Integer left, Integer right) {
        this.stringMatrix.put(1, 0, pivot + "", null, null);

        if (d != null)
            this.stringMatrix.put(1, 1, d + "", null, null);

        if (i != null)
            this.stringMatrix.put(1, 2, i + "", null, null);

        if (left != null)
            this.stringMatrix.put(1, 3, left + "", null, null);

        if (right != null)
            this.stringMatrix.put(1, 4, right + "", null, null);
    }

    private void generateOverviewMatrix() {
        final String[][] contentOverview = {{"Pivot", "d", "i", "left", "right"}, {"", "", "", "", ""}};
        stringMatrix = this.lang.newStringMatrix(new Offset(60, -40, "array", "NE"), contentOverview, "overview", null, mp);

        for (int i = 0; i < contentOverview.length; i++) {
            for (int k = 0; k < contentOverview[0].length; k++) {
                stringMatrix.setGridBorderColor(i, k, Color.BLACK, null, null);
            }
        }
    }

    private void closeIntro() {
        Arrays.stream(introDescription).forEach(Primitive::hide);
    }

    private void intro() {
        this.generateDescription(this.getIntroDescription(), this.introDescription);
    }

    private void generateDescription(final String[] description, Text[] text) {
        headerText = lang.newText(new Coordinates(20, 30), "Multi-Key Quicksort", "header", null, headerProps);
        final int cordX = 20;
        int cordY = 70;
        for (int i = 0; i < text.length; i++) {
            text[i] = lang.newText(new Coordinates(cordX, cordY), description[i], "description", null, descriptionProps);
            cordY += 20;
        }
    }


    private void outro() {
        this.sourceCode.hide();
        this.stringArray.hide();
        this.stringMatrix.hide();
        this.swapText.hide();
        this.swapCounter = 0;

        Text[] outroDescription = new Text[this.getOutroDescription().length];
        this.generateDescription(this.getOutroDescription(), outroDescription);
    }

    public String getName() {
        return "Multi-Key Quicksort";
    }

    public String getAlgorithmName() {
        return "Multi-Key Quicksort";
    }

    public String getAnimationAuthor() {
        return "Nossair Ouladali, Zarina Catar";
    }

    public String getDescription() {
        return "In numerical analysis, pairwise summation, also called cascade summation, is a technique to sum a sequence of finite-precision floating-point numbers that substantially reduces the accumulated  round-off error compared to naively accumulating the sum in sequence."
                + "\n"
                + "In particular, pairwise summation of a sequence of n numbers x_n works by recursively breaking the sequence into two halves, summing each half, and adding the two sums: a divide and conquer algorithm."
                + "\n"
                + "(source: wikipedia)";
    }

    private String[] getIntroDescription() {
        return new String[]{
                "Multi-Key Quicksort, also known as three-way radix quicksort, is an algorithm for sorting strings. The algorithm chooses like Quicksort a pivot p and divides the given list into three parts.",
                "The first part contains strings smaller than p, the second one equal to p and the last part has only elements greater than p.",
                "The algorithm gets also an integer d that decides which part of the string will be compared to the pivot p."};
    }

    private String[] getOutroDescription() {
        return new String[]{
                "The multi-key quicksort based on the quicksort algorithm. Quicksort randomly picks a pivot element and divides the list into two parts.",
                "The first part contains elements that are greater or equal than the pivot and the other part contains elements smaller or equal.",
                "The main difference between these algorithms is that quicksort can only sort numerical lists while multiy-key quicksort sorts strings.",
                "Furthermore, the multi-key quicksort divides the given list into three parts (not like quicksort). The first/third part contains elements greater/smaller than the pivot element and the second part has only",
                "strings that are equal to the pivot.",
                "Practical implementations of multi-key quicksort can benefit from the same optimizations typically applied to standard quicksort: median-of-three pivoting, switching to insertion sort for small arrays, etc."};
    }

    public String getCodeExample() {
        return "01 private void sort(String[] input, int l, int r, int d) {\n" +
                "02\n" +
                "03    if (r <= l)\n" +
                "04        return;\n" +
                "05\n" +
                "06    char pivot = input[ThreadLocalRandom.current().nextInt(l, r)].charAt(d);\n" +
                "07    int left = l;\n" +
                "08    int right = r;\n" +
                "09    int i = l;\n" +
                "10\n" +
                "11    while (i <= right) {\n" +
                "12        if (input[i].charAt(d) < pivot) {\n" +
                "13            swap(input, left++, i++);\n" +
                "14        } else if (input[i].charAt(d) > pivot)\n" +
                "15            swap(input, i, right--);\n" +
                "16        else\n" +
                "17            i++;\n" +
                "18    }\n" +
                "19    sort(input, l, left, d);\n" +
                "20    sort(input, left, right, d + 1);\n" +
                "21    sort(input, right + 1, r, d);\n" +
                "22 }\n";
    }

    public String getFileExtension() {
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.ENGLISH;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }

    public static void main(String[] args) {
        MultiKeyQuicksortGenerator multiKeyQuicksortGenerator = new MultiKeyQuicksortGenerator();
        Animal.startGeneratorWindow(multiKeyQuicksortGenerator);
    }

}