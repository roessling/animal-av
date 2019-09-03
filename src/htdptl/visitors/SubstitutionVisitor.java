package htdptl.visitors;

import htdptl.ast.AST;
import htdptl.ast.Expression;
import htdptl.ast.IVisitor;
import htdptl.ast.Leaf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


/**
 * This visitor executes a set of substitutions. 
 *
 */
public class SubstitutionVisitor implements IVisitor {

	private HashMap<AST, AST> substitution;
	private ArrayList<AST> substituted = new ArrayList<AST>();

	public SubstitutionVisitor(HashMap<AST, AST> substitution) {
		this.substitution = substitution;
	}

	@Override
	public void visit(Expression expression) {
		AST match = substitution.get(expression);
		substitute(expression, match);
		if (match == null && expression.getExpressions().size() > 0) {
			for (Iterator<AST> iterator = expression.getExpressions()
					.iterator(); iterator.hasNext();) {
				AST ast = iterator.next();
				ast.accept(this);
			}
		}
	}

	/**
	 * replace ast with match, if match!=null
	 * 
	 * @param ast
	 * @param match
	 */
	private void substitute(AST ast, AST match) {
		if (match != null) {
			AST myMatch = match.clone();
			substituted.add(myMatch);
			ast.getParent().replaceChild(ast, myMatch);
		}
	}

	@Override
	public void visit(Leaf leaf) {
		AST match = substitution.get(leaf);
		substitute(leaf, match);
	}

	public ArrayList<AST> getSubstitutedExpressions() {
		return substituted;
	}
}
