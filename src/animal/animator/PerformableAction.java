package animal.animator;

/**
 * Base interface for performable actions including questions and documentation.
 * Only provides the central <code>perform</code> method for actually
 * performing the action.
 * 
 * @author <a href="http://www.ahrgr.de/guido/">Guido
 *         R&ouml;&szlig;ling </a>
 * @version 1.0 2000-09-30
 */
public interface PerformableAction {
	// =================================================================
	//                           ANIMATION EXECUTORS
	// =================================================================

	/**
	 * Perform the action by showing the target URL in an HTML frame
	 */
	public void perform();
}
