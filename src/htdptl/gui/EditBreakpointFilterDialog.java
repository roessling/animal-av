package htdptl.gui;

import htdptl.facade.Facade;
import htdptl.filter.BreakpointFilter;

import java.awt.event.ActionEvent;

public class EditBreakpointFilterDialog extends AddBreakpointDialog {

  /**
   * 
   */
  private static final long serialVersionUID = 7724682890626798609L;
  private BreakpointFilter filter;

  public EditBreakpointFilterDialog(Facade facade, BreakpointFilter filter) {
    super(facade);
    this.filter = filter;
    this.procedures.setSelectedItem(filter.getProcedure());
    this.times.setText(new Integer(filter.getTimes()).toString());    
  }
  
  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals("ok")) {
      filter.setProcedure(procedures.getSelectedItem().toString());
      filter.setTimes(new Integer(times.getText()));
      FilterTableModel.getInstance().fireTableDataChanged();
      setVisible(false);
      dispose();
    } else if (e.getActionCommand().equals("cancel")) {
      setVisible(false);
      dispose();
    }

  }
  
}
