package generators.graph.helpers;

import java.util.LinkedList;
import java.util.List;

import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.util.Coordinates;

/**
 * Encapsulates the functionality for table-like outputs in animal.
 * 
 * @author chollubetz
 * 
 */
public class TableWrapper {

  Language   lang;
  Position   position;
  int        widthOfP;
  int        widthOfX;

  List<Text> Q;
  List<Text> P;
  List<Text> X;

  public TableWrapper(Language lang, Position position, int widthOfP,
      int widthOfX) {
    this.lang = lang;
    this.position = position;
    this.widthOfP = widthOfP;
    this.widthOfX = widthOfX;
    Q = new LinkedList<Text>();
    P = new LinkedList<Text>();
    X = new LinkedList<Text>();
  }

  int pToQ            = 68;
  int xToP            = 35;
  int distP           = 22;
  int distX           = 22;

  int linesContainedQ = 0;
  int linesContainedP = 0;
  int linesContainedX = 0;

  /**
   * Draws the Table.
   */
  public void draw() {
    Q.add(lang.newText(new Coordinates(position.getX(), position.getY()), "Q",
        "", null));
    for (int i = 0; i < widthOfP; i++)
      P.add(lang.newText(new Coordinates(position.getX() + pToQ + i * distP,
          position.getY()), "p" + (i + 1), "", null));
    for (int i = 0; i < widthOfX; i++)
      X.add(lang.newText(new Coordinates(position.getX() + pToQ
          + (widthOfP - 1) * distP + xToP + i * distX, position.getY()), "x"
          + i, "", null));
    linesContainedQ++;
    linesContainedP++;
    linesContainedX++;
  }

  /**
   * Adds a new line.
   * 
   * @param Q
   *          the Q part
   * @param P
   *          the P part
   * @param X
   *          the X part
   */
  public void add(Iterable<?> Q, Iterable<?> P, Iterable<?> X) {
    addQ(Q);
    addP(P);
    addX(X);
  }

  /**
   * Adds a new Q line.
   * 
   * @param Q
   *          new line
   */
  public void addQ(Iterable<?> Q) {
    this.Q.add(lang.newText(new Coordinates(position.getX(), position.getY()
        + linesContainedQ * 20), printSet(Q, "v"), "", null));
    linesContainedQ++;
  }

  /**
   * Adds a new P line.
   * 
   * @param P
   *          new line
   */
  public void addP(Iterable<?> P) {
    int i = 0;
    for (Object current : P) {
      this.P.add(lang.newText(new Coordinates(position.getX() + pToQ + i
          * distP, position.getY() + linesContainedP * 20), current.toString(),
          "", null));
      i++;
    }
    linesContainedP++;
  }

  /**
   * Adds a new X line.
   * 
   * @param X
   *          new line
   */
  public void addX(Iterable<?> X) {
    int i = 0;
    for (Object current : X) {
      this.X.add(lang.newText(new Coordinates(position.getX() + pToQ
          + (widthOfP - 1) * distP + xToP + i * distX, position.getY()
          + linesContainedX * 20), current.toString(), "", null));
      i++;
    }
    linesContainedX++;
  }

  /**
   * Visualizes an iterable via a string.
   * 
   * @param it
   *          an iterable
   * @param prefix
   *          a prefix that should be prewritten
   * @return the string representation
   */
  private String printSet(Iterable<?> it, String prefix) {
    StringBuilder sb = new StringBuilder("{");
    for (Object o : it)
      sb.append(prefix + o.toString() + ",");
    if (sb.toString().endsWith(","))
      sb.deleteCharAt(sb.lastIndexOf(","));
    sb.append("}");
    return sb.toString();
  }

  /**
   * Hides the Table.
   */
  public void hide() {
    for (Text c : Q)
      c.hide();
    for (Text c : P)
      c.hide();
    for (Text c : X)
      c.hide();
  }
}
