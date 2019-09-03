package generators.sorting;

import extras.lifecycle.common.Variable;
import extras.lifecycle.monitor.CheckpointUtils;

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

public class GenericAnnotatedShellSort extends AnimatedIntArrayAlgorithm
    implements Generator {
  protected Text   swapLabel, swapPerf;

  protected Locale contentLocale = null;

  public GenericAnnotatedShellSort(String aResourceName, Locale aLocale) {
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
    int i, j, dist, v;

    ArrayMarker iMarker = null, jMarker = null;

    code.highlight("header");
    lang.nextStep();

    code.toggleHighlight("header", "variables");
    lang.nextStep();

    code.toggleHighlight("variables", "initDist");
    lang.nextStep();

    for (dist = 1; dist < array.getLength() / 4; dist = dist * 3 + 1)
      ;
    code.toggleHighlight("initDist", "loop1");
    lang.nextStep();

    for (; dist > 0; dist /= 3) {
      code.toggleHighlight("loop1", "loop2");
      lang.nextStep();

      for (i = dist; i < array.getLength(); i++) {
        array.unhighlightCell(0, array.getLength() - 1, null, null);
        for (int cntr = i; cntr >= 0; cntr -= dist)
          array.highlightCell(cntr, null, null);

        if (iMarker == null)
          iMarker = installArrayMarker("iMarker", array, i);
        else
          iMarker.move(i, null, null);
        code.toggleHighlight("loop2", "takeOut");
        lang.nextStep();

        // array.unhighlightElem(minIndex, null, null);
        // minIndex = i;
        v = array.getData(i);
        incrementNrAssignments();
        array.highlightElem(i, null, null);
        code.toggleHighlight("takeOut", "setJ");
        lang.nextStep();

        j = i;
        code.toggleHighlight("setJ", "loop3");
        lang.nextStep();

        CheckpointUtils.checkpointEvent(this, "nextColumn", new Variable(
            "array[i]", v), new Variable("i", i));

        while (j >= dist && v < array.getData(j - dist)) {
          incrementNrComparisons();
          if (jMarker == null)
            jMarker = installArrayMarker("jMarker", array, j);
          else
            jMarker.move(j, null, null);
          code.toggleHighlight("loop3", "moveForward");
          lang.nextStep();

          array.put(j, array.getData(j - dist), null, null);

          CheckpointUtils.checkpointEvent(this, "switchEvent", new Variable(
              "swapIndex1", j), new Variable("swapIndex2", j - dist),
              new Variable("val", array.getData(j - dist)), new Variable(
                  "animstep", lang.getStep()));

          incrementNrAssignments();
          code.toggleHighlight("moveForward", "decrementJ");
          lang.nextStep();

          j = j - dist;
          code.toggleHighlight("decrementJ", "loop3");
          lang.nextStep();
        }

        code.toggleHighlight("loop3", "insert");
        lang.nextStep();

        array.put(j, v, null, null);

        CheckpointUtils.checkpointEvent(this, "ReplaceValueEvent",
            new Variable("index", j), new Variable("value", v), new Variable(
                "animstep", lang.getStep()));

        incrementNrAssignments();
        array.unhighlightElem(i, null, null);
        code.toggleHighlight("insert", "loop2");
        lang.nextStep();
      }
      code.toggleHighlight("loop2", "loop1");
    }

    code.unhighlight("loop1");
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
    // System.err.println(lang.toString());
    return lang.toString();
  }

  public String getAlgorithmName() {
    return "Shell Sort";
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
