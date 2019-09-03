/*
 * Created on 15.04.2005 by T. Ackermann
 */
package generators.framework;

/**
 * This interface is implemented by all classes that want to listen to the
 * PropertiesPanel in Final Mode. callMethod is called when the user chose
 * to call a specific Generator Method.
 *
 * @author T. Ackermann
 */
public interface PropertiesPanelListener {
	
	/**
	 * callMethod is called when the user chose
	 * to call a specific Generator Method.
	 * @param methodName The name of the method that should be called.
	 */
	public void callMethod(String methodName);
}
