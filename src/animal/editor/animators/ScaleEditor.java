package animal.editor.animators;

import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.JTextField;

import translator.AnimalTranslator;
import translator.TranslatableGUIElement;
import animal.animator.Scale;
import animal.editor.Editor;
import animal.graphics.PTPoint;
import animal.misc.EditableObject;
import animal.misc.ObjectSelectionButton;
import animal.misc.XProperties;

/**
 * Editor for Scale
 * 
 * @see animal.animator.Scale
 * 
 * @author <a href="http://www.algoanim.info/Animal2/">Guido R&ouml;&szlig;ling</a>
 * @version 1.0 2001-03-16
 */
public class ScaleEditor extends TimedAnimatorEditor {
  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = -7598478572744872282L;

  private ObjectSelectionButton center;

  private JTextField xScaleFactorTF;

  private JTextField yScaleFactorTF;

  public ScaleEditor() {
    super();
  }

  protected void buildGUI() {
    super.buildGUI();
    TranslatableGUIElement generator = AnimalTranslator.getGUIBuilder();
    JPanel c = new JPanel(new FlowLayout(FlowLayout.LEFT));
    c.add(generator.generateJLabel("ScaleEditor.scaleCenterLabel"));
    c.add(center = new ObjectSelectionButton(false));
    c.add(generator.generateJLabel("ScaleEditor.scaleFactorXLabel"));
    c.add(xScaleFactorTF = new JTextField(5));
    c.add(generator.generateJLabel("ScaleEditor.scaleFactorYLabel"));
    c.add(yScaleFactorTF = new JTextField(5));

    addLayer(c);
    finish();
  }

  public void setProperties(
  XProperties props) {
    // do nothing
  }

  public void extractAttributesFrom(EditableObject eo) {
    super.extractAttributesFrom(eo);
    Scale scale = (Scale) eo;
    center.setObjectNum(scale.getCenterNum());
    // setInt(scaleFactorTF, scale.getDegrees());
    xScaleFactorTF.setText(String.valueOf(scale.getXScaleFactor()));
    yScaleFactorTF.setText(String.valueOf(scale.getYScaleFactor()));
  }

  public void storeAttributesInto(EditableObject scale) {
    super.storeAttributesInto(scale);
    if (scale instanceof Scale) {
      Scale scaler = (Scale) scale;
      scaler.setCenterNum(center.getObjectNum());
      scaler.setXScaleFactor(getDouble(xScaleFactorTF.getText(), 1.0));
      scaler.setYScaleFactor(getDouble(yScaleFactorTF.getText(), 1.0));
    }
  }

  public Editor getSecondaryEditor(EditableObject eo) {
    ScaleEditor result = new ScaleEditor();
    result.extractAttributesFrom(eo);
    return result;
  }

  public EditableObject createObject() {
    Scale r = new Scale();
    storeAttributesInto(r);
    return r;
  }

  /**
   * Scale is valid if the center is a PTPoint.
   */
  protected String isOK() {
    String result = super.isOK();
    if (result != null)
      return result;
    center.checkObjects();
    int num = center.getObjectNum();
    if (num == 0)
      return AnimalTranslator.translateMessage("ScaleEditor.scaleNoCenter");
    if (!(getGraphicObject(num) instanceof PTPoint))
      return AnimalTranslator.translateMessage("ScaleEditor.scaleCenterNotPoint");
    return null;
  }
} // ScaleEditor
