/*
 * smith_waterman.java
 * Felix Mannl, Tim Hoffman, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;

import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.addons.InfoBox;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;
import animal.main.Animal;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;

public class Smith_waterman implements ValidatingGenerator {
	private Language lang;
	private int gapCost;
	private int matchCost;
	private int mismatchCost;
	private SourceCodeProperties sourceCodeProperties;
	private MatrixProperties matrixProperties;
	private String s1;
	private String s2;

	// ========================================== Algo Daten ==================================================
	private int lengthInputString1;
	private int lengthInputString2;

	private Cell[][] scoringMatrixAlgorithm;
	private String[][] scoringMatrixOnlyString;
	private LinkedList<ArrowIndexPair> scoringArrows;
	private LinkedList<ArrowIndexPair> tracebackArrows;
	private List<Position> startingCells = new ArrayList<Position>();
	private List<Alignment> optimalAlignments = new LinkedList<Alignment>();
	// ==========================================================================================================

	// =========================================== Animal Daten =================================================
	private static final String HEADLINE = "Smith-Waterman: local string alignment";
	private final static String HEADLINE_INTRO = "Smith-Waterman: overview";
	private final static String HEADLINE_OUTRO = "Smith-Waterman: conclusion";
	private final static String STEPS1 = "Initializing";
	private final static String STEPS2 = "Scoring";
	private final static String STEPS3 = "Traceback";

	private final String Hint0 = "Generic hints are shown here.";
	private final String Hint1 = "The general steps of this algorithm are shown up there.";
	private final String Hint2 = "The source code can be found to the left.";
	private final String Hint25 = "The input is shown under the source code.";
	private final String Hint3 = "Now we start the algorithm by initializing the scoring matrix.";
	private final String Hint41 = "The following arrows indicate a possible score for the respective cell.";
	private final String Hint42 = "The exact calculation of the score is shown in the source code.";
	private final String Hint51 = "The arrow with the highest score is picked and will be saved for later use.";
	private final String Hint52 = "If no score is positive, 0 will be used instead.";
	private final String Hint53 = "Skip to the traceback phase if you understand.";
	private final String Hint61 = "Now that the full matrix is calculated, the traceback phase begins.";
	private final String Hint62 = "Notice the changing source code.";
	private final String Hint7 = "The matrix is copied for better visuals.";
	private final String Hint8 = "First an emtpy stack, which will hold the optimal aligment, is created.";
	private final String Hint91 = "Then the cell with the highest score is found.";
	private final String Hint92 = "It's indices are noted.";
	private final String Hint101 = "Now the arrows are followed reversely until a cell with a score of 0 is reached.";
	private final String Hint102 = "The passed characters are pushed onto the stack.";
	private final String Hint111 = "Once the final cell is reached, the stack is popped (reversed)";
	private final String Hint112 = "and the optimal aligment is found.";

	public final static Timing defaultDuration = new TicksTiming(30);

	// intro page
	private InfoBox introDescriptionPage;
	private LinkedList<String> introDescriptionStrings1;
	private LinkedList<String> introDescriptionStrings2;

	// outro Page
	private InfoBox outroDescriptionPage;
	private LinkedList<String> outroDescriptionSrings;

	// headline
	private TextProperties headlineProps;
	private Text headline;

	// input data display
	private InfoBox inputDataDisplay;
	private LinkedList<String> inputDataStrings = new LinkedList<String>();

	// steps and arrows between them
	private TextProperties steps1Props;
	private Text steps1;
	private TextProperties steps2Props;
	private Text steps2;
	private TextProperties steps3Props;
	private Text steps3;
	private Arrow stepsArrow1;
	private Arrow stepsArrow2;

	// source code background
	private SourceCode srcPart1;
	private SourceCode srcPart2;

	private RectProperties srcBackground;
	private Rect srcBackgroundRect;

	// matrix and background
	private StringMatrix scoringMatrixAnimation;
	private StringMatrix tracebackMatrixAnimation;

	// text field for hints and notes
	private RectProperties tippsAndTricksBackgroundProps;
	private Rect tippsAndTricksBackgroundRect;
	private Text tippsAndTricks;
	private LinkedList<Text> tippsFields;

	// "stack"
	private RectProperties stackBackground;
	private Rect stackBackgroundRect;
	private TextProperties outputStringProps;
	private Text outputString1Display;
	private Text outputString2Display;
	private Text outputString1ReverseDisplay;
	private Text outputString2ReverseDisplay;
	// =========================================================================================================

	public void init() {
		lang = new AnimalScript("Smith-Waterman", "Felix Mannl, Tim Hoffman", 800, 600);
		lang.setStepMode(true);
	}

	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		String s1 = (String) primitives.get("s1");
		if (s1.length() > 15)
			return false;

		String s2 = (String) primitives.get("s2");
		if (s2.length() > 15)
			return false;
		return true;
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		gapCost = (Integer) primitives.get("gapCost");
		matchCost = (Integer) primitives.get("matchCost");
		sourceCodeProperties = (SourceCodeProperties) props.getPropertiesByName("sourceCodeProperties");
		matrixProperties = (MatrixProperties) props.getPropertiesByName("matrixProperties");
		mismatchCost = (Integer) primitives.get("mismatchCost");
		this.s1 = (String) primitives.get("s1");
		this.s2 = (String) primitives.get("s2");
		// ==============================================================================================================
		setupAnimalData();
		doStuff();
		// ==============================================================================================================
		return lang.toString();
	}

	public static void main(String[] args) {
		Generator generator = new Smith_waterman();
		Animal.startGeneratorWindow(generator);
	}

	public String getName() {
		return "Smith-Waterman";
	}

	public String getAlgorithmName() {
		return "Smith-Waterman: local string alignment";
	}

	public String getAnimationAuthor() {
		return "Felix Mannl, Tim Hoffman";
	}

	public String getDescription(){
        return "The Smith-Waterman algorithm is used to find the optimal local alignment for two strings."
        		+"\n"
				+ "This means similar substrings are determined by optimizing a similarity measure."
        		+ "\n"
				+ "The similarity measure describes the cost of actions to convert one string into the other."
        		+ "\n"
				+ "The algorithm is usually applied in bioinformatics to analyse sequences of DNA or proteins."
        		+ "\n"
				+ "\n"
        		+ "Please do not use unreasonable long input strings and inappropriate costs."
        		+ "\n"
				+ "Long input strings will not explain the algorithm better and they only add more steps to the animation."
				+ "\n"
				+ "High negative or positive scores will make it harder to read the matrices.";
    }

	public String getCodeExample() {
		return "First phase: scoring" + "\n"
				+ "Given: String s1 with length m , String s2 with length n, gapCost, matchCost, mismatchCost" + "\n"
				+ "\n" + "    initialize matrix M with 0 in first line and column" + "\n" + "    for i=1 to m" + "\n"
				+ "            for j=1 to n" + "\n" + "                    deletionScore = M[i][j-1] + gapCost" + "\n"
				+ "                    insertionScore = M[i-1][j] + gapCost" + "\n"
				+ "                    char1 = s1[j-1]" + "\n" + "                    char2 = s2[i-1]" + "\n"
				+ "                    if( char1 == char2 )" + "\n"
				+ "                        substitutionScore = M[i-1][j-1] + matchCost" + "\n"
				+ "                    else" + "\n"
				+ "                        substitutionScore = M[i-1][j-1] + mismatchCost" + "\n"
				+ "                    M[i][j] = max(deletionScore, insertionScore, substitutionScore, 0)" + "\n"
				+ "        return M" + "\n" + "\n" + "\n" + "\n" + "Second phase: traceback" + "\n"
				+ "Given: Matrix M, String s1 with length m , String s2 with length n" + "\n" + "\n"
				+ "    optimalAlignment = optimal alignment, initially empty" + "\n"
				+ "    stack = keep track of cells in the alignment, initially empty" + "\n" + "\n"
				+ "       cell = cell with maximum score" + "\n" + "       i,j = coordinates of cell" + "\n" + "\n"
				+ "        while( cell.score > 0 )" + "\n" + "            if( cell.score is a substitution score )"
				+ "\n" + "                char1 = s1[j-1]" + "\n" + "                char2 = s2[i-1]" + "\n"
				+ "                stack.push( {char1,char2} )" + "\n" + "                i = i-1" + "\n"
				+ "                j = j-1" + "\n" + "            else if( cell.score is an insertion score )" + "\n"
				+ "                char1 = GAP" + "\n" + "                char2 = s2[i-1]" + "\n"
				+ "                stack.push( {char1,char2} )" + "\n" + "                i = i-1" + "\n"
				+ "            else if( cell.score is a deletion score )" + "\n" + "                char1 = s1[j-1]"
				+ "\n" + "                char2 = GAP" + "\n" + "                stack.push( {char1,char2} )" + "\n"
				+ "                j = j-1" + "\n" + "        for( bases : stack )" + "\n"
				+ "            a1 = a1 + bases[0]" + "\n" + "            a2 = a2 + bases[1]" + "\n"
				+ "        optimalAlignment.add( {a1,a2} )" + "\n" + "    return optimalAlignment" + "\n";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.ENGLISH;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	// ==================================================custom methods
	// ===============================================================
	void setupAnimalData() {
		lengthInputString1 = s1.length();
		lengthInputString2 = s2.length();

		scoringMatrixAlgorithm = new Cell[lengthInputString1 + 1][lengthInputString2 + 1];
		scoringMatrixOnlyString = new String[lengthInputString1 + 3][lengthInputString2 + 2];

		tippsFields = new LinkedList<Text>();

		// headline propertie, the same everywhere
		headlineProps = new TextProperties();
		headlineProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 22));

		// intro page
		introDescriptionPage = new InfoBox(lang, new Coordinates(10, 20), 500, "");
		introDescriptionStrings1 = new LinkedList<String>();
		introDescriptionStrings1.add("");
		introDescriptionStrings1.add("");
		introDescriptionStrings1
				.add("The Smith-Waterman algorithm is used to find the optimal local alignment for two strings.");
		introDescriptionStrings1
				.add("This means similar substrings are determined, by optimizing a similarity measure.");
		introDescriptionStrings1
				.add("The similarity measure describes the cost of actions to convert one string into the other.");
		introDescriptionStrings1.add("It's given by a gap cost which represents needed insertions and deletions ");
		introDescriptionStrings1.add(
				"and substitution costs, which describe the cost for replacing a character with the same (match) or with a different (mismatch) character.");
		introDescriptionStrings1
				.add("The algorithm is usually applied in bioinformatics to analyse sequences of DNA or proteins.");
		introDescriptionStrings1.add("");
		introDescriptionStrings1.add("");
		introDescriptionStrings1.add("Alignment example:");
		introDescriptionStrings1.add("CC-A");
		introDescriptionStrings1.add("CGT-");
		introDescriptionStrings1.add("");
		introDescriptionStrings1.add(
				"In this alignment we would need the following actions to convert the first string into the second:");
		introDescriptionStrings1.add("C  C: Substitution with same character (match)");
		introDescriptionStrings1.add("C  G: Substitution with character G (mismatch)");
		introDescriptionStrings1.add("-   G: Insertion of character G (gap)");
		introDescriptionStrings1.add("A   -: Deletion of character A (gap)");
		introDescriptionStrings2 = new LinkedList<String>();
		introDescriptionStrings2.add("");
		introDescriptionStrings2.add("");
		introDescriptionStrings2.add("Step overview");
		introDescriptionStrings2.add("Step 1: Initialize scoring matrix with 0 in first row and column.");
		introDescriptionStrings2
				.add("Step 2: Calculate all scores by means of the gap cost and the substitution costs.");
		introDescriptionStrings2
				.add("Step 3: Traceback starting at the highest score and ending when first 0 is encountered.");
		introDescriptionStrings2.add(
				"              Save the corresponding characters of passed cells to obtain the optimal local string alignment.");
		introDescriptionStrings2.add("");
		introDescriptionStrings2.add("");
		introDescriptionStrings2
				.add("Hint: You should go in fullscreen mode, to see all elements of the following animation.");
		introDescriptionPage.hide();

		// outro page
		outroDescriptionPage = new InfoBox(lang, new Coordinates(10, 20), 500, "");
		outroDescriptionSrings = new LinkedList<String>();
		outroDescriptionSrings.add("");
		outroDescriptionSrings.add("");
		outroDescriptionSrings.add("The Smith-Waterman algorithm is based on the Needleman-Wunsch algorithm,");
		outroDescriptionSrings.add("although the Needleman-Wunsch algorithm is used to find the global string alignment.");
		outroDescriptionSrings.add("That means it aligns the full strings instead of substrings as the local alignment does.");
		outroDescriptionSrings.add("");
		outroDescriptionSrings.add("Lastly we compare the complexity of these methods.");
		outroDescriptionSrings.add("The performance and space complexity for two Strings with length m and n are shown below:");
		outroDescriptionSrings.add("[with n from the shorter string]");
		outroDescriptionSrings.add("");
		outroDescriptionSrings.add("                                               Performance        Space");
		outroDescriptionSrings.add("Smith-Waterman:               O(m^2*n)                O(m*n)");
		outroDescriptionSrings.add("with optimizations:             O(m*n)                    O(n)");
		outroDescriptionSrings.add("Needleman-Wunsch:        O(m*n)                    O(m*n)");
		outroDescriptionSrings.add("");
		outroDescriptionSrings.add("Because of the comparatively bad performance the default Smith-Waterman algorithm is not often used,");
		outroDescriptionSrings.add("especially for larger problems.");
		outroDescriptionPage.hide();

		// headline
		headline = lang.newText(new Coordinates(10, 10), HEADLINE, null, null, headlineProps);
		headline.hide();

		// steps
		steps1Props = new TextProperties();
		steps1 = lang.newText(new Offset(0, 25, headline, "NW"), STEPS1, "steps1", null, steps1Props);
		steps1.hide();
		steps2Props = new TextProperties();
		steps2 = lang.newText(new Offset(30, 0, steps1, "NE"), STEPS2, "steps2", null, steps2Props);
		steps2.hide();
		steps3Props = new TextProperties();
		steps3 = lang.newText(new Offset(30, 0, steps2, "NE"), STEPS3, "steps3", null, steps3Props);
		steps3.hide();

		stepsArrow1 = new Arrow(lang, new Coordinates(70, 43), ArrowDirection.W);
		stepsArrow1.hide();
		stepsArrow2 = new Arrow(lang, new Coordinates(145, 43), ArrowDirection.W);
		stepsArrow2.hide();

		// code background
		srcBackground = new RectProperties();
		srcBackground.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.LIGHT_GRAY);
		srcBackground.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
		srcBackground.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		srcBackgroundRect = lang.newRect(new Offset(0, 30, steps1, "NW"), new Offset(430, 520, steps1, "NW"),
				"srcBackgroundRect", null, srcBackground);
		srcBackgroundRect.hide();

		// source code
		sourceCodeProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));

		// source code part 1
		srcPart1 = lang.newSourceCode(new Offset(10, 0, srcBackgroundRect, "NW"), "sourceCode", null,
				sourceCodeProperties);
		srcPart1.addCodeLine("Smith-Waterman: Scoring", null, 0, null);
		srcPart1.addCodeLine("Given: String s1 with length m , String s2 with length n", null, 1, null);
		srcPart1.addCodeLine("Given: gapCost, matchCost, mismatchCost", null, 1, null);
		srcPart1.addCodeLine("", null, 0, null);
		srcPart1.addCodeLine("initialize matrix M with 0 in first line and column", null, 1, null);
		srcPart1.addCodeLine("for i = 1 to m", null, 1, null);
		srcPart1.addCodeLine("for j = 1 to n", null, 2, null);
		srcPart1.addCodeLine("deletionScore = M[i][j-1] + gapCost", null, 3, null);
		srcPart1.addCodeLine("insertionScore = M[i-1][j] + gapCost", null, 3, null);
		srcPart1.addCodeLine("", null, 0, null);
		srcPart1.addCodeLine("char1 = s1[j-1]", null, 3, null);
		srcPart1.addCodeLine("char2 = s2[i-1]", null, 3, null);
		srcPart1.addCodeLine("if( char1 == char2 )", null, 3, null);
		srcPart1.addCodeLine("substitutionScore = M[i-1][j-1] + matchCost", null, 4, null);
		srcPart1.addCodeLine("else", null, 3, null);
		srcPart1.addCodeLine("substitutionScore = M[i-1][j-1] + mismatchCost", null, 4, null);
		srcPart1.addCodeLine("", null, 0, null);
		srcPart1.addCodeLine("M[i][j] = max(deletionScore, insertionScore,", null, 3, null);
		srcPart1.addCodeLine("substitutionScore, 0)", null, 11, null);
		srcPart1.addCodeLine("return M", null, 1, null);
		srcPart1.hide();
		// sc.addCodeLine(, null, 2, null);

		// source code part 2
		srcPart2 = lang.newSourceCode(new Offset(10, 0, srcBackgroundRect, "NW"), "sourceCode", null,
				sourceCodeProperties);
		srcPart2.addCodeLine("Smith-Waterman: Traceback", null, 0, null);
		srcPart2.addCodeLine("Given: String s1 with length m , String s2 with length n", null, 0, null);
		srcPart2.addCodeLine("", null, 0, null);
		srcPart2.addCodeLine("stack = saves chars for alignment, initially empty", null, 0, null);
		srcPart2.addCodeLine("cell = current cell, initially cell with maximum score", null, 0, null);
		srcPart2.addCodeLine("i,j = coordinates of cell", null, 0, null);
		srcPart2.addCodeLine("", null, 0, null);
		srcPart2.addCodeLine("while( cell.score > 0 )", null, 0, null);
		srcPart2.addCodeLine("if( cell.score is a substitution score )", null, 1, null);
		srcPart2.addCodeLine("char1 = s1[j-1]", null, 2, null);
		srcPart2.addCodeLine("char2 = s2[i-1]", null, 2, null);
		srcPart2.addCodeLine("stack.push( {char1,char2} )", null, 2, null);
		srcPart2.addCodeLine("i = i-1", null, 2, null);
		srcPart2.addCodeLine("j = j-1", null, 2, null);
		srcPart2.addCodeLine("", null, 0, null);
		srcPart2.addCodeLine("else if( cell.score is an insertion score )", null, 1, null);
		srcPart2.addCodeLine("char1 = GAP", null, 2, null);
		srcPart2.addCodeLine("char2 = s2[i-1]", null, 2, null);
		srcPart2.addCodeLine("stack.push( {char1,char2} )", null, 2, null);
		srcPart2.addCodeLine("i = i-1", null, 2, null);
		srcPart2.addCodeLine("", null, 0, null);
		srcPart2.addCodeLine("else if( cell.score is a deletion score )", null, 1, null);
		srcPart2.addCodeLine("char1 = s1[j-1]", null, 2, null);
		srcPart2.addCodeLine("char2 = GAP", null, 2, null);
		srcPart2.addCodeLine("stack.push( {char1,char2} )", null, 2, null);
		srcPart2.addCodeLine("j = j-1", null, 2, null);
		srcPart2.addCodeLine("", null, 0, null);
		srcPart2.addCodeLine("optimalAlignment = pop chars from stack", null, 0, null);
		srcPart2.addCodeLine("return optimalAlignment", null, 0, null);
		srcPart2.hide();

		// scoring matrix
		initializeScoringStringMatrix();
		matrixProperties.set(AnimationPropertiesKeys.GRID_ALIGN_PROPERTY, "center");
		matrixProperties.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "plain");
		scoringMatrixAnimation = lang.newStringMatrix(new Offset(50, 0, srcBackgroundRect, "NE"),
				scoringMatrixOnlyString, null, null, matrixProperties);
		scoringMatrixAnimation.hide();

		// tipps area
		tippsAndTricksBackgroundProps = new RectProperties();
		tippsAndTricksBackgroundProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.LIGHT_GRAY);
		tippsAndTricksBackgroundProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
		tippsAndTricksBackgroundProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		tippsAndTricksBackgroundRect = lang.newRect(new Offset(0, 40, scoringMatrixAnimation, "SW"),
				new Offset(440, 110, scoringMatrixAnimation, "SW"), null, null, tippsAndTricksBackgroundProps); // new
																												// Coordinates(500,
																												// 510)
																												// -
																												// (940,
																												// 590)
		tippsAndTricksBackgroundRect.hide();

		tippsAndTricks = lang.newText(new Offset(0, 0, tippsAndTricksBackgroundRect, "NW"), "", null, null); // (500,
																												// 500)
		tippsAndTricks.hide();

		// input data
		inputDataDisplay = new InfoBox(lang, new Offset(10, 15, srcBackgroundRect, "SW"), 100, "Inputs:");
		inputDataDisplay.hide();
	}

	void doStuff() {
		animateIntro();

		steps1.changeColor(null, Color.RED, null, null);
		animateInitialize();

		buildScoringMatrix();

		steps2.changeColor(null, Color.BLACK, null, null);
		steps3.changeColor(null, Color.RED, null, null);
		srcPart1.hide();
		srcPart2.show();
		findOptimalAlignment();

		animateOutro();
	}

	void animateIntro() {
		headline.show();
		headline.setText(HEADLINE_INTRO, null, null);
		introDescriptionPage.setText(introDescriptionStrings1);
		introDescriptionPage.show();
		lang.nextStep("Introduction");
		introDescriptionPage.setText(introDescriptionStrings2);
		lang.nextStep();
		introDescriptionPage.hide();
	}

	void animateOutro() {
		lang.nextStep();
		headline.setText(HEADLINE_OUTRO, null, null);
		// hide anything else...
		steps1.hide();
		stepsArrow1.hide();
		steps2.hide();
		stepsArrow2.hide();
		steps3.hide();
		srcPart2.hide();
		srcBackgroundRect.hide();
		inputDataDisplay.hide();
		scoringMatrixAnimation.hide();
		tippsAndTricks.hide();
		tippsAndTricksBackgroundRect.hide();
		tracebackMatrixAnimation.hide();
		for (ArrowIndexPair pair : scoringArrows)
			pair.getArrow().hide();
		for (ArrowIndexPair pair : tracebackArrows)
			pair.getArrow().hide();
		stackBackgroundRect.hide();
		outputString1ReverseDisplay.hide();
		outputString2ReverseDisplay.hide();
		clearTipps();
		// and show the conclusion
		outroDescriptionPage.setText(outroDescriptionSrings);
		outroDescriptionPage.show();
		lang.nextStep("Conclusion");
	}

	void animateInitialize() {
		headline.setText(HEADLINE, null, null);
		headline.show();

		lang.nextStep();
		tippsAndTricksBackgroundRect.show();
		tippsAndTricks.show();
		setTipps(Hint0);

		lang.nextStep("Initialize");
		setTipps(Hint1);
		lang.nextStep();

		steps1.show();
		stepsArrow1.show();
		steps2.show();
		stepsArrow2.show();
		steps3.show();

		lang.nextStep();
		setTipps(Hint2);
		lang.nextStep();
		srcBackgroundRect.show();
		srcPart1.show();
		lang.nextStep();

		setTipps(Hint25);
		lang.nextStep();

		inputDataDisplay.show();
		inputDataStrings.clear();
		inputDataStrings.add("s1: " + s1 + ", length: " + s1.length());
		inputDataStrings.add("s2: " + s2 + ", length: " + s2.length());
		inputDataStrings.add("gapCost: " + gapCost);
		inputDataStrings.add("matchCost: " + this.matchCost);
		inputDataStrings.add("mismatchCost: " + this.mismatchCost);
		inputDataDisplay.setText(inputDataStrings);
		srcPart1.highlight(1);
		srcPart1.highlight(2);
		lang.nextStep();
		srcPart1.unhighlight(1);
		srcPart1.unhighlight(2);
	}

	void buildScoringMatrix() {
		initializeCellMatrix();
		scoringMatrixAnimation.show();

		setTipps(Hint3);
		srcPart1.highlight(4);
		lang.nextStep("Scoring");

		steps1.changeColor(null, Color.BLACK, null, null);
		steps2.changeColor(null, Color.RED, null, null);
		srcPart1.unhighlight(4);
		setTipps(Hint41, Hint42);
		lang.nextStep();
		setTipps(Hint51, Hint52, Hint53);
		lang.nextStep();

		// store arrows for easier editing
		int[] scores = new int[3];
		TextArrow[] arrows = new TextArrow[3];
		scoringArrows = new LinkedList<ArrowIndexPair>();
		int highestScoreArrow = -1;

		// coordinates and move distance of the arrows
		Coordinates arrowCoordinates;
		int verticalDistance = 29;
		int horizontalDistance = 27;

		// score cells in matrix
		for (int i = 1; i <= lengthInputString1; i++) {
			scoringMatrixAnimation.highlightElem(i + 1, 0, null, null);

			for (int j = 1; j <= lengthInputString2; j++) {
				scoringMatrixAnimation.highlightElem(0, j + 1, null, null);

				// initialization: max is 0
				int max = 0;
				int score = 0;

				// first comparison: west cell (deletion)
				score = scoringMatrixAlgorithm[i][j - 1].score + gapCost;
				scores[0] = score;
				if (score > max) {
					max = score;
					scoringMatrixAlgorithm[i][j].alignment = "deletion";
					highestScoreArrow = 0;
				}

				// second comparison: north cell (insertion)
				score = scoringMatrixAlgorithm[i - 1][j].score + gapCost;
				scores[1] = score;
				if (score > max) {
					max = score;
					scoringMatrixAlgorithm[i][j].alignment = "insertion";
					highestScoreArrow = 1;
				}

				// last comparison: north-west cell (alignment)
				int base1 = s1.charAt(i - 1);
				int base2 = s2.charAt(j - 1);
				int alignmentScore = 0;

				if (base1 == base2) // match
					alignmentScore = matchCost;
				else // mismatch
					alignmentScore = mismatchCost;

				score = scoringMatrixAlgorithm[i - 1][j - 1].score + alignmentScore;
				scores[2] = score;
				if (score > max) {
					max = score;
					scoringMatrixAlgorithm[i][j].alignment = "alignment";
					highestScoreArrow = 2;
				}

				// finished all comparisons
				scoringMatrixAlgorithm[i][j].score = max;

				// display the arrows and color the one with the highest score
				// red, or none if highest score was 0
				// horizontal arrow
				arrowCoordinates = new Coordinates(542 + (j - 1) * verticalDistance,
						132 + (i - 1) * horizontalDistance);
				arrows[0] = new TextArrow(lang, arrowCoordinates, ArrowDirection.W, Integer.toString(scores[0]));
				srcPart1.highlight(7);
				lang.nextStep();

				// vertical arrow
				arrowCoordinates = new Coordinates(560 + (j - 1) * verticalDistance,
						112 + (i - 1) * horizontalDistance);
				arrows[1] = new TextArrow(lang, arrowCoordinates, ArrowDirection.N, Integer.toString(scores[1]));
				srcPart1.toggleHighlight(7, 8);
				lang.nextStep();

				// diagonal arrow
				arrowCoordinates = new Coordinates(542 + (j - 1) * verticalDistance,
						112 + (i - 1) * horizontalDistance);
				arrows[2] = new TextArrow(lang, arrowCoordinates, ArrowDirection.NW, Integer.toString(scores[2]));
				if (base1 == base2) {
					srcPart1.toggleHighlight(8, 10);
					srcPart1.highlight(11);
					srcPart1.highlight(12);
					srcPart1.highlight(13);
				} else {
					srcPart1.toggleHighlight(8, 10);
					srcPart1.highlight(11);
					srcPart1.highlight(14);
					srcPart1.highlight(15);
				}
				lang.nextStep();

				// write the highest score to the matrix
				scoringMatrixAnimation.put(i + 1, j + 1, Integer.toString(max), null, null);
				unhighlightAll();
				srcPart1.highlight(17);
				srcPart1.highlight(18);

				// signal the connection with the arrows by coloring the arrow
				// with the highest score and the matrix record red
				Arrow saveForTracebackArrow = null;
				if (highestScoreArrow != -1) {
					arrows[highestScoreArrow].changeColor(Color.RED);
					saveForTracebackArrow = new Arrow(lang, arrows[highestScoreArrow].getOrigin(),
							arrows[highestScoreArrow].getDirection());
					saveForTracebackArrow.hide();
					highestScoreArrow = -1;
				}
				scoringMatrixAnimation.highlightElem(i + 1, j + 1, null, null);

				if (saveForTracebackArrow != null) {
					scoringArrows.add(new ArrowIndexPair(saveForTracebackArrow, i, j));
					saveForTracebackArrow.hide();
				}

				lang.nextStep();
				srcPart1.unhighlight(17);
				srcPart1.unhighlight(18);

				for (TextArrow t : arrows)
					t.hide();

				if (saveForTracebackArrow != null)
					saveForTracebackArrow.show();

				scoringMatrixAnimation.unhighlightElem(i + 1, j + 1, null, null);
				lang.nextStep();
				scoringMatrixAnimation.unhighlightElem(0, j + 1, null, null);
			}
			scoringMatrixAnimation.unhighlightElem(i + 1, 0, null, null);
		}
	}

	void findOptimalAlignment() {
		setTipps(Hint61, Hint62);
		lang.nextStep("Traceback");

		// traceback matrix
		tracebackMatrixAnimation = lang.newStringMatrix(new Coordinates(1000, 60), scoringMatrixOnlyString,
				"tracebackAsStrings", null, matrixProperties); // (1000, 60)
																// input is
																// maximum 15
																// characters

		setTipps(Hint7);
		// copy the scoringMatrixAnimation
		fillTracebackStringMatrix();
		lang.nextStep();

		srcPart2.highlight(3);
		setTipps(Hint8);
		stackBackground = new RectProperties();
		stackBackground.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.LIGHT_GRAY);
		stackBackground.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
		stackBackground.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		stackBackgroundRect = lang.newRect(new Offset(0, 30, tracebackMatrixAnimation, "SW"),
				new Offset(180, 80, tracebackMatrixAnimation, "SW"), null, null, stackBackground); // size:
																									// (180,
																									// 50)

		outputStringProps = new TextProperties();
		outputStringProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 20));
		// text fields for the output strings
		outputString1Display = lang.newText(new Offset(10, 0, stackBackgroundRect, "NW"), "", null, null,
				outputStringProps);
		outputString2Display = lang.newText(new Offset(10, 20, stackBackgroundRect, "NW"), "", null, null,
				outputStringProps);
		outputString1ReverseDisplay = lang.newText(new Offset(10, 0, stackBackgroundRect, "NW"), "", null, null,
				outputStringProps);
		outputString2ReverseDisplay = lang.newText(new Offset(10, 20, stackBackgroundRect, "NW"), "", null, null,
				outputStringProps);

		lang.nextStep();
		srcPart2.toggleHighlight(3, 4);
		srcPart2.highlight(5);

		// Offset scoringMatrixNECorner = new Offset(0, 0,
		// scoringMatrixAnimation, "NW");
		// turn all arrows around and show them in the traceback matrix
		tracebackArrows = new LinkedList<ArrowIndexPair>();
		for (ArrowIndexPair pair : scoringArrows) {
			Arrow a = new Arrow(lang,
					new Coordinates(pair.getArrow().getOrigin().getX() + 510, pair.getArrow().getOrigin().getY()),
					ArrowDirection.invertedDirection(pair.getArrow().getDirection()));
			a.hide();
			ArrowIndexPair newPair = new ArrowIndexPair(a, pair.getRow(), pair.getColoumn());
			tracebackArrows.add(newPair);
		}

		setTipps(Hint91, Hint92);
		// find cells with maximum score
		getHighestValueList();

		// for (Position pos : startingCells) {
		// keep track of optimal alignment
		StringBuilder sequence1 = new StringBuilder();
		StringBuilder sequence2 = new StringBuilder();
		// starting position
		int i = startingCells.get(0).x;
		int j = startingCells.get(0).y;

		// for the animation we have to use i,j +1, because the animation matrix
		// is bigger
		tracebackMatrixAnimation.highlightElem(i + 1, j + 1, null, null);
		tracebackMatrixAnimation.highlightCell(i + 1, j + 1, null, null);
		lang.nextStep();
		srcPart2.unhighlight(4);
		srcPart2.unhighlight(5);
		int lineToHighlight = 0;

		setTipps(Hint101, Hint102);

		// traceback from starting cell to find optimal alignment
		while (scoringMatrixAlgorithm[i][j].score > 0) {
			int i_highlight = i;
			int j_highlight = j;

			colorFittingArrow(i, j, Color.CYAN);
			tracebackMatrixAnimation.highlightElem(0, j_highlight + 1, null, null);
			tracebackMatrixAnimation.highlightElem(i_highlight + 1, 0, null, null);

			scoringMatrixAnimation.highlightElem(0, j_highlight + 1, null, null);
			scoringMatrixAnimation.highlightElem(i_highlight + 1, 0, null, null);

			// if max was from NW cell
			if (scoringMatrixAlgorithm[i][j].alignment.equals("alignment")) {
				sequence1.append(s1.charAt(i - 1));
				sequence2.append(s2.charAt(j - 1));
				i = i - 1;
				j = j - 1;
				srcPart2.highlight(8);
				srcPart2.highlight(9);
				srcPart2.highlight(10);
				lineToHighlight = 11;
				// if max was from N cell
			} else if (scoringMatrixAlgorithm[i][j].alignment.equals("insertion")) {
				sequence1.append(s1.charAt(i - 1));
				sequence2.append("-");
				i = i - 1;
				srcPart2.highlight(15);
				srcPart2.highlight(16);
				srcPart2.highlight(17);
				lineToHighlight = 18;
				// if max was from W cell
			} else if (scoringMatrixAlgorithm[i][j].alignment.equals("deletion")) {
				sequence1.append("-");
				sequence2.append(s2.charAt(j - 1));
				j = j - 1;
				srcPart2.highlight(21);
				srcPart2.highlight(22);
				srcPart2.highlight(23);
				lineToHighlight = 24;
			}

			lang.nextStep();
			unhighlightAll();
			srcPart2.highlight(lineToHighlight);
			outputString1Display.setText(sequence1.toString(), null, null);
			outputString2Display.setText(sequence2.toString(), null, null);
			lang.nextStep();
			srcPart2.unhighlight(lineToHighlight);
			tracebackMatrixAnimation.highlightCell(i + 1, j + 1, null, null);
			tracebackMatrixAnimation.unhighlightElem(0, j_highlight + 1, null, null);
			tracebackMatrixAnimation.unhighlightElem(i_highlight + 1, 0, null, null);

			scoringMatrixAnimation.unhighlightElem(0, j_highlight + 1, null, null);
			scoringMatrixAnimation.unhighlightElem(i_highlight + 1, 0, null, null);
		}

		lang.nextStep();
		// reverse to get right string order
		String bla1 = sequence1.reverse().toString();
		String bla2 = sequence2.reverse().toString();

		setTipps(Hint111, Hint112);
		unhighlightAll();
		srcPart2.highlight(27);
		optimalAlignments.add(new Alignment(bla1, bla2));
		outputString1Display.hide();
		outputString2Display.hide();
		outputString1ReverseDisplay.setText(bla1, null, null);
		outputString2ReverseDisplay.setText(bla2, null, null);
		lang.nextStep();
		srcPart2.toggleHighlight(27, 28);
		outputString1ReverseDisplay.changeColor(null, Color.RED, null, null);
		outputString2ReverseDisplay.changeColor(null, Color.RED, null, null);

		// }
	}

	private void setTipps(String... strings) {
		clearTipps();
		int i = 0;
		for (String s : strings) {
			if (i == 0) {
				tippsAndTricks.setText(s, null, null);
				tippsFields.add(tippsAndTricks);
				i++;
			} else {
				Text x = lang.newText(new Offset(0, 15, tippsFields.get(i - 1), "NW"), "", null, null);
				x.setText(s, null, null);
				tippsFields.add(x);
				i++;
			}
		}
	}

	private void clearTipps() {
		for (Text t : tippsFields)
			t.hide();
		tippsFields.clear();
		tippsAndTricks = lang.newText(new Offset(0, 0, tippsAndTricksBackgroundRect, "NW"), "", null, null);
	}

	// Writes (0, "________") in all cells
	void initializeCellMatrix() {
		for (int i = 0; i <= lengthInputString1; i++) {
			for (int j = 0; j <= lengthInputString2; j++) {
				scoringMatrixAlgorithm[i][j] = new Cell(0, "________");
			}
		}
	}

	// one matrix is used to hold the input string and the numbers
	void initializeScoringStringMatrix() {
		for (int i = 0; i != s1.length() + 2; i++) {
			for (int j = 0; j != s2.length() + 2; j++) {
				scoringMatrixOnlyString[i][j] = " ";
			}
		}
		int i = 2;
		for (char c : s2.toCharArray()) {
			scoringMatrixOnlyString[0][i] = Character.toString(c);
			i++;
		}
		int j = 2;
		for (char c : s1.toCharArray()) {
			scoringMatrixOnlyString[j][0] = Character.toString(c);
			j++;
		}
		for (int k = 0; k != s2.length() + 1; k++) {
			scoringMatrixOnlyString[1][k + 1] = "0";
		}
		for (int l = 0; l != s1.length() + 1; l++) {
			scoringMatrixOnlyString[l + 1][1] = "0";
		}

		for (int m = 0; m != s2.length() + 2; m++) {
			scoringMatrixOnlyString[s1.length() + 2][m] = "      ";
		}
	}

	void fillTracebackStringMatrix() {
		for (int i = 0; i != scoringMatrixAnimation.getNrRows(); i++) {
			for (int j = 0; j != scoringMatrixAnimation.getNrCols(); j++) {
				tracebackMatrixAnimation.put(i, j, scoringMatrixAnimation.getElement(i, j), null, null);
			}
		}
	}

	// List the cells with maximum score
	void getHighestValueList() {
		int highestValue = 0;
		for (int i = 1; i <= lengthInputString1; i++) {
			for (int j = 1; j <= lengthInputString2; j++) {
				if (scoringMatrixAlgorithm[i][j].score > highestValue) {
					highestValue = scoringMatrixAlgorithm[i][j].score;
					startingCells.clear();
					startingCells.add(new Position(i, j));
				} else if (scoringMatrixAlgorithm[i][j].score == highestValue)
					startingCells.add(new Position(i, j));
			}
		}
	}

	// method only used for the animation
	void unhighlightAll() {
		for (int i = 0; i != srcPart1.length(); i++)
			srcPart1.unhighlight(i);
		for (int i = 0; i != srcPart2.length(); i++)
			srcPart2.unhighlight(i);
	}

	// method only used for the animation
	void colorFittingArrow(int i, int j, Color color) {
		for (ArrowIndexPair pair : tracebackArrows) {
			if (pair.equals(i, j))
				pair.getArrow().show();
			pair.getArrow().changeColor(color);
		}
	}
}

//
// used for position of a cell
class Position {
	final int x;
	final int y;

	Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
}

// ================================================================ custom
// classes ===========================================================
// used for optimal alignment of strings
class Alignment {
	final String x;
	final String y;

	Alignment(String x, String y) {
		this.x = x;
		this.y = y;
	}
}

// used to save 'arrows' and score in matrix cells
class Cell {
	int score;
	String alignment;

	Cell(int score, String alignment) {
		this.score = score;
		this.alignment = alignment;
	}
}

// ====================================================== arrows
// =================================================
class ArrowIndexPair {
	Arrow a;
	int c;
	int r;

	public ArrowIndexPair(Arrow arrow, int row, int coloumn) {
		a = arrow;
		r = row;
		c = coloumn;
	}

	Arrow getArrow() {
		return a;
	}

	int getColoumn() {
		return c;
	}

	int getRow() {
		return r;
	}

	boolean equals(ArrowIndexPair pair) {
		if (c == pair.getColoumn() && r == pair.getRow())
			return true;
		return false;
	}

	boolean equals(int r, int c) {
		if (this.r == r && this.c == c)
			return true;
		return false;
	}
}

enum ArrowDirection {
	N, W, NW, S, E, SE;

	static ArrowDirection invertedDirection(ArrowDirection direction) {
		ArrowDirection d = null;
		if (direction.equals(ArrowDirection.N)) {
			d = ArrowDirection.S;
		} else if (direction.equals(ArrowDirection.E)) {
			d = ArrowDirection.W;
		} else if (direction.equals(ArrowDirection.S)) {
			d = ArrowDirection.N;
		} else if (direction.equals(ArrowDirection.W)) {
			d = ArrowDirection.E;
		} else if (direction.equals(ArrowDirection.NW)) {
			d = ArrowDirection.SE;
		} else if (direction.equals(ArrowDirection.SE)) {
			d = ArrowDirection.NW;
		}
		return d;
	}
}

class Arrow {
	Language l;
	Polyline p;
	PolylineProperties pp;
	Coordinates[] nodes = new Coordinates[5];
	ArrowDirection d;
	Coordinates c;

	public Arrow(Language lang, Coordinates origin, ArrowDirection direction) {
		this(lang, origin, direction, Color.BLACK);
	}

	public Arrow(Language lang, Coordinates origin, ArrowDirection direction, Color color) {
		l = lang;
		d = direction;
		c = origin;
		pp = new PolylineProperties();
		pp.set(AnimationPropertiesKeys.COLOR_PROPERTY, color);
		createNodes(origin, direction);
		p = lang.newPolyline(nodes, "polyline", null, pp);
	}

	void hide() {
		p.hide();
	}

	void show() {
		p.show();
	}

	void changeColor(Color color) {
		p.changeColor(null, color, null, null);
	}

	ArrowDirection getDirection() {
		return d;
	}

	Coordinates getOrigin() {
		return c;
	}

	void move(ArrowDirection direction, int distance) {
		p.hide();
		if (direction.equals(ArrowDirection.W)) {
			c = new Coordinates(c.getX() - distance, c.getY());
		} else if (direction.equals(ArrowDirection.N)) {
			c = new Coordinates(c.getX(), c.getY() - distance);
		} else if (direction.equals(ArrowDirection.E)) {
			c = new Coordinates(c.getX() + distance, c.getY());
		} else if (direction.equals(ArrowDirection.S)) {
			c = new Coordinates(c.getX(), c.getY() + distance);
		}

		createNodes(c, d);
		p = l.newPolyline(nodes, null, null, pp);
	}

	void createNodes(Coordinates origin, ArrowDirection d) {
		if (d.equals(ArrowDirection.NW)) {
			nodes[0] = origin;
			nodes[1] = new Coordinates(origin.getX() + 10, origin.getY() + 10);
			nodes[2] = new Coordinates(origin.getX() + 5, origin.getY() + 10);
			nodes[3] = new Coordinates(origin.getX() + 10, origin.getY() + 10);
			nodes[4] = new Coordinates(origin.getX() + 10, origin.getY() + 5);
		} else if (d.equals(ArrowDirection.N)) {
			nodes[0] = origin;
			nodes[1] = new Coordinates(origin.getX(), origin.getY() + 12);
			nodes[2] = new Coordinates(origin.getX() - 3, origin.getY() + 9);
			nodes[3] = new Coordinates(origin.getX(), origin.getY() + 12);
			nodes[4] = new Coordinates(origin.getX() + 3, origin.getY() + 9);
		} else if (d.equals(ArrowDirection.W)) {
			nodes[0] = origin;
			nodes[1] = new Coordinates(origin.getX() + 10, origin.getY());
			nodes[2] = new Coordinates(origin.getX() + 7, origin.getY() - 3);
			nodes[3] = new Coordinates(origin.getX() + 10, origin.getY());
			nodes[4] = new Coordinates(origin.getX() + 7, origin.getY() + 3);
		} else if (d.equals(ArrowDirection.S)) {
			nodes[0] = new Coordinates(origin.getX(), origin.getY() + 12);
			nodes[1] = new Coordinates(origin.getX(), origin.getY());
			nodes[2] = new Coordinates(origin.getX() + 3, origin.getY() + 3);
			nodes[3] = new Coordinates(origin.getX(), origin.getY());
			nodes[4] = new Coordinates(origin.getX() - 3, origin.getY() + 3);
		} else if (d.equals(ArrowDirection.E)) {
			nodes[0] = new Coordinates(origin.getX() + 10, origin.getY());
			nodes[1] = new Coordinates(origin.getX(), origin.getY());
			nodes[2] = new Coordinates(origin.getX() + 3, origin.getY() + 3);
			nodes[3] = new Coordinates(origin.getX(), origin.getY());
			nodes[4] = new Coordinates(origin.getX() + 3, origin.getY() - 3);
		} else if (d.equals(ArrowDirection.SE)) {
			nodes[0] = new Coordinates(origin.getX() + 10, origin.getY() + 10);
			nodes[1] = new Coordinates(origin.getX(), origin.getY());
			nodes[2] = new Coordinates(origin.getX() + 5, origin.getY());
			nodes[3] = new Coordinates(origin.getX(), origin.getY());
			nodes[4] = new Coordinates(origin.getX(), origin.getY() + 5);
		}

	}
}

class TextArrow extends Arrow {
	Text t;
	TextProperties tp;

	public TextArrow(Language lang, Coordinates origin, ArrowDirection direction, String text) {
		this(lang, origin, direction, text, Color.BLACK);
	}

	public TextArrow(Language lang, Coordinates origin, ArrowDirection direction, String text, Color color) {
		super(lang, origin, direction, color);

		tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.COLOR_PROPERTY, color);
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 10));
		tp.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
		createText(lang, origin, direction, text);
	}

	void hide() {
		super.hide();
		t.hide();
	}

	void show() {
		super.show();
		t.show();
	}

	public void changeColor(Color color) {
		super.changeColor(color);
		t.changeColor(null, color, null, null);
	}

	void createText(Language lang, Coordinates origin, ArrowDirection direction, String text) {
		if (direction.equals(ArrowDirection.NW)) {
			t = lang.newText(new Coordinates(origin.getX() + 5, origin.getY() - 10), text, null, null, tp);
		} else if (direction.equals(ArrowDirection.N)) {
			t = lang.newText(new Coordinates(origin.getX() + 8, origin.getY() - 2), text, null, null, tp);
		} else if (direction.equals(ArrowDirection.W)) {
			t = lang.newText(new Coordinates(origin.getX() + 4, origin.getY() - 14), text, null, null, tp);
		} else if (direction.equals(ArrowDirection.SE)) {
			t = lang.newText(new Coordinates(origin.getX() + 5, origin.getY() - 15), text, null, null, tp);
		} else if (direction.equals(ArrowDirection.S)) {
			t = lang.newText(new Coordinates(origin.getX() - 7, origin.getY() - 10), text, null, null, tp);
		} else if (direction.equals(ArrowDirection.E)) {
			t = lang.newText(new Coordinates(origin.getX(), origin.getY() - 13), text, null, null, tp);
		}
	}
}
