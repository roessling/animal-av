package extras.animalsense.evaluate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import extras.lifecycle.common.Record;
import extras.lifecycle.common.Variable;
import extras.lifecycle.query.function.Ok;
import extras.lifecycle.query.workflow.AbstractBox;

@XmlRootElement
public class Question implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6189389391030843090L;
	/**
	 */
	private String questionId;

	/**
	 */
	private String questionText;

	/**
	 * 
	 */
	private String script;

	/**
	 * 
	 */
	private AbstractBox decisionBox;

	private List<Variable> variables;

	/**
	 * 
	 */
	public Question() {
		super();
	}

	public Question(String questionText, String script, AbstractBox decisionBox) {
		this(null, questionText, script, decisionBox);
	}
	
	/**
	 * @param questionId
	 * @param questionText
	 * @param script
	 * @param decisionBox
	 */
	public Question(String questionId, String questionText, String script, AbstractBox decisionBox) {
		super();
		init(questionId, questionText, script, decisionBox);
	}
	
	private void init(String questionId, String questionText, String script, AbstractBox decisionBox) {
		this.questionId = questionId;
		this.questionText = questionText;
		this.script = script;
		this.decisionBox = decisionBox;
		checkId();
	}

	public void checkId() {
		if (this.questionId == null) {
			// Ensure the ID is unique
			this.questionId = Question.class.getSimpleName()
					+ this.questionText.hashCode() + System.currentTimeMillis();
		}
	}

	/**
	 * Defines if the <code>Question</code> can be applied to certain record.
	 * 
	 * @param record
	 *            , which will be checked
	 * @return true, if the <code>Question</code> can be applied to the given
	 *         record
	 */
	public boolean applicableForRecord(Record record) {
		return true;
	}

	/**
	 * @return the questionText
	 */
	public String getQuestionText() {
		return questionText;
	}

	/**
	 * @param questionText
	 *            the questionText to set
	 */
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Question: " + questionText;
	}

	/**
	 * @return the questionId
	 */
	public String getQuestionId() {
		return questionId;
	}

	/**
	 * @param questionId
	 *            the questionId to set
	 */
	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public List<Variable> getInputVariables() {
		if ((decisionBox == null) || (decisionBox.getInputVariables() == null))
			return new ArrayList<Variable>();
		return decisionBox.getInputVariables();
	}

	public void setInputVariables(List<Variable> variables) {
		if (decisionBox == null)
			System.err
					.println("Unable to set initial variables. Decision box is not initialized.");
		else
			decisionBox.setInputVariables(variables);
	}

	/**
	 * @return the decisionBox
	 */
	public AbstractBox getDecisionBox() {
		return decisionBox;
	}

	/**
	 * @param decisionBox
	 *            the decisionBox to set
	 */
	public void setDecisionBox(AbstractBox decisionBox) {
		this.decisionBox = decisionBox;
	}

	/**
	 * @return the script
	 */
	public String getScript() {
		return script;
	}

	/**
	 * @param script
	 *            the script to set
	 */
	public void setScript(String script) {
		this.script = script;
	}

	/**
	 * @return the variables
	 */
	public List<Variable> getVariables() {
		if (variables == null)
			return new ArrayList<Variable>();
		return variables;
	}

	/**
	 * @param variables
	 *            the variables to set
	 */
	public void setVariables(List<Variable> variables) {
		this.variables = variables;
	}
	
	public void generateDefault() {
		Ok ok = new Ok();
		ok.addInputVariable(new Variable("x"));
		init(null, "New Question", "// No script\nOk();", ok);
	}
}
