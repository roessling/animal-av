package extras.lifecycle.query.workflow;

import javax.xml.bind.annotation.XmlRootElement;

import extras.lifecycle.query.workflow.Calculator;

import extras.lifecycle.query.Knowledge;

@XmlRootElement(name = "Constant")
public class ConstantValue extends Calculator {
	
	private Object value;

	/**
	 * 
	 */
	public ConstantValue() {
		super();
	}

	/**
	 * @param value
	 */
	public ConstantValue(Object value) {
		this();
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see extras.lifecycle.checkpoint.evaluator.box.calc.Calculator#calculate(extras.lifecycle.checkpoint.evaluator.EvaluatorKnowledge)
	 */
	@Override
	public Object calculate(Knowledge knowledge) {
		return value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[" + value + "]";
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}
	
	
	
}
