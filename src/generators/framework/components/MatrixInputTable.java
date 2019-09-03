/*
 * Created on 12.11.2004 by T. Ackermann
 */
package generators.framework.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

/**
 * MatrixInputTable is a visual class that allows entering matrices.
 * 
 * @author T. Ackermann
 */
public class MatrixInputTable extends JScrollPane {

	/** Matrix stores Integer */
	public static final char MATRIX_TYPE_INTEGER = 1;

	/** Matrix stores String */
	public static final char MATRIX_TYPE_STRING = 2;

	/** stores the type of this Array */
	private char arrayType = MATRIX_TYPE_INTEGER;

  /**
   * a generated serial Version UID because MatrixInputTable is serializable.
   */
  private static final long serialVersionUID = -2264273704590965509L;

  /** stores the JTable-Object */
  private JTable myTable;

  /** stores the number of columns in the matrix */
  private int iNumberOfColumns = 3;

  /** stores the number of rows in the matrix */
  private int iNumberOfRows = 3;

  /** stores the local String for the Table Header ("Column") */
  private String strLocalHeader = "Column";

  /**
   * Constructor creates a new 3x3-MatrixInputTable-Object.
   */
  public MatrixInputTable() {
    super();
    this.iNumberOfRows = 3;
    this.iNumberOfColumns = 3;
    init();
  }

  /**
   * Constructor creates a new MatrixInputTable-Object.
   * 
   * @param numRows
   *          The number of displayed Rows.
   * @param numColumns
   *          The number of displayed Columns.
   */
  public MatrixInputTable(int numRows, int numColumns) {
    super();
    iNumberOfRows = (numRows < 1) ? 3 : numRows;
    iNumberOfColumns = (numColumns < 1) ? 3 : numColumns;
//    if (numRows < 1) {
//      numRows = 3;
//    }
//
//    if (numColumns < 1) {
//      numColumns = 3;
//    }
//
//    this.iNumberOfRows = numRows;
//    this.iNumberOfColumns = numColumns;
    init();
  }

  /**
   * Creates a new MatrixInputTable object and uses the passed values. The first
   * index is the row, the second one is for the column number.
   * 
   * @param newValues
   *          The values to use in the table.
   */
  public MatrixInputTable(int[][] newValues) {
    super();
    this.iNumberOfRows = 3;
    arrayType = MATRIX_TYPE_INTEGER;
    this.iNumberOfColumns = 3;
    init();
    setValues(newValues);
  }
  
  /**
   * Creates a new MatrixInputTable object and uses the passed values. The first
   * index is the row, the second one is for the column number.
   * 
   * @param newValues
   *          The values to use in the table.
   */
  public MatrixInputTable(String[][] newValues) {
    super();
    this.iNumberOfRows = 3;
    arrayType = MATRIX_TYPE_STRING;
    this.iNumberOfColumns = 3;
    init();
    setValues(newValues);
  }


  /**
   * Sets the number of columns in the matrix.
   * 
   * @param numColumns
   *          The new number of columns in the matrix.
   */
  public void setNumberOfColumns(int numColumns) {
    if (numColumns < 1) {
      return;
    }

    this.iNumberOfColumns = numColumns;
    changeDimensions(this.iNumberOfRows, numColumns);
  }

  /**
   * Returns the number of columns in the matrix.
   * 
   * @return Returns the number of columns in the matrix.
   */
  public int getNumberOfColumns() {
    return this.iNumberOfColumns;
  }

  /**
   * Sets the number of rows in the matrix.
   * 
   * @param numRows
   *          The new number of rows in the matrix.
   */
  public void setNumberOfRows(int numRows) {
    if (numRows < 1) {
      return;
    }

    this.iNumberOfRows = numRows;
    changeDimensions(numRows, this.iNumberOfColumns);
  }

  /**
   * Returns the number of rows in the matrix.
   * 
   * @return Returns the number of rows in the matrix.
   */
  public int getNumberOfRows() {
    return this.iNumberOfRows;
  }

  /**
   * Sets the values via an double[][]. The first index is the row and the second
   * index is for the column number.
   * 
   * @param newValues
   *          The values as an double[][].
   */
  public void setValues(double[][] newValues) {
    if (newValues == null) {
      return;
    }

    int iNewRows = newValues.length;

    if (iNewRows == 0) {
      return;
    }

    int iNewColumns = newValues[0].length;

    if (iNewColumns == 0) {
      return;
    }

    changeDimensions(iNewRows, iNewColumns);

    // enter new values
    for (int i = 0; i < iNewRows; i++) {
      for (int j = 0; j < iNewColumns; j++) {
        if (newValues[i].length >= (j - 1)) {
          Double doubleObj = Double.valueOf(newValues[i][j]);

          this.myTable.setValueAt(doubleObj, i, j);
        } else {
          this.myTable.setValueAt(Double.valueOf(0.0), i, j);
        }
      }
    }
  }

  
  /**
   * Sets the values via an int[][]. The first index is the row and the second
   * index is for the column number.
   * 
   * @param newValues
   *          The values as an int[][].
   */
  public void setValues(int[][] newValues) {
    if (newValues == null) {
      return;
    }

    int iNewRows = newValues.length;

    if (iNewRows == 0) {
      return;
    }

    int iNewColumns = newValues[0].length;

    if (iNewColumns == 0) {
      return;
    }

    changeDimensions(iNewRows, iNewColumns);

    // enter new values
    for (int i = 0; i < iNewRows; i++) {
      for (int j = 0; j < iNewColumns; j++) {
        if (newValues[i].length >= (j - 1)) {
          Integer intObj = Integer.valueOf(newValues[i][j]);

          this.myTable.setValueAt(intObj, i, j);
        } else {
          this.myTable.setValueAt(Integer.valueOf(0), i, j);
        }
      }
    }
  }
  
  /**
   * Sets the values via an String[][]. The first index is the row and the second
   * index is for the column number.
   * 
   * @param newValues
   *          The values as an String[][].
   */
  public void setValues(String[][] newValues) {
    if (newValues == null) {
      return;
    }

    int iNewRows = newValues.length;

    if (iNewRows == 0) {
      return;
    }

    int iNewColumns = newValues[0].length;

    if (iNewColumns == 0) {
      return;
    }

    changeDimensions(iNewRows, iNewColumns);

    // enter new values
    for (int i = 0; i < iNewRows; i++) {
      for (int j = 0; j < iNewColumns; j++) {
        if (newValues[i].length >= (j - 1)) {
          String cVal = new String(newValues[i][j]);

          this.myTable.setValueAt(cVal, i, j);
        } else {
          this.myTable.setValueAt("0", i, j);
        }
      }
    }
  }


  /**
   * Returns the values as an double[][]. The first index is the row and the second
   * index is for the column number.
   * 
   * @return The values as an double[][].
   */
  public double[][] getDoubleMatrixValues() {
    double[][] retval = new double[this.iNumberOfRows][this.iNumberOfColumns];

    for (int i = 0; i < this.iNumberOfRows; i++) {
      for (int j = 0; j < this.iNumberOfColumns; j++) {
        retval[i][j] = ((Double)this.myTable.getValueAt(i, j)).doubleValue();
      }
    }

    return retval;
  }

  /**
   * Returns the values as an int[][]. The first index is the row and the second
   * index is for the column number.
   * 
   * @return The values as an int[][].
   */
  public int[][] getIntMatrixValues() {
    int[][] retval = new int[this.iNumberOfRows][this.iNumberOfColumns];

    for (int i = 0; i < this.iNumberOfRows; i++) {
      for (int j = 0; j < this.iNumberOfColumns; j++) {
        retval[i][j] = ((Integer) this.myTable.getValueAt(i, j)).intValue();
      }
    }

    return retval;
  }
  
  /**
   * Returns the values as an String[][]. The first index is the row and the second
   * index is for the column number.
   * 
   * @return The values as an String[][].
   */
  public String[][] getStringMatrixValues() {
  	String[][] retval = new String[this.iNumberOfRows][this.iNumberOfColumns];

    for (int i = 0; i < this.iNumberOfRows; i++) {
      for (int j = 0; j < this.iNumberOfColumns; j++) {
        retval[i][j] = ((String) this.myTable.getValueAt(i, j));
      }
    }

    return retval;
  }


  /**
   * Sets the String that is used for the header of each column. In english this
   * could be "Column" for example.
   * 
   * @param strNewLocalHeader
   *          String that should be used for the header of each column.
   */
  public void setLocalHeaderString(String strNewLocalHeader) {
    if (strNewLocalHeader == null) {
      return;
    }
    String localHeader = strNewLocalHeader.trim();

    // we allow zero-length Strings...
//    strNewLocalHeader = strNewLocalHeader.trim();
    if (this.strLocalHeader.equals(localHeader)) {
      return;
    }

    this.strLocalHeader = localHeader;

    // update header...
    for (int i = 0; i < this.myTable.getColumnCount(); i++) {
      TableColumn column = this.myTable.getColumnModel().getColumn(i);

      column.setHeaderValue((localHeader + " " + Integer.toString(i))
          .trim());

      // here we could change the size of the column...
    }
  }

  /**
   * Returns the String that is used for the header of each column. In english
   * this could be "Column" for example.
   * 
   * @return The String that is used for the header of each column.
   */
  public String getLocalHeaderString() {
    return this.strLocalHeader;
  }

  /**
   * Changes the Dimensions of the matrix. If the matrix gets smaller, the old
   * values are taken oven. If the matrix grows, the new elements are
   * initialized with value "0".
   * 
   * @param numRows
   *          The number of displayed Rows.
   * @param numColumns
   *          The number of displayed Columns.
   */
  public void changeDimensions(int numRows, int numColumns) {
    if ((numRows < 1) || (numColumns < 1)) {
      return;
    }
//    System.err.println(myTable.getRowCount() +" x " +myTable.getColumnCount());

    int iOldCols = myTable.getColumnCount();//this.iNumberOfColumns;
    int iOldRows = myTable.getRowCount();//this.iNumberOfRows;

    Object[][] oldData = new Object[numRows][numColumns];

    // Backup old data
    for (int i = 0; i < numRows; i++) {
      oldData[i] = new Object[numColumns];
      for (int j = 0; j < numColumns; j++) {
        if ((i >= iOldRows) || (j >= iOldCols)) {
        	if (arrayType == MATRIX_TYPE_INTEGER)
        		oldData[i][j] = Integer.valueOf(0);
        	else
        		oldData[i][j] = "";
        } else {
          oldData[i][j] = this.myTable.getValueAt(i, j);
        }
      }
    }

    // set new values and re-init
    this.iNumberOfRows = numRows;
    this.iNumberOfColumns = numColumns;

    init();

    // reenter old values
    for (int i = 0; i < numRows; i++) {
      for (int j = 0; j < numColumns; j++) {
        this.myTable.setValueAt(oldData[i][j], i, j);
      }
    }
  }

  /**
   * Initializes the MatrixInputTable, creates the Table, and fills the values
   * with the default (0).
   */
  private void init() {
    this.setPreferredSize(new Dimension(200, 200));

    String[] columnNames = new String[this.iNumberOfColumns];
    Object[][] data = null;
    if (arrayType == MATRIX_TYPE_INTEGER) {
    	data = new Integer[this.iNumberOfRows][this.iNumberOfColumns];

    	for (int i = 0; i < this.iNumberOfColumns; i++) {
    		columnNames[i] = (this.strLocalHeader + " " + Integer.toString(i)).trim();
    		for (int j = 0; j < this.iNumberOfRows; j++) {
    			data[j][i] = Integer.valueOf(0);
    		}
    	}
    }
    else if (arrayType == MATRIX_TYPE_STRING) {
    	data = new String[this.iNumberOfRows][this.iNumberOfColumns];

    	for (int i = 0; i < this.iNumberOfColumns; i++) {
    		columnNames[i] = (this.strLocalHeader + " " + Integer.toString(i)).trim();
    		for (int j = 0; j < this.iNumberOfRows; j++) {
    			data[j][i] = "";
    		}
    	}
    }
    
    this.myTable = new JTable();
    this.myTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
    if (arrayType == MATRIX_TYPE_INTEGER)
    	this.myTable.setModel(new IntegerTableModel((Integer[][])data, columnNames));
    else 
    	this.myTable.setModel(new DefaultTableModel((String[][])data, columnNames));
    
    if (arrayType == MATRIX_TYPE_INTEGER) {
    	for (int i = 0; i < this.myTable.getColumnCount(); i++) {
    		TableColumn column = this.myTable.getColumnModel().getColumn(i);
    		IntegerCellEditor editor = new IntegerCellEditor();

    		column.setCellEditor(editor);
    	}
    } else {
    	for (int i = 0; i < this.myTable.getColumnCount(); i++) {
    		TableColumn column = this.myTable.getColumnModel().getColumn(i);
    		StringCellEditor editor = new StringCellEditor(new JTextField());

    		column.setCellEditor(editor);
    	}
    	
    }
    
    this.myTable.setCellSelectionEnabled(true);
    this.myTable.getTableHeader().setReorderingAllowed(false);
    this.myTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

    this.setViewportView(this.myTable);
  }

  /*
   * ******************************************************************* BELOW
   * ARE TWO HELPER CLASSES
   * *******************************************************************
   */

  /**
   * This is the Editor, we use for the input of the values. It uses a
   * IntegerTextField, so we don't have to take care of the validation of the
   * entered text.
   * 
   * @author T. Ackermann
   */
  static class IntegerCellEditor extends AbstractCellEditor implements
      TableCellEditor {

    /**
     * a generated serial Version UID because IntegerCellEditor is serializable.
     */
    private static final long serialVersionUID = -6725001766631532850L;

    /** stores the IntegerTextField, that manages the Input */
    private IntegerTextField textField = new IntegerTextField();

    /**
     * (non-Javadoc)
     * 
     * @see javax.swing.CellEditor#getCellEditorValue()
     */
    public Object getCellEditorValue() {
      return this.textField.getValue();
    }

    /**
     * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable,
     *      java.lang.Object, boolean, int, int)
     */
    public Component getTableCellEditorComponent(JTable table, Object value,
        boolean isSelected, int row, int column) {
      if ((table == null) || (row < 0) || (column < 0)) {
        return null;
      }

      if ((value instanceof Integer) && ((Integer) value != null)) {
        this.textField.setText(((Integer) value).toString());
        this.textField.setDefaultValue((Integer) value);
      } else {
        this.textField.setText("0");
        this.textField.setDefaultValue(Integer.valueOf(0));
      }

      // this makes no sense, but now we don't have an unused parameter...
      if (isSelected || !isSelected) {

        // here, we set the Background-Color. Maybe someone want's to change
        // that...
        this.textField.setBackground(new Color(255, 255, 128));
      }

      this.textField.setBorder(BorderFactory.createEmptyBorder());
      return this.textField;
    }

    /**
     * (non-Javadoc)
     * 
     * @see javax.swing.CellEditor#stopCellEditing()
     */
    public boolean stopCellEditing() {
      super.stopCellEditing();
      return true;
    }
  }

  /**
   * We need this class because we want the table to display only Integer
   * values. This makes validating the input much easier.
   * 
   * @author T. Ackermann
   */
  private static class IntegerTableModel extends DefaultTableModel {

    /**
     * a generated serial Version UID because IntegerTableModel is serializable.
     */
    private static final long serialVersionUID = 4270055943157017558L;

    /**
     * Constructor creates a new IntegerTableModel-Object.
     * 
     * @param data
     *          The data for the table-cells.
     * @param columnNames
     *          The names for the columns.
     */
    public IntegerTableModel(Object[][] data, Object[] columnNames) {
      super(data, columnNames);
    }

    /**
     * This method tells the table to always use Integers when reading,
     * displaying, showing, entering values in the table.
     * 
     * @param column
     *          The column number.
     * 
     * @return The class of the data in the column. We always return Integer.
     * 
     * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
     */
    public Class<Integer> getColumnClass(int column) {
      // we only have Integers...
      return Integer.class;
    }
  }
	/**
	 * This is the Editor, we use for the input of the String values.
	 * 
	 * @author T. Ackermann
	 */
	private static class StringCellEditor extends DefaultCellEditor {

		/**
		 * a generated serial Version UID because StringCellEditor is serializable.
		 */
		private static final long serialVersionUID = 8203744556751380673L;

		/** stores the StringTextField, that manages the Input */
		private JTextField textField = new JTextField();

		/**
		 * Constructor creates a new StringCellEditor-Object.
		 * 
		 * @param theTextField
		 */
		public StringCellEditor(JTextField theTextField) {
			super(theTextField);
			setClickCountToStart(1);
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.CellEditor#getCellEditorValue()
		 */
		public Object getCellEditorValue() {
			if (textField.getText().length() == 0) {
				return new String("A");
			}

			return textField.getText();
		}

		/**
		 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable,
		 *      java.lang.Object, boolean, int, int)
		 */
		public Component getTableCellEditorComponent(JTable table, Object value,
				boolean isSelected, int row, int column) {
			if ((table == null) || (row < 0) || (column < 0)) {
				return null;
			}

			if ((value instanceof String) && ((String) value != null)) {
				textField.setText((String) value);
			} else {
				textField.setText("A");
			}

			// this makes no sense, but now we don't have an unused parameter...
			if (isSelected || !isSelected) {

				// here, we set the Background-Color. Maybe someone want's to change
				// that...
				textField.setBackground(new Color(255, 255, 128));
			}

			textField.setBorder(BorderFactory.createEmptyBorder());
			return textField;
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.CellEditor#stopCellEditing()
		 */
		public boolean stopCellEditing() {
			super.stopCellEditing();
			return true;
		}
	}
}
