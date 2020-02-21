package generators.network.chandylamport;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import generators.network.chandylamport.Event.Case;
import generators.network.chandylamport.Event.EventType;
import generators.network.chandylamport.Event.Reaction;

public class Model {

	private Map<Integer, List<Event>> processToEventsMap;

	private List<Event> allEvents;

	private Display display;
	
	public Model(Map<Integer, List<Event>> processToEventsMap,Display display) {
		this.processToEventsMap = processToEventsMap;
		this.display = display;
		init();
	}

	public void init() {
		processToEventsMap.values().forEach(e -> e.sort(Comparator.comparing(Event::getEventTime)));
		processToEventsMap.values().forEach(e -> calculateCasesForProcessEvents(e));
		processToEventsMap.values().forEach(e -> calculateReactionsForProcessEvents(e));
		allEvents = processToEventsMap.values().stream().flatMap(List::stream).collect(Collectors.toList());
		allEvents.sort(Comparator.comparing(Event::getEventTime));
		//System.out.println(allEvents);
	}

	private void calculateCasesForProcessEvents(List<Event> events) {
		for (Event event : events) {
			switch (event.getEventType()) {
			case RECEIVE_MESSAGE:
				calculateCaseForReceiveMessageEvent(event, events);
				break;
			case RECEIVE_MARKER:
				calculateCaseForReceiveMarkerEvent(event, events);
				break;
			default:
				break;
			}
		}
	}

	private void calculateReactionsForProcessEvents(List<Event> events) {
		for (Event event : events) {
			switch (event.getEventType()) {
			case START_SNAPSHOT:
				event.setReaction(Reaction.START_SNAPSHOT);
				break;
			case RECEIVE_MARKER:
				switch (event.getCase_()) {
				case NO_MARKER_RECEIVED:
					event.setReaction(Reaction.RECORD_STATE_AND_SEND_MARKER);
					break;
				case THIS_MARKER_NOT_RECEIVED:
					event.setReaction(Reaction.STOPP_THIS_RECORD);
					break;
				default:
					break;
				}
				break;
			case RECEIVE_MESSAGE:
				switch (event.getCase_()) {
				case NO_MARKER_RECEIVED:
					event.setReaction(Reaction.DONT_RECORD_MESSAGE);
					break;
				case THIS_MARKER_NOT_RECEIVED:
					event.setReaction(Reaction.RECORD_MESSAGE);
					break;
				case THIS_MARKER_RECEIVED:
					event.setReaction(Reaction.DONT_RECORD_MESSAGE);
					break;
				default:
					break;
				}
				break;
			default:
				break;
			}
		}
	}

	private void calculateCaseForReceiveMarkerEvent(Event event, List<Event> events) {
		List<Event> previouslyReceivedMarkers = events.stream().filter(
				e -> e.getEventType().equals(EventType.RECEIVE_MARKER) && events.indexOf(e) < events.indexOf(event))
				.collect(Collectors.toList());
		Optional<Event> snapshotEvent = events.stream().filter(e -> e.getEventType().equals(EventType.START_SNAPSHOT))
				.findAny();

		if (CollectionUtils.isEmpty(previouslyReceivedMarkers) && !snapshotEvent.isPresent()) {
			event.setCase_(Case.NO_MARKER_RECEIVED);
		} else if (previouslyReceivedMarkers.stream().filter(e -> e.getFromProcess_id() == event.getFromProcess_id())
				.findAny().isPresent()) {
			event.setCase_(Case.THIS_MARKER_RECEIVED);
		} else {
			event.setCase_(Case.THIS_MARKER_NOT_RECEIVED);
		}
	}

	private void calculateCaseForReceiveMessageEvent(Event event, List<Event> events) {
		List<Event> previouslyReceivedMarkers = events.stream().filter(
				e -> e.getEventType().equals(EventType.RECEIVE_MARKER) && events.indexOf(e) < events.indexOf(event))
				.collect(Collectors.toList());
		Optional<Event> snapshotEvent = events.stream().filter(e -> e.getEventType().equals(EventType.START_SNAPSHOT))
				.findAny();
		
		if (CollectionUtils.isEmpty(previouslyReceivedMarkers)&& (!snapshotEvent.isPresent() || event.getEventTime()<=snapshotEvent.get().getEventTime())) {
			event.setCase_(Case.NO_MARKER_RECEIVED);
		}
		else if (previouslyReceivedMarkers.stream()
				.filter(e -> e.getFromProcess_id() == event.getFromProcess_id()).findAny().isPresent()) {
			event.setCase_(Case.THIS_MARKER_RECEIVED);
		} else {
			event.setCase_(Case.THIS_MARKER_NOT_RECEIVED);
		}
	}

	public Map<Integer, List<Event>> getProcessToEventsMap() {
		return processToEventsMap;
	}

	public List<Event> getAllEvents() {
		return allEvents;
	}
	
	private void executeReactionForEvent(Event event) {
		switch (event.getReaction()) {
		case START_SNAPSHOT:
			executeStartSnapshot(event);
			break;
		case RECORD_MESSAGE:
			executeRecordMessage(event);
			break;
		case DONT_RECORD_MESSAGE:
			executeDontRecordMessage(event,event.getCase_());
			break;
		case RECORD_STATE_AND_SEND_MARKER:
			executeRecordStateAndSendMarker(event);
			break;
		case STOPP_THIS_RECORD:
			executeStopThisRecord(event);
			break;
		default:
			break;
		}
	}

	public void calculateSteps() {
		for(Event e : allEvents) {
			executeReactionForEvent(e);
		}
		display.doFinalCleanup();
	}
	
	private void executeRecordMessage(Event event) {
		int processId = event.getProcess_id();
		int fromProcessId = event.getFromProcess_id();
		int messageNumber = event.getNumber_message();
		
		display.doRecordMessage(processId,fromProcessId,messageNumber);
	}
	
	private void executeDontRecordMessage(Event event, Case case_) {
		int processId = event.getProcess_id();
		int fromProcessId = event.getFromProcess_id();
		int messageNumber = event.getNumber_message();
		
		display.doNotRecordMessage(processId,fromProcessId,messageNumber,case_);
	}
	
	private void executeRecordStateAndSendMarker(Event event) {
		int processId = event.getProcess_id();
		int fromProcessId = event.getFromProcess_id();
		
		display.doRecordStateAndSendMarker(processId,fromProcessId);
	}
	
	private void executeStartSnapshot(Event event) {
		int processId = event.getProcess_id();
		int fromProcessId = event.getFromProcess_id();
		
		display.doStartSnapshot(processId,fromProcessId);	
	}
	
	private void executeStopThisRecord(Event event) {
		int processId = event.getProcess_id();
		int fromProcessId = event.getFromProcess_id();
		
		display.doStopThisRecord(processId, fromProcessId);
	}
}
