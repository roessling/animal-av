/*
 * Created on 22.11.2004
 */
package de.ahrgr.animal.kohnert.asugen;

/**
 * @author ek
 */
public class TextBox extends AnimalObject {
    
    Rectangle rect;
    Text text;

    public TextBox(AnimalScriptWriter aWriter, EKNode pposition, String ptext) {
        super(aWriter);
        name = "textbox" + instance_index;
        position = pposition;
        text = new Text(aWriter, pposition.createOffset(5, -5), ptext);
        rect = new Rectangle(aWriter, pposition, 
          text.createOffset(5, -5, NE));
        rect.setFillColor(EKColor.DEFAULT_FILLCOLOR);
        setDepth(depth);
    }
    
    public void setDepth(int aDepth) {
        depth = aDepth;
        text.setDepth(depth);
        rect.setDepth(depth+1);
    }
    /* (non-Javadoc)
     * @see animalobjects.AnimalObject#register()
     */
    public void register() {
        super.register();
        if(!registered) {
            text.register();
            rect.register();
            out.print("group \"");
            out.print(name);
            out.print("\" \"");
            out.print(text.getName());
            out.print("\" \"");
            out.print(rect.getName());            
            out.print("\"");
            printDisplayOptions();
            out.println();
            registered = true;
        }
    }
    
    public Text getText() {
        return text;
    }
    
    public Text setText(String s) {        
        if(registered) {
            Text t = new Text(scriptwriter, position.createOffset(5, -5), s);
            getText().setHidden(true);
            t.register();
            text = t;
            return t;
        } 
        text.text = s;
        return text;               
    }
    
    public Rectangle getRectangle() {
        return rect;
    }
    
    public EKNode createOffset(int dx, int dy, int direction) {
        return rect.createOffset(dx, dy, direction);
    }

}
