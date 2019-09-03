package extras.animalsense.ui.show;

import java.util.ArrayList;
import java.util.List;

import extras.animalsense.evaluate.Question;
import extras.lifecycle.common.Event;
import extras.lifecycle.common.Variable;

public class SetUpVariablesEvent implements Event {
	
	/**
	 * 
	 */
//	private static final long serialVersionUID = 1L;
	
	Question question;
	List<Variable> variables;
	
	public SetUpVariablesEvent(Question question, List<Variable> variables) {
		super();
		this.question = question;
		this.variables = variables;
	}
	
	public SetUpVariablesEvent(Question question) {
		this(question, new ArrayList<Variable>(question.getInputVariables().size()));
	}
	
	/**
	 * @return the question
	 */
	public Question getQuestion() {
		return question;
	}
	/**
	 * @param Question the question to set
	 */
	public void setQuestion(Question Question) {
		this.question = Question;
	}
	/**
	 * @return the variables
	 */
	public List<Variable> getVariables() {
		return variables;
	}
	/**
	 * @param variables the variables to set
	 */
	public void setVariables(List<Variable> variables) {
		this.variables = variables;
	}
	
	public void addVariable(Variable v) {
		this.variables.add(v);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SetUpVariablesEvent [question=" + question.getQuestionId() + ", variables=" + variables + "]";
	}
	
	
	
	
}
