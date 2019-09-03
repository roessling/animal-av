package extras.lifecycle.query.function;

import javax.xml.bind.annotation.XmlRootElement;

import extras.lifecycle.query.JXPathUtils;
import extras.lifecycle.query.Knowledge;
import extras.lifecycle.query.workflow.Function;

/**
 * @author Mihail Mihaylov
 * 
 */
@XmlRootElement(name = "Equals")
public class Equals extends Function {

	/**
	 * 
	 */
	public Equals() {
		super();
	}

	@Override
	public Object calculate(Knowledge knowledge) {
		int argCount = arguments.size();
		if (argCount < 2) {
			knowledge.addError("Compare needs min. 2 arguments!");
			return null;
		}

		Object first = arguments.get(0);
		Object second = arguments.get(1);
		// The third parameter should be comparator
		if (argCount >= 3) {
			knowledge
					.addWarning("This version of compare ignores the 3 argument and uses weak equals instead!");
		}
		boolean weakEqual = JXPathUtils.weakEqual(first, second);
		return weakEqual;
	}


}
