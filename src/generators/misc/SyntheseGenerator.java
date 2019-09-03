package generators.misc;

import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.misc.impl.synthese.I18n;
import generators.misc.impl.synthese.SyntheseImpl;
import translator.Translator;

/*
 * SyntheseGenerator.java
 * Lawrence Emory Dean, Waldemar Graf, 2017 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */

public class SyntheseGenerator implements ValidatingGenerator {
	public static void appendGenerators(Vector<Generator> generators) {
		// Our Generators
		generators.add(new SyntheseGenerator("resources/synthese", Locale.GERMANY));
		generators.add(new SyntheseGenerator("resources/synthese", Locale.US));
	}
	
	private Language lang;
	private String resourceName;
	private Locale locale;
	private Translator translator;

	public SyntheseGenerator(String resourceName, Locale locale) {
		this.resourceName = resourceName;
		this.locale = locale;
		init();
	}

	public void init() {
		translator = new Translator(resourceName, locale);
		lang = new AnimalScript(getName(), getAnimationAuthor(), 800, 600);
		lang.setStepMode(true);
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		String[] funcDependencies = (String[]) primitives.get("functionalDependencies");
		new SyntheseImpl(funcDependencies, lang, translator);
		lang.finalizeGeneration();
		return lang.toString();
	}

	public String getName() {
		return translator.translateMessage(I18n.name);
	}

	public String getAlgorithmName() {
		return translator.translateMessage(I18n.algoName);
	}

	public String getAnimationAuthor() {
		return "Lawrence Dean, Waldemar Graf";
	}

	public String getDescription() {
		return translator.translateMessage(I18n.algoDesc);
	}

	public String getCodeExample() {
		return translator.translateMessage(I18n.textSteps);
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return locale;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	public static void main(String[] args) {
		SyntheseGenerator gen = new SyntheseGenerator("translatorfiles/synthese", Locale.US);
		gen.init();
		String[] defaultInput = new String[] { "A->B;E", "A;E->B;D", "B;D;E;F->C", "F->C;D", "C;D->B;E;F", "C;F->B",
				"C;F->B", "G;H->L" };
		new SyntheseImpl(defaultInput, gen.lang, gen.getTranslator());
	}

	public Translator getTranslator() {
		return this.translator;
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer arg0, Hashtable<String, Object> arg1)
			throws IllegalArgumentException {
		try {
			SyntheseGenerator copy = new SyntheseGenerator(resourceName, locale);
			copy.generate(arg0, arg1);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
