
/*
 * ZAlgorithmGenerator.java
 * Kai Tanaka, 2019 for the Animal project at TU Darmstadt.
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
import algoanim.primitives.generators.Language;

import java.awt.Color;
import java.awt.Font;
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

public class ZAlgorithmGenerator implements ValidatingGenerator {
	/**
     * The concrete (animal) language object used for creating output
     */
    private Language lang;
    
    /**
	 * The header text representing the headline
     */
    private Text header;
    
    /**
     * The rectangle around the headline
     */
    private Rect hRect;
    
    /**
     * Globally defined text properties for default text
     */
    private TextProperties textProps;
    
    /**
     * Globally defined text properties for showing steps
     */
    private TextProperties stepProps;
    
    /**
     * the main source code for getZArr() shown in the animation
     */
    private SourceCode Zsrc;
    
    /**
     * The rectangle around the getZArray() code
     */
    private Rect zRect;
    
    /**
     * the initial source code for search() shown in the animation
     */
    private SourceCode initSrc;
    
    /**
     * The rectangle around the search() code
     */
    private Rect sRect;
    
    /**
     * Globally defined source code properties
     */
    private SourceCodeProperties sourceCode;
    
    /**
     * Globally defined array property for the stringArray
     */
    private ArrayProperties stringArrayProperty;
    
    /**
     * Globally defined array property for the ZArray
     */
    private ArrayProperties ZArrayProperty;
    
    /**
     * Pattern P
     */
    private String Pattern;
    
    /*
     * Text/String S
     */
    private String Text;
    
    /**
     * Globally defined default timing
     */
    private Timing defaultTiming;
    
    /**
     * Globally defined counter text
     */
    private Text compCounter;
    
    /**
	 * Globally defined counter variable denoting the number of comparisons
     */
	private int counter;

	/**
	 * Global lock variable to ensure the generate() method runs only once
	 * (Some how it runs twice)
	 * Without the counter, animation is bugged! 
	 */
	private int finish;
	
	/** 
	 * Global Variables showed in the animation -> window -> Variables
	 */
	private Variables lVar;    // L
	private Variables rVar;    // R
	private Variables rSubL;   // L-R
	private Variables tempVar; // R-i+1
	private Variables kVar;    // K=i-L
	
    public void init(){
    	
    }
    
    /**
	   * Default constructor
	   * 
	   * @param l
	   *          the concrete (animal) language object used for creating output
	   */
    public ZAlgorithmGenerator(Language l) {
        lang = l;
        lang.setStepMode(true);
	  }
    public ZAlgorithmGenerator() {
        this(new AnimalScript( "Z-Algorithm", "Kai Tanaka, Tomoki Tokuyama",
            800, 600));
      }
    
    /**
	   * This method initializes the animation. Shows a start page with a description. Then,
	   * displays the animation with source code executing the z algorithmn.
	   * 
	   * @param text
	   * 		  the text string
	   * 
	   * @param pattern
	   *          the pattern string we are looking for in text
	   *          
	   * @param sa
	   * 		   the string array consisting of P + "$" + S
	   * 		   with Pattern P and String S
	   */
    public void search(String text, String pattern, String[] sa) {
    	
         lang.nextStep("Introduction");
         
         
    	 // Create Z array
		 int[] Z = new int[sa.length];
		 
	     //Declare default timing
		 Timing defaultTiming = new TicksTiming(30);
		 
		 // show the header with a heading 
		 TextProperties headerProps = new TextProperties();
		 headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 28));
		 header = lang.newText(new Coordinates(20, 30), "Z-Algorithmn","header", null, headerProps);
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
				 "The Z-algorithm is a linear time string matching algorithm which is used to find all occurrences of a pattern P in a string S.", 
				 "description1",null, textProps);
		 lang.newText(new Offset(0, 30, "description1",
				 AnimalScript.DIRECTION_NW),
				 "Given a string S of length n, the algorithmn produces an Z array where each Z[i] is the length of the longest substring",
				 "description2", null, textProps);
		 lang.newText(new Offset(0, 30, "description2",
				 AnimalScript.DIRECTION_NW),
				 "starting from S[i] which is also a prefix of S. Note that the first entry of the Z array is meaningless since the",
				 "description3", null, textProps);
		 lang.newText(new Offset(0, 30, "description3",
				 AnimalScript.DIRECTION_NW),
				 "complete string is always a prefix of itself.",
				 "description4", null, textProps);
		 lang.newText(new Offset(0, 30, "description4",
				 AnimalScript.DIRECTION_NW),
				 "The main idea is to run the Z-algorithm with the concatenated string 'P$S' with pattern P and string S seperated by",
				 "description5", null, textProps);
		 lang.newText(new Offset(0, 30, "description5",
				 AnimalScript.DIRECTION_NW),
				 "a special character '$' which should not be present in P and S.",
				 "description6", null, textProps);
		 lang.newText(new Offset(0, 30, "description6",
				 AnimalScript.DIRECTION_NW),
				 "The Z values at any point equal to the pattern length indicate the presence of the pattern at that point.",
				 "description7", null, textProps);
		 
		 // STEP 2: Display first approach: First, create the concatenated string array as well as the empty z array
		 lang.nextStep("Initialisation");
		 lang.hideAllPrimitives();
		 header.show();
		 hRect.show();

		 // display next step: 
		 stepProps = new TextProperties();
		 stepProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 20));
		 stepProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
		 Text step1 = lang.newText(new Coordinates(10,100), "First, create the concatenated string array as well as the empty z array:", "STEP1", null, stepProps);

		 // Display code for search() method
		 initSrc = lang.newSourceCode(new Coordinates(10, 460), "startCode",null, sourceCode);

		 initSrc.addCodeLine("public static void search(String text, String pattern)    {", null, 0, null);  
		 initSrc.addCodeLine("",null,0,null);
		 initSrc.addCodeLine("String concat = pattern + '$' + text;", null, 2, null);
		 initSrc.addCodeLine("String[] sa = concat.split('');", null, 2, null);
		 initSrc.addCodeLine("int Z[] = new int[sa.length]",null,2,null);
		 initSrc.addCodeLine("",null,0,null);
		 initSrc.addCodeLine("getZarr(sa, Z);" ,null, 2, null);
		 initSrc.addCodeLine("",null,0,null);
		 initSrc.addCodeLine("for (int i=0; i<sa.length; i++)   {", null, 2, null);
		 initSrc.addCodeLine("if (Z[i]==pattern.length()) {", null,3,null);
		 initSrc.addCodeLine("System.out.println('Pattern found at index' + (i-pattern.length()-1));        ",null,4,null);
		 initSrc.addCodeLine("}",null,3,null);
		 initSrc.addCodeLine("}",null,2,null);
		 initSrc.addCodeLine("}",null,0, null);
		 //Surround code with rectangle
		 sRect = lang.newRect(new Offset(-5, -5, "startCode",
				 AnimalScript.DIRECTION_NW), new Offset(5, 5, "startCode", "SE"), "sRect", null, rectProps);


		 //STEP 3: Display Text and Pattern
		 lang.nextStep();
		 TextProperties varProps = new TextProperties();
		 varProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 20));
		 
		 Text varText = lang.newText(new Coordinates(50,275), "Text:", "TEXT", null, varProps);
		 Text textText = lang.newText(new Offset(100,0, "TEXT",AnimalScript.DIRECTION_NW), text, "textVar", null, textProps);
		 Text varPattern = lang.newText(new Offset(0, 115, "TEXT",AnimalScript.DIRECTION_NW),"Pattern:", "PATTERN", null, varProps);
		 Text patternText = lang.newText(new Offset(100,0, "PATTERN",AnimalScript.DIRECTION_NW), pattern, "patternVar", null, textProps);
		 
		 initSrc.highlight(2);
		 initSrc.highlight(3);
		 initSrc.highlight(4);

		 //STEP 4: Replace Text and Pattern with String array and Z array highlighting code 
		 lang.nextStep();
		 varText.hide();
		 textText.hide();
		 varPattern.hide();
		 patternText.hide();
		 
		 //Show String Array as well as empty Z array
		 lang.newText(new Coordinates(50,275), "SA:", "SA", null, varProps);
		 StringArray stringArray = lang.newStringArray(new Coordinates(100, 275), sa, "stringArray",null, stringArrayProperty);
		 lang.newText(new Coordinates(50,385), "Z:", "", null, varProps);
		 IntArray ZArray = lang.newIntArray(new Coordinates(100, 385), Z , "ZArray",null, ZArrayProperty);

		

		 
		  // STEP 5: Display 2nd approach: Perform the z algorithm
		 lang.nextStep("Z Algorithm");
		 initSrc.unhighlight(2);
		 initSrc.unhighlight(3);
		 initSrc.unhighlight(4);
		 step1.hide();
		 Text step2 = lang.newText(new Coordinates(10,100), "Perform the Z Algorithm:", "STEP2", null, stepProps);
         
		 // STEP 6: Display source code and counter
		 lang.nextStep();
		 initSrc.highlight(6);
		 // display counter
		 compCounter = lang.newText(new Coordinates(10, 150), "Number of comparisons: " + counter , "compCounter", null,textProps);
		 // Code for getZarr() method
		 Zsrc = lang.newSourceCode(new Coordinates(800,125), "ZsourceCode",null, sourceCode);
		 Zsrc.addCodeLine("private static void getZarr(String[] sa, int[] Z)  {        ", null, 0,null);  // 0
		 Zsrc.addCodeLine("",null,0,null);                                                                // 1
		 Zsrc.addCodeLine("int n = sa.length;", null, 2, null);											  // 2
		 Zsrc.addCodeLine("int L = 0, R = 0;", null, 2, null);											  // 3
		 Zsrc.addCodeLine("",null,0,null);																  // 4
		 Zsrc.addCodeLine("for (int i=1; i<n; i++)    {", null, 2, null);                                 // 5
		 Zsrc.addCodeLine("if (i>R) {", null, 3, null);                                                   // 6 
		 Zsrc.addCodeLine("L=R=i;", null, 4, null);                                                       // 7 
		 Zsrc.addCodeLine("while (R < n  &&  sa[R-L] == s[R])   {", null, 4, null);                       // 8 
		 Zsrc.addCodeLine("R++;", null, 5, null);                                                         // 9 
		 Zsrc.addCodeLine("}", null, 4, null);                                                            // 10 
		 Zsrc.addCodeLine("Z[i]=R-L;", null, 3, null);                                                    // 11
		 Zsrc.addCodeLine("R--;", null, 3, null);                                                         // 12
		 Zsrc.addCodeLine("}", null, 3, null);                                                            // 13
		 Zsrc.addCodeLine("",null,0,null);                                                                // 14 
		 Zsrc.addCodeLine("else {", null, 3, null);                                                       // 15
		 Zsrc.addCodeLine("int k = i-L;",null, 4,null);                                                   // 16
		 Zsrc.addCodeLine("if (Z[k] < R-i+1) {",null, 4,null);                                            // 17
		 Zsrc.addCodeLine("Z[i]=Z[k];",null, 5,null);                                                     // 18
		 Zsrc.addCodeLine("}",null, 4,null);                                                              // 19
		 Zsrc.addCodeLine("else {",null, 4,null);                                                         // 20
		 Zsrc.addCodeLine("L=i;",null, 5,null);	                                                          // 21 
		 Zsrc.addCodeLine("while (R < n && sa[R-L] == s[R])   {",null, 5,null);                           // 22 
		 Zsrc.addCodeLine("R++;",null, 6,null);	                                                          // 23
		 Zsrc.addCodeLine("}",null, 5,null);	                                                          // 24
		 Zsrc.addCodeLine("Z[i]=R-L;",null, 5,null);	                                                  // 25
		 Zsrc.addCodeLine("R--;",null, 5,null);	                                                          // 26
		 Zsrc.addCodeLine("}",null, 4,null);	                                                          // 27 
		 Zsrc.addCodeLine("}",null, 2,null);	                                                          // 28 
		 Zsrc.addCodeLine("}",null, 0,null);	                                                          // 29 

		 // surround code with rect
		 zRect =lang.newRect(new Offset(-5, -5, "ZsourceCode",
				 AnimalScript.DIRECTION_NW), new Offset(5, 5, "ZsourceCode", "SE"), "zRect", null, rectProps);

		 //STEP 7: Run the Z Algorithmn: Mark Line 2
		 lang.nextStep();
		 getZArray(stringArray, ZArray);

		 // NEXT STEP: Z array completed
		 lang.nextStep(); 
		 step2.hide();
		 Text step3 = lang.newText(new Coordinates(10,100), "Get the corresponding indexes from the Z Array:", "STEP3", null, stepProps);
		 //NEW STEP: hide src code and unhighlight getArrayZ() method
		 lang.nextStep();
		 initSrc.unhighlight(6);
		 Zsrc.hide();
		 zRect.hide();

		 //NEW STEP: Display output, mark next code line and create i marker
		 lang.nextStep("Output");
		 initSrc.highlight(8);
		 Text output = lang.newText(new Coordinates(750,480), "Output:", "OUTPUT", null, varProps);
		 ArrayMarkerProperties iProp = new ArrayMarkerProperties();
		 iProp.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
		 iProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		 ArrayMarker iMark = lang.newArrayMarker(ZArray, 0, "i" ,null, iProp);

		 int offset = 30;

		 for (int i = 0; i< ZArray.getLength(); i++) {
			 //NEW STEP: highlight for cond
			 lang.nextStep();
			 initSrc.highlight(8);
			 iMark.move(i, null, defaultTiming);

			 //NEW STEP: highlight if cond, highlight elem 
			 lang.nextStep();
			 initSrc.toggleHighlight(8,9);

			 if(ZArray.getData(i) == pattern.length()) {
				 ZArray.highlightCell(i, null,null);
				 //NEW STEP: If true, display output
				 lang.nextStep();
				 initSrc.toggleHighlight(9,10);
				 String oName = "OUTPUT" + i;

				 lang.newText(new Coordinates(750, 480 + offset) ,"Pattern found at index: " + (i-pattern.length()-1) +" of string text", oName,null, textProps);
				 offset = offset + 30;
				 //NEW STEP
				 lang.nextStep();
				 initSrc.unhighlight(10);
				 ZArray.unhighlightCell(i, null,null);

			 }
			 //NEW STEP
			 lang.nextStep();
			 initSrc.unhighlight(9);
		 }

		 lang.nextStep("Time Complexity");
		 //NEW STEP:  DISPLAY FINAL SUMMARY PAGE
		 lang.newText(new Coordinates(750,300), "As we can see the algorithmn runs in O(n+m) time with " + counter +" comparisons were n is the length of the text", "finalMessage1",null, textProps);
		 lang.newText(new Offset(0, 30, "finalMessage1",
				 AnimalScript.DIRECTION_NW),
				 "and m the length of the pattern. The naive algorithmn (with 2 nested loops) would have a time complexity of O(n^2).",
				 "finalMessage2", null, textProps);
    }
    
    /**
	   * This method performs the Z-Algorithm
	   * 
	   * @param sa
	   * 		  the StringArray consisting of P+"$"+S
	   * 
	   * @param Z
	   *         the Z Array which is initially filled with zeros 
	   */
    public void getZArray(StringArray sa, IntArray Z) {
    	
    	
    	
    	Zsrc.highlight(2);
    	// STEP 8: Mark line 3 and display marker L and R
    	lang.nextStep();
    	Zsrc.toggleHighlight(2,3);
    	// Create properties for the 3 pointers: L,R and i
    	ArrayMarkerProperties lP = new ArrayMarkerProperties();
    	lP.set(AnimationPropertiesKeys.LABEL_PROPERTY, "L");
    	lP.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    	ArrayMarker lM = lang.newArrayMarker(sa, 0, "l", null, lP);

    	ArrayMarkerProperties rP = new ArrayMarkerProperties();
    	rP.set(AnimationPropertiesKeys.LABEL_PROPERTY, "R");
    	rP.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    	ArrayMarker rM = lang.newArrayMarker(sa, 0, "r", null, rP);
    	ArrayMarkerProperties iP = new ArrayMarkerProperties();
    	
    	//Set Variables for this animation
    	
        
        
    	//STEP 9: Mark Line 5 and set i pointer
    	lang.nextStep();
    	Zsrc.toggleHighlight(3,5);
    	// Make i longer to avoid visual collision
    	iP.set(AnimationPropertiesKeys.LONG_MARKER_PROPERTY, true);
    	iP.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
    	iP.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    	ArrayMarker iM = lang.newArrayMarker(sa, 1, "i" ,null, iP);

    	int n= sa.getLength();   


    	for(int i = 1; i < n; i++) { 
    		// Highlight for loop when coming back from loop	
    		lang.nextStep();
    		Zsrc.highlight(5);

    		iM.move(i, null, defaultTiming);
    		// update variable
    		kVar.set("k", String.valueOf(i - lM.getPosition()));
    		
    		// if i>R nothing matches so we will calculate. 
    		// Z[i] using naive way.  

    		//NEW STEP: Highlight if condition line 6
    		lang.nextStep();
    		Zsrc.toggleHighlight(5,6);
    		if(i > rM.getPosition()){ 

    			//NEW STEP: If true, highlight line 7 and move L and R
    			lang.nextStep();
    			Zsrc.unhighlight(6);
    			Zsrc.highlight(7);
    			rM.move(i,null,defaultTiming);
    			lM.move(i,null, defaultTiming);
    			// update variables
    			rVar.set("R", String.valueOf(rM.getPosition()));
    			lVar.set("L", String.valueOf(lM.getPosition()));
    			kVar.set("k", String.valueOf(i - lM.getPosition()));
    			rSubL.set("R-L", String.valueOf(rM.getPosition()-lM.getPosition()));
    			//NEW STEP: Highlight while condition line 8 and highlight compared cell elements
    			lang.nextStep();
    			Zsrc.unhighlight(7);
    			Zsrc.highlight(8);
    			sa.highlightElem(rM.getPosition()-lM.getPosition(), null,null);
    			sa.highlightElem(rM.getPosition(),null,null);
    			//update counter and text
    			counter++;
    			compCounter.setText( "Number of compared character pairs: " + counter, null, null);
    			while( rM.getPosition() < n && sa.getData(rM.getPosition()-lM.getPosition()).contentEquals(sa.getData( rM.getPosition()))) {
    				//NEW STEP: If while true, mark cells green
    				lang.nextStep();
    				sa.unhighlightElem(rM.getPosition()-lM.getPosition(), null,null);
    				sa.unhighlightElem(rM.getPosition(),null,null);
    				sa.highlightCell(rM.getPosition()-lM.getPosition(), null,null);
    				sa.highlightCell(rM.getPosition(), null,null);

    				//NEW STEP increment R unhighlighting while 
    				lang.nextStep();
    				Zsrc.unhighlight(8);
    				Zsrc.highlight(9);
    				rM.increment(null, defaultTiming);
    				// update variable
    				rVar.set("R", String.valueOf(rM.getPosition()));
        			rSubL.set("R-L", String.valueOf(rM.getPosition()-lM.getPosition()));
        			
    				//NEW STEP: unhighlight R++;
    				lang.nextStep();
    				Zsrc.unhighlight(9);

    				//NEW STEP: Highlight while cond and mark next cell elems
    				lang.nextStep();
    				// highlight while cond
    				Zsrc.highlight(8);
    				// mark compared elems
    				sa.highlightElem(rM.getPosition()-lM.getPosition(), null,null);
    				sa.highlightElem(rM.getPosition(),null,null);
    				//update counter and text
    				counter++;
    				compCounter.setText( "Number of compared character pairs: " + counter, null, null);
    				if (!(rM.getPosition() < n && sa.getData(rM.getPosition()-lM.getPosition()).contentEquals(sa.getData(rM.getPosition())))){
    					break;
    				}

    			}
    			// NEW STEP: unhighlight  while condition
    			lang.nextStep();
    			Zsrc.unhighlight(8);

    			//NEW STEP: highlight line 11 and z array
    			lang.nextStep();
    			Z.highlightCell(i, null, null);
    			Zsrc.highlight(11);
    			lang.nextStep();
    			Z.put(i,  rM.getPosition()-lM.getPosition(),null,null);  
    			Z.highlightElem(i, null, null);

    			//NEW STEP: unhighlight marked cells
    			lang.nextStep();
    			sa.unhighlightCell(0, sa.getLength()-1, null, null);
    			sa.unhighlightElem(0, sa.getLength()-1, null, null);

    			// NEW STEP: decrement R and unighlight z elem
    			lang.nextStep();
    			//Z.unhighlightElem(i,null,null);
    			//Besser wenn weg?
    			Z.unhighlightCell(i,null,null);
    			Zsrc.unhighlight(11);
    			Zsrc.highlight(12);
    			//rM.decrement(null, defaultTiming);  
    			// update variable
    			rVar.set("R", String.valueOf(rM.getPosition()));
    			rSubL.set("R-L", String.valueOf(rM.getPosition()-lM.getPosition()));
    			
    			//NEW STEP: unhighlight line 12 after decrement and jump back to for loop
    			lang.nextStep();
    			Zsrc.unhighlight(12);
    		} 

    		else{ 
    			// update variable
    			kVar.set("k", String.valueOf(i - lM.getPosition()));
    			
    			//NEW STEP: unhighlight if and highlight first line of else
    			lang.nextStep();
    			Zsrc.unhighlight(6);
    			Zsrc.highlight(16);
    			int k = i - lM.getPosition(); 
    			//NEW STEP: unhighlight 16 and highlight if condition line 17
    			lang.nextStep();
    			Zsrc.unhighlight(16);
    			Zsrc.highlight(17);
    			if(Z.getData(k) <  rM.getPosition() - i + 1) {

    				// NEW STEP: if true display message
    				lang.nextStep();
    				// DISPLAY TEXT
    				Text info = lang.newText(new Coordinates(270,30), "At this point we know that the string in the intervall [L,R] is the same as the pattern.", "info1", null, textProps);
    				Text info2 =lang.newText(new Offset(0, 30, "info1",
    						AnimalScript.DIRECTION_NW),
    						"We can use this knowledge to determine the z value: If the Z value of the corresponding",
    						"info2", null, textProps);
    				Text info3 =lang.newText(new Offset(0, 30, "info2",
    						AnimalScript.DIRECTION_NW),
    						"i th character doesn't exceed the remaining intervall, just copy the z value (no character comparison is needed)",
    						"info3", null, textProps);
    				//NEW STEP: unhighlight if and highlight line 18 and Z cell
    				lang.nextStep();
    				Zsrc.toggleHighlight(17,18);
    				Z.highlightCell(i, null, null);
    				Z.highlightCell(k, null, null);

    				//NEW STEP:  Put elem in z array
    				lang.nextStep();
    				Z.put(i, Z.getData(k), null,null); 
    				Z.highlightElem(i, null, null);

    				//NEW STEP unhighlight 18 and go back to for & hide text
    				lang.nextStep();
    				Zsrc.unhighlight(18);
    				Z.unhighlightCell(i, null, null);
    				Z.unhighlightCell(k, null, null);
    				info.hide();
    				info2.hide();
    				info3.hide();
    			}

    			else{ 
    				//NEW STEP: unhighlight if cond line 17 and highlight first esle line on 21
    				lang.nextStep();
    				Zsrc.unhighlight(17);
    				Zsrc.highlight(21);
    				// else start from R and check manually 
    				lM.move(i, null, null); 
    				//updates variables
    				//lVar.set("L", String.valueOf(i));
        			//kVar.set("k", String.valueOf(i - lM.getPosition()));
        			//zOfK.set("Z[k]", String.valueOf(Z.getData(i - lM.getPosition())));
        			//rSubL.set("R-L", String.valueOf(rM.getPosition()-lM.getPosition()));
    				//NEW STEP: unhighlight line 21 and highlight while cond as well as highlighting compared cell elements
    				lang.nextStep();
    				Zsrc.unhighlight(21);
    				Zsrc.highlight(22);
    				sa.highlightElem(rM.getPosition()-lM.getPosition(),null,null); 
    				sa.highlightElem(rM.getPosition(),null,null);
    				//update counter and text
    				counter++;
    				compCounter.setText( "Number of compared character pairs: " + counter, null, null);
    				while( rM.getPosition() < n && sa.getData( rM.getPosition()-lM.getPosition()).contentEquals(sa.getData( rM.getPosition()))) {
    					//NEW STEP: If while true, mark cells green
    					lang.nextStep();
    					sa.unhighlightElem(rM.getPosition()-lM.getPosition(), null,null);
    					sa.unhighlightElem(rM.getPosition(),null,null);
    					sa.highlightCell(rM.getPosition()-lM.getPosition(), null,null);
    					sa.highlightCell(rM.getPosition(), null,null);

    					//NEW STEP increment R unhighlighting while 
    					lang.nextStep();
    					Zsrc.unhighlight(22);
    					Zsrc.highlight(23);
    					rM.increment(null, defaultTiming);
    					// update variable
    	    			rSubL.set("R-L", String.valueOf(rM.getPosition()-lM.getPosition()));
    	    			rVar.set("R", String.valueOf(rM.getPosition()));
    	    			
    					//NEW STEP: unhighlight R++;
    					lang.nextStep();
    					Zsrc.unhighlight(23);

    					//NEW STEP: Highlight while cond and mark next cell elems
    					lang.nextStep();
    					// highlight while cond
    					Zsrc.highlight(22);
    					// mark compared elems
    					if ((rM.getPosition()==(n-1))) {
    						sa.highlightElem(rM.getPosition()-lM.getPosition(), null,null);
    					}
    					sa.highlightElem(rM.getPosition(),null,null);
    					//update counter and text
    					counter++;
    					compCounter.setText( "Number of compared character pairs: " + counter, null, null);
    					if (!(rM.getPosition() < n && sa.getData(rM.getPosition()-lM.getPosition()).contentEquals(sa.getData( rM.getPosition())))){
    						break;
    					}

    				}
    				// NEW STEP: unhighlight  while condition
    				lang.nextStep();
    				Zsrc.unhighlight(22);

    				//NEW STEP: highlight line 25 and z array
    				lang.nextStep();
    				Z.highlightCell(i, null, null);
    				Zsrc.highlight(25);
    				lang.nextStep();
    				Z.put(i,  rM.getPosition()-lM.getPosition(),null,null);  
    				Z.highlightElem(i, null, null);

    				//NEW STEP: unhighlight marked cells
    				lang.nextStep();
    				sa.unhighlightCell(0, sa.getLength()-1, null, null);
    				sa.unhighlightElem(0, sa.getLength()-1, null, null);

    				// NEW STEP: decrement R and unighlight z elem
    				lang.nextStep();
    				//Besser wenn weg?
    				//Z.unhighlightElem(i,null,null);
    				Z.unhighlightCell(i,null,null);
    				Zsrc.unhighlight(25);
    				Zsrc.highlight(26);
    				rM.decrement(null, defaultTiming);  
    				// update variable
        			rSubL.set("R-L", String.valueOf(rM.getPosition()-lM.getPosition()));
        			rVar.set("R", String.valueOf(rM.getPosition()));
        			
    				//NEW STEP: unhighlight line 26 and jump back to for loop
    				lang.nextStep();
    				Zsrc.unhighlight(26);
    			} 
    		}

    	} 	
    	//NEXT STEP: End og z algorithmn, hide the markers
    	lang.nextStep();
    	iM.hide();
    	lM.hide();
    	rM.hide();
    }
    
    
    
    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
    	
    	String pattern;
    	String text;
    	
    	if (finish==1) {
    	lang.setStepMode(true);
        ZArrayProperty = (ArrayProperties)props.getPropertiesByName("ZArrayProperty");
        sourceCode = (SourceCodeProperties)props.getPropertiesByName("sourceCode");
        Pattern = (String)primitives.get("Pattern");
        stringArrayProperty = (ArrayProperties)props.getPropertiesByName("stringArrayProperty");
        Text = (String)primitives.get("Text");
        
        pattern=Pattern;
        text=Text;
        String[] p = pattern.split("\\r?\\n");
        pattern=p[0];
        String[] t = text.split("\\r?\\n");
        text=t[0];
        
        
        // Convert String and Text: String retrieved have the string "/n" appended at the end
        if (Pattern.substring(0, Pattern.length()-2).equals("aba") && Text.substring(0, Text.length()-2).equals("abacababa")) {
        pattern = Pattern.substring(0, Pattern.length()-2);
        text = Text.substring(0, Text.length()-2);
        }
        
       
		//Configure arrayproperties
        ZArrayProperty.set(AnimationPropertiesKeys.FONT_PROPERTY,new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        stringArrayProperty.set(AnimationPropertiesKeys.FONT_PROPERTY,new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        
        String concat = pattern + "$" + text;
        // Create String array
        String[] sa = concat.split("");
       
        //Set variables for this animation: will be updated through algorithmn.
        // Note: Trying to set the role doesn't seem to work 
       
        lVar = lang.newVariables();
        lVar.declare("int", "L", "0");
        
        rVar = lang.newVariables();
        rVar.declare("int", "R", "0");
        
        rSubL = lang.newVariables();
        rSubL.declare("int", "R-L", "0");
        
        kVar = lang.newVariables();
        kVar.declare("int", "k", "0");
        
        /**
         * This somehow results in a error
         * 
         * tempVar = lang.newVariables();
         * tempVar.declare("Int",  "R" + "-i+"  + "1", "0");
         */
       
        
        //start animation
        search(text,pattern,sa);
      	}
        
        finish++;
    	return lang.toString();
    }

    public String getName() {
        return "Z Algorithm";
    }

    public String getAlgorithmName() {
        return "Z-Algorithm";
    }

    public String getAnimationAuthor() {
        return "Kai Tanaka, Tomoki Tokuyama";
    }

    public String getDescription(){
        return "The Z-algorithm is a linear time string matching algorithm which is used to find all occurrences of a "
 +"\n"
 +"pattern P in a string S. Given a string S of length n, the algorithmn produces an Z array where each"
 +"\n"
 +"Z[i] is the length of the longest substring starting from S[i] which is also a prefix of S. "
 +"\n"
 +"Note that the first entry of the Z array is meaningless since the complete string is always a prefix of itself."
 +"\n"
 +"\n"
 +"The main idea is to run the Z-algorithm with the concatenated string 'P$S' with pattern P and string S "
 +"\n"
 +"seperated by a special character '$' which should not be present in P and S."
 +"\n"
 +"The Z values at any point equal to the pattern length indicate the presence of the pattern at that point.";
    }

    public String getCodeExample(){
        return "public static void search(String text, String pattern)  { "
 +"\n"
 +"        String concat = pattern + \"$\" + text; "
 +"\n"
 +"        String[] sa = concat.split(\"\");"
 +"\n"
 +"        int Z[] = new int[sa.length]; "
 +"\n"
 +"        getZarr(concat, Z); "
 +"\n"
 +"        for(int i = 0; i < sa.length; ++i){ "
 +"\n"
 +"            if(Z[i] == pattern.length()){ "
 +"\n"
 +"                System.out.println(\"Pattern found at index \" + (i - pattern.length() - 1)); "
 +"\n"
 +"            } "
 +"\n"
 +"        } "
 +"\n"
 +"    } "
 +"\n"
 +"\n"
 +"private static void getZarr(String[] sa, int[] Z) { "
 +"\n"
 +"  "
 +"\n"
 +"        int n = sa.length; "
 +"\n"
 +"        int L = 0, R = 0; "
 +"\n"
 +"  "
 +"\n"
 +"        for(int i = 1; i < n; ++i)  {"
 +"\n"
 +"            if(i > R){ "
 +"\n"
 +"                L = R = i; "
 +"\n"
 +"                while(R < n && sa[R - L] == sa[R])  {"
 +"\n"
 +"                    R++; "
 +"\n"
 +"                 } "
 +"\n"
 +"                Z[i] = R - L; "
 +"\n"
 +"                R--; "
 +"\n"
 +"            } "
 +"\n"
 +"            else{ "
 +"\n"
 +"                  int k = i - L; "
 +"\n"
 +"\n"
 +"                 if(Z[k] < R - i + 1) {"
 +"\n"
 +"                    Z[i] = Z[k]; "
 +"\n"
 +"                  } "
 +"\n"
 +"\n"
 +"                else{ "
 +"\n"
 +"                    L = i; "
 +"\n"
 +"                   while(R < n && sa[R - L] == sa[R])  {"
 +"\n"
 +"                        R++; "
 +"\n"
 +"                     }"
 +"\n"
 +"                    Z[i] = R - L; "
 +"\n"
 +"                    R--; "
 +"\n"
 +"                } "
 +"\n"
 +"            } "
 +"\n"
 +"        } ";
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

	@Override
	public boolean validateInput(AnimationPropertiesContainer arg0, Hashtable<String, Object> arg1)
			throws IllegalArgumentException {
		try {
		// Get primitives
        String valPattern = (String)arg1.get("Pattern");
        String valText = (String)arg1.get("Text");
        valPattern = valPattern.substring(0, valPattern.length()-2);
        valText = valText.substring(0, valText.length()-2);
        
        /**
         *  Validate primitives:
         *  pattern and text doesn't contain "$"
         */
        String[] pa = valPattern.split("");
        String[] ta = valText.split("");
        Set<String> charSet = new HashSet<>();
        for (String s : pa) {
            charSet.add(s);
        }
        for (String s : ta) {
        	charSet.add(s);
        }
        
        if(charSet.contains("$")) {
        	return false;
        }
		}
		
		catch(IllegalArgumentException e) {
			e.printStackTrace();
		}
		return true;
	
	}

	/**
	public static void main(String[] args) {
	    Generator generator = new ZAlgorithmGenerator(); // Generator erzeugen
	    Animal.startGeneratorWindow(generator); // Animal mit Generator starten
	}
	*/
	
}
