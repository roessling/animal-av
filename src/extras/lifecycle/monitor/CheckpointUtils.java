/**
 * 
 */
package extras.lifecycle.monitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import extras.lifecycle.monitor.Monitor;
import extras.lifecycle.monitor.MonitorException;
import extras.lifecycle.monitor.MonitorImpl;

import extras.lifecycle.checkpoint.CheckpointedEvent;
import extras.lifecycle.common.Variable;

/**
 * 
 * Common utilities for management of checkpoints.
 * 
 * @author Mihail Mihaylov
 *
 */
public final class CheckpointUtils {
	
	/**
	 * A private constructor to adapt the standards for an utility class.
	 */
	private CheckpointUtils() {
		super();
	}
	
	/**
	 * Map between an observed object and its record.
	 */
	private static Map<Object, MonitorImpl> monitors;
	
	/**
	 * Creates a checkpoint for a specific object.
	 * @param thisObject is the object, which will be checkpoint-ed
	 */
	public static synchronized void checkpoint(Object thisObject) {
		if (monitors == null)
			return;
	
		MonitorImpl monitor = monitors.get(thisObject);
		
		if (monitor != null) 
			monitor.fireCheckpoint();
	}
	
	
	/**
	 * Generates a <code>String</code> from a list of checkpoints. This method has testing purposes.
	 * 
	 * @param checkpoints which will be used for the output string
	 * @return a String
	 */
//	public static String getCheckpointsAsString(List<Checkpoint> checkpoints) {
//		StringBuffer sb = new StringBuffer();
//		sb.append('[');
//		
//		for (Checkpoint cp : checkpoints) {
//			sb.append(cp);
//			sb.append("\n");
//		}
//		
//		sb.append(']');
//		return sb.toString();
//	}
	
//	public static void dumpCheckpoints(List<Checkpoint> checkpoints) {
//		System.out.println(getCheckpointsAsString(checkpoints));
//	}

  /**
   * Creates a checkpoint event on the given object
   * 
   * @param thisObject the basic object
   * @param event which will be dumped
   */
	public static synchronized void checkpointEvent(Object thisObject, CheckpointedEvent event) {
		if (monitors == null)
			return;
	
		MonitorImpl recordForObj = monitors.get(thisObject);
		
		if (recordForObj != null) 
			recordForObj.fireEvent(event);
	}
	
	
	public static synchronized void checkpointEvent(Object thisObject, String eventName, List<Variable> variables) {
		if (monitors == null)
			return;
		
		CheckpointedEvent event = new CheckpointedEvent();
		event.setName(eventName);

		for (Variable variable : variables) {
			event.set(variable.getName(), variable.getValue());
		}
		
		checkpointEvent(thisObject, event);
	}
	
	public static synchronized void checkpointEvent(Object thisObject, String eventName, Variable... variables) {
		if (monitors == null)
			return;
		
		List<Variable> listOfVariables = new ArrayList<Variable>(variables.length);
		Collections.addAll(listOfVariables, variables);
		checkpointEvent(thisObject, eventName, listOfVariables);
	}
	
	protected static void removeMonitor(Monitor monitor) {
		if (monitors != null)
			monitors.remove(monitor.getObservedObject());
	}

	public static Monitor getMonitor(Object thisObject) throws MonitorException {
		
		MonitorImpl monitor = null;
		// Try to find a monitor in our map
		if (monitors != null) {
			monitor = monitors.get(thisObject);
		} else {
			if (monitors == null)
				monitors = new WeakHashMap<Object, MonitorImpl>();
		}
		
		// If there is no monitor for this object
		if (monitor == null) {
			// we create it
			monitor = new MonitorImpl(thisObject);
			// and add it to the map
			monitors.put(monitor.getObservedObject(), monitor);
		}
		
		return monitor;
	}

}
