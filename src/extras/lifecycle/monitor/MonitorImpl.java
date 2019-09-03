/**
 * 
 */
package extras.lifecycle.monitor;

import java.lang.ref.WeakReference;

import extras.lifecycle.monitor.Monitor;
import extras.lifecycle.monitor.MonitorException;
import extras.lifecycle.monitor.MonitorListener;

import extras.lifecycle.checkpoint.Checkpoint;
import extras.lifecycle.checkpoint.CheckpointedEvent;
import extras.lifecycle.common.AbstractObservable;
import extras.lifecycle.common.Record;

/**
 * @author Mihail Mihaylov
 * 
 */
public class MonitorImpl extends AbstractObservable<MonitorListener> implements Monitor {

	private WeakReference<? extends Object> refToObsObject;
	private Record record;
	private boolean recording;

	/**
	 * @param clazz
	 * @param observedObject
	 * @throws MonitorException
	 */
	protected MonitorImpl(Object observedObject) throws MonitorException {
		super(MonitorListener.class);

		if (observedObject == null)
			throw new MonitorException("Observed object can not be null.");
		
		this.refToObsObject = new WeakReference<Object>(observedObject);
		this.recording = false;
	}

	/* (non-Javadoc)
	 * @see extras.lifecycle.monitor.Monitor#startRecording()
	 */
	public void startRecording() throws MonitorException {
		Object observedObject = refToObsObject.get();
		if (observedObject == null)
			throw new MonitorException("Observed object can not be null.");
		
		record = new Record(observedObject);
		this.recording = true;
	}

	/* (non-Javadoc)
	 * @see extras.lifecycle.monitor.Monitor#stopRecording()
	 */
	public void stopRecording() {
		this.recording = false;
	}

	/* (non-Javadoc)
	 * @see extras.lifecycle.monitor.Monitor#getObservedObject()
	 */
	public Object getObservedObject() {
		return refToObsObject.get();
	}

	/* (non-Javadoc)
	 * @see extras.lifecycle.monitor.Monitor#getRecord()
	 */
	public Record getRecord() {
		return record;
	}

	synchronized void fireCheckpoint() {
		if (recording) {
			Checkpoint cp = record.checkpoint();

			for (MonitorListener l : listenerList.getListeners(MonitorListener.class)) {
				l.onCheckpoint(cp);
			}
		}
	}

	synchronized void fireEvent(CheckpointedEvent event) {
		if (recording) {
			record.addEvent(event);

			for (MonitorListener l : listenerList.getListeners(MonitorListener.class)) {
				l.onEvent(event);
			}
		}
	}

}
