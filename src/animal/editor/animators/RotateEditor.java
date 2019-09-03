package animal.editor.animators;

import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.JTextField;

import translator.AnimalTranslator;
import animal.animator.Rotate;
import animal.editor.Editor;
import animal.graphics.PTPoint;
import animal.misc.EditableObject;
import animal.misc.ObjectSelectionButton;
import animal.misc.XProperties;

/**
 * Editor for Rotate
 * 
 * @see animal.animator.Rotate
 * 
 * @author <a href="http://www.algoanim.info/Animal2/">Guido R&ouml;&szlig;ling</a>
 * @version 2.5 2008-06-23
 */
public class RotateEditor extends TimedAnimatorEditor {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = -3355178488198635871L;

  private ObjectSelectionButton center;

  private JTextField degreesTF;

  public RotateEditor() {
    super();
  }

  protected void buildGUI() {
    super.buildGUI();
    JPanel c = new JPanel(new FlowLayout(FlowLayout.LEFT));
    c.add(AnimalTranslator.getGUIBuilder().generateJLabel(
        "RotateEditor.rotationCenterLabel"));
    c.add(center = new ObjectSelectionButton(false));
    c.add(AnimalTranslator.getGUIBuilder().generateJLabel(
        "RotateEditor.rotationDegreesLabel"));
    c.add(degreesTF = new JTextField(5));

    addLayer(c);
    finish();
  }

  public void setProperties(
  XProperties props) {
    // do nothing
  }

  public void extractAttributesFrom(EditableObject eo) {
    super.extractAttributesFrom(eo);
    Rotate rotate = (Rotate) eo;
    center.setObjectNum(rotate.getCenterNum());
    setInt(degreesTF, rotate.getDegrees());
  }

  public void storeAttributesInto(EditableObject rotate) {
    super.storeAttributesInto(rotate);
    ((Rotate) rotate).setCenterNum(center.getObjectNum());
    ((Rotate) rotate).setDegrees(getInt(degreesTF.getText(), 0));
  }

  public Editor getSecondaryEditor(EditableObject eo) {
    RotateEditor result = new RotateEditor();
    result.extractAttributesFrom(eo);
    return result;
  }

  public EditableObject createObject() {
    Rotate r = new Rotate();
    storeAttributesInto(r);
    return r;
  }

  /**
   * Rotate is valid if the center is a PTPoint.
   * 
   * @return a String indicating if everything was OK (null) or not (an error
   *         String)
   */
  protected String isOK() {
    String result = super.isOK();
    if (result != null)
      return result;
    center.checkObjects();
    int num = center.getObjectNum();
    if (num == 0)
      return AnimalTranslator.translateMessage("RotateEditor.rotationNoCenter");
    if (!(getGraphicObject(num) instanceof PTPoint))
      return AnimalTranslator
          .translateMessage("RotateEditor.rotationCenterNotPoint");
    return null;
  }
} // RotateEditor
