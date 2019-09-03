package generators.maths.fixpointinteration.mathterm;

import java.math.BigDecimal;

public class Number extends Term {
	public Double value;
	public int tokenLength;
	
	public Number(Double value, int tokenLength) {
		this.value = value;
		this.tokenLength = tokenLength;
	}

	@Override
	public Double evaluate(Double x) {
		return value;
	}

	@Override
	protected int getTokenLength() {
		return tokenLength;
	}

	@Override
	public int getTotalLength() {
		return getTokenLength();
	}

	@Override
	public String toString() {
		return BigDecimal.valueOf(value).stripTrailingZeros().toString();
		//return value.toString();
	}

}
