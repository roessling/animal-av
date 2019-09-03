/*
 * Created on 28.01.2005
 */
package de.ahrgr.animal.kohnert.generatorgui;


import java.awt.BorderLayout;
import java.awt.TextField;

import de.ahrgr.animal.kohnert.asugen.property.Property;
import de.ahrgr.animal.kohnert.asugen.property.TextProperty;

/**
 * @author ek
 */
public class TextPropertyEdit extends PropertyEdit {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6811152359626870899L;
		protected TextProperty property;
    protected TextField textField;
    
    public TextPropertyEdit() {
        super();
        this.setLayout(new BorderLayout());
        textField = new TextField();
        add(textField, BorderLayout.NORTH);
    }
    
    public void setProperty(TextProperty p) {
    	this.property = p;
    	textField.setText(p.getValue());
    }
    
    /* (non-Javadoc)
     * @see generatorgui.PropertyEdit#getProperty()
     */
    public Property getProperty() {
        return property;
    }
    
    public TextProperty getTextProperty() {
        return property;
    }

    /* (non-Javadoc)
     * @see generatorgui.PropertyEdit#writeProperty()
     */
    public void writeProperty() {
       property.setValue(textField.getText());        
    }

	/* (non-Javadoc)
	 * @see generatorgui.PropertyEdit#reloadProperty()
	 */
	public void reloadProperty() {
		setProperty(property);
		
	}
    
}
