package extras.lifecycle.query.function;

import javax.xml.bind.annotation.XmlRootElement;

import extras.lifecycle.query.Knowledge;
import extras.lifecycle.query.workflow.Function;

/**
 * 
 * @author Mihail Mihaylov
 * 
 */
@XmlRootElement(name = "Min")
public class Min extends Function {

	/**
	 * 
	 */
	public Min() {
		super();
	}

	@Override
	public Object calculate(Knowledge knowledge) {
	
		
		// Convert the list to a list of Strings
		Object min = null;
		String minAsStr = "";
		for (Object temp : arguments) {
			if (temp == null)
				continue;
			String tempAsStr = temp.toString();
			// If tempAsString is bigger
			if (minAsStr.compareTo(tempAsStr) > 0) {
				min = temp;
				minAsStr = tempAsStr;
			}
		}
	
		
		return min;
	}

	public static boolean weakEqual(Object expected, Object actual) {
		if (expected == null)
			return false;

		if (expected.equals(actual))
			return true;

		// Check if 2 Variable are from different types but mean the same.
		Object exptectedValue = normalizeObject(expected);
		Object actualValue = normalizeObject(actual);

		return exptectedValue.equals(actualValue);
	}

	public static Object normalizeObject(Object object) {
		if (object == null)
			return null;
		Object objValue = object;
		if (object instanceof Double)
			objValue = ((Double) object).intValue();

		return objValue.toString().trim();
	}

}
