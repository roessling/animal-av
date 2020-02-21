/*
 * Drachenkurve.java
 * Frank Nelles, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.maths;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;
import algoanim.primitives.Polyline;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PolylineProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import translator.Translator;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;

import animal.main.Animal;

public class Drachenkurve implements ValidatingGenerator {
	private Language lang;

	private Locale LANG = Locale.GERMANY;
	Translator t = new Translator("resources/Drachenkurve", LANG);

	private SourceCodeProperties sourceCode;
	private SourceCodeProperties variablesSourcecode;
	private PolylineProperties dragoncurve;
	private TextProperties titles;
	private TextProperties variablesValues;
	private TextProperties explanation;

	private int dragoncurveIteration;
	private int lineLength;

	
	/* For testing purposes only */
	public static void main(String[] args) {
		Generator generator = new Drachenkurve(Locale.GERMANY); // Generator erzeugen
		Animal.startGeneratorWindow(generator); // Animal mit Generator starten
	}
	
	public Drachenkurve(Locale l) {
		t.setTranslatorLocale(l);
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer arg0, Hashtable<String, Object> arg1)
			throws IllegalArgumentException {
		int dcIt = (int) arg1.get("dragoncurveIteration");
		return 0 <= dcIt && dcIt <= 12;
	}

	public void init() {
		lang = new AnimalScript("Drachenkurve", "Frank Nelles", 1000, 800);
		lang.setStepMode(true);
	}

	public String generate(AnimationPropertiesContainer props, Hashtable < String, Object > primitives) {
		sourceCode = (SourceCodeProperties) props.getPropertiesByName("sourceCode");
		variablesSourcecode = (SourceCodeProperties) props.getPropertiesByName("variablesSourcecode");
		dragoncurve = (PolylineProperties) props.getPropertiesByName("dragoncurve");
		titles = (TextProperties) props.getPropertiesByName("titles");
		variablesValues = (TextProperties) props.getPropertiesByName("variablesValues");
		dragoncurveIteration = (Integer) primitives.get("dragoncurveIteration");
		explanation = (TextProperties) props.getPropertiesByName("explanation");

		generateAnimalOutput();

		return lang.toString();
	}

	public String getName() {
		return t.translateMessage("title");
	}

	public String getAlgorithmName() {
		return t.translateMessage("title");
	}

	public String getAnimationAuthor() {
		return "Frank Nelles";
	}

	public String getDescription() {
		return t.translateMessage("desc");
	}

	public String getCodeExample() {
		return "public static String getSequence(int n) {\n" +
			"	if (n == 0) return \"\";\n" +
			"	if (n == 1) return \"R\";\n\n" +
			"	String prevSeq = getSequence(n-1);\n\n" +
			"	String result = prevSeq + \"R\";\n\n" +
			"	if (prevSeq.length() == 1) {\n" +
			"		result += \"L\";\n" +
			"	} else {\n" +
			"		int divider = prevSeq.length()/2;\n" +
			"		result += prevSeq.substring(0, divider)\n" +
			"		    + \"L\"\n" +
			"		    + prevSeq.substring(divider+1, prevSeq.length());\n" +
			"	}\n\n" +
			"	return result;\n" +
			"}";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return LANG;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
	}

	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

	private void generateAnimalOutput() {
			 if (dragoncurveIteration <= 5) lineLength = 15;
		else if (dragoncurveIteration <= 7) lineLength = 10;
		else 								lineLength =  4;

		titles.set(AnimationPropertiesKeys.FONT_PROPERTY, ((Font)titles.get(AnimationPropertiesKeys.FONT_PROPERTY)).deriveFont((float) 24.0));

		// Show title
		lang.newText(new Coordinates(50, 30), this.getName(), "head", null, titles);

		showExplanation();

		showMainDisplay();

		String finCurve = generateDragoncurve(dragoncurveIteration);

		showSummary(finCurve);
	}

	private void showExplanation() {
		explanation.set(AnimationPropertiesKeys.FONT_PROPERTY, ((Font)explanation.get(AnimationPropertiesKeys.FONT_PROPERTY)).deriveFont((float) 18.0));

		// Show an explanation at the beginning
		Text explanation_1 = lang.newText(new Coordinates(50, 80), t.translateMessage("expl_1"), "explanation_1", null, explanation);
		Text explanation_2 = lang.newText(new Coordinates(50, 100), t.translateMessage("expl_2"), "explanation_2", null, explanation);
		Text explanation_3 = lang.newText(new Coordinates(50, 120), t.translateMessage("expl_3"), "explanation_3", null, explanation);
		Text explanation_4 = lang.newText(new Coordinates(50, 140), t.translateMessage("expl_4"), "explanation_4", null, explanation);
		Text explanation_5 = lang.newText(new Coordinates(50, 160), t.translateMessage("expl_5"), "explanation_5", null, explanation);

		lang.nextStep(t.translateMessage("expl"));
		
		// Hide explanations
		explanation_1.hide();
		explanation_2.hide();
		explanation_3.hide();
		explanation_4.hide();
		explanation_5.hide();
	}

	private Text var_n, var_prevSeq, var_result, var_divider, var_returnVal, if_n0, if_n1, if_psL1, head_srcCode, head_vars;
	private SourceCode srcCode, vars, varsAss;
	private void showMainDisplay() {
		head_srcCode 	= lang.newText(new Coordinates(50, 80), t.translateMessage("codeTitle"), "srcCodeTitle", null, titles);
		head_vars 		= lang.newText(new Coordinates(500, 80), t.translateMessage("varTitle"), "varTitle", null, titles);
						  lang.newText(new Coordinates(50, 390), t.translateMessage("curveTitle"), "curveTitle", null, titles);

		/* Show src code */
		srcCode = lang.newSourceCode(new Coordinates(50, 100), "srcCode", null, sourceCode);
		srcCode.addCodeLine("public static String getSequence(int n) {", null, 0, null); // 1
		srcCode.addCodeLine("if (n == 0) return '';", null, 1, null); // 2
		srcCode.addCodeLine("if (n == 1) return 'R';", null, 1, null); // 3
		srcCode.addCodeLine("String prevSeq = getSequence(n-1);", null, 1, null); // 4
		srcCode.addCodeLine("String result = prevSeq + 'R';", null, 1, null); // 5
		srcCode.addCodeLine("", null, 1, null); // +++++
		srcCode.addCodeLine("if (prevSeq.length() == 1) {", null, 1, null); // 6
		srcCode.addCodeLine("result += 'L';", null, 2, null); // 7
		srcCode.addCodeLine("} else {", null, 1, null); // 8
		srcCode.addCodeLine("int divider = prevSeq.length()/2;", null, 2, null); // 9
		srcCode.addCodeLine("result += prevSeq.substring(0, divider)", null, 2, null); // 10
		srcCode.addCodeLine("+ 'L'", null, 3, null); // 11
		srcCode.addCodeLine("+ prevSeq.substring(divider+1, prevSeq.length());", null, 3, null); // 12
		srcCode.addCodeLine("}", null, 1, null); // 13
		srcCode.addCodeLine("return result;", null, 1, null); // 14
		srcCode.addCodeLine("}", null, 0, null); // 15

		vars = lang.newSourceCode(new Coordinates(500, 100), "vars", null, variablesSourcecode);
		
		vars.addCodeLine("n", null, 0, null);
		vars.addCodeLine("prevSeq", null, 0, null);
		vars.addCodeLine("result", null, 0, null);
		vars.addCodeLine("divider", null, 0, null);
		vars.addCodeLine("returns", null, 0, null);

		varsAss = lang.newSourceCode(new Coordinates(550, 100), "varsAss", null, variablesSourcecode);
		
		varsAss.addCodeLine(" = ", null, 0, null);
		varsAss.addCodeLine(" = ", null, 0, null);
		varsAss.addCodeLine(" = ", null, 0, null);
		varsAss.addCodeLine(" = ", null, 0, null);

		var_n 			= lang.newText(new Coordinates(570, 112), ((Integer) dragoncurveIteration).toString(), "var_n", null, variablesValues);
		var_prevSeq 	= lang.newText(new Coordinates(570, 128), "null", "var_prevSeq", null, variablesValues);
		var_result 		= lang.newText(new Coordinates(570, 144), "null", "var_result", null, variablesValues);
		var_divider 	= lang.newText(new Coordinates(570, 160), ((Integer) 0).toString(), "var_divider", null, variablesValues);
		var_returnVal	= lang.newText(new Coordinates(570, 176), "", "var_returnVal", null,
				variablesValues);

		if_n0			= lang.newText(new Coordinates(350, 128), "", "if_n0", null, variablesValues);
		if_n1			= lang.newText(new Coordinates(350, 144), "", "if_n1", null, variablesValues);
		if_psL1			= lang.newText(new Coordinates(350, 208), "", "if_psL1", null, variablesValues);

		drawCurve("");
		
		lang.nextStep();
	}

	//public String getSequence(int n) {
	private String generateDragoncurve(int n) {
		//Unhighlight all
		for (int i = 0; i <= 3; i++) srcCode.unhighlight(i);
		editVar(var_returnVal, "");
		editVar(if_n0, "");
		editVar(if_n1, "");
		editVar(if_psL1, "");

		//Set n and highlight 1
		editVar(var_n, n);
		srcCode.highlight(0);

		lang.nextStep(t.translateMessage("exec", ((Integer) n).toString()));

		//Show first if
		srcCode.unhighlight(0);
		srcCode.highlight(1);
		if (n == 0) {
			//Show internals
			editVar(if_n0, true);
			editVar(var_returnVal, "''");
			vars.highlight(4);

			lang.nextStep();
			return "";
		} else {
			editVar(if_n0, false);
		}
		
		//Show second if
		lang.nextStep();
		srcCode.unhighlight(1);
		srcCode.highlight(2);

		if (n == 1) {
			//Show internals
			editVar(if_n1, true);
			editVar(var_returnVal, "R");
			vars.highlight(4);

			lang.nextStep();
			drawCurve("R");
			return "R";
		} else {
			editVar(if_n1, false);
		}

		lang.nextStep();
		
		//Show prevSeq-line being exec
		srcCode.unhighlight(2);
		srcCode.highlight(3);

		lang.nextStep();
		//Exec prevSeq-line
		String prevSeq = generateDragoncurve(n - 1);
		
		//Leaving recursion -> show var content
		srcCode.highlight(3);

		editVar(var_n, n);
		vars.highlight(0);

		editVar(var_prevSeq, prevSeq);
		vars.highlight(1);

		//Reset temp values
		editVar(var_returnVal, "");
		editVar(var_result, "null");
		editVar(if_n0, "");
		editVar(if_n1, "");
		editVar(if_psL1, "");

		//Unhighlight all
		for (int i = 0; i <= 2; i++) srcCode.unhighlight(i);
		srcCode.unhighlight(14);
		vars.unhighlight(4);
		 
		lang.nextStep(t.translateMessage("end", ((Integer) n).toString()));
		srcCode.unhighlight(3);

		//Highlight next step (result)
		srcCode.highlight(4);
		vars.unhighlight(0);
		vars.unhighlight(1);

		lang.nextStep();

		//Calulcate result and show it
		String result = prevSeq + "R";

		editVar(var_result, result);
		vars.highlight(2);

		lang.nextStep();
		srcCode.unhighlight(4);
		vars.unhighlight(2);
		
		//Start if exec
		srcCode.highlight(6);

		if (prevSeq.length() == 1) {
			editVar(if_psL1, true);

			lang.nextStep();
			srcCode.unhighlight(6);

			srcCode.highlight(7);
			result += "L";

			lang.nextStep();

			vars.highlight(2);
			editVar(var_result, result);
		} else {
			editVar(if_psL1, false);

			lang.nextStep();

			//Calculating divider
			srcCode.unhighlight(6);
			srcCode.highlight(9);

			int divider = prevSeq.length() / 2;

			lang.nextStep();

			vars.highlight(3);
			editVar(var_divider, divider);

			lang.nextStep();
			//Finished divider, showing result
			srcCode.unhighlight(9);
			vars.unhighlight(3);
			
			srcCode.highlight(10);
			srcCode.highlight(11);
			srcCode.highlight(12);

			result += prevSeq.substring(0, divider) +
				"L" +
				prevSeq.substring(divider + 1, prevSeq.length());

			lang.nextStep();

			vars.highlight(2);
			editVar(var_result, result);
		}

		//Unhighlight all
		lang.nextStep();
		for (int i = 6; i <= 13; i++) srcCode.unhighlight(i);
		vars.unhighlight(2);

		srcCode.highlight(14);

		lang.nextStep();
		//Show return value

		vars.highlight(4);
		editVar(var_returnVal, result);
		
		lang.nextStep();

		//Draw dragon curve
		drawCurve(result);
		return result;		
	}
	//}

	private void showSummary(String resultString) {
		lang.nextStep();
		head_srcCode.setText(t.translateMessage("summary"), null, null);

		//Hide Code and vars
		head_vars.hide();
		vars.hide();
		varsAss.hide();
		srcCode.hide();
		if_n0.hide();
		if_n1.hide();
		if_psL1.hide();
		var_divider.hide();
		var_n.hide();
		var_prevSeq.hide();
		var_result.hide();
		var_returnVal.hide();

		lang.newText(new Coordinates(50, 110), t.translateMessage("summ_1"), "summary_1", null, explanation);
		lang.newText(new Coordinates(50, 130), t.translateMessage("summ_2", resultString), "summary_2", null, explanation);
		lang.newText(new Coordinates(50, 150), t.translateMessage("summ_3"), "summary_3", null, explanation);
		lang.newText(new Coordinates(50, 170), t.translateMessage("summ_4", ((Integer) dragoncurveIteration).toString()), "summary_4", null, explanation);

		lang.nextStep(t.translateMessage("summ"));
	}

	Polyline lastCurve;
	private void drawCurve(String dirs) {
		//Hide last curve
		if (lastCurve != null) lastCurve.hide();

		int size = lineLength; //Line length

		//Curve
		char lastDir = 'N';
		ArrayList<Coordinates> curve = new ArrayList<Coordinates>();

		//Coordinates
		curve.add(new Coordinates(450, 500));
		curve.add(new Coordinates(450, 500 - size));

		int lastX = 450;
		int lastY = 500 - size;

		//String to char array
		char[] dir = dirs.toCharArray();

		//Calculate coordinates for different directions (N(orth) pointing upwards on display, W,S,E according)
		for (int d = 0; d < dir.length; d++)
			switch (lastDir) {
				case 'N': 
					if (dir[d] == 'R') 	{ lastX += size; curve.add(new Coordinates(lastX, lastY)); lastDir = 'E'; }
					else /* L */		{ lastX -= size; curve.add(new Coordinates(lastX, lastY)); lastDir = 'W'; }
					break;

				case 'S': 
					if (dir[d] == 'R') 	{ lastX -= size; curve.add(new Coordinates(lastX, lastY)); lastDir = 'W'; }
					else /* L */		{ lastX += size; curve.add(new Coordinates(lastX, lastY)); lastDir = 'E'; }
					break;


				case 'W': 
					if (dir[d] == 'R') 	{ lastY -= size; curve.add(new Coordinates(lastX, lastY)); lastDir = 'N'; }
					else /* L */		{ lastY += size; curve.add(new Coordinates(lastX, lastY)); lastDir = 'S'; }
					break;
					
				default: //E
					if (dir[d] == 'R') 	{ lastY += size; curve.add(new Coordinates(lastX, lastY)); lastDir = 'S'; }
					else /* L */		{ lastY -= size; curve.add(new Coordinates(lastX, lastY)); lastDir = 'N'; }
					break;
			}

		//Coordinate ArrayList to Node array
		Node[] curveA = new Node[curve.size()];
		for (int c = 0; c < curve.size(); c++) curveA[c] = curve.get(c);

		//Paint dragon curve
		lastCurve = lang.newPolyline(curveA, "dragonCurve" + dir.length, null, dragoncurve);
	}
	
	/*
		Aux. methods to change variable values
	*/
	private void editVar(Text var, String val) {
		var.setText(((val.equals("")||val.equals("null")) ? val : "'" + val + "'"), null, null);
	}

	private void editVar(Text var, boolean val) {
		var.setText(((Boolean) val).toString(), null, null);
	}
	
	private void editVar(Text var, int val) {
		var.setText(((Integer) val).toString(), null, null);
	}
}