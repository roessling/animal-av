/*
 * Created on 22.04.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package animal.exchange;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.PrintJob;

import translator.AnimalTranslator;
import animal.gui.AnimalMainWindow;
import animal.main.Animal;
import animal.main.AnimationCanvas;
import animal.misc.MessageDisplay;

/**
 * Prints the current animation
 * 
 * @author Guido
 * @version 0.7 20050422
 */
public class AnimationPrintJob {
	/**
	 * Prints the currenet animation to a file
	 * 
	 * @param animal
	 *            the curretn Animal instance
	 */
	public AnimationPrintJob(Animal animal) {
		PrintJob pj = animal.getToolkit()
				.getPrintJob(animal, "animPrint", null);

		if (pj != null) {
			Graphics g = pj.getGraphics();
			AnimationCanvas canvas = AnimalMainWindow.getWindowCoordinator().getAnimationWindow(true)
					.getAnimationCanvas();

			if (canvas != null) {
				Image animationImage = canvas.getCurrentImage();
				g.drawImage(animationImage, 0, 0, canvas);
				g.dispose();

				pj.end();
			} else {
				MessageDisplay.message(
						AnimalTranslator.translateMessage("canvasNullForPrinting"));
			}
		} else {
			MessageDisplay.message(
					AnimalTranslator.translateMessage("printJobNullForPrinting"));
		}
	}
}
