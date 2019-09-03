package animal.editor.properties;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import animal.main.Animal;
import animal.main.PropertiedObject;
import animal.misc.MessageDisplay;
import animal.misc.XProperties;

public class FontNamePropertyEditor extends PropertyEditor implements
		ActionListener {
	private JComboBox<String> fontNames;

	public FontNamePropertyEditor(String type, XProperties properties) {
		super(type, properties);
	}

	public void addEditorTo(Container container) {
		String property = getProperties().getProperty("property");
		PropertiedObject ptgo = target;
		String label = getProperties().getProperty("param0");
		JLabel myLabel = new JLabel(label, SwingConstants.LEFT);
		container.add(myLabel);
		// GraphicsEnvironment ge =
		// GraphicsEnvironment.getLocalGraphicsEnvironment();
		// fontNames = new JComboBox(ge.getAvailableFontFamilyNames());
		// //Toolkit.getDefaultToolkit().getFontList());
		fontNames = new JComboBox<String>(Animal.GLOBAL_FONTS);
		fontNames.addActionListener(this);
		container.add(myLabel);
		String selectedFont = ptgo.getProperties().getProperty(property);

		if (selectedFont != null)
			fontNames.setSelectedItem(ptgo.getProperties().getProperty(property));

		container.add(fontNames);
	}

	public void storeProperty() {
		String propertyToChange = getProperties().getProperty("property");
		getTargetObject().getProperties().put(propertyToChange,
				fontNames.getSelectedItem());
	}

	public void actionPerformed(
	ActionEvent actionEvent) {
		PropertiedObject targetObject = getTargetObject();
		if (targetObject != null && targetObject.getProperties() != null) {
			storeProperty();
		} else
			MessageDisplay.errorMsg("objectNull", new Object[] { getClass() },
					MessageDisplay.INFO);
	}
}
