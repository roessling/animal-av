package generators.misc;

import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

public class Prototype implements Generator {

	@Override
	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		final Language l = new AnimalScript("Testbed", "Dominik Fischer", 800, 300);
		l.setStepMode(true);
		l.nextStep();
		return l.toString();
	}

	@Override
	public String getAlgorithmName() {
		return "Prototype";
	}

	@Override
	public String getAnimationAuthor() {
		return "Dominik Fischer";
	}

	@Override
	public String getCodeExample() {
		return "pass";
	}

	@Override
	public Locale getContentLocale() {
		return Locale.ENGLISH;
	}

	@Override
	public String getDescription() {
		return "This is a correctly implemented generator that does absolutely nothing.\n"
				+ "Its single purpose is to provide a testbed for "
				+ "bugfixes in the language object and its relatives.";
	}

	@Override
	public String getFileExtension() {
		return "asu";
	}

	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
	}

	@Override
	public String getName() {
		return "Prototype";
	}

	@Override
	public String getOutputLanguage() {
		return "Pseudocode";
	}

	@Override
	public void init() {
	}

}
