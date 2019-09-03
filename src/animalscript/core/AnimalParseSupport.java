package animalscript.core;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.util.Hashtable;
import java.util.Vector;

import animal.animator.TimedAnimator;
import animal.animator.TimedShow;
import animal.graphics.PTGraphicObject;
import animal.graphics.PTPoint;
import animal.graphics.PTPolyline;
import animal.main.Animation;
import animal.main.AnimationState;
import animal.misc.ColorChoice;
import animal.misc.MessageDisplay;
import animal.misc.ParseSupport;
import animal.misc.XProperties;

/**
 * This class enhances ASCII file parsing support by many helpful methods. For
 * details, see the definition of the individual methods.
 * 
 * @author Guido R&ouml;&szlig;ling(mailto:roessling@acm.org)
 * @version 1.0 1999-01-22
 */
public class AnimalParseSupport {
	public static int currentNodeMode = ParseSupport.NODETYPE_ABSOLUTE;

	private static int token = 0;

	/**
	 * Parse a double value from the StreamTokenizer object passed. The result
	 * is the double value read in iff a number inside [minValue, maxValue] was
	 * read in. Otherwise, an Exception is thrown.
	 * 
	 * @param stok
	 *            The StreamTokenizer object used for parsing the file
	 * @param description
	 *            the descriptive text to be output iff an exception occurs.
	 * @param minValue
	 *            the minimum allowed value for the double to be 'OK'
	 * @param maxValue
	 *            the maximum allowed value for the double to be 'OK'
	 * 
	 * @return the double read in iff everything checked out OK
	 * @throws IOException iff one of the conditions was not
	 *             met.
	 */
	public static double parseDouble(StreamTokenizer stok, String description,
			double minValue, double maxValue) throws IOException {
		// 1. Get the next token
		token = stok.nextToken();

		double valueRead = 0.0;

		// 2. Check whether it was of type NUMBER
		if (token == StreamTokenizer.TT_NUMBER)
			valueRead = stok.nval;
		else if (token == '"') // might be a variable or offset!
		{
			String referenceName = stok.sval;
			int oid = getObjectIDs().getIntProperty(referenceName);
			token = stok.nextToken();
			if (token != StreamTokenizer.TT_WORD)
				ParseSupport.formatException("[x|y|width|height] "
						+ description + " expected", stok);
			if (oid != -1) {
				// now, determine the basic object's bounding box
				PTGraphicObject ptgo = getAnimationState().getCloneByNum(oid);
				Rectangle rect = null;
				if (ptgo != null) // retrieve the bounding box
				{
					rect = ptgo.getBoundingBox();
					if (stok.sval.equalsIgnoreCase("x"))
						valueRead = rect.x;
					else if (stok.sval.equalsIgnoreCase("y"))
						valueRead = rect.y;
					else if (stok.sval.equalsIgnoreCase("width"))
						valueRead = rect.width;
					else if (stok.sval.equalsIgnoreCase("height"))
						valueRead = rect.height;
				}
			}
		} else if (token == '(') {
			valueRead = parseDouble(stok, description + " expression L",
					Double.MIN_VALUE, Double.MAX_VALUE);
			int localToken = stok.nextToken();
			double secondValue = parseDouble(stok, description
					+ " expression R", Double.MIN_VALUE, Double.MAX_VALUE);
			if (localToken == '+')
				valueRead += secondValue;
			else if (localToken == '-')
				valueRead -= secondValue;
			if (localToken == '*')
				valueRead *= secondValue;
			if (localToken == ':')
				if (secondValue != 0.0)
					valueRead /= secondValue;
			ParseSupport.parseMandatoryChar(stok, "')' for " + description
					+ " expression", ')');
		}

		// 4. Check whether it was inside the range
		if (valueRead < minValue || valueRead > maxValue)
			ParseSupport.formatException("double value for " + description
					+ " expected", stok);

		// 5. return the value. This only happens if no exception was cast!
		return valueRead;
	}

	/**
	 * Parse a polyline, ie. a series of nodes separated by ',' and encapsulated
	 * by braces '{' '}'. Throws a StreamCorruptedException iff the tokens read
	 * in were not a polyline.
	 * 
	 * <strong>Warning: </strong> This method <em>only</em> reads in the
	 * PTPolyline <em>nodes</em>, <strong>not </strong> the other attributes
	 * like Text, Color, ...
	 * 
	 * @param stok
	 *            The StreamTokenizer object used for parsing the file
	 * @param description
	 *            the descriptive text to be output iff an exception occurs.
	 * @return the PTPolyline object in iff everything checked out OK
	 * @throws IOException iff the input was not a polyline
	 */
	public static PTPolyline parsePolyline(StreamTokenizer stok,
			String description) throws IOException {
		PTPolyline poly = new PTPolyline(); // the object read in
		Point p; // for temporary node storage

		//  1. Check for curly brace open
		ParseSupport.parseMandatoryChar(stok, description + " '{'", '{');

		//  2. Start the loop; terminates when '}' was read in
		while (!ParseSupport.parseChar(stok, description + "'}'", '}')) {
			// 3. push back the token read in - should be a '('
			stok.pushBack();

			// 4. parse a single node
			p = ParseSupport.parseNode(stok, description + " node");

			// 5. add the point to the polyline
			poly.addNode(new PTPoint(p.x, p.y));
		}

		// 6. fini -- return the PTPolyline
		return poly;
	}

	// ===================================================================
	//                           Parsing support routines
	// ===================================================================

	/**
	 * Check for a optional color component for the current object passed. If
	 * available, store the color given in the appropriate XProperty. If no
	 * color is present, set the color to the color stored in the properties or
	 * default color black, if no entry is available as yet.
	 * 
	 * @param objectType
	 *            the type of the object(for exceptions)
	 * @param propertyName
	 *            the property name for storing the color.
	 */
	public static Color parseAndSetColor(StreamTokenizer stok,
			String objectType, String propertyName) throws IOException {
		return parseAndSetColor(stok, objectType, propertyName, "black");
	}

	/**
	 * Check for a optional color component for the current object passed. If
	 * available, store the color given in the appropriate XProperty. If no
	 * color is present, set the color to the color stored in the properties or
	 * default color black, if no entry is available as yet.
	 * 
	 * @param objectType
	 *            the type of the object(for exceptions)
	 * @param propertyName
	 *            the property name for storing the color.
	 */
	public static Color parseAndSetColor(StreamTokenizer stok,
			String objectType, String propertyName, String defaultColor)
			throws IOException {
		// parse optional color: is there such a definition in the stream?
		if (ParseSupport.parseOptionalWord(stok, propertyName + " tag for "
				+ objectType, propertyName)) {
			int aToken = stok.nextToken();
			stok.pushBack();
			String colorName = defaultColor;
			if (aToken == StreamTokenizer.TT_WORD) { // named color
				// parse the color name from the stream
				colorName = ParseSupport.parseWord(stok, objectType + ' '
						+ propertyName);

				if (colorName.equalsIgnoreCase("light"))
					colorName = colorName
							+ " "
							+ ParseSupport.parseWord(stok, objectType + ' '
									+ propertyName);
				// check whether the name is valid
				if (colorName == null || !ColorChoice.validColorName(colorName))
					colorName = defaultColor; // invalid - set to default color
			} else {
				Color targetColor = ParseSupport.parseColorRaw(stok,
						propertyName);
				if (targetColor != null)
					colorName = ColorChoice.getColorName(targetColor);
			}

			// store the color property
			getObjectProperties().put(objectType + '.' + propertyName,
					colorName);

			// return the color
			return ColorChoice.getColor(colorName);
		} 
		return getObjectProperties().getColorProperty(
				objectType + '.' + propertyName,
				ColorChoice.getColor(defaultColor));
	}

	/**
	 * Check for a optional color component for the current object passed. If no
	 * color is present, set the color to the color stored in the properties or
	 * default color black, if no entry is available as yet.
	 * 
	 * @param descriptor
	 *            a string describing the color usage / context
	 */
	public static Color parseColor(StreamTokenizer stok, String descriptor)
			throws IOException {
		// parse the color name from the stream
		int aToken = stok.nextToken();
		stok.pushBack();
//		System.err.println("Token:" +stok.sval + "/" +(char)stok.ttype);
		String colorName = "black";
		if (aToken == StreamTokenizer.TT_WORD) { // named color
			// parse the color name from the stream
			colorName = ParseSupport.parseWord(stok, descriptor);

			if (colorName.equalsIgnoreCase("light"))
				colorName = colorName
						+ " "
						+ ParseSupport.parseWord(stok, descriptor);
			// check whether the name is valid
		   // check whether the name is valid
//	    if (!ColorChoice.validColorName(colorName)) {
//	      MessageDisplay.errorMsg("Color '" + colorName
//	          + "' unknown, substituting black.",
//	          MessageDisplay.RUN_ERROR);
//	      colorName = "black"; // invalid - set to default color
//		  }
			if (colorName == null || !ColorChoice.validColorName(colorName))
				colorName = "black"; // invalid - set to default color
		} else {
			Color targetColor = ParseSupport.parseColorRaw(stok, descriptor);
			if (targetColor != null)
				colorName = ColorChoice.getColorName(targetColor);
		}
		// test whether it's a two-word color name!
		if (colorName.equalsIgnoreCase("light")
				|| colorName.equalsIgnoreCase("dark"))
			colorName += ' ' + ParseSupport.parseWord(stok, descriptor
					+ " grey color");

//		}

		// return the color
		return ColorChoice.getColor(colorName);
	}

	/**
	 * Check for a optional depth entry for the current object passed. If not
	 * available, set it to 'background'(=Integer.MAX_VALUE).
	 * 
	 * @param ptgo
	 *            the graphic object whose depth is to be checked
	 * @param objectType
	 *            the name of the object type(for error messages)
	 */
	public static void parseAndSetDepth(StreamTokenizer stok,
			PTGraphicObject ptgo, String objectType) throws IOException {
		// parse optional depth
		if (ParseSupport.parseOptionalWord(stok, "'depth' tag for "
				+ objectType, "depth")) {
			// read in next token and if it is a number, set the depth
			if ((stok.nextToken()) == StreamTokenizer.TT_NUMBER)
				ptgo.setDepth((int) stok.nval);
		} else
			ptgo.setDepth(Integer.MAX_VALUE); // set depth to 'background'
	}

	/**
	 * Check for a optional font definition entry for the current object. If not
	 * available, set font to standard 'SansSerif', 16pt.
	 * 
	 * @param stok the StreamTokenizer used to parse the stream
	 * @param baseTag
	 *            display name of the object for which the font is checked
	 */
	public static Font parseFontInfo(StreamTokenizer stok, String baseTag)
			throws IOException {
		String fontName = "SansSerif";
		int fontSize = 16;
		boolean isBold = false, isItalic = false;

		// parse optional font name(otherwise, use 'Serif')
		if (ParseSupport.parseOptionalWord(stok, "'font' tag for " + baseTag,
				"font")) {
			fontName = ParseSupport.parseWord(stok, baseTag + " font");
			if (!fontName.equalsIgnoreCase("Serif")
					&& !fontName.equalsIgnoreCase("SansSerif")
					&& !fontName.equalsIgnoreCase("Monospaced")) {
				MessageDisplay.message("Font name '" + fontName
						+ "' is not a Java system font, "
						+ "use 'Serif', 'SansSerif' or 'Monospaced'");
				fontName = "SansSerif";
			}
			//      getObjectProperties().put(baseTag +".fontName", fontName);
		} else
			fontName = getObjectProperties().getProperty(baseTag + ".fontName",
					"SansSerif");

		// parse optional font size
		if (ParseSupport.parseOptionalWord(stok, "'size' tag for " + baseTag
				+ " font size", "size")) {
			fontSize = ParseSupport.parseInt(stok, baseTag + " font size"); //, 8,
																			// 24);
			//getObjectProperties().put(baseTag +".fontSize", fontSize);
		} else
			fontSize = getObjectProperties().getIntProperty(
					baseTag + ".fontSize", 16);

		// parse optional 'bold' attribute
		isBold = ParseSupport.parseOptionalWord(stok,
				"optional 'bold' tag for " + baseTag, "bold");

		// parse optional 'italic' attribute
		isItalic = ParseSupport.parseOptionalWord(stok,
				"optional 'italic' tag for " + baseTag, "italic");
		if (ParseSupport.parseOptionalWord(stok,
				"optional 'plain' tag for " + baseTag, "plain")) {
			isBold = false;
			isItalic = false;
		}

		// Allocate font
		Font f = new Font(fontName, Font.PLAIN + (isBold ? Font.BOLD : 0)
				+ (isItalic ? Font.ITALIC : 0), fontSize);
		//    getObjectProperties().put(baseTag +".fontStyle", f.getStyle());
		getObjectProperties().put(baseTag + ".font", f);

		fontName = null;
		return f;
	}

	/**
	 * Parse object IDs - either unquoted strings or integer values.
	 * 
	 * @param label
	 *            the label to be output for error diagnostics if an error
	 *            occurs.
	 * @return the String for the object names, "null" if an error occurred.
	 */
	public static String parseOIDsTillEOL(StreamTokenizer stok, String label)
			throws IOException {
		int aToken = stok.ttype;
		StringBuilder ids = new StringBuilder(128);
		while ((aToken = stok.nextToken()) != StreamTokenizer.TT_EOL
				&& aToken != StreamTokenizer.TT_EOF) {
			if (aToken == StreamTokenizer.TT_NUMBER)
				ids.append((int) stok.nval);
			else if (aToken == StreamTokenizer.TT_WORD)
				ids.append(stok.sval);
			else
				ParseSupport.formatException("String or int value for " + label
						+ " expected", stok);
			ids.append(' ');
		}
		stok.pushBack();
		return ids.toString();
	}

	/**
	 * Check the current animator's entry for timing information Note that this
	 * method <em>sets</em> the TimedAnimator's components <code>total,
	 * units, offset</code> by calling the methods <code>setDuration,
	 * setOffset, setUnitIsTicks(boolean)</code>.
	 * 
	 * @param stok the StreamTokenizer used for parsing the timing
	 * @param ta the TimedAnimator instance for which the timing is being parsed
	 * @param animatorTag the tag for the current animator (for debug output)
	 * 
	 * @see animal.animator.TimedAnimator#setDuration(int)
	 * @see animal.animator.TimedAnimator#setOffset(int)
	 * @see animal.animator.TimedAnimator#setUnitIsTicks(boolean)
	 */
	public static void parseTiming(StreamTokenizer stok, TimedAnimator ta,
			String animatorTag) throws IOException {
		int duration = 0, delay = 0;
		String unit = "ticks";

		// check for offset keyword 'after'
		if (ParseSupport.parseOptionalWord(stok, animatorTag
				+ " 'after' keyword", "after")) {
			// parse the offset
			delay = ParseSupport.parseInt(stok, animatorTag + " delay", 0);

			// parse unit
			unit = ParseSupport.parseWord(stok, animatorTag + " unit");
		}

		// Check if keyword 'within' is given
		if (ParseSupport.parseOptionalWord(stok, animatorTag
				+ " 'within' keyword", "within")) {
			// parse time - must be at least 0!
			duration = ParseSupport
					.parseInt(stok, animatorTag + " duration", 0);

			// if there was no "after", also parse units
			if (delay == 0)
				unit = ParseSupport.parseWord(stok, animatorTag + " unit");
			else {
				if (ParseSupport.parseOptionalWord(stok, animatorTag + " unit",
						"ms"))
					unit = "ms";
				else {
					if (ParseSupport.parseOptionalWord(stok, animatorTag
							+ " unit", "ticks"))
						unit = "ticks";
				}
			}
		}

		// set the values
		ta.setDuration(duration);
		ta.setOffset(delay);

		// use "ms" only if it's written correctly, else use ticks!
		ta.setUnitIsTicks(!unit.equalsIgnoreCase("ms"));
		unit = null;
	}

	/**
	 * Parse and return the current object's starting position
	 *  
	 */
	public static Point parseStartPosition(StreamTokenizer stok, String type)
			throws IOException {
		Point uLB = null;

		// check for keyword 'at'
		if (ParseSupport.parseOptionalWord(stok, type + " keyword 'at'", "at"))
			uLB = ParseSupport.parseNode(stok, type + "left boundary");
		else {
			// parse keyword 'offset'
			ParseSupport.parseMandatoryWord(stok, type + " keyword 'offset'",
					"offset");

			// parse offset as a point
			Point offset = ParseSupport.parseNode(stok, type + " offset");

			// parse keyword 'from'
			ParseSupport.parseMandatoryWord(stok, type + " keyword 'from'",
					"from");

			// parse the basic object's ID
			String originName = ParseSupport.parseText(stok,
					"target object name");

			// parse the alignment
			String direction = ParseSupport.parseWord(stok, "offset direction")
					.toLowerCase();

			// test if the basic object exists
			int oid = getObjectIDs().getIntProperty(originName);
			if (oid != -1) {
				// now, determine the basic object's bounding box
				PTGraphicObject ptgo = getAnimationState().getCloneByNum(oid);

				// retrieve the bounding box
				Rectangle rect = ptgo.getBoundingBox();

				// determine the side to be offset from
				if (direction.equals("center"))
					uLB = new Point(rect.x + (rect.width >>> 1) + offset.x,
							rect.y + (rect.height >>> 1) + offset.y);
				else if (direction.equals("top"))
					uLB = new Point(rect.x + offset.x, rect.y + offset.y);
				else if (direction.equals("bottom"))
					uLB = new Point(rect.x + offset.x, rect.y + offset.y
							+ rect.height);
				else if (direction.equals("right"))
					uLB = new Point(rect.x + offset.x + rect.width, rect.y
							+ rect.height + offset.y);
				else
					uLB = new Point(rect.x + offset.x, rect.y + rect.height
							+ offset.y);

				rect = null;
				ptgo = null;
			} else
				uLB = new Point(50, 50);
			direction = null;
			offset = null;
			originName = null;
		}

		return uLB;
	}

	/**
	 * Parse a node information: either offset or 'keyword' followed by(x, y)
	 * 
	 * @param info
	 *            the base name of the underlying object
	 * @return the coordinates resulting from the definition
	 */
	public static Point xParseNodeInfo(StreamTokenizer stok, String info)
			throws IOException {
		return parseNodeInfo(stok, info, null);
	}

	/**
	 * Parse a node information: either offset or 'keyword' followed by(x, y)
	 * 
	 * @param info
	 *            the base name of the underlying object
	 * @param keyword
	 *            the keyword that must be given before the coordinates
	 * @return the coordinates resulting from the definition
	 */
	public static Point parseNodeInfo(StreamTokenizer stok, String info,
			String keyword) throws IOException {
		if (keyword != null)
			ParseSupport.parseOptionalWord(stok, info + " keyword '" + keyword
					+ "'", keyword);
		Point p = parseOptionalOffset(stok, info);
		if (p == null) {
			currentNodeMode = ParseSupport.NODETYPE_ABSOLUTE;
			int aToken = stok.nextToken();
			stok.pushBack();
			if ((char) aToken == '(')
				p = ParseSupport.parseNode(stok, info);
			else if ((char) aToken == '"') {
				// must be a location!
				String locationName = ParseSupport.parseText(stok,
						"target object name");
				if (getLocations().containsKey(locationName))
					p = getLocations().getPointProperty(locationName);
				else {
					MessageDisplay.message("Invalid node definition in line "
							+ stok.lineno()
							+ ", replaced by point of origin (0, 0)");
					p = new Point(0, 0);
				}
			} else {
				MessageDisplay.message("Invalid node definition in line "
						+ stok.lineno()
						+ ", replaced by point of origin (0, 0)");
				p = new Point(0, 0);
			}
		}
		getObjectProperties().put("Polyline.lastNode", p);
		return p;
	}

	private static Point parseMoveOffset(Point offset) {
		Point reference = getObjectProperties().getPointProperty(
				"Polyline.lastNode");
		Point refNode = new Point(reference.x, reference.y);
		refNode.translate(offset.x, offset.y);
		currentNodeMode = ParseSupport.NODETYPE_OFFSET_MOVE;
		return refNode;
	}

	private static Point parseNodeOffset(StreamTokenizer stok, Point offset,
			String originName) throws IOException {
		int[] oids = getObjectIDs().getIntArrayProperty(originName);
		PTGraphicObject ptgo = getAnimationState().getCloneByNum(oids[0]); // for
																		   // now!
		Point reference = null;
		if (ptgo instanceof PTPolyline) {
			PTPolyline line = (PTPolyline) ptgo;
			int nodeNr = ParseSupport.parseInt(stok, "node number [1, "
					+ line.getNodeCount() + "]", 1, line.getNodeCount());
			reference = line.getNodeAsPoint(nodeNr - 1);
//			reference = new Point(refPoint.getX(), refPoint.getY());
			reference.translate(offset.x, offset.y);
		} // ptgo instanceof PTPolyline
		else {
			ParseSupport.formatException(
					"Invalid reference for 'node' -- need PTPolyline object, got "
							+ ptgo, stok);
			reference = BasicParser.origin;
		}
		currentNodeMode = ParseSupport.NODETYPE_OFFSET_NODE;
		return reference;
	}

	private static Point parseDirOffset(StreamTokenizer stok, Point offset,
			String originName) throws IOException {
		PTGraphicObject ptgo = null;
		Rectangle bBox = null;
		Point reference = null;

		// parse the alignment
		String direction = ParseSupport.parseWord(stok, "offset direction")
				.toLowerCase();
		int dir = getCompass().getIntProperty(direction);

//		MessageDisplay.addDebugMessage("parsing request for offset dir " +direction
//				+" delta= " +offset.toString() +" from " +originName);
		if (dir == -1) {
			MessageDisplay.errorMsg("Invalid offset direction in line "
					+ stok.lineno() + " changed to 'center'",
					MessageDisplay.RUN_ERROR);
			dir = 8;
		} // if (dir == -1)

		// test if the basic object exists
		//    int oid = getObjectIDs().getIntProperty(originName);
//    MessageDisplay.addDebugMessage("OID for offset: " +getObjectIDs().getProperty(originName));
    int[] oids = null;
		if (originName != null && getObjectIDs().getProperty(originName) != null)
 		  oids = getObjectIDs().getIntArrayProperty(originName);

		if (oids != null) {
			Rectangle rect = null;
			for (int i = 0; i < oids.length; i++) {
				// now, determine the basic object's bounding box
//				MessageDisplay.addDebugMessage("request clone for step for OID "+oids[i]);
				ptgo = getAnimationState().getCloneByNum(oids[i]);
				if (ptgo == null) {
//					MessageDisplay.addDebugMessage("reference @dirOffset for oid# " +i +"=" +oids[i] 
//							+" for '" +originName +"' yielded null");
					rect = bBox;
				} else {
  				// retrieve the bounding box
	  			rect = ptgo.getBoundingBox();
//					MessageDisplay.addDebugMessage("BBox:" +rect.toString());
				}
				if (bBox == null)
					bBox = rect;
				else
					bBox = bBox.union(rect);
				rect = null;
				ptgo = null;
			} // for loop over oids for bBox calculation
			oids = null;
			// determine top left corner of offset bounding box
			reference = bBox.getLocation();
			if (dir % 3 == 1) // N, C, S
				reference.x += (bBox.width >>> 1);
			if (dir % 3 == 2) // NE, E, SE
				reference.x += bBox.width;

			if (dir / 3 == 1) // W, C, E
				reference.y += (bBox.height >>> 1);
			if (dir / 3 == 2)
				reference.y += bBox.height;
			//      lrc = null;
			offset.translate(reference.x, reference.y);
		} else {
			MessageDisplay.errorMsg("Offset object '" + originName
					+ "' does not exist in line " + stok.lineno(),
					MessageDisplay.RUN_ERROR);
			return BasicParser.origin;
		} // if (oids == null)
		currentNodeMode = ParseSupport.NODETYPE_OFFSET_DIRECTION;
		return offset;
	}

	private static Point parseBaselineOffset(StreamTokenizer stok, Point offset,
			String originName) throws IOException {
		PTGraphicObject ptgo = null;
		Rectangle bBox = null;
		Point reference = null;
		int width = 0;
		boolean incorrectUsage = false;
		// test if the basic object exists
		//    int oid = getObjectIDs().getIntProperty(originName);
		int[] oids = getObjectIDs().getIntArrayProperty(originName);

		if (oids != null) {
			if (oids.length == 1
					&& getObjectTypes().getProperty(originName).equals(
							BasicParser.getTypeIdentifier("text"))) {
				reference = getObjectProperties().getPointProperty(
						originName + ".baseline");
				ptgo = getAnimationState().getCloneByNum(oids[0]);
				if (ptgo != null) {
					width = ptgo.getBoundingBox().width;
				}
			} else {
				incorrectUsage = true;
				Rectangle rect = null;
				for (int i = 0; i < oids.length; i++) {
					// now, determine the basic object's bounding box
					ptgo = getAnimationState().getCloneByNum(oids[i]);
					// retrieve the bounding box
					rect = ptgo.getBoundingBox();
					if (bBox == null)
						bBox = rect;
					else
						bBox = bBox.union(rect);
				} // for loop over oids for bBox calculation
				// determine top left corner of offset bounding box
				reference = bBox.getLocation();
				reference.translate(0, bBox.height);
				width = bBox.width;
				rect = null;
				ptgo = null;
			}
			offset.translate(reference.x, reference.y);
			if (!ParseSupport.parseOptionalWord(stok,
					"baseline position start|end", "start"))
				offset.translate(width, 0); // should be "better"!
			// provide an error message that it would be better to use
			// either offset (dx, dy) from "originName" + SW (for 'start') or SE
			// (for 'end')
			if (incorrectUsage)
				MessageDisplay
						.errorMsg(
								"Use placement relative to a bounding box corner SW (start) or SE (end) if the base object is not a single text primitive! [line "
										+ stok.lineno() + "]",
								MessageDisplay.RUN_ERROR);
		} else {
			MessageDisplay.errorMsg("Offset base object '" + originName
					+ "' does not exist in line " + stok.lineno(),
					MessageDisplay.RUN_ERROR);
			return BasicParser.origin;
		} // if (oids == null)

		currentNodeMode = ParseSupport.NODETYPE_OFFSET_BASELINE;
		return offset;
	}

	/**
	 * Parse the current object's starting position
	 */
	public static Point parseOptionalOffset(StreamTokenizer stok, String type)
			throws IOException {
		if (ParseSupport.parseOptionalWord(stok,
				"relative command 'at location'", "at")) {
			String originName = ParseSupport.parseText(stok,
					"target object name");
			if (getLocations().containsKey(originName))
				return getLocations().getPointProperty(originName);
		} else if (ParseSupport.parseOptionalWord(stok,
				"relative command 'offset' | 'move'", "offset")) { // offset
																   // mode
			// parse 'offset' as a point
			Point offset = ParseSupport.parseNode(stok, type + " offset");

			// parse keyword 'from'
			ParseSupport.parseMandatoryWord(stok, type + " keyword 'from'",
					"from");

			// parse the basic object's ID
			String originName = ParseSupport.parseText(stok,
					"target object name");
			if (getLocations().containsKey(originName)) {
				Point referenceLocation = getLocations().getPointProperty(
						originName);
				offset.translate(referenceLocation.x, referenceLocation.y);
				return offset;
			}

			// check for either keyword "node" or direction
			if (ParseSupport.parseOptionalWord(stok,
					"relative command keyword 'node' | DIR", "node"))
				return parseNodeOffset(stok, offset, originName);
			else if (ParseSupport.parseOptionalWord(stok,
					"relative command keyword 'baseline'", "baseline"))
				return parseBaselineOffset(stok, offset, originName);
			else
				return parseDirOffset(stok, offset, originName);
		} else { // if ("offset")
			if (ParseSupport.parseOptionalWord(stok, type + " keyword 'move'",
					"move")) {
				Point offset = ParseSupport.parseNode(stok, type + " offset");
				Point reference = parseMoveOffset(offset);
				return reference;
			}
		}
		return null;
	}

	/**
	 * Check the current component's entry for displaying If the component is
	 * tagged as 'hidden', don't display it; otherwise, generate a Show animator
	 * or a TimedShow, if keyword 'after' is found.
	 * 
	 * @param idString
	 *            the string containing the objects IDs to be used for the
	 *            Show/TimedShow +
	 * @param objectType
	 *            the current object's type for parsing errors
	 * @param isShow
	 *            toggle between 'show' and 'hide'
	 * @throws IOException
	 *             if there's a problem with the underlying stream
	 */
	public static void showComponents(StreamTokenizer stok, String idString,
			String objectType, boolean isShow) throws IOException {
		int[] ids = ParseSupport.parseOIDsFromString(idString);
		// check whether the object is hidden and should not be shown
		if (!ParseSupport.parseOptionalWord(stok, objectType
				+ " 'hidden' keyword", "hidden")) {
			// check for keyword 'after'
			if (ParseSupport.parseOptionalWord(stok, objectType
					+ " 'after' keyword", "after")) {
				// push back the token for the parsing method!
				stok.pushBack();

				// generate the TimedShow animator
				TimedShow ts = new TimedShow(getCurrentStep(), ids, 0,
						(isShow) ? "show" : "hide", isShow);

				// now parse the timing information
				parseTiming(stok, ts, "Timed Show");

				// insert the animator
				BasicParser.addAnimatorToAnimation(ts, getAnimation());
			} else {
				// insert a 'normal', ie. untimed, show
				BasicParser.addAnimatorToAnimation(
						new TimedShow(getCurrentStep(), ids, 0,
								(isShow) ? "show" : "hide", isShow), getAnimation());
			}
			for (int i = 0; i < ids.length; i++)
				getCurrentlyVisible().put(String.valueOf(ids[i]),
						String.valueOf(isShow));
		}
		ids = null;
	}

	// ===================================================================
	//                    Animator parsing routines
	// ===================================================================

	/**
	 * Parse a submethod string of the type <em>keyword "value"</em>.
	 * 
	 * @param operation
	 *            the underlying operation, used for error messages
	 * @param keyword
	 *            the expected keyword
	 * @param defaultValue
	 *            the default value to be used, if the keyword is not
	 *            encountered.
	 * @return the value read in
	 */
	public static String parseMethod(StreamTokenizer stok, String operation,
			String keyword, String defaultValue) throws IOException {
		if (ParseSupport.parseOptionalWord(stok, operation + " keyword '"
				+ keyword + "' ", keyword))
			return ParseSupport.parseText(stok, operation + " subtype for "
					+ keyword);

		return defaultValue;
	}

	/**
	 * Read in a text component from the StreamTokenizer passed. Throws a
	 * StreamCorruptedException iff the tokens read in were not a text.
	 * 
	 * @param stok
	 *            The StreamTokenizer object used for parsing the file
	 * @param description
	 *            the descriptive text to be output iff an exception occurs.
	 * @return the String read in iff everything checked out OK
	 * @throws IOException iff the input was not a String
	 */
	public static String parseText(StreamTokenizer stok, String description)
			throws IOException {
		return parseText(stok, description, null, false, null);
	}

	/**
	 * Read in a text component from the StreamTokenizer passed. Throws a
	 * StreamCorruptedException iff the tokens read in were not a text.
	 * 
	 * @param stok
	 *            The StreamTokenizer object used for parsing the file
	 * @param description
	 *            the descriptive text to be output iff an exception occurs.
	 * @param tag
	 *            the tag that must appear before the component
	 * @return the String read in iff everything checked out OK
	 * @throws IOException iff the input was not a String
	 */
	public static String parseText(StreamTokenizer stok, String description,
			String tag) throws IOException {
		return parseText(stok, description, tag, true, null);
	}

	/**
	 * Read in a text component from the StreamTokenizer passed. Throws a
	 * StreamCorruptedException iff the tokens read in were not a text.
	 * 
	 * @param stok
	 *            The StreamTokenizer object used for parsing the file
	 * @param description
	 *            the descriptive text to be output iff an exception occurs.
	 * @param tag
	 *            the tag that must appear before the component
	 * @param mandatoryTag
	 *            if true, tag must appear; otherwise, tag is optional
	 * @return the String read in iff everything checked out OK
	 * @throws IOException iff the input was not a String
	 */
	public static String parseText(StreamTokenizer stok, String description,
			String tag, boolean mandatoryTag) throws IOException {
		return parseText(stok, description, tag, mandatoryTag, null);
	}

	/**
	 * Read in a text component from the StreamTokenizer passed. Throws a
	 * StreamCorruptedException iff the tokens read in were not a text.
	 * 
	 * @param stok
	 *            The StreamTokenizer object used for parsing the file
	 * @param description
	 *            the descriptive text to be output iff an exception occurs.
	 * @return the String read in iff everything checked out OK
	 * @throws IOException iff the input was not a String
	 */
	public static String parseText(StreamTokenizer stok, String description,
			String tag, boolean mandatoryTag, String languageKey)
			throws IOException {
		if (tag != null) {
			if (mandatoryTag)
				ParseSupport.parseMandatoryWord(stok, description, tag);
			else
				ParseSupport.parseOptionalWord(stok, description, tag);
			ParseSupport.parseOptionalChar(stok, description + " colon ':'",
					':');
		}
		// 1. Try to read in a single component
		token = stok.nextToken();
		String value = stok.sval;

		if (token == StreamTokenizer.TT_WORD) {
			if (value.equalsIgnoreCase("key")) {
				// parse text using a resourceBundle
				//        key: "bundleKey"
				ParseSupport.parseMandatoryChar(stok,
						"text entry key colon ':'", ':');
				token = stok.nextToken();
				String keyReadIn = null;
				if (token == '"')
					keyReadIn = stok.sval;
				//        if (bundle != null)
				value = ParseSupport.getMessagePattern(keyReadIn);
				// bundle.getMessage(keyReadIn);
			} else if (value.equalsIgnoreCase("from")) {
				// parse text extracted from a file
				//      from: "baseFileName" line <nat>
				String filename = parseText(stok, "text base file name", null,
						false, null);
				ParseSupport.parseMandatoryWord(stok,
						"text from file keywod 'line'", "line");
				int lineNumber = ParseSupport.parseInt(stok,
						"text from file line number", 0);
				int currentLineNr = -1;
				try {
					FileInputStream fis = new FileInputStream(filename);
					BufferedReader br = new BufferedReader(
							new InputStreamReader(fis));
					while ((value = br.readLine()) != null
							&& currentLineNr < lineNumber)
						currentLineNr++;
					if (currentLineNr < lineNumber)
						System.err.println("file " + filename
								+ " contained only lines [0, " + currentLineNr
								+ "]!");
					br.close();
				} catch (IOException e) {
					System.err.println("****" + e.getMessage());
				}
			}
		} else if (token == '(') {
			// parse internally translated text
			//       ( { <languageKey>: "text" } )
			String langKey = null, langText = null;
			while ((token = stok.nextToken()) != ')') {
				stok.pushBack();
				langKey = ParseSupport.parseWord(stok, "language key");
				ParseSupport.parseMandatoryChar(stok, description
						+ " colon ':'", ':');
				langText = parseText(stok, "language entry", null, false, null);
				if (langKey.equalsIgnoreCase(languageKey))
					value = langText;
			}
		} else if (token == '$') {
			// parse a variable reference
			//    $"<variableID>"
			token = stok.nextToken();
			String variableName = null;
			if (token == StreamTokenizer.TT_WORD || token == '"')
				variableName = stok.sval;
			String varType = getObjectProperties().getProperty(
					variableName + ".subType", "String");
			value = getObjectProperties().getProperty(variableName + ".value",
					"$" + variableName);
			boolean treatAsInt = ParseSupport.parseOptionalWord(stok,
					variableName + " optional keyword 'asInt'", "asInt");
			if (!treatAsInt)
				treatAsInt = varType.equalsIgnoreCase("int")
						&& (value.indexOf('.') != -1);
			if (treatAsInt) //(varType.equalsIgnoreCase("int") &&
							// value.indexOf('.') != -1)
				value = value.substring(0, value.indexOf('.'));
		} else if (token != '"' && languageKey == null) {
			// error!
			ParseSupport.formatException("*** expected '\"' here for "
					+ description, stok);
		}
		token = stok.nextToken();
		if (token == '+') {
			value += parseText(stok, description, null, false, languageKey);
		} else
			stok.pushBack();

		// 3. return the text read in
		return value;
	}

	public static String[] parseTexts(StreamTokenizer stok, String description,
			String tag, boolean mandatoryTag, String languageKey)
			throws IOException {
		Vector<String> elements = new Vector<String>(20);
		token = stok.nextToken();
		boolean inInternationalMode = languageKey != null && (token == '(');
		stok.pushBack();
		while ((token = stok.nextToken()) == '"'
				|| (inInternationalMode && token != ')')) {
			stok.pushBack();
			String element = parseText(stok, description, tag, mandatoryTag,
					languageKey);
			elements.addElement(element);
		}
		stok.pushBack();
		String[] results = new String[elements.size()];
		elements.copyInto(results);
		return results;
	}

	// ======================================================================
	//         Adapt after moving everything from BasicParser!
	// ======================================================================

	private static Animation getAnimation() {
		return BasicParser.anim;
	}

	private static AnimationState getAnimationState() {
		return BasicParser.animState;
	}

	private static XProperties getCompass() {
		return BasicParser.compass;
	}

	public static Hashtable<String, String> getCurrentlyVisible() {
		return BasicParser.getCurrentlyVisible();
	}

	public static int getCurrentStep() {
		return BasicParser.currentStep;
	}

	private static XProperties getLocations() {
		return BasicParser.getLocations();
	}

	private static XProperties getObjectIDs() {
		return BasicParser.getObjectIDs();
	}

	private static XProperties getObjectProperties() {
		if (BasicParser.getObjectProperties() == null)
			BasicParser.objectProperties = new XProperties();
		return BasicParser.getObjectProperties();
	}

	private static XProperties getObjectTypes() {
		return BasicParser.getObjectTypes();
	}
}
