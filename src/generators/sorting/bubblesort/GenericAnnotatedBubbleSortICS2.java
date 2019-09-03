package generators.sorting.bubblesort;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.helpers.AnimatedIntArrayAlgorithm;

import interactionsupport.models.HtmlDocumentationModel;
import interactionsupport.parser.InteractionFactory;

import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import translator.Translator;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.Text;
import algoanim.properties.AnimationProperties;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.TextProperties;
import algoanim.util.Offset;

public class GenericAnnotatedBubbleSortICS2 extends AnimatedIntArrayAlgorithm
    implements Generator {
  protected Text               swapLabel, swapPerf;
  protected InteractionFactory factory;
  protected Locale             contentLocale = null;

  public GenericAnnotatedBubbleSortICS2(String aResourceName, Locale aLocale) {
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
    int i, j; // Schleifenzaehler
    // int enumSwaps = 0;
    // Total number of elements in the array
    int nrElems = array.getLength();
    // Create marker objects
    ArrayMarker iMarker = null, jMarker = null;
    // highlight method header
    code.highlight("header");
    lang.nextStep();

    // switch to variable declaration
    code.toggleHighlight("header", "variables");
    incrementNrAssignments();
    // create i marker
    iMarker = installArrayMarker("iMarker", array, nrElems - 1);
    lang.nextStep();
    // switch to init for swapPerformed
    code.toggleHighlight("variables", "initialize");
    boolean swapPerformed = true; // wurde getauscht?
    incrementNrAssignments(); // swapPerformed = true

    // Text properties for the "sorted=..."
    TextProperties textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 13));
    // Create the labels for "sorted=.."
    swapLabel = lang.newText(new Offset(80, -8, array,
        AnimalScript.DIRECTION_SE), "sorted=", "swapLabel", null, textProps);
    swapPerf = lang.newText(new Offset(0, 3, swapLabel,
        AnimalScript.DIRECTION_BASELINE_END), "false", "swapVal", null,
        textProps);

    lang.nextStep();
    code.unhighlight("initialize");

    // Outer Loop
    for (i = nrElems; swapPerformed && i > 0; i--) {
      code.highlight("outerLoop");

      incrementNrComparisons(2); // swapPer, i>0

      // reset swapPerformed
      lang.nextStep("Bubble Sort, i=" + i);
      code.toggleHighlight("outerLoop", "sorted=true");
      swapPerf.setText("true", null, null);
      swapPerformed = false;
      incrementNrAssignments(); // swapPerformed = false

      // create j marker on entering the loop
      lang.nextStep();

      // Inner Loop
      for (j = 1; j < i; j++) {
        code.toggleHighlight("sorted=true", "innerLoop");
        if (jMarker == null) {
          jMarker = installArrayMarker("jMarker", array, j);
        } else
          jMarker.move(j, null, null);
        incrementNrAssignments(); // j = 1 // j++
        incrementNrComparisons(); // j < i

        // compare a[j-1], a[j]
        lang.nextStep();
        code.toggleHighlight("innerLoop", "if");
        array.highlightElem(j - 1, null, null);
        array.highlightElem(j, null, null);

        incrementNrComparisons(); // if (a[j-1] > a[j])
        lang.nextStep();
        if (array.getData(j - 1) > array.getData(j)) {
          // swap elements
          // enumSwaps++;

          code.toggleHighlight("if", "swap1");
          incrementNrAssignments(); // swap
          lang.nextStep();
          code.toggleHighlight("swap1", "swap2");
          incrementNrAssignments(); // swap
          lang.nextStep();
          code.toggleHighlight("swap2", "swap3");
          array.swap(j - 1, j, null, null);
          incrementNrAssignments(); // swap

          // set swapPerformed to true
          lang.nextStep();
          code.toggleHighlight("swap3", "sorted=false");
          swapPerf.setText("false", null, null);
          swapPerformed = true;
          incrementNrAssignments(); // swapPerformed = true

          // clean up...
          lang.nextStep();
          code.unhighlight("sorted=false");
        } else {
          code.unhighlight("if");
        }
        array.unhighlightElem(j - 1, null, null);
        array.unhighlightElem(j, null, null);
      } // for j...

      // Decreace i marker by one and highlight cell with sorted number
      iMarker.decrement(null, null);
      array.highlightCell(i - 1, null, null);

      // i=i-1
      code.highlight("i=i-1");
      incrementNrAssignments(); // last iteration of inner loop
      lang.nextStep();
      code.unhighlight("i=i-1");

      incrementNrComparisons(); // last iteration of inner loop
      incrementNrAssignments(); // last iteration of inner loop
    }
    incrementNrComparisons(); // last iteration of outer loop
    incrementNrAssignments(); // last increment of outer loop

    // Documentation
    HtmlDocumentationModel link = new HtmlDocumentationModel("link");
    link.setLinkAddress("http://de.wikipedia.org/wiki/Bubblesort");
    lang.addDocumentationLink(link);
    HtmlDocumentationModel link2 = new HtmlDocumentationModel("link2");
    link2.setLinkAddress("http://java.net/index.html");
    lang.addDocumentationLink(link2);
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

    sort();
    if (swapPerf != null)
      swapPerf.hide();
    if (swapLabel != null)
      swapLabel.hide();
    wrapUpAnimation();
    lang.finalizeGeneration();
    return lang.getAnimationCode();
  }
}
