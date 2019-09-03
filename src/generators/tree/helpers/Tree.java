/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package generators.tree.helpers;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author Alex Krause, Markus Ermuth
 */
public class Tree {

  public static void main(String[] args) {
    Node root = new Node();
    Tree tree = new Tree(root);
    Node node1 = new Node();
    tree.insertNode(root, node1, 1);
    Node node3 = new Node();
    tree.insertNode(root, node3, 3);
    Node child1 = new Node();
    tree.insertNode(node1, child1, 0);
    Node child3 = new Node();
    tree.insertNode(node3, child3, 0);
    tree.insertNode(child3, new Node(), 2);
    System.out.println(tree.getSize());
    int[][] matrix = tree.getAdjacencyMatrix();
    for (int y = 0; y < matrix.length; y++) {
      System.out.println(Arrays.toString(matrix[y]));
    }
    System.out.println(Integer.toString(tree.getHeight()));
  }

  public Node root;

  public Tree(Node root) {
    this.root = root;
  }

  public void insertNode(Node parent, Node newNode, int index) {
    Node[] children = parent.getChildren();
    children[index] = newNode;
    for (int i = 0; i < 4; i++) {
      if (children[i] == null) {
        children[i] = new Node();
      }
    }
  }

  public int[][] getAdjacencyMatrix() {
    int size = root.getSize();
    int[][] matrix = new int[size][size];
    getAdjacencyMatrix(root, matrix, 0);
    // sortMatrix(matrix);
    transposeMatrix(matrix);
    return matrix;
  }

  private void getAdjacencyMatrix(Node currentNode, int[][] matrix, int index) {
    Node[] children = currentNode.getChildren();
    int nextIndex = index;
    if (children[0] == null) {
      return;
    }
    for (int i = 0; i < children.length; i++) {
      {
        matrix[nextIndex + 1][index] = 1;
        getAdjacencyMatrix(children[i], matrix, nextIndex + 1);
        nextIndex += children[i].getSize();

      }
    }

  }

  // private class ArrayIndexComparator implements Comparator<int[]> {
  //
  // public int compare(int[] a, int[] b) {
  // if (findFirstOne(a) < findFirstOne(b)) {
  // return -1;
  // }
  // if (findFirstOne(a) > findFirstOne(b)) {
  // return 1;
  // }
  //
  // return 0;
  // }
  //
  // private int findFirstOne(int[] a) {
  // for (int i = 0; i < a.length; i++) {
  // if (a[i] == 1) {
  // return i;
  // }
  // }
  // return Integer.MAX_VALUE;
  // }
  // }

  // private void sortMatrix(int[][] matrix) {
  // Comparator<int[]> comparator = new ArrayIndexComparator();
  // List<int[]> arrays = new ArrayList<int[]>(matrix.length);
  // for (int i = 1; i < matrix.length; i++) {
  // arrays.add(matrix[i]);
  // }
  // java.util.Collections.sort(arrays, comparator);
  // for (int i = 1; i < matrix.length; i++) {
  // matrix[i] = arrays.get(i-1);
  // }
  // }

  private void transposeMatrix(int[][] matrix) {
    for (int y = 0; y < matrix.length; y++) {
      for (int x = 0; x <= y; x++) {
        if (matrix[y][x] == 1) {
          matrix[y][x] = 0;
          matrix[x][y] = 1;
        }
      }
    }
  }

  public int getHeight() {
    return root.getHeight();
  }

  public int getSize() {
    return root.getSize();
  }

  public String[] getLabels() {
    List<String> labels = new ArrayList<String>();
    root.getLabels(labels);
    return labels.toArray(new String[getSize()]);
  }

  public Node[] getAllNodes() {
    List<Node> nodes = new ArrayList<Node>();
    root.getAllNodes(nodes);
    return nodes.toArray(new Node[nodes.size()]);
  }
}
