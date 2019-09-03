package generators.maths.fixpointinteration.mathterm;

public class Variable extends Term {

	@Override
	public Double evaluate(Double x) {
		return x;
	}

	@Override
	protected int getTokenLength() {
		return 1;
	}

	@Override
	public int getTotalLength() {
		return getTokenLength();
	}

	@Override
	public String toString() {
		return "x";
	}

}
