package generators.generatorframe.controller;




import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import generators.generatorframe.store.SearchLoader;

import javax.swing.DefaultListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


/**
 * 
 * @author Nora Wester
 *
 */


public class GeneratorListSelectionListener implements ListSelectionListener, KeyListener, MouseListener {

	SearchLoader loader;
	int index;
	
	public GeneratorListSelectionListener(){
		loader = SearchLoader.getInstance();
	}
	
	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		// TODO Auto-generated method stub
		if(!arg0.getValueIsAdjusting()){

			DefaultListSelectionModel list = (DefaultListSelectionModel)arg0.getSource();

			index = list.getMinSelectionIndex();
			
//			if(index != -1){
//			 
//				loader.setSelectedGenerator(index);
//			}
			
		}	
	}

  @Override
  public void mouseClicked(MouseEvent e) {
    // TODO Auto-generated method stub
    if(e.getClickCount() == 2 && index != -1){
      loader.setSelectedGenerator(index);
    }
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void mouseExited(MouseEvent e) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void mousePressed(MouseEvent e) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void keyPressed(KeyEvent e) {
    // TODO Auto-generated method stub
    if(e.getKeyCode() == KeyEvent.VK_ENTER && index != -1){
      loader.setSelectedGenerator(index);
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void keyTyped(KeyEvent e) {
    // TODO Auto-generated method stub
    
  }

}
