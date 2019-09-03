/*
 * LamportBakeryAlgo.java
 * Jonathan Speth, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */


package generators.misc;


import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Random;
import java.util.Stack;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.IntArray;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.SquareProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.FillInBlanksQuestionModel;
import translator.Translator;

public class LamportBakeryAlgo implements ValidatingGenerator {
	
	/**
	* The language object used for creating output
	*/
    private Language lang;
    
    /**
     * The translator object
     */
    private Translator translator;
    
    /**
     * The locale object used to determine which language to use
     */
    private Locale locale;
    
    /**
     * The number of processes
     */
    private int numberOfThreads;
    
    /**
     * Globally defined array properties
     */
    private ArrayProperties ArrayProperties;
    
    /**
     * The header text including the headline
     */
    private Text headerText;

    /**
     * The rectangle around the headline
     */
    private Rect hRect;

    /**
     * Globally defined text properties
     */
    private TextProperties textProps;

    /**
     * The source code shown in the animation
     */
    private SourceCode src;

    /**
     * Globally defined source code properties
     */
    private SourceCodeProperties SourceCodeProperties;
    
    private String[] entering;
    private int[] number;
    
    
    //simulated order of threads arriving, for purposes of this animation every thread executes its function exactly once
    private Stack<Integer> order;
    
    
    private IntArray numberArray;
    //private TwoValueView numberView;
    private StringArray enteringArray;
    //private TwoValueView enteringView;
  
    
    private Random rand = new Random();
    
    public void init(){
    	lang = new AnimalScript("Lamport's bakery algorithm", "Jonathan Speth",
        	800, 600);
        lang.setStepMode(true);
    }

    public LamportBakeryAlgo(Locale locale) {
    	this.locale = locale;
    	translator = new Translator("resources/LamportBakeryAlgo", locale);
    	
    	lang = new AnimalScript("Lamport's bakery algorithm", "Jonathan Speth",
    		800, 600);
    	lang.setStepMode(true);
    }
    
    /**
    public static void main(String[] args) {        
       	Generator generator = new LamportBakeryAlgo();
    	Animal.startGeneratorWindow(generator);
    }
    **/
   
    // Initializes the animation. Shows a start page with a description. 
    // Then, shows the the source code and calls the algorithm.
    public void start() {
    	numberArray.hide();
    	enteringArray.hide();

        // show the header surrounded by a rectangle
        TextProperties headerProps = new TextProperties();
        headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
            Font.SANS_SERIF, Font.BOLD, 24));
        headerText = lang.newText(new Coordinates(20, 30), "Lamport's Bakery Algorithm",
            "header", null, headerProps);
        RectProperties rectProps = new RectProperties();
        rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
        rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
        hRect = lang.newRect(new Offset(-5, -5, "header",
            AnimalScript.DIRECTION_NW), new Offset(5, 5, "header", "SE"), "hRect",
            null, rectProps);

        // setup the start page with the description
        lang.nextStep();
        
        lang.newText(new Coordinates(10, 100),
            translator.translateMessage("description1"), "description1", null, textProps);
        lang.newText(new Offset(0, 25, "description1",
            AnimalScript.DIRECTION_NW),
            translator.translateMessage("description2"), "description2", null, textProps);
        lang.newText(new Offset(0, 25, "description2",
            AnimalScript.DIRECTION_NW),
            translator.translateMessage("description3"), "description3", null, textProps);
        lang.newText(new Offset(0, 25, "description3",
            AnimalScript.DIRECTION_NW),
            translator.translateMessage("description4"), "description4", null, textProps);
        lang.newText(new Offset(0, 25, "description4",
            AnimalScript.DIRECTION_NW),
            translator.translateMessage("description5"), "description5", null, textProps);
        lang.newText(new Offset(0, 25, "description5",
            AnimalScript.DIRECTION_NW),
            translator.translateMessage("description6"), "description6", null, textProps);
        lang.newText(new Offset(0, 25, "description6",
            AnimalScript.DIRECTION_NW),
            translator.translateMessage("description7"), "description7", null, textProps);

        lang.nextStep();
        
        lang.newText(new Offset(0, 50, "description7", AnimalScript.DIRECTION_NW),
            translator.translateMessage("algo11"), "algo11", null, textProps);
        lang.newText(new Offset(0, 25, "algo11", AnimalScript.DIRECTION_NW),
            translator.translateMessage("algo12"), "algo12", null, textProps);
        lang.newText(new Offset(0, 25, "algo12", AnimalScript.DIRECTION_NW),
            translator.translateMessage("algo13"), "algo13", null, textProps);
        lang.newText(new Offset(0, 25, "algo13", AnimalScript.DIRECTION_NW),
            translator.translateMessage("algo14"), "algo14", null, textProps);
        lang.newText(new Offset(0, 25, "algo14", AnimalScript.DIRECTION_NW),
            translator.translateMessage("algo15"), "algo15", null, textProps);
        
        lang.nextStep();

        lang.hideAllPrimitivesExcept(new ArrayList<Primitive>(Arrays.asList(headerText, hRect)));
        
        src = lang.newSourceCode(new Coordinates(10, 100), "sourceCode", null, SourceCodeProperties);
        src.addCodeLine("  // declaration and initial values of global variables", null, 0, null); // 0
        src.addCodeLine("Entering: array [1..NUM_THREADS] of bool = {false};", null, 0, null); // 1
        src.addCodeLine("Number: array [1..NUM_THREADS] of integer = {0};", null, 0, null); // 2
        src.addCodeLine("lock(integer i) {", null, 0, null); // 3
        src.addCodeLine("Entering[i] = true;", null, 1, null); // 4
        src.addCodeLine("Number[i] = 1 + max(Number[1], ..., Number[NUM_THREADS]);", null, 1, null); // 5
        src.addCodeLine("Entering[i] = false;", null, 1, null); // 6
        src.addCodeLine("for (integer j = 1; j <= NUM_THREADS; j++) {", null, 1, null); // 7
        src.addCodeLine("// Wait until thread j receives its number:", null, 2, null); // 8
        src.addCodeLine("while (Entering[j]) { /* nothing */ }", null, 2, null); // 9
        src.addCodeLine("// Wait until all threads with smaller numbers or with the same", null, 2, null); // 10
        src.addCodeLine("// number, but with higher priority, finish their work:", null, 2, null); // 11
        src.addCodeLine("while ((Number[j] != 0) && ((Number[j], j) < (Number[i], i))) { /* nothing */ }", null, 2, null); // 12
        src.addCodeLine("}", null, 1, null); // 13
        src.addCodeLine("}", null, 0, null); // 14
        src.addCodeLine("", null, 0, null); // 15
        src.addCodeLine("unlock(integer i) {", null, 0, null); // 16
        src.addCodeLine("Number[i] = 0;", null, 1, null); // 17
        src.addCodeLine("}", null, 0, null); // 18
        src.addCodeLine("", null, 0, null); // 19
        src.addCodeLine("Thread(integer i) {", null, 0, null); // 20
        src.addCodeLine("while (true) {", null, 1, null); // 21
        src.addCodeLine("lock(i);", null, 2, null); // 22
        src.addCodeLine("// The critical section goes here...", null, 2, null); // 23
        src.addCodeLine("unlock(i);", null, 2, null); // 24
        src.addCodeLine("// non-critical section...", null, 2, null); // 25
        src.addCodeLine("}", null, 1, null); // 26
        src.addCodeLine("}", null, 0, null); // 27
        
        lang.nextStep();
        
        lamport();
    }
    
    // setup for the main algorithm
    private void lamport() {
    	
    	src.highlight(20);
    	src.highlight(21);
    	src.highlight(22);
    	src.highlight(23);
    	src.highlight(24);
    	src.highlight(25);
    	src.highlight(26);
    	src.highlight(27);
    	
    	lang.newText(new Offset(0, -100, "sourceCode", AnimalScript.DIRECTION_SE),
    			translator.translateMessage("explanation1"), "explanation1", null, textProps);
    	lang.newText(new Offset(0, 25, "explanation1", AnimalScript.DIRECTION_NW),
    			translator.translateMessage("explanation2"), "explanation2", null, textProps);
    	lang.newText(new Offset(0, 25, "explanation2", AnimalScript.DIRECTION_NW),
    			translator.translateMessage("explanation3"), "explanation3", null, textProps);
    	lang.newText(new Offset(0, 25, "explanation3", AnimalScript.DIRECTION_NW),
    			translator.translateMessage("explanation4"), "explanation4", null, textProps);
    	
    	lang.nextStep();
    
    	lang.hideAllPrimitivesExcept(new ArrayList<Primitive>(Arrays.asList(headerText, hRect, src)));
    	
    	src.unhighlight(20);
    	src.unhighlight(21);
    	src.unhighlight(22);
    	src.unhighlight(23);
    	src.unhighlight(24);
    	src.unhighlight(25);
    	src.unhighlight(26);
    	src.unhighlight(27);
    	
    	src.highlight(7);
    	src.highlight(8);
    	src.highlight(9);
    	src.highlight(10);
    	src.highlight(11);
    	src.highlight(12);
    	src.highlight(13);
    	
    	lang.newText(new Offset(0, 50, "sourceCode", AnimalScript.DIRECTION_NE),
    			translator.translateMessage("explanation5"), "explanation5", null, textProps);
    	lang.newText(new Offset(0, 25, "explanation5", AnimalScript.DIRECTION_NW),
    			translator.translateMessage("explanation6"), "explanation6", null, textProps);
    	lang.newText(new Offset(0, 25, "explanation6", AnimalScript.DIRECTION_NW),
    			translator.translateMessage("explanation7"), "explanation7", null, textProps);
       	lang.newText(new Offset(0, 25, "explanation7", AnimalScript.DIRECTION_NW),
    			translator.translateMessage("explanation8"), "explanation8", null, textProps);
    	
    	lang.nextStep();
    	
    	lang.hideAllPrimitivesExcept(new ArrayList<Primitive>(Arrays.asList(headerText, hRect, src)));

    	src.unhighlight(7);
    	src.unhighlight(8);
    	src.unhighlight(9);
    	src.unhighlight(13);
    	
    	lang.newText(new Offset(0, 100, "sourceCode", AnimalScript.DIRECTION_NE),
    			translator.translateMessage("explanation9"), "explanation9", null, textProps);
    	lang.newText(new Offset(0, 25, "explanation9", AnimalScript.DIRECTION_NW),
    			translator.translateMessage("explanation10"), "explanation10", null, textProps);
    	lang.newText(new Offset(0, 25, "explanation10", AnimalScript.DIRECTION_NW),
    			translator.translateMessage("explanation11"), "explanation11", null, textProps);
       	lang.newText(new Offset(0, 25, "explanation11", AnimalScript.DIRECTION_NW),
    			translator.translateMessage("explanation12"), "explanation12", null, textProps);
       	
    	lang.nextStep();

    	lang.hideAllPrimitivesExcept(new ArrayList<Primitive>(Arrays.asList(headerText, hRect, src)));
    	
    	src.unhighlight(10);
    	src.unhighlight(11);
    	src.unhighlight(12);

  	
    	numberArray.show();
    	//numberView.show();
    	numberArray.setFillColor(0, numberOfThreads, Color.CYAN, null, null);
        lang.newText(new Offset(-75, 0, "entering", AnimalScript.DIRECTION_NW), "Entering", "enteringName", null, textProps);
    	enteringArray.show();
    	//enteringView.show();
        lang.newText(new Offset(-75, 0, "number", AnimalScript.DIRECTION_NW), "Number", "numberName", null, textProps);

        lang.newText(new Offset(25, 25, "enteringName", AnimalScript.DIRECTION_NW), "i = ", "iDisplayEntering", null, textProps);
        lang.newText(new Offset(25, 25, "numberName", AnimalScript.DIRECTION_NW), "i = ", "iDisplayNumber", null, textProps);
        
        lang.newText(new Offset(-25, 250, "iDisplayNumber", AnimalScript.DIRECTION_SW), "Legend", "legend", null, textProps);
        
        SquareProperties legendProperties = new SquareProperties();
        legendProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        
        legendProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.CYAN);
        lang.newSquare(new Offset(25, 15, "legend", AnimalScript.DIRECTION_SW), 15, "legendSquare1", null, legendProperties);
        lang.newText(new Offset(25, -5, "legendSquare1", AnimalScript.DIRECTION_NE), translator.translateMessage("legend1"), "legendDesc1", null, textProps);
        
        legendProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
        lang.newSquare(new Offset(25, 40, "legend", AnimalScript.DIRECTION_SW), 15, "legendSquare2", null, legendProperties);
        lang.newText(new Offset(25, -5, "legendSquare2", AnimalScript.DIRECTION_NE), translator.translateMessage("legend2"), "legendDesc2", null, textProps);
        
        legendProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GREEN);
        lang.newSquare(new Offset(25, 65, "legend", AnimalScript.DIRECTION_SW), 15, "legendSquare3", null, legendProperties);
        lang.newText(new Offset(25, -5, "legendSquare3", AnimalScript.DIRECTION_NE), translator.translateMessage("legend3"), "legendDesc3", null, textProps);
        
        
        src.highlight(1);
        src.highlight(2);

    	lang.nextStep(translator.translateMessage("explanationend"));
    	
    	src.unhighlight(1);
    	src.unhighlight(2);
    	
    	// have every thread enter once 
    	while(order.size() >= 1) {
    		lock();
    	}
       	
       	for(int i = 0; i < numberOfThreads; i++) {
       		int index = findLowestNonZero();
       		if(index >= 0) {
       			unlock(index);
       		}
       	}
       	
       	
       	lang.newText(new Offset(0, 100, "numberName", AnimalScript.DIRECTION_NW),
    			translator.translateMessage("ending1"), "ending1", null, textProps);
       	lang.newText(new Offset(0, 25, "ending1", AnimalScript.DIRECTION_NW),
    			translator.translateMessage("ending2"), "ending2", null, textProps);
       	lang.newText(new Offset(0, 50, "ending2", AnimalScript.DIRECTION_NW),
    			translator.translateMessage("ending3"), "ending3", null, textProps);
       	lang.newText(new Offset(0, 25, "ending3", AnimalScript.DIRECTION_NW),
    			translator.translateMessage("ending4"), "ending4", null, textProps);
       	
       	lang.nextStep(translator.translateMessage("conclusion"));
       	
    }
    
    // main logic for the algorithm
	private void lock() {
		
		// new thread enters
		int threadID = enterNewThread();
		
		// assign number to new thread
		int max = Arrays.stream(number).max().orElseThrow(() -> new IllegalArgumentException("Array is empty"));
		if(rand.nextBoolean()) {
			nextNumberQuestion(threadID, max);
			drawNumber(threadID, max);
			max++;
			
			lang.nextStep();
			
			numberArray.unhighlightElem(threadID, null, null);
			src.unhighlight(5);
			
			finishEntering(threadID);
			
			lang.nextStep();
			
			enteringArray.unhighlightElem(threadID, null, null);
			src.unhighlight(6);
		}
		
		// simulate other threads entering sometimes
		if((order.size() >= 1) ) {
			// rolls dice between 0 and 1 + order.size / 4, since the right boundary is exclusive
			int randy = rand.nextInt(2 + (order.size() / 4));
			for(int r = 0; r < randy; r++) {
				int newThread = enterNewThread();
				
				if(rand.nextBoolean()) {
					drawNumber(newThread, max);
					max++;
					
					lang.nextStep();
					
					numberArray.unhighlightElem(newThread, null, null);
					src.unhighlight(5);
					
					finishEntering(newThread);
					
					lang.nextStep();
					
					enteringArray.unhighlightElem(0, numberOfThreads - 1, null, null);
					src.unhighlight(6);
				}
			}
		}

		
		max = Arrays.stream(number).max().orElseThrow(() -> new IllegalArgumentException("Array is empty"));
		
		for (int otherThreadID = 0; otherThreadID < numberOfThreads; otherThreadID++) {
			// Wait until thread  receives its number:
			if (entering[otherThreadID] == "T") {
				// simulate thread receiving its number
				// both threads receive the same number
				drawNumber(otherThreadID, max);
				
				finishEntering(otherThreadID);

			}
		}
		
		// When merged with loop above sometimes the wrong thread gets unlocked, haven't found other solution
		for (int otherThreadID = 0; otherThreadID < numberOfThreads; otherThreadID++) {	
			// Wait until all threads with smaller numbers or with the same
			// number, but with higher priority, finish their work:
			if (number[otherThreadID] != 0 && ( number[threadID] > number[otherThreadID]  || (number[threadID] == number[otherThreadID] && threadID > otherThreadID))) {
				// set i to the thread with the next highest priority
				threadID = otherThreadID;
			}	
		}
		
		lang.nextStep();
		
		enteringArray.unhighlightElem(0, numberOfThreads - 1, null, null);
		numberArray.unhighlightElem(0, numberOfThreads - 1, null, null);
		src.unhighlight(5);
		src.unhighlight(6);
		
		src.highlight(9);
		enteringArray.highlightElem(0, numberOfThreads - 1, null, null);
		
		lang.nextStep();
		
		src.unhighlight(9);
		enteringArray.unhighlightElem(0, numberOfThreads - 1, null, null);
		src.highlight(12);
		
		// steadily unlock threads to keep the pace
		nextUnlockQuestion(threadID);
		
		src.unhighlight(12);
		
		unlock(threadID);
	}
 
	private void unlock(int i) {
		src.highlight(23);
		numberArray.highlightElem(i, null, null);
		
		lang.nextStep("Thread " + i + translator.translateMessage("unlock"));
		
		src.unhighlight(23);
		numberArray.unhighlightElem(i, null, null);
		
		number[i] = 0;
		numberArray.put(i, 0, null, null);
		numberArray.highlightCell(i, null, null);
		src.highlight(17);
		src.highlight(24);
		
		lang.nextStep();
		
		src.unhighlight(17);
		src.unhighlight(24);
	}
	
	private int enterNewThread() {
		int nextThread = order.pop();
		entering[nextThread] = "T";	
		enteringArray.put(nextThread, "T", null, null);
		enteringArray.highlightElem(nextThread, null, null);
		src.highlight(4);
		numberArray.setFillColor(nextThread, Color.YELLOW, null, null);
		
		lang.nextStep();
		
		enteringArray.unhighlightElem(nextThread, null, null);
		src.unhighlight(4);
		
		return nextThread;
	}
	
	private void drawNumber(int thread, int max) {
		number[thread] = 1 + max;
		numberArray.put(thread, 1 + max, null, null);
		numberArray.highlightElem(thread, null, null);
		src.highlight(5);
	}
	
	private void finishEntering(int thread) {
		entering[thread] = "F";
		enteringArray.put(thread, "F", null, null);
		enteringArray.highlightElem(thread, null, null);
		src.highlight(6);
	}
	
	private int findLowestNonZero() {
        int index = -1;
        int min = Integer.MAX_VALUE;
        for (int i=0; i<number.length; i++){

            if (number[i] < min && number[i] != 0){
                min = number[i];
                index = i;
        	}
    	}
        return index;
	}
	
	public void nextUnlockQuestion(int threadID) {
		FillInBlanksQuestionModel nextUnlock = new FillInBlanksQuestionModel("unlock"+ Integer.toString(threadID));
		nextUnlock.setPrompt(translator.translateMessage("unlockquestion"));
		nextUnlock.addAnswer("unlock"+ Integer.toString(threadID), Integer.toString(threadID), 1, "Thread " + Integer.toString(threadID) + " " + translator.translateMessage("unlockanswer"));
		lang.addFIBQuestion(nextUnlock);
		
		lang.nextStep();
	}
	
	public void nextNumberQuestion(int threadID, int max) {
		FillInBlanksQuestionModel nextNumber = new FillInBlanksQuestionModel("number"+ Integer.toString(threadID));
		nextNumber.setPrompt("Thread " + Integer.toString(threadID) + " " + translator.translateMessage("nextnumberquestion1") + " thread " + Integer.toString(threadID) + "'s " + translator.translateMessage("nextnumberquestion2"));
		nextNumber.addAnswer("number"+ Integer.toString(threadID), Integer.toString(1 + max), 1, "Thread " + Integer.toString(threadID) + " " + translator.translateMessage("nextnumberanswer") + " " + (1 + max));
		lang.addFIBQuestion(nextNumber);
		
		lang.nextStep();
	}
	
    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
    	lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    	
        numberOfThreads = (Integer)primitives.get("NumberOfThreads");
        ArrayProperties = (ArrayProperties)props.getPropertiesByName("ArrayProperties");
        SourceCodeProperties = (SourceCodeProperties)props.getPropertiesByName("SourceCodeProperties");
        
        textProps = new TextProperties();
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
            Font.SANS_SERIF, Font.PLAIN, 16));
        
        // Entering array initialized with "f" for false
    	entering =  new String[numberOfThreads];
    	for(int e = 0; e < numberOfThreads; e++) {
    		entering[e] = "F";
    	}
    	// Number Array
    	number = new int[numberOfThreads];
    	
    	// generate order of entering threads and shuffle
    	order = new Stack<Integer>();
    	for(int k = 0; k < numberOfThreads; k++) {
    		order.push(k);
    	}
    	Collections.shuffle(order);
    	
    	enteringArray = lang.newStringArray(new Coordinates(600, 100), entering, "entering", null, ArrayProperties);
    	
    	/**
    	TwoValueCounter counterEntering = lang.newCounter(enteringArray);
    	CounterProperties cpEntering = new CounterProperties(); 
    	cpEntering.set(AnimationPropertiesKeys.FILLED_PROPERTY, true); 
    	cpEntering.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);
    	enteringView = lang.newCounterView(counterEntering, new Coordinates(800, 200), cpEntering, true, true);
    	enteringView.hide();
		**/
    	
    	numberArray = lang.newIntArray(new Offset(0, 50, "entering", AnimalScript.DIRECTION_NW), number, "number", null, ArrayProperties);
    	/**
    	TwoValueCounter counterNumber = lang.newCounter(numberArray);
    	CounterProperties cpNumber = new CounterProperties(); 
    	cpNumber.set(AnimationPropertiesKeys.FILLED_PROPERTY, true); 
    	cpNumber.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);
    	numberView = lang.newCounterView(counterNumber, new Coordinates(800, 100), cpNumber, true, true);
    	numberView.hide();
        **/
    	
        start();
        
        lang.finalizeGeneration();
        return lang.toString();
    }

    
    
    public String getName() {
        return "Lamport's Bakery Algorithm [" + locale.getLanguage().toUpperCase() + "]";
    }

    public String getAlgorithmName() {
        return "Lamport's Bakery Algorithm [" + locale.getLanguage().toUpperCase() + "]";
    }

    public String getAnimationAuthor() {
        return "Jonathan Speth";
    }

    public String getDescription(){
        return translator.translateMessage("description1")
		 +"\n"
		 +translator.translateMessage("description2")
		 +"\n"
		 +"\n"
		 +translator.translateMessage("description3")
		 +"\n"
		 +translator.translateMessage("description4")
		 +"\n"
		 +translator.translateMessage("description5")
		 +"\n"
		 +translator.translateMessage("description6")
		 +"\n"
		 +translator.translateMessage("description7");
    }

    public String getCodeExample(){
        return "  // declaration and initial values of global variables"
		 +"\n"
		 +"  Entering: array [1..NUM_THREADS] of bool = {false};"
		 +"\n"
		 +"  Number: array [1..NUM_THREADS] of integer = {0};"
		 +"\n"
		 +"\n"
		 +"  lock(integer i) {"
		 +"\n"
		 +"      Entering[i] = true;"
		 +"\n"
		 +"      Number[i] = 1 + max(Number[1], ..., Number[NUM_THREADS]);"
		 +"\n"
		 +"      Entering[i] = false;"
		 +"\n"
		 +"      for (integer j = 1; j <= NUM_THREADS; j++) {"
		 +"\n"
		 +"          // Wait until thread j receives its number:"
		 +"\n"
		 +"          while (Entering[j]) { /* nothing */ }"
		 +"\n"
		 +"          // Wait until all threads with smaller numbers or with the same"
		 +"\n"
		 +"          // number, but with higher priority, finish their work:"
		 +"\n"
		 +"          while ((Number[j] != 0) && ((Number[j], j) < (Number[i], i))) { /* nothing */ }"
		 +"\n"
		 +"      }"
		 +"\n"
		 +"  }"
		 +"\n"
		 +"  "
		 +"\n"
		 +"  unlock(integer i) {"
		 +"\n"
		 +"      Number[i] = 0;"
		 +"\n"
		 +"  }"
		 +"\n"
		 +"\n"
		 +"  Thread(integer i) {"
		 +"\n"
		 +"      while (true) {"
		 +"\n"
		 +"          lock(i);"
		 +"\n"
		 +"          // The critical section goes here..."
		 +"\n"
		 +"          unlock(i);"
		 +"\n"
		 +"          // non-critical section..."
		 +"\n"
		 +"      }"
		 +"\n"
		 +"  }";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return locale;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

	@Override
	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		
		numberOfThreads = (Integer)primitives.get("NumberOfThreads");
		if (numberOfThreads <= 0) {
			throw new IllegalArgumentException("Please choose a number of threads that is greater than 0 !");
		}
		else {
			return true;
		}
	}

}
