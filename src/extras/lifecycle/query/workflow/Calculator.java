package extras.lifecycle.query.workflow;

import extras.lifecycle.query.workflow.AbstractBox;
import extras.lifecycle.query.workflow.Box;
import extras.lifecycle.query.Knowledge;
import extras.lifecycle.query.QueryException;

/**
 * Defines a simple method, which is used by the <code>CalculateBox</code> to perform the actual calculation.
 * @author Mihail Mihaylov
 */
public abstract class Calculator extends AbstractBox {
	
	/**
	 * Calculates a value and return it. It uses information from the <code>Knowledge</code> object.
	 * @param knowledge contains information, which will be used to calculate the result.
	 * @return the calculated value
	 */
	public abstract Object calculate(final Knowledge knowledge) throws QueryException;

	/* (non-Javadoc)
	 * @see extras.lifecycle.query.workflow.Box#evaluate(extras.lifecycle.common.Knowledge)
	 */
	@Override
	public Box evaluate(Knowledge knowledge) throws QueryException {
		return getNext();
	}

}
