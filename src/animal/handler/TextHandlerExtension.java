/**
 * 
 */
package animal.handler;

import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.util.Vector;

import animal.graphics.PTGraphicObject;
import animal.graphics.PTText;

/**
 * Extends TextHandler by methods for TextObject manipulation (see Animators
 * <code>setText</code> and <code>setFont</code>)
 * 
 * @author <a href="mailto:here@christoph-preisser.de">Christoph Prei&szlig;er</a>
 * @version 0.96 2006-11-30
 */
public class TextHandlerExtension extends GraphicObjectHandlerExtension {

  static final String SET_TEXT = "setText";

  static final String SET_FONT = "setFont";

  public TextHandlerExtension() {
    type = PTText.TEXT_TYPE;
  }

  public Vector<String> getMethods(
  PTGraphicObject ptgo, Object o) {
    Vector<String> methods = new Vector<String>();
    if (o instanceof String)
      methods.addElement(SET_TEXT);
    if (o instanceof Font)
      methods.addElement(SET_FONT);
    return methods;
  }

  public void propertyChange(PTGraphicObject ptgo, PropertyChangeEvent e) {

    if (ptgo instanceof PTText) {
      PTText textObject = (PTText) ptgo;
      String cmd = e.getPropertyName();
      if (cmd.equalsIgnoreCase(SET_TEXT))
        textObject.setText((String) e.getNewValue());
      if (cmd.equalsIgnoreCase(SET_FONT))
        textObject.setFont((Font) e.getNewValue());

    }
    // else sth really gone wrong!
  }
}
