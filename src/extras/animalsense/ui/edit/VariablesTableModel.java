/**
 * 
 */
package extras.animalsense.ui.edit;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import extras.animalsense.evaluate.Question;
import extras.lifecycle.common.Variable;

/**
 * @author Mihail Mihaylov
 *
 */
public class VariablesTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<Variable> variables;
	
	/**
	 * @param variables
	 */
	public VariablesTableModel(List<Variable> variables) {
		super();
		setVariables(variables);
	}

	/**
	 * 
	 */
	public VariablesTableModel() {
		this(new ArrayList<Variable>(0));
	}
	

	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int row, int column) {
		Object result;
		if (column == 0)
			result = variables.get(row).getName();
		else if (column == 1)
			result = variables.get(row).getValue();
		else //if (column == 2)
			result =  variables.get(row);
		
		return result;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableModel#getRowCount()
	 */
	@Override
	public int getRowCount() {
		return variables.size();
	}
	
	 /*
     * JTable uses this method to determine the default renderer/
     * editor for each cell. If we didn't implement this method,
     * then the last column would contain text ("true"/"false"),
     * rather than a check box.
     */
	public Class<?> getColumnClass(int c) {
		if (c < 2)
			return String.class;
		else
			return Question.class;

	}

	/**
	 * @return the variables
	 */
	public List<Variable> getVariables() {
		return variables;
	}

	/**
	 * @param variables the questions to set
	 */
	public void setVariables(List<Variable> variables) {
		this.variables = variables;		
	}

	@Override
	public int getColumnCount() {
		return 3;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(int column) {
		if (column == 0)
			return "Name";
		else if (column == 1)
			return "Value";
		else
			return "-";
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return true;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
	 */
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if (columnIndex == 0)
			variables.get(rowIndex).setName((String) aValue);
		else if (columnIndex == 1)
			variables.get(rowIndex).setValue(aValue);
	}
	
	
	
}
