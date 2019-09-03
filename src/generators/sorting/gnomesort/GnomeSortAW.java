package generators.sorting.gnomesort;
import extras.lifecycle.common.Variable;
import extras.lifecycle.monitor.CheckpointUtils;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.Primitive;
import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;


public class GnomeSortAW implements Generator {

	/**
	 * @author Anqi Wang
	 * @version 1.0
	 */
	private Language lang;
	private SourceCodeProperties scProps = new SourceCodeProperties();
	private ArrayProperties arrayProps = new ArrayProperties();
    private static int[] sourceArray = { 2, 4, 1, 13, 52, 24, 5 };

	
	
	public Timing defaultTiming = new TicksTiming(15);

	public GnomeSortAW(){
	    this(new AnimalScript("GnomeSort Animation", "Anqi Wang", 800, 800));
	    
	}
	
	public GnomeSortAW(Language l) {

	}

	private static final String descr = 
	    "<ol><li> Betrachte paarweise das zu sortierende Array, der Index wird mit 1 initialisiert.</li>"
			+ "<li> Wenn das Zahlenpaar in der richtigen Reihenfolge steht, z&auml;hle Index um 1 hoch.</li>"
			+ "<li> Wenn das Zahlenpaar in der falschen Reihenfolge steht, z&auml;hle Index um 1 runter und vertausche das Paar.</li>"
			+ "<li> Terminierung wenn Index = array.length, wenn Index = 0 setze Index auf 1.</li></ol>";

	private static final String code = 
		"public void GnomeSort(int[] inputArray){"  					// 0
		+ "\n  for (int index = 1; index <= input.length; ) {" 				// 2
		+ "\n    if (input[index - 1] <= input[index]) {"								// 3
		+ "\n      ++index;" 								// 4
		+ "\n    } else { " 			// 5
		+ "\n        int tempVal = input[index];" 						// 6
		+ "\n        input[index] = input[index - 1];" 						// 7
		+ "\n        input[index - 1] = tempVal;"								// 8
		+ "\n        --index;" 												// 9
		+ "\n        if (index == 0) {"													// 10
		+ "\n          index = 1;"									// 11
		+ "\n      }" 									// 12
		+ "\n   }" 								// 13
		+ "\n }"; 		
	
	
	
	private IntArray createIntArray(int[] a)
	  {
		// Create Array: coordinates, data, name, display options,
		// default properties first, set the visual properties (somewhat similar
		// to CSS)
		arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
		arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
		arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
	    return lang.newIntArray(new Coordinates(45, 250), a, "intArray", null, arrayProps);
	  }
	
	
	
	private SourceCode initSourceCode(Primitive reference)
	  {
		// Create SourceCode: coordinates, name, display options, 
		// default properties

		// first, set the visual properties for the source code

		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif", Font.PLAIN, 14));
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);

		// now, create the source code entity
		SourceCode sc = lang.newSourceCode(new Coordinates(250, 170), "sourceCode", null, scProps);

		// Add the lines to the SourceCode object.
		// Line, name, indentation, display delay

		sc.addCodeLine("GnomeSort(int[] inputArray){", null, 0, null);  // 0
		sc.addCodeLine("for (int index = 1; index <= input.length; ){", null, 1, null); 
		sc.addCodeLine("if (input[index - 1] <= input[index] ) {", null, 2, null); 
		sc.addCodeLine("++index;", null, 2, null);  // 3
		sc.addCodeLine("} else{", null, 1, null);  // 4
		sc.addCodeLine("int tempVal = input[index]", null, 2, null);  // 5
		sc.addCodeLine("input[index] = input[index - 1];", null, 2, null);  // 6
		sc.addCodeLine("input[index - 1] = tempVal;", null, 2, null); // 7
		sc.addCodeLine("--index;", null, 2, null); // 8
		sc.addCodeLine("if ( index == 0 ) {", null, 2, null); // 9
		sc.addCodeLine("index = 1;", null, 3, null); // 10
		sc.addCodeLine("}", null, 2, null); // 13
		sc.addCodeLine("}", null, 1, null); // 14
		sc.addCodeLine("}", null, 0, null); // 13
		return sc;

	  }
	
	private ArrayMarker createArrayMarker(String label, int position, IntArray array)
	  {
	    ArrayMarkerProperties arrayMarkerProps = new ArrayMarkerProperties();
	    arrayMarkerProps.set("label", label);
	    arrayMarkerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
	    return this.lang.newArrayMarker(array, position, label, null, arrayMarkerProps);
	  }


	private void GnomeS(int[] inputArray) throws LineNotExistsException {

		TextProperties textProps = new TextProperties();
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"SansSerif", 1, 24));

		lang.newText(new Coordinates(70, 60), "GnomeSort", "header", null,
				textProps);

		RectProperties rectProperties = new RectProperties();
		rectProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
		rectProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY,
				Boolean.TRUE);
		rectProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		lang.newRect(new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW),
				new Offset(5, 5, "header", AnimalScript.DIRECTION_SE), "hRect",
				null, rectProperties);
		this.lang.nextStep();
		
		
	    IntArray array = createIntArray(inputArray);
		SourceCode sc = initSourceCode(array);
	    this.lang.nextStep();
	    
	    ArrayMarker indexMarker = createArrayMarker("index", 1, array);
	    this.lang.nextStep();

	    sc.highlight(0);
	    this.lang.nextStep();
	    
		for (int index = 1; index <= array.getLength();) {
			sc.unhighlight(3);
			sc.unhighlight(10);
			sc.toggleHighlight(0, 0, false, 1, 0);
		    this.lang.nextStep();
		    
			array.highlightElem(index, null, defaultTiming);
			array.highlightElem(index - 1, null, defaultTiming);
			if (array.getData(index - 1) <= array.getData(index)) {
				sc.toggleHighlight(1, 0, false, 2, 0);
				lang.nextStep();
				index++;
				sc.toggleHighlight(2, 0, false, 3, 0);
				lang.nextStep();
				if (index == array.getLength()){
					sc.unhighlight(3);
					lang.nextStep();
					break;
				}
				array.unhighlightElem(index - 2, null, defaultTiming);
				array.highlightElem(index, null, defaultTiming);
				indexMarker.move(index, null, defaultTiming);
				lang.nextStep();
				sc.unhighlight(2);
				lang.nextStep();

			} else {
				lang.nextStep();
				sc.unhighlight(1);
				sc.toggleHighlight(3, 0, false, 4, 0);
				lang.nextStep();
				CheckpointUtils.checkpointEvent(this, "swaps", new Variable("ele1",array.getData(index)),new Variable("ele2",array.getData(index - 1)));
				array.swap(index - 1, index, null, defaultTiming);
				sc.toggleHighlight(4, 0, false, 5, 0);
				sc.highlight(6);
				sc.highlight(7);
				lang.nextStep();
				index--;
				sc.unhighlight(5);
				sc.unhighlight(6);
				sc.toggleHighlight(7, 0, false, 8, 0);
				lang.nextStep();

				sc.unhighlight(8);
				lang.nextStep();
				array.unhighlightElem(index + 1, null, defaultTiming);
				array.highlightElem(index - 1, null, defaultTiming);
				indexMarker.move(index, null, defaultTiming);
				lang.nextStep();

				if (index == 0) {
					lang.nextStep();
					sc.highlight(9);
					lang.nextStep();
					index = 1;
					sc.toggleHighlight(9, 0, false, 10, 0);
					lang.nextStep();
					array.highlightElem(index, null, defaultTiming);
					sc.unhighlight(9);
					lang.nextStep();
				} else {
					lang.nextStep();
				}
			}
		}
	}

	protected String getAlgorithmDescription() {
		return descr;
	}

	public String getName() {
		return "GnomeSort";
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
		sourceArray = (int[])arg1.get("array");
		
		arrayProps = (ArrayProperties) arg0.getPropertiesByName("arrayProps");  
		scProps = (SourceCodeProperties) arg0.getPropertiesByName("sourceCode"); 
		
		return generate();
	}

	 public String generate() {
			this.lang = new AnimalScript("GnomeSort", "Anqi Wang", 640, 480);
		    lang.setStepMode(true);
			GnomeS(sourceArray);
		    return lang.toString();
		  }
	
	@Override
	public String getAlgorithmName() {
		return "Gnome Sort";
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
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
	}

	@Override
	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

	@Override
	public void init() {
		this.lang = new AnimalScript("GnomeSort", "Anqi Wang", 640, 480);
		this.lang.setStepMode(true); 
		
	}
}
