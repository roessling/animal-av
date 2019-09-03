/**
 * 
 */
package extras.lifecycle.checkpoint;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import extras.lifecycle.checkpoint.annotation.Checkpointing;
import extras.lifecycle.common.SerializingUtils;

/**
 * This defines the properties e.g fields, which should be checkpoint-ed for a
 * given class.
 * 
 * @author Mihail Mihaylov
 * 
 */
public class CheckpointedClass {

	/**
	 * This class will be observed.
	 */
	private Class<?> thisClass;

	/**
	 * Model of the <code>checkpoints</code>, which will be created. It
	 * corresponds to the fields from the <code>thisClass</code> object.
	 */
	private CheckpointModel checkpointModel;

	/**
	 * Creates a <code>CheckpointedClass</code> for a given class.
	 * 
	 * @param thisClass
	 *            is the class, which will be observed.
	 */
	public CheckpointedClass(Class<?> thisClass) {
		super();
		this.thisClass = thisClass;
		buildCheckPointModel();
	}

	/**
	 * Creates a <code>CheckpointModel</code>. This model contains all
	 * properties of the given class, which will be saved by doing a checkpoint.
	 */
	private void buildCheckPointModel() {
		// Holds all properties, which have been annotated with
		// <code>Checkpointing</code>
		Set<String> tProperties = new HashSet<String>();

		Checkpointing classCp = thisClass.getAnnotation(Checkpointing.class);

		boolean addAll = false;

		if (classCp != null)
			addAll = true;

		// If we use getDeclaredFields() we access only the field in the current
		// class and not the fields of the superclass.
		for (Field field : thisClass.getFields()) {
			String fieldName = field.getName();
			if (addAll)
				tProperties.add(fieldName);
			else {
				// introspect to get the annotation from the field.
				Checkpointing cp = field.getAnnotation(Checkpointing.class);
				if (cp != null) {
					tProperties.add(fieldName);
				}
			}
		}

		// We initialize the checkpointEntryClass
		// This object is used to generate checkpointEntry-s
		checkpointModel = new CheckpointModel(tProperties);
	}

	/**
	 * Generates a checkpoint for a particular instance.
	 * 
	 * @param classInstance
	 *            is the object, which will be observed
	 * @return a new checkpoint
	 */
	public Checkpoint getCheckpoint(Object classInstance) {
		if (!thisClass.isInstance(classInstance)) {
			// TODO throw a custom exception
			return null;
		}

		// We create a new checkpoint
		Checkpoint checkpoint = checkpointModel.newInstance();
		Set<String> fields = checkpointModel.getFields();
		// We try to put every marked property in this checkpoint
		for (String fName : fields) {
			try {
				Field f = thisClass.getField(fName);
				Object fValue = f.get(classInstance);
				
				// Make a clone of the object
				Object fValueClone = SerializingUtils.getClone(fValue);
				checkpoint.set(fName, fValueClone);

			} catch (SecurityException e) {
				// This should not happen
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// This should not happen
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				// This should not happen
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// This should not happen
				e.printStackTrace();
			}
		}

		return checkpoint;
	}

}
