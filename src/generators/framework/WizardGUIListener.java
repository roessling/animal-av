/*
 * Created on 08.04.2005 by T. Ackermann
 */

package generators.framework;

/**
 * This Interface has to be implemented if the user want's to get feedback and
 * change the default handling of a WizardGUI.
 * 
 * @author T. Ackermann
 */
public interface WizardGUIListener {

	/**
	 * nextPressed is called when next is pressed. If getCurrentStep returns
	 * getNumberOfSteps minus one, then the Finish-Button is pressed.
	 * @return If this functions returns false, the next step is not shown.
	 */
	public boolean nextPressed();


	/**
	 * backPressed is called when back is pressed.
	 * @return If this functions returns false, the previous step is not shown.
	 */
	public boolean backPressed();


	/**
	 * afterShowStep is called, when the current step has changed.
	 * @param index The step that is shown now.
	 */
	public void afterShowStep(int index);
}
