/*
 * Created on 17.12.2004
 */
package de.ahrgr.animal.kohnert.asugen;

/**
 * @author ek
 */
public class Circle extends AnimalObject {
    
    protected int radius;

    public Circle(AnimalScriptWriter w, EKNode aPosition, int aRadius) {
        super(w);
        name = "circle" + instance_index;
        position = aPosition;
        radius = aRadius;
    }
    
    public int getRadius() {
        return radius;        
    }
    
    /* (non-Javadoc)
     * @see animalobjects.AnimalObject#register()
     */
    public void register() {
        super.register();
        if(registered) return;
        out.print("circle");
        printID();
        position.print();
        out.print(" radius ");
        out.print(radius);
        printColor();
        printDepth();
        printFillColor();
        printDisplayOptions();
        out.println();
        registered = true;
    }

}
