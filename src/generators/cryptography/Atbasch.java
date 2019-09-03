package generators.cryptography;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.awt.Font;
import java.util.Locale;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;

import java.util.Hashtable;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class Atbasch implements ValidatingGenerator {
	private Language lang;
	private SourceCodeProperties sourceCodeProperties, descriptionProperties;
	private ArrayProperties plainTextProperties, cipherTextProperties;
	private ArrayProperties alphabetDeutschProperties, alphabetAtbaschProperties;
	private String plainText;
	private RectProperties titleBoxProperties, sourceCodeBoxProperties;
	private ArrayMarkerProperties arrayMarkerProperties;
	private TextProperties titleProperties;
	

	public void init() {
		lang = new AnimalScript("Atbasch [DE]",
				"Nicolas Kolb, Marco Br&auml;uning", 1024, 768);
		lang.setStepMode(true);
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
	}

	private String[] inputConversion(String text) {
		String input = text.toUpperCase().replace("Ä", "AE").replace("Ö", "OE")
				.replace("Ü", "UE").replace("ß", "SS");

		String[] result = new String[input.length()];
		for (int i = 0; i < input.length(); i++) {
			result[i] = String.valueOf(input.charAt(i));
		}
		return result;

	}

	private String[] generateEmptyStringArray(int size) {
		String[] out = new String[size];
		for (int i = 0; i < size; i++) {
			out[i] = "  ";
		}
		return out;
	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		sourceCodeProperties = (SourceCodeProperties) props
				.getPropertiesByName("sourceCodeProperties");
		plainTextProperties = (ArrayProperties) props
				.getPropertiesByName("plainTextProperties");
		alphabetDeutschProperties = (ArrayProperties) props
				.getPropertiesByName("alphabetDeutschProperties");
		plainText = (String) primitives.get("plainText");
		descriptionProperties = (SourceCodeProperties) props
				.getPropertiesByName("descriptionProperties");
		cipherTextProperties = (ArrayProperties) props
				.getPropertiesByName("cipherTextProperties");
		alphabetAtbaschProperties = (ArrayProperties) props
				.getPropertiesByName("alphabetAtbaschProperties");
		titleProperties = (TextProperties) props
				.getPropertiesByName("titleProperties");
		arrayMarkerProperties = (ArrayMarkerProperties) props
				.getPropertiesByName("arrayMarkerProperties");
		sourceCodeBoxProperties = (RectProperties) props
				.getPropertiesByName("sourceCodeBoxProperties");
		titleBoxProperties = (RectProperties) props
				.getPropertiesByName("titleBoxProperties");
		
		titleProperties.set(AnimationPropertiesKeys.FONT_PROPERTY,
				((Font) titleProperties
						.get(AnimationPropertiesKeys.FONT_PROPERTY))
						.deriveFont(Font.BOLD, 24));
		lang.newText(new Coordinates(20, 30), "Atbasch-Chiffre", "header",
				null, titleProperties);
		lang.newRect(new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW),
				new Offset(5, 5, "header", "SE"), "hRect", null, titleBoxProperties);

		lang.nextStep();

		TextProperties textProps = new TextProperties();
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.BOLD, 20));
		Text descriptionHeader = lang.newText(new Coordinates(20, 80),
				"Beschreibung des Algorithmus", "descriptionHeader", null,
				textProps);

		SourceCode intro = lang.newSourceCode(new Offset(0, 30,
				"descriptionHeader", AnimalScript.DIRECTION_NW), "descr", null,
				descriptionProperties);

		intro.addCodeLine(
				"Atbasch ist eine einfache Methode zur Verschlüsselung bzw. Entschlüsselung ",
				null, 0, null);
		intro.addCodeLine(
				"eines Textes und beruht auf dem Hebräischen Alphabet. Der Name leitet sich von den",
				null, 0, null);
		intro.addCodeLine(
				"beiden ersten und letzten Buchstaben des hebräischen Schriftsystems (A-T-B-Sch) ab.",
				null, 0, null);
		intro.addCodeLine("", null, 0, null);
		intro.addCodeLine(
				"Atbasch gehört zu den einfachen monoalphabetischen Substitutions-Verfahren. ",
				null, 0, null);
		intro.addCodeLine(
				"Eine Besonderheit ist, dass es genügt, die Atbasch-Substitution ein zweites Mal auf den",
				null, 0, null);
		intro.addCodeLine(
				"Geheimtext anzuwenden, um den Ursprungstext zu erhalten. Daher zählt es auch zu den",
				null, 0, null);
		intro.addCodeLine(
				"invulorischen (Ver- und Entschlüsselungsmethode sind identisch) Verfahren.",
				null, 0, null);
		intro.addCodeLine("", null, 0, null);
		intro.addCodeLine(
				"Das Prinzip von Atbasch ist simpel: Die Reihenfolge der Buchstaben des Alphabets wird invertiert",
				null, 0, null);
		intro.addCodeLine(
				"und anschließend wieder den Buchstaben des Alphabets zugewiesen:",
				null, 0, null);
		intro.addCodeLine("", null, 0, null);
		intro.addCodeLine(
				"A  B C  D   E  F  G H   I   J   K  L  M  N  O  P  Q R S  T  U  V  W  X   Y  Z",
				null, 0, null);
		intro.addCodeLine(
				"Z  Y  X  W  V  U  T  S  R  Q  P  O N  M   L  K  J   I  H  G  F  E  D  C  B  A",
				null, 0, null);
		intro.addCodeLine("", null, 0, null);
		intro.addCodeLine(
				"Beispielsweise wird das Wort 'GEHEIM' verschlüsselt zu 'TVSVRN'.",
				null, 0, null);
		intro.addCodeLine("", null, 0, null);
		intro.addCodeLine(
				"Wie alle monoalphabetischen Substitutions-Verfahren ist auch Atbasch seit der Verbreitung von ",
				null, 0, null);
		intro.addCodeLine(
				"Computern nicht mehr sicher. Genau genommen war eine kryptographische Sicherheit ",
				null, 0, null);
		intro.addCodeLine(
				"praktisch nie vorhanden, da es völlig ohne geheime oder öffentliche Schlüssel funktioniert.",
				null, 0, null);
		intro.addCodeLine("", null, 0, null);
		intro.addCodeLine(
				"Die asymptotische Komplexität dieses Verfahrens beträgt O(|input|).",
				null, 0, null);

		lang.nextStep("Einleitung");
		intro.hide();
		descriptionHeader.hide();

		textProps = new TextProperties();
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.BOLD, 20));
		lang.newText(new Coordinates(20, 80), "", "exmplHeader", null,
				textProps);


		plainTextProperties.set(AnimationPropertiesKeys.FONT_PROPERTY,
				((Font) plainTextProperties
						.get(AnimationPropertiesKeys.FONT_PROPERTY))
						.deriveFont((float) 16));
		
		cipherTextProperties.set(AnimationPropertiesKeys.FONT_PROPERTY,
				((Font) plainTextProperties
						.get(AnimationPropertiesKeys.FONT_PROPERTY))
						.deriveFont((float) 16));

		StringArray input = lang.newStringArray(new Offset(0, 30,
				"exmplHeader", AnimalScript.DIRECTION_SW),
				inputConversion(plainText), "input", null, plainTextProperties);

		StringArray alphabet = lang.newStringArray(new Offset(0, 90,
				"exmplHeader", AnimalScript.DIRECTION_SW),
				new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I",
						"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
						"U", "V", "W", "X", "Y", "Z" }, "alphabet", null,
				alphabetDeutschProperties);

		StringArray revers = lang.newStringArray(new Offset(0, 130,
				"exmplHeader", AnimalScript.DIRECTION_SW),
				new String[] { "Z", "Y", "X", "W", "V", "U", "T", "S", "R",
						"Q", "P", "O", "N", "M", "L", "K", "J", "I", "H", "G",
						"F", "E", "D", "C", "B", "A" }, "revers", null,
				alphabetAtbaschProperties);

		StringArray result = lang.newStringArray(new Offset(0, 190,
				"exmplHeader", AnimalScript.DIRECTION_SW),
				generateEmptyStringArray(inputConversion(plainText).length),
				"result", null, cipherTextProperties);

		textProps = new TextProperties();
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.BOLD, 20));
		lang.newText(new Coordinates(500, 80), "Java-Code", "codeHeader", null,
				textProps);

		SourceCode sc = lang.newSourceCode(new Offset(0, 10, "codeHeader",
				AnimalScript.DIRECTION_SW), "code", null, sourceCodeProperties);

		sc.addCodeLine("private String atbasch(String input) {", null, 0, null);
		sc.addCodeLine("String output = new String();", null, 1, null);
		sc.addCodeLine("for (char c : input.toCharArray()) {", null, 1, null);
		sc.addCodeLine("output += (char) (155 - ((int) c));", null, 2, null);
		sc.addCodeLine("}", null, 1, null);
		sc.addCodeLine("return output;", null, 1, null);
		sc.addCodeLine("}", null, 0, null);

		lang.newRect(new Offset(-5, -5, "code", AnimalScript.DIRECTION_NW),
				new Offset(5, 5, "code", "SE"), "hRect", null, sourceCodeBoxProperties);

		String atbaschResult = atbasch(input, alphabet, revers, result, sc);

		textProps = new TextProperties();
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.BOLD, 18));
		lang.newText(new Coordinates(20, 360),
				"Das Ergebnis der Atbasch-Chiffrierung lautet:",
				"atbaschResult", null, textProps);
		lang.newText(new Offset(0, 5, "atbaschResult",
				AnimalScript.DIRECTION_SW), atbaschResult, "resultText", null,
				textProps);

		sc.highlight(5);
		lang.nextStep("Ende");
		lang.finalizeGeneration();
		return lang.toString();
	}

	private String atbasch(StringArray input, StringArray alphabet,
			StringArray revers, StringArray result, SourceCode code) {

		String output = "";

		for (int i = 0; i < input.getLength(); i++) {

			ArrayMarker markerInput = lang.newArrayMarker(input, i,
					"markerInput", null, arrayMarkerProperties);

			code.highlight(2);

			lang.nextStep((i + 1) + ". Iteration");

			code.unhighlight(2);

			alphabet.highlightCell(
					((int) input.getData(i).toCharArray()[0]) - 65, null, null);

			lang.nextStep();

			revers.highlightCell(
					((int) input.getData(i).toCharArray()[0]) - 65, null, null);

			lang.nextStep();

			alphabet.unhighlightCell(
					((int) input.getData(i).toCharArray()[0]) - 65, null, null);
			revers.unhighlightCell(
					((int) input.getData(i).toCharArray()[0]) - 65, null, null);

			result.highlightCell(i, null, null);
			result.put(i, String.valueOf((char) (155 - ((int) input.getData(i)
					.toCharArray()[0]))), null, null);

			output += (char) (155 - ((int) input.getData(i).toCharArray()[0]));

			code.highlight(3);

			lang.nextStep();
			code.unhighlight(3);

			markerInput.hide();
			result.unhighlightCell(i, null, null);
		}
		return output;
	}

	public String getName() {
		return "Atbasch [DE]";
	}

	public String getAlgorithmName() {
		return "Atbasch";
	}

	public String getAnimationAuthor() {
		return "Nicolas Kolb, Marco Br&auml;uning";
	}

	public String getDescription() {
		return ("Atbasch ist eine einfache Methode zur Verschl&uuml;sselung bzw. Entschl&uuml;sselung "
				+ "\n"
				+ "eines Textes und beruht auf dem Hebr&auml;ischen Alphabet. Der Name leitet sich von den"
				+ "\n"
				+ "beiden ersten und letzten Buchstaben des hebr&auml;ischen Schriftsystems (A-T-B-Sch) ab."
				+ "\n"
				+ "\n"
				+ "Atbasch geh&ouml;rt zu den einfachen monoalphabetischen Substitutions-Verfahren. "
				+ "\n"
				+ "Eine Besonderheit ist, dass es gen&uuml;gt, die Atbasch-Substitution ein zweites Mal auf den "
				+ "\n"
				+ "Geheimtext anzuwenden, um den Ursprungstext zu erhalten. Daher z&auml;hlt es auch zu den"
				+ "\n"
				+ "invulorischen (Ver- und Entschl&uuml;sselungsmethode sind identisch) Verfahren."
				+ "\n"
				+ "\n"
				+ "Das Prinzip von Atbasch ist simpel: Die Reihenfolge der Buchstaben des Alphabets wird invertiert"
				+ "\n"
				+ "und anschlie&szlig;end wieder den Buchstaben des Alphabets zugewiesen:"
				+ "\n"
				+ "\n"
				+ "A  B C  D   E  F  G H   I   J   K  L  M  N  O  P  Q R S  T  U  V  W  X  Y   Z"
				+ "\n"
				+ "Z  Y  X  W  V  U  T  S  R  Q  P  O N  M   L  K  J   I  H  G  F  E  D  C  B  A"
				+ "\n"
				+ "\n"
				+ "Beispielsweise wird das Wort \"GEHEIM\" verschl&uuml;sselt zu \"TVSVRN\"."
				+ "\n"
				+ "\n"
				+ "Wie alle monoalphabetischen Substitutions-Verfahren ist auch Atbasch seit der Verbreitung von "
				+ "\n"
				+ "Computern nicht mehr sicher. Genau genommen war eine kryptographische Sicherheit "
				+ "\n"
				+ "praktisch nie vorhanden, da es v&ouml;llig ohne geheime oder &ouml;ffentliche Schl&uuml;ssel funktioniert."
				+ "\n"
				+ "Die asymptotische Komplexit&auml;t dieses Verfahrens betr&auml;gt O(|input|)."
				+ "\n").replace("\n", "<br>");
	}

	public String getCodeExample() {
		return "public String atbasch(String input) {" + "\n"
				+ "   String output = \"\";" + "\n"
				+ "   for (char c : input.toCharArray()) {" + "\n"
				+ "      output +=  (char) (155-((int) c));" + "\n" + "   }"
				+ "\n" + "   return output;" + "\n" + "}";
	}

	public String getFileExtension() {
		return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	public Locale getContentLocale() {
		return Locale.GERMANY;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_CRYPT);
	}

	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives)
			throws IllegalArgumentException {

		String input = (String) primitives.get("plainText");

		if (!input.matches("([a-zA-Z])+"))
			throw new IllegalArgumentException("\n\nBitte verwenden sie nur gültige Zeichen für 'plainText'.\n\n"
					+ "(A-Z und a-z, keine Leerzeichen, Umlaute oder Sonderzeichen!)");

		return true;
	}

}