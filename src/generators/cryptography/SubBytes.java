package generators.cryptography;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.QuestionGroupModel;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Random;

import javax.swing.JOptionPane;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Group;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Offset;

public class SubBytes implements ValidatingGenerator {

	private Language lang;
	private boolean correctedState;
	
	// External input
	private String[][] state;
	// S-box
	private Color sboxBackgroundColor;
	private Color sboxBorderColor;
	private TextProperties sboxTextProperties;
	private TextProperties sboxIndexTextProps;
	// State
	private Color stateBackgroundColor;
	private Color stateBorderColor;
	private TextProperties stateTextProps;
	// Text properties
	private SourceCodeProperties textProps;
	// Code properties
	private SourceCodeProperties codeProps;
	// Animation properties
	private Color highlightElementColor;
	private Color highlightSboxEntryColor;
	private Color highlightProcessedEntryColor;
	private Color highlightSboxIndexFontColor;
	// Questions
	private boolean questions;

	// SBOX string variant
	private static final String[][] SBOX = {
		{"63", "7C", "77", "7B", "F2", "6B", "6F", "C5", "30", "01", "67", "2B", "FE", "D7", "AB", "76"},
		{"CA", "82", "C9", "7D", "FA", "59", "47", "F0", "AD", "D4", "A2", "AF", "9C", "A4", "72", "C0"},
		{"B7", "FD", "93", "26", "36", "3F", "F7", "CC", "34", "A5", "E5", "F1", "71", "D8", "31", "15"},
		{"04", "C7", "23", "C3", "18", "96", "05", "9A", "07", "12", "80", "E2", "EB", "27", "B2", "75"},
		{"09", "83", "2C", "1A", "1B", "6E", "5A", "A0", "52", "3B", "D6", "B3", "29", "E3", "2F", "84"},
		{"53", "D1", "00", "ED", "20", "FC", "B1", "5B", "6A", "CB", "BE", "39", "4A", "4C", "58", "CF"},
		{"D0", "EF", "AA", "FB", "43", "4D", "33", "85", "45", "F9", "02", "7F", "50", "3C", "9F", "A8"},
		{"51", "A3", "40", "8F", "92", "9D", "38", "F5", "BC", "B6", "DA", "21", "10", "FF", "F3", "D2"},
		{"CD", "0C", "13", "EC", "5F", "97", "44", "17", "C4", "A7", "7E", "3D", "64", "5D", "19", "73"},
		{"60", "81", "4F", "DC", "22", "2A", "90", "88", "46", "EE", "B8", "14", "DE", "5E", "0B", "DB"},
		{"E0", "32", "3A", "0A", "49", "06", "24", "5C", "C2", "D3", "AC", "62", "91", "95", "E4", "79"},
		{"E7", "C8", "37", "6D", "8D", "D5", "4E", "A9", "6C", "56", "F4", "EA", "65", "7A", "AE", "08"},
		{"BA", "78", "25", "2E", "1C", "A6", "B4", "C6", "E8", "DD", "74", "1F", "4B", "BD", "8B", "8A"},
		{"70", "3E", "B5", "66", "48", "03", "F6", "0E", "61", "35", "57", "B9", "86", "C1", "1D", "9E"},
		{"E1", "F8", "98", "11", "69", "D9", "8E", "94", "9B", "1E", "87", "E9", "CE", "55", "28", "DF"},
		{"8C", "A1", "89", "0D", "BF", "E6", "42", "68", "41", "99", "2D", "0F", "B0", "54", "BB", "16"}};


	public void init(){
		lang = new AnimalScript("Advanced Encryption Standard - SubBytes", "Bernd Conrad, Sandra Amend", 800, 600);
		lang.setStepMode(true);
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
	}

	public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
		
		// If the state input was invalid and a new one was generated don't parse the old one
		if (!correctedState) {
			// State
			state = (String[][])primitives.get("State");
		}
		
		// S-box
		sboxBackgroundColor = (Color)primitives.get("S-box Background Color");
		sboxBorderColor = (Color)primitives.get("S-box Matrix Border Color");
		sboxTextProperties = (TextProperties)props.getPropertiesByName("S-box Properties");
		sboxIndexTextProps = (TextProperties)props.getPropertiesByName("S-box Index Properties");
		// State
		stateBackgroundColor = (Color)primitives.get("State Background Color");
		stateBorderColor = (Color)primitives.get("State Matrix Border Color");
		stateTextProps = (TextProperties)props.getPropertiesByName("State Properties");
		// Text properties
		textProps = (SourceCodeProperties)props.getPropertiesByName("Text Properties");
		// Code properties
		codeProps = (SourceCodeProperties)props.getPropertiesByName("Code Properties");
		// Animation properties
		highlightProcessedEntryColor = (Color)primitives.get("Highlight Processed Entry Color");
		highlightSboxIndexFontColor = (Color)primitives.get("Highlight S-box Index Color");
		highlightElementColor = (Color)primitives.get("Highlight Element Color");
		highlightSboxEntryColor = (Color)primitives.get("Highlight S-box Entry Color");
		// Questions (should questions be displayed)
		questions = (Boolean)primitives.get("Questions");
				
		subBytes(state);
		
		if (questions) lang.finalizeGeneration();

		return lang.toString();
	}

	public String getName() {
		return "Advanced Encryption Standard - SubBytes";
	}

	public String getAlgorithmName() {
		return "Advanced Encryption Standard (AES)";
	}

	public String getAnimationAuthor() {
		return "Bernd Conrad, Sandra Amend";
	}

	public String getDescription(){
		return "Die SubBytes Methode des Advanced Encryption Standards (AES) stellt eine simple Substitution dar."
				+"\n"
				+"Jeder Eintrag des States wird durch einen korrespondierenden Eintrag einer S-Box (substitution box) "
				+"\n"
				+"ersetzt. Die Methode wird im AES Algorithmus sowohl in den Verschl&uuml;sselungsrunden als auch in der "
				+"\n"
				+"Finalen Runde verwendet.";
	}

	public String getCodeExample(){
		return "SubBytes(byte state[4, 4])"
				+"\n"
				+"begin"
				+"\n"
				+"byte currentElement"
				+"\n"
				+"int row, column"
				+"\n"
				+"for i = 0 step 1 to 3"
				+"\n"
				+"    for j = 0 step 1 to 3"
				+"\n"
				+"        currentElement = state[j][i]"
				+"\n"
				+"        row = first 4 bits of currentElement"
				+"\n"
				+"        column = last 4 bits of currentElement"
				+"\n"
				+"        state[j][i] = sbox[row][column]"
				+"\n"
				+"    end for"
				+"\n"
				+"end for"
				+"\n"
				+"end";
	}

	public String getFileExtension(){
		return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	public Locale getContentLocale() {
		return Locale.GERMANY;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_CRYPT);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	public void subBytes(String[][] state) {
		
		// Description and Code
		SourceCode text, code;
		// Rectangles for Description, Title and Code
		Rect headerRect, titleRect, codeRect;
		// Text for the Title
		Text title, header;

		// Rectangle Properties
		RectProperties rectProps = new RectProperties();
		rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);

		// Background Rectangle Properties
		RectProperties backgroundRectProps = new RectProperties();
		backgroundRectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 5);
		backgroundRectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		// Header Rectangle Properties
		RectProperties headerRectProps = new RectProperties();
		headerRectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		headerRectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		headerRectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		headerRectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(0, 0, 208));

		// Title Properties
		TextProperties titleProps = new TextProperties();
		titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 16));
		titleProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
		titleProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		titleProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

		// Text Properties
		String tfName = ((Font)textProps.get(AnimationPropertiesKeys.FONT_PROPERTY)).getFamily();
		boolean tfBold = (Boolean)textProps.get(AnimationPropertiesKeys.BOLD_PROPERTY);
		int tfSize = (Integer)textProps.get(AnimationPropertiesKeys.SIZE_PROPERTY);
		Font textFont = new Font(tfName, getFontStyle(tfBold), tfSize);
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, textFont);
		textProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, codeProps.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY));

		// Code Properties
		String cfName = ((Font)codeProps.get(AnimationPropertiesKeys.FONT_PROPERTY)).getFamily();
		boolean cfBold = (Boolean)codeProps.get(AnimationPropertiesKeys.BOLD_PROPERTY);
		int cfSize = (Integer)codeProps.get(AnimationPropertiesKeys.SIZE_PROPERTY);
		Font codeFont = new Font(cfName, getFontStyle(cfBold), cfSize);
		codeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, codeFont);
		
		// CodeRectangle Properties
		RectProperties codeRectProps = new RectProperties();
		codeRectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		codeRectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 4);

		// S-box matrix rectangles properties
		RectProperties sboxRectProps = new RectProperties();
		sboxRectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, sboxBorderColor);
		sboxRectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		sboxRectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		sboxRectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, sboxBackgroundColor);

		// S-box index rectangles properties
		RectProperties indexRectProps = new RectProperties();
		indexRectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, sboxBorderColor);
		indexRectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		indexRectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		indexRectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		
		// State matrix rectangles properties
		RectProperties stateRectProps = new RectProperties();
		stateRectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, stateBorderColor);
		stateRectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		stateRectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		stateRectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, stateBackgroundColor);

		// S-box value text properties
		String sbfName = ((Font)sboxTextProperties.get(AnimationPropertiesKeys.FONT_PROPERTY)).getFamily();
		boolean sbfBold = true;
		int sbfSize = 14;
		Font sboxFont = new Font(sbfName, getFontStyle(sbfBold), sbfSize);
		sboxTextProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, sboxFont);

		// State value text properties
		String stfName = ((Font)stateTextProps.get(AnimationPropertiesKeys.FONT_PROPERTY)).getFamily();
		boolean stfBold = true;
		int stfSize = 14;
		Font stateFont = new Font(stfName, getFontStyle(stfBold), stfSize);
		stateTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, stateFont);
		
		// S-box index text properties
		String sifName = ((Font)sboxIndexTextProps.get(AnimationPropertiesKeys.FONT_PROPERTY)).getFamily();
		boolean sifBold = true;
		int sifSize = 12;
		Font sboxIndexFont = new Font(sifName, getFontStyle(sifBold), sifSize);
		sboxIndexTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, sboxIndexFont);
		Color sboxIndexFontColor = (Color)sboxIndexTextProps.get(AnimationPropertiesKeys.COLOR_PROPERTY);
		
		// State, Sbox, algo values title properties
		TextProperties titleTextProps = new TextProperties();
		titleTextProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		titleTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 12));
		titleTextProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		titleTextProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);

		// Alog values element text properties
		TextProperties AlgoValueTextProps = new TextProperties();
		AlgoValueTextProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		AlgoValueTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 18));
		AlgoValueTextProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		AlgoValueTextProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);

		// Algo value rect properties
		RectProperties algoValuesRectProps = new RectProperties();
		algoValuesRectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		algoValuesRectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);

		// Header
		TextProperties headerProps = new TextProperties();
		headerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE);
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 20));
		headerProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		headerProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
		header = lang.newText(new Coordinates(388, 22), "SubBytes", "header", null, headerProps);

		// Header Rectangle
		headerRect = lang.newRect(new Offset(-86, -18, header, AnimalScript.DIRECTION_C), new Offset(86, 18, header, AnimalScript.DIRECTION_C), "headerRect", null, headerRectProps);
		// Background rectangle
		@SuppressWarnings("unused")
		Rect backgroundRect = lang.newRect(new Coordinates(10, 30), new Coordinates(768, 600), "backgroundRect", null, backgroundRectProps);

		// Title Introduction
		title = lang.newText(new Offset(0, 14, headerRect, AnimalScript.DIRECTION_C), "Einleitung", "title",  null, titleProps);
		titleRect = lang.newRect(new Offset(-4, -4, title, AnimalScript.DIRECTION_NW), new Offset(4, 4, title, AnimalScript.DIRECTION_SE), "titleRect", null, rectProps);

		// Text
		text = lang.newSourceCode(new Offset(-345, 43, headerRect, AnimalScript.DIRECTION_S), "text", null, textProps);
		text.addCodeLine("Die SubBytes Methode wird vom Advanced Encprytion Standard (AES) während des Ver- und", null, 0, null);
		text.addCodeLine("Entschlüsselungsvorgangs verwendet. Die Methode wird in den Verschlüsselungsrunden und der Finalen", null, 0, null);
		text.addCodeLine("Runde angewendet (siehe Übersicht).", null, 0, null);
		text.addCodeLine("", null, 0, null);
		text.addCodeLine("Die SubBytes Funktion ist eine relativ simple Substitution. Ein Byte des States wird jeweils durch", null, 0, null);
		text.addCodeLine("ein korrespondierendes Byte aus der sogenannten Rijndael S-Box ersetzt, einer 16x16 Matrix mit", null, 0, null);
		text.addCodeLine("Byte-Einträgen. Dabei wird das zu ersetzende Byte in zwei Teile zerlegt. Die ersten vier Bits", null, 0, null);
		text.addCodeLine("spezifizieren den Zeilenindex und die letzten vier Bits den Spaltenindex des Eintrags der S-Box,", null, 0, null);
		text.addCodeLine("durch den das zu ersetzende Byte substituiert wird. Dieses Vorgehen wird wiederholt, bis alle ", null, 0, null);
		text.addCodeLine("16 Einträge des States substituiert wurden.", null, 0, null);
		text.addCodeLine("", null, 0, null);
		text.addCodeLine("SubBytes liefert eine monoalphabetische Verschlüsselung und garantiert die Nicht-Linearität von AES.", null, 0, null);
		
		// Sbox
		lang.nextStep("Einleitung");
		text.hide();
		//textRect.hide();
		title.hide();
		titleRect.hide();

		// Title Sbox
		title = lang.newText(new Offset(0, 22, headerRect, AnimalScript.DIRECTION_C), "S-Box", "title",  null, titleProps);
		titleRect = lang.newRect(new Offset(-4, -4, title, AnimalScript.DIRECTION_NW), new Offset(4, 4, title, AnimalScript.DIRECTION_SE), "titleRect", null, rectProps);

		text = lang.newSourceCode(new Offset(-345, 43, headerRect, AnimalScript.DIRECTION_S), "text", null, textProps);
		text.addCodeLine("Die Rijndael S-Box (substitution box) wird in der SubBytes Methode als einfache Lookup-Tabelle", null, 0, null);
		text.addCodeLine("verwendet. Sie enthält Byte-Einträge und wird erzeugt, indem man das Multiplikative Inverse des", null, 0, null);
		text.addCodeLine("Körpers GF(2^8) für eine gegebene Zahl berechnet und dieses dann durch eine affine", null, 0, null);
		text.addCodeLine("Transformation transformiert.", null, 0, null);
		text.addCodeLine("", null, 0, null);
		text.addCodeLine("Da die oben genannte Zahl, mit der das Multiplikative Inverse berechnet wird, für den AES Algorithmus", null, 0, null);
		text.addCodeLine("immer gleich ist, kann die S-Box vorberechnet werden und muss zur Laufzeit nicht dynamisch erzeugt", null, 0, null);
		text.addCodeLine("werden.", null, 0, null);
		text.addCodeLine("", null, 0, null);
		text.addCodeLine("Zur Entschlüsselung wird die inverse S-Box benötigt. Diese wird erzeugt, indem zuerst die affine", null, 0, null);
		text.addCodeLine("Transformation angewendet wird und dann das Multiplikative Inverse berechnet wird.", null, 0, null);
		text.addCodeLine("", null, 0, null);
		text.addCodeLine("Die Rijndael S-Box wurde speziell dafür konzipiert resistent gegen lineare und differentielle ", null, 0, null);
		text.addCodeLine("Kryptoanalysen zu sein.", null, 0, null);
		
		// Code
		lang.nextStep("S-Box");
		title.hide();
		titleRect.hide();
		text.hide();

		// Title Code
		title = lang.newText(new Offset(0, 22, headerRect, AnimalScript.DIRECTION_C), "Code", "title",  null, titleProps);
		titleRect = lang.newRect(new Offset(-4, -4, title, AnimalScript.DIRECTION_NW), new Offset(4, 4, title, AnimalScript.DIRECTION_SE), "titleRect", null, rectProps);

		// Code
		code = lang.newSourceCode(new Offset(-165, 36, headerRect, AnimalScript.DIRECTION_S), "subBytesCode", null, codeProps);
		code.addCodeLine("SubBytes(byte state[4, 4])", null, 0, null);
		code.addCodeLine("begin", null, 0, null);
		code.addCodeLine("byte currentElement", null, 2, null);
		code.addCodeLine("int row, column", null, 2, null);
		code.addCodeLine("for i = 0 step 1 to 3", null, 2, null);
		code.addCodeLine("for j = 0 step 1 to 3", null, 4, null);
		code.addCodeLine("currentElement = state[j][i]", null, 6, null);
		code.addCodeLine("row = first 4 bits of currentElement", null, 6, null);
		code.addCodeLine("column = last 4 bits of currentElement", null, 6, null);
		code.addCodeLine("state[j][i] = sbox[row][column]", null, 6, null);
		code.addCodeLine("end for", null, 4, null);
		code.addCodeLine("end for", null, 2, null);
		code.addCodeLine("end", null, 0, null);
		// Code rect
		codeRect = lang.newRect(new Offset(-4, -2, code, AnimalScript.DIRECTION_NW), new Offset(4, 2, code, AnimalScript.DIRECTION_SE), "subBytesCodeRect", null, codeRectProps);

		// Text
		text = lang.newSourceCode(new Offset(-345, 266, headerRect, AnimalScript.DIRECTION_S), "text", null, textProps);
		text.addCodeLine("Der Code der SubBytes Methode repräsentiert eine simple Substitution. Jedes Byte des States wird", null, 0, null);
		text.addCodeLine("durch ein entsprechendes Byte der S-Box substituiert.", null, 0, null);
		text.addCodeLine("", null, 0, null);
		text.addCodeLine("Im folgenden wird der Code genau beschrieben:", null, 0, null);
		
		lang.nextStep();
		code.highlight(2);
		code.highlight(3);

		text.addCodeLine("Es werden Variablen für den aktuellen Eintrag des States und den Zeilen- und Spaltenindex der S-Box", null, 0, null);
		text.addCodeLine("angelegt.", null, 0, null);
		text.highlight(4);
		text.highlight(5);

		lang.nextStep();
		code.unhighlight(2);
		code.unhighlight(3);
		text.unhighlight(4);
		text.unhighlight(5);
		code.highlight(4);
		code.highlight(5);

		text.addCodeLine("Über den State wird zuerst Zeilenweise und dann Spaltenweise iteriert. Das bedeutet, dass die", null, 0, null);
		text.addCodeLine("Variable i den aktuellen Spaltenindex und die Variable j den aktuellen Zeilenindex des Eintrags", null, 0, null);
		text.addCodeLine("des States enthält, der substituiert werden soll.", null, 0, null);
		text.highlight(6);
		text.highlight(7);
		text.highlight(8);

		lang.nextStep();
		code.unhighlight(4);
		code.unhighlight(5);
		text.unhighlight(6);
		text.unhighlight(7);
		text.unhighlight(8);
		code.highlight(6);

		text.addCodeLine("Der aktuelle Eintrag des States, der substituiert werden soll, wird zugewiesen.", null, 0, null);
		text.highlight(9);

		lang.nextStep();
		code.unhighlight(6);
		text.unhighlight(9);
		code.highlight(7);

		text.addCodeLine("Der Zeilenindex des Eintrags der S-Box wird durch die ersten vier Bits des State Eintrags bestimmt.", null, 0, null);
		text.highlight(10);

		lang.nextStep();
		code.unhighlight(7);
		text.unhighlight(10);
		code.highlight(8);

		text.addCodeLine("Der Spaltenindex des Eintrags der S-Box wird durch die letzten vier Bits des State Eintrags bestimmt.", null, 0, null);
		text.highlight(11);

		lang.nextStep();
		code.unhighlight(8);
		text.unhighlight(11);
		code.highlight(9);

		text.addCodeLine("Zuletzt wird der aktuelle State Eintrag durch den S-Box Eintrag substituiert, der durch den Zeilen-", null, 0, null);
		text.addCodeLine("und Spaltenindex definierten wird.", null, 0, null);
		text.highlight(12);
		text.highlight(13);

		lang.nextStep();
		code.unhighlight(9);
		text.unhighlight(12);
		text.unhighlight(13);
		text.addCodeLine("", null, 0, null);
		text.addCodeLine("In der folgenden Animation werden die Byte-Einträge des States und der S-Box als Hex-Werte dargestellt. ", null, 0, null);

		// SubBytes animation
		lang.nextStep("Code");
		title.hide();
		titleRect.hide();
		code.hide();
		codeRect.hide();
		text.hide();

		// Title Animation
		title = lang.newText(new Offset(0, 22, headerRect, AnimalScript.DIRECTION_C), "Animation", "title",  null, titleProps);
		titleRect = lang.newRect(new Offset(-4, -4, title, AnimalScript.DIRECTION_NW), new Offset(4, 4, title, AnimalScript.DIRECTION_SE), "titleRect", null, rectProps);

		// State group list
		LinkedList<Primitive> statePrimitives = new LinkedList<Primitive>();
		// Generate state matrix
		Rect[][] stateRects = generateState(102, 100, 26, 22, stateRectProps, statePrimitives);

		// State title
		Text stateTitle = lang.newText(new Offset(0, -24, stateRects[0][1], AnimalScript.DIRECTION_NE), "State", "stateTitle", null, titleTextProps);
		statePrimitives.add(stateTitle);

		// Save state matrix texts for later substitution
		Text[][] stateValues = new Text[4][4];
		// Fill generates matrix with state values
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				// First text position is bugging?
				if (i == 0 && j == 0) {
					stateValues[i][j] = lang.newText(new Offset(0, -8, stateRects[i][j], AnimalScript.DIRECTION_C), state[i][j], "stateValue" + i + j, null, stateTextProps);
					statePrimitives.add(stateValues[i][j]);
				}
				else {
					stateValues[i][j] = lang.newText(new Offset(0, -10, stateRects[i][j], AnimalScript.DIRECTION_C), state[i][j], "stateValue" + i + j, null, stateTextProps);
					statePrimitives.add(stateValues[i][j]);
				}
			}
		}

		// State group
		Group stateGroup = lang.newGroup(statePrimitives, "stateGroup");

		// Sbox
		// Sbox group list
		LinkedList<Primitive> sboxPrimitives = new LinkedList<Primitive>();
		// Generate Sbox matrix
		Rect[][] sboxRects = generateSbox(320, 122, 26, 22, sboxRectProps, sboxPrimitives);

		// State Title
		Text sboxTitle = lang.newText(new Offset(0, -44, sboxRects[0][7], AnimalScript.DIRECTION_N), "S-box", "sboxTitle", null, titleTextProps);
		sboxPrimitives.add(sboxTitle);

		// Sbox row index rects
		Rect[] sboxRowIndexRects = new Rect[16];
		// Sbox column index rects
		Rect[] sboxColumnIndexRects = new Rect[16];

		// Generate row and column inidex rects
		for (int i = 0; i < 16; i++) {
			sboxRowIndexRects[i] = lang.newRect(new Offset(0, -22, sboxRects[0][i], AnimalScript.DIRECTION_NW), new Offset(0, 0, sboxRects[0][i], AnimalScript.DIRECTION_NE), "sboxIndex0" + i, null, indexRectProps);
			sboxPrimitives.add(sboxRowIndexRects[i]);
			sboxColumnIndexRects[i] = lang.newRect(new Offset(-22, 0, sboxRects[i][0], AnimalScript.DIRECTION_NW), new Offset(0, 0, sboxRects[i][0], AnimalScript.DIRECTION_SW), "sboxIndex" + i + "0", null, indexRectProps);
			sboxPrimitives.add(sboxColumnIndexRects[i]);
		}
		// Filler inxdex rect
		Rect nullIndex = lang.newRect(new Offset(-22, -22, sboxRects[0][0], AnimalScript.DIRECTION_NW), new Offset(0, 0, sboxRects[0][0], AnimalScript.DIRECTION_NW), "sboxNullIndex", null, indexRectProps);
		sboxPrimitives.add(nullIndex);

		// Fill Sbox matrix
		fillSboxMatrix(sboxRects, sboxTextProperties, sboxPrimitives);

		// Column index texts
		int xOffsetH = 0;
		int yOffsetH = -8;
		String directionH = AnimalScript.DIRECTION_C;

		Text[] sboxColumnIndexTexts = new Text[16];
		sboxColumnIndexTexts[0] = lang.newText(new Offset(xOffsetH, -10, sboxRowIndexRects[0], directionH), "0", "zeroTextH", null, sboxIndexTextProps);
		sboxColumnIndexTexts[1] = lang.newText(new Offset(xOffsetH, yOffsetH, sboxRowIndexRects[1], directionH), "1", "oneTextH", null, sboxIndexTextProps);
		sboxColumnIndexTexts[2] = lang.newText(new Offset(xOffsetH, yOffsetH, sboxRowIndexRects[2], directionH), "2", "twoTextH", null, sboxIndexTextProps);
		sboxColumnIndexTexts[3] = lang.newText(new Offset(xOffsetH, yOffsetH, sboxRowIndexRects[3], directionH), "3", "threeTextH", null, sboxIndexTextProps);
		sboxColumnIndexTexts[4] = lang.newText(new Offset(xOffsetH, yOffsetH, sboxRowIndexRects[4], directionH), "4", "fourTextH", null, sboxIndexTextProps);
		sboxColumnIndexTexts[5] = lang.newText(new Offset(xOffsetH, yOffsetH, sboxRowIndexRects[5], directionH), "5", "fiveTextH", null, sboxIndexTextProps);
		sboxColumnIndexTexts[6] = lang.newText(new Offset(xOffsetH, yOffsetH, sboxRowIndexRects[6], directionH), "6", "sixTextH", null, sboxIndexTextProps);
		sboxColumnIndexTexts[7] = lang.newText(new Offset(xOffsetH, yOffsetH, sboxRowIndexRects[7], directionH), "7", "sevenTextH", null, sboxIndexTextProps);
		sboxColumnIndexTexts[8] = lang.newText(new Offset(xOffsetH, yOffsetH, sboxRowIndexRects[8], directionH), "8", "eightTextH", null, sboxIndexTextProps);
		sboxColumnIndexTexts[9] = lang.newText(new Offset(xOffsetH, yOffsetH, sboxRowIndexRects[9], directionH), "9", "nineTextH", null, sboxIndexTextProps);
		sboxColumnIndexTexts[10] = lang.newText(new Offset(xOffsetH, yOffsetH, sboxRowIndexRects[10], directionH), "A", "aTextH", null, sboxIndexTextProps);
		sboxColumnIndexTexts[11] = lang.newText(new Offset(xOffsetH, yOffsetH, sboxRowIndexRects[11], directionH), "B", "bTextH", null, sboxIndexTextProps);
		sboxColumnIndexTexts[12] = lang.newText(new Offset(xOffsetH, yOffsetH, sboxRowIndexRects[12], directionH), "C", "cTextH", null, sboxIndexTextProps);
		sboxColumnIndexTexts[13] = lang.newText(new Offset(xOffsetH, yOffsetH, sboxRowIndexRects[13], directionH), "D", "dTextH", null, sboxIndexTextProps);
		sboxColumnIndexTexts[14] = lang.newText(new Offset(xOffsetH, yOffsetH, sboxRowIndexRects[14], directionH), "E", "eTextH", null, sboxIndexTextProps);
		sboxColumnIndexTexts[15] = lang.newText(new Offset(xOffsetH, yOffsetH, sboxRowIndexRects[15], directionH), "F", "fTextH", null, sboxIndexTextProps);

		// Add all column indices to group list
		for (Text t : sboxColumnIndexTexts) {
			sboxPrimitives.add(t);
		}

		// Row index texts
		int xOffsetV = 0;
		int yOffsetV = -8;
		String directionV = AnimalScript.DIRECTION_C;

		Text[] sboxRowIndexTexts = new Text[16];
		sboxRowIndexTexts[0] = lang.newText(new Offset(xOffsetH, yOffsetH, sboxColumnIndexRects[0], directionV), "0", "zeroTextV", null, sboxIndexTextProps);
		sboxRowIndexTexts[1] = lang.newText(new Offset(xOffsetH, yOffsetH, sboxColumnIndexRects[1], directionV), "1", "oneTextV", null, sboxIndexTextProps);
		sboxRowIndexTexts[2] = lang.newText(new Offset(xOffsetV, yOffsetV, sboxColumnIndexRects[2], directionV), "2", "twoTextV", null, sboxIndexTextProps);
		sboxRowIndexTexts[3] = lang.newText(new Offset(xOffsetV, yOffsetV, sboxColumnIndexRects[3], directionV), "3", "threeTextV", null, sboxIndexTextProps);
		sboxRowIndexTexts[4] = lang.newText(new Offset(xOffsetV, yOffsetV, sboxColumnIndexRects[4], directionV), "4", "fourTextV", null, sboxIndexTextProps);
		sboxRowIndexTexts[5] = lang.newText(new Offset(xOffsetV, yOffsetV, sboxColumnIndexRects[5], directionV), "5", "fiveTextV", null, sboxIndexTextProps);
		sboxRowIndexTexts[6] = lang.newText(new Offset(xOffsetV, yOffsetV, sboxColumnIndexRects[6], directionV), "6", "sixTextV", null, sboxIndexTextProps);
		sboxRowIndexTexts[7] = lang.newText(new Offset(xOffsetV, yOffsetV, sboxColumnIndexRects[7], directionV), "7", "sevenTextV", null, sboxIndexTextProps);
		sboxRowIndexTexts[8] = lang.newText(new Offset(xOffsetV, yOffsetV, sboxColumnIndexRects[8], directionV), "8", "eightTextV", null, sboxIndexTextProps);
		sboxRowIndexTexts[9] = lang.newText(new Offset(xOffsetV, yOffsetV, sboxColumnIndexRects[9], directionV), "9", "nineTextV", null, sboxIndexTextProps);
		sboxRowIndexTexts[10] = lang.newText(new Offset(xOffsetV, yOffsetV, sboxColumnIndexRects[10], directionV), "A", "aTextV", null, sboxIndexTextProps);
		sboxRowIndexTexts[11] = lang.newText(new Offset(xOffsetV, yOffsetV, sboxColumnIndexRects[11], directionV), "B", "bTextV", null, sboxIndexTextProps);
		sboxRowIndexTexts[12] = lang.newText(new Offset(xOffsetV, yOffsetV, sboxColumnIndexRects[12], directionV), "C", "cTextV", null, sboxIndexTextProps);
		sboxRowIndexTexts[13] = lang.newText(new Offset(xOffsetV, yOffsetV, sboxColumnIndexRects[13], directionV), "D", "dTextV", null, sboxIndexTextProps);
		sboxRowIndexTexts[14] = lang.newText(new Offset(xOffsetV, yOffsetV, sboxColumnIndexRects[14], directionV), "E", "eTextV", null, sboxIndexTextProps);
		sboxRowIndexTexts[15] = lang.newText(new Offset(xOffsetV, yOffsetV, sboxColumnIndexRects[15], directionV), "F", "fTextV", null, sboxIndexTextProps);

		// Add all row indices to group list
		for (Text t : sboxRowIndexTexts) {
			sboxPrimitives.add(t);
		}

		// Sbox group
		Group sboxGroup = lang.newGroup(sboxPrimitives, "sboxGroup");

		// Algo values

		// Algo values group list
		LinkedList<Primitive> algoValuesPrimitives = new LinkedList<Primitive>();
	
		// Current element box
		Rect currentElementRect = lang.newRect(new Offset(-15, 56, stateRects[3][1], AnimalScript.DIRECTION_SE), new Offset(15, 76, stateRects[3][1], AnimalScript.DIRECTION_SE), "currentElementRect", null, rectProps);
		Text currentElementText = lang.newText(new Offset(0, -20, currentElementRect, AnimalScript.DIRECTION_N), "currentElement", "currentElementText", null, titleTextProps);
		Text currentElementValue = lang.newText(new Offset(-10, -6, currentElementRect, AnimalScript.DIRECTION_C), "", "currentElementValue", null, AlgoValueTextProps);
		algoValuesPrimitives.add(currentElementRect);
		algoValuesPrimitives.add(currentElementText);
		algoValuesPrimitives.add(currentElementValue);
	
		// Row element box
		Rect rowElementRect = lang.newRect(new Offset(-36, 24, currentElementRect, AnimalScript.DIRECTION_S), new Offset(-16, 44, currentElementRect, AnimalScript.DIRECTION_S), "rowElementRect", null, rectProps);
		Text rowElementTitle = lang.newText(new Offset(0, -26, rowElementRect, AnimalScript.DIRECTION_N), "row", "rowElementTitle", null, titleTextProps);
		Text rowElementValue = lang.newText(new Offset(-4, -7, rowElementRect, AnimalScript.DIRECTION_C), "", "rowElementValue", null, AlgoValueTextProps);
		algoValuesPrimitives.add(rowElementRect);
		algoValuesPrimitives.add(rowElementTitle);
		algoValuesPrimitives.add(rowElementValue);
	
		// Column element box
		Rect columnElementRect = lang.newRect(new Offset(16, 24, currentElementRect, AnimalScript.DIRECTION_S), new Offset(36, 44, currentElementRect, AnimalScript.DIRECTION_S), "columnElementRect", null, rectProps);
		Text columnElementTitle = lang.newText(new Offset(0, -26, columnElementRect, AnimalScript.DIRECTION_N), "column", "columnElementTitle", null, titleTextProps);
		Text columnElementValue = lang.newText(new Offset(-4, -7, columnElementRect, AnimalScript.DIRECTION_C), "", "columnElementValue", null, AlgoValueTextProps);
		algoValuesPrimitives.add(columnElementRect);
		algoValuesPrimitives.add(columnElementTitle);
		algoValuesPrimitives.add(columnElementValue);

		// Substitution element box
		Rect substElementRect = lang.newRect(new Offset(0, 70, currentElementRect, AnimalScript.DIRECTION_SW), new Offset(30, 90, currentElementRect, AnimalScript.DIRECTION_SW), "substElementRect", null, rectProps);
		Text substElementTitle = lang.newText(new Offset(0, -26, substElementRect, AnimalScript.DIRECTION_N), "substitution", "substElementTitle", null, titleTextProps);
		Text substElementValue = lang.newText(new Offset(-10, -6, substElementRect, AnimalScript.DIRECTION_C), "", "substElementValue", null, AlgoValueTextProps);
		algoValuesPrimitives.add(substElementRect);
		algoValuesPrimitives.add(substElementTitle);
		algoValuesPrimitives.add(substElementValue);
	
		// Algo values rect
		Rect algoValuesRect = lang.newRect(new Offset(-4, 0, currentElementText, AnimalScript.DIRECTION_NW), new Offset(4, 136, currentElementText, AnimalScript.DIRECTION_NE), "elementsRect", null, algoValuesRectProps);
		algoValuesPrimitives.add(algoValuesRect);
	
		// Algo value title
		Text algoValuesTitle = lang.newText(new Offset(0, -26, algoValuesRect, AnimalScript.DIRECTION_N), "Algo values", "elementsText", null, titleTextProps);
		algoValuesPrimitives.add(algoValuesTitle);

		// Group Algo values
		Group algoValuesGroup = lang.newGroup(algoValuesPrimitives, "algoValuesGroup");

		// Code
		code = lang.newSourceCode(new Coordinates(20, 374), "subBytesCode", null, codeProps);
		code.addCodeLine("SubBytes(byte state[4, 4])", null, 0, null);
		code.addCodeLine("begin", null, 0, null);
		code.addCodeLine("byte currentElement", null, 2, null);
		code.addCodeLine("int row, column", null, 2, null);
		code.addCodeLine("for i = 0 step 1 to 3", null, 2, null);
		code.addCodeLine("for j = 0 step 1 to 3", null, 4, null);
		code.addCodeLine("currentElement = state[j][i]", null, 6, null);
		code.addCodeLine("row = first 4 bits of currentElement", null, 6, null);
		code.addCodeLine("column = last 4 bits of currentElement", null, 6, null);
		code.addCodeLine("state[j][i] = sbox[row][column]", null, 6, null);
		code.addCodeLine("end for", null, 4, null);
		code.addCodeLine("end for", null, 2, null);
		code.addCodeLine("end", null, 0, null);
		// Code rect
		codeRect = lang.newRect(new Offset(-4, 0, code, AnimalScript.DIRECTION_NW), new Offset(4, 0, code, AnimalScript.DIRECTION_SE), "subBytesCodeRect", null, codeRectProps);

		if (questions) {
			// Question Groups
			lang.addQuestionGroup(new QuestionGroupModel("rowQuestions", 1));
			lang.addQuestionGroup(new QuestionGroupModel("columnQuestions", 1));
			lang.addQuestionGroup(new QuestionGroupModel("sboxEntryQuestions", 1));
			
			// Question
			MultipleChoiceQuestionModel stateEntryQuestion = new MultipleChoiceQuestionModel("stateEntryQuestion");
			stateEntryQuestion.setPrompt("Welchen Wert nimmt die Variable currentElement an? (Mit welchem State Eintrag wird begonnen?)");
			stateEntryQuestion.addAnswer(state[3][0], 0, "");
			stateEntryQuestion.addAnswer(state[3][3], 0, "");
			stateEntryQuestion.addAnswer(state[0][0], 1, "Korrekt, es wird mit dem ersten Eintrag des States begonnen (Zeilenindex 0, Spaltenindex 0).");
			stateEntryQuestion.addAnswer(state[0][3], 0, "");
			lang.addMCQuestion(stateEntryQuestion);
		}
		
		// SubBytes
		lang.nextStep("Animation");
		
		int rowValue, columnValue;
		String currentElement, rowValueHex, columnValueHex, substitution;

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {

				// Algo values
				currentElement = state[j][i];
				rowValueHex = currentElement.substring(0, 1);
				columnValueHex = currentElement.substring(1, 2);
				rowValue = Integer.parseInt(rowValueHex, 16);
				columnValue = Integer.parseInt(columnValueHex, 16);
				substitution = SBOX[rowValue][columnValue];
				
				// Highlight current element step
				code.highlight(6);
				stateRects[j][i].changeColor("fillColor", highlightElementColor, null, new MsTiming(500));
				currentElementRect.changeColor("fillColor", highlightElementColor, null, new MsTiming(500));
				currentElementValue.setText(currentElement, null, new MsTiming(500));
			
				// Question
				if (questions) {
					FillInBlanksQuestionModel rowQuestion = new FillInBlanksQuestionModel("rowQuestion"+i+j);
					rowQuestion.setGroupID("rowQuestions");
					rowQuestion.setPrompt("Welchen Wert nimmt die Variable row an?");
					rowQuestion.addAnswer(rowValueHex, 1, "Korrekt. Die Variable row nimmt den Wert an, den die ersten 4 Bits des aktuellen State Eintrags beschreiben.");
					lang.addFIBQuestion(rowQuestion);
				}
				
				// Row step
				lang.nextStep();
				
				currentElementRect.changeColor("fillColor", Color.WHITE, null, new MsTiming(500));
				rowElementRect.changeColor("fillColor", highlightElementColor, null, new MsTiming(500));
				rowElementValue.setText(rowValueHex, null, new MsTiming(500));
				sboxRowIndexTexts[rowValue].changeColor("color", highlightSboxIndexFontColor, null, new MsTiming(500));
				code.unhighlight(6);
				code.highlight(7);
				for (int k = 0; k < 16; k++) {
					sboxRects[rowValue][k].changeColor("fillColor", highlightElementColor, null, new MsTiming(500));
				}
				
				// Question
				if (questions) {
					FillInBlanksQuestionModel columnQuestion = new FillInBlanksQuestionModel("columnQuestion"+i+j);
					columnQuestion.setGroupID("columnQuestions");
					columnQuestion.setPrompt("Welchen Wert nimmt die Variable column an?");
					columnQuestion.addAnswer(columnValueHex, 1, "Korrekt. Die Variable column nimmt den Wert an, den die letzten 4 Bits des aktuellen State Eintrags beschreiben.");
					lang.addFIBQuestion(columnQuestion);
				}
				
				// Column step
				lang.nextStep();
				rowElementRect.changeColor("fillColor", Color.WHITE, null, new MsTiming(500));
				columnElementRect.changeColor("fillColor", highlightElementColor, null, new MsTiming(500));
				columnElementValue.setText(columnValueHex, null, new MsTiming(500));
				sboxColumnIndexTexts[columnValue].changeColor("color", highlightSboxIndexFontColor, null, new MsTiming(500));
				code.unhighlight(7);
				code.highlight(8);
				for (int k = 0; k < 16; k++) {
					sboxRects[k][columnValue].changeColor("fillColor", highlightElementColor, null, new MsTiming(500));
				}
				
				// Question
				if (questions) {
					FillInBlanksQuestionModel sboxEntryQuestion = new FillInBlanksQuestionModel("sboxEntryQuestion"+i+j);
					sboxEntryQuestion.setGroupID("sboxEntryQuestions");
					sboxEntryQuestion.setPrompt("Durch welchen S-Box Eintrag wird der aktuelle State Eintrag substituiert?");
					sboxEntryQuestion.addAnswer(substitution, 1, "Korrekt. Die Variablen row und column geben die Position des S-Box Eintrags an, durch den substituiert wird.");
					lang.addFIBQuestion(sboxEntryQuestion);
				}
				
				// Mark substitution element
				lang.nextStep();
				
				substElementValue.setText(substitution, null, new MsTiming(500));
				columnElementRect.changeColor("fillColor", Color.WHITE, null, new MsTiming(500));
				substElementRect.changeColor("fillColor", highlightElementColor, null, new MsTiming(500));
				code.unhighlight(8);
				code.highlight(9);
				sboxRects[rowValue][columnValue].changeColor("fillColor", highlightSboxEntryColor, null, new MsTiming(500));

				// Substitution step
				lang.nextStep();
				stateValues[j][i].setText(substitution, null, new MsTiming(500));
				stateRects[j][i].changeColor("fillColor", highlightProcessedEntryColor, new MsTiming(800), new MsTiming(500));

				// Question
				if (i == 0 && j == 00 && questions) {
					MultipleChoiceQuestionModel nextStateEntryQuestion = new MultipleChoiceQuestionModel("nextStateEntryQuestion");
					nextStateEntryQuestion.setPrompt("Welcher State Eintrag wird als nächstes substituiert?");
					nextStateEntryQuestion.addAnswer(state[1][0], 1, "Korrekt. Der Algorithmus geht erst zeilenweise, dann spaltenweise vor.");
					nextStateEntryQuestion.addAnswer(state[0][1], 0, "");
					nextStateEntryQuestion.addAnswer(state[1][1], 0, "");
					lang.addMCQuestion(nextStateEntryQuestion);
				}
				
				// Cleanup
				lang.nextStep();
				code.unhighlight(9);
				substElementRect.changeColor("fillColor", Color.WHITE, null, new MsTiming(500));
				sboxRowIndexTexts[rowValue].changeColor("color", sboxIndexFontColor, null, new MsTiming(500));
				sboxColumnIndexTexts[columnValue].changeColor("color", sboxIndexFontColor, null, new MsTiming(500));
				for (int k = 0; k < 16; k++) {
					sboxRects[k][columnValue].changeColor("fillColor", sboxBackgroundColor, null, null);
				}
				for (int k = 0; k < 16; k++) {
					sboxRects[rowValue][k].changeColor("fillColor", sboxBackgroundColor, null, null);
				}
			}
		}

		// Question
		if (questions){
			MultipleChoiceQuestionModel sboxAccessQuestion = new MultipleChoiceQuestionModel("sboxAccessQuestion");
			sboxAccessQuestion.setPrompt("Wie viele Zugriffe auf die S-Box wurden für die Substitution des gesamten States benötigt?");
			sboxAccessQuestion.addAnswer("16", 1, "Korrekt. Für jeden State Eintrag wird ein Zugriff auf die S-Box benötigt.");
			sboxAccessQuestion.addAnswer("32", 0, "");
			sboxAccessQuestion.addAnswer("1", 0, "");
			sboxAccessQuestion.addAnswer("128", 0, "");
			lang.addMCQuestion(sboxAccessQuestion);
		}
		
		// Final
		lang.nextStep();
		title.hide();
		titleRect.hide();
		stateGroup.hide();
		sboxGroup.hide();
		algoValuesGroup.hide();
		code.hide();
		codeRect.hide();

		// Title Final
		title = lang.newText(new Offset(0, 22, headerRect, AnimalScript.DIRECTION_C), "Abschluss", "title",  null, titleProps);
		titleRect = lang.newRect(new Offset(-4, -4, title, AnimalScript.DIRECTION_NW), new Offset(4, 4, title, AnimalScript.DIRECTION_SE), "titleRect", null, rectProps);

		// Text
		text = lang.newSourceCode(new Offset(-345, 43, headerRect, AnimalScript.DIRECTION_S), "text", null, textProps);
		text.addCodeLine("Die SubBytes Methode ist sehr effizient und einfach zu implementieren. Sie braucht immer 272 Bytes", null, 0, null);
		text.addCodeLine("an Speicher: 256 Bytes für die S-Box und 16 Bytes für den State, der substituiert werden soll.", null, 0, null);
		text.addCodeLine("", null, 0, null);
		text.addCodeLine("Es werden immer 16 Zugriffe auf die S-Box benötigt, ein Zugriff pro State-Eintrag. Da die S-Box", null, 0, null);
		text.addCodeLine("nicht dynamisch berechnet werden muss, sondern statisch vorliegt, wird auch hier kein zusätzlicher", null, 0, null);
		text.addCodeLine("Rechenaufwand benötigt.", null, 0, null);
		text.addCodeLine("", null, 0, null);
		text.addCodeLine("Wenn die SubBytes Methode auch zum Entschlüssel verwendet werden soll, muss auch die inverse S-Box", null, 0, null);
		text.addCodeLine("vorliegen, was nochmal zusätzlich 256 Bytes an Speicher kosten würde. Der Substitutionsvorgang ist", null, 0, null);
		text.addCodeLine("ansonsten absolut identisch mit der Ausnahme der inversen S-Box.", null, 0, null);
		text.addCodeLine("", null, 0, null);
		
		// Last label
		lang.nextStep("Abschluss");
	}

	// Generate rects to dispaly sbox matrix
	private Rect[][] generateSbox(int x, int y, int width, int height, RectProperties rectProps, LinkedList<Primitive> sboxPrimitives) {

		// 16x16 matrix
		Rect[][] temp = new Rect[16][16];

		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				if (i == 0 && j == 0) {
					temp[i][j] = lang.newRect(new Coordinates(x, y), new Coordinates(x + width, y + height), "sboxRect" + i + j, null, rectProps);
					sboxPrimitives.add(temp[i][j]);
				}
				else if (j == 0) {
					temp[i][j] = lang.newRect(new Offset(0, 0, temp[i-1][j], AnimalScript.DIRECTION_SW), new Offset(width, height, temp[i-1][j], AnimalScript.DIRECTION_SW), "sboxRect" + i + j, null, rectProps);
					sboxPrimitives.add(temp[i][j]);
				}
				else {	
					temp[i][j] = lang.newRect(new Offset(0, 0, temp[i][j-1], AnimalScript.DIRECTION_NE), new Offset(width, height, temp[i][j-1], AnimalScript.DIRECTION_NE), "sboxRect" + i + j, null, rectProps);
					sboxPrimitives.add(temp[i][j]);
				}
			}
		}

		return temp;
	}

	// Generate rects to display state matrix
	private Rect[][] generateState(int x, int y, int width, int height, RectProperties rectProps, LinkedList<Primitive> statePrimitives) {

		// 4x4 matrix
		Rect[][] temp = new Rect[4][4];

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (i == 0 && j == 0) {
					temp[i][j] = lang.newRect(new Coordinates(x, y), new Coordinates(x + width, y + height), "stateRect" + i + j, null, rectProps);
					statePrimitives.add(temp[i][j]);
				}
				else if (j == 0) {
					temp[i][j] = lang.newRect(new Offset(0, 0, temp[i-1][j], AnimalScript.DIRECTION_SW), new Offset(width, height, temp[i-1][j], AnimalScript.DIRECTION_SW), "stateRect" + i + j, null, rectProps);
					statePrimitives.add(temp[i][j]);
				}
				else {	
					temp[i][j] = lang.newRect(new Offset(0, 0, temp[i][j-1], AnimalScript.DIRECTION_NE), new Offset(width, height, temp[i][j-1], AnimalScript.DIRECTION_NE), "stateRect" + i + j, null, rectProps);
					statePrimitives.add(temp[i][j]);
				}
			}
		}

		return temp;
	}

	// Fill Sbox rects with precoded values
	private void fillSboxMatrix(Rect[][] sboxRects, TextProperties MatrixValueProps, LinkedList<Primitive> sboxPrimitives) {

		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				if (i == 0 && j == 0) {
					// Different offset because of display bug
					sboxPrimitives.add(lang.newText(new Offset(0, -8, sboxRects[i][j], AnimalScript.DIRECTION_C), SBOX[i][j], "stateValue" + i + j, null, MatrixValueProps));
				}
				else {
					sboxPrimitives.add(lang.newText(new Offset(0, -10, sboxRects[i][j], AnimalScript.DIRECTION_C), SBOX[i][j], "stateValue" + i + j, null, MatrixValueProps));
				}
			}
		}
	}

	// Checks if the state is correct in terms of size and content
	private String checkInputState(String[][] s) {
		
		boolean isHex;
		
		// State not null and correct amount of rows
		if (s == null) {
			return "Fehler beim Parsen des States (null).";
		}
		if (s.length != 4) {
			return "Die Anzahl der Zeilen des States is nicht korrekt.";
		}
		// Correct amount of columns per row
		if (s[0].length != 4 || s[1].length != 4 || s[2].length != 4 || s[3].length != 4)
			return "Die Anzahl der Spalten des States ist nicht korrekt.";
		// Every entry is a 8 bit (2 digit) hex value
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				String value = s[j][i];
				// Check if the state value is a 2 digit hex value
				isHex = value.matches("[0-9a-fA-F]{2}");
				// If the value is not a valid 8 bit hex value return error message
				if (!isHex) return "Der Eintrag \"" + value + "\" des States ist kein korrekter 8 bit Hex Wert.";
			}
		}
		return "correct";
	}
	
	// Generates a random state in case of wrong input state
	private String[][] generateState() {
		
		String[][] state = new String[4][4];
		int rnd;
		String hexValue;
		Random rand = new Random();
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				rnd = rand.nextInt(256);
				hexValue = Integer.toHexString(rnd);
				// Only single digit hex -> prepend 0
				if (hexValue.length() == 1) {
					hexValue = "0" + hexValue;
				}
				state[j][i] = hexValue.toUpperCase();
			}
		}
		
		return state;
	}
	
	// Returns Font.BOLD if input true, else Font.PLAIN
	private int getFontStyle(boolean bold) {

		if (bold) { 
			return Font.BOLD;
		}
		else {
			return Font.PLAIN;
		}
	}
	
	@Override
	public boolean validateInput(AnimationPropertiesContainer arg0,
			Hashtable<String, Object> arg1) throws IllegalArgumentException {
		
		// Parse state
		String[][] s = (String[][])arg1.get("State");
		// Check state
		String result = checkInputState(s);
		
		// Evaluate result of checkInputState
		if (result.equalsIgnoreCase("correct")){
			return true;
		}
		else {
			// Build error message
			StringBuilder errorMesage = new StringBuilder();
			errorMesage.append("Der angegebene State ist nicht korrekt.\nFolgender Fehler ist aufgetreten:\n\n");
			errorMesage.append(result);
			errorMesage.append("\n\nSoll ein zufälliger State generiert werden?");
			
			// Ask user if he wants to generate a random state
			int choice = JOptionPane.showConfirmDialog(null, errorMesage.toString(), "Fehlerhafter State", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
			
			// Check users choice
			if (choice == JOptionPane.OK_OPTION) {
				// Generate random state
				state = generateState();
				correctedState = true;
				return true;
			}
			else {
				throw new IllegalArgumentException(result);
			}
		}

	}

}