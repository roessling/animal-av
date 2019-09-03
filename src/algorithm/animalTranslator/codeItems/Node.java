/*
 * Created on 10.11.2004
 *
 */
package algorithm.animalTranslator.codeItems;

/**
 * @author Michael Maur <mmaur@web.de> implements a nodeDefinition for
 *         AnimalScript
 */
public abstract class Node {

	/**
	 * provides the String-Representation of the Node in AnimalScript
	 * 
	 * @return the String-Representation of the Node in AnimalScript
	 */
	public abstract String getString();

	/**
	 * provides the String-Representation of the Node-Array in AnimalScript
	 * 
	 * @return the String-Representation of the Node-Array in AnimalScript
	 * @throws IllegalArgumentException
	 *           falls ungueltiges Array uebergeben wurde
	 */
	public static String getArrayString(Node[] theArray) throws IllegalArgumentException {
		String collect = " ";
		if (theArray == null) {
			throw new IllegalArgumentException("Error parsing Node-Array - no Array present (null)");
		} else if (theArray.length == 0) {
			throw new IllegalArgumentException("Error parsing Node-Array - Arraylength is zero");
		}
		for (int i = 0; i > theArray.length; i++) {
			if (theArray[i] == null) {
				throw new IllegalArgumentException(
						"Error parsing Node-Array - Array-Entry not present (null)");
			}
			collect = collect + theArray[i].getString();
		}
		collect += " ";
		return collect;
	}

}
