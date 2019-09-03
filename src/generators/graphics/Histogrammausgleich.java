package generators.graphics;

/*
 * Historgrammausgleich.java
 * Niklas Grimm, Nicolai Minter, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
//package generators.graphics;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;

import javax.swing.JOptionPane;

import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;

import java.awt.Color;
import java.awt.Font;
//import java.io.IOException;

import algoanim.primitives.IntMatrix;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Square;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.SquareProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;
//import animal.main.Animal;

public class Histogrammausgleich implements  ValidatingGenerator {
	private Language lang;
	private int[][] intMatrix;
	private SourceCodeProperties scProps;
	private MatrixProperties matrixProps;
	private TextProperties tp = new TextProperties();
	Variables vars;
	
	@Override
	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		if(!checkCorrect((int[][]) primitives.get("intMatrix"))) {
			JOptionPane.showMessageDialog(null, "Es d�rfen nur Zahlen von 0 bis 255 verwendet werden.", "Invalid input", JOptionPane.OK_OPTION);
			return false;
		}
		
		return true;
	}

	public void init() {
		lang = new AnimalScript("Histogrammausgleich", "Niklas Grimm, Nicolai Minter", 800, 600);
		// This initializes the step mode. Each pair of subsequent steps has to
		// be divdided by a call of lang.nextStep();
		lang.setStepMode(true);
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		intMatrix = (int[][]) primitives.get("intMatrix");
		scProps = (SourceCodeProperties)props.getPropertiesByName("sourceCode");
		matrixProps = (MatrixProperties)props.getPropertiesByName("matrixProps");
		histogram(intMatrix);
		return lang.toString();
	}

	public String getName() {
		return "Histogrammausgleich";
	}

	public String getAlgorithmName() {
		return "Histogrammausgleich";
	}

	public String getAnimationAuthor() {
		return "Niklas Grimm, Nicolai Minter";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.GERMAN;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPHICS);
	}

	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

	////////////////////////////////////////////////////////////////////
	///////////////////////// Code ////////////////////////////////
	////////////////////////////////////////////////////////////////////

	/**
	 * The concrete language object used for creating output
	 */

	/**
	 * Default constructor
	 * 
	 * @param l
	 *            the conrete language object used for creating output
	 */
	public Histogrammausgleich() {
		init();
	}

	private final String DESCRIPTION = "Beim Histogramm Ausgleich werden die Grauwerte des Bildes m�glichst gleichm��ig "
			+ "verteilt, um den Kontras zu verst�rken. "
			+ "Dabei wird im ersten Schritt jeder Pixel durchgegangen und gez�hlt wie oft "
			+ "welcher Wert vorkommt. Dabei wird an der i-ten Stelle im Array der Wert um 1 erh�ht, "
			+ "wenn der Pixelwert gleich i ist. Aus Platzgr�nden haben wir den Array in "
			+ "tabellarischer Form dargestellt. \n"

			+ "Im zweiten Schritt wird ein neues Array erstellt. Dann wird �ber das erste Array "
			+ "iteriert und die Summe der bisherigen Werte im Histrogram-Array mit 255 multipliziert "
			+ "und durch die gesamte Anzahl an Pixeln dividiert. Der neu errechnete Wert wird "
			+ "in das neue Array (look-up-table) gespeichert. \n"

			+ "Im letzten Schritt wird das Eingabebild durchgegangen und der "
			+ "Pixelwert (valueBefore) in der (look-up-table) an der Stelle (valueBefore) nachgeschaut. "
			+ "Der erhaltene Wert wird dann in das Resultatsbild eingetragen. \n"
			+ "Wir empfehlen Ihnen, eine Matrix mit mindestens 20 Eintr�gen zu verwenden.";

	private final String SOURCE_CODE = "public static void histogram(int[][] matrix) {" // 0
			+ "\n int width =matrix.length" + "\n int height = matrix[0].length;" + "\n int anzpixel= width*height;"
			+ "\n int[][] pic = matrix" + "\n int[] histogram = new int[256];"

			// read pixel intensities into histogram
			+ "\n for (int x = 0; x < width; x++) {" + "\n    for (int y = 0; y < height; y++) {"
			+ "\n   int valueBefore= matrix[x][y];" + "\n histogram[valueBefore]++;" + "\n}" + "\n}"

			+ "\n int sum =0;"
			// build a Lookup table LUT containing scale factor
			+ "\n float[] lut = new float[256];" + "\n for (int i=0; i < 256; ++i ) {" + "\n    sum += histogram[i];"
			+ "\n    lut[i] = sum * 255 / anzpixel;" + "\n}"

			// transform image using sum histogram as a Lookup table
			+ "\n for (int x = 0; x < width; x++) {" + "\n    for (int y = 0; y < height; y++) {"
			+ "\n        int valueBefore = matrix[x][y];" + "\n        int valueAfter = (int) lut[valueBefore];"
			+ "\n        pic[x][y] = valueAfter;" + "\n    }" + "\n}";

	/**
	 * default duration for swap processes
	 */
	public final Timing defaultDuration = new TicksTiming(30);

	@SuppressWarnings("unused")
  public void histogram(int[][] m) {
		
		vars = lang.newVariables();
		
	    vars.declare("int", "x", "0", "Aktueller X Wert");
	    vars.declare("int", "y", "0","Aktueller Y Wert");
	    vars.declare("int", "valueBefore", "0","Aktuelle valueBefore");
	    vars.declare("int", "sum", "0","Anzahl der schon betrachteten Zahlen");

		RectProperties rp = new RectProperties();
		rp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);

		
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 15));
		tp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);

		Rect invertRGBR = lang.newRect(new Coordinates(40, 40), new Coordinates(315, 80), "Histogrammausgleich", null,
				rp);
		invertRGBR.changeColor("fillColor", Color.YELLOW, null, null);

		Text invertRGB = lang.newText(new Coordinates(50, 50), "Histogrammausgleich", "invertRGB", null, tp);
		invertRGB.setFont(new Font("Monospaced", Font.PLAIN, 20), null, null);

		int[][] fm = new int[m.length][m[0].length];
		int[][] hm = new int[11][27];
		fillMatrix(hm);

		// now, create the source code entity
		SourceCode sc = lang.newSourceCode(new Coordinates(50, 100), "sourceCode", null, scProps);
		addSourceCode(sc);

		lang.nextStep("Einf�hrung in die Notation");
		// Matrix verst�ndnis beispiel

		Text example1 = lang.newText(new Offset(50, 0, sc, AnimalScript.DIRECTION_NE),
				"Normalerweise wird f�r den Histogrammausgleich ein Array verwendet, wie auch im Source Code zu ",
				"example", null, tp);
		Text example2 = lang.newText(new Offset(0, 15, example1, AnimalScript.DIRECTION_SW),
				"sehen ist (histogram und lut). Wir verwenden in der Animation eine Matrix f�r „histogram“ und „look-up-table (lut)“ ",
				"example", null, tp);
		Text example3 = lang.newText(new Offset(0, 15, example2, AnimalScript.DIRECTION_SW),
				"um sie besser darstellen zu k�nnen. Um diese zu verstehen, zeigen wir ihnen nun ein Beispiel ",
				"example", null, tp);
		Text example4 = lang.newText(new Offset(0, 15, example3, AnimalScript.DIRECTION_SW),
				"f�r die Zahlen 13 und 37.", "example", null, tp);

		IntMatrix beiMat = lang.newIntMatrix(new Offset(0, 100, example3, AnimalScript.DIRECTION_SW), hm,
				"intFinalMatrix", null, matrixProps);

		highlightMatrixIndex(beiMat);

		Text example5 = lang.newText(new Offset(0, 15, example4, AnimalScript.DIRECTION_SW), "10 + 3 = ", "example",
				null, tp);

		beiMat.setGridHighlightFillColor(0, 2, Color.blue, null, null);
		beiMat.setGridHighlightFillColor(4, 0, Color.blue, null, null);

		lang.nextStep();

		example5.setText("10 + 3 = 13.  Die Zahl 13 wurde gelb makiert.", null, null);
		beiMat.highlightCell(4, 2, null, null);
		beiMat.setGridHighlightFillColor(4, 2, Color.yellow, null, null);

		lang.nextStep();

		example5.hide();
		beiMat.setGridHighlightFillColor(0, 2, Color.gray, null, null);
		beiMat.setGridHighlightFillColor(4, 0, Color.gray, null, null);
		beiMat.unhighlightCell(4, 2, null, null);

		lang.nextStep();
		example5.show();
		example5.setText("30 + 7 = ", null, null);

		beiMat.setGridHighlightFillColor(0, 4, Color.blue, null, null);
		beiMat.setGridHighlightFillColor(8, 0, Color.blue, null, null);

		lang.nextStep();

		example5.setText("30 + 7 = 37.  Die Zahl 37 wurde gelb makiert.", null, null);
		beiMat.highlightCell(8, 4, null, null);
		beiMat.setGridHighlightFillColor(8, 4, Color.yellow, null, null);

		lang.nextStep();

		example1.setText("Wie beginnen nun mit dem Histogrammausgleich.", null, null);

		beiMat.hide();

		example2.hide();
		example3.hide();
		example4.hide();
		example5.hide();

		lang.nextStep();

		example1.hide();

		lang.nextStep("Start Berechnung");
		IntMatrix im = lang.newIntMatrix(new Offset(50, 0, sc, "NE"), m, "intMatrix", null, matrixProps);
		Text matrixT = lang.newText(new Offset(0, -20, im, "NW"), "matrix", "matrix", null, tp);
		
		IntMatrix ifm = lang.newIntMatrix(new Offset(0, 50, im, AnimalScript.DIRECTION_SW), fm, "intFinalMatrix", null,
				matrixProps);
		Text picT = lang.newText(new Offset(0, -20, ifm, "NW"), "pic", "pic", null, tp);
		

		// start a new step after the matrix was created
		lang.nextStep("f�llen des Histogram");

		IntMatrix histom = lang.newIntMatrix(new Offset(50, 0, im, AnimalScript.DIRECTION_NE), hm, "histoMatrix", null,
				matrixProps);
		
		Text histoT = lang.newText(new Offset(0, -20, histom, "NW"), "histogram", "histogram", null, tp);

		highlightMatrixIndex(histom);

		// start a new step after the matrix was created
		
		Text fillHistText = lang.newText(new Offset(0, -50, histom, "NW"),
				"Als erstes geht der Algorithmus die Eingabematrix Pixel f�r Pixel durch und z�hlt wie h�ufig "
				+ "ein Wert im Histogramm vorkommt.", "bla", null, tp);
		
		

		// fills the histogram
		fillHistom(sc,im,histom);
		
		fillHistText.hide();
		
		Text fillLutText0 = lang.newText(new Offset(0, -100, histom, "NW"),
				"Jetzt berechnet der Algorithmus die Look up Table,", "bla", null, tp);
		Text fillLutText1 = lang.newText(new Offset(0, -85, histom, "NW"),
				"indem er die zuvor erstellte Matrix durchgeht und jeden Wert mit 255 multipliziert. Anschlie�end ", "bla", null, tp);
		Text fillLutText2 = lang.newText(new Offset(0, -70, histom, "NW"),
				"dividiert er den Wert durch die gesamte Anzahl der Pixel.", "bla", null, tp);
		Text fillLutText3 = lang.newText(new Offset(0, -55, histom, "NW"),
				"Das Ergebnis wird in der neuen Matrix gespeichert. ", "bla", null, tp);
		
		sc.highlight(15);
		sc.highlight(16);
		
		lang.nextStep("f�llen der Look up Table");

		sc.unhighlight(15);
		sc.unhighlight(16);		
		IntMatrix lutm = lang.newIntMatrix(new Offset(0, 50, histom, AnimalScript.DIRECTION_SW), hm,
				"LookUpTableMatrix", null, matrixProps);
		
		Text lutT = lang.newText(new Offset(0, -20, lutm, "NW"), "lut", "lutT", null, tp);


		highlightMatrixIndex(lutm);

		int x = 0;
		int y = 0;

		// fills the look up table
		x = fillLUT(sc, lutm, histom ,im.getNrCols() * im.getNrRows());

		sc.highlight(17);
		sc.highlight(18);
		sc.highlight(19);
		sc.highlight(20);

		vars.set("valueBefore", "" + 0);
		
		
		Text all255 = lang.newText(new Offset(0, 15, histom, "SW"),
				"Die restlichen Pixel werden in der Look Up Table mit 255 aufgef�llt.", "bla", null, tp);
		all255.changeColor("Color", Color.red, null, null);

		lang.nextStep("f�llen der Ergebnismatrix");

		// fills the not filled fields form lutm with 255 in one step
		for (x += 1; x < lutm.getNrCols(); x++) {
			for (y = 1; y < lutm.getNrRows(); y++) {
				if (x == lutm.getNrCols() - 1 && y == 7)
					break;
				lutm.put(y, x, 255, null, null);
			}
		}
		
		all255.hide();
		
		sc.unhighlight(17);
		sc.unhighlight(18);
		sc.unhighlight(19);
		sc.unhighlight(20);
		sc.highlight(22);
		
		fillLutText0.hide();
		fillLutText1.hide();
		fillLutText2.hide();
		fillLutText3.hide();
		
		Text fillResText0 = lang.newText(new Offset(0, -100, histom, "NW"),
				"Im letzten Schritt wird die Eingabematrix durchgegangen und der", "bla", null, tp);
		Text fillResText1 = lang.newText(new Offset(0, -85, histom, "NW"),
			"Pixelwert (valueBefore) in der der Look-Up-Table an der Stelle valueBefore nachgeschaut.", "bla", null, tp);
		Text fillResText2 = lang.newText(new Offset(0, -70, histom, "NW"),
				"Der erhaltene Wert wird dann in das Resultatmatrix eingetragen.", "bla", null, tp);
		
		
		// fills the final Matrix ifm the correct numbers
		fillFM(sc, im, ifm, lutm);
		fillResText0.hide();
		fillResText1.hide();
		fillResText2.hide();
		
		lang.nextStep("Outro");
		
		matrixT.setText("Die urspr�ngliche Matrix (links) ist jetzt gleichverteilt (rechts)", null, null);
		Text outro = lang.newText(new Coordinates(400,85),
				"Wir haben zur Verdeutlichung die Werte der Matrix in Schwarz/Wei� dargestellt. ", "bla", null, tp);
		Text outro1 = lang.newText(new Coordinates(400,100),
				"Der Wert i wird als RGB-Wert (R: i, G: i, B: i) dargestellt.", "bla", null, tp);
		
		matrixT.moveTo("NE", null, new Coordinates(400,70), null, null);
		histom.hide();
		lutm.hide();
		lutT.hide();
		histoT.hide();
		picT.hide();
		
		im.moveTo("NE",null , new Coordinates(400,120 + 40 * im.getNrRows()) , null, null);
		ifm.moveTo("NE",null , new Coordinates(500 + im.getNrCols()*40 ,120 + 40 * im.getNrRows()) , null, null);
		
		
		drawOutro(im,400,120);
		drawOutro(ifm, 500 + im.getNrCols()*40 ,120);
		

		
		
	}

	protected String getAlgorithmDescription() {
		return DESCRIPTION;
	}

	protected String getAlgorithmCode() {
		return SOURCE_CODE;
	}

	public String getDescription() {
		return DESCRIPTION;
	}

	public String getCodeExample() {
		return SOURCE_CODE;
	}

	/*
	 * highlights the index for the matrix m also colors the never used fields black
	 */
	private void highlightMatrixIndex(IntMatrix m) {
		m.highlightCellColumnRange(0, 0, 26, null, null);
		m.highlightCellRowRange(0, 10, 0, null, null);
		m.highlightCellRowRange(7, 10, 26, null, null);

		for (int i = 1; i < m.getNrCols(); i++) {
			m.setGridHighlightFillColor(0, i, Color.gray, null, null);
			m.setGridHighlightBorderColor(0, i, Color.black, null, null);
			m.setGridTextColor(0, i, Color.white, null, null);
		}

		for (int i = 1; i < m.getNrRows(); i++) {
			m.setGridHighlightFillColor(i, 0, Color.gray, null, null);
			m.setGridHighlightBorderColor(i, 0, Color.black, null, null);
			m.setGridTextColor(i, 0, Color.white, null, null);
		}

		m.setGridHighlightFillColor(0, 0, Color.black, null, null);
		m.setGridHighlightFillColor(7, 26, Color.black, null, null);
		m.setGridHighlightFillColor(8, 26, Color.black, null, null);
		m.setGridHighlightFillColor(9, 26, Color.black, null, null);
		m.setGridHighlightFillColor(10, 26, Color.black, null, null);

		m.setGridHighlightBorderColor(0, 0, Color.black, null, null);
		m.setGridHighlightBorderColor(7, 26, Color.black, null, null);
		m.setGridHighlightBorderColor(8, 26, Color.black, null, null);
		m.setGridHighlightBorderColor(9, 26, Color.black, null, null);
		m.setGridHighlightBorderColor(10, 26, Color.black, null, null);
	}

	/*
	 * gets a int[][] and fills it with
	 * 
	 */
	private void fillMatrix(int[][] m) {
		for (int i = 0; i < m.length - 1; i++) {
			m[i + 1][0] = i;
		}
		for (int j = 0; j < m[0].length - 1; j++) {
			m[0][j + 1] = j * 10;
		}
	}

	/*
	 * @param m gets an IntMatrix lutm
	 * 
	 * @param n value of the searched number Bekommt die alte Nummer und wandelt sie
	 * in das lutm Format um und gibt die neue Nummer zur�ck
	 */
	private int getValueAfter(IntMatrix m, int n) {
		int i = 0;
		while (n >= 10) {
			n = n - 10;
			i++;
		}
		m.highlightCell(n + 1, i + 1, null, null);
		return m.getElement(n + 1, i + 1);
	}

	/*
	 * @param m gets an IntMatrix histom
	 * 
	 * @param n value of the searched number Bekommt eine Zahl und speichert sie die
	 * Anzahl der vorkommen der Zahl an der Stelle der Zahl
	 */
	private void addValuebefore(IntMatrix m, int n) {
		int i = 0;
		while (n >= 10) {
			n = n - 10;
			i++;
		}

		m.highlightCell(n + 1, i + 1, null, null);
		lang.nextStep();
		m.unhighlightCell(n + 1, i + 1, null, null);
		m.put(n + 1, i + 1, m.getElement(n + 1, i + 1) + 1, null, null);
	}

	private void unHighlightGVA(IntMatrix m, int n) {
		int i = 0;
		while (n >= 10) {
			n = n - 10;
			i++;
		}
		m.unhighlightCell(n + 1, i + 1, null, null);

	}

	/*
	 * gets a SourceCode Class and sets the Sourceode for the "Histogrammausgleich"
	 */
	private void addSourceCode(SourceCode sc) {
		sc.addCodeLine("Histogramm(int[][] matrix){", null, 0, null); // 0
		sc.addCodeLine("int width = matrix.length", null, 1, null); // 1
		sc.addCodeLine("int height = matrix[0].length;", null, 1, null); // 2
		sc.addCodeLine("int anzpixel = width*height;", null, 1, null); // 3
		sc.addCodeLine("int[][] pic = matrix;", null, 1, null); // 4
		sc.addCodeLine("int[] histogram = new int[256];", null, 1, null); // 5
		sc.addCodeLine("int sum;", null, 1, null); // 6
		sc.addCodeLine("", null, 1, null); // 7
		sc.addCodeLine("for (int x = 0; x < width; x++) {", null, 1, null); // 8
		sc.addCodeLine("for (int y = 0; y < height; y++) {", null, 2, null); // 9
		sc.addCodeLine("int valueBefore = matrix[x][y];", null, 3, null); // 10
		sc.addCodeLine("histogram[valueBefore]++;", null, 3, null); // 11
		sc.addCodeLine("}", null, 2, null); // 12
		sc.addCodeLine("}", null, 1, null); // 13
		sc.addCodeLine("", null, 1, null); // 14
		sc.addCodeLine("sum = 0;", null, 1, null); // 15
		sc.addCodeLine("float[] lut = new float[256];", null, 1, null); // 16
		sc.addCodeLine("for ( i=0; i < 256; ++i ) {", null, 1, null); // 17
		sc.addCodeLine("sum += histogram[i];", null, 2, null); // 18
		sc.addCodeLine("lut[i] = sum * 255 / anzpixel;", null, 2, null); // 19
		sc.addCodeLine("}", null, 1, null); // 20
		sc.addCodeLine("", null, 1, null); // 21
		sc.addCodeLine("for (int x = 0; x < width; x++) {", null, 1, null); // 22
		sc.addCodeLine("for (int y = 0; y < height; y++) {", null, 2, null); // 23
		sc.addCodeLine("int valueBefore = matrix[x][y];", null, 3, null); // 24
		sc.addCodeLine("int valueAfter = (int) lut[valueBefore];", null, 3, null); // 25
		sc.addCodeLine("pic[x][y] = valueAfter;", null, 3, null); // 26
		sc.addCodeLine("}", null, 2, null); // 27
		sc.addCodeLine("}", null, 1, null); // 28
		sc.addCodeLine("}", null, 0, null); // 29 \
	}
	
	/**
	 * fills the Look Up Table
	 * 
	 * @param sc the sourcecode for highlights
	 * @param lutm the Look Up Table Matrix to fill
	 * @param histom the values for teh lut
	 * @param allPixel height x weidth from the array
	 */
	private int fillLUT(SourceCode sc, IntMatrix lutm, IntMatrix histom, int allPixel) {
		Text fillLutText4 = null;
		int sum = 0;
		int x = 1;
		vars.set("x", "0");
		vars.set("y", "0");
		for (x = 1; x < lutm.getNrCols(); x++) {
			for (int y = 1; y < lutm.getNrRows(); y++) {
				
				// highlight for loop
				sc.highlight(17);
				if(y == 1)
					lang.nextStep("LookUpTableSpalte"+ (x-1));
				else
					lang.nextStep();
				
				vars.set("valueBefore", "" + ((y-1) + (x-1)* 10));
				
				// highlight for loop body
				sc.unhighlight(17);
				sc.highlight(18);
				sc.highlight(19);

				// there are no numbers after 255
				if (x == lutm.getNrCols() - 1 && y == 7)
					break;

				// there are no zeros in the code
				histom.highlightCell(y, x, null, null);
				histom.setGridHighlightFillColor(y, x, Color.YELLOW, null, null);
				lutm.highlightCell(y, x, null, null);
				lutm.setGridHighlightFillColor(y, x, Color.YELLOW, null, null);
				
				sum += histom.getElement(y, x);
				vars.set("sum", ""+ sum);
				int tmp = sum * 255 / allPixel;
				lutm.put(y, x, tmp, null, null);

				fillLutText4 = lang.newText(new Offset(0, -40, histom, "NW"),
						sum + " * 255 / " + allPixel + " = " + tmp, "bla", null, tp);
				lang.nextStep();

				// unhighlight loop body
				sc.unhighlight(18);
				sc.unhighlight(19);

				histom.unhighlightCell(y, x, null, null);
				lutm.unhighlightCell(y, x, null, null);
				fillLutText4.hide();
			}
			if (sum * 255 / (allPixel) == 255)
				break;
		}
		fillLutText4.hide();
		return x;
	}
	
	/**
	 * fills the Histogram
	 * @param sc
	 * @param im
	 * @param histom
	 */
	private void fillHistom(SourceCode sc, IntMatrix im, IntMatrix histom) {
		for (int x = 0; x < im.getNrRows(); x++) {
			sc.highlight(8, 0, false);
			lang.nextStep("HistogrammF�llenReihe"+x);
			vars.set("x", ""+x);
			sc.unhighlight(8);
			for (int y = 0; y < im.getNrCols(); y++) {
				vars.set("y", ""+y);
				sc.highlight(9);
				lang.nextStep();
				sc.toggleHighlight(9, 0, false, 10, 0);
				im.highlightCell(x, y, null, null);
				im.setGridHighlightFillColor(x, y, Color.YELLOW, null, null);
				int valueBefore = im.getElement(x, y);
				vars.set("valueBefore", "" + valueBefore);
				lang.nextStep();
				sc.toggleHighlight(10, 0, false, 11, 0);				
				addValuebefore(histom, valueBefore);
				lang.nextStep();
				im.unhighlightCell(x, y, null, null);
				sc.unhighlight(11);
			}
		}
	}
	
	/**
	 * fills the final Matrix ifm the correct numbers
	 * 
	 * @param sc the sourcecode for highlights
	 * @param im the start Matrix
	 * @param ifm the final Matrix
	 * @param lutm the lut 
	 */
	private void fillFM(SourceCode sc, IntMatrix im, IntMatrix ifm, IntMatrix lutm) {
		for (int x = 0; x < ifm.getNrRows(); x++) {
			vars.set("x", ""+x);
			// highlights outer loop
			sc.highlight(22);

			lang.nextStep("FinalMatrixF�llenReihe"+x);

			for (int y = 0; y < ifm.getNrCols(); y++) {
				vars.set("y", ""+y);
				// highlights inner loop line
				sc.toggleHighlight(22, 23);

				lang.nextStep();

				sc.toggleHighlight(23, 24);
				// gets the old value from the input matrix
				int valueBefore = im.getElement(x, y);

				// highlights the current im cell
				im.highlightCell(x, y, null, null);
				vars.set("valueBefore", "" + valueBefore);
				lang.nextStep();

				sc.toggleHighlight(24, 25);

				// gets the new value from the lut
				int valueAfter = (int) getValueAfter(lutm, valueBefore);
				
				lang.nextStep();

				sc.toggleHighlight(25, 26);
				ifm.highlightCell(x, y, null, null);
				// puts the valueafter in ifm
				ifm.put(x, y, valueAfter, null, null);

				lang.nextStep();

				unHighlightGVA(lutm, valueBefore);
				// unhighlights the ifm and im cell
				im.unhighlightCell(x, y, null, null);
				ifm.unhighlightCell(x, y, null, null);
				sc.unhighlight(26);
			}
		}
	}
	
	/**
	 * checks if all Values are between 0 and 255.
	 * @param matrix the matrix which should be checkes
	 * @return true if all Values are between 0 and 255 else false
	 */
	private boolean checkCorrect(int[][] matrix) {
		 for(int i = 0; i < matrix.length;i++){
	            for (int j = 0; j < matrix[i].length; j++){
	               if(matrix[i][j] >255 || matrix[i][j] < 0) 
	            	   return false;
	            }
	        }
		return true;
		
	}
	
	private void drawOutro(IntMatrix m, int xin, int yin) {
		SquareProperties sp = new SquareProperties();
		sp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		
		for (int x = 0; x < m.getNrCols(); x++) {
			int tmp = xin;
			for (int y = 0; y < m.getNrRows(); y++) {

				// get the RGB Value
				int val = m.getElement(y, x);
				Color col = new Color(val,val,val);

				// Create an Square
				Square s = lang.newSquare(new Coordinates(xin, yin), 40, "ni", null, sp);

				// change
				s.changeColor("fillColor", new Color(col.getRed(), col.getGreen(), col.getBlue()), null, null);

				//
				xin += 40;

			}
			yin += 40;
			xin = tmp;
		}
	}
	
//	public static void main(String[] args) throws IOException {
//		Generator generator = new Histogrammausgleich();
//		Animal.startGeneratorWindow(generator);
//	}

	
}
