package algoanim.counter.controller;

import algoanim.counter.enumeration.PrimitiveEnum;

/**
 * Class for ArrayCounterControllers (int, Double and StringArray)
 * 
 * @author Axel Heimann
 */

public class ArrayCounterController extends AccessAndAssigmentCounterController {

	/**
	 * Handles the operations executed on the array
	 * 
	 * @param message
	 *            the operation executed on the array
	 */

	@Override
	public void handleOperations(PrimitiveEnum message) {
		if (aac.getActivationStatus()) {
			switch (message) {
			case put:
				aac.assignmentsInc(1);
				break;
			case swap:
				aac.assignmentsInc(3);
				break;
			case getData:
				aac.accessInc(1);
				break;
			default:
				throw new IllegalArgumentException("NoSuchArrayOperation");
			}
		}
	}
}
