package htdptl.prettyPrinting;

import htdptl.ast.AST;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;

import algoanim.primitives.SourceCode;

/**
 * This class is used to highlight different parts of the AST in the animation 
 *
 */
public class Highlighter {

	private ExpressionPositions expressionPositions;
	private LineBreaker prettyPrinter;

	public Highlighter(LineBreaker prettyPrinter,
			ExpressionPositions expressionPositions) {
		this.expressionPositions = expressionPositions;
		this.prettyPrinter = prettyPrinter;
	}

	public void highlight(SourceCode sourceCode, ArrayList<AST> expressions,
			boolean highlight) {

		/*
		 * iterate over all elements
		 */
		for (Iterator<AST> iterator = expressions.iterator(); iterator
				.hasNext();) {
			AST expression = iterator.next();
			ArrayList<Point> points = expressionPositions
					.getPositions(expression);
			if (points != null) {
				/*
				 * for all occurences of this element in the AST:
				 */
				highlight(sourceCode, highlight, points);
			}
		}
	}

	private void highlight(SourceCode sourceCode, boolean highlight,
			ArrayList<Point> points) {
		if (!highlight)
			return;
		for (Iterator<Point> iterator = points.iterator(); iterator.hasNext();) {
			Point point = iterator.next();
			// sourceCode.highlight(point.x, point.y, false);
			if (point.y == 0) {
				highlightBlock(point.x, sourceCode);
			} else {
				sourceCode.highlight(point.x, point.y, false);
			}
		}
	}

	/**
	 * Highlights the given block of the expression in line
	 * 
	 * @param line
	 * @param sourceCode
	 */
	public void highlightBlock(int line, int block, SourceCode sourceCode) {
	  int blockLevel = block;
		int currentIndent = prettyPrinter.getIndentation(line);
		// highlight the following lines if indent is greater
		// than current indent
		for (int i = line + 1; i < prettyPrinter.numLines(); i++) {

			if (blockLevel == 0) {
				highlightBlock(i - 1, sourceCode);		
				return;
			} else {
				String l = prettyPrinter.getLines().get(i).get(0);
				boolean isClosingParenthesis = l.equals(")") || l.equals("]");
				if (prettyPrinter.getIndentation(i) == currentIndent + 1
						&& !isClosingParenthesis) {
				  blockLevel--;
					if (blockLevel == 0) {
						sourceCode.highlight(i);
					}
				}
			}
		}
	}

	public void highlightBlock(int line, SourceCode sourceCode) {
		int indent = prettyPrinter.getIndentation(line);
		sourceCode.highlight(line);
		if (prettyPrinter.getIndentation(line + 1) > indent) {
			for (int i = line + 1; i < prettyPrinter.numLines(); i++) {
				if (prettyPrinter.getIndentation(i) == indent) {

					// reached the end of this block
					// check if its the right parenthesis of this
					// expression, highlight and exit loop
					String l = prettyPrinter.getLines().get(i).get(0);
					boolean isClosingParenthesis = l.equals(")")
							|| l.equals("]");
					if (isClosingParenthesis) {
						sourceCode.highlight(i);
					}
					break;
				} else if (prettyPrinter.getIndentation(i) > indent) {
					sourceCode.highlight(i);
				}
			}
		}

	}

	public void highlightRedex(SourceCode sourceCode) {
		Point redexPosition = expressionPositions.getRedexPosition();
		sourceCode.highlight(redexPosition.x, redexPosition.y, false);
	}

	/**
	 * This method is used for highlighting arguments in a procedure call.
	 * 
	 * @param arguments
	 * @param redexPosition
	 * @param sourceCode
	 */
	public void highlightArguments(ArrayList<AST> arguments, int redexPosition,
			SourceCode sourceCode) {
		int redexIndend = prettyPrinter.getIndentation(redexPosition);
		highlightInLine(redexPosition, arguments, sourceCode);
		for (int i = redexPosition + 1; i < prettyPrinter.numLines(); i++) {
			if (prettyPrinter.getIndentation(i) <= redexIndend) {
				return;
			}
			highlightInLine(i, arguments, sourceCode);
		}
	}

	public void highlightInLine(int line, ArrayList<AST> arguments,
			SourceCode sourceCode) {
		for (Iterator<AST> iterator = arguments.iterator(); iterator.hasNext();) {
			AST argument = iterator.next();
			ArrayList<Point> points = expressionPositions
					.getPositions(argument);
			for (Iterator<Point> iterator2 = points.iterator(); iterator2
					.hasNext();) {
				Point point = iterator2.next();
				if (point.x == line) {
					sourceCode.highlight(point.x, point.y, false);
				}

			}
		}
	}

}
