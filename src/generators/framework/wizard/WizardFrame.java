/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package generators.framework.wizard;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * 
 * @author Jerome
 */
public class WizardFrame extends JFrame {
  int width  = 600;
  int height = 600;
  
  public static final String APP_NAME = "Generator Wizard";

  public WizardFrame() {
    super(APP_NAME);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    add(new WizardPanel(), BorderLayout.CENTER);
    pack();
    setVisible(true);
    setSize(width, height);
  }

  public static void main(String[] args) {
    // Schedule a job for the event dispatch thread:
    // creating and showing this application's GUI.
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        // Turn off metal's use of bold fonts
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        // createAndShowGUI();
        new WizardFrame();
      }
    });
  }
}
