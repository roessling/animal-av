package generators.graphics.helpers;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Circle;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.Text;
import algoanim.primitives.Triangle;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.TriangleProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class ImageMatrix {

  private static final int LEGEND_FIELD_SIZE = 40;
  private static final int LEGEND_SEPARATION = 10;

  public enum HighlightStyles {
    CURRENT, NEIGHBOR, CURRENT_NEIGHBOR, ROI, VISITED, NONVISITED
  };

  private Language                  lang;
  private Integer[][]               image;
  private Coordinate[]              seeds;
  private CountingList<Coordinate>  roi;
  private int                       length               = 200;
  private Coordinates               topLeft;                                           // =
                                                                                       // new
                                                                                       // Coordinates(300,
                                                                                       // 300);
  private Rect[][]                  rects;
  private Circle[]                  seedsAnim;
  private int                       max;
  private int                       index                = 0;
  private Text[]                    labels;

  /*
   * private Color highlightColor = Color.GREEN; private Color neighborColor =
   * Color.CYAN; private Color currentNeighborColor = Color.BLUE; private Color
   * roiColor = Color.YELLOW; private Color visColor = Color.YELLOW; private
   * Color nonVisColor = Color.MAGENTA; private Color seedColor = Color.GREEN;
   */
  private TextProperties            textProperties;
  private TriangleProperties        visitedProperties;
  private TriangleProperties        nonVisitedProperties;
  private CircleProperties          seedProperties;
  private RectProperties            currentProperties, neighborProperties,
      currentNeighborProperties, roiProperties;

  private Rect                      currentCache         = null;
  private Rect                      currentNeighborCache = null;
  private Map<Coordinate, Rect>     neighborCache        = null;
  private Map<Coordinate, Rect>     roiCache             = null;

  private Map<Coordinate, Triangle> visCache             = null;
  private Map<Coordinate, Triangle> nonVisCache          = null;

  private List<Primitive>           legendObjects        = new ArrayList<Primitive>();

  public ImageMatrix(Integer[][] image, Coordinate[] seeds, Language lang,
      Coordinates topLeft) {
    this.image = image;
    this.lang = lang;
    this.seeds = seeds;
    this.seedsAnim = new Circle[seeds.length];
    this.roi = new CountingList<Coordinate>(null, null);
    this.topLeft = topLeft;
    labels = new Text[image.length + image[0].length + 2];
    max = getMax(image);

    neighborCache = new HashMap<Coordinate, Rect>();
    roiCache = new HashMap<Coordinate, Rect>();
    visCache = new HashMap<Coordinate, Triangle>();
    nonVisCache = new HashMap<Coordinate, Triangle>();
  }

  private void initialiseProperties() {
    /*
     * textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new
     * Font(Font.SANS_SERIF, Font.BOLD, 10));
     * visitedProp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
     * visitedProp.set(AnimationPropertiesKeys.FILL_PROPERTY, visColor);
     * visitedProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, visColor);
     * nonVisitedProp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
     * nonVisitedProp.set(AnimationPropertiesKeys.FILL_PROPERTY, nonVisColor);
     * nonVisitedProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, nonVisColor);
     */
  }

  /**
   * make the x/y coordinate axis and give it numbers
   */
  private void initialiseAxis() {
    int i = 0;
    // make x axis
    labels[i++] = lang.newText(new Coordinates(topLeft.getX() - lengthOfField()
        / 4, topLeft.getY() - lengthOfField() / 2), "x", getNextName(), null,
        textProperties);
    for (int j = 0; j < image[0].length; j++) {
      Rect r = rects[0][j];
      labels[i++] = lang.newText(new Offset(lengthOfField() / 4,
          -lengthOfField() / 2, r, AnimalScript.DIRECTION_NW), new Integer(j)
          .toString(), getNextName(), null, textProperties);
    }

    // make y axis
    labels[i++] = lang.newText(new Coordinates(topLeft.getX() - lengthOfField()
        / 2, topLeft.getY() - lengthOfField() / 4), "y", getNextName(), null,
        textProperties);
    for (int j = 0; j < image.length; j++) {
      Rect r = rects[j][0];
      labels[i++] = lang.newText(new Offset(-lengthOfField() / 2,
          lengthOfField() / 4, r, AnimalScript.DIRECTION_NW), new Integer(j)
          .toString(), getNextName(), null, textProperties);
    }
  }

  private Coordinates legendUpperLeft(Coordinates topLeft, int i) {
    int upperLeftX = topLeft.getX();
    int upperLeftY = topLeft.getY()
        + (i * (LEGEND_FIELD_SIZE + LEGEND_SEPARATION));
    return new Coordinates(upperLeftX, upperLeftY);
  }

  private Coordinates legendBottomRight(Coordinates topLeft, int i) {
    int bottomRightX = topLeft.getX() + LEGEND_FIELD_SIZE;
    int bottomRightY = topLeft.getY() + LEGEND_FIELD_SIZE
        + (i * (LEGEND_FIELD_SIZE + LEGEND_SEPARATION));
    return new Coordinates(bottomRightX, bottomRightY);
  }

  public void drawLegend() {
    Coordinates c = new Coordinates(800, 100);
    // RectProperties rectProps = new RectProperties();

    for (int i = 0; i < 7; i++) {
      Coordinates upperLeft = legendUpperLeft(c, i);
      Coordinates bottomRight = legendBottomRight(c, i);

      RectProperties legendRectProps = new RectProperties();
      legendRectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
      legendRectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(128,
          128, 128));
      Rect r = lang.newRect(upperLeft, bottomRight, getNextName(), null,
          legendRectProps);
      legendObjects.add(r);

      String legendText = "NO TEXT ERROR";
      if (i == 0) {
        // seed
        Node center = new Coordinates(
            (upperLeft.getX() + bottomRight.getX()) / 2,
            (upperLeft.getY() + bottomRight.getY()) / 2);

        /*
         * CircleProperties circleProps = new CircleProperties();
         * circleProps.set(AnimationPropertiesKeys.FILL_PROPERTY, seedColor);
         * circleProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
         */

        legendObjects.add(lang.newCircle(center, LEGEND_FIELD_SIZE / 4, "c_"
            + c.getX() + "_" + c.getY(), null, seedProperties));
        legendText = "seed point";
      } else if (i == 1) {
        // highlight (current)
        upperLeft = new Coordinates(upperLeft.getX() + LEGEND_FIELD_SIZE / 4,
            upperLeft.getY() + LEGEND_FIELD_SIZE / 4);
        bottomRight = new Coordinates(bottomRight.getX() - LEGEND_FIELD_SIZE
            / 4, bottomRight.getY() - LEGEND_FIELD_SIZE / 4);
        // rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        // rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, highlightColor);

        legendObjects.add(lang.newRect(upperLeft, bottomRight, getNextName(),
            null, currentProperties));
        legendText = "current element (v)";
      } else if (i == 2) {
        // neighbors
        upperLeft = new Coordinates(upperLeft.getX() + 2, upperLeft.getY() + 2);
        bottomRight = new Coordinates(bottomRight.getX() - 2,
            bottomRight.getY() - 2);
        // rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, false);
        // rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, neighborColor);
        legendObjects.add(lang.newRect(upperLeft, bottomRight, getNextName(),
            null, neighborProperties));
        legendText = "Neighbors";
      } else if (i == 3) {
        // current neighbor
        upperLeft = new Coordinates(upperLeft.getX() + LEGEND_FIELD_SIZE / 4,
            upperLeft.getY() + LEGEND_FIELD_SIZE / 4);
        bottomRight = new Coordinates(bottomRight.getX() - LEGEND_FIELD_SIZE
            / 4, bottomRight.getY() - LEGEND_FIELD_SIZE / 4);
        // rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        // rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY,
        // currentNeighborColor);
        legendObjects.add(lang.newRect(upperLeft, bottomRight, getNextName(),
            null, currentNeighborProperties));
        legendText = "Current Neighbor (neighbor)";
      } else if (i == 4) {
        // roi
        upperLeft = new Coordinates(upperLeft.getX() + 1, upperLeft.getY() + 1);
        bottomRight = new Coordinates(bottomRight.getX() - 1,
            bottomRight.getY() - 1);
        // rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, false);
        // rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, roiColor);
        legendObjects.add(lang.newRect(upperLeft, bottomRight, getNextName(),
            null, roiProperties));
        legendText = "Region of Interest (roi)";
      } else if (i == 5) {
        // visited
        Node x = new Coordinates(upperLeft.getX() + LEGEND_FIELD_SIZE * 3 / 4,
            upperLeft.getY() + 1);
        Node y = new Coordinates(bottomRight.getX() - 1, upperLeft.getY() + 1);
        Node z = new Coordinates(bottomRight.getX() - 1, upperLeft.getY()
            + LEGEND_FIELD_SIZE * 1 / 4);
        legendObjects.add(lang.newTriangle(x, y, z, getNextName(), null,
            visitedProperties));
        legendText = "visited coordinates";
      } else if (i == 6) {
        // not visited
        Node x = new Coordinates(upperLeft.getX() + LEGEND_FIELD_SIZE * 3 / 4,
            bottomRight.getY() - 1);
        Node y = new Coordinates(bottomRight.getX() - 1, bottomRight.getY() - 1);
        Node z = new Coordinates(bottomRight.getX() - 1, upperLeft.getY()
            + LEGEND_FIELD_SIZE * 3 / 4);
        legendObjects.add(lang.newTriangle(x, y, z, getNextName(), null,
            nonVisitedProperties));
        legendText = "non visited coordinates";
      }

      legendObjects.add(lang.newText(new Offset(10, LEGEND_FIELD_SIZE / 4, r,
          AnimalScript.DIRECTION_NE), legendText, getNextName(), null,
          textProperties));
    }

  }

  public void highlightCoordinate(Coordinate c, HighlightStyles s) {
    int i = c.getY();
    int j = c.getX();
    int upperLeftX = topLeft.getX() + (j * lengthOfField());
    int upperLeftY = topLeft.getY() + (i * lengthOfField());
    int bottomRightX = topLeft.getX() + ((j + 1) * lengthOfField());
    int bottomRightY = topLeft.getY() + ((i + 1) * lengthOfField());

    RectProperties rectProps = new RectProperties();
    if (s == HighlightStyles.CURRENT)
      makeCurrentHighlight(upperLeftX, upperLeftY, bottomRightX, bottomRightY,
          rectProps);
    else if (s == HighlightStyles.CURRENT_NEIGHBOR)
      makeCurrentNeighborHighlight(upperLeftX, upperLeftY, bottomRightX,
          bottomRightY, rectProps);
    else if (s == HighlightStyles.NEIGHBOR) {
      if (neighborCache.containsKey(c))
        return;
      makeNeighborHighlight(c, upperLeftX, upperLeftY, bottomRightX,
          bottomRightY, rectProps);
    } else if (s == HighlightStyles.ROI) {
      if (roiCache.containsKey(c))
        return;
      makeRoiHighlight(c, upperLeftX, upperLeftY, bottomRightX, bottomRightY,
          rectProps);
    } else if (s == HighlightStyles.VISITED) {
      if (visCache.containsKey(c))
        return;
      makeVisitedHighlight(c, upperLeftX, upperLeftY, bottomRightX);
    } else if (s == HighlightStyles.NONVISITED) {
      if (nonVisCache.containsKey(c))
        return;
      makeNonVisitedHighlight(c, upperLeftX, upperLeftY, bottomRightX,
          bottomRightY);
    }
  }

  public void highlightNeighbors(List<Coordinate> lc) {
    clearNeighborHighlight();
    for (Coordinate c : lc)
      highlightCoordinate(c, HighlightStyles.NEIGHBOR);
  }

//  private void clearLabels() {
//    for (Text t : labels)
//      if (t != null)
//        t.hide();
//  }

  private void clearCurrentHighlight() {
    if (currentCache != null)
      currentCache.hide();
    currentCache = null;
  }

  private void clearCurrentNeighborHighlight() {
    if (currentNeighborCache != null)
      currentNeighborCache.hide();
    currentNeighborCache = null;
  }

  private void clearNeighborHighlight() {
    for (Entry<Coordinate, Rect> e : neighborCache.entrySet()) {
      e.getValue().hide();
    }

    neighborCache.clear();
  }

//  private void clearRoiHighlight() {
//    for (Entry<Coordinate, Rect> e : roiCache.entrySet()) {
//      e.getValue().hide();
//    }
//
//    roiCache.clear();
//  }

  /**
   * remove everything drawn by ImageMatrix
   */
  public void clearAll() {
    // clearRoiHighlight();
    clearCurrentHighlight();
    clearNeighborHighlight();
    clearCurrentNeighborHighlight();
    // clearRects();
    clearTriangles();
    // clearSeedsAnim();
    // clearLabels();
    // clearLegend();
  }

//  private void clearSeedsAnim() {
//    for (Circle c : seedsAnim)
//      c.hide();
//  }

//  private void clearRects() {
//    for (Rect[] ra : rects)
//      for (Rect r : ra)
//        r.hide();
//  }

  private void clearTriangles() {
    for (Entry<Coordinate, Triangle> e : visCache.entrySet()) {
      e.getValue().hide();
    }
    for (Entry<Coordinate, Triangle> e : nonVisCache.entrySet()) {
      e.getValue().hide();
    }

    visCache.clear();
    nonVisCache.clear();
  }

//  private void clearLegend() {
//    for (Primitive p : legendObjects)
//      p.hide();
//
//    legendObjects.clear();
//  }

  private void makeNeighborHighlight(Coordinate c, int upperLeftX,
      int upperLeftY, int bottomRightX, int bottomRightY,
      RectProperties rectProps) {
    Node upperLeft = new Coordinates(upperLeftX + 2, upperLeftY + 2);
    Node bottomRight = new Coordinates(bottomRightX - 2, bottomRightY - 2);
    // rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, false);
    // rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, neighborColor);
    neighborCache.put(c, lang.newRect(upperLeft, bottomRight, getNextName(),
        null, neighborProperties));
  }

  private void makeCurrentNeighborHighlight(int upperLeftX, int upperLeftY,
      int bottomRightX, int bottomRightY, RectProperties rectProps) {
    clearCurrentNeighborHighlight();
    Node upperLeft = new Coordinates(upperLeftX + lengthOfField() / 4,
        upperLeftY + lengthOfField() / 4);
    Node bottomRight = new Coordinates(bottomRightX - lengthOfField() / 4,
        bottomRightY - lengthOfField() / 4);
    // rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    // rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY,
    // currentNeighborColor);
    currentNeighborCache = lang.newRect(upperLeft, bottomRight, getNextName(),
        null, currentNeighborProperties);
  }

  private void makeCurrentHighlight(int upperLeftX, int upperLeftY,
      int bottomRightX, int bottomRightY, RectProperties rectProps) {
    clearCurrentHighlight();
    Node upperLeft = new Coordinates(upperLeftX + lengthOfField() / 4,
        upperLeftY + lengthOfField() / 4);
    Node bottomRight = new Coordinates(bottomRightX - lengthOfField() / 4,
        bottomRightY - lengthOfField() / 4);
    // rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    // rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, highlightColor);
    currentCache = lang.newRect(upperLeft, bottomRight, getNextName(), null,
        currentProperties);
  }

  private void makeRoiHighlight(Coordinate c, int upperLeftX, int upperLeftY,
      int bottomRightX, int bottomRightY, RectProperties rectProps) {
    Node upperLeft = new Coordinates(upperLeftX + 1, upperLeftY + 1);
    Node bottomRight = new Coordinates(bottomRightX - 1, bottomRightY - 1);
    // rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, false);
    // rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, roiColor);
    roiCache.put(c, lang.newRect(upperLeft, bottomRight, getNextName(), null,
        roiProperties));
  }

  private void makeNonVisitedHighlight(Coordinate c, int upperLeftX,
      int upperLeftY, int bottomRightX, int bottomRightY) {
    Node x = new Coordinates(upperLeftX + lengthOfField() * 3 / 4,
        bottomRightY - 1);
    Node y = new Coordinates(bottomRightX - 1, bottomRightY - 1);
    Node z = new Coordinates(bottomRightX - 1, upperLeftY + lengthOfField() * 3
        / 4);
    nonVisCache.put(c,
        lang.newTriangle(x, y, z, getNextName(), null, nonVisitedProperties));
  }

  private void makeVisitedHighlight(Coordinate c, int upperLeftX,
      int upperLeftY, int bottomRightX) {
    Node x = new Coordinates(upperLeftX + lengthOfField() * 3 / 4,
        upperLeftY + 1);
    Node y = new Coordinates(bottomRightX - 1, upperLeftY + 1);
    Node z = new Coordinates(bottomRightX - 1, upperLeftY + lengthOfField() * 1
        / 4);
    visCache.put(c,
        lang.newTriangle(x, y, z, getNextName(), null, visitedProperties));
  }

  private String getNextName() {
    index++;
    return "highrect" + index;
  }

  public void deHighlightCoordinate(HighlightStyles s) {
    if (s == HighlightStyles.CURRENT)
      clearCurrentHighlight();
    else if (s == HighlightStyles.CURRENT_NEIGHBOR)
      clearCurrentNeighborHighlight();
    else if (s == HighlightStyles.NEIGHBOR)
      clearNeighborHighlight();
  }

  public void deHighlightCoordinate(Coordinate c, HighlightStyles s) {
    if (s == HighlightStyles.NONVISITED && nonVisCache.containsKey(c)) {
      nonVisCache.get(c).hide();
      nonVisCache.remove(c);
    } else if (s == HighlightStyles.VISITED && visCache.containsKey(c)) {
      visCache.get(c).hide();
      visCache.remove(c);
    }
  }

  private int lengthOfField() {
    return length / image.length;
  }

  public boolean isSeed(int x, int y) {
    for (Coordinate c : seeds)
      if (c.getX() == x && c.getY() == y)
        return true;
    return false;
  }

  public static int getMax(Integer[][] image) {
    int result = Integer.MIN_VALUE;
    for (Integer[] arr : image)
      for (int v : arr)
        if (v > result)
          result = v;
    return result;
  }

  private void initializeRects() {
    rects = new Rect[image.length][];
    for (int i = 0; i < image.length; i++) {
      rects[i] = new Rect[image[i].length];
      for (int j = 0; j < image[i].length; j++) {
        rects[i][j] = initRect(i, j, image[i][j]);
      }
    }
  }

  private void initializeSeeds() {
    int i = 0;
    for (Coordinate c : seeds) {
      Node center = new Offset(lengthOfField() / 2, lengthOfField() / 2,
          rects[c.getY()][c.getX()], AnimalScript.DIRECTION_NW);

      // CircleProperties circleProps = new CircleProperties();
      // circleProps.set(AnimationPropertiesKeys.FILL_PROPERTY, seedColor);
      // circleProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);

      Circle circle = lang.newCircle(center, lengthOfField() / 4,
          "c_" + c.getX() + "_" + c.getY(), null, seedProperties);
      seedsAnim[i++] = circle;
    }
  }

  private Rect initRect(int i, int j, int value) {
    Node upperLeft = new Coordinates(topLeft.getX() + (j * lengthOfField()),
        topLeft.getY() + (i * lengthOfField()));
    Node bottomRight = new Coordinates(topLeft.getX()
        + ((j + 1) * lengthOfField()), topLeft.getY()
        + ((i + 1) * lengthOfField()));

    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(value
        * (255 / max), value * (255 / max), value * (255 / max)));
    return lang.newRect(upperLeft, bottomRight, "r_" + i + "_" + j, null,
        rectProps);
  }

  public void addToROI(Coordinate v) {
    roi.add(v);
    highlightCoordinate(v, ImageMatrix.HighlightStyles.ROI);
    // updateROIS();
  }

  public CountingList<Coordinate> getROI() {
    return roi;
  }

  public TextProperties getTextProperties() {
    return textProperties;
  }

  public void setTextProperties(TextProperties textProperties) {
    this.textProperties = textProperties;
  }

  public TriangleProperties getVisitedProperties() {
    return visitedProperties;
  }

  public void setVisitedProperties(TriangleProperties visitedProperties) {
    this.visitedProperties = visitedProperties;
  }

  public TriangleProperties getNonVisitedProperties() {
    return nonVisitedProperties;
  }

  public void setNonVisitedProperties(TriangleProperties nonVisitedProperties) {
    this.nonVisitedProperties = nonVisitedProperties;
  }

  public CircleProperties getSeedProperties() {
    return seedProperties;
  }

  public void setSeedProperties(CircleProperties seedProperties) {
    this.seedProperties = seedProperties;
  }

  public RectProperties getCurrentProperties() {
    return currentProperties;
  }

  public void setCurrentProperties(RectProperties currentProperties) {
    this.currentProperties = currentProperties;
  }

  public RectProperties getNeighborProperties() {
    return neighborProperties;
  }

  public void setNeighborProperties(RectProperties neighborProperties) {
    this.neighborProperties = neighborProperties;
  }

  public RectProperties getCurrentNeighborProperties() {
    return currentNeighborProperties;
  }

  public void setCurrentNeighborProperties(
      RectProperties currentNeighborProperties) {
    this.currentNeighborProperties = currentNeighborProperties;
  }

  public RectProperties getRoiProperties() {
    return roiProperties;
  }

  public void setRoiProperties(RectProperties roiProperties) {
    this.roiProperties = roiProperties;
  }

  public void init() {
    initialiseProperties();
    initializeRects();
    initializeSeeds();
    initialiseAxis();
  }

}
