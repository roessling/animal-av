/*
 * Created on 08.12.2004
 */
package de.ahrgr.animal.kohnert.asugen;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author ek
 */
public class CodeGroup extends AnimalObject {

    protected ArrayList<String> lines;
    protected EKFont font;

    public CodeGroup(AnimalScriptWriter w, EKNode aPosition) {
        super(w);
        name = "code" + instance_index;
        lines = new ArrayList<String>();
        position = aPosition;
        font = EKFont.FT_DEFAULT;
    }
    
    private void doAddCodeLine(String code, String lineID) {
        scriptwriter.out.print("addCodeLine \"");
        scriptwriter.out.print(code);
        if(lineID != null) {       
            scriptwriter.out.print("\" name \"");
            scriptwriter.out.print(lineID);
        }            
        scriptwriter.out.print("\" to \"");        
        scriptwriter.out.print(getName());
        scriptwriter.out.print("\"");
        // TimeOffset
        scriptwriter.out.println();
    }
    
    public void addCodeLine(String line) {
        lines.add(line);
        if(registered) {
            doAddCodeLine(line, null);
        }
    }
    
    public EKFont getFont() {
    	return font;
    }
    
    public boolean setFont(EKFont f) {
    	if(!registered) {
    		this.font = f;
    		return true;
    	} 
    	return false;
    }

    /* (non-Javadoc)
     * @see animalobjects.AnimalObject#register()
     */
    public void register() {
       super.register();
       if(!registered) {
           scriptwriter.out.print("codeGroup \"");
           scriptwriter.out.print(name);
           scriptwriter.out.print("\" at ");
           position.print();
           printColor();
           scriptwriter.out.print(" ");
           scriptwriter.out.print(font.toAnimalString());
           timeOffset.print();           
           scriptwriter.out.println();
           
           Iterator<String> i = lines.iterator();
           while (i.hasNext()) {
               String s = i.next();
               doAddCodeLine(s, null);
           }
           registered = true;           
       }
    }

}
