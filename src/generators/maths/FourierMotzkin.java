package generators.maths;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.DoubleMatrix;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.AnimationType;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

/**
 * @author Melvin Laux
 */
public class FourierMotzkin implements ValidatingGenerator {

	private Language lang;
	private SourceCode sc;
	private MatrixProperties mp;
	private ArrayProperties iap;
	private int[][] matrixA;
	private int[] vectorB;
	private int k;

	private static final int SOURCE_CODE_OFFSET_X = 25;
	private static final int SOURCE_CODE_OFFSET_Y = 50;
	private static final int TITLE_OFFSET = 15;

	public FourierMotzkin() {
		initialise();
	}

	private void initialise() {

		// create language object for AnimalScript
		lang = Language.getLanguageInstance(AnimationType.ANIMALSCRIPT, "Fourier-Motzkin Transformation", "Melvin Laux",
				800, 600);
		lang.setStepMode(true);

		// make title
		TextProperties tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 20));
		lang.newText(new Coordinates(TITLE_OFFSET, TITLE_OFFSET), "THE FOURIER-MOTZKIN TRANSFORMATION", "title", null,
				tp);

		// setup the start page with the description
		lang.nextStep();
		tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 16));
		lang.newText(new Coordinates(25, 50), "The Fourier-Motzkin transformation is an algorithim that takes a",
				"description1", null, tp);
		lang.newText(new Offset(0, 25, "description1", AnimalScript.DIRECTION_NW),
				"polhedron represented as a set of inequalities P(A,b) and the index", "description2", null, tp);
		lang.newText(new Offset(0, 25, "description2", AnimalScript.DIRECTION_NW),
				"of a dimension to be orthogonally projected to zero. The output is a", "description3", null, tp);
		lang.newText(new Offset(0, 25, "description3", AnimalScript.DIRECTION_NW),
				"system of inequalities P(D,d) that represents exactly result of this.", "description4", null, tp);
		lang.newText(new Offset(0, 25, "description4", AnimalScript.DIRECTION_NW), "projection.", "description5", null,
				tp);

		lang.nextStep();

		lang.newText(new Offset(0, 50, "description5", AnimalScript.DIRECTION_NW),
				"To find such a matrix D and vector d, the row indices of the input", "algo11", null, tp);
		lang.newText(new Offset(0, 25, "algo11", AnimalScript.DIRECTION_NW),
				"matrix A are split up into three subsets (C₀, C₋, C₊) based", "algo12", null, tp);
		lang.newText(new Offset(0, 25, "algo12", AnimalScript.DIRECTION_NW),
				"on the sign of the k-th coefficient of each row.", "algo13", null, tp);

		lang.nextStep();

		lang.newText(new Offset(0, 50, "algo13", AnimalScript.DIRECTION_NW),
				"Rows with a k-th coefficient of zero do non have to be changed for the", "algo21", null, tp);
		lang.newText(new Offset(0, 25, "algo21", AnimalScript.DIRECTION_NW),
				"projection can be copied to the new matrix D. The remaining rows can be", "algo22", null, tp);
		lang.newText(new Offset(0, 25, "algo22", AnimalScript.DIRECTION_NW),
				" transformed by combining each row with a negative k-coefficient with", "algo23", null, tp);
		lang.newText(new Offset(0, 25, "algo23", AnimalScript.DIRECTION_NW),
				"every row of a positive k-coefficient and adding them to the matrix D.", "algo24", null, tp);
		lang.newText(new Offset(0, 25, "algo24", AnimalScript.DIRECTION_NW),
				"Analogous, the same must be done for the right hand side vectors.", "algo25", null, tp);

		lang.nextStep();

		lang.hideAllPrimitives();

		// make title
		tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 20));
		// tp.set(AnimationPropertiesKeys.FONT_PROPERTY, Font.);
		lang.newText(new Coordinates(TITLE_OFFSET, TITLE_OFFSET), "FOURIER-MOTZKIN TRANSFORMATION", "title", null, tp);

		// Create SourceCode: coordinates, name, display options,
		// default properties

		// first, set the visual properties for the source code
		SourceCodeProperties scProps = new SourceCodeProperties();
		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 16));
		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		// now, create the source code entity
		sc = lang.newSourceCode(new Coordinates(SOURCE_CODE_OFFSET_X, SOURCE_CODE_OFFSET_Y + TITLE_OFFSET),
				"sourceCode", null, scProps);

		// Add the lines to the SourceCode object.
		// Line, name, indentation, display delay
		sc.addCodeLine("for each row index l:", null, 0, null); // 0
		sc.addCodeLine("if Aₗₖ > 0 ", null, 1, null); // 1
		sc.addCodeLine("add l to C₊", null, 2, null); // 2
		sc.addCodeLine("if Aₗₖ < 0 ", null, 1, null); // 3
		sc.addCodeLine("add l to C₋", null, 2, null); // 4
		sc.addCodeLine("if Aₗₖ == 0 ", null, 1, null); // 5
		sc.addCodeLine("add l to C₀", null, 2, null); // 6
		sc.addCodeLine("row index l := 0", null, 0, null); // 7
		sc.addCodeLine("for each e in C₀:", null, 0, null); // 8
		sc.addCodeLine("Dₗ. := Aₑ.", null, 1, null); // 9
		sc.addCodeLine("dₗ. := bₑ.", null, 1, null); // 10
		sc.addCodeLine("l = l+1", null, 1, null); // 11
		sc.addCodeLine("for each (s,t)", null, 0, null); // 12
		sc.addCodeLine("Dₗ. := AₜₖAₛ. -	AₛₖAₜ.", null, 1, null); // 13
		sc.addCodeLine("dₗ. := Aₜₖbₛ. -	Aₛₖbₜ.", null, 1, null); // 14
		sc.addCodeLine("l = l+1", null, 1, null); // 15

		lang.nextStep();
	}

	private void transform(double[][] A, double[] b, int k) {

		mp = new MatrixProperties();
		mp.set(AnimationPropertiesKeys.GRID_ALIGN_PROPERTY, "center");
		mp.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "matrix");

		DoubleMatrix matA = lang.newDoubleMatrix(new Offset(50, 0, "sourceCode", AnimalScript.DIRECTION_NE), A, "matA",
				null, mp);
		lang.newText(new Offset(0, 10, "matA", AnimalScript.DIRECTION_SW), "Matrix A", "matALabel", null);

		double[][] data = new double[b.length][1];
		for (int i = 0; i < b.length; i++) {
			data[i][0] = b[i];
		}
		DoubleMatrix vecB = lang.newDoubleMatrix(new Offset(25, 0, "matA", AnimalScript.DIRECTION_NE), data, "vecB",
				null, mp);
		lang.newText(new Offset(0, 10, "vecB", AnimalScript.DIRECTION_SW), "Vector b", "vecBLabel", null);

		// construct index sets
		ArrayList<Integer> C0 = new ArrayList<Integer>();
		ArrayList<Integer> Cpos = new ArrayList<Integer>();
		ArrayList<Integer> Cneg = new ArrayList<Integer>();

		sc.highlight(0);
		lang.nextStep();

		sc.unhighlight(0);

		for (int i = 0; i < A.length; i++) {
			sc.highlight(1);
			matA.highlightElem(i, k, null, null);
			lang.nextStep();
			if (A[i][k] > 0) {
				sc.highlight(2);
				sc.unhighlight(1);
				Cpos.add(i);
				lang.nextStep();
				sc.unhighlight(2);
			}
			sc.highlight(3);
			sc.unhighlight(1);
			lang.nextStep();
			if (A[i][k] < 0) {
				sc.highlight(4);
				sc.unhighlight(3);
				lang.nextStep();
				Cneg.add(i);
				sc.unhighlight(4);
			}
			sc.highlight(5);
			sc.unhighlight(3);
			lang.nextStep();
			if (A[i][k] == 0) {
				sc.highlight(6);
				sc.unhighlight(5);
				lang.nextStep();
				C0.add(i);
				sc.unhighlight(6);
			}

			sc.unhighlight(5);
			matA.unhighlightElem(i, k, null, null);
		}

		lang.nextStep();

		iap = new ArrayProperties();
		iap.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.WHITE);
		iap.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.BLACK);
		iap.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.GREEN);
		iap.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 14));

		IntArray Cz = lang.newIntArray(new Offset(25, 50, "sourceCode", AnimalScript.DIRECTION_SW),
				C0.stream().mapToInt(i -> i).toArray(), "C0", null, iap);
		IntArray Cn = lang.newIntArray(new Offset(0, 35, "C0", AnimalScript.DIRECTION_SW),
				Cneg.stream().mapToInt(i -> i).toArray(), "Cneg", null, iap);
		IntArray Cp = lang.newIntArray(new Offset(0, 35, "Cneg", AnimalScript.DIRECTION_SW),
				Cpos.stream().mapToInt(i -> i).toArray(), "Cpos", null, iap);

		lang.newText(new Offset(-25, 5, "C0", AnimalScript.DIRECTION_NW), "C₀", "labelC0", null);
		lang.newText(new Offset(-25, 5, "Cneg", AnimalScript.DIRECTION_NW), "C₋", "labelCneg", null);
		lang.newText(new Offset(-25, 5, "Cpos", AnimalScript.DIRECTION_NW), "C₊", "labelCpos", null);

		double[][] D = new double[C0.size() + Cneg.size() * Cpos.size()][A[0].length];
		double[][] d = new double[C0.size() + Cneg.size() * Cpos.size()][1];

		DoubleMatrix matD = lang.newDoubleMatrix(new Offset(50, 50, "sourceCode", AnimalScript.DIRECTION_SE), D, "matD",
				null, mp);
		lang.newText(new Offset(0, 10, "matD", AnimalScript.DIRECTION_SW), "Matrix D", "matDLabel", null);

		DoubleMatrix vecD = lang.newDoubleMatrix(new Offset(25, 0, "matD", AnimalScript.DIRECTION_NE), d, "vecD", null,
				mp);
		lang.newText(new Offset(0, 10, "vecD", AnimalScript.DIRECTION_SW), "Vector d", "vecDLabel", null);

		int row = 0;

		lang.nextStep();

		sc.highlight(7);

		// iterate
		for (int i : C0) {

			sc.highlight(8);

			Cz.highlightCell(C0.indexOf(i), null, null);
			Cz.highlightElem(C0.indexOf(i), null, null);
			lang.nextStep();
			sc.unhighlight(7);
			sc.unhighlight(11);
			matA.highlightCellColumnRange(i, 0, matA.getNrCols() - 1, null, null);
			matD.highlightCellColumnRange(row, 0, matD.getNrCols() - 1, null, null);
			sc.unhighlight(8);
			sc.highlight(9);

			for (int j = 0; j < D[row].length; j++)
				matD.put(row, j, A[i][j], null, null);

			lang.nextStep();
			sc.unhighlight(9);
			sc.highlight(10);
			matA.unhighlightCellColumnRange(i, 0, matA.getNrCols() - 1, null, null);
			matD.unhighlightCellColumnRange(row, 0, matD.getNrCols() - 1, null, null);

			vecB.highlightCellColumnRange(i, 0, vecB.getNrCols() - 1, null, null);

			vecD.put(row, 0, b[i], null, null);
			vecD.highlightCell(row, 0, null, null);
			lang.nextStep();

			sc.unhighlight(10);
			sc.highlight(11);

			vecB.unhighlightCellColumnRange(i, 0, vecB.getNrCols() - 1, null, null);
			vecD.unhighlightCell(row, 0, null, null);

			Cz.unhighlightCell(C0.indexOf(i), null, null);
			Cz.unhighlightElem(C0.indexOf(i), null, null);

			matD.unhighlightCellColumnRange(row, 0, matD.getNrCols() - 1, null, null);
			vecD.unhighlightCell(row, 0, null, null);
			row++;
		}

		for (int s : Cneg) {

			Cn.highlightCell(Cneg.indexOf(s), null, null);
			Cn.highlightElem(Cneg.indexOf(s), null, null);
			for (int t : Cpos) {

				Cp.highlightCell(Cpos.indexOf(t), null, null);
				Cp.highlightElem(Cpos.indexOf(t), null, null);
				sc.highlight(12);
				lang.nextStep();
				sc.unhighlight(11);
				matA.highlightCellColumnRange(s, 0, matA.getNrCols() - 1, null, null);
				matA.highlightCellColumnRange(t, 0, matA.getNrCols() - 1, null, null);
				matD.highlightCellColumnRange(row, 0, matD.getNrCols() - 1, null, null);
				sc.unhighlight(12);
				sc.unhighlight(14);
				sc.highlight(13);

				for (int col = 0; col < A[row].length; col++)
					matD.put(row, col, A[t][k] * A[s][col] - A[s][k] * A[t][col], null, null);

				lang.nextStep();
				sc.unhighlight(13);
				sc.highlight(14);

				matA.unhighlightCellColumnRange(s, 0, matA.getNrCols() - 1, null, null);
				matA.unhighlightCellColumnRange(t, 0, matA.getNrCols() - 1, null, null);
				matD.unhighlightCellColumnRange(row, 0, matD.getNrCols() - 1, null, null);

				vecB.highlightCellColumnRange(t, 0, vecB.getNrCols() - 1, null, null);
				vecB.highlightCellColumnRange(s, 0, vecB.getNrCols() - 1, null, null);
				vecD.highlightCell(row, 0, null, null);

				vecD.put(row, 0, A[t][k] * b[s] - A[s][k] * b[t], null, null);

				lang.nextStep();

				sc.unhighlight(14);
				sc.highlight(15);

				vecB.unhighlightCellColumnRange(t, 0, vecB.getNrCols() - 1, null, null);
				vecB.unhighlightCellColumnRange(s, 0, vecB.getNrCols() - 1, null, null);
				vecD.unhighlightCell(row, 0, null, null);
				row++;

				matA.unhighlightCellColumnRange(t, 0, matA.getNrCols() - 1, null, null);
				vecB.unhighlightCellColumnRange(t, 0, vecB.getNrCols() - 1, null, null);
				Cp.unhighlightCell(Cpos.indexOf(t), null, null);
				Cp.unhighlightElem(Cpos.indexOf(t), null, null);
			}
			Cn.unhighlightCell(Cneg.indexOf(s), null, null);
			Cn.unhighlightElem(Cneg.indexOf(s), null, null);
		}

		// make title
		TextProperties tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 20));
		lang.newText(new Coordinates(TITLE_OFFSET, TITLE_OFFSET), "THE FOURIER-MOTZKIN TRANSFORMATION", "title", null,
				tp);

		// setup the start page with the description
		lang.nextStep();
		tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 16));
		lang.newText(new Coordinates(25, 50), "The Fourier-Motzkin transformation is an algorithim that takes a",
				"description1", null, tp);
		lang.newText(new Offset(0, 25, "description1", AnimalScript.DIRECTION_NW),
				"polhedron represented as a set of inequalities P(A,b) and the index", "description2", null, tp);
		lang.newText(new Offset(0, 25, "description2", AnimalScript.DIRECTION_NW),
				"of a dimension to be orthogonally projected to zero. The output is a", "description3", null, tp);
		lang.newText(new Offset(0, 25, "description3", AnimalScript.DIRECTION_NW),
				"system of inequalities P(D,d) that represents exactly result of this.", "description4", null, tp);
		lang.newText(new Offset(0, 25, "description4", AnimalScript.DIRECTION_NW), "projection.", "description5", null,
				tp);

		lang.nextStep();

	}

	@Override
	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		matrixA = (int[][]) primitives.get("matrixA");
		vectorB = (int[]) primitives.get("vectorB");

		double[][] A = new double[matrixA.length][matrixA[0].length];

		for (int i = 0; i < A.length; i++)
			for (int j = 0; j < A[i].length; j++)
				A[i][j] = (double) matrixA[i][j];

		double[] b = new double[vectorB.length];

		for (int i = 0; i < A.length; i++)
			b[i] = (double) vectorB[i];

		initialise();

		transform(A, b, k);

		return lang.toString();
	}

	@Override
	public String getAlgorithmName() {
		return "Fourier-Motzkin Transformation";
	}

	@Override
	public String getAnimationAuthor() {
		return "Melvin Laux";
	}

	@Override
	public String getCodeExample() {
		return "initialise row index sets C0, Cneg, Cpos" + "\n" + "loop:" + "\n" + "    add new row to matrix D" + "\n"
				+ "    add new entry to vector d";
	}

	@Override
	public Locale getContentLocale() {
		return Locale.ENGLISH;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "The Fourier-Motzkin transformation is an algorithim that takes a" + "\n"
				+ "polhedron represented as a set of inequalities P(A,b) and the index" + "\n"
				+ "of a dimension to be orthogonally projected to zero. The output is a" + "\n"
				+ "system of inequalities P(D,d) that represents exactly result of this.";
	}

	@Override
	public String getFileExtension() {
		return "asu";
	}

	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
	}

	@Override
	public String getName() {
		return "Fourier-Motzkin Transformation";
	}

	@Override
	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	@Override
	public void init() {
		lang = new AnimalScript("Ellipsoid method", "Melvin Laux", 800, 600);
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives)
			throws IllegalArgumentException {

		matrixA = (int[][]) primitives.get("matrixA");
		vectorB = (int[]) primitives.get("vectorB");
		k = (int) primitives.get("k");

		if (matrixA.length != vectorB.length)

			if (k > matrixA[0].length || k < 0)
				return false;

		return true;
	}

}
