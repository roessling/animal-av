package generators.maths;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.DoubleArray;
import algoanim.primitives.Polyline;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.AnimationType;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;
import algoanim.util.Offset;
import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;
import translator.Translator;

public class Skalarprodukt implements ValidatingGenerator {

	private Translator translator;
	private Language lang;
	private SourceCode sc;
	private int[] vec1;
	private int[] vec2;
	private SourceCodeProperties scExampleProp;
	private SourceCode scExample;
	private SourceCodeProperties scSourceCodeProp;
	private TextProperties headerProp;
	private TextProperties descriptionProp;
	private TextProperties formulaProp;
	private PolylineProperties titleLine;
	private TextProperties solutionSentenceProp;
	private int questionID = 0;
	
	private final String transpondiert = "T";
	// Properties

	Text description;
	Text returnDesc;
	Text returnOut;

	// Constants
	private static final String NAME = "NAME";
	private static final String TITLE = "TITLE";
	private static final String DESCRIPTION = "Description";
	private static final String RETURNGENERAL = "returnGeneral";
	private static final String RETURN = "RETURN";
	private static final String ALGODESCRIPTION = "algorithmenbeschreibung";

	public Skalarprodukt(String resource, Locale locale) {
		this.translator = new Translator(resource, locale);
		this.lang = new AnimalScript(this.translator.translateMessage(TITLE), "Romal Bijan, Luisa Dyroff", 800, 600);
		this.lang.setStepMode(true);
		this.lang.setInteractionType(1024);

	}

	public void init() {

		lang = new AnimalScript("Berechnung des Skalarprodukts", "Romal Bijan, Luisa Dyroff", 800, 600);
		lang.setStepMode(true);
		this.lang.setInteractionType(1024);

	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		vec1 = (int[]) primitives.get("vec1");
		vec2 = (int[]) primitives.get("vec2");
		scExampleProp = (SourceCodeProperties) props.getPropertiesByName("scExampleProp");
		scSourceCodeProp = (SourceCodeProperties) props.getPropertiesByName("scSourceCodeProp");
		headerProp = (TextProperties) props.getPropertiesByName("headerProp");
		descriptionProp = (TextProperties) props.getPropertiesByName("descriptionProp");
		formulaProp = (TextProperties) props.getPropertiesByName("formulaProp");
		titleLine = (PolylineProperties) props.getPropertiesByName("titleLine");
		solutionSentenceProp = (TextProperties) props.getPropertiesByName("solutionSentenceProp");
		start(vec1, vec2);
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
		return "Romal Bijan, Luisa Dyroff";
	}

	public String getDescription() {
		return this.translator.translateMessage(ALGODESCRIPTION);
	}

	public String getCodeExample() {
		return "public int calculate(int[] vec1, int[] vec2) {" + "\n 	int skalarprodukt = 0;"
				+ "\n		for(int i = 0; i < vec1.length; i++)" + "\n 		    skalarprodukt += vec1[i] * vec2[i];"
				+ "\n	    }" + "\n 	return skalarprodukt;" + "\n}";
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
			vec1 = (int[]) primitives.get("vec1");
			vec2 = (int[]) primitives.get("vec2");
			scExampleProp = (SourceCodeProperties) props.getPropertiesByName("scExampleProp");
			scSourceCodeProp = (SourceCodeProperties) props.getPropertiesByName("scSourceCodeProp");
			headerProp = (TextProperties) props.getPropertiesByName("headerProp");
			descriptionProp = (TextProperties) props.getPropertiesByName("descriptionProp");
			formulaProp = (TextProperties) props.getPropertiesByName("formulaProp");
			titleLine = (PolylineProperties) props.getPropertiesByName("titleLine");
			solutionSentenceProp = (TextProperties) primitives.get("solutionSentenceProp");
  
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		if (vec1.length != vec2.length) {
			checkLength(vec1, vec2);
			return false;
		}

		return true;
	}

	// helper functions

	private String displayNumber(int zahl) {

		return zahl > 0 ? zahl + "" : "(" + zahl + ")";
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

	private void firstLine(int[] vec1, int[] vec2, SourceCode example, SourceCode sourcecode) {

		example.addCodeElement("(", null, 0, null);

		for (int i = 0; i < vec1.length; i++) {
			sourcecode.highlight(2);
			sourcecode.highlight(4);
			lang.nextStep();
			sourcecode.highlight(3);
			lang.nextStep();
			if (i != 0) {
				example.addCodeElement("+", null, 0, null);

			}
			example.addCodeElement(displayNumber(vec1[i]) + "*" + displayNumber(vec1[i]), null, 0, null);
			if (i != vec1.length - 1)
				lang.nextStep();
			unhighlightAll(sourcecode);
		}
		example.addCodeElement(")", null, 0, null);
	}

	private void displayArrayAsVector(int[] a, int[] b, SourceCode sc) {
		sc.addCodeElement(this.translator.translateMessage("VEKTOR") + "1: (", null, 0, null);
		for (int i = 0; i < a.length; i++) {
			sc.addCodeElement(displayNumber(a[i]), null, 0, null);
			if (!(i == a.length - 1)) {
				sc.addCodeElement(",", null, 0, null);
			} else {
				sc.addCodeElement(")", null, 0, null);
			}
		}
		sc.addCodeElement("                    ", null, 0, null);
		sc.addCodeElement(this.translator.translateMessage("VEKTOR") + "2: (", null, 0, null);
		for (int i = 0; i < b.length; i++) {
			sc.addCodeElement(displayNumber(b[i]), null, 0, null);
			if (!(i == b.length - 1)) {
				sc.addCodeElement(",", null, 0, null);
			} else {
				sc.addCodeElement(")", null, 0, null);
			}
		}
	}

	private void checkLength(int[] vec1, int[] vec2) {

		TextProperties textProp = new TextProperties();
		textProp.set("color", Color.RED);
		textProp.set("font", new Font("Bold", 0, 18));
		this.lang.newText(new Coordinates(50, 200),
				"Das Skalarprodukt kann nicht berechnet werden, da die Vektoren nicht die gleiche Länge haben.", "text",
				(DisplayOptions) null, textProp);

	}

	// main functions

	public void start(int[] vec1, int[] vec2) {

		// Create header
		headerProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.MONOSPACED, Font.BOLD, 20));

		lang.newText(new Coordinates(50, 20), this.translator.translateMessage(TITLE), "Title", null, headerProp);
		Node[] node = new Node[] { new Offset(0, 5, "Title", "SW"), new Offset(0, 5, "Title", "SE") };
		this.lang.newPolyline(node, "titleLine", null, this.titleLine);

		lang.nextStep();
		
        MultipleChoiceQuestionModel question = new MultipleChoiceQuestionModel(String.valueOf(questionID));
		question.setPrompt(this.translator.translateMessage("question1"));
        question.addAnswer(this.translator.translateMessage("answer1.0"), 0, this.translator.translateMessage("question1FeedbackW"));
        question.addAnswer(this.translator.translateMessage("answer1.1"), 1, this.translator.translateMessage("question1FeedbackR"));
        this.lang.addMCQuestion(question);
        questionID++;
        lang.nextStep();
        

		// Description

		description = lang.newText(new Coordinates(10, 55), this.translator.translateMessage(DESCRIPTION),
				"Beschreibung", null, descriptionProp);
		returnDesc = lang.newText(new Coordinates(10, 75), this.translator.translateMessage(RETURNGENERAL),
				"Beschreibung", null, descriptionProp);
		returnOut = lang.newText(new Coordinates(10, 95), this.translator.translateMessage(RETURN), "Beschreibung",
				null, descriptionProp);

		lang.nextStep(this.translator.translateMessage("Beschreibung"));

		// Hide description from view
		description.hide();
		returnDesc.hide();
		returnOut.hide();

		// Properties for the source code

		// source code
		sc = lang.newSourceCode(new Coordinates(10, 50), "sourceCode", null, scSourceCodeProp);

		sc.addCodeLine("public int calculate(int[] vec1, int[] vec2){", null, 0, null); // 0
		sc.addCodeLine("int skalarprodukt = 0;", null, 1, null); // 1
		sc.addCodeLine("for(int i = 0; i< vec1.length; i++){", null, 1, null); // 2
		sc.addCodeLine("skalarprodukt += vec1[i] + vec2[i];", null, 2, null); // 3
		sc.addCodeLine("}", null, 1, null); // 4
		sc.addCodeLine("return skalarprodukt;", null, 1, null); // 5
		sc.addCodeLine("}", null, 0, null); // 6

		lang.nextStep(this.translator.translateMessage("SourceCode"));

		// Example header

		lang.newText(new Offset(50, 0, "sourceCode", "NE"), this.translator.translateMessage("Beispiel") + " :",
				"Beispiel", null, formulaProp);

		// Example

		scExample = lang.newSourceCode(new Offset(50, 0, "Beispiel", "SW"), "Example", null, scExampleProp);
		scExample.addCodeLine(this.translator.translateMessage("FORMEL") + "(a,b,c) * (d,e,f) = (a*b + c*d + e*f)",
				null, 0, null);
		scExample.addCodeLine("", null, 0, null);

		// Display the example function

		displayArrayAsVector(vec1, vec2, scExample);
		lang.nextStep();
		sc.highlight(0);
		lang.nextStep();
		sc.unhighlight(0);
		sc.highlight(1);
		lang.nextStep();
		sc.unhighlight(1);
		scExample.addCodeLine("", null, 0, null);
		firstLine(vec1, vec2, scExample,sc);
		lang.nextStep(this.translator.translateMessage("Beispiel"));
		
		MultipleChoiceQuestionModel question2 = new MultipleChoiceQuestionModel(String.valueOf(questionID));
		question2.setPrompt(this.translator.translateMessage("question2"));
        question2.addAnswer(this.translator.translateMessage("answer2.0"), 0, this.translator.translateMessage("question2FeedbackW"));
        question2.addAnswer(this.translator.translateMessage("answer2.1"), 1, this.translator.translateMessage("question2FeedbackR"));
        question2.addAnswer(this.translator.translateMessage("answer2.2"), 0, this.translator.translateMessage("question2FeedbackW"));
        this.lang.addMCQuestion(question2);
        questionID++;
        lang.nextStep();

		// function call

		int solution = calculate(vec1, vec2, scExample, sc);

		scExample.addCodeElement("= " + solution, null, 0, null);
		lang.nextStep();
		unhighlightAll(sc);
		sc.highlight(5);
		lang.newText(new Offset(0, 10, "Example", "SW"), this.translator.translateMessage("Thesolutionis") + solution,
				"solution", null, solutionSentenceProp);
		lang.newText(new Offset(0, 40, "Example", "SW"), this.translator.translateMessage("Zusammenfassung"),
				"solution", null, solutionSentenceProp);
		node = new Node[] { new Offset(0, 5, "solution", "SW"), new Offset(0, 5, "solution", "SE") };
		this.lang.newPolyline(node, "solutionline", null, this.titleLine);
		node = new Node[] { new Offset(0, 5, "solutionline", "SW"), new Offset(0, 5, "solutionline", "SE") };
		this.lang.newPolyline(node, "solutionline2", null, this.titleLine);
		lang.nextStep(this.translator.translateMessage("Solution"));

		MultipleChoiceQuestionModel question3 = new MultipleChoiceQuestionModel(String.valueOf(questionID));
		question3.setPrompt(this.translator.translateMessage("question3"));
        question3.addAnswer(this.translator.translateMessage("answer3.0"), 0, this.translator.translateMessage("question3FeedbackW"));
        question3.addAnswer(this.translator.translateMessage("answer3.1"), 1, this.translator.translateMessage("question3FeedbackR"));
        this.lang.addMCQuestion(question3);
        questionID++;
        lang.nextStep();
		
		unhighlightAll(sc);
		unhighlightAll(scExample);
		lang.nextStep();
		

	}

	public int calculate(int vec1[], int vec2[], SourceCode example, SourceCode sourceCode) {
		int skalarprodukt = 0;
		for (int i = 0; i < vec1.length; i++) {
			skalarprodukt += vec1[i] * vec2[i];
		}
		return skalarprodukt;
	}

	public static void main(String[] args) {
		Generator generator = new Skalarprodukt("src/translationSkalarprodukt", Locale.US);
		Animal.startGeneratorWindow(generator);
	}


}
