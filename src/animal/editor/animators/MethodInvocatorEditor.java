package animal.editor.animators;

import javax.swing.JPanel;

import animal.animator.MethodInvocator;
import animal.editor.Editor;
import animal.misc.EditableObject;
import animal.misc.XProperties;

/**
 * The Editor for the MethodInvocator
 * 
 * @see animal.animator.MethodInvocator
 * 
 * @author <a href="http://www.algoanim.info/Animal2/">Guido R&ouml;&szlig;ling</a>
 * @version 2.5 2008-06-23
 */
public class MethodInvocatorEditor extends TimedAnimatorEditor {
  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = -2801983655565599248L;

  /**
   * JComboBox: constructor, method; JTextField for methodName
   */
  public MethodInvocatorEditor() {
    super();
  }

  protected void buildGUI() {
    super.buildGUI();
    JPanel c = new JPanel();
    addLayer(c);
    finish();
  }

  public void setProperties(XProperties props) {
    super.setProperties(props);
  }

  public void getProperties(XProperties props) {
    super.getProperties(props);
  }

  public void extractAttributesFrom(EditableObject colorChanger) {
    super.extractAttributesFrom(colorChanger);
  }

  public void storeAttributesInto(EditableObject colorChanger) {
    super.storeAttributesInto(colorChanger);
  }

  public Editor getSecondaryEditor(EditableObject eo) {
    MethodInvocatorEditor result = new MethodInvocatorEditor();
    result.extractAttributesFrom(eo);
    return result;
  }

  public EditableObject createObject() {
    MethodInvocator c = new MethodInvocator();
    storeAttributesInto(c);
    return c;
  }
} // MethodInvocatorEditor

