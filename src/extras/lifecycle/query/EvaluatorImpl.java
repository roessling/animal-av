/**
 * 
 */
package extras.lifecycle.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import extras.lifecycle.query.EvaluationMode;
import extras.lifecycle.query.Evaluator;
import extras.lifecycle.query.Knowledge;
import extras.lifecycle.query.QueryException;
import extras.lifecycle.query.Result;
import extras.lifecycle.query.ShortResult;

import extras.lifecycle.checkpoint.Checkpoint;
import extras.lifecycle.checkpoint.CheckpointedEvent;
import extras.lifecycle.common.PropertiesBean;
import extras.lifecycle.common.Record;
import extras.lifecycle.query.workflow.Box;

/**
 * @author Mihail Mihaylov
 *
 */
public class EvaluatorImpl implements Evaluator {
	
	/* (non-Javadoc)
	 * @see extras.lifecycle.query.Evaluator#evaluate(extras.lifecycle.query.workflow.Box, extras.lifecycle.common.Record, extras.lifecycle.query.EvaluationMode)
	 */
	public Result evaluate(Box box, Record record, EvaluationMode evaluationMode) {
		assert record != null;
		
		// Create instance so that they does not change by the evaluation
	    List<Checkpoint> checkpoints = new ArrayList<Checkpoint>(record.getCheckpoints());
	    Map<String, List<CheckpointedEvent>> events = new HashMap<String, List<CheckpointedEvent>>();
	    events.putAll(record.getEvents());
	    
	    // But we use the same variables thus they can be modified.
	    PropertiesBean variables = record.getVariables();
	    
	    Knowledge knowledge = new Knowledge(variables, checkpoints, events);
	    knowledge.setEvaluationMode(evaluationMode);
	    
	    Result result = new Result(ShortResult.NO, "Unable to decide.");
	    Box aBox = box;
	    while (aBox != null) {
	    	//System.err.println("Evaluating Box: " + box);
	    	try {
				aBox = aBox.evaluate(knowledge);
		     // GR: was "box = box.evaluate(knowledge);"
			} catch (QueryException e) {
				knowledge.addError("Error: " + e.getMessage());
				break;
			}
	    	
			if (knowledge.isDecisionMade()) {
				ShortResult shortResult = (knowledge.isSuccess()) ? ShortResult.YES : ShortResult.NO;
			    result = new Result(shortResult);
			    break;
			}
		}
	    
	    result.setData(knowledge.getResultData());
	    result.appendComment(knowledge.getComment());
		return result;
	}

}
