package animal.editor.animators;

import java.awt.Color;

import javax.swing.JPanel;

import translator.AnimalTranslator;
import animal.animator.ColorChanger;
import animal.editor.Editor;
import animal.misc.ColorChoice;
import animal.misc.EditableObject;
import animal.misc.XProperties;

/**
 * The Editor for the ColorChanger
 * 
 * @see animal.animator.ColorChanger
 * 
 * @author <a href="http://www.algoanim.info/Animal2/">Guido R&ouml;&szlig;ling</a>
 * @version 2.5 2008-06-23
 */
public class ColorChangerEditor extends TimedAnimatorEditor {
  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = -243298461803526770L;

  private ColorChoice color;

  public ColorChangerEditor() {
    super();
  }

  protected void buildGUI() {
    super.buildGUI();
    JPanel c = new JPanel();
    c.add(AnimalTranslator.getGUIBuilder().generateJLabel(
        "ColorChangerEditor.targetColor"));
    c.add(color = new ColorChoice());
    addLayer(c);
    finish();
  }

  public void setProperties(XProperties props) {
    super.setProperties(props);
    color.setColor(props
        .getColorProperty(ColorChanger.COLOR_LABEL, Color.black));
  }

  public void getProperties(XProperties props) {
    super.getProperties(props);
    props.put(ColorChanger.COLOR_LABEL, color.getColor());
  }

  public void extractAttributesFrom(EditableObject colorChanger) {
    super.extractAttributesFrom(colorChanger);
    color.setColor(((ColorChanger) colorChanger).getColor());
   }

  public void storeAttributesInto(EditableObject colorChanger) {
    super.storeAttributesInto(colorChanger);
    ((ColorChanger) colorChanger).setColor(color.getColor());
  }

  public Editor getSecondaryEditor(EditableObject eo) {
    ColorChangerEditor result = new ColorChangerEditor();
    result.extractAttributesFrom(eo);
    return result;
  }

  public EditableObject createObject() {
    ColorChanger c = new ColorChanger();
    storeAttributesInto(c);
    return c;
  }
} // ColorChangerEditor
