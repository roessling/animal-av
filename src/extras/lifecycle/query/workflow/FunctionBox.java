/**
 * 
 */
package extras.lifecycle.query.workflow;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import extras.lifecycle.query.workflow.Box;
import extras.lifecycle.query.workflow.Calculator;
import extras.lifecycle.query.workflow.Function;

import extras.lifecycle.query.Knowledge;
import extras.lifecycle.query.QueryException;

/**
 * @author Mihail Mihaylov
 *
 */
@XmlRootElement(name = "FunctionBox")
@XmlType(
 propOrder = { "function", "arguments" } 
) 
public class FunctionBox extends Calculator {
	
	private Function function;
	private List<Calculator> arguments;
	
	/**
	 * 
	 */
	public FunctionBox() {
		this(null, new ArrayList<Calculator>());
	}
	
	/**
	 * @param function
	 * @param arguments
	 */
	public FunctionBox(Function function, List<Calculator> arguments) {
		super();
		this.function = function;
		this.arguments = arguments;
	}

	/* (non-Javadoc)
	 * @see extras.lifecycle.query.workflow.Box#evaluate(extras.lifecycle.common.Knowledge)
	 */
	@Override
	public Box evaluate(Knowledge knowledge) throws QueryException {
		// We first calculate
		calculate(knowledge);
		// And then return simply the next box
		return getNext();
	}

	/* (non-Javadoc)
	 * @see extras.lifecycle.query.workflow.Calculator#calculate(extras.lifecycle.common.Knowledge)
	 */
	@Override
	public Object calculate(Knowledge knowledge) throws QueryException{
		if (function == null)
			return null;
		
		List<Object> evalArguments = new ArrayList<Object>(arguments.size());
		
		// Evaluate all arguments to values.
		for (Calculator argCalculator : arguments) {
			Object argValue = argCalculator.calculate(knowledge);
			evalArguments.add(argValue);
		}
		
		function.setArguments(evalArguments);
		Object result = function.calculate(knowledge);
		
		// We need this to prevent the XML Serializer to save the calculated arguments
		function.setArguments(null);
		return result;
	}

	/**
	 * @return the arguments
	 */
	public List<Calculator> getArguments() {
		return arguments;
	}

	/**
	 * @param arguments the arguments to set
	 */
	public void setArguments(List<Calculator> arguments) {
		this.arguments = arguments;
	}

	/**
	 * @return the function
	 */
	public Function getFunction() {
		return function;
	}

	/**
	 * @param function the function to set
	 */
	public void setFunction(Function function) {
		this.function = function;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String funcName = (function == null) ? "null" : function.getClass().getSimpleName();
		return "Function [" + funcName + "(" + arguments + ")]";
	}
	
}
