package algoanim.properties;

import java.awt.Color;
import java.awt.Font;
import java.util.Set;

import algoanim.properties.AnimationProperties;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArcProperties;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CircleProperties;
import algoanim.properties.CircleSegProperties;
import algoanim.properties.EllipseProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.ListElementProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PointProperties;
import algoanim.properties.PolygonProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.SquareProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.TriangleProperties;

public class PropertiesBuilder {

  private static Font DEFAULT_FONT  = new Font(Font.SANS_SERIF, 16, Font.PLAIN);
  private static int  DEFAULT_DEPTH = 1;

  // Arc Properties

  /**
   * inserts the depth and hidden values
   * 
   * @param properties
   *          the AnimationProperties object to be updated
   * @param depth
   *          the target depth
   * @param isHidden
   *          whether the object is hidden
   */
  public static void addDepthAndHiddenOptions(AnimationProperties properties,
      int depth, boolean isHidden) {
    properties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, depth);
    properties.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, isHidden);
  }

  /**
   * inserts the "arrow options" (forward arrow, backward arrow, or both)
   * 
   * @param properties
   *          the properties to be updated
   * @param hasForwardArrow
   * @param hasBackwardArrow
   */
  public static void setArrowOptions(AnimationProperties properties,
      boolean hasForwardArrow, boolean hasBackwardArrow) {
    properties.set(AnimationPropertiesKeys.FWARROW_PROPERTY, hasForwardArrow);
    properties.set(AnimationPropertiesKeys.BWARROW_PROPERTY, hasBackwardArrow);
  }

  /**
   * creates a new instance of an ArcProperties object by copying all fitting
   * properties from the AnimationProperties instance passed in
   * 
   * @param cloneFrom
   *          the basic AnimationProperties object to copy from
   * @return a new ArcProperties instance with the selected properties
   */
  public static ArcProperties createArcProperties(AnimationProperties cloneFrom) {
    ArcProperties prop = new ArcProperties();
    copyIn(prop, cloneFrom);
    return prop;
  }

  /**
   * creates a new ArcProperties instance from the values passed in
   * 
   * @param color
   *          the chosen color
   * @param fillColor
   *          the chosen fill color, assuming that the element is filled
   * @param angle
   *          the total angle of the arc
   * @param startAngle
   *          the starting angle of the arc
   * @param clockwise
   *          if true, the angle is measured in clockwise orientation, otherwise
   *          in counterclockwise orientation
   * @param closed
   *          if true, the arc is closed (and may thus also be filled)
   * @return the created ArcProperties instance with the above values
   */
  public static ArcProperties createArcProperties(Color color, Color fillColor,
      int angle, int startAngle, boolean clockwise, boolean closed,
      boolean filled) {
    return createArcProperties(color, fillColor, angle, startAngle, clockwise,
        closed, false, DEFAULT_DEPTH, false, false, false);
  }

  /**
   * creates a new ArcProperties instance from the values passed in
   * 
   * @param color
   *          the chosen color
   * @param fillColor
   *          the chosen fill color, assuming that the element is filled
   * @param angle
   *          the total angle of the arc
   * @param startAngle
   *          the starting angle of the arc
   * @param clockwise
   *          if true, the angle is measured in clockwise orientation, otherwise
   *          in counterclockwise orientation
   * @param closed
   *          if true, the arc is closed (and may thus also be filled)
   * @param filled
   *          if true, the arc is filled. Note: this is only effective if the
   *          arc is also closed
   * @param depth
   *          the depth of the arc
   * @param hidden
   *          if true, the arc is initially hidden
   * @param bwArrow
   *          determines if the arc shall have a backward-pointing arrow
   * @param fwArrow
   *          determines if the arc shall have a forward-pointing arrow
   * @return the created ArcProperties instance with the above values
   */
  public static ArcProperties createArcProperties(Color color, Color fillColor,
      int angle, int startAngle, boolean clockwise, boolean closed,
      boolean filled, int depth, boolean hidden, boolean bwArrow,
      boolean fwArrow) {
    ArcProperties prop = new ArcProperties();
    setStandards(prop, color, fillColor, depth, hidden);
    prop.set(AnimationPropertiesKeys.ANGLE_PROPERTY, angle);
    prop.set(AnimationPropertiesKeys.STARTANGLE_PROPERTY, startAngle);
    prop.set(AnimationPropertiesKeys.CLOCKWISE_PROPERTY, clockwise);
    prop.set(AnimationPropertiesKeys.COUNTERCLOCKWISE_PROPERTY, !clockwise);
    prop.set(AnimationPropertiesKeys.CLOSED_PROPERTY, closed);
    setArrowOptions(prop, fwArrow, bwArrow);
    return prop;
  }

  /**
   * creates a new ArrayMarkerProperties instance from the values passed in
   * 
   * @param depth
   *          the depth of the arc
   * @param color
   *          the chosen color
   * @param label
   *          the chosen label for the ArrayMarker
   * @param hidden
   *          if true, the arc is initially hidden
   * @return the created ArrayMarkerProperties instance with the above values
   */
  public static ArrayMarkerProperties createArrayMarkerProperties(int depth,
      Color color, String label, boolean hidden) {
    ArrayMarkerProperties prop = new ArrayMarkerProperties();
    setStandards(prop, color, null, depth, hidden);
    if (label != null)
      prop.set(AnimationPropertiesKeys.LABEL_PROPERTY, label);
    return prop;
  }

  /**
   * creates a new instance of an ArrayMarkerProperties object by copying all
   * fitting properties from the AnimationProperties instance passed in
   * 
   * @param cloneFrom
   *          the basic AnimationProperties object to copy from
   * @return a new ArrayProperties instance with the selected properties
   */
  public static ArrayMarkerProperties createArrayMarkerProperties(
      AnimationProperties cloneFrom) {
    ArrayMarkerProperties prop = new ArrayMarkerProperties();
    copyIn(prop, cloneFrom);
    return prop;
  }

  /**
   * creates a new ArrayProperties instance from the values passed in
   * 
   * @param color
   *          the chosen color for the array boundary
   * @param fillColor
   *          the chosen fill color for the array cells
   * @param elementColor
   *          the color for the array values
   * @param cellHighlight
   *          the highlight color for the cells
   * @param elemHighlight
   *          the highlight color for the values
   * @param direction
   *          if false, the array is in horizontal orientation, else vertical
   * @return a new ArrayProperties instance with the selected properties
   */
  public static ArrayProperties createArrayProperties(Color color,
      Color fillColor, Color elementColor, Color cellHighlight,
      Color elemHighlight, boolean direction) {
    return createArrayProperties(color, fillColor, elementColor, cellHighlight,
        elemHighlight, direction, DEFAULT_DEPTH, false, false);
  }

  /**
   * creates a new ArrayProperties instance from the values passed in
   * 
   * @param color
   *          the chosen color for the array boundary
   * @param fillColor
   *          the chosen fill color for the array cells
   * @param elementColor
   *          the color for the array values
   * @param cellHighlight
   *          the highlight color for the cells
   * @param elemHighlight
   *          the highlight color for the values
   * @param direction
   *          if false, the array is in horizontal orientation, else vertical -
   *          <strong>deprecated</strong>
   * @param depth
   *          the depth of the object (the higher, the further towards the
   *          background)
   * @param hidden
   *          whether the object is hidden (=true) or not
   * @param cascaded
   *          if true, show "cell by cell" - <strong>deprecated</strong>
   * @return a new ArrayProperties instance with the selected properties
   */
  public static ArrayProperties createArrayProperties(Color color,
      Color fillColor, Color elementColor, Color cellHighlight,
      Color elemHighlight, boolean direction, int depth, boolean hidden,
      boolean cascaded) {
    ArrayProperties prop = new ArrayProperties();
    setStandards(prop, color, fillColor, depth, hidden);
    if (elementColor != null)
      prop.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, elementColor);
    if (cellHighlight != null)
      prop.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, cellHighlight);
    if (elemHighlight != null)
      prop.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, elemHighlight);
    prop.set(AnimationPropertiesKeys.DIRECTION_PROPERTY, direction);
    prop.set(AnimationPropertiesKeys.CASCADED_PROPERTY, cascaded);
    return prop;
  }

  /**
   * creates a new instance of an ArrayProperties object by copying all fitting
   * properties from the AnimationProperties instance passed in
   * 
   * @param cloneFrom
   *          the basic AnimationProperties object to copy from
   * @return a new ArrayProperties instance with the selected properties
   */
  public static ArrayProperties createArrayProperties(
      AnimationProperties cloneFrom) {
    ArrayProperties prop = new ArrayProperties();
    copyIn(prop, cloneFrom);
    return prop;
  }

  /**
   * creates a new CircleProperties instance from the values passed in
   * 
   * @param color
   *          the chosen color for the circle boundary
   * @param fillColor
   *          the chosen fill color for the circle
   * @param depth
   *          the depth of the object (the higher, the further towards the
   *          background)
   * @param hidden
   *          whether the object is hidden (=true) or not
   * @return a new CircleProperties instance with the selected properties
   */
  public static CircleProperties createCircleProperties(Color color,
      Color fillColor, int depth, boolean hidden) {
    CircleProperties prop = new CircleProperties();
    setStandards(prop, color, fillColor, depth, hidden);
    return prop;
  }

  /**
   * creates a new instance of an CircleProperties object by copying all fitting
   * properties from the AnimationProperties instance passed in
   * 
   * @param cloneFrom
   *          the basic AnimationProperties object to copy from
   * @return a new CircleProperties instance with the selected properties
   */
  public static CircleProperties createCircleProperties(
      AnimationProperties cloneFrom) {
    CircleProperties prop = new CircleProperties();
    copyIn(prop, cloneFrom);
    return prop;
  }

  /**
   * creates a new CircleSegProperties instance from the values passed in
   * 
   * @param color
   *          the chosen color for the circle segment boundary
   * @param fillColor
   *          the chosen fill color for the circle segment, provided it is also
   *          closed
   * @param angle
   *          the total angle of the circle segment
   * @param startAngle
   *          the starting angle of the circle segment
   * @param clockwise
   *          if true, the angle is measured in clockwise direction, otherwise
   *          in counterclockwise direction
   * @param closed
   *          if true, the circle segment is closed (a "pie")
   * @return a new CircleSegProperties instance with the selected properties
   */
  public static CircleSegProperties createCircleSegProperties(Color color,
      Color fillColor, int angle, int startAngle, boolean clockwise,
      boolean closed) {
    return createCircleSegProperties(color, fillColor, angle, startAngle,
        clockwise, closed, DEFAULT_DEPTH, false, false, false);
  }

  /**
   * creates a new CircleSegProperties instance from the values passed in
   * 
   * @param color
   *          the chosen color for the circle segment boundary
   * @param fillColor
   *          the chosen fill color for the circle segment, provided it is also
   *          "closed"
   * @param angle
   *          the total angle of the circle segment
   * @param startAngle
   *          the starting angle of the circle segment
   * @param clockwise
   *          if true, the angle is measured in clockwise direction, otherwise
   *          in counterclockwise direction
   * @param closed
   *          if true, the circle segment is closed (a "pie")
   * @param depth
   *          the depth of the object (the higher, the further towards the
   *          background)
   * @param hidden
   *          whether the object is hidden (=true) or not
   * @param bwArrow
   *          if true, the circle segment has a backward-pointing arrow
   * @param fwArrow
   *          if true, the circle segment has a forward-pointing arrow
   * @return a new CircleSegProperties instance with the selected properties
   */
  public static CircleSegProperties createCircleSegProperties(Color color,
      Color fillColor, int angle, int startAngle, boolean clockwise,
      boolean closed, int depth, boolean hidden, boolean bwArrow,
      boolean fwArrow) {
    CircleSegProperties prop = new CircleSegProperties();
    setStandards(prop, color, fillColor, depth, hidden);
    prop.set(AnimationPropertiesKeys.ANGLE_PROPERTY, angle);
    prop.set(AnimationPropertiesKeys.STARTANGLE_PROPERTY, startAngle);
    prop.set(AnimationPropertiesKeys.CLOCKWISE_PROPERTY, clockwise);
    prop.set(AnimationPropertiesKeys.COUNTERCLOCKWISE_PROPERTY, !clockwise);
    prop.set(AnimationPropertiesKeys.CLOSED_PROPERTY, closed);
    prop.set(AnimationPropertiesKeys.BWARROW_PROPERTY, bwArrow);
    prop.set(AnimationPropertiesKeys.FWARROW_PROPERTY, fwArrow);
    return prop;
  }

  /**
   * creates a new instance of an CircleSegProperties object by copying all
   * fitting properties from the AnimationProperties instance passed in
   * 
   * @param cloneFrom
   *          the basic AnimationProperties object to copy from
   * @return a new CircleSegProperties instance with the selected properties
   */
  public static CircleSegProperties createCircleSegProperties(
      AnimationProperties cloneFrom) {
    CircleSegProperties prop = new CircleSegProperties();
    copyIn(prop, cloneFrom);
    return prop;
  }

  /**
   * creates a new EllipseProperties instance from the values passed in
   * 
   * @param color
   *          the chosen color for the ellipse boundary
   * @param fillColor
   *          the chosen fill color for the ellipse
   * @param depth
   *          the depth of the object (the higher, the further towards the
   *          background)
   * @param hidden
   *          whether the object is hidden (=true) or not
   * @return a new CircleProperties instance with the selected properties
   */
  public static EllipseProperties createEllipseProperties(Color color,
      Color fillColor, int depth, boolean hidden) {
    EllipseProperties prop = new EllipseProperties();
    setStandards(prop, color, fillColor, depth, hidden);
    return prop;
  }

  /**
   * creates a new instance of an EllipseProperties object by copying all
   * fitting properties from the AnimationProperties instance passed in
   * 
   * @param cloneFrom
   *          the basic AnimationProperties object to copy from
   * @return a new EllipseProperties instance with the selected properties
   */
  public static EllipseProperties createEllipseProperties(
      AnimationProperties cloneFrom) {
    EllipseProperties prop = new EllipseProperties();
    copyIn(prop, cloneFrom);
    return prop;
  }

  /**
   * creates a new GraphProperties instance from the values passed in
   * 
   * @param color
   *          the chosen color for the graph boundary
   * @param fillColor
   *          the chosen fill color for the graph
   * @param edgeColor
   *          the chosen color for the graph edges
   * @param elementColor
   *          the chosen color for the graph node values
   * @param nodeColor
   *          the chosen color for the graph nodes
   * @param elemHighlight
   *          the chosen color used when an element is highlighted
   * @param highlightColor
   *          the chosen color used when an entry is highlighted
   * @return a new GraphProperties instance with the selected properties
   */
  public static GraphProperties createGraphProperties(Color color,
      Color fillColor, Color edgeColor, Color elementColor, Color nodeColor,
      Color elemHighlight, Color highlightColor) {
    return createGraphProperties(color, fillColor, edgeColor, elementColor,
        nodeColor, elemHighlight, highlightColor, false, DEFAULT_DEPTH, false);
  }

  /**
   * creates a new GraphProperties instance from the values passed in
   * 
   * @param color
   *          the chosen color for the graph boundary
   * @param fillColor
   *          the chosen fill color for the graph
   * @param edgeColor
   *          the chosen color for the graph edges
   * @param elementColor
   *          the chosen color for the graph node values
   * @param nodeColor
   *          the chosen color for the graph nodes
   * @param elemHighlight
   *          the chosen color used when an element is highlighted
   * @param highlightColor
   *          the chosen color used when an entry is highlighted
   * @param directed
   *          determines whether the graph is directed (=true) or not
   * @param depth
   *          the depth of the object (the higher, the further towards the
   *          background)
   * @param hidden
   *          whether the object is hidden (=true) or not
   * @return a new GraphProperties instance with the selected properties
   */
  public static GraphProperties createGraphProperties(Color color,
      Color fillColor, Color edgeColor, Color elementColor, Color nodeColor,
      Color elemHighlight, Color highlightColor, boolean directed, int depth,
      boolean hidden) {
    GraphProperties prop = new GraphProperties();
    setStandards(prop, color, null, depth, hidden);
    if (fillColor != null)
      prop.set(AnimationPropertiesKeys.FILLED_PROPERTY, fillColor);
    if (edgeColor != null)
      prop.set(AnimationPropertiesKeys.EDGECOLOR_PROPERTY, edgeColor);
    if (elementColor != null)
      prop.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, elementColor);
    if (nodeColor != null)
      prop.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY, nodeColor);
    if (elemHighlight != null)
      prop.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, elemHighlight);
    if (highlightColor != null)
      prop.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, highlightColor);
    prop.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, directed);
    return prop;
  }

  /**
   * creates a new instance of an GraphProperties object by copying all fitting
   * properties from the AnimationProperties instance passed in
   * 
   * @param cloneFrom
   *          the basic AnimationProperties object to copy from
   * @return a new GraphProperties instance with the selected properties
   */
  public static GraphProperties createGraphProperties(
      AnimationProperties cloneFrom) {
    GraphProperties prop = new GraphProperties();
    copyIn(prop, cloneFrom);
    return prop;
  }

  /**
   * creates a new ListElementProperties instance from the values passed in
   * 
   * @param text
   *          the text value for the list element
   * @param color
   *          the chosen color for the list element boundary
   * @param textColor
   *          the chosen color for the list element value
   * @param boxFillColor
   *          the fill color for the list element value box
   * @param position
   *          the chosen position for the list pointers; use one of the
   *          AnimationPropertiesKeys.LIST_POSITION_* constants!
   * @return a new ListElementProperties instance with the selected properties
   */
  public static ListElementProperties createListElemProperties(String text,
      Color color, Color textColor, Color boxFillColor, int position) {
    return createListElemProperties(text, color, textColor, null, null,
        boxFillColor, position, DEFAULT_DEPTH, false);
  }

  /**
   * creates a new ListElementProperties instance from the values passed in
   * 
   * @param text
   *          the text value for the list element
   * @param color
   *          the chosen color for the list element boundary
   * @param textColor
   *          the chosen color for the list element value
   * @param pointerAreaColor
   *          the color for the outline of the pointer area
   * @param pointerAreaFillColor
   *          the fill color for the pointer area
   * @param boxFillColor
   *          the fill color for the list element value box
   * @param position
   *          the chosen position for the list pointers; use one of the
   *          AnimationPropertiesKeys.LIST_POSITION_* constants!
   * @param depth
   *          the depth of the object (the higher, the further towards the
   *          background)
   * @param hidden
   *          whether the object is hidden (=true) or not
   * @return a new ListElementProperties instance with the selected properties
   */
  public static ListElementProperties createListElemProperties(String text,
      Color color, Color textColor, Color pointerAreaColor,
      Color pointerAreaFillColor, Color boxFillColor, int position, int depth,
      boolean hidden) {
    ListElementProperties prop = new ListElementProperties();
    setStandards(prop, color, null, depth, hidden);
    if (text != null)
      prop.set(AnimationPropertiesKeys.TEXT_PROPERTY, text);
    if (textColor != null)
      prop.set(AnimationPropertiesKeys.TEXTCOLOR_PROPERTY, textColor);
    if (pointerAreaColor != null)
      prop.set(AnimationPropertiesKeys.POINTERAREACOLOR_PROPERTY,
          pointerAreaColor);
    if (pointerAreaFillColor != null)
      prop.set(AnimationPropertiesKeys.POINTERAREAFILLCOLOR_PROPERTY,
          pointerAreaFillColor);
    if (boxFillColor != null)
      prop.set(AnimationPropertiesKeys.BOXFILLCOLOR_PROPERTY, boxFillColor);
    prop.set(AnimationPropertiesKeys.POSITION_PROPERTY, position);
    return prop;
  }

  /**
   * creates a new instance of an ListElementProperties object by copying all
   * fitting properties from the AnimationProperties instance passed in
   * 
   * @param cloneFrom
   *          the basic AnimationProperties object to copy from
   * @return a new ListElementProperties instance with the selected properties
   */
  public static ListElementProperties createListElemProperties(
      AnimationProperties cloneFrom) {
    ListElementProperties prop = new ListElementProperties();
    copyIn(prop, cloneFrom);
    return prop;
  }

  /**
   * creates a new CircleProperties instance from the values passed in
   * 
   * @param color
   *          the chosen color for the array boundary
   * @param fillColor
   *          the chosen fill color for the array cells
   * @param elemHighlight
   *          the chosen color for highlighting a matrix element
   * @param cellHighlight
   *          the chosen (fill) color for highlighting a matrix cell
   * @return a new CircleProperties instance with the selected properties
   */
  public static MatrixProperties createMatrixProperties(Color color,
      Color fillColor, Color elemHighlight, Color cellHighlight) {
    return createMatrixProperties(color, fillColor, null, elemHighlight,
        cellHighlight, DEFAULT_DEPTH, false);
  }

  /**
   * creates a new CircleProperties instance from the values passed in
   * 
   * @param color
   *          the chosen color for the array boundary
   * @param fillColor
   *          the chosen fill color for the array cells
   * @param elemHighlight
   *          the chosen color for highlighting a matrix element
   * @param cellHighlight
   *          the chosen (fill) color for highlighting a matrix cell
   * @param depth
   *          the depth of the object (the higher, the further towards the
   *          background)
   * @param hidden
   *          whether the object is hidden (=true) or not
   * @return a new CircleProperties instance with the selected properties
   */
  public static MatrixProperties createMatrixProperties(Color color,
      Color fillColor, Color elementColor, Color elemHighlight,
      Color cellHighlight, int depth, boolean hidden) {
    MatrixProperties prop = new MatrixProperties();
    setStandards(prop, color, fillColor, depth, hidden);
    if (elementColor != null)
      prop.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, elementColor);
    if (elemHighlight != null)
      prop.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, elemHighlight);
    if (cellHighlight != null)
      prop.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, cellHighlight);
    return prop;
  }

  /**
   * creates a new instance of an MatrixProperties object by copying all fitting
   * properties from the AnimationProperties instance passed in
   * 
   * @param cloneFrom
   *          the basic AnimationProperties object to copy from
   * @return a new MatrixProperties instance with the selected properties
   */
  public static MatrixProperties createMatrixProperies(
      AnimationProperties cloneFrom) {
    MatrixProperties prop = new MatrixProperties();
    copyIn(prop, cloneFrom);
    return prop;
  }

  /**
   * creates a new PointProperties instance from the values passed in
   * 
   * @param color
   *          the chosen color for the point
   * @param depth
   *          the depth of the object (the higher, the further towards the
   *          background)
   * @param hidden
   *          whether the object is hidden (=true) or not
   * @return a new PointProperties instance with the selected properties
   */
  public static PointProperties createPointProperties(Color color, int depth,
      boolean hidden) {
    PointProperties prop = new PointProperties();
    setStandards(prop, color, null, depth, hidden);
    return prop;
  }

  /**
   * creates a new instance of an PointProperties object by copying all fitting
   * properties from the AnimationProperties instance passed in
   * 
   * @param cloneFrom
   *          the basic AnimationProperties object to copy from
   * @return a new PointProperties instance with the selected properties
   */
  public static PointProperties createPointProperties(
      AnimationProperties cloneFrom) {
    PointProperties prop = new PointProperties();
    copyIn(prop, cloneFrom);
    return prop;
  }

  /**
   * creates a new PolygonProperties instance from the values passed in
   * 
   * @param color
   *          the chosen color for the polygon
   * @param fillColor
   *          the chosen fill color for the polygon
   * @param depth
   *          the depth of the object (the higher, the further towards the
   *          background)
   * @param hidden
   *          whether the object is hidden (=true) or not
   * @return a new PolygonProperties instance with the selected properties
   */
  public static PolygonProperties createPolygonProperties(Color color,
      Color fillColor, int depth, boolean hidden) {
    PolygonProperties prop = new PolygonProperties();
    setStandards(prop, color, fillColor, depth, hidden);
    return prop;
  }

  /**
   * creates a new instance of an PolygonProperties object by copying all
   * fitting properties from the AnimationProperties instance passed in
   * 
   * @param cloneFrom
   *          the basic AnimationProperties object to copy from
   * @return a new PolygonProperties instance with the selected properties
   */
  public static PolygonProperties createPolygonProperties(
      AnimationProperties cloneFrom) {
    PolygonProperties prop = new PolygonProperties();
    copyIn(prop, cloneFrom);
    return prop;
  }

  /**
   * creates a new PolylineProperties instance from the values passed in
   * 
   * @param color
   *          the chosen color for the polyline
   * @return a new PolylineProperties instance with the selected properties
   */
  public static PolylineProperties createPolylineProperties(Color color) {
    return createPolylineProperties(color, false, false, DEFAULT_DEPTH, false);
  }

  /**
   * creates a new PolylineProperties instance from the values passed in
   * 
   * @param color
   *          the chosen color for the polyline
   * @param bwArrow
   *          determines if the polyline has a backwards-pointing arrow (=true)
   *          or not
   * @param fwArrow
   *          determines if the polyline has a forwards-pointing arrow (=true)
   *          or not
   * @param depth
   *          the depth of the object (the higher, the further towards the
   *          background)
   * @param hidden
   *          whether the object is hidden (=true) or not
   * @return a new PolylineProperties instance with the selected properties
   */
  public static PolylineProperties createPolylineProperties(Color color,
      boolean bwArrow, boolean fwArrow, int depth, boolean hidden) {
    PolylineProperties prop = new PolylineProperties();
    setStandards(prop, color, null, depth, hidden);
    prop.set(AnimationPropertiesKeys.BWARROW_PROPERTY, bwArrow);
    prop.set(AnimationPropertiesKeys.FWARROW_PROPERTY, fwArrow);
    return prop;
  }

  /**
   * creates a new instance of an PolylineProperties object by copying all
   * fitting properties from the AnimationProperties instance passed in
   * 
   * @param cloneFrom
   *          the basic AnimationProperties object to copy from
   * @return a new PolylineProperties instance with the selected properties
   */
  public static PolylineProperties createPolylineProperties(
      AnimationProperties cloneFrom) {
    PolylineProperties prop = new PolylineProperties();
    copyIn(prop, cloneFrom);
    return prop;
  }

  /**
   * creates a new RectProperties instance from the values passed in
   * 
   * @param color
   *          the chosen color for the shape
   * @param fillColor
   *          the chosen fill color for the shape
   * @param depth
   *          the depth of the object (the higher, the further towards the
   *          background)
   * @param hidden
   *          whether the object is hidden (=true) or not
   * @return a new RectProperties instance with the selected properties
   */
  public static RectProperties createRectProperties(Color color,
      Color fillColor, int depth, boolean hidden) {
    RectProperties prop = new RectProperties();
    setStandards(prop, color, fillColor, depth, hidden);
    return prop;
  }

  /**
   * creates a new instance of an RectProperties object by copying all fitting
   * properties from the AnimationProperties instance passed in
   * 
   * @param cloneFrom
   *          the basic AnimationProperties object to copy from
   * @return a new RectProperties instance with the selected properties
   */
  public static RectProperties createRectProperties(
      AnimationProperties cloneFrom) {
    RectProperties prop = new RectProperties();
    copyIn(prop, cloneFrom);
    return prop;
  }

  /**
   * creates a new SourceCodeProperties instance from the values passed in
   * 
   * @param color
   *          the chosen color for the shape
   * @param contextColor
   *          the chosen color for a "context" element, e.g., the "for" loop
   *          while executing a line of its body
   * @param highlightColor
   *          the chosen color for highlighting a code line
   * @param font
   *          the font used for the (complete) source code
   * @param indent
   *          the basic indentation for the (complete) source code,
   *          <strong>deprecated</strong>
   * @return a new SourceCodeProperties instance with the selected properties
   */
  public static SourceCodeProperties createSourceCodeProperties(Color color,
      Color contextColor, Color highlightColor, Font font, int indent) {
    return createSourceCodeProperties(color, contextColor, highlightColor,
        font, indent, 0, DEFAULT_DEPTH, false);
  }

  /**
   * creates a new SourceCodeProperties instance from the values passed in
   * 
   * @param color
   *          the chosen color for the shape
   * @param contextColor
   *          the chosen color for a "context" element, e.g., the "for" loop
   *          while executing a line of its body
   * @param highlightColor
   *          the chosen color for highlighting a code line
   * @param font
   *          the font used for the (complete) source code
   * @param indent
   *          the basic indentation for the (complete) source code,
   *          <strong>deprecated</strong>
   * @param row
   *          the basic row for the (complete) source code,
   *          <strong>deprecated</strong>
   * @param depth
   *          the depth of the object (the higher, the further towards the
   *          background)
   * @param hidden
   *          whether the object is hidden (=true) or not
   * @return a new SourceCodeProperties instance with the selected properties
   */
  public static SourceCodeProperties createSourceCodeProperties(Color color,
      Color contextColor, Color highlightColor, Font font, int indent, int row,
      int depth, boolean hidden) {
    SourceCodeProperties prop = new SourceCodeProperties();
    setStandards(prop, color, null, depth, hidden);
    if (contextColor != null)
      prop.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, contextColor);
    if (highlightColor != null)
      prop.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, highlightColor);
    if (font != null)
      prop.set(AnimationPropertiesKeys.FONT_PROPERTY, font);
    prop.set(AnimationPropertiesKeys.INDENTATION_PROPERTY, indent);
    prop.set(AnimationPropertiesKeys.ROW_PROPERTY, row);
    return prop;
  }

  /**
   * creates a new instance of an SourceCodeProperties object by copying all
   * fitting properties from the AnimationProperties instance passed in
   * 
   * @param cloneFrom
   *          the basic AnimationProperties object to copy from
   * @return a new SourceCodeProperties instance with the selected properties
   */
  public static SourceCodeProperties createSourceCodeProperties(
      AnimationProperties cloneFrom) {
    SourceCodeProperties prop = new SourceCodeProperties();
    copyIn(prop, cloneFrom);
    return prop;
  }

  /**
   * creates a new SquareProperties instance from the values passed in
   * 
   * @param color
   *          the chosen color for the shape
   * @param fillColor
   *          the chosen fill color for the shape
   * @param depth
   *          the depth of the object (the higher, the further towards the
   *          background)
   * @param hidden
   *          whether the object is hidden (=true) or not
   * @return a new SquareProperties instance with the selected properties
   */
  public static SquareProperties createSquareProperties(Color color, Color fillColor,
      int depth, boolean hidden) {
    SquareProperties prop = new SquareProperties();
    setStandards(prop, color, fillColor, depth, hidden);
    return prop;
  }

  /**
   * creates a new instance of an SquareProperties object by copying all fitting
   * properties from the AnimationProperties instance passed in
   * 
   * @param cloneFrom
   *          the basic AnimationProperties object to copy from
   * @return a new SquareProperties instance with the selected properties
   */
  public static SquareProperties createSquareProperties(
      AnimationProperties cloneFrom) {
    SquareProperties prop = new SquareProperties();
    copyIn(prop, cloneFrom);
    return prop;
  }

  /**
   * creates a new TextProperties instance from the values passed in
   * 
   * @param color
   *          the chosen color for the shape
   * @return a new TextProperties instance with the selected properties
   */
  public static TextProperties createTextProperties(Color color) {
    return createTextProperties(color, PropertiesBuilder.DEFAULT_FONT, false,
        1, false);
  }

  /**
   * creates a new TextProperties instance from the values passed in
   * 
   * @param color
   *          the chosen color for the shape
   * @param font
   *          the font for the text component
   * @return a new TextProperties instance with the selected properties
   */
  public static TextProperties createTextProperties(Color color, Font font) {
    return createTextProperties(color, font, false, 1, false);
  }

  /**
   * creates a new TextProperties instance from the values passed in
   * 
   * @param color
   *          the chosen color for the shape
   * @param font
   *          the font for the text component
   * @param centered
   *          if true, the text is centered, else left justified
   * @param depth
   *          the depth of the object (the higher, the further towards the
   *          background)
   * @param isHidden
   *          whether the object is hidden (=true) or not
   * @return a new TextProperties instance with the selected properties
   */
  public static TextProperties createTextProperties(Color color, Font font,
      boolean centered, int depth, boolean isHidden) {
    TextProperties prop = new TextProperties();
    setStandards(prop, color, null, depth, isHidden);
    prop.set(AnimationPropertiesKeys.FONT_PROPERTY, font);
    prop.set(AnimationPropertiesKeys.CENTERED_PROPERTY, centered);
    return prop;
  }

  /**
   * creates a new instance of an TextProperties object by copying all fitting
   * properties from the AnimationProperties instance passed in
   * 
   * @param cloneFrom
   *          the basic AnimationProperties object to copy from
   * @return a new TextProperties instance with the selected properties
   */
  public static TextProperties createTextProperties(
      AnimationProperties cloneFrom) {
    TextProperties prop = new TextProperties();
    copyIn(prop, cloneFrom);
    return prop;
  }

  /**
   * creates a new TriangleProperties instance from the values passed in
   * 
   * @param color
   *          the chosen color for the shape
   * @param fillColor
   *          the chosen fill color for the shape
   * @param depth
   *          the depth of the object (the higher, the further towards the
   *          background)
   * @param hidden
   *          whether the object is hidden (=true) or not
   * @return a new TriangleProperties instance with the selected properties
   */
  public static TriangleProperties createTriangleProperties(Color color,
      Color fillColor, int depth, boolean hidden) {
    TriangleProperties prop = new TriangleProperties();
    setStandards(prop, color, fillColor, depth, hidden);
    return prop;
  }

  /**
   * creates a new instance of an TriangleProperties object by copying all
   * fitting properties from the AnimationProperties instance passed in
   * 
   * @param cloneFrom
   *          the basic AnimationProperties object to copy from
   * @return a new TriangleProperties instance with the selected properties
   */
  public static TriangleProperties createTriangleProperties(
      AnimationProperties cloneFrom) {
    TriangleProperties prop = new TriangleProperties();
    copyIn(prop, cloneFrom);
    return prop;
  }

  private static void copyIn(AnimationProperties destination,
      AnimationProperties cloneFrom) {
    Set<String> cloneProps = cloneFrom.getAllPropertyNames();
    for (String key : destination.getAllPropertyNames()) {
      if (cloneProps.contains(key))
        destination.set(key, cloneFrom.get(key));
    }
  }

  private static void setStandards(AnimationProperties destination,
      Color color, Color fill, int depth, boolean hidden) {
    if (color != null)
      destination.set(AnimationPropertiesKeys.COLOR_PROPERTY, color);
    if (fill != null) {
      destination.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
      destination.set(AnimationPropertiesKeys.FILL_PROPERTY, fill);
    }
    destination.set(AnimationPropertiesKeys.DEPTH_PROPERTY, depth);
    destination.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, hidden);
  }

}
