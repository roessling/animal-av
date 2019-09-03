package interactionsupport.patterns;
/*
 * Created on 19.09.2008 by Bjoern Dasbach <dasbach@rbg.informatik.tu-darmstadt.de>
 */
import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class DocuPanel extends InteractionPanel {
	protected JLabel urlL;
	protected JTextField url;
	
	public DocuPanel(PatternUpdate u) {
		super(u);
		
		urlL = new JLabel("Documentation Link: ");
		url = new JTextField("", 15);
		
		// url
        layout.putConstraint(SpringLayout.EAST, urlL,  0, SpringLayout.EAST, uidL);
        layout.putConstraint(SpringLayout.NORTH, urlL, 5, SpringLayout.SOUTH, uid);
        layout.putConstraint(SpringLayout.WEST, url,  0, SpringLayout.WEST, uid);
        layout.putConstraint(SpringLayout.NORTH, url, 5, SpringLayout.SOUTH, uid);
        
        panel.add(urlL);
        panel.add(url);
	}
	
	public String getURL() {
		return url.getText();
	}
	
	public void actionPerformed(ActionEvent e) {
		updater.update(this);
    }
	
	public void resetInteraction() {
    	uid.setText("");
    	url.setText("");
    }
}
