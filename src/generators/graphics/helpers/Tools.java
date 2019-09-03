package generators.graphics.helpers;

import java.util.ArrayList;
import java.util.List;

public class Tools {
  public static String[] intToStrArray(int[] a) {
    return intToStrArray(a, 3);
  }

  public static String[] intToStrArray(List<Integer> a, int width) {
    List<String> str = new ArrayList<String>();
    for (Integer i : a)
      str.add(intToStr(i, width));
    return str.toArray(new String[] {});
  }

  public static String[] intToStrArray(List<Integer> a) {
    return intToStrArray(a, 3);
  }

  public static int[] listToIntArray(List<Integer> a) {
    int[] arr = new int[a.size()];
    int i = 0;
    for (Integer in : a)
      arr[i++] = in;
    return arr;
  }

  public static String intToStr(int i) {
    return intToStr(i, 3);
  }

  public static String intToStr(int i, int width) {
    return String.format("%" + width + "s", i);
  }

  public static String[] intToStrArray(int[] a, int width) {
    String[] outA = new String[a.length];
    for (int i = 0; i < a.length; i++) {
      outA[i] = String.format("%" + width + "s", a[i]);
    }
    return outA;
  }

  public static int heightForValue(int value, int max) {
    return (100 * value / max);
  }
}
