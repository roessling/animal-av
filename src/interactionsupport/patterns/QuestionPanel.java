package interactionsupport.patterns;

/*
 * Created on 19.09.2008 by Bjoern Dasbach <dasbach@rbg.informatik.tu-darmstadt.de>
 */
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;


public class QuestionPanel extends InteractionPanel {
	protected JLabel promptL, pointsL, groupL;
	protected JTextField group;
	protected JTextArea promptt;
	protected JScrollPane prompt;
	protected JSpinner points;
	
	public QuestionPanel(PatternUpdate updater) {
		super(updater);
		
		promptL = new JLabel("Prompt:");
        pointsL = new JLabel("Points Possible:");
        groupL = new JLabel("Group ID:");
        
        promptt = new JTextArea(3, 15);
        prompt = new JScrollPane(promptt);
        group = new JTextField("", 15);
        
        SpinnerModel model = new SpinnerNumberModel(0, -100, 100, 1);
        points = new JSpinner(model);
        JFormattedTextField tmp = ((JSpinner.DefaultEditor)points.getEditor()).getTextField();
        tmp.setColumns(8);
        
        // QuestionPrompt
        layout.putConstraint(SpringLayout.EAST, promptL,  0, SpringLayout.EAST, uidL);
        layout.putConstraint(SpringLayout.NORTH, promptL, 5, SpringLayout.SOUTH, uid);
        layout.putConstraint(SpringLayout.WEST, prompt,  0, SpringLayout.WEST, uid);
        layout.putConstraint(SpringLayout.NORTH, prompt, 5, SpringLayout.SOUTH, uid);
        // QuestionGroup
        layout.putConstraint(SpringLayout.EAST, groupL,  0, SpringLayout.EAST, uidL);
        layout.putConstraint(SpringLayout.NORTH, groupL, 5, SpringLayout.SOUTH, prompt);
        layout.putConstraint(SpringLayout.WEST, group,  0, SpringLayout.WEST, uid);
        layout.putConstraint(SpringLayout.NORTH, group, 5, SpringLayout.SOUTH, prompt);
        // PointsPossible
        layout.putConstraint(SpringLayout.EAST, pointsL,  0, SpringLayout.EAST, uidL);
        layout.putConstraint(SpringLayout.NORTH, pointsL, 30, SpringLayout.SOUTH, group);
        layout.putConstraint(SpringLayout.EAST, points,  0, SpringLayout.EAST, prompt);
        layout.putConstraint(SpringLayout.NORTH, points, 30, SpringLayout.SOUTH, group);
        
        panel.add(prompt); panel.add(promptL);
        panel.add(group); panel.add(groupL);
        panel.add(points); panel.add(pointsL);
	}
	
	public String getPrompt() {
		return promptt.getText();
	}
	
	public String getGroup() {
		return group.getText();
	}
	
	public int getPointsPossible() {
		String tmp = points.getValue().toString();
		return Integer.parseInt(tmp);
	}
}
