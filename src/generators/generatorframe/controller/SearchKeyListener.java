package generators.generatorframe.controller;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;

import generators.generatorframe.filter.FilterCoordinator;
import generators.generatorframe.view.AlgoListing;

/**
 * 
 * @author Nora Wester
 *
 */

public class SearchKeyListener implements KeyListener, ActionListener {
	
	int type;
	private JTextField field;
	
	public SearchKeyListener(int type){
		this.type = type;
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
//		if(arg0.getKeyCode() == KeyEvent.VK_ENTER){
//			if(arg0.getSource() instanceof JTextField){
//				JTextField field = (JTextField)arg0.getSource();
//				//System.out.println(field.getText() + "text");
//				FilterCoordinator.coorinate(field.getText(), type);
//				
////				Component view = arg0.getComponent().getParent();
////				while(!(view instanceof Listing)){
////					
////					view = view.getParent();
////				}
//				
//			}
//		}
	  if(field == null){
      if(arg0.getSource() instanceof JTextField){
        field = (JTextField) arg0.getSource();
      }
    }
    
    if(arg0.getKeyCode() == KeyEvent.VK_ENTER){
        FilterCoordinator.coorinate(field.getText(), type);
        field.setText("");
        try {
          AlgoListing.getLatestAlgoListing().setNoFilter();
        } catch (Exception e2) {}
      }

	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

  @Override
  public void actionPerformed(ActionEvent e) {
    // TODO Auto-generated method stub
    if(field != null){
      FilterCoordinator.coorinate(field.getText(), type);
      field.setText("");
    }else{
      FilterCoordinator.coorinate("", type);
    }
    try {
      AlgoListing.getLatestAlgoListing().setNoFilter();
    } catch (Exception e2) {}
  }

}
