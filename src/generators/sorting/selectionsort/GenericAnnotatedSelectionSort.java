package generators.sorting.selectionsort;

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

public class GenericAnnotatedSelectionSort extends AnimatedIntArrayAlgorithm implements
		Generator {
	protected Text swapLabel, swapPerf;

	protected Locale contentLocale = null;
    
	public GenericAnnotatedSelectionSort(String aResourceName, Locale aLocale) {
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
		int minIndex = 0; // Speicher fuer aktuelles Element

		ArrayMarker iMarker = null, jMarker = null;

		code.highlight("header");
		lang.nextStep("Start of Algorithm");

		code.toggleHighlight("header", "variables");
		lang.nextStep();

		code.toggleHighlight("variables", "outerLoop");
		lang.nextStep();

		for (i = 0; i < array.getLength() - 1; i++) {
			if (iMarker == null)
				iMarker = installArrayMarker("iMarker", array, i);
			else
				iMarker.move(i, null, null);
			code.toggleHighlight("outerLoop", "setMinIndex");
			lang.nextStep("selectionSort, i=" +i);

			array.unhighlightElem(minIndex, null, null);
			minIndex = i;
			incrementNrAssignments();
			array.highlightElem(minIndex, null, null);
			code.toggleHighlight("setMinIndex", "innerLoop");
			lang.nextStep();

			for (j=i+1; j<array.getLength(); j++) {
				incrementNrComparisons();
				if (jMarker == null)
					jMarker = installArrayMarker("jMarker", array, j);
				else
					jMarker.move(j, null, null);
				code.toggleHighlight("innerLoop", "compare");
				lang.nextStep();

				if (array.getData(j) < array.getData(minIndex)) {
					incrementNrComparisons();
					code.toggleHighlight("compare", "minFound");
					lang.nextStep();

					array.unhighlightElem(minIndex, null, null);
					minIndex = j;
					incrementNrAssignments();
					array.highlightElem(minIndex, null, null);
					code.toggleHighlight("minFound", "innerLoop");
					lang.nextStep();
				} else {
					incrementNrComparisons();
					code.toggleHighlight("compare", "innerLoop");
					lang.nextStep();
				}
			}

			code.toggleHighlight("innerLoop", "swap");
			lang.nextStep();
			
			if (i != minIndex) {
  		  CheckpointUtils.checkpointEvent(this, "swapEvent",
            new Variable("index1", i),
            new Variable("value1", array.getData(i)),
            new Variable("index2", minIndex),
            new Variable("value2", array.getData(minIndex)),
            new Variable("animstep",lang.getStep()));
  			array.swap(i, minIndex, null, null);
			}
			
			incrementNrAssignments();
			code.toggleHighlight("swap", "outerLoop");
			array.unhighlightElem(i, null, null);
			array.highlightCell(i, null, null);
			lang.nextStep();
      CheckpointUtils.checkpointEvent(this, "nextStep", new Variable("animstep",lang.getStep()));
		}

		code.unhighlight("outerLoop");
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
