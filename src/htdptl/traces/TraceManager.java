package htdptl.traces;

import htdptl.ast.AST;
import htdptl.exceptions.StepException;
import htdptl.filter.IFilter;
import htdptl.stepper.Stepper;
import htdptl.visitors.NormalformVisitor;
import htdptl.visitors.VisitorUtil;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * The TraceManager creates the Trace using the Stepper. Filters are applied while creating the Trace. 
 *
 */
public class TraceManager {

	private Stepper stepper;
	public AbstractMap<Integer, TraceStep> traces = new TreeMap<Integer, TraceStep>();

	public int step;
	private int lastStep;
	private NormalformVisitor pv;
	private AST currentDefinition;
	private ArrayList<IFilter> filters = new ArrayList<IFilter>();

	public TraceManager(Stepper stepper) {
		this.stepper = stepper;
		pv = stepper.newPrimitiveVisitor();
	}

	public void buildTrace() throws StepException {
		step = 0;
		while (!stepper.isDone()) {	
			stepper.updateRedex();
			saveTraceStep();
			stepper.evalRedex();
			saveEvaluatedRedex();
			step++;
		}
		saveTraceStep();
		lastStep = step;		
	}

	private void saveTraceStep() {
		for (Iterator<IFilter> iterator = filters.iterator(); iterator.hasNext();) {
			IFilter filter = iterator.next();
			if (filter.skip()) {
				return;
			}
			
		}
		TraceStep trace = new TraceStep(stepper.getAST(), pv);
		currentDefinition = stepper.getCurrentDefinition();
		trace.setDefinition(currentDefinition);	
		traces.put(new Integer(step), trace);
	}
	
	private void saveEvaluatedRedex() {
		if (getCurrentTrace()!=null) {
			String operator = VisitorUtil.toCode(getCurrentTrace().getRedex().getOperator());
			if (stepper.isProcedure(operator)) {
				getCurrentTrace().setResolvedBody(stepper.getResolvedBody());
				getCurrentTrace().setSubstitutedExpressions(stepper.getSubstitutedExpressions());
			} else {
				getCurrentTrace().setRedexValue(stepper.getEvaluatedRedex());
			}
		}
				
	}
	
	


	/**
	 * resets this trace manager to step 0
	 */
	public void reset() {
		step = 0;
	}

	public TraceStep getCurrentTrace() {
		return traces.get(step);
	}

	public void next() {
		step++;
		while (!traces.containsKey(step) && step <= lastStep) {
			step++;
		}
	}

	public boolean done() {
		return step >= lastStep;
	}

	public TraceStep getPreviousTrace() {
		return traces.get(step - 1);
	}

	public TraceStep getNextTrace() {
		return traces.get(step + 1);
	}

	@Override
	public String toString() {
		String result = "";
		for (Iterator<Integer> iterator = traces.keySet().iterator(); iterator
				.hasNext();) {
			Integer key = iterator.next();
			result += "Step " + key + ":\n";
			result += traces.get(key) + "\n";
		}
		return result;
	}

	public boolean isNextStepFiltered() {
		return traces.get(step + 1) == null;
	}

	public int getLastStep() {
		return lastStep;
	}

	public int getStep() {
		return step;
	}

	public int numSteps() {
		return traces.size();
	}

	

	
	
	public void addFilter(IFilter filter) {
		stepper.addStepObserver(filter);		
		filter.setStepper(stepper);		
		filters.add(filter);
	}

}
