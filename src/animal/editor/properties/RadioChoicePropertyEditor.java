package animal.editor.properties;

import java.awt.Container;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import animal.misc.XProperties;

public class RadioChoicePropertyEditor extends PropertyEditor
{
  JRadioButton[] radioButtons = null;

  public RadioChoicePropertyEditor(String type, XProperties properties)
  {
    super(type, properties);
  }

  public void storeProperty()
  {
    int chosenPos = -1;
    for (int i=0; chosenPos<0 && i<radioButtons.length; i++)
      if (radioButtons[i] != null && radioButtons[i].isSelected())
        chosenPos = i;
    getTargetObject().getProperties().put(getProperties().getProperty("property"),
                                          getProperties().getProperty("param" +chosenPos));
  }

  public void addEditorTo(Container container)
  {
    XProperties props = getProperties();
    String property = getProperties().getProperty("property");
    String label = props.getProperty("param0");
    JLabel myLabel = new JLabel(label, SwingConstants.LEFT);
    container.add(myLabel);
    ButtonGroup buttonGroup = new ButtonGroup();
    JPanel panel = new JPanel();
    String chosenLabel = target.getProperties().getProperty(property);
    radioButtons = new JRadioButton[props.size()];
    for (int i=1; i<props.size(); i++)
      if (props.containsKey("param" +i))
      {
        String currentLabel = props.getProperty("param"+i);
        radioButtons[i] = new JRadioButton(currentLabel);
        if (currentLabel.equals(chosenLabel))
          radioButtons[i].setSelected(true);
        buttonGroup.add(radioButtons[i]);
        panel.add(radioButtons[i]);
      }
    container.add(panel);
  }
}
