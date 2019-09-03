package htdptl.visitors;

import htdptl.ast.AST;
import htdptl.ast.Expression;
import htdptl.ast.IVisitor;
import htdptl.ast.Leaf;

import java.util.Iterator;


/**
 * This visitor computes the redex in the AST. Eager evaluation is used.  
 *
 */
public class RedexVisitor implements IVisitor {

	private AST redex;
	private NormalformVisitor pv;

	public RedexVisitor(NormalformVisitor pv) {
		this.pv = pv;
	}

	public AST getRedex() {
		return redex;
	}

	@Override
	public void visit(Expression expression) {
		String operator = VisitorUtil.toCode(expression.getOperator());
		// an if expression is the redex, if the condition is in normal form
		if (operator.equals("if")) {
			String condition = VisitorUtil.toCode(expression.getChild(0));
			if (condition.equals("false") || condition.equals("true")) {
				redex = expression;
			} else {
				visitChildren(expression);
			}
		} 
		// a cond expression is the redex if it contains a cond clause with condition "true" or "else"
		else if (operator.equals("cond")) {
			for (int i = 0; i < expression.numChildren(); i++) {
				AST test = expression.getChild(i).getOperator();
				if (VisitorUtil.toCode(test).equals("true") || VisitorUtil.toCode(test).equals("else")) {
					redex = expression;
					return;
				}
				test.accept(pv);
				if (!pv.isPrimitive()) {
					test.accept(this);
					return;
				}
			}
		} else {
			visitChildren(expression);
		}

	}

	/**
	 * Iterate over all child expressions. If a child is not in normal form, search the redex in this child
	 * @param expression
	 */
	private void visitChildren(Expression expression) {
		for (Iterator<AST> iterator = expression.getExpressions().iterator(); iterator
				.hasNext();) {
			AST child = iterator.next();
			child.accept(pv);
			if (!pv.isPrimitive()) {
				child.accept(this);
				return;
			}
		}
		redex = expression;
	}

	@Override
	public void visit(Leaf leaf) {
		redex = leaf;
	}

}
