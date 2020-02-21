package generators.network;
/*
 * ChandyLamport.java
 * Gregor Heß, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.addons.Slide;
import algoanim.animalscript.addons.bbcode.NetworkStyle;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.network.chandylamport.Display;
import generators.network.chandylamport.Event;
import generators.network.chandylamport.Model;
import generators.network.chandylamport.Event.EventType;
import translator.Translator;

public class ChandyLamport implements ValidatingGenerator {
	private Language lang;

	private int[] P1_P2;
	private int[] P1_P3;
	private int[] P2_P1;
	private int[] P2_P3;
	private int[] P3_P1;
	private int[] P3_P2;

	private int delay_P1_P2;
	private int delay_P1_P3;
	private int delay_P2_P3;

	private int snapshot_process;
	private int snapshot_starttime;

	public static final int P1 = 1;
	public static final int P2 = 2;
	public static final int P3 = 3;

	private Locale locale;
	private String path;
	private NetworkStyle networkStyle;
	private Translator translator;

	public void init() {
		lang = new AnimalScript("Chandy Lamport", "Sven Dotzauer-Klier, Gregor Heß", 800, 600);
		lang.setStepMode(true);
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
		showSlide("LBL_TITLE_SLIDE","TITLESLIDE");
	}

	private void showSlide(String stepLabel, String slideName) {
		Text t1 = lang.newText(new Coordinates(20, 30), translator.translateMessage("ALGOCLASS"), "header1", null,
				(TextProperties) networkStyle.getProperties("h1"));
		Text t2 = lang.newText(new Offset(0, 5, "header1", AnimalScript.DIRECTION_SW),
				translator.translateMessage("ALGONAME"), "header2", null,
				(TextProperties) networkStyle.getProperties("h2"));

		lang.nextStep(stepLabel);

		Slide slide = new Slide(lang, getResource(slideName, true), "header2", networkStyle);
		slide.hide();
		t1.hide();
		t2.hide();
	}

	public ChandyLamport(String path, Locale locale) {
		this.path = path;
		this.locale = locale;
		this.networkStyle = new NetworkStyle();
		translator = new Translator(path, locale);
	}

	public static void main(String[] args) {
		Generator generator = new ChandyLamport("resources\\ChandyLamport", Locale.US); // Generator
																														// erzeugen
		Animal.startGeneratorWindow(generator); // Animal mit Generator starten
	}

	private String getResource(String id, boolean languageDependant) {
		String trans = translator.getResourceBundle().getMessage(id, false);
		String res = new String();
		if (trans == null || trans == "") {
			res = path + "_" + id;
			if (languageDependant) {
				res = res.concat("." + locale);
			}
		} else {
			res = translator.translateMessage(id);
		}
		return res;
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		Map<Integer, List<Event>> processToEventsMap = generateMessageAndMarkerEvents();

		Display display = new Display(lang, translator);
		display.init(processToEventsMap);

		Model model = new Model(processToEventsMap, display);
		model.calculateSteps();
		
		display.generateFinalQuestions();

		lang.hideAllPrimitives();
		display.hideAllMarkers();
		
		showSlide("conclusion", "FINAL");
		
		lang.finalizeGeneration();
		return lang.toString();
	}

	private Map<Integer, List<Event>> generateMessageAndMarkerEvents() {
		Map<Integer, List<Event>> processToEventsMap = collectAllMessageEvents();
		collectAllMarkerEvents().forEach(e -> processToEventsMap.get(e.getProcess_id()).add(e));
		return processToEventsMap;
	}

	private Map<Integer, List<Event>> collectAllMessageEvents() {
		Map<Integer, List<Event>> processToEventsMap = new HashMap<>();

		List<Event> eventsForP1 = new LinkedList<>();
		List<Event> eventsForP2 = new LinkedList<>();
		List<Event> eventsForP3 = new LinkedList<>();

		eventsForP1.addAll(collectMessageEvents(2, 1, delay_P1_P2, P2_P1));
		eventsForP1.addAll(collectMessageEvents(3, 1, delay_P1_P3, P3_P1));
		processToEventsMap.put(P1, eventsForP1);

		eventsForP2.addAll(collectMessageEvents(1, 2, delay_P1_P2, P1_P2));
		eventsForP2.addAll(collectMessageEvents(3, 2, delay_P2_P3, P3_P2));
		processToEventsMap.put(P2, eventsForP2);

		eventsForP3.addAll(collectMessageEvents(1, 3, delay_P1_P3, P1_P3));
		eventsForP3.addAll(collectMessageEvents(2, 3, delay_P2_P3, P2_P3));
		processToEventsMap.put(P3, eventsForP3);

		return processToEventsMap;
	}

	private List<Event> collectMessageEvents(int fromProcess, int toProcess, int delay, int[] messages) {
		List<Event> events = new LinkedList<>();
		if (messages == null) {
			return events;
		}
		for (int i = 0; i < messages.length; i++) {
			EventType eventType = EventType.RECEIVE_MESSAGE;
			int process_id = toProcess;
			int fromProcess_id = fromProcess;
			int number_message = i;
			int timeMessageWasSent = messages[i];
			int eventTime = timeMessageWasSent + delay;
			Event event = new Event(eventType, process_id, fromProcess_id, number_message, timeMessageWasSent,
					eventTime);
			events.add(event);
		}
		return events;
	}

	private List<Event> collectAllMarkerEvents() {
		List<Event> markerEvents = new LinkedList<>();
		switch (snapshot_process) {
		case P1:
			markerEvents.add(new Event(EventType.START_SNAPSHOT, P1, -1, -1, -1, snapshot_starttime));
			markerEvents.add(new Event(EventType.RECEIVE_MARKER, P2, P1, -1, snapshot_starttime,
					snapshot_starttime + delay_P1_P2));
			markerEvents.add(new Event(EventType.RECEIVE_MARKER, P3, P1, -1, snapshot_starttime,
					snapshot_starttime + delay_P1_P3));
			markerEvents.add(new Event(EventType.RECEIVE_MARKER, P1, P2, -1, snapshot_starttime + delay_P1_P2,
					snapshot_starttime + delay_P1_P2 + delay_P1_P2));
			markerEvents.add(new Event(EventType.RECEIVE_MARKER, P3, P2, -1, snapshot_starttime + delay_P1_P2,
					snapshot_starttime + delay_P1_P2 + delay_P2_P3));
			markerEvents.add(new Event(EventType.RECEIVE_MARKER, P1, P3, -1, snapshot_starttime + delay_P1_P3,
					snapshot_starttime + delay_P1_P3 + delay_P1_P3));
			markerEvents.add(new Event(EventType.RECEIVE_MARKER, P2, P3, -1, snapshot_starttime + delay_P1_P3,
					snapshot_starttime + delay_P1_P3 + delay_P2_P3));
			break;
		case P2:
			markerEvents.add(new Event(EventType.START_SNAPSHOT, P2, -1, -1, -1, snapshot_starttime));
			markerEvents.add(new Event(EventType.RECEIVE_MARKER, P1, P2, -1, snapshot_starttime,
					snapshot_starttime + delay_P1_P2));
			markerEvents.add(new Event(EventType.RECEIVE_MARKER, P3, P2, -1, snapshot_starttime,
					snapshot_starttime + delay_P2_P3));
			markerEvents.add(new Event(EventType.RECEIVE_MARKER, P2, P1, -1, snapshot_starttime + delay_P1_P2,
					snapshot_starttime + delay_P1_P2 + delay_P1_P2));
			markerEvents.add(new Event(EventType.RECEIVE_MARKER, P3, P1, -1, snapshot_starttime + delay_P1_P2,
					snapshot_starttime + delay_P1_P2 + delay_P1_P3));
			markerEvents.add(new Event(EventType.RECEIVE_MARKER, P1, P3, -1, snapshot_starttime + delay_P2_P3,
					snapshot_starttime + delay_P2_P3 + delay_P1_P3));
			markerEvents.add(new Event(EventType.RECEIVE_MARKER, P2, P3, -1, snapshot_starttime + delay_P2_P3,
					snapshot_starttime + delay_P2_P3 + delay_P2_P3));
			break;
		case P3:
			markerEvents.add(new Event(EventType.START_SNAPSHOT, P3, -1, -1, -1, snapshot_starttime));
			markerEvents.add(new Event(EventType.RECEIVE_MARKER, P1, P3, -1, snapshot_starttime,
					snapshot_starttime + delay_P1_P3));
			markerEvents.add(new Event(EventType.RECEIVE_MARKER, P2, P3, -1, snapshot_starttime,
					snapshot_starttime + delay_P2_P3));
			markerEvents.add(new Event(EventType.RECEIVE_MARKER, P1, P2, -1, snapshot_starttime + delay_P2_P3,
					snapshot_starttime + delay_P2_P3 + delay_P1_P2));
			markerEvents.add(new Event(EventType.RECEIVE_MARKER, P3, P2, -1, snapshot_starttime + delay_P2_P3,
					snapshot_starttime + delay_P2_P3 + delay_P2_P3));
			markerEvents.add(new Event(EventType.RECEIVE_MARKER, P2, P1, -1, snapshot_starttime + delay_P1_P3,
					snapshot_starttime + delay_P1_P3 + delay_P1_P2));
			markerEvents.add(new Event(EventType.RECEIVE_MARKER, P3, P1, -1, snapshot_starttime + delay_P1_P3,
					snapshot_starttime + delay_P1_P3 + delay_P1_P3));
			break;
		default:
			break;
		}
		return markerEvents;
	}

	public String getName() {
		return "Chandy Lamport";
	}

	public String getAlgorithmName() {
		return "Chandy Lamport";
	}

	public String getAnimationAuthor() {
		return "Sven Dotzauer-Klier, Gregor Heß";
	}

	public String getDescription() {
		return translator.translateMessage("description");
	}

	public String getCodeExample() {
		try {
			return new String(Files.readAllBytes(Paths.get(getResource("CODEEXAMPLE", true))));
		} catch (IOException e) {
			
		}
		return null;
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return locale;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_NETWORK);
	}

	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer arg0, Hashtable<String, Object> primitives)
			throws IllegalArgumentException {

		P1_P2 = (int[]) primitives.get("P1_P2");
		P1_P3 = (int[]) primitives.get("P1_P3");
		P2_P1 = (int[]) primitives.get("P2_P1");
		P2_P3 = (int[]) primitives.get("P2_P3");
		P3_P1 = (int[]) primitives.get("P3_P1");
		P3_P2 = (int[]) primitives.get("P3_P2");

		delay_P1_P2 = (int) primitives.get("delay P1_P2");
		delay_P1_P3 = (int) primitives.get("delay P1_P3");
		delay_P2_P3 = (int) primitives.get("delay P2_P3");

		snapshot_process = (int) primitives.get("snapshot_process");
		snapshot_starttime = (int) primitives.get("snapshot_starttime");

		if (!(snapshot_process == 1 || snapshot_process == 2 || snapshot_process == 3))
			throw new IllegalArgumentException(translator.translateMessage("snapshot_process_exception"));
		if (snapshot_starttime < 0)
			throw new IllegalArgumentException(translator.translateMessage("snapshot_starttime_exception"));
		if (delay_P1_P2 < 0)
			throw new IllegalArgumentException(translator.translateMessage("delay_exception", "P1", "P2"));
		if (delay_P1_P3 < 0)
			throw new IllegalArgumentException(translator.translateMessage("delay_exception", "P1", "P3"));
		if (delay_P2_P3 < 0)
			throw new IllegalArgumentException(translator.translateMessage("delay_exception", "P2", "P3"));
		if (checkArrayForNegativeValues(P1_P2) || checkArrayForNegativeValues(P1_P3)
				|| checkArrayForNegativeValues(P2_P1) || checkArrayForNegativeValues(P2_P3)
				|| checkArrayForNegativeValues(P3_P1) || checkArrayForNegativeValues(P3_P2))
			throw new IllegalArgumentException(translator.translateMessage("negative_message_sent_time_exception"));
		return true;
	}

	private boolean checkArrayForNegativeValues(int[] array) {
		for (int i : array) {
			if (i < 0)
				return true;
		}
		return false;
	}

}