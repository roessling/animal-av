/*
 * MersenneTwisterGenerator.java
 * Jan Philipp Wagner, Joelle Heun, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Ellipse;
import algoanim.primitives.IntArray;
import algoanim.primitives.IntMatrix;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.EllipseProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.FillInBlanksQuestionModel;

public class MersenneTwisterGenerator implements Generator {
	private Language lang;
	private SourceCodeProperties sourceCode;
	private ArrayProperties binaryArrayProperties;
	private MatrixProperties mersenneTwisterProperties;

	private int seed;

	private IntArray row1;
	private IntArray row2;
	private IntArray row3;
	private Text operation;

	public void init() {
		lang = new AnimalScript("Mersenne Twister", "Jan Philipp Wagner, Joelle Heun", 800, 600);
		lang.setStepMode(true);
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		sourceCode = (SourceCodeProperties) props.getPropertiesByName("sourceCode");
		seed = (Integer) primitives.get("seed");
		binaryArrayProperties = (ArrayProperties) props.getPropertiesByName("binaryArrayProperties");
		mersenneTwisterProperties = (MatrixProperties) props.getPropertiesByName("mersenneTwisterProperties");

		// Execute standard algorithm
		int[] MT = seed_mt(seed);

		// **** PROPERTIES **** //
		MatrixProperties matrixProps = mersenneTwisterProperties;

		ArrayProperties arrayProps = binaryArrayProperties;

		MatrixProperties indicesProps = new MatrixProperties();
		indicesProps.set(AnimationPropertiesKeys.GRID_BORDER_COLOR_PROPERTY, Color.BLUE);
		indicesProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLUE);

		EllipseProperties ellipsePropsBlack = new EllipseProperties();
		ellipsePropsBlack.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		EllipseProperties ellipsePropsRed = new EllipseProperties();
		ellipsePropsRed.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);

		RectProperties rp = new RectProperties();
		rp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);

		TextProperties tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 15));
		tp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);

		SourceCodeProperties scSeedProps = sourceCode;

		// **** INTRODUCTION **** //

		// Headline
		Rect banner1 = lang.newRect(new Coordinates(20, 20), new Coordinates(230, 60), "Banner", null, rp);
		banner1.changeColor("fillColor", Color.YELLOW, null, null);
		Text headline1 = lang.newText(new Coordinates(30, 30), "Mersenne Twister", "Mersenne Twister", null, tp);
		headline1.setFont(new Font("Monospaced", Font.BOLD, 20), null, null);

		// Introduction text
		lang.newText(new Coordinates(20, 85), "Der Mersenne Twister ist der am Weitesten verbreitete (general purpose)",
				"Description1", null, tp);
		lang.newText(new Coordinates(20, 100),
				"pseudorandom number generator und gilt zudem als einer der schnellsten.", "Description2", null, tp);
		lang.newText(new Coordinates(20, 115),
				"Verwendet wird er beispielsweise in Microsoft Excel, PHP, Python, MATLAB und", "Description3", null,
				tp);
		lang.newText(new Coordinates(20, 130), "GNU Octave.", "Description4", null, tp);
		lang.newText(new Coordinates(20, 145),
				"Da es sich bei den verwendeten Operation lediglich um Addition, Multiplikation,", "Description3", null,
				tp);
		lang.newText(new Coordinates(20, 160),
				"bitwise XOR/OR/AND und Shifts handelt, konnte ich mit dieser Implementierung", "Description4", null,
				tp);
		lang.newText(new Coordinates(20, 175), "40000000 Pseudozufallszahlen in nur 2.578 Sekunden generieren.",
				"Description4", null, tp);

		lang.nextStep();

		lang.hideAllPrimitives();
		banner1.show();
		headline1.show();

		lang.nextStep("Seeding");

		// create source code entity
		SourceCode scSeed = lang.newSourceCode(new Coordinates(20, 330), "sourceCode", null, scSeedProps);

		// add the lines to the SourceCode object
		scSeed.addCodeLine("public void seed_mt(int seed) {", null, 0, null);
		scSeed.addCodeLine("  MT[0] = seed;", null, 0, null);
		scSeed.addCodeLine("  for (int i = 1; i < 624; i++) {", null, 0, null);
		scSeed.addCodeLine("    int s = MT[i - 1] ^ (MT[i - 1] >>> 30);", null, 0, null);
		scSeed.addCodeLine("    MT[i] = s * 1812433253 + i;", null, 0, null);
		scSeed.addCodeLine("  }", null, 0, null);
		scSeed.addCodeLine("  twist();", null, 0, null);
		scSeed.addCodeLine("}", null, 0, null);

		scSeed.highlight(0);

		lang.nextStep();

		scSeed.toggleHighlight(0, 1);

		lang.nextStep("Visualize State Array");

		// 2 relative Anchors for moving whole structure
		int a1 = 618;
		int a2 = 80;

		// Visualize parts of state array
		int[][] MT1 = new int[2][32];
		int[][] MT2 = new int[1][32];
		IntMatrix mt1 = lang.newIntMatrix(new Coordinates(a1, a2), MT1, "mt1", null, matrixProps);
		IntMatrix mt2 = lang.newIntMatrix(new Coordinates(a1, a2 + 80), MT1, "mt2", null, matrixProps);
		IntMatrix mt3 = lang.newIntMatrix(new Coordinates(a1, a2 + 160), MT1, "mt3", null, matrixProps);
		IntMatrix mt4 = lang.newIntMatrix(new Coordinates(a1, a2 + 240), MT2, "mt4", null, matrixProps);

		// Visualize the indices
		int[][] INDICES1 = new int[][] { { 0 }, { 1 } };
		int[][] INDICES2 = new int[][] { { 227 }, { 228 } };
		int[][] INDICES3 = new int[][] { { 396 }, { 397 } };
		int[][] INDICES4 = new int[][] { { 623 } };

		IntMatrix indices1 = lang.newIntMatrix(new Coordinates(a1 + 580, a2), INDICES1, "indices1", null, indicesProps);
		IntMatrix indices2 = lang.newIntMatrix(new Coordinates(a1 + 580, a2 + 80), INDICES2, "indices2", null,
				indicesProps);
		IntMatrix indices3 = lang.newIntMatrix(new Coordinates(a1 + 580, a2 + 160), INDICES3, "indices3", null,
				indicesProps);
		IntMatrix indices4 = lang.newIntMatrix(new Coordinates(a1 + 580, a2 + 240), INDICES4, "indices4", null,
				indicesProps);

		// Visualize missing parts of array with ellipses
		Ellipse ellipse1 = lang.newEllipse(new Coordinates(a1 + 290, a2 + 65), new Coordinates(2, 2), "e1", null,
				ellipsePropsBlack);
		Ellipse ellipse2 = lang.newEllipse(new Coordinates(a1 + 290, a2 + 145), new Coordinates(2, 2), "e1", null,
				ellipsePropsBlack);
		Ellipse ellipse3 = lang.newEllipse(new Coordinates(a1 + 290, a2 + 225), new Coordinates(2, 2), "e1", null,
				ellipsePropsBlack);

		lang.nextStep();

		Text text = lang.newText(new Coordinates(20, 80), "Seed:", "t1", null, tp);
		Text seedT = lang.newText(new Coordinates(20, 90), Integer.toString(seed), "t2", null, tp);

		lang.nextStep();

		// Move Seed into Array
		seedT.moveTo("W", "Translate", new Coordinates(618, 90), new TicksTiming(0), new TicksTiming(150));

		lang.nextStep();

		text.hide();
		seedT.hide();

		intToBinaryArrayInMatrix(seed, mt1, 0);
		scSeed.toggleHighlight(1, 2);
		mt1.highlightCellColumnRange(0, 0, 31, null, null);

		lang.nextStep();

		mt1.unhighlightCellColumnRange(0, 1, 31, null, null); // for some reason necessary?
		mt1.unhighlightCellColumnRange(0, 0, 1, null, null);

		scSeed.toggleHighlight(1, 2);

		lang.nextStep();

		scSeed.toggleHighlight(2, 3);

		// Question 1
		FillInBlanksQuestionModel frage1 = new FillInBlanksQuestionModel("frage1");
		frage1.setPrompt(
				"Welchen Wert (in binaer) hat s nach dieser Zeile? Gib deine Antwort in 32 Bit beginnend mit '0b' an!");
		frage1.addAnswer("1", getS(MT[0]), 1, "Richtig! Um s zu berechnene XORed man MT[i-1] mit seinen 2 msbs.");
		lang.addFIBQuestion(frage1);

		lang.nextStep();

		row1 = lang.newIntArray(new Coordinates(48, 80), new int[32], "row1", null, arrayProps);
		row1.showIndices(false, null, null);
		intToBinaryArray(MT[0], row1);

		lang.nextStep();

		Polyline line = lang.newPolyline(new Coordinates[] { new Coordinates(20, 145), new Coordinates(592, 145) },
				"line", null, new PolylineProperties());

		lang.nextStep();

		operation = lang.newText(new Coordinates(20, 125), "", "operation", null, tp);
		operation.hide();
		shiftBinaryArrayRightLogical(row1, 30);

		mt1.highlightCellColumnRange(0, 0, 31, null, null);

		lang.nextStep();

		row2 = lang.newIntArray(new Coordinates(48, 108), new int[32], "row2", null, arrayProps);
		row2.showIndices(false, null, null);
		intToBinaryArray(MT[0], row2);
		row2.highlightCell(0, 31, null, null);

		lang.nextStep();

		mt1.unhighlightCellColumnRange(0, 1, 31, null, null);
		mt1.unhighlightCellColumnRange(0, 0, 1, null, null);
		row2.unhighlightCell(0, 31, null, null);

		lang.nextStep();

		row3 = lang.newIntArray(new Coordinates(48, 150), new int[32], "row3", null, arrayProps);
		row3.showIndices(false, null, null);
		row3.hide();

		lang.nextStep();

		int s = calculate("XOR");

		lang.nextStep();

		scSeed.toggleHighlight(3, 4);

		lang.nextStep();

		swapArrays(row1, row3);

		lang.nextStep();

		row3.hide();

		lang.nextStep();

		row2.hide();

		lang.nextStep();

		intToBinaryArray(1812433253, row2);
		row2.show();

		lang.nextStep();

		calculate("MUL");
		operation.hide();

		lang.nextStep();

		swapArrays(row1, row3);

		lang.nextStep();

		row3.hide();

		lang.nextStep();

		row2.hide();

		lang.nextStep();

		intToBinaryArray(1, row2);
		row2.show();

		lang.nextStep();

		operation.setText("ADD", null, null);
		operation.show();

		lang.nextStep();

		intToBinaryArray(1 + binaryArrayToInt(row1), row3);
		row3.show();

		lang.nextStep();

		row3.highlightCell(0, 31, null, null);

		lang.nextStep();

		assert MT[1] == binaryArrayToInt(row3);
		row3.unhighlightCell(0, 31, null, null);
		intToBinaryArrayInMatrix(binaryArrayToInt(row3), mt1, 1);

		lang.nextStep();

		row1.hide();
		row2.hide();
		row3.hide();
		operation.hide();
		line.hide();
		mt1.unhighlightCellColumnRange(1, 1, 31, null, null);
		mt1.unhighlightCellColumnRange(1, 0, 1, null, null);

		lang.nextStep();

		scSeed.unhighlight(4);

		lang.nextStep();

		scSeed.highlight(2);
		scSeed.highlight(3);
		scSeed.highlight(4);
		scSeed.highlight(5);

		lang.nextStep();

		ellipse1.changeColor("Color", Color.RED, null, null);

		lang.nextStep();

		ellipse1.changeColor("Color", Color.BLACK, null, null);
		scSeed.unhighlight(2);
		scSeed.unhighlight(3);
		scSeed.unhighlight(4);
		scSeed.unhighlight(5);

		lang.nextStep();

		scSeed.highlight(2);
		scSeed.highlight(3);
		scSeed.highlight(4);
		scSeed.highlight(5);

		lang.nextStep();

		intToBinaryArrayInMatrix(MT[227], mt2, 0);

		lang.nextStep();

		mt2.unhighlightCellColumnRange(0, 1, 31, null, null);
		mt2.unhighlightCellColumnRange(0, 0, 1, null, null);
		scSeed.unhighlight(2);
		scSeed.unhighlight(3);
		scSeed.unhighlight(4);
		scSeed.unhighlight(5);

		lang.nextStep();

		scSeed.highlight(2);
		scSeed.highlight(3);
		scSeed.highlight(4);
		scSeed.highlight(5);

		lang.nextStep();

		intToBinaryArrayInMatrix(MT[228], mt2, 1);

		lang.nextStep();

		mt2.unhighlightCellColumnRange(1, 1, 31, null, null);
		mt2.unhighlightCellColumnRange(1, 0, 1, null, null);
		scSeed.unhighlight(2);
		scSeed.unhighlight(3);
		scSeed.unhighlight(4);
		scSeed.unhighlight(5);

		lang.nextStep();

		ellipse2.changeColor("Color", Color.RED, null, null);

		lang.nextStep();

		ellipse2.changeColor("Color", Color.BLACK, null, null);
		scSeed.unhighlight(2);
		scSeed.unhighlight(3);
		scSeed.unhighlight(4);
		scSeed.unhighlight(5);

		lang.nextStep();

		scSeed.highlight(2);
		scSeed.highlight(3);
		scSeed.highlight(4);
		scSeed.highlight(5);

		lang.nextStep();

		intToBinaryArrayInMatrix(MT[396], mt3, 0);

		lang.nextStep();

		mt3.unhighlightCellColumnRange(0, 1, 31, null, null);
		mt3.unhighlightCellColumnRange(0, 0, 1, null, null);
		scSeed.unhighlight(2);
		scSeed.unhighlight(3);
		scSeed.unhighlight(4);
		scSeed.unhighlight(5);

		lang.nextStep();

		scSeed.highlight(2);
		scSeed.highlight(3);
		scSeed.highlight(4);
		scSeed.highlight(5);

		lang.nextStep();

		intToBinaryArrayInMatrix(MT[397], mt3, 1);

		lang.nextStep();

		mt3.unhighlightCellColumnRange(1, 1, 31, null, null);
		mt3.unhighlightCellColumnRange(1, 0, 1, null, null);
		scSeed.unhighlight(2);
		scSeed.unhighlight(3);
		scSeed.unhighlight(4);
		scSeed.unhighlight(5);

		lang.nextStep();

		scSeed.highlight(2);
		scSeed.highlight(3);
		scSeed.highlight(4);
		scSeed.highlight(5);

		lang.nextStep();

		ellipse3.changeColor("Color", Color.RED, null, null);

		lang.nextStep();

		ellipse3.changeColor("Color", Color.BLACK, null, null);
		scSeed.unhighlight(2);
		scSeed.unhighlight(3);
		scSeed.unhighlight(4);
		scSeed.unhighlight(5);

		lang.nextStep();

		scSeed.highlight(2);
		scSeed.highlight(3);
		scSeed.highlight(4);
		scSeed.highlight(5);

		lang.nextStep();

		intToBinaryArrayInMatrix(MT[623], mt4, 0);

		lang.nextStep();

		mt4.unhighlightCellColumnRange(0, 1, 31, null, null);
		mt4.unhighlightCellColumnRange(0, 0, 1, null, null);
		scSeed.unhighlight(2);
		scSeed.unhighlight(3);
		scSeed.unhighlight(4);
		scSeed.unhighlight(5);

		lang.nextStep();

		scSeed.highlight(2);
		scSeed.highlight(3);
		scSeed.highlight(4);
		scSeed.highlight(5);

		lang.nextStep();

		scSeed.unhighlight(2);
		scSeed.unhighlight(3);
		scSeed.unhighlight(4);
		scSeed.unhighlight(5);
		scSeed.highlight(6);

		lang.nextStep("Twist");

		lang.hideAllPrimitives();

		Rect banner2 = lang.newRect(new Coordinates(20, 20), new Coordinates(170, 60), "Banner", null, rp);
		banner2.changeColor("fillColor", Color.YELLOW, null, null);
		Text headline2 = lang.newText(new Coordinates(30, 30), "Erlaeuterung", "Erlaeuterung", null, tp);
		headline2.setFont(new Font("Monospaced", Font.PLAIN, 20), null, null);

		lang.newText(new Coordinates(20, 85),
				"Nachdem das Array nun geseedet-, und so der initiale Zustand des Mersenne", "Description1", null, tp);
		lang.newText(new Coordinates(20, 100),
				"Twisters erreicht wurde, kann mit dem 'Twisting' des Arrays fortgefahren werden.", "Description2",
				null, tp);
		lang.newText(new Coordinates(20, 115), "Hier wird durch eine Reihe von weiteren bitwise Operationen auf das",
				"Description2", null, tp);
		lang.newText(new Coordinates(20, 130), "Array eine stochastische Gleichverteilung dessen Eintraege hergestellt.",
				"Description2", null, tp);

		lang.nextStep();

		lang.hideAllPrimitives();
		banner1.show();
		headline1.show();
		mt1.show();
		mt2.show();
		mt3.show();
		mt4.show();
		ellipse1.show();
		ellipse2.show();
		ellipse3.show();
		indices1.show();
		indices2.show();
		indices3.show();
		indices4.show();

		SourceCode scTwist = lang.newSourceCode(new Coordinates(20, 330), "sourceCode", null, scSeedProps);

		scTwist.addCodeLine("private void twist() {", null, 0, null);
		scTwist.addCodeLine("  for (int i = 0; i < 227; i++) {", null, 0, null);
		scTwist.addCodeLine("    int bits = (MT[i] & 0x80000000) | (MT[i+1] & 0x7fffffff);", null, 0, null);
		scTwist.addCodeLine("    MT[i] = MT[i + 397] ^ (bits >>> 1) ^ ((bits & 1) * 0x9908b0df);", null, 0, null);
		scTwist.addCodeLine("  }", null, 0, null);
		scTwist.addCodeLine("", null, 0, null);
		scTwist.addCodeLine("  for (int i = 227; i < 623; i++) {", null, 0, null);
		scTwist.addCodeLine("    int bits = (MT[i] & 0x80000000) | (MT[i+1] & 0x7fffffff);", null, 0, null);
		scTwist.addCodeLine("    MT[i] = MT[i - 227] ^ (bits >>> 1) ^ ((bits & 1) * 0x9908b0df);", null, 0, null);
		scTwist.addCodeLine("  }", null, 0, null);
		scTwist.addCodeLine("", null, 0, null);
		scTwist.addCodeLine("  int bits = (MT[623] & 0x80000000) | (MT[0] & 0x7fffffff);", null, 0, null);
		scTwist.addCodeLine("  MT[623] = MT[396] ^ (bits >>> 1) ^ ((bits & 1) * 0x9908b0df);", null, 0, null);
		scTwist.addCodeLine("", null, 0, null);
		scTwist.addCodeLine(" next = 0;", null, 0, null);
		scTwist.addCodeLine("}", null, 0, null);

		lang.nextStep();

		scTwist.highlight(0);

		lang.nextStep();

		scTwist.toggleHighlight(0, 1);

		lang.nextStep();

		scTwist.toggleHighlight(1, 2);

		// Question 2
		FillInBlanksQuestionModel frage2 = new FillInBlanksQuestionModel("frage2");
		frage2.setPrompt(
				"Welchen Wert (in binaer) hat bits nach dieser Zeile? Gib deine Antwort in 32 Bit beginnend mit '0b' an!");
		frage2.addAnswer("1", getBits(MT[0], MT[1]), 1,
				"Richtig! bits wird zusammengesetzt aus dem msb von MT[i] (sign bit) und den 31 lsbs von MT[i+1]");
		lang.addFIBQuestion(frage2);

		lang.nextStep();

		mt1.highlightCellColumnRange(0, 0, 31, null, null);

		lang.nextStep();

		intToBinaryArray(MT[0], row1);
		row1.show();
		row1.highlightCell(0, 31, null, null);

		lang.nextStep();

		mt1.unhighlightCellColumnRange(0, 0, 1, null, null);
		mt1.unhighlightCellColumnRange(0, 1, 31, null, null);
		row1.unhighlightCell(0, 31, null, null);

		lang.nextStep();

		intToBinaryArray(0x80000000, row2);
		row2.show();

		lang.nextStep();

		line.show();

		lang.nextStep();

		int bits = calculate("AND");

		lang.nextStep();

		row3.hide();

		lang.nextStep();

		row2.hide();

		lang.nextStep();

		mt1.highlightCellColumnRange(1, 0, 31, null, null);

		lang.nextStep();

		intToBinaryArray(MT[1], row1);
		row1.highlightCell(0, 31, null, null);

		lang.nextStep();

		mt1.unhighlightCellColumnRange(1, 0, 1, null, null);
		mt1.unhighlightCellColumnRange(1, 1, 31, null, null);
		row1.unhighlightCell(0, 31, null, null);

		lang.nextStep();

		intToBinaryArray(2147483647, row2);
		row2.show();

		lang.nextStep();

		calculate("AND");

		lang.nextStep();

		swapArrays(row1, row3);

		lang.nextStep();

		row3.hide();

		lang.nextStep();

		row2.hide();

		lang.nextStep();

		intToBinaryArray(bits, row2);
		row2.show();

		lang.nextStep();

		bits = calculate("OR");

		lang.nextStep();

		scTwist.toggleHighlight(2, 3);

		lang.nextStep();

		swapArrays(row1, row3);

		lang.nextStep();

		row3.hide();

		lang.nextStep();

		row2.hide();

		lang.nextStep();

		shiftBinaryArrayRightLogical(row1, 1);

		mt3.highlightCellColumnRange(1, 0, 31, null, null);

		lang.nextStep();

		row2.show();
		intToBinaryArray(MT[397], row2);
		row2.highlightCell(0, 31, null, null);

		lang.nextStep();

		mt3.unhighlightCellColumnRange(1, 0, 1, null, null);
		mt3.unhighlightCellColumnRange(1, 1, 31, null, null);
		row2.unhighlightCell(0, 31, null, null);

		lang.nextStep();

		s = calculate("XOR");

		lang.nextStep();

		row3.hide();

		lang.nextStep();

		row2.hide();

		lang.nextStep();

		intToBinaryArray(bits, row1);

		lang.nextStep();

		intToBinaryArray(1, row2);
		row2.show();

		lang.nextStep();

		calculate("AND");

		lang.nextStep();

		swapArrays(row1, row3);

		lang.nextStep();

		row3.hide();

		lang.nextStep();

		row2.hide();

		lang.nextStep();

		intToBinaryArray(0x9908b0df, row2);
		row2.show();

		lang.nextStep();

		calculate("MUL");

		lang.nextStep();

		swapArrays(row1, row3);

		lang.nextStep();

		row3.hide();

		lang.nextStep();

		row2.hide();

		lang.nextStep();

		intToBinaryArray(s, row2);
		row2.show();

		lang.nextStep();

		s = calculate("XOR");

		lang.nextStep();

		row3.highlightCell(0, 31, null, null);

		lang.nextStep();

		intToBinaryArrayInMatrix(binaryArrayToInt(row3), mt1, 0);

		// Calculate twist for the whole Array
		int[] twistedMT = twist(MT.clone());
		assert (s == twistedMT[0]);

		lang.nextStep();

		row3.unhighlightCell(0, 31, null, null);
		mt1.unhighlightCellColumnRange(0, 0, 1, null, null);
		mt1.unhighlightCellColumnRange(0, 1, 31, null, null);

		row3.hide();

		lang.nextStep();

		line.hide();

		lang.nextStep();

		row2.hide();

		lang.nextStep();

		row1.hide();

		lang.nextStep();

		scTwist.highlight(1);
		scTwist.highlight(2);
		scTwist.highlight(4);

		intToBinaryArrayInMatrix(twistedMT[1], mt1, 1);

		lang.nextStep();

		mt1.unhighlightCellColumnRange(1, 0, 1, null, null);
		mt1.unhighlightCellColumnRange(1, 1, 31, null, null);

		lang.nextStep();

		scTwist.unhighlight(1);
		scTwist.unhighlight(2);
		scTwist.unhighlight(3);
		scTwist.unhighlight(4);

		lang.nextStep();

		scTwist.highlight(1);
		scTwist.highlight(2);
		scTwist.highlight(3);
		scTwist.highlight(4);

		lang.nextStep();

		ellipse1.changeColor("Color", Color.RED, null, null);

		lang.nextStep();

		ellipse1.changeColor("Color", Color.BLACK, null, null);
		scTwist.unhighlight(1);
		scTwist.unhighlight(2);
		scTwist.unhighlight(3);
		scTwist.unhighlight(4);

		lang.nextStep();

		scTwist.highlight(6);

		// Question 3
		FillInBlanksQuestionModel frage3 = new FillInBlanksQuestionModel("frage3");
		frage3.setPrompt(
				"Welchen Wert (in binaer) hat bits nach dieser Zeile? Gib deine Antwort in 32 Bit beginnend mit '0b' an!");
		frage3.addAnswer("1", getBits(MT[227], MT[228]), 1,
				"Richtig! bits wird zusammengesetzt aus dem msb von MT[i] (sign bit) und den 31 lsbs von MT[i+1]");
		lang.addFIBQuestion(frage3);

		lang.nextStep();

		scTwist.toggleHighlight(6, 7);

		lang.nextStep();

		mt2.highlightCellColumnRange(0, 0, 31, null, null);

		lang.nextStep();

		intToBinaryArray(MT[227], row1);
		row1.show();
		row1.highlightCell(0, 31, null, null);

		lang.nextStep();

		mt2.unhighlightCellColumnRange(0, 0, 1, null, null);
		mt2.unhighlightCellColumnRange(0, 1, 31, null, null);
		row1.unhighlightCell(0, 31, null, null);

		lang.nextStep();

		intToBinaryArray(0x80000000, row2);
		row2.show();

		lang.nextStep();

		line.show();

		lang.nextStep();

		bits = calculate("AND");

		lang.nextStep();

		row3.hide();

		lang.nextStep();

		row2.hide();

		lang.nextStep();

		mt2.highlightCellColumnRange(1, 0, 31, null, null);

		lang.nextStep();

		intToBinaryArray(MT[228], row1);
		row1.highlightCell(0, 31, null, null);

		lang.nextStep();

		mt2.unhighlightCellColumnRange(1, 0, 1, null, null);
		mt2.unhighlightCellColumnRange(1, 1, 31, null, null);
		row1.unhighlightCell(0, 31, null, null);

		lang.nextStep();

		intToBinaryArray(2147483647, row2);
		row2.show();

		lang.nextStep();

		calculate("AND");

		lang.nextStep();

		swapArrays(row1, row3);

		lang.nextStep();

		row3.hide();

		lang.nextStep();

		intToBinaryArray(bits, row2);

		lang.nextStep();

		bits = calculate("OR");

		lang.nextStep();

		scTwist.toggleHighlight(7, 8);

		lang.nextStep();

		swapArrays(row1, row3);

		lang.nextStep();

		row3.hide();

		lang.nextStep();

		row2.hide();

		lang.nextStep();

		shiftBinaryArrayRightLogical(row1, 1);

		mt1.highlightCellColumnRange(0, 0, 31, null, null);

		lang.nextStep();

		row2.show();
		intToBinaryArray(twistedMT[0], row2);
		row2.highlightCell(0, 31, null, null);

		lang.nextStep();

		mt1.unhighlightCellColumnRange(0, 0, 1, null, null);
		mt1.unhighlightCellColumnRange(0, 1, 31, null, null);
		row2.unhighlightCell(0, 31, null, null);

		lang.nextStep();

		s = calculate("XOR");

		lang.nextStep();

		row3.hide();

		lang.nextStep();

		row2.hide();

		lang.nextStep();

		intToBinaryArray(bits, row1);

		lang.nextStep();

		intToBinaryArray(1, row2);
		row2.show();

		lang.nextStep();

		calculate("AND");

		lang.nextStep();

		swapArrays(row1, row3);

		lang.nextStep();

		row3.hide();

		lang.nextStep();

		row2.hide();

		lang.nextStep();

		intToBinaryArray(0x9908b0df, row2);
		row2.show();

		lang.nextStep();

		calculate("MUL");

		lang.nextStep();

		swapArrays(row1, row3);

		lang.nextStep();

		row3.hide();

		lang.nextStep();

		row2.hide();

		lang.nextStep();

		intToBinaryArray(s, row2);
		row2.show();

		lang.nextStep();

		s = calculate("XOR");

		lang.nextStep();

		row3.highlightCell(0, 31, null, null);

		lang.nextStep();

		intToBinaryArrayInMatrix(binaryArrayToInt(row3), mt2, 0);
		mt2.highlightCellColumnRange(0, 0, 31, null, null);

		assert (s == twistedMT[227]);

		lang.nextStep();

		row3.unhighlightCell(0, 31, null, null);
		mt2.unhighlightCellColumnRange(0, 0, 1, null, null);
		mt2.unhighlightCellColumnRange(0, 1, 31, null, null);

		row3.hide();

		lang.nextStep();

		line.hide();

		lang.nextStep();

		row2.hide();

		lang.nextStep();

		row1.hide();

		lang.nextStep();

		scTwist.highlight(6);
		scTwist.highlight(7);
		scTwist.highlight(8);
		scTwist.highlight(9);

		intToBinaryArrayInMatrix(twistedMT[228], mt2, 1);
		mt2.highlightCellColumnRange(1, 0, 31, null, null);

		lang.nextStep();

		mt2.unhighlightCellColumnRange(1, 0, 1, null, null);
		mt2.unhighlightCellColumnRange(1, 1, 31, null, null);

		lang.nextStep();

		scTwist.unhighlight(6);
		scTwist.unhighlight(7);
		scTwist.unhighlight(8);
		scTwist.unhighlight(9);

		lang.nextStep();

		scTwist.highlight(6);
		scTwist.highlight(7);
		scTwist.highlight(8);
		scTwist.highlight(9);

		lang.nextStep();

		ellipse2.changeColor("Color", Color.RED, null, null);

		lang.nextStep();

		ellipse2.changeColor("Color", Color.BLACK, null, null);
		scTwist.unhighlight(6);
		scTwist.unhighlight(7);
		scTwist.unhighlight(8);
		scTwist.unhighlight(9);

		lang.nextStep();

		scTwist.highlight(6);
		scTwist.highlight(7);
		scTwist.highlight(8);
		scTwist.highlight(9);

		intToBinaryArrayInMatrix(twistedMT[396], mt3, 0);
		mt3.highlightCellColumnRange(0, 0, 31, null, null);

		lang.nextStep();

		mt3.unhighlightCellColumnRange(0, 0, 1, null, null);
		mt3.unhighlightCellColumnRange(0, 1, 31, null, null);

		lang.nextStep();

		scTwist.unhighlight(6);
		scTwist.unhighlight(7);
		scTwist.unhighlight(8);
		scTwist.unhighlight(9);

		lang.nextStep();

		scTwist.highlight(6);
		scTwist.highlight(7);
		scTwist.highlight(8);
		scTwist.highlight(9);

		intToBinaryArrayInMatrix(twistedMT[397], mt3, 1);
		mt3.highlightCellColumnRange(1, 0, 31, null, null);

		lang.nextStep();

		mt3.unhighlightCellColumnRange(1, 0, 1, null, null);
		mt3.unhighlightCellColumnRange(1, 1, 31, null, null);

		lang.nextStep();

		scTwist.unhighlight(6);
		scTwist.unhighlight(7);
		scTwist.unhighlight(8);
		scTwist.unhighlight(9);

		lang.nextStep();

		scTwist.highlight(6);
		scTwist.highlight(7);
		scTwist.highlight(8);
		scTwist.highlight(9);

		ellipse3.changeColor("Color", Color.RED, null, null);

		lang.nextStep();

		ellipse3.changeColor("Color", Color.BLACK, null, null);
		scTwist.unhighlight(6);
		scTwist.unhighlight(7);
		scTwist.unhighlight(8);
		scTwist.unhighlight(9);

		lang.nextStep();

		scTwist.highlight(11);

		// Question 4
		FillInBlanksQuestionModel frage4 = new FillInBlanksQuestionModel("frage4");
		frage4.setPrompt(
				"Welchen Wert (in binaer) hat bits nach dieser Zeile? Gib deine Antwort in 32 Bit beginnend mit '0b' an!");
		frage4.addAnswer("1", getBits(MT[623], twistedMT[0]), 1,
				"Richtig! bits wird zusammengesetzt aus dem msb von MT[623] (sign bit) und den 31 lsbs von MT[0]");
		lang.addFIBQuestion(frage4);

		lang.nextStep();

		mt4.highlightCellColumnRange(0, 0, 31, null, null);

		lang.nextStep();

		intToBinaryArray(MT[623], row1);
		row1.show();
		row1.highlightCell(0, 31, null, null);

		lang.nextStep();

		mt4.unhighlightCellColumnRange(0, 0, 1, null, null);
		mt4.unhighlightCellColumnRange(0, 1, 31, null, null);
		row1.unhighlightCell(0, 31, null, null);

		lang.nextStep();

		intToBinaryArray(0x80000000, row2);
		row2.show();

		lang.nextStep();

		line.show();

		lang.nextStep();

		bits = calculate("AND");

		lang.nextStep();

		row3.hide();

		lang.nextStep();

		row2.hide();

		lang.nextStep();

		mt1.highlightCellColumnRange(0, 0, 31, null, null);

		lang.nextStep();

		intToBinaryArray(twistedMT[0], row1);
		row1.highlightCell(0, 31, null, null);

		lang.nextStep();

		mt1.unhighlightCellColumnRange(0, 0, 1, null, null);
		mt1.unhighlightCellColumnRange(0, 1, 31, null, null);
		row1.unhighlightCell(0, 31, null, null);

		lang.nextStep();

		intToBinaryArray(2147483647, row2);
		row2.show();

		lang.nextStep();

		calculate("AND");

		lang.nextStep();

		swapArrays(row1, row3);

		lang.nextStep();

		row3.hide();

		lang.nextStep();

		row2.hide();

		lang.nextStep();

		intToBinaryArray(bits, row2);
		row2.show();

		lang.nextStep();

		bits = calculate("OR");

		lang.nextStep();

		scTwist.toggleHighlight(11, 12);

		lang.nextStep();

		swapArrays(row1, row3);

		lang.nextStep();

		row3.hide();

		lang.nextStep();

		row2.hide();

		lang.nextStep();

		shiftBinaryArrayRightLogical(row1, 1);

		mt3.highlightCellColumnRange(0, 0, 31, null, null);

		lang.nextStep();

		row2.show();
		intToBinaryArray(twistedMT[396], row2);
		row2.highlightCell(0, 31, null, null);

		lang.nextStep();

		mt3.unhighlightCellColumnRange(0, 0, 1, null, null);
		mt3.unhighlightCellColumnRange(0, 1, 31, null, null);
		row2.unhighlightCell(0, 31, null, null);

		lang.nextStep();

		s = calculate("XOR");

		lang.nextStep();

		row3.hide();

		lang.nextStep();

		row2.hide();

		lang.nextStep();

		intToBinaryArray(bits, row1);

		lang.nextStep();

		intToBinaryArray(1, row2);
		row2.show();

		lang.nextStep();

		calculate("AND");

		lang.nextStep();

		swapArrays(row1, row3);

		lang.nextStep();

		row3.hide();

		lang.nextStep();

		row2.hide();

		lang.nextStep();

		intToBinaryArray(0x9908b0df, row2);
		row2.show();

		lang.nextStep();

		calculate("MUL");

		lang.nextStep();

		swapArrays(row1, row3);

		lang.nextStep();

		row3.hide();

		lang.nextStep();

		row2.hide();

		lang.nextStep();

		intToBinaryArray(s, row2);
		row2.show();

		lang.nextStep();

		s = calculate("XOR");

		lang.nextStep();

		row3.highlightCell(0, 31, null, null);

		lang.nextStep();

		intToBinaryArrayInMatrix(s, mt4, 0);

		assert (s == twistedMT[623]);

		lang.nextStep();

		mt4.unhighlightCellColumnRange(0, 0, 1, null, null);
		mt4.unhighlightCellColumnRange(0, 1, 31, null, null);
		row3.unhighlightCell(0, 31, null, null);

		lang.nextStep();

		row1.hide();
		row2.hide();
		row3.hide();
		operation.hide();
		line.hide();

		lang.nextStep();

		scTwist.toggleHighlight(12, 14);

		lang.nextStep("Extract");

		lang.hideAllPrimitives();

		banner2.show();
		headline2.show();

		lang.newText(new Coordinates(20, 85),
				"Zu guter Letzt wird der erste Wert aus dem Array extrahiert, nochmals 'getwisted'", "Description1",
				null, tp);
		lang.newText(new Coordinates(20, 100),
				"und die generierte Pseudozufallszahl wird zurueckgegeben. Nachdem jeder Eintrag ", "Description2", null,
				tp);
		lang.newText(new Coordinates(20, 115),
				"des Arrays extrahiert wurde, wird erneut twist() aufgerufen und das Array wechselt", "Description2",
				null, tp);
		lang.newText(new Coordinates(20, 130),
				"schalgartig in einen komplett neuen Zustand basierend den Werten des Alten, aus", "Description2", null,
				tp);
		lang.newText(new Coordinates(20, 145), "welchem sich neue Pseudozufallszahlen generieren lassen.",
				"Description2", null, tp);

		lang.nextStep();

		lang.hideAllPrimitives();
		banner1.show();
		headline1.show();
		mt1.show();
		mt2.show();
		mt3.show();
		mt4.show();
		ellipse1.show();
		ellipse2.show();
		ellipse3.show();
		indices1.show();
		indices2.show();
		indices3.show();
		indices4.show();

		scTwist.hide();

		SourceCode scExtract = lang.newSourceCode(new Coordinates(20, 330), "sourceCode", null, scSeedProps);

		scExtract.addCodeLine("public int extract_number(int[] MT) {", null, 0, null);
		scExtract.addCodeLine("  if (next >= 624)", null, 0, null);
		scExtract.addCodeLine("    twist();", null, 0, null);
		scExtract.addCodeLine("", null, 0, null);
		scExtract.addCodeLine("  int x = MT[next++];", null, 0, null);
		scExtract.addCodeLine("  x ^=  x >>> 11;", null, 0, null);
		scExtract.addCodeLine("  x ^= (x <<  7) & 0x9d2c5680;", null, 0, null);
		scExtract.addCodeLine("  x ^= (x << 15) & 0xefc60000;", null, 0, null);
		scExtract.addCodeLine("  x ^=  x >>> 18;", null, 0, null);
		scExtract.addCodeLine("", null, 0, null);
		scExtract.addCodeLine("  return x;", null, 0, null);
		scExtract.addCodeLine("}", null, 0, null);

		scExtract.show();

		lang.nextStep();

		scExtract.highlight(0);

		lang.nextStep();

		scExtract.toggleHighlight(0, 1);

		lang.nextStep();

		scExtract.toggleHighlight(1, 4);

		lang.nextStep();

		int x = twistedMT[0];
		intToBinaryArray(x, row1);
		row1.show();

		lang.nextStep();

		scExtract.toggleHighlight(4, 5);

		lang.nextStep();

		line.show();
		shiftBinaryArrayRightLogical(row1, 11);

		lang.nextStep();

		intToBinaryArray(x, row2);
		row2.show();

		lang.nextStep();

		x = calculate("XOR");

		lang.nextStep();

		scExtract.toggleHighlight(5, 6);

		lang.nextStep();

		swapArrays(row1, row3);

		lang.nextStep();

		row3.hide();

		lang.nextStep();

		row2.hide();

		lang.nextStep();

		shiftBinaryArrayLeft(row1, 7);

		intToBinaryArray(0x9d2c5680, row2);
		row2.show();

		lang.nextStep();

		calculate("AND");

		lang.nextStep();

		swapArrays(row1, row3);

		lang.nextStep();

		row3.hide();

		lang.nextStep();

		row2.hide();

		lang.nextStep();

		intToBinaryArray(x, row2);
		row2.show();

		lang.nextStep();

		x = calculate("XOR");

		lang.nextStep();

		scExtract.toggleHighlight(6, 7);

		lang.nextStep();

		swapArrays(row1, row3);

		lang.nextStep();

		row3.hide();

		lang.nextStep();

		row2.hide();

		lang.nextStep();

		shiftBinaryArrayLeft(row1, 15);

		intToBinaryArray(0xefc60000, row2);
		row2.show();

		lang.nextStep();

		calculate("AND");

		lang.nextStep();

		swapArrays(row1, row3);

		lang.nextStep();

		row3.hide();

		lang.nextStep();

		row2.hide();

		lang.nextStep();

		intToBinaryArray(x, row2);
		row2.show();

		lang.nextStep();

		x = calculate("XOR");

		lang.nextStep();

		scExtract.toggleHighlight(7, 8);

		lang.nextStep();

		swapArrays(row1, row3);

		lang.nextStep();

		row3.hide();

		lang.nextStep();

		row2.hide();

		lang.nextStep();

		shiftBinaryArrayRightLogical(row1, 18);

		lang.nextStep();

		intToBinaryArray(x, row2);
		row2.show();

		lang.nextStep();

		x = calculate("XOR");

		assert (x == extractNumber(twistedMT));

		// Question 5
		FillInBlanksQuestionModel frage5 = new FillInBlanksQuestionModel("frage5");
		frage5.setPrompt(
				"Welcher Wert (in dezimal) wurde als erste Pseudozufallszahl vom Mersenne Twister berechnet? Gib dein Ergebnis beginnend mit '0d' an und vergiss nicht, dass die Zahl hier im Zweierkomplement angegeben ist! Hol dir einen Taschenrechner ;)");
		frage5.addAnswer("1", "0d" + Integer.toString(x), 1,
				"Richtig! Diese Pseudozufallszahl wurde aus deinem gewaehlten seed (" + Integer.toString(seed)
						+ ") berechnet.");
		lang.addFIBQuestion(frage5);
		
		lang.nextStep();

		scExtract.toggleHighlight(8, 10);

		lang.newText(new Coordinates(15, 175), "= " + Integer.toString(binaryArrayToInt(row3)), "t2", null, tp);

		lang.nextStep();

		lang.hideAllPrimitives();
		scExtract.hide();

		Rect banner3 = lang.newRect(new Coordinates(20, 20), new Coordinates(230, 60), "Banner", null, rp);
		banner3.changeColor("fillColor", Color.YELLOW, null, null);
		Text headline3 = lang.newText(new Coordinates(30, 30), "Zusammenfassung", "Zusammenfassung", null, tp);
		headline3.setFont(new Font("Monospaced", Font.PLAIN, 20), null, null);

		lang.newText(new Coordinates(20, 85),
				"Seinen Namen erhaelt der Mersenne Twister durch seine Periode (Zahl ab wann sich", "Description1", null,
				tp);
		lang.newText(new Coordinates(20, 100),
				"die generierten Pseudozufallszahlen wiederholen). Diese betraegt 2^19937 - 1,", "Description2", null,
				tp);
		lang.newText(new Coordinates(20, 115),
				"also eine sogenannte Mersenne-Zahl (Primzahl der Form 2^n-1). Aufgrund dieser", "Description2", null,
				tp);
		lang.newText(new Coordinates(20, 130),
				"sehr hohen Periode, seiner hochgradigen Gleichverteilung und hoher Geschwindigkeit", "Description2",
				null, tp);
		lang.newText(new Coordinates(20, 145),
				"wird der Mersenne Twister in vielen Anwendungen verwendet. Allerdings eignet er sich", "Description2",
				null, tp);
		lang.newText(new Coordinates(20, 160),
				"nicht fuer kryptografische Anwendungen, da ein Angreifer aus einem gro� genugen", "Description2", null,
				tp);
		lang.newText(new Coordinates(20, 175),
				"(624 Iterationen in diesem Fall) Sample zukuenftige Zufallszahlen vorherbestimmen kann.",
				"Description2", null, tp);

		lang.finalizeGeneration();
		return lang.toString();
	}

	// Helper functions
	private void shiftBinaryArrayRightLogical(IntArray array, int shift) {
		operation.setText(shift + " >>>", null, null);
		operation.show();

		lang.nextStep();

		for (int i = 0; i < shift; i++) {
			for (int j = 31; j > 0; j--) {
				array.put(j, array.getData(j - 1), null, null);
			}
			array.put(0, 0, null, null);
			lang.nextStep();
		}

		operation.hide();
	}

	private void shiftBinaryArrayLeft(IntArray array, int shift) {
		operation.setText(shift + " <<", null, null);
		operation.show();

		lang.nextStep();

		for (int i = 0; i < shift; i++) {
			for (int j = 0; j < 31; j++) {
				array.put(j, array.getData(j + 1), null, null);
			}
			array.put(31, 0, null, null);
			lang.nextStep();
		}
		operation.hide();
	}

	private void intToBinaryArray(int num, IntArray array) {
		String numS = Integer.toBinaryString(num);

		for (int i = 31, b = numS.length() - 1; i >= 0; i--, b--) {
			if (b >= 0)
				array.put(i, Character.getNumericValue(numS.charAt(b)), null, null);
			else
				array.put(i, 0, null, null);
		}
	}

	private void intToBinaryArrayInMatrix(int num, IntMatrix matrix, int index) {
		String numS = Integer.toBinaryString(num);

		for (int i = 31, b = numS.length() - 1; i >= 0; i--, b--) {
			if (b >= 0)
				matrix.put(index, i, Character.getNumericValue(numS.charAt(b)), null, null);
			else
				matrix.put(index, i, 0, null, null);
		}

		matrix.highlightCellColumnRange(index, 0, 31, null, null);
	}

	private int binaryArrayToInt(IntArray array) {
		int num = 0;
		boolean signed = array.getData(0) == 1;

		for (int i = 31; i > 0; i--) {
			if ((signed && array.getData(i) == 0) || (!signed && array.getData(i) == 1))
				num = num + (int) Math.pow(2, (31 - i));
		}

		if (signed)
			return -(num + 1);
		else
			return num;
	}

	private void swapArrays(IntArray to, IntArray from) {
		from.highlightCell(0, 31, null, null);

		lang.nextStep();

		for (int i = 0; i < 32; i++) {
			to.put(i, from.getData(i), null, null);
		}
		from.unhighlightCell(0, 31, null, null);
		to.highlightCell(0, 31, null, null);

		lang.nextStep();

		to.unhighlightCell(0, 31, null, null);
	}

	private int calculate(String op) {
		operation.setText(op, null, null);
		operation.show();

		lang.nextStep();

		switch (op) {
		case "AND": {
			for (int i = 0; i < 32; i++) {
				if ((row1.getData(i) & row2.getData(i)) == 1)
					row3.put(i, 1, null, null);
				else
					row3.put(i, 0, null, null);
			}
			break;
		}
		case "XOR": {
			for (int i = 0; i < 32; i++) {
				if ((row1.getData(i) ^ row2.getData(i)) == 1)
					row3.put(i, 1, null, null);
				else
					row3.put(i, 0, null, null);
			}
			break;
		}
		case "OR": {
			for (int i = 0; i < 32; i++) {
				if ((row1.getData(i) | row2.getData(i)) == 1)
					row3.put(i, 1, null, null);
				else
					row3.put(i, 0, null, null);
			}
			break;
		}
		case "MUL": {
			int a = binaryArrayToInt(row1);
			int b = binaryArrayToInt(row2);
			intToBinaryArray(a * b, row3);
		}
		}
		row3.show();

		lang.nextStep();

		operation.hide();

		return binaryArrayToInt(row3);
	}

	// Calculate MT for assertions
	public int[] seed_mt(int seed) {
		int[] MT = new int[624];
		MT[0] = seed;

		for (int i = 1; i < 624; i++) {
			int s = MT[i - 1] ^ (MT[i - 1] >>> 30);
			MT[i] = s * 1812433253 + i;
		}

		return MT;
	}

	private String getS(int mti) {
		String result = Integer.toBinaryString((mti ^ (mti >>> 30)));
		for (int i = 0; result.length() < 32; i++) {
			result = "0" + result;
		}
		return "0b" + result;
	}

	private int[] twist(int[] MT) {
		for (int i = 0; i < 227; i++) {
			int bits = (MT[i] & 0x80000000) | (MT[i + 1] & 0x7fffffff);
			MT[i] = MT[i + 397] ^ (bits >>> 1) ^ ((bits & 1) * 0x9908b0df);
		}

		for (int i = 227; i < 623; i++) {
			int bits = (MT[i] & 0x80000000) | (MT[i + 1] & 0x7fffffff);
			MT[i] = MT[i - 227] ^ (bits >>> 1) ^ ((bits & 1) * 0x9908b0df);
		}

		int bits = (MT[623] & 0x80000000) | (MT[0] & 0x7fffffff);
		MT[623] = MT[396] ^ (bits >>> 1) ^ ((bits & 1) * 0x9908b0df);

		return MT;
	}

	private String getBits(int mti, int mti2) {
		String result = Integer.toBinaryString((mti & 0x80000000) | (mti2 & 0x7fffffff));
		for (int i = 0; result.length() < 32; i++) {
			result = "0" + result;
		}
		return "0b" + result;
	}

	public int extractNumber(int[] MT) {
		int x = MT[0];
		x ^= x >>> 11;
		x ^= (x << 7) & 0x9d2c5680;
		x ^= (x << 15) & 0xefc60000;
		x ^= x >>> 18;

		return x;
	}

	public String getName() {
		return "Mersenne Twister";
	}

	public String getAlgorithmName() {
		return "Mersenne Twister";
	}

	public String getAnimationAuthor() {
		return "Jan Philipp Wagner, Joelle Heun";
	}

	public String getDescription() {
		return "Der Mersenne Twister ist der am Weitesten verbreitete (general purpose) pseudorandom number generator und gilt zudem als einer der schnellsten. Verwendet wird er beispielsweise in Microsoft Excel, PHP, Python, MATLAB und GNU Octave.";
	}

	public String getCodeExample() {
		return "public void seed_mt(int seed) {" + "\n" + "  MT[0] = seed;" + "\n" + "  for (int i = 1; i < 624; i++) {"
				+ "\n" + "    int s = MT[i - 1] ^ (MT[i - 1] >>> 30);" + "\n"
				+ "    MT[i] = s * 1812433253 + i;\", null, 0, null)" + "\n" + "  }" + "\n" + "  twist();" + "\n" + "}"
				+ "\n" + "\n" + "private void twist() {" + "\n" + "  for (int i = 0; i < 227; i++) {" + "\n"
				+ "    int bits = (MT[i] & 0x80000000) | (MT[i+1] & 0x7fffffff);" + "\n"
				+ "    MT[i] = MT[i + 397] ^ (bits >>> 1) ^ ((bits & 1) * 0x9908b0df);" + "\n" + "  }" + "\n" + "\n"
				+ "  for (int i = 227; i < 623; i++) {" + "\n"
				+ "    int bits = (MT[i] & 0x80000000) | (MT[i+1] & 0x7fffffff);" + "\n"
				+ "      MT[i] = MT[i - 227] ^ (bits >>> 1) ^ ((bits & 1) * 0x9908b0df);" + "\n" + "  }" + "\n" + "\n"
				+ "  int bits = (MT[623] & 0x80000000) | (MT[0] & 0x7fffffff);" + "\n"
				+ "  MT[623] = MT[396] ^ (bits >>> 1) ^ ((bits & 1) * 0x9908b0df);" + "\n" + "\n" + "  next = 0;" + "\n"
				+ "}" + "\n" + "\n" + "public int extract_number(int[] MT) {" + "\n" + "  if (next >= 624)" + "\n"
				+ "    twist();" + "\n" + "\n" + "  int x = MT[next++];" + "\n" + "  x ^=  x >>> 11;" + "\n"
				+ "  x ^= (x <<  7) & 0x9d2c5680;" + "\n" + "  x ^= (x << 15) & 0xefc60000;" + "\n"
				+ "  x ^=  x >>> 18;" + "\n" + "\n" + "  return x;" + "\n" + "}";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.GERMAN;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
	}

	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

	public static void main(String[] args) {
		Generator generator = new MersenneTwisterGenerator();
		Animal.startGeneratorWindow(generator);
	}
}