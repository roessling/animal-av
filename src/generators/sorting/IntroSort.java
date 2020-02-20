package generators.sorting;

import java.awt.Color;
import java.awt.Font;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.TwoValueView;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.ListElement;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.AnimationType;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CounterProperties;
import algoanim.properties.ListElementProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;
import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import translator.TranslatableGUIElement;
import translator.Translator;

public class IntroSort implements Generator {
	
	/**
	  * The concrete language object used for creating output
	  */
	private Language lang;
	
	private Locale locale = Locale.US;
	private Translator translator;
	
	private static final String SOURCE_CODE     = "private void introsort(int[] arr, int begin, int end, int maxdepth) {\n" + 
			"		int n = end-begin+1;\n" + 
			"		int p = partition(arr, begin, end);\n" + 
			"		if(n <= 1)\n" + 
			"			return;\n" + 
			"		else if (maxdepth == 0) {\n" + 
			"			int[] temp = copyOut(arr, begin, end);\n" + 
			"			heapsort(temp);\n" + 
			"			copyIn(arr, temp, begin);\n" + 
			"		} else {\n" + 
			"			introsort(arr, begin, p, maxdepth-1);\n" + 
			"			introsort(arr, p+1, end, maxdepth-1);\n" + 
			"		}\n" + 
			"	}"
			+ "\n"
			+ "private void heapsort(int[] arr) {\n" + 
			"		int n = arr.length;\n" + 
			"		for(int i = n/2 - 1; i>= 0; i--) {\n" + 
			"			heapify(arr, n, i);\n" + 
			"		}\n" + 
			"		for(int i=n-1; i>=0; i--) {\n" + 
			"			swap(arr, 0, i);\n" + 
			"			heapify(arr, i, 0);\n" + 
			"		}\n" + 
			"	}";
	
	/**
	  * default duration for swap processes
	  */
	public final static Timing  defaultDuration = new TicksTiming(30);
	
	private ArrayProperties arrayProps;
	private ArrayMarker beginMarker;
	private ArrayMarker endMarker;
	private ArrayMarker pMarker;
	private Text maxDepthText;
	private Text titleRecursion;
	private int recCounter = 0;
	private int loopCounter = 0;
	
	/**
	  * Default constructor
	  */
	public IntroSort(String path, Locale locale) {
		this.locale = locale;
		translator = new Translator(path, locale); 
		init();
	}
	
	public void init() {
		// Create a new language object for generating animation code
	    // this requires type, name, author, screen width, screen height
		lang = new AnimalScript("Introsort", "Simon Althaus, Florian Giger", 800, 600);
		
		// This initializes the step mode. Each pair of subsequent steps has to
	    // be divided by a call of langintroSrcProps.nextStep();
		lang.setStepMode(true);
	}
	
	public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
	    
		SourceCodeProperties scProps = (SourceCodeProperties)props.getPropertiesByName("sourceCode");
		arrayProps = (ArrayProperties)props.getPropertiesByName("array");
		int[] arr = (int[])primitives.get("intArray");
        
        TextProperties titleProps = new TextProperties();
		titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 24));
		
		Text title = lang.newText(new Coordinates(20,30), "Introsort", "title", null, titleProps);
		
		//Intro
		SourceCodeProperties introScProps = new SourceCodeProperties();
		introScProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 18));
		SourceCode introSC = lang.newSourceCode(new Offset(0, 20, title, AnimalScript.DIRECTION_SW), "introSourceCode", null, introScProps);
		introSC.addMultilineCode(translator.translateMessage("introduction"), null, null);		
		
		lang.nextStep(translator.translateMessage("introductionStep"));
		
		introSC.hide();
		
		// Create Array: coordinates, data, name, display options,
	    IntArray ia = lang.newIntArray(new Coordinates(20, 150), arr, "intArray", null, arrayProps);
	    
	    //Counter
	    TwoValueCounter counter = lang.newCounter(ia);
	    CounterProperties cp = new CounterProperties();
	    cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
	    cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);
	    TwoValueView view = lang.newCounterView(counter, new Coordinates(350,60), cp, true, true);
	    
    	//Create two markers to point on begin and end
  		// Array, current index, name, display options, properties
  	    ArrayMarkerProperties arrayBeginMProps = new ArrayMarkerProperties();
  	    arrayBeginMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "begin");
  	    arrayBeginMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
  	    arrayBeginMProps.set(AnimationPropertiesKeys.LONG_MARKER_PROPERTY, true);
  	    beginMarker = lang.newArrayMarker(ia, 0, "begin", null, arrayBeginMProps);
  	    beginMarker.hide();
  	    
  	    ArrayMarkerProperties arrayEndMProps = new ArrayMarkerProperties();
  	    arrayEndMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "end");
  	    arrayEndMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
  	    arrayEndMProps.set(AnimationPropertiesKeys.SHORT_MARKER_PROPERTY, true);
  	    endMarker = lang.newArrayMarker(ia, ia.getLength()-1, "end", null, arrayEndMProps);
  	    endMarker.hide();
  	    
		//Create marker to point on p
	    ArrayMarkerProperties arrayPMProps = new ArrayMarkerProperties();
	    arrayPMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "p");
	    arrayPMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
	    pMarker = lang.newArrayMarker(ia, 0, "p", null, arrayPMProps);
	    pMarker.hide();
	    
	    // Create SourceCode: coordinates, name, display options,
	    SourceCode sc = lang.newSourceCode(new Coordinates(40, 190), "sourceCode", null, scProps);
	    
	    //Add the lines to the SourceCode object.
	    // Line, name, indentation, display dealy
	    sc.addCodeLine("private void introsort(int[] arr, int begin, int end, int maxdepth) {", null, 0, null); //0
	    sc.addCodeLine("int n = end-begin+1;", null, 1, null); //1
	    sc.addCodeLine("int p = partition(arr, begin, end);", null, 1, null); //2
	    sc.addCodeLine("if(n <= 1)", null, 1, null); //3
	    sc.addCodeLine("return;", null, 2, null); //4
	    sc.addCodeLine("else if (maxdepth == 0) {", null, 1, null); //5
	    sc.addCodeLine("int[] temp = copyOut(arr, begin, end);", null, 2, null); //6
	    sc.addCodeLine("heapsort(temp);", null, 2, null); //7
	    sc.addCodeLine("copyIn(arr, temp, begin);", null, 2, null); //8
	    sc.addCodeLine("} else {", null, 1, null); //9
	    sc.addCodeLine("introsort(arr, begin, p, maxdepth-1);", null, 2, null); //10
	    sc.addCodeLine("introsort(arr, p+1, end, maxdepth-1);", null, 2, null); //11
	    sc.addCodeLine("}", null, 1, null); //12
	    sc.addCodeLine("}", null, 0, null); //13
	    sc.addCodeLine("", null, 0, null); //14
	    sc.addCodeLine("private void heapsort(int[] arr) {", null, 0, null); //15
	    sc.addCodeLine("int n = arr.length;", null, 1, null); //16
	    sc.addCodeLine("for(int i = n/2 - 1; i>= 0; i--) {", null, 1, null); //17
	    sc.addCodeLine("heapify(arr, n, i);", null, 2, null); //18
	    sc.addCodeLine("}", null, 1, null); //19
	    sc.addCodeLine("for(int i=n-1; i>=0; i--) {", null, 1, null); //20
	    sc.addCodeLine("swap(arr, 0, i);", null, 2, null); //21
	    sc.addCodeLine("heapify(arr, i, 0);", null, 2, null); //22
	    sc.addCodeLine("}", null, 1, null); //23
	    sc.addCodeLine("}", null, 0, null); //24
	    
	    
	    TextProperties titleRecursionProps = new TextProperties();
	    titleRecursionProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 18));
        titleRecursion = lang.newText(new Offset(0, 200, sc, AnimalScript.DIRECTION_NE), translator.translateMessage("recursionTitle"), "recursionTitle", null, titleRecursionProps);
        
        lang.nextStep("Introsort");
		
		int maxdepth = (int) Math.floor(Math.log(arr.length))*2;
		TextProperties maxDepthTextProps = new TextProperties();
		maxDepthTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));
	    maxDepthText = lang.newText(new Offset(0, -25, titleRecursion, AnimalScript.DIRECTION_NW), "maxdepth = " + maxdepth , "maxDepth", null, maxDepthTextProps);
		
		try { 
			introsort(ia, sc, 0, arr.length-1, maxdepth, null, 0, 2);
		} catch (LineNotExistsException e) {
			e.printStackTrace();
		}

		//Summary
		lang.hideAllPrimitives();
		beginMarker.changeColor("color", Color.WHITE, null, null);
		endMarker.changeColor("color", Color.WHITE, null, null);
		title.show();
		int n = ia.getLength();
		SourceCodeProperties summaryScProps = new SourceCodeProperties();
		summaryScProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 18));
		SourceCode summarySC = lang.newSourceCode(new Offset(0, 20, title, AnimalScript.DIRECTION_SW), "summarySourceCode", null, summaryScProps);
		summarySC.addMultilineCode(translator.translateMessage("summary1") + " " + recCounter + " "
				+ translator.translateMessage("summary2") + " " + loopCounter + " "
				+ translator.translateMessage("summary3") + " " + (recCounter+loopCounter)
				+ " < n log n = " + n + " log " + n + " ≈ " + ((int) Math.floor(n * Math.log(n) / Math.log(2))) + ".\n"
				+ translator.translateMessage("summary4"), null, null);
		
		lang.nextStep(translator.translateMessage("summaryStep"));
        
        return lang.toString();
    }
	  
	private void introsort(IntArray arr, SourceCode codeSupport, int begin, int end, int maxdepth, Rect prev, int depth, int relative) throws LineNotExistsException {		
		// Highlight first line
	    // Line, Column, use context colour?, display options, duration
		pMarker.hide();
		arr.unhighlightCell(0, arr.getLength()-1, null, null);
		
		maxDepthText.setText("maxdepth = " + maxdepth, null, null);
		codeSupport.highlight(0, 0, false);
		arr.highlightCell(begin, end, null, null);
		
		beginMarker.move(begin, null, null);
		endMarker.move(end, null, null);
		beginMarker.show();
		endMarker.show();
		
		String introCall = "intro(arr," + begin + "," + end + "," + maxdepth + ")";
		Rect listElem = createNode(relative, prev, depth, introCall);
		recCounter++;
	    
	    lang.nextStep(introCall);
	    codeSupport.toggleHighlight(0,1);
	    int n = end-begin+1;
	    
	    lang.nextStep();
	    codeSupport.toggleHighlight(1,2);
		int p = partition(arr, begin, end);
	    
		lang.nextStep();
	    codeSupport.toggleHighlight(2,3);
		if(n <= 1) {
			lang.nextStep();
			codeSupport.toggleHighlight(3,4);
			
			lang.nextStep();
			codeSupport.unhighlight(4);
			listElem.changeColor("color", Color.GREEN, null, null);
			return;
		} else if (maxdepth == 0) {
			lang.nextStep();
			codeSupport.toggleHighlight(3,5);
			
			lang.nextStep();
			codeSupport.toggleHighlight(5,6);
			IntArray temp = copyOut(arr, begin, end);
			
			lang.nextStep(translator.translateMessage("heapsortCall1") + begin + translator.translateMessage("heapsortCall1")  + end);
			codeSupport.toggleHighlight(6,7);
			heapsort(temp, codeSupport);
			
			lang.nextStep();
			codeSupport.unhighlight(24);
			codeSupport.toggleHighlight(7,8);
			copyIn(arr, temp, begin);
			
			lang.nextStep();
			codeSupport.unhighlight(8);
			temp.hide();
		} else {
			lang.nextStep();
			codeSupport.toggleHighlight(3,9);
			
			lang.nextStep();
			codeSupport.toggleHighlight(9,10);
			lang.nextStep();
			codeSupport.unhighlight(10);
			introsort(arr, codeSupport, begin, p, maxdepth-1, listElem, depth + 1, 0);
			
			lang.nextStep();
			codeSupport.toggleHighlight(10,11);
			lang.nextStep();
			codeSupport.unhighlight(11);
			introsort(arr, codeSupport, p+1, end, maxdepth-1, listElem, depth + 1, 1);
			
			lang.nextStep();
			codeSupport.unhighlight(11);
		}
		
		listElem.changeColor("color", Color.GREEN, null, null);
		lang.nextStep();
	}
	
	private Rect createNode(int relative, Primitive prev, int depth, String text) {
		int yOff = (int) (400 / Math.pow(2, depth));
        Offset offset;
        if(relative == 2) { //middle for start
        	offset = new Offset(10, 40, titleRecursion, AnimalScript.DIRECTION_SW);
        } else if (relative == 0) { //lower
        	offset = new Offset(50, yOff+10, prev, AnimalScript.DIRECTION_NE);
        } else if (relative == 1) { //upper
        	offset = new Offset(50, -yOff+10, prev, AnimalScript.DIRECTION_NE);
        } else {
        	throw new IllegalArgumentException("relative not between 0 and 2!");
        }
        
        TextProperties titleProps = new TextProperties();
		titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 12));
        Text textPrim = lang.newText(offset, text, null, null, titleProps);
        
        Rect rect = lang.newRect(new Offset(-10, -10, textPrim, AnimalScript.DIRECTION_NW), new Offset(10, 10, textPrim, AnimalScript.DIRECTION_SE), null, null);
        
        if(prev != null) {
        	Node[] vertices = { new Offset(0,0, prev, AnimalScript.DIRECTION_E), new Offset(0,0, rect, AnimalScript.DIRECTION_W)};
        	lang.newPolyline(vertices, null, null);
        }
        
        return rect;
	}
	
	private int partition(IntArray arr, int begin, int end) {
		int idx = begin + (end - begin) / 2;
		int p = arr.getData(idx);
		pMarker.move(idx, null, null);
		pMarker.show();
		
		int i = begin;
		int j = end;

		while(i <= j) {
			lang.nextStep();
			while(arr.getData(i) < p) {
				i++;
			}
			arr.highlightElem(i, null, defaultDuration);
			
			lang.nextStep();
			while(arr.getData(j) > p) {
				j--;
			}
			arr.highlightElem(j, null, defaultDuration);
			
			if(i <= j) {
				lang.nextStep();
				arr.swap(i, j, null, defaultDuration);
				if(i == idx)
					pMarker.move(j, null, null);
				if(j == idx)
					pMarker.move(i, null, null);
				
				lang.nextStep();
				arr.unhighlightElem(i, null, defaultDuration);
				arr.unhighlightElem(j, null, defaultDuration);
				i++;
				j--;
			} else {
				lang.nextStep();
				arr.unhighlightElem(i, null, defaultDuration);
				arr.unhighlightElem(j, null, defaultDuration);
			}
		}	
		
		return i-1;
	}
	
	private void heapsort(IntArray arr, SourceCode codeSupport) {
		//build heap
		codeSupport.highlight(15);
		
		lang.nextStep();
		codeSupport.toggleHighlight(15,16);
		int n = arr.getLength();
		
		lang.nextStep();
	    //Create marker to point on i (heapsort)
	    ArrayMarkerProperties arrayIMProps = new ArrayMarkerProperties();
	    arrayIMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
	    arrayIMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
	    
	    codeSupport.toggleHighlight(16,17);
	    ArrayMarker iMarker = lang.newArrayMarker(arr, n/2 - 1, "i", null, arrayIMProps);
	    
		for(; iMarker.getPosition() >= 0; iMarker.decrement(null, defaultDuration)) {			
			if(iMarker.getPosition() != n/2 - 1) codeSupport.unhighlight(18);
			loopCounter++;
			lang.nextStep();
			codeSupport.toggleHighlight(17,18);
			heapify(arr, n, iMarker.getPosition());
		}
		
		lang.nextStep();
		codeSupport.toggleHighlight(18,19);
		iMarker.hide();
		
		lang.nextStep();
		codeSupport.toggleHighlight(19,20);
		iMarker = lang.newArrayMarker(arr, n-1, "i", null, arrayIMProps);
		
		for(; iMarker.getPosition()>=0; iMarker.decrement(null, defaultDuration)) {
			loopCounter++;
			lang.nextStep();
			if(iMarker.getPosition() != n-1) codeSupport.unhighlight(22);
			codeSupport.toggleHighlight(20,21);
			arr.swap(0, iMarker.getPosition(), null, defaultDuration);
			
			lang.nextStep();
			codeSupport.toggleHighlight(21,22);
			heapify(arr, iMarker.getPosition(), 0);
		}
		
		lang.nextStep();
		iMarker.hide();
		codeSupport.toggleHighlight(22,23);
		
		lang.nextStep();
		codeSupport.toggleHighlight(23,24);
	}
	
	private void heapify(IntArray arr, int n, int i) {
		int largest = i;
		int l = 2*i + 1;
		int r = 2*i + 2;
		
		
		//left child larger
		if(l < n && arr.getData(l) > arr.getData(largest))
			largest = l;
		
		//right child larger
		if(r < n && arr.getData(r) > arr.getData(largest))
			largest = r;
		
		//restore heap if root is not largest
		if(largest != i) {
			arr.swap(i, largest, null, defaultDuration);
			lang.nextStep();
			heapify(arr, n, largest);
		}
	}
	
	
	/*
	 * returns a new array containing elements from given array starting at
	 * begin until end
	 */
	private IntArray copyOut(IntArray arr, int begin, int end) {
		int size = end-begin+1;
		int[] temp = new int[size];
		for(int i=0; i < size; i++) {
			temp[i] = arr.getData(begin+i);
		}
		
		IntArray res = lang.newIntArray(new Offset(20, 0, arr, AnimalScript.DIRECTION_NE), temp, "intArray2", null, arrayProps);
		return res;
	}
	
	/**
	 * Copies given array source into given array dest beginning from index begin
	 * */
	private void copyIn(IntArray dest, IntArray source, int begin) {
		for(int i=0; i < source.getLength(); i++) {
			dest.put(begin+i, source.getData(i), null, defaultDuration);
		}
	}
	
	
	public String getName() {
        return "Introsort";
    }

    public String getAlgorithmName() {
        return "Introsort";
    }

    public String getAnimationAuthor() {
        return "Simon Althaus, Florian Giger";
    }
    
    public String getDescription() {
    	return translator.translateMessage("description");
    }
    
    public String getCodeExample() {
    	return SOURCE_CODE;
    }
    
    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return locale;
    }
    
    public void setLanguage(Locale lang) {
		this.locale = lang;
		this.translator.setTranslatorLocale(this.locale);
	}

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }

	/*public static void main(String[] args) {
		
	    Generator s = new IntroSort();
	    //Animal.startGeneratorWindow(s);
	    
	    Hashtable<String, Object> primitives = new Hashtable<String, Object>();
	    int[] a = { 7, 3, 2, 4, 1, 13, 52, 13, 5, 1 };
	    primitives.put("intArray", a);
	    
	    AnimationPropertiesContainer props = new AnimationPropertiesContainer();
	    
	    SourceCodeProperties scProps = new SourceCodeProperties();
	    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
	    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
	        new Font("Monospaced", Font.PLAIN, 12));
	    
	    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
	    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
	    scProps.setName("sourceCode");
	    props.add(scProps);

	    ArrayProperties arrayProps = new ArrayProperties();
	    arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
	    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
	    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
	    arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
	    arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
	    arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW);
	    arrayProps.setName("array");
	    props.add(arrayProps);
	    
	    String code = s.generate(props, primitives);
	    
	    try (PrintWriter out = new PrintWriter("Introsort.asu")) {
			out.println(code);
			System.out.println("Written to Introsort.asu. Please open or reload Animal");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println(code);
		    Animal.startAnimationFromAnimalScriptCode(code);
		}
	}*/
}