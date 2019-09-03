package animal.editor.properties;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JTextField;

import animal.main.PropertiedObject;
import animal.misc.MessageDisplay;
import animal.misc.XProperties;


public class StringPropertyEditor 
  extends PropertyEditor 
  implements ActionListener
{
  private JTextField textField;

  public StringPropertyEditor(String type, XProperties properties)
  {
    super(type, properties);
  }
  
  public void addEditorTo(Container container)
  {
    String property = getProperties().getProperty("property");
    String currentText = getTargetObject().getProperties().getProperty(property);
    JLabel myLabel = new JLabel(getProperties().getProperty("param0"));
    textField = new JTextField(currentText, 20);
    textField.addActionListener(this);
    container.add(myLabel);
    container.add(textField);
  }

  public void storeProperty()
  {
    String propertyToChange = getProperties().getProperty("property");
    getTargetObject().getProperties().put(propertyToChange,
                                          textField.getText());
  }
  public void actionPerformed(ActionEvent actionEvent)
  {
    PropertiedObject targetObject = getTargetObject();
    if (targetObject != null && targetObject.getProperties() != null)
    {
      String propertyToChange = getProperties().getProperty("property");
      targetObject.getProperties().put(propertyToChange,
                                       actionEvent.getActionCommand());
    }
    else
    	MessageDisplay.errorMsg("objectNull", new Object[] { getClass() });
  }
}
