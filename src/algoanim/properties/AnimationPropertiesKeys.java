package algoanim.properties;

/**
 * 
 * This class defines some constants to make it possible to easily access the
 * properties by names. Please always use these to access
 * <code>PropertyItem</code>s within an <code>AnimationPropertie</code>s object.
 * 
 * @author Stephan Mehlhase, Dima Vronskyi
 */
public interface AnimationPropertiesKeys {
  public static final String FOOBAR                                         = "foobar";
  public static final String METHOD_NAME                                    = "methodName";
  public static final String NAME                                           = "name";
  public static final String The_Answer_to_Life_the_Universe_and_Everything = "42";

  // Graphical Properties
  public static final String BORDER_PROPERTY                                = "border";
  public static final String BWARROW_PROPERTY                               = "bwArrow";
  public static final String COLOR_PROPERTY                                 = "color";
  public static final String DEPTH_PROPERTY                                 = "depth";
  public static final String FILL_PROPERTY                                  = "fillColor";
  public static final String FILLED_PROPERTY                                = "filled";
  public static final String FWARROW_PROPERTY                               = "fwArrow";
  
  // Text properties
  public static final String CENTERED_PROPERTY                              = "centered";
  public static final String RIGHT_PROPERTY                                 = "right";
  public static final String FONT_PROPERTY                                  = "font";
  
  // Arc Primitives
  public static final String ANGLE_PROPERTY                                 = "angle";
  public static final String STARTANGLE_PROPERTY                            = "startAngle";
  public static final String CLOCKWISE_PROPERTY                             = "clockwise";
  public static final String CLOSED_PROPERTY                                = "closed";
  public static final String COUNTERCLOCKWISE_PROPERTY                      = "counterclockwise";
  
  // Array Primitives
  public static final String CASCADED_PROPERTY                              = "cascaded";
  public static final String CELLHIGHLIGHT_PROPERTY                         = "cellHighlight";
  public static final String DIRECTION_PROPERTY                             = "vertical";
  public static final String ELEMENTCOLOR_PROPERTY                          = "elementColor";
  public static final String ELEMHIGHLIGHT_PROPERTY                         = "elemHighlight";
  public static final String LABEL_PROPERTY                                 = "label";

  // Array Marker Primitives
  public static final String LONG_MARKER_PROPERTY                           = "long";
  public static final String SHORT_MARKER_PROPERTY                          = "short";

  // Graph Primitives
  public static final String DIRECTED_PROPERTY                              = "directed";
  // public static final String EDGECOLOR_PROPERTY = "edgeColor";
  public static final String NODECOLOR_PROPERTY                             = "nodeColor";
  public static final String WEIGHTED_PROPERTY                              = "weighted";

  // List Element Primitives
  public static final String BOXFILLCOLOR_PROPERTY                          = "boxFillColor";
  public static final String POINTERAREACOLOR_PROPERTY                      = "pointerAreaColor";
  public static final String POINTERAREAFILLCOLOR_PROPERTY                  = "pointerAreaFillColor";
  public static final String POSITION_PROPERTY                              = "position";
  public static final String PREV_PROPERTY                                  = "prev";
  public static final String TEXT_PROPERTY                                  = "text";
  public static final String TEXTCOLOR_PROPERTY                             = "textColor";
  public static final int    LIST_POSITION_BOTTOM                           = 4;
  public static final int    LIST_POSITION_LEFT                             = 2;
  public static final int    LIST_POSITION_NONE                             = 0;
  public static final int    LIST_POSITION_RIGHT                            = 1;
  public static final int    LIST_POSITION_TOP                              = 3;

  // SourceCode Primitives
  public static final String CONTEXTCOLOR_PROPERTY                          = "contextColor";
  public static final String HIGHLIGHTCOLOR_PROPERTY                        = "highlightColor";
  public static final String INDENTATION_PROPERTY                           = "indentation";
  public static final String ROW_PROPERTY                                   = "row";

  // below --> GR
  public static final String BOLD_PROPERTY                                  = "bold";
  public static final String HIDDEN_PROPERTY                                = "hidden";
  public static final String ITALIC_PROPERTY                                = "italic";
  public static final String SIZE_PROPERTY                                  = "size";

  // Stack & Queue Properties
  public static final String ALTERNATE_FILL_PROPERTY                        = "alternateFillColor";
  public static final String ALTERNATE_FILLED_PROPERTY                      = "alternateFilled";

  // Stack Properties
  public static final String DIVIDINGLINE_COLOR_PROPERTY                    = "dividingLineColor";

  // Grid properties
  public static final String GRID_ALIGN_PROPERTY                            = "align";
  public static final String GRID_BORDER_COLOR_PROPERTY                     = "borderColor";
  public static final String GRID_HIGHLIGHT_BORDER_COLOR_PROPERTY           = "highlightBorderColor";
  public static final String GRID_STYLE_PROPERTY                            = "style";
  public static final String CELL_WIDTH_PROPERTY                            = "cellwidth";
  public static final String CELL_HEIGHT_PROPERTY                           = "cellheight";

  // Improved Graph Support
  public static final String EDGECOLOR_PROPERTY                             = "edgeColor";
  public static final String EDGETEXTCOLOR_PROPERTY                         = "edgeTextColor";
  public static final String MARKEDGECOLOR_PROPERTY                         = "markEdgeColor";
  public static final String MARKEDGETEXTCOLOR_PROPERTY                     = "markEdgeTextColor";
  public static final String MARKNODECOLOR_PROPERTY                         = "markNodeColor";
  public static final String MARKNODETEXTCOLOR_PROPERTY                     = "markNodeTextColor";
  public static final String NODEFILLCOLOR_PROPERTY                         = "nodeFillColor";
  public static final String NODETEXTCOLOR_PROPERTY                         = "nodeTextColor";

}
