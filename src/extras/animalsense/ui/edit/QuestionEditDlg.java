/**
 * 
 */
package extras.animalsense.ui.edit;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import extras.animalsense.evaluate.EvaluatorListener;
import extras.animalsense.evaluate.Exercise;
import extras.animalsense.evaluate.Question;
import extras.animalsense.evaluate.QuestionCompiler;
import extras.animalsense.evaluate.QuestionEvaluator;
import extras.animalsense.evaluate.QuestionEvaluatorImpl;
import extras.animalsense.ui.show.QEventListener;
import extras.animalsense.ui.show.SetUpAndVisualize;
import extras.animalsense.ui.show.SetUpVariablesEvent;
import extras.lifecycle.common.Event;
import extras.lifecycle.common.Variable;
import extras.lifecycle.query.EvaluationMode;
import extras.lifecycle.query.Result;
/**
 * @author Mihail Mihaylov
 * 
 */
public class QuestionEditDlg extends QuestionEditJDlgBase implements
		ActionListener, QEventListener, EvaluatorListener {

	private Question question;
	private QuestionCompiler qc;
	private Exercise exercise;
	private Exercise tempExercise;
	private boolean saved;
	private QuestionEvaluator questionEvaluator;

	/**
	 * 
	 */
	private static final long serialVersionUID = -53139475781753108L;

	public QuestionEditDlg(Frame owner, Exercise exercise) {
		super(owner);
		this.exercise = exercise;
		this.qc = new QuestionCompiler();
		questionEvaluator = new QuestionEvaluatorImpl();
		questionEvaluator.addListener(this);
		this.setModal(true);
	}

	/**
	 * @return the question
	 */
	public Question getQuestion() {
		return question;
	}

	/**
	 * @param question
	 *            the question to set
	 */
	public void setQuestion(Question question) {
		this.question = question;
		saved = false;
		initControls();
	}

	public boolean edit(Question question) {
		setQuestion(question);
		this.setVisible(true);
		return saved;
	}

	public void initControls() {
		getQuestionTextArea().setText(question.getQuestionText());
		getScriptTextArea().setText(question.getScript());
		List<Variable> allVariables = new ArrayList<Variable>(question
				.getInputVariables());
		allVariables.addAll(question.getVariables());
		getVariablesTable().setVariables(allVariables);
		getVariablesTable().update();

		getScriptTextArea().addFocusListener(new FocusAdapter() {
			String lastChange = "";

			public void focusLost(FocusEvent evt) {
				String currentText = getScriptTextArea().getText();
				if (!lastChange.equals(currentText)) {
					preview();
					lastChange = currentText;
				}
			}

		});

		// Force preview
		preview();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ("close".equals(e.getActionCommand()))
			close();
		else if ("save".equals(e.getActionCommand()))
			save();
		else if ("compile".equals(e.getActionCommand()))
			compile();
		else if ("preview".equals(e.getActionCommand()))
			preview();
	}

	void preview() {
		// We do always compile first
		if (compile()) {
			// If compile is true, then workflow is surely not null
			getConsolePreview().clearText();

			tempExercise = new Exercise();
			tempExercise.generateDefault();
			tempExercise.setGeneratorName(exercise.getGeneratorName());
			tempExercise.setChainPath(exercise.getChainPath());
			tempExercise.setInitialVariables(exercise.getInitialVariables());

			String questionText = getQuestionTextArea().getText();
			Question tempQuestion = new Question(questionText, "", qc.getWorkflow());
			tempQuestion
					.setInputVariables(extractInputVariables(getVariablesTable()
							.getVariables()));
			tempQuestion
					.setVariables(extractOverrideVariables(getVariablesTable()
							.getVariables()));

			tempExercise.addQuestion(tempQuestion);

			getShowExercisePane().setTitle("Preview");
			getShowExercisePane().setEventHandler(this);
			ArrayList<Question> qList = new ArrayList<Question>();
			qList.add(tempQuestion);
			getShowExercisePane().setQuestions(qList);
			getShowExercisePane().pack();
			getShowExercisePane().setEnabled(true);
		} else {
			getShowExercisePane().setEnabled(false);
			output("Preview disabled.");
		}
	}

	private List<Variable> extractOverrideVariables(List<Variable> variables) {
		List<Variable> inputVariables = extractInputVariables(variables);
		List<Variable> overrideVariables = new ArrayList<Variable>(variables);

		overrideVariables.removeAll(inputVariables);
		return overrideVariables;
	}

	private List<Variable> extractInputVariables(List<Variable> variables) {
		List<Variable> inputVariables = new ArrayList<Variable>();
		for (Variable variable : variables) {
			// We assume that all variable without given values
			// are input variables e.g. the user should give values before
			// evaluation
			if ((variable.getValue() == null)
					|| (variable.getValue().toString().isEmpty()))
				inputVariables.add(variable);
		}
		return inputVariables;
	}

	private void close() {
		this.setVisible(false);
	}

	protected boolean compile() {
		boolean ok = false;
		output("Start compiling...");

		String script = getScriptTextArea().getText();
		qc.setScript(script);
		ok = qc.compile();
		output(qc.getOutput());

		if (ok)
			output("Compilation completed successfully.");
		else
			output("Compilation failed!");

		return ok;
	}

	protected void save() {
		String questionText = getQuestionTextArea().getText();
		if (questionText.isEmpty()) {
			showErrorMessage("Please set a text for the question before saving.");
			return;
		}
		String questionScript = getScriptTextArea().getText();
		if (questionScript.isEmpty()) {
			showErrorMessage("Please set a script for the question before saving.");
			return;
		}
		if (qc.getWorkflow() == null && !compile()) {
			showErrorMessage("Please compile the script first before saving.");
			return;
		}

		question.setQuestionText(questionText);
		question.setScript(questionScript);
		question.setDecisionBox(qc.getWorkflow());
		question.setInputVariables(extractInputVariables(getVariablesTable()
				.getVariables()));
		question.setVariables(extractOverrideVariables(getVariablesTable()
				.getVariables()));

		question.checkId();

		saved = true;

		close();
	}

	protected void output(String text) {
		getConsolePreview().appendText(text);
	}

	private void showErrorMessage(String msgText) {
		
		JOptionPane.showMessageDialog(this, msgText, this.getTitle(),
				JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public void onSetInputForQuestion(SetUpVariablesEvent qe) {
		if (qe instanceof SetUpAndVisualize) {
			JOptionPane.showMessageDialog(this, "Visualization is not available in editing mode.", this.getTitle(),
					JOptionPane.INFORMATION_MESSAGE);
		}
		// Clear the console
		getConsolePreview().clearText();
		
		// We set up the question and evaluate it
		Result result = questionEvaluator.setUpAndEvaluate(tempExercise, qe
				.getQuestion(), qe.getVariables(), EvaluationMode.Debug);
		getShowExercisePane().setComment(qe.getQuestion().getQuestionId(),
				result.toString());
	}

	@Override
	public void onMessage(String message) {
		output(message);
		
	}

	@Override
	public void onEvent(Event event) {
		// Do nothing
	}

}
