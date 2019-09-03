package algoanim.counter.controller;

import algoanim.counter.enumeration.PrimitiveEnum;
import algoanim.counter.model.TwoValueCounter;

/**
 * Abstract class for AccessandAssignmentCounterController (Array, Matrix,
 * Variables)
 * 
 * @author Axel Heimann
 */

public abstract class AccessAndAssigmentCounterController extends
		CounterController {

	public TwoValueCounter aac = new TwoValueCounter();

	/**
	 * Handles the operations executed on the primitive
	 * 
	 * @param message
	 *            the operation executed on the Primitive
	 */

	@Override
	public abstract void handleOperations(PrimitiveEnum message);

}
