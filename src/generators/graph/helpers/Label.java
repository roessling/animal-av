package generators.graph.helpers;

import java.awt.Font;

import algoanim.primitives.Rect;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;

/**
 * Encapsulates the functionality for label-like outputs in animal.
 * 
 * @author chollubetz
 * 
 */
public class Label {

  private Text text;
  private Rect rect;

  /**
   * Creates a new label.
   * 
   * @param position
   *          the position where the label should be drawn
   * @param height
   *          the height of the label
   * @param width
   *          the width of the label
   * @param lang
   *          the language to which the label should be drawn
   * @param text
   *          the text of the label
   */
  public Label(Position position, int height, int width, Language lang,
      String text) {

    TextProperties tp = new TextProperties();
    tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF,
        Font.ITALIC, 24));

    this.text = lang.newText(
        new Coordinates(position.getX() + 10, position.getY() + 10), text, "",
        null, tp);
    this.rect = lang.newRect(new Coordinates(position.getX(), position.getY()),
        new Coordinates(position.getX() + width, position.getY() + height), "",
        null);
  }

  /**
   * Hides the label.
   */
  public void hide() {
    text.hide();
    rect.hide();
  }
}
