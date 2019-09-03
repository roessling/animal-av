package animal.editor.animators;

import javax.swing.JPanel;

import animal.animator.TimedShow;
import animal.editor.Editor;
import animal.gui.AnimalMainWindow;
import animal.misc.EditableObject;
import animal.misc.XProperties;

/**
 * The Editor for TimedShow
 * 
 * @see animal.animator.TimedShow
 * 
 * @author <a href="http://www.algoanim.info/Animal2/">Guido R&ouml;&szlig;ling</a>
 * @version 2.0 2001-03-16
 */
public class TimedShowEditor extends TimedAnimatorEditor {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = -9028354094616293137L;

  public TimedShowEditor() {
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

  public void extractAttributesFrom(EditableObject timedShow) {
    super.extractAttributesFrom(timedShow);
  }

  public Editor getSecondaryEditor(EditableObject eo) {
    TimedShowEditor result = new TimedShowEditor();
    result.extractAttributesFrom(eo);
    return result;
  }

  public void storeAttributesInto(EditableObject a) {
    super.storeAttributesInto(a);
    TimedShow ts = (TimedShow) a;
    if (ts.getMethod() != null)
      ts.setShow("show".equalsIgnoreCase(ts.getMethod()));
    else
      ts.setShow(true);
  }

  public EditableObject createObject() {
    TimedShow s = new TimedShow();
    storeAttributesInto(s);
    return s;
  }

  /**
   * checks whether the selected path is a valid TimedShowBase
   */
  protected String isOK() {
    String result = super.isOK();
    if (result != null)
      return result;
    // to make the changes visible and write them back
    AnimalMainWindow.getWindowCoordinator().getDrawWindow(false).setChanged();
    return null;
  }
} // TimedShowEditor
