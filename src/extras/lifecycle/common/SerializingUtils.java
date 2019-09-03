/**
 * 
 */
package extras.lifecycle.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


/**
 * 
 * Serialization utilities for management of checkpoints.
 * 
 * @author Mihail Mihaylov
 *
 */
public final class SerializingUtils {
	
	/**
	 * A private constructor to adapt the standards for an utility class.
	 */
	private SerializingUtils() {
		super();
	}

	// http://www.javaworld.com/javaworld/javatips/jw-javatip76.html?page=2
	/**
	 * Makes a deep copy of the given object using serialization.
	 * This way we omit the clone procedure, whose implementation is a tricky one.
	 */
	public static Object deepCopy(Object oldObj) throws Exception {
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream(); // A
			oos = new ObjectOutputStream(bos); // B
			// serialize and pass the object
			oos.writeObject(oldObj); // C
			oos.flush(); // D
			ByteArrayInputStream bin = new ByteArrayInputStream(bos
					.toByteArray()); // E
			ois = new ObjectInputStream(bin); // F
			// return the new object
			return ois.readObject(); // G
		} catch (Exception e) {
			System.out.println("Exception in ObjectCloner = " + e);
			throw (e);
		} finally {
			oos.close();
			ois.close();
		}
	}
	
	public static Object getClone(Object object) {
		Object valueClone;
		try {
			valueClone = deepCopy(object);
		} catch (Exception e) {
			// If no clone was created, we just give up and
			// we give the original object
			System.err.println("Object can not be cloned. Returing a reference");
			valueClone = object;
		}
		
		return valueClone;
	}
	

}
