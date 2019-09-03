package extras.animalsense.ui.show;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import extras.animalsense.evaluate.Question;
import extras.lifecycle.common.Variable;

public class ShowExercisePane extends BasicTextPane {

	/**
	 * default serial Nr
	 */
	private static final long serialVersionUID = 6120949648993740355L;

	QEventListener eventHandler;

	/**
	 * 
	 */
	public ShowExercisePane() {
		this(null);
	}

	public ShowExercisePane(QEventListener eventHandler) {
		super();
		this.eventHandler = eventHandler;
	}

	public void pack() {
		StyledDocument doc = getStyledDocument();
		try {
			doc.remove(0, doc.getLength());

			String twoLines = NEWLINE + NEWLINE;
			if (getTitle() != null && !getTitle().isEmpty())
				doc.insertString(doc.getLength(), getTitle() + NEWLINE, doc
						.getStyle(BasicStyle.TITLE.name()));
			if (getSubtitle() != null && !getSubtitle().isEmpty())
				doc.insertString(doc.getLength(), getSubtitle() + twoLines, doc
						.getStyle(BasicStyle.SUBTITLE.name()));
			if (getDescription() != null && !getDescription().isEmpty())
				doc.insertString(doc.getLength(), getDescription() + twoLines,
						doc.getStyle(BasicStyle.DESCRIPTION.name()));

			for (Question qi : getQuestions()) {
				addQuestionToDoc(qi);
			}

		} catch (BadLocationException e) {
			// e.printStackTrace();
			// This should never happen :)
		}

		// Scroll to the top
		setCaretPosition(0);
	}

	//
	// private void addQuestionStyleToDoc(Question qi) {
	// StyledDocument doc = getStyledDocument();
	//
	// Style regular = doc.getStyle(BasicStyle.REGULAR.name());
	// Style s = doc.addStyle(qi.getQuestionId(), regular);
	// StyleConstants.setAlignment(s, StyleConstants.ALIGN_CENTER);
	//
	// JPanel p = getQPanel(qi);
	// StyleConstants.setComponent(s, p);
	// }

	private void addQuestionToDoc(Question q) {

		StyledDocument doc = getStyledDocument();

		try {
			doc.insertString(doc.getLength(), q.getQuestionText(), doc
					.getStyle(BasicStyle.QUESTION.name()));
			doc.insertString(doc.getLength(), NEWLINE, doc
					.getStyle(BasicStyle.REGULAR.name()));

			// The following two lines add the input box to the text pane
			// addQuestionStyleToDoc(q);
			// doc.insertString(doc.getLength(), " ", doc.getStyle(q
			// .getQuestionId()));
			addComponentToDoc(q.getQuestionId(), generateQPanel(q));

			// doc.insertString(doc.getLength(), NEWLINE, doc
			// .getStyle(BasicStyle.REGULAR.name()));
			addComment(q.getQuestionId());
		} catch (BadLocationException e) {
			// This should never happen :)
		}
	}

	private JPanel generateQPanel(final Question question) {
		JPanel jp = new JPanel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void setEnabled(boolean enabled) {
				super.setEnabled(enabled);

				Component[] comps = this.getComponents();
				for (Component panelComp : comps) {
					panelComp.setEnabled(enabled);
				}
			}

		};

		final List<JTextField> textFields = new LinkedList<JTextField>();
		for (Variable iVariable : question.getInputVariables()) {
			// Create InputBox
			JTextField textField = new JTextField(10);
			textField.setName(iVariable.getName());

			JLabel textFieldLabel = new JLabel(iVariable.getName() + ": ");
			textFieldLabel.setLabelFor(textField);

			// add to the list of input boxes
			textFields.add(textField);

			jp.add(textFieldLabel);
			jp.add(textField);

		}

		if (eventHandler != null) {
			// Create button OK
			JButton button = new JButton();
			button.setText("OK");
			// button.setActionCommand(Question.getQuestionId());
			button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					SetUpVariablesEvent qe = new SetUpVariablesEvent(question);
					for (JTextField jTextField : textFields) {
						String value = jTextField.getText();
						String name = jTextField.getName();
						qe.addVariable(new Variable(name, value));
						// System.err.println("Variable added: " + name + "=" +
						// value);
					}

					eventHandler.onSetInputForQuestion(qe);
				}
			});
			// Add the button to the panel
			jp.add(button);
			
			// Create button Show me
			JButton showMeBtn = new JButton();
			showMeBtn.setText("Show me");
			// button.setActionCommand(Question.getQuestionId());
			showMeBtn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					SetUpAndVisualize qe = new SetUpAndVisualize(question);
					for (JTextField jTextField : textFields) {
						String value = jTextField.getText();
						String name = jTextField.getName();
						qe.addVariable(new Variable(name, value));
						// System.err.println("Variable added: " + name + "=" +
						// value);
					}

					eventHandler.onSetInputForQuestion(qe);
				}
			});

			// Add the button to the panel
			jp.add(showMeBtn);
		}
		jp.setMinimumSize(jp.getPreferredSize());
		

		return jp;
	}

	/**
	 * @return the eventHandler
	 */
	public QEventListener getEventHandler() {
		return eventHandler;
	}

	/**
	 * @param eventHandler
	 *            the eventHandler to set
	 */
	public void setEventHandler(QEventListener eventHandler) {
		this.eventHandler = eventHandler;
	}

}
