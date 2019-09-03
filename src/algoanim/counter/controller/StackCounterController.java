package algoanim.counter.controller;

import algoanim.counter.enumeration.PrimitiveEnum;
import algoanim.primitives.ArrayBasedStack;
import algoanim.primitives.ConceptualStack;
import algoanim.primitives.ListBasedStack;
import algoanim.primitives.VisualStack;

/**
 * Class for StackCounterController (ListBased, ArrayBased and ConceptualStack)
 * 
 * @author Axel Heimann
 */

public class StackCounterController extends FourValueCounterController {

	int pushAssignments = 0;

	/**
	 * Instantiates the <code>StackCounterController</code>
	 * 
	 * @param observedObject
	 *            Stack whose operations are counted
	 */

	public StackCounterController(VisualStack<?> observedObject) {
		if (observedObject instanceof ArrayBasedStack) {
			pushAssignments = 1;
		}
		if (observedObject instanceof ConceptualStack) {
			pushAssignments = 1;
		}
		if (observedObject instanceof ListBasedStack) {
			pushAssignments = 2;
		}
	}

	/**
	 * Handles the operations executed on the stack
	 * 
	 * @param message
	 *            the operation executed on the stack
	 */

	@Override
	public void handleOperations(PrimitiveEnum message) {
		if (fvc.getActivationStatus()) {
			switch (message) {
			case push:
				fvc.assignmentsInc(pushAssignments);
				fvc.queueingsInc(1);
				break;
			case pop:
				fvc.assignmentsInc(1);
				fvc.unqueueingsInc(1);
				break;
			case top:
				fvc.accessInc(1);
				break;
			default:
				throw new IllegalArgumentException(
						"NoSuchConceptualStackOperation");
			}
		}
	}
}
