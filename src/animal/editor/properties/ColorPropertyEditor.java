package animal.editor.properties;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import translator.AnimalTranslator;
import animal.misc.AnimalColorChooserPanel;
import animal.misc.ColorChoice;
import animal.misc.ColoredSquare;
import animal.misc.MessageDisplay;
import animal.misc.XProperties;

public class ColorPropertyEditor extends PropertyEditor implements
		ActionListener {
	Container localContainer;

	JColorChooser chooser;

	JButton activatorButton = null;

	Color currentColor;

	ColoredSquare colorSquare = null;

	public ColorPropertyEditor(String type, XProperties properties) {
		super(type, properties);
		Color currentCol= properties.getColorProperty(properties
				.getProperty("property"), Color.black);
		chooser = new JColorChooser(currentCol);
	}

	public void addEditorTo(Container container) {
		// TO DO!
		localContainer = container;
		String label = getProperties().getProperty("param0");
		JLabel myLabel = new JLabel(label, SwingConstants.LEFT);
		String property = getProperties().getProperty("property");
		XProperties props = getTargetObject().getProperties();
		currentColor = props.getColorProperty(property, Color.black);
		if (chooser == null)
			chooser = new JColorChooser(currentColor);
		chooser.addChooserPanel(new AnimalColorChooserPanel());
		container.add(myLabel);
		colorSquare = new ColoredSquare(currentColor);
		activatorButton = new JButton(ColorChoice.getColorName(currentColor),
				colorSquare);

		container.add(activatorButton);
		activatorButton.addActionListener(this);
	}

	public void storeProperty() {
		String propertyToChange = getProperties().getProperty("property");
		getTargetObject().getProperties().put(propertyToChange, currentColor);
		activatorButton.setText(ColorChoice.getColorName(currentColor));
		colorSquare.changeColor(currentColor);
		activatorButton.setIcon(colorSquare);
	}

	public void actionPerformed(ActionEvent actionEvent) {
		if (actionEvent.getSource() instanceof JButton) {
			currentColor = JColorChooser.showDialog(new JFrame(), 
					AnimalTranslator.translateMessage("selectColor"), currentColor);
			storeProperty();
		} else
			MessageDisplay.errorMsg(actionEvent.toString(), MessageDisplay.INFO);
	}

	public void propertyChange(PropertyChangeEvent event) {
		MessageDisplay.errorMsg(event.toString(), MessageDisplay.INFO);
	}
}
