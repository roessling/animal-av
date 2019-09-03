package algoanim.util;

/**
 * A concrete kind of a <code>Timing</code>. This one is based on a unit
 * measured in milliseconds.
 * 
 * @author Jens Pfau
 */
public class MsTiming extends Timing {
	
    /**
     * Creates a new MsTiming instance for a delay of <em>delay</em> milliseconds.
     * @param delay the delay to apply to this <code>Timing</code>.
     */
    public MsTiming(int delay) {
        super(delay);
    }
    
    /**
     * returns the timing unit for this type of timing ("ms").
     * 
     * @return the timing unit for this type of timing
     * @see algoanim.util.Timing#getUnit()
     */
    public String getUnit() {
        return "ms";
    }
}
