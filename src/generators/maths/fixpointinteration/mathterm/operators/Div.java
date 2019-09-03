package generators.maths.fixpointinteration.mathterm.operators;

public class Div extends Operator {

	@Override
	public Character getOperatorToken() {
		return '/';
	}

	@Override
	public Double evaluateOperator(Double x) {
		return left.evaluate(x) / right.evaluate(x);
	}

	@Override
	public int getPriority() {
		return 2;
	}

}
