package htdptl.prettyPrinting;

import htdptl.ast.AST;
import htdptl.visitors.SubtreeVisitor;
import htdptl.visitors.VisitorUtil;

import java.awt.Point;
import java.util.ArrayList;


/**
 * This class handles the line breaks of the given AST based on the redex and
 * the given expressions that should be highlightable
 * 
 */
public class LineBreaker {

	protected int intend;
	protected int currentLine = 0;

	/**
	 * contains the indentation of each row
	 */
	private ArrayList<Integer> indentations = new ArrayList<Integer>();

	/**
	 * contains the list of elements of each row
	 */
	private ArrayList<ArrayList<String>> lines = new ArrayList<ArrayList<String>>();
	private AST redex;
	private String redexValue;
	private ExpressionPositions expressionPositions;

	public LineBreaker(AST ast, AST redex, String redexValue,
			ExpressionPositions expressionPositions) {
		this.redex = redex;
		this.redexValue = redexValue;
		this.expressionPositions = expressionPositions;
		if (expressionPositions == null) {
			throw new IllegalArgumentException();
		}
		buildLines(ast);
	}

	private void buildLines(AST ast) {
		if (ast == redex) {
			expressionPositions.setRedexPosition(currentLine, 0);
		}
		if (drawOneLine(ast, redex)) {
			addLine(ast);
		} else {
			addLineBreak(ast);
			processChildren(ast);
		}
	}

	private void addLineBreak(AST ast) {
		if (ast.isCondClause()) {
			handleCondClause(ast);
		} else {
			addLineBreakAST(ast);
		}
	}

	/**
	 * Continue with all children.
	 * 
	 * @param ast
	 */
	private void processChildren(AST ast) {

		intend++;
		for (int i = 0; i < ast.numChildren(); i++) {
			AST child = ast.getChild(i);
			buildLines(child);
		}
		intend--;
		addLine(ast.getRightParenthesis());
	}

	/**
	 * 
	 * This method handles the line break of cond clauses.
	 * 
	 * 
	 * @param condClause
	 */
	private void handleCondClause(AST condClause) {
		AST test = condClause.getOperator();
		/*
		 * test is the redex. A line break is added afer the redex to give room
		 * for the evaluated redex
		 */
		// [test = redexValue
		if (test == redex) {
			expressionPositions.setRedexPosition(currentLine, 1);
			ArrayList<String> temp = new ArrayList<String>();
			temp.add(condClause.getLeftParenthesis());
			temp.add(redexCode());
			addLine(temp);
		}

		else {
			// check if the redex is a subtree of test
			SubtreeVisitor cv = new SubtreeVisitor(redex);
			test.accept(cv);
			/*
			 * the redex is contained in test. Add line break after the operator
			 * of test
			 */
			if (cv.isSubtree()) {
				expressionPositions.setRedexPosition(currentLine, intend);
				// add '[(op'
				addLine(condClause.getLeftParenthesis()
						+ test.getLeftParenthesis() + test.getOperator());
				// add children of test
				processChildren(test);
			}
			/*
			 * The redex is not contained in test.
			 */
			else {
				/*
				 * if test should be highlightable, add [ and test as seperate
				 * elements
				 */
				if (expressionPositions.containsExpression(test, new Point(
						currentLine, intend))) {
					ArrayList<String> temp = new ArrayList<String>();
					temp.add(condClause.getLeftParenthesis());
					temp.add(VisitorUtil.toCode(test));
					addLine(temp);
				} else {
					ArrayList<String> split = expressionPositions.split(test,
							currentLine);
					/*
					 * if test contains expressions that should be
					 * highlightable, add the split as elements
					 */
					if (split.size() > 1) {
					  split.add(0, condClause.getLeftParenthesis());
						addLine(split);
					}
					// add [test
					else {
						addLine(condClause.getLeftParenthesis()
								+ VisitorUtil.toCode(test));
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param ast
	 */
	private void addLineBreakAST(AST ast) {

		/*
		 * if the operator of ast should be highlightable, add parenthesis and
		 * operator separately
		 */
		if (expressionPositions.containsExpression(ast.getOperator(),
				new Point(currentLine, 1))) {
			ArrayList<String> temp = new ArrayList<String>();
			temp.add(ast.getLeftParenthesis());
			temp.add(VisitorUtil.toCode(ast.getOperator()));
			addLine(temp);
		}
		// else add '(operator'
		else {
			addLine(ast.getLeftParenthesis() + ast.getOperator());
		}

	}

	private void addLine(AST ast) {
		if (ast == redex && redexValue!=null) {
			addLine(VisitorUtil.toCode(redex) + " = " + redexValue);
		} else {
			addLine(expressionPositions.split(ast, currentLine));
		}
	}

	private String redexCode() {
		String code = VisitorUtil.toCode(redex);
		if (redexValue != null) {
			code += " = " + redexValue;
		}
		return code;
	}

	private void addLine(String line) {
		ArrayList<String> temp = new ArrayList<String>();
		temp.add(line);
		addLine(temp);
	}

	private void addLine(ArrayList<String> line) {
		lines.add(line);
		indentations.add(intend);
		currentLine++;
	}

	/*
	 * return true if this ast should be displayed in one codeline
	 */
	protected boolean drawOneLine(AST ast, AST redex) {

		String operator = VisitorUtil.toCode(ast.getOperator());

		boolean result = true;
		boolean isIf = operator.equals("if");
		boolean isCond = operator.equals("cond");
		boolean isTooLong = VisitorUtil.toCode(ast).length() > 60;
		SubtreeVisitor cv = new SubtreeVisitor(redex);
		ast.accept(cv);
		boolean containsRedex = cv.isSubtree();
		boolean isRedex = (ast == redex);

		// Dont draw in one line if the redex is contained in ast
		result &= (!containsRedex || isRedex);

		// Dont draw in one line if the given AST is too long.
		// Exception: ast is the redex and redexValue is given.
		// Then redex = redexValue should be displayed in one line
		result &= (!isTooLong || ast == redex && redexValue != null);

		/*
		 * if-expression and cond-expression are displayed with line break to
		 * ease highlighting
		 */
		result &= !isIf && !isCond;

		return result;
	}

	public int getRedexPosition() {
		return expressionPositions.getRedexPosition().x;
	}

	public ArrayList<Integer> getIndentations() {
		return indentations;
	}

	public ArrayList<ArrayList<String>> getLines() {
		return lines;
	}

	@Override
	public String toString() {
		String result = "";
		for (int i = 0; i < lines.size(); i++) {
			result += indentations.get(i);
			result += " " + lines.get(i) + "\n";
		}
		return result;
	}

	public int getIndentation(int line) {
		return indentations.get(line);
	}

	public int numLines() {
		return lines.size();
	}

}
