package generators.sorting.insertionsort;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.helpers.AnimatedIntArrayAlgorithm;

import java.util.Hashtable;
import java.util.Locale;

import translator.Translator;
import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.Text;
import algoanim.properties.AnimationProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Offset;

public class GenericAnnotatedInsertionSort extends AnimatedIntArrayAlgorithm
    implements Generator {
  protected Text   vLabel, vContent;

  protected Locale contentLocale = null;

  public GenericAnnotatedInsertionSort(String aResourceName, Locale aLocale) {
    resourceName = aResourceName;
    locale = aLocale;
    init();
  }

  /**
   * hides the array, code, and number of steps taken from the display
   */
  protected void hideNrStepsArrayCode() {
    super.hideNrStepsArrayCode();
    if (array != null)
      array.hide();
    vLabel.hide();
    vContent.hide();
  }

  public void sort() {
    int i, j = 0;
    int v;
    ArrayMarker iMarker = null, jMarker = null;
    code.highlight("header");
    lang.nextStep("Init");

    code.toggleHighlight("header", "variables");
    vLabel = lang.newText(new Offset(0, 35, nrCompLabel,
        AnimalScript.DIRECTION_NW), "v =", "v=", null,
        (TextProperties) primitiveProps.get("title"));
    vContent = lang.newText(new Offset(10, 0, vLabel,
        AnimalScript.DIRECTION_BASELINE_END), "", "vValue", null,
        (TextProperties) primitiveProps.get("title"));
    lang.nextStep();

    code.toggleHighlight("variables", "outerLoop");

    for (i = 1; i < array.getLength(); i++) {
      vContent.setText("", null, null);
      incrementNrAssignments(); // i = 1 or i++
      incrementNrComparisons(); // i < array.getLength
      if (iMarker == null)
        iMarker = installArrayMarker("iMarker", array, i);
      else
        iMarker.move(i, null, null);
      lang.nextStep("Insertion Sort, i=" + i);

      array.highlightCell(i - 1, null, null);
      code.toggleHighlight("outerLoop", "takeOut");
      array.highlightElem(i, null, null);
      vContent.setText(String.valueOf(array.getData(i)), null, null);
      v = array.getData(i);
      incrementNrAssignments(); // v = a[i]
      lang.nextStep();

      code.toggleHighlight("takeOut", "innerLoop");
      array.unhighlightElem(i, null, null);

      for (j = i; j > 0 && v < array.getData(j - 1); j--) {
        incrementNrComparisons(2); // j> 0, v < a[j-1]
        incrementNrAssignments(); // j = i or j--
        if (jMarker == null)
          jMarker = installArrayMarker("jMarker", array, j);
        else
          jMarker.move(j, null, null);
        lang.nextStep();

        code.toggleHighlight("innerLoop", "moveForward");
        array.highlightCell(j, null, null);
        array.unhighlightCell(j - 1, null, null);
        array.put(j, array.getData(j - 1), null, null);
        incrementNrAssignments(); // a[j] = a[j-1]
        lang.nextStep();

        code.toggleHighlight("moveForward", "innerLoop");
      }
      incrementNrAssignments(); // last j--
      if (j > 0)
        incrementNrComparisons(2); // j > 0 && v < a[j-1] == false
      else
        incrementNrComparisons(); // j > 0 == false
      lang.nextStep();

      code.toggleHighlight("innerLoop", "insert");
      array.highlightElem(j, null, null);
      array.put(j, v, null, null);
      incrementNrAssignments(); // a[j] = v

      lang.nextStep();
      code.toggleHighlight("insert", "outerLoop");
      array.unhighlightElem(j, null, null);
      array.highlightCell(j, null, null);

    }
    incrementNrAssignments(); // last i++
    incrementNrComparisons(); // last loop comparison

    array.highlightCell(i - 1, null, null);
    code.unhighlight("outerLoop");
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> prims) {
    setUpDefaultElements(props, prims, "array", "code", "code", 0, 20);
    // new Offset(0, 20, array, AnimalScript.DIRECTION_SW));
    sort();
    wrapUpAnimation();
    lang.finalizeGeneration();
    return lang.getAnimationCode();
  }

  public String getAlgorithmName() {
    return "Insertion Sort";
  }

  public String getAnimationAuthor() {
    return "Guido Rößling";
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

  public void init() {
    translator = new Translator(resourceName, locale);
    primitiveProps = new Hashtable<String, AnimationProperties>(59);
    localType = new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
    contentLocale = locale;
  }
}
