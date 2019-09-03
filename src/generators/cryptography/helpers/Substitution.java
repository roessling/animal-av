package generators.cryptography.helpers;
import java.awt.Color;
import java.awt.Font;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;

public class Substitution {

	private static Language lang;

	public static final String SOURCE_CODE = "function encrypt(String plainText) \n"
			+ "    String cipherText := \\\"\\\" \n"
			+ "\n"
			+ "    for each character c in plainText: \n"
			+ "        // substitute represents the predefined substitution function. \n"
			+ "        char c' := substitute(c)\n"
			+ "        cipherText.append(c') \n\n" 
			+ "    return cipherText \n";

	private Map<Character, Character> map;
	private String domain;
	private String range;

	private Text header;

	private Text subHeader;
	private Text textVal1;
	private Text textVal2;

	private SourceCode src;

	public Substitution(String domain, String range) {
		this.domain = domain;
		this.range = range;

		map = new HashMap<Character, Character>();
		for (int i = 0; i < domain.length(); ++i) {
			if (!map.containsKey(domain.charAt(i)))
				map.put(domain.charAt(i), range.charAt(i));
		}
	}
	
	public String apply(String input) {
		ArrayProperties arrayProps = new ArrayProperties();
		arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
		arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.white);
		arrayProps.set(AnimationPropertiesKeys.DIRECTION_PROPERTY, false);
		arrayProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"Monospaced", Font.PLAIN, 20));

		// plain text
		lang.newText(new Coordinates(700, 190), "Plain text: ", "p", null)
				.setFont(new Font("SansSerif", Font.PLAIN, 20), null, null);

		StringArray plainText = lang.newStringArray(new Coordinates(850, 190),
				stringToStringArray(input), "plainText", null, arrayProps);

		// cipher text
		lang.newText(new Coordinates(700, 300), "Cipher text: ", "c", null)
				.setFont(new Font("SansSerif", Font.PLAIN, 20), null, null);

		String[] emptyCipher = new String[input.length()];
		for (int i = 0; i < emptyCipher.length; i++) {
			emptyCipher[i] = " ";
		}
		StringArray cipherText = lang.newStringArray(new Coordinates(850, 300),
				emptyCipher, "cipherText", null, arrayProps);

		// substitution function
		lang.newText(new Coordinates(700, 390), "Substitution function: ",
				"subst", null).setFont(new Font("SansSerif", Font.PLAIN, 20),
				null, null);

		String[][] subst = new String[2][domain.length()];
		subst[0] = stringToStringArray(domain);
		subst[1] = stringToStringArray(range);

		StringMatrix substFunc = lang.newStringMatrix(
				new Coordinates(850, 420), subst, "substfunc", null);
		lang.nextStep("Example");

		ArrayMarker plainTextMarker = lang.newArrayMarker(plainText, 0,
				"plainTextMarker", null);
		ArrayMarker cipherTextMarker = lang.newArrayMarker(cipherText, 0,
				"cipherTextMarker", null);
		StringBuilder sb = new StringBuilder(input.length());

		Text noSubst = lang.newText(new Coordinates(970, 490),
				"no substitution, keeping value!", "noSubst", null);
		noSubst.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED,
				null, null);
		noSubst.hide();

		animToggleSource(1, 1, true);
		lang.nextStep();
		animToggleSource(1, 1, false);

		for (int i = 0; i < input.length(); ++i) {
			animToggleSource(3, 3, true);
			lang.nextStep();
			if (map.get(input.charAt(i)) != null) {
				sb.append(map.get(input.charAt(i)));
				substFunc.highlightCellRowRange(0, 1, domain.indexOf(input
						.charAt(i)), null, null);
				animToggleSource(3, 3, false);
				animToggleSource(5, 5, true);
				lang.nextStep();
				cipherText.put(i, map.get(input.charAt(i)).toString(), null,
						null);
				animToggleSource(5, 5, false);
				animToggleSource(6, 6, true);
				lang.nextStep();
				animToggleSource(6, 6, false);
				substFunc.unhighlightCellRowRange(0, 1, domain.indexOf(input
						.charAt(i)), null, null);

			} else {
				animToggleSource(3, 3, false);
				noSubst.show();
				sb.append(input.charAt(i));
				cipherText.put(i, ((Character) input.charAt(i)).toString(),
						null, null);
				lang.nextStep();
				noSubst.hide();
			}
			plainTextMarker.increment(null, null);
			cipherTextMarker.increment(null, null);

		}
		animToggleSource(0, 6, false);
		animToggleSource(8, 8, true);
		plainTextMarker.hide();
		cipherTextMarker.hide();
		lang.nextStep();
		return sb.toString();
	}

	public void initSlide() {
		// title
		header = lang.newText(new Coordinates(20, 30),
				"Mono-alphabetic Substitution Cipher Demonstration", "header", null);
		header.setFont(new Font("SansSerif", Font.BOLD, 24), null, null);
		lang.nextStep("Introduction");

		// objective
		subHeader = lang.newText(new Coordinates(30, 80), "Objective:",
				"objective", null);
		subHeader.setFont(new Font("SansSerif", Font.BOLD, 20), null, null);
		textVal1 = lang
				.newText(
						new Coordinates(30, 110),
						"Mono-alphabetic Substitution Ciphers 'encrypt' a given plain text by replacing every character in"
								+ " the plain text with a different character according to a predefined substitution.",
						"objectiveval", null);
		textVal2 = lang
				.newText(
						new Coordinates(30, 130),
						"Popular examples of simple substitution ciphers are the"
								+ " Ceaser Cipher, the ROT13 Cipher, and the BASE64 Encoding.",
						"objectiveval", null);
		textVal1.setFont(new Font("SansSerif", Font.PLAIN, 16), null, null);
		textVal2.setFont(new Font("SansSerif", Font.PLAIN, 16), null, null);

		lang.nextStep("Objective");

		// set up the source code
		SourceCodeProperties srcProps = new SourceCodeProperties();
		srcProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"Monospaced", Font.PLAIN, 14));
		srcProps
				.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		src = lang.newSourceCode(new Coordinates(30, 190), "code", null,
				srcProps);

		String[] code = SOURCE_CODE.split("\n");
		for (int i = 0; i < code.length; i++)
			src.addCodeLine(code[i], null, 1, null);

		lang.nextStep("Pseudo-Code");
	}

	public void finalSlide(String plainText, String cipher, String pattern) {
		// hide example header and source code
		src.hide();

		subHeader.setText("Conclusion:", null, null);
		textVal1.setText("The resulting 'cipher' text is: " + cipher + ".",
				null, null);
		textVal2
				.setText(
						"The 'decryption' is possible if the substitution function is bijective.",
						null, null);
		lang.nextStep("Conclusion");
	}

	private static String[] stringToStringArray(String in) {
		String[] inArray = in.split("");
		String[] outArray = new String[inArray.length - 1];
		for (int i = 1; i < inArray.length; i++) {
			outArray[i - 1] = inArray[i];
		}
		return outArray;
	}

	private void animToggleSource(int from, int to, boolean highlight) {
		animToggleSource(from, to, highlight, 0);
	}

	private void animToggleSource(int from, int to, boolean highlight, int delay) {
		for (int i = from; i <= to; ++i) {
			if (highlight)
				src.highlight(i, -1, false, new MsTiming(delay), null);
			else
				src.unhighlight(i, -1, false, new MsTiming(delay), null);
		}
	}
	
	public static void setLanguage(Language l) {
		lang = l;
	}

	public static void main(String[] args) {
		lang = new AnimalScript("Substitution Cipher Demonstration",
				"Dominik Bollmann, Sogol Mazaheri", 2000, 1500);
		lang.setStepMode(true);

		String inputAlphabet = "abcd";
		String substAlphabet = "dcba";
		Substitution substCipher = new Substitution(inputAlphabet,
				substAlphabet);

		String plainText = "HelabcdloWorld";

		substCipher.initSlide();
		String cipher = substCipher.apply(plainText);
		substCipher.finalSlide(plainText, cipher, substAlphabet);

		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(
					"subst-gen.asu"));
			out.writeBytes(lang.toString());
			out.close();
			System.out
					.println("Ok, ANIMAL script has been generated and written to 'subst-gen.asu'");
		} catch (FileNotFoundException e) {
			System.err.println("subst-gen.asu cannot be opened for writing!");
		} catch (IOException e) {
			System.err.println("writing the animal script caused an error :/");
			e.printStackTrace();
		}

	}
}
