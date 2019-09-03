package htdptl.gui.listener;

import htdptl.gui.CreatingAnimationDialog;
import htdptl.gui.HtDPTLWizard;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;


public class ButtonPaneListener implements ActionListener {

  HtDPTLWizard wizard;
  private CreatingAnimationDialog dialog;

	public ButtonPaneListener(HtDPTLWizard wizard) {
		this.wizard = wizard;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("back")) {
			wizard.back();
		}
		else if (e.getActionCommand().equals("next")) {
			wizard.next();
		}
		else if (e.getActionCommand().equals("OK")) {
			wizard.finish();
		}
		else if (e.getActionCommand().equals("cancel")) {
			wizard.cancel();
		}
		else if (e.getActionCommand().equals("finish")) {
		  dialog = new CreatingAnimationDialog(null);
		  SwingUtilities.invokeLater(new Runnable() {
	      public void run() {
	        wizard.finish();
	        getDialog().setVisible(false);
	      }
	    });
			
		}
	}

  protected Dialog getDialog() {
    return dialog;
  }

}
