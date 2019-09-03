package animal.editor.properties;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import animal.main.PropertiedObject;
import animal.misc.MessageDisplay;
import animal.misc.XProperties;

public class BooleanPropertyEditor extends PropertyEditor implements
		ActionListener {
	JCheckBox checkBox;

	public BooleanPropertyEditor(String type, XProperties properties) {
		super(type, properties);
	}

	public void addEditorTo(Container container) {
		String label = getProperties().getProperty("param0");
		String property = getProperties().getProperty("property");
		JLabel myLabel = new JLabel(label, SwingConstants.LEFT);
		PropertiedObject ptgo = target;
		boolean isActive = ptgo.getProperties().getBoolProperty(property);

		checkBox = new JCheckBox(label);
		checkBox.addActionListener(this);
		checkBox.setSelected(isActive);
		container.add(myLabel);
		container.add(checkBox);
	}

	public void storeProperty() {
		PropertiedObject targetObject = getTargetObject();
		String propertyToChange = getProperties().getProperty("property");
		targetObject.getProperties().put(propertyToChange, checkBox.isSelected());
	}

	public void actionPerformed(
	ActionEvent actionEvent) {
		PropertiedObject targetObject = getTargetObject();
		if (targetObject != null && targetObject.getProperties() != null) {
			storeProperty();
		} else
			MessageDisplay.errorMsg("objectOrPropertyNullException", new Object[] {
					"actionPerformed", getClass().getName() });
	}
}
