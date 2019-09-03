package generators.searching;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.Graph;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;

public class StringMatchingFiniteStateMachine implements Generator {

	private Language lang;
	private String text;
	private String pattern;

	// Induction basis for the actual algorithm
	private HashMap<Integer, HashMap<Character, Integer>> lookupTable = new HashMap<Integer, HashMap<Character, Integer>>();
	private HashSet<Character> alphabet = new HashSet<Character>();
	private LinkedList<Integer> result = new LinkedList<Integer>();

	// Auxiliary data for Visualization
	private String[][] transitionTable;

	// mapping the characters of the alphabet to Integers, which are
	// representing the indices of the transition table
	private HashMap<Character, Integer> charToIntMapping = new HashMap<Character, Integer>();

	// the adjacency matrix of the created graph, which represents the
	// finite state machine
	private int[][] adjacencyMatrix;

	// the coordinates of the graph
	private Coordinates[] coordinates;
	// variables to calculate the coordinates (see below)
	private int radius = 175;
	private int degree = 0;
	private double step;

	private String[] graphLabels;

	// Visualization data
//	private Text header;
	private TextProperties headerProps;
//	private Rect headerBackground;
	private RectProperties headerBGProps;
	private LinkedList<Text> description;
	private TextProperties descriptionProps;
	private SourceCode sourceCode;
	private SourceCodeProperties sourceCodeProps;
//	private Text patternString;
	private TextProperties patternStringProps;
	private Text iString;
	private Text kString;
	private Text cString;
	private Text firstString;
//	private Text substringOne;
	private Text secondString;
	private StringMatrix stringMatrix;
	private MatrixProperties stringMatrixProps;
	private Text stringMatrixLabel;
	private TextProperties stringMatrixLabelProps;
	private Graph graph;
	private GraphProperties graphProps;
	private StringArray stringArray;
	private ArrayMarker stringArrayMarker;
	private IntArray integerArray;
	private Text resultLabel;
	private TextProperties resultLabelProps;
	private ArrayProperties arrayProps;

	public StringMatchingFiniteStateMachine() {
		init();
	}

	public void init() {
		lang = new AnimalScript(
				"String-Matching mit endlichen Zustandsautomaten",
				"Jonas Dopf, Taylan Özden", 1200, 1000);
		lang.setStepMode(true);
	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		text = (String) primitives.get("text");
		pattern = (String) primitives.get("pattern");

		sourceCodeProps = (SourceCodeProperties) props
				.getPropertiesByName("sourceCodeProps");
		arrayProps = (ArrayProperties) props.getPropertiesByName("arrayProps");

		findPattern(pattern, text, sourceCodeProps, arrayProps);

		lang.finalizeGeneration();

		return lang.toString();
	}

	public String getName() {
		return "String-Matching mit endlichen Zustandsautomaten";
	}

	public String getAlgorithmName() {
		return "String-Matching mit endlichen Zustandsautomaten";
	}

	public String getAnimationAuthor() {
		return "Jonas Dopf, Taylan Özden";
	}

	public String getDescription() {
		return "String-Matching mit endlichen Zustandsautomaten wird zum Finden von Textesegmenten in<br>"
				+ "\n"
				+ "Strings anhand von Suchmustern genutzt. Der Algorithmus generiert hierbei zuerst einen<br>"
				+ "\n"
				+ "endlichen Zustandsautomaten über das Alphabet des Suchmusters und läuft diesen dann,<br>"
				+ "\n"
				+ "anhand des zu durchsuchenden Strings, Buchstabe für Buchstabe ab. Sobald der akzeptierende<br>"
				+ "\n"
				+ "Zustand erreocht worden ist, ist dies gleichbedeutend mit einer Übereinstimmung und der<br>"
				+ "\n"
				+ "Stratindex des Segments wird in ein zusätzliches Ergebnis-Feld kopiert.";
	}

	public String getCodeExample() {
		return "R := {}"
				+ "\n"
				+ "q := 0"
				+ "\n"
				+ "for every c in alphabet {"
				+ "\n"
				+ "  if (pattern[0] = c) {"
				+ "\n"
				+ "    lookupTable[0][c] := 1"
				+ "\n"
				+ "  }"
				+ "\n"
				+ "  else {"
				+ "\n"
				+ "    lookupTable[0][c] := 0"
				+ "\n"
				+ "   }"
				+ "\n"
				+ "}"
				+ "\n"
				+ "for every i from 1 to pattern.length() {"
				+ "\n"
				+ "  for every c in alphabet {"
				+ "\n"
				+ "    k := min(pattern.length(), i+1)"
				+ "\n"
				+ "    while (k &gt; 0 &&"
				+ "\n"
				+ "    pattern.substring(0, k) != pattern.substring((i-k)+1,i)+c) {"
				+ "\n" + "      k := k-1" + "\n" + "    }" + "\n"
				+ "    lookupTable[i][c] := k" + "\n" + "  }" + "\n" + "}"
				+ "\n" + "for every i from 0 to text.length()-1" + "\n"
				+ "  q := lookupTable[q][text[i]]" + "\n"
				+ "  if (q = pattern.length()) {" + "\n"
				+ "    add i-pattern.length()+1 to R" + "\n" + "  }" + "\n"
				+ "}";
	}

	public String getFileExtension() {
		return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	public Locale getContentLocale() {
		return Locale.GERMANY;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_SEARCH);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	/**
	 * Finds a specified pattern in a specified text
	 * 
	 * @param pattern
	 *            the specified pattern
	 * @param text
	 *            the specified text
	 */
	public void findPattern(String pattern, String text,
			SourceCodeProperties sourceCodeProps, ArrayProperties arrayProps) {

		int lengthOfPattern = pattern.length();
		int numberOfStates = lengthOfPattern + 1;
		int sizeOfAlphabet;

		// setting the header properties with its background and visualizing
		// them
		headerBGProps = new RectProperties();
		headerBGProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		headerBGProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		headerBGProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);

		lang
				.newRect(new Coordinates(50, 30), new Coordinates(575, 60),
						"headBackground", null, headerBGProps);

		headerProps = new TextProperties();
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"Monospaced", Font.BOLD, 18));

		lang.newText(new Coordinates(55, 35),
				"String matching mit endlichen Zustandsautomaten", "header",
				null, headerProps);

		lang.nextStep("1. Einleitung");

		// same for the description
		descriptionProps = new TextProperties();
		descriptionProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SERIF, Font.PLAIN, 16));

		description = new LinkedList<Text>();
		description
				.add(lang
						.newText(
								new Coordinates(50, 100),
								"String-Matching mit endlichen Zustandsautomaten, ist ein Verfahren, indem die String-Suche in einem Text, anhand von Zustandsautomaten stattfindet.",
								"description1", null, descriptionProps));
		description
				.add(lang
						.newText(
								new Coordinates(50, 120),
								"Der Automat wird anhand des Textes der durchsucht werden soll abgelaufen. Jedes Mal, wenn der akzeptierende Zustand erreicht ist,",
								"description2", null, descriptionProps));
		description.add(lang.newText(new Coordinates(50, 140),
				"ist dies gleichbedeutend mit einer Übereinstimmung.",
				"description3", null, descriptionProps));
		description
				.add(lang
						.newText(
								new Coordinates(50, 160),
								"Das Alphabet besteht aus jedem Buchstaben, den der gesuchte String enthält, welcher aber nur einmal im Alphabet vertreten sein darf.",
								"description4", null, descriptionProps));
		description
				.add(lang
						.newText(
								new Coordinates(50, 180),
								"Die Zustandsübergänge sind nun abhängig vom aktuellen Zustand und dem nächten eingelesenen Buchstaben.",
								"description5", null, descriptionProps));
		description
				.add(lang
						.newText(
								new Coordinates(50, 200),
								"Sobald eine Übereinstimmung gefunden wurde, wird in einem zusätzlichen Feld, der Startindex der Übereinstimmung gespeichert.",
								"description6", null, descriptionProps));
		description
				.add(lang
						.newText(
								new Coordinates(50, 220),
								"Die Erstellung der Zustandsübergangstabelle ist in dieser Variante des Algorithmus in O(m³) für m = Länge des gesuchten Strings.",
								"description7", null, descriptionProps));
		description
				.add(lang
						.newText(
								new Coordinates(50, 240),
								"Trotzdem ist dieser Algorithmus sehr effizient, da zur Laufzeit nur noch die Länge n des zu durchsuchenden Strings hinzuaddiert werden muss.",
								"description8", null, descriptionProps));
		description.add(lang.newText(new Coordinates(50, 260),
				"Wir erhalten also eine insgesamte Laufzeit O(m³+n)",
				"description9", null, descriptionProps));

		description
				.add(lang
						.newText(
								new Coordinates(50, 300),
								"Es gibt verschiedene Möglichkeiten die Erstellung der Zustandsübergangstabelle effizienter zu gestalten, auf die wir hier aber nicht eingehen werden.",
								"description10", null, descriptionProps));

		lang.nextStep();

		for (Text t : description) {
			t.hide();
		}

		// setting the properties of the source code and visualizing it
		this.sourceCodeProps = sourceCodeProps;

		sourceCode = lang.newSourceCode(new Coordinates(10, 120), "sourceCode",
				null, sourceCodeProps);

		sourceCode.addCodeLine("R := {}", "Step 1", 0, null);
		sourceCode.addCodeLine("q := 0", "Step 2", 0, null);
		sourceCode.addCodeLine("for every c in alphabet {", "Step 3", 0, null);
		sourceCode.addCodeLine("if (pattern[0] = c) {", "Step 4", 1, null);
		sourceCode.addCodeLine("lookupTable[0][c] := 1", "Step 5", 2, null);
		sourceCode.addCodeLine("}", "Step 6", 1, null);
		sourceCode.addCodeLine("else {", "Step 7", 1, null);
		sourceCode.addCodeLine("lookupTable[0][c] := 0", "Step 8", 2, null);
		sourceCode.addCodeLine("}", "Step 9", 1, null);
		sourceCode.addCodeLine("}", "Step 10", 0, null);
		sourceCode.addCodeLine("for every i from 1 to pattern.length() {",
				"Step 11", 0, null);
		sourceCode.addCodeLine("for every c in alphabet {", "Step 12", 1, null);
		sourceCode.addCodeLine("k := min(pattern.length(), i+1)", "Step 13", 2,
				null);
		sourceCode.addCodeLine("while (k > 0 &&", "Step 14", 2, null);
		sourceCode.addCodeLine(
				"pattern.substring(0, k) != pattern.substring((i-k)+1,i)+c) {",
				"Step 15", 2, null);
		sourceCode.addCodeLine("k := k-1", "Step 16", 3, null);
		sourceCode.addCodeLine("}", "Step 17", 2, null);
		sourceCode.addCodeLine("lookupTable[i][c] := k", "Step 18", 2, null);
		sourceCode.addCodeLine("}", "Step 19", 1, null);
		sourceCode.addCodeLine("}", "Step 20", 0, null);
		sourceCode.addCodeLine("for every i from 0 to text.length()-1",
				"Step 21", 0, null);
		sourceCode.addCodeLine("q := lookupTable[q][text[i]]", "Step 22", 1,
				null);
		sourceCode.addCodeLine("if (q = pattern.length()) {", "Step 23", 1,
				null);
		sourceCode.addCodeLine("add i-pattern.length()+1 to R", "Step 24", 2,
				null);
		sourceCode.addCodeLine("}", "Step 25", 1, null);
		sourceCode.addCodeLine("}", "Step 26", 0, null);

		lang.nextStep();

		patternStringProps = new TextProperties();
		patternStringProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.MONOSPACED, Font.PLAIN, 16));

		lang.newText(new Coordinates(50, 80),
				"gesuchter String: ".concat(pattern), "patternString", null,
				patternStringProps);

		lang.nextStep("2.1 Initialisierung der ersten Spalte");

		// create new HashMaps in the HashMap (simulating a MultiKeyHashMap)
		for (int i = 0; i < lengthOfPattern + 1; i++) {
			lookupTable.put(i, new HashMap<Character, Integer>());
		}

		// collect every character of the pattern
		for (int i = 0; i < lengthOfPattern; i++) {
			alphabet.add(pattern.charAt(i));
		}

		sizeOfAlphabet = alphabet.size();

		for (int i = 3; i <= 10; i++) {
			sourceCode.highlight("Step ".concat(String.valueOf(i)), true);
		}

		// create a transition table for the visualization as a matrix
		transitionTable = new String[sizeOfAlphabet + 1][numberOfStates + 1];

		// fill the table with empty characters
		for (int i = 0; i < sizeOfAlphabet + 1; i++) {
			for (int j = 0; j < numberOfStates + 1; j++) {
				transitionTable[i][j] = " ";
			}
		}

		transitionTable[0][0] = " ";

		// states as column
		for (int i = 0; i < numberOfStates; i++) {
			transitionTable[0][i + 1] = String.valueOf(i);
		}

		// characters as rows
		int rowIndex = 1;
		for (Character c : alphabet) {
			transitionTable[rowIndex][0] = String.valueOf(c);
			rowIndex++;
		}

		// labels of the variables for the algorithms
		iString = lang.newText(new Coordinates(700, 300), " ", "i", null);
		iString.hide();
		cString = lang.newText(new Coordinates(700, 320), " ", "c", null);
		cString.hide();
		kString = lang.newText(new Coordinates(700, 340), " ", "k", null);
		kString.hide();
		firstString = lang.newText(new Coordinates(700, 370), " ",
				"firstString", null);
		firstString.hide();
		secondString = lang.newText(new Coordinates(700, 390), " ",
				"secondString", null);
		secondString.hide();

		// setting the StringMatrix properties and visualizing it
		stringMatrixProps = new MatrixProperties();
		stringMatrixProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"Monospaced", Font.PLAIN, 12));
		stringMatrixProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				Color.BLACK);

		stringMatrix = lang.newStringMatrix(new Coordinates(700, 100),
				transitionTable, "transitionTable", null, stringMatrixProps);

		stringMatrixLabelProps = new TextProperties();
		stringMatrixLabelProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
				new Font(Font.MONOSPACED, Font.BOLD, 16));

		stringMatrixLabel = lang.newText(new Coordinates(700, 70),
				"Zustandsübergangstabelle", "stringMatrixLabel", null,
				stringMatrixLabelProps);

		lang.nextStep();

		// mapping the characters to an Integer
		int intMapping = 1;
		for (Character c : alphabet) {
			charToIntMapping.put(c, intMapping);
			intMapping++;
		}

		// Initialization of the actual algorithm
		for (Character c : alphabet) {

			cString.setText("c = ".concat(String.valueOf(c)), null, null);
			cString.changeColor("", Color.RED, null, null);
			cString.show();

			sourceCode.highlight("Step 3", false);

			lang.nextStep();

			cString.changeColor("", Color.BLACK, null, null);

			sourceCode.unhighlight("Step 3");
			sourceCode.highlight("Step 3", true);
			sourceCode.highlight("Step 4", false);

			lang.nextStep();

			sourceCode.unhighlight("Step 4");
			sourceCode.highlight("Step 4", true);

			if (pattern.charAt(0) == c) {

				sourceCode.highlight("Step 5", false);
				lookupTable.get(0).put(c, 1);

				stringMatrix.put(charToIntMapping.get(c), 0 + 1, "1", null,
						null);
				stringMatrix.highlightCell(charToIntMapping.get(c), 0 + 1,
						null, null);
				lang.nextStep();
				stringMatrix.unhighlightCell(charToIntMapping.get(c), 0 + 1,
						null, null);

			} else {

				sourceCode.highlight("Step 8", false);

				lookupTable.get(0).put(c, 0);

				stringMatrix.put(charToIntMapping.get(c), 0 + 1, "0", null,
						null);
				stringMatrix.highlightCell(charToIntMapping.get(c), 0 + 1,
						null, null);
				lang.nextStep();
				stringMatrix.unhighlightCell(charToIntMapping.get(c), 0 + 1,
						null, null);
			}
			sourceCode.unhighlight("Step 5");
			sourceCode.highlight("Step 5", true);
			sourceCode.unhighlight("Step 8");
			sourceCode.highlight("Step 8", true);
			lang.nextStep();
		}

		cString.setText("", null, null);

		for (int i = 2; i <= 9; i++) {
			sourceCode.unhighlight(i);
		}

		lang.nextStep("2.2 Initialisierung restlicher Spalten");

		for (int i = 11; i <= 20; i++) {
			sourceCode.highlight("Step ".concat(String.valueOf(i)), true);
		}

		lang.nextStep();

		// building the transition table
		int k;
		for (int i = 1; i <= lengthOfPattern; i++) {

			iString.setText("i = ".concat(String.valueOf(i)), null, null);
			iString.changeColor(null, Color.RED, null, null);
			iString.show();
			sourceCode.unhighlight("Step 18");
			sourceCode.highlight("Step 18", true);
			sourceCode.highlight("Step 11", false);

			lang.nextStep();
			iString.changeColor(null, Color.BLACK, null, null);
			for (Character c : alphabet) {

				cString.setText("c = ".concat(String.valueOf(c)), null, null);
				cString.changeColor(null, Color.RED, null, null);
				cString.show();
				sourceCode.unhighlight("Step 11");
				sourceCode.unhighlight("Step 18");
				sourceCode.highlight("Step 11", true);
				sourceCode.highlight("Step 18", true);
				sourceCode.highlight("Step 12", false);

				lang.nextStep();

				cString.changeColor(null, Color.BLACK, null, null);
				sourceCode.unhighlight("Step 12");
				sourceCode.highlight("Step 12", true);
				sourceCode.highlight("Step 13", false);
				k = Math.min(lengthOfPattern, i + 1);
				kString.setText("k = ".concat(String.valueOf(k)), null, null);
				kString.changeColor(null, Color.RED, null, null);
				kString.show();

				lang.nextStep();

				kString.changeColor(null, Color.BLACK, null, null);
				sourceCode.unhighlight("Step 13");
				sourceCode.highlight("Step 13", true);
				sourceCode.highlight("Step 14", false);
				sourceCode.highlight("Step 15", false);
				firstString.setText(
						"Substring 1: ".concat(pattern.substring(0, k)), null,
						null);
				firstString.changeColor(null, Color.RED, null, null);
				secondString.setText("Substring 2: ".concat(pattern.substring(
						(i - k) + 1, i).concat(String.valueOf(c))), null, null);
				secondString.changeColor(null, Color.RED, null, null);
				firstString.show();
				secondString.show();

				lang.nextStep();

				boolean trigger = false;

				while (k > 0
						&& !pattern.substring(0, k).equals(
								pattern.substring((i - k) + 1, i).concat(
										String.valueOf(c)))) {

					if (trigger) {
						sourceCode.unhighlight("Step 16");
						sourceCode.highlight("Step 16", true);
						sourceCode.highlight("Step 14", false);
						sourceCode.highlight("Step 15", false);
						kString.changeColor(null, Color.BLACK, null, null);
						firstString
								.setText("Substring 1: ".concat(pattern
										.substring(0, k)), null, null);
						firstString.changeColor(null, Color.RED, null, null);
						secondString.setText(
								"Substring 2 :".concat(pattern.substring(
										(i - k) + 1, i).concat(
										String.valueOf(c))), null, null);
						secondString.changeColor(null, Color.RED, null, null);

						lang.nextStep();
					}

					firstString.changeColor(null, Color.BLACK, null, null);
					secondString.changeColor(null, Color.BLACK, null, null);
					sourceCode.unhighlight("Step 14");
					sourceCode.unhighlight("Step 15");
					sourceCode.highlight("Step 14", true);
					sourceCode.highlight("Step 15", true);
					sourceCode.highlight("Step 16", false);

					k--;
					kString.setText("k = ".concat(String.valueOf(k)), null,
							null);
					kString.changeColor(null, Color.RED, null, null);

					trigger = true;

					lang.nextStep();

					if (k == 0) {

						kString.changeColor(null, Color.BLACK, null, null);
						sourceCode.unhighlight("Step 16");
						sourceCode.highlight("Step 16", true);
						sourceCode.highlight("Step 14", false);
						sourceCode.highlight("Step 15", false);

						lang.nextStep();
					}

				}

				firstString.changeColor(null, Color.BLACK, null, null);
				secondString.changeColor(null, Color.BLACK, null, null);
				kString.changeColor(null, Color.BLACK, null, null);
				sourceCode.unhighlight("Step 14");
				sourceCode.unhighlight("Step 15");
				sourceCode.unhighlight("Step 16");
				sourceCode.highlight("Step 14", true);
				sourceCode.highlight("Step 15", true);
				sourceCode.highlight("Step 16", true);
				sourceCode.highlight("Step 18", false);
				lookupTable.get(i).put(c, k);
				stringMatrix.put(charToIntMapping.get(c), i + 1,
						String.valueOf(k), null, null);
				stringMatrix.highlightCell(charToIntMapping.get(c), i + 1,
						null, null);
				lang.nextStep();
				stringMatrix.unhighlightCell(charToIntMapping.get(c), i + 1,
						null, null);
			}
		}

		sourceCode.unhighlight("Step 18");
		sourceCode.highlight("Step 18", true);

		lang.nextStep();

		for (int i = 11; i <= 20; i++) {
			sourceCode.unhighlight("Step ".concat(String.valueOf(i)));
		}

		iString.setText("", null, null);
		kString.setText("", null, null);
		cString.setText("", null, null);
		firstString.setText("", null, null);
		secondString.setText("", null, null);

		lang.nextStep();

		// setting the adjacency matrix to "0" everywhere, before we proceed
		adjacencyMatrix = new int[numberOfStates][numberOfStates];
		for (int i = 0; i < numberOfStates; i++) {
			for (int j = 0; j < numberOfStates; j++) {
				adjacencyMatrix[i][j] = 0;
			}
		}

		// setting the adjacency matrix, with the transition table
		for (int i = 0; i < numberOfStates; i++) {
			for (Character c : alphabet) {
				adjacencyMatrix[i][lookupTable.get(i).get(c)] = 1;
			}
		}

		coordinates = new Coordinates[numberOfStates];

		// calculate the euclidian coordinates on the basis of polar coordinates
		// to create a dynamic way of representing the finite state machine as a
		// circle of states independently of the number of states
		step = 360 / numberOfStates;
		for (int i = 0; i < numberOfStates; i++) {
			coordinates[i] = new Coordinates(
					800 + (int) (radius * Math.cos(Math.toRadians(degree))),
					500 + (int) (radius * Math.sin(Math.toRadians(degree))));
			degree += step;
		}

		graphLabels = new String[numberOfStates];
		for (int i = 0; i < numberOfStates; i++) {
			graphLabels[i] = String.valueOf(i);
		}

		// setting the graph properties and visualizing it
		graphProps = new GraphProperties();
		graphProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
				Color.RED);
		graphProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		graphProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		graphProps.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, Boolean.TRUE);
		graphProps
				.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, Boolean.FALSE);

		graph = lang.newGraph("graph", adjacencyMatrix, coordinates,
				graphLabels, null, graphProps);

		this.arrayProps = arrayProps;

		char[] charArrayOfText = text.toCharArray();
		String[] stringArrayOfText = new String[charArrayOfText.length];

		for (int i = 0; i < stringArrayOfText.length; i++) {
			stringArrayOfText[i] = String.valueOf(charArrayOfText[i]);
		}

		stringArray = lang.newStringArray(new Coordinates(10, 750),
				stringArrayOfText, "stringArray", null, arrayProps);
		stringArrayMarker = lang.newArrayMarker(stringArray, 0, "arrayMarker",
				null);
		stringArrayMarker.hide();

		lang.nextStep("3. Automatendurchlauf");

		for (int i = 21; i <= 26; i++) {
			sourceCode.highlight("Step ".concat(String.valueOf(i)), true);
		}

		lang.nextStep();

		stringArrayMarker.show();

		resultLabelProps = new TextProperties();
		resultLabelProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.MONOSPACED, Font.BOLD, 14));

		resultLabel = lang.newText(new Coordinates(10, 610), "Ergebnis",
				"resultLabel", null, resultLabelProps);
		resultLabel.hide();

		// Actual algorithm
		int state = 0;
		int[] resultIndices;
		for (int i = 0; i < text.length(); i++) {

			sourceCode.highlight("Step 21");

			lang.nextStep();

			sourceCode.unhighlight("Step 21");
			sourceCode.highlight("Step 21", true);
			sourceCode.highlight("Step 22");

			if (lookupTable.get(state).get(text.charAt(i)) == null) {
				graph.highlightEdge(state, 0, null, null);
			} else {
				graph.highlightEdge(state,
						lookupTable.get(state).get(text.charAt(i)), null, null);
			}

			lang.nextStep();

			sourceCode.unhighlight("Step 22");
			sourceCode.highlight("Step 22", true);

			if (lookupTable.get(state).get(text.charAt(i)) == null) {
				graph.unhighlightEdge(state, 0, null, null);
			} else {
				graph.unhighlightEdge(state,
						lookupTable.get(state).get(text.charAt(i)), null, null);
			}

			if (lookupTable.get(state).get(text.charAt(i)) == null) {
				state = 0;
			} else {
				state = lookupTable.get(state).get(text.charAt(i));
			}

			sourceCode.highlight("Step 23");

			graph.highlightNode(state, null, null);

			if (state == lengthOfPattern) {

				lang.nextStep();
				result.add(i - lengthOfPattern + 1);

				if (integerArray != null) {
					integerArray.hide();
					integerArray = null;
				}

				resultIndices = new int[result.size()];
				for (int index = 0; index < result.size(); index++) {
					resultIndices[index] = result.get(index);
				}

				resultLabel.show();
				integerArray = lang.newIntArray(new Coordinates(10, 630),
						resultIndices, "integerArray", null, arrayProps);

				sourceCode.unhighlight("Step 23");
				sourceCode.highlight("Step 23", true);
				sourceCode.highlight("Step 24");

			}

			lang.nextStep();

			sourceCode.unhighlight("Step 23");
			sourceCode.highlight("Step 23", true);
			sourceCode.unhighlight("Step 24");
			sourceCode.highlight("Step 24", true);

			graph.unhighlightNode(state, null, null);

			stringArrayMarker.increment(null, null);
		}

		stringArrayMarker.hide();

		for (int i = 21; i <= 26; i++) {
			sourceCode.unhighlight("Step ".concat(String.valueOf(i)));
		}

		lang.nextStep("4. Ende");

		sourceCode.hide();
		stringMatrixLabel.hide();
		stringMatrix.hide();
		graph.hide();

		description.clear();
		description
				.add(lang
						.newText(
								new Coordinates(50, 300),
								"Nach der Anwendung des Algorithmus, kann man nun anhand des Ergebnis-Feldes erkennen, wo die Startindizes des gesuchten Strings in dem Text liegen.",
								"description11", null, descriptionProps));

	}
	
}