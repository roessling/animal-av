package animal.animator;

import animal.main.AnimalConfiguration;
import animal.main.AnimationState;
import animal.variables.Variable;
import animal.variables.VariableRoles;

/**
 * Animator for showing or hiding GraphicObjects. This is achieved by inserting
 * them into or deleting them from a GraphicVector.
 * 
 * @author <a href="http://www.ahrgr.de/guido/">Guido R&ouml;&szlig;ling </a>
 * @version 1.0 2008-02-22
 */
public class VariableDeclaration extends Animator {

	private static final int FILE_VERSION = 1;
	public static final String TYPE_LABEL = "VariableDeclaration";
	private transient AnimationState animState;

	private String name;
	private String value;
	private int initStep;
	private VariableRoles role;

	public VariableDeclaration() {
		// do nothing; only used for serialization
	}

	public VariableDeclaration(int step, String name) {
		this(step, name, "", VariableRoles.UNKNOWN);
	}

	public VariableDeclaration(int step, String name, String value) {
	  this(step, name, value, VariableRoles.UNKNOWN);
	}
	
	public VariableDeclaration(int currentStep, String varName, String varValue,
	    VariableRoles varRole) {
	  
		super(currentStep, (int[]) null);
		name = varName;
		value = varValue;
		initStep = currentStep;
		role = varRole;
	}

	public void init(AnimationState animationState, long time, double ticks) {
		super.init(animationState, time, ticks);
		animState = animationState;
	}

	public void action(long time, double ticks) {
		execute();
	}

	public void execute() {
		super.execute();
		
		Variable newVar = new Variable(name, value, initStep, role);
		animState.putVariable(newVar);
		AnimalConfiguration.getDefaultConfiguration().getWindowCoordinator().
				getVariableView().setStep(getStep());
	}

	// =================================================================
	// ATTRIBUTE GET/SET
	// =================================================================

	/**
	 * Returns the name of this animator, used for saving.
	 * 
	 * @return the name of the animator.
	 */
	public String getAnimatorName() {
		return "VariableDeclaration";
	}

	/***************************************************************************
	 * retrieve the file version for this animator Note: this method must be
	 * overridden by all subclasses!
	 * 
	 * @return the file version for the animator, needed for import/export
	 */
	public int getFileVersion() {
		return FILE_VERSION;
	}

	public String getType() {
		return TYPE_LABEL;
	}

	/**
	 * Returns the keywords of Animal's ASCII format this animator handles.
	 * 
	 * @return a String array of the keywords handled by this animator.
	 */
	public String[] handledKeywords() {
		return new String[] { "VariableDeclaration" };
	}

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
	 *         attributes, as is the case here.
	 */
	public boolean isChangingAnimator() {
		return false;
	}

	/**
	 * Determines whether this Animator animates graphical object.
	 * 
	 * @return <code>true</code> if the Animator animates graphical objects.
	 *         This is relevant because animators that do <em>not</em> animate
	 *         graphical objects will throw an error message if they are
	 *         invoked, as the number of affected objects and / or their IDs
	 *         will be undefined.
	 */
	public boolean isGraphicalObjectAnimator() {
		return false;
	}

	// =================================================================
	// I/O
	// =================================================================

	/**
	 * Reset the attributes for this animator for a "clean memory" state.
	 */
	public void discard() {
		animState = null;
		name = null;
		value = null;
		initStep = -1;
		super.discard();
	}

	/**
	 * Return the Animator's description to be displayed in the
	 * AnimationOverview.
	 * 
	 * @return the String representation of this object.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder(255);
		sb.append(name);
		if (value != null && value.length() > 0)
			sb.append(" = ").append(value);
		sb.append(";");
		return sb.toString();
	}

	public AnimationState getAnimState() {
		return animState;
	}

	public void setAnimState(AnimationState animState) {
		this.animState = animState;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
