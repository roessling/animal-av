package generators.graphics.helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WSTAlgo {

  public static List<List<Integer>> watershed(List<Integer> arr) {
    int n = 1;
    List<Integer> arr2 = arr;
    int max = arrayMax(arr2);
    arr2 = buildWalls(arr2);
    List<Integer> underwater = searchUnderwater(arr2, n);
    List<List<Integer>> connectedComponents = searchConnectedComponents(underwater);

    while (n < max + 1) {
      n++;
      List<Integer> newUnderwater = searchUnderwater(arr2, n);
      List<Integer> mergePositions = searchMergePositions(arr2, n, underwater,
          newUnderwater);
      for (int i : mergePositions)
        arr2.set(i, max + 1);
      underwater = searchUnderwater(arr2, n);
      connectedComponents = searchConnectedComponents(underwater);
    }

    return connectedComponents;
  }

  public static List<Integer> searchMergePositions(List<Integer> arr, int n,
      List<Integer> oldUnderwater, List<Integer> newUnderwater) {
    List<Integer> mergePos = new ArrayList<Integer>();
    for (int i = 1; i < arr.size() - 1; i++) {
      if (newUnderwater.contains(i) && !oldUnderwater.contains(i)) {
        if (arr.get(i - 1) < n - 1 && arr.get(i + 1) < n - 1)
          mergePos.add(i);
      }
    }
    return mergePos;
  }

  public static List<Integer> searchMergePositions(WSTAnim anim,
      List<Integer> underwater) {
    int n = anim.getWaterlevel();
    List<Integer> mergePos = new ArrayList<Integer>();
    for (int i = 0; i < underwater.size(); i++) {
      Integer elem = underwater.get(i);
      if (anim.getArrElement(elem) == n - 1) {
        if (anim.getArrElement(elem - 1) < n - 1
            && anim.getArrElement(elem + 1) < n - 1)
          mergePos.add(elem);
      }
    }
    return mergePos;
  }

  public static <T> List<T> intersect(List<T> A, List<T> B) {
    List<T> rtnList = new ArrayList<T>();
    for (T dto : A) {
      if (B.contains(dto)) {
        rtnList.add(dto);
      }
    }
    return rtnList;
  }

  public static List<List<Integer>> searchConnectedComponents(List<Integer> arr) {
    List<List<Integer>> result = new ArrayList<List<Integer>>();
    List<Integer> first = new ArrayList<Integer>();
    result.add(first);
    for (int i = 0; i < arr.size(); i++) {
      result.get(result.size() - 1).add(arr.get(i));
      if (i < arr.size() - 1) {
        if (arr.get(i + 1) != arr.get(i) + 1)
          result.add(new ArrayList<Integer>());
      }
    }
    return result;
  }

  public static List<Integer> searchUnderwater(List<Integer> arr, int n) {
    List<Integer> underwater = new ArrayList<Integer>();
    for (int i = 0; i < arr.size(); i++) {
      if (arr.get(i) < n)
        underwater.add(i);
    }
    return underwater;
  }

  public static List<Integer> searchUnderwater(WSTAnim anim) {
    List<Integer> underwater = new ArrayList<Integer>();
    for (int i = 0; i < anim.getElementCount(); i++) {
      if (anim.getArrElement(i) < anim.getWaterlevel())
        underwater.add(i);
    }
    return underwater;
  }

  public static List<Integer> buildWalls(List<Integer> arr) {
    int max = arrayMax(arr);
    arr.set(0, max + 1);
    arr.set(arr.size() - 1, max + 1);
    return arr;
  }

  public static void main(String[] args) {
    WSTAlgo algo = new WSTAlgo();
    List<List<Integer>> res = WSTAlgo.watershed(Arrays.asList(new Integer[] {
        0, 0, 2, 7, 4, 4, 5, 6, 5, 2, 2, 4, 3, 2, 0 }));
    algo.printLL(res);
  }

  public void printLL(List<List<Integer>> res) {
    String s = "";
    for (List<Integer> l : res) {
      for (Integer i : l) {
        s += i + ", ";
      }
      s = s.substring(0, s.length() - 2);
      s += "; ";
    }
    s = s.substring(0, s.length() - 2);
    System.out.println(s);
  }

  public static int arrayMax(List<Integer> arr) {
    int max = 0;
    for (int a : arr) {
      if (a > max)
        max = a;
    }
    return max;
  }

  public static int arrayMax(WSTAnim anim) {
    int max = 0;
    for (int i = 0; i < anim.getElementCount(); i++) {
      int tmp = anim.getArrElement(i);
      if (tmp > max)
        max = tmp;
    }
    return max;
  }
}
