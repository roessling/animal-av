package generators.sorting.bubblesort;

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

public class DelphiAnnotatedBubbleSort extends AnimatedIntArrayAlgorithm
    implements Generator {
  protected Text               tempLabel, tempValue;
  protected InteractionFactory factory;
  protected Locale             contentLocale = null;

  public DelphiAnnotatedBubbleSort() {
    this("resources/DelphiBubbleSort", Locale.GERMANY);
  }

  public DelphiAnnotatedBubbleSort(String aResourceName, Locale aLocale) {
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
   * Bubble Sort swaps neighbors if they are not sorted. It iterates up to n
   * times over the array, regarding only the elements at indices [0, n-i] in
   * iteration i. Run-time complexity in worst case: O(n*n)
   */
  public void sort() {
    // int enumSwaps = 0;

    int nrElems = array.getLength();
    ArrayMarker iMarker = null, jMarker = null;
    // highlight method header
    code.highlight("header");
    lang.nextStep("Erster Aufruf");

    // switch to variable declaration
    code.toggleHighlight("header", "variables");
    int i, j; // loop counters
    int t; // temporary swap element
    tempLabel = lang.newText(new Offset(0, 30, nrCompLabel,
        AnimalScript.DIRECTION_SW), "T =", "tLabel", null,
        (TextProperties) primitiveProps.get("title"));
    tempValue = lang.newText(new Offset(10, 0, tempLabel,
        AnimalScript.DIRECTION_BASELINE_END), "", "tValue", null,
        (TextProperties) primitiveProps.get("title"));
    lang.nextStep("Deklaration der Variablen");

    // create i marker
    code.unhighlight("variables"); // leave variable declaration
    boolean done = false;
    for (i = nrElems - 1; !done && i >= 0; i--) {
      code.highlight("outerLoop");
      if (i == nrElems - 1) {
        iMarker = installArrayMarker("iMarker", array, i);
        // iMarker.moveOutside(null, DEFAULT_TIMING);
      } else {
        iMarker.move(i, null, DEFAULT_TIMING);
        array.highlightCell(i + 1, null, DEFAULT_TIMING);
      }
      incrementNrComparisons(); // i >= 0
      incrementNrAssignments(); // i-- [i = nrElems in first iteration]

      // reset swapPerformed
      lang.nextStep("Bubble Sort, i=" + i);
      code.unhighlight("outerLoop"); // change to inner loop

      for (j = 0; !done && j < nrElems - 1; j++) {
        code.highlight("innerLoop"); // enter j loop
        if (jMarker == null) {
          jMarker = installArrayMarker("jMarker", array, j);
        } else
          jMarker.move(j, null, DEFAULT_TIMING);
        incrementNrAssignments(); // j = 1 // j++
        incrementNrComparisons(); // j <= nrElems - 1

        // compare a[j-1], a[j]
        lang.nextStep();
        code.toggleHighlight("innerLoop", "if");
        array.highlightElem(j + 1, null, null);
        array.highlightElem(j, null, null);

        incrementNrComparisons(); // if (a[j] > a[j + 1])
        lang.nextStep();
        if (array.getData(j) > array.getData(j + 1)) {
          // swap elements
          // enumSwaps++;

          // t = a[j]
          code.toggleHighlight("if", "copy");
          t = array.getData(j);
          tempValue.setText(String.valueOf(t), null, DEFAULT_TIMING);
          incrementNrAssignments();
          lang.nextStep("  Vertausche " + array.getData(j) + ", "
              + array.getData(j + 1));

          // a[j] = a[j+1]
          code.toggleHighlight("copy", "replicate");
          array.put(j, array.getData(j + 1), null, DEFAULT_TIMING);
          incrementNrAssignments();
          lang.nextStep();

          // a[j+1] = t
          code.toggleHighlight("replicate", "insertCopy");
          array.put(j + 1, t, null, DEFAULT_TIMING);
          incrementNrAssignments();
          lang.nextStep();

          // clean up...
          done = terminated(array);
          code.toggleHighlight("insertCopy", "checkDone");
          lang.nextStep();
          code.unhighlight("checkDone");
        } else {
          code.unhighlight("if");
        }
        array.unhighlightElem(j + 1, null, null);
        array.unhighlightElem(j, null, null);
      } // for j...
      incrementNrComparisons(); // last iteration of inner loop
      incrementNrAssignments(); // last iteration of inner loop
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
    return "Bubble Sort";
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
