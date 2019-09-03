package generators.sorting.selectionsort;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.helpers.AnimatedIntArrayAlgorithm;

import java.util.Hashtable;
import java.util.Locale;

import translator.Translator;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.Text;
import algoanim.properties.AnimationProperties;

public class GenericAnnotatedSelectionSortICS2 extends AnimatedIntArrayAlgorithm implements
		Generator {
	protected Text swapLabel, swapPerf;

	protected Locale contentLocale = null;
    
	public GenericAnnotatedSelectionSortICS2(String aResourceName, Locale aLocale) {
      resourceName = aResourceName;
      locale = aLocale;
      init();
	}
    
    public void init() {
		translator = new Translator(resourceName, locale);
		primitiveProps = new Hashtable<String, AnimationProperties>(59);
		localType = new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
		contentLocale = locale;
	}

	/**
	 * hides the array, code, and number of steps taken from the display
	 */
	protected void hideNrStepsArrayCode() {
		super.hideNrStepsArrayCode();
		if (array != null)
			array.hide();
	}

	/**
	 * Bubble Sort swaps neighbours if they are not sorted. It iterates up to n
	 * times over the array, regarding only the elements at indices [0, n-i] in
	 * iteration i. Run-time complexity in worst case: O(n*n)
	 */
	public void sort() {
		int i, j = 0; // Schleifenzaehler
		int maxIndex = 0; // Speicher fuer aktuelles Element
		boolean swap = false;
		// Create marker objects
		ArrayMarker iMarker = null, jMarker = null;
		
		// Higlight function header
		code.highlight("header");
		lang.nextStep();
		
		// Highlight variable initialization
		code.toggleHighlight("header", "variables");
		lang.nextStep();
		code.unhighlight("variables");
		// Initialize iMarker
		iMarker = installArrayMarker("iMarker", array, array.getLength() - 1);

		// Outer Loop
		for (i = array.getLength() - 1; i > 0 ; i--) {
		  // Move iMarker
      if(swap)
         iMarker.move(i+1, null, null);
      else
        iMarker.move(i, null, null);
      
      // Highlight outerloop
		  code.highlight("outerLoop");
      incrementNrAssignments();   // for loop
      incrementNrComparisons();   // for loop
	    lang.nextStep();
	    
	    // Highlight setmaxIndex
			code.toggleHighlight("outerLoop", "setmaxIndex");
      incrementNrAssignments();    // maxIndex = i
			array.unhighlightElem(maxIndex, null, null);
			
			// Set max index and highlight it
			maxIndex = i;
			array.highlightElem(maxIndex, null, null);
      lang.nextStep();
			code.unhighlight("setmaxIndex");
			
			// Inner loop
			for (j=0; j<i; j++) {
			  // Highlight inner loop
			  code.highlight("innerLoop");
	      incrementNrAssignments(); // for loop
				incrementNrComparisons(); // for loop
				
				// Move jMarker
				if (jMarker == null)
					jMarker = installArrayMarker("jMarker", array, j);
				else
					jMarker.move(j, null, null);

        lang.nextStep();
        
        // Highlight "if"
				code.toggleHighlight("innerLoop", "compare");
        incrementNrComparisons();
				lang.nextStep();
				
				if (array.getData(j) > array.getData(maxIndex)) {
					code.toggleHighlight("compare", "minFound");
					
					// Highlight maxIndex set
					array.unhighlightElem(maxIndex, null, null);
					maxIndex = j;
          array.highlightElem(maxIndex, null, null);
					incrementNrAssignments();
					
					// Cleaning Up
          lang.nextStep();
					code.unhighlight("minFound");
				} else code.unhighlight("compare");
			}
			
			// Swapping
			code.toggleHighlight("innerLoop", "swap1");
      incrementNrAssignments();   // swap
			lang.nextStep();
			code.toggleHighlight("swap1", "swap2");
      incrementNrAssignments();   // swap
      lang.nextStep();
      code.toggleHighlight("swap2", "swap3");
      incrementNrAssignments();   // swap

      if(i==maxIndex+1) swap = true;
      else swap = false;
      array.swap(i, maxIndex, null, null);
      lang.nextStep();
			code.unhighlight("swap3");
	    array.highlightCell(i, null, null);
      array.unhighlightElem(i, null, null);
		}
		// Cleaning up
		code.unhighlight("outerLoop");
	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> prims) {
		setUpDefaultElements(props, prims, "array", "code", "code", 0, 20);
		
		sort();
		if (swapPerf != null)
			swapPerf.hide();
		if (swapLabel != null)
			swapLabel.hide();
		wrapUpAnimation();
		
		return lang.toString();
	}

	public String getAlgorithmName() {
	  return "Selection Sort";
	}
    
    public String getAnimationAuthor() {
      return "Krasimir Markov";
    }


	/**
	 * getContentLocale returns the target Locale of the generated output
	 * Use e.g. Locale.US for English content, Locale.GERMANY for German, etc.
	 * 
	 * @return a Locale instance that describes the content type of the output
	 */
	public Locale getContentLocale() {
		return contentLocale;
	}
}
