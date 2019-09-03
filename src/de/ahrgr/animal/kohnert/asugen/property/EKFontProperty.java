/*
 * Created on 28.01.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.ahrgr.animal.kohnert.asugen.property;

import de.ahrgr.animal.kohnert.asugen.EKColor;
import de.ahrgr.animal.kohnert.asugen.EKFont;



/**
 * @author ek
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class EKFontProperty extends Property {
	
	protected EKColor color;
	protected EKColor defaultColor;
	protected EKFont font;
	protected EKFont defaultFont;
	
	public EKFontProperty(String aKey, String aDescription,
			EKColor theDefaultColor, EKFont theDefaultFont) {
		super(aKey, aDescription);
		defaultColor = theDefaultColor;
		color = theDefaultColor;
		defaultFont = theDefaultFont;
		font = theDefaultFont;
	}
	
	public EKFontProperty(String aKey, String theDescription, 
			String theDefaultText) {
		this(aKey, theDescription, EKColor.BLACK, EKFont.FT_DEFAULT);
	}

	public EKColor getColor() {
		return color;
	}
	
	public void setColor(EKColor aColor) {
		this.color = aColor;
	}
    
    public void setColor(String colors) {
        this.color = EKColor.createFromString(colors);
    }
	
	public EKFont getFont() {
		return font;
	}
	
	public void setFont(EKFont theFont) {
		font = theFont;
	}
    
    public void setFont(String fonts) {
        this.font = EKFont.createFromString(fonts);
    }    
    
	/* (non-Javadoc)
	 * @see generatorgui.Property#setToDefaultValue()
	 */
	public void setToDefaultValue() {
		color = defaultColor;
		font = defaultFont;
	}
	
	public String toString() {
		return "\"" + getKey() + "\" = Font(\"" + getFont().toAnimalString() + "\", " + getColor() + ")";
	}

}
