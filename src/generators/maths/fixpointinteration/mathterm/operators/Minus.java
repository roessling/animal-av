package generators.maths.fixpointinteration.mathterm.operators;

public class Minus extends Operator {
	@Override
	public Double evaluateOperator(Double x) {
		return left.evaluate(x) - right.evaluate(x);
	}

	@Override
	public int getPriority() {
		return 1;
	}

	@Override
	public Character getOperatorToken() {
		return '-';
	}

}
