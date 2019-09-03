package animal.exchange.animalascii;

import java.io.IOException;
import java.io.StreamTokenizer;

import animal.graphics.PTTriangle;
import animal.misc.MessageDisplay;
import animal.misc.ParseSupport;

public class PTTriangleImporter extends PTGraphicObjectImporter {
  public Object importFrom(
  int version, StreamTokenizer stok) {
    PTTriangle shape = new PTTriangle();
    try {
      // parse the triangle nodes
      shape.setFirstNode(ParseSupport.parseNode(stok, "Triangle node #1"));
      shape.setSecondNode(ParseSupport.parseNode(stok, "Triangle node #2"));
      shape.setThirdNode(ParseSupport.parseNode(stok, "Triangle node #3"));

      // parse the square color
      shape.setColor(ParseSupport.parseColor(stok, "Triangle color", "color"));

      // parse optional fill attributes
      parseFillAttributes(stok, shape, "Triangle");

      // parse common end attributes
      parseEndingValuesFrom(stok, shape, "Triangle");
    } catch (IOException e) {
      MessageDisplay.errorMsg(e.getMessage(), MessageDisplay.RUN_ERROR);
    }
    return shape;
  }
}
