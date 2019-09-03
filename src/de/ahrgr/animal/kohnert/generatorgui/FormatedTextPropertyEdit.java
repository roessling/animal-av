/*
 * Created on 28.01.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.ahrgr.animal.kohnert.generatorgui;

import generators.framework.components.ColorChooserComboBox;
import generators.framework.components.FontChooserComboBox;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.ahrgr.animal.kohnert.asugen.EKFont;
import de.ahrgr.animal.kohnert.asugen.property.FormatedTextProperty;
import de.ahrgr.animal.kohnert.asugen.property.Property;

/**
 * @author ek
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FormatedTextPropertyEdit extends PropertyEdit {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6371618650311318498L;
	protected FormatedTextProperty property;
	protected JTextField textField;
	protected FontChooserComboBox fontCombo;
	protected ColorChooserComboBox colorCombo;
	protected JComboBox<String> sizeCombo;
	protected JCheckBox boldCheckBox;
	protected JCheckBox italicCheckBox;
	
	public FormatedTextPropertyEdit() {
		super();
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(10,1));
		p.add(new javax.swing.JLabel("text line:"), 0);
		textField = new JTextField();
		p.add(textField, 1);
		p.add(new JLabel("font:"), 2);
		fontCombo = new FontChooserComboBox();
		p.add(fontCombo, 3);
		
		p.add(new JLabel("font size:"), 4);
		String[] sizes = {"0", "6","8", "12", "16", "20", "22"};
		sizeCombo = new JComboBox<String>(sizes);
		p.add(sizeCombo, 5);
		
		boldCheckBox = new JCheckBox("bold");
		p.add(boldCheckBox, 6);
		
		italicCheckBox = new JCheckBox("italic");
		p.add(italicCheckBox, 7);
		
		p.add(new JLabel("font color:"), 8);
		colorCombo = new ColorChooserComboBox();
		p.add(colorCombo, 9);
		add(p, BorderLayout.NORTH);
		p.validate();
		validate();
	}
	
	public void setProperty(FormatedTextProperty p) {
		this.property = p;
		textField.setText(p.getText());
		EKFont f = p.getFont();
		fontCombo.setFontSelected(p.getFont().toString());
		boldCheckBox.setSelected(f.getIsBold());
		italicCheckBox.setSelected(f.getIsItalic());
		sizeCombo.setSelectedItem("" + f.getSize());
		
		colorCombo.setColorSelected(p.getColor().toString());
	}
	/* (non-Javadoc)
	 * @see generatorgui.PropertyEdit#getProperty()
	 */
	public Property getProperty() {
		return property;
	}

	/* (non-Javadoc)
	 * @see generatorgui.PropertyEdit#writeProperty()
	 */
	public void writeProperty() {
		property.setText(textField.getText());
		EKFont f = EKFont.createFromString(fontCombo.getFontSelectedAsString());
		f = f.deriveBold(boldCheckBox.isSelected());
		f = f.deriveItalic(italicCheckBox.isSelected());
		int size = Integer.parseInt((String)sizeCombo.getSelectedItem());
		f = f.deriveSize(size);
		property.setFont(f);
		property.setColor(colorCombo.getColorSelectedAsString());
	}

	/* (non-Javadoc)
	 * @see generatorgui.PropertyEdit#reloadProperty()
	 */
	public void reloadProperty() {
		setProperty(property);
		
	}
	
}
