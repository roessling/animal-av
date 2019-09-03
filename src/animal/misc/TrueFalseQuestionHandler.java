package animal.misc;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.ScrollPaneConstants;

import translator.AnimalTranslator;
import translator.TranslatableGUIElement;

public class TrueFalseQuestionHandler implements ActionListener,
		TrueFalseQuestionInterface {
	// private boolean answerIsCorrect = false;

	private String questionText = null;

	private JFrame questionFrame = null;

	private JToggleButton falseButton = null;

	private JToggleButton preselectedButton = null;

	private JToggleButton trueButton = null;

	public TrueFalseQuestionHandler() {
		// do nothing
	}

	public Component getComponent() {
		return questionFrame;
	}

	public void MakePanel() {
		TranslatableGUIElement guiBuilder = AnimalTranslator.getGUIBuilder();
		questionFrame = guiBuilder.generateJFrame("tfQuestion");
		questionFrame.getContentPane().setLayout(new BorderLayout());

		trueButton = guiBuilder.generateJToggleButton("tfTrue", 
				null, null, true);
		falseButton = guiBuilder.generateJToggleButton("tfFalse", 
				null, null, true);
		preselectedButton = guiBuilder.generateJToggleButton("preselected", 
				null, null, true);

		// trueButton.addActionListener(this);
		// falseButton.addActionListener(this);

		ButtonGroup aButtonGroup = new ButtonGroup();
		aButtonGroup.add(trueButton);
		aButtonGroup.add(falseButton);
		aButtonGroup.add(preselectedButton);
		preselectedButton.setSelected(true);
		JTextArea textArea = new JTextArea(questionText);
		JScrollPane p = new JScrollPane(textArea,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		textArea.setEditable(false);
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		questionFrame.getContentPane().add(p, BorderLayout.CENTER);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(2, 2));

		// buttonPanel.setLayout(new GridLayout(2, 1));
		buttonPanel.add(trueButton);
		buttonPanel.add(falseButton);

		AbstractButton okButton = guiBuilder.generateJButton("submit");
		okButton.addActionListener(this);
		buttonPanel.add(okButton);
		AbstractButton cancelButton = guiBuilder.generateJButton("cancel");
		cancelButton.addActionListener(this);
		buttonPanel.add(cancelButton);

		questionFrame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		questionFrame.pack();
		// questionFrame.setVisible(true);
	}

	/**
	 * Store whether the prepared answer is correct
	 * 
	 * @param isCorrect
	 *          if true, the question is true; otherwise, the correct answer would
	 *          be "false"
	 */
	public void SetAnswer(boolean isCorrect) {
//		answerIsCorrect = isCorrect;
	}

	/**
	 * Store the question text for use in the panel generation
	 * 
	 * @param aQuestionText
	 *          the text prompt to be displayed, which can be answered with either
	 *          "true" or "false"
	 */
	public void SetQuestion(String aQuestionText) {
		questionText = aQuestionText;
	}

	public void SetText(String aText) {
		SetQuestion(aText);
	}

	public void reset() {
		if (preselectedButton != null)
			preselectedButton.setSelected(true);
	}

	public void actionPerformed(ActionEvent theEvent) {
		MessageDisplay.message(theEvent.getActionCommand().toString());
	}
}
