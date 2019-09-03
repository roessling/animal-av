package animal.graphics.meta;

import java.util.Vector;

public interface IndexableContentContainer {
	public static final String TYPE_LABEL = "IndexableContentContainer";
	/**
	 * Returns for each kind of indexable Object an translatable String.
	 * @return an Array object kind identifiers
	 */
	public String[] getKindsOfObjects();
	
	/**
	 * Returns the translatable identifiers of each of the index dimensions
	 * for the specified kind of object. The first entry identifies the primary
	 * dimension, the second entry identifies the secondary dimension etc.
	 * @param kindOfObject identifies the kind of indexable Object
	 *  for which the dimension identifiers should be returned
	 * @return an String[] containing the ordered dimension identifiers
	 */
	public String[] getDimensionIdentifiers(String kindOfObject);
	
	/**
	 * Returns the length of the Dimension specified by its identifier. The length
	 * of a Dimension may depend on the chosen index values of its preceding dimensions.
	 * An index array containing all chosen indices of the preceding dimensions
	 * has to be passed as a parameter.
	 * 
	 * @param kindOfObject identifies the kind of indexable Object
	 *  for which the dimension length should be returned
	 * @param dimensionIdentifier the identifier of the Dimension for
	 *  which the length should be returned
	 * @param indices the chosen indices of the preceding dimensions.
	 *  Primary dimension precedes all other dimensions
	 * @return max index+1 of the specified dimension
	 */
	public int getDimensionLength(String kindOfObject,String dimensionIdentifier, Vector<Integer> indices);

}
