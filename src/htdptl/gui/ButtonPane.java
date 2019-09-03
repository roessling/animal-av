package htdptl.gui;

import htdptl.gui.listener.ButtonPaneListener;

import java.awt.BorderLayout;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ButtonPane extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4004302813059300975L;
	JButton back;
	JButton finish;
	AbstractButton next;
	JButton cancel;

	public ButtonPane(HtDPTLWizard wizard) {
		
		setLayout(new BorderLayout());
		
		Box box = Box.createHorizontalBox();
		
		back = new JButton("< back");
		back.setActionCommand("back");
		back.addActionListener(new ButtonPaneListener(wizard));
		back.setEnabled(false);

		next = new JButton("next >");
		next.setActionCommand("next");
		next.addActionListener(new ButtonPaneListener(wizard));

		finish = new JButton("Finish");
		finish.setActionCommand("finish");
		finish.addActionListener(new ButtonPaneListener(wizard));
		finish.setEnabled(false);
		
		cancel = new JButton("Cancel");
		cancel.setActionCommand("cancel");
		cancel.addActionListener(new ButtonPaneListener(wizard));
		
		box.add(back);
		box.add(Box.createHorizontalStrut(5));
		box.add(next);
		box.add(Box.createHorizontalStrut(10));
		box.add(finish);
		box.add(Box.createHorizontalStrut(10));
		box.add(cancel);
		
		add( box, BorderLayout.EAST );
		
		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
	}

	public void setBackEnabled(boolean b) {
		back.setEnabled(b);		
	}

}
