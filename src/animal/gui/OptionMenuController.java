/*
 * Created on 14.07.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package animal.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;

import animal.dialog.OptionDialog;
import animal.main.Animal;
import animal.misc.ComponentConfigurer;
import animal.misc.MessageDisplay;


/**
 * @author guido
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class OptionMenuController implements ActionListener {

	/**
	 * The animal instance of which we will be working!
	 */
	private Animal animalInstance;
	
	
	/**
	 * The animal instance of which we will be working!
	 */
	private ComponentConfigurer configurer;
	
	
	/**
	 * generate a controller for the "Edit" menu
	 * 
	 * @param animal the current instance of Animal
	 */
	public OptionMenuController(Animal animal) {
		super();
		
		// store the reference to the Animal instance
		animalInstance = animal;
	}

	
	/**
	 * handle the events thrown from the "File" menu
	 * 
	 * @param event the event describing the underlying action
	 */
	public void actionPerformed(ActionEvent event) {
    // retrieve the action command used	
		String command = event.getActionCommand();
//    DrawCanvas drawCanvas = 
		AnimalMainWindow.getWindowCoordinator().getDrawWindow(false).getDrawCanvas();
		// decide on the appropriate action
		if (command.equals(MainMenuBar.PREFERENCES))
		  OptionDialog.getOptionDialog(animalInstance).setVisible(true);
		else if (command.equals(MainMenuBar.THREE_BUTTON_MOUSE))
		    DrawCanvas.setMouseType(
		            InputEvent.BUTTON1_MASK,
		            InputEvent.BUTTON2_MASK,
		            InputEvent.BUTTON3_MASK);
		else if (command.equals(MainMenuBar.TWO_BUTTON_MOUSE))
		    DrawCanvas.setMouseType(
		            InputEvent.BUTTON1_MASK,
		            InputEvent.BUTTON3_MASK,
		            InputEvent.BUTTON2_MASK);
		else if (command.equals(MainMenuBar.COMPONENT_CONFIGURATION)) {
			if (configurer == null)
				 configurer = new ComponentConfigurer();
			configurer.setVisible(true);
		}
		else if (command.equals(MainMenuBar.BOUNDING_BOX))
			Animal.get().showBoundingBox();
 		else 
      MessageDisplay.errorMsg("nothingAssoc", command, 
      		MessageDisplay.RUN_ERROR);
	}
}