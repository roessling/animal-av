/*
 * Created on 11.11.2004
 *
 */
package algorithm.animalTranslator.library;

import algorithm.animalTranslator.codeItems.DisplayOptions;
import algorithm.animalTranslator.codeItems.Node;
import algorithm.animalTranslator.codeItems.Timing;
import algorithm.fileHandler.FileHandler;

/**
 * implements basic utilities/methods to deal with strings/stringarrays
 * 
 * @author Michael Maur <mmaur@web.de>
 */
public abstract class Utilities {

	/**
	 * a reference to the filehandler to be used
	 */
	public FileHandler fh;

	/**
	 * sets the fileHandler to be used
	 * 
	 * @param newFileHandler
	 *          the FileHandler to be used
	 */
	void setFileHandler(FileHandler newFileHandler) {
		fh = newFileHandler;
	}

	/**
	 * provides a combined String with id and node for animalscript
	 * 
	 * @param id
	 *          the id to be part of the returned string
	 * @param node
	 *          the node to be part of the returned string
	 * @return a combined string with id and node for animalscript
	 */
	String idp(String id, Node node) throws Exception {
		return idString(id) + nodeString(node);
	}

	/**
	 * provides a string-representation with quotation-marks for the id
	 * 
	 * @param id
	 *          the id to be returned in quotation marks
	 * @return a string-representation of the id in quotation-marks
	 */
	String idString(String id) throws Exception {
		checkID(id);
		return " \"" + id + "\" ";
	}

	/**
	 * provides a string-representation with quotation-marks for a string
	 * 
	 * @param toString
	 *          the string to be returned in quotation marks
	 * @return a string-representation of a string in quotation-marks
	 */
	String asString(String toString) throws Exception {
		checkString(toString);
		return " \"" + toString + "\" ";
	}

	/**
	 * provides a string-representation with quotation-marks for the node (with
	 * check by checkNode)
	 * 
	 * @param node
	 *          the nodeID to be returned in quotation marks
	 * @return a string-representation of the id in quotation-marks
	 */
	String nodeString(Node node) throws Exception {
		checkNode(node);
		return node.getString();
	}

	/**
	 * provides the string-representation of displayoptions
	 * 
	 * @param displayOptions
	 *          the displayoption to be transformed to a string for animalscript
	 * @return the displayoption as string for animalscript
	 */
	String timeString(DisplayOptions displayOptions) {
		if (displayOptions == null) {
			return "";
		} 
		return displayOptions.getString();
	}

	/**
	 * provides the string-representation of timings
	 * 
	 * @param timing
	 *          the timing to be transformed to a string for animalscript
	 * @return the string-representation of the timing in animalscript
	 */
	String timingString(Timing timing) {
		if (timing == null) {
			return "";
		} 
		return timing.getString();
	}

	/**
	 * adds spaces on the left and the right of the passed options-string
	 * 
	 * @param options
	 *          the string to be zoned by spaces on the left&right
	 * @return the passed string with spaces on the left&right
	 */
	String optionsString(String options) {
		if (options == null) {
			return "";
		} 
		return " " + options + " ";
	}

	/**
	 * checks a passed IDString wether it's neither null, nor empty
	 * 
	 * @param id
	 *          String to be checked, wether null or empty
	 * @throws Exception,
	 *           if id null or empty
	 */
	void checkID(String id) throws Exception {
		if ((id == null) || (id == "")) {
			throw new Exception("passed ID-String is null or empty");
		}
	}

	/**
	 * checks a passed Node, wether it's null
	 * 
	 * @param node
	 *          node to be checked
	 * @throws Exception
	 *           if node is null
	 */
	void checkNode(Node node) throws Exception {
		if (node == null) {
			throw new Exception("Node has to be given - null found");
		}
	}

	/**
	 * checks a passed String, wether it's null or empty
	 * 
	 * @param toCheck
	 *          string to be checked
	 * @throws Exception
	 *           if String null or empty
	 */
	void checkString(String toCheck) throws Exception {
		if (toCheck == null) {
			throw new Exception("Checked String null - string required");
		}
		if (toCheck == "") {
			throw new Exception("Checked String empty - not allowed");
		}
	}

	/**
	 * provides a posibillity to create a single string from a string-array - each
	 * of the strings from the array will be set in quotation-marks..
	 * 
	 * @param theArray
	 *          array to be transformed to a quoted string
	 * @param compareLength
	 *          length to compare the arraylength with
	 * @return a string that contains strings from the array (quoted)
	 * @throws Exception
	 *           if array/string in array is null/arraylength zero
	 */
	String createArrayString(String[] theArray, int compareLength)
			throws Exception {
		String collect = " ";
		if (theArray == null) {
			throw new Exception("passed array is Null - not allowed");
		}
		if (theArray.length == 0) {
			throw new Exception("passed array has zero entries - entries required");
		}
		if (theArray.length != compareLength) {
			throw new Exception(
					"passed arrays length is different from passed compareLength");
		}
		for (int i = 0; i < theArray.length; i++) {
			if (theArray[i] == null) {
				throw new Exception("array-entry is null - you shouldn't do this");
			}
			collect += "\"" + theArray[i] + "\" ";
		}
		return collect;
	}

	/**
	 * provides a string representation of a string-array with quotation-marks for
	 * each string
	 * 
	 * @param theArray
	 *          the array to be transformed to a single string with quoted
	 *          substrings
	 * @return a string-representation of quoted strings from the array
	 */
	String createArrayString(String[] theArray) throws Exception {
		if (theArray == null) {
			throw new Exception("passed array is null - not allowed");
		}
		return createArrayString(theArray, theArray.length);
	}

}
