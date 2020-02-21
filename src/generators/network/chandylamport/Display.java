package generators.network.chandylamport;

import java.awt.Color;
import java.awt.Font;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import generators.network.ChandyLamport;
import generators.network.chandylamport.Event.Case;
import generators.network.chandylamport.Event.EventType;
import interactionsupport.models.MultipleChoiceQuestionModel;
import translator.Translator;

public class Display {

	private Language lang;
	private List<Polyline> processLines;
	private List<Process> processes;
	private List<Text> matrixHeaders;
	private PolylineProperties messageProperties;
	private TextProperties lineTextProperties;
	private TextProperties headerProperty;
	private TextProperties aufzeichnungenProperty;

	private MatrixProperties matrixProperties;
	private Translator translator;

	public Display(Language lang, Translator translator) {
		this.lang = lang;
		this.translator = translator;

		processLines = new LinkedList<>();
		matrixHeaders = new LinkedList<>();

		processes = new LinkedList<>();
		processes.add(new Process(ChandyLamport.P1));
		processes.add(new Process(ChandyLamport.P2));
		processes.add(new Process(ChandyLamport.P3));

		messageProperties = new PolylineProperties();
		messageProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
		messageProperties.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);

		lineTextProperties = new TextProperties();
		lineTextProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 12));
		lineTextProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);

		matrixProperties = new MatrixProperties();
		matrixProperties.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "matrix");
		matrixProperties.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, new Color(192, 96, 0));
		matrixProperties.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.GREEN);
		matrixProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 11));

		headerProperty = new TextProperties();
		headerProperty.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 24));

		aufzeichnungenProperty = new TextProperties();
		aufzeichnungenProperty.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 12));
	}

	public void init(Map<Integer, List<Event>> processToEventsMap) {
		lang.newText(new Coordinates(10, 15), "Chandy Lamport", "header", null, headerProperty);

		int maximalProcessDuration = calculateMaximalProcessDuration(processToEventsMap);
		initializeProcessLines(maximalProcessDuration);
		initializeMatrixHeaders();
		generateMarkers(processToEventsMap);
		generateMessages(processToEventsMap);
		int maximalLinesOfMatrix = calculateMaximalLinesOfMatrix(processToEventsMap);
		generateMatrixes(processToEventsMap, maximalLinesOfMatrix);
		generateCodeBoxes();
		prepareInitialStep();
	}

	private void prepareInitialStep() {
		processes.forEach(p -> p.getProcessToMessageMap().values().forEach(ml -> ml.forEach(m -> {
			m.getMessageLine().hide();
			m.getMessageText().hide();
		})));
		processes.forEach(p -> p.getProcessToMarkerMap().values().forEach(m -> {
			m.getMarkerLine().hide();
			m.getMarkerText().hide();
		}));
		processes.forEach(p -> {
			int rows = p.getStringMatrix().getNrRows();
			for (int i = 0; i < 3; i++) {
				for (int j = 1; j < rows; j++) {
					p.getStringMatrix().setGridTextColor(j, i, Color.WHITE, null, null);
				}
			}
		});
	}

	private void initializeMatrixHeaders() {
		matrixHeaders.add(lang.newText(new Offset(30, 0, "P3", AnimalScript.DIRECTION_NE),
				translator.translateMessage("records") + " P1:", "Aufzeichnungen_P1", null, aufzeichnungenProperty));
		matrixHeaders.add(lang.newText(new Offset(30 + 280, 0, "P3", AnimalScript.DIRECTION_NE),
				translator.translateMessage("records") + " P2:", "Aufzeichnungen_P2", null, aufzeichnungenProperty));
		matrixHeaders.add(lang.newText(new Offset(30 + 560, 0, "P3", AnimalScript.DIRECTION_NE),
				translator.translateMessage("records") + " P3:", "Aufzeichnungen_P3", null, aufzeichnungenProperty));
	}

	private void initializeProcessLines(int maximalProcessDuration) {
		processLines.add(lang.newPolyline(
				new Node[] { new Offset(5, 50, "header", AnimalScript.DIRECTION_NW),
						new Offset(5, maximalProcessDuration + 50, "header", AnimalScript.DIRECTION_NW) },
				"line1", null));
		processLines.add(lang.newPolyline(
				new Node[] { new Offset(155, 50, "header", AnimalScript.DIRECTION_NW),
						new Offset(155, maximalProcessDuration + 50, "header", AnimalScript.DIRECTION_NW) },
				"line2", null));
		processLines.add(lang.newPolyline(
				new Node[] { new Offset(310, 50, "header", AnimalScript.DIRECTION_NW),
						new Offset(310, maximalProcessDuration + 50, "header", AnimalScript.DIRECTION_NW) },
				"line3", null));

		TextProperties lineTextProperties_ = new TextProperties();
		lineTextProperties_.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 12));
		lineTextProperties_.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		Text line1 = lang.newText(new Offset(10, 0, "line1", AnimalScript.DIRECTION_NW), "P1", "P1", null,
				lineTextProperties_);
		Text line2 = lang.newText(new Offset(10, 0, "line2", AnimalScript.DIRECTION_NW), "P2", "P2", null,
				lineTextProperties_);
		Text line3 = lang.newText(new Offset(10, 0, "line3", AnimalScript.DIRECTION_NW), "P3", "P3", null,
				lineTextProperties_);
	}

	private int calculateMaximalProcessDuration(Map<Integer, List<Event>> processToEventsMap) {
		List<Event> messageAndMarkerEventsForP1 = processToEventsMap.get(ChandyLamport.P1).stream()
				.filter(e -> e.getEventType().equals(EventType.RECEIVE_MESSAGE)
						|| e.getEventType().equals(EventType.RECEIVE_MARKER))
				.collect(Collectors.toList());
		List<Event> messageAndMarkerEventsForP2 = processToEventsMap.get(ChandyLamport.P2).stream()
				.filter(e -> e.getEventType().equals(EventType.RECEIVE_MESSAGE)
						|| e.getEventType().equals(EventType.RECEIVE_MARKER))
				.collect(Collectors.toList());
		List<Event> messageAndMarkerEventsForP3 = processToEventsMap.get(ChandyLamport.P3).stream()
				.filter(e -> e.getEventType().equals(EventType.RECEIVE_MESSAGE)
						|| e.getEventType().equals(EventType.RECEIVE_MARKER))
				.collect(Collectors.toList());

		int maxTimeForP1 = messageAndMarkerEventsForP1.stream().map(e -> e.getEventTime()).reduce(Math::max).get();
		int maxTimeForP2 = messageAndMarkerEventsForP2.stream().map(e -> e.getEventTime()).reduce(Math::max).get();
		int maxTimeForP3 = messageAndMarkerEventsForP3.stream().map(e -> e.getEventTime()).reduce(Math::max).get();

		return 10 * Math.max(maxTimeForP1, Math.max(maxTimeForP2, maxTimeForP3));
	}

	private void generateMatrixes(Map<Integer, List<Event>> processToEventsMap, int maximalDimension) {
		int lines = (maximalDimension < 1) ? 2 : (1 + maximalDimension);

		List<Event> messagesEventsForProcessP1 = processToEventsMap.get(ChandyLamport.P1).stream()
				.filter(e -> e.getEventType().equals(EventType.RECEIVE_MESSAGE)).collect(Collectors.toList());
		List<Event> messagesEventsForProcessP2 = processToEventsMap.get(ChandyLamport.P2).stream()
				.filter(e -> e.getEventType().equals(EventType.RECEIVE_MESSAGE)).collect(Collectors.toList());
		List<Event> messagesEventsForProcessP3 = processToEventsMap.get(ChandyLamport.P3).stream()
				.filter(e -> e.getEventType().equals(EventType.RECEIVE_MESSAGE)).collect(Collectors.toList());

		String[][] matrixForP1 = new String[lines][3];
		String[][] matrixForP2 = new String[lines][3];
		String[][] matrixForP3 = new String[lines][3];

		initializeMessageMatrix(messagesEventsForProcessP1, matrixForP1, ChandyLamport.P1);
		initializeMessageMatrix(messagesEventsForProcessP2, matrixForP2, ChandyLamport.P2);
		initializeMessageMatrix(messagesEventsForProcessP3, matrixForP3, ChandyLamport.P3);

		StringMatrix table1 = lang.newStringMatrix(
				new Offset(-5, 30, matrixHeaders.get(ChandyLamport.P1 - 1), AnimalScript.DIRECTION_NW), matrixForP1,
				"table1", null, matrixProperties);
		StringMatrix table2 = lang.newStringMatrix(
				new Offset(-5, 30, matrixHeaders.get(ChandyLamport.P2 - 1), AnimalScript.DIRECTION_NW), matrixForP2,
				"table1", null, matrixProperties);
		StringMatrix table3 = lang.newStringMatrix(
				new Offset(-5, 30, matrixHeaders.get(ChandyLamport.P3 - 1), AnimalScript.DIRECTION_NW), matrixForP3,
				"table1", null, matrixProperties);

		processes.get(ChandyLamport.P1 - 1).setStringMatrix(table1);
		processes.get(ChandyLamport.P2 - 1).setStringMatrix(table2);
		processes.get(ChandyLamport.P3 - 1).setStringMatrix(table3);

	}

	private int calculateMaximalLinesOfMatrix(Map<Integer, List<Event>> processToEventsMap) {
		List<Event> messagesEventsForProcessP1 = processToEventsMap.get(ChandyLamport.P1).stream()
				.filter(e -> e.getEventType().equals(EventType.RECEIVE_MESSAGE)).collect(Collectors.toList());
		List<Event> messagesEventsForProcessP2 = processToEventsMap.get(ChandyLamport.P2).stream()
				.filter(e -> e.getEventType().equals(EventType.RECEIVE_MESSAGE)).collect(Collectors.toList());
		List<Event> messagesEventsForProcessP3 = processToEventsMap.get(ChandyLamport.P3).stream()
				.filter(e -> e.getEventType().equals(EventType.RECEIVE_MESSAGE)).collect(Collectors.toList());
		int linesForP1 = 0;
		int linesForP2 = 0;
		int linesForP3 = 0;
		if (CollectionUtils.isNotEmpty(messagesEventsForProcessP1)) {
			linesForP1 = messagesEventsForProcessP1.stream().map(e -> e.getNumber_message()).reduce(Math::max).get();
		}
		if (CollectionUtils.isNotEmpty(messagesEventsForProcessP2)) {
			linesForP2 = messagesEventsForProcessP2.stream().map(e -> e.getNumber_message()).reduce(Math::max).get();
		}
		if (CollectionUtils.isNotEmpty(messagesEventsForProcessP3)) {
			linesForP3 = messagesEventsForProcessP3.stream().map(e -> e.getNumber_message()).reduce(Math::max).get();
		}
		return Math.max(linesForP1, Math.max(linesForP2, linesForP3)) + 1;
	}

	private void initializeMessageMatrix(List<Event> messageEventsForProcess, String[][] messageMatrixForProcess,
			int processId) {
		switch (processId) {
		case ChandyLamport.P1:
			messageMatrixForProcess[0][0] = "P1->P1";
			messageMatrixForProcess[0][1] = "P2->P1";
			messageMatrixForProcess[0][2] = "P3->P1";
			messageMatrixForProcess[1][0] = "P1 (state)";
			break;
		case ChandyLamport.P2:
			messageMatrixForProcess[0][0] = "P1->P2";
			messageMatrixForProcess[0][1] = "P2->P2";
			messageMatrixForProcess[0][2] = "P3->P2";
			messageMatrixForProcess[1][1] = "P2 (state)";
			break;
		case ChandyLamport.P3:
			messageMatrixForProcess[0][0] = "P1->P3";
			messageMatrixForProcess[0][1] = "P2->P3";
			messageMatrixForProcess[0][2] = "P3->P3";
			messageMatrixForProcess[1][2] = "P3 (state)";
			break;
		default:
			break;
		}
		for (Event event : messageEventsForProcess) {
			int col = event.getFromProcess_id() - 1;
			int line = event.getNumber_message() + 1;
			messageMatrixForProcess[line][col] = "m" + event.getFromProcess_id() + "_" + event.getProcess_id() + "_"
					+ event.getNumber_message();
		}
	}

	private void generateMarkers(Map<Integer, List<Event>> processToEventsMap) {
		List<Event> markerEventsForProcessP1 = processToEventsMap.get(ChandyLamport.P1).stream()
				.filter(e -> e.getEventType().equals(EventType.RECEIVE_MARKER)).collect(Collectors.toList());
		List<Event> markerEventsForProcessP2 = processToEventsMap.get(ChandyLamport.P2).stream()
				.filter(e -> e.getEventType().equals(EventType.RECEIVE_MARKER)).collect(Collectors.toList());
		List<Event> markerEventsForProcessP3 = processToEventsMap.get(ChandyLamport.P3).stream()
				.filter(e -> e.getEventType().equals(EventType.RECEIVE_MARKER)).collect(Collectors.toList());
		generateMarkersForProcess(markerEventsForProcessP1, ChandyLamport.P1);
		generateMarkersForProcess(markerEventsForProcessP2, ChandyLamport.P2);
		generateMarkersForProcess(markerEventsForProcessP3, ChandyLamport.P3);
	}

	private void generateMessages(Map<Integer, List<Event>> processToEventsMap) {
		List<Event> messagesEventsForProcessP1 = processToEventsMap.get(ChandyLamport.P1).stream()
				.filter(e -> e.getEventType().equals(EventType.RECEIVE_MESSAGE)).collect(Collectors.toList());
		List<Event> messagesEventsForProcessP2 = processToEventsMap.get(ChandyLamport.P2).stream()
				.filter(e -> e.getEventType().equals(EventType.RECEIVE_MESSAGE)).collect(Collectors.toList());
		List<Event> messagesEventsForProcessP3 = processToEventsMap.get(ChandyLamport.P3).stream()
				.filter(e -> e.getEventType().equals(EventType.RECEIVE_MESSAGE)).collect(Collectors.toList());
		generateMessagesForProcess(messagesEventsForProcessP1, ChandyLamport.P1);
		generateMessagesForProcess(messagesEventsForProcessP2, ChandyLamport.P2);
		generateMessagesForProcess(messagesEventsForProcessP3, ChandyLamport.P3);
	}

	private void generateMarkersForProcess(List<Event> markerEventsForProcess, int processId) {
		for (Event event : markerEventsForProcess) {
			Offset start = new Offset(0, event.getTimeMessageWasSent() * 10,
					processLines.get(event.getFromProcess_id() - 1), AnimalScript.DIRECTION_NW);
			Offset end = new Offset(0, event.getEventTime() * 10, processLines.get(event.getProcess_id() - 1),
					AnimalScript.DIRECTION_NW);
			Polyline markerLine = lang.newPolyline(new Node[] { start, end },
					"markerLine_" + event.getFromProcess_id() + "_" + event.getProcess_id(), null, messageProperties);
			Text markerText = lang.newText(new Offset(0, -15, markerLine, AnimalScript.DIRECTION_C),
					"marker_" + event.getFromProcess_id() + "_" + event.getProcess_id(),
					"marker_" + event.getFromProcess_id() + "_" + event.getProcess_id(), null, lineTextProperties);
			Marker marker = new Marker(markerText, markerLine);
			processes.get(processId - 1).putToMarkerMap(event.getFromProcess_id(), marker);
		}
	}

	private void generateMessagesForProcess(List<Event> messageEventsForProcess, int processId) {
		List<Event> messageEventsForProcessOrdered = new LinkedList<>();
		messageEventsForProcessOrdered.addAll(messageEventsForProcess);
		messageEventsForProcessOrdered.sort(Comparator.comparing(Event::getNumber_message));
		for (Event event : messageEventsForProcess) {
			Offset start = new Offset(0, event.getTimeMessageWasSent() * 10,
					processLines.get(event.getFromProcess_id() - 1), AnimalScript.DIRECTION_NW);
			Offset end = new Offset(0, event.getEventTime() * 10, processLines.get(event.getProcess_id() - 1),
					AnimalScript.DIRECTION_NW);
			Polyline messageLine = lang.newPolyline(new Node[] { start, end },
					"messageLine_" + event.getFromProcess_id() + "_" + event.getProcess_id(), null, messageProperties);
			Text messageText = lang.newText(new Offset(0, -15, messageLine, AnimalScript.DIRECTION_C),
					"m" + event.getFromProcess_id() + "_" + event.getProcess_id() + "_" + event.getNumber_message(),
					"m" + event.getFromProcess_id() + "_" + event.getProcess_id() + "_" + event.getNumber_message(),
					null, lineTextProperties);
			Message message = new Message(messageText, messageLine, event.getNumber_message() + 1,
					event.getFromProcess_id() - 1);
			processes.get(processId - 1).putToMessageMap(event.getFromProcess_id(), message);
		}
	}

	private void generateCodeBoxes() {
		CodeBox box1 = new CodeBox(
				new Offset(-15, -15, matrixHeaders.get(ChandyLamport.P1 - 1), AnimalScript.DIRECTION_NW),
				new Offset(0, 10, processes.get(ChandyLamport.P1 - 1).getStringMatrix(), AnimalScript.DIRECTION_SW),
				lang, translator, ChandyLamport.P1);
		processes.get(ChandyLamport.P1 - 1).setCodeBox(box1);

		CodeBox box2 = new CodeBox(
				new Offset(-15, -15, matrixHeaders.get(ChandyLamport.P2 - 1), AnimalScript.DIRECTION_NW),
				new Offset(0, 10, processes.get(ChandyLamport.P2 - 1).getStringMatrix(), AnimalScript.DIRECTION_SW),
				lang, translator, ChandyLamport.P2);
		processes.get(ChandyLamport.P2 - 1).setCodeBox(box2);

		CodeBox box3 = new CodeBox(
				new Offset(-15, -15, matrixHeaders.get(ChandyLamport.P3 - 1), AnimalScript.DIRECTION_NW),
				new Offset(0, 10, processes.get(ChandyLamport.P3 - 1).getStringMatrix(), AnimalScript.DIRECTION_SW),
				lang, translator, ChandyLamport.P3);
		processes.get(ChandyLamport.P3 - 1).setCodeBox(box3);
	}

	private void cleanup() {
		processes.forEach(p -> p.cleanup());
	}

	public void doFinalCleanup() {
		lang.nextStep();
		cleanup();
	}

	public void doRecordMessage(int processId, int fromProcessId, int messageNumber) {
		lang.nextStep(translator.translateMessage("record_message"));
		cleanup();
		processes.get(processId - 1).displayRecordMessage(fromProcessId, messageNumber);
	}

	public void doNotRecordMessage(int processId, int fromProcessId, int messageNumber, Case case_) {
		lang.nextStep(translator.translateMessage("dont_record_message"));
		cleanup();
		processes.get(processId - 1).displayDoNotRecordMessage(fromProcessId, messageNumber,case_);
	}

	public void doRecordStateAndSendMarker(int processId, int fromProcessId) {
		lang.nextStep(translator.translateMessage("send_marker"));
		cleanup();
		processes.get(processId - 1).displayRecordState(fromProcessId);
		processes.stream().filter(p -> processes.indexOf(p) != (processId - 1))
				.forEach(pr -> pr.displayMarkerOnTheWay(processId));
	}

	public void doStopThisRecord(int processId, int fromProcessId) {
		lang.nextStep(translator.translateMessage("stop_this_recording"));
		cleanup();
		processes.get(processId - 1).displayStopThisRecord(fromProcessId);
	}

	public void doStartSnapshot(int processId, int fromProcessId) {
		lang.nextStep(translator.translateMessage("start_snapshot"));
		cleanup();
		processes.get(processId - 1).displayStartSnapshot(fromProcessId);
		processes.stream().filter(p -> processes.indexOf(p) != (processId - 1))
				.forEach(pr -> pr.displayMarkerOnTheWay(processId));
	}
	
	public void generateFinalQuestions() {
		lang.nextStep(translator.translateMessage("firstQuestion"));
		
		MultipleChoiceQuestionModel firstMarkerReceived = new MultipleChoiceQuestionModel("firstMarkerReceivedQuestion");
	    firstMarkerReceived.setPrompt(translator.translateMessage("firstQuestionPrompt"));
	    firstMarkerReceived.addAnswer(translator.translateMessage("firstQuestionAnswerOption1"), 0, translator.translateMessage("firstQuestionAnswerOptionResult1"));
	    firstMarkerReceived.addAnswer(translator.translateMessage("firstQuestionAnswerOption2"), 1, translator.translateMessage("firstQuestionAnswerOptionResult2"));
	    firstMarkerReceived.addAnswer(translator.translateMessage("firstQuestionAnswerOption3"), 0, translator.translateMessage("firstQuestionAnswerOptionResult3"));
	    firstMarkerReceived.addAnswer(translator.translateMessage("firstQuestionAnswerOption4"), 0, translator.translateMessage("firstQuestionAnswerOptionResult4"));
	    lang.addMCQuestion(firstMarkerReceived);
	    
	    lang.nextStep(translator.translateMessage("secondQuestion"));
	    
	    MultipleChoiceQuestionModel messageReceivedNoMarkerReceived = new MultipleChoiceQuestionModel("messageReceivedNoMarkerReceived");
	    messageReceivedNoMarkerReceived.setPrompt(translator.translateMessage("secondQuestionPrompt"));
	    messageReceivedNoMarkerReceived.addAnswer(translator.translateMessage("secondQuestionAnswerOption1"), 0, translator.translateMessage("secondQuestionAnswerOptionResult1"));
	    messageReceivedNoMarkerReceived.addAnswer(translator.translateMessage("secondQuestionAnswerOption2"), 1, translator.translateMessage("secondQuestionAnswerOptionResult2"));
	    lang.addMCQuestion(messageReceivedNoMarkerReceived);
	    
	    lang.nextStep(translator.translateMessage("thirdQuestion"));
	    
	    MultipleChoiceQuestionModel messageReceivedAndMarkerReceived = new MultipleChoiceQuestionModel("messageReceivedAndMarkerReceived");
	    messageReceivedAndMarkerReceived.setPrompt(translator.translateMessage("thirdQuestionPrompt"));
	    messageReceivedAndMarkerReceived.addAnswer(translator.translateMessage("thirdQuestionAnswerOption1"), 0, translator.translateMessage("thirdQuestionAnswerOptionResult1"));
	    messageReceivedAndMarkerReceived.addAnswer(translator.translateMessage("thirdQuestionAnswerOption2"), 1, translator.translateMessage("thirdQuestionAnswerOptionResult2"));
	    lang.addMCQuestion(messageReceivedAndMarkerReceived);
	    
	    lang.nextStep(translator.translateMessage("forthQuestion"));
	    
	    MultipleChoiceQuestionModel markerReceived = new MultipleChoiceQuestionModel("markerReceived");
	    markerReceived.setPrompt(translator.translateMessage("forthQuestionPrompt"));
	    markerReceived.addAnswer(translator.translateMessage("forthQuestionAnswerOption1"), 0, translator.translateMessage("forthQuestionAnswerOptionResult1"));
	    markerReceived.addAnswer(translator.translateMessage("forthQuestionAnswerOption2"), 1, translator.translateMessage("forthQuestionAnswerOptionResult2"));
	    markerReceived.addAnswer(translator.translateMessage("forthQuestionAnswerOption3"), 0, translator.translateMessage("forthQuestionAnswerOptionResult3"));
	    lang.addMCQuestion(markerReceived);
	    
	    lang.nextStep(translator.translateMessage("conclusion"));
	}
	
	public void hideAllMarkers() {
		processes.forEach(p -> p.hideAllMarkers());
	}
}
