package animal.editor.properties;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import translator.AnimalTranslator;
import animal.main.PropertiedObject;
import animal.misc.MessageDisplay;
import animal.misc.XProperties;

public class IntPropertyEditor
  extends PropertyEditor
  implements ActionListener
{
  JTextField textField;
  public IntPropertyEditor(String type, XProperties properties)
  {
    super(type, properties);
  }

  public void addEditorTo(Container container)
  {
    String label = getProperties().getProperty("param0");
    PropertiedObject ptgo = target;
    int defaultValue = Integer.valueOf(getProperties().getProperty("param2")).intValue();
    int value = ptgo.getProperties().getIntProperty(getProperties().getProperty("property"), defaultValue);
    JLabel myLabel = new JLabel(label, SwingConstants.LEFT);
    textField = new JTextField(String.valueOf(value), 8);
    textField.addActionListener(this);
    container.add(myLabel);
    container.add(textField);
  }

  public void storeProperty()
  {
    int value = Integer.MIN_VALUE;
    try {
      value = Integer.parseInt(textField.getText());
      PropertiedObject targetObject = getTargetObject();
      String propertyToChange = getProperties().getProperty("property");
      targetObject.getProperties().put(propertyToChange, value);
   }
    catch(NumberFormatException numberFormatException)
    {
    	MessageDisplay.errorMsg(AnimalTranslator.translateMessage("invalidNumberInput",
    			new String[] {textField.getText()}), 
    			MessageDisplay.INFO);
    }
  }

  public void actionPerformed( ActionEvent actionEvent)
  {
    PropertiedObject targetObject = getTargetObject();
    if (targetObject != null && targetObject.getProperties() != null)
      storeProperty();
    else
    	MessageDisplay.errorMsg("objectNull", new Object[]{ getClass() });
  }
}
