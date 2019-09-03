package interactionsupport.patterns;
/*
 * Created on 19.09.2008 by Bjoern Dasbach <dasbach@rbg.informatik.tu-darmstadt.de>
 */
import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;


public class FIBPanel extends QuestionPanel {
	protected JLabel answerTextL, commentL;
	protected JTextArea answerTextt, commentt;
	protected JScrollPane answerText, comment;

	public FIBPanel(PatternUpdate updater) {
		super(updater);
		
		answerTextL = new JLabel("Answer:");
        commentL  = new JLabel("Comment:");
        
        answerTextt = new JTextArea(3, 15);
        commentt = new JTextArea(3, 15);
        answerText = new JScrollPane(answerTextt);
        comment = new JScrollPane(commentt);
        
        // AnswerText
        layout.putConstraint(SpringLayout.EAST, answerTextL,  0, SpringLayout.EAST, uidL);
        layout.putConstraint(SpringLayout.NORTH, answerTextL, 30, SpringLayout.SOUTH, points);
        layout.putConstraint(SpringLayout.EAST, answerText,  0, SpringLayout.EAST, prompt);
        layout.putConstraint(SpringLayout.NORTH, answerText, 30, SpringLayout.SOUTH, points);
        // AnswerComment
        layout.putConstraint(SpringLayout.EAST, commentL, 0, SpringLayout.EAST, uidL);
        layout.putConstraint(SpringLayout.NORTH, commentL, 5, SpringLayout.SOUTH, answerText);
        layout.putConstraint(SpringLayout.WEST, comment, 0, SpringLayout.WEST, uid);
        layout.putConstraint(SpringLayout.NORTH, comment, 5, SpringLayout.SOUTH, answerText);
        
        panel.add(answerText); panel.add(answerTextL);
        panel.add(comment); panel.add(commentL);
	}
	
	public String getAnswer() {
		return answerTextt.getText();
	}
	
	public String getComment() {
		return commentt.getText();
	}
	
    public void actionPerformed(ActionEvent e) {
    	updater.update(this);
    }
    
    public void resetInteraction() {
    	uid.setText("");
    	promptt.setText("");
    	group.setText("");
    	points.setValue(0);
    	answerTextt.setText("");
    	commentt.setText("");
    }
}
