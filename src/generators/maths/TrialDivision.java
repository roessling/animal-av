package generators.maths;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.*;
import algoanim.primitives.generators.Language;
import algoanim.properties.*;
import algoanim.properties.items.FontPropertyItem;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import animal.variables.VariableRoles;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import translator.Translator;

import java.awt.*;
import java.util.*;
import java.util.Locale;
import java.util.List;
import java.util.stream.Collectors;

public class TrialDivision implements ValidatingGenerator {
    private Language lang;
    private Variables variables;

    // Translator
    private Translator translator;
    private String resourceName;
    private Locale locale;

    // UI (Elements)
    private Text headlineText;
    private Rect headlineRect;
    private Text stepTitleText;
    private SourceCode pseudoCodeBlock;
    private SourceCode introductionTextBlock;
    private SourceCode epilogTextBlock;
    private Text explanationText;
    private StringMatrix matrix;

    // UI (Props)
    private TextProperties headlineTextProps;
    private RectProperties headlineRectProps;
    private TextProperties stepTitleTextProps;
    private SourceCodeProperties textBlockProps;
    private SourceCodeProperties codeBlockProps;
    private TextProperties explanationTextProps;

    // Algorithm variables
    private List<Integer> p;
    private int f;
    private int n;
    private int nOld;

    // Meta data
    private String[][] matrixData;
    private final Coordinates UPPER_LEFT = new Coordinates(0, 0);

    // PseudoCode
    private final List<Tuple<String, Integer>> PSEUDO_CODE = Arrays.asList(
            new Tuple<>("def trial_division(n):", 0),
            new Tuple<>("primeFactors = [];", 1),
            new Tuple<>("factor = 2;", 1),
            new Tuple<>("while n > 1:", 1),
            new Tuple<>("if (n % factor == 0):", 2),
            new Tuple<>("primeFactors.append(factor);", 3),
            new Tuple<>("n /= factor;", 3),
            new Tuple<>("else:", 2),
            new Tuple<>("factor += 1;", 3),
            new Tuple<>("return primeFactors;", 1)
    );

    // PseudoCode step explanation
    @SuppressWarnings("serial")
    private Map<Integer, String> EXPLANATIONS = new HashMap<Integer, String>(){};

    // The generator supports both german and english therefor the Constructer has to be called twice
    // First:  Generator generator = new TrialDivision(Locale.GERMAN);
    // Second: Generator generator = new TrialDivision(Locale.ENGLISH);
    public TrialDivision(Locale locale) {
        this.resourceName = "resources/TrialDivision";
        this.locale = locale;

        // Initialize translator
        this.translator = new Translator(this.resourceName, this.locale);
    }

    public void init() {
        // Initialize AnimalScript
        this.lang = new AnimalScript("Trial Division", "Sven Palberg, Timo Martin", 800, 600);
        this.lang.setStepMode(true);

        // Initialize variables
        this.p = new ArrayList<>();
        this.f = 2;
        this.n = 0;

        // Initialize Matrix
        this.matrixData = new String[][]{
                {"primeFactors", "[]"},
                {"factor", String.valueOf(f)},
                {"n", String.valueOf(n)}
        };

        this.variables = this.lang.newVariables();
        this.variables.declare("string", "p", "[]", VariableRoles.GATHERER.toString());
        //this.variables.setRole("p", translator.translateMessage("VAR_ROLE_p"));
        this.variables.declare("int", "f", "" + this.f, VariableRoles.STEPPER.toString());
        //this.variables.setRole("f", translator.translateMessage("VAR_ROLE_f"));
        this.variables.declare("int", "n", "" + this.n, VariableRoles.STEPPER.toString());
        //this.variables.setRole("n", translator.translateMessage("VAR_ROLE_n"));
    }

    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
        this.n = (Integer) primitives.get("n");
        this.nOld = n;

        // Get UI properties
        this.headlineTextProps = (TextProperties) props.getPropertiesByName("headlineTextProps");
        this.headlineRectProps = (RectProperties) props.getPropertiesByName("headlineRectProps");
        this.stepTitleTextProps = (TextProperties) props.getPropertiesByName("stepTitleTextProps");
        this.textBlockProps = (SourceCodeProperties) props.getPropertiesByName("textBlockProps");
        this.codeBlockProps = (SourceCodeProperties) props.getPropertiesByName("codeBlockProps");
        this.explanationTextProps = (TextProperties) props.getPropertiesByName("explanationTextProps");

        // Update props
        this.updateFontSize(this.headlineTextProps, 16);
        this.updateFontSize(this.stepTitleTextProps, 16);
        this.updateFontSize(this.explanationTextProps, 14);
        this.updateFontStyle(this.headlineTextProps, Font.BOLD);
        this.updateFontStyle(this.stepTitleTextProps, Font.BOLD);

        // Start the animation
        this.showHeadline();
        this.showIntroduction();
        this.showAlgorithm();
        this.showEpilog();

        return this.lang.toString();
    }

    // ==START== METHODS

    private void showHeadline() {
        this.headlineText = this.lang.newText(
                new Offset(30, 10, UPPER_LEFT, "NE"),
                "Trial Division",
                "headlineText",
                null,
                this.headlineTextProps
        );

        this.headlineRect = this.lang.newRect(
                new Offset(-10, 0, this.headlineText, "NW"),
                new Offset(10, 0, this.headlineText, "SE"),
                "headlineRect",
                null,
                this.headlineRectProps
        );

        this.stepTitleText = this.lang.newText(
                new Offset(30, 0, this.headlineRect, "NE"),
                "",
                "stepTitleText",
                null,
                this.stepTitleTextProps
        );
    }

    private void showIntroduction() {
        this.stepTitleText.setText(translator.translateMessage("STEP0"), null, null);

        this.introductionTextBlock = this.lang.newSourceCode(
                new Offset(0, 20, this.headlineRect, "SW"),
                "introductionTextBlock",
                null,
                this.textBlockProps
        );

        for(int i = 0; i < 7; i++){
            if( i <= 5) {
                this.introductionTextBlock.addCodeLine(this.translator.translateMessage("INTRO" + i), null, 0, null);
            }
            else {
                this.introductionTextBlock.addCodeLine(this.translator.translateMessage("INTRO" + i) + " " + this.n + ".", null, 0, null);

            }
        }

        this.lang.nextStep("Introduction");
        introductionTextBlock.hide();
    }

    private void showAlgorithm() {
        this.stepTitleText.setText(translator.translateMessage("STEP1") + " " + this.nOld, null, null);

        this.pseudoCodeBlock = this.lang.newSourceCode(
                new Offset(0, 20, this.headlineRect, "SW"),
                "pseudoCodeBlock",
                null,
                this.codeBlockProps
        );
        this.explanationText = this.lang.newText(
                new Offset(250, 25, headlineRect, "SW"),
                "",
                "explanationText",
                null,
                this.explanationTextProps
        );
        MatrixProperties matrixProps = new MatrixProperties();
        matrixProps.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
        matrixProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        this.matrix = this.lang.newStringMatrix(
                new Offset(0, 10, explanationText, "SW"),
                this.matrixData,
                "matrix",
                null,
                matrixProps
        );
        updateMatrix();

        // Add the pseudoCode
        for (Tuple<String, Integer> line : PSEUDO_CODE) {
            this.pseudoCodeBlock.addCodeLine(line.x, null, line.y, null);
        }

        // Add pseudoCode explanations
        for(int i = 0; i < 10; i++){
            this.EXPLANATIONS.put(i, translator.translateMessage("EXPLANATIONS" + i));
        }

        this.lang.nextStep("Algorithm");
        this.trialDivision();
        this.pseudoCodeBlock.hide();
        this.explanationText.hide();
        this.matrix.hide();
    }

    private void showEpilog() {
        this.stepTitleText.setText(
                this.nOld + " = " + p.stream().map(String::valueOf).collect(Collectors.joining(" * ")),
                null,
                null
        );

        this.epilogTextBlock = this.lang.newSourceCode(
                new Offset(0, 20, this.headlineRect, "SW"),
                "epilogTextBlock",
                null,
                this.textBlockProps
        );

        for(int i = 0; i < 9; i++){
            if(i == 2) {
                this.epilogTextBlock.addCodeLine("" + this.nOld, "n", 1, null);
                this.epilogTextBlock.highlight("n");
            }
            else if(i == 4){
                this.epilogTextBlock.addCodeLine("" + p.stream().map(String::valueOf).collect(Collectors.joining(", ")), "factors", 1, null);
                this.epilogTextBlock.highlight("factors");
            }
            else {
                this.epilogTextBlock.addCodeLine(this.translator.translateMessage("OUTRO" + i), null, 0, null);
            }
        }

        this.lang.nextStep("Conclusion");
    }

    // ===END=== METHODS

    public String getName() {
        return "Trial Division";
    }

    public String getAlgorithmName() {
        return "Trial Division ";
    }

    public String getAnimationAuthor() {
        return "Sven Palberg, Timo Martin";
    }

    public String getDescription() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 5; i++) {
            sb.append(this.translator.translateMessage("INTRO" + i));
            sb.append(" ");
        }
        return sb.toString();
    }

    public String getCodeExample() {
        return PSEUDO_CODE.stream()
                .map(entry -> String.join("", Collections.nCopies(entry.y, "\t")) + entry.x)
                .reduce("", (code, line) -> code + line + "\n");
    }


    public String getFileExtension() {
        return "asu";
    }

    public Locale getContentLocale() {
        return this.locale;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

    @Override
    public boolean validateInput(AnimationPropertiesContainer animationPropertiesContainer, Hashtable<String, Object> hashtable)
            throws IllegalArgumentException {
        int n = (Integer) hashtable.get("n");
        return n > 1;
    }

    // Small tuple implementation
    private class Tuple<X, Y> {
        final X x;
        final Y y;

        Tuple(X x, Y y) {
            this.x = x;
            this.y = y;
        }
    }

    private void trialDivision() {
        nextStep(0);
        this.p = new ArrayList<>();
        nextStep(1);
        this.f = 2;
        nextStep(2);
        while (this.n > 1) {
            nextStep(3);
            if (this.n % this.f == 0) {
                nextStep(4);
                this.p.add(this.f);
                nextStep(5);
                this.n /= this.f;
                nextStep(6);
            } else {
                nextStep(7);
                this.f += 1;
                nextStep(8);
            }
        }
        nextStep(9);
    }

    private void nextStep(int lineNo) {
        this.updateMatrix();
        this.explanationText.setText(this.EXPLANATIONS.get(lineNo), null, null);
        this.pseudoCodeBlock.highlight(lineNo);
        this.lang.nextStep();
        this.pseudoCodeBlock.unhighlight(lineNo);
    }

    private void updateMatrix() {
        String pString = "[" + this.p.stream().map(String::valueOf).collect(Collectors.joining(", ")) + "]";
        String fString = Integer.toString(f);
        String nString = Integer.toString(n);

        if (!pString.equals(this.matrixData[0][1])) {
            this.matrix.put(0, 1, pString, null, null);
            highlightMatrixRow(0);
        } else {
            unHighlightMatrixRow(0);
        }

        if (!fString.equals(this.matrixData[1][1])) {
            this.matrix.put(1, 1, fString, null, null);
            highlightMatrixRow(1);
        } else {
            unHighlightMatrixRow(1);
        }

        if (!nString.equals(this.matrixData[2][1])) {
            this.matrix.put(2, 1, nString, null, null);
            highlightMatrixRow(2);
        } else {
            unHighlightMatrixRow(2);
        }

        this.matrixData = new String[][]{
                {"p", pString},
                {"f", fString},
                {"n", nString}
        };

        this.variables.set("p", pString);
        this.variables.set("f", fString);
        this.variables.set("n", nString);
    }

    private void highlightMatrixRow(int row) {
        this.matrix.highlightCell(row, 0, null, null);
        this.matrix.highlightCell(row, 1, null, null);
    }

    private void unHighlightMatrixRow(int row) {
        this.matrix.unhighlightCell(row, 0, null, null);
        this.matrix.unhighlightCell(row, 1, null, null);
    }

    private void updateFontSize(AnimationProperties props, int size) {
        Font font = getFont(props);
        props.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(font.getName(), font.getStyle(), size));
    }

    private void updateFontStyle(AnimationProperties props, int style) {
        Font font = getFont(props);
        props.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(font.getName(), style, font.getSize()));
    }

    private Font getFont(AnimationProperties props) {
        Object f = props.get(AnimationPropertiesKeys.FONT_PROPERTY);
        Font font;
        if (f instanceof FontPropertyItem) {
            font = (Font) ((FontPropertyItem) f).get();
        }
        else {
            font = (Font) f;
        }
        return font;
    }
}
