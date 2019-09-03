package generators.searching;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.searching.helpers.KadaneScriptWriter;

import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;

public class Kadane implements Generator {
  private Language           lang;
  private int[]              array;
  private KadaneScriptWriter scriptWriter;

  public void init() {
    lang = new AnimalScript("Kadane's Algorithm", "Sheip Dargutev", 800, 600);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    scriptWriter = new KadaneScriptWriter(lang, props, primitives);
    array = (int[]) primitives.get("array");
    start();
    return lang.toString();
  }

  private void start() {
    scriptWriter.writeInitialization();
    getMaxSum(array);
  }

  public int getMaxSum(int[] array) {
    int current_max_sum = 0;
    int max_positive_sum_until_position = 0;
    int iterate = 0;
    int startIndex = 0;
    int endIndex = 0;
    int countStart = 0;
    scriptWriter.writeInitialSteps();
    int maxElIndex = maxElementIndex(array);
    int maxElement = array[maxElIndex];
    if (maxElement <= 0) {
      current_max_sum = maxElement;
      startIndex = maxElIndex;
      endIndex = maxElIndex;
      scriptWriter.writeAllNegative(maxElIndex);
    } else {
      for (int i = 0; i < array.length; i++) {
        if (max_positive_sum_until_position + array[i] > 0) {
          max_positive_sum_until_position = max_positive_sum_until_position
              + array[i];
          countStart++;
          scriptWriter.writeChangeMaxPositiveSumUntilPosition(i,
              current_max_sum, max_positive_sum_until_position);
        } else {
          max_positive_sum_until_position = 0;
          countStart = 0;
          scriptWriter.writeResetMaxPositiveSumUntilPosition(i,
              current_max_sum, max_positive_sum_until_position);
        }
        if (countStart == 1) {
          startIndex = iterate;
          scriptWriter.writeStartNewSubArray(startIndex, current_max_sum,
              max_positive_sum_until_position);
        }
        if (current_max_sum < max_positive_sum_until_position) {
          current_max_sum = max_positive_sum_until_position;
          endIndex = iterate;
          scriptWriter.writeIncrementMaxSum(startIndex, endIndex,
              current_max_sum, max_positive_sum_until_position);
        }
        iterate++;
      }
      scriptWriter.writeElseReturn();
    }
    System.out.println("Start Index: " + startIndex + "\nEnd Index: "
        + endIndex + "\nSum: " + current_max_sum);

    return current_max_sum;
  }

  private int maxElementIndex(int[] array) {
    int max = 0;
    for (int i = 0; i < array.length; i++) {
      if (array[max] < array[i]) {
        max = i;
      }
    }
    return max;
  }

  public String getName() {
    return "Kadane's Algorithm";
  }

  public String getAlgorithmName() {
    return "KadanesAlgorithm";
  }

  public String getAnimationAuthor() {
    return "Sheip Dargutev";
  }

  public String getDescription() {
    return "Kadane's algorithm consists of a scan through the array values, computing at each position the maximum subarray ending at that position."
        + "\nIn case of an array with negative only values it returns the biggest element of the array."
        + "\nFor more information visit:"
        + "\nhttp://en.wikipedia.org/wiki/Kadane's_algorithm";
  }

  public String getCodeExample() {
    return "public int getMaxSum(int[] array) {"
        + "\n"
        + "	int current_max_sum = 0;"
        + "\n"
        + "	int iterate = 0;"
        + "\n"
        + "	int startIndex = 0;"
        + "\n"
        + "	int endIndex = 0;"
        + "\n"
        + "	int countStart = 0;"
        + "\n"
        + "	int max_positive_sum_until_position = 0;"
        + "\n"
        + "	int maxElement=max(array);"
        + "\n"
        + "	if (maxElement <= 0) "
        + "\n"
        + "		current_max_sum=maxElement;"
        + "\n"
        + "	else{"
        + "\n"
        + "		for (int i : array) {"
        + "\n"
        + "			if (max_positive_sum_until_position + i > 0) {"
        + "\n"
        + "				max_positive_sum_until_position = max_positive_sum_until_position + i;"
        + "\n" + "				countStart++;" + "\n" + "			} else {" + "\n"
        + "				max_positive_sum_until_position = 0;" + "\n"
        + "				countStart = 0;" + "\n" + "			}" + "\n"
        + "			if (countStart == 1) {" + "\n" + "				startIndex = iterate;"
        + "\n" + "			}" + "\n"
        + "			if (current_max_sum < max_positive_sum_until_position) {" + "\n"
        + "				current_max_sum = max_positive_sum_until_position;" + "\n"
        + "				endIndex = iterate;" + "\n" + "			}" + "\n" + "			iterate++;"
        + "\n" + "		}" + "\n" + "	}" + "\n" + "	return current_max_sum;" + "\n"
        + "}";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.US;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SEARCH);
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

}