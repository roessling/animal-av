/*
 * Created on 21.11.2004
 */
package de.ahrgr.animal.kohnert.asugen;

/**
 * @author ek
 */
public class OffsetNode extends EKNode {

    protected AnimalObject src;
    protected int offset_x;
    protected int offset_y;
    protected int direction;
    
    public OffsetNode(AnimalScriptWriter aWriter, AnimalObject psrc, 
        int poffset_x, int poffset_y, int pdirection) {
            super(aWriter);
            src = psrc;
            offset_x = poffset_x;
            offset_y = poffset_y;
            direction = pdirection;
    }

    /* (non-Javadoc)
     * @see animalobjects.Node#print()
     */
    public void print() {
       out.print(" offset (");
       out.print(offset_x);
       out.print(", ");
       out.print(offset_y);
       out.print(") from \"");
       out.print(src.getName());
       out.print("\"  ");
       String ds = null; 
       switch(direction) {
           case AnimalObject.NW: ds = "NW"; break;
           case AnimalObject.N: ds = "N"; break;
           case AnimalObject.NE: ds = "NE"; break;
           case AnimalObject.W: ds = "W"; break;
           case AnimalObject.E: ds = "E"; break;
           case AnimalObject.SW: ds = "SW"; break;
           case AnimalObject.S: ds = "S"; break;
           case AnimalObject.SE: ds = "SE"; break;
           default: ds = "C";
       }
       out.print(ds);
       out.print(" ");
    }

    /* (non-Javadoc)
     * @see animalobjects.Node#createOffset(int, int)
     */
    public EKNode createOffset(int x, int y) {
        return new OffsetNode(scriptwriter, src, offset_x + x, offset_y + y, direction);
    }

}
