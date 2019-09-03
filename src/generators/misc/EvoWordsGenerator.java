package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

import javax.swing.JOptionPane;

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
import algoanim.util.OffsetFromLastPosition;

/**
 * 
 * @author Andreas Altenkirch, Stefan Hoerndler
 * 
 */
public class EvoWordsGenerator implements Generator, ValidatingGenerator {

	// General
	static Language lang;
	int iterations = 0;
	boolean wordFound = false;

	// Static Coordinates
	Coordinates headerCoordinates;
	Coordinates textStartCoordinates;
	Coordinates headerStepCoordinates;
	Coordinates textStepCoordinates;
	Coordinates wordsStartCoordinates;
	Coordinates ratingsStartCoordinates;
	Coordinates targetStringArrayCoordinates;
	Coordinates wordStringArrayCoordinates;
	Coordinates targetStringArrayTextCoordinates;
	Coordinates wordStringArrayTextCoordinates;
	Coordinates newWordStringArrayCoordinates;
	Coordinates ratingStringArrayTextCoordinates;

	// Properties
	static final int textSize = 14;
	int wordXCoordinates = 400;
	TextProperties titleProperties;
	TextProperties stepTitleProperties;
	SourceCodeProperties textProperties;
	SourceCodeProperties boldTextProperties;
	ArrayProperties arrayProperties;

	// input
	private String targetWord;
	private String alphabet;
	private int numberOfTrialWords;
	private double mutationStrength;
	private int numberOfIterations;

	private static List<Word> trialWords;
	private static Random random;

	SourceCode words;
	SourceCode ratings;
	// String[] wordArray;
	Word target;

	// text
	Text headerText;
	SourceCode textSourceCode;
	Text partHeaderText;
	SourceCode partCode;
	SourceCode wordStringText;
	SourceCode targetStringText;
	SourceCode word1StringText;
	SourceCode word2StringText;
	SourceCode newWordStringText;

	StringArray targetStringArray;
	StringArray wordStringArray;
	StringArray word1StringArray;
	StringArray word2StringArray;
	StringArray newWordStringArray;

	static final String START_TEXT_INTRODUCTION = "Evolutionary algorithms are class of stochastical and eneric population-based metaheuristic optimization algorithms, "
			+ "\nwhich find a sufficiently good solution instead of an optimal solution. "
			+ "\nThey are inspired by biological evolution such as reproduction, mutation, recombination und selection.";
	private static final String START_TEXT_DETAILS = "In the generator we solve a problem to get a target word."
			+ "\nThe new words only can evaluate the distance of every char to the target. Therefore we have fourphases within an iteration."
			+ "\nThey are as follows:";
	private static final String INITIALIZE_TEXT = "In the initialize phase all points are generated at random positions.";
	private static final String EVALUATE_TEXT = "In the evalutation phase every words get a rating which is calcuted as the"
			+ "\ndistance between the characters of the new word an the target word.";
	private static final String SELECT_TEXT = "In the selection phase half of the words are deleted. Only the best ratings"
			+ "\n(i. e. the closest words to the target word) are kept.";
	private static final String MUTATE_TEXT = "In the mutation phase the characters are randomly mutated.";
	private static final String RECOMBINE_TEXT = "In the recombination phase an new word is created with a recombination of two words.";

	@Override
	public boolean validateInput(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives)
			throws IllegalArgumentException {

		stepTitleProperties = (TextProperties) props
				.getPropertiesByName("stepTitleProperties");
		titleProperties = (TextProperties) props
				.getPropertiesByName("titleProperties");
		textProperties = (SourceCodeProperties) props
				.getPropertiesByName("textProperties");
		targetWord = (String) primitives.get("targetWord");
		numberOfTrialWords = (Integer) primitives.get("numberOfTrialWords");
		mutationStrength = (double) primitives.get("mutationStrength");
		alphabet = (String) primitives.get("alphabet");
		numberOfIterations = (Integer) primitives.get("numberOfIterations");
		arrayProperties = (ArrayProperties) props
				.getPropertiesByName("arrayProperties");

		boolean isValid = true;
		StringBuilder errorMessage = new StringBuilder(
				"The following errors occurred:");

		String word = targetWord;
		for (char c : alphabet.toCharArray()) {
			word = word.replaceAll(String.valueOf(c), "");
		}
		if (!word.equals("")) {
			errorMessage
					.append("\n - targetWord must only consist of characters in alphabet");
			isValid = false;
		}

		if (numberOfIterations <= 0) {
			errorMessage
					.append("\n - numberOfIterations must be greater than 0");
			isValid = false;
		}

		if (alphabet.length() < 2 || alphabet.length() > 20) {
			errorMessage
					.append("\n - length of alphabet must be between 1 and 20");
			isValid = false;
		}

		if (targetWord.length() < 1 || targetWord.length() > 20) {
			errorMessage
					.append("\n - length of targetWord must be between 1 and 20");
			isValid = false;
		}

		if (numberOfTrialWords < 3) {
			errorMessage
					.append("\n - numberOfTrialWords must be greater than 3");
			isValid = false;
		}

		if (mutationStrength < 0 || mutationStrength > 1) {
			errorMessage
					.append("\n - mutationStrength must be between 0 and 1");
			isValid = false;
		}

		if (!isValid) {
			showError(errorMessage.toString());
			return false;
		}

		if (numberOfTrialWords > 20 || numberOfIterations > 20) {
			boolean result = showWarning("The value \"numberOfTrialWords\" may result in a large amount of animation steps. \n\nDo you want to cancel?");
			return result;
		}

		return isValid;
	}

	private void showError(String message) {
		JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), message,
				"Input validation error", JOptionPane.ERROR_MESSAGE);
	}

	private void run() {
		init();
		initializeVariables();

		showStartPage();

		initializePageContent();

		lang.nextStep();

		textSourceCode.unhighlight(0);
		partCode.unhighlight(0);

		iterations = 0;

		Word match = null;
		for (int i = 0; i < numberOfIterations; i++) {

			iterations++;
			textSourceCode.highlight(1);
			lang.nextStep();
			textSourceCode.unhighlight(1);

			if (match != null) {
				wordFound = true;
				break;
			}

			if (i == 0) { // First iteration - Detailed
				match = evaluateDetailed();
			} else {
				match = evaluate();
			}

			if (i == 0) { // First iteration - Detailed
				selectDetailed();
				mutateDetailed();
				recombineDetailed();
			} else {
				select();
				mutate();
				recombine();
			}
		}
		showEndPage();
		lang.nextStep("Result.");
	}

	private void showEndPage() {

		// hide everything
		textSourceCode.hide();
		partHeaderText.hide();
		partCode.hide();
		words.hide();
		ratings.hide();

		String endText = "Target word: " + targetWord
				+ "\n \nNumber of iterations: " + numberOfIterations
				+ "\n \nComplexity: Depends on implementation"
				+ "\n   Evaluate: NumberOfWords * NumberOfCharacters"
				+ "\n   Select: NumberOfTrialWords / 2"
				+ "\n   Mutate: NumberOfWord * NumberOfCharacters"
				+ "\n   Recombine: NumberOfTrialWords * NumberOfCharacters";
		endText += "\n \nThe last trial words were:";
		for (Word word : trialWords) {
			endText += "\n" + word.getText();
		}

		if (wordFound) {
			endText += "\nThe target word (" + targetWord + ") was found.";
		} else {
			endText += "\nThe target word (" + targetWord + ") was not found.";
		}

		SourceCode endSourceText = lang.newSourceCode(textStartCoordinates,
				"endText", null, textProperties);
		endSourceText.addMultilineCode(endText, "endTextCode", null);

		lang.nextStep();

	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {

		stepTitleProperties = (TextProperties) props
				.getPropertiesByName("stepTitleProperties");
		titleProperties = (TextProperties) props
				.getPropertiesByName("titleProperties");
		textProperties = (SourceCodeProperties) props
				.getPropertiesByName("textProperties");
		targetWord = (String) primitives.get("targetWord");
		numberOfTrialWords = (Integer) primitives.get("numberOfTrialWords");
		mutationStrength = (double) primitives.get("mutationStrength");
		alphabet = (String) primitives.get("alphabet");
		numberOfIterations = (Integer) primitives.get("numberOfIterations");
		arrayProperties = (ArrayProperties) props
				.getPropertiesByName("arrayProperties");

		run();

		return lang.toString();
	}

	public void init() {
		lang = new AnimalScript("Evolutionary algorithm - Words [EN]",
				"Andreas Altenkirch, Stefan Hoerndler", 800, 700);
		lang.setStepMode(true);
	}

	private void initializeVariables() {

		wordFound = false;
		iterations = 0;

		// Coordinates
		headerCoordinates = new Coordinates(10, 10);
		textStartCoordinates = new Coordinates(10, 20);
		headerStepCoordinates = new Coordinates(10, 200);
		textStepCoordinates = new Coordinates(10, 220);
		wordsStartCoordinates = new Coordinates(wordXCoordinates, 190);
		ratingsStartCoordinates = new Coordinates(wordXCoordinates
				+ targetWord.length() + 60, 190);

		targetStringArrayTextCoordinates = new Coordinates(wordXCoordinates, 30);
		wordStringArrayTextCoordinates = new Coordinates(wordXCoordinates, 65);
		ratingStringArrayTextCoordinates = new Coordinates(wordXCoordinates,
				100);

		targetStringArrayCoordinates = new Coordinates(wordXCoordinates + 90,
				42);
		wordStringArrayCoordinates = new Coordinates(wordXCoordinates + 90, 80);
		newWordStringArrayCoordinates = new Coordinates(wordXCoordinates + 90,
				118);

		// Text Properties
		Font titlePropertiesTemp = (Font) titleProperties.get("font");
		titleProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				titlePropertiesTemp.getFamily(), Font.BOLD, textSize + 4));

		Font stepTitlePropertiesTemp = (Font) stepTitleProperties.get("font");
		stepTitleProperties.set(AnimationPropertiesKeys.FONT_PROPERTY,
				new Font(stepTitlePropertiesTemp.getFamily(), Font.BOLD,
						textSize + 2));

		Font textPropertiesTemp = (Font) textProperties.get("font");
		textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				textPropertiesTemp.getFamily(), Font.PLAIN, textSize));

		boldTextProperties = new SourceCodeProperties();
		boldTextProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				textPropertiesTemp.getFamily(), Font.BOLD, textSize));

		random = new Random();
		trialWords = new ArrayList<Word>();

		for (int i = 0; i < numberOfTrialWords; i++) {
			String newWord = createRandomWord(targetWord.length());
			trialWords.add(new Word(newWord));
		}

		// Text
		partHeaderText = lang.newText(headerStepCoordinates, "Evaluate()", "",
				null, stepTitleProperties);
		partCode = lang.newSourceCode(textStepCoordinates, "selectCodeText",
				null, textProperties);
		partHeaderText.hide();
		partCode.hide();

		targetStringText = lang.newSourceCode(targetStringArrayTextCoordinates,
				"", null, textProperties);
		targetStringText.hide();

		wordStringText = lang.newSourceCode(wordStringArrayTextCoordinates, "",
				null, textProperties);
		wordStringText.hide();

		word1StringText = lang.newSourceCode(targetStringArrayTextCoordinates,
				"", null, textProperties);
		word1StringText.hide();

		word2StringText = lang.newSourceCode(wordStringArrayTextCoordinates,
				"", null, textProperties);
		word2StringText.hide();

		newWordStringText = lang.newSourceCode(
				ratingStringArrayTextCoordinates, "", null, textProperties);
		newWordStringText.hide();

		String[] temp = new String[1];
		wordStringArray = lang.newStringArray(wordStringArrayCoordinates, temp,
				"wordStringArray", null, arrayProperties);
		wordStringArray.hide();

		targetStringArray = lang.newStringArray(targetStringArrayCoordinates,
				temp, "targetStringArray", null, arrayProperties);
		targetStringArray.hide();

		word1StringArray = lang.newStringArray(targetStringArrayCoordinates,
				temp, "word1StringArray", null, arrayProperties);
		word1StringArray.hide();

		word2StringArray = lang.newStringArray(wordStringArrayCoordinates,
				temp, "word2StringArray", null, arrayProperties);
		word2StringArray.hide();

		newWordStringArray = lang.newStringArray(newWordStringArrayCoordinates,
				temp, "newWordStringArray", null, arrayProperties);
		newWordStringArray.hide();

	}

	private void showStartPage() {

		headerText = lang.newText(headerCoordinates,
				"Evolutionary algorithm - Words", "headerAlgo", null,
				titleProperties);

		SourceCode startIntroduction = lang.newSourceCode(
				new OffsetFromLastPosition(0, 15), "startIntroductionText",
				null, textProperties);
		startIntroduction.addMultilineCode(START_TEXT_INTRODUCTION,
				"startIntroductionText", null);
		SourceCode startText = lang.newSourceCode(new OffsetFromLastPosition(0,
				60), "startText", null, textProperties);
		startText.addMultilineCode(START_TEXT_DETAILS, "startText", null);

		lang.nextStep();

		// Evaluate
		SourceCode evaluateText = lang.newSourceCode(
				new OffsetFromLastPosition(0, 60), "evaluateStartHeaderText",
				null, boldTextProperties);
		evaluateText.addMultilineCode("Evaluate", "evaluateStartHeaderText",
				null);
		SourceCode evaluate = lang.newSourceCode(new OffsetFromLastPosition(0,
				20), "evaluateStartText", null, textProperties);
		evaluate.addMultilineCode(EVALUATE_TEXT, "evaluateStartText", null);

		lang.nextStep();

		// Select
		SourceCode selectText = lang.newSourceCode(new OffsetFromLastPosition(
				0, 40), "selectStartHeaderText", null, boldTextProperties);
		selectText.addMultilineCode("Select", "selectStartHeaderText", null);
		SourceCode select = lang.newSourceCode(
				new OffsetFromLastPosition(0, 20), "selectStartText", null,
				textProperties);
		select.addMultilineCode(SELECT_TEXT, "selectStartText", null);

		lang.nextStep();

		// Mutate
		SourceCode mutateText = lang.newSourceCode(new OffsetFromLastPosition(
				0, 42), "mutateStartHeaderText", null, boldTextProperties);
		mutateText.addMultilineCode("Mutate", "mutateStartHeaderText", null);
		SourceCode mutate = lang.newSourceCode(
				new OffsetFromLastPosition(0, 20), "mutateStartText", null,
				textProperties);
		mutate.addMultilineCode(MUTATE_TEXT, "mutateStartText", null);

		lang.nextStep();

		// Recombine
		SourceCode recombineText = lang.newSourceCode(
				new OffsetFromLastPosition(0, 22), "recombineStartHeaderText",
				null, boldTextProperties);
		recombineText.addMultilineCode("Recombine", "recombineStartHeaderText",
				null);
		SourceCode recombine = lang.newSourceCode(new OffsetFromLastPosition(0,
				20), "recombineStartText", null, textProperties);
		recombine.addMultilineCode(RECOMBINE_TEXT, "recombineStartText", null);

		lang.nextStep();
		// Hide all texts

		startIntroduction.hide();
		startText.hide();

		evaluateText.hide();
		evaluate.hide();

		selectText.hide();
		select.hide();

		mutateText.hide();
		mutate.hide();

		recombineText.hide();
		recombine.hide();

	}

	private void initializePageContent() {

		textSourceCode = lang.newSourceCode(textStartCoordinates,
				"textSourceCode", null, textProperties);
		textSourceCode.addMultilineCode(getTextSourceCode(), "", null);
		textSourceCode.highlight(0);

		lang.nextStep("Initialize words and ratings.");

		updatePartText("Initialize()", getInitializeCode(trialWords.size()));
		partCode.highlight(0);

		initializeWordsAndRatings();
		target = new Word(targetWord);

	}

	private String getInitializeCode(int size) {
		return "Find " + size + " random words.";
	}

	private void initializeWordsAndRatings() {

		if (words != null)
			words.hide();
		if (ratings != null)
			ratings.hide();

		words = lang.newSourceCode(wordsStartCoordinates, "words", null,
				textProperties);
		ratings = lang.newSourceCode(ratingsStartCoordinates, "ratings", null,
				textProperties);

		words.addCodeLine("Words", "", 0, null);
		ratings.addCodeLine("Ratings", "", 0, null);

		for (Word word : trialWords) {
			words.addCodeLine(word.getText(), "", 0, null);
		}
	}

	private Word evaluate() {
		initializeWordsAndRatings();

		textSourceCode.highlight(2);

		updatePartText("Evaluate()", getEvaluateCode('?', '?'));

		lang.nextStep("Step " + iterations + ": " + "Evaluate step.");

		// Evaluate each word
		for (Word word : trialWords) {
			int rating = 0;
			String text = word.getText();

			// count number of matching letters at the same position
			for (int i = 0; i < text.length(); i++) {

				if (text.charAt(i) == targetWord.charAt(i)) {
					rating++;
				}
			}

			word.setRating(rating);

			ratings.addCodeLine(word.getRatingString(), "", 0, null);

			if (rating == targetWord.length()) {
				lang.nextStep();
				words.hide();
				ratings.hide();
				textSourceCode.unhighlight(2);
				return word;
			}
		}

		lang.nextStep();
		words.hide();
		ratings.hide();
		textSourceCode.unhighlight(2);

		return null;
	}

	private Word evaluateDetailed() {

		initializeWordsAndRatings();

		textSourceCode.highlight(2);

		updatePartText("Evaluate()", getEvaluateCode('?', '?'));

		lang.nextStep("Step " + iterations + ": " + "Evaluate step.");

		showStringArrays();

		// Rating for new word
		SourceCode ratingNewWord = lang.newSourceCode(
				ratingStringArrayTextCoordinates, "", null, textProperties);
		ratingNewWord.addCodeLine("Rating of new word: 0", "", 0, null);

		int wordcounter = 1;
		// Evaluate each word
		partCode.highlight(0);
		for (Word word : trialWords) {
			int rating = 0;
			String text = word.getText();

			// highlight word in wordlist
			words.highlight(wordcounter);
			lang.nextStep();

			partCode.unhighlight(0);

			partCode.highlight(1);

			updateWordStringArray(word);

			lang.nextStep();
			partCode.unhighlight(1);

			// count number of matching letters at the same position
			for (int i = 0; i < text.length(); i++) {

				partCode.highlight(2);
				highlightStringCell(i);

				lang.nextStep();

				updatePartText("Evaluate()",
						getEvaluateCode(text.charAt(i), targetWord.charAt(i)));

				partCode.highlight(3);
				partCode.highlight(4);

				lang.nextStep();
				partCode.unhighlight(3);
				partCode.unhighlight(4);

				if (text.charAt(i) == targetWord.charAt(i)) {
					rating++;
					ratingNewWord = updateSourceCode(ratingNewWord,
							ratingStringArrayTextCoordinates,
							"Rating of new word: " + rating);
					highlightArrayElem(i);
					partCode.highlight(5);
				} else {
					partCode.highlight(6);
				}

				lang.nextStep();
				partCode.unhighlight(5);
				partCode.unhighlight(6);

				unhighlightStringCell(i);

			}

			partCode.highlight(6);

			// Reset highlight and arrays
			wordStringArray.hide();
			targetStringArray.hide();
			ratingNewWord = updateSourceCode(ratingNewWord,
					ratingStringArrayTextCoordinates, "Rating of new word: 0");

			word.setRating(rating);

			ratings.addCodeLine(word.getRatingString(), "", 0, null);
			ratings.highlight(wordcounter);

			lang.nextStep();
			partCode.unhighlight(2);
			partCode.unhighlight(6);
			partCode.highlight(0);

			words.unhighlight(wordcounter);
			ratings.unhighlight(wordcounter);

			wordcounter++;

			if (rating == targetWord.length()) {
				partCode.unhighlight(0);
				return word;
			}
		}

		partCode.unhighlight(0);
		lang.nextStep();
		ratingNewWord.hide();
		textSourceCode.unhighlight(2);
		words.hide();
		ratings.hide();
		hideStringArrays();
		return null;
	}

	private void select() {
		textSourceCode.highlight(3);
		showWordsandRatings(true);
		updatePartText("Select()", getSelectCode(trialWords.size()));
		lang.nextStep("Step " + iterations + ": " + "Select step.");

		Collections.sort(trialWords);
		showWordsandRatings(true);
		partCode.highlight(0);
		lang.nextStep();

		partCode.unhighlight(0);
		partCode.highlight(4);
		partCode.highlight(5);
		partCode.highlight(6);
		partCode.highlight(7);
		partCode.highlight(8);

		int numberDeletingWords = (int) Math.floor(trialWords.size() / 2);
		for (int i = 0; i < trialWords.size();) {
			words.highlight(i + 1);
			ratings.highlight(i + 1);

			if (trialWords.size() > numberOfTrialWords - numberDeletingWords) {
				trialWords.remove(i);
			} else {
				Word trialWord = trialWords.get(i);
				i++;
			}
		}

		showWordsandRatings(true);
		lang.nextStep();

		partCode.unhighlight(4);
		partCode.unhighlight(5);
		partCode.unhighlight(6);
		partCode.unhighlight(7);
		partCode.unhighlight(8);
		textSourceCode.unhighlight(3);
	}

	private void selectDetailed() {

		// Sort words
		Collections.sort(trialWords);

		showWordsandRatings(true);

		textSourceCode.highlight(3);

		updatePartText("Select()", getSelectCode(trialWords.size()));
		partCode.highlight(0);

		lang.nextStep("Step " + iterations + ": " + "Select step.");
		partCode.unhighlight(0);

		int numberDeletingWords = (int) Math.floor(trialWords.size() / 2);
		for (int i = 0; i < trialWords.size();) {

			updatePartText("Select()", getSelectCode(trialWords.size()));
			words.highlight(i + 1);
			ratings.highlight(i + 1);
			partCode.highlight(1);
			partCode.highlight(2);
			partCode.highlight(3);
			partCode.highlight(4);

			lang.nextStep();
			partCode.unhighlight(1);
			partCode.unhighlight(2);
			partCode.unhighlight(3);
			partCode.unhighlight(4);
			if (trialWords.size() > numberOfTrialWords - numberDeletingWords) {
				trialWords.remove(i);

				partCode.highlight(5);
				showWordsandRatings(true);
				lang.nextStep();
				partCode.unhighlight(5);

			} else {
				Word trialWord = trialWords.get(i);
				partCode.highlight(6);
				partCode.highlight(7);
				showWordsandRatings(true);
				lang.nextStep();
				partCode.unhighlight(6);
				partCode.unhighlight(7);
				i++;
			}
		}

		textSourceCode.unhighlight(3);
	}

	private void mutate() {
		showWordsandRatings(false);

		textSourceCode.highlight(4);

		updatePartText("Mutate()", getMutateCode());

		lang.nextStep("Step " + iterations + ": " + "Mutate step.");

		for (Word trialWord : trialWords) {
			String trialText = trialWord.getText();
			String mutatedText = "";

			for (int i = 0; i < trialText.length(); i++) {
				// mutate letter at position i if
				if (random.nextDouble() < mutationStrength) {
					mutatedText += alphabet.charAt(random.nextInt(alphabet
							.length()));
				} else {
					mutatedText += trialText.charAt(i);
				}

			}

			trialWord.setText(mutatedText);
			trialWord.setRating(0);

			showWordsandRatings(false);
		}

		lang.nextStep();
		textSourceCode.unhighlight(4);
	}

	private void mutateDetailed() {

		showWordsandRatings(false);

		textSourceCode.highlight(4);

		updatePartText("Mutate()", getMutateCode());
		partCode.highlight(0);

		lang.nextStep("Step " + iterations + ": " + "Mutate step.");
		partCode.unhighlight(0);

		showStringArrays();

		int wordcounter = 1;
		for (Word trialWord : trialWords) {
			String trialText = trialWord.getText();
			String mutatedText = "";

			words.highlight(wordcounter);
			// ratings.highlight(wordcounter);

			updateWordStringArray(trialWord);

			for (int i = 0; i < trialText.length(); i++) {

				highlightStringCell(i);
				partCode.highlight(1);
				lang.nextStep();

				partCode.unhighlight(1);
				partCode.highlight(2);

				lang.nextStep();
				partCode.unhighlight(2);

				// mutate letter at position i if
				if (random.nextDouble() < mutationStrength) {
					mutatedText += alphabet.charAt(random.nextInt(alphabet
							.length()));
					partCode.highlight(5);

				} else {
					mutatedText += trialText.charAt(i);
					partCode.highlight(3);

				}

				wordStringArray.put(i, String.valueOf(mutatedText.charAt(i)),
						null, null);

				lang.nextStep();

				partCode.unhighlight(3);
				partCode.unhighlight(5);

				unhighlightStringCell(i);
			}

			partCode.highlight(8);

			trialWord.setText(mutatedText);
			trialWord.setRating(0);

			showWordsandRatings(false);

			lang.nextStep();
			partCode.unhighlight(8);

			words.unhighlight(wordcounter);
			// ratings.unhighlight(wordcounter);
			wordcounter++;
		}
		textSourceCode.unhighlight(4);
	}

	private void recombine() {
		showWordsandRatings(false);
		textSourceCode.highlight(5);
		updatePartText("Recombine()",
				getEvaluateCode(trialWords.size(), numberOfTrialWords));
		lang.nextStep("Step " + iterations + ": " + "Recombine step.");

		List<Integer> occuredWordCombinations = new ArrayList<Integer>();
		while (trialWords.size() < numberOfTrialWords) {
			Word word1 = trialWords.get(random.nextInt(trialWords.size()));
			Word word2 = trialWords.get(random.nextInt(trialWords.size()));

			// try another w2 if w1 == w2 or combination of words occurred
			// already and there is no free word combination
			boolean combinationOccurred = occuredWordCombinations
					.contains(word1.hashCode() * word2.hashCode());
			boolean freeCombinations = trialWords.size() > occuredWordCombinations
					.size() * 2;
			while (word1.equals(word2) || combinationOccurred
					&& !freeCombinations) {
				word2 = trialWords.get(random.nextInt(trialWords.size()));
			}
			occuredWordCombinations.add(word1.hashCode() + word2.hashCode());

			// combine
			String newText = "";
			String text1 = word1.getText();
			String text2 = word2.getText();
			Word newWord = new Word("");
			for (int i = 0; i < text1.length(); i++) {
				if (random.nextBoolean()) {
					newText += text1.charAt(i);
				} else {
					newText += text2.charAt(i);
				}
				newWord.setText(newText);
			}

			trialWords.add(newWord);
			showWordsandRatings(false);

		}
		lang.nextStep();
		partCode.hide();
		partHeaderText.hide();
		textSourceCode.unhighlight(5);
	}

	private void recombineDetailed() {
		showWordsandRatings(false);
		textSourceCode.highlight(5);
		updatePartText("Recombine()",
				getEvaluateCode(trialWords.size(), numberOfTrialWords));
		targetStringText.hide();
		wordStringText.hide();
		targetStringArray.hide();
		wordStringArray.hide();

		// word1StringText.show();
		// word2StringText.show();
		// newWordStringText.show();

		word1StringText.addCodeLine("First word: ", "", 0, null);
		word2StringText.addCodeLine("Second word: ", "", 0, null);
		newWordStringText.addCodeLine("New word: ", "", 0, null);

		lang.nextStep("Step " + iterations + ": " + "Recombine step.");

		List<Integer> occuredWordCombinations = new ArrayList<Integer>();
		while (trialWords.size() < numberOfTrialWords) {
			partCode.highlight(0);
			partCode.highlight(1);
			partCode.highlight(2);
			lang.nextStep();

			updatePartText("Recombine()",
					getEvaluateCode(trialWords.size(), numberOfTrialWords));

			partCode.unhighlight(0);
			partCode.unhighlight(1);
			partCode.unhighlight(2);
			partCode.highlight(3);

			Word word1 = trialWords.get(random.nextInt(trialWords.size()));
			Word word2 = trialWords.get(random.nextInt(trialWords.size()));

			// try another w2 if w1 == w2 or combination of words occurred
			// already and there is no free word combination
			boolean combinationOccurred = occuredWordCombinations
					.contains(word1.hashCode() * word2.hashCode());
			boolean freeCombinations = trialWords.size() > occuredWordCombinations
					.size() * 2;
			while (word1.equals(word2) || combinationOccurred
					&& !freeCombinations) {
				word2 = trialWords.get(random.nextInt(trialWords.size()));
			}
			occuredWordCombinations.add(word1.hashCode() + word2.hashCode());

			updateRecombineStringArrays(word1, word2);
			int word1Line = trialWords.indexOf(word1) + 1;
			int word2Line = trialWords.indexOf(word2) + 1;
			words.highlight(word1Line);
			words.highlight(word2Line);
			word1StringArray.hide();
			word1StringArray.show();
			word2StringArray.hide();
			word2StringArray.show();
			lang.nextStep();

			// combine
			String newText = "";
			String text1 = word1.getText();
			String text2 = word2.getText();
			Word newWord = new Word("");
			for (int i = 0; i < text1.length(); i++) {

				highlightRecombineStringCell(i);
				partCode.unhighlight(3);
				partCode.highlight(4);
				lang.nextStep();

				partCode.unhighlight(4);
				partCode.highlight(5);
				lang.nextStep();

				if (random.nextBoolean()) {
					partCode.unhighlight(5);
					partCode.highlight(6);

					newText += text1.charAt(i);
					word1StringArray.highlightElem(i, null, null);

					lang.nextStep();
					partCode.unhighlight(6);

				} else {
					partCode.unhighlight(5);
					partCode.highlight(8);

					newText += text2.charAt(i);
					word2StringArray.highlightElem(i, null, null);

					lang.nextStep();
					partCode.unhighlight(8);
				}

				newWord.setText(newText);
				updateNewWordStringArray(newWord);
				unhighlightRecombineCell(i);
			}

			partCode.highlight(10);
			lang.nextStep();
			partCode.unhighlight(10);
			words.unhighlight(word1Line);
			words.unhighlight(word2Line);
			newWordStringArray.hide();
			trialWords.add(newWord);
			showWordsandRatings(false);
			partCode.highlight(11);
			lang.nextStep();
			partCode.unhighlight(11);

		}

		partCode.highlight(12);
		lang.nextStep();
		partCode.hide();
		partHeaderText.hide();

		word1StringText.hide();
		word1StringArray.hide();
		word2StringText.hide();
		word2StringArray.hide();
		newWordStringText.hide();
		newWordStringArray.hide();
		textSourceCode.unhighlight(5);
	}

	public String getName() {
		return "Evolutionary algorithm - Words [EN]";
	}

	public String getAlgorithmName() {
		return "Evolutionary algorithm";
	}

	public String getAnimationAuthor() {
		return "Andreas Altenkirch, Stefan Hoerndler";
	}

	public String getDescription() {
		return "An evolutionary algorithm is inspired by biological evolution such as reproduction, mutation, recombination und selection. There are stochastical algorithms so they don't find the best solution but a sufficiently good solution."
				+ "\n"
				+ "In the generator we solve a problem to get a target word. The new words only can evaluate the distance of every char to the target. Therefore we have fourphases within an iteration. They are as follows:"
				+ "\n"
				+ "\n"
				+ "Evaluate:"
				+ "\n"
				+ "In the evalutation phase every words get a rating which is calcuted as the distance between the characters of the new word an the target word."
				+ "\n"
				+ "\n"
				+ "Select:"
				+ "\n"
				+ "In the selection phase half of the words are deleted. Only the best ratings (i. e. the closest words to the target word) are kept."
				+ "\n"
				+ "\n"
				+ "Mutate:"
				+ "\n"
				+ "In the mutation phase the characters of the words are randomly mutated."
				+ "\n"
				+ "\n"
				+ "Recombine:"
				+ "\n"
				+ "In the recombination phase an new word is created with a recombination of two words.";
	}

	public String getCodeExample() {
		return "Initialize" + "\n" + "For each iteration do {" + "\n"
				+ "   Evaluate" + "\n" + "   Select" + "\n" + "   Mutate"
				+ "\n" + "   Recombine" + "\n" + "}";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.ENGLISH;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	// ///////////////////////////////////
	// ////////// AUXILIARIES ////////////
	// ///////////////////////////////////

	private String getTextSourceCode() {
		return "Initialize()" // 0
				+ "\nWhile (targetWord (=" + targetWord + ") not found) {" // 1
				+ "\n   Evaluate()" // 2
				+ "\n   Select()" // 3
				+ "\n   Mutate()" // 4
				+ "\n   Recombine()" // 5
				+ "\n}"; // 6;
	}

	private boolean showWarning(String message) {
		int result = JOptionPane.showConfirmDialog(JOptionPane.getRootFrame(),
				message, "Performance warning", JOptionPane.YES_NO_OPTION);
		return result != JOptionPane.YES_OPTION;
	}

	private String getEvaluateCode(char newWord, char targetChar) {

		partCode.hide();

		String textEvaluateCode = "for all words do until targetWord (="+targetWord+") found {"
				+ "\n   initializeWord(); // rating = 0"
				+ "\n   for each char{"
				+ "\n      if (charOfNewWord equals charOfTargetWord){"
				+ "\n      // charOfNewWord=" + newWord + ", charOfTargetWord="
				+ targetChar + "\n         rating++;" + "\n      }" + "\n   }"
				+ "\n}";

		return textEvaluateCode;
	}

	private void highlightStringCell(int i) {
		targetStringArray.highlightCell(i, null, null);
		wordStringArray.highlightCell(i, null, null);
	}

	private void unhighlightStringCell(int i) {
		targetStringArray.unhighlightCell(i, null, null);
		wordStringArray.unhighlightCell(i, null, null);
	}

	private void highlightArrayElem(int i) {
		targetStringArray.highlightElem(i, null, null);
		wordStringArray.highlightElem(i, null, null);
	}

	private void highlightRecombineStringCell(int i) {
		word1StringArray.highlightCell(i, null, null);
		word2StringArray.highlightCell(i, null, null);
	}

	private void unhighlightRecombineCell(int i) {
		word1StringArray.unhighlightCell(i, null, null);
		word2StringArray.unhighlightCell(i, null, null);
	}

	private void updateWordStringArray(Word word) {
		wordStringArray.hide();
		wordStringArray = lang.newStringArray(wordStringArrayCoordinates,
				word.getTextArray(), "wordStringArray", null, arrayProperties);
		targetStringArray.hide();
		targetStringArray = lang.newStringArray(targetStringArrayCoordinates,
				target.getTextArray(), "targetStringArray", null,
				arrayProperties);
	}

	private void updateRecombineStringArrays(Word word1, Word word2) {
		word1StringArray.hide();
		word1StringArray = lang
				.newStringArray(wordStringArrayCoordinates,
						word1.getTextArray(), "word1StringArray", null,
						arrayProperties);

		word2StringArray.hide();
		word2StringArray = lang
				.newStringArray(targetStringArrayCoordinates,
						word2.getTextArray(), "word2StringArray", null,
						arrayProperties);
	}

	private void updateNewWordStringArray(Word newWord) {
		newWordStringArray.hide();
		newWordStringArray = lang.newStringArray(newWordStringArrayCoordinates,
				newWord.getTextArray(), "newWordStringArray", null,
				arrayProperties);
	}

	private void showWordsandRatings(boolean showRatings) {

		if (words != null)
			words.hide();
		if (ratings != null)
			ratings.hide();

		words = lang.newSourceCode(wordsStartCoordinates, "words", null,
				textProperties);
		words.addCodeLine("Words", "", 0, null);

		if (showRatings) {
			ratings = lang.newSourceCode(ratingsStartCoordinates, "ratings",
					null, textProperties);
			ratings.addCodeLine("Ratings", "", 0, null);
		}

		for (Word word : trialWords) {
			words.addCodeLine(word.getText(), "", 0, null);

			if (showRatings) {
				ratings.addCodeLine(word.getRatingString(), "", 0, null);
			}
		}
	}

	private String getMutateCode() {
		String textMutateCode = "for all words do {"
				+ "\n   for each char do {"
				+ "\n      if (randomDouble < mutationStrength(="
				+ mutationStrength + "){" + "\n         Keep char"
				+ "\n      } else {"
				+ "\n         Replace char with random char" + "\n      }"
				+ "\n   }" + "\nUpdate wordlist" + "\n}";
		return textMutateCode;
	}

	private String getEvaluateCode(int trialWords, int numberOfWords) {
		return "// trialWords = " + trialWords + "\n// numberOfWords = "
				+ numberOfWords + "\nwhile (trialWords < numberOfWords) {"
				+ "\n   choose two disjunct words" + "\n   for each char {"
				+ "\n      if (randomBoolean) {"
				+ "\n         newWord += char of second word"
				+ "\n      } else {"
				+ "\n         newWord += char of first word" + "\n      }"
				+ "\n   }" + "\n   add newWord to trialWords" + "\n}";
	}

	private void showStringArrays() {
		// Target word
		// targetStringText.hide();
		targetStringText = lang.newSourceCode(targetStringArrayTextCoordinates,
				"", null, textProperties);
		targetStringText.addCodeLine("Target word: ", "", 0, null);

		// new word
		// wordStringText.hide();
		wordStringText = lang.newSourceCode(wordStringArrayTextCoordinates, "",
				null, textProperties);
		wordStringText.addCodeLine("New word: ", "", 0, null);
	}

	private void hideStringArrays() {
		targetStringText.hide();
		wordStringText.hide();
	}

	private String getSelectCode(int numberOfWords) {
		String textSelectCode = "Sort words descending" + "\n// trialWords = "
				+ numberOfTrialWords
				+ "\n// numberDeletingWords = roundOff(trialwords/2) = "
				+ (int) Math.floor(trialWords.size() / 2)
				+ "\n// numberOfWords = " + numberOfWords
				+ "\nif (trialWords > numberOfWords - numberDeletingWords) {" // 0
				+ "\n   Remove word" + "\n} else {" + "\n   Keep word" + "\n}";
		return textSelectCode;
	}

	private void updatePartText(String headerString, String evaluateCode) {

		partHeaderText.hide();
		partCode.hide();
		partHeaderText = lang.newText(headerStepCoordinates, headerString, "",
				null, stepTitleProperties);
		partCode = lang.newSourceCode(textStepCoordinates, "selectCodeText",
				null, textProperties);
		partCode.addMultilineCode(evaluateCode, "evaluateCode", null);

	}

	private String createRandomWord(int length) {
		String word = "";
		for (int i = 0; i < length; i++) {
			word += alphabet.charAt(random.nextInt(alphabet.length()));
		}
		return word;
	}

	public String[] toStringArray(String string) {

		String[] stringArray = string.split("");

		// Bugfix for Java 7: "test".split("") results in ["", "t", "e", "s",
		// "t"]
		if (stringArray[0].equals("")) {
			stringArray = Arrays
					.copyOfRange(stringArray, 1, stringArray.length);
		}

		return stringArray;
	}

	private SourceCode updateSourceCode(SourceCode sourceCodeObject,
			Coordinates coordinates, String newText) {
		sourceCodeObject.hide();
		sourceCodeObject = lang.newSourceCode(coordinates, "", null,
				textProperties);
		sourceCodeObject.addCodeLine(newText, "", 0, null);
		return sourceCodeObject;
	}

	private class Word implements Comparable<Word> {
		private UUID id = UUID.randomUUID();
		private String[] text;
		private int rating;

		public Word(String text) {
			this.text = toStringArray(text);
		}

		public String[] getTextArray() {
			return this.text;
		}

		public String getText() {
			String text = "";
			for (String character : this.text) {
				text += character;
			}
			return text;
		}

		public void setText(String text) {
			this.text = toStringArray(text);
		}

		public int getRating() {
			return rating;
		}

		public String getRatingString() {
			return String.valueOf(rating);
		}

		public void setRating(int rating) {
			this.rating = rating;
		}

		@Override
		public int compareTo(Word other) {
			return this.getRating() - other.getRating();
		}

		public int hashCode() {
			return this.id.hashCode();
		}
	}
}
