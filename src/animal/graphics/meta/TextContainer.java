package animal.graphics.meta;

import java.awt.Font;

/**
 * Interface for objects that contain text and a Font
 * 
 * @author Guido Roessling <roessling@acm.org>
 * @version 1.0 2007-12-11
 */
public interface TextContainer {
  /**
   * returns the text container's font
   */
  public Font getFont();

  /**
   * sets the text container's font
   */
  public void setFont(Font targetFont);
}
