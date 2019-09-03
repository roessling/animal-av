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
import de.ahrgr.animal.kohnert.asugen.property.EKFontProperty;
import de.ahrgr.animal.kohnert.asugen.property.Property;

/**
 * @author ek
 */
public class FontPropertyEdit extends PropertyEdit {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1842895057191650052L;
	protected EKFontProperty property;
	protected JTextField textField;
	protected FontChooserComboBox fontCombo;
	protected ColorChooserComboBox colorCombo;
	protected JComboBox<String> sizeCombo;
	protected JCheckBox boldCheckBox;
	protected JCheckBox italicCheckBox;
	
	public FontPropertyEdit() {
		super();
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(8,1));
		p.add(new JLabel("font:"), 0);
		fontCombo = new FontChooserComboBox();
		p.add(fontCombo, 1);
		
		p.add(new JLabel("font size:"), 2);
		String[] sizes = {"0", "6","8", "12", "16", "20", "22"};
		sizeCombo = new JComboBox<String>(sizes);
		p.add(sizeCombo, 3);
		
		boldCheckBox = new JCheckBox("bold");
		p.add(boldCheckBox, 4);
		
		italicCheckBox = new JCheckBox("italic");
		p.add(italicCheckBox, 5);
		
		p.add(new JLabel("font color:"), 6);
		colorCombo = new ColorChooserComboBox();
		p.add(colorCombo, 7);
		add(p, BorderLayout.NORTH);
		p.validate();
		validate();
	}
	
	public void setProperty(EKFontProperty p) {
		this.property = p;
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
