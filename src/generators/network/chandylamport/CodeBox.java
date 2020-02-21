package generators.network.chandylamport;

import java.awt.Color;
import java.awt.Font;
import java.util.LinkedList;
import java.util.List;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Rect;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.TextProperties;
import algoanim.util.Node;
import algoanim.util.Offset;
import translator.Translator;

public class CodeBox {

	private Text events;
	private Text startSnapshot;
	private Text receiveMessage;
	private Text receiveMarker;
	
	private Text cases;
	private Text thisMarkerSeen;
	private Text thisMarkerNotSeen;
	private Text noMarkerSeen;
	
	private Text reactions;
	private Text recordMessage;
	private Text dontRecordMessage;
	private Text recordState;
	private Text sendMarker;
	private Text stoppThisRecording;
	
	private List<Text> textList;
	
	private Rect box;
	
	private TextProperties algStepsTextProperties;
	
	private Language lang;
	
	public CodeBox(Node upperLeftBox,Node upperLeftCode, Language lang,Translator translator, int processID) {
		this.lang = lang;
		
		algStepsTextProperties = new TextProperties();
		algStepsTextProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 11));
		algStepsTextProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.LIGHT_GRAY);
		
		events = lang.newText(upperLeftCode, "Event:", "events"+processID, null,
				algStepsTextProperties);
		startSnapshot = lang.newText(new Offset(30, 0, events, AnimalScript.DIRECTION_NE), translator.translateMessage("start_snapshot"),
				"startSnapshot"+processID, null, algStepsTextProperties);
		receiveMessage = lang.newText(new Offset(0, 15, startSnapshot, AnimalScript.DIRECTION_NW),
				translator.translateMessage("receive_message"), "receiveMessage"+processID, null, algStepsTextProperties);
		receiveMarker = lang.newText(new Offset(0, 15, receiveMessage, AnimalScript.DIRECTION_NW),
				translator.translateMessage("receive_marker"), "receiveMarker"+processID, null, algStepsTextProperties);

		cases = lang.newText(new Offset(0, 45, events, AnimalScript.DIRECTION_NW), translator.translateMessage("case"), "cases"+processID, null,
				algStepsTextProperties);
		thisMarkerSeen = lang.newText(new Offset(0, 15, receiveMarker, AnimalScript.DIRECTION_NW),
				translator.translateMessage("this_marker_received"), "thisMarkerSeen"+processID, null, algStepsTextProperties);
		thisMarkerNotSeen = lang.newText(new Offset(0, 15, thisMarkerSeen, AnimalScript.DIRECTION_NW),
				translator.translateMessage("this_marker_not_received"), "thisMarkerNotSeen"+processID, null, algStepsTextProperties);
		noMarkerSeen = lang.newText(
				new Offset(0, 15, thisMarkerNotSeen, AnimalScript.DIRECTION_NW), translator.translateMessage("no_marker_received"),
				"noMarkerSeen"+processID, null, algStepsTextProperties);

		reactions = lang.newText(new Offset(0, 90, events, AnimalScript.DIRECTION_NW), translator.translateMessage("reaction"),
				"reactions"+processID, null, algStepsTextProperties);
		recordMessage = lang.newText(
				new Offset(0, 15, noMarkerSeen, AnimalScript.DIRECTION_NW), translator.translateMessage("record_message"),
				"recordMessage"+processID, null, algStepsTextProperties);
		dontRecordMessage = lang.newText(
				new Offset(0, 15, recordMessage, AnimalScript.DIRECTION_NW), translator.translateMessage("dont_record_message"),
				"dontRecordMessage"+processID, null, algStepsTextProperties);
		recordState = lang.newText(
				new Offset(0, 15, dontRecordMessage, AnimalScript.DIRECTION_NW), translator.translateMessage("record_state"),
				"recordState"+processID, null, algStepsTextProperties);
		sendMarker = lang.newText(new Offset(0, 15, recordState, AnimalScript.DIRECTION_NW),
				translator.translateMessage("send_marker"), "sendMarker"+processID, null, algStepsTextProperties);
		stoppThisRecording = lang.newText(new Offset(0, 15, sendMarker, AnimalScript.DIRECTION_NW),
				translator.translateMessage("stop_this_recording"), "stoppThisRecording"+processID, null, algStepsTextProperties);
		
		box = lang.newRect(upperLeftBox,
				new Offset(20, 10, stoppThisRecording, AnimalScript.DIRECTION_SE), "box"+processID, null);
		
		textList = new LinkedList<Text>();
		
		textList.add(events);
		textList.add(startSnapshot);
		textList.add(receiveMessage);
		textList.add(receiveMarker);
		
		textList.add(cases);
		textList.add(thisMarkerSeen);
		textList.add(thisMarkerNotSeen);
		textList.add(noMarkerSeen);
		
		textList.add(reactions);
		textList.add(recordMessage);
		textList.add(dontRecordMessage);
		textList.add(recordState);
		textList.add(sendMarker);
		textList.add(stoppThisRecording);	
	}

	public void cleanup() {
		textList.forEach(t -> t.changeColor("color", Color.LIGHT_GRAY, null, null));
		box.changeColor("color", Color.LIGHT_GRAY, null, null);
	}
	
	public Text getEvents() {
		return events;
	}

	public Text getStartSnapshot() {
		return startSnapshot;
	}

	public Text getReceiveMessage() {
		return receiveMessage;
	}

	public Text getReceiveMarker() {
		return receiveMarker;
	}

	public Text getCases() {
		return cases;
	}

	public Text getThisMarkerSeen() {
		return thisMarkerSeen;
	}

	public Text getThisMarkerNotSeen() {
		return thisMarkerNotSeen;
	}

	public Text getNoMarkerSeen() {
		return noMarkerSeen;
	}

	public Text getReactions() {
		return reactions;
	}

	public Text getRecordMessage() {
		return recordMessage;
	}

	public Text getDontRecordMessage() {
		return dontRecordMessage;
	}

	public Text getRecordState() {
		return recordState;
	}

	public Text getSendMarker() {
		return sendMarker;
	}

	public Text getStoppThisRecording() {
		return stoppThisRecording;
	}

	public Rect getBox() {
		return box;
	}

	public TextProperties getAlgStepsTextProperties() {
		return algStepsTextProperties;
	}

	public Language getLang() {
		return lang;
	}
	
}
