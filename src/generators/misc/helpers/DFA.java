package generators.misc.helpers;

public class DFA {
  // Variablen
  private int        states;  // Anzahl Zuständen (Namen: 0, 1, ... , states -
                               // 1)
  private int        start;   // Startzustand
  private char[]     alphabet;
  private String[][] matrix;
  private int[]      accepts;

  public DFA(int states, int start, char[] alphabet, String[][] matrix,
      int[] accepts) {
    this.states = states;
    this.start = start;
    this.alphabet = alphabet;
    this.matrix = matrix;
    this.accepts = accepts;
  }

  /**
   * Es wird davon ausgegangen, dass der Automat deterministisch ist
   */
  public int getNext(int actual, char sign) {
    String[] row = matrix[actual];
    for (int y = 0; y < row.length; y++) {
      if (row[y].contains("" + sign)) {
        return y;
      }
    }
    return -1;
  }

  public int getStates() {
    return states;
  }

  public int getStart() {
    return start;
  }

  public char[] getAlphabet() {
    return alphabet;
  }

  public String[][] getMatrix() {
    return matrix;
  }

  public int[] getAccepts() {
    return accepts;
  }

  public String printGraph(int locX, int locY) {
    String graph = "graph \"dfagraph\" size " + (states)
        + " directed weighted nodes ";

    // Knoten generieren
    String nodes = "{";
    int width = locX - 100;
    int height = locY;
    for (int i = 0; i < states; i++) {
      width += 100;
      if (i % 3 == 0 && i != 0) {
        height += 100;
        width = locX;
      }

      nodes += " \"" + i + "\" " + "( " + width + " , " + height + " )";

      if (i != states - 1) {
        nodes += " ,";
      }
    }
    nodes += " }";
    graph += nodes;

    // Kanten generieren
    String edges = " edges {";
    boolean empty = true;
    for (int x = 0; x < matrix.length; x++) {
      for (int y = 0; y < matrix[x].length; y++) {

        if (!matrix[x][y].equals("")) {
          edges += " (" + x + ", " + y + ", \"" + matrix[x][y] + "\" ) ,";
          empty = false;
        }
      }
    }
    if (!empty) {
      edges = edges.substring(0, edges.length() - 2);
    }
    edges += " }";
    graph += edges;
    return graph;
  }

  public String printAlphabet() {
    String result = "{";
    boolean first = true;
    for (int i = 0; i < alphabet.length; i++) {
      if (!first) {
        result += ",";
      }
      first = false;
      result += alphabet[i];
    }
    result += "}";
    return result;
  }

  public String printMatrix() {
    String result = "{";
    boolean first = true;

    for (int x = 0; x < matrix.length; x++) {
      for (int y = 0; y < matrix[x].length; y++) {

        if (!matrix[x][y].equals("")) {
          if (!first) {
            result += ",";
          }
          first = false;

          result += "(" + x + "," + matrix[x][y] + "," + y + ")";

        }
      }
    }
    result += "}";
    return result;
  }

  public String printAccepts() {
    String result = "{";
    boolean first = true;
    for (int i = 0; i < accepts.length; i++) {
      if (!first) {
        result += ",";
      }
      first = false;
      result += accepts[i];
    }
    result += "}";
    return result;
  }
}
