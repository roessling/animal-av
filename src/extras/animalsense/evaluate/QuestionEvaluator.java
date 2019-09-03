package extras.animalsense.evaluate;

import java.util.List;

import extras.animalsense.simulate.GeneratorEvaluationBean;
import extras.lifecycle.common.Variable;
import extras.lifecycle.query.EvaluationMode;
import extras.lifecycle.query.Result;

public interface QuestionEvaluator {

	public abstract Result setUpAndEvaluate(Exercise exercise, Question question, List<Variable> variables, EvaluationMode evaluationMode);
	 
	public abstract GeneratorEvaluationBean setUpEvaluateAndVisualize(Exercise exercise, Question question, List<Variable> variables);

	public abstract Result setUpAndEvaluate(Exercise exercise, Question question, List<Variable> variables);

	public void addListener(EvaluatorListener listener);

	public void removeListener(EvaluatorListener listener);
}