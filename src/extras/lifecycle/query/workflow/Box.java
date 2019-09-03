package extras.lifecycle.query.workflow;

import java.util.List;

import extras.lifecycle.common.Variable;
import extras.lifecycle.query.Knowledge;
import extras.lifecycle.query.QueryException;

/**
 * This interface provides the basic functions of the box. Boxes are used to create a workflow,
 * which can make simple decisions.
 * 
 * @author Mihail Mihaylov
 *
 */
public interface Box {
	
	/**
	 * In this method the box performs its task and returns the next box, which should be evaluated.
	 *  
	 * @param knowledge the knowledge object, which keeps temporary information and checkpoints
	 * @return the next box, which should be processed.
	 */
	Box evaluate(Knowledge knowledge) throws QueryException;
	
	/**
	 * Get a list of all input variables. These are variables, which should be set, to ensure
	 * that the box will run correctly.
	 * 
	 * @return list of input variables
	 */
	List<Variable> getInputVariables();
	
}
