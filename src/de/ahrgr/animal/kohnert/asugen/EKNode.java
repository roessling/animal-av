/*
 * Created on 21.11.2004
 */
package de.ahrgr.animal.kohnert.asugen;

import java.io.PrintWriter;

/**
 * @author ek
 */
public abstract class EKNode {
    protected PrintWriter out;
    protected AnimalScriptWriter scriptwriter;

    public EKNode(AnimalScriptWriter aWriter) {
        scriptwriter = aWriter;
        out = aWriter.getOut();
    }
        
    public abstract void print();
    public abstract EKNode createOffset(int x, int y);
        
}
