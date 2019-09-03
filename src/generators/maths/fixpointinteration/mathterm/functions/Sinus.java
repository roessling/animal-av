package generators.maths.fixpointinteration.mathterm.functions;

public class Sinus extends Function {

	@Override
	public Double evaluateFunction(Double x) {
		return Math.sin(parameters.get(0).evaluate(x));
	}

	@Override
	public String functionName() {
		return "sin";
	}

	@Override
	public int parameterCount() {
		return 1;
	}
}
