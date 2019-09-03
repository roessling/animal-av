package extras.lifecycle.script.parser;

import extras.lifecycle.query.workflow.Calculator;

public interface Expression {

	public abstract void putIdentifier(String identifier);

	public abstract void putStringValue(String stringValue);

	public abstract void putDecimalIntegerValue(String decimalIntValue);
	
	public Calculator generateExpressionCalculator();

}