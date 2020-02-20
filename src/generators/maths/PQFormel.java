/*
 * PQFormel.java
 * Ayman El Ouariachi, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.maths;

import java.awt.Font;
import java.util.Hashtable;
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

public class PQFormel implements ValidatingGenerator {

	private Translator translator;
	private Language lang;
	private SourceCode sc;
	private double a;
	private ArrayProperties arrayProps;
	private double b;
	private double c;
	private double d;
	private SourceCodeProperties scExampleProp;
	private SourceCode scExample;
	private SourceCodeProperties scProps;
	private TextProperties headerProp;
	private TextProperties descriptionProp;
	private TextProperties exampleHeaderProp;
	private Polyline hLine;
	private PolylineProperties titleLine;
	//Properties
	
	Text description;
	Text returnDesc;
	Text returnOut;
	Text returnOut2;
	DoubleArray ia;
	
	// Constants
	private static final String NAME ="NAME";
	private static final String TITLE = "PQ-Formel";
	private static final String DESCRIPTION = "beschreibungsText";
	private static final String RETURNGENERAL = "returnGeneral";
	private static final String RETURN = "RETURN";
	private static final String RETURN2 = "RETURN2";
	private static final String ALGOBESCH = "algorithmenbeschreibung";
	private static final String DESCRIPTION_ALGO = "description_algo";

	public PQFormel(String resource, Locale locale) {
		this.translator = new Translator(resource, locale);
		this.lang = new AnimalScript(this.translator.translateMessage(TITLE), "Ayman El Ouariachi", 800, 600);
		this.lang.setStepMode(true);

	}

	public void init() {
		lang = new AnimalScript("Nullstellenberechnung quadratischer Funktionen", "Ayman El Ouariachi", 800, 600);
		lang.setStepMode(true);
	}

	
	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		a = (double) primitives.get("a");
		arrayProps = (ArrayProperties) props.getPropertiesByName("arrayProps");
		b = (double) primitives.get("b");
		c = (double) primitives.get("c");
		d = (double) primitives.get("d");
		scExampleProp = (SourceCodeProperties) props.getPropertiesByName("scExampleProp");
		scProps = (SourceCodeProperties) props.getPropertiesByName("scProps");
		headerProp = (TextProperties) props.getPropertiesByName("headerProp");
		descriptionProp = (TextProperties) props.getPropertiesByName("descriptionProp");
		exampleHeaderProp = (TextProperties) props.getPropertiesByName("exampleHeaderProp");
		 this.titleLine = (PolylineProperties)props.getPropertiesByName("titleLine");
	    this.start(a,b,c,d);
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
		return "public double[] function(double a, double b, double c, double d, SourceCode scExample, SourceCode sc) {"
				+ "\n 	if (d != 0) {" 
				+ "\n		c -= d;" 
				+ "\n 		d -= d;" 
				+ "\n	}" 
				+ "\n	if (a != 0 && a != 1) {" 
				+ "\n		b = Math.round(b / a * 10000.0) / 10000.0;" 
				+ "\n		c = Math.round(c / a * 10000.0) / 10000.0;"
				+ "\n		a /= a;" 
				+ "\n	}"
				+ "\n	double pHalbe = Math.round(((-1) * (b / 2)) * 10000.0) / 10000.0;" 
				+ "\n	if ((pHalbe * pHalbe) - c < 0.0) {" 
				+ "\n		return null;"
				+ "\n	}"
				+ "\n	double wurzel = Math.round((Math.sqrt((pHalbe * pHalbe) - c)) * 10000.0) / 10000.0;"
				+ "\n	if (wurzel == 0.0) {" 
				+ "\n		double x1 = Math.round((pHalbe + wurzel) * 10000.0) / 10000.0;" 
				+ "\n		return new double[] { x1 };"
				+ "\n	}"
				+ "\n	double x1 = Math.round((pHalbe + wurzel) * 10000.0) / 10000.0;"
				+ "\n	double x2 = Math.round((pHalbe - wurzel) * 10000.0) / 10000.0;"
				+ "\n	return new double[] { x1, x2 };" 
				+ "\n}";
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
		a = (double) primitives.get("a");
		arrayProps = (ArrayProperties) props.getPropertiesByName("arrayProps");
		b = (double) primitives.get("b");
		c = (double) primitives.get("c");
		d = (double) primitives.get("d");
		scExampleProp = (SourceCodeProperties) props.getPropertiesByName("scExampleProp");
		scProps = (SourceCodeProperties) props.getPropertiesByName("scProps");
		headerProp = (TextProperties) props.getPropertiesByName("headerProp");
		descriptionProp = (TextProperties) props.getPropertiesByName("descriptionProp");
		exampleHeaderProp = (TextProperties) props.getPropertiesByName("exampleHeaderProp");
	} catch (Exception e) {
		// TODO: handle exception
		return false;
	}
		
		return true;
	}

	// helper functions
	/**
	 * displays the function correctly
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @return
	 */
	private String showFunction(double a, double b, double c, double d) {
		String function = "";

		function += a == 0.0 ? "" : a == 1.0 ? "x²" : a + "x²";
		// + or not
		function += !function.equals("") && (b != 0.0 || c != 0.0) ? " + " : "";
		function += b == 0.0 ? "" : b == 1.0 ? "x" : (b < 0.0 ? "(" + b + ")x" : b + "x");
		// + or not
		function += !function.equals("") && c != 0.0 ? " + " : "";
		function += c == 0.0 ? "" : (c < 0 ? "(" + c + ")" : c);
		function += " = " + d;

		return function;
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

	// main functions

	public void start(double a, double b, double c, double d) {

		// Create header
		headerProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.MONOSPACED, Font.BOLD, 20));
		lang.newText(new Coordinates(50, 20), this.translator.translateMessage(TITLE), "Title", null, headerProp);
        Node[] node = new Node[]{new Offset(0, 5, "Title", "SW"), new Offset(0, 5, "Title", "SE")};
        this.hLine = this.lang.newPolyline(node, "titleLine", null, this.titleLine);

		lang.nextStep();

		// Description

		description = lang.newText(new Coordinates(10, 55), this.translator.translateMessage(DESCRIPTION),
				"Beschreibung", null, descriptionProp);
		returnDesc = lang.newText(new Coordinates(10, 75), this.translator.translateMessage(RETURNGENERAL),
				"Beschreibung", null, descriptionProp);
		returnOut = lang.newText(new Coordinates(10, 95), this.translator.translateMessage(RETURN), "Beschreibung",
				null, descriptionProp);
		returnOut2 = lang.newText(new Coordinates(10, 115), this.translator.translateMessage(RETURN2), "Beschreibung",
				null, descriptionProp);

		lang.nextStep(this.translator.translateMessage("Beschreibung"));

		// Hide description from view
		description.hide();
		returnDesc.hide();
		returnOut.hide();
		returnOut2.hide();

		// Properties for the source code

		// source code
		sc = lang.newSourceCode(new Coordinates(10, 50), "sourceCode", null, scProps);

		sc.addCodeLine("public double[] PQFormel(double a, double b, double c, double d) {", null, 0, null); // 0
		sc.addCodeLine("if(d != 0) {", null, 1, null); // 1
		sc.addCodeLine("c -= d;", null, 2, null); // 2
		sc.addCodeLine("d -= d;", null, 2, null); // 3
		sc.addCodeLine("}", null, 1, null); // 4
		sc.addCodeLine("if(a!= 0 && a!= 1) {", null, 1, null); // 5
		sc.addCodeLine("b = Math.round(b/a *10000.0)/10000.0;", null, 2, null); // 6
		sc.addCodeLine("c = Math.round(c/a *10000.0)/10000.0;", null, 2, null); // 7
		sc.addCodeLine("a/=a;", null, 2, null); // 8
		sc.addCodeLine("}", null, 1, null); // 9
		sc.addCodeLine("double pHalbe = (-1)*b/2; ", null, 1, null); // 10
		sc.addCodeLine("if ((pHalbe * pHalbe) -c < 0.0) {", null, 1, null); // 11
		sc.addCodeLine("return null;", null, 2, null); // 12
		sc.addCodeLine("}", null, 1, null); // 13
		sc.addCodeLine("double wurzel = Math.round((Math.sqrt((pHalbe * pHalbe) -c)) *10000.0)/10000.0;", null, 1,
				null); // 14
		sc.addCodeLine("if(wurzel==0.0) {", null, 1, null); // 15
		sc.addCodeLine("double x1 = Math.round((pHalbe + wurzel)*10000.0)/10000.0;", null, 2, null); // 16
		sc.addCodeLine("return new double[] {x1};", null, 2, null); // 17
		sc.addCodeLine("} ", null, 1, null); // 18
		sc.addCodeLine("double x1 = pHalbe + wurzel;", null, 1, null); // 19
		sc.addCodeLine("double x2 = pHalbe - wurzel;", null, 1, null); // 20
		sc.addCodeLine("return new double[] {x1,x2};", null, 1, null); // 21
		sc.addCodeLine("}", null, 0, null); // 22

		lang.nextStep(this.translator.translateMessage("SourceCode"));

		// Example header

		lang.newText(new Offset(10, 0, "sourceCode", "NE"), this.translator.translateMessage("Beispiel") + " :",
				"Beispiel", null, exampleHeaderProp);

		// Example


		scExample = lang.newSourceCode(new Offset(10, 0, "Beispiel", "SW"), "Example", null, scExampleProp);
		// Display the example function
		scExample.addCodeLine(showFunction(a, b, c, d), null, 0, null);

		lang.nextStep(this.translator.translateMessage("Beispiel"));

		// function call

		double[] resultArray = function(a, b, c, d, scExample, sc);

		// now, create the IntArray object, linked to the properties
		if (resultArray != null) {
			lang.newText(new Offset(0, 10, "Example", "SW"), this.translator.translateMessage("Thesolutionis"),
					"solution", null, exampleHeaderProp);
			ia = lang.newDoubleArray(new Offset(10, -10, "solution", "E"), resultArray, "intArray", null, arrayProps);

			ia.highlightCell(0, null, null);
			ia.highlightCell(1, null, null);
		}
		lang.nextStep(this.translator.translateMessage("Solution"));

		unhighlightAll(sc);
		unhighlightAll(scExample);
		lang.nextStep();

	}

	public double[] function(double a, double b, double c, double d, SourceCode scExample, SourceCode sc) {

		// d has to 0
		sc.highlight(1);
		if (d != 0) {
			lang.nextStep();
			unhighlightAll(sc);
			sc.highlight(2);
			sc.highlight(3);

			scExample.addCodeElement(" |" + (d < 0 ? "-(" + d + ")" : " -" + d), null, 0, null);
			lang.nextStep("d != 0");
			c -= d;
			d -= d;
			scExample.addCodeLine(showFunction(a, b, c, d), null, 0, null);

		}
		lang.nextStep();
		// Wenn a != 1 && a != 0 dann muss die funktion durch a geteilt werden
		unhighlightAll(sc);
		sc.highlight(5);

		// before the xÂ², so a, has to be 1 to do the use PQ formula
		if (a != 0 && a != 1) {
			lang.nextStep();
			sc.unhighlight(5);
			sc.highlight(6);
			sc.highlight(7);
			sc.highlight(8);
			scExample.addCodeElement(" |" + (a < 0 ? "/(" + a + ")" : "/" + a), null, 0, null);
			lang.nextStep("a != 1");
			b = Math.round(b / a * 10000.0) / 10000.0;
			c = Math.round(c / a * 10000.0) / 10000.0;
			a /= a;
			scExample.addCodeLine(showFunction(a, b, c, d), null, 0, null);
		}
		lang.nextStep();
		// Nun haben wir die gewÃ¼nschte Form und kÃ¶nnen nun die PQ Formel ausrechnen
		unhighlightAll(sc);
		sc.highlight(10);

		// Calculates p half
		double pHalbe = Math.round(((-1) * (b / 2)) * 10000.0) / 10000.0;
		lang.nextStep(this.translator.translateMessage("Phalbeausrechnen"));
		unhighlightAll(sc);

		// If square root cannot be calculated => radicand < 0
		if ((pHalbe * pHalbe) - c < 0.0) {

			sc.highlight(14);
			scExample.addCodeLine(this.translator.translateMessage("EsexistiertkeineLosungimreelenBereich"), null, 0,
					null);

			unhighlightAll(sc);
			sc.highlight(12);
			scExample.highlight(scExample.length() - 1);

			return null;
		}
		// Calculates the square root
		double wurzel = Math.round((Math.sqrt((pHalbe * pHalbe) - c)) * 10000.0) / 10000.0;

		// If only one solution exists => root = 0
		if (wurzel == 0.0) {
			double x1 = Math.round((pHalbe + wurzel) * 10000.0) / 10000.0;
			sc.highlight(16);

			scExample.addCodeLine("x1 = ", null, 0, null);
			lang.nextStep();
			sc.highlight(10);
			scExample.addCodeElement(pHalbe + " + ", null, 0, null);
			lang.nextStep();
			sc.unhighlight(10);
			sc.highlight(14);
			String displayC = c < 0 ? "(" + c + ")" : "" + c;
			scExample.addCodeElement("√(" + (pHalbe * pHalbe) + "-" + displayC + ")", null, 0, null);
			lang.nextStep("");
			unhighlightAll(sc);
			sc.highlight(19);

			scExample.addCodeLine("x1 = " + pHalbe + " + " + wurzel, null, 0, null);
			lang.nextStep("x1");
			unhighlightAll(sc);

			sc.highlight(17);

			scExample.addCodeLine("x1 = " + x1, null, 0, null);
			scExample.highlight(scExample.length() - 1);
			return new double[] { x1 };
		}

		// else, so if two solution are existing
		double x1 = Math.round((pHalbe + wurzel) * 10000.0) / 10000.0;
		sc.highlight(19);

		scExample.addCodeLine("x1 = ", null, 0, null);
		lang.nextStep();
		sc.highlight(10);
		scExample.addCodeElement(pHalbe + " + ", null, 0, null);
		lang.nextStep();
		sc.unhighlight(10);
		sc.highlight(14);
		String displayC = c < 0 ? "(" + c + ")" : "" + c;
		scExample.addCodeElement("√(" + (pHalbe * pHalbe) + "-" + displayC + ")", null, 0, null);
		lang.nextStep();
		unhighlightAll(sc);

		double x2 = Math.round((pHalbe - wurzel) * 10000.0) / 10000.0;
		sc.highlight(20);
		scExample.addCodeLine("x2 = ", null, 0, null);
		lang.nextStep();
		lang.nextStep();
		sc.highlight(10);
		scExample.addCodeElement(pHalbe + " - ", null, 0, null);
		lang.nextStep();
		sc.unhighlight(10);
		sc.highlight(14);
		scExample.addCodeElement("√(" + (pHalbe * pHalbe) + "-" + displayC + ")", null, 0, null);

		lang.nextStep();
		unhighlightAll(sc);
		sc.highlight(19);

		scExample.addCodeLine("x1 = " + pHalbe + " + " + wurzel, null, 0, null);
		lang.nextStep("x1");
		unhighlightAll(sc);
		sc.highlight(20);
		scExample.addCodeLine("x2 = " + pHalbe + " - " + wurzel, null, 0, null);

		lang.nextStep("x2");
		unhighlightAll(sc);
		sc.highlight(21);

		scExample.addCodeLine("x1 = " + x1, null, 0, null);
		scExample.addCodeLine("x2 = " + x2, null, 0, null);

		scExample.highlight(scExample.length() - 1);

		scExample.highlight(scExample.length() - 2);

		return new double[] { x1, x2 };
	}

	public static void main(String[] args) {
		Generator generator = new PQFormel("resources/pq", Locale.US);
		Animal.startGeneratorWindow(generator);


	}

}