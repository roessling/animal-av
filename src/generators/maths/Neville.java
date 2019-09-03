/*
 * AnimalNeville.java
 * Imed Ben Ghozi, 2016 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.maths;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Random;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.DoubleMatrix;
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
import algoanim.util.Timing;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;
import translator.Translator;

public class Neville implements ValidatingGenerator {

	private static final String TITLE = "Neville";
	private static final String AUTHOR = "Imed Ben Ghozi <benghozi.imed@gmail.com>";
	private static final String SOURCE_CODE_0 = "public static double neville (double[] xi,double[] yi,int x){";
	private static final String SOURCE_CODE_1 = "  int n=xi.length-1;";
	private static final String SOURCE_CODE_2 = "  double[][] f=new double [n+1][n+1];";

	private static final String SOURCE_CODE_3 = "  for (int i=0; i<f.length;i++){";
	private static final String SOURCE_CODE_4 = "    f[i][0]=yi[i];";
	private static final String SOURCE_CODE_5 = "  }";
	private static final String SOURCE_CODE_6 = "  for (int j = 1; j <= n; j++) {";

	private static final String SOURCE_CODE_7 = "    for (int i = 0; i < ((n+1) - j); i++) {";
	private static final String SOURCE_CODE_8 = "        f[i][j] = ((x - xi[i + j]) * f[i][j - 1] - (x-xi[i]) * f[i+1][j-1]) / (xi[i]-xi[i+j]);";
	private static final String SOURCE_CODE_9 = "    }";
	private static final String SOURCE_CODE_10 = "  }";
	private static final String SOURCE_CODE_11 = "  return f[0][n];";
	private static final String SOURCE_CODE_12 = "}";

	private String description1;
	private String description2;
	private String description3;
	private String description4;
	private String calculations;
	private String question1;
	private String conclusion1;
	private String conclusion2;
	private String conclusion3;
	private String conclusion4;
	private String conclusion5;
	private String conclusion6;
	private String feedback11;
	private String feedback12;
	private String header;
	private String question12;

	private double x;

	private Language lang;

	private String[] y_i;
	private String[] x_i;

	private SourceCodeProperties sourceCode;
	private TextProperties text;
	private MatrixProperties results;

	private Locale l;

	private Translator trans;

	Variables v;

	public Neville(Locale l) {
		this.l = l;
		trans = new Translator("generators/maths/NevilleLang/neville", l);

		trans.getCurrentLocale();
		header = trans.translateMessage("header");
		description1 = trans.translateMessage("description1");
		description2 = trans.translateMessage("description2");
		description3 = trans.translateMessage("description3");
		description4 = trans.translateMessage("description4");
		conclusion1 = trans.translateMessage("conclusion1");
		conclusion2 = trans.translateMessage("conclusion2");
		conclusion3 = trans.translateMessage("conclusion3");
		conclusion4 = trans.translateMessage("conclusion4");
		conclusion5 = trans.translateMessage("conclusion5");
		conclusion6 = trans.translateMessage("conclusion6");
		calculations = trans.translateMessage("calculations");
		question1 = trans.translateMessage("question1");
		question12 = trans.translateMessage("question12");
		feedback11 = trans.translateMessage("feedback11");
		feedback12 = trans.translateMessage("feedback12");

	}

	@Override
	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		sourceCode = (SourceCodeProperties) props.getPropertiesByName("sourceCode");
		y_i = (String[]) primitives.get("y_i");
		x_i = (String[]) primitives.get("x_i");
		x = (double) primitives.get("x");
		text = (TextProperties) props.getPropertiesByName("text");
		results = (MatrixProperties) props.getPropertiesByName("results");
		double[] xi = new double[x_i.length];
		double[] yi = new double[y_i.length];
		for (int i = 0; i < x_i.length; i++) {
			xi[i] = Double.parseDouble(x_i[i]);
		}
		for (int i = 0; i < y_i.length; i++) {
			yi[i] = Double.parseDouble(y_i[i]);
		}

		v = lang.newVariables();
		neville(xi, yi, x);

		return lang.toString();
	}

	@Override
	public String getAlgorithmName() {
		return header;
	}

	@Override
	public String getAnimationAuthor() {
		return AUTHOR;
	}

	@Override
	public String getCodeExample() {
		return SOURCE_CODE_0 + "\n" + SOURCE_CODE_1 + "\n" + SOURCE_CODE_2 + "\n" + SOURCE_CODE_3 + "\n" + SOURCE_CODE_4
				+ "\n" + SOURCE_CODE_5 + "\n" + SOURCE_CODE_6 + "\n" + SOURCE_CODE_7 + "\n" + SOURCE_CODE_8 + "\n"
				+ SOURCE_CODE_9 + "\n" + SOURCE_CODE_10 + "\n" + SOURCE_CODE_11 + "\n" + SOURCE_CODE_12;
	}

	@Override
	public Locale getContentLocale() {
		return l;
	}

	@Override
	public String getDescription() {
		return description1 + "\n" + description2 + "\n" + description3 + "\n" + description4;
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
		return TITLE;
	}

	@Override
	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

	@Override
	public void init() {
		lang = new AnimalScript(TITLE, AUTHOR, 1280, 800);
		lang.setStepMode(true);
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
	}

	public void neville(double[] xi, double yi[], double x) {
		TextProperties hprops = new TextProperties();

		hprops.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		hprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 24));
		algoanim.primitives.Text h = lang.newText(new Coordinates(10, 10), header, "header", null, hprops);
		h.show();
		lang.nextStep();
		Text d1 = lang.newText(new Offset(0, 100, "header", AnimalScript.DIRECTION_SW), description1, "description1",
				null, text);
		Text d2 = lang.newText(new Offset(0, 25, "description1", AnimalScript.DIRECTION_SW), description2,
				"description2", null, text);
		Text d3 = lang.newText(new Offset(0, 25, "description2", AnimalScript.DIRECTION_SW), description3,
				"description3", null, text);
		Text d4 = lang.newText(new Offset(0, 25, "description3", AnimalScript.DIRECTION_SW), description4,
				"description4", null, text);
		lang.nextStep("Description");
		lang.hideAllPrimitives();
		h.show();
		int n = xi.length - 1;
		v.declare("double", "x", String.valueOf(x),"FIXED_VALUE");

		double[][] f = new double[n + 1][n + 1];
		String[][] pointSet = new String[n + 2][2];
		pointSet[0][0] = "xi";
		pointSet[0][1] = "yi";
		for (int i = 1; i < n + 2; i++) {
			pointSet[i][0] = String.valueOf(xi[i - 1]);
			pointSet[i][1] = String.valueOf(yi[i - 1]);
		}

		StringMatrix dataSet = lang.newStringMatrix(new Offset(0, 40, "header", AnimalScript.DIRECTION_SW), pointSet,
				"dataSet", null, results);
		SourceCode sC = lang.newSourceCode(new Offset(0, 40, "dataSet", AnimalScript.DIRECTION_SW), "sourceCode", null,
				sourceCode);
		sC.addCodeLine(SOURCE_CODE_0, null, 0, null);
		sC.addCodeLine(SOURCE_CODE_1, null, 2, null);
		sC.addCodeLine(SOURCE_CODE_2, null, 2, null);
		sC.addCodeLine(SOURCE_CODE_3, null, 2, null);
		sC.addCodeLine(SOURCE_CODE_4, null, 4, null);
		sC.addCodeLine(SOURCE_CODE_5, null, 2, null);
		sC.addCodeLine(SOURCE_CODE_6, null, 2, null);
		sC.addCodeLine(SOURCE_CODE_7, null, 4, null);
		sC.addCodeLine(SOURCE_CODE_8, null, 6, null);
		sC.addCodeLine(SOURCE_CODE_9, null, 4, null);
		sC.addCodeLine(SOURCE_CODE_10, null, 2, null);
		sC.addCodeLine(SOURCE_CODE_11, null, 0, null);
		sC.addCodeLine(SOURCE_CODE_12, null, 0, null);
		sC.show();

		int numberOfCalcs = 0;
		v.declare("int", "numberOfCalcs", String.valueOf(numberOfCalcs), "FOLLOWER");
		Text numberOfops = lang.newText(new Offset(0, 20, "sourceCode", AnimalScript.DIRECTION_SW),
				calculations + "=" + numberOfCalcs, "numberofcalcs", null, text);
		lang.nextStep();
		sC.highlight(0);
		lang.nextStep("Calling Neville");
		sC.toggleHighlight(0, 1);
		v.declare("int", "n", String.valueOf(n), "ORGANIZER");
		String currentCalc = "n=" + n;
		Text calcs = lang.newText(new Offset(30, 0, "sourceCode", AnimalScript.DIRECTION_NE), currentCalc,
				"calculations", null, text);
		SourceCode calcD = lang.newSourceCode(new Offset(0, 25, "calculations", AnimalScript.DIRECTION_SW), "steps",
				null, sourceCode);
		lang.nextStep();
		sC.toggleHighlight(1, 2);

		DoubleMatrix animalF = lang.newDoubleMatrix(new Offset(30, 0, "dataSet", AnimalScript.DIRECTION_NE), f, "f",
				null, results);

		lang.nextStep("Intialize Result Matrix");
		v.declare("int", "i", "0", "Stepper");
		for (int i = 0; i < f.length; i++) {
			sC.toggleHighlight(2, 3);
			sC.toggleHighlight(4, 3);
			currentCalc = "i=" + i;
			v.set("i", String.valueOf(i));
			calcs.setText(currentCalc, null, null);
			lang.nextStep();
			sC.toggleHighlight(3, 4);
			currentCalc = "n=" + n + " " + "i=" + i + " " + "f[" + i + "][0]=" + yi[i];
			calcs.setText(currentCalc, null, null);
			f[i][0] = yi[i];
			dataSet.highlightCell(i + 1, 1, null, Timing.MEDIUM);
			animalF.highlightCell(i, 0, null, Timing.MEDIUM);
			animalF.put(i, 0, yi[i], null, null);
			dataSet.unhighlightCell(i + 1, 1, Timing.MEDIUM, null);
			animalF.unhighlightCell(i, 0, Timing.MEDIUM, null);
			lang.nextStep("Iteration" + i + "Over y[i]");

		}
		lang.nextStep();

		v.declare("int", "j", "1", "Stepper");
		Random rand = new Random();
		boolean questionAsked=false;
		for (int j = 1; j <= n; j++) {
			lang.nextStep();
			v.set("j", String.valueOf(j));
			sC.toggleHighlight(4, 6);
			currentCalc = "n=" + n + " " + "j=" + j;
			calcs.setText(currentCalc, null, null);
			lang.nextStep("Iteration " + j + "of Outer Loop");
			for (int i = 0; i < ((n + 1) - j); i++) {
				int iq=rand.nextInt(n+1-j);
				System.out.println(iq);
				v.set("i", String.valueOf(i));
				sC.toggleHighlight(6, 7);
				currentCalc = "n=" + n + " j=" + j + " i=" + i;
				calcs.setText(currentCalc, null, null);
				lang.nextStep();
				sC.toggleHighlight(7, 8);

				f[i][j] = ((x - xi[i + j]) * f[i][j - 1] - (x - xi[i]) * f[i + 1][j - 1]) / (xi[i] - xi[i + j]);
				FillInBlanksQuestionModel fib = new FillInBlanksQuestionModel("fib");
				if (i == iq &&!questionAsked) {
					lang.nextStep();
					fib.setPrompt(String.format("f[%d][%d]=?", i, j));
					fib.addAnswer(String.format("(((%s-xi[%d])*f[%d][%d]-(%s-xi[%d])*f[%d][%d]/xi[%d]-xi[%d]))", x,
							(i + j), i, j - 1, x, i, i + 1, j - 1, i, i + j), 1, feedback11);
					lang.addFIBQuestion(fib);
					questionAsked=true;
					lang.nextStep("Fill In the Blanks");
				}
				numberOfCalcs++;
				numberOfops.setText(calculations + "=" + numberOfCalcs, null, null);

				animalF.highlightCell(i, j, null, Timing.MEDIUM);
				animalF.put(i, j, f[i][j], null, Timing.MEDIUM);
				animalF.unhighlightCell(i, j, Timing.MEDIUM, null);
				currentCalc = String.format(
						"n=%d j=%d i=%d f[%d][%d]=(((%s-xi[%d])*f[%d][%d]-(%s-xi[%d])*f[%d][%d]/xi[%d]-xi[%d]))", n, j,
						i, i, j, x, (i + j), i, j - 1, x, i, i + 1, j - 1, i, i + j);
				calcs.setText(currentCalc, null, null);
				calcD.addCodeLine(String.format("f[%d][%d]=(" + x + "-" + xi[i + j] + ")*" + f[i][j - 1] + "-(" + x
						+ "-" + xi[i] + ")*" + f[i + 1][j - 1] + ")/(" + xi[i] + "-" + xi[i + j] + ")", i, j), null, 0,
						Timing.MEDIUM);
				calcD.addCodeLine("=(" + (x - xi[i + j]) + "*" + f[i][j - 1] + "-" + (x - xi[i]) + "*" + f[i + 1][j - 1]
						+ ")/" + (xi[i] - xi[i + j] + "=" + f[i][j]), null, 1, Timing.MEDIUM);
				lang.nextStep("Iteration " + i + "of Inner Loop");
				sC.toggleHighlight(8, 6);

			}

		}
		sC.toggleHighlight(6, 11);
		// ich wei� nicht warum das nicht korrekt funktioniert
		double res=f[0][n];
		v.declare("double", "FofX", String.valueOf(res),"MOST_WANTED_HOLDER");
		calcs.setText("x=" + x + " f(x)=" + f[0][n], null, null);
		animalF.highlightCell(0, n, null, null);
		lang.nextStep();
		lang.hideAllPrimitives();
		sC.hide();
		animalF.hide();
		dataSet.hide();
		h.show();
		Text c1 = lang.newText(new Offset(0, 100, "header", AnimalScript.DIRECTION_SW), conclusion1, "conclusion1",
				null, text);
		Text c2 = lang.newText(new Offset(0, 25, "conclusion1", AnimalScript.DIRECTION_SW), conclusion2, "conclusion2",
				null, text);
		Text c3 = lang.newText(new Offset(0, 25, "conclusion2", AnimalScript.DIRECTION_SW), conclusion3, "conclusion3",
				null, text);
		Text c4 = lang.newText(new Offset(0, 25, "conclusion3", AnimalScript.DIRECTION_SW), conclusion4, "conclusion4",
				null, text);
		Text c5 = lang.newText(new Offset(0, 25, "conclusion4", AnimalScript.DIRECTION_SW), conclusion5, "conclusion5",
				null, text);
		Text c6 = lang.newText(new Offset(0, 25, "conclusion5", AnimalScript.DIRECTION_SW), conclusion6, "conclusion6",
				null, text);
		lang.nextStep("Conclusion");
		lang.hideAllPrimitivesExcept(h);
		MultipleChoiceQuestionModel q1 = new MultipleChoiceQuestionModel("MC");
		
		int rnd = 3 + rand.nextInt((20 - 3) + 1);
		q1.setPrompt(question1 + " " + rnd + " " + question12);
		q1.addAnswer("" + rnd, 0, feedback12);
		q1.addAnswer("" + (rnd + 1), 1, feedback11);
		q1.addAnswer("" + (rnd - 1), 0, feedback12);
		q1.setNumberOfTries(1);
		lang.addMCQuestion(q1);
		lang.nextStep("Questions");
		lang.finalizeGeneration();

	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer animationPropertiesContainer,
			Hashtable<String, Object> hashtable) throws IllegalArgumentException {
		y_i = (String[]) hashtable.get("y_i");
		x_i = (String[]) hashtable.get("x_i");
		x = (double) hashtable.get("x");
		for (String v : x_i) {
			if (Double.parseDouble(v) == x)
				throw new IllegalArgumentException("x should not be part of the dataset");
		}
		if (y_i.length != x_i.length) {
			throw new IllegalArgumentException("x_i and y_i must have the same length");
		}

		return (y_i.length == x_i.length);

	}
}