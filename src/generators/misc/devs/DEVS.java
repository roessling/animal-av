package generators.misc.devs;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.misc.devs.model.Event;

import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.TrueFalseQuestionModel;

import java.awt.Color;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Queue;
import java.util.Random;

import javax.swing.JOptionPane;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.DoubleArray;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

/**
 * @author Andrej Felde (andrej.felde@stud.tu-darmstadt.de)
 * @author Thomas Hesse (thomas.hesse@stud.tu-darmstadt.de)
 * @version 0.7
 * @since 2013
 */
public class DEVS implements ValidatingGenerator {


	// @formatter:off

	private static final String SOURCE_CODE = "public void run(double tmax) {" // 0
			+ "\n    boolean busy = false;" // 1
			+ "\n    double t = 0;" // 2
			+ "\n    int N = 0;" // 3
			+ "\n    while (t < tmax) {" // 4
			+ "\n        Event event = L.get(0);" // 5
			+ "\n        L.remove(0);" // 6
			+ "\n        t = event.getTime();" // 7
			+ "\n        switch (event.getType()) {" // 8
			+ "\n        case 'A':" // 9
			+ "\n            L.add(new Event(t + expQueue.poll(), 'A'));" // 10
			+ "\n            if (busy) {" // 11
			+ "\n                N++;" // 12
			+ "\n            } else {" // 13
			+ "\n                busy = true;" // 14
			+ "\n                L.add(new Event(t + normQueue.poll(), 'D'));" // 15
			+ "\n            }" // 16
			+ "\n            break;" // 17
			+ "\n        case 'D':" // 18
			+ "\n            if (N == 0) {" // 19
			+ "\n                busy = false;" // 20
			+ "\n            } else {" // 21
			+ "\n                N--;" // 22
			+ "\n                L.add(new Event(t + normQueue.poll(), 'D'));" // 23
			+ "\n            }" // 24
			+ "\n            break;" // 25
			+ "\n        }" // 26
			+ "\n        Collections.sort(L);" // 27
			+ "\n    }" // 28
			+ "\n}"; // 29

	private static final String DESCRIPTION = "Der DEVS Algorithmus wird genutzt "
			+ "um diskrete Eventsysteme zu modellieren "
			+ "\n"
			+ "und zu analysieren. Diskrete Eventsysteme werden beschrieben durch"
			+ "\n"
			+ "Zustandstransitionstabellen oder kontinuierliche Zustandssysteme, welche "
			+ "\n"
			+ "wiederrum durch Differentialgleichungen beschrieben werden. Der DEVS Algorithmus"
			+ "\n" + "ist ein zeitbasiertes Eventsystem.";

	private static final int	SOURCE_CODE_DISTANCE	= 35;

	// @formatter:on

	private Language					lang;

	private SourceCode					sc;

	private int							currentLine;

	private Queue<Double>				expQueue;

	private Queue<Double>				normQueue;

	// list is needed because of sortable attribute
	private List<Event>					L;

	/**
	 * Contains mapping between internal representation and the animal
	 * primitives.
	 */
	private HashMap<String, Primitive>	pMap;

	private SourceCodeProperties		scProps;

	private int							normCellPos				= 0;

	private int							expCellPos				= 0;

	private int							cellHeight				= 20;

	private Random						rnd;

	private int							possibilityQuestion;

	private int							questionCounter;
	
	private int							idleCounter;
	
	private int							busyCounter;

	private int							sortCounter;

	private double						tmax;

	@Override
	public void init() {
		lang = new AnimalScript("Discrete Event System Specification", "Andrej Felde, Thomas Hesse", 800, 600);
		lang.setStepMode(true);
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

		expQueue = new LinkedList<Double>();
		normQueue = new LinkedList<Double>();
		L = new LinkedList<Event>();
		pMap = new HashMap<String, Primitive>();
		rnd = new Random();
		questionCounter = 0;
		idleCounter = 0;
		busyCounter = 0;
		sortCounter = 0;
	}

	@Override
	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		init();

		ArrayProperties ap = (ArrayProperties) props.getPropertiesByName("distributionProps");
		scProps = (SourceCodeProperties) props.getPropertiesByName("scProps");
		parseDistributionNumbers((String) primitives.get("normalDistribution"), this.normQueue);
		parseDistributionNumbers((String) primitives.get("exponentialDistribution"), this.expQueue);
		L.add(new Event((Double) primitives.get("initialEvent"), 'A'));
		possibilityQuestion = (Integer) primitives.get("possibilityQuestions (0-100)");
		tmax = (Double) primitives.get("tmax");

		start(ap);
		run();
		end();

		lang.finalizeGeneration();
		return lang.toString();
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		String errorMessage = "";
		boolean error = false;

		String norm = (String) primitives.get("normalDistribution");
		String exp = (String) primitives.get("exponentialDistribution");
		try {
			parseDistributionNumbers(norm, this.normQueue);
		} catch (NumberFormatException e) {
			errorMessage += "Das Format der Werte für die Normalverteilung sind nicht korrekt.\n";
			errorMessage += norm;
			error = true;
		}
		try {
			parseDistributionNumbers(exp, this.expQueue);
		} catch (NumberFormatException e) {
			errorMessage += (error) ? "\n\n" : "";
			errorMessage += "Das Format der Werte für die Exponentialverteilung sind nicht korrekt.\n";
			errorMessage += exp;
			error = true;
		}
		int possQuestions = (Integer) primitives.get("possibilityQuestions (0-100)");
		if (possQuestions < 0 || possQuestions > 100) {
			errorMessage += (error) ? "\n\n" : "";
			errorMessage += "Der Wert für possibilityQuestions (0-100) muss zwischen 0 und 100 liegen.\n";
			errorMessage += "Eingegebener Wert: " + possQuestions;
			error = true;
		}
		if (error) {
			showErrorWindow(errorMessage);
		}
		return !error; // no error found
	}

	private void showErrorWindow(String message) {
		JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), message, "Fehler", JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public String getName() {
		return "Discrete Event System Specification";
	}

	@Override
	public String getAlgorithmName() {
		return "DEVS Algorithm";
	}

	@Override
	public String getAnimationAuthor() {
		return "Andrej Felde, Thomas Hesse";
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	public String getCodeExample() {
		return SOURCE_CODE;
	}

	@Override
	public String getFileExtension() {
		return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	@Override
	public Locale getContentLocale() {
		return Locale.GERMANY;
	}

	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
	}

	@Override
	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

	protected void start(ArrayProperties ap) {
		/* AS: 1 */
		Text title = lang.newText(new Coordinates(20, 26), "DEVS Algorithmus", "header", null);
		pMap.put("algoTitle", title);
		RectProperties rectProperties = new RectProperties();
		rectProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		rectProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		pMap.put("hRect", lang.newRect(new Offset(-5, -5, title, "NW"), new Offset(5, 5, title, "SE"), "hRect", null,
				rectProperties));
		next();

		/* AS: 1.1 */
		sc = lang.newSourceCode(new Offset(0, 45, title, null), "initT", null, scProps);
		sc.addCodeLine("Der DEVS Algorithmus wird genutzt um diskrete Eventsysteme zu modellieren und zu analysieren.",
				"initT", 0, null);
		sc.addCodeLine(
				"Diskrete Eventsysteme werden beschrieben durch Zustandstransitionstabellen oder kontinuierliche Zustandssysteme, ",
				"initT", 0, null);
		sc.addCodeLine("welche wiederrum durch Differentialgleichungen beschrieben werden.", "initT", 0, null);
		sc.addCodeLine("Der DEVS Algorithmus ist ein zeitbasiertes Eventsystem.", "initT", 0, null);
		sc.addCodeLine("", "initT", 0, null);
		sc.addCodeLine("Der Algorithmus wird standardmäßig mit folgenden Werten aufgerufen:", "initT", 0, null);
		sc.addCodeLine("- der Zeit t = 0", "initT", 1, null);
		sc.addCodeLine("- dem Eventtyp E = I (für 'initial event')", "initT", 1, null);
		sc.addCodeLine("- der Warteschlangenlänge N = 0", "initT", 1, null);
		sc.addCodeLine("- dem Status der Warteschlange S = idle", "initT", 1, null);
		sc.addCodeLine("- der Warteschlange L", "initT", 1, null);
		sc.addCodeLine("", "initT", 0, null);
		sc.addCodeLine("In diesem Falle hier wird der Algorithmus noch zusätzlich mit L = {" + L.get(0)
				+ "} aufgerufen.", "initT", 0, null);
		next();
		sc.hide();

		/* AS: 2 */
		sc = lang.newSourceCode(new Offset(0, 45, title, null), "initT", null, scProps);
		sc.addCodeLine("protected void run(double tmax) {", "initT", 0, null);
		sc.addCodeLine("boolean busy = false;", "initT", 1, null);
		sc.addCodeLine("double t = 0;", "initT", 1, null);
		sc.addCodeLine("int N = 0;", "initT", 1, null);
		sc.addCodeLine("while (t < tmax) {", "initT", 1, null);
		sc.addCodeLine("Event event = L.get(0);", "initT", 2, null);
		sc.addCodeLine("L.remove(0);", "initT", 2, null);
		sc.addCodeLine("t = event.getTime();", "initT", 2, null);
		sc.addCodeLine("switch (event.getType()) {", "initT", 2, null);
		sc.addCodeLine("case 'A':", "initT", 2, null);
		sc.addCodeLine("L.add(new Event(t + expQueue.poll(), 'A'));", "initT", 3, null);
		sc.addCodeLine("if (busy) {", "initT", 3, null);
		sc.addCodeLine("N++;", "initT", 4, null);
		sc.addCodeLine("} else {", "initT", 3, null);
		sc.addCodeLine("busy = true;", "initT", 4, null);
		sc.addCodeLine("L.add(new Event(t + normQueue.poll(), 'D'));", "initT", 4, null);
		sc.addCodeLine("}", "initT", 3, null);
		sc.addCodeLine("break;", "initT", 3, null);
		sc.addCodeLine("case 'D':", "initT", 2, null);
		sc.addCodeLine("if (N == 0) {", "initT", 3, null);
		sc.addCodeLine("busy = false;", "initT", 4, null);
		sc.addCodeLine("} else {", "initT", 3, null);
		sc.addCodeLine("N--;", "initT", 4, null);
		sc.addCodeLine("L.add(new Event(t + normQueue.poll(), 'D'));", "initT", 4, null);
		sc.addCodeLine("}", "initT", 3, null);
		sc.addCodeLine("break;", "initT", 3, null);
		sc.addCodeLine("}", "initT", 2, null);
		sc.addCodeLine("Collections.sort(L);", "initT", 2, null);
		sc.addCodeLine("}", "initT", 1, null);
		sc.addCodeLine("}", "initT", 0, null);
		next();

		pMap.put("expLegend",
				lang.newText(new Offset(SOURCE_CODE_DISTANCE, 0, sc, "NE"), "Exponentialverteilung", "expLegend", null));
		pMap.put("normLegend",
				lang.newText(new Offset(SOURCE_CODE_DISTANCE, 70, sc, "NE"), "Normalverteilung", "normLegend", null));

		List<Double> temp;
		temp = new LinkedList<Double>(this.normQueue);
		double[] normData = new double[this.normQueue.size()];
		double[] expData = new double[this.expQueue.size()];
		for (int i = 0; i < normData.length; i++) {
			normData[i] = temp.get(i).doubleValue();
		}
		temp = new LinkedList<Double>(this.expQueue);
		for (int i = 0; i < expData.length; i++) {
			expData[i] = temp.get(i).doubleValue();
		}

		DoubleArray expArray = lang.newDoubleArray(new Offset(SOURCE_CODE_DISTANCE + 150, 0, sc, "NE"), expData,
				"expArray", null, ap);
		expArray.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
		expArray.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE, null, null);

		DoubleArray normArray = lang.newDoubleArray(new Offset(SOURCE_CODE_DISTANCE + 150, 70, sc, "NE"), normData,
				"normArray", null, ap);
		normArray.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
		normArray.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE, null, null);

		pMap.put("normMarker", lang.newArrayMarker(normArray, 0, "normMarker", null));
		pMap.put("expMarker", lang.newArrayMarker(expArray, 0, "expMarker", null));
		pMap.put("normArray", normArray);
		pMap.put("expArray", expArray);

		Text currentEvent = lang.newText(new Offset(SOURCE_CODE_DISTANCE, 110, sc, "NE"), "Event = ", "currentEvent",
				null);
		pMap.put("currentEvent", currentEvent);

		Text currentL = lang.newText(new Offset(70, 0, currentEvent, "NE"), "L = {" + L.get(0) + "}", "currentL", null);
		pMap.put("currentL", currentL);

		createTableHead();
		next();

		sc.highlight(0, 0, false);
		DoubleArray array;
		array = (DoubleArray) pMap.get("normArray");
		array.highlightElem(0, null, null);
		array.highlightCell(0, null, null);
		array = (DoubleArray) pMap.get("expArray");
		array.highlightElem(0, null, null);
		array.highlightCell(0, null, null);
		next("Start Algorithmus");
		// run method is triggered after this
	}

	// @formatter:off

	protected void run() { /** 0 */
		int counter = 0;
		createNewRow(++counter);
		insertLInTable(counter, L);
		insertEventInTable(counter, new Event(42, 'I'));
		next(1);
		boolean busy = false; /** 1 */
		insertStatusInTable(counter, busy);
		next(2);
		double t = 0; /** 2 */
		insertTimeInTable(counter, t);
		next(3);
		int N = 0; /** 3 */
		insertNInTable(counter, N);
		next(4, "Schleifendurchlauf " + counter);
		while (t < tmax) { /** 4 */
			createNewRow(++counter);
			next(5);
			Event event = L.get(0); /** 5 */
			askForNextEvent(event);
			actualizeCurrentEvent(event);
			next(6);
			L.remove(0); /** 6 */
			updateCurrentL();
			next(7);
			t = event.getTime(); /** 7 */
			askTimeInTable(t);
			insertTimeInTable(counter, t);
			next(8);
			askEventTypeInTable(event.getType());
			insertEventInTable(counter, event);
			switch (event.getType()) { /** 8 */
				case 'A': /** 9 */
					next(9);
					next(10);
					Event newEvent = new Event(t + expQueue.poll(), 'A');
					L.add(newEvent); /** 10 */
					askWhichEventIsAdded(newEvent);
					updateCurrentL();
					incrementArrayMarker("expMarker");
					toggleArrayHighlight("expArray", ++expCellPos);
					next(11);
					if (busy) { /** 11 */
						busyCounter++;
						askStatusInTable(busy);
						insertStatusInTable(counter, busy);
						next(12);
						N++; /** 12 */
						askNInTable(N);
						insertNInTable(counter, N);
					} else { /** 13 */
						idleCounter++;
						next(13);
						askNInTable(N);
						insertNInTable(counter, N);
						next(14);
						busy = true; /** 14 */
						askStatusInTable(busy);
						insertStatusInTable(counter, busy);
						next(15);
						Event addEvent = new Event(t + normQueue.poll(), 'D');
						L.add(addEvent); /** 15 */
						askWhichEventIsAdded(addEvent);
						updateCurrentL();
						incrementArrayMarker("normMarker");
						toggleArrayHighlight("normArray", ++normCellPos);
						next(16);
					} /** 16 */
					next(17);
					break; /** 17 */
				case 'D': /** 18 */
					busyCounter++; // all time busy here
					next(18);
					next(19);
					if (N == 0) { /** 19 */
						askNInTable(N);
						insertNInTable(counter, N);
						next(20);
						busy = false; /** 20 */
						askStatusInTable(busy);
						insertStatusInTable(counter, busy);
					} else { /** 21 */
						next(21);
						askStatusInTable(busy);
						insertStatusInTable(counter, busy);
						next(22);
						N--; /** 22 */
						askNInTable(N);
						insertNInTable(counter, N);
						next(23);
						Event addEvent = new Event(t + normQueue.poll(), 'D');
						L.add(addEvent); /** 23 */
						askWhichEventIsAdded(addEvent);
						updateCurrentL();
						incrementArrayMarker("normMarker");
						toggleArrayHighlight("normArray", ++normCellPos);
						next(24);
					} /** 24 */
					next(25);
					break; /** 25 */
			} /** 26 */
			next(26);
			next(27);
			Collections.sort(L); /** 27 */
			sortCounter++;
			updateCurrentL();
			insertLInTable(counter, L);
			next(4, "Schleifendurchlauf " + counter);
		} /** 28 */
		next(28);
		next(29);
	}

	/** 29 */

	// @formatter:on

	protected void end() {
		/* last AS */
		next();
		sc.unhighlight(29);
		next("Ende Algorithmus");
		for (Primitive p : pMap.values()) {
			p.hide();
		}
		sc.hide();
		next("Epilog");
		writeEpilog();
	}

	private void writeEpilog() {
		sc = lang.newSourceCode(new Coordinates(20, 26), "epilog", null);
		sc.addCodeLine("Der DEVS Algorithmus hat eine Komplexität von O(n), wobei", "lw", 0, null);
		sc.addCodeLine("n die Anzahl der Schleifendurchläufe ist die der Algorithmus", "lw", 0, null);
		sc.addCodeLine("vom Zeitpunkt 0 bis " + tmax + " benötigt.", "lw", 0, null);
		sc.addCodeLine("", "lw", 0, null);
		sc.addCodeLine("Insgesamt war bei dieser Ausführung des Algorithmus die", "lw", 0, null);
		sc.addCodeLine("Warteschlange genau...", "lw", 0, null);
		sc.addCodeLine(busyCounter + "-mal ausgelastet und", "lw", 1, null);
		sc.addCodeLine(idleCounter + "-mal nicht ausgelastet.", "lw", 1, null);
		sc.addCodeLine("", "lw", 0, null);
		sc.addCodeLine("Außerdem wurde die Liste L des Algorithmus genau " + sortCounter, "lw", 0, null);
		sc.addCodeLine("mal sortiert.", "lw", 0, null);
		sc.addCodeLine("", "lw", 0, null);
		sc.addCodeLine("Man beachte, dass der letzte Eintrag nicht mitgezählt wurde, da", "lw", 0, null);
		sc.addCodeLine("dieser auch tmax überschreitet.", "lw", 0, null);

		next("Hilfsklasse(n)");

		sc.hide();
		sc = lang.newSourceCode(new Coordinates(20, 26), "epilog", null);
		sc.addCodeLine("Für den DEVS Algorithmus wurde die Hilfsklasse Event verwendet.", "epilog", 0, null);
		sc.addCodeLine("Diese wird Ihnen hier einmal präsentiert.", "epilog", 0, null);
		sc.addCodeLine("", "epilog", 0, null);
		sc.addCodeLine("public class Event implements Comparable<Event> {", "epilog", 0, null);
		sc.addCodeLine("private double	time;", "epilog", 1, null);
		sc.addCodeLine("private char	type;", "epilog", 1, null);
		sc.addCodeLine("", "epilog", 1, null);
		sc.addCodeLine("public Event(double time, char type) {", "epilog", 1, null);
		sc.addCodeLine("this.time = time;", "epilog", 2, null);
		sc.addCodeLine("this.type = type;", "epilog", 2, null);
		sc.addCodeLine("}", "epilog", 1, null);
		sc.addCodeLine("", "epilog", 1, null);
		sc.addCodeLine("public double getTime() {", "epilog", 1, null);
		sc.addCodeLine("return this.time;", "epilog", 2, null);
		sc.addCodeLine("}", "epilog", 1, null);
		sc.addCodeLine("", "epilog", 1, null);
		sc.addCodeLine("public char getType() {", "epilog", 1, null);
		sc.addCodeLine("return this.type;", "epilog", 2, null);
		sc.addCodeLine("}", "epilog", 1, null);
		sc.addCodeLine("", "epilog", 1, null);
		sc.addCodeLine("@Override", "epilog", 1, null);
		sc.addCodeLine("public int compareTo(Event event) {", "epilog", 1, null);
		sc.addCodeLine("return (time < event.getTime()) ? -1 : (time > event.getTime()) ? 1 : 0;", "epilog", 2, null);
		sc.addCodeLine("}", "epilog", 1, null);
		sc.addCodeLine("}", "epilog", 0, null);

		next("Danksagung");
		sc.hide();
		sc = lang.newSourceCode(new Coordinates(20, 26), "thanks", null);
		sc.addCodeLine("Vielen Dank für die Nutzung des DEVS Algorithmus!", "thanks", 0, null);
	}

	private void next() {
		lang.nextStep();
	}

	private void next(String label) {
		lang.nextStep(label);
	}

	private void next(int nextLine) {
		lang.nextStep();
		sc.toggleHighlight(currentLine, nextLine);
		currentLine = nextLine;
	}

	private void next(int nextLine, String label) {
		lang.nextStep(label);
		sc.toggleHighlight(currentLine, nextLine);
		currentLine = nextLine;
	}

	private void parseDistributionNumbers(String inputString, Queue<Double> distribution) throws NumberFormatException {
		String[] doubleString = inputString.split(",");
		for (String doubleNumber : doubleString) {
			distribution.add(Double.parseDouble(doubleNumber));
		}
	}

	private void actualizeCurrentEvent(Event event) {
		Text currentEvent = (Text) pMap.get("currentEvent");
		currentEvent.setText("Event = " + event, null, null);
	}

	private void updateCurrentL() {
		String currentLText = "L = {";
		if (!L.isEmpty()) {
			currentLText += L.get(0);
			for (int i = 1; i < L.size(); i++) {
				currentLText += ", " + L.get(i);
			}
		}
		((Text) pMap.get("currentL")).setText(currentLText + "}", null, null);
	}

	private void createTableHead() {
		int xLength = 500;
		int innerPadding = 50;
		int hip = innerPadding / 2;
		Offset[] vertices = new Offset[] { new Offset(SOURCE_CODE_DISTANCE, 160, sc, "NE"),
				new Offset(SOURCE_CODE_DISTANCE + xLength, 160, sc, "NE") };
		pMap.put("horizontalLine", lang.newPolyline(vertices, "horizontalLine", null));
		pMap.put("timeLabel",
				lang.newText(new Offset(innerPadding, -15, "horizontalLine", "NW"), "t/s", "timeLabel", null));
		pMap.put("eventLabel", lang.newText(new Offset(innerPadding, 0, "timeLabel", "NE"), "E", "eventLabel", null));
		pMap.put("numberQueueLabel",
				lang.newText(new Offset(innerPadding, 0, "eventLabel", "NE"), "N", "numberQueueLabel", null));
		pMap.put("stateLabel",
				lang.newText(new Offset(innerPadding, 0, "numberQueueLabel", "NE"), "S", "stateLabel", null));
		pMap.put("eventListLabel",
				lang.newText(new Offset(innerPadding, 0, "stateLabel", "NE"), "L", "eventListLabel", null));
		pMap.put(
				"plT.0",
				lang.newPolyline(new Offset[] { new Offset(hip, 0, "timeLabel", "NE"),
						new Offset(hip, 17, "timeLabel", "NE") }, "plT.0", null));
		pMap.put(
				"plE.0",
				lang.newPolyline(new Offset[] { new Offset(hip, 0, "eventLabel", "NE"),
						new Offset(hip, 17, "eventLabel", "NE") }, "plE.0", null));
		pMap.put(
				"plN.0",
				lang.newPolyline(new Offset[] { new Offset(hip, 0, "numberQueueLabel", "NE"),
						new Offset(hip, 17, "numberQueueLabel", "NE") }, "plN.0", null));
		pMap.put(
				"plS.0",
				lang.newPolyline(new Offset[] { new Offset(hip, 0, "stateLabel", "NE"),
						new Offset(hip, 17, "stateLabel", "NE") }, "plS.0", null));
	}

	private void createNewRow(int counter) {
		Polyline plT = lang.newPolyline(new Offset[] { new Offset(0, 0, "plT." + (counter - 1), "S"),
				new Offset(0, cellHeight, "plT." + (counter - 1), "S") }, "plT." + counter, null);
		pMap.put("plT." + counter, plT);

		Polyline plE = lang.newPolyline(new Offset[] { new Offset(0, 0, "plE." + (counter - 1), "S"),
				new Offset(0, cellHeight, "plE." + (counter - 1), "S") }, "plE." + counter, null);
		pMap.put("plE." + counter, plE);

		Polyline plN = lang.newPolyline(new Offset[] { new Offset(0, 0, "plN." + (counter - 1), "S"),
				new Offset(0, cellHeight, "plN." + (counter - 1), "S") }, "plN." + counter, null);
		pMap.put("plN." + counter, plN);

		Polyline plS = lang.newPolyline(new Offset[] { new Offset(0, 0, "plS." + (counter - 1), "S"),
				new Offset(0, cellHeight, "plS." + (counter - 1), "S") }, "plS." + counter, null);
		pMap.put("plS." + counter, plS);
	}

	private void insertTimeInTable(int counter, double t) {
		Primitive plT = pMap.get("plT." + counter);
		Text tText = lang.newText(new Offset(-42, 0, plT, "NW"), String.format(Locale.CANADA, "%.2f", t), "tText"
				+ counter, null);
		pMap.put("tText" + counter, tText);
	}

	private void insertEventInTable(int counter, Event event) {
		Primitive plE = pMap.get("plE." + counter);
		Text eText = lang.newText(new Offset(-33, 0, plE, "NW"), "" + event.getType(), "eText" + counter, null);
		pMap.put("eText" + counter, eText);
	}

	private void insertNInTable(int counter, int n) {
		Primitive plN = pMap.get("plN." + counter);
		Text nText = lang.newText(new Offset(-33, 0, plN, "NW"), "" + n, "nText" + counter, null);
		pMap.put("nText" + counter, nText);
	}

	private void insertStatusInTable(int counter, boolean status) {
		String busy = status ? "busy" : "idle";
		Primitive plS = pMap.get("plS." + counter);
		Text sText = lang.newText(new Offset(-42, 0, plS, "NW"), busy, "sText" + counter, null);
		pMap.put("sText" + counter, sText);
	}

	private void insertLInTable(int counter, List<Event> L) {
		Primitive plS = pMap.get("plS." + counter);
		String events = "{";
		boolean first = true;
		for (Event ev : L) {
			if (!first)
				events += ", ";
			events += ev.toString();
			first = false;
		}
		events += "}";
		Text lText = lang.newText(new Offset(10, 0, plS, "NW"), events, "lText" + counter, null);
		pMap.put("lText" + counter, lText);
	}

	private void askTimeInTable(double t) {
		if (rnd.nextInt(100) < possibilityQuestion) {
			MultipleChoiceQuestionModel mc = new MultipleChoiceQuestionModel("Time in Tabel " + (questionCounter++));
			mc.setPrompt("Wie lautet die Zeit, die als nächstes in die Tabelle eingetragen wird?");
			mc.addAnswer("1", String.valueOf(t).replace(".", ","), 1, "Richtig!");
			mc.addAnswer("2", String.valueOf(-t).replace(".", ","), -1, "Falsch.");
			mc.addAnswer("3", String.valueOf(t + 0.1).replace(".", ","), -1, "Falsch.");
			mc.addAnswer("4", String.valueOf(t - 0.1).replace(".", ","), -1, "Falsch.");
			lang.addMCQuestion(mc);
			next();
		}
	}

	private void askEventTypeInTable(char type) {
		if (rnd.nextInt(100) < possibilityQuestion) {
			String actualType = (type == 'A') ? "(A)rrival-Event" : "(D)eparture-Event";
			String notType = (type == 'A') ? "(D)eparture-Event" : "(A)rrival-Event";
			boolean correctType = (rnd.nextInt(100) < 50);
			String qType = correctType ? actualType : notType;
			TrueFalseQuestionModel tfm = new TrueFalseQuestionModel("TypeOfEvent" + (questionCounter++), correctType, 1);
			tfm.setPrompt("Ist das gegenwärtige Event ein " + qType + "?");
			lang.addTFQuestion(tfm);
			next();
		}
	}

	private void askNInTable(int n) {
		if (rnd.nextInt(100) < possibilityQuestion) {
			MultipleChoiceQuestionModel mc = new MultipleChoiceQuestionModel("N in Table" + (questionCounter++));
			mc.setPrompt("Wie lautet die Warteschlangenlänge, die als nächstes in die Tabelle eingetragen wird?");
			mc.addAnswer("1", String.valueOf(n), 1, "Richtig!");
			mc.addAnswer("2", String.valueOf(-n), -1, "Falsch.");
			mc.addAnswer("3", String.valueOf(n - 1), -1, "Falsch.");
			mc.addAnswer("4", String.valueOf(n + 1), -1, "Falsch.");
			lang.addMCQuestion(mc);
			next();
		}
	}

	private void askStatusInTable(boolean status) {
		if (rnd.nextInt(100) < possibilityQuestion) {
			boolean correctStatus = (rnd.nextInt(100) < 50);
			String actualStatus = (status) ? "busy" : "idle";
			String notStatus = status ? "idle" : "busy";
			String qStatus = correctStatus ? actualStatus : notStatus;
			TrueFalseQuestionModel tfm = new TrueFalseQuestionModel("CurrentStatus" + (questionCounter++),
					correctStatus, 1);
			tfm.setPrompt("Ist der Status " + qStatus + "?");
			lang.addTFQuestion(tfm);
			next();
		}
	}

	private void askForNextEvent(Event event) {
		if (rnd.nextInt(100) < possibilityQuestion) {
			char notType = event.getType() == 'A' ? 'D' : 'A';
			MultipleChoiceQuestionModel mc = new MultipleChoiceQuestionModel("Next Event" + (questionCounter++));
			mc.setPrompt("Welches Event tritt als nächstes ein?");
			mc.addAnswer("1", event.toString().replace(".", ","), 1, "Richtig!");

			Event fev1 = new Event(event.getTime(), notType);
			mc.addAnswer("2", fev1.toString().replace(".", ","), -1, "Falsch.");

			Event fev2 = new Event(event.getTime(), 'I');
			mc.addAnswer("3", fev2.toString().replace(".", ","), -1, "Falsch.");

			if (L.size() > 1) {
				Event fev3 = L.get(1);
				mc.addAnswer("4", fev3.toString().replace(".", ","), -1, "Falsch.");
			}
			lang.addMCQuestion(mc);
			next();
		}
	}

	private void askWhichEventIsAdded(Event event) {
		if (rnd.nextInt(100) < possibilityQuestion) {
			char notType = event.getType() == 'A' ? 'D' : 'A';
			MultipleChoiceQuestionModel mc = new MultipleChoiceQuestionModel("EventAdd" + (questionCounter++));
			mc.setPrompt("Welches Event wird hinzugefügt?");
			mc.addAnswer("1", event.toString(), 1, "Richtig!");

			Event fev1 = new Event(event.getTime(), notType);
			mc.addAnswer("2", fev1.toString(), -1, "Falsch.");

			Event fev2 = new Event(event.getTime() - 1, event.getType());
			mc.addAnswer("3", fev2.toString(), -1, "Falsch.");

			Event fev3 = new Event(event.getTime() - 1, notType);
			mc.addAnswer("4", fev3.toString(), -1, "Falsch.");
			lang.addMCQuestion(mc);
			next();
		}
	}

	private void incrementArrayMarker(String id) {
		((ArrayMarker) pMap.get(id)).increment(null, null);
	}

	private void toggleArrayHighlight(String arrayId, int pos) {
		DoubleArray array = (DoubleArray) pMap.get(arrayId);
		array.highlightElem(pos, null, null);
		array.highlightCell(pos, null, null);
		array.unhighlightElem(0, pos - 1, null, null);
		array.unhighlightCell(0, pos - 1, null, null);
	}
}