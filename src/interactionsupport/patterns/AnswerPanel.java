package interactionsupport.patterns;
/*
 * Created on 19.09.2008 by Bjoern Dasbach <dasbach@rbg.informatik.tu-darmstadt.de>
 */
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;


public class AnswerPanel extends QuestionPanel {
	protected boolean state;
	protected JLabel headline, answerL, commentL, pntsL, answerStateL, delAnswerL;
	protected JTextArea answerr, commentt;
	protected JScrollPane answer, comment;
	protected JSpinner pnts, delAnswerIndex;
	protected JComboBox<String> answerState;
	protected JButton saveAnswer, deleteAnswer;
	
	public AnswerPanel(PatternUpdate updater) {
		super(updater);
		
		headline = new JLabel("New Answer:");
		answerL = new JLabel("Answer:");
		commentL = new JLabel("Comment:");
		pntsL = new JLabel("Points:");
		answerStateL = new JLabel("Answer State:");
		delAnswerL = new JLabel("Delete Answer Number:");
		delAnswerL.setVisible(false);
		
		answerr = new JTextArea(3, 15);
		commentt = new JTextArea(3, 15);
		answer = new JScrollPane(answerr);
		comment = new JScrollPane(commentt);
		
		SpinnerModel model = new SpinnerNumberModel(0, -100, 100, 1);
        pnts = new JSpinner(model);
        JFormattedTextField tmp = ((JSpinner.DefaultEditor)pnts.getEditor()).getTextField();
        tmp.setColumns(8);
        
        SpinnerModel modelx = new SpinnerNumberModel(0, 0, 0, 1);
        delAnswerIndex = new JSpinner(modelx);
        JFormattedTextField tmpx = ((JSpinner.DefaultEditor)delAnswerIndex.getEditor()).getTextField();
        tmpx.setColumns(8);
        delAnswerIndex.setVisible(false);
        
        String[] state = { "True", "False" };
        answerState = new JComboBox<String>(state);
        answerState.setSelectedIndex(0);
        answerState.addActionListener(this);
        this.state = true;
        
        saveAnswer = new JButton("Save Answer");
        saveAnswer.setMnemonic(KeyEvent.VK_S);
        saveAnswer.addActionListener(this);
        
        deleteAnswer = new JButton("Delete Answer");
        deleteAnswer.setMnemonic(KeyEvent.VK_D);
        deleteAnswer.addActionListener(this);
        deleteAnswer.setVisible(false);
                
        // headline
        layout.putConstraint(SpringLayout.EAST, headline,  0, SpringLayout.EAST, uidL);
        layout.putConstraint(SpringLayout.NORTH, headline, 50, SpringLayout.SOUTH, points);
        // Answer
        layout.putConstraint(SpringLayout.EAST, answerL,  0, SpringLayout.EAST, uidL);
        layout.putConstraint(SpringLayout.NORTH, answerL, 10, SpringLayout.SOUTH, headline);
        layout.putConstraint(SpringLayout.WEST, answer,  0, SpringLayout.WEST, uid);
        layout.putConstraint(SpringLayout.NORTH, answer, 10, SpringLayout.SOUTH, headline);
        layout.putConstraint(SpringLayout.EAST, commentL,  0, SpringLayout.EAST, uidL);
        layout.putConstraint(SpringLayout.NORTH, commentL, 5, SpringLayout.SOUTH, answer);
        layout.putConstraint(SpringLayout.WEST, comment,  0, SpringLayout.WEST, uid);
        layout.putConstraint(SpringLayout.NORTH, comment, 5, SpringLayout.SOUTH, answer);
        layout.putConstraint(SpringLayout.WEST, pntsL,  30, SpringLayout.EAST, answer);
        layout.putConstraint(SpringLayout.NORTH, pntsL, 0, SpringLayout.NORTH, answer);
        layout.putConstraint(SpringLayout.EAST, pnts,  -15, SpringLayout.EAST, panel);
        layout.putConstraint(SpringLayout.NORTH, pnts, 0, SpringLayout.NORTH, pntsL);
        layout.putConstraint(SpringLayout.WEST, answerStateL,  0, SpringLayout.WEST, pntsL);
        layout.putConstraint(SpringLayout.NORTH, answerStateL, 15, SpringLayout.SOUTH, pnts);
        layout.putConstraint(SpringLayout.EAST, answerState,  -15, SpringLayout.EAST, panel);
        layout.putConstraint(SpringLayout.NORTH, answerState, 10, SpringLayout.SOUTH, pnts);
        // Save
        layout.putConstraint(SpringLayout.EAST, saveAnswer,  0, SpringLayout.EAST, uid);
        layout.putConstraint(SpringLayout.SOUTH, saveAnswer, 0, SpringLayout.SOUTH, applyButton);
        // Delete
        layout.putConstraint(SpringLayout.EAST, deleteAnswer,  0, SpringLayout.EAST, uidL);
        layout.putConstraint(SpringLayout.SOUTH, deleteAnswer, 0, SpringLayout.SOUTH, applyButton);
        layout.putConstraint(SpringLayout.EAST, delAnswerL,  0, SpringLayout.EAST, uidL);
        layout.putConstraint(SpringLayout.NORTH, delAnswerL, 15, SpringLayout.SOUTH, comment);
        layout.putConstraint(SpringLayout.EAST, delAnswerIndex,  0, SpringLayout.EAST, uid);
        layout.putConstraint(SpringLayout.NORTH, delAnswerIndex, 15, SpringLayout.SOUTH, comment);
        
        
        panel.add(headline);
        panel.add(answerL);
        panel.add(answer);
        panel.add(commentL);
        panel.add(comment);
        panel.add(pntsL);
        panel.add(pnts);
        panel.add(answerStateL);
        panel.add(answerState);
        panel.add(saveAnswer);
        panel.add(deleteAnswer);
        panel.add(delAnswerL);
        panel.add(delAnswerIndex);
	}
	
	public String getAnswer() {
		return answerr.getText();
	}
	
	public String getComment() {
		return commentt.getText();
	}
	
	public int getPoints() {
		String tmp = pnts.getValue().toString();
		return Integer.parseInt(tmp);
	}
	
	public int getDelAnswerIndex() {
		String tmp = delAnswerIndex.getValue().toString();
		return Integer.parseInt(tmp);
	}
	
	public boolean isState() {
		return state;
	}
	
	public void reset() {
		answerr.setText("");
		commentt.setText("");
		pnts.setValue(0);
		answerState.setSelectedIndex(0);
		state = true;
	}
}
