package generators.maths.fixpointinteration.mathterm.functions;

public class Log extends Function {

	@Override
	public Double evaluateFunction(Double x) {
		return Math.log(parameters.get(0).evaluate(x));
	}

	@Override
	public String functionName() {
		return "log";
	}

	@Override
	public int parameterCount() {
		return 1;
	}

}
