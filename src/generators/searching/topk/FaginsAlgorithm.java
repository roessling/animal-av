/*
 * FaginsAlgorithm.java
 * Johannes Nachtwey, Dominik Glenz, 2014 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.searching.topk;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.network.helper.ClassName;
import interactionsupport.models.MultipleChoiceQuestionModel;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import translator.ResourceLocator;
import translator.Translator;
import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.addons.CodeView;
import algoanim.animalscript.addons.InfoBox;
import algoanim.animalscript.addons.Slide;
import algoanim.animalscript.addons.bbcode.NetworkStyle;
import algoanim.counter.view.TwoValueView;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CounterProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

/**
 * 
 * @author Dominik, Johannes
 *
 */
public class FaginsAlgorithm implements ValidatingGenerator {
	private Language lang;
	private String[][] inputRelation;
	private Text title;
	private SourceCode explanation;
	private SourceCode pseudocode;
	private Node belowTitle;
	private MatrixRepresentation listOfMaps;
	private MatrixRepresentation attributesSeen;
	private StringMatrix topK;
	private Map<String, Double>[] mapsArray;
	private int k;
	private Translator translator;
	private String resourceName;
	private List<Primitive> toBeHidden;
	private Rect positioning;
	private int highlightedLine;
	private Slide titleSlide;
	private double arrayWithOnlyValues[][];
	private String firstColumn[];
	private Locale locale;
	private MultipleChoiceQuestionModel savingQuestion = null;
	private MultipleChoiceQuestionModel scoreQuestion = null;
	private TwoValueView view;
	private TwoValueView view2;

	private TextProperties textProps;
	private SourceCodeProperties srcProps;
	private MatrixProperties matrixProps;
	private TextProperties h2Props;
	private SourceCodeProperties plainProps;
	private CounterProperties counterProps;

	public FaginsAlgorithm(String aResourceName, Locale aLocale) {
		resourceName = ClassName.getPackageAsPath(this) + aResourceName; // "resources/FaginsAlgorithm"
		locale = aLocale;
		translator = new Translator(resourceName, locale);

	}

	public boolean validateInput(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		inputRelation = (String[][]) primitives.get("Input Relation");
		k = (Integer) primitives.get("Top k");

		if (k < 1)
			throw new IllegalArgumentException(
					translator.translateMessage("WRONG_K"));

		try {
			// preparing the array for creating a matrix
			// first column contains only the row name(Typ String) inputRelation
			// are
			// the values which the user have set or which are set by default
			firstColumn = new String[inputRelation.length];
			// contains only the values of the rows
			arrayWithOnlyValues = new double[inputRelation.length][inputRelation[0].length - 1];
			int i = 0;
			for (String[] row : inputRelation) {
				firstColumn[i] = row[0];
				for (int j = 0; j < row.length - 1; j++) {
					arrayWithOnlyValues[i][j] = Double.valueOf(row[j + 1]);
				}
				i++;
			}
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(
					translator.translateMessage("WRONG_TABLE"));
		} catch (NullPointerException e) {
			throw new IllegalArgumentException(
					translator.translateMessage("WRONG_TABLE"));
		}

		// Graphical Properties like color, font, and depth

		textProps = (TextProperties) props.getPropertiesByName("Text Properties");
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				((Font) textProps.get("font")).getFamily(), Font.BOLD, 15));
		srcProps = (SourceCodeProperties) props.getPropertiesByName("Source Code Properties");
		h2Props = (TextProperties) props.getPropertiesByName("Headline Properties");
		plainProps = (SourceCodeProperties) props
				.getPropertiesByName("Plain Text Properties");
		matrixProps = (MatrixProperties) props
				.getPropertiesByName("Matrix Properties");
		counterProps = (CounterProperties) props
				.getPropertiesByName("Counter Properties");

		return true;
	}

	/**
	 * generates AnimalScript for Fagins Algorithm
	 * 
	 */
	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		init();
		showExplanationOfFaginsAlgo();
		showMatrixTransformation();
		prepareFagin();
		executeFagin();
		showFinalSlide();

		lang.finalizeGeneration();
		return lang.toString().replace("elementColor", "textColor");
	}

	/**
	 * initialize AnimalScript
	 * 
	 */
	public void init() {
		lang = new AnimalScript(translator.translateMessage("ALGO_NAME"),
				"Johannes Nachtwey, Dominik Glenz", 800, 600);
		lang.setStepMode(true);
		this.highlightedLine = -1;

		toBeHidden = new ArrayList<Primitive>();
		positioning = lang.newRect(new Coordinates(20, 20), new Coordinates(21,
				21), "positioning", null);
		positioning.hide();

		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
	}

	/**
	 * prints the explanation of Fagins Algo on the screen
	 * 
	 */
	private void showExplanationOfFaginsAlgo() {
		titleSlide = new Slide(lang, getResource("TITLESLIDE", true),
				positioning.getName(), new SearchingStyle(h2Props, plainProps,
						srcProps, false), new Object());
	}

	/**
	 * initialize Title slide
	 */
	private void initTitle() {
		title = lang.newText(new Coordinates(10, 10), "Fagin's Algorithm",
				"title", null, textProps);
		belowTitle = new Offset(20, 20, title, AnimalScript.DIRECTION_SW);
	}

	/**
	 * prepares the input matrix to print on screen prints the original
	 * (default/user given) matrix on screen
	 */
	private void showMatrixTransformation() {
		initTitle();

		// prints the by default/user given Matrix on screen
		MatrixRepresentation originalMatrix = new MatrixRepresentation(lang,
				inputRelation, belowTitle, matrixProps, true);

		// shows first Matrix and Explanation
		InfoBox explanationFirstMatrix = new InfoBox(lang, new Offset(0, 20,
				originalMatrix.m, AnimalScript.DIRECTION_SW), 2,
				translator.translateMessage("MATRIX_EXPL"));

		titleSlide.hide();
		lang.nextStep(translator.translateMessage("STEP_ONE"));
		explanationFirstMatrix.hide();

		// prints the UNSORTED converted Matrix and Explanation on screen
		this.mapsArray = convertMatrixToListOfMaps(arrayWithOnlyValues,
				firstColumn);
		listOfMaps = new MatrixRepresentation(lang, mapsArray, new Offset(50,
				0, originalMatrix.m, AnimalScript.DIRECTION_NE), matrixProps,
				true);

		// prints the explanation of matrix converting on screen
		InfoBox explanationUNSorted = new InfoBox(lang, new Offset(0, 20,
				originalMatrix.m, AnimalScript.DIRECTION_SW), 2, getResource(
				"CONVERT_EXPL", true));

		lang.nextStep();
		explanationUNSorted.hide();
		listOfMaps.m.hide();

		// prints the sorted converted Matrix and Explanation on screen
		this.mapsArray = convertMatrixToSortedListOfMaps(arrayWithOnlyValues,
				firstColumn);
		listOfMaps = new MatrixRepresentation(lang, mapsArray, new Offset(50,
				0, originalMatrix.m, AnimalScript.DIRECTION_NE), matrixProps,
				true);

		// prints the explanation of matrix converting on screen
		InfoBox explanationSorted = new InfoBox(lang, new Offset(0, 20,
				originalMatrix.m, AnimalScript.DIRECTION_SW), 2, getResource(
				"SORT_EXPL", true));
		lang.nextStep(translator.translateMessage("STEP_TWO"));
		originalMatrix.m.hide();
		explanationSorted.hide();
	}

	/**
	 * contains steps to prepare the fagins algorithm loads Pseudocode and
	 * explanation initialize TvalueList and attributesAlreadySeen
	 */
	private void prepareFagin() {
		pseudocode = CodeView.primitiveFromFile(lang,
				getResource("SOURCE", true), "pseudocode1", belowTitle, null,
				new SearchingStyle(h2Props, plainProps, srcProps, true));

		toggleCodeHighlight(3);
		listOfMaps.moveTo(AnimalScript.DIRECTION_NE, "translate",
				(Primitive) pseudocode, 50, 60);
		lang.nextStep(translator.translateMessage("STEP_THREE"));

		explanation = CodeView.primitiveFromFile(lang,
				getResource("EXPL", true), "explanation", new Offset(0, 50,
						pseudocode, AnimalScript.DIRECTION_SW), null,
				new SearchingStyle(h2Props, plainProps, srcProps, false));

		lang.nextStep(translator.translateMessage("STEP_THREE_A"));
	}

	/**
	 * excecutes Fagins algorithm
	 * 
	 */
	private void executeFagin() {
		findKObjectsInAllLists(k);
		doRandomAccess();
		calculateScore();
		displayTopK(k);
	}

	/**
	 * finds all objects in all lists
	 * 
	 * @param k
	 *            - the number of best objects
	 */
	private void findKObjectsInAllLists(int k) {
		int row = 0; // Zeilennummer

		String[] names = { translator.translateMessage("ASSIGNMENTS"),
				translator.translateMessage("ACCESSES") };

		view = lang.newCounterView(listOfMaps.getCounter(), new Offset(0, -60,
				listOfMaps.m, AnimalScript.DIRECTION_NW), counterProps, true,
				true, names);

		// attributesSeen muss initialisiert werden
		attributesSeen = new MatrixRepresentation(lang,
				listOfMaps.m.getNrCols() + 1, new Offset(0, 80, listOfMaps.m,
						AnimalScript.DIRECTION_SW), true, matrixProps, false);

		view2 = lang.newCounterView(attributesSeen.getCounter(), new Offset(0,
				20, listOfMaps.m, AnimalScript.DIRECTION_SW), counterProps,
				true, true, names);

		// Schleife über die Zeilen
		while (objectsFullySeen().size() < k) {
			toggleCodeHighlight(4);
			listOfMaps.m.highlightElemColumnRange(row + 1, 0,
					listOfMaps.m.getNrCols() - 1, null, null);
			// Schleife über die Spalten
			for (int column = 0; column < listOfMaps.m.getNrCols(); column++) {
				String element = listOfMaps.get(row, column);
				String entityName = element.split("\\,")[0];
				String entityValue = element.split("\\,")[1];

				// Wenn entityName schon in randomAccessMatrix vorhanden
				if (attributesSeen.containsElementInColumn(0, entityName)) {
					// +1, damit der Zeilenkopf nicht in die Quere kommt
					attributesSeen.set(
							attributesSeen.findElementRow(entityName, 0),
							column + 1, entityValue);
					//Damit nur beim letzten Aufruf eine neue Kapitelmarke gesetzt wird
					//TODO: Hier statt auf column zu gehen, die while-Bedingung einsetzen
					if(objectsFullySeen().size() == k)
						lang.nextStep(translator.translateMessage("STEP_THREE_B"));
					else
						lang.nextStep();
					toggleCodeHighlight(5);
				} else {
					int rownum = attributesSeen.addNewLine();
					attributesSeen.set(rownum, 0, entityName);
					// +1, damit der Zeilenkopf nicht in die Quere kommt
					attributesSeen.set(rownum, column + 1, entityValue);
					//Damit nur beim letzten Aufruf eine neue Kapitelmarke gesetzt wird
					//TODO: Hier statt auf column zu gehen, die while-Bedingung einsetzen
					if(objectsFullySeen().size() == k)
						lang.nextStep(translator.translateMessage("STEP_THREE_B"));
					else
						lang.nextStep();
					toggleCodeHighlight(5);
				}
			}
			row++;
		}
	}

	/**
	 * do a random access and highlight accessed cells
	 * 
	 */
	private void doRandomAccess() {
		// prints the explanation on screen
		explanation.hide();
		explanation = CodeView.primitiveFromFile(lang,
				getResource("RAND_EXPL", true), "explanation", new Offset(0,
						50, pseudocode, AnimalScript.DIRECTION_SW), null,
				new SearchingStyle(h2Props, plainProps, srcProps, false));

		// do random access
		List<String> fullySeen = objectsFullySeen();
		toggleCodeHighlight(6);
		for (int row = 0; row < attributesSeen.getNrRows(); row++) {
			if (!fullySeen.contains(attributesSeen.getUncounted(row, 0))) {
				for (int column = 1; column < attributesSeen.m.getNrCols(); column++) {
					if (attributesSeen.getUncounted(row, column) == " ") {
						// Nimmt das Highlighting vor

						if (listOfMaps.m.getNrCols() >= column) {
							if (listOfMaps.getRowInColumnByName(column,
									attributesSeen.get(row, 0)) != -1) {
								listOfMaps.m.highlightCell(listOfMaps
										.getRowInColumnByName(column,
												attributesSeen.getUncounted(
														row, 0)), column - 1,
										null, null);
								listOfMaps.accessInc(1);
							}

							// Highlighting RandomAccessMatrix
							attributesSeen.m.highlightCell(row + 1, 0, null,
									null);
							attributesSeen.m.highlightCell(0, column, null,
									null);
							if(!(row==0 && column==1))
								lang.nextStep();

							attributesSeen.set(
									row,
									column,
									mapsArray[column - 1]
											.get(attributesSeen.getUncounted(
													row, 0)).toString());

							// Unhighlighting RandomAccessMatrix
							attributesSeen.m.unhighlightCell(row + 1, 0, null,
									null);
							attributesSeen.m.unhighlightCell(0, column, null,
									null);
						}
						lang.nextStep();
					}
				}
			}
			if(row==0)
				lang.nextStep(translator.translateMessage("STEP_FOUR"));
		}
	}

	/**
	 * calculates the Score of objects shows explanation of calculation
	 * 
	 */
	private void calculateScore() {

		// prints explanation of scorce calculation on screen
		explanation.hide();
		explanation = CodeView.primitiveFromFile(lang,
				getResource("SCORE_EXPL", true), "explanation", new Offset(0,
						50, pseudocode, AnimalScript.DIRECTION_SW), null,
				new SearchingStyle(h2Props, plainProps, srcProps, false));

		toggleCodeHighlight(7);
		List<String> newcolumn = new ArrayList<String>();

		// Frage
		if (scoreQuestion == null) {
			Double d = attributesSeen.getRowSum(0);
			DecimalFormat df=(DecimalFormat) NumberFormat.getInstance(locale);
			df.applyLocalizedPattern("#.##");
			
			scoreQuestion = new MultipleChoiceQuestionModel("Score");
			scoreQuestion.setPrompt(translator.translateMessage("SCORE"));
			scoreQuestion.addAnswer(Double.toString(d-2.5).replace(".", ","), 0,
					translator.translateMessage("SCORE_WRONG"));
			scoreQuestion.addAnswer(Double.toString(d).replace(".", ","), 1,
					translator.translateMessage("SCORE_RIGHT"));
			scoreQuestion.addAnswer(Double.toString(d+1.3).replace(".", ","), 0,
					translator.translateMessage("SCORE_WRONG"));
			lang.addMCQuestion(scoreQuestion);
			lang.nextStep();
		}

		for (int row = 0; row < attributesSeen.getNrRows(); row++)
			newcolumn.add(Double.toString(attributesSeen.getRowSum(row)));
		int col = attributesSeen.addNewColumn("Score");
		lang.nextStep();
		for (int row = 0; row < attributesSeen.getNrRows(); row++) {
			attributesSeen.set(row, col, newcolumn.get(row));
			lang.nextStep();
		}
	}

	/**
	 * display the final result of K objects
	 * 
	 * @param k
	 */
	private void displayTopK(int k) {

		// prints explanation of final result on screen
		explanation.hide();
		explanation = CodeView.primitiveFromFile(lang,
				getResource("FINAL_EXPL", true), "explanation", new Offset(0,
						50, pseudocode, AnimalScript.DIRECTION_SW), null,
				new SearchingStyle(h2Props, plainProps, srcProps, false));

		Map<String, Double> scoreMap = new HashMap<String, Double>();
		// Namen //Score
		for (int row = 0; row < attributesSeen.getNrRows(); row++)
			scoreMap.put(
					attributesSeen.get(row, 0),
					Double.valueOf(attributesSeen.get(row,
							attributesSeen.m.getNrCols() - 1)));

		scoreMap = sortByValue(scoreMap); // jetzt ist es sortiert
		String topKArray[][] = new String[k][2];
		for (int row = 0; row < k; row++) {
			topKArray[row][0] = scoreMap.keySet().toArray(new String[1])[row];
			topKArray[row][1] = Double
					.toString(scoreMap.get(topKArray[row][0]));
		}

		topK = MatrixRepresentation
				.fastStringMatrix(lang, new Offset(0, 20, explanation,
						AnimalScript.DIRECTION_SW), topKArray, matrixProps);

		if (savingQuestion == null) {

			savingQuestion = new MultipleChoiceQuestionModel("Savings");
			savingQuestion.setPrompt(translator.translateMessage("SAVINGS"));

			savingQuestion.addAnswer(
					(inputRelation.length - attributesSeen.getNrRows() - 1)
							+ "", 0,
					translator.translateMessage("SAVINGS_WRONG"));
			savingQuestion.addAnswer(
					(inputRelation.length - attributesSeen.getNrRows()) + "",
					1, translator.translateMessage("SAVINGS_RIGHT"));
			savingQuestion.addAnswer(
					(inputRelation.length - attributesSeen.getNrRows() + 1)
							+ "", 0,
					translator.translateMessage("SAVINGS_WRONG"));
			lang.addMCQuestion(savingQuestion);
		}

		toBeHidden.add(topK);
		toBeHidden.add(explanation);
		toBeHidden.add(pseudocode);
		toBeHidden.add(attributesSeen.m);
		toBeHidden.add(listOfMaps.m);
		toBeHidden.add(title);
	}

	/**
	 * display the last final slide The final slide contains some statistics
	 * 
	 */
	private void showFinalSlide() {
		lang.nextStep(translator.translateMessage("STEP_FIVE"));
		for (Primitive p : toBeHidden)
			p.hide();
		view.hide();
		view2.hide();

		new Slide(lang, getResource("FINAL", true), positioning.getName(),
				new NetworkStyle(), k, attributesSeen.getNrRows(),
				inputRelation.length, listOfMaps.getCounter().getAccess(),
				listOfMaps.getNrElements());
	}

	public String getName() {
		return "Fagin's Algorithm";
	}

	public String getAlgorithmName() {
		return "Fagin's Algorithm";
	}

	public String getAnimationAuthor() {
		return "Johannes Nachtwey, Dominik Glenz";
	}

	public String getDescription() {
		return getFileContent(getResource("DESC", true));
	}

	public String getCodeExample() {
		return getFileContent(getResource("SOURCE", true));
	}

	/**
	 * Taken from generators.network.anim.Slide.java by Marc Werner
	 * 
	 * @author Marc Werner
	 * @param fileName
	 * @return
	 */
	private static String getFileContent(String fileName) {
		StringBuilder fileContent = new StringBuilder();
		try {
			InputStream f = ResourceLocator.getResourceLocator()
					.getResourceStream(fileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(f));
			String thisLine = null;
			while ((thisLine = br.readLine()) != null) {
				thisLine = thisLine.concat("\n");
				fileContent.append(thisLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return fileContent.toString();
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return locale;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_SEARCH);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	/**
	 * Unhighlights the previously marked line and highlights the line given by
	 * the parameter
	 * 
	 * @param newHighlightedLine
	 *            the
	 */
	public void toggleCodeHighlight(int newHighlightedLine) {
		if (highlightedLine != -1)
			pseudocode.unhighlight(highlightedLine);
		pseudocode.highlight(newHighlightedLine);
		this.highlightedLine = newHighlightedLine;
	}

	/**
	 * Errechnet aus attributesAlreadySeen die Zeilen, die bereits vollständig
	 * befüllt sind.
	 * 
	 * @return
	 */
	public List<String> objectsFullySeen() {
		List<String> result = new ArrayList<String>();
		for (int row = 0; row < attributesSeen.getNrRows(); row++) {
			boolean addToResult = true;
			for (int column = 1; column < attributesSeen.m.getNrCols(); column++)
				if (attributesSeen.get(row, column) == " ")
					addToResult = false;
			if (addToResult)
				result.add(attributesSeen.get(row, 0));
		}
		return result;
	}

	/**
	 * convertMatrixToListOfMaps
	 * 
	 * @param inputArrayMatrix
	 *            - a 2 column arrary
	 * @param names
	 *            - String array of //Was steckt da drin?
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static Map<String, Double>[] convertMatrixToSortedListOfMaps(
			double inputArrayMatrix[][], String[] names) {
		Map<String, Double>[] resultHashMap = new LinkedHashMap[inputArrayMatrix[0].length];
		for (int i = 0; i < inputArrayMatrix[0].length; i++) {
			resultHashMap[i] = new LinkedHashMap<String, Double>();
			for (int j = 0; j < inputArrayMatrix.length; j++)
				resultHashMap[i].put(names[j], inputArrayMatrix[j][i]);
			resultHashMap[i] = sortByValue(resultHashMap[i]);
		}
		return resultHashMap;
	}

	@SuppressWarnings("unchecked")
	private static Map<String, Double>[] convertMatrixToListOfMaps(
			double inputArrayMatrix[][], String[] names) {
		Map<String, Double>[] resultHashMap = new LinkedHashMap[inputArrayMatrix[0].length];
		for (int i = 0; i < inputArrayMatrix[0].length; i++) {
			resultHashMap[i] = new LinkedHashMap<String, Double>();
			for (int j = 0; j < inputArrayMatrix.length; j++)
				resultHashMap[i].put(names[j], inputArrayMatrix[j][i]);
			resultHashMap[i] = resultHashMap[i];
		}
		return resultHashMap;
	}

	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(
			Map<K, V> map) {
		List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(
				map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		Map<K, V> result = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : list)
			result.put(entry.getKey(), entry.getValue());
		return result;
	}

	/**
	 * Get a resource by its ID.
	 * 
	 * @param id
	 *            The resource ID
	 * @param languageDependant
	 *            If the resource is language depended i.e. there needs to be a
	 *            translation for the current language set to true. Otherwise
	 *            set to false.
	 * @return The resource
	 */
	protected String getResource(String id, boolean languageDependant) {
		String trans = translator.getResourceBundle().getMessage(id, false);
		String res = new String();
		if (trans == null || trans == "") {
			res = resourceName + "_" + id;
			if (languageDependant) {
				res = res.concat("." + locale);
			}
		} else {
			res = translator.translateMessage(id);
		}
		return res;
	}

}
