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
public class VariableIncrease extends Animator {
	private static final int FILE_VERSION = 1;
	public static final String TYPE_LABEL = "VariableIncrease";
	
	private transient AnimationState animState;
	private String name;

	public VariableIncrease() {
		// do nothing; only used for serialization
	}

	public VariableIncrease(int step, String name) {
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
		String value = animState.getVariables().get(name).getValue();
		
		try {
			int newValue = Integer.parseInt(value) + 1;
			animState.getVariables().get(name).setValue(Integer.toString(newValue));
			
			AnimalConfiguration.getDefaultConfiguration().getWindowCoordinator()
					.getVariableView().setStep(getStep());
		}
		catch(NumberFormatException e)
		{
			System.out.println("WARNING: " + name + " is not a number, ignoring INC command");
		}
	}

	public void counterAction(int value) {
		animState.getVariables().get(name).setValue(Integer.toString(value + 1));
		
		AnimalConfiguration.getDefaultConfiguration().getWindowCoordinator()
				.getVariableView().setStep(getStep());
	}
	
	public String getAnimatorName() {
		return "VariableIncrease";
	}

	public int getFileVersion() {
		return FILE_VERSION;
	}

	public String getType() {
		return TYPE_LABEL;
	}

	public String[] handledKeywords() {
		return new String[] { "VariableIncrease" };
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
		return "Variable Increase: "+name;
	}
}
