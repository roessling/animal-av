/*
 * GaussJordan.java
 * Simon Breitfelder, 2016 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.maths;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import javax.swing.SwingUtilities;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import animal.variables.Variable;
import animal.variables.VariableRoles;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.maths.gaussjordan.SquareMatrix;
import translator.Translator;

public class GaussJordan implements ValidatingGenerator {
	private Language lang;
	private Locale locale;
	private Translator translator;

	// current primitives and properties
	private SquareMatrix inputA;
	private TextProperties headerProperties;
	private SourceCodeProperties descriptionProperties;
	private SourceCodeProperties sourceCodeProperties;
	private TextProperties statusTextProperties;
	private MatrixProperties matrixProperties;
	private TextProperties matrixLabelProperties;

	// animation elements
	private Text title;
	@SuppressWarnings("unused")
	private Rect titleRect;
	private SourceCode description;
	private SourceCode code1, code2;
	private StringMatrix matrixA, matrixE;
	@SuppressWarnings("unused")
	private Text labelMatrixA, labelMatrixE;
	private Text status;
	private SourceCode conclusion;
	private Variables variables;

	public GaussJordan() {
		this.locale = Locale.GERMANY;
		this.translator = new Translator("resources/GaussJordan", this.locale);
	}

	public GaussJordan(Locale locale) {
		this.locale = locale;
		this.translator = new Translator("resources/GaussJordan", this.locale);
	}

	public GaussJordan(String resPath, Locale locale) {
		this.locale = locale;
		this.translator = new Translator(resPath, this.locale);
	}

	public void setLang(Language lang) {
		this.lang = lang;
		this.lang.setStepMode(true);
	}

	public static void main(String[] args) {
		GaussJordan gen = new GaussJordan();
		SquareMatrix mat = new SquareMatrix(new int[][] { { 1, 2, -2 }, { 2, -1, 0 }, { 0, -1, 1 } });
		String script = gen.generate(mat);
		System.out.println(script);
	}

	private int getMaxSplitPos(String str, FontMetrics fontMetrics, int maxWidth) {
		int pos = str.indexOf(" ") + 1;
		while (str.indexOf(" ", pos) >= 0) {
			int newPos = str.indexOf(" ", pos) + 1;
			int lineWidth = SwingUtilities.computeStringWidth(fontMetrics, str.substring(0, newPos));
			if (lineWidth >= maxWidth)
				return pos;
			pos = newPos;
		}
		return pos;
	}

	private String cutOverlappingLines(String str, Font font, int maxWidth) {
		Canvas canvas = new Canvas();
		FontMetrics fontMetrics = canvas.getFontMetrics(font);

		// cut long lines into pieces
		StringBuilder sb = new StringBuilder();
		List<String> parts = new ArrayList<String>(Arrays.asList(str.split("\n")));
		for (int i = 0; i < parts.size(); i++) {
			int lineWidth = SwingUtilities.computeStringWidth(fontMetrics, parts.get(i));
			if (lineWidth > maxWidth) {
				int splitPos = getMaxSplitPos(parts.get(i), fontMetrics, maxWidth);
				String str1 = parts.get(i).substring(0, splitPos).trim();
				String str2 = parts.get(i).substring(splitPos).trim();
				parts.set(i, str1);
				if (str2.length() > 0)
					parts.add(i + 1, str2);

				if (str1.length() == 0)
					sb.append(" \n");
				else
					sb.append(str1).append("\n");
			} else {
				if (parts.get(i).length() == 0)
					sb.append(" \n");
				else
					sb.append(parts.get(i)).append("\n");
			}
		}
		return sb.toString();
	}

	private String doubleToString(double val) {
		val = Math.round(100.0 * val) / 100.0;
		if (Math.abs(val - Math.round(val)) < 0.001)
			return Integer.toString((int) val);
		else
			return Double.toString(val);
	}

	private void generateDescriptionPage() {
		this.title = this.lang.newText(new Coordinates(10, 35), getName(), "title", null, this.headerProperties);
		this.lang.newRect(new Offset(-5, -5, this.title, "NW"), new Offset(5, 5, this.title, "SE"), "titlerect", null);
		this.description = this.lang.newSourceCode(new Coordinates(30, 70), "description", null, this.descriptionProperties);
		this.description.addMultilineCode(cutOverlappingLines(getDescription(), (Font) this.descriptionProperties.get(AnimationPropertiesKeys.FONT_PROPERTY), 680), null, null);
		this.lang.nextStep();
		this.description.hide();
	}

	private void setupCode1() {
		this.code1 = this.lang.newSourceCode(new Coordinates(30, 100), "code1", null, this.sourceCodeProperties);
		this.code1.addMultilineCode(this.translator.translateMessage("sourceCode1"), null, null);
	}

	private void setupCode2() {
		this.code1.hide();
		this.code2 = this.lang.newSourceCode(new Coordinates(30, 100), "code2", null, this.sourceCodeProperties);
		this.code2.addMultilineCode(this.translator.translateMessage("sourceCode2"), null, null);
	}

	private SquareMatrix gaussJordan(SquareMatrix a) {
		this.variables = this.lang.newVariables();
		//TODO variable role
		this.variables.declare("int", "n22", "0", Variable.getRoleString(VariableRoles.FIXED_VALUE));
		this.variables.declare("int", "n");
		this.variables.set("n", Integer.toString(a.getSize()));

		setupCode1();
		this.status = this.lang.newText(new Coordinates(50, 250), "", "status", null, this.statusTextProperties);
		this.status.hide();

		this.matrixA = this.lang.newStringMatrix(new Coordinates(80, 280), a.getStringArray(), "matrixA", null, this.matrixProperties);
		this.labelMatrixA = this.lang.newText(new Offset(10, 10, this.matrixA, "SW"), this.translator.translateMessage("matrixA"), "labelMatrixA", null, this.matrixLabelProperties);
		this.code1.highlight(0);
		this.lang.nextStep();
		this.code1.unhighlight(0);

		SquareMatrix e = new SquareMatrix(a.getSize());
		this.matrixE = this.lang.newStringMatrix(new Coordinates(380, 280), e.getStringArray(), "matrixE", null, this.matrixProperties);
		this.labelMatrixE = this.lang.newText(new Offset(10, 10, this.matrixE, "SW"), this.translator.translateMessage("matrixE"), "labelMatrixE", null, this.matrixLabelProperties);
		this.code1.highlight(1);
		this.lang.nextStep();
		this.code1.unhighlight(1);

		this.code1.highlight(2);
		//TODO variable role
		//this.variables.declare("int", "k", animal.variables.VariableRoles.STEPPER.toString());
		this.variables.declare("int", "k");
		for (int i = 0; i < (a.getSize() - 1); i++) {
			this.variables.set("k", Integer.toString(i));
			processRowDown(i, a, e);
		}
		this.code1.unhighlight(2);

		setupCode2();
		this.code2.highlight(1);
		for (int i = (a.getSize() - 1); i >= 0; i--) {
			this.variables.set("k", Integer.toString(i));
			processRowUp(i, a, e);
		}
		this.code2.unhighlight(1);
		this.variables.discard("k");

		this.code2.highlight(6);
		this.lang.nextStep();

		this.code2.hide();
		this.status.setText("", null, null); // hiding the text field doesn't work sometimes...
		this.status.hide();

		this.variables.discard("n");
		return e;
	}

	private void processRowDown(int row, SquareMatrix a, SquareMatrix e) {
		// select row with largest element in that that diagonal column
		this.code1.highlight(3);
		int iMax = a.getPivotRow(row, row);
		//TODO variable role
		//this.variables.declare("int", "i", animal.variables.VariableRoles.MOST_WANTED_HOLDER.toString());
		this.variables.declare("int", "i");
		this.variables.set("i", Integer.toString(iMax));
		this.status.show();
		this.status.setText(this.translator.translateMessage("pivotMaximum", Double.toString(Math.round(100.0 * a.get(iMax, row)) / 100.0), Integer.toString(iMax), Integer.toString(row)), null, null);
		this.matrixA.highlightCellRowRange(row, a.getSize() - 1, row, null, null);
		this.lang.nextStep();
		this.code1.unhighlight(3);
		// error in the api: closing " is missing when unhighlighting an incomplete row-range
		//this.matrixA.unhighlightCellRowRange(row, a.getSize() - 1, row, null, null);
		for (int i = row; i < a.getSize(); i++)
			this.matrixA.unhighlightCell(i, row, null, null);

		if (iMax != row) {
			// swap rows
			this.code1.highlight(4);
			this.status.setText(this.translator.translateMessage("swapRows", Integer.toString(iMax), Integer.toString(row)), null, null);
			this.matrixA.highlightCellColumnRange(row, 0, a.getSize() - 1, null, null);
			this.matrixA.highlightCellColumnRange(iMax, 0, a.getSize() - 1, null, null);
			this.matrixE.highlightCellColumnRange(row, 0, e.getSize() - 1, null, null);
			this.matrixE.highlightCellColumnRange(iMax, 0, e.getSize() - 1, null, null);
			this.lang.nextStep();
			this.matrixA.unhighlightCellColumnRange(row, 0, a.getSize() - 1, null, null);
			this.matrixA.unhighlightCellColumnRange(iMax, 0, a.getSize() - 1, null, null);
			this.matrixE.unhighlightCellColumnRange(row, 0, e.getSize() - 1, null, null);
			this.matrixE.unhighlightCellColumnRange(iMax, 0, e.getSize() - 1, null, null);
			this.status.hide();

			a.swapRows(row, iMax);
			e.swapRows(row, iMax);
			for (int j = 0; j < a.getSize(); j++) {
				this.matrixA.put(row, j, a.getString(row, j), null, null);
				this.matrixA.put(iMax, j, a.getString(iMax, j), null, null);
				this.matrixE.put(row, j, e.getString(row, j), null, null);
				this.matrixE.put(iMax, j, e.getString(iMax, j), null, null);
			}
			this.lang.nextStep();
			this.code1.unhighlight(4);
		}

		//TODO variable role
		//this.variables.setRole("i", animal.variables.VariableRoles.STEPPER.toString());
		//this.variables.declare("double", "Alpha", animal.variables.VariableRoles.FIXED_VALUE.toString());
		this.variables.declare("double", "Alpha");
		this.code1.highlight(5);
		this.code1.highlight(6);
		this.code1.highlight(7);
		this.status.show();
		// subtract current row from following rows
		for (int i = (row + 1); i < a.getSize(); i++) {
			this.variables.set("i", Integer.toString(i));
			float mul = a.get(i, row) / a.get(row, row);
			this.variables.set("Alpha", doubleToString(mul));
			this.status.setText(this.translator.translateMessage("eliminate", Integer.toString(row), Integer.toString(i), Double.toString(Math.round(100.0 * mul) / 100.0)), null, null);
			this.matrixA.highlightCellColumnRange(i, 0, a.getSize() - 1, null, null);
			this.matrixE.highlightCellColumnRange(i, 0, e.getSize() - 1, null, null);
			this.lang.nextStep();
			this.matrixA.unhighlightCellColumnRange(i, 0, a.getSize() - 1, null, null);
			this.matrixE.unhighlightCellColumnRange(i, 0, e.getSize() - 1, null, null);
			for (int j = 0; j < a.getSize(); j++) {
				a.set(i, j, a.get(i, j) - mul * a.get(row, j));
				this.matrixA.put(i, j, a.getString(i, j), null, null);
				e.set(i, j, e.get(i, j) - mul * e.get(row, j));
				this.matrixE.put(i, j, e.getString(i, j), null, null);
			}
			this.lang.nextStep();
		}

		this.variables.discard("i");
		this.variables.discard("Alpha");
		this.status.hide();
		this.code1.unhighlight(5);
		this.code1.unhighlight(6);
		this.code1.unhighlight(7);
	}

	private void processRowUp(int row, SquareMatrix a, SquareMatrix e) {
		// transform diagonal element to 1
		this.code2.highlight(2);
		this.status.show();
		float div = a.get(row, row);
		this.status.setText(this.translator.translateMessage("normalize", Integer.toString(row), doubleToString(div)), null, null);
		this.matrixA.highlightCellColumnRange(row, 0, a.getSize() - 1, null, null);
		this.matrixE.highlightCellColumnRange(row, 0, e.getSize() - 1, null, null);
		this.lang.nextStep();
		this.matrixA.unhighlightCellColumnRange(row, 0, a.getSize() - 1, null, null);
		this.matrixE.unhighlightCellColumnRange(row, 0, e.getSize() - 1, null, null);
		this.status.hide();

		for (int j = 0; j < a.getSize(); j++) {
			a.set(row, j, a.get(row, j) / div);
			this.matrixA.put(row, j, a.getString(row, j), null, null);
			e.set(row, j, e.get(row, j) / div);
			this.matrixE.put(row, j, e.getString(row, j), null, null);
		}
		this.lang.nextStep();
		this.code2.unhighlight(2);

		//TODO variable role
		//this.variables.declare("int", "i", animal.variables.VariableRoles.STEPPER.toString());
		this.variables.declare("int", "i");
		//this.variables.declare("double", "Alpha", animal.variables.VariableRoles.FIXED_VALUE.toString());
		this.variables.declare("double", "Alpha");
		this.code2.highlight(3);
		this.code2.highlight(4);
		this.code2.highlight(5);
		this.status.show();
		// subtract current row from previous rows
		for (int i = (row - 1); i >= 0; i--) {
			this.variables.set("i", Integer.toString(i));
			float mul = a.get(i, row) / a.get(row, row);
			this.variables.set("Alpha", doubleToString(mul));
			this.status.setText(this.translator.translateMessage("eliminate", Integer.toString(row), Integer.toString(i), Double.toString(Math.round(100.0 * mul) / 100.0)), null, null);
			this.matrixA.highlightCellColumnRange(i, 0, a.getSize() - 1, null, null);
			this.matrixE.highlightCellColumnRange(i, 0, e.getSize() - 1, null, null);
			this.lang.nextStep();
			this.matrixA.unhighlightCellColumnRange(i, 0, a.getSize() - 1, null, null);
			this.matrixE.unhighlightCellColumnRange(i, 0, e.getSize() - 1, null, null);
			for (int j = 0; j < a.getSize(); j++) {
				a.set(i, j, a.get(i, j) - mul * a.get(row, j));
				this.matrixA.put(i, j, a.getString(i, j), null, null);
				e.set(i, j, e.get(i, j) - mul * e.get(row, j));
				this.matrixE.put(i, j, e.getString(i, j), null, null);
			}
			this.lang.nextStep();
		}

		this.variables.discard("i");
		this.variables.discard("Alpha");
		this.code2.unhighlight(3);
		this.code2.unhighlight(4);
		this.code2.unhighlight(5);
	}

	private String generate() {
		generateDescriptionPage();

		gaussJordan(new SquareMatrix(this.inputA));

		this.conclusion = this.lang.newSourceCode(new Coordinates(30, 100), "conclusion", null, this.descriptionProperties);
		this.conclusion.addMultilineCode(this.cutOverlappingLines(this.translator.translateMessage("conclusion"), (Font) this.descriptionProperties.get(AnimationPropertiesKeys.FONT_PROPERTY), 680), null, null);

		return lang.toString();
	}

	public void init() {
		setLang(new AnimalScript(getName(), getAnimationAuthor(), 800, 600));
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		this.inputA = new SquareMatrix((int[][]) primitives.get("A"));

		setupDefaultProperties();
		//this.headerProperties = (TextProperties) props.getPropertiesByName("headerProperties");
		//this.descriptionProperties = (SourceCodeProperties) props.getPropertiesByName("descriptionProperties");
		this.sourceCodeProperties = (SourceCodeProperties) props.getPropertiesByName("sourceCodeProperties");
		//this.statusTextProperties = (TextProperties) props.getPropertiesByName("statusTextProperties");
		this.matrixProperties = (MatrixProperties) props.getPropertiesByName("matrixProperties");
		//this.matrixLabelProperties = (TextProperties) props.getPropertiesByName("matrixLabelProperties");

		init();

		return generate();
	}

	private void setupDefaultProperties() {
		this.headerProperties = new TextProperties();
		this.headerProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 20));
		this.headerProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		this.descriptionProperties = new SourceCodeProperties();
		this.descriptionProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif", Font.PLAIN, 16));
		this.descriptionProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		this.sourceCodeProperties = new SourceCodeProperties();
		this.sourceCodeProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));
		this.sourceCodeProperties.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		this.sourceCodeProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		this.statusTextProperties = new TextProperties();
		this.statusTextProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 16));
		this.statusTextProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		this.matrixProperties = new MatrixProperties();
		this.matrixProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		this.matrixProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		this.matrixProperties.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.RED);

		this.matrixLabelProperties = new TextProperties();
		this.matrixLabelProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 16));
		this.matrixLabelProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
	}

	public String generate(SquareMatrix a) {
		this.inputA = a;
		setupDefaultProperties();

		init();

		return generate();
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) throws IllegalArgumentException {
		// check matrix A is square
		int[][] a = (int[][]) primitives.get("A");
		int size = a.length;
		if (size == 0)
			throw new IllegalArgumentException(this.translator.translateMessage("errorMatrixAEmpty"));
		for (int i = 0; i < size; i++) {
			if (a[i].length != size)
				throw new IllegalArgumentException(this.translator.translateMessage("errorMatrixANonSquare"));
		}
		return true;
	}

	public String getName() {
		return this.translator.translateMessage("generatorName");
	}

	public String getAlgorithmName() {
		return this.translator.translateMessage("algoName");
	}

	public String getAnimationAuthor() {
		return "Simon Breitfelder";
	}

	public String getDescription() {
		return this.translator.translateMessage("description");
	}

	public String getCodeExample() {
		return this.translator.translateMessage("sourceCode");
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return this.locale;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
	}

	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}
}