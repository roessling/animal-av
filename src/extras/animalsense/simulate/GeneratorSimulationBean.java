package extras.animalsense.simulate;

import extras.lifecycle.common.Record;

public class GeneratorSimulationBean {
	
	Record record;
	String script;
	/**
	 * @param record
	 * @param script
	 */
	GeneratorSimulationBean(Record record, String script) {
		super();
		this.record = record;
		this.script = script;
	}
	/**
	 * @return the record
	 */
	public Record getRecord() {
		return record;
	}
	/**
	 * @param record the record to set
	 */
	public void setRecord(Record record) {
		this.record = record;
	}
	/**
	 * @return the script
	 */
	public String getScript() {
		return script;
	}
	/**
	 * @param script the script to set
	 */
	public void setScript(String script) {
		this.script = script;
	}

	
}
