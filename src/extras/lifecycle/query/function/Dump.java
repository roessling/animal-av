package extras.lifecycle.query.function;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import extras.lifecycle.common.PropertiesBean;
import extras.lifecycle.query.Knowledge;
import extras.lifecycle.query.workflow.Function;

/**
 * This function can produce comments or display other information about the
 * current status. If it is run with parameters it shows only them. If no
 * parameters are given it dumps all information from the knowledge.
 * 
 * @author Mihail Mihaylov
 * 
 */
@XmlRootElement(name = "Dump")
public class Dump extends Function {

	/**
	 * 
	 */
	public Dump() {
		super();
	}

	@Override
	public Object calculate(Knowledge knowledge) {
		String dump;
		if (arguments.size() == 0) {
			// if there are no parameters we dump everything
			dump = knowledge.getVariables().toString();
		} else {
			// if there are some parameters we dump only the given parameter
			dump = dumpVariables(knowledge, arguments);
		}
		knowledge.addComment(dump);
		return null;
	}

	public static String dumpVariables(Knowledge knowledge,
			List<Object> arguments) {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("[");
		int last = arguments.size() - 1;
		try {
			for (int i = 0; i <= last; i++) {
				String name = (String) arguments.get(i);

				Object value = knowledge.getVariables().get(name);
				strBuf.append(name);
				strBuf.append(" = ");
				strBuf.append(value);
				if (i != last)
					strBuf.append(PropertiesBean.NEWLINE);
			}
		} catch (RuntimeException e) {
			// Wrong parameters
			//
			System.err.println("Error occured by Dump: " + e.getMessage());
		}

		strBuf.append("]");
		return strBuf.toString();
	}

}
