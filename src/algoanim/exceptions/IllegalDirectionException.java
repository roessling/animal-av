package algoanim.exceptions;

public class IllegalDirectionException extends RuntimeException {
	private String direction;

	public static final long serialVersionUID = 42424242;

	public IllegalDirectionException(String aDirection) {
		direction = aDirection;
	}

	public String getMessage() {
		return "Direction '" + direction + "' is not valid.";
	}
}
