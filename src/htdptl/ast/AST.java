package htdptl.ast;

import java.util.ArrayList;


/**
 * Representation of abstract syntax trees using composite pattern.  
 */
public abstract class AST {

	protected AST parent;

	@Override
	public abstract AST clone();

	public abstract AST getOperator();

	public abstract void accept(IVisitor visitor);

	public void addChild(AST child) {
		throw new RuntimeException();
	}

	public AST getChild(int i) {
		throw new RuntimeException();
	}

	public ArrayList<AST> getChildren() {
		throw new RuntimeException();
	}

	public void removeChild(int i) {
		throw new RuntimeException();
	}

	public void removeChild(AST child) {
		throw new RuntimeException();
	}

	public abstract int numChildren();

	public void replaceChild(AST replace, AST match) {
		throw new RuntimeException();
	}

	public String getLeftParenthesis() {
		return isCondClause() ? "[" : "(";
	}

	public String getRightParenthesis() {
		return isCondClause() ? "]" : ")";
	}

	public AST getParent() {
		return parent;
	}

	public boolean isCondClause() {
		if (parent == null) {
			return false;
		} else {
			AST operator = parent.getOperator();
			if (operator instanceof Leaf) {
				return ((Leaf) operator).getValue().toString().equals("cond");
			}
			return false;
		}
	}
	
	@Override
	public String toString() {
		CodeGenerationVisitor cgv = new CodeGenerationVisitor();
		accept(cgv);
		return cgv.getCode();
	}

	public ArrayList<AST> getExpressions() {
		throw new RuntimeException();
	}

}
