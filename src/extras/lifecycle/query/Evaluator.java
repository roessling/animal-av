package extras.lifecycle.query;

import extras.lifecycle.query.EvaluationMode;
import extras.lifecycle.query.Result;
import extras.lifecycle.common.Record;
import extras.lifecycle.query.workflow.Box;

public interface Evaluator {

	public abstract Result evaluate(Box box, Record record, EvaluationMode evaluationMode);

}