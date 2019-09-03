package animal.editor.properties;

import javax.swing.JPanel;

import animal.animator.PropertyChanger;
import animal.editor.Editor;
import animal.editor.animators.TimedAnimatorEditor;
import animal.misc.EditableObject;
import animal.misc.XProperties;

/**
 * The Editor for the PropertyChanger
 * @see animal.animator.PropertyChanger
 *
 * @author <a href="http://www.algoanim.info/Animal2/">Guido R&ouml;&szlig;ling</a>
 * @version 2.0 2001-03-16
 */
public class PropertyChangerEditor extends TimedAnimatorEditor
{
  /**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -969071277151214870L;


public PropertyChangerEditor()
  {
    super();
    JPanel c = new JPanel();
    addLayer(c);
    finish();
  }


  public void setProperties(XProperties props)
  {
    super.setProperties(props);
  }


  public void getProperties(XProperties props)
  {
    super.getProperties(props);
  }


  public void extractAttributesFrom(EditableObject colorChanger)
  {
    super.extractAttributesFrom(colorChanger);
  }


  public void storeAttributesInto(EditableObject colorChanger)
  {
    super.storeAttributesInto(colorChanger);
  }


  public Editor getSecondaryEditor(EditableObject eo)
  {
    PropertyChangerEditor result = new PropertyChangerEditor();
    result.extractAttributesFrom(eo);
    return result;
  }


  public EditableObject createObject()
  {
    PropertyChanger c = new PropertyChanger();
    storeAttributesInto(c);
    return c;
  }
} // PropertyChangerEditor

