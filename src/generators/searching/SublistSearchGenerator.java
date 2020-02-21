/*
 * SublistSearchGenerator.java
 * , 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.searching;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;
import java.util.Set;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.AnimationType;
import algoanim.primitives.generators.Language;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
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
import animal.main.Animal;
import animal.variables.*;

public class SublistSearchGenerator implements Generator {
	/**
	 * predefined properties
	 */
	private Language lang;
	private SourceCodeProperties sourceCode;
	private int[] intArrayA;
	private ArrayProperties arrayPropertyA;
	private int[] intArrayB;
	private ArrayProperties arrayPropertyB;
	/**
	 * The header text representing the headline
	 */
	private Text header;
	/**
	 * The rectangle around the headline
	 */
	private Rect hRect;

	/**
	 * Globally defined text properties
	 */
	private TextProperties textProps;
	
	/**
	 * Globally defined text properties for variables
	 */
	private TextProperties varProps;
	/**
	 * the initial source code shown in the animation
	 */
	private SourceCode src;

	/**
	 * The rectangle around the isSubArrayCode() code
	 */
	private Rect sRect;

	/**
     * Globally defined default timing
     */
    private Timing defaultTiming;
    
    /**
	 * Globally defined Output text
	 */
    private Text output;
    
    /**
     * Gloablly defined result variable
     * True if subarray was found
     */
    private boolean result;
    
	/**
	 * Globally defined finish counter
	 * Restricts to run method generate only run 1 time
	 */
	private int finish;
	
	/**
	 * Globally defined counter to count the number of comparisons
	 */
	private int counter;
	 /**
     * Globally defined text to display the number of comparisons
     */
    private Text counterText;
    /** 
	 * Global Variables showed in the animation -> window -> Variables
	 */
	private Variables N;    
	private Variables M;    
	private Variables I;  
	private Variables J;  
	private Variables A_i;
	private Variables  B_j;

	public void init(){
		lang = new AnimalScript("Sublist-Search", "", 800, 600);
	}
	/**
	 * Default constructor
	 * 
	 * @param l
	 *          the concrete (animal) language object used for creating output
	 */
	public SublistSearchGenerator(Language l) {
		lang = l;
		lang.setStepMode(true);
	}
	public SublistSearchGenerator() {
		lang = new AnimalScript( "Sublist-Search", "Kai Tanaka, Tomoki Tokuyama",
				800, 600);
		lang.setStepMode(true);
	}

    public void search(int[] A, int[] B) {
		  
    	  Timing defaultTiming = new TicksTiming(30);
    	  
		  lang.nextStep("Introduction");
		  //STEP 1: Show Algorithmn Information with header

		  // show the header with a heading 
		  TextProperties headerProps = new TextProperties();
		  headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 28));
		  header = lang.newText(new Coordinates(20, 30), "Sublist Search","header", null, headerProps);
		  // surround header with rectangle
		  RectProperties rectProps = new RectProperties();
		  rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		  rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		  rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		  hRect = lang.newRect(new Offset(-5, -5, "header",
				  AnimalScript.DIRECTION_NW), new Offset(5, 5, "header", "SE"), "hRect", null, rectProps);
		  // set and display algorithmn description
		  textProps = new TextProperties();
		  textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 20));

		  lang.newText(new Coordinates(10, 100),
				  "Given two arrays A[] and B[] with respective length of n and m,this algorithmn checks whether the array B[] is a subarray of", 
				  "description1",null, textProps);
		  lang.newText(new Offset(0, 30, "description1",
				  AnimalScript.DIRECTION_NW),
				  "the array A[] or not.",
				  "description2", null, textProps);
		  lang.newText(new Offset(0, 60, "description2",
				  AnimalScript.DIRECTION_NW),
				  "Simple Approach:",
				  "description3", null, textProps);
		  lang.newText(new Offset(0, 30, "description3",
				  AnimalScript.DIRECTION_NW),
				  "A simple bruteforce approach is to run two nested loops and generate all subarrays of the array A[] and use one more loop",
				  "description4", null, textProps);
		  lang.newText(new Offset(0, 30, "description4",
				  AnimalScript.DIRECTION_NW),
				  "to check if any of the subarray of A[] is equal to the array B[].",
				  "description5", null, textProps);
		  lang.newText(new Offset(0, 60, "description5",
				  AnimalScript.DIRECTION_NW),
				  "Efficient Approach:",
				  "description6", null, textProps);
		  lang.newText(new Offset(0, 30, "description6",
				  AnimalScript.DIRECTION_NW),
				  "An efficient approach is to use two pointers to traverse both the array simultaneously. Keep the pointer of array B[]",
				  "description7", null, textProps);
		  lang.newText(new Offset(0, 30, "description7",
				  AnimalScript.DIRECTION_NW),
				  "still and if any element of A[] matches with the first element of B[] then increase the pointer of both the array else",
				  "description8", null, textProps);
		  lang.newText(new Offset(0, 30, "description8",
				  AnimalScript.DIRECTION_NW),
				  "increase the pointer of A and reset the pointer of B to 0. ",
				  "description9", null, textProps);
		  lang.newText(new Offset(0, 50, "description9",
				  AnimalScript.DIRECTION_NW),
				  "Returns true if  all the elements of B are matched else false.",
				  "description10", null, textProps);
		  
		  // STEP 2: Display arrays A,B and the source code and various texts
		  lang.nextStep("Algorithmn");
		  lang.hideAllPrimitives();
		  header.show();
		  hRect.show();
		  
		  // display source code
		  src = lang.newSourceCode(new Coordinates(10, 100), "src",null, sourceCode);
		  src.addCodeLine("public boolean isSubArray(int A[], int B[])  {", null, 0, null);  //0
		  src.addCodeLine("",null,0,null); 													 //1
		  src.addCodeLine("int n = A.length;", null, 2, null);								 //2
		  src.addCodeLine("int m = B.length;", null, 2, null);								 //3
		  src.addCodeLine("int i = 0, j = 0;",null,2,null);									 //4
		  src.addCodeLine("",null,0,null);													 //5
		  src.addCodeLine("while (i < n && j < m) {" ,null, 2, null);						 //6
		  src.addCodeLine("if (A[i] == B[j]) { ",null,3,null);								 //7
		  src.addCodeLine("i++;", null, 4, null);											 //8
		  src.addCodeLine("j++;", null,4,null);												 //9
		  src.addCodeLine("if (j == m) {",null,4,null);										 //10
		  src.addCodeLine("return true;",null,5,null);										 //11
		  src.addCodeLine("}",null,4,null);													 //12
		  src.addCodeLine("}",null,3, null);												 //13
		  src.addCodeLine("else {",null,3, null);											 //14
		  src.addCodeLine("if(A[i] != B[0]) {",null,4, null);								 //15
		  src.addCodeLine("i++;",null,5, null);											     //16
		  src.addCodeLine("}",null,4, null);												 //17
		  src.addCodeLine("j=0;",null,4, null);												 //18
		  src.addCodeLine("}",null,3, null);									             //19
		  src.addCodeLine("}",null,2, null);												 //20
		  src.addCodeLine("return false;",null,2, null);									 //21
		  src.addCodeLine("}",null,0, null);												 //22
		  
		  //Surround code with rectangle
		  sRect = lang.newRect(new Offset(-5, -5, "src",
				  AnimalScript.DIRECTION_NW), new Offset(5, 5, "src", "SE"), "sRect", null, rectProps);
		  
		  //Display Text and Arrays
		  varProps = new TextProperties();
		  varProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 15));
		  
		  //Place arrays
		  lang.newText(new Coordinates(320,120), "A:", "A", null, varProps);
		  IntArray a = lang.newIntArray(new Coordinates(350, 120 ), A, "A[]",null, arrayPropertyA);
		  lang.newText(new Coordinates(320,240), "B:", "B", null, varProps);
		  IntArray b = lang.newIntArray(new Coordinates(350, 240), B , "B[]",null, arrayPropertyB);
		  //Place output text
		  output = lang.newText(new Coordinates(320,330), "Output:", "OUTPUT", null, varProps);
		  //Place counter Text
		  counterText = lang.newText(new Coordinates(320,30), "Number of comparisons:", "count", null, varProps);
		  
		  
		  // STEP 3: Perform algorithmn
		  lang.nextStep();
		  isSubArray(a,b);
		  
		 // ..... wait ....
		  
		 //NEW STEP: Update output
		 lang.nextStep("Output");
		 output.setText("Output: " + result, null,null);
		  
		 //NEW STEP: final message
		 lang.nextStep("Conclusion");
		 lang.newText(new Coordinates(320,360), "As we can see the algorithm runs in O(n) run time with " + counter + " comparions where n is the length ", "message1", null, textProps);
		 lang.newText(new Offset(0, 30, "message1",
				  AnimalScript.DIRECTION_NW),
				  "of array A. The naive algorithmn (with 2 nested loops) would have a time complexity of O(n^2).",
				  "message2", null, textProps);
	  } 
    
    public void isSubArray(IntArray A, IntArray B)  {
    	
    	boolean finalResult = false;
    	 	
    	//highlight line 2 
    	src.highlight(2);
    	int n=A.getLength();
    	
    	//NEXT STEP: highlight line 3 
    	lang.nextStep();
    	src.toggleHighlight(2,3);
    	int m=B.getLength(); 
    	
    	//NEXT STEP: highlight line 4 and set pointers
    	lang.nextStep();
    	src.toggleHighlight(3, 4);
    	
    	ArrayMarkerProperties iP = new ArrayMarkerProperties();
    	iP.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
    	iP.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    	ArrayMarker iM = lang.newArrayMarker(A, 0, "i", null, iP);

    	ArrayMarkerProperties jP = new ArrayMarkerProperties();
    	jP.set(AnimationPropertiesKeys.LABEL_PROPERTY, "j");
    	jP.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    	ArrayMarker jM = lang.newArrayMarker(B, 0, "j", null, jP);
    	
 
    	
    	//NEW STEP: mark while
    	lang.nextStep();
    	src.toggleHighlight(4, 6);
        while (iM.getPosition() < n && jM.getPosition() < m) { 
        	//NEW STEP: mark while for loopback
        	lang.nextStep();
        	src.highlight(6);
        	//NEW STEP: mark if and cells
        	lang.nextStep();
        	src.unhighlight(6);
        	src.highlight(7);
        	A.highlightElem(iM.getPosition(), null,null);
        	B.highlightElem(jM.getPosition(), null,null);
        	counter++;
        	counterText.setText("Number of comparisons: " + counter, null,null);
            if (A.getData(iM.getPosition()) == B.getData(jM.getPosition())) { 
            	//NEW STEP: highlight cell elem instead
            	lang.nextStep();
            	A.unhighlightElem(iM.getPosition(), null,null);
            	B.unhighlightElem(jM.getPosition(), null,null);
            	A.highlightCell(iM.getPosition(), null,null);
            	B.highlightCell(jM.getPosition(), null,null);
            	//NEW STEP: execute line 8 : increment i
            	lang.nextStep();
            	src.unhighlight(7);
            	src.highlight(8);
            	iM.increment(null,defaultTiming);
            	I.set("i", String.valueOf(iM.getPosition()));
            	
            	//NEW STEP: execute 9 : increment j
                lang.nextStep();
                src.unhighlight(8);
                src.highlight(9);
                jM.increment(null, defaultTiming);
                J.set("j", String.valueOf(jM.getPosition()));
                
                //NEW STEP: mark if line 10
                lang.nextStep();
                src.unhighlight(9);
                src.highlight(10);
                if (jM.getPosition() == m) {
                  //NEW STEP: mark line 11 and terminate
                  lang.nextStep();
                  src.unhighlight(10);
                  src.highlight(11);
                  //set result to true and break
                  finalResult = true;
                  break;
                }
                // NEW STEP: unhighlight if
                lang.nextStep();
                src.unhighlight(10);
                
            } 
            else { 
            	//NEW STEP unhighlight if
            	lang.nextStep();
            	src.unhighlight(7);
            	//NEW STEP: IF false: unhighlight elem 
            	lang.nextStep();
            	A.unhighlightElem(0,iM.getPosition(), null,null);
            	B.unhighlightElem(0,jM.getPosition(), null,null);
                A.unhighlightCell(0,iM.getPosition(), null,null);
            	B.unhighlightCell(0,jM.getPosition(), null,null);
            	//NEW STEP: mark if line 15 and elemnts
            	lang.nextStep();
            	src.highlight(15);
            	A.highlightElem(iM.getPosition(), null,null);
            	B.highlightElem(0, null,null);
            	
            	if(A.getData(iM.getPosition()) != B.getData(0)) {
            		//NEW STEP: increment i
            		lang.nextStep();
            		A.unhighlightElem(iM.getPosition(), null,null);
                	B.unhighlightElem(0, null,null);
            		src.unhighlight(15);
            		src.highlight(16);
                    iM.increment(null, defaultTiming);
                    I.set("i", String.valueOf(iM.getPosition()));
            	}
                //NEW STEP: increment j
                lang.nextStep();
                A.unhighlightElem(iM.getPosition(), null,null);
            	B.unhighlightElem(0, null,null);
                src.unhighlight(15);
                src.unhighlight(16);
                src.highlight(18);
                jM.move(0, null, defaultTiming); 
                J.set("j", String.valueOf(0));
                //NEW STEP; unhighlight line 16 and go back
                lang.nextStep();
                src.unhighlight(18);
            } 
        } 
       // NEW STEP
        result = finalResult ? true : false;
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
    	
    	if(finish==1) {
    	lang.setStepMode(true);
        sourceCode = (SourceCodeProperties)props.getPropertiesByName("sourceCode");
        intArrayA = (int[])primitives.get("intArrayA");
        arrayPropertyA = (ArrayProperties)props.getPropertiesByName("arrayPropertyA");
        intArrayB = (int[])primitives.get("intArrayB");
        arrayPropertyB = (ArrayProperties)props.getPropertiesByName("arrayPropertyB");
        
        //Set variables for this animation: will be updated through algorithmn.
        // Note: Trying to set the role doesn't seem to work 
       
        N = lang.newVariables();
        N.declare("int", "n", String.valueOf(intArrayA.length));
        
        M = lang.newVariables();
        M.declare("int", "m", String.valueOf(intArrayB.length));
        
        I = lang.newVariables();
        I.declare("int", "i", "0");
        
        J = lang.newVariables();
        J.declare("int", "j", "0");
           
        //Start animation
        search(intArrayA, intArrayB);
        
        
    	}
    	finish++;
    	return lang.toString();
    }

    public String getName() {
        return "Sublist-Search";
    }

    public String getAlgorithmName() {
        return "Sublist-Search";
    }

    public String getAnimationAuthor() {
        return "Kai Tanaka, Tomoki Tokuyama";
    }

    public String getDescription(){
        return "Given two arrays A[] and B[] with respective length of n and m,this algorithmn checks whether the array B[] is a subarray of the array A[] or not."
 +"\n"
 +"\n"
 +"Simple Approach:"
 +"\n"
 +"A simple bruteforce approach is to run two nested loops and generate all subarrays of the array A[] and use one more loop to check if any of the subarray of A[] is equal to the array B[]."
 +"\n"
 +"\n"
 +"Efficient Approach:"
 +"\n"
 +"An efficient approach is to use two pointers to traverse both the array simultaneously. Keep the pointer of array B[] still and if any element of A[] matches with the first element of B[] then"
 +"\n"
 +"increase the pointer of both the array else increase the pointer of A and reset the pointer of B to 0. "
 +"\n"
 +"\n"
 +"Returns true if  all the elements of B are matched else false.";
    }

    public String getCodeExample(){
        return "public boolean isSubArray(int A[], int B[])  {"
    +"\n"
    +"       int n = A.length;"
    +"\n"
    +"       int m = B.length; "
    +"\n"
    +"       int i = 0, j = 0; "
    +"\n"
    +"\n"
    +"       while (i < n && j < m) { "
    +"\n"
    +"          if (A[i] == B[j]) { "
    +"\n"
    +"              i++; "
    +"\n"
    +"              j++; "
    +"\n"
    +"              if (j == m) {"
    +"\n"
    +"                return true; "
    +"\n"
    +"              }"
    +"\n"
    +"          } "
    +"\n"
    +"          else { "
    +"\n"
    +"              if(A[i]!=B[0]) {"
    +"\n"
    +"              i++; "
    +"\n"
    +"              }"
    +"\n"
    +"              j = 0; "
    +"\n"
    +"          } "
    +"\n"
    +"     } "
    +"\n"
    +"    return false; "
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
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_SEARCH);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }
    
	public static void main(String[] args) {
	    Generator generator = new SublistSearchGenerator(); // Generator erzeugen
	    Animal.startGeneratorWindow(generator); // Animal mit Generator starten
	}
	

}
