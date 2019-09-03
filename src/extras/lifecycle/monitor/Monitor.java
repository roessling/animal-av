package extras.lifecycle.monitor;

import extras.lifecycle.monitor.MonitorException;
import extras.lifecycle.monitor.MonitorListener;
import extras.lifecycle.common.Record;

public interface Monitor {

	public abstract void startRecording() throws MonitorException;

	public abstract void stopRecording();

	/**
	 * @return the observedObject
	 */
	public abstract Object getObservedObject();

	/**
	 * @return the record
	 */
	public abstract Record getRecord();
	
	public void addListener(MonitorListener monitorListener);

	 public void removeListener(MonitorListener monitorListener);

}