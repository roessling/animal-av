package algoanim.counter.controller;

import algoanim.counter.enumeration.PrimitiveEnum;

/**
 * Class for MatrixCounterControllers (int, Double and StringMatrix)
 * 
 * @author Axel Heimann
 */

public class VariableCounterController extends
		AccessAndAssigmentCounterController {

	/**
	 * Handles the operations executed on the Variables
	 * 
	 * @param message
	 *            the operation executed on the Variables
	 */

	@Override
	public void handleOperations(PrimitiveEnum message) {
		if (aac.getActivationStatus()) {
			switch (message) {
			case set:
				aac.assignmentsInc(1);
				break;
			case get:
				aac.accessInc(1);
				break;
			default:
				throw new IllegalArgumentException("NoSuchVariableOperation");
			}
		}
	}
}