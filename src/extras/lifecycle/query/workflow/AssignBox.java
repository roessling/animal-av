package extras.lifecycle.query.workflow;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import extras.lifecycle.query.workflow.AbstractBox;
import extras.lifecycle.query.workflow.Box;
import extras.lifecycle.query.workflow.Calculator;

import extras.lifecycle.query.Knowledge;
import extras.lifecycle.query.QueryException;

/**
 * 
 * @author Mihail Mihaylov
 * 
 */
@XmlRootElement(name = "AssignBox")
@XmlType(
 propOrder = { "variableName", "expression" } 
) 
public class AssignBox extends AbstractBox {

	/**
	 * A calculator instance to perform the actual calculation.
	 */
	private Calculator expression;

	/**
	 * In the variable with this name will be created in the knowledge to store
	 * the result of the calculation.
	 */
	private String variableName;
	
	/**
	 * 
	 */
	public AssignBox() {
		super();
	}

	/**
	 * Construct a <code>CalculateBox</code> by a given variableName and calculator.
	 * @param variableName name of the variable, which will be created in the knowledge
	 * @param calculator function used to calculate the value of the variable
	 */
	public AssignBox(String variableName, Calculator calculator) {
		super();
		this.variableName = variableName;
		this.expression = calculator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * extras.lifecycle.checkpoint.evaluator.box.Box#evaluate(extras.lifecycle.checkpoint.evaluator.EvaluatorKnowledge)
	 */
	@Override
	public Box evaluate(Knowledge knowledge) throws QueryException {
		Object calculation = expression.calculate(knowledge);
		knowledge.getVariables().set(variableName, calculation);

		return getNext();
	}

	/**
	 * @return the calculator
	 */
	public Calculator getExpression() {
		return expression;
	}

	/**
	 * @param calculator the calculator to set
	 */
	public void setExpression(Calculator calculator) {
		this.expression = calculator;
	}

	/**
	 * @return the variableName
	 */
	public String getVariableName() {
		return variableName;
	}

	/**
	 * @param variableName the variableName to set
	 */
	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Assign [" + variableName + "=" + expression + "]";
	}
	
	

}
