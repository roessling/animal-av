package generators.tree;

import java.util.Hashtable;
import java.util.Locale;

import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

public class ID3de implements ValidatingGenerator {

	private ID3 id3;



	public ID3de(boolean codeLanguage) {

		id3 = new ID3(false, codeLanguage);
	}


	@Override
	public String generate(AnimationPropertiesContainer arg0, Hashtable<String, Object> arg1) {
		return id3.generate(arg0, arg1);
	}


	@Override
	public String getAlgorithmName() {
		return id3.getAlgorithmName();
	}


	@Override
	public String getAnimationAuthor() {
		return id3.getAnimationAuthor();
	}


	@Override
	public String getCodeExample() {
		return id3.getCodeExample();
	}


	@Override
	public Locale getContentLocale() {
		return id3.getContentLocale();
	}


	@Override
	public String getDescription() {
		return id3.getDescription();
	}


	@Override
	public String getFileExtension() {
		return id3.getFileExtension();
	}


	@Override
	public GeneratorType getGeneratorType() {
		return id3.getGeneratorType();
	}


	@Override
	public String getName() {
		return id3.getName();
	}


	@Override
	public String getOutputLanguage() {
		return id3.getOutputLanguage();
	}


	@Override
	public void init() {
		id3.init();

	}


	@Override
	public boolean validateInput(AnimationPropertiesContainer arg0, Hashtable<String, Object> arg1) throws IllegalArgumentException {
		return id3.validateInput(arg0, arg1);
	}

}
