package generators.misc.nonuniformTM;

import java.util.ArrayList;

/**
 * Utility functions.
 * @author Giang Nam Nguyen
 *
 */
public class Utils {

  /**
   * Convert a decimal number to a binary string.
   * 
   * @param a given decimal number
   * @param numberOfBits number of bits used to represent
   * @return a binary string
   */
  public static String decToBin(int a, int numberOfBits) {
    String result = Integer.toBinaryString(a);
    while (result.length() < numberOfBits) {
        result = "0" + result;
    }
    return result;
  }
  
  /**
   * To check if a binary string equals 1 or not.
   * 
   * @param counterTapeString given binary string
   * @return true if equals, false otherwise
   */
  public static boolean isOne(ArrayList<String> count) {
    if (!count.get(count.size() - 1).equals("1"))
      return false;
    for (int i = 0; i < count.size() - 1; i++)
      if (!count.get(i).equals("0"))
        return false;
    return true;
  }
  
  /**
   * Find max of a and b.
   * 
   * @param a a
   * @param b b
   * @return max of a and b
   */
  public static int max(int a, int b) {
    return (a < b) ? b : a;
  }
  
  /**
   * Number of inputs required for given operator.
   * 
   * @param operator given operator (e.g. and, or, xor, not)
   * @return 2, when operator in {and, or, xor}, 1 when operator = not
   */
  public static int numberOfInputsRequired(String operator) {
    switch (operator) {
      case "AND":
      case "OR":
      case "XOR":
        return 2;
      case "NOT":
        return 1;
      default:
        return 0;
    }
  }
  
  /**
   * Logic calculator.
   * 
   * @param operator given operator (e.g. and, or, xor, not)
   * @param inputs array of int
   * @return value of logic calculation
   */
  public static int calculate(String operator, int[] inputs) {
    switch (operator) {
      case "AND":
        return inputs[0] & inputs[1];
      case "OR":
        return inputs[0] | inputs[1];
      case "XOR":
        return inputs[0] ^ inputs[1];
      default:
        return 1-inputs[0];
    }
  }
  
}
