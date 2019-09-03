package generators.misc.arithconvert;

/**
 * Information container about the conversion of an arithmetic expression.
 * 
 * @author Jannis Weil, Hendrik Wuerz
 */
public class ConvertInformation {
	/**
	 * The postorder representation.
	 */
	public String postOrder;
	/**
	 * The amount of stack operations which are necessary
	 */
	public int stackOperations;
	/**
	 * The maximum stack size (depth) for the postOrder evaluation
	 */
	public int maxStackSize;

	/**
	 * The amount of literals which were handled during this conversion
	 */
	public int numberOfLiterals;

	/**
	 * The number of expressions which were handled during this conversion
	 */
	public int numberOfExpressions;

	public ConvertInformation() {
		postOrder = "";
		stackOperations = 0;
		maxStackSize = 0;
		numberOfLiterals = 0;
		numberOfExpressions = 0;
	}
}
