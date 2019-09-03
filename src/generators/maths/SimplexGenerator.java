package generators.maths;/*
 * SimplexGenerator.java
 * Andreas Bauer, Fabian Bauer, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.addons.InfoBox;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.*;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.maths.simplex.SimplexAlgorithm;
import generators.maths.simplex.SimplexState;
import generators.maths.simplex.Utils;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleSelectionQuestionModel;
import translator.Translator;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class SimplexGenerator implements ValidatingGenerator {
    private Locale locale;
    private Language lang;
    private Translator translator;
    private Font textFont;
    private MatrixProperties matrixProps;
    private SourceCodeProperties sourceCodeProps;
    private TextProperties textProps;
    private TextProperties finalRemarksProps;

    // Label --> Code line; LinkedHashMap preserves insertion order!
    private Map<String, String> sourceCode = new LinkedHashMap<>();
    private List<String> description = new ArrayList<>();
    private List<String> introduction = new ArrayList<>();

    private Random rand = new Random();

    private boolean askQuestions;

    public SimplexGenerator(String textFile, Locale locale) {
        this.locale = locale;
        this.translator = new Translator(textFile, this.locale);

        // Load source code from resource file.
        int[] codeLines = new int[] {0, 10, 11, 20, 21, 30, 31, 311, 32, 33, 34, 40, 41, 42, 43};
        for (int lineNumber : codeLines) {
            String label = String.format("code%02d", lineNumber);
            String codeLine = translator.translateMessage(label);
            if (lineNumber % 10 != 0) {
                codeLine = "    " + codeLine; // indentation
                if (lineNumber > 100) {
                    codeLine = "    " + codeLine;
                }
            }
            sourceCode.put(label, codeLine);
        }

        // Load description.
        int[] descriptionLines = new int[] {10, 20, 21, 22, 23, 30, 40, 50, 60};
        for (int lineNumber : descriptionLines) {
            String label = String.format("exp%02d", lineNumber);
            String line = translator.translateMessage(label);
            if (lineNumber % 10 != 0) {
                line = "         " + line; // indentation
            }
            description.add(line);
        }

        // Load introduction.
        int[] introLines = new int[] {30, 40, 50, 60, 70};
        for (int i = 0; i < 5; i++) {
            introduction.add(description.get(i));
        }
        for (int lineNumber : introLines) {
            String label = String.format("intro%02d", lineNumber);
            String line = translator.translateMessage(label);
            introduction.add(line);
        }
    }

    public SimplexGenerator() {
        this("resources/SimplexGenerator", Locale.US);
    }

    public void init() {
        lang = new AnimalScript("Simplex algorithm", "Andreas Bauer, Fabian Bauer", 800, 600);
        lang.setStepMode(true);
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

        textFont = new Font(Font.SANS_SERIF, Font.PLAIN, 16);
        textProps = new TextProperties();
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, textFont);

        Font fatTextFont = new Font(Font.SANS_SERIF, Font.BOLD, 16);
        finalRemarksProps = new TextProperties();
        finalRemarksProps.set(AnimationPropertiesKeys.FONT_PROPERTY, fatTextFont);
    }

    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
        // Input values.
        askQuestions = (boolean) primitives.get("askQuestions");
        double[][] A = Utils.intMatrixToDoubleMatrix((int[][]) primitives.get("A"));
        double[] b = Utils.intArrayToDoubleArray((int[]) primitives.get("b"));
        double[] c = DoubleStream.of(Utils.intArrayToDoubleArray((int[]) primitives.get("c"))).map(d -> -d).toArray();
        matrixProps = (MatrixProperties) props.getPropertiesByName("matrixProperties");
        sourceCodeProps = (SourceCodeProperties) props.getPropertiesByName("codeProperties");

        Coordinates topLeftAnchor = new Coordinates(10, 10);

        // Headline.
        TextProperties hp = new TextProperties();
        hp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 30));
        Text header = lang.newText(topLeftAnchor, translator.translateMessage("name"), "title", null, hp);
        RectProperties rp = new RectProperties();
        rp.set(AnimationPropertiesKeys.FILL_PROPERTY,  Color.LIGHT_GRAY);
        rp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        rp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 15);
        lang.newRect(new Offset(-5, -5, header, "NW"), new Offset(5, 5, header, "SE"), "null", null, rp);

        // ####### INTRO #######

        InfoBox descriptionBox = new InfoBox(lang, new Offset(50, 0, header, AnimalScript.DIRECTION_SW), 22, "");
        descriptionBox.setText(Utils.insertLineBreaks(introduction, 140));

        lang.nextStep();

        descriptionBox.setText(new ArrayList<>());

        // ####### ALGORITHM #######

        // Load current state.
        SimplexState currentState = new SimplexState(A, b, c);

        int height = A.length;
        int width = A[0].length;

        // Simplex table.
        Node tablePosition = new Offset(50, 50, topLeftAnchor, AnimalScript.DIRECTION_SW);
        String[][] table = simplexStateToStringMatrix(currentState);
        StringMatrix simplexTable = lang.newStringMatrix(tablePosition, table, "simplexTable", null, matrixProps);
        //simplexTable.setGridFillColor(0, 0, Color.BLUE, null, null); // doesn't work ...

        // Display pseudo code.
        Node codeAnchor = new Offset(0, 0, simplexTable, AnimalScript.DIRECTION_SW);
        SourceCode code = lang.newSourceCode(codeAnchor, "code", null, sourceCodeProps);
        for (Map.Entry<String, String> line : sourceCode.entrySet()) {
            code.addCodeLine(line.getValue(), line.getKey(), 0, null);
        }

        // Results table.
        Offset resultsTablePos = new Offset(50, 0, code, AnimalScript.DIRECTION_NE);
        int resultRowCount = 10; // excluding head row
        String[][] initialResultsTable = new String[resultRowCount + 1][3];
        Utils.clearMatrix(initialResultsTable, "");
        initialResultsTable[0] = new String[] {"i", "x", "F(x)"};
        StringMatrix resultsTable = lang.newStringMatrix(resultsTablePos, initialResultsTable, "resultsTable", null, matrixProps);

        List<String[]> results = new ArrayList<>(resultRowCount);

        int limit = 100;
        int iteration = 1;

        lang.nextStep(translator.translateMessage("iteration", Integer.toString(iteration)));

        // ## Step 1: check if current solution is optimal ##
        Utils.highlightCode(code, "code10");

        results.add(new String[] {Integer.toString(iteration),
                formatDoubleArray(SimplexAlgorithm.getCurrentBasicSolution(currentState)),
                formatDouble(currentState.getF())});
        Utils.updateResultsTable(resultsTable, results);

        maybeAskQuestion(iteration, lang, currentState);

        lang.nextStep();

        while (!SimplexAlgorithm.stateIsOptimal(currentState) && (iteration++ < limit)) {

            // ## Step 2: Find pivot column ##
            Utils.highlightCode(code, "code20", "code21");

            int pivotCol = SimplexAlgorithm.findPivotColumnIndex(currentState);
            Utils.highlightColumn(simplexTable, pivotCol + 1);

            lang.nextStep();

            // ## Step 3: Find pivot row ##
            Utils.highlightCode(code, "code30", "code31");

            double[] improvementVector = SimplexAlgorithm.calculateImprovementVector(currentState, pivotCol);

            if (SimplexAlgorithm.modelIsUnbounded(improvementVector)) {
                lang.nextStep(translator.translateMessage("final"));

                Utils.highlightCode(code, "code30", "code31", "code311");

                Text t = lang.newText(new Offset(50, 0, simplexTable, AnimalScript.DIRECTION_NE), translator.translateMessage("result"), "finalMsg", null, finalRemarksProps);
                lang.newText(new Offset(0, 0, t, AnimalScript.DIRECTION_SW), translator.translateMessage("unbounded"), "finalMsg", null, finalRemarksProps);

                // EXIT HERE
                lang.finalizeGeneration();
                return lang.toString();
            }

            lang.nextStep();

            Utils.highlightCode(code, "code30", "code32");

            // Insert improment calculation into table.
            for (int i = 0; i < improvementVector.length; i++) {
                String cellContent;
                if (Double.isNaN(improvementVector[i])) {
                    cellContent = String.format("(%s)", translator.translateMessage("negative_ait"));
                }
                else if (Double.isInfinite(improvementVector[i])) {
                    cellContent = String.format("(%s)", translator.translateMessage("zero_ait"));
                }
                else {
                    cellContent = String.format("%s / %s = %s",
                        formatDouble(currentState.getValueAt(i, currentState.getColCount() - 1)),
                        formatDouble(currentState.getValueAt(i, pivotCol)),
                        formatDouble(improvementVector[i]));
                }
                simplexTable.put(i + 1, width + 2, cellContent, null, null);
                simplexTable.highlightCell(i + 1, width + 2, null, null);
            }

            lang.nextStep();

            Utils.unhighlightColumn(simplexTable, width + 2);
            Utils.highlightCode(code, "code30", "code33");

            int pivotRow = SimplexAlgorithm.findPivotRowIndex(improvementVector);
            Utils.highlightRow(simplexTable, pivotRow + 1);

            lang.nextStep();

            Utils.highlightCode(code, "code30", "code34");

            // Unhighlight pivot column and row
            Utils.unhighlightColumn(simplexTable, pivotCol + 1);
            Utils.unhighlightRow(simplexTable, pivotRow + 1);

            // Highlight pivot element only
            double pivotElement = SimplexAlgorithm.getPivotElement(currentState, pivotRow, pivotCol);
            simplexTable.highlightCell(pivotRow + 1, pivotCol + 1, null, null);
            simplexTable.highlightElem(pivotRow + 1, pivotCol + 1, null, null);

            lang.nextStep();

            // ## Step 4: Replace base variable ##
            Utils.highlightCode(code, "code40", "code41");

            currentState = SimplexAlgorithm.replaceBaseVariable(currentState, pivotRow, pivotCol);
            updateStringMatrix(simplexTable, simplexStateToStringMatrix(currentState));
            simplexTable.unhighlightCell(pivotRow + 1, pivotCol + 1, null, null); // Unhighlight pivot element
            simplexTable.highlightCell(0, pivotCol + 1, null, null);
            simplexTable.highlightCell(pivotRow + 1, 0, null, null);

            lang.nextStep();

            simplexTable.unhighlightCell(0, pivotCol + 1, null, null);
            simplexTable.unhighlightCell(pivotRow + 1, 0, null, null);

            // ## Step 4: Update pivot row (divide by pivot element) ##
            Utils.highlightCode(code, "code40", "code42");

            simplexTable.put(pivotRow + 1, simplexTable.getNrCols() - 1, String.format("/ %s", formatDouble(pivotElement)), null, null);

            lang.nextStep();

            currentState = SimplexAlgorithm.updatePivotRow(currentState, pivotRow, pivotCol);
            updateStringMatrix(simplexTable, simplexStateToStringMatrix(currentState));

            Utils.highlightRow(simplexTable, pivotRow + 1);

            lang.nextStep();

            // ## Step 4: Update other rows such that there is a unit vector under the pivot column ##
            Utils.highlightCode(code, "code40", "code43");
            Utils.unhighlightRow(simplexTable, pivotRow + 1);

            String formattedPivotRow = DoubleStream.of(currentState.getRow(pivotRow))
                    .mapToObj(this::formatDouble).collect(Collectors.joining(" "));
            for (int i = 1; i < height + 2; i++) {
                if (i - 1 != pivotRow) {
                    String rowFactor = formatDouble(currentState.getValueAt(i - 1, pivotCol));
                    String correctionString = String.format("- (%s) * [%s]", rowFactor, formattedPivotRow);
                    simplexTable.put(i, simplexTable.getNrCols() - 1, correctionString, null, null);
                    Utils.highlightRow(simplexTable, i);
                }
            }

            lang.nextStep();

            currentState = SimplexAlgorithm.updateAllRows(currentState, pivotRow, pivotCol);
            updateStringMatrix(simplexTable, simplexStateToStringMatrix(currentState));

            for (int i = 1; i < height + 2; i++) {
                if (i != pivotRow + 1) {
                    Utils.highlightRow(simplexTable, i);
                }
            }

            lang.nextStep(translator.translateMessage("iteration", Integer.toString(iteration)));

            simplexTable.unhighlightElem(pivotRow + 1, pivotCol + 1, null, null);
            Utils.unhighlightAll(simplexTable);

            Utils.highlightCode(code, "code10");

            results.add(new String[] {Integer.toString(iteration),
                    formatDoubleArray(SimplexAlgorithm.getCurrentBasicSolution(currentState)),
                    formatDouble(currentState.getF())});
            Utils.updateResultsTable(resultsTable, results);

            maybeAskQuestion(iteration, lang, currentState);

            lang.nextStep();
        }

        Utils.highlightCode(code, "code10", "code11");

        lang.nextStep(translator.translateMessage("final"));

        String finalMsg = translator.translateMessage("success",
                Integer.toString(iteration),
                formatDoubleArray(SimplexAlgorithm.getCurrentBasicSolution(currentState)),
                formatDouble(currentState.getF()));

        Text t = lang.newText(new Offset(50, 0, simplexTable, AnimalScript.DIRECTION_NE), translator.translateMessage("result"), "finalMsg", null, finalRemarksProps);
        lang.newText(new Offset(0, 0, t, AnimalScript.DIRECTION_SW), finalMsg, "finalMsg", null, finalRemarksProps);

        lang.finalizeGeneration();
        return lang.toString();
    }

    private void maybeAskQuestion(int iteration, Language lang, SimplexState currentState) {
        if (!askQuestions) {
            return;
        }

        int type = rand.nextInt(2);

        if (type == 0) {
            MultipleSelectionQuestionModel q = new MultipleSelectionQuestionModel("q1_" + iteration);
            q.setPrompt(translator.translateMessage("q1prompt"));
            double[] c = currentState.getC();
            for (int i = 0; i < c.length; i++) {
                int reward = c[i] < 0 ? 1 : 0;
                String variable = Utils.getXVariable(i + 1);
                String feedback;
                if (reward > 0) {
                    feedback = translator.translateMessage("q1correct", variable);
                }
                else {
                    feedback = translator.translateMessage("q1incorrect", variable);
                }

                q.addAnswer(variable, reward, feedback);
            }
            boolean stateIsOptimal = SimplexAlgorithm.stateIsOptimal(currentState);
            q.addAnswer(translator.translateMessage("q1none"),
                    stateIsOptimal ? 1 : 0,
                    stateIsOptimal ? translator.translateMessage("q1none_correct") : translator.translateMessage("q1none_incorrect"));
            lang.addMSQuestion(q);
        }
        else if (type == 1) {
            if (SimplexAlgorithm.stateIsOptimal(currentState)) {
                return;
            }
            int pivotCol = SimplexAlgorithm.findPivotColumnIndex(currentState);
            double[] improvementVec = SimplexAlgorithm.calculateImprovementVector(currentState, pivotCol);
            if (SimplexAlgorithm.modelIsUnbounded(improvementVec)) {
                return;
            }
            int pivotRow = SimplexAlgorithm.findPivotRowIndex(improvementVec);

            FillInBlanksQuestionModel q2 = new FillInBlanksQuestionModel("q2_" + iteration);
            String answer = String.format("%d,%d", pivotRow + 1, pivotCol + 1);
            q2.setPrompt(translator.translateMessage("q2prompt"));
            q2.addAnswer(answer, 1, translator.translateMessage("q_correct"));
            lang.addFIBQuestion(q2);
        }
    }

    public String[][] simplexStateToStringMatrix(SimplexState state) {
        int[] baseVariables = state.getBasicVariables();

        int rowCount = state.getRowCount() + 1;
        int columnCount = state.getColCount() + 2;

        // some indexes
        int fRow = rowCount - 1;
        int bColumn = columnCount - 2;
        int hintColumn = columnCount - 1;

        String[][] table = new String[rowCount][columnCount];

        // Top labels
        table[0][0] = "";
        for (int i = 1; i < bColumn; i++) {
            table[0][i] = Utils.getXVariable(i);
        }
        table[0][bColumn] = "b_i";

        // Left labels
        for (int i = 1; i < rowCount - 1; i++) {
            table[i][0] = Utils.getXVariable(baseVariables[i - 1]);
        }
        table[fRow][0] = "F";

        // Copy all data from simplex state
        for (int i = 0; i < state.getRowCount(); i++) {
            for (int j = 0; j < state.getColCount(); j++) {
                table[i + 1][j + 1] = formatDouble(state.getValueAt(i, j));
            }
        }

        // Clear hint column
        for (int i = 0; i < rowCount; i++) {
            table[i][hintColumn] = "";
        }

        return table;
    }

    public void updateStringMatrix(StringMatrix mat, String[][] content) {
        for (int i = 0; i < mat.getNrRows(); i++) {
            for (int j = 0; j < mat.getNrCols(); j++) {
                mat.put(i, j, content[i][j], null, null);
            }
        }
    }

    public String formatDouble(Double d) {
        // note: "+ 0.0" removes sign from negative zero
        return new DecimalFormat("#0.##", DecimalFormatSymbols.getInstance(locale)).format(d + 0.0);
    }

    public String formatDoubleArray(double[] dArr) {
        return String.format("(%s)", DoubleStream.of(dArr).mapToObj(this::formatDouble).collect(Collectors.joining(" ")));
    }

    public String getName() {
        return translator.translateMessage("name");
    }
    public String getAlgorithmName() {
        return translator.translateMessage("name");
    }
    public String getAnimationAuthor() {
        return "Andreas Bauer, Fabian Bauer";
    }
    public String getDescription(){ return String.join("\n", description); }
    public String getCodeExample(){
        return String.join("\n", sourceCode.values());
    }
    public String getFileExtension(){
        return "asu";
    }
    public Locale getContentLocale() {
        return locale;
    }
    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
    }
    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

    @Override
    public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) throws IllegalArgumentException {
        double[][] A = Utils.intMatrixToDoubleMatrix((int[][]) primitives.get("A"));
        double[] b = Utils.intArrayToDoubleArray((int[]) primitives.get("b"));
        double[] c = DoubleStream.of(Utils.intArrayToDoubleArray((int[]) primitives.get("c"))).map(d -> -d).toArray();
        if (!SimplexState.dimensionsAreValid(A, b, c)) {
            throw new IllegalArgumentException(translator.translateMessage("errDimensions"));
        }
        if (!SimplexState.isInCanonicalForm(A, b, c)) {
            throw new IllegalArgumentException(translator.translateMessage("errCanonical"));
        }
        return true;
    }
}