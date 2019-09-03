package generators.helpers.candidateElimination;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

/**
 * @author mateusz
 */
public class ExampleGenerator {

  public static String[][] generateInstances(Hashtable<String, Object> primProps) {
    if (primProps == null) {
      return generateHardCodedExamples();
    } else {
      return (String[][]) primProps.get("TrainingInstances");
    }
  }

  private static String[][] generateHardCodedExamples() {
    String[][] examples = new String[9][];
    examples[0] = new String[] { "sunny", "hot", "normal", "strong", "warm",
        "same", "+" };
    examples[1] = new String[] { "sunny", "hot", "high", "strong", "warm",
        "same", "+" };
    examples[2] = new String[] { "rainy", "cool", "high", "strong", "warm",
        "change", "-" };
    examples[3] = new String[] { "sunny", "hot", "high", "strong", "cool",
        "change", "+" };
    examples[4] = new String[] { "rainy", "hot", "high", "weak", "cool",
        "change", "-" };
    examples[5] = new String[] { "sunny", "cool", "high", "weak", "warm",
        "same", "+" };
    examples[6] = new String[] { "sunny", "hot", "high", "weak", "cool",
        "same", "+" };
    examples[7] = new String[] { "rainy", "hot", "high", "strong", "cool",
        "same", "-" };
    examples[8] = new String[] { "sunny", "coll", "high", "weak", "cool",
        "change", "+" };
    return examples;
  }

  public static String[][] generateFeatureValues(String[][] te) {
    int nrOfRows = te.length;
    int nrOfColumns = te[0].length - 1; // classification column!
    if (nrOfColumns <= 0)
      throw new IllegalArgumentException();
    String[][] featureStrings = new String[nrOfColumns][];

    // filter duplicates
    ArrayList<String> temp;
    for (int i = 0; i < nrOfColumns; i++) {
      temp = new ArrayList<String>(nrOfColumns);
      for (int j = 0; j < nrOfRows; j++) {
        String featureValue = te[j][i];
        if (!temp.contains(featureValue))
          temp.add(featureValue);
      }
      featureStrings[i] = temp.toArray(new String[0]);
    }
    return featureStrings;
  }

//  private static String[][] generateFeatureValues() {
//    String[][] featureStrings = new String[6][];
//    featureStrings[0] = new String[] { "sunny", "rainy" };
//    featureStrings[1] = new String[] { "hot", "cool" };
//    featureStrings[2] = new String[] { "normal", "high" };
//    featureStrings[3] = new String[] { "strong", "weak" };
//    featureStrings[4] = new String[] { "warm", "cool" };
//    featureStrings[5] = new String[] { "same", "change" };
//
//    return featureStrings;
//  }

  public static Example[] generateExamples(String[][] exampleGrid) {
    Example[] exampleArray = new Example[exampleGrid.length];
    for (int i = 0; i < exampleArray.length; i++) {
      exampleArray[i] = new Example(Arrays.copyOfRange(exampleGrid[i], 0,
          exampleGrid[i].length - 1),
          (exampleGrid[i][exampleGrid[i].length - 1].equals("+")) ? true
              : false);
    }
    return exampleArray;
  }

}
