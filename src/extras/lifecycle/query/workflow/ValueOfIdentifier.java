package extras.lifecycle.query.workflow;

import javax.xml.bind.annotation.XmlRootElement;

import extras.lifecycle.query.workflow.Calculator;

import extras.lifecycle.query.Knowledge;

@XmlRootElement(name = "Value")
public class ValueOfIdentifier extends Calculator {
	
	private String variableName;
	
	/**
	 * 
	 */
	public ValueOfIdentifier() {
		super();
	}

	/**
	 * @param variableName
	 */
	public ValueOfIdentifier(String variableName) {
		this();
		this.variableName = variableName;
	}

	/* (non-Javadoc)
	 * @see extras.lifecycle.checkpoint.evaluator.box.calc.Calculator#calculate(extras.lifecycle.checkpoint.evaluator.EvaluatorKnowledge)
	 */
	@Override
	public Object calculate(Knowledge knowledge) {
		Object valueOf = knowledge.getVariables().get(variableName);
		return valueOf;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[" + variableName + "]";
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
	
}
