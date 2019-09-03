package htdptl.exceptions;

public class TraceTooLargeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7407702045167732844L;
	private String log;

	public TraceTooLargeException(String log) {
		this.log = log;
	}

	public String getLog() {
		return log;
	}

}
