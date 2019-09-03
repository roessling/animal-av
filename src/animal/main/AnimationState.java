package animal.main;

import java.util.TreeMap;
import java.util.Vector;

import animal.animator.Animator;
import animal.graphics.PTGraphicObject;
import animal.graphics.meta.PTMatrix;
import animal.gui.GraphicVector;
import animal.gui.GraphicVectorEntry;
import animal.misc.MessageDisplay;
import animal.variables.Variable;
import translator.AnimalTranslator;

/**
 * The class that holds the state of an Animation object, i.e. a current step
 * and a clone of all objects at this step. For the need of AnimationStates, cf.
 * the printed documentation. Animators must not be executed directly on
 * Animations GraphicObjects, as then their original state would be overwritten.
 * Therefore, AnimationState stores clones of them. Thus, Animation is
 * stateless(technically, not politically :-) ) AnimationState automatically
 * asks Animation(if required) whether any new objects exist.
 * 
 * @see Animation
 * 
 * @author <a href="http://www.ahrgr.de/guido/">Guido R&ouml;&szlig;ling </a>
 * @version 1.0 18.07.1998
 */
public class AnimationState {
  /** the basic Animation. */
  private Animation                 animation;

  /** clones of all GraphicObjects */
  private Vector<PTGraphicObject>   allObjectClones = new Vector<PTGraphicObject>(
                                                        400);

  /** current variables */
  private TreeMap<String, Variable> variables       = new TreeMap<String, Variable>();

  // /**
  // * New performance feature: updated to Hashtable!
  // */
  // private Hashtable<String, PTGraphicObject> allObjectClonesTable =
  // new Hashtable<String, PTGraphicObject>(65531);

  /** Animators that need to be executed to reach the next step. */
  private Vector<Animator>          nowAnimators    = new Vector<Animator>();

  /**
   * The Objects in the current step. This object must not be reallocated during
   * runtime, for some objects(e.g. Show) rely on the vector to insert into
   * being the same as at their initialization time.
   */
  private GraphicVector             nowObjects      = new GraphicVector();

  /** current step. */
  private int                       step            = 0;

  /**
   * initializes a new AnimationState Object by setting its basic Animation to
   * <i>animation </i>.
   * 
   * @param targetAnimation
   *          the Animation on which AnimationState is based.
   */
  public AnimationState(Animation targetAnimation) {
    animation = targetAnimation;
  }

  /**
   * returns the current animation
   * 
   * @return the Animation on which this AnimationState is based. public for
   *         DrawWindow and AnimationWindow.
   */
  public Animation getAnimation() {
    return animation;
  }

  /**
   * returns the clone of the GraphicObject with number <i>num </i>. If the
   * GraphicObject cannot be found here, it is searched in Animation. If it
   * exists there, AnimationState is not up to date and will be reset, then the
   * Object will be returned.
   * 
   * @param num
   *          the number of the GraphicObject looked for. must be public for
   *          Animator.reset(); null if the number is negative, null for
   *          non-existing objects. / public PTGraphicObject getCloneByNumHT(int
   *          num) { if (num < 0) return null; // illegal value -- return null
   *          PTGraphicObject result = null; // first, check the
   *          "allObjectClonesTable"
   *          MessageDisplay.addDebugMessage("checking allObjectClonesTable for ID "
   *          +num); // generate a query key String key = Integer.toString(num);
   *          if (allObjectClonesTable.containsKey(key)) { result =
   *          allObjectClonesTable.get(key); if (result != null) {
   *          MessageDisplay.addDebugMessage("retrieved from table: " +result);
   *          if (result.getNum(false) == num)
   *          MessageDisplay.addDebugMessage("object has correct ID; return it"
   *          ); else MessageDisplay.addDebugMessage("invalid ID: "
   *          +result.getNum(false)); } else
   *          MessageDisplay.addDebugMessage("returned null, although key was valid!"
   *          ); } else MessageDisplay.addDebugMessage("key "+key +
   *          " does not exist in the table.");
   *          MessageDisplay.addDebugMessage("now querying the animation itself..."
   *          ); if (animation.getGraphicObject(num) != null) {
   *          MessageDisplay.addDebugMessage
   *          ("object not found in table, but in animation..."); int nowStep =
   *          step; reset(); setStep(nowStep, false); }
   * 
   *          return result; }
   */

  /**
   * returns the clone of the GraphicObject with number <i>num </i>. If the
   * GraphicObject cannot be found here, it is searched in Animation. If it
   * exists there, AnimationState is not up to date and will be reset, then the
   * Object will be returned.
   * 
   * @param num
   *          the number of the GraphicObject looked for. must be public for
   *          Animator.reset()
   */
  public PTGraphicObject getCloneByNum(int num) {
    if (num < 0)
      return null;
    PTGraphicObject result;
    // first, let us examine the allObjectsClones elements!
    for (int i = 0; i < allObjectClones.size(); i++) {
      result = allObjectClones.elementAt(i);
      if (result.getNum(true) == num) {
        return result;
      }
    }
//    System.err.println("clone of object " + num
//        + " was not found in first attempt.");
    // two loops. one internally, one considering the Animation object.
    for (int j = 0; j < 2; j++) {
      for (int i = 0; i < allObjectClones.size(); i++) {
        result = allObjectClones.elementAt(i);
        if (result.getNum(true) == num) {
          return result;
        }
      }
      // try to get any new objects
      if (j == 0) {
        if (animation.getGraphicObject(num) == null)
          break;

        int nowStep = step;
        reset();
        setStep(nowStep, false);
      }
    }
    MessageDisplay.errorMsg("cloneNotFound", new Integer[] {
        Integer.valueOf(num), Integer.valueOf(getStep()) },
        MessageDisplay.PROGRAM_ERROR);
    return null;
  }

  /**
   * @return the Animators that have to be executed to reach the step set by
   *         <code>setStep</code>.
   */
  public Vector<Animator> getCurrentAnimators() {
    return nowAnimators;
  }

  /**
   * @return the GraphicObjects that are currently visible.
   */
  public GraphicVector getCurrentObjects() {
    return nowObjects;
  }

  /**
   * @return all GraphicObjects that are currently visible.
   */
  public Vector<PTGraphicObject> getAllObjects() {
    return allObjectClones;
  }

  /**
   * @return all variables that are currently in use.
   */
  public TreeMap<String, Variable> getVariables() {
    if (variables != null)
      return variables;
    else
      return new TreeMap<String, Variable>();
  }

  public void putVariable(Variable var) {
    variables.put(var.getName(), var);
  }

  /**
   * @return the last step(i.e. the step that is executed last) of this
   *         Animation
   */
  public int getFirstRealStep() {
    return animation.getNextStep(Link.START);
  }

  /**
   * @return the last step(i.e. the step that is executed last) of this
   *         Animation
   */
  public int getLastStep() {
    return animation.getLastStep();
  }

  /**
   * @return the step executed after this step.
   */
  public int getNextStep() {
    // must not set this.step to the new step!!!
    return animation.getNextStep(step);
  }

  /**
   * @return the step executed before this step.
   */
  public int getPrevStep() {
    // must not set this.step to the new step!!!
    return animation.getPrevStep(step);
  }

  /**
   * @return the current step of this AnimationState object
   */
  public int getStep() {
    return step;
  }

  /**
   * resets the AnimationState by recreating all GraphicObject clones and
   * resetting the step.
   */
  public void reset() {
    int i;
    allObjectClones.removeAllElements();
    if (animation == null)
      return;
    Vector<PTGraphicObject> go = animation.getGraphicObjects();
    allObjectClones.ensureCapacity(go.size());
    for (i = 0; i < go.size(); i++) {
      PTGraphicObject origin = go.elementAt(i);
      PTGraphicObject clonedObject = (PTGraphicObject) (origin.clone());

      // TODO: check if this is sufficient!
      // if (!(clonedObject instanceof PolygonalShape))
      // clonedObject.clonePropertiesFrom(origin.getProperties(), true);
      allObjectClones.addElement(clonedObject);
    }

    step = 0;
    allObjectClones.trimToSize();
    nowObjects.removeAllElements();
    // variables.clear();
  }
  
  /**
   * sets the step of this AnimationState object by setting all objects to the
   * state they have in this step. This is done by sequentially executing all
   * Animators from the first step to <i>step </i>).
   * 
   * @param targetStep
   *          the step to change to
   * @param delayed
   *          if true, the Animators of the <i>step </i> itself are not
   *          executed(only those of the steps before). This is useful, if the
   *          caller wants to execute the Animators on its own(which is the case
   *          only in AnimationWindow to display a step "slowly").
   * @return true if the step could be set, <br>
   *         false if the step could not be set, e.g. because it did not exist.
   *         Then, a step "close" to the desired step is selected, i.e. the
   *         nextStep, prevStep or first Step.
   */
  public boolean setStep(int targetStep, boolean delayed) {
    Animator a;
    int wasStep = getStep();  // Link.END;  //TODO: if not function an animator, change to Link.End to reset all and run all steps before
    step = animation.verifyStep(targetStep);
    int[] path = animation.findWayToStep(targetStep, wasStep);
    boolean result = true;
    if (path == null)
      result = false;
    if(wasStep>targetStep) {
      reset();
    }
    step = targetStep; // must be done after reset because reset resets
    // step.
    // for all the steps before this step...
    if (path != null)
      for (int i = 0; i < path.length; i++) {
        int renderStep = path[i];
        nowObjects.nextStep();
        nowAnimators = animation.getAnimatorsAtStep(renderStep);
        // ...for all Animators in each step...
        for (int j = 0; j < nowAnimators.size(); j++) {
          a = nowAnimators.elementAt(j);
          // ...(except perhaps for the last step)...
          if (i < path.length - 1 || !delayed) {
            // in the last step, don't execute, because
            // AnimationWindow
            // will do it.
            // link the Animator with this AnimationState Object
            // (time and ticks do not matter, as it's executed at
            // once)
            a.init(this, 0, 0);
            a.execute();
          }
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
          if (a.isChangingAnimator() && a.isGraphicalObjectAnimator()) {
            int[] nums = a.getObjectNums();
            if (nums != null)
              for (int k = 0; k < nums.length; k++) {
                GraphicVectorEntry gve = nowObjects.getGVEByNum(nums[k]);
                if (gve != null) {// should be, but one never
                  if(gve.getGraphicObject() instanceof PTMatrix) {
                    PTMatrix sm = (PTMatrix)gve.getGraphicObject();
                    sm.updateFullObject();
                  }
                  // knows :)
                  gve.setAnimated();
                }
              }
          }

          int[] o;
          if (a.isGraphicalObjectAnimator()
              && (o = a.getTemporaryObjects()) != null) {
            for (int k = 0; k < o.length; k++) {
              nowObjects.addElement(getCloneByNum(o[k]),
                  GraphicVectorEntry.TEMPORARY);
            }
          }
        } // for j(Animators)
      } // for i(steps)
    if (!delayed)// no Animators need to be processed
      nowAnimators = new Vector<Animator>();
    return result;
  }

  /**
   * sets the step of this AnimationState object by setting all objects to the
   * state they have in this step. This is done by sequentially executing all
   * Animators from the first step to <i>step </i>).
   * 
   * @param targetStep
   *          the step to change to
   */
  public void setQuickStep(int targetStep) {
    Animator a;
    step = animation.verifyStep(targetStep);
    reset();
    nowObjects.nextStep();
    nowAnimators = animation.getAnimatorsAtStep(targetStep);
    // ...for all Animators in each step...
    for (int j = 0; j < nowAnimators.size(); j++) {
      a = nowAnimators.elementAt(j);
      a.init(this, 0, 0);
      a.execute();
    }
  }

  public String toString() {
    StringBuilder sb = new StringBuilder(80);
    sb.append(AnimalTranslator.translateMessage("animStateAtStep", Integer
        .valueOf(step)));
    return sb.toString();
  }
}
