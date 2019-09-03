package htdptl.gui;

import htdptl.facade.Facade;
import htdptl.filter.BreakpointFilter;
import htdptl.filter.IFilter;
import htdptl.filter.ProcedureFilter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class FilterPage extends JComponent implements ActionListener, ListSelectionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7623837681170179376L;
	private JButton remove;
	private int index;
  private JButton edit;
  private Facade facade;

	public FilterPage(Facade facade) {
	  this.facade = facade;
	  
		TopPanel topPanel = new TopPanel(
				"Add procedure filters or breakpoints.");
		setLayout(new BorderLayout());

		JTable table = new JTable(FilterTableModel.getInstance());
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.black));
		scrollPane.setPreferredSize(new Dimension(500,0));
		table.setFillsViewportHeight(true);
		table.setRowSelectionAllowed(true);
		ListSelectionModel lsm = table.getSelectionModel();
		lsm.addListSelectionListener(this);
		table.setSelectionModel(lsm);
		lsm.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		

		JPanel right = new JPanel();

		JPanel buttons = new JPanel(new GridBagLayout());	
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		
		JButton addProcedureFilter = new JButton("Add procedure filter");
		addProcedureFilter.setActionCommand("procedure");
		addProcedureFilter.addActionListener(this);
		addProcedureFilter.setPreferredSize(new Dimension(200,25));
		buttons.add(addProcedureFilter, c);	
		
		buttons.add(Box.createVerticalStrut(5),c);
		
		JButton addBreakpoint = new JButton("Add breakpoint");
		addBreakpoint.setActionCommand("breakpoint");
		addBreakpoint.addActionListener(this);
		addBreakpoint.setPreferredSize(new Dimension(200,25));
		buttons.add(addBreakpoint, c);
		
		buttons.add(Box.createVerticalStrut(5),c);
		
		remove = new JButton("Remove filter");
		remove.setEnabled(false);
		remove.setActionCommand("remove");
		remove.addActionListener(this);
		remove.setPreferredSize(new Dimension(200,25));
		buttons.add(remove, c);
		
		buttons.add(Box.createVerticalStrut(5),c);
    		
		edit = new JButton("Edit filter");
		edit.setEnabled(false);
    edit.setActionCommand("edit");
    edit.addActionListener(this);
    edit.setPreferredSize(new Dimension(200,25));
    buttons.add(edit, c);
		
		buttons.add(Box.createVerticalStrut(5),c);
		
		
		
		right.add(buttons);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(scrollPane,BorderLayout.CENTER);
		panel.add(right,BorderLayout.EAST);
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		

		add(topPanel, BorderLayout.PAGE_START);
		add(panel, BorderLayout.CENTER);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("procedure")) {
			new AddProcedureFilterDialog(facade);
		} else if (e.getActionCommand().equals("breakpoint")) {
			new AddBreakpointDialog(facade);
		} else if (e.getActionCommand().equals("remove")) {
			FilterTableModel.getInstance().remove(index);
		} else if (e.getActionCommand().equals("edit")) {
      IFilter filter = FilterTableModel.getInstance().get(index);
      if (filter instanceof ProcedureFilter) {
        new EditProcedureFilterDialog(facade, (ProcedureFilter)filter);
      }
      else {
        new EditBreakpointFilterDialog(facade, (BreakpointFilter)filter);
      }
    }
	}


	@Override
	public void valueChanged(ListSelectionEvent e) {
		ListSelectionModel lsm = (ListSelectionModel)e.getSource();
		if (lsm.isSelectionEmpty()) {
			remove.setEnabled(false);
			edit.setEnabled(false);
			return;
		}
		
		index = lsm.getMinSelectionIndex();
		
		remove.setEnabled(true);
		edit.setEnabled(true);
		
		
		
	}

}
