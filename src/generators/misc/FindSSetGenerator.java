/*
 * FindSSetGenerator.java
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
import java.util.*;
import java.util.List;

public class FindSSetGenerator implements ValidatingGenerator {
    private static final String DESCRIPTION = "FindS-Set finds the most specific hypotheses that are correct on the data.\n"
            + "FindS-Set starts with the most specific hypothesis that covers no examples.\n"
            + "This hypothesis is iteratively refined to more general ones where each more\n"
            + "general hypothesis only differentiates in one condition from the previous more\n"
            + "specific hypothesis. Unlike the Find-S algorithm, FindS-Set does not select one\n"
            + "of the more general hypotheses in each refinement step but instead keeps all of\n"
            + "them that are correct on the data. This approach results in a bias towards specific\n"
            + "hypotheses.\n"
            + "The counterpart of FindS-Set is FindG-Set, starting with the most general hypothesis\n"
            + "and specializing it in each step, resulting in a bias towards more general hypotheses.";
    private static final String SOURCE_CODE = "I.   h = most specific hypothesis in H (covering no examples)\n"
            + "II.  S = { h }\n"
            + "III. for each training example e\n"
            + "     a) if e is positive\n"
            + "           for all hypotheses h ∈ S that do not cover e\n"
            + "              S = S ∖ { h }\n"
            + "              for every condition c in e that is not part of h\n"
            + "                  h' = h ∪ { c }\n"
            + "                  if h' covers e\n"
            + "                     S = S ∪ { h' }\n"
            + "           for all hypotheses h₁, h₂ ∈ S\n"
            + "              if h₁ is more general than h₂\n"
            + "                  S = S ∖ { h₁ }\n"
            + "     b) if e is negative\n"
            + "           remove all h ∈ S that cover e\n"
            + "IV.  return G";
    private static final String AUTHOR = "Valentin Kuhn";
    private static final String FILE_EXTENSION = "asu";
    private static final String ALGO_NAME = "FindS-Set";
    private static final String GENERATOR_NAME = "FindS-Set";
    private static final Timing TIMING = null; // Timing.INSTANTEOUS;
    private int stepDelay; // Timing.MEDIUM.getDelay();
    private Language lang;
    private int expCounter = 0;
    /**
     * states whether h0 has already been changed (from the most specific one)
     */
    private boolean h0Unchanged;
    /**
     * defines a "more specific than" relation on hypotheses<br />
     * a hypothesis is more specific than another only if it includes all conditions of the other hypothesis and at least one more<br />
     * compare(h1,h2) > 0 -> h1 is more specific than h2<br />
     * compare(h1,h2) < 0 -> h1 is more general than h2<br />
     * comapre(h1,h2) = 0 -> h1 and h2 are equal or cannot be put into a "more specific than" relation
     */
    private Comparator<String[]> hComp = (h1, h2) -> {
        if (h1.length != h2.length) {
            throw new IllegalArgumentException(
                    String.format(Locale.getDefault(),
                            "Cannot compare h1=%s to h2=%s!",
                            hToString(h1), hToString(h2)));
        }
        int h1Conditions = 0;
        int h2Conditions = 0;
        for (int i = 0; i < h1.length; i++) {
            if ((h1[i] == null && h2[i] == null)
                    || (h1[i] != null && h2[i] != null && h1[i].equals(h2[i]))) {
                continue;
            }
            if (h1[i] != null) {
                h1Conditions++;
            }
            if (h2[i] != null) {
                h2Conditions++;
            }
        }
        if (h1Conditions != 0 && h2Conditions != 0) {
            // hypotheses don't include each other
            return 0;
        }
        return h1Conditions - h2Conditions;
    };

    public FindSSetGenerator() {
    }

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
        Text introTitle = lang.newText(new Coordinates(20, 20), "FindS-Set", "introTitle", null, titleTextProperties);
        SourceCode intro = lang.newSourceCode(new Coordinates(20, 50), "explanation0", null, explanationProps);
        intro.addMultilineCode(DESCRIPTION, null, null);
        lang.nextStep(stepDelay, "Introduction");
        introTitle.hide(TIMING);
        intro.hide(TIMING);

        // Visuals

        Text scTitle = lang.newText(new Coordinates(20, 20), "FindS-Set (Pseudo-Code)", "sourceCodeTitle", null, titleTextProperties);
        // Show the pseudo source code to the user
        SourceCode sc = lang.newSourceCode(new Coordinates(20, 50), "sourceCode", null, sourceCodeProps);
        sc.addCodeLine("I.   h = most specific hypothesis in H (covering no examples)", null, 0, null); // 00
        sc.addCodeLine("II.  S = { h }", null, 0, null);                                                // 01
        sc.addCodeLine("III. for each training example e", null, 0, null);                              // 02
        sc.addCodeLine("a) if e is positive", null, 1, null);                                           // 03
        sc.addCodeLine("for all hypotheses h ∈ S that do not cover e", null, 2, null);                 // 04
        sc.addCodeLine("S = S ∖ { h }", null, 3, null);                                                 // 05
        sc.addCodeLine("for every condition c in e that is not part of h", null, 3, null);              // 06
        sc.addCodeLine("h' = h ∪ { c }", null, 4, null);                                               // 07
        sc.addCodeLine("if h' covers e", null, 4, null);                                                // 08
        sc.addCodeLine("S = S ∪ { h' }", null, 5, null);                                               // 09
        sc.addCodeLine("for all hypotheses h₁, h₂ ∈ S", null, 2, null);                                 // 10
        sc.addCodeLine("if h₁ is more general than h₂", null, 3, null);                                  // 11
        sc.addCodeLine("S = S ∖ { h₁ }", null, 4, null);                                                 // 12
        sc.addCodeLine("b) if e is negative", null, 1, null);                                           // 13
        sc.addCodeLine("remove all h ∈ S that cover e", null, 2, null);                                // 14
        sc.addCodeLine("IV.  return G", null, 0, null);                                                 // 15

        Text smTitle = lang.newText(new Coordinates(20, 330), "Training set", "trainingSetTitle", null, titleTextProperties);
        // Show the training set to the user
        StringMatrix sm = lang.newStringMatrix(new Coordinates(20, 360), trainingSet, "trainingSet", null, matrixProps);

        lang.newText(new Coordinates(500, 20), "Explanation", "explanationTitle", null, titleTextProperties);
        // Show description / explanation to the user --> to be updated in each step
        //Text ex = lang.newText(new Coordinates(530, 50), DESCRIPTION.replaceAll("\n", " "), "explanation", null);
        SourceCode ex = lang.newSourceCode(new Coordinates(500, 50), "explanation0", null, explanationProps);
        ex.addMultilineCode(DESCRIPTION, null, null);
        //Slide slide = new Slide()

        lang.newText(new Coordinates(500, 330), "Learned S-Set", "learnedSetTitle", null, titleTextProperties);
        // Show the learned set to the user
        SourceCode ls = lang.newSourceCode(new Coordinates(500, 360), "learnedSet", null, sourceCodeProps);

        Variables var = lang.newVariables();

        lang.nextStep(stepDelay);

        // Highlight all cells
        //sm.highlightCell(0, sm.getLength() - 1, null, null);

        try {
            findSSet(var, sm, sc, ex, ls, classIndex, posClass, negClass);
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
     * runs the FindS-Set algorithm
     *
     * @param sm         training data (each row represents one example)
     * @param sc         pseudo code
     * @param ex         explanation
     * @param ls         learned sets
     * @param classIndex column index in sm describing the class of each example
     * @param posClass   positive class (in column at classIndex)
     * @param negClass   negative class (in column at classIndex)
     */
    private void findSSet(Variables var, StringMatrix sm, SourceCode sc, SourceCode ex, SourceCode ls, int classIndex,
                          String posClass, String negClass) {
        List<List<String[]>> S = new ArrayList<>();
        int currentSIndex = 0;
        var.declare("int", "currentSIndex", String.valueOf(currentSIndex), "stepper");
        // "stepper" is defined in VariableContext class and mapped to VariableRoles enum wrapper class

        // h0 is the most specific hypothesis
        sc.highlight(0);
        ArrayList<String[]> S0 = new ArrayList<>();
        String[] h0 = new String[sm.getNrCols()];
        h0Unchanged = true;
        S0.add(h0);
        ex = replaceExplanation(ex, String.format("The most specific hypothesis is %s.", hToString(h0)));
        var.declare("string", "h", hToString(h0), "temporary");
        lang.nextStep(stepDelay);

        // S starts with most general hypothesis: S0 = {h0}
        sc.unhighlight(0);
        sc.highlight(1);
        S.add(S0);
        String gString = gToString(S, currentSIndex);
        ls.addCodeLine(gString, "S" + currentSIndex, 0, TIMING);
        ex = replaceExplanation(ex,
                String.format("At first the S-Set contains only the most specific hypothesis, thus %s.", gString));
        var.declare("string", "S0", gToString(S0), "gatherer");
        var.discard("h");
        lang.nextStep(stepDelay, "S0");

        // for each example e
        sc.unhighlight(1);
        sc.highlight(2);
        ex = replaceExplanation(ex,
                "For each training example, the set of hypotheses is refined to only cover\n" +
                        "all positive examples. In order to achieve this, hypotheses covering\n" +
                        "negative examples ar generalized in the least possible way while still\n" +
                        "not covering any previously learned negative examples.");
        lang.nextStep(stepDelay);
        var.declare("int", "rowIndex", "0", "stepper");
        for (int i = 0; i < sm.getNrRows(); i++) {
            currentSIndex++;
            String[] e = new String[sm.getNrCols()];
            for (int j = 0; j < sm.getNrCols(); j++) {
                e[j] = sm.getElement(i, j);
            }
            sm.highlightCellColumnRange(i, 0, sm.getNrCols() - 1, TIMING, TIMING);
            var.set("currentSIndex", String.valueOf(currentSIndex));
            var.set("rowIndex", String.valueOf(i));
            var.declare("string", "e", hToString(e), "temporary");
            lang.nextStep(stepDelay);

            if (e[classIndex].equals(negClass)) {
                // if e is negative
                sc.highlight(13);
                sm.setGridFillColor(i, classIndex, Color.RED, TIMING, TIMING);
                ex = replaceExplanation(ex,
                        "The next example to be learned is negative.");
                lang.nextStep(stepDelay);

                // remove all h in S that do not cover e
                sc.highlight(14);
                ls.highlight(currentSIndex - 1);
                ex = replaceExplanation(ex,
                        "Since the example is negative, all hypothesis in S must not cover it.\n" +
                                "All hypothesis in S covering the example are removed.");
                lang.nextStep(stepDelay);
                List<String[]> newS = new ArrayList<>();
                if (h0Unchanged) {
                    ex = replaceExplanation(ex, "Since the most specific hypothesis does not cover any exmaples,\n" +
                            "none have to be removed");
                    var.declare("string", "S" + currentSIndex, "", "gatherer");
                    newS.add(h0);
                    S.add(newS);
                    lang.nextStep(stepDelay);
                } else {
                    var.declare("string", "S" + currentSIndex, "", "gatherer");
                    var.declare("string", "h", "", "temporary");
                    hLoop:
                    for (String[] h : S.get(currentSIndex - 1)) {
                        var.set("h", hToString(h));
                        var.declare("string", "colIndex", "0", "stepper");
                        for (int colIndex = 0; colIndex < sm.getNrCols(); colIndex++) {
                            var.set("colIndex", String.valueOf(colIndex));
                            if (!covers(h, e, classIndex)) {
                                // h does not cover negative e --> add it to the next S set
                                ex = replaceExplanation(ex,
                                        String.format("%s does not cover the negative example.\n" +
                                                "It will be kept in S.", hToString(h)));
                                newS.add(h);
                                lang.nextStep(stepDelay);
                                continue hLoop;
                            }
                        }
                        // h covers negative e --> do not add it to the next S set
                        ex = replaceExplanation(ex,
                                String.format("%s covers the negative example.\n" +
                                        "It will be removed from S.", hToString(h)));
                        var.set("S" + currentSIndex, gToString(newS));
                        lang.nextStep(stepDelay);
                    }
                    S.add(newS);
                    var.discard("colIndex");
                    var.discard("h");
                }
                var.set("S" + currentSIndex, gToString(newS));
                sc.unhighlight(13);
                sc.unhighlight(14);
                ls.unhighlight(currentSIndex - 1);
                lang.nextStep(stepDelay);
            } else if (e[classIndex].equals(posClass)) {
                // if e is positive
                sc.highlight(3);
                sm.setGridFillColor(i, classIndex, Color.GREEN, TIMING, TIMING);
                ex = replaceExplanation(ex,
                        "The next example to be learned is positive.");
                lang.nextStep(stepDelay);

                // generalize all h in S that cover e
                List<String[]> newS = new ArrayList<>();
                if (h0Unchanged) {
                    ex = replaceExplanation(ex, "Since the most specific hypothesis does not cover any examples,\n" +
                            "all conditions of this positive example will be in the next hypothesis.");
                    h0Unchanged = false;
                    String[] h1 = new String[e.length];
                    for (int h1Index = 0; h1Index < h1.length; h1Index++) {
                        if (h1Index != classIndex)
                            h1[h1Index] = e[h1Index];
                    }
                    var.declare("string", "S" + currentSIndex, "", "gatherer");
                    newS.add(h1);
                    lang.nextStep(stepDelay);
                } else {
                    sc.highlight(4);
                    ls.highlight(currentSIndex - 1);
                    ex = replaceExplanation(ex,
                            "Since the example is positive, all hypothesis in S have to cover it.\n" +
                                    "All hypothesis in S not covering the example are refined.");
                    lang.nextStep(stepDelay);
                    var.declare("string", "S" + currentSIndex, "", "gatherer");
                    var.declare("string", "h", "", "temporary");
                    for (String[] h : S.get(currentSIndex - 1)) {
                        var.set("h", hToString(h));
                        if (covers(h, e, classIndex)) {
                            // h covers positive e --> add it to the next S set and continue with next h
                            ex = replaceExplanation(ex,
                                    String.format("%s covers the positive example.\n" +
                                            "It will be kept in S.", hToString(h)));
                            lang.nextStep(stepDelay);
                            newS.add(h);
                            var.set("S" + currentSIndex, gToString(newS));
                            continue;
                        }
                        // h does not cover positive e --> generalize it
                        sc.highlight(5);
                        sc.highlight(6);
                        ex = replaceExplanation(ex,
                                String.format("%s does not cover the positive example.\n" +
                                        "It will be refined.", hToString(h)));
                        lang.nextStep(stepDelay);
                        sc.unhighlight(5);
                        for (int colIndex = 0; colIndex < sm.getNrCols(); colIndex++) {
                            // for every c in e that is not in h
                            if ((colIndex < classIndex && h[colIndex] != null)
                                    || (colIndex > classIndex && h[colIndex - 1] != null)) {
                                ex = replaceExplanation(ex,
                                        "For every condition in the example that is not already set in the hypothesis\n" +
                                                "a new hypothesis is created with that condition.");
                                lang.nextStep(stepDelay);

                                String c = e[colIndex];
                                String[] hNew = h.clone();
                                var.declare("string", "c", c, "temporary");
                                sm.setGridFillColor(i, colIndex, Color.BLUE, TIMING, TIMING);
                                ex = replaceExplanation(ex,
                                        String.format("Condition c='%s'.", c));
                                lang.nextStep();

                                // for all cNew that negate c
                                sc.highlight(7);
                                int cIndexInH = colIndex < classIndex ? colIndex : colIndex - 1;
                                if (!hNew[cIndexInH].equals(c)) {
                                    // h has another condition -> generalize to most general condition
                                    hNew[cIndexInH] = null;
                                }
                                ex = replaceExplanation(ex,
                                        String.format("Condition c='%s'\n" +
                                                "will be used to generalize the hypothesis h='%s'\n" +
                                                "to h'='%s'.", c, hToString(h), hToString(hNew)));
                                var.declare("string", "hNew", hToString(hNew), "temporary");
                                lang.nextStep(stepDelay);
                                var.discard("hNew");
                                var.discard("c");

                                sc.unhighlight(7);
                                sc.highlight(8);
                                sc.highlight(9);
                                if (covers(hNew, e, classIndex)) {
                                    ex = replaceExplanation(ex, "The refined hypothesis h' will be kept in S\n" +
                                            "since it covers the positive example e.");
                                    newS.add(hNew);
                                } else {
                                    ex = replaceExplanation(ex, "The refined hypothesis h' will be further generalized\n" +
                                            "since it does not cover the positive example e.");
                                    h = hNew.clone();
                                }
                                lang.nextStep(stepDelay);

                                sc.unhighlight(8);
                                sc.unhighlight(9);
                                sc.unhighlight(6);
                                sm.setGridFillColor(i, colIndex, (Color) sm.getProperties().get("fillColor"), TIMING, TIMING);
                                // gotta re-set some values, since animal always puts the last one on top
                                sm.unhighlightCellColumnRange(i, 0, sm.getNrCols() - 1, TIMING, TIMING);
                                sm.highlightCellColumnRange(i, 0, sm.getNrCols() - 1, TIMING, TIMING);
                                sm.setGridFillColor(i, classIndex, Color.GREEN, TIMING, TIMING);
                                // done re-setting all the things, let's show this
                                lang.nextStep(stepDelay);
                            }
                        }
                        sc.unhighlight(6);
                        ls.unhighlight(currentSIndex - 1);
                        lang.nextStep(stepDelay);
                    }
                    sc.unhighlight(4);
                    sc.highlight(10);
                    var.discard("h");
                    lang.nextStep(stepDelay);
                    for (int h1CompIndex = 0; h1CompIndex < newS.size(); h1CompIndex++) {
                        ex = replaceExplanation(ex, "Remove all hypothesis in S that are more general\n" +
                                "than another hypothesis in S");
                        lang.nextStep(stepDelay);
                        String[] h1comp = newS.get(h1CompIndex);
                        var.declare("string", "h1", hToString(h1comp), "temporary");
                        ex = replaceExplanation(ex, String.format(Locale.getDefault(),
                                "Comparing h1=%s to all other hypotheses.", hToString(h1comp)));
                        lang.nextStep(stepDelay);
                        var.declare("string", "h2", "", "temporary");
                        for (int h2CompIndex = (h1CompIndex + 1); h2CompIndex < newS.size(); h2CompIndex++) {
                            String[] h2comp = newS.get(h2CompIndex);
                            var.set("h2", hToString(h2comp));
                            ex = replaceExplanation(ex, String.format(Locale.getDefault(),
                                    "Comparing h1=%s to h2=%s.", hToString(h1comp), hToString(h2comp)));
                            lang.nextStep(stepDelay);
                            if (hComp.compare(h1comp, h2comp) < 0) {
                                newS.remove(h1comp);
                            } else if (hComp.compare(h1comp, h2comp) > 0) {
                                newS.remove(h2comp);
                            }
                        }
                        var.discard("h1");
                        var.discard("h2");
                        ex = replaceExplanation(ex, String.format(Locale.getDefault(),
                                "Finished comparing h1=%s to other hypotheses", hToString(h1comp)));
                        lang.nextStep(stepDelay);
                    }
                }

                S.add(newS);
                var.set("S" + currentSIndex, gToString(newS));
                sc.unhighlight(3);
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

            ls.addCodeLine(gToString(S, currentSIndex), "S" + currentSIndex, 0, TIMING);
            ls.highlight("S" + currentSIndex);
            ex = replaceExplanation(ex,
                    String.format("S%d contains all hypotheses learned on\n" +
                            "S%d and e='%s'.", currentSIndex, currentSIndex - 1, hToString(e)));
            lang.nextStep(stepDelay, "S" + currentSIndex);
            sm.unhighlightCellColumnRange(i, 0, sm.getNrCols() - 1, TIMING, TIMING);
            ls.unhighlight("S" + currentSIndex);
        }
        sc.unhighlight(2);
        var.discard("rowIndex");
        lang.nextStep(stepDelay);

        // end
        sc.highlight(15);
        ex = replaceExplanation(ex,
                String.format("S%d is the learned S-Set containing all most specific hypotheses\n" +
                        "that are correct on the training set.", currentSIndex));
        lang.nextStep();
        sc.unhighlight(15);

        sc.hide(TIMING);
        sm.hide(TIMING);
        ex.addMultilineCode(String.format("The hypotheses in S%d may be used to predict the class of new, unknown examples.\n" +
                        "For this, all hypotheses have to be combined, e.g. by voting their outcomes.\n" +
                        "When voting, one has to consider corner-cases in which a draw might be voted.\n" +
                        "In this case, FindS-Set cannot predict a class.",
                currentSIndex), null, TIMING);
        var.discard("currentSIndex");
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
        if (h0Unchanged)
            return false;
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
            hBuilder.append(h[j - 1] == null ? (h0Unchanged ? "∅" : "?") : h[j - 1]);
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