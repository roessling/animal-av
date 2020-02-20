/*
 * VectorClock.java
 * Sven Dotzauer-Klier, Gregor Heß, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.network;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;
import java.util.Random;

import algoanim.primitives.Graph;
import algoanim.primitives.Point;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.LinkedHashMap;

import generators.framework.properties.AnimationPropertiesContainer;
import generators.network.vectorclock.EventType;
import generators.network.vectorclock.Process;
import generators.network.vectorclock.ProcessEvent;
import generators.network.vectorclock.VectorClockStyle;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.MultipleSelectionQuestionModel;
import interactionsupport.models.QuestionGroupModel;
import interactionsupport.models.TrueFalseQuestionModel;
import translator.Translator;
import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.addons.Slide;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PointProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.OffsetFromLastPosition;

public class VectorClock implements ValidatingGenerator {
    private Language lang;
    private Variables var;
    private String[][] Events;
    private MatrixProperties ClockVectorProperties;
    private String[] Processes;
    private Color HighlightColor;
    private boolean SkipDescription;
    private SourceCodeProperties SourceCodeProperties;
    private int QuestionLikelihood;
    private int TimelineLength;
    
    private Translator translator;
    private Locale locale;
    private Random rand;
	
    // Strings for ANIMAL ids
	private static final String PROCESS_PREFIX = "Process";
	private static final String CLOCK_VECTOR_PREFIX = "ClockVector";
	private static final String RECEIVED_CLOCK_VECTOR_PREFIX = "RcvClockVector";
	private static final String LINE_PREFIX = "Line";
	private static final String EVENT_PREFIX = "Event";
	private static final String MESSAGE_PREFIX = "Message";
	private static final String CURRENT_ACTIVITY = "CurrentActivity";
	private static final String CURRENT_PROCESS_LABEL = "CurrentProcessLabel";
	private static final String CURRENT_PROCESS = "CurrentProcess";
	private static final String CURRENT_EVENT = "CurrentEvent";
	private static final String CURRENT_EVENT_LABEL = "CurrentEventLabel";
	private static final String OTHER_EVENT = "OtherEvent";
	private static final String OTHER_EVENT_LABEL = "OtherEventLabel";
	private static final String FOUND_EVENTS_LABEL = "FoundEventsLabel";
	
	// question groups and ids
	private static final String QUESTIONS_INITIALIZATION = "qInitialization";
	private static final String QUESTIONS_EVENT_TYPE = "qEventType";
	private static final String QUESTIONS_EVENT_ACTION_SEQ = "qEventActionSeq";
	private static final String QUESTIONS_EVENT_TIMESTAMP = "qEventTimestamp";
	private static final String QUESTIONS_EVENT_HAPPENED_BEFORE = "qEventHappenedBefore";
	
	private Text currActivity, currProcLabel, currEventLabel, otherEventLabel, foundEventsLabel;
	
	private ArrayList<ProcessEvent> events;
	private LinkedHashMap<String, Process> processes;
	private int eventCounter = 0;
	private ArrayList<ProcessEvent> eventsToCheck;
	
	public void doVectorClock() {
		
		// ANIMAL: heading with algo name and box
		TextProperties tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 24));
		Text title = lang.newText(new Coordinates(10, 10), "Vector Clock", "title", null, tp);
		RectProperties rp = new RectProperties();
		Rect rect = lang.newRect(new Offset(-5, -5, "title", "NW"),
	        new Offset(5, 5, "title", "SE"), "topicRect", null, rp);
		
		if(!SkipDescription) {
			lang.nextStep(this.translator.translateMessage("introduction"));
			Slide introSlide = new Slide(lang, "resources/VectorClock/"+translator.translateMessage("slideIntroduction"), "title", new VectorClockStyle());
			introSlide.hide();
		}
		
		// placeholder between heading and animation
		PointProperties pp = new PointProperties();
		pp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE);
		lang.newPoint(new Offset(0, 5, "title", "SW"), "placeholder", null, pp);
		
		String offsetNodeName = "placeholder";
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 20));
		
		// create processes
		// grid does not have specified fill color in ANIMAL. Why?
		ClockVectorProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.MONOSPACED, Font.PLAIN, 12));
		ClockVectorProperties.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, (Color)ClockVectorProperties.get(AnimationPropertiesKeys.FILL_PROPERTY));
		ClockVectorProperties.set(AnimationPropertiesKeys.GRID_HIGHLIGHT_BORDER_COLOR_PROPERTY, HighlightColor);
		ClockVectorProperties.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, HighlightColor);
		
		PolylineProperties lp = new PolylineProperties();
		
		this.processes = new LinkedHashMap<String, Process>();
		int tmp = 0;
		for(String p : this.Processes) {
			Process process = new Process(this.Processes, tmp++);
			
			// ANIMAL: per process: add process name, grid for vector clocks (own + received) and (time)line for events
			// name
			process.name = lang.newText(new Offset(0, 5, offsetNodeName, "SW"), p, VectorClock.PROCESS_PREFIX + p, null, tp);
			// clock vectors
			String[][] clockGrid = new String[this.Processes.length][];
			for(int i=0; i<this.Processes.length; i++)
				clockGrid[i] = new String[] {this.Processes[i], "0"};
			// own clock vector
			process.clockVector = lang.newStringMatrix(new Offset(0, 1, VectorClock.PROCESS_PREFIX + p, "SW"),
					clockGrid,
					VectorClock.CLOCK_VECTOR_PREFIX + p, null, ClockVectorProperties);
			// received clock vector
			process.clockVectorSR = lang.newStringMatrix(new Offset(2, 0, VectorClock.CLOCK_VECTOR_PREFIX + p, "NE"),
					clockGrid,
					VectorClock.RECEIVED_CLOCK_VECTOR_PREFIX + p, null, ClockVectorProperties);
			process.clockVector.hide();
			process.clockVectorSR.hide();
			// event time line
			process.line = lang.newPolyline(new Node[] {new Offset(10, -11, VectorClock.RECEIVED_CLOCK_VECTOR_PREFIX + p, "NE"), new OffsetFromLastPosition(this.TimelineLength ,0)}, VectorClock.LINE_PREFIX + p, null, lp);
			
			offsetNodeName = VectorClock.CLOCK_VECTOR_PREFIX + p;
			
			this.processes.put(p, process);
		}
		
		// parse event string arrays and create events (sorted by time)
		// 0: time of event
		// 1: process name (of sender)
		// 2: event type: I(nternal) or M(essage)
		// 3: if M: name of receiving process
		// 4: if M: time of reception
		// 5: if y: check for events that happened before
		// Event id: Event+Process+Time
		CircleProperties cp = new CircleProperties();
		cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		
		// find minimum and maximum event time
		int maxTime = 0, minTime = Integer.MAX_VALUE;
		for(String[] ev : this.Events) {
			int time = Integer.parseInt(ev[0]);
			int time2 = time;
			if(ev[2].equals("M") || ev[2].equals("m"))
				time2 = Integer.parseInt(ev[4]);
			if(time > maxTime)
				maxTime = time;
			if(time < minTime)
				minTime = time;
			if(time != time2) {
				if(time2 > maxTime)
					maxTime = time2;
				if(time2 < minTime)
					minTime = time2;
			}
		}
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 10));
		this.events = new ArrayList<ProcessEvent>();
		this.eventsToCheck = new ArrayList<ProcessEvent>();
		for(String[] ev : this.Events) {
			// internal event
			if(ev[2].equals("I") || ev[2].equals("i")) {
				ProcessEvent event = new ProcessEvent(Integer.parseInt(ev[0]), ev[1], EventType.INTERNAL);
				String eventId = VectorClock.EVENT_PREFIX + ev[1] + ev[0];
				
				// ANIMAL: draw circle for each internal event on timeline of process and add label
				int position = (int) ((Integer.parseInt(ev[0]) - minTime) / (float)(maxTime - minTime) * this.TimelineLength);
				event.circle = lang.newCircle(new Offset(position, 0, VectorClock.LINE_PREFIX + ev[1], "W"),
						4, eventId, null, cp);
				this.events.add(event);
				// store event to check for earlier events
				if(ev[5].equals("y") || ev[5].equals("Y"))
					this.eventsToCheck.add(event);
			}
			// message event -> 1 message sent and 1 message received event
			else if(ev[2].equals("M") || ev[2].equals("m")) {
				ProcessEvent event1 = new ProcessEvent(Integer.parseInt(ev[0]), ev[1], EventType.MESSAGE_SENT);
				String event1Id = VectorClock.EVENT_PREFIX + ev[1] + ev[0];
				// ANIMAL: draw circle for each message sent event on timeline of process
				int position = (int) ((Integer.parseInt(ev[0]) - minTime) / (float)(maxTime - minTime) * this.TimelineLength);
				event1.circle = lang.newCircle(new Offset(position, 0, VectorClock.LINE_PREFIX + ev[1], "W"),
						4, event1Id, null, cp);
				
				ProcessEvent event2 = new ProcessEvent(Integer.parseInt(ev[4]), ev[3], EventType.MESSAGE_RECEIVED);
				String event2Id = VectorClock.EVENT_PREFIX + ev[3] + ev[4];
				// ANIMAL: draw circle for each message received event on timeline of process
				position = (int) (((Integer.parseInt(ev[4]) - minTime) / (float)(maxTime - minTime)) * this.TimelineLength);
				event2.circle = lang.newCircle(new Offset(position, 0, VectorClock.LINE_PREFIX + ev[3], "W"),
						4, event2Id, null, cp);
				
				// ANIMAL: draw arrow between message events
				// message id: message+event1+event2
				lp.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
				Polyline arrow = lang.newPolyline(new Node[] {
						new Offset(0, 0, VectorClock.EVENT_PREFIX + ev[1] + ev[0], "C"),
						new Offset(0, 0, VectorClock.EVENT_PREFIX + ev[3] + ev[4], "C")
				}, VectorClock.MESSAGE_PREFIX+event1Id+event2Id, null, lp);
				event1.messageArrow = arrow;
				event2.messageArrow = arrow;
				event1.otherEvent = event2;
				event2.otherEvent = event1;
				this.events.add(event1);
				this.events.add(event2);
				// store event to check for earlier events
				if(ev[5].equals("y") || ev[5].equals("Y"))
					this.eventsToCheck.add(event2);
			}
		}
		// sort events by time
		Collections.sort(this.events);
		for(ProcessEvent pe : this.events) {
			pe.id = eventCounter;
			pe.nameLabel = lang.newText(new Offset(-5, -20, VectorClock.EVENT_PREFIX + pe.process + pe.time, "W"), "E"+eventCounter, "E"+eventCounter, null, tp);
			eventCounter++;
		}
		
		// ANIMAL: show current process and activity
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 16));
		this.currActivity = lang.newText(new Offset(50, -15, VectorClock.LINE_PREFIX + this.Processes[0], "NE"), this.translator.translateMessage("currentActivityInit"), VectorClock.CURRENT_ACTIVITY, null, tp);
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.MONOSPACED, Font.PLAIN, 16));
		this.currProcLabel = lang.newText(new Offset(0, 24, VectorClock.CURRENT_ACTIVITY, "SW"), this.translator.translateMessage("currentProcessLabel")+":", VectorClock.CURRENT_PROCESS_LABEL, null, tp);
		Text currProc = lang.newText(new Offset(4, 0, VectorClock.CURRENT_PROCESS_LABEL, "NE"), this.translator.translateMessage("noElement"), VectorClock.CURRENT_PROCESS, null, tp);
		
		// ANIMAL: source code
		SourceCodeProperties.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, HighlightColor);
		SourceCode scInit = lang.newSourceCode(new Offset(0, 2, VectorClock.CURRENT_PROCESS_LABEL, "SW"), "sourceCodeInit", null, SourceCodeProperties);
	    scInit.addCodeLine(this.translator.translateMessage("codeInitLine1"), null, 0, null);
	    scInit.addCodeLine(this.translator.translateMessage("codeInitLine2"), null, 1, null);
	    scInit.addCodeLine(this.translator.translateMessage("codeInitLine3"), null, 1, null);
		
	    SourceCode sc = lang.newSourceCode(new Offset(0, 2, VectorClock.CURRENT_PROCESS_LABEL, "SW"), "sourceCode", null, SourceCodeProperties);
	    sc.addCodeLine(this.translator.translateMessage("code1Line1"), null, 0, null);
	    sc.addCodeLine(this.translator.translateMessage("code1Line2"), null, 1, null);
	    sc.addCodeLine(this.translator.translateMessage("code1Line3"), null, 0, null);
	    sc.addCodeLine(this.translator.translateMessage("code1Line4"), null, 1, null);
	    sc.addCodeLine(this.translator.translateMessage("code1Line5"), null, 1, null);
	    sc.addCodeLine(this.translator.translateMessage("code1Line6"), null, 0, null);
	    sc.addCodeLine(this.translator.translateMessage("code1Line7"), null, 1, null);
	    sc.addCodeLine(this.translator.translateMessage("code1Line8"), null, 1, null);
	    sc.addCodeLine(this.translator.translateMessage("code1Line9"), null, 2, null);
	    sc.addCodeLine(this.translator.translateMessage("code1Line10"), null, 0, null);
	    sc.hide();
	    
	    // for when all events are processed: show if happened before
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.MONOSPACED, Font.PLAIN, 16));
		this.currEventLabel = lang.newText(new Offset(0, 24, VectorClock.CURRENT_ACTIVITY, "SW"), this.translator.translateMessage("currentEventLabel")+":", VectorClock.CURRENT_EVENT_LABEL, null, tp);
		Text currEvent = lang.newText(new Offset(4, 0, VectorClock.CURRENT_EVENT_LABEL, "NE"), this.translator.translateMessage("noElement"), VectorClock.CURRENT_EVENT, null, tp);
		this.otherEventLabel = lang.newText(new Offset(0, 4, VectorClock.CURRENT_EVENT_LABEL, "SW"), this.translator.translateMessage("otherEventLabel")+":", VectorClock.OTHER_EVENT_LABEL, null, tp);
		Text otherEvent = lang.newText(new Offset(4, 0, VectorClock.OTHER_EVENT_LABEL, "NE"), this.translator.translateMessage("noElement"), VectorClock.OTHER_EVENT, null, tp);
	    SourceCode sc2 = lang.newSourceCode(new Offset(0, 2, VectorClock.OTHER_EVENT_LABEL, "SW"), "sourceCode2", null, SourceCodeProperties);
	    sc2.addCodeLine(this.translator.translateMessage("code2Line1"), null, 0, null);
	    sc2.addCodeLine(this.translator.translateMessage("code2Line2"), null, 1, null);
	    sc2.addCodeLine(this.translator.translateMessage("code2Line3"), null, 2, null);
	    sc2.addCodeLine(this.translator.translateMessage("code2Line4"), null, 1, null);
	    sc2.addCodeLine(this.translator.translateMessage("code2Line5"), null, 2, null);
	    sc2.addCodeLine(this.translator.translateMessage("code2Line6"), null, 0, null);
	    sc2.addCodeLine(this.translator.translateMessage("code2Line7"), null, 1, null);
	    sc2.addCodeLine(this.translator.translateMessage("code2Line8"), null, 2, null);

		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.MONOSPACED, Font.PLAIN, 12));
	    this.foundEventsLabel = lang.newText(new Offset(0, 8, "sourceCode2", "SW"), translator.translateMessage("foundEventsLabel")+":", VectorClock.FOUND_EVENTS_LABEL, null, tp);
	    Text foundEvents = lang.newText(new Offset(2, 0, VectorClock.FOUND_EVENTS_LABEL, "NE"), "", VectorClock.FOUND_EVENTS_LABEL, null, tp);
	    this.foundEventsLabel.hide();
		this.currEventLabel.hide();
		currEvent.hide();
		this.otherEventLabel.hide();
		otherEvent.hide();
	    sc2.hide();

	    QuestionGroupModel qgInit = new QuestionGroupModel(VectorClock.QUESTIONS_INITIALIZATION, 1);
	    lang.addQuestionGroup(qgInit);
	    
	    if(rand.nextInt(100) < QuestionLikelihood) {
		    // question: what value to initialize vectors?
		    FillInBlanksQuestionModel qi1 = new FillInBlanksQuestionModel(VectorClock.QUESTIONS_INITIALIZATION+"Value");
		    qi1.setPrompt(this.translator.translateMessage("qInitValue"));
		    qi1.addAnswer("correct", "0", 1, this.translator.translateMessage("aInitValue"));
		    qi1.setGroupID(VectorClock.QUESTIONS_INITIALIZATION);
		    lang.addFIBQuestion(qi1);
	    }
	    
	    // show code for initialization, new current activity
		lang.nextStep(this.translator.translateMessage("initialization"));
	    
	    // question: how many rows per vector?
	    if(rand.nextInt(100) < QuestionLikelihood) {
		    FillInBlanksQuestionModel qi2 = new FillInBlanksQuestionModel(VectorClock.QUESTIONS_INITIALIZATION+"Rows");
		    qi2.setPrompt(this.translator.translateMessage("qInitRows"));
		    qi2.addAnswer("correct", Integer.toString(this.Processes.length), 1, this.translator.translateMessage("aInitRows", new Object[]{this.Processes.length}));
		    qi2.setGroupID(VectorClock.QUESTIONS_INITIALIZATION);
		    lang.addFIBQuestion(qi2);
	    }

		scInit.highlight(0, 0, false);
		lang.nextStep();
		scInit.highlight(0, 0, true);
		scInit.highlight(1, 0, false);
		scInit.highlight(2, 0, false);
		
		// ANIMAL: show initialization
		this.processes.forEach((name, process)->{
			currProc.setText(name, null, null);
			currProc.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, HighlightColor, null, null);
			process.name.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, HighlightColor, null, null);
			for(int i=0;i<process.clockVector.getNrRows();i++) {
				process.clockVector.highlightCell(i, 0, null, null);
				process.clockVector.highlightCell(i, 1, null, null);
				process.clockVector.highlightElem(i, 1, null, null);
			}
			process.clockVector.show();
			lang.nextStep();

			for(int i=0;i<process.clockVector.getNrRows();i++) {
				process.clockVector.unhighlightCell(i, 0, null, null);
				process.clockVector.unhighlightCell(i, 1, null, null);
				process.clockVector.unhighlightElem(i, 1, null, null);
			}
			currProc.setText(this.translator.translateMessage("noElement"), null, null);
			currProc.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
			process.name.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
		});
		scInit.unhighlight(0);
		scInit.unhighlight(1);
		scInit.unhighlight(2);
		lang.nextStep(this.translator.translateMessage("initializationComplete"));
		scInit.hide();
		
		// start vector clock algorithm
		vectorClock(this.Processes, sc, currProc, currEvent, otherEvent, sc2, foundEvents);
		
		// add final slide, hide all primitives
		lang.hideAllPrimitivesExcept(Arrays.asList(new Primitive[] {title, rect}));
		Slide slideConclusion = new Slide(lang, "resources/VectorClock/"+translator.translateMessage("slideConclusion"), "title", new VectorClockStyle());
		lang.nextStep(this.translator.translateMessage("conclusion"));
	}
	
	private void vectorClock(String[] processes, SourceCode sc, Text currProc, Text currEvent, Text otherEvent, SourceCode sc2, Text foundEvents) {
		this.currActivity.setText(this.translator.translateMessage("currentActivity1"), null, null);
		sc.show();
		
		// questions: event type? which actions for given type?
	    QuestionGroupModel qgEvType = new QuestionGroupModel(VectorClock.QUESTIONS_EVENT_TYPE, 3);
	    lang.addQuestionGroup(qgEvType);
	    // questions: action sequence?
	    QuestionGroupModel qgEvActionSeq = new QuestionGroupModel(VectorClock.QUESTIONS_EVENT_ACTION_SEQ, 2);
	    lang.addQuestionGroup(qgEvActionSeq);
	    // questions: event timestamps?
	    QuestionGroupModel qgEvTimestamps = new QuestionGroupModel(VectorClock.QUESTIONS_EVENT_TIMESTAMP, 2);
	    lang.addQuestionGroup(qgEvTimestamps);
	    // questions: event happened before other event?
	    QuestionGroupModel qgEvHapBef = new QuestionGroupModel(VectorClock.QUESTIONS_EVENT_HAPPENED_BEFORE, 2);
	    lang.addQuestionGroup(qgEvHapBef);
		
		this.events.forEach(event->{
			Process currentProcess = this.processes.get(event.process);
			
		    if(rand.nextInt(100) < QuestionLikelihood) {
				// event type questions
		    	if(rand.nextInt(100)<50 || event.type.equals(EventType.INTERNAL)) {
			    	if(rand.nextInt(100)<50) {
				    	// question: event type?
				    	MultipleChoiceQuestionModel q = new MultipleChoiceQuestionModel(VectorClock.QUESTIONS_EVENT_TYPE+event.nameLabel.getText());
					    q.setGroupID(VectorClock.QUESTIONS_EVENT_TYPE);
					    q.setPrompt(this.translator.translateMessage("qEvType1", new Object[] {event.nameLabel.getText()}));
					    q.addAnswer("internal", this.translator.translateMessage("evIN"), event.type.equals(EventType.INTERNAL) ? 1 : 0,
					    		event.type.equals(EventType.INTERNAL) ? this.translator.translateMessage("correctAnswer") : this.translator.translateMessage("wrongAnswer"));
					    q.addAnswer("messageSent", this.translator.translateMessage("evMS"), event.type.equals(EventType.MESSAGE_SENT) ? 1 : 0,
					    		event.type.equals(EventType.MESSAGE_SENT) ? this.translator.translateMessage("correctAnswer") : this.translator.translateMessage("wrongAnswer"));
					    q.addAnswer("messageReceived", this.translator.translateMessage("evMR"), event.type.equals(EventType.MESSAGE_RECEIVED) ? 1 : 0,
					    		event.type.equals(EventType.MESSAGE_RECEIVED) ? this.translator.translateMessage("correctAnswer") : this.translator.translateMessage("wrongAnswer"));
					    lang.addMCQuestion(q);
			    	}
			    	else {
			    		// question: what actions for event type?
			    		MultipleSelectionQuestionModel q = new MultipleSelectionQuestionModel(VectorClock.QUESTIONS_EVENT_TYPE+event.nameLabel.getText());
					    q.setGroupID(VectorClock.QUESTIONS_EVENT_TYPE);
					    q.setPrompt(this.translator.translateMessage("qEvType2", new Object[] {event.nameLabel.getText()}));
					    q.addAnswer("increment", this.translator.translateMessage("actionIncrement"),
					    		(event.type.equals(EventType.INTERNAL) || event.type.equals(EventType.MESSAGE_SENT) || event.type.equals(EventType.MESSAGE_RECEIVED)) ? 1 : -1,
					    				(event.type.equals(EventType.INTERNAL) || event.type.equals(EventType.MESSAGE_SENT) || event.type.equals(EventType.MESSAGE_RECEIVED)) ? this.translator.translateMessage("correctAnswer") : this.translator.translateMessage("wrongAnswer"));
					    q.addAnswer("sendVector", this.translator.translateMessage("actionSend"), event.type.equals(EventType.MESSAGE_SENT) ? 1 : -1,
					    		event.type.equals(EventType.MESSAGE_SENT) ? this.translator.translateMessage("correctAnswer") : this.translator.translateMessage("wrongAnswer"));
					    q.addAnswer("findMaximum", this.translator.translateMessage("actionMax"), event.type.equals(EventType.MESSAGE_RECEIVED) ? 1 : -1,
					    		event.type.equals(EventType.MESSAGE_RECEIVED) ? this.translator.translateMessage("correctAnswer") : this.translator.translateMessage("wrongAnswer"));
					    lang.addMSQuestion(q);
			    	}
		    	}
		    	// event action sequence questions
		    	else {
			    	MultipleChoiceQuestionModel q = new MultipleChoiceQuestionModel(VectorClock.QUESTIONS_EVENT_ACTION_SEQ+event.nameLabel.getText());
				    q.setGroupID(VectorClock.QUESTIONS_EVENT_ACTION_SEQ);
				    q.setPrompt(this.translator.translateMessage("qEvActionSeq1"));
				    q.addAnswer("action1", this.translator.translateMessage("actionIncrement"), 1, this.translator.translateMessage("correctAnswer"));
				    q.addAnswer("action2",
				    		event.type.equals(EventType.MESSAGE_SENT) ? this.translator.translateMessage("actionSend") : this.translator.translateMessage("actionMax"),
				    				0, this.translator.translateMessage("wrongAnswer"));
				    lang.addMCQuestion(q);
		    	}
		    }
		    
			// ANIMAL
			// highlight event, process, change current process
			event.circle.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, HighlightColor, null, null);
			event.nameLabel.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, HighlightColor, null, null);
			currentProcess.name.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, HighlightColor, null, null);
			currProc.setText(currentProcess.name.getText(), null, null);
			currProc.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, HighlightColor, null, null);
			
			switch (event.type) {
				// internal event: increase own counter by 1
				case INTERNAL:
					currentProcess.clock.put(
							event.process,
							currentProcess.clock.get(event.process) + 1);
					
					// ANIMAL
					// highlight code
					sc.highlight(0, 0, false);
					lang.nextStep();
					
					this.createTimestampQuestion(currentProcess, event);
					
					// highlight code, grid and update grid
					sc.highlight(0, 0, true);
					currProc.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
					sc.highlight(1, 0, false);
					currentProcess.clockVector.highlightCellColumnRange(currentProcess.clockRow, 0, 1, null, null);
					currentProcess.clockVector.highlightElem(currentProcess.clockRow, 1, null, null);
					lang.nextStep();
					
					currentProcess.clockVector.put(currentProcess.clockRow, 1, currentProcess.clock.get(event.process).toString(), null, null);
					lang.nextStep();
					
					// unhighlight
					sc.unhighlight(0);
					sc.unhighlight(1);
					currentProcess.clockVector.unhighlightCell(currentProcess.clockRow, 0, null, null);
					currentProcess.clockVector.unhighlightCell(currentProcess.clockRow, 1, null, null);
					currentProcess.clockVector.unhighlightElem(currentProcess.clockRow, 1, null, null);
					
					break;
				
				// message sent:
				case MESSAGE_SENT:
					// increase own counter by 1
					currentProcess.clock.put(
							event.process,
							currentProcess.clock.get(event.process) + 1);
					
					// ANIMAL
					sc.highlight(2, 0, false);
					event.messageArrow.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, HighlightColor, null, null);
					lang.nextStep();
					
					this.createTimestampQuestion(currentProcess, event);
					
					// highlight code, grid and update grids
					sc.highlight(2, 0, true);
					currProc.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
					sc.highlight(3, 0, false);
					currentProcess.clockVector.highlightCellColumnRange(currentProcess.clockRow, 0, 1, null, null);
					currentProcess.clockVector.highlightElem(currentProcess.clockRow, 1, null, null);
					lang.nextStep();
					
					currentProcess.clockVector.put(currentProcess.clockRow, 1, currentProcess.clock.get(event.process).toString(), null, null);
					for(int i=0; i<currentProcess.clockVector.getNrRows();i++) {
						currentProcess.clockVectorSR.put(i, 1, currentProcess.clockVector.getElement(i, 1), null, null);
					}
					lang.nextStep();
					
					// send own clock
					event.sentClockVector = (LinkedHashMap<String, Integer>) currentProcess.clock.clone();
					
					// ANIMAL
					sc.unhighlight(3);
					sc.highlight(4, 0, false);
					currentProcess.clockVector.unhighlightCell(currentProcess.clockRow, 0, null, null);
					currentProcess.clockVector.unhighlightCell(currentProcess.clockRow, 1, null, null);
					currentProcess.clockVector.unhighlightElem(currentProcess.clockRow, 1, null, null);
					// highlight sent clock
					for (int i=0;i<currentProcess.clockVectorSR.getNrRows(); i++) {
						currentProcess.clockVectorSR.highlightCell(i, 0, null, null);
						currentProcess.clockVectorSR.highlightCell(i, 1, null, null);
					}
					currentProcess.clockVectorSR.show();
					lang.nextStep();
					
					// unhighlight
					// do not do lang.nextStep() here!
					sc.unhighlight(2);
					sc.unhighlight(4);
					for (int i=0;i<currentProcess.clockVectorSR.getNrRows(); i++) {
						currentProcess.clockVectorSR.unhighlightCell(i, 0, null, null);
						currentProcess.clockVectorSR.unhighlightCell(i, 1, null, null);
					}
					currentProcess.clockVectorSR.hide();
					
					break;
				
				// message received:
				case MESSAGE_RECEIVED:
					// increase own counter by one
					currentProcess.clock.put(
							event.process,
							currentProcess.clock.get(event.process) + 1);
					
					// ANIMAL
					sc.highlight(5, 0, false);
					event.messageArrow.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, HighlightColor, null, null);
					// update received vector grid
					for(int i=0; i<currentProcess.clockVectorSR.getNrRows();i++) {
						currentProcess.clockVectorSR.put(i, 1, event.otherEvent.sentClockVector.get(currentProcess.clockVectorSR.getElement(i, 0)).toString(), null, null);
						currentProcess.clockVectorSR.highlightCell(i, 0, null, null);
						currentProcess.clockVectorSR.highlightCell(i, 1, null, null);
					}
					currentProcess.clockVectorSR.show();
					lang.nextStep();
					
					// highlight code, grid and update grid, unhighlight received grid
					sc.highlight(5, 0, true);
					currProc.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
					sc.highlight(6, 0, false);
					currentProcess.clockVector.highlightCellColumnRange(currentProcess.clockRow, 0, 1, null, null);
					currentProcess.clockVector.highlightElem(currentProcess.clockRow, 1, null, null);
					for (int i=0;i<currentProcess.clockVectorSR.getNrRows(); i++) {
						currentProcess.clockVectorSR.unhighlightCell(i, 0, null, null);
						currentProcess.clockVectorSR.unhighlightCell(i, 1, null, null);
					}
					lang.nextStep();
					
					currentProcess.clockVector.put(currentProcess.clockRow, 1, currentProcess.clock.get(event.process).toString(), null, null);
					lang.nextStep();
					
					sc.unhighlight(6);
					sc.highlight(7, 0, true);
					sc.highlight(8, 0, false);
					currentProcess.clockVector.unhighlightCell(currentProcess.clockRow, 0, null, null);
					currentProcess.clockVector.unhighlightCell(currentProcess.clockRow, 1, null, null);
					currentProcess.clockVector.unhighlightElem(currentProcess.clockRow, 1, null, null);
					
					// take maximum of every other entry of the sender's vector clock and own clock
					currentProcess.clock.forEach((name, counter)->{
						currentProcess.clock.put(
								name,
								Math.max(
										// sender process
										event.otherEvent.sentClockVector.get(name),
										// receiver process
										currentProcess.clock.get(name))
								);
					});
					
					this.createTimestampQuestion(currentProcess, event);
					
					// ANIMAL
					for(int i=0; i<currentProcess.clockVector.getNrRows(); i++) {
						currentProcess.clockVector.highlightCellColumnRange(i, 0, 1, null, null);
						currentProcess.clockVectorSR.highlightCellColumnRange(i, 0, 1, null, null);
						currentProcess.clockVector.highlightElem(i, 1, null, null);
						currentProcess.clockVectorSR.highlightElem(i, 1, null, null);
						lang.nextStep();
						
						if(Integer.parseInt(currentProcess.clockVector.getElement(i, 1)) < Integer.parseInt(currentProcess.clockVectorSR.getElement(i, 1))) {
							currentProcess.clockVector.put(i, 1, currentProcess.clock.get(
									currentProcess.clockVector.getElement(i, 0)).toString(), null, null);
							lang.nextStep();
						}
						
						currentProcess.clockVector.unhighlightCell(i, 0, null, null);
						currentProcess.clockVector.unhighlightCell(i, 1, null, null);
						currentProcess.clockVectorSR.unhighlightCell(i, 0, null, null);
						currentProcess.clockVectorSR.unhighlightCell(i, 1, null, null);
						currentProcess.clockVector.unhighlightElem(i, 1, null, null);
						currentProcess.clockVectorSR.unhighlightElem(i, 1, null, null);
					}
					
					// unhighlight
					sc.unhighlight(5);
					sc.unhighlight(7);
					sc.unhighlight(8);
					currentProcess.clockVectorSR.hide();
					
					break;
			}
			// attach vector clock grid to event, update vector clock of event and show
			sc.highlight(9, 0, false);
			String[][] grid = new String[processes.length][];
			String id = "";
			for(int i=0; i<processes.length;i++) {
				grid[i] = new String[] {currentProcess.clock.get(processes[i]).toString()};
				id += processes[i]+currentProcess.clock.get(processes[i]).toString();
			}
			event.computedClockVectorGrid = lang.newStringMatrix(new Offset(0, 5, VectorClock.EVENT_PREFIX+event.process+event.time, "SW"), grid, VectorClock.CLOCK_VECTOR_PREFIX+id, null, ClockVectorProperties);
			event.computedClockVector = (LinkedHashMap<String, Integer>) currentProcess.clock.clone();
			lang.nextStep(event.nameLabel.getText() + " "+this.translator.translateMessage("oneProcessed"));
			if(event.messageArrow != null)
				event.messageArrow.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
			event.circle.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLACK, null, null);
			event.nameLabel.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
			currentProcess.name.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
			sc.unhighlight(9);
		});

		// done with timestamps
		currProc.setText(this.translator.translateMessage("noElement"), null, null);
		lang.nextStep(this.translator.translateMessage("allProcessed"));
		
		sc.hide();
		this.currProcLabel.hide();
		currProc.hide();
		this.currEventLabel.show();
		currEvent.show();
		this.otherEventLabel.show();
		otherEvent.show();
	    sc2.show();
	    this.foundEventsLabel.show();
	    this.currActivity.setText(this.translator.translateMessage("currentActivity2"), null, null);
	    lang.nextStep(this.translator.translateMessage("compareEvents"));

		// check for partial order
		// create graph for partial order
		Point[] points = new Point[this.eventCounter];
		PointProperties pp = new PointProperties();
		Node[] nodes = new Node[this.eventCounter];
		String[] labels = new String[this.eventCounter];
		int[][] adj = new int[this.eventCounter][];
		int[] adjRow = new int[this.eventCounter];
		boolean[][] hapBef = new boolean[this.eventCounter][];
		boolean[] hapBefRow = new boolean[this.eventCounter];
		int[] elementsInRow = new int[this.eventCounter];
		int[] rowOfEvent = new int[this.eventCounter];
		for(int j=0;j<this.eventCounter;j++) {
			adjRow[j] = 0;
			hapBefRow[j] = false;
			nodes[j] = null;
			elementsInRow[j] = 0;
			rowOfEvent[j] = -1;
		}
		for(int j=0;j<this.eventCounter;j++) {
			adj[j] = adjRow.clone();
			hapBef[j] = hapBefRow.clone();
		}
		for(int i=this.eventCounter-1;i>=0;i--) {
			ProcessEvent ev = this.events.get(i);
			if(nodes[i] == null && rowOfEvent[i] == -1) {
				rowOfEvent[i] = 0;
				elementsInRow[rowOfEvent[i]] += 1;
				points[i] = lang.newPoint(new Offset(elementsInRow[rowOfEvent[i]] * 100 + (rowOfEvent[i] % 2) * (-50), 50, this.currActivity.getName(), "SW"), "P"+i, null, pp);
				nodes[i] = points[i].getCoords();
			}
			labels[i] = this.events.get(i).nameLabel.getText();
			for(int j=0;j<this.eventCounter;j++) {
				boolean noEventBetween = true;
				ProcessEvent other = this.events.get(j);
				if(i == j || !happenedBefore(processes, other, ev)) {
					continue;
				}
				else {
					hapBef[j][i] = true;
					for(int c=0;c<this.eventCounter;c++) {
						ProcessEvent tmp = this.events.get(c);
						if(!(c==i) && !(c==j) && happenedBefore(processes, tmp, ev) && happenedBefore(processes, other, tmp)) {
							noEventBetween = false;
							break;
						}
					}
				}
				if(noEventBetween) {
					adj[j][i] = 1;
					if(!(rowOfEvent[j] == rowOfEvent[i]+1) && !(rowOfEvent[j] - rowOfEvent[i] >= 2)) {
						rowOfEvent[j] = rowOfEvent[i]+1;
						points[j] = lang.newPoint(new Offset(elementsInRow[rowOfEvent[j]]*100 + (rowOfEvent[j] % 2) * (-50), rowOfEvent[j]*100, "P"+(this.eventCounter-1), "C"), "P"+j, null, pp);
						elementsInRow[rowOfEvent[j]] += 1;
						nodes[j] = points[j].getCoords();
					}
				}
			}
		}
		
		String lteTrueString = this.translator.translateMessage("comparisonSuccessful")+"? -> ";
		String ltTrueString = this.translator.translateMessage("strictlySmaller")+"? -> ";
		TextProperties tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		Text lteText = lang.newText(new Offset(0, 8, this.foundEventsLabel.getName(), "SW"), "", "lteText", null, tp);
		Text ltText = lang.newText(new Offset(0, 2, "lteText", "SW"), "", "ltText", null, tp);
		Text lteTrueText = lang.newText(new Offset(0, 2, "ltText", "SW"), lteTrueString, "lteTrueText", null, tp);
		Text ltTrueText = lang.newText(new Offset(0, 2, "lteTrueText", "SW"), ltTrueString, "ltTrueText", null, tp);
		ltTrueText.hide();
		lteTrueText.hide();
	    this.var = lang.newVariables();
		this.var.declare("String", "Name", "Value", "Role");
		for(ProcessEvent ev : this.eventsToCheck) {
			ArrayList<ProcessEvent> happenedBeforeEvents = new ArrayList<ProcessEvent>();
			String happenedBeforeEventsString = "";
			
			currEvent.setText(ev.nameLabel.getText(), null, null);
			currEvent.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, HighlightColor, null, null);
			ev.nameLabel.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, HighlightColor, null, null);
			ev.circle.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, HighlightColor, null, null);
			
			for(ProcessEvent other : this.events) {
				if(ev.equals(other))
					continue;
				
				otherEvent.setText(other.nameLabel.getText(), null, null);
				otherEvent.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, HighlightColor, null, null);
				other.nameLabel.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, HighlightColor, null, null);
				other.circle.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, HighlightColor, null, null);
				sc2.unhighlight(1);
				sc2.unhighlight(6);
				lteTrueText.setText(lteTrueString + this.translator.translateMessage("false"), null, null);
			    this.var.declare("String", "allOtherElementsLessThanOrEqual", "false", "Temporary");
				ltTrueText.setText(ltTrueString + this.translator.translateMessage("false"), null, null);
			    this.var.declare("String", "oneOtherElementStrictlySmaller", "false", "Temporary");
				ltTrueText.show();
				lteTrueText.show();
				
				if(this.rand.nextInt(100) < this.QuestionLikelihood) {
					boolean answer = hapBef[other.id][ev.id];
					TrueFalseQuestionModel q = new TrueFalseQuestionModel(VectorClock.QUESTIONS_EVENT_HAPPENED_BEFORE+other.nameLabel.getText()+ev.nameLabel.getText());
					q.setPrompt(this.translator.translateMessage("qEvHappenedBefore1", new Object[] {other.nameLabel.getText(), ev.nameLabel.getText()}));
					q.setPointsPossible(1);
					q.setCorrectAnswer(answer);
					q.setFeedbackForAnswer(answer, this.translator.translateMessage("correctAnswer"));
					q.setFeedbackForAnswer(!answer, this.translator.translateMessage("wrongAnswer"));
					q.setGroupID(VectorClock.QUESTIONS_EVENT_HAPPENED_BEFORE);
					lang.addTFQuestion(q);
				}
				
				lang.nextStep();

				currEvent.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
				otherEvent.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
				
				sc2.highlight(0, 0, false);
				for(int i=0; i<ev.computedClockVectorGrid.getNrRows();i++)
					ev.computedClockVectorGrid.highlightCell(i, 0, null, null);
				for(int i=0; i<other.computedClockVectorGrid.getNrRows();i++)
					other.computedClockVectorGrid.highlightCell(i, 0, null, null);
				lang.nextStep();
				
				for(int i=0; i<ev.computedClockVectorGrid.getNrRows();i++)
					ev.computedClockVectorGrid.unhighlightCell(i, 0, null, null);
				for(int i=0; i<other.computedClockVectorGrid.getNrRows();i++)
					other.computedClockVectorGrid.unhighlightCell(i, 0, null, null);
				
				boolean strictlySmaller = false;
				boolean happenedBefore = true;
				for(int i=0;i<processes.length;i++) {
					String lteString = ""+other.computedClockVectorGrid.getElement(i, 0)+" \u2264 "+ev.computedClockVectorGrid.getElement(i, 0)+"?";
					String ltString = ""+other.computedClockVectorGrid.getElement(i, 0)+" < "+ev.computedClockVectorGrid.getElement(i, 0)+"?";
					
					ev.computedClockVectorGrid.highlightCell(i, 0, null, null);
					other.computedClockVectorGrid.highlightCell(i, 0, null, null);
					sc2.highlight(0, 0, true);
					sc2.highlight(1, 0, false);
					lteText.setText(lteString, null, null);
					ltText.setText(ltString, null, null);
					ltText.show();
					lteText.show();
					
					ev.computedClockVectorGrid.highlightElem(i, 0, null, null);
					other.computedClockVectorGrid.highlightElem(i, 0, null, null);
					lang.nextStep();
					
					int value = ev.computedClockVector.get(processes[i]);
					int otherValue = other.computedClockVector.get(processes[i]);
					if(otherValue < value) {
						ltText.setText(ltString + " -> "+this.translator.translateMessage("true"), null, null);
						if(!strictlySmaller) {
							strictlySmaller = true;
							ltTrueText.setText(ltTrueString + this.translator.translateMessage("true"), null, null);
						    this.var.set("oneOtherElementStrictlySmaller", "true");
						}
					}
					else if(!(otherValue < value)) {
						ltText.setText(ltString + " -> "+this.translator.translateMessage("false"), null, null);
					}
					if(!(otherValue <= value)) {
						happenedBefore = false;

						lteText.setText(lteString+ " -> "+this.translator.translateMessage("false"), null, null);
						
						sc2.highlight(1, 0, true);
						sc2.highlight(3, 0, false);
						sc2.highlight(4, 0, false);
						lang.nextStep(ev.nameLabel.getText() + " "+this.translator.translateMessage("compareWith")+" " + other.nameLabel.getText());
						sc2.unhighlight(1);
						sc2.unhighlight(3);
						sc2.unhighlight(4);
						ev.computedClockVectorGrid.unhighlightCell(i, 0, null, null);
						other.computedClockVectorGrid.unhighlightCell(i, 0, null, null);
						ev.computedClockVectorGrid.unhighlightElem(i, 0, null, null);
						other.computedClockVectorGrid.unhighlightElem(i, 0, null, null);

						this.var.discard("oneOtherElementStrictlySmaller");
						this.var.discard("allOtherElementsLessThanOrEqual");
						
						break;
					}
					else {
						lteText.setText(lteString+ " -> "+this.translator.translateMessage("true"), null, null);
						
						sc2.highlight(1, 0, true);
						sc2.highlight(2, 0, false);
						lang.nextStep();
						sc2.unhighlight(1);
						sc2.unhighlight(2);
					}
					
					ltText.hide();
					lteText.hide();
					ev.computedClockVectorGrid.unhighlightCell(i, 0, null, null);
					other.computedClockVectorGrid.unhighlightCell(i, 0, null, null);
					ev.computedClockVectorGrid.unhighlightElem(i, 0, null, null);
					other.computedClockVectorGrid.unhighlightElem(i, 0, null, null);
				}

				sc2.unhighlight(0);
				if(happenedBefore && strictlySmaller) {
					happenedBeforeEvents.add(other);
					if(happenedBeforeEvents.size() > 1)
						happenedBeforeEventsString += ", ";
					happenedBeforeEventsString += other.nameLabel.getText();

					lteTrueText.setText(lteTrueString + this.translator.translateMessage("true"), null, null);
				    this.var.set("allOtherElementsLessThanOrEqual", "true");
					sc2.highlight(5, 0, false);
					sc2.highlight(6, 0, false);
					lang.nextStep();
					
					sc2.highlight(5, 0, true);
					sc2.highlight(6, 0, true);
					sc2.highlight(7, 0, false);
					foundEvents.setText(happenedBeforeEventsString, null, null);
					other.circle.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, HighlightColor, null, null);
					lang.nextStep(ev.nameLabel.getText() + " "+this.translator.translateMessage("compareWith")+" " + other.nameLabel.getText());

					this.var.discard("oneOtherElementStrictlySmaller");
					this.var.discard("allOtherElementsLessThanOrEqual");

					sc2.unhighlight(5);
					sc2.unhighlight(6);
					sc2.unhighlight(7);
				}
				other.nameLabel.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
				other.circle.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLACK, null, null);
				ltText.setText("", null, null);
				lteText.setText("", null, null);
				ltTrueText.hide();
				lteTrueText.hide();
				otherEvent.setText(this.translator.translateMessage("noElement"), null, null);
			}
			this.var.discard("Name"); // not removed during animation, ANIMAL bug? serves as placeholder to make sure the other two variables are discarded
			sc2.unhighlight(1);
			foundEvents.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, HighlightColor, null, null);
			lang.nextStep(this.translator.translateMessage("happenedBefore")+" "+ev.nameLabel.getText());

			foundEvents.setText("", null, null);
			foundEvents.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
			ev.nameLabel.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
			ev.circle.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLACK, null, null);
			for(ProcessEvent pe : happenedBeforeEvents)
				pe.circle.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
		}
		currEvent.setText(this.translator.translateMessage("noElement"), null, null);
		otherEvent.setText(this.translator.translateMessage("noElement"), null, null);
		sc2.unhighlight(1);
		sc2.unhighlight(6);
		ltText.hide();
		lteText.hide();
		ltTrueText.hide();
		lteTrueText.hide();
		lang.nextStep();
		
	    this.currActivity.setText(this.translator.translateMessage("currentActivity3"), null, null);
		this.currEventLabel.hide();
		currEvent.hide();
		this.otherEventLabel.hide();
		otherEvent.hide();
	    sc2.hide();
	    this.foundEventsLabel.hide();
		GraphProperties gp = new GraphProperties();
		gp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		gp.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, true);
		Graph g = lang.newGraph("graph", adj, nodes, labels, null, gp);
		lang.nextStep(this.translator.translateMessage("eventOrder"));
		g.hide();
	}
	
	private boolean happenedBefore(String[] processes, ProcessEvent other, ProcessEvent ev) {
		boolean strictlySmaller = false;
		boolean happenedBefore = true;
		for(int i=0;i<processes.length;i++) {
			int value = ev.computedClockVector.get(processes[i]);
			int otherValue = other.computedClockVector.get(processes[i]);
			if(!strictlySmaller && otherValue < value)
				strictlySmaller = true;
			if(!(otherValue <= value)) {
				happenedBefore = false;
				break;
			}
		}
		return happenedBefore && strictlySmaller;
	}
	
	private void createTimestampQuestion(Process p, ProcessEvent ev) {
		if(this.rand.nextInt(100) < this.QuestionLikelihood) {
			String correctAnswer = "";
			for(int i=0;i<this.Processes.length;i++) {
				correctAnswer += p.clock.get(this.Processes[i]);
				if(i<this.Processes.length-1)
					correctAnswer += ",";
			}
			
			FillInBlanksQuestionModel q = new FillInBlanksQuestionModel(VectorClock.QUESTIONS_EVENT_TIMESTAMP+ev.nameLabel.getText());
		    q.setPrompt(this.translator.translateMessage("qEvTimestamp1", new Object[] {ev.nameLabel.getText()}));
		    q.addAnswer("correct", correctAnswer, 1, this.translator.translateMessage("correctAnswer"));
		    q.setGroupID(VectorClock.QUESTIONS_EVENT_TIMESTAMP);
		    lang.addFIBQuestion(q);
	    }
		return;
	}

    public void init(){
        lang = new AnimalScript("Vector Clock", "Sven Dotzauer-Klier, Gregor Heß", 800, 600);
		lang.setStepMode(true);
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    }

	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		
		// check for duplicate processes
		boolean processesCorrect = true;
		String[] ps = (String[])primitives.get("Processes");
        for(int i=0;i<ps.length;i++) {
        	for(int j=0;j<ps.length;j++) {
        		if(i!=j && ps[i].equals(ps[j])) {
        			processesCorrect = false;
        			throw new IllegalArgumentException(this.translator.translateMessage("processException"));
        		}
        	}
        	if(!processesCorrect)
        		break;
        }
        
    	boolean eventsCorrect = true;
    	int maxTime = 0;
        if(processesCorrect) {
        	String[][] evs = (String[][])primitives.get("Events");
        	for(String[] ev : evs) {
        		// check at least 6 columns
        		if(!(ev.length >= 6)) {
        			eventsCorrect = false;
        			throw new IllegalArgumentException(this.translator.translateMessage("eventElementsException"));
        		}
        		// check event type correct
        		if(!(ev[2].matches("I|i|M|m"))) {
        			eventsCorrect = false;
        			throw new IllegalArgumentException(this.translator.translateMessage("eventTypeException"));
        		}
        		else {
        			// check time not below 0
        			if(Integer.parseInt(ev[0]) < 0) {
            			eventsCorrect = false;
            			throw new IllegalArgumentException(this.translator.translateMessage("eventTimeBelowZeroException"));
        			}
        			else
        				maxTime = Math.max(maxTime, Integer.parseInt(ev[0]));
        			// check process name exists
        			if(!Arrays.asList(ps).contains(ev[1])) {
            			eventsCorrect = false;
            			throw new IllegalArgumentException(this.translator.translateMessage("eventProcessException"));
        			}
            		if(ev[2].matches("M|m")) {
            			// check message received after sent
            			if(Integer.parseInt(ev[4]) <= Integer.parseInt(ev[0])) {
	            			eventsCorrect = false;
	            			throw new IllegalArgumentException(this.translator.translateMessage("eventMessageException"));
            			}
            			else
            				maxTime = Math.max(maxTime, Integer.parseInt(ev[0]));
            			// check process name exists
            			if(!Arrays.asList(ps).contains(ev[3])) {
                			eventsCorrect = false;
                			throw new IllegalArgumentException(this.translator.translateMessage("eventReceiverException"));
            			}
            		}
        		}
        	if(!eventsCorrect)
        		break;
        	}
    		if(maxTime == 0) {
    			eventsCorrect = false;
    			throw new IllegalArgumentException(this.translator.translateMessage("eventMaxTimeNotAboveZeroException"));
    		}
        }
		return processesCorrect && eventsCorrect;
	}

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        Events = (String[][])primitives.get("Events");
        Processes = (String[])primitives.get("Processes");
        HighlightColor = (Color)primitives.get("HighlightColor");
        SkipDescription = (boolean)primitives.get("SkipDescription");
        ClockVectorProperties = (MatrixProperties)props.getPropertiesByName("ClockVectorProperties");
        SourceCodeProperties = (SourceCodeProperties)props.getPropertiesByName("SourceCodeProperties");
        QuestionLikelihood = (Integer)primitives.get("QuestionLikelihood");
        TimelineLength = (Integer)primitives.get("TimelineLength");

        rand = new Random(System.currentTimeMillis());
    	eventCounter = 0;
		doVectorClock();
		lang.finalizeGeneration();
        return lang.toString();
    }

    public String getName() {
        return "Vector Clock";
    }

    public String getAlgorithmName() {
        return "Vector Clock";
    }

    public String getAnimationAuthor() {
        return "Sven Dotzauer-Klier, Gregor Heß";
    }

    public String getDescription(){
    	return translator.translateMessage("descriptionPart1")+translator.translateMessage("descriptionPart2")+translator.translateMessage("descriptionPart3");
    }

    public String getCodeExample(){
    	return 
    			translator.translateMessage("initialization")
    			+"\n"
    			+translator.translateMessage("codeExampleInitialization")
    			+"\n\n"
    			+translator.translateMessage("currentActivity1")
    			+"\n"
    			+translator.translateMessage("codeExampleTimestamps")
    			+"\n\n"
    			+translator.translateMessage("currentActivity2")
    			+"\n"
    			+translator.translateMessage("codeExampleComparison");
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return this.locale;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_NETWORK);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

    public VectorClock(String path, Locale locale) {
    	this.locale = locale;
    	this.translator = new Translator(path, locale);
    }
}