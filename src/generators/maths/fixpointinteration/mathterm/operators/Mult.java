package generators.maths.fixpointinteration.mathterm.operators;

public class Mult extends Operator {
	@Override
	public Double evaluateOperator(Double x) {
		return left.evaluate(x) * right.evaluate(x);
	}

	@Override
	public int getPriority() {
		return 2;
	}

	@Override
	public Character getOperatorToken() {
		return '*';
	}

}
