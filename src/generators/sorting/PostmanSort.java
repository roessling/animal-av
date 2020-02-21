/*
 * PostmanSort.java
 * Leon Würsching, Antonia Wüst, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.sorting;

import generators.framework.Generator;
import generators.framework.ValidatingGenerator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.*;
import algoanim.primitives.*;
import algoanim.counter.model.TwoValueCounter;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import animal.main.Animal;
import java.awt.*;
import java.util.*;
import algoanim.primitives.generators.Language;

public class PostmanSort implements ValidatingGenerator {
	private static final String PR_DESCRIPTION = "desc";
	private static final String PR_ALGORITHM = "code";
	private static final String PR_MATRIX = "matrix";
	private static final String PR_MATRIX_TITLE = "matrix_title";
	private static final String PR_SUMMARY = "summary";
	private static final String PR_TITLE = "title";
	private static final String PR_TITLE_RECT = "titleRect";
	private static final String PR_EXPLANATION = "explanation";
	private static final String PR_BUCKET_MATRIX = "bucket_matrix";
	private static final String PR_BUCKET_NR = "bucket_nr_";
	private static final String PR_ARRAY = "array";
	private static final String PR_ARRAY_TEXT = "arraytext";

	private static Language lang;
	private SourceCodeProperties sourceCodeProps;
	private ArrayProperties arrayProps;
	private TextProperties titleProps;
	private TextProperties textProps;
	private MatrixProperties matrixProps;
	private RectProperties rectProps;
	private int[][] inputMatrix;
	private Map<String, Primitive> primitives = new HashMap<>();

	public void init() {
		lang = new AnimalScript("Postman's Sort", "Leon W\u00fcrsching, Antonia W\u00fcst", 800, 600);
		lang.setStepMode(true);
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		int[][] inputMatrix = (int[][]) primitives.get("inputMatrix");
		if (getMaxValue(inputMatrix) <= 9 && inputMatrix.length < 7 && inputMatrix[0].length < 6)
			return true;
		else
			return false;
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		sourceCodeProps = (SourceCodeProperties) props.getPropertiesByName("sourceCode");
		arrayProps = (ArrayProperties) props.getPropertiesByName("array");
		titleProps = (TextProperties) props.getPropertiesByName("title");
		textProps = (TextProperties) props.getPropertiesByName("text");
		matrixProps = (MatrixProperties) props.getPropertiesByName("matrix");
		rectProps = (RectProperties) props.getPropertiesByName("rectProps");
		inputMatrix = (int[][]) primitives.get("inputMatrix");

		doPostman(inputMatrix, getMaxValue(inputMatrix));
		return lang.toString();
	}

	public String getName() {
		return "Postman's Sort";
	}

	public String getAlgorithmName() {
		return "Postman's Sort";
	}

	public String getAnimationAuthor() {
		return "Leon W\u00fcrsching, Antonia W\u00fcst";
	}

	public String getDescription() {
		return "Der Postman's Sort Algorithmus ist ein Algorithmus, der vor allem im Postwesen eingesetzt wird. In diesem Kontext sortiert "
				+ "er Elemente zuerst nach Staat, dann nach Poststelle und dann nach Route." + "\n"
				+ "In der Animation wird eine vereinfachten Version mit Integerelemente gezeigt, die die Elemente nach Ihrer Gr&ouml;&szlig;e sortiert. Die Komplexit&auml;t des Algorithmus betr&auml;gt O(n).";
	}

	public String getCodeExample() {
		return "code lines";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.GERMAN;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
	}

	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

	private int getMaxValue(int[][] matrix) {
		int maxvalue = 0;
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				if (matrix[i][j] > maxvalue)
					maxvalue = matrix[i][j];
			}
		}
		return maxvalue;
	}

	/*
	 * Draw methods
	 */
	private void drawRectangle(Primitive p1, Primitive p2, String id) {
		Node nw = new Offset(-5, -5, p1, AnimalScript.DIRECTION_NW);
		Node se = new Offset(5, 5, p2, AnimalScript.DIRECTION_SE);
		Primitive rect = lang.newRect(nw, se, PR_TITLE_RECT, null, rectProps);

		primitives.put(id, rect);
	}

	private void showTitleAndDescription() {
		/*
		 * Show title
		 */
		TextProperties myTitleProps = titleProps;
		myTitleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 20));
		Text title = lang.newText(new Coordinates(10, 10), getName(), PR_TITLE, null, myTitleProps);
		/*
		 * Draw rectangle around title
		 */
		drawRectangle(title, title, PR_TITLE_RECT);

		/*
		 * Show description
		 */
		SourceCode desc = lang.newSourceCode(new Offset(0, 20, PR_TITLE, AnimalScript.DIRECTION_SW), PR_DESCRIPTION,
				null, sourceCodeProps);
		desc.addCodeLine(
				"Der Postman's Sort Algorithmus ist ein Algorithmus, der vor allem im Postwesen eingesetzt wird.", null,
				0, null);
		desc.addCodeLine(
				"In diesem Kontext sortiert er Elemente zuerst nach Staat, dann nach Poststelle und dann nach Route. ",
				null, 0, null);
		desc.addCodeLine(
				"In dieser vereinfachten Version werden die Integerelemente nach Ihrer Gr\u00f6\u00dfe sortiert.", null,
				0, null);
		desc.addCodeLine("Die Komplexit\u00e4t des Algorithmus betr\u00e4gt O(n).", null, 0, null);
		desc.addCodeLine("Dabei h\u00e4ngt c von der Gr\u00f6\u00dfe der Zahlen ab und der Anzahl an Buckets.", null, 0,
				null);
		primitives.put(PR_TITLE, title);
		primitives.put(PR_DESCRIPTION, desc);
	}

	private void showAlgorithm() {
		/*
		 * Create empty explanation
		 */
		Text explanation = (Text) lang.newText(new Offset(400, 0, PR_MATRIX_TITLE, AnimalScript.DIRECTION_NE), "",
				PR_EXPLANATION, null, textProps);
		primitives.put(PR_EXPLANATION, explanation);
		/*
		 * Show algorithm
		 */
		SourceCode algorithm = lang.newSourceCode(new Offset(0, 5, PR_EXPLANATION, AnimalScript.DIRECTION_SW),
				PR_ALGORITHM, null, sourceCodeProps);
		algorithm.addCodeLine(
				" public void postmansSort(int[][] matrix, int currentCol, int rowMin, int rowMax, int maxValue) {",
				null, 0, null);
		algorithm.addCodeLine("if (currentCol == matrix.length) return;", null, 1, null);
		algorithm.addCodeLine("int[] bucketCounter = new int[maxValue + 1];", null, 1, null);
		algorithm.addCodeLine("int[][][]bucket = new int [maxValue+1][rowMax-rowMin][matrix.length-currentCol];", null,
				1, null);
		algorithm.addCodeLine("for(int r = rowMin; r < rowMax; r++) {", null, 1, null);
		algorithm.addCodeLine("int currentValue = matrix[r][currentCol];", null, 2, null);
		algorithm.addCodeLine("int bucketRow = bucketCounter[currentValue];", null, 2, null);
		algorithm.addCodeLine(
				"System.arraycopy(matrix[r], currentCol, bucket[currentValue][bucketRow], 0, matrix[r].length-currentCol-1);",
				null, 2, null);
		algorithm.addCodeLine("bucketCounter[currentValue]++;", null, 2, null);
		algorithm.addCodeLine("}", null, 1, null);
		algorithm.addCodeLine("int matrixCounter = 0;", null, 1, null);
		algorithm.addCodeLine("for(int i = 0; i < bucket.length; i++) {", null, 1, null);
		algorithm.addCodeLine("int rowStart = rowMin, rowEnd = rowMax;", null, 2, null);
		algorithm.addCodeLine("for(int r = 0; r < bucketCounter[i] && matrixCounter < matrix.length; r++) {", null, 2,
				null);
		algorithm.addCodeLine(
				"System.arraycopy(bucket[i][r], 0, matrix[matrixCounter], currentCol, bucket[i][r].length);", null, 3,
				null);
		algorithm.addCodeLine("matrixCounter++;", null, 3, null);
		algorithm.addCodeLine("rowEnd++;", null, 3, null);
		algorithm.addCodeLine("}", null, 2, null);
		algorithm.addCodeLine("if(bucketCounter[i] > 1) {", null, 2, null);
		algorithm.addCodeLine("	postmansSort(matrix, maxvalue, currentCol+1, rowStart, rowEnd);", null, 3, null);
		algorithm.addCodeLine("}", null, 2, null);
		algorithm.addCodeLine("rowEnd++;", null, 2, null);
		algorithm.addCodeLine("rowStart = rowEnd;", null, 2, null);
		algorithm.addCodeLine("}", null, 1, null);
		algorithm.addCodeLine("}", null, 0, null);

		primitives.put(PR_ALGORITHM, algorithm);
	}

	private void showMatrix(int[][] inputMatrix) {
		/*
		 * Show matrix
		 */
		Text matrixTitle = lang.newText(new Offset(20, 20, PR_TITLE, AnimalScript.DIRECTION_SW), "Matrix",
				PR_MATRIX_TITLE, null, textProps);
		primitives.put(PR_MATRIX_TITLE, matrixTitle);
		IntMatrix matrix = lang.newIntMatrix(new Offset(20, 45, PR_TITLE, AnimalScript.DIRECTION_SW), inputMatrix,
				PR_MATRIX, null, matrixProps);
		primitives.put(PR_MATRIX, matrix);
	}

	private void showCounter(IntMatrix matrix) {
		/*
		 * Create counter
		 */
		TwoValueCounter counter = lang.newCounter(matrix);
		CounterProperties counterProps = new CounterProperties();
		counterProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		counterProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);
		/*
		 * Create counter view
		 */
		lang.newCounterView(counter, new Offset(0, 10, PR_MATRIX, AnimalScript.DIRECTION_SW), counterProps, true,
				true);

	}

	private void showExplanation(int i) {
		String e;
		switch (i) {
		case 0:
			e = "Rufe postmansSort auf mit der Matrix, der aktuellen Spalte, dem Sortierfenster und dem maximalen Zahlenwert.";
			break;
		case 1:
			e = "\u00dcberpr\u00fcfe, ob es noch Spalten zum Sortieren gibt. Falls nein, beende.";
			break;
		case 2:
			e = "Erstelle Array zum Festhalten der Anzahl an Elementen im Bucket.";
			break;
		case 3:
			e = "Erstelle Buckets zum Sortieren.";
			break;
		case 4:
			e = "Gehe die Zeilen der Matrix innerhalb des \u00fcbergebenem Sorting Windows durch.";
			break;
		case 5:
			e = "Hole den Wert der aktuellen Spalte und Zeile";
			break;
		case 6:
			e = "Finde den n\u00e4chsten freien Platz im dazugeh\u00f6rigen Bucket.";
			break;
		case 7:
			e = "Kopiere die Elemente der Matrix in den entsprechenden Bucket.";
			break;
		case 8:
			e = "Erh\u00f6he den bucketCounter des Buckets.";
			break;
		case 10:
			e = "Initialisiere einen matrixCounter um die Elemente in die Matrix zur\u00fcckzusortieren.";
			break;
		case 11:
			e = "Gehe rekursiv durch die Buckets durch, um sie in die Matrix zur\u00fcckzukopieren.";
			break;
		case 12:
			e = "Setze rowStart und rowEnd f\u00fcr das Sortierfenster.";
			break;
		case 13:
			e = "Gehe den Bucket durch, falls er nicht leer ist.";
			break;
		case 14:
			e = "Kopiere die Bucket Reihe in die Matrix zur\u00fcck.";
			break;
		case 15:
			e = "Erh\u00f6he den matrixCounter.";
			break;
		case 16:
			e = "Vergr\u00f6\u00dfere das Sortierfenster um eins.";
			break;
		case 18:
			e = "Checke, ob der Bucket mehr als ein Element beihnhaltet.";
			break;
		case 19:
			e = "Falls ja, sortiere den Bucket mit Postman's Sort erneut.";
			break;
		case 21:
		case 22:
			e = "\u00d6ffne ein neues Sortierfenster f\u00fcr den n\u00e4chsten Bucket.";
			break;
		default:
			e = "";
			break;
		}
		Text explanation = (Text) primitives.get(PR_EXPLANATION);
		explanation.setText(e, null, null);

	}

	private IntArray showBucketCounter(int[] counter) {
		Text anBucketCounterText = lang.newText(new Offset(0, 70, PR_MATRIX, AnimalScript.DIRECTION_SW),
				"bucketCounter[]", PR_ARRAY_TEXT, null);
		IntArray anBucketCounter = lang.newIntArray(new Offset(0, 10, PR_ARRAY_TEXT, AnimalScript.DIRECTION_SW),
				counter, PR_ARRAY, null, arrayProps);
		primitives.put(PR_ARRAY, anBucketCounter);
		primitives.put(PR_ARRAY_TEXT, anBucketCounterText);
		return anBucketCounter;
	}

	private IntMatrix[] generateBuckets(int rmin, int rmax, int matrixColumns, int c, int maxValue) {
		/*
		 * Generate bucket for every possible bucket
		 */
		IntMatrix[] anBucket = new IntMatrix[maxValue + 1];
		for (int i = 0; i < maxValue + 1; i++) {
			int bucketElement[][] = new int[rmax - rmin][c];
			// setDefaultValues(bucketElement);
			IntMatrix bucketMatrix = lang.newIntMatrix(
					new Offset((i % 3) * 110, 50 + ((i / 3) * (rmax-rmin) * 34), PR_ARRAY, AnimalScript.DIRECTION_SW),
					bucketElement, PR_BUCKET_MATRIX + i, null, matrixProps);
			anBucket[i] = bucketMatrix;
			primitives.put(PR_BUCKET_MATRIX + i + rmin + c, bucketMatrix);
		}
		return anBucket;
	}

	private Text[] generateBucketDescriptions(int rmin, int rmax, int matrixColumns, int c, int maxValue) {
		Text[] anBucketDescription = new Text[maxValue + 1];
		for (int i = 0; i < maxValue + 1; i++) {
			Text description = lang.newText(
					new Offset((i % 3) * 110, 30 + ((i / 3) * (rmax-rmin) * 34), PR_ARRAY, AnimalScript.DIRECTION_SW), "Bucket " + i,
					PR_BUCKET_NR + i, null, textProps);
			anBucketDescription[i] = description;
			primitives.put(PR_BUCKET_NR + i + rmin + c, description);
		}
		return anBucketDescription;
	}

	private void showSummary() {
		SourceCode summary = lang.newSourceCode(new Offset(0, 20, PR_TITLE, AnimalScript.DIRECTION_SW), PR_SUMMARY,
				null, sourceCodeProps);
		summary.addCodeLine("Die Matrix wurde mithilfe von Postman's Sort sortiert. ", null, 0, null);
		summary.addCodeLine("Die erste Spalte wurde in die Buckets einsortiert und wenn am Ende", null, 0, null);
		summary.addCodeLine("mehr als ein Element in einem Bucket war, wurde dieser Bucket so tief weiter aufgeteilt,",
				null, 0, null);
		summary.addCodeLine("bis alle Elemente wieder zur\u00fcck in die Matrix \u00fcbertragen werden konnten.", null,
				0, null);
		summary.addCodeLine(
				"Der Algorithmus versucht damit, das nat\u00fcrliche Sortierverhalten wie z.B. im Postwesen wiederzuspiegeln.",
				null, 0, null);
		summary.addCodeLine(
				"Die gezeigte Version des Algorithmus ist nicht als optimal zu verstehen. So gibt der Counter der Zugriffe auf die Matrix",
				null, 0, null);
		summary.addCodeLine(
				"beim Kopieren einer Reihe in der Matrix so viele Zugriffe an, wie es Elemente in der Reihe gibt.",
				null, 0, null);
		summary.addCodeLine(
				"In einer Optimierung des Algorithmus w\u00e4re es daher denkbar, nur die Pointer auf die Zeilen in die Buckets zu sortieren,",
				null, 0, null);
		summary.addCodeLine("und damit Zugriffe auf die Matrix zu verringern.", null, 0, null);

		primitives.put(PR_SUMMARY, summary);
	}

	/*
	 * Animation methods
	 */
	private void processLine(int i) {
		SourceCode algorithm = ((SourceCode) primitives.get(PR_ALGORITHM));
		algorithm.highlight(i);
		showExplanation(i);
	}

	private void hideLine(int i) {
		SourceCode algorithm = ((SourceCode) primitives.get(PR_ALGORITHM));
		algorithm.unhighlight(i);
	}

	private void unhighlightRow(IntMatrix matrix, int row) {
		for (int i = 0; i < matrix.getNrCols(); i++) {
			matrix.unhighlightCell(row, i, null, null);
		}
	}

	private void copyRow(IntMatrix matrix1, int r1, int c1, IntMatrix matrix2, int r2, int c2) {
		while (c1 < matrix1.getNrCols() || c2 < matrix2.getNrCols()) {
			matrix2.put(r2, c2, matrix1.getElement(r1, c1), null, null);
			c1++;
			c2++;
		}
	}

	private void hideCurrentBuckets(IntMatrix[] anBucket, Text[] anBucketDescription) {
		for (int i = 0; i < anBucket.length; i++) {
			anBucket[i].hide();
			anBucketDescription[i].hide();
		}
	}

	private void showCurrentBuckets(IntMatrix[] anBucket, Text[] anBucketDescription) {
		for (int i = 0; i < anBucket.length; i++) {
			anBucket[i].show();
			anBucketDescription[i].show();
		}
	}

	private void hideBucketCounter(IntArray anBucketCounter) {
		anBucketCounter.hide();
	}
	private void showBucketCounter(IntArray anBucketCounter) {
		anBucketCounter.show();
	}

	/*
	 * Postman's Sort related methods
	 */
	private void doPostman(int[][] inputMatrix, int maxvalue) {
		/*
		 * Show title and description
		 */
		showTitleAndDescription();
		lang.nextStep("Titel und Beschreibung");
		/*
		 * Hide description
		 */
		primitives.get(PR_DESCRIPTION).hide();
		/*
		 * Show array to sort
		 */
		showMatrix(inputMatrix);
		IntMatrix anMatrix = (IntMatrix) primitives.get(PR_MATRIX);

		lang.nextStep("Array");
		/*
		 * Show algorithm code
		 */
		showAlgorithm();
		showCounter(anMatrix);
		lang.nextStep("Algorithmus");
		/*
		 * Execute first line and start method
		 */
		processLine(0);
		lang.nextStep("Initialer Methodenaufruf");

		postmansSort(0, 0, inputMatrix.length, maxvalue);
		/*
		 * Hide all primitives and show end text
		 */
		lang.hideAllPrimitives();
		primitives.get(PR_TITLE).show();
		primitives.get(PR_TITLE_RECT).show();
		showSummary();
		lang.nextStep("Fazit");
	}

	private void postmansSort(int currentCol, int rmin, int rmax, int maxValue) {
		/*
		 * Get animated Matrix
		 */
		IntMatrix anMatrix = (IntMatrix) primitives.get(PR_MATRIX);
		/*
		 * Initialize counter array for filling of buckets
		 */
		hideLine(0);
		processLine(1);
		lang.nextStep();
		if (currentCol == anMatrix.getNrCols())
			return;
		/*
		 * Document elements of buckets
		 */
		hideLine(1);
		processLine(2);
		int[] bucketCounter = new int[maxValue + 1];
		IntArray anBucketCounter = showBucketCounter(bucketCounter);
		/*
		 * TODO: Animate bucketCounter;
		 */
		lang.nextStep();
		/*
		 * Initialize buckets and show them in animation
		 */
		hideLine(2);
		processLine(3);
		IntMatrix[] anBuckets = generateBuckets(rmin, rmax, anMatrix.getNrCols(), anMatrix.getNrCols() - currentCol,
				maxValue);
		Text[] anBucketDescription = generateBucketDescriptions(rmin, rmax, anMatrix.getNrCols(),
				anMatrix.getNrCols() - currentCol, maxValue);
		lang.nextStep("Bucket Initialisierung");
		/*
		 * Sort column currentCol
		 */
		hideLine(3);
		sortColumn(anMatrix, currentCol, anBucketCounter, anBuckets, rmin, rmax);
		hideLine(10);
		/*
		 * Go through every bucket and sort back in matrix
		 */
		goRecursiveThroughBuckets(anBuckets, anBucketDescription, anMatrix, rmin, rmax, currentCol, anBucketCounter,
				maxValue);
		lang.nextStep();
		hideLine(24);
		hideCurrentBuckets(anBuckets, anBucketDescription);
		hideBucketCounter(anBucketCounter);
	}

	private void sortColumn(IntMatrix anMatrix, int c, IntArray anBucketCounter, IntMatrix[] anBucket, int rmin,
			int rmax) {
		/*
		 * Go through every row of the matrix
		 */
		for (int r = rmin; r < rmax; r++) {
			processLine(4);
			if (r == rmin)
				lang.nextStep("Sortiere Spalte " + c);
			else
				lang.nextStep();
			/*
			 * Copy row to respective bucket
			 */
			hideLine(4);
			processLine(5);
			/*
			 * Highlight current row
			 */
			System.out.println(anMatrix.getNrCols());
			System.out.println(rmin + " " + rmax);
			System.out.println(r);
			System.out.println(c);
			anMatrix.highlightCellColumnRange(r, c, anMatrix.getNrCols() - 1, null, null);
			System.out.println("Highlight:" + r + c);
			/*
			 * Get value of element in current column
			 */
			int currentValue = anMatrix.getElement(r, c);
			lang.nextStep();
			/*
			 * Get free position in respective bucket
			 */
			hideLine(5);
			processLine(6);
			anBucketCounter.highlightCell(currentValue, null, null);
			int bucketRow = anBucketCounter.getData(currentValue);
			anBucket[currentValue].highlightCellColumnRange(bucketRow, 0, anBucket[currentValue].getNrCols() - 1, null,
					null);
			lang.nextStep();
			/*
			 * Copy matrix row to bucket row
			 */
			hideLine(6);
			processLine(7);
			copyRow(anMatrix, r, c, anBucket[currentValue], bucketRow, 0);
			lang.nextStep();
			/*
			 * Unhighlight rows
			 */
			unhighlightRow(anMatrix, r);
			unhighlightRow(anBucket[currentValue], bucketRow);
			/*
			 * Increase bucketCounter for bucket
			 */
			hideLine(7);
			processLine(8);
			anBucketCounter.put(currentValue, anBucketCounter.getData(currentValue) + 1, null, null);
			lang.nextStep();
			anBucketCounter.unhighlightCell(currentValue, null, null);
			hideLine(8);
		}
	}

	private void goRecursiveThroughBuckets(IntMatrix[] anBucket, Text[] anBucketDescription, IntMatrix anMatrix,
			int rmin, int rmax, int currentCol, IntArray anBucketCounter, int maxvalue) {
		/*
		 * Set matrix counter for recopying
		 */
		processLine(10);
		int counterMatrix = 0;
		lang.nextStep();
		hideLine(10);
		/*
		 * Go through every bucket
		 */

		for (int i = 0; i < anBucket.length && counterMatrix < anMatrix.getNrRows(); i++) {
			processLine(11);
			anBucketDescription[i].changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLUE, null,
					null);
			if (i == 0)
				lang.nextStep("Elemente aus Buckets zur\u00fcck in Matrix sortieren");
			else
				lang.nextStep();
			hideLine(11);
			/*
			 * Go through bucket
			 */
			processLine(12);
			int rowStart = rmin;
			int rowEnd = rmin;
			lang.nextStep();
			hideLine(12);
			processLine(13);
			for (int j = 0; j < anBucketCounter.getData(i) && counterMatrix < anMatrix.getNrRows(); j++) {
				processLine(13);
				lang.nextStep();
				hideLine(13);
				processLine(14);
				lang.nextStep();
				/*
				 * Copy bucket row back to matrix at row counterMatrix
				 */

				anBucket[i].highlightCellColumnRange(j, 0, anBucket[i].getNrCols() - 1, null, null);

				/*
				 * Add row to animated matrix
				 */
				lang.nextStep();
				copyRow(anBucket[i], j, 0, anMatrix, counterMatrix, currentCol);
				anMatrix.highlightCellColumnRange(counterMatrix, currentCol, anMatrix.getNrCols() - 1, null, null);

				lang.nextStep();
				hideLine(14);
				processLine(15);
				unhighlightRow(anMatrix, counterMatrix);
				unhighlightRow(anBucket[i], j);

				/*
				 * Increment counterMatrix
				 */
				counterMatrix++;

				lang.nextStep();
				hideLine(15);
				processLine(16);

				/*
				 * Increment row window
				 */
				rowEnd++;
				lang.nextStep();
				hideLine(16);
			}
			hideLine(13);
			lang.nextStep();
			processLine(18);
			lang.nextStep();
			hideLine(18);
			/*
			 * If the difference between rowEnd and rowStart is higher than 1, the rows need
			 * to be further processed (bucket has more than 1 element)
			 */
			if (rowEnd - rowStart > 1 && rowEnd <= rmax) {
				hideCurrentBuckets(anBucket, anBucketDescription);
				hideBucketCounter(anBucketCounter);
				processLine(19);
				processLine(0);
				lang.nextStep();
				hideLine(19);
				hideLine(0);
				postmansSort(currentCol + 1, rowStart, rowEnd, maxvalue);
				showCurrentBuckets(anBucket, anBucketDescription);
				showBucketCounter(anBucketCounter);
			}
			processLine(21);
			/*
			 * Increment rowEnd for next bucket
			 */
			rowEnd++;
			lang.nextStep();
			hideLine(21);
			processLine(22);
			rowStart = rowEnd;
			lang.nextStep();
			hideLine(22);
			anBucketDescription[i].changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, null, null);
		}
	}

	public static void main(String[] args) {
		Animal.startGeneratorWindow(new generators.sorting.PostmanSort());
	}

}