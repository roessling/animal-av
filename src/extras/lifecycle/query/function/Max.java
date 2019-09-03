package extras.lifecycle.query.function;

import javax.xml.bind.annotation.XmlRootElement;

import extras.lifecycle.query.Knowledge;
import extras.lifecycle.query.workflow.Function;

/**
 * 
 * @author Mihail Mihaylov
 * 
 */
@XmlRootElement(name = "Max")
public class Max extends Function {

	/**
	 * 
	 */
	public Max() {
		super();
	}

	@Override
	public Object calculate(Knowledge knowledge) {
	
		
		// Convert the list to a list of Strings
		Object max = null;
		String maxAsStr = "";
		for (Object temp : arguments) {
			if (temp == null)
				continue;
			String tempAsStr = temp.toString();
			// If tempAsString is bigger
			if (maxAsStr.compareTo(tempAsStr) < 0) {
				max = temp;
				maxAsStr = tempAsStr;
			}
		}
	
		
		return max;
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
