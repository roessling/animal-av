package generators.misc;
/*
 * secretaryProblem.java
 * Viola Hofmeister, Yvonne Meuleneers, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Timing;
import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import translator.Translator;

public class SecretaryProblem implements Generator {
	private Language lang;
	private Locale LANG = Locale.GERMANY;
	private Translator t;
	
	private SourceCodeProperties sourceCode;
	private ArrayProperties array;
	private TextProperties text;
	private TextProperties endProps;
	private TextProperties headerProps;
	private SourceCodeProperties srcProps;
	private TextProperties costProps;

	private int[] a;
	private int c; 

	private IntArray intArray;
	private Text end;
	private Text header;
	private Text cost;
	private SourceCode code;
	
	// TODO Translator

	public static void main(String[] args) {
//		Language l = Language.getLanguageInstance(AnimationType.ANIMALSCRIPT, "Probabilistic Analysis", "Viola Hofmeister, Yvonne Meuleneers", 800, 600);
		Generator generator = new SecretaryProblem(Locale.GERMANY);
		Animal.startGeneratorWindow(generator);
	}

	public SecretaryProblem(Locale locale) {
		t = new Translator("resources/SecretaryProblem", locale);
	}
	
	public void init() {
		lang = new AnimalScript("Probabilistic Analysis", "Viola Hofmeister, Yvonne Meuleneers", 800, 600);
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
		lang.setInteractionType(1024);
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		sourceCode = (SourceCodeProperties) props.getPropertiesByName("sourceCode");
		c = (Integer) primitives.get("c");
		array = (ArrayProperties) props.getPropertiesByName("array");
		text = (TextProperties) props.getPropertiesByName("text");
		a = (int[]) primitives.get("intArray");
		
		setProperties();
		
		lang.setStepMode(true);
		run(a);
		return lang.toString();
	}

	public void setProperties() {
		// Cost Counter
		costProps = new TextProperties();
		costProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(151, 42, 201));
		costProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif", Font.BOLD, 20));
		
		// Header
		this.headerProps = new TextProperties();
		headerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 28));
		headerProps.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, false);

		// Text
		this.text = new TextProperties();
		text.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		text.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 20));
		
		// End
		this.endProps = new TextProperties();
		endProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif", Font.ITALIC, 40));
		endProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(122, 37, 168));

		// Source Code
		this.srcProps = new SourceCodeProperties();
		srcProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		srcProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 20));
		srcProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, new Color(194, 31, 31));
		srcProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
	}
	
	/*
	private void ask() {
		Random r = new Random();
		MultipleChoiceQuestionModel question = new MultipleChoiceQuestionModel(Integer.toString(r.nextInt(10000)));
		question.setNumberOfTries(2);
		
		question.setPrompt("Just to keep you awake - Which one might be the case that will cost us the most?");
		question.addAnswer("When the elements are sorted ascending.", Integer.toString(r.nextInt(10000)), 1, "Great!");
		question.addAnswer("When the elements are sorted descending.", Integer.toString(r.nextInt(10000)), 0, "Meh.");
		question.addAnswer("When the elements are unsorted.", Integer.toString(r.nextInt(10000)), 0, "What was the question again?");
		lang.addMCQuestion(question);
	} */
	
	public void run(int[] a) {
		cost = lang.newText(new Coordinates(250, 90), t.translateMessage("c0"), "cost", null, costProps);
		header = lang.newText(new Coordinates(50,30), t.translateMessage("h1"), "EntryHeader", null, headerProps);
		end = lang.newText(new Coordinates(200,200), t.translateMessage("end3"), "End", null, endProps);
		code = lang.newSourceCode(new Coordinates(50, 170), "hireAssistantCode", null, srcProps);
		intArray = lang.newIntArray(new Coordinates(60, 90), a, "array", null, array);
		
		// 1 6 3 4 8 2 5 10 9 7
		intArray.swap(5,1,null, null);
		// 1 2 3 4 8 6 5 10 9 7
		intArray.swap(4,6,null, null);
		// 1 2 3 4 5 6 8 10 9 7
		intArray.swap(6,9,null, null);
		// 1 2 3 4 5 6 7 10 9 8
		intArray.swap(7,9,null, null);
		// 1 2 3 4 5 6 7 8 9 10
		
		code.hide();
		intArray.hide();
		cost.hide();
		end.hide();
		
		Coordinates tc = new Coordinates(50,140);
		Coordinates hc = new Coordinates(50, 30);
		Coordinates cc = new Coordinates(50, 170); 	// code
		Coordinates c = new Coordinates(250,90);	//cost
		
		/////////////////////// Entry //////////////////////////////////
		Text explanation = lang.newText(tc, 
				t.translateMessage("e1"), 
				"EntryExplanation", null, text);
		
		lang.nextStep();
		
		explanation.hide();
		explanation = lang.newText(tc, 
				t.translateMessage("e2"), 
				"EntryExplanation", null, text);
		
		lang.nextStep();
		
		explanation.hide();
		explanation = lang.newText(tc, 
				t.translateMessage("e3"), 
				"EntryExplanation", null, text);
		
		lang.nextStep();
		
		explanation.hide();
		explanation = lang.newText(tc, 
				t.translateMessage("e4"), 
				"EntryExplanation", null, text);
		
		lang.nextStep();
		
		explanation.hide();
		explanation = lang.newText(tc, 
				t.translateMessage("e5"), 
				"EntryExplanation", null, text);
		
		lang.nextStep();
		explanation.hide();
		explanation = lang.newText(tc, 
				t.translateMessage("e6"), 
				"EntryExplanation", null, text);
				
		lang.nextStep();
		
		explanation.hide();
		explanation = lang.newText(tc, t.translateMessage("e7"), "EntryExplanation", null, text);
		
		lang.nextStep();
		
		explanation.hide();
		explanation = lang.newText(tc, 
				t.translateMessage("e8"), 
				"EntryExplanation", null, text);
		
		lang.nextStep();
		
		explanation.hide();
		explanation = lang.newText(tc, t.translateMessage("e9"), "EntryExplanation", null, text);
		
		lang.nextStep();
		
		explanation.hide();
		explanation = lang.newText(tc, t.translateMessage("e10"), "EntryExplanation", null, text);
		
		lang.nextStep("hireAssistant");
		
		/////////////////////// hireAssistant ///////////////////////////
		header.hide();
		explanation.hide();
		header = lang.newText(hc, t.translateMessage("h2"), "hireAssistantHeader", null, headerProps);
		
		code.addCodeLine("public void hireAssistant(int[] a){", null,0,null);
		code.addCodeLine("int cost = 0;", null, 1, null);
		code.addCodeLine("int hired = 0", null, 1, null);
		code.addCodeLine("for (int i = 0; i < a.length; i++) {", null, 1, null);
		code.addCodeLine("cost++;", null, 2, null);
		code.addCodeLine("if(a[i] > hired) {", null, 2, null);
		code.addCodeLine("hired = a[i];", null, 3, null);
		code.addCodeLine("cost += 2;", null, 3, null);
		code.addCodeLine("}", null, 2, null);
		code.addCodeLine("}", null, 1, null);
		code.addCodeLine("}", null, 0, null);
		// explain code 1
		explanation = lang.newText(tc,
				t.translateMessage("e11"),
				"hireAssistantExplanation", null, text);

		lang.nextStep("hireAssistant");

		explanation.hide();
		// to myself: code highlighting counts starting from 1, array highlighting starts from 0
		code.highlight(1);
		code.highlight(2);

		explanation = lang.newText(tc, t.translateMessage("e12"),
				"hireAssistantExplanation", null, text);

		lang.nextStep();
		
		code.unhighlight(1);
		code.unhighlight(2);
		explanation.hide();

		code.highlight(3);
		explanation = lang.newText(tc, t.translateMessage("e13"),
				"hireAssistantExplanation", null, text);

		lang.nextStep();

		code.unhighlight(3);
		explanation.hide();

		code.highlight(4);
		explanation = lang.newText(tc, t.translateMessage("e14"),
				"hireAssistantExplanation", null, text);

		lang.nextStep();

		code.unhighlight(4);
		explanation.hide();

		code.highlight(5);
		explanation = lang.newText(tc, t.translateMessage("e15"),
				"hireAssistantExplanation", null, text);
		
		lang.nextStep();

		code.unhighlight(5);
		explanation.hide();

		code.highlight(6);
		explanation = lang.newText(tc, t.translateMessage("e16"),
				"hireAssistantExplanation", null, text);

		lang.nextStep();

		code.unhighlight(6);
		explanation.hide();

		code.highlight(7);
		explanation = lang.newText(tc, t.translateMessage("e17"),
				"hireAssistantExplanation", null, text);

		lang.nextStep();

		lang.nextStep("array");
		
		/////////////////// explain steps on array //////////////////
		
		explanation.hide();
		code.unhighlight(7);
		header.hide();
		
		header = lang.newText(hc, t.translateMessage("h3"), "arrayExplanationHeader", null, headerProps);
		explanation = lang.newText(tc, t.translateMessage("e18"), "arrayExplanation", null, text);
				
		lang.nextStep();
		
		explanation.hide();
		explanation = lang.newText(tc, t.translateMessage("e19"), "arrayExplanation", null, text);
		
		lang.nextStep();
		
		explanation.hide();
		explanation = lang.newText(tc, t.translateMessage("e20"), "arrayExplanation", null, text);
		
		lang.nextStep();
		
		explanation.hide();
		explanation = lang.newText(tc, t.translateMessage("e21"), "arrayExplanation", null, text);
		 
		lang.nextStep();
		
		explanation.hide();
		explanation = lang.newText(tc, t.translateMessage("e22"), "arrayExplanation", null, text);
		 
		lang.nextStep();
		
		explanation.hide();
		explanation = lang.newText(tc, t.translateMessage("e23"), "arrayExplanation", null, text);
		
		lang.nextStep();
		
		explanation.hide();
		explanation = lang.newText(tc, t.translateMessage("e24"), "arrayExplanation", null, text);
		
		lang.nextStep();
		
		explanation.hide();
		intArray.show();
		explanation = lang.newText(tc, t.translateMessage("e25"), "arrayExplanation", null, text);
		
		lang.nextStep();
		
		explanation.hide();
		cost.show();
		explanation = lang.newText(tc, t.translateMessage("e26"), "arrayExplanation", null, text);
		
		lang.nextStep();
		
		explanation.hide();
		cost.hide();
		intArray.highlightCell(0, Timing.INSTANTEOUS, null);
		cost = lang.newText(c, "cost: 3", "cost", null, costProps);
		explanation = lang.newText(tc, t.translateMessage("e27"), "arrayExplanation", null, text);
		
		lang.nextStep();
		
		intArray.unhighlightCell(0, Timing.INSTANTEOUS, null);
		explanation.hide();
		cost.hide();
		intArray.highlightCell(1, Timing.INSTANTEOUS, null);
		cost = lang.newText(c, "cost: 6", "cost", null,costProps);
		explanation = lang.newText(tc, t.translateMessage("e28"), "arrayExplanation", null, text);
		
		lang.nextStep();
		
		intArray.unhighlightCell(1, Timing.INSTANTEOUS, null);
		explanation.hide();
		cost.hide();
		intArray.highlightCell(2, Timing.INSTANTEOUS,null);
		cost = lang.newText(c, "cost: 9", "cost", null,costProps);
		explanation = lang.newText(tc, t.translateMessage("e29"), "arrayExplanation", null, text);
		
		lang.nextStep();
		
		intArray.unhighlightCell(2, Timing.INSTANTEOUS, null);
		explanation.hide();
		cost.hide();
		intArray.highlightCell(3, Timing.INSTANTEOUS,null);
		cost = lang.newText(c, "cost: 12", "cost",null, costProps);
		explanation = lang.newText(tc, t.translateMessage("e30"), "arrayExplanation", null,text);
		
		lang.nextStep();
		
		intArray.unhighlightCell(3, Timing.INSTANTEOUS, null);
		cost.hide();
		intArray.highlightCell(4, Timing.INSTANTEOUS,null);
		cost = lang.newText(c, "cost: 15", "cost", null, costProps);
		
		lang.nextStep();
		
		intArray.unhighlightCell(4, Timing.INSTANTEOUS, null);
		cost.hide();
		intArray.highlightCell(5, Timing.INSTANTEOUS,null);
		cost = lang.newText(c, "cost: 18", "cost", null,costProps);
		
		lang.nextStep();
		
		intArray.unhighlightCell(5, Timing.INSTANTEOUS, null);
		cost.hide();
		intArray.highlightCell(6, Timing.INSTANTEOUS,null);
		cost = lang.newText(c, "cost: 21", "cost", null,costProps);
		
		lang.nextStep();
		
		explanation.hide();
		intArray.unhighlightCell(6, Timing.INSTANTEOUS, null);
		cost.hide();
		intArray.highlightCell(7, Timing.INSTANTEOUS,null);
		cost = lang.newText(c, "cost: 24", "cost", null, costProps);
		explanation = lang.newText(tc, t.translateMessage("e31"), "arrayExplanation", null, text);
		
		lang.nextStep();
		
		header.hide();
		code.hide();
		intArray.hide();
		cost.hide();
		explanation.hide();

		explanation = lang.newText(tc,
				t.translateMessage("e32"),
				"hireAssistantRandomizedExplanation", null, text);

		lang.nextStep("hireAssistantRandomized");
		
		///////////////////////// hireAssistantRandomized ///////////////////////// 

		// show header
		explanation.hide();
		header = lang.newText(hc, t.translateMessage("h4"), "hireAssistantRandomizedHeader", null, headerProps);
		code = lang.newSourceCode(cc, "hireAssistantRandomizedCode", null, srcProps);

		// show code
		code.addCodeLine("public void hireAssistantRandomized(int[] a) {", null, 0, null);
		code.addCodeLine("permuteBySorting(a);", null, 1, null);
		code.addCodeLine("int cost = 0;", null, 1, null);
		code.addCodeLine("int hired = a[i]", null, 1, null);
		code.addCodeLine("for (int i = 0; i < a.length; i++) {", null, 1, null);
		code.addCodeLine("cost++;", null, 2, null);
		code.addCodeLine("if(a[i] > hired) {", null, 2, null);
		code.addCodeLine("hired = a[i];", null, 3, null);
		code.addCodeLine("cost += 2;", null, 3, null);
		code.addCodeLine("}", null, 3, null);
		code.addCodeLine("}", null, 2, null);
		code.addCodeLine("}", null, 0, null);

		explanation = lang.newText(tc, 
				t.translateMessage("e33"), 
				"hireAssistantRandomizedExplanation", null, text);
		
		lang.nextStep();

		explanation.hide();
		code.highlight(1);
		explanation = lang.newText(tc, t.translateMessage("e34"), "hireAssistantRandomizedExplanation", null, text);
		
		lang.nextStep();
		
		explanation.hide();
		code.unhighlight(1);
		code.highlight(2);
		code.highlight(3);
		code.highlight(4);
		code.highlight(5);
		code.highlight(6);
		code.highlight(7);
		code.highlight(8);
		explanation = lang.newText(tc, t.translateMessage("e35"),
				"hireAssistantRandomizedExplanation1", null, text);

		lang.nextStep();
		
		explanation.hide();
		code.unhighlight(2);
		code.unhighlight(3);
		code.unhighlight(4);
		code.unhighlight(5);
		code.unhighlight(6);
		code.unhighlight(7);
		code.unhighlight(8);
		explanation = lang.newText(tc, t.translateMessage("e36"), "hireAssistantRandomizedExplanation", null, text);

		lang.nextStep("permuteBySorting");
		
		/////////////////////////////// permuteBySorting //////////////////////////////////////////////////////////////////
		
		header.hide();
		code.hide();
		explanation.hide();
		
		explanation = lang.newText(tc, t.translateMessage("e37"), "permuteBySortingExplanation",null, text);
				
		lang.nextStep();
		
		explanation.hide();
		explanation = lang.newText(tc, t.translateMessage("e38"), "permuteBySortingExplanation",null, text);
		
		lang.nextStep();
		
		explanation.hide();
		header = lang.newText(hc, t.translateMessage("h5"), "permuteBySortingHeader", null,
				headerProps);
		code = lang.newSourceCode(cc, "permuteBySortingCode", null, srcProps);

		code.addCodeLine("public void permuteBySorting(int[] a) {", null, 0, null);
		code.addCodeLine("Random r = new Random();", null, 1, null);
		code.addCodeLine("int n = a.length;", null, 1, null);
		code.addCodeLine("int[] b = new int[n];", null, 1, null);
		code.addCodeLine("for (int i = 0; i < n; i++) {", null, 1, null);
		code.addCodeLine("int rand = r.nextInt(n*n*n + 1);", null, 2, null);
		code.addCodeLine("b[i] = rand;", null, 2, null);
		code.addCodeLine("}", null, 1, null);
		code.addCodeLine("sortUsingPriorities(a, b);", null, 2, null);
		code.addCodeLine("}", null, 0, null);
		
		explanation = lang.newText(tc,
				t.translateMessage("e39"), "permuteBySortingExplanation", null, text);

		lang.nextStep();

		explanation.hide();
		code.highlight(1);
		explanation = lang.newText(tc, t.translateMessage("e40"),
				"permuteBySortingExplanation", null, text);

		lang.nextStep();
		
		explanation.hide();
		explanation = lang.newText(tc, t.translateMessage("e41"),
				"permuteBySortingExplanation", null, text);
		
		lang.nextStep();

		explanation.hide();
		code.unhighlight(1);
		code.highlight(2);
		code.highlight(3);
		explanation = lang.newText(tc, t.translateMessage("e42"),
				"permuteBySortingExplanation", null, text);

		lang.nextStep();

		explanation.hide();
		code.unhighlight(2);
		code.unhighlight(3);
		code.highlight(4);
		explanation = lang.newText(tc, t.translateMessage("e43"),
				"permuteBySortingExplanation", null, text);

		lang.nextStep();

		explanation.hide();
		code.unhighlight(4);
		code.highlight(5);
		explanation = lang.newText(tc, t.translateMessage("e44"),
				"permuteBySortingExplanation", null, text);

		lang.nextStep();
		
		explanation.hide();
		explanation = lang.newText(tc, t.translateMessage("e45"),
				"permuteBySortingExplanation", null, text);
		
		lang.nextStep();
		
		explanation.hide();
		explanation = lang.newText(tc, t.translateMessage("e46"),
				"permuteBySortingExplanation", null, text); 
		
		lang.nextStep();
		
		explanation.hide();
		explanation = lang.newText(tc, t.translateMessage("e47"),
				"permuteBySortingExplanation", null, text); 
		
		lang.nextStep();

		explanation.hide();
		explanation = lang.newText(tc, t.translateMessage("e48"),
				"permuteBySortingExplanation", null, text);

		lang.nextStep();

		explanation.hide();
		code.unhighlight(5);
		code.highlight(6);
		explanation = lang.newText(tc, t.translateMessage("e49"), "permuteBySortingExplanation", null, text);
		
		lang.nextStep();
		
		explanation.hide();
		code.unhighlight(6);
		code.highlight(8);
		explanation = lang.newText(tc, t.translateMessage("e50"),
				"permuteBySortingExplanation", null, text);

		lang.nextStep();
		
		explanation.hide();
		explanation = lang.newText(tc, t.translateMessage("e51"), "permuteBySortingExpanation", null, text);
		
		lang.nextStep();
		
		explanation.hide();
		explanation = lang.newText(tc, t.translateMessage("e52"), "permuteBySortingExpanation", null, text);
		
		lang.nextStep("ArrayExample2");
		
		code.unhighlight(8);
		explanation.hide();
		header.hide();
		
		////////////////////////////////// randomized Array Example ////////////////////////////////

		header = lang.newText(hc, t.translateMessage("h6"), "ArrayExplanation2Header", null, headerProps);
		// 1 6 3 4 8 2 5 10 9 7
		IntArray a2 = lang.newIntArray(new Coordinates(60, 90), a, "array", null, array);
		
		explanation= lang.newText(tc, t.translateMessage("e53"), "Array2Explanation", null, text);
		
		lang.nextStep();
		
		explanation.hide();
		a2.highlightCell(0, Timing.INSTANTEOUS, null);
		cost = lang.newText(c, "cost: 3", "cost2", null, costProps);
		explanation = lang.newText(tc, t.translateMessage("e54"), "Array2Explanation", null, text);
		
		lang.nextStep();
		
		explanation.hide();
		cost.hide();
		a2.unhighlightCell(0, Timing.INSTANTEOUS, null);
		a2.highlightCell(1, Timing.INSTANTEOUS, null);
		cost = lang.newText(c, "cost: 6", "cost2", null, costProps);
		explanation = lang.newText(tc, t.translateMessage("e55"), "Array2Explanation", null, text);
		
		lang.nextStep();
		
		explanation.hide();
		cost.hide();
		a2.unhighlightCell(1, Timing.INSTANTEOUS, null);
		a2.highlightCell(2, Timing.INSTANTEOUS, null);
		cost = lang.newText(c, "cost: 7", "cost2", null, costProps);
		explanation = lang.newText(tc, t.translateMessage("e56"), "Array2Explanation", null, text);
		
		lang.nextStep();
		
		cost.hide();
		a2.unhighlightCell(2, Timing.INSTANTEOUS, null);
		cost = lang.newText(c, "cost: 8", "cost2", null, costProps);
		a2.highlightCell(3, Timing.INSTANTEOUS, null);
		
		lang.nextStep();
		
		explanation.hide();
		cost.hide();
		a2.unhighlightCell(3, Timing.INSTANTEOUS, null);
		a2.highlightCell(4, Timing.INSTANTEOUS, null);
		cost = lang.newText(c, "cost: 11", "cost2", null, costProps);
		explanation = lang.newText(tc, t.translateMessage("e57"), "Array2Explanation", null, text);
		
		lang.nextStep();
		
		explanation.hide();
		cost.hide();
		a2.unhighlightCell(4, Timing.INSTANTEOUS, null);
		a2.highlightCell(5, Timing.INSTANTEOUS, null);
		cost = lang.newText(c, "cost: 12", "cost2", null, costProps);
		explanation = lang.newText(tc, t.translateMessage("e58"), "Array2Explanation", null, text);
		
		lang.nextStep();
		
		cost.hide();
		a2.unhighlightCell(5, Timing.INSTANTEOUS, null);
		a2.highlightCell(6, Timing.INSTANTEOUS, null);
		cost = lang.newText(c, "cost: 13", "cost2", null, costProps);
		
		lang.nextStep();
		
		explanation.hide();
		cost.hide();
		a2.unhighlightCell(6, Timing.INSTANTEOUS, null);
		a2.highlightCell(7, Timing.INSTANTEOUS, null);
		cost = lang.newText(c, "cost: 16", "cost2", null, costProps);
		explanation = lang.newText(tc, t.translateMessage("e59"), "Array2Explanation", null, text);
		
		lang.nextStep();
		
		explanation.hide();
		a2.unhighlightCell(7, Timing.INSTANTEOUS, null);
		explanation = lang.newText(tc, t.translateMessage("e60"), "Array2Explanation", null, text);
		
		lang.nextStep();
		
		explanation.hide();
		explanation = lang.newText(tc, t.translateMessage("e61"), "Array2Explanation", null, text);
		
		lang.nextStep();
		
		explanation.hide();
		explanation = lang.newText(tc, t.translateMessage("e62"), "Array2Explanation", null, text);
		
		lang.nextStep();
		
		explanation.hide();
		explanation = lang.newText(tc, t.translateMessage("e63"), "Array2Explanation", null, text);
		
		lang.nextStep("randomizeInPlace");
		
		////////////////////////////////// randomizeInPlace ///////////////////////////////////////
		explanation.hide();
		a2.hide();
		header.hide();
		code.hide();
		cost.hide();
		
		header = lang.newText(hc, t.translateMessage("h7"), "randomizeInPlaceHeader", null,
				headerProps);
		code = lang.newSourceCode(cc, "randomizeInPlaceCode", null, srcProps);
		
		code.addCodeLine("public void randomizeInPlace(int[] a) {", null, 0, null);
		code.addCodeLine("Random r = new Random();", null, 1, null);
		code.addCodeLine("int n = a.length;", null,1,null);
		code.addCodeLine("for (int i = 0; i < n; i++) {",null,1,null);
		code.addCodeLine("int randI = r.nextInt((((n - 1) - i) + 1) + i);", null,2,null);
		code.addCodeLine("int rand = a[randI];", null,2,null);
		code.addCodeLine("int temp = a[i];", null,2,null);
		code.addCodeLine("a[i] = rand;",null,2,null);
		code.addCodeLine("a[randI] = temp;",null,2,null);
		code.addCodeLine("}", null, 1,null);
		code.addCodeLine("[...]", null, 1, null);
		code.addCodeLine("}", null, 0, null);
		
		explanation = lang.newText(tc,
				t.translateMessage("e64"), "RandomizeInPlaceExplanation", null, text);

		lang.nextStep();

		explanation.hide();
		explanation = lang.newText(tc,
				t.translateMessage("e65"), "RandomizeInPlaceExplanation", null, text);
		
		lang.nextStep();

		explanation.hide();
		code.highlight(1);
		explanation = lang.newText(tc,
				t.translateMessage("e66"), "RandomizeInPlaceExplanation", null, text);
		
		
		lang.nextStep();

		explanation.hide();
		code.unhighlight(1);
		code.highlight(3);
		explanation = lang.newText(tc,
				t.translateMessage("e67"), "RandomizeInPlaceExplanation", null, text);
		
		lang.nextStep();

		explanation.hide();
		code.unhighlight(3);
		code.highlight(4);
		explanation = lang.newText(tc,
				t.translateMessage("e68"), "RandomizeInPlaceExplanation", null, text);
		
		lang.nextStep();
		
		explanation.hide();
		explanation = lang.newText(tc, t.translateMessage("e69"), "RandomizeInPlaceExplanation", null, text);
		
		lang.nextStep();
		
		explanation.hide();
		explanation = lang.newText(tc, t.translateMessage("e70"), "RandomizeInPlaceExplanation", null, text);
		
		lang.nextStep();
		
		explanation.hide();
		code.unhighlight(4);
		code.highlight(5);
		code.highlight(6);
		code.highlight(6);
		explanation = lang.newText(tc, t.translateMessage("e71"), "RandomizeInPlaceExplanation", null,text);
		
		lang.nextStep();
		
		explanation.hide();
		code.unhighlight(5);
		code.unhighlight(6);
		code.unhighlight(7);
		code.highlight(9);
		explanation = lang.newText(tc, t.translateMessage("e72"),
				"RandomizeInPlaceExplanation", null, text);

		lang.nextStep("End");
		
		explanation.hide();
		explanation = lang.newText(tc, t.translateMessage("e73"), "RandomizeInPlaceExplanation", null, text);
		
		////////////////////////////// END //////////////////////////////////
		
		lang.nextStep("End");
		
		header.hide();
		code.hide();
		explanation.hide();
		explanation = lang.newText(tc, t.translateMessage("end1"), "End", null, text);
		
		lang.nextStep();
		
		explanation.hide();
		explanation = lang.newText(tc,  t.translateMessage("end2"), "End", null, text);
		
		lang.nextStep();
		
		explanation.hide();
		end.show();
	}

	public String getName() {
		return "Probabilistic Analysis";
	}

	public String getAlgorithmName() {
		return "Secretary Problem, randomized";
	}

	public String getAnimationAuthor() {
		return "Viola Hofmeister, Yvonne Meuleneers";
	}

	public String getDescription() {
		return "Imagine, you had to hire a new assistant at your working place. An agency  is about to help you find the "
				+ "\n"
				+ "candidate who will make the grade. Each interview costs you 1 credit, and each time you hire someone"
				+ "\n"
				+ "costs 2 since the agency does not do the job for free. Every time a new best candidate is found, "
				+ "\n" + "you fire the current secretary and hire the new one. Since you still want to keep some money,"
				+ "\n"
				+ "you now gnerate a randomized list of your candidates so that your chance is high to always pay"
				+ "\n" + "the same price when it is time to hire assistants again.";
	}

	public String getCodeExample() {
		return "public void hireAssistantRandomized(int[] a) {" + "\n" + "	permuteBySorting(a);" + "\n"
				+ "	/* The grade of qualification is represented as whole numbers between 1 and 10." + "\n"
				+ "	*  For this, we start with a dummy rated with 0 to start comparing. " + "\n" + "	*/" + "\n"
				+ "	int hired = 0; " + "\n" + "	for (int i = 0; i < a.length; i++) {" + "\n"
				+ "		cost++; // interview candidate i, costs 1 credit" + "\n" + "		if (a[i] > hired) {" + "\n"
				+ "			hired = a[i]; // hire candidate i" + "\n"
				+ "			cost += 2; // hiring costs 2 credits" + "\n" + "		}" + "\n" + "	}" + "\n" + "}" + "\n";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return LANG;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
	}

	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}
}