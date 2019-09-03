/*
 * HornformelMarkierung.java
 * Tristan Gahler, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.maths;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;

import java.awt.Color;
import java.awt.Font;

public class HornformelMarkierung implements ValidatingGenerator {
	private Language lang;
	private String formula;
	private int count;
	String[] splitForm;
	String[] newForm;
	String[] formatedAG;
	ArrayProperties arrayProps = new ArrayProperties();
	// (-A v -D v B) ^ D ^ -B ^ E ^ (-D v -E v C)
	// (A v -C) ^(-Av-Cv-B)^C^(-CvB)


	public void init() {
		lang = new AnimalScript("Markierungsalgorithmus fuer Hornformeln", "Tristan Gahler", 800, 600);
	}

	public String getName() {
		return "Markierungsalgorithmus fuer Hornformeln";
	}

	public String getAlgorithmName() {
		return "Hornformel Markierungsalgorithmus";
	}

	public String getAnimationAuthor() {
		return "Tristan Gahler";
	}

	public String getDescription() {
		return "Der Algorithmus dient dem finden einer erfuellenden Belegung fuer eine beliebige Hornformel." + "\n"
				+ "Hierbei werden Schritt fuer Schritt Literale markiert. " + "\n"
				+ "Am Ende erhaelt man die Antwort ob eine erfuellende Belegung existiert und wie diese aussieht." +"\n"
				+ "Ein Beispiel f�r eine unerf�llbare Formel ist (-Av-C)^C^(-Cv-B)^-B.";
	}

	public String getCodeExample() {
		return "1. In \"Implikationsschreibweise\" bringen" + "\n" + "2. Kommt (1 -> X) vor, markiere alle X" + "\n"
				+ "3. Kommt (X1 ... Xn -> 0) vor, und alle X1 ... Xn sind markiert, stoppe: unerfuellbar" + "\n"
				+ "4. Kommt (X1 ... Xn -> Y) vor, und alle X1 ... Xn sind markiert, Y aber noch nicht, markiere alle Y"
				+ "\n" + "5. Wenn Schritt 4 nicht zutraf, stoppe: erfuellbar, sonst weiter mit 3.";
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

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		formula = (String) primitives.get("formel");
		lang.setStepMode(true);
		display(formula);

		return lang.toString();
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer arg0, Hashtable<String, Object> arg1)
			throws IllegalArgumentException {

		if (checkFormula(arg1)) {
			return true;
		} else {
			throw new IllegalArgumentException("Bitte nur die angegebenen Zeichen verwenden");
		}
	}

	public boolean checkFormula(Hashtable<String, Object> arg1) {
		// if(arg1.contains("A")||!arg1.contains("B")||!arg1.contains("C")||!arg1.contains("D")||!arg1.contains("E")||!arg1.contains("(")||!arg1.contains(")")||!arg1.contains("^")||!arg1.contains("v")||!arg1.contains("-"))
		// {
		String helper = (String) arg1.get("formel");
		char[] helperArray = helper.toCharArray();
		for (int i = 0; i < helperArray.length; i++) {
			if (helperArray[i] != 'A' && helperArray[i] != 'B' && helperArray[i] != 'C' && helperArray[i] != 'D'
					&& helperArray[i] != 'E' && helperArray[i] != '(' && helperArray[i] != ')' && helperArray[i] != 'v'
					&& helperArray[i] != '^' && helperArray[i] != '-'&& helperArray[i]!=' ') {
				return false;
			}
		}
		return true;
	}

	public void display(String formula) {
		/*
		 * //counter TwoValueCounter counter = lang.newCounter(); CounterProperties cp =
		 * new CounterProperties(); cp.set(AnimationPropertiesKeys.FILLED_PROPERTY,
		 * true); cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);
		 * TwoValueView view = lang.newCounterView(counter, new Coordinates(80, 60), cp,
		 * true, true);
		 */

		// header
		TextProperties headerProps = new TextProperties();
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 24));
		lang.newText(new Coordinates(70, 30), "Hornformel Markierungsalgorithmus", "header", null, headerProps);

		// rectangle around the header
		RectProperties rectProps = new RectProperties();
		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.CYAN);
		rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		lang.newRect(new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW), new Offset(5, 5, "header", "SE"), "hRect",
				null, rectProps);

		arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
		arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
		arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.CYAN);

		TextProperties introductionProps = new TextProperties();
		introductionProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 18));
		lang.nextStep("Intro");
		Text introduction1 = lang.newText(new Coordinates(70, 90),
				"Der Hornformel Markierungsalgorithmus hilft uns eine Aussage zu treffen, ob eine Hornformel erfuellbar ist.",
				"introduction", null, introductionProps);
		Text introduction2 = lang.newText(new Coordinates(70, 110),
				"Hierbei werden wiederholt die 5 Schritte angewandt bis man anhand der Formel eine Aussage ueber Erfuellbarkeit treffen kann.",
				"introduction2", null, introductionProps);
		Text introduction3 = lang.newText(new Coordinates(70, 130),
				"Sollte die Formel erfuellbar sein ist eine erfuellende Belegung wenn man alle unmarkierten null, und alle markierten gleich eins setzt.",
				"introduction3", null, introductionProps);
		Text introduction4 = lang.newText(new Coordinates(70, 170),
				"Nachfolgend zeigen wir die Berechnung fuer die Formel: " + formula, "introduction4", null,
				introductionProps);

		lang.nextStep();
		introduction1.hide();
		introduction2.hide();
		introduction3.hide();
		introduction4.hide();

		TextProperties formulaDisplayHeader = new TextProperties();
		formulaDisplayHeader.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 20));
		lang.newText(new Coordinates(70, 130), "Zu betrachtende Formel: " + formula, "formulaDisplayHeader", null,
				formulaDisplayHeader);

		SourceCodeProperties hornProps = new SourceCodeProperties();
		hornProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		hornProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 16));
		hornProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		hornProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		SourceCode steps = lang.newSourceCode(new Coordinates(70, 140), "sourceCode", null, hornProps);

		steps.addCodeLine("", null, 0, null);
		steps.addCodeLine("", null, 0, null);
		steps.addCodeLine("1. In Implikationsschreibweise bringen", null, 0, null);
		steps.addCodeLine("2. Kommt (1 -> X) vor, markiere alle X", null, 0, null);
		steps.addCodeLine("3. Kommt (X1 ... Xn -> 0) vor, und alle X1 ... Xn sind markiert, stoppe: unerfuellbar", null,
				0, null);
		steps.addCodeLine(
				"4. Kommt (X1 ... Xn -> Y) vor, und alle X1 ... Xn sind markiert, Y aber noch nicht, markiere alle Y",
				null, 0, null);
		steps.addCodeLine("5. Wenn Schritt 4 nicht zutraf, stoppe: erfuellbar, sonst weiter mit 3.", null, 0, null);

		calculate(formula, steps);
	}

	// creates the implication
	private String createImp(String shortFormula) {		
		boolean changed = false;
		String newString = "";
		char[] helper = new char[shortFormula.toCharArray().length + 1];
		char[] helperArray = shortFormula.toCharArray();
		for (int i = 0; i < helper.length - 1; i++)
			helper[i] = helperArray[i];
		for (int i = helper.length - 2; changed == false; i--) {
			if (helper[i] != '^') {
				helper[i + 1] = helper[i];
			} else {
				helper[i] = '-';
				helper[i + 1] = '>';
				changed = true;
			}
		}
		for (int i = 0; i < helper.length; i++) {
			newString = newString + helper[i];
		}
		return newString;
	}

	// returns how many parts the formula has
	public int formulaLength(String formula) {
		count = formula.length() - formula.replace("^", "").length();
		return count;
	}

	// splits the formula in single pieces, removes logical and
	public void splitFormula(String formula) {
		splitForm = new String[formulaLength(formula) + 1];
		int pos = 0;
		for (int i = 0; i < count; i++) {
			int secondPos = formula.indexOf('^', pos);
			splitForm[i] = formula.substring(pos, secondPos);
			pos = secondPos + 1;
			//System.out.println(splitForm[i]);
		}
		splitForm[count] = formula.substring(pos);
		//System.out.println(splitForm[count]);
	}

	// removes empty spaces and shortens the formula that way
	public void removeSpace(String[] stringArray) {
		for (int i = 0; i < stringArray.length; i++) {
			splitForm[i] = splitForm[i].replaceAll("\\s+", "");
		}
	}

	// creats the implication form and calls the necessary methods
	public void imp(String[] splitForm) {
		newForm = new String[splitForm.length];
		removeSpace(splitForm);
		for (int i = 0; i < splitForm.length; i++) {
			// Fall X
			if (splitForm[i].equals("A"))
				splitForm[i] = "(1 -> A)";
			if (splitForm[i].equals("B"))
				splitForm[i] = "(1 -> B)";
			if (splitForm[i].equals("C"))
				splitForm[i] = "(1 -> C)";
			if (splitForm[i].equals("D"))
				splitForm[i] = "(1 -> D)";
			if (splitForm[i].equals("E"))
				splitForm[i] = "(1 -> E)";

			// Fall -X
			if (splitForm[i].equals("-A"))
				splitForm[i] = "(A -> 0)";
			if (splitForm[i].equals("-B"))
				splitForm[i] = "(B -> 0)";
			if (splitForm[i].equals("-C"))
				splitForm[i] = "(C -> 0)";
			if (splitForm[i].equals("-D"))
				splitForm[i] = "(D -> 0)";
			if (splitForm[i].equals("-E"))
				splitForm[i] = "(E -> 0)";
		}
		String[][] helper = new String[count + 1][15];
		for (int i = 0; i < splitForm.length; i++) {
			char[] helperChar = splitForm[i].toCharArray();
			for (int j = 0; j < splitForm[i].length(); j++) {
				helper[i][j] = String.valueOf(helperChar[j]);
			}
		}
		boolean changed = false;
		boolean freeImp;
		boolean[] posLit = new boolean[helper.length];
		for (int i = 0; i < splitForm.length; i++) {
			freeImp = true;
			for (int j = 0; j < splitForm[i].length(); j++) {
				if (helper[i][j].contains(">")) {
					freeImp = false;
				}
				// if the viewed part has a positive literal
				if (helper[i][j].equals("A") || helper[i][j].equals("B") || helper[i][j].equals("C")
						|| helper[i][j].equals("D") || helper[i][j].equals("E")) {
					if (!(helper[i][j - 1].equals("-"))) {
						posLit[i] = true;
					}
				}
			}
			// if the viewed part doesnt have a positiv literal
			if ((freeImp == true) && (posLit[i] == true)) {
				for (int k = 0; k < splitForm[i].length(); k++) {
					helper[i][k] = helper[i][k].replaceAll("-", "");
					helper[i][k] = helper[i][k].replaceAll("v", "^");
				}
			}
			if ((freeImp == true) && (posLit[i] == false)) {
				for (int k = 0; k < splitForm[i].length(); k++) {
					helper[i][k] = helper[i][k].replaceAll("-", "");
					helper[i][k] = helper[i][k].replaceAll("v", "^");
				}
				for (int l = helper.length - 1; changed == false; l++) {
					if (helper[i][l].equals(")")) {
						helper[i][l + 3] = ")";
						helper[i][l + 2] = "0";
						helper[i][l + 1] = ">";
						helper[i][l] = "-";
						changed = true;
					}
				}
			}
		}

		for (int i = 0; i < splitForm.length; i++) {
			String sHelper = "";
			for (int j = 0; helper[i][j] != null; j++) {
				sHelper = sHelper + helper[i][j];
			}
			removeSpace(splitForm);
			splitForm[i] = sHelper;
		}

		for (int i = 0; i < splitForm.length; i++)
			if (!splitForm[i].contains("->")) {
				splitForm[i] = createImp(splitForm[i]);
			}

		//for (int i = 0; i < splitForm.length; i++)
			//System.out.println(splitForm[i]);

	}

	// calls the methods necessary to create the array the user sees
	public void calculate(String formula, SourceCode steps) {
		splitFormula(formula);
		imp(splitForm);
		stepOne(formula, steps);
	}

	// builds a string from the single parts and connects them with a logic and
	public String buildForm() {
		String buildForm = "";
		int counter = 0;
		for (int i = 0; i < splitForm.length; i++) {
			buildForm = buildForm + splitForm[i];
			counter++;
			if (counter < splitForm.length)
				buildForm = buildForm + " ^ ";
		}
		return buildForm;
	}

	// removes null values from the array
	public String[] removeNull(String[] helperArray) {
		List<String> values = new ArrayList<String>();
		for (String data : helperArray) {
			if (data != null) {
				values.add(data);
			}
		}
		String[] target = values.toArray(new String[values.size()]);
		return target;

	}

	// creates a string array where every index is a single symbol
	public String[] formatedArray(String formula) {
		formula = formula.replaceAll("\\s+", "");
		String[] formatedArray = new String[formula.length()];
		char[] helperArray = formula.toCharArray();
		for (int i = 0; i < formula.length(); i++) {
			if (helperArray[i] != '-')
				formatedArray[i] = String.valueOf(helperArray[i]);
			else {
				formatedArray[i] = String.valueOf(helperArray[i]) + String.valueOf(helperArray[i + 1]);
				i++;
			}
		}
		formatedArray = removeNull(formatedArray);
		formatedAG = formatedArray;
		return formatedArray;
	}

	public void stepOne(String formula, SourceCode steps) {
		ArrayMarkerProperties arrayMProps = new ArrayMarkerProperties();
		arrayMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "");
		arrayMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		TextProperties introductionProps = new TextProperties();
		introductionProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 16));
		Text step1 = lang.newText(new Coordinates(70, 330), "1. " + formula + "  =  ", "formula display", null,
				introductionProps);

		steps.highlight(2);
		lang.nextStep("Schritt 1");

		step1.setText("1. " + formula + "  =  " + buildForm(), null, null);

		lang.nextStep();

		StringArray formulaArray = lang.newStringArray(new Coordinates(70, 420), formatedArray(buildForm()),
				"stringArray", null, arrayProps);
		ArrayMarker marker = lang.newArrayMarker(formulaArray, 0, "", null, arrayMProps);
		marker.hide();
		steps.unhighlight(2);
		stepTwo(formula, steps, marker, formulaArray, arrayMProps, step1);
	}

	public void stepTwo(String formula, SourceCode steps, ArrayMarker marker, StringArray formulaArray,
			ArrayMarkerProperties arrayMProps, Text step1) {
		formulaArray.showIndices(false, null, null);
		steps.highlight(3);
		lang.nextStep("Schritt 2");
		boolean highlightCounter = false;
		boolean[] markedHelper = new boolean[formatedAG.length];
		for (int i = 0; i < formatedAG.length - 4; i++) {
			if (highlightCounter == false) {
				highlightCounter = true;
			}
			if (formatedAG[i].equals("(") && formatedAG[i + 1].equals("1") && formatedAG[i + 2].equals("->")
					&& formatedAG[i + 4].equals(")")) {
				marker.show();
				formulaArray.highlightElem(i + 1, null, null);
				formulaArray.highlightElem(i + 2, null, null);
				formulaArray.highlightElem(i + 3, null, null);
				String markThis = formatedAG[i + 3];
				for (int j = 0; j < formatedAG.length; j++) {
					if (formatedAG[j].equals(markThis)) {
						marker.move(j, null, null);
						lang.nextStep();
						formulaArray.highlightCell(j, null, null);
						markedHelper[j] = true;
					}
				}
			}
			formulaArray.unhighlightElem(i + 1, null, null);
			formulaArray.unhighlightElem(i + 2, null, null);
			formulaArray.unhighlightElem(i + 3, null, null);
		}
		steps.unhighlight(3);
		marker.hide();
		stepThree(formula, steps, marker, formulaArray, arrayMProps, markedHelper, step1);
	}

	public void stepThree(String formula, SourceCode steps, ArrayMarker marker, StringArray formulaArray,
			ArrayMarkerProperties arrayMProps, boolean[] markedHelper, Text step1) {
		steps.highlight(4);
		lang.nextStep("Schritt 3");
		boolean helperEnd = false;
		for (int i = 0; i < formatedAG.length; i++) {
			boolean helper = true;
			if (formatedAG[i].equals("0")) {
				marker.move(i, null, null);
				marker.show();
				lang.nextStep();
				arrayMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "markiert?");
				for (int j = i; !formatedAG[j].equals("("); j--) {
					if (!formatedAG[j].equals("->") && !formatedAG[j].equals("^") && !formatedAG[j].equals("0")) {
						marker.move(j, null, null);
						lang.nextStep();
						if (markedHelper[j] == false) {
							helper = false;
							break;
						}
					}
				}
				// unaccomplishable, can stop immediatley
				if (helper == true) {
					helperEnd = true;
					break;
				}
			}
		}
		if (helperEnd == true) {
			steps.unhighlight(4);
			unaccomplishable(step1);
			marker.hide();
		} else {
			steps.unhighlight(4);
			marker.hide();
			stepFour(steps, marker, formulaArray, arrayMProps, markedHelper, step1);
		}
	}

	private void stepFour(SourceCode steps, ArrayMarker marker, StringArray formulaArray,
			ArrayMarkerProperties arrayMProps, boolean[] markedHelper, Text step1) {
		steps.highlight(5);
		lang.nextStep("Schritt 4");
		boolean changeHelper;
		boolean stepThree = false;
		String viewedHelper = "";
		for (int i = 0; i < formatedAG.length; i++) {
			changeHelper = true;
			if (formatedAG[i].equals(")") && markedHelper[i - 1] == false) {
				viewedHelper = formatedAG[i - 1];
				for (int j = i - 3; !formatedAG[j].equals("("); j--) {
					if (!formatedAG[j].equals("->") && !formatedAG[j].equals("^") && !formatedAG[j].equals("0")
							&& !formatedAG[j].equals(")")) {
						if (markedHelper[j] == false) {
							changeHelper = false;
							break;
						}
					}
				}
			} else {
				changeHelper = false;
			}
			if (changeHelper == true) {
				marker.move(i - 1, null, null);
				marker.show();
				lang.nextStep();
				for (int k = 0; k < formatedAG.length; k++) {
					if (formatedAG[k].equals(viewedHelper)) {
						formulaArray.highlightCell(k, null, null);
						markedHelper[k] = true;
						marker.move(k, null, null);
						lang.nextStep();
						stepThree = true;
					}
				}
			}
		}
		marker.hide();
		steps.unhighlight(5);
		if (stepThree == true) {
			steps.highlight(6);
			lang.nextStep("Schritt 5");
			steps.unhighlight(6);
			stepThree(viewedHelper, steps, marker, formulaArray, arrayMProps, markedHelper, step1);
		} else {
			accomplishable(step1, formatedAG, markedHelper);
		}
	}

	private String sat(String[] formatedAG, boolean[] markedHelper) {
		String helper = "";
		for (int i = 0; i < formatedAG.length; i++) {
			if (markedHelper[i] == true && !helper.contains(formatedAG[i]))
				helper = helper + formatedAG[i] + " ";
		}
		return helper;
	}

	private void unaccomplishable(Text step1) {
		step1.hide();
		TextProperties introductionProps = new TextProperties();
		introductionProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 16));
		lang.newText(new Coordinates(70, 330), "Da bereits alle Literale markiert sind, ist die Formel unerfuellbar", "result", null,
				introductionProps);
	}

	private void accomplishable(Text step1, String[] formatedAG2, boolean[] markedHelper) {
		step1.hide();
		TextProperties introductionProps = new TextProperties();
		introductionProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 16));
		lang.newText(new Coordinates(70, 330), "Die Formel " + formula
				+ " ist erfuellbar. Fuer eine erfuellende Belegung muessen folgende Variablen mit 1 belegt werden: "+ sat(formatedAG2, markedHelper)+", alle anderen mit 0.", "result", null,
				introductionProps);
	}
}
