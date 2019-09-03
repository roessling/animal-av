package algoanim.counter.model;

import java.util.LinkedList;
import java.util.ListIterator;

import algoanim.counter.enumeration.ControllerEnum;
import algoanim.counter.view.FourValueView;

/**
 * Class to count Accesses, Assignments, Queueings and Unqueueings.
 * 
 * @author Axel Heimann
 */

public class FourValueCounter extends TwoValueCounter {

	private int queueings = 0;
	private int unqueueings = 0;
	private final LinkedList<FourValueView> observers = new LinkedList<FourValueView>();

	/**
	 * Adds an view as observer to the Counter.
	 * 
	 * @param observer
	 *            the View that should observe this counter
	 */

	public void addObserver(FourValueView observer) {
		observers.add(observer);
	}

	/**
	 * Adds the values of this Counter to the View. The View will be updated if
	 * Counter Values change.
	 * 
	 * @param observer
	 *            the View that should observe this counter
	 */

	public void addCounterToView(FourValueView observer) {
		observers.add(observer);
		observer.update(ControllerEnum.access, access);
		observer.update(ControllerEnum.assignments, assignments);
		observer.update(ControllerEnum.queueings, queueings);
		observer.update(ControllerEnum.unqueueings, unqueueings);
	}

	/**
	 * Notifies all observers about changes
	 * 
	 * @param valueType
	 *            the value which should be updated
	 * 
	 * @param value
	 *            the increase of the value
	 */

	@Override
	public void notifyObservers(ControllerEnum valueType, int value) {
		ListIterator<FourValueView> iterator = observers.listIterator();
		while (iterator.hasNext()) {
			FourValueView visual = iterator.next();
			visual.update(valueType, value);
		}
	}

	/**
	 * Increases the number of queueings
	 * 
	 * @param i
	 *            the increase of queueings
	 */

	public void queueingsInc(int i) {
		queueings = queueings + i;
		notifyObservers(ControllerEnum.queueings, i);
	}

	/**
	 * Returns the number of queueings
	 * 
	 * @return number of queueings
	 */

	public int getQueueings() {
		return queueings;
	}

	/**
	 * Increases the number of unqueueings
	 * 
	 * @param i
	 *            the increase of unqueueings
	 */

	public void unqueueingsInc(int i) {
		unqueueings = unqueueings + i;
		notifyObservers(ControllerEnum.unqueueings, i);
	}

	/**
	 * Returns the number of unqueueings
	 * 
	 * @return number of unqueueings
	 */

	public int getUnqueueings() {
		return unqueueings;
	}
}
