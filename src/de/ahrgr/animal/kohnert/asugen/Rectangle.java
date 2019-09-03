/*
 * Created on 22.11.2004
 */
package de.ahrgr.animal.kohnert.asugen;

/**
 * @author ek
 */
public class Rectangle extends AnimalObject {

    protected EKNode position2 = null; // Die andere Ecke

    public Rectangle(AnimalScriptWriter aWriter) {
        super(aWriter);
    }

    public Rectangle(AnimalScriptWriter aWriter, EKNode edge1, EKNode edge2) {
        super(aWriter);
        name = "rect" + instance_index;
        position = edge1;
        position2 = edge2;
    }
        
    public Rectangle(AnimalScriptWriter aWriter, int x1, int y1, int x2, int y2) {
        this(aWriter, new AbsoluteNode(aWriter, x1, y1), 
          new AbsoluteNode(aWriter, x2, y2));
    }
    
    /* (non-Javadoc)
     * @see animalobjects.AnimalObject#register()
     */
    public void register() {
        super.register();
        if(!registered) {
            out.print("rectangle ");
            printID();
            position.print();
            position2.print();
            printColor();
            printDepth();
            printFillColor();
            printDisplayOptions();
            out.println();
            registered = true;
        }
    }
    


}
