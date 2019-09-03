package generators.maths.romannumbers;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.SourceCodeProperties;

public class RomanNumberGenerator implements Generator {
	private Language lang;

	public void init() {
		lang = new AnimalScript("Römische Zahlendarstellung",
				"Nicole Brunkhorst, Stefan Rado", 900, 900);
	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		SourceCodeProperties sourceCode = (SourceCodeProperties) props.getPropertiesByName("sourceCode");
		SourceCodeProperties description = (SourceCodeProperties) props.getPropertiesByName("descriptions");
		int number = (Integer) primitives.get("number");

		RomanNumbers roman = new RomanNumbers(lang);
		roman.setSourceCodeProperties(sourceCode);
		roman.setDescriptionProperties(description);
		roman.createAnimation(number);
		
		return lang.toString();
	}

	public String getName() {
		return "Römische Zahlendarstellung";
	}

	public String getAlgorithmName() {
		return "Römische Zahlendarstellung";
	}

	public String getAnimationAuthor() {
		return "Nicole Brunkhorst, Stefan Rado";
	}

	public String getDescription() {
		return "Als römische Zahlen bezeichnet man die gebräuchliche Zahlschrift die in der römischen Antike\n"
				+ "entstanden ist und noch heute für Nummern und besondere Zwecke genutzt wird.\n"
				+ "\n"
				+ "Dieser Algorithmus erklärt die Umwandlung von natürlichen Zahlen in die römische Zahldarstellung.\n";
	}

	public String getCodeExample() {
		return "public String toRomanNumeral(int number) {\n"
				+ "  if (number < 0 || number > 3999)\n"
				+ "    throw new IllegalArgumentException(\"Value must be in the range 0 - 3999.\");\n"
				+ "\n"
				+ "  if (number == 0)\n"
				+ "    return \"N\";\n"
				+ "\n"
				+ "  StringBuilder result = new StringBuilder();\n"
				+ "\n"
				+ "  final int[] values = \n"
				+ "    new int[] { 1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1 };\n"
				+ "  final String[] numerals = \n"
				+ "    new String[] { \"M\", \"CM\", \"D\", \"CD\", \"C\", \"XC\", \"L\", \"XL\", \"X\", \"IX\", \"V\", \"IV\", \"I\" };\n"
				+ "\n"
				+ "  for (int i = 0; i < values.length; i++) {\n"
				+ "    while (number >= values[i]) {\n"
				+ "      number -= values[i];\n"
				+ "      result.append(numerals[i]);\n"
				+ "    }\n"
				+ "  }\n"
				+ "  return result.toString();\n"
				+ "}";
	}

	public String getFileExtension() {
		return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	public Locale getContentLocale() {
		return Locale.GERMANY;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
	}

	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

}