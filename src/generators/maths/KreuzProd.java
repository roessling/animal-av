package generators.maths;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;


public class KreuzProd implements Generator {

	/**
	 * @author Anqi Wang
	 * @version 1.0
	 */
	private Language lang;
	private SourceCodeProperties scProps = new SourceCodeProperties();
	private ArrayProperties leftArrayProps = new ArrayProperties();
	private ArrayProperties rightArrayProps = new ArrayProperties();
	private ArrayProperties resultArrayProps = new ArrayProperties();

	
    private int[] leftVek = {3, 9, 5 };
    private int[] rightVek = { 2, 4, 1 };

	public Timing defaultTiming = new TicksTiming(15);

	public KreuzProd(){
	    this(new AnimalScript("Kreuzprodukt von Vektoren aus R3", "Anqi Wang", 800, 800));
	    
	}
	
	public KreuzProd(Language l) {

	}

	private static final String descr = "Zur Berechnung des Kreuzproduktes von zwei Vektoren der Laenge 3 (und NUR der Laenge 3!!) muessen die einzelnen Elemente der Vektoren richtig miteinander kombiniert werden.";

	private static final String code  = 
		"public int[] kreuzProd(int[] a, int[] b)" 			
		+ "\n{" 		
		+ "\n int[]result = new int[3];"  										
		+ "\n result[0] = a[1]*b[2]-a[2]*b[1];" 		
		+ "\n result[1] = a[2]*b[0]-a[0]*b[2];" 	
		+ "\n result[2] = a[0]*b[1]-a[1]*b[0];" 		
		+ "\n return result;" 					
		+ "\n }"; 		
	
	
	
	private IntArray createLeftIntArray(int[] a, int x, int y)
	  {
	    return lang.newIntArray(new Coordinates(x, y), a, "intArray", null, leftArrayProps);
	  }
	
	private IntArray createRightIntArray(int[] a, int x, int y)
	  {
		return lang.newIntArray(new Coordinates(x, y), a, "intArray", null, rightArrayProps);
	  }
	
	private IntArray createResultIntArray(int[] a, int x, int y)
	  {
		return lang.newIntArray(new Coordinates(x, y), a, "intArray", null, resultArrayProps);
	  }
	
	private SourceCode initSourceCode(int x, int y)
	  {
		
		SourceCode sc = lang.newSourceCode(new Coordinates(x, y), "sourceCode", null, scProps);

		// Add the lines to the SourceCode object.
		// Line, name, indentation, display delay

		sc.addCodeLine("public int[] kreuzProd(int[] a, int[] b){", null, 0, null);  // 0
		sc.addCodeLine("int[]result = new int[2];", null, 1, null); 
		sc.addCodeLine("result[0] = a[1] * b[2] - a[2] * b[1];", null, 1, null);  // 3
		sc.addCodeLine("result[1] = a[2] * b[0] - a[0] * b[2];", null, 1, null);  // 4
		sc.addCodeLine("result[2] = a[0] * b[1] - a[1] * b[0];", null, 1, null);  // 5
		sc.addCodeLine("return result;", null, 1, null);  // 6
		sc.addCodeLine("}", null, 0, null); // 13
		return sc;

	  }

	private void kreuzMult(int[] left, int[] right) throws LineNotExistsException {

	    int[] result = new int[3];
		 
//------------------ Textproperties ------------------------------------------
		TextProperties headerProps = new TextProperties();
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", 1, 24));
		
		TextProperties textProps = new TextProperties();
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif", 1, 12));
		textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.magenta);
	
		TextProperties x = new TextProperties();
		x.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", 1, 20));
		x.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
		
//------------------- Header --------------------------------------------------
		
		lang.newText(new Coordinates(70, 60), "Kreuzprodukt von zwei Vektoren der Laenge 3", "header", null,
				headerProps);

		RectProperties rectProperties = new RectProperties();
		rectProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.PINK);
		rectProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		rectProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		lang.newRect(new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW),
				new Offset(5, 5, "header", AnimalScript.DIRECTION_SE), "hRect",
				null, rectProperties);
		this.lang.nextStep();
		
//------------------ Darstellung der Vektoren, Texte und Sourcecode -----------------
		
	    IntArray leftArray = createLeftIntArray(left, 45, 250);
	    lang.newText(new Coordinates(45, 275), "a: linker Faktor", "", null, textProps);

	    IntArray rightArray = createRightIntArray(right, 195, 250);
	    lang.newText(new Coordinates(195, 275), "b: rechter Faktor", "", null, textProps);

	    IntArray resultArray = createResultIntArray(result, 330, 250);
	    lang.newText(new Coordinates(300, 275), "result: Produkt der Vektoren", "", null, textProps);
	    
	    lang.newText(new Coordinates(140, 260), "X", "", null, x);
	    lang.newText(new Coordinates(270, 250), "=", "", null, x);
	    
		SourceCode sc = initSourceCode(45, 330);
	    this.lang.nextStep();
	    
//------------------ begin highlighting ----------------------------------------------

	    
	    sc.highlight(0);
	    this.lang.nextStep();
	    
	    sc.unhighlight(0);
	    sc.highlight(2);
	    this.lang.nextStep();
	    
	    leftArray.highlightElem(1, null, defaultTiming);
	    rightArray.highlightElem(2, null, defaultTiming);
	    this.lang.nextStep();

	    leftArray.unhighlightElem(1, null, defaultTiming);
	    rightArray.unhighlightElem(2, null, defaultTiming);
	    leftArray.highlightElem(2, null, defaultTiming);
	    rightArray.highlightElem(1, null, defaultTiming);
	    this.lang.nextStep();
	    
        resultArray.put(0, left[1]*right[2]-left[2]*right[1], null, defaultTiming);
	    this.lang.nextStep();
	    
	    sc.toggleHighlight(2, 0, false, 3, 0);
	    this.lang.nextStep(); 
	    
	    leftArray.unhighlightElem(2, null, defaultTiming);
	    rightArray.unhighlightElem(1, null, defaultTiming);
	    leftArray.highlightElem(2, null, defaultTiming);
	    rightArray.highlightElem(0, null, defaultTiming);
	    this.lang.nextStep();
	    
	    leftArray.unhighlightElem(2, null, defaultTiming);
	    rightArray.unhighlightElem(0, null, defaultTiming);
	    leftArray.highlightElem(0, null, defaultTiming);
	    rightArray.highlightElem(2, null, defaultTiming);
	    this.lang.nextStep();
	    	    	    
        resultArray.put(1, left[2]*right[0]-left[0]*right[2], null, defaultTiming);
	    this.lang.nextStep();
	    
	    
	    sc.toggleHighlight(3, 0, false, 4, 0);
	    this.lang.nextStep();
	    
	    leftArray.unhighlightElem(0, null, defaultTiming);
	    rightArray.unhighlightElem(2, null, defaultTiming);
	    leftArray.highlightElem(0, null, defaultTiming);
	    rightArray.highlightElem(1, null, defaultTiming);
	    this.lang.nextStep();
	    
	    leftArray.unhighlightElem(0, null, defaultTiming);
	    rightArray.unhighlightElem(1, null, defaultTiming);
	    leftArray.highlightElem(1, null, defaultTiming);
	    rightArray.highlightElem(0, null, defaultTiming);
	    this.lang.nextStep();
	    
        resultArray.put(2, left[0]*right[1]-left[1]*right[0], null, defaultTiming);
	    this.lang.nextStep();

	    leftArray.unhighlightElem(1, null, defaultTiming);
	    rightArray.unhighlightElem(0, null, defaultTiming);
	    sc.toggleHighlight(4, 0, false, 5, 0);
	    this.lang.nextStep();
	    
	    
	}

	protected String getAlgorithmDescription() {
		return descr;
	}

	public String getName() {
		return "Kreuzprodukt";
	}
	

	public String getCodeExample() {
		return code;
	}
		
	public String getDescription() {
		return descr;
	}


	@Override
	public String generate(AnimationPropertiesContainer arg0, Hashtable<String, Object> arg1) {
		init();
		leftVek = (int[])arg1.get("leftArray");
		rightVek = (int[])arg1.get("rightArray");
		
		leftArrayProps = (ArrayProperties) arg0.getPropertiesByName("leftArrayProps");  
		rightArrayProps = (ArrayProperties) arg0.getPropertiesByName("rightArrayProps");  
		resultArrayProps = (ArrayProperties) arg0.getPropertiesByName("resultArrayProps");  

		scProps = (SourceCodeProperties) arg0.getPropertiesByName("sourceCodeProps"); 
		
		return generate();
	}

	 public String generate() {
			this.lang = new AnimalScript("Kreuzprodukt", "Anqi Wang", 640, 480);
		    lang.setStepMode(true);
			kreuzMult(leftVek, rightVek);
		    return lang.toString();
		  }
	
	@Override
	public String getAlgorithmName() {
		return "Kreuzprodukt";
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
	public void init() {
		this.lang = new AnimalScript("KreuzProdukt", "Anqi Wang", 640, 480);
		this.lang.setStepMode(true); 
		
	}
}


