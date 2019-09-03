package algoanim.counter.generator;

import algoanim.counter.controller.ArrayCounterController;
import algoanim.counter.controller.MatrixCounterController;
import algoanim.counter.controller.QueueCounterController;
import algoanim.counter.controller.StackCounterController;
import algoanim.counter.controller.VariableCounterController;
import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.model.FourValueCounter;
import algoanim.primitives.ArrayPrimitive;
import algoanim.primitives.CountablePrimitive;
import algoanim.primitives.MatrixPrimitive;
import algoanim.primitives.Variables;
import algoanim.primitives.VisualQueue;
import algoanim.primitives.VisualStack;

/**
 * Factory class to create Counters.
 * 
 * @author Axel Heimann
 */

public class CounterFactory {

	/**
	 * Creates and returns a counter for an <code>countablePrimitive</code>
	 * 
	 * @param observedObject
	 *            the <code>countablePrimitive</code> whose operations are
	 *            counted
	 * @return AbstractCounter the <code>AbstractCounter</code> which saves the
	 *         counted values.
	 */

	public TwoValueCounter createCounter(
			CountablePrimitive observedObject) {
		if (observedObject instanceof Variables) {
			Variables var = (Variables) observedObject;
			return createCounter(var);
		}
		if (observedObject instanceof ArrayPrimitive) {
			ArrayPrimitive array = (ArrayPrimitive) observedObject;
			return createCounter(array);
		}
		if (observedObject instanceof MatrixPrimitive) {
			MatrixPrimitive matrix = (MatrixPrimitive) observedObject;
			return createCounter(matrix);
		}
		if (observedObject instanceof VisualStack) {
			VisualStack<?> stack = (VisualStack<?>)observedObject;
			return createCounter(stack);
		}
		if (observedObject instanceof VisualQueue) {
			VisualQueue<?> queue = (VisualQueue<?>)observedObject;
			return createCounter(queue);
		}
		try {
			throw new IllegalArgumentException("NotSupportedPrimitive");
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Creates and returns a counter for a <code>Variables</code>
	 * 
	 * @param observedObject
	 *            the <code>Variables</code> whose operations are counted
	 * @return AccessAndAssignmentCounter the
	 *         <code>AccessAndAssignmentCounter</code> which saves the counted
	 *         values.
	 */

	public TwoValueCounter createCounter(Variables observedObject) {
		VariableCounterController vcc = new VariableCounterController();
		observedObject.addObserver(vcc);
		return vcc.aac;
	}

	/**
	 * Creates and returns a counter for an <code>ArrayPrimitive</code>
	 * (IntArray, DoubleArray and StringArray)
	 * 
	 * @param observedObject
	 *            the <code>ArrayPrimitive</code> whose operations are counted
	 * @return AccessAndAssignmentCounter the
	 *         <code>AccessAndAssignmentCounter</code> which saves the counted
	 *         values.
	 */

	public TwoValueCounter createCounter(
			ArrayPrimitive observedObject) {
		ArrayCounterController acc = new ArrayCounterController();
		observedObject.addObserver(acc);
		return acc.aac;
	}

	/**
	 * Creates and returns a counter for an <code>MatrixPrimitive</code>
	 * (IntMatrix, DoubleMatrix and StringMatrix)
	 * 
	 * @param observedObject
	 *            the <code>MatrixPrimitive</code> whose operations are counted
	 * @return AccessAndAssignmentCounter the
	 *         <code>AccessAndAssignmentCounter</code> which saves the counted
	 *         values.
	 */

	public TwoValueCounter createCounter(
			MatrixPrimitive observedObject) {
		MatrixCounterController mcc = new MatrixCounterController();
		observedObject.addObserver(mcc);
		return mcc.aac;
	}

	/**
	 * Creates and returns a counter for an <code>VisualStack</code>
	 * (QueueBasedStack, ArrayBasedStack, ConceptualStack).
	 * 
	 * @param observedObject
	 *            the <code>VisualStack</code> whose operations are counted
	 * @return FourValueCounter the <code>FourValueCounter</code> which saves
	 *         the counted values.
	 */

	public FourValueCounter createCounter(VisualStack<?> observedObject) {
		StackCounterController scc = new StackCounterController(observedObject);
		observedObject.addObserver(scc);
		return scc.fvc;
	}

	/**
	 * Creates and returns a counter for an <code>VisualQueue</code>
	 * (QueueBasedQueue, ArrayBasedQueue, ConceptualQueue).
	 * 
	 * @param observedObject
	 *            the <code>VisualQueue</code> whose operations are counted
	 * @return FourValueCounter the <code>FourValueCounter</code> which saves
	 *         the counted values.
	 */

	public FourValueCounter createCounter(VisualQueue<?> observedObject) {
		QueueCounterController qcc = new QueueCounterController(observedObject);
		observedObject.addObserver(qcc);
		return qcc.fvc;
	}
}
