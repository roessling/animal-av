/*
 * The Editor for a PTDoubleArray.
 * @see animal.graphics.PTDoubleArray
 *
 * @author <a href="mailto:roessling@acm.org">Dr. Guido R&ouml;&szlig;ling</a>
 * @version 1.4
 * @date 2008-03-07
 */
package animal.editor.graphics;

import java.awt.event.KeyEvent;

import animal.editor.graphics.meta.AbstractArrayEditor;
import animal.graphics.PTDoubleArray;
import animal.misc.EditableObject;

/**
 * Editor for a double-based array
 * 
 * @author Dr. Guido Roessling <roessling@acm.org>
 * @version 2.5 2008-06-23
 */
public class DoubleArrayEditor extends AbstractArrayEditor {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 2295451210221833754L;

  /**
   * Construct a new DoubleArrayEditor window
   */
  public DoubleArrayEditor() {
    super(PTDoubleArray.DOUBLE_ARRAY_TYPE);
  }

  /**
   * Constructor used by the secondary editor window. This one is (and must be)
   * different from the default constructor, because by definition the array
   * size is static, so the according tab is missing in this version of the
   * DoubleArrayEditor window.
   * 
   * @param i
   *          the size of the DoubleArray used to determine the correct indices of
   *          the array cells
   */
  public DoubleArrayEditor(int i) {
    super(PTDoubleArray.DOUBLE_ARRAY_TYPE, i);
  }

  protected void buildGUI() {
    buildGUI(PTDoubleArray.DOUBLE_ARRAY_TYPE);
  }

  /**
   * Finally create a new DoubleArray by this editor.
   * 
   * @return the created <code>PTDoubleArray</code>
   */
  public EditableObject createObject() {
    PTDoubleArray result = new PTDoubleArray(getInt(arraySize.getText(), 1));
//    System.err.println("target size:" +result.getSize());
    storeAttributesInto(result);
    return result;
  }

  public void keyTyped(KeyEvent e) {
    PTDoubleArray array = (PTDoubleArray) getCurrentObject();
    String currentString = content.getText();
    double value = -1;
    int insertPos = content.getCaretPosition();
    double currentVal = getDouble(currentString, 0);
    char insertedChar = e.getKeyChar();
    if (e.getSource() == content) {
      boolean isOK = Character.isDigit(insertedChar);
      // accept a "-" if at first position and value was at least 0
      if (!isOK && insertedChar == '-' && insertPos == 0 && currentVal >= 0) {
        isOK = true;
      } else if (insertedChar == '.' && currentString.indexOf('.') == -1) {
        // accept a "." only if this is the first occurrence
        isOK = true;
      }
      if (!isOK) {
        e.consume();
        System.err.println("consumed as illegal: " +insertedChar);
      } else {
        StringBuffer sb = new StringBuffer(currentString.length()+1);
        if (insertPos > 0)
          sb.append(currentString.substring(0, insertPos));
        sb.append(insertedChar);
        if (insertPos < currentString.length())
          sb.append(currentString.substring(insertPos));
        value = getDouble(sb.toString(), -1);
        array.enterValue(calcIndex(), value);
      }
    }
    repaintNow();

//    
//    if (e.getSource() == content) {
//      // this is tricky!!! :-)
//      // ---
//      // the opposite of the condition is valid, so negate everything
//      if (!
//      // check if String isn't a negative number when entering '-' at first
//      // position
//      (((e.getKeyChar() == '-') && (content.getCaretPosition() == 0) && (getInt(
//          content.getText(), 0) >= 0)) ||
//      // check for decimal point - must be only one!
//      (e.getKeyChar() == '.' && content.getText().indexOf('.') == -1) ||
//      // check for digit pressed and...
//      (Character.isDigit(e.getKeyChar()) &&
//      // ... not at position 0 or String doesn't contain '-'
//      ((content.getCaretPosition() > 0) || (getInt(content.getText(), 0) >= 0))))) {
//        e.consume();
//      }
//      array.enterValue(calcIndex(), getInt(content.getText(), calcIndex()));
//    }
//    repaintNow();
  }

  public String getBasicType() {
    return PTDoubleArray.DOUBLE_ARRAY_TYPE;
  }

} // DoubleArrayEditor