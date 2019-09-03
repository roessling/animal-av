package generators.maths;

import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Offset;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Kenten on 15.04.2016.
 */
public class TEUtil {
  /**
   * Finds the Index of a pair in the list, which has AFind as
   * the first value.
   * @param AList List to find the Key in
   * @param AFind Key of the Pair, which you want to find in the list
   * @param <K> ClassType of the Object to find
   * @param <V> ClassType of the Value
   * @return index of the pair, having AFind as first element.
   */
  public static <K, V> int IndexOf (List<TEPair<K, V>> AList, K AFind ) {
    for (int i = 0; i < AList.size(); i++) {
      if (AList.get(i).getKey().equals(AFind)) {
        return i;
      }
    }
    return -1;
  }

  public static String[][] BucketXY1ToMatrix (List<Integer> ABucket, int x_wsize, int y_wsize) {
    // Setup useful variables:
    int max_windowsize = Math.max(x_wsize, y_wsize);

    // Init result:
    String[][] result = new String[2][max_windowsize+1];
    for (int i = 0; i < result.length; i++) {
      for (int j = 0; j < result[i].length; j++) {
        result[i][j] = "";
      }
    }

    // Fill with data:
    for (int i = 0; i < x_wsize; i++) {
      result[0][max_windowsize-i-1] = ABucket.get(i).toString();
    }
    for (int i = 0; i < y_wsize; i++) {
      result[1][max_windowsize-i-1] = ABucket.get(i + x_wsize).toString();
    }
    result[1][max_windowsize] = ABucket.get( x_wsize + y_wsize ).toString();

    return result;
  }

  public static String[][] BucketXYToMatrix (List<Integer> ABucket, int x_wsize, int y_wsize) {
    // Setup useful variables:
    int max_windowsize = Math.max(x_wsize, y_wsize);

    // Init result:
    String[][] result = new String[2][max_windowsize];
    for (int i = 0; i < result.length; i++) {
      for (int j = 0; j < result[i].length; j++) {
        result[i][j] = "";
      }
    }

    // Fill with data:
    for (int i = 0; i < x_wsize; i++) {
      result[0][max_windowsize-i-1] = ABucket.get(i).toString();
    }
    for (int i = 0; i < y_wsize; i++) {
      result[1][max_windowsize-i-1] = ABucket.get(i + x_wsize).toString();
    }

    return result;
  }

  public static String[][] BucketY1ToMatrix (List<Integer> ABucket, int x_wsize, int y_wsize) {
    // Init result:
    String[][] result = new String[2][y_wsize+1];
    for (int i = 0; i < result.length; i++) {
      for (int j = 0; j < result[i].length; j++) {
        result[i][j] = "";
      }
    }

    // Fill with data:
    for (int i = 0; i < y_wsize; i++) {
      result[1][y_wsize-i-1] = ABucket.get(i).toString();
    }
    result[1][y_wsize] = ABucket.get(y_wsize).toString();

    return result;
  }

  public static String[][] BucketYToMatrix (List<Integer> ABucket, int x_wsize, int y_wsize) {
    // Init result:
    String[][] result = new String[2][y_wsize];
    for (int i = 0; i < result.length; i++) {
      for (int j = 0; j < result[i].length; j++) {
        result[i][j] = "";
      }
    }

    // Fill with data:
    for (int i = 0; i < y_wsize; i++) {
      result[1][y_wsize-i-1] = ABucket.get(i).toString();
    }

    return result;
  }

  public static TEPair<TEPair<List<Text>, Rect>, Integer> StringMatrixToTextFields (Language script, String[][] matrix, TextProperties tp, RectProperties bg, Offset pos, String name_prefix) {
    LinkedList<Text> result = new LinkedList<Text>();

    int longestString = 0;
    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < matrix[i].length; j++) {
        longestString = Math.max( longestString, matrix[i][j].length() );
      }
    }

    Font afont = (Font) tp.get( AnimationPropertiesKeys.FONT_PROPERTY );
    int yoffset = afont.getSize() + 2;
    int xoffset = afont.getSize() * longestString;
    int ystart = (int) Math.round( -(yoffset / 2.0) * (matrix.length - 1) );

    boolean b = true;

    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < matrix[i].length; j++) {
        if (matrix[i][j] != null && !matrix[i][j].equals("")) {
          result.add( script.newText(
            new Offset(pos.getX() + j * xoffset, pos.getY() + ystart + i * yoffset + (b ? -1 : 0), pos.getBaseID(), pos.getDirection() ),
            matrix[i][j],
            name_prefix + "_elem" + i + "_elem" + j,
            null,
            tp ) );
          b = false;
        }
      }
    }
    Rect result_R = script.newRect( new Offset( pos.getX() - 2, pos.getY() + ystart, pos.getBaseID(), pos.getDirection() ),
      new Offset( pos.getX() + xoffset *  matrix[0].length - 2, pos.getY() + ystart + matrix.length * yoffset + 1, pos.getBaseID(), pos.getDirection() ),
      name_prefix + "_background", null, bg );

    return new TEPair<TEPair<List<Text>, Rect>, Integer> (new TEPair<List<Text>, Rect> (result, result_R), xoffset *  matrix[0].length);
  }

  public static void newHighlight(SourceCode sCurCode, int start, int stop) {
    for (int i = start; i <= stop ; i++) {
      sCurCode.highlight(i);
    }
  }

  public static void delHighlight(SourceCode sCurCode, int start, int stop) {
    for (int i = start; i <= stop ; i++) {
      sCurCode.unhighlight(i);
    }
  }
}
