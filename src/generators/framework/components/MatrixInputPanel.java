/*
 * Created on 16.06.2005 by T. Ackermann
 */
package generators.framework.components;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * MatrixInputPanel is a Panel with a TextField that sets the number of elements.
 * It is used to setup an edit an Matrix.
 *
 * @author T. Ackermann
 */
public class MatrixInputPanel extends JPanel implements ActionListener,
FocusListener {

	/**
     * a generated serial Version UID because MatrixInputPanel is serializable.
     */
	private static final long serialVersionUID = 3257289145079313976L;
	
	/** stores the TextField for the number of rows */
	public IntegerTextFieldEx txtNumberOfRows;
	/** stores the TextField for the number of columns */
	public IntegerTextFieldEx txtNumberOfColumns;
	
	/** stores the Table for the elements */
	public MatrixInputTable tblElements;

	public static final int[][] DEFAULT_INT_ARRAY = new int[][]{{1,2,3,4},{5,6,7,8}};
	public static final String[][] DEFAULT_STRING_ARRAY = 
		new String[][]{{"A", "B", "C", "D"}, {"E", "F", "G", "H"}};

	/**
	 * Constructor
	 * creates a new MatrixInputPanel-Object.
	 */
	public MatrixInputPanel() {
		super();
		int[][] values = DEFAULT_INT_ARRAY;
		init(values);
	}
	
	/**
	 * Constructor
	 * creates a new MatrixInputPanel-Object.
	 * @param values The initial Matrix elements.
	 */
	public MatrixInputPanel(int[][] values) {
		super();
		
		if (values == null || values.length == 0) {
			int[][] elems = DEFAULT_INT_ARRAY;
			init(elems);
		} else {
			int[][] elems = copyMatrix(values);
			init(elems);
		}
	}

	private int[][] copyMatrix(int[][] input) {
		int[][] result = null;
		if (input == null) 
			return copyMatrix(DEFAULT_INT_ARRAY);
		int nrCols = input.length, colLength = 0;
		result = new int[nrCols][];
		for (int i = 0; i < nrCols; i++) {
			if (input[i] == null)
				result[i] = new int[0];
			else {
				colLength = input[i].length;
				result[i] = new int[colLength];
				System.arraycopy(input[i], 0, result[i], 0, colLength);
			}
		}
		return result;
	}
	
	/**
	 * Constructor
	 * creates a new MatrixInputPanel-Object.
	 * @param values The initial Matrix elements.
	 */
	public MatrixInputPanel(String[][] values) {
		super();
		
		if (values == null || values.length == 0) {
			String[][] elems = DEFAULT_STRING_ARRAY;
			init(elems);
		} else {
			String[][] elems = copyMatrix(values);
			init(elems);
		}
	}

	private String[][] copyMatrix(String[][] input) {
		String[][] result = null;
		if (input == null) 
			return copyMatrix(DEFAULT_STRING_ARRAY);
		int nrCols = input.length, colLength = 0;
		result = new String[nrCols][];
		for (int i = 0; i < nrCols; i++) {
			if (input[i] == null)
				result[i] = new String[0];
			else {
				colLength = input[i].length;
				result[i] = new String[colLength];
				System.arraycopy(input[i], 0, result[i], 0, colLength);
			}
		}
		return result;
	}

	/**
	 * init
	 * creates all the components and handles their layout
	 */
	private void init(int[][] elements) {
		commonInit(elements.length);

		tblElements = new MatrixInputTable(elements);
		finishCommonInit();
	}
	
	private void commonInit(int defaultSize) {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		txtNumberOfRows = new IntegerTextFieldEx(1, 128);
		txtNumberOfRows.setDefaultValue(new Integer(8));
		txtNumberOfRows.setText(Integer.toString(defaultSize));
		txtNumberOfRows.addActionListener(this);
		txtNumberOfRows.addFocusListener(this);
		txtNumberOfRows.setAlignmentX(0.0f);

		txtNumberOfColumns = new IntegerTextFieldEx(1, 128);
		txtNumberOfColumns.setDefaultValue(new Integer(8));
		txtNumberOfColumns.setText(Integer.toString(defaultSize));
		txtNumberOfColumns.addActionListener(this);
		txtNumberOfColumns.addFocusListener(this);
		txtNumberOfColumns.setAlignmentX(0.0f);		
	}
	
	private void finishCommonInit() {
		tblElements.setAlignmentX(0.0f);
		//JLabel values = new JLabel("Values:");
		//values.setAlignmentX(0.0f);
		
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.LINE_AXIS));
		p.add(new JLabel("Number of rows:"));
		p.add(Box.createRigidArea(new Dimension(10,1)));
		p.add(txtNumberOfRows);
		p.setAlignmentX(0.0f);
		
		p.add(new JLabel("Number of columns:"));
		p.add(Box.createRigidArea(new Dimension(10,1)));
		p.add(txtNumberOfColumns);
		p.setAlignmentX(0.0f);
		
		add(p);
		add(Box.createRigidArea(new Dimension(1,8)));
		//add(values);
		add(Box.createRigidArea(new Dimension(1,2)));
		add(tblElements);
	}
	
	/**
	 * init
	 * creates all the components and handles their layout
	 */
	private void init(String[][] elements) {
		commonInit(elements.length);
		tblElements = new MatrixInputTable(elements);
		finishCommonInit();
	}

	/**
	 * updateTable
	 * updates the number of elements in the table
	 */
	private void updateTable() {
		tblElements.setNumberOfRows(txtNumberOfRows.getValue().intValue());
		tblElements.setNumberOfColumns(txtNumberOfColumns.getValue().intValue());
	}

	/** (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e == null) return;
		updateTable();
	}
	
	/** (non-Javadoc)
	 * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
	 */
	public void focusLost(FocusEvent e) {
		if (e == null) return;
		updateTable();
	}

	/** (non-Javadoc)
	 * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
	 */
	public void focusGained(FocusEvent e) {
		if (e == null) return;
		// do nothing
	}
}
