/*
 * TrialDivision.java
 * Sven Palberg, Timo Martin, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.hashing;

import static java.lang.Math.min;
import static java.lang.System.arraycopy;
import static java.util.Arrays.fill;

import java.awt.Color;
import java.awt.Font;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationProperties;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.items.FontPropertyItem;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.network.helper.ClassName;
import translator.Translator;

@SuppressWarnings("unused")
public class SHA3 implements ValidatingGenerator {
    private final Set<Primitive> keepThese = new HashSet<>();
    private final Coordinates UPPER_LEFT = new Coordinates(0, 0);
    private Language lang;

    // Translator
    private Translator translator;
    private String resourceName;
    private Locale locale;

    // Meta and Config
    private static final String GENERATOR_NAME = "SHA-3";
    private static final String AUTHORS = "Sven Palberg, Timo Martin";
    private static final int WINDOW_X = 1000;
    private static final int WINDOW_Y = 800;

    private final int absorbingPhaseArraySize = 16;
    private final int absorbingPhaseMaxIterations = 2;

    // UI (Elements)
    private Text headlineText;
    private Rect headlineRect;
    private Text stepTitleText;
    private Text explanationText;
    private Text elementName;
    private SourceCode pseudoCodeBlock;
    private SourceCode introductionTextBlock;
    private SourceCode epilogTextBlock;
    private Map<Integer, Text> logicInputTexts;
    private Map<Integer, Text> logicOutputTexts;
    private StringMatrix rhoAndPiConstantMatrix;
    private StringArray thetaCStringArray;
    private StringArray thetaDStringArray;
    private SourceCode uStateViz;
    private SourceCode uMessageViz;
    private List<Primitive> absorbingPhaseArrows;

    // UI (Props)
    private TextProperties headlineTextProps;
    private TextProperties stepTitleTextProps;
    private TextProperties elementNameProps;
    private TextProperties explanationTextProps;
    private RectProperties headlineRectProps;
    private SourceCodeProperties textBlockProps;
    private SourceCodeProperties codeBlockProps;
    private SourceCodeProperties explanationBlockProps;
    private ArrayProperties arrayProps;


    // Algorithm variables
    private String message;
    private String hash;
    private String mode;
    private int iterationToBeDisplayed;
    private StringMatrix stateMatrix;
    private BigInteger[][] state;
    private int[] uState;
    private int[] uMessage;
    private int blockSize;
    private int rateInBytes;
    private int inputOffset;

    private BigInteger[] thetaC;
    private BigInteger[] thetaD;

    private int rhoAndPiX;
    private int rhoAndPiY;
    private BigInteger rhoAndPiCurrent;

    // Meta flags
    private boolean showKeecakF = false;

    // Logic UI Settings
    private final int logicIOBoxWidth = 150;
    private final int logicIOColumnHeight = 300;

    // Code example
    private final List<Tuple<String, Integer>> CODE_EXAMPLE = Arrays.asList(
            new Tuple<>("// Absorbing phase", 0),
            new Tuple<>("while (inputOffset < uMessage.length) {", 0),
            new Tuple<>("blockSize = min(uMessage.length - inputOffset, rateInBytes);", 1),
            new Tuple<>("for (int i = 0; i < blockSize; i++) {", 1),
            new Tuple<>("uState[i] = uState[i] ^ uMessage[i + inputOffset];", 2),
            new Tuple<>("}", 1),
            new Tuple<>("", 0),
            new Tuple<>("inputOffset = inputOffset + blockSize;", 1),
            new Tuple<>("if (blockSize == rateInBytes) {", 1),
            new Tuple<>("doKeccakf(uState);", 2),
            new Tuple<>("blockSize = 0;", 2),
            new Tuple<>("}", 1),
            new Tuple<>("}", 0)
    );


    // The generator supports both german and english therefor the Constructer has to be called twice
    // First:  Generator generator = new SHA3(Locale.GERMAN);
    // Second: Generator generator = new SHA3(Locale.ENGLISH);
    public SHA3(Locale locale) {
        this.resourceName = "resources/SHA3";
        this.locale = locale;

        // Initialize translator
        this.translator = new Translator(this.resourceName, this.locale);
    }

    //<editor-fold desc="Generator Base Methods">
    @Override
    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
        // Get primitives
        this.message = (String) primitives.get("Message");
        this.mode = (String) primitives.get("Mode");
        this.iterationToBeDisplayed = (int) primitives.get("Displayed Iteration Index");

        // Get UI properties
        this.headlineTextProps = (TextProperties) props.getPropertiesByName("headlineTextProps");
        this.headlineRectProps = (RectProperties) props.getPropertiesByName("headlineRectProps");
        this.stepTitleTextProps = (TextProperties) props.getPropertiesByName("stepTitleTextProps");
        this.textBlockProps = (SourceCodeProperties) props.getPropertiesByName("textBlockProps");
        this.codeBlockProps = (SourceCodeProperties) props.getPropertiesByName("codeBlockProps");
        this.explanationBlockProps = (SourceCodeProperties) props.getPropertiesByName("explanationBlockProps");
        this.explanationTextProps = (TextProperties) props.getPropertiesByName("explanationTextProps");
        this.elementNameProps = (TextProperties) props.getPropertiesByName("uiElementNames");

        // Update props
        this.updateFontSize(this.headlineTextProps, 16);
        this.updateFontSize(this.stepTitleTextProps, 16);
        this.updateFontSize(this.explanationTextProps, 14);
        this.updateFontSize(this.codeBlockProps, 12);
        this.updateFontStyle(this.headlineTextProps, Font.BOLD);
        this.updateFontStyle(this.stepTitleTextProps, Font.BOLD);

        // Start the animation
        this.showHeadline();
        this.showIntroduction();

        Parameters parameters = this.getParametersByName(mode);
        byte[] messageBytes = this.convertToBytes(message);
        byte[] hashBytes = this.getHash(messageBytes, parameters);
        hash = convertBytesToString(hashBytes);
        // this.createStateMatrix();

        this.showEpilog();

        return this.lang.toString();
    }

    @Override
    public void init() {
        this.lang = new AnimalScript(GENERATOR_NAME, AUTHORS, WINDOW_X, WINDOW_Y);
        this.lang.setStepMode(true);

        this.state = new BigInteger[5][5];

        this.arrayProps = new ArrayProperties();
        this.arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        this.arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        this.arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
        this.arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, new Color(0xD00000));
        this.arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, new Color(0x009000));
        this.arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.GREEN);
    }

    @Override
    public String getName() {
        return GENERATOR_NAME;
    }

    @Override
    public String getAlgorithmName() {
        return GENERATOR_NAME;
    }

    @Override
    public String getAnimationAuthor() {
        return AUTHORS;
    }

    @Override
    public String getDescription() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.append(this.translator.translateMessage("INTRO" + i));
            sb.append(" ");
        }
        return sb.toString();
    }

    @Override
    public String getCodeExample() {
        return CODE_EXAMPLE.stream()
                .map(entry -> String.join("", Collections.nCopies(entry.y, "\t")) + entry.x)
                .reduce("", (code, line) -> code + line + "\n");
    }

    @Override
    public String getFileExtension() {
        return "asu";
    }

    @Override
    public Locale getContentLocale() {
        return this.locale;
    }

    @Override
    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_HASHING);
    }

    @Override
    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }

    @Override
    public boolean validateInput(AnimationPropertiesContainer animationPropertiesContainer, Hashtable<String, Object> hashtable) throws IllegalArgumentException {
        final String mode = (String) hashtable.get("Mode");

        Parameters params = this.getParametersByName(mode);

        if (params == null) {
            throw new IllegalArgumentException("Invalid mode (use one of the modes listed above)");
        }

        final int iterationIndex = (int) hashtable.get("Displayed Iteration Index");
        if (iterationIndex < 0 || iterationIndex > 23) {
            throw new IllegalArgumentException("Displayed Iteration Index must be between 0 and 23, inclusive.");
        }

        return true;
    }
    //</editor-fold>

    //<editor-fold desc="UI Sections">
    private void showHeadline() {
        this.headlineText = this.lang.newText(
                new Offset(30, 10, UPPER_LEFT, "NE"),
                GENERATOR_NAME,
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

        this.keepThese.add(this.headlineText);
        this.keepThese.add(this.headlineRect);
        this.keepThese.add(this.stepTitleText);
    }

    private void showIntroduction() {
		// this.hideAll();

        this.stepTitleText.setText(
                translator.translateMessage("TITLE0"),
                null,
                null
        );

        this.introductionTextBlock = this.lang.newSourceCode(
                new Offset(0, 20, this.headlineRect, "SW"),
                "introductionTextBlock",
                null,
                this.textBlockProps
        );

        for (int i = 0; i < 25; i++) {
            this.introductionTextBlock.addCodeLine(this.translator.translateMessage("INTRO" + i), null, 0, null);
        }

        this.nextSlide(translator.translateMessage("TITLE0"));
    }

    private void showEpilog() {
        this.hideAll();

        this.stepTitleText.setText(
                translator.translateMessage("TITLE2"),
                null,
                null
        );

        this.epilogTextBlock = this.lang.newSourceCode(
                new Offset(0, 20, this.headlineRect, "SW"),
                "epilogTextBlock",
                null,
                this.textBlockProps
        );

        for (int i = 0; i < 12; i++) {
            if (i == 3) {
                this.epilogTextBlock.addCodeLine(this.message, "message", 1, null);
                this.epilogTextBlock.highlight("message");
            } else if (i == 7) {
                this.epilogTextBlock.addCodeLine(this.hash, "hash", 1, null);
                this.epilogTextBlock.highlight("hash");
            } else if (i == 11) {
                this.epilogTextBlock.addCodeLine(this.mode, "mode", 1, null);
                this.epilogTextBlock.highlight("mode");
            } else {
                this.epilogTextBlock.addCodeLine(this.translator.translateMessage("OUTRO" + i), null, 0, null);
            }
        }

        this.nextSlide(translator.translateMessage("TITLE2"));
    }

    private final String uStateFormat = "uState[ %d ] = %s";
    private final String uMessageFormat = "uMessage[ %d ] = %s";

    private void createUStateViz() {
        if (this.uStateViz != null) this.uStateViz.hide();
        this.uStateViz = this.createStringArray(
                new Offset(0, 20, this.headlineRect, "SW"),
                "uState",
                this.convertIntArrayToHexStringArray(uState, (i, s) -> String.format(uStateFormat, i, s)),
                0,
                this.absorbingPhaseArraySize
        );
    }

    private void createUMessageViz(final int offset) {
        if (this.uMessageViz != null) this.uMessageViz.hide();
        this.uMessageViz = this.createStringArray(
                new Offset(280, 20, this.headlineRect, "SW"),
                "uMessage",
                this.convertIntArrayToHexStringArray(uMessage, (i, s) -> String.format(uMessageFormat, i, s)),
                offset,
                this.absorbingPhaseArraySize
        );
    }

    private void createAbsorbingPhaseArrows(final int count) {
        final int height = 16;
        final int offset = 2 * height;

        if(this.absorbingPhaseArrows != null && this.absorbingPhaseArrows.size() > 0) {
            this.absorbingPhaseArrows.forEach(Primitive::hide);
        }

        this.absorbingPhaseArrows = new ArrayList<>();
        for(int i = 0; i < count; i++) {
            this.absorbingPhaseArrows.add(
                this.createArrow(
                    new Offset(-20, 9 + offset + i * height, this.uMessageViz, "NW"),
                    new Offset(20, 9 + offset + i * height, this.uStateViz, "NE")
                )
            );
        }
    }
    

    private void showAbsorbingPhase() {
        this.hideAll();

        this.createUStateViz();
        this.createUMessageViz(0);

        SourceCode explanation = this.lang.newSourceCode(new Offset(30, 20, this.uMessageViz, "NE"), null, null, this.explanationBlockProps);
        explanation.addCodeLine(translator.translateMessage("EXPLANATIONS11") + " " + this.rateInBytes + " " + translator.translateMessage("EXPLANATIONS12"), null, 0, null);
        explanation.addCodeLine(translator.translateMessage("EXPLANATIONS13"), null, 0, null);
        explanation.addCodeLine(translator.translateMessage("EXPLANATIONS14"), null, 0, null);
        explanation.addCodeLine(translator.translateMessage("EXPLANATIONS15"), null, 0, null);

        SourceCode sc = this.lang.newSourceCode(new Offset(0, 10, this.uStateViz, "SW"), null, null, this.codeBlockProps);
        sc.addCodeLine("while (inputOffset < uMessage.length) {", null, 0, null);
        sc.addCodeLine("  blockSize = min(uMessage.length - inputOffset, rateInBytes);", null, 1, null);
        sc.addCodeLine("  for (int i = 0; i < blockSize; i++) {", null, 1, null);
        sc.addCodeLine("    uState[i] = uState[i] ^ uMessage[i + inputOffset];", null, 2, null);
        sc.addCodeLine("  }", null, 1, null);
        sc.addCodeLine("", null, 0, null);
        sc.addCodeLine("  inputOffset = inputOffset + blockSize;", null, 1, null);
        sc.addCodeLine("  if (blockSize == rateInBytes) {", null, 1, null);
        sc.addCodeLine("    doKeccakf(uState);", null, 2, null);
        sc.addCodeLine("    blockSize = 0;", null, 2, null);
        sc.addCodeLine("  }", null, 1, null);
        sc.addCodeLine("}", null, 0, null);

        sc.highlight(2);
        sc.highlight(3);
        sc.highlight(4);

        this.stepTitleText.setText("Absorbing Phase", null, null);
        this.nextSlide("Absorbing Phase");
    }

    private void showKeccakF() {
        if (!this.showKeecakF) return;
        this.hideAll();

        this.stepTitleText.setText("Keccak-F Phase", null, null);

        this.createStateMatrix(new Offset(0, 40, this.headlineRect, "SW"));

        final SourceCode sc = this.lang.newSourceCode(new Offset(0, 20, this.stateMatrix, "SW"), null, null, this.codeBlockProps);
        sc.addCodeLine("BigInteger[][] lState = new BigInteger[5][5];", null, 0, null);
        sc.addCodeLine("for (int i = 0; i < 5; i++) {", null, 0, null);
        sc.addCodeLine("    for (int j = 0; j < 5; j++) {", null, 1, null);
        sc.addCodeLine("        int[] data = new int[8];", null, 2, null);
        sc.addCodeLine("        arraycopy(uState, 8 * (i + 5 * j), data, 0, data.length);", null, 2, null);
        sc.addCodeLine("        lState[i][j] = convertFromLittleEndianTo64(data);", null, 2, null);
        sc.addCodeLine("    }", null, 1, null);
        sc.addCodeLine("}", null, 0, null);
        sc.highlight(4);
        sc.highlight(5);

        SourceCode explanation = this.lang.newSourceCode(new Offset(0, 20, sc, "SW"), null, null, this.explanationBlockProps);
        for(int i = 1; i <= 6; i++) {
            explanation.addCodeLine(translator.translateMessage("KF_EXPLANATION_" + i), null, 0, null);
        }

        this.keepThese.add(this.stateMatrix);
        this.keepThese.add(this.elementName);

        this.nextSlide("Keccak-F Phase");
    }

	private void showThetaStep1() {
        if (!this.showKeecakF) return;
        this.hideAll();
		String theta = "θ";
		System.setProperty("file.encoding", "UTF-8");
		String title = "θ-" + translator.translateMessage("TITLE1");
        this.stepTitleText.setText(title, null, null);
        this.updateStateMatrix();

        final Text t1 = this.lang.newText(
                new Offset(0, 40, this.stateMatrix, "SW"),
                translator.translateMessage("EXPLANATIONS0"),
                null, null, explanationTextProps
        );
        this.thetaCStringArray = this.lang.newStringArray(
                new Offset(0, 20, t1, "SW"),
                this.convertBigIntegerArrayToStringArray(this.thetaC),
                null, null, this.arrayProps
        );

        this.nextSlide(title);
    }

	private void showThetaStep2() {
        if (!this.showKeecakF) return;

        final Text t2 = this.lang.newText(
                new Offset(0, 40, this.thetaCStringArray, "SW"),
                translator.translateMessage("EXPLANATIONS1"),
                null, null, explanationTextProps
        );
        this.thetaDStringArray = this.lang.newStringArray(
                new Offset(0, 20, t2, "SW"),
                this.convertBigIntegerArrayToStringArray(this.thetaD),
                null, null, this.arrayProps
        );
    }

	private void showThetaStep3() {
        if (!this.showKeecakF) return;

        final Text t3 = this.lang.newText(
                new Offset(0, 40, this.thetaDStringArray, "SW"),
                translator.translateMessage("EXPLANATIONS2"),
                null, null, explanationTextProps
        );
    }

	private void showRhoAndPiSteps() {
        if (!this.showKeecakF) return;
        this.hideAll();

		String title = "ρ- " + translator.translateMessage("TITLE3") + " Pi-" + translator.translateMessage("TITLE1");
        this.stepTitleText.setText(title, null, null);
        this.updateStateMatrix();

        final SourceCode sc = this.lang.newSourceCode(new Offset(0, 20, this.stateMatrix, "SW"), null, null, this.codeBlockProps);
        sc.addCodeLine("int x = 1, y = 0;", null, 0, null);
        sc.addCodeLine("BigInteger current = state[x][y];", null, 0, null);
        sc.addCodeLine("for (int i = 0; i < 24; i++) {", null, 0, null);
        sc.addCodeLine("  int tX = x;", null, 1, null);
        sc.addCodeLine("  x = y;", null, 1, null);
        sc.addCodeLine("  y = (2 * tX + 3 * y) % 5;", null, 1, null);
        sc.addCodeLine("", null, 0, null);
        sc.addCodeLine("  BigInteger shiftValue = current;", null, 1, null);
        sc.addCodeLine("  current = state[x][y];", null, 1, null);
        sc.addCodeLine("", null, 0, null);
        sc.addCodeLine("  state[x][y] = leftRotate64(shiftValue, (i + 1) * (i + 2) / 2);", null, 1, null);
        sc.addCodeLine("}", null, 0, null);

        final String[][] constants = new String[][]{
                new String[]{"", "x = 3", "x = 4", "x = 0", "x = 1", "x = 2"},
                new String[]{"y = 2", "153", "231", "3", "10", "171"},
                new String[]{"y = 1", "55", "276", "36", "300", "6"},
                new String[]{"y = 0", "28", "91", "0", "1", "190"},
                new String[]{"y = 4", "120", "78", "210", "66", "253"},
                new String[]{"y = 3", "21", "136", "105", "45", "15"}
        };

        final MatrixProperties mp = new MatrixProperties();
        mp.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
        mp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GRAY);
        this.rhoAndPiConstantMatrix = this.lang.newStringMatrix(new Offset(40, 0, sc, "NE"), constants, null, null, mp);
        this.pseudoCodeBlock = sc;

        this.explanationText = this.lang.newText(new Offset(0, -20, rhoAndPiConstantMatrix, "NW"), translator.translateMessage("EXPLANATIONS3"), null, null, elementNameProps);

        this.explanationText = this.lang.newText(new Offset(0, 20, sc, "SW"), translator.translateMessage("EXPLANATIONS4"), null, null, this.explanationTextProps);
        this.explanationText = this.lang.newText(new Offset(0, 40, sc, "SW"), translator.translateMessage("EXPLANATIONS5"), null, null, this.explanationTextProps);

        this.nextSlide(title);
    }

	private void tickRhoAndPiSteps(final int line) {
        if (!this.showKeecakF) return;

        this.pseudoCodeBlock.highlight(line);
        this.stateMatrix.highlightCell(this.rhoAndPiX, this.rhoAndPiY, null, null);
        int matrixY = ((this.rhoAndPiX + 2) % 5) + 1;
        int matrixX = ((7 - this.rhoAndPiY) % 5) + 1;
        this.rhoAndPiConstantMatrix.highlightCell(matrixX, matrixY, null, null);
        this.updateStateMatrix();

        this.lang.nextStep();
        this.pseudoCodeBlock.unhighlight(line);
        this.stateMatrix.unhighlightCell(this.rhoAndPiX, this.rhoAndPiY, null, null);
        this.rhoAndPiConstantMatrix.unhighlightCell(matrixX, matrixY, null, null);
    }

    private void showχStep() {
        if (!this.showKeecakF) return;
        this.hideAll();

        String title = "χ-" + translator.translateMessage("TITLE1");
        this.stepTitleText.setText(title, null, null);
        this.updateStateMatrix();

        final int offsetX = 20;
        final int offsetY = 230;

        this.logicInputTexts = new HashMap<>();
        this.logicOutputTexts = new HashMap<>();

        for (int i = 0; i < 5; i++) {
            this.createLogicColumn(new Coordinates(i * this.logicIOBoxWidth + offsetX, offsetY), i);
        }
        SourceCode sc = this.lang.newSourceCode(
                new Coordinates(offsetX, offsetY + this.logicIOColumnHeight + 40),
                null,
                null,
                this.explanationBlockProps
        );

        for (int i = 6; i < 11; i++) {
            sc.addCodeLine(translator.translateMessage("EXPLANATIONS" + i), null, 0, null);
        }

        this.nextSlide(title);
    }

    private void showιStep() {
        if (!this.showKeecakF) return;
        this.hideAll();

        String title = "ι-" + translator.translateMessage("TITLE1");
        this.stepTitleText.setText(title, null, null);
        this.updateStateMatrix();
        this.stateMatrix.highlightCell(0,0, null, null);

        String[] roundConstants = new String[]{
                "0x0000000000000001", "0x0000000000008082",
                "0x800000000000808A", "0x8000000080008000",
                "0x000000000000808B", "0x0000000080000001",
                "0x8000000080008081", "0x8000000000008009",
                "0x000000000000008A", "0x0000000000000088",
                "0x0000000080008009", "0x000000008000000A",
                "0x000000008000808B", "0x800000000000008B",
                "0x8000000000008089", "0x8000000000008003",
                "0x8000000000008002", "0x8000000000000080",
                "0x000000000000800A", "0x800000008000000A",
                "0x8000000080008081", "0x8000000000008080",
                "0x0000000080000001", "0x8000000080008008"
        };

        String[] roundConstantsArray = new String[roundConstants.length];
        for(int i = 0; i < roundConstants.length; i++) {
            roundConstantsArray[i] = String.format("rc[ %d ] = %s", i, roundConstants[i]);
        }

        SourceCode rcSc = this.createStringArray(
                new Offset(0, 30, this.stateMatrix, "SW"),
                this.translator.translateMessage("IOTA_ROUND_CONSTANT_VARIABLE_NAME"),
                roundConstantsArray,
                0,
                24
        );
        rcSc.highlight(2 + this.iterationToBeDisplayed);

        SourceCode eb = this.lang.newSourceCode(
                new Offset(20, 0, rcSc, "NE"),
                null,
                null,
                this.explanationBlockProps
        );
        eb.addCodeLine("", null, 0, null);

        for (int i = 1; i <= 4; i++) {
            eb.addCodeLine(translator.translateMessage("IOTA_EXPLANATION_" + i), null, 0, null);
        }

        this.nextSlide(title);

    }
    //</editor-fold>

    //<editor-fold desc="Algorithm">
    private static BigInteger BIT_64 = new BigInteger("18446744073709551615");

    private byte[] convertToBytes(String message) {
        return this.message.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Do hash.
     *
     * @param message   input data
     * @param parameter keccak param
     * @return byte-array result
     */
    private byte[] getHash(final byte[] message, final Parameters parameter) {
        this.uState = new int[200];
        this.uMessage = convertToUint(message);

        rateInBytes = parameter.getRate() / 8;
        blockSize = 0;
        inputOffset = 0;

        // Absorbing phase
        this.showAbsorbingPhase();
        while (inputOffset < uMessage.length) {
            blockSize = min(uMessage.length - inputOffset, rateInBytes);
            for (int i = 0; i < blockSize; i++) {
                uState[i] = uState[i] ^ uMessage[i + inputOffset];
            }

            this.createUStateViz();
            this.createUMessageViz(inputOffset);
            this.createAbsorbingPhaseArrows(Math.min(this.absorbingPhaseArraySize, blockSize));
            this.lang.nextStep();

            inputOffset = inputOffset + blockSize;
            if (blockSize == rateInBytes) {
                doKeccakf(uState);
                blockSize = 0;
            }
        }

        // Padding phase
        uState[blockSize] = uState[blockSize] ^ parameter.getD();

        // This block is never executed with the given parameters
        if ((parameter.getD() & 0x80) != 0 && blockSize == (rateInBytes - 1)) {
            doKeccakf(uState);
        }

        uState[rateInBytes - 1] = uState[rateInBytes - 1] ^ 0x80;

        this.showKeecakF = true;
        doKeccakf(uState);
        this.showKeecakF = false;

        // Squeezing phase
        ByteArrayOutputStream byteResults = new ByteArrayOutputStream();
        int tOutputLen = parameter.getOutputLen() / 8;
        while (tOutputLen > 0) {
            blockSize = min(tOutputLen, rateInBytes);
            for (int i = 0; i < blockSize; i++) {
                byteResults.write((byte) uState[i]);
            }

            tOutputLen -= blockSize;
            if (tOutputLen > 0) {
                doKeccakf(uState);
            }
        }

        return byteResults.toByteArray();
    }

    private void doKeccakf(final int[] uState) {
        this.showKeccakF();

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                int[] data = new int[8];
                arraycopy(uState, 8 * (i + 5 * j), data, 0, data.length);
                state[i][j] = convertFromLittleEndianTo64(data);
            }
        }
        if (showKeecakF) {
            updateStateMatrix();
            lang.nextStep();
        }
        roundB(state);

        fill(uState, 0);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                int[] data = convertFrom64ToLittleEndian(state[i][j]);
                arraycopy(data, 0, uState, 8 * (i + 5 * j), data.length);
            }
        }
    }

    /**
     * Permutation on the given state.
     *
     * @param state state
     */
    private void roundB(final BigInteger[][] state) {
        int LFSRstate = 1;

        boolean showKeecakFWasTrue1 = this.showKeecakF;
        for (int round = 0; round < 24; round++) {
            // Show only first iteration
            this.showKeecakF = showKeecakFWasTrue1 && round == this.iterationToBeDisplayed;

            thetaC = new BigInteger[5];
            thetaD = new BigInteger[5];

            // θ step
			this.showThetaStep1();
            for (int i = 0; i < 5; i++) {
                thetaC[i] = state[i][0].xor(state[i][1]).xor(state[i][2]).xor(state[i][3]).xor(state[i][4]);
                if (this.showKeecakF) {
                    this.thetaCStringArray.put(i, this.toHex(thetaC[i]), null, null);
                    this.thetaCStringArray.highlightCell(i, null, null);
                    this.stateMatrix.highlightCell(i, 0, null, null);
                    this.stateMatrix.highlightCell(i, 1, null, null);
                    this.stateMatrix.highlightCell(i, 2, null, null);
                    this.stateMatrix.highlightCell(i, 3, null, null);
                    this.stateMatrix.highlightCell(i, 4, null, null);
                    this.lang.nextStep();
                    this.thetaCStringArray.unhighlightCell(i, null, null);
                    this.stateMatrix.unhighlightCell(i, 0, null, null);
                    this.stateMatrix.unhighlightCell(i, 1, null, null);
                    this.stateMatrix.unhighlightCell(i, 2, null, null);
                    this.stateMatrix.unhighlightCell(i, 3, null, null);
                    this.stateMatrix.unhighlightCell(i, 4, null, null);
                }
            }

			this.showThetaStep2();
            for (int i = 0; i < 5; i++) {
                thetaD[i] = thetaC[(i + 4) % 5].xor(leftRotate64(thetaC[(i + 1) % 5], 1));
                if (this.showKeecakF) {
                    this.thetaDStringArray.put(i, this.toHex(thetaD[i]), null, null);
                    this.thetaDStringArray.highlightCell(i, null, null);
                    this.thetaCStringArray.highlightCell((i + 4) % 5, null, null);
                    this.thetaCStringArray.highlightCell((i + 1) % 5, null, null);
                    this.lang.nextStep();
                    this.thetaDStringArray.unhighlightCell(i, null, null);
                    this.thetaCStringArray.unhighlightCell((i + 4) % 5, null, null);
                    this.thetaCStringArray.unhighlightCell((i + 1) % 5, null, null);
                }
            }

			this.showThetaStep3();
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    state[i][j] = state[i][j].xor(thetaD[i]);
                }
                if (this.showKeecakF) {
                    this.updateStateMatrix();
                    this.thetaDStringArray.highlightCell(i, null, null);
                    this.stateMatrix.highlightCell(i, 0, null, null);
                    this.stateMatrix.highlightCell(i, 1, null, null);
                    this.stateMatrix.highlightCell(i, 2, null, null);
                    this.stateMatrix.highlightCell(i, 3, null, null);
                    this.stateMatrix.highlightCell(i, 4, null, null);
                    this.lang.nextStep();
                    this.thetaDStringArray.unhighlightCell(i, null, null);
                    this.stateMatrix.unhighlightCell(i, 0, null, null);
                    this.stateMatrix.unhighlightCell(i, 1, null, null);
                    this.stateMatrix.unhighlightCell(i, 2, null, null);
                    this.stateMatrix.unhighlightCell(i, 3, null, null);
                    this.stateMatrix.unhighlightCell(i, 4, null, null);
                }
            }

            //ρ and π steps
			this.showRhoAndPiSteps();
            rhoAndPiX = 1;
            rhoAndPiY = 0;
            rhoAndPiCurrent = null;
			this.tickRhoAndPiSteps(0);
            rhoAndPiCurrent = state[rhoAndPiX][rhoAndPiY];
			this.tickRhoAndPiSteps(1);
            boolean showKeecakFWasTrue2 = this.showKeecakF;
            for (int i = 0; i < 24; i++) {
                // ONLY SHOW FIRST AND LAST ITERATION
                this.showKeecakF = showKeecakFWasTrue2 && (i == 0 || i == 23);

				this.tickRhoAndPiSteps(2);
                int tX = rhoAndPiX;
				this.tickRhoAndPiSteps(3);
                rhoAndPiX = rhoAndPiY;
				this.tickRhoAndPiSteps(4);
                rhoAndPiY = (2 * tX + 3 * rhoAndPiY) % 5;
				this.tickRhoAndPiSteps(5);
                BigInteger shiftValue = rhoAndPiCurrent;
				this.tickRhoAndPiSteps(7);
                rhoAndPiCurrent = state[rhoAndPiX][rhoAndPiY];
				this.tickRhoAndPiSteps(8);
                state[rhoAndPiX][rhoAndPiY] = leftRotate64(shiftValue, (i + 1) * (i + 2) / 2);
				this.tickRhoAndPiSteps(10);
            }

            //χ step
            this.showχStep();
            for (int j = 0; j < 5; j++) {
                BigInteger[] t = new BigInteger[5];
                for (int i = 0; i < 5; i++) {
                    t[i] = state[i][j];
                    if (this.showKeecakF) {
                        this.stateMatrix.highlightCell(i, j, null, null);
                        this.updateLogicIOValue(i, true, state[i][j]);
                    }
                }
                if (this.showKeecakF) this.lang.nextStep();

                for (int i = 0; i < 5; i++) {
                    // ~t[(i + 1) % 5]
                    BigInteger invertVal = t[(i + 1) % 5].xor(BIT_64);
                    // t[i] ^ ((~t[(i + 1) % 5]) & t[(i + 2) % 5])
                    state[i][j] = t[i].xor(invertVal.and(t[(i + 2) % 5]));
                    if (this.showKeecakF) this.updateLogicIOValue(i, false, state[i][j]);
                }
                if (this.showKeecakF) {
                    this.lang.nextStep();
                    this.updateStateMatrix();
                    for (int i = 0; i < 5; i++) {
                        this.stateMatrix.unhighlightCell(i, j, null, null);
                        this.updateLogicIOValue(i, false, null);
                    }
                }
            }

            //ι step
            if (this.showKeecakF) this.showιStep();
            for (int i = 0; i < 7; i++) {
                LFSRstate = ((LFSRstate << 1) ^ ((LFSRstate >> 7) * 0x71)) % 256;
                // pow(2, i) - 1
                int bitPosition = (1 << i) - 1;
                if ((LFSRstate & 2) != 0) {
                    BigInteger rc = new BigInteger("1").shiftLeft(bitPosition);
                    state[0][0] = state[0][0].xor(rc);
                }
            }
            if (this.showKeecakF) {
                this.updateStateMatrix();
                this.lang.nextStep();
            }

        }
        this.keepThese.remove(this.stateMatrix);
        this.keepThese.remove(this.elementName);
    }

    //</editor-fold>

    //<editor-fold desc="Helper Methods">
    private Parameters getParametersByName(final String modeName) {

        switch (modeName) {
            case "SHA3-224":
                return Parameters.SHA3_224;
            case "SHA3-256":
                return Parameters.SHA3_256;
            case "SHA3-384":
                return Parameters.SHA3_384;
            case "SHA3-512":
                return Parameters.SHA3_512;
        }

        return null;
    }

    private String leftPad(final String str, final char pad, final int n) {
        final String p = IntStream.range(0, n).mapToObj(i -> Character.toString(pad)).collect(Collectors.joining(""));
        return p.substring(str.length()) + str;
    }

    private String toHex(String s, int l) {
        return "0x" + this.leftPad(s.toUpperCase(), '0', l);
    }

    private String toHex(int i) {
        return this.toHex(Integer.toHexString(i), 4);
    }

    private String toHex(BigInteger b) {
        return this.toHex(b.toString(16), 16);
    }

    private String[] convertIntArrayToHexStringArray(int[] data, BiFunction<Integer, String, String> fn) {
        List<String> arr = new ArrayList<>(data.length);
        for (int i = 0; i < data.length; i++) {
            String hex = this.toHex(data[i]);
            arr.add(fn.apply(i, hex));
        }
        return arr.toArray(new String[0]);
    }

    private String[][] convertState() {
        Function<BigInteger, String> fn = this::toHex;

        return Stream
                .of(this.state)
                .map(values -> Stream.of(values).map(
                        value -> value == null ? this.toHex("", 16) : fn.apply(value)
                ).toArray(String[]::new))
                .toArray(String[][]::new);
    }

    private String[] convertBigIntegerArrayToStringArray(BigInteger[] arr) {
        return Stream.of(arr).map(b -> b == null ? String.join("", Collections.nCopies(18, " ")) : this.toHex(b)).toArray(String[]::new);
    }

    private void nextStep(String title) {
        this.lang.nextStep(title);
    }

    private void hideAll() {
        this.lang.hideAllPrimitivesExcept(new ArrayList<>(this.keepThese));
        if (stateMatrix != null && !this.keepThese.contains(stateMatrix)) this.stateMatrix.hide();
    }

    private void nextSlide(String title) {
        this.nextStep(title);
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
    //</editor-fold>

    //<editor-fold desc="UI Helper Methods">
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
        } else {
            font = (Font) f;
        }
        return font;
    }

    private SourceCode createStringArray(final Node position, final String title, final String[] data, final int offset, final int maxLength) {
        SourceCode sc = this.lang.newSourceCode(position, null, null, this.codeBlockProps);
        sc.addCodeLine((title != null ? title : ""), null, 0, null);
        sc.addCodeLine("", null, 0, null);

        for (int i = offset; i < Math.min(data.length, offset + maxLength); i++) {
            String s = data[i];
            sc.addCodeLine((s != null ? s : ""), null, 0, null);
        }

        if (data.length - offset > maxLength) {
            sc.addCodeLine(" ... ", null, 0, null);
        }

        return sc;
    }

    private void createStateMatrix(final Node pos) {
        MatrixProperties matrixProps = new MatrixProperties();
        matrixProps.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
        matrixProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        this.updateFontSize(matrixProps, 12);

        String[][] stateData = this.convertState();

        this.stateMatrix = this.lang.newStringMatrix(
                pos,
                stateData,
                "matrix",
                null,
                matrixProps
        );
        this.elementName = this.lang.newText(new Offset(0, -20, stateMatrix, "NW"), translator.translateMessage("EXPLANATIONS"), null, null, elementNameProps);

    }

    private void updateStateMatrix() {
        if (this.stateMatrix == null) return;

        String[][] stateData = this.convertState();
        for (int x = 0; x < stateData.length; x++) {
            for (int y = 0; y < stateData[x].length; y++) {
                this.stateMatrix.put(x, y, stateData[x][y], null, null);
            }
        }
    }

    private void updateLogicIOValue(final int index, final boolean input, final BigInteger b) {
        final Map<Integer, Text> texts = input ? this.logicInputTexts : this.logicOutputTexts;
        final Text text = texts.get(index);

        if (b == null) {
            text.setText("", null, null);
        } else {
            text.setText(this.toHex(b), null, null);
        }
    }

    private void createLogicColumn(final Node position, final int index) {
        final int heightBlock = this.logicIOColumnHeight / 10;
        final int lane = this.logicIOBoxWidth / 4;
        final int gateSize = lane;

        final Primitive input = this.createLogicIOBox(position, this.logicIOBoxWidth, true, 0, index);
        final Primitive output = this.createLogicIOBox(position, this.logicIOBoxWidth, false, this.logicIOColumnHeight, index);

        final Primitive notGate = this.createLogicGate(position, gateSize, 2 * heightBlock, (int) (2.5 * lane), "NOT");
        final Primitive andGate = this.createLogicGate(position, gateSize, 4 * heightBlock, (int) (2.5 * lane), "AND");
        final Primitive xorGate = this.createLogicGate(position, gateSize, 8 * heightBlock, (int) (1.5 * lane), "XOR");

        final PolylineProperties pp = new PolylineProperties();
        pp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);

        final Polyline wireInputOut = this.lang.newPolyline(
                new Node[]{
                        new Offset(0, 0, input, "S"),
                        new Offset(0, heightBlock, input, "S")
                },
                null,
                null,
                pp
        );
        final Polyline wireInputOutToNot = this.lang.newPolyline(
                new Node[]{
                        new Offset(0, heightBlock, input, "S"),
                        new Offset(lane, heightBlock, input, "S"),
                        new Offset(lane, 2 * heightBlock, input, "S")
                },
                null,
                null,
                pp
        );
        final Polyline wireInputOutToXor = this.lang.newPolyline(
                new Node[]{
                        new Offset(0, heightBlock, input, "S"),
                        new Offset(-lane, heightBlock, input, "S"),
                        new Offset(-lane, 6 * heightBlock, input, "S"),
                        new Offset(gateSize / -4, 6 * heightBlock, input, "S"),
                        new Offset(gateSize / -4, 8 * heightBlock, input, "S")
                },
                null,
                null,
                pp
        );
        final Polyline wireNotToAnd = this.lang.newPolyline(
                new Node[]{
                        new Offset(lane, 2 * heightBlock, input, "S"),
                        new Offset(lane, 3 * heightBlock, input, "S"),
                        new Offset(lane - gateSize / 4, 3 * heightBlock, input, "S"),
                        new Offset(lane - gateSize / 4, 4 * heightBlock, input, "S"),
                },
                null,
                null,
                pp
        );
        final Polyline wireXorToOutput = this.lang.newPolyline(
                new Node[]{
                        new Offset(0, 8 * heightBlock, input, "S"),
                        new Offset(0, 10 * heightBlock, input, "S")
                },
                null,
                null,
                pp
        );

        if (index == 0) {
            return;
        }

        final Polyline crossWireInputOutLeftAnd = this.lang.newPolyline(
                new Node[]{
                        new Offset(-lane, heightBlock, input, "S"),
                        new Offset(-lane, 3 * heightBlock, input, "S"),
                        new Offset(-this.logicIOBoxWidth + lane + gateSize / 4, 3 * heightBlock, input, "S"),
                        new Offset(-this.logicIOBoxWidth + lane + gateSize / 4, 4 * heightBlock, input, "S")
                },
                null,
                null,
                pp
        );
        final Polyline crossWireAndToLeftXor = this.lang.newPolyline(
                new Node[]{
                        new Offset(lane, 4 * heightBlock, input, "S"),
                        new Offset(lane, 5 * heightBlock, input, "S"),
                        new Offset(-this.logicIOBoxWidth + gateSize / 4, 6 * heightBlock, input, "S"),
                        new Offset(-this.logicIOBoxWidth + gateSize / 4, 8 * heightBlock, input, "S")
                },
                null,
                null,
                pp
        );
    }

    private Primitive createLogicIOBox(final Node position, final int width, final boolean labelOnTop, final int offsetY, final int index) {
        final RectProperties rp = new RectProperties();
        rp.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(135, 206, 255));
        rp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        rp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
        final TextProperties tp = new TextProperties();
        tp.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
        tp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

        // Input Box
        final Node upperLeft = new Offset(0, offsetY, position, "SE");
        final Node lowerRight = new Offset(width, offsetY + 20, position, "SE");
        final Primitive rect = this.lang.newRect(upperLeft, lowerRight, null, null, rp);
        this.lang.newText(new Offset(0, labelOnTop ? -15 : 5, rect, labelOnTop ? "N" : "S"), labelOnTop ? "INPUT" : "OUTPUT", null, null, tp);
        Text text = this.lang.newText(new Offset(5, 2, rect, "NW"), "", null, null, tp);

        if (labelOnTop) this.logicInputTexts.put(index, text);
        else this.logicOutputTexts.put(index, text);

        return rect;
    }

    private Primitive createLogicGate(final Node position, final int size, final int offsetY, final int offsetX, final String label) {
        final RectProperties rp = new RectProperties();
        rp.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(249, 166, 2));
        rp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        rp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
        final TextProperties tp = new TextProperties();
        tp.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
        this.updateFontSize(tp, 10);
        tp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

        final Node upperLeft = new Offset(offsetX, offsetY, position, "SE");
        final Node lowerRight = new Offset(offsetX + size, offsetY + size, position, "SE");
        final Primitive rect = this.lang.newRect(upperLeft, lowerRight, null, null, rp);
        this.lang.newText(new Offset(0, 8, rect, "N"), label, null, null, tp);

        return rect;
    }

    private Primitive createArrow(Node from, Node to) {
        PolylineProperties pp = new PolylineProperties();
        pp.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);

        Polyline arrow = this.lang.newPolyline(
                new Node[]{from, to},
                null,
                null,
                pp
        );
        return arrow;
    }
    //</editor-fold>

    //<editor-fold desc="Mode-Parameter Enum">
    public enum Parameters {
        KECCAK_224(1152, 0x01, 224),
        KECCAK_256(1088, 0x01, 256),
        KECCAK_384(832, 0x01, 384),
        KECCAK_512(576, 0x01, 512),

        SHA3_224(1152, 0x06, 224),
        SHA3_256(1088, 0x06, 256),
        SHA3_384(832, 0x06, 384),
        SHA3_512(576, 0x06, 512),

        SHAKE128(1344, 0x1F, 256),
        SHAKE256(1088, 0x1F, 512);

        private final int rate;

        /**
         * Delimited suffix.
         */
        public final int d;

        /**
         * Output length (bits).
         */
        public final int outputLen;

        Parameters(int rate, int d, int outputLen) {
            this.rate = rate;
            this.d = d;
            this.outputLen = outputLen;
        }

        public int getRate() {
            return rate;
        }

        public int getD() {
            return d;
        }

        public int getOutputLen() {
            return outputLen;
        }
    }
    //</editor-fold>

    //<editor-fold desc="HexUtils">
    private static final byte[] ENCODE_BYTE_TABLE = {
            (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7',
            (byte) '8', (byte) '9', (byte) 'a', (byte) 'b', (byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f'
    };

    /**
     * Convert byte array to unsigned array.
     *
     * @param data byte array
     * @return unsigned array
     */
    private static int[] convertToUint(final byte[] data) {
        int[] converted = new int[data.length];
        for (int i = 0; i < data.length; i++) {
            converted[i] = data[i] & 0xFF;
        }

        return converted;
    }

    /**
     * Convert LE to 64-bit value (unsigned long).
     *
     * @param data data
     * @return 64-bit value (unsigned long)
     */
    private static BigInteger convertFromLittleEndianTo64(final int[] data) {
        BigInteger uLong = new BigInteger("0");
        for (int i = 0; i < 8; i++) {
            uLong = uLong.add(new BigInteger(Integer.toString(data[i])).shiftLeft(8 * i));
        }

        return uLong;
    }

    /**
     * Convert 64-bit (unsigned long) value to LE.
     *
     * @param uLong 64-bit value (unsigned long)
     * @return LE
     */
    private static int[] convertFrom64ToLittleEndian(final BigInteger uLong) {
        int[] data = new int[8];
        BigInteger mod256 = new BigInteger("256");
        for (int i = 0; i < 8; i++) {
            data[i] = uLong.shiftRight((8 * i)).mod(mod256).intValue();
        }

        return data;
    }

    /**
     * Bitwise rotate left.
     *
     * @param value  unsigned long value
     * @param rotate rotate left
     * @return result
     */
    private static BigInteger leftRotate64(final BigInteger value, final int rotate) {
        BigInteger lp = value.shiftRight(64 - (rotate % 64));
        BigInteger rp = value.shiftLeft(rotate % 64);

        return lp.add(rp).mod(new BigInteger("18446744073709551616"));
    }

    /**
     * Convert bytes to string.
     *
     * @param data bytes array
     * @return string
     */
    private static String convertBytesToString(final byte[] data) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        for (int i = 0; i < data.length; i++) {
            int uVal = data[i] & 0xFF;

            buffer.write(ENCODE_BYTE_TABLE[(uVal >>> 4)]);
            buffer.write(ENCODE_BYTE_TABLE[uVal & 0xF]);
        }

        return new String(buffer.toByteArray());
    }
    //</editor-fold>
}
