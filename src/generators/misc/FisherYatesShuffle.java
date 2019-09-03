package generators.misc;

import java.util.Locale;
import java.util.Hashtable;
import java.util.Random;
import java.awt.Font;
import java.awt.Color;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;
import algoanim.primitives.IntArray;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.AnimalVariablesGenerator;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.TextProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import algoanim.util.Offset;
import interactionsupport.models.QuestionGroupModel;
import interactionsupport.models.MultipleChoiceQuestionModel;

public class FisherYatesShuffle implements Generator {
	private Language lang;
	private SourceCodeProperties sourceCode;
	private ArrayMarkerProperties jMarker;
	private int[] intArray;
	private ArrayMarkerProperties iMarker;
	private ArrayProperties array;
	private boolean quiz;
	// iTextProps
	private Color iColor;
	private Font iFont;
	private int iSize;
	private boolean iBold;
	private boolean iItalic;
	private boolean iHidden;
	// jTextProps
	private Color jColor;
	private Font jFont;
	private int jSize;
	private boolean jBold;
	private boolean jItalic;
	private boolean jHidden;
	// IntroOutroTextProps
	private Color introColor;
	private Font introFont;
	private int introSize;
	private boolean introBold;
	private boolean introItalic;
	// TitleTextProps
	private Color titleColor;
	private Color titleBoxColor;
	private Font titleFont;
	private int titleSize;
	private boolean titleBold;
	private boolean titleItalic;

	public void init() {
		lang = new AnimalScript("Fisher Yates Shuffle [EN]", "Jan Wiesel",
				1024, 768);
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
		lang.setStepMode(true);
	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		sourceCode = (SourceCodeProperties) props
				.getPropertiesByName("sourceCode");
		jMarker = (ArrayMarkerProperties) props.getPropertiesByName("jMarker");
		intArray = (int[]) primitives.get("intArray");
		iMarker = (ArrayMarkerProperties) props.getPropertiesByName("iMarker");
		array = (ArrayProperties) props.getPropertiesByName("array");
		quiz = (Boolean) primitives.get("quiz");

		// iTextPrimitives
		iColor = (Color) primitives.get("iTextColor");
		iFont = (Font) primitives.get("iTextFont");
		iSize = (Integer) primitives.get("iTextSize");
		iBold = (Boolean) primitives.get("iTextBold");
		iItalic = (Boolean) primitives.get("iTextItalic");
		iHidden = (Boolean) primitives.get("iTextHidden");

		// jTextPrimitives
		jColor = (Color) primitives.get("jTextColor");
		jFont = (Font) primitives.get("jTextFont");
		jSize = (Integer) primitives.get("jTextSize");
		jBold = (Boolean) primitives.get("jTextBold");
		jItalic = (Boolean) primitives.get("jTextItalic");
		jHidden = (Boolean) primitives.get("jTextHidden");

		// IntroOutroTextPrimitives
		introColor = (Color) primitives.get("textColor");
		introFont = (Font) primitives.get("textFont");
		introSize = (Integer) primitives.get("textSize");
		introBold = (Boolean) primitives.get("textBold");
		introItalic = (Boolean) primitives.get("textItalic");

		// TitleTextPrimitives
		titleColor = (Color) primitives.get("titleColor");
		titleBoxColor = (Color) primitives.get("titleBoxColor");
		titleFont = (Font) primitives.get("titleFont");
		titleSize = (Integer) primitives.get("titleSize");
		titleBold = (Boolean) primitives.get("titleBold");
		titleItalic = (Boolean) primitives.get("titleItalic");

		// Do the algorithm
		shuffle(intArray);

		lang.finalizeGeneration();
		return lang.toString();
	}

	/**
	 * Shuffle the given array using the Fisher-Yates algorithm
	 * 
	 * @param a
	 *            the array to be shuffled
	 */
	@SuppressWarnings("unused")
	public void shuffle(int[] a) {
		// Title
		// Title Text
		TextProperties titleProps = new TextProperties();
		titleProps.set("depth", 5);
		int titleStyle = 0;
		if (titleBold)
			titleStyle += Font.BOLD;
		if (titleItalic)
			titleStyle += Font.ITALIC;
		titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				titleFont.getName(), titleStyle, titleSize));
		titleProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, titleColor);
		Text title = lang.newText(new Coordinates(40, 40),
				"Fisher Yates Shuffle", "title", null, titleProps);
		// Title Box
		RectProperties rectProps = new RectProperties();
		rectProps.set("fillColor", titleBoxColor);
		rectProps.set("filled", true);
		rectProps.set("depth", 10);
		lang.newRect(new Offset(-5, -5, title, AnimalScript.DIRECTION_NW),
				new Offset(5, 5, title, AnimalScript.DIRECTION_SE), "titlebox",
				null, rectProps);
		lang.nextStep();

		// Intro Text
		TextProperties textProps = new TextProperties();
		int introStyle = 0;
		if (introBold)
			introStyle += Font.BOLD;
		if (introItalic)
			introStyle += Font.ITALIC;
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
				new Font(introFont.getName(), introStyle, introSize));
		textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, introColor);
		Text intro1 = lang
				.newText(
						new Offset(0, 40, title, AnimalScript.DIRECTION_SW),
						"The Fisher Yates Shuffle is an algorithm for shuffling an ordered and finite set of user defined elements (e.g. arrays).",
						"intro1", null, textProps);
		Text intro2 = lang
				.newText(
						new Offset(0, 10, intro1, AnimalScript.DIRECTION_SW),
						"The Algorithm differentiates between the shuffled part of the set (at the end) and the unshuffled part. On every step it",
						"intro2", null, textProps);
		Text intro3 = lang
				.newText(
						new Offset(0, 0, intro2, AnimalScript.DIRECTION_SW),
						"randomly chooses an element of the unshuffled part and swaps its position with the element at the end of the",
						"intro3", null, textProps);
		Text intro4 = lang
				.newText(
						new Offset(0, 0, intro3, AnimalScript.DIRECTION_SW),
						"unshuffled part. This element is henceforth considered shuffled, so that the size of the unshuffled part is decreased",
						"intro4", null, textProps);
		Text intro5 = lang
				.newText(
						new Offset(0, 0, intro4, AnimalScript.DIRECTION_SW),
						"by one on every step. This continues until every element is considered shuffled.",
						"intro5", null, textProps);
		lang.nextStep("Introduction");

		// Hide the Intro Text
		intro1.hide();
		intro2.hide();
		intro3.hide();
		intro4.hide();
		intro5.hide();

		// IntArray object with the created array properties
		IntArray ia = lang.newIntArray(new Offset(0, 80, title,
				AnimalScript.DIRECTION_SW), a, "array", null, array);
		ia.hide();

		// source code entity
		SourceCode sc = lang.newSourceCode(new Offset(0, 50, ia,
				AnimalScript.DIRECTION_SW), "sourceCode", null, sourceCode);

		// Add the lines to the SourceCode object.
		sc.addCodeLine("public void fisherYatesShuffle(int[] array) {", null,
				0, null); // 0
		sc.addCodeLine("int i, j;", null, 1, null); // 1
		sc.addCodeLine("Random rnd = new Random();", null, 1, null); // 2
		sc.addCodeLine("for (i = array.length() - 1; i >= 0 ; i--)", null, 1,
				null); // 3
		sc.addCodeLine("{", null, 1, null); // 4
		sc.addCodeLine("j = rnd.nextInt(i + 1);", null, 2, null); // 5
		sc.addCodeLine("array.swap(i, j);", null, 2, null); // 6
		sc.addCodeLine("}", null, 1, null); // 7
		sc.addCodeLine("}", null, 0, null); // 8

		lang.nextStep();

		AnimalVariablesGenerator gen = new AnimalVariablesGenerator(lang);
		String oriArray = "";
		for (int i = 0; i < a.length - 1; i++) {
			oriArray = oriArray + String.valueOf(a[i]) + ",";
		}
		oriArray = oriArray + String.valueOf(a[a.length - 1]);
		gen.declare("originalArray", oriArray);

		ia.show();

		// Timing used for several transitions
		TicksTiming tm = new TicksTiming(500);

		// Highlight line "public void fisherYatesShuffle(int[] array)"
		sc.highlight(0, 0, false);
		lang.nextStep();

		gen.declare("i", "");
		gen.declare("j", "");

		// Highlight line "int i, j;"
		sc.toggleHighlight(0, 0, false, 1, 0);
		sc.highlight(2, 0, false);

		// Create marker
		ArrayMarker im = lang.newArrayMarker(ia, 0, "iMarker", null, iMarker);
		ArrayMarker jm = lang.newArrayMarker(ia, 0, "jMarker", null, jMarker);

		// i and j Texts
		// Set the properties
		TextProperties iTextProps = new TextProperties();
		int iStyle = 0;
		if (iBold)
			iStyle += Font.BOLD;
		if (iItalic)
			iStyle += Font.ITALIC;
		iTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
				new Font(iFont.getName(), iStyle, iSize));
		iTextProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, iColor);
		TextProperties jTextProps = new TextProperties();
		int jStyle = 0;
		if (jBold)
			jStyle += Font.BOLD;
		if (jItalic)
			jStyle += Font.ITALIC;
		jTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
				new Font(jFont.getName(), jStyle, jSize));
		jTextProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, jColor);		
		// Add the texts
		Text it = lang.newText(
				new Offset(0, 20, ia, AnimalScript.DIRECTION_SW), "i:",
				"iText", null, iTextProps);
		Text jt = lang.newText(
				new Offset(100, 0, it, AnimalScript.DIRECTION_NW), "j:",
				"jText", null, jTextProps);
		if(iHidden)
			it.hide();
		if(jHidden)
			jt.hide();		
		lang.nextStep("Initialized");

		int i = 0, j = 0;
		Random rnd = new Random();

		sc.unhighlight(1, 0, false);
		sc.unhighlight(2, 0, false);

		int iteration = 1;

		if (quiz) {
			QuestionGroupModel groupInfo = new QuestionGroupModel(
					"Question group", 2);
			lang.addQuestionGroup(groupInfo);
		}

		for (i = ia.getLength() - 1; i >= 0; i--) {
			// Move iMarker
			sc.highlight(3, 0, false);
			// If there was a swap between two neighboring array elements
			// recently the iMarker needs to move to i+1 instead of i
			if (i == j)
				im.move(i + 1, null, tm);
			else
				im.move(i, null, tm);
			it.setText("i: " + i, tm, null);
			gen.update("i", String.valueOf(i));
			lang.nextStep("Iteration " + iteration);
			iteration++;

			// Move jMarker
			j = rnd.nextInt(i + 1);
			sc.toggleHighlight(3, 0, false, 5, 0);
			jm.move(j, null, tm);
			jt.setText("j: " + j, tm, null);
			gen.update("j", String.valueOf(j));
			if (quiz && i == ia.getLength() / 2)
				lang.nextStep("Quiz next");
			else
				lang.nextStep();

			if (quiz && i == ia.getLength() / 2) {
				MultipleChoiceQuestionModel mcqm = new MultipleChoiceQuestionModel(
						"jRandomQ");
				mcqm.setPrompt("On the previous step j took a position between 0 and i+1 (including i) As the chosen element will swap position with the element at position i anyway, would the result be random too, if the position of j was chosen between 0 and i (excluding i) ?");
				mcqm.setNumberOfTries(1);
				mcqm.addAnswer(
						"true",
						0,
						"Incorrect Answer! On a first look the result would seem random, but in fact it would not be completely unbiased. As an example, it would not be possible for the value that is on the last position in the Array at initialization time to stay there, as it would definitely move on the first step.");
				mcqm.addAnswer(
						"false",
						1,
						"Correct Answer! On a first look the result would seem random, but in fact it would not be completely unbiased. As an example, it would not be possible for the value that is on the last position in the Array at initialization time to stay there, as it would definitely move on the first step.");
				mcqm.setGroupID("Question group");
				lang.addMCQuestion(mcqm);
				lang.nextStep();
			}

			// Swap the array elements pointed by i and j
			ia.swap(i, j, null, tm);
			sc.toggleHighlight(5, 0, false, 6, 0);
			ia.highlightCell(i, tm, tm);
			ia.highlightElem(i, tm, tm);
			lang.nextStep();

			sc.unhighlight(6, 0, false);
		}

		// Unhighlight the background of the array
		// As "array.unhighlightCell(0, array.getLength()-1, null, null);" did
		// not work every cell has to be unhighlighted manually
		for (int k = 0; k < ia.getLength(); k++) {
			ia.unhighlightCell(k, null, tm);
		}

		// Hide the markers
		im.hide();
		jm.hide();
		it.hide();
		jt.hide();
		String shuffleArray = "";
		for (int k = 0; k < ia.getLength() - 1; k++) {
			shuffleArray = shuffleArray + String.valueOf(ia.getData(k)) + ",";
		}
		shuffleArray = shuffleArray
				+ String.valueOf(ia.getData(ia.getLength() - 1));
		gen.declare("shuffledArray", shuffleArray);
		gen.discard("j");
		gen.discard("i");
		if (quiz)
			lang.nextStep("Algorithm finished, Quiz next");
		else
			lang.nextStep("Algorithm finished");

		if (quiz) {
			MultipleChoiceQuestionModel mcqm = new MultipleChoiceQuestionModel(
					"complexityQuestion");
			mcqm.setPrompt("What is the complexity of the algorithm?");
			mcqm.setNumberOfTries(1);
			mcqm.addAnswer(
					"O(1)",
					0,
					"Incorrect. The time the algorithm needs grows linear with the size of the array to shuffle!");
			mcqm.addAnswer(
					"O(log(n))",
					0,
					"Incorrect. The time the algorithm needs grows linear with the size of the array to shuffle!");
			mcqm.addAnswer("O(n)", 1, "Correct Answer!");
			mcqm.addAnswer(
					"O(n log(n))",
					0,
					"Incorrect. The time the algorithm needs grows linear with the size of the array to shuffle!");
			mcqm.addAnswer(
					"O(n^2)",
					0,
					"Incorrect. The time the algorithm needs grows linear with the size of the array to shuffle!");
			mcqm.setGroupID("Question group");
			lang.addMCQuestion(mcqm);
			lang.nextStep();
		}

		// Hide source code and array
		sc.hide();
		ia.hide();

		// Outro Text
		Text outro1 = lang
				.newText(
						new Coordinates(40, 100),
						"This animation described the modern version of the algorithm, which swaps the elements instead of copying the",
						"outro1", null, textProps);
		Text outro2 = lang
				.newText(
						new Offset(0, 0, outro1, AnimalScript.DIRECTION_SW),
						"chosen element elsewhere. Therefore it does not need any additional data space except for identifiers i and j. The",
						"outro2", null, textProps);
		Text outro3 = lang
				.newText(
						new Offset(0, 0, outro2, AnimalScript.DIRECTION_SW),
						"algorithm has linear complexity, meaning it scales well with large input sizes, too. Given input size n it needs n",
						"outro3", null, textProps);
		Text outro4 = lang.newText(new Offset(0, 0, outro3,
				AnimalScript.DIRECTION_SW),
				"swap() and n random() operations.", "outro4", null, textProps);
		Text outro5 = lang
				.newText(
						new Offset(0, 10, outro4, AnimalScript.DIRECTION_SW),
						"If the results of the used random() method are completely unbiased the algorithm will return unbiased results as",
						"outro5", null, textProps);
		Text outro6 = lang.newText(new Offset(0, 0, outro5,
				AnimalScript.DIRECTION_SW), "well.", "outro6", null, textProps);
		lang.nextStep("Conclusion");
	}

	public String getName() {
		return "Fisher Yates Shuffle [EN]";
	}

	public String getAlgorithmName() {
		return "Fisher Yates Shuffle";
	}

	public String getAnimationAuthor() {
		return "Jan Wiesel";
	}

	public String getDescription() {
		return "The Fisher Yates Shuffle is a shuffling algorithm for arrays and other ordered and finite structures of user defined elements."
				+ "\n"
				+ "Please note, that this algorithm uses a time seeded random number generator. Therefore the animation will be slightly different every time it is generated.";
	}

	public String getCodeExample() {
		return "public void fisherYatesShuffle(int[] array)" + "\n" + "{"
				+ "\n" + "   int i, j;" + "\n"
				+ "   Random rnd = new Random();" + "\n"
				+ "   for (i = array.length() - 1; i >= 0 ; i--)" + "\n"
				+ "   {" + "\n" + "      j = rnd.nextInt(i + 1);" + "\n"
				+ "      array.swap(i, j);" + "\n" + "   }" + "\n" + "}";
	}

	public String getFileExtension() {
		return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	public Locale getContentLocale() {
		return Locale.US;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
	}

	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

}