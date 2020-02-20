/*
 * C-Scan.java
 * Steffen Lott, Ozan Agtas, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.hardware;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;

import algoanim.primitives.IntArray;
import algoanim.primitives.Point;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.Triangle;
import algoanim.primitives.generators.Language;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.TrueFalseQuestionModel;
import translator.Translator;
import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.TwoValueView;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CounterProperties;
import algoanim.properties.PointProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class CScan implements ValidatingGenerator {

	// AnimalScript Objekte
	private Translator transe;
	private Language lang;
	private SourceCode srcCode;

	// Variablen
	private int currentHeadPosition;
	private boolean goRight;
	private int segmentCount;
	private int[] queue;

	// GUI
	private IntArray queueDisplay;
	private IntArray resultDisplay;
	private TwoValueCounter counterQueue;
	private ArrayList<Integer> queList;
	private ArrayList<Integer> resList;

	// Properties
	private ArrayProperties queueProperties;
	private SourceCodeProperties sourcecodeProperties;
	private TextProperties textProperties;
	private TextProperties headerProperties;
	private RectProperties rectProperties;

	/*
	 * Strings zum Uebersetzten:
	 */
	private String str_queueHeader;
	private String str_modifyingAccess;
	private String str_readAccess;
	private String str_resultHeader;

	private String str_mcqm1Question;
	private String str_correctText;
	private String str_incorrectText;
	private String str_currentHeadStatus;
	private String str_qmRQuestion;
	private String str_qmLQuestion;
	private String str_mcqm2Question;
	private String str_mcqm2Correct;
	private String str_mcqm2Incorrect;

	private String str_algoDescrHeader;
	private String str_algoDescr1;
	private String str_algoDescr2;
	private String str_algoDescr3;
	private String str_algoDescr4;

	private String str_algoMeta1;
	private String str_algoMeta2;

	private String str_summary;
	private String str_algoSummary1;
	private String str_algoSummary2;
	private String str_algoSummary3;
	private String str_algoSummary4;

	private void refreshStrings() {
		this.str_currentHeadStatus = this.transe.translateMessage("headpos", new Object[] { currentHeadPosition });
		this.str_qmRQuestion = this.transe.translateMessage("nextheadposCurrent", new Object[] { currentHeadPosition });
		this.str_qmLQuestion = this.transe.translateMessage("nextheadposSegment", new Object[] { segmentCount });
		return;
	}

	public CScan(String pathToResource, Locale l) {
		this.transe = new Translator(pathToResource, l);
		
		headerProperties = new TextProperties();
		headerProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 24));
		
		rectProperties = new RectProperties();
		rectProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		rectProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
	}

//	#-----------------------------------------------#
//	|				AnimalAPI Methoden				|
//	#-----------------------------------------------#

	public void init() {
		this.lang = new AnimalScript("C-Scan", "Steffen Lott, Ozan Agtas", 800, 600);
		this.lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
		
		// Wird unter anderem beim Refresh druecken aufgerufen:
		this.refreshStrings();
		
		// ### STRINGS INITIALISIEREN ###
		this.str_queueHeader = transe.translateMessage("queue");

		this.str_modifyingAccess = this.transe.translateMessage("modifyAccess");
		this.str_readAccess = this.transe.translateMessage("readingAccess");
		this.str_resultHeader = this.transe.translateMessage("result");

		this.str_mcqm1Question = this.transe.translateMessage("question1");
		this.str_correctText = this.transe.translateMessage("correct");
		this.str_incorrectText = this.transe.translateMessage("incorrect");

		this.str_mcqm2Question = this.transe.translateMessage("question2");
		this.str_mcqm2Correct = this.transe.translateMessage("answer21");
		this.str_mcqm2Incorrect = this.transe.translateMessage("answer22");

		this.str_algoDescrHeader = this.transe.translateMessage("algoSteps");
		this.str_algoDescr1 = this.transe.translateMessage("algoStep1");
		this.str_algoDescr2 = this.transe.translateMessage("algoStep2");
		this.str_algoDescr3 = this.transe.translateMessage("algoStep3");
		this.str_algoDescr4 = this.transe.translateMessage("algoStep4");

		this.str_algoMeta1 = this.transe.translateMessage("algoMeta1");
		this.str_algoMeta2 = this.transe.translateMessage("algoMeta2");

		this.str_summary = this.transe.translateMessage("algoSummary");
		this.str_algoSummary1 = this.transe.translateMessage("algoSumm1");
		this.str_algoSummary2 = this.transe.translateMessage("algoSumm2");
		this.str_algoSummary3 = this.transe.translateMessage("algoSumm3");
		this.str_algoSummary4 = this.transe.translateMessage("algoSumm4");

	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) 
			throws IllegalArgumentException {
		this.currentHeadPosition = (Integer) primitives.get("currentHeadPosition");
		this.queueProperties = (ArrayProperties) props.getPropertiesByName("QueueProperties");
		this.goRight = (Boolean) primitives.get("moveRight");
		this.sourcecodeProperties = (SourceCodeProperties) props.getPropertiesByName("SourcecodeProperties");
		this.segmentCount = (Integer) primitives.get("segmentCount");
		this.queue = (int[]) primitives.get("queue");
		this.textProperties = new TextProperties();
		this.textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 14));

		if (currentHeadPosition < 0 || currentHeadPosition >= segmentCount) {
			return false;
		}
		if (segmentCount <= 0 || segmentCount < queue.length) {
			return false;
		}
		if (queue.length < 1) {
			return false;
		} else {
			for (int i = 0; i < queue.length; i++) {
				// Check auf negative Zahlen
				if (queue[i] < 0) {
					return false;
				}

				// Check auf Duplikate
				for (int j = 0; j < queue.length; j++) {
					if (queue[j] == queue[i] && i != j) {
						return false;
					}
				}
			}
		}

		return true;
	}

	@Override
	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		this.lang.setStepMode(true);
		ArrayList<Primitive> gui = new ArrayList<Primitive>(5);
		queList = new ArrayList<Integer>();
		resList = new ArrayList<Integer>();
		
		// Titel und Beschreibung
		
		Text header = this.lang.newText(new Coordinates(10, 10), this.getAlgorithmName(), "header", null, headerProperties);
		gui.add(header);

		this.lang.newText(new Offset(0, 50, header, AnimalScript.DIRECTION_SW), str_algoDescrHeader, "_algoDescrHead", null, headerProperties);
		this.lang.newText(new Offset(0, 5, "_algoDescrHead", AnimalScript.DIRECTION_SW), str_algoDescr1, "_algoDescr1", null, headerProperties);
		this.lang.newText(new Offset(0, 5, "_algoDescr1", AnimalScript.DIRECTION_SW), str_algoDescr2, "_algoDescr2", null, headerProperties);
		this.lang.newText(new Offset(0, 5, "_algoDescr2", AnimalScript.DIRECTION_SW), str_algoDescr3, "_algoDescr3", null, headerProperties);
		this.lang.newText(new Offset(0, 5, "_algoDescr3", AnimalScript.DIRECTION_SW), str_algoDescr4, "_algoDescr4", null, headerProperties);

		this.lang.newText(new Offset(0, 50, "_algoDescr3", AnimalScript.DIRECTION_SW), str_algoMeta1, "_algoMeta1", null, textProperties);
		this.lang.newText(new Offset(0, 5, "_algoMeta1", AnimalScript.DIRECTION_SW), str_algoMeta2, "_algoMeta2", null, textProperties);

		
		Rect headRect = this.lang.newRect(new Offset(-5, -5, header, AnimalScript.DIRECTION_NW),
				new Offset(5, 5, "header", AnimalScript.DIRECTION_SE), "hRect", null, rectProperties);
		gui.add(headRect);
		this.lang.nextStep();
		this.lang.hideAllPrimitivesExcept(gui);

		// Sourcecode
		srcCode = lang.newSourceCode(new Offset(550, 10, "hRect", AnimalScript.DIRECTION_NW), "sourceCode", null, sourcecodeProperties);

		srcCode.addCodeLine("public void cscan(List<int> queue, int head, int segmentCount, boolean goRight)", null, 0, null); // 0
		srcCode.addCodeLine("{", null, 0, null); // 1
		srcCode.addCodeLine("while (!(queue.isEmpty())) {", null, 1, null); // 2
		srcCode.addCodeLine("if (queue.contains(head)) {", null, 2, null); // 3
		srcCode.addCodeLine("memoryController.handleReadRequest(head);", null, 3, null); // 4
		srcCode.addCodeLine("queue.remove(head);", null, 3, null); // 5
		srcCode.addCodeLine("}", null, 2, null); // 6
		srcCode.addCodeLine("if (goRight) {", null, 2, null); // 7
		srcCode.addCodeLine("head = (head + 1) % segmentCount;", null, 3, null); // 8
		srcCode.addCodeLine("}", null, 2, null); // 9
		srcCode.addCodeLine("else", null, 2, null); // 10
		srcCode.addCodeLine("{", null, 2, null); // 11
		srcCode.addCodeLine("head = (head - 1) % segmentCount;", null, 3, null); // 12
		srcCode.addCodeLine("}", null, 2, null); // 13
		srcCode.addCodeLine("}", null, 1, null); // 14
		srcCode.addCodeLine("}", null, 0, null); // 15

		srcCode.registerLabel("start", 0);
		srcCode.registerLabel("while", 2);
		srcCode.registerLabel("if_C", 3);
		srcCode.registerLabel("doStuff1", 4);
		srcCode.registerLabel("removeFromQueue", 5);
		srcCode.registerLabel("if_goR", 7);
		srcCode.registerLabel("head+1", 8);
		srcCode.registerLabel("head-1", 12);

		this.lang.newRect(new Offset(-10, -10, "sourceCode", AnimalScript.DIRECTION_NW),
				new Offset(40, 10, "sourceCode", AnimalScript.DIRECTION_SE), "hRect2", null, rectProperties);

		srcCode.highlight("start");

		lang.newText(new Offset(20, 20, "hRect", AnimalScript.DIRECTION_SW), str_queueHeader, "txt1", null, textProperties);
		lang.newText(new Offset(10, 0, "txt1", AnimalScript.DIRECTION_SW), str_modifyingAccess, "txt2", null, textProperties);
		lang.newText(new Offset(0, 0, "txt2", AnimalScript.DIRECTION_SW), str_readAccess, "txt3", null, textProperties);

		// Queue Array (Array oben)
		for (Integer integer : queue) {
			queList.add(integer);
		}
		if (queueDisplay != null) {
			queueDisplay.hide();
		}
		queueDisplay = lang.newIntArray(new Offset(-10, 30, "txt3", AnimalScript.DIRECTION_SW), 
				queList.stream().mapToInt(x -> x).toArray(), "intArray", null, queueProperties);

		// Counter fuer modifizierender und lesender Zugriff
		counterQueue = lang.newCounter(queueDisplay);
		CounterProperties cp = new CounterProperties(); // Zaehler-Properties anlegen
		cp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 14));
		counterQueue.activateCounting();
		TwoValueView viewQ = lang.newCounterView(counterQueue, new Offset(0, 0, "txt2", AnimalScript.DIRECTION_NE),
				cp, true, false);

		viewQ.hideText();
		lang.newText(new Offset(-10, 100, "txt3", AnimalScript.DIRECTION_SW), str_resultHeader, "txt5", null, textProperties);

		drawStrahl(false);
		this.lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
		cScanAS();

		this.lang.nextStep();
		lang.hideAllPrimitivesExcept(gui);

		Text summhead = this.lang.newText(new Coordinates(10, 50), str_summary, "x", null, headerProperties);
		Text summ1 = this.lang.newText(new Offset(0, 20, summhead, AnimalScript.DIRECTION_SW), str_algoSummary1, "x1", null, textProperties);
		Text summ2 = this.lang.newText(new Offset(0, 10, summ1, AnimalScript.DIRECTION_SW), str_algoSummary2, "x2", null, textProperties);
		Text summ3 = this.lang.newText(new Offset(0, 10, summ2, AnimalScript.DIRECTION_SW), str_algoSummary3, "x3", null, textProperties);
		this.lang.newText(new Offset(0, 10, summ3, AnimalScript.DIRECTION_SW), str_algoSummary4, "x4", null, textProperties);

		this.lang.finalizeGeneration();
		return this.lang.toString();
	}

//	#-----------------------------------------------#
//	|			GUI Hilfsmethoden Methoden			|
//	#-----------------------------------------------#

	private Triangle headPtrTriangle;
	private Rect strahlRect;
	private Text headText;

	private void drawStrahl(boolean accessHappened) {
		int rectOffsetX = 20;
		int rectOffsetY = 350;
		int rectWidth = 400+((int)(this.segmentCount/100)) *100;
		int stepWidth = rectWidth / this.segmentCount;
		int headPtrX = this.currentHeadPosition * stepWidth + (stepWidth / 2);

		Point p = lang.newPoint(new Coordinates(rectOffsetX, rectOffsetY), "main", null, new PointProperties("idc"));

		strahlRect = lang.newRect(new Offset(0, 0, p, AnimalScript.DIRECTION_C), new Offset(rectWidth, 5, p, AnimalScript.DIRECTION_SE), "strahl", null, rectProperties);
		if (strahlRect == null) 
			lang.newText(new Offset(5, -5, strahlRect, AnimalScript.DIRECTION_NE), Integer.toString(this.segmentCount), "segmentCount", null);

		if (headPtrTriangle != null)
			headPtrTriangle.hide();

		headPtrTriangle = lang.newTriangle(new Offset(headPtrX - 3, -8, p, AnimalScript.DIRECTION_NW),
				new Offset(headPtrX, 0, p, AnimalScript.DIRECTION_NW),
				new Offset(headPtrX + 3, -8, p, AnimalScript.DIRECTION_NW), "headptr", null);

		if (headText != null)
			headText.hide();

		headText = lang.newText(new Offset(0, -15, headPtrTriangle, AnimalScript.DIRECTION_NW), Integer.toString(currentHeadPosition), "chp", null);

		if (accessHappened) {
			RectProperties rp = new RectProperties();
			rp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
			rp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GREEN);

			lang.newRect(new Offset(-stepWidth / 2, 0, headPtrTriangle, AnimalScript.DIRECTION_S), 
					new Offset(stepWidth / 2, 5, headPtrTriangle, AnimalScript.DIRECTION_S), "green", null, rp);
			lang.newText(new Offset(0, 10, headText, AnimalScript.DIRECTION_SW), Integer.toString(resList.size()), "accessCounter", null);
		}
	}
	
//	#-----------------------------------------------#
//	|				C-Scan Algorithmus				|
//	#-----------------------------------------------#

	public void cScanAS() {
		refreshStrings();
		Text currentHeadText = lang.newText(new Offset(-10, 0, "txt3", AnimalScript.DIRECTION_SW),
				str_currentHeadStatus, "txt4", null, textProperties);
		srcCode.toggleHighlight("start", "while");
		lang.nextStep("Start");

		int firstInQueue = queList.stream().filter(x -> (x % segmentCount) > currentHeadPosition).sorted().findFirst().get().intValue();
		int anyInQueue = queList.stream().filter(x -> x != firstInQueue).unordered().findAny().get().intValue();

		MultipleChoiceQuestionModel mcqm = new MultipleChoiceQuestionModel("first");
		mcqm.setPrompt(str_mcqm1Question);
		mcqm.addAnswer(Integer.toString(firstInQueue), 1, str_correctText);
		mcqm.addAnswer(Integer.toString(anyInQueue), 0, str_incorrectText);
		this.lang.addMCQuestion(mcqm);
		
		while (!(queList.isEmpty())) {
			srcCode.unhighlight("head+1");
			srcCode.unhighlight("head-1");
			srcCode.highlight("while");
			lang.nextStep("whileBegin_" + currentHeadPosition);

			srcCode.toggleHighlight("while", "if_C");
			counterQueue.accessInc(1);
			lang.nextStep();
			for (int i = 0; i < queList.size(); i++) {

				if (currentHeadPosition == queList.get(i)) {
					srcCode.toggleHighlight("if_C", "doStuff1");
					lang.nextStep();
					// 4: memoryController.handle (...)

					// 5: queue.remove(head)
					srcCode.toggleHighlight("doStuff1", "removeFromQueue");
					drawStrahl(true);
					lang.nextStep("AccessMemory_" + currentHeadPosition);
					queueDisplay.highlightElem(i, null, null);
					counterQueue.assignmentsInc(1);
					resList.add(queList.remove(i));
					
					// zur Visualisierung:
					if (queueDisplay != null) {
						queueDisplay.hide();
					}
					if (queList.size() > 0) {
						queueDisplay = lang.newIntArray(new Offset(-10, 30, "txt3", AnimalScript.DIRECTION_SW),
								queList.stream().mapToInt(x -> x).toArray(), "intArray", null, queueProperties);
					}
					if (resultDisplay != null) {
						resultDisplay.hide();
					}
					if (resList.size() > 0) {
						resultDisplay = lang.newIntArray(new Offset(0, 10, "txt5", AnimalScript.DIRECTION_SW),
								resList.stream().mapToInt(x -> x).toArray(), "intArray", null, queueProperties);
					}

					break;
				}

			}

			// ----------------------------
			srcCode.unhighlight("removeFromQueue");
			srcCode.toggleHighlight("if_C", "if_goR");

			lang.nextStep("moveHead_" + currentHeadPosition);
			if (goRight) {
				srcCode.toggleHighlight("if_goR", "head+1");
				if (currentHeadPosition == segmentCount - 2) {
					TrueFalseQuestionModel qmR = new TrueFalseQuestionModel("headposR");
					refreshStrings();
					qmR.setPrompt(str_qmRQuestion);
					// Richtig ist 0 (die Zahl Null)
					qmR.setCorrectAnswer(false);
					qmR.setPointsPossible(1);
					qmR.setFeedbackForAnswer(true, str_correctText);
					qmR.setFeedbackForAnswer(false, str_incorrectText);
					this.lang.addTFQuestion(qmR);
				}
				
				currentHeadPosition = (currentHeadPosition + 1) % segmentCount;

			} else {
				srcCode.toggleHighlight("if_goR", "head-1");
				if (currentHeadPosition == 1) {
					TrueFalseQuestionModel qmL = new TrueFalseQuestionModel("headposL");
					refreshStrings();
					qmL.setPrompt(str_qmLQuestion);
					// Richtig ist segmentCount-1
					qmL.setCorrectAnswer(true);
					qmL.setPointsPossible(1);
					qmL.setFeedbackForAnswer(true, str_correctText);
					qmL.setFeedbackForAnswer(false, str_incorrectText);
					this.lang.addTFQuestion(qmL);
					
				}
				currentHeadPosition = (currentHeadPosition - 1) % segmentCount;
			}

			refreshStrings();
			currentHeadText.setText(str_currentHeadStatus, null, null);

			drawStrahl(false);
			lang.nextStep();

		}

		srcCode.unhighlight("while");
		srcCode.highlight(15);

		MultipleChoiceQuestionModel mcqm2 = new MultipleChoiceQuestionModel("last");
		mcqm2.setPrompt(str_mcqm2Question);
		mcqm2.addAnswer(str_mcqm2Correct, 1, str_correctText);
		mcqm2.addAnswer(str_mcqm2Incorrect, 0, str_incorrectText);
		this.lang.addMCQuestion(mcqm2);

		mcqm2.getPointsAchieved();
		mcqm2.getPointsPossible();

		return;
	}

//	#-----------------------------------------------#
//	|					Metadaten					|
//	#-----------------------------------------------#

	@Override
	public String getName() {
		return "C-Scan";
	}

	@Override
	public String getAlgorithmName() {
		return "C-Scan";
	}

	@Override
	public String getAnimationAuthor() {
		return "Steffen Lott, Ozan Agtas";
	}

	@Override
	public String getDescription() {
		return this.transe.translateMessage("description");
	}

	@Override
	public String getCodeExample() {
		return "public void cscan(List<Integer> queue, int head, int segmentCount, boolean r) " + "\n" + "{" + "\n"
				+ "	while (!(queue.isEmpty())) {	" + "\n" + "		for (int i = 0; i < queue.size(); i++) {" + "\n"
				+ "			if (head == queue.get(i)) {" + "\n"
				+ "				// Gib das angefragte Speichersegment von der Festplatte retour" + "\n"
				+ "				// memoryController.readSegment (i);" + "\n\n"
				+ "				// Entferne angefragtes Speichersegment von der Warteschlange" + "\n"
				+ "				resList.add(queue.remove(i));" + "\n" + "			} " + "\n" + "		}" + "\n" + "\n"
				+ "		if (r) {" + "\n" + "			head = (head + 1) % segmentCount;" + "\n" + "		} else {"
				+ "\n" + "			head = (head - 1) % segmentCount;" + "\n" + "		}" + "\n" + "	}" + "\n" + "}";
	}

	@Override
	public String getFileExtension() {
		return "asu";
	}

	@Override
	public Locale getContentLocale() {
		return transe.getCurrentLocale();
	}

	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_HARDWARE);
	}

	@Override
	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

}
