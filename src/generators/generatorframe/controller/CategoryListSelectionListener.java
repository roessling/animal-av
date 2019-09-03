package generators.generatorframe.controller;



import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import generators.generatorframe.store.FilterInfo;
import generators.generatorframe.store.SearchLoader;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * 
 * @author Nora Wester
 *
 */

public class CategoryListSelectionListener implements ListSelectionListener, MouseListener, KeyListener {
	
	SearchLoader collection;
	FilterInfo filterInfo;
	String value;
	
	public CategoryListSelectionListener(){
		collection = SearchLoader.getInstance();
		filterInfo = FilterInfo.getInstance();
	}
	
	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		// TODO Auto-generated method stub
		if(!arg0.getValueIsAdjusting()){
			if(arg0.getSource() instanceof JList){
				@SuppressWarnings("unchecked")
				JList<String> list = (JList<String>)arg0.getSource();
				//System.out.println("list");
				value = list.getSelectedValue();

			}
		}
	}
	
	
	private void dosom(){
	  if(value != null){
      collection.setCategorySelected(value);
      collection.setFristSelection(collection.getSelectedIndexes());
      filterInfo.setNoFilter();
    }
	}
	
  @Override
  public void mouseClicked(MouseEvent arg0) {
    // TODO Auto-generated method stub
    if(arg0.getClickCount() == 2){
     // System.out.println("budö");
      dosom();
    }
  }

  @Override
  public void mouseEntered(MouseEvent arg0) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void mouseExited(MouseEvent arg0) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void mousePressed(MouseEvent arg0) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void mouseReleased(MouseEvent arg0) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void keyPressed(KeyEvent arg0) {
    // TODO Auto-generated method stub
    if(arg0.getKeyCode() == KeyEvent.VK_ENTER){
      //System.out.println("Enter");
      dosom();
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

}
