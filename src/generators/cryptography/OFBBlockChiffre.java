package generators.cryptography;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.TextProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import static algoanim.animalscript.AnimalScript.*;
import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.*;
import algoanim.primitives.generators.Language;
import algoanim.properties.*;
import algoanim.util.*;
import java.awt.Font;
import java.util.Arrays;
import java.util.LinkedList;

public class OFBBlockChiffre implements Generator {
	/**
	 * Parameter Definitions
	 */
	private static int LENGTH_I;
	private static int LENGTH_R;
	private static String[] IV;
	private static int KEY;
	private static String plainText;
	private static String chiffreText;
	
	/**
	 * Property Definitions
	 */
	public static TextProperties TITLE;
	public static TextProperties TITLE2;
	public static TextProperties TEXT;
	public static RectProperties TITLE_RECT;
	public static ArrayProperties ARRAY;
	public static PolylineProperties LINE;
	public static PolylineProperties ARROW;
	public static PolylineProperties HIGHLIGHTED_ARROW;
	public static SourceCodeProperties CODE;
	public static final Timing DEFAULTTIMING = new TicksTiming(15);
	public static final Timing LONGTIMING = new TicksTiming(40);

	/**
	 * Global Primitive Definitions
	 */
	private Text titleText;
	private Text descriptionText;	
	private Rect titleBorder;
	
	/**
	 * Other Definitions
	 */
	private Language lang;
	private Locale locale;
	private LinkedList<String> translatedText;
	
	public OFBBlockChiffre(Locale locale) {
		translatedText = new LinkedList<String>();
		
		this.locale = locale;
		if(locale == Locale.GERMAN) {
			//Description
			translatedText.add("Output Feedback Mode (OFB) ist eine Betriebsart, in der Blockchiffren betrieben werden können.");
			translatedText.add("Dabei hängt der Schlüsselstrom nicht vom Datenstrom ab und dadurch ist er gut geeignet für");
			translatedText.add("Fehleranfällige Umgebungen.\n");
			translatedText.add("In diesem Beispiel wird der Algorithmus mit der Verschlüsselungsfunktion E(i) = i + k mod %s");
			translatedText.add("betrieben.");
			
			//Pseudo Code
			translatedText.add("Wähle Parameter:");
			translatedText.add("Initialisierungsvektor IV = {0,1}^n");
			translatedText.add("Blocklänge 0 %s r %s n+1");
			translatedText.add("Schlüssel k");
			translatedText.add("Initialisierung:");
			translatedText.add("Setze I_0 = IV");
			translatedText.add("Für jeden Block m_i der Länge r");
			translatedText.add("Beginn");
			translatedText.add("Berechne E(I_(i-1))");
			translatedText.add("Setze O_i = I_i = Ergebnis");
			translatedText.add("Wenn Länge m_i < r dann");
			translatedText.add("fülle m_i mit 0 auf bis Länge m_i = r");
			translatedText.add("Setze C_i = m_i xor (linken r Bits von O_j)");
			translatedText.add("Ende");
			
			//steps
			translatedText.add("Setze den Initialisierungsvektor (IV)");
			translatedText.add("Es wird der neue Wert I mit der Funktion E(i) berechnet");
			translatedText.add("Der neue Wert I wird übernommen");
			translatedText.add("Da der aktuelle Nachrichten-Block nicht vollständig ist muss dieser mit 0 Bits aufgefüllt werden.");
			translatedText.add("Der aktuelle Block m wird verschlüsselt, indem er mit der XOR Operation mit den ersten R Bits aus O verknüpft wird");
			translatedText.add("Der verschlüsselte Block wird in dem Ausgabe Puffer gespeichert");
			translatedText.add("Alle Zeichen des Klartextes sind nun verschlüsselt....");
			
			//outro description
			translatedText.add("Nach durchlaufen des Algorithmuses wurde der\nKlartext %s\nin das\nChiffrat %s\nverschlüsselt.\n\nDamit ist der Algorithmus fertig!!!");
			
			//others
			translatedText.add("2^Länge_I");
			translatedText.add("Initalisierung");
			translatedText.add("Aktueller Schritt:");
		}
		else {
			//Description
			translatedText.add("Output feedback mode (OFB) is a mode in the block ciphers can be operated.");
			translatedText.add("While the key current does not depends on the stream, ");
			translatedText.add("he is well suited for error-prone environments.\n");
			translatedText.add("In this Example the Algorithm used the Encryption-Function E(i) = i + k mod %s");
			translatedText.add("");
			
			//Pseudo Code
			translatedText.add("choose parameter:");
			translatedText.add("initialvector IV = {0,1}^n");
			translatedText.add("blocklength 0 < r < n+1");
			translatedText.add("encryption-key k");
			translatedText.add("init:");
			translatedText.add("set I_0 = IV");
			translatedText.add("for each block m_i with length r");
			translatedText.add("begin");
			translatedText.add("calculate E(I_(i-1))");
			translatedText.add("set O_i = I_i = result");
			translatedText.add("if length m_i < r then");
			translatedText.add("fill m_i with 0 untill length m_i = r");
			translatedText.add("set C_i = m_i xor (left r Bits of O_j)");
			translatedText.add("end");
			
			//steps
			translatedText.add("set initialvector (IV)");
			translatedText.add("the new value of I is used to calculate the new result of E(i)");
			translatedText.add("take the new value of I");
			translatedText.add("the actual message block is to short and is filled with 0 bits");
			translatedText.add("The current message block is encrypted by xored it with R bits of O");
			translatedText.add("the encrypted block ist saved in the output buffer");
			translatedText.add("All characters of the plaintext are now encrypted...");
			
			//outro description
			translatedText.add("After Iteration through the algorithm\n the plaintext %s\n is encrypted in\nthe chiffre %s\n\n The algorithm is now finished!!!");
			
			//others
			translatedText.add("2^Length_I");
			translatedText.add("Init");
			translatedText.add("Current Step:");			
		}
		
	}

	@Override
    public void init(){
        lang = new AnimalScript("Block Chiffre - Output Feedback Mode", "Fabian Möller, Matthias Mettel", 800, 600);
		lang.setStepMode(true);
    }

	@Override
    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
		//Extract Properties
		TITLE  = (TextProperties)props.getPropertiesByName("Titel");
		TITLE.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 24));
		TITLE2 = (TextProperties)props.getPropertiesByName("Titel2");
		TITLE2.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 16));
		TEXT   = (TextProperties)props.getPropertiesByName("Text");
		TITLE_RECT = (RectProperties)props.getPropertiesByName("Titelumrandung");
		ARRAY = (ArrayProperties)props.getPropertiesByName("Arrays");
		ARRAY.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.MONOSPACED, Font.PLAIN, 18));
		LINE = (PolylineProperties)props.getPropertiesByName("Linie");
		ARROW = (PolylineProperties)props.getPropertiesByName("Pfeil");
		HIGHLIGHTED_ARROW = (PolylineProperties)props.getPropertiesByName("Hervorgehobener Pfeil");
		CODE = (SourceCodeProperties)props.getPropertiesByName("Quellcode");
		CODE.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 14));
		
		//Extract Parameter
		plainText = (String)primitives.get("Klartext (binär)");
		LENGTH_R = (Integer)primitives.get("Länge R");
		KEY = (Integer)primitives.get("Schlüssel");
		String temp = (String)primitives.get("Initalisierungsvektor (binär)");
		IV = Arrays.copyOfRange(temp.split(""), 1, temp.length() + 1);
		LENGTH_I = IV.length;
		
		//Check Values
		Assert(LENGTH_R <= LENGTH_I);
		Assert(LENGTH_R > 0);
		
		//Generate Intro
		Intro();
		
		//Generate Encryption
		try
		{
			Encrypt();
		}
		catch(Exception e)
		{
			lang.addError("Error");
		}
		
		//Generate Outro
		Outro();
		
        return lang.toString();
    }

	@Override
    public String getName() {
        return "Output Feedback Mode (OFB)";
    }

	@Override
    public String getAlgorithmName() {
        return "Output Feedback Mode (OFB)";
    }

	@Override
    public String getAnimationAuthor() {
        return "Fabian Möller, Matthias Mettel";
    }

	@Override
    public String getDescription(){
        return String.format("%s\n%s\n%s\n\n%s\n%s", translatedText.get(0), translatedText.get(1), translatedText.get(2), String.format(translatedText.get(3), translatedText.get(27)), translatedText.get(4));
    }

	@Override
    public String getCodeExample(){
        return String.format("%s\n    %s\n    %s\n    %s\n\n%s\n    %s\n\n%s\n%s\n    %s\n    %s\n\n    %s\n        %s\n\n    %s\n%s",
				translatedText.get(5), translatedText.get(6), String.format(translatedText.get(7), "&lt;", "&lt;"),
				translatedText.get(8), translatedText.get(9), translatedText.get(10),
				translatedText.get(11), translatedText.get(12), translatedText.get(13),
				translatedText.get(14), translatedText.get(15), translatedText.get(16),
				translatedText.get(17), translatedText.get(18));
    }

	@Override
    public String getFileExtension(){
        return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
    }

	@Override
    public Locale getContentLocale() {
        return locale == Locale.GERMANY ? locale : Locale.US;
    }

	@Override
    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_CRYPT);
    }

	@Override
    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

	/**
	 * Method to create Intro Animation
	 */
	private void Intro() {
		//Create & Display Title
		titleText = lang.newText(new Coordinates(35, 25), "Block Chiffre - Output Feedback Mode (OFB)", "AnimationTitle", null, TITLE);
		titleBorder = lang.newRect(
				new Offset(-5, -5, titleText, DIRECTION_NW),
				new Offset(5, 5, titleText, DIRECTION_SE),
				"titleBorder", null, TITLE_RECT);

		//Create & Display Description
		SourceCode description = lang.newSourceCode(new Coordinates(30, 80), "desc", null);
		int modder = (int)Math.pow(2, LENGTH_I);
		String str = String.format("%d", modder);
		for(int i=0; i<2; i++)
			description.addCodeLine(translatedText.get(i), "", 0, null);
		description.addCodeLine("", "", 0, null);
		description.addCodeLine(String.format(translatedText.get(3), str), "", 0, null);
		description.addCodeLine(translatedText.get(4), "", 0, null);
		
		//Hide Algorithm Description after next step
		lang.nextStep("Start");
		description.hide();
	}
	
	private String flattenArray(StringArray s) {
	  if (s == null)
	    return "";
	  StringBuilder sb = new StringBuilder(2048);
	  int nrElems = s.getLength();
	  for (int i = 0; i < nrElems; i++)
	    sb.append(s.getData(i));
	  return sb.toString();
	}
	
	private void Encrypt() throws IllegalDirectionException{
		//Create & Display Labeled Plain Text Array
		StringArray plainTextArray = lang.newStringArray(new Offset(25, 35, titleBorder, DIRECTION_SW), Arrays.copyOfRange(plainText.split(""), 1, plainText.length() + 1), "PlainText", null, ARRAY);
		createLabel(lang, plainTextArray, "PlainTextLabel", "Klartext (binär):", 0);

		//Create & Display Labeled Chiffrate Array
		StringArray chiffreTextArray = lang.newStringArray(new Offset(0, 400, plainTextArray, DIRECTION_SW), createFilledStringArray(plainTextArray.getLength(), " "), "ChiffreText", null, ARRAY);
		createLabel(lang, chiffreTextArray, "ChiffreTextLabel", "Ciffrat (binär):", 0);

		//Create & Display Chiffre Input Array
		int center = Math.max(LENGTH_R * 16 + 85, 40 + (LENGTH_I / 2) * 16);
		StringArray chiffreInputArray = lang.newStringArray(new Coordinates(20 + center - (LENGTH_I / 2) * 16, 200), createFilledStringArray(LENGTH_I, " "), "chiffreInputArray", null, ARRAY);
		createLabel(lang, chiffreInputArray, "ChiffreInputLabel", "I[j]", 1);

		//Create PseudoCode Display
		SourceCode pseudoCode = lang.newSourceCode(new Offset(200, -75, chiffreInputArray, DIRECTION_NE), "Pseudo Code", null, CODE);
		pseudoCode.addCodeLine(translatedText.get(5), "params", 0, null);
		pseudoCode.addCodeLine(translatedText.get(6), "param", 2, null);
		pseudoCode.addCodeLine(String.format(translatedText.get(7), "<", "<"), "StartUp2", 2, null);
		pseudoCode.addCodeLine(translatedText.get(8), "StartUp3", 2, null);
		pseudoCode.addCodeLine("", "", 0, null);
		pseudoCode.addCodeLine(translatedText.get(9), "Init1", 0, null);
		pseudoCode.addCodeLine(translatedText.get(10), "Init2", 2, null);
		pseudoCode.addCodeLine("", "", 0, null);
		pseudoCode.addCodeLine(translatedText.get(11), "Bedingung", 0, null);
		pseudoCode.addCodeLine(translatedText.get(12), "Begin", 0, null);
		pseudoCode.addCodeLine(translatedText.get(13), "calc", 2, null);
		pseudoCode.addCodeLine(translatedText.get(14), "calc", 2, null);
		pseudoCode.addCodeLine("", "", 0, null);
		pseudoCode.addCodeLine(translatedText.get(15), "check", 2, null);
		pseudoCode.addCodeLine(translatedText.get(16), "fill", 4, null);
		pseudoCode.addCodeLine("", "", 0, null);
		pseudoCode.addCodeLine(translatedText.get(17), "chiffre", 2, null);
		pseudoCode.addCodeLine(translatedText.get(18), "End", 0, null);
		for (int i = 0; i < 4; i++) {
			pseudoCode.highlight(i);
		}

		//Create & Display Crypt Method
		String[] str = {"E(i) = i + " + KEY + " mod " + (1 << LENGTH_I)};
		StringArray enEk = lang.newStringArray(new Offset(-(str[0].length() * 11 / 2) + 3, 41, chiffreInputArray, DIRECTION_S), str, "CryptMethod", null, ARRAY);

		//Create & Display Chiffre Output Array
		StringArray chiffreOutputArray = lang.newStringArray(new Offset(0, 120, chiffreInputArray, DIRECTION_SW), createFilledStringArray(LENGTH_I, " "), "chiffreOutputArray", null, ARRAY);
		createLabel(lang, chiffreOutputArray, "ChiffreOutputLabel", "O[j]", 1);

		//Create & Display Initize Vector Input
		TEXT.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
		lang.newText(new Offset(0, -70, chiffreInputArray, DIRECTION_N), "IV", "InitizeVectorLabel", null, TEXT);
		TEXT.set(AnimationPropertiesKeys.CENTERED_PROPERTY, false);

		//Create & Display Operation Paths
		lang.newPolyline(asArray(
				new Offset(0, 0, chiffreInputArray, DIRECTION_S),
				new Offset(-5, 0, enEk, DIRECTION_N)), "pathItoE", null, ARROW);
		lang.newPolyline(asArray(
				new Offset(-5, 0, enEk, DIRECTION_S),
				new Offset(0, 0, chiffreOutputArray, DIRECTION_N)), "pathEtoO", null, ARROW);
		lang.newPolyline(asArray(
				new Offset(0, -50, chiffreInputArray, DIRECTION_N),
				new Offset(0, 0, chiffreInputArray, DIRECTION_N)), "pathItopToIbottom", null, ARROW);

		//Create Display Animation Operation path
		Polyline pathEtoI = lang.newPolyline(asArray(
				new Offset(-5, 0, enEk, DIRECTION_S),
				new Offset(-5, 15, enEk, DIRECTION_S),
				new Offset(100, 116, chiffreInputArray, DIRECTION_NE),
				new Offset(100, -30, chiffreInputArray, DIRECTION_NE),
				new Offset(0, -30, chiffreInputArray, DIRECTION_N),
				new Offset(0, 0, chiffreInputArray, DIRECTION_N)), "pathAnimOpFromEtoI", null, ARROW);

		//Create & Display XOR Symbol
		Circle circleXor = lang.newCircle(new Offset(0, 64, chiffreOutputArray, DIRECTION_S), 9, "XORCircle", null);
		lang.newPolyline(asArray(
				new Offset(0, 0, circleXor, DIRECTION_N),
				new Offset(0, 0, circleXor, DIRECTION_S)), "XORPlusVLine", null);
		lang.newPolyline(asArray(
				new Offset(0, 0, circleXor, DIRECTION_W),
				new Offset(0, 0, circleXor, DIRECTION_E)), "XORPlusHLine", null);

		//Create & Display MessageBlock Array
		StringArray messageBlockArray = lang.newStringArray(new Offset(-40 - LENGTH_R * 16, -6, circleXor, DIRECTION_N), createFilledStringArray(LENGTH_R, " "), "messageBlockArray", null, ARRAY);
		createLabel(lang, messageBlockArray, "messageBlockLabel", "M[j]", 1);

		//Create & Display ChiffreBlockArray
		StringArray chiffreBlockArray = lang.newStringArray(new Offset(40, -6, circleXor, DIRECTION_N), createFilledStringArray(LENGTH_R, " "), "chiffreBlockArray", null, ARRAY);
		createLabel(lang, chiffreBlockArray, "chiffreBlockLabel", "C[j]", 2);

		//Connection  XOR, M[j], O[j] & C[j]
		lang.newPolyline(asArray(
				new Offset(0, 0, messageBlockArray, DIRECTION_E),
				new Offset(0, 0, circleXor, DIRECTION_W)), " ", null, ARROW);
		lang.newPolyline(asArray(
				new Offset(0, 0, chiffreOutputArray, DIRECTION_S),
				new Offset(0, 0, circleXor, DIRECTION_N)), " ", null, ARROW);
		lang.newPolyline(asArray(
				new Offset(0, 0, circleXor, DIRECTION_E),
				new Offset(0, 0, chiffreBlockArray, DIRECTION_W)), " ", null, ARROW);

		//Create & Hide Animated Arrow
		Polyline highlightedArrow = lang.newPolyline(asArray(
				new Offset(20, 20, enEk, DIRECTION_S),
				new Offset(0, 0, enEk, DIRECTION_S)),
				"animHighlightedArrow", new Hidden(), HIGHLIGHTED_ARROW);

		//Create Step Description Section
		Text textCurStep = lang.newText(new Offset(-10, 30, chiffreTextArray, DIRECTION_SW), translatedText.get(29), "currentStepCaption", null, TITLE2);
		descriptionText = lang.newText(new Offset(10, 10, textCurStep, DIRECTION_SW), "", "currentStepDescription", null, TEXT);

		//Create Next Step
		lang.nextStep(translatedText.get(29));
		descriptionText.setText(translatedText.get(19), null, null);

		//change highlighting of PseudoCode
		unhighlightCodeLines(pseudoCode, 0, 4);
		highlightCodeLines(pseudoCode, 5, 7);

		//Fill I[j] Array with IV
		for (int i = 0; i < IV.length; ++i) {
			chiffreInputArray.put(i, IV[i], new TicksTiming(i * DEFAULTTIMING.getDelay()), DEFAULTTIMING);
			highlight(chiffreInputArray, i);
		}

		//Start Encryption
		for (int indexPlain = 0; indexPlain < plainTextArray.getLength(); indexPlain += LENGTH_R) {

			//Set current step description
			lang.nextStep(indexPlain == 0 ? "Begin Encryption" : "Nächster Block");
			descriptionText.setText(translatedText.get(20), null, null);
			
			//highlight the specific CodeLines
			unhighlightCodeLines(pseudoCode, 5, 7);
			pseudoCode.highlight(8);
			pseudoCode.highlight(10);
			
			//reset highlighted Arrow
			highlightedArrow.moveTo(DIRECTION_NW, null,
					new Offset(0, 0, enEk, DIRECTION_S),
					null, null);

			//Calculate E(I[j], k);
			highlight(enEk, 0);
//			int ij = Integer.parseInt(JoinArray(chiffreInputArray.getData()), 2);
      int ij = Integer.parseInt(flattenArray(chiffreInputArray), 2);
			int oj = E(ij, KEY);

			//Set Current step description
			lang.nextStep();
			descriptionText.setText(translatedText.get(21), null, null);

			//Change Highlighting for PseudoCode, CryptMethod & chiffreInput Array
			pseudoCode.unhighlight(10);
			pseudoCode.highlight(11);
			unhighlight(enEk, 0);
			unhighlight(chiffreInputArray, lang, 0, chiffreInputArray.getLength() - 1);

			//Set O[j] and I[j] with result from E(I[j-1], k)
			String[] ojOut = Int2Bin(oj, LENGTH_I);
			for (int i = 0; i < ojOut.length; ++i) {
				//Set O[j]
				chiffreOutputArray.put(i, ojOut[i], new TicksTiming(i * DEFAULTTIMING.getDelay()), DEFAULTTIMING);

				//Set I[j]
				chiffreInputArray.put(i, ojOut[i], new TicksTiming(i * DEFAULTTIMING.getDelay()), DEFAULTTIMING);
			}

			//Highlight O[j] & I[j]
			highlight(chiffreOutputArray, lang, 0, chiffreOutputArray.getLength() - 1);
			highlight(chiffreInputArray, lang, 0, chiffreInputArray.getLength() - 1);

			//Move Arrow
			highlightedArrow.show();
			highlightedArrow.moveVia(DIRECTION_NW, null, Clone(lang, pathEtoI), null, new TicksTiming(200));

			//set next step marker
			lang.nextStep();

			//Unhighlight all Primitives
			pseudoCode.unhighlight(11);
			unhighlight(chiffreOutputArray, lang, 0, chiffreOutputArray.getLength() - 1);
			unhighlight(chiffreInputArray, lang, 0, chiffreInputArray.getLength() - 1);

			//remove all unecessary primitives
			highlightedArrow.hide();

			//create next step marker
			lang.nextStep();

			//calc Last Index of the Block and Block Length
			int lastIndex = indexPlain + LENGTH_R - 1;
			int blockLen = LENGTH_R;
			if (lastIndex >= plainTextArray.getLength()) {
				blockLen = plainTextArray.getLength() % LENGTH_R;
			}
			
			//Highlight actual Block and clear messageBlock Array
			unhighlight(plainTextArray, lang, 0, plainTextArray.getLength() - 1);
			highlight(plainTextArray, lang, indexPlain, indexPlain + blockLen - 1);
			for (int i = 0; i < messageBlockArray.getLength(); ++i) {
				messageBlockArray.put(i, " ", null, null);
			}

			//Set Next Step Marker
			lang.nextStep();

			//Copy specific Array Range & move this to MessageBlockArray
			String[] enMjData = new String[blockLen];
			for (int pos = 0; pos < blockLen; pos++) {
			  enMjData[pos] = plainTextArray.getData(pos + indexPlain);
			}
//			String[] enMjData = Arrays.copyOfRange(plainTextArray.getData(), indexPlain, indexPlain + blockLen);
			moveArrayContent(enMjData, plainTextArray, messageBlockArray, indexPlain, false, false, false, false, null);

			//If BlockLength < LENGTH_R, fill with 0
			if (blockLen < LENGTH_R) {
				//Set Step Description & Highlight Code
				descriptionText.setText(translatedText.get(22), null, null);
				highlightCodeLines(pseudoCode, 13, 14);
				
				//Fille Array with 0 & Highlight Array
				for (int i = blockLen; i < LENGTH_R; ++i) {
					messageBlockArray.put(i, "0", new TicksTiming((i - blockLen) * DEFAULTTIMING.getDelay()), DEFAULTTIMING);
				}
				highlight(messageBlockArray, lang, 0, messageBlockArray.getLength() - 1);
				
				//Set Next Step Marker
				lang.nextStep();
				
				//Unhighlight Code
				pseudoCode.unhighlight(13);
				pseudoCode.unhighlight(14);
			}

			//Set Step Description & Highlight Code and Array
			descriptionText.setText(translatedText.get(23), null, null);
			pseudoCode.highlight(16);
			highlight(chiffreOutputArray, lang, 0, LENGTH_R - 1);
			
			//Calculate message XOR O[j]
      int mj = Integer.parseInt(flattenArray(messageBlockArray), 2);
//			int mj = Integer.parseInt(JoinArray(messageBlockArray.getData()), 2);
			int cj = mj ^ (oj >> (LENGTH_I - LENGTH_R));
			String[] cjOut = Int2Bin(cj, LENGTH_R);

			//Set Result to Array & Highlight Array
			for (int i = 0; i < cjOut.length; ++i) {
				chiffreBlockArray.put(i, cjOut[i], new TicksTiming(i * DEFAULTTIMING.getDelay()), DEFAULTTIMING);
			}
			highlight(chiffreBlockArray, lang, 0, chiffreBlockArray.getLength() - 1);

			//Set next Step Marker
			lang.nextStep();

			//Unhighlight All CodeLines & Arrays
			pseudoCode.unhighlight(16);
			unhighlight(chiffreBlockArray, lang, blockLen, LENGTH_R);
			unhighlight(chiffreOutputArray, lang, 0, chiffreOutputArray.getLength() - 1);
			unhighlight(messageBlockArray, lang, 0, messageBlockArray.getLength() - 1);

			//Set next Description & Step Marker
			lang.nextStep();
			descriptionText.setText(translatedText.get(24), null, null);

			//Copy & Move Result to Chiffre Array
			cjOut = Arrays.copyOf(cjOut, blockLen);
			moveArrayContent(cjOut, chiffreBlockArray, chiffreTextArray, 0, true, true, false, true, null);

			//Set Result to Chiffre Array & Highlight
			for (int i = blockLen; i < LENGTH_R; ++i) {
				chiffreBlockArray.put(i, " ", null, null);
			}
			unhighlight(chiffreBlockArray, lang, 0, chiffreBlockArray.getLength() - 1);
		}

		//unhighlight all highlighted primitives
		pseudoCode.unhighlight(8);
		unhighlight(plainTextArray, lang, 0, plainTextArray.getLength() - 1);
		
		//chiffre to string
		chiffreText = flattenArray(chiffreTextArray);
//    chiffreText = JoinArray(chiffreTextArray.getData());
		
		//set last step description
		pseudoCode.highlight(17);
		descriptionText.setText(translatedText.get(25), null, null);
		
		//set last step marker
		lang.nextStep("Ende");
		
		//Hide all Primitives
		lang.addLine("hideAll");
		titleBorder.show();
		titleText.show();
	}
	
	/**
	 * Outro Generator
	 */
	public void Outro() {		
		SourceCode outroDescription = lang.newSourceCode(new Offset(10, 45, titleBorder, DIRECTION_NW), "", null, CODE);
		String[] str = String.format(translatedText.get(26), plainText, chiffreText).split("\n");
		for(String s : str)
			outroDescription.addCodeLine(s, "", 0, null);
		
		
//		outroDescription.addCodeLine("Nach durchlaufen des Algorithmuses wurde der", "", 0, null);
//		outroDescription.addCodeLine("", "", 0, null);
//		outroDescription.addCodeLine("Klartext " + plainText, "", 0, null);
//		outroDescription.addCodeLine("", "", 0, null);
//		outroDescription.addCodeLine("in das", "", 0, null);
//		outroDescription.addCodeLine("", "", 0, null);
//		outroDescription.addCodeLine("Chiffrat " + chiffreText, "", 0, null);
//		outroDescription.addCodeLine("", "", 0, null);
//		outroDescription.addCodeLine("verschlüsselt.", "", 0, null);
//		outroDescription.addCodeLine("", "", 0, null);
//		outroDescription.addCodeLine("", "", 0, null);
//		outroDescription.addCodeLine("Damit ist der Algorithmus fertig!!!", "", 0, null);
	}
	
	/**
	 * Method to Move Array from Point to Point
	 * @param data Moving Array Data
	 * @param charArray destination Array
	 * @param buffer
	 * @param startPosition
	 * @param append
	 * @param unhighlight
	 * @param animateCopy
	 * @param deleteInput
	 * @param moveMessage
	 * @throws IllegalDirectionException 
	 */
	private void moveArrayContent(
			String[] data, StringArray charArray, StringArray buffer, int startPosition,
			boolean append, boolean unhighlight, boolean animateCopy, boolean deleteInput,
			String moveMessage) throws IllegalDirectionException {

		//Change Depth-Array-Property
		ARRAY.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		
		//Create & Highlight Moving Array
		StringArray charArray2 = lang.newStringArray(
				new Offset(startPosition * 16, 0, charArray, DIRECTION_NW),
				createFilledStringArray(data.length, " "),
				charArray.getName(),
				(ArrayDisplayOptions) charArray.getDisplayOptions(),
				ARRAY);
				
		for (int i = 0; i < data.length; ++i) {
			if (animateCopy) {
				charArray2.put(i, data[i], new TicksTiming(i * DEFAULTTIMING.getDelay()), DEFAULTTIMING);
			} else {
				charArray2.put(i, data[i], null, null);
			}

			if (deleteInput) {
				charArray.put(i, " ", null, null);
			}
		}
		highlight(charArray2, lang, 0, charArray2.getLength() - 1);
		
		//Reset Depth-Array-Property
		ARRAY.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);

		//if copy moving array, then create next step marker
		if (animateCopy) {
			lang.nextStep();
		}

		//if exists a moving message, then set it
		if (moveMessage != null) {
			descriptionText.setText(moveMessage, null, null);
		}

		//calculate new free index
		int freeIndex = append ? GetFreeIndex(buffer) : 0;

		//Move Array
		charArray2.moveTo(DIRECTION_NW, null, new Offset(freeIndex * 16, 0, buffer, DIRECTION_NW), null, LONGTIMING);
		
		//set highlight destination
		for (int i = 0; i < data.length; ++i) {
			buffer.put(freeIndex + i, charArray2.getData(i), LONGTIMING, null);
		}
		highlight(buffer, lang, freeIndex, freeIndex + data.length - 1);

		//create new next step marker
		lang.nextStep();

		//reset view
		charArray2.hide();
		if (unhighlight) {
			unhighlight(buffer, lang, 0, buffer.getLength() - 1);
		}
	}
	
	/**
	 * Encryption Method E
	 * @param i input-value
	 * @param k encryption key
	 * @return result of calculation
	 */
	private static int E(int i, int k) {
		return (i + k) % (1 << LENGTH_I);
	}

	/**
	 * calculate integer to binary string array
	 * @param i input value
	 * @param bits number of bits
	 * @return i as binary string
	 */
	private static String[] Int2Bin(int i, int bits) {
		String[] ret = new String[bits];
		int theI = i, theBits = bits;
		while (theBits > 0) {
			--theBits;
			ret[theBits] = (theI & 1) != 0 ? "1" : "0";
			theI >>= 1;
		}

		return ret;
	}

	/**
	 * Creates a list of Nodes from Nodes enumeration
	 * @param list enumeration of Nodes
	 * @return Array of Nodes
	 */
	private static Node[] asArray(Node... list) {
		return list;
	}

	/**
	 * Searchs for a Free Index in a Array Primitives
	 * @param array String Array Primitive
	 * @return freeIndex
	 */
	private static int GetFreeIndex(StringArray array) {
		int freeIndex;
		for (freeIndex = 0; freeIndex < array.getLength(); freeIndex++) {
			if (array.getData(freeIndex).equals(" ")) {
				break;
			}
		}
		return freeIndex;
	}

	/**
	 * Utility Method to check inputs
	 * @param val boolean value to check
	 */
	private static void Assert(boolean val) {
		if (!val) {
			throw new AssertionError();
		}
	}
//
//	/**
//	 * Converts String Array to String
//	 * @param arr StringArray to convert in String
//	 * @return Joined String
//	 */
//	private static String JoinArray(String[] arr) {
//		StringBuilder sb = new StringBuilder();
//		for (String s : arr) {
//			sb.append(s);
//		}
//		return sb.toString();
//	}

	/**
	 * Create a Label to a specific Array
	 * @param lang Animal Script Language
	 * @param array specific array
	 * @param name Name of the Label
	 * @param text Text of the Label
	 * @param direction Position of the Label relativ to the Array
	 */
	public static Text createLabel(Language lang, StringArray array, String name, String text, int direction) {
		Node node;
		
		switch(direction)
		{
			case 0:
				node = new Offset(-10, -25, array, AnimalScript.DIRECTION_NW);
			break;
			case 1:
				node = new Offset(-20, 0, array, AnimalScript.DIRECTION_NW);
			break;
			default:
				node = new Offset(10, 0, array, AnimalScript.DIRECTION_NE);
			break;
		}
		
		return lang.newText(node, text, name, null, TEXT);
	}
	
	/**
	 * creates a string array with a specific content
	 * @param length array length
	 * @param fillin content to put into the array
	 * @return return string array
	 */
	public static String[] createFilledStringArray(int length, String fillin) {
		String[] str = new String[length];

		Arrays.fill(str, fillin);

		return str;
	}
	
	/**
	 * utility method to highlight a range of codelines
	 * @param sc Source Code Primitive
	 * @param start start idx
	 * @param end end idx
	 */
	public static void highlightCodeLines(SourceCode sc, int start, int end) {
		for(int i=start; i<end; i++)
			sc.highlight(i);
	}
	
	/**
	 * utility method to unhighlight a range of codelines
	 * @param sc Source Code Primitive
	 * @param start start idx
	 * @param end end idx
	 */
	public static void unhighlightCodeLines(SourceCode sc, int start, int end) {
		for(int i=start; i<end; i++)
			sc.unhighlight(i);
	}
	
	/**
	 * highlight a array element
	 * @param array
	 * @param idx 
	 */
	public static void highlight(StringArray array, int idx) {
		array.highlightCell(idx, null, null);
		array.highlightElem(idx, null, null);
	}
	
	/**
	 * unhighlight a array element
	 * @param array
	 * @param idx 
	 */
	public static void unhighlight(StringArray array, int idx) {
		array.unhighlightCell(idx, null, null);
		array.unhighlightElem(idx, null, null);
	}

	/**
	 * highlight elements in range of the array
	 * @param array
	 * @param lang
	 * @param start
	 * @param end 
	 */
	public static void highlight(StringArray array, Language lang, int start, int end) {
		lang.addLine("highlightArrayCell on \"" + array.getName() + "\" from " + start + " to " + end);
		lang.addLine("highlightArrayElem on \"" + array.getName() + "\" from " + start + " to " + end);
	}

	/**
	 * unhighlight elements in range of the array
   * @param array
   * @param lang
   * @param start
   * @param end 
	 */
	public static void unhighlight(StringArray array, Language lang, int start, int end) {
		lang.addLine("unhighlightArrayCell on \"" + array.getName() + "\" from " + start + " to " + end);
		lang.addLine("unhighlightArrayElem on \"" + array.getName() + "\" from " + start + " to " + end);
	}
	
	/**
	 * Clones a Line
	 * @param lang Animal Script Language
	 * @param p Polyline
	 * @return new PolyLine
	 */
	public static Polyline Clone(Language lang, Polyline p) {
		return lang.newPolyline(p.getNodes(), p.getName(), p.getDisplayOptions(), p.getProperties());
	}
}