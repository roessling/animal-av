package animal.animator;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import jhave.support.InfoFrameInterface;
import translator.AnimalTranslator;
import animal.editor.Editor;
import animal.editor.animators.AnimatorEditor;
import animal.graphics.PTGraphicObject;
import animal.main.Animal;
import animal.main.AnimalConfiguration;
import animal.main.AnimationState;
import animal.main.Link;
import animal.misc.AnimalInfoFrame;
import animal.misc.EditableObject;
import animal.misc.MessageDisplay;
import animal.misc.ObjectSelectionButton;
import animal.misc.XProperties;

/**
 * Animator is the superclass for all animator objects, i.e. objects that
 * perform operations on GraphicObjects
 * 
 * @author <a href="http://www.ahrgr.de/guido/">Guido R&ouml;&szlig;ling </a>
 * @version 1.1 2000-06-30
 */
@SuppressWarnings("unchecked")
public abstract class Animator extends EditableObject { 
	//implements Externalizable {
	// =================================================================
	//                           CONSTANTS
	// =================================================================

	/**
	 * The label to use when requesting the method of an animator
	 */
	public static final String METHOD_LABEL = "method";

	/**
	 * The label to use when requesting the object IDs of an animator
	 */
	public static final String OID_LABEL = "objectNums";

	/**
	 * The label to use when requesting the step number of an animator
	 */
	public static final String STEP_LABEL = "step";

	/**
	 * This constant contains the UID for serialization, <em>do not edit</em>!
	 */
//	private static final long serialVersionUID = -8003195822156146231L;

	// =================================================================
	//                                 STATICS
	// =================================================================

	/**
	 * The registered object types, needed for parsing
	 */
	public static XProperties DefaultProperties;

	/**
	 * the infoFrame interface to be used for interactions
	 */
	public static InfoFrameInterface infoFrame = null;

	/**
	 * This hashtable contains a mapping of ASCII keywords to animators. It is
	 * used for looking up the appropriate animator when parsing ASCII code.
	 */
	public static Hashtable<String, String> registeredHandlers;

	// =================================================================
	//                       CLASS INITIALIZER
	// =================================================================

	/**
	 * Static class initializer; will instantiate the handlers
	 */
	static {
		if (registeredHandlers == null) {
			registeredHandlers = new Hashtable<String, String>(23);
		}

		//FIXME is this correct?
		Hashtable<String, Editor> animalEditors = Animal.get().getEditors();
		Enumeration<String> e = animalEditors.keys();
		String editorName = null;
		Editor editor = null;
		String type = null;

		while (e.hasMoreElements()) {
			editorName = e.nextElement();
			editor = animalEditors.get(editorName);

			if ((editor != null) && editor instanceof AnimatorEditor) {
				// retrieve keywords handled by type
				type = "animal.animator." + editorName;

				try {
					Class<Animator> targetClass = (Class<Animator>) Class.forName(type);
					Animator animator = targetClass.newInstance();

					if (animator != null) {
						String[] handledKeywords = animator.handledKeywords();

						if (handledKeywords != null) {
							for (int i = 0; i < handledKeywords.length; i++) {
								registeredHandlers.put(handledKeywords[i].toLowerCase(),
										type);
							}
						}
					}
				} catch (ClassNotFoundException classNotFound) {
					MessageDisplay.errorMsg("classNotFoundException",
							new Object[] { type });
				} catch (IllegalAccessException illegalAccess) {
					MessageDisplay.errorMsg("illegalAccessException",
							new Object[] { type });
				} catch (InstantiationException instantiationException) {
					MessageDisplay.errorMsg("instantiationException",
							new Object[] { type });
				}
			}
		}
	}

	// =================================================================
	//                               TRANSIENTS
	// =================================================================

	/**
	 * Tells if the animator has finished its execution
	 */
	protected transient boolean finished = false;

	/**
	 * The GraphicObjects to work on. These may vary, because Animators work on
	 * clones of the original objects. Thus it must be reset whenever a new
	 * clone is created (which happens in AnimationState.reset()). This reset is
	 * done in <code>init</code>.
	 * 
	 * @see #init(animal.main.AnimationState, long, double)
	 */
	protected transient PTGraphicObject[] objects;
    
    /**
     * the animation step during which the animator is specified
     */
    private int animationStep = Link.START;

	// =================================================================
	//                           CONSTRUCTORS
	// =================================================================

	/**
	 * Public(empty) constructor required for serialization.
	 */
	public Animator() {
		if (DefaultProperties == null) {
			initializeDefaultProperties(AnimalConfiguration
					.getDefaultConfiguration().getProperties());
		}

		setProperties((XProperties) DefaultProperties.clone());
	}

	/**
	 * Construct a new animator for a given step and the given object ID.
	 * 
	 * @param step
	 *            the step in which the animator is placed
	 * @param objectNum
	 *            the number of the object used in the animator
	 */
	public Animator(int step, int objectNum) {
		this(step, new int[] { objectNum });
	}

	/**
	 * Construct a new animator for a given step and the given object IDs.
	 * 
	 * @param step
	 *            the step in which the animator is placed
	 * @param objectNums
	 *            the numbers of the objects used in the animator
	 */
	public Animator(int step, int[] objectNums) {
		this();
		setStep(step);
		setObjectNums(objectNums);
	}
	
	/**
	 * Construct a new animator for a given step and the given object IDs.
	 * 
	 * @param step
	 *            the step in which the animator is placed
	 * @param objectNums
	 *            the numbers of the objects used in the animator
	 */
	public Animator(int step, Vector<Integer> objectNums) {
		this();
		setStep(step);
		setObjectNumVector(objectNums);
	}


	private void setObjectNumVector(Vector<Integer> objectNums) {
		int nrElems = objectNums.size();
		int[] objectNumbers = new int[nrElems];
		for (int i=0; i < nrElems; i++)
			objectNumbers[i] = objectNums.elementAt(i).intValue();
		getProperties().put(OID_LABEL, objectNumbers);		
	}

	/**
	 * creates a new Animator instance based on the properties passed in
	 * 
	 * @param props
	 *            the properties from which to generate the new object
	 */
	public Animator(XProperties props) {
		setProperties(props);
	}

	/***************************************************************************
	 * retrieve the file version for this animator Note: this method must be
	 * overridden by all subclasses!
	 * 
	 * @return the file version for the animator, needed for import/export
	 */
	public abstract int getFileVersion();

	/**
	 * Returns all properties starting with the 'prefix' string. Used to
	 * initialize the default properties of all subtypes.
	 * 
	 * @param props
	 *            the properties object from which the properties are extracted,
	 *            typically taken directly from animal.main.Animal
	 * @param prefix
	 *            the prefix for the attributes to extract
	 * @return the selected attributes as an XProperties object
	 */
	public static XProperties extractDefaultProperties(XProperties props,
			String prefix) {
		return props.getElementsForPrefix(prefix);
	}

	// =================================================================
	//                         INITIALIZATION
	// =================================================================

	/**
	 * Initialize the default properties object!
	 * 
	 * @param properties
	 *            the properties object used for initialization
	 */
	public static void initializeDefaultProperties(XProperties properties) {
		DefaultProperties = extractDefaultProperties(properties, "Animator");
	}

	/**
	 * Prepares the Animator for use by the AnimationState and initializes it.
	 * If any actions have to be done <em>before</em>
	 * <code>action</code> is
	 * called, it has to be done here.
	 * 
	 * @param animationState
	 *            the animation state in which the animator is placed
	 * @param time
	 *            the duration of this animator in ms
	 * @param ticks
	 *            the duration of this animator in ticks
	 */
	public void init(AnimationState animationState, 
			 long time, 
			 double ticks) {
		//     objects can't be initialized before here, because at
		//     construction time it's not sure which Vector will contain the
		//     GraphicObjects at the time this Animation is executed!
		int[] objectNums = getObjectNums();

		if (objectNums != null) {
			objects = new PTGraphicObject[objectNums.length];

			// get references to all clones required by this animator
			for (int a = 0; a < objectNums.length; a++)
				objects[a] = animationState.getCloneByNum(objectNums[a]);

			finished = false;
		} else if (!(this instanceof TimedAnimator)) {
			MessageDisplay.errorMsg(AnimalTranslator.translateMessage(
					"noObjectsSetException", new Object[] {
							String.valueOf(animationState.getStep()),
							getAnimatorName() }), MessageDisplay.RUN_ERROR);
		}
	}

	// =================================================================
	//                           ANIMATION EXECUTORS
	// =================================================================

	/**
	 * Sets the object to the state it has after a certain time has passed. Can
	 * rely on <code>init</code> begin called before. Should call <code>
	 * execute</code> when finished(e.g. because time is over). If <code>
	 * hasFinished()</code> is true, it should return immediately
	 * 
	 * @param time
	 *            the current time [ms], used for determining what action is to
	 *            be taken
	 * @param ticks
	 *            the current time in ticks, used for determining what action is
	 *            to be taken
	 * @see #hasFinished()
	 */
	public abstract void action(long time, double ticks);

	/**
	 * Changes the state of the GraphicObject to its final state after
	 * completely executing the Animator. Must be overwritten and called by
	 * subclasses. The object must have been initialized before but some actions
	 * may have been done meanwhile. Should be called at the end of <code>
	 * action</code>
	 * 
	 * @see #action(long, double)
	 */
	public void execute() {
		setFinished(true);
	}

	/**
	 * Returns whether this animator has completely finished its work.
	 * 
	 * @return true if <code>execute</code> has been called, else <code>false
	 *         </code>
	 */
	public boolean hasFinished() {
		return finished;
	}

	/**
	 * set the status to "finished" or "unfinished"
	 * 
	 * @param finishState
	 *            if true, the animator has finished executing
	 */
	public void setFinished(boolean finishState) {
		finished = finishState;
	}

	// =================================================================
	//                        ATTRIBUTE GET/SET
	// =================================================================

	/**
	 * checks if the animator animates the object with ID oid
	 * 
	 * @param oid
	 *            the numeric ID of a given graphical object
	 * @return true if the ID passed in is part of the animated object IDs, else
	 *         false
	 */
	public boolean animatesObjectByID(int oid) {
		int[] oids = getObjectNums();

		if ((oids == null) || (oids.length == 0)) {
			return false;
		}

		boolean found = false;

		for (int i = 0; (i < oids.length) && !found; i++)
			found = (oids[i] == oid);

		return found;
	}

	/**
	 * Returns the name of this animator, used for saving.
	 * 
	 * @return the name of the animator.
	 */
	public abstract String getAnimatorName();

	/**
	 * Returns the objects the Animator works on except for temporary objects.
	 * 
	 * @return the numeric IDs of the affected objects.
	 */
	public int[] getObjectNums() {
		return getProperties().getIntArrayProperty(OID_LABEL);
	}

	/*
	 * public int[] getSpecialObjectNums() { return null; }
	 */

	/**
	 * Returns the step in which this animator takes place.
	 * 
	 * @return the number of the step in which the animator is executed.
	 */
	public int getStep() {
//      return animationStep;
      if (animationStep != getProperties().getIntProperty(STEP_LABEL, 1))
        System.err.println("Step != : " + animationStep +", " 
            +getProperties().getIntProperty(STEP_LABEL, 1));
		return getProperties().getIntProperty(STEP_LABEL, 1);
	}

	/**
	 * Returns the temporary objects of this Animator. Temporary object are
	 * required for animation, but are not animated themselves: for example,
	 * move paths. These are passed as an <code>int[]</code> and not as
	 * PTGraphicObject[], as resetting the numbers of the temporary objects in
	 * the Animators doesn't change the objects but only the numbers. The
	 * objects are not changed until the animator is reinitialized.
	 * 
	 * @return an array containing the numerics IDs of the temporary objects;
	 *         <code>null</code> if no temporary objects are used.
	 */
	public int[] getTemporaryObjects() {
		return null;
	}

	/**
	 * Returns the keywords of Animal's ASCII format this animator handles.
	 * 
	 * @return a String array of the keywords handled by this animator.
	 */
	public abstract String[] handledKeywords();

	/**
	 * Determines whether this Animator changes its object.
	 * 
	 * @return <code>true</code> if the Animator(potentially) changes any of
	 *         the object attributes, <code>false</code> otherwise.
	 *         <p>
	 *         E.g. Show never changes attributes, with Move and Rotate it
	 *         depends on the start and end state of the object, therefore they
	 *         should be declared as changing Animators. If the object really is
	 *         the same as before the Animator's execution, this could be false,
	 *         but implementing this could be quite tricky to implement and
	 *         difficult to debug. This should be a static method, as being a
	 *         changing animator depends on the class and not on the instance!
	 *         But this is not possible as with a static method, always this
	 *         method is called, not that of the subclasses, if a variable is
	 *         declared of type Animator! So this method only has to be
	 *         overwritten if an Animator does not change its object's
	 *         attributes.
	 */
	public boolean isChangingAnimator() {
		return true;
	}

    /**
     * Determines whether this Animator animates graphical object.
     * 
     * @return <code>true</code> if the Animator animates graphical objects.
     * This is relevant because animators that do <em>not</em> animate
     * graphical objects will throw an error message if they are invoked,
     * as the number of affected objects and / or their IDs will be undefined.
     */
    public boolean isGraphicalObjectAnimator() {
        return true;
    }

	/**
	 * retrieves the current infoFrame. If none is available, creates a new one.
	 * 
	 * @return an InfoFramInterface instance which is guaranteed to be non-null
	 */
	public static InfoFrameInterface getInfoFrame() {
		if (infoFrame == null) {
			infoFrame = new AnimalInfoFrame();
		}

		return infoFrame;
	}

	/**
	 * assigns the current infoFrame.
	 * 
	 * @param anInfoFrame
	 *            the InfoFramInterface instance to be used
	 */
	public static void setInfoFrame(InfoFrameInterface anInfoFrame) {
		infoFrame = anInfoFrame;
	}

	/**
	 * Set the object numbers for this animator to the array passed.
	 * 
	 * @param objectNumbers
	 *            the array of object numbers to be used for this animator
	 */
	public void setObjectNums(int[] objectNumbers) {
		getProperties().put(OID_LABEL, objectNumbers);
	}

	/**
	 * returns the current animator type name
	 * 
	 * @return the type name of this animator
	 */
	public abstract String getType();

	/**
	 * Sets the step in which this animator takes place.
	 * 
	 * @param targetStep
	 *            the number of the step in which the animator is executed.
	 */
	public void setStep(int targetStep) {
      animationStep = targetStep;
      getProperties().put(STEP_LABEL, targetStep);
	}

	// =================================================================
	//                               I/O
	// =================================================================

	/**
	 * Reset the attributes for this animator for a "clean memory" state.
	 */
	public void discard() {
		setObjectNums(null);
		objects = null;
	}

	/**
	 * Return a String represenation of the int array passed in.
	 * 
	 * @param ids
	 *            the int array to convert to a String
	 * @return a String representation of the int array passed in
	 */
	public static String printIDs(int[] ids) {
		int n = ids.length;
		StringBuilder sb = new StringBuilder(ids.length * 5);

		for (int i = 0; i < n; i++)
			sb.append(ids[i]).append(' ');

		return sb.toString();
	}

	/**
	 * Determine which objects are used(and thus prevented from deletion).
	 * 
	 * @param usedValues
	 *            updated with the info that the used objects must not be
	 *            deleted.
	 */
	public void scavenge(boolean[] usedValues) {
		int[] objectNums = getObjectNums();
		int oidLengths = objectNums.length;
		int usedLength = usedValues.length;

		for (int i = 0; i < oidLengths; i++)
			if (usedLength >= objectNums[i]) {
				usedValues[objectNums[i] - 1] = true;
			}
	}

	/**
	 * Return the Animator's description to be displayed in the
	 * AnimationOverview.
	 * 
	 * @return the String representation of this object.
	 */
	public String toString() {
		return ObjectSelectionButton.getNumArrayString(getObjectNums());
	}

	/**
	 * Return a String representation of this object until position length.
	 * 
	 * @param length
	 *            the position up to which the String is returned
	 * @return the first <code>length</code> characters of the String
	 *         representation of this object.
	 */
	public String toString(int length) {
		return toString().substring(0, length);
	}
    
    public void setProperties(XProperties props) {
      super.setProperties(props);
      if (props != null)
        setStep(props.getIntProperty(STEP_LABEL, 1));
    }

    
}