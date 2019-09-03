/*
 * PetersonAlgo.java
 * Jonathan Speth, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */

package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;
import java.util.Random;
import java.util.Stack;

import algoanim.primitives.IntArray;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
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

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import translator.Translator;
import algoanim.animalscript.AnimalScript;

public class PetersonAlgo implements ValidatingGenerator {
	
    private Language lang;
    
    private Translator translator;
    
    private Locale locale;
    
    private int numberOfProcesses;
    
    private Text headerText;
    
    private Rect hRect;
    
    private SourceCode src;
    
    private TextProperties textProperties;
    
    private SourceCodeProperties sourceCodeProperties;
    
    private ArrayProperties arrayProperties;
    
    private int[] level;
    private int[] lastToEnter;
    
    private IntArray levelArray;
    private IntArray lastToEnterArray;
    
    private Stack<Integer>	order;
    
    private int remaining;
    
    private Random rand = new Random();

    public void init(){
        lang = new AnimalScript("Peterson's Algorithm", "Jonathan Speth", 800, 600);
        lang.setStepMode(true);
    }

    public PetersonAlgo(Locale locale) {
    	this.locale = locale;
    	translator = new Translator("resources/PetersonAlgo", locale);
    	
    	lang = new AnimalScript("Peterson's Algorithm", "Jonathan Speth",
    		800, 600);
    	lang.setStepMode(true);
    }
    
    public void start() {
    	levelArray.hide();
    	lastToEnterArray.hide();
    	
    	 // show the header surrounded by a rectangle
        TextProperties headerProps = new TextProperties();
        headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
            Font.SANS_SERIF, Font.BOLD, 24));
        headerText = lang.newText(new Coordinates(20, 30), "Peterson's Algorithm",
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
            translator.translateMessage("description1"), "description1", null, textProperties);
        lang.newText(new Offset(0, 25, "description1", AnimalScript.DIRECTION_NW),
            translator.translateMessage("description2"), "description2", null, textProperties);
        lang.newText(new Offset(0, 25, "description2", AnimalScript.DIRECTION_NW),
            translator.translateMessage("description3"), "description3", null, textProperties);
        lang.newText(new Offset(0, 25, "description3", AnimalScript.DIRECTION_NW),
            translator.translateMessage("description4"), "description4", null, textProperties);
        lang.newText(new Offset(0, 25, "description4", AnimalScript.DIRECTION_NW),
            translator.translateMessage("description5"), "description5", null, textProperties);
        lang.newText(new Offset(0, 25, "description5", AnimalScript.DIRECTION_NW),
            translator.translateMessage("description6"), "description6", null, textProperties);
        lang.newText(new Offset(0, 25, "description6", AnimalScript.DIRECTION_NW),
            translator.translateMessage("description7"), "description7", null, textProperties);
        lang.newText(new Offset(0, 25, "description7", AnimalScript.DIRECTION_NW),
                translator.translateMessage("description8"), "description8", null, textProperties);
        lang.newText(new Offset(0, 25, "description8", AnimalScript.DIRECTION_NW),
                translator.translateMessage("description9"), "description9", null, textProperties);

        lang.nextStep();
        
        lang.newText(new Offset(0, 50, "description9", AnimalScript.DIRECTION_NW),
            translator.translateMessage("algo11"), "algo11", null, textProperties);
        lang.newText(new Offset(0, 25, "algo11", AnimalScript.DIRECTION_NW),
            translator.translateMessage("algo12"), "algo12", null, textProperties);
        lang.newText(new Offset(0, 25, "algo12", AnimalScript.DIRECTION_NW),
            translator.translateMessage("algo13"), "algo13", null, textProperties);
        lang.newText(new Offset(0, 25, "algo13", AnimalScript.DIRECTION_NW),
            translator.translateMessage("algo14"), "algo14", null, textProperties);
        lang.newText(new Offset(0, 25, "algo14", AnimalScript.DIRECTION_NW),
            translator.translateMessage("algo15"), "algo15", null, textProperties);
        
        lang.nextStep();
        
        lang.hideAllPrimitivesExcept(new ArrayList<Primitive>(Arrays.asList(headerText, hRect)));
        
        src = lang.newSourceCode(new Coordinates(10, 100), "sourceCode", null, sourceCodeProperties);
        src.addCodeLine(" // declaration and initial values of global variables for N processes", null, 0, null); 			// 0
        src.addCodeLine("level : array of N integers", null, 0, null);														// 1
        src.addCodeLine("last_to_enter : array of N-1 integers", null, 0, null);											// 2
        src.addCodeLine("lock(integer i) {", null, 0, null); 																// 3
        src.addCodeLine("for L from 0 to N-1 exclusive {", null, 1, null); 													// 4
        src.addCodeLine("level[i] <- L", null, 2, null); 																	// 5
        src.addCodeLine("last_to_enter[L] <- i", null, 2, null); 															// 6
        src.addCodeLine("while last_to_enter[L] = i and there exists k != i, such that level[k] >= L {", null, 2, null);	// 7
        src.addCodeLine("// wait", null, 3, null); 																			// 8
        src.addCodeLine("}", null, 2, null); 																				// 9
        src.addCodeLine("}", null, 1, null); 																				// 10
        src.addCodeLine("}", null, 0, null);																				// 11
        src.addCodeLine("", null, 0, null); 																				// 12
        src.addCodeLine("unlock(integer i) {", null, 0, null); 																// 13
        src.addCodeLine("level[i] <- 0.", null, 1, null); 																	// 14
        src.addCodeLine("}", null, 0, null); 																				// 15
        src.addCodeLine("", null, 0, null); 																				// 16
        src.addCodeLine("Thread(integer i) {", null, 0, null); 																// 17
        src.addCodeLine("while (true) {", null, 1, null); 																	// 18
        src.addCodeLine("lock(i);", null, 2, null); 																		// 19
        src.addCodeLine("// The critical section goes here...", null, 2, null); 											// 20
        src.addCodeLine("unlock(i);", null, 2, null); 																		// 21
        src.addCodeLine("// non-critical section...", null, 2, null); 														// 22
        src.addCodeLine("}", null, 1, null); 																				// 23
        src.addCodeLine("}", null, 0, null); 																				// 24

        
        lang.nextStep();
        
        peterson();
        
    }
    
    private void peterson() {

        src.highlight(17);
    	src.highlight(18);
    	src.highlight(19);
    	src.highlight(20);
    	src.highlight(21);
    	src.highlight(22);
    	src.highlight(23);
    	src.highlight(24);
    	
    	lang.newText(new Offset(0, -100, "sourceCode", AnimalScript.DIRECTION_SE),
    			translator.translateMessage("explanation1"), "explanation1", null, textProperties);
    	lang.newText(new Offset(0, 25, "explanation1", AnimalScript.DIRECTION_NW),
    			translator.translateMessage("explanation2"), "explanation2", null, textProperties);
    	lang.newText(new Offset(0, 25, "explanation2", AnimalScript.DIRECTION_NW),
    			translator.translateMessage("explanation3"), "explanation3", null, textProperties);
    	lang.newText(new Offset(0, 25, "explanation3", AnimalScript.DIRECTION_NW),
    			translator.translateMessage("explanation4"), "explanation4", null, textProperties);
    	
    	lang.nextStep();
        
    	lang.hideAllPrimitivesExcept(new ArrayList<Primitive>(Arrays.asList(headerText, hRect, src)));
    	
    	src.unhighlight(17);
    	src.unhighlight(18);
    	src.unhighlight(19);
    	src.unhighlight(20);
    	src.unhighlight(21);
    	src.unhighlight(22);
    	src.unhighlight(23);
    	src.unhighlight(24);
    	
    	src.highlight(3);
    	src.highlight(4);
    	src.highlight(5);
    	src.highlight(6);
    	src.highlight(7);
    	src.highlight(8);
    	src.highlight(9);
    	src.highlight(10);
    	src.highlight(11);
    	
    	lang.newText(new Offset(0, 50, "sourceCode", AnimalScript.DIRECTION_NE),
    			translator.translateMessage("explanation5"), "explanation5", null, textProperties);
    	lang.newText(new Offset(0, 25, "explanation5", AnimalScript.DIRECTION_NW),
    			translator.translateMessage("explanation6"), "explanation6", null, textProperties);
    	lang.newText(new Offset(0, 25, "explanation6", AnimalScript.DIRECTION_NW),
    			translator.translateMessage("explanation7"), "explanation7", null, textProperties);
       	lang.newText(new Offset(0, 25, "explanation7", AnimalScript.DIRECTION_NW),
    			translator.translateMessage("explanation8"), "explanation8", null, textProperties);
       	
       	lang.nextStep();
    	
    	lang.hideAllPrimitivesExcept(new ArrayList<Primitive>(Arrays.asList(headerText, hRect, src)));
    	
    	src.unhighlight(3);
    	src.unhighlight(4);
    	src.unhighlight(5);
    	src.unhighlight(6);
    	src.unhighlight(7);
    	src.unhighlight(8);
    	src.unhighlight(9);
    	src.unhighlight(10);
    	src.unhighlight(11);
    	
    	levelArray.show();
    	levelArray.setFillColor(0, numberOfProcesses, Color.CYAN, null, null);
        lang.newText(new Offset(-100, 0, "level", AnimalScript.DIRECTION_NW), "level", "levelName", null, textProperties);
    	lastToEnterArray.show();
    	lastToEnterArray.setFillColor(0, Color.BLACK, null, null);
    	
        lang.newText(new Offset(0, 50, "levelName", AnimalScript.DIRECTION_NW), "last_to_enter", "lastToEnterName", null, textProperties);

        lang.newText(new Offset(25, 25, "levelName", AnimalScript.DIRECTION_NW), "i = ", "iDisplayLevel", null, textProperties);
        lang.newText(new Offset(25, 25, "lastToEnterName", AnimalScript.DIRECTION_NW), "i = ", "iDisplayLastToEnter", null, textProperties);
        
        lang.newText(new Offset(0, 250, "lastToEnterName", AnimalScript.DIRECTION_SW), "Legend", "legend", null, textProperties);
        
        SquareProperties legendProperties = new SquareProperties();
        legendProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        
        legendProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.CYAN);
        lang.newSquare(new Offset(25, 15, "legend", AnimalScript.DIRECTION_SW), 15, "legendSquare1", null, legendProperties);
        lang.newText(new Offset(25, -5, "legendSquare1", AnimalScript.DIRECTION_NE), translator.translateMessage("legend1"), "legendDesc1", null, textProperties);
        
        legendProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
        lang.newSquare(new Offset(25, 40, "legend", AnimalScript.DIRECTION_SW), 15, "legendSquare2", null, legendProperties);
        lang.newText(new Offset(25, -5, "legendSquare2", AnimalScript.DIRECTION_NE), translator.translateMessage("legend2"), "legendDesc2", null, textProperties);
        
        legendProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GREEN);
        lang.newSquare(new Offset(25, 65, "legend", AnimalScript.DIRECTION_SW), 15, "legendSquare3", null, legendProperties);
        lang.newText(new Offset(25, -5, "legendSquare3", AnimalScript.DIRECTION_NE), translator.translateMessage("legend3"), "legendDesc3", null, textProperties);
        
        src.highlight(1);
        src.highlight(2);

    	lang.nextStep(translator.translateMessage("explanationend"));
    	
    	src.unhighlight(1);
    	src.unhighlight(2);
    	
    	while(remaining > 0) {
    		lock();
    	}
    	
       	lang.newText(new Offset(0, 100, "levelName", AnimalScript.DIRECTION_NW),
    			translator.translateMessage("ending1"), "ending1", null, textProperties);
       	lang.newText(new Offset(0, 25, "ending1", AnimalScript.DIRECTION_NW),
    			translator.translateMessage("ending2"), "ending2", null, textProperties);
       	lang.newText(new Offset(0, 50, "ending2", AnimalScript.DIRECTION_NW),
    			translator.translateMessage("ending3"), "ending3", null, textProperties);
       	lang.newText(new Offset(0, 25, "ending3", AnimalScript.DIRECTION_NW),
    			translator.translateMessage("ending4"), "ending4", null, textProperties);
       	
       	lang.nextStep(translator.translateMessage("conclusion"));
    }
    
    private void lock()  {
    	
    	int pID;
    	// new process enters
    	if(order.size() >= 1) {
    		pID = order.pop();
    	}
    	else {
    		pID = 0;

    		for (int i = 0; i < level.length; i++) {
    		    pID = level[i] > level[pID] ? i : pID;
    		}
    	}
    	
    	for(int L = 1; L < numberOfProcesses; L++) {
    		if(level[pID] > 0 && level[pID] < numberOfProcesses - 1) {
				final int key = L-1;
				level[pID]--;
				if(!(lastToEnter[L] == pID && Arrays.stream(level).anyMatch(k -> k >= key))) {
		    		level[pID] = L;
		    		levelArray.put(pID, L, null, null);
		    		levelArray.highlightElem(pID, null, null);
		    		levelArray.setFillColor(pID, Color.YELLOW, null, null);
		    		src.highlight(5);
		    		
		    		lang.nextStep();
		    		
		    		lastToEnter[L] = pID;
		    		lastToEnterArray.put(L, pID, null, null);
		    		lastToEnterArray.highlightElem(L, null, null);
		    		src.unhighlight(5);
		    		src.highlight(6);
		    		levelArray.unhighlightElem(pID, null, null);
		    		
		    		lang.nextStep();
		    		
		    		lastToEnterArray.unhighlightElem(L, null, null);
		    		src.unhighlight(6);

				} else {
					level[pID]++;
				}
    		}
    		else {
    			level[pID] = L;
	    		levelArray.put(pID, L, null, null);
	    		levelArray.highlightElem(pID, null, null);
	    		levelArray.setFillColor(pID, Color.YELLOW, null, null);
	    		src.highlight(5);
	    		
	    		lang.nextStep();
	    		
	    		lastToEnter[L] = pID;
	    		lastToEnterArray.put(L, pID, null, null);
	    		lastToEnterArray.highlightElem(L, null, null);
	    		src.unhighlight(5);
	    		src.highlight(6);
	    		levelArray.unhighlightElem(pID, null, null);
	    		
	    		lang.nextStep();
	    		
	    		lastToEnterArray.unhighlightElem(L, null, null);
	    		src.unhighlight(6);
    		}
    		
    		// simulate other threads entering sometimes
    		if((order.size() >= 1) ) {
    			// rolls dice between 0 and 1 + order.size / 4, since the right boundary is exclusive
    			int randy = rand.nextInt(2 + (order.size() / 4));
    			for(int r = 0; r < randy; r++) {
    				int newpID = order.pop();
    				
    	    		level[newpID] = 1;
    	    		levelArray.put(newpID, 1, null, null);
    	    		levelArray.highlightElem(newpID, null, null);
    	    		levelArray.setFillColor(newpID, Color.YELLOW, null, null);
    	    		src.highlight(5);
    	    		
    	    		lastToEnter[1] = newpID;
    	    		lastToEnterArray.put(1, newpID, null, null);
    	    		lastToEnterArray.highlightElem(1, null, null);
    	    		src.highlight(6);
    	    		
    			}
        		lang.nextStep();
        		
        		src.unhighlight(5);
        		src.unhighlight(6);
        		lastToEnterArray.unhighlightElem(1, null, null);
        		levelArray.unhighlightElem(0, numberOfProcesses - 1, null, null);
        		
    		}
    		
    		for(int k = 0; k < numberOfProcesses; k++) {
    			if(level[k] == numberOfProcesses - 1) {
    				unlock(k);
    			}
    			
    			if(level[k] > 0 && level[k] < numberOfProcesses - 1) {
    				final int key = k;
    				if(!Arrays.stream(lastToEnter).anyMatch(x -> x == key)) {
    					level[k]++;
    		    		levelArray.put(k, level[k], null, null);
    		    		levelArray.highlightElem(k, null, null);
    		    		levelArray.setFillColor(k, Color.YELLOW, null, null);
    		    		src.highlight(5);
    		    		
    		    		lang.nextStep();
    		    		
    		    		lastToEnter[level[k]] = k;
    		    		lastToEnterArray.put(level[k], k, null, null);
    		    		lastToEnterArray.highlightElem(level[k], null, null);
    		    		src.unhighlight(5);
    		    		src.highlight(6);
    		    		levelArray.unhighlightElem(k, null, null);
    		    		
    		    		lang.nextStep();
    		    		
    		    		lastToEnterArray.unhighlightElem(level[k], null, null);
    		    		src.unhighlight(6);
    		    		
    		    		if(key == pID) {
    		    			L++;
    		    		}
    				}
    			}
    			if(level[k] == numberOfProcesses - 1) {
    				unlock(k);
    			}
    		}
    	}
    }
    
	private void unlock(int pID) {
		src.highlight(20);
		levelArray.highlightElem(pID, null, null);
		
		lang.nextStep("Process " + pID + translator.translateMessage("unlock"));
		
		src.unhighlight(20);
		levelArray.unhighlightElem(pID, null, null);
		
		level[pID] = 0;
		levelArray.put(pID, 0, null, null);
		levelArray.setFillColor(pID, Color.GREEN, null, null);
		src.highlight(14);
		src.highlight(21);
		
		lang.nextStep();
		
		src.unhighlight(14);
		src.unhighlight(21);
		
		remaining--;
	}
	
    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
    	lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

        numberOfProcesses = (Integer)primitives.get("Number of processes");
        arrayProperties = (ArrayProperties)props.getPropertiesByName("ArrayProperties");
        sourceCodeProperties = (SourceCodeProperties)props.getPropertiesByName("SourceCodeProperties");
        
        textProperties = new TextProperties();
        textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
            Font.SANS_SERIF, Font.PLAIN, 16));
        
        level = new int[numberOfProcesses];
        lastToEnter = new int[numberOfProcesses];
        for(int i = 0; i < numberOfProcesses; i++) {
        	lastToEnter[i] = -1;
        }
        
        levelArray = lang.newIntArray(new Coordinates(600, 100), level, "level", null, arrayProperties);
        lastToEnterArray = lang.newIntArray(new Offset(0, 50, "level", AnimalScript.DIRECTION_NW), lastToEnter, "lastToEnter", null, arrayProperties);
        
        // generate order of entering threads and shuffle
    	order = new Stack<Integer>();
    	for(int k = 0; k < numberOfProcesses; k++) {
    		order.push(k);
    	}
    	Collections.shuffle(order);
    	remaining = numberOfProcesses;
    	
        start();
        
        lang.finalizeGeneration();
        return lang.toString();
    }

    public String getName() {
        return "Peterson's Algorithm [" + locale.getLanguage().toUpperCase() + "]";
    }

    public String getAlgorithmName() {
        return "Peterson's Algorithm [" + locale.getLanguage().toUpperCase() + "]";
    }

    public String getAnimationAuthor() {
        return "Jonathan Speth";
    }

    public String getDescription(){
        return translator.translateMessage("description1")
		 +"\n"
		 +translator.translateMessage("description2")
		 +"\n"
		 +translator.translateMessage("description3")
		 +"\n"
		 +"\n"
		 +translator.translateMessage("description4")
		 +"\n"
		 +translator.translateMessage("description5")
		 +"\n"
		 +translator.translateMessage("description6")
		 +"\n"
		 +"\n"
		 +translator.translateMessage("description7")
        +"\n"
		 +translator.translateMessage("description8")
        +"\n"
		 +translator.translateMessage("description9");
    }

    public String getCodeExample(){
        return "level : array of N integers"
        		+"\n"
				+"last_to_enter : array of N?1 integers"
				+"\n"
				+"\n"
				+"lock(int i) {"
				+"\n"
				+"  for l from 0 to N?1 exclusive"
				+"\n"
				+"      level[i] ? l"
				+"\n"
				+"      last_to_enter[l] ? i"
				+"\n"
				+"      while (last_to_enter[l] = i and there exists k ? i, such that level[k] ? l) {"
				+"\n"
				+"        //wait"
				+"\n"
				+"      }"
				+"\n"
				+"}"
				+"\n"
				+"\n"
				+"unlock(int i) {"
				+"\n"
				+"  level[i] ? -1"
				+"\n"
				+"}";
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
		
		numberOfProcesses = (Integer)primitives.get("Number of processes");
		if (numberOfProcesses <= 0) {
			throw new IllegalArgumentException("Please choose a number of threads that is greater than 0 !");
		}
		else {
			return true;
		}
	}

}