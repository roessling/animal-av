package generators.maths.fixpointinteration.mathterm;

import generators.maths.fixpointinteration.mathterm.functions.Brackets;
import generators.maths.fixpointinteration.mathterm.functions.Cosinus;
import generators.maths.fixpointinteration.mathterm.functions.Function;
import generators.maths.fixpointinteration.mathterm.functions.Log;
import generators.maths.fixpointinteration.mathterm.functions.Sinus;
import generators.maths.fixpointinteration.mathterm.operators.Div;
import generators.maths.fixpointinteration.mathterm.operators.Minus;
import generators.maths.fixpointinteration.mathterm.operators.Mult;
import generators.maths.fixpointinteration.mathterm.operators.Operator;
import generators.maths.fixpointinteration.mathterm.operators.Plus;
import generators.maths.fixpointinteration.mathterm.operators.Power;

public abstract class Term {
	
	private static final Operator[] operatorList = {new Mult(), new Div(), new Plus(), new Minus(), new Power()};
	private static final Function[] functionList = { new Sinus(), new Cosinus(), new Brackets(), new Log() };
	
	protected Term() {}
	
	public static Term fixpointFromEquation(String string) throws IllegalArgumentException {
		String[] parts = string.split("=");
		if (parts.length != 2) {
			throw new IllegalArgumentException("Invalid Equation");
		}
		Term fx = Term.parse(parts[0]);
		Term gx = Term.parse(parts[1]);
		Operator minus = new Minus();
		minus.addOperand(fx);
		minus.addOperand(gx);
		Operator plus = new Plus();
		plus.addOperand(minus);
		plus.addOperand(new Variable());
		return plus;
	}

	public static Term parse(String string) throws IllegalArgumentException {
		string = string.replaceAll("\\s+", ""); // remove whitespace
//		System.out.println("String to parse: " + string);
		if (string.equals("")) {
			throw new IllegalArgumentException("Term must not be empty.");
		}

		Term root = null;

		Term nextToAdd = null;
		Operator openOperator = null;
		Operator nextOperator = null;
		int currentPos = 0;

		while (currentPos < string.length()) {
			nextToAdd = parseNumberOrFunction(string, currentPos);
//			System.out.println("nextToAdd: " + nextToAdd.toString());
			currentPos += nextToAdd.getTotalLength();

			if (currentPos < string.length()) {
				nextOperator = parseNextOperator(string, currentPos);
//				System.out.println("nextOperator: " + nextOperator.toString());
				currentPos += nextOperator.getTokenLength();
			} else {
//				System.out.println("String ended.");
				// string ended, we found the last operand
				// if an open operator exists, add operand
				if (openOperator != null) {
					openOperator.addOperand(nextToAdd);
//					System.out.println("Appending " + nextToAdd + " to "
//							+ openOperator);
					if (openOperator.isComplete()) {
						openOperator = null;
					}
					break;
				} else {
//					System.out.println("No open operator. " + nextToAdd
//							+ " is root.");
					// else nextToAdd is root
					if (root == null) {
						root = nextToAdd;
						break;
					} else {
						throw new IllegalArgumentException(
								"Found redundant operand");
					}
				}
			}

			if (openOperator == null) { // no current operator, initial state
				nextOperator.addOperand(nextToAdd);
				root = nextOperator;
//				System.out.println(nextToAdd + " added to " + nextOperator);
			} else {
				if (nextOperator.priorityIsGreaterEqual(openOperator)) {
					// next operator has a higher/equal priority, append below
					openOperator.addOperand(nextOperator);
					nextOperator.addOperand(nextToAdd);
//					System.out.println(nextOperator + " added to "
//							+ openOperator);
//					System.out.println(nextToAdd + " added to " + nextOperator);
				} else {
					// next operator has a lower priority, append above
					openOperator.addOperand(nextToAdd);
					nextOperator.addOperand(openOperator);
					root = nextOperator;
//					System.out.println(nextToAdd + " added to " + openOperator);
//					System.out.println(openOperator + " added to "
//							+ nextOperator);
				}
			}
			openOperator = nextOperator;

		}
		if (openOperator == null) {
			return root;
		} else {
			throw new IllegalArgumentException("Incomplete Operator.");
		}

	}

	private static Term parseNumberOrFunction(String string, int pos)
			throws IllegalArgumentException {
		if (Character.isDigit(string.charAt(pos)) || string.charAt(pos) == '-') {
			int count = 1;
			while (pos + count < string.length()
					&& (Character.isDigit(string.charAt(pos + count)) || string
							.charAt(pos + count) == '.')) {
				count++;
			}
			Double number = null;
			try {
				number = Double.parseDouble(string.substring(pos, pos + count));
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("Invalid number at " + pos
						+ ".");
			}
			return new Number(number, count);
		}
		if (string.charAt(pos) == 'x') {
			return new Variable();
		}
		for (Function f : functionList) {
			if (string.substring(pos).startsWith(f.functionName() + "(")) {
				int openingBracketPos = pos + f.getTokenLength();
				int closingBracketPos = findClosingBracket(openingBracketPos,
						string);
				if (closingBracketPos == -1) {
					throw new IllegalArgumentException(
							"Brackets unbalanced at pos " + pos + ".");
				}
				Function fCopy = f.getInstance();
				String parameterString = string.substring(openingBracketPos+1, closingBracketPos);
				String[] parameters = parameterString.split(",");
				for (String parameter: parameters) {
					if (!fCopy.addParameter(Term.parse(parameter))) {
						throw new IllegalArgumentException("Too many arguments at " + pos);
					}
				}
				if (fCopy.allParametersSet()) {
					return fCopy;
				} else {
					throw new IllegalArgumentException("Not enough arguments at " + pos);
				}
				
			}
		}
		// else unknown symbol
		throw new IllegalArgumentException("Unexpected symbol at " + pos + ".");
	}
	
	private static Operator parseNextOperator(String string, int pos) {
		for (Operator o: operatorList) {
			if (string.charAt(pos) == o.getOperatorToken()) {
				return o.getInstance();
			}
		}
		// else unknown symbol
		throw new IllegalArgumentException("Unexpected symbol at " + pos + ".");
	}

	private static int findClosingBracket(int openBracketPos, String string) {
		int counter = 1;
		int position = openBracketPos+1;
		while (counter != 0 && position < string.length()) {
			if (string.charAt(position) == '(') {
				counter++;
			} else {
				if (string.charAt(position) == ')') {
					counter--;
				}
			}
			position++;
		}
		if (counter == 0) {
			return position-1;
		}
		return -1; // unbalanced brackets
	}

	public abstract Double evaluate(Double x);

	protected abstract int getTokenLength();

	public abstract int getTotalLength();

	public abstract String toString();
}
