/*
 * PTGraph.java
 * The class for a PTGraph.
 * 
 * Created on 11. June 2006
 *
 * @author Pierre Villette
 */

package animal.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.SwingUtilities;

import animal.graphics.meta.TextContainer;
import animal.main.Animal;
import animal.main.AnimalConfiguration;
import animal.misc.MSMath;
import animal.misc.XProperties;

public class PTGraph extends PTGraphicObject implements TextContainer {

  // =========
  // CONSTANTS
  // =========

  // public static XProperties DefaultProperties;

  public static final String TYPE_LABEL = "Graph";

  // public static final String GRAPH_LOCATION = TYPE_LABEL + ".location";
  //
  // public static final String BG_COLOR = TYPE_LABEL + ".bgColor";
  //
  // public static final String NODE_FONT_COLOR = TYPE_LABEL + ".nodeFontColor";
  //
  // public static final String NODE_FONT = TYPE_LABEL + ".nodeFont";
  //
  // public static final String EDGE_FONT_COLOR = TYPE_LABEL + ".edgeFontColor";
  //
  // public static final String EDGE_FONT = TYPE_LABEL + ".edgeFont";
  //
  // public static final String HIGHLIGHT_COLOR = TYPE_LABEL +
  // ".highlightColor";
  //
  // public static final String ELEM_HIGHLIGHT_COLOR = TYPE_LABEL
  // + ".elemHighlightColor";
  //
  // public static final String OUTLINE_COLOR = TYPE_LABEL + ".outlineColor";
  //
  // public static final String GRAPH_SIZE = TYPE_LABEL + ".graphSize";
  //
  // public static final String NODE_FONT_NAME = TYPE_LABEL + ".nodeFontName";
  //
  // public static final String NODE_FONT_SIZE = TYPE_LABEL + ".nodeFontSize";
  // public static final String NODE_STYLE = TYPE_LABEL + ".nodeStyle";
  // public static final String EDGE_STYLE = TYPE_LABEL + ".edgeStyle";
  //
  // public static final String EDGE_FONT_NAME = TYPE_LABEL + ".edgeFontName";
  //
  // public static final String EDGE_FONT_SIZE = TYPE_LABEL + ".edgeFontSize";
  //
  // public static final String DIRECTION = TYPE_LABEL + ".direction";
  //
  // public static final String WEIGHT = TYPE_LABEL + ".weight";
  //
  // public static final String SHOW_INDICES = TYPE_LABEL + ".showIndices";
  //
  public static final String UNDEFINED_EDGE = "^";

  public static final String UNDEFINED_EDGE_WEIGHT = "%";

  /**
   * The nodes nodeText: contains the entries of the nodes nodes: contains the
   * arcs
   */
  protected PTText[] nodeText;

  // protected PTArc[] nodes;
  protected PTCircle[] nodes;
  protected Integer[] nodesRadius;

  /**
   * The edges edgeText: contains the entries of the edges edges: contains the
   * lines of the edges between two nodes edgeLoop: contains the lines of the
   * edges that concern only one node
   */
  protected PTText[][] edgeText;

  protected PTPolyline[][] edges;

  protected PTOpenCircleSegment[] edgeLoop;

  /**
   * Selects if the graph is directed and if edges have a weight
   */
  protected boolean directed;

  protected boolean weighted;

  /**
   * Selects if the indices of the nodes are displayed
   */
  protected boolean showIndices;

  /**
   * The origin of the graph is the upper left corner.
   */
  protected Point origin;

  /**
   * The amount of nodes
   */
  public int length;

  /**
   * The fonts
   */
  protected Font nodeFont;

  protected Font edgeFont;

  protected FontMetrics nodeFm;

  protected FontMetrics edgeFm;

  //TODO Color Changer
  protected Color backgroundColor;
  protected Color edgeFontColor;
  protected Color[][] edgeHighlightFontColorArray;
  protected Color[][] edgeHighlightPolyColorArray;
  protected Color elemHighlightColor;
  protected Color[] elemHighlightColorArray;
  protected Color highlightColor;
  protected Color[] highlightColorArray;
  protected Color outlineColor;

  protected final byte DEACTIVATED = (byte) 0;

  protected final byte ACTIVATED = (byte) 1;

  protected final byte HIGHLIGHTED = (byte) 2;

  protected final byte ELEM_HIGHLIGHTED = (byte) 4;

  protected final byte VISIBLE = (byte) 8;

  /**
   * Store the current states of the cells This is a combination of
   * <code>ACTIVATED/DEACTIVATED,
   * HIGHLIGHTED</code> and
   * <code>ELEM_HIGHLIGHTED</code> set as bit flags. Default is
   * <code>ACTIVATED</code> only.
   * 
   * If set <code>DEACTIVATED</code> the cell is greyed out and all other
   * settings are ignored.
   */
  protected byte[] nodeStates;

  protected byte[][] edgeStates;

  protected byte[][] edgeWeightStates;

  // ============
  // CONSTRUCTORS
  // ============

  /**
   * Default constructor
   */
  public PTGraph() {
    this(AnimalConfiguration.getDefaultConfiguration().getDefaultIntValue(
        PTGraph.TYPE_LABEL, "graphSize", 10));
    // if (DefaultProperties == null)
    // initializeDefaultProperties(AnimalConfiguration.getDefaultConfiguration()
    // .getProperties());
    // setProperties((XProperties) DefaultProperties.clone());
    // length = getProperties().getIntProperty(mapKey(GRAPH_SIZE), 1);
    // nodeFont = getProperties().getFontProperty(
    // mapKey(NODE_FONT),
    // new Font(getProperties().getProperty(mapKey(NODE_FONT_NAME),
    // "Monospaced"), Font.PLAIN, getProperties().getIntProperty(
    // mapKey(NODE_FONT_SIZE), 14)));
    // nodeFm = Animal.getConcreteFontMetrics(nodeFont);
    // edgeFont = getProperties().getFontProperty(
    // mapKey(EDGE_FONT),
    // new Font(getProperties().getProperty(mapKey(EDGE_FONT_NAME),
    // "Monospaced"), Font.PLAIN, getProperties().getIntProperty(
    // mapKey(EDGE_FONT_SIZE), 14)));
    // edgeFm = Animal.getConcreteFontMetrics(edgeFont);
    // origin = new Point();
    // init();
  }

  /**
   * Constructs a PTGraph of the specified length. This is the one used by the
   * <code>GraphEditor</code>.
   * 
   * @param size
   *          the number of entries
   */
  public PTGraph(int size) {
    // if (DefaultProperties == null)
    // initializeDefaultProperties(AnimalConfiguration.getDefaultConfiguration()
    // .getProperties());
    // setProperties((XProperties) DefaultProperties.clone());
    // length = size;
    // nodeFont = getProperties().getFontProperty(
    // mapKey(NODE_FONT),
    // new Font(getProperties().getProperty(mapKey(NODE_FONT_NAME),
    // "Monospaced"), Font.PLAIN, getProperties().getIntProperty(
    // mapKey(NODE_FONT_SIZE), 14)));
    // nodeFm = Animal.getConcreteFontMetrics(nodeFont);
    // edgeFont = getProperties().getFontProperty(
    // mapKey(EDGE_FONT),
    // new Font(getProperties().getProperty(mapKey(EDGE_FONT_NAME),
    // "Monospaced"), Font.PLAIN, getProperties().getIntProperty(
    // mapKey(EDGE_FONT_SIZE), 14)));
    // edgeFm = Animal.getConcreteFontMetrics(edgeFont);
    // origin = new Point();
    // initialize with default attributes
    initializeWithDefaults(getType());
    length = size;
    highlightColorArray = new Color[length];
    for (int i = 0; i < highlightColorArray.length; i++) {
    	highlightColorArray[i] = highlightColor;
	}
    elemHighlightColorArray = new Color[length];
    for (int i = 0; i < elemHighlightColorArray.length; i++) {
    	elemHighlightColorArray[i] = elemHighlightColor;
	}
    edgeHighlightFontColorArray = new Color[length][length];
    for (int i = 0; i < edgeHighlightFontColorArray.length; i++) {
        for (int j = 0; j < edgeHighlightFontColorArray.length; j++) {
        	edgeHighlightFontColorArray[i][j] = elemHighlightColor;
        }
	}
    edgeHighlightPolyColorArray = new Color[length][length];
    for (int i = 0; i < edgeHighlightPolyColorArray.length; i++) {
        for (int j = 0; j < edgeHighlightPolyColorArray.length; j++) {
        	edgeHighlightPolyColorArray[i][j] = highlightColor;
        }
	}
    nodesRadius = new Integer[length];
    for (int i = 0; i < nodesRadius.length; i++) {
    	nodesRadius[i] = null;
	}
    init();
  }

  // /**
  // * Constructs a PTGraph depending on the settings in the properties file
  // *
  // * @param props
  // * the XProperties to be used
  // */
  // public PTGraph(XProperties props) {
  // setProperties(props);
  // length = getProperties().getIntProperty(mapKey(GRAPH_SIZE), 1);
  // nodeFont = getProperties().getFontProperty(
  // mapKey(NODE_FONT),
  // new Font(getProperties().getProperty(mapKey(NODE_FONT_NAME),
  // "Monospaced"), Font.PLAIN, getProperties().getIntProperty(
  // mapKey(NODE_FONT_SIZE), 14)));
  // nodeFm = Animal.getConcreteFontMetrics(nodeFont);
  // edgeFont = getProperties().getFontProperty(
  // mapKey(EDGE_FONT),
  // new Font(getProperties().getProperty(mapKey(EDGE_FONT_NAME),
  // "Monospaced"), Font.PLAIN, getProperties().getIntProperty(
  // mapKey(EDGE_FONT_SIZE), 14)));
  // edgeFm = Animal.getConcreteFontMetrics(edgeFont);
  // origin = new Point();
  // init();
  // }

  // ===============
  // PROPERTY ACCESS
  // ===============

  public void initializeWithDefaults(String primitiveName) {
    AnimalConfiguration config = AnimalConfiguration.getDefaultConfiguration();
    backgroundColor = config.getDefaultColor(primitiveName, "bgColor",
        Color.WHITE);
    directed = config.getDefaultBooleanValue(primitiveName, "directed", false);
    edgeFont = config.getDefaultFontValue(primitiveName, "edgeFont", new Font(
        "SansSerif", Font.PLAIN, 12));
    edgeFm = Animal.getConcreteFontMetrics(edgeFont);
    nodeFont = config.getDefaultFontValue(primitiveName, "nodeFont", new Font(
        "SansSerif", Font.PLAIN, 12));
    nodeFm = Animal.getConcreteFontMetrics(nodeFont);
    length = config.getDefaultIntValue(primitiveName, "graphSize", 10);
    edgeFontColor = config.getDefaultColor(primitiveName, "edgeFontColor",
        Color.BLACK);
    elemHighlightColor = config.getDefaultColor(primitiveName,
        "elemHighlightColor", Color.RED);
    highlightColor = config.getDefaultColor(primitiveName, "highkightColor",
        Color.BLUE);
    outlineColor = config.getDefaultColor(primitiveName, "outlineColor",
        Color.BLACK);
    showIndices = config.getDefaultBooleanValue(primitiveName, "showIndices",
        false);
    weighted = config.getDefaultBooleanValue(primitiveName, "weighted", true);
    origin = new Point(10, 10);
  }

  /**
   * Defines objects of this class as Graph
   */
  public String[] handledKeywords() {
    return new String[] { "Graph" };
  }

  // /**
  // * Set the default properties of the newly created Graph
  // *
  // * @param prop
  // * the properties file being used
  // */
  // public static void initializeDefaultProperties(XProperties prop) {
  // DefaultProperties = extractDefaultProperties(prop, "Graph");
  // }

  /**
   * Returns the PTText corresponding to the ith node
   */
  public PTText getPTTextNode(int i) {
    return nodeText[i];
  }

  /**
   * Returns the PTText corresponding to an edge
   */
  public PTText getPTTextEdge(int i, int j) {
    return edgeText[i][j];
  }

  /**
   * Return the Arc of the ith node
   */
  public PTCircle getNode(int i) {
    return nodes[i];
  }

  /**
   * Returns the arc of an edge
   */
  public PTPolyline getEdge(int i, int j) {
    return edges[i][j];
  }

  /**
   * Returns the arc of an one-node edge
   */
  public PTOpenCircleSegment getEdgeLoop(int i) {
    return edgeLoop[i];
  }

  /**
   * Store the given String in the specified node
   * 
   * @param index
   *          the number of the nodes cell
   * @param val
   *          the value that's to be stored
   */
  public void enterValueNode(int index, String val) {
    if ((index >= 0) && (index < length)) {
      nodeText[index].setText(val);
      resizeNode(index);
    }
  }
  public void enterNodeRadius(int index, Integer radius) {
	  /*if(radius >5){
		  radius =5;
	  }*/
	    if ((index >= 0) && (index < length)) {
	    	nodesRadius[index] = radius;
	    	resizeNode(index);
	    }
  }

  /**
   * Retrieve the content of a node as a String
   * 
   * @param index
   *          the cell which content is requested.
   */
  public String getValueNode(int index) {
    if ((index >= 0) && (index < length)) {
      return nodeText[index].getText();
    }
    return null;
  }

  /**
   * Store the given String in the specified edge
   * 
   * @param index1
   *          the start node of the edge
   * @param index2
   *          the targetnode of the edge
   * @param val
   *          the value that's to be stored
   */
  public void enterValueEdge(int index1, int index2, String val) {
    if ((index1 >= 0) && index2 >= 0 && (index1 < length) && (index2 < length)) {
      edgeText[index1][index2].setText(val);
      setEdge(index1, index2);
      // TODO check if this is correct!
      setEdge(index2, index1);
    }
  }

  /**
   * Retrieve the content of an edge as a String
   * 
   * @param index1
   *          the start node of the edge
   * @param index2
   *          the end node of the edge
   */
  public String getValueEdge(int index1, int index2) {
    if ((index1 >= 0) && (index1 < length) && (index2 >= 0)
        && (index2 < length)) {
      return edgeText[index1][index2].getText();
    }
    return null;
  }

  /**
   * Set the depth of the PTGraph.
   * 
   * This value must be transmitted to the nodes and edges
   */
  public void setDepth(int newDepth) {
    int theDepth = (newDepth < 2) ? 2 : newDepth;
    theDepth = (theDepth != Integer.MAX_VALUE) ? theDepth
        : Integer.MAX_VALUE - 2;
    super.setDepth(theDepth);
    for (int i = 0; i < length; i++) {
      nodeText[i].setDepth(theDepth - 2);
      nodes[i].setDepth(theDepth);
      edgeLoop[i].setDepth(theDepth);
    }
    for (int j = 0; j < length; j++) {
      for (int k = 0; k < length; k++) {
        edgeText[j][k].setDepth(theDepth - 2);
        edges[j][k].setDepth(theDepth);
      }
    }
  }

  /**
   * Activates or deactivates a graph node.
   * 
   * @param i
   *          the node whose state shall be changed
   * @param newState
   *          the state of the node true if node is activated, false if it shall
   *          be greyed out
   */
  public void setActivatedNode(int i, boolean newState) {
    if ((0 <= i) && (i < length)) {
      if (newState) {
        nodeStates[i] = (byte) (nodeStates[i] | ACTIVATED);
        nodes[i].setFillColor(isHighlightedNode(i) ? getHighlightColor(i)
            : getBGColor());
        nodeText[i].setColor(isElemHighlightedNode(i) ? getElemHighlightColor(i)
            : getNodeFontColor());
      } else {
        nodeStates[i] = (byte) (nodeStates[i] & ~ACTIVATED);
        nodes[i].setFillColor(Color.GRAY);
        nodeText[i].setColor(Color.DARK_GRAY);
      }
    }
  }

  /**
   * Activates or deactivates a graph edge.
   * 
   * @param i
   *          the start node of the edge
   * @param j
   *          the end node of the edge
   * @param newState
   *          the state of the edge true if edge is activated, false if it shall
   *          be greyed out
   */
  public void setActivatedEdge(int i, int j, boolean newState) {
    if ((0 <= i) && (i < length) && (0 <= j) && (j < length)) {
      if (newState) {
        edgeStates[i][j] = (byte) (edgeStates[i][j] | ACTIVATED);
        edgeWeightStates[i][j] = (byte) (edgeWeightStates[i][j] | ACTIVATED);
        edgeText[i][j]
            .setColor(getEdgeTextColor(i, j));
        if (i != j) {
          edges[i][j].setColor(getEdgePolyColor(i, j));
        } else {
          edgeLoop[i].setColor(getEdgePolyColor(i, j));
        }
      } else {
        edgeStates[i][j] = (byte) (edgeStates[i][j] & ~ACTIVATED);
        edgeWeightStates[i][j] = (byte) (edgeWeightStates[i][j] & ~ACTIVATED);
        edgeText[i][j].setColor(Color.DARK_GRAY);
        if (i != j) {
          edges[i][j].setColor(Color.GRAY);
        } else {
          edgeLoop[i].setColor(Color.GRAY);
        }
      }
    }
  }

  /**
   * Is the current node activated or deactivated?
   * 
   * @param i
   *          the index of the node
   */
  public boolean isActivatedNode(int i) {
    if ((0 <= i) && (i < length)) {
      return ((nodeStates[i] & ACTIVATED) == ACTIVATED);
    }
    return false;
  }

  /**
   * Is the current edge activated or deactivated?
   * 
   * @param i
   *          the index of the edge
   */
  public boolean isActivatedEdge(int i, int j) {
    if ((0 <= i) && (i < length) && (0 <= j) && (j < length)) {
      return ((edgeStates[i][j] & ACTIVATED) == ACTIVATED);
    }
    return false;
  }

  /**
   * Changes the highlight state of a node
   * 
   * @param i
   *          the node whose state shall be changed
   * @param highlight
   *          the state of the node true if node shall be highlighted, false if
   *          not
   */
  public void setHighlightedNode(int i, boolean highlight) {
    if ((0 <= i) && (i < length)) {
      nodeStates[i] = (highlight ? (byte) (nodeStates[i] | HIGHLIGHTED)
          : (byte) (nodeStates[i] & ~HIGHLIGHTED));
      if (isActivatedNode(i)) {
        nodes[i].setFillColor(isHighlightedNode(i) ? getHighlightColor(i)
            : getBGColor());
      }
    }
  }

  /**
   * Changes the highlight state of an edge
   * 
   * @param i
   *          the start node of the edge whose state shall be changed
   * @param j
   *          the end node of the edge whose state shall be changed
   * @param highlight
   *          the state of the edge true if edge shall be highlighted, false if
   *          not
   */
  public void setHighlightedEdge(int i, int j, boolean highlight) {
    if ((0 <= i) && (i < length) && (0 <= j) && (j < length)) {
      edgeStates[i][j] = (highlight ? (byte) (edgeStates[i][j] | HIGHLIGHTED)
          : (byte) (edgeStates[i][j] & ~HIGHLIGHTED));
      if (isActivatedEdge(i, j)) {
        if (i != j) {
          edges[i][j].setColor(getEdgePolyColor(i, j));
        } else {
          edgeLoop[i].setColor(getEdgePolyColor(i, j));
        }
      }
    }
  }

  /**
   * Is the current node highlighted?
   * 
   * @param i
   *          the index of the node
   */
  public boolean isHighlightedNode(int i) {
    if ((0 <= i) && (i < length)) {
      return ((nodeStates[i] & HIGHLIGHTED) == HIGHLIGHTED);
    }
    return false;
  }

  /**
   * Is the current edge highlighted?
   * 
   * @param i
   *          the start node of the edge
   * @param j
   *          the end node of the edge
   */
  public boolean isHighlightedEdge(int i, int j) {
    if ((0 <= i) && (i < length) && (0 <= j) && (j < length)) {
      return ((edgeStates[i][j] & HIGHLIGHTED) == HIGHLIGHTED);
    }
    return false;
  }

  /**
   * Sets a node as visible
   */
  public void setVisibleNode(int i, boolean visible) {
    if ((0 <= i) && (i < length)) {
      nodeStates[i] = (visible ? (byte) (nodeStates[i] | VISIBLE)
          : (byte) (nodeStates[i] & ~VISIBLE));
    }
  }

  /**
   * Sets an edge as visible
   */
  public void setVisibleEdge(int i, int j, boolean visible) {
    if ((0 <= i) && (i < length) && (0 <= j) && (j < length)) {
      edgeStates[i][j] = (visible ? (byte) (edgeStates[i][j] | VISIBLE)
          : (byte) (edgeStates[i][j] & ~VISIBLE));
    }
  }

  /**
   * Sets an edge weightas visible
   */
  public void setEdgeWeightVisibility(int i, int j, boolean visible) {
    if ((0 <= i) && (i < length) && (0 <= j) && (j < length)) {
      edgeWeightStates[i][j] = (visible ? (byte) (edgeWeightStates[i][j] | VISIBLE)
          : (byte) (edgeWeightStates[i][j] & ~VISIBLE));
    }
  }

  /**
   * Is the current node visible?
   */
  public boolean isVisibleNode(int i) {
    if ((0 <= i) && (i < length)) {
      return ((nodeStates[i] & VISIBLE) == VISIBLE);
    }
    return false;
  }

  /**
   * Is the current edge visible?
   * 
   * @param i
   *          the start node of the egge
   * @param j
   *          the end node of of the edge
   */
  public boolean isVisibleEdge(int i, int j) {
    if ((0 <= i) && (i < length) && (0 <= j) && (j < length)) {
      return ((edgeStates[i][j] & VISIBLE) == VISIBLE);
    }
    return false;
  }

  /**
   * Changes the highlight state of a node entry.
   * 
   * @param i
   *          the node whose state shall be changed
   * @param highlight
   *          the state of the entry true if the entry shall be highlighted,
   *          false if not
   */
  public void setElemHighlightedNode(int i, boolean highlight) {
    if ((0 <= i) && (i < length)) {
      nodeStates[i] = (highlight ? (byte) (nodeStates[i] | ELEM_HIGHLIGHTED)
          : (byte) (nodeStates[i] & ~ELEM_HIGHLIGHTED));
      if (isActivatedNode(i)) {
        nodeText[i].setColor(highlight ? getElemHighlightColor(i)
            : getNodeFontColor());
      }
    }
  }

  /**
   * Changes the highlight state of an edge entry.
   * 
   * @param i
   *          the start node of the edge whose state shall be changed
   * @param j
   *          the end node of the edge whose state shall be changed
   * @param highlight
   *          the state of the entry: true if the entry shall be highlighted,
   *          false if not
   */
  public void setElemHighlightedEdge(int i, int j, boolean highlight) {
    if ((0 <= i) && (i < length) && (0 <= j) && (j < length)) {
      edgeStates[i][j] = (highlight ? (byte) (edgeStates[i][j] | ELEM_HIGHLIGHTED)
          : (byte) (edgeStates[i][j] & ~ELEM_HIGHLIGHTED));
      if (isActivatedEdge(i, j)) {
        edgeText[i][j].setColor(getEdgeTextColor(i, j));
      }
    }
  }

  /**
   * Is the current node entry highlighted?
   * 
   * @param i
   *          the index of the node
   */
  public boolean isElemHighlightedNode(int i) {
	  return isHighlightedNode(i);//TODO Spaeter einzeln Element hervorheben
//    if ((0 <= i) && (i < length)) {
//      return ((nodeStates[i] & ELEM_HIGHLIGHTED) == ELEM_HIGHLIGHTED);
//    }
//    return false;
  }

  /**
   * Is the current edge entry highlighted?
   * 
   * @param i
   *          the index of the edge
   */
  public boolean isElemHighlightedEdge(int i, int j) {
	  return isHighlightedEdge(i, j);//TODO Spaeter einzeln Element hervorheben
//    if ((0 <= i) && (i < length) && (0 <= j) && (j < length)) {
//      return ((edgeStates[i][j] & ELEM_HIGHLIGHTED) == ELEM_HIGHLIGHTED);
//    }
//    return false;
  }

  /**
   * Store the default size to the properties file.
   * 
   * @param m
   *          the default size
   */
  public void setSize(int m) {
    // getProperties().put(mapKey(GRAPH_SIZE), m);
    length = m;
    init();
  }

  /**
   * Retrieve the default size from the properties.
   */
  public int getSize() {
    return length;
    // return getProperties().getIntProperty(mapKey(GRAPH_SIZE), 1);
  }

  public int getLength() {
    return length;
  }

  /**
   * Set color of the background. This is the color of the background of the
   * nodes.
   * 
   * @param c
   *          a color value
   */
  public void setBGColor(Color c) {
    backgroundColor = c;
    // getProperties().put(mapKey(BG_COLOR), c);
    for (int i = 0; i < length; i++) {
      if (isActivatedNode(i)) {
        nodes[i].setFillColor(isHighlightedNode(i) ? getHighlightColor(i) : c);
      }
    }
  }

  /**
   * Retrieve the background color
   */
  public Color getBGColor() {
    return backgroundColor;
    // return getProperties().getColorProperty(mapKey(BG_COLOR), Color.white);
  }

  /**
   * Set the font of the node text.
   * 
   * @param f
   *          the font to set
   */
  public void setNodeFont(Font f) {
    // getProperties().put(reverseMapKey(NODE_FONT), f);
    nodeFont = f;
    nodeFm = Animal.getConcreteFontMetrics(f);
    for (int i = 0; i < length; i++) {
      nodeText[i].setFont(f);
    }
  }

  /**
   * Set the font of the edge text.
   * 
   * @param f
   *          the font to set
   */
  public void setEdgeFont(Font f) {
    // getProperties().put(reverseMapKey(EDGE_FONT), f);
    edgeFont = f;
    edgeFm = Animal.getConcreteFontMetrics(f);

    for (int i = 0; i < length; i++) {
      for (int j = 0; j < length; j++) {
        edgeText[i][j].setFont(f);
      }
    }
  }

  /**
   * Retrieve the currently used font for nodes
   */
  public Font getNodeFont() {
    return nodeFont;
    // return getProperties().getFontProperty(mapKey(NODE_FONT), nodeFont);
  }

  /**
   * Retrieve the currently used font for edges
   */
  public Font getEdgeFont() {
    return edgeFont;
    // return getProperties().getFontProperty(mapKey(EDGE_FONT), edgeFont);
  }

  /**
   * Set the text to the given color for nodes
   * 
   * @param c
   *          the requested color
   */
  public void setNodeFontColor(Color c) {
    setColor(c);
    // getProperties().put(mapKey(NODE_FONT_COLOR), c);
    for (int i = 0; i < length; i++) {
      nodeText[i].setColor(c);
    }
  }

  /**
   * Set the text to the given color for edges
   * 
   * @param c
   *          the requested color
   */
  public void setEdgeFontColor(Color c) {
    // getProperties().put(mapKey(EDGE_FONT_COLOR), c);
    edgeFontColor = c;
    for (int i = 0; i < length; i++) {
      for (int j = 0; j < length; j++) {
        edgeText[i][j].setColor(getEdgeTextColor(i, j));
      }
    }
  }

  /**
   * Retrieve the color of the text for nodes
   */
  public Color getNodeFontColor() {
    return getColor();
    // return getProperties().getColorProperty(mapKey(NODE_FONT_COLOR),
    // Color.black);
  }

  /**
   * Retrieve the color of the text for edges
   */
  public Color getEdgeFontColor() {
    return edgeFontColor;
    // return getProperties().getColorProperty(mapKey(EDGE_FONT_COLOR),
    // Color.black);
  }

  public void setEdgeHighlightFontColor(Color c, int indexFrom, int indexTo) {
	  edgeHighlightFontColorArray[indexFrom][indexTo] = c;
      edgeText[indexFrom][indexTo].setColor(getEdgeTextColor(indexFrom, indexTo));
  }
  public Color getEdgeHighlightFontColor(int indexFrom, int indexTo) {
	    return edgeHighlightFontColorArray[indexFrom][indexTo];
  }
  
  public Color getEdgeTextColor(int indexFrom, int indexTo){
	  return isHighlightedEdge(indexFrom, indexTo) ? getEdgeHighlightFontColor(indexFrom, indexTo) : getEdgeFontColor();
  }

  public void setEdgeHighlightPolyColor(Color c, int indexFrom, int indexTo) {
	  edgeHighlightPolyColorArray[indexFrom][indexTo] = c;
      if (indexFrom != indexTo) {
          edges[indexFrom][indexTo].setColor(getEdgePolyColor(indexFrom, indexTo));
      } else {
    	  edgeLoop[indexFrom].setColor(getEdgePolyColor(indexFrom, indexTo));
      }
  }
  public Color getEdgeHighlightPolyColor(int indexFrom, int indexTo) {
	    return edgeHighlightPolyColorArray[indexFrom][indexTo];
  }
  
  public Color getEdgePolyColor(int indexFrom, int indexTo){
	  return isHighlightedEdge(indexFrom, indexTo) ? getEdgeHighlightPolyColor(indexFrom, indexTo) : getOutlineColor();
  }

  /**
   * Set the color of the highlighted cells.
   * 
   * @param c
   *          the color that will be applied to highlighted cells
   */
  public void setHighlightColor(Color c) {
    highlightColor = c;
    for (int i = 0; i < length; i++) {
    	highlightColorArray[i] = highlightColor;
	}
    // getProperties().put(mapKey(HIGHLIGHT_COLOR), c);
    for (int i = 0; i < length; i++) {
      if (isActivatedNode(i)) {
        nodes[i].setFillColor(isHighlightedNode(i) ? c : getBGColor());
      }
    }
    for (int i = 0; i < length; i++) {
      for (int j = 0; j < length; j++) {
        if (isActivatedEdge(i, j)) {
        	setEdgeHighlightPolyColor(c, i, j);
//          if (i != j) {
//            edges[i][j].setColor(getEdgePolyColor(i, j));
//          } else {
//            edgeLoop[i].setColor(getEdgePolyColor(i, j));
//          }
        }
      }
    }
  }

  /**
   * Get the color of highlighted cells.
   */
  public Color getHighlightColor() {
    return highlightColor;
    // return getProperties().getColorProperty(mapKey(HIGHLIGHT_COLOR),
    // Color.yellow);
  }

  public Color getHighlightColor(int i) {
    return highlightColorArray[i];
  }
  public void setHighlightColor(Color c, int i) {
  		highlightColorArray[i] = c;
	    if (isActivatedNode(i)) {
	    	nodes[i].setFillColor(isHighlightedNode(i) ? c : getBGColor());
	    }
  }

  /**
   * Set the color of the highlighted cells entries.
   * 
   * @param c
   *          the color that will be applied to highlighted entries
   */
  public void setElemHighlightColor(Color c) {
    // getProperties().put(mapKey(ELEM_HIGHLIGHT_COLOR), c);
    elemHighlightColor = c;
    for (int i = 0; i < length; i++) {
    	elemHighlightColorArray[i] = elemHighlightColor;
	}
    for (int i = 0; i < length; i++) {
      if (isActivatedNode(i)) {
        nodeText[i].setColor(isElemHighlightedNode(i) ? c : getNodeFontColor());
      }
    }
  }

  /**
   * Get the color of highlighted cell entries.
   */
  public Color getElemHighlightColor() {
    return elemHighlightColor;
    // return getProperties().getColorProperty(mapKey(ELEM_HIGHLIGHT_COLOR),
    // Color.red);
  }

  public Color getElemHighlightColor(int i) {
    return elemHighlightColorArray[i];
  }
  public void setElemHighlightColor(Color c, int i) {
	  	elemHighlightColorArray[i] = c;
	    if (isActivatedNode(i)) {
	        nodeText[i].setColor(isElemHighlightedNode(i) ? c : getNodeFontColor());
	    }
}

  /**
   * Set the outline color to the given one.
   * 
   * @param c
   *          the wanted color
   */
  public void setOutlineColor(Color c) {
    // getProperties().put(mapKey(OUTLINE_COLOR), c);
    outlineColor = c;
    for (int i = 0; i < length; i++) {
      nodes[i].setColor(c);
    }
    for (int i = 0; i < length; i++) {
      for (int j = 0; j < length; j++) {
        if (i != j) {
          edges[i][j].setColor(getEdgePolyColor(i, j));
        } else {
          edgeLoop[i].setColor(getEdgePolyColor(i, j));
        }
      }
    }
  }

  /**
   * Return the color of the graph outlines.
   */
  public Color getOutlineColor() {
    // return getProperties().getColorProperty(mapKey(OUTLINE_COLOR),
    // Color.black);
    return outlineColor;
  }

  /**
   * Return the x-coordinate of a node
   */
  public int getXNode(int index) {
    return nodeText[index].getLocation().x;
  }

  /**
   * Return the y-coordinate of a node
   */
  public int getYNode(int index) {
    return nodeText[index].getLocation().y;
  }

  /**
   * Overwrite method of super class for completeness.
   * 
   * Doesn't call super method to avoid double execution of translate method.
   */
  public void setLocation(Point p) {
    setOrigin(p);
  }

  /**
   * Set the upper left corner of the graph
   * 
   * @param p
   *          the point to which to set the origin
   */
  public void setOrigin(Point p) {
    setOrigin(p.x, p.y);
  }

  /**
   * Set the upper left corner of the graph
   * 
   * @param xpos
   *          the x-coordinate of the origin
   * @param ypos
   *          the y-coordinate of the origin
   */
  public void setOrigin(int xpos, int ypos) {
    translate(xpos - origin.x, ypos - origin.y);
  }

  public Point getOrigin() {
    return origin;
  }

  /**
   * The bounding box for the whole PTArray, specified by the the top left
   * point, width and height.
   */
  public Rectangle getBoundingBox() {
    Rectangle r = nodes[0].getBoundingBox();
    Rectangle r1 = new Rectangle();
    for (int i = 0; i < length; i++) {
      r1 = nodes[i].getBoundingBox();
      r = SwingUtilities.computeUnion(r1.x, r1.y, r1.width, r1.height, r);
      r1 = nodeText[i].getBoundingBox();
      r = SwingUtilities.computeUnion(r1.x, r1.y, r1.width, r1.height, r);
      if (!edgeText[i][i].getText().equalsIgnoreCase(UNDEFINED_EDGE)) {
        r1 = edgeLoop[i].getBoundingBox();
        r = SwingUtilities.computeUnion(r1.x, r1.y, r1.width, r1.height, r);
        r1 = edgeText[i][i].getBoundingBox();
        r = SwingUtilities.computeUnion(r1.x, r1.y, r1.width, r1.height, r);
      }
    }
    for (int j = 0; j < length; j++) {
      for (int k = 0; k < length; k++) {
        if (j != k) {
          if (!edgeText[j][k].getText().equalsIgnoreCase(UNDEFINED_EDGE)) {
            r1 = edges[j][k].getBoundingBox();
            r = SwingUtilities.computeUnion(r1.x, r1.y, r1.width, r1.height, r);
            r1 = edgeText[j][k].getBoundingBox();
            r = SwingUtilities.computeUnion(r1.x, r1.y, r1.width, r1.height, r);
          }
        }
      }
    }
    return r;
  }

  public Rectangle getBoundingBox(int i) {
    Rectangle[] temp = this.getBoundingBoxes();
    return temp[i];
  }

  /**
   * An array of bounding boxes for all the cells of the graph, each described
   * as a rectangle.
   */
  public Rectangle[] getBoundingBoxes() {
    Rectangle[] temp = new Rectangle[length * (2 * length + 3)];
    for (int i = 0; i < length; i++) {
      temp[i] = nodes[i].getBoundingBox();
    }
    for (int i = 0; i < length; i++) {
      temp[i + length] = nodeText[i].getBoundingBox();
    }
    for (int i = 0; i < length; i++) {
      temp[i + 2 * length] = edgeLoop[i].getBoundingBox();
    }
    for (int j = 0; j < length; j++) {
      for (int k = 0; k < length; k++) {
        temp[(j + 3) * length + k] = edges[j][k].getBoundingBox();
      }
    }
    for (int j = 0; j < length; j++) {
      for (int k = 0; k < length; k++) {
        temp[(j + 3 + length) * length + k] = edgeText[j][k].getBoundingBox();
      }
    }
    return temp;
  }

  /**
   * places a node
   * 
   * @param i
   *          index of the node
   * @param x
   *          horizontal position
   * @param y
   *          vertical position
   */
  public void setPositionNode(int i, int x, int y) {
    nodeText[i].setLocation(new Point(x, y));
    Rectangle temp = nodeText[i].getBoundingBox();
    nodes[i].setCenter(new Point((temp.x + (temp.width) / 2), temp.y
        + (temp.height) / 2));
    setNodeEdges(i);
  }

  public void setPositionNode(int i, Point p) {
    setPositionNode(i, p.x, p.y);
  }

  /**
   * resizes the node at the index i
   */
  public void resizeNode(int i) {
    Rectangle r = nodeText[i].getBoundingBox();
    int rad = (r.width + 20) / 2;
    nodes[i].setCenter(new Point((r.x + (r.width) / 2), r.y + (r.height) / 2));
    if(nodesRadius[i]==null){
    	if(rad>15){
    		rad =15;
    	}
        nodes[i].setRadius(rad);
    }else{
    	if(rad>15){
    		rad =15;
    	}
        nodes[i].setRadius(nodesRadius[i]);
    }
    setNodeEdges(i);
  }

  /**
   * Sets the position of the elements of the edge (i,j). This position is
   * different if the graph is directed.
   */
  public void setEdge(int i, int j) {
    if (i != j) {

      double x0 = nodes[i].getCenter().x;
      double y0 = nodes[i].getCenter().y;
      double x1 = nodes[j].getCenter().x;
      double y1 = nodes[j].getCenter().y;
      double r0 = nodes[i].getRadius();
      double r1 = nodes[j].getRadius();

      if (x0 != x1 || y0 != y1) {

        double l = Math.sqrt(Math.pow((x1 - x0), 2) + Math.pow((y1 - y0), 2));

        int X0, Y0, X1, Y1;

        if (directed == false) { // direction is false

        	 X0 = (int) Math.round(x0 + r0 * ((x1 - x0) / l));
             Y0 = (int) Math.round(y0 + r0 * ((y1 - y0) / l));
             X1 = (int) Math.round(x1 + r1 * ((x0 - x1) / l));
             Y1 = (int) Math.round(y1 + r1 * ((y0 - y1) / l));

             
             if(Math.abs(X0-X1) > Math.abs(Y0-Y1) && Math.abs(X0-X1) >1 && Math.abs(Y0-Y1)>1){
             	
           
            
             	if(Y0 -Y1 <0){
             			if((Y0-Y1)%2 == 0){
                 			Y0 = Y0 +1;
                 			Y1 = Y1 -1;
                 			if(X0 < X1){
     	            			X0 = X0 -1;
     	            			X1 = X1 +1;
                 			}else{
                 				X0 = X0 +1;
     	            			X1 = X1 -1;
                 				
                 			}
             			}else{
             				Y0 = Y0 +1;
                 			Y1 = Y1 -2;
                 			if(X0 < X1){
     	            			X0 = X0 -1;
     	            			X1 = X1 +1;
                 			}else{
                 				X0 = X0 +1;
     	            			X1 = X1 -1;
                 				
                 			}
             			}
             			if(Math.abs(Y0-Y1) !=0){
	             			if((Math.abs(X0-X1)% Math.abs(Y0-Y1)) %2 != 0){
	             				Y0 = Y0+1;
	             			}
	             			/*if((Math.abs(X0-X1)% Math.abs(Y0-Y1)) >=2){
	             				Y0 = Y0+2;
	             				Y1 = Y1-2;
	             			}*/
             			}	
             		}else{
             			if(Y1 -Y0 <0){
     	              		if((Y1-Y0)%2 == 0){
     	              			Y0 = Y0 -1;
     	              			Y1 = Y1 +1;
     	              			if(X0 < X1){
         	            			X0 = X0 -1;
         	            			X1 = X1 +1;
                     			}else{
                     				X0 = X0 +1;
         	            			X1 = X1 -1;
                     				
                     			}
     	          			}else{
     	          				Y0 = Y0 -2;
     	              			Y1 = Y1 +1;
     	              			if(X0 < X1){
         	            			X0 = X0 -1;
         	            			X1 = X1 +1;
                     			}else{
                     				X0 = X0 +1;
         	            			X1 = X1 -1;
                     				
                     			}  	            
     	          			}
             		  	}
             		}
             	if(Math.abs(Y0-Y1) !=0){
	             	if((Math.abs(X0-X1)% Math.abs(Y0-Y1)) %2 != 0){
	     				Y1 = Y1+1;
	     			}
	             	/*if((Math.abs(X0-X1)% Math.abs(Y0-Y1)) >=2){
         				Y0 = Y0-2;
         				Y1 = Y1+2;
         			}*/
             	}	
             }else{
            	 if( Math.abs(X0-X1) >1 && Math.abs(Y0-Y1)>1){
	             	if(X0 -X1 <0){
	         			if((X0-X1)%2 == 0){
	             			X0 = X0 +1;
	             			X1 = X1 -1;
	             			if(Y0 < Y1){
     	            			Y0 = Y0 -1;
     	            			Y1 = Y1 +1;
                 			}else{
                 				Y0 = Y0 +1;
     	            			Y1 = Y1 -1;
                 				
                 			}
	         			}else{
	         				X0 = X0 -1;
	             			X1 = X1 +2;
	             			if(Y0 < Y1){
     	            			Y0 = Y0 -1;
     	            			Y1 = Y1 +1;
                 			}else{
                 				Y0 = Y0 +1;
     	            			Y1 = Y1 -1;
                 				
                 			}
	         			}
	         			if(Math.abs(X0-X1) !=0){
		         			if((Math.abs(Y0-Y1)% Math.abs(X0-X1)) %2 != 0){
	             				X0 = X0+1;
	             			}
		         		/*	if((Math.abs(Y0-Y1)% Math.abs(X0-X1)) >=2){
	             				X0 = X0+2;
	             				X1 = X1-2;
	             			}*/
	         			}	
	         		}else{
	         			if(X1 -X0 <0){
	                 		if((X1-X0)%2 == 0){
	                 			X0 = X0 -1;
	                 			X1 = X1 +1;
	                 			if(Y0 < Y1){
	     	            			Y0 = Y0 -1;
	     	            			Y1 = Y1 +1;
	                 			}else{
	                 				Y0 = Y0 +1;
	     	            			Y1 = Y1 -1;
	                 				
	                 			}
	             			}else{
	             				X0 = X0 +2;
	                 			X1 = X1 -1;
	                 			if(Y0 < Y1){
	     	            			Y0 = Y0 -1;
	     	            			Y1 = Y1 +1;
	                 			}else{
	                 				Y0 = Y0 +1;
	     	            			Y1 = Y1 -1;
	                 				
	                 			}
	             			}
	         		  	}
	         		}
	             	if(Math.abs(X0-X1) !=0){
		             	if((Math.abs(Y0-Y1)% Math.abs(X0-X1)) %2 != 0){
	         				X1 = X1+1;
	         			}
		             /*	if((Math.abs(Y0-Y1)% Math.abs(X0-X1)) >=2){
             				X0 = X0-2;
             				X1 = X1+2;
             			}*/
	             	}	
             	}	
             }
           

            	
          edges[i][j].setNode(0, new PTPoint(X0, Y0));
          edges[i][j].setNode(1, new PTPoint(X1, Y1));

          edges[i][j].setFWArrow(false);

        } else { // direction is true

          double beta = 10;
          double alpha = 0;
          if (y1 > y0) {
            alpha = Math.toDegrees(Math.acos((x1 - x0) / l));
          } else {
            if (y1 < y0) {
              alpha = -Math.toDegrees(Math.acos((x1 - x0) / l));
            } else {
              if (x1 < x0) {
                alpha = 180;
              }
            }
          }

          if (edgeText[j][i].getText().equalsIgnoreCase(UNDEFINED_EDGE)) {
            X0 = (int) Math.round(x0 + r0 * ((x1 - x0) / l));
            Y0 = (int) Math.round(y0 + r0 * ((y1 - y0) / l));
            X1 = (int) Math.round(x1 + r1 * ((x0 - x1) / l));
            Y1 = (int) Math.round(y1 + r1 * ((y0 - y1) / l));
          } else {
            X0 = (int) Math.round(x0 + r0
                * Math.cos(Math.toRadians(alpha + beta)));
            Y0 = (int) Math.round(y0 + r0
                * Math.sin(Math.toRadians(alpha + beta)));
            X1 = (int) Math.round(x1 + r1
                * Math.cos(Math.toRadians(alpha + 180 - beta)));
            Y1 = (int) Math.round(y1 + r1
                * Math.sin(Math.toRadians(alpha + 180 - beta)));
          }
        //experimential
        
          if(Math.abs(X0-X1) > Math.abs(Y0-Y1) && Math.abs(X0-X1) >1 && Math.abs(Y0-Y1)>1){
           	
              
              
           	if(Y0 -Y1 <0){
           			if((Y0-Y1)%2 == 0){
               			Y0 = Y0 +1;
               			Y1 = Y1 -1;
               			if(X0 < X1){
   	            			X0 = X0 -1;
   	            			X1 = X1 +1;
               			}else{
               				X0 = X0 +1;
   	            			X1 = X1 -1;
               				
               			}
           			}else{
           				Y0 = Y0 +1;
               			Y1 = Y1 -2;
               			if(X0 < X1){
   	            			X0 = X0 -1;
   	            			X1 = X1 +1;
               			}else{
               				X0 = X0 +1;
   	            			X1 = X1 -1;
               				
               			}
           			}
           			if(Math.abs(Y0-Y1) !=0)
	             			if((Math.abs(X0-X1)% Math.abs(Y0-Y1)) %2 != 0){
	             				Y0 = Y0+1;
	             			}
           		}else{
           			if(Y1 -Y0 <0){
   	              		if((Y1-Y0)%2 == 0){
   	              			Y0 = Y0 -1;
   	              			Y1 = Y1 +1;
   	              			if(X0 < X1){
       	            			X0 = X0 -1;
       	            			X1 = X1 +1;
                   			}else{
                   				X0 = X0 +1;
       	            			X1 = X1 -1;
                   				
                   			}
   	          			}else{
   	          				Y0 = Y0 -2;
   	              			Y1 = Y1 +1;
   	              			if(X0 < X1){
       	            			X0 = X0 -1;
       	            			X1 = X1 +1;
                   			}else{
                   				X0 = X0 +1;
       	            			X1 = X1 -1;
                   				
                   			}  	            
   	          			}
           		  	}
           		}
           	if(Math.abs(Y0-Y1) !=0)
	             	if((Math.abs(X0-X1)% Math.abs(Y0-Y1)) %2 != 0){
	     				Y1 = Y1+1;
	     			}
           }else{
          	 if( Math.abs(X0-X1) >1 && Math.abs(Y0-Y1)>1){
	             	if(X0 -X1 <0){
	         			if((X0-X1)%2 == 0){
	             			X0 = X0 +1;
	             			X1 = X1 -1;
	             			if(Y0 < Y1){
   	            			Y0 = Y0 -1;
   	            			Y1 = Y1 +1;
               			}else{
               				Y0 = Y0 +1;
   	            			Y1 = Y1 -1;
               				
               			}
	         			}else{
	         				X0 = X0 -1;
	             			X1 = X1 +2;
	             			if(Y0 < Y1){
   	            			Y0 = Y0 -1;
   	            			Y1 = Y1 +1;
               			}else{
               				Y0 = Y0 +1;
   	            			Y1 = Y1 -1;
               				
               			}
	         			}
	         			if(Math.abs(X0-X1) !=0)
		         			if((Math.abs(Y0-Y1)% Math.abs(X0-X1)) %2 != 0){
	             				X0 = X0+1;
	             			}
	         		}else{
	         			if(X1 -X0 <0){
	                 		if((X1-X0)%2 == 0){
	                 			X0 = X0 -1;
	                 			X1 = X1 +1;
	                 			if(Y0 < Y1){
	     	            			Y0 = Y0 -1;
	     	            			Y1 = Y1 +1;
	                 			}else{
	                 				Y0 = Y0 +1;
	     	            			Y1 = Y1 -1;
	                 				
	                 			}
	             			}else{
	             				X0 = X0 +2;
	                 			X1 = X1 -1;
	                 			if(Y0 < Y1){
	     	            			Y0 = Y0 -1;
	     	            			Y1 = Y1 +1;
	                 			}else{
	                 				Y0 = Y0 +1;
	     	            			Y1 = Y1 -1;
	                 				
	                 			}
	             			}
	         		  	}
	         		}
	             	if(Math.abs(X0-X1) !=0)
		             	if((Math.abs(Y0-Y1)% Math.abs(X0-X1)) %2 != 0){
	         				X1 = X1+1;
	         			}
	             	
           	}	
           }
         

          
          edges[i][j].setNode(0, new PTPoint(X0, Y0));
          edges[i][j].setNode(1, new PTPoint(X1, Y1));

        //  edges[i][j].setFWArrow(true);
         
          edges[i][j].setDepth(getDepth()+6);

        }

        if (x1 > x0) {
          if (y1 > y0) {
           /* edgeText[i][j].setLocation(new Point(X0 + (X1 - X0) / 2
                - edgeFm.stringWidth(edgeText[i][j].getText()), Y0 + (Y1 - Y0)
                / 2 + edgeFm.getHeight()));*/
        	  if(Math.abs(x1-x0)>Math.abs(y1-y0))
        	  edgeText[i][j].setLocation(new Point(X0 + (X1 - X0) / 2  - edgeFm.stringWidth(edgeText[i][j].getText()), Y0 + (Y1 - Y0)  / 2 - edgeFm.getDescent()));
        	  else{
        		  edgeText[i][j].setLocation(new Point(X0 + (X1 - X0) / 2  - edgeFm.stringWidth(edgeText[i][j].getText()), Y0 + (Y1 - Y0)  / 2 - edgeFm.getDescent()));
        	  }
        	  edgeText[i][j].setDepth(getDepth()-6);
        	 
            
          } else {
            if (y1 < y0) {
            	
            	 if(Math.abs(x1-x0)>Math.abs(y1-y0))
            		edgeText[i][j].setLocation(new Point(X0 + (X1 - X0) / 2, Y0 + (Y1 - Y0) / 2 - edgeFm.getDescent()));  // 3 2 1 5
            	 else{
            		 edgeText[i][j].setLocation(new Point(X0 + (X1 - X0) / 2, Y0 + (Y1 - Y0) / 2 - edgeFm.getDescent()));  // 3 2 1 5
            	 }
            	 edgeText[i][j].setDepth(getDepth()-6);
            	 
            } else {
            /*  edgeText[i][j].setLocation(new Point(X0 + (X1 - X0) / 2, Y0
                  + (Y1 - Y0) / 2 + edgeFm.getHeight()));*/
            	  edgeText[i][j].setLocation(new Point(X0 + (X1 - X0) / 2, Y0
                          + (Y1 - Y0) / 2)); //uncheckt
            	  edgeText[i][j].setDepth(getDepth()-6);
          
            }
          }
        } else {
          if (x1 < x0) {
            if (y1 > y0) {
             /* edgeText[i][j].setLocation(new Point(X0 + (X1 - X0) / 2
                  - edgeFm.stringWidth(edgeText[i][j].getText()), Y0
                  + (Y1 - Y0) / 2 - edgeFm.getDescent()));*/
            	 //if(Math.abs(x1-x0)>Math.abs(y1-y0))
            		 edgeText[i][j].setLocation(new Point(X0 + (X1 - X0) / 2- edgeFm.stringWidth(edgeText[i][j].getText()), Y0    + (Y1 - Y0) / 2 ));//- edgeFm.getDescent())); //3
            	// else{
            	//	 edgeText[i][j].setLocation(new Point(X0 + (X1 - X0) / 2- edgeFm.stringWidth(edgeText[i][j].getText()), Y0    + (Y1 - Y0) / 2 ));//- edgeFm.getDescent())); //3
            	// }
            	 edgeText[i][j].setDepth(getDepth()-6);
            	
            } else {
              if (y1 < y0) {
            	  if(Math.abs(x1-x0)>Math.abs(y1-y0))
            		  edgeText[i][j].setLocation(new Point(X0 + (X1 - X0) / 2, Y0+ (Y1 - Y0) / 2 ));//+ edgeFm.getHeight()));
            	  else{
            		  edgeText[i][j].setLocation(new Point(X0 + (X1 - X0) / 2 -edgeFm.stringWidth(edgeText[i][j].getText()), Y0+ (Y1 - Y0) / 2 ));//+ edgeFm.getHeight()));
            	  }
                edgeText[i][j].setDepth(getDepth()-6);
               
              } else {
               /* edgeText[i][j].setLocation(new Point(X0 + (X1 - X0) / 2, Y0
                    + (Y1 - Y0) / 2 + edgeFm.getHeight()));*/
            	//  if(Math.abs(x1-x0)>Math.abs(y1-y0))
            		  edgeText[i][j].setLocation(new Point(X0 + (X1 - X0) / 2, Y0 + (Y1 - Y0) / 2));
            	 /* else{
            		  edgeText[i][j].setLocation(new Point(X0 + (X1 - X0) / 2, Y0 + (Y1 - Y0) / 2));
            	  }*/
            	  edgeText[i][j].setDepth(getDepth()-6);
              
              }
            }
          } else {
            if (y1 > y0) {
            /* edgeText[i][j].setLocation(new Point(X0 + (X1 - X0) / 2
                  - edgeFm.stringWidth(edgeText[i][j].getText()) - 5, Y0
                  + (Y1 - Y0) / 2));*/
            	// if(Math.abs(x1-x0)>Math.abs(y1-y0))
            		 edgeText[i][j].setLocation(new Point(X0 + (X1 - X0) / 2 -7, Y1 - (Y1 - Y0) / 2 -8));
            	/* else{
            		 edgeText[i][j].setLocation(new Point(X0 + (X1 - X0) / 2, Y0 + (Y1 - Y0) / 2));
            	 }*/
            	 edgeText[i][j].setDepth(getDepth()-6);
            	
            } else {
              if (y1 < y0) {
            	 // if(Math.abs(x1-x0)>Math.abs(y1-y0))
            		  edgeText[i][j].setLocation(new Point(X0 + (X1 - X0) / 2 +5, Y0   - (Y0 - Y1) / 2));
            	 /* else{
            		  edgeText[i][j].setLocation(new Point(X0 + (X1 - X0) / 2 + 5, Y0   + (Y1 - Y0) / 2));
            	  }*/
                edgeText[i][j].setDepth(getDepth()-6);
                
              }
            }
          }
        }

      }
    } else { // i == j
      int x0 = nodes[i].getCenter().x;
      int y0 = nodes[i].getCenter().y;
      int r0 = nodes[i].getRadius();
      int r = 15;

      int alpha = (int) Math.round(Math.toDegrees(Math.acos(r0
          / Math.sqrt((Math.pow(r0, 2) + Math.pow(r, 2))))));
      int y = (int) Math.round((y0 - Math.sqrt((Math.pow(r0, 2) + Math
          .pow(r, 2)))));

      edgeLoop[i].setCenter(new Point(x0, y));
      edgeLoop[i].setTotalAngle(180 + 2 * alpha);
      edgeLoop[i].setRadius(r);
      edgeLoop[i].setStartAngle(-alpha);
      edgeText[i][i].setLocation(new Point(x0
          - edgeFm.stringWidth(edgeText[i][i].getText()) / 2, y - r
          - edgeFm.getDescent()));

      if (directed == false) {
        edgeLoop[i].setFWArrow(false);
      } else {
        edgeLoop[i].setFWArrow(true);
      }

    }
  }

  /**
   * sets all the edges for the indexth node
   */
  public void setNodeEdges(int index) {
    for (int i = 0; i < length; i++) {
      setEdge(index, i);
      setEdge(i, index);
    }
  }

  /**
   * sets all the edges of the graph
   */
  public void setEdges() {
    for (int i = 0; i < length; i++) {
      for (int j = 0; j < length; j++) {
        setEdge(i, j);
      }
    }
  }

  // ================
  // INTERNAL METHODS
  // ================

  /**
   * Return the object type as "PTGraph".
   */
  public String getType() {
    return TYPE_LABEL;
  }

  /**
   * Translate this PTGraph by the given values.
   * 
   * @param dx
   *          the horizontal translation in pixels
   * @param dy
   *          the vertical translation in pixels
   */
  public void translate(int dx, int dy) {
    for (int i = 0; i < length; i++) {
      nodeText[i].translate(dx, dy);
      nodes[i].translate(dx, dy);
      edgeLoop[i].translate(dx, dy);
    }
    for (int j = 0; j < length; j++) {
      for (int k = 0; k < length; k++) {
        edgeText[j][k].translate(dx, dy);
        edges[j][k].translate(dx, dy);
      }
    }
  }

  public void translateNode(int index, int dx, int dy) {
    nodeText[index].translate(dx, dy);
    nodes[index].translate(dx, dy);
    setNodeEdges(index);
  }

  public void translateNodes(boolean[] moveTheseOnly, int deltaX, int deltaY) {
    int maxIndex = getLength(), nodeNr;

    // Iterate on all nodes, telling each to translate itself
    for (nodeNr = 0; nodeNr < maxIndex; nodeNr++)
      if (moveTheseOnly[nodeNr])
        translateNode(nodeNr, deltaX, deltaY);
    // All points are translated => PTGraph is translated.
  }

  public void translateNodesTo(boolean[] moveTheseOnly, int deltaX, int deltaY) {
    int maxIndex = getLength(), nodeNr;

    // Iterate on all nodes, telling each to translate itself
    for (nodeNr = 0; nodeNr < maxIndex; nodeNr++)
      if (moveTheseOnly[nodeNr]) {
        Point old = getNode(nodeNr).getBoundingBox().getLocation(), now = new Point(deltaX, deltaY), diff = MSMath
            .diff(now, old);
        translateNode(nodeNr, diff.x, diff.y);
      }
    // All points are translated => PTGraph is translated.
  }

  /**
   * Clone method to create a clone from a PTStringArray object
   */
  public Object clone() {
    // PTGraph graph = (PTGraph) super.clone();
    PTGraph graph = new PTGraph();
    cloneCommonFeaturesInto(graph);
    // graph.setDepth(getDepth());
    // graph.setColor(new Color(color.getRed(), color.getGreen(),
    // color.getBlue()));
    // TODO needs to be done...
    graph.backgroundColor = createColor(backgroundColor);
    graph.elemHighlightColor = createColor(elemHighlightColor);
    graph.highlightColor = createColor(highlightColor);
    graph.outlineColor = createColor(outlineColor);
    
    //TODO
    graph.highlightColorArray = new Color[length];
    for (int i = 0; i < highlightColorArray.length; i++) {
    	graph.highlightColorArray[i] = new Color(highlightColorArray[i].getRGB());
	}
    graph.elemHighlightColorArray = new Color[length];
    for (int i = 0; i < elemHighlightColorArray.length; i++) {
    	graph.elemHighlightColorArray[i] = new Color(elemHighlightColorArray[i].getRGB());
	}
    graph.edgeHighlightFontColorArray = new Color[length][length];
    for (int i = 0; i < edgeHighlightFontColorArray.length; i++) {
    	for (int j = 0; j < edgeHighlightFontColorArray[i].length; j++) {
    		graph.edgeHighlightFontColorArray[i][j] = new Color(edgeHighlightFontColorArray[i][j].getRGB());
    	}
	}
    graph.edgeHighlightPolyColorArray = new Color[length][length];
    for (int i = 0; i < edgeHighlightPolyColorArray.length; i++) {
    	for (int j = 0; j < edgeHighlightPolyColorArray[i].length; j++) {
    		graph.edgeHighlightPolyColorArray[i][j] = new Color(edgeHighlightPolyColorArray[i][j].getRGB());
    	}
	}
    graph.nodesRadius = new Integer[length];
    for (int i = 0; i < nodesRadius.length; i++) {
    	if(nodesRadius[i]!=null){
        	graph.nodesRadius[i] = new Integer(nodesRadius[i].intValue());
    	}else{
        	graph.nodesRadius[i] = null;
    	}
	}
    
    graph.showIndices = showIndices;
    // PTGraph.DefaultProperties = (XProperties) DefaultProperties.clone();
    graph.clonePropertiesFrom(getProperties(), true);
    graph.nodeFont = new Font(nodeFont.getName(), nodeFont.getStyle(), nodeFont
        .getSize());
    graph.edgeFont = new Font(edgeFont.getName(), edgeFont.getStyle(), edgeFont
        .getSize());
    graph.nodeFm = Animal.getConcreteFontMetrics(nodeFm.getFont());
    graph.edgeFm = Animal.getConcreteFontMetrics(edgeFm.getFont());
    graph.nodeText = new PTText[length];
    graph.nodes = new PTCircle[length];
    graph.edgeText = new PTText[length][length];
    graph.edges = new PTPolyline[length][length];
    graph.edgeLoop = new PTOpenCircleSegment[length];
    graph.nodeStates = new byte[length];
    graph.edgeStates = new byte[length][length];
    graph.edgeWeightStates = new byte[length][length];
    graph.origin = (Point) origin.clone();
    for (int i = 0; i < length; i++) {
      graph.nodes[i] = (PTCircle) nodes[i].clone();
      graph.nodeStates[i] = nodeStates[i];
      graph.nodeText[i] = (PTText) nodeText[i].clone();
      graph.edgeLoop[i] = (PTOpenCircleSegment) edgeLoop[i].clone();
    }
    for (int i = 0; i < length; i++) {
      for (int j = 0; j < length; j++) {
        graph.edges[i][j] = (PTPolyline) edges[i][j].clone();
        graph.edgeStates[i][j] = edgeStates[i][j];
        graph.edgeText[i][j] = (PTText) edgeText[i][j].clone();
        graph.edgeWeightStates[i][j] = edgeWeightStates[i][j];
      }
    }
    graph.directed = directed;
    graph.weighted = weighted;
    graph.length = length;
    graph.showIndices = showIndices;
    return graph;
  }

  /**
   * Converts the PTGraph into a String
   */
  public String toString() {
    StringBuilder toString = new StringBuilder();
    toString.append(getType());
    if (getObjectName() != null)
      toString.append(" '" + getObjectName() + "'");
    toString.append(" with " + length + " nodes");
    return toString.toString();
  }

  /**
   * Set weight of the graph
   * 
   * @param state
   *          graph shall be weighed or not
   */
  public void setWeight(boolean state) {
    // getProperties().put(mapKey(WEIGHT), state);
    weighted = state;
  }

  /**
   * Is the graph weighted?
   */
  public boolean hasWeight() {
    return weighted;
    // return getProperties().getBoolProperty(mapKey(WEIGHT), true);
  }

  /**
   * Set direction of the graph
   * 
   * @param state
   *          graph shall be directed or not
   */
  public void setDirection(boolean state) {
    // getProperties().put(mapKey(DIRECTION), state);
    directed = state;
    setEdges();
  }

  /**
   * Is the graph directed?
   */
  public boolean hasDirection() {
    return directed;
    // return getProperties().getBoolProperty(mapKey(DIRECTION), true);
  }

  /**
   * Sets if the indices are shown
   * 
   */
  public void setIndices(boolean state) {
    // getProperties().put(mapKey(SHOW_INDICES), state);
    showIndices = state;
  }

  /**
   * Are the indices shown?
   * 
   */
  public boolean indicesShown() {
    return showIndices;
    // return getProperties().getBoolProperty(mapKey(SHOW_INDICES), false);
  }

  public boolean operationRequiresSpecialSelector(String methodName) {
    return (methodName != null && methodName.indexOf("Nodes") != -1);
  }

  public boolean enableMultiSelectionFor(String methodName) {
    return (methodName != null && methodName.indexOf("Nodes") != -1);
  }

  public String baseOperationName(String methodName) {
    if (methodName.endsWith("..."))
      return methodName.substring(0, methodName.indexOf('.'));
    else if (methodName.indexOf("Nodes") != 0)
      return methodName.substring(0, methodName.indexOf(' '));
    else
      return methodName;
  }

  public boolean compatibleMethod(String method) {
    return (method != null && method.indexOf("Nodes") != -1);
  }

  public int getFileVersion() {
    return 1;
  }

  // ================
  // INIT METHODS
  // ================

  protected void init() {

    nodeText = new PTText[length];
    nodes = new PTCircle[length];
    edgeText = new PTText[length][length];
    edges = new PTPolyline[length][length];
    edgeLoop = new PTOpenCircleSegment[length];
    nodeStates = new byte[length];
    edgeStates = new byte[length][length];
    edgeWeightStates = new byte[length][length];
    directed = true;
    weighted = true;
    showIndices = false;

    for (int index = 0; index < length; index++) {
      nodeText[index] = new PTText(Character.toString(Character.forDigit(
          10 + index % 26, 36)), nodeFont);
      nodeText[index].setLocation(new Point(origin.x + 50 * (index + 1),
          origin.y + 50));
      nodeText[index].setColor(getNodeFontColor());
      nodeText[index].setDepth(getDepth() - 2);

      PTCircle node = new PTCircle();
      // node.setFWArrow(false);
      // node.setBWArrow(false);
      // node.setArcAngle(720);
      Rectangle temp = nodeText[index].getBoundingBox();
      node.setCenter(new Point(temp.x + (temp.width) / 2, temp.y
          + (temp.height) / 2));
      // node.setCircle(true);
      // node.setClockwise(true);
     int  rad = getWidth(index) / 2;
    			if(rad>15){
    	    		rad =15;
    	    	}
      node.setRadius(rad);
      if (getDepth() < Integer.MAX_VALUE)
        node.setDepth(getDepth() + 1);
      else
        node.setDepth(getDepth());
      // node.setClosed(true);
      node.setColor(getOutlineColor());
      node.setFilled(true);
      node.setFillColor(getBGColor());
      // node.setStartAngle(0);
      nodes[index] = node;

      nodeStates[index] = ACTIVATED;
      nodeStates[index] = (byte) (nodeStates[index] | VISIBLE);

      PTOpenCircleSegment edge = new PTOpenCircleSegment();
      edge.setFWArrow(true);
      edge.setBWArrow(false);
      edge.setTotalAngle(300);
      edge.setCenter(new Point(origin.x, origin.y));
//      edge.setCircle(true);
      edge.setClockwise(false);
      edge.setRadius(0);
      edge.setDepth(getDepth());
//      edge.setClosed(false);
//      edge.setFilled(false);
      edge.setColor(getOutlineColor());
//      edge.setFillColor(getBGColor());
      edge.setStartAngle(0);
      edgeLoop[index] = edge;
    }

    for (int index1 = 0; index1 < length; index1++) {
      for (int index2 = 0; index2 < length; index2++) {
        edgeText[index1][index2] = new PTText(UNDEFINED_EDGE, edgeFont);
        edgeText[index1][index2].setLocation(new Point(origin.x, origin.y));
        edgeText[index1][index2].setColor(getEdgeFontColor());
        edgeText[index1][index2].setDepth(getDepth() - 2);

        PTPolyline edge = new PTPolyline();
        edge.setFWArrow(true);
        edge.setBWArrow(false);
        edge.setDepth(getDepth());
        // edge.setClosed(false);
        // edge.setFilled(false);
        edge.setColor(getOutlineColor());
        // TODO check if this omission is really OK (it should be...)
        // edge.setFillColor(getBGColor());
        edges[index1][index2] = edge;

        edgeStates[index1][index2] = ACTIVATED;
        edgeStates[index1][index2] = (byte) (edgeStates[index1][index2] | VISIBLE);
        edgeWeightStates[index1][index2] = ACTIVATED | VISIBLE;
      }
    }
    setDepth((getDepth() < 2) ? 2 : getDepth());
    setEdges();
  }

  /**
   * Get the width of the node as it should be depending on the entry and the
   * currently chosen font for that entry. Adds an additional space of 20 pixels
   * to the width of the text that is contained in the node.
   */
  protected int getWidth(int i) {
    if (nodeText[i] != null) {
      return nodeText[i].getBoundingBox().width + 20;
    }
    return 20;
  }

  // ================
  // GRAPHICAL OUTPUT
  // ================
  public void paint(Graphics g) {
    for (int index = 0; index < length; index++) {
      if (isVisibleNode(index)) {
        nodes[index].paint(g);
        nodeText[index].paint(g);
      }
    }

    if (!directed) {
      for (int i = 0; i < length; i++) {
        for (int j = i; j < length; j++) {
          if (isVisibleEdge(i, j)
              && !edgeText[i][j].getText().equalsIgnoreCase(UNDEFINED_EDGE)) {
            // if (edges[i][j].getNodeCount() > 0 && isVisibleEdge(i, j)) {
            // if (!edgeText[i][j].getText().equalsIgnoreCase(UNDEFINED_EDGE) &&
            // isVisibleEdge(i,j)){
            if (i != j) {
              edges[i][j].paint(g);
              if (weighted == true
                  && (edgeWeightStates[i][j] & VISIBLE) == VISIBLE) {
                edgeText[i][j].paint(g);
              }
            } else {
              edgeLoop[i].paint(g);
              if (weighted == true
                  && (edgeWeightStates[i][j] & VISIBLE) == VISIBLE) {
                edgeText[i][i].paint(g);
              }
            }
          } else {
            if (!edgeText[j][i].getText().equalsIgnoreCase(UNDEFINED_EDGE)
                && (i != j) && isVisibleEdge(j, i)) {
              edges[j][i].paint(g);
              if (weighted == true
                  && (edgeWeightStates[i][j] & VISIBLE) == VISIBLE) {
                edgeText[j][i].paint(g);
              }
            }
          }
        }
      }
    } else {
      for (int i = 0; i < length; i++) {
        for (int j = 0; j < length; j++) {
          // TODO if "hasWeigth", weight > 0?
          if (isVisibleEdge(i, j)
              && !edgeText[i][j].getText().equalsIgnoreCase(UNDEFINED_EDGE)) {
            // if (edges[i][j].getNodeCount() > 0 && isVisibleEdge(i, j)) {
            // if (!edgeText[i][j].getText().equalsIgnoreCase(UNDEFINED_EDGE) &&
            // isVisibleEdge(i,j)){
            if (i != j) {
              edges[i][j].paint(g);
              if (weighted == true
                  && (edgeWeightStates[i][j] & VISIBLE) == VISIBLE) {
                edgeText[i][j].paint(g);
              }
            } else {
              edgeLoop[i].paint(g);
              if (weighted == true
                  && (edgeWeightStates[i][j] & VISIBLE) == VISIBLE) {
                edgeText[i][i].paint(g);
              }
            }
          }
        }
      }
    }

    float[] hsb = Color.RGBtoHSB(getBGColor().getRed(),
        getBGColor().getGreen(), getBGColor().getBlue(), null);
    for (int index = 0; index < length; index++) {
      if (showIndices && isVisibleNode(index)) {
        PTText idx = new PTText(String.valueOf(index), nodeFont);
        idx.setLocation(new Point(nodeText[index].getLocation().x
            + nodeFm.stringWidth(nodeText[index].getText()), nodeText[index]
            .getLocation().y
            + nodeFm.getDescent()));
        idx.setDepth(getDepth() - 1);
        if (hsb[2] > 0.5) {
          if (hsb[1] > 0.5) {
            idx.setColor(Color.getHSBColor(hsb[0], hsb[1] - 0.1f, hsb[2])
                .darker());
          } else {
            idx.setColor(Color.getHSBColor(hsb[0], hsb[1] + 0.1f, hsb[2])
                .darker());
          }
        } else {
          if (hsb[1] > 0.5) {
            idx.setColor(Color.getHSBColor(hsb[0], hsb[1] - 0.1f, hsb[2])
                .brighter());
          } else {
            idx.setColor(Color.getHSBColor(hsb[0], hsb[1] + 0.1f, hsb[2])
                .brighter());
          }
        }
        idx.paint(g);
      }
    }

  }

  public void hideEdge(int startNode, int endNode) {
    setVisibleEdge(startNode, endNode, false);
    // // remove VISIBLE flag without touching other properties
    // edgeStates[startNode][endNode] ^= VISIBLE;
    // // setEdgeVisibility(startNode, endNode,
    // // edgeStates[startNode][endNode] ^ VISIBLE);
  }

  public void showEdge(int startNode, int endNode) {
    setVisibleEdge(startNode, endNode, true);
    // // set VISIBLE flag without touching other properties
    // edgeStates[startNode][endNode] |= VISIBLE;
    // // setEdgeVisibility(startNode, endNode,
    // // edgeStates[startNode][endNode] | VISIBLE);
  }

  public void hideNode(int index) {
    setVisibleNode(index, false);
    for (int i = 0; i < length; i++) {
      setVisibleEdge(i, index, false);
      setVisibleEdge(index, i, false);
    }
    // // setNodeVisibility(index, nodeStates[index] ^ VISIBLE);
    // // remove VISIBLE flag without touching other properties
    // nodeStates[index] ^= VISIBLE;
    // // hide all in- or outbound, previously visible edges
    // for (int i = 0; i < length; i++) {
    // hideEdge(i, index);
    // hideEdge(index, i);
    // }
  }

  public void showNode(int index) {
    setVisibleNode(index, true);
    for (int i = 0; i < length; i++) {
      // tricky: re-showing a node should re-show its edges
      // but only if the other node is also visible!
      if (isVisibleNode(i)) {
        setVisibleEdge(i, index, true);
        setVisibleEdge(index, i, true);
      }
    }
    //
    // // setNodeVisibility(index, nodeStates[index] | VISIBLE);
    // // add VISIBLE flag without touching other properties
    // nodeStates[index] |= VISIBLE;
    // // unhide all previously hidden edges
    // for (int i = 0; i < length; i++) {
    // showEdge(i, index);
    // showEdge(index, i);
    // }
  }

  protected void setEdgeVisibility(int startNode, int endNode, int newStatus) {
    edgeStates[startNode][endNode] = (byte) newStatus;
  }

  // protected void setNodeVisibility(int index, int newStatus) {
  // nodeStates[index] = (byte)newStatus;
  // // NEW
  // // also hide edges going from or to the hidden node!
  // for (int i = 0; i < length; i++) {
  // // setEdgeVisibility(i, index,, endNode, newStatus)
  // // setEdgeVisibility(i, index, edgeStates[i][index] ^ newStatus);
  // // setEdgeVisibility(index, i, edgeStates[i][index] ^ newStatus);
  // }
  // }

  public Font getFont() {
    return getNodeFont();
  }

  public void setFont(Font targetFont) {
    setNodeFont(targetFont);
  }

  /**
   * Update the default properties for this object
   * 
   * @param defaultProperties
   *          the properties to be updated
   */
  public void updateDefaults(XProperties defaultProperties) {
    super.updateDefaults(defaultProperties);
    defaultProperties.put(getType() + ".bgColor", backgroundColor);
    defaultProperties.put(getType() + ".directed", directed);
    Font f = getEdgeFont();
    defaultProperties.put(getType() + ".edgeFont", f);
    defaultProperties.put(getType() + ".edgeFontName", f.getFamily());
    defaultProperties.put(getType() + ".edgeFontSize", f.getSize());
    defaultProperties.put(getType() + ".edgeFontStyle", f.getStyle());
    defaultProperties.put(getType() + ".elemHighlightColor",
        getElemHighlightColor());
    defaultProperties.put(getType() + ".graphSize", length);
    defaultProperties.put(getType() + ".highlightColor", highlightColor);
    f = getNodeFont();
    defaultProperties.put(getType() + ".nodeFont", f);
    defaultProperties.put(getType() + ".nodeFontName", f.getFamily());
    defaultProperties.put(getType() + ".nodeFontSize", f.getSize());
    defaultProperties.put(getType() + ".nodeFontStyle", f.getStyle());
    defaultProperties.put(getType() + ".outlineColor", outlineColor);
    defaultProperties.put(getType() + ".showIndices", showIndices);
    defaultProperties.put(getType() + ".weighted", weighted);
  }

}