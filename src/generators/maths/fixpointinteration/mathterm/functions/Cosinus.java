package generators.maths.fixpointinteration.mathterm.functions;

public class Cosinus extends Function {

	@Override
	public Double evaluateFunction(Double x) {
		return Math.cos(parameters.get(0).evaluate(x));
	}

	@Override
	public String functionName() {
		return "cos";
	}

	@Override
	public int parameterCount() {
		return 1;
	}
}
