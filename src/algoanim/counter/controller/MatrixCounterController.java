package algoanim.counter.controller;

import algoanim.counter.enumeration.PrimitiveEnum;

/**
 * Class for MatrixCounterControllers (int, Double and StringMatrix)
 * 
 * @author Axel Heimann
 */

public class MatrixCounterController extends
		AccessAndAssigmentCounterController {

	/**
	 * Handles the operations executed on the matrix
	 * 
	 * @param message
	 *            the operation executed on the matrix
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
			case getElement:
				aac.accessInc(1);
				break;
			default:
				throw new IllegalArgumentException("NoSuchMatrixOperation");
			}
		}
	}
}
