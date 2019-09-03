/**
 * 
 */
package extras.lifecycle.checkpoint;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BasicDynaClass;
import org.apache.commons.beanutils.DynaProperty;

/**
 * This class describes the "model" of a <code>Checkpoint</code> e.g. knows exactly
 * its properties. An instance of this class is also able to create <code>Checkpoint</code>s
 * with exactly these properties.
 * 
 * @author Mihail Mihaylov
 */
public class CheckpointModel {
	
	/** 
	 * It can create <code>Checkpoints</code> (<code>Dynabeans</code>).
	 */
	private BasicDynaClass dynaClass;
	
	/**
	 * The set of fields, which the <code>Checkpoint</code> should have.
	 */
	private Set<String> fields;
	
	/**
	 * Constructors a <code>CheckpointClass</code>, which creates <code>checkpoints</code>
	 * with the specified fields.
	 * 
	 * @param fields for the <code>checkpoints</code>
	 */
	public CheckpointModel(Set<String> fields) {
		super();
		this.fields = fields;
		init();		
	}

	/**
	 * @return the fields
	 */
	public Set<String> getFields() {
		return fields;
	}
	
	/**
	 * Initializes the <code>dynaClass</code> object.
	 */
	private void init() {
	    List<DynaProperty> dynaProperties = new LinkedList<DynaProperty>();
	    
	    for (String string : fields) {
			dynaProperties.add(new DynaProperty(string));
		}
	    
	    //TODO Make this get this properties automatically in some way
	    // or use LazyDynaBeans
	    dynaProperties.add(new DynaProperty(Checkpoint.NEXT));
	    dynaProperties.add(new DynaProperty(Checkpoint.SEQ_NR));
	    
	    DynaProperty[] dynaPropertiesAsArray = dynaProperties.toArray(new DynaProperty[dynaProperties.size()]);
	    dynaClass = new BasicDynaClass("checkpoint", Checkpoint.class, dynaPropertiesAsArray);
	    
	}
	
	/**
	 * Creates new instance of <code>Checkpoint</code>.
	 * @return new instance of <code>Checkpoint</code>
	 */
	public Checkpoint newInstance() {
		try {
			Checkpoint cp = (Checkpoint) dynaClass.newInstance();
			return cp;
		} catch (IllegalAccessException e) {
			// This can never happen :)
			e.printStackTrace();
		} catch (InstantiationException e) {
			// This can never happen :)
			e.printStackTrace();
		}
		return null;
	}
	
	

}
