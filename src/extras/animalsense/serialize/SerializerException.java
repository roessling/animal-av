/**
 * 
 */
package extras.animalsense.serialize;

/**
 * This exception is thrown when a problem by saving or loading problem occurs.
 * 
 * @author Mihail Mihaylov
 * 
 */
public class SerializerException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	protected SerializerException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	protected SerializerException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	protected SerializerException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	protected SerializerException(Throwable cause) {
		super(cause);
	}

}
