package generators.maths.fixpointinteration.mathterm.functions;

public class Absolute extends Function {

	@Override
	public Double evaluateFunction(Double x) {
		return Math.abs(parameters.get(0).evaluate(x));
	}

	@Override
	public String functionName() {
		return "abs";
	}

	@Override
	public int parameterCount() {
		return 1;
	}

}
