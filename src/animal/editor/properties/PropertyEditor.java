package animal.editor.properties;

import java.awt.Container;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;

import translator.AnimalTranslator;
import animal.main.PropertiedObject;
import animal.misc.MessageDisplay;
import animal.misc.XProperties;

public class PropertyEditor implements PropertyChangeListener {

	PropertiedObject target;

	public PropertyEditor() {
		// do nothing
	}

	XProperties internalHash = null;

	public PropertyEditor( String type, XProperties properties) {
		internalHash = properties;
	}

	public void setTargetObject(PropertiedObject o) {
		target = o;
	}

	public PropertiedObject getTargetObject() {
		return target;
	}

	public void addEditorTo(Container container) {
		container.add(new JLabel(getClass().getName()));
		container.add(AnimalTranslator.getGUIBuilder().generateJLabel(
				"noValueAssigned"));
	}

	public XProperties getProperties() {
		return internalHash;
	}

	public void propertyChange( PropertyChangeEvent propertyChangeEvent) {
		MessageDisplay.errorMsg("propertyChangeEmpty", new String[] { getClass()
				.getName() }, MessageDisplay.RUN_ERROR);
	}

	public void storeProperty(){
		MessageDisplay.errorMsg("storePropertyEmpty", new String[] { getClass()
				.getName() }, MessageDisplay.RUN_ERROR);
	}

	public String toString() {
		return getClass().getName() + ": " + internalHash.toString();
	}
}
