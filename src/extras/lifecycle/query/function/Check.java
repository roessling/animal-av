/**
 * 
 */
package extras.lifecycle.query.function;

import javax.xml.bind.annotation.XmlRootElement;

import extras.lifecycle.query.JXPathUtils;
import extras.lifecycle.query.Knowledge;
import extras.lifecycle.query.QueryException;
import extras.lifecycle.query.workflow.Function;

/**
 * This functions checks is the arguments are weakequal. And writes the result
 * as a result of the script. E.g. if the comparison is true, the script ends
 * with "OK", otherwise it ends with "not correct".
 * 
 * @author Mihail Mihaylov
 * 
 */
@XmlRootElement(name = "Check")
public class Check extends Function {

	/**
	 * 
	 */
	public Check() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * extras.lifecycle.query.workflow.function.Equals#calculate(extras.lifecycle
	 * .common.Knowledge)
	 */
	@Override
	public Object calculate(Knowledge knowledge) throws QueryException {
		int argCount = arguments.size();

		if (argCount < 1) {
			knowledge.addError("Compare needs min. 1 argument!");
			return null;
		}

		// If a single argument it is an expression
		Object first = arguments.get(0);
		
		if (first == null) {
		  knowledge.setSuccess(false);
	    return false;
		}
		
		String expression = first.toString(); // first should be always string
		boolean result = JXPathUtils.checkCondition(expression, knowledge);

		if (result)
			return result;
		
		// If the condition is not satisfied
		
		if (argCount > 1) {
			Object commentObj = arguments.get(1);
			String comment = commentObj.toString(); // first should be always
			knowledge.addComment(comment);
		}
		
		knowledge.setSuccess(false);

		return false;
	}

}
