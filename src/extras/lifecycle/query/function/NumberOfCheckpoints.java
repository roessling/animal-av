package extras.lifecycle.query.function;

import javax.xml.bind.annotation.XmlRootElement;

import extras.lifecycle.query.Knowledge;
import extras.lifecycle.query.workflow.Function;

/**
 * An implementation of <code>Function</code> which calculate the number of
 * the checkpoints in the <code>Knowledge</code>.
 * 
 * This function takes no arguments.
 * 
 * @author Mihail Mihaylov
 *
 */
@XmlRootElement(name = "NumberOfCheckpoints")
public class NumberOfCheckpoints extends Function {
	
	/* (non-Javadoc)
	 * @see extras.lifecycle.checkpoint.evaluator.box.calc.Calculator#calculate(extras.lifecycle.checkpoint.evaluator.EvaluatorKnowledge)
	 */
	@Override
	public Object calculate(Knowledge knowledge) {
		return knowledge.getCheckpoints().size();
	}

}
