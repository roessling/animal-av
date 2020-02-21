package generators.network.chandylamport;

import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import algoanim.primitives.StringMatrix;
import generators.network.chandylamport.Event.Case;
import generators.network.chandylamport.Marker.MarkerState;

public class Process {

	private Map<Integer, Marker> processToMarkerMap;
	private Map<Integer, List<Message>> processToMessageMap;
	private List<Message> recordedMessages;
	private List<Message> receivedMessages;
	private CodeBox codeBox;
	private StringMatrix stringMatrix;
	private boolean isStateRecorded;
	private int id;

	public Process(int id) {
		processToMarkerMap = new HashMap<Integer, Marker>();
		processToMessageMap = new HashMap<Integer, List<Message>>();
		isStateRecorded = false;
		recordedMessages = new LinkedList<>();
		receivedMessages = new LinkedList<>();
		this.id = id;
	}

	public CodeBox getCodeBox() {
		return codeBox;
	}

	public void setCodeBox(CodeBox codeBox) {
		this.codeBox = codeBox;
	}

	public StringMatrix getStringMatrix() {
		return stringMatrix;
	}

	public Map<Integer, Marker> getProcessToMarkerMap() {
		return processToMarkerMap;
	}

	public Map<Integer, List<Message>> getProcessToMessageMap() {
		return processToMessageMap;
	}

	public void setStringMatrix(StringMatrix stringMatrix) {
		this.stringMatrix = stringMatrix;
	}

	public void putToMarkerMap(int fromProcess, Marker marker) {
		processToMarkerMap.put(fromProcess, marker);
	}

	public void putToMessageMap(int fromProcess, Message message) {
		if (!processToMessageMap.containsKey(fromProcess)) {
			processToMessageMap.put(fromProcess, new LinkedList<>());
		}
		processToMessageMap.get(fromProcess).add(message);
	}

	public void displayRecordMessage(int fromProcessId, int messageNumber) {
		highlightMessage(fromProcessId, messageNumber);

		codeBox.getEvents().changeColor("color", Color.RED, null, null);
		codeBox.getReceiveMessage().changeColor("color", Color.RED, null, null);
		codeBox.getCases().changeColor("color", Color.RED, null, null);
		codeBox.getThisMarkerNotSeen().changeColor("color", Color.RED, null, null);
		codeBox.getReactions().changeColor("color", Color.RED, null, null);
		codeBox.getRecordMessage().changeColor("color", Color.RED, null, null);
		;
		codeBox.getBox().changeColor("color", Color.RED, null, null);

		stringMatrix.setGridTextColor(messageNumber + 1, fromProcessId - 1, Color.RED, null, null);

		Message recordedMessage = processToMessageMap.get(fromProcessId).get(messageNumber);
		recordedMessage.setWasSaved(true);
		recordedMessages.add(recordedMessage);
	}

	public void displayDoNotRecordMessage(int fromProcessId, int messageNumber, Case case_) {
		highlightMessage(fromProcessId, messageNumber);

		codeBox.getEvents().changeColor("color", Color.RED, null, null);
		codeBox.getReceiveMessage().changeColor("color", Color.RED, null, null);
		codeBox.getCases().changeColor("color", Color.RED, null, null);
		if (case_ == Case.THIS_MARKER_RECEIVED) {
			codeBox.getThisMarkerSeen().changeColor("color", Color.RED, null, null);
		} else {
			codeBox.getNoMarkerSeen().changeColor("color", Color.RED, null, null);
		}
		codeBox.getReactions().changeColor("color", Color.RED, null, null);
		codeBox.getDontRecordMessage().changeColor("color", Color.RED, null, null);
		codeBox.getBox().changeColor("color", Color.RED, null, null);
	}

	public void displayRecordState(int fromProcessId) {
		if (fromProcessId >= 0) {
			processToMarkerMap.get(fromProcessId).getMarkerLine().show();
			processToMarkerMap.get(fromProcessId).getMarkerText().show();
			processToMarkerMap.get(fromProcessId).getMarkerLine().changeColor("color", Color.GREEN, null, null);
			processToMarkerMap.get(fromProcessId).getMarkerText().changeColor("color", Color.GREEN, null, null);
			processToMarkerMap.get(fromProcessId).setState(MarkerState.RECEIVED);
		}

		codeBox.getEvents().changeColor("color", Color.RED, null, null);
		codeBox.getReceiveMarker().changeColor("color", Color.RED, null, null);
		codeBox.getCases().changeColor("color", Color.RED, null, null);
		codeBox.getNoMarkerSeen().changeColor("color", Color.RED, null, null);
		codeBox.getReactions().changeColor("color", Color.RED, null, null);
		codeBox.getRecordState().changeColor("color", Color.RED, null, null);
		codeBox.getSendMarker().changeColor("color", Color.RED, null, null);
		codeBox.getBox().changeColor("color", Color.RED, null, null);

		stringMatrix.setGridTextColor(1, id - 1, Color.RED, null, null);
		isStateRecorded = true;
	}

	public void displayStartSnapshot(int fromProcessId) {
		if (fromProcessId >= 0) {
			processToMarkerMap.get(fromProcessId).getMarkerLine().show();
			processToMarkerMap.get(fromProcessId).getMarkerText().show();
			processToMarkerMap.get(fromProcessId).getMarkerLine().changeColor("color", Color.RED, null, null);
			processToMarkerMap.get(fromProcessId).getMarkerText().changeColor("color", Color.RED, null, null);
			processToMarkerMap.get(fromProcessId).setState(MarkerState.RECEIVED);
		}

		codeBox.getEvents().changeColor("color", Color.RED, null, null);
		codeBox.getStartSnapshot().changeColor("color", Color.RED, null, null);
		codeBox.getCases().changeColor("color", Color.RED, null, null);
		codeBox.getReactions().changeColor("color", Color.RED, null, null);
		codeBox.getRecordState().changeColor("color", Color.RED, null, null);
		codeBox.getSendMarker().changeColor("color", Color.RED, null, null);
		codeBox.getBox().changeColor("color", Color.RED, null, null);

		stringMatrix.setGridTextColor(1, id - 1, Color.RED, null, null);
		isStateRecorded = true;
	}

	public void displayMarkerOnTheWay(int fromProcessId) {
		processToMarkerMap.get(fromProcessId).getMarkerLine().show();
		processToMarkerMap.get(fromProcessId).getMarkerText().show();
		processToMarkerMap.get(fromProcessId).setState(MarkerState.SENT);
	}

	public void displayStopThisRecord(int fromProcessId) {
		processToMarkerMap.get(fromProcessId).getMarkerLine().changeColor("color", Color.RED, null, null);
		processToMarkerMap.get(fromProcessId).getMarkerText().changeColor("color", Color.RED, null, null);
		processToMarkerMap.get(fromProcessId).setState(MarkerState.RECEIVED);

		codeBox.getEvents().changeColor("color", Color.RED, null, null);
		codeBox.getReceiveMarker().changeColor("color", Color.RED, null, null);
		codeBox.getCases().changeColor("color", Color.RED, null, null);
		codeBox.getThisMarkerNotSeen().changeColor("color", Color.RED, null, null);
		codeBox.getReactions().changeColor("color", Color.RED, null, null);
		codeBox.getStoppThisRecording().changeColor("color", Color.RED, null, null);
		codeBox.getBox().changeColor("color", Color.RED, null, null);
	}

	private void highlightMessage(int fromProcessId, int messageNumber) {
		processToMessageMap.get(fromProcessId).get(messageNumber).getMessageLine().show();
		processToMessageMap.get(fromProcessId).get(messageNumber).getMessageText().show();
		receivedMessages.add(processToMessageMap.get(fromProcessId).get(messageNumber));
	}

	public void cleanup() {
		processToMarkerMap.values().forEach(m -> {
			if (m.getState().equals(MarkerState.RECEIVED)) {
				m.getMarkerText().changeColor("color", Color.BLACK, null, null);
				m.getMarkerLine().changeColor("color", Color.BLACK, null, null);
			} else if (m.getState().equals(MarkerState.SENT)) {
				m.getMarkerText().changeColor("color", Color.LIGHT_GRAY, null, null);
				m.getMarkerLine().changeColor("color", Color.LIGHT_GRAY, null, null);
			}
		});
		receivedMessages.stream().forEach(m -> {
			if (m.isWasSaved()) {
				m.getMessageLine().changeColor("color", Color.BLACK, null, null);
				m.getMessageText().changeColor("color", Color.BLACK, null, null);
			} else {
				m.getMessageLine().changeColor("color", Color.LIGHT_GRAY, null, null);
				m.getMessageText().changeColor("color", Color.LIGHT_GRAY, null, null);
			}
		});
		codeBox.cleanup();
		if (isStateRecorded) {
			stringMatrix.setGridTextColor(1, id - 1, Color.BLACK, null, null);
		}
		recordedMessages.forEach(m -> stringMatrix.setGridTextColor(m.getMessageMatrixRowIndex(),
				m.getMessageMatrixColIndex(), Color.BLACK, null, null));
	}
	
	public void hideAllMarkers() {
		processToMarkerMap.values().forEach(m -> {
				m.getMarkerText().hide();
				m.getMarkerLine().hide();
		});
	}

}
