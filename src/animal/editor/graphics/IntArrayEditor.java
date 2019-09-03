/*
 * The Editor for a PTIntArray.
 * @see animal.graphics.PTIntArray
 *
 * @author <a href="mschmitt@rbg.informatik.tu-darmstadt.de"> Michael Schmitt </a>,
 * <a href="mailto:roessling@acm.org">Dr. Guido R&ouml;&szlig;ling</a>
 * @version 1.4
 * @date 2006-06-16
 */
package animal.editor.graphics;

import java.awt.event.KeyEvent;

import animal.editor.graphics.meta.AbstractArrayEditor;
import animal.graphics.PTIntArray;
import animal.misc.EditableObject;

/**
 * Editor for int-based arrays
 * 
 * @author Dr. Guido Roessling <roessling@acm.org>
 * @version 2.5 2008-06-23
 */
public class IntArrayEditor extends AbstractArrayEditor {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 2295451210277833754L;

  /**
   * Construct a new IntArrayEditor window
   */
  public IntArrayEditor() {
    super(PTIntArray.INT_ARRAY_TYPE);
  }

  /**
   * Constructor used by the secondary editor window. This one is (and must be)
   * different from the default constructor, because by definition the array
   * size is static, so the according tab is missing in this version of the
   * IntArrayEditor window.
   * 
   * @param i
   *          the size of the IntArray used to determine the correct indices of
   *          the array cells
   */
  public IntArrayEditor(int i) {
    super(PTIntArray.INT_ARRAY_TYPE, i);
  }

  protected void buildGUI() {
    buildGUI(PTIntArray.INT_ARRAY_TYPE);
  }

  /**
   * Finally create a new IntArray by this editor.
   * 
   * @return the created <code>PTIntArray</code>
   */
  public EditableObject createObject() {
    PTIntArray result = new PTIntArray(getInt(arraySize.getText(), 1));
    storeAttributesInto(result);
    return result;
  }

  public void keyTyped(KeyEvent e) {
    PTIntArray intArray = (PTIntArray) getCurrentObject();
    String currentString = content.getText();
    int value = -1, insertPos = content.getCaretPosition();
    int currentVal = getInt(currentString, 0);
    char insertedChar = e.getKeyChar();
    if (e.getSource() == content) {
      boolean isOK = Character.isDigit(insertedChar);
      // accept a "-" if at first position and value was at least 0
      if (!isOK && insertedChar == '-' && insertPos == 0 && currentVal >= 0) {
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
        value = getInt(sb.toString(), 1);
        intArray.enterValue(calcIndex(), value);
      }
    }
    repaintNow();
  }

  public String getBasicType() {
    return PTIntArray.INT_ARRAY_TYPE;
  }

} // IntArrayEditor