package extras.lifecycle.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import extras.lifecycle.common.PropertiesBean;

import extras.lifecycle.checkpoint.Checkpoint;
import extras.lifecycle.checkpoint.CheckpointedClass;
import extras.lifecycle.checkpoint.CheckpointedEvent;

/**
 * It contains a complete record of all checkpoints from the start of process of
 * gathering checkpoints to the current point.
 * 
 * @author Mihail Mihaylov
 * 
 */
public class Record {

	// TODO Make it with weakref
	/**
	 * Instance of the object, which is being observed.
	 */
	private Object keyObject;

	/**
	 * A checkpointedClass, which is used to create the checkpoints.
	 */
	private CheckpointedClass checkpointedClass;

	/**
	 * List of all checkpoints.
	 */
	private List<Checkpoint> checkpoints;

	/**
	 * List of all events.
	 */
	private Map<String, List<CheckpointedEvent>> events;

	/**
	 * Container for more variables.
	 */
	private PropertiesBean variables;

	/**
	 * Creates a Record for a given object.
	 * 
	 * @param keyObject
	 *            the object, which will be observed.
	 */
	public Record(Object keyObject) {
		super();
		this.keyObject = keyObject;
		checkpoints = new LinkedList<Checkpoint>(); // Changed from ArrayLIst to
													// LinkedList for more
													// performance
		events = new HashMap<String, List<CheckpointedEvent>>();

		checkpointedClass = new CheckpointedClass(keyObject.getClass());
	}

	/**
	 * Creates a <code>Checkpoint</code> and adds it to the record.
	 * 
	 * @return the created checkpoint
	 */
	public synchronized Checkpoint checkpoint() {
		Checkpoint cp = checkpointedClass.getCheckpoint(keyObject);
		assert cp != null;

		int size = checkpoints.size();
		cp.setSequenceNumber(size);

		// We keep the relation -> next for checkpoints
		if (size > 0) {
			Checkpoint lastCP = checkpoints.get(size - 1);
			lastCP.setNext(cp);
		}

		checkpoints.add(cp);

		return cp;
	}

	/**
	 * Adds an event to the list of events.
	 * 
	 * @param event which will be added.
	 */
	public synchronized void addEvent(CheckpointedEvent event) {
		List<CheckpointedEvent> previousEvents = events.get(event.getName());
		int lastSequenceNr = 0;

		if (previousEvents == null) {
			previousEvents = new LinkedList<CheckpointedEvent>();
			events.put(event.getName(), previousEvents);
		} else if (!previousEvents.isEmpty()) {
			// We get the last event with the same name
			CheckpointedEvent lastEvent = previousEvents.get(previousEvents
					.size() - 1);
			lastSequenceNr = lastEvent.getSequenceNumber();
			lastEvent.setNext(event);
		}

		event.setSequenceNumber(lastSequenceNr + 1);

		previousEvents.add(event);
	}

	/**
	 * Getter for all checkpoints.
	 * 
	 * @return the checkpoints
	 */
	public List<Checkpoint> getCheckpoints() {
		return new ArrayList<Checkpoint>(checkpoints);
	}

	/**
	 * @return the variables
	 */
	public PropertiesBean getVariables() {
		return variables;
	}

	/**
	 * @param variables
	 *            the variables to set
	 */
	public void setVariables(PropertiesBean variables) {
		this.variables = variables;
	}

	/**
	 * @return the events
	 */
	public Map<String, List<CheckpointedEvent>> getEvents() {
		return events;
	}

}
