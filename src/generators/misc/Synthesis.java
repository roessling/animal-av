package generators.misc;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;
import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleSelectionQuestionModel;
import interactionsupport.models.TrueFalseQuestionModel;
import translator.Translator;

/**
 * Synthesis algorithm
 * 
 * @author Noémie Catherine Hélène Spiller, Philipp Grenz
 * 
 */
public class Synthesis implements ValidatingGenerator {

	private static String[] fd;

	private final String exampleCode = "fd := functional dependencies\nkey := key of fd\n\n"
			+ "synthesis(fd) {\n  reduce(fd)\n  relations = formRelations(fd)\n  eliminate(relations)\n"
			+ "  addKey(relations)\n  return relations\n}\nreduce(fd) {\n  leftReduce(fd)\n  rightReduce(fd)\n"
			+ "  group(fd)\n}\nleftReduce(fd) {\n  for X -> Y in fd:\n    getSmallest(x ⊆ X where Y in closure(x))\n}\n"
			+ "rightReduce(fd) {\n  for X -> Y in fd:\n    for y in Y:\n      if X -> y covered by fd with X -> (Y \\ y):\n        "
			+ "remove y from X -> Y\n}\nformRelations(fd) {\n  for X -> Y in fd:\n    relations.add(relation(X -> Y)\n}\n"
			+ "eliminate(relations) {\n  for r, s in relations with r =/= s:\n    if r in s:\n      remove r\n}\n"
			+ "addKey(relations) {\n  for r in relations:\n    if key in r:\n      return\n  relations.add(relation(key))\n}";

	protected static Language lang;

	public final static Timing defaultDuration = new TicksTiming(30);

	private StringArray sa;

	private StringArray sa2;

	private Coordinates infoCoords = new Coordinates(20, 225);

	private Coordinates arrayCoords = new Coordinates(335, 100);

	private TextProperties paragraph;

	private TextProperties paragraphBold;

	private ArrayProperties arrayProps;

	private Text infoText;

	private SourceCode src;

	private SourceCodeProperties sourceProps;

	private Locale locale;

	private Translator translator;

	public Synthesis(String path, Locale loc) {
		this.locale = loc;
		this.translator = new Translator(path, this.locale);

	}

	public void init() {
		lang = new AnimalScript("Synthesis algorithm", "No\u00e9mie Catherine H\u00e9l\u00e8ne Spiller, Philipp Grenz",
				800, 600);
		lang.setStepMode(true);
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		fd = (String[]) primitives.get("Functional Dependencies");
		sourceProps = (SourceCodeProperties) props.getPropertiesByName("sourcecode properties");
		arrayProps = (ArrayProperties) props.getPropertiesByName("array properties");
		paragraph = (TextProperties) props.getPropertiesByName("text property");
		paragraphBold = (TextProperties) props.getPropertiesByName("step header property");
		synthesize(fd);
		lang.finalizeGeneration();
		return lang.toString();
	}

	/**
	 * @param dep
	 * @param att
	 */
	public void synthesize(String[] dep) {

		TextProperties heading = new TextProperties();
		heading.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 16));

		Text header = lang.newText(new Coordinates(20, 20), translator.translateMessage("header"), "header", null,
				heading);
		lang.nextStep();

		// Setting up overview of steps
		lang.newText(new Coordinates(20, 60), translator.translateMessage("introLine1"), "line1", null, paragraph);
		lang.newText(new Offset(0, 15, "line1", AnimalScript.DIRECTION_NW), translator.translateMessage("introLine2"),
				"line2", null, paragraph);
		lang.newText(new Offset(0, 20, "line2", AnimalScript.DIRECTION_NW), translator.translateMessage("introLine3"),
				"line3", null, paragraph);
		lang.newText(new Offset(0, 20, "line3", AnimalScript.DIRECTION_NW), translator.translateMessage("introLine4"),
				"line4", null, paragraph);
		lang.newText(new Offset(0, 20, "line4", AnimalScript.DIRECTION_NW), translator.translateMessage("introLine5"),
				"line5", null, paragraph);
		lang.newText(new Offset(0, 20, "line5", AnimalScript.DIRECTION_NW), translator.translateMessage("introLine6"),
				"line6", null, paragraph);

		// Setting up description of first step
		lang.nextStep(translator.translateMessage("stepLabel1"));
		lang.hideAllPrimitivesExcept(header);
		lang.newText(new Coordinates(20, 60), translator.translateMessage("step1Desc1"), "line21", null, paragraphBold);
		lang.newText(new Offset(0, 25, "line21", AnimalScript.DIRECTION_NW), translator.translateMessage("step1Desc2"),
				"line22", null, paragraph);
		lang.newText(new Offset(0, 20, "line22", AnimalScript.DIRECTION_NW), translator.translateMessage("step1Desc3"),
				"lineLinks1", null, paragraph);
		lang.nextStep();
		lang.newText(new Offset(0, 15, "lineLinks1", AnimalScript.DIRECTION_NW),
				translator.translateMessage("step1Desc4"), "lineLinks2", null, paragraph);
		lang.newText(new Offset(0, 15, "lineLinks2", AnimalScript.DIRECTION_NW),
				translator.translateMessage("step1Desc5"), "lineLinks3", null, paragraph);
		lang.newText(new Offset(0, 15, "lineLinks3", AnimalScript.DIRECTION_NW),
				translator.translateMessage("step1Desc6"), "lineLinks4", null, paragraph);
		lang.nextStep();
		lang.newText(new Offset(0, 20, "lineLinks4", AnimalScript.DIRECTION_NW),
				translator.translateMessage("step1Desc7"), "lineRechts1", null, paragraph);
		lang.nextStep();
		lang.newText(new Offset(0, 15, "lineRechts1", AnimalScript.DIRECTION_NW),
				translator.translateMessage("step1Desc8"), "lineRechts2", null, paragraph);
		lang.newText(new Offset(0, 15, "lineRechts2", AnimalScript.DIRECTION_NW),
				translator.translateMessage("step1Desc9"), "lineRechts3", null, paragraph);
		lang.newText(new Offset(0, 15, "lineRechts3", AnimalScript.DIRECTION_NW),
				translator.translateMessage("step1Desc10"), "lineRechts4", null, paragraph);
		lang.nextStep();
		lang.newText(new Offset(0, 20, "lineRechts4", AnimalScript.DIRECTION_NW),
				translator.translateMessage("step1Desc11"), "line27", null, paragraph);
		lang.nextStep();
		lang.newText(new Offset(0, 15, "line27", AnimalScript.DIRECTION_NW), translator.translateMessage("step1Desc12"),
				"line8", null, paragraph);
		lang.nextStep();

		lang.hideAllPrimitivesExcept(header);

		// Setting up the left reduction
		lang.newText(new Coordinates(20, 60), translator.translateMessage("subHeader1"), "line1", null, paragraphBold);

		src = lang.newSourceCode(new Coordinates(20, 93), "SrcLeft", null, sourceProps);
		src.addCodeLine("for each Ψ -> Γ ∈ FD do", null, 0, null);
		src.addCodeLine("if size(Ψ) > 1 then", null, 1, null);
		src.addCodeLine("for each ψ ⊆ Ψ do", null, 2, null);
		src.addCodeLine("if Γ ⊆ closure(FD, ψ) then", null, 3, null);
		src.addCodeLine("replace Ψ with ψ", null, 4, null);

		sa = lang.newStringArray(arrayCoords, arrayAsFD(dep), "StringArray", null, arrayProps);
		sa.showIndices(false, null, null);
		sa.setBorderColor(0, sa.getLength(), Color.WHITE, null, null);
		lang.nextStep(translator.translateMessage("stepLabel2"));

		// Computing key for last step
		String key = computeKeyCandidates(dep);

		// Starting left reduction
		String[] step1 = reduceLeft(dep.clone());
		infoText = lang.newText(infoCoords, translator.translateMessage("step1infoDone"), "info", null, paragraph);

		lang.nextStep();
		lang.hideAllPrimitivesExcept(header);

		// Setting up the right reduction
		lang.newText(new Coordinates(20, 60), translator.translateMessage("subHeader2"), "line1", null, paragraphBold);

		src = lang.newSourceCode(new Coordinates(20, 93), "SrcRight", null, sourceProps);
		src.addCodeLine("for each Ψ -> Γ ∈ FD do", null, 0, null);
		src.addCodeLine("for each γ ∈ Γ do", null, 1, null);
		src.addCodeLine("if size(Γ) > 1 then", null, 2, null);
		src.addCodeLine("if γ ∈ closure(FD where Ψ -> (Γ without γ) , Ψ) then", null, 3, null);
		src.addCodeLine("remove γ from Γ", null, 4, null);
		src.addCodeLine("else if γ ∈ closure(FD without Ψ -> Γ, Ψ) then", null, 2, null);
		src.addCodeLine("remove Ψ -> Γ from FD", null, 3, null);

		sa = lang.newStringArray(arrayCoords, arrayAsFD(step1), "StringArray", null, arrayProps);
		sa.showIndices(false, null, null);
		sa.setBorderColor(0, sa.getLength(), Color.WHITE, null, null);
		lang.nextStep(translator.translateMessage("stepLabel3"));

		// Starting right reduction
		String[] step2 = reduceRight(step1.clone());
		src.unhighlight(0);
		infoText = lang.newText(infoCoords, translator.translateMessage("step2infoDone"), "info", null, paragraph);

		lang.nextStep();
		lang.hideAllPrimitivesExcept(header);

		// Setting up grouping
		lang.newText(new Coordinates(20, 60), translator.translateMessage("subHeader3"), "line1", null, paragraphBold);

		sa = lang.newStringArray(arrayCoords, arrayAsFD(step2), "StringArray", null, arrayProps);
		sa.moveBy(null, -315, 0, null, null);
		sa.showIndices(false, null, null);
		sa.setBorderColor(0, sa.getLength(), Color.WHITE, null, null);
		infoText = lang.newText(infoCoords, translator.translateMessage("step3info"), "info", null, paragraph);

		// Grouping
		String[] step3 = groupLeft(step2.clone());

		// Question about grouping step
		String groupQuestion = translator.translateMessage("questionGroup");
		String questionFeedback = translator.translateMessage("questionFeedback");
		FillInBlanksQuestionModel question = new FillInBlanksQuestionModel("groupQuestion");
		question.setPrompt(groupQuestion);
		question.addAnswer(Integer.toString(step3.length), 1, questionFeedback);
		lang.addFIBQuestion(question);

		lang.nextStep(translator.translateMessage("stepLabel4"));
		sa.hide();

		infoText.hide();
		infoText = lang.newText(infoCoords, translator.translateMessage("step3infoDone"), "info", null, paragraph);
		sa = lang.newStringArray(arrayCoords, arrayAsFD(step3), "StringArray", null, arrayProps);
		sa.showIndices(false, null, null);
		sa.setBorderColor(0, sa.getLength(), Color.WHITE, null, null);
		sa.moveBy(null, -315, 0, null, null);
		lang.nextStep();
		lang.hideAllPrimitivesExcept(header);
		sa.hide();

		// Setting up relation forming step
		lang.newText(new Coordinates(20, 60), translator.translateMessage("subHeader4"), "line1", null, paragraphBold);

		lang.newText(new Offset(0, 25, "line1", AnimalScript.DIRECTION_NW), translator.translateMessage("step4Desc1"),
				"info", null, paragraph);
		lang.nextStep(translator.translateMessage("stepLabel5"));
		lang.hideAllPrimitivesExcept(header);
		lang.newText(new Coordinates(20, 60), translator.translateMessage("subHeader4"), "line1", null, paragraphBold);

		src = lang.newSourceCode(new Coordinates(20, 93), "SrcBuild", null, sourceProps);
		src.addCodeLine("for each Ψ -> Γ ∈ FD do", null, 0, null);
		src.addCodeLine("formRelation(Ψ -> Γ)", null, 1, null);

		sa.show();
		sa.moveBy(null, 315, 0, null, null);

		String[] placeholder = new String[step3.length + 1];
		for (int i = 0; i < placeholder.length; i++)
			placeholder[i] = " ";

		sa2 = lang.newStringArray(new Offset(0, 40, "StringArray", AnimalScript.DIRECTION_NW), placeholder,
				"SecondStringArray", null, arrayProps);
		sa2.showIndices(false, null, null);
		sa2.setBorderColor(0, sa2.getLength(), Color.WHITE, null, null);

		lang.nextStep();

		// Forming relations
		String[] relations = formRelations(step3);
		lang.nextStep();

		infoText = lang.newText(infoCoords, translator.translateMessage("step4infoDone"), "info", null, paragraph);

		lang.nextStep();
		sa.hide();
		lang.hideAllPrimitivesExcept(header);

		// Setting up elimination step
		lang.newText(new Coordinates(20, 60), translator.translateMessage("subHeader5"), "line31", null, paragraphBold);

		lang.newText(new Offset(0, 25, "line31", AnimalScript.DIRECTION_NW), translator.translateMessage("step5Desc1"),
				"line32", null, paragraph);
		lang.newText(new Offset(0, 15, "line32", AnimalScript.DIRECTION_NW), translator.translateMessage("step5Desc2"),
				"line33", null, paragraph);
		lang.nextStep(translator.translateMessage("stepLabel6"));

		lang.hideAllPrimitivesExcept(header);
		lang.newText(new Coordinates(20, 60), translator.translateMessage("subHeader5"), "line1", null, paragraphBold);

		src = lang.newSourceCode(new Coordinates(20, 93), "SrcEliminate", null, sourceProps);
		src.addCodeLine("for each Ri ∈ R do", null, 0, null);
		src.addCodeLine("for j:= i + 1; j <= size(R); j++ do", null, 1, null);
		src.addCodeLine("if Rj ⊆ Ri then", null, 2, null);
		src.addCodeLine("remove Rj", null, 3, null);
		src.addCodeLine("else if Ri ⊆ Rj then", null, 2, null);
		src.addCodeLine("remove Ri", null, 3, null);
		src.addCodeLine("break", null, 3, null);

		// Question about elimination needs at least 2 relations to make sense
		if (relations.length > 1) {
			Random random = new Random();
			MultipleSelectionQuestionModel questionMulSel = new MultipleSelectionQuestionModel("questionElim");
			questionMulSel.setPrompt(translator.translateMessage("questionElim"));
			if (relations.length < 3) {

				int a = addAnswerToQuestion(questionMulSel, "R" + 1, relations, 0);
				int b = addAnswerToQuestion(questionMulSel, "R" + 1, relations, 1);

				if (a + b > 0)
					questionMulSel.addAnswer(translator.translateMessage("questionNone"), 0,
							translator.translateMessage("questionNoneFalse"));
				else
					questionMulSel.addAnswer(translator.translateMessage("questionNone"), 1,
							translator.translateMessage("questionNoneCorrect"));

			} else {

				int rnd1 = random.nextInt(relations.length);

				int a = addAnswerToQuestion(questionMulSel, "R" + (rnd1 + 1), relations, rnd1);

				int rnd2 = random.nextInt(relations.length);

				while (rnd2 == rnd1)
					rnd2 = random.nextInt(relations.length);

				int b = addAnswerToQuestion(questionMulSel, "R" + (rnd2 + 1), relations, rnd2);

				int rnd3 = random.nextInt(relations.length);

				while (rnd3 == rnd1 || rnd3 == rnd2)
					rnd2 = random.nextInt(relations.length);

				int c = addAnswerToQuestion(questionMulSel, "R" + (rnd3 + 1), relations, rnd2);

				if (a + b + c > 0)
					questionMulSel.addAnswer(translator.translateMessage("questionNone"), 0,
							translator.translateMessage("questionNoneFalse"));
				else
					questionMulSel.addAnswer(translator.translateMessage("questionNone"), 1,
							translator.translateMessage("questionNoneCorrect"));
			}

			lang.addMSQuestion(questionMulSel);

		}

		sa = lang.newStringArray(arrayCoords, step3, "StringArray", null, arrayProps);
		sa.showIndices(false, null, null);
		sa.setBorderColor(0, sa.getLength(), Color.WHITE, null, null);

		// Eliminating relations
		List<String> list = eliminateCoveredRelations(relations.clone(), step3);
		infoText = lang.newText(infoCoords, translator.translateMessage("step5infoDone"), "info", null, paragraph);

		lang.nextStep();
		lang.hideAllPrimitivesExcept(header);
		lang.newText(new Coordinates(20, 60), translator.translateMessage("subHeader6") + key, "line41", null,
				paragraphBold);
		lang.newText(new Offset(0, 25, "line41", AnimalScript.DIRECTION_NW), translator.translateMessage("step6Desc1"),
				"line42", null, paragraph);
		lang.newText(new Offset(0, 15, "line42", AnimalScript.DIRECTION_NW), translator.translateMessage("step6Desc2"),
				"line43", null, paragraph);

		lang.nextStep(translator.translateMessage("stepLabel7"));
		lang.hideAllPrimitivesExcept(header);

		// Setting up last step
		lang.newText(new Coordinates(20, 60), translator.translateMessage("subHeader6") + key, "line1", null,
				paragraphBold);

		src = lang.newSourceCode(new Coordinates(20, 93), "SrcAddKey", null, sourceProps);
		src.addCodeLine("for i := 1; i <= size(R); 1++ do", null, 0, null);
		src.addCodeLine("if " + key + " ⊆ Ri then", null, 1, null);
		src.addCodeLine("break", null, 2, null);
		src.addCodeLine("else if " + key + " ⊈ Ri && i == size(R) then", null, 1, null);
		src.addCodeLine("add formRelation(" + key + ")", null, 2, null);

		// Question
		TrueFalseQuestionModel questionTF = new TrueFalseQuestionModel("questionAdded", needToAddKey(list, key), 1);
		questionTF.setPrompt(translator.translateMessage("questionTF", key));
		lang.addTFQuestion(questionTF);

		sa = lang.newStringArray(arrayCoords, placeholder, "StringArray", null, arrayProps);
		sa.showIndices(false, null, null);
		sa.setBorderColor(0, sa.getLength(), Color.WHITE, null, null);

		for (int i = 0; i < list.size(); i++)
			sa.put(i, "R" + (i + 1) + " = (" + list.get(i).split(";")[0] + "), Key: " + list.get(i).split(";")[0] + ";",
					null, null);

		// Determining whether last step is needed
		boolean addedKeyRelation = isMissingRelation(list, key);

		if (addedKeyRelation) {
			infoText = lang.newText(infoCoords, key + " " + translator.translateMessage("step6Added"), "info", null,
					paragraph);
			lang.nextStep();
			sa.put(list.size() - 1, "R" + (list.size()) + " = (" + list.get(list.size() - 1).split(";")[0] + "), Key: "
					+ list.get(list.size() - 1).split(";")[0], null, null);
		} else {
			infoText = lang.newText(infoCoords, key + " " + translator.translateMessage("step6NotAdded"), "info", null,
					paragraph);

		}

		lang.nextStep();
		lang.hideAllPrimitivesExcept(header);

		// Showing final relations
		lang.newText(new Coordinates(20, 60), translator.translateMessage("subHeader7"), "line71", null, paragraphBold);
		sa.show();
		sa.moveBy(null, -315, 0, null, null);
		infoText.hide();
		lang.nextStep();
		sa.hide();

		// Showing summary of steps
		lang.newText(new Offset(0, 25, "line71", AnimalScript.DIRECTION_NW), translator.translateMessage("summary1"),
				"summary1", null, paragraph);
		String summary;
		if (addedKeyRelation)
			summary = translator.translateMessage("summary2");
		else
			summary = translator.translateMessage("summary3");
		lang.newText(new Offset(0, 15, "summary1", AnimalScript.DIRECTION_NW), summary, "summary", null, paragraph);
	}

	/**
	 * Reduces the left sides of all dependencies
	 * 
	 * @param dep the dependencies
	 * @return left reduced dependencies
	 */
	private String[] reduceLeft(String[] dep) {
		src.highlight(0);
		// Build a question for a random functional dependency
		Random random = new Random();
		int questionRound = random.nextInt(dep.length);
		String questionLeft = translator.translateMessage("questionLeft", dep[questionRound]);

		for (int i = 0; i < dep.length; i++) {
			sa.highlightElem(i + 1, null, null);
			String[] parts = dep[i].split(" -> ");

			// Prompt the question when the randomly selected round is reached
			TrueFalseQuestionModel questionTFLeft = new TrueFalseQuestionModel("questionLeft",
					(smallestSubsetQuestion(parts[0], parts[1], dep).length() != parts[0].length()), 1);
			questionTFLeft.setPrompt(questionLeft);
			if (i == questionRound)
				lang.addTFQuestion(questionTFLeft);

			lang.nextStep();
			src.toggleHighlight(0, 1);

			// Check length of left side
			if (parts[0].length() > 1) {

				infoText = lang.newText(infoCoords, translator.translateMessage("step1infoNotMinimal"), "info", null,
						paragraph);

				// Find the smallest possible subset containing the right side in its closure
				dep[i] = smallestSubset(parts[0], parts[1], dep) + " -> " + parts[1];
				sa.put(i + 1, dep[i] + ";", null, null);

			} else {
				// left side contains only one attribute
				infoText = lang.newText(infoCoords, translator.translateMessage("step1infoMinimal"), "info", null,
						paragraph);
			}
			lang.nextStep();
			src.unhighlight(1);
			src.highlight(0);
			sa.unhighlightElem(i + 1, null, null);
			infoText.hide();
		}
		src.unhighlight(0);
		return dep;
	}

	/**
	 * Finds smallest subset of left side covering right side
	 * 
	 * @param leftSide  left side of a dependency
	 * @param rightSide right side of a dependency
	 * @param dep       the dependencies
	 * @return the smallest subset of leftSide
	 */
	private String smallestSubset(String leftSide, String rightSide, String[] dep) {

		lang.nextStep();
		src.toggleHighlight(1, 2);
		infoText.hide();
		infoText = lang.newText(infoCoords, translator.translateMessage("step1infoCheckSubsets"), "info", null,
				paragraph);

		// Split left side into attributes
		String[] items = leftSide.split("");

		// Generate all possible subsets of the set of attributes
		String[] sets = computeSubsets(items).split(";");

		// Sort the subsets by length
		Arrays.sort(sets, new Comparator<String>() {
			public int compare(String s, String t) {
				return s.length() - t.length();
			}
		});

		// Go through sets and return the first subset that covers rightSide
		for (String s : sets) {
			if (s.equals(leftSide))
				break;
			lang.nextStep();
			src.toggleHighlight(2, 3);
			infoText.hide();
			String hullS = computeAttributeHull(s, dep);
			infoText = lang.newText(infoCoords, translator.translateMessage("step1infoHulls") + " " + s + ": " + hullS,
					"info", null, paragraph);
			if (covered(hullS, rightSide)) {
				lang.nextStep();
				src.toggleHighlight(3, 4);
				infoText.hide();
				infoText = lang.newText(infoCoords,
						translator.translateMessage("step1infoContains1") + " " + s + " "
								+ translator.translateMessage("step1infoContains2") + " " + rightSide + ", "
								+ translator.translateMessage("step1infoContains3"),
						"info", null, paragraph);
				lang.nextStep();
				src.unhighlight(4);
				return s;
			}
		}
		lang.nextStep();
		src.unhighlight(3);
		infoText.hide();
		infoText = lang.newText(infoCoords, translator.translateMessage("step1infoContainsNot1") + " " + rightSide
				+ ", " + translator.translateMessage("step1infoContainsNot2"), "info", null, paragraph);
		// return the leftSide if no smaller subset was found
		return leftSide;
	}

	/**
	 * Used for determining the answer of the question Functions identical to
	 * smallestSubset()
	 * 
	 */
	private String smallestSubsetQuestion(String leftSide, String rightSide, String[] dep) {

		String[] items = leftSide.split("");

		String[] sets = computeSubsets(items).split(";");

		Arrays.sort(sets, new Comparator<String>() {
			public int compare(String s, String t) {
				return s.length() - t.length();
			}
		});
		for (String s : sets) {
			if (s.equals(leftSide))
				break;

			String hullS = computeAttributeHull(s, dep);

			if (covered(hullS, rightSide)) {
				return s;
			}
		}
		return leftSide;
	}

	/**
	 * Computes all subsets the given items
	 * 
	 * @param items the attributes that will make up the subsets
	 * @return
	 */
	private String computeSubsets(String[] items) {

		String subsets = "";

		// Calculate the number of possible sets
		int sets = (int) Math.pow(2, items.length);

		// Leave out the empty subset by setting counter to 1
		for (int i = 1; i < sets; i++) {
			int bits = i;
			int index = 0;

			String temp = "";

			// Select the attributes based on the bit representation of the counter
			while (bits != 0) {
				if ((bits & 1) == 1)
					temp += items[index];
				bits = bits >> 1;
				index++;
			}
			subsets += temp + ";";
		}

		// return the subsets
		return subsets;
	}

	/**
	 * Reduces the right sides of all dependencies
	 * 
	 * @param dep the dependencies
	 * @return the right reduced dependencies
	 */
	private String[] reduceRight(String[] dep) {
		src.highlight(0);
		for (int i = 0; i < dep.length; i++) {
			// Save the current dependency in temp and remove it from dep
			String temp = dep[i];
			dep[i] = "";
			String[] parts = temp.split(" -> ");
			sa.highlightElem(i + 1, null, null);
			// If the length of the right side is 1 check if the dependency is needed
			if (parts[1].length() == 1) {
				lang.nextStep();
				src.toggleHighlight(0, 1);
				lang.nextStep();
				src.toggleHighlight(0, 1);
				lang.nextStep();
				src.toggleHighlight(1, 5);
				infoText = lang.newText(infoCoords, translator.translateMessage("step2infoMinimal"), "info", null,
						paragraph);
				lang.nextStep();
				infoText.hide();
				infoText = lang.newText(infoCoords, translator.translateMessage("step2infoCheckTransitive"), "info",
						null, paragraph);
				// Check if the modified dep already covers the closure of the dependency
				if (!covered(computeAttributeHull(parts[0], dep), parts[1])) {
					// Dependency is still needed and gets added back into dep
					dep[i] = temp;
					lang.nextStep();
					src.unhighlight(5);
					infoText.hide();
					infoText = lang.newText(infoCoords, translator.translateMessage("step2infoNotGiven"), "info", null,
							paragraph);
				} else {
					// Dependency is not needed and is not added back
					lang.nextStep();
					src.toggleHighlight(5, 6);
					infoText.hide();
					infoText = lang.newText(infoCoords, translator.translateMessage("step2infoGiven"), "info", null,
							paragraph);
					lang.nextStep();
					src.unhighlight(6);
					sa.put(i + 1, "", null, null);
				}
			} else {
				// Right side has more than 1 attribute now each attribute has to be checked
				int j = 0;
				while (j < parts[1].length()) {
					lang.nextStep();
					infoText.hide();
					src.toggleHighlight(0, 1);
					char cj = parts[1].charAt(j);
					// Check if the current char is the last one in order to properly remove it
					if (j < parts[1].length() - 1) {
						lang.nextStep();
						src.toggleHighlight(1, 2);
						infoText = lang.newText(infoCoords, translator.translateMessage("step2infoNotMinimal"), "info",
								null, paragraph);
						lang.nextStep();
						src.toggleHighlight(2, 3);
						infoText.hide();
						// Modify the current dependency by removing the current attribute from it
						dep[i] = parts[0] + " -> " + parts[1].substring(0, j) + parts[1].substring(j + 1);
						String hull = computeAttributeHull(parts[0], dep);
						infoText = lang.newText(infoCoords,
								translator.translateMessage("step2infoCheckIf") + cj + " -> " + parts[0] + " "
										+ translator.translateMessage("step2infoAlreadyGiven") + dep[i] + " "
										+ translator.translateMessage("step2infoByFD"),
								"info", null, paragraph);
						// Check if attribute is already covered.
						if (!covered(hull, String.valueOf(parts[1].charAt(j)))) {
							// Add the attribute back
							lang.nextStep();
							src.unhighlight(3);
							infoText.hide();
							infoText = lang
									.newText(infoCoords,
											translator.translateMessage("step2infoDepNotAlready") + cj + " "
													+ translator.translateMessage("step2infoDepKeep"),
											"info", null, paragraph);
							dep[i] = temp;
							j++;
						} else {
							// Attribute is not needed
							lang.nextStep();
							src.toggleHighlight(3, 4);
							infoText.hide();
							infoText = lang.newText(infoCoords,
									translator.translateMessage("step2infoDepAlready") + cj + " "
											+ translator.translateMessage("step2infoDepRemove"),
									"info", null, paragraph);
							temp = dep[i];
							parts = temp.split(" -> ");
							lang.nextStep();
							src.unhighlight(4);
							sa.put(i + 1, dep[i] + ";", null, null);
						}
					} else {
						// Last char check if only one char is left
						if (parts[1].length() == 1) {
							// Only 1 left
							lang.nextStep();
							src.toggleHighlight(1, 2);
							infoText = lang.newText(infoCoords, translator.translateMessage("step2infoMinimal"), "info",
									null, paragraph);
							dep[i] = "";
							String hull = computeAttributeHull(parts[0], dep);
							lang.nextStep();
							src.toggleHighlight(2, 5);
							infoText.hide();
							infoText = lang.newText(infoCoords, translator.translateMessage("step2infoCheckTransitive"),
									"info", null, paragraph);
							// Check if dependency is needed
							if (!covered(hull, String.valueOf(parts[1].charAt(j)))) {
								// Keep it
								lang.nextStep();
								src.unhighlight(5);
								infoText.hide();
								infoText = lang.newText(infoCoords,
										translator.translateMessage("step2infoDepNotAlready") + temp + " "
												+ translator.translateMessage("step2infoDepKeep"),
										"info", null, paragraph);
								dep[i] = temp;
								j++;
							} else {
								// Drop it
								lang.nextStep();
								src.toggleHighlight(5, 6);
								infoText.hide();
								infoText = lang.newText(infoCoords,
										translator.translateMessage("step2infoDepAlready") + temp + " "
												+ translator.translateMessage("step2infoDepRemove"),
										"info", null, paragraph);
								lang.nextStep();
								src.unhighlight(6);
								sa.put(i + 1, dep[i], null, null);
								break;
							}
						} else {
							// Last char but more than one attribute left on right side
							lang.nextStep();
							src.toggleHighlight(1, 2);
							infoText = lang.newText(infoCoords, translator.translateMessage("step2infoNotMinimal"),
									"info", null, paragraph);
							// Remove last attribute
							dep[i] = parts[0] + " -> " + parts[1].substring(0, j);
							String hull = computeAttributeHull(parts[0], dep);
							lang.nextStep();
							src.toggleHighlight(2, 3);
							infoText.hide();

							infoText = lang.newText(infoCoords,
									translator.translateMessage("step2infoCheckIf") + cj + " -> " + parts[0] + " "
											+ translator.translateMessage("step2infoAlreadyGiven") + dep[i] + " "
											+ translator.translateMessage("step2infoByFD"),
									"info", null, paragraph);
							// Check if attribute is needed
							if (!covered(hull, String.valueOf(parts[1].charAt(j)))) {
								// Needed. Keep it
								lang.nextStep();
								src.unhighlight(3);
								infoText.hide();
								infoText = lang.newText(infoCoords,
										translator.translateMessage("step2infoDepNotAlready") + cj + " "
												+ translator.translateMessage("step2infoDepKeep"),
										"info", null, paragraph);
								dep[i] = temp;
								j++;
							} else {
								// Not needed. Drop it
								lang.nextStep();
								src.toggleHighlight(3, 4);
								infoText.hide();
								infoText = lang.newText(infoCoords,
										translator.translateMessage("step2infoDepAlready") + cj + " "
												+ translator.translateMessage("step2infoDepRemove"),
										"info", null, paragraph);
								temp = dep[i];
								parts = temp.split(" -> ");
								lang.nextStep();
								src.unhighlight(4);
								sa.put(i + 1, dep[i] + ";", null, null);
							}
						}
					}
				}
			}
			lang.nextStep();
			src.highlight(0);
			sa.unhighlightElem(i + 1, null, null);
			infoText.hide();
		}
		// return reduced dependencies
		return dep;
	}

	/**
	 * Groups equal left sides
	 * 
	 * @param dep the dependencies
	 * @return the grouped dependencies
	 */
	private String[] groupLeft(String[] dep) {
		String temp = "";
		// Go through dependencies
		for (int i = 0; i < dep.length; i++) {
			// Check if dependency as already been grouped with another
			if (!dep[i].equals("")) {
				temp += dep[i];
				String leftSide = dep[i].split(" -> ")[0];
				// Look at all dependencies after the current one
				for (int j = i + 1; j < dep.length; j++) {
					String[] parts = dep[j].split(" -> ");
					// If the left side is the same merge the right sides
					if (parts[0].equals(leftSide)) {
						for (char c : parts[1].toCharArray()) {
							if (!dep[i].contains(c + ""))
								temp += c;
						}
						dep[j] = "";
					}
				}
				temp += ";";
			}
		}
		// return the grouped dependencies
		return temp.split(";");
	}

	/**
	 * Forms relations from functional dependencies
	 * 
	 * @param dep the dependencies
	 * @return the relations
	 */
	private String[] formRelations(String[] dep) {

		String[] ret = new String[dep.length];
		String[] temp;
		// Turn each dependency into a relation using the left side as the key
		for (int i = 0; i < dep.length; i++) {
			temp = dep[i].split(" -> ");
			ret[i] = temp[0] + temp[1] + ";" + temp[0];
			dep[i] = "R" + (i + 1) + " = (" + temp[0] + temp[1] + "), Key: " + temp[0] + ";";
			sa.highlightElem(i + 1, null, null);
			src.highlight(0);
			lang.nextStep();
			src.toggleHighlight(0, 1);
			sa2.put(i, dep[i], null, null);
			lang.nextStep();
			src.unhighlight(1);
			sa.unhighlightElem(i + 1, null, null);
		}
		// return the relations
		return ret;
	}

	/**
	 * Eliminates relations that are already contained in another relation
	 * 
	 * @param rel        the relations
	 * @param relStrings the relations as string representation
	 * @return the left over relations
	 */
	private List<String> eliminateCoveredRelations(String[] rel, String[] relStrings) {
		List<String> ret = new ArrayList<String>();
		String relA;
		String relB;
		lang.nextStep();
		// Go through all relations
		for (int i = 0; i < rel.length - 1; i++) {
			// Skip the field if it has already been removed
			if (!rel[i].equals("")) {
				// Get attributes from the current relation
				relA = rel[i].split(";")[0];
				infoText = lang.newText(infoCoords, translator.translateMessage("step5infoCompare1") + (i + 1) + " "
						+ translator.translateMessage("step5infoCompare2"), "info", null, paragraph);
				sa.highlightElem(i, null, null);
				src.highlight(0);
				lang.nextStep();
				src.unhighlight(0);
				// Compare to attributes of all following relations
				for (int j = i + 1; j < rel.length; j++) {
					src.highlight(1);
					lang.nextStep();
					sa.highlightElem(j, null, null);
					src.toggleHighlight(1, 2);
					relB = rel[j].split(";")[0];
					// Check if one is contained in the other and remove the contained one
					if (containsAllChars(relA, relB)) {
						infoText.hide();
						infoText = lang.newText(infoCoords,
								"R" + (i + 1) + " " + translator.translateMessage("step5infoContains") + " R" + (j + 1)
										+ " " + translator.translateMessage("step5infoCompletely") + " R" + (j + 1)
										+ " " + translator.translateMessage("step5infoRemoved"),
								"info", null, paragraph);
						lang.nextStep();
						src.toggleHighlight(2, 3);
						rel[j] = "";
						sa.put(j, " ", null, null);
					} else {
						lang.nextStep();
						src.toggleHighlight(2, 4);
						if (containsAllChars(relB, relA)) {
							infoText.hide();
							infoText = lang.newText(infoCoords,
									"R" + (j + 1) + " " + translator.translateMessage("step5infoContains") + " R"
											+ (i + 1) + " " + translator.translateMessage("step5infoCompletely") + " R"
											+ (i + 1) + " " + translator.translateMessage("step5infoRemoved"),
									"info", null, paragraph);
							lang.nextStep();
							src.toggleHighlight(4, 5);
							rel[i] = "";
							sa.put(j, " ", null, null);
							lang.nextStep();
							src.toggleHighlight(5, 6);
							sa.unhighlightElem(j, null, null);
							infoText.hide();
							break;
						}
					}
					lang.nextStep();
					src.unhighlight(4);
					src.unhighlight(3);
					sa.unhighlightElem(j, null, null);
				}
				if (!rel[i].equals("")) {
					ret.add(rel[i]);
				}
				sa.unhighlightElem(i, null, null);
				infoText.hide();
				src.unhighlight(1);
				src.unhighlight(2);
				src.unhighlight(6);
			}
		}
		// Finish the return array
		if (!rel[rel.length - 1].equals("")) {
			ret.add(rel[rel.length - 1]);
			src.highlight(0);
			sa.highlightElem(rel.length - 1, null, null);
			lang.nextStep();
			src.toggleHighlight(0, 1);
			lang.nextStep();
			src.unhighlight(1);
			sa.unhighlightElem(rel.length - 1, null, null);
		}
		// return the left over relations
		return ret;
	}

	/**
	 * Checks if the key is contained one of the relations
	 * 
	 * @param rel a list of relations
	 * @param key the key
	 * @return a boolean stating whether a key relation has been added or not
	 */
	private boolean isMissingRelation(List<String> rel, String key) {

		boolean isCovered = false;
		lang.nextStep();
		// Go through all relations
		for (int i = 0; i < rel.size(); i++) {

			src.highlight(0);
			sa.highlightElem(i, null, null);
			lang.nextStep();
			src.toggleHighlight(0, 1);
			lang.nextStep();
			src.unhighlight(1);
			// If it contains key break and set isCovered to true
			if (containsAllChars(rel.get(i), key)) {
				src.highlight(2);
				lang.nextStep();
				src.unhighlight(2);
				isCovered = true;
				sa.unhighlightElem(i, null, null);
				break;
			} else {
				src.highlight(3);
				lang.nextStep();

				if (!containsAllChars(rel.get(i), key) && i == rel.size() - 1) {
					src.toggleHighlight(3, 4);
					lang.nextStep();
					src.unhighlight(4);
				}
			}
			src.unhighlight(3);
			sa.unhighlightElem(i, null, null);
		}
		// add key relation if key is not Covered
		if (!isCovered)
			rel.add(key + ";" + key);

		// return true if not covered else false
		return !isCovered;

	}

	/**
	 * Used for determining correct answer of the question Functions indentically to
	 * isMissingRelation()
	 */
	private boolean needToAddKey(List<String> rel, String key) {
		boolean isCovered = false;
		for (int i = 0; i < rel.size(); i++) {
			if (containsAllChars(rel.get(i), key)) {
				isCovered = true;
				break;
			}
		}
		return !isCovered;
	}

	/**
	 * Adds answer to the question and determines whether that answer is wrong or
	 * right
	 * 
	 * @param q     the question
	 * @param qText answer's text
	 * @param rel   the relations
	 * @param index index of the used relations
	 * @return 1 if correct answer was added 0 if not
	 */
	private int addAnswerToQuestion(MultipleSelectionQuestionModel q, String qText, String[] rel, int index) {

		String relation = rel[index];
		String attributes = relation.split(";")[0];
		// Checks if the relation at index will be removed and adds an answer
		// accordingly
		for (String s : rel) {
			if (!s.equals(relation)) {
				s = s.split(";")[0];
				if (containsAllChars(s, attributes)) {
					q.addAnswer(qText, 1, translator.translateMessage("questionElimCorrect", qText));
					return 1;
				}
			}
		}
		q.addAnswer(qText, 0, translator.translateMessage("questionElimWrong", qText));
		return 0;
	}

	/**
	 * Checks if relA contains all of relB's chars
	 * 
	 * @param relA
	 * @param relB
	 * @return true if relA contains all of relB's chars
	 */
	private boolean containsAllChars(String relA, String relB) {
		return toCharSet(relA).containsAll(toCharSet(relB));
	}

	/**
	 * Turns string into a set of single character strings
	 * 
	 * @param s strings
	 * @return the set of strings
	 */
	private Set<String> toCharSet(String s) {
		Set<String> set = new HashSet<String>();

		for (char c : s.toCharArray())
			set.add(c + "");

		return set;
	}

	/**
	 * Finds a key for the relational db schema
	 * 
	 * @param dep dependencies
	 * @return the key
	 */
	private String computeKeyCandidates(String[] dep) {

		// Get all attributes that appear in dep
		String[] attributes = getAttributes(dep);

		// Find all subsets of attributes
		String[] sets = computeSubsets(attributes).split(";");

		// Sort the subsets alphabetically and by length
		Arrays.sort(sets);
		Arrays.sort(sets, new Comparator<String>() {
			public int compare(String s, String t) {
				return s.length() - t.length();
			}
		});

		// Build a string containing all the attributes
		String att = "";
		for (String s : attributes) {
			att += s;
		}

		// return the first element of set that coveres all attributes with its hull
		for (String s : sets) {
			if (att.equals(computeAttributeHull(s, dep))) {
				return s;
			}
		}

		// return a string containing all attributes if no shorter alternative was found
		return Arrays.toString(attributes);
	}

	/**
	 * Returns all attributes appearing in the given dependencies
	 * 
	 * @param dep the dependencies
	 * @return an array containing all the attributes
	 */
	private String[] getAttributes(String[] dep) {
		String att = "";
		String[] temp;
		for (String s : dep) {
			temp = s.split(" -> ");
			for (char c : temp[0].toCharArray()) {
				if (!att.contains(c + ""))
					att += c + ";";
			}
			for (char c : temp[1].toCharArray()) {
				if (!att.contains(c + ""))
					att += c + ";";
			}
		}
		temp = att.split(";");
		Arrays.sort(temp);
		return temp;
	}

	/**
	 * computes the closure of an attribute in the set of dependencies
	 * 
	 * @param att
	 * @param dep
	 * @return the closure of att as a string
	 */
	private String computeAttributeHull(String att, String[] dep) {
		String attHull = att;
		boolean changed = false;
		attHull = att;
		for (int i = 0; i < dep.length; i++) {
			if (!dep[i].equals("")) {
				String[] parts = dep[i].split(" -> ");
				if (covered(attHull, parts[0]) && parts.length > 1) {
					char[] temp = parts[1].toCharArray();
					for (char c : temp) {
						if (!attHull.contains("" + c)) {
							attHull = attHull + c;
							changed = true;
						}
					}
				}
				if (i == dep.length - 1 && changed) {
					i = 0;
					changed = false;
				}
				char[] attHullChars = attHull.toCharArray();
				Arrays.sort(attHullChars);
				attHull = String.valueOf(attHullChars);
			}
		}
		return attHull;
	}

	/**
	 * @param hull       Attributhülle des betrachteten Attributes bzw. der
	 *                   betrachteten Attribute
	 * @param attributes Attribute auf die die Hülle überprüft wird
	 * @return true wenn alle Attribute enthalten sind
	 */
	private boolean covered(String hull, String attributes) {
		boolean ret = true;
		char[] temp = attributes.toCharArray();

		for (char c : temp)
			ret = ret && hull.contains("" + c);

		return ret;
	}

	/**
	 * Formats to array to look like a set when turned into a string
	 * 
	 * @param input
	 * @return
	 */
	private String[] arrayAsFD(String[] input) {
		String[] ret = new String[input.length + 2];
		ret[0] = "FD = {";
		ret[ret.length - 1] = "}";
		for (int i = 0; i < input.length; i++)
			if (!input[i].equals(""))
				ret[i + 1] = input[i] + ";";
			else
				ret[i + 1] = " ";
		return ret;
	}

	public static void main(String[] args) {
		Synthesis synthesisGeneratorUS = new Synthesis("resources/Synthesis", Locale.US);
		Synthesis synthesisGeneratorDE = new Synthesis("resources/Synthesis", Locale.GERMANY);

		// Animal.startGeneratorWindow(synthesisGeneratorDE);
		Animal.startGeneratorWindow(synthesisGeneratorUS);

	}

	public String getName() {
		return translator.translateMessage("name");
	}

	public String getAlgorithmName() {
		return translator.translateMessage("algoName");
	}

	public String getAnimationAuthor() {
		return "No\u00e9mie Catherine H\u00e9l\u00e8ne Spiller, Philipp Grenz";
	}

	public String getDescription() {
		return translator.translateMessage("algoDesc");
	}

	public String getCodeExample() {
		return exampleCode;
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return this.locale;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer arg0, Hashtable<String, Object> arg1)
			throws IllegalArgumentException {
		boolean valid = true;
		String[] input = (String[]) arg1.get("Functional Dependencies");
		for (String s : input)
			valid = valid && s.contains(" -> ");

		return valid;
	}

}
