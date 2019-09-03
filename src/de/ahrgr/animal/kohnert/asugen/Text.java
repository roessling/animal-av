/*
 * Created on 22.11.2004
 */
package de.ahrgr.animal.kohnert.asugen;


/**
 * @author ek
 */
public class Text extends AnimalObject {
    
    protected String text;
    protected EKFont font;

    public Text(AnimalScriptWriter aWriter, EKNode ppos, String ptext) {
        super(aWriter);
        name = "text" + instance_index;
        position = ppos;
        text = ptext;
        font = EKFont.FT_DEFAULT;
        color = EKColor.BLACK;
    }
    
    public String getValue() { 
        return text;
    }
    
    public void setValue(String pText) {
        text = pText; // TODO Text richtig ändern, wenn bereits registriert
    }
        
    public boolean setFont(EKFont aFont) {
        if(!registered) {
            font = aFont;
            return true;
        }
        return false;
    }
    
    public EKFont getFont() {
        return font;
    }

    /* (non-Javadoc)
     * @see animalobjects.AnimalObject#register()
     */
    public void register() {
        super.register();
        if(!registered) {
            out.print("text \"");
            out.print(name);
            out.print("\" \"");
            out.print(text);
            out.print("\" ");
            position.print();
            printColor();
            out.print(" depth ");
            out.print(depth);
            out.print(" ");
           //* if(!font.equals(Font.FT_DEFAULT)) {
                out.print(font.toAnimalString());
            //}
            printDisplayOptions();
            out.println();
            registered = true;
        }
    }

}
