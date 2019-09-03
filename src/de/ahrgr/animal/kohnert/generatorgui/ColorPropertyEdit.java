/*
 * Created on 28.01.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.ahrgr.animal.kohnert.generatorgui;


import generators.framework.components.ColorChooserComboBox;

import java.awt.BorderLayout;

import de.ahrgr.animal.kohnert.asugen.property.ColorProperty;
import de.ahrgr.animal.kohnert.asugen.property.Property;

/**
 * @author ek
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ColorPropertyEdit extends PropertyEdit {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2452680016442077166L;
	protected ColorProperty property;
	protected ColorChooserComboBox colorCombo;

	public ColorPropertyEdit() {
		super();
		colorCombo = new ColorChooserComboBox();
		add(colorCombo, BorderLayout.NORTH);
		validate();
	}
	
	public void setProperty(ColorProperty p) {
		this.property = p;
		colorCombo.setColorSelected(p.getValue());
	}
	
	/* (non-Javadoc)
	 * @see generatorgui.PropertyEdit#getProperty()
	 */
	public Property getProperty() {
		return property;
	}
	
	public ColorProperty getColorProperty() {
		return property;
	}

	/* (non-Javadoc)
	 * @see generatorgui.PropertyEdit#writeProperty()
	 */
	public void writeProperty() {
		property.setValue(colorCombo.getColorSelectedAsString());
	}

	/* (non-Javadoc)
	 * @see generatorgui.PropertyEdit#reloadProperty()
	 */
	public void reloadProperty() {
		setProperty(property);		
	}

}
