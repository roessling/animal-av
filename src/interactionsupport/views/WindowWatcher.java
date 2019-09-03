/*
 * Created on 21.12.2005 by Guido Roessling (roessling@acm.org>
 */
package interactionsupport.views;

import interactionsupport.controllers.InteractionController;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;


/**
 * A window listener that only watches out for window closing
 *
 * @author $author$
 * @version $Revision$
 */
public class WindowWatcher implements WindowListener {
  /**
   * the InteractionController instance currently at work
   */
  private InteractionController handler;
  
  /**
   * the current question ID
   */
  private String questionID;

  /**
   * assigns the current question ID
   *
   * @param id the current question's ID
   */
  public void setID(String id) {
    questionID = id;
  }

  /**
   * stores the current interaction handler at work
   *
   * @param module the current interaction handler at work
   */
  public void setInstance(InteractionController module) {
    handler = module;
  }

  /**
   * does nothing if the window is activated
   *
   * @param arg0 the window event
   */
  public void windowActivated(WindowEvent arg0) {
    // TODO Auto-generated method stub
  }

  /**
   * does nothing if the window is closed
   *
   * @param arg0 the window event
   */
  public void windowClosed(WindowEvent arg0) {
		// do nothing
  }

  /**
   * informs the handler if the window is closing
   *
   * @param arg0 the window event
   */
  public void windowClosing(WindowEvent arg0) {
    if (handler != null) {
      handler.closeElement(questionID);
    }
  }

  /**
   * does nothing if the window is deactivated
   *
   * @param arg0 the window event
   */
  public void windowDeactivated(WindowEvent arg0) {
    // TODO Auto-generated method stub
  }

  /**
   * does nothing if the window is deiconified
   *
   * @param arg0 the window event
   */
  public void windowDeiconified(WindowEvent arg0) {
    // TODO Auto-generated method stub
  }

  /**
   * does nothing if the window is iconified
   *
   * @param arg0 the window event
   */
  public void windowIconified(WindowEvent arg0) {
    // TODO Auto-generated method stub
  }

  /**
   * does nothing if the window is opened
   *
   * @param arg0 the window even
   */
  public void windowOpened(WindowEvent arg0) {
    // TODO Auto-generated method stub
  }
}
