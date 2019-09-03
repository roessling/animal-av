package htdptl.gui;

import htdptl.facade.Facade;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

public class ChooseExpressionsPage extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private Facade facade;
	JCheckBox[] checkBoxes;
	ArrayList<Object> expressions;

	public ChooseExpressionsPage(Facade facade) {
		this.facade = facade;

		setLayout(new BorderLayout());		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

		expressions = facade.getExpressions();
		JPanel checkBoxPanel = new JPanel();
		checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel,
				BoxLayout.PAGE_AXIS));
		checkBoxPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		checkBoxPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		checkBoxes = new JCheckBox[expressions.size()];
		for (int i = 0; i < expressions.size(); i++) {
			checkBoxes[i] = new JCheckBox(expressions.get(i).toString());
			checkBoxes[i].setSelected(true);
			checkBoxPanel.add(checkBoxes[i]);
		}

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		JButton selectAll = new JButton("Select All");
		JButton deselectAll = new JButton("Deselect All");
		selectAll.setActionCommand("selectAll");
		deselectAll.setActionCommand("deselectAll");
		selectAll.addActionListener(this);
		deselectAll.addActionListener(this);

		buttonPanel.add(selectAll);
		buttonPanel.add(Box.createRigidArea(new Dimension(20, 0)));
		buttonPanel.add(deselectAll);

		panel.add(checkBoxPanel);
		panel.add(buttonPanel);

		TopPanel topPanel = new TopPanel(
				"Please select the expressions to visualize:");

		JPanel container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
		container.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		container.add(panel);

		add(topPanel, BorderLayout.PAGE_START);
		add(container, BorderLayout.CENTER);

	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("selectAll")) {
			select(true);
		} else if (e.getActionCommand().equals("deselectAll")) {
			select(false);
		}

	}

	private void select(boolean b) {
		for (int i = 0; i < checkBoxes.length; i++) {
			checkBoxes[i].setSelected(b);			
		}		
	}
	
	public void doSelection() {
		ArrayList<Object> selected = new ArrayList<Object>();
		for (int i = 0; i < checkBoxes.length; i++) {
			if (checkBoxes[i].isSelected()) {
				selected.add(expressions.get(i));
			}
		}
		facade.setExpressions(selected);
	}

}
