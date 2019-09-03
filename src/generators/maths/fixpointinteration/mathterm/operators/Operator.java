package generators.maths.fixpointinteration.mathterm.operators;

import generators.maths.fixpointinteration.mathterm.Term;

public abstract class Operator extends Term {
	protected Term left;
	protected Term right;
	
	@Override
	public Double evaluate(Double x) {
		if (isComplete()) {
			return evaluateOperator(x);
		} else {
			System.err.println("Evaluate was called without initializing both operands");
			return null;
		}
	}
	
	public boolean addOperand(Term operand) {
		if (left == null) {
			left = operand;
			return true;
		}
		if (right == null) {
			right = operand;
			return true;
		}
		return false;
	}
	
	public Operator getInstance() {
		try {
			return this.getClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			System.err.println(e);
			return null;
		}
	}
	
	@Override
	public int getTotalLength() {
		return left.getTotalLength() + getTokenLength() + right.getTotalLength();
	}
	
	@Override
	public int getTokenLength() {
		return 1;
	}
	
	@Override
	public String toString() {
		if (isComplete()) {
			return left.toString() + getOperatorToken() + right.toString();
		} else {
			return getOperatorToken() + "";
		}
	}
	
	public abstract Character getOperatorToken();
	
	public abstract Double evaluateOperator(Double x);
	
	public abstract int getPriority();
	
	public boolean priorityIsGreaterEqual(Operator op) {
		return (this.getPriority() >= op.getPriority());
	}
	
	public boolean isComplete() {
		return (left != null && right != null);
	}

}
