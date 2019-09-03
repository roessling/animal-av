package animalscript.core;

import java.util.Vector;

import animal.animator.Animator;
import animal.graphics.PTGraphicObject;
import animal.gui.GraphicVector;
import animal.gui.GraphicVectorEntry;
import animal.main.Animation;
import animal.main.AnimationState;
import animal.misc.MessageDisplay;

public class QuickAnimationStep extends AnimationState {
  private Vector<PTGraphicObject> myObjects = null;
  private Animation myAnimation = null;
  private int currentStepNr = -1;
  private GraphicVector myCurrentObjects = new GraphicVector();
  
  public QuickAnimationStep(Animation targetAnimation) {
  	super(targetAnimation);
  	myObjects = new Vector<PTGraphicObject>(32768);
  	myAnimation = targetAnimation;
//  	animState = new AnimationState(targetAnimation);
  	resetAnimationState();
  }
  
  /**
   * insert the new graphic object into the proper vectors
   * @param ptgo
   */
  public void addGraphicObject(PTGraphicObject ptgo) {
  	if (ptgo == null)
  		return; // oops!
//		int sizePre = myObjects.size();
		PTGraphicObject clonedObject = (PTGraphicObject) (ptgo.clone());
		clonedObject.clonePropertiesFrom(ptgo.getProperties(), true);
		myObjects.addElement(clonedObject);
  }
  
  public PTGraphicObject getCloneByNum(int num) {
		if (num < 0)
			return null;
		PTGraphicObject result;
		
		// problem setting here: 
		// "new" objects for this step may not have been inserted into
		// the myXY vectors
		// two loops. one internally, one considering the Animation object.
		for (int j = 0; j < 2; j++) {
			for (int i = 0; i < myObjects.size(); i++) {
				result = myObjects.elementAt(i); 
				if (result.getNum(true) == num) {
					return result;
				}
			}
			// try to get any new objects
			if (j == 0) {
				if (myAnimation.getGraphicObject(num) == null)
					break;

				int nowStep = currentStepNr;
				//FIXME: Test if commenting out this line is OK
				// as long as BasicParser will add the clone manually!
//				resetAnimationState();
				setStep(nowStep, false);
//				MessageDisplay.addDebugMessage("setStep("+nowStep +", false), used to be (0, false)");

			}
		}
		// this really shouldn't happen, yet it sometimes does :-(
		MessageDisplay.errorMsg("Clone " + num + " not found in step "
				+ currentStepNr, MessageDisplay.PROGRAM_ERROR);
		return null;
		}
  
	public void resetAnimationState() {
		myObjects.removeAllElements();
		if (myAnimation == null)
			return;
		Vector<PTGraphicObject> go = myAnimation.getGraphicObjects();
		int size = go.size();
		myObjects.ensureCapacity(size);
		for (int i = 0; i < size; i++) {
			addGraphicObject(go.elementAt(i));
		}

		currentStepNr = 0;
		myObjects.trimToSize();
		myCurrentObjects.removeAllElements();
  }
  
  public boolean advanceStep(int targetStep) {
  	if (myAnimation == null)
  		return false; // oops!

  	Animator a = null;
  	// remove temporary objects from the previous step
		myCurrentObjects.nextStep();
		Vector<Animator> nowAnimators = myAnimation.getAnimatorsAtStep(targetStep);
		// ...for all Animators in each step...
		for (int j = 0; j < nowAnimators.size(); j++) {
			a = nowAnimators.elementAt(j);
			a.init(this, 0, 0);
			a.execute();
			// setting the ANIMATED attribute must happen *after*
			// executing
			// the animator, as an Object that is shown(i.e. Animator
			// Show
			// is executed) is not contained in the GraphicVector before
			// the Animator is executed.
			// On the other hand, not all Objects that an Animator runs
			// on
			// are really changed, therefore query! E.g. Show doesn't
			// change any attributes.
			if (a.isChangingAnimator()) {
				int[] nums = a.getObjectNums();
				if (nums != null)
					for (int k = 0; k < nums.length; k++) {
						GraphicVectorEntry gve = myCurrentObjects.getGVEByNum(nums[k]);
						if (gve != null)// should be, but one never knows :)
							gve.setAnimated();
					}
			}

			int[] o;
			if ((o = a.getTemporaryObjects()) != null) {
				for (int k = 0; k < o.length; k++)
					myCurrentObjects.addElement(getCloneByNum(o[k]),
							GraphicVectorEntry.TEMPORARY);
			}
		}
  	currentStepNr++;
  	return true;
  }
  
  public boolean setStep(int targetStep, boolean isDelayed) {
  	return setStep(targetStep);
  }
  
  public boolean setStep(int targetStep) {
  	int cStep = currentStepNr;
  	while (cStep < targetStep && advanceStep(cStep))
  		cStep++;
  	return (currentStepNr == targetStep);
  }
}
