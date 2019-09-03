package generators.tree.id3;

@SuppressWarnings("serial")
public class RecursionTooDeepException extends Exception {

	public RecursionTooDeepException() {
		super("Recursion too deep.");
	}

}
