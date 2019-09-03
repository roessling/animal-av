package htdptl.gui;

import htdptl.facade.Facade;
import htdptl.filter.IFilter;

import javax.swing.table.AbstractTableModel;

public class FilterTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4742633412075604558L;
	private static FilterTableModel instance;

	public static FilterTableModel getInstance() {
		if (instance == null) {
			instance = new FilterTableModel();
		}
		return instance;
	}

	private Facade facade;
	
	private FilterTableModel() {
		
	}
	
	public void setFacade(Facade facade) {
		this.facade = facade;
	}

	private String[] columnNames = {"Type","Procedure","Amount"};

	@Override
	public int getColumnCount() {
		return 3;
	}
	
	@Override
	public String getColumnName(int column) {
		return columnNames [column];
	}

	@Override
	public int getRowCount() {
		return facade.getNumFilters();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return facade.getFilter(rowIndex).getClass().getSimpleName();
		case 1:
			return facade.getFilter(rowIndex).getProcedure();
		case 2:
			return facade.getFilter(rowIndex).getTimes();
		}
		return null;
	}

	public void addProcedureFilter(String procedure, int times) {
		facade.addProcedureFilter(procedure, times);
		fireTableDataChanged();
	}

	public void addBreakpoint(String procedure, int times) {
		facade.addBreakpoint(procedure, times);
		fireTableDataChanged();
	}

	public void remove(int index) {
		facade.remove(index);
		fireTableDataChanged();
	}

  public IFilter get(int index) {
    return facade.getFilter(index);
    
  }

  


}
