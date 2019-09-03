package animal.editor.graphics;

import animal.editor.Editor;
import animal.editor.graphics.meta.AbstractMatrixEditor;
import animal.graphics.PTStringMatrix;
import animal.graphics.meta.PTMatrix;
import animal.misc.EditableObject;

/**
 * Editor for String-based matrices
 * 
 * @author Dr. Guido Roessling <roessling@acm.org>
 * @version 2.5 2008-06-23
 */
public class StringMatrixEditor extends AbstractMatrixEditor {

  /**
   * Serial version UID
   */
  private static final long serialVersionUID = 5238390989530348602L;

  @Override
  protected void setDataAt(int row, int column, String text, PTMatrix matrix) {
    matrix.setElementAt(row, column, text);
  }

  @Override
  public String getBasicType() {
    return PTStringMatrix.TYPE_LABEL;
  }

  @Override
  public EditableObject createObject() {
    PTStringMatrix result;
    if (getInt(rowCnt.getText(), 1) > 0)
      result = new PTStringMatrix(getInt(rowCnt.getText(), 1), 1);
    else
      result = new PTStringMatrix(1, 1);
    storeAttributesInto(result);
    this.setChooseIndexContent(result);
    return result;
  }

  @Override
  public Editor getSecondaryEditor(EditableObject e) {
    StringMatrixEditor result = new StringMatrixEditor();
    result.extractAttributesFrom(e);
    return result;
  }

}
