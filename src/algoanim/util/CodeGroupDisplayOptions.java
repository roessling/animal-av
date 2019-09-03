package algoanim.util;

/**
 * This is a workaround for the <code>DisplayOptions</code> associated with a code group.
 * Code groups follow other display options rules than the other 
 * <code>Primitive</code>s.
 * @author Stephan Mehlhase, Jens Pfau
 */
public class CodeGroupDisplayOptions extends DisplayOptions {

	/**
	 * The offset of the display (how long it takes before the code groups starts to be displayed)
	 */
	private Timing offset;
	
	/**
	 * The duration of the display (how long it takes until the code group is fully displayed)
	 */
	private Timing duration;
	
	/**
	 * Creates a new CodeGroupDisplayOptions object based on the parameters passed in
	 * 
	 * @param offsetTiming defines the offset <code>Timing</code> to apply to the code group creation.
	 * @param durationTiming defines the duration <code>Timing</code> to apply to the code group creation.
	 */
	public CodeGroupDisplayOptions(Timing offsetTiming, Timing durationTiming) {
		offset = offsetTiming;
		duration = durationTiming;
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
}
