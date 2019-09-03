package extras.lifecycle.monitor;

/**
 * @author Mihail Mihaylov
 *
 */
public class MonitorException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1090965981224964923L;

	/**
	 * 
	 */
	public MonitorException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public MonitorException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public MonitorException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public MonitorException(String message, Throwable cause) {
		super(message, cause);
	}

}
