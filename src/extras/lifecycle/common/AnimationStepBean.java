/**
 * 
 */
package extras.lifecycle.common;

/**
 * @author Mihail Mihaylov
 *
 */
public class AnimationStepBean {
	
	String label;
	int step;
	
	/**
	 * 
	 */
	public AnimationStepBean() {
		super();
	}

	/**
	 * @param label
	 * @param step
	 */
	public AnimationStepBean(String label, int step) {
		super();
		this.label = label;
		this.step = step;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return the step
	 */
	public int getStep() {
		return step;
	}

	/**
	 * @param step the step to set
	 */
	public void setStep(int step) {
		this.step = step;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return label + " (" + step + ")";
	}
	
	

}
