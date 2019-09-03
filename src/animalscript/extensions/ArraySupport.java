package animalscript.extensions;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.Hashtable;
import java.util.StringTokenizer;

import animal.animator.ColorChanger;
import animal.animator.Move;
import animal.animator.TimedShow;
import animal.graphics.PTGraphicObject;
import animal.graphics.PTPolyline;
import animal.graphics.PTText;
import animal.main.Animal;
import animal.misc.MessageDisplay;
import animal.misc.ParseSupport;
import animal.misc.XProperties;
import animalscript.core.AnimalParseSupport;
import animalscript.core.AnimalScriptInterface;
import animalscript.core.BasicParser;

/**
 * This class provides an import filter for AnimalScript array commands
 * 
 * @author <a href="mailto:roessling@acm.org">Guido R&ouml;&szlig;ling</a>
 * @version 1.0 2000-03-21
 */
public class ArraySupport extends BasicParser implements AnimalScriptInterface {

	/**
	 * instantiates the key class dispatcher mapping keyword to definition type
	 */
	public ArraySupport() {
		handledKeywords = new Hashtable<String, Object>();
		rulesHash = new XProperties();
		handledKeywords.put("array", "parseArrayInput");
		handledKeywords.put("field", "parseArrayInput");

		// rulesHash.put("array", "array <id> [at] <nodeDefinition> [color <color>]
		// [fillColor <color>]\n [elementColor <color>] [elemHighlight <color>]
		// [cellHighlight <color>] [vertical | horizontal] length <n+> { \"text\" }
		// [depth <n>] [after <n> ms | ticks] [cascaded [within <n> ms | ticks]]
		// [fontInfo]");
		// rulesHash.put("field", "see rule for 'array'");
		handledKeywords.put("arraymarker", "parseArrayMarkerInput");
		handledKeywords.put("arraypointer", "parseArrayMarkerInput");
		handledKeywords.put("arrayindex", "parseArrayMarkerInput");

		// rulesHash.put("arraymarker", "arrayMarker <id> on <id> atIndex <n> [label
		// \"label\"] [<timing>]\n#\tinsert a pointer to an array position on the
		// target array.");
		// rulesHash.put("arrayindex", "see rule for 'arrayMarker'");
		// rulesHash.put("arraypointer", "see rule for 'arrayMarker'");
		handledKeywords.put("highlightarraycell", "parseColorChange");
		handledKeywords.put("highlightarrayelem", "parseColorChange");
		handledKeywords.put("highlightarrayelement", "parseColorChange");
		handledKeywords.put("unhighlightarraycell", "parseColorChange");
		handledKeywords.put("unhighlightarrayelem", "parseColorChange");
		handledKeywords.put("unhighlightarrayelement", "parseColorChange");

		// rulesHash.put("highlightarraycell", "highlightarraycell on <id> position
		// <n> [<timing>]");
		// rulesHash.put("highlightarrayelem", "highlightarrayelem on <id> position
		// <n> [<timing>]");
		// rulesHash.put("unhighlightarraycell", "unhighlightarraycell on <id>
		// position <n> [<timing>]");
		// rulesHash.put("unhighlightarrayelem", "unhighlightarrayelem on <id>
		// position <n> [<timing>]");
		handledKeywords.put("arrayput", "parseArrayPut");
		rulesHash.put("arrayput",
				"# put the given value at the given array position");

		handledKeywords.put("arrayswap", "parseArraySwap");
		rulesHash.put("arrayswap",
				"# swap the given array positions on the target array");

		handledKeywords.put("jumparrayindex", "parseMovePointer");
		handledKeywords.put("jumparraymarker", "parseMovePointer");
		handledKeywords.put("jumparraypointer", "parseMovePointer");
		handledKeywords.put("jumpindex", "parseMovePointer");
		handledKeywords.put("jumpmarker", "parseMovePointer");
		handledKeywords.put("jumppointer", "parseMovePointer");
		handledKeywords.put("movearrayindex", "parseMovePointer");
		handledKeywords.put("movearraymarker", "parseMovePointer");
		handledKeywords.put("movearraypointer", "parseMovePointer");
		handledKeywords.put("moveindex", "parseMovePointer");
		handledKeywords.put("movemarker", "parseMovePointer");
		handledKeywords.put("movepointer", "parseMovePointer");

		// rulesHash.put("movemarker", "moveMarker <id> to [position] <n> | arrayEnd
		// | outside [<timing>]");
		// rulesHash.put("jumparrayindex", "see rule for 'moveMarker'");
		// rulesHash.put("jumparraymarker", "see rule for 'moveMarker'");
		// rulesHash.put("jumpindex", "see rule for 'moveMarker'");
		// rulesHash.put("jumparraypointer", "see rule for 'moveMarker'");
		// rulesHash.put("jumpmarker", "see rule for 'moveMarker'");
		// rulesHash.put("jumppointer", "see rule for 'moveMarker'");
		// rulesHash.put("movearrayindex", "see rule for 'moveMarker'");
		// rulesHash.put("movearraymarker", "see rule for 'moveMarker'");
		// rulesHash.put("movearraypointer", "see rule for 'moveMarker'");
		// rulesHash.put("moveindex", "see rule for 'moveMarker'");
		// rulesHash.put("movepointer", "see rule for 'moveMarker'");
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
	 * @param currentCommand
	 *          the command used for the decision.
	 * @return true if a new step must be generated
	 */
	public boolean generateNewStep(String currentCommand) {
		return !sameStep; // !sameStep || !currentCommand("arrayswap");
	}

	// ===================================================================
	// Animator parsing routines
	// ===================================================================

	/**
	 * Create a array from the description read from the StreamTokenizer. The
	 * description is usually generated by other programs and dumped in a file or
	 * on System.out.
	 */
	public void parseArrayInput() throws IOException {
		int i; // for loops
		String localType; // array type and name
		String arrayName; // array type and name
		Point basePoint; // starting point for the array
		Color arrayColor; // outline, fill, element color
		Color fillColor; // outline, fill, element color
		Color elementColor; // outline, fill, element color
		boolean verticalMode = false; // toggle vertical and horizontal
		int arrayLength; // nr or array elements -- must be at least 1
		String[] elements; // the array elements
		int basicDepth; // the depth of the array
		int displayDelay = 0; // the delay for displaying the elements
		boolean cascadedDisplay; // display elements "in order" or all at once?
		int displayDuration = 0; // the time to be skipped between displaying
															// elements
		int[] oids; // OIDs for storing

		int[] entryIDs = null;
		StringBuilder components;

		// read in object type
		localType = ParseSupport.parseWord(stok, "Array type").toLowerCase();

		// read in OID(object name)
		arrayName = AnimalParseSupport.parseText(stok, "Array object name");

		// a helper to prevent too many String operations
		String basicFeedbackTag = localType + " '" + arrayName + "' ";

		// Parse deprecated array tag
		ParseSupport.parseOptionalWord(stok, basicFeedbackTag + " deprecated 'at'",
				"at");
		basePoint = AnimalParseSupport.parseNodeInfo(stok, basicFeedbackTag
				+ "start node", null);

		// parse and set the colors for the array outline, fill color and element
		// color
		arrayColor = AnimalParseSupport.parseAndSetColor(stok, arrayName, "color",
				"black");
		fillColor = AnimalParseSupport.parseAndSetColor(stok, arrayName,
				"fillColor", "white");
		elementColor = AnimalParseSupport.parseAndSetColor(stok, arrayName,
				"elementColor", "black");

		AnimalParseSupport
				.parseAndSetColor(stok, arrayName, "elemHighlight", "red");
		AnimalParseSupport.parseAndSetColor(stok, arrayName, "cellHighlight",
				"yellow");

		// parse the orientation: horizontal or vertical(default is horizontal)
		verticalMode = ParseSupport.parseOptionalWord(stok, "Array '" + arrayName
				+ "'orientation 'vertical'", "vertical");

		if (!verticalMode) {
			ParseSupport.parseOptionalWord(stok, basicFeedbackTag
					+ "orientation 'horizontal'", "horizontal");
		}

		// now parse the elements. First, we need to know the array length!
		ParseSupport.parseMandatoryWord(stok,
				basicFeedbackTag + "Keyword 'length'", "length");
		arrayLength = ParseSupport.parseInt(stok, basicFeedbackTag + "length(>=1)",
				1);

		entryIDs = new int[arrayLength << 1];

		components = new StringBuilder(arrayLength << 3);

		// parse the elements contained in the array
		elements = new String[arrayLength];

		for (i = 0; i < arrayLength; i++)
			elements[i] = AnimalParseSupport.parseText(stok, basicFeedbackTag
					+ "element #" + i, null, false, chosenLanguage);

		getObjectProperties().put(arrayName + ".length", arrayLength);

		// parse the depth of the array elements. Note that the outline and
		// fillColor are
		// one level below the array elements(i.e., have a depth greater by 1)
		basicDepth = Integer.MAX_VALUE - 4;

		if (ParseSupport.parseOptionalWord(stok, "'depth' tag for Array "
				+ arrayName, "depth")) {
			basicDepth = ParseSupport.parseInt(stok, "Array '" + arrayName
					+ "'  depth(>=0))", 0);
		}

		// parse the basic delay for displaying the elements.
		String unit = "ticks";

		// check for offset keyword 'after'
		if (ParseSupport.parseOptionalWord(stok, basicFeedbackTag
				+ "keyword 'after'", "after")) {
			// parse the offset
			displayDelay = ParseSupport.parseInt(stok, basicFeedbackTag + "delay", 0);

			// parse unit
			unit = ParseSupport.parseWord(stok, basicFeedbackTag + "delay unit");
		}

		// Check for optional keyword "cascaded"
		cascadedDisplay = ParseSupport.parseOptionalWord(stok, basicFeedbackTag
				+ "keyword 'cascaded'", "cascaded");

		// Check if keyword 'within' is given
		if (cascadedDisplay) {
			if (ParseSupport.parseOptionalWord(stok, basicFeedbackTag
					+ "keyword 'within'", "within")) {
				// parse time - must be at least 0!
				displayDuration = ParseSupport.parseInt(stok, basicFeedbackTag
						+ "duration", 0);

				// if there was no "after", also parse units
				if (displayDelay == 0) {
					unit = ParseSupport.parseWord(stok, basicFeedbackTag
							+ "duration unit");
				} else {
					if (ParseSupport.parseOptionalWord(stok, basicFeedbackTag
							+ "duration unit", "ticks")) {
						unit = "ticks";
					} else if (ParseSupport.parseOptionalWord(stok, basicFeedbackTag
							+ "duration unit", "ms")) {
						unit = "ms";
					}
				}
			} else {
				displayDuration = arrayLength
						* ((unit.equalsIgnoreCase("ticks")) ? 10 : 200);
			}
		}
		boolean isHidden = ParseSupport.parseOptionalWord(stok, "hidden", "hidden");
		oids = new int[((displayDuration == 0) ? (arrayLength << 1) : 2)];

		Font f = AnimalParseSupport.parseFontInfo(stok, "array");

		ArrayProducer.makeArray(arrayName, basePoint, arrayColor, fillColor,
				elementColor, verticalMode, arrayLength, elements, basicDepth,
				displayDelay, cascadedDisplay, displayDuration, oids, entryIDs,
				components, f, unit, isHidden, stok);
	}

	/**
	 * Create a array from the description read from the StreamTokenizer. The
	 * description is usually generated by other programs and dumped in a file or
	 * on System.out.
	 */
	public void parseArrayMarkerInput() throws IOException {
		// int i; // for loops
		String localType; // array type and name
		String markerName; // array type and name
		String markerLabel = null; // array type and name
		PTText labelText = null;

		// read in object type
		localType = ParseSupport.parseWord(stok, "Array marker type").toLowerCase();

		// read in OID(object name)
		markerName = AnimalParseSupport.parseText(stok, "Array marker name");

		// a helper to prevent too many String operations
		String basicTag = localType + " '" + markerName + "' ";

		// check for mandatory keyword "on [arrayID]"
		ParseSupport.parseMandatoryWord(stok, basicTag + "keyword 'on'", "on");

		// Parse the array OID
		String targetObjectName = AnimalParseSupport.parseText(stok, basicTag
				+ "base array name");
		int[] targetOIDs = getObjectIDs().getIntArrayProperty(targetObjectName);

		if ((targetOIDs == null)
				|| !getObjectTypes().getProperty(targetObjectName).equals(
						getTypeIdentifier("array"))) {
			ParseSupport.formatException("Target object '" + targetObjectName
					+ "' unknown or not an array.", stok);
		}

		int maxLength = getObjectProperties().getIntProperty(
				targetObjectName + ".length", -1);

		if (maxLength < 1) {
			ParseSupport.formatException("Invalid number of elements in array '"
					+ targetObjectName + "'.", stok);
		}

		// parse the index where the element is to be placed
		ParseSupport.parseMandatoryWord(stok, basicTag + "keyword 'atIndex'",
				"atIndex");

		int targetIndex = ParseSupport.parseInt(stok, basicTag
				+ "target index([0, " + (maxLength - 1) + "])", 0, maxLength - 1);

		if (ParseSupport.parseOptionalWord(stok, basicTag
				+ "optional keyword 'label'", "label")) {
			markerLabel = AnimalParseSupport.parseText(stok, basicTag + "label",
					null, false, chosenLanguage);
		}

		int length = 40;

		if (ParseSupport.parseOptionalWord(stok, basicTag
				+ "optional keyword 'short'", "short")) {
			length = 20;
		} else if (ParseSupport.parseOptionalWord(stok, basicTag
				+ "optional keyword 'long'", "long")) {
			length = 60;
		} else if (ParseSupport.parseOptionalWord(stok, basicTag
				+ "optional keyword 'normal'", "normal")) {
			length = 40;
		}

		// int indexOID = getObjectIDs().getIntProperty(targetObjectName
		// +"["+targetIndex +"].box");
		int indexOID = targetOIDs[maxLength + targetIndex];

		// parse optional index marker color
		Point[] coords = new Point[2];

		// now determine the basic object's bounding box
		PTGraphicObject ptgo = animState.getCloneByNum(indexOID);

		// retrieve the bounding box
		Rectangle boundingBox = ptgo.getBoundingBox();

		if (getObjectProperties().getProperty(targetObjectName + ".orientation")
				.equals("horizontal")) {
			coords[1] = new Point(boundingBox.x + (boundingBox.width >>> 1),
					boundingBox.y);
			coords[0] = new Point(coords[1].x, boundingBox.y - length);
		} else {
			coords[1] = new Point(boundingBox.x, boundingBox.y
					+ (boundingBox.height >>> 1));
			coords[0] = new Point(boundingBox.x - length, coords[1].y);
		}

		PTPolyline indexMarker = new PTPolyline(coords);
		indexMarker.setObjectName(markerLabel + ".ptr");
		indexMarker.setFWArrow(true);
		indexMarker.setBWArrow(false);
		indexMarker.setColor(AnimalParseSupport.parseAndSetColor(stok, localType,
				"color"));
		AnimalParseSupport.parseAndSetDepth(stok, indexMarker, localType);

		if (markerLabel != null) {
			// Font f = new Font("Serif", Font.PLAIN, 16);
			Font f = AnimalParseSupport.parseFontInfo(stok, "arrayIndex");
			labelText = new PTText(markerLabel, f);
			labelText.setObjectName(markerLabel);
			labelText.setColor(indexMarker.getColor());

			FontMetrics fm = Animal.getConcreteFontMetrics(f);

			// Toolkit.getDefaultToolkit().getFontMetrics(f);
			int textWidth = fm.stringWidth(markerLabel);

			if (getObjectProperties().getProperty(targetObjectName + ".orientation")
					.equals("horizontal")) {
				labelText.setPosition(new Point(coords[0].x - (textWidth >>> 1),
						coords[0].y - 5));
			} else {
				labelText.setPosition(new Point(coords[0].x - textWidth - 5,
						coords[0].y + 8));
			}

			BasicParser.addGraphicObject(labelText, anim);
			getObjectIDs().put(markerName + ".label", labelText.getNum(false));
		}

		BasicParser.addGraphicObject(indexMarker, anim);

		String markerID = String.valueOf(indexMarker.getNum(false));
		String ids = (labelText == null) ? markerID : (markerID + " " + String
				.valueOf(labelText.getNum(false)));

		if (getObjectProperties().getProperty(targetObjectName + ".ptrs") != null) {
			getObjectProperties().put(
					targetObjectName + ".ptrs",
					getObjectProperties().getProperty(targetObjectName + ".ptrs") + " "
							+ markerName);
		} else {
			getObjectProperties().put(targetObjectName + ".ptrs", markerName);
		}

		StringBuffer sb = new StringBuffer(32);
		if (dependentObjects.containsKey(targetObjectName))
		  sb.append(dependentObjects.get(targetObjectName)).append(' ');
		sb.append(ids);
		dependentObjects.put(targetObjectName, sb.toString());
//		System.err.println("++now dependent on '" +targetObjectName +": " + sb.toString());
		getObjectIDs().put(markerName, ids);
		getObjectIDs().put(targetObjectName,
				getObjectIDs().getProperty(targetObjectName) + ' ' + ids);
		getObjectProperties().put(markerName + ".target", targetObjectName);
		getObjectProperties().put(markerName + ".pos", targetIndex);
		getObjectTypes().put(markerName, getTypeIdentifier("arraymarker"));
		AnimalParseSupport.showComponents(stok, ids, localType, true);
	}

	/**
	 * Parse operations for putting a new element in the array
	 * 
	 * @exception IOException
	 *              if a parsing error occurs
	 */
	public void parseArrayPut() throws IOException {
		// arrayPut "value" on "arrayName" position i [color] [delay]
		ParseSupport.parseWord(stok, "data structure-specific operator"); // stok.sval;
		String arrayName = null;
		// determine value to be put
		String objectValue = AnimalParseSupport.parseText(stok, "arrayPut value",
				null, false, chosenLanguage);
		// System.out.println("@parseArrayPut, objectValue: " +objectValue);

		// parse keyword 'on'
		ParseSupport.parseMandatoryWord(stok, "arrayPut keyword 'on'", "on");

		// parse the array name itself
		arrayName = AnimalParseSupport.parseText(stok, "arrayPut base array");

		// System.out.println("array Name: " +arrayName);
		// retrieve IDs for array
		String newIDs = getObjectIDs().getProperty(arrayName);
		int[] arrayIDs = getObjectIDs().getIntArrayProperty(arrayName);

		// check name(registered, getTypeIdentifier("array") type)
		if ((arrayIDs == null)
				|| !(getObjectTypes().getProperty(arrayName)
						.equals(getTypeIdentifier("array")))) {
			ParseSupport.formatException("Target object '" + arrayName
					+ "' unknown or not an array.", stok);
		}

		// parse the keyword 'position'
		ParseSupport.parseMandatoryWord(stok, "arrayPut keyword 'position'",
				"position");

		// parse the target position and check if it is valid
		int maxLength = getObjectProperties().getIntProperty(arrayName + ".length",
				-1);
		// System.out.println("max length: " +maxLength);
		// getObjectProperties().list(System.out);

		if (maxLength < 1) {
			ParseSupport.formatException("Invalid number of elements in array '"
					+ arrayName + "'.", stok);
		}

		int targetPos = ParseSupport.parseInt(stok, "arrayPut target pos [0, "
				+ (maxLength - 1) + "]", 0, maxLength - 1);

		// generate a default entry for text display
		String defaultEntry = arrayName + "[" + targetPos + "]";

		// generate the new entry and its new bounding box and set its color
		// Font f = new Font("Serif", Font.PLAIN, 16);
		Font f = getObjectProperties().getFontProperty(arrayName + ".font");
		FontMetrics fm = Animal.getConcreteFontMetrics(f);

		// Toolkit.getDefaultToolkit().getFontMetrics(f);
		// determine the with of the entry
		int textWidth = fm.stringWidth(objectValue);
		int originalArrayCellID = arrayIDs[maxLength + targetPos];

		// retrieve the old bounding box!
//		 System.out.println("arrayCellID: " +originalArrayCellID);
//		 System.out.println("ptgo: "
//		 +animState.getCloneByNum(originalArrayCellID));
		// System.out.println("animState: " +animState);
		PTGraphicObject ptgo = animState.getCloneByNum(originalArrayCellID);
		Rectangle originalArrayCellBB = ptgo.getBoundingBox();

		boolean verticalMode = getObjectProperties().getProperty(
				arrayName + ".orientation").equalsIgnoreCase("vertical");

		// generate "hide" for old elements
		TimedShow hideOldElement = new TimedShow(currentStep, arrayIDs[targetPos],
				0, null, false);
		AnimalParseSupport.parseTiming(stok, hideOldElement, "hide");
		BasicParser.addAnimatorToAnimation(hideOldElement, anim);
		getCurrentlyVisible().put(String.valueOf(arrayIDs[targetPos]), "false");

		// generate new element
		PTText newValue = new PTText(objectValue, f);
		newValue.setObjectName(defaultEntry);
		PTText ptgoText = (PTText)animState.getCloneByNum(arrayIDs[targetPos]);
		//        System.err.println("oldText: " +ptgoText.getPosition() + " /" +ptgoText.getLocation());
		//		newValue.setLocation(new Point(originalArrayCellBB.x + 3,
		//				(originalArrayCellBB.height + originalArrayCellBB.y) - 8));
		newValue.setLocation(ptgoText.getLocation());
		//        System.err.println("newText: " +newValue.getLocation() +" / " +newValue.getPosition()
		//            +"\n\t" + ptgo.getBoundingBox());
		newValue.setColor(getObjectProperties().getColorProperty(
		    arrayName + ".elementColor", Color.black));
		newValue.setDepth(ptgo.getDepth() - 1);
		BasicParser.addGraphicObject(newValue, anim);
		getObjectIDs().put(defaultEntry, newValue.getNum(false));

		arrayIDs[targetPos] = newValue.getNum(false);
//		System.err.println("targetPos: " + targetPos + ", ID query: " +defaultEntry);
//		System.err.println("Text ID at " +targetPos +" before put: " +originalArrayCellID +", object: " +ptgo);
//    System.err.println("new object ID: " +newValue.getNum(false) + " / @pos: " +getObjectIDs().get(defaultEntry));
//    System.err.println("IDs before put: " + getObjectIDs().get(arrayName));
		// update the properties
		getObjectIDs().put(arrayName, arrayIDs);
//		System.err.println("IDs after put: " + getObjectIDs().get(arrayName));
		getObjectTypes().put(defaultEntry, getTypeIdentifier("text"));

		// oids[0] = newValue.getNum(false);
		TimedShow showNewElement = new TimedShow(currentStep, newValue
				.getNum(false), 0, null, true);
		showNewElement.copyTimingFrom(hideOldElement);
		BasicParser.addAnimatorToAnimation(showNewElement, anim);
		getCurrentlyVisible().put(String.valueOf(newValue.getNum(false)), "true");

		// determine if update is necessary!
		// boolean mustUpdateBox = (originalArrayCellBB.width != (textWidth + 5));

		if (!verticalMode) {
			updateHArrayAfterPut(originalArrayCellBB, textWidth, originalArrayCellID,
					arrayName, targetPos, maxLength, arrayIDs, hideOldElement);
		} else {
			updateVArrayAfterPut(originalArrayCellBB, textWidth, originalArrayCellID,
					arrayName, targetPos, maxLength, arrayIDs, hideOldElement, newValue);
		}

		String unparsed = ParseSupport.consumeIncludingEOL(stok,
				"ds specific stuff");

		if (unparsed.length() > 1) {
			System.err.println("#Left unparsed...: '" + unparsed + "' @AS:568 line " + stok.lineno());
		}

		stok.pushBack();
	} // parseArrayPut

	/**
	 * Parse data structure-specific operations, such as array element swapping
	 * 
	 */
	public void parseArraySwap() throws IOException {
		int i;
		String localType = ParseSupport.parseWord(stok,
				"data structure-specific operator"); // stok.sval;
		String arrayName = null;

		// arraySwap on "baseArray" position i with j [timing]
		ParseSupport.parseMandatoryWord(stok, localType + " keyword 'on'", "on");
		arrayName = AnimalParseSupport.parseText(stok, localType
				+ " base array name");

		int[] arrayIDs = getObjectIDs().getIntArrayProperty(arrayName);

		// int[] targetOIDs = getObjectIDs().getIntArrayProperty(arrayName);
		ParseSupport.parseMandatoryWord(stok, localType + " keyword 'position'",
				"position");

		int maxLength = getObjectProperties().getIntProperty(arrayName + ".length");
		int firstSwapPos = ParseSupport.parseInt(stok, localType
				+ " first position", 0, maxLength - 1);
		ParseSupport
				.parseMandatoryWord(stok, localType + " keyword 'with'", "with");

		int secondSwapPos = ParseSupport.parseInt(stok, localType
				+ " second position", 0, maxLength - 1);
		int minPos = (firstSwapPos < secondSwapPos) ? firstSwapPos : secondSwapPos;
		int maxPos = (firstSwapPos > secondSwapPos) ? firstSwapPos : secondSwapPos;
		int minElemID = getObjectIDs().getIntProperty(
				arrayName + "[" + minPos + "]");
		int maxElemID = getObjectIDs().getIntProperty(
				arrayName + "[" + maxPos + "]");
		int minBoxID = getObjectIDs().getIntProperty(
				arrayName + "[" + minPos + "].box");
		int maxBoxID = getObjectIDs().getIntProperty(
				arrayName + "[" + maxPos + "].box");
		PTGraphicObject ptgo = animState.getCloneByNum(minBoxID);
		Rectangle minBB = ptgo.getBoundingBox();
		ptgo = animState.getCloneByNum(maxBoxID);

		Rectangle maxBB = ptgo.getBoundingBox();

		boolean horizontalMode = getObjectProperties().getProperty(
				arrayName + ".orientation").equals("horizontal");

		// swap the two array cell boxes with no duration!
		Point[] boxExchanger = new Point[2];
		boxExchanger[0] = minBB.getLocation();

		if (horizontalMode) {
			boxExchanger[1] = new Point((maxBB.x + maxBB.width) - minBB.width,
					maxBB.y);
		} else {
			boxExchanger[1] = new Point(maxBB.x, (maxBB.y + maxBB.height)
					- minBB.height);
		}

		// move the cell with smaller index to the right
		PTPolyline boxMover = new PTPolyline(boxExchanger);
		boxMover.setObjectName("boxMoveLine1");

		int[] moveIDs = new int[1];
		moveIDs[0] = minBoxID;
		BasicParser.addGraphicObject(boxMover, anim);

		// generate the animator
		Move move = new Move(currentStep, moveIDs, 0, "translate", boxMover
				.getNum(false));
		AnimalParseSupport.parseTiming(stok, move, localType + " operation");

		boolean ticksMode = move.isUnitIsTicks();
		int durationOfWholeOperation = move.getDuration(); // store for later!
		int offsetForWholeOperation = move.getOffset(); // store for later!
		move.setDuration(0); // this must take place "at once" - but *after* the
													// offset!
		BasicParser.addAnimatorToAnimation(move, anim); // insert this operation

		// update the properties
		getObjectIDs().put(arrayName + "[" + maxPos + "].box", minBoxID);

		// move the cell with higher index to the left
		boxExchanger[0] = maxBB.getLocation();
		boxExchanger[1] = minBB.getLocation();

		boxMover = new PTPolyline(boxExchanger);
		boxMover.setObjectName("boxMoveLine2");
		BasicParser.addGraphicObject(boxMover, anim);
		moveIDs = new int[1];
		moveIDs[0] = maxBoxID;
		move = new Move(currentStep, moveIDs, 0, "translate", boxMover
				.getNum(false));
		move.setOffset(offsetForWholeOperation);
		move.setUnitIsTicks(ticksMode);
		move.setDuration(durationOfWholeOperation);
		BasicParser.addAnimatorToAnimation(move, anim);

		// update the properties
		getObjectIDs().put(arrayName + "[" + minPos + "].box", maxBoxID);

		// update the element IDs
		arrayIDs[maxLength + minPos] = maxBoxID;
		arrayIDs[maxLength + maxPos] = minBoxID;

		// adapt the cells between 'minPos' and 'maxPos'
		if (maxPos > minPos) {
			StringBuilder fullUpdateIDs = new StringBuilder(200);
			StringBuilder halfUpdateIDs = new StringBuilder(50);
			int[] fullIDs = null;
			int[] halfIDs = null;

			for (i = minPos + 1; i < maxPos; i++) {
				fullUpdateIDs.append(
						getObjectIDs().getIntProperty(arrayName + "[" + i + "].box"))
						.append(' ');
				fullUpdateIDs.append(
						getObjectIDs().getIntProperty(arrayName + "[" + i + "]")).append(
						' ');
			}

			String registeredMarkers = getObjectProperties().getProperty(
					arrayName + ".ptrs");

			if (registeredMarkers != null) {
				StringTokenizer stringTok = new StringTokenizer(registeredMarkers);

				while (stringTok.hasMoreTokens()) {
					String currentPointerName = stringTok.nextToken();
					int currentPointerPos = getObjectProperties().getIntProperty(
							currentPointerName + ".pos");

					if ((currentPointerPos >= minPos) && (currentPointerPos <= maxPos)) {
						int[] pointerIDs = getObjectIDs().getIntArrayProperty(
								currentPointerName);

						if ((currentPointerPos != minPos) && (currentPointerPos != maxPos)) {
							if (pointerIDs.length == 2) {
								fullUpdateIDs.append(pointerIDs[1]).append(" ");
							}

							fullUpdateIDs.append(pointerIDs[0]).append(" ");
						} else {
							if (pointerIDs.length == 2) {
								halfUpdateIDs.append(pointerIDs[1]).append(" ");
							}

							halfUpdateIDs.append(pointerIDs[0]).append(" ");
						}
					}
				}

				stringTok = new StringTokenizer(fullUpdateIDs.toString());

				int n = stringTok.countTokens();

				if (n > 0) {
					fullIDs = new int[n];

					for (i = 0; i < n; i++)
						try {
							fullIDs[i] = Integer.valueOf(stringTok.nextToken()).intValue();
						} catch (NumberFormatException e) {
							System.err.println("Could not determine int #" + i + "/"
									+ fullUpdateIDs.toString());
						}
				}

				stringTok = new StringTokenizer(halfUpdateIDs.toString());
				n = stringTok.countTokens();

				if (n > 0) {
					halfIDs = new int[n];

					for (i = 0; i < n; i++)
						try {
							halfIDs[i] = Integer.valueOf(stringTok.nextToken()).intValue();
						} catch (NumberFormatException e) {
							System.err.println("Could not determine int #" + i + "/"
									+ halfUpdateIDs.toString());
						}
				}
			}

			updateArrayBoxesAndPointers(fullIDs, halfIDs, minBB, maxBB, 
					offsetForWholeOperation, move.isUnitIsTicks());
		}

		Point[] firstMovePoints = new Point[4];
		Point[] secondMovePoints = new Point[4];
		firstMovePoints[0] = minBB.getLocation();
		secondMovePoints[0] = maxBB.getLocation();
		secondMovePoints[3] = minBB.getLocation();

		if (horizontalMode) {
			// move first(to right): up, right, down
			firstMovePoints[3] = new Point((maxBB.x + maxBB.width) - minBB.width,
					maxBB.y);
			firstMovePoints[1] = new Point(firstMovePoints[0].x, firstMovePoints[0].y
					- minBB.height - 5);
			firstMovePoints[2] = new Point(firstMovePoints[3].x, firstMovePoints[1].y);

			// move second(to left): down, left, up
			secondMovePoints[1] = new Point(secondMovePoints[0].x,
					secondMovePoints[0].y + maxBB.height + 5);
			secondMovePoints[2] = new Point(secondMovePoints[3].x,
					secondMovePoints[1].y);
		} else {
			// move first(down right): right, down, left
			firstMovePoints[3] = new Point(maxBB.x, (maxBB.y + maxBB.height)
					- minBB.height);
			firstMovePoints[1] = new Point(firstMovePoints[0].x + minBB.width + 5,
					firstMovePoints[0].y);
			firstMovePoints[2] = new Point(firstMovePoints[1].x, firstMovePoints[3].y);

			// move second(up): left, up, right
			secondMovePoints[1] = new Point(secondMovePoints[0].x - maxBB.width - 5,
					secondMovePoints[0].y);
			secondMovePoints[2] = new Point(secondMovePoints[1].x,
					secondMovePoints[3].y);
		}

		boxMover = new PTPolyline(firstMovePoints);
		boxMover.setObjectName("boxMoveLine3");
		moveIDs = new int[1];
		moveIDs[0] = minElemID;
		BasicParser.addGraphicObject(boxMover, anim);
		move = new Move(currentStep, moveIDs, durationOfWholeOperation,
				"translate", boxMover.getNum(false));
		move.setOffset(offsetForWholeOperation);
		move.setUnitIsTicks(ticksMode);
		move.setDuration(durationOfWholeOperation);
		BasicParser.addAnimatorToAnimation(move, anim);

		boxMover = new PTPolyline(secondMovePoints);
		boxMover.setObjectName("boxMoveLine4");
		moveIDs = new int[1];
		moveIDs[0] = maxElemID;
		BasicParser.addGraphicObject(boxMover, anim);
		move = new Move(currentStep, moveIDs, durationOfWholeOperation,
				"translate", boxMover.getNum(false));
		move.setOffset(offsetForWholeOperation);
		move.setUnitIsTicks(ticksMode);
		move.setDuration(durationOfWholeOperation);
		BasicParser.addAnimatorToAnimation(move, anim);

		// update the properties
		arrayIDs[minPos] = maxElemID;
		arrayIDs[maxPos] = minElemID;
		getObjectIDs().put(arrayName, arrayIDs);
		getObjectIDs().put(arrayName + "[" + minPos + "]", maxElemID);
		getObjectIDs().put(arrayName + "[" + maxPos + "]", minElemID);

		String unparsed = ParseSupport.consumeIncludingEOL(stok,
				"ds specific stuff");

		if (unparsed.length() > 1) {
			System.err.println("#Left unparsed...: '" + unparsed + "' @AS:832 line "  + stok.lineno());
		}

		stok.pushBack();
	} // parseArraySwap

	/**
	 * Create a ColorChange from the description read from the StreamTokenizer.
	 * The description is usually generated by other programs and dumped in a file
	 * or on System.out.
	 */
	public void parseColorChange() throws IOException {
		// parse exact command
		String colorType = ParseSupport.parseWord(stok, "color change operation");
		// boolean contextMode = false;
		ColorChanger colChanger = null;
		Color c = null;
		int[] targetOIDs = null;
		boolean elemMode = colorType.toLowerCase().endsWith("elem");
		boolean highlightMode = colorType.toLowerCase().startsWith("high");
		// String targetProperty = null;

		ParseSupport.parseMandatoryWord(stok, colorType + " keyword 'on'", "on");

		String targetArray = AnimalParseSupport.parseText(stok, colorType
				+ " array name");

		if (ParseSupport.parseOptionalWord(stok, colorType + " keyword 'position'",
				"position")) {
			if (getObjectProperties().getIntProperty(targetArray + ".length", -2) == -2) {
				XProperties helper = getObjectProperties().getElementsForPrefix(
						targetArray);
				helper.list(System.out);
			}
			int targetLine = ParseSupport.parseInt(stok, colorType + " array index",
					0, getObjectProperties().getIntProperty(targetArray + ".length", 0));
			targetOIDs = new int[1];
			targetOIDs[0] = getObjectIDs().getIntProperty(
					targetArray + "[" + targetLine + ((elemMode) ? "]" : "].box"));

		} else {
			int fromRange = 0;
			int toRange = getObjectProperties().getIntProperty(
					targetArray + ".length") - 1;

			if (ParseSupport.parseOptionalWord(stok, colorType
					+ " cell/element range keyword 'from'", "from")) {
				fromRange = ParseSupport.parseInt(stok, colorType + " array index", 0,
						getObjectProperties().getIntProperty(targetArray + ".length"));
			}

			if (ParseSupport.parseOptionalWord(stok, colorType
					+ " cell/element range keyword 'to'", "to")) {
				toRange = ParseSupport.parseInt(stok, colorType + " array index",
						fromRange, getObjectProperties().getIntProperty(
								targetArray + ".length"));
			}

			targetOIDs = new int[toRange - fromRange + 1];

			for (int i = 0; fromRange <= toRange; fromRange++, i++)
				targetOIDs[i] = getObjectIDs().getIntProperty(
						targetArray + "[" + fromRange + ((elemMode) ? "]" : "].box"));
		}

		if (elemMode) {
			if (highlightMode) {
				c = getObjectProperties().getColorProperty(
						targetArray + ".elemHighlight", Color.blue);
			} else {
				c = getObjectProperties().getColorProperty(
						targetArray + ".elementColor", Color.black);
			}
		} else {
			if (highlightMode) {
				c = getObjectProperties().getColorProperty(
						targetArray + ".cellHighlight", Color.yellow);
			} else {
				c = getObjectProperties().getColorProperty(targetArray + ".fillColor",
						Color.white);
			}
		}

		colChanger = new ColorChanger(currentStep, targetOIDs, 0,
				(elemMode) ? "color" : "fillColor", c);

		// parse optional timing - is set within the method!
		AnimalParseSupport.parseTiming(stok, colChanger, "Color");

		// insert into list of animators
		BasicParser.addAnimatorToAnimation(colChanger, anim);
	}

	/**
	 * Create a Move from the description read from the StreamTokenizer. The
	 * description is usually generated by other programs and dumped in a file or
	 * on System.out.
	 */
	public void parseMovePointer() throws IOException {
		String localType = ParseSupport.parseWord(stok, "move pointer keyword");

		String targetIDName = AnimalParseSupport.parseText(stok,
				"index marker name");
		int[] targetOIDs = getObjectIDs().getIntArrayProperty(targetIDName);
		PTPolyline moveLine = null;
		PTGraphicObject ptgo = animState.getCloneByNum(targetOIDs[0]);
		String baseElementName = getObjectProperties().getProperty(
				targetIDName + ".target");

		// calculate target coordinates
		Rectangle boundingBox = ptgo.getBoundingBox();
		Point[] points = new Point[2];
		points[0] = new Point(boundingBox.x + boundingBox.width, boundingBox.y
				+ boundingBox.height);

		if ((targetOIDs == null)
				|| !(getObjectTypes().getProperty(targetIDName)
						.equals(getTypeIdentifier("arraymarker")))
				|| !(getObjectTypes().getProperty(baseElementName)
						.equals(getTypeIdentifier("array")))) {
			ParseSupport.formatException("invalid or unknown array ID: "
					+ targetIDName, stok);
		}

		ParseSupport.parseMandatoryWord(stok, "index marker keyword 'to'", "to");

		int maxIndex = getObjectProperties().getIntProperty(
				baseElementName + ".length", 0) - 1;

		// Toggle possible modes
		if (ParseSupport.parseOptionalWord(stok, "index marker keyword 'position'",
				"position")) {
			int targetPos = ParseSupport.parseInt(stok,
					"index marker position value [0, " + maxIndex + "]", 0, maxIndex);

			// determine the basic object's bounding box
			int targetElementOID = getObjectIDs().getIntProperty(
					baseElementName + "[" + targetPos + "].box", -1);
			ptgo = animState.getCloneByNum(targetElementOID);

			// retrieve the bounding box
			boundingBox = ptgo.getBoundingBox();

			if (getObjectProperties().getProperty(baseElementName + ".orientation")
					.equals("horizontal")) {
				points[1] = new Point(boundingBox.x + (boundingBox.width >>> 1),
						boundingBox.y);
			} else {
				points[1] = new Point(boundingBox.x, boundingBox.y
						+ (boundingBox.height >>> 1));
			}

			moveLine = new PTPolyline(points);
			getObjectProperties().put(targetIDName + ".pos", targetPos);
		} else if (ParseSupport.parseOptionalWord(stok,
				"index marker keyword 'arrayEnd'", "arrayEnd")) {
			// determine the basic object's bounding box
			int targetElementOID = getObjectIDs().getIntProperty(
					baseElementName + "[" + maxIndex + "].box", -1);

			ptgo = animState.getCloneByNum(targetElementOID);

			// retrieve the bounding box
			boundingBox = ptgo.getBoundingBox();

			if (getObjectProperties().getProperty(baseElementName + ".orientation")
					.equals("horizontal")) {
				points[1] = new Point(boundingBox.x + (boundingBox.width >>> 1),
						boundingBox.y);
			} else {
				points[1] = new Point(boundingBox.x, boundingBox.y
						+ (boundingBox.height >>> 1));
			}

			moveLine = new PTPolyline(points);
		} else if (ParseSupport.parseOptionalWord(stok,
				"index marker keyword 'outside'", "outside")) {
			// determine the basic object's bounding box
			int targetElementOID = getObjectIDs().getIntProperty(
					baseElementName + "[" + maxIndex + "].box", -1);
			ptgo = animState.getCloneByNum(targetElementOID);

			// retrieve the bounding box
			boundingBox = ptgo.getBoundingBox();

			if (getObjectProperties().getProperty(baseElementName + ".orientation")
					.equals("horizontal")) {
				points[1] = new Point(boundingBox.x + boundingBox.width + 20,
						boundingBox.y);
			} else {
				points[1] = new Point(boundingBox.x, boundingBox.y + boundingBox.height
						+ 20);
			}

			moveLine = new PTPolyline(points);
		} else {
			ParseSupport.formatException("invalid keyword for array marker move",
					stok);
		}

		int[] oid = targetOIDs;
		moveLine.setObjectName("moveLine3");

		// insert target point into list of graphic objects
		BasicParser.addGraphicObject(moveLine, anim);

		// set the numeric ID of the move target
		int moveBaseNum = moveLine.getNum(false);

		// generate a generic move(instanteous jump)
		Move move = new Move(currentStep, oid, 0, "translate", moveBaseNum);

		// parse optional timing - is set within the method!
		AnimalParseSupport.parseTiming(stok, move, "Array Marker Move");

		// if "jump", erase possible mention of 'duration'
		if (localType.equalsIgnoreCase("jump")) {
			if (move.getDuration() != 0) {
				MessageDisplay.message("'jump' can not have a duration - use 'move'"
						+ " instead in line " + stok.lineno());
				move.setDuration(0);
			}
		}

		// insert into list of animators
		BasicParser.addAnimatorToAnimation(move, anim);
	}

	private void updateHArrayAfterPut(Rectangle originalArrayCellBB,
			int textWidth, int originalArrayCellID, String arrayName, int targetPos,
			int maxLength, int[] arrayIDs, TimedShow hideOldElement) {
		// determine how to adapt the old box...
		Point[] coords = {
				new Point(originalArrayCellBB.x + originalArrayCellBB.width,
						originalArrayCellBB.y + originalArrayCellBB.height + 50),
				new Point(originalArrayCellBB.x + textWidth + 5, originalArrayCellBB.y
						+ originalArrayCellBB.height + 50) };
		PTPolyline oldBoxMove = new PTPolyline(coords);
		// System.err.println("move: (" + coords[0] + ", " + coords[1] + ")");
		BasicParser.addGraphicObject(oldBoxMove, anim);

		int[] moveIDs = { originalArrayCellID };
		Move move = new Move(currentStep, moveIDs, 0, "translateNodes 3 4",
				oldBoxMove.getNum(false));
		move.copyTimingFrom(hideOldElement);
		BasicParser.addAnimatorToAnimation(move, anim);

		// int size = 0;
		int pos = 0;

		String registeredMarkers = getObjectProperties().getProperty(
				arrayName + ".ptrs");
		int fullPos = 0;
		int partialPos = 0;
		int[] tmpFullMoveIDs = null;
		int[] tmpPartialMoveIDs = null;

		if (registeredMarkers != null) {
			StringTokenizer stringTok = new StringTokenizer(registeredMarkers);
			tmpFullMoveIDs = new int[stringTok.countTokens() << 1];
			tmpPartialMoveIDs = new int[stringTok.countTokens() << 1];

			while (stringTok.hasMoreTokens()) {
				String currentPointerName = stringTok.nextToken();
				int currentPointerPos = getObjectProperties().getIntProperty(
						currentPointerName + ".pos");

				if (currentPointerPos >= targetPos) {
					int[] pointerIDs = getObjectIDs().getIntArrayProperty(
							currentPointerName);

					if (currentPointerPos > targetPos) {
						if (pointerIDs.length == 2) {
							tmpFullMoveIDs[fullPos++] = pointerIDs[1];
						}

						tmpFullMoveIDs[fullPos++] = pointerIDs[0];
					} else {
						if (pointerIDs.length == 2) {
							tmpPartialMoveIDs[partialPos++] = pointerIDs[1];
						}

						tmpPartialMoveIDs[partialPos++] = pointerIDs[0];
					}
				}
			}
		}

		int moveIDSize = ((maxLength - targetPos - 1) << 1) + fullPos;

		if (moveIDSize > 0) {
			moveIDs = new int[((maxLength - targetPos - 1) << 1) + fullPos];
			pos = 0;

			int i;

			for (i = targetPos + 1; i < maxLength; i++)
				moveIDs[pos++] = arrayIDs[i];

			// update array cells after "put" object
			for (i = maxLength + targetPos + 1; i < (maxLength << 1); i++)
				moveIDs[pos++] = arrayIDs[i];

			for (i = 0; i < fullPos; i++)
				moveIDs[pos++] = tmpFullMoveIDs[i];

			move = new Move(currentStep, moveIDs, 0, "translate", oldBoxMove
					.getNum(false));
			move.copyTimingFrom(hideOldElement);
			BasicParser.addAnimatorToAnimation(move, anim);
		}

		if (partialPos > 0) {
			Point[] partialMoveCoords = null;
			int dx = (textWidth + 5) - originalArrayCellBB.width;

			if (dx >= 0) {
				partialMoveCoords = new Point[] { new Point(0, 20),
						new Point(((textWidth + 5) - originalArrayCellBB.width) >>> 1, 20) };
			} else {
				partialMoveCoords = new Point[] { new Point((-dx) >>> 1, 20),
						new Point(0, 20) };
			}

			PTPolyline markerBoxMove = new PTPolyline(partialMoveCoords);
			BasicParser.addGraphicObject(markerBoxMove, anim);

			int[] actualMoveTargets = new int[partialPos];
			System.arraycopy(tmpPartialMoveIDs, 0, actualMoveTargets, 0, partialPos);
			move = new Move(currentStep, actualMoveTargets, 0, "translate",
					markerBoxMove.getNum(false));
			move.copyTimingFrom(hideOldElement);
			BasicParser.addAnimatorToAnimation(move, anim);
		}
	}

	private void updateArrayBoxesAndPointers(int[] boxIDs, int[] pointerIDs,
			Rectangle originalArrayCellBB, Rectangle newBB, 
			int offset, boolean unitIsTicks) {
		// int i;
		boolean needUpdate = (originalArrayCellBB.width != newBB.width)
				|| (originalArrayCellBB.height != newBB.height);

		if (needUpdate) {
			if (boxIDs != null) {
				Point[] boxMovePoints = new Point[2];
				boxMovePoints[0] = new Point(originalArrayCellBB.width,
						originalArrayCellBB.height);
				boxMovePoints[1] = new Point(newBB.width, newBB.height);

				PTPolyline boxMoveLine = new PTPolyline(boxMovePoints);
				boxMoveLine.setObjectName("boxMoveLine");
				BasicParser.addGraphicObject(boxMoveLine, anim);

				Move move = new Move(currentStep, boxIDs, 0, "translate", boxMoveLine
						.getNum(false));
				move.setOffset(offset);
				move.setUnitIsTicks(unitIsTicks);
				BasicParser.addAnimatorToAnimation(move, anim);
			}

			if (pointerIDs != null) {
				Point[] pointerMovePoints = new Point[2];
				pointerMovePoints[0] = new Point(originalArrayCellBB.width >>> 1,
						originalArrayCellBB.height >>> 1);
				pointerMovePoints[1] = new Point(newBB.width >>> 1, newBB.height >>> 1);

				PTPolyline pointerMoveLine = new PTPolyline(pointerMovePoints);
				pointerMoveLine.setObjectName("pointerMoveLine");
				BasicParser.addGraphicObject(pointerMoveLine, anim);

				Move move = new Move(currentStep, pointerIDs, 0, "translate",
						pointerMoveLine.getNum(false));
				move.setOffset(offset);
				move.setUnitIsTicks(unitIsTicks);
				BasicParser.addAnimatorToAnimation(move, anim);
			}
		}
	}

	private void updateVArrayAfterPut(Rectangle originalArrayCellBB,
			int textWidth, int originalArrayCellID, String arrayName, int targetPos,
			int maxLength, int[] arrayIDs, TimedShow hideOldElement, PTText newValue) {
		// PTGraphicObject tmpPTGO = animState.getCloneByNum(getObjectIDs()
		// .getIntProperty(arrayName +
		// ".bBox"));
		// System.err.println("underlying command: arrayPut(" +newValue.getText()
		// +")");
		// System.err.println("array total BB: " + tmpPTGO.getBoundingBox());
		// System.err.println("@arrayPut, old BB: " + originalArrayCellBB);
		// System.err.println("BB: " +
		// getObjectIDs().getProperty(arrayName + ".bBox"));
		// System.err.println("BB new element: " + newValue.getBoundingBox());
		// System.err.println("Location of new value: " + newValue.getLocation());

		// determine how to adapt the old box...
		int maxWidth = -1;
		// int i, maxPos = 0;
		for (int i = 0; i < maxLength; i++) {
			int valueID = getObjectIDs()
					.getIntProperty(arrayName + "[" + i + "]", -1);
			PTGraphicObject currentValue = animState.getCloneByNum(valueID);

			if ((currentValue != null)
					&& (currentValue.getBoundingBox().getWidth() > maxWidth)) {
				maxWidth = (int) currentValue.getBoundingBox().getWidth();
//				maxPos = i;
				// System.err.println("\tnew max width from element " + valueID + " (" +
				// currentValue.toString() + "): " + maxWidth);
			}
		}
		boolean checkForResize = textWidth + 5 >= maxWidth
				|| (maxWidth + 5 < originalArrayCellBB.width);
		// System.err.println("Move needed? maxWidth = " +maxWidth +", width of new
		// elem: "
		// +textWidth +", old cell was " +originalArrayCellBB.width +": "
		// +((checkForResize) ? "yes" : "no"));
		if (checkForResize) {
			Point[] coords = {
					new Point(originalArrayCellBB.width, originalArrayCellBB.height + 50),
					new Point(maxWidth + 5, // was: textWidth + 5
							originalArrayCellBB.height + 50) };
			// System.err.println(" resize: " +(coords[1].x - coords[0].x));
			PTPolyline oldBoxMove = new PTPolyline(coords);
			BasicParser.addGraphicObject(oldBoxMove, anim);

			// update for move in two cases:
			// 1. new element is larger than the largest current element
			// 2. new element narrows the whole array by overwriting the former
			// largest element
			int[] moveIDs = new int[maxLength]; // { originalArrayCellID };

			for (int i = 0; i < maxLength; i++) {
				int loopBoxID = getObjectIDs().getIntProperty(
						arrayName + "[" + i + "].box", -1);
				if (loopBoxID != -1) {
					moveIDs[i] = loopBoxID;
				}
			}

			Move move = new Move(currentStep, moveIDs, 0, "translateNodes 3 4",
					oldBoxMove.getNum(false));
			move.copyTimingFrom(hideOldElement);
			BasicParser.addAnimatorToAnimation(move, anim);
		}
	}
}