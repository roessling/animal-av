package generators;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;

public class Sumup extends AnnotatedAlgorithm implements Generator {
  @Override
  public String getAnimationAuthor() {
    return "Sebastian Proksch <sproksch[at]rbg.informatik.tu-darmstadt.de>";
  }
  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  public String getAnnotatedSrc()
	{
		return
			"int sumup(int number) {	@label(\"header\") @highlight(\"end\")\n"+
			"	int result = 0;			@label(\"variables\") @declare(\"int\", \"result\", \"0\")\n"+
			"	for(i = 0;				@label(\"forInit\") @declare(\"int\", \"i\")\n"+
			"	     i <= number;		@label(\"forCond\") @continue\n"+
			"	            i++)		@label(\"forInc\") @continue @inc(\"i\")\n"+
			"		result += i;		@label(\"sumup\") @eval(\"result\", \"result + i\")\n"+
			"	return result;			@label(\"return\")\n" +
			"}							@label(\"end\") @discard(\"i\")\n";
	}

	/**
	 * initalization method
	 */
	public void init()
	{
		super.init();

		SourceCodeProperties props = new SourceCodeProperties();
		props.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.BLUE);
		props.set(AnimationPropertiesKeys.BOLD_PROPERTY, true);
		sourceCode = lang.newSourceCode(new Coordinates(20, 20), "sumupCode", null, props);

		parse();
	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives)
	{
		init();
		
//		System.out.println(annotatedSrc);
		
		// framework provides parameter
		int number = ((Integer)primitives.get("Integer Argument")).intValue();
		
		// generate animation
		vars.declare("int", "number", String.valueOf(number));
		exec("header"); lang.nextStep();
		
		exec("variables"); lang.nextStep();
		exec("forInit"); lang.nextStep();
		exec("forCond"); lang.nextStep();
		
		int i = 0;
		while(i <= number) {
			exec("sumup"); lang.nextStep();
			exec("forInc"); lang.nextStep();
			exec("forCond"); lang.nextStep();
			
			i = Integer.parseInt(vars.get("i"));
		}
		
		exec("return");

		return lang.toString(); 
	}

	/**
	 * to give readers an impression
	 *
	public String getCodeExample() {
		return "int sumup(int number) {\n"+
		"  int result = 0;\n"+
		"  for(i = 0; i <= number; i++)\n"+
		"    result += i;\n"+
		"  return result;\n"+
		"}\n";

	}*/
	
	/**
	 * the locale of this animation
	 */
	public Locale getContentLocale() {
		return Locale.US;
	}

	/**
	 * description of this animation
	 */
	public String getDescription() {
		return "The summing up of numbers from 0 to n is animated in this generator to work as an example for the new annotation system";
	}

	/**
	 * fileextension for the generated file
	 */
	public String getFileExtension() {
		return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	/**
	 * the type of this animation
	 */
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
	}

	/**
	 * title to get displayed
	 */
	public String getName() {
		return "Sumup [annotation based]";
	}

	/**
	 * Name of the Algorithm
	 */
	public String getAlgorithmName() {
		return "Sumup";
	}
}
