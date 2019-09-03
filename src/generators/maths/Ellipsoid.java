/*
 * Ellipsoid.java
 * Melvin Laux, 2017 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.maths;

import java.awt.Color;
import java.awt.Font;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import javax.swing.text.html.HTML;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.NotEnoughNodesException;
import algoanim.primitives.Circle;
import algoanim.primitives.DoubleMatrix;
import algoanim.primitives.Ellipse;
import algoanim.primitives.Polygon;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.EllipseProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PolygonProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

public class Ellipsoid implements ValidatingGenerator {
	private Language lang;
	private int[][] matrixA;
	private int[] vectorB, upperBound, lowerBound;
	private SourceCode sc;
	private Text infoText1, infoText2, yTickNegLabel, xTickNegLabel, yTickPosLabel, xTickPosLabel;
	private RealVector ex, ey;
	private SourceCodeProperties sourceCodeProps;
	private PolygonProperties polyhedronProps;
	private Polygon polyhedron;
	private List<RealVector> polyVerts;
	private CircleProperties centerProps;
	private EllipseProperties ellipseProps;
	private MatrixProperties matrixProps;
	private Ellipse ell;
	private Circle center;

	private static final int SOURCE_CODE_OFFSET = 25;
	private static final int TITLE_OFFSET = 15;
	private static final int DRAWING_OFFSET_X = 325;
	private static final int DRAWING_OFFSET_Y = 600;

	private static final int DRAWING_SIZE = 250;

	private static final int PARAM_OFFSET_X = 25;
	private static final int PARAM_OFFSET_Y = 175;

	private static final int PARAM_SIZE_X = 600;
	private static final int PARAM_SIZE_Y = 75;

	private double SCALING = 25;

	public void init() {
		lang = new AnimalScript("Ellipsoid method", "Melvin Laux", 800, 600);
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		matrixA = (int[][]) primitives.get("matrixA");
		vectorB = (int[]) primitives.get("vectorB");
		lowerBound = (int[]) primitives.get("lowerBound");
		upperBound = (int[]) primitives.get("upperBound");

		// initialise properties
		sourceCodeProps = (SourceCodeProperties) props.getPropertiesByName("sourceCodeProps");
		polyhedronProps = (PolygonProperties) props.getPropertiesByName("polyhedronProps");
		matrixProps = (MatrixProperties) props.getPropertiesByName("matrixProps");
		centerProps = (CircleProperties) props.getPropertiesByName("centerProps");
		ellipseProps = (EllipseProperties) props.getPropertiesByName("ellipseProps");

		double[][] doubleA = new double[matrixA.length][matrixA[0].length];

		for (int i = 0; i < matrixA.length; i++) {
			for (int j = 0; j < matrixA[i].length; j++) {
				doubleA[i][j] = matrixA[i][j];
			}
		}

		double[] doubleB = new double[vectorB.length];

		for (int j = 0; j < vectorB.length; j++)
			doubleB[j] = vectorB[j];

		double[] u = new double[upperBound.length];
		double[] l = new double[lowerBound.length];

		for (int j = 0; j < upperBound.length; j++) {
			u[j] = upperBound[j];
			l[j] = lowerBound[j];
		}

		initialise();

		findPoint(new Array2DRowRealMatrix(doubleA), new ArrayRealVector(doubleB), new ArrayRealVector(u),
				new ArrayRealVector(l));

		return lang.toString();
	}

	public String getName() {
		return "Ellipsoid method";
	}

	public String getAlgorithmName() {
		return "Ellipsoid method";
	}

	public String getAnimationAuthor() {
		return "Melvin Laux";
	}

	public String getDescription() {
		return "The ellipsoid method is an algorithm that solves LPs (Linear Programs) in polynomial time. It " + "\n"
				+ "calculates a point that satisfies a given set of inequalities if such a point exists. Geometrically "
				+ "\n" + "this set of inequalities can be interpreted a polyhedron. Any point that lies within this "
				+ "\n" + "polyhedron satisfies all given inequalities." + "\n";
	}

	public String getCodeExample() {
		return "initialise" + "\n" + "loop:" + "\n" + "    check stopping criteria" + "\n"
				+ "    choose violated inequality" + "\n" + "    construct new ellipsoid";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.ENGLISH;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	private void initialise() {

		// create language object for AnimalScript
		lang.setStepMode(true);

		// make title
		TextProperties tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 20));
		lang.newText(new Coordinates(TITLE_OFFSET, TITLE_OFFSET), "THE ELLIPSOID METHOD", "title", null, tp);

		// setup the start page with the description
		lang.nextStep("Introduction");
		tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 16));
		lang.newText(new Coordinates(25, 50), "The ellipsoid method calculates a point that satisfies a given",
				"description1", null, tp);
		lang.newText(new Offset(0, 25, "description1", AnimalScript.DIRECTION_NW),
				"set of inequalities if such a point exists. Geometrically this ", "description2", null, tp);
		lang.newText(new Offset(0, 25, "description2", AnimalScript.DIRECTION_NW),
				"set of inequalitiescan be interpreted a polyhedron. Any point that ", "description3", null, tp);
		lang.newText(new Offset(0, 25, "description3", AnimalScript.DIRECTION_NW),
				"lies within this polyhedron satisfies all given inequalities.", "description4", null, tp);

		lang.nextStep("Introduction");

		lang.newText(new Offset(0, 50, "description4", AnimalScript.DIRECTION_NW),
				"In order to find such a point, the ellipsoid method creates a ", "algo11", null, tp);
		lang.newText(new Offset(0, 25, "algo11", AnimalScript.DIRECTION_NW),
				"series of ellipsoids decreasing in volume that contain the ", "algo12", null, tp);
		lang.newText(new Offset(0, 25, "algo12", AnimalScript.DIRECTION_NW),
				"entire polyhedron. To find a point in the polyhedron, it is checked ", "algo13", null, tp);
		lang.newText(new Offset(0, 25, "algo13", AnimalScript.DIRECTION_NW),
				"whether the center of the ellipsoid satisfied all conditions.", "algo14", null, tp);

		lang.nextStep("Introduction");

		lang.newText(new Offset(0, 50, "algo14", AnimalScript.DIRECTION_NW),
				"For the inital ellipsoid a scaled unit cricle is used. If the", "algo21", null, tp);
		lang.newText(new Offset(0, 25, "algo21", AnimalScript.DIRECTION_NW),
				"scaling factor is chosen large enough, the ellipsoid method is", "algo22", null, tp);
		lang.newText(new Offset(0, 25, "algo22", AnimalScript.DIRECTION_NW),
				"guaranteed to find a point in the polyhedron if one exists.", "algo23", null, tp);

		lang.nextStep("Introduction");

		lang.newText(new Offset(0, 50, "algo23", AnimalScript.DIRECTION_NW),
				"To construct the next ellipsoid, one violated inequality of the ", "algo31", null, tp);
		lang.newText(new Offset(0, 25, "algo31", AnimalScript.DIRECTION_NW),
				"system is selected and used to compute the new center a and affine", "algo32", null, tp);
		lang.newText(new Offset(0, 25, "algo32", AnimalScript.DIRECTION_NW),
				"projection matrix A of the next ellipsoid:", "algo33", null, tp);
		lang.newText(new Offset(10, 35, "algo33", AnimalScript.DIRECTION_NW), " d    = c*Aₖ / sqrt(c*Aₖ*c)", "algo34",
				null, tp);
		lang.newText(new Offset(0, 25, "algo34", AnimalScript.DIRECTION_NW), "aₖ₊₁ = aₖ - d / (n+1)", "algo35", null,
				tp);
		lang.newText(new Offset(0, 25, "algo35", AnimalScript.DIRECTION_NW),
				"Aₖ₊₁ = n/(n-1) * (Aₖ - 2* d\u22C5d/(n+1))", "algo36", null, tp);

		lang.nextStep("Introduction");

		lang.newText(new Offset(-10, 50, "algo36", AnimalScript.DIRECTION_NW),
				"DISCLAIMER: In order to accurately visualise the ellipsoids the scale ", "algo41", null, tp);
		lang.newText(new Offset(0, 25, "algo41", AnimalScript.DIRECTION_NW),
				"of the coordinate system will be required to be adapted on occasion.", "algo42", null, tp);
		lang.newText(new Offset(0, 25, "algo42", AnimalScript.DIRECTION_NW),
				"Please keep an eye on the values next to the ticks on the axes. Small", "algo43", null, tp);
		lang.newText(new Offset(0, 25, "algo43", AnimalScript.DIRECTION_NW),
				"numerical inaccuracies may occur. The center of the ellipsoid is enlarged", "algo44", null, tp);
		lang.newText(new Offset(0, 25, "algo44", AnimalScript.DIRECTION_NW), "for better visualisation.", "algo45",
				null, tp);

		lang.nextStep("Introduction");

		lang.hideAllPrimitives();

		// make title
		tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 20));
		// tp.set(AnimationPropertiesKeys.FONT_PROPERTY, Font.);
		lang.newText(new Coordinates(TITLE_OFFSET, TITLE_OFFSET), "THE ELLIPSOID METHOD", "title", null, tp);

		// Create SourceCode: coordinates, name, display options,
		// default properties

		// now, create the source code entity
		sc = lang.newSourceCode(new Coordinates(SOURCE_CODE_OFFSET, SOURCE_CODE_OFFSET + TITLE_OFFSET), "sourceCode",
				null, sourceCodeProps);

		// Add the lines to the SourceCode object.
		// Line, name, indentation, display delay
		sc.addCodeLine("initialise", null, 0, null); // 0
		sc.addCodeLine("do:", null, 0, null); // 1
		sc.addCodeLine("check stopping criteria", null, 1, null); // 2
		sc.addCodeLine("choose violated inequality", null, 1, null); // 3
		sc.addCodeLine("construct new ellipsoid", null, 1, null); // 4

		// initialise drawing background
		RectProperties rp = new RectProperties();

		rp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
		rp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.LIGHT_GRAY);
		rp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);

		lang.newRect(new Coordinates(DRAWING_OFFSET_X - DRAWING_SIZE, DRAWING_OFFSET_Y - DRAWING_SIZE),
				new Coordinates(DRAWING_OFFSET_X + DRAWING_SIZE, DRAWING_OFFSET_Y + DRAWING_SIZE), null, null, rp);

		// initialise explanation box
		rp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		rp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		lang.newRect(new Coordinates(PARAM_OFFSET_X, PARAM_OFFSET_Y),
				new Coordinates(PARAM_OFFSET_X + PARAM_SIZE_X, PARAM_OFFSET_Y + PARAM_SIZE_Y), null, null, rp);

		Coordinates[] xAxis = { new Coordinates(DRAWING_OFFSET_X - DRAWING_SIZE, DRAWING_OFFSET_Y),
				new Coordinates(DRAWING_OFFSET_X + DRAWING_SIZE, DRAWING_OFFSET_Y) };

		Coordinates[] yAxis = { new Coordinates(DRAWING_OFFSET_X, DRAWING_OFFSET_Y - DRAWING_SIZE),
				new Coordinates(DRAWING_OFFSET_X, DRAWING_OFFSET_Y + DRAWING_SIZE) };

		Coordinates[] xTickPos = { new Coordinates(DRAWING_OFFSET_X + 100, DRAWING_OFFSET_Y - 2),
				new Coordinates(DRAWING_OFFSET_X + 100, DRAWING_OFFSET_Y + 2) };

		Coordinates[] xTickNeg = { new Coordinates(DRAWING_OFFSET_X - 100, DRAWING_OFFSET_Y - 2),
				new Coordinates(DRAWING_OFFSET_X - 100, DRAWING_OFFSET_Y + 2) };

		Coordinates[] yTickPos = { new Coordinates(DRAWING_OFFSET_X + 2, DRAWING_OFFSET_Y - 100),
				new Coordinates(DRAWING_OFFSET_X - 2, DRAWING_OFFSET_Y - 100) };

		Coordinates[] yTickNeg = { new Coordinates(DRAWING_OFFSET_X + 2, DRAWING_OFFSET_Y + 100),
				new Coordinates(DRAWING_OFFSET_X - 2, DRAWING_OFFSET_Y + 100) };

		PolylineProperties pp = new PolylineProperties();
		pp.set(AnimationPropertiesKeys.BWARROW_PROPERTY, true);
		pp.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
		pp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.DARK_GRAY);

		lang.newPolyline(xAxis, null, null, pp);
		lang.newPolyline(yAxis, null, null, pp);

		pp.set(AnimationPropertiesKeys.BWARROW_PROPERTY, false);
		pp.set(AnimationPropertiesKeys.FWARROW_PROPERTY, false);

		lang.newPolyline(yTickNeg, "yTickNeg", null, pp);
		lang.newPolyline(yTickPos, "yTickPos", null, pp);
		lang.newPolyline(xTickNeg, "xTickNeg", null, pp);
		lang.newPolyline(xTickPos, "xTickPos", null, pp);

		tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 9));

		yTickNegLabel = lang.newText(new Offset(2, -1, "yTickNeg", AnimalScript.DIRECTION_E), "", "yTickNegLabel", null,
				tp);
		yTickPosLabel = lang.newText(new Offset(2, -1, "yTickPos", AnimalScript.DIRECTION_E), "", "yTickPosLabel", null,
				tp);
		xTickNegLabel = lang.newText(new Offset(-1, 2, "xTickNeg", AnimalScript.DIRECTION_S), "", "xTickNegLabel", null,
				tp);
		xTickPosLabel = lang.newText(new Offset(-1, 2, "xTickPos", AnimalScript.DIRECTION_S), "", "xTickPosLabel", null,
				tp);

	}

	private void findPoint(RealMatrix A, RealVector b, RealVector u, RealVector l) {

		TextProperties tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 15));

		infoText1 = lang.newText(new Coordinates(PARAM_OFFSET_X + 15, PARAM_OFFSET_Y + 15),
				"Let's take a look at the input data... ", "info1", null, tp);

		lang.nextStep("Initialisation");

		DoubleMatrix matA = lang.newDoubleMatrix(new Coordinates(400, 50), A.getData(), "matA", null, matrixProps);
		infoText2 = lang.newText(new Offset(0, 25, "info1", AnimalScript.DIRECTION_NW), "Matrix A", "info2", null, tp);

		lang.newText(new Offset(-100, 0, "matA", AnimalScript.DIRECTION_NE), "A = ", "A", null);

		lang.nextStep("Initialisation");

		infoText2.setText("Matrix A and Vector b", null, null);

		// setup input data
		double[][] data = new double[b.getDimension()][1];
		for (int i = 0; i < b.getDimension(); i++) {
			data[i][0] = b.getEntry(i);
		}

		DoubleMatrix vecB = lang.newDoubleMatrix(new Coordinates(525, 50), data, "vecB", null, matrixProps);
		lang.newText(new Offset(-70, 0, "vecB", AnimalScript.DIRECTION_NE), "b = ", "A", null);

		lang.nextStep();

		infoText1.setText("... and draw the polyhedron.", null, null);
		infoText2.setText("", null, null);

		// draw polyhedron

		double[] e1 = { 1.0, 0.0 };
		ex = new ArrayRealVector(e1);
		double[] e2 = { 0.0, 1.0 };
		ey = new ArrayRealVector(e2);

		polyVerts = findPolygonVerts(A, b);

		double absMax = Double.NEGATIVE_INFINITY;

		for (RealVector point : polyVerts)
			absMax = Math.max(absMax, point.getMaxValue());

		rescale(absMax);

		if (polyVerts.size() > 0) {

			drawPolyhedron();

		} else {
			infoText1.setText("As it turns out, the system of inequalities is infeasible. Hence,", null, null);
			infoText2.setText("no polyhedron can be drawn.", null, null);
		}

		lang.nextStep();

		sc.highlight(0);

		infoText1.setText("First off, we must initialise our ellipsoid to a scaled unit circle", null, null);
		infoText2.setText("at the origin point.", null, null);

		// initialization
		int n = A.getColumnDimension();

		// initialisation
		double m0 = Math.pow(Math.max(Math.abs(u.getEntry(0)), Math.abs(l.getEntry(0))), 2.0);
		double m1 = Math.pow(Math.max(Math.abs(u.getEntry(1)), Math.abs(l.getEntry(1))), 2.0);

		// initialise R
		double R = Math.sqrt(m0 + m1);

		// initialise A_k
		double[][] vals = new double[n][n];

		// fill diagonal matrix
		for (int i = 0; i < n; i++)
			vals[i][i] = R * R;

		// initialise matrix
		RealMatrix Ak = new Array2DRowRealMatrix(vals);

		// initialise a_k
		RealVector ak = new ArrayRealVector(new double[n]);

		// initialise k
		int k = 0;

		// initialise N
		int N = 2 * n * ((3 * n + 1) * codingLength(A) + (2 * n + 1) * codingLength(b) - (int) Math.pow(n, 3));

		// setup solution vector
		double[][] dataAK = new double[ak.getDimension()][1];
		for (int i = 0; i < ak.getDimension(); i++) {
			dataAK[i][0] = ak.getEntry(i);
		}

		// pre-calculate constant factors
		double ratio = 1.0 / ((double) n + 1.0);
		double ratioOuter = Math.pow(n, 2) / (Math.pow(n, 2) - 1.0);
		double ratioInner = 2.0 / (n + 1.0);

		int row = 0;

		lang.nextStep();

		lang.newText(new Coordinates(25, 300), "current center = ", "solText", null);
		DoubleMatrix sol = lang.newDoubleMatrix(new Offset(15, -20, "solText", AnimalScript.DIRECTION_NE), dataAK,
				"sol", null, matrixProps);

		// start loop
		while (true) {

			// display current solution
			for (int i = 0; i < ak.getDimension(); i++)
				sol.put(i, 0, ak.getEntry(i), null, null);

			lang.nextStep();

			infoText1.setText("Let's take a look.", null, null);
			infoText2.setText("", null, null);

			lang.nextStep();

			double rx = Ak.operate(ex).subtract(ak).getNorm();
			double ry = Ak.operate(ey).subtract(ak).getNorm();

			if (center != null) {
				center.hide();
				ell.hide();
			}

			absMax = Math.max(Math.abs(ak.getEntry(1)) + ry, Math.abs(ak.getEntry(0)) + rx);

			rescale(absMax);

			Coordinates centerCoords = new Coordinates(DRAWING_OFFSET_X + (int) (ak.getEntry(0) * SCALING),
					DRAWING_OFFSET_Y - (int) (ak.getEntry(1) * SCALING));

			center = lang.newCircle(centerCoords, 2, "center", null, centerProps);

			// draw ellipse
			ell = lang.newEllipse(centerCoords, new Coordinates((int) (rx * SCALING), (int) (ry * SCALING)), "ell",
					null, ellipseProps);

			// rotate ellipse
			RealVector x = new ArrayRealVector(2);
			x.setEntry(0, rx);
			ell.rotate(centerCoords, (int) Math.toDegrees(Math.acos(Ak.operate(ex).subtract(ak).cosine(ex))), null,
					null);

			sc.unhighlight(0);
			sc.unhighlight(4);
			sc.highlight(2);
			lang.nextStep();

			infoText1.setText("Now, we check if the max. number of iterations was reached:", null, null);
			infoText2.setText("Current iteration: " + k + ", Max. iteration: " + N, null, null);

			// stopping criterion
			if (k >= N) {

				infoText1.setText("Finally, we reached the maximum number of iterations.", null, null);
				infoText2.setText("The System is therefore infesible.", null, null);

				lang.nextStep();

				return;
			}

			lang.nextStep();

			infoText2.setText("If the center of the current ellipsoid lies in the polyhedron, we found ", null, null);
			infoText2.setText("a solution and con stop iterating.", null, null);

			// find violated inequalities
			List<Integer> rows = leq(A.operate(ak), b);

			if (rows.isEmpty()) {

				infoText1.setText("The center of the current ellipsoid lies within the polyhedron. Hence, ", null,
						null);
				infoText2.setText("this point is a solution for the given system of inequalities.", null, null);

				lang.nextStep();

				return;
			}

			lang.nextStep();

			infoText1.setText("No stopping criterion was met, so let's take all violated inequalites.", null, null);
			infoText2.setText("(Violated inequalities highlighted)", null, null);

			sc.unhighlight(2);
			sc.highlight(3);

			// highlight violated inequalities
			for (int i : rows) {
				matA.highlightCellColumnRange(i, 0, matA.getNrCols() - 1, null, null);
				vecB.highlightCellColumnRange(i, 0, vecB.getNrCols() - 1, null, null);
			}

			lang.nextStep();

			infoText1.setText("And then select one of them (randomly).", null, null);
			infoText2.setText("(Selected inequality highlighted)", null, null);

			int old = row;

			do {
				row = rows.get(ThreadLocalRandom.current().nextInt(0, rows.size()));
			} while (row == old && rows.size() > 1);

			RealVector c = A.getRowVector(row);

			// unhighlight violated inequalities
			for (int i : rows) {
				matA.unhighlightCellColumnRange(i, 0, matA.getNrCols() - 1, null, null);
				vecB.unhighlightCellColumnRange(i, 0, vecB.getNrCols() - 1, null, null);
			}

			// highlight selected row
			matA.highlightCellColumnRange(row, 0, matA.getNrCols() - 1, null, null);
			vecB.highlightCellColumnRange(row, 0, vecB.getNrCols() - 1, null, null);

			lang.nextStep();

			infoText1.setText("We use this to update our ellipsoid, which will have a smaller volume ", null, null);
			infoText2.setText("than the previous one, but still contain the entire polyhedron.", null, null);

			// calculate d
			RealVector d = Ak.operate(c).mapMultiply(1.0 / Math.sqrt(c.dotProduct(Ak.operate(c))));

			// update a_k
			RealVector sub = d.mapMultiply(ratio);
			ak = ak.subtract(sub);

			// update A_k
			RealMatrix subtr = d.outerProduct(d);
			Ak = (Ak.subtract(subtr.scalarMultiply(ratioInner))).scalarMultiply(ratioOuter);

			// update k
			k += 1;
			sc.unhighlight(3);
			sc.highlight(4);

			lang.nextStep();

			// unhighlight selected row
			matA.unhighlightCellColumnRange(row, 0, matA.getNrCols() - 1, null, null);
			vecB.unhighlightCellColumnRange(row, 0, vecB.getNrCols() - 1, null, null);
		}

	}

	private int codingLength(int x) {
		return (int) Math.ceil(Math.log(Math.abs(x) + 1.0) / Math.log(2)) + 1;
	}

	private int codingLength(double x) {
		Fraction f = new Fraction(x);
		return codingLength(f.getDenominator()) + codingLength(f.getNumerator());
	}

	private int codingLength(RealVector v) {
		int sum = 0;

		for (int i = 0; i < v.getDimension(); i++)
			sum += codingLength(v.getEntry(i));

		return sum;
	}

	private int codingLength(RealMatrix M) {
		int sum = 0;
		for (int i = 0; i < M.getRowDimension(); i++)
			for (int j = 0; j < M.getColumnDimension(); j++)
				sum += codingLength(M.getEntry(i, j));

		return sum;
	}

	/**
	 * Returns a list of all indices where a is greater than b.
	 * 
	 * @param a
	 *            vector a of the inequality
	 * @param b
	 *            vector b of the inequality
	 * @return List of indices that violate the inequality
	 */
	private List<Integer> leq(RealVector a, RealVector b) {
		LinkedList<Integer> list = new LinkedList<Integer>();
		for (int i = 0; i < a.getDimension(); i++)
			if (a.getEntry(i) > b.getEntry(i))
				list.add(i);

		return list;
	}

	private List<RealVector> findPolygonVerts(RealMatrix A, RealVector b) {
		List<RealVector> points = new LinkedList<RealVector>();

		int[] columns = IntStream.range(0, A.getColumnDimension()).toArray();

		for (int i = 0; i < A.getRowDimension() - 1; i++) {
			for (int j = i + 1; j < A.getRowDimension(); j++) {
				int[] rows = { i, j };
				RealMatrix subA = A.getSubMatrix(rows, columns);
				DecompositionSolver solver = new LUDecomposition(subA).getSolver();
				if (solver.isNonSingular()) {
					RealVector bSub = new ArrayRealVector(2);
					bSub.setEntry(0, b.getEntry(rows[0]));
					bSub.setEntry(1, b.getEntry(rows[1]));
					RealVector sol = solver.solve(bSub);
					if (leq(A.operate(sol), b).isEmpty())
						points.add(sol);
				}
			}
		}

		if (points.size() > 1) {
			RealVector m = points.get(0).add(points.get(1)).mapMultiply(0.5);

			for (int i = 0; i < points.size(); i++) {
				RealVector mv = points.get(i).subtract(m);
				RealVector ext = points.get(i).append(Math.acos(mv.cosine(ey)));
				points.set(i, ext);
			}

			Comparator<RealVector> comp = new Comparator<RealVector>() {

				@Override
				public int compare(RealVector o1, RealVector o2) {
					return (int) Math.signum(o1.getEntry(2) - o2.getEntry(2));
				}

			};

			Collections.sort(points, comp);

		}

		return points;
	}

	private Coordinates[] prepareVertsForDrawing(List<RealVector> points) {
		Coordinates[] verts = new Coordinates[points.size()];

		for (int i = 0; i < points.size(); i++)
			verts[i] = new Coordinates(DRAWING_OFFSET_X + (int) (points.get(i).getEntry(0) * SCALING),
					DRAWING_OFFSET_Y - (int) (points.get(i).getEntry(1) * SCALING));

		return verts;
	}

	public void rescale(double absMax) {
		if (absMax * SCALING >= 200 || absMax * SCALING <= 100) {

			SCALING = (int) (190.0 / absMax);

			xTickNegLabel.setText(String.format("%.2f", -100 / SCALING), null, null);
			xTickPosLabel.setText(String.format("%.2f", 100 / SCALING), null, null);
			yTickNegLabel.setText(String.format("%.2f", -100 / SCALING), null, null);
			yTickPosLabel.setText(String.format("%.2f", 100 / SCALING), null, null);

			drawPolyhedron();

			// lang.nextStep();
		}
	}

	private void drawPolyhedron() {
		try {
			if (polyhedron != null)
				polyhedron.hide();
			polyhedron = lang.newPolygon(prepareVertsForDrawing(polyVerts), "P", null, polyhedronProps);
		} catch (NotEnoughNodesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		matrixA = (int[][]) primitives.get("matrixA");
		vectorB = (int[]) primitives.get("vectorB");
		lowerBound = (int[]) primitives.get("lowerBound");
		upperBound = (int[]) primitives.get("upperBound");

		if ((upperBound.length != matrixA[0].length) || (lowerBound.length != matrixA[0].length))
			return false;

		if (matrixA[0].length != 2)
			return false;

		if (matrixA.length != vectorB.length)
			return false;

		for (int i = 0; i < matrixA.length; i++)
			for (int j = 0; j < matrixA[i].length; j++)
				if ((matrixA[i][j] > 10) || (matrixA[i][j] < -10))
					return false;

		for (int i = 0; i < vectorB.length; i++)
			if ((vectorB[i] > 10) || (vectorB[i] < -10))
				return false;

		return true;
	}

}
