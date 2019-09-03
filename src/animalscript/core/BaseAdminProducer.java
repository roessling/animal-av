package animalscript.core;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Enumeration;

import translator.AnimalTranslator;
import animal.graphics.PTGraphicObject;
import animal.main.Link;
import animal.misc.MessageDisplay;
import animal.misc.ParseSupport;
import animal.misc.XProperties;

/**
 * This class generates the required actions for the basic administrative
 * operations.
 * 
 * @author Guido Roessling <roessling@acm.orgA>
 * @version 1.1 20050504
 */
public class BaseAdminProducer {

	/**
	 * The current location counter, used for incrementing new location numbers
	 */
	private int locationCounter = 0;

	
	/**
	 * Invokes the appropriate API methods for generating a delay on the current step.
	 *  
	 * @param properties the XProperties object specifying the delay type and time
	 * @param currentLink the link on which to add the delay
	 */
	public void produceDelay(XProperties properties, Link currentLink) {
		if (currentLink != null && properties != null) {
			String delayMode = properties.getProperty(
					BaseAdminParser.DELAY_TYPE, BaseAdminParser.DELAY_TIME);
			if (delayMode.equals(BaseAdminParser.DELAY_TIME)) {
				currentLink.setMode(Link.WAIT_TIME);
				currentLink.setTime(properties.getIntProperty(
						BaseAdminParser.DELAY_TIME, 0));
			} else if (delayMode.equals(BaseAdminParser.DELAY_OBJECT_CLICK)) {
				currentLink.setMode(Link.WAIT_CLICK);
				currentLink.setTargetObjectID(properties.getIntProperty(
						BaseAdminParser.DELAY_TARGET_ID, 0));
			} else
				currentLink.setMode(Link.WAIT_KEY);
		}
	}

	/**
	 * Invokes the appropriate API methods for generating an echo command.
	 *  
	 * @param properties the XProperties object specifying the echo type and content
	 * @param objectIDs the mapping of object names to IDs
	 * @param objectProperties the mapping of object names to properties
	 * @param currentLine the current line in the AnimalScript code
	 * @param currentStep the step on which to execute the echo command
	 */
	public void produceEcho(XProperties properties, XProperties objectIDs,
			XProperties objectProperties, int currentLine, int currentStep) {
		String echoMode = properties.getProperty(BaseAdminParser.ECHO_MODE,
				"text");
		StringBuilder sb = new StringBuilder(80);
		if (echoMode.equals("text"))
			MessageDisplay.message("# " +properties.getProperty(BaseAdminParser.ECHO_TEXT));
		else if (echoMode.equals("location")) {
			Point p = properties.getPointProperty(
					BaseAdminParser.ECHO_LOCATION, null);
			String invalid="";
			if (p == null)
				invalid = AnimalTranslator.translateMessage("invalidLocation");
			if (p != null)
				sb.append('(').append(p.x).append(", ").append(p.y).append(')');
			showEchoMessage("echoLocation", new String[]{invalid, 
						Integer.toString(currentLine),
						sb.toString()});
		} else if (echoMode.equals("boundingBox")) {
			Rectangle bBox = null;
			String[] oids = properties
					.getStringArrayProperty(BaseAdminParser.ECHO_BOUND_IDS);
			int i;
			//TODO touch this!
			BasicParser.animState.setStep(currentStep, false);
			int[] components;
			Rectangle currentBounds = null;
			StringBuilder localBuffer = null;
			for (i = 0; i < oids.length; i++) {
				localBuffer = new StringBuilder(64);
				components = objectIDs.getIntArrayProperty(oids[i]);
				if (components != null) {
					for (int j = 0; j < components.length; j++) {
						int currentID = components[j];
						if (currentID != -1) {
							PTGraphicObject ptgo = BasicParser.animState.getCloneByNum(
									currentID);
							bBox = ptgo.getBoundingBox();
							if (currentBounds == null)
								currentBounds = bBox;
							else
								currentBounds = currentBounds.union(bBox);
						} else
							showEchoMessage("echoUnknownSubcomponent", 
									new String[]{Integer.toString(components[j]), oids[i]});
					}
					if (currentBounds != null) {
						localBuffer.append('(').append(currentBounds.x).append(", ");
						localBuffer.append(currentBounds.y).append("), (");
						localBuffer.append(currentBounds.x + currentBounds.width);
						localBuffer.append(", ").append(currentBounds.y + currentBounds.height);
						localBuffer.append(") ");
					} else
						localBuffer.append("-- no bounds --");
					sb.append(AnimalTranslator.translateMessage("echoBounds", 
							new String[] {oids[i],
							localBuffer.toString(), 
							String.valueOf(currentStep),
							String.valueOf(currentLine)}));
					if (i < oids.length - 1)
						sb.append(MessageDisplay.LINE_FEED);
//					showEchoMessage("echoBounds",	new String[] {oids[i], 
//							localBuffer.toString()});
				} else
					  showEchoMessage("echoObjectUnknown", new String[]{oids[i]});
				currentBounds = null;
			}
      MessageDisplay.message(sb.toString());
			components = null;
		} else if (echoMode.equals("value")) {
			String[] oids = properties
					.getStringArrayProperty(BaseAdminParser.ECHO_VALUE_IDS);
			String result = null;
			for (int i = 0; i < oids.length; i++) {
				result = objectProperties.getProperty(oids[i] + ".value",
						AnimalTranslator.translateMessage("unknownVar", new String[]{oids[i]}));
				showEchoMessage("echoValue",
						new String[]{oids[i], result});
			}
		} else if (echoMode.equals("ids")) {
			String[] oids = properties
					.getStringArrayProperty(BaseAdminParser.ECHO_VALUE_IDS);
			String result = null;
			for (int i = 0; i < oids.length; i++) {
				result = objectIDs.getProperty(oids[i], 
							AnimalTranslator.translateMessage("unknownVar", new String[]{oids[i]}));
				showEchoMessage("echoIDs", 
						new String[]{oids[i], result});
			}
		} else if (echoMode.equals("visible")) {
			Enumeration<String> e = BasicParser.getCurrentlyVisible().keys();
			while (e.hasMoreElements()) {
				String value = e.nextElement();
				if ("true".equalsIgnoreCase(BasicParser.getCurrentlyVisible().get(value)))
					sb.append(" '").append(value).append("'");
			}
			showEchoMessage("echoVisible",
					new String[]{sb.toString()});
		} else if (echoMode.equals("rule")) {
			String rule = properties.getProperty(BaseAdminParser.ECHO_RULE_NAME);
			showEchoMessage("echoRule",
					new String[]{rule, properties.getProperty(BaseAdminParser.ECHO_RULE_VALUE)});
		}
		sb = null;
	}

	/**
	 * Invoke the appropriate API methods to group objects
	 * 
	 * @param properties the properties specifying the group ID
	 * @param objectIDs the mapping of object names to IDs
	 * @param nextGONum the next free graphical object ID
	 */
	public void produceGrouping(XProperties properties, XProperties objectIDs,
			int nextGONum) {
		// test which IDs we want to insert
//		boolean[] mergeIDs = new boolean[nextGONum];
		String targetID = properties.getProperty(BaseAdminParser.GROUP_ID);
		String[] stringIDs = properties
				.getStringArrayProperty(BaseAdminParser.GROUP_TARGET_OIDS);
		if (properties.getBoolProperty(BaseAdminParser.GROUP_MODE)) {
			int[] targetIDs = ParseSupport.expandIDsFromStrings(stringIDs,
					objectIDs, nextGONum);
			StringBuilder sb = new StringBuilder(targetIDs.length * 5);
			for (int i = 0; i < targetIDs.length; i++)
				sb.append(targetIDs[i]).append(' ');
			objectIDs.put(targetID, sb.toString());
		} else {
			int[] prunedIDs = objectIDs.getIntArrayProperty(targetID);
			int[] targetIDs = ParseSupport.expandIDsFromStrings(stringIDs,
					objectIDs, nextGONum);
//			int nrEntries = 0;
			int i = 0;
			boolean[] keepThese = new boolean[nextGONum];
			for (i = 0; i < prunedIDs.length; i++) {
//				nrEntries++;
				keepThese[prunedIDs[i]] = true;
			}
			for (i = 0; i < targetIDs.length; i++) {
				if (keepThese[targetIDs[i]]) {
					keepThese[targetIDs[i]] = false;
//					nrEntries--;
				} else
					MessageDisplay.errorMsg("Entry " + targetIDs[i]
							+ " not included or removed twice",
							MessageDisplay.RUN_ERROR);
			}
			StringBuilder resultingIDs = new StringBuilder(stringIDs.length * 3);
			for (i = 0; i < keepThese.length; i++)
				if (keepThese[i])
					resultingIDs.append(i).append(" ");
			objectIDs.put(targetID, resultingIDs.toString());
		}
	}

	private void showEchoMessage(String key, String[] params) {
		MessageDisplay.message(AnimalTranslator.translateMessage(key, params));
	}
	
	/**
	 * Invokes the appropriate API methods for generating a label on the current step.
	 *  
	 * @param properties the XProperties object specifying the label title
	 * @param currentLink the link on which to add the label
	 */
	public void produceLabel(XProperties properties, Link currentLink) {
		if (currentLink != null && properties != null) {
			currentLink.setLinkLabel(properties.getProperty(
					BaseAdminParser.LABEL_ID, "<<label>>"));
		}
	}

	/**
	 * Invokes the appropriate API methods for generating a new location.
	 *  
	 * @param properties the XProperties object specifying the delay type and time
	 * @param locations the mapping of locations to coordinates
	 */
	public void produceLocation(XProperties properties, XProperties locations) {
		if (locations != null && properties != null) {
			String locationName = properties
					.getProperty(BaseAdminParser.LOCATION_ID);
			if (locationName == null)
				locationName = "loc" + (locationCounter++);
			locations.put(locationName, properties.getPointProperty(
					BaseAdminParser.LOCATION_POINT, BasicParser.origin));
		}
	}

	/**
	 * Invokes the appropriate API methods for swapping object IDs.
	 *  
	 * @param properties the XProperties object specifying the delay type and time
	 * @param objectIDs the mapping of object names to IDs
	 * @param currentLine the current line of the underlying AnimalScript code
	 */
	public void produceSwap(XProperties properties, XProperties objectIDs,
			int currentLine) {
		// retrieve the numeric object IDs
		String firstName = properties
				.getProperty(BaseAdminParser.SWAP_FIRST_OID);
		String secondName = properties
				.getProperty(BaseAdminParser.SWAP_SECOND_OID);
		int firstID = objectIDs.getIntProperty(firstName, -1);
		int secondID = objectIDs.getIntProperty(secondName, -1);

		// check if both objects exists
		if (firstID != -1 && secondID != -2) {
			// set the numeric ID of the first ID to the one of the second
			// object
			objectIDs.put(firstName, secondID);

			// set the numeric ID of the second object to the one of the first
			// obj
			objectIDs.put(secondName, firstID);
		} else {
			if (firstID == -1)
				MessageDisplay.message("Object ID invalid: '" + firstName
						+ "' in line " + currentLine);
			else
				MessageDisplay.message("Object ID invalid: '" + secondName
						+ "' in line " + currentLine);
		}
	}
}
