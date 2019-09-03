package generators.sorting;

import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Vector;

import algoanim.animalscript.AnimalGenerator;
import algoanim.animalscript.AnimalScript;
import algoanim.counter.enumeration.ControllerEnum;
import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.TwoValueView;
import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.Group;
import algoanim.primitives.IntArray;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.Triangle;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.*;
import algoanim.properties.AnimationProperties;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CounterProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PointProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.TriangleProperties;
import algoanim.util.ArrayDisplayOptions;
import algoanim.util.Coordinates;
import algoanim.util.DisplayOptions;
import algoanim.util.Hidden;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;
import animal.variables.Variable;
import animal.variables.VariableRoles;

public class BitonicSort implements ValidatingGenerator {
	
	/**
	 * Reference to an instance of AnimalScript
	 */
	private Language lang;

    /**
     * The array that is being sorted
     */
    private IntArray array;
    
	/**
	 * The comparator network
	 */
	private BitonicSortNetwork sn;
	
	private TwoValueCounter counter;
	
	private TwoValueView counterView;
	
	private TextArea ta;
    /**
     * AnimationProperties for the array. Is set within {@link #generate(AnimationPropertiesContainer, Hashtable)}
     * with a copy
     * @see #getPropertiesCopy(AnimationProperties)
     */
    private ArrayProperties arrayProp;
    /**
     * AnimationProperties for the sorting network. Is set within {@link #generate(AnimationPropertiesContainer, Hashtable)}
     * with a copy
     * @see #getPropertiesCopy(AnimationProperties)
     */
    private MatrixProperties comparatorProp; 
    /**
     * AnimationProperties for the source code. Is set within {@link #generate(AnimationPropertiesContainer, Hashtable)}
     * with a copy
     * @see #getPropertiesCopy(AnimationProperties)
     */
    private SourceCodeProperties sourceCodeProp;
    /**
     * AnimationProperties for text. Is set within {@link #generate(AnimationPropertiesContainer, Hashtable)}
     * with a copy
     * @see #getPropertiesCopy(AnimationProperties)
     */
    private TextProperties textProp;
    
	/**
	 * The title element which is present on each step
	 */
	private Text title;
	
	/**
	 * A filled box which surround the {@link BitonicSort#title title}
	 */
	private Rect titleBox;
	
	/**
	 * Variables tracking the current values of the algorithm
	 */
	private Variables v;

	/**
	 * The source code with current line highlighted
	 */
	private SourceCode sc;
	
    /**
     * Constant for the Sorting direction of a sequence
     */
    private final static boolean ASCENDING=true, DESCENDING=false;
	
    /**
     * Duration
     */
    private static final int BLINK_DURATION = 40;
    
    /**
     * used for table of contents numeration
     */
    private int compareCounter;
    
	@Override
	public String getAlgorithmName() {
		return "Bitonic Sort";
	}

	@Override
	public String getAnimationAuthor() {
		return "Tobias Becker";
	}

	@Override
	public String getCodeExample() {
	    return 
			"private void bitonicSort(int lo, int n, boolean dir)\n" + 
		    "{\n" + 
		    "    if (n>1)\n" + 
		    "    {\n" + 
		    "        int m=n/2;\n" + 
		    "        bitonicSort(lo, m, ASCENDING);\n" + 
		    "        bitonicSort(lo+m, m, DESCENDING);\n" + 
		    "        bitonicMerge(lo, n, dir);\n" + 
		    "    }\n" + 
		    "}\n" + 
			"\n" + 
		    "private void bitonicMerge(int lo, int n, boolean dir)\n" + 
		    "{\n" + 
		    "    if (n>1)\n" + 
		    "    {\n" + 
		    "        int m=n/2;\n" + 
		    "        for (int i=lo; i<lo+m; i++)\n" + 
		    "            compare(i, i+m, dir);\n" + 
		    "        bitonicMerge(lo, m, dir);\n" + 
		    "        bitonicMerge(lo+m, m, dir);\n" + 
		    "    }\n" + 
		    "}\n";
	}

	@Override
	public Locale getContentLocale() {
		return Locale.US;
	}

	@Override
	public String getDescription() {
		return String.join(System.lineSeparator(),
				"Bitonic Sort or Bitonic mergesort", 
				"is a parallel sorting algorithm devised by Ken Batcher. Apart from sorting it is also used to construct a sorting network.",
				"This Generator shows the sorting of a sequence and a (finished) sorting network.");
	}

	@Override
	public String getFileExtension() {
		return "asu";
	}

	@Override
	public GeneratorType getGeneratorType() {
		int gtype = GeneratorType.GENERATOR_TYPE_SORT;
		return new GeneratorType(gtype);
	}

	@Override
	public String getName() {
		return "Bitonic Sort";
	}

	@Override
	public String getOutputLanguage() {
		return "Java";
	}

	@Override
	public void init() {
		lang = new AnimalScript("Bitonic Sort Animation", "Tobias Becker", 640, 480);
		lang.setStepMode(true);
		compareCounter = 0;
	}
	
	/**
	 * Creates a copy of the given <code>AnimationProperties</code> element or returns <code>null</code>
	 * if an instance cannot be instantiated with the standard constructor.
	 * <br>
	 * Circumvents a bug in <code>AnimationProperties</code> which prevents setting of a <code>EnumerationPropertyItem</code> correctly
	 * @param ap - a concrete AnimationProperties element that will be copied
	 * @return a copy of <code>ap</code> or null
	 * @see algoanim.properties.AnimationProperties
	 * @see algoanim.properties.items.EnumerationPropertyItem
	 */
	@SuppressWarnings("unchecked")
	private <P extends AnimationProperties> P getPropertiesCopy(P ap) {
		try {
			AnimationProperties p = ap.getClass().newInstance();
			ap.getAllPropertyNames().forEach(s -> {
				Object set = ap.get(s);
				if (set instanceof Vector<?>) {
					Vector<String> v = (Vector<String>)set;
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < v.size()-1; i++) {
						sb.append(v.get(i)).append(",");
					}
					sb.append(v.get(v.size()-1));
					set = sb.toString();
				}
				p.set(s, set);
			});
			return (P) p;
		} catch (Exception e) {
//			System.out.println("Properties Copy failed - " + e.getClass().getSimpleName());
		}
		return null;
	}

	@Override
	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		try {
			return generateInner(props, primitives);
		} catch (Exception e) {
			// debugging
			// e.printStackTrace();
		}
		return "";
	}
	
	private String generateInner(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		// primitives
		int[] a = ((int[])primitives.get("array")).clone();
		// properties
	    arrayProp = getPropertiesCopy((ArrayProperties) props.getPropertiesByName("arrayProperties"));
	    comparatorProp = getPropertiesCopy((MatrixProperties) props.getPropertiesByName("comparatorProperties"));
	    sourceCodeProp = getPropertiesCopy((SourceCodeProperties) props.getPropertiesByName("sourceCodeProperties"));
	    textProp = getPropertiesCopy((TextProperties) props.getPropertiesByName("textProperties"));
	    // title
	    TextProperties titleProps = new TextProperties();
	    titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 20));
		title = lang.newText(new Coordinates(300, 20), "Bitonic Sort", "title", null, titleProps);
	    RectProperties titleBoxProps = new RectProperties();
	    titleBoxProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
	    titleBoxProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.yellow);
	    titleBoxProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
	    // we use titleBox as our central anchor for offsets
		titleBox = lang.newRect(new Offset(-5, -4, title, "NW"), new Offset(5, 4, title, "SE"), "titleBox", null, titleBoxProps);

		sn = new BitonicSortNetwork(a.length, lang, new Offset(-280, 30, titleBox, "SW"), comparatorProp);
    	array = lang.newIntArray(sn.getOffset(30, 30, "SW"), a, "array", null, arrayProp);
    	array.hide();
    	sn.hide();
		// introduction
		introduction(a);
		
		// sort
		bitonicSort(0, array.getLength(), ASCENDING);
		
		// summary
		summary();
		
		lang.finalizeGeneration(); // works without, just here to make sure
		
		return lang.toString();
	}

	/**
	 * Encapsulates the introduction of the animation
	 * @param the array which later will be sorted
	 */
	private void introduction(int[] a) {
		// short text explaining what happens
		String descr = String.join(System.lineSeparator(),
			    "Bitonic sort is one of the fastest sorting networks.",
			    "A sorting network is a special kind of sorting algorithm,",
			    "where the sequence of comparisons is not data-dependent.",
			    "This makes sorting networks suitable for implementation in",
			    "hardware or in parallel processor arrays.");
	    ta = new TextArea(lang, new Offset(-40, 10, titleBox, "SW"), 10, descr, "description", null, textProp);
		sc = lang.newSourceCode(new Offset(-280, 20, titleBox, "SW"), "sourceCode", null, sourceCodeProp);
		sourceCode();
		sc.hide();
	    lang.nextStep("Introduction");
	    array.show(new TicksTiming(200));
	    sn.show(new TicksTiming(200));
	    ta.moveTo("NW", null, sn.getOffset(50, 0, "NE"), null, new TicksTiming(200));
	    ta.setText(String.join(System.lineSeparator(),
				"This is the array that will be sorted and", 
				"the sorting network that shows all comparisons", 
				"between elements in the array."), null, null);
		// explain the comparator network

		lang.nextStep();
		ta.setText(String.join(System.lineSeparator(),
				"Each line represents one cell or element within our array.", 
				"Each arrow represents one comparison, the direction of the arrow",
				"signals the direction of the possible swap, i.e. after a swap the arrow",
				"points at the larger element."), null, null);
		arrayProp.set(AnimationPropertiesKeys.DIRECTION_PROPERTY, true);
		Offset newArrayPos = sn.getOffset(-10, -(BitonicSortNetwork.LINE_SPACING/2), "NW");
        array.moveTo("NE", null, newArrayPos, null, new TicksTiming(200));
        array.hide(new TicksTiming(200));
        array = lang.newIntArray(newArrayPos, a, "array", new ArrayDisplayOptions(new TicksTiming(200), new TicksTiming(200), false), arrayProp);
        sn.moveBy(null, 10, 0, new TicksTiming(200), new TicksTiming(200)); // aligns it with the array
        
        lang.nextStep();
		ta.setText(String.join(System.lineSeparator(),
				"The complete sorting network is build out of smaller comparator", 
				"networks. Each of it takes two smaller (by a power of 2) sorted",
				"arrays and sorts them. It not necessarily sorts them in ascending",
				"order"), null, null);
		for (int x = 1; x <= (a.length/2); x*=2) {
			sn.highlightNetwork(x, null);
		}
		
		lang.nextStep();
    	counter = lang.newCounter(array);
    	counter.deactivateCounting();
    	CounterProperties cp = new CounterProperties();
    	cp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
    	cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.gray);
    	cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    	counterView = lang.newCounterView(counter, new Offset(0, 20, array, "S"), cp, true, true);
    	ta.setText(String.join(System.lineSeparator(),
    			"We will also keep track of our accesses and assignments on the array.",
				"Afterwards we will use it to find a complexity of this algorithm."), null, null);
		
		lang.nextStep();
		counterView.hide();
		sn.hide();
	    sn.unhighlightNetwork(null);
		array.hide();
		sc.show();
		ta.setText(String.join(System.lineSeparator(),
				"This is the complete source code of the algorithm we use.",
				"bitonicSort gets 2 integer values. The first (lo) denotes the",
				"starting point of the sequence, the second (n) the length of it.",
				"bitonicSort always splits the sequence in two halves of length n/2. One",
				"starting at lo, the other starting at lo+n/2.",
				"We keep track of those in the Variables-Window!"), null, null);
		ta.moveTo("NW", null, new Offset(50, 0, sc, "NE"), null, new TicksTiming(200));
		sc.highlight(0);
		sc.highlight(7);
		v = lang.newVariables();
    	v.declare("int", "lo", "0", "walker");
    	v.declare("int", "n", String.valueOf(array.getLength()), Variable.getRoleString(VariableRoles.WALKER));
    	v.declare("String", "dir", "true", Variable.getRoleString(VariableRoles.WALKER));
    	v.declare("int", "\"n/2\"", String.valueOf(array.getLength()/2), Variable.getRoleString(VariableRoles.UNKNOWN)); // should be TRANSFORMATION
    	v.declare("int", "\"lo+n/2\"", String.valueOf(array.getLength()/2), Variable.getRoleString(VariableRoles.UNKNOWN)); // should be TRANSFORMATION
		
    	lang.nextStep();
		sc.unhighlight(0);
		sc.unhighlight(7);
    	sc.highlight(12);
    	sc.highlight(16, 0, true);
		ta.setText(String.join(System.lineSeparator(),
				"The current line in the algorithm is highlighted in the source",
				"code. Currently it would be bitonicMerge.",
				"A if- or for-condition that is not taken is highlighted in a",
				"different color. Currently this would be the if in compare.",
				"Because this algorithm his highly recursive, we will indicate a",
				"return of a recursive call with short blinking of the code line we",
				"returned to. We will illustrate this in the next step."), null, null);
    	
		lang.nextStep();
    	sc.unhighlight(12);
    	sc.unhighlight(16, 0, true);
		blink(3);

		lang.nextStep();
		sc.unhighlight(3);
//		ta.hide();
		sc.moveTo("NW", null, new Offset(20, 30, titleBox, "S"), null, new TicksTiming(200));
		array.show(new TicksTiming(200));
		sn.show(new TicksTiming(200));
		counterView.show(new TicksTiming(200));
		ta.moveTo("NW", null, new Offset(0, 75, array, "S"), null, null); // move under counterView to prepare explanation during algorithm
		ta.setText(String.join(System.lineSeparator(),
				"Bitonic Sort works in 3 steps:",
				"1. 2xbitonicSort in bitonicSort: Split the sequence in half",
				"and sort both parts: one ascending, the other descending",
				"2. For-Loop: Make sure that every element in one subsequence",
				"is larger than every element in the other - depends on direction",
				"3. 2xbitonicMerge in bitonicMerge: Do (2) for both subsequences"), null, null);
	    lang.nextStep("Algorithm Start");
	}

	/**
	 * Encapsulates the summary of the animaton
	 */
	private void summary() {
		sc.hide();
		array.hide();
		sn.hide();
		counterView.hide();
		counterView = lang.newCounterView(counter, new Offset(-280, 30, titleBox, "SW"), new CounterProperties(), true, false);
		counterView.update(ControllerEnum.access, counter.getAccess());
		counterView.update(ControllerEnum.assignments, counter.getAssigments());
		ta.show();
		ta.setText(String.join(System.lineSeparator(),
				"We are now finished and the array is sorted.",
				"It took " + counter.getAccess() + " accesses on the array and",
				counter.getAssigments() + " assignments. That are 2 accesses for",
				"every compare and 2 assignments for every swap.",
				"",
				"To form a sorted sequence of length n from two sorted sequences of length n/2,",
				"there are log(n) comparator stages required. Because we have to sort the n/2",
				"sequences first, the number of comparator stages T(n) for the entire network is:",
				"T(n) = log(n)+T(n/2) = log(n)+log(n)-1+log(n)-2+...+1 = log(n)*(log(n)+1)/2",
				"With n/2 comparatos per stage Bitonic Sort needs Θ(n·log(n)²) comparisons"
				), null, null);
		ta.moveTo("NW", null, new Offset(-30, 30, titleBox, "S"), null, null);
		
		lang.nextStep("Summary");

	}
	/**
	 * adds the source code to {@link sc}<br>
	 * <code>sc</code> must already been initialized!
	 */
	private void sourceCode() {
/* 0 */ sc.addCodeLine("private void bitonicSort(int lo, int n, boolean dir) {", null, 0, null);
/* 1 */ sc.addCodeLine("if (n>1) {", null, 1, null);
/* 2 */ sc.addCodeLine("bitonicSort(lo, n/2, ASCENDING);", null, 2, null);
/* 3 */ sc.addCodeLine("bitonicSort(lo+n/2, n/2, DESCENDING);", "bitonicSort2", 2, null);
/* 4 */ sc.addCodeLine("bitonicMerge(lo, n, dir);", "bitonicSort3", 2, null);
/* 5 */ sc.addCodeLine("}", null, 1, null);
/* 6 */ sc.addCodeLine("}", null, 0, null);
/* 7 */ sc.addCodeLine("private void bitonicMerge(int lo, int n, boolean dir) {", null, 0, null);
/* 8 */ sc.addCodeLine("if (n>1) {", null, 1, null);
/* 9 */ sc.addCodeLine("for (int i=lo; i<lo+n/2; i++)", null, 2, null);
/* 10 */sc.addCodeLine("compare(i, i+n/2, dir);", null, 3, null);
/* 11 */sc.addCodeLine("bitonicMerge(lo, n/2, dir);", null, 2, null);
/* 12 */sc.addCodeLine("bitonicMerge(lo+n/2, n/2, dir);", null, 2, null);
/* 13 */sc.addCodeLine("}", null, 1, null);
/* 14 */sc.addCodeLine("}", null, 0, null);
/* 15 */sc.addCodeLine("private void compare(int i, int j, boolean dir) {", null, 0, null);
/* 16 */sc.addCodeLine("if (dir==(array[i]>array[j])) {", null, 1, null);
/* 17 */sc.addCodeLine("swap(i,j);", null, 2, null);
/* 18 */sc.addCodeLine("}", null, 1, null);
/* 19 */sc.addCodeLine("}", null, 0, null);
	}
	
  
    /**
     * highlights the subsequence of the array
     * @param from the lower bound of the sequence
     * @param len the length of the sequence
     * @param dir 
     */
    private void highlightArray(int from, int len) {
		array.unhighlightCell(0, array.getLength()-1, null, null);
		array.highlightCell(from, from+len-1, null, null);
    }
    
    private void updateVariables(int from, int len, boolean dir) {
    	v.set("lo", String.valueOf(from));
    	v.set("n", String.valueOf(len));
    	v.set("dir", String.valueOf(dir));
    	v.set("\"n/2\"", String.valueOf(len/2));
    	v.set("\"lo+n/2\"", String.valueOf(from+len/2));
    }
    
    private void updateAnimation(int from, int len, boolean dir) {
    	highlightArray(from, len);
    	updateVariables(from, len, dir);
    }
    
    private void blink(int i) {
    	sc.highlight(i, 0, false, null, new TicksTiming(BLINK_DURATION/2));
    	for (int x = BLINK_DURATION; x < BLINK_DURATION*5; x+=BLINK_DURATION) {
    		sc.highlight(i, 0, false, new TicksTiming(x), new TicksTiming(BLINK_DURATION/2));
    	}
	}
    
    private void returnFromMethodCall(int codeLine, int from, int len, boolean dir, boolean lastLine) {
    	updateAnimation(from, len, dir); // restore highlighting of array
    	blink(codeLine); // blink the codeLine to indicate recursive backtracking
    	lang.nextStep();
    	if (!lastLine) {
	        sc.toggleHighlight(codeLine, codeLine+1); // highlight next line since its not the last line
	        lang.nextStep();
	        sc.unhighlight(codeLine+1); // unhighlight the next line (in every case its a new method call)
    	} else
    		sc.unhighlight(codeLine); // just unhighlight the line, its the last line
    }
    
    private String makeSqn(int lo, int n) {
    	return "[" + lo + "," + (lo+n) + "]";
    }
    
    private void bitonicSortExpl(int lo, int n, boolean dir, boolean first, boolean second, boolean third) {
		ta.setText(String.join(System.lineSeparator(),
				"Our current sequence is " + makeSqn(lo, n),
				"First we sort(bitonicSort) the subsequences",
				makeSqn(lo, n/2) + " ascending" + (first ? " - done!" : ""),
				makeSqn(lo+n/2, n/2) + " descending" + (second ? " - done!" : ""),
				"Then we merge(bitonicMerge) the whole sequence") + (third ? " - done!" : ""), null, null);
		if (first)
			ta.highlightLine(2);
		if (second)
			ta.highlightLine(3);
		if (third)
			ta.highlightLine(4);
    }
    
    private void bitonicSort(int lo, int n, boolean dir) {
    	ta.show();
		ta.setText(String.join(System.lineSeparator(),
				"Our current sequence is " + makeSqn(lo, n)), null, null);
    	sc.highlight(0);
    	updateAnimation(lo, n, dir);
    	lang.nextStep("bitonicSort " + makeSqn(lo,n));  // TABLE OF CONTENTS
    	sc.toggleHighlight(0, 1); // highlight if
        if (n>1) {
        	bitonicSortExpl(lo, n, dir, false, false, false);
        	int m = n/2;
        	lang.nextStep();
        	sc.toggleHighlight(1, 2); // highlight 1. bitonicSort
        	lang.nextStep();
        	sc.unhighlight(2);
        	bitonicSort(lo, m, ASCENDING);
        	bitonicSortExpl(lo, n, dir, true, false, false);
        	returnFromMethodCall(2, lo, n, dir, false);
        	bitonicSort(lo+m, m, DESCENDING);
        	bitonicSortExpl(lo, n, dir, true, true, false);
        	returnFromMethodCall(3, lo, n, dir, false);
            bitonicMerge(lo, n, dir);
            bitonicSortExpl(lo, n, dir, true, true, true);
        } else {
    		ta.setText(String.join(System.lineSeparator(),
    				"Our current sequence is " + makeSqn(lo, n),
    				"The sequence length is 1, so it's already sorted!"), null, null);
        	sc.highlight(1, 0, true);
        	lang.nextStep();
        	sc.unhighlight(1);
        }
    }
    
	private void bitonicMerge(int lo, int n, boolean dir) {
		if (n>1)
			ta.setText(String.join(System.lineSeparator(),
				"Our current sequence is " + makeSqn(lo, n),
				makeSqn(lo, n/2) + " should now be sorted ascending",
				"and " + makeSqn(lo+n/2, n/2) + " should be now sorted descending."), null, null);
		else
			ta.setText(String.join(System.lineSeparator(),
					"Our current sequence is " + makeSqn(lo, n)), null, null);
		sc.highlight(7);
		updateAnimation(lo, n, dir);
    	lang.nextStep();
    	sc.toggleHighlight(7, 8); // highlight if
        if (n>1) {
    		ta.setText(String.join(System.lineSeparator(),
    				"Our current sequence is " + makeSqn(lo, n),
    				"First step: Make sure that every element in " + (dir ? makeSqn(lo+n/2, n/2) : makeSqn(lo, n/2)),
    				"is larger than every element in " + (dir ? makeSqn(lo, n/2) : makeSqn(lo+n/2, n/2)),
    				"Important: This could fail if we wouldn't make sure that the",
    				"second sequence is sorted in descending order!"), null, null);
        	int m = n/2;
        	lang.nextStep();
            for (int i=lo; i<lo+m; i++) {
            	sc.toggleHighlight(8, 9); // highlight for
            	lang.nextStep();
            	sc.toggleHighlight(9, 10); // highlight compare
            	lang.nextStep();
            	sc.unhighlight(10); // unhighlight compare
                compare(i, i+m, dir);
                returnFromMethodCall(10, lo, n, dir, true);
            }
        	sc.highlight(9, 0, true); // for-loop is finished
        	lang.nextStep();
    		ta.setText(String.join(System.lineSeparator(),
    				"Our current sequence is " + makeSqn(lo, n),
    				"Now we merge both subsequences again, both in descending",
    				"order, then we are finished (with sequence " + makeSqn(lo, n) + ")"), null, null);
        	sc.unhighlight(9, 0, true);
            sc.highlight(11); // highlight 1. bitonicMerge
            lang.nextStep();
            sc.unhighlight(11);
            bitonicMerge(lo, m, dir);
    		ta.setText(String.join(System.lineSeparator(),
    				"Our current sequence is " + makeSqn(lo, n),
    				"Now we merge both subsequences again, both in descending",
    				"order, then we are finished (with sequence " + makeSqn(lo, n) + ")",
    				makeSqn(lo, m) + " - merged!"), null, null);
    		ta.highlightLine(3);
            returnFromMethodCall(11, lo, n, dir, false);
            bitonicMerge(lo+m, m, dir);
    		ta.setText(String.join(System.lineSeparator(),
    				"Our current sequence is " + makeSqn(lo, n),
    				"Now we merge both subsequences again, both in descending",
    				"order, then we are finished (with sequence " + makeSqn(lo, n) + ")",
    				makeSqn(lo, m) + " - merged!",
    				makeSqn(lo+m, m) + " - merged!"), null, null);
    		ta.highlightLine(3);
    		ta.highlightLine(4);
            returnFromMethodCall(12, lo, n, dir, true);
        } else {
    		ta.setText(String.join(System.lineSeparator(),
    				"Our current sequence is " + makeSqn(lo, n),
    				"The sequence length is 1, so it's already merged!"), null, null);
        	sc.highlight(8, 0, true);
        	lang.nextStep();
        	sc.unhighlight(8);
        }
    }
    
    /**
     * compares two elements of the array and swaps them in the indicated direction<br>
     * - highlights the elements in the array<br>
     * - shows both elements in the variables window<br>
     * - highlights the comparison in the sorting network<br>
     * - adds a entry to the table of contents<br>
     * @param i
     * @param j
     * @param dir
     */
    private void compare(int i, int j, boolean dir) {
    	v.openContext(); // new variables context, its only valid within the compare
    	v.declare("int", "i", String.valueOf(i), Variable.getRoleString(VariableRoles.TEMPORARY));
    	v.declare("int", "j", String.valueOf(j), Variable.getRoleString(VariableRoles.TEMPORARY));
    	v.declare("String", "dir", String.valueOf(dir), Variable.getRoleString(VariableRoles.TEMPORARY));
    	if (dir)
        	sn.highlightArrow(i, j);
    	else
        	sn.highlightArrow(j, i);
    	array.highlightElem(i, null, null);
    	array.highlightElem(j, null, null);
    	sc.highlight(15); // highlight method compare
    	lang.nextStep("compare #" + ++compareCounter); // TABLE OF CONTENTS
    	sc.toggleHighlight(15, 16); // highlight if
    	counter.accessInc(2);
        if (dir==(array.getData(i)>array.getData(j))) {
        	lang.nextStep();
        	sc.toggleHighlight(16, 17); // highlight swap
            array.swap(i, j, null, new TicksTiming(150));
            counter.assignmentsInc(2);
            lang.nextStep();
            sc.unhighlight(17);
        } else {
        	sc.highlight(16, 0, true);
        	lang.nextStep();
        	sc.unhighlight(16);
        }
    	array.unhighlightElem(i, null, null);
    	array.unhighlightElem(j, null, null);
    	sn.finishLastHighlighted();
    	v.closeContext(); // we return to our normal variables
    }

	/**
	 * Only allows array sizes 2, 4 and 8
	 * @see generators.framework.ValidatingGenerator#validateInput(generators.framework.properties.AnimationPropertiesContainer, java.util.Hashtable)
	 */
	@Override
	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		int[] a = (int[])primitives.get("array");
		if (a.length == 0 || ((a.length & (a.length - 1)) != 0) || a.length > 8)
			throw new IllegalArgumentException("Size should be 2, 4 or 8");
		return true;
	}
}

class TextArea extends AnimalGenerator {
	private Node upperLeft;
	private String name;
	private DisplayOptions display;
	private TextProperties tp;
	private Language lang;
	private LinkedList<Primitive> allLines = new LinkedList<Primitive>();
	private Group group;
	private int maxLines;
	
	TextArea(Language lang, Node upperLeft, int maxLines, String text, String name, DisplayOptions display, TextProperties tp) {
		super(lang);
		this.upperLeft = upperLeft;
		setName(name);
		setDisplayOptions(display);
		this.tp = tp;
		this.lang = lang;
		this.maxLines = maxLines;
		createLines();
		setText(text, null, null);
	}
	
	public void highlightLine(int l) {
		if (l < allLines.size()) {
			((Text) allLines.get(l)).changeColor(null, Color.green, null, null);
		}
	}
	
	private void createLines() {
		for (int i = 0; i < maxLines; i++) {
			Node pos;
			if (i == 0) {
				pos = upperLeft;
			} else {
				pos = new Offset(0, 7, allLines.get(i-1), "SW");
			}
			allLines.add(lang.newText(pos, "", name + i, display, tp));
		}
	}
	
	public void setText(String text, Timing delay, Timing duration) {
		String[] lines = text.split(System.getProperty("line.separator"));
		if (lines.length > allLines.size())
			throw new IllegalArgumentException("Too much Text for this TextArea (Maximum: " + maxLines + ")");
		for (int i = 0; i < allLines.size(); i++) {
			((Text) allLines.get(i)).changeColor(null, (Color) tp.get(AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
			if (i < lines.length)
				((Text) allLines.get(i)).setText(lines[i], delay, duration);
			else {
				((Text) allLines.get(i)).setText("", delay, duration);
			}
		}
		group = lang.newGroup(allLines, name);
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(String newName) {
		if (newName != null)
			this.name = newName;
	}

	public DisplayOptions getDisplayOptions() {
		return this.display;
	}

	private void setDisplayOptions(DisplayOptions displayOptions) {
		this.display = displayOptions;
	}

	public void exchange(Primitive q) {
		group.exchange(q);
	}

	public void rotate(Primitive around, int degrees, Timing t, Timing d) {
		group.rotate(around, degrees, t, d);
	}

	public void rotate(Node center, int degrees, Timing t, Timing d) {
		group.rotate(center, degrees, t, d);
	}

	public void changeColor(String colorType, Color newColor, Timing t, Timing d) {
		group.changeColor(colorType, newColor, t, d);
	}

	public void moveVia(String direction, String moveType, Primitive via,
			Timing delay, Timing duration) throws IllegalDirectionException {
		group.moveVia( direction, moveType, via, delay, duration);
	}

	public void moveBy(String moveType, int dx, int dy, Timing delay,
			Timing duration) {
		group.moveBy(moveType, dx, dy, delay, duration);
	}

	public void moveTo(String direction, String moveType, Node target,
			Timing delay, Timing duration) throws IllegalDirectionException {
		group.moveTo(direction, moveType, target, delay, duration);
	}

	public void show(Timing t) {
		group.show(t);
	}

	public void show() {
		show(null);
	}

	public void hide(Timing t) {
		group.hide(t);
	}

	public void hide() {
		hide(null);
	}
}

class BitonicSortNetwork {
	private int size;
	private Language lang;
	private Primitive basePoint;
	/**
	 * We use only the following properties:
	 * color -> everything is initial in this color
	 * elementColor -> a comparison to be made
	 * elementHighlight -> a comparison already made
	 * cellHighlight -> to highlight a comparator network
	 */
	private MatrixProperties prop;
	public final Color COLOR;
	public final Color ELEMENT_COLOR;
	public final Color ELEMENT_HIGHLIGHT;
	public final Color CELL_HIGHLIGHT;
	
	public final static int LINE_SPACING = 21;
	public final static int TRIANGLE_SIZE = 5;
	public final static int COMPARE_INNER_SPACING = 10;

	private LinkedList<Primitive> allElements = new LinkedList<Primitive>();
	
	/**
	 * This Network is captured as an animal-group
	 */
	private Group g;
	
	public final int COMPARE_SPACING;
	
	/**
	 * Properties for Arrowhead + Lines to use (user-defined) colors
	 */
	private TriangleProperties tp;
	private PolylineProperties plp;
	
	public BitonicSortNetwork(int size, Language lang, Node node, MatrixProperties comparatorProp) {
		this.size = size;
		this.lang = lang;
		this.basePoint = lang.newPoint(node, "bitonicBasepoint", new Hidden(), new PointProperties());
		this.prop = comparatorProp;
		COMPARE_SPACING = COMPARE_INNER_SPACING * (size/2 + 1);
		COLOR = (Color)prop.get(AnimationPropertiesKeys.COLOR_PROPERTY);
		ELEMENT_COLOR = (Color)prop.get(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY);
		ELEMENT_HIGHLIGHT = (Color)prop.get(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY);
		CELL_HIGHLIGHT  = (Color)prop.get(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY);
		tp = new TriangleProperties();
		tp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		tp.set(AnimationPropertiesKeys.FILL_PROPERTY, COLOR);
		tp.set(AnimationPropertiesKeys.COLOR_PROPERTY, COLOR);
		plp = new PolylineProperties();
		plp.set(AnimationPropertiesKeys.COLOR_PROPERTY, COLOR);
		
		layout();
	}
	
	private String[] compares = new String[]{
	/* 0 */	"",
	/* 2 */	"0,1",
	/* 4 */	"0,1 3,2 0,2 1,3 0,1 2,3",
	/* 8 */	"0,1 3,2 4,5 7,6 0,2 1,3 6,4 7,5 0,1 2,3 5,4 7,6 0,4 1,5 2,6 3,7 0,2 1,3 4,6 5,7 0,1 2,3 4,5 6,7"};
	
	public void hide() {
		g.hide();
	}
	
	public void show(Timing t) {
		g.show(t);
	}
	
	private int movedByX = 0;
	private int movedByY = 0;
	
	public void moveBy(String moveType, int dx, int dy, Timing delay, Timing duration) {
		movedByX += dx;
		movedByY += dy;
		g.moveBy(moveType, dx, dy, delay, duration);
	}
	
	public Offset getOffset(int xCoordinate, int yCoordinate, String targetDirection) {
		switch (targetDirection) {
		case AnimalScript.DIRECTION_C:
		case AnimalScript.DIRECTION_E:
		case AnimalScript.DIRECTION_W:
			Node l = new Offset(0, ((size-1)/2)*LINE_SPACING, basePoint, "C");
			Node r = new Offset(LINE_WIDTH, ((size-1)/2)*LINE_SPACING, basePoint, "C");
			Polyline temp = lang.newPolyline(new Node[]{l,r}, "bsnLine-center", new Hidden());
			return new Offset(xCoordinate, yCoordinate, temp, targetDirection); 
		case AnimalScript.DIRECTION_S:
		case AnimalScript.DIRECTION_SW:
		case AnimalScript.DIRECTION_SE:
			return new Offset(xCoordinate, yCoordinate, "bsnLine" + (size-1), targetDirection);
		case AnimalScript.DIRECTION_N:
		case AnimalScript.DIRECTION_NW:
		case AnimalScript.DIRECTION_NE:
		default:
			return new Offset(xCoordinate, yCoordinate, "bsnLine" + 0, targetDirection);
		}
	}
	
	private int LINE_WIDTH;
	
	public void layout() {
		String comp = compares[(int) (Math.log10(size)/Math.log10(2))];
		String[] pairCompares = comp.split(" ");
		for (int i = 0; i < pairCompares.length; i++) {
			String[] p = pairCompares[i].split(",");
			int x = Integer.parseInt(p[0]);
			int y = Integer.parseInt(p[1]);
			int spalte = (i / (size / 2));
			
			int j = i - 1;
			int overlap = 0;
			while (j >= 0 && (int)(j / (size / 2)) == spalte) {
				String[] pOld = pairCompares[j].split(",");
				int xOld = Integer.parseInt(pOld[0]);
				int yOld = Integer.parseInt(pOld[1]);
				if (doOverlap(x, y, xOld, yOld))
					overlap++;
				j--;
			}
			addArrow(x, y, spalte, overlap);
		}
		LINE_WIDTH = (pairCompares.length / (size / 2)) * COMPARE_SPACING;
		for (int i = 0; i < size; i++) {
			Node l = new Offset(0, i*LINE_SPACING, basePoint, "E");
			Node r = new Offset(LINE_WIDTH, i*LINE_SPACING, basePoint, "E");
			allElements.add(lang.newPolyline(new Node[]{l, r}, "bsnLine" + i, null, plp));
		}
		g = lang.newGroup(allElements, "BitonicSortNetwork");
	}
	
	private boolean doOverlap(int x1, int y1, int x2, int y2) {
		int min1 = Math.min(x1, y1);
		int max1 = Math.max(x1, y1);
		int min2 = Math.min(x2, y2);
		int max2 = Math.max(x2, y2);
		return min2 >= min1 && min2 <= max1 ||
				max2 >= min1 && max2 <= max1 ||
				min2 <= min1 && max2 >= max1;
	}
	
	class ArrowWrapper {
		Polyline p;
		Triangle t;
		private int i;
		private int j;
		ArrowWrapper(Polyline p, Triangle t, int i, int j) {
			this.p = p;
			this.t = t;
			this.i = i;
			this.j = j;
		}
		
		int getFrom() {
			return i;
		}
		int getTo() {
			return j;
		}
		
		void highlight() {
			p.changeColor("", ELEMENT_COLOR, null, null);
			t.changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, ELEMENT_COLOR, null, null);
			t.changeColor(AnimalScript.COLORCHANGE_COLOR, ELEMENT_COLOR, null, null);
		}
		void finished() {
			p.changeColor("", ELEMENT_HIGHLIGHT, null, null);
			t.changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, ELEMENT_HIGHLIGHT, null, null);
			t.changeColor(AnimalScript.COLORCHANGE_COLOR, ELEMENT_HIGHLIGHT, null, null);
		}
	}
	
	LinkedList<ArrowWrapper> al = new LinkedList<ArrowWrapper>();
	
	private void addArrow(int i, int j, int spalte, int overlap) {
		Node top = new Offset(COMPARE_SPACING/2+(COMPARE_SPACING*(spalte))+(COMPARE_INNER_SPACING*overlap), i*LINE_SPACING, basePoint, "C");
		Node bottom = new Offset(COMPARE_SPACING/2+(COMPARE_SPACING*(spalte))+(COMPARE_INNER_SPACING*overlap), j*LINE_SPACING, basePoint, "C");
		Polyline pl = lang.newPolyline(new Node[]{top, bottom}, "bsnArrowLine" + spalte + "+" + i, null, plp);
		allElements.add(pl);
		int yCoord = i < j ? 0-TRIANGLE_SIZE : TRIANGLE_SIZE;
		String downOrUp = i < j ? "S" : "N";
		Node leftCorner = new Offset(0-TRIANGLE_SIZE, yCoord, pl, downOrUp);
		Node rightCorner = new Offset(TRIANGLE_SIZE, yCoord, pl, downOrUp);
		Triangle t = lang.newTriangle(leftCorner, rightCorner, bottom, "bsnArrow"  + spalte + "+" + i, null, tp);
		allElements.add(t);
		
		al.add(new ArrowWrapper(pl, t, i, j)); 
	} 
	
	private ArrowWrapper highlighted;
	
	public void highlightArrow(int from, int to) {
		ArrowWrapper elem = null;
		int i = 0;
		while (elem == null) {
			if (i >= al.size())
				throw new NoSuchElementException("Can't find an arrow from '" + from + "' to '" + to + "'.");
			ArrowWrapper test = al.get(i);
			if (test.getFrom() == from && test.getTo() == to)
				elem = al.remove(i);
			i++;
		}
		elem.highlight();
		highlighted = elem;
	}
	
	public void finishLastHighlighted() {
		highlighted.finished();
		highlighted = null;
	}
	
	private Set<Rect> highlighter = new HashSet<Rect>();
	public void highlightNetwork(int n, Timing d) {
		int j = 0;
		LinkedList<ArrowWrapper> elem = new LinkedList<ArrowWrapper>();
		int i = 0;
		while (j < n) {
			if (i >= al.size())
				throw new NoSuchElementException("No such network length (given: " + n + ")");
			ArrowWrapper test = al.get(i);
			if (test.getTo() - test.getFrom() == n) {
				elem.add(test);
				j++;
			}
			i++;
		}
		LinkedList<Primitive> temp1 = new LinkedList<Primitive>();
		elem.forEach(aw -> temp1.add(aw.p));
		Group temp2 = lang.newGroup(temp1, "tmpGrp");

		RectProperties rp = new RectProperties();
		rp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rp.set(AnimationPropertiesKeys.FILL_PROPERTY, CELL_HIGHLIGHT);
		rp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
		highlighter.add(lang.newRect(new Offset(-(2*TRIANGLE_SIZE)+movedByX, -(2*TRIANGLE_SIZE)+movedByY, temp2, "NW"), new Offset((2*TRIANGLE_SIZE)+movedByX, (2*TRIANGLE_SIZE)+movedByY, temp2, "SE"), "highlight" + n, d, rp));
	}

	public void unhighlightNetwork(Timing t) {
		if (highlighter != null && !highlighter.isEmpty())
			highlighter.forEach(h -> h.hide(t));
	}
}
