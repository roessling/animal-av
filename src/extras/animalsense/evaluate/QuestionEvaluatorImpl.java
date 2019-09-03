package extras.animalsense.evaluate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import algoanim.primitives.Graph;

import extras.animalsense.simulate.AnimalGeneratorController;
import extras.animalsense.simulate.GeneratorEvaluationBean;
import extras.animalsense.simulate.GeneratorSimulationBean;
import extras.animalsense.simulate.SimulationException;
import extras.lifecycle.checkpoint.Checkpoint;
import extras.lifecycle.common.AbstractObservable;
import extras.lifecycle.common.Event;
import extras.lifecycle.common.PropertiesBean;
import extras.lifecycle.common.Record;
import extras.lifecycle.common.SerializingUtils;
import extras.lifecycle.common.Variable;
import extras.lifecycle.monitor.MonitorListener;
import extras.lifecycle.query.EvaluationMode;
import extras.lifecycle.query.Evaluator;
import extras.lifecycle.query.EvaluatorImpl;
import extras.lifecycle.query.Result;
import extras.lifecycle.query.ShortResult;
import extras.lifecycle.query.workflow.Box;
import gfgaa.gui.parser.GraphReader;

public class QuestionEvaluatorImpl extends
		AbstractObservable<EvaluatorListener> implements QuestionEvaluator,
		MonitorListener {

	public QuestionEvaluatorImpl() {
		super(EvaluatorListener.class);
	}

	/*
	 * 
	 */
	private Result evaluate(Question question, Record record,
			EvaluationMode evaluationMode) {
		assert question != null;
		if (!question.applicableForRecord(record))
			return Result.notApplicable();

		Box box = question.getDecisionBox();
		Evaluator evaluator = new EvaluatorImpl();
		return evaluator.evaluate(box, record, evaluationMode);
	}

	@Override
	public Result setUpAndEvaluate(Exercise exercise, Question question,
			List<Variable> variables) {
		return setUpAndEvaluate(exercise, question, variables,
				EvaluationMode.Run);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * extras.animalsense.evaluate.QuestionEvaluator#setUpAndEvaluate(extras
	 * .animalsense.evaluate.Exercise, extras.animalsense.evaluate.Question,
	 * java.util.List, extras.lifecycle.query.EvaluationMode)
	 */
	@Override
	public Result setUpAndEvaluate(Exercise exercise, Question question,
			List<Variable> variables, EvaluationMode evaluationMode) {
		return setUpEvaluate(exercise, question, variables, evaluationMode)
				.getResult();
	}

	private GeneratorEvaluationBean setUpEvaluate(Exercise exercise,
			Question question, List<Variable> variables,
			EvaluationMode evaluationMode) {
		AnimalGeneratorController animalgeneratorController = new AnimalGeneratorController();

		// First we set up the variables in the question
		// Expect an exception if some of the values are not allowed
		PropertiesBean variablesBean = setUpVariablesAsBean(exercise, question,
				variables);

		// Second we run the simulation
		GeneratorSimulationBean ags;
    try {
      ags = animalgeneratorController.executeSimulation(exercise.getGeneratorName(), exercise.getChainPath(),  variablesBean, this);
 
		Record record = ags.getRecord();
		Result result;
		if (record != null) {
			// We set the variables in the record
			record.setVariables(variablesBean);

			// Evaluate the question on the simulation results
			result = evaluate(question, record, evaluationMode);
		} else {
			result = new Result(ShortResult.UNKNOWN,
					"Unable to simulate the algorithmus: "
							+ exercise.getGeneratorName());
		}

		return new GeneratorEvaluationBean(record, ags.getScript(), result);
    } catch (SimulationException e) {
      e.printStackTrace();
    }
    
    return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * extras.animalsense.evaluate.Evaluator#setUpAndEvaluate(extras.animalsense
	 * .evaluate.Exercise, extras.animalsense.evaluate.Question, java.util.List,
	 * extras.lifecycle.common.MonitorListener,
	 * extras.lifecycle.common.EvaluationMode)
	 */
	@Override
	public GeneratorEvaluationBean setUpEvaluateAndVisualize(Exercise exercise,
			Question question, List<Variable> variables) {
		return setUpEvaluate(exercise, question, variables,
				EvaluationMode.Animate);
	}

	private PropertiesBean setUpVariablesAsBean(Exercise exercise,
			Question question, List<Variable> variables) {
		PropertiesBean variablesBean = new PropertiesBean();

		for (Variable variable : setUpVariables(exercise, question, variables)) {
			variablesBean.set(variable.getName(), variable.getValue());

		}
		return variablesBean;
	}

	private List<Variable> setUpVariables(Exercise exercise, Question question,
			List<Variable> variables) {
		Map<String, Variable> variablesMap = new HashMap<String, Variable>();

		// First we add all variable from the exercise
		for (Variable variable : exercise.getInitialVariables()) {
			variablesMap.put(variable.getName(), setUpVariable(variable));
		}

		// Then we add the variables from the question
		for (Variable variable : question.getVariables()) {
			// This automatically replaces the values
//			variablesMap.put(variable.getName(), variable);
			variablesMap.put(variable.getName(), setUpVariable(variable));
		}

		// We add the dynamic variables
		for (Variable variable : variables) {
			// This automatically replaces the values
			//variablesMap.put(variable.getName(), variable);
			variablesMap.put(variable.getName(), setUpVariable(variable));
		}

		return new ArrayList<Variable>(variablesMap.values());
	}

	private Variable setUpVariable(Variable variable) {
		String name = variable.getName();
		Object value = variable.getValue();

		String val = null;
		if (value != null && value instanceof String)
		  val = (String)value;
		// check for special forms: arrays and graphs
		if ("array".equals(name) || 
		    (val != null && val.startsWith("[") && val.endsWith("]"))) { 
		    value = QuestionEvaluatorImpl.createArrayFromString(val);
		} else if (val != null && val.startsWith("%graphscript")) {
		  value = QuestionEvaluatorImpl.createGraphFromString(val);
		} else
			value = SerializingUtils.getClone(value);
		return new Variable(name, value);
	}

	@Override
	public void onCheckpoint(Checkpoint checkpoint) {
		fireOnMessage("CP: " + checkpoint.toString());
	}

	@Override
	public void onEvent(Event event) {
		fireOnMessage("EV: " + event.toString());
	}

	public synchronized void fireOnMessage(String message) {
		for (EvaluatorListener l : listenerList
				.getListeners(EvaluatorListener.class)) {
			l.onMessage(message);
		}
	}

	protected static int[] createArrayFromString(String arrayString) {
		if (arrayString == null)
			return null;
		String processString = null;
		int start = 0, end = arrayString.length();
		if (arrayString.startsWith("["))
		  start = 1;
		if (arrayString.endsWith("]"))
		  end--;
		processString = arrayString.substring(start, end);
		StringTokenizer stok = new StringTokenizer(processString, ", ");
		int[] tmp = new int[processString.length()];
		int pos = 0;
		while (stok.hasMoreTokens()) {
			String token = stok.nextToken();
			try {
				Integer i = Integer.parseInt(token);
				tmp[pos++] = i.intValue();
			} catch (NumberFormatException nfe) {
				System.err.println(token + " could not be coded as an int");
			}
		}
		int[] result = new int[pos];
		System.arraycopy(tmp, 0, result, 0, pos);
		return result;
	}

	protected static Graph createGraphFromString(String graphScriptString) {
    GraphReader gr = new GraphReader("no file");
    Graph graph = gr.readGraph(graphScriptString, false);
    return graph;
	}
}
