package generators.searching.minmax;

import generators.searching.helpers.Node;

import java.util.StringTokenizer;

/**
 * <p>
 * Utility class for parsing a string representation of a tree into a java
 * object tree model represented by a custom node class (with user defined node
 * names). The parser can also be used for validating the textual tree
 * representation.
 * </p>
 * 
 * @see Node
 * 
 * @author Andrej Felde (andrej.felde@stud.tu-darmstadt.de)
 * @author Thomas Hesse (thomas.hesse@stud.tu-darmstadt.de)
 * @author Carina Oberle (carina.oberle@stud.tu-darmstadt.de)
 */
public class TreeParserWithNodeNames {

	private StringTokenizer st;
	private boolean error;
	private String msg;

	/**
	 * <p>
	 * Default constructor which initializes the variables for this class.
	 * </p>
	 */
	public TreeParserWithNodeNames() {
		this.error = false;
	}

	/**
	 * <p>
	 * This method receives a tree in text form which could look like, i.e.
	 * 
	 * <pre>
	 * A {B {1 2} C {3 4}}
	 * </pre>
	 * 
	 * And as a tree this will look as the following:
	 * 
	 * <pre>
	 *        A
	 *       / \
	 *      /   \
	 *     B     C
	 *    / \   / \
	 *   1   2 3   4
	 * </pre>
	 * 
	 * The root node of the tree is returned afterwards.
	 * </p>
	 * 
	 * @param tree
	 *            - The textual representation of a tree as pointed out in this
	 *            documentation which will be parsed into a java object tree
	 *            model represented by a custom node class.
	 * @return root node of the tree
	 * 
	 * @see Node
	 */
	public Node parseText(String tree) {
		this.validate(tree);
		st = new StringTokenizer(tree, "{} ", true);
		String n0 = st.nextToken();
		if (Character.isDigit(n0.charAt(0))) { // root is a leaf
			return new Node(n0, null, Integer.valueOf(n0));
		}
		Node root = new Node(n0, null);
		if (!error)
			buildTree(root);
		return root;
	}

	/**
	 * <p>
	 * This method should be called after the method {@link #parseText(String)}.
	 * This method will return always true (indicates that there is an error) if
	 * the {@link #parseText(String)} method was not called before calling this
	 * method.
	 * </p>
	 * 
	 * @return true if an error occurs and false if not
	 */
	public boolean isValid() {
		return !error;
	}

	public String getMessage() {
		return msg;
	}

	private void validate(String tree) {
		StringTokenizer st = new StringTokenizer(tree, "{} ", true);
		String token = st.nextToken();
		while (token.equals(" ")) { // skip spaces
			token = st.nextToken();
		}
		if (!Character.isLetter(token.charAt(0))) {
			error = true;
			if (msg == null)
				msg = "Expected root node, found: '" + token
						+ "'. (Node names have to start with a letter.)";
			return;
		}
		st = this.validateScope(st);
		if (st.hasMoreTokens()) {
			error = true;
			if (msg == null)
				msg = "No single tree structure entered. (Tokens after actual tree definition found.)";
		}
	}

	private StringTokenizer validateScope(StringTokenizer st) {
		StringTokenizer st2 = st;
		String token = st2.nextToken();
		while (token.equals(" ")) { // skip spaces
			token = st2.nextToken();
		}
		if (!token.equals("{")) { // Non-optional {
			error = true;
			if (msg == null)
				msg = "Expected '{' but found '" + token + "'.";
		}
		token = st2.nextToken();
		while (token.equals(" ")) { // skip spaces
			token = st2.nextToken();
		}
		if (token.equals("{")) {
			error = true;
			if (msg == null)
				msg = "Missing parent node for scope. (Probably wrong placement of braces.)";
		}
		// there must be at least one digit or letter in the scope
		if (!(Character.isLetter(token.charAt(0)) || Character.isDigit(token
				.charAt(0)))) {
			error = true;
			if (msg == null)
				msg = "There must be at least one node in each scope (i.e. between curly braces).";
		}
		while (!token.equals("}")) {
			if (Character.isLetter(token.charAt(0))) {
				st2 = this.validateScope(st2);
			} else if (token.equals("{")) {
				error = true;
				if (msg == null)
					msg = "Missing parent node for scope. (Digits cannot be parent nodes.)";
			} else if (!(Character.isDigit(token.charAt(0)) || token
					.equals(" "))) {
				error = true;
				if (msg == null)
					msg = "Invalid token: " + token;
				break;
			}
			if (!st2.hasMoreTokens()) {
				error = true;
				if (msg == null)
					msg = "Missing closing brace '}'.";
				break;
			}
			token = st2.nextToken();
		}
		return st2;
	}

	private void buildTree(Node parent) {
		String token;
		Node node = parent;
		while (st.hasMoreTokens()) {
			token = st.nextToken();
			if (Character.isLetter(token.charAt(0))) {
				node = new Node(token, parent);
			} else if (token.equals("{")) {
				buildTree(node);
			} else if (Character.isDigit(token.charAt(0))) {
				new Node(token, parent, Integer.valueOf(token));
			} else if (token.equals("}")) {
				return;
			}
		}
	}
}