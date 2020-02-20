/*
 * LongestIncreasingSubsequenceNaive.java
 * Tuan Kiet Tran, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.maths;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.CountablePrimitive;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.VisualStack;
import algoanim.primitives.generators.Language;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.TwoValueView;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CounterProperties;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Timing;
import animal.main.Animal;

public class LongestIncreasingSubsequenceNaive implements ValidatingGenerator {
	private int recursioncounter = 0;
    private Language lang;
    private SourceCodeProperties titleProps;
    private ArrayProperties arrayProps;
    private SourceCodeProperties descProp;
    private ArrayMarkerProperties arrayMarkerProps;
    private SourceCodeProperties scProps;
    private TextProperties tp;
    private int[] intArray;

    public void init(){
        lang = new AnimalScript("LIS Naive", "Tuan Kiet Tran", 800, 600);
        lang.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        titleProps = (SourceCodeProperties)props.getPropertiesByName("titleProps");
        arrayProps = (ArrayProperties)props.getPropertiesByName("arrayProps");
        descProp = (SourceCodeProperties)props.getPropertiesByName("descProp");
        arrayMarkerProps = (ArrayMarkerProperties)props.getPropertiesByName("arrayMarkerProps");
        scProps = (SourceCodeProperties)props.getPropertiesByName("scProps");
        tp = (TextProperties)props.getPropertiesByName("tp");
        intArray = (int[])primitives.get("intArray");

		//int arr[] = { 22, 9, 33, 21, 50, 41, 60 };
		int n = intArray.length;
		//System.out.println("Length of lis is " + lengthOfLIS(arr) + "\n");
		
		lisprep(intArray);

        return lang.toString();
    }
    
    @Override
	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		
    	intArray = (int[])primitives.get("intArray");
    	
    	if (intArray.length <= 7) {
    		return true;
    	}
    	else return false;
    	
	}
    
    
    public void lisprep(int[] array) {
    	
		// Create Description Text
    	
		SourceCode description = lang.newSourceCode(new Coordinates(400, 80), "sourceCode", null, descProp);
		description.addCodeLine("The simplest approach is to try to find all increasing subsequences and then", null, 0,
				null); // 0
		description.addCodeLine("returning the maximum length of the longest increasing subsequence. In order to ", null, 0,
				null); // 0
		description.addCodeLine("do this, we make use of a recursive function lengthofLIS which returns the length",
				null, 0, null); // 0
		description.addCodeLine("of the LIS possible from the current element(corresponding to 'curpos') ", null, 0,
				null); // 0
		description.addCodeLine("onwards(including the current element).", null, 0, null); // 0

		// Create algorithm title
		//SourceCodeProperties titleProps = new SourceCodeProperties();
		
		//TODO: vorruebergehende loesung
		titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 20));

		
		SourceCode title = lang.newSourceCode(new Coordinates(20, 5), "title", null, titleProps);

		title.addCodeLine("Longest Increasing Subsequence Naive", null, 0, null);
		
		lang.nextStep();
		
		description.hide();
		
	    // now, create the IntArray object, linked to the properties
	    IntArray ia = lang.newIntArray(new Coordinates(40, 120), array, "intArray",
	        null, arrayProps);
	    
	    
	    // Create counter
    	TwoValueCounter counter = lang.newCounter(ia);
    	CounterProperties cp = new CounterProperties(); 
    	cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    	cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE); 
    	
    	TwoValueView view = lang.newCounterView(counter,
    			new Coordinates(600, 20), cp, true, true);
	    
	    
	    // Create the SourceCode
	    SourceCode sc = lang.newSourceCode(new Coordinates(40, 160), "sourceCode",
	        null, scProps);
	    // Add the lines to the SourceCode object.
	    sc.addCodeLine("public static int lengthofLIS(int[] nums, int prev, int curpos)", null, 0,
	        null); // 0
	    sc.addCodeLine("{", null, 0, null);
	    sc.addCodeLine("if (curpos == nums.length)", null, 1, null);
	    sc.addCodeLine("{", null, 1, null); // 3
	    sc.addCodeLine("return 0;", null, 2, null); // 4
	    sc.addCodeLine("}", null, 1, null); // 5
	    sc.addCodeLine("int taken = 0;", null, 1, null); // 6
	    sc.addCodeLine("if (nums[curpos] > prev)", null, 1, null); // 7
	    sc.addCodeLine("{", null, 1, null); // 8
	    sc.addCodeLine("taken = 1 + lengthofLIS(nums, nums[curpos], curpos + 1);", null, 2, null); // 9
	    sc.addCodeLine("}", null, 1, null); // 10
	    sc.addCodeLine("int nottaken = lengthofLIS(nums, prev, curpos + 1);", null, 1, null); // 11
	    sc.addCodeLine("return Math.max(taken, nottaken);", "last return", 1, null); // 12
	    sc.addCodeLine("}", null, 0, null); // 13
	    
	    
		Text taken = lang.newText(new Coordinates(500,20), "taken: 0", "taken", null, tp);
		Text nottaken = lang.newText(new Coordinates(500,40), "nottaken: 0", "nottaken", null, tp);
		
	    sc.highlight(0, 0, false);
	    
	    ArrayMarker currentMarker = lang.newArrayMarker(ia, 0, "curpos",
	        null, arrayMarkerProps);
	    
	    int result = lengthofLIS(ia, Integer.MIN_VALUE, 0, taken, nottaken, sc, currentMarker, tp);
	    
	}
    
    
    
    public int lengthofLIS(IntArray nums, int prev, int curpos, Text takentext, Text nottakentext, SourceCode sc, ArrayMarker currentMarker, TextProperties tp) {

		currentMarker.move(curpos, Timing.MEDIUM, Timing.INSTANTEOUS);
		
		sc.highlight(0);
		
		
		lang.nextStep();
		
		//TODO: set current ELement pointer
		
		sc.unhighlight(0);
		sc.highlight(2);
		
		lang.nextStep();
		
		sc.unhighlight(2);
		
		if (curpos == nums.getLength()) {
			sc.highlight(4);
			lang.nextStep();
			sc.unhighlight(4);
			
			nums.unhighlightElem(0, nums.getLength()-1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			return 0;
		}
		
		sc.highlight(6);
		
		
		lang.nextStep();
		
		sc.unhighlight(6);
		sc.highlight(7);
		//TODO: chek if it makes sense????
		//currentlength = 0;
		
		int taken = 0;
		
		lang.nextStep();
		
		sc.unhighlight(7);
		
		if (nums.getData(curpos) > prev) {
			sc.highlight(9);
			lang.nextStep();
			sc.unhighlight(9);
			
			nums.highlightElem(curpos, null, null);
			recursioncounter++;
			taken = 1 + lengthofLIS(nums, nums.getData(curpos), curpos + 1, takentext, nottakentext, sc, currentMarker, tp);
			recursioncounter--;
			
			takentext.setText("taken:"+ Integer.toString(taken), Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		}
	
		sc.highlight(11);
		lang.nextStep();
		
		sc.unhighlight(11);
		recursioncounter++;
		int nottaken = lengthofLIS(nums, prev, curpos + 1, takentext, nottakentext, sc, currentMarker, tp);
		recursioncounter--;
		
		nottakentext.setText("nottaken:"+ Integer.toString(nottaken), Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		
		sc.highlight("last return");
		
		lang.nextStep();
		sc.unhighlight("last return");
		if (recursioncounter == 0) {
			sc.highlight("last return");
			
			Text endtext = lang.newText(new Coordinates(500,200), "The algorithm terminates and returns " + Integer.toString(Math.max(taken, nottaken)), "endtext", null, tp);
		}
		return Math.max(taken, nottaken);
	}
    
    

    public String getName() {
        return "LIS Naive";
    }

    public String getAlgorithmName() {
        return "Longest Increasing Subsequence Naive";
    }

    public String getAnimationAuthor() {
        return "Tuan Kiet Tran";
    }

    public String getDescription(){
        return "The longest increasing subsequence problem is to find the length of a subsequence of a "
 +"\n"
 +"given sequence in which the subsequence's elements are in sorted order, lowest to highest, and in"
 +"\n"
 +" which the subsequence is as long as possible. This subsequence is not necessarily contiguous, "
 +"\n"
 +"or unique.";
    }

    public String getCodeExample(){
        return "public static int lengthofLIS(int[] nums, int prev, int curpos)"
 +"\n"
 +"{"
 +"\n"
 +"    if (curpos == nums.length)"
 +"\n"
 +"    {"
 +"\n"
 +"        return 0;"
 +"\n"
 +"    }"
 +"\n"
 +"    int taken = 0;"
 +"\n"
 +"    if (nums[curpos] > prev)"
 +"\n"
 +"    {"
 +"\n"
 +"        taken = 1 + lengthofLIS(nums, nums[curpos], curpos + 1);"
 +"\n"
 +"    }"
 +"\n"
 +"    int nottaken = lengthofLIS(nums, prev, curpos + 1);"
 +"\n"
 +"    return Math.max(taken,nottaken);"
 +"\n"
 +"}";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.ENGLISH;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }
    
    
    public static void main(String[] args) {
    	Generator generator = new LongestIncreasingSubsequenceNaive(); // Generator erzeugen
    	Animal.startGeneratorWindow(generator); // Animal mit Generator starten
    	}

	
    

}