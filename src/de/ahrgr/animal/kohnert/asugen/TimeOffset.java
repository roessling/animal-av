/*
 * Created on 08.12.2004
 */
package de.ahrgr.animal.kohnert.asugen;

/**
 * @author ek
 */
public class TimeOffset {

    public static final int UNIT_TICKS = 0;
    public static final int UNIT_MS = 1;
    
    protected int value;
    protected int unit;
    protected AnimalScriptWriter w;
    
    public TimeOffset(AnimalScriptWriter aWriter, int aValue, int aUnit) {
        this.w = aWriter;
        this.value = aValue;
        this.unit = aUnit;
    }
    
    public TimeOffset(AnimalScriptWriter aWriter, int aValue) {
        this(aWriter, aValue, UNIT_MS);
    }
    
    public int getValue() {
        return value;
    }
    
    public int getUnit() {
        return unit;
    }
    
    public void print() {
        if(value == 0) return;
        w.out.print(" after ");
        w.out.print(value);        
        w.out.print(unit == UNIT_TICKS ? " ticks " : " ms ");
    }
}
