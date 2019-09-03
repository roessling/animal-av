/*
 * ARC4.java
 * David Becker, 2015 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.cryptography;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.MultipleSelectionQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import algoanim.primitives.Circle;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.CircleProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.OffsetFromLastPosition;

import java.util.Hashtable;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import animal.variables.VariableRoles;

public class ARC4 implements ValidatingGenerator {
	private Language lang;
	private String inputPlain;
	private String keyPlain;
	private boolean withQuiz;
	private Color headlineColor;
	private Color subheadlineColor;
	private Color arrayMarkerColor;
	private Color siColor;
	private Color sjColor;
	private Color pColor;
	private Color kColor;

	private TextProperties defaultText;
	private TextProperties parameterText;
	private SourceCodeProperties primaryHighlightingSourceCode;
	private SourceCodeProperties secondaryHighlightingSourceCode;
	private SourceCodeProperties textSourceCode;

	private Variables v;

	public void init() {
		lang = new AnimalScript("ARC4",
				"David Becker<davidbeckerlulz@googlemail.com>", 800, 600);
		lang.setStepMode(true);
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {

		// Primitives
		inputPlain = (String) primitives.get("plaintext");
		keyPlain = (String) primitives.get("key");
		withQuiz = ((Boolean) primitives.get("activate quiz")).booleanValue();
		headlineColor = (Color) primitives.get("Headline FillColor");
		subheadlineColor = (Color) primitives.get("Subheadline Color");
		arrayMarkerColor = (Color) primitives.get("Array Marker Color");
		siColor = (Color) primitives.get("S[i] FillColor");
		sjColor = (Color) primitives.get("S[j] FillColor");
		pColor = (Color) primitives.get("P FillColor");
		kColor = (Color) primitives.get("K FillColor");

		defaultText = (TextProperties) props
				.getPropertiesByName("Default Text");
		parameterText = (TextProperties) props
				.getPropertiesByName("Parameter Text");
		primaryHighlightingSourceCode = (SourceCodeProperties) props
				.getPropertiesByName("Primary Highlighting SourceCode");
		secondaryHighlightingSourceCode = (SourceCodeProperties) props
				.getPropertiesByName("Secondary Highlighting SourceCode");
		textSourceCode = (SourceCodeProperties) props
				.getPropertiesByName("Text SourceCode");

		v = lang.newVariables();
		v.declare("String", "plaintextString", inputPlain,
				animal.variables.Variable
						.getRoleString(VariableRoles.FIXED_VALUE));
		v.declare("String", "keyString", keyPlain, animal.variables.Variable
				.getRoleString(VariableRoles.FIXED_VALUE));
		v.declare("String", "keylength", Integer.toString(keyPlain.length()),
				animal.variables.Variable
						.getRoleString(VariableRoles.FIXED_VALUE));

		runARC4();

		if (withQuiz)
			lang.finalizeGeneration();

		return lang.toString();
	}

	private void runARC4() {

		// Init algorithm parameters
		byte[] input = inputPlain.getBytes(StandardCharsets.UTF_8);
		/*
		 * Output generation could be done in-place, use different array to
		 * facilitate visualisation
		 */
		byte[] output = new byte[input.length];
		byte[] key = keyPlain.getBytes(StandardCharsets.UTF_8);

		StringBuilder keyAsHexBuilder = new StringBuilder();
		for (byte keyVal : key) {
			// keyAsHexBuilder.append("0x");
			keyAsHexBuilder.append(String.format("%02x", keyVal));
			keyAsHexBuilder.append(",");
		}
		StringBuilder inputAsHexBuilder = new StringBuilder();
		for (byte inputVal : input) {
			// inputAsHexBuilder.append("0x");
			inputAsHexBuilder.append(String.format("%02x", inputVal));
			inputAsHexBuilder.append(",");
		}

		v.declare(
				"String",
				"plaintextArray",
				"\u2022 byte[]: ["
						+ inputAsHexBuilder.toString().substring(0,
								inputAsHexBuilder.length() - 1) + "]",
				animal.variables.Variable
						.getRoleString(VariableRoles.FIXED_VALUE));
		v.declare(
				"String",
				"keyArray",
				"\u2022 byte[]: ["
						+ keyAsHexBuilder.toString().substring(0,
								keyAsHexBuilder.length() - 1) + "]",
				animal.variables.Variable
						.getRoleString(VariableRoles.FIXED_VALUE));

		byte[] state = new byte[256];

		// Text Properties
		TextProperties defaultTextProperties = new TextProperties();
		defaultTextProperties.set("centered", true);
		defaultTextProperties.set("color", defaultText.get("color"));
		defaultTextProperties.set("depth", 2);
		defaultTextProperties.set("font",
				new Font("Monospaced",
						Font.BOLD, 16));

		TextProperties parameterText12Properties = new TextProperties();
		parameterText12Properties.set("color", parameterText.get("color"));
		parameterText12Properties.set("depth", 2);
		parameterText12Properties.set("font",
				new Font("Monospaced",
						Font.PLAIN, 12));

		TextProperties parameterText13Properties = new TextProperties();
		parameterText13Properties.set("color", parameterText.get("color"));
		parameterText13Properties.set("depth", 2);
		parameterText13Properties.set("font",
				new Font("Monospaced",
						Font.PLAIN, 13));

		TextProperties parameterBoldTextProperties = new TextProperties();
		parameterBoldTextProperties.set("color", parameterText.get("color"));
		parameterBoldTextProperties.set("depth", 2);
		parameterBoldTextProperties.set("font",
				new Font("Monospaced",
						Font.BOLD, 13));

		// SourceCode Properties
		SourceCodeProperties sourceCodePrimaryHighlightProperties = new SourceCodeProperties();
		sourceCodePrimaryHighlightProperties.set("color",
				primaryHighlightingSourceCode.get("color"));
		sourceCodePrimaryHighlightProperties.set(
				"font",
				new Font("Monospaced", Font.BOLD,
						(int) primaryHighlightingSourceCode.get("size")));
		sourceCodePrimaryHighlightProperties.set("highlightColor",
				primaryHighlightingSourceCode.get("highlightColor"));

		SourceCodeProperties sourceCodeSecondaryHighlightProperties = new SourceCodeProperties();
		sourceCodeSecondaryHighlightProperties.set("color",
				secondaryHighlightingSourceCode.get("color"));
		sourceCodeSecondaryHighlightProperties.set(
				"font",
				new Font("Monospaced", Font.BOLD,
						(int) secondaryHighlightingSourceCode.get("size")));
		sourceCodeSecondaryHighlightProperties.set("highlightColor",
				secondaryHighlightingSourceCode.get("highlightColor"));

		// SourceCodeText Properties
		SourceCodeProperties sourceCodeTextProperties = new SourceCodeProperties();
		sourceCodeTextProperties.set("color", textSourceCode.get("color"));
		sourceCodeTextProperties.set("font",
				new Font("Monospaced",
						Font.PLAIN, (int) textSourceCode.get("size")));
		sourceCodeTextProperties.set("highlightColor",
				textSourceCode.get("highlightColor"));

		// Title #1: ARC4
		TextProperties headerProperties = new TextProperties();
		headerProperties.set("centered", true);
		headerProperties.set("depth", 0);
		headerProperties.set("color", Color.BLACK);
		headerProperties.set("font", new Font("SansSerif", Font.PLAIN, 20));
		@SuppressWarnings("unused")
		Text header = lang.newText(new Coordinates(375, 20), "ARC4", "header",
				null, headerProperties);

		RectProperties headerRectProperties = new RectProperties();
		headerRectProperties.set("depth", 1);
		headerRectProperties.set("filled", true);
		headerRectProperties.set("fillColor", headlineColor);
		Rect headerRect = lang.newRect(new Offset(-10, -5, "header", "NW"),
				new Offset(10, 5, "header", "SE"), "headerRect", null,
				headerRectProperties);

		RectProperties plusRectProperties = new RectProperties();
		plusRectProperties.set("depth", 1);
		plusRectProperties.set("filled", true);
		plusRectProperties.set("fillColor", arrayMarkerColor);

		CircleProperties headerCircleProperties = new CircleProperties();
		headerCircleProperties.set("depth", 1);
		headerCircleProperties.set("filled", true);
		headerCircleProperties.set("fillColor", arrayMarkerColor);

		// Subtitle #1: Introduction
		TextProperties subheaderProperties = new TextProperties();
		subheaderProperties.set("centered", true);
		subheaderProperties.set("color", subheadlineColor);
		subheaderProperties.set("depth", 2);
		subheaderProperties.set("font", new Font("MonoSpaced", Font.BOLD, 16));
		Text subheader = lang.newText(new Offset(0, 20, headerRect, "C"),
				"Introduction", "subheader", null, subheaderProperties);

		// Introduction #1: Introduction to Algorithm + History
		SourceCode codeText = lang.newSourceCode(new Offset(-345, 43,
				headerRect, "S"), "codeText", null, sourceCodeTextProperties);
		codeText.addCodeLine(
				"RC4 (Rivest Cipher 4, also known as ARC4 or ARCFOUR meaning Alleged RC4) is a software",
				null, 0, null);
		codeText.addCodeLine(
				"stream cipher used to encrypt a plaintext. Its simplicity and speed make it the most",
				null, 0, null);
		codeText.addCodeLine(
				"commonly used stream cipher and allows for efficient implementations that are easy to",
				null, 0, null);
		codeText.addCodeLine("develop.", null, 0, null);
		codeText.addCodeLine("", null, 0, null);
		codeText.addCodeLine(
				"RC4 was designed by Ron Rivest of RSA Security in 1987. Even though it was a trade secret",
				null, 0, null);
		codeText.addCodeLine(
				"initially, the algorithm itself got leaked in 1994. The identifier ARC4 (Alleged RC4) ",
				null, 0, null);
		codeText.addCodeLine(
				"stems from the fact that the name RC4 is trademarked.", null,
				0, null);
		codeText.addCodeLine("", null, 0, null);
		codeText.addCodeLine(
				"RC4 generates a pseudorandom stream of bits (a keystream). Encryption and decryption",
				null, 0, null);
		codeText.addCodeLine(
				"are both achieved by combining the keystream with the plaintext using bit-wise xor.",
				null, 0, null);
		codeText.addCodeLine("", null, 0, null);
		codeText.addCodeLine(
				"Note: The content of the byte arrays is displayed in hexadecimal format.",
				null, 0, null);

		lang.nextStep("General Introduction");

		// Introduction #2: Key-scheduling algorithm (KSA)
		codeText.hide();
		subheader.hide();

		subheader = lang.newText(new Offset(0, 27, headerRect, "C"),
				"Key-scheduling algorithm", "subheader", null,
				subheaderProperties);

		if (withQuiz) {
			MultipleSelectionQuestionModel multipleSelectionQuestionModel = new MultipleSelectionQuestionModel(
					"ARC4 Advantages");
			multipleSelectionQuestionModel
					.setPrompt("Check all advantages of ARC4:");
			multipleSelectionQuestionModel.addAnswer("Easy to develop.", 1,
					"Easy to develop - Yes.");
			multipleSelectionQuestionModel
					.addAnswer(
							"No vulnerabilities have been found in ARC4 so far.",
							0,
							"No vulnerabilities have been found in ARC4 so far - Wrong, the opposite is true and forms the main weakness of ARC4.");
			multipleSelectionQuestionModel
					.addAnswer(
							"Good speed even though using a separate nonce alongside the key.",
							0,
							"Good speed even though using a separate nonce alongside the key - One part of the statement is true.");
			lang.addMSQuestion(multipleSelectionQuestionModel);
		}

		SourceCode code = lang.newSourceCode(new Offset(-166, 47, headerRect,
				"S"), "code", null, sourceCodePrimaryHighlightProperties);
		code.addCodeLine("for i from 0 to 255", null, 0, null);
		code.addCodeLine("S[i] := i", null, 2, null);
		code.addCodeLine("endfor", null, 0, null);
		code.addCodeLine("j := 0", null, 0, null);
		code.addCodeLine("for i from 0 to 255", null, 0, null);
		code.addCodeLine("j := (j + S[i] + key[i mod keylength])", null, 2,
				null);
		code.addCodeLine("     mod 256", null, 2, null);
		code.addCodeLine("swap values of S[i] and S[j]", null, 2, null);
		code.addCodeLine("endfor", null, 0, null);

		RectProperties codeRectProperties = new RectProperties();
		codeRectProperties.set("depth", 2);
		codeRectProperties.set("color", Color.BLACK);

		Rect codeRect = lang.newRect(new Offset(-4, -2, code, "NW"),
				new Offset(4, 2, code, "SE"), "codeRect", null,
				codeRectProperties);

		codeText = lang.newSourceCode(new Offset(-345, 227, headerRect, "S"),
				"codeText", null, sourceCodeTextProperties);
		codeText.addCodeLine(
				"To generate the keystream, the cipher makes use of a secret internal state consisting of:",
				null, 0, null);
		codeText.addCodeLine(
				"\u2022 A permutation of all 256 possible bytes, S", null, 0,
				null);
		codeText.addCodeLine("\u2022 Two 8-bit index-pointers, i and j", null,
				0, null);
		codeText.addCodeLine("", null, 0, null);

		lang.nextStep("Introduction - Key-scheduling algorithm (KSA)");

		code.highlight(0);
		code.highlight(1);
		code.highlight(2);
		code.highlight(3);
		codeText.addCodeLine(
				"First initialise the array S to the identity permutation. Also initialise j to 0.",
				null, 0, null);
		codeText.addCodeLine("", null, 0, null);
		codeText.highlight(4);

		lang.nextStep();

		code.unhighlight(0);
		code.unhighlight(1);
		code.unhighlight(2);
		code.unhighlight(3);
		code.highlight(4);
		code.highlight(5);
		code.highlight(6);
		code.highlight(7);
		code.highlight(8);
		codeText.addCodeLine(
				"Process S for 256 iterations, swapping elements S[i] and S[j].",
				null, 0, null);
		codeText.addCodeLine(
				"j is determined by mixing in bytes of the plaintext, key and old value of j.",
				null, 0, null);
		codeText.addCodeLine(
				"keylength is defined as the number of bytes in the key and can be in the range",
				null, 0, null);
		codeText.addCodeLine(
				"1 \u2264 keylength \u2264 256, typically between 5 and 16.",
				null, 0, null);
		codeText.unhighlight(4);
		codeText.highlight(6);
		codeText.highlight(7);
		codeText.highlight(8);
		codeText.highlight(9);

		if (withQuiz) {
			MultipleChoiceQuestionModel multipleChoiceQuestionModel0 = new MultipleChoiceQuestionModel(
					"KSA Understanding");
			multipleChoiceQuestionModel0
					.setPrompt("Given the same plaintext, does the key-scheduling algorithm generate the same state?");
			multipleChoiceQuestionModel0
					.addAnswer(
							"Yes, the same plaintext always results in the same state!",
							0,
							"Consider how the key affects the state generation.");
			multipleChoiceQuestionModel0.addAnswer(
					"Yes, if the used keys are identical.", 1,
					"That's correct!");
			multipleChoiceQuestionModel0
					.addAnswer(
							"No, the generated state is never identical.",
							0,
							"Try looking at the algorithm again and think about what might be different between two algorithm runs!");
			lang.addMCQuestion(multipleChoiceQuestionModel0);
		}

		lang.nextStep();

		// Key-scheduling algorithm (KSA): Setup
		code.hide();
		codeRect.hide();
		codeText.hide();
		subheader.hide();

		subheader = lang.newText(new Offset(0, 27, headerRect, "C"),
				"Animation - KSA", "subheader", null, subheaderProperties);

		RectProperties stateRectProperties = new RectProperties();
		stateRectProperties.set("depth", 3);
		stateRectProperties.set("filled", false);
		Rect arrayStateRect = lang.newRect(new Offset(-256, 30, "subheader",
				"C"), new Offset(257, 80, "subheader", "C"), "arrayStateRect",
				null, stateRectProperties);
		Text arrayStateText = lang.newText(new Offset(-30, -8, arrayStateRect,
				"W"), "S", "arrayStateText", null, defaultTextProperties);
		Text iIdentifierText = lang.newText(new Offset(30, -28, arrayStateRect,
				"E"), "i:", "iStateText", null, defaultTextProperties);
		Text jIdentifierText = lang.newText(new Offset(30, 13, arrayStateRect,
				"E"), "j:", "jStateText", null, defaultTextProperties);

		code = lang.newSourceCode(new Offset(-95, 240, arrayStateRect, "SW"),
				"code", null, sourceCodePrimaryHighlightProperties);
		code.addCodeLine("Key-scheduling algorithm (KSA)", null, 0, null);
		code.addCodeLine("for i from 0 to 255", null, 0, null);
		code.addCodeLine("S[i] := i", null, 2, null);
		code.addCodeLine("endfor", null, 0, null);
		code.addCodeLine("j := 0", null, 0, null);
		code.addCodeLine("for i from 0 to 255", null, 0, null);
		code.addCodeLine("j := (j + S[i] + key[i mod keylength])", null, 2,
				null);
		code.addCodeLine("     mod 256", null, 2, null);
		code.addCodeLine("swap values of S[i] and S[j]", null, 2, null);
		code.addCodeLine("endfor", null, 0, null);

		codeRect = lang.newRect(new Offset(-4, -2, code, "NW"), new Offset(4,
				2, code, "SE"), "codeRect", null, codeRectProperties);

		code.highlight(1);
		code.highlight(2);
		code.highlight(3);
		code.highlight(4);

		String parametersString0 = "Key";
		String parametersString1 = "\u2022 keylength: " + key.length
				+ "   \u2022 String: " + keyPlain;
		String parametersString2 = "\u2022 byte[]: ["
				+ keyAsHexBuilder.toString().substring(0,
						keyAsHexBuilder.length() - 1) + "]";
		String parametersString3 = "Input";
		String parametersString4 = "\u2022 String: " + inputPlain;
		String parametersString5 = "\u2022 byte[]: ["
				+ inputAsHexBuilder.toString().substring(0,
						inputAsHexBuilder.length() - 1) + "]";
		String parametersString6 = "Output";
		String parametersString7 = "\u2022 byte[]: ";

		Text parameters0 = lang
				.newText(new Offset(28, -3, codeRect, "NE"), parametersString0,
						"parameters0", null, parameterBoldTextProperties);
		Text parameters1 = lang.newText(new Offset(0, 5, "parameters0", "SW"),
				parametersString1, "parameters1", null,
				parameterText12Properties);
		Text parameters2 = lang.newText(new Offset(0, 5, "parameters1", "SW"),
				parametersString2, "parameters2", null,
				parameterText12Properties);
		Text parameters3 = lang
				.newText(new Offset(0, 12, "parameters2", "SW"),
						parametersString3, "parameters3", null,
						parameterBoldTextProperties);
		Text parameters4 = lang.newText(new Offset(0, 5, "parameters3", "SW"),
				parametersString4, "parameters4", null,
				parameterText12Properties);
		Text parameters5 = lang.newText(new Offset(0, 5, "parameters4", "SW"),
				parametersString5, "parameters5", null,
				parameterText12Properties);
		Text parameters6 = lang
				.newText(new Offset(0, 12, "parameters5", "SW"),
						parametersString6, "parameters6", null,
						parameterBoldTextProperties);
		Text parameters7 = lang.newText(new Offset(0, 5, "parameters6", "SW"),
				parametersString7, "parameters7", null,
				parameterText12Properties);

		Text iValueText = lang.newText(new Offset(35, 2, iIdentifierText, "N"),
				"0", "iValueText", null, defaultTextProperties);
		Text jValueText = lang.newText(new Offset(35, 2, jIdentifierText, "N"),
				"0", "jValueText", null, defaultTextProperties);

		lang.nextStep("Animation - KSA State Init");

		// Key-scheduling algorithm (KSA): Algorithm - Init

		iValueText.hide();

		Text arrayInitValueText = null;

		int j = 0;
		int old_j = 0;

		for (int i = 0; i < 256; i++) {
			byte iByte = (byte) i;
			state[i] = iByte;
			iValueText = lang.newText(new Offset(35, 0, iIdentifierText, "N"),
					Integer.toString(i), "iValueText", (i < 11) ? new MsTiming(
							i * 330) : new MsTiming(3267 + i * 25),
					defaultTextProperties);
			arrayInitValueText = lang.newText(new Offset(5, 40, arrayStateRect,
					"C"), "S[" + i + "] := 0x" + String.format("%02X ", iByte),
					"arrayInitValueText", (i < 11) ? new MsTiming(i * 330)
							: new MsTiming(3267 + i * 25),
					defaultTextProperties);

			if (i < 255) {
				iValueText.hide((i < 11) ? new MsTiming(330 + i * 330)
						: new MsTiming(3300 + i * 25));
				arrayInitValueText.hide((i < 11) ? new MsTiming(330 + i * 330)
						: new MsTiming(3300 + i * 25));
			}

			RectProperties rectAnimationFillProperties = new RectProperties();
			rectAnimationFillProperties.set("color", arrayMarkerColor);
			rectAnimationFillProperties.set("fillColor", arrayMarkerColor);

			Rect arrayInitRect = lang.newRect(new Offset(1 + (i * 2), 1,
					"arrayStateRect", "NW"), new Offset(2 + (i * 2), 49,
					"arrayStateRect", "NW"), "arrayInitRect",
					(i < 11) ? new MsTiming(i * 330) : new MsTiming(
							3267 + i * 25), rectAnimationFillProperties);
			arrayInitRect.hide(new MsTiming(3630 + i * 25));
		}

		lang.nextStep();

		// Key-scheduling algorithm (KSA): Algorithm - Processing
		arrayInitValueText.hide();
		code.hide();
		iValueText.hide();

		code = lang.newSourceCode(new Offset(-95, 240, arrayStateRect, "SW"),
				"code", null, sourceCodePrimaryHighlightProperties);
		code.addCodeLine("Key-scheduling algorithm (KSA)", null, 0, null);
		code.addCodeLine("for i from 0 to 255", null, 0, null);
		code.addCodeLine("S[i] := i", null, 2, null);
		code.addCodeLine("endfor", null, 0, null);
		code.addCodeLine("j := 0", null, 0, null);
		code.addCodeLine("for i from 0 to 255", null, 0, null);
		code.addCodeLine("", null, 0, null);
		code.addCodeLine("", null, 0, null);
		code.addCodeLine("", null, 0, null);
		code.addCodeLine("endfor", null, 0, null);
		code.highlight(5);
		code.highlight(9);

		SourceCode secondaryCode = lang.newSourceCode(new Offset(-95, 342,
				arrayStateRect, "SW"), "secondaryCode", null,
				sourceCodeSecondaryHighlightProperties);
		secondaryCode.addCodeLine("j := (j + S[i] + key[i mod keylength])",
				null, 2, null);
		secondaryCode.addCodeLine("     mod 256", null, 2, null);
		secondaryCode
				.addCodeLine("swap values of S[i] and S[j]", null, 2, null);

		iValueText = lang.newText(new Offset(35, 0, iIdentifierText, "N"), "0",
				"iValueText", null, defaultTextProperties);

		RectProperties stateRectSiProperties = new RectProperties();
		stateRectSiProperties.set("depth", 3);
		stateRectSiProperties.set("filled", true);
		stateRectSiProperties.set("fillColor", siColor);
		RectProperties stateRectSjProperties = new RectProperties();
		stateRectSjProperties.set("depth", 3);
		stateRectSjProperties.set("filled", true);
		stateRectSjProperties.set("fillColor", sjColor);

		Rect arraySiStateRect = lang.newRect(new Offset(-100, 90,
				"arrayStateRect", "C"), new Offset(-30, 120, "arrayStateRect",
				"C"), "arraySiStateRect", null, stateRectSiProperties);
		Text arraySiStateText = lang.newText(new Offset(0, 10,
				arraySiStateRect, "S"), "S[i]", "arraySiStateText", null,
				defaultTextProperties);
		Rect arraySjStateRect = lang.newRect(new Offset(30, 90,
				"arrayStateRect", "C"), new Offset(100, 120, "arrayStateRect",
				"C"), "arraySjStateRect", null, stateRectSjProperties);
		Text arraySjStateText = lang.newText(new Offset(0, 10,
				arraySjStateRect, "S"), "S[j]", "arraySjStateText", null,
				defaultTextProperties);

		PolylineProperties polylineSiProperties = new PolylineProperties();
		polylineSiProperties.set("color", siColor);
		polylineSiProperties.set("fwArrow", true);
		PolylineProperties polylineSjProperties = new PolylineProperties();
		polylineSjProperties.set("color", sjColor);
		polylineSjProperties.set("fwArrow", true);
		PolylineProperties polylineSiWithoutArrowProperties = new PolylineProperties();
		polylineSiWithoutArrowProperties.set("color", siColor);
		polylineSiWithoutArrowProperties.set("fwArrow", false);
		PolylineProperties polylineSjWithoutArrowProperties = new PolylineProperties();
		polylineSjWithoutArrowProperties.set("color", sjColor);
		polylineSjWithoutArrowProperties.set("fwArrow", false);
		RectProperties rectSiProperties = new RectProperties();
		rectSiProperties.set("color", siColor);
		rectSiProperties.set("fillColor", siColor);
		rectSiProperties.set("filled", true);
		RectProperties rectSjProperties = new RectProperties();
		rectSjProperties.set("color", sjColor);
		rectSjProperties.set("fillColor", sjColor);
		rectSjProperties.set("filled", true);

		lang.nextStep("Animation - KSA Processing");

		// variables
		Polyline arrayToSiPolyline0 = null;
		Polyline arrayToSiPolyline1 = null;
		Polyline arrayToSiPolyline2 = null;
		Polyline arrayToSjPolyline0 = null;
		Polyline arrayToSjPolyline1 = null;
		Polyline arrayToSjPolyline2 = null;
		Polyline siToArrayPolyline0 = null;
		Polyline siToArrayPolyline1 = null;
		Polyline siToArrayPolyline2 = null;
		Polyline sjToArrayPolyline0 = null;
		Polyline sjToArrayPolyline1 = null;
		Polyline sjToArrayPolyline2 = null;
		Rect markSiRect = null;
		Rect markSjRect = null;
		Text siValueText = null;
		Text sjValueText = null;
		Text jCalculationText0 = null;
		Text jCalculationText1 = null;

		// step part, no animation
		for (int i = 0; i < 10; i++) {
			/*
			 * Key-scheduling algorithm (KSA): Algorithm - Processing Calculate
			 * j
			 */
			iValueText.hide();
			jValueText.hide();
			arraySiStateText.hide();
			arraySjStateText.hide();
			if (i > 0) {
				siToArrayPolyline0.hide();
				siToArrayPolyline1.hide();
				siToArrayPolyline2.hide();
				sjToArrayPolyline0.hide();
				sjToArrayPolyline1.hide();
				sjToArrayPolyline2.hide();
				siValueText.hide();
				sjValueText.hide();
				markSiRect.hide();
				markSjRect.hide();
			}

			secondaryCode.highlight(0);
			secondaryCode.highlight(1);
			secondaryCode.unhighlight(2);

			v.declare("String", "iKSA", Integer.toString(i),
					animal.variables.Variable
							.getRoleString(VariableRoles.WALKER));

			// & 0xff == %256
			old_j = j;
			j = (j + state[i] + key[i % key.length]) & 0xff;

			v.declare("String", "jKSA", Integer.toString(j),
					animal.variables.Variable
							.getRoleString(VariableRoles.MOST_WANTED_HOLDER));

			iValueText = lang.newText(new Offset(35, 0, iIdentifierText, "N"),
					Integer.toString(i), "iValueText", null,
					defaultTextProperties);
			jValueText = lang.newText(new Offset(35, 2, jIdentifierText, "N"),
					Integer.toString(j), "jValueText", null,
					defaultTextProperties);
			arraySiStateText = lang.newText(new Offset(0, 10, arraySiStateRect,
					"S"), "S[" + i + "]", "arraySiStateText", null,
					defaultTextProperties);
			arraySjStateText = lang.newText(new Offset(0, 10, arraySjStateRect,
					"S"), "S[" + j + "]", "arraySjStateText", null,
					defaultTextProperties);
			siValueText = lang.newText(
					new Offset(5, -8, arraySiStateRect, "C"),
					"0x" + String.format("%02X ", state[i]), "siValueText",
					null, defaultTextProperties);
			sjValueText = lang.newText(
					new Offset(5, -8, arraySjStateRect, "C"),
					"0x" + String.format("%02X ", state[j]), "sjValueText",
					null, defaultTextProperties);
			jCalculationText0 = lang.newText(new Offset(-265, 170,
					arrayStateRect, "S"), "j := (" + old_j + " + S[" + i
					+ "] + key[" + i + " % " + key.length + "]) % 256"
					+ " := (" + old_j + " + S[" + i + "] + key["
					+ (i % key.length) + "]) % 256", "jCalculationText0", null,
					parameterText13Properties);
			jCalculationText1 = lang.newText(
					new Offset(0, 9, jCalculationText0, "SW"),
					"  := (" + old_j + " + 0x"
							+ String.format("%02x", state[i]) + " + 0x"
							+ String.format("%02x", key[i % key.length])
							+ ") % 256 := (" + old_j + " + " + state[i] + " + "
							+ key[i % key.length] + ") % 256 := " + j,
					"jCalculationText1", null, parameterText13Properties);

			// polylines from array to boxes
			arrayToSiPolyline0 = lang.newPolyline(new Node[] {
					new Offset(0 + (i * 2), 1, "arrayStateRect", "SW"),
					new Offset(-1, -3, "arraySiStateRect", "N") },
					"arrayToSi0", null, polylineSiWithoutArrowProperties);
			arrayToSiPolyline1 = lang.newPolyline(new Node[] {
					new Offset(1 + (i * 2), 1, "arrayStateRect", "SW"),
					new Offset(0, -3, "arraySiStateRect", "N") }, "arrayToSi1",
					null, polylineSiProperties);
			arrayToSiPolyline2 = lang.newPolyline(new Node[] {
					new Offset(2 + (i * 2), 1, "arrayStateRect", "SW"),
					new Offset(1, -3, "arraySiStateRect", "N") }, "arrayToSi2",
					null, polylineSiWithoutArrowProperties);
			arrayToSjPolyline0 = lang.newPolyline(new Node[] {
					new Offset(0 + (j * 2), 1, "arrayStateRect", "SW"),
					new Offset(-1, -3, "arraySjStateRect", "N") },
					"arrayToSj0", null, polylineSjWithoutArrowProperties);
			arrayToSjPolyline1 = lang.newPolyline(new Node[] {
					new Offset(1 + (j * 2), 1, "arrayStateRect", "SW"),
					new Offset(0, -3, "arraySjStateRect", "N") }, "arrayToSj1",
					null, polylineSjProperties);
			arrayToSjPolyline2 = lang.newPolyline(new Node[] {
					new Offset(2 + (j * 2), 1, "arrayStateRect", "SW"),
					new Offset(1, -3, "arraySjStateRect", "N") }, "arrayToSj2",
					null, polylineSjWithoutArrowProperties);

			/*
			 * marks position in array
			 */
			markSiRect = lang.newRect(new Offset((i * 2), 1, "arrayStateRect",
					"NW"), new Offset(2 + (i * 2), 49, "arrayStateRect", "NW"),
					"markSi", null, rectSiProperties);
			markSjRect = lang.newRect(new Offset((j * 2), 1, "arrayStateRect",
					"NW"), new Offset(2 + (j * 2), 49, "arrayStateRect", "NW"),
					"markSj", null, rectSjProperties);

			if (withQuiz) {
				if (i == 1) {
					MultipleChoiceQuestionModel multipleChoiceQuestionModel1 = new MultipleChoiceQuestionModel(
							"KSA Swapping: General Understanding");
					multipleChoiceQuestionModel1
							.setPrompt("How often is each element in S[] accessed?");
					multipleChoiceQuestionModel1
							.addAnswer(
									"At most once.",
									0,
									"That's not correct. What would prevent one element from getting accessed more than once?");
					multipleChoiceQuestionModel1.addAnswer("At least once.", 1,
							"That's correct!");
					multipleChoiceQuestionModel1
							.addAnswer(
									"At least twice.",
									0,
									"Even though each element is accessed twice statistically, this is not guaranteed.");
					lang.addMCQuestion(multipleChoiceQuestionModel1);
				}
			}

			lang.nextStep();

			/*
			 * Key-scheduling algorithm (KSA): Algorithm - Processing Swap S[i]
			 * and S[j]
			 */
			jCalculationText0.hide();
			jCalculationText1.hide();
			secondaryCode.unhighlight(0);
			secondaryCode.unhighlight(1);
			secondaryCode.highlight(2);
			arrayToSiPolyline0.hide();
			arrayToSjPolyline0.hide();
			arrayToSiPolyline1.hide();
			arrayToSjPolyline1.hide();
			arrayToSiPolyline2.hide();
			arrayToSjPolyline2.hide();
			markSiRect.hide();
			markSjRect.hide();

			byte temp = state[i];
			state[i] = state[j];
			state[j] = temp;

			// polylines from boxes to array
			siToArrayPolyline0 = lang.newPolyline(new Node[] {
					new Offset(-1, -1, "arraySiStateRect", "N"),
					new Offset(0 + (j * 2), 0, "arrayStateRect", "SW") },
					"siToArray0", null, polylineSiWithoutArrowProperties);
			siToArrayPolyline1 = lang.newPolyline(new Node[] {
					new Offset(0, -1, "arraySiStateRect", "N"),
					new Offset(1 + (j * 2), 0, "arrayStateRect", "SW") },
					"siToArray1", null, polylineSiProperties);
			siToArrayPolyline2 = lang.newPolyline(new Node[] {
					new Offset(1, -1, "arraySiStateRect", "N"),
					new Offset(2 + (j * 2), 0, "arrayStateRect", "SW") },
					"siToArray2", null, polylineSiWithoutArrowProperties);
			sjToArrayPolyline0 = lang.newPolyline(new Node[] {
					new Offset(-1, -1, "arraySjStateRect", "N"),
					new Offset(0 + (i * 2), 0, "arrayStateRect", "SW") },
					"sjToArray0", null, polylineSjWithoutArrowProperties);
			sjToArrayPolyline1 = lang.newPolyline(new Node[] {
					new Offset(0, -1, "arraySjStateRect", "N"),
					new Offset(1 + (i * 2), 0, "arrayStateRect", "SW") },
					"sjToArray1", null, polylineSjProperties);
			sjToArrayPolyline2 = lang.newPolyline(new Node[] {
					new Offset(1, -1, "arraySjStateRect", "N"),
					new Offset(2 + (i * 2), 0, "arrayStateRect", "SW") },
					"sjToArray2", null, polylineSjWithoutArrowProperties);

			/*
			 * marks position in array
			 */
			markSiRect = lang.newRect(new Offset((i * 2), 1, "arrayStateRect",
					"NW"), new Offset(2 + (i * 2), 49, "arrayStateRect", "NW"),
					"markSi2", null, rectSjProperties);
			markSjRect = lang.newRect(new Offset((j * 2), 1, "arrayStateRect",
					"NW"), new Offset(2 + (j * 2), 49, "arrayStateRect", "NW"),
					"markSj2", null, rectSiProperties);

			if (withQuiz) {
				if (i == 2) {
					MultipleChoiceQuestionModel multipleChoiceQuestionModel2 = new MultipleChoiceQuestionModel(
							"KSA Swapping: j Calculation");
					multipleChoiceQuestionModel2
							.setPrompt("Calculate j for the next iteration. Help: Remember that i gets increased by 1 and the key values have hexadecimal formatting. State["
									+ (i + 1) + "]: " + state[i + 1]);
					multipleChoiceQuestionModel2.addAnswer(
							Integer.toString((j + state[i + 1] + key[(i + 1)
									% key.length]) & 0xff), 1,
							"That's correct!");
					multipleChoiceQuestionModel2.addAnswer(
							Integer.toString((j + state[i]
									+ key[(i + 1) % key.length] + 177) & 0xff),
							0, "That's not correct!");
					multipleChoiceQuestionModel2.addAnswer(
							Integer.toString((j + state[i + 1]
									+ key[i % key.length] + 1337) & 0xff), 0,
							"That's not correct!");
					lang.addMCQuestion(multipleChoiceQuestionModel2);
				}
				if (i == 4) {
					MultipleChoiceQuestionModel multipleChoiceQuestionModel3 = new MultipleChoiceQuestionModel(
							"KSA Swapping: j Calculation Revisited");
					multipleChoiceQuestionModel3
							.setPrompt("Calculate j for the next iteration. Help: Remember that i gets increased by 1 and the key values have hexadecimal formatting. State["
									+ (i + 1) + "]: " + state[i + 1]);
					multipleChoiceQuestionModel3.addAnswer(
							Integer.toString((j + state[i + 1] + key[(i + 1)
									% key.length]) & 0xff), 1,
							"That's correct!");
					multipleChoiceQuestionModel3.addAnswer(
							Integer.toString((j + state[i + 1]
									+ key[(i + 2) % key.length] + 317) & 0xff),
							0, "That's not correct!");
					multipleChoiceQuestionModel3.addAnswer(
							Integer.toString((j + state[i + 1]
									+ key[i % key.length] + 7) & 0xff), 0,
							"That's not correct!");
					lang.addMCQuestion(multipleChoiceQuestionModel3);
				}
			}
			lang.nextStep();
		}

		// Key-scheduling algorithm (KSA): Algorithm Animation
		arraySiStateText.hide();
		arraySjStateText.hide();
		iValueText.hide();
		jValueText.hide();
		siToArrayPolyline0.hide();
		siToArrayPolyline1.hide();
		siToArrayPolyline2.hide();
		sjToArrayPolyline0.hide();
		sjToArrayPolyline1.hide();
		sjToArrayPolyline2.hide();
		siValueText.hide();
		sjValueText.hide();
		markSiRect.hide();
		markSjRect.hide();
		subheader.hide();

		secondaryCode.highlight(0);
		secondaryCode.highlight(1);
		secondaryCode.highlight(2);

		subheader = lang.newText(new Offset(0, 27, headerRect, "C"),
				"Animation - KSA (Displaying every 15th step)", "subheader",
				null, subheaderProperties);

		for (int i = 10; i < 256; i++) {
			/*
			 * Key-scheduling algorithm (KSA): Algorithm - Processing Calculate
			 * j
			 */

			// & 0xff == %256
			old_j = j;
			j = (j + state[i] + key[i % key.length]) & 0xff;

			if (i % 15 == 0) {
				v.declare("String", "iKSA", Integer.toString(i),
						animal.variables.Variable
								.getRoleString(VariableRoles.WALKER));
				v.declare("String", "jKSA", Integer.toString(j),
						animal.variables.Variable
								.getRoleString(VariableRoles.WALKER));

				iValueText = lang.newText(new Offset(35, 0, iIdentifierText,
						"N"), Integer.toString(i), "iValueText", new MsTiming(
						(2 * (i - 15)) * 30), defaultTextProperties);
				jValueText = lang.newText(new Offset(35, 2, jIdentifierText,
						"N"), Integer.toString(j), "jValueText", new MsTiming(
						(2 * (i - 15)) * 30), defaultTextProperties);
				arraySiStateText = lang.newText(new Offset(0, 10,
						arraySiStateRect, "S"), "S[" + i + "]",
						"arraySiStateText", new MsTiming((2 * (i - 15)) * 30),
						defaultTextProperties);
				arraySjStateText = lang.newText(new Offset(0, 10,
						arraySjStateRect, "S"), "S[" + j + "]",
						"arraySjStateText", new MsTiming((2 * (i - 15)) * 30),
						defaultTextProperties);
				siValueText = lang.newText(new Offset(5, -8, arraySiStateRect,
						"C"), "0x" + String.format("%02X ", state[i]),
						"siValueText", new MsTiming((2 * (i - 15)) * 30),
						defaultTextProperties);
				sjValueText = lang.newText(new Offset(5, -8, arraySjStateRect,
						"C"), "0x" + String.format("%02X ", state[j]),
						"sjValueText", new MsTiming((2 * (i - 15)) * 30),
						defaultTextProperties);

				if (i < 255) {
					iValueText.hide(new MsTiming(900 + (2 * (i - 15)) * 30));
					jValueText.hide(new MsTiming(900 + (2 * (i - 15)) * 30));
					arraySiStateText.hide(new MsTiming(
							900 + (2 * (i - 15)) * 30));
					arraySjStateText.hide(new MsTiming(
							900 + (2 * (i - 15)) * 30));
					siValueText.hide(new MsTiming(900 + (2 * (i - 15)) * 30));
					sjValueText.hide(new MsTiming(900 + (2 * (i - 15)) * 30));
				}

				// polylines from array to boxes
				arrayToSiPolyline0 = lang.newPolyline(new Node[] {
						new Offset(0 + (i * 2), 1, "arrayStateRect", "SW"),
						new Offset(-1, -3, "arraySiStateRect", "N") },
						"arrayToSiAnim0", new MsTiming((2 * (i - 15)) * 30),
						polylineSiWithoutArrowProperties);
				arrayToSiPolyline1 = lang.newPolyline(new Node[] {
						new Offset(1 + (i * 2), 1, "arrayStateRect", "SW"),
						new Offset(0, -3, "arraySiStateRect", "N") },
						"arrayToSiAnim1", new MsTiming((2 * (i - 15)) * 30),
						polylineSiProperties);
				arrayToSiPolyline2 = lang.newPolyline(new Node[] {
						new Offset(2 + (i * 2), 1, "arrayStateRect", "SW"),
						new Offset(1, -3, "arraySiStateRect", "N") },
						"arrayToSiAnim2", new MsTiming((2 * (i - 15)) * 30),
						polylineSiWithoutArrowProperties);
				arrayToSjPolyline0 = lang.newPolyline(new Node[] {
						new Offset(0 + (j * 2), 1, "arrayStateRect", "SW"),
						new Offset(-1, -3, "arraySjStateRect", "N") },
						"arrayToSjAnim0", new MsTiming((2 * (i - 15)) * 30),
						polylineSjWithoutArrowProperties);
				arrayToSjPolyline1 = lang.newPolyline(new Node[] {
						new Offset(1 + (j * 2), 1, "arrayStateRect", "SW"),
						new Offset(0, -3, "arraySjStateRect", "N") },
						"arrayToSjAnim1", new MsTiming((2 * (i - 15)) * 30),
						polylineSjProperties);
				arrayToSjPolyline2 = lang.newPolyline(new Node[] {
						new Offset(2 + (j * 2), 1, "arrayStateRect", "SW"),
						new Offset(1, -3, "arraySjStateRect", "N") },
						"arrayToSjAnim2", new MsTiming((2 * (i - 15)) * 30),
						polylineSjWithoutArrowProperties);

				/*
				 * marks position in array
				 */
				markSiRect = lang.newRect(new Offset((i * 2), 1,
						"arrayStateRect", "NW"), new Offset(2 + (i * 2), 49,
						"arrayStateRect", "NW"), "markSiAnim", new MsTiming(
						(2 * (i - 15)) * 30), rectSiProperties);
				markSjRect = lang.newRect(new Offset((j * 2), 1,
						"arrayStateRect", "NW"), new Offset(2 + (j * 2), 49,
						"arrayStateRect", "NW"), "markSjAnim", new MsTiming(
						(2 * (i - 15)) * 30), rectSjProperties);

				arrayToSiPolyline0
						.hide(new MsTiming(450 + (2 * (i - 15)) * 30));
				arrayToSiPolyline1
						.hide(new MsTiming(450 + (2 * (i - 15)) * 30));
				arrayToSiPolyline2
						.hide(new MsTiming(450 + (2 * (i - 15)) * 30));
				arrayToSjPolyline0
						.hide(new MsTiming(450 + (2 * (i - 15)) * 30));
				arrayToSjPolyline1
						.hide(new MsTiming(450 + (2 * (i - 15)) * 30));
				arrayToSjPolyline2
						.hide(new MsTiming(450 + (2 * (i - 15)) * 30));
				markSiRect.hide(new MsTiming(450 + (2 * (i - 15)) * 30));
				markSjRect.hide(new MsTiming(450 + (2 * (i - 15)) * 30));
			}

			/*
			 * Key-scheduling algorithm (KSA): Algorithm - Processing Swap S[i]
			 * and S[j]
			 */

			byte temp = state[i];
			state[i] = state[j];
			state[j] = temp;

			if (i % 15 == 0) {
				// polylines from boxes to array
				siToArrayPolyline0 = lang.newPolyline(new Node[] {
						new Offset(-1, -1, "arraySiStateRect", "N"),
						new Offset(0 + (j * 2), 0, "arrayStateRect", "SW") },
						"siToArrayAnim0",
						new MsTiming((2 * (i - 15) + 15) * 30),
						polylineSiWithoutArrowProperties);
				siToArrayPolyline1 = lang.newPolyline(new Node[] {
						new Offset(0, -1, "arraySiStateRect", "N"),
						new Offset(1 + (j * 2), 0, "arrayStateRect", "SW") },
						"siToArrayAnim1",
						new MsTiming((2 * (i - 15) + 15) * 30),
						polylineSiProperties);
				siToArrayPolyline2 = lang.newPolyline(new Node[] {
						new Offset(1, -1, "arraySiStateRect", "N"),
						new Offset(2 + (j * 2), 0, "arrayStateRect", "SW") },
						"siToArrayAnim2",
						new MsTiming((2 * (i - 15) + 15) * 30),
						polylineSiWithoutArrowProperties);
				sjToArrayPolyline0 = lang.newPolyline(new Node[] {
						new Offset(-1, -1, "arraySjStateRect", "N"),
						new Offset(0 + (i * 2), 0, "arrayStateRect", "SW") },
						"sjToArrayAnim0",
						new MsTiming((2 * (i - 15) + 15) * 30),
						polylineSjWithoutArrowProperties);
				sjToArrayPolyline1 = lang.newPolyline(new Node[] {
						new Offset(0, -1, "arraySjStateRect", "N"),
						new Offset(1 + (i * 2), 0, "arrayStateRect", "SW") },
						"sjToArrayAnim1",
						new MsTiming((2 * (i - 15) + 15) * 30),
						polylineSjProperties);
				sjToArrayPolyline2 = lang.newPolyline(new Node[] {
						new Offset(1, -1, "arraySjStateRect", "N"),
						new Offset(2 + (i * 2), 0, "arrayStateRect", "SW") },
						"sjToArrayAnim2",
						new MsTiming((2 * (i - 15) + 15) * 30),
						polylineSjWithoutArrowProperties);

				/*
				 * marks position in array, polyline instead of rectangle to be
				 * more flexible
				 */
				markSiRect = lang.newRect(new Offset((i * 2), 1,
						"arrayStateRect", "NW"), new Offset(2 + (i * 2), 49,
						"arrayStateRect", "NW"), "markSiAnim2", new MsTiming(
						(2 * (i - 15) + 15) * 30), rectSjProperties);
				markSjRect = lang.newRect(new Offset((j * 2), 1,
						"arrayStateRect", "NW"), new Offset(2 + (j * 2), 49,
						"arrayStateRect", "NW"), "markSjAnim2", new MsTiming(
						(2 * (i - 15) + 15) * 30), rectSiProperties);

				if (i < 255) {
					siToArrayPolyline0.hide(new MsTiming(
							450 + (2 * (i - 15) + 15) * 30));
					siToArrayPolyline1.hide(new MsTiming(
							450 + (2 * (i - 15) + 15) * 30));
					siToArrayPolyline2.hide(new MsTiming(
							450 + (2 * (i - 15) + 15) * 30));
					sjToArrayPolyline0.hide(new MsTiming(
							450 + (2 * (i - 15) + 15) * 30));
					sjToArrayPolyline1.hide(new MsTiming(
							450 + (2 * (i - 15) + 15) * 30));
					sjToArrayPolyline2.hide(new MsTiming(
							450 + (2 * (i - 15) + 15) * 30));
					markSiRect
							.hide(new MsTiming(450 + (2 * (i - 15) + 15) * 30));
					markSjRect
							.hide(new MsTiming(450 + (2 * (i - 15) + 15) * 30));
				}
			}
		}

		lang.nextStep();

		// Introduction #3: Pseudo-random generation algorithm (PRGA)
		code.hide();
		codeRect.hide();
		secondaryCode.hide();
		codeText.hide();
		subheader.hide();
		arrayStateRect.hide();
		arrayStateText.hide();
		arraySiStateRect.hide();
		arraySjStateRect.hide();
		iIdentifierText.hide();
		jIdentifierText.hide();
		parameters0.hide();
		parameters1.hide();
		parameters2.hide();
		parameters3.hide();
		parameters4.hide();
		parameters5.hide();
		parameters6.hide();
		parameters7.hide();

		iValueText.hide();
		jValueText.hide();
		arraySiStateText.hide();
		arraySjStateText.hide();
		siValueText.hide();
		sjValueText.hide();
		siToArrayPolyline0.hide();
		siToArrayPolyline1.hide();
		siToArrayPolyline2.hide();
		sjToArrayPolyline0.hide();
		sjToArrayPolyline1.hide();
		sjToArrayPolyline2.hide();
		markSiRect.hide();
		markSjRect.hide();

		subheader = lang.newText(new Offset(0, 27, headerRect, "C"),
				"Pseudo-random generation algorithm", "subheader", null,
				subheaderProperties);

		code = lang.newSourceCode(new Offset(-130, 47, headerRect, "S"),
				"code", null, sourceCodePrimaryHighlightProperties);
		code.addCodeLine("i := 0", null, 0, null);
		code.addCodeLine("j := 0", null, 0, null);
		code.addCodeLine("while GeneratingOutput:", null, 0, null);
		code.addCodeLine("i := (i + 1) mod 256", null, 2, null);
		code.addCodeLine("j := (j + S[i]) mod 256", null, 2, null);
		code.addCodeLine("swap values of S[i] and S[j]", null, 2, null);
		code.addCodeLine("K := S[(S[i] + S[j]) mod 256]", null, 2, null);
		code.addCodeLine("P := next plaintext byte", null, 2, null);
		code.addCodeLine("output (K XOR P)", null, 2, null);
		code.addCodeLine("endwhile", null, 0, null);

		codeRect = lang.newRect(new Offset(-4, -2, code, "NW"), new Offset(4,
				2, code, "SE"), "codeRect", null, codeRectProperties);

		codeText = lang.newSourceCode(new Offset(-345, 240, headerRect, "S"),
				"codeText", null, sourceCodeTextProperties);
		codeText.addCodeLine(
				"At the end of KSA the initial permutation of the array S is finished and the second part",
				null, 0, null);
		codeText.addCodeLine(
				"of RC4 begins, the pseudo-random generation algorithm (PRGA). Here the actual encryption",
				null, 0, null);
		codeText.addCodeLine("takes place.", null, 0, null);
		codeText.addCodeLine("", null, 0, null);

		lang.nextStep("Introduction - Pseudo-random generation algorithm (PRGA)");

		code.highlight(0);
		code.highlight(1);
		codeText.addCodeLine(
				"Initialise i and j to 0. Both are used to determine the two elements of S that get used in",
				null, 0, null);
		codeText.addCodeLine("encrypting the next plaintext character.", null,
				0, null);
		codeText.addCodeLine("", null, 0, null);
		codeText.highlight(4);
		codeText.highlight(5);

		lang.nextStep();

		code.unhighlight(0);
		code.unhighlight(1);
		code.highlight(2);
		code.highlight(9);
		codeText.addCodeLine(
				"For as many iterations as are needed, the PRGA modifies the state and outputs a byte",
				null, 0, null);
		codeText.addCodeLine(
				"of the keystream. Each iteration one character of the plaintext gets encrypted.",
				null, 0, null);
		codeText.addCodeLine("", null, 0, null);
		codeText.unhighlight(4);
		codeText.unhighlight(5);
		codeText.highlight(7);
		codeText.highlight(8);

		lang.nextStep();

		code.highlight(3);
		code.highlight(4);
		code.highlight(5);
		codeText.addCodeLine(
				"Increment i and look up S[i]. Add that to j and swap the values of S[i] and S[j].",
				null, 0, null);
		codeText.addCodeLine("", null, 0, null);
		codeText.unhighlight(7);
		codeText.unhighlight(8);
		codeText.highlight(10);

		lang.nextStep();

		code.unhighlight(3);
		code.unhighlight(4);
		code.unhighlight(5);
		code.highlight(6);
		code.highlight(7);
		code.highlight(8);
		codeText.addCodeLine(
				"The sum of S[i] and S[j] (modulo 256) is used as an index to fetch a third element of S,",
				null, 0, null);
		codeText.addCodeLine(
				"called K, which is XORed with the next byte of unencrypted plaintext, called P,",
				null, 0, null);
		codeText.addCodeLine("to produce the next byte of ciphertext.", null,
				0, null);
		codeText.addCodeLine("", null, 0, null);
		codeText.addCodeLine(
				"Decryption works analogous due to the symmetric nature of XOR.",
				null, 0, null);
		codeText.unhighlight(10);
		codeText.highlight(12);
		codeText.highlight(13);
		codeText.highlight(14);
		codeText.highlight(16);

		lang.nextStep();

		// Pseudo-random generation algorithm (PRGA): Setup
		code.hide();
		codeRect.hide();
		codeText.hide();
		subheader.hide();

		parameters0.show();
		parameters1.show();
		parameters2.show();
		parameters3.show();
		parameters4.show();
		parameters5.show();
		parameters6.show();
		parameters7.show();
		arrayStateRect.show();
		arrayStateText.show();
		iIdentifierText.show();
		jIdentifierText.show();
		iValueText = lang.newText(new Offset(35, 1, iIdentifierText, "N"), "0",
				"iValueText", null, defaultTextProperties);
		jValueText = lang.newText(new Offset(35, 0, jIdentifierText, "N"), "0",
				"jValueText", null, defaultTextProperties);

		subheader = lang.newText(new Offset(0, 27, headerRect, "C"),
				"Animation - PRGA", "subheader", null, subheaderProperties);

		code = lang.newSourceCode(new Offset(-95, 240, arrayStateRect, "SW"),
				"code", null, sourceCodePrimaryHighlightProperties);
		code.addCodeLine("Pseudo-random generation algorithm (PRGA)", null, 0,
				null);
		code.addCodeLine("i := 0", null, 0, null);
		code.addCodeLine("j := 0", null, 0, null);
		code.addCodeLine("while GeneratingOutput:", null, 0, null);
		code.addCodeLine("i := (i + 1) mod 256", null, 2, null);
		code.addCodeLine("j := (j + S[i]) mod 256", null, 2, null);
		code.addCodeLine("swap values of S[i] and S[j]", null, 2, null);
		code.addCodeLine("K := S[(S[i] + S[j]) mod 256]", null, 2, null);
		code.addCodeLine("P := next plaintext byte", null, 2, null);
		code.addCodeLine("output (K XOR P)", null, 2, null);
		code.addCodeLine("endwhile", null, 0, null);

		codeRect = lang.newRect(new Offset(-4, -2, code, "NW"), new Offset(4,
				2, code, "SE"), "codeRect", null, codeRectProperties);

		code.highlight(1);
		code.highlight(2);

		int i_prga = 0;
		int j_prga = 0;
		int old_j_prga = 0;

		lang.nextStep("Animation - PRGA Init");

		// Pseudo-random generation algorithm (PRGA): Algorithm - Processing

		code.hide();

		code = lang.newSourceCode(new Offset(-95, 240, arrayStateRect, "SW"),
				"code", null, sourceCodePrimaryHighlightProperties);
		code.addCodeLine("Pseudo-random generation algorithm (PRGA)", null, 0,
				null);
		code.addCodeLine("i := 0", null, 0, null);
		code.addCodeLine("j := 0", null, 0, null);
		code.addCodeLine("while GeneratingOutput:", null, 0, null);
		code.addCodeLine("", null, 0, null);
		code.addCodeLine("", null, 0, null);
		code.addCodeLine("", null, 0, null);
		code.addCodeLine("", null, 0, null);
		code.addCodeLine("", null, 0, null);
		code.addCodeLine("", null, 0, null);
		code.addCodeLine("endwhile", null, 0, null);
		code.highlight(3);
		code.highlight(10);

		secondaryCode = lang.newSourceCode(new Offset(-95, 308, arrayStateRect,
				"SW"), "secondaryCode", null,
				sourceCodeSecondaryHighlightProperties);
		secondaryCode.addCodeLine("i := (i + 1) mod 256", null, 2, null);
		secondaryCode.addCodeLine("j := (j + S[i]) mod 256", null, 2, null);
		secondaryCode
				.addCodeLine("swap values of S[i] and S[j]", null, 2, null);
		secondaryCode.addCodeLine("K := S[(S[i] + S[j]) mod 256]", null, 2,
				null);
		secondaryCode.addCodeLine("P := next plaintext byte", null, 2, null);
		secondaryCode.addCodeLine("output (K XOR P)", null, 2, null);

		arraySiStateRect = lang.newRect(new Offset(-100, 110, "arrayStateRect",
				"C"), new Offset(-30, 140, "arrayStateRect", "C"),
				"arraySiStateRect2", null, stateRectSiProperties);
		arraySjStateRect = lang.newRect(new Offset(30, 110, "arrayStateRect",
				"C"), new Offset(100, 140, "arrayStateRect", "C"),
				"arraySjStateRect2", null, stateRectSjProperties);
		arraySiStateText = lang.newText(
				new Offset(0, 10, arraySiStateRect, "S"), "S[i]",
				"arraySiStateText", null, defaultTextProperties);
		arraySjStateText = lang.newText(
				new Offset(0, 10, arraySjStateRect, "S"), "S[j]",
				"arraySjStateText", null, defaultTextProperties);

		lang.nextStep("Animation - PRGA Processing");

		// Pseudo-random generation algorithm (PRGA): Algorithm - Processing
		Text iCalculationText0 = null;
		Text sijCalculationText0 = null;
		Text sijCalculationText1 = null;
		Text kCalculationText0 = null;
		Text kxorpCalculationText0 = null;
		// Rectangle around Plus Symbol
		Rect ijState = lang.newRect(new Offset(15, -72, "arraySiStateRect2",
				"E"), new Offset(-15, -42, "arraySjStateRect2", "W"),
				"ijStateRect", null, plusRectProperties);
		// Circle around XOR Symbol
		Circle kxorpState = lang.newCircle(new Offset(30, 0,
				"arraySiStateRect2", "E"), 15, "kxorpStateCircle", null,
				headerCircleProperties);

		PolylineProperties p = new PolylineProperties();
		// Plus Symbol Lines
		Polyline plusPolyline0 = lang.newPolyline(new Node[] {
				new Offset(15, -57, "arraySiStateRect2", "E"),
				new Offset(-15, -57, "arraySjStateRect2", "W") }, "plusPL0",
				null, p);
		Polyline plusPolyline1 = lang.newPolyline(new Node[] {
				new Offset(30, -72, "arraySiStateRect2", "E"),
				new Offset(-30, -42, "arraySjStateRect2", "W") }, "plusPL1",
				null, p);
		// XOR Symbol Lines
		Polyline xorPolyline0 = lang.newPolyline(new Node[] {
				new Offset(15, 0, "arraySiStateRect2", "E"),
				new Offset(-15, 0, "arraySjStateRect2", "W") }, "xorPL0", null,
				p);
		Polyline xorPolyline1 = lang.newPolyline(new Node[] {
				new Offset(30, -15, "arraySiStateRect2", "E"),
				new Offset(-30, +15, "arraySjStateRect2", "W") }, "xorPL1",
				null, p);

		RectProperties rectSijProperties = new RectProperties();
		rectSijProperties.set("color", arrayMarkerColor);
		rectSijProperties.set("fillColor", arrayMarkerColor);
		rectSijProperties.set("filled", true);

		PolylineProperties polylineSijProperties = new PolylineProperties();
		polylineSijProperties.set("color", arrayMarkerColor);
		polylineSijProperties.set("fwArrow", true);
		PolylineProperties polylineSijWithoutArrowProperties = new PolylineProperties();
		polylineSijWithoutArrowProperties.set("color", arrayMarkerColor);
		polylineSijWithoutArrowProperties.set("fwArrow", false);

		RectProperties kRectProperties = new RectProperties();
		kRectProperties.set("depth", 3);
		kRectProperties.set("filled", true);
		kRectProperties.set("fillColor", kColor);
		RectProperties pRectProperties = new RectProperties();
		pRectProperties.set("depth", 3);
		pRectProperties.set("filled", true);
		pRectProperties.set("fillColor", pColor);

		Polyline ijToArrayPolyline0 = null;
		Polyline ijToArrayPolyline1 = null;
		Polyline ijToArrayPolyline2 = null;

		Polyline arrayToKPolyline0 = null;
		Polyline arrayToKPolyline1 = null;
		Polyline arrayToKPolyline2 = null;

		Rect kRect = null;
		Rect pRect = null;
		Text kDescriptionText = null;
		Text pDescriptionText = null;
		Text kValueText = null;
		Text pValueText = null;
		Text inputValueText = null;
		Text outputValueText = null;

		PolylineProperties polylinePProperties = new PolylineProperties();
		polylinePProperties.set("color", pColor);
		polylinePProperties.set("fwArrow", true);
		PolylineProperties polylineKProperties = new PolylineProperties();
		polylineKProperties.set("color", kColor);
		polylineKProperties.set("fwArrow", true);
		PolylineProperties polylinePWithoutArrowProperties = new PolylineProperties();
		polylinePWithoutArrowProperties.set("color", pColor);
		polylinePWithoutArrowProperties.set("fwArrow", false);
		PolylineProperties polylineKWithoutArrowProperties = new PolylineProperties();
		polylineKWithoutArrowProperties.set("color", kColor);
		polylineKWithoutArrowProperties.set("fwArrow", false);

		for (int x = 0; x < input.length; x++) {
			/*
			 * Pseudo-random generation algorithm (PRGA): - Processing Calculate
			 * i & j
			 */
			iValueText.hide();
			jValueText.hide();
			arraySiStateText.hide();
			arraySjStateText.hide();
			siValueText.hide();
			sjValueText.hide();
			ijState.hide();
			kxorpState.hide();
			plusPolyline0.hide();
			plusPolyline1.hide();
			xorPolyline0.hide();
			xorPolyline1.hide();
			siToArrayPolyline0.hide();
			siToArrayPolyline1.hide();
			siToArrayPolyline2.hide();
			sjToArrayPolyline0.hide();
			sjToArrayPolyline1.hide();
			sjToArrayPolyline2.hide();
			markSiRect.hide();
			if (x > 0) {
				kCalculationText0.hide();
				kxorpCalculationText0.hide();
				kRect.hide();
				pRect.hide();
				kDescriptionText.hide();
				pDescriptionText.hide();
				kValueText.hide();
				pValueText.hide();
				outputValueText.hide();
				arraySiStateRect.show();
				arraySjStateRect.show();
				arrayToKPolyline0.hide();
				arrayToKPolyline1.hide();
				arrayToKPolyline2.hide();
				inputValueText.hide();
			}

			secondaryCode.highlight(0);
			secondaryCode.highlight(1);
			secondaryCode.unhighlight(3);
			secondaryCode.unhighlight(4);
			secondaryCode.unhighlight(5);

			i_prga = (i_prga + 1) & 0xff;
			old_j_prga = j_prga;
			j_prga = (state[i_prga] + j_prga) & 0xff;

			v.declare("String", "iPRGA", Integer.toString(i_prga),
					animal.variables.Variable
							.getRoleString(VariableRoles.MOST_RECENT_HOLDER));
			v.declare("String", "jPRGA", Integer.toString(j_prga),
					animal.variables.Variable
							.getRoleString(VariableRoles.MOST_RECENT_HOLDER));

			iValueText = lang.newText(new Offset(35, 2, iIdentifierText, "N"),
					Integer.toString(i_prga), "iValueText", null,
					defaultTextProperties);
			jValueText = lang.newText(new Offset(35, 0, jIdentifierText, "N"),
					Integer.toString(j_prga), "jValueText", null,
					defaultTextProperties);
			arraySiStateText = lang.newText(new Offset(0, 10, arraySiStateRect,
					"S"), "S[" + i_prga + "]", "arraySiStateText", null,
					defaultTextProperties);
			arraySjStateText = lang.newText(new Offset(0, 10, arraySjStateRect,
					"S"), "S[" + j_prga + "]", "arraySjStateText", null,
					defaultTextProperties);
			siValueText = lang.newText(
					new Offset(5, -8, arraySiStateRect, "C"),
					"0x" + String.format("%02X ", state[i_prga]),
					"siValueText", null, defaultTextProperties);
			sjValueText = lang.newText(
					new Offset(5, -8, arraySjStateRect, "C"),
					"0x" + String.format("%02X ", state[j_prga]),
					"sjValueText", null, defaultTextProperties);

			iCalculationText0 = lang.newText(new Offset(-270, 180,
					arrayStateRect, "S"), "i := (i + 1) % 256 " + ":= ("
					+ (i_prga - 1) + " + 1) % 256 := " + i_prga,
					"iCalculationText0", null, parameterText13Properties);
			jCalculationText0 = lang.newText(new Offset(0, 9,
					iCalculationText0, "SW"),
					"j := (j + S[i]) % 256 := (" + (old_j_prga) + " + 0x"
							+ String.format("%02x", state[i_prga])
							+ ") % 256 := (" + (old_j_prga) + " + "
							+ state[i_prga] + ") % 256 := " + j_prga,
					"jCalculationText00", null, parameterText13Properties);

			// polylines from array to boxes
			arrayToSiPolyline0 = lang.newPolyline(new Node[] {
					new Offset(0 + (i_prga * 2), 1, "arrayStateRect", "SW"),
					new Offset(-1, -3, "arraySiStateRect2", "N") },
					"arrayToSi0", null, polylineSiWithoutArrowProperties);
			arrayToSiPolyline1 = lang.newPolyline(new Node[] {
					new Offset(1 + (i_prga * 2), 1, "arrayStateRect", "SW"),
					new Offset(0, -3, "arraySiStateRect2", "N") },
					"arrayToSi1", null, polylineSiProperties);
			arrayToSiPolyline2 = lang.newPolyline(new Node[] {
					new Offset(2 + (i_prga * 2), 1, "arrayStateRect", "SW"),
					new Offset(1, -3, "arraySiStateRect2", "N") },
					"arrayToSi2", null, polylineSiWithoutArrowProperties);
			arrayToSjPolyline0 = lang.newPolyline(new Node[] {
					new Offset(0 + (j_prga * 2), 1, "arrayStateRect", "SW"),
					new Offset(-1, -3, "arraySjStateRect2", "N") },
					"arrayToSj0", null, polylineSjWithoutArrowProperties);
			arrayToSjPolyline1 = lang.newPolyline(new Node[] {
					new Offset(1 + (j_prga * 2), 1, "arrayStateRect", "SW"),
					new Offset(0, -3, "arraySjStateRect2", "N") },
					"arrayToSj1", null, polylineSjProperties);
			arrayToSjPolyline2 = lang.newPolyline(new Node[] {
					new Offset(2 + (j_prga * 2), 1, "arrayStateRect", "SW"),
					new Offset(1, -3, "arraySjStateRect2", "N") },
					"arrayToSj2", null, polylineSjWithoutArrowProperties);

			/*
			 * marks position in array
			 */
			markSiRect = lang.newRect(new Offset((i_prga * 2), 1,
					"arrayStateRect", "NW"), new Offset(2 + (i_prga * 2), 49,
					"arrayStateRect", "NW"), "markSi", null, rectSiProperties);
			markSjRect = lang.newRect(new Offset((j_prga * 2), 1,
					"arrayStateRect", "NW"), new Offset(2 + (j_prga * 2), 49,
					"arrayStateRect", "NW"), "markSj", null, rectSjProperties);

			if (withQuiz) {
				if (x == 1) {
					MultipleChoiceQuestionModel multipleChoiceQuestionModel4 = new MultipleChoiceQuestionModel(
							"PRGA Swapping: General Understanding");
					multipleChoiceQuestionModel4
							.setPrompt("How often is an arbitrary element of S[] swapped with another element given an infinite plaintext?");
					multipleChoiceQuestionModel4.addAnswer(
							"There's no guarantee, it might never happen.", 0,
							"That's not correct. What's the role of i?");
					multipleChoiceQuestionModel4.addAnswer(
							"At least once every 256 iterations.", 1,
							"That's correct!");
					lang.addMCQuestion(multipleChoiceQuestionModel4);
				}
			}

			lang.nextStep();

			/*
			 * Pseudo-random generation algorithm (PRGA): - Processing Swap S[i]
			 * & S[j]
			 */
			iCalculationText0.hide();
			jCalculationText0.hide();
			arrayToSiPolyline0.hide();
			arrayToSjPolyline0.hide();
			arrayToSiPolyline1.hide();
			arrayToSjPolyline1.hide();
			arrayToSiPolyline2.hide();
			arrayToSjPolyline2.hide();
			markSiRect.hide();
			markSjRect.hide();

			secondaryCode.highlight(2);
			secondaryCode.unhighlight(0);
			secondaryCode.unhighlight(1);

			byte temp = state[i_prga];
			state[i_prga] = state[j_prga];
			state[j_prga] = temp;

			// polylines from boxes to array
			siToArrayPolyline0 = lang.newPolyline(new Node[] {
					new Offset(-1, -1, "arraySiStateRect2", "N"),
					new Offset(0 + (j_prga * 2), 0, "arrayStateRect", "SW") },
					"siToArray0", null, polylineSiWithoutArrowProperties);
			siToArrayPolyline1 = lang.newPolyline(new Node[] {
					new Offset(0, -1, "arraySiStateRect2", "N"),
					new Offset(1 + (j_prga * 2), 0, "arrayStateRect", "SW") },
					"siToArray1", null, polylineSiProperties);
			siToArrayPolyline2 = lang.newPolyline(new Node[] {
					new Offset(1, -1, "arraySiStateRect2", "N"),
					new Offset(2 + (j_prga * 2), 0, "arrayStateRect", "SW") },
					"siToArray2", null, polylineSiWithoutArrowProperties);
			sjToArrayPolyline0 = lang.newPolyline(new Node[] {
					new Offset(-1, -1, "arraySjStateRect2", "N"),
					new Offset(0 + (i_prga * 2), 0, "arrayStateRect", "SW") },
					"sjToArray0", null, polylineSjWithoutArrowProperties);
			sjToArrayPolyline1 = lang.newPolyline(new Node[] {
					new Offset(0, -1, "arraySjStateRect2", "N"),
					new Offset(1 + (i_prga * 2), 0, "arrayStateRect", "SW") },
					"sjToArray1", null, polylineSjProperties);
			sjToArrayPolyline2 = lang.newPolyline(new Node[] {
					new Offset(1, -1, "arraySjStateRect2", "N"),
					new Offset(2 + (i_prga * 2), 0, "arrayStateRect", "SW") },
					"sjToArray2", null, polylineSjWithoutArrowProperties);

			/*
			 * marks position in array
			 */
			markSiRect = lang.newRect(new Offset((i_prga * 2), 1,
					"arrayStateRect", "NW"), new Offset(2 + (i_prga * 2), 49,
					"arrayStateRect", "NW"), "markSi2", null, rectSjProperties);
			markSjRect = lang.newRect(new Offset((j_prga * 2), 1,
					"arrayStateRect", "NW"), new Offset(2 + (j_prga * 2), 49,
					"arrayStateRect", "NW"), "markSj2", null, rectSiProperties);

			lang.nextStep();

			/*
			 * Pseudo-random generation algorithm (PRGA): - Processing Calculate
			 * output
			 */
			siToArrayPolyline0.hide();
			siToArrayPolyline1.hide();
			siToArrayPolyline2.hide();
			sjToArrayPolyline0.hide();
			sjToArrayPolyline1.hide();
			sjToArrayPolyline2.hide();
			markSiRect.hide();
			markSjRect.hide();
			ijState.show();
			plusPolyline0.show();
			plusPolyline1.show();

			secondaryCode.highlight(3);
			secondaryCode.highlight(4);
			secondaryCode.highlight(5);
			secondaryCode.unhighlight(2);

			int sij = (state[i_prga] + state[j_prga]) & 0xff;

			// polylines from boxes to [+] box
			siToArrayPolyline0 = lang.newPolyline(new Node[] {
					new Offset(-1, -1, "arraySiStateRect2", "N"),
					new OffsetFromLastPosition(0, -42),
					new Offset(-1, -1, "ijStateRect", "W") }, "siToArray0-0",
					null, polylineSiWithoutArrowProperties);
			siToArrayPolyline1 = lang.newPolyline(new Node[] {
					new Offset(0, -1, "arraySiStateRect2", "N"),
					new OffsetFromLastPosition(0, -41),
					new Offset(-1, 0, "ijStateRect", "W") }, "siToArray1-1",
					null, polylineSiProperties);
			siToArrayPolyline2 = lang.newPolyline(new Node[] {
					new Offset(1, -1, "arraySiStateRect2", "N"),
					new OffsetFromLastPosition(0, -40),
					new Offset(-1, 1, "ijStateRect", "W") }, "siToArray2-2",
					null, polylineSiWithoutArrowProperties);
			sjToArrayPolyline0 = lang.newPolyline(new Node[] {
					new Offset(-1, -1, "arraySjStateRect2", "N"),
					new OffsetFromLastPosition(0, -40),
					new Offset(1, 1, "ijStateRect", "E") }, "sjToArray0-0",
					null, polylineSjWithoutArrowProperties);
			sjToArrayPolyline1 = lang.newPolyline(new Node[] {
					new Offset(0, -1, "arraySjStateRect2", "N"),
					new OffsetFromLastPosition(0, -41),
					new Offset(1, 0, "ijStateRect", "E") }, "sjToArray1-1",
					null, polylineSjProperties);
			sjToArrayPolyline2 = lang.newPolyline(new Node[] {
					new Offset(1, -1, "arraySjStateRect2", "N"),
					new OffsetFromLastPosition(0, -42),
					new Offset(1, -1, "ijStateRect", "E") }, "sjToArray2-2",
					null, polylineSjWithoutArrowProperties);

			/*
			 * marks S[S[i] + S[j]] position in array
			 */
			markSiRect = lang.newRect(new Offset((sij * 2), 1,
					"arrayStateRect", "NW"), new Offset(2 + (sij * 2), 49,
					"arrayStateRect", "NW"), "markSi", null, rectSijProperties);

			/*
			 * Polyline from ij Rect to State
			 */
			ijToArrayPolyline0 = lang.newPolyline(new Node[] {
					new Offset(-1, -1, "ijStateRect", "N"),
					new Offset(sij * 2, 51, "arrayStateRect", "NW") },
					"ijToArrayPolyline0", null,
					polylineSijWithoutArrowProperties);
			ijToArrayPolyline1 = lang.newPolyline(new Node[] {
					new Offset(0, -1, "ijStateRect", "N"),
					new Offset(1 + (sij * 2), 51, "arrayStateRect", "NW") },
					"ijToArrayPolyline1", null, polylineSijProperties);
			ijToArrayPolyline2 = lang.newPolyline(new Node[] {
					new Offset(1, -1, "ijStateRect", "N"),
					new Offset(2 + (sij * 2), 51, "arrayStateRect", "NW") },
					"ijToArrayPolyline2", null,
					polylineSijWithoutArrowProperties);

			sijCalculationText0 = lang.newText(
					new Offset(-260, 180, arrayStateRect, "S"),
					"(S[i] + S[j]) % 256 := (S[" + Integer.toString(i_prga)
							+ "] + [" + Integer.toString(j_prga)
							+ "]) % 256 := (0x"
							+ String.format("%02x", state[i_prga]) + " + 0x"
							+ String.format("%02x", state[j_prga]) + ") % 256",
					"ijCalculationText0", null, parameterText13Properties);
			sijCalculationText1 = lang.newText(new Offset(0, 9,
					sijCalculationText0, "SW"), "                    := ("
					+ state[i_prga] + " + " + state[j_prga] + ") % 256 := "
					+ sij, "ijCalculationText1", null, parameterText13Properties);

			if (withQuiz) {
				if (x == 2) {
					MultipleChoiceQuestionModel multipleChoiceQuestionModel5 = new MultipleChoiceQuestionModel(
							"PRGA Processing: Value of P");
					multipleChoiceQuestionModel5
							.setPrompt("What's the next plaintext value for P?");
					multipleChoiceQuestionModel5.addAnswer(
							String.valueOf(inputPlain.charAt(0)), 0,
							"That's not correct. Compare input and output.");
					multipleChoiceQuestionModel5.addAnswer(
							String.valueOf(inputPlain.charAt(1)), 0,
							"That's not correct. Compare input and output.");
					multipleChoiceQuestionModel5.addAnswer(
							String.valueOf(inputPlain.charAt(2)), 0,
							"That's correct!");
					lang.addMCQuestion(multipleChoiceQuestionModel5);
				}
				if (x == 4) {
					MultipleChoiceQuestionModel multipleChoiceQuestionModel6 = new MultipleChoiceQuestionModel(
							"PRGA Processing: Value of P Revisited");
					multipleChoiceQuestionModel6
							.setPrompt("What's the next plaintext value for P?");
					multipleChoiceQuestionModel6.addAnswer(
							String.valueOf(inputPlain.charAt(1)), 0,
							"That's not correct. Compare input and output.");
					multipleChoiceQuestionModel6.addAnswer(
							String.valueOf(inputPlain.charAt(3)), 0,
							"That's not correct. Compare input and output.");
					multipleChoiceQuestionModel6.addAnswer(
							String.valueOf(inputPlain.charAt(4)), 0,
							"That's correct!");
					lang.addMCQuestion(multipleChoiceQuestionModel6);
				}
			}

			// output = k^p
			lang.nextStep();

			parameters7.hide();
			ijState.hide();
			plusPolyline0.hide();
			plusPolyline1.hide();
			ijToArrayPolyline0.hide();
			ijToArrayPolyline1.hide();
			ijToArrayPolyline2.hide();
			sijCalculationText0.hide();
			sijCalculationText1.hide();
			siToArrayPolyline0.hide();
			siToArrayPolyline1.hide();
			siToArrayPolyline2.hide();
			sjToArrayPolyline0.hide();
			sjToArrayPolyline1.hide();
			sjToArrayPolyline2.hide();
			arraySiStateRect.hide();
			arraySiStateText.hide();
			arraySjStateRect.hide();
			arraySjStateText.hide();
			siValueText.hide();
			sjValueText.hide();

			kxorpState.show();
			xorPolyline0.show();
			xorPolyline1.show();

			output[x] = (byte) ((input[x] ^ state[(state[i_prga] + state[j_prga]) & 0xff]));

			StringBuilder outputAsHexBuilder = new StringBuilder();
			int i = 0;
			for (byte outputVal : output) {
				if (i <= x) {
					outputAsHexBuilder.append(String.format("%02x", outputVal));
					outputAsHexBuilder.append(",");
				}
				i++;
			}

			pRect = lang.newRect(
					new Offset(-10, -72, "arraySiStateRect2", "W"), new Offset(
							0, -42, "arraySiStateRect2", "E"), "pRect", null,
					pRectProperties);
			kRect = lang.newRect(new Offset(0, -72, "arraySjStateRect2", "W"),
					new Offset(0, -42, "arraySjStateRect2", "E"), "kRect",
					null, kRectProperties);
			pDescriptionText = lang.newText(new Offset(0, 10, pRect, "S"), "P",
					"pDescriptionText", null, defaultTextProperties);
			kDescriptionText = lang.newText(new Offset(0, 10, kRect, "S"), "K",
					"kDescriptionText", null, defaultTextProperties);
			pValueText = lang.newText(
					new Offset(-1, -8, pRect, "C"),
					inputPlain.charAt(x) + "->0x"
							+ String.format("%02x", input[x]), "pValueText",
					null, defaultTextProperties);
			kValueText = lang.newText(new Offset(0, -8, kRect, "C"), "0x"
					+ String.format("%02x", state[sij]), "kValueText", null,
					defaultTextProperties);
			inputValueText = lang.newText(new Offset(-220, 8, kRect, "NW"),
					inputPlain.substring(x, inputPlain.length()),
					"inputValueText", null, defaultTextProperties);
			outputValueText = lang.newText(new Offset(0, 10, kxorpState, "S"),
					"Out := 0x" + String.format("%02x", output[x]),
					"outputValueText", null, defaultTextProperties);

			/*
			 * Polyline from State to K Rect
			 */
			arrayToKPolyline0 = lang.newPolyline(new Node[] {
					new Offset(sij * 2, 51, "arrayStateRect", "NW"),
					new Offset(-1, -1, "kRect", "N") }, "arrayToKPolyline0",
					null, polylineSijWithoutArrowProperties);
			arrayToKPolyline1 = lang.newPolyline(new Node[] {
					new Offset(1 + (sij * 2), 51, "arrayStateRect", "NW"),
					new Offset(0, -1, "kRect", "N") }, "arrayToKPolyline1",
					null, polylineSijProperties);
			arrayToKPolyline2 = lang.newPolyline(new Node[] {
					new Offset(2 + (sij * 2), 51, "arrayStateRect", "NW"),
					new Offset(1, -1, "kRect", "N") }, "arrayToKPolyline2",
					null, polylineSijWithoutArrowProperties);

			//
			// polylines from boxes to (+) box
			siToArrayPolyline0 = lang.newPolyline(new Node[] {
					new Offset(1, -1, "pRect", "E"),
					new OffsetFromLastPosition(18, 0),
					new Offset(-4, -1, "kxorpStateCircle", "N") }, "pToXOR0",
					null, polylinePProperties);
			siToArrayPolyline1 = lang.newPolyline(new Node[] {
					new Offset(1, 0, "pRect", "E"),
					new OffsetFromLastPosition(17, 0),
					new Offset(-5, -1, "kxorpStateCircle", "N") }, "pToXOR1",
					null, polylinePProperties);
			siToArrayPolyline2 = lang.newPolyline(new Node[] {
					new Offset(1, 1, "pRect", "E"),
					new OffsetFromLastPosition(16, 0),
					new Offset(-6, -1, "kxorpStateCircle", "N") }, "pToXOR2",
					null, polylinePProperties);
			sjToArrayPolyline0 = lang.newPolyline(new Node[] {
					new Offset(-1, -1, "kRect", "W"),
					new OffsetFromLastPosition(-18, 0),
					new Offset(4, -1, "kxorpStateCircle", "N") }, "kToXOR0",
					null, polylineKProperties);
			sjToArrayPolyline1 = lang.newPolyline(new Node[] {
					new Offset(-1, 0, "kRect", "W"),
					new OffsetFromLastPosition(-17, 0),
					new Offset(5, -1, "kxorpStateCircle", "N") }, "kToXOR1",
					null, polylineKProperties);
			sjToArrayPolyline2 = lang.newPolyline(new Node[] {
					new Offset(-1, 1, "kRect", "W"),
					new OffsetFromLastPosition(-16, 0),
					new Offset(6, -1, "kxorpStateCircle", "N") }, "kToXOR2",
					null, polylineKProperties);

			v.declare(
					"String",
					"output",
					"\u2022 byte[]: ["
							+ outputAsHexBuilder.toString().substring(0,
									outputAsHexBuilder.length() - 1) + "]",
					animal.variables.Variable
							.getRoleString(VariableRoles.CONTAINER));

			parametersString7 = "\u2022 byte[]: ["
					+ outputAsHexBuilder.toString().substring(0,
							outputAsHexBuilder.length() - 1) + "]";
			parameters7 = lang.newText(new Offset(0, 5, "parameters6", "SW"),
					parametersString7, "parameters7", null,
					parameterText12Properties);

			kCalculationText0 = lang.newText(new Offset(-210, 180,
					arrayStateRect, "S"), "K := S[(S[i] + S[j]) % 256] := S["
					+ sij + "] := 0x" + String.format("%02x", state[sij])
					+ " := " + sij, "kCalculationText0", null,
					parameterText13Properties);
			kxorpCalculationText0 = lang.newText(new Offset(0, 9,
					kCalculationText0, "SW"),
					"Output := K^P := 0x" + String.format("%02x", state[sij])
							+ " ^ 0x" + String.format("%02x", input[x])
							+ " := 0x" + String.format("%02x", output[x]),
					"kxorpCalculationText0", null, parameterText13Properties);

			lang.nextStep();
		}

		// Conclusion
		code.hide();
		codeRect.hide();
		secondaryCode.hide();
		subheader.hide();
		arrayStateRect.hide();
		arrayStateText.hide();
		arraySiStateRect.hide();
		arraySjStateRect.hide();
		iIdentifierText.hide();
		jIdentifierText.hide();
		parameters0.hide();
		parameters1.hide();
		parameters2.hide();
		parameters3.hide();
		parameters4.hide();
		parameters5.hide();
		parameters6.hide();
		parameters7.hide();
		plusPolyline0.hide();
		plusPolyline1.hide();
		ijState.hide();
		markSiRect.hide();
		iValueText.hide();
		jValueText.hide();
		arraySiStateText.hide();
		arraySjStateText.hide();
		siValueText.hide();
		sjValueText.hide();
		kRect.hide();
		pRect.hide();
		kDescriptionText.hide();
		pDescriptionText.hide();
		kValueText.hide();
		pValueText.hide();
		inputValueText.hide();
		outputValueText.hide();
		xorPolyline0.hide();
		xorPolyline1.hide();
		kCalculationText0.hide();
		kxorpCalculationText0.hide();
		arrayToKPolyline0.hide();
		arrayToKPolyline1.hide();
		arrayToKPolyline2.hide();
		siToArrayPolyline0.hide();
		siToArrayPolyline1.hide();
		siToArrayPolyline2.hide();
		sjToArrayPolyline0.hide();
		sjToArrayPolyline1.hide();
		sjToArrayPolyline2.hide();
		kxorpState.hide();

		subheader = lang.newText(new Offset(0, 30, headerRect, "C"),
				"Conclusion", "subheader", null, subheaderProperties);

		codeText = lang.newSourceCode(new Offset(-345, 43, headerRect, "S"),
				"codeText", null, sourceCodeTextProperties);
		codeText.addCodeLine(
				"RC4 has become part of commonly used encryption protocols and standards, including WEP",
				null, 0, null);
		codeText.addCodeLine(
				"and WPA for wireless cards and TLS. It's also used in PDF, BitTorrent and Skype.",
				null, 0, null);
		codeText.addCodeLine("", null, 0, null);
		codeText.addCodeLine(
				"Still, multiple vulnerabilities have been discovered in RC4, rendering it insecure.",
				null, 0, null);
		codeText.addCodeLine(
				"Unlike modern stream ciphers, RC4 does not take a separate nonce alongside the key.",
				null, 0, null);
		codeText.addCodeLine(
				"It's vulnerable to related key attacks and bit-flipping attacks.",
				null, 0, null);
		codeText.addCodeLine("", null, 0, null);
		codeText.addCodeLine(
				"The use of RC4 in TLS is prohibited by RFC 7465 published in February 2015 and follows",
				null, 0, null);
		codeText.addCodeLine(
				"the general trend of moving away from it to more secure encryption protocols.",
				null, 0, null);
		codeText.addCodeLine("", null, 0, null);
		codeText.addCodeLine(
				"Any reversible cipher lies in \u03a9(n) (where n is the size of the plaintext), since",
				null, 0, null);
		codeText.addCodeLine(
				"each bit needs to get touched at least once. A stream cipher such as RC4 does some",
				null, 0, null);
		codeText.addCodeLine(
				"fixed amount of work for each bit of output with initial preparation overhead",
				null, 0, null);
		codeText.addCodeLine("for the state, which results in O(n).", null, 0,
				null);

		lang.nextStep("Conclusion");
	}

	public String getName() {
		return "ARC4";
	}

	public String getAlgorithmName() {
		return "ARC4 (Alleged RC4)";
	}

	public String getAnimationAuthor() {
		return "David Becker";
	}

	public String getDescription() {
		// longer description in algorithm itself
		return "RC4 (Rivest Cipher 4, also known as ARC4 or ARCFOUR meaning Alleged RC4)\n"
				+ "is a software stream cipher used to encrypt a plaintext. Its simplicity and\n"
				+ "speed make it the most commonly used stream cipher.\n\n"
				+ "RC4 has become part of commonly used encryption protocols and standards,\n"
				+ "including WEP and WPA for wireless cards and TLS.\n\n"
				+ "Multiple vulnerabilities have been discovered in RC4, rendering it insecure.\n\n"
				+ "The algorithm can be divided into two parts:\n"
				+ "\u2022 Key-scheduling algorithm (KSA)\n"
				+ "\u2022 Pseudo-random generation algorithm (PRGA)";
	}

	public String getCodeExample() {
		return "\u2022 Key-scheduling algorithm (KSA)\n"
				+ "   for i from 0 to 255\n" + "       S[i] := i\n"
				+ "   endfor\n" + "   j := 0\n" + "   for i from 0 to 255\n"
				+ "       j := (j + S[i] + key[i mod keylength]) mod 256\n"
				+ "       swap values of S[i] and S[j]\n" + "   endfor\n\n"
				+ "\u2022 Pseudo-random generation algorithm (PRGA)\n"
				+ "   i := 0\n" + "   j := 0\n"
				+ "   while GeneratingOutput:\n"
				+ "       i := (i + 1) mod 256\n"
				+ "       j := (j + S[i]) mod 256\n"
				+ "       swap values of S[i] and S[j]\n"
				+ "       K := S[(S[i] + S[j]) mod 256]\n"
				+ "       P := next unencrypted plaintext byte"
				+ "       output (K XOR P)\n" + "   endwhile";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.ENGLISH;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_CRYPT);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer arg0,
			Hashtable<String, Object> arg1) throws IllegalArgumentException {
		String plaintextVal = (String) arg1.get("plaintext");
		String keyVal = (String) arg1.get("key");
		if (plaintextVal.isEmpty()) {
			throw new IllegalArgumentException("Plaintext can't be empty.");
		} else if (plaintextVal.length() > 14) {
			throw new IllegalArgumentException(
					"Plaintext can't be longer than 14 bytes/characters.\n\n"
							+ "ARC4 imposes no such restrictions and accepts arbitrary\n"
							+ "byte arrays as input. Limiting the size\n"
							+ "faciliates the visualisation.");
		}
		if (keyVal.isEmpty()) {
			throw new IllegalArgumentException("Key can't be empty.");
		} else if (keyVal.length() < 5) {
			throw new IllegalArgumentException(
					"Key has to consist of at least 5 bytes/characters.\n\n"
							+ "ARC4 imposes no such restrictions and accepts keys with\n"
							+ "1 \u2264 keylength \u2264 256 bytes.\n"
							+ "Typical ARC4 keylengths range from 5 to 16 bytes, which\n"
							+ "is the reason for the self-imposed lower bound of 5.");
		} else if (keyVal.length() > 16) {
			throw new IllegalArgumentException(
					"Key has to consist of at most 16 bytes/characters.\n\n"
							+ "ARC4 imposes no such restrictions and accepts keys with\n"
							+ "1 \u2264 keylength \u2264 256 bytes.\n"
							+ "Typical ARC4 keylengths range from 5 to 16 bytes, which\n"
							+ "is the reason for the self-imposed upper bound of 16.");
		}
		return true;
	}
}