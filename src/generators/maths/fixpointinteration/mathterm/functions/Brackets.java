package generators.maths.fixpointinteration.mathterm.functions;

public class Brackets extends Function {

	@Override
	public Double evaluateFunction(Double x) {
		return parameters.get(0).evaluate(x);
	}

	@Override
	public String functionName() {
		return "";
	}

	@Override
	public int parameterCount() {
		return 1;
	}

}
