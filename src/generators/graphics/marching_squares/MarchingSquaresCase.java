package generators.graphics.marching_squares;

import algoanim.primitives.Rect;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.util.Node;
import algoanim.util.Offset;
import java.awt.Color;

public class MarchingSquaresCase {
  public enum CaseType {
    NoContour,
    SingleSegment,
    TwoSegmentSaddle
  }

  String binary;
  int margin;
  MarchingSquaresPixel northWest;
  MarchingSquaresPixel northEast;
  MarchingSquaresPixel southWest;
  MarchingSquaresPixel southEast;
  Language lang;
  Node node;
  Rect highlightRect;
  CaseType caseType;

  public MarchingSquaresCase(Language lang, Node node, String binary, CaseType caseType, int margin) {
    this.lang = lang;
    this.node = node;
    this.binary = binary;
    this.caseType = caseType;
    this.margin = margin;
    this.setUp();
  }

  private void setUp() {
    char[] chars = binary.toCharArray();

    boolean filled = chars[0] == '1';
    northWest = new MarchingSquaresPixel(lang, node, filled);
    filled = chars[1] == '1';
    northEast = new MarchingSquaresPixel(lang, new Offset(margin, 0, node,"C"), filled);
    filled = chars[2] == '1';
    southEast = new MarchingSquaresPixel(lang, new Offset(margin, margin, node, "C"), filled);
    filled = chars[3] == '1';
    southWest = new MarchingSquaresPixel(lang, new Offset(0, margin, node, "C"), filled);


    int radius = northEast.getRadius();
    RectProperties rectProperties = new RectProperties();
    rectProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 4);
    rectProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(203,250,100));
    highlightRect = lang.newRect(new Offset(-radius,-radius,northWest.getNode(),"C"), new Offset(margin+radius,margin+radius,northWest.getNode(),"C"), "null", null, rectProperties);
    highlightRect.hide();
  }

  public void highlightCase() {
    highlightRect.show();
  }
  public void unhighlightCase() {
    highlightRect.hide();
  }

  public CaseType getCaseType() {
    return caseType;
  }

  public int getMargin() {
    return margin;
  }

  public MarchingSquaresPixel getNorthWest() {
    return northWest;
  }

  public MarchingSquaresPixel getNorthEast() {
    return northEast;
  }

  public MarchingSquaresPixel getSouthWest() {
    return southWest;
  }

  public MarchingSquaresPixel getSouthEast() {
    return southEast;
  }
}
