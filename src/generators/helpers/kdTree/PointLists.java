package generators.helpers.kdTree;

//import java.awt.Point;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Comparator;

/**
 * @author mateusz
 */
public class PointLists {

  private boolean             isXplane          = true;

  protected int               nrOfPoints;

  private ArrayDeque<Point[]> currentPointLists = new ArrayDeque<Point[]>();
  private Point[][]           currentPointListArray;
  private ArrayDeque<Point[]> nextPointLists;

  static class Point extends java.awt.Point {

    public Point(int x, int y) {
      super(x, y);
    }

    @Override
    public String toString() {
      return "(" + this.x + "," + this.y + ")";
    }
  }

  /**
   * @param pointList
   */
  public PointLists(int[][] pointList) {
    super();
    if ((pointList == null) || (pointList[0].length != 2))
      throw new IllegalArgumentException("Illegal pointList, was " + pointList
          + ".");
    this.nrOfPoints = pointList.length;

    // fill ArrayDeque with first point list
    this.currentPointLists = new ArrayDeque<Point[]>();
    this.nextPointLists = new ArrayDeque<Point[]>();

    Point[] points = new Point[this.nrOfPoints];
    for (int i = 0; i < this.nrOfPoints; i++) {
      int x = pointList[i][0];
      int y = pointList[i][1];
      points[i] = new Point(x, y);
    }
    this.currentPointLists.offer(points);
    this.currentPointListArray = this.currentPointLists
        .toArray(new Point[0][0]);
  }

  class Xcomparator implements Comparator<Point> {

    /**
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(Point point1, Point point2) {
      if (point1.getX() < point2.getX())
        return -1;
      else if (point1.getX() > point2.getX())
        return 1;
      else
        return 0;
    }

  }

  public Point[] sortByX(Point[] points) {
    Arrays.sort(points, new Xcomparator());
    return points;
  }

  class Ycomparator implements Comparator<Point> {

    /**
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(Point point1, Point point2) {
      if (point1.getY() < point2.getY())
        return -1;
      else if (point1.getY() > point2.getY())
        return 1;
      else
        return 0;
    }
  }

  public Point[] sortByY(Point[] points) {
    Arrays.sort(points, new Ycomparator());
    return points;
  }

  protected class ExtractMedianResult {

    public ExtractMedianResult(Point median, int medianIndex, Point[] leftList,
        Point[] rightList) {
      super();
      this.median = median;
      this.medianIndex = medianIndex;
      this.leftList = leftList;
      this.rightList = rightList;
    }

    Point   median;
    int     medianIndex;
    Point[] leftList;
    Point[] rightList;

    @Override
    public String toString() {
      return "emr [" + this.median + "at " + this.medianIndex + ", "
          + Arrays.toString(this.leftList) + ", "
          + Arrays.toString(this.rightList) + "]";
    }

  }

  /**
   * Extracts the median and devides the point list into two new lists.
   * 
   * @param points
   * @return
   */
  private final ExtractMedianResult extractMedian(Point[] points) {
    int medianIndex = (points.length / 2); // takes the left
    Point median = points[medianIndex];
    Point[] leftList = (medianIndex == 0) ? new Point[0]
        : new Point[medianIndex];
    Point[] rightList = ((points.length - medianIndex) == 0) ? new Point[0]
        : new Point[points.length - medianIndex - 1];
    for (int i = 0; i < points.length; i++) {
      if (i < medianIndex)
        leftList[i] = points[i];
      else if (i > medianIndex)
        rightList[i - medianIndex - 1] = points[i];
    }
    return new ExtractMedianResult(median, medianIndex, leftList, rightList);
  }

  /**
   * ugly to have class-wide scope... but we need to store the state somewhere
   * between the get-invocations.
   */
  private int currentPointIndex = 0;
  private int offset            = 0;

  /**
   * Implements the main logic for getting median points.
   * 
   * @return the median point in the current list
   */
  public final Point getNextPoint() {

    Point result = null;

    // we process a set of point lists after another stored in 2
    // ArrayDeques
    if (this.currentPointLists.isEmpty()) {

      // check if we are done with processing points
      if (this.nextPointLists.isEmpty()) {
        displayFinishedGettingPoints();
        return null;
      }
      // signal we are done
      // else process more points
      this.currentPointLists = this.nextPointLists;
      this.currentPointListArray = this.nextPointLists.toArray(new Point[0][0]);
      this.nextPointLists = new ArrayDeque<Point[]>();

      // reset and display
      this.isXplane = !this.isXplane;
      this.currentPointIndex = this.offset = 0;
      displayProcessingMoreLists();

    }
    // if (this.offset == 0) {
    displayCurrentPointLists(this.currentPointListArray, this.isXplane);
    // }

    // sort a list
    Point[] currentPoints = this.currentPointLists.poll();
    if (this.isXplane)
      sortByX(currentPoints);
    else
      sortByY(currentPoints);
    displayAListIsSorted(this.currentPointIndex, this.isXplane);

    // if (this.offset == 0) {
    displayCurrentPointLists(this.currentPointListArray, this.isXplane);
    // }

    ExtractMedianResult emr = extractMedian(currentPoints);
    displayExtractedMedian(emr.median, emr.medianIndex, this.offset);
    result = emr.median;
    // filter out the empty point lists while building the next
    // ArrayDeque
    if (emr.leftList.length != 0)
      this.nextPointLists.offer(emr.leftList);
    if (emr.rightList.length != 0)
      this.nextPointLists.offer(emr.rightList);

    this.currentPointIndex++; // processing of this list is over
    this.offset += currentPoints.length;
    return result;
  }

  /**
   * @param median
   * @param medianIndex
   */
  protected void displayExtractedMedian(Point median, int medianIndex,
      int offset) {
    System.out.println("Median extracted: " + median + " at position "
        + medianIndex + " which means: " + (offset + medianIndex));
  }

  /**
   * @param currentPointIndex
   * 
   */
  protected void displayAListIsSorted(int currentPointIndex, boolean isXplane) {
    String plane = isXplane ? "x" : "y";
    System.out.println("Sorted list nr." + currentPointIndex + " by " + plane
        + "-value.");
  }

  /**
     * 
     */
  protected void displayProcessingMoreLists() {
    System.out.println("All point lists processed in this iteration.");
  }

  /**
   * @param isXplane
   * 
   */
  protected void displayCurrentPointLists(Point[][] currentPointListArray,
      boolean isXplane) {
    String plane = isXplane ? "x-value" : "y-value";
    System.out.println("For all point lists sort by " + plane
        + " and extract median.");
    String result = "Displaying lists: ";
    for (Point[] points : currentPointListArray) {
      result += Arrays.toString(points) + " ";
    }
    System.out.println(result);
  }

  /**
     * 
     */
  protected void displayFinishedGettingPoints() {
    System.out.println("Finish.");
  }
}
