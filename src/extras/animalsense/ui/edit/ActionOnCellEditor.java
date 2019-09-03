/**
 * 
 */
package extras.animalsense.ui.edit;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * @author Mihail Mihaylov
 *
 */
public class ActionOnCellEditor extends AbstractCellEditor implements TableCellEditor, TableCellRenderer, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2895893033152009911L;
	private JButton button;
	private Object value;
	private ActionListener actionListener;

	/**
	 * 
	 */
	public ActionOnCellEditor(String actionCommand, String label, ActionListener actionListener) {
		super();
		button = new JButton(label);
        button.setActionCommand(actionCommand);
        button.addActionListener(this);
        // this.questionEditor = questionEditor;
		this.actionListener = actionListener;
		// button.setBorderPainted(false);
		// button.setOpaque(true);
	}

	/* (non-Javadoc)
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue() {
		return value;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		this.value = value;
		return button;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		fireEditingStopped(); //Make the renderer reappear.
		e.setSource(this.value);
		actionListener.actionPerformed(e);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
	  
		return button;
	}
	
}
