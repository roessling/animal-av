package generators.cryptography;

import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;
import generators.cryptography.helpers.Substitution;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

public class SubstitutionGenerator implements Generator {

	private Language lang;

	public String getName() {
		return "Mono-alphabetic Substitution Cipher Demonstration";
	}

	public String getAlgorithmName() {
		return "Mono-alphabetic Substitution Cipher";
	}

	public String getAnimationAuthor() {
		return "Dominik Bollmann, Sogol Mazaheri";
	}

	public String getDescription() {
		return "Mono-alphabetic substitution ciphers 'encrypt' a given plain text by replacing every character in"
				+ " the plain text with another character according to a predefined substitution.\n"
				+ "Popular examples of simple substitution ciphers are the"
				+ " Ceaser Cipher, the ROT13 Cipher, and the BASE64 Encoding.\n"
				+ "Note that mono-alphabetic substitution ciphers are generally"
				+ " not cryptographically secure and should therefore not be used to encrypt confidential data.";
	}

	public String getCodeExample() {
		return Substitution.SOURCE_CODE.replace("\\", "");
	}

	public String getFileExtension() {
		return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	public Locale getContentLocale() {
		return Locale.US;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_CRYPT);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	public void init() {
		lang = new AnimalScript(getAlgorithmName(), getAnimationAuthor(), 800,
				600);
		lang.setStepMode(true);
	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {

		String plainText = (String) primitives.get("plainText");
		String inputAlphabet = (String) primitives.get("inputAlphabet");
		String outputAlphabet = (String) primitives.get("outputAlphabet");

		this.init();

		Substitution.setLanguage(lang);
		Substitution substCipher = new Substitution(inputAlphabet,
				outputAlphabet);

		substCipher.initSlide();
		String cipher = substCipher.apply(plainText);
		substCipher.finalSlide(plainText, cipher, outputAlphabet);

		return lang.toString();
	}
}