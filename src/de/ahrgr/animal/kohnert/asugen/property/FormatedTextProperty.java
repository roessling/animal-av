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
public class FormatedTextProperty extends Property {
	
	protected String text;
	protected String defaultText;
	protected EKColor color;
	protected EKColor defaultColor;
	protected EKFont font;
	protected EKFont defaultFont;
	
	public FormatedTextProperty(String aKey, String theDescription, 
			String theDefaultText,
			EKColor theDefaultColor, EKFont theDefaultFont) {
		super(aKey, theDescription);
		this.defaultText = theDefaultText;
		this.text = theDefaultText;
		this.defaultColor = theDefaultColor;
		this.color = theDefaultColor;
		this.defaultFont = theDefaultFont;
		this.font = theDefaultFont;
	}
	
	public FormatedTextProperty(String aKey, String theDescription,
			String theDefaultText) {
		this(aKey, theDescription, theDefaultText, EKColor.BLACK,
				EKFont.FT_DEFAULT);
	}

	public String getText() {
		return text;
	}
	
	public void setText(String theText) {
		this.text = theText;
	}
	
	public EKColor getColor() {
		return color;
	}
	
	public void setColor(EKColor theColor) {
		this.color = theColor;
	}
    
    public void setColor(String colors) {
        this.color = EKColor.createFromString(colors);
    }
	
	public EKFont getFont() {
		return font;
	}
	
	public void setFont(EKFont theFont) {
		this.font = theFont;
	}
    
    public void setFont(String fonts) {
        this.font = EKFont.createFromString(fonts);
    }    
    
	/* (non-Javadoc)
	 * @see generatorgui.Property#setToDefaultValue()
	 */
	public void setToDefaultValue() {
		text = defaultText;
		color = defaultColor;
		font = defaultFont;
	}
	
	public String toString() {
		return "\"" + getKey() + "\" = FormatedText(\"" + getText() + "\", \"" + getFont().toAnimalString() + "\", " + getColor() + ")";
	}

}
