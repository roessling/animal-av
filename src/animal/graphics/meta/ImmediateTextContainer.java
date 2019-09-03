package animal.graphics.meta;

/**
 * Interface for objects that contain text and a Font
 * 
 * @author Guido Roessling <roessling@acm.org>
 * @version 1.0 2007-12-11
 */
public interface ImmediateTextContainer extends TextContainer {
  /**
   * returns the text container's text
   */
  public String getText();

  /**
   * sets the text container's text
   */
  public void setText(String targetText);
}
