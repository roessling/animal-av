package generators.maths;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.IntMatrix;
import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;


public class ModPow implements Generator {

	private Language lang;
	private SourceCodeProperties scProps = new SourceCodeProperties();
	private SourceCode sc;
	private MatrixProperties matrixProps = new MatrixProperties();
	private IntMatrix paint;
	private int[] sourceArray;
	private int base;
	private int expo;
	private int modulo;
	private int numRek;
	private int[][] paintMatrix;
	private int count = 0;


	public Timing defaultTiming = new TicksTiming(25);

	public ModPow(){
		this(new AnimalScript("Schnelle Exponentiation", "Anqi Wang", 800, 800));
	}

	public ModPow(Language l) {

	}

	private static final String descr = 
		"<ol> Durch die Beobachtung, dass a<sup>b</sup> : "
		+ "<li> bei geradem b &auml;quivalent ist zu (a<sup>b/2</sup>)<sup>2</sup> mod N </li>"
		+ "<li> und bei ungeradem b &auml;quivalent ist zu a*(a<sup>(b-1)/2</sup>)<sup>2</sup></li>"
		+ "wird die Exponentiation beschleunigt, da in jedem Rekursionsschritt der Exponent halbiert wird.</ol>";

	private static final String code  = 
		"public int modPow( int a, int b, int N){ "  					// 0
		+ "\n if (a <= 1 || b <= 0) return Integer.MAX_VALUE;" 				// 2
		+ "\n int result, tmp;"								// 3
		+ "\n  if (b == 1){" 								// 4
		+ "\n    return a; " 			// 5
		+ "\n  } else {" 						// 6
		+ "\n    if(b % 2 == 0){" 						// 7
		+ "\n      tmp = modPow(a, b/2, N);"								// 8
		+ "\n      result = (int)((Math.pow(tmp,2))%N);" 												// 9
		+ "\n      return result;"													// 10
		+ "\n    } else {"									// 11
		+ "\n        tmp = modPow(a, (b-1)/2, N);" 	
		+ "\n        result = (a*(int)(Math.pow(tmp,2))%N);"
		+ "\n        return result;"// 12
		+ "\n      }" 
		+ "\n   }" // 13
		+ "\n }"; 	


	private IntMatrix createMatrix(int[][] a)
	{
		return lang.newIntMatrix(new Coordinates(45, 190), a, "intMatrix", null, matrixProps);
	}

	private SourceCode initSourceCode()
	{
		sc = lang.newSourceCode(new Coordinates(250, 170), "sourceCode", null, scProps);

		// Add the lines to the SourceCode object.
		// Line, name, indentation, display delay

		sc.addCodeLine("public int modPow( int a, int b, int N){", null, 0, null); 					// 0
		sc.addCodeLine("if (a <= 1 || b <= 0) return Integer.MAX_VALUE;", null, 1, null);				// 2
		sc.addCodeLine("int result, tmp;", null, 1, null);	
		sc.addCodeLine("if (b == 1){", null, 2, null);								
		sc.addCodeLine("return a;", null, 3, null);
		sc.addCodeLine("}else {", null, 2, null);						// 6
		sc.addCodeLine("if(b % 2 == 0){", null, 3, null);					// 7
		sc.addCodeLine("tmp = modPow(a, b/2, N);", null, 4, null);							// 8
		sc.addCodeLine("result = (int)((Math.pow(tmp,2))%N);", null, 4, null);
		sc.addCodeLine("return result;", null, 4, null);													// 10
		sc.addCodeLine("}else {", null, 3, null);									// 11
		sc.addCodeLine("tmp = modPow(a, (b-1)/2, N);", null, 4, null);	
		sc.addCodeLine("result = (a*(int)(Math.pow(tmp,2))%N);", null, 4, null);
		sc.addCodeLine("return result;", null, 4, null);
		sc.addCodeLine("}", null, 3, null); 
		sc.addCodeLine("}", null, 2, null); 
		sc.addCodeLine("}", null, 1, null); 
		return sc;

	}

	public void doHeader(){
		TextProperties textProps = new TextProperties();
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"SansSerif", 1, 24));

		lang.newText(new Coordinates(70, 60), "Schnelle Exponentiation: a^b mod N", "header", null,
				textProps);

		RectProperties rectProperties = new RectProperties();
		rectProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
		rectProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY,
				Boolean.TRUE);
		rectProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		lang.newRect(new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW),
				new Offset(5, 5, "header", AnimalScript.DIRECTION_SE), "hRect",
				null, rectProperties);

		TextProperties titleProp = new TextProperties();
		titleProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif", 1, 14));
		titleProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.green);

		lang.newText(new Coordinates(46, 520), "- Jede Zeile der Matrix repraesentiert einen neuen rekursiven Funktionsaufruf mit jeweils neuem Wert fuer b.", "", null, titleProp);

	}

	public int modPow( int a, int b, int N){ 

		sc.highlight(0);

		TextProperties titleProp = new TextProperties();
		titleProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif", 1, 14));
		titleProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.red);
		this.lang.nextStep();

		int tmp1 = count; 

		if (a <= 1 || b <= 0) return Integer.MAX_VALUE;
		sc.toggleHighlight(0, 0, false, 1, 0);
		this.lang.nextStep();

		sc.unhighlight(1);
		this.lang.nextStep();

		int result, tmp; //result = (a^b)mod N

		if (b == 1){

			sc.highlight(3);
			sc.highlight(4);
			this.lang.nextStep();

			++count;
			tmp1 = count;
			
			paint.put(tmp1-1, 3, a, null, defaultTiming);

			sc.unhighlight(3);
			sc.unhighlight(4);
			this.lang.nextStep();

			this.lang.nextStep();
			return a;
		}
		else{
			sc.unhighlight(3);
			sc.unhighlight(4);
			sc.highlight(5);
			this.lang.nextStep();

			sc.unhighlight(5);
			this.lang.nextStep();

			if(b % 2 == 0){

				sc.highlight(6);
				sc.highlight(7);
				sc.highlight(8);
				sc.highlight(9);
				this.lang.nextStep();

				++count;
				tmp1 = count;
				paint.put(tmp1, 0, a, null, defaultTiming);
				paint.put(tmp1, 1, b/2, null, defaultTiming);
				paint.put(tmp1, 2, N, null, defaultTiming);

				this.lang.nextStep();

				sc.unhighlight(6);
				sc.unhighlight(7);
				sc.unhighlight(8);
				sc.unhighlight(9);
				this.lang.nextStep();

				tmp = modPow(a, b/2, N);
				result = (int)((Math.pow(tmp,2))%N);
				paint.put(tmp1-1, 3, result, null, defaultTiming);
				this.lang.nextStep();



				return result;
			}
			else{

				sc.highlight(10);
				sc.highlight(11);
				sc.highlight(12);
				sc.highlight(13);
				this.lang.nextStep();

				++count;
				tmp1 = count;
				paint.put(tmp1, 0, a, null, defaultTiming);
				paint.put(tmp1, 1, (b-1)/2, null, defaultTiming);
				paint.put(tmp1, 2, N, null, defaultTiming);

				this.lang.nextStep();

				sc.unhighlight(10);
				sc.unhighlight(11);
				sc.unhighlight(12);
				sc.unhighlight(13);
				this.lang.nextStep();

				tmp = modPow(a, (b-1)/2, N);
				result = (a*(int)(Math.pow(tmp,2))%N);
				paint.put(tmp1-1, 3, result, null, defaultTiming);
				this.lang.nextStep();


				return result;
			}
		}
	}

	protected String getAlgorithmDescription() {
		return descr;
	}

	public String getName() {
		return "Schnelle Exponentiation";
	}


	public String getCodeExample() {
		return code;
	}

	public String getDescription() {
		return descr;
	}

	@Override
	public String generate(AnimationPropertiesContainer arg0, Hashtable<String, Object> arg1) {
		
		sourceArray = (int[])arg1.get("array");
		base = sourceArray[0];
		expo = sourceArray[1];
		modulo = sourceArray[2];
		matrixProps = (MatrixProperties) arg0.getPropertiesByName("matrixProps");  
		scProps = (SourceCodeProperties) arg0.getPropertiesByName("sourceCode"); 

		localInit();

		return generate();
	}

	public void doAnimation(){		 
		this.doHeader();
		sc = this.initSourceCode();
		base = sourceArray[0];
		expo = sourceArray[1];
		modulo = sourceArray[2];
		paintMatrix[0][0] = base;
		paintMatrix[0][1] = expo;
		paintMatrix[0][2] = modulo;
		paint = createMatrix(paintMatrix);

		TextProperties titleProp = new TextProperties();
		titleProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", 1, 20));
		titleProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
		lang.newText(new Coordinates(46, 170), "a", "", null, titleProp);
		lang.newText(new Coordinates(76, 165), "b", "", null, titleProp);
		lang.newText(new Coordinates(106, 165), "N", "", null, titleProp);
		lang.newText(new Coordinates(136, 165), "result", "", null, titleProp);

	}

	public String generate() {
		this.lang = new AnimalScript("Schnelle Exponentiation", "Anqi Wang", 640, 480);
		lang.setStepMode(true);
		this.doAnimation();
		modPow(base, expo, modulo);
		return lang.toString();
	}

	@Override
	public String getAlgorithmName() {
		return "Exponentiation";
	}

	@Override
	public String getAnimationAuthor() {
		return "Anqi Wang";
	}

	@Override
	public Locale getContentLocale() {
		return Locale.GERMANY;
	}

	@Override
	public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
	}

	@Override
	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

    @Override
	public void init() {}
	public void localInit() {
		count = 0;
		numRek = (int)Math.floor(Math.log((double)sourceArray[1])/Math.log(2.0)) + 1;
		paintMatrix = new int [numRek][4];
		this.lang = new AnimalScript("Schnelle Exponentiation: berechnet a^b modulo N", "Anqi Wang", 640, 480);
		this.lang.setStepMode(true); 
	}
}


