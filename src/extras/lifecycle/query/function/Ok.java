package extras.lifecycle.query.function;

import javax.xml.bind.annotation.XmlRootElement;

import extras.lifecycle.query.JXPathUtils;
import extras.lifecycle.query.Knowledge;
import extras.lifecycle.query.QueryException;
import extras.lifecycle.query.workflow.Function;

/**
 * @author Mihail Mihaylov
 * 
 */
@XmlRootElement(name = "Ok")
public class Ok extends Function {

	/**
	 * 
	 */
	public Ok() {
		super();
	}
	@Override
	public Object calculate(Knowledge knowledge) throws QueryException {
		boolean result = false;
		
		int argCount = arguments.size();
		if (argCount < 1) {
			// With no arguments we end the process with success
			result = true;
		} else if (argCount == 1) {
			// If a single argument it is an expression
			Object first = arguments.get(0);
			String expression = first.toString(); // first should be always
			// string
			result = JXPathUtils.checkCondition(expression, knowledge);
		} else if (argCount == 2) {
			// If two parameter we check for equality
			Object first = arguments.get(0);
			Object second = arguments.get(1);
			result = JXPathUtils.weakEqual(first, second);
		}

		knowledge.setSuccess(result);
		return result;
	}
}
