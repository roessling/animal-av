package generators.sorting.selectionsort;

import extras.lifecycle.monitor.CheckpointUtils;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.helpers.AnimatedIntArrayAlgorithm;
import interactionsupport.parser.InteractionFactory;

import java.util.Hashtable;
import java.util.Locale;

import translator.Translator;
import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.Text;
import algoanim.properties.AnimationProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Offset;

public class DelphiAnnotatedSelectionSort extends AnimatedIntArrayAlgorithm implements Generator {
  protected Text tempLabel, tempValue;
  protected InteractionFactory factory;
  protected Locale contentLocale = null;
  
  // Used for logging
  // All these variables will be checkpointed
  //---
  public int enumSwaps;
  public int arrayIndexI; // loop counters
  public int arrayIndexJ; // loop counters
  public int arrayValueT; // temporary swap element
  public int arrayValueI; // The real value of the array at position I
  public int arrayValueJ; // The real value of the array at position J
  // ---

  public DelphiAnnotatedSelectionSort() {
    this("resources/DelphiSelectionSort", Locale.GERMANY);
  }
  
  public DelphiAnnotatedSelectionSort(String aResourceName, Locale aLocale) {
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
   * This Sort swaps neighbors if they are not sorted. It iterates up to n
   * times over the array, regarding only the elements at indices [0, n-i] in
   * iteration i. Run-time complexity in worst case: O(n*n)
   */
  public void sort() {
	
    int nrElems = array.getLength();
    ArrayMarker iMarker = null, jMarker = null;
    // highlight method header
    code.highlight("header");
    lang.nextStep("Erster Aufruf");

    // We initialize our checkpointed variables
    enumSwaps = 0;
    arrayIndexI = 0;
    arrayValueT = 0;
    arrayIndexJ = 0;
    arrayValueI = 0;
    arrayValueJ = 0;
    
    // switch to variable declaration
    code.toggleHighlight("header", "variables");
    
    tempLabel = lang.newText(
        new Offset(0, 30, nrCompLabel, AnimalScript.DIRECTION_SW), "T =",
        "tLabel", null, (TextProperties) primitiveProps.get("title"));
    tempValue = lang.newText(new Offset(10, 0, tempLabel,
        AnimalScript.DIRECTION_BASELINE_END), "", "tValue", null,
        (TextProperties) primitiveProps.get("title"));
    lang.nextStep("Deklaration der Variablen");

    // create i marker
    code.unhighlight("variables"); // leave variable declaration
    boolean done = false;
    for (arrayIndexI = 0; !done && arrayIndexI < nrElems - 1; arrayIndexI++) {
      code.highlight("outerLoop");
      if (arrayIndexI == 0) {
        iMarker = installArrayMarker("iMarker", array, arrayIndexI);
//        iMarker.moveOutside(null, DEFAULT_TIMING);
      } else {
        iMarker.move(arrayIndexI, null, DEFAULT_TIMING);
//        array.highlightCell(i - 1, null, DEFAULT_TIMING);
      }
      incrementNrComparisons(); // i >= 0
      incrementNrAssignments(); // i-- [i = nrElems in first iteration]

      // reset swapPerformed
      array.highlightElem(arrayIndexI, null, null); // highlight element i
      lang.nextStep("Selection Sort, i="+arrayIndexI);
      code.unhighlight("outerLoop"); // change to inner loop
      
       for (arrayIndexJ = nrElems - 1; !done && arrayIndexJ > arrayIndexI; arrayIndexJ--) {
        code.highlight("innerLoop"); // enter j loop
        
        if (jMarker == null) {
          jMarker = installArrayMarker("jMarker", array, arrayIndexJ);
        } else
          jMarker.move(arrayIndexJ, null, DEFAULT_TIMING);
        incrementNrAssignments(); // j = 1 // j++
        incrementNrComparisons(); // j <= nrElems - 1

        // compare a[i], a[j]
        lang.nextStep();
        code.toggleHighlight("innerLoop", "if");
        
        array.highlightElem(arrayIndexJ, null, null); // highlight element j

        incrementNrComparisons(); // if A[I] > A[J]
        lang.nextStep();
        arrayValueI = array.getData(arrayIndexI);
		arrayValueJ = array.getData(arrayIndexJ);
		if (arrayValueI > arrayValueJ) {
          // swap elements
          enumSwaps++;
          
          // T := A[I]
          code.toggleHighlight("if", "copy");
          arrayValueT = arrayValueI;
          tempValue.setText(String.valueOf(arrayValueT), null, DEFAULT_TIMING);
          incrementNrAssignments();
          lang.nextStep("  Vertausche " + arrayValueI +", " +arrayValueJ);
          
          // A[I] = A[J]
          code.toggleHighlight("copy", "replicate");
          array.put(arrayIndexI, arrayValueJ, null, DEFAULT_TIMING);
          array.highlightElem(arrayIndexI, null, DEFAULT_TIMING);
          arrayValueI = arrayValueJ;
          incrementNrAssignments();
          lang.nextStep();

          // A[J] = T
          code.toggleHighlight("replicate", "insertCopy");
          array.put(arrayIndexJ, arrayValueT, null, DEFAULT_TIMING);
          incrementNrAssignments();
          arrayValueJ = arrayValueT;
          lang.nextStep();
          
          // clean up...
          done = terminated(array);
          
          code.toggleHighlight("insertCopy", "checkDone");
          lang.nextStep();
          code.unhighlight("checkDone");
        } else {
          code.unhighlight("if");
        }
        array.unhighlightElem(arrayIndexJ, null, null);
        
        code.highlight("checkpoint");
        CheckpointUtils.checkpoint(this);
        lang.nextStep();
        code.unhighlight("checkpoint");
//        array.unhighlightElem(j, null, null);
      } // for j...
      incrementNrComparisons(); // last iteration of inner loop
      incrementNrAssignments(); // last iteration of inner loop
      array.unhighlightElem(arrayIndexI, null, null);
      array.highlightCell(arrayIndexI, null, null);
    }
    incrementNrComparisons(); // last iteration of outer loop
    incrementNrAssignments(); // last increment of outer loop
  }
  
  private boolean terminated(IntArray theArray) {
    for (int i = 0; i < array.getLength() - 1; i++)
      if (array.getData(i) > array.getData(i + 1))
        return false;
    return true;
  }

  /**
   * getContentLocale returns the target Locale of the generated output Use e.g.
   * Locale.US for English content, Locale.GERMANY for German, etc.
   * 
   * @return a Locale instance that describes the content type of the output
   */
  public Locale getContentLocale() {
    return contentLocale;
  }

  public String getAlgorithmName() {
    return "Selection Sort";
  }

  public String getAnimationAuthor() {
    return "Guido Rößling";
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> prims) {
    setUpDefaultElements(props, prims, "array", "code", "code", 0, 20);
    // new Offset(0, 20, array, AnimalScript.DIRECTION_SW));
    sort();
    if (tempValue != null)
      tempValue.hide();
    if (tempLabel != null)
      tempLabel.hide();
    wrapUpAnimation();
    lang.finalizeGeneration();
    return lang.getAnimationCode();
  }
}
