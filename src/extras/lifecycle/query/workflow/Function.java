package extras.lifecycle.query.workflow;

import java.util.ArrayList;
import java.util.List;

import extras.lifecycle.query.workflow.Calculator;

public abstract class Function extends Calculator {
	
	protected List<Object> arguments;
	
	/**
	 * 
	 */
	protected Function() {
		this(new ArrayList<Object>());
	}

	/**
	 * @param arguments
	 */
	protected Function(List<Object> arguments) {
		super();
		this.arguments = arguments;
	}

	/**
	 * @param arguments
	 *            the arguments to set
	 */
	public void setArguments(List<Object> arguments) {
		this.arguments = arguments;
	}

	/**
	 * @return the arguments
	 */
	public List<Object> getArguments() {
		return arguments;
	}
	
	

	
}
