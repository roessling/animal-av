package generators.maths.fixpointinteration.mathterm.functions;

public class Sqrt extends Function {

	@Override
	public Double evaluateFunction(Double x) {
		return Math.sqrt(parameters.get(0).evaluate(x));
	}

	@Override
	public String functionName() {
		return "sqrt";
	}

	@Override
	public int parameterCount() {
		return 1;
	}

}
