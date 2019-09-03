package generators.backtracking.helpers;

import java.util.regex.Pattern;

public class Parser {

  /**
   * 
   * @param cs
   * @return a StringMatrix
   */
  public static String[][] parseString(String cs) {
    String helper = cs;
    helper = cs.replaceAll("\\s+", "");
    helper = helper + ",->"; // not elegant, but works

    String[] array = helper.split(Pattern.quote(","));

    String[][] matrix = new String[array.length][];
    int r = 0;
    for (String row : array) {
      matrix[r++] = row.split("\\->");
    }

    return matrix;
  }

}
