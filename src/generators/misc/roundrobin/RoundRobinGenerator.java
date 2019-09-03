package generators.misc.roundrobin;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.AnimationType;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.ArrayDisplayOptions;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;
import translator.ResourceLocator;

public class RoundRobinGenerator {
	private int k;

	private int helpK;

	private int exampleID;

	private LinkedList<Process> processes = new LinkedList<Process>();

	private LinkedList<Process> presentProcesses = new LinkedList<Process>();

	/* ANIMALSCRIPT OBJECTS */
	private Language lang;

	private SourceCode code;

	private Text title;

	private ArrayProperties processQueueProperties;

	private ArrayDisplayOptions processArrayOptions;

	private Timing timing = new TicksTiming(200);

	private LinkedList<String> codeText = new LinkedList<String>();

	private LinkedList<Text> arrayText = new LinkedList<Text>();

	private Rect codeRect;

	private ArrayMarker pointer;

	private ArrayMarkerProperties pointerProperties;

	/* HILFSOBJEKTE */
	private LinkedList<Process> processesN = new LinkedList<Process>();

	private LinkedList<Process> allProcesses = new LinkedList<Process>();

	private LinkedList<Process> presentProcessesN = new LinkedList<Process>();

	private LinkedList<StringArray> processList = new LinkedList<StringArray>();

	private boolean infinityMode;

	private boolean runProcess = true;

	// Weitere Einstellungen:

	private String[][] processArray = new String[][] { { "Word", "0", "3" }, { "Excel", "5", "5" }, { "GIMP", "1", "12" },
			{ "PowerPoint", "8", "7" } };

	private int processArrayLength = 40;

	private int colorEffectDuration = 50;

	private Color processColor1 = Color.PINK;

	private Color processColor2 = Color.GREEN;

	private Color processColor3 = Color.RED;

	private Color processColor4 = Color.CYAN;

	private Color processColor5 = Color.MAGENTA;

	private Color processColor6 = Color.ORANGE;

	private Color sCColor = Color.BLACK;

	private Color sCHighlightColor = new Color(230, 138, 0);

	private int sCFontSize = 10;

	private boolean sCBold = false;

	private boolean sCItalic = false;

	private String sCFont = "Monospaced";

	private CodeText cT;

	private double probGeneral;

	private double probSpecific;



	public static void main(String[] args) throws Exception {

		RoundRobinGenerator roundrobin = new RoundRobinGenerator(3, 3, false, Lang.GERMAN, CodeText.JAVA);
		roundrobin.execute(false);
	}


	public RoundRobinGenerator(int k, int exampleID, boolean infinityMode, Lang language, CodeText codeText) throws Exception {
		this.k = k;
		this.exampleID = exampleID;
		MessageOrganizer.setupTexts(language, infinityMode, exampleID);
		setupExample();
		setupCode(codeText, language);
		this.infinityMode = infinityMode;
		this.cT = codeText;
	}


	public RoundRobinGenerator(Language lang, boolean language, boolean codeLanguage, int k, int exampleNumber, boolean infinityMode,
			String[][] processes, int processArrayLength, int colorEffectDuration, Color processColor1, Color processColor2, Color processColor3,
			Color processColor4, Color processColor5, Color processColor6, Color sCColor, Color sCHighlightColor, int sCFontSize, boolean sCBold,
			boolean sCItalic, String sCFont, int probGeneral, int probSpecific) throws Exception {

		this.lang = lang;

		Lang l = null;
		if (language) {
			l = Lang.ENGLISH;
		} else {
			l = Lang.GERMAN;
		}

		MessageOrganizer.setupTexts(l, infinityMode, exampleNumber);

		CodeText codeText = null;
		if (codeLanguage) {
			codeText = CodeText.PSEUDOCODE;
		} else {
			codeText = CodeText.JAVA;
		}
		this.cT = codeText;
		setupCode(codeText, l);

		this.k = k;

		this.processArray = processes;

		this.processColor1 = processColor1;
		this.processColor2 = processColor2;
		this.processColor3 = processColor3;
		this.processColor4 = processColor4;
		this.processColor5 = processColor5;
		this.processColor6 = processColor6;

		this.exampleID = exampleNumber;
		setupExample();

		this.infinityMode = infinityMode;

		this.processArrayLength = processArrayLength;
		this.colorEffectDuration = colorEffectDuration;

		this.sCColor = sCColor;
		this.sCHighlightColor = sCHighlightColor;
		this.sCFontSize = sCFontSize;
		this.sCBold = sCBold;
		this.sCItalic = sCItalic;
		this.sCFont = sCFont;

		this.probGeneral = probGeneral / ((double) 100);
		this.probSpecific = probSpecific / ((double) 100);

	}


	private void setUpProcess(int id, int arrivalTime, int remainingTime, String name, Color color) {
		processes.add(new Process(id, arrivalTime, remainingTime, name, color));
		processesN.add(new Process(id, arrivalTime, remainingTime, name, color));
		allProcesses.add(new Process(id, arrivalTime, remainingTime, name, color));
	}


	/**
	 * 
	 */
	private void setupExample() {
		switch (exampleID) {
		case 1: /* EXAMPLE: Trivial */
			setUpProcess(0, 0, 10, MessageOrganizer.get("Bsp2-P1"), processColor1);
			setUpProcess(1, 1, 5, MessageOrganizer.get("Bsp2-P2"), processColor2);
			setUpProcess(2, 2, 3, MessageOrganizer.get("Bsp2-P3"), processColor3);
			setUpProcess(3, 25, 2, MessageOrganizer.get("Bsp2-P4"), processColor4);
			break;

		case 2: /* EXAMPLE: Präsentation */
			setUpProcess(0, 0, 3, MessageOrganizer.get("Bsp1-P1"), processColor1);
			setUpProcess(1, 5, 5, MessageOrganizer.get("Bsp1-P2"), processColor2);
			setUpProcess(2, 1, 12, MessageOrganizer.get("Bsp1-P3"), processColor3);
			setUpProcess(3, 8, 7, MessageOrganizer.get("Bsp1-P4"), processColor4);
			break;

		case 3: /* EXAMPLE: Raketenstart */
			setUpProcess(0, 0, 9, MessageOrganizer.get("Bsp3-P1"), processColor1);
			setUpProcess(1, 1, 15, MessageOrganizer.get("Bsp3-P2"), processColor2);
			setUpProcess(2, 0, 5, MessageOrganizer.get("Bsp3-P3"), processColor3);
			setUpProcess(3, 20, 11, MessageOrganizer.get("Bsp3-P4"), processColor4);
			setUpProcess(4, 32, 4, MessageOrganizer.get("Bsp3-P5"), processColor5);
			setUpProcess(5, 29, 10, MessageOrganizer.get("Bsp3-P6"), processColor6);
			break;

		case 4: /* CUSTOM EXAMPLE */

			List<Color> processColors = Arrays.asList(processColor1, processColor2, processColor3, processColor4, processColor5, processColor6);

			for (int i = 0; i < processArray.length; i++) {
				setUpProcess(i, Integer.parseInt(processArray[i][1]), Integer.parseInt(processArray[i][2]), processArray[i][0], processColors.get(i));
			}
			break;
		}
	}


	private void setupCode(CodeText text, Lang language) throws Exception {

		BufferedReader reader = null;

		if (text == CodeText.PSEUDOCODE && language == Lang.ENGLISH) {
      reader = ResourceLocator.getResourceLocator().getBufferedReader("resources/roundrobin/pseudocode_en");
//			reader = new BufferedReader(new FileReader("resources/roundrobin/pseudocode_en"));
		} else if (text == CodeText.PSEUDOCODE && language == Lang.GERMAN) {
      reader = ResourceLocator.getResourceLocator().getBufferedReader("resources/roundrobin/pseudocode_de");
//			reader = new BufferedReader(new FileReader("resources/roundrobin/pseudocode_de"));
		} else if (text == CodeText.JAVA && language == Lang.ENGLISH) {
      reader = ResourceLocator.getResourceLocator().getBufferedReader("resources/roundrobin/javacode_en");
//			reader = new BufferedReader(new FileReader("resources/roundrobin/javacode_en"));
		} else {
      reader = ResourceLocator.getResourceLocator().getBufferedReader("resources/roundrobin/javacode_de");
//			reader = new BufferedReader(new FileReader("resources/roundrobin/javacode_de"));
		}

		reader.lines().forEach((String line) -> {
			codeText.add(line);
		});

	}


	public void execute(boolean animalVersion) throws IOException {

		// Language setup
		if (!animalVersion) {
			lang = Language.getLanguageInstance(AnimationType.ANIMALSCRIPT, "RoundRobin", "Anja Kirchhöfer, Ben Kohr", 640, 480);
		}
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
		lang.setStepMode(true);

		Questions.init(lang, probGeneral, probSpecific); // 0.3, 0.6

		roundRobin();

		// Animalscript setup
		animalScriptSetup();

		title.hide();
		code.hide();
		for (StringArray array : processList) {
			array.hide();
		}
		for (Text t : arrayText) {
			t.hide();
		}
		pointer.hide();

		codeRect.hide();

		lang.nextStep();

		title.setText(MessageOrganizer.get("title"), null, timing);
		title.show();

		lang.nextStep();

		code.show();
		codeRect.show();

		// lang.nextStep();

		roundRobinAnimal();

		MessageOrganizer.showInfoBox(lang);

		if (infinityMode) {
			MessageOrganizer.showEndText(lang, true, true);
		} else if (processes.isEmpty() || (exampleID == 3 && processIsReady(1))) {
			MessageOrganizer.showEndText(lang, false, true);
		} else {
			MessageOrganizer.showEndText(lang, false, false);
		}

		MessageOrganizer.hideInfoBox(lang);

		lang.finalizeGeneration();

		if (!animalVersion) {
			String langString = lang.toString();

			// print Animalscript code
			System.out.println(langString);


			BufferedWriter writer = new BufferedWriter(new FileWriter(System.getProperty("user.home") + "/Desktop/animalscriptfile.txt"));
			writer.write(lang + "");
			writer.close();
		}

	}


	private boolean processIsReady(int i) {
		for (Process p : processes) {
			if (p.getID() == i) {
				return false;
			}
		}
		return true;
	}


	private void animalScriptSetup() {
		// title
		TextProperties titleProperties = new TextProperties();
		titleProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 20));
		title = lang.newText(new Coordinates(20, 5), "", "title", null, titleProperties);

		// code
		SourceCodeProperties codeProperties = new SourceCodeProperties();
		codeProperties.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, sCHighlightColor);
		codeProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, sCColor);

		codeProperties.set(AnimationPropertiesKeys.FONT_PROPERTY,
				new Font(sCFont, ((sCBold) ? Font.BOLD : 0) + ((sCItalic) ? Font.ITALIC : 0) + Font.PLAIN, sCFontSize));

		code = lang.newSourceCode(new Coordinates(20, 215), "code", null, codeProperties);

		code.addCodeLine(codeText.get(0), null, 0, null);
		code.addCodeLine(codeText.get(1), null, 0, null);
		code.addCodeLine(codeText.get(2), null, 2, null);
		code.addCodeLine(codeText.get(3), null, 4, null);
		code.addCodeLine(codeText.get(4), null, 6, null);
		code.addCodeLine(codeText.get(5), null, 4, null);
		code.addCodeLine(codeText.get(6), null, 6, null);
		code.addCodeLine(codeText.get(7), null, 6, null);
		code.addCodeLine(codeText.get(8), null, 8, null);
		code.addCodeLine(codeText.get(9), null, 8, null);
		code.addCodeLine(codeText.get(10), null, 6, null);
		code.addCodeLine(codeText.get(11), null, 6, null);
		code.addCodeLine(codeText.get(12), null, 8, null);
		code.addCodeLine(codeText.get(13), null, 6, null);
		code.addCodeLine(codeText.get(14), null, 8, null);
		if (cT == CodeText.JAVA) {
			code.addCodeLine(codeText.get(15), null, 6, null);
			code.addCodeLine(codeText.get(16), null, 4, null);
		}
		code.addCodeLine(codeText.get(17), null, 2, null);
		code.addCodeLine(codeText.get(18), null, 0, null);
		// code.addCodeLine(codeText.get(19), null, 0, null);

		RectProperties codeRectProperties = new RectProperties();
		codeRectProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		codeRect = lang.newRect(new Offset(-8, -5, code, AnimalScript.DIRECTION_NW), new Offset(8, 5, code, AnimalScript.DIRECTION_SE), "coderect",
				null, codeRectProperties);

		processQueueProperties = new ArrayProperties();
		processQueueProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		processQueueProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);

		processArrayOptions = new ArrayDisplayOptions(null, null, false);
		String[] array = new String[processArrayLength];
		for (int x = 0; x < array.length; x++) {
			array[x] = "   ";
		}

		StringArray stringArray;
		int i = 0;

		int yOffset = (int) (177 - 13 * (6 - processes.size()));


		for (Process p : processes) {
			processQueueProperties.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, p.getColor());
			processQueueProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 10));
			stringArray = lang.newStringArray(new Offset(100, -yOffset + i * 27, code, null), array, "process" + p.getID(), processArrayOptions,
					processQueueProperties);
			stringArray.showIndices(false, null, null);
			TextProperties processNameOptions = new TextProperties();
			processNameOptions.set(AnimationPropertiesKeys.COLOR_PROPERTY, p.getColor());
			processNameOptions.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 18));

			int xOffset = 94 - p.getName().length() * 11;

			arrayText.add(lang.newText(new Offset(xOffset, -yOffset + i * 27 + 1, code, null), p.getName(), p.getName() + "_text", null,
					processNameOptions));
			if (p.getArrivalTime() < stringArray.getLength()) {
				stringArray.put(p.getArrivalTime(), " A ", null, null);
				stringArray.setHighlightFillColor(p.getArrivalTime(), Color.LIGHT_GRAY, null, null);
				stringArray.highlightCell(p.getArrivalTime(), null, null);
			}
			processList.add(stringArray);

			i++;
		}

		pointerProperties = new ArrayMarkerProperties();
		pointerProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		pointerProperties.set(AnimationPropertiesKeys.SHORT_MARKER_PROPERTY, true);
		// pointerProperties.set(AnimationPropertiesKeys.HIDDEN_PROPERTY,
		// false);
		pointer = lang.newArrayMarker(processList.getFirst(), 0, "arrayPointer", processArrayOptions, pointerProperties);
	}


	public void roundRobin() {
		int time = 0;
		getPresentProcessesN(time, false);

		while (!processesN.isEmpty()) {

			if (!presentProcessesN.isEmpty()) {
				// aktueller Prozess arbeitet
				for (int helpK = k; helpK > 0; helpK--) {

					presentProcessesN.getFirst().setRemainingTime(presentProcessesN.getFirst().getRemainingTime() - 1);

					// Entferne Prozess, wenn fertig
					if (presentProcessesN.getFirst().getRemainingTime() == 0) {

						processesN = removeElemFromList(presentProcessesN.getFirst().getID(), processesN);
						presentProcessesN = removeElemFromList(presentProcessesN.getFirst().getID(), presentProcessesN);
						// Nächster Zeitschritt
						time++;
						getPresentProcessesN(time, false);
						break;
					}


					// Nächster Zeitschritt
					time++;
					if (helpK > 1)
						getPresentProcessesN(time, false);
					else
						getPresentProcessesN(time, true);
				}
			} else {
				time++;
				getPresentProcessesN(time, false);
			}
		}
	}


	public void roundRobinAnimal() {

		Drafter.setup(lang, processes, k, colorEffectDuration);
		for (StringArray array : processList) {
			array.show();
		}
		for (Text t : arrayText) {
			t.show();
		}

		// lang.nextStep();

		Drafter.initProcessor(lang);

		Drafter.createLegend(lang);

		lang.nextStep();

		MessageOrganizer.initInfoBox(lang);
		MessageOrganizer.showStartText(lang, infinityMode);
		MessageOrganizer.hideInfoBox(lang);
		MessageOrganizer.initBox(lang);

		lang.nextStep("Processor starts working");


		// Initialer Zustand vor Begin
		int time = 0;

		getPresentProcesses(time, false, 0);
		Drafter.updateProcessorRemainingTime(lang, 0, true);

		lang.nextStep();

		pointer.show();
		code.highlight(2);
		lang.nextStep();

		boolean rotate = true;

		while (!processes.isEmpty() && runProcess) {
			rotate = true;

			List<String> stillToDo = new LinkedList<String>();
			for (Process p : processes) {
				stillToDo.add(p.getName());
			}
			Questions.getSpecificQuestion(lang, "stilltodo", stillToDo);

			code.unhighlight(2);
			code.highlight(3);
			lang.nextStep();


			List<String> answerP = new LinkedList<String>();
			for (Process p : allProcesses) {
				if (p.getArrivalTime() > time) {
					answerP.add(p.getName());
				}

			}
			Questions.getSpecificQuestion(lang, "wait", presentProcesses.isEmpty(), answerP.size() > 1);


			if (!presentProcesses.isEmpty()) {

				code.unhighlight(3);
				code.highlight(5);
				lang.nextStep();


				List<String> allProcessNames = new LinkedList<String>();
				for (Process p : allProcesses) {
					allProcessNames.add(p.getName());
				}

				List<String> answerProcesses = new LinkedList<String>();
				for (Process p : allProcesses) {
					String pName = p.getName();
					int remainingTime = p.getRemainingTime();
					for (Process pp : presentProcesses) {
						if (pp.getName().equals(pName) && pp.getRemainingTime() < remainingTime) {
							answerProcesses.add(pName);
						}
					}
				}

				Questions.getSpecificQuestion(lang, "notcompletely", allProcessNames, answerProcesses);


				code.unhighlight(5);
				code.highlight(6);
				lang.nextStep();
				Drafter.updateProcessorRemainingTime(lang, k, true);
				int pTime = presentProcesses.getFirst().getRemainingTime();
				lang.nextStep("The process " + presentProcesses.getFirst().getName() + " is next to be executed (with " + pTime
						+ ((pTime != 1) ? " time units" : " time unit") + " left).");

				Questions.pickRandomQuestion(lang);

				code.unhighlight(6);
				code.highlight(7);
				lang.nextStep();

				boolean toEnd = presentProcesses.getFirst().getRemainingTime() <= k;
				Questions.getSpecificQuestion(lang, "toEnd", toEnd);

				// aktueller Prozess arbeitet
				for (helpK = k; helpK > 0 && runProcess; helpK--) {

					updateQueue(presentProcesses.getFirst(), time, false, helpK);

					code.unhighlight(7);
					code.highlight(8);
					code.highlight(9);
					MessageOrganizer.showText(lang, "mWork");
					lang.nextStep();

					presentProcesses.getFirst().setRemainingTime(presentProcesses.getFirst().getRemainingTime() - 1);

					int i = helpK - 1;
					updateQueue(presentProcesses.getFirst(), time, true, i);
					updateProcessArrays(presentProcesses.getFirst(), time);

					lang.nextStep();

					Questions.pickRandomQuestion(lang);

					code.unhighlight(8);
					code.unhighlight(9);
					MessageOrganizer.hideTexts();
					code.highlight(10);
					lang.nextStep();

					time++;
					getPresentProcesses(time, false, i);

					if (processActiveInThisStep(time)) {
						lang.nextStep();
						MessageOrganizer.hideTexts();
					}

					code.unhighlight(10);
					code.highlight(7);
					lang.nextStep();


					// Entferne Prozess, wenn fertig
					if (presentProcesses.getFirst().getRemainingTime() == 0) {
						rotate = false;

						code.unhighlight(7);
						code.highlight(11);
						lang.nextStep();

						Questions.getSpecificQuestion(lang, "rotate", false);

						code.unhighlight(11);
						code.highlight(12);
						MessageOrganizer.showText(lang, "mRemove");
						
						Drafter.setFinishedMode(true);
						lang.nextStep("Process " + presentProcesses.getFirst().getName() + " is finished and gets removed from the queue.");

						allProcesses.get(presentProcesses.getFirst().getID()).setRemainingTime(0);
						processes = removeElemFromList(presentProcesses.getFirst().getID(), processes);
						presentProcesses = removeElemFromList(presentProcesses.getFirst().getID(), presentProcesses);

						
						Drafter.setNoProcessMode(true);
						Drafter.setFinishedMode(false);
						Drafter.setNoProcessMode(false);
						updateQueue(null, time, false, i);
						lang.nextStep();

						code.unhighlight(12);
						MessageOrganizer.hideTexts();
						code.highlight(2);
						lang.nextStep();

						// Nächster Zeitschritt

						break;
					}

					if (helpK == 1 && rotate) {


						List<String> alreadyDone = new LinkedList<String>();
						for (Process p : allProcesses) {
							alreadyDone.add(p.getName());

						}
						List<String> sTD = new LinkedList<String>();
						for (Process p : processes) {
							sTD.add(p.getName());
						}

						alreadyDone.removeAll(sTD);

						Questions.getSpecificQuestion(lang, "alreadydone", alreadyDone);


						getPresentProcesses(time, true, i);
						code.highlight(2);
						lang.nextStep();

						Questions.pickRandomQuestion(lang);

						MessageOrganizer.hideTexts();
					}
					rotate = true;

					// Nächster Zeitschritt
				}
			} else {

				code.unhighlight(3);
				code.highlight(4);

				MessageOrganizer.showText(lang, "mWait");
				lang.nextStep("The processor waits for one time unit, as there is no process to execute in the queue.");

				Questions.pickRandomQuestion(lang);

				Drafter.setWaitingMode(true);

				lang.nextStep();

				updateProcessArrays(null, time);
				time++;

				lang.nextStep();

				Drafter.setWaitingMode(false);

				if (processActiveInThisStep(time)) {
					lang.nextStep();
				}

				getPresentProcesses(time, false, Math.max(0, helpK - 1));
				lang.nextStep();
				code.unhighlight(4);
				MessageOrganizer.hideTexts();
				code.highlight(2);
				lang.nextStep();

			}
		}
	}


	private void updateQueue(Process actualProcess, int actualTime, boolean working, int kToDisplay) {

		int length = presentProcesses.size();

		ProcessorType type;
		if (working && !presentProcesses.isEmpty()) {
			type = ProcessorType.WORKING;
		} else if (!presentProcesses.isEmpty()) {
			type = ProcessorType.ACTIVE;
		} else {
			type = ProcessorType.INACTIVE;
		}

		Drafter.animateProcessor(lang, type);
		Drafter.flush();

		for (int i = 0; i < length; i++) {
			Process p = presentProcesses.get(i);

			Drafter.createProcess(lang, i, p);
		}

		Drafter.updateProcessorRemainingTime(lang, kToDisplay, true);

	}


	private void updateProcessArrays(Process actualProcess, int actualTime) {
		if (processList.getFirst().getLength() > actualTime) {
			pointer.increment(null, null);
			setNewTimeStepAtArray(actualProcess, actualTime, actualTime);
			if (!infinityMode && processList.getFirst().getLength() - 1 == actualTime)
				runProcess = false;

		} else if (infinityMode) {
			StringArray tempArray;
			for (int i = 0; i < processList.size(); i++) {
				tempArray = processList.get(i);
				for (int t = 0; t < tempArray.getLength() - 1; t++) {
					if (tempArray.getData(t + 1).equals(" - ")) {
						tempArray.unhighlightCell(t, null, null);
						tempArray.put(t, " - ", null, null);

					} else if (tempArray.getData(t + 1).equals("   ")) {
						tempArray.setHighlightFillColor(t, allProcesses.get(i).getColor(), null, null);
						tempArray.highlightCell(t, null, null);
						tempArray.put(t, "   ", null, null);

					} else if (processList.get(i).getData(t + 1).equals(" w ")) {
						tempArray.unhighlightCell(t, null, null);
						tempArray.put(t, " w ", null, null);

					} else {
						/* default */ }
				}
				tempArray.unhighlightCell(processList.getFirst().getLength() - 1, null, null);
				tempArray.put(processList.getFirst().getLength() - 1, "   ", null, null);
			}
			setNewTimeStepAtArray(actualProcess, actualTime, processList.getFirst().getLength() - 1);
		}
	}


	public LinkedList<Process> removeElemFromList(int id, LinkedList<Process> list) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getID() == id) {
				list.remove(i);
				break;
			}
		}
		return list;
	}


	private void getPresentProcesses(int actualTime, boolean rotate, int kToDisplay) {

		if (!(presentProcesses.isEmpty()) && rotate) {
			// code.unhighlight(11);
			// code.highlight(8);
			// lang.nextStep();

			code.unhighlight(7);
			code.highlight(11);
			lang.nextStep();
			Questions.getSpecificQuestion(lang, "rotate", true);

			code.unhighlight(11);
			code.highlight(13);
			lang.nextStep("The queue is rotated, as slide has been decremented to zero.");


			code.unhighlight(13);
			code.highlight(14);
			MessageOrganizer.showText(lang, "mRotate");
			Drafter.setRotateMode(true);
			updateQueue(presentProcesses.getFirst(), actualTime, false, kToDisplay);
			lang.nextStep();

			rotateProcesses();
			updateQueue(presentProcesses.getFirst(), actualTime, false, kToDisplay);

			lang.nextStep();

			Drafter.setRotateMode(false);
			code.unhighlight(14);
			MessageOrganizer.hideTexts();
		}


		for (Process p : processes) {
			if (actualTime == p.getArrivalTime() && !presentProcesses.contains(p)) {
				presentProcesses.addLast(p);
				if (actualTime != 0) {
					MessageOrganizer.showText(lang, "mNewProcess");
				}
			}
		}

		if (!presentProcesses.isEmpty()) {
			updateQueue(presentProcesses.getFirst(), actualTime, false, kToDisplay);
		} else {
			updateQueue(null, actualTime, false, kToDisplay);
		}
	}


	private void rotateProcesses() {

		Process first = presentProcesses.getFirst();
		int i;
		for (i = 0; i < presentProcesses.size() - 1; i++) {
			presentProcesses.set(i, presentProcesses.get(i + 1));
		}
		presentProcesses.set(i, first);
	}


	public static void printList(LinkedList<Process> list) {

		for (int x = 0; x < list.size(); x++) {
			System.out.print(list.get(x).getRemainingTime() + " ");
		}
		System.out.println(" ");
	}


	private void setNewTimeStepAtArray(Process actualProcess, int actualTime, int arrayPosition) {
		for (int i = 0; i < processList.size(); i++) {
			if (actualProcess != null && actualProcess.getID() == i) {
				processList.get(i).setHighlightFillColor(arrayPosition, actualProcess.getColor(), null, null);
				processList.get(i).highlightCell(arrayPosition, null, null);
				processList.get(i).put(arrayPosition, "   ", null, null);
			} else if (allProcesses.get(i).getArrivalTime() > actualTime || allProcesses.get(i).getRemainingTime() == 0) {
				processList.get(i).unhighlightCell(arrayPosition, null, null);
				processList.get(i).put(arrayPosition, " - ", null, null);
			} else {
				processList.get(i).unhighlightCell(arrayPosition, null, null);
				processList.get(i).put(arrayPosition, " w ", null, null);
			}
		}
	}


	public void setK(int newK) {
		k = newK;
	}


	private void getPresentProcessesN(int actualTime, boolean rotate) {

		if (!(presentProcessesN.isEmpty()) && rotate) {
			rotateProcessesN();
		}

		for (Process p : processesN) {
			if (actualTime == p.getArrivalTime()) {
				presentProcessesN.addLast(p);
			}
		}

	}


	private void rotateProcessesN() {

		Process first = presentProcessesN.getFirst();
		int i;
		for (i = 0; i < presentProcessesN.size() - 1; i++) {
			presentProcessesN.set(i, presentProcessesN.get(i + 1));
		}
		presentProcessesN.set(i, first);
	}


	private boolean processActiveInThisStep(int time) {

		for (Process p : allProcesses) {
			if (p.getArrivalTime() == time) {
				return true;
			}
		}

		return false;
	}


}
