package generators.maths;

import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.primitives.Text;
import algoanim.primitives.updater.TextUpdater;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;

public class Fibonacci extends AnnotatedAlgorithm implements Generator {

	private String assi = "Assignments";
	private String comp = "Compares";
	private String recdep = "Recursiondepth";
  @Override
  public String getAnimationAuthor() {
    return "Sebastian Proksch <sproksch[at]rbg.informatik.tu-darmstadt.de>";
  }
  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

	public String getAnnotatedSrc()
	{

		return "int fib(int number) {	@label(\"header\") @highlight(\"end\") @openContext @inc(\""+recdep+"\")\n"+
		"	int result;					@label(\"vars\") @declare(\"int\", \"result\")\n" +
		"	if(number == 0)				@label(\"test0\") @inc(\""+comp+"\")\n" +
		"		result = 1;				@label(\"res0\") @inc(\""+assi+"\") @set(\"result\", \"1\")\n"+
		"	elseif(number == 1)			@label(\"test1\") @inc(\""+comp+"\")\n" +
		"		result = 1;				@label(\"res1\") @inc(\""+assi+"\") @set(\"result\", \"1\")\n" +
		"	else {						@label(\"else\") @highlight(\"elseend\")\n" +

		"		int fib1 = 				@label(\"setfib1\") @dec(\""+recdep+"\") @closeContext @inc(\""+assi+"\") @highlight(\"calcfib1\")\n" + 
		"							fib(number - 1);	@label(\"calcfib1\") @continue\n"+
		
		"		int fib2 =				@label(\"setfib2\") @dec(\""+recdep+"\") @closeContext @inc(\""+assi+"\") @highlight(\"calcfib2\")\n" +
		"							fib(number - 2);	@label(\"calcfib2\") @continue\n" + 
		
		"		result = fib1 + fib2;	@label(\"result\") @inc(\""+assi+"\") @eval(\"result\", \"fib1 + fib2\")\n" +
		"	}							@label(\"elseend\")\n" +
		"	return result;				@label(\"return\")\n" +
		"}								@label(\"end\")\n";
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

		// instantiate source code primitive to work with
		sourceCode = lang.newSourceCode(new Coordinates(20, 10), "sumupCode", null, props);
//		System.out.println(annotatedSrc);
		
		// parsing anwerfen
		parse();
	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives)
	{
		init();
		
		// init all vars used in the visualization
		vars.declare("int", assi, "0");   vars.setGlobal(assi);
		vars.declare("int", comp, "0");   vars.setGlobal(comp);
		vars.declare("int", recdep, "0"); vars.setGlobal(recdep);

		Text text = lang.newText(new Coordinates(270, 100), "", "rekText", null);
		TextUpdater recdepTU = new TextUpdater(text);
		recdepTU.addToken("aktuelle Rekursionstiefe: ");
		recdepTU.addToken(vars.getVariable(recdep));
		recdepTU.update(); // zum Initialisieren
		
		Text compText = lang.newText(new Coordinates(270, 120), "", "complexText", null);
		TextUpdater compTU = new TextUpdater(compText);
		compTU.addToken(assi + ": ");
		compTU.addToken(vars.getVariable(assi));
		compTU.addToken(" - " + comp + ": ");
		compTU.addToken(vars.getVariable(comp));
		compTU.update(); // zum Initialisieren
		
		// fetch provided parameter from Framework
		int param = ((Integer)primitives.get("Integer Argument")).intValue();
		
//		System.out.println("result => fib("+param+") = " + fib(param));
		fib(param);
		return lang.toString(); 
	}
	
	public int fib(int number)
	{
		exec("header");
		vars.declare("int", "number", String.valueOf(number));
		lang.nextStep();
		
		exec("vars"); lang.nextStep();
		
		exec("test0"); lang.nextStep();
		if(number == 0) {
			this.exec("res0"); lang.nextStep();
		} else {
			this.exec("test1"); lang.nextStep();
		}

		if(number == 1) {
			exec("res1"); lang.nextStep();
		} else if(number != 0 && number != 1) {
			exec("else"); lang.nextStep();
			
			exec("calcfib1"); lang.nextStep();
			int fib1 = fib(number - 1);
			
			exec("setfib1");
			vars.declare("int", "fib1", String.valueOf(fib1));
			lang.nextStep();
			
			exec("calcfib2"); lang.nextStep();
			int fib2 = fib(number - 2);
			
			exec("setfib2");
			vars.declare("int", "fib2", String.valueOf(fib2));
			lang.nextStep();
			
			exec("result"); lang.nextStep();
		}

		exec("return"); lang.nextStep();
		return Integer.parseInt(vars.get("result"));
	}
	
	/**
	 * to give readers an impression
	 *
	public String getCodeExample() {
		return "int fib(int number) {\n" +
				"  int result;\n" +
				"  if(number == 0)\n" +
				"    result = 1;\n" +
				"  elseif(number == 1)\n" +
				"    result = 1;\n" +
				"  else {\n" +
				"    int fib1 = fib(number - 1);\n" +
				"    int fib2 = fib(number - 2);\n" +
				"    result = fib1 + fib2;\n" +
				"  }\n" +
				"  return result;\n" +
				"}";
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
		return "This is the second example for the new annotationsystem. the animation visualizes the calculation of a fibonacci-number.";
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
		return "Fibonacci [annotation based]";
	}

	/**
	 * Name of the Algorithm
	 */
	public String getAlgorithmName() {
		return "Fibonacci";
	}
}