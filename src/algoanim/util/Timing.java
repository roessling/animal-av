package algoanim.util;

/**
 * An abstract representation of a Timing. This is based on a number and an
 * arbitrary unit.
 * 
 * @author Jens Pfau
 */
public abstract class Timing extends DisplayOptions {
  
  public final static Timing VERY_SLOW = new MsTiming(1000);
  public final static Timing SLOW = new MsTiming(7500);
  public final static Timing MEDIUM = new MsTiming(500);
  public final static Timing FAST = new MsTiming(250);
  public final static Timing INSTANTEOUS = new MsTiming(0);
  
	/**
	 * the numeric value.
	 */
	private int delay;

	/**
	 * Creates a new Timing instance using the specified delay.
	 * @param theDelay
	 *          the numeric value for this <code>Timing</code>.
	 */
	public Timing(int theDelay) {
		delay = theDelay;
	}

	/**
	 * Returns the contained unit as a String.
	 * 
	 * @return the unit of this <code>Timing</code>.
	 */
	public abstract String getUnit();

	/**
	 * Returns the numeric value for this <code>Timing</code>.
	 * 
	 * @return the numeric value for this <code>Timing</code>.
	 */
	public int getDelay() {
		return delay;
	}
}
