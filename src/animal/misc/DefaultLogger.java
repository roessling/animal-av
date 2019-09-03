package animal.misc;

public class DefaultLogger implements DebugLogger {
	public DefaultLogger() {
		// do nothing
	}

	public void init() {
		// do nothing
	}

	public DebugLogger getCurrentLogger() {
		return this;
	}

	public void logMessage(String message, int level) {
		if (level < ERROR)
			System.out.println(message);
		else
			System.err.println("!!!*** " + message + " ***!!!");
	}

	public void errorMessage(String message, int errorLevel) {
		System.err.println(message);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see animal.misc.DebugLogger#message(java.lang.String)
	 */
	public void message(String msg) {
		errorMessage(msg, DebugLogger.INFO);
	}
}
