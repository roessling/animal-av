package animal.editor.animators;

import javax.swing.JPanel;

import translator.AnimalTranslator;
import animal.animator.DepthChanger;
import animal.editor.Editor;
import animal.misc.EditableObject;
import animal.misc.XProperties;

/**
 * The Editor for the DepthChanger
 * 
 * @see animal.animator.DepthChanger
 * 
 * @author <a href="http://www.algoanim.info/Animal2/">Guido R&ouml;&szlig;ling</a>
 * @version 2.5 2008-06-23
 */
public class DepthChangerEditor extends TimedAnimatorEditor {
  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = -9029598958797873851L;

  public DepthChangerEditor() {
    super();
    // JPanel c = new JPanel();
    // c.add(AnimalTranslator.getGUIBuilder().generateJLabel("targetDepth"));
    // addLayer(c);
    // finish();
  }

  protected void buildGUI() {
    super.buildGUI();
    JPanel c = new JPanel();
    c.add(AnimalTranslator.getGUIBuilder().generateJLabel(
        "DepthChangerEditor.targetDepth"));
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

  public void storeAttributesInto(EditableObject depthChanger) {
    super.storeAttributesInto(depthChanger);
  }

  public Editor getSecondaryEditor(EditableObject eo) {
    DepthChangerEditor result = new DepthChangerEditor();
    result.extractAttributesFrom(eo);
    return result;
  }

  public EditableObject createObject() {
    DepthChanger c = new DepthChanger();
    storeAttributesInto(c);
    return c;
  }
} // DepthChangerEditor
