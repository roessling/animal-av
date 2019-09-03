package extras.lifecycle.checkpoint;

import org.apache.commons.beanutils.BasicDynaBean;
import org.apache.commons.beanutils.DynaClass;
import org.apache.commons.beanutils.DynaProperty;

/**
 * A checkpoint is a snapshot of the state of an observed object.
 * One checkpoint entry contains the variable names and its values.
 * To omit saving up too much unneeded data only the fields annotated with <code>@Checkpointing</code> will
 * be contained in the checkpoint.
 * 
 * @author Mihail Mihaylov
 * 
 */
public class Checkpoint extends BasicDynaBean {
	
	/**
	 * Name of the property defining the sequence number of the checkpoint.
	 */
	public static final String SEQ_NR = "seqnr";
	
	/**
	 * Name of the property defining the checkpoint, which follows the current one.
	 */
	public static final String NEXT = "next";

	/**
	 * This is the generated serial versionUID.
	 */
	private static final long serialVersionUID = 4097416449608062627L;

	/**
     * Construct a new <code>Checkpoint</code> associated with the specified
     * <code>DynaClass</code> instance.
     *
     * @param dynaClass The DynaClass we are associated with
     */
	public Checkpoint(DynaClass dynaClass) {
		super(dynaClass);
		setSequenceNumber(0);
	}
	
	/**
	 * Sets the sequence number of the checkpoint.
	 * @param sequenceNumber parameter for the sequence number
	 */
	public void setSequenceNumber(Integer sequenceNumber) {
		set(SEQ_NR, sequenceNumber);
	}

	/**
	 * Gets the sequence number of the checkpoint.
	 * @return integer containing the sequence number
	 */
	public Integer getSequenceNumber() {
		// it is always an integer
		return (Integer) get(SEQ_NR);
	}

	/**
	 * Sets the checkpoint, which follows the current one.
	 * @param next refers to the following checkpoint
	 */
	public void setNext(Checkpoint next) {
		set(NEXT, next);
	}

	/**
	 * Gets the checkpoint, which follows the current one.
	 * @return the next checkpoint
	 */
	public Checkpoint getNext() {
		return (Checkpoint) get(NEXT);
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append('[');
	
		DynaProperty[] dynaProperties = this.getDynaClass().getDynaProperties();
	
		boolean comma = false;
		for (int i = 0, n = dynaProperties.length; i < n; i++) {
			String fName = dynaProperties[i].getName();
			Object fValue = get(fName);
	
			// We does not display checkpoints because this calls recursive
			// toString()
			if (fValue instanceof Checkpoint)
				fValue = "Checkpoint";
	
			if (comma)
				sb.append(",");
	
			sb.append(fName + " = " + fValue);
	
			comma = true;
		}
		sb.append(']');
		return sb.toString();
	}

}
