package animalscript.core;

import java.awt.Point;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.Hashtable;

import animal.misc.ParseSupport;
import animal.misc.XProperties;

/**
 * This class provides an import filter for adminstrative AnimalScript commands
 * 
 * @author <a href="mailto:roessling@acm.org">Guido R&ouml;&szlig;ling</a>
 * @version 1.0 1999-06-05
 */
public class BaseAdminParser extends BasicParser implements
		AnimalScriptInterface {
	public static final String DELAY_KEY_PRESS = "delay.keyPress";

	public static final String DELAY_OBJECT_CLICK = "delay.clickOn";

	public static final String DELAY_TARGET_ID = "delay.targetID";

	public static final String DELAY_TIME = "delay.time";

	public static final String DELAY_TYPE = "delay.type";

	public static final String DELAY_UNIT = "delay.unit";

	public static final String ECHO_BOUND_IDS = "echo.boundingBoxIDs";

	public static final String ECHO_LOCATION = "echo.location";

	public static final String ECHO_MODE = "echo.mode";

	public static final String ECHO_RULE_NAME = "echo.ruleName";

	public static final String ECHO_RULE_VALUE = "echo.ruleValue";

	public static final String ECHO_TEXT = "echo.text";

	public static final String ECHO_VALUE_IDS = "echo.valueIDs";

	public static final String GROUP_ID = "group.groupID";

	public static final String GROUP_MODE = "group.mode";

	public static final String GROUP_TARGET_OIDS = "group.targetOIDs";

	public static final String GROUP_TYPE = "group.type";

	public static final String LABEL_ID = "label.ID";

	public static final String LOCATION_ID = "location.name";

	public static final String LOCATION_POINT = "location.point";

	public static final String SWAP_TYPE = "swap.type";

	public static final String SWAP_FIRST_OID = "swap.firstOID";

	public static final String SWAP_SECOND_OID = "swap.secondOID";

	private static BaseAdminProducer producer = new BaseAdminProducer();

	// ========================= attributes =========================

	/**
	 * instantiates the key class dispatcher mapping keyword to definition type
	 */
	public BaseAdminParser() {
		handledKeywords = new Hashtable<String, Object>();
		rulesHash = new XProperties();

		handledKeywords.put("delay", "parseDelay");
		rulesHash.put("delay", "# delay next step by <int> ms or ticks");

		handledKeywords.put("echo", "parseEcho");
		handledKeywords.put("print", "parseEcho");
		handledKeywords.put("write", "parseEcho");
		rulesHash
				.put(
						"echo",
						"# debugging support by printing...:\n# * the location of the given node,\n# * the bounding box for each given object\n# * the given text to stdout; useful for state messages in loading\n# *  the value of assignments\n# * the numeric IDs of the objects with the given name(s)\n# * the ids of all currently visible objects");
		rulesHash.put("print", "see rule for 'echo'");
		rulesHash.put("write", "see rule for 'echo'");

		handledKeywords.put("group", "parseGrouping");
		handledKeywords.put("merge", "parseGrouping");
		handledKeywords.put("remove", "parseGrouping");
		handledKeywords.put("set", "parseGrouping");
		handledKeywords.put("ungroup", "parseGrouping");
		rulesHash.put("group", "# merge the given IDs under new ID 'groupID'");
		rulesHash.put("merge", "# see rule for 'group'");
		rulesHash.put("set", "# see rule for 'group'");
		rulesHash.put("remove", "# remove the given ID(s) from the group ID");
		rulesHash.put("ungroup", "# see rule for 'remove'");

		handledKeywords.put("label", "parseLabel");
		rulesHash
				.put("label",
						"# insert 'label title' as a target for direct jumps in the TimeLine window");

		handledKeywords.put("deflocation", "parseLocation");
		handledKeywords.put("definelocation", "parseLocation");
		handledKeywords.put("location", "parseLocation");
		handledKeywords.put("movelocation", "parseLocation");
		rulesHash
				.put(
						"location",
						"# define a new location that can be used wherever\n# a location is expected, as if generated from an object");
		rulesHash.put("deflocation", "# see rule for 'location'");
		rulesHash.put("definelocation", "# see rule for 'location'");

		handledKeywords.put("swap", "parseSwap");
		handledKeywords.put("exchange", "parseSwap");
		rulesHash.put("swap", "# swap the IDs of object 'id1' and 'id2'");
		rulesHash.put("exchange", "# see rule for 'swap'");

		rulesHash.put("id",
				"# Object ids are arbitrary strings placed in double quotes \"\"");
		rulesHash.put("int", "# an integer number, i.e. -10, 0, 42, ...");
		rulesHash.put("double", "# a double, i.e. -10.2, 3.14159, ...");
		rulesHash.put("nat", "# a natural number, i.e. 0, 1, 2, ...");
		rulesHash.put("nat+", "# a natural number greater than 0, i.e.1, 2, ...");
		rulesHash.put("dir",
				"# a compass needle point: NW, N, NE, W, C, CENTER, E, SW, S, SE");
		rulesHash
				.put("font",
						"# valid font names for all platforms are 'Serif', 'SansSerif' and 'Monospace'");
	}

	// ===================================================================
	// interface methods
	// ===================================================================

	/**
	 * Determine depending on the command passed if a new step is needed Also keep
	 * in mind that we might be in a grouped step using the {...} form. Usually,
	 * every command not inside such a grouped step is contained in a new step.
	 * However, this is not the case for operations without visible effect -
	 * mostly maintenance or declaration entries.
	 * 
	 * @param command
	 *          the command used for the decision.
	 * @return true if a new step must be generated
	 */
	public boolean generateNewStep(String command) {
		return false;
	}

	// ===================================================================
	// Animator parsing routines
	// ===================================================================

	/**
	 * Change the delay information of the current link to the value passed. The
	 * description is usually generated by other programs and dumped in a file or
	 * on System.out.
	 */
	public XProperties parseDelay() throws IOException {
		XProperties parsedProperties = new XProperties();

		int token = stok.nextToken();
		if (token != StreamTokenizer.TT_WORD)
			throw new IOException("keyword 'delay' expected!");

		if (ParseSupport.parseOptionalWord(stok, "link delay mode `click on <id>'",
				"click")) {
			parsedProperties.put(DELAY_TYPE, DELAY_OBJECT_CLICK);
			ParseSupport.parseWord(stok, "link delay keyword `on'", "on");
			String refName = ParseSupport.parseText(stok,
					"link delay click target ID");
			int targetID = getObjectIDs().getIntProperty(refName, -1);
			if (targetID != -1)
				parsedProperties.put(DELAY_TARGET_ID, targetID);
			else { // non-existing object!
				parsedProperties.put(DELAY_TYPE, DELAY_KEY_PRESS);
				System.err.println("target ID \"" + refName
						+ "\" unknown; replaced by key press delay.");
			}
		} else {
			// this definition currently always uses time...
			parsedProperties.put(DELAY_TYPE, DELAY_TIME);

			// parse the offset
			parsedProperties.put(DELAY_TIME, ParseSupport.parseInt(stok,
					"link delay", 0));

			ParseSupport.parseOptionalWord(stok, "link delay unit 'ms'", "ms");
			parsedProperties.put(DELAY_UNIT, "ms");
		}

		producer.produceDelay(parsedProperties, currentLink);
		return parsedProperties;
	}

	/**
	 * Print out the information the user wants to System.out
	 */
	public XProperties parseEcho() throws IOException {
		XProperties parsedProperties = new XProperties();
		StringBuilder sb = new StringBuilder(80);
		sb.append("# ");
		// first, parse keyword 'echo'
		ParseSupport.parseMandatoryWord(stok, "'echo' command", "echo");
		// now, determine mode: 'text:', 'location:', 'boundingBox:'
		int token = stok.nextToken();
		String modeString = null;
		if (token == StreamTokenizer.TT_WORD)
			modeString = stok.sval;
		else
			stok.pushBack();
		boolean success = (modeString != null);
		if (modeString != null) {
			if ("location".equalsIgnoreCase(modeString)) {
				parsedProperties.put(ECHO_MODE, "location");
				ParseSupport.parseMandatoryChar(stok, "echo location colon ':'", ':');
				Point p = AnimalParseSupport.parseNodeInfo(stok, "echo location", null);
				parsedProperties.put(ECHO_LOCATION, p);
				// if (p == null)
				// sb.append("Invalid location in line ").append(stok.lineno());
				// else
				// sb.append("location
				// [line=").append(stok.lineno()).append("]:(").append(p.x).append(",
				// ").append(p.y).append(")");
			} else if ("boundingBox".equalsIgnoreCase(modeString)
					|| "bounds".equalsIgnoreCase(modeString)) {
				parsedProperties.put(ECHO_MODE, "boundingBox");
				ParseSupport.parseMandatoryChar(stok, "echo location colon ':'", ':');
				// retrieve all names of the object passed in
				String[] oids = ParseSupport.parseOIDs(stok, getObjectIDs(), true);
				parsedProperties.put(ECHO_BOUND_IDS, oids);
			} else if ("text".equalsIgnoreCase(modeString)) {
				ParseSupport.parseOptionalChar(stok, "echo text colon ':'", ':');
				parsedProperties.put(ECHO_MODE, "text");
				parsedProperties.put(ECHO_TEXT, AnimalParseSupport.parseText(stok,
						"echo text value"));
			} else if ("value".equalsIgnoreCase(modeString)) {
				ParseSupport.parseMandatoryChar(stok, "echo value colon ':'", ':');
				// retrieve all names of the object passed in
				String[] oids = ParseSupport.parseOIDs(stok, getObjectIDs(), false);
				parsedProperties.put(ECHO_MODE, "value");
				parsedProperties.put(ECHO_VALUE_IDS, oids);
				// for (int i=0; i<oids.length; i++)
				// {
				// sb.append(oids[i]).append(" = ");
				// sb.append(objectProperties.getProperty(oids[i] +".value",
				// "unknown variable: '" +oids[i] +"'"));
				// if (i != oids.length-1)
				// sb.append("\n# ");
				// }
			} else if ("ids".equalsIgnoreCase(modeString)) {
				ParseSupport.parseMandatoryChar(stok, "echo ids colon ':'", ':');
				// retrieve all names of the object passed in
				String[] oids = ParseSupport.parseOIDs(stok, getObjectIDs(), false);
				parsedProperties.put(ECHO_MODE, "ids");
				parsedProperties.put(ECHO_VALUE_IDS, oids);
				// for (int i=0; i<oids.length; i++)
				// {
				// sb.append("\"").append(oids[i]).append("\" has ID(s) ");
				// sb.append(getObjectIDs().getProperty(oids[i], "unknown variable: '"
				// +oids[i] +"'"));
				// if (i != oids.length-1)
				// sb.append("\n# ");
				// }
			} else if ("visible".equalsIgnoreCase(modeString)) {
				parsedProperties.put(ECHO_MODE, "visible");
				// sb.append("Objects currently visible:");
				// Enumeration e = getCurrentlyVisible().keys();
				// while (e.hasMoreElements())
				// {
				// String value = (String)e.nextElement();
				// if
				// (((String)getCurrentlyVisible().get(value)).equalsIgnoreCase("true"))
				// sb.append(" '").append(value).append("'");
				// }
			} else if ("rule".equalsIgnoreCase(modeString)) {
				ParseSupport.parseMandatoryChar(stok, "echo rule colon ':'", ':');
				String rule = AnimalParseSupport.parseText(stok, "echo text value");
				parsedProperties.put(ECHO_MODE, "rule");
				parsedProperties.put(ECHO_RULE_NAME, rule);
				parsedProperties.put(ECHO_RULE_VALUE, getRuleFor(rule));
				// sb.append("rule for '").append(rule).append("': \n");
				// sb.append(getRuleFor(rule));
			} else
				success = false;
		}
		if (!success) {
			stok.pushBack();
			parsedProperties.put(ECHO_MODE, "text");
			parsedProperties.put(ECHO_TEXT, ParseSupport.consumeIncludingEOL(stok,
					"echo parameter"));
			stok.pushBack();
		}
		producer.produceEcho(parsedProperties, getObjectIDs(),
				getObjectProperties(), stok.lineno(), currentStep);

		// Animal.message(sb.toString());
		return parsedProperties;
	}

	/**
	 * Parse grouping of objects: merging several objects under the same ID.
	 */
	public XProperties parseGrouping() throws IOException {
		XProperties parsedProperties = new XProperties();
		// Parse the keyword "set"
		String localType = ParseSupport.parseWord(stok,
				"Keyword 'group' | 'set' | 'merge'");
		parsedProperties.put(GROUP_TYPE, localType);
		boolean groupingMode = localType.equalsIgnoreCase("group")
				|| localType.equalsIgnoreCase("merge")
				|| localType.equalsIgnoreCase("set");
		parsedProperties.put(GROUP_MODE, groupingMode);

		parsedProperties.put(GROUP_ID, AnimalParseSupport.parseText(stok,
				"Grouping target ID"));

		parsedProperties.put(GROUP_TARGET_OIDS, ParseSupport.parseOIDs(stok,
				getObjectIDs()));

		producer.produceGrouping(parsedProperties, getObjectIDs(), anim
				.getNextGraphicObjectNum());

		return parsedProperties;
	}

	/**
	 * Add the label to the hierarchy of covered topics in this animation
	 */
	public XProperties parseLabel() throws IOException {
		if (currentLink == null)
			newLink();
		XProperties parsedProperties = new XProperties();
		ParseSupport.parseWord(stok, "label keyword");
		parsedProperties.put(LABEL_ID, AnimalParseSupport.parseText(stok,
				"label text entry", null, false, chosenLanguage));
		producer.produceLabel(parsedProperties, currentLink);
		return parsedProperties;
	}

	/**
	 * Parse a given location and store it in 'locations'
	 */
	public XProperties parseLocation() throws IOException {
		XProperties parsedProperties = new XProperties();
		String keyWords = ParseSupport.parseWord(stok, "defineLocation keyword");
		parsedProperties.put(LOCATION_ID, AnimalParseSupport.parseText(stok,
				"location ID"));
		if (keyWords.equalsIgnoreCase("moveLocation"))
			ParseSupport.parseOptionalWord(stok, "move location keyword 'to'", "to");
		else
			ParseSupport
					.parseOptionalWord(stok, "define location keyword 'at'", "at");
		parsedProperties.put(LOCATION_POINT, AnimalParseSupport.parseNodeInfo(stok,
				"target for location definition", null));
		producer.produceLocation(parsedProperties, getLocations());
		return parsedProperties;
	}

	/**
	 * Exchange the two object IDs - very useful for sorting animations etc.
	 * 
	 * The description is usually generated by other programs and dumped in a file
	 * or on System.out.
	 */
	public XProperties parseSwap() throws IOException {
		XProperties parsedProperties = new XProperties();
		// Read in the command
		stok.nextToken();
		parsedProperties.put(SWAP_TYPE, stok.sval);

		// read in the first object name(OID)
		parsedProperties.put(SWAP_FIRST_OID, AnimalParseSupport.parseText(stok,
				"swap: first object name"));

		// read in the second object name(OID)
		parsedProperties.put(SWAP_SECOND_OID, AnimalParseSupport.parseText(stok,
				"swap: second object name"));

		producer.produceSwap(parsedProperties, getObjectIDs(), stok.lineno());
		return parsedProperties;
	}
}
