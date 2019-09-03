package algoanim.primitives;

import java.util.LinkedList;
import java.util.ListIterator;

import algoanim.counter.controller.CounterController;
import algoanim.counter.enumeration.PrimitiveEnum;
import algoanim.primitives.generators.GeneratorInterface;
import algoanim.util.DisplayOptions;

public abstract class CountablePrimitive extends Primitive {

	private final LinkedList<CounterController> observers;

	protected CountablePrimitive(GeneratorInterface g, DisplayOptions d) {
		super(g, d);
		observers = new LinkedList<CounterController>();
	}

	public void addObserver(CounterController observer) {
		observers.add(observer);
	}

	public void removeObserver(CounterController observer) {
		observers.remove(observer);
	}

	protected void notifyObservers(PrimitiveEnum message) {
		ListIterator<CounterController> iterator = observers.listIterator();
		while (iterator.hasNext()) {
			CounterController controller = iterator.next();
			controller.handleOperations(message);
		}
	}
}
