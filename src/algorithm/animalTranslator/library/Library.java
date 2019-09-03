/*
 * Created on 10.11.2004
 *
 */
package algorithm.animalTranslator.library;

import algorithm.animalTranslator.codeItems.DisplayOptions;
import algorithm.animalTranslator.codeItems.Node;
import algorithm.animalTranslator.codeItems.Pos;
import algorithm.animalTranslator.codeItems.TimeOffset;
import algorithm.animalTranslator.codeItems.Timing;

/**
 * provides a set of simple commands to easily create AnimalScript-code
 * 
 * @author Michael Maur <mmaur@web.de>
 */
public abstract class Library extends Utilities {

	/**
	 * int to note, when a parenthesis has to be opened { for a composite step and
	 * when it has to be closed }
	 */
	private int compositeStepsOpened = 0;

	/**
	 * command to send a line of text directly to the fileHandler - it will be
	 * saved that way in the animation-file without changes
	 * 
	 * @param newLine
	 *          String to be sent to the fileHandler
	 */
	public void prosaLine(String newLine) {
		fh.add(newLine);
	}

	/**
	 * command to add a String to the current line in use at the fileHandler
	 * 
	 * @param newFragment
	 *          String to be added to the current line
	 */
	public void prosaFragment(String newFragment) {
		fh.append(newFragment);
	}

	/**
	 * adds a header with version, title and author to the animation
	 * 
	 * @param version
	 *          string that contains the version to be displayed in the header
	 * @param title
	 *          string that contains the title to be displayed in the header
	 * @param author
	 *          string that contains the author to be displayed in the header
	 */
	public void addHeader(String version, String title, String author) {
		addHeaderVersion(version);
		if (title != null) {
			addHeaderTitle(title);
		}
		if (author != null) {
			addHeaderAuthor(author);
		}
	}

	/**
	 * adds a header with version 2.0, title and author to the animation
	 * 
	 * @param title
	 *          string that contains the title to be displayed in the header
	 * @param author
	 *          string that contains the author to be displayed in the header
	 */
	public void addHeaderTA(String title, String author) {
		addHeader("2.0", title, author);
	}

	/**
	 * adds a header with version 2.0 and a title to the animation
	 * 
	 * @param title
	 *          string that contains the title to be displayed in the header
	 */
	public void addHeaderT(String title) {
		addHeader("2.0", title, null);
	}

	/**
	 * adds a header with version 2.0 and an author to the animation
	 * 
	 * @param author
	 *          string that contains the author to be displayed in the header
	 */
	public void addHeaderA(String author) {
		addHeader("2.0", null, author);
	}

	/**
	 * adds the line containing the version to the animation
	 * 
	 * @param version
	 *          the version to be contained in the header (String)
	 */
	private void addHeaderVersion(String version) {
		fh.add("%Animal " + version);
	}

	/**
	 * adds the line containing the title to the animation
	 * 
	 * @param title
	 *          title to be displayed in the animation (string)
	 */
	private void addHeaderTitle(String title) {
		fh.add("title \"" + title + "\"");
	}

	/**
	 * adds the line containing the author to the animation
	 * 
	 * @param author
	 *          author to be displayed in the animation (string)
	 */
	private void addHeaderAuthor(String author) {
		fh.add("author \"" + author + "\"");
	}

	/**
	 * adds a point to the animation
	 * 
	 * @param id
	 *          id-string for the point
	 * @param node
	 *          position, where the point is to be placed
	 * @param options
	 *          options-string to be added in the point-definition after the
	 *          node-definition
	 * @param displayOptions
	 *          displayOptions that determine, when/whether the point is to be
	 *          displayed
	 */
	public void addPoint(String id, Node node, String options,
			DisplayOptions displayOptions) {
		try {
			String txt = "point ";
			txt += idp(id, node);
			txt += optionsString(options);
			txt += timeString(displayOptions);
			fh.add(txt);
		} catch (Exception ex) {
			System.out.println("Error: " + ex.getMessage());
		}
	}

	/**
	 * adds a square to the animation
	 * 
	 * @param id
	 *          id-string for the item
	 * @param node
	 *          position, where the item is to be placed
	 * @param width
	 *          int - width of the square
	 * @param options
	 *          options-string to be added after the width in the square
	 *          definition
	 * @param displayOptions
	 *          displayOptions that determine, when/whether the item is to be
	 *          displayed
	 */
	public void addSquare(String id, Node node, int width, String options,
			DisplayOptions displayOptions) {
		try {
			String txt = "square ";
			txt += idp(id, node);
			txt += " " + width + " ";
			txt += optionsString(options);
			txt += timeString(displayOptions);
			fh.add(txt);
		} catch (Exception ex) {
			System.out.println("Error: " + ex.getMessage());
		}
	}

	/**
	 * adds a rectangle to the animation
	 * 
	 * @param id
	 *          id-string for the item
	 * @param node1
	 *          node that determines the upper left edge of the rectangle
	 * @param node2
	 *          node that determines the lower right edge of the rectangle
	 * @param options
	 *          options-string to be added after the node-definition in the
	 *          rectangle-definition
	 * @param displayOptions
	 *          displayOptions that determine, when/whether the item is to be
	 *          displayed
	 */
	public void addRectangle(String id, Node node1, Node node2, String options,
			DisplayOptions displayOptions) {
		try {
			String txt = "rectangle ";
			txt += idp(id, node1);
			checkNode(node2);
			txt += node2.getString();
			txt += optionsString(options);
			txt += timeString(displayOptions);
			fh.add(txt);
		} catch (Exception ex) {
			System.out.println("Error: " + ex.getMessage());
		}
	}

	/**
	 * adds a relative rectangle to the animation
	 * 
	 * @param id
	 *          id-string for the item
	 * @param node1
	 *          node that determines the upper left edge of the rectangle
	 * @param node2
	 *          node that determines the lower right edge of the rectangle as
	 *          offset from the upper left edge
	 * @param options
	 *          options-string to be added after the node-definition in the
	 *          rectangle-definition
	 * @param displayOptions
	 *          displayOptions that determine, when/whether the item is to be
	 *          displayed
	 */
	public void addRelRectangle(String id, Node node1, Pos node2, String options,
			DisplayOptions displayOptions) {
		try {
			String txt = "relrectangle ";
			txt += idp(id, node1);
			checkNode(node2);
			txt += node2.getString();
			txt += optionsString(options);
			txt += timeString(displayOptions);
			fh.add(txt);
		} catch (Exception ex) {
			System.out.println("Error: " + ex.getMessage());
		}
	}

	/**
	 * adds a triangle to the animation
	 * 
	 * @param id
	 *          id-string for the item
	 * @param node1
	 *          node that the first edge of the triangle
	 * @param node2
	 *          node that the second edge of the triangle
	 * @param node3
	 *          node that the third edge of the triangle
	 * @param options
	 *          options-string to be added after the node-definitions in the
	 *          triangle-definition
	 * @param displayOptions
	 *          displayOptions that determine, when/whether the item is to be
	 *          displayed
	 */
	public void addTriangle(String id, Node node1, Node node2, Node node3,
			String options, DisplayOptions displayOptions) {
		try {
			String txt = "triangle ";
			txt += idp(id, node1);
			checkNode(node2);
			txt += node2.getString();
			checkNode(node3);
			txt += node3.getString();
			txt += optionsString(options);
			txt += timeString(displayOptions);
			fh.add(txt);
		} catch (Exception ex) {
			System.out.println("Error: " + ex.getMessage());
		}
	}

	/**
	 * adds a polygon to the animation
	 * 
	 * @param id
	 *          id-string for the item
	 * @param node1
	 *          first node of the Polygon
	 * @param node2
	 *          second node of the Polygon
	 * @param otherNodes
	 *          node-array, that contains the other nodes of the polygon
	 * @param options
	 *          options-string to be added after the node-definitions in the
	 *          polygon-definition
	 * @param displayOptions
	 *          displayOptions that determine, when/whether the item is to be
	 *          displayed
	 */
	public void addPolygon(String id, Node node1, Node node2, Node[] otherNodes,
			String options, DisplayOptions displayOptions) {
		try {
			String txt = "polygon ";
			txt += idp(id, node1);
			checkNode(node2);
			txt += node2.getString();
			txt += Node.getArrayString(otherNodes);
			txt += optionsString(options);
			txt += timeString(displayOptions);
			fh.add(txt);
		} catch (Exception ex) {
			System.out.println("Error: " + ex.getMessage());
		}
	}

	/**
	 * adds a text-primitive to the animation
	 * 
	 * @param id
	 *          id-string for the item
	 * @param content
	 *          the text to be displayed (as String)
	 * @param node
	 *          node that determines the position, where the text will be shown
	 * @param options
	 *          options-string to be added after the node-definitions in the
	 *          polygon-definition
	 * @param displayOptions
	 *          displayOptions that determine, when/whether the item is to be
	 *          displayed
	 */
	public void addText(String id, String content, Node node, String options,
			DisplayOptions displayOptions) {
		try {
			String txt = "text ";
			txt += idString(id);
			txt += " \"" + content + "\" ";
			txt += nodeString(node);
			txt += optionsString(options);
			txt += timeString(displayOptions);
			fh.add(txt);
		} catch (Exception ex) {
			System.out.println("Error: " + ex.getMessage());
		}
	}

	/**
	 * adds an array to the animation
	 * 
	 * @param id
	 *          id-string for the item
	 * @param node
	 *          node that determines the position, where the item will be shown
	 * @param options1
	 *          options-string to be added after node-definition and in front of
	 *          array-length
	 * @param length
	 *          the length of the array
	 * @param content
	 *          String-Array that contains the elements to be contained in the
	 *          array
	 * @param options2
	 *          2nd options-string to be added after the arrays elements
	 * @param timeOffset
	 *          timeOffset that determines, when the item is to be displayed
	 * @param options3
	 *          3rd options string to be added after the timeOffset-definition
	 */
	public void addArray(String id, Node node, String options1, int length,
			String[] content, String options2, TimeOffset timeOffset, String options3) {
		try {
			String txt = "array ";
			txt += idp(id, node);
			txt += optionsString(options1);
			txt += "length " + length;
			txt += createArrayString(content, length);
			txt += optionsString(options2);
			txt += timeString(timeOffset);
			txt += optionsString(options3);
			fh.add(txt);
		} catch (Exception ex) {
			System.out.println("Error: " + ex.getMessage());
		}
	}

	/**
	 * adds an array to the animation
	 * 
	 * @param id
	 *          id-string for the item
	 * @param node
	 *          node that determines the position, where the item will be shown
	 * @param options1
	 *          options-string to be added after node-definition and in front of
	 *          array-length
	 * @param length
	 *          the length of the array
	 * @param content
	 *          String-Array that contains the elements to be contained in the
	 *          array
	 * @param options2
	 *          2nd options-string to be added after the arrays elements
	 * @param timeOffset
	 *          timeOffset that determines, when the item is to be displayed
	 * @param options3
	 *          3rd options string to be added after the timeOffset-definition
	 */
	public void addField(String id, Node node, String options1, int length,
			String[] content, String options2, TimeOffset timeOffset, String options3) {
		addArray(id, node, options1, length, content, options2, timeOffset,
				options3);
	}

	/**
	 * adds an array to the animation
	 * 
	 * @param id
	 *          id-string for the item
	 * @param node
	 *          node that determines the position, where the item will be shown
	 * @param options1
	 *          options-string to be added after node-definition and in front of
	 *          array-length
	 * @param length
	 *          the length of the array
	 * @param content
	 *          int-Array that contains the elements to be contained in the array
	 * @param options2
	 *          2nd options-string to be added after the arrays elements
	 * @param timeOffset
	 *          timeOffset that determines, when the item is to be displayed
	 * @param options3
	 *          3rd options string to be added after the timeOffset-definition
	 */
	public void addArray(String id, Node node, String options1, int length,
			int[] content, String options2, TimeOffset timeOffset, String options3) {
		String[] sArray = new String[content.length];
		for (int i = 0; i < content.length; i++) {
			sArray[i] = Integer.toString(content[i]);
		}
		addArray(id, node, options1, length, sArray, options2, timeOffset, options3);
	}

	/**
	 * adds an array to the animation
	 * 
	 * @param id
	 *          id-string for the item
	 * @param node
	 *          node that determines the position, where the item will be shown
	 * @param options1
	 *          options-string to be added after node-definition and in front of
	 *          array-length
	 * @param length
	 *          the length of the array
	 * @param content
	 *          String to be contained in the array
	 * @param options2
	 *          2nd options-string to be added after the arrays elements
	 * @param timeOffset
	 *          timeOffset that determines, when the item is to be displayed
	 * @param options3
	 *          3rd options string to be added after the timeOffset-definition
	 */
	public void addArray(String id, Node node, String options1, int length,
			String content, String options2, TimeOffset timeOffset, String options3) {
		String[] sArray = new String[content.length()];
		for (int i = 0; i < content.length(); i++) {
			sArray[i] = content.substring(i, i + 1);
		}
		addArray(id, node, options1, length, sArray, options2, timeOffset, options3);
	}

	/**
	 * adds an array to the animation
	 * 
	 * @param id
	 *          id-string for the item
	 * @param node
	 *          node that determines the position, where the item will be shown
	 * @param options1
	 *          options-string to be added after node-definition and in front of
	 *          array-length
	 * @param length
	 *          the length of the array
	 * @param content
	 *          int-Array that contains the elements to be contained in the array
	 * @param options2
	 *          2nd options-string to be added after the arrays elements
	 * @param timeOffset
	 *          timeOffset that determines, when the item is to be displayed
	 * @param options3
	 *          3rd options string to be added after the timeOffset-definition
	 */
	public void addField(String id, Node node, String options1, int length,
			int[] content, String options2, TimeOffset timeOffset, String options3) {
		addArray(id, node, options1, length, content, options2, timeOffset,
				options3);
	}

	/**
	 * adds an array-marker to an array in the animation
	 * 
	 * @param markerID
	 *          id-string for the item
	 * @param arrayID
	 *          id-string of the array, the marker will be added to
	 * @param atIndex
	 *          index, where the marker will be added
	 * @param options
	 *          options-string to be added after the index
	 * @param displayOptions
	 *          displayOptions that determine, when/whether the item is to be
	 *          displayed
	 */
	public void addArrayMarker(String markerID, String arrayID, int atIndex,
			String options, DisplayOptions displayOptions) {
		String txt = "arrayMarker ";
		if ((markerID == null) || (arrayID == null)) {
			// exception
		}
		txt += " \"" + markerID + "\" on ";
		txt += "\"" + arrayID + "\" atIndex " + atIndex + " ";
		txt += optionsString(options);
		txt += timeString(displayOptions);
		fh.add(txt);
	}

	/**
	 * adds an array-marker to an array in the animation
	 * 
	 * @param markerID
	 *          id-string for the item
	 * @param arrayID
	 *          id-string of the array, the marker will be added to
	 * @param atIndex
	 *          index, where the marker will be added
	 * @param options
	 *          options-string to be added after the index
	 * @param displayOptions
	 *          displayOptions that determine, when/whether the item is to be
	 *          displayed
	 */
	public void addArrayPointer(String markerID, String arrayID, int atIndex,
			String options, DisplayOptions displayOptions) {
		addArrayMarker(markerID, arrayID, atIndex, options, displayOptions);
	}

	/**
	 * adds an array-marker to an array in the animation
	 * 
	 * @param markerID
	 *          id-string for the item
	 * @param arrayID
	 *          id-string of the array, the marker will be added to
	 * @param atIndex
	 *          index, where the marker will be added
	 * @param options
	 *          options-string to be added after the index
	 * @param displayOptions
	 *          displayOptions that determine, when/whether the item is to be
	 *          displayed
	 */
	public void addArrayIndex(String markerID, String arrayID, int atIndex,
			String options, DisplayOptions displayOptions) {
		addArrayMarker(markerID, arrayID, atIndex, options, displayOptions);
	}

	/**
	 * sets an entry at an array in the animation
	 * 
	 * @param value
	 *          the value to be put to the array
	 * @param arrayName
	 *          id-string of the array
	 * @param targetPos
	 *          index, where value will be put
	 * @param timing
	 *          timing, to determine when the value is to be put to the array
	 */
	public void arrayPut(String value, String arrayName, int targetPos,
			Timing timing) {
		try {
			String txt = "arrayPut ";
			checkString(value);
			checkString(arrayName);
			txt += "\"" + value + "\" on \"" + arrayName + "\" position " + targetPos
					+ " ";
			txt += timingString(timing);
			fh.add(txt);
		} catch (Exception ex) {
			System.out.println("Error: " + ex.getMessage());
		}
	}

	/**
	 * swaps two values in an array in the animation
	 * 
	 * @param arrayName
	 *          id-string of the array
	 * @param pos1
	 *          first index to be swappend
	 * @param pos2
	 *          second index to be swapped
	 * @param timing
	 *          timing, to determine when the values are to be swapped
	 */
	public void arraySwap(String arrayName, int pos1, int pos2, Timing timing) {
		try {
			String txt = "arraySwap on ";
			checkString(arrayName);
			txt += "\"" + arrayName + "\" position " + pos1 + " with " + pos2 + " ";
			txt += timingString(timing);
			fh.add(txt);
		} catch (Exception ex) {
			System.out.println("Error: " + ex.getMessage());
		}
	}

	/**
	 * moves an arraymarker at an array in the animation
	 * 
	 * @param markerID
	 *          id-string of the arraymarker
	 * @param toPos
	 *          target-index for the arraymarker
	 * @param timing
	 *          timing to determine, when the array-marker will move
	 */
	public void moveArrayMarker(String markerID, int toPos, Timing timing) {
		try {
			String txt = "moveArrayMarker ";
			checkString(markerID);
			txt += "\"" + markerID + "\" to position " + toPos + " ";
			txt += timingString(timing);
			fh.add(txt);
		} catch (Exception ex) {
			System.out.println("Error: " + ex.getMessage());
		}
	}

	/**
	 * moves an arraymarker at an array in the animation
	 * 
	 * @param markerID
	 *          id-string of the arraymarker
	 * @param toPos
	 *          target-index for the arraymarker
	 * @param timing
	 *          timing to determine, when the array-marker will move
	 */
	public void moveArrayIndex(String markerID, int toPos, Timing timing) {
		moveArrayMarker(markerID, toPos, timing);
	}

	/**
	 * moves an arraymarker at an array in the animation
	 * 
	 * @param markerID
	 *          id-string of the arraymarker
	 * @param toPos
	 *          target-index for the arraymarker
	 * @param timing
	 *          timing to determine, when the array-marker will move
	 */
	public void moveArrayPointer(String markerID, int toPos, Timing timing) {
		moveArrayMarker(markerID, toPos, timing);
	}

	/**
	 * moves an arraymarker at an array in the animation
	 * 
	 * @param markerID
	 *          id-string of the arraymarker
	 * @param toPos
	 *          target-index for the arraymarker
	 * @param timing
	 *          timing to determine, when the array-marker will move
	 */
	public void moveIndex(String markerID, int toPos, Timing timing) {
		moveArrayMarker(markerID, toPos, timing);
	}

	/**
	 * moves an arraymarker at an array in the animation
	 * 
	 * @param markerID
	 *          id-string of the arraymarker
	 * @param toPos
	 *          target-index for the arraymarker
	 * @param timing
	 *          timing to determine, when the array-marker will move
	 */
	public void moveMarker(String markerID, int toPos, Timing timing) {
		moveArrayMarker(markerID, toPos, timing);
	}

	/**
	 * moves an arraymarker at an array in the animation
	 * 
	 * @param markerID
	 *          id-string of the arraymarker
	 * @param toPos
	 *          target-index for the arraymarker
	 * @param timing
	 *          timing to determine, when the array-marker will move
	 */
	public void movePointer(String markerID, int toPos, Timing timing) {
		moveArrayMarker(markerID, toPos, timing);
	}

	/**
	 * moves an arraymarker at an array in the animation
	 * 
	 * @param markerID
	 *          id-string of the arraymarker
	 * @param toPos
	 *          target-index for the arraymarker
	 * @param timing
	 *          timing to determine, when the array-marker will move
	 */
	public void jumpArrayIndex(String markerID, int toPos,
			Timing timing) {
		moveArrayMarker(markerID, toPos, new TimeOffset(0));
	}

	/**
	 * moves an arraymarker at an array in the animation
	 * 
	 * @param markerID
	 *          id-string of the arraymarker
	 * @param toPos
	 *          target-index for the arraymarker
	 * @param timing
	 *          timing to determine, when the array-marker will move
	 */
	public void jumpArrayMarker(String markerID, int toPos,
			Timing timing) {
		moveArrayMarker(markerID, toPos, new TimeOffset(0));
	}

	/**
	 * moves an arraymarker at an array in the animation
	 * 
	 * @param markerID
	 *          id-string of the arraymarker
	 * @param toPos
	 *          target-index for the arraymarker
	 * @param timing
	 *          timing to determine, when the array-marker will move
	 */
	public void jumpArrayPointer(String markerID, int toPos,
			Timing timing) {
		moveArrayMarker(markerID, toPos, new TimeOffset(0));
	}

	/**
	 * moves an arraymarker at an array in the animation
	 * 
	 * @param markerID
	 *          id-string of the arraymarker
	 * @param toPos
	 *          target-index for the arraymarker
	 * @param timing
	 *          timing to determine, when the array-marker will move
	 */
	public void jumpIndex(String markerID, int toPos, Timing timing) {
		moveArrayMarker(markerID, toPos, new TimeOffset(0));
	}

	/**
	 * moves an arraymarker at an array in the animation
	 * 
	 * @param markerID
	 *          id-string of the arraymarker
	 * @param toPos
	 *          target-index for the arraymarker
	 * @param timing
	 *          timing to determine, when the array-marker will move
	 */
	public void jumpMarker(String markerID, int toPos,
			Timing timing) {
		moveArrayMarker(markerID, toPos, new TimeOffset(0));
	}

	/**
	 * moves an arraymarker at an array in the animation
	 * 
	 * @param markerID
	 *          id-string of the arraymarker
	 * @param toPos
	 *          target-index for the arraymarker
	 * @param timing
	 *          timing to determine, when the array-marker will move
	 */
	public void jumpPointer(String markerID, int toPos,
			Timing timing) {
		moveArrayMarker(markerID, toPos, new TimeOffset(0));
	}

	/**
	 * moves an arraymarker at an array in the animation
	 * 
	 * @param markerID
	 *          id-string of the arraymarker
	 * @param toPlace
	 *          string to determine the place the arraymarker will move to
	 * @param timing
	 *          timing to determine, when the array-marker will move
	 */
	public void moveArrayMarker(String markerID, String toPlace, Timing timing) {
		try {
			String txt = "moveArrayMarker ";
			checkString(markerID);
			txt += "\"" + markerID + "\" to " + toPlace + " ";
			txt += timingString(timing);
			fh.add(txt);
		} catch (Exception ex) {
			System.out.println("Error: " + ex.getMessage());
		}
	}

	/**
	 * moves an arraymarker at an array in the animation
	 * 
	 * @param markerID
	 *          id-string of the arraymarker
	 * @param toPlace
	 *          string to determine the place the arraymarker will move to
	 * @param timing
	 *          timing to determine, when the array-marker will move
	 */
	public void moveArrayIndex(String markerID, String toPlace, Timing timing) {
		moveArrayMarker(markerID, toPlace, timing);
	}

	/**
	 * moves an arraymarker at an array in the animation
	 * 
	 * @param markerID
	 *          id-string of the arraymarker
	 * @param toPlace
	 *          string to determine the place the arraymarker will move to
	 * @param timing
	 *          timing to determine, when the array-marker will move
	 */
	public void moveArrayPointer(String markerID, String toPlace, Timing timing) {
		moveArrayMarker(markerID, toPlace, timing);
	}

	/**
	 * moves an arraymarker at an array in the animation
	 * 
	 * @param markerID
	 *          id-string of the arraymarker
	 * @param toPlace
	 *          string to determine the place the arraymarker will move to
	 * @param timing
	 *          timing to determine, when the array-marker will move
	 */
	public void moveIndex(String markerID, String toPlace, Timing timing) {
		moveArrayMarker(markerID, toPlace, timing);
	}

	/**
	 * moves an arraymarker at an array in the animation
	 * 
	 * @param markerID
	 *          id-string of the arraymarker
	 * @param toPlace
	 *          string to determine the place the arraymarker will move to
	 * @param timing
	 *          timing to determine, when the array-marker will move
	 */
	public void moveMarker(String markerID, String toPlace, Timing timing) {
		moveArrayMarker(markerID, toPlace, timing);
	}

	/**
	 * moves an arraymarker at an array in the animation
	 * 
	 * @param markerID
	 *          id-string of the arraymarker
	 * @param toPlace
	 *          string to determine the place the arraymarker will move to
	 * @param timing
	 *          timing to determine, when the array-marker will move
	 */
	public void movePointer(String markerID, String toPlace, Timing timing) {
		moveArrayMarker(markerID, toPlace, timing);
	}

	/**
	 * moves an arraymarker at an array in the animation
	 * 
	 * @param markerID
	 *          id-string of the arraymarker
	 * @param toPlace
	 *          string to determine the place the arraymarker will move to
	 * @param timing
	 *          timing to determine, when the array-marker will move
	 */
	public void jumpArrayIndex(String markerID, String toPlace,
			Timing timing) {
		moveArrayMarker(markerID, toPlace, new TimeOffset(0));
	}

	/**
	 * moves an arraymarker at an array in the animation
	 * 
	 * @param markerID
	 *          id-string of the arraymarker
	 * @param toPlace
	 *          string to determine the place the arraymarker will move to
	 * @param timing
	 *          timing to determine, when the array-marker will move
	 */
	public void jumpArrayMarker(String markerID, String toPlace,
			Timing timing) {
		moveArrayMarker(markerID, toPlace, new TimeOffset(0));
	}

	/**
	 * moves an arraymarker at an array in the animation
	 * 
	 * @param markerID
	 *          id-string of the arraymarker
	 * @param toPlace
	 *          string to determine the place the arraymarker will move to
	 * @param timing
	 *          timing to determine, when the array-marker will move
	 */
	public void jumpArrayPointer(String markerID, String toPlace,
			Timing timing) {
		moveArrayMarker(markerID, toPlace, new TimeOffset(0));
	}

	/**
	 * moves an arraymarker at an array in the animation
	 * 
	 * @param markerID
	 *          id-string of the arraymarker
	 * @param toPlace
	 *          string to determine the place the arraymarker will move to
	 * @param timing
	 *          timing to determine, when the array-marker will move
	 */
	public void jumpIndex(String markerID, String toPlace,
			Timing timing) {
		moveArrayMarker(markerID, toPlace, new TimeOffset(0));
	}

	/**
	 * moves an arraymarker at an array in the animation
	 * 
	 * @param markerID
	 *          id-string of the arraymarker
	 * @param toPlace
	 *          string to determine the place the arraymarker will move to
	 * @param timing
	 *          timing to determine, when the array-marker will move
	 */
	public void jumpMarker(String markerID, String toPlace,
			Timing timing) {
		moveArrayMarker(markerID, toPlace, new TimeOffset(0));
	}

	/**
	 * moves an arraymarker at an array in the animation
	 * 
	 * @param markerID
	 *          id-string of the arraymarker
	 * @param toPlace
	 *          string to determine the place the arraymarker will move to
	 * @param timing
	 *          timing to determine, when the array-marker will move
	 */
	public void jumpPointer(String markerID, String toPlace,
			Timing timing) {
		moveArrayMarker(markerID, toPlace, new TimeOffset(0));
	}

	/**
	 * changes the colors of objects in the animation
	 * 
	 * @param oIDs
	 *          string-array, containing the OIDs of the elements, whose colors
	 *          shall be changed
	 * @param options
	 *          options to be added after the OIDs
	 * @param colorName
	 *          string containing the name of the new color
	 * @param timing
	 *          timing to determine, when the color shall be changed
	 */
	public void colorChange(String[] oIDs, String options, String colorName,
			Timing timing) {
		try {
			String txt = "color ";
			txt += createArrayString(oIDs);
			txt += optionsString(options);
			checkString(colorName);
			txt += optionsString(colorName);
			txt += timingString(timing);
			fh.add(txt);
		} catch (Exception ex) {
			System.out.println("Error: " + ex.getMessage());
		}
	}

	/**
	 * causes the animation to move on to the next step after a certain delay
	 * 
	 * @param msDelay
	 *          delay in ms
	 */
	public void delay(int msDelay) {
		fh.add("delay " + msDelay + " ms ");
	}

	/**
	 * starts a composite Step in the animation
	 * 
	 */
	public void compositeStepStart() {
		compositeStepsOpened++;
		if (compositeStepsOpened == 1) {
			fh.add("{");
		}
	}

	/**
	 * ends a composite step in the animation
	 * 
	 */
	public void compositeStepEnd() {
		if (compositeStepsOpened > 0) {
			compositeStepsOpened--;
		}
		if (compositeStepsOpened == 0) {
			fh.add("}");
		}
	}

	/**
	 * starts a composite step in the animation
	 * 
	 */
	public void cSS() {
		compositeStepStart();
	}

	/**
	 * ends a composite step in the animation
	 * 
	 */
	public void cSE() {
		compositeStepEnd();
	}

	/**
	 * adds a comment to the anmimation
	 * 
	 * @param theComment
	 *          comment to be added
	 */
	public void comment(String theComment) {
		fh.add("# " + theComment);
	}

	/**
	 * add a codegroup to the animation
	 * 
	 * @param id
	 *          id-string for the item
	 * @param node
	 *          node, where the item will be displayed
	 * @param options
	 *          options to be added after the node-definition
	 * @param timeOffset
	 *          timeOffset, to determine, when the array will be shown
	 */
	public void addCodeGroup(String id, Node node, String options,
			TimeOffset timeOffset) {
		try {
			String txt = "codegroup ";
			txt += idp(id, node);
			txt += optionsString(options);
			txt += timeString(timeOffset);
			fh.add(txt);
		} catch (Exception ex) {
			System.out.println("Error: " + ex.getMessage());
		}
	}

	/**
	 * adds a code-line to a code-group
	 * 
	 * @param theCode
	 *          code-line to be added
	 * @param id
	 *          id-string for the new code-line
	 * @param codeGroupID
	 *          id-string of the code-group
	 * @param indentation
	 *          indentation for the new codeline (int)
	 * @param timeOffset
	 *          timeOffset, to determine, when the new codeline will be added
	 */
	public void addCodeLine(String theCode, String id, String codeGroupID,
			int indentation, TimeOffset timeOffset) {
		try {
			String txt = "addCodeLine ";
			txt += asString(theCode);
			if (id != null)
				txt += " name " + idString(id);
			txt += " to " + asString(codeGroupID) + " indentation " + indentation
					+ " ";
			txt += timeString(timeOffset);
			fh.add(txt);
		} catch (Exception ex) {
			System.out.println("Error: " + ex.getMessage());
		}
	}

	/**
	 * adds a new codeline to the current codeline in a certain codegroup
	 * 
	 * @param theCode
	 *          the code-element to be added
	 * @param id
	 *          id-string for the new code-element
	 * @param codeGroupID
	 *          ids-string of the code-group
	 * @param positionInRow
	 *          the index of the new element in the current code-line
	 * @param indentation
	 *          indentation for the new code-element
	 * @param timeOffset
	 *          timeOffset, to determine, when the new codeelement will be added
	 */
	public void addCodeElem(String theCode, String id, String codeGroupID,
			int positionInRow, int indentation, TimeOffset timeOffset) {
		try {
			String txt = "addCodeElem ";
			txt += asString(theCode);
			if (id != null)
				txt += " name " + idString(id);
			txt += " to " + asString(codeGroupID) + "\" row " + positionInRow
					+ " indentation " + indentation + " ";
			txt += timeString(timeOffset);
			fh.add(txt);
		} catch (Exception ex) {
			System.out.println("Error: " + ex.getMessage());
		}
	}

	/**
	 * adds a new codeline to the current codeline in a certain codegroup
	 * 
	 * @param theCode
	 *          the code-element to be added
	 * @param id
	 *          id-string for the new code-element
	 * @param codeGroupID
	 *          ids-string of the code-group
	 * @param indentation
	 *          indentation for the new code-element
	 * @param timeOffset
	 *          timeOffset, to determine, when the new codeelement will be added
	 */
	public void addCodeElem(String theCode, String id, String codeGroupID,
			int indentation, TimeOffset timeOffset) {
		try {
			String txt = "addCodeElem ";
			txt += asString(theCode);
			if (id != null)
				txt += " name " + idString(id);
			txt += " to " + asString(codeGroupID) + " indentation " + indentation
					+ " ";
			txt += timeString(timeOffset);
			fh.add(txt);
		} catch (Exception ex) {
			System.out.println("Error: " + ex.getMessage());
		}
	}

	/**
	 * hides code
	 * 
	 * @param id
	 *          id-string of the code to be hidden
	 * @param timeOffset
	 *          timeOffset to determine, when the code will be hidden
	 */
	public void codeHide(String id, TimeOffset timeOffset) {
		try {
			String txt = "hideCode ";
			txt += idString(id);
			txt += timeString(timeOffset);
			fh.add(txt);
		} catch (Exception ex) {
			System.out.println("Error: " + ex.getMessage());
		}
	}

	/**
	 * hides code
	 * 
	 * @param id
	 *          id-string of the code to be hidden
	 * @param timeOffset
	 *          timeOffset to determine, when the code will be hidden
	 */
	public void hideCode(String id, TimeOffset timeOffset) {
		codeHide(id, timeOffset);
	}

	/**
	 * hides objects in the animation
	 * 
	 * @param id
	 *          id-string of the object to be hidden
	 * @param timeOffset
	 *          timeOffset to determine, when the items will be hidden
	 */
	public void hide(String id, TimeOffset timeOffset) {
		try {
			String txt = "hide ";
			txt += idString(id);
			txt += timeString(timeOffset);
			fh.add(txt);
		} catch (Exception ex) {
			System.out.println("Error: " + ex.getMessage());
		}
	}

	/**
	 * hides objects in the animation
	 * 
	 * @param ids
	 *          string array with id-strings of the objects to be hidden
	 * @param timeOffset
	 *          timeOffset to determine, when the items will be hidden
	 */
	public void hide(String[] ids, TimeOffset timeOffset) {
		try {
			String txt = "hide ";
			txt += createArrayString(ids);
			txt += timeString(timeOffset);
			fh.add(txt);
		} catch (Exception ex) {
			System.out.println("Error: " + ex.getMessage());
		}
	}

	/**
	 * shows a previously hidden item
	 * 
	 * @param id
	 *          id-string of the item
	 * @param timeOffset
	 *          timeOffset to determine, when the item will be shown
	 */
	public void show(String id, TimeOffset timeOffset) {
		try {
			String txt = "show ";
			txt += idString(id);
			txt += timeString(timeOffset);
			fh.add(txt);
		} catch (Exception ex) {
			System.out.println("Error: " + ex.getMessage());
		}
	}

	/**
	 * shows previously hidden items
	 * 
	 * @param ids
	 *          string-array with the id-string of the items
	 * @param timeOffset
	 *          timeOffset to determine, when the items will be shown
	 */
	public void show(String[] ids, TimeOffset timeOffset) {
		try {
			String txt = "show ";
			txt += createArrayString(ids);
			txt += timeString(timeOffset);
			fh.add(txt);
		} catch (Exception ex) {
			System.out.println("Error: " + ex.getMessage());
		}
	}

	/**
	 * add a label to the animation
	 * 
	 * @param labelEntry
	 *          the string to be displayed as label
	 */
	public void addLabel(String labelEntry) {
		try {
			String txt = "label ";
			txt += asString(labelEntry);
			fh.add(txt);
		} catch (Exception ex) {
			System.out.println("Error: " + ex.getMessage());
		}
	}

	/**
	 * echoes the location of a node
	 * 
	 * @param node
	 *          node, whose location will be echoed to the screen
	 */
	public void echoLocation(Node node) {
		try {
			String txt = "echo location : ";
			txt += nodeString(node);
			fh.add(txt);
		} catch (Exception ex) {
			System.out.println("Error: " + ex.getMessage());
		}
	}

	/**
	 * determines the bounding box for items
	 * 
	 * @param ids
	 *          array with id-strings of the items, whose bounding box will be
	 *          determined
	 */
	public void echoBoundingBox(String[] ids) {
		try {
			String txt = "echo boundingBox : ";
			txt += createArrayString(ids);
			fh.add(txt);
		} catch (Exception ex) {
			System.out.println("Error: " + ex.getMessage());
		}
	}

	/**
	 * determines the bounding box for items
	 * 
	 * @param ids
	 *          array with id-strings of the items, whose bounding box will be
	 *          determined
	 */
	public void echoBounds(String[] ids) {
		echoBoundingBox(ids);
	}

	/**
	 * echoes text to the animation
	 * 
	 * @param textToEcho
	 *          String to be echoed
	 */
	public void echoText(String textToEcho) {
		try {
			String txt = "echo text : ";
			txt += asString(textToEcho);
			fh.add(txt);
		} catch (Exception ex) {
			System.out.println("Error: " + ex.getMessage());
		}
	}

	/**
	 * echoes text to the animation
	 * 
	 * @param textToEcho
	 *          String to be echoed
	 */
	public void echo(String textToEcho) {
		echoText(textToEcho);
	}

	/**
	 * swaps the oIDs of two items in the animation
	 * 
	 * @param oID1
	 *          first oID to be swapped
	 * @param oID2
	 *          second oID to be swapped
	 */
	public void swap(String oID1, String oID2) {
		try {
			String txt = "swap ";
			txt += asString(oID1) + asString(oID2);
			fh.add(txt);
		} catch (Exception ex) {
			System.out.println("Error: " + ex.getMessage());
		}
	}

	/**
	 * swaps the oIDs of two items in the animation
	 * 
	 * @param oID1
	 *          first oID to be swapped
	 * @param oID2
	 *          second oID to be swapped
	 */
	public void exchange(String oID1, String oID2) {
		swap(oID1, oID2);
	}

	/**
	 * highlights an array-element in the animation (the characters)
	 * 
	 * @param arrayID
	 *          id-string of the array
	 * @param position
	 *          index of the element to be hightlightened
	 * @param options
	 *          options-string to be added after the position
	 * @param timing
	 *          timing to determine, when the highlightning will take place
	 */
	public void highlightArrayElem(String arrayID, int position, String options,
			Timing timing) {
		try {
			String txt = "highlightArrayElem on ";
			txt += idString(arrayID) + " position " + position + " ";
			txt += optionsString(options);
			txt += timingString(timing);
			fh.add(txt);
		} catch (Exception ex) {
			System.out.println("Error: " + ex.getMessage());
		}
	}

	/**
	 * highlights an array-cell in the animation (the background)
	 * 
	 * @param arrayID
	 *          id-string of the array
	 * @param position
	 *          index of the cell to be hightlightened
	 * @param options
	 *          options-string to be added after the position
	 * @param timing
	 *          timing to determine, when the highlightning will take place
	 */
	public void highlightArrayCell(String arrayID, int position, String options,
			Timing timing) {
		try {
			String txt = "highlightArrayCell on ";
			txt += idString(arrayID) + " position " + position + " ";
			txt += optionsString(options);
			txt += timingString(timing);
			fh.add(txt);
		} catch (Exception ex) {
			System.out.println("Error: " + ex.getMessage());
		}
	}

	/**
	 * unhighlights an array-element in the animation (the characters)
	 * 
	 * @param arrayID
	 *          id-string of the array
	 * @param position
	 *          index of the element to be unhightlightened
	 * @param options
	 *          options-string to be added after the position
	 * @param timing
	 *          timing to determine, when the unhighlightning will take place
	 */
	public void unhighlightArrayElem(String arrayID, int position,
			String options, Timing timing) {
		try {
			String txt = "unhighlightArrayElem on ";
			txt += idString(arrayID) + " position " + position + " ";
			txt += optionsString(options);
			txt += timingString(timing);
			fh.add(txt);
		} catch (Exception ex) {
			System.out.println("Error: " + ex.getMessage());
		}
	}

	/**
	 * unhighlights an array-cell in the animation (the background)
	 * 
	 * @param arrayID
	 *          id-string of the array
	 * @param position
	 *          index of the cell to be unhightlightened
	 * @param options
	 *          options-string to be added after the position
	 * @param timing
	 *          timing to determine, when the unhighlightning will take place
	 */
	public void unhighlightArrayCell(String arrayID, int position,
			String options, Timing timing) {
		try {
			String txt = "unhighlightArrayCell on ";
			txt += idString(arrayID) + " position " + position + " ";
			txt += optionsString(options);
			txt += timingString(timing);
			fh.add(txt);
		} catch (Exception ex) {
			System.out.println("Error: " + ex.getMessage());
		}
	}

	/**
	 * highlights code in a codegroup in the animation
	 * 
	 * @param baseCodeGroup
	 *          id-string of the codegroup
	 * @param lineNr
	 *          int - lineNr of the line to be highlightened
	 * @param options
	 *          options-string to be added after the line-nr
	 * @param timing
	 *          timing to determine, when the highlightning will take place
	 */
	public void highlightCode(String baseCodeGroup, int lineNr, String options,
			Timing timing) {
		try {
			String txt = "highlightCode on ";
			txt += idString(baseCodeGroup) + " line " + lineNr + " ";
			txt += optionsString(options);
			txt += timingString(timing);
			fh.add(txt);
		} catch (Exception ex) {
			System.out.println("Error: " + ex.getMessage());
		}
	}

	/**
	 * unhighlights code in a codegroup in the animation
	 * 
	 * @param baseCodeGroup
	 *          id-string of the codegroup
	 * @param lineNr
	 *          int - lineNr of the line to be unhighlightened
	 * @param options
	 *          options-string to be added after the line-nr
	 * @param timing
	 *          timing to determine, when the unhighlightning will take place
	 */
	public void unhighlightCode(String baseCodeGroup, int lineNr, String options,
			Timing timing) {
		try {
			String txt = "unhighlightCode on ";
			txt += idString(baseCodeGroup) + " line " + lineNr + " ";
			txt += optionsString(options);
			txt += timingString(timing);
			fh.add(txt);
		} catch (Exception ex) {
			System.out.println("Error: " + ex.getMessage());
		}
	}

}
