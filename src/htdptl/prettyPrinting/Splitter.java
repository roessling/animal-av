package htdptl.prettyPrinting;

import htdptl.ast.AST;
import htdptl.ast.Expression;
import htdptl.ast.IVisitor;
import htdptl.ast.Leaf;
import htdptl.visitors.VisitorUtil;

import java.awt.Point;
import java.util.ArrayList;


/**
 * This visitor is given a list of expressions that should be inserted
 * separately into a code block
 * 
 * It return a split of the AST according to this list.
 * 
 * E.g.
 * expressions: [map, sqr]
 * ast: (map sqr (list 1 2 3))
 * 
 * returns:
 * [(map, sqr, (list 1 2 3))]
 * 
 */
public class Splitter implements IVisitor {

	private ExpressionPositions expressionPositions;
	private StringBuilder builder = new StringBuilder();
	private ArrayList<String> result = new ArrayList<String>();
	private int line;

	public Splitter(ExpressionPositions expressionPositions) {
		this.expressionPositions = expressionPositions;
	}

	public ArrayList<String> split(AST ast, int line) {
		if (expressionPositions.numexpressions() == 0) {
			result.add(VisitorUtil.toCode(ast));
			return result;
		}
		this.line = line;
		ast.accept(this);

		clearBuilder();

		return result;
	}

	@Override
	public void visit(Expression ast) {

		/*
		 * is the whole AST a expression which should be highlighted? e.g.
		 * (make-point 2 3)
		 */
		if (expressionPositions.containsExpression(ast)) {
			clearBuilder();
			expressionPositions.getPositions(ast).add(
					new Point(line, result.size()));
			result.add(VisitorUtil.toCode(ast));
			return;
		}

		/*
		 * handle operator. If operator is a leaf lookup in expressionPositions
		 */
		else if (ast.getOperator() instanceof Leaf) {
			if (expressionPositions.containsExpression(ast.getOperator())) {
				clearBuilder();
				result.add(ast.getLeftParenthesis());
				expressionPositions.getPositions(ast.getOperator()).add(
						new Point(line, result.size()));
				result.add(VisitorUtil.toCode(ast.getOperator()));
			} else {
				builder.append(ast.getLeftParenthesis());
				builder.append(ast.getOperator());
			}
		}
		// operator is an expression
		else {
			builder.append(ast.getLeftParenthesis());
			ast.getOperator().accept(this);
		}

		// children
		for (int i = 0; i < ast.numChildren(); i++) {
			builder.append(" ");
			ast.getChild(i).accept(this);
		}

		builder.append(ast.getRightParenthesis());

	}

	private void clearBuilder() {
		if (builder.toString().trim().length() > 0) {
			result.add(builder.toString().trim());
			builder = new StringBuilder();
		}
	}

	@Override
	public void visit(Leaf leaf) {
		if (expressionPositions.containsExpression(leaf)) {
			clearBuilder();
			expressionPositions.getPositions(leaf).add(
					new Point(line, result.size()));
			result.add(VisitorUtil.toCode(leaf));
		} else {
			builder.append(VisitorUtil.toCode(leaf));
		}

	}
}
