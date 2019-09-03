package animal.animator;

import animal.main.AnimalConfiguration;
import animal.main.AnimationState;
import animal.variables.VariableRoles;

/**
 * Animator for showing or hiding GraphicObjects. This is achieved by inserting
 * them into or deleting them from a GraphicVector.
 * 
 * @author <a href="http://www.ahrgr.de/guido/">Guido R&ouml;&szlig;ling </a>
 * @version 1.0 2008-02-22
 */
public class VariableUpdate extends Animator {
	private static final int FILE_VERSION = 1;
	public static final String TYPE_LABEL = "VariableUpdate";
	
	private transient AnimationState animState;
	private String name;
	private String newValue;
	private VariableRoles newRole;
	
	public VariableUpdate() {
		// do nothing; only used for serialization
	}

	public VariableUpdate(int step, String name, String newValue) {
		this(step, name, newValue, VariableRoles.UNKNOWN);
	}
	
  public VariableUpdate(int varStep, String varName, String newVarValue,
      VariableRoles newVarRole) {
    super(varStep, (int[]) null);
    name = varName;
    newValue = newVarValue;
    newRole = (newVarRole != null) ? newVarRole : VariableRoles.UNKNOWN;
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
		if (animState != null) {
			animState.getVariables().get(name).setValue(newValue);
			animState.getVariables().get(name).setRole(newRole);
		}
		
		AnimalConfiguration.getDefaultConfiguration().getWindowCoordinator()
				.getVariableView().setStep(getStep());
	}

	public String getAnimatorName() {
		return "VariableUpdate";
	}

	public int getFileVersion() {
		return FILE_VERSION;
	}

	public String getType() {
		return TYPE_LABEL;
	}

	public String[] handledKeywords() {
		return new String[] { "VariableUpdate" };
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
		newValue = null;
		super.discard();
	}

	public String toString() {
		return "Variable Update: "+name+" to "+newValue;
	}
}
