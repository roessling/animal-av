package animal.editor.animators;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JTextField;

import translator.AnimalTranslator;
import animal.animator.IndexedSetText;
import animal.editor.Editor;
import animal.misc.EditableObject;

/**
 * @author Editor for updating an indexed text component 
 */
public class IndexedSetTextEditor extends IndexedTimedAnimatorEditor {

  /**
   * 
   */
  private static final long serialVersionUID = -126046709872853858L;

  private JTextField textField;

  /**
   * 
   */
  public IndexedSetTextEditor() {
    super();
  }

  protected void buildGUI() {
    super.buildGUI();
    addBox(createTextEdit());
    finishBoxes();
  }

  protected Box createTextEdit() {
    Box textBox = new Box(BoxLayout.LINE_AXIS);
    textBox.add(AnimalTranslator.getGUIBuilder().generateJLabel(
        "IndexedSetTextEditor.text"));
    textField = new JTextField(15);
    textField.addActionListener(this);
    textBox.add(textField);
    return textBox;
  }

  /**
   * Open a secondary IndexedSetTextEditor window
   * 
   * @param eo
   *          the object from which to take the current settings
   */
  public Editor getSecondaryEditor(EditableObject eo) {
    IndexedSetTextEditor result = new IndexedSetTextEditor();
    result.extractAttributesFrom(eo);
    return result;
  }

  @Override
  public EditableObject createObject() {
    IndexedSetText returnValue = new IndexedSetText();
    storeAttributesInto(returnValue);
    return returnValue;
  }

  /**
   * Extract the attributes from the chosen object and adjust the corresponding
   * editor values
   * 
   * @param eo
   *          the object to be modified
   */
  public void extractAttributesFrom(EditableObject eo) {
    super.extractAttributesFrom(eo);
    IndexedSetText indexedSetText = (IndexedSetText) eo;
    textField.setText(indexedSetText.getValue());
  }

  /**
   * Set the attributes of the chosen object to the values of the editor
   * 
   * @param eo
   *          the object to be modified
   */
  public void storeAttributesInto(EditableObject eo) {
    super.storeAttributesInto(eo);
    IndexedSetText indexedSetText = (IndexedSetText) eo;
    indexedSetText.setValue(textField.getText());
  }
}
