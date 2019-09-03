/*
 * Created on 14.07.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package animal.gui;

import interactionsupport.models.backend.AnimalEvalBackend;
import interactionsupport.models.backend.BackendInterface;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.swing.JEditorPane;

import translator.AnimalTranslator;
import animal.dialog.AboutDialog;
import animal.dialog.AnimInfoDialog;
import animal.dialog.HTMLView;
import animal.dialog.HelpWindow;
import animal.main.Animal;
import animal.main.AnimalConfiguration;
import animal.main.AnimationWindow;
import animal.misc.MessageDisplay;

/**
 * @author guido
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class HelpMenuController implements ActionListener {
	
	/**
	 * The animal instance of which we will be working!
	 */
	private HelpWindow tutorialWindow;
	
  private HTMLView   animalScriptDisplay;

  private int        zoomCounter = 0;
	
	/**
	 * generate a controller for the "Edit" menu
	 */
	public HelpMenuController() {
		super();
	}

	
	/**
	 * handle the events thrown from the "File" menu
	 * 
	 * @param event the event describing the underlying action
	 */
	public void actionPerformed(ActionEvent event) {
    // retrieve the action command used	
		String command = event.getActionCommand();
		
		// determine the appropriate action to take!
		if (MainMenuBar.ABOUT.equals(command))
		  AboutDialog.getAboutDialog(Animal.get()).setVisible(true);
		else if (MainMenuBar.ANIMALSCRIPT_DEFINITION.equals(command)) {
//			HTMLDisplay animalScriptDisplay = new HTMLDisplay();
      animalScriptDisplay = new HTMLView();
	 		String localeString = Animal.getCurrentLocale().toString();
			String baseURLString = "/animalScript";
			String targetURL = baseURLString + "_" + localeString +".html";
			try {				
				animalScriptDisplay.setURL(targetURL);
			} catch(MalformedURLException mfue) {
				MessageDisplay.errorMsg("URL malformed: " + targetURL,
						MessageDisplay.RUN_ERROR);
			} catch (IOException ioExc) {
				try {
					animalScriptDisplay.setURL(baseURLString + ".html");
				} catch (Exception e) {
					MessageDisplay.errorMsg("Could not access " + targetURL
							+ "or " + baseURLString + ".html",
						MessageDisplay.RUN_ERROR);
				}
			}
			if(zoomCounter >0) {
			  for(int i = 0; i < zoomCounter; i++) {
			    animalScriptDisplay.zoom(true);
			  }
			  
			}else {
        for (int i = 0; i > zoomCounter; i--) {
          animalScriptDisplay.zoom(false);
        }
			  
			}
		}
		else if (MainMenuBar.ANIMATION_INFORMATION.equals(command))
		  AnimInfoDialog.getAnimInfoDialog(Animal.get()).setVisible(true);
    else if (MainMenuBar.QUIZ_RESULTS.equals(command)) {
      BackendInterface quizBackend = Animal.getAnimalConfiguration().getInteractionController().getBackend();
      if (quizBackend != null && quizBackend instanceof AnimalEvalBackend)
      	MessageDisplay.message(((AnimalEvalBackend) quizBackend).getResultString());
    }
    else if (MainMenuBar.ANIMATION_WINDOW_SIZE.equals(command)) {
      AnimationWindow aWin = 
        AnimalMainWindow.getWindowCoordinator().getAnimationWindow(true);
      MessageDisplay.message(AnimalTranslator.translateMessage("animWinSizeIs",
          new Integer[] { 
            Integer.valueOf(aWin.getViewWidth()),
            Integer.valueOf(aWin.getViewHeight())
      }));      
    }
		else if (MainMenuBar.TUTORIAL.equals(command)) {
			if (tutorialWindow == null)
				tutorialWindow = new HelpWindow(Animal.get(),
						AnimalConfiguration.getDefaultConfiguration().getProperties(), 
						AnimalTranslator.translateMessage("tutorial.label"), 
						AnimalTranslator.translateMessage("tutorialPath"));
			tutorialWindow.setVisible(true);						           
		}
		else 
      MessageDisplay.errorMsg("nothingAssoc", command, 
      		MessageDisplay.RUN_ERROR);
	}

  /**
   * @param zoomIn
   *          if true zooms in, if false zooms out
   */
  public void zoom(boolean zoomIn) {

    if (zoomIn) {
      if (zoomCounter < 6)
        zoomCounter++;
    } else {
      if (zoomCounter > -1)
        zoomCounter--;
    }

    if (animalScriptDisplay != null)
      animalScriptDisplay.zoom(zoomIn);

  }
}
