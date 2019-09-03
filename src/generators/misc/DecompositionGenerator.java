package generators.misc;

/*
 * DecompositionGenerator.java
 * Lawrence Emory Dean, Waldemar Graf, 2017 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */

import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.misc.impl.decomposition.Decomposition;
import generators.misc.impl.decomposition.I;
import translator.Translator;

public class DecompositionGenerator implements ValidatingGenerator {
	public static void appendGenerators(Vector<Generator> generators) {
		// Our Generators
		generators.add(new DecompositionGenerator("resources/decomposition", Locale.GERMANY));
		generators.add(new DecompositionGenerator("resources/decomposition", Locale.US));

	}
	
	private Language lang;
	private String resourceName;
	private Locale locale;
	public Translator translator;

	public DecompositionGenerator(String resourceName, Locale locale) {
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
		String[] closureIntroAttributes = (String[])primitives.get("closureIntroAttributes");
		new Decomposition(funcDependencies, closureIntroAttributes, lang, translator);
		lang.finalizeGeneration();
		return lang.toString();
	}

	public String getName() {
		return translator.translateMessage(I.name);
	}

	public String getAlgorithmName() {
		return translator.translateMessage(I.algoName);
	}

	public String getAnimationAuthor() {
		return "Lawrence Dean, Waldemar Graf";
	}

	public String getDescription() {
		return translator.translateMessage(I.description);
	}

	public String getCodeExample() {
		return translator.translateMessageWithoutParameterExpansion(I.instructions)
				+ translator.translateMessageWithoutParameterExpansion(I.symbolTable);
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
		DecompositionGenerator gen = new DecompositionGenerator("translatorfiles/decomposition", Locale.GERMANY);
		gen.init();
		String[] defaultInput = new String[] { "A->B;C;D;E", "E->F;G;H", "I->J", "A;I->K", "A;L->M" };
		new Decomposition(defaultInput, new String[] {"A"}, gen.lang, gen.translator);
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer paramAnimationPropertiesContainer,
			Hashtable<String, Object> paramHashtable) throws IllegalArgumentException {
		
		try {
			DecompositionGenerator copy = new DecompositionGenerator(resourceName, locale);
			copy.generate(paramAnimationPropertiesContainer, paramHashtable);
			return true;
		} catch(Exception e) {
			return false;
		}
	}

}
