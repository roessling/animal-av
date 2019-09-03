package animal.editor.animators;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import translator.AnimalTranslator;
import translator.TranslatableGUIElement;
import animal.animator.Show;
import animal.editor.Editor;
import animal.misc.EditableObject;
import animal.misc.XProperties;

/**
 * Editor for Show.
 * 
 * @see animal.animator.Show
 * 
 * @author <a href="http://www.algoanim.info/Animal2/">Guido R&ouml;&szlig;ling</a>
 * @version 2.0 2001-03-16
 */
public class ShowEditor extends AnimatorEditor {
  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = -8972332883991718878L;

  private JToggleButton showCB;

  private JToggleButton hideCB;

  public ShowEditor() {
    super();
  }

  protected void buildGUI() {
    JPanel p = new JPanel();
    TranslatableGUIElement generator = AnimalTranslator.getGUIBuilder();
    p.add(showCB = generator.generateJToggleButton("ShowEditor.show", null, this, true));
    showCB.setSelected(true);
//    showCB.setMnemonic(KeyEvent.VK_S);
    p.add(hideCB = generator.generateJToggleButton("ShowEditor.hide", null, this, true));
//    hideCB.setMnemonic(KeyEvent.VK_H);
    hideCB.setSelected(false);
    ButtonGroup g = new ButtonGroup();
    g.add(showCB);
    g.add(hideCB);
    addLayer(p);
    finish();
  }

  public void setProperties(
  XProperties props) {
    // do nothing
  }

  public void extractAttributesFrom(EditableObject eo) {
    super.extractAttributesFrom(eo);
    setShow(((Show) eo).isShow());
  }

  public void storeAttributesInto(EditableObject show) {
    super.storeAttributesInto(show);
    ((Show) show).setShow(getShow());
  }

  public Editor getSecondaryEditor(EditableObject eo) {
    ShowEditor result = new ShowEditor();
    result.extractAttributesFrom(eo);
    return result;
  }

  void setShow(boolean b) {
    hideCB.setSelected(!b);
    showCB.setSelected(b);
  }

  boolean getShow() {
    return showCB.isSelected();
  }

  public EditableObject createObject() {
    Show s = new Show();
    storeAttributesInto(s);
    return s;
  }
}
