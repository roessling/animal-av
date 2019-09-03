package extras.lifecycle.query.function;

import javax.xml.bind.annotation.XmlRootElement;

import extras.lifecycle.query.function.Dump;

import extras.lifecycle.query.EvaluationMode;
import extras.lifecycle.query.Knowledge;

/**
 * This function works as <code>Dump</code> but only if run in Debug mode.
 * If it is executed into another mode, it does nothing.
 * 
 * @author Mihail Mihaylov
 * 
 */
@XmlRootElement(name = "Debug")
public class Debug extends Dump {

	/**
	 * 
	 */
	public Debug() {
		super();
	}

	@Override
	public Object calculate(Knowledge knowledge) {
		// We display the output only if we run in Debug mode
		if (knowledge.getEvaluationMode() == EvaluationMode.Debug)
			super.calculate(knowledge);
		
		return null;
	}

}
