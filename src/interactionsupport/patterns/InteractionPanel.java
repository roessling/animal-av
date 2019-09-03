package interactionsupport.patterns;
/*
 * Created on 19.09.2008 by Bjoern Dasbach <dasbach@rbg.informatik.tu-darmstadt.de>
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class InteractionPanel implements ActionListener {
	protected JPanel panel;
	protected SpringLayout layout;
	protected JButton applyButton;
	protected JLabel uidL, warning;
	protected JTextField uid;
	protected PatternUpdate updater;
    
	public InteractionPanel(PatternUpdate u) {
		updater = u;
		panel = new JPanel();
		layout = new SpringLayout();
		panel.setLayout(layout);
		
		applyButton = new JButton("Save Interaction Pattern");
		applyButton.setMnemonic(KeyEvent.VK_I);
        applyButton.addActionListener(this);
        uidL = new JLabel("UID:");
        warning = new JLabel("");
        uid = new JTextField("", 15);
        
        // UID
        layout.putConstraint(SpringLayout.WEST, uidL,  150, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, uidL, 10, SpringLayout.NORTH, panel);
        layout.putConstraint(SpringLayout.WEST, uid,  20, SpringLayout.EAST, uidL);
        layout.putConstraint(SpringLayout.NORTH, uid, 10, SpringLayout.NORTH, panel);
        // Save
        layout.putConstraint(SpringLayout.EAST, applyButton,  -5, SpringLayout.EAST, panel);
        layout.putConstraint(SpringLayout.SOUTH, applyButton, -5, SpringLayout.SOUTH, panel);
        layout.putConstraint(SpringLayout.WEST, warning,  5, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.SOUTH, warning, -10, SpringLayout.NORTH, applyButton);
        
        panel.add(uidL);
        panel.add(uid);
        panel.add(applyButton);
        panel.add(warning);
	}
	
	public void setWarning(String text) {
		warning.setText(text);
	}
	
	public JComponent getPanel() {
		return panel;
	}
	
	public String getUid() {
		return uid.getText();
	}
	
	public void setUid(String text) {
		uid.setText(text);
	}

	@Override
	public void actionPerformed(ActionEvent e) {}
}
