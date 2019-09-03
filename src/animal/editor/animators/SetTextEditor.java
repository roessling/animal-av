/**
 * Editor for SetText
 * @see animal.main.animator.SetText
 *
 * Created on 16. December 2006, 0:05
 *
 * @author MichaelSchmitt
 * @version 0.2.1a
 * @date 2006-02-15
 */

package animal.editor.animators;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import translator.AnimalTranslator;
import animal.animator.SetText;
import animal.editor.Editor;
import animal.misc.EditableObject;

public class SetTextEditor extends TimedAnimatorEditor {

  /**
   * 
   */
  private static final long serialVersionUID = -2832505946091287650L;

  /**
   * Make the highlight panel scrollable if it contains too many elements.
   */
  private JScrollPane scp;

  private JTextField textField;

  /**
   * Create a new SwapEditor
   */
  public SetTextEditor() {
    super();
  }

  protected void buildGUI() {
    super.buildGUI();
    GridBagLayout gbl = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();
    JPanel textPanel = new JPanel(gbl);

    gbc.anchor = GridBagConstraints.WEST;
    textPanel.add(AnimalTranslator.getGUIBuilder().generateJLabel("SetTextEditor.text"), gbc);
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    textField = new JTextField(15);
    textField.addActionListener(this);

    textPanel.add(textField, gbc);
    scp = new JScrollPane(textPanel);
    addLayer(scp);
    finish();
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
    SetText setText = (SetText) eo;
    textField.setText(setText.getValue());
  }

  /**
   * Set the attributes of the chosen object to the values of the editor
   * 
   * @param eo
   *          the object to be modified
   */
  public void storeAttributesInto(EditableObject eo) {
    super.storeAttributesInto(eo);
    SetText setText = (SetText) eo;
    setText.setValue(textField.getText());
  }

  /**
   * Open a secondary SwapEditor window
   * 
   * @param eo
   *          the object from which to take the current settings
   */
  public Editor getSecondaryEditor(EditableObject eo) {
    SetTextEditor result = new SetTextEditor();
    result.extractAttributesFrom(eo);
    return result;
  }

  /**
   * Create a new swap animation with the current settings
   */
  public EditableObject createObject() {
    SetText h = new SetText();
    storeAttributesInto(h);
    return h;
  }
}