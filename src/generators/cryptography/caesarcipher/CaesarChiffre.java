package generators.cryptography.caesarcipher;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;

public class CaesarChiffre implements generators.framework.Generator {

	private static Language	lang;

	public CaesarChiffre() {
		lang = new AnimalScript("Caesar-Chiffre",
				"Henrik Schröder, Nedislav Nedyalkov, Latinka Pavlova", 1024, 768);
	}

	public CaesarChiffre(Language l) {
		lang = l;
	}

	private static final String	DESCRIPTION	= "Die Cäsar-Chiffre ist ein einfaches Verschlüsselungsverfahren, "
																							+ " bei dem jeder Buchstabe um eine feste Anzahl von Stellen im Alphabet verschoben wird.";

	private static final String	SOURCE_CODE	= "public static String encrypt(int shiftValue, byte[] plainText) { \n\n "
																							+ "   String[] plainAlphabet = {'A','B','C','D','E','F','G','H','I','J','K','L','M',\n"
																							+ "                             'N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};\n"
																							+ "   String[] cipherAlphabet = new String[26];\n\n"
																							+ "   for (int i = 0; i < plainAlphabet.length; i++) {\n"
																							+ "      cipherAlphabet[i] = plainAlphabet[(i+shiftValue)%26];\n"
																							+ "   }\n\n"
																							+ "   StringBuilder cipherText = new StringBuilder(plainText.length);\n\n"
																							+ "   for (int i = 0; i < plainText.length; i++) {\n"
																							+ "      cipherText.append(cipherAlphabet[(Byte.valueOf(plainText[i])-65)%26]);\n"
																							+ "   }\n\n"
																							+ "   return cipherText.toString();\n"
																							+ "}";

	protected String getAlgorithmDescription() {
		return DESCRIPTION;
	}

	protected String getAlgorithmCode() {
		return SOURCE_CODE;
	}

	public String getName() {
		return "Caesar-Chiffre";
	}

	public String getDescription() {
		return DESCRIPTION;
	}

	public String getCodeExample() {
		return SOURCE_CODE;
	}

	@Override
	public String generate(AnimationPropertiesContainer arg0,
			Hashtable<String, Object> arg1) {

		int shiftValue;

		String allowedLetters = "abcdefghijklmnopqrstuvwxyz";

		String inputTemp = arg1.get("Plaintext").toString().trim();

		StringBuilder sbPlainText = new StringBuilder(inputTemp.length());

		for (int i = 0; i < inputTemp.length(); i++) {
			if (allowedLetters.indexOf(inputTemp.substring(i, i + 1).toLowerCase()) != -1) {
				sbPlainText.append(inputTemp.substring(i, i + 1).toUpperCase());
			}
		}

		inputTemp = sbPlainText.toString();

		String[] arrPlainTextInput = new String[inputTemp.length()];

		for (int i = 0; i < arrPlainTextInput.length; i++) {
			arrPlainTextInput[i] = inputTemp.substring(i, i + 1);
		}

		// put spaces to narrow letters so that the arrays look even
		for (int i = 0; i < arrPlainTextInput.length; i++) {
			if (arrPlainTextInput[i].equalsIgnoreCase("I")) {
				arrPlainTextInput[i] = " I ";
			} else if (arrPlainTextInput[i].equalsIgnoreCase("J")) {
				arrPlainTextInput[i] = "J ";
			} else if (arrPlainTextInput[i].equalsIgnoreCase("L")) {
				arrPlainTextInput[i] = "L ";
			} else if (arrPlainTextInput[i].equalsIgnoreCase("T")) {
				arrPlainTextInput[i] = "T ";
			} else if (arrPlainTextInput[i].equalsIgnoreCase("Y")) {
				arrPlainTextInput[i] = "Y ";
			} else if (arrPlainTextInput[i].equalsIgnoreCase("Z")) {
				arrPlainTextInput[i] = "Z ";
			}
		}

		// read shift value

		shiftValue = Integer.parseInt(arg1
				.get("Shiftvalue(minValue=1,maxValue=25)").toString());

		if (shiftValue < 1) {
			shiftValue = 1;
		} else if (shiftValue > 25) {
			shiftValue = 25;
		}

		lang.setStepMode(true);

		// create the Caesar-Cipher header with the rectangle around it.
		RectProperties rp = new RectProperties();
		rp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
		rp.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		lang.newRect(new Coordinates(120, 10), new Coordinates(315, 41),
				"HeaderRect", null, rp);
		TextProperties tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
				Font.BOLD, 20));
		lang.newText(new Coordinates(140, 15), "Caesar-Cipher", "Header", null, tp);

		// draw vertical line
		lang.newRect(new Coordinates(490, 5), new Coordinates(490, 600),
				"vertLine", null, rp);

		// display source code

		SourceCodeProperties scProps = (SourceCodeProperties) arg0
				.getPropertiesByName("SourceCode");

		SourceCode sc = lang.newSourceCode(new Coordinates(500, 100), "sourceCode",
				null, scProps);

		sc.addCodeLine("public static String encrypt", null, 0, null); // 0
		sc.addCodeLine("(int shiftValue, ", null, 16, null);// 1
		sc.addCodeLine("byte[] plainText) {", null, 17, null);// 2
		sc.addCodeLine("", null, 0, null);// 3
		sc
				.addCodeLine(
						"String[] plainAlphabet = {'A','B','C','D','E','F','G','H','I','J','K','L','M',",
						null, 3, null);
		sc.addCodeLine("'N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};",
				null, 18, null);
		sc.addCodeLine("String[] cipherAlphabet = new String[26];", null, 3, null);
		sc.addCodeLine("", null, 0, null);
		sc.addCodeLine("for (int i = 0; i < plainAlphabet.length; i++) {", null, 3,
				null);
		sc.addCodeLine("cipherAlphabet[i] = plainAlphabet[(i+shiftValue)%26];",
				null, 6, null);
		sc.addCodeLine("}", null, 3, null);
		sc.addCodeLine("", null, 0, null);
		sc.addCodeLine(
				"StringBuilder cipherText = new StringBuilder(plainText.length);",
				null, 3, null);
		sc.addCodeLine("", null, 0, null);
		sc.addCodeLine("for (int i = 0; i < plainText.length; i++) {", null, 3,
				null);
		sc.addCodeLine(
				"cipherText.append(cipherAlphabet[Byte.valueOf(plainText[i])-65]);",
				null, 6, null);
		sc.addCodeLine("}", null, 3, null);
		sc.addCodeLine("", null, 0, null);
		sc.addCodeLine("return cipherText.toString();", null, 3, null);
		sc.addCodeLine("}", null, 3, null);

		lang.nextStep();////////////////////////////////////////////////////////////
										// /
										// /////////////////////////////////////////////////////////
										// ///////

		// display shift value
		TextProperties textProp = (TextProperties) arg0.getPropertiesByName("Text");
		textProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
				Font.BOLD, 14));
		Text tshiftValue = lang.newText(new Coordinates(20, 100), "Shift Value = "
				+ shiftValue, "verschiebeWert", null, textProp);

		// highlight shiftValue
		sc.highlight(1);

		lang.nextStep();////////////////////////////////////////////////////////////
										// /
										// /////////////////////////////////////////////////////////
										// ///////

		// set plain text properties

		ArrayProperties prop_plainText = (ArrayProperties) arg0
				.getPropertiesByName("Arrays");

		// display plain text
		lang.newText(new Coordinates(20, 150), "Plaintext", "plaintext", null,
				textProp);

		StringArray strArrPlainText = lang.newStringArray(new Coordinates(20, 180),
				arrPlainTextInput, "stringArray", null, prop_plainText);

		// highlight plainText

		sc.unhighlight(1);
		sc.highlight(2);

		lang.nextStep();////////////////////////////////////////////////////////////
										// /
										// /////////////////////////////////////////////////////////
										// ///////

		// display plain alphabet

		lang.newText(new Coordinates(20, 270), "Plain alphabet", "plain alphabet",
				null, textProp);
		String[] plainAlphabet = { "A", "B", "C", "D", "E", "F", "G", "H", " I ",
				"J ", "K", "L ", "M", "N", "O", "P", "Q", "R", "S", "T ", "U", "V",
				"W", "X", "Y ", "Z " };
		StringArray strArrPlainAlphabet = lang.newStringArray(new Coordinates(20,
				300), plainAlphabet, "Alphabet1", null, prop_plainText);

		// hightlight plain alphabet
		sc.unhighlight(2);
		sc.highlight(4);
		sc.highlight(5);

		lang.nextStep();////////////////////////////////////////////////////////////
										// /
										// /////////////////////////////////////////////////////////
										// ///////

		// display empty cipher alphabet

		String[] cipherAlphabet = new String[26];
		for (int i = 0; i < cipherAlphabet.length; i++) {
			cipherAlphabet[i] = "   ";
		}

		lang.newText(new Coordinates(20, 330), "Cipher alphabet",
				"cipher alphabet", null, textProp);
		StringArray strArrCipherAlphbet = lang.newStringArray(new Coordinates(20,
				360), cipherAlphabet, "Alphabet2", null, prop_plainText);

		// highlight cipher alphabet
		sc.unhighlight(4);
		sc.unhighlight(5);
		sc.highlight(6);

		lang.nextStep();////////////////////////////////////////////////////////////
										// /
										// /////////////////////////////////////////////////////////
										// ///////

		// highlight first cells in each array

		strArrCipherAlphbet.highlightCell(0, null, null);

		strArrPlainAlphabet.highlightCell(0, null, null);

		// highlight corresponding source code

		sc.unhighlight(6);
		sc.highlight(8);
		sc.highlight(9);
		sc.highlight(10);

		lang.nextStep();////////////////////////////////////////////////////////////
										// /
										// /////////////////////////////////////////////////////////
										// ///////

		// show shift counter

		int shiftCounter = shiftValue;

		Text tShiftCounter = lang.newText(new Coordinates(200, 270), "Shift: "
				+ String.valueOf(shiftCounter), "shift counter", null, textProp);
		tShiftCounter.changeColor(null, Color.RED, null, null);
		tshiftValue.changeColor(null, Color.RED, null, null);

		lang.nextStep();////////////////////////////////////////////////////////////
										// /
										// /////////////////////////////////////////////////////////
										// ///////

		// change text color back to black

		tShiftCounter.changeColor(null, Color.BLACK, null, null);
		tshiftValue.changeColor(null, Color.BLACK, null, null);

		lang.nextStep();////////////////////////////////////////////////////////////
										// /
										// /////////////////////////////////////////////////////////
										// ///////

		// shift position in first array
		for (int i = 0; i < shiftValue; i++) {
			shiftCounter--;
			strArrPlainAlphabet.unhighlightCell(i % 26, null, null);
			strArrPlainAlphabet.highlightCell((i + 1) % 26, null, null);
			tShiftCounter.hide();
			tShiftCounter = lang.newText(new Coordinates(200, 270), "Shift: "
					+ String.valueOf(shiftCounter), "shift counter", null, textProp);
			lang.nextStep();
		}

		////////////////////////////////////////////////////////////////////////////
		// //////////////////////////////

		// put first letter into cipher alphabet
		strArrCipherAlphbet.put(0, plainAlphabet[shiftValue % 26], null, null);

		lang.nextStep();////////////////////////////////////////////////////////////
										// /
										// /////////////////////////////////////////////////////////
										// ///////

		// hide shift counter
		tShiftCounter.hide();

		lang.nextStep();////////////////////////////////////////////////////////////
										// /
										// /////////////////////////////////////////////////////////
										// ///////

		// move to second element
		strArrPlainAlphabet.unhighlightCell(shiftValue % 26, null, null);
		strArrCipherAlphbet.unhighlightCell(0, null, null);

		strArrPlainAlphabet.highlightCell((shiftValue + 1) % 26, null, null);
		strArrCipherAlphbet.highlightCell(1, null, null);

		lang.nextStep();////////////////////////////////////////////////////////////
										// /
										// /////////////////////////////////////////////////////////
										// ///////

		// put second element in cipher alphabet

		strArrCipherAlphbet
				.put(1, plainAlphabet[(shiftValue + 1) % 26], null, null);

		lang.nextStep();////////////////////////////////////////////////////////////
										// /
										// /////////////////////////////////////////////////////////
										// ///////

		// move to third element
		strArrPlainAlphabet.unhighlightCell((shiftValue + 1) % 26, null, null);
		strArrCipherAlphbet.unhighlightCell(1, null, null);

		strArrPlainAlphabet.highlightCell((shiftValue + 2) % 26, null, null);
		strArrCipherAlphbet.highlightCell(2, null, null);

		lang.nextStep();////////////////////////////////////////////////////////////
										// /
										// /////////////////////////////////////////////////////////
										// ///////

		// put third element in cipher alphabet

		strArrCipherAlphbet
				.put(2, plainAlphabet[(shiftValue + 2) % 26], null, null);

		lang.nextStep();////////////////////////////////////////////////////////////
										// /
										// /////////////////////////////////////////////////////////
										// ///////

		strArrPlainAlphabet.unhighlightCell((shiftValue + 2) % 26, null, null);
		strArrCipherAlphbet.unhighlightCell(2, null, null);

		// fill cipher-alphabet except last 3 fields

		String[] strArrCipherAlmostFull = new String[26];
		for (int i = 0; i < 23; i++) {
			strArrCipherAlmostFull[i] = strArrPlainAlphabet
					.getData((i + shiftValue) % 26);
		}
		strArrCipherAlmostFull[23] = "   ";
		strArrCipherAlmostFull[24] = "   ";
		strArrCipherAlmostFull[25] = "   ";

		strArrCipherAlphbet.hide();
		strArrCipherAlphbet = lang.newStringArray(new Coordinates(20, 360),
				strArrCipherAlmostFull, "Alphabet3", null, prop_plainText);

		strArrPlainAlphabet.highlightCell((shiftValue + 23) % 26, null, null);
		strArrCipherAlphbet.highlightCell(23, null, null);

		lang.nextStep();////////////////////////////////////////////////////////////
										// /
										// /////////////////////////////////////////////////////////
										// ///////

		// put 24. element in cipher alphabet

		strArrCipherAlphbet.put(23, plainAlphabet[(shiftValue + 23) % 26], null,
				null);

		lang.nextStep();////////////////////////////////////////////////////////////
										// /
										// /////////////////////////////////////////////////////////
										// ///////

		// move to 25. element
		strArrPlainAlphabet.unhighlightCell((shiftValue + 23) % 26, null, null);
		strArrCipherAlphbet.unhighlightCell(23, null, null);

		strArrPlainAlphabet.highlightCell((shiftValue + 24) % 26, null, null);
		strArrCipherAlphbet.highlightCell(24, null, null);

		lang.nextStep();////////////////////////////////////////////////////////////
										// /
										// /////////////////////////////////////////////////////////
										// ///////

		// put 25. element in cipher alphabet

		strArrCipherAlphbet.put(24, plainAlphabet[(shiftValue + 24) % 26], null,
				null);

		lang.nextStep();////////////////////////////////////////////////////////////
										// /
										// /////////////////////////////////////////////////////////
										// ///////

		// move to 26. element
		strArrPlainAlphabet.unhighlightCell((shiftValue + 24) % 26, null, null);
		strArrCipherAlphbet.unhighlightCell(24, null, null);

		strArrPlainAlphabet.highlightCell((shiftValue + 25) % 26, null, null);
		strArrCipherAlphbet.highlightCell(25, null, null);

		lang.nextStep();////////////////////////////////////////////////////////////
										// /
										// /////////////////////////////////////////////////////////
										// ///////

		// put 26. element in cipher alphabet

		strArrCipherAlphbet.put(25, plainAlphabet[(shiftValue + 25) % 26], null,
				null);

		lang.nextStep();////////////////////////////////////////////////////////////
										// /
										// /////////////////////////////////////////////////////////
										// ///////

		// unhighlight 26. element
		strArrPlainAlphabet.unhighlightCell((shiftValue + 25) % 26, null, null);
		strArrCipherAlphbet.unhighlightCell(25, null, null);

		lang.nextStep();////////////////////////////////////////////////////////////
										// /
										// /////////////////////////////////////////////////////////
										// ///////

		// show cipherText Array + text

		String[] ciphTxt = new String[strArrPlainText.getLength()];

		for (int i = 0; i < ciphTxt.length; i++) {
			ciphTxt[i] = "   ";
		}

		lang.newText(new Coordinates(20, 470), "Ciphertext", "ciphertext", null,
				textProp);
		StringArray strArrCipherText = lang.newStringArray(
				new Coordinates(20, 500), ciphTxt, "CipherText", null, prop_plainText);

		// highlight corresponding sourcecode
		sc.unhighlight(8);
		sc.unhighlight(9);
		sc.unhighlight(10);

		sc.highlight(12);

		lang.nextStep();////////////////////////////////////////////////////////////
										// ///////////

		// highlight next code-section

		sc.unhighlight(12);

		sc.highlight(14);
		sc.highlight(15);
		sc.highlight(16);

		lang.nextStep();////////////////////////////////////////////////////////////
										// ///////////

		// encrypt

		int alphPos = 0;

		for (int i = 0; i < strArrPlainText.getLength(); i++) {
			strArrPlainText.highlightCell(i, null, null);
			lang.nextStep();

			for (int j = 0; j < 26; j++) {
				if (strArrPlainText.getData(i).trim().equalsIgnoreCase(
						strArrPlainAlphabet.getData(j).trim())) {
					alphPos = j;
					strArrPlainAlphabet.highlightCell(j, null, null);
					lang.nextStep();
					break;
				}
			}

			strArrCipherAlphbet.highlightCell(alphPos, null, null);
			lang.nextStep();

			strArrCipherText.highlightCell(i, null, null);
			strArrCipherText.put(i, strArrCipherAlphbet.getData(alphPos), null, null);
			lang.nextStep();

			strArrPlainText.unhighlightCell(i, null, null);
			strArrPlainAlphabet.unhighlightCell(alphPos, null, null);
			strArrCipherAlphbet.unhighlightCell(alphPos, null, null);
			strArrCipherText.unhighlightCell(i, null, null);

		}

		sc.unhighlight(14);
		sc.unhighlight(15);
		sc.unhighlight(16);

		sc.highlight(18);

		return lang.toString();

	}

	@Override
	public String getAlgorithmName() {
		return "Caesar Cipher";
	}

	@Override
	public Locale getContentLocale() {
		return Locale.US;
	}

	@Override
	public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	@Override
	public GeneratorType getGeneratorType() {
		return new generators.framework.GeneratorType(
				generators.framework.GeneratorType.GENERATOR_TYPE_CRYPT);
	}

	@Override
	public String getOutputLanguage() {
		return generators.framework.Generator.JAVA_OUTPUT;
	}

	@Override
	public String getAnimationAuthor() {
		return "Henrik Schröder, Nedislav Nedyalkov, Latinka Pavlova";
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

}