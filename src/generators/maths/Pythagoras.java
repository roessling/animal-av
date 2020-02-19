package generators.maths;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
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
import interactionsupport.models.MultipleChoiceQuestionModel;
import translator.Translator;

/**
 * @author Romal Bijan, Luisa Dyroff
 * @version 0.1 03.06.2019
 */

public class Pythagoras implements ValidatingGenerator {


    private Translator translator;
    private Language lang;
    private SourceCode sc;
    
    private double a;
    private double b;
    private double c;
    private boolean wantToCalculateHypotenuse; 
    
    private SourceCodeProperties scExampleProp;
    private SourceCodeProperties scSourceCodeProp;
	private SourceCode scExample;
    private TextProperties headerProp;
    private TextProperties descriptionProp;
    private TextProperties exampleHeaderProp;
    private TextProperties zahlenExample; 
    private TextProperties result; 
    private TextProperties solutionSentenceProp;
    private TextProperties formulaProp;
    private PolylineProperties titleLine;
 
    private PolylineProperties kathete1;
    private PolylineProperties kathete2;
    private PolylineProperties hypotenuse;
    
    private int questionID = 0;
    //Properties

    Text description;
    Text returnDesc;
    Text returnOut;
    Text returnOut2;

    // Constants
    private static final String NAME ="NAME";
    private static final String TITLE = "Pythagoras";
    private static final String DESCRIPTION = "beschreibungsText";
    private static final String RETURN = "RETURN";
    private static final String RETURN2 = "RETURN2";
    private static final String ALGOBESCH = "algorithmenbeschreibung";
    private static final String DESCRIPTION_ALGO = "description_algo";
    

    public Pythagoras(String resource, Locale locale) {
        this.translator = new Translator(resource, locale);
        this.lang = new AnimalScript(this.translator.translateMessage(TITLE), "Romal Bijan, Luisa Dyroff", 800, 600);
        this.lang.setStepMode(true);
		this.lang.setInteractionType(1024);
    }

    public void init() {
        lang = new AnimalScript("Satz des Pythagoras", "Romal Bijan, Luisa Dyroff", 800, 600);
        lang.setStepMode(true);
		this.lang.setInteractionType(1024);
    }


    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
        a = (double) primitives.get("a");
        b = (double) primitives.get("b");
        wantToCalculateHypotenuse = (boolean) primitives.get("wantToCalculateHypotenuse");
        
        
        
        
        scExampleProp = (SourceCodeProperties) props.getPropertiesByName("scExampleProp");
        scSourceCodeProp = (SourceCodeProperties) props.getPropertiesByName("scSourceCodeProp");
        headerProp = (TextProperties) props.getPropertiesByName("headerProp");
        descriptionProp = (TextProperties) props.getPropertiesByName("descriptionProp");
        exampleHeaderProp = (TextProperties) props.getPropertiesByName("exampleHeaderProp");
    	formulaProp = (TextProperties) props.getPropertiesByName("formulaProp");
      
        titleLine = (PolylineProperties)props.getPropertiesByName("titleLine");
        kathete1= (PolylineProperties)props.getPropertiesByName("kathete1");
        kathete2= (PolylineProperties)props.getPropertiesByName("kathete2");
        hypotenuse= (PolylineProperties)props.getPropertiesByName("hypotenuse");
        
        solutionSentenceProp = (TextProperties) props.getPropertiesByName("solutionSentenceProp");
        start(a,b, wantToCalculateHypotenuse);
        
        
        this.lang.finalizeGeneration();
        return lang.toString();
    }
    
    public String displayNumber(double a) {
    	return a>0? ""+a : "("+a+")";
    }
	private void unhighlightAll(SourceCode sc) {
		for (int i = 0; i < sc.length(); i++) {
			sc.unhighlight(i);
		}
	}
    public void start(double a, double b, boolean calculateHypotenuse) {

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
		returnOut = lang.newText(new Coordinates(10, 75), this.translator.translateMessage(RETURN), "Beschreibung",
				null, descriptionProp);

		lang.nextStep(this.translator.translateMessage("Beschreibung"));

		// Hide description from view
		description.hide();
		returnOut.hide();


		// Properties for the source code

		// source code
		sc = lang.newSourceCode(new Coordinates(10, 50), "sourceCode", null, scSourceCodeProp);

		sc.addCodeLine(" public double calculate(double a, double b, boolean wantToCalculateHypotenuse){", null, 0,
				null); // 0
		sc.addCodeLine("if (wantToCalculateHypotenuse) { ", null, 1, null); // 1
		sc.addCodeLine("double hypoSquare = Math.pow(a,2) + Math.pow(b,2);", null, 2, null); // 2
		sc.addCodeLine("return Math.sqrt(hypoSquare);", null, 2, null); // 3
		sc.addCodeLine("}", null, 1, null); // 4
		sc.addCodeLine("// want to calculate cathete", null, 1, null); // 5
		sc.addCodeLine("if (a > b ) { // a is hypotenuse", null, 1, null); // 6
		sc.addCodeLine("double kateteSquare = Math.pow(a,2) - Math.pow(b,2);", null, 2, null); // 7
		sc.addCodeLine("return Math.sqrt(kateteSquare);", null, 2, null); // 8
		sc.addCodeLine("}", null, 1, null); // 9
		sc.addCodeLine("// b is hypotenuse", null, 1, null); // 10
		sc.addCodeLine("double kateteSquare = Math.pow(b,2) - Math.pow(a,2);", null, 1, null); // 11
		sc.addCodeLine("return Math.sqrt(kateteSquare);", null, 1, null); // 12
		sc.addCodeLine("}", null, 0, null); // 13
	
		lang.nextStep(this.translator.translateMessage("SourceCode"));
		
		MultipleChoiceQuestionModel question2 = new MultipleChoiceQuestionModel(String.valueOf(questionID));
		question2.setPrompt(this.translator.translateMessage("question2"));
        question2.addAnswer(this.translator.translateMessage("answer2.0"), 1, this.translator.translateMessage("question2FeedbackR"));
        question2.addAnswer(this.translator.translateMessage("answer2.1"), 0, this.translator.translateMessage("question2FeedbackW"));
        question2.addAnswer(this.translator.translateMessage("answer2.2"), 0, this.translator.translateMessage("question2FeedbackW"));
        this.lang.addMCQuestion(question2);
        questionID++;
        lang.nextStep();
        
		// Example header

		lang.newText(new Offset(70, 0, "sourceCode", "NE"), this.translator.translateMessage("Beispiel") + " :",
				"Beispiel", null, exampleHeaderProp);

		
		
        
		// Example
		String tmpA = displayNumber(a);
		String tmpB = displayNumber(b);

		// function call
		double c = calculate(a, b, calculateHypotenuse);
		
		scExample = lang.newSourceCode(new Offset(30, 0, "Beispiel", "SW"), "Example", null, scExampleProp);
		scExample.addCodeLine(this.translator.translateMessage("Formel")+":", null, 0, null);
		sc.highlight(0);
		lang.nextStep();
		sc.unhighlight(0);
		if(wantToCalculateHypotenuse){
			sc.highlight(1);
			scExample.addCodeElement("a² + b² = c²", null, 0, null); 
			lang.nextStep();
			sc.unhighlight(1);
			sc.highlight(2);
			scExample.addCodeElement("=> c= √(a²+b²)", null, 0, null);
			lang.nextStep();
			scExample.addCodeLine(this.translator.translateMessage("eingesetzt")+": a="+tmpA+ "; b="+tmpB, null, 0, null);
			lang.nextStep();
			scExample.addCodeElement("=> c = √("+tmpA+"²"+"+" +tmpB+"²)", null, 0, null);
			lang.nextStep(this.translator.translateMessage("eingesetzt"));
			unhighlightAll(sc);
			sc.highlight(3);
		}else {
			sc.highlight(5);
			lang.nextStep();
			sc.unhighlight(5);
			if(a>b) {
				sc.highlight(6);
				lang.nextStep();
				scExample.addCodeElement("c² = a² - b²", null, 0, null);
				scExample.addCodeElement("=> c = √(a²-b²)", null, 0, null);
				sc.unhighlight(6);
				lang.nextStep();
				sc.highlight(7);
				lang.nextStep();
				scExample.addCodeLine(this.translator.translateMessage("eingesetzt")+": a="+a + "; b="+b, null, 0, null);
				lang.nextStep();
				scExample.addCodeElement("=> c = √("+tmpA+"²"+"-"+tmpB+"²)", null, 0, null);
				lang.nextStep(this.translator.translateMessage("eingesetzt"));
				unhighlightAll(sc);
				sc.highlight(8);
			}else {
				sc.highlight(10);
				lang.nextStep();
				scExample.addCodeElement("c² = b² - a²", null, 0, null); 
				scExample.addCodeElement("=> c= √(b²-a²)", null, 0, null);
				sc.unhighlight(10);
				lang.nextStep();
				sc.highlight(11);
				lang.nextStep();
				scExample.addCodeLine(this.translator.translateMessage("eingesetzt")+": a="+a + "; b="+b, null, 0, null);
				lang.nextStep();
				scExample.addCodeElement("=> c = √("+tmpB+"²"+"-"+tmpA+"²)", null, 0, null);
				lang.nextStep(this.translator.translateMessage("eingesetzt"));
				unhighlightAll(sc);
				sc.highlight(12);
			}
		}
		

		MultipleChoiceQuestionModel question3 = new MultipleChoiceQuestionModel(String.valueOf(questionID));
		question3.setPrompt(this.translator.translateMessage("question3"));
        question3.addAnswer(this.translator.translateMessage("answer3.0"), 1, this.translator.translateMessage("question3FeedbackR"));
        question3.addAnswer(this.translator.translateMessage("answer3.1"), 0, this.translator.translateMessage("question3FeedbackW"));
        this.lang.addMCQuestion(question3);
        questionID++;
        lang.nextStep();
		
		lang.newText(new Offset(0, 10, "Example", "SW"), this.translator.translateMessage("DREIECK") + ":",
				"DREICKE", null, solutionSentenceProp);
		lang.nextStep();
		// create trinangle 
		// whichone is the hypotenuse ?
		double tmpc= a>=b ? (a>=c? a:c):(b>=c? b:c);
		Node[] node1 = new Node[] { new Offset(0, 35, "DREICKE", "SW"), new Offset((int)a*10, 35, "DREICKE", "SW") };
		this.lang.newPolyline(node1, "Dreieck1", null, this.kathete1);
		Node[] node2 = new Node[] {new Offset(0, 0, "Dreieck1","NW"), new Offset(0, (int)b*10, "Dreieck1", "NW") };
		this.lang.newPolyline(node2, "Dreieck2", null, this.kathete2);
		Node[] node3 = new Node[] {new Offset(0, 0, "Dreieck2","SW"), new Offset((int) tmpc*10, 0, "Dreieck1", "NW") };
		this.lang.newPolyline(node3, "Dreieck3", null, this.hypotenuse); 
		//c= a>=b ? (a>=c? a:c):(b>=c? b:c);
		//beschriftung 
		lang.newText(new Offset(0, 10, "DREICKE", "NE"), "a ="+ displayNumber(a)+", b ="+displayNumber(b)+", c="+displayNumber((Math.round(c*100.0))/100.0),
				"a", null, solutionSentenceProp);
		lang.nextStep(this.translator.translateMessage("DREIECK"));
		
		
		lang.newText(new Offset(0, 10, "Dreieck2", "SW"), this.translator.translateMessage("Thesolutionis") + displayNumber((Math.round(c*100.0))/100.0),
				"solution", null, solutionSentenceProp);
		node = new Node[] { new Offset(0, 5, "solution", "SW"), new Offset(0, 5, "solution", "SE") };
		this.lang.newPolyline(node, "solutionline", null, this.titleLine);
//		node = new Node[] { new Offset(0, 5, "solutionline", "SW"), new Offset(0, 5, "solutionline", "SE") };
//		this.lang.newPolyline(node, "solutionline2", null, this.titleLine);
		lang.nextStep(this.translator.translateMessage("Solution"));
		lang.newText(new Offset(0, 10, "solutionline", "SW"), this.translator.translateMessage("zusammenfassung"), "solution", null, solutionSentenceProp);
		unhighlightAll(sc);

		lang.nextStep(this.translator.translateMessage("Solution"));
		
		unhighlightAll(sc);
		unhighlightAll(scExample);
		lang.nextStep();

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
        return this.translator.translateMessage(ALGOBESCH);
    }
    public double calculate(double a, double b, boolean isHypothenuse){ 
    	
    	if(isHypothenuse) {
    		   double hypoSquare = Math.pow(a,2) + Math.pow(b,2);
    	       return Math.sqrt(hypoSquare);
    	}
    	if(a> b ) {
    			double kateteSquare = Math.pow(a,2) - Math.pow(b,2);
    	        return Math.sqrt(kateteSquare);
    	}
    	double kateteSquare = Math.pow(b,2) - Math.pow(a,2);
    	return Math.sqrt(kateteSquare);
    
    	}
     
    
    public static void main(String[] args) {
		Generator generator =  new Pythagoras("Pythagoras/translationPythagoras", Locale.US);
		Animal.startGeneratorWindow(generator);
    }

	@Override
	public String getCodeExample() {
		// TODO Auto-generated method stub
		return "public int calculate(double a, double b, boolean wantToCalculateHypotenuse) {"
				+ "\n 	if (wantToCalculateHypotenuse) {"
				+ "\n		 double hypoSquare = Math.pow(a,2) + Math.pow(b,2);"
				+ "\n		 return Math.sqrt(hypoSquare);"
				+ "\n } "
				+ "\n // want to calculate cathete"
				+ "\n	if (a > b ) { // a is hypotenuse"
				+ "\n		double kateteSquare = Math.pow(a,2) - Math.pow(b,2);"
				+ "\n		return Math.sqrt(kateteSquare);"
				+ "\n }"
				+ "\n // b is hypotenuse"
				+ "\n 	double kateteSquare = Math.pow(b,2) - Math.pow(a,2);"
				+ "\n	return Math.sqrt(kateteSquare);"
				+ "\n }";
	}
	@Override
	public Locale getContentLocale() {
		// TODO Auto-generated method stub
		return translator.getCurrentLocale();
	}

	@Override
	public String getFileExtension() {
		// TODO Auto-generated method stub
		return "asu";
	}

	@Override
	public GeneratorType getGeneratorType() {
		// TODO Auto-generated method stub
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
	}

	@Override
	public String getOutputLanguage() {
		// TODO Auto-generated method stub
		return Generator.JAVA_OUTPUT;
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer arg0, Hashtable<String, Object> arg1)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return true;
	}




}

