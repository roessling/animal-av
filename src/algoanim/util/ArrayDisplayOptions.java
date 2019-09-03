package algoanim.util;

/**
 * This is a workaround for the <code>DisplayOptions</code> associated with an array.
 * Arrays follow other display options rules than the other 
 * <code>Primitive</code>s.
 * 
 * @author Stephan Mehlhase, Jens Pfau
 */
public class ArrayDisplayOptions extends DisplayOptions {
	/**
	 * Stores the offset when the array will start to be displayed
	 */
	private Timing offset;
	
	/**
	 * Stores the duration it will take for the array to be completely displayed
	 */
	private Timing duration;
	
	/**
	 * Determines if the array is displayed at once (=false) or element by element (=true)
	 */
	private boolean cascaded;
	
	/**
	 * Creates a new instance of the ArrayDisplayOptions.
	 * 
	 * @param offsetTiming defines the offset <code>Timing</code> to apply to the array creation.
	 * @param durationTiming defines the duration <code>Timing</code> to apply to the array creation.
	 * @param isCascaded defines whether the array creation is animated by showing one element after
	 * the other instead of all at once.
	 */
	public ArrayDisplayOptions(Timing offsetTiming, Timing durationTiming, boolean isCascaded) {
		offset = offsetTiming;
		duration = durationTiming;
		cascaded = isCascaded;
	}
	
	/**
	 * Returns the offset <code>Timing</code>.
	 * @return the offset <code>Timing</code>.
	 */
	public Timing getOffset() {
		return offset;
	}
	
	/**
	 * Returns the duration <code>Timing</code>.
	 * @return the duration <code>Timing</code>.
	 */
	public Timing getDuration() {
		return duration;
	}
	
	/**
	 * Returns whether the creation of the array is showed cascaded.
	 * @return whether the creation of the array is showed cascaded.
	 */
	public boolean getCascaded() {
		return cascaded;
	}
}
