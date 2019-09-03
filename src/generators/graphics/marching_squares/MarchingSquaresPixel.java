package generators.graphics.marching_squares;

import algoanim.primitives.Circle;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Node;
import algoanim.util.Offset;
import java.awt.Color;
import java.awt.Font;

public class MarchingSquaresPixel {

  public MarchingSquaresPixel(Language lang, Node node, int value, int threshold, int radius) {
    this.lang = lang;
    this.node = node;
    this.value = value;
    this.threshold = threshold;
    this.radius = radius;

    this.setUp();
  }

  // cases
  public MarchingSquaresPixel(Language lang, Node node, boolean filled) {
    this.lang = lang;
    this.node = node;

    radius = 10;
    threshold = 1;
    value = filled ? 1 : 0;
    backgroundColor = filled ? IN_BG_COLOR : OUT_BG_COLOR;

    CircleProperties circleProperties = new CircleProperties();
    circleProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    circleProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, backgroundColor);
    circleProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    this.circle = this.lang.newCircle(this.node, radius, "null", null, circleProperties);
  }

  private Language lang;
  private Node node;
  private int value;
  private int threshold;
  private Text text;
  private Circle circle;

  private Color textColor;
  private Color backgroundColor;
  private Color bgHighlightColor;

  private int radius;
  private static final Color OUT_TEXT_COLOR = Color.WHITE;
  private static final Color OUT_BG_COLOR = new Color(35, 43, 43);
  private static Color OUT_BG_HIGHLIGHT_COLOR = new Color(114, 140, 56);
  private static final Color IN_TEXT_COLOR = Color.BLACK;
  private static final Color IN_BG_COLOR = Color.WHITE;
  private static Color IN_BG_HIGHLIGHT_COLOR = new Color(203, 250, 100);
  private static final Font FONT = new Font(Font.SANS_SERIF, Font.BOLD, 24);
  // text
  // circle

  void setUp() {

    if (value < threshold) {
      // not in iso area
      textColor = OUT_TEXT_COLOR;
      backgroundColor = OUT_BG_COLOR;
      bgHighlightColor = OUT_BG_HIGHLIGHT_COLOR;
    } else {
      // in iso area
      textColor = IN_TEXT_COLOR;
      backgroundColor = IN_BG_COLOR;
      bgHighlightColor = IN_BG_HIGHLIGHT_COLOR;
    }

    // Create Text
    TextProperties textProperties = new TextProperties();
    textProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, textColor);
    textProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, FONT);
    textProperties.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
    this.text = this.lang.newText(node, value + "", "null", null, textProperties);

    // Create Circle
    CircleProperties circleProperties = new CircleProperties();
    circleProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    circleProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, backgroundColor);
    circleProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    this.circle = this.lang
        .newCircle(new Offset(0, 0, text, "C"), radius, "null", null, circleProperties);
  }

  public void highlight() {
    this.circle
        .changeColor(AnimationPropertiesKeys.FILL_PROPERTY, this.bgHighlightColor, null, null);
  }

  public void unHighlight() {
    this.circle
        .changeColor(AnimationPropertiesKeys.FILL_PROPERTY, this.backgroundColor, null, null);
  }

  public boolean isFilled() {
    return (this.value >= threshold);
  }

  public Node getNode() {
    return node;
  }

  public void multValue(int factor) {
    this.value *= factor;
  }

  public void setValue(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public int getThreshold() {
    return threshold;
  }

  public Text getText() {
    return text;
  }

  public Circle getCircle() {
    return circle;
  }

  public int getRadius() {
    return radius;
  }

  public static Color getOutBgHighlightColor() {
    return OUT_BG_HIGHLIGHT_COLOR;
  }

  public static Color getInBgHighlightColor() {
    return IN_BG_HIGHLIGHT_COLOR;
  }

  public static void setOutBgHighlightColor(Color outBgHighlightColor) {
    OUT_BG_HIGHLIGHT_COLOR = outBgHighlightColor;
  }

  public static void setInBgHighlightColor(Color inBgHighlightColor) {
    IN_BG_HIGHLIGHT_COLOR = inBgHighlightColor;
  }
}
