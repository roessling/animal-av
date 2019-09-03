package generators.misc;

import java.util.Hashtable;
import java.util.Locale;

import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

public class RoundRobinEn implements ValidatingGenerator {

	private RoundRobin roundRobin;



	public RoundRobinEn(boolean codeLanguage) {

		roundRobin = new RoundRobin(true, codeLanguage);
	}


	@Override
	public String generate(AnimationPropertiesContainer arg0, Hashtable<String, Object> arg1) {
		return roundRobin.generate(arg0, arg1);
	}


	@Override
	public String getAlgorithmName() {
		return roundRobin.getAlgorithmName();
	}


	@Override
	public String getAnimationAuthor() {
		return roundRobin.getAnimationAuthor();
	}


	@Override
	public String getCodeExample() {
		return roundRobin.getCodeExample();
	}


	@Override
	public Locale getContentLocale() {
		return roundRobin.getContentLocale();
	}


	@Override
	public String getDescription() {
		return roundRobin.getDescription();
	}


	@Override
	public String getFileExtension() {
		return roundRobin.getFileExtension();
	}


	@Override
	public GeneratorType getGeneratorType() {
		return roundRobin.getGeneratorType();
	}


	@Override
	public String getName() {
		return roundRobin.getName();
	}


	@Override
	public String getOutputLanguage() {
		return roundRobin.getOutputLanguage();
	}


	@Override
	public void init() {
		roundRobin.init();

	}


	@Override
	public boolean validateInput(AnimationPropertiesContainer arg0, Hashtable<String, Object> arg1) throws IllegalArgumentException {
		return roundRobin.validateInput(arg0, arg1);
	}

}
