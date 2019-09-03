/*
 * Created on 24.11.2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package animal.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import animal.misc.MessageDisplay;

/**
 * @author guido
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class MainWindowListener implements ActionListener {
//  private Animal animal = null;
  
  public MainWindowListener() {
//  	animal = animalInstance;
  }
  
  /** (non-Javadoc)
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  public void actionPerformed( ActionEvent event) {
  	MessageDisplay.errorMsg("notYetImplemented",
  			MessageDisplay.INFO);
  }
}
