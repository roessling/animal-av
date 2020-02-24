/*
 * Created on 29.12.2004 by T. Ackermann
 */

package generators.framework;

import java.util.HashMap;
import java.util.TreeMap;
import java.util.Vector;

import animal.main.Animal;

/**
 * GeneratorType is a class that helps organizing the different Generators into
 * categories like "Sort" or "Graph" algorithms. One Generator has one or many
 * categories at a time.<br>
 * <br>
 * For setting the GeneratorType an integer is passed to the constructor that
 * represents the type(s). To create a Generator for a Sort, Graph and
 * Data-Structure algorithm, call<br>
 * <code>int aType =<br>GeneratorType.GENERATOR_TYPE_SORT |<br>
 * GeneratorType.GENERATOR_TYPE_DATA_STRUCTURE |<br>
 * GeneratorType.GENERATOR_TYPE_GRAPH;<br>GeneratorType gt = new
 * GeneratorType(aType);</code><br>
 * If a wrong number or no parameter is passed to the constructor the category
 * GENERATOR_TYPE_MORE is chosen.<br>
 * <br>
 * If someone wants to change (add or remove) the Types, the <code>init</code>
 * methods need to be changed!
 * 
 * @author T. Ackermann
 */
public class GeneratorType implements Comparable<GeneratorType> {

	private static final int GENERATOR_TYPE_LOCAL = 0;

	/** Generator Type - Sort */
	public static final int GENERATOR_TYPE_SORT = 1;

	/** Generator Type - Search */
	public static final int GENERATOR_TYPE_SEARCH = 2;

	/** Generator Type - Tree */
	public static final int GENERATOR_TYPE_TREE = 4;

	/** Generator Type - Graph */
	public static final int GENERATOR_TYPE_GRAPH = 8;

	/** Generator Type - Data Structure */
	public static final int GENERATOR_TYPE_DATA_STRUCTURE = 16;

	/** Generator Type - Hashing */
	public static final int GENERATOR_TYPE_HASHING = 32;

	/** Generator Type - Compression */
	public static final int GENERATOR_TYPE_COMPRESSION = 64;

	/** Generator Type - Crypt */
	public static final int GENERATOR_TYPE_CRYPT = 128;

	/** Generator Type - Backtracking */
	public static final int GENERATOR_TYPE_BACKTRACKING = 256;

	/** Generator Type - Backtracking */
	public static final int GENERATOR_TYPE_MATHS = 512;

	/** Generator Type - Backtracking */
	public static final int GENERATOR_TYPE_HARDWARE = 1024;

	/** Generator Type - Networking */
	public static final int GENERATOR_TYPE_NETWORK = 2048;

	/** Generator Type - Graphics */
	public static final int GENERATOR_TYPE_GRAPHICS = 4096;

	/** Generator Type - Interactive */
	public static final int GENERATOR_TYPE_INTERACTIVE = 8192;

	/** Generator Type - More */
	public static final int GENERATOR_TYPE_MORE = 1073741824;

	/** stores all the Generator Types and the according Strings */
	private static TreeMap<Integer, String> mapStrings = new TreeMap<Integer, String>();

	/** stores to all Generator Types the position in the array */
	private static HashMap<Integer, Integer> mapIndex = new HashMap<Integer, Integer>();

	/** stores the Type of this Generator Type */
	private int myType;

	/**
	 * Constructor creates a new GeneratorType-Object.
	 */
	public GeneratorType() {
		this.myType = GENERATOR_TYPE_MORE;
	}

	/**
	 * Constructor creates a new GeneratorType-Object. To create a Generator for a
	 * Sort, Graph and Data-Structure algorithm, call<br>
	 * <code>int aType =<br>GeneratorType.GENERATOR_TYPE_SORT |<br>
	 * GeneratorType.GENERATOR_TYPE_DATA_STRUCTURE |<br>
	 * GeneratorType.GENERATOR_TYPE_GRAPH;<br>GeneratorType gt = new
	 * GeneratorType(aType);</code><br>
	 * If a wrong number or no parameter is passed to the constructor the category
	 * GENERATOR_TYPE_MORE is chosen.
	 * 
	 * @param newType The Generator Type that should be set.
	 */
	public GeneratorType(int newType) {
		setType(newType);
	}

	/**
	 * getNumberOfTypes returns the number of Generator Types.
	 * 
	 * @return The number of Generator Types
	 */
	public static int getNumberOfTypes() {
		init();
		return mapStrings.size();
	}

	/**
	 * getStringForType returns the String for the passed Generator Type. For
	 * example when 128 is passed "Crypt" is returned.
	 * 
	 * @param type The Generator Type as an int.
	 * @return The String for the Generator Type.
	 */
	public static String getStringForType(int type) {
		init();
		String retVal = mapStrings.get(Integer.valueOf(type));
		if (retVal == null)
			return "";
		return retVal;
	}

	/**
	 * getStringForArrayIndex returns the String for the passed Array Index. This is
	 * useful if the have the array returned by <code>getTypes</code>.
	 * 
	 * @param index The yrray index.
	 * @return The String for the array index.
	 */
	public static String getStringForArrayIndex(int index) {
		init();
		// iterate over the HashMap
		for (Integer key : mapIndex.keySet()) {
			if (key.intValue() == index)
				return getStringForType(key.intValue());
		}
//		Iterator<Map.Entry<Integer, Integer>> iter = mapIndex.entrySet().iterator();
//		while (iter.hasNext()) {
//			Map.Entry<Integer, Integer> e = iter.next();
//			if (e.getValue().intValue() == index) {
//				int type = e.getKey().intValue();
//				return getStringForType(type);
//			}
//		}
		return "More";
	}

	/**
	 * getType returns the Generator Type as an int.
	 * 
	 * @return Returns the Generator Type as an int.
	 */
	public int getType() {
		return this.myType;
	}

	/**
	 * setType sets the Generator Type. To set the type for a Generator of a Sort,
	 * Graph and Data-Structure algorithm, call<br>
	 * <code>int aType =<br>GeneratorType.GENERATOR_TYPE_SORT |<br>
	 * GeneratorType.GENERATOR_TYPE_DATA_STRUCTURE |<br>
	 * GeneratorType.GENERATOR_TYPE_GRAPH;<br>aGeneratorType.setType(aType);
	 * </code><br>
	 * If a wrong number or no parameter is passed to the setType method the
	 * category GENERATOR_TYPE_MORE is chosen.
	 * 
	 * @param newType The Generator Type to set as an int.
	 */
	public void setType(int newType) {
		init();

		int storeNewType = 0;
		int typeValue;

		for (Integer key : mapStrings.keySet()) {
			typeValue = key.intValue();
			if ((newType & typeValue) == typeValue)
				storeNewType |= typeValue;
		}
//		Iterator<Map.Entry<Integer, String>> iter = mapStrings.entrySet().iterator();
//		while (iter.hasNext()) {
//			Map.Entry<Integer, String> e = iter.next();
//			typeValue = e.getKey().intValue();
//			if ((newType & typeValue) == typeValue) {
//				storeNewType = storeNewType | typeValue;
//			}
//		}

		// if there was no valid type, then GENERATOR_TYPE_MORE is chosen
		if (storeNewType == 0)
			storeNewType = GENERATOR_TYPE_MORE;

		this.myType = storeNewType;
	}

	/**
	 * getTypes returns an int array with all the Generator Types.
	 * 
	 * @return An int array with all the Generator Types.
	 */
	public static int[] getTypes() {
		init();
		int[] retVal = new int[mapStrings.size()];
		int pos = 0;

		// iterate over the TreeMap
		for (Integer key : mapStrings.keySet())
			retVal[pos++] = key.intValue();
//		Iterator iter = mapStrings.entrySet().iterator();
//		while (iter.hasNext()) {
//			Map.Entry e = (Map.Entry) iter.next();
//			retVal[pos++] = ((Integer) e.getKey()).intValue();
//		}

		return retVal;
	}

	/**
	 * getArrayIndexes returns a Vector with Integers that specify the Positions in
	 * the array that is returned by <code>getTypes</code>. Because a Generator can
	 * have multiple Types we have a Vector instead of a single value.
	 * 
	 * @param type The Generator Type as an int.
	 * @return A Vector with Integers that specify the Positions in the array that
	 *         is returned by <code>getTypes</code>.
	 */
	public static Vector<Integer> getArrayIndexes(int type) {
		Vector<Integer> v = new Vector<Integer>();

		int typeValue;

		for (Integer key : mapStrings.keySet()) {
			typeValue = key.intValue();
			if ((type & typeValue) == typeValue)
				v.add(Integer.valueOf(getArrayIndex(typeValue)));
		}
//		Iterator iter = mapStrings.entrySet().iterator();
//		while (iter.hasNext()) {
//			Map.Entry e = (Map.Entry) iter.next();
//			typeValue = ((Integer) e.getKey()).intValue();
//			if ((type & typeValue) == typeValue) {
//				v.add(Integer.valueOf(getArrayIndex(typeValue)));
//			}
//		}

		if (v.size() == 0)
			v.add(Integer.valueOf(getArrayIndex(GENERATOR_TYPE_MORE)));

		return v;
	}

	/**
	 * getArrayIndex returns the position of type in the array that is returned by
	 * <code>getTypes</code>.
	 * 
	 * @param type The Generator Type as an int.
	 * @return The position in the <code>getTypes</code> array.
	 */
	private static int getArrayIndex(int type) {
		init();
		Integer intObj = mapIndex.get(Integer.valueOf(type));
		if (intObj == null)
			return mapIndex.size() - 1;
		return intObj.intValue();
	}

	/**
	 * init adds all the Generator Types and the according Strings into the TreeMap.
	 * This is only done once.
	 */
	private static void init() {
		if (mapStrings.isEmpty()) {
			if (Animal.localGenerator != null) {
				mapStrings.put(Integer.valueOf(GENERATOR_TYPE_LOCAL), "#Local");
			}
			mapStrings.put(Integer.valueOf(GENERATOR_TYPE_SORT), "Sorting");
			mapStrings.put(Integer.valueOf(GENERATOR_TYPE_SEARCH), "Searching");
			mapStrings.put(Integer.valueOf(GENERATOR_TYPE_TREE), "Tree");
			mapStrings.put(Integer.valueOf(GENERATOR_TYPE_GRAPH), "Graph");
			mapStrings.put(Integer.valueOf(GENERATOR_TYPE_DATA_STRUCTURE), "DataStructures");
			mapStrings.put(Integer.valueOf(GENERATOR_TYPE_HASHING), "Hashing");
			mapStrings.put(Integer.valueOf(GENERATOR_TYPE_COMPRESSION), "Compression");
			mapStrings.put(Integer.valueOf(GENERATOR_TYPE_CRYPT), "Cryptography");
			mapStrings.put(Integer.valueOf(GENERATOR_TYPE_BACKTRACKING), "Backtracking");
			mapStrings.put(Integer.valueOf(GENERATOR_TYPE_MATHS), "Maths");
			mapStrings.put(Integer.valueOf(GENERATOR_TYPE_HARDWARE), "Hardware");
			mapStrings.put(Integer.valueOf(GENERATOR_TYPE_NETWORK), "Network");
			mapStrings.put(Integer.valueOf(GENERATOR_TYPE_GRAPHICS), "Graphics");
			mapStrings.put(Integer.valueOf(GENERATOR_TYPE_INTERACTIVE), "Interactive");
			mapStrings.put(Integer.valueOf(GENERATOR_TYPE_MORE), "More");

			int pos = 0;

			for (Integer key : mapStrings.keySet()) {
				mapIndex.put(key, Integer.valueOf(pos++));
			}
//			// iterate over the TreeMap
//			Iterator iter = mapStrings.entrySet().iterator();
//			while (iter.hasNext()) {
//				Map.Entry e = (Map.Entry) iter.next();
//				mapIndex.put((Integer)e.getKey(), Integer.valueOf(pos++));
//			}
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(GeneratorType type) {
//		if (o instanceof GeneratorType) {
//			if (((GeneratorType) o).getType() == this.myType)
		if (type.getType() == myType)
			return 0; // they are equal
		return -1; // they differ
//		}
//		return Integer.MIN_VALUE; // o is not a GeneratorType
	}

}
