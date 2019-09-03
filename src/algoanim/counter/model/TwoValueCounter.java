package algoanim.counter.model;

import java.util.LinkedList;
import java.util.ListIterator;

import algoanim.counter.enumeration.ControllerEnum;
import algoanim.counter.view.TwoValueView;

/**
 * Class to count Access and Assignments.
 * 
 * @author Axel Heimann
 */

public class TwoValueCounter {

	protected boolean activated = true;
	protected int access = 0;
	protected int assignments = 0;
	private final LinkedList<TwoValueView> observers = new LinkedList<TwoValueView>();

	/**
	 * Returns the activations Status of the counter
	 * 
	 * @return true-> counter activated false -> counter deactivated
	 */

	public boolean getActivationStatus() {
		return activated;
	}

	/**
	 * Adds an view as observer to the Counter.
	 * 
	 * @param observer
	 *            the View that should observe this counter
	 */

	public void addObserver(TwoValueView observer) {
		observers.add(observer);
	}

	/**
	 * Adds the values of this Counter to the View. The View will be updated if
	 * Counter Values change.
	 * 
	 * @param observer
	 *            the View that should observe this counter
	 */

	public void addCounterToView(TwoValueView observer) {
		observers.add(observer);
		observer.update(ControllerEnum.access, access);
		observer.update(ControllerEnum.assignments, assignments);
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

	public void notifyObservers(ControllerEnum valueType, int value) {
		ListIterator<TwoValueView> iterator = observers.listIterator();
		while (iterator.hasNext()) {
			TwoValueView visual = iterator.next();
			visual.update(valueType, value);
		}
	}

	/**
	 * Removes an view as observer from the Counter.
	 * 
	 * @param observer
	 *            the View that should be removed as observer from this counter
	 */

	protected void removeObservers(TwoValueView observer) {
		observers.remove(observer);
	}

	/**
	 * Activates the automatic counting. The counter can still be manually
	 * modified.
	 */

	public void activateCounting() {
		activated = true;
	}

	/**
	 * Deactivates the automatic counting. The counter can still be manually
	 * modified.
	 */

	public void deactivateCounting() {
		activated = false;
	}

	/**
	 * Increases the number of accesses
	 * 
	 * @param i
	 *            the increase of accesses
	 */

	public void accessInc(int i) {
		access = access + i;
		notifyObservers(ControllerEnum.access, i);
	}

	/**
	 * Returns the number of accesses
	 * 
	 * @return number of accesses
	 */

	public int getAccess() {
		return access;
	}

	/**
	 * Increases the number of assignments
	 * 
	 * @param i
	 *            the increase of assignments
	 */

	public void assignmentsInc(int i) {
		assignments = assignments + i;
		notifyObservers(ControllerEnum.assignments, i);
	}

	/**
	 * Returns the number of assignments
	 * 
	 * @return number of assignments
	 */

	public int getAssigments() {
		return assignments;
	}
}
