package animal.editor.graphics;

import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeListener;

import javax.swing.event.ChangeListener;

import animal.editor.Editor;
import animal.editor.graphics.meta.AbstractMatrixEditor;
import animal.graphics.PTIntMatrix;
import animal.graphics.meta.PTMatrix;
import animal.misc.EditableObject;

/**
 * The Editor for a PTIntMatrix
 */
public class IntMatrixEditor extends AbstractMatrixEditor implements ActionListener,
    ChangeListener, ItemListener, PropertyChangeListener {
  /**
   * 
   */
  private static final long serialVersionUID = 4992995362249172854L;

  public EditableObject createObject() {
    PTIntMatrix result;
    if (getInt(rowCnt.getText(), 1) > 0)
      result = new PTIntMatrix(getInt(rowCnt.getText(), 1), 1);
    else
      result = new PTIntMatrix(1, 1);
    storeAttributesInto(result);
    this.setChooseIndexContent(result);
    return result;
  }

  public Editor getSecondaryEditor(EditableObject e) {
    IntMatrixEditor result = new IntMatrixEditor();
    result.extractAttributesFrom(e);
    return result;
  }

  public String getBasicType() {
    return PTIntMatrix.TYPE_LABEL;
  }

  @Override
  protected void setDataAt(int row, int column, String text, PTMatrix matrix) {
    if (matrix instanceof PTIntMatrix) {
      PTIntMatrix m = (PTIntMatrix) matrix;
      m.setDataAt(row, column, getInt(text, m.getDataAt(row, column)));
    }
  }

}
