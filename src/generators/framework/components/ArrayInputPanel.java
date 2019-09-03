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
 * ArrayInputPanel is a Panel with a TextField that sets the number of elements.
 * It is used to setup an edit an array.
 *
 * @author T. Ackermann
 */
public class ArrayInputPanel extends JPanel implements ActionListener,
FocusListener {

	/**
     * a generated serial Version UID because ArrayInputPanel is serializable.
     */
	private static final long serialVersionUID = 3257289145079313976L;
	
	/** stores the TextField for the number of elements */
	public IntegerTextFieldEx txtNumberOfElements;
	
	/** stores the Table for the elements */
	public ArrayInputTable tblElements;

	/**
	 * Constructor
	 * creates a new ArrayInputPanel-Object.
	 */
	public ArrayInputPanel() {
		super();
		int[] values = {1,2,3,4,5,6,7,8};
		init(values);
	}
	
	/**
	 * Constructor
	 * creates a new ArrayInputPanel-Object.
	 * @param values The initial array elements.
	 */
	public ArrayInputPanel(int[] values) {
		super();
		
		if (values == null || values.length == 0) {
			int[] elems = {1,2,3,4,5,6,7,8};
			init(elems);
		} else {
			// copy the values
			int[] elems = new int[values.length];
			System.arraycopy( values, 0, elems, 0, values.length ); 
			init(elems);
		}
	}
	
	/**
	 * init
	 * creates all the components and handles their layout
	 */
	private void init(int[] elements) {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		this.txtNumberOfElements = new IntegerTextFieldEx(1, 128);
		this.txtNumberOfElements.setDefaultValue(new Integer(8));
		this.txtNumberOfElements.setText(Integer.toString(elements.length));
		this.txtNumberOfElements.addActionListener(this);
		this.txtNumberOfElements.addFocusListener(this);
		this.txtNumberOfElements.setAlignmentX(0.0f);
		this.tblElements = new ArrayInputTable(elements);
		this.tblElements.setAlignmentX(0.0f);
		//JLabel values = new JLabel("Values:");
		//values.setAlignmentX(0.0f);
		
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.LINE_AXIS));
		p.add(new JLabel("Number of elements:"));
		p.add(Box.createRigidArea(new Dimension(10,1)));
		p.add(this.txtNumberOfElements);
		p.setAlignmentX(0.0f);
		
		this.add(p);
		this.add(Box.createRigidArea(new Dimension(1,8)));
		//this.add(values);
		this.add(Box.createRigidArea(new Dimension(1,2)));
		this.add(this.tblElements);
	}
	
	/**
	 * updateTable
	 * updates the number of elements in the table
	 */
	private void updateTable() {
		this.tblElements.setNumberOfElements(
			this.txtNumberOfElements.getValue().intValue());
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
