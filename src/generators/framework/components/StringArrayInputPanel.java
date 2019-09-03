/*
 * Created on 27. 11. 2006 by G. Roessling
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
 * StringArrayInputPanel is a Panel with a TextField that sets the number of elements.
 * It is used to setup an edit an array.
 *
 * @author G. Roessling
 */
public class StringArrayInputPanel extends JPanel implements ActionListener,
FocusListener {

	/**
     * a generated serial Version UID because StringArrayInputPanel is serializable.
     */
	private static final long serialVersionUID = 3257289145079313976L;
	
	/** stores the TextField for the number of elements */
	public IntegerTextFieldEx txtNumberOfElements;
	
	/** stores the Table for the elements */
	public ArrayInputTable tblElements;

	/**
	 * Constructor
	 * creates a new StringArrayInputPanel-Object.
	 */
	public StringArrayInputPanel() {
		super();
		String[] values = {"A", "B", "C", "D"};
		init(values);
	}
	
	/**
	 * Constructor
	 * creates a new StringArrayInputPanel-Object.
	 * @param values The initial array elements.
	 */
	public StringArrayInputPanel(String[] values) {
		super();
		
		if (values == null || values.length == 0) {
			String[] elems = {"A", "B", "C", "D"};
			init(elems);
		} else {
			// copy the values
			String[] elems = new String[values.length];
			System.arraycopy(values, 0, elems, 0, values.length ); 
			init(elems);
		}
	}
	
	/**
	 * init
	 * creates all the components and handles their layout
	 */
	private void init(String[] elements) {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		txtNumberOfElements = new IntegerTextFieldEx(1, 128);
		txtNumberOfElements.setDefaultValue(new Integer(8));
		txtNumberOfElements.setText(Integer.toString(elements.length));
		txtNumberOfElements.addActionListener(this);
		txtNumberOfElements.addFocusListener(this);
		txtNumberOfElements.setAlignmentX(0.0f);
		tblElements = new ArrayInputTable(elements);
		tblElements.setAlignmentX(0.0f);
		//JLabel values = new JLabel("Values:");
		//values.setAlignmentX(0.0f);
		
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.LINE_AXIS));
		p.add(new JLabel("Number of elements:"));
		p.add(Box.createRigidArea(new Dimension(10,1)));
		p.add(txtNumberOfElements);
		p.setAlignmentX(0.0f);
		
		add(p);
		add(Box.createRigidArea(new Dimension(1,8)));
		//add(values);
		add(Box.createRigidArea(new Dimension(1,2)));
		add(tblElements);
	}
	
	/**
	 * updateTable
	 * updates the number of elements in the table
	 */
	private void updateTable() {
		tblElements.setNumberOfElements(
			txtNumberOfElements.getValue().intValue());
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
