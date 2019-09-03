/*
 * SpaltenminimumMethode.java
 * Yue Hu, Xinyu Liu, 2017 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */

package generators.misc.spaltenminimumMethode;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import interactionsupport.models.FillInBlanksQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.Locale;

import algoanim.primitives.IntArray;
import algoanim.primitives.IntMatrix;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;

import java.util.Hashtable;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

/**
 * SpaltenminimumMethode
 * 
 * @author Yue Hu, Xinyu Liu
 *
 */
public class SpaltenminimumMethode implements ValidatingGenerator{
	private Language language;
	private int[][] KostenMatrix;
	private ArrayProperties AnbieterPro;
	private ArrayProperties NachfragerPro;
	private MatrixProperties KostenMatrixPro;
	private int[] Nachfrager;
	private int[] Anbieter;

	/**
	 * Constructor
	 * 
	 * @param language
	 */
	public SpaltenminimumMethode(Language language) {
		this.language = language;
		language.setStepMode(true);
	}

	/**
	 * default Constructor
	 */
	public SpaltenminimumMethode() {
		init();
		language.setStepMode(true);
	}

	public void init() {
		language = new AnimalScript("SpaltenminimumMethode",
				"Yue Hu, Xinyu Liu", 800, 600);
	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		KostenMatrix = (int[][]) primitives.get("KostenMatrix");
		AnbieterPro = (ArrayProperties) props
				.getPropertiesByName("AnbieterPro");
		NachfragerPro = (ArrayProperties) props
				.getPropertiesByName("NachfragerPro");
		KostenMatrixPro = (MatrixProperties) props
				.getPropertiesByName("KostenMatrixPro");
		Nachfrager = (int[]) primitives.get("Nachfrager");
		Anbieter = (int[]) primitives.get("Anbieter");

		int[][] a = new int[KostenMatrix.length][KostenMatrix[0].length];
		SpaltenminimumMethode n = new SpaltenminimumMethode(language);

		if (!validateInput(props, primitives)) {

			SourceCodeProperties title = new SourceCodeProperties();
			title.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
			title.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
					"Monospaced", Font.PLAIN, 30));
			title.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
					Color.RED);
			title.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
			SourceCode our_title = language.newSourceCode(
					new Coordinates(0, 0), "sourceCode", null, title);
			String tem = "Fehler!!!";
			our_title.addCodeLine(tem, null, 0, null);
			String tem0 = "1. Alle Elemente sollen >= 0(aber alle Elemente sollen nicht gleichzeitig 0 sein)!";
			our_title.addCodeLine(tem0, null, 0, null);
			String tem2 = "2. Die Summe von Anbieter und die Summe von Nachfrager sollen gleich sein!";
			our_title.addCodeLine(tem2, null, 0, null);
			String tem3 = "3. (m*n) KostenMatrix: m = Elenment-Anzahl von Anbiter; n = Elenment-Anzahl von Nachfrager";
			our_title.addCodeLine(tem3, null, 0, null);
			language.finalizeGeneration();
			return language.toString();

		} else {
			n.algorithm(KostenMatrix, a, Anbieter, Nachfrager);
			language.finalizeGeneration();
			return language.toString();
		}

	}


    /**
     * Algorithm for SpaltenminimumMethode
     * @param kostenMatrix
     * @param a
     * @param provider
     * @param consumer
     */
	public void algorithm(int[][] kostenMatrix, int[][] a, int[] provider,
			int[] consumer) {
		// Title
/*		SourceCodeProperties title = new SourceCodeProperties();
		title.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		title.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
				Font.PLAIN, 30));
		title.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		title.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		SourceCode our_title = language.newSourceCode(new Coordinates(0, 0),
				"sourceCode", null, title);
		our_title.addCodeLine("SpaltenminimumMethode", null, 0, null);*/
		TextProperties title = new TextProperties();
		title.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
				Font.PLAIN, 25));
		Text our_title = language.newText(new Coordinates(10, 20),
				"SpaltenminimumMethode", "SpaltenminimumMethode", null, title);
		
		// Rect
		RectProperties rect = new RectProperties();
		Rect our_rect = language.newRect(new Coordinates(0, 20),
				new Coordinates(340, 70), "null", null, rect);
		//Einleitungsseite
		TextProperties einleitung = new TextProperties();
		Text einleitung0 = language.newText(new Coordinates(10, 80),
				"Einleitung: ", "0", null, einleitung);
		Text einleitung1 = language.newText(new Coordinates(10, 110),
				"Spaltenminimum-Methode ist ein Eroeffnungsverfahren fuer das Transportproblem.", "1", null, einleitung);
		Text einleitung2 = language.newText(new Coordinates(10, 140),
				"Die Spalten (Nachfrager) des Transporttableaus werden in der gegebenen Reihenfolge zyklisch ueberprueft.", "2", null, einleitung);
		Text einleitung3 = language.newText(new Coordinates(10, 170),
				"Falls der Nachfrager noch Bedarf aufweist, wird die guenstigste Transportverbindung von", "3", null, einleitung);
		Text einleitung4 = language.newText(new Coordinates(10, 200),
				"einem noch lieferfaehigen Anbieter aus eingerichtet und die groesstmoegliche Transportmenge vorgesehen.", "4", null, einleitung);
		
		language.nextStep();
		einleitung0.hide();
		einleitung1.hide();
		einleitung2.hide();
		einleitung3.hide();
		einleitung4.hide();
		
		
		
		// Create Matrix: coordinates, data, name, display options,
		// default properties
		// first, set the visual properties (somewhat similar to CSS)
		MatrixProperties matrixProps = new MatrixProperties();
		matrixProps.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
		matrixProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		matrixProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		matrixProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		matrixProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
				Color.BLACK);
		matrixProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
				Color.RED);
		matrixProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
				Color.YELLOW);

		// now, create the IntMatirx object, linked to the properties
		// kostenMatrix

		MatrixProperties kmProps = new MatrixProperties();
		kmProps.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "matrix");
		// Matrix on the left
		IntMatrix km = language.newIntMatrix(new Coordinates(470, 280),
				kostenMatrix, "intMatrix", null, kmProps);

		// variable matrix in the mittel
		IntMatrix im = language.newIntMatrix(new Coordinates(805, 150), a,
				"intMatrix", null, matrixProps);

		int m = a.length;
		int n = a[0].length;

		// ============== A B Array ; =================
		ArrayProperties arrayProps = new ArrayProperties();
		arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
				Color.BLACK);
		arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
				Color.RED);
		arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
				Color.GREEN);
		IntArray ia_pro = language.newIntArray(new Coordinates(590, 120),
				provider, "Angebotsmengen a = ", null, arrayProps);
		IntArray ia_con = language.newIntArray(new Coordinates(590, 180),
				consumer, "Nachfragemengen b = ", null, arrayProps);
		// set array color
		ia_pro.highlightCell(0, provider.length - 1, null, null);
		ia_con.highlightCell(0, consumer.length - 1, null, null);
		// ========Text Kostenmatrix C = C_ij==========
		TextProperties textProps = new TextProperties();
		Text pro = language.newText(new Coordinates(460, 120),
				"Angebotsmengen a = ", "Angebotsmengen a = ", null, textProps);
		Text con = language
				.newText(new Coordinates(460, 180), "Nachfragemengen b = ",
						"Nachfragemengen b = ", null, textProps);
		Text kmText = language.newText(new Coordinates(460, 240),
				"Kostenmatrix C = (C_ij) =:", "Kostenmatrix C = (C_ij) =:",
				null, textProps);
		// text Nachfrager Anbieter
		Text nfText = language.newText(new Coordinates(800, 100), "Nachfrager",
				"Nachfrager", null, textProps);
		Text anText = language.newText(new Coordinates(720, 160 + m * 15),
				"Anbieter", "Anbieter", null, textProps);

		// String Matrix for 1 2 3 4 a_i and b_j
		// ===============For spalteA===========================================
		MatrixProperties matrixPropsString = new MatrixProperties();
		matrixPropsString.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY,
				"table");
		matrixPropsString.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				Color.BLACK);
		matrixPropsString.set(AnimationPropertiesKeys.FILL_PROPERTY,
				Color.WHITE);
		matrixPropsString.set(AnimationPropertiesKeys.FILLED_PROPERTY,
				Boolean.TRUE);
		matrixPropsString.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
				Color.BLACK);
		matrixPropsString.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
				Color.RED);
		matrixPropsString.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
				Color.GREEN);
		matrixPropsString.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"Monospaced", Font.ITALIC, 15));
		// ===============For zeileB===========================================
		MatrixProperties matrixPropsString2 = new MatrixProperties();
		matrixPropsString2.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY,
				"table");
		matrixPropsString2.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				Color.BLACK);
		matrixPropsString2.set(AnimationPropertiesKeys.FILL_PROPERTY,
				Color.WHITE);
		matrixPropsString2.set(AnimationPropertiesKeys.FILLED_PROPERTY,
				Boolean.TRUE);
		matrixPropsString2.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
				Color.BLACK);
		matrixPropsString2.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
				Color.RED);
		matrixPropsString2.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
				Color.GREEN);
		matrixPropsString2.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"Monospaced", Font.ITALIC, 12));

		String[][] spalteAString = generateStirngMatrixInCol(n);
		String[][] zeileBString = generateStirngMatrixInRow(m);
		// Top spalteA a_i
		StringMatrix spalteA = language.newStringMatrix(new Coordinates(805,
				115), spalteAString, "zeileA", null, matrixPropsString);
		// left side zeileB b_j
		StringMatrix zeileB = language.newStringMatrix(
				new Coordinates(770, 150), zeileBString, "spalteB", null,
				matrixPropsString2);

		// first, set the visual properties for the source code
		SourceCodeProperties scProps = new SourceCodeProperties();
		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"Monospaced", Font.PLAIN, 15));
		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		// now, create the source code entity
		SourceCode sc = language.newSourceCode(new Coordinates(10, 100),
				"sourceCode", null, scProps);


		// Add the lines to the SourceCode object.
		// Line, name, indentation, display dealy
		sc.addCodeLine("Start: Alle Zeilen unmarkiert alle x_ij:= 0 j:= 1",
				null, 0, null); // 0
		sc.addCodeLine("while(true) do", null, 0, null);
		sc.addCodeLine("begin", null, 0, null);
		sc.addCodeLine("if(j = n) and (m+n-1) Basis-variablen bestimmt;", null,
				3, null); // 3
		sc.addCodeLine("break;", null, 6, null); // 3
		sc.addCodeLine("else", null, 3, null); // 3
		sc.addCodeLine("Suche in Spalte j das kleinste Element", null, 6, null); // 3
		sc.addCodeLine("c_hj aus unmarkierter Zeile h", null, 6, null); // 3
		sc.addCodeLine("x_hj:= min{a_h, b_j};", null, 6, null); // 3
		sc.addCodeLine("a_h := a_h - x_ij und b_j := b_j - x_ij;", null, 6,
				null); // 3
		sc.addCodeLine("if(a_h = 0) Markiere Zeile h", null, 6, null); // 3
		sc.addCodeLine("else j:= j+1;", null, 6, null); // 3
		sc.addCodeLine("end;", null, 0, null); // 8
		language.nextStep();

		try {
			// Start
			algo_start(im, km, a, kostenMatrix, provider, consumer, sc, ia_pro,
					ia_con);
		} catch (LineNotExistsException e) {
			e.printStackTrace();
		}
		km.hide();
		kmText.hide();
		our_title.hide();
		our_rect.hide();
		sc.hide();
		ia_pro.hide();
		ia_con.hide();
		pro.hide();
		con.hide();
		/*
		 * im.hide(); nfText.hide(); anText.hide(); spalteA.hide();
		 * zeileB.hide();
		 */
	}

	/**
	 * Get the smallest element from first column of a matrix
	 * @param kostenMatrix
	 * @return
	 */
	public int getSmallestfromFirstCol(int[][] kostenMatrix) {
		int res = Integer.MAX_VALUE;
		for (int i = 0; i < kostenMatrix.length; i++) {
			int tem = kostenMatrix[i][0];
			if (tem < res) {
				res = tem;
			}
		}

		return res;
	}

	/**
	 * Get the position of the smallest element from first column
	 * @param kostenMatrix
	 * @return
	 */
	public int getPosition_SmallestfromFirstCol(int[][] kostenMatrix) {
		int res = Integer.MAX_VALUE;
		int result = 0;
		for (int i = 0; i < kostenMatrix.length; i++) {
			int tem = kostenMatrix[i][0];
			if (tem < res) {
				res = tem;
				result = i;
			}
		}

		return result;
	}

    /**
     * Main Algorithm
     * @param im
     * @param kostenmatrix
     * @param changedMatrix
     * @param kostenMatrix
     * @param a
     * @param b
     * @param codeSupport
     * @param ia_pro
     * @param ia_con
     * @throws LineNotExistsException
     */
	private void algo_start(IntMatrix im, IntMatrix kostenmatrix,
			int[][] changedMatrix, int[][] kostenMatrix, int[] a, int[] b,
			SourceCode codeSupport, IntArray ia_pro, IntArray ia_con)
			throws LineNotExistsException {

		String marker = ""; // used to mark row
		String newMarker = "";
		int basisCounter = 0; // used to counter the determined basis variable
		int m = changedMatrix.length; // ROW
		int n = changedMatrix[0].length;// COL
		// int x_ij = 0;
		int j = 1;

		// Timing
		Timing defaultTiming = new TicksTiming(30);

		// Rect :move for km 
		RectProperties rectKM = new RectProperties();
		Rect out_rectKM = language.newRect(new Coordinates(490, 280),
				new Coordinates(35+430, 310 + 15 * m), "null", null, rectKM);

		
		// sourcecode
		codeSupport.highlight(0);

		// used to counter
		int[] memA = new int[m];
		int[] memB = new int[n];

		// set grid for calculate : B
		// =========================================

		MatrixProperties matrixBString = new MatrixProperties();
		matrixBString.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
		matrixBString.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		matrixBString.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		matrixBString
				.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		matrixBString.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
				Color.BLACK);
		matrixBString.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
				Color.RED);
		matrixBString.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
				Color.GREEN);
		matrixBString.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"Monospaced", Font.ITALIC, 15));
		// String[][] test = new String[5][n];
		String[][] calGridB = generateEmptyMatrix(m + 1, n);
		// matrix on the right of b_i
		StringMatrix gridB = language.newStringMatrix(new Coordinates(805,
				135 + 10 + m * 30), calGridB, "B", null, matrixBString);
		for (int col = 0; col < n; col++) {
			// gridB.setGridFillColor(0, col, Color.GREEN, null, null);
			gridB.put(0, col, "" + b[col], null, null);
		}

		String[][] calGridA = generateEmptyMatrix(m, n + 1);
		// matrix on the right side
		StringMatrix gridA = language.newStringMatrix(new Coordinates(
				760 + n * 36, 150), calGridA, "A", null, matrixBString);
		for (int row = 0; row < m; row++) {
			// gridA.setGridFillColor(row, 0, Color.GREEN, null, null);
			gridA.put(row, 0, "" + a[row], null, null);
		}

		// =========================================
		// Variable
		TextProperties VarProps = new TextProperties();

		Text variableText = language.newText(new Coordinates(720, 143 + 70 + 2
				* m * 30), "Variable:", "Variable:", null, VarProps);

		// markierte Zeilen:
		Text mzText = language.newText(new Coordinates(720, 143 + 100 + 2 * m
				* 30), "markierte Zeilen:", "markierte Zeilen", null, VarProps);
		// j
		Text jText = language.newText(new Coordinates(720, 143 + 130 + 2 * m
				* 30), "j := ", "j := ", null, VarProps);
		// h
		Text hText = language.newText(new Coordinates(720, 143 + 160 + 2 * m
				* 30), "h := ", "h := ", null, VarProps);

		// changed Variable

		Text change_mzText = language.newText(new Coordinates(830, 143 + 100
				+ 2 * m * 30), "keine", "markierte Zeilen", null, VarProps);
		// j
		Text change_jText = language.newText(new Coordinates(750, 143 + 130 + 2
				* m * 30), "1", "j := ", null, VarProps);
		// h
		Text change_hText = language.newText(new Coordinates(750, 143 + 160 + 2
				* m * 30), "", "h := ", null, VarProps);
		// ==========================================
		language.nextStep();

		//=============== Aufgabe 5.2==============================
		language.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
		FillInBlanksQuestionModel ques1 = new FillInBlanksQuestionModel("year");
		ques1.setPrompt("Was ist das kleinste Element aus Spalte 1 in KostenMatrix?");
		ques1.addAnswer("" + getSmallestfromFirstCol(kostenMatrix), 1,
				"Richtig! C_hj = " + getSmallestfromFirstCol(kostenMatrix)
						+ "; h = "
						+ (getPosition_SmallestfromFirstCol(kostenMatrix) + 1)
						+ "; j = 1");
		language.addFIBQuestion(ques1);
		// ===========================================================
		//Erklaren
		TextProperties erklaren = new TextProperties();
		erklaren.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
				Font.PLAIN, 20));
		erklaren.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
		while (true) {
			// source code
			Text our_erklaren = language.newText(new Coordinates(10, 143 + 100 + 2*m*30),
					"", "0", null, erklaren);
			if (j == n && basisCounter == (m + n - 1)) {
				codeSupport.unhighlight(0);
				codeSupport.highlight(4);
				our_erklaren.setText("Alle Zeilen sind schon mit # markiert! Iteration ist fertig!", null, null);
				language.nextStep();
				our_erklaren.hide();
				break;
			} else {
				int h = minFromUnmark(j, marker, kostenMatrix);
				//CHANGE KOSTEN MATRIX 05.05.2017
				kostenmatrix.highlightCell(h, j - 1, null, null);
				// set h
				codeSupport.unhighlight(0);
				//Erklaren
				int minElement = minElementFromUnmark(j, marker, kostenMatrix);
				our_erklaren.setText("In Kostenmatrix das kleiste Element in Spalte "+j+" ist: "+minElement+" aus Zeile"+" " +(h+1), null, null);
				codeSupport.highlight(6);
				codeSupport.highlight(7);
				language.nextStep();
				kostenmatrix.unhighlightCell(h, j - 1, null, null);

				change_hText.setText("" + (h + 1), null, null);

				int a_h = a[h];
				int b_j = b[j - 1];
				int x_hj = min(a_h, b_j);
				codeSupport.unhighlight(6);
				codeSupport.unhighlight(7);
				codeSupport.highlight(8);
				//Erklaren
				our_erklaren.setText("Vergleich "+ (h+1)+ " Element aus Array a und "+j+" Element aus Array b", null, null);
				// set min color
				gridA.highlightCell(h, memA[h], defaultTiming, defaultTiming);
				gridB.highlightCell(memB[j - 1], j - 1, defaultTiming,
						defaultTiming);

				changedMatrix[h][j - 1] = x_hj;
				// Set value in Matrix
				im.highlightCell(h, j - 1, null, defaultTiming);
				language.nextStep();

				im.highlightElem(h, j - 1, null, null);
				language.nextStep();
				im.put(h, j - 1, x_hj, null, null);
				//Erklaren
				our_erklaren.setText("Die Transortmenge von Anbieter "+(h+1)+" zu Nachfrager "+j+" ist: " +x_hj, null, null);
				language.nextStep();
				// unlight min color
				gridA.unhighlightCell(h, memA[h], null, null);
				gridB.unhighlightCell(memB[j - 1], j - 1, null, null);

				memA[h] = memA[h] + 1;
				memB[j - 1] = memB[j - 1] + 1;
				//Erklaren
				our_erklaren.setText("", null, null);
				language.nextStep();
				//Erklaren
				our_erklaren.setText("Subtraktion: "+a[h]+" - "+x_hj+" = " +(a[h] - x_hj)+"; "+b[j-1]+" - "+x_hj+" = " +(b[j - 1] - x_hj), null, null);
				a[h] = a[h] - x_hj;
				codeSupport.unhighlight(8);
				codeSupport.highlight(9);

				// set value in A
				gridA.put(h, memA[h], "" + a[h], null, null);
				gridA.highlightElem(h, memA[h], null, null);
				b[j - 1] = b[j - 1] - x_hj;
				// set value in B
				gridB.put(memB[j - 1], j - 1, "" + b[j - 1], null, null);
				gridB.highlightElem(memB[j - 1], j - 1, null, null);
				language.nextStep();
				
				codeSupport.unhighlight(9);
				gridA.unhighlightElem(h, memA[h], null, null);
				gridB.unhighlightElem(memB[j - 1], j - 1, null, null);
				if (a[h] == 0) {
					codeSupport.highlight(10);
					marker = marker + h;
					int newH = h + 1;
					newMarker = newMarker + newH;
					change_mzText.setText(newMarker, null, null);
					//
					gridA.put(h, memA[h] + 1, "#", null, null);
					//Erklaren
					our_erklaren.setText("Denn a[h] = 0, markiere Zeile "+ (h+1)+" mit #!", null, null);

				} else {
					codeSupport.highlight(11);
					j = j + 1;
					change_jText.setText("" + j, null, null);
					//more rect can change the value
					out_rectKM.moveBy(null, 18, 0, null, null);
					//Erklaren
					our_erklaren.setText("Denn a[h] = "+a[h]+" != 0, mache naechste Iteration von Kostenmatrix j = "+j, null, null);
				}
				language.nextStep();
				our_erklaren.hide();
				codeSupport.unhighlight(10);
				codeSupport.unhighlight(11);
				basisCounter = basisCounter + 1;
			}

			language.nextStep();

		}

		language.nextStep();
		gridB.hide();
		gridA.hide();
		mzText.hide();
		hText.hide();
		jText.hide();
		variableText.hide();
		change_mzText.hide();
		change_jText.hide();
		change_hText.hide();
		out_rectKM.hide();
		
		// Zusammenfassung
		// Zusammenfassung
		SourceCodeProperties scProps = new SourceCodeProperties();
		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"Monospaced", Font.PLAIN, 15));
		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		SourceCode zusammenfassung = language.newSourceCode(new Coordinates(
				200, 100), "sourceCode", null, scProps);
		// Zusammenfassung
		zusammenfassung.addCodeLine("Zusammenfassung:", null, 0, null);
		zusammenfassung.addCodeLine(
				"Zum Transport einheitlicher Objekte von mehreren Angebots-",
				null, 0, null);
		zusammenfassung.addCodeLine(
				"zu mehreren Nachfrageorten ist ein optimaler,", null, 0, null);
		zusammenfassung.addCodeLine("d. h. kostenminimaler Plan zu finden.",
				null, 0, null);
		zusammenfassung.addCodeLine(
				"Schauen Sie bitte den Plan auf rechter Seite!", null, 0, null);
		
		TextProperties zusam = new TextProperties();
        int position = 220;
		for(int i = 0;i < m;i ++){
			for(int p =0;p < n;p++){
				if(changedMatrix[i][p] != 0){
					System.out.println("====== i = " + i+ "    p= " + p);
					position = position + 20;
					Text zusammen = language.newText(new Coordinates(200, position), "Transportmenge von Anbieter "+ (i+1)+ " zu Nachfrager "+(p+1)+" is: " +changedMatrix[i][p], ""+i+p, null, zusam);	
				}	
			}
		}
	}

	/**
	 * Generator a empty String matrix
	 * @param row
	 * @param col
	 * @return
	 */
	private String[][] generateEmptyMatrix(int row, int col) {
		String[][] res = new String[row][col];
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				res[i][j] = "";
			}
		}
		return res;
	}
    /**
     * Generator a only row 
     * @param len
     * @return
     */
	private String[][] generateStirngMatrixInRow(int len) {
		String[][] res = new String[len + 1][1];
		for (int i = 0; i < len; i++) {
			int tem = i + 1;
			res[i][0] = "" + tem;
		}
		res[len][0] = "b_i";
		return res;
	}
    /**
     * Generator a only column
     * @param len
     * @return
     */
	private String[][] generateStirngMatrixInCol(int len) {
		String[][] res = new String[1][len + 1];
		for (int i = 0; i < len; i++) {
			int tem = i + 1;
			res[0][i] = "" + tem;
		}
		res[0][len] = "  a_j";
		return res;
	}
    /**
     * Get the minimal element from unchecked line 
     * @param j
     * @param mark
     * @param matrix
     * @return
     */
	private int minFromUnmark(int j, String mark, int[][] matrix) {
		int res = 0;
		int tem = Integer.MAX_VALUE;
		for (int i = 0; i < matrix.length; i++) {
			if (!mark.contains("" + i) && matrix[i][j - 1] < tem) {
				tem = matrix[i][j - 1];
				res = i;
			}
		}
		return res;
	}
	
	private int minElementFromUnmark(int j, String mark, int[][] matrix) {
		int res = 0;
		int tem = Integer.MAX_VALUE;
		for (int i = 0; i < matrix.length; i++) {
			if (!mark.contains("" + i) && matrix[i][j - 1] < tem) {
				tem = matrix[i][j - 1];
				res = tem;
			}
		}
		return res;
	}

	private int min(int a, int b) {
		if (a <= b)
			return a;
		else
			return b;
	}

   //==================================================================
	public String getName() {
		return "SpaltenminimumMethode";
	}

	public String getAlgorithmName() {
		return "SpaltenminimumMethode";
	}

	public String getAnimationAuthor() {
		return "Yue Hu, Xinyu Liu";
	}

	public String getDescription() {
		return "Diese Methode ist ein Er?ffnungsverfahren für das Transportproblem. Die Spalten (Nachfrager) des Transporttableaus werden in der gegebenen Reihenfolge zyklisch überprüft. Falls der Nachfrager noch Bedarf aufweist, wird die günstigste Transportverbindung von einem noch lieferf?higen Anbieter aus eingerichtet und die gr??tm?gliche Transportmenge vorgesehen.";
	}

	public String getCodeExample() {
		return "	Start: Alle Zeilen unmarkiert alle x_ij:= 0 j:= 1"
				+ "\n"
				+ "	while(ture) do"
				+ "\n"
				+ "	begin"
				+ "\n"
				+ "	   if(j = n) and (m+n-1) Basis-variablen bestimmt;"
				+ "\n"
				+ "	      break;"
				+ "\n"
				+ "	   else"
				+ "\n"
				+ "	      Suche in Spalte j das kleinste Element c_hj aus unmarkierter Zeile h"
				+ "\n" + "	      x_hj:= min{a_h, b_j};" + "\n"
				+ "	      a_h := a_h - x_ij und b_j := b_j - x_ij;" + "\n"
				+ "	      if(a_h = 0) Markiere Zeile h" + "\n"
				+ "	      else j:= j+1;" + "\n" + "	end;";
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
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	/**
	 * Check all of the errors
	 * @param props
	 * @param primitives
	 * @return
	 */
	// generater parameter
	@Override
	public boolean validateInput(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		KostenMatrix = (int[][]) primitives.get("KostenMatrix");
		Nachfrager = (int[]) primitives.get("Nachfrager");
		Anbieter = (int[]) primitives.get("Anbieter");
		boolean nachfrager_neg = hasArrayNegativ(Nachfrager);
		boolean anbieter_neg = hasArrayNegativ(Anbieter);
		boolean kostenMatrix_neg = hasMatrixNegativ(KostenMatrix);
		boolean checkSum = checkSumEqual(Anbieter, Nachfrager);
		boolean checkZero = checkAllZero(Anbieter, Nachfrager);

		if (nachfrager_neg || anbieter_neg || kostenMatrix_neg || checkZero) {
			return false;
		}

		else if (!checkSum) {
			return false;
		} else if (KostenMatrix.length != Anbieter.length
				|| KostenMatrix[0].length != Nachfrager.length) {
			return false;
		} else {
			return true;
		}

	}

	public boolean checkAllZero(int[] input1, int[] input2) {
		int size1 = input1.length;
		int zero1 = 0;

		int size2 = input2.length;
		int zero2 = 0;
		for (int i = 0; i < size1; i++) {
			if (input1[i] == 0) {
				zero1 = zero1 + 1;
			}
		}
		for (int j = 0; j < size1; j++) {
			if (input2[j] == 0) {
				zero2 = zero2 + 1;
			}
		}

		if (zero1 == size1 || zero2 == size2) {
			return true;
		}

		return false;
	}

	public boolean checkSumEqual(int[] input1, int[] input2) {
		int sum1 = 0;
		int sum2 = 0;
		for (int i = 0; i < input1.length; i++) {
			sum1 = sum1 + input1[i];
		}
		for (int j = 0; j < input2.length; j++) {
			sum2 = sum2 + input2[j];
		}
		if (sum1 != sum2) {
			return false;
		}
		return true;
	}

	public boolean hasMatrixNegativ(int[][] input) {
		for (int i = 0; i < input.length; i++) {
			for (int j = 0; j < input[i].length; j++) {
				if (input[i][j] < 0) {
					return true;
				}
			}
		}

		return false;
	}

	public boolean hasArrayNegativ(int[] input) {
		for (int i = 0; i < input.length; i++) {
			if (input[i] < 0) {
				return true;
			}
		}
		return false;
	}
}