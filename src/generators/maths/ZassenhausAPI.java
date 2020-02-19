/*
 * ZassenhausAPI.java
 * Peter Reinhardt, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.maths;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.TrueFalseQuestionModel;
import algoanim.animalscript.AnimalScript;
import java.awt.Font;

import algoanim.primitives.DoubleMatrix;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import animal.main.Animal;

/**
 * @author Peter Reinhardt
 * @version 1.0 2019-08-21
 */
public class ZassenhausAPI implements Generator {

	/**
	 * The concrete language object used for creating output
	 */
	private Language language;

	/**
	 * The title text including the headline
	 */
	private Text title;

	/**
	 * The status text including the status
	 */
	private Text status;

	/**
	 * The rectangle around the headline
	 */
	private Rect tRect;

	/**
	 * the source code shown in the animation
	 */
	private SourceCode sourceCode;

	/**
	 * Globally defined properties
	 */
	private SourceCodeProperties sourceCodeProps = new SourceCodeProperties();
	private TextProperties headerProps = new TextProperties();
	private TextProperties textProps = new TextProperties();
	private MatrixProperties matrixProps = new MatrixProperties();
	private RectProperties titleRectProps = new RectProperties();
	private RectProperties codeRectProps = new RectProperties();

	/**
	 * input values
	 */
	private int[] intVektorU1;
	private int[] intVektorU2;
	private int[] intVektorW1;
	private int[] intVektorW2;

	/**
	 * Default constructor
	 * 
	 */
	public ZassenhausAPI() {
	}

	/**
	 * Initializes the animation. Shows a start page with a description header
	 */
	public void start(double[][] u1, double[][] u2, double[][] w1, double[][] w2) {

		// show the title with a heading surrounded by a rectangle
		TextProperties titleProps = new TextProperties();
		titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 30));
		title = language.newText(new Coordinates(20, 30), "Zassenhaus-Algorithmus", "title", null, titleProps);
		tRect = language.newRect(new Offset(-5, -5, "title", AnimalScript.DIRECTION_NW),
				new Offset(5, 5, "title", "SE"), "tRect", null, titleRectProps);

		// show the description header under the title
		language.newText(new Offset(20, 40, "title", AnimalScript.DIRECTION_SW), "Beschreibung des Algorithmus",
				"descriptionHeader", null, headerProps);
		SourceCode descr;
		descr = language.newSourceCode(new Offset(0, 0, "descriptionHeader", AnimalScript.DIRECTION_SW), "descr", null,
				sourceCodeProps);
		descr.addCodeLine("Der Zassenhaus-Algorithmus ist ein Algorithmus zur Bestimmung von Schnitt- und Summenbasen",

				null, 0, null);
		descr.addCodeLine("von 2 Teilräumen in der Linearen Algebra. Dazu müssen die beiden Teilräume Unterräume eines",
				null, 0, null);
		descr.addCodeLine("gemeinsamen Vektorraums sein, und die Basen dieser beiden Teilräume müssen gegeben sein.",
				null, 0, null);
		descr.addCodeLine("Er wird in Computeralgebrasystemen verwendet.", null, 0, null);
		descr.addCodeLine("Er wurde 1948 von dem Mathematiker Hans Zassenhaus vorgeschlagen.", null, 0, null);
		language.nextStep();
		input(u1, u2, w1, w2);
	}

	/**
	 * Shows the input vectors and the source code
	 */
	private void input(double[][] u1, double[][] u2, double[][] w1, double[][] w2) {
		FillInBlanksQuestionModel algoYear = new FillInBlanksQuestionModel("year");
		algoYear.setPrompt("In welchem Jahr wurde der Algorithmus vorgeschlagen?");
		algoYear.addAnswer("1948", 1, "Hans Zassenhaus im Jahre 1948");
		language.addFIBQuestion(algoYear);
		language.hideAllPrimitives();
		title.show();
		tRect.show();
		status = language.newText(new Offset(500, 25, "title", AnimalScript.DIRECTION_SW),
				"[STATUS] Sei V ein Vektorraum und U und W jeweils die Basis zweier Untervektorräume von V", "status",
				null, textProps);
		language.newText(new Offset(20, 40, "title", AnimalScript.DIRECTION_SW), "Die Basen der Unterräume", "init",
				null, headerProps);

		// basis u1 and basis u2
		language.newDoubleMatrix(new Offset(80, 30, "init", AnimalScript.DIRECTION_SW), u1, "basisU1", null,
				matrixProps);
		language.newDoubleMatrix(new Offset(25, 0, "basisU1", AnimalScript.DIRECTION_NE), u2, "basisU2", null,
				matrixProps);
		language.newText(new Offset(-22, 34, "basisU1", AnimalScript.DIRECTION_NW), "=", "Gleich", null, textProps);
		language.newText(new Offset(-12, 0, "Gleich", AnimalScript.DIRECTION_NW), "U", "letterU", null, textProps);
		language.newText(new Offset(-15, 34, "basisU2", AnimalScript.DIRECTION_NW), ",", "Komma", null, textProps);

		// basis w1 and basis w2
		language.newDoubleMatrix(new Offset(150, 0, "basisU2", AnimalScript.DIRECTION_NE), w1, "basisW1", null,
				matrixProps);
		language.newDoubleMatrix(new Offset(25, 0, "basisW1", AnimalScript.DIRECTION_NE), w2, "basisW2", null,
				matrixProps);
		language.newText(new Offset(-22, 34, "basisW1", AnimalScript.DIRECTION_NW), "=", "Gleich2", null, textProps);
		language.newText(new Offset(-14, 0, "Gleich2", AnimalScript.DIRECTION_NW), "W", "letterW", null, textProps);
		language.newText(new Offset(-15, 34, "basisW2", AnimalScript.DIRECTION_NW), ",", "Komma2", null, textProps);

		// source code
		sourceCode = language.newSourceCode(new Offset(700, 100, "title", AnimalScript.DIRECTION_NW), "sourceCode",
				null, sourceCodeProps);
		sourceCode.addCodeLine("Sei K ein Körper und V ein K-Vektorraum endlicher Dimension.", null, 0, null);
		sourceCode.addCodeLine(
				"Seien zudem zwei Teilräume U=(u1,...,uk) und W=(w1,...,wk) durch Erzeugendensystem gegeben.", null, 0,
				null);
		sourceCode.addCodeLine("Man definiere die Matrix M als:", null, 0, null);
		sourceCode.addCodeLine("									(	u1^T		|  u1^T 	)", null, 0, null);
		sourceCode.addCodeLine("									(	u2^T		|  u2^T 	)", null, 0, null);
		sourceCode.addCodeLine("									(	  ...   	|    ...   		)", null, 0, null);
		sourceCode.addCodeLine("									(	uk^T  |  uk^T 	)", null, 0, null);
		sourceCode.addCodeLine("									(	w1^T  |    0    	)", null, 0, null);
		sourceCode.addCodeLine("									(	w2^T  |    0    	)", null, 0, null);
		sourceCode.addCodeLine("									(	  ...  	  |    ...    )", null, 0, null);
		sourceCode.addCodeLine("									(	wk^T  |    0    	)", null, 0, null);
		sourceCode.addCodeLine(
				"Man führe dann Zeilenumformungen auf dieser Matrix aus, bis sie in der folgenden Zeilenstufenform ist:",
				null, 0, null);
		sourceCode.addCodeLine(
				"									(						s1											|												 *															 	)",
				null, 0, null);
		sourceCode.addCodeLine(
				"									(						s2											|												 *															 	)",
				null, 0, null);
		sourceCode.addCodeLine(
				"									(						...											|											 	*															 	)",
				null, 0, null);
		sourceCode.addCodeLine(
				"									(	s dim(U+W)	 |													*																	 )",
				null, 0, null);
		sourceCode.addCodeLine("									(-----------|------------------)", null, 0, null);
		sourceCode.addCodeLine(
				"									(						0											 |												d1														  	)",
				null, 0, null);
		sourceCode.addCodeLine(
				"									(						0											 |												d2														 	 )",
				null, 0, null);
		sourceCode.addCodeLine(
				"									(						...											|												...															  	)",
				null, 0, null);
		sourceCode.addCodeLine(
				"									(						0											 |	d dim(U Schnitt W)	 		 )",
				null, 0, null);
		sourceCode.addCodeLine("									(-----------|------------------)", null, 0, null);
		sourceCode.addCodeLine(
				"									(						0  										|												0																	 )",
				null, 0, null);
		sourceCode.addCodeLine(
				"									(						0  										|												0																 	)",
				null, 0, null);
		sourceCode.addCodeLine("wobei s dim(U+W) ungleich 0 und d dim(U Schnitt W) ungleich 0 gilt.", null, 0, null);
		sourceCode.addCodeLine(
				"Die Blöcke, welche die si und di enthalten, müssen außerdem in strikter Zeilenstufenform sein!", null,
				0, null);
		sourceCode.addCodeLine("Ablesen der Matrix", null, 0, null);
		sourceCode.addCodeLine("Nun ist	(s1, s2, ... , s dim(U+W)) eine Basis von U + W", null, 0, null);
		sourceCode.addCodeLine("und	(d1, d2, ... , d dim(U Schnitt W)) ist eine Basis von U Schnitt W", null, 0, null);
		language.newRect(new Offset(-5, -5, "sourceCode", AnimalScript.DIRECTION_NW),
				new Offset(5, 5, "sourceCode", "SE"), "codeRect", null, codeRectProps);
		sourceCode.highlight(0);
		sourceCode.highlight(1);
		language.nextStep("Eingabe");
		buildMatrix(u1, u2, w1, w2);
	}

	/**
	 * build the matrix for the algorithm
	 */
	private void buildMatrix(double[][] u1, double[][] u2, double[][] w1, double[][] w2) {
		status.setText("[STATUS] Füge zuert die Basisvektoren von U als Zeilen in eine Matrix ein", null, null);
		sourceCode.unhighlight(0);
		sourceCode.unhighlight(1);
		sourceCode.highlight(2);
		sourceCode.highlight(3);
		sourceCode.highlight(4);
		sourceCode.highlight(5);
		sourceCode.highlight(6);
		language.newText(new Offset(-80, 40, "basisU1", AnimalScript.DIRECTION_SW), "Aufstellen der Matrix",
				"buildMatrix", null, headerProps);

		// basis vector u as rows
		double[][] matrixTemp1 = new double[2][u1.length];
		for (int i = 0; i < u1.length; i++) {
			matrixTemp1[0][i] = u1[i][0];
		}
		for (int i = 0; i < u2.length; i++) {
			matrixTemp1[1][i] = u2[i][0];
		}
		DoubleMatrix matrixbuildTemp1 = language.newDoubleMatrix(
				new Offset(80, 30, "buildMatrix", AnimalScript.DIRECTION_SW), matrixTemp1, "matrixbuildTemp1", null,
				matrixProps);
		language.nextStep("Aufstellen der Matrix");

		// matrix twice as wide
		status.setText("[STATUS] Das verdoppelst du jetzt nach rechts", null, null);
		matrixbuildTemp1.hide();
		double[][] matrixTemp2 = new double[2][u1.length * 2];
		for (int i = 0; i < 4; i += 3) {
			for (int j = 0; j < u1.length; j++) {
				matrixTemp2[0][i + j] = u1[j][0];
			}
			for (int j = 0; j < u2.length; j++) {
				matrixTemp2[1][i + j] = u2[j][0];
			}
		}
		DoubleMatrix matrixbuildTemp2 = language.newDoubleMatrix(
				new Offset(80, 30, "buildMatrix", AnimalScript.DIRECTION_SW), matrixTemp2, "matrixbuildTemp2", null,
				matrixProps);
		language.nextStep();

		// final build matrix
		status.setText(
				"[STATUS] Anschließend schreibst du die Basisvektoren von W unten links als weitere Zeilen hin, allerdings nur einmal",
				null, null);
		matrixbuildTemp2.hide();
		sourceCode.highlight(7);
		sourceCode.highlight(8);
		sourceCode.highlight(9);
		sourceCode.highlight(10);
		double[][] matrixFinal = new double[4][u1.length * 2];
		for (int i = 0; i < 4; i += 3) {
			for (int j = 0; j < u1.length; j++) {
				matrixFinal[0][i + j] = u1[j][0];
			}
			for (int j = 0; j < u2.length; j++) {
				matrixFinal[1][i + j] = u2[j][0];
			}
			for (int j = 0; j < w1.length; j++) {
				matrixFinal[2][j] = w1[j][0];
			}
			for (int j = 0; j < w2.length; j++) {
				matrixFinal[3][j] = w2[j][0];
			}
		}
		language.newDoubleMatrix(new Offset(80, 30, "buildMatrix", AnimalScript.DIRECTION_SW), matrixFinal,
				"matrixbuildFinal", null, matrixProps);
		language.nextStep();
		calculateMatrix(matrixFinal);
	}

	private void calculateMatrix(double[][] matrixFinal) {
		sourceCode.unhighlight(2);
		sourceCode.unhighlight(3);
		sourceCode.unhighlight(4);
		sourceCode.unhighlight(5);
		sourceCode.unhighlight(6);
		sourceCode.unhighlight(7);
		sourceCode.unhighlight(8);
		sourceCode.unhighlight(9);
		sourceCode.unhighlight(10);
		sourceCode.highlight(11);
		sourceCode.highlight(12);
		sourceCode.highlight(13);
		sourceCode.highlight(14);
		sourceCode.highlight(15);
		sourceCode.highlight(16);
		sourceCode.highlight(17);
		sourceCode.highlight(18);
		sourceCode.highlight(19);
		sourceCode.highlight(20);
		sourceCode.highlight(21);
		sourceCode.highlight(22);
		sourceCode.highlight(23);
		sourceCode.highlight(24);
		sourceCode.highlight(25);
		language.newText(new Offset(-80, 40, "matrixbuildFinal", AnimalScript.DIRECTION_SW),
				"Berechnen der Zeilenstufenform", "calculateMatrix", null, headerProps);
		DoubleMatrix matrixCalculateFinal = language.newDoubleMatrix(
				new Offset(80, 30, "calculateMatrix", AnimalScript.DIRECTION_SW), matrixFinal, "matrixCalculateFinal",
				null, matrixProps);
		status.setText("[STATUS] Zur Berechnung der Zeilenstufenform dient der Gauß-Algorithmus.", null, null);
		language.nextStep("Berechnen der Zeilenstufenform");

		FillInBlanksQuestionModel whichAlgo = new FillInBlanksQuestionModel("whichAlgo");
		whichAlgo.setPrompt("Welcher Algorithmus dient zur Berechnung der Zeilenstufenform?");
		whichAlgo.addAnswer("Gauss", 1, "Gauß-Algorithmus");
		language.addFIBQuestion(whichAlgo);
		
		int tmpRow;

		// iterates over all columns
		for (int column = 0; column < matrixFinal[0].length; column++) {

			tmpRow = -1;

			// iterates over all rows with begin at the diagonal
			// goal of the loop: find a line with the first value not equal to 0
			for (int row = column; row < matrixFinal.length - 1; row++) {

				// element is not equal to 0
				if (matrixFinal[row][column] != 0) {
					tmpRow = row;
					break;
				}
			}

			// Value not equal to 0 discovered
			if (tmpRow != -1) {

				if (matrixFinal[tmpRow][column] != 1) {

					// highlight the matrix elements
					status.setText("[STATUS] Dividiere die " + tmpRow + ". Zeile mit " + matrixFinal[tmpRow][column],
							null, null);
					for (int i = 0; i < matrixFinal[tmpRow].length; i++) {
						matrixCalculateFinal.highlightElem(tmpRow, i, null, null);
					}
					language.nextStep();

					matrixFinal = divideLine(tmpRow, matrixFinal[tmpRow][column], matrixFinal);

					// put the new values in Animal
					for (int i = 0; i < matrixFinal[tmpRow].length; i++) {
						matrixCalculateFinal.put(tmpRow, i, matrixFinal[tmpRow][i], null, null);
					}
					language.nextStep();
					for (int i = 0; i < matrixFinal[tmpRow].length; i++) {
						matrixCalculateFinal.unhighlightElem(tmpRow, i, null, null);
					}
				}

				// find a second line to subtract
				for (int row = tmpRow + 1; row < matrixFinal.length; row++) {

					// Element is not equal to 0
					if (matrixFinal[row][column] != 0) {

						// highlight the matrix elements
						status.setText("[STATUS] Subtrahiere die " + (tmpRow + 1) + ". Zeile "
								+ matrixFinal[row][column] + "-mal von der " + (row + 1) + ". Zeile", null, null);
						for (int i = 0; i < matrixFinal[tmpRow].length; i++) {
							matrixCalculateFinal.highlightElem(tmpRow, i, null, null);
							matrixCalculateFinal.highlightElem(row, i, null, null);
						}
						language.nextStep();

						matrixFinal = removeRowLeadingNumber(matrixFinal[row][column], tmpRow, row, matrixFinal);

						// put the new values in Animal
						for (int i = 0; i < matrixFinal[row].length; i++) {
							matrixCalculateFinal.put(row, i, matrixFinal[row][i], null, null);
						}
						language.nextStep();
						for (int i = 0; i < matrixFinal[tmpRow].length; i++) {
							matrixCalculateFinal.unhighlightElem(tmpRow, i, null, null);
							matrixCalculateFinal.unhighlightElem(row, i, null, null);
						}
					}
				}
			}
		}
		showResult(matrixFinal);
	}

	/**
	 * One row (rowRoot) is subtracted with a corresponding multiple factor from
	 * another row.
	 */
	private double[][] removeRowLeadingNumber(double factor, int rowRoot, int row, double[][] matrix) {

		for (int column = 0; column < matrix[row].length; column++) {
			matrix[row][column] = matrix[row][column] - factor * matrix[rowRoot][column];
		}
		return matrix;
	}

	/**
	 * a line is divided by "div"
	 * 
	 * @return
	 */
	private double[][] divideLine(int row, double div, double[][] matrix) {

		for (int column = 0; column < matrix[row].length; column++) {
			matrix[row][column] = matrix[row][column] / div;
		}
		return matrix;
	}

	private void showResult(double[][] matrixFinal) {
		sourceCode.unhighlight(11);
		sourceCode.unhighlight(12);
		sourceCode.unhighlight(13);
		sourceCode.unhighlight(14);
		sourceCode.unhighlight(15);
		sourceCode.unhighlight(16);
		sourceCode.unhighlight(17);
		sourceCode.unhighlight(18);
		sourceCode.unhighlight(19);
		sourceCode.unhighlight(20);
		sourceCode.unhighlight(21);
		sourceCode.unhighlight(22);
		sourceCode.unhighlight(23);
		sourceCode.unhighlight(24);
		sourceCode.unhighlight(25);
		sourceCode.highlight(26);
		language.newText(new Offset(-80, 40, "matrixCalculateFinal", AnimalScript.DIRECTION_SW),
				"Ablesen der Basis der Schnittmenge und der Summe", "resultMatrix", null, headerProps);
		status.setText("[STATUS] Nun kann man die Summen- und Schnittbasen direkt ablesen.", null, null);
		DoubleMatrix matrixFinalTemp1 = language.newDoubleMatrix(
				new Offset(80, 30, "resultMatrix", AnimalScript.DIRECTION_SW), matrixFinal, "matrixFinalTemp1", null,
				matrixProps);
		language.nextStep("Ablesen der Matrix");
		// ------------------------------------------------------------------------------------------------------------

		status.setText("[STATUS] Die berechnete Zeilenstufenform wird in der Mitte geteilt", null, null);
		matrixFinalTemp1.hide();
		String[][] matrixResultTempIntern = new String[matrixFinal.length][matrixFinal[0].length + 1];
		// copy the matrix and makes a pipe in the middle of the matrix
		int j = 0;
		for (int row = 0; row < matrixResultTempIntern.length; row++) {
			j = 0;
			for (int column = 0; column < matrixResultTempIntern[0].length; column++) {
				// check for the middle
				if (column == (matrixFinal[0].length / 2)) {
					matrixResultTempIntern[row][column] = "|";
					j--;
				} else {
					matrixResultTempIntern[row][column] = String.valueOf(matrixFinal[row][j]);
				}
				j++;
			}
		}
		StringMatrix matrixResultTemp = language.newStringMatrix(
				new Offset(80, 30, "resultMatrix", AnimalScript.DIRECTION_SW), matrixResultTempIntern,
				"matrixResultTemp", null, matrixProps);
		language.nextStep();
		// ------------------------------------------------------------------------------------------------------------

		TrueFalseQuestionModel whereSumResult = new TrueFalseQuestionModel("whereSumResult", true, 1);
		whereSumResult.setPrompt("Die Basisvektoren der Summe befinden sich jeweils als Zeile oben links.");
		language.addTFQuestion(whereSumResult);

		status.setText(
				"[STATUS] Da wo die Nullzeilen der linken Hälfte anfangen wird die Matrix nochmal horizontal geteilt",
				null, null);
		matrixResultTemp.hide();
		String[][] matrixResultIntern = new String[matrixFinal.length + 1][matrixFinal[0].length + 1];

		// find the row to cut the matrix
		int count;
		int rowToCut = matrixFinal.length - 1;
		for (int row = 0; row < matrixResultTempIntern.length; row++) {
			count = 0;
			for (int column = 0; column < matrixResultTempIntern[0].length; column++) {
				if (matrixResultTempIntern[row][column].equals("|")) {
					break;
				}
				if (matrixResultTempIntern[row][column].equals("0.0")) {
					count++;
				}
			}
			if (count == matrixFinal[0].length / 2) {
				rowToCut = row;
				break;
			}
		}

		// copy the matrix and cut the matrix horizontal
		int k = 0;
		for (int row = 0; row < matrixResultIntern.length; row++) {
			for (int column = 0; column < matrixResultIntern[0].length; column++) {
				if (row == rowToCut) {
					matrixResultIntern[row][column] = "---";
				} else {
					matrixResultIntern[row][column] = matrixResultTempIntern[k][column];
				}
			}
			if (row != rowToCut) {
				k++;
			}
		}
		StringMatrix matrixResult = language.newStringMatrix(
				new Offset(80, 30, "resultMatrix", AnimalScript.DIRECTION_SW), matrixResultIntern, "matrixResult", null,
				matrixProps);
		language.nextStep();
		// ------------------------------------------------------------------------------------------------------------

		status.setText("[STATUS] Die Basisvektoren der Summe von U und W befinden sich jeweils als Zeile oben links",
				null, null);
		sourceCode.highlight(27);
		String[][] sumResult1Intern = new String[matrixFinal[0].length / 2][1];

		// copy the result in the vector and highlight the matrix elements
		for (int column = 0; column < matrixResultIntern[0].length; column++) {
			if (matrixResultIntern[0][column].equals("|")) {
				break;
			}
			sumResult1Intern[column][0] = matrixResultIntern[0][column];
			matrixResult.highlightElem(0, column, null, null);
		}
		language.newStringMatrix(new Offset(400, 0, "matrixResult", AnimalScript.DIRECTION_NE), sumResult1Intern,
				"sumResult1", null, matrixProps);
		language.newText(new Offset(-22, 34, "sumResult1", AnimalScript.DIRECTION_NW), "=", "Gleich3", null, textProps);
		language.newText(new Offset(-117, 0, "Gleich3", AnimalScript.DIRECTION_NW), "Basis für U + W", "textResultSum",
				null, textProps);
		language.nextStep();
		// ------------------------------------------------------------------------------------------------------------

		String[][] sumResult2Intern = new String[matrixFinal[0].length / 2][1];

		// copy the result in the vector and highlight the matrix elements
		for (int column = 0; column < matrixResultIntern[0].length; column++) {
			if (matrixResultIntern[1][column].equals("|")) {
				break;
			}
			sumResult2Intern[column][0] = matrixResultIntern[1][column];
			matrixResult.unhighlightElem(0, column, null, null);
			matrixResult.highlightElem(1, column, null, null);
		}
		language.newStringMatrix(new Offset(25, 0, "sumResult1", AnimalScript.DIRECTION_NE), sumResult2Intern,
				"sumResult2", null, matrixProps);
		language.newText(new Offset(-15, 34, "sumResult2", AnimalScript.DIRECTION_NW), ",", "Komma3", null, textProps);
		language.nextStep();
		// ------------------------------------------------------------------------------------------------------------

		TrueFalseQuestionModel whereIntersectionResult = new TrueFalseQuestionModel("whereIntersectionResult", false,
				1);
		whereIntersectionResult.setPrompt(
				"Die Basisvektoren der Schnittmenge befinden sich jeweils als Zeile oben rechts  neben den Summenvektoren.");
		language.addTFQuestion(whereIntersectionResult);

		String[][] sumResult3Intern = new String[matrixFinal[0].length / 2][1];

		// copy the result in the vector and highlight the matrix elements
		for (int column = 0; column < matrixResultIntern[0].length; column++) {
			if (matrixResultIntern[2][column].equals("|")) {
				break;
			}
			sumResult3Intern[column][0] = matrixResultIntern[2][column];
			matrixResult.unhighlightElem(1, column, null, null);
			matrixResult.highlightElem(2, column, null, null);
		}
		language.newStringMatrix(new Offset(25, 0, "sumResult2", AnimalScript.DIRECTION_NE), sumResult3Intern,
				"sumResult3", null, matrixProps);
		language.newText(new Offset(-15, 34, "sumResult3", AnimalScript.DIRECTION_NW), ",", "Komma4", null, textProps);
		language.nextStep();
		// ------------------------------------------------------------------------------------------------------------

		status.setText(
				"[STATUS] Die Basisvektoren der Schnittmenge von U und W befinden sich jeweils als Zeile unten rechts",
				null, null);
		sourceCode.unhighlight(27);
		sourceCode.highlight(28);

		// unhighlight the matrix elements
		for (int column = 0; column < matrixResultIntern[0].length; column++) {
			if (matrixResultIntern[2][column].equals("|")) {
				break;
			}
			matrixResult.unhighlightElem(2, column, null, null);
		}
		String[][] intersectionResultIntern = new String[matrixFinal[0].length / 2][1];

		// copy the result in the vector and highlight the matrix elements
		boolean copy = false;
		int l = 0;
		for (int column = 0; column < matrixResultIntern[0].length; column++) {
			// only after the pipe copy
			if (copy) {
				intersectionResultIntern[l][0] = matrixResultIntern[rowToCut + 1][column];
				matrixResult.highlightElem(rowToCut + 1, column, null, null);
				l++;
			}

			if (matrixResultIntern[rowToCut + 1][column].equals("|")) {
				copy = true;
			}
		}
		language.newStringMatrix(new Offset(0, 50, "sumResult1", AnimalScript.DIRECTION_SW), intersectionResultIntern,
				"intersectionResult", null, matrixProps);
		language.newText(new Offset(-22, 34, "intersectionResult", AnimalScript.DIRECTION_NW), "=", "Gleich4", null,
				textProps);
		language.newText(new Offset(-171, 0, "Gleich4", AnimalScript.DIRECTION_NW), "Basis der Schnittmenge",
				"textResultIntersection", null, textProps);
		language.nextStep();
		// ------------------------------------------------------------------------------------------------------------

		// show the conclusion of the algorithm
		TrueFalseQuestionModel manyResult = new TrueFalseQuestionModel("manyResult", true, 1);
		manyResult.setPrompt("Gibt es mehrere mögliche Ergebnisse?");
		language.addTFQuestion(manyResult);
		language.hideAllPrimitives();
		title.show();
		tRect.show();
		language.newText(new Offset(20, 40, "title", AnimalScript.DIRECTION_SW), "Fazit", "conclusionHeader", null,
				headerProps);
		SourceCode conclusion = language.newSourceCode(new Offset(0, 0, "conclusionHeader", AnimalScript.DIRECTION_SW),
				"conclusion", null, sourceCodeProps);
		conclusion.addCodeLine(
				"Der Zassenhaus-Algorithmus ist nicht der einzigste Algorithmus um die Summe zweier Untervektorräume auszurechnen.",
				null, 0, null);
		conclusion.addCodeLine(
				"Besonders einfach gestaltet sich die Berechnung der Summe, falls die beiden Untervektorräume U und W als lineare Hülle gegeben sind.",
				null, 0, null);
		conclusion.addCodeLine("Dann ist die Summe gegeben duch die Zusammenlegung beider Räume.", null, 0, null);
		conclusion.addCodeLine("Der Schnitt lässt sich dann durch ein lineares Gleichungssystem lösen.", null, 0, null);
		conclusion.addCodeLine("", null, 0, null);
		conclusion.addCodeLine(
				"Bei dieser Animation ist absichtlich nicht näher auf die Berechnung der Zeilenstufenform eingegangen,",
				null, 0, null);
		conclusion.addCodeLine("da dies ein eigener Algorithmus ist. Um mehr zur Berechnung einer Zeilenstufenform",
				null, 0, null);
		conclusion.addCodeLine(
				"zu erfahren, gibt es in Animal bereits die sehr gute Animation zum Gaußschen Eliminationsverfahrens.",
				null, 0, null);
		language.nextStep("Fazit");
	}

	public static void main(String[] args) {
		// Generator erzeugen
		Generator generator = new ZassenhausAPI();

		// Animal mit Generator starten
		Animal.startGeneratorWindow(generator);
	}

	public void init() {
		// Create a new language object for generating animation code
		// this requires type, name, author, screen width, screen height
		language = new AnimalScript("Zassenhaus-Algorithmus", "Peter Reinhardt", 1024, 768);
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		// This initializes the step mode. Each pair of subsequent steps has to
		// be divdided by a call of lang.nextStep();
		language.setStepMode(true);
		language.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
		intVektorU1 = (int[]) primitives.get("intVektorU1");
		intVektorU2 = (int[]) primitives.get("intVektorU2");
		intVektorW1 = (int[]) primitives.get("intVektorW1");
		intVektorW2 = (int[]) primitives.get("intVektorW2");

		// properties
		codeRectProps = (RectProperties) props.getPropertiesByName("sourceRect");
		sourceCodeProps = (SourceCodeProperties) props.getPropertiesByName("sourceCode");
		matrixProps = (MatrixProperties) props.getPropertiesByName("matrixProperties");
		titleRectProps = (RectProperties) props.getPropertiesByName("titleRect");
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 20));
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 15));

		double[][] u1 = new double[intVektorU1.length][1];
		double[][] u2 = new double[intVektorU2.length][1];
		double[][] w1 = new double[intVektorW1.length][1];
		double[][] w2 = new double[intVektorW2.length][1];

		for (int i = 0; i < intVektorU1.length; i++) {
			u1[i][0] = intVektorU1[i];
			u2[i][0] = intVektorU2[i];
			w1[i][0] = intVektorW1[i];
			w2[i][0] = intVektorW2[i];
		}

		start(u1, u2, w1, w2);
		language.finalizeGeneration();

		return language.toString();
	}

	public String getName() {
		return "Zassenhaus-Algorithmus";
	}

	public String getAlgorithmName() {
		return "Zassenhaus-Algorithmus";
	}

	public String getAnimationAuthor() {
		return "Peter Reinhardt";
	}

	public String getDescription() {
		return "Der Zassenhaus-Algorithmus ist ein Algorithmus aus dem mathematischen Teilgebiet der Linearen Algebra."
				+ "\n"
				+ "Es ist ein wichtiges Verfahren zur Bestimmung von Schnitt- und Summenbasen bei gegebenen Erzeugersystem der beiden Eingabevektoren."
				+ "\n"
				+ "Der Algorithmus schafft es mit ein paar wenigen Matrixumformungen gleichzeitig Summe und Schnitt für die Vektoren zu berechnen."
				+ "\n" + "Er benutzt für die notwendige Zeilenstufenform den Gauss-Algorithmus." + "\n"
				+ "Nach der Berechnung kann die L&oumlsungsmenge leicht abgelesen werden.";
	}

	public String getCodeExample() {
		return "Grundkonzept des Zassenhaus-Algorithmus" + "\n" + "\n" + "Voraussetzung:" + "\n"
				+ "	Sei U=(u1,...,uk) und W=(w1,...,wk) zwei Teilr&aunlume durch Erzeugendensystem gegeben." + "\n"
				+ "\n" + "Der Algorithmus:" + "\n" + "	Man definiere die Matrix M als" + "\n" + "		( u1^T | u1^T )"
				+ "\n" + "		(   ...    |   ...    )" + "\n" + "		( uk^T | uk^T )" + "\n" + "		( w1^T | w1^T )"
				+ "\n" + "		(   ...    |   ...    )" + "\n" + "		( wk^T | wk^T )" + "\n" + "\n"
				+ "	Man führe dann Zeilenumformungen auf dieser Matrix aus, bis sie in der folgenden Form ist" + "\n"
				+ "		(      s1          |             *              )" + "\n"
				+ "		(        ...         |             *              )" + "\n"
				+ "		( s dim(U+V) |             *              )" + "\n" + "		(----------|--------------)" + "\n"
				+ "		(        0          |           d1            )" + "\n"
				+ "		(        0          |           ...             )" + "\n"
				+ "		(        0          | d dim(U &cap V) )" + "\n" + "		(----------|--------------)" + "\n"
				+ "		(        0          |            *              )" + "\n"
				+ "		(        0          |            *              )" + "\n"
				+ "		(        0          |            *               )" + "\n"
				+ "	wobei s dim(U+V) ungleich 0 und d dim(U Schnitt V) ungleich 0 gilt." + "\n"
				+ "	Die Bl&oumlcke, welche die si und di enthalten, müssen au&szligerdem in strikter Zeilenstufenform sein."
				+ "\n" + "	" + "\n" + "Ergebniss:" + "\n" + "	Nun ist (s1,...,s dim(U+V) eine Basis von U + V" + "\n"
				+ "	und (d1,...,d dim(U &cap V) ist eine Basis von U &cap V" + "\n";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.GERMAN;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}
}