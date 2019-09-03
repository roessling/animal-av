/*
 * Created on 21.11.2004
 */
package de.ahrgr.animal.kohnert.asugen;

import java.io.PrintWriter;

/**
 * Represents a graphical Animal Object.
 * @author ek
 */
public abstract class AnimalObject {
    
    public static final int NW = 0;
    public static final int N = 1;
    public static final int NE = 2;
    public static final int W = 3;
    public static final int C= 4;
    public static final int E = 5;
    public static final int SW = 6;
    public static final int S = 7;
    public static final int SE = 8;
    
    protected static int instance_count = 0; // fuer automatische Namensgebung
    protected PrintWriter out;
    protected AnimalScriptWriter scriptwriter;
    protected boolean registered = false;
    protected String name = null;
    protected int instance_index = 0; 
    protected EKNode position;    
    protected int depth;
    protected boolean hidden = false;
    protected TimeOffset timeOffset;
    protected EKColor color = null;
    protected EKColor fillColor = null;
    
    /**
     * 
     * @param sw the associated scriptgenerator
     */
    public AnimalObject(AnimalScriptWriter sw) {
        scriptwriter = sw;
        out = sw.getOut();
        instance_count++;
        name = "obj" + instance_count;
        instance_index = instance_count;
        depth = sw.default_depth;
        timeOffset = new TimeOffset(sw, 0);
    }
    
    /**
     * Sets the name of the object displayed in the generated
     * script file
     * @param pname
     */
    public void setName(String pname) {
        // Name kann nur geändert werden, wenn Objekt noch nicht 
        // geschrieben wurde
        if(!registered) name = pname;
    }
    
    /**
     * Sets the depth of the object
     * 
     * @param aDepth the depth
     */
    public void setDepth(int aDepth) {
        if(!registered) 
        	depth = aDepth;
    }
    
    /**
     * Returns the name of the object displayed in the generated
     * script file
     * @return the name of the object
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns the current position of the Object
     * @return the objects position
     */
    public EKNode getPosition() {
        return position;
    }
    
    /**
     * is the object hidden?
     * @return hidden
     */
    public boolean getHidden() {
        return hidden;
    }
    
    /**
     * Tells the Object to generate its script.
     *
     */
    public void register() {
        if(!registered)
            scriptwriter.registeredObjects.add(this);
    }
    
    public void moveTo(EKNode dst) {
        if(!registered) register();
        out.print("move \"");
        out.print(name);
        out.print("\" to ");
        dst.print();
        out.print(" within 400 ms");
        out.println();
        position = dst;
    }
    
    /**
     * Create a new Node with teh given relative position to this
     * object
     * @param dx relative x coordinate
     * @param dy relative y coordinate
     * @param direction a direction constant
     * @return a new Node object
     */
    public EKNode createOffset(int dx, int dy, int direction) {
        return new OffsetNode(scriptwriter, this, dx, dy, direction);
    }   
    
    /**
     * sets a delay
     * @param value the delay of the object in ms
     */
    public void setTimeOffset(int value) {
        if(registered) return;
        timeOffset = new TimeOffset(scriptwriter, value);
    }
    
    /**
     * Sets the color of the object
     * @param c the color
     */
    public void setColor(EKColor c) {
        color = c;
        if(!registered) {
            return;
        }
    }
    
    /**
     * Sets the fillcolor of the object
     * @param c the fill color
     */
    public void setFillColor(EKColor c) {
        fillColor = c;
        if(!registered) {            
            return; 
        }
        out.print("color \"");
        out.print(name);
        out.print("\" type \"fillColor\" ");
        out.println(c.getColorString());
    }
        
    /**
     * 
     * @return the fill color of the object
     */
    public EKColor getFillColor() {
        return fillColor;
    }
    
    /**
     * 
     * @return the color of the object
     */
    public EKColor getColor() {
        return color;
    }
    
    /**
     * Sets the visibility of the object
     * 
     * @param isHidden should the object be hidden?
     */
    public void setHidden(boolean isHidden) {
    	if (!registered) 
    		hidden = isHidden;
    	else {
    		if (isHidden) 
    			out.print("hide \"");
    		else 
    			out.print("show \"");
    		out.print(name);
    		out.println("\"");            
        }
    }
    
    /**
     * Sets the position of the Object
     * @param node the position
     */
    public void setPosition(EKNode node) {
        position = node;
        if(!registered) {            
            return;
        }
        out.print("move \"");
        out.print(name);
        out.print("\" to ");
        position.print();        
        out.println();        
    }
    
    
    protected void printID() {
        out.print(" \"");
        out.print(name);
        out.print("\"");
    }
    protected void printQuoted(String s) {
        out.print(" \"");
        out.print(s);
        out.print("\"");
    }
    protected void printDisplayOptions() {
        if(hidden) out.print(" hidden ");
    }
    
    protected void printColor() {
        if(color == null) return;
        out.print(" color ");
        out.print(color.getColorString());        
    }

    protected void printFillColor() {
        if(fillColor == null) return;
        out.print(" filled fillColor ");
        out.print(fillColor.getColorString());        
    }
    
    protected void printDepth() {
        if(depth == 0) return;
        out.print(" depth ");
        out.print(depth);
    }
    
}
