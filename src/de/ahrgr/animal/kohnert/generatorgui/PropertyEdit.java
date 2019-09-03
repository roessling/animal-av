/*
 * Created on 17.01.2005
 */
package de.ahrgr.animal.kohnert.generatorgui;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import de.ahrgr.animal.kohnert.asugen.property.Property;

/**
 * @author ek
 */
public abstract class PropertyEdit extends JPanel {
          
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public abstract Property getProperty();
    
    public PropertyEdit() {
    	setLayout(new BorderLayout());
    }
    
    /**
     * setz das zugehoerige Property auf seinen Standardwert
     * und zeigt diesen an.
     */
    public void setToDefaultValue() {
        getProperty().setToDefaultValue();
        reloadProperty();
    }
    
    public abstract void reloadProperty();
    
    /**
     * zwingt die Editierkomponente, den aktuellen Wert in das
     * zugehoerige Property Objekt zu schreiben
     */
    public abstract void writeProperty();     
    
}
