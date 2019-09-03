/*
 * PPBSGenerator.java
 * Bekir �zkara, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
//package generators.sorting;
package generators.sorting;

import java.awt.Color;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.TwoValueView;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CounterProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.sorting.helperPpbsGenerator.Manager;
import translator.TranslatableGUIElement;
import translator.Translator;

public class PPBSGenerator implements Generator {
    private Language lang;
    
    // algorithm related
    private int pivot;
    private int[] intArray;
    
 	Timing timing = new TicksTiming(15);
 	SourceCode scInit;
 	SourceCode scPartition;
 	
 	// m1 - 1 	 	elements in list are less than pivot
 	// m2 - m1 		elements are equal to pivot
 	// n - m2 + 1 	elements greater than pivot (where n = list.size()-1)
 	private ArrayMarker m1; 
 	private ArrayMarker m2; 
 	
 	// before and after each iteration: 
 	// 1.	1 <= i1 <= m1 <= i2 <= m2 <= i3 <= n+1
 	// 2.	S[j] < pivot for j in (1, ..., i1-1),  if i1 < m1: S[i1] >= pivot
 	// 3. 	S[j] = pivot for j in (m1, ..., i2-1), if i2 < m2: S[i2] != pivot
 	// 4.	S[j] > pivot for j in (m2, ..., i3-1), if i3 <= n: S[i3] <= pivot
 	private ArrayMarker i1;
 	private ArrayMarker i2;
 	private ArrayMarker i3;	
 	
 	// j is used in the for loops for setting m1, m2
 	private ArrayMarker j;
 	
 	
    
    // array marker props
    private ArrayMarkerProperties m1MarkerProps;
    private ArrayMarkerProperties m2MarkerProps;
    private ArrayMarkerProperties i1MarkerProps;
    private ArrayMarkerProperties i2MarkerProps;
    private ArrayMarkerProperties i3MarkerProps;
    private ArrayMarkerProperties jMarkerProps;
    // array props
    private ArrayProperties arrayProps;
    // text props (general)
    private TextProperties textProps;
    // source code props
    private SourceCodeProperties sourceCodeProps;
    // box props with text
    private RectProperties boxProps;
    private TextProperties textBoxProps;
    // pivot and result text props
    private TextProperties pivotTextProps;
    private TextProperties resultTextProps;
    // # iterations text
    private TextProperties iterationTextProps;
    // counter props
    private CounterProperties counterProps;
    
    // own values
 		private Color i1Color;
 		private Color i2Color;
 		private Color i3Color;
 		
 		private TwoValueCounter counter;
 		@SuppressWarnings("unused")
    private TwoValueView view;
 		
 		private Translator translator;
		private TranslatableGUIElement builder;
 		private Locale locale;
 		
 	public PPBSGenerator(String path, Locale locale) {
 		translator = new Translator(path, locale);
		builder = translator.getGenerator();
 		this.locale = locale;
 	}
    

    public void init(){
        lang = new AnimalScript("Pivot Partitioning By Scanning", "Bekir �zkara", 640, 480);
        lang.setStepMode(true);
        
        Manager.lang = lang;
        Manager.builder = builder;
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
    	
    	pivot = (Integer)primitives.get("pivot");
    	intArray = (int[])primitives.get("intArray");
        
        m1MarkerProps = (ArrayMarkerProperties)props.getPropertiesByName("m1MarkerProps");
        m2MarkerProps = (ArrayMarkerProperties)props.getPropertiesByName("m2MarkerProps");
        i1MarkerProps = (ArrayMarkerProperties)props.getPropertiesByName("i1MarkerProps");
        i2MarkerProps = (ArrayMarkerProperties)props.getPropertiesByName("i2MarkerProps");
        i3MarkerProps = (ArrayMarkerProperties)props.getPropertiesByName("i3MarkerProps");
        jMarkerProps = (ArrayMarkerProperties)props.getPropertiesByName("jMarkerProps");
        arrayProps = (ArrayProperties)props.getPropertiesByName("arrayProps");
        textProps = (TextProperties)props.getPropertiesByName("textProps");
        sourceCodeProps = (SourceCodeProperties)props.getPropertiesByName("sourceCodeProps");
        boxProps = (RectProperties)props.getPropertiesByName("boxProps");
        textBoxProps = (TextProperties)props.getPropertiesByName("textBoxProps");
        pivotTextProps = (TextProperties)props.getPropertiesByName("pivotTextProps");
        resultTextProps = (TextProperties)props.getPropertiesByName("resultTextProps");
        
        // end auto
        counterProps = new CounterProperties();
        counterProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);
        counterProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        
        iterationTextProps = pivotTextProps;
        
        i1Color = (Color)i1MarkerProps.get(AnimationPropertiesKeys.COLOR_PROPERTY);
        i2Color = (Color)i2MarkerProps.get(AnimationPropertiesKeys.COLOR_PROPERTY);
        i3Color = (Color)i3MarkerProps.get(AnimationPropertiesKeys.COLOR_PROPERTY);
        
        // main()
        Manager.makeTitle();
		
		showIntroText();
		
//		int[] arr = new int[] {11, 20, 19, 21, 13, 19, 15};
//		int pivot = 19;
//		pivot = 13;
		
		ppbs(intArray, pivot);	// use Animal-provided values instead
		
		Manager.hideArrayMarker(i1);
		Manager.hideArrayMarker(i2);
		Manager.hideArrayMarker(i3);
		lang.nextStep();
		
		Manager.clear();
		showOutroText();
        
        return lang.toString();
    }

    private void showIntroText() {
		Manager.showIntroText(textProps);
		lang.nextStep("Introduction");
		Manager.clear();
	}
	
    private void showOutroText() {
		Manager.showOutroText(textProps);
		lang.nextStep("Outro");
	}
    
    
    // variant: at least one of i1, i2, i3 is increased by at least 1, none of them is decreased
	// break condition: i1 == m1 && i2 == m2 && i3 == n + 1
	private void ppbs(int[] arr, int pivot) {
		IntArray array = lang.newIntArray(new Coordinates(400, 250), arr, "array", null, arrayProps);
		Manager.addToClearList(array);
		
		counter = lang.newCounter(array);
		view = lang.newCounterView(counter, new Coordinates(400, 100), counterProps, true, true);
		
		int pivotX = 600;
		int pivotY = 350;
		Manager.makePivotText(pivot, pivotX, pivotY, pivotTextProps);
		
		// this dummy array is needed to make i3 point one past the last element at the end (it will not be shown though)
		int[] dummyArr = new int[arr.length+1];
		for(int i=0; i < arr.length; ++i) {
			dummyArr[i] = arr[i];
		}
		IntArray dummyArray = lang.newIntArray(new Coordinates(400, 250), dummyArr, "dummyArray", null, arrayProps);
		dummyArray.hide();
		
		Manager.makeBox(400, 350, 550, 450, boxProps);
		
		// init
		showSourceCodeInit();
		
		scInit.highlight(0);
		lang.nextStep("Init");
		
		m1 = lang.newArrayMarker(array, 0, "m1", null, m1MarkerProps);
		scInit.toggleHighlight(0, 1);
		Manager.addToClearList(m1);
		Manager.makeM1Text(m1.getPosition(), textBoxProps);
		lang.nextStep();
		scInit.unhighlight(1);

		j = lang.newArrayMarker(array, 0, "j", null, jMarkerProps);
		for(; j.getPosition() < array.getLength(); j.increment(null, timing)) {
			scInit.highlight(2);
			lang.nextStep();
			
			scInit.toggleHighlight(2, 3);
			if(array.getData(j.getPosition()) < pivot) {
				lang.nextStep();
				scInit.highlight(3, 0, true);
				m1.increment(null, timing);
				scInit.highlight(4);
				Manager.makeM1Text(m1.getPosition(), textBoxProps);
			}
			
			// unhighlight if-case
			lang.nextStep();
			scInit.unhighlight(3);
			scInit.unhighlight(4);
		}
		j.hide();
		
		m2 = lang.newArrayMarker(array, m1.getPosition(), "m2", null, m2MarkerProps);
		scInit.highlight(5);
		Manager.addToClearList(m2);
		Manager.makeM2Text(m2.getPosition(), textBoxProps);
		lang.nextStep();
		scInit.unhighlight(5);

		
		j = lang.newArrayMarker(array, 0, "j", null, jMarkerProps);
		for(; j.getPosition() < array.getLength(); j.increment(null, timing)) {
			scInit.highlight(6);
			lang.nextStep();
			
			scInit.toggleHighlight(6, 7);
			if(array.getData(j.getPosition()) == pivot) {
				lang.nextStep();
				scInit.highlight(7, 0, true);
				m2.increment(null, timing);
				scInit.highlight(8);
				Manager.makeM2Text(m2.getPosition(), textBoxProps);
			}
			
			// unhighlight if-case
			lang.nextStep();
			scInit.unhighlight(7);
			scInit.unhighlight(8);
		}
		j.hide();
		
		i1 = lang.newArrayMarker(array, 0, "i1", null, i1MarkerProps);
		scInit.highlight(9);
		Manager.addToClearList(i1);
		Manager.makeI1Text(i1.getPosition(), textBoxProps);
		lang.nextStep();
		
		i2 = lang.newArrayMarker(array, m1.getPosition(), "i2", null, i2MarkerProps);
		scInit.toggleHighlight(9, 10);
		Manager.addToClearList(i2);
		Manager.makeI2Text(i2.getPosition(), textBoxProps);
		lang.nextStep();
		
		i3 = lang.newArrayMarker(dummyArray, m2.getPosition(), "i3", null, i3MarkerProps);			// TODO: is dummyArray ok?
		scInit.toggleHighlight(10, 11);
		Manager.addToClearList(i3);
		Manager.makeI3Text(i3.getPosition(), textBoxProps);
		lang.nextStep();
		
		scInit.toggleHighlight(11, 12);
		lang.nextStep();
		while(i1.getPosition() < m1.getPosition() && array.getData(i1.getPosition()) < pivot) {
			Manager.highlightCell(array, i1.getPosition(), i1Color);
			i1.increment(null, timing);
			scInit.toggleHighlight(12, 13);
			Manager.makeI1Text(i1.getPosition(), textBoxProps);
			lang.nextStep();
			
			scInit.toggleHighlight(13, 12);
			lang.nextStep();
		}
		scInit.unhighlight(12);
		
		scInit.highlight(14);
		lang.nextStep();
		while(i2.getPosition() < m2.getPosition() && array.getData(i2.getPosition()) == pivot) {
			Manager.highlightCell(array, i2.getPosition(), i2Color);
			i2.increment(null, timing);
			scInit.toggleHighlight(14, 15);
			Manager.makeI2Text(i2.getPosition(), textBoxProps);
			lang.nextStep();
			
			scInit.toggleHighlight(15, 14);
			lang.nextStep();
		}
		scInit.unhighlight(14);
		
		scInit.highlight(16);
		lang.nextStep();
		while(i3.getPosition() < array.getLength() && array.getData(i3.getPosition()) > pivot) {
			Manager.highlightCell(array, i3.getPosition(), i3Color);
			i3.increment(null, timing);
			scInit.toggleHighlight(16, 17);
			Manager.makeI3Text(i3.getPosition(), textBoxProps);
			lang.nextStep();
			
			scInit.toggleHighlight(17, 16);
			lang.nextStep();
		}
		scInit.unhighlight(16);
		// endinit
		
		scInit.hide();
			
		// partitioning
		showSourceCodePartition();
		
		scPartition.highlight(0);
		lang.nextStep("Partition");
		
		scPartition.toggleHighlight(0, 1);
		lang.nextStep();
		int iter = 0;
		while(i1.getPosition() != m1.getPosition() || i2.getPosition() != m2.getPosition() || i3.getPosition() != array.getLength()) {
			Manager.makeIterationText(iter++, pivotX, pivotY + 25, iterationTextProps);
			
			scPartition.toggleHighlight(1, 2);
			lang.nextStep();
			
			if(i1.getPosition() < m1.getPosition()) {
				// highlight if first
				scPartition.highlight(2, 0, true);
				scPartition.highlight(3);
				lang.nextStep();
				if(array.getData(i1.getPosition()) == pivot) {
// 						scPartition.unhighlight(2);
					scPartition.highlight(3, 0, true);
					scPartition.highlight(4);
					array.swap(i1.getPosition(), i2.getPosition(), null, timing);	// nextStep before or after swap ???
					lang.nextStep();
					
					scPartition.toggleHighlight(4, 5);
					while(i2.getPosition() < m2.getPosition() && array.getData(i2.getPosition()) == pivot) {
						lang.nextStep();
						
						Manager.highlightCell(array, i2.getPosition(), i2Color);
						i2.increment(null, timing);
						scPartition.toggleHighlight(5, 6);
						Manager.makeI2Text(i2.getPosition(), textBoxProps);
						
						lang.nextStep();
						scPartition.toggleHighlight(6, 5);
					}
					lang.nextStep();
					scPartition.unhighlight(5);	// while()
					scPartition.unhighlight(3);	// if(array[i1] == pivot)
				}
				// S[i1] > p
				else {
					// unhighlight if and highlight else 
					scPartition.toggleHighlight(3, 8);
					lang.nextStep();
					
					scPartition.highlight(8, 0, true);
					scPartition.highlight(9);
					array.swap(i1.getPosition(), i3.getPosition(), null, timing);
					lang.nextStep();
					
					scPartition.toggleHighlight(9, 10);
					while(i3.getPosition() < array.getLength() && array.getData(i3.getPosition()) > pivot) {
						lang.nextStep();
						
						Manager.highlightCell(array, i3.getPosition(), i3Color);
						i3.increment(null, timing);
						scPartition.toggleHighlight(10, 11);
						Manager.makeI3Text(i3.getPosition(), textBoxProps);
						
						lang.nextStep();
						scPartition.toggleHighlight(11, 10);
					}
					lang.nextStep();
					scPartition.unhighlight(10);	// while()
					scPartition.unhighlight(8);		// else
				}
				
				scPartition.highlight(12);
				while(i1.getPosition() < m1.getPosition() && array.getData(i1.getPosition()) < pivot) {
					lang.nextStep();
					
					Manager.highlightCell(array, i1.getPosition(), i1Color);
					i1.increment(null, timing);
					scPartition.toggleHighlight(12, 13);
					Manager.makeI1Text(i1.getPosition(), textBoxProps);
					
					lang.nextStep();
					scPartition.toggleHighlight(13, 12);
				}
				lang.nextStep();				
				scPartition.unhighlight(12);	// while()
// 					scPartition.unhighlight(2);		// if(i1 < m1) needed?
			}
			// i1 == m1
			else {
				// first else case
				scPartition.toggleHighlight(2, 15);
				lang.nextStep();
				
				scPartition.highlight(15, 0, true);
				scPartition.highlight(16);
				array.swap(i2.getPosition(), i3.getPosition(), null, timing);
				lang.nextStep();
				
				scPartition.toggleHighlight(16, 17);
				while(i2.getPosition() < m2.getPosition() && array.getData(i2.getPosition()) == pivot) {
					lang.nextStep();
					
					Manager.highlightCell(array, i2.getPosition(), i2Color);
					i2.increment(null, timing);
					scPartition.toggleHighlight(17, 18);
					Manager.makeI2Text(i2.getPosition(), textBoxProps);
					
					lang.nextStep();
					scPartition.toggleHighlight(18, 17);
				}
				lang.nextStep();
				
				scPartition.toggleHighlight(17, 19);
				while(i3.getPosition() < array.getLength() && array.getData(i3.getPosition()) > pivot) {
					lang.nextStep();
					
					Manager.highlightCell(array, i3.getPosition(), i3Color);
					i3.increment(null, timing);
					scPartition.toggleHighlight(19, 20);
					Manager.makeI3Text(i3.getPosition(), textBoxProps);
					
					lang.nextStep();
					scPartition.toggleHighlight(20, 19);
				}
				lang.nextStep();
				scPartition.unhighlight(19);	// while()
				scPartition.unhighlight(15);	// else (context)
			}
		} // while()
		Manager.makeIterationText(iter++, pivotX, pivotY + 25, iterationTextProps);
		
		lang.nextStep();
		scPartition.highlight(21);
		Manager.makeResultText(new int[] { m1.getPosition(),  m2.getPosition() }, pivotX, pivotY + 50,  resultTextProps);
	} // ppbs()
    
    
    // ===========================================================================
	// ===========================================================================
	// ===========================================================================
	// ===========================================================================
	// ===========================================================================
	// ===========================================================================
	
	private void showSourceCodeInit() {
		scInit = lang.newSourceCode(new Coordinates(20, 150), "sourceCode", null, sourceCodeProps);
		Manager.addToClearList(scInit);
		
		scInit.addCodeLine("init(A, pivot, m1, m2, i1, i2, i3)", null, 0, null);	// 1
		scInit.addCodeLine("m1 = 0", null, 1, null);								// 2
		scInit.addCodeLine("for j=0 to A.length-1", null, 1, null);					// 3
		scInit.addCodeLine("if A[j] < pivot", null, 2, null);						// 4
		scInit.addCodeLine("m1++", null, 3, null);									// 5
		scInit.addCodeLine("m2 = m1", null, 1, null);								// 6
		scInit.addCodeLine("for j=0 to A.length-1", null, 1, null);					// 7
		scInit.addCodeLine("if A[j] == pivot", null, 2, null);						// 8
		scInit.addCodeLine("m2++", null, 3, null);									// 9
		scInit.addCodeLine("i1 = 0", null, 1, null);								// 10
		scInit.addCodeLine("i2 = m1", null, 1, null);								// 11
		scInit.addCodeLine("i3 = m2", null, 1, null);								// 12
		scInit.addCodeLine("while i1 < m1 and A[i1] < pivot", null, 1, null);		// 13
		scInit.addCodeLine("i1++", null, 2, null);									// 14
		scInit.addCodeLine("while i2 < m2 and A[i2] == pivot", null, 1, null);		// 15
		scInit.addCodeLine("i2++", null, 2, null);									// 16
		scInit.addCodeLine("while i3 < A.length and A[i3] > pivot", null, 1, null);	// 17
		scInit.addCodeLine("i3++", null, 2, null);									// 18
	}
	
	private void showSourceCodePartition() {
		scPartition = lang.newSourceCode(new Coordinates(20, 150), "sourceCode", null, sourceCodeProps);
		Manager.addToClearList(scPartition);
		
		scPartition.addCodeLine("partition(A, pivot, m1, m2, i1, i2, i3)", null, 0, null);		// 1
		scPartition.addCodeLine("while i1 != m1 or i2 != m2 or i3 != A.length", null, 1, null);	// 2
		scPartition.addCodeLine("if i1 < m1", null, 2, null);									// 3
		scPartition.addCodeLine("if A[i1] == pivot", null, 3, null);							// 4
		scPartition.addCodeLine("swap A[i1] with A[i2]", null, 4, null);						// 5
		scPartition.addCodeLine("while i2 < m2 and A[i2] == pivot", null, 4, null);				// 6
		scPartition.addCodeLine("i2++", null, 5, null);											// 7
		scPartition.addCodeLine("// A[i1] > pivot", null, 3, null);								// 8
		scPartition.addCodeLine("else", null, 3, null);											// 9
		scPartition.addCodeLine("swap A[i1] with A[i3]", null, 4, null);						// 10
		scPartition.addCodeLine("while i3 < A.length and A[i3] > pivot", null, 4, null);		// 11
		scPartition.addCodeLine("i3++", null, 5, null);											// 12
		scPartition.addCodeLine("while i1 < m1 and A[i1] < pivot", null, 3, null);				// 13
		scPartition.addCodeLine("i1++", null, 4, null);											// 14
		
		scPartition.addCodeLine("// i1 == m1", null, 2, null);									// 15
		scPartition.addCodeLine("else", null, 2, null);											// 16
		scPartition.addCodeLine("swap A[i2] with A[i3]", null, 3, null);						// 17
		scPartition.addCodeLine("while i2 < m2 and A[i2] == pivot", null, 3, null);				// 18
		scPartition.addCodeLine("i2++", null, 4, null);											// 19
		scPartition.addCodeLine("while i3 < A.length and A[i3] > pivot", null, 3, null);		// 20
		scPartition.addCodeLine("i3++", null, 4, null);											// 21
		
		// optional:
		scPartition.addCodeLine("return [m1, m2]", null, 1, null);
	}
    
    
    
	// ===========================================================================
	// ===========================================================================
	// ===========================================================================
	// ===========================================================================
	// ===========================================================================
	// ===========================================================================
    
    
    public String getName() {
        return builder.generateJLabel("name").getText();
    }

    public String getAlgorithmName() {
        return "Partitioning";
    }

    public String getAnimationAuthor() {
        return "Bekir �zkara";
    }

    public String getDescription(){
        return builder.generateJLabel("d1").getText()
 +"\n"
 +builder.generateJLabel("d2").getText()
 +"\n"
 +builder.generateJLabel("d3").getText()                              
 +"\n"
 +builder.generateJLabel("d4").getText()
 +"\n"
 +builder.generateJLabel("d5").getText()
 +"\n"
 +builder.generateJLabel("d6").getText()
 +"\n"
 +builder.generateJLabel("d7").getText()                                         
 +"\n"
 +"                                                                                                                                                    "
 +"\n"
 +builder.generateJLabel("d8").getText()                                                                  
 +"\n"
 +"   " + builder.generateJLabel("d9").getText()                                                                                   
 +"\n"
 +"   " + builder.generateJLabel("d10").getText()                                                                               
 +"\n"
 +"   " + builder.generateJLabel("d11").getText();
    }

    public String getCodeExample(){
        return "// here: pseudo-code"
 +"\n"
 +"init(Array A, Pivot p)"
 +"\n"
 +"  m1 = 0"
 +"\n"
 +"  foreach(elem in A): if elem < p then m1++"
 +"\n"
 +"  m2 = m1"
 +"\n"
 +"  foreach(elem in A): if elem == p then m2++"
 +"\n"
 +"  i1 = 0"
 +"\n"
 +"  i2 = m1"
 +"\n"
 +"  i3 = m2"
 +"\n"
 +"  while(i1 < m1 and A[i1] < p) i1++"
 +"\n"
 +"  while(i2 < m2 and A[i1] == p) i2++"
 +"\n"
 +"  while(i3 < A.length and A[i3] > p) i3++"
 +"\n"
 +"\n"
 +"\n"
 +"partition(Array A, Pivot p)"
 +"\n"
 +"  until(i1 == m1 and i2 == m2 and i3 == A.length) do:"
 +"\n"
 +"      if i1 < m1"
 +"\n"
 +"          if A[i1] == p"
 +"\n"
 +"              swap A[i1] with A[i2]"
 +"\n"
 +"              while(i2 < m2 and A[i2] == p) i2++"
 +"\n"
 +"          else"
 +"\n"
 +"              swap A[i1] with A[i3]"
 +"\n"
 +"              while(i3 < A.length and A[i3] > p) i3++"
 +"\n"
 +"          while(i1 < m1 and A[i1] < p) i1++"
 +"\n"
 +"      else "
 +"\n"
 +"          swap A[i2] with A[i3]"
 +"\n"
 +"          while(i2 < m2 and A[i2] == p) i2++"
 +"\n"
 +"          while(i3 < A.length and A[i3] > p) i3++"
 +"\n"
 +"  return m1 and m2"
 +"\n"
 +"  ";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return locale;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }

}