package generators.sorting.gnomesort;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.Primitive;
import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;


/**   
 * @author Patrick Fongue, Matthias Prager, Daniel Staesche
 * @version 1.0 2008-09-12
 *
 */
public class GnomeSort implements Generator{


	private static final String DESCRIPTION = 
		"Put items in order by comparing the current item with the previous item. " +
		"If they are in order, move to the next item (or stop if the end is reached)." +
		"If they are out of order, swap them and move to the previous item." +
		"If there is no previous item, move to the next item." +
		"When the stop state is reached the array is sorted.";
	
	private static final String SOURCE_CODE = 
			  "public int[] gnomeSort(int[] a) {\n"
			+ "  int i;\n"
			+ "  for (i = 1; i < a.length;) {\n"
			+ "    if (a[i-1] <= a[i]) {\n"
			+ "      i++;\n"
			+ "    } else {\n"
			+ "      swap(a, i, i - 1);\n"
			+ "      if(i>1) {\n"
			+ "        i--;\n"
			+ "      } else {\n"
			+ "        i++;\n"
			+ "      }"
			+ "    }\n"
			+ "  }\n"
			+ "  return a;\n" 
			+ "}";        
	private static final String AUTHOR = "Patrick Fongue, Matthias Prager, Daniel Staesche";
//	private static final String ALGORITHM_NAME = "GnomeSort";
	
	
	/**
	 * The concrete language object used for creating output
	 */
	private Language lang;
	
	
	public void init() {
		// Store the language object
		lang = new AnimalScript(getName(), getAnimationAuthor(), 620, 480);
		// This initializes the step mode. Each pair of subsequent steps has to
		// be divdided by a call of lang.nextStep();
		lang.setStepMode(true);
		// draw the algo-name
		lang.newText(new Coordinates(0,0), getName(), "AlgoTitle", null);	
	}
	
	private IntArray createIntArray(int[] a){
	    // set the array's properties and create the animated array 
	    ArrayProperties arrayProps = new ArrayProperties();
            arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
            arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
            arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);   
            arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, 
                Color.BLACK);
            arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, 
                Color.RED);
            arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, 
                Color.YELLOW);
            return lang.newIntArray(new Coordinates(20, 100), a, "intArray", 
			null, arrayProps);
	}
	
	private SourceCode initSourceCode(Primitive reference){
	    // first, set the visual properties for the source code
	    SourceCodeProperties scProps = new SourceCodeProperties();
	    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
	    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", 
			Font.PLAIN, 12));

	    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, 
			Color.RED);   
	    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

	    // now, create the source code entity
	    int spacing_distance=50;
	    SourceCode code = lang.newSourceCode(new Offset(0,spacing_distance ,
			reference, AnimalScript.DIRECTION_SW), "sourceCode", null,
			scProps);
	    // Add the lines to the SourceCode object.
	    // Line, name, indentation, display dealy
	    code.addCodeLine("public int[] gnomeSort(int[] a) {", null, 0, null);  
	    code.addCodeLine("int i;", null, 1, null); 
	    code.addCodeLine("for (i = 1; i < a.length;){", null, 1, null); 
	    code.addCodeLine("if (a[i-1] <= a[i]) {", null, 2, null);  
	    code.addCodeLine("i++;", null, 3, null); 
	    code.addCodeLine("} else {", null, 2, null); 
	    code.addCodeLine("swap(a, i, i - 1);", null, 3, null); 
	    code.addCodeLine("if(i>1) { ", null, 3, null); 
	    code.addCodeLine("i--;", null, 4, null);
	    code.addCodeLine("} else{ ", null, 3, null);
	    code.addCodeLine("i++;", null, 4, null); 
	    code.addCodeLine("}", null, 3, null); 
	    code.addCodeLine("}", null, 2, null); 
	    code.addCodeLine("}", null, 1, null); 
	    code.addCodeLine("return a;", null, 1, null); 
	    code.addCodeLine("}", null, 0, null);
	    return code;
	}
	
	/**
	 * Unhighlights the current line and highglights
	 * the following one instead.
	 * Ceased by a nextStep() on the current language-object.
	 * 
	 * @param code the SourceCode object the highlighting is applied to
	 * @param currentLine the line currently highlighted
	 */
	private void highlightNextLine(SourceCode code, int currentLine){
	    code.unhighlight(currentLine);
	    code.highlight(currentLine+1);
	    lang.nextStep();
	}
	
	/**
	 * The same like <b>highlightThatLine(SourceCode, int, int)</b> with
	 * a default value of 0 for the last parameter.
	 * 
	 * @param code the SourceCode object the highlighting is applied to
	 * @param thatLine the line to be highlighted
	 */
	private void highlightThatLine(SourceCode code, int thatLine){
	    highlightThatLine(code,thatLine, 0);
	}
	
	/**
	 * Highlights the given line without highlighting any other line.
	 * Ceased by a nextStep(int) on the current language-object, passing
	 * the given duration.
	 * 
	 * @param code the SourceCode object the highglighting is applied to
	 * @param thatLine the line to be highlighted
	 * @param duration the duration to be passed to nextStep(int) given in ms
	 */
	private void highlightThatLine(SourceCode code, int thatLine, int duration){
	    code.highlight(thatLine);
	    lang.nextStep(duration);
	}
	
	private ArrayMarker createArrayMarker(String label, int position, IntArray array){
	    // create an array marker with desired label and position on a given array
	    // Array, current index, name, display options, properties
	    ArrayMarkerProperties arrayMarkerProps = new ArrayMarkerProperties();
	    arrayMarkerProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, label);   
	    arrayMarkerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
	    return lang.newArrayMarker(array, position, label ,	null, arrayMarkerProps);
	}
	

	/**
	 * Sort the int array passed in
	 * @param inputArray the array to be sorted
	 */
	public void gnomeSort(int[] inputArray) {
	
		// default properties
		final Timing swapDuration = new TicksTiming(75);
		final Timing moveDuration = new TicksTiming(25);
		final Timing ifDuration = new MsTiming(1500);
		
		// create the array
		IntArray animalArray = createIntArray(inputArray);
		final int arraySize = animalArray.getLength();
		
		// start a new step after the array was created
		lang.nextStep();		
		
		// show the source code
		SourceCode animalSourceCode = initSourceCode(animalArray);		
		lang.nextStep();
		
		// Highlight all cells
		animalArray.highlightCell(0, arraySize - 1, null, null);
		
		// Highlight the first line: function header 
		highlightThatLine(animalSourceCode, 0);
		
		// Create markers to point to i		
		ArrayMarker animalIMarker = createArrayMarker("i",1, animalArray);
		ArrayMarker animalPrevMarker = createArrayMarker("i-1",0,animalArray);
		
		// Highlight next line: int i;
		highlightNextLine(animalSourceCode, 0);

		// Prepare to highlight next line
		animalSourceCode.unhighlight(1);
	
		//GnomeSort
		for (int i=1; i<arraySize;) {
		    	// highlight lines: for loop, invariant
			highlightThatLine(animalSourceCode, 2); 
			highlightNextLine(animalSourceCode, 2);	
			animalSourceCode.unhighlight(3);		
			
			if(animalArray.getData(i-1)<=animalArray.getData(i)){
			    highlightThatLine(animalSourceCode,4,ifDuration.getDelay());
			    i++; // GnomeSort i++
			    if(i == arraySize){
				// don't show markers, when animation has finished
				animalIMarker.hide();
				animalPrevMarker.hide();			
			    } else {
				animalIMarker.move(i, null, moveDuration);
				animalPrevMarker.move(i-1, null, moveDuration);
			    }
			    animalSourceCode.unhighlight(4);
			} else {			
			    highlightThatLine(animalSourceCode, 5);
			    highlightNextLine(animalSourceCode,5);
			    animalArray.swap(i, i-1,null,swapDuration); // GnomeSort: swap
			    lang.nextStep();
			    highlightNextLine(animalSourceCode,6);
			    animalSourceCode.unhighlight(7);
			    if(i>1){
				highlightThatLine(animalSourceCode, 8, ifDuration.getDelay());
				i--; // GnomeSort: i--
				animalSourceCode.unhighlight(8);
			    } else {
				highlightThatLine(animalSourceCode,9,ifDuration.getDelay());
				i++; // GnomeSort: i++
				highlightNextLine(animalSourceCode, 9);
				
				animalSourceCode.unhighlight(10);				
			    }
			    animalIMarker.move(i, null, moveDuration);
			    animalPrevMarker.move(i-1, null, moveDuration);
			}
		}
		
		highlightThatLine(animalSourceCode,14);
		animalSourceCode.unhighlight(14);

	}
	
	protected String getAlgorithmDescription() {
		return DESCRIPTION;
	}
	
	protected String getAlgorithmCode() {
		return SOURCE_CODE;
	}
	
	public String getFileExtension(){
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}
	public Locale getContentLocale(){
		return Locale.US;
	}
	public GeneratorType getGeneratorType(){
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
	}
	public String getName(){
		return getAlgorithmName();
	}
	public String getAlgorithmName() {
		return "Gnome Sort";
	}
	
	public String getAnimationAuthor(){
		return AUTHOR;
	}
	public String getOutputLanguage(){
		return Generator.JAVA_OUTPUT;
	}
	
	public String getDescription() {
		return DESCRIPTION;
	}
	
	public String getCodeExample() {
		return SOURCE_CODE;
	}
	
	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives){
		init();
		int[] arrayData=(int[])primitives.get("array");
		gnomeSort(arrayData);
		return lang.toString();
	}
}

