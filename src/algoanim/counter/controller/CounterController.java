package algoanim.counter.controller;

import algoanim.counter.enumeration.PrimitiveEnum;

/**
 * Abstract class for Counter Controllers
 * 
 * @author Axel Heimann
 */

public abstract class CounterController {

	/**
	 * Handles the operations executed on the primitive
	 * 
	 * @param message
	 *            the operation executed on the Primitive
	 */

	public abstract void handleOperations(PrimitiveEnum message);

}
