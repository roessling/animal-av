package extras.lifecycle.query.workflow;

import java.util.LinkedList;
import java.util.List;

import extras.lifecycle.query.workflow.AbstractBox;
import extras.lifecycle.query.workflow.Box;

import extras.lifecycle.common.Variable;

/**
 * This is the initial implementation of the <code>Box</code> interface.
 * 
 * @author Mihail Mihaylov
 *
 */
public abstract class AbstractBox implements Box {
	
	/**
	 * List of input variables, which are required by this <code>Box</code>.
	 */
	private List<Variable> inputVariables;
	
	/**
	 * Default constructor.
	 */
	public AbstractBox() {
		super();
		inputVariables = new LinkedList<Variable>();
	}

	/**
	 * A single variable, which refers to the next (successor) box.
	 */
	private AbstractBox next;

	/**
	 * Gets the box, which should be executed after this box.
	 * @return the box, which should be executed after this box
	 */
	public AbstractBox getNext() {
		return next;
	}

	/**
	 * Sets the box, which should be executed after this box.
	 * @param next sets this box a successor box
	 */
	public void setNext(AbstractBox next) {
		this.next = next;
	}

	/* (non-Javadoc)
	 * @see extras.lifecycle.checkpoint.evaluator.box.Box#getInputVariables()
	 */
	@Override
	public List<Variable> getInputVariables() {
		return inputVariables;
	}

	/**
	 * Adds a single variable to the list of input variables.
	 * @param variable which will be added
	 */
	public void addInputVariable(Variable variable) {
		inputVariables.add(variable);
	}

	/**
	 * Set the list of input variables. These are variables, which should be set, to ensure
	 * that the box will run correctly.
	 * @param inputVariables which will be set
	 */
	public void setInputVariables(List<Variable> inputVariables) {
		this.inputVariables = inputVariables;
	}
	
	
		

}
