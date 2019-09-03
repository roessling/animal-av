/**
 * 
 */
package extras.animalsense.ui.edit;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import extras.lifecycle.common.Variable;

/**
 * @author Mihail Mihaylov
 *
 */
public class VariablesTable extends VariablesTableBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3281792846979358947L;
	private List<Variable> variables;
	private Variable variable;
	
	/**
	 * 
	 */
	protected VariablesTable() {
		super();
		this.variables = new ArrayList<Variable>();
		
	}

	private void init() {
		ActionOnCellEditor removeAction = new ActionOnCellEditor("remove", "Remove", this);
		getQTable().getColumnModel().getColumn(2).setCellEditor(removeAction);
		getQTable().getColumnModel().getColumn(2).setCellRenderer(removeAction);
		getQTable().getColumnModel().getColumn(2).setPreferredWidth(80);
		getQTable().getColumnModel().getColumn(2).setMinWidth(60);
		getQTable().getColumnModel().getColumn(2).setMaxWidth(100);
	}

	public void update() {
		this.getQTable().setModel(new VariablesTableModel(variables));
		init();
	}

	/**
	 * @return the variables
	 */
	public List<Variable> getVariables() {
		return variables;
	}
	
	/**
	 * @param variables the variables to set
	 */
	public void setVariables(List<Variable> variables) {
		this.variables = variables;
	}

	/* (non-Javadoc)
	 * @see extras.animalsense.ui.edit.VariablesTableBase#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object eventSrc = e.getSource();
		if (eventSrc instanceof Variable)
			variable = (Variable) eventSrc;
		
		if ("addNew".equals(e.getActionCommand()))
			addNew();
		else if ("remove".equals(e.getActionCommand()))
			removeVariable();
	}

	private void removeVariable() {
//		System.err.println("Remove Variable: " + variable);
		//default icon, custom title
		int n = JOptionPane.showConfirmDialog(
		    this,
		    "Are you sure you want to remove the variable " + variable + "?",
		    "Remove Question?",
		    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		System.err.println("Remove: " + n);
		if (n == JOptionPane.YES_OPTION) {
			variables.remove(variable);
			update();
		}
	}

	private void addNew() {
		if (variables == null)
			return;
		
		Variable variable = new Variable(getNextName(), new String(""));
		
		variables.add(variable);
		update();
	}

	/**
	 * Creates an unique variable name
	 * @return
	 */
	private String getNextName() {
		for (char ch='a'; ch <= 'z'; ch++) {
			String temp = "" + ch;
			boolean exists = false;
			for (Variable var : variables) {
				if (temp.equals(var.getName())) {
					exists = true;
					break;					
				}	
			}
			if (!exists)
				return temp;
		}
		
		return "var" + System.currentTimeMillis();
	}

}
