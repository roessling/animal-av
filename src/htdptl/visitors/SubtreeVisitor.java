package htdptl.visitors;

import htdptl.ast.AST;
import htdptl.ast.Expression;
import htdptl.ast.IVisitor;
import htdptl.ast.Leaf;

import java.util.Iterator;


/**
 * This visitor computes whether the given AST (search) is contained in the AST that is visited. 
 *
 */
public class SubtreeVisitor implements IVisitor {

	private boolean result = false;
	private AST search;

	public SubtreeVisitor(AST search) {
		this.search = search;
	}

	@Override
	public void visit(Expression expression) {
		if (expression == search) {
			result = true;
			return;
		}
		if (expression.getOperator() != null) {

			for (Iterator<AST> iterator = expression.getExpressions()
					.iterator(); iterator.hasNext();) {
				AST child = iterator.next();
				child.accept(this);
				if (result == true) {
					return;
				}
			}
		}
	}

	@Override
	public void visit(Leaf leaf) {
		if (leaf == search) {
			result = true;
		}
	}

	public boolean isSubtree() {
		return result;
	}

}
