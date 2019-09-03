/**
 * 
 */
package extras.lifecycle.script.parser;

import extras.lifecycle.script.parser.AbstractHelper;
import extras.lifecycle.script.parser.Expression;
import extras.lifecycle.query.workflow.Calculator;
import extras.lifecycle.query.workflow.ConstantValue;
import extras.lifecycle.query.workflow.ValueOfIdentifier;

/**
 * @author Mihail Mihaylov
 *
 */
public class SimpleExpression extends AbstractHelper implements Expression {
	
	private Calculator calculator;
	
	@Override
	public Calculator generateExpressionCalculator() {
		return calculator;
	}

	@Override
	public void putDecimalIntegerValue(String decimalIntValue) {
		Integer intValue = Integer.decode(decimalIntValue);
		calculator = new ConstantValue(intValue);
	}

	@Override
	public void putIdentifier(String identifier) {
		calculator = new ValueOfIdentifier(identifier);	
	}

	@Override
	public void putStringValue(String stringValue) {
		calculator = new ConstantValue(stringValue);
	}

}
