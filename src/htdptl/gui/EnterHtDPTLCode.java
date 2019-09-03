package htdptl.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

public class EnterHtDPTLCode extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextArea textArea;

	public EnterHtDPTLCode() {
		
		TopPanel topPanel = new TopPanel("Please enter a HtDP-TL program.");

		setLayout(new BorderLayout());
		textArea = new JTextArea();
		Font font = new Font("Courier", Font.PLAIN, 12);
		textArea.setFont(font);	
		
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		JScrollPane pane = new JScrollPane(textArea);
		pane.setBorder(new LineBorder(Color.gray, 1));
		panel.add(pane,BorderLayout.CENTER);

		add(topPanel, BorderLayout.PAGE_START);
		add(panel, BorderLayout.CENTER);

	}

	public String getProgram() {
		return textArea.getText();
	}

	
}
