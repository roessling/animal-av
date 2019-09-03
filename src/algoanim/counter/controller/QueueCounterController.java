package algoanim.counter.controller;

import algoanim.counter.enumeration.PrimitiveEnum;
import algoanim.primitives.ArrayBasedQueue;
import algoanim.primitives.ConceptualQueue;
import algoanim.primitives.ListBasedQueue;
import algoanim.primitives.VisualQueue;

/**
 * Class for QueueCounterController (ListBased, ArrayBased and ConceptualQueue)
 * 
 * @author Axel Heimann
 */

public class QueueCounterController extends FourValueCounterController {

	int enqueueAssignments = 0;

	/**
	 * Instantiates the <code>QueueCounterController</code>
	 * 
	 * @param observedObject
	 *            Queue whose operations are counted
	 */

	public QueueCounterController(VisualQueue<?> observedObject) {
		if (observedObject instanceof ArrayBasedQueue) {
			enqueueAssignments = 1;
		}
		if (observedObject instanceof ConceptualQueue) {
			enqueueAssignments = 1;
		}
		if (observedObject instanceof ListBasedQueue) {
			enqueueAssignments = 2;
		}
	}

	/**
	 * Handles the operations executed on the queue
	 * 
	 * @param message
	 *            the operation executed on the queue
	 */

	@Override
	public void handleOperations(PrimitiveEnum message) {
		if (fvc.getActivationStatus()) {
			switch (message) {
			case enqueue:
				fvc.assignmentsInc(enqueueAssignments);
				fvc.queueingsInc(1);
				break;
			case dequeue:
				fvc.assignmentsInc(1);
				fvc.unqueueingsInc(1);
				break;
			case front:
				fvc.accessInc(1);
				break;
			case tail:
				fvc.accessInc(1);
				break;
			default:
				throw new IllegalArgumentException(
						"NoSuchConceptualQueueOperation");
			}
		}
	}

}
