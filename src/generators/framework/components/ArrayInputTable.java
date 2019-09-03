/*
 * Created on 25.11.2004 by T. Ackermann
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
 * ArrayInputTable is a visual class that allows entering Arrays.
 * 
 * @author T. Ackermann
 */
public class ArrayInputTable extends JScrollPane {

	/** Array stores Integer */
	public static final char ARRAY_TYPE_INTEGER = 1;

	/** Array stores String */
	public static final char ARRAY_TYPE_STRING = 2;

	/**
	 * a generated serial Version UID because ArrayInputTable is serializable.
	 */
	private static final long serialVersionUID = -6548779207990710834L;

	/** stores the JTable-Object */
	private JTable myTable;

	/** stores the local String for the Table Header ("Element") */
	private String strLocalHeader = "Element";

	/** stores the type of this Array */
	private char arrayType = ARRAY_TYPE_INTEGER;

	/** stores the number of elements */
	private int iNumberOfElements = 3;

	/**
	 * Constructor creates a new 3-Element-ArrayInputTable-Object.
	 */
	public ArrayInputTable() {
		super();
		iNumberOfElements = 3;
		arrayType = ARRAY_TYPE_INTEGER;
		init();
	}

	/**
	 * Constructor creates a new ArrayInputTable-Object.
	 * 
	 * @param numElements
	 *          The number of elements in the table.
	 */
	public ArrayInputTable(int numElements) {
		super();
		iNumberOfElements = (numElements >= 1) ? numElements : 3;
//		if (numElements < 1) {
//			numElements = 3;
//		}
//
//		iNumberOfElements = numElements;
		arrayType = ARRAY_TYPE_INTEGER;
		init();
	}

	/**
	 * Creates a new ArrayInputTable object and uses the passed int-values.
	 * 
	 * @param newValues
	 *          The int-values to use in the table.
	 */
	public ArrayInputTable(int[] newValues) {
		super();
		iNumberOfElements = 3;
		arrayType = ARRAY_TYPE_INTEGER;
		init();
		setValues(newValues);
	}

	/**
	 * Creates a new ArrayInputTable object and uses the passed String-values.
	 * 
	 * @param newValues
	 *          The String-values to use in the table.
	 */
	public ArrayInputTable(String[] newValues) {
		super();
		iNumberOfElements = 3;
		arrayType = ARRAY_TYPE_STRING;
		init();
		setValues(newValues);
	}

	/**
	 * Sets the ArrayType of the table. This can be either ARRAY_TYPE_INTEGER or
	 * ARRAY_TYPE_STRING.
	 * 
	 * @param newArrayType
	 *          The new ArrayType of the table. This can be either
	 *          ARRAY_TYPE_INTEGER or ARRAY_TYPE_STRING.
	 */
	public void setArrayType(char newArrayType) {
		if ((newArrayType != ARRAY_TYPE_INTEGER)
				&& (newArrayType != ARRAY_TYPE_STRING)) {
			return;
		}

		if (newArrayType == arrayType) {
			return;
		}

		arrayType = newArrayType;
		init();
	}

	/**
	 * Returns the ArrayType of the table. This can be either ARRAY_TYPE_INTEGER
	 * or ARRAY_TYPE_STRING.
	 * 
	 * @return The ArrayType of the table. This can be either ARRAY_TYPE_INTEGER
	 *         or ARRAY_TYPE_STRING.
	 */
	public char getArrayType() {
		return arrayType;
	}

	/**
	 * Returns the values as an int[].
	 * 
	 * @return The values as an int[].
	 */
	public int[] getIntValues() {
		int[] retval = new int[iNumberOfElements];

		for (int j = 0; j < iNumberOfElements; j++) {
			if (arrayType == ARRAY_TYPE_INTEGER) {
				retval[j] = ((Integer) myTable.getValueAt(0, j)).intValue();
			} else {
				retval[j] = 0;
			}
		}

		return retval;
	}

	/**
	 * Sets the String that is used for the header of each column. In english this
	 * could be "Element" for example.
	 * 
	 * @param strNewLocalHeader
	 *          String that should be used for the header of each column.
	 */
	public void setLocalHeaderString(String strNewLocalHeader) {
		if (strNewLocalHeader == null) {
			return;
		}

		// we allow zero-length Strings...
		String newLocalHeader = strNewLocalHeader.trim();
		if (strLocalHeader.equals(newLocalHeader)) {
			return;
		}

		strLocalHeader = newLocalHeader;

		// update header...
		for (int i = 0; i < myTable.getColumnCount(); i++) {
			TableColumn column = myTable.getColumnModel().getColumn(i);

			column.setHeaderValue((newLocalHeader + " " + Integer.toString(i))
					.trim());

			// here we could change the size of the column...
		}
	}

	/**
	 * Returns the String that is used for the header of each column. In English,
	 * this could be "Element" for example.
	 * 
	 * @return The String that is used for the header of each column.
	 */
	public String getLocalHeaderString() {
		return strLocalHeader;
	}

	/**
	 * Sets the number of elements in the table.
	 * 
	 * @param numElements
	 *          The new number of elements in the table.
	 */
	public void setNumberOfElements(int numElements) {
		if (numElements < 1) {
			return;
		}

		changeDimensions(numElements);
	}

	/**
	 * Returns the number of elements in the table.
	 * 
	 * @return Returns the number of elements in the table.
	 */
	public int getNumberOfElements() {
		return iNumberOfElements;
	}

	/**
	 * Returns the values as a String[].
	 * 
	 * @return The values as a String[].
	 */
	public String[] getStringValues() {
		String[] retval = new String[iNumberOfElements];

		for (int j = 0; j < iNumberOfElements; j++) {
			if (arrayType == ARRAY_TYPE_STRING) {
				retval[j] = (String) myTable.getValueAt(0, j);
			} else {
				retval[j] = "A";
			}
		}

		return retval;
	}

	/**
	 * Sets the values via an int[].
	 * 
	 * @param newValues
	 *          The values as an int[].
	 */
	public void setValues(int[] newValues) {
		if (newValues == null) {
			return;
		}

		int iNewColumns = newValues.length;

		if (iNewColumns == 0) {
			return;
		}

		changeDimensions(iNewColumns);

		// enter new values
		for (int j = 0; j < iNewColumns; j++) {
			if (newValues.length >= (j - 1)) {
				Integer intObj = new Integer(newValues[j]);

				myTable.setValueAt(intObj, 0, j);
			} else {
				myTable.setValueAt(new Integer(0), 0, j);
			}
		}
	}

	/**
	 * Sets the values via a String[].
	 * 
	 * @param newValues
	 *          The values as a String[].
	 */
	public void setValues(String[] newValues) {
		if (newValues == null) {
			return;
		}

		int iNewColumns = newValues.length;

		if (iNewColumns == 0) {
			return;
		}

		changeDimensions(iNewColumns);

		// enter new values
		for (int j = 0; j < iNewColumns; j++) {
			if (newValues.length >= (j - 1)) {
				myTable.setValueAt(newValues[j], 0, j);
			} else {
				myTable.setValueAt("A", 0, j);
			}
		}
	}

	/**
	 * Changes the number of elements in the table. If the table gets smaller, the
	 * old values are taken oven. If the table grows, the new elements are
	 * initialized with value "0".
	 * 
	 * @param numColumns
	 *          The new number of elements.
	 */
	private void changeDimensions(int numColumns) {
		if (numColumns < 1) {
			return;
		}

		int iOldCols = iNumberOfElements;

		Object[] oldData = new Object[numColumns];

		// Backup old data
		for (int j = 0; j < numColumns; j++) {
			if (j >= iOldCols) {
				if (arrayType == ARRAY_TYPE_INTEGER) {
					oldData[j] = new Integer(0);
				} else {
					oldData[j] = "A";
				}
			} else {
				oldData[j] = myTable.getValueAt(0, j);
			}
		}

		// set new values and re-init
		iNumberOfElements = numColumns;

		init();

		// reenter old values
		for (int j = 0; j < numColumns; j++) {
			myTable.setValueAt(oldData[j], 0, j);
		}
	}

	/**
	 * Initializes the ArrayInputTable, creates the Table, and fills the values
	 * with the default values (0 or "A").
	 */
	private void init() {
		String[] columnNames = new String[iNumberOfElements];
		Object[][] data = new Object[1][iNumberOfElements];

		myTable = new JTable();
		myTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

		if (arrayType == ARRAY_TYPE_INTEGER) {

			// we have an Integer Array
			data[0] = new Integer[iNumberOfElements];

			for (int j = 0; j < iNumberOfElements; j++) {
				columnNames[j] = (strLocalHeader + " " + Integer.toString(j))
						.trim();
				data[0][j] = new Integer(0);
			}

			myTable.setModel(new IntegerTableModel(data, columnNames));

			// for all columns set the CellEditor
			for (int i = 0; i < myTable.getColumnCount(); i++) {
				TableColumn column = myTable.getColumnModel().getColumn(i);
				IntegerCellEditor editor = new IntegerCellEditor();

				column.setCellEditor(editor);
			}
		} else {

			// we have a String Array
			data[0] = new String[iNumberOfElements];

			for (int j = 0; j < iNumberOfElements; j++) {
				columnNames[j] = "Element " + Integer.toString(j);
				data[0][j] = "A";
			}

			myTable.setModel(new DefaultTableModel(data, columnNames));

			// for all columns set the CellEditor
			for (int i = 0; i < myTable.getColumnCount(); i++) {
				TableColumn column = myTable.getColumnModel().getColumn(i);
				StringCellEditor editor = new StringCellEditor(new JTextField());

				column.setCellEditor(editor);
			}
		}

		// here we set the size of the ArrayInputTable
		int iNewHeight = (int) getHorizontalScrollBar().getPreferredSize()
				.getHeight();

		iNewHeight += myTable.getPreferredSize().getHeight();
		iNewHeight += 24;

		setMinimumSize(new Dimension(100, iNewHeight));
		setPreferredSize(new Dimension(200, iNewHeight));

		// this makes the table look good
		myTable.setCellSelectionEnabled(true);
		myTable.getTableHeader().setReorderingAllowed(false);
		myTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		// the scrollbar scrolls the table
		setViewportView(myTable);
	}

	/*
	 * ******************************************************************* BELOW
	 * ARE TWO HELPER CLASSES
	 * *******************************************************************
	 */

	/**
	 * This is the Editor, we use for the input of the Integer values. It uses a
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
			return textField.getValue();
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
				textField.setText(((Integer) value).toString());
				textField.setDefaultValue((Integer) value);
			} else {
				textField.setText("0");
				textField.setDefaultValue(new Integer(0));
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
