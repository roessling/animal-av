package generators.misc.id3_chi_squared;

import algoanim.primitives.Circle;
import algoanim.primitives.Ellipse;
import algoanim.primitives.Polyline;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.*;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.LinkedList;
import java.util.List;

/**
 * Node
 */
public class ID3ChiNode {

  private ID3ChiNode parent;
  private ID3ChiNode children[];

  private List<Date> data;
  private List<String> attributes;

  private algoanim.util.Node node;
  private Circle circle;
  private Ellipse ellipse;
  private Text text;
  private List<Polyline> lineList;
  private List<Text> lineText;
  private Color color;
  private Language lang;

  private boolean changed;

  private int radius;

  //numberOfClasses: count number of classes in data set
  //numberOfValues: each element of array is the amount of values
  //orderOfValues: array to differ between attributes, list to differ between values of the data
  //orderOfClasses: each element is a different seen class
  private int numberOfClasses;
  private int[] numberOfValues;
  private List<String>[] orderOfValues;
  private List<String> orderOfClasses;

  /**
   * Node creates a Circle instance and calls analyseData() to compute usefull informations. The
   * circle is hidden when created. This constructor is called by ID3.generate() and ID3.expand()
   *
   * @param parent pointer to parent node
   * @param node position of circle
   * @param color highlight color of the node
   * @param radius radius of the circle
   */
  public ID3ChiNode(ID3ChiNode parent, List<Date> data, List<String> attributes, algoanim.util.Node node,
                    Color color, Language lang, int radius) {
    this.parent = parent;
    this.data = data;
    this.node = node;
    this.lang = lang;
    this.attributes = attributes;
    this.radius = radius;
    this.color = color;
    this.lineList = new LinkedList<>();
    this.lineText = new LinkedList<>();
    changed = false;
    analyseData();

    CircleProperties circleProperties = new CircleProperties();
    circleProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    circleProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    circleProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    this.circle = lang
        .newCircle(node, radius, "null", null, circleProperties); //Todo: static radius still
    circle.hide();
  }

  /**
   * ID3ChiNode calls analyseData() to compute usefull informations. This constructor is called by
   * ID3.generate() and ID3.expand()
   */
  public ID3ChiNode(ID3ChiNode parent, List<Date> data, List<String> attributes) {
    this.parent = parent;
    this.data = data;
    this.attributes = attributes;
    analyseData();
  }

  /**
   * show shows the circle
   */
  public void show() {
    circle.show();
  }

  /**
   * hide hides the circle/ellipse
   */
  public void hide() {
    if (circle != null) {
      circle.hide();
    }
    if (ellipse != null) {
      ellipse.hide();
    }
    if (text != null) {
      text.hide();
    }
    for (Polyline p : lineList) {
      p.hide();
    }
    for (Text t : lineText) {
      t.hide();
    }
  }

  /**
   * analyseData estimate orderOfClasses, orderOfValues, numberOfValues and numberOfClasses
   */
  private void analyseData() {

    orderOfClasses = new LinkedList<String>();
    orderOfValues = new List[attributes.size()];

    for (int i = 0; i < attributes.size(); i++) {
      orderOfValues[i] = new LinkedList<String>();
    }

    for (Date d : data) {

      boolean labelSeen = false;
      for (int i = 0; i < orderOfClasses.size(); i++) {
        if (d.getLabel().equals(orderOfClasses.get(i))) {
          labelSeen = true;
          break;
        }
      }
      if (!labelSeen) {
        orderOfClasses.add(d.getLabel());
      }

      for (int i = 0; i < attributes.size(); i++) {
        boolean literalSeen = false;
        for (int j = 0; j < orderOfValues[i].size(); j++) {
          if (d.getLiterals().get(i).equals(orderOfValues[i].get(j))) {
            literalSeen = true;
            break;
          }
        }
        if (!literalSeen) {
          orderOfValues[i].add(d.getLiterals().get(i));
        }
      }

    }

    numberOfValues = new int[attributes.size()];
    numberOfClasses = orderOfClasses.size();
    for (int i = 0; i < attributes.size(); i++) {
      numberOfValues[i] = orderOfValues[i].size();
    }
  }

  /**
   * setUpChildren initializes children array needs to be called before children are added
   *
   * @param seenValues number of children, one child per seen value
   */
  public void setUpChildren(int seenValues) {
    children = new ID3ChiNode[seenValues];
  }

  /**
   * setAttribut create an Ellipse instance, hide circle and add text to the node
   */
  public void setAttribut(String text) {
    Font FONT = new Font(Font.SANS_SERIF, Font.BOLD, 24);
    AffineTransform affinetransform = new AffineTransform();
    FontRenderContext frc = new FontRenderContext(affinetransform, true, true);

    int textWidth = (int) (FONT.getStringBounds(text, frc).getWidth());
    textWidth *= 0.55;
    if (textWidth < radius) {
      textWidth = radius;
    }

    TextProperties textProperties = new TextProperties();
    textProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    textProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, FONT);
    textProperties.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
    this.text = this.lang
        .newText(new Offset(0, -10, node, "C"), text, "null", null, textProperties);

    circle.hide();

    EllipseProperties ellipseProperties = new EllipseProperties();
    ellipseProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    ellipseProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    ellipseProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    this.ellipse = lang
        .newEllipse(node, new Coordinates(textWidth, radius), "null", null, ellipseProperties);

    changed = true;
  }

  /**
   * addChild
   *
   * @param node child node
   * @param i index of child node
   */
  public void addChild(ID3ChiNode node, int i) {
    children[i] = node;
  }

  /**
   * drawLineToChildren call drawLine for each child
   *
   * @param attribute index of attribute
   */
  public void drawLineToChildren(int attribute) {
    for (int i = 0; i < children.length; i++) {
      drawLine(i, attribute);
    }
  }

  /**
   * drawLine
   *
   * @param index index of children
   * @param attribute index of attribute
   */
  private void drawLine(int index, int attribute) {
    algoanim.util.Node[] nodes = new algoanim.util.Node[2];
    nodes[0] = node;
    nodes[1] = children[index].getNode();

    PolylineProperties pp = new PolylineProperties();
    pp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
    Polyline pl = lang.newPolyline(nodes, "line", null, pp);
    lineList.add(pl);

    Font FONT = new Font(Font.SANS_SERIF, Font.BOLD, 24);
    TextProperties textProperties = new TextProperties();
    textProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    textProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, FONT);
    textProperties.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
    Text text = this.lang
        .newText(new Offset(0, 0, pl, "C"), orderOfValues[attribute].get(index), "null", null,
            textProperties);
    lineText.add(text);
  }

  /**
   * highlight highlights circle/ellipse
   */
  public void highlight() {
    if (!changed) {
      this.circle.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, color, null, null);
    } else {
      this.ellipse.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, color, null, null);
    }
  }

  /**
   * unhighlights circle/ellipse
   */
  public void unhighlight() {
    if (!changed) {
      this.circle.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE, null, null);
    } else {
      this.ellipse.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE, null, null);
    }
  }

  /**
   * getDistributionOfEachClass
   *
   * @return distribution array
   */
  public double[] getDistributionOfEachClass() {
    double[] distribution = new double[numberOfClasses];
    int count = 0;
    for (Date d : data) {
      for (int i = 0; i < numberOfClasses; i++) {
        if (d.getLabel().equals(orderOfClasses.get(i))) {
          distribution[i] += 1.0;
          break;
        }
      }
    }
    for (int i = 0; i < numberOfClasses; i++) {
      distribution[i] /= data.size();
    }

    return distribution;
  }

  public List<Date> getData() {
    return data;
  }

  public List<String> getAttributes() {
    return attributes;
  }

  public algoanim.util.Node getNode() {
    return node;
  }

  public int getNumberOfClasses() {
    return numberOfClasses;
  }

  public int[] getNumberOfValues() {
    return numberOfValues;
  }

  public List<String>[] getOrderOfValues() {
    return orderOfValues;
  }

  public List<String> getOrderOfClasses() {
    return orderOfClasses;
  }
}
