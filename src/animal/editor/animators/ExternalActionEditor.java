package animal.editor.animators;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import animal.animator.ExternalAction;
import animal.editor.Editor;
import animal.misc.EditableObject;
import animal.misc.XProperties;

/**
 * Editor for ExternalAction.
 * @see animal.animator.ExternalAction
 *
 * @author <a href="http://www.algoanim.info/Animal2/">Guido R&ouml;&szlig;ling</a>
 * @version 2.0 2001-03-16
 */
public class ExternalActionEditor extends AnimatorEditor
{
  /**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -143709106850164008L;
//private JRadioButton showCB;
//  private JRadioButton hideCB;
  private JComboBox<String> comboBox;
  private JTextArea textArea;

  public ExternalActionEditor()
  {
    super();
  }
 
  protected void buildGUI() {
    JPanel p = new JPanel();
    comboBox = new JComboBox<String>();
    fillComboBox();
    p.add(comboBox);
    textArea = new JTextArea(7, 30);
    p.add(textArea);
    addLayer(p);
    finish();
  }

  public void fillComboBox()
  {
    if (comboBox == null)
      comboBox = new JComboBox<String>();
    else
      comboBox.removeAllItems();
    comboBox.addItem("Documentation");
    comboBox.addItem("Question");
  }

  public void setProperties( XProperties props) {
		// do nothing; only used for serialization
  }

  public void extractAttributesFrom(EditableObject eo)
  {
    super.extractAttributesFrom(eo);
  }

  public void storeAttributesInto(EditableObject eo)
  {
    super.storeAttributesInto(eo);
  }


  public Editor getSecondaryEditor(EditableObject eo)
  {
    ExternalActionEditor result = new ExternalActionEditor();
    result.extractAttributesFrom(eo);
    return result;
  }

  void setExternalAction( boolean b)
  {
		// do nothing; only used for serialization
  }

  boolean getExternalAction()
  {
    return false;
  }

  public EditableObject createObject()
  {
    ExternalAction externalAction = new ExternalAction();
    storeAttributesInto(externalAction);
    return externalAction;
  }

 }
