/*
 * Hirschberg.java
 * Felix Mannl, Tim Hoffmann, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc.hirschberg;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;

import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.LinkedList;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.addons.InfoBox;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class Hirschberg implements Generator {
	// ========================================== Algo Daten ==================================================
	private Language lang;
	private int delCost;
	private int insCost;
	private int matchCost;
	private int mismatchCost;
	private String s1;
	private String s2;

	private StringBuilder inputString1;
	private StringBuilder inputString2;
	int recursionDepth;
	// ==========================================================================================================

	// =========================================== Animal Daten  =================================================
	public final static Timing defaultDuration = new TicksTiming(30);

	private static final String HEADLINE = "Hirschberg: global string alignment";
	private final static String HEADLINE_INTRO = "Hirschberg: overview";
	private final static String HEADLINE_OUTRO = "Hirschberg: conclusion";

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

	// source code background
	private SourceCodeProperties srcProps;
	private SourceCode srcPart1;
	private SourceCode srcPart2;

	private RectProperties srcBackground;
	private Rect srcBackgroundRect;

	// matrix and background
	private MatrixProperties matrixProps;
	private StringMatrix scoringMatrixAnimation;

	// tree
	private TreeNode hirschbergTreeRoot;
	private TreeObserver to;

	// alignment display
	private RectProperties alignmentBackgroundProps;
	private Rect alignmentBackground;
	private TextProperties alignmentProps;
	private Text alignment1;
	private Text alignment2;

	// index calculation
	private MatrixProperties scoreDisplayProps;
	private String[][] scoreDisplayBase;
	private StringMatrix scoreLDisplay;
	private StringMatrix scoreRDisplay;
	private StringMatrix scoreSumDisplay;
	private TextProperties sumTextProps;
	private Text sumLine;
	private Text addSymbol;
	private Text equalSymbol;
	private int[] sum;

	// tipps and tricks
	private RectProperties tippsAndTricksBackgroundProps;
	private Rect tippsAndTricksBackgroundRect;
	private Text tippsAndTricks;
	private LinkedList<Text> tippFields;

	// ==========================================================================================================

	public void init(){
		lang = new AnimalScript("Hirschberg", "Felix Mannl, Tim Hoffmann", 800, 600);
		lang.setStepMode(true);
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		delCost = (Integer)primitives.get("delCost");
		matrixProps = (MatrixProperties)props.getPropertiesByName("matrixProps");
		insCost = (Integer)primitives.get("insCost");
		matchCost = (Integer)primitives.get("matchCost");
		srcProps = (SourceCodeProperties)props.getPropertiesByName("srcProps");
		mismatchCost = (Integer)primitives.get("mismatchCost");
		s1 = (String)primitives.get("s1");
		s2 = (String)primitives.get("s2");

		setupAnimalData();
		doStuff();

		return lang.toString();
	}

	void setupAnimalData() {
		inputString1 = new StringBuilder(s1);
		inputString2 = new StringBuilder(s2);

		// intro page
		introDescriptionPage = new InfoBox(lang, new Coordinates(10, 20), 500, "");
		introDescriptionStrings1 = new LinkedList<String>();
		introDescriptionStrings1.add("");
		introDescriptionStrings1.add("");
		introDescriptionStrings1.add("The Hirschberg algorithm is used to find the optimal global alignment for two strings.");
		introDescriptionStrings1.add("Optimality is reached by minimizing the Levenshtein distance, which is defined by the sum of");
		introDescriptionStrings1.add("the costs of insertions, substitutions and deletions needed to change one string into the other.");
		introDescriptionStrings1.add("The algorithm is usually applied in bioinformatics to analyse sequences of DNA or proteins.");
		introDescriptionStrings1.add("");
		introDescriptionStrings1.add("");
		introDescriptionStrings1.add("Alignment example:");
		introDescriptionStrings1.add("CC-A");
		introDescriptionStrings1.add("CGT-");
		introDescriptionStrings1.add("");
		introDescriptionStrings1.add("In this alignment we would need the following actions to convert the first string into the second:");
		introDescriptionStrings1.add("C  C: Substitution with same character (match)");
		introDescriptionStrings1.add("C  G: Substitution with character G (mismatch)");
		introDescriptionStrings1.add("-   G: Insertion of character G");
		introDescriptionStrings1.add("A   -: Deletion of character A");
		
		introDescriptionStrings2 = new LinkedList<String>();
		introDescriptionStrings2.add("");
		introDescriptionStrings2.add("");
		introDescriptionStrings2.add("The Hirschberg algorithm is a modification of the Needleman-Wunsch algorithm that uses");
		introDescriptionStrings2.add("a divide and conquer strategy. Therefore Hirschberg is called recursively with substrings");
		introDescriptionStrings2.add("until one of the recursion anchors is reached.");
		introDescriptionStrings2.add("To get the substrings one string is always split in the middle (s1mid).");
		introDescriptionStrings2.add("The second split index (s2split) needs to be calculated, this includes");
		introDescriptionStrings2.add("computing the Needleman-Wunsch matrix. As a result the costs for insertion,");
		introDescriptionStrings2.add("deletion and substitution are involved.");
		introDescriptionPage.hide();
		

		// outro page
		outroDescriptionPage = new InfoBox(lang, new Coordinates(10, 20), 500, "");
		outroDescriptionSrings = new LinkedList<String>();
		outroDescriptionSrings.add("");
		outroDescriptionSrings.add("");
		outroDescriptionSrings.add("The Hirschberg algorithm is a modification of the Needleman-Wunsch algorithm that uses ");
		outroDescriptionSrings.add("a divide and conquer strategy.");
		outroDescriptionSrings.add("Let's compare the complexity of these methods.");
		outroDescriptionSrings.add("The performance and space complexity for two Strings with length m and n are shown below:");
		outroDescriptionSrings.add("");
		outroDescriptionSrings.add("                                              Performance        Space");
		outroDescriptionSrings.add("Hirschberg:                        O(m*n)                     O(m*n)");
		outroDescriptionSrings.add("with optimization:              O(m*n)                     O(min{m,n})");
		outroDescriptionSrings.add("Needleman-Wunsch:      O(m*n)                     O(m*n)");
		outroDescriptionSrings.add("");
		outroDescriptionSrings.add("As we can see the complexity of both methods is the same, except for the optimized Hirschberg method.");
		outroDescriptionSrings.add("The optimization needs just O(min{n, m}) space, since it doesn't save the whole Needleman-Wunsch matrix ");
		outroDescriptionSrings.add("while calculating the last line of the matrix. ");
		outroDescriptionPage.hide();

		// headline property, the same everywhere
		headlineProps = new TextProperties();
		headlineProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 22));
		// headline
		headline = lang.newText(new Coordinates(10, 10), HEADLINE, null, null, headlineProps);
		headline.hide();

		// code background
		srcBackground = new RectProperties();
		srcBackground.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.LIGHT_GRAY);
		srcBackground.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
		srcBackground.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		srcBackgroundRect = lang.newRect(new Offset(0, 30, headline, "NW"), new Offset(490, 530, headline, "NW"),
				"srcBackgroundRect", null, srcBackground);
		srcBackgroundRect.hide();

		// source code part 1
		srcPart1 = lang.newSourceCode(new Offset(10, 0, srcBackgroundRect, "NW"), "sourceCode", null, srcProps);
		srcPart1.addCodeLine("Hirschberg main method", null, 0, null);
		srcPart1.addCodeLine("", null, 0, null);
		srcPart1.addCodeLine("Given: String s1 with length m , String s2 with length n", null, 1, null);
		srcPart1.addCodeLine("Initialize empty String align1, align2", null, 1, null);
		srcPart1.addCodeLine("", null, 0, null);
		srcPart1.addCodeLine("if m == 0", null, 2, null);
		srcPart1.addCodeLine("for i=1 to n", null, 3, null);
		srcPart1.addCodeLine("align1 = align1 + '-'", null, 4, null);
		srcPart1.addCodeLine("align2 = align2 + s2[i]", null, 4, null);
		srcPart1.addCodeLine("end", null, 2, null);
		srcPart1.addCodeLine("", null, 0, null);
		srcPart1.addCodeLine("if n == 0", null, 2, null);
		srcPart1.addCodeLine("for i=1 to m", null, 3, null);
		srcPart1.addCodeLine("align1 = align1 + s1[i]", null, 4, null);
		srcPart1.addCodeLine("align2 = align2 + '-'", null, 4, null);
		srcPart1.addCodeLine("end", null, 2, null);
		srcPart1.addCodeLine("", null, 0, null);
		srcPart1.addCodeLine("else if m == 1 or n == 1", null, 2, null);
		srcPart1.addCodeLine("(align1,align2) = NeedlemanWunsch(s1,s2)", null, 3, null);
		srcPart1.addCodeLine("", null, 0, null);
		srcPart1.addCodeLine("else", null, 2, null);
		srcPart1.addCodeLine("s1mid = m/2", null, 3, null);
		srcPart1.addCodeLine("lineL = NWLastLine(s1[0:s1mid], s2)", null, 3, null);
		srcPart1.addCodeLine("lineR = NWLastLine(reverse(s1[s1mid+1:m]), reverse(s2))", null, 3, null);
		srcPart1.addCodeLine("s2split = index of max(lineL + reverse(lineR))", null, 3, null);
		srcPart1.addCodeLine("(align1, align2) = Hirschberg(s1[0:s1s], s2[0:s2split])", null, 3, null);
		srcPart1.addCodeLine("+ Hirschberg(s1[s1mid+1:m], s2[s2split+1:n])", null, 13, null);
		srcPart1.addCodeLine("end", null, 2, null);
		srcPart1.addCodeLine("", null, 0, null);
		srcPart1.addCodeLine("return (align1,align2)", null, 2, null);
		srcPart1.hide();
		// sc.addCodeLine(, null, 2, null);

		// source code part 2
		srcPart2 = lang.newSourceCode(new Offset(10, 0, srcBackgroundRect, "NW"), "sourceCode", null, srcProps);
		srcPart2.addCodeLine("Get last line of Needleman-Wunsch matrix", null, 0, null);
		srcPart2.addCodeLine("", null, 0, null);
		srcPart2.addCodeLine("Given: String s1 with length m , String s2 with length n", null, 1, null);
		srcPart2.addCodeLine("Given: delCost, insCost, matchCost, mismatchCost", null, 1, null);
		srcPart2.addCodeLine("Initialize Matrix M, Array lastLine", null, 1, null);
		srcPart2.addCodeLine("", null, 0, null);
		srcPart2.addCodeLine("M[0][0] = 0", null, 2, null);
		srcPart2.addCodeLine("for j=1 to n", null, 2, null);
		srcPart2.addCodeLine("M[0][j] = M[0][j-1] + insCost", null, 3, null);
		srcPart2.addCodeLine("for i=1 to m", null, 2, null);
		srcPart2.addCodeLine("M[i][0] = M[i-1][0] + delCost", null, 3, null);
		srcPart2.addCodeLine("", null, 0, null);
		srcPart2.addCodeLine("for i=1 to m", null, 2, null);
		srcPart2.addCodeLine("for j=1 to n", null, 3, null);
		srcPart2.addCodeLine("char1 = s1[j-1]", null, 4, null);
		srcPart2.addCodeLine("char2 = s2[i-1]", null, 4, null);
		srcPart2.addCodeLine("if( char1 == char2 )", null, 4, null);
		srcPart2.addCodeLine("subScore = M[i-1][j-1] + matchCost", null, 5, null);
		srcPart2.addCodeLine("else", null, 4, null);
		srcPart2.addCodeLine("subScore = M[i-1][j-1] + mismatchCost", null, 5, null);
		srcPart2.addCodeLine("delScore = M[i][j-1] + delCost", null, 4, null);
		srcPart2.addCodeLine("insScore = M[i-1][j] + insCost", null, 4, null);
		srcPart2.addCodeLine("M[i][j] = max(delScore, insScore, subScore)", null, 4, null);
		srcPart2.addCodeLine("", null, 0, null);
		srcPart2.addCodeLine("for j=0 to n", null, 2, null);
		srcPart2.addCodeLine("lastLine(j) = M[m][j]", null, 3, null);
		srcPart2.addCodeLine("return lastLine", null, 2, null);
		srcPart2.hide();

		// index calculation
		scoreDisplayProps = new MatrixProperties();
		scoreDisplayProps.set(AnimationPropertiesKeys.GRID_ALIGN_PROPERTY, "right");
		scoreDisplayProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));
		sumTextProps = new TextProperties();
		sumTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));

		// input data
		inputDataDisplay = new InfoBox(lang, new Offset(10, 15, srcBackgroundRect, "SW"), 100, "Inputs:");
		inputDataDisplay.hide();

		// tree
		hirschbergTreeRoot = new TreeNode(lang, inputString1.toString(), inputString2.toString());
		hirschbergTreeRoot.updateNode(new Coordinates(1000, 60));
		to = new TreeObserver(hirschbergTreeRoot);
		to.getNode().hide();

		// alignment
		alignmentBackgroundProps = new RectProperties();
		alignmentBackgroundProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.LIGHT_GRAY);
		alignmentBackgroundProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
		alignmentBackgroundProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		alignmentBackground = lang.newRect(new Coordinates(1300, 60), new Coordinates(1480, 110), null, null, alignmentBackgroundProps);
		alignmentBackground.hide();
		alignmentProps = new TextProperties();
		alignmentProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 20));
		alignment1 = lang.newText(new Offset(10, 0, alignmentBackground, "NW"), "", null, null, alignmentProps);
		alignment2 = lang.newText(new Offset(10, 20, alignmentBackground, "NW"), "", null, null, alignmentProps);

		// tipps and tricks
		tippFields = new LinkedList<Text>();
		tippsAndTricksBackgroundProps = new RectProperties();
		tippsAndTricksBackgroundProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.LIGHT_GRAY);
		tippsAndTricksBackgroundProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
		tippsAndTricksBackgroundProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		tippsAndTricksBackgroundRect = lang.newRect(new Offset(0, 40, srcBackgroundRect, "SE"),
				new Offset(470, 130, srcBackgroundRect, "SE"), null, null, tippsAndTricksBackgroundProps);
		tippsAndTricksBackgroundRect.hide();

		tippsAndTricks = lang.newText(new Offset(0, 0, tippsAndTricksBackgroundRect, "NW"), "", null, null);
		tippsAndTricks.hide();
	}

	void doStuff() {
		animateIntro();
		animateInitialize();
		hirschberg(inputString1, inputString2);
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
		setTipps("The optimal global alignment is returned.");
		alignment1.changeColor(null, Color.RED, null, null);
		alignment2.changeColor(null, Color.RED, null, null);
		lang.nextStep();
		headline.setText(HEADLINE_OUTRO, null, null);
		// hide anything else...
		srcPart1.hide();
		srcPart2.hide();
		srcBackgroundRect.hide();
		inputDataDisplay.hide();
		alignment1.hide();
		alignment2.hide();
		alignmentBackground.hide();
		TreeNode.hideAll(hirschbergTreeRoot);
		clearTipps();
		tippsAndTricksBackgroundRect.hide();
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
		setTipps("Generic tipps are shown here.");
		lang.nextStep("Initialization");
		setTipps("The source code can be found to the left.");
		lang.nextStep();

		srcBackgroundRect.show();
		srcPart1.show();
		srcPart1.highlight(0);
		lang.nextStep();

		setTipps("The initial inputs are found below the source code.");
		lang.nextStep();

		srcPart1.toggleHighlight(0, 2);
		inputDataDisplay.show();
		inputDataStrings.clear();
		inputDataStrings.add("s1: " + inputString1 + ", length: " + inputString1.length());
		inputDataStrings.add("s2: " + inputString2 + ", length: " + inputString2.length());
		inputDataDisplay.setText(inputDataStrings);
		lang.nextStep();

		inputDataStrings.add("delCost: " + this.delCost);
		inputDataStrings.add("insCost: " + this.insCost);
		inputDataStrings.add("matchCost: " + this.matchCost);
		inputDataStrings.add("mismatchCost: " + this.mismatchCost);
		inputDataDisplay.setText(inputDataStrings);
		lang.nextStep();

		to.getNode().show();
		setTipps("A tree is initialized with the initial inputs.",
				"It is used for better visualization of the recursive descent of the hirschberg algorithm.");
		lang.nextStep();
		alignmentBackground.show();
		setTipps("An empty stack is initialized.",
				"It will later hold the optimal aligment of the two input strings.");
		srcPart1.toggleHighlight(2, 3);
		lang.nextStep();
		setTipps("Now we start the algorithm.");
		lang.nextStep("Algorithm start");
	}

	Alignment hirschberg(StringBuilder x, StringBuilder y) {
		Alignment align = new Alignment(new StringBuilder(), new StringBuilder());

		setTipps("Each recursive call attaches a new node to the tree.", "The node holds the two inputs of this recursive call.",
				"The root node holds the two initial inputs strings.");
		to.getNode().unhighlightNode();
		if(recursionDepth > 0) {
			if(!to.getNode().hasLeftChild()) {
				to.getNode().addChild(x.toString(), y.toString(), true);
				to.setNode(to.getNode().leftChild);
			}
			else {
				to.getNode().addChild(x.toString(), y.toString(), false);
				to.setNode(to.getNode().rightChild);
			}
			to.getNode().highlightNode();
		}

		recursionDepth++;

		unhighlightAll();
		lang.nextStep();

		if (x.length() == 0) {
			srcPart1.toggleHighlight(3, 5);
			setTipps("A recursion anchor visualised by a leaf is reached.",
					"The first substring is empty.");
			lang.nextStep();
			for (int i = 0; i < y.length(); i++) {
				align.str1.append("-");
				align.str2.append(y.charAt(i));
				srcPart1.toggleHighlight(5, 6);
				srcPart1.highlight(7);
				srcPart1.highlight(8);
				setTipps("The second input string of the current recursive call is copied.",
						"The first one is filled with gaps respectively.",
						"Both strings are appendend to the alignment.");
				//				lang.nextStep();
			}

		} else if (y.length() == 0) {
			srcPart1.toggleHighlight(3, 11);
			setTipps("A recursion anchor visualised by reaching a leaf.",
					"The second substring is empty.");
			lang.nextStep();
			for (int i = 0; i < x.length(); i++) {
				align.str1.append(x.charAt(i));
				align.str2.append("-");
				srcPart1.toggleHighlight(11, 12);
				srcPart1.highlight(13);
				srcPart1.highlight(14);
				setTipps("The first input string of the current recursive call is copied.",
						"The second one is filled with gaps respectively.",
						"Both strings are appendend to the alignment.");
				//				lang.nextStep();
			}

		} else if (x.length() == 1 || y.length() == 1) {
			srcPart1.toggleHighlight(3, 17);
			setTipps("A recursion anchor visualised by reaching a leaf.");
			lang.nextStep();
			
			align = needlemanWunsch(x, y);
			srcPart1.toggleHighlight(17, 18);
			setTipps("In this case we need to call the Needleman-Wunsch algorithm.",
					"The resulting two strings are appended to the alignment.",
					"For further explanation have a look at the Needleman-Wunsch",
					"algorithm in ANIMAL.");
			//			lang.nextStep();
		} else {
			srcPart1.toggleHighlight(3, 20);
			setTipps("We didn't reach a recursion anchor, so we need to calculate",
					"the split indices for the strings.");
			lang.nextStep();
			
			int xmid = x.length() / 2;
			srcPart1.toggleHighlight(20, 21);
			setTipps("The first index is calculated simply by halving the first string.");
			lang.nextStep();
			
			
			srcPart1.toggleHighlight(21, 22);
			setTipps("The split index for the second string will now be calculated.",
					"Therefore two Needleman-Wunsch(NW) matrices are created,",
					"using a different half of the first string for each.",
					"From the last rows of these two matrices the split index is computed",
					"by adding them and finding the index of the highest element.",
					"Everything is shown in detail in the next steps.");
			lang.nextStep();

			// calculate maximum at its position(ymid) y is split
			srcPart1.hide();
			int[][] matrixL = buildNeedlemanWunschMatrix(new StringBuilder(x.substring(0, xmid)), y);
			int[] scoreL = getLastLine(matrixL);
			setTipps("The last row of the first Needleman-Wunsch matrix can now be found above.");
			scoreDisplayBase = new String[1][scoreL.length];
			scoreDisplayBase[0] = transformToString(scoreL);
			scoreLDisplay = lang.newStringMatrix(new Offset(10, 20, scoringMatrixAnimation, "SW"), scoreDisplayBase, null, null, scoreDisplayProps);

			scoringMatrixAnimation.hide();
			srcPart1.show();
			srcPart1.highlight(22);
			scoreLDisplay.highlightElemColumnRange(0, 1, scoreLDisplay.getNrCols() - 1, null, null);
			lang.nextStep();

			scoreLDisplay.unhighlightElemColumnRange(0, 1, scoreLDisplay.getNrCols() - 1, null, null);
			srcPart1.toggleHighlight(22, 23);
			setTipps("The second Needleman-Wunsch matrix will now be calculated.");
			lang.nextStep();

			srcPart1.hide();
			int[][] matrixR = buildNeedlemanWunschMatrix(new StringBuilder(x.substring(xmid, x.length())).reverse(),
					new StringBuilder(y).reverse());
			int[] scoreR = getLastLine(matrixR);
			// display scoreR reverse before its actually calculated
			int[] revScoreRTemp = new int[scoreR.length];
			for (int i = 0; i < scoreR.length; i++) {
				revScoreRTemp[i] = scoreR[scoreR.length - 1 - i];
			}
			setTipps("The reversed last row of the second Needleman-Wunsch matrix",
					 "can now be found above.");
			scoreDisplayBase[0] = transformToString(revScoreRTemp);
			scoreRDisplay = lang.newStringMatrix(new Offset(10, 40, scoringMatrixAnimation, "SW"), scoreDisplayBase, null, null, scoreDisplayProps);

			scoringMatrixAnimation.hide();
			srcPart1.show();
			srcPart1.highlight(23);
			scoreRDisplay.highlightElemColumnRange(0, 1, scoreRDisplay.getNrCols() - 1, null, null);
			lang.nextStep();

			scoreRDisplay.unhighlightElemColumnRange(0, 1, scoreRDisplay.getNrCols() - 1, null, null);
			int ymid = getSplitIndex(scoreL, scoreR);

			setTipps("The two arrays are added element-wise.");
			sumLine = lang.newText(new Offset(0, 60, scoringMatrixAnimation, "SW"), "----------------------------------", null, null, sumTextProps);
			addSymbol = lang.newText(new Offset(0, 45, scoringMatrixAnimation, "SW"), "+", null, null, sumTextProps);
			srcPart1.toggleHighlight(23, 24);

			lang.nextStep();
			setTipps("The highest element of the result is found and its index is noted.",
					"The index is now used as the split index.");
			equalSymbol = lang.newText(new Offset(0, 75, scoringMatrixAnimation, "SW"), "=", null, null, sumTextProps);

			scoreDisplayBase[0] = transformToString(sum);
			scoreSumDisplay = lang.newStringMatrix(new Offset(10, 70, scoringMatrixAnimation, "SW"), scoreDisplayBase, null, null, scoreDisplayProps);
			scoreSumDisplay.highlightCell(0, ymid + 1, null, null);

			lang.nextStep();

			scoreLDisplay.hide();
			scoreRDisplay.hide();
			scoreSumDisplay.hide();
			sumLine.hide();
			addSymbol.hide();
			equalSymbol.hide();

			srcPart1.toggleHighlight(24, 25);
			setTipps("The first recursive call follows.",
					"The inputs are the first half of the first string",
					"and the first part of the second string split at the split index.",
					"They can afterwards be found in the left child node.");
			// recursive call with left part of x,y and right part of x,y
			Alignment alignLeft = hirschberg(new StringBuilder(x.substring(0, xmid)), new StringBuilder(y.substring(0, ymid)));

			unhighlightAll();
			srcPart1.highlight(25);
			lang.nextStep();
			setTipps("The second recursive call follows.",
					"The inputs are the second half of the first string",
					"and the second part of the second string split at the split index.",
					"They can afterwards be found in the right child node.");
			srcPart1.toggleHighlight(25, 26);
			lang.nextStep();
			Alignment alignRight = hirschberg(new StringBuilder(x.substring(xmid, x.length())), new StringBuilder(y.substring(ymid, y.length())));
			align = alignLeft.append(alignRight);

			unhighlightAll();
			srcPart1.highlight(26);
			//			lang.nextStep();
		}

		// if leaf, add current strings to alignment
		if(to.getNode().isLeaf()) {
//			setTipps("The current node is a leaf.",
//					"The two strings are appended to the alignment.");
			alignment1.setText(alignment1.getText() + align.str1.toString(), null, null);
			alignment2.setText(alignment2.getText() + align.str2.toString(), null, null);
		}

		lang.nextStep();
		setTipps("The recursive call for this sub tree is finished.",
				"We return to the last unfinished recursive call.",
				"This is visualised by returning to the first parent",
				"node without a right child node.");
		to.getNode().unhighlightNode();
		if(!to.getNode().isRoot())
			to.getNode().parent.highlightNode();
		to.setNode(to.getNode().parent);

		unhighlightAll();
		srcPart1.highlight(29);
		lang.nextStep();

		recursionDepth--;
		return align;
	}

	// -----effizientere Berechnung der letzen Zeile?!-----
	int[][] buildNeedlemanWunschMatrix(StringBuilder x, StringBuilder y) {
		// animation stuff
		TextArrow[] arrows = new TextArrow[3];
		int highestScoreArrow = -1;
		int scoreMax;

		Coordinates arrowCoordinates;
		int verticalDistance = 29;
		int horizontalDistance = 27;

		//initialize animated matrix
		String[][] scoringMatrixOnlyString = new String[x.length() + 3][y.length() + 2];
		initializeScoringStringMatrix(scoringMatrixOnlyString, x, y);
		scoringMatrixAnimation = lang.newStringMatrix(new Offset(50, 0, srcBackgroundRect, "NE"),
				scoringMatrixOnlyString, null, null, matrixProps);
		srcPart2.show();
		srcPart2.highlight(2);
		srcPart2.highlight(3);
		srcPart2.highlight(4);
		setTipps("The Needleman-Wunsch matrix of the two strings is created.",
				"The string above the matrix is either the second one in the current node or its reverse.",
				"The string to the left of the matrix is either the first half of the first string",
				"in the node or the reversed second half.");
		lang.nextStep();

		int[][] matrixScore = new int[x.length() + 1][y.length() + 1];
		matrixScore[0][0] = 0;
		scoringMatrixAnimation.put(1, 1, Integer.toString(0), null, null);
		scoringMatrixAnimation.highlightElem(1, 1, null, null);
		unhighlightAll();
		srcPart2.highlight(6);
		setTipps("The matrix is initialized by filling the first row with multiples of the delCost",
				"and the first coloumn with multiples of the insCost.");
		lang.nextStep();

		// fill first row
		for (int j = 1; j <= y.length(); j++){
			matrixScore[0][j] = matrixScore[0][j - 1] + insCost;
			scoringMatrixAnimation.put(1, j+1, Integer.toString(matrixScore[0][j]), null, null);
		}
		scoringMatrixAnimation.unhighlightElem(1, 1, null, null);
		scoringMatrixAnimation.highlightCellColumnRange(1, 2, scoringMatrixAnimation.getNrCols()-1, null, null);
		srcPart2.toggleHighlight(6, 7);
		srcPart2.highlight(8);
		lang.nextStep();

		// fill first column
		for (int i = 1; i <= x.length(); i++) {
			matrixScore[i][0] = matrixScore[i - 1][0] + delCost;
			scoringMatrixAnimation.put(i+1, 1, Integer.toString(matrixScore[i][0]), null, null);
		}
		unhighlightAll();
		srcPart2.highlight(9);
		srcPart2.highlight(10);
		scoringMatrixAnimation.unhighlightCellColumnRange(1, 2, scoringMatrixAnimation.getNrCols()-1, null, null);
		scoringMatrixAnimation.highlightCellRowRange(2, scoringMatrixAnimation.getNrRows()-2, 1, null, null);
		lang.nextStep();
		scoringMatrixAnimation.unhighlightCellRowRange(2, scoringMatrixAnimation.getNrRows()-2, 1, null, null);
		setTipps("The rest of the matrix is now calculated.",
				"Each arrow indicates a possible score for the cell it's pointing to.",
				"The highest score is picked and written into the cell.",
				"For further explanation have a look at",
				"the Smith-Waterman or Needleman-Wunsch algorithm.");
		// fill rest
		for (int i = 1; i <= x.length(); i++) {
			for (int j = 1; j <= y.length(); j++) {
				int scoreSub;
				if (x.charAt(i - 1) == y.charAt(j - 1)) {
					scoreSub = matrixScore[i - 1][j - 1] + matchCost;
				} else {
					scoreSub = matrixScore[i - 1][j - 1] + mismatchCost;
				}
				int scoreDel = matrixScore[i - 1][j] + delCost;
				int scoreIns = matrixScore[i][j - 1] + insCost;
				matrixScore[i][j] = Math.max(scoreSub, Math.max(scoreDel, scoreIns));
				lang.nextStep();

				scoreMax = scoreSub;
				highestScoreArrow = 0;
				if(scoreDel > scoreMax) {
					scoreMax = scoreDel;
					highestScoreArrow = 1;
				}
				if(scoreIns > scoreMax) {
					scoreMax = scoreIns;
					highestScoreArrow = 2;
				}

				// display the arrows and color the one with the highest score red, or none if highest score was 0
				// diagonal arrow
				arrowCoordinates = new Coordinates(602 + (j-1) * verticalDistance, 87 + (i-1) * horizontalDistance);
				arrows[0] = new TextArrow(lang, arrowCoordinates, ArrowDirection.NW, Integer.toString(scoreSub));
				unhighlightAll();
				srcPart2.highlight(14);
				srcPart2.highlight(15);
				if (x.charAt(i - 1) == y.charAt(j - 1)) {
					srcPart2.highlight(16);
					srcPart2.highlight(17);					
				}else{
					srcPart2.highlight(18);
					srcPart2.highlight(19);	
				}
				lang.nextStep();

				// vertical arrow
				arrowCoordinates = new Coordinates(620 + (j-1) * verticalDistance, 87 + (i-1) * horizontalDistance);
				arrows[1] = new TextArrow(lang, arrowCoordinates, ArrowDirection.N, Integer.toString(scoreDel));
				unhighlightAll();
				srcPart2.highlight(20);
				lang.nextStep();

				// horizontal arrow
				arrowCoordinates = new Coordinates(602 + (j-1) * verticalDistance, 107 + (i-1) * horizontalDistance);
				arrows[2] = new TextArrow(lang, arrowCoordinates, ArrowDirection.W, Integer.toString(scoreIns));
				srcPart2.toggleHighlight(20, 21);
				lang.nextStep();

				arrows[highestScoreArrow].changeColor(Color.RED);
				scoringMatrixAnimation.put(i+1, j+1, Integer.toString(matrixScore[i][j]), null, null);
				scoringMatrixAnimation.highlightElem(i+1, j+1, null, null);
				unhighlightAll();
				srcPart2.highlight(22);
				lang.nextStep();

				for(TextArrow a : arrows) {
					a.hide();
				}
				scoringMatrixAnimation.unhighlightElem(i+1, j+1, null, null);
			}
		}
		return matrixScore;
	}

	// initialize the animated matrix
	void initializeScoringStringMatrix(String[][] scoringMatrixOnlyString, StringBuilder x, StringBuilder y) {		
		for (int i = 0; i < x.length() + 2; i++) {
			for (int j = 0; j < y.length() + 2; j++) {
				scoringMatrixOnlyString[i][j] = " ";
			}
		}
		int i = 2;
		for (char c : y.toString().toCharArray()) {
			scoringMatrixOnlyString[0][i] = Character.toString(c);
			i++;
		}
		int j = 2;
		for (char c : x.toString().toCharArray()) {
			scoringMatrixOnlyString[j][0] = Character.toString(c);
			j++;
		}
		for (int m = 0; m < y.length() + 2; m++) {
			scoringMatrixOnlyString[x.length() + 2][m] = "      ";
		}
	}

	// -----effizientere Berechnung der letzen Zeile?!-----
	int[] getLastLine(int[][] matrix) {
		int mb = matrix[0].length;
		int mh = matrix.length;
		int[] lastLine = new int[mb];
		for (int j = 0; j < mb; j++) {
			lastLine[j] = matrix[mh - 1][j];
		}
		srcPart2.toggleHighlight(22, 24);
		srcPart2.highlight(25);
		srcPart2.highlight(26);
		scoringMatrixAnimation.highlightCellColumnRange(mh, 1, mb, null, null);
		setTipps("The last line of the matrix is saved for further calculation.");

		lang.nextStep();
		unhighlightAll();
		srcPart2.hide();
		return lastLine;
	}

	// reverse scoreR, add up both arrays, get position of max value of
	// resulting array
	int getSplitIndex(int[] scoreL, int[] scoreR) {
		// reverse scoreR and add up arrays
		int[] revScoreR = new int[scoreR.length];
		int[] sum = new int[scoreR.length];
		for (int i = 0; i < scoreR.length; i++) {
			revScoreR[i] = scoreR[scoreR.length - 1 - i];
			sum[i] = scoreL[i] + revScoreR[i];
		}
		// get max value of sum and corresponding index
		int max = scoreL[0] + revScoreR[0] - 1;
		int maxPosition = -1;
		for (int i = 0; i < scoreL.length; i++) {
			if (sum[i] > max) {
				max = sum[i];
				maxPosition = i;
			}
		}
		this.sum = sum;
		return maxPosition;
	}

	private String getNumberOfSpaces(int n) {
		int spaces = 3 - getNumberOfDigits(n);

		String ret = "";
		for(int i = 0; i != spaces; i++)
			ret = ret + " ";
		return ret;
	}

	private int getNumberOfDigits(int n) {
		int digits = 1;
		if(n < 0)
			digits++;
		if(-n > 9)
			digits++;
		if(n > 99)
			digits++;
		return digits;
	}

	private String[] transformToString(int[] array) {
		String[] res = new String[array.length + 2];

		res[0] = "[";
		res[res.length - 1] = "]";

		for(int i = 0; i != array.length; i++) {
			res[i + 1] = getNumberOfSpaces(array[i]) + Integer.toString(array[i]);
		}
		return res;
	}

	// NeedlemanWunsch Algo
	Alignment needlemanWunsch(StringBuilder x, StringBuilder y) {
		Alignment align = new Alignment(new StringBuilder(), new StringBuilder());

		//build Needleman-Wunsch matrix
		int[][] matrixScore = new int[x.length() + 1][y.length() + 1];
		matrixScore[0][0] = 0;
		// fill first row
		for (int j = 1; j <= y.length(); j++)
			matrixScore[0][j] = matrixScore[0][j - 1] + insCost;
		// fill first column
		for (int i = 1; i <= x.length(); i++) {
			matrixScore[i][0] = matrixScore[i - 1][0] + delCost;
			// fill rest
			for (int j = 1; j <= y.length(); j++) {
				int scoreSub;
				if (x.charAt(i - 1) == y.charAt(j - 1))
					scoreSub = matrixScore[i - 1][j - 1] + matchCost;
				else
					scoreSub = matrixScore[i - 1][j - 1] + mismatchCost;
				int scoreDel = matrixScore[i - 1][j] + delCost;
				int scoreIns = matrixScore[i][j - 1] + insCost;
				matrixScore[i][j] = Math.max(scoreSub, Math.max(scoreDel, scoreIns));
			}
		}

		int i = x.length();
		int j = y.length();

		// traceback
		while (i > 0 || j > 0) {
			// if substitution
			if (i > 0 && j > 0) {
				boolean match = x.charAt(i - 1) == y.charAt(j - 1);
				if ((match && matrixScore[i][j] == matrixScore[i - 1][j - 1] + matchCost)
						|| (!match && matrixScore[i][j] == matrixScore[i - 1][j - 1] + mismatchCost)) {
					align.str1.append(x.charAt(i - 1));
					align.str2.append(y.charAt(j - 1));
					i = i - 1;
					j = j - 1;
				}
				// if deletion
			}
			if (i > 0 && matrixScore[i][j] == matrixScore[i - 1][j] + delCost) {
				align.str1.append(x.charAt(i - 1));
				align.str2.append("-");
				i = i - 1;
				// if insertion
			} else if ((j > 0 && matrixScore[i][j] == matrixScore[i][j - 1] + insCost)) {
				align.str1.append("-");
				align.str2.append(y.charAt(j - 1));
				j = j - 1;
			}
		}
		// reverse Strings for output
		align.str1.reverse();
		align.str2.reverse();
		return align;
	}

	// method only used for the animation
	void unhighlightAll() {
		for(int i = 0; i != srcPart1.length(); i++)
			srcPart1.unhighlight(i);
		for(int i = 0; i != srcPart2.length(); i++)
			srcPart2.unhighlight(i);
	}

	private void setTipps(String...strings) {
		clearTipps();
		int i = 0;
		for(String s : strings) {
			if(i == 0) {
				tippsAndTricks.setText(s, null, null);
				tippFields.add(tippsAndTricks);
				i++;
			}
			else {
				Text x = lang.newText(new Offset(0, 15, tippFields.get(i-1), "NW"), "", null, null);
				x.setText(s, null, null);
				tippFields.add(x);
				i++;
			}	
		}
	}

	private void clearTipps() {
		for(Text t : tippFields)
			t.hide();
		tippFields.clear();
		tippsAndTricks = lang.newText(new Offset(0, 0, tippsAndTricksBackgroundRect, "NW"), "", null, null);
	}

	public String getName() {
		return "Hirschberg";
	}

	public String getAlgorithmName() {
		return "Hirschberg: global string alignment";
	}

	public String getAnimationAuthor() {
		return "Felix Mannl, Tim Hoffmann";
	}

	public String getDescription(){
		return "The Hirschberg algorithm is used to find the optimal global alignment for two strings."
				+"\n"
				+"Optimality is reached by minimizing the Levenshtein distance, which is defined by the sum of"
				+"\n"
				+"the cost of insertions, substitutions and deletions needed to convert one string into the other."
				+"\n"
				+"The algorithm is usually applied in bioinformatics to analyse sequences of DNA or proteins."
				+"\n"
				+"\n"
				+"Please do not use unreasonable long input strings and inappropriate costs."
				+"\n"
				+"Long input strings will not explain the algorithm better and they only add more steps to the animation."
				+"\n"
				+"High negative or positive scores will make it harder to read the matrices.";
	}

	public String getCodeExample(){
		return "Hirschberg:"
				+"\n"
				+"Given: String s1 with length m, String s2 with length n"
				+"\n"
				+"Initialize empty String align1, align2"
				+"\n"
				+"\n"
				+"    if m == 0"
				+"\n"
				+"        for i = 1 to n"
				+"\n"
				+"            align1 = align1 + ' - '"
				+"\n"
				+"            align2 = align2 + s2[i]"
				+"\n"
				+"    end"
				+"\n"
				+"\n"
				+"    if n == 0"
				+"\n"
				+"        for i = 1 to m"
				+"\n"
				+"            align1 = align1 + s1[i]"
				+"\n"
				+"            align2 = align2 + ' - '"
				+"\n"
				+"    end"
				+"\n"
				+"\n"
				+"    else if m == 1 or n == 1"
				+"\n"
				+"        (align1 , align2) = NeedlemanWunsch(s1 , s2)"
				+"\n"
				+"\n"
				+"    else"
				+"\n"
				+"        s1mid = m / 2"
				+"\n"
				+"        lineL = NWLastLine(s1[0:s1mid] , s2)"
				+"\n"
				+"        lineR = NWLastLine(reverse(s1[s1mid+1:m]) , reverse(s2))"
				+"\n"
				+"        s2split = index of max(lineL + reverse(lineR))"
				+"\n"
				+"        (align1, align2) = Hirschberg(s1[0:s1s] , s2[0:s2split]) + Hirschberg(s1[s1mid+1:m] , s2[s2split+1:n])"
				+"\n"
				+"    end"
				+"\n"
				+"\n"
				+"    return (align1 , align2)"
				+"\n"
				+"\n"
				+"\n"
				+"Get last line of Needleman-Wunsch matrix:"
				+"\n"
				+"\n"
				+"    Given: String s1 with length m , String s2 with length n"
				+"\n"
				+"    Given: delCost, insCost, matchCost, mismatchCost"
				+"\n"
				+"    Initialize Matrix M, Array lastLine"
				+"\n"
				+"\n"
				+"    M[0][0] = 0"
				+"\n"
				+"        for j=1 to n"
				+"\n"
				+"            M[0][j] = M[0][j-1]"
				+"\n"
				+"        for i=1 to m"
				+"\n"
				+"            M[i][0] = M[i-1][0] + delCost"
				+"\n"
				+"\n"
				+"        for i=1"
				+"\n"
				+"            for j=1 to n"
				+"\n"
				+"                char1 = s1[j-1]"
				+"\n"
				+"                char2 = s2[i-1]"
				+"\n"
				+"                if( char1 == char2 )"
				+"\n"
				+"                    subScore = M[i-1][j-1] + matchCost"
				+"\n"
				+"                else"
				+"\n"
				+"                     subScore = M[i-1][j-1] + mismatchCost"
				+"\n"
				+"                delScore = M[i][j-1] + delCost"
				+"\n"
				+"                insScore = M[i-1][j] + insCost"
				+"\n"
				+"                M[i][j] = max(delScore, insScore, subScore)"
				+"\n"
				+"\n"
				+"        for j=0 to n"
				+"\n"
				+"            lastLine(j) = M[m][j]"
				+"\n"
				+"    return lastLine";
	}

	public String getFileExtension(){
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

}

//Contains two strings, which represents an alignment
class Alignment {
	StringBuilder str1;
	StringBuilder str2;

	Alignment(StringBuilder str1, StringBuilder str2) {
		this.str1 = str1;
		this.str2 = str2;
	}

	// append both Strings of given alignment
	Alignment append(Alignment al1) {
		this.str1.append(al1.str1);
		this.str2.append(al1.str2);
		return this;
	}
}

class TreeNode {

	final static int X_OFFSET = 200;
	final static int Y_OFFSET = 50;

	final static int X_OFFSET_REDUCTION_PER_PARENT_LEFT = 60;
	final static int X_OFFSET_REDUCTION_PER_PARENT_RIGHT = 55;

	Language lang;
	Text text;
	String leftContent;
	String rightContent;
	Coordinates position;

	TreeNode parent;
	TreeNode leftChild;
	TreeNode rightChild;

	Polyline leftLine;
	Polyline rightLine;

	public TreeNode(Language l, String left, String right) {
		lang = l;
		leftContent = left; 
		rightContent = right;
	}

	public void updateNode(Coordinates pos) {
		position = pos;
		text = lang.newText(pos, "", null, null);
		setText(leftContent, rightContent);
	}

	public void addChild(String left, String right, boolean isLeftChild) {
		TreeNode childNode = new TreeNode(lang, left, right);
		childNode.parent = this;
		childNode.updateNode(calculatePosition(this, isLeftChild));

		if(isLeftChild)
			leftChild = childNode;
		else
			rightChild = childNode;

		drawPolyline(this, isLeftChild);
	}

	public boolean hasLeftChild() {
		return leftChild != null;
	}

	public boolean isRoot() {
		return parent == null;
	}

	public boolean isLeaf() {
		return leftChild == null && rightChild == null;
	}

	public static void hideAll(TreeNode node) {

		if(node.leftChild != null) {
			hideAll(node.leftChild);
		}
		if(node.rightChild != null) {
			hideAll(node.rightChild);
		}
		node.hide();
	}

	public void hide() {
		text.hide();
		if(leftLine != null)
			leftLine.hide();
		if(rightLine != null)
			rightLine.hide();
	}

	public void show() {
		text.show();
		if(leftLine != null)
			leftLine.show();
		if(rightLine != null)
			rightLine.show();
	}

	public void highlightNode() {
		this.changeColor(Color.RED);
	}
	public void unhighlightNode() {
		this.changeColor(Color.BLACK);
	}

	private void changeColor(Color color) {
		text.changeColor(null, color, null, null);
	}

	public void setText(String left, String right) {
		this.text.setText("( " + left + " , "+ right + " )", null, null);
	}

	private void drawPolyline(TreeNode parent, boolean isLeftChild) {
		Coordinates start = new Coordinates(parent.position.getX() + text.getText().length() / 2 * 5, parent.position.getY() + 17);

		Coordinates end = calculatePosition(this, isLeftChild);
		end = new Coordinates(end.getX() + text.getText().length() / 2 * 5, end.getY() - 0);

		Coordinates[] nodes = {start, end};
		if(isLeftChild)
			leftLine = lang.newPolyline(nodes, null, null);
		else
			rightLine = lang.newPolyline(nodes, null, null);
	}

	private Coordinates calculatePosition(TreeNode parent, boolean isLeftChild) {
		Coordinates pos;
		int parents = 0;

		for(TreeNode node = parent; /* empty */ ; node = node.parent) {
			if(node == null)
				break;
			parents++;
		}

		int x = parent.position.getX() + getRelativePosition(parents, isLeftChild);
		int y = parent.position.getY() + TreeNode.Y_OFFSET;

		pos = new Coordinates(x, y);

		return pos;
	}

	private int getRelativePosition(int parents, boolean isLeftChild) {
		if(isLeftChild)
			return -TreeNode.X_OFFSET + (parents * X_OFFSET_REDUCTION_PER_PARENT_LEFT);
		else
			return +TreeNode.X_OFFSET - (parents * X_OFFSET_REDUCTION_PER_PARENT_RIGHT);
	}
}

class TreeObserver {
	TreeNode node;

	public TreeObserver(TreeNode root) {
		node = root;
	}

	public void setNode(TreeNode node) {
		this.node = node;
	}

	public TreeNode getNode() {
		return node;
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
			c = new Coordinates(c.getX()-distance, c.getY());
		} else if (direction.equals(ArrowDirection.N)) {
			c = new Coordinates(c.getX(), c.getY()-distance);
		} else if (direction.equals(ArrowDirection.E)) {
			c = new Coordinates(c.getX()+distance, c.getY());
		} else if (direction.equals(ArrowDirection.S)) {
			c = new Coordinates(c.getX(), c.getY()+distance);
		}

		createNodes(c, d);
		p = l.newPolyline(nodes, null, null, pp);
	}

	void createNodes(Coordinates origin, ArrowDirection d) {
		if (d.equals(ArrowDirection.NW)) {
			nodes[0] = origin;
			nodes[1] = new Coordinates(origin.getX() + 10, origin.getY() + 10);
			nodes[2] = new Coordinates(origin.getX() + 5 , origin.getY() + 10);
			nodes[3] = new Coordinates(origin.getX() + 10, origin.getY() + 10);
			nodes[4] = new Coordinates(origin.getX() + 10, origin.getY() + 5 );
		} else if (d.equals(ArrowDirection.N)) {
			nodes[0] = origin;
			nodes[1] = new Coordinates(origin.getX()	 , origin.getY() + 12);
			nodes[2] = new Coordinates(origin.getX() - 3 , origin.getY() + 9 );
			nodes[3] = new Coordinates(origin.getX()	 , origin.getY() + 12);
			nodes[4] = new Coordinates(origin.getX() + 3 , origin.getY() + 9 );
		} else if (d.equals(ArrowDirection.W)) {
			nodes[0] = origin;
			nodes[1] = new Coordinates(origin.getX() + 10, origin.getY()	 );
			nodes[2] = new Coordinates(origin.getX() + 7 , origin.getY() - 3 );
			nodes[3] = new Coordinates(origin.getX() + 10, origin.getY()	 );
			nodes[4] = new Coordinates(origin.getX() + 7 , origin.getY() + 3 );
		} else if (d.equals(ArrowDirection.S)) {
			nodes[0] = new Coordinates(origin.getX()	 , origin.getY() + 12);
			nodes[1] = new Coordinates(origin.getX()	 , origin.getY()	 );
			nodes[2] = new Coordinates(origin.getX() + 3 , origin.getY() + 3 );
			nodes[3] = new Coordinates(origin.getX()	 , origin.getY()	 );
			nodes[4] = new Coordinates(origin.getX() - 3 , origin.getY() + 3 );
		} else if (d.equals(ArrowDirection.E)) {
			nodes[0] = new Coordinates(origin.getX()+10, origin.getY());
			nodes[1] = new Coordinates(origin.getX(), origin.getY());
			nodes[2] = new Coordinates(origin.getX()+3, origin.getY()+3);
			nodes[3] = new Coordinates(origin.getX(), origin.getY());
			nodes[4] = new Coordinates(origin.getX()+3, origin.getY()-3);
		} else if (d.equals(ArrowDirection.SE)) {
			nodes[0] = new Coordinates(origin.getX() + 10, origin.getY() + 10);
			nodes[1] = new Coordinates(origin.getX()	 , origin.getY()	 );
			nodes[2] = new Coordinates(origin.getX() + 5 , origin.getY()	 );
			nodes[3] = new Coordinates(origin.getX()	 , origin.getY()	 );
			nodes[4] = new Coordinates(origin.getX()	 , origin.getY() + 5 );
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
			t = lang.newText(new Coordinates(origin.getX()+5, origin.getY()-10), text, null, null, tp);
		} else if (direction.equals(ArrowDirection.N)) {
			t = lang.newText(new Coordinates(origin.getX()+8, origin.getY()-2), text, null, null, tp);
		} else if (direction.equals(ArrowDirection.W)) {
			t = lang.newText(new Coordinates(origin.getX()+4, origin.getY()-14), text, null, null, tp);
		} else if (direction.equals(ArrowDirection.SE)) {
			t = lang.newText(new Coordinates(origin.getX()+5, origin.getY()-15), text, null, null, tp);
		} else if (direction.equals(ArrowDirection.S)) {
			t = lang.newText(new Coordinates(origin.getX()-7, origin.getY()-10), text, null, null, tp);
		} else if (direction.equals(ArrowDirection.E)) {
			t = lang.newText(new Coordinates(origin.getX(), origin.getY()-13), text, null, null, tp);
		}
	}
}