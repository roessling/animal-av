package animal.main;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Vector;

import javax.swing.SwingUtilities;

import translator.AnimalTranslator;
import animal.animator.Animator;
import animal.animator.TimedAnimator;
import animal.animator.TimedShow;
import animal.editor.Editor;
import animal.graphics.PTGraphicObject;
import animal.gui.AnimalMainWindow;
import animal.gui.GraphicVector;
import animal.gui.GraphicVectorEntry;
import animal.misc.MessageDisplay;
import animal.misc.XProperties;

/**
 * The main collection of objects in Animal. It contains all GraphicObjects,
 * Animators and Steps and provides methods for accessing them. <br>
 * The current state of the Animation is not saved here but in an AnimationState
 * object.
 * 
 * @see AnimationState
 * 
 * @author <a href="http://www.ahrgr.de/guido/">Guido
 *         R&ouml;&szlig;ling </a>
 * @version 1.1 2000-12-01
 */
public class Animation { 

	/** current version of Animation */
	private static final int fileVersion = 1;

	Rectangle visualizationSize = new Rectangle(0, 0, 0, 0);

	public String language = "none";

	private int width = 0;

	private int height = 0;

	/** The animation that is currently being initialized. */
	private static Animation pendingAnimation;

	/** the Animation that is currently registered. */
	private static Animation registeredAnimation;

	/** the Vector containing all Animators, in order of steps. */
  //private Vector<Animator> allAnimators = new Vector<Animator>();
  
  private Vector<Vector<Animator>> allAnimatorsSorted = new Vector<Vector<Animator>>();

	/** the Vector containing all GraphicObjects */
	private Vector<PTGraphicObject> allGraphicObjects = new Vector<PTGraphicObject>();

	/** the Vector containg all Links */
	private Vector<Link> allLinks = new Vector<Link>();

	/**
	 * The animation's author, if given
	 */
	String author;

	/** "time" of the last change. */
	private int lastChange = 0;

	/** the property mapper for sequential link IDs */
	private XProperties linkNrMapper;

	/**
	 * the counter for the GraphicObject's num. Ensure that each GraphicObject
	 * gets a unique number by always increasing this counter.
	 */
	private int nextGraphicObjectNum = 0;

	private int nrAnimationSteps = 0;

	private int nrAnimators = 0;

	private int nrObjects = 0;

	/** the property mapper for sequential object IDs */
	private XProperties objectIDMapper;

	private long animationUID;

//	private Hashtable<String, QuestionView> interactions = 
//		new Hashtable<String, QuestionView>(512);
	
	/**
	 * the title for this animation(if any was given)
	 */
	String title;

	public int getFileVersion() {
		return fileVersion;
	}

	/**
	 * creates an empty Animation, consisting just of step 1.
	 * <i>pendingAnimation </i> is set to this object, so every object that
	 * needs a reference to the new Animation can get it via <code>get()</code>.
	 * <p>
	 * There is a circular reference when initializing a new Animation: The
	 * Animation cannot be set before it's complete(i.e. all objects have been
	 * inserted) and the Objects depend on an Animation to determine their
	 * number etc. This can only be done by static members, as loadObject
	 * doesn't allow any additional parameters. Therefore, the
	 * <i>pendingAnimation </i> is introduced. It stores an Animation that is to
	 * be set after initializing it, but has not yet been set. When get() is
	 * called, always this new <i>pendingAnimation </i> is returned if it
	 * exists, otherwise the <i>registeredAnimation </i>. Thus, a
	 * <i>pendingAnimation </i> may only exist when initializing an Animation.
	 * An animation that is initialized must either be registered (via
	 * <code>register()</code>) or discarded(via <code>discard()</code>),
	 * either of which is normally done automatically in
	 * <code>Animal.setAnimation()</code> public constructor required for
	 * serialization
	 */
	public Animation() {
    while(getNrOfSteps()>=allAnimatorsSorted.size()) {
      allAnimatorsSorted.add(new Vector<Animator>());
    }
		insertLink(new Link(Link.START, 1));
		insertLink(new Link(1, Link.END));
		pendingAnimation = this;
		doChange();
		nrAnimationSteps = 0;
		nrAnimators = 0;
		nrObjects = 0;
	}

	/***************************************************************************
	 * general methods
	 **************************************************************************/

	/**
	 * executes one step by executing all Animators "slowly"(i.e. not at once
	 * but according to their execution time).
	 * 
	 * @return next step to be executed immediately afterwards; Link.END, if
	 *         none such step exists
	 */
	public Rectangle determineVisualizationSize() {
		AnimationState state = new AnimationState(this);
		Vector<Animator> animators; // Animators to execute in this step
		Animator animator; // Animator currently processed
		long time; // for synchronization
		int ticks = 0;
		Rectangle size = new Rectangle(0, 0, 1, 1);
		Rectangle currentBoundingBox = null;
		int step = Link.START;
		int w = 0, h = 0;
		state.setStep(Link.START, false);

		for (step = state.getNextStep(); step != Link.END; step = state
				.getNextStep()) {
			state.setStep(step, true);
			animators = state.getCurrentAnimators();
			time = System.currentTimeMillis();
			// initialize each Animator by linking it with AnimationWindow's
			// AnimationState object and resetting its start time and ticks
			for (int a = 0; a < animators.size(); a++)
				animators.elementAt(a).init(state, time, ticks);

			boolean finished = false; // no more Animators to be processed?
			int[] currentObjects = null;
			PTGraphicObject currentObject = null;
			while (!finished) {
				finished = true;
				time = System.currentTimeMillis();
				ticks++;
				// execute each Animator slowly
				for (int a = 0; a < animators.size();) {
					animator = animators.elementAt(a);
					// perform an action on the Animator
					animator.action(time, ticks);
					currentObjects = animator.getObjectNums();
					for (int i = 0; i < currentObjects.length; i++) {
						currentObject = state.getCloneByNum(currentObjects[i]);
						if (currentObject != null) {
						    currentBoundingBox = currentObject.getBoundingBox();
						    w = currentBoundingBox.width + currentBoundingBox.x;
						    h = currentBoundingBox.height + currentBoundingBox.y;
						}
						size = SwingUtilities.computeUnion(0, 0, w, h, size);
					}
					// and if this was its last action, remove it from the
					// list of Animators to be processed
					if (animator.hasFinished())
						animators.removeElementAt(a);
					else {
						// if it's not yet removed, it has to be processed once
						// more in the next loop.
						finished = false;
						a++;
					}
				}
			}
		}
		setWidth(size.width);
		setHeight(size.height);
		return size;
	} // determineImageSize()

	public Rectangle getVisualizationSize() {
		return visualizationSize;
	}

	protected void setVisualizationSize(Rectangle size) {
		visualizationSize = (size != null) ? size : new Rectangle(0, 0, 1, 1);
		setWidth(visualizationSize.width);
		setHeight(visualizationSize.height);
	}
  
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String lang) {
		language = lang;
	}

	/**
	 * Retrieve the animation's author
	 * 
	 * @return the author's name, if given; else "&lt;none given&gt;"
	 */
	public String getAuthor() {
		return (author != null) ? author :
			AnimalTranslator.translateMessage("unknownAuthor");
	}

	/**
	 * Retrieve the animation's title
	 * 
	 * @return the title, if given; else "&lt;no title&gt;"
	 */
	public String getTitle() {
		return (title != null) ? title : 
			AnimalTranslator.translateMessage("unnamedAnimation");
	}

	/**
	 * Set the animation's author to the string passed
	 * 
	 * @param authorName
	 *            the name of the animation's author
	 */
	public void setAuthor(String authorName) {
		author = authorName;
	}

	/**
	 * Set the animation title to the string passed
	 * 
	 * @param titleString
	 *            the title of the animation
	 */
	public void setTitle(String titleString) {
		title = titleString;
	}

	/**
	 * discard the new Animation
	 * 
	 * @see Animation#Animation
	 * @see #register
	 */
	void discard() {
		long timeForDiscard = System.currentTimeMillis();
		int i;
		AnimationListEntry[] localinfo;

		localinfo = pendingAnimation.getAnimatorList();

		// remove all entries
		if (localinfo != null)
			for (i = 0; i < localinfo.length; i++)
				localinfo[i].discard();
		Vector<PTGraphicObject> objects = pendingAnimation.allGraphicObjects;
		int nr = objects.size();
		for (i = 0; i < nr; i++)
			objects.elementAt(i).discard();
		pendingAnimation = null;
		MessageDisplay.message("animDiscarded", 
				Long.valueOf(System.currentTimeMillis() - timeForDiscard));
	}

	/**
	 * increase the change counter and thus indicate that the Animation has
	 * changed. is accessed from Editor.apply.
	 */
	public void doChange() {
		lastChange++;
	}

	/**
	 * returns a reference to the current Animation.
	 * 
	 * @return if a pending Animation exists(i.e. an Animation that is currently
	 *         being initialized), this Animation is returned, otherwise the
	 *         registered Animation.
	 */
	public static Animation get() {
		if (pendingAnimation != null)
			return pendingAnimation;
		return registeredAnimation;
	}

	/**
	 * returns the "time" of the last change. Whenever a change occurs to
	 * Animation, this counter is increased. By comparing their local counter to
	 * this one, every object can determine whether the Animation has changed
	 * meanwhile.
	 */
	public int getLastChange() {
		if (AnimalMainWindow.getWindowCoordinator().getDrawWindow(false).isInitialized())
			AnimalMainWindow.getWindowCoordinator().getDrawWindow(true).writeBack();
		return lastChange;
	}

	public void resetChange() {
		lastChange = 0;
	}


	public void resetNextGraphicObjectNum() {
		nextGraphicObjectNum = 0;
	}

	public int getNextGraphicObjectNum() {
		return nextGraphicObjectNum;
	}

	public void setNextGraphicObjectNum(int nr) {
		nextGraphicObjectNum = nr;
	}

	/**
	 * register the Animation.
	 * 
	 * @see Animation#Animation
	 * @see #discard
	 */
	void register() {
		//    if (registeredAnimation != null && registeredAnimation !=
		// pendingAnimation)
		//      registeredAnimation.discard();
		registeredAnimation = pendingAnimation;
		Editor.closeAllEditors();
	}


	/***************************************************************************
	 * GraphicObject related methods
	 **************************************************************************/

	/**
	 * deletes the GraphicObject and all Animators that work on it
	 * 
	 * @param objectNum
	 *            the number of the GraphicObject to be deleted.
	 */
	public void deleteGraphicObject(int objectNum) {
		// delete the object's Animators or remove the object from
		// the list of the animated objects of an Animator
	  for (Vector<Animator> stepVec : allAnimatorsSorted) {
	    for (int i = stepVec.size() - 1; i >= 0; i--) {
	      Animator a = stepVec.elementAt(i);
	      int[] nums = a.getObjectNums();
	      if (isContained(nums, objectNum)) {
	        if (nums.length == 1)
	          stepVec.removeElementAt(i);
	        else {
	          int[] newNums = new int[nums.length - 1];
	          int c = 0;
	          for (int j = 0; j < nums.length; j++)
	            if (nums[j] != objectNum)
	              newNums[c++] = nums[j];
	          a.setObjectNums(newNums);
	        }
	      } else { // search the temporary objects
	        nums = a.getTemporaryObjects();
	        if (isContained(nums, objectNum)) {
	          if (nums.length == 1)
	            stepVec.removeElementAt(i);
	          else
	            // what kind of temporary object is this? Don't know
	            // a case where more than one temporary object is
	            // used, so skip this. If later such a thing is
	            // implemented, what is to happen to the temporary
	            // objects then? They can't simply be reset as above,
	            // as they probably originate from different sources.
	            // (e.g. MoveRotate: center & path), so probably
	            // the best thing is to delete all the Animator.
	            MessageDisplay.errorMsg(
	                AnimalTranslator.translateMessage("cannotDeleteMultiple"),
	                    MessageDisplay.PROGRAM_ERROR);
	        }
	      }
	    }
	  }
		// in any case, remove the object itself.
		// (could also be left to the(not yet existing) internal
		// "garbage collection", as there are no more references in any
		// Animator.
		PTGraphicObject go = getGraphicObject(objectNum);
		if (go != null) {
			allGraphicObjects.removeElement(go);
			nrObjects--;
		}
		doChange();
	}

	/**
	 * returns the GraphicObject with the given number. If it cannot be found,
	 * DrawWindow is asked for new objects and these are also searched.
	 * 
	 * @return the GraphicObject with the given number. Null, if none such
	 *         object exists.
	 */
	public PTGraphicObject getGraphicObject(int num) {
		/*
		 * iterate at most twice. Once on the already existing objects and once
		 * on the objects written back from DrawWindow.
		 */
		for (int j = 0; j < 2; j++) {
			for (int i = 0; i < allGraphicObjects.size(); i++) {
				PTGraphicObject g = allGraphicObjects
						.elementAt(i);
				if (g.getNum(false) == num)
					return g;
			}
			// if not found, check if any new objects exist. Might be the case
			// if an object that was not yet written back is selected in an
			// AnimatorEditor, for instance.
			if (j == 0)
				// true is harmless for the above reason! DrawWindow is open
				// because something was selected inside

				if (!AnimalMainWindow.getWindowCoordinator().getDrawWindow(true).writeBack())// if no objects are
															// written back,
															// don't reloop.

					break;
		}
		return null;
	}

	/**
	 * returns a Vector of all GraphicObjects.
	 */
	public Vector<PTGraphicObject> getGraphicObjects() {
		return allGraphicObjects;
	}

	/**
	 * returns a unique number for GraphicObjects. Every GraphicObjects needs a
	 * unique number to be identified.
	 */
	public int getUniqueGraphicObjectNum() {
		return ++nextGraphicObjectNum;
	}

	/**
	 * inserts a GraphicObject into the Animation or replaces an old one. If the
	 * Object existed before and was animated, it is not inserted because it
	 * would overwrite its original state. Otherwise, if an old instance of the
	 * object exists, it is removed before the new one is inserted to guarantee
	 * the uniqueness of numbers.
	 * 
	 * @return true if the object was inserted; false otherwise(i.e the object
	 *         was ANIMATED or replaced)
	 */
	private boolean insertGraphicObject(GraphicVectorEntry gve) {
		// create a new number, if necessary
		int num = gve.go.getNum(true);
		// don't insert animated objects as these already exist and must not
		// overwrite their original state.
		if (gve.getMode() == GraphicVectorEntry.ANIMATED) {
			return false;
		}
		// TODO: change to Hashtable here && @AnimationState.getCloneByNum!
		// remove old instance at first!
		for (int a = 0; a < allGraphicObjects.size(); a++) {
			if (allGraphicObjects.elementAt(a).getNum(true) == num) {
				allGraphicObjects.setElementAt(gve.go, a);
				doChange();
				return false;
			}
		}
		allGraphicObjects.addElement(gve.go);
		doChange();
		gve.go.updateDefaults(AnimalConfiguration.getDefaultConfiguration().getProperties());
		nrObjects++;
		return true;
	}

	/**
	 * Inserts a new GraphicObject into the Animation.
	 * 
	 * @return the number of the GraphicObject inserted.
	 */
	public int insertGraphicObject(PTGraphicObject g) {
		insertGraphicObject(new GraphicVectorEntry(g, false,
				GraphicVectorEntry.CREATED));
		return g.getNum(true);
	}

	/**
	 * determines whether the GraphicObject is used temporarily in this step,
	 * i.e. an Animator uses it as its temporaryObject.
	 * 
	 * @param step
	 *            if 0, check all steps, otherwise just <i>step </i>
	 */
	public boolean isUsedTemporarily(int num, int step) {
		Vector<Animator> animators;
		int[] o;

		for (int s = step; // consistently if 0, start with 0
		(step != 0 && s == step) || // if step != 0, only one iteration
				(s != Link.END && step != Link.ERROR); s = getNextStep(s)) {
			animators = getAnimatorsAtStep(s);
			// iterate all Animators.
			for (int i = 0; i < animators.size(); i++) {
				Animator a = animators.elementAt(i);
				// for each Animator iterate all temporary objects.
				if ((o = a.getTemporaryObjects()) != null) {
					for (int k = 0; k < o.length; k++)
						if (o[k] == num)
							return true;
				}
			}
		}
		return false;
	}

	/**
	 * puts all GraphicObjects of this step into the Animation. If they are not
	 * temporary, a Show-Animator is also inserted for this step, such that the
	 * GraphicObject will always become visible in this step.
	 */
	public void putObjectsAtStep(int step, GraphicVector objects) {
	  int theStep = step;
		int s;
		// if at the end, insert into the last step
		if (theStep == Link.END)
			if ((s = getLastStep()) != Link.END)
			  theStep = s;
			else
			  theStep = newStep(theStep); // this mustn't happen!
		// never insert into step 0!
		if (theStep == Link.START)// this mustn't happen either!

		  theStep = getNextStep(theStep);
		// insert all objects into the Vector
		Vector<Integer> newObjects = new Vector<Integer>();
		for (int a = 0; a < objects.getSize(); a++) {
			GraphicVectorEntry gve = objects.elementAt(a);
			// insert a TimedShow-Animator, if the object was newly inserted
			// and is not temporary.
			// First just store the numbers
			if (insertGraphicObject(gve) && !gve.isTemporary()
					&& !isUsedTemporarily(gve.go.getNum(true), theStep))
				newObjects.addElement(new Integer(gve.go.getNum(true)));
		}
		// then convert the numbers to an array and pass them to a Show-Animator
		if (newObjects.size() > 0) {
			int[] newO = new int[newObjects.size()];
			for (int a = 0; a < newObjects.size(); a++)
				newO[a] = newObjects.elementAt(a).intValue();
			insertAnimator(new TimedShow(theStep, newO, 0, "show", true));
		}
	}

	/***************************************************************************
	 * Animator related methods
	 **************************************************************************/

	/**
	 * checks whether the Animation already contains the Animator. This is
	 * useful for AnimatorEditors, because there is a difference between
	 * updating and creating an Animator.
	 */
	public boolean containsAnimator(Animator animator) {
	  return allAnimatorsSorted.get(animator.getStep()).contains(animator);
//		return allAnimators.contains(animator);
	}

	
	public int nrOfLabels() {
    String s = null;
    int labelCounter = 0;
	  for (Link l : allLinks) {
	    if (l != null) {
	      s = l.getLinkLabel();
	      if (s != null && s.trim().length() > 0)
	        labelCounter++;
	    }
	  }
	  return labelCounter;
	}
	
	/**
	 * returns the Vector of all Animators.
	 */
	Vector<Animator> getAnimators() {
	  Vector<Animator> allAnimators = new Vector<Animator>();
	  for(Vector<Animator> animatorVecForStep : allAnimatorsSorted){
	    allAnimators.addAll(animatorVecForStep);
	  }
		return allAnimators;
	}

	/**
	 * Inserts the Animator. It will be the last one in the list that has its
	 * step. So if several Animators are inserted, it is ensured that those
	 * inserted earlier are also executed earlier.
	 */
	public void insertAnimator(Animator animator) {
    while(allAnimatorsSorted.size()<=animator.getStep()) {
      allAnimatorsSorted.add(new Vector<Animator>());
    }
	  allAnimatorsSorted.get(animator.getStep()).add(animator);
	  
//		int i = allAnimators.size() - 1;
//		while (i >= 0
//				&& allAnimators.elementAt(i).getStep() > animator
//						.getStep())
//			i--;
//		allAnimators.insertElementAt(animator, i + 1);
		
		if (animator instanceof TimedAnimator && animator != null) {
			TimedAnimator ta = (TimedAnimator) animator;
			Link targetLink = getLink(animator.getStep());
			int duration = ta.getOffset() + ta.getDuration();
			if (targetLink != null)
			    if (duration > targetLink.getDurationInTicks()
			            && ta.isUnitIsTicks())
			        targetLink.setDurationInTicks(duration);
		}
		nrAnimators++;
		doChange();
	}
	
//	public void insertInteraction(String interactionID, QuestionView o) {
//		interactions.put(interactionID, o);
//	}

	/**
	 * gets the Animator(as only at most one may exist) that animates the
	 * GraphicObject with num <i>objectNum </i> in step <i>step </i>, or any
	 * Animator that works in this step, if <i>objectNum </i> is 0, or any
	 * Animator that works on <i>objectNum </i>, if step is 0.
	 * 
	 * @param objectNum
	 *            if 0, retrieve any Animator in this step
	 *            <p>
	 *            else retrieve the Animator that animates the given object in
	 *            this step.
	 * @param step
	 *            if 0, retrieve any Animator on the object
	 *            <p>
	 *            else retrieve the Animator that animates the given object in
	 *            this step.
	 * @param dontGetThis
	 *            even if <i>dontGetThis </i> is found, it is not returned, for
	 *            instance because this Animator is expected to contain
	 *            <i>objectNum </i>, but a different Animator is to be searched.
	 * 
	 * @return null if no Animator was found, a(resp. the) matching Animator
	 *         otherwise.
	 */
	public Animator getAnimator(int step, int objectNum, Animator dontGetThis) {
    for (Animator a : allAnimatorsSorted.get(step)) {
      if (((step == 0
          && (objectNum == 0 || isContained(a.getObjectNums(),
              objectNum)) || (a.getStep() == step && (objectNum == 0 || isContained(
          a.getObjectNums(), objectNum)))))
          && a != dontGetThis)
        return a;
    }
    return null;
    
//		for (int i = 0; i < allAnimators.size(); i++) {
//			Animator a = allAnimators.elementAt(i);
//			if (((step == 0
//					&& (objectNum == 0 || isContained(a.getObjectNums(),
//							objectNum)) || (a.getStep() == step && (objectNum == 0 || isContained(
//					a.getObjectNums(), objectNum)))))
//					&& a != dontGetThis)
//				return a;
//		}
//		return null;
	}

	/**
	 * returns all Animators in the given step.
	 */
	public Vector<Animator> getAnimatorsAtStep(int step) {
	  try {
	    return new Vector<Animator>(allAnimatorsSorted.get(step));
    } catch (ArrayIndexOutOfBoundsException e) {
      return new Vector<Animator>();
    }
	  
//		Vector<Animator> nowAnimators = new Vector<Animator>();
//		for (int a = 0; a < allAnimators.size(); a++) {
//			Animator ae = allAnimators.elementAt(a);
//			if (ae.getStep() == step)
//				nowAnimators.addElement(ae);
//		}
//		return nowAnimators;
	}

	/**
	 * removes an Animator from the Animation.
	 */
	public void deleteAnimator(Animator a) {
	  allAnimatorsSorted.get(a.getStep()).removeElement(a);
	  
//		allAnimators.removeElement(a);
	  
		nrAnimators--;
		if (a instanceof TimedAnimator) {
			Link targetStep = getLink(a.getStep());
			targetStep.setDurationInTicks(0);
			int duration = 0, localDuration = 0;
			TimedAnimator ta = null;
			Vector<Animator> stepAnimators = getAnimatorsAtStep(targetStep.getStep());
			for (int i = 0; i < stepAnimators.size(); i++)
				if (stepAnimators.elementAt(i) instanceof TimedAnimator) {
					ta = (TimedAnimator) stepAnimators.elementAt(i);
					if (ta.isUnitIsTicks()) {
						localDuration = ta.getDuration() + ta.getOffset();
						if (localDuration > duration)
							duration = localDuration;
					}
				}
			targetStep.setDurationInTicks(duration);
		}
		doChange();
	}

	/***************************************************************************
	 * step related methods. Repetition, branching and nonlinear order are
	 * available by reimplementing these functions(at least, I hope so)
	 **************************************************************************/

	/**
	 * deletes a step and all Animators in it and closes all LinkEditors
	 * refering to this step.
	 */
	public void deleteStep(int step) {//TODO Test if new implementation is correct
		Link s = getLink(step);
		Link before = getLink(getPrevStep(step));
		// losing the Editor must happen before resetting the Link, as
		// closeLinkEditors depends on the Link's variables!
		Editor.closeLinkEditors(step, true);
		// jump over the old nextStep
		before.setNextStep(s.getNextStep());
		allLinks.removeElement(s);
		nrAnimationSteps--;
		// remove all Animators in the step
//		for (int i = allAnimators.size() - 1; i >= 0; i--)
//			if (allAnimators.elementAt(i).getStep() == step)
//				allAnimators.removeElementAt(i);
		
    allAnimatorsSorted.get(step).removeAllElements();
    
		doChange();
	}

	/**
	 * returns the way to a step. By executing all Animators of all these steps,
	 * the state of the step can be reached.
	 * 
	 * @return the way to the step, i.e. a sequence of steps starting with step
	 *         0 and ending with the requested step.
	 */
	public int[] findWayToStep(int step,  int wasStep) {
    int toStep = step;
    int thisStep = wasStep;
    int size = getMaxStepNum() + 1;
    int[] tempResult = new int[size];
    int count = 0;
    if(wasStep>toStep) {
      thisStep = Link.START;
    }
    
    while (thisStep != Link.END && count < size && thisStep < toStep) {
      thisStep = getNextStep(thisStep);
      tempResult[count++] = thisStep;
    }
    int[] result = null;
	  
//	  int theStep = step;
//		//    long timeTaken = System.currentTimeMillis();
//		int size = getMaxStepNum() + 1;
//		// try to find a direct path from the current step, if possible!
////		int pathLength = 0, i;
//		int[] result = null;
//		int[] tempResult = new int[size];
//		int thisStep = Link.START;
//		// can't find a step to the end mark, but to the last step instead
//		if (theStep == Link.END)
//		  theStep = getLastStep();
//		int count = 0;
//		//    boolean followsCurrentStep = false;
//		//    int startPosition = 0;
//		// always walk to the next step, until the requested step is reached,
//		// remember the path.
//		while (thisStep != theStep && count < size) {
//			thisStep = getNextStep(thisStep);
//			/*
//			 * if (thisStep == currentStep) { followsCurrentStep = true;
//			 * startPosition = count; }
//			 */
//			tempResult[count++] = thisStep;
//		}
//		//    count -= startPosition;
//
//		if (count == size && thisStep != theStep) {
//			// no way found, perhaps because step doesn't exist
//			return null;
//		}
		
		// copy the path into a path of the right size.
		result = new int[count];
		System.arraycopy(tempResult, 0, result, 0, count);

		tempResult = null;
		return result;
	}

	/**
	 * returns the way to a step. By executing all Animators of all these steps,
	 * the state of the step can be reached.
	 * 
	 * @return the way to the step, i.e. a sequence of steps starting with step
	 *         0 and ending with the requested step.
	 */
	public int[] findWayToStepORIG(int step,  int currentStep) {
	  int theStep = step;
		long timeTaken = System.currentTimeMillis();
		int size = getMaxStepNum() + 1;
		// try to find a direct path from the current step, if possible!
//		int pathLength = 0;
		int i;
		int[] result = null;
		int[] tempResult = new int[size];
		int thisStep = Link.START;
		// can't find a step to the end mark, but to the last step instead
		if (theStep == Link.END)
		  theStep = getLastStep();
		int count = 0;
		// always walk to the next step, until the requested step is reached,
		// remember the path.
		while (thisStep != theStep && count < size) {
			thisStep = getNextStep(thisStep);
			tempResult[count++] = thisStep;
		}
		if (count == size && thisStep != theStep) {
			// no way found, perhaps because step doesn't exist
			return null;
		}

		// copy the path into a path of the right size.
		result = new int[count];
		for (i = 0; i < count; i++)
			result[i] = tempResult[i];
		timeTaken = System.currentTimeMillis() - timeTaken;

		return result;
	}

	/**
	 * returns the last step of the Animation. This is not necessarily the step
	 * with the highest number, but the step that is executed last.
	 */
	public int getLastStep() {
		Link l;
		for (int a = 0; a < allLinks.size(); a++) {

			l = allLinks.elementAt(a);
			if (l.getNextStep() == Link.END)
				return l.getStep();
		}
		return Link.ERROR; // this should not happen as one step must be
		// the last step
	}

	/**
	 * @return the step that is executed after this step, consistently, return
	 *         Link.ERROR, if the step is the end mark or if no next step is
	 *         found.
	 */
	public int getNextStep(int step) {
		Link l = getLink(step);
		if (l == null)
			return Link.ERROR;
		return l.getNextStep();
	}

	/**
	 * @return a new unique step number (consistently, for a newly created
	 *         animation, 1)
	 */
	public int getNextStepNum() {
		return getMaxStepNum() + 1;
	}

	public int getNrAnimationSteps() {
	    if (nrAnimationSteps <= 0) {
		nrAnimationSteps = 1;
		int currentStepNr = Link.START;
		while (currentStepNr != Link.END) {
		    currentStepNr = getNextStep(currentStepNr);
		    nrAnimationSteps++;
		}
	    }
	    return nrAnimationSteps;
	}

	public int getNrAnimators() {
		if (nrAnimators < 0)
			nrAnimators = 0;
		return nrAnimators;
	}

	public int getNrObjects() {
		if (nrObjects < 0)
			nrObjects = 0;
		return nrObjects;
	}

	/**
	 * returns the highest step number. This is not necessarily the step that is
	 * executed last, but only the one who has the highest number.
	 */
	public int getMaxStepNum() {
		int max = 0;
		int newLink;
		// iterate all Links
		for (int i = 0; i < allLinks.size(); i++) {
			// determine maximum
			newLink = allLinks.elementAt(i).getNextStep();
			if (newLink != Link.END && newLink > max)
				max = newLink;
		}
		return max;
	}

	/**
	 * @return the step that is executed before this step. If step is already
	 *         the first step, this step is also returned. If no previous step
	 *         is found, Link.END is returned, which should not happen.
	 */
	public int getPrevStep(int step) {
		Link l;
		// if already at start, return start
		if (step == Link.START)
			return Link.START;
		for (int a = 0; a < allLinks.size(); a++) {
			l = allLinks.elementAt(a);
			if (l.getNextStep() == step)
				return l.getStep();
		}
		return Link.END; /*
						  * this should not happen but is required as the
						  * program won't compile without.
						  */
	}

	/**
	 * @return the total number of steps in the Animation.
	 */
	public int getStepCount() {
		return allLinks.size() - 1;
	}

	/**
	 * @return a textual representation of the Step number, i.e. "end" if step
	 *         is the end mark, or the number as a String otherwise.
	 */
	public static String getStepString(int step) {
		if (step == Link.END)
			return "end";

		return Integer.toString(step);
	}

	/**
	 * inserts a new step before this step. If the old sequence was x-y, and z
	 * is inserted before y, the new sequence is x-z-y, where the link from x-z
	 * has the same attributes as the old link x-y, and z-y has default
	 * attributes(wait for key). The Editor containing the Link x-y is closed.
	 * 
	 * @return the number of the new step.
	 */
	public int newStep(int beforeStep) {
		int newStep = getNextStepNum();
		int prevStep = getPrevStep(beforeStep);
		Link l = getLink(prevStep);
		l.setNextStep(newStep);
		insertLink(new Link(newStep, beforeStep));
		Editor.closeLinkEditors(prevStep, false);
		doChange();
		return newStep;
	}

	/**
	 * inserts a new step after this step. If the old sequence was x-y, and z is
	 * inserted after x, the new sequence is x-z-y, where the link from x-z has
	 * the same attributes as the old link x-y, and z-y has default
	 * attributes(wait for key). The Editor containing the Link x-y is closed.
	 * 
	 * @return the number of the new step.
	 */
	public int appendStep(int currentStep) {
		int newStep = getNextStepNum();
		Link l = getLink(currentStep); // successor of current step
		int nextStep = l.getNextStep(); // get ID of next step
		if (l.getMode() == Link.WAIT_KEY)
			insertLink(new Link(newStep, nextStep));
		else
			insertLink(new Link(newStep, nextStep, l.getTime()));
		l.setNextStep(newStep);
		Editor.closeLinkEditors(newStep, false);
		doChange();
		return newStep;
	}
	
	/**
	 * inserts a new step before the step passed in.
	 * If the old sequence was x-y, and z is
	 * inserted before y, the new sequence is x-z-y, where the link from x-z has
	 * the same attributes as the old link x-y, and z-y has default
	 * attributes (wait for key). The Editor containing the Link x-y is closed.
	 * 
	 * @return the number of the new step.
	 */
	public int prependStep(int currentStep) {
	  int theStep = currentStep;
   if (theStep == Link.START)
     theStep = getNextStep(theStep);
   return newStep(theStep);
	}


	/**
	 * verifies the number of the step and returns a valid step number.
	 * 
	 * @return the number of the step, if it is valid, Link.START, if step was
	 *         negative, the number of the last step if step was the end mark.
	 */
	public int verifyStep(int step) {
	  int theStep = step;
		if (theStep < Link.START)
			return Link.START;
		if (theStep == Link.END)
		  theStep = getLastStep();
		if (getLink(theStep) == null)
			return Link.START;
		return theStep;
	}

	/**
	 * inserts a new Link. If an old Link existed that originated from the same
	 * step, the old one is removed before.
	 */
	public void insertLink(Link l) {
	  while(l.getStep()>=allAnimatorsSorted.size()) {
      allAnimatorsSorted.add(new Vector<Animator>());
	  }
	  
		Link oldLink = getLink(l.getStep());
		if (oldLink != null)
			allLinks.removeElement(oldLink);
		allLinks.addElement(l);
		doChange();
		nrAnimationSteps++;
	}

	/**
	 * @return the Link originating from step "from", null if the Link could not
	 *         be found.
	 */
	public Link getLink(int from) {
		for (int a = 0; a < allLinks.size(); a++) {
			Link l = allLinks.elementAt(a);
			if (from == l.getStep())
				return l;
		}
		return null;
	}

	/**
	 * for future extensions. Determine whether step1 and step2 are equal, i.e.
	 * they contain the same objects in the same state. This is required for
	 * loops and branches in the Animation, which are not yet implemented.
	 */
	public boolean isSameState( int step1, 
			 int step2) {
		return false;
	}

	/**
	 * get a list of all steps and the Animators in them sorted according to
	 * execution order of the steps.
	 * 
	 * @return a Vector of AnimationListEntrys containing all information
	 *         necessary to build a list of the whole Animation.
	 */
	public AnimationListEntry[] getAnimatorList() {
		Vector<AnimationListEntry> result = new Vector<AnimationListEntry>();
		Animator a;
		Link l;

		for (l = getLink(Link.START); l != null; l = getLink(l.getNextStep())) {

			// added to check for nonterminating loop!
			// there are no Animators in Step 0
			if (l.getStep() != Link.START) {
				result.addElement(new AnimationListEntry(l.toString(), l));
				// insert Animators for current step
				Vector<Animator> animators = getAnimatorsAtStep(l.getStep());
				for (int i = 0; i < animators.size(); i++) {
					a = animators.elementAt(i);
					result.addElement(new AnimationListEntry(a.toString(), a));
				}
			}
			// display the link mode to the next step.
			if (l.getStep() == Link.END)
				break;
		} // for step
		// add end mark
		result.addElement(new AnimationListEntry("----- end -----", new Link(
				Link.END, Link.END)));
		AnimationListEntry[] ai = new AnimationListEntry[result.size()];
		result.copyInto(ai);
		return ai;
	}

	/**
	 * checks whether the int <i>num </i> is contained in the array <i>nums
	 * </i>.
	 */
	public static boolean isContained(int[] nums, int num) {
		if (nums == null)
			return false;
		for (int a = 0; a < nums.length; a++)
			if (nums[a] == num)
				return true;
		return false;
	}

	public XProperties getObjectIDs() {
		return objectIDMapper;
	}

	public int mapLinkID(int oldID) {
		return linkNrMapper.getIntProperty(String.valueOf(oldID));
	}

	public int mapObjectID(int id) {
		return objectIDMapper.getIntProperty(String.valueOf(id));
	}

	public int[] mapObjectIDs(int[] ids) {
		int n = ids.length;
		int[] result = null;
		if (n != 0) {
			result = new int[n];
			for (int i = 0; i < n; i++)
				result[i] = objectIDMapper.getIntProperty(String
						.valueOf(ids[i]));
		}
		return result;
	}

	public int mapNewObject(int objectID) {
		nextGraphicObjectNum++;
		objectIDMapper.put(objectID, nextGraphicObjectNum);
		return nextGraphicObjectNum;
	}

	public void sequentialize() {
		// do nothing
	}

	public int getWidth() {
		if (width == 0)
			return -1;
		//TODO is that OK?
//			determineVisualizationSize();
		return width;
	}

	public int getHeight() {
		if (height <= 0)
			return -1;
		//TODO is that OK?
//			determineVisualizationSize();
		return height;
	}

	public void setWidth(int w) {
		width = w;
	}

  public void setSize(Dimension d) {
    setWidth((int)d.getWidth());
    setHeight((int)d.getHeight());
  }
  
	public void setHeight(int h) {
		height = h;
	}

	public Dimension getSize() {
		return new Dimension(getWidth(), getHeight());
	}

	public String[] getLinkLabels() {
		int nr = allLinks.size();
		String[] result = new String[nr];
		for (int i = 0; i < nr; i++) {
			//      result[i] = "Step "
			// +((Link)allLinks.elementAt(i)).getLinkDescription();
			result[i] = allLinks.elementAt(i).getLinkDescription();
		}
		return result;
	}

	public int getNrOfSteps() {
		return allLinks.size();
	}

	public int getPositionOfStep(int stepNr) {
		int linkPos = -1, nrLinks = allLinks.size();
		for (int a = 0; linkPos == -1 && a < nrLinks; a++) {
			if (stepNr == allLinks.elementAt(a).getStep())
				linkPos = a;
		}
		return linkPos;
	}

	public Animation getPartialAnimation(boolean[] exportSteps) {
		if (exportSteps == null)
			return this;

		// check if this is the full animation :-)
		int currentStep = 0;
		while (currentStep < exportSteps.length && exportSteps[currentStep])
			currentStep++;

		if (currentStep == exportSteps.length)
			return this;
		int currentIndex = 0;

		// check which step(s) have to be exported...
		AnimationState animationState = new AnimationState(this);
		currentStep = Link.START;
		animationState.setStep(currentStep, true);
		animationState.setStep(animationState.getNextStep(), true);
		currentStep = animationState.getStep();
//		int nrOfObjects = getNextGraphicObjectNum();
//		boolean[] objectHasChangedSinceExport = new boolean[nrOfObjects + 1];
		while (currentStep != Link.END) {
			if (exportSteps[currentIndex]) {
    		// do nothing
			}

			// go to next step and step index
			animationState.setStep(animationState.getNextStep(), true);
			currentStep = animationState.getStep();
			currentIndex++;
		}

		//    return targetAnimation;
		return this;
	}

	public Animation getPartialAnimation2(boolean[] exportSteps) {
		if (exportSteps == null)
			return this;
		// determine if export concerns full animation -- then simply return
		// "this"!
		int i = 0;
		while (i < exportSteps.length && exportSteps[i])
			i++;
		if (i == exportSteps.length)
			return this;

		// declare and initialize several helper objects...
//		PTGraphicObject ptgo = null;
		// make sure only the GOs we really need are included!
		AnimationState animationState = new AnimationState(this);
		int nrOfObjects = getNextGraphicObjectNum();
		boolean[] objectHasBeenExported = new boolean[nrOfObjects + 1];
		boolean[] dirtyObject = new boolean[nrOfObjects + 1];
//		boolean[] visibleInStep = new boolean[nrOfObjects + 1];
//		int currentObjectID = 1;
		int currentStep = Link.START;
//		GraphicVectorEntry[] objectsVisibleInStep = null;
		animationState.setStep(currentStep, true);
		animationState.setStep(animationState.getNextStep(), true);
		currentStep = animationState.getStep();
//		int previousStep = Link.START;
		int j = 0;
		XProperties propertyHandler = new XProperties();
//		String dirtyUsedKey = "dirtyUsed";

		// build the required animation structure
		Animation animationToExport = buildNewAnimationStructure(exportSteps);

		// loop over the individual steps!
		while (currentStep != Link.END) {
			// no elements added in this new step
			if (!exportSteps[currentStep]) {
				// must determine the "dirty" objects

				// loop over all animators for the current step
				Vector<Animator> animators = animationState.getCurrentAnimators();
				for (j = 0; j < animators.size(); j++) {
					Animator animator = animators.elementAt(j);

					// retrieve the IDs affected by "relevant" operation -- not
					// simply show/hide!
					// on second reflection, it *is* relevant whether the object
					// has since been hidden!
					if (animator.isChangingAnimator()) {
						int[] objectNums = animator.getObjectNums();
						for (int pos = 0; pos < objectNums.length; pos++)
							dirtyObject[objectNums[pos]] = true; // mark object
																 // as "dirty"
					} // if isChangingAnimator
				} // end loop all animators for step
			} // if step is not exported
			else { // inside a step that will be exported

				// divide the action into the following sections:

				//  1. insert all visible GOs that were NOT exported previously
				//     Use the clone of the current state for this purpose!

				//  2. insert a new link *before* the currrent step containing
				// all formerly
				//    "dirty" objects at their current position at the start of the
				// step.
				//    Link delay must be 0. Reset the objects affected as
				// "non-dirty",
				//    and update the "mapper" entries accordingly!

				//     a. determine the "dirty" objects
				//     b. insert a new link for this effect
				//     c. generate a TimedShow animator for hiding the old objects
				//     d. generate a TimedShow for showing the new objects, updating
				// the mapping
				//     e. add this TimedShow to the animation

				//  3. insert a mapped(!) animator for each animator
				//     This mapped animator contains a mapping of each object ID
				//     to an appropriate new ID, if necessary

				// 1. directly insert all GOs that were NOT previously exported
				animationToExport.directGraphicObjectExport(animationState,
						objectHasBeenExported);

				// 2a. determine all "dirty" objects that must be shown
				// separately
				int[] reinsertTheseObjects = animationToExport
						.determineDirtyExportObjects(animationState,
								dirtyObject);

				// 2b. - 2e. insert a new link; add a show/hide for the new
				// objects
				//           and update the object mappings
				animationToExport.insertTemporaryExportLink(animationState,
						reinsertTheseObjects, currentStep, propertyHandler);

				// 3. export "standard" animators
				animationToExport.exportAnimators(animationState
						.getCurrentAnimators(), propertyHandler,
						animationState, objectHasBeenExported);

//				previousStep = currentStep;
			}
			currentStep = animationState.getNextStep();
			animationState.setStep(currentStep, true);
		}

		//    animationToExport.dumpState(true, true);
		return animationToExport;
	}

	private Animator updateObjectIDs(Animator animator, XProperties mapping) {
		if (animator == null || mapping == null)
			return null;
		Animator newAnimator = (Animator) animator.clone();
		int[] animatorObjects = newAnimator.getObjectNums();
		int nrOfObjects = animatorObjects.length;
		int[] newAnimatorObjects = new int[nrOfObjects];
		for (int i = 0; i < nrOfObjects; i++) {
			newAnimatorObjects[i] = mapping.getIntProperty(String
					.valueOf(animatorObjects[i]), animatorObjects[i]);
		}
		newAnimator.setObjectNums(newAnimatorObjects);
		return newAnimator;
	}

	private Animation buildNewAnimationStructure(boolean[] selectedSteps) {
		Animation animationToExport = new Animation();
		int nrOfSteps = selectedSteps.length, previousStep = Link.START;
		for (int j = 0; j < nrOfSteps; j++) {
			if (selectedSteps[j]) {
				animationToExport.insertLink(new Link(previousStep, j));
				previousStep = j;
			}
		}
		animationToExport.insertLink(new Link(previousStep, Link.END));
		return animationToExport;
	}

	/**
	 * Export those graphic objects that have not been exported before
	 * 
	 * @param animationState
	 *            the current state of the animation, used to retrieve the
	 *            up-to-date clone of the graphic object(s) to be exported
	 * @param isExported
	 *            allows determination for each object what its export status is
	 */
	private void directGraphicObjectExport(AnimationState animationState,
			boolean[] isExported) {
		PTGraphicObject ptgo = null;
		GraphicVectorEntry[] objectsVisibleInStep = animationState
				.getCurrentObjects().convertToArray();
		if (objectsVisibleInStep != null && objectsVisibleInStep.length != 0)
			for (int objectID = 0; objectID < objectsVisibleInStep.length; objectID++) {
				// retrieve the objects visible from the previous step(s)
				ptgo = objectsVisibleInStep[objectID].getGraphicObject();
				if (ptgo != null && !isExported[ptgo.getNum(false)]) {
					insertGraphicObject(animationState.getCloneByNum(ptgo
							.getNum(false)));
					isExported[ptgo.getNum(false)] = true;
				}
			} // export all ptgo if not done
	}

	/**
	 * Determine the "dirty" objects of the current animation state. An object
	 * is "dirty" if it has been animated (except for a simple show/hide) in a
	 * previous step that was not exported. To determine this, take the
	 * animators of the current step and examine each object used in an animator
	 * for "dirty" status. Then do the same on all visible objects.
	 * 
	 * @param animationState
	 *            the current state of the animation
	 * @param isDirty
	 *            the current state of "dirtiness" of the objects; is reset in
	 *            this method for those objects which were tagged
	 * @return the int array containing the objects that have changed
	 */
	private int[] determineDirtyExportObjects(AnimationState animationState,
			boolean[] isDirty) {
		int objectNr = 0, objectID = 0;
		int animatorNr;
		XProperties localHandler = new XProperties();
		Vector<Animator> animators = animationState.getCurrentAnimators();
		String dirty = "dirty";
		localHandler.put(dirty, " ");

		// a "dirty" object is only relevant if it is used in an animator...
		for (animatorNr = 0; animatorNr < animators.size(); animatorNr++) {
			Animator animator = animators.elementAt(animatorNr);
			// check for additional PTGOs that have to be added!
			int[] objectNums = animator.getObjectNums();
			for (objectNr = 0; objectNr < objectNums.length; objectNr++)
				if (isDirty[objectNums[objectNr]]) {
					localHandler.put(dirty, localHandler.get(dirty) + " "
							+ objectNums[objectNr]);
					isDirty[objectNums[objectNr]] = false;
				}
		}

		// ... or is simply visible!
		GraphicVectorEntry[] objectsVisibleInStep = animationState
				.getCurrentObjects().convertToArray();
		if (objectsVisibleInStep != null && objectsVisibleInStep.length != 0)
			for (objectNr = 0; objectNr < objectsVisibleInStep.length; objectNr++) {
				// retrieve numeric ID of object
				objectID = objectsVisibleInStep[objectID].getGraphicObject()
						.getNum(false);
				if (isDirty[objectID])
					localHandler.put(dirty, localHandler.get(dirty) + " "
							+ objectID);
			}
		int[] dirtyMustBeReinserted = null;
		if (localHandler.getProperty(dirty) != null)
			dirtyMustBeReinserted = localHandler.getIntArrayProperty(dirty);
		localHandler = null;
		animators = null;
		dirty = null;
		return dirtyMustBeReinserted;
	}

	/**
	 * Insert a new step that hides the previous objects and show the new state
	 * 
	 * @param animationState
	 *            the current animation state used for retrieving the objects
	 * @param targetIDs
	 *            the numeric IDs of the objects to redisplay
	 * @param currentStep
	 *            the current step of the animation
	 * @param propertyHandler
	 *            the handler of the object IDs. The new IDs will be added to
	 *            property as "oldID -> newID"
	 */
	private void insertTemporaryExportLink(AnimationState animationState,
			int[] targetIDs, int currentStep, XProperties propertyHandler) {
		if (targetIDs == null || targetIDs.length == 0)
			return;
		// generate a new animation step just before the current step with no
		// delay
		int generatedStepID = newStep(currentStep);
		Link generatedLink = getLink(generatedStepID);
		generatedLink.setMode(Link.WAIT_TIME);
		generatedLink.setTime(0);

		// add a TimedShow for hiding the selected objects
		TimedShow timedShow = new TimedShow(generatedStepID, targetIDs, 0,
				"hide", false);
		insertAnimator(timedShow);

		// now map the objects!
		PTGraphicObject ptgo = null, ptgoClone = null;
		int nrOfObjects = targetIDs.length;
		int[] newIDs = new int[nrOfObjects];
		for (int objectNr = 0; objectNr < nrOfObjects; objectNr++) {
			ptgo = animationState.getCloneByNum(targetIDs[objectNr]);
			ptgoClone = (PTGraphicObject) ptgo.clone();
			ptgoClone.resetNum();
			newIDs[objectNr] = ptgoClone.getNum(true);
			propertyHandler.put(String.valueOf(ptgo.getNum(false)), ptgoClone
					.getNum(false));
		}
		// add a TimedShow for showing the newly mapped objects
		timedShow = new TimedShow(generatedStepID, newIDs, 0, "show", true);
		insertAnimator(timedShow);
	}

	private void exportAnimators(Vector<Animator> animators, 
			XProperties mapping, AnimationState animationState,
			boolean[] isExported) {
		// Now deal with all animators
		for (int j = 0; j < animators.size(); j++) {
			// add all animators of exported steps - for now, "as is"!
			Animator animator = animators.elementAt(j);
			insertAnimator(updateObjectIDs(animator, mapping));
			//      int[] objectNums = animator.getSpecialObjectNums();
			int[] objectNums = animator.getTemporaryObjects();
			if (objectNums != null)
				for (int k = 0; k < objectNums.length; k++) {
					PTGraphicObject ptgo = animationState
							.getCloneByNum(objectNums[k]);
					if (ptgo != null && !isExported[ptgo.getNum(false)]) {
						isExported[ptgo.getNum(false)] = true;
						insertGraphicObject(ptgo);
					} // if ptgo exists and has not been exported, export!
				} // export all ptgos is not yet done
		}
	}

	public boolean hasSingleAnimatorInStep(int[] selectedOIDs, int step,
			Animator currentAnimator) {
		return ((checkSingleAnimator(selectedOIDs, step, currentAnimator)) == null);
	}

	public String checkSingleAnimator(int[] selectedOIDs, int step,
			Animator currentAnimator) {
		int nrOfOIDs = (selectedOIDs == null) ? 0 : selectedOIDs.length;
		StringBuilder usedBuffer = new StringBuilder(nrOfOIDs << 2);
		for (int oidIndex = 0; oidIndex < nrOfOIDs; oidIndex++) {
			// the current object is expected to contain the element, so
			// ignore when searching.
			if (get().getAnimator(step, selectedOIDs[oidIndex], currentAnimator) != null) {
				usedBuffer.append(selectedOIDs[oidIndex]).append(' ');
			}
		}
		return (usedBuffer.length() == 0) ? null : usedBuffer.toString();
	}

	public long getAnimationUID() {
		return animationUID;
	}

} // Animation
