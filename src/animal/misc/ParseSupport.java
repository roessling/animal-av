package animal.misc;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StreamCorruptedException;
import java.io.StreamTokenizer;
import java.text.MessageFormat;
import java.util.StringTokenizer;
import java.util.Vector;

import translator.AnimalTranslator;
import translator.ExtendedResourceBundle;

/**
 * This class enhances ASCII file parsing support by many helpful methods. For
 * details, see the definition of the individual methods.
 * 
 * @author Guido R&ouml;&szlig;ling(mailto:roessling@acm.org)
 * @version 1.0 1999-01-22
 */
public class ParseSupport {
	public static final int NODETYPE_ABSOLUTE = 0;

	public static final int NODETYPE_OFFSET_DIRECTION = 1;

	public static final int NODETYPE_OFFSET_NODE = 2;

	public static final int NODETYPE_OFFSET_MOVE = 4;

	public static final int NODETYPE_OFFSET_BASELINE = 8;

	/**
	 * The token encapsulating the input read from the StreamTokenizer.
	 */
	protected static int token;

	protected static ExtendedResourceBundle bundle;

	protected static int errorCounter = 0;

	public static void setResourceBundle(ExtendedResourceBundle theBundle) {
		bundle = theBundle;
	}

	/**
	 * Parse and return a whole line of text including the EOL. Usually only used
	 * when the 'real' parsing hasn't been finished yet.
	 * 
	 * @param stok
	 *          The StreamTokenizer object used for parsing the file
	 * @param description
	 *          the descriptive text to be output iff an exception occurs.
	 * @throws StreamCorruptedException
	 *           (hey, that's what this method is there for!)
	 */
	public static String consumeIncludingEOL(StreamTokenizer stok,
			String description) throws IOException {
		StringBuilder sb = new StringBuilder();

		while ((token = stok.nextToken()) != StreamTokenizer.TT_EOF
				&& token != StreamTokenizer.TT_EOL) {
			if (token == StreamTokenizer.TT_WORD)
				sb.append(stok.sval);
			else if (token == StreamTokenizer.TT_NUMBER) {
				if ((int) stok.nval == stok.nval)
					sb.append((int) stok.nval);
				else
					sb.append(stok.nval);
			} else if (token == '"')
				sb.append("\"").append(stok.sval).append("\"");
			else
				sb.append((char) token);
			sb.append(' ');
		}
		if (sb.length() > 2)
			sb.setLength(sb.length() - 1); // to eliminate last appended space
		return sb.toString();
	}

	/**
	 * Throw a StreamCorruptedException with some hints as to why this happens.
	 * 
	 * @param message
	 *          the message to be displayed, describing the error cause
	 * @param stok
	 *          the StreamTokenizer this errors happened in
	 * @throws StreamCorruptedException
	 *           (hey, that's what this method is there for!)
	 */
	public static void formatException(String message, StreamTokenizer stok)
			throws StreamCorruptedException {
		StringBuilder sb = new StringBuilder(message);
		sb.append(", read '").append(stok.sval).append("'/").append(stok.nval);
		sb.append("/").append(stok.ttype).append(" at line ");
		sb.append(stok.lineno());

		throw new StreamCorruptedException(sb.toString());
	}

	/**
	 * Throw a StreamCorruptedException with some hints as to why this happens.
	 * 
	 * @param messagePattern
	 *          the message to be displayed, describing the error cause
	 * @param arguments
	 *          the arguments to mix into the message
	 * @throws StreamCorruptedException
	 *           (hey, that's what this method is there for!)
	 */
	public static void formatException2(String messagePattern, Object[] arguments)
			throws StreamCorruptedException {
		incrementErrorCounter();
		MessageFormat format = new MessageFormat(getMessagePattern(messagePattern));
		throw new StreamCorruptedException(format.format(arguments));
	}

	public static String getMessagePattern(String type) {
		return AnimalTranslator.getResourceBundle().getMessage(type);
	}

	public static boolean nextTokenIsNodeToken(StreamTokenizer stok)
			throws IOException {
		token = stok.nextToken();
		boolean result = (token == '(') // explicit absolute placement
				|| (token == StreamTokenizer.TT_WORD && (stok.sval
						.equalsIgnoreCase("offset") // relative, type 1 and 2
				|| stok.sval.equalsIgnoreCase("move") // relative "Logo" placement
				));
		// push back token, as it must be parsed by client
		stok.pushBack();
		return result;
	}

	/**
	 * Parse a char from the StreamTokenizer object passed and match it to the
	 * expected value. The result is a boolean determining whether there was a
	 * match or a mismatch. If input was of type TT_NUMBER or TT_WORD, an
	 * exception is thrown.
	 * 
	 * @param stok
	 *          The StreamTokenizer object used for parsing the file
	 * @param description
	 *          the descriptive text to be output if an exception occurs.
	 * @param expectedChar
	 *          the char expected to be read
	 * 
	 * @return a boolean whether the there was a match or a mismatch
	 * @throws IOException if one of the conditions was not met.
	 */
	public static boolean parseChar(StreamTokenizer stok, String description,
			char expectedChar) throws IOException {
		// 1. Get the next token
		token = stok.nextToken();

		// 2. Check whether it was neither a NUMBER nor a WORD
		if (token == StreamTokenizer.TT_NUMBER || token == StreamTokenizer.TT_WORD)
			formatException2("mandatoryWordNotFound", new Object[] {
					String.valueOf(expectedChar), description, stok.toString() });

		// 3. Return 'true' or 'false' based on match/mismatch
		return (token == expectedChar);
	}

	/**
	 * Parse a char from the StreamTokenizer object passed and match it to the
	 * expected value. If input was of type TT_NUMBER or TT_WORD or was not the
	 * char expected, an exception is thrown.
	 * 
	 * @param stok
	 *          The StreamTokenizer object used for parsing the file
	 * @param description
	 *          the descriptive text to be output if an exception occurs.
	 * @param expectedChar
	 *          the char expected to be read
	 * 
	 * @throws IOException if one of the conditions was not met.
	 */
	public static void parseMandatoryChar(StreamTokenizer stok,
			String description, char expectedChar) throws IOException {
		// 1. Get the next token
		token = stok.nextToken();

		// 2. Check whether it was neither a NUMBER nor a WORD
		if (token == StreamTokenizer.TT_NUMBER || token == StreamTokenizer.TT_WORD)
			formatException2("wrongTypeError", new Object[] { "char", description,
					stok.toString() });

		// 3. Return 'true' or 'false' based on match/mismatch
		if (token != expectedChar)
			formatException2("mandatoryWordNotFound", new Object[] {
					String.valueOf(expectedChar), description, stok.toString() });
	}

	/**
	 * Parse a color entry from the input. The result is a java.io.Color object
	 * if such a object was read in. Otherwise, an Exception is thrown.
	 * 
	 * @param stok
	 *          The StreamTokenizer object used for parsing the file
	 * @param description
	 *          the descriptive text to be output if an exception occurs.
	 * 
	 * @return the Color object read in if everything checked out OK
	 * @throws IOException if one of the conditions was not met.
	 */
	public static Color parseColor(StreamTokenizer stok, String description)
			throws IOException {
		return parseColor(stok, description, "color");
	}

	/**
	 * Parse a color entry from the input. The result is a java.io.Color object
	 * iff such a object was read in. Otherwise, an Exception is thrown.
	 * 
	 * @param stok
	 *          The StreamTokenizer object used for parsing the file
	 * @param description
	 *          the descriptive text to be output iff an exception occurs.
	 * @param heading
	 *          a String describing the color to be read in
	 * 
	 * @return the Color object read in iff everything checked out OK
	 * @throws IOException iff one of the conditions was not met.
	 */
	public static Color parseColor(StreamTokenizer stok, String description,
			String heading) throws IOException {
		// 1. Read in "color"
		parseMandatoryWord(stok, "'" + heading + "' for " + description + " "
				+ heading, heading);

		// 2. Parse the color values
		return parseColorRaw(stok, description);
	}

	/**
	 * Parse a color entry from the input. The result is a java.io.Color object
	 * iff such a object was read in. Otherwise, an Exception is thrown.
	 * 
	 * @param stok
	 *          The StreamTokenizer object used for parsing the file
	 * @param description
	 *          the descriptive text to be output iff an exception occurs.
	 * 
	 * @return the Color object read in iff everything checked out OK
	 * @throws IOException iff one of the conditions was not met.
	 */
	public static Color parseColorRaw(StreamTokenizer stok, String description)
			throws IOException {
		int r, g, b;
		Color col;

		// 1. Read in "("
		parseMandatoryChar(stok, "( for " + description + " color", '(');

		// 2. Read in r value
		r = parseInt(stok, description + " R color value", 0, 255);

		// 3. Read in "," between color values
		parseMandatoryChar(stok, "',' between " + description + " R/G color", ',');

		// 4. Read in g value
		g = parseInt(stok, description + " G color value", 0, 255);

		// 5. Read in "," between color values
		parseMandatoryChar(stok, "',' between " + description + " G/B color", ',');

		// 6. Read in b value
		b = parseInt(stok, description + " B color value", 0, 255);

		// 7. Read in ")"
		parseMandatoryChar(stok, ") for " + description + " color", ')');

		// 8. generate the color and return it
		col = new Color(r, g, b);
		return col;
	}

	/**
	 * Parse a double value from the StreamTokenizer object passed. The result is
	 * the double value read in iff a number was read in. Otherwise, an Exception
	 * is thrown.
	 * 
	 * @param stok
	 *          The StreamTokenizer object used for parsing the file
	 * @param description
	 *          the descriptive text to be output iff an exception occurs.
	 * 
	 * @return the double read in iff everything checked out OK
	 * @throws IOException iff one of the conditions was not met.
	 */
	public static double parseDouble(StreamTokenizer stok, String description)
			throws IOException {
		return parseDouble(stok, description, Double.MIN_VALUE, Double.MAX_VALUE);
	}

	/**
	 * Parse a double value from the StreamTokenizer object passed. The result is
	 * the double value read in iff a number inside [minValue, maxValue] was read
	 * in. Otherwise, an Exception is thrown.
	 * 
	 * @param stok
	 *          The StreamTokenizer object used for parsing the file
	 * @param description
	 *          the descriptive text to be output iff an exception occurs.
	 * @param minValue
	 *          the minimum allowed value for the double to be 'OK'
	 * @param maxValue
	 *          the maximum allowed value for the double to be 'OK'
	 * 
	 * @return the double read in iff everything checked out OK
	 * @throws IOException iff one of the conditions was not met.
	 */
	public static double parseDouble(StreamTokenizer stok, String description,
			double minValue, double maxValue) throws IOException {
		// 1. Get the next token
		token = stok.nextToken();

		double valueRead = 0.0;

		// 2. Check whether it was of type NUMBER
		if (token == StreamTokenizer.TT_NUMBER)
			valueRead = stok.nval;
		// else if (token == '"') // might be a variable or offset!
		// {
		// String referenceName = stok.sval;
		// int oid = BasicParser.objectIDs.getIntProperty(referenceName);
		// token = stok.nextToken();
		// if (token != StreamTokenizer.TT_WORD)
		// formatException("[x|y|width|height] " +description + " expected",stok);
		// if (oid != -1)
		// {
		// // now, determine the basic object's bounding box
		// PTGraphicObject ptgo = BasicParser.animState.getCloneByNum(oid);
		// Rectangle rect = null;
		// if (ptgo != null) // retrieve the bounding box
		// {
		// rect = ptgo.getBoundingBox();
		// if (stok.sval.equalsIgnoreCase("x"))
		// valueRead = rect.x;
		// else if (stok.sval.equalsIgnoreCase("y"))
		// valueRead = rect.y;
		// else if (stok.sval.equalsIgnoreCase("width"))
		// valueRead = rect.width;
		// else if (stok.sval.equalsIgnoreCase("height"))
		// valueRead = rect.height;
		// }
		// }
		// }
		else if (token == '(') {
			valueRead = parseDouble(stok, description + " expression L");
			int localToken = stok.nextToken();
			double secondValue = parseDouble(stok, description + " expression R");
			if (localToken == '+')
				valueRead += secondValue;
			else if (localToken == '-')
				valueRead -= secondValue;
			if (localToken == '*')
				valueRead *= secondValue;
			if (localToken == ':')
				if (secondValue != 0.0)
					valueRead /= secondValue;
			parseMandatoryChar(stok, "')' for " + description + " expression", ')');
		}

		// 4. Check whether it was inside the range
		if (valueRead < minValue || valueRead > maxValue)
			formatException("double value for " + description + " expected inside ["
					+ minValue + ", " + maxValue + "]", stok);

		// 5. return the value. This only happens if no exception was cast!
		return valueRead;
	}

	public static double parseDoubleOLD(StreamTokenizer stok, String description,
			double minValue, double maxValue) throws IOException {
		// 1. Get the next token
		token = stok.nextToken();

		// 2. Check whether it was of type NUMBER
		if (token != StreamTokenizer.TT_NUMBER)
			formatException("double value for " + description + " expected", stok);

		// 3. Store the value read in
		double valueRead = stok.nval;

		// 4. Check whether it was inside the range
		if (valueRead < minValue || valueRead > maxValue)
			formatException("double value for " + description + " expected", stok);

		// 5. return the value. This only happens if no exception was cast!
		return valueRead;
	}

	public static Font parseFontInformation(StreamTokenizer stok,
			String description) throws IOException {
		// 1. get the name
		String fontName = ParseSupport.parseWord(stok, "fontname");
		// properties.put(PTText.TEXT_FONT_NAME, ParseSupport.parseWord(stok,
		// "fontname"));

		// 2. check for "style"
		ParseSupport.parseMandatoryWord(stok, "fontstyle 'style'", "style");

		// 3. get style
		int fontStyle = ParseSupport.parseInt(stok, "fontstyle");
		// properties.put(PTText.TEXT_FONT_STYLE, ParseSupport.parseInt(stok,
		// "fontstyle"));

		// 10. check for "size"
		ParseSupport.parseMandatoryWord(stok, "fontsize 'size'", "size");

		// 11. get the size
		int fontSize = ParseSupport.parseInt(stok, "fontsize");
		// properties.put(PTText.TEXT_FONT_SIZE, ParseSupport.parseInt(stok,
		// "fontsize"));

		return new Font(fontName, fontStyle, fontSize);
	}

	/**
	 * Parse the file for a EOL from the StreamTokenizer. If the next token is not
	 * StreamTokenizer.TT_EOL, an Exception is thrown.
	 * 
	 * @param stok
	 *          The StreamTokenizer object used for parsing the file
	 * @param description
	 *          the descriptive text to be output iff an exception occurs.
	 * 
	 * @throws IOException iff one of the conditions was not met.
	 */
	public static void parseMandatoryEOL(StreamTokenizer stok, String description)
			throws IOException {
		token = stok.nextToken();
		if (token != StreamTokenizer.TT_EOL)
			formatException("EOL for " + description + " expected", stok);
	}

	/**
	 * Parse an integer value from the StreamTokenizer object passed. The result
	 * is the int value read in iff a number was read in. Otherwise, an Exception
	 * is thrown.
	 * 
	 * @param stok
	 *          The StreamTokenizer object used for parsing the file
	 * @param description
	 *          the descriptive text to be output iff an exception occurs.
	 * 
	 * @return the integer read in iff everything checked out OK
	 * @throws IOException iff one of the conditions was not met.
	 */
	public static int parseInt(StreamTokenizer stok, String description)
			throws IOException {
		return parseInt(stok, description, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}

	/**
	 * Parse an integer value from the StreamTokenizer object passed. The result
	 * is the int value read in iff a number was read in. Otherwise, an Exception
	 * is thrown.
	 * 
	 * @param stok
	 *          The StreamTokenizer object used for parsing the file
	 * @param description
	 *          the descriptive text to be output iff an exception occurs.
	 * @param minValue
	 *          the minimum acceptable input value
	 * 
	 * @return the integer read in iff everything checked out OK
	 * @throws IOException iff one of the conditions was not met.
	 */
	public static int parseInt(StreamTokenizer stok, String description,
			int minValue) throws IOException {
		return parseInt(stok, description, minValue, Integer.MAX_VALUE);
	}

	/**
	 * Parse an integer value from the StreamTokenizer object passed. The result
	 * is the int value read in iff a number inside [minValue, maxValue] was read
	 * in. Otherwise, an Exception is thrown.
	 * 
	 * @param stok
	 *          The StreamTokenizer object used for parsing the file
	 * @param description
	 *          the descriptive text to be output iff an exception occurs.
	 * @param minValue
	 *          the minimum allowed value for the integer to be 'OK'
	 * @param maxValue
	 *          the maximum allowed value for the integer to be 'OK'
	 * 
	 * @return the integer read in iff everything checked out OK
	 * @throws IOException iff one of the conditions was not met.
	 */
	public static int parseInt(StreamTokenizer stok, String description,
			int minValue, int maxValue) throws IOException {
		return (int) Math.round(parseDouble(stok, description, minValue, maxValue));
	}

	/**
	 * Parse a node from the StreamTokenizer object passed. The result is the node
	 * as a java.awt.Point iff a valid node was read in. Otherwise, an Exception
	 * is thrown.
	 * 
	 * @param stok
	 *          The StreamTokenizer object used for parsing the file
	 * @param description
	 *          the descriptive text to be output iff an exception occurs.
	 * 
	 * @return the node read in iff everything checked out OK
	 * @throws IOException iff one of the conditions was not met.
	 */
	public static Point parseNode(StreamTokenizer stok, String description)
			throws IOException {
		int x, y;
		Point p;

		// 1. Read in "("
		parseMandatoryChar(stok, "open brace for " + description, '(');

		// 2. Read in x
		x = parseInt(stok, description + " x value");

		// 3. Read in "," between coordinates
		parseMandatoryChar(stok, "',' between " + description + " coords expected",
				',');

		// 4. Read in y
		y = parseInt(stok, description + " y value");

		// 5. Read in ")"
		parseMandatoryChar(stok, "open brace for " + description, ')');

		// 6. generate the point and return it
		p = new Point(x, y);
		return p;
	}

	/**
	 * Read in a list of integers, usually object ids for animators. Assumes the
	 * delimiting keyword is 'by'.
	 * 
	 * @param stok
	 *          The StreamTokenizer object used for parsing the file
	 * @param description
	 *          the descriptive text to be output iff an exception occurs.
	 * 
	 * @return the array of values
	 * @throws IOException
	 *           iff an error occurs
	 */
	public static int[] parseObjectIDs(StreamTokenizer stok, String description)
			throws IOException {
		return parseObjectIDs(stok, description, "by");
	}

	/**
	 * Read in a list of integers, usually object ids for animators.
	 * 
	 * @param stok
	 *          The StreamTokenizer object used for parsing the file
	 * @param description
	 *          the descriptive text to be output iff an exception occurs.
	 * @param keyword
	 *          the delimiting keyword
	 * 
	 * @return the array of values
	 * @throws IOException
	 *           iff an error occurs
	 */
	public static int[] parseObjectIDs(StreamTokenizer stok, String description,
			String keyword) throws IOException {
		// 1. Declare and allocate a storage object
		Vector<Integer> targetObjects = new Vector<Integer>();

		// 2. a quick helper..
		StringBuilder msgBuffer = new StringBuilder(description);
		msgBuffer.append(" objects keyword ").append(keyword);
		String msg = msgBuffer.toString();

		// 3. keep on repeating the input until delimiting keyword is found
		while (!ParseSupport.parseOptionalWord(stok, msg, keyword)
				&& stok.ttype != StreamTokenizer.TT_EOL) {
			targetObjects.addElement(new Integer(ParseSupport.parseInt(stok,
					"Animator target ID")));
		}

		// 4. transform into an array of int
		int n = targetObjects.size();
		int[] objectNums = new int[n];
		for (int i = 0; i < n; i++)
			objectNums[i] = targetObjects.elementAt(i).intValue();

		// 5. finally, return it
		return objectNums;
	}

	public static String[] parseOIDs(StreamTokenizer stok, XProperties xprops) {
		return parseOIDs(stok, xprops, true);
	}

	public static String[] parseOIDs(StreamTokenizer stok, XProperties xprops,
			boolean checkIDRegistry) {
		int oid = -1, i;
		String name = "";
		Vector<String> ids = new Vector<String>(50, 20);

		try {
			while ((token = stok.nextToken()) == '"') {
				// get object name
				name = stok.sval;

				// lookup object id(test if object exists)
				if (!checkIDRegistry || xprops.getProperty(name) != null)
					ids.addElement(name);
			}
		} catch (IOException e) {
			System.err.println("Name: " + name + " OID: " + oid + " #:" + ids.size()
					+ e.getMessage());
		}
		// push back the token read in
		stok.pushBack();

		// generate the array of elements
		String[] result = new String[ids.size()];

		// copy the values into the array
		for (i = 0; i < result.length; i++)
			result[i] = ids.elementAt(i);
		// return the result
		return result;
	}

	// now in XProperties!
	public static int[] parseOIDsFromString(String name) {
		int[] result = null;

		StringTokenizer stok = new StringTokenizer(name);
		int size = stok.countTokens();
		result = new int[size];
		for (int i = 0; i < size; i++)
			result[i] = Integer.parseInt(stok.nextToken());
//		if (result == null)
//			System.err.println("int[]-Property not found! " + name);

		return result;
	}

	public static int[] parseOIDNames(StreamTokenizer stok, XProperties xprops,
			int nrObjects) {
		String[] oidNames = parseOIDs(stok, xprops);
		return expandIDsFromStrings(oidNames, xprops, nrObjects);
	}

	public static int[] expandIDsFromStrings(String[] ids, XProperties xprops,
			int nrLastElement) {
		// generate the array of elements
		boolean[] usedIDs = new boolean[nrLastElement + 1];
		int i, j, nrEntries = 0;
		for (i = 0; i < ids.length; i++) {
			int[] firstElements = xprops.getIntArrayProperty(ids[i]);
			for (j = 0; j < firstElements.length; j++) {
				if (!usedIDs[firstElements[j]])
					nrEntries++;
				usedIDs[firstElements[j]] = true; // eliminate double entries :-)
			}
		}

		int[] oids = new int[nrEntries];
		j = 0;
		// copy the values into the array
		for (i = 0; i < usedIDs.length; i++)
			if (usedIDs[i])
				oids[j++] = i;
		// return the result
		return oids;
	}

	/**
	 * Read in a list of integers, usually object ids for animators.
	 * 
	 * @param stok
	 *          The StreamTokenizer object used for parsing the file
	 * @param description
	 *          the descriptive text to be output iff an exception occurs.
	 * 
	 * @return the array of values
	 * @throws IOException
	 *           iff an error occurs
	 */
	public static int[] parseObjectIDsTillEOL(StreamTokenizer stok,
			String description) throws IOException {
		// 1. Declare and allocate a storage object
		Vector<Integer> targetObjects = new Vector<Integer>();

		// 2. a quick helper..
		StringBuilder msgBuffer = new StringBuilder(description);
		msgBuffer.append(" objects EOL ");

		// 3. keep on repeating the input until delimiting keyword is found
		while ((token = stok.nextToken()) == StreamTokenizer.TT_NUMBER) {
			targetObjects.addElement(new Integer((int) stok.nval));
		}
		// 4. transform into an array of int
		int n = targetObjects.size();
		int[] objectNums = new int[n];
		for (int i = 0; i < n; i++)
			objectNums[i] = targetObjects.elementAt(i).intValue();

		// 5. finally, return it
		return objectNums;
	}

	/**
	 * Parse a char from the StreamTokenizer object passed and match it to the
	 * expected value. The result is a boolean determining whether there was a
	 * match or a mismatch. If input was of type TT_NUMBER or TT_WORD, an
	 * exception is thrown.
	 * 
	 * @param stok
	 *          The StreamTokenizer object used for parsing the file
	 * @param description
	 *          the descriptive text to be output iff an exception occurs.
	 * @param expectedChar
	 *          the char expected to be read
	 * 
	 * @return a boolean whether the there was a match or a mismatch
	 * @throws IOException iff one of the conditions was not met.
	 */
	public static boolean parseOptionalChar(StreamTokenizer stok,
			String description, char expectedChar) throws IOException {
		// 1. Get the next token
		token = stok.nextToken();

		// 3. Return 'true' or 'false' based on match/mismatch
		boolean result = (token == expectedChar);

		// 4. if not the expected char, push the token back...
		if (!result)
			stok.pushBack();

		return result;
	}

	/**
	 * Parse a char from the StreamTokenizer object passed and match it to the
	 * expected value. The result is a boolean determining whether there was a
	 * match or a mismatch. If input was of type TT_NUMBER or TT_WORD, an
	 * exception is thrown.
	 * 
	 * @param stok
	 *          The StreamTokenizer object used for parsing the file
	 * @param description
	 *          the descriptive text to be output iff an exception occurs.
	 * 
	 * @return a boolean whether the there was a match or a mismatch
	 * @throws IOException iff one of the conditions was not met.
	 */
	public static double parseOptionalNumber(StreamTokenizer stok,
			String description) throws IOException {
		// 1. Get the next token
		token = stok.nextToken();

		// 2. Check whether it was neither a NUMBER nor a WORD
		if (token == StreamTokenizer.TT_NUMBER)
			return stok.nval;

		stok.pushBack();

		return Double.MAX_VALUE;
	}

	/**
	 * Check the input for a <strong>expected</strong> word component. Simply
	 * reads a word component and then tries a case-insensitive matching. Throws a
	 * StreamCorruptedException iff the token read in was not of type
	 * 'StreamTokenizer.TT_WORD'. Returns true or false based on whether the
	 * component was the expected one whether not.
	 * 
	 * @param stok
	 *          The StreamTokenizer object used for parsing the file
	 * @param description
	 *          the descriptive text to be output iff an exception occurs.
	 * @param expectedWord
	 *          the word expected for this operation
	 * @return true to indicate 'everything OK', false to indicate 'mismatch'.
	 * @throws IOException iff the input was not a String
	 */
	public static boolean parseOptionalWord(StreamTokenizer stok,
			String description, String expectedWord) throws IOException {
		// 1. Get the next token
		token = stok.nextToken();

		// 2. Check whether it was of type WORD
		boolean result = (token == StreamTokenizer.TT_WORD && stok.sval
				.equalsIgnoreCase(expectedWord));

		// 3. if mismatch, push back the token read in
		if (!result)
			stok.pushBack();

		// 4. return result
		return result;
	}

	/**
	 * Read in a text component from the StreamTokenizer passed. Throws a
	 * StreamCorruptedException iff the tokens read in were not a text.
	 * 
	 * @param stok
	 *          The StreamTokenizer object used for parsing the file
	 * @param description
	 *          the descriptive text to be output iff an exception occurs.
	 * @return the String read in iff everything checked out OK
	 * @throws IOException iff the input was not a String
	 */
	public static String parseText(StreamTokenizer stok, String description)
			throws IOException {
		// 1. Try to read in a single component
		token = stok.nextToken();

		if (token != '"')
			formatException("*** expected '\"' here for " + description, stok);

		// 3. return the text read in
		return stok.sval;
	}

	/**
	 * Read in a text component from the StreamTokenizer passed. Throws a
	 * StreamCorruptedException iff the tokens read in were not a text.
	 * 
	 * @param stok
	 *          The StreamTokenizer object used for parsing the file
	 * @param description
	 *          the descriptive text to be output iff an exception occurs.
	 * @param tag
	 *          the tag that must appear before the component
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
	 *          The StreamTokenizer object used for parsing the file
	 * @param description
	 *          the descriptive text to be output iff an exception occurs.
	 * @param tag
	 *          the tag that must appear before the component
	 * @param mandatoryTag
	 *          if true, tag must appear; otherwise, tag is optional
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
	 *          The StreamTokenizer object used for parsing the file
	 * @param description
	 *          the descriptive text to be output iff an exception occurs.
	 * @return the String read in iff everything checked out OK
	 * @throws IOException iff the input was not a String
	 */
	public static String parseText(StreamTokenizer stok, String description,
			String tag, boolean mandatoryTag, String languageKey) throws IOException {
		if (tag != null) {
			if (mandatoryTag)
				parseMandatoryWord(stok, description, tag);
			else
				parseOptionalWord(stok, description, tag);
			parseOptionalChar(stok, description + " colon ':'", ':');
		}

		// 1. Try to read in a single component
		token = stok.nextToken();
		String value = stok.sval;

		if (token == StreamTokenizer.TT_WORD) {
			if (value.equalsIgnoreCase("key")) {
				parseMandatoryChar(stok, "text entry key colon ':'", ':');
				token = stok.nextToken();
				String keyReadIn = null;
				if (token == '"')
					keyReadIn = stok.sval;
				if (bundle != null)
					value = bundle.getMessage(keyReadIn);
			} else if (value.equalsIgnoreCase("from")) {
				String filename = parseText(stok, "text base file name");
				parseMandatoryWord(stok, "text from file keywod 'line'", "line");
				int lineNumber = parseInt(stok, "text from file line number", 0);
				int currentLineNr = -1;
				try {
					FileInputStream fis = new FileInputStream(filename);
					@SuppressWarnings("resource")
          BufferedReader br = new BufferedReader(new InputStreamReader(fis));
					while ((value = br.readLine()) != null && currentLineNr < lineNumber)
						currentLineNr++;
					if (currentLineNr < lineNumber)
						System.err.println("file " + filename
								+ " contained only lines [0, " + currentLineNr + "]!");
				} catch (IOException e) {
					System.err.println("****" + e.getMessage());
				}
			}
		} else if (token == '(') {
			String langKey = null, langText = null;
			while ((token = stok.nextToken()) != ')') {
				stok.pushBack();
				langKey = parseWord(stok, "language key");
				parseMandatoryChar(stok, description + " colon ':'", ':');
				langText = parseText(stok, "language entry");
				if (langKey.equalsIgnoreCase(languageKey))
					value = langText;
			}
		} else if (token != '"' && languageKey == null)
			formatException("*** expected '\"' here for " + description, stok);

		// 3. return the text read in
		return value;
	}

	public static String[] parseTexts(StreamTokenizer stok, String description,
			String tag, boolean mandatoryTag, String languageKey) throws IOException {
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

	/**
	 * Read in a text component reaching till EOL from the StreamTokenizer passed.
	 * Throws a StreamCorruptedException iff the tokens read in were not a text.
	 * 
	 * @param stok
	 *          The StreamTokenizer object used for parsing the file
	 * @param description
	 *          the descriptive text to be output iff an exception occurs.
	 * @return the String read in iff everything checked out OK
	 * @throws IOException iff the input was not a String
	 */
	public static String parseTextTillEOL(StreamTokenizer stok, String description)
			throws IOException {
		// 1. Rest of line is supposed to be text - consume it!
		StringBuilder textReadIn = new StringBuilder();

		// 2. Try to read in a single component
		token = stok.nextToken();

		textReadIn.append(stok.sval);

		// consume the EOL
		token = stok.nextToken();
		if (token != StreamTokenizer.TT_EOL)
			formatException("Expected an end of line here for parsing text till EOL",
					stok);

		// 3. return the text read in
		return textReadIn.toString();
	}

	/**
	 * Read in a word component from the StreamTokenizer passed. Throws a
	 * StreamCorruptedException iff the token read in was not of type
	 * 'StreamTokenizer.TT_WORD'.
	 * 
	 * @param stok
	 *          The StreamTokenizer object used for parsing the file
	 * @param description
	 *          the descriptive text to be output iff an exception occurs.
	 * @return the String read in iff everything checked out OK
	 * @throws IOException iff the input was not a String
	 */
	public static String parseWord(StreamTokenizer stok, String description)
			throws IOException {
		// 1. Get the next token
		token = stok.nextToken();

		// 2. Check whether it was of type WORD
		if (token != StreamTokenizer.TT_WORD)
			formatException("String value for " + description + " expected", stok);

		// 3. return the value read in
		return stok.sval;
	}

	/**
	 * Check the input for a <strong>expected</strong> word component. Simply
	 * reads a word component and then tries a case-insensitive matching. Throws a
	 * StreamCorruptedException iff the token read in was not of type
	 * 'StreamTokenizer.TT_WORD'. Returns true or false based on whether the
	 * component was the expected one whether not.
	 * 
	 * @param stok
	 *          The StreamTokenizer object used for parsing the file
	 * @param description
	 *          the descriptive text to be output iff an exception occurs.
	 * @param expectedWord
	 *          the word expected for this operation
	 * @return true to indicate 'everything OK', false to indicate 'mismatch'.
	 * @throws IOException iff the input was not a String
	 */
	public static boolean parseWord(StreamTokenizer stok, String description,
			String expectedWord) throws IOException {
		// 1. Retrieve the word read in
		String wordReadIn = ParseSupport.parseWord(stok, description);

		// 2. return the match/mismatch value
		return (wordReadIn.equalsIgnoreCase(expectedWord));
	}

	/**
	 * Check the input for a <strong>expected</strong> word component. Simply
	 * reads a word component and then tries a case-insensitive matching. Throws a
	 * StreamCorruptedException iff the token read in was not of type
	 * 'StreamTokenizer.TT_WORD'.
	 * 
	 * @param stok
	 *          The StreamTokenizer object used for parsing the file
	 * @param description
	 *          the descriptive text to be output iff an exception occurs.
	 * @param expectedWord
	 *          the word expected for this operation
	 * @throws IOException iff the input was not a String
	 */
	public static void parseMandatoryWord(StreamTokenizer stok,
			String description, String expectedWord) throws IOException {
		// 1. Retrieve the word read in
		String wordReadIn = ParseSupport.parseWord(stok, description);

		// 2. if mismatch, throw an exception
		if (!wordReadIn.equalsIgnoreCase(expectedWord))
			formatException2("mandatoryWordNotFound", new Object[] { expectedWord,
					description, stok.toString() });
	}

	/**
	 * Skip the remaining entries on the current input line of text, including the
	 * EOL. Usually only used when the 'real' parsing hasn't been finished yet, as
	 * well as for supporting later additions to the end of a line.
	 * 
	 * @param stok
	 *          The StreamTokenizer object used for parsing the file
	 * @param description
	 *          the descriptive text to be output iff an exception occurs.
	 * @throws StreamCorruptedException
	 *           (hey, that's what this method is there for!)
	 */
	public static void skipLineTillEOL(StreamTokenizer stok, String description)
			throws IOException {
		boolean messageFlag = false;
		while ((token = stok.nextToken()) != StreamTokenizer.TT_EOL)
			messageFlag = true;
		if (messageFlag)
			MessageDisplay.message("Skipped entries in line " + stok.lineno()
					+ " until EOL.");
	}

	public static int getErrorCount() {
		return errorCounter;
	}

	public static void resetErrorCounter() {
		errorCounter = 0;
	}

	public static void incrementErrorCounter() {
		addToErrorCounter(1);
	}

	public static void addToErrorCounter(int nrErrors) {
		if (nrErrors > 0)
			errorCounter += nrErrors;
	}

	public int nextToken(StreamTokenizer stok) {
		int currentToken = 0;
		try {
			currentToken = stok.nextToken();
			// System.err.println("token: " +token +" / " +stok.sval);
			if (currentToken == '\\') {
				currentToken = stok.nextToken();
				if (currentToken != StreamTokenizer.TT_EOL)
					stok.pushBack();
				else
					currentToken = stok.nextToken();
			}
		} catch (IOException ioexc) {
			// do nothing
		}
		return currentToken;
	}
}
