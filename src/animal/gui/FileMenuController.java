/*
 * Created on 14.07.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package animal.gui;

import htdptl.gui.HtDPTLWizard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import translator.AnimalTranslator;
import animal.exchange.AnimationExporter;
import animal.exchange.AnimationImporter;
import animal.exchange.AnimationPrintJob;
import animal.main.Animal;
import animal.main.Animation;
import animal.misc.MessageDisplay;
import animal.vhdl.exchange.VHDLAnimationImporter;

/**
 * @author Guido Roessling (roessling@acm.org)
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class FileMenuController implements ActionListener {

	/**
	 * The animal instance of which we will be working!
	 */
	private Animal animal;


	/**
	 * create a new file menu controller
	 * 
	 * @param animalInstance the current instance of Animal
	 */
	public FileMenuController(Animal animalInstance) {
		super();
		
		// store the reference to the Animal instance!
		animal = animalInstance;
	}

	
	/**
	 * handle the events thrown from the "File" menu
	 * 
	 * @param event the event describing the underlying action
	 */
	public void actionPerformed(ActionEvent event) {
		// make sure the Animal reference exists!
		if (animal == null)
			animal = Animal.get();
		
		String command = event.getActionCommand();
		if (command.equals(MainMenuBar.COLLECT_GARBAGE_NOW)) {
			System.gc();
			JOptionPane.showMessageDialog(animal, 
					AnimalTranslator.translateMessage("garbageCollected"));
		}
		else if (command.equals(MainMenuBar.DEMO_ANIMATION))
			animal.demoAnimation();
		else if (command.equals(MainMenuBar.NEW) || command.equals(MainMenuBar.EMPTY))
			animal.newFile();
		else if (command.equals(MainMenuBar.OPEN))
			AnimationImporter.importAnimation();
    else if (command.equals(MainMenuBar.OPENVHDL))
      VHDLAnimationImporter.importAnimation();
		else if (command.equals(MainMenuBar.OPEN_REPOSITORY))
			animal.openRepository();
		else if (command.equals(MainMenuBar.HTDP_TL))
			HtDPTLWizard.instance.show();
		else if (command.equals(MainMenuBar.EXAMPLES)) {
//		  LoadFromCollection lfc = 
		  new LoadFromCollection(animal, AnimalCollectionTypes.ALL); //GENERATORS);
//		  JFrame f = new JFrame();
//	    f.getContentPane().add(lfc);
//	    f.pack();
//	    f.setVisible(true);
		}
		else if (command.equals(MainMenuBar.GENERATOR))
			animal.openGenerator();
		else if (command.equals(MainMenuBar.PRINT))
			new AnimationPrintJob(animal);
		else if (command.equals(MainMenuBar.RELOAD))
			animal.reloadFile();
		else if (command.equals(MainMenuBar.QUIT))
			animal.quitAnimal();
		else if (command.equals(MainMenuBar.SAVE)) {
			Animation currentAnimation = animal.getAnimation();
			if (currentAnimation != null)
			  Animal.get().getScriptInputWindow().doClick_ScriptingInputSAVE();
				//AnimationExporter.saveAnimation(currentAnimation);  //TODO
		}
		else if (command.equals(MainMenuBar.SAVE_AS)) {
			Animation currentAnimation = animal.getAnimation();
			if (currentAnimation != null)
				AnimationExporter.exportAnimation(currentAnimation);
		}
		else 
      MessageDisplay.errorMsg("nothingAssoc", command, MessageDisplay.RUN_ERROR);
	}
}
