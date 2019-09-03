package htdptl.gui;

import htdptl.facade.Facade;
import htdptl.filter.ProcedureFilter;

import java.awt.event.ActionEvent;

public class EditProcedureFilterDialog extends AddProcedureFilterDialog {

  /**
   * 
   */
  private static final long serialVersionUID = -1892718443762667797L;
  private ProcedureFilter filter;

  public EditProcedureFilterDialog(Facade facade, ProcedureFilter filter) {
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
