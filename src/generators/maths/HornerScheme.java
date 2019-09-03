/*
 * HornerScheme.java
 * Lennard Rüdesheim, Tim Steinke, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.maths;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;
import java.text.NumberFormat;
import java.text.DecimalFormat;

import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.QuestionGroupModel;
import algoanim.animalscript.AnimalRectGenerator;
import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.AnimalTextGenerator;

public class HornerScheme implements ValidatingGenerator {
	private Language lang;
	private double x0;
//	private String[] coefficients;

    // used to shorten the outputs in the StringMatrix (numF.format(Double))
    NumberFormat numF = new DecimalFormat("##0.##");

    public void init() {
		lang = new AnimalScript("Horner-Schema", "Lennard Rüdesheim, Tim Steinke", 800, 600);
		lang.setStepMode(true);
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		x0 = (double) primitives.get("x0");
		String[][] coefficientsString = (String[][]) primitives.get("Coefficients");
		double[] coeffs = new double[coefficientsString[0].length];
		for (int i = 0; i < coefficientsString[0].length; ++i) {
            // transform to double and flip the order so that the polynome starts
            // with the highest exponent
		    coeffs[coefficientsString[0].length - i - 1] = Double.parseDouble(coefficientsString[0][i]);
        }

		horner(coeffs, x0);
		lang.finalizeGeneration();
		return lang.toString();
	}

	/**
	 * Creates an animation for the horner scheme of the following polynomial:
	 * coeffs[0] * x0^N + coeffs[1] * x0^(N-1) ... + coeffs[N]
	 * 
	 * @param coeffs
	 *            the coefficients of the polynomial, sorted from highest exponent
	 *            to lowest
	 * @param x0
	 *            the value for which to evaluate the polynomial
	 */
	public void horner(double[] coeffs, double x0) {
		// header
		AnimalTextGenerator tg = new AnimalTextGenerator(lang);
		TextProperties headerProps = new TextProperties();
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
				new Font(Font.SANS_SERIF, Font.BOLD, 24));
		Text header = new Text(tg, new Coordinates(40, 30), "Horner-Scheme", "header",
				new TicksTiming(0), headerProps);
		tg.create(header);
		// header kasten
		Rect headerRect = drawYellowRectAround(header, "headerRect", 10);
		// start
		SourceCodeProperties scp = new SourceCodeProperties();
		SourceCode startText = lang.newSourceCode(
				new Offset(0, 20, header, AnimalScript.DIRECTION_SW), "sourceCode", null, scp);
		startText.addCodeLine(
				"The Horner scheme is a computional method for calculating the value of a polynomial ",
				null, 0, null);
		startText.addCodeLine("", null, 0, null);
		startText.addCodeLine(" f(x)=a_n*x^n + a_(a-1)*x^(n-1) + ... + a_1*x + a_0", null, 0, null);
		startText.addCodeLine("", null, 0, null);
		startText.addCodeLine(" at a specific value x_0.", null, 0, null);
		startText.addCodeLine("", null, 0, null);
		startText.addCodeLine(" The procedure consists of the following steps:", null, 0, null);
		startText.addCodeLine("", null, 0, null);
		startText.addCodeLine("1. Copy the first coefficient to the first slot of the results row",
				null, 0, null);
		startText.addCodeLine("2. Repeat for i=2,...,N:", null, 1, null);
		startText.addCodeLine("2.1  middle_row[i] = results[i - 1] * x0", null, 2, null);
		startText.addCodeLine("2.2 results[i] = coefficients[i] + middle_row[i]", null, 2, null);
		drawRectAround(startText, "startTextRect", 10);

		lang.nextStep();
		ArrayList<Primitive> headerAndRect = new ArrayList<>();
		headerAndRect.add(header);
		headerAndRect.add(headerRect);
		lang.hideAllPrimitivesExcept(headerAndRect);
		lang.nextStep();
		// schritte
		scp.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		SourceCode sourceCode = lang.newSourceCode(
				new Offset(0, 20, header, AnimalScript.DIRECTION_SW), "sourceCode", null, scp);
		sourceCode.addCodeLine("1. Copy the first coefficient to the first slot of the results row",
				null, 0, null);
		sourceCode.addCodeLine("2. Repeat for i=2,...,N:", null, 1, null);
		sourceCode.addCodeLine("2.1  middle_row[i] = results[i - 1] * x0", null, 2, null);
		sourceCode.addCodeLine("2.2 results[i] = coefficients[i] + middle_row[i]", null, 2, null);
		drawRectAround(sourceCode, "sourceRect", 4);
		sourceCode.highlight(0);
		lang.nextStep("Step 1");
		// Polynom
		MatrixProperties mp = new MatrixProperties();
		mp.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.RED);
		// not using standard green to improve visibility on white
		mp.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, new Color(0x30C840));
		StringMatrix polynom = lang.newStringMatrix(
				new Offset(-130, 30, sourceCode, AnimalScript.DIRECTION_S),
				new String[1][2 * coeffs.length], "polynom", null, mp);
		
		// used in conclusion
		String polynomeString = "f(x)= ";
		polynom.put(0, 0, "f(x)= ", null, null);
		for (int i = 0; i < coeffs.length - 1; ++i) {
			polynomeString += numF.format(coeffs[i]) + "*x^" + (coeffs.length - 1 - i) + " +";
			polynom.put(0, 2 * i + 1, "" + numF.format(coeffs[i]), null, null);
			polynom.put(0, 2 * i + 2, "*x^" + (coeffs.length - 1 - i) + " +", null, null);
		}
		polynomeString += numF.format(coeffs[coeffs.length - 1]);
		polynom.put(0, 2 * coeffs.length - 1, "" + numF.format(coeffs[coeffs.length - 1]), null, null);

		lang.nextStep();
		// Stelle
		StringMatrix stelle = lang.newStringMatrix(
				new Offset(-50, 40, polynom, AnimalScript.DIRECTION_W), new String[1][1], "stelle",
				null, mp);
		stelle.put(0, 0, "x_0 = " + numF.format(x0), null, null);
		// ersteZeile
		StringMatrix schema = lang.newStringMatrix(
				new Offset(50, -50, stelle, AnimalScript.DIRECTION_S), new String[3][coeffs.length],
				"ersteZeile", null, mp);
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < coeffs.length; ++j) {
				schema.put(i, j, "              ", null, null);
			}
		}
		drawRectAround(schema, "schemaRect");
		int i = 0;
		for (int j = 0; j < coeffs.length; j++) {
			for (int k = 0; k < 3; k++)
				schema.put(k, j, "      ", null, null);
		}
		for (int j = 0; j < coeffs.length; j++) {
			polynom.highlightCell(0, ++i, null, null);
			schema.highlightElem(0, j, null, null);
			lang.nextStep();
			schema.put(0, j, "" + numF.format(coeffs[j]) + "      ", null, null);
			lang.nextStep();
			schema.unhighlightElem(0, j, null, null);
			polynom.unhighlightCell(0, i, null, null);
			i++;
			lang.nextStep();
		}
		// step2
		sourceCode.unhighlight(0);
		sourceCode.highlight(1);
		sourceCode.highlight(2);
		lang.nextStep("Step 2");
		// StringMatrix step2 = lang.newStringMatrix(new Offset(-110, -5, ersteZeile,
		// AnimalScript.DIRECTION_S),
		// new String[2][5], "step2", null, mp);
		// for (i = 0; i < 2; i++) {
		// for (int j = 0; j < 5; j++) {
		// step2.put(i, j, " ", null, null);
		// }
		// }
		schema.put(2, 0, "=" + numF.format(coeffs[0]), null, null);
		schema.highlightCell(0, 0, null, null);
		schema.highlightElem(2, 0, null, null);
		lang.nextStep("Step2: Row 1");
		schema.put(2, 0, "" + numF.format(coeffs[0]) + "     ", null, null);
		schema.unhighlightElem(2, 0, null, null);
		schema.unhighlightCell(0, 0, null, null);
		lang.nextStep();
		double[][] help = new double[2][coeffs.length];
		help[1][0] = coeffs[0];
		@SuppressWarnings("unused")
    QuestionGroupModel nextStepGroup = new QuestionGroupModel("nextStepGroup", 1);
		for (i = 1; i < coeffs.length; i++) {
			// 2.1
			//Question
            MultipleChoiceQuestionModel nextStep = new MultipleChoiceQuestionModel("nextStep" + i);
            nextStep.setPrompt("Which values are needed for the next calculation?");
            nextStep.setGroupID("nextStepGroup");
            double a = help[1][i - 1] - 1;
            double c = help[1][i - 1] + 2;
            nextStep.addAnswer(numF.format(x0) + " and " + numF.format(a), 0, "Wrong, x0 and the last value in the result row are needed.");
            if (i <= 1) {
                nextStep.addAnswer(numF.format(x0) + " and " + numF.format(coeffs[0]), 3, "Right");
            } else {
                nextStep.addAnswer(numF.format(x0) + " and " + numF.format(help[1][i - 1]), 3, "Right");
            }
            nextStep.addAnswer(1 + " and " + numF.format(c), 0, "Wrong, x0 and the last value in the result row are needed.");
            nextStep.setNumberOfTries(1);
            lang.addMCQuestion(nextStep);
            lang.nextStep(); //answer should not be marked before the question
			
			// 2.1
			help[0][i] = x0 * help[1][i - 1];
			stelle.highlightCell(0, 0, null, null);
			schema.highlightCell(2, i - 1, null, null);
			schema.highlightElem(1, i, null, null);
			schema.put(1, i, "= " + numF.format(x0) + "*" + numF.format(help[1][i - 1]), null, null);
			lang.nextStep("Step2: Column " + (i + 1));
			schema.put(1, i, "" + numF.format(x0 * help[1][i - 1]) + "     ", null, null);
			lang.nextStep();
			stelle.unhighlightCell(0, 0, null, null);
			schema.unhighlightElem(1, i, null, null);
			schema.unhighlightCell(2, i - 1, null, null);
			// 2.2
			lang.nextStep();
			sourceCode.unhighlight(2);
			sourceCode.highlight(3);
			schema.highlightElem(2, i, null, null);
			schema.put(2, i, "= " + numF.format(coeffs[i]) + "+" + numF.format(help[0][i]), null, null);
			schema.highlightCell(0, i, null, null);
			schema.highlightCell(1, i, null, null);
			lang.nextStep();
			help[1][i] = coeffs[i] + help[0][i];
			schema.put(2, i, numF.format(coeffs[i] + help[0][i]) + "     ", null, null);
			lang.nextStep();
			schema.unhighlightElem(2, i, null, null);
			schema.unhighlightCell(0, i, null, null);
			schema.unhighlightCell(1, i, null, null);
			lang.nextStep();
			sourceCode.highlight(2);
			sourceCode.unhighlight(3);
		}
		sourceCode.unhighlight(2);
		sourceCode.unhighlight(1);

        //Question
        MultipleChoiceQuestionModel result = new MultipleChoiceQuestionModel("result");
        result.setPrompt("What is the result?");
        result.setGroupID("nextStep");
        result.addAnswer("f(" + numF.format(x0) + ")= " + numF.format(help[1][coeffs.length - 1]), 2, "Right");
        result.addAnswer("x0= " + numF.format(help[1][coeffs.length - 1]), 0, "Wrong, we calculated the value of the funtion at x0");
        result.addAnswer("f(" + numF.format(help[1][coeffs.length - 1]) + ") = " + numF.format(x0), 0, "Wrong, we calculated the value of the funtion at x0");
        lang.addMCQuestion(result);
        lang.nextStep();
		
		schema.highlightCell(2, coeffs.length - 1, null, null);
		//resultPolynString
		String resultPolynom ="";
		for (int k = 0; k < coeffs.length - 1; ++k) {
			resultPolynom += numF.format(coeffs[k]);
			resultPolynom += "*"+numF.format(x0)+"^" + (coeffs.length - 1 - k) + " +";
		}
		resultPolynom += "" + numF.format(coeffs[coeffs.length - 1]);
		StringMatrix ende = lang.newStringMatrix(
				new Offset(100, 0, schema, AnimalScript.DIRECTION_S), new String[1][1], "ende",
				null, mp);
		ende.put(0, 0, "thus applies f(x_0)=f("+numF.format(x0)+")= "+ resultPolynom+" = " + numF.format(help[1][coeffs.length - 1]), null, null);
		ende.highlightCell(0, 0, null, null);
		lang.nextStep("Information");
		schema.unhighlightCell(1, coeffs.length - 1, null, null);
		ende.put(0, 0, "", null, null);
		ende.unhighlightCell(0, 0, null, null);
		lang.hideAllPrimitivesExcept(headerAndRect);

		SourceCode conclusion = lang.newSourceCode(
				new Offset(0, 20, header, AnimalScript.DIRECTION_SW), "sourceCode", null, scp);
		conclusion.addCodeLine("Conclusion:", null, 0, null);
		conclusion.addCodeLine("", null, 0, null);
		conclusion.addCodeLine(
				"Using the Horner scheme, we calculated the value of the polynomial ",
				null, 0, null);
		conclusion.addCodeLine("", null, 0, null);
		conclusion.addCodeLine(polynomeString, null, 0, null);
		conclusion.addCodeLine("", null, 0, null);
		conclusion.addCodeLine(" at the specific value " + numF.format(x0) + ".", null, 0, null);
		conclusion.addCodeLine("", null, 0, null);
		conclusion.addCodeLine(" The resulting value was f(" + numF.format(x0) + ") = " + numF.format(help[1][coeffs.length - 1]), null, 0, null);
		conclusion.addCodeLine("", null, 0, null);
		conclusion.addCodeLine(" The procedure consists of the following steps:", null, 0, null);
		conclusion.addCodeLine("", null, 0, null);
		conclusion.addCodeLine("1. Copy the first coefficient to the first slot of the results row",
				null, 0, null);
		conclusion.addCodeLine("2. Repeat for i=2,...,N:", null, 1, null);
		conclusion.addCodeLine("2.1  middle_row[i] = results[i - 1] * x0", null, 2, null);
		conclusion.addCodeLine("2.2 results[i] = coefficients[i] + middle_row[i]", null, 2, null);
		drawRectAround(conclusion, "startTextRec", 10);
		
		
		SourceCode endText = lang.newSourceCode(
				new Offset(0, 20, conclusion, AnimalScript.DIRECTION_SW), "sourceCode", null, scp);
		endText.addCodeLine("Complexity and further applications:", null, 0, null);
		endText.addCodeLine("", null, 0, null);
		endText.addCodeLine("- The complexity of evaluating f(x_0) with the Horner scheme is in O(n). ", null, 0, null);
		endText.addCodeLine("", null, 0, null);
		endText.addCodeLine("- It can also be used for:", null, 0, null);
		endText.addCodeLine("", null, 0, null);
				
				endText.addCodeLine("  - Dividing f(x) by the linear factor x-x_0. -> f(x) / (x-x_0) ", null, 0, null);
		endText.addCodeLine(
				"  - Calculating the values of the derivations at x_0. -> f'(x_0), f''(x_0), ... , f^(n)(x_0)",
				null, 0, null);
		endText.addCodeLine("  - Calculating the Taylor-expansion of f at x_0 ", null, 0, null);
		drawRectAround(endText, "endTextRect", 10);

	}

	/**
	 * Creates an animation for the horner scheme of the following polynomial:
	 * coeffs[0] * x0^N + coeffs[1] * x0^(N-1) ... + coeffs[N]
	 * 
	 * @param coeffs
	 *            the coefficients of the polynomial, sorted from highest exponent
	 *            to lowest
	 * @param x0
	 *            the value for which to evaluate the polynomial
	 */
	public void horner(int[] coeffs, double x0) {
		// get double array from integer coefficients
		double[] coeffsDouble = new double[coeffs.length];
		for (int i = 0; i < coeffs.length; ++i)
			coeffsDouble[i] = (double) coeffs[i];
		horner(coeffsDouble, x0);
	}

	/**
	 * Draws a rectangle around basePrimitive
	 * 
	 * @param basePrimitive
	 *            the element around which a rectangle should be drawn
	 * @return the created {@link Rect} object
	 */
	private algoanim.primitives.Rect drawRectAround(algoanim.primitives.Primitive basePrimitive, String name) {
		AnimalRectGenerator rg = new AnimalRectGenerator(lang);
		RectProperties rectProps = new RectProperties();
		Rect rect = new Rect(rg, new Offset(0, 0, basePrimitive, AnimalScript.DIRECTION_NW),
				new Offset(0, 0, basePrimitive, AnimalScript.DIRECTION_SE), name, null,
				rectProps);
		rg.create(rect);
		return rect;
	}

	/**
	 * Draws a rectangle around basePrimitive
	 * 
	 * @param basePrimitive
	 *            the element around which a rectangle should be drawn
	 * @param margin
	 *            how much space should be between the object and the rectangle
	 * @return the created {@link Rect} object
	 */
	private algoanim.primitives.Rect drawRectAround(algoanim.primitives.Primitive basePrimitive, String name,
			int margin) {
		AnimalRectGenerator rg = new AnimalRectGenerator(lang);
		RectProperties rectProps = new RectProperties();
		Rect rect = new Rect(rg,
				new Offset(-margin, -margin, basePrimitive, AnimalScript.DIRECTION_NW),
				new Offset(margin, margin, basePrimitive, AnimalScript.DIRECTION_SE), name,
				null, rectProps);
		rg.create(rect);
		return rect;
	}
	
	/**
	 * Draws a rectangle around basePrimitive with color
	 * 
	 * @param basePrimitive
	 *            the element around which a rectangle should be drawn
	 * @param margin
	 *            how much space should be between the object and the rectangle
	 * @return the created {@link Rect} object
	 */
	private algoanim.primitives.Rect drawYellowRectAround(algoanim.primitives.Primitive basePrimitive, String name,
			int margin) {
		AnimalRectGenerator rg = new AnimalRectGenerator(lang);
		RectProperties rectProps = new RectProperties();
		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, -1);
		Rect rect = new Rect(rg,
				new Offset(-margin, -margin, basePrimitive, AnimalScript.DIRECTION_NW),
				new Offset(margin, margin, basePrimitive, AnimalScript.DIRECTION_SE), name,
				null, rectProps);
		rg.create(rect);
		return rect;
	}

	public String getName() {
		return "Horner-Schema";
	}

	public String getAlgorithmName() {
		return "Horner-Schema";
	}

	public String getAnimationAuthor() {
		return "Lennard Rüdesheim, Tim Steinke";
	}

	public String getDescription() {
		return "The Horner scheme can be used to efficiently calculate the value of a polynomial at x0. It can also be used for polynomial division.";
	}

	public String getCodeExample() {
		return "1. Copy the first coefficient to the first slot of the results row" + "\n"
				+ "2. Repeat for i=2,...,N:" + "\n" + "2.1  middle_row[i] = results[i - 1] * x0"
				+ "\n" + "2.2 results[i] = coefficients[i] + middle_row[i]";
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

	@Override
	public boolean validateInput(AnimationPropertiesContainer properties, Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		x0 = (double) primitives.get("x0");
		String[][] coeffs = (String[][]) primitives.get("Coefficients");
		//minimum Koeffizienten
		if(coeffs[0].length < 1) throw new IllegalArgumentException("Mindestens eine Koeffizient benötigt.");
		//1-dimensional
		if(coeffs.length != 1) throw new IllegalArgumentException("Koeffizienten müssen 1-dimensional sein.");
		
		return true;
	}
}
