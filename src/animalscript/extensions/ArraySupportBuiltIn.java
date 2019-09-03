package animalscript.extensions;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Point;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;

import animal.animator.ColorChanger;
import animal.animator.Move;
import animal.animator.SetText;
import animal.animator.TimedShow;
import animal.graphics.PTGraphicObject;
import animal.graphics.PTPolyline;
import animal.graphics.PTStringArray;
import animal.graphics.PTText;
import animal.graphics.meta.PTArray;
import animal.main.Animal;
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
public class ArraySupportBuiltIn extends BasicParser implements
		AnimalScriptInterface {
	
	private HashMap<Integer, Object> objectMap = new HashMap<Integer, Object>();
	
	// ========================= attributes =========================
	// private ArrayProducer arrayProducer;

	/**
	 * instantiates the key class dispatcher mapping keyword to definition type
	 */
	public ArraySupportBuiltIn() {
		handledKeywords = new Hashtable<String, Object>();
		rulesHash = new XProperties();
		handledKeywords.put("array", "parseArrayInput");
		handledKeywords.put("field", "parseArrayInput");

		// rulesHash.put("array",
		// "array <id> [at] <nodeDefinition> [color <color>] [fillColor <color>]\n  [elementColor <color>] [elemHighlight <color>] [cellHighlight <color>] [vertical | horizontal] length <n+> { \"text\" } [depth <n>] [after <n> ms | ticks] [cascaded [within <n> ms | ticks]] [fontInfo]"
		// );
		// rulesHash.put("field", "see rule for 'array'");
		handledKeywords.put("arraymarker", "parseArrayMarkerInput");
		handledKeywords.put("arraypointer", "parseArrayMarkerInput");
		handledKeywords.put("arrayindex", "parseArrayMarkerInput");

		// rulesHash.put("arraymarker",
		// "arrayMarker <id> on <id> atIndex <n> [label \"label\"] [<timing>]\n#\tinsert a pointer to an array position on the target array."
		// );
		// rulesHash.put("arrayindex", "see rule for 'arrayMarker'");
		// rulesHash.put("arraypointer", "see rule for 'arrayMarker'");
		handledKeywords.put("highlightarraycell", "parseColorChange");
		handledKeywords.put("highlightarrayelem", "parseColorChange");
		handledKeywords.put("highlightarrayelement", "parseColorChange");
		handledKeywords.put("unhighlightarraycell", "parseColorChange");
		handledKeywords.put("unhighlightarrayelem", "parseColorChange");
		handledKeywords.put("unhighlightarrayelement", "parseColorChange");

		// rulesHash.put("highlightarraycell",
		// "highlightarraycell on <id> position <n> [<timing>]");
		// rulesHash.put("highlightarrayelem",
		// "highlightarrayelem on <id> position <n> [<timing>]");
		// rulesHash.put("unhighlightarraycell",
		// "unhighlightarraycell on <id> position <n> [<timing>]");
		// rulesHash.put("unhighlightarrayelem",
		// "unhighlightarrayelem on <id> position <n> [<timing>]");
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

		handledKeywords.put("arrayindices", "parseArrayIndices");

		// rulesHash.put("movemarker",
		// "moveMarker <id> to [position] <n> | arrayEnd | outside [<timing>]");
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
		// arrayProducer = new ArrayProducer();

		handledKeywords.put("setbordercolor", "parseColorSet");
		handledKeywords.put("setfillcolor", "parseColorSet");
		handledKeywords.put("settextcolor", "parseColorSet");
		handledKeywords.put("sethighlightbordercolor", "parseColorSet");
		handledKeywords.put("sethighlightfillcolor", "parseColorSet");
		handledKeywords.put("sethighlighttextcolor", "parseColorSet");
		
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
		return !sameStep; // !sameStep ||
											// !currentCommand.equalsIgnoreCase("arrayswap");
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
		// int displayDuration = 0; // the time to be skipped between displaying
		// elements
		// int[] oids; // OIDs for storing

		// int[] entryIDs = null;
		// StringBuilder components;

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

		Color elemHighlightColor = AnimalParseSupport.parseAndSetColor(stok,
				arrayName, "elemHighlight", "red");
		Color cellHighlightColor = AnimalParseSupport.parseAndSetColor(stok,
				arrayName, "cellHighlight", "yellow");

		// parse the orientation: horizontal or vertical(default is horizontal)
		verticalMode = ParseSupport.parseOptionalWord(stok, "Array '" + arrayName
				+ "'orientation 'vertical'", "vertical");

		if (!verticalMode) {
			ParseSupport.parseOptionalWord(stok, basicFeedbackTag
					+ "orientation 'horizontal'", "horizontal");
			getObjectProperties().setProperty(arrayName + ".orientation","horizontal");
		}else{
			getObjectProperties().setProperty(arrayName + ".orientation","vertical");
		}

		// now parse the elements. First, we need to know the array length!
		ParseSupport.parseMandatoryWord(stok,
				basicFeedbackTag + "Keyword 'length'", "length");
		arrayLength = ParseSupport.parseInt(stok, basicFeedbackTag + "length(>=1)",
				1);

		// entryIDs = new int[arrayLength << 1];

		// components = new StringBuilder(arrayLength << 3);

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
		@SuppressWarnings("unused")
		String unit = "ticks";

		// check for offset keyword 'after'
		if (ParseSupport.parseOptionalWord(stok, basicFeedbackTag
				+ "keyword 'after'", "after")) {
			// parse the offset
			displayDelay = ParseSupport.parseInt(stok, basicFeedbackTag + "delay", 0);

			// parse unit - handle only default
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
				
//				int displayDuration = 
				ParseSupport.parseInt(stok, basicFeedbackTag
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
				// nothing to be done here
//				int displayDuration = arrayLength
//						* ((unit.equalsIgnoreCase("ticks")) ? 10 : 200);
			}
		}

		// oids = new int[((displayDuration == 0) ? (arrayLength << 1) : 2)];

		Font f = AnimalParseSupport.parseFontInfo(stok, "array");

		PTStringArray stringArray = new PTStringArray(elements);
		stringArray.setObjectName(arrayName);
		stringArray.setLocation(basePoint);
		stringArray.setColor(arrayColor);
		stringArray.setBGColor(fillColor);
		stringArray.setFontColor(elementColor);
		stringArray.setDepth(basicDepth);
		stringArray.setFont(f);
		stringArray.setHighlightColor(cellHighlightColor);
		stringArray.setElemHighlightColor(elemHighlightColor);
		stringArray.setOrientation(verticalMode);

		// add the object to the list of graphic objects
		BasicParser.addGraphicObject(stringArray, anim);
		getObjectIDs().put(arrayName, stringArray.getNum(false));
		getObjectTypes().put(arrayName, getTypeIdentifier("array"));

		// generate the TimedShow animator
		TimedShow ts = new TimedShow(currentStep, stringArray.getNum(true), 0,
				"show", true);
		// insert the animator
		BasicParser.addAnimatorToAnimation(ts, anim);

		// ArrayProducer.makeArray(arrayName, basePoint, arrayColor, fillColor,
		// elementColor, verticalMode, arrayLength, elements, basicDepth,
		// displayDelay, cascadedDisplay, displayDuration, oids, entryIDs,
		// components, f, unit, stok);
		
		objectMap.put(stringArray.getNum(true), stringArray.clone());
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
		int indexOID = targetOIDs[0];

		// parse optional index marker color
		//Point[] coords = new Point[2];

		// now determine the basic object's bounding box
		@SuppressWarnings("unused")
		PTGraphicObject ptgo = animState.getCloneByNum(indexOID);
		//PTArray ptarray = (PTArray) ptgo;
		PTArray ptarray = (PTArray) objectMap.get(indexOID);
		// retrieve the bounding box

		Point p1 = ptarray.getArrowPoint(targetIndex);
		Point p2;
		if (ptarray.getOrientation()) {
			p2 = new Point(p1.x+length, p1.y);
		} else {
			p2 = new Point(p1.x, p1.y-length);
		}
		

		PTPolyline indexMarker = new PTPolyline(new Point[]{p2,p1});
		indexMarker.setObjectName(markerLabel + ".ptr");
		indexMarker.setFWArrow(true);
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

			if (ptarray.getOrientation()) {
				labelText.setLocation(new Point(p2.x + 5,
						p2.y - (fm.getHeight()/2)));
			} else {
				labelText.setLocation(new Point(p2.x - (textWidth >>> 1),
						p2.y - fm.getHeight()));
			}

			BasicParser.addGraphicObject(labelText, anim);
			getObjectIDs().put(markerName + ".label", labelText.getNum(true));
		}

		BasicParser.addGraphicObject(indexMarker, anim);

		String markerID = String.valueOf(indexMarker.getNum(true));
		String ids = (labelText == null) ? markerID : (markerID + " " + String
				.valueOf(labelText.getNum(true)));

		if (getObjectProperties().getProperty(targetObjectName + ".ptrs") != null) {
			getObjectProperties().put(
					targetObjectName + ".ptrs",
					getObjectProperties().getProperty(targetObjectName + ".ptrs") + " "
							+ markerName);
		} else {
			getObjectProperties().put(targetObjectName + ".ptrs", markerName);
		}

		getObjectIDs().put(markerName, ids);
//		getObjectIDs().put(targetObjectName,
//				getObjectIDs().getProperty(targetObjectName) + ' ' + ids);
		getObjectProperties().put(markerName + ".target", targetObjectName);
		getObjectProperties().put(markerName + ".pos", targetIndex);
		getObjectProperties().put(markerName + ".location", p1);
		getObjectTypes().put(markerName, getTypeIdentifier("arraymarker"));
		AnimalParseSupport.showComponents(stok, ids, localType, true);
		
		objectMap.put(indexMarker.getNum(true), indexMarker.clone());
	}

	/**
	 * Parse operations for putting a new element in the array
	 * 
	 * @exception IOException
	 *              if a parsing error occurs
	 */
	public void parseArrayPut() throws IOException {
		// arrayPut "value" on "arrayName" position i [color] [delay]
		ParseSupport.parseWord(stok, "data structure-specific operator");
		String arrayName = null;
		// determine value to be put
		String objectValue = AnimalParseSupport.parseText(stok, "arrayPut value",
				null, false, chosenLanguage);

		// parse keyword 'on'
		ParseSupport.parseMandatoryWord(stok, "arrayPut keyword 'on'", "on");

		// parse the array name itself
		arrayName = AnimalParseSupport.parseText(stok, "arrayPut base array");

		// retrieve IDs for array
		int arrayID = getObjectIDs().getIntProperty(arrayName, -1);

		// check name(registered, getTypeIdentifier("array") type)

		// ensure that it is an array, then access length etc.
		PTGraphicObject ptgo = animState.getCloneByNum(arrayID);
		if ((arrayID <= 0) || !(ptgo instanceof PTArray)) {
			ParseSupport.formatException("Target object '" + arrayName
					+ "' unknown or not an array.", stok);
		}
		//PTArray theArray = (PTArray) ptgo;
		PTArray theArray = (PTArray) objectMap.get(arrayID);

		// parse the keyword 'position'
		ParseSupport.parseMandatoryWord(stok, "arrayPut keyword 'position'",
				"position");

		// parse the target position and check if it is valid
		// replaced by array.getSize()
		int maxLength = theArray.getSize();
		// int maxLength = getObjectProperties().getIntProperty(arrayName +
		// ".length", -1);

		if (maxLength < 1) {
			ParseSupport.formatException("Invalid number of elements in array '"
					+ arrayName + "'.", stok);
		}

		int targetPos = ParseSupport.parseInt(stok, "arrayPut target pos [0, "
				+ (maxLength - 1) + "]", 0, maxLength - 1);
		
		// generate "hide" for old elements
		SetText setter = new SetText(currentStep, arrayID, 0, 0, "ticks", objectValue);
		setter.setMethod(setter.getMethod()+" "+targetPos);
		theArray.put(targetPos, objectValue);
		BasicParser.addAnimatorToAnimation(setter, anim);
		
		
//		Put putAnimator = null;
//		if (ptgo instanceof PTIntArray)
//			putAnimator = new Put(currentStep, arrayID, 0, 0, Integer.parseInt(objectValue), targetPos);
//		else{
//			putAnimator = new Put(currentStep, arrayID, 0, 0, objectValue, targetPos);
//		}
//		int oldEntryNumber = theArray.getEntry(targetPos).getNum(true);
//		if(((QuickAnimationStep)animState).currentObjectsContains(oldEntryNumber)){
//			System.out.println("Hide: "+theArray.getEntry(targetPos));
//			TimedShow ts = new TimedShow(currentStep, oldEntryNumber, 0, "show", false);
//			BasicParser.addAnimatorToAnimation(ts, anim);
//			getCurrentlyVisible().put(String.valueOf(oldEntryNumber), "false");
//		}
//		theArray.put(targetPos, putAnimator.getNewContent());
//		BasicParser.addAnimatorToAnimation(putAnimator, anim);
		AnimalParseSupport.parseTiming(stok, setter, "arrayPut");

		String unparsed = ParseSupport.consumeIncludingEOL(stok,
				"ds specific stuff");

		if (unparsed.length() > 1) {
			System.err.println("#Left unparsed...: '" + unparsed + "' @ASBI:560 line "  + stok.lineno());
		}
		
		updateArrayMarker(theArray);

		stok.pushBack();
		
		objectMap.put(theArray.getNum(true), theArray);
	} // parseArrayPut

	/**
	 * Parse data structure-specific operations, such as array element swapping
	 * 
	 */
	public void parseArraySwap() throws IOException {
		// int i;
		String localType = ParseSupport.parseWord(stok,
				"data structure-specific operator"); // stok.sval;

		// arraySwap on "baseArray" position i with j [timing]
		ParseSupport.parseMandatoryWord(stok, localType + " keyword 'on'", "on");
		String arrayName = AnimalParseSupport.parseText(stok, localType
				+ " base array name");

		int arrayID = getObjectIDs().getIntProperty(arrayName);
		PTGraphicObject ptgo = animState.getCloneByNum(arrayID);
		if ((arrayID <= 0) || !(ptgo instanceof PTArray)) {
			ParseSupport.formatException("Target object '" + arrayName
					+ "' unknown or not an array.", stok);
		}
		//PTArray theArray = (PTArray) ptgo;
		PTArray theArray = (PTArray) objectMap.get(arrayID);

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
		// PTGraphicObject ptgo = animState.getCloneByNum(arrayID);

		// Swap throws Exceptions! Now using two PUTs instead swap.
//		Swap swapAnimator = new Swap(currentStep, arrayID, 0, firstSwapPos,
//				secondSwapPos);
//		BasicParser.addAnimatorToAnimation(swapAnimator, anim); // insert this operation
		
		
//		Put putAnimatorFirst = null;
//		Put putAnimatorSecond = null;
//		PTText ptTextFirst = theArray.getEntry(firstSwapPos);
//		PTText ptTextSecond = theArray.getEntry(secondSwapPos);
//		if (ptgo instanceof PTIntArray){
//			putAnimatorFirst = new Put(currentStep, arrayID, 0, 0, Integer.parseInt(ptTextFirst.getText()), secondSwapPos);
//			putAnimatorSecond = new Put(currentStep, arrayID, 0, 0, Integer.parseInt(ptTextSecond.getText()), firstSwapPos);
//		}else{
//			putAnimatorFirst = new Put(currentStep, arrayID, 0, 0, ptTextFirst.getText(), secondSwapPos);
//			putAnimatorSecond = new Put(currentStep, arrayID, 0, 0, ptTextSecond.getText(), firstSwapPos);
//		}
//		theArray.put(secondSwapPos, putAnimatorFirst.getNewContent());
//		theArray.put(firstSwapPos, putAnimatorSecond.getNewContent());
//		BasicParser.addAnimatorToAnimation(putAnimatorFirst, anim);
//		BasicParser.addAnimatorToAnimation(putAnimatorSecond, anim);

		SetText setter = new SetText(currentStep, arrayID, 0, 0, "ticks", firstSwapPos+" "+secondSwapPos);
		setter.setMethod(setter.getMethod()+"Swap");
		BasicParser.addAnimatorToAnimation(setter, anim);
		theArray.doSwap(firstSwapPos, secondSwapPos);
		

		String unparsed = ParseSupport.consumeIncludingEOL(stok,
				"ds specific stuff");

		if (unparsed.length() > 1) {
			System.err.println("#Left unparsed...: '" + unparsed + "' @ASBI:639 line "  + stok.lineno());
		}
		
		updateArrayMarker(theArray);

		stok.pushBack();

		objectMap.put(theArray.getNum(true), theArray);
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
		boolean elemMode = colorType.toLowerCase().endsWith("elem");
		boolean highlightMode = colorType.startsWith("high");
		// String targetProperty = null;

		ParseSupport.parseMandatoryWord(stok, colorType + " keyword 'on'", "on");

		String targetArray = AnimalParseSupport.parseText(stok, colorType
				+ " array name");
		int arrayID = getObjectIDs().getIntProperty(targetArray, -1);
		PTGraphicObject ptgo = animState.getCloneByNum(arrayID);
		if ((arrayID <= 0) || !(ptgo instanceof PTArray)) {
			ParseSupport.formatException("Target object '" + targetArray
					+ "' unknown or not an array.", stok);
		}
		//PTArray theArray = (PTArray) ptgo;
		PTArray theArray = (PTArray) objectMap.get(arrayID);
		
		int fromRange = 0;
		int toRange = getObjectProperties().getIntProperty(targetArray + ".length") - 1;
		if (ParseSupport.parseOptionalWord(stok, colorType + " keyword 'position'",
				"position")) {
			fromRange = ParseSupport.parseInt(stok, colorType + " array index",
					0, getObjectProperties().getIntProperty(targetArray + ".length", 0));
			toRange = fromRange;
		} else {
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
		}

		// System.err.println("Range: ");
		// for (int ii=0;ii<targetOIDs.length; ii++)
		// System.err.println(targetOIDs[ii] +"  ");

		if(toRange-fromRange>=0){
			Color c = null;
			for(int i=fromRange ; i<=toRange ; i++){
				if (elemMode) {
					theArray.setElemHighlighted(i, highlightMode);
					c = theArray.getElemColor(i);
				} else {
					theArray.setHighlighted(i, highlightMode);
					c = theArray.getCellFillColor(i);
				}
			}
			String method = highlightMode ? "highlight " : "unhighlight ";
			method += elemMode ? "elements" : "cells";
//			method = "cellFillColor";
			method += " "+fromRange+" "+toRange;
			ColorChanger colChanger = new ColorChanger(currentStep, theArray.getNum(true), 0,
					method, c);
			// parse optional timing - is set within the method!
			AnimalParseSupport.parseTiming(stok, colChanger, "Color");
			// insert into list of animators
			BasicParser.addAnimatorToAnimation(colChanger, anim);
		}

		objectMap.put(theArray.getNum(true), theArray);
	}
	
	/**
	 * Create a Move from the description read from the StreamTokenizer. The
	 * description is usually generated by other programs and dumped in a file or
	 * on System.out.
	 */
	public void parseMovePointer() throws IOException {
		@SuppressWarnings("unused")
		String localType = ParseSupport.parseWord(stok, "move pointer keyword");	// Not Implemented

		String markerName = AnimalParseSupport.parseText(stok,
				"index marker name");
		int[] targetOIDs = getObjectIDs().getIntArrayProperty(markerName);
		int idFromMarker = targetOIDs[0];
		@SuppressWarnings("unused")
		PTGraphicObject ptgo = animState.getCloneByNum(idFromMarker);
		String baseElementName = getObjectProperties().getProperty(
				markerName + ".target");
		
		if ((targetOIDs == null)
				|| !(getObjectTypes().getProperty(markerName)
						.equals(getTypeIdentifier("arraymarker")))
				|| !(getObjectTypes().getProperty(baseElementName)
						.equals(getTypeIdentifier("array")))) {
			ParseSupport.formatException("invalid or unknown array ID: "
					+ markerName, stok);
		}

		//PTArray theArray = (PTArray) animState.getCloneByNum(getObjectIDs().getIntProperty(baseElementName));
		//PTPolyline marker = (PTPolyline) ptgo;
		PTArray theArray = (PTArray) objectMap.get(getObjectIDs().getIntProperty(baseElementName));
		@SuppressWarnings("unused")
		PTPolyline marker = (PTPolyline) objectMap.get(idFromMarker);
		
		ParseSupport.parseMandatoryWord(stok, "index marker keyword 'to'", "to");

		int maxIndex = getObjectProperties().getIntProperty(
				baseElementName + ".length", -1);

		// Toggle possible modes
		if (ParseSupport.parseOptionalWord(stok, "index marker keyword 'position'",
				"position")) {// Position
			int targetPos = ParseSupport.parseInt(stok,
					"index marker position value [0, " + maxIndex + "]", 0, maxIndex);
			
			getObjectProperties().put(markerName + ".pos",targetPos);
			
			updateArrayMarker(theArray);
		} else if (ParseSupport.parseOptionalWord(stok,
				"index marker keyword 'arrayEnd'", "arrayEnd")) {// LAST ELEMENT
			int targetPos = maxIndex-1;
			
			getObjectProperties().put(markerName + ".pos",targetPos);
			
			updateArrayMarker(theArray);
		} else if (ParseSupport.parseOptionalWord(stok,
				"index marker keyword 'outside'", "outside")) {// Outsite Right
			
			getObjectProperties().put(markerName + ".pos", -1);
			
			updateArrayMarker(theArray);
		} else {
			ParseSupport.formatException("invalid keyword for array marker move",
					stok);
		}

		objectMap.put(theArray.getNum(true), theArray);
	}
	
	public void parseArrayIndices() throws IOException {
		// parse exact command
		String operationType = ParseSupport.parseWord(stok, "set index visibility operation");

		ParseSupport.parseMandatoryWord(stok, operationType + " keyword 'set'", "set");
		ParseSupport.parseMandatoryWord(stok, operationType + " keyword 'visibility'", "visibility");
		
		Boolean visibility = AnimalParseSupport.parseText(stok, operationType + " boolean value").equals("true");

		ParseSupport.parseMandatoryWord(stok, operationType + " keyword 'on'", "on");

		String targetArray = AnimalParseSupport.parseText(stok, operationType
				+ " array name");
		int arrayID = getObjectIDs().getIntProperty(targetArray, -1);
		PTArray theArray = (PTArray) objectMap.get(arrayID);
		
		SetText setter = new SetText(currentStep, arrayID, 0, 0, "ticks", String.valueOf(visibility));
		setter.setMethod("showIndices");
		BasicParser.addAnimatorToAnimation(setter, anim);
		theArray.showIndices(visibility);

		String unparsed = ParseSupport.consumeIncludingEOL(stok,
				"ds specific stuff");

		if (unparsed.length() > 1) {
			System.err.println("#Left unparsed...: '" + unparsed + "' @ASBI:820 line "  + stok.lineno());
		}
		
		stok.pushBack();
		
		objectMap.put(theArray.getNum(true), theArray);
	}
	
	public void parseColorSet() throws IOException {
		// parse exact command
		String colorType = ParseSupport.parseWord(stok, "color set operation");
		// boolean contextMode = false;
		boolean modeSetBorderColor = colorType.toLowerCase().equals("setBorderColor".toLowerCase());
		boolean modeSetFillColor = colorType.toLowerCase().equals("setFillColor".toLowerCase());
		boolean modeSetTextColor = colorType.toLowerCase().equals("setTextColor".toLowerCase());
		boolean modeSetHighlightBorderColor = colorType.toLowerCase().equals("setHighlightBorderColor".toLowerCase());
		boolean modeSetHighlightFillColor = colorType.toLowerCase().equals("setHighlightFillColor".toLowerCase());
		boolean modeSetHighlightTextColor = colorType.toLowerCase().equals("setHighlightTextColor".toLowerCase());
		
		ParseSupport.parseMandatoryWord(stok, colorType + " keyword 'on'", "on");

		String targetArray = AnimalParseSupport.parseText(stok, colorType
				+ " array name");
		int arrayID = getObjectIDs().getIntProperty(targetArray, -1);
		PTArray theArray = (PTArray) objectMap.get(arrayID);
		
		int fromRange = 0;
		int toRange = getObjectProperties().getIntProperty(targetArray + ".length") - 1;
		if (ParseSupport.parseOptionalWord(stok, colorType + " keyword 'position'",
				"position")) {
			fromRange = ParseSupport.parseInt(stok, colorType + " array index",
					0, getObjectProperties().getIntProperty(targetArray + ".length", 0));
			toRange = fromRange;
		} else {
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
		}
		
		
		ParseSupport.parseMandatoryWord(stok, colorType + " keyword 'with'", "with");
		ParseSupport.parseMandatoryWord(stok, colorType + " keyword 'color'", "color");
		
		Color color = AnimalParseSupport.parseColor(stok, colorType + " color");
		
		ColorChanger colChanger = new ColorChanger(currentStep, theArray.getNum(true), 0,
				colorType+" "+fromRange+" "+toRange, color);
		// parse optional timing - is set within the method!
		AnimalParseSupport.parseTiming(stok, colChanger, "Color");
		// insert into list of animators
		BasicParser.addAnimatorToAnimation(colChanger, anim);
		
		for (int i = fromRange; i <= toRange; i++) {
			if(modeSetBorderColor){
				theArray.setOutlineColor(i, color);
			}else if(modeSetFillColor){
				theArray.setElemHighlightColor(i, color);
			}else if(modeSetTextColor){
				theArray.setFontColor(i, color);
			}else if(modeSetHighlightBorderColor){
				theArray.setHighlightOutlineColor(i, color);
			}else if(modeSetHighlightFillColor){
				theArray.setHighlightColorIndex(i, color);
			}else if(modeSetHighlightTextColor){
				theArray.setElemHighlightColor(i, color);
			}
		}
		
		
		

		objectMap.put(theArray.getNum(true), theArray);
	}
	

	/**
	 * This Method update the ArrayMarker (and his Label) after a value change in the array.
	 * @param theArray The Array which should be updated
	 */
	private void updateArrayMarker(PTArray theArray) {
		String arrayName = theArray.getObjectName();
		if(getObjectProperties().containsKey(arrayName + ".ptrs")){
			for(String markerName : getObjectProperties().getStringArrayProperty(arrayName + ".ptrs", " ")){
				int idFromMarker = getObjectIDs().getIntArrayProperty(markerName)[0];
				int posFromMarkerInArray = getObjectProperties().getIntProperty(markerName + ".pos");
				int[] objectsToMove;
				
				//PTPolyline marker = (PTPolyline) animState.getCloneByNum(idFromMarker);
				PTPolyline marker = (PTPolyline) objectMap.get(idFromMarker);
				Point pTopMiddleOld = getObjectProperties().getPointProperty(markerName + ".location");
				Point pTopMiddleNew = theArray.getArrowPoint(posFromMarkerInArray);
				
				PTPolyline currentMoveBase = new PTPolyline(new int[] { 0, pTopMiddleNew.x-pTopMiddleOld.x }, new int[] { 0, pTopMiddleNew.y-pTopMiddleOld.y }); // array of xcoords and  array of ycoords
				currentMoveBase.setDepth(10);
				currentMoveBase.getNum(true);

				//UPDATE OBJECTS
				marker.translate(pTopMiddleNew.x-pTopMiddleOld.x, pTopMiddleNew.y-pTopMiddleOld.y);
				getObjectProperties().put(markerName + ".location", marker.getLastNode().toPoint());
				
				if(getObjectIDs().getIntArrayProperty(markerName).length>1){
					int idFromMarkerLabel = getObjectIDs().getIntArrayProperty(markerName)[1];
					PTText markerLabel = (PTText) animState.getCloneByNum(idFromMarkerLabel);
					markerLabel.translate(pTopMiddleNew.x-pTopMiddleOld.x, pTopMiddleNew.y-pTopMiddleOld.y);
					
					objectsToMove = new int[]{idFromMarker,idFromMarkerLabel};
				}else{
					objectsToMove = new int[]{idFromMarker};
				}
				
				Move currentMoveAnimation = new Move(currentStep, objectsToMove, 0, "translate", currentMoveBase.getNum(true));
			    BasicParser.addGraphicObject(currentMoveBase, anim);
				BasicParser.addAnimatorToAnimation(currentMoveAnimation, anim);
			}
		}
	}
}