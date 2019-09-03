package generators.cryptography;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.xml.transform.Source;

import org.apache.commons.math3.util.MathUtils;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.Circle;
import algoanim.primitives.Polyline;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.AnimationType;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CircleProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Timing;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;

/**
 * @author Felix Heller & Hendrik Jöntgen
 *
 */
public class MorseCode implements ValidatingGenerator {
	// @formatter:off
	private static final String NAME = "Morse Decode";
	private static final String AUTHORS = "Felix Heller & Hendrik Jöntgen";
	
	private static final String DESCRIPTION = ""
			+ "Der Morse Code ist nach Samuel Morse benannt, der diesen zusammen mit seinem"
			+ "\nMitarbeiter Alfred Vail schon ab 1837 entwickelte. Normalerweise wird Morsecode"
			+ "\nvisuell oder auditiv übertragenen. Im Rahmen dieser Visualisierung liegt der"
			+ "\nMorsecode jedoch als Zeichenfolge vor."
			+ "\n"
			+ "\nZur Dekodierung eines Morse Codes kann eine Art binärer Suchbaum verwendet werden:"
			+ "\n– Bei jedem empfangenen Punkt wird dieser nach links unten hinabgestiegen."
			+ "\n– Bei jedem empfangenen Strich wird nach rechts unten hinabgestiegen."
			+ "\n– Ein neuer Buchstabe wird durch einen Schrägstrich begonnen.";
	
	private static final String INTRO = ""
			+ "Im folgenden wird die Dekodierung eines Morsecodes visualisiert."
			+ "\n"
			+ "\n" + DESCRIPTION;
	
	private static final String OUTRO = ""
			+ "Wir hoffen, dass die Funktionsweise der Dekodierung eines Morsecodes durch die"
			+ "\neben gezeigte Visualisierung verdeutlicht werden konnte."
			+ "\n"
			+ "\nDer verwendete binäre Suchbaum kann beliebig erweitert werden um die Dekodierung"
			+ "\nvon Sonderzeichen zu unterstützen. Je nach Morsealphabet werden jedoch die"
			+ "\nMorsezeichen auf eine unterschiedliche Art und Weise übersetzt. Aus diesem Grund"
			+ "\nwurden sie in dieser Visualiserung außer Acht gelassen."
			+ "\n"
			+ "\nHeutzutage wurde der Morsecode zum größten Teil durch andere Chiffrier-Algorithmen"
			+ "\nbzw. Kommunikationstechnologien ersetzt. In der Schifffahrt wird er jedoch - trotz"
			+ "\nseines hohen Alters von bereits über 180 Jahren - immer noch verwendet.";
	
	private static final String SOURCE = ""
			+ "morseDecode(String[] morseCode) {" 					// 00
			+ "\n" 													// 01
			+ "\n	String result = \"\";"  						// 02
			+ "\n	currentNode = morseTree.root;"  				// 03
			+ "\n"  												// 04
			+ "\n	for (int i = 0; i < morseCode.length; i++) {"	// 05
			+ "\n		String morseSymbol = morseCode[i];" 		// 06
			+ "\n"													// 07
			+ "\n		switch (currentMorse) {" 					// 08
			+ "\n		case \".\":" 								// 09
			+ "\n			currentNode = currentNode.left;" 		// 10
			+ "\n			break;" 								// 11
			+ "\n			"  										// 12
			+ "\n		case \"-\":" 								// 13
			+ "\n			currentNode = currentNode.right;"  		// 14
			+ "\n			break;" 								// 15
			+ "\n" 													// 16
			+ "\n		case \"/\":" 								// 17
			+ "\n			result += \" \";" 						// 18
			+ "\n			break;" 								// 19
			+ "\n				" 									// 20
			+ "\n		default:" 									// 21
			+ "\n			result += currentNode.letter;" 			// 22
			+ "\n			currentNode = morseTree.root;" 			// 23
			+ "\n			break;" 								// 24
			+ "\n		}" 											// 25
			+ "\n	}" 												// 26
			+ "\n" 													// 27
			+ "\n	return result;" 								// 28
			+ "\n}";			
	// @formatter:on									// 29

	private static Language lang;
	private static MultipleChoiceQuestionModel traversePoint;
	private static MultipleChoiceQuestionModel traverseLine;
	private static MultipleChoiceQuestionModel dq1;
	private static MultipleChoiceQuestionModel dq2;
	private static MorseTree morseTree = new MorseTree();
	private static Text title;
	private static SourceCode sourceCode;
	private static SourceCodeProperties sourceCodeProps;
	private static ArrayProperties arrayProps;
	private static ArrayMarkerProperties arrayMarkerProps;
	private static TextProperties slideTitleProps;
	private static TextProperties slideTextProps;
	private static SourceCodeProperties slideTextBlockProps;
	private static Color morseTreeEdgeColor, morseTreeFillColor, morseTreeTextColor, morseTreeFillHighlightColor;
	private static String morseInput;

	@Override
	public String generate(AnimationPropertiesContainer arg0, Hashtable<String, Object> arg1) {
		lang = Language.getLanguageInstance(
				AnimationType.ANIMALSCRIPT,
				"Morse Decode",
				"Felix Heller & Hendrik Jöntgen",
				1024,
				768
		);
		lang.setStepMode(true);
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
		
		showIntro();
		setupQuestions();

		setupAlgorithmScreen();
		decode(morseInput);
		
		showOutro();

		lang.finalizeGeneration();
		return lang.toString();
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer arg0, Hashtable<String, Object> arg1) throws IllegalArgumentException {
		extractStyles(arg0);
		extractData(arg1);
		
		return morseInput.matches("[.-]{1,5}(?> [.-]{1,5})*(?> / [.-]{1,5}(?> [.-]{1,5})*)*");
	}
	
	private static void extractStyles(AnimationPropertiesContainer styles) {
		slideTextProps = (TextProperties) styles.getPropertiesByName("Folientexte-Styling");
		slideTitleProps = new TextProperties();
		slideTitleProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, slideTextProps.get(AnimationPropertiesKeys.COLOR_PROPERTY));
		slideTitleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, ((Font) slideTextProps.get(AnimationPropertiesKeys.FONT_PROPERTY)).deriveFont(18f));
		slideTextBlockProps = new SourceCodeProperties();
		slideTextBlockProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, slideTextProps.get(AnimationPropertiesKeys.COLOR_PROPERTY));
		slideTextBlockProps.set(AnimationPropertiesKeys.FONT_PROPERTY, ((Font) slideTextProps.get(AnimationPropertiesKeys.FONT_PROPERTY)).deriveFont(16f).deriveFont(Font.ITALIC));
		sourceCodeProps = (SourceCodeProperties) styles.getPropertiesByName("SourceCode-Styling");
		arrayProps = (ArrayProperties) styles.getPropertiesByName("Morse-Input-Styling");
		arrayMarkerProps = (ArrayMarkerProperties) styles.getPropertiesByName("Morse-Input-Marker-Styling");
		
		ArrayProperties ap = (ArrayProperties) styles.getPropertiesByName("Morse-Baum-Styling");
		morseTreeEdgeColor = (Color) ap.get(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY);
		morseTreeTextColor = (Color) ap.get(AnimationPropertiesKeys.COLOR_PROPERTY);
		morseTreeFillColor = (Color) ap.get(AnimationPropertiesKeys.FILL_PROPERTY);
		morseTreeFillHighlightColor = (Color) ap.get(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY);
	}
	
	private static void extractData(Hashtable<String, Object> data) {
		morseInput = (String) data.get("Morse-Input");
	}
	
	private static void showIntro() {
		lang.nextStep("Intro");
		recursivelyDrawMorseTree(morseTree.root, new Coordinates(370, 350), 180);

		title = lang.newText(new Coordinates(20, 10), "Morse Decode – Intro", "", null, slideTitleProps);
		SourceCode descriptionText = lang.newSourceCode(new Coordinates(20, 40), "description", null, slideTextBlockProps);
		for (String line : INTRO.split("\n")) {
			descriptionText.addCodeLine(line, null, 0, null);
		}	
	}
	
	private static void setupQuestions() {
		traversePoint = new MultipleChoiceQuestionModel ("traversePoint");
		traverseLine = new MultipleChoiceQuestionModel ("traverseLine");

		traversePoint.setPrompt("In welche Richtung wird der Morsebaum bei einem Punkt hinabgestiegen?");
		traversePoint.addAnswer("Nach links", 1, "Richtig, der Morsebaum steigt nach links ab.");
		traversePoint.addAnswer("Nach rechts", 0, "Falsch, der Morsebaum steigt nach links ab.");

		traverseLine.setPrompt("In welche Richtung wird der Morsebaum bei einem Strich hinabgestiegen?");
		traverseLine.addAnswer("Nach rechts", 1, "Richtig, der Morsebaum steigt nach rechts ab.");
		traverseLine.addAnswer("Nach links", 0, "Falsch, der Morsebaum steigt nach rechts ab.");
		
		dq1 = null;
		dq2 = null;
	}
	
	private static void showOutro() {
		lang.nextStep("Fazit");
		lang.hideAllPrimitivesExcept(title);

		title.setText("Morse Decode – Fazit", null, null);
		SourceCode descriptionText = lang.newSourceCode(new Coordinates(20, 40), "description", null, slideTextBlockProps);
		for (String line : OUTRO.split("\n")) {
			descriptionText.addCodeLine(line, null, 0, null);
		}
	}
	
	static void setupAlgorithmScreen() {
		lang.nextStep("Initialisierung");
		lang.hideAllPrimitivesExcept(title);
	    sourceCode = lang.newSourceCode(new Coordinates(20, 40), "sourceCode", null, sourceCodeProps);
		String[] codeLines = SOURCE.split("\n");
		for (String codeLine : codeLines) {
			 sourceCode.addCodeLine(codeLine.replace("\"", "\\\""), null, getCodeLevel(codeLine), null);
		}
		
		morseTree.root.circle.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, morseTreeFillHighlightColor, null, null);
		recursivelyDrawMorseTree(morseTree.root, new Coordinates(550, 350), 180);
		
		TextProperties titleFontProperties = new TextProperties();
		titleFontProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SERIF, Font.BOLD, 22));
		title.setText("Morse Decode – Initialisierung", null, null);
	}
	
	static void decode(String morseCode) {
		lang.nextStep("Dekodierung: 1.ter Buchstabe");
		title.setText("Morse Decode – Dekodierung: 1.ter Buchstabe", null, null);
		int letterCount = 2;

		String[] inputData = morseCode.split("");
		
		TextProperties subTitleFontProperties = new TextProperties();
		subTitleFontProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SERIF, Font.BOLD, 14));
		
		// draw input view
		lang.newText(new Coordinates(350, 150), "Eingabe:", "inputTitle", null, subTitleFontProperties);
		StringArray morseStringArray = lang.newStringArray(new Coordinates(350, 210), inputData, "morseStringArray", null, arrayProps);
		ArrayMarker inputMarker = lang.newArrayMarker(morseStringArray, 0, "InputMarker", null, arrayMarkerProps);
		
		// draw result view
		lang.newText(new Coordinates(350, 260), "Ergebnis:", "resultTitle", null, subTitleFontProperties);
		Text resultText = lang.newText(new Coordinates(350, 285), "", "resultText", null, slideTextProps);
		
		morseStringArray.highlightCell(0, null, null);
		// traverse the tree with respect to the morse input
		MorseTree.Node currentNode = morseTree.root;
		for (int i = 0; i < inputData.length; i++) {
			SourceCodeHighlighter.unhighlight();
			SourceCodeHighlighter.highlight(6, true);
			lang.nextStep();			
			String currentMorse = inputData[i];
			
			switch (currentMorse) {
			case ".":
				String[] chars = {"E", "I", "A", "S", "U", "R", "W", "H", "V", "F", "L", "P", "J"};
				if(i > 8 && dq1 == null) {
					Random rnd = new Random();
					dq1 = new MultipleChoiceQuestionModel("dynamicQuestion1");
					dq1.setPrompt("Der aktuelle Buchstabe ist " + currentNode.letter + ". Zu welchem Buchstaben wird im nächsten Schritt hinabgestiegen?");
					dq1.addAnswer(currentNode.left.letter, 1, "Richtig!");
					int c = 0;
					for (int j = 0; j < chars.length; j++) {
						if (!chars[j].equals(currentNode.left.letter)) {
							dq1.addAnswer(chars[j], 0, "Falsch, der nächste Buchstabe wäre " + currentNode.left.letter + " gewesen.");
							c++;
						}
						
						if (c > 2) {
							break;
						}
						
						j += rnd.nextInt(3);
					}
					lang.addMCQuestion(dq1);
				}
				SourceCodeHighlighter.unhighlight();
				SourceCodeHighlighter.highlight(9);
				lang.nextStep();
				SourceCodeHighlighter.unhighlight();
				SourceCodeHighlighter.highlight(10);
				currentNode.circle.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, morseTreeFillColor, null, null);
				currentNode = currentNode.left;
				currentNode.circle.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, morseTreeFillHighlightColor, null, null);
				lang.nextStep();
				SourceCodeHighlighter.unhighlight();
				SourceCodeHighlighter.highlight(11);
				lang.nextStep();
				break;
				
			case "-":
				String[] chars2 = {"T", "N", "M", "D", "K", "G", "O", "B", "X", "C", "Y", "Z", "Q"};
				if(i > 12 && dq2 == null) {
					Random rnd = new Random();
					dq2 = new MultipleChoiceQuestionModel("dynamicQuestion2");
					dq2.setPrompt("Der aktuelle Buchstabe ist " + currentNode.letter + ". Zu welchem Buchstaben wird im nächsten Schritt hinabgestiegen?");
					dq2.addAnswer(currentNode.right.letter, 1, "Richtig!");
					int c = 0;
					for (int j = 0; j < chars2.length; j++) {
						if (!chars2[j].equals(currentNode.right.letter)) {
							dq2.addAnswer(chars2[j], 0, "Falsch, der nächste Buchstabe wäre " + currentNode.right.letter + " gewesen.");
							c++;
						}

						if (c > 2) {
							break;
						}
						
						j += rnd.nextInt(3);
					}
					lang.addMCQuestion(dq2);
				}
				SourceCodeHighlighter.unhighlight();
				SourceCodeHighlighter.highlight(13);
				lang.nextStep();
				SourceCodeHighlighter.unhighlight();
				SourceCodeHighlighter.highlight(14);
				currentNode.circle.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, morseTreeFillColor, null, null);
				currentNode = currentNode.right;
				currentNode.circle.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, morseTreeFillHighlightColor, null, null);
				lang.nextStep();
				SourceCodeHighlighter.unhighlight();
				SourceCodeHighlighter.highlight(15);
				break;
				
			case "/":
				SourceCodeHighlighter.unhighlight();
				SourceCodeHighlighter.highlight(17);
				lang.nextStep();
				resultText.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) sourceCodeProps.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY), null, null);
				resultText.setText(resultText.getText() + " ", null, null);
				SourceCodeHighlighter.unhighlight();
				SourceCodeHighlighter.highlight(18);
				lang.nextStep();
				resultText.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) slideTextProps.get(AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
				SourceCodeHighlighter.unhighlight();
				SourceCodeHighlighter.highlight(19);
				lang.nextStep();
				break;
				
			default:
				if (currentNode != morseTree.root) {
					SourceCodeHighlighter.highlight(21);
					lang.nextStep();
					SourceCodeHighlighter.unhighlight();
					SourceCodeHighlighter.highlight(22);
					resultText.setText(resultText.getText() + currentNode.letter, null, null);
					resultText.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) sourceCodeProps.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY), null, null);
					lang.nextStep("Dekodierung: " + letterCount + ".ter Buchstabe");
					resultText.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) slideTextProps.get(AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
					title.setText("Morse Decode – Dekodierung: " + letterCount + ".ter Buchstabe", null, null);
					letterCount++;
					SourceCodeHighlighter.unhighlight();
				}

				SourceCodeHighlighter.highlight(23);
				currentNode.circle.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, morseTreeFillColor, null, null);
				currentNode = morseTree.root;
				currentNode.circle.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, morseTreeFillHighlightColor, null, null);
				lang.nextStep();
				SourceCodeHighlighter.unhighlight();
				SourceCodeHighlighter.highlight(24);
				lang.nextStep();
				SourceCodeHighlighter.unhighlight();
				break;
			}

			MultipleChoiceQuestionModel dynamicQuestion = new MultipleChoiceQuestionModel ("dynamicQuestion");
			dynamicQuestion.setPrompt("Welcher Buchstabe wird im nächsten Schritt der Übersetzung hinzugefügt?");
			dynamicQuestion.addAnswer("G", 0, "Falsch, der nächste Buchstabe wäre " + currentNode.letter + " gewesen.");
			dynamicQuestion.addAnswer("U", 0, "Falsch, der nächste Buchstabe wäre " + currentNode.letter + " gewesen.");
			dynamicQuestion.addAnswer("P", 0, "Falsch, der nächste Buchstabe wäre " + currentNode.letter + " gewesen.");
			dynamicQuestion.addAnswer(currentNode.letter, 1, "Richtig!");

			morseStringArray.highlightCell(i+1, null, null);
			inputMarker.increment(Timing.INSTANTEOUS, Timing.FAST);
			

			switch(i) {
				case 1:
					lang.addMCQuestion(traversePoint);
					break;
				case 4:
					lang.addMCQuestion(traverseLine);
					break;
				case 8:
					lang.addMCQuestion(traversePoint);
					break;
			}			
		}
		
		// insert last letter if necessary
		if (currentNode != morseTree.root) {
			currentNode.circle.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, morseTreeFillColor, null, null);
			resultText.setText(resultText.getText() + currentNode.letter, null, null);
			resultText.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) sourceCodeProps.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY), null, null);
			currentNode = morseTree.root;
			currentNode.circle.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, morseTreeFillHighlightColor, null, null);
			lang.nextStep();
		}
		resultText.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) slideTextProps.get(AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
		SourceCodeHighlighter.unhighlight();
		SourceCodeHighlighter.highlight(28);
	}
	
	static class SourceCodeHighlighter {
		static List<Integer> highlightedLines = new ArrayList<>();
		
		public static void highlight(int lineNo) {
			highlight(lineNo, false);
		}
		
		public static void highlightCodeBlock(int startLineNo, int endLineNo) {
			highlightCodeBlock(startLineNo, endLineNo, false);
		}
		
		public static void highlightCodeBlock(int startLineNo, int endLineNo, boolean contextColor) {
			for (int i = startLineNo; i <= endLineNo; i++) {
				highlight(i, contextColor);
			}
		}
		
		private static void highlight(int lineNo, boolean contextColor) {
			sourceCode.highlight(lineNo, 0, contextColor);
			highlightedLines.add(lineNo);
		}
		
		public static void unhighlight() {
			for (int i : highlightedLines) {
				sourceCode.unhighlight(i);
			}
			highlightedLines.clear();
		}
	}
	
	private static int getCodeLevel(String codeLine) {
		return codeLine.length() - codeLine.replace("\t", "").length();
	}

	static void recursivelyDrawMorseTree(MorseTree.Node morseTreeNode, Coordinates startPoint, int width) {
		CircleProperties cp = new CircleProperties();
		cp.set(AnimationPropertiesKeys.FILL_PROPERTY, morseTreeFillColor);
		cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		
		PolylineProperties pp = new PolylineProperties();
		pp.set(AnimationPropertiesKeys.COLOR_PROPERTY, morseTreeEdgeColor);
		
		TextProperties tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.COLOR_PROPERTY, morseTreeTextColor);
		tp.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
		
		// Create left child node
		if (morseTreeNode.left != null) {
			Coordinates childPosition = new Coordinates(startPoint.getX() - width, startPoint.getY() + 40);
			Polyline line = lang.newPolyline(new Node[] {startPoint, childPosition}, ".", null, pp);
			line.getProperties().set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
			recursivelyDrawMorseTree(morseTreeNode.left, childPosition, width / 2);
		}
		
		// Create right child node
		if (morseTreeNode.right != null) {
			Coordinates childPosition = new Coordinates(startPoint.getX() + width, startPoint.getY() + 40);
			Polyline line = lang.newPolyline(new Node[] {startPoint, childPosition}, ".", null, pp);
			line.getProperties().set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
			recursivelyDrawMorseTree(morseTreeNode.right, childPosition, width / 2);
		}
		
		// create Circle objects in the end so they are on top of the lines.
		morseTreeNode.circle = lang.newCircle(startPoint, 15, morseTreeNode.letter, null, cp);
		morseTreeNode.circle.changeColor("", morseTreeEdgeColor, null, null);
		lang.newText(new Coordinates(startPoint.getX(), startPoint.getY() - 3), morseTreeNode.letter, morseTreeNode.letter, null, tp);
	}
	
	/**
	 * Represents a binary search tree to decode a morse code text
	 */
	static class MorseTree {
		Node root;

		public static class Node {
			String letter;
			Node left, right;
			Circle circle; // Animal Circle object

			public Node(String value) {
				letter = value;
			}
		}

		public MorseTree() {
			root = new Node("");

			// @formatter:off
			// for a better presentation of a morse tree, see: https://upload.wikimedia.org/wikipedia/commons/1/19/Morse-code-tree.svg
	        String[][] morseLayers = {
	        		{                          "E",                                                       "T"                             },
	        		{           "I",                          "A",                          "N",                          "M"             },
	        		{    "S",           "U",           "R",           "W",           "D",           "K",           "G",           "O"     },
	        		{"H",    "V",   "F",     "",   "L",     "",   "P",    "J",   "B",    "X",   "C",    "Y",   "Z",    "Q",    "",     "" }
	        		
	        };
	        // @formatter:on
	        
	        // generates all {@link MorseTree#Node} objects
	        // might be not the most performant implementation, but that's not of importance in this case
	        // at least it's more elegant than root.left = E, root.left.left = I, root.left.left.left = S etc.
	        int i = 0; // layer number
	        for (String[] morseLayer : morseLayers) {
	        	int j = 0; // letter number in layer
	        	for (String letter : morseLayer) {
	        		Node node = new Node(letter);
	        		
	        		// connect parent node to the new one
	        		String parentLetter = i == 0 ? "" : morseLayers[i - 1][j / 2];
	        		boolean leftNode = j % 2 == 0;
	        		Node parentNode = findNode(parentLetter);
	        		if (leftNode) {
	        			parentNode.left = node;
	        		}
	        		else {
	        			parentNode.right = node;
	        		}
	        		j++;
	        	}
	        	i++;
	        }
		}

		/**
		 * Recursively finds the node by the given letter.
		 */
		public Node findNode(String letter) {
			return findNode(letter, root);
		}
		
		private Node findNode(String letter, Node n) {
			if (n.letter == letter) {
				return n;
			}
			
			Node leftWay = n.left != null ? findNode(letter, n.left) : null;
			if (leftWay != null) {
				return leftWay;
			}
			
			Node rightWay = n.right != null ? findNode(letter, n.right) : null;
			if (rightWay != null) {
				return rightWay;
			}
			
			return null;
		}
	}

	@Override
	public String getAlgorithmName() {
		return NAME;
	}

	@Override
	public String getAnimationAuthor() {
		return AUTHORS;
	}

	@Override
	public String getCodeExample() {
		return SOURCE;
	}

	@Override
	public Locale getContentLocale() {
		return Locale.GERMANY;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	public String getFileExtension() {
		return "asu";
	}

	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_CRYPT);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

	@Override
	public void init() {		
	}
}
