package animal.editor.graphics;

import animal.editor.Editor;
import animal.editor.graphics.meta.AbstractMatrixEditor;
import animal.graphics.PTDoubleMatrix;
import animal.graphics.meta.PTMatrix;
import animal.misc.EditableObject;

/**
 * concrete editor for a double-based matrix
 * 
 * @author Dr. Guido Roessling <roessling@acm.org>
 * @version 2.5 2008-06-23
 */
public class DoubleMatrixEditor extends AbstractMatrixEditor {

  /**
   * 
   */
  private static final long serialVersionUID = -907172258898710073L;

  @Override
  protected void setDataAt(int row, int column, String text, PTMatrix matrix) {
    if (matrix instanceof PTDoubleMatrix) {
      PTDoubleMatrix m = (PTDoubleMatrix) matrix;
      m.setDataAt(row, column, getDouble(text, m.getDataAt(row, column)));
    }
  }

  @Override
  public String getBasicType() {
    return PTDoubleMatrix.TYPE_LABEL;
  }

  @Override
  public EditableObject createObject() {
    PTDoubleMatrix result;
    if (getInt(rowCnt.getText(), 1) > 0)
      result = new PTDoubleMatrix(getInt(rowCnt.getText(), 1), 1);
    else
      result = new PTDoubleMatrix(1, 1);
    storeAttributesInto(result);
    this.setChooseIndexContent(result);
    return result;
  }

  @Override
  public Editor getSecondaryEditor(EditableObject go) {
    DoubleMatrixEditor result = new DoubleMatrixEditor();
    result.extractAttributesFrom(go);
    return result;
  }

}
