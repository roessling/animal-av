package generators.maths.fixpointinteration.mathterm.functions;

import generators.maths.fixpointinteration.mathterm.Term;

import java.util.ArrayList;

public abstract class Function extends Term {
	protected final ArrayList<Term> parameters = new ArrayList<Term>();

	public boolean addParameter(Term t) {
		if (parameters.size() < parameterCount()) {
			parameters.add(t);
			return true;
		} else {
			return false;
		}
	}

	public boolean allParametersSet() {
		return parameters.size() == parameterCount();
	}

	@Override
	public Double evaluate(Double x) {
		if (allParametersSet()) {
			return evaluateFunction(x);
		} else {
			return 0.0;
		}
	}

	@Override
	public int getTokenLength() {
		return functionName().length();
	}

	@Override
	public int getTotalLength() {
		if (allParametersSet()) {
			int parameterLength = 0;
			for (Term t : parameters) {
				parameterLength += t.getTotalLength();
			}
			return getTokenLength() // function name
					+ 2 // 2 brackets
					+ parameterLength // parameter lengths
					+ parameters.size() - 1; // Separators
		} else {
			return getTokenLength();
		}
	}
	
	public String toString() {
		String term = functionName() + "(";
		for (int i = 0; i < parameters.size(); i++) {
			term += parameters.get(i).toString();
			if (i < parameters.size() -1) {
				term += ",";
			}
		}
		term += ")";
		return term;
		
	}
	
	public Function getInstance() {
		try {
			return this.getClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			System.err.println(e);
			return null;
		}
	}

	public abstract Double evaluateFunction(Double x);

	public abstract String functionName();

	public abstract int parameterCount();

}
