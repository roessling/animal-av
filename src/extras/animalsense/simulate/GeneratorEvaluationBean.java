package extras.animalsense.simulate;

import extras.lifecycle.common.Record;
import extras.lifecycle.query.Result;

public class GeneratorEvaluationBean extends GeneratorSimulationBean {
	
	Result result;

	public GeneratorEvaluationBean(Record record, String script, Result result) {
		super(record, script);
		this.result = result;
	}

	/**
	 * @return the result
	 */
	public Result getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(Result result) {
		this.result = result;
	}
	
	

}
