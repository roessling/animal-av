/**
 * 
 */
package extras.animalsense.ui.edit;

import java.awt.BorderLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 * @author Mihail Mihaylov
 *
 */
public class VariablesTableBase extends Panel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JScrollPane tableSP = null;
	private JButton addNewBtn = null;
	private JTable qTable = null;
	private JPanel jPanel = null;
	/**
	 * This is the default constructor
	 */
	public VariablesTableBase() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.add(getTableSP(), BorderLayout.CENTER);
		this.add(getJPanel(), BorderLayout.SOUTH);
	}

	/**
	 * This method initializes tableSP	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getTableSP() {
		if (tableSP == null) {
			tableSP = new JScrollPane();
			tableSP.setViewportView(getQTable());
		}
		return tableSP;
	}

	/**
	 * This method initializes addNewBtn	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getAddNewBtn() {
		if (addNewBtn == null) {
			addNewBtn = new JButton();
			addNewBtn.setText("Add new");
			addNewBtn.setActionCommand("addNew");
			addNewBtn.addActionListener(this);
		}
		return addNewBtn;
	}

	/**
	 * This method initializes qTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
	public JTable getQTable() {
		if (qTable == null) {
			qTable = new JTable();
		}
		return qTable;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// nothing to be done here		
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setLayout(new BoxLayout(getJPanel(), BoxLayout.X_AXIS));
			jPanel.add(getAddNewBtn(), null);
		}
		return jPanel;
	}

}
