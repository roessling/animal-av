package generators.sorting;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.helpers.AnimatedIntArrayAlgorithm;

import java.util.Hashtable;
import java.util.Locale;

import translator.Translator;
import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.Primitive;
import algoanim.primitives.Text;
import algoanim.properties.AnimationProperties;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class GenericAnnotatedMergeSort extends AnimatedIntArrayAlgorithm
    implements Generator {
  protected Text   swapLabel, swapPerf;

  protected Locale contentLocale = null;

  protected ArrayMarker iMarker = null, jMarker = null, kMarker = null;

  protected IntArray    bArray  = null;

  public GenericAnnotatedMergeSort(String aResourceName, Locale aLocale) {
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
    if (bArray != null)
      bArray.hide();
  }

  public void sort() {
    mergeSort(0, (array.getLength() - 1), 0);
  }

  private String createSplitLabel(int l, int r, int d) {
    StringBuilder mergeLabel = new StringBuilder(40);
    for (int a = 0; a < d; a++)
      mergeLabel.append(' ');
    mergeLabel.append("Mergesort(array, ").append(l).append(", ");
    mergeLabel.append(r).append(")");
    return mergeLabel.toString();
  }

  private String createMergeLabel(int l, int r, int d) {
    StringBuilder mergeLabel = new StringBuilder(40);
    for (int a = 0; a < d; a++)
      mergeLabel.append(' ');
    mergeLabel.append("merge array [").append(l).append(", ");
    mergeLabel.append(r).append("]");
    return mergeLabel.toString();
  }

  private void mergeSort(int l, int r, int depth) {
    int i, j, k, m;

    bArray.highlightElem(0, bArray.getLength() - 1, null, null);

    code.highlight("header");
    array.highlightCell(l, r, null, null);
    lang.nextStep();

    code.toggleHighlight("header", "variables");
    lang.nextStep(createSplitLabel(l, r, depth));

    code.toggleHighlight("variables", "array");
    lang.nextStep();

    code.toggleHighlight("array", "check");
    lang.nextStep();

    if (r > l) {
      code.toggleHighlight("check", "determineMid");
      incrementNrComparisons();
      lang.nextStep();

      m = (l + r) / 2;
      code.toggleHighlight("determineMid", "sortLeftside");
      incrementNrAssignments();
      lang.nextStep();

      code.unhighlight("sortLeftside");
      array.unhighlightCell(l, r, null, null);
      mergeSort(l, m, depth + 1);
      code.highlight("sortRightside");
      lang.nextStep();

      code.unhighlight("sortRightside");
      mergeSort(m + 1, r, depth + 1);
      code.highlight("copyLeftside");
      array.highlightCell(l, r, null, null);
      lang.nextStep(createMergeLabel(l, r, depth));

      incrementNrAssignments();
      incrementNrComparisons(2);

      for (i = l; i <= m && i < array.getLength(); i++) {
        bArray.put(i - l, array.getData(i), null, null);
        incrementNrAssignments();
        bArray.unhighlightElem(i - l, null, null);

        incrementNrAssignments();
        incrementNrComparisons(2);
      }
      code.toggleHighlight("copyLeftside", "copyRightside");
      lang.nextStep();

      incrementNrAssignments();
      incrementNrComparisons();
      for (j = m + 1; j <= r; j++) {
        bArray.put(r + m + 1 - j - l, array.getData(j), null, null);
        incrementNrAssignments();
        bArray.unhighlightElem(r + m + 1 - j - l, null, null);

        incrementNrAssignments();
        incrementNrComparisons();
      }
      code.toggleHighlight("copyRightside", "loop");
      lang.nextStep();

      incrementNrAssignments(3);
      incrementNrComparisons(2);
      for (k = l, i = 0, j = r - l; k <= r && k < array.getLength(); k++) {
        if (iMarker == null)
          iMarker = installArrayMarker("iMarker", bArray, i);
        else
          iMarker.move(i, null, null);
        if (jMarker == null)
          jMarker = installArrayMarker("jMarker", bArray, j);
        else
          jMarker.move(j, null, null);
        if (kMarker == null)
          kMarker = installArrayMarker("kMarker", array, k);
        else
          kMarker.move(k, null, null);

        code.toggleHighlight("loop", "merge");
        lang.nextStep();

        array.put(k,
            (bArray.getData(i) < bArray.getData(j)) ? bArray.getData(i++)
                : bArray.getData(j--), null, null);
        incrementNrAssignments(3);
        incrementNrComparisons(1);
        code.toggleHighlight("merge", "loop");
        lang.nextStep();

        incrementNrAssignments();
        incrementNrComparisons(2);
      }
      code.unhighlight("loop");

    } else {
      code.unhighlight("check");
    }
    array.unhighlightCell(l, r, null, null);
  }

  /**
   * Install the display of the array
   * 
   * @param key
   *          the key for retrieving the array's properties
   * @return the IntArray instance
   */
  protected IntArray installIntArray(String key, int x, int y) {
    // create and install array
    int[] arrayData = (int[]) primitives.get(key);
    if (arrayData == null)
      arrayData = new int[array.getLength()];
    ArrayProperties iap = new ArrayProperties();
    iap.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        animationProperties.get(key, AnimationPropertiesKeys.COLOR_PROPERTY));
    iap.set(AnimationPropertiesKeys.FILL_PROPERTY,
        animationProperties.get(key, AnimationPropertiesKeys.FILL_PROPERTY));
    iap.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    iap.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, animationProperties
        .get(key, AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY));
    iap.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, animationProperties
        .get(key, AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY));
    iap.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, animationProperties
        .get(key, AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY));
    IntArray intArray = lang.newIntArray(new Coordinates(x, y), arrayData,
        "array", null, iap);
    return intArray;
  }

  public Primitive installAdditionalComponents(String arrayKey, String codeKey,
      String codeName, int dx, int dy) {
    // install the arrays
    String bArrayKey = "bArray";
    array = installIntArray(arrayKey, 30, 150);
    bArray = installIntArray(bArrayKey, 30, 250);
    // install the source code (if any)
    code = installCodeBlock(codeKey, codeName, new Offset(dx, dy, bArray,
        AnimalScript.DIRECTION_SW));
    return array;
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> prims) {
    setUpDefaultElements(props, prims, "array", "code", "code", 0, 20);
    // new Offset(0, 20, array, AnimalScript.DIRECTION_SW));
    sort();
    if (swapPerf != null)
      swapPerf.hide();
    if (swapLabel != null)
      swapLabel.hide();
    wrapUpAnimation();
    return lang.toString();
  }

  public String getAlgorithmName() {
    return "Merge Sort";
  }

  public String getAnimationAuthor() {
    return "Krasimir Markov";
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
}
