package generators.maths;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Random;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.IntMatrix;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

/**
 * @author Irina Smidt, Simon Sprankel
 * @version 1.0 2011-05-23
 */
public class DeterminantGenerator implements Generator {

	/**
	 * The concrete language object used for creating output
	 */
	private Language language;

	/**
	 * The user generated matrix
	 */
	private int[][] matrix;

	/**
	 * The header text including the headline
	 */
	private Text header;

	/**
	 * The rectangle around the headline
	 */
	private Rect hRect;

	/**
	 * Globally defined text properties
	 */
	private TextProperties textProps;

	/**
	 * the source code shown in the animation
	 */
	private SourceCode src;

	/**
	 * Globally defined source code properties
	 */
	private SourceCodeProperties sourceCodeProps;

	/**
	 * The user defined matrix as an AnimalScript primitive
	 */
	private IntMatrix matrixPrimitive;

	private int xCoordinate;

	private int yCoordinate;

	/**
	 * The user defined matrix properties
	 */
	private MatrixProperties matrixProps;

	public void init() {
		
	}

	/**
	 * Default constructor
	 */
	public DeterminantGenerator(Language language) {
		this.language = language;
		// This initializes the step mode. Each pair of subsequent steps has to
		// be divdided by a call of lang.nextStep();
		language.setStepMode(true);
	}

	public DeterminantGenerator() {
	  language = new AnimalScript(
        "Determinantenberechnung nach Laplace [DE]",
        "Irina Smidt, Simon Sprankel", 800, 600);
	  language.setStepMode(true);
	}

	/**
	 * Initializes the animation. Shows a start page with a description. Then,
	 * shows the matrix and the source code and calls the determinant algorithm.
	 */
	public void start() {
		// set the header text
		TextProperties headerProps = new TextProperties();
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.BOLD, 24));
		header = language.newText(new Coordinates(20, 30),
				"Determinantenberechnung nach Laplace", "header", null,
				headerProps);
		RectProperties rectProps = new RectProperties();
		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		hRect = language.newRect(new Offset(-5, -5, "header",
				AnimalScript.DIRECTION_NW), new Offset(5, 5, "header", "SE"),
				"hRect", null, rectProps);

		// setup the start page with the description
		language.nextStep();
		textProps = new TextProperties();
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.PLAIN, 16));
		language
				.newText(
						new Coordinates(10, 100),
						"Eine Determinante ist eine Funktion, die einer quadratischen Matrix",
						"description1", null, textProps);
		language.newText(new Offset(0, 25, "description1",
				AnimalScript.DIRECTION_NW), "einen Skalar zuordnet.",
				"description2", null, textProps);
		language
				.newText(
						new Offset(0, 25, "description2",
								AnimalScript.DIRECTION_NW),
						"Mit ihr lässt sich feststellen, ob ein lineares Gleichungssystem (LGS) ",
						"description3", null, textProps);
		language
				.newText(
						new Offset(0, 25, "description3",
								AnimalScript.DIRECTION_NW),
						"eine eindeutige Lösung hat. Wenn die Determinante der Koeffizientenmatrix",
						"description4", null, textProps);
		language
				.newText(
						new Offset(0, 25, "description4",
								AnimalScript.DIRECTION_NW),
						"ungleich Null ist, ist das LGS eindeutig lösbar. Somit ist eine quadratische",
						"description5", null, textProps);
		language
				.newText(
						new Offset(0, 25, "description5",
								AnimalScript.DIRECTION_NW),
						"Matrix genau dann invertierbar, wenn ihre Determinante ungleich Null ist.",
						"description6", null, textProps);

		language.nextStep();
		language
				.newText(
						new Offset(0, 50, "description6",
								AnimalScript.DIRECTION_NW),
						"Es gibt verschiedene Algorithmen zur Determinantenberechnung. Zum Beispiel",
						"description7", null, textProps);
		language
				.newText(
						new Offset(0, 25, "description7",
								AnimalScript.DIRECTION_NW),
						"den Gauß Algorithmus sowie den Laplaceschen Entwicklungssatz, der hier",
						"description8", null, textProps);
		language.newText(new Offset(0, 25, "description8",
				AnimalScript.DIRECTION_NW), "präsentiert wird.",
				"description9", null, textProps);

		// hide the description and show the graph
		language.nextStep();
		language.hideAllPrimitives();
		header.show();
		hRect.show();
		// show the matrix
		matrixPrimitive = language
				.newIntMatrix(new Offset(10, 75, hRect,
						AnimalScript.DIRECTION_NW), this.matrix, "matrix", null,
						matrixProps);
		// show the source code
		src = language
				.newSourceCode(new Offset(30, -20, "matrix",
						AnimalScript.DIRECTION_NE), "sourceCode", null,
						sourceCodeProps);
		src.addCodeLine("public int determinant(int[][] matrix) {", null, 0,
				null); // 0
		src.addCodeLine(
				"// pruefe, ob uebergebene Matrix eine n x n-Matrix ist", null,
				1, null); // 1
		src.addCodeLine("int n = matrix.length;", null, 1, null); // 2
		src.addCodeLine("if (n == 0 || n != matrix[0].length)", null, 1, null); // 3
		src.addCodeLine("throw new IllegalArgumentException();", null, 2, null); // 4
		src
				.addCodeLine(
						"// Rekursionsanker: Wenn Matrix nur ein Element enthaelt, gib es zurueck",
						null, 1, null); // 5
		src.addCodeLine("if (n == 1)", null, 1, null); // 6
		src.addCodeLine("return matrix[0][0];", null, 2, null); // 7
		src.addCodeLine("// Laplacesche Entwicklung nach erster Spalte", null,
				1, null); // 8
		src.addCodeLine("int result = 0;", null, 1, null); // 9
		src.addCodeLine("for (int i = 0; i < n; i++) {", null, 1, null); // 10
		src
				.addCodeLine(
						"result += Math.pow(-1, i) * matrix[i][0] * determinant(submatrix(matrix, i, 0));",
						null, 2, null); // 11
		src.addCodeLine("}", null, 1, null); // 12
		src.addCodeLine("return result;", null, 1, null); // 13
		src.addCodeLine("}", null, 0, null); // 14

		// call the determinant algorithm
		language.nextStep();
		double d = (matrix.length - 1) * 1.0 / 2;
		int offset = (int) Math.round(d * 25);
		xCoordinate = 10;
		yCoordinate = 400 + offset;
		determinantAnimation(matrix);

		language.nextStep();
		matrixPrimitive.hide();
		language.hideAllPrimitives();
		header.show();
		hRect.show();
		language
				.newText(
						new Coordinates(10, 100),
						"Bei dieser Animation wurde nach der ersten Spalte der Matrix entwickelt.",
						"resultText1", null, textProps);
		language
				.newText(
						new Offset(0, 25, "resultText1",
								AnimalScript.DIRECTION_NW),
						"Analog dazu kann man sowohl nach einer anderen Spalte, als auch nach",
						"resultText2", null, textProps);
		language.newText(new Offset(0, 25, "resultText2",
				AnimalScript.DIRECTION_NW),
				"einer beliebigen Zeile entwickeln.", "resultText3", null,
				textProps);
		language
				.newText(
						new Offset(0, 50, "resultText3",
								AnimalScript.DIRECTION_NW),
						"Bei n x n Matrizen hat der Algorithmus eine Komplexität von O(n!).",
						"resultText4", null, textProps);
		language.newText(new Offset(0, 25, "resultText4",
				AnimalScript.DIRECTION_NW),
				"Deshalb ist er bei großen Matrizen sehr ineffizient.",
				"resultText5", null, textProps);
		language.newText(new Offset(0, 25, "resultText5",
				AnimalScript.DIRECTION_NW),
				"Er ist aber insbesondere bei Matrizen, die in einer Spalte",
				"resultText6", null, textProps);
		language
				.newText(
						new Offset(0, 25, "resultText6",
								AnimalScript.DIRECTION_NW),
						"oder Zeile viele Nullen haben, äußerst praktisch und kann auch",
						"resultText7", null, textProps);
		language.newText(new Offset(0, 25, "resultText7",
				AnimalScript.DIRECTION_NW),
				"bei kleinen Matrizen gut angewendet werden.", "resultText8",
				null, textProps);
	}

	/**
	 * Executes the Laplace determinant algorithm on the given matrix.
	 * 
	 * @param matrix
	 *            the matrix on which the Laplace determinant algorithm should
	 *            be executed
	 */
	private int determinantAnimation(int[][] matrix) {
		// highlight the code lines with the initialization
		unhighlightAllLines();
		src.highlight(0);
		src.highlight(2);
		src.highlight(3);
		src.highlight(4);
		// show the current matrix
		Coordinates start = printDeterminant(matrix, new Coordinates(
				xCoordinate, yCoordinate));
		xCoordinate = start.getX() + 25;
		yCoordinate = start.getY();
		language.newText(start, " = ", "", null, textProps);
		// check, whether given matrix is a n x n matrix and show result to the
		// user
		int n = matrix.length;
		Text matrixOkayText;
		if (n == 0 || n != matrix[0].length) {
			textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
			language.newText(new Coordinates(xCoordinate, yCoordinate),
					"Die Matrix muss quadratisch sein.",
					"illegalArgumentException", null, textProps);
			textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
			return 0;
		} else {
			textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(34,
					139, 34));
			matrixOkayText = language.newText(new Coordinates(xCoordinate,
					yCoordinate), "Die Matrix ist quadratisch und nicht leer.",
					"matrixOkay", null, textProps);
			textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		}

		language.nextStep();
		matrixOkayText.hide();
		src.unhighlight(0);
		src.unhighlight(2);
		src.unhighlight(3);
		src.unhighlight(4);
		src.highlight(6);
		src.highlight(7);
		// recursion anchor: if matrix only contains one element, return it
		Text dimensionText;
		if (n == 1) {
			language.newText(new Coordinates(xCoordinate, yCoordinate), ""
					+ matrix[0][0], "", null, textProps);
			xCoordinate -= 20;
			yCoordinate -= 10;
			return matrix[0][0];
		} else {
			dimensionText = language.newText(new Coordinates(xCoordinate,
					yCoordinate), "Matrix hat Dimension > 1", "", null,
					textProps);
		}

		// execute the main loop
		language.nextStep();
		dimensionText.hide();
		src.unhighlight(6);
		src.unhighlight(7);
		src.highlight(9);
		src.highlight(10);
		src.highlight(11);
		src.highlight(12);
		for (int i = 0; i < n; i++) {
			int[][] submatrix = submatrix(matrix, i, 0);
			String one = "+ 1 ";
			if (i % 2 != 0)
				one = "- 1 ";
			int xOffset = (i == 0) ? 20 : 0;
			language.newText(new Coordinates(start.getX() + xOffset, start
					.getY()), one + " * " + matrix[i][0] + " * ", "", null,
					textProps);
			start = printDeterminant(submatrix, new Coordinates(start.getX()
					+ xOffset + 85, start.getY()));
		}
		int result = 0;
		// TO DO: Laplace expansion along the first column
		for (int i = 0; i < n; i++) {
			int[][] submatrix = submatrix(matrix, i, 0);
			// TO DO: highlight the factor and the submatrix
			// matrixPrimitive.highlightCell(i + this.matrix.length -
			// matrix.length, this.matrix.length - matrix.length, null, null);
			xCoordinate += 20 - 125 - matrix.length * 25;
			yCoordinate += 25 * matrix.length;
			language.nextStep();
			result += Math.pow(-1, i) * matrix[i][0]
					* determinantAnimation(submatrix);
			xCoordinate -= 20 * (matrix.length - 2);
			// TO DO: matrixPrimitive.unhighlightCell(i + this.matrix.length -
			// matrix.length, this.matrix.length - matrix.length, null, null);
		}

		language.nextStep();
		unhighlightAllLines();
		src.highlight(13);
		// only show the result in the last step
		if (this.matrix.length == matrix.length) {
			double d = (matrix.length - 1) * 1.0 / 2;
			int offset = (int) Math.round(d * 25);
			xCoordinate = 10;
			yCoordinate += 70;
			language.newText(new Coordinates(xCoordinate, yCoordinate),
					"Ergebnis:", "", null, textProps);
			yCoordinate += offset + 25;
			start = printDeterminant(matrix, new Coordinates(xCoordinate,
					yCoordinate));
			xCoordinate = start.getX() + 25;
			yCoordinate = start.getY();
			language.newText(start, " = ", "", null, textProps);

			for (int i = 0; i < n; i++) {
				int[][] submatrix = submatrix(matrix, i, 0);
				String one = "+ 1 ";
				if (i % 2 != 0)
					one = "- 1 ";
				int det = determinant(submatrix);
				String determinant = (det < 0) ? "(" + det + ")" : det + "";
				language.newText(new Coordinates(xCoordinate, yCoordinate), one
						+ " * " + matrix[i][0] + " * " + determinant, "", null,
						textProps);
				xCoordinate += 120;
			}

			language.newText(new Coordinates(xCoordinate, yCoordinate), " = "
					+ result, "", null, textProps);
		}
		return result;
	}

	/**
	 * 
	 * @param matrix
	 * @param i
	 * @param j
	 * @return
	 */
	private int[][] submatrix(int[][] matrix, int i, int j) {
		// create a new, empty (n-1) x (n-1) matrix
		int n = matrix.length;
		int[][] submatrix = new int[n - 1][n - 1];
		// counter for the elements of the submatrix
		int x = 0, y = 0;
		// go through all elements of the base matrix
		// and leave out the i-th row and j-th column
		for (int k = 0; k < n; k++) {
			if (k == i)
				continue;
			for (int l = 0; l < n; l++) {
				if (l == j)
					continue;
				submatrix[x][y] = matrix[k][l];
				y++;
			}
			y = 0;
			x++;
		}
		return submatrix;
	}

	private void unhighlightAllLines() {
		for (int i = 0; i < 15; i++)
			src.unhighlight(i);
	}

	private Coordinates printDeterminant(int[][] matrix, Coordinates position) {
		double d = (matrix.length - 1) * 1.0 / 2;
		int offset = (int) Math.round(d * 25);
		// create random identifier
		Random random = new Random();
		String detId = "detId" + Math.abs(random.nextInt());
		String matrixId = "matrixId" + Math.abs(random.nextInt());
		language.newText(new Coordinates(position.getX(), position.getY()),
				"determinant(", detId, null, textProps);
		language
				.newIntMatrix(new Offset(10, -offset, detId,
						AnimalScript.DIRECTION_NE), matrix, matrixId, null,
						matrixProps);
		language.newText(
				new Offset(0, -10, matrixId, AnimalScript.DIRECTION_E), ")",
				"", null, textProps);
		return new Coordinates(position.getX() + 125 + matrix.length * 25,
				position.getY());
	}

	public int determinant(int[][] matrix) {
		// pruefe, ob uebergebene Matrix eine n x n-Matrix ist
		int n = matrix.length;
		if (n == 0 || n != matrix[0].length)
			throw new IllegalArgumentException();
		// Rekursionsanker: Wenn Matrix nur ein Element enthaelt, gib es zurueck
		if (n == 1)
			return matrix[0][0];
		// Laplacesche Entwicklung nach erster Spalte
		int result = 0;
		for (int i = 0; i < n; i++) {
			result += Math.pow(-1, i) * matrix[i][0]
					* determinant(submatrix(matrix, i, 0));
		}
		return result;
	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		this.matrix = (int[][]) primitives.get("matrix");
		sourceCodeProps = (SourceCodeProperties) props
				.getPropertiesByName("sourceCodeProps");
		matrixProps = (MatrixProperties) props
				.getPropertiesByName("matrixProps");

		start();

		return language.toString();
	}

	public String getName() {
		return "Determinantenberechnung nach Laplace";
	}

	public String getAlgorithmName() {
		return "Determinantenberechnung nach Laplace";
	}

	public String getAnimationAuthor() {
		return "Irina Smidt, Simon Sprankel";
	}

	public String getDescription() {
		return "Eine Determinante ist eine Funktion, die einer quadratischen Matrix "
				+ "\n"
				+ "einen Skalar zuordnet. "
				+ "\n"
				+ "Mit ihr l&auml;sst sich feststellen, ob ein lineares Gleichungssystem (LGS) "
				+ "\n"
				+ "eine eindeutige L&ouml;sung hat. Wenn die Determinante der Koeffizientenmatrix "
				+ "\n"
				+ "ungleich Null ist, ist das LGS eindeutig l&ouml;sbar. Somit ist eine quadratische "
				+ "\n"
				+ "Matrix genau dann invertierbar, wenn ihre Determinante ungleich Null ist. "
				+ "\n"
				+ "\n"
				+ "Es gibt verschiedene Algorithmen zur Determinantenberechnung. Zum Beispiel"
				+ "\n"
				+ "den Gau&szlig; Algorithmus sowie den Laplaceschen Entwicklungssatz, der hier "
				+ "\n" + "pr&auml;sentiert wird.";
	}

	public String getCodeExample() {
		return "public int determinant(int[][] matrix) {"
				+ "\n"
				+ "    // pruefe, ob uebergebene Matrix eine n x n-Matrix ist"
				+ "\n"
				+ "    int n = matrix.length;"
				+ "\n"
				+ "    if (n == 0 || n != matrix[0].length)"
				+ "\n"
				+ "        throw new IllegalArgumentException();"
				+ "\n"
				+ "    // Rekursionsanker: Wenn Matrix nur ein Element enthaelt, gib es zurueck"
				+ "\n"
				+ "    if (n == 1)"
				+ "\n"
				+ "        return matrix[0][0];"
				+ "\n"
				+ "    // Laplacesche Entwicklung nach erster Spalte"
				+ "\n"
				+ "    int result = 0;"
				+ "\n"
				+ "    for (int i = 0; i < n; i++) {"
				+ "\n"
				+ "        result += Math.pow(-1, i) * matrix[i][0] * determinant(submatrix(matrix, i, 0));"
				+ "\n" + "    }" + "\n" + "    return result;" + "\n" + "}";
	}

	public String getFileExtension() {
		return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	public Locale getContentLocale() {
		return Locale.GERMANY;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
	}

	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

}