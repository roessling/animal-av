/*
 * Batch_FindG.java
 * Hermann Berket, 2017 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;
//import jdk.internal.dynalink.support.ClassLoaderGetterContextProvider;

public class Batch_FindG implements Generator {
	private Language lang;
	private SourceCodeProperties sourceCode;
	private String[][] possibleFeaturesMatrix;
	private String[][] trainingDataMatrix;
	private MatrixProperties matrixProps;
	private TextProperties textProps;

	public void init() {
		lang = new AnimalScript("Batch-FindG", "Hermann Berket", 1000, 800);

	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		matrixProps = (MatrixProperties) props.getPropertiesByName("matrixProps");
		sourceCode = (SourceCodeProperties) props.getPropertiesByName("sourceCode");
		possibleFeaturesMatrix = (String[][]) primitives.get("possibleFeaturesMatrix");
		trainingDataMatrix = (String[][]) primitives.get("trainingDataMatrix");
		textProps = (TextProperties) props.getPropertiesByName("textProps");

		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

		BatchFindG(possibleFeaturesMatrix, trainingDataMatrix, sourceCode, matrixProps, textProps);

		lang.finalizeGeneration();

		return lang.toString();
	}

	public String getName() {
		return "Batch-FindG";
	}

	public String getAlgorithmName() {
		return "Batch-FindG";
	}

	public String getAnimationAuthor() {
		return "Hermann Berket";
	}

	public String getDescription() {
		return " Machine Learning Algorithm which is used to find the most general hypothesis, that classifies examples "
				+ "\n" + "based on a training set. This algorithm works with \"General-to-Specific (Top-Down) Search."
				+ "\n" + "It is similar to FindG algorithm , but" + "\n"
				+ "    - FindG makes an arbitrary selection among possible refinements, taking the risk that it may lead to \""
				+ "\n" + "     an inconsistency later.\"" + "\n"
				+ "     - Batch-FindG selects next refinement based on all training examples.\";" + "\n" + " ";
	}

	public String getCodeExample() {
		return "I.    r = most general hypothesis / rule in H" + "\n" + "      F = set of all possible features" + "\n"
				+ "II.   while r covers negative examples" + "\n" + "     i)   rbest = r" + "\n"
				+ "    ii)  for each possible feature {f} element of F" + "\n" + "         a) r' = r union {f}" + "\n"
				+ "         b) if r' covers" + "\n" + "                  - all positive examples" + "\n"
				+ "                  - and fewer negative examples than rbest" + "\n" + "             then rbest = r'"
				+ "\n" + "     iii)  r = rbest" + "\n" + "III.   return rbest";
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

	public final static Timing defaultDuration = new TicksTiming(30);

	/**
	 * 
	 * @param possibleFeatures
	 * @param trainingData
	 * @return most General Hypothesis as arrays of strings
	 */
	public void BatchFindG(String[][] possibleFeatures, String[][] trainingData, SourceCodeProperties scProps,
			MatrixProperties matrixProps, TextProperties txtProps) {
		lang.setStepMode(true);

		boolean covered = true;
		SourceCodeProperties introProps = new SourceCodeProperties();
		Text mainHeader = lang.newText(new Coordinates(0, 0), "Batch-FindG Algorithm", "mainHeader", null);
		mainHeader.setFont(new Font("Monospaced", Font.BOLD, 20), null, null);
		introProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 15));
		SourceCode intro = lang.newSourceCode(new Coordinates(10, 10), "sourceCode", null, introProps);

		intro.addCodeLine("The Batch-FindG algorithm is usually used in Machine Learning topics.", null, 0, null);
		intro.addCodeLine("The purpose of this algorithm is to find the most general rule, that covers all ", null, 0,
				null);
		intro.addCodeLine("positive and no negative examples in a training data set.", null, 0, null);
		intro.addCodeLine("", null, 0, null);
		lang.nextStep();
		intro.addCodeLine("It is similar to FindG algorithm , but", null, 0, null);
		intro.addCodeLine("- FindG makes an arbitrary selection among possible refinements, taking the risk ", null, 1,
				null);
		intro.addCodeLine("  that it may lead to an inconsistency later.", null, 1, null);
		intro.addCodeLine("- Batch-FindG selects next refinement based on all training examples.", null, 1, null);
		intro.addCodeLine("", null, 0, null);

		lang.nextStep();
		intro.addCodeLine("As input the algorithm gets only a labeled training data set.", null, 0, null);
		intro.addCodeLine("Labeled means in this case, that the training examples have a ", null, 0, null);
		intro.addCodeLine("class, like true or false / yes or no.", null, 0, null);
		lang.nextStep();

		intro.hide();
		intro = null;

		intro = lang.newSourceCode(new Coordinates(10, 10), "sourceCode", null, introProps);

		intro.addCodeLine("I. In the first step of the algorithm we initialize our start rule", null, 0, null);
		intro.addCodeLine("   as the most general rule <?,?,?,?,?,....?>. That means that this ", null, 0, null);
		intro.addCodeLine("   rule covers all examples of the training data; all positive and ", null, 0, null);
		intro.addCodeLine("   negative examples.", null, 0, null);
		intro.addCodeLine("", null, 0, null);
		intro.addCodeLine("", null, 0, null);

		lang.nextStep();
		intro.addCodeLine("II. As long as our rule/ hypothesis covers negative examples on the", null, 0, null);
		intro.addCodeLine("    training data, we replace in each iteration a '?' by an attribute", null, 0, null);
		intro.addCodeLine("    so that less negative but all positive examples are covered.", null, 0, null);
		intro.addCodeLine("", null, 0, null);
		intro.addCodeLine("", null, 0, null);

		lang.nextStep();
		intro.addCodeLine("III. In the last step we return the resulting rule/hypothetis from  ", null, 0, null);
		intro.addCodeLine("the algorithm.", null, 0, null);

		lang.nextStep();
		intro.hide();
		intro = null;
		String[] rBest = new String[possibleFeatures.length];
		String[] r = new String[possibleFeatures.length];

		for (int i = 0; i < rBest.length; i++) {
			r[i] = "?";
			rBest[i] = "?";
		}

		Text pseudoCodeHeader = lang.newText(new Coordinates(0, 20), "Pseudo code", "pseudoCode", null);
		Text trainingDataHeader = lang.newText(new Coordinates(0, 350), "Training Data", "trainingData", null);
		Text possibleFeaturesHeader = lang.newText(new Coordinates(600, 350), "Possible Features", "possibleFeatures",
				null);
		Text descriptionHeader = lang.newText(new Coordinates(500, 20), "Description", "description", null);
		Text stateOfRbest = lang.newText(new Coordinates(500, 150), "State of r-best", "stateOfRbest", null);
		Text stateOfRTick = lang.newText(new Offset(150, 0, stateOfRbest, AnimalScript.DIRECTION_NE), "State of R'",
				"stateOfRTick", null);

		Text description = lang.newText(new Offset(0, 10, descriptionHeader, AnimalScript.DIRECTION_SW), "",
				"description", null, txtProps);
		Text description2 = lang.newText(new Offset(0, 10, description, AnimalScript.DIRECTION_SW), "", "description",
				null, txtProps);

		trainingDataHeader.setFont(new Font("Monospaced", Font.BOLD, 16), null, null);
		stateOfRTick.setFont(new Font("Monospaced", Font.BOLD, 16), null, null);
		possibleFeaturesHeader.setFont(new Font("Monospaced", Font.BOLD, 16), null, null);
		pseudoCodeHeader.setFont(new Font("Monospaced", Font.BOLD, 16), null, null);
		descriptionHeader.setFont(new Font("Monospaced", Font.BOLD, 16), null, null);
		stateOfRbest.setFont(new Font("Monospaced", Font.BOLD, 16), null, null);

		lang.nextStep();

		// create matrix tables
		StringMatrix tm = lang.newStringMatrix(new Offset(0, 10, trainingDataHeader, AnimalScript.DIRECTION_SW),
				trainingData, "training_data", null, matrixProps);

		StringMatrix pf = lang.newStringMatrix(new Offset(0, 10, possibleFeaturesHeader, AnimalScript.DIRECTION_S),
				possibleFeatures, "possible_Features", null, matrixProps);

		String[][] coveringMatrix = new String[trainingDataMatrix.length][1];
		StringMatrix cm = lang.newStringMatrix(new Offset(10, 2, tm, AnimalScript.DIRECTION_NE), coveringMatrix,
				"coveringMatrix", null, matrixProps);

		Text descriptionCover = lang.newText(new Offset(0, 20, tm, AnimalScript.DIRECTION_SW), "coverdescription",
				"coverDescription", null, textProps);
		descriptionCover.setText("'+' = 'covered' | '-' = 'not covered'", null, null);
		descriptionCover.setFont(new Font("Monospaced", Font.BOLD, 16), null, null);
		descriptionCover.changeColor("Text", Color.BLUE, null, null);
		descriptionCover.hide();
		
		int classPosition = 0;
		for(int i = 0; i < tm.getNrCols();i++){
			if(tm.getElement(0, i).compareTo("CLASS") == 0){
				classPosition = i;
			}
		}
		
		SourceCode sc = lang.newSourceCode(new Offset(0, 10, pseudoCodeHeader, AnimalScript.DIRECTION_S), "sourceCode",
				null, scProps);

		// add code lines
		sc.addCodeLine("I.  r = most general hypothesis / rule in H", null, 0, null);
		sc.addCodeLine("    F = set of all possible features", null, 0, null);
		sc.addCodeLine("II. while r covers negative examples", null, 0, null);
		sc.addCodeLine("i)   rbest = r", null, 1, null);
		sc.addCodeLine("ii)  for each possible feature {f} element of F", null, 1, null);
		sc.addCodeLine("a) r' = r union {f}", null, 2, null);
		sc.addCodeLine("b) if r' covers", null, 2, null);
		sc.addCodeLine("- all positive examples", null, 3, null);
		sc.addCodeLine("- fewer negative examples than rbest", null, 3, null);
		sc.addCodeLine("then rbest = r'", null, 2, null);
		sc.addCodeLine("iii)  r = rbest", null, 1, null);
		sc.addCodeLine("III.   return rbest", null, 0, null);

		pf.hide();
		sc.hide();
		// setup mostGeneralHypothesis
		ArrayProperties arrayProps = new ArrayProperties();
		arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
		arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
		arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW);

		String[] coveredPos = new String[2];
		String[] coveredNeg = new String[2];
		String[] lastCoveredNeg = new String[2];
		String[] coveringTests = new String[2];

		// counter

		coveredPos[0] = "Current rule covered positives: ";
		coveredNeg[0] = "Current rule covered negatives: ";
		lastCoveredNeg[0] = "Last best rule covered negatives: ";
		coveringTests[0] = "Tests on rule coverage: ";

		StringArray mgh = lang.newStringArray(new Offset(0, 10, stateOfRbest, AnimalScript.DIRECTION_S), rBest,
				"stateOfRbest", null, arrayProps);
		StringArray actualRuleCoveredPositive = lang.newStringArray(new Offset(0, 10, mgh, AnimalScript.DIRECTION_SW),
				coveredPos, "coveredPos", null, arrayProps);
		StringArray actualRuleCoveredNegative = lang.newStringArray(
				new Offset(0, 10, actualRuleCoveredPositive, AnimalScript.DIRECTION_SW), coveredNeg, "coveredNeg", null,
				arrayProps);
		StringArray lastRuleCoveredNegative = lang.newStringArray(
				new Offset(0, 10, actualRuleCoveredNegative, AnimalScript.DIRECTION_SW), lastCoveredNeg,
				"coveredNegold", null, arrayProps);
		StringArray coveringTest = lang.newStringArray(new Offset(0, -40, stateOfRbest, AnimalScript.DIRECTION_NW),
				coveringTests, "coveringTests", null, arrayProps);
		// start a new step after the array was created

		// hide indices
		mgh.showIndices(false, null, null);
		actualRuleCoveredPositive.showIndices(false, null, null);
		actualRuleCoveredNegative.showIndices(false, null, null);
		coveringTest.showIndices(false, null, null);
		lastRuleCoveredNegative.showIndices(false, null, null);

		mgh.hide();
		actualRuleCoveredNegative.hide();
		actualRuleCoveredPositive.hide();
		lastRuleCoveredNegative.hide();
		// Algorithm Implementation

		// Set up Start values at I.
		int allPositiveExamples = 0;
		int allNegativeExamples = 0;

		for (int i = 1; i < trainingData.length; i++) {
			if (isPositive(trainingData[i],classPosition)) {
				allPositiveExamples++;
			}
		}

		for (int i = 1; i < trainingData.length; i++) {
			if (!isPositive(trainingData[i],classPosition)) {
				allNegativeExamples++;
			}
		}

		for (int i = 0; i < cm.getNrRows(); i++) {
			cm.put(i, 0, " ", null, null);
		}

		int coveringTested = 0;
		int activeRuleCoveredPosRules = 0;
		int activeRuleCoveredNegRules = 0;

		int lastRuleCoveredNegRules = allNegativeExamples;

		String[] rTick = new String[r.length];

		StringArray rTickDisp = lang.newStringArray(new Offset(0, 10, stateOfRTick, AnimalScript.DIRECTION_S), r,
				"rTickDisplay", null, arrayProps);
		rTickDisp.showIndices(false, null, null);

		actualRuleCoveredPositive.put(1,
				String.valueOf(activeRuleCoveredPosRules) + "/" + String.valueOf(allPositiveExamples), null, null);
		actualRuleCoveredNegative.put(1,
				String.valueOf(activeRuleCoveredNegRules) + "/" + String.valueOf(allNegativeExamples), null, null);
		lastRuleCoveredNegative.put(1,
				String.valueOf(lastRuleCoveredNegRules) + "/" + String.valueOf(allNegativeExamples), null, null);
		coveringTest.put(1, String.valueOf(coveringTested), null, null);

		lang.nextStep();

		description.setText("Initialize the most general hypothesis as '<?,?,.....>'.", null, null);

		lang.nextStep();
		MultipleChoiceQuestionModel q1 = new MultipleChoiceQuestionModel("Question1");
		q1.setPrompt(
				"At first, we initialize our best rule as <?,?,,,,?>, as most general rule. What do you think which training examples does it cover?");
		q1.addAnswer("All positives and all negatives", 1, "Correct because it is the most general rule.");
		q1.addAnswer("No examples.", 0, "False, the most general rule covers all examples.");
		lang.addMCQuestion(q1);

		lang.nextStep();
		// First algo step
		sc.show();
		sc.highlight(0);
		mgh.show();

		lang.nextStep();
		description.setText("Init table with possible features.", null, null);
		sc.unhighlight(0);
		sc.highlight(1);
		pf.show();

		lang.nextStep();
		description.setText("The most general hypothesis covers all positive and negative rules.", null, null);
		sc.unhighlight(1);
		sc.highlight(2);
		actualRuleCoveredPositive.show();
		actualRuleCoveredNegative.show();
		lastRuleCoveredNegative.show();
		lastRuleCoveredNegative.highlightCell(1, null, null);

		lang.nextStep();
		// loop
		while (lastRuleCoveredNegRules > 0) {
			description.setText("Our hypothesis covers " + lastRuleCoveredNegRules + " negative examples now.", null,
					null);
			// rbest = r
			sc.unhighlight(2);
			sc.unhighlight(10);
			lastRuleCoveredNegative.unhighlightCell(1, null, null);
			actualRuleCoveredNegative.unhighlightCell(1, null, null);
			actualRuleCoveredPositive.unhighlightCell(1, null, null);

			sc.highlight(3);

			lang.nextStep();
			for (int i = 0; i < possibleFeatures.length; i++) {
				if (lastRuleCoveredNegRules == 0) {
					description.setText("We got our hypothesis that covers 0 negative examples and all positives.",
							null, null);
					sc.unhighlight(10);
					break;
				}
				for (int j = 1; j < possibleFeatures[0].length; j++) {
					actualRuleCoveredNegative.unhighlightCell(1, null, null);
					lastRuleCoveredNegative.unhighlightCell(1, null, null);
					actualRuleCoveredPositive.unhighlightCell(1, null, null);

					if (lastRuleCoveredNegRules == 0) {
						description.setText("We got our hypothesis that covers 0 negative examples and all positives.",
								null, null);
						sc.unhighlight(4);
						sc.unhighlight(5);
						sc.unhighlight(6);
						sc.unhighlight(7);
						sc.unhighlight(8);
						break;
					}

					if (r[i] != "?") // if we got this feature goto next
						continue;

					sc.unhighlight(3);
					sc.highlight(4);

					lang.nextStep();

					sc.unhighlight(4);
					sc.highlight(5);

					rTick = r.clone();
					rTick[i] = possibleFeatures[i][j];

					for (int k = 0; k < rTickDisp.getLength(); k++) {
						rTickDisp.put(k, rTick[k], null, null);
					}

					lang.nextStep();
					sc.unhighlight(5);
					sc.highlight(6);

					String hyp = String.join(",", rTick);
					description.setText("We try to cover all positive examples and less negatives than rbest", null,
							null);
					description2.setText("with <" + hyp + "> - rule.", null, null);
					pf.highlightCell(i, j, null, null);

					lang.nextStep();

					for (int l = 1; l < trainingData.length; l++) {
						description.setText("Test our hypothesis <" + hyp + "> on every training data example.", null,
								null);
						description2.hide();
						tm.highlightCellColumnRange(l, 0, trainingData[0].length - 1, null, null);

						lang.nextStep();

						coveringTested++;
						coveringTest.put(1, String.valueOf(coveringTested), null, null);

						if (coversRule(rTick, trainingData[l],classPosition) && isPositive(trainingData[l],classPosition)) {

							activeRuleCoveredPosRules++;
							actualRuleCoveredPositive.put(1, String.valueOf(activeRuleCoveredPosRules) + "/"
									+ String.valueOf(allPositiveExamples), null, null);
						} else if (coversRule(rTick, trainingData[l],classPosition) && !isPositive(trainingData[l],classPosition)) {
							activeRuleCoveredNegRules++;
							actualRuleCoveredNegative.put(1, String.valueOf(activeRuleCoveredNegRules) + "/"
									+ String.valueOf(allNegativeExamples), null, null);
						}

						tm.unhighlightCellColumnRange(l, 0, trainingData[0].length - 1, null, null);
						// lang.nextStep();
					}

					lang.nextStep();

					// recap most general hypothesis
					String[] mostGeneralHyp = new String[mgh.getLength()];
					for (int l = 0; l < mgh.getLength(); l++) {
						mostGeneralHyp[l] = mgh.getData(l);
					}

					descriptionCover.show();

					if ((activeRuleCoveredPosRules == allPositiveExamples)
							&& activeRuleCoveredNegRules < lastRuleCoveredNegRules) {

						actualRuleCoveredNegative.highlightCell(1, null, null);
						lastRuleCoveredNegative.highlightCell(1, null, null);
						actualRuleCoveredPositive.highlightCell(1, null, null);

						lang.nextStep();

						sc.unhighlight(4);
						sc.unhighlight(5);

						MultipleChoiceQuestionModel qAdd = new MultipleChoiceQuestionModel("Question2");
						qAdd.setPrompt("Should we add the actual attribute to our rBest rule?");
						qAdd.addAnswer("Yes", 1, "Correct, because it covers less negatives than the last one.");
						qAdd.addAnswer("No", 0,
								"Wrong answer, we should because the new rule covers less negatives than the last one. ");
						lang.addMCQuestion(qAdd);

						sc.highlight(6);
						sc.highlight(7);
						actualRuleCoveredNegative.unhighlightCell(1, null, null);
						lastRuleCoveredNegative.unhighlightCell(1, null, null);
						actualRuleCoveredPositive.highlightCell(1, null, null);

						for (int l = 1; l < tm.getNrRows(); l++) {
							if (coversRule(rTick, trainingData[l],classPosition) && isPositive(trainingData[l],classPosition)) {
								cm.put(l, 0, "+", null, null);
								tm.highlightCellColumnRange(l, 0, trainingData[0].length - 1, null, null);

							} else if (!coversRule(rTick, trainingData[l],classPosition) && isPositive(trainingData[l],classPosition)) {
								cm.put(l, 0, "-", null, null);
								tm.highlightCellColumnRange(l, 0, trainingData[0].length - 1, null, null);
							}
						}

						lang.nextStep();
						for (int l = 1; l < tm.getNrRows(); l++) {
							tm.unhighlightCellColumnRange(l, 0, trainingData[0].length - 1, null, null);
						}

						sc.unhighlight(7);
						sc.highlight(8);
						actualRuleCoveredNegative.highlightCell(1, null, null);
						lastRuleCoveredNegative.highlightCell(1, null, null);
						actualRuleCoveredPositive.unhighlightCell(1, null, null);

						for (int z = 0; z < cm.getNrRows(); z++) {
							cm.put(z, 0, " ", null, null);
						}

						for (int l = 1; l < tm.getNrRows(); l++) {
							String coveredMgh = "rbest:-";
							if (coversRule(mostGeneralHyp, trainingData[l],classPosition))
								coveredMgh = "rbest:+";
							if (coversRule(rTick, trainingData[l],classPosition) && !isPositive(trainingData[l],classPosition)) {
								cm.put(l, 0, "current:+|" + coveredMgh, null, null);
								tm.highlightCellColumnRange(l, 0, trainingData[0].length - 1, null, null);
							} else if (!coversRule(rTick, trainingData[l],classPosition) && !isPositive(trainingData[l],classPosition)) {
								cm.put(l, 0, "current:-|" + coveredMgh, null, null);
								tm.highlightCellColumnRange(l, 0, trainingData[0].length - 1, null, null);
							}
						}

						lang.nextStep();

						sc.unhighlight(6);
						sc.unhighlight(8);
						sc.highlight(9);
						descriptionCover.hide();

						rBest = rTick;
						lastRuleCoveredNegRules = activeRuleCoveredNegRules;
						for (int m = 0; m < rBest.length; m++)
							mgh.put(m, rBest[m], null, null);

						// sc.unhighlight(9);
						description.setText("Our hypothesis covers less negatives than the last one", null, null);
						lang.nextStep();
						for (int z = 0; z < cm.getNrRows(); z++) {
							cm.put(z, 0, " ", null, null);
						}
						for (int l = 1; l < tm.getNrRows(); l++) {
							tm.unhighlightCellColumnRange(l, 0, trainingData[0].length - 1, null, null);
						}
					} else {
						sc.highlight(6);
						sc.highlight(7);
						actualRuleCoveredNegative.unhighlightCell(1, null, null);
						lastRuleCoveredNegative.unhighlightCell(1, null, null);
						actualRuleCoveredPositive.highlightCell(1, null, null);

						for (int l = 1; l < tm.getNrRows(); l++) {
							if (coversRule(rTick, trainingData[l],classPosition) && isPositive(trainingData[l],classPosition)) {
								cm.put(l, 0, "+", null, null);
								tm.highlightCellColumnRange(l, 0, trainingData[0].length - 1, null, null);

							} else if (!coversRule(rTick, trainingData[l],classPosition) && isPositive(trainingData[l],classPosition)) {
								cm.put(l, 0, "-", null, null);
								tm.highlightCellColumnRange(l, 0, trainingData[0].length - 1, null, null);
							}
						}

						lang.nextStep();
						for (int l = 1; l < tm.getNrRows(); l++) {
							tm.unhighlightCellColumnRange(l, 0, trainingData[0].length - 1, null, null);
						}

						sc.unhighlight(7);
						sc.highlight(8);
						actualRuleCoveredNegative.highlightCell(1, null, null);
						lastRuleCoveredNegative.highlightCell(1, null, null);
						actualRuleCoveredPositive.unhighlightCell(1, null, null);

						for (int z = 0; z < cm.getNrRows(); z++) {
							cm.put(z, 0, " ", null, null);
						}

						for (int l = 1; l < tm.getNrRows(); l++) {
							String coveredMgh = "rbest:-";
							if (coversRule(mostGeneralHyp, trainingData[l],classPosition)) {
								coveredMgh = "rbest:+";
							}
							if (coversRule(rTick, trainingData[l],classPosition) && !isPositive(trainingData[l], classPosition)) {
								cm.put(l, 0, "current:+|" + coveredMgh, null, null);
								tm.highlightCellColumnRange(l, 0, trainingData[0].length - 1, null, null);
							} else if (!coversRule(rTick, trainingData[l],classPosition) && !isPositive(trainingData[l],classPosition)) {
								cm.put(l, 0, "current:-|" + coveredMgh, null, null);
								tm.highlightCellColumnRange(l, 0, trainingData[0].length - 1, null, null);
							}
						}

						String answer = "";
						if (activeRuleCoveredNegRules >= lastRuleCoveredNegRules) {
							answer = "not less negatives than rbest";
						}
						if ((activeRuleCoveredNegRules >= lastRuleCoveredNegRules)
								&& (activeRuleCoveredPosRules < allPositiveExamples)) {
							answer = answer + " and " + "not all positive examples";
						} else if (activeRuleCoveredPosRules < allPositiveExamples) {
							answer = "not all positive examples";
						}
						// TODO tell me why this rule will ignored
						description.setText("Ignore this Attribute because this rule covers", null, null);
						description2.setText(answer, null, null);
						description2.show();

						lang.nextStep();

						description2.setText("", null, null);
						description2.hide();

						for (int z = 0; z < cm.getNrRows(); z++) {
							cm.put(z, 0, " ", null, null);
						}
						for (int l = 1; l < tm.getNrRows(); l++) {
							tm.unhighlightCellColumnRange(l, 0, trainingData[0].length - 1, null, null);
						}

						descriptionCover.hide();
						sc.unhighlight(6);
						sc.unhighlight(8);
					}

					activeRuleCoveredNegRules = 0;
					activeRuleCoveredPosRules = 0;
					actualRuleCoveredPositive.put(1,
							String.valueOf(activeRuleCoveredPosRules) + "/" + String.valueOf(allPositiveExamples), null,
							null);
					actualRuleCoveredNegative.put(1,
							String.valueOf(activeRuleCoveredNegRules) + "/" + String.valueOf(allNegativeExamples), null,
							null);
					lastRuleCoveredNegative.put(1,
							String.valueOf(lastRuleCoveredNegRules) + "/" + String.valueOf(allNegativeExamples), null,
							null);

					sc.unhighlight(9);
					description.setText("", null, null);
					pf.unhighlightCell(i, j, null, null);
					actualRuleCoveredNegative.highlightCell(1, null, null);
					lastRuleCoveredNegative.highlightCell(1, null, null);
					// lang.nextStep();
				}
			}
			descriptionCover.hide();
			sc.unhighlight(4);
			sc.unhighlight(5);
			sc.unhighlight(6);
			sc.unhighlight(7);
			sc.unhighlight(8);
			sc.unhighlight(9);
			sc.highlight(10);

			if (r == rBest) {
				description.setText("There is no rule that covers all positives and no negative examples", null, null);
				covered = false;
				break;
			}

			r = rBest;
			lang.nextStep();

		}

		// rbest assignment
		sc.unhighlight(2);
		sc.unhighlight(10);
		sc.highlight(11);
		lang.nextStep();

		sc.unhighlight(11);
		MultipleChoiceQuestionModel qLast = new MultipleChoiceQuestionModel("Question3");
		qLast.setPrompt("The algorithm Batch-FindG...");
		qLast.addAnswer(
				"makes an arbitrary selection among possible refinements, taking the risk that it may lead to an inconsistency later.",
				0, "Sorry but this is how FindG works, not Batch-FindG.");
		qLast.addAnswer("selects next refinement based on all training examples.", 1, "Correct!.");
		lang.addMCQuestion(qLast);

		lang.nextStep();
		sc.hide();
		pseudoCodeHeader.hide();
		description.hide();
		descriptionHeader.hide();
		actualRuleCoveredNegative.hide();
		actualRuleCoveredPositive.hide();
		lastRuleCoveredNegative.hide();
		possibleFeaturesHeader.hide();
		pf.hide();
		tm.hide();
		trainingDataHeader.hide();
		descriptionCover.hide();
		mgh.showIndices(false, null, null);
		stateOfRTick.hide();
		rTickDisp.hide();

		StringMatrix matrix = lang.newStringMatrix(new Coordinates(0, 100), trainingData, "td", null);
		mgh.highlightCell(0, mgh.getLength() - 1, null, null);

		intro = lang.newSourceCode(new Coordinates(10, 10), "sourceCode", null, introProps);

		if (!covered) {
			intro.addCodeLine("Now you can verify that there is no most general rule that covers all ", null, 0, null);
			intro.addCodeLine("positive and no negative examples of the training data.", null, 0, null);
			lang.nextStep();
		} else {
			intro.addCodeLine("Now you can verify on your own that our generated rule", null, 0, null);
			intro.addCodeLine("covers all positive examples and no negative example.", null, 0, null);
			intro.addCodeLine("", null, 0, null);
		}
	}

	private boolean coversRule(String[] rtick, String[] rule, int classPosition) {
		int ruleIndex = 0;
		for (int i = 0; i < rtick.length; i++) {
			if(i == classPosition){
				ruleIndex++;
			}
				
			if ((rtick[i].compareTo("?") != 0) && (rtick[i].compareTo(rule[ruleIndex]) != 0))
				return false;
			ruleIndex++;
		}
		return true;
	}

	private boolean isPositive(String[] rule, int classPosition) {
		if (rule[classPosition].compareTo("yes") == 0)
			return true;
		else
			return false;
	}

}