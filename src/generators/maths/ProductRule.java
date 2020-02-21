/*
 * PQFormel.java
 * Ayman El Ouariachi, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.maths;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.DoubleArray;
import algoanim.primitives.Polyline;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import translator.Translator;

public class ProductRule implements ValidatingGenerator {

	private Translator translator;
	private Language lang;
	private SourceCode sc;
	private String function1;
	private String function2;
	private ArrayProperties arrayProps;

	private SourceCodeProperties scExampleProp;
	private SourceCode scExample;
	private SourceCodeProperties scProps;
	private TextProperties headerProp;
	private TextProperties descriptionProp;
	private TextProperties exampleHeaderProp;
	private PolylineProperties titleLine;

	// Properties

	Text description;
	Text returnDesc;
	Text returnIn;
	DoubleArray ia;

	// Constants
	private static final String NAME = "NAME";
	private static final String TITLE = "Produktregel";
	private static final String DESCRIPTION = "beschreibungsText";
	private static final String RETURNGENERAL = "returnGeneral";
	private static final String ALGOBESCH = "algorithmenbeschreibung";
	private static final String RETURNIN = "returnIN";

	public ProductRule(String resource, Locale locale) {
		this.translator = new Translator(resource, locale);
		this.lang = new AnimalScript(this.translator.translateMessage(TITLE), "Ayman El Ouariachi", 800, 600);
		this.lang.setStepMode(true);

	}

	public void init() {
		lang = new AnimalScript("Ableitung nach der Produktregel", "Ayman El Ouariachi", 800, 600);
		lang.setStepMode(true);
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		// Primitives:
		function1 = (String) primitives.get("function1");
		function2 = (String) primitives.get("function2");

		// Properties
		arrayProps = (ArrayProperties) props.getPropertiesByName("arrayProps");
		scExampleProp = (SourceCodeProperties) props.getPropertiesByName("scExampleProp");
		scProps = (SourceCodeProperties) props.getPropertiesByName("scProps");
		headerProp = (TextProperties) props.getPropertiesByName("headerProp");
		descriptionProp = (TextProperties) props.getPropertiesByName("descriptionProp");
		exampleHeaderProp = (TextProperties) props.getPropertiesByName("exampleHeaderProp");

		this.titleLine = (PolylineProperties) props.getPropertiesByName("titleLine");
		this.start(parser(function1), parser(function2));
		this.lang.finalizeGeneration();
		return lang.toString();
	}

	public String getName() {
		return translator.translateMessage(NAME);
	}

	public String getAlgorithmName() {
		return translator.translateMessage(TITLE);
	}

	public String getAnimationAuthor() {
		return "Ayman El Ouariachi";
	}

	public String getDescription() {
		return this.translator.translateMessage(ALGOBESCH);
	}

	public String getCodeExample() {
		return "public double[] function(LinkedList<Double> faktor1, LinkedList<Double> faktor2, SourceCode scExample,\r\n"
				+ "			SourceCode sc) {\r\n" + "				\r\n"
				+ "		if (faktor1.isEmpty() && faktor2.isEmpty()) {\r\n" + "			return null;\r\n"
				+ "		}\r\n" + "		if (faktor1.equals(null) || faktor2.equals(null)) {\r\n"
				+ "			return null;\r\n" + "		}\r\n" + "\r\n"
				+ "		long notZeros = faktor1.stream().filter(x -> x != 0).count();\r\n"
				+ "		long notZerosII = faktor2.stream().filter(x -> x != 0).count();\r\n" + "\r\n"
				+ "		if (notZeros == 0 || notZerosII == 0)\r\n" + "			return new double[] { 0.0 };\r\n"
				+ "		\r\n" + "		LinkedList<Double> dFaktor1 = new LinkedList<Double>();\r\n"
				+ "		LinkedList<Double> dFaktor2 = new LinkedList<Double>();\r\n" + "\r\n" + "\r\n"
				+ "		for (int i = 1; i < faktor1.size(); i++) {\r\n"
				+ "			dFaktor1.add(faktor1.get(i) * i);\r\n" + "		}\r\n"
				+ "		for (int i = 1; i < faktor2.size(); i++) {\r\n"
				+ "			dFaktor2.add(faktor2.get(i) * i);\r\n" + "		}\r\n" + "\r\n"
				+ "		int a = dFaktor1.size() - 1 + faktor2.size() - 1;\r\n"
				+ "		int b = dFaktor2.size() - 1 + faktor1.size() - 1;\r\n"
				+ "		int newSize = a > b ? a : b;\r\n" + "		\r\n"
				+ "		double[] ergebnis = new double[newSize + 1];\r\n" + "		\r\n"
				+ "		for (int i = 0; i < ergebnis.length; i++) {\r\n" + "			ergebnis[i] = 0.0;\r\n"
				+ "		}\r\n" + "\r\n" + "		for (int i = 0; i < dFaktor1.size(); i++) {\r\n"
				+ "			for (int j = 0; j < faktor2.size(); j++) {\r\n" + "				int stelle = i + j;\r\n"
				+ "				ergebnis[stelle] += dFaktor1.get(i) * faktor2.get(j);\r\n" + "			}\r\n"
				+ "		}\r\n" + "		for (int i = 0; i < dFaktor2.size(); i++) {\r\n"
				+ "			for (int j = 0; j < faktor1.size(); j++) {\r\n" + "				int stelle = i + j;\r\n"
				+ "				ergebnis[stelle] += dFaktor2.get(i) * faktor1.get(j);\r\n" + "			}\r\n"
				+ "		}\r\n" + "\r\n" + "		return ergebnis;\r\n" + "	}";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return translator.getCurrentLocale();
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
	}

	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		try {

			function1 = (String) primitives.get("function1");
			function2 = (String) primitives.get("function2");
			
			arrayProps = (ArrayProperties) props.getPropertiesByName("arrayProps");

			scExampleProp = (SourceCodeProperties) props.getPropertiesByName("scExampleProp");
			scProps = (SourceCodeProperties) props.getPropertiesByName("scProps");
			headerProp = (TextProperties) props.getPropertiesByName("headerProp");
			descriptionProp = (TextProperties) props.getPropertiesByName("descriptionProp");
			exampleHeaderProp = (TextProperties) props.getPropertiesByName("exampleHeaderProp");

		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		if (parser(function1).isEmpty() || parser(function2).isEmpty()) {
			return false;
		}	
		if (parser(function1).equals(null)|| parser(function2).equals(null)) {
			return false;
		}
		return true;
	}

	// helper functions
	/**
	 * This function Parses the Function
	 * 
	 * @param functionAsString
	 * @return
	 */
	private LinkedList<Double> parser(String functionAsString) {
		LinkedList<Double> func = new LinkedList<Double>();
		String[] teile1 = functionAsString.split(";");

		for (int i = 0; i < teile1.length; i++) {
			try {

				func.add(Double.parseDouble(teile1[i]));
			}catch(Exception ex){
				return null;
			}
		}

		return func;
	}

	private String getExponent(int number) {
		String result = "";

		while (number > 0) {
			result += getExponentOfDigit(number % 10);
			number = number / 10;
		}
		return new StringBuilder(result).reverse().toString();
	}

	/**
	 * returns a String from an given integer as an exponent
	 * 
	 * @param i
	 * @return
	 */
	private String getExponentOfDigit(int i) {

		switch (i) {
		case 0:
			return "\u2070";
		case 1:
			return "\u00B9";
		case 2:
			return "\u00B2";
		case 3:
			return "\u00B3";
		case 4:
			return "\u2074";
		case 5:
			return "\u2075";
		case 6:
			return "\u2076";
		case 7:
			return "\u2077";
		case 8:
			return "\u2078";
		case 9:
			return "\u2079";

		default:
			return "";
		}
	}

	/**
	 * displays the function correctly
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @return
	 */
	private String showFunctionList(LinkedList<Double> func) {
		String function = "";

		if (func.size() >= 2) {
			for (int i = func.size() - 1; i >= 2; i--) {
				function += func.get(i) > 0 ? func.get(i) : "(" + func.get(i) + ")";
				function += "x" + getExponent(i);
				function += " + ";
			}
			function += func.get(1) > 0 ? func.get(1) : "(" + func.get(1) + ")";
			function += "x + ";
			function += func.get(0) > 0 ? func.get(0) : "(" + func.get(0) + ")";
		} else if(func.size()>1){
			function += func.get(1) > 0 ? func.get(1) : "(" + func.get(1) + ")";
			function += "x + ";
			function += func.get(0) > 0 ? func.get(0) : "(" + func.get(0) + ")";
		}else {
			function += func.get(0) > 0 ? func.get(0) : "(" + func.get(0) + ")";
		}

		return function;
	}

	private String displayFunction(LinkedList<Double> func1, LinkedList<Double> func2) {

		return "f(x)= [" + showFunctionList(func1) + "] * [" + showFunctionList(func2) + "]";
	}

	private LinkedList<Double> arrayToList(double[] tmp) {
		LinkedList<Double> resultList = new LinkedList<Double>();
		for (double zahl : tmp)
			resultList.add(zahl);
		return resultList;
	}

	/**
	 * unhighlight everything from a given sourcecode
	 * 
	 * @param sc
	 */
	private void unhighlightAll(SourceCode sc) {
		for (int i = 0; i < sc.length(); i++) {
			sc.unhighlight(i);
		}
	}

	private void highlightLines(SourceCode sc, int[] lines) {
		for (int line : lines)
			sc.highlight(line);
	}
	// main functions

	public void start(LinkedList<Double> func1, LinkedList<Double> func2) {

		// Create header
		headerProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.MONOSPACED, Font.BOLD, 20));
		lang.newText(new Coordinates(50, 20), this.translator.translateMessage(TITLE), "Title", null, headerProp);
		Node[] node = new Node[] { new Offset(0, 5, "Title", "SW"), new Offset(0, 5, "Title", "SE") };
		this.lang.newPolyline(node, "titleLine", null, this.titleLine);
		lang.nextStep();

		// Description

		description = lang.newText(new Coordinates(10, 55), this.translator.translateMessage(DESCRIPTION),
				"Beschreibung", null, descriptionProp);
		returnIn = lang.newText(new Coordinates(10, 75), this.translator.translateMessage(RETURNIN), "Beschreibung",
				null, descriptionProp);
		returnDesc = lang.newText(new Coordinates(10, 95), this.translator.translateMessage(RETURNGENERAL),
				"Beschreibung", null, descriptionProp);

		lang.nextStep(this.translator.translateMessage("Beschreibung"));

		// Hide description from view
		description.hide();
		returnIn.hide();
		returnDesc.hide();

		// Properties for the source code

		// source code
		sc = lang.newSourceCode(new Coordinates(10, 50), "sourceCode", null, scProps);

		sc.addCodeLine("public double[] Productrule(LinkedList<Double> faktor1, LinkedList<Double> faktor2) {", null, 0,
				null); // 0
		sc.addCodeLine("if (faktor1.isEmpty() && faktor2.isEmpty()) ", null, 1, null); // 1
		sc.addCodeLine("return null;", null, 2, null); // 2
		sc.addCodeLine("if (faktor1.equals(null) || faktor2.equals(null)) ", null, 1, null); // 3
		sc.addCodeLine("return null;", null, 2, null); // 4
		sc.addCodeLine("long notZeros = faktor1.stream().filter(x -> x != 0).count();", null, 1, null); // 5
		sc.addCodeLine("long notZerosII = faktor2.stream().filter(x -> x != 0).count();", null, 1, null); // 6
		sc.addCodeLine("if (notZeros == 0 || notZerosII == 0)", null, 1, null); // 7
		sc.addCodeLine("return new double[] { 0.0 };", null, 2, null); // 8
		sc.addCodeLine("LinkedList<Double> dFaktor1 = new LinkedList<Double>();", null, 1, null); // 9
		sc.addCodeLine("LinkedList<Double> dFaktor2 = new LinkedList<Double>();", null, 1, null); // 10
		sc.addCodeLine("for (int i = 1; i < faktor1.size(); i++) ", null, 1, null); // 11
		sc.addCodeLine("dFaktor1.add(faktor1.get(i) * i);", null, 2, null); // 12
		sc.addCodeLine("for (int i = 1; i < faktor2.size(); i++) ", null, 1, null); // 13
		sc.addCodeLine("dFaktor2.add(faktor2.get(i) * i);", null, 2, null); // 14
		sc.addCodeLine("======" + translator.translateMessage("zusammenfassung") + "======", null, 1, null);// 15
		sc.addCodeLine("for (int i = 0; i < dFaktor1.size(); i++) {", null, 1, null); // 16
		sc.addCodeLine("for (int j = 0; j < faktor2.size(); j++) {", null, 2, null); // 17
		sc.addCodeLine("int stelle = i + j;", null, 3, null); // 18
		sc.addCodeLine("ergebnis[stelle] += dFaktor1.get(i) * faktor2.get(j);", null, 3, null); // 19
		sc.addCodeLine("}", null, 2, null); // 20
		sc.addCodeLine("}", null, 1, null); // 21
		sc.addCodeLine("for (int i = 0; i < dFaktor2.size(); i++) {", null, 1, null); // 22
		sc.addCodeLine("for (int j = 0; j < faktor1.size(); j++) {", null, 2, null); // 23
		sc.addCodeLine("int stelle = i + j;", null, 3, null); // 24
		sc.addCodeLine("ergebnis[stelle] += dFaktor2.get(i) * faktor1.get(j);", null, 3, null); // 25
		sc.addCodeLine("}", null, 2, null); // 26
		sc.addCodeLine("}", null, 1, null); // 27
		sc.addCodeLine("return ergebnis;", null, 1, null); // 28
		sc.addCodeLine("}", null, 0, null); // 29

		lang.nextStep(this.translator.translateMessage("SourceCode"));

		// Example header

		lang.newText(new Offset(70, 0, "sourceCode", "NE"), this.translator.translateMessage("Beispiel") + " :",
				"Beispiel", null, exampleHeaderProp);

		// Example

		scExample = lang.newSourceCode(new Offset(30, 0, "Beispiel", "SW"), "Example", null, scExampleProp);
		// Display the example function
		scExample.addCodeLine(displayFunction(parser(function1), parser(function2)), null, 0, null);

		lang.nextStep(this.translator.translateMessage("Beispiel"));

		// function call

		double[] resultArray = function(func1, func2, scExample, sc);
		// now, create the IntArray object, linked to the properties

		if (resultArray != null) {
			lang.newText(new Offset(0, 10, "Example", "SW"), this.translator.translateMessage("Thesolutionis"),
					"solution", null, exampleHeaderProp);
			ia = lang.newDoubleArray(new Offset(10, -10, "solution", "E"), resultArray, "intArray", null, arrayProps);

			for (int i = 0; i < ia.getLength(); i++) {
				ia.highlightCell(i, null, null);
			}
		}

		lang.nextStep(this.translator.translateMessage("Solution"));

		unhighlightAll(sc);
		unhighlightAll(scExample);
		lang.nextStep();

	}

	boolean functionIs0(LinkedList<Double> func) {
		for (double zahl : func)
			if (zahl != 0)
				return false;
		return true;
	}

	public double[] function(LinkedList<Double> faktor1, LinkedList<Double> faktor2, SourceCode scExample,
			SourceCode sc) {

		// trivial cases
		sc.highlight(1);
		if (faktor1.isEmpty() && faktor2.isEmpty()) {
			sc.highlight(2);
			scExample.addCodeLine(translator.translateMessage("emptycase"), null, 0, null);

			lang.nextStep(translator.translateMessage("emptyCASE"));
			return null;
		}
		unhighlightAll(sc);
		sc.highlight(3);
		if (faktor1.equals(null) || faktor2.equals(null)) {
			sc.highlight(4);
			scExample.addCodeLine(translator.translateMessage("nullException"), null, 0, null);
			lang.nextStep(translator.translateMessage("nullEXCEPTION"));
			return null;
		}
		unhighlightAll(sc);

		long notZeros = faktor1.stream().filter(x -> x != 0).count();
		long notZerosII = faktor2.stream().filter(x -> x != 0).count();
		sc.highlight(7);
		if (notZeros == 0 || notZerosII == 0) {
			sc.highlight(8);
			scExample.addCodeLine("f'(x) = 0", null, 0, null);
			return new double[] { 0.0 };
		}
		unhighlightAll(sc);
		// Ableitungsliste erstellen
		// faktor1'
		LinkedList<Double> dFaktor1 = new LinkedList<Double>();
		// faktor2'
		LinkedList<Double> dFaktor2 = new LinkedList<Double>();

		// Ableitung bilden
		for (int i = 1; i < faktor1.size(); i++) {
			dFaktor1.add(faktor1.get(i) * i);
		}

		highlightLines(sc, new int[] { 9, 11, 12 });
		scExample.addCodeLine(translator.translateMessage("ersteAbleitung"), null, 0, null);
		scExample.addCodeLine("u(x)= " + showFunctionList(faktor1), null, 0, null);
		scExample.addCodeLine("u'(x)= " + showFunctionList(dFaktor1), null, 0, null);
		lang.nextStep(translator.translateMessage("ersteAbleitung"));
		unhighlightAll(sc);

		for (int i = 1; i < faktor2.size(); i++) {
			dFaktor2.add(faktor2.get(i) * i);
		}

		highlightLines(sc, new int[] { 10, 13, 14 });
		scExample.addCodeLine(translator.translateMessage("zweiteAbleitung"), null, 0, null);
		scExample.addCodeLine("v(x)= " + showFunctionList(faktor2), null, 0, null);
		scExample.addCodeLine("v'(x)= " + showFunctionList(dFaktor2), null, 0, null);
		lang.nextStep(translator.translateMessage("zweiteAbleitung"));
		unhighlightAll(sc);
		// ErgebnissArray erstellen

		// Erstmal die große herausfinden dazu nimmt man die addition der beiden längen,
		// da zwei variablen mit einander multipliziert werrden, indem man die
		// exponenten addiert
		// aber es muss erst einmal gecheckt werden, welches die großere ist
		int a = dFaktor1.size() - 1 + faktor2.size() - 1;
		int b = dFaktor2.size() - 1 + faktor1.size() - 1;
		int newSize = a > b ? a : b;
		// ergebniss Array erstellen mit der neuen größe

		double[] ergebnis = new double[newSize + 1];
		// Array initalisieren
		for (int i = 0; i < ergebnis.length; i++) {
			ergebnis[i] = 0.0;
		}

		/**
		 * Die eigentliche Ableitung: faktor1' * faktor 2 + faktor 1 * faktor2'
		 */
		scExample.addCodeLine(translator.translateMessage("Formel"), null, 0, null);
		scExample.addCodeLine("f'(x) = u'(x) * v(x) + u(x) * v*(x)", null, 0, null);
		lang.nextStep(translator.translateMessage("Formel"));
		scExample.addCodeLine(translator.translateMessage("eingesetzt"), null, 0, null);
		scExample.addCodeLine("f'(x)= [" + showFunctionList(dFaktor1) + " * " + showFunctionList(faktor2) + "] + ["
				+ showFunctionList(faktor1) + " * " + showFunctionList(dFaktor2) + "]", null, 0, null);
		lang.nextStep(translator.translateMessage("eingesetzt"));
		for (int i = 0; i < dFaktor1.size(); i++) {
			for (int j = 0; j < faktor2.size(); j++) {
				int stelle = i + j;
				ergebnis[stelle] += dFaktor1.get(i) * faktor2.get(j);
			}
		}
		highlightLines(sc, new int[] { 16, 17, 18, 19, 20, 21 });
		scExample.addCodeLine(translator.translateMessage("ersterSummand"), null, 0, null);
		scExample.addCodeLine("f'(x)= [" + showFunctionList(arrayToList(ergebnis)) + "] + [" + showFunctionList(faktor1)
				+ " * " + showFunctionList(dFaktor2) + "]", null, 0, null);
		lang.nextStep(translator.translateMessage("ersterSummand"));

		unhighlightAll(sc);
		for (int i = 0; i < dFaktor2.size(); i++) {
			for (int j = 0; j < faktor1.size(); j++) {
				int stelle = i + j;
				ergebnis[stelle] += dFaktor2.get(i) * faktor1.get(j);
			}
		}

		highlightLines(sc, new int[] { 22, 23, 24, 25, 26, 27 });
		scExample.addCodeLine(translator.translateMessage("komplett"), null, 0, null);
		scExample.addCodeLine("f(x) = " + showFunctionList(arrayToList(ergebnis)), null, 0, null);
		lang.nextStep(translator.translateMessage("komplett"));
		unhighlightAll(sc);
		sc.highlight(28);
		return ergebnis;
	}

	public static void main(String[] args) {
		Generator generator = new ProductRule("resources/translationProductRule", Locale.US);
		Animal.startGeneratorWindow(generator);

	}

}