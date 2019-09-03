package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import algoanim.primitives.IntMatrix;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.VisualStack;
import algoanim.primitives.generators.Language;

import java.util.Hashtable;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CounterProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class KuhnMunkres implements ValidatingGenerator {
	private int n;
	private int[] rowZeros, colZeros, selectedInRow, selectedInCol;
	private ArrayList<Integer> markedRows, markedCols;
	private Language lang;
	private MatrixProperties matrixProperties;
	private SourceCodeProperties scProperties;
	private TextProperties textProperties;
	IntMatrix costMatrix;
	SourceCode pseudoCode;
	private final String[] descriptionText = {
			"The Hungarian method is a combinatorial optimization algorithm that solves the assignment problem in polynomial time.",
			"We are given a nonnegative nxn cost-matrix where each cell contains the cost of assigning the j-th element to the i-th element.",
			"The goal is to find the best way of assigning elements to each other (1 to 1) achieving a minimal (or maximal, see final step) total cost.",
			" ",
			"The algorithm was developed by Harold Kuhn in 1955 and reviewed by James Munkres and is therefor known as the Kuhn-Munkres algorithm.",
			"It is also largely known as the Hungarian algorithm, due to the fact that it was largely based on the earlier works of two Hungarian mathematicians", };
	private final String[] complexityText = {
			"Complexity:",
			"The hungarian algorithm has in this implementation a worst case running-time function 3n³ + 2n² (without counting step 6.).",
			"The algorithm will iterate at most n times. Within the algorithm iterations the matrix will be iterated in 3 different steps, at most completely.",
			"First for step 3. (Highlighting the rows/columns containing 0's). This will require checking the whole matrix for 0's: n²",
			"Secondly for step 5. (Find smallest uncovered entry). The whole matrix must be checked to find the smallest entry: n² ",
			"And third for step 5.1 and 5.2 (add/subtract smallest entry from other entries). This can be done in the same loop: n²",
			"Per iteration we have then 3n² worst case running-time, multiplied by the worst-case amount of iterations (n) is 3n³.",
			"To this we add the row and column reductions of the beginning (2n²) and we're done. The algorithm has an asymptotic upper bound of O(n²)" };
	private final String[] otherUsesText = {
			"Other uses:",
			"The algorithm can also be used to solve the assignment problem while achieving maximal total (as opposed to minimal).",
			"For this, two additional steps are added at the beginning of the algorithm:",
			"1. Find the highest valued entry in the matrix",
			"2. Update each entries' value to be the highest value of 1. minus the original value of the entry",
			"The algorithm will then proceed to find the assignments for the minimal total loss instead of minimal total cost." };
	private Text highlightedLinesText, traversedText, changedText;
	int matrixIterations, entryOperations;

	public void init() {
		lang = new AnimalScript("Hungarian Method",
				"Nicolas Morew, Melissa Mendoza", 800, 600);
		lang.setStepMode(true);
	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		matrixProperties = (MatrixProperties) props
				.getPropertiesByName("matrix");
		scProperties = (SourceCodeProperties) props
				.getPropertiesByName("pseudoCode");
		textProperties = (TextProperties) props
				.getPropertiesByName("text");
		Font pseudoFont = (Font) scProperties.get("font");
		boolean pseudoBold = (Boolean) scProperties.get("bold");
		int pseudoSize = (Integer) scProperties.get("size");
		pseudoFont = new Font(pseudoFont.getFamily(), (pseudoBold) ? Font.BOLD
				: Font.PLAIN, pseudoSize);
		scProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, pseudoFont);

		// intro screen text
		TextProperties headerProps = new TextProperties();
		Font textFont = (Font) textProperties.get("font");
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				textFont.getFamily(), Font.BOLD, 28));
		headerProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
		RectProperties hRectProps = (RectProperties) props.getPropertiesByName("titleBackground");
		Text header = lang.newText(new Coordinates(20, 35),
				"Kuhn-Munkres Algorithm", "header", null, headerProps);
		Rect hRect = lang.newRect(new Offset(-5, -5, "header",
				AnimalScript.DIRECTION_NW), new Offset(5, 5, "header", "SE"),
				"hRect", null, hRectProps);
		textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.PLAIN, 16));
		lang.newText(new Coordinates(20, 70), descriptionText[0],
				"description0", null, textProperties);
		for (int i = 1; i < descriptionText.length; i++)
			lang.newText(new Offset(0, 25, "description" + (i - 1),
					AnimalScript.DIRECTION_NW), descriptionText[i],
					"description" + i, null, textProperties);
		lang.nextStep("Start");
		lang.hideAllPrimitives();
		header.show();
		hRect.show();
		// initialize algorithm values
		int[][] intMatrix = (int[][]) primitives.get("costMatrix");
		n = intMatrix.length;
		int[][] originalMatrix = new int[n][n];
		for (int r = 0; r < n; r++)
			for (int c = 0; c < n; c++)
				originalMatrix[r][c] = intMatrix[r][c];
		costMatrix = lang.newIntMatrix(new Coordinates(35, 75),
				(int[][]) primitives.get("costMatrix"), "matrix", null,
				matrixProperties);
		pseudoCode = lang.newSourceCode(new Coordinates(80 + (n * 25), 65),
				"pseudoCode", null, scProperties);
		pseudoCode.addCodeLine("1. For each row in the matrix", "1", 0, null); // 0
		pseudoCode.addCodeLine("1.1. Find smallest entry in row", "1.1", 1,
				null);
		pseudoCode.addCodeLine("1.2. Subtract it from all entries in row",
				"1.2", 1, null);
		pseudoCode
				.addCodeLine("2. For each column in the matrix", "2", 0, null); // 0
		pseudoCode.addCodeLine("2.1. Find smallest entry in column", "2.1", 1,
				null);
		pseudoCode.addCodeLine("2.2. Subtract it from all entries in column",
				"2.2", 1, null);
		pseudoCode
				.addCodeLine(
						"3. Find minimal combination of rows/columns that covers all the zero-entries of the matrix",
						"3", 0, null); // 2
		pseudoCode
				.addCodeLine(
						"4. Test for Optimality: (If the amount of rows/columns used for 3. == n)",
						"4", 0, null); // 3
		pseudoCode.addCodeLine("4.1 True: optimal, go to 6.", "4.1", 1, null); // 4
		pseudoCode.addCodeLine("4.2 False: proceed with 5:", "4.2", 1, null); // 5
		pseudoCode.addCodeLine("5. Find smallest uncovered in matrix.", "5", // 6
				0, null);
		pseudoCode.addCodeLine("5.1 Subtract it from all uncovered entries.", // 7
				"5.1", 1, null);
		pseudoCode.addCodeLine("5.2 And add it to each entry covered twice.", // 8
				"5.2", 1, null);
		pseudoCode.addCodeLine("5.3 Go to 3.", "5.3", 1, null); // 9
		pseudoCode
				.addCodeLine(
						"6. Select a zero-entry for each row so that all rows have one. (using a backtracking algorithm for example)", // 10
						"6", 0, null); //
		markedRows = new ArrayList<Integer>();
		markedCols = new ArrayList<Integer>();
		rowZeros = new int[n];
		colZeros = new int[n];
		// textProperties.set(AnimationPropertiesKeys.BORDER_PROPERTY, true);
		lang.newText(new Coordinates(240 + (n * 25) + pseudoSize * 12, 80),
				"Info: ", "infoTitle", null, textProperties);
		lang.newText(new Offset(0, 15, "infoTitle", AnimalScript.DIRECTION_NW),
				"n: " + n, "nText", null, textProperties);
		highlightedLinesText = lang.newText(new Offset(0, 15, "nText",
				AnimalScript.DIRECTION_NW), "marked rows/columns: 0", "hlText",
				null, textProperties);
		traversedText = lang.newText(new Offset(0, 15, "hlText",
				AnimalScript.DIRECTION_NW), "entries traversed: 0", "tText",
				null, textProperties);
		changedText = lang.newText(new Offset(0, 15, "tText",
				AnimalScript.DIRECTION_NW), "changes to entries: 0", "cText",
				null, textProperties);
		lang.newRect(
				new Offset(-5, -1, "infoTitle", AnimalScript.DIRECTION_NW),
				new Offset(45, 1, "cText", "SE"), "infoRect", null,
				new RectProperties());

		matrixIterations = 0;
		entryOperations = 0;
		// algorithm start
		lang.nextStep("1. Row reduction");
		pseudoCode.highlight("1");
		for (int row = 0; row < n; ++row) {
			lang.nextStep("1.1 Find row minima");
			pseudoCode.highlight("1.1");
			pseudoCode.unhighlight("1.2");
			int rowMin = Integer.MAX_VALUE;
			int rowMinPos = -1;
			for (int col = 0; col < n; ++col) {
				incTraversals();
				int cell = costMatrix.getElement(row, col);
				if (cell < rowMin) {
					rowMin = cell;
					rowMinPos = col;
				}
			}
			costMatrix.highlightElem(row, rowMinPos, null, null);
			lang.nextStep("1.2 Subtract row minima from row");
			pseudoCode.unhighlight("1.1");
			pseudoCode.highlight("1.2");
			if (rowMin != 0)
				for (int col = 0; col < n; ++col) {
					costMatrix.put(row, col, costMatrix.getElement(row, col)
							- rowMin, null, null);
					incTraversals();
					incChanges();
					if (costMatrix.getElement(row, col) == 0) {
						rowZeros[row]++;
						colZeros[col]++;
					}
				}
			costMatrix.unhighlightElem(row, rowMinPos, null, null);
		}

		// --== Column Reduction ==--
		lang.nextStep("2. Column reduction");
		pseudoCode.unhighlight("1");
		pseudoCode.unhighlight("1.2");
		pseudoCode.highlight("2");
		for (int col = 0; col < n; ++col) {
			lang.nextStep("2.1 Find column minima");
			pseudoCode.highlight("2.1");
			pseudoCode.unhighlight("2.2");
			int colMin = Integer.MAX_VALUE;
			int colMinPos = -1;
			for (int row = 0; row < n; ++row) {
				incTraversals();
				int cell = costMatrix.getElement(row, col);
				if (cell < colMin) {
					colMin = cell;
					colMinPos = row;
				}
			}
			costMatrix.highlightElem(colMinPos, col, null, null);
			lang.nextStep("2.2 Subtract column minima from column");
			pseudoCode.unhighlight("2.1");
			pseudoCode.highlight("2.2");
			for (int row = 0; row < n; ++row) {
				if (colMin != 0) {
					incTraversals();
					incChanges();
					costMatrix.put(row, col, costMatrix.getElement(row, col)
							- colMin, null, null);
				}
				if (costMatrix.getElement(row, col) == 0) {
					rowZeros[row]++;
					colZeros[col]++;
				}
			}
			costMatrix.unhighlightElem(colMinPos, col, null, null);
		}
		// Step 3: Mark all the 0's in the Matrix with as few highlighted lines
		// as possible
		lang.nextStep("3. Setup for optimality test");
		pseudoCode.unhighlight("2");
		pseudoCode.unhighlight("2.2");
		pseudoCode.highlight("3");
		highlightLines();
		lang.nextStep("4. Test for optimality");
		pseudoCode.unhighlight("3");
		pseudoCode.highlight("4");
		Text ifText = lang
				.newText(
						new Coordinates(180 + (n * 25) + pseudoSize * 12,
								75 + 11 * pseudoSize),
						(markedRows.size() + markedCols.size())
								+ (((markedRows.size() + markedCols.size()) == n) ? " == "
										: " ≠ ") + n, "ifText", null,
						new TextProperties());
		RectProperties ifRectProps = new RectProperties();
		ifRectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		ifRectProps.set(AnimationPropertiesKeys.FILL_PROPERTY,
				((markedRows.size() + markedCols.size() == n) ? Color.GREEN
						: Color.RED));
		ifRectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		Rect ifRect = lang.newRect(new Offset(-5, -1, "ifText",
				AnimalScript.DIRECTION_NW), new Offset(5, 1, "ifText", "SE"),
				"hRect", null, ifRectProps);

		// ---=== WHILE ===---
		int iterations = 0;
		while (markedRows.size() + markedCols.size() < n) {
			iterations++;
			lang.nextStep("4.2 not optimal");
			pseudoCode.highlight("4.2");
			lang.nextStep("5. Find smalles uncovered entry");
			ifText.hide();
			ifRect.hide();
			pseudoCode.unhighlight("4");
			pseudoCode.unhighlight("4.2");
			pseudoCode.highlight("5");
			int minVal = Integer.MAX_VALUE;
			int[] minValPos = new int[2];
			for (int row = 0; row < n; row++) {
				if (markedRows.contains(row)) {
					incTraversals();
					continue;
				}
				for (int col = 0; col < n; col++) {
					incTraversals();
					if (markedCols.contains(col))
						continue;
					if (costMatrix.getElement(row, col) < minVal) {
						minVal = costMatrix.getElement(row, col);
						minValPos[0] = row;
						minValPos[1] = col;
					}
				}

			}
			costMatrix.highlightElem(minValPos[0], minValPos[1], null, null);
			lang.nextStep("5.1 Subtract it from all uncovered entries");
			pseudoCode.highlight("5.1");
			for (int row = 0; row < n; row++) {
				for (int col = 0; col < n; col++) {
					incTraversals();
					if (!markedRows.contains(row) && !markedCols.contains(col)) {
						incChanges();
						costMatrix.put(row, col,
								costMatrix.getElement(row, col) - minVal, null,
								null);
						if (costMatrix.getElement(row, col) == 0) {
							rowZeros[row]++;
							colZeros[col]++;
						}
					}
				}
			}
			lang.nextStep("5.2 Add it to entries covered twice");
			pseudoCode.unhighlight("5.1");
			pseudoCode.highlight("5.2");
			for (int row = 0; row < n; row++) {
				for (int col = 0; col < n; col++) {
					if (markedRows.contains(row) && markedCols.contains(col)) {
						incChanges();
						costMatrix.put(row, col,
								costMatrix.getElement(row, col) + minVal, null,
								null);
					}
				}
			}
			lang.nextStep("5.3 Return to 3. (iteration end)");
			costMatrix.unhighlightElem(minValPos[0], minValPos[1], null, null);
			pseudoCode.unhighlight("5.2");
			pseudoCode.highlight("5.3");
			lang.nextStep("3. Setup for optimality test");
			pseudoCode.unhighlight("5");
			pseudoCode.unhighlight("5.3");
			pseudoCode.highlight("3");
			highlightLines();
			lang.nextStep("4. Test for optimality");

			ifText.setText((markedRows.size() + markedCols.size())
					+ (((markedRows.size() + markedCols.size()) == n) ? " == "
							: " != ") + n, null, null);
			ifRectProps.set(AnimationPropertiesKeys.FILL_PROPERTY,
					((markedRows.size() + markedCols.size() == n) ? Color.GREEN
							: Color.RED));
			if (markedRows.size() + markedCols.size() == n) {
				ifRectProps.set(AnimationPropertiesKeys.FILL_PROPERTY,
						Color.GREEN);
				ifRect = lang.newRect(new Offset(-5, -1, "ifText",
						AnimalScript.DIRECTION_NW), new Offset(5, 1, "ifText",
						"SE"), "hRect", null, ifRectProps);

			} else
				ifRect.show();
			ifText.show();
			pseudoCode.unhighlight("3");
			pseudoCode.highlight("4");
		}
		lang.nextStep("4.1 optimal");
		pseudoCode.highlight("4.1");
		lang.nextStep("6. Assign pairs");
		ifText.hide();
		ifRect.hide();
		pseudoCode.unhighlight("4");
		pseudoCode.unhighlight("4.1");
		pseudoCode.highlight("6");

		// select optimals
		selectedInRow = new int[n];
		selectedInCol = new int[n];
		Arrays.fill(selectedInRow, -1);
		Arrays.fill(selectedInCol, -1);
		selectOptimals(0);
		costMatrix.hide();
		costMatrix = lang.newIntMatrix(new Coordinates(35, 75), intMatrix,
				"matrix", null, matrixProperties);

		for (int row = 0; row < n; row++) {
			costMatrix.highlightCell(row, selectedInRow[row], null, null);
		}
		lang.nextStep();
		costMatrix.hide();
		IntMatrix endMatrix = lang.newIntMatrix(new Coordinates(35, 75),
				originalMatrix, "matrix", null, matrixProperties);
		for (int row = 0; row < n; row++) {
			endMatrix.highlightCell(row, selectedInRow[row], null, null);
		}

		pseudoCode
				.addCodeLine(
						"The positions of the selected zero's in step 6 will be optimal assignments",
						"end", 0, null);
		pseudoCode.unhighlight("6");
		pseudoCode.highlight("end");
		lang.nextStep();
		lang.hideAllPrimitives();
		header.show();
		hRect.show();
		endMatrix.hide();
		// Complexity
		TextProperties bold = new TextProperties();
		bold.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.BOLD, 17));
		lang.newText(new Coordinates(20, 70), complexityText[0], "cText0",
				null, bold);
		for (int i = 1; i < complexityText.length; i++)
			lang.newText(new Offset(0, 20, "cText" + (i - 1),
					AnimalScript.DIRECTION_NW), complexityText[i], "cText" + i,
					null, textProperties);
		// Stats
		lang.newText(new Offset(0, 50, "cText" + (complexityText.length - 1),
				AnimalScript.DIRECTION_NW), "Some statistics:", "statText0",
				null, bold);
		lang.newText(
				new Offset(0, 20, "statText0", AnimalScript.DIRECTION_NW),
				"The algorithm required "
						+ iterations
						+ " iterations after the reduction steps (1, 2) to complete",
				"statText1", null, textProperties);
		lang.newText(new Offset(0, 20, "statText1", AnimalScript.DIRECTION_NW),
				"A total of " + entryOperations
						+ " changes were performed on the array entries",
				"statText2", null, textProperties);
		lang.newText(
				new Offset(0, 20, "statText2", AnimalScript.DIRECTION_NW),
				matrixIterations
						+ " iteration steps were performed over the matrix entries in total",
				"statText2", null, textProperties);
		// Other uses
		lang.newText(new Offset(0, 70, "statText2", AnimalScript.DIRECTION_NW),
				otherUsesText[0], "oUText0", null, bold);
		for (int i = 1; i < otherUsesText.length; i++)
			lang.newText(new Offset(0, 25, "oUText" + (i - 1),
					AnimalScript.DIRECTION_NW), otherUsesText[i], "oUText" + i,
					null, textProperties);

		return lang.toString().replaceAll(" refresh", "");
	}

	private void highlightLines() {
		lang.addLine("# Highlighting Rows/Cols");
		for (int row = 0; row < n; row++) {
			if (markedRows.contains(row))
				continue;
			for (int col = 0; col < n; col++) {
				if (markedCols.contains(col))
					continue;
				if (costMatrix.getElement(row, col) == 0) {
					if (rowZeros[row] > colZeros[col]) {
						markedRows.add(row);
						lang.nextStep();
						lang.addLine("# Highlighting row " + row);
						/*
						 * costMatrix.highlightCellColumnRange(0, n - 1, row,
						 * null, null);
						 */
						lang.addLine("highlightGridCell \"matrix[" + row
								+ "][]\"");
						highlightedLinesText.setText("marked rows/columns: "
								+ (markedCols.size() + markedRows.size()),
								null, null);
						break;
					} else {
						markedCols.add(col);
						lang.nextStep();
						lang.addLine("# Highlighting column " + col);
						/*
						 * costMatrix.highlightCellRowRange(0, n - 1, col, null,
						 * null);
						 */
						lang.addLine("highlightGridCell \"matrix[][" + col
								+ "]\"");
						highlightedLinesText.setText("marked rows/columns: "
								+ (markedCols.size() + markedRows.size()),
								null, null);
					}
				}
			}
		}
	}

	private boolean selectOptimals(int row) {
		boolean possible = false;
		for (int col = 0; col < n; col++) {
			if (costMatrix.getElement(row, col) == 0
					&& selectedInCol[col] == -1) {
				selectedInRow[row] = col;
				selectedInCol[col] = row;
				if (row < n - 1)
					possible = selectOptimals(row + 1);
				else
					possible = true;
				if (possible)
					break;
				else {
					selectedInRow[row] = -1;
					selectedInCol[col] = -1;
				}
			}
		}
		return possible;
	}

	private void incTraversals() {
		matrixIterations++;
		traversedText.setText("entries traversed: " + matrixIterations, null,
				null);
	}

	private void incChanges() {
		entryOperations++;
		changedText.setText("changes to entries: " + entryOperations, null,
				null);
	}

	public String getName() {
		return "Hungarian Method";
	}

	public String getAlgorithmName() {
		return "Kuhn-Munkres Algorithm";
	}

	public String getAnimationAuthor() {
		return "Nicolas Morew, Melissa Mendoza";
	}

	public String getDescription() {
		String returned = "";
		for (int i = 0; i < descriptionText.length; i++) {
			returned += descriptionText[i] + "<br>";
		}
		return returned;
	}

	public String getCodeExample() {
		return "1. Subtract the smallest entry in each row from all the entries of that row."
				+ "\n"
				+ "2. Subtract the smallest entry in each column from all the entries of that column."
				+ "\n"
				+ "3. Find minimal combination of rows/columns that covers all the zero-entries of the matrix"
				+ "\n"
				+ "4. Test for Optimality:"
				+ "\n"
				+ "4.1 If the amount of rows/columns used for 3. == n, go to 6."
				+ "\n"
				+ "4.2 Otherwise proceed with 5:"
				+ "\n"
				+ "5. Find smallest uncovered entry in matrix"
				+ "\n"
				+ "   Subtract it from other uncovered entries and add it to each entry covered twice."
				+ "\n"
				+ "   Go to 3."
				+ "\n"
				+ "6. Select a zero-entry for each row so that all rows have one.";
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

	public boolean validateInput(AnimationPropertiesContainer arg0,
			Hashtable<String, Object> arg1) throws IllegalArgumentException {
		int[][] costMatrix = (int[][]) arg1.get("costMatrix");
		if (costMatrix.length != costMatrix[0].length) {
			throw new IllegalArgumentException("The matrix must be of n*n size!");
		}
		for (int r = 0; r < costMatrix.length; r++)
			for (int c = 0; c < costMatrix.length; c++)
				if (costMatrix[r][c] < 0) {
					throw new IllegalArgumentException("All values of the matrix must be >= 0!");
				}
		return true;
	}

}