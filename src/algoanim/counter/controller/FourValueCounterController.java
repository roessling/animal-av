package algoanim.counter.controller;

import algoanim.counter.enumeration.PrimitiveEnum;
import algoanim.counter.model.FourValueCounter;

/**
 * Abstract class for FourValueCounterController (Stack and Queue)
 * 
 * @author Axel Heimann
 */

public abstract class FourValueCounterController extends CounterController {

	public FourValueCounter fvc = new FourValueCounter();

	/**
	 * Handles the operations executed on the primitive
	 * 
	 * @param message
	 *            the operation executed on the Primitive
	 */

	@Override
	public abstract void handleOperations(PrimitiveEnum message);

}
