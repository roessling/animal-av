/**
 * The Editor for a PTStringArray.
 * @see animal.main.graphics.PTStringArray
 *
 * @author <a href="mschmitt@rbg.informatik.tu-darmstadt.de"> Michael Schmitt
 * @version 1.6.3a
 * @date 2006-01-17
 */

package animal.editor.graphics;

import java.awt.event.KeyEvent;

import animal.editor.graphics.meta.AbstractArrayEditor;
import animal.graphics.PTStringArray;
import animal.misc.EditableObject;

/**
 * Editor for a String-based array
 * 
 * @author Dr. Guido Roessling <roessling@acm.org>
 * @version 2.5 2008-06-23
 */
public class StringArrayEditor extends AbstractArrayEditor {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 2295451210977833754L;

  /**
   * Construct a new StringArrayEditor window
   */
  public StringArrayEditor() {
    super(PTStringArray.STRING_ARRAY_TYPE);
  }

  /**
   * Constructor used by the secondary editor window. This one is (and must be)
   * different from the default constructor, because by definition the array
   * size is static, so the according tab is missing in this version of the
   * StringArrayEditor window.
   * 
   * @param i
   *          the size of the StringArray used to determine the correct indices
   *          of the array cells
   */
  public StringArrayEditor(int i) {
    super(PTStringArray.STRING_ARRAY_TYPE, i);
  }

  protected void buildGUI() {
    buildGUI(PTStringArray.STRING_ARRAY_TYPE);
  }

  /**
   * Finally create a new StringArray by this editor.
   * 
   * @return the created <code>PTStringArray</code>
   */
  public EditableObject createObject() {
    PTStringArray result = new PTStringArray(getInt(arraySize.getText(), 1));
    storeAttributesInto(result);
    return result;
  }
  
  public void keyTyped(KeyEvent e) {
    PTStringArray stringArray = (PTStringArray) getCurrentObject();
    if (e.getSource() == content) {
      stringArray.enterValue(calcIndex(), content.getText());
    }
    repaintNow();
  }

  public String getBasicType() {
    return PTStringArray.STRING_ARRAY_TYPE;
  }
}