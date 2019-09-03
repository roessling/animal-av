package interactionsupport.patterns;
/*
 * Created on 19.09.2008 by Bjoern Dasbach <dasbach@rbg.informatik.tu-darmstadt.de>
 */
import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;


public class MSPanel extends AnswerPanel {
	private Vector<Answer> answerSet = null;
	private JTextArea answers;
	private JScrollPane scrollPane;
	
	public MSPanel(PatternUpdate updater) {
		super(updater);
		
		answerSet = new Vector<Answer>();
		
		answers = new JTextArea(10, 20);
		answers.setEditable(false);
        scrollPane = new JScrollPane(answers);
        JLabel answersL = new JLabel("Saved Answers:");
        
        layout.putConstraint(SpringLayout.WEST, answersL,  30, SpringLayout.EAST, uid);
        layout.putConstraint(SpringLayout.NORTH, answersL, 0, SpringLayout.NORTH, uid);
        layout.putConstraint(SpringLayout.WEST, scrollPane,  30, SpringLayout.EAST, uid);
        layout.putConstraint(SpringLayout.NORTH, scrollPane, 5, SpringLayout.SOUTH, uid);
        
        panel.add(answersL);
        panel.add(scrollPane);
	}
	
	public Vector<Answer> getAnswerSet() {
		return answerSet;
	}
	
	public void actionPerformed(ActionEvent e) {
    	Object event = e.getSource();
    	
    	if(event.equals(answerState)) {
    		@SuppressWarnings("unchecked")
        JComboBox<String> cb = (JComboBox<String>)event;
    		String value = (String)cb.getSelectedItem();
    		
    		if(value.equals("True")) {
    			state = true;
    		} else {
    			state = false;
    		}
    	}
    	if(event.equals(saveAnswer)) {
    		String answer = "Answer: " + getAnswer() +
    		"\nComment: " + getComment() +
    		"\nPoints: " + getPoints() +
    		"\nState: " + isState() + "\n\n";
    		
    		answers.append(answer);
    		answers.setCaretPosition(answers.getDocument().getLength());
    		
    		answerSet.add(new Answer(getAnswer(), getComment(), getPoints(), isState()));
    		reset();
    		
    		SpinnerModel model = new SpinnerNumberModel(0, 0, answerSet.size()-1, 1);
    		delAnswerIndex.setModel(model);
    		delAnswerIndex.setVisible(true);
    		
    		delAnswerL.setVisible(true);
    		deleteAnswer.setVisible(true);
    	}
    	if(event.equals(deleteAnswer)) {
    		String answer = "";
    		Answer tmp = null;
    		SpinnerModel model = null;
    		
    		answerSet.remove(getDelAnswerIndex());
    		answers = new JTextArea(10, 20);
    		answers.setEditable(false);
    		
    		for(int i = 0; i < answerSet.size(); i++) {
    			tmp = answerSet.get(i);
    			answer = answer + "Answer: " + tmp.getAnswer() +
        		"\nComment: " + tmp.getComment() +
        		"\nPoints: " + tmp.getPoints() +
        		"\nState: " + tmp.isState() + "\n\n";
    		}
    		
    		answers.append(answer);
    		answers.setCaretPosition(answers.getDocument().getLength());
    		scrollPane.setViewportView(answers);
    		
    		
    		if(answerSet.size() == 0) {
    			model = new SpinnerNumberModel(0, 0, 0, 1);
    		} else {
    			model = new SpinnerNumberModel(0, 0, answerSet.size()-1, 1);
    		}
    		delAnswerIndex.setModel(model);
    		
    		if(answerSet.size() == 0) {
	    		delAnswerIndex.setVisible(false);
	    		delAnswerL.setVisible(false);
	    		deleteAnswer.setVisible(false);
    		}
    	}
    	if(event.equals(applyButton)) {
    		updater.update(this);
    	}
    }
	
	public void resetInteraction() {
    	uid.setText("");
    	promptt.setText("");
    	group.setText("");
    	points.setValue(0);
    	answers.setText("");
    	reset();
    }
}