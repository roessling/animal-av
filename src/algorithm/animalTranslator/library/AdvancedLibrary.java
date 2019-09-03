/*
 * Created on 12.11.2004
 *
 */
package algorithm.animalTranslator.library;

import algorithm.animalTranslator.codeItems.Node;
import algorithm.animalTranslator.codeItems.Pos;
import algorithm.animalTranslator.codeItems.TimeOffset;
import algorithm.fileHandler.FileHandler;

/**
 * provides a set of advanced commands to easily create AnimalScript-code
 * 
 * @author Michael Maur <mmaur@web.de>
 */
public abstract class AdvancedLibrary extends Library {

	/**
	 * constructor, that saves the FileHandler the AnimalScript-code shall be sent
	 * to
	 * 
	 * @param fileHandlerToUse
	 *          the FileHandler the AnimalScript-code shall be sent to
	 */
	public AdvancedLibrary(FileHandler fileHandlerToUse) {
		setFileHandler(fileHandlerToUse);
	}

	/**
	 * creates a white rectangle from (5,5) to (5+x,5+y) named "background"
	 * 
	 * @param x
	 *          width of the rectangle (background)
	 * @param y
	 *          height of the rectangle (background)
	 */
	public void advancedCreateWorkSheet(int x, int y) {
		addRectangle("background", new Pos(5, 5), new Pos(5 + x, 5 + y),
				"color white filled fillColor white", null);
	}

	/**
	 * creates a Worksheet at a width of 1200 and a height of 800
	 */
	public void advancedCreateWorkSheet() {
		advancedCreateWorkSheet(1200, 800);
	}

	/**
	 * adds the header to the AnimalScript-file containing version 2.0 and Author
	 * Michael Maur and a custom title
	 * 
	 * @param title
	 *          a String to be used as the title in the AnimalScript-header
	 */
	public void advancedAddHeaderMM(String title) {
		addHeader("2.0", title, "algorithm by Michael Maur <mmaur@web.de>");
	}

	/**
	 * creates an (int-) array in the AnimalScript-animation, thats
	 * cellhighlight-color is set to gray (to be able to reduce the regarded part
	 * of the array) elemHighlight-color is red
	 * 
	 * @param arrayID
	 *          String which will be used as the Arrays Name
	 * @param node
	 *          Node, that determines, where to place the Array
	 * @param theArray
	 *          the Arrays content - an int-array
	 */
	public void advancedArrayReduction(String arrayID, Node node, int[] theArray) {
		addArray(arrayID, node,
				"color blue fillColor white elementColor black elemHighlight red "
						+ "cellHighlight gray horizontal", theArray.length, theArray, null,
				new TimeOffset(0), null);
	}

	/**
	 * creates an (String-) array in the AnimalScript-animation, thats
	 * cellhighlight-color is set to gray (to be able to reduce the regarded part
	 * of the array) elemHighlight-color is red
	 * 
	 * @param arrayID
	 *          String which will be used as the Arrays Name
	 * @param node
	 *          Node, that determines, where to place the Array
	 * @param theArray
	 *          the Arrays content - an int-array
	 */
	public void advancedArrayReduction(String arrayID, Node node,
			String[] theArray) {
		addArray(arrayID, node,
				"color blue fillColor white elementColor black elemHighlight red "
						+ "cellHighlight gray horizontal", theArray.length, theArray, null,
				new TimeOffset(0), null);
	}

	/**
	 * creates an (String-) array in the AnimalScript-animation, thats
	 * cellhighlight-color is set to gray (to be able to reduce the regarded part
	 * of the array) elemHighlight-color is red
	 * 
	 * @param arrayID
	 *          String which will be used as the Arrays Name
	 * @param node
	 *          Node, that determines, where to place the Array
	 * @param theString
	 *          the String content - an int-array
	 */
	public void advancedArrayReduction(String arrayID, Node node, String theString) {
		addArray(arrayID, node,
				"color blue fillColor white elementColor black elemHighlight red "
						+ "cellHighlight gray horizontal", theString.length(), theString,
				null, new TimeOffset(0), null);
	}

	/**
	 * creates an (int-) array in the AnimalScript-animation with
	 * standard-settings cellhighlight-color is yellow, elemHighlight-color is red
	 * 
	 * @param arrayID
	 *          String which will be used as the Arrays Name
	 * @param node
	 *          Node, that determines, where to place the Array
	 * @param theArray
	 *          the Arrays content - an int-array
	 */
	public void advancedArrayStandard(String arrayID, Node node, int[] theArray) {
		addArray(arrayID, node,
				"color blue fillColor white elementColor black elemHighlight red "
						+ "cellHighlight yellow horizontal", theArray.length, theArray,
				null, new TimeOffset(0), null);
	}

	/**
	 * creates an (String-) array in the AnimalScript-animation with
	 * standard-settings cellhighlight-color is yellow, elemHighlight-color is red
	 * 
	 * @param arrayID
	 *          String which will be used as the Arrays Name
	 * @param node
	 *          Node, that determines, where to place the Array
	 * @param theArray
	 *          the Arrays content - an int-array
	 */
	public void advancedArrayStandard(String arrayID, Node node, String[] theArray) {
		addArray(arrayID, node,
				"color blue fillColor white elementColor black elemHighlight red "
						+ "cellHighlight yellow horizontal", theArray.length, theArray,
				null, new TimeOffset(0), null);
	}

	/**
	 * creates an (String-) array in the AnimalScript-animation with
	 * standard-settings cellhighlight-color is yellow, elemHighlight-color is red
	 * 
	 * @param arrayID
	 *          String which will be used as the Arrays Name
	 * @param node
	 *          Node, that determines, where to place the Array
	 * @param theString
	 *          the Arrays content - an int-array
	 */
	public void advancedArrayStandard(String arrayID, Node node, String theString) {
		addArray(arrayID, node,
				"color blue fillColor white elementColor black elemHighlight red "
						+ "cellHighlight yellow horizontal", theString.length(), theString,
				null, new TimeOffset(0), null);
	}

	/**
	 * highlights a range of array-cells in an array in the animation (background
	 * only)
	 * 
	 * @param arrayID
	 *          the id-string of the array, of whome elements shall be
	 *          highlightened (background only)
	 * @param beginIndex
	 *          first element to be highlightened (background only)
	 * @param endIndex
	 *          last element to be hightlightened (background only)
	 */
	public void advancedHighlightArrayCells(String arrayID, int beginIndex,
			int endIndex) {
		if ((beginIndex < 0) || (endIndex < 0)) {
			// exception
		} else if (beginIndex > endIndex) {
			// nothing
		} else {
			compositeStepStart();
			for (int i = beginIndex; i <= endIndex; i++) {
				highlightArrayCell(arrayID, i, null, new TimeOffset(0));
			}
			compositeStepEnd();
		}
	}

	/**
	 * highlights an array-cells in an array in the animation (background only)
	 * 
	 * @param arrayID
	 *          the id-string of the array, of whome an element shall be
	 *          highlightened (background only)
	 * @param index
	 *          element to be highlightened (background only)
	 */
	public void advancedHighlightArrayCell(String arrayID, int index) {
		highlightArrayCell(arrayID, index, null, new TimeOffset(0));
	}

	/**
	 * unhighlights a range of array-cells in an array in the animation
	 * (background only)
	 * 
	 * @param arrayID
	 *          the id-string of the array, of whome elements shall be
	 *          unhighlightened (background only)
	 * @param beginIndex
	 *          first element to be unhighlightened (background only)
	 * @param endIndex
	 *          last element to be unhightlightened (background only)
	 */
	public void advancedUnhighlightArrayCells(String arrayID, int beginIndex,
			int endIndex) {
		if ((beginIndex < 0) || (endIndex < 0)) {
			// exception
		} else if (beginIndex > endIndex) {
			// nothing
		} else {
			compositeStepStart();
			for (int i = beginIndex; i <= endIndex; i++) {
				unhighlightArrayCell(arrayID, i, null, new TimeOffset(0));
			}
			compositeStepEnd();
		}
	}

	/**
	 * unhighlights an array-cells in an array in the animation (background only)
	 * 
	 * @param arrayID
	 *          the id-string of the array, of whome an element shall be
	 *          unhighlightened (background only)
	 * @param index
	 *          element to be unhighlightened (background only)
	 */
	public void advancedUnhighlightArrayCell(String arrayID, int index) {
		unhighlightArrayCell(arrayID, index, null, new TimeOffset(0));
	}

	/**
	 * highlights a range of array-elements in an array in the animation
	 * 
	 * @param arrayID
	 *          the id-string of the array, of whome elements shall be
	 *          highlightened
	 * @param beginIndex
	 *          first element to be highlightened
	 * @param endIndex
	 *          last element to be hightlightened
	 */
	public void advancedHighlightArrayElems(String arrayID, int beginIndex,
			int endIndex) {
		if ((beginIndex < 0) || (endIndex < 0)) {
			// exception
		} else if (beginIndex > endIndex) {
			// nothing
		} else {
			compositeStepStart();
			for (int i = beginIndex; i <= endIndex; i++) {
				highlightArrayElem(arrayID, i, null, new TimeOffset(0));
			}
			compositeStepEnd();
		}
	}

	/**
	 * highlights an array-element in an array in the animation
	 * 
	 * @param arrayID
	 *          the id-string of the array, of whome an element shall be
	 *          highlightened
	 * @param index
	 *          element to be highlightened
	 */
	public void advancedHighlightArrayElem(String arrayID, int index) {
		highlightArrayElem(arrayID, index, null, new TimeOffset(0));
	}

	/**
	 * unhighlights a range of array-elements in an array in the animation
	 * 
	 * @param arrayID
	 *          the id-string of the array, of whome elements shall be
	 *          unhighlightened
	 * @param beginIndex
	 *          first element to be unhighlightened
	 * @param endIndex
	 *          last element to be unhightlightened
	 */
	public void advancedUnhighlightArrayElems(String arrayID, int beginIndex,
			int endIndex) {
		if ((beginIndex < 0) || (endIndex < 0)) {
			// exception
		} else if (beginIndex > endIndex) {
			// nothing
		} else {
			compositeStepStart();
			for (int i = beginIndex; i <= endIndex; i++) {
				unhighlightArrayElem(arrayID, i, null, new TimeOffset(0));
			}
			compositeStepEnd();
		}
	}

	/**
	 * unhighlights an array-element in an array in the animation
	 * 
	 * @param arrayID
	 *          the id-string of the array, of whome an element shall be
	 *          unhighlightened
	 * @param index
	 *          element to be unhighlightened
	 */
	public void advancedUnhighlightArrayElem(String arrayID, int index) {
		unhighlightArrayElem(arrayID, index, null, new TimeOffset(0));
	}

	/**
	 * switches the ArrayElementHighlightning from one element of an array to
	 * another
	 * 
	 * @param arrayID
	 *          the id-string of the array, of whome an element shall be
	 *          unhighlightened
	 * @param toUnhighlight
	 *          the index of the element to be unhighlightened
	 * @param toHighlight
	 *          the index of the element to be highlightened
	 */
	public void advancedHighlightArrayElemSwitch(String arrayID,
			int toUnhighlight, int toHighlight) {
		compositeStepStart();
		advancedUnhighlightArrayElem(arrayID, toUnhighlight);
		advancedHighlightArrayElem(arrayID, toHighlight);
		compositeStepEnd();
	}

	/**
	 * creates a codegroup in the animation containing code given as parameter
	 * 
	 * @param groupID
	 *          the name of the new CodeGroup
	 * @param node
	 *          the position, the new CodeGroup shall be placed to
	 * @param options
	 *          the first possible options-string after the node-definition
	 * @param timeOffset
	 *          a timeOffset for displaying the codeGroup
	 * @param lines
	 *          string-array that contains the lines to be shown in the CodeGroup
	 * @param indentations
	 *          int-array that contains the indentations of the lines in the
	 *          codegroup each line in "lines" has one entry here!!!
	 * @param lineNames
	 *          string-array, that contains lineNames for each line in the
	 *          CodeGroup
	 */
	public void advancedCodeGroupConfig(String groupID, Node node,
			String options, TimeOffset timeOffset, String[] lines,
			int[] indentations, String[] lineNames) {
	  int[] indents = indentations;
	  String[] lineN = lineNames;
		if (lines == null || lines.length == 0) {
			// exception
		}
		try {
			checkID(groupID);
		} catch (Exception ex) {
			System.out.println("Error: " + ex.getMessage());
		}
		boolean initializeIndentations = false;
		boolean initializeLineNames = false;
		if (indents == null) {
			indents = new int[lines.length];
			initializeIndentations = true;
		}
		if (lineN == null) {
			lineN = new String[lines.length];
			initializeLineNames = true;
		}
		for (int i = 0; i < lines.length; i++) {
			// kein setzten der lineN mehr, da dann das highlightning nicht mehr
			// klappt
			if (initializeLineNames) {
				// lineN[i] = groupID + "_" + i;
			}
			if (initializeIndentations) {
				indents[i] = 0;
			}
		}
		if (indents.length != lineN.length
				|| indents.length != lines.length) {
			// exception
		}
		compositeStepStart();
		addCodeGroup(groupID, node, options, timeOffset);
		for (int i = 0; i < lines.length; i++) {
			addCodeLine(lines[i], lineN[i], groupID, indents[i],
					new TimeOffset(0));
		}
		compositeStepEnd();
	}

	/**
	 * creates a codegroup in the animation containing code given as parameter
	 * 
	 * @param groupID
	 *          the name of the new CodeGroup
	 * @param node
	 *          the position, the new CodeGroup shall be placed to
	 * @param options
	 *          the first possible options-string after the node-definition
	 * @param timeOffset
	 *          a timeOffset for displaying the codeGroup
	 * @param lines
	 *          string-array that contains the lines to be shown in the CodeGroup
	 * @param indentations
	 *          int-array that contains the indentations of the lines in the
	 *          codegroup each line in "lines" has one entry here!!!
	 */
	public void advancedCodeGroupConfig(String groupID, Node node,
			String options, TimeOffset timeOffset, String[] lines, int[] indentations) {
		advancedCodeGroupConfig(groupID, node, options, timeOffset, lines,
				indentations, null);
	}

	/**
	 * creates a codegroup in the animation containing code given as parameter
	 * 
	 * @param groupID
	 *          the name of the new CodeGroup
	 * @param node
	 *          the position, the new CodeGroup shall be placed to
	 * @param options
	 *          the first possible options-string after the node-definition
	 * @param timeOffset
	 *          a timeOffset for displaying the codeGroup
	 * @param lines
	 *          string-array that contains the lines to be shown in the CodeGroup
	 */
	public void advancedCodeGroupConfig(String groupID, Node node,
			String options, TimeOffset timeOffset, String[] lines) {
		advancedCodeGroupConfig(groupID, node, options, timeOffset, lines, null);
	}

	/**
	 * creates a codegroup in the animation containing code given as parameter
	 * using standard-options as black text-color and red as highlightcolor and
	 * contextColor
	 * 
	 * @param groupID
	 *          the name of the new CodeGroup
	 * @param node
	 *          the position, the new CodeGroup shall be placed to
	 * @param timeOffset
	 *          a timeOffset for displaying the codeGroup
	 * @param lines
	 *          string-array that contains the lines to be shown in the CodeGroup
	 * @param indentations
	 *          int-array that contains the indentations of the lines in the
	 *          codegroup each line in "lines" has one entry here!!!
	 */
	public void advancedCodeGroupStandard(String groupID, Node node,
			TimeOffset timeOffset, String[] lines, int[] indentations) {
		advancedCodeGroupConfig(groupID, node,
				"color black highlightColor red contextColor red", timeOffset, lines,
				indentations, null);
	}

	/**
	 * creates a codegroup in the animation containing code given as parameter
	 * using standard-options as black text-color and red as highlightcolor and
	 * contextColor
	 * 
	 * @param groupID
	 *          the name of the new CodeGroup
	 * @param node
	 *          the position, the new CodeGroup shall be placed to
	 * @param timeOffset
	 *          a timeOffset for displaying the codeGroup
	 * @param lines
	 *          string-array that contains the lines to be shown in the CodeGroup
	 */
	public void advancedCodeGroupStandard(String groupID, Node node,
			TimeOffset timeOffset, String[] lines) {
		advancedCodeGroupStandard(groupID, node, timeOffset, lines, null);
	}

	/**
	 * creates a CodeGroup with a single line of text - for ex. for headlines,
	 * etc.
	 * 
	 * @param lineID
	 *          a name for the CodeLine
	 * @param node
	 *          the position, the new CodeGroup shall be placed to
	 * @param options
	 *          the options-string to be added after the node-definition in the
	 *          codegroup-definition
	 * @param timeOffset
	 *          a timeOffset for displaying the codeGroup
	 * @param lineContent
	 *          string to be contained in the single line of text
	 */
	public void advancedTextLine(String lineID, Node node, String options,
			TimeOffset timeOffset, String lineContent) {
		try {
			checkID(lineID);
			checkString(lineContent);
		} catch (Exception ex) {
			System.out.println("Error: " + ex.getMessage());
		}
		compositeStepStart();
		addCodeGroup(lineID + "_group", node, options, timeOffset);
		addCodeLine(lineContent, lineID, lineID + "_group", 0, new TimeOffset(0));
		compositeStepEnd();
	}

}
