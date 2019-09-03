package algoanim.util;


/**
 * TicksTiming is a certain kind of a <code>Timing</code> with a numeric 
 * value measured in ticks.
 * 
 * @author Jens Pfau
 */
public class TicksTiming extends Timing {
	/**
	 * Creates a new timing object based on ticks (individual animation frames)
	 * 
	 * @param delay the numeric value for this <code>Timing</code>.
	 */
    public TicksTiming(int delay) {
        super(delay);
    }
    
    /**
     * Returns the base unit for measuring time, here "ticks"
     * @see algoanim.util.Timing#getUnit()
     */
    public String getUnit() {
        return "ticks";
    }
}
