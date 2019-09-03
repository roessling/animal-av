package extras.animalsense.ui.edit;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class ConsolePane extends JScrollPane {

	private static final long serialVersionUID = 1L;
	private JTextArea jTextArea = null;

	/**
	 * This is the default constructor
	 */
	public ConsolePane() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setSize(300, 200);
		
		this.setPreferredSize(new Dimension(4, 100));
		this.setBorder(BorderFactory.createTitledBorder(null, "Output", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Lucida Grande", Font.PLAIN, 13), Color.black));
		this.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

		this.setViewportView(getJTextArea());
	}

	/**
	 * This method initializes jTextArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getJTextArea() {
		if (jTextArea == null) {
			jTextArea = new JTextArea();
			jTextArea.setRows(20);
			jTextArea.setEditable(false);
		}
		return jTextArea;
	}
	
	public void appendText(String text) {
		getJTextArea().append(text);getJTextArea().append("\n");
		getJTextArea().setCaretPosition(getJTextArea().getDocument().getLength());
	}
	
	public void clearText() {
		Document doc = getJTextArea().getDocument();
		try {
			doc.remove(0, doc.getLength());
			
		} catch (BadLocationException e) {
			// This should always work without errors
		}
	}

}
