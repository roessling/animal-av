package animal.gui;

import javax.swing.table.AbstractTableModel;

import animal.main.AnimationState;
import animal.variables.Variable;

public class VariableTableModel extends AbstractTableModel {
	private static final long serialVersionUID = -4525246143284609287L;
	private static final String[] columnNames = new String[] { "Name", "Value", "Role" };
	private String[][] data = null;
	private AnimationState animState;
	
	public VariableTableModel() {
	}

	public void setStep(int stepNr) {
			updateDataForStep();
//		if(data != null) System.out.println("data-size: "+data.length);
		
		fireTableDataChanged();
	}

	public void updateDataForStep() {
		animState = AnimalMainWindow.getWindowCoordinator().getAnimationWindow(false).getAnimationState();
		int pos = 0;
		int nrVars = animState.getVariables().size();
		if (nrVars > 0) {
			data = new String[nrVars][columnNames.length];

			for (String variableName : animState.getVariables().keySet()) {
				Variable var = animState.getVariables().get(variableName);
				data[pos][0] = var.getName();
				data[pos][1] = var.getValue();
				data[pos][2] = var.getRoleString();
				pos++;
			}
		}
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		if (data == null)
			return 0;
		return data.length;
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	public String getValueAt(int row, int col) {
		if (data == null || data.length < row || data[row].length < col)
			return null;

		return data[row][col];
	}

	public boolean isCellEditable(int row, int col) {
		return false;
	}
}
