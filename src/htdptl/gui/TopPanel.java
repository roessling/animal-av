package htdptl.gui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TopPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1190578042386486023L;

	public TopPanel(String text) {		
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);		
		setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
		
		JLabel label = new JLabel(text);
		label.setBorder(BorderFactory.createEmptyBorder(20, 5, 5, 5));		
		add(label,BorderLayout.PAGE_START);
	}
	
}
