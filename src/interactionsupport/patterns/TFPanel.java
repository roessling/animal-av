package interactionsupport.patterns;
/*
 * Created on 19.09.2008 by Bjoern Dasbach <dasbach@rbg.informatik.tu-darmstadt.de>
 */
import java.awt.event.ActionEvent;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;


public class TFPanel extends QuestionPanel {
	protected JLabel answerStateL, correctCommentL, wrongCommentL;
	protected JTextArea correctCommentt, wrongCommentt;
	protected JScrollPane correctComment, wrongComment;
	protected JComboBox<String> answerState;
	protected boolean state;
	
	public TFPanel(PatternUpdate updater) {
		super(updater);
		
		answerStateL = new JLabel("Answer State:");
        correctCommentL = new JLabel("Correct Answer Comment:");
        wrongCommentL = new JLabel("Wrong Answer Comment:");
        
        correctCommentt = new JTextArea(3, 15);
        wrongCommentt = new JTextArea(3, 15);
        correctComment = new JScrollPane(correctCommentt);
        wrongComment = new JScrollPane(wrongCommentt);
        
        String[] state = { "True", "False" };
        answerState = new JComboBox<String>(state);
        answerState.setSelectedIndex(0);
        answerState.addActionListener(this);
        this.state = true;
        
        // AnswerState
        layout.putConstraint(SpringLayout.EAST, answerStateL,  0, SpringLayout.EAST, uidL);
        layout.putConstraint(SpringLayout.NORTH, answerStateL, 15, SpringLayout.SOUTH, points);
        layout.putConstraint(SpringLayout.EAST, answerState,  0, SpringLayout.EAST, prompt);
        layout.putConstraint(SpringLayout.NORTH, answerState, 10, SpringLayout.SOUTH, points);
        // AnswerComment
        layout.putConstraint(SpringLayout.EAST, correctCommentL, 0, SpringLayout.EAST, uidL);
        layout.putConstraint(SpringLayout.NORTH, correctCommentL, 30, SpringLayout.SOUTH, answerState);
        layout.putConstraint(SpringLayout.WEST, correctComment, 0, SpringLayout.WEST, uid);
        layout.putConstraint(SpringLayout.NORTH, correctComment, 30, SpringLayout.SOUTH, answerState);
        layout.putConstraint(SpringLayout.EAST, wrongCommentL, 0, SpringLayout.EAST, uidL);
        layout.putConstraint(SpringLayout.NORTH, wrongCommentL, 5, SpringLayout.SOUTH, correctComment);
        layout.putConstraint(SpringLayout.WEST, wrongComment, 0, SpringLayout.WEST, uid);
        layout.putConstraint(SpringLayout.NORTH, wrongComment, 5, SpringLayout.SOUTH, correctComment);
        
        panel.add(answerState); panel.add(answerStateL);
        panel.add(correctComment); panel.add(correctCommentL);
        panel.add(wrongComment); panel.add(wrongCommentL);
	}
	
	public boolean isAnswerState() {
		return state;
	}
	
	public String getCommentTrue() {
		return correctCommentt.getText();
	}
	
	public String getCommentFalse() {
		return wrongCommentt.getText();
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
    	if(event.equals(applyButton)) {
    		updater.update(this);
    	}
    }
    
    public void resetInteraction() {
    	uid.setText("");
    	promptt.setText("");
    	group.setText("");
    	points.setValue(0);
    	answerState.setSelectedIndex(0);
    	correctCommentt.setText("");
    	wrongCommentt.setText("");
    }
}
