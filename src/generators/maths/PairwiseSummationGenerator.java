/*
 * PairwiseSummation.java
 * Nossair Ouladali, Zarina Catar, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.maths;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.*;
import algoanim.primitives.generators.Language;
import algoanim.properties.*;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;
import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * @author Nossair Ouladali, Zarina Catar
 */
public class PairwiseSummationGenerator implements ValidatingGenerator {

    private Language lang;
    private int yCoord = -90, xCoord = 20, counterDivide = 1, counterAssignments = 0, counterComparisons = 0;
    private double globalSum = 0.0;
    private Variables globalSumVariable, sVariable, mVariable;
    private Text globalSumText, mText, localSumText, assignmentsText, comparisonsText;
    private Text[] introDescription = new Text[this.getIntroDescription().length], outroDescription = new Text[this.getOutroDescription().length];
    private ArrayProperties inputProperties;
    private TextProperties globalSumProps, descriptionProps, headerProps, textProps, calculationProps;
    private ArrayMarkerProperties arrowP;
    private Timing defaultTiming = new TicksTiming(30);
    private final String globalSumTextPrefix = "Global sum: ", localSumTextPrefix = "Local sum: ", assignmentsTextPrefix = "Assignments: ", comparisonsTextPrefix = "Comparisons: ";
    private SourceCode sourceCode;
    private SourceCodeProperties sourceCodeProperties;
    private ArrayList<DoubleArray> createdArrays = new ArrayList<DoubleArray>();
    private ArrayList<Text> createdText = new ArrayList<>();

    public PairwiseSummationGenerator() {
        lang = new AnimalScript("Pairwise Summation", "Nossair Ouladali, Zarina Catar", 800, 600);
        lang.setStepMode(true);
    }

    public void init() {
        lang = new AnimalScript("Pairwise Summation", "Nossair Ouladali, Zarina Catar", 800, 600);
        lang.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {

        this.sourceCodeProperties = (SourceCodeProperties) props.getPropertiesByName("sourceCodeProperties");
        this.inputProperties = (ArrayProperties) props.getPropertiesByName("arrayProperties");

        double third = (double) primitives.get("third");
        double fifth = (double) primitives.get("fifth");
        double fourth = (double) primitives.get("fourth");
        double first = (double) primitives.get("first");
        double second = (double) primitives.get("second");

        double[] input = {first, second, third, fourth, fifth};
        start(input);

        return lang.toString();
    }

    private void start(double[] input) {

        // sets properties of pointer that points on values of the double array
        arrowP = new ArrayMarkerProperties();
        arrowP.set(AnimationPropertiesKeys.SHORT_MARKER_PROPERTY, true);
        arrowP.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");

        // sets a global text that shows the current sum value
        globalSumProps = new TextProperties();
        globalSumProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 18));
        globalSumProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

        // sets properties of description
        descriptionProps = new TextProperties();
        descriptionProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 13));

        // sets properties of description
        calculationProps = new TextProperties();
        calculationProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 15));
        calculationProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

        // sets properties of the header
        this.headerProps = new TextProperties();
        headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 16));
        headerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);

        this.intro();
        lang.nextStep("Introduction");

        this.closeIntro();
        this.generateSourceCode();

        this.globalSumVariable = lang.newVariables();
        globalSumVariable.declare("double", "globalSum", "0.0");

        this.sVariable = lang.newVariables();
        sVariable.declare("double", "s", "0.0");

        this.mVariable = lang.newVariables();
        mVariable.declare("int", "m", "0");

        globalSumText = lang.newText(new Coordinates(550, 80), globalSumTextPrefix + "0.0",
                "globalSum", null, globalSumProps);

        this.assignmentsText = lang.newText(new Offset(-160, 0, "globalSum", "NW"), assignmentsTextPrefix + "0",
                "assignment", null, calculationProps);

        this.comparisonsText = lang.newText(new Offset(0, 20, "assignment", "NW"), comparisonsTextPrefix + "0",
                "comparison", null, calculationProps);

        createdText.add(globalSumText);

        this.sort(input);

        // Algorithm ends here
        globalSumText.setText("Result: " + this.globalSum, null, this.defaultTiming);

        // Shows further description, e.g. the complexity of pairwise summation in comparinson with kahan
        lang.nextStep("Result");
        this.outro();
        this.lang.nextStep("Outroduction");
    }

    /**
     * The summation algorithm.
     *
     * @param input list of doubles
     * @return sum of given double values
     */
    private double sort(double[] input) {

        // it's needed to create arrays one beneath the other
        yCoord += 90;
        sVariable.set("s", "0.0");
        mVariable.set("m", "0");
        final DoubleArray doubleArray = this.lang.newDoubleArray(new Coordinates(xCoord, yCoord), input, "array " + this.yCoord, null, this.inputProperties);


        // it's needed to delete all created arrays
        createdArrays.add(doubleArray);

        if (yCoord == 90) {
            lang.nextStep("Start with sorting");
        } else {
            lang.nextStep();
        }

        sourceCode.highlight(1);

        this.counterComparisons++;
        this.comparisonsText.setText(this.comparisonsTextPrefix + this.counterComparisons, null, null);

        if (doubleArray.getLength() < 3) {
            double s = input[0];

            lang.nextStep();
            this.localSumText = lang.newText(new Offset(10, 0, "array " + yCoord, "NE"), localSumTextPrefix + s,
                    "local sum", null, globalSumProps);
            this.sVariable.set("s", s + "");

            // necessary to delete all created local text
            createdText.add(localSumText);

            sourceCode.unhighlight(1);
            sourceCode.highlight(3);

            this.counterAssignments++;
            this.assignmentsText.setText(this.assignmentsTextPrefix + this.counterAssignments, null, null);

            lang.nextStep();
            sourceCode.unhighlight(3);

            // move pointer
            ArrayMarker arrow = lang.newArrayMarker(doubleArray, doubleArray.getLength() - 1, "i", null, arrowP);
            arrow.move(1, null, defaultTiming);
            sourceCode.highlight(4);
            this.counterComparisons++;
            this.comparisonsText.setText(this.comparisonsTextPrefix + this.counterComparisons, null, null);

            this.counterAssignments++;
            this.assignmentsText.setText(this.assignmentsTextPrefix + this.counterAssignments, null, null);

            lang.nextStep();
            while (arrow.getPosition() < doubleArray.getLength()) {
                sourceCode.unhighlight(4);
                sourceCode.highlight(5);
                doubleArray.highlightCell(arrow.getPosition(), null, defaultTiming);

                lang.nextStep();

                // shows the sum of the given arrays and shows its result next to the array
                this.localSumText.setText(localSumTextPrefix + s + " + " + doubleArray.getData(arrow.getPosition()), null, defaultTiming);
                s += doubleArray.getData(arrow.getPosition());
                s = Math.round(s * 1000) / 1000.000;

                this.counterAssignments++;
                this.assignmentsText.setText(this.assignmentsTextPrefix + this.counterAssignments, null, null);

                lang.nextStep();
                this.localSumText.setText(localSumTextPrefix + s, null, defaultTiming);
                this.sVariable.set("s", s + "");

                doubleArray.unhighlightCell(arrow.getPosition(), null, defaultTiming);
                sourceCode.unhighlight(5);

                arrow.increment(null, defaultTiming);
                sourceCode.highlight(4);

                this.counterComparisons++;
                this.comparisonsText.setText(this.comparisonsTextPrefix + this.counterComparisons, null, null);

                this.counterAssignments++;
                this.assignmentsText.setText(this.assignmentsTextPrefix + this.counterAssignments, null, null);

                lang.nextStep();
            }

            sourceCode.unhighlight(4);
            sourceCode.unhighlight(1);
            arrow.hide();

            sourceCode.highlight(7);
            // shows the current sum value
            this.globalSumText.setText(globalSumTextPrefix + this.globalSum + " + " + s, null, defaultTiming);

            lang.nextStep();
            this.globalSum += s;
            this.globalSum = Math.round(this.globalSum * 1000) / 1000.000;
            this.globalSumText.setText(globalSumTextPrefix + this.globalSum, null, defaultTiming);
            this.globalSumVariable.set("globalSum", this.globalSum + "");

            lang.nextStep();
            sourceCode.unhighlight(7);

            return s;
        } else {
            lang.nextStep();
            sourceCode.unhighlight(1);
            sourceCode.highlight(8);

            int m = (int) Math.floor(input.length / 2);
            lang.nextStep();
            sourceCode.unhighlight(8);
            sourceCode.highlight(10);

            mText = lang.newText(new Offset(10, 0, "array " + yCoord, "NE"), "m: " + m,
                    "sum", null, globalSumProps);
            mVariable.set("m", m + "");

            this.counterAssignments++;
            this.assignmentsText.setText(this.assignmentsTextPrefix + this.counterAssignments, null, null);

            createdText.add(mText);
            lang.nextStep();
            sourceCode.unhighlight(10);

            for (int i = 0; i < m; i++) {
                doubleArray.highlightCell(i, null, defaultTiming);
            }

            sourceCode.highlight(11);

            lang.nextStep(this.counterDivide + ". Recursion");
            this.counterDivide++;
            sourceCode.unhighlight(11);

            final double a = sort(Arrays.copyOfRange(input, 0, m));
            for (int i = 0; i < m; i++) {
                doubleArray.unhighlightCell(i, null, defaultTiming);
            }

            lang.nextStep();
            for (int i = m; i < doubleArray.getLength(); i++) {
                doubleArray.highlightCell(i, null, defaultTiming);
            }

            sourceCode.highlight(12);
            lang.nextStep(this.counterDivide + ". Recursion");

            sourceCode.unhighlight(12);
            this.counterDivide++;
            lang.nextStep();

            final double b = sort(Arrays.copyOfRange(input, m, input.length));
            for (int i = m; i < doubleArray.getLength(); i++) {
                doubleArray.unhighlightCell(i, null, defaultTiming);
            }

            return a + b;
        }
    }

    // creates the first layout. it describes the algorithm in a few sentences.
    private void intro() {
        // creates the header "Pairwise Summation"
        Text headerText = lang.newText(new Coordinates(20, 30), "Pairwise Summation", "header", null, headerProps);
        final int cordX = 20;
        int cordY = 70;
        for (int i = 0; i < this.introDescription.length; i++) {
            introDescription[i] = lang.newText(new Coordinates(cordX, cordY), this.getIntroDescription()[i], "description", null, descriptionProps);
            cordY += 20;
        }
    }

    // deletes the sourcecode to show the outro description
    private void outro() {
        this.vanishTextFields();
        this.showOutroDescription();
        this.resetCounters();

    }

    private void resetCounters() {
        this.yCoord = 0;
        this.globalSum = 0;
        this.globalSumVariable.set("globalSum", "0.0");
        this.sVariable.set("s", "0.0");
        this.mVariable.set("m", "0");
        this.counterDivide = 1;
        this.counterAssignments = 0;
        this.counterComparisons = 0;
    }

    // deletes the intro description
    private void closeIntro() {
        Arrays.stream(introDescription).forEach(Primitive::hide);
    }

    private void vanishTextFields() {
        createdArrays.forEach(Primitive::hide
        );
        createdText.forEach(Primitive::hide);
        sourceCode.hide();
        this.comparisonsText.hide();
        this.assignmentsText.hide();

    }

    private void showOutroDescription() {

        int cordY = 70;
        for (int i = 0; i < this.outroDescription.length; i++) {
            outroDescription[i] = lang.newText(new Coordinates(this.xCoord, cordY), this.getOutroDescription()[i], "description", null, descriptionProps);
            cordY += 20;
        }
    }

    private void generateSourceCode() {
        sourceCode = lang.newSourceCode(new Coordinates(550, 100), "sourceCode", null, this.sourceCodeProperties);

        Scanner scanner = new Scanner(this.getCodeExample());
        while (scanner.hasNextLine()) {
            sourceCode.addCodeLine(scanner.nextLine(), null, 0, null);
        }
        scanner.close();
    }

    private String[] getIntroDescription() {
        return new String[]{
                "In numerical analysis, pairwise summation, also called cascade summation, is a technique to sum a sequence of finite-precision floating-point numbers that substantially reduces the accumulated ",
                "round-off error compared to naively accumulating the sum in sequence.",
                "In particular, pairwise summation of a sequence of n numbers x_n works by recursively breaking the sequence into two halves, summing each half, and adding the two sums: a divide and conquer algorithm.",
                "(source: wikipedia)"};
    }

    private String[] getOutroDescription() {
        return new String[]{
                "In comparison, the naive technique of accumulating the sum in sequence (adding each xi one at a time for i = 1, ..., n) has roundoff errors that grow at worst as O(εn). ",
                "Kahan summation has a worst-case error of roughly O(ε), independent of n, but requires several times more arithmetic operations. If the roundoff errors are random, and in particular have random signs,",
                "then they form a random walk and the error growth is reduced to an average of O(ε sqrt(log n)) for pairwise summation.",
                "(source: wikipedia)"
        };
    }

    public String getName() {
        return "Pairwise Summation";
    }

    public String getAlgorithmName() {
        return "Pairwise Summation";
    }

    public String getAnimationAuthor() {
        return "Nossair Ouladali, Zarina Catar";
    }

    public String getCodeExample() {
        return "01 private double pairwiseSummation(double[] input) {\n" +
                "02    if (input.length < 3) {\n" +
                "03\n" +
                "04        double s = input[0];\n" +
                "05        for (int i = 1; i < input.length; i++) {\n" +
                "06            s += input[i];\n" +
                "07        }\n" +
                "08        return s;\n" +
                "09    } else {\n" +
                "10\n" +
                "11        int m = (int) Math.floor(input.length / 2);\n" +
                "12        return pairwiseSummation(Arrays.copyOfRange(input, 0, m)) +\n" +
                "13                pairwiseSummation(Arrays.copyOfRange(input, m, input.length));\n" +
                "14    }\n" +
                "15 }\n";
    }

    public String getFileExtension() {
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.ENGLISH;
    }

    public String getDescription() {
        return "In numerical analysis, pairwise summation, also called cascade summation, is a technique to sum a sequence of finite-precision floating-point numbers that substantially reduces the accumulated round-off error compared to naively accumulating the sum in sequence.\n" +
                "In particular, pairwise summation of a sequence of n numbers xn works by recursively breaking the sequence into two halves, summing each half, and adding the two sums: a divide and conquer algorithm.\n" +
                "(source: wikipedia)\n";
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_DATA_STRUCTURE);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }

    public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives)
            throws IllegalArgumentException {

        double first = (double) primitives.get("first");
        double second = (double) primitives.get("second");
        double third = (double) primitives.get("third");
        double fifth = (double) primitives.get("fifth");
        double fourth = (double) primitives.get("fourth");

        double[] input = {first, second, third, fourth, fifth};

        for (double v : input) {

            String s = v + "";

            List<String> doublesAsString = Arrays.asList(s.trim().split("\\."));
            if (doublesAsString.get(1).length() > 3)
                throw new IllegalArgumentException("Please work with equal less than 3 decimal places.");
        }

        return true;
    }

    public static void main(String[] args) {
        PairwiseSummationGenerator pw = new PairwiseSummationGenerator();
        Animal.startGeneratorWindow(pw);
    }
}
