package generators.graph.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class Pathfinder {
  Map<Integer, List<Integer>> nextIntegerMap;
  int[][]                     graphAdjacencyMatrix;
  List<List<Integer>>         directionList;

  public Pathfinder(int[][] graphAdjacencyMatrix) {
    this.graphAdjacencyMatrix = graphAdjacencyMatrix;
  }

  public List<List<Integer>> getDirections(Integer source, Integer destination) {
    Integer sourceInteger = source + 1;
    Integer destinationInteger = destination + 1;
    directionList = new ArrayList<List<Integer>>();
    // Initialization.
    nextIntegerMap = new HashMap<Integer, List<Integer>>();
    Integer currentInteger = sourceInteger;
    // Queue
    Queue<Integer> queue = new LinkedList<Integer>();
    queue.add(currentInteger);
    Map<Integer, Integer> pathLenght = new HashMap<Integer, Integer>();
    /*
     * The set of visited Integers doesn't have to be a Map, and, since order is
     * not important, an ordered collection is not needed. HashSet is fast for
     * add and lookup, if configured properly.
     */
    pathLenght.put(currentInteger, 0);
    // Search.
    while (!queue.isEmpty()) {
      currentInteger = queue.remove();
      if (currentInteger != destinationInteger) {
        for (Integer nextInteger : getChildIntegers(currentInteger)) {
          if (!pathLenght.containsKey(nextInteger)) {
            queue.add(nextInteger);
            pathLenght.put(nextInteger, pathLenght.get(currentInteger) + 1);
            // Look up of next Integer instead of previous.
            if (!nextIntegerMap.containsKey(nextInteger)) {
              List<Integer> tempList = new ArrayList<Integer>();
              tempList.add(currentInteger);
              nextIntegerMap.put(nextInteger, tempList);
            }
          } else if ((pathLenght.get(currentInteger) + 1) <= pathLenght
              .get(nextInteger)) {
            if (nextIntegerMap.containsKey(nextInteger)) {
              List<Integer> tempList = nextIntegerMap.get(nextInteger);
              tempList.add(currentInteger);
              nextIntegerMap.put(nextInteger, tempList);
            } else {
              List<Integer> tempList = new ArrayList<Integer>();
              tempList.add(currentInteger);
              nextIntegerMap.put(nextInteger, tempList);
            }
          }
        }
      }

    }
    reconstructPath(sourceInteger, destinationInteger, new ArrayList<Integer>());
    correctDirectionList();
    return directionList;
  }

  public void correctDirectionList() {
    for (List<Integer> list : directionList) {
      for (int i = 0; i < list.size(); i++)
        list.set(i, list.get(i) - 1);
    }
  }

  public void reconstructPath(int source, int current, List<Integer> untilNow) {
    if (source == current) {
      List<Integer> newUntilNow = new ArrayList<Integer>();
      newUntilNow.addAll(untilNow);
      newUntilNow.add(source);
      Collections.reverse(newUntilNow);
      directionList.add(newUntilNow);
    } else {
      if (nextIntegerMap.size() > 0 && nextIntegerMap.containsKey(current)) {
        for (int i = 0; i < nextIntegerMap.get(current).size(); i++) {
          List<Integer> newUntilNow = new ArrayList<Integer>();
          newUntilNow.addAll(untilNow);
          newUntilNow.add(current);
          reconstructPath(source, nextIntegerMap.get(current).get(i),
              newUntilNow);
        }
      }
    }
  }

  private List<Integer> getChildIntegers(Integer currentInteger) {
    List<Integer> childs = new ArrayList<Integer>();
    for (int i = 0; i < graphAdjacencyMatrix[currentInteger - 1].length; i++) {
      if (graphAdjacencyMatrix[currentInteger - 1][i] == 1) {
        childs.add(i + 1);
      }
    }
    return childs;
  }

  public List<Integer> getReachableNodes(Integer fromNode) {
    List<Integer> reachable = new ArrayList<Integer>();
    Queue<Integer> queue = new LinkedList<Integer>();
    List<Integer> visited = new ArrayList<Integer>();
    queue.addAll(getChildIntegers(fromNode + 1));
    visited.add(fromNode + 1);
    reachable.add(fromNode);
    while (!queue.isEmpty()) {
      Integer node = queue.remove();
      reachable.add(node - 1);
      visited.add(node);
      List<Integer> temp_nodes = getChildIntegers(node);
      while (!temp_nodes.isEmpty()) {
        Integer child_node = temp_nodes.remove(0);
        if (!(visited.contains(child_node) || queue.contains(child_node))) {
          queue.add(child_node);
        }
      }
    }
    return reachable;
  }
}