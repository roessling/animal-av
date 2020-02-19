/*
 * FloydsCycleDetection.java
 * Tuan Kiet Tran, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */


package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;

import algoanim.primitives.ListElement;
import algoanim.primitives.Polyline;
import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.Language;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.LinkedList;

import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.QuestionGroupModel;
import interactionsupport.models.TrueFalseQuestionModel;
import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.AnimalTextGenerator;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Timing;
import animal.main.Animal;
import algoanim.properties.PolylineProperties;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ListElementProperties;


//A linked list node
class Node
{
	int data;
	Node next;

	Node(int data, Node next) {
		this.data = data;
		this.next = next;
	}
};

public class FloydsCycleDetection implements ValidatingGenerator {
	private Language lang;
    private int[] linkedlist;
    private int startcycle;
    private int endcycle;
    private SourceCodeProperties sourceCode;
    private SourceCodeProperties description;
    private SourceCodeProperties endtext;
    private SourceCodeProperties title;
    private ListElementProperties listElements;
    

    public void init(){
        lang = new AnimalScript("Floyd's Cycle Detection Algorithm", "Tuan Kiet Tran", 800, 600);
        lang.setStepMode(true);
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
    	linkedlist = (int[])primitives.get("linkedlist");
        startcycle = (Integer)primitives.get("startcycle");
        endcycle = (Integer)primitives.get("endcycle");
        sourceCode = (SourceCodeProperties)props.getPropertiesByName("sourceCode");
        description = (SourceCodeProperties)props.getPropertiesByName("description");
        endtext = (SourceCodeProperties)props.getPropertiesByName("endtext");
        title = (SourceCodeProperties)props.getPropertiesByName("title");
        listElements = (ListElementProperties)props.getPropertiesByName("listElements");
        
        /**
		 * Configure your LinkedList and the existing Cycle here
		 */
		// input keys
		//Integer[] keys = { 1, 2, 3, 4, 5 };
        int[] keys = linkedlist;
      
        //head.data starts at first key
      		Node head = null;
      		for (int i = keys.length-1; i >= 0; i--) {
      			head = new Node(keys[i], head);
      		}

      	int start = startcycle;
      	int end = endcycle;
      		
      		// insert cycle
      		Node startcycle = head;
      		Node endcycle = head;
      		
      		for (int i = 0; i<start-1 && startcycle.next != null; i++) {
      			startcycle = startcycle.next;
      		}
      		
      		for (int i = 0; i<end-1; i++) {
      			endcycle = endcycle.next;
      		}
      		if(this.startcycle != 0 && this.endcycle != 0)
      		startcycle.next = endcycle;
		
		floydPrep(head, keys.length, start, end);
        
		lang.finalizeGeneration();
        return lang.toString();
    }
        
    public void floydPrep(Node first, int listlength, int startcycle, int endcycle) {
    	
    	
    	//Create Description Text
		
		SourceCodeProperties descProp = this.description;
	    //descProp.set(AnimationPropertiesKeys.FONT_PROPERTY,
	    //   new Font("SansSerif", Font.PLAIN, 12));

	    SourceCode description = lang.newSourceCode(new Coordinates(400, 160), "sourceCode",
	        null, descProp);
	    
	    description.addCodeLine("The Floyds Cycle Detection Algorithm is a cycle-finding algorithm which", null, 0, null); // 0
	    description.addCodeLine("detects cycles in for example lists. It's a pointer algorithm using only", null, 0, null); // 0
	    description.addCodeLine(" two pointers, which move through the sequence at different speeds.", null, 0, null); // 0
	    description.addCodeLine(" Both Pointer start at position i, while the first pointer moves at a speed of one ", null, 0, null); // 0
	    description.addCodeLine("step per iteration and the second pointer two steps per iteration. ", null, 0, null); // 0
	    description.addCodeLine("The algorithm compares the sequence values at these two pointers at each iteration.", null, 0, null); // 0
	    description.addCodeLine(" If both values are equal, a cycle has been detected and the algorithm terminates,", null, 0, null); // 0
	    description.addCodeLine("otherwise no cycle exists in the sequence.", null, 0, null); // 0
		
		
		//Create algorithm title
		SourceCodeProperties titleProps = this.title;
		 
		SourceCode title = lang.newSourceCode(new Coordinates(20, 5), "title",
		        null, titleProps);
		
		title.addCodeLine("Floyd's Cycle Detection Algorithm", null, 0, null);
		
		// set the default list properties
		ListElementProperties listProps = this.listElements;

		lang.nextStep("Introduction");
		
		LinkedList<Object> currentPointers = new LinkedList<Object>();
		
		//Create lines from element 5 to 3
		
		//With Arrow
		PolylineProperties polyProps1 = new PolylineProperties();
		polyProps1.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
		
		//Without Arrow
		PolylineProperties polyProps2 = new PolylineProperties();
		polyProps2.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
		polyProps2.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
		
		//Create the first line for an existing cycle, if there is one
		if (startcycle > 0) {
		Coordinates[] vertices = new Coordinates[2];
		
		vertices[0] = new Coordinates(50 + (startcycle-1)*60, 160);
		vertices[1] = new Coordinates(50 + (endcycle-1)*60,160);
				
		Polyline line1 = lang.newPolyline(vertices, "firstline", null, polyProps1);
				
		currentPointers.add(vertices[0]);
		
		Coordinates[] vertices2 = new Coordinates[2];
		
		vertices2[0] = new Coordinates(50 + (endcycle-1)*60,160);
		vertices2[1] = new Coordinates(50 + (endcycle-1)*60,112);
				
		Polyline line2 = lang.newPolyline(vertices2, "firstline", null, polyProps2);
		}
		
		//Create the listobjects
		ListElement[] listelements = new ListElement[listlength];
		int x = 20;
		int y = 100;
		for (int i = 0; i < listlength; i++) {
			if (i == startcycle-1) {
				listelements[i] = lang.newListElement(new Coordinates(x, y), 1, currentPointers, null, Integer.toString(i), null, listProps);
				x = x + 60;
			}
			else {
			listelements[i] = lang.newListElement(new Coordinates(x, y), 1, null, null, Integer.toString(i), null, listProps);
			x = x + 60;
			}
		}
		
		//Link the elements together
		for(int k = 1; k < listlength; k++) {
			// connect the linked list objects
			if (k != startcycle)
			listelements[k-1].link(listelements[k], 1, Timing.FAST, Timing.FAST);
				// TODO: else-case: last listelement doesnt have a next. (Probably no need?)	
		}
		
		lang.nextStep();
		
		//Hide Description
		description.hide();
		
		//Create sourcecode to display
		
		//visual properties for the source code
	    SourceCodeProperties scProps = this.sourceCode;

	    // source code entity
	    SourceCode sc = lang.newSourceCode(new Coordinates(40, 200), "sourceCode",
	        null, scProps);
	    
	    // Add the lines to the SourceCode object.
	    sc.addCodeLine("public static boolean detectCycle(Node head)", null, 0, null); // 0
	    sc.addCodeLine("{", null, 0, null);
	    sc.addCodeLine("Node slow = head, fast = head;", null, 1, null);
	    sc.addCodeLine("while (fast != null && fast.next != null)", null, 1, null); // 3
	    sc.addCodeLine("{", null, 1, null); // 4
	    sc.addCodeLine("slow = slow.next;", null, 2, null); // 5
	    sc.addCodeLine("fast = fast.next.next;", null, 2, null); // 6
	    sc.addCodeLine("if (slow == fast)", null, 2, null); // 7
	    sc.addCodeLine("{", null, 2, null); // 8
	    sc.addCodeLine("return true;", null, 3, null); // 9
	    sc.addCodeLine("}", null, 2, null); // 10
	    sc.addCodeLine("}", null, 1, null); // 11
	    sc.addCodeLine("return false;", null, 1, null); // 12
	    sc.addCodeLine("}", null, 0, null); // 13
	    
	    lang.nextStep("Initialization");
	    
	    //start Floyd's Cycle algorithm
	    detectCycle(first, sc, startcycle, endcycle, listelements);
	}

    public boolean detectCycle(Node head, SourceCode code, int startcycle, int endcycle, ListElement[] listelements)
	{
		
    	//Create Question Groups
    	QuestionGroupModel terminate = new QuestionGroupModel("1", 4);
    	lang.addQuestionGroup(terminate);
    	
    	//Create Questons
    	FillInBlanksQuestionModel iterations = new FillInBlanksQuestionModel("iterations");
    	iterations.setPrompt("How many iterations will the while loop have?");
		iterations.addAnswer(Integer.toString(detectCycleIterations(head)), 3, "Thats right! :)");
		
		TrueFalseQuestionModel terminatingright = new TrueFalseQuestionModel("terminatingright");
    	terminatingright.setPrompt("The while loop stops after this Iteration");
    	terminatingright.setGroupID("1");
		terminatingright.setCorrectAnswer(true);
		terminatingright.setFeedbackForAnswer(true, "Thats right! :)");
		terminatingright.setPointsPossible(1);
		terminatingright.setNumberOfTries(2);
		

		TrueFalseQuestionModel terminatingwrong = new TrueFalseQuestionModel("terminatingwrong");
    	terminatingwrong.setPrompt("The while loop stops after this Iteration");
    	terminatingwrong.setGroupID("1");
		terminatingwrong.setCorrectAnswer(false);
		terminatingwrong.setFeedbackForAnswer(false, "Thats right! :");
		terminatingwrong.setPointsPossible(1);
		terminatingwrong.setNumberOfTries(2);
		
    	int iteration = 2;
    	
		//Create Highlight Arrows
		code.highlight(0, 0, false);
		lang.nextStep();
		
		code.toggleHighlight(0, 0, false, 2, 0);
		
		//Create two Polylines for slow and fast
		PolylineProperties polyPropsSlow = new PolylineProperties();
		PolylineProperties polyPropsFast = new PolylineProperties();
		polyPropsSlow.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);		
		polyPropsSlow.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
		polyPropsSlow.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
		
		polyPropsFast.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);		
		polyPropsFast.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
		polyPropsFast.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN);
						
		//Create slow pointer
		Coordinates[] vertices = new Coordinates[2];
						
		vertices[0] = new Coordinates(40,70);
		vertices[1] = new Coordinates(40,100);
						
		Polyline slowLine = lang.newPolyline(vertices, "firstline", null, polyPropsSlow);
				
		//Create fast pointer	
		Coordinates[] vertices2 = new Coordinates[2];
				
		vertices2[0] = new Coordinates(60,70);
		vertices2[1] = new Coordinates(60,100);
						
		Polyline fastLine = lang.newPolyline(vertices2, "firstline", null, polyPropsFast);
		
		//Create legend for fast
		SourceCodeProperties scPropsFast = new SourceCodeProperties();
	    scPropsFast.set(AnimationPropertiesKeys.FONT_PROPERTY,
	        new Font("Monospaced", Font.PLAIN, 12));
	    scPropsFast.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN);

	    SourceCode legendFast = lang.newSourceCode(new Coordinates(500, 30), "sourceCode",
	        null, scPropsFast);
	    
	    legendFast.addCodeLine("fast = green", null, 0, null); // 0
		
	    //Create legend for slow
	    SourceCodeProperties scPropsSlow = new SourceCodeProperties();
	    scPropsSlow.set(AnimationPropertiesKeys.FONT_PROPERTY,
	        new Font("Monospaced", Font.PLAIN, 12));
	    scPropsSlow.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);

	    SourceCode legendSlow = lang.newSourceCode(new Coordinates(500, 50), "sourceCode",
	        null, scPropsSlow);
	    
	    legendSlow.addCodeLine("slow = blue", null, 0, null); // 0
	    
	    
		// take two references - slow and fast
		Node slow = head, fast = head;
		
		//next step

		
		lang.nextStep();
		code.toggleHighlight(2, 0, false, 3, 0);

		lang.addFIBQuestion(iterations);
		
		lang.nextStep("1. Iteration");
		
		
		code.toggleHighlight(3, 0, false, 5, 0);
		
		boolean firstloop = true;
		int fastcounter = 3;
		int slowcounter = 2;
		while (fast != null && fast.next != null)
		{
			if (firstloop) {
				//next step
				
				slowLine.moveBy(null, 60, 0, Timing.INSTANTEOUS, Timing.MEDIUM);
				
				// move slow by one
				slow = slow.next;
				
				lang.nextStep();
				code.toggleHighlight(5, 0, false, 6, 0);
				
				
				if (fastcounter - 2 == startcycle - 1) {
					fastLine.moveBy(null, (startcycle-endcycle-1)*(-60), 0, Timing.INSTANTEOUS, Timing.MEDIUM);;
					
					// move fast by two
					fast = fast.next.next;
					
					fastcounter = endcycle;
				}
				
				else {
				fastLine.moveBy(null, 120, 0, Timing.INSTANTEOUS, Timing.MEDIUM);
				
				// move fast by two
				fast = fast.next.next;
				}
				
				
				firstloop = false;
			}
			else {
				
				lang.nextStep();
				code.toggleHighlight(7, 0, false, 3, 0);
				
				Node tempslow = slow.next;
				Node tempfast = fast.next.next;
				if (tempslow == tempfast) lang.addTFQuestion(terminatingright);
				else lang.addTFQuestion(terminatingwrong);
				
				lang.nextStep(Integer.toString(iteration) + ". Iteration");
				code.toggleHighlight(3, 0, false, 5, 0);
				
				if (slowcounter == startcycle) {
					slowLine.moveBy(null, (startcycle-endcycle)*(-60), 0, Timing.INSTANTEOUS, Timing.MEDIUM);;
					// move slow by one
					slow = slow.next;
					slowcounter = endcycle;
				}
				else {
					slowLine.moveBy(null, 60, 0, Timing.INSTANTEOUS, Timing.MEDIUM);
					// move slow by one
					slow = slow.next;
					
					slowcounter++;
				}
				
				lang.nextStep();
				code.toggleHighlight(5, 0, false, 6, 0);
				
				// First case if counter is at the start of the cycle
				if(fastcounter == startcycle) {
					fastLine.moveBy(null, (startcycle-endcycle-1)*(-60), 0, Timing.INSTANTEOUS, Timing.MEDIUM);
					
					// move fast by two
					fast = fast.next.next;
					
					fastcounter = endcycle + 1;
				}
				// Second case if counter is one behind the start of the cycle
				else if (fastcounter == startcycle - 1) {
					fastLine.moveBy(null, (startcycle-endcycle-1)*(-60), 0, Timing.INSTANTEOUS, Timing.MEDIUM);;
					
					// move fast by two
					fast = fast.next.next;
					
					fastcounter = endcycle;
				}
				else {
					fastLine.moveBy(null, 120, 0, Timing.INSTANTEOUS, Timing.MEDIUM);
					
					// move fast by two
					fast = fast.next.next;
					
					fastcounter = fastcounter + 2;
				}
			}
			
			// if they meet at any node, linked list contains a cycle
			if (slow == fast) {
				lang.nextStep();
				code.toggleHighlight(6, 0, false, 7, 0);
			
				lang.nextStep("Conclusion");
				code.toggleHighlight(7, 0, false, 9, 0);
				
				//Create end text
				SourceCodeProperties endProp = this.endtext;

			    SourceCode endtext = lang.newSourceCode(new Coordinates(400, 160), "sourceCode",
			        null, endProp);
			    endtext.addCodeLine("The algorithm terminates and returns true, because slow and fast are equal", null, 0, null); // 0
			    lang.addQuestionGroup(terminate);
				return true;
			}
			
			lang.nextStep();
			
			code.toggleHighlight(6, 0, false, 7, 0);
			
			
		}
		
		lang.nextStep();
		code.toggleHighlight(7, 0, false, 3, 0);

		// we reach here if slow & fast do not meet
		lang.nextStep("Conclusion");
		code.toggleHighlight(3, 0, false, 12, 0);
		SourceCodeProperties endProp = this.endtext;
	    //endProp.set(AnimationPropertiesKeys.FONT_PROPERTY,
	    //    new Font("SansSerif", Font.PLAIN, 12));

	    SourceCode endtext = lang.newSourceCode(new Coordinates(400, 160), "sourceCode",
	        null, endProp);
	    endtext.addCodeLine("The algorithm terminates and returns false, because fast or fast.next is null", null, 0, null); // 0
		
	    
	    lang.addQuestionGroup(terminate);
		return false;
	}

    
    public static int detectCycleIterations(Node head)
	{
		// take two references - slow and fast
		Node slow = head, fast = head;

		int iterations = 0;
		
		while (fast != null && fast.next != null)
		{
			// move slow by one
			slow = slow.next;

			// move fast by two
			fast = fast.next.next;

			iterations++;
			// if they meet at any node, linked list contains a cycle
			if (slow == fast) {
				return iterations;
			}
		}

		// we reach here if slow & fast do not meet
		return iterations;
	}
    
    
	@Override
	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		linkedlist = (int[])primitives.get("linkedlist");
        startcycle = (Integer)primitives.get("startcycle");
        endcycle = (Integer)primitives.get("endcycle");
		
        int start = startcycle;
        int end = endcycle;
        int[] list = linkedlist;
        
        if (start <= list.length && end < start || end == 0 && start == 0) {
        	return true;
        }
		return false;
	}
    
    public String getName() {
        return "Floyd's Cycle Detection Algorithm";
    }

    public String getAlgorithmName() {
        return "Floyds Cycle Detection Algorithm";
    }

    public String getAnimationAuthor() {
        return "Tuan Kiet Tran";
    }

    public String getDescription(){
        return "Floyd's cycle-finding algorithm is a pointer algorithm that uses only two pointers, which move through the sequence at different speeds. It is also called the \"tortoise and the hare algorithm\", alluding to Aesop's fable of The Tortoise and the Hare."
 +"\n"
 +"The algorithm is named after Robert W. Floyd, who was credited with its invention by Donald Knuth."
 +"\n"
 +"The algorithm maintains two pointers into the given sequence, one (the tortoise) at xi, and the other (the hare) at x2i. At each step of the algorithm, it increases i by one, "
 +"\n"
 +"moving the tortoise one step forward and the hare two steps forward in the sequence, and then compares the sequence values at these two pointers. The smallest value of i > 0 for which the tortoise and hare point to equal values is the desired value.";
    }

    public String getCodeExample(){
        return "public static boolean detectCycle(Node head)"
 +"\n"
 +"	{"
 +"\n"
 +"		// take two references - slow and fast"
 +"\n"
 +"		Node slow = head, fast = head;"
 +"\n"
 +"\n"
 +"		while (fast != null && fast.next != null)"
 +"\n"
 +"		{"
 +"\n"
 +"			// move slow by one"
 +"\n"
 +"			slow = slow.next;"
 +"\n"
 +"\n"
 +"			// move fast by two"
 +"\n"
 +"			fast = fast.next.next;"
 +"\n"
 +"\n"
 +"			// if they meet at any node, linked list contains a cycle"
 +"\n"
 +"			if (slow == fast) {"
 +"\n"
 +"				return true;"
 +"\n"
 +"			}"
 +"\n"
 +"		}"
 +"\n"
 +"\n"
 +"		// we reach here if slow & fast do not meet"
 +"\n"
 +"		return false;"
 +"\n"
 +"	}";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.ENGLISH;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }
    
    public static void main(String[] args) {
    	Generator generator = new FloydsCycleDetection(); // Generator erzeugen
    	Animal.startGeneratorWindow(generator); // Animal mit Generator starten
    }



}