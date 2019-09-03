package extras.lifecycle.checkpoint;

import extras.lifecycle.common.Event;
import extras.lifecycle.common.PropertiesBean;

/**
 * A class witch represents a specific event, which occurs in the observed
 * object.
 * 
 * @author Mihail Mihaylov
 * 
 */
public class CheckpointedEvent extends PropertiesBean implements Event {

	/**
	 * Name of the property defining the sequence number of the event. All
	 * events with the same name are numbered consecutively.
	 */
	public static final String SEQ_NR = "seqnr";

	/**
	 * Name of the property defining the event with the same name, which follows
	 * the current one.
	 */
	public static final String NEXT = "next";

	/**
	 * Name of the property defining the name of the event.
	 */
	public static final String NAME = "name";

	/**
	 * This is the generated serial versionUID.
	 */
	private static final long serialVersionUID = 4097416449608062627L;

	/**
	 * Sets the sequence number of the event.
	 * 
	 * @param sequenceNumber
	 *            parameter for the sequence number
	 */
	public void setSequenceNumber(Integer sequenceNumber) {
		set(SEQ_NR, sequenceNumber);
	}

	/**
	 * Gets the sequence number of the event.
	 * 
	 * @return integer containing the sequence number
	 */
	public Integer getSequenceNumber() {
		// it is always an integer
		return (Integer) get(SEQ_NR);
	}

	/**
	 * Sets the event, which follows the current one.
	 * 
	 * @param next
	 *            refers to the following event
	 */
	public void setNext(CheckpointedEvent next) {
		set(NEXT, next);
	}

	/**
	 * Gets the event, which follows the current one.
	 * 
	 * @return the next event
	 */
	public CheckpointedEvent getNext() {
		return (CheckpointedEvent) get(NEXT);
	}

	/**
	 * Gets the name of the event.
	 * 
	 * @return the name of the event
	 */
	public String getName() {
		return (String) get(NAME);
	}

	/**
	 * Sets the name of the event.
	 * 
	 * @param name
	 *            of the event
	 */
	public void setName(String name) {
		set(NAME, name);
	}
	
	

}
