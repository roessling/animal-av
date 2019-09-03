package htdptl.ast;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class represents HtDP-TL expressions. It can be used for expressions like (operator argument1 argument2 ...) or (exp exp exp).
 * In the first case getChildren returns the arguments and in the second case getExpressions returns all child expressions 
 *
 */
public class Expression extends AST {

	private ArrayList<AST> expressions = new ArrayList<AST>();

	@Override
	public AST clone() {
		AST result = new Expression();
		for (Iterator<AST> iterator = expressions.iterator(); iterator
				.hasNext();) {
			AST child = iterator.next();
			result.addChild(child.clone());
		}
		return result;
	}
	

	@Override
	/**
	 * Return null if expressions is empty. Special case
	 * (define-struct empty-lst ())
	 */
	public AST getOperator() {
		AST result = expressions.isEmpty() ? null : expressions.get(0);
		return result;
	}

	@Override
	public void accept(IVisitor visitor) {
		visitor.visit(this);
	}
	
	@Override
	public void addChild(AST child) {
		child.parent = this;
		expressions.add(child);
	}

	@Override
	public AST getChild(int i) {
		return expressions.get(i + 1);
	}

	@Override
	public ArrayList<AST> getChildren() {
		ArrayList<AST> result = new ArrayList<AST>();
		result.addAll(expressions);
		result.remove(0);
		return result;
	}
	
	@Override
	public void removeChild(int i) {
		expressions.get(i).parent = null;
		expressions.remove(i);
		
	}
	@Override
	public void removeChild(AST child) {
		child.parent = null;
		expressions.remove(child);
	}

	

	@Override
	public int numChildren() {
		return expressions.size() - 1;
	}

	

	

	@Override
	public void replaceChild(AST replace, AST with) {
		for (int i=0; i<expressions.size();i++) {
			if (expressions.get(i) == replace) {
				expressions.set(i,with);
				with.parent = this;
				return;
			}
		}
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((expressions == null) ? 0 : expressions.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Expression other = (Expression) obj;
		if (expressions == null) {
			if (other.expressions != null)
				return false;
		} else if (!expressions.equals(other.expressions))
			return false;
		return true;
	}
	
	@Override
	public ArrayList<AST> getExpressions() {
		return expressions;
	}

	
}
