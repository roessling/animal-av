package generators.cryptography;

import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.Color;
import java.awt.Font;
import java.util.LinkedList;
import java.util.Locale;

import algoanim.primitives.Group;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;

import java.util.Hashtable;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class AES implements Generator {
    private Language lang;
    private Font textFont;
    private Font sourceCodeFont;
    private boolean textBold;
    private boolean sourceCodeBold;
    private int textFontSize;
    private int sourceCodeFontSize;
    private Color textColor;
    private Color sourceCodeColor;
    private Color sourceCodeContextColor;
    private Color sourceCodeHighlightColor;


    public void init(){
        lang = new AnimalScript("Advanced Encryption Standard - Übersicht", "Bernd Conrad, Sandra Amend", 800, 600);
        lang.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
    	
        textFont = (Font)primitives.get("Text Font");
        sourceCodeFontSize = (Integer)primitives.get("Source Code Font Size");
        sourceCodeColor = (Color)primitives.get("Source Code Color");
        textBold = (Boolean)primitives.get("Text Bold");
        sourceCodeBold = (Boolean)primitives.get("Source Code Bold");
        textFontSize = (Integer)primitives.get("Text Font Size");
        sourceCodeContextColor = (Color)primitives.get("Source Code Context Color");
        textColor = (Color)primitives.get("Text Color");
        sourceCodeHighlightColor = (Color)primitives.get("Source Code Highlight Color");
        sourceCodeFont = (Font)primitives.get("Source Code Font");
        
        generateOverview();
        
        // Prevent Matrix Display Bug
        String refreshBug = lang.toString().replaceAll("refresh", "");
        
        return refreshBug;
    }

    public String getName() {
        return "Advanced Encryption Standard - Übersicht";
    }

    public String getAlgorithmName() {
        return "Advanced Encryption Standard (AES)";
    }

    public String getAnimationAuthor() {
        return "Bernd Conrad, Sandra Amend";
    }

    public String getDescription(){
        return "Der Advanced Encryption Standard (AES) ist ein symmetrisches Kryptosystem. "
        		+ "AES wurde im Oktober 2000 als Nachfolger des gebrochenen Data Encryption Standards (DES) "
        		+ "vom National Institute of Standards and Technology (NIST) ausgew&auml;hlt. Entwickelt wurde AES von "
        		+ "Joan Daemen und Vincent Rijmen und ist eine spezielle Version des Rijndael Algorithmus mit fester Blockl&auml;nge und variablen Schl&uuml;ssell&auml;ngen. "
        		+ "<br><br>Diese Animation gibt einen groben &Uuml;berblick &uuml;ber den high-level Ablauf des AES Algorithmus. "
        		+ "F&uuml;r genauere Informationen und Animationen zu den verwendeten Methoden siehe den jeweiligen Generator.";
    }

    public String getCodeExample(){
        return "Cipher(byte in[4, Nb], byte out[4, Nb], word w[Nb*(Nr+1)])"
			 +"\n"
			 +"begin "
			 +"\n"
			 +"    byte state[4, Nb]"
			 +"\n"
			 +"    state = in"
			 +"\n"
			 +"    AddRoundKey(state, w[0, Nb-1])"
			 +"\n"
			 +"    for round = 1 step 1 to Nr-1"
			 +"\n"
			 +"        SubBytes(state)"
			 +"\n"
			 +"        ShiftRows(state)"
			 +"\n"
			 +"        MixColumns(state)"
			 +"\n"
			 +"        AddRoundKey(state, w[round*Nb, (rountd+1)*Nb-1])"
			 +"\n"
			 +"    end for"
			 +"\n"
			 +"    SubBytes(state)"
			 +"\n"
			 +"    ShiftRows(state)"
			 +"\n"
			 +"    AddRoundKey(state, w[Nr*Nb, (NR+1)*Nb-1])"
			 +"\n"
			 +"    out = state"
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
    
    public void generateOverview() {
    	
    	// Description and code
		SourceCode text, code;
		// Rectangles for description, title and code
		Rect textRect, titleRect, codeRect;
		// Text for the title
		Text title;
		
		// Text Properties
		SourceCodeProperties textProperties = new SourceCodeProperties();
		textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(textFont.getFamily(), getFontStyle(textBold), textFontSize));
		textProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, textColor);
		textProperties.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.BLACK);
		textProperties.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLACK);
		textProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

		// Code Properties
		SourceCodeProperties codeProps = new SourceCodeProperties();
		codeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(sourceCodeFont.getFamily(), getFontStyle(sourceCodeBold), sourceCodeFontSize));
		codeProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, sourceCodeColor);
		codeProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		codeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, sourceCodeHighlightColor);
		codeProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, sourceCodeContextColor);
		
		// Rectangle Properties
		RectProperties rectProps = new RectProperties();
		rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		
		// Title Properties
		TextProperties titleProps = new TextProperties();
		titleProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
		titleProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		titleProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 16));

		
		// #1
		// Header
		TextProperties headerProps = new TextProperties();
		headerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE);
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 20));
		headerProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		Text header = lang.newText(new Coordinates(250, 25), "Advanced Encryption Standard", "header", null, headerProps);
		
		// Header Rectangle
		RectProperties headerRectProps = new RectProperties();
		headerRectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		headerRectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		headerRectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		headerRectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(0, 0, 208));
		Rect headerRect = lang.newRect(new Offset(-5, -5, header, AnimalScript.DIRECTION_NW), new Offset(5, 5, header, AnimalScript.DIRECTION_SE), "headerRect", null, headerRectProps);
		
		// Description
		text = lang.newSourceCode(new Coordinates(40, 80), "description", null, textProperties);
		text.addCodeLine("Der Advanced Encryption Standard (AES) ist ein symmetrisches Kryptosystem, dass vom", null, 0, null);
		text.addCodeLine("Institute of Standards and Technology (NIST) im Oktober 2000 als Nachfolger für", null, 0, null);
		text.addCodeLine("das gebrochene DES und 3DES als Standard bekanntgegeben wurde.", null, 0, null);
		text.addCodeLine("", null, 0, null);
		text.addCodeLine("AES ist eine spezialisierte Variante der von Joan Daemen und Vincent Rijment entwickelten", null, 0, null);
		text.addCodeLine("Rijndael-Chiffre. Die Rijndael-Chiffre unterstützt variable, voneinander unabhängige", null, 0, null);
		text.addCodeLine("Block- und Schlüssellängen von 128, 160, 192, 224 oder 256 Bit.", null, 0, null);
		text.addCodeLine("", null, 0, null);
		text.addCodeLine("AES setzt auf eine feste Blocklänge von 128 Bit und Schlüssel der Länge 128, 192 oder 256 Bit,", null, 0, null);
		text.addCodeLine("woraus sich auch die Bezeichnungen AES-128, AES-192 und AES-256 ableiten, die jeweils", null, 0, null);
		text.addCodeLine("auf die gewählte Schlüssellänge zurückzuführen sind. Bis jetzt sind keine praktisch relevante", null, 0, null);
		text.addCodeLine("Angriffe auf AES bekannt. AES wird unter anderem als Verschlüsselungsalgorithmus bei", null, 0, null);
		text.addCodeLine("verschlüsselten SSL und TLS Verbindungen benutzt, auch ist AES in den USA für staatliche", null, 0, null);
		text.addCodeLine("Dokumente mit höchster Geheimhaltsungsstufe zugelassen.", null, 0, null);
		textRect = lang.newRect(new Offset(-4, -4, text, AnimalScript.DIRECTION_NW), new Offset(700, 4, text, AnimalScript.DIRECTION_SW), "descRect", null, rectProps);
		
		// #2 AlgoOverview
		lang.nextStep();
		text.hide();
		textRect.hide();
		
		// Title
		title = lang.newText(new Offset(0, 20, headerRect, AnimalScript.DIRECTION_C), "Ablauf des Algorithmus", "algoOverview",  null, titleProps);
		titleRect = lang.newRect(new Offset(-4, -4, title, AnimalScript.DIRECTION_NW), new Offset(4, 4, title, AnimalScript.DIRECTION_SE), "algoOverviewRect", null, rectProps);
		
		// Algorithm Overview
		LinkedList<Primitive> algoOverviewPrimitives = new LinkedList<Primitive>();
		LinkedList<Primitive> algoOverviewRoundTexts = new LinkedList<Primitive>();
		LinkedList<Primitive> algoOverviewKeyAndPlain = new LinkedList<Primitive>();
		
		TextProperties algoOverviewTextProps = new TextProperties();
		algoOverviewTextProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		algoOverviewTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 12));
		algoOverviewTextProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		algoOverviewTextProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
		
		// KeyExpansion
		Text keyExpansion = lang.newText(new Coordinates(100, 98), "Schlüsselexpansion", "keyExpansion", null, algoOverviewTextProps);
		Rect keRect = lang.newRect(new Offset(-80, -14, keyExpansion, AnimalScript.DIRECTION_C), new Offset(80, 14, keyExpansion, AnimalScript.DIRECTION_C), "keRect", null, rectProps);
		
		algoOverviewPrimitives.add(keyExpansion);
		algoOverviewPrimitives.add(keRect);
		
		// Initial Round
		Text addRoundKey = lang.newText(new Offset(0, 36, keRect, AnimalScript.DIRECTION_S), "AddRoundKey", "addRoundKey", null, algoOverviewTextProps);
		Rect arkRect = lang.newRect(new Offset(-80, -14, addRoundKey, AnimalScript.DIRECTION_C), new Offset(80, 14, addRoundKey, AnimalScript.DIRECTION_C), "arkRect", null, rectProps);
		arkRect.changeColor("color", new Color(50, 205, 50), null, null);
		algoOverviewPrimitives.add(addRoundKey);
		algoOverviewPrimitives.add(arkRect);
		
		// Round
		Text subBytes = lang.newText(new Offset(0, 44, arkRect, AnimalScript.DIRECTION_S), "SubBytes", "subBytes", null, algoOverviewTextProps);
		Rect sbRect = lang.newRect(new Offset(-80, -14, subBytes, AnimalScript.DIRECTION_C), new Offset(80, 14, subBytes, AnimalScript.DIRECTION_C), "sbRect", null, rectProps);
		sbRect.changeColor("color", Color.BLUE, null, null);
		algoOverviewPrimitives.add(subBytes);
		algoOverviewPrimitives.add(sbRect);
		
		Text shiftRows = lang.newText(new Offset(0, 34, sbRect, AnimalScript.DIRECTION_S), "ShiftRows", "shiftRows", null, algoOverviewTextProps);
		Rect srRect = lang.newRect(new Offset(-80, -14, shiftRows, AnimalScript.DIRECTION_C), new Offset(80, 14, shiftRows, AnimalScript.DIRECTION_C), "srRect", null, rectProps);
		srRect.changeColor("color", Color.BLUE, null, null);
		algoOverviewPrimitives.add(shiftRows);
		algoOverviewPrimitives.add(srRect);
		
		Text mixColumns = lang.newText(new Offset(0, 34, srRect, AnimalScript.DIRECTION_S), "MixColumns", "mixColumns", null, algoOverviewTextProps);
		Rect mcRect = lang.newRect(new Offset(-80, -14, mixColumns, AnimalScript.DIRECTION_C), new Offset(80, 14, mixColumns, AnimalScript.DIRECTION_C), "mcRect", null, rectProps);
		mcRect.changeColor("color", Color.BLUE, null, null);
		algoOverviewPrimitives.add(mixColumns);
		algoOverviewPrimitives.add(mcRect);
		
		Text addRoundKeyRound = lang.newText(new Offset(0, 34, mcRect, AnimalScript.DIRECTION_S), "AddRoundKey", "addRoundKeyRound", null, algoOverviewTextProps);
		Rect arkrRect = lang.newRect(new Offset(-80, -14, addRoundKeyRound, AnimalScript.DIRECTION_C), new Offset(80, 14, addRoundKeyRound, AnimalScript.DIRECTION_C), "arkrRect", null, rectProps);
		arkrRect.changeColor("color", Color.BLUE, null, null);
		algoOverviewPrimitives.add(addRoundKeyRound);
		algoOverviewPrimitives.add(arkrRect);
		
		// Final Round
		Text subBytesFinal = lang.newText(new Offset(0, 44, arkrRect, AnimalScript.DIRECTION_S), "SubBytes", "subBytesFinal", null, algoOverviewTextProps);
		Rect sbRectFinal = lang.newRect(new Offset(-80, -14, subBytesFinal, AnimalScript.DIRECTION_C), new Offset(80, 14, subBytesFinal, AnimalScript.DIRECTION_C), "sbRectFinal", null, rectProps);
		sbRectFinal.changeColor("color", Color.RED, null, null);
		algoOverviewPrimitives.add(subBytesFinal);
		algoOverviewPrimitives.add(sbRectFinal);
		
		Text shiftRowsFinal = lang.newText(new Offset(0, 34, sbRectFinal, AnimalScript.DIRECTION_S), "ShiftRows", "shiftRowsFinal", null, algoOverviewTextProps);
		Rect srRectFinal = lang.newRect(new Offset(-80, -14, shiftRowsFinal, AnimalScript.DIRECTION_C), new Offset(80, 14, shiftRowsFinal, AnimalScript.DIRECTION_C), "srRectFinal", null, rectProps);
		srRectFinal.changeColor("color", Color.RED, null, null);
		algoOverviewPrimitives.add(shiftRowsFinal);
		algoOverviewPrimitives.add(srRectFinal);
		
		Text addRoundKeyFinal = lang.newText(new Offset(0, 34, srRectFinal, AnimalScript.DIRECTION_S), "AddRoundKey", "addRoundKeyFinal", null, algoOverviewTextProps);
		Rect arkRectFinal = lang.newRect(new Offset(-80, -14, addRoundKeyFinal, AnimalScript.DIRECTION_C), new Offset(80, 14, addRoundKeyFinal, AnimalScript.DIRECTION_C), "arkRectFinal", null, rectProps);
		arkRectFinal.changeColor("color", Color.RED, null, null);
		algoOverviewPrimitives.add(addRoundKeyFinal);
		algoOverviewPrimitives.add(arkRectFinal);
		
		TextProperties roundTextProps = new TextProperties();
		roundTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 12));
		roundTextProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		
		// Roundtexts
		Text initialRoundText = lang.newText(new Coordinates(430, 110), "Initiale Runde", "initialRoundText", null, roundTextProps);
		initialRoundText.changeColor("color", new Color(50, 205, 50), null, null);
		Rect irtRect = lang.newRect(new Offset(-10,6, initialRoundText, AnimalScript.DIRECTION_NW), new Offset(-6, 10, initialRoundText, AnimalScript.DIRECTION_NW), "irtRect", null, rectProps);
		irtRect.changeColor("color", new Color(50, 205, 50), null, null);
		irtRect.changeColor("fillColor", new Color(50, 205, 50), null, null);
		algoOverviewRoundTexts.add(initialRoundText);
		algoOverviewRoundTexts.add(irtRect);
		
		Text roundText = lang.newText(new Coordinates(430, 130), "Verschlüsselungsrunde", "roundText", null, roundTextProps);
		roundText.changeColor("color", Color.BLUE, null, null);
		Rect rtRect = lang.newRect(new Offset(-10,6, roundText, AnimalScript.DIRECTION_NW), new Offset(-6, 10, roundText, AnimalScript.DIRECTION_NW), "rtRect", null, rectProps);
		rtRect.changeColor("color", Color.BLUE, null, null);
		rtRect.changeColor("fillColor", Color.BLUE, null, null);
		algoOverviewRoundTexts.add(roundText);
		algoOverviewRoundTexts.add(rtRect);
		
		Text finalRoundText = lang.newText(new Coordinates(430, 150), "Finale Runde", "finalRoundText", null, roundTextProps);
		finalRoundText.changeColor("color", Color.RED, null, null);
		Rect frRect = lang.newRect(new Offset(-10,6, finalRoundText, AnimalScript.DIRECTION_NW), new Offset(-6, 10, finalRoundText, AnimalScript.DIRECTION_NW), "frRect", null, rectProps);
		frRect.changeColor("color", Color.RED, null, null);
		frRect.changeColor("fillColor", Color.RED, null, null);
		algoOverviewRoundTexts.add(finalRoundText);
		algoOverviewRoundTexts.add(frRect);
		
		// Arrows
		PolylineProperties arrowProps = new PolylineProperties();
		arrowProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		arrowProps.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
		arrowProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		
		TextProperties keyAndPlainProps = new TextProperties();
		keyAndPlainProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		keyAndPlainProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 12));
		keyAndPlainProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		
		// KeyExpansion to AddRoundKey Initial
		Polyline keToArk = lang.newPolyline(new Offset[]{new Offset(0, 0, keRect, AnimalScript.DIRECTION_S), new Offset(0, 0, arkRect, AnimalScript.DIRECTION_N)}, "keToArk", null, arrowProps);
		algoOverviewPrimitives.add(keToArk);
		// Key to KeyExpansion
		Text keyText = lang.newText(new Offset(46, -8, keRect, AnimalScript.DIRECTION_E), "Schlüssel", "keyText", null, keyAndPlainProps);
		algoOverviewKeyAndPlain.add(keyText);
		Polyline keyToKe = lang.newPolyline(new Offset[]{new Offset(-5, 0, keyText, AnimalScript.DIRECTION_W), new Offset(0, 0, keRect, AnimalScript.DIRECTION_E)}, "keyToKe", null, arrowProps);
		algoOverviewKeyAndPlain.add(keyToKe);
		// AddRoundKey Initial to SubBytes Round
		Polyline arkToSb = lang.newPolyline(new Offset[]{new Offset(0, 0, arkRect, AnimalScript.DIRECTION_S), new Offset(0, 0, sbRect, AnimalScript.DIRECTION_N)}, "arkToSb", null, arrowProps);
		algoOverviewPrimitives.add(arkToSb);
		// Plaintext to AddRoundKey Initial
		Text plainText = lang.newText(new Offset(46,  -8, arkRect, AnimalScript.DIRECTION_E), "Klartext", "plainText", null, keyAndPlainProps);
		algoOverviewKeyAndPlain.add(plainText);
		Polyline plainToArk = lang.newPolyline(new Offset[]{new Offset(-5, 0, plainText, AnimalScript.DIRECTION_W), new Offset(0, 0, arkRect, AnimalScript.DIRECTION_E)}, "plainToArk", null, arrowProps);
		algoOverviewKeyAndPlain.add(plainToArk);
		// SubBytes Round to ShiftRows Round
		Polyline sbToSr = lang.newPolyline(new Offset[]{new Offset(0, 0, sbRect, AnimalScript.DIRECTION_S), new Offset(0, 0, srRect, AnimalScript.DIRECTION_N)}, "sbToSr", null, arrowProps);
		algoOverviewPrimitives.add(sbToSr);
		// ShiftRows Round to MixColumns Round
		Polyline srToMc = lang.newPolyline(new Offset[]{new Offset(0, 0, srRect, AnimalScript.DIRECTION_S), new Offset(0, 0, mcRect, AnimalScript.DIRECTION_N)}, "srToMc", null, arrowProps);
		algoOverviewPrimitives.add(srToMc);
		// MixColumns Round to AddRoundKey Round
		Polyline mcToArk = lang.newPolyline(new Offset[]{new Offset(0, 0, mcRect, AnimalScript.DIRECTION_S), new Offset(0, 0, arkrRect, AnimalScript.DIRECTION_N)}, "mcToArk", null, arrowProps);
		algoOverviewPrimitives.add(mcToArk);
		// AddRoundKey Round to SubBytes Round
		Polyline arkrToSb = lang.newPolyline(new Offset[]{new Offset(0, 0, arkrRect, AnimalScript.DIRECTION_S), new Offset(0, 12, arkrRect, AnimalScript.DIRECTION_S), new Offset(94, 12, arkrRect, AnimalScript.DIRECTION_S), new Offset(94, -22, sbRect, AnimalScript.DIRECTION_N), new Offset(0, -22, sbRect, AnimalScript.DIRECTION_N), new Offset(0, -0, sbRect, AnimalScript.DIRECTION_N)}, "arkrToSb", null, arrowProps);
		algoOverviewPrimitives.add(arkrToSb);
		// AddRoundKey Round to SubBytes Final
		Polyline arkrToSbf = lang.newPolyline(new Offset[]{new Offset(0, 0, arkrRect, AnimalScript.DIRECTION_S), new Offset(0, 0, sbRectFinal, AnimalScript.DIRECTION_N)}, "arkrToSbf", null, arrowProps);
		algoOverviewPrimitives.add(arkrToSbf);
		// SubBytes Final to ShiftRows Final
		Polyline sbfToSrf = lang.newPolyline(new Offset[]{new Offset(0, 0, sbRectFinal, AnimalScript.DIRECTION_S), new Offset(0, 0, srRectFinal, AnimalScript.DIRECTION_N)}, "sbfToSrf", null, arrowProps);
		algoOverviewPrimitives.add(sbfToSrf);
		// ShiftRows Final to AddRoundKey Final
		Polyline srfToArkf = lang.newPolyline(new Offset[]{new Offset(0, 0, srRectFinal, AnimalScript.DIRECTION_S), new Offset(0, 0, arkRectFinal, AnimalScript.DIRECTION_N)}, "srfToArkf", null, arrowProps);
		algoOverviewPrimitives.add(srfToArkf);
		
		// Description
		text = lang.newSourceCode(new Coordinates(230, 188), "algoDescription", null, textProperties);
		text.addCodeLine("AES basiert auf einem Substitutions-Permutations-Netzwerk, das sehr", null, 0, null);
		text.addCodeLine("effizient in Software und Hardware implementierbar ist. Der Ablauf", null, 0, null);
		text.addCodeLine("gliedert sich in eine Schlüsselexpansion und drei Runden: Vorrunde,", null, 0, null);
		text.addCodeLine("Verschlüsselungsrunde und Schlussrunde. Die Verschlüsselungsrunde", null, 0, null);
		text.addCodeLine("wird mehrfach wiederholt, wobei sich die Anzahl der Wiederholungen", null, 0, null);
		text.addCodeLine("nach der Schlüssellänge richtet.", null, 0, null);
		text.addCodeLine("", null, 0, null);
		text.addCodeLine("Eingabe für den Algorithmus ist ein maximal 128 Bit langer Klartext und", null, 0, null);
		text.addCodeLine("ein Schlüssel der Länge 128, 192 oder 256 Bit. Nachdem die", null, 0, null);
		text.addCodeLine("Schlüsselexpansion abgeschlossen ist, kann der eigentliche", null, 0, null);
		text.addCodeLine("Verschlüsselungsvorgang beginnen. Eingabe für die Initiale Runde ist der", null, 0, null);
		text.addCodeLine("Klartext und die Matrix aller Rundenschlüssel, welche durch die", null, 0, null);
		text.addCodeLine("Schlüsselexpansion aus dem Schlüssel erzeugt wird. Alle Methoden", null, 0, null);
		text.addCodeLine("arbeiten auf derselben Matrix, die am Anfang den Klartext beinhaltet,", null, 0, null);
		text.addCodeLine("d.h. jede Methode bekommt als Eingabe die bearbeitete Matrix der", null, 0, null);
		text.addCodeLine("Vorgängermethode. Die Ausgabe des Algorithmus ist die Matrix, die den", null, 0, null);
		text.addCodeLine("verschlüsselten Klartext enthält.", null, 0, null);
		textRect = lang.newRect(new Offset(-4, -4, text, AnimalScript.DIRECTION_NW), new Offset(530, 4, text, AnimalScript.DIRECTION_SW), "algoDescriptionRect", null, rectProps);
		
		// Groups
		Group algoOverview = lang.newGroup(algoOverviewPrimitives, "algoOverview");
		Group algoOverviewRT = lang.newGroup(algoOverviewRoundTexts, "algoOveriewRT");
		Group algoOverviewKP = lang.newGroup(algoOverviewKeyAndPlain, "algoOverviewKP");
		
		// #3 Key and Plain
		lang.nextStep();
		title.hide();
		titleRect.hide();
		text.hide();
		textRect.hide();
		algoOverview.hide();
		algoOverviewRT.hide();
		algoOverviewKP.hide();
		
		// Title
		title = lang.newText(new Offset(0, 20, headerRect, AnimalScript.DIRECTION_C), "Klartext und Schlüssel", "plainAndKeyHeader",  null, titleProps);
		titleRect = lang.newRect(new Offset(-4, -4, title, AnimalScript.DIRECTION_NW), new Offset(4, 4, title, AnimalScript.DIRECTION_SE), "playinAndKeyHeaderRect", null, rectProps);
		
		// Description
		text = lang.newSourceCode(new Coordinates(40, 80), "plainKeyDescription", null, textProperties);
		text.addCodeLine("Klartextblöcke und Schlüssel werden im AES Algorithmus durch Matritzen dargestellt.", null, 0, null);
		text.addCodeLine("Eine AES-Matrix hat immer 4 Spalten, wobei ein Eintrag einen 8 Bit Hex Wert repräsentiert.", null, 0, null);
		text.addCodeLine("Das bedeutet eine Zeile repräsentiert ein 32 Bit Wort.", null, 0, null);
		text.addCodeLine("", null, 0, null);
		text.addCodeLine("Klartextblöcke haben eine feste Länge von 128 Bit, das bedeuted ein Klartextblock wird als 4x4", null, 0, null);
		text.addCodeLine("Matrix dargestellt. Ist der Klartextblock zu lang, muss dieser in mehrere Teile aufgesplittet", null, 0, null);
		text.addCodeLine("werden. Ist er zu kurz, wird er auf 128 Bit mittels Padding aufgefüllt.", null, 0, null);
		text.addCodeLine("", null, 0, null);
		text.addCodeLine("Schlüssel haben eine variable Länge von 128, 192 oder 256 Bit, was einer Zeilenanzahl von 4, 6", null, 0, null);
		text.addCodeLine("bzw. 8 entspricht. Die Schlüssellänge ist auch der Namensgeber der respektiven AES Variante:", null, 0, null);
		text.addCodeLine("AES-128, AES-192 oder AES-256.", null, 0, null);
		text.addCodeLine("Auch richtet sich die Anzahl der Verschlüsselungsrunden nach der Schlüssellänge.", null, 0, null);
		textRect = lang.newRect(new Offset(-4, -4, text, AnimalScript.DIRECTION_NW), new Offset(700, 4, text, AnimalScript.DIRECTION_SW), "plainKeyDescriptionRect", null, rectProps);
		
		// Examples
		String[][] plain = {
				{"bd", "49", "a1", "3c"},
				{"35", "c9", "58", "b4"},
				{"80", "b1", "8d", "d4"},
				{"77", "7a", "d1", "7d"},
		};
		
		String[][] key128 = {
				{"61", "e7", "88", "2a"},
				{"32", "db", "09", "20"},
				{"93", "21", "81", "71"},
				{"d7", "d7", "80", "89"},
		};
		
		String[][] key192 = {
				{"18", "a8", "86", "25"},
				{"7e", "b7", "ac", "6d"},
				{"69", "39", "c5", "03"},
				{"f3", "a0", "89", "39"},
				{"71", "7c", "29", "4c"},
				{"c1", "af", "6c", "4d"},
		};
		
		String[][] key256 = {
				{"e3", "75", "84", "18"},
				{"ec", "47", "d6", "5f"},
				{"a8", "21", "9c", "8a"},
				{"21", "9c", "8a", "e4"},
				{"e8", "e0", "5f", "ec"},
				{"f7", "f3", "c1", "8f"},
				{"19", "7d", "7d", "ab"},
				{"77", "74", "83", "c0"},
		};
		
		TextProperties matrixTitleProps = new TextProperties();
		matrixTitleProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		matrixTitleProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		matrixTitleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 12));
		matrixTitleProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
		
		MatrixProperties matrixProperties = new MatrixProperties();
		matrixProperties.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
		
		LinkedList<Primitive> examplePrimitivies = new LinkedList<Primitive>();
		
		// Plainmatrix
		StringMatrix plainMatrix = lang.newStringMatrix(new Offset(40, 40, text, AnimalScript.DIRECTION_SW), plain, "plainMatrix", null, matrixProperties);
		Text plainTitle = lang.newText(new Offset(0, -20, plainMatrix, AnimalScript.DIRECTION_N), "AES Klartext", "aesPlainText", null, matrixTitleProps);
		examplePrimitivies.add(plainTitle);
		examplePrimitivies.add(plainMatrix);
		// 128 Bit Key
		StringMatrix key128Matrix = lang.newStringMatrix(new Offset(260, 40, text, AnimalScript.DIRECTION_SW), key128, "keyMatrix128", null, matrixProperties);
		Text key128Title = lang.newText(new Offset(0, -20, key128Matrix, AnimalScript.DIRECTION_N), "AES-128 Schlüssel", "aes128keyText", null, matrixTitleProps);
		examplePrimitivies.add(key128Title);
		examplePrimitivies.add(key128Matrix);
		// 192 Bit Key
		StringMatrix key192Matrix = lang.newStringMatrix(new Offset(400, 40, text, AnimalScript.DIRECTION_SW), key192, "keyMatrix192", null, matrixProperties);
		Text key192Title = lang.newText(new Offset(0, -20, key192Matrix, AnimalScript.DIRECTION_N), "AES-192 Schlüssel", "aes192keyText", null, matrixTitleProps);
		examplePrimitivies.add(key192Title);
		examplePrimitivies.add(key192Matrix);
		// 256 Bit Key
		StringMatrix key256Matrix = lang.newStringMatrix(new Offset(540, 40, text, AnimalScript.DIRECTION_SW), key256, "keyMatrix256", null, matrixProperties);
		Text key256Title = lang.newText(new Offset(0, -20, key256Matrix, AnimalScript.DIRECTION_N), "AES-256 Schlüssel", "aes256keyText", null, matrixTitleProps);
		examplePrimitivies.add(key256Title);
		examplePrimitivies.add(key256Matrix);
		
		// Group
		Group matrices = lang.newGroup(examplePrimitivies, "plainAndKey");
		
		// #4 KeyExpansion
		lang.nextStep();
		matrices.hide();
		text.hide();
		textRect.hide();
		title.hide();
		titleRect.hide();
		algoOverview.show();
		
		// Title
		title = lang.newText(new Offset(0, 22, headerRect, AnimalScript.DIRECTION_C), "Schlüsselexpansion", "keyExpansionHeader",  null, titleProps);
		titleRect = lang.newRect(new Offset(-4, -4, title, AnimalScript.DIRECTION_NW), new Offset(4, 4, title, AnimalScript.DIRECTION_SE), "keyExpansionHeaderRect", null, rectProps);
		
		// Description
		text = lang.newSourceCode(new Coordinates(230, 86), "keyExpansionDescription", null, textProperties);
		text.addCodeLine("Bei der Schlüsselexpansion wird eine feste Anzahl von Rundenschlüssel", null, 0, null);
		text.addCodeLine("aus dem Schlüssel erzeugt. Die Anzahl der erzeugten Rundenschlüssel", null, 0, null);
		text.addCodeLine("hängt von der Bitlänge des Schlpssels ab. Bei einer Länge von 128 Bit", null, 0, null);
		text.addCodeLine("werden 11 Rundenschlüssel, bei einer Länge von 192 Bit werden 13", null, 0, null);
		text.addCodeLine("Rundenschlüssel und bei einer Länge von 256 Bit werden 15", null, 0, null);
		text.addCodeLine("Rundenschlüssel erzeugt.", null, 0, null);
		text.addCodeLine("", null, 0, null);
		text.addCodeLine("Das Erzeugen der Rundenschlüssel geschieht mittels Expansions des", null, 0, null);
		text.addCodeLine("Schlüssels mit dem sogenannten Rijndael key schedule.", null, 0, null);
		text.addCodeLine("Jeder Rundenschlüssel wird während eines Durchlaufs des AES", null, 0, null);
		text.addCodeLine("Algorithmus genau einmal verwendet. Die Rundenschlüssel werden in", null, 0, null);
		text.addCodeLine("der AddRoundKey Methode verwendet.", null, 0, null);
		text.addCodeLine("", null, 0, null);
		text.addCodeLine("Die Schlüsselexpansion wird genau einmal vor der Initialen Runde", null, 0, null);
		text.addCodeLine("ausgeführt.", null, 0, null);
		text.addCodeLine("", null, 0, null);
		text.addCodeLine("Für weitere Informationen siehe KeyExpansion Animation.", null, 0, null);
		textRect = lang.newRect(new Offset(-4, -4, text, AnimalScript.DIRECTION_NW), new Offset(530, 4, text, AnimalScript.DIRECTION_SW), "keyExpansionDescriptionRect", null, rectProps);
		
		// Highlight
		arkRect.changeColor("color", Color.BLACK, null, null);
		sbRect.changeColor("color", Color.BLACK, null, null);
		srRect.changeColor("color", Color.BLACK, null, null);
		mcRect.changeColor("color", Color.BLACK, null, null);
		arkrRect.changeColor("color", Color.BLACK, null, null);
		sbRectFinal.changeColor("color", Color.BLACK, null, null);
		srRectFinal.changeColor("color", Color.BLACK, null, null);
		arkRectFinal.changeColor("color", Color.BLACK, null, null);
		keRect.changeColor("fillColor", Color.RED, null, null);
		
		// #5 AddRoundKey
		lang.nextStep();
		text.hide();
		textRect.hide();
		title.hide();
		titleRect.hide();
		
		// Title
		title = lang.newText(new Offset(0, 20, headerRect, AnimalScript.DIRECTION_C), "AddRoundKey", "addRoundKeyHeader",  null, titleProps);
		titleRect = lang.newRect(new Offset(-4, -4, title, AnimalScript.DIRECTION_NW), new Offset(4, 4, title, AnimalScript.DIRECTION_SE), "addRoundKeyHeaderRect", null, rectProps);
		
		// Description
		text = lang.newSourceCode(new Coordinates(230, 86), "addRoundKeyDescription", null, textProperties);
		text.addCodeLine("Bei der AddRoundKey Methode wird auf den aktuellen State des", null, 0, null);
		text.addCodeLine("Algorithmus, also die Ausgabe der vorherigen Methode oder im Fall der", null, 0, null);
		text.addCodeLine("Initialen Runde der Klartext, der aktuelle Rundenschlüssel angewendet.", null, 0, null);
		text.addCodeLine("", null, 0, null);
		text.addCodeLine("Rundenschlüssel sind immer 128 Bit lang und werden mit einer bitweisen", null, 0, null);
		text.addCodeLine("XOR-Operation auf den State angewendet.", null, 0, null);
		text.addCodeLine("", null, 0, null);
		text.addCodeLine("Die AddRoundKey Methode wird jeweils am Ende jeder Runde angewendet", null, 0, null);
		text.addCodeLine("und ist die einzige Methode die den AES Algorithmus abhängig vom", null, 0, null);
		text.addCodeLine("Schlüssel macht.", null, 0, null);
		text.addCodeLine("", null, 0, null);
		text.addCodeLine("Für weitere Informationen siehe AddRoundKey Animation.", null, 0, null);
		textRect = lang.newRect(new Offset(-4, -4, text, AnimalScript.DIRECTION_NW), new Offset(530, 4, text, AnimalScript.DIRECTION_SW), "addRoundKeyDescriptionRect", null, rectProps);
		
		// Highlight
		keRect.changeColor("fillColor", Color.WHITE, null, null);
		arkRect.changeColor("fillColor", Color.RED, null, null);
		arkrRect.changeColor("fillColor", Color.RED, null, null);
		arkRectFinal.changeColor("fillColor", Color.RED, null, null);
		
		// #6 SubBytes
		lang.nextStep();
		text.hide();
		textRect.hide();
		title.hide();
		titleRect.hide();
		
		// Title
		title = lang.newText(new Offset(0, 20, headerRect, AnimalScript.DIRECTION_C), "SubBytes", "subBytesHeader",  null, titleProps);
		titleRect = lang.newRect(new Offset(-4, -4, title, AnimalScript.DIRECTION_NW), new Offset(4, 4, title, AnimalScript.DIRECTION_SE), "subBytesHeaderRect", null, rectProps);
				
		// Description
		text = lang.newSourceCode(new Coordinates(230, 86), "subBytesDescription", null, textProperties);
		text.addCodeLine("Bei der SubBytes Methode wird jedes Bytes des States durch ein", null, 0, null);
		text.addCodeLine("entsprechendes Byte der sogenannten S-Box ersetzt.", null, 0, null);
		text.addCodeLine("", null, 0, null);
		text.addCodeLine("Die S-Box ist eine statische Matrix, die unabängig von den Eingabewerten", null, 0, null);
		text.addCodeLine("des AES Algorithmus ist und dementsprechend vorberechnet werden", null, 0, null);
		text.addCodeLine("kann. Ein Byte im State wird dann durch das entsprechend", null, 0, null);
		text.addCodeLine("korresponiderende Byte der S-Box ersetzt.", null, 0, null);
		text.addCodeLine("", null, 0, null);
		text.addCodeLine("Die SubBytes Methode wird am Anfang der Verschlüsselungsrunde", null, 0, null);
		text.addCodeLine("und der Finalen Runde angewendet, liefert eine monoalphabetische", null, 0, null);
		text.addCodeLine("Verschlüsselung und garantiert die Nicht-Linearität des Algortithmus.", null, 0, null);
		text.addCodeLine("", null, 0, null);
		text.addCodeLine("Für weitere Informationen siehe SubBytes Animation.", null, 0, null);
		textRect = lang.newRect(new Offset(-4, -4, text, AnimalScript.DIRECTION_NW), new Offset(530, 4, text, AnimalScript.DIRECTION_SW), "subBytesDescriptionRect", null, rectProps);
				
		// Highlight
		arkRect.changeColor("fillColor", Color.WHITE, null, null);
		arkrRect.changeColor("fillColor", Color.WHITE, null, null);
		arkRectFinal.changeColor("fillColor", Color.WHITE, null, null);
		sbRect.changeColor("fillColor", Color.RED, null, null);
		sbRectFinal.changeColor("fillColor", Color.RED, null, null);
		
		// #7 ShiftRows
		lang.nextStep();
		text.hide();
		textRect.hide();
		title.hide();
		titleRect.hide();
		
		// Title
		title = lang.newText(new Offset(0, 20, headerRect, AnimalScript.DIRECTION_C), "ShiftRows", "shiftRowsHeader",  null, titleProps);
		titleRect = lang.newRect(new Offset(-4, -4, title, AnimalScript.DIRECTION_NW), new Offset(4, 4, title, AnimalScript.DIRECTION_SE), "shiftRowsHeaderRect", null, rectProps);
				
		// Description
		text = lang.newSourceCode(new Coordinates(230, 86), "shiftRowsDescription", null, textProperties);
		text.addCodeLine("Bei der ShiftRows Methode werden die Spalten einer Zeile um einen", null, 0, null);
		text.addCodeLine("bestimmten Wert nach links verschoben. ", null, 0, null);
		text.addCodeLine("", null, 0, null);
		text.addCodeLine("Die erste Zeile bleibt unverändert, die Spalten der zweiten Zeile werden", null, 0, null);
		text.addCodeLine("um 1 nach links, die Spalten der zweiten Zeile um 2 nach links und die", null, 0, null);
		text.addCodeLine("Spalten der dritten Zeile um 3 nach links verschoben.", null, 0, null);
		text.addCodeLine("", null, 0, null);
		text.addCodeLine("Die ShiftRows Methode wird sowohl in der Verschlüsselungsrunde als", null, 0, null);
		text.addCodeLine("auch in der Finalen Runde verwendet.", null, 0, null);
		text.addCodeLine("", null, 0, null);
		text.addCodeLine("Für weitere Informationen siehe ShiftRows Animation.", null, 0, null);
		textRect = lang.newRect(new Offset(-4, -4, text, AnimalScript.DIRECTION_NW), new Offset(530, 4, text, AnimalScript.DIRECTION_SW), "shiftRowsDescriptionRect", null, rectProps);
		
		// Highlight
		sbRect.changeColor("fillColor", Color.WHITE, null, null);
		sbRectFinal.changeColor("fillColor", Color.WHITE, null, null);
		srRect.changeColor("fillColor", Color.RED, null, null);
		srRectFinal.changeColor("fillColor", Color.RED, null, null);
		
		// #8 MixColumns
		lang.nextStep();
		text.hide();
		textRect.hide();
		title.hide();
		titleRect.hide();
		
		// Title
		title = lang.newText(new Offset(0, 20, headerRect, AnimalScript.DIRECTION_C), "MixColumns", "mixColumnsHeader",  null, titleProps);
		titleRect = lang.newRect(new Offset(-4, -4, title, AnimalScript.DIRECTION_NW), new Offset(4, 4, title, AnimalScript.DIRECTION_SE), "mixColumnsHeaderRect", null, rectProps);
				
		// Description
		text = lang.newSourceCode(new Coordinates(230, 86), "mixColumnsDescription", null, textProperties);
		text.addCodeLine("Bei der MixColumns Methode werden die vier Bytes jeder Spalte des States", null, 0, null);
		text.addCodeLine("mit einer invertierbaren Linearen Transformation kombiniert.", null, 0, null);
		text.addCodeLine("", null, 0, null);
		text.addCodeLine("Jede Spalte wird als ein Polynom des Körpers GF(2^8) gesehen und", null, 0, null);
		text.addCodeLine("modulo x^4+1 mit einem festen Polnyom multipliziert.", null, 0, null);
		text.addCodeLine("", null, 0, null);
		text.addCodeLine("Die MixColumns Methode wird nur in der Verschlüsselungsrunde", null, 0, null);
		text.addCodeLine("verwendet und garantiert zusammen mit ShiftRows die Diffusion des", null, 0, null);
		text.addCodeLine("AES Algorithmus.", null, 0, null);
		text.addCodeLine("", null, 0, null);
		text.addCodeLine("Für weitere Informationen siehe MixColumns Animation.", null, 0, null);
		textRect = lang.newRect(new Offset(-4, -4, text, AnimalScript.DIRECTION_NW), new Offset(530, 4, text, AnimalScript.DIRECTION_SW), "mixColumnsDescriptionRect", null, rectProps);
		
		// Highlight
		srRect.changeColor("fillColor", Color.WHITE, null, null);
		srRectFinal.changeColor("fillColor", Color.WHITE, null, null);
		mcRect.changeColor("fillColor", Color.RED, null, null);
		
		// #9 Code
		lang.nextStep();
		text.hide();
		textRect.hide();
		title.hide();
		titleRect.hide();
		
		// Highlight
		mcRect.changeColor("fillColor", Color.WHITE, null, null);
		
		// Title
		title = lang.newText(new Offset(0, 20, headerRect, AnimalScript.DIRECTION_C), "Code", "codeHeader",  null, titleProps);
		titleRect = lang.newRect(new Offset(-4, -4, title, AnimalScript.DIRECTION_NW), new Offset(4, 4, title, AnimalScript.DIRECTION_SE), "codeHeaderRect", null, rectProps);
		
		// Code
		code = lang.newSourceCode(new Coordinates(230, 86), "aesCode", null, codeProps);
		code.addCodeLine("Cipher(byte in[4, Nb], byte out[4, Nb], word w[Nb*(Nr+1)])", null, 0, null);
		code.addCodeLine("begin", null, 0, null);
		code.addCodeLine("byte state[4, Nb]", null, 2, null);
		code.addCodeLine("state = in", null, 2, null);
		code.addCodeLine("AddRoundKey(state, w[0, Nb-1])", null, 2, null);
		code.addCodeLine("for round = 1 step 1 to Nr-1", null, 2, null);
		code.addCodeLine("SubBytes(state)", null, 4, null);
		code.addCodeLine("ShiftRows(state)", null, 4, null);
		code.addCodeLine("MixColumns(state)", null, 4, null);
		code.addCodeLine("AddRoundKey(state, w[round*Nb, (rountd+1)*Nb-1])", null, 4, null);
		code.addCodeLine("end for", null, 2, null);
		code.addCodeLine("SubBytes(state)", null, 2, null);
		code.addCodeLine("ShiftRows(state)", null, 2, null);
		code.addCodeLine("AddRoundKey(state, w[Nr*Nb, (NR+1)*Nb-1])", null, 2, null);
		code.addCodeLine("out = state", null, 2, null);
		code.addCodeLine("end", null, 0, null);
		codeRect = lang.newRect(new Offset(-4, -4, code, AnimalScript.DIRECTION_NW), new Offset(530, 4, code, AnimalScript.DIRECTION_SW), "aesCodeRect", null, rectProps);
		
		// #10
		lang.nextStep();
		
		// Highlights
		keRect.changeColor("fillColor", Color.RED, null, null);
		code.highlight(0);
		
		// Description
		text = lang.newSourceCode(new Offset(4, -6, codeRect, AnimalScript.DIRECTION_SW), "aesCodeDescription", null, textProperties);
		text.addCodeLine("Der Algorithmus erwartet als Eingabe den Klartext (Byte Array in) und die", null, 0, null);
		textRect = lang.newRect(new Offset(-4, -4, text, AnimalScript.DIRECTION_NW), new Offset(530, 4, text, AnimalScript.DIRECTION_SW), "aesCodeDescriptionRect", null, rectProps);
		
		// #11
		lang.nextStep();
		text.hide();
		textRect.hide();
		
		// Highlights
		keRect.changeColor("fillColor", Color.WHITE, null, null);
		code.unhighlight(0);
		code.highlight(2);
		code.highlight(3);
		
		// Description
		text = lang.newSourceCode(new Offset(4, -6, codeRect, AnimalScript.DIRECTION_SW), "aesCodeDescription", null, textProperties);
		text.addCodeLine("Ein temporäres Array mit der benötigten Größe wird angelegt.", null, 0, null);
		text.addCodeLine("Der Klartextblock wird in dieses Arrays kopiert.", null, 0, null);
		textRect = lang.newRect(new Offset(-4, -4, text, AnimalScript.DIRECTION_NW), new Offset(530, 4, text, AnimalScript.DIRECTION_SW), "aesCodeDescriptionRect", null, rectProps);
		
		// #12
		lang.nextStep();
		text.hide();
		textRect.hide();
		
		// Highlights
		arkRect.changeColor("fillColor", Color.RED, null, null);
		code.unhighlight(2);
		code.unhighlight(3);
		code.highlight(4);
		
		// Description
		text = lang.newSourceCode(new Offset(4, -6, codeRect, AnimalScript.DIRECTION_SW), "aesCodeDescription", null, textProperties);
		text.addCodeLine("Die Initiale Runde besteht nur aus einer Methode.", null, 0, null);
		text.addCodeLine("Es wird die AddRoundKey Methode auf den Klartext angewendet.", null, 0, null);
		text.addCodeLine("Hierbei wird der erste Rundenschlüssel verwendet, was den ersten 128 Bit", null, 0, null);
		text.addCodeLine("des expandierten Schlüssels (w) entspricht.", null, 0, null);
		textRect = lang.newRect(new Offset(-4, -4, text, AnimalScript.DIRECTION_NW), new Offset(530, 4, text, AnimalScript.DIRECTION_SW), "aesCodeDescriptionRect", null, rectProps);
		
		// #13
		lang.nextStep();
		text.hide();
		textRect.hide();
		
		// Highlights
		arkRect.changeColor("fillColor", Color.WHITE, null, null);
		code.unhighlight(4);
		code.highlight(5, 0, true);
		code.highlight(10, 0, true);
		RectProperties roundRectProps = new RectProperties();
		roundRectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
		roundRectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, sourceCodeContextColor);
		Rect roundRect = lang.newRect(new Offset(-8, -30, sbRect, AnimalScript.DIRECTION_NW), new Offset(24, 20, arkrRect, AnimalScript.DIRECTION_SE), "roundRect", null, roundRectProps);
		
		// Description
		text = lang.newSourceCode(new Offset(4, -6, codeRect, AnimalScript.DIRECTION_SW), "aesCodeDescription", null, textProperties);
		text.addCodeLine("Dies ist die Verschlüsselungsrunde, die abhängig von der Schlüssellänge", null, 0, null);
		text.addCodeLine("9, 11 oder 13 mal ausgeführt wird. Die Schlüsselexpansion erzeugt je", null, 0, null);
		text.addCodeLine("nach Schlüssellänge 11 (128 Bit), 13 (192 Bit) oder 15 (256 Bit)", null, 0, null);
		text.addCodeLine("Rundenschlüssel. Da sowohl für die Initiale Runde als auch für die", null, 0, null);
		text.addCodeLine("Finale Runde jeweils ein Rundenschlüssel benötigt wird, ergibt sich die", null, 0, null);
		text.addCodeLine("oben genannten Anzahl von Durchführungen der Verschlüsselungsrunde.", null, 0, null);
		textRect = lang.newRect(new Offset(-4, -4, text, AnimalScript.DIRECTION_NW), new Offset(530, 4, text, AnimalScript.DIRECTION_SW), "aesCodeDescriptionRect", null, rectProps);
		
		// #14
		lang.nextStep();
		text.hide();
		textRect.hide();
		
		// Highlights
		sbRect.changeColor("fillColor", Color.RED, null, null);
		code.highlight(6);
		
		// Description
		text = lang.newSourceCode(new Offset(4, -6, codeRect, AnimalScript.DIRECTION_SW), "aesCodeDescription", null, textProperties);
		text.addCodeLine("Anwendung der SubBytes Methode auf den aktuellen State.", null, 0, null);
		textRect = lang.newRect(new Offset(-4, -4, text, AnimalScript.DIRECTION_NW), new Offset(530, 4, text, AnimalScript.DIRECTION_SW), "aesCodeDescriptionRect", null, rectProps);
		
		// #15
		lang.nextStep();
		text.hide();
		textRect.hide();
		
		// Highlights
		sbRect.changeColor("fillColor", Color.WHITE, null, null);
		srRect.changeColor("fillColor", Color.RED, null, null);
		code.unhighlight(6);
		code.highlight(7);
		
		// Description
		text = lang.newSourceCode(new Offset(4, -6, codeRect, AnimalScript.DIRECTION_SW), "aesCodeDescription", null, textProperties);
		text.addCodeLine("Anwendung der ShiftRows Methode auf den aktuellen State.", null, 0, null);
		textRect = lang.newRect(new Offset(-4, -4, text, AnimalScript.DIRECTION_NW), new Offset(530, 4, text, AnimalScript.DIRECTION_SW), "aesCodeDescriptionRect", null, rectProps);
		
		// #16
		lang.nextStep();
		text.hide();
		textRect.hide();
		
		// Highlights
		srRect.changeColor("fillColor", Color.WHITE, null, null);
		mcRect.changeColor("fillColor", Color.RED, null, null);
		code.unhighlight(7);
		code.highlight(8);
		
		// Description
		text = lang.newSourceCode(new Offset(4, -6, codeRect, AnimalScript.DIRECTION_SW), "aesCodeDescription", null, textProperties);
		text.addCodeLine("Anwendung der MixColumns Methode auf den aktuellen State.", null, 0, null);
		textRect = lang.newRect(new Offset(-4, -4, text, AnimalScript.DIRECTION_NW), new Offset(530, 4, text, AnimalScript.DIRECTION_SW), "aesCodeDescriptionRect", null, rectProps);
		
		// #17
		lang.nextStep();
		text.hide();
		textRect.hide();
		
		// Highlights
		mcRect.changeColor("fillColor", Color.WHITE, null, null);
		arkrRect.changeColor("fillColor", Color.RED, null, null);
		code.unhighlight(8);
		code.highlight(9);
		
		// Description
		text = lang.newSourceCode(new Offset(4, -6, codeRect, AnimalScript.DIRECTION_SW), "aesCodeDescription", null, textProperties);
		text.addCodeLine("Am Ende jeder Verschlüsselungsrunde wird die AddRoundKey Methode", null, 0, null);
		text.addCodeLine("auf den aktuellen State angewendet. Hierbei wird der jeweils zur", null, 0, null);
		text.addCodeLine("aktuellen Runde gehörende Schlüssel verwendet.", null, 0, null);
		textRect = lang.newRect(new Offset(-4, -4, text, AnimalScript.DIRECTION_NW), new Offset(530, 4, text, AnimalScript.DIRECTION_SW), "aesCodeDescriptionRect", null, rectProps);
		
		// #18
		lang.nextStep();
		text.hide();
		textRect.hide();
		roundRect.hide();
		
		// Highlights
		arkrRect.changeColor("fillColor", Color.WHITE, null, null);
		code.unhighlight(5);
		code.unhighlight(9);
		code.unhighlight(10);
		code.highlight(11, 0, true);
		code.highlight(12, 0, true);
		code.highlight(13, 0, true);
		Rect finalRect = lang.newRect(new Offset(-8, -8, sbRectFinal, AnimalScript.DIRECTION_NW), new Offset(8, 8, arkRectFinal, AnimalScript.DIRECTION_SE), "finalRect", null, roundRectProps);
		
		// Description
		text = lang.newSourceCode(new Offset(4, -6, codeRect, AnimalScript.DIRECTION_SW), "aesCodeDescription", null, textProperties);
		text.addCodeLine("Die Finale Runde wird nach den Verschlüsselungsrunden einmalig", null, 0, null);
		text.addCodeLine("durchlaufen.", null, 0, null);
		textRect = lang.newRect(new Offset(-4, -4, text, AnimalScript.DIRECTION_NW), new Offset(530, 4, text, AnimalScript.DIRECTION_SW), "aesCodeDescriptionRect", null, rectProps);
		
		// #19
		lang.nextStep();
		text.hide();
		textRect.hide();
		
		// Highlights
		sbRectFinal.changeColor("fillColor", Color.RED, null, null);
		code.unhighlight(12);
		code.unhighlight(13);
		code.highlight(11);
		
		// Description
		text = lang.newSourceCode(new Offset(4, -6, codeRect, AnimalScript.DIRECTION_SW), "aesCodeDescription", null, textProperties);
		text.addCodeLine("Anwendung der SubBytes Methode auf den aktuellen State.", null, 0, null);
		textRect = lang.newRect(new Offset(-4, -4, text, AnimalScript.DIRECTION_NW), new Offset(530, 4, text, AnimalScript.DIRECTION_SW), "aesCodeDescriptionRect", null, rectProps);
		
		// #20
		lang.nextStep();
		text.hide();
		textRect.hide();
		
		// Highlights
		sbRectFinal.changeColor("fillColor", Color.WHITE, null, null);
		srRectFinal.changeColor("fillColor", Color.RED, null, null);
		code.unhighlight(11);
		code.highlight(12);
		
		// Description
		text = lang.newSourceCode(new Offset(4, -6, codeRect, AnimalScript.DIRECTION_SW), "aesCodeDescription", null, textProperties);
		text.addCodeLine("Anwendung der ShiftRows Methode auf den aktuellen State.", null, 0, null);
		textRect = lang.newRect(new Offset(-4, -4, text, AnimalScript.DIRECTION_NW), new Offset(530, 4, text, AnimalScript.DIRECTION_SW), "aesCodeDescriptionRect", null, rectProps);
		
		// #21
		lang.nextStep();
		text.hide();
		textRect.hide();
		
		// Highlights
		srRectFinal.changeColor("fillColor", Color.WHITE, null, null);
		arkRectFinal.changeColor("fillColor", Color.RED, null, null);
		code.unhighlight(12);
		code.highlight(13);
		
		// Description
		text = lang.newSourceCode(new Offset(4, -6, codeRect, AnimalScript.DIRECTION_SW), "aesCodeDescription", null, textProperties);
		text.addCodeLine("Anwendung der ShiftRows Methode auf den aktuellen State.", null, 0, null);
		text.addCodeLine("Hierbei wird der letzte Rundenschlüssel verwendet.", null, 0, null);
		textRect = lang.newRect(new Offset(-4, -4, text, AnimalScript.DIRECTION_NW), new Offset(530, 4, text, AnimalScript.DIRECTION_SW), "aesCodeDescriptionRect", null, rectProps);
		
		// #22
		lang.nextStep();
		text.hide();
		textRect.hide();
		finalRect.hide();
		
		// Highlights
		arkRectFinal.changeColor("fillColor", Color.WHITE, null, null);
		code.unhighlight(13);
		code.highlight(14);
		
		// Description
		text = lang.newSourceCode(new Offset(4, -6, codeRect, AnimalScript.DIRECTION_SW), "aesCodeDescription", null, textProperties);
		text.addCodeLine("Der aktuelle State, der jetzt den verschlüsselten Klartext enthält, wird in", null, 0, null);
		text.addCodeLine("das übergebene Array (out) kopiert.", null, 0, null);
		textRect = lang.newRect(new Offset(-4, -4, text, AnimalScript.DIRECTION_NW), new Offset(530, 4, text, AnimalScript.DIRECTION_SW), "aesCodeDescriptionRect", null, rectProps);
    }
    
    private int getFontStyle(boolean bold) {
    	
    	if (bold) { 
    		return Font.BOLD;
		}
		else {
			return Font.PLAIN;
		}
    }

}