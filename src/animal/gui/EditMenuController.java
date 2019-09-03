/*
 * Created on 14.07.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package animal.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import animal.main.Animal;
import animal.misc.MessageDisplay;

/**
 * @author guido
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class EditMenuController implements ActionListener {

	/**
	 * The main window from which the events originate
	 */
	private WindowCoordinator windowCoordinator;

	/**
	 * generate a controller for the "Edit" menu
	 * 
	 * @param windowController
	 *          the current instance for coordinating all windows
	 */
	public EditMenuController(WindowCoordinator windowController) {
		super();

		// store the reference to the Animal main window instance
		windowCoordinator = windowController;
	}

	/**
	 * handle the events thrown from the "File" menu
	 * 
	 * @param event
	 *          the event describing the underlying action
	 */
	public void actionPerformed(ActionEvent event) {
		// retrieve the action command used
		String command = event.getActionCommand();
		if (command.equals(MainMenuBar.TOGGLE_DRAWING_WINDOW)) 
	    if (Animal.PREVENT_EDITING) {
        JOptionPane.showMessageDialog(Animal.get(), 
              "Editing is not possible within CrypTool, please start Animal\n" 
              +"with java -jar Animal-x.y.z.jar from the command line,\nor by double clicking on the file");
	    }
	    else
          windowCoordinator.showDrawWindow();
		else if (command.equals(MainMenuBar.TOGGLE_ANIMATION_OVERVIEW))
      if (Animal.PREVENT_EDITING) {
        JOptionPane.showMessageDialog(Animal.get(), 
              "Editing is not possible within CrypTool, please start Animal\n" 
              +"with java -jar Animal-x.y.z.jar from the command line,\nor by double clicking on the file");
      }
      else
        windowCoordinator.showAnimationOverview();
		else if (command.equals(MainMenuBar.TOGGLE_ANIMATION_WINDOW))
			windowCoordinator.showAnimationWindow();
		else if (command.equals(MainMenuBar.INPUT_SCRIPTING)) {
      if (Animal.PREVENT_EDITING) {
        JOptionPane.showMessageDialog(Animal.get(), 
              "Editing is not possible within CrypTool, please start Animal\n" 
              +"with java -jar Animal-x.y.z.jar from the command line,\nor by double clicking on the file");
      }
      else {
        AnimalScriptInputWindow siw = Animal.get().getScriptInputWindow();
        siw.setVisible(true);
      }
		}
		else if (command.equals(MainMenuBar.TOGGLE_TIME_LINE_WINDOW))
			windowCoordinator.showTimeLineWindow();
		else if (command.equals(MainMenuBar.TOGGLE_OBJECTS_WINDOW))
			windowCoordinator.showObjectsWindow();
		else if (command.equals(MainMenuBar.TOGGLE_ANNOTATION_WINDOW))
			windowCoordinator.showAnnotationWindow();
        else if (command.equals(MainMenuBar.TOGGLE_VARIABLE_VIEW))
            windowCoordinator.showVariableView();
		else
      MessageDisplay.errorMsg("nothingAssoc", command, 
      		MessageDisplay.RUN_ERROR);
	}

  /**
   * @param zoomIn
   *          if true zooms in, if false zooms out
   */
  public void zoom(boolean zoomIn) {

    if (windowCoordinator != null) {
      windowCoordinator.zoom(zoomIn);
    }

  }
}
