package animal.animator;

import animal.main.AnimalConfiguration;
import animal.main.AnimationState;

/**
 * Animator for showing or hiding GraphicObjects. This is achieved by inserting
 * them into or deleting them from a GraphicVector.
 * 
 * @author <a href="http://www.ahrgr.de/guido/">Guido R&ouml;&szlig;ling </a>
 * @version 1.0 2008-02-22
 */
public class VariableDiscard extends Animator {
	private static final int FILE_VERSION = 1;
	public static final String TYPE_LABEL = "VariableUpdate";
	
	private transient AnimationState animState;
	private String name;
//	private String newValue;

	public VariableDiscard() {
		// do nothing; only used for serialization
	}

	public VariableDiscard(int step, String name) {
		super(step, (int[]) null);
		this.name = name;
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
		animState.getVariables().remove(name);
		
		AnimalConfiguration.getDefaultConfiguration().getWindowCoordinator()
				.getVariableView().setStep(getStep());
	}

	public String getAnimatorName() {
		return "VariableDiscard";
	}

	public int getFileVersion() {
		return FILE_VERSION;
	}

	public String getType() {
		return TYPE_LABEL;
	}

	public String[] handledKeywords() {
		return new String[] { "VariableDiscard" };
	}

	public boolean isChangingAnimator() {
		return false;
	}

	public boolean isGraphicalObjectAnimator() {
		return false;
	}

	public void discard() {
		animState = null;
		name = null;
		super.discard();
	}

	public String toString() {
		return "Variable Discard: "+name;
	}
}
