/*
 * FindGSetGenerator.java
 * Valentin Kuhn, 2016 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Timing;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

public class FindGSetGenerator implements ValidatingGenerator {
    private static final String DESCRIPTION = "FindG-Set finds the most general hypotheses that are correct on the data.\n"
            + "FindG-Set starts with the most general hypothesis that covers all examples.\n"
            + "This hypothesis is iteratively refined to more specific ones where each more\n"
            + "specific hypothesis only differentiates in one condition from the previous more\n"
            + "general hypothesis. Unlike the Find-G algorithm, FindG-Set does not select one\n"
            + "of the more specific hypotheses in each refinement step but instead keeps all of\n"
            + "them that are correct on the data. This approach results in a bias towards general\n"
            + "hypotheses.\n"
            + "The counterpart of FindG-Set is FindS-Set, starting with the most specific hypothesis\n"
            + "and generalizing it in each step, resulting in a bias towards more specific hypotheses.";
    private static final String SOURCE_CODE = "I.   h = most general hypothesis in H (covering all examples)\n"
            + "II.  G = { h }\n"
            + "III. for each training example e\n"
            + "     a) if e is positive\n"
            + "           remove all h ? G that do not cover e\n"
            + "     b) if e is negative\n"
            + "           for all hypotheses h ? G that cover e\n"
            + "              G = G ? { h }\n"
            + "              for every condition c in e that is not part of h\n"
            + "                 for all conditions c' that negate c\n"
            + "                    h' = h ? { c' }\n"
            + "                    if h' covers all previous positive examples\n"
            + "                       G = G ? { h' }\n"
            + "IV.  return G";
    private static final String AUTHOR = "Valentin Kuhn";
    private static final String FILE_EXTENSION = "asu";
    private static final String ALGO_NAME = "FindG-Set";
    private static final String GENERATOR_NAME = "FindG-Set";
    private static final Timing TIMING = null; // Timing.INSTANTEOUS;
    private int stepDelay; // Timing.MEDIUM.getDelay();
    private Language lang;
    private int expCounter = 0;

    @Override
    public void init() {
        lang = new AnimalScript(ALGO_NAME, AUTHOR, 1000, 800);
        lang.setStepMode(true);
    }

    @Override
    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
        // Properties
        MatrixProperties matrixProps = (MatrixProperties) props.getPropertiesByName("matrixProps");
        SourceCodeProperties sourceCodeProps = (SourceCodeProperties) props.getPropertiesByName("pseudoCodeProps");
        SourceCodeProperties explanationProps = (SourceCodeProperties) props.getPropertiesByName("explanationProps");

        TextProperties titleTextProperties = new TextProperties();
        titleTextProperties.set("font", new Font(Font.SANS_SERIF, Font.PLAIN, 24));

        // Primitives
        String[][] trainingSet = (String[][]) primitives.get("trainingSet");
        int classIndex = (int) primitives.get("classIndex");
        String posClass = (String) primitives.get("posClass");
        String negClass = (String) primitives.get("negClass");
        stepDelay = (int) primitives.get("stepDelay");

        // Introduction
        Text introTitle = lang.newText(new Coordinates(20, 20), "FindG-Set", "introTitle", null, titleTextProperties);
        SourceCode intro = lang.newSourceCode(new Coordinates(20, 50), "explanation0", null, explanationProps);
        intro.addMultilineCode(DESCRIPTION, null, null);
        lang.nextStep(stepDelay, "Introduction");
        introTitle.hide(TIMING);
        intro.hide(TIMING);

        // Visuals

        Text scTitle = lang.newText(new Coordinates(20, 20), "FindG-Set (Pseudo-Code)", "sourceCodeTitle", null, titleTextProperties);
        // Show the pseudo source code to the user
        SourceCode sc = lang.newSourceCode(new Coordinates(20, 50), "sourceCode", null, sourceCodeProps);
        sc.addCodeLine("I.   h = most general hypothesis in H (covering all examples)", null, 0, null); // 00
        sc.addCodeLine("II.  G = { h }", null, 0, null);                                                // 01
        sc.addCodeLine("III. for each training example e", null, 0, null);                              // 02
        sc.addCodeLine("a) if e is positive", null, 1, null);                                           // 03
        sc.addCodeLine("remove all h ∈ G that do not cover e", null, 2, null);                         // 04
        sc.addCodeLine("b) if e is negative", null, 1, null);                                           // 05
        sc.addCodeLine("for all hypotheses h ∈ G that cover e", null, 2, null);                        // 06
        sc.addCodeLine("G = G ∖ { h }", null, 3, null);                                                 // 07
        sc.addCodeLine("for every condition c in e that is not part of h", null, 3, null);              // 08
        sc.addCodeLine("for all conditions c' that negate c", null, 4, null);                           // 09
        sc.addCodeLine("h' = h ∪ { c' }", null, 5, null);                                              // 10
        sc.addCodeLine("if h' covers all previous positive examples", null, 5, null);                   // 11
        sc.addCodeLine("G = G ∪ { h' }", null, 6, null);                                               // 12
        sc.addCodeLine("IV.  return G", null, 0, null);                                                 // 13

        Text smTitle = lang.newText(new Coordinates(20, 330), "Training set", "trainingSetTitle", null, titleTextProperties);
        // Show the training set to the user
        StringMatrix sm = lang.newStringMatrix(new Coordinates(20, 360), trainingSet, "trainingSet", null, matrixProps);

        lang.newText(new Coordinates(500, 20), "Explanation", "explanationTitle", null, titleTextProperties);
        // Show description / explanation to the user --> to be updated in each step
        //Text ex = lang.newText(new Coordinates(530, 50), DESCRIPTION.replaceAll("\n", " "), "explanation", null);
        SourceCode ex = lang.newSourceCode(new Coordinates(500, 50), "explanation0", null, explanationProps);
        ex.addMultilineCode(DESCRIPTION, null, null);
        //Slide slide = new Slide()

        lang.newText(new Coordinates(500, 330), "Learned G-Set", "learnedSetTitle", null, titleTextProperties);
        // Show the learned set to the user
        SourceCode ls = lang.newSourceCode(new Coordinates(500, 360), "learnedSet", null, sourceCodeProps);

        Variables var = lang.newVariables();

        lang.nextStep(stepDelay);

        // Highlight all cells
        //sm.highlightCell(0, sm.getLength() - 1, null, null);

        try {
            findGSet(var, sm, sc, ex, ls, classIndex, posClass, negClass);
        } catch (LineNotExistsException e) {
            e.printStackTrace();
        }
        scTitle.hide(TIMING);
        smTitle.hide(TIMING);
        lang.nextStep(stepDelay, "Conclusion");

        // AnimalScript output
        return lang.toString();
    }

    /**
     * runs the FindG-Set algorithm
     *
     * @param sm         training data (each row represents one example)
     * @param sc         pseudo code
     * @param ex         explanation
     * @param ls         learned sets
     * @param classIndex column index in sm describing the class of each example
     * @param posClass   positive class (in column at classIndex)
     * @param negClass   negative class (in column at classIndex)
     */
    private void findGSet(Variables var, StringMatrix sm, SourceCode sc, SourceCode ex, SourceCode ls, int classIndex,
                          String posClass, String negClass) {
        List<List<String[]>> G = new ArrayList<>();
        int currentGIndex = 0;
        var.declare("int", "currentGIndex", String.valueOf(currentGIndex), "stepper");
        // "stepper" is defined in VariableContext class and mapped to VariableRoles enum wrapper class

        // h0 is the most general hypothesis
        sc.highlight(0);
        ArrayList<String[]> G0 = new ArrayList<>();
        String[] h0 = new String[sm.getNrCols()];
        G0.add(h0);
        ex = replaceExplanation(ex, String.format("The most general hypothesis is %s.", hToString(h0)));
        var.declare("string", "h", hToString(h0), "temporary");
        lang.nextStep(stepDelay);

        // G starts with most general hypothesis: G0 = {h0}
        sc.unhighlight(0);
        sc.highlight(1);
        G.add(G0);
        String gString = gToString(G, currentGIndex);
        ls.addCodeLine(gString, "G" + currentGIndex, 0, TIMING);
        ex = replaceExplanation(ex,
                String.format("At first the G-Set contains only the most general hypothesis, thus %s.", gString));
        var.declare("string", "G0", gToString(G0), "gatherer");
        var.discard("h");
        lang.nextStep(stepDelay, "G0");

        // for each example e
        sc.unhighlight(1);
        sc.highlight(2);
        ex = replaceExplanation(ex,
                "For each training example, the set of hypotheses is refined to only cover\n" +
                        "all positive examples. In order to achieve this, hypotheses covering\n" +
                        "negative examples ar specialized in the least possible way while still\n" +
                        "covering all previously learned positive examples.");
        lang.nextStep(stepDelay);
        var.declare("int", "rowIndex", "0", "stepper");
        for (int i = 0; i < sm.getNrRows(); i++) {
            currentGIndex++;
            String[] e = new String[sm.getNrCols()];
            for (int j = 0; j < sm.getNrCols(); j++) {
                e[j] = sm.getElement(i, j);
            }
            sm.highlightCellColumnRange(i, 0, sm.getNrCols() - 1, TIMING, TIMING);
            var.set("currentGIndex", String.valueOf(currentGIndex));
            var.set("rowIndex", String.valueOf(i));
            var.declare("string", "e", hToString(e), "temporary");
            lang.nextStep(stepDelay);

            if (e[classIndex].equals(posClass)) {
                // if e is positive
                sc.highlight(3);
                sm.setGridFillColor(i, classIndex, Color.GREEN, TIMING, TIMING);
                ex = replaceExplanation(ex,
                        "The next example to be learned is positive.");
                lang.nextStep(stepDelay);

                // remove all h in G that do not cover e
                sc.highlight(4);
                ls.highlight(currentGIndex - 1);
                ex = replaceExplanation(ex,
                        "Since the example is positive, all hypothesis in G have to cover it.\n" +
                                "All hypothesis in G not covering the example are removed.");
                lang.nextStep(stepDelay);
                List<String[]> newG = new ArrayList<>();
                var.declare("string", "G" + currentGIndex, "", "gatherer");
                var.declare("string", "h", "", "temporary");
                hLoop:
                for (String[] h : G.get(currentGIndex - 1)) {
                    var.set("h", hToString(h));
                    var.declare("string", "colIndex", "0", "stepper");
                    for (int colIndex = 0; colIndex < sm.getNrCols(); colIndex++) {
                        var.set("colIndex", String.valueOf(colIndex));
                        if ((colIndex < classIndex && h[colIndex] != null && !h[colIndex].equals(e[colIndex]))
                                || (colIndex > classIndex && h[colIndex - 1] != null && !h[colIndex - 1].equals(e[colIndex]))) {
                            // h does not cover positive e --> do not add it to the next G set
                            ex = replaceExplanation(ex,
                                    String.format("%s does not cover the positive example.\n" +
                                            "It will be removed from G.", hToString(h)));
                            lang.nextStep(stepDelay);
                            continue hLoop;
                        }
                    }
                    // h covers positive e --> add it to the next G set
                    ex = replaceExplanation(ex,
                            String.format("%s covers the positive example.\n" +
                                    "It will be kept in G.", hToString(h)));
                    newG.add(h);
                    var.set("G" + currentGIndex, gToString(newG));
                    lang.nextStep(stepDelay);
                }
                G.add(newG);
                var.discard("colIndex");
                var.discard("h");
                var.set("G" + currentGIndex, gToString(newG));
                sc.unhighlight(3);
                sc.unhighlight(4);
                ls.unhighlight(currentGIndex - 1);
                lang.nextStep(stepDelay);
            } else if (e[classIndex].equals(negClass)) {
                // if e is negative
                sc.highlight(5);
                sm.setGridFillColor(i, classIndex, Color.RED, TIMING, TIMING);
                ex = replaceExplanation(ex,
                        "The next example to be learned is negative.");
                lang.nextStep(stepDelay);

                // specialize all h in G that cover e
                sc.highlight(6);
                ls.highlight(currentGIndex - 1);
                ex = replaceExplanation(ex,
                        "Since the example is negative, all hypothesis in G must not cover it.\n" +
                                "All hypothesis in G covering the example are refined.");
                lang.nextStep(stepDelay);
                List<String[]> newG = new ArrayList<>();
                var.declare("string", "G" + currentGIndex, "", "gatherer");
                var.declare("string", "h", "", "temporary");
                for (String[] h : G.get(currentGIndex - 1)) {
                    var.set("h", hToString(h));
                    if (!covers(h, e, classIndex)) {
                        // h does not cover negative e --> add it to the next G set and continue with next h
                        ex = replaceExplanation(ex,
                                String.format("%s does not cover the negative example.\n" +
                                        "It will be kept in G.", hToString(h)));
                        lang.nextStep(stepDelay);
                        newG.add(h);
                        var.set("G" + currentGIndex, gToString(newG));
                        continue;
                    }
                    // h covers negative e --> specialize it
                    sc.highlight(7);
                    sc.highlight(8);
                    ex = replaceExplanation(ex,
                            String.format("%s covers the negative example.\n" +
                                    "It will be refined.", hToString(h)));
                    lang.nextStep(stepDelay);
                    for (int colIndex = 0; colIndex < sm.getNrCols(); colIndex++) {
                        // for every c in e that is not in h
                        if ((colIndex < classIndex && h[colIndex] == null)
                                || (colIndex > classIndex && h[colIndex - 1] == null)) {
                            ex = replaceExplanation(ex,
                                    "For every condition in the example that is not already set in the hypothesis\n" +
                                            "a new hypothesis is created with that condition.");
                            lang.nextStep(stepDelay);

                            String c = e[colIndex];
                            var.declare("string", "c", c, "temporary");
                            sm.setGridFillColor(i, colIndex, Color.BLUE, TIMING, TIMING);
                            ex = replaceExplanation(ex,
                                    String.format("Condition c='%s'.", c));
                            lang.nextStep();

                            // for all cNew that negate c
                            sc.highlight(9);
                            ex = replaceExplanation(ex,
                                    String.format("All conditions c' that negate c='%s'\n" +
                                            "will be used to specialize the hypothesis h='%s'.", c, hToString(h)));
                            lang.nextStep(stepDelay);

                            List<String> seenC = new ArrayList<>();
                            seenC.add(c);
                            var.declare("string", "seenC", seenC.toString(), "temporary");
                            cLoop:
                            for (int rowIndex = 0; rowIndex < sm.getNrRows(); rowIndex++) {
                                String cNew = sm.getElement(rowIndex, colIndex);
                                if (!seenC.contains(cNew)) {
                                    seenC.add(cNew);
                                    var.set("c", cNew);
                                    var.set("seenC", seenC.toString());
                                    // create specialized hNew
                                    sc.highlight(10);
                                    String[] hNew = h.clone();
                                    hNew[colIndex < classIndex ? colIndex : colIndex - 1] = cNew;
                                    var.declare("string", "hNew", hToString(hNew), "temporary");
                                    ex = replaceExplanation(ex,
                                            String.format("c'='%s' specializes h='%s'\n" +
                                                    "to h'='%s'.", cNew, hToString(h), hToString(hNew)));
                                    lang.nextStep(stepDelay);

                                    // if hNew covers all previous positive examples
                                    sc.highlight(11);
                                    lang.nextStep(stepDelay);
                                    for (int k = 0; k < i; k++) {
                                        if (sm.getElement(k, classIndex).equals(posClass)) {
                                            if (!covers(hNew, rowToArray(sm, k), classIndex)) {
                                                // hNew does not cover a previous positive example
                                                sc.unhighlight(10);
                                                sc.unhighlight(11);
                                                var.discard("hNew");
                                                continue cLoop;
                                            }
                                        }
                                    }

                                    // add h to G
                                    sc.highlight(12);
                                    ex = replaceExplanation(ex,
                                            String.format("h'='%s' covers all previous positive examples,\n" +
                                                    "thus h' stays in G.", hToString(hNew)));
                                    newG.add(hNew);
                                    var.discard("hNew");
                                    var.set("G" + currentGIndex, gToString(newG));
                                    lang.nextStep(stepDelay);

                                    sc.unhighlight(10);
                                    sc.unhighlight(11);
                                    sc.unhighlight(12);
                                    lang.nextStep(stepDelay);
                                }
                            }
                            var.discard("c");
                            var.discard("seenC");
                            sc.unhighlight(9);
                            sm.setGridFillColor(i, colIndex, (Color) sm.getProperties().get("fillColor"), TIMING, TIMING);
                            // gotta re-set some values, since animal always puts the last one on top
                            sm.unhighlightCellColumnRange(i, 0, sm.getNrCols() - 1, TIMING, TIMING);
                            sm.highlightCellColumnRange(i, 0, sm.getNrCols() - 1, TIMING, TIMING);
                            sm.setGridFillColor(i, classIndex, Color.RED, TIMING, TIMING);
                            // done re-setting all the things, let's show this
                            lang.nextStep(stepDelay);
                        }
                    }
                    sc.unhighlight(7);
                    sc.unhighlight(8);
                    ls.unhighlight(currentGIndex - 1);
                    lang.nextStep(stepDelay);
                }
                G.add(newG);
                var.discard("h");
                var.set("G" + currentGIndex, gToString(newG));
                sc.unhighlight(5);
                sc.unhighlight(6);
                lang.nextStep(stepDelay);
            } else {
                sm.setGridFillColor(i, classIndex, Color.BLUE, TIMING, TIMING);
                String textUnknownClass = String.format("Found unknown class %s at (%d,%d)!", e[classIndex], i, classIndex);
                ex = replaceExplanation(ex,
                        textUnknownClass);
                lang.nextStep(stepDelay);
                throw new IllegalArgumentException(textUnknownClass);
            }
            var.discard("e");

            ls.addCodeLine(gToString(G, currentGIndex), "G" + currentGIndex, 0, TIMING);
            ls.highlight("G" + currentGIndex);
            ex = replaceExplanation(ex,
                    String.format("G%d contains all hypotheses learned on\n" +
                            "G%d and e='%s'.", currentGIndex, currentGIndex - 1, hToString(e)));
            lang.nextStep(stepDelay, "G" + currentGIndex);
            sm.unhighlightCellColumnRange(i, 0, sm.getNrCols() - 1, TIMING, TIMING);
            ls.unhighlight("G" + currentGIndex);
        }
        sc.unhighlight(2);
        var.discard("rowIndex");
        lang.nextStep(stepDelay);

        // end
        sc.highlight(13);
        ex = replaceExplanation(ex,
                String.format("G%d is the learned G-Set containing all most general hypotheses\n" +
                        "that are correct on the training set.", currentGIndex));
        lang.nextStep();
        sc.unhighlight(13);

        sc.hide(TIMING);
        sm.hide(TIMING);
        ex.addMultilineCode(String.format("The hypotheses in G%d may be used to predict the class of new, unknown examples.\n" +
                        "For this, all hypotheses have to be combined, e.g. by voting their outcomes.\n" +
                        "When voting, one has to consider corner-cases in which a draw might be voted.\n" +
                        "In this case, FindG-Set cannot predict a class.",
                currentGIndex), null, TIMING);
        var.discard("currentGIndex");
    }

    /**
     * does some crazy hacking to replace the explanation
     *
     * @param ex   the old explanation to be replaced
     * @param text the new explanation text
     * @return the new explanation
     */
    private SourceCode replaceExplanation(SourceCode ex, String text) {
        expCounter++;
        SourceCodeProperties exProps = new SourceCodeProperties();
        for (String key : ex.getProperties().getAllPropertyNames()) {
            exProps.set(key, ex.getProperties().get(key));
        }
        ex.hide();
        String explanationName = "explanation" + expCounter;
        ex = lang.newSourceCode(ex.getUpperLeft(), explanationName, null, exProps);
        ex.addMultilineCode(text, null, TIMING);
        return ex;
    }

    /**
     * checks whether hypothesis h covers example e
     *
     * @param h          hypothesis
     * @param e          example
     * @param classIndex column index of the class
     * @return true if h covers e, false if otherwise
     */
    private boolean covers(String[] h, String[] e, int classIndex) {
        for (int col = 0; col < e.length; col++) {
            if ((col < classIndex && h[col] != null && !h[col].equals(e[col]))
                    || (col > classIndex && h[col - 1] != null && !h[col - 1].equals(e[col]))) {
                return false;
            }
        }
        return true;
    }

    /**
     * converts a StringMatrix row into a String[]
     *
     * @param sm  StringMatrix
     * @param row row index in sm
     * @return String[] representation of the row in sm
     */
    private String[] rowToArray(StringMatrix sm, int row) {
        String[] array = new String[sm.getNrCols()];
        for (int col = 0; col < array.length; col++) {
            array[col] = sm.getElement(row, col);
        }
        return array;
    }

    /**
     * creates a String representation of a G-Set
     *
     * @param G             List of all G-Sets
     * @param currentGIndex index of the G-Set to be represented in G
     * @return a String representation of the G-Set at index currentGIndex in G
     */
    private String gToString(List<List<String[]>> G, int currentGIndex) {
        List<String[]> currentG = G.get(currentGIndex);
        return String.format("G%d: %s", currentGIndex, gToString(currentG));
    }

    /**
     * creates a String representation of a G-Set
     *
     * @param G a G-Set
     * @return a String representation of G
     */
    private String gToString(List<String[]> G) {
        StringBuilder hBuilder = new StringBuilder();
        hBuilder.append("{");
        for (int i = 0; i < G.size(); i++) {
            String[] h = G.get(i);
            hBuilder.append(" ");
            hBuilder.append(hToString(h));
            hBuilder.append(" ");
            if (i < (G.size() - 1)) {
                hBuilder.append(",");
            }
        }
        hBuilder.append("}");
        return hBuilder.toString();
    }

    /**
     * creates a String representation of a hypothesis
     *
     * @param h hypothesis
     * @return a String representation of h
     */
    private String hToString(String[] h) {
        StringBuilder hBuilder = new StringBuilder();
        hBuilder.append("<");
        for (int j = 1; j <= (h.length - 1); j++) {
            hBuilder.append(h[j - 1] == null ? "?" : h[j - 1]);
            if (j < (h.length - 1)) {
                hBuilder.append(",");
            }
        }
        hBuilder.append(">");
        return hBuilder.toString();
    }

    @Override
    public String getName() {
        return GENERATOR_NAME;
    }

    @Override
    public String getAlgorithmName() {
        return ALGO_NAME;
    }

    @Override
    public String getAnimationAuthor() {
        return AUTHOR;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public String getCodeExample() {
        return SOURCE_CODE;
    }

    @Override
    public String getFileExtension() {
        return FILE_EXTENSION;
    }

    @Override
    public Locale getContentLocale() {
        return Locale.ENGLISH;
    }

    @Override
    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
    }

    @Override
    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

    @Override
    public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives)
            throws IllegalArgumentException {
        String[][] trainingSet = (String[][]) primitives.get("trainingSet");
        int classIndex = (Integer) primitives.get("classIndex");
        String posClass = (String) primitives.get("posClass");
        String negClass = (String) primitives.get("negClass");
        if (posClass.equals(negClass)) {
            throw new IllegalArgumentException("The positive and negative class may not be equal!");
        }
        if (trainingSet.length == 0 || trainingSet[0].length == 0) {
            throw new IllegalArgumentException("Cannot learn on empty training set!");
        }
        if (classIndex >= trainingSet[0].length) {
            throw new IllegalArgumentException(
                    String.format("Class index (%d) may not be out of bounds (%d)!", classIndex, trainingSet[0].length));
        }
        for (int row = 0; row < trainingSet.length; row++) {
            String[] e = trainingSet[row];
            if (!(e[classIndex].equals(posClass) || e[classIndex].equals(negClass))) {
                throw new IllegalArgumentException(
                        String.format("Found unknown class '%s' at (%d,%d) in the stringMatrix!\n" +
                                        "The column at classIndex='%d' may only include posClass='%s' and negClass='%s'.",
                                e[classIndex], row, classIndex, classIndex, posClass, negClass));
            }
        }
        return true;
    }
}