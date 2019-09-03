/*
 * MultilevelQueue.java
 * Andre Challier <andre.challier@stud.tu-darmstadt.de>, Christian Richter <chrisrichter145@gmail.com>, 2017 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.MultipleSelectionQuestionModel;
import interactionsupport.models.QuestionGroupModel;
import translator.Translator;
import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.TwoValueView;

import java.awt.Color;
import java.awt.Font;

import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CounterProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class MultilevelQueue implements ValidatingGenerator {
	
	private String path;
	private Locale loc;
    private Language lang;
    private Color highlightColor;
    private ArrayProperties arg_queueProperties;
    private String[] arg_queues;
    private String[][] arg_processes;
    private MatrixProperties arg_processProperties;
    
    private ArrayMarkerProperties headMarkerProperties;
    private ArrayMarkerProperties tailMarkerProperties;
    
    private Translator trl;
    private Variables vars;

	/**
	 * The List of queues to schedule.
	 * 
	 * queues.get(0) is list with highest priority, queues.get(1) is the next
	 * highest and so on.
	 */
	private List<Queue> queues;
	
	/**
	 * The list of incoming processes that has to be scheduled.
	 */
	private List<Process> inc_procs;
	
	/**
	 * Pseudocode SourceCode
	 */
	private SourceCode sc;

	/**
	 * Title showing on every page
	 */
	private Text title;
	
	/**
	 * The current timeslice of schedulting
	 */
	private int currentTime;
	
	/**
	 * The animal primitive showing the current timeslice of scheduling
	 */
	private Text currentTimeText;
	
	/**
	 * The animal primitive showing the current execution hint
	 */
	private Text hint;
	
	/**
	 * The Array holding all displayed queues
	 */
	StringArray[] queueViews;
	
	/**
	 * The Array holding all displayed queueNames
	 */
	Text[] queueNames;
	
	/**
	 * The Array holding counters for all queues
	 */
	TwoValueCounter[] queueCounters;
	
	/**
	 * The Array holding the views for all queue-counters
	 */
	TwoValueView[] queueCounterViews;
	
	/**
	 * The Matrix showing all incoming Processes
	 */
	StringMatrix processMatrix;
	
	/*
	 * QuestionGroups 
	 */
	
	QuestionGroupModel scheduleQuestions;
	QuestionGroupModel nextQueueQuestions;
	QuestionGroupModel enqueueQuestions;
	QuestionGroupModel rescheduleQuestions;
	
	/*
	 * Fields for Summary
	 */
	String schedulingOrder;
	int computingSteps;
	int idlingSteps;
	
	private static Font defaultFont =new Font(Font.SANS_SERIF, Font.PLAIN, 12);
	private static Font highlightFont =new Font(Font.SANS_SERIF, Font.BOLD, 12);
	
	/**
	 * default duration for swap processes
	 */
	public final static Timing defaultDuration = new TicksTiming(30);
	
	/*
	 * Constants for animation
	 */
	private static final int DESCRIPTION_LENGTH = 12;
	private static final int QUEUES_MAX = 10;
	private static final int PROCESSES_MAX = 10;
	private static final int WORK_MAX = 100;
	private static final int ARRIVAL_MAX = 100;
	
	/*
	 * Constant Strings for initialisation
	 */
	private static final String INIT_TITLE = "Multi-Level Queue";
	private static final String INIT_AUTHOR = "Andre Challier <andre.challier@stud.tu-darmstadt.de>, Christian Richter <chrisrichter145@gmail.com>";
	private static final String EMPTY_QUEUE_FIELD = " ";
	private static final String SCHEDULING_ORDER_IDLE = "-";
	private static final String FILE_EXTENSION= "asu";
	
	/*
	 * Constant Strings for property keys
	 */
	private static final String PROP_TABLE = "table";
	
	/*
	 * Constant Strings for variable Types
	 */
	private static final String VAR_TYPE_STRING = "String";
	private static final String VAR_TYPE_INT = "int";
	/*
	 * Constant Strings for argument parameters
	 */
	private static final String ARG_KEY_QUEUES = "Queues";
	private static final String ARG_KEY_QUEUE_STRAT_RR = "RR";
	private static final String ARG_KEY_QUEUE_STRAT_FIFO = "FIFO";
	private static final String ARG_KEY_PROCESSES = "Processes";
	private static final String ARG_KEY_HIGHLIGHTCOLOR = "HighlightColor";
	private static final String ARG_KEY_QUEUETABLE = "QueueTable";
	private static final String ARG_KEY_PROCESSTABLE = "ProcessTable";
	
	/*
	 * Constant Strings for names of primitives
	 */
	private static final String NAME_TITLE = "title";
	private static final String NAME_DESCRIPTION_LN = "description_ln_";
	private static final String NAME_SOURCECODE = "source_code";
	private static final String NAME_CURRENTTIME = "current_time";
	private static final String NAME_HINT = "hint";
	private static final String NAME_HEAD_MARKER = "head_marker";
	private static final String NAME_TAIL_MARKER = "tail_marker";
	private static final String NAME_QG_SCHEDULE_QUESTIONS = "schedule_questions";
	private static final String NAME_QG_RESCHEDULE_QUESTIONS = "reschedule_questions";
	private static final String NAME_QG_ENQUEUE_QUESTIONS = "enqueue_questions";
	private static final String NAME_QG_NEXT_QUEUE_QUESTIONS = "nextqueue_questions";
	private static final String NAME_SCHEDULE_QUESTION = "schedule_question";
	private static final String NAME_RESCHEDULE_QUESTION = "reschedule_question";
	private static final String NAME_ENQUEUE_QUESTION = "enqueue_question";
	private static final String NAME_NEXT_QUEUE_QUESTION = "nextqueue_question";
	private static final String NAME_SUM_LN = "summary_ln_";
	private static final String NAME_QUEUETEXT = "queuetext_";
	private static final String NAME_QUEUE = "queue_";
	private static final String NAME_INCOMING_PROCESSES = "inc_procs";
	
	/*
	 * Constant Strings for translation keys
	 */
	private static final String TRL_TITLE = "TITLE";
	private static final String TRL_DESCRIPTION = "DESCRIPTION";
	private static final String TRL_SOURCE_CODE = "SOURCECODE";
	private static final String TRL_DESCRIPTION_LINE_ = "DESCRIPTION_LN_";
	private static final String TRL_CURRENTTIME = "CURRENTTIME";
	private static final String TRL_ID = "ID";
	private static final String TRL_QUEUE = "QUEUE";
	private static final String TRL_WORK = "WORK";
	private static final String TRL_TIME = "TIME";
	private static final String TRL_HEAD_MARKER = "HEAD_MARKER";
	private static final String TRL_TAIL_MARKER = "TAIL_MARKER";
	private static final String TRL_QUEUETYPE_RR = "QUEUETYPE_RR";
	private static final String TRL_QUEUETYPE_FIFO = "QUEUETYPE_FIFO";
	private static final String TRL_QUEUENAME = "QUEUENAME";
	
	// SECTIONS
	private static final String TRL_SECTION_DESCRIPTION = "SECTION_DESCRIPTION";
	private static final String TRL_SECTION_ITERATION = "SECTION_ITERATION";
	private static final String TRL_SECTION_CONCLUSION = "SECTION_CONCLUSION";
	
	// VARIABLES
	private static final String TRL_VAR_KEY_PROCESS = "VAR_KEY_PROCESS";
	private static final String TRL_VAR_KEY_DUE = "VAR_KEY_DUE";
	private static final String TRL_VAR_KEY_QUEUE = "VAR_KEY_QUEUE";
	private static final String TRL_VAR_KEY_I = "VAR_KEY_I";
	
	// HINT MESSAGES
	private static final String TRL_HINT_REMAINING_WORK = "HINT_REMAINING_WORK";
	private static final String TRL_HINT = "HINT";
	private static final String TRL_HINT_INCOMING_PROCESS = "HINT_INCOMING_PROCESS";
	private static final String TRL_HINT_CHECK_QUEUE = "HINT_CHECK_QUEUE";
	private static final String TRL_HINT_QUEUE_HAS_PENDING_WORK = "HINT_QUEUE_HAS_PENDING_WORK";
	private static final String TRL_HINT_CHECK_FOR_QUEUE_TO_SCHEDULE = "HINT_CHECK_FOR_QUEUE_TO_SCHEDULE";
	private static final String TRL_HINT_NO_QUEUE_HAS_PENDING_WORK = "HINT_NO_QUEUE_HAS_PENDING_WORK";
	private static final String TRL_HINT_SCHEDULE_PROCESS_FROM_QUEUE = "HINT_SCHEDULE_PROCESS_FROM_QUEUE";
	private static final String TRL_HINT_CHECK_IF_WORK_LEFT = "HINT_CHECK_IF_WORK_LEFT";
	private static final String TRL_HINT_PROCESS_HAS_NO_WORK_LEFT_REMOVE = "HINT_PROCESS_HAS_NO_WORK_LEFT_REMOVE";
	private static final String TRL_HINT_PROCESS_HAS_WORK_LEFT_RESCHEDULE = "HINT_PROCESS_HAS_WORK_LEFT_RESCHEDULE";
	private static final String TRL_HINT_TEMP_ROUND_ROBIN = "HINT_TEMP_ROUND_ROBIN";
	private static final String TRL_HINT_NO_PROCESS_HAS_PENDING_WORK = "HINT_NO_PROCESS_HAS_PENDING_WORK";
	private static final String TRL_HINT_REMOVE_PROCESS_FROM_QUEUE = "HINT_REMOVE_PROCESS_FROM_QUEUE";
	private static final String TRL_HINT_ADD_PROCESS_TO_QUEUE = "HINT_ADD_PROCESS_TO_QUEUE";
	private static final String TRL_HINT_SEARCH_FOR_QUEUE_TO_SCHEDULE = "HINT_SEARCH_FOR_QUEUE_TO_SCHEDULE";
	private static final String TRL_HINT_PROCESS_STARTS_NOW = "HINT_PROCESS_STARTS_NOW";
	
	// SUMMARY MESSAGES
	private static final String TRL_SUM_MLQ_TIMESLOTS = "SUM_MLQ_TIMESLOTS";
	private static final String TRL_SUM_PROC_IN_QUEUES = "SUM_PROC_IN_QUEUES";
	private static final String TRL_SUM_WORK_TIME = "SUM_WORK_TIME";
	private static final String TRL_SUM_IDLE_TIME = "SUM_IDLE_TIME";
	private static final String TRL_SUM_SCHEDULING_ORDER = "SUM_SCHEDULING_ORDER";
	
	// VALIDATION MESSAGES
	private static final String TRL_VAL_INV_QUEUES = "VAL_NO_QUEUES";
	private static final String TRL_VAL_INV_PROCESSES = "VAL_NO_PROCESSES";
	private static final String TRL_VAL_INV_FIELDS = "VAL_INV_FIELDS";
	private static final String TRL_VAL_NO_SCHED = "VAL_NO_SCHED";
	private static final String TRL_VAL_INV_SCHED = "VAL_INV_SCHED";
	private static final String TRL_VAL_EMPTY_NAME = "VAL_EMPTY_NAME";
	private static final String TRL_VAL_PROC_WO_QUEUE = "VAL_PROC_WO_QUEUE";
	private static final String TRL_VAL_INV_QUEUE_ID = "VAL_INV_QUEUE_ID";
	private static final String TRL_VAL_MALF_QUEUE_ID = "VAL_MALF_QUEUE_ID";
	private static final String TRL_VAL_PROC_WO_WORK = "VAL_PROC_WO_WORK";
	private static final String TRL_VAL_MALF_WORK = "VAL_MALF_WORK";
	private static final String TRL_VAL_PROC_WO_ARRIVAL = "VAL_PROC_WO_ARRIVAL";
	private static final String TRL_VAL_MALF_ARRIVAL = "VAL_MALF_ARRIVAL";
	
	// QUESTION MESSAGES
	private static final String TRL_QST_SCHED = "QST_SCHED";
	private static final String TRL_ANS_SCHED_NONE = "ANS_SCHED_NONE";
	private static final String TRL_FB_SCHED_RIGHT = "FB_SCHED_RIGHT";
	private static final String TRL_FB_SCHED_WRONG = "FB_SCHED_WRONG";

	private static final String TRL_QST_ENQ = "QST_ENQ";
	private static final String TRL_ANS_ENQ_NONE = "ANS_ENQ_NONE";
	private static final String TRL_FB_ENQ_RIGHT_ARRIVAL = "FB_ENQ_RIGHT_ARRIVAL";
	private static final String TRL_FB_ENQ_WRONG_ARRIVAL = "FB_ENQ_WRONG_ARRIVAL";
	private static final String TRL_FB_ENQ_RIGHT_NONE = "FB_ENQ_RIGHT_NONE";
	private static final String TRL_FB_ENQ_WRONG_NONE = "FB_ENQ_WRONG_NONE";
	
	private static final String TRL_QST_NQ = "QST_NQ";
	private static final String TRL_ANS_NQ_NONE = "ANS_NQ_NONE";
	private static final String TRL_FB_NQ_WRONG_NOT_FIRST = "FB_NQ_WRONG_NOT_FIRST";
	private static final String TRL_FB_NQ_WRONG_EMPTY = "FB_NQ_WRONG_EMPTY";
	private static final String TRL_FB_NQ_RIGHT_QUEUE = "FB_NQ_RIGHT_QUEUE";
	private static final String TRL_FB_NQ_RIGHT_NONE = "FB_NQ_RIGHT_NONE";
	private static final String TRL_FB_NQ_WRONG_NONE = "FB_NQ_WRONG_NONE";

	private static final String TRL_QST_RSCHED = "QST_RSCHED";
	private static final String TRL_ANS_RSCHED_STAY = "ANS_RSCHED_STAY";
	private static final String TRL_ANS_RSCHED_RE = "ANS_RSCHED_RE";
	private static final String TRL_ANS_RSCHED_REM = "ANS_RSCHED_REM";
	private static final String TRL_FB_RSCHED_RIGHT_NO_WORK = "FB_RSCHED_RIGHT_NO_WORK";
	private static final String TRL_FB_RSCHED_WRONG_NO_WORK = "FB_RSCHED_WRONG_NO_WORK";
	private static final String TRL_FB_RSCHED_WRONG_WORK_LEFT = "FB_RSCHED_WRONG_WORK_LEFT";
	private static final String TRL_FB_RSCHED_RIGHT_ONLY_ONE = "FB_RSCHED_RIGHT_ONLY_ONE";
	private static final String TRL_FB_RSCHED_WRONG_ONLY_ONE = "FB_RSCHED_WRONG_ONLY_ONE";
	private static final String TRL_FB_RSCHED_WRONG_RR = "FB_RSCHED_WRONG_RR";
	private static final String TRL_FB_RSCHED_RIGHT_RR = "FB_RSCHED_RIGHT_RR";
	private static final String TRL_FB_RSCHED_RIGHT_FIFO = "FB_RSCHED_RIGHT_FIFO";
	private static final String TRL_FB_RSCHED_WRONG_FIFO = "FB_RSCHED_WRONG_FIFO";
	
	
	
	
	public MultilevelQueue(String path, Locale loc) {
		this.path = path;
		this.loc = loc;
		trl = new Translator(path, loc);
	}

    public void init(){
        lang = new AnimalScript(INIT_TITLE, INIT_AUTHOR, 800, 600);
        lang.setStepMode(true);
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
        vars = lang.newVariables();
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        
    	arg_queues = (String[])primitives.get(ARG_KEY_QUEUES);
        arg_processes = (String[][])primitives.get(ARG_KEY_PROCESSES);
    	
    	highlightColor = (Color)primitives.get(ARG_KEY_HIGHLIGHTCOLOR);
        arg_queueProperties = (ArrayProperties)props.getPropertiesByName(ARG_KEY_QUEUETABLE);
        arg_processProperties = (MatrixProperties)props.getPropertiesByName(ARG_KEY_PROCESSTABLE);
        
		arg_processProperties.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, PROP_TABLE);
		headMarkerProperties = new ArrayMarkerProperties();
		tailMarkerProperties = new ArrayMarkerProperties();
		headMarkerProperties.set(AnimationPropertiesKeys.LABEL_PROPERTY, trl.translateMessage(TRL_HEAD_MARKER));
		tailMarkerProperties.set(AnimationPropertiesKeys.LABEL_PROPERTY, trl.translateMessage(TRL_TAIL_MARKER));
		tailMarkerProperties.set(AnimationPropertiesKeys.SHORT_MARKER_PROPERTY, true);
        
        queues = new LinkedList<Queue>();
        inc_procs = new LinkedList<Process>();
        
        // set initial variables
        
        schedulingOrder = new String();
        computingSteps = 0;
        idlingSteps = 0;
        currentTime = 0;
        
        // draw description
        
        TextProperties titleProps = new TextProperties();
		titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		title = lang.newText(new Coordinates(30,30), trl.translateMessage(TRL_TITLE), NAME_TITLE, null, titleProps);
		
		Text[] description = new Text[DESCRIPTION_LENGTH];
		description[0] = lang.newText(new Offset(0, 20, title, AnimalScript.DIRECTION_SW), trl.translateMessage(TRL_DESCRIPTION_LINE_ + 1), NAME_DESCRIPTION_LN + 1, null );
		for(int i = 1; i < DESCRIPTION_LENGTH; i++) {
			description[i] =lang.newText(new Offset(0, 5, description[i-1], AnimalScript.DIRECTION_SW), trl.translateMessage(TRL_DESCRIPTION_LINE_ + (i+1)), NAME_DESCRIPTION_LN + (i+1), null );
		}
        
        lang.nextStep(trl.translateMessage(TRL_SECTION_DESCRIPTION));
        
        //hide description
        for(Text t : description) {
        	t.hide();
        }
		
		// first, set the visual properties for the source code
		SourceCodeProperties scProps = new SourceCodeProperties();
		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.GRAY);
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GRAY);
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.MONOSPACED, Font.PLAIN, 12));
		
		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, highlightColor);
		
		// now, create the source code entity
		sc = lang.newSourceCode(new Offset(0, 20, title, AnimalScript.DIRECTION_SW), NAME_SOURCECODE, null, scProps);
		
		sc.addCodeLine(trl.translateMessage("SRC_0"), null, 0, null);	// line 0
		sc.addCodeLine(trl.translateMessage("SRC_1"), null, 1, null);	// line 1
		sc.addCodeLine(trl.translateMessage("SRC_2"), null, 2, null);	// line 2
		sc.addCodeLine(trl.translateMessage("SRC_3"), null, 2, null);	// line 3
		sc.addCodeLine(trl.translateMessage("SRC_4"), null, 1, null);	// line 4
		sc.addCodeLine(trl.translateMessage("SRC_5"), null, 2, null);	// line 5
		sc.addCodeLine(trl.translateMessage("SRC_6"), null, 3, null);	// line 6
		sc.addCodeLine(trl.translateMessage("SRC_7"), null, 3, null);	// line 7
		sc.addCodeLine(trl.translateMessage("SRC_8"), null, 1, null);	// line 8
		sc.addCodeLine(trl.translateMessage("SRC_9"), null, 2, null); 	// line 9
		sc.addCodeLine(trl.translateMessage("SRC_10"), null, 2, null);	// line 10
		sc.addCodeLine(trl.translateMessage("SRC_11"), null, 1, null);	// line 11
		sc.addCodeLine(trl.translateMessage("SRC_12"), null, 1, null);	// line 12
		sc.addCodeLine(trl.translateMessage("SRC_13"), null, 2, null);	// line 13
		sc.addCodeLine(trl.translateMessage("SRC_14"), null, 1, null);	// line 14
		sc.addCodeLine(trl.translateMessage("SRC_15"), null, 2, null);	// line 15
		sc.addCodeLine(trl.translateMessage("SRC_16"), null, 3, null);	// line 16
		sc.addCodeLine(trl.translateMessage("SRC_17"), null, 3, null);	// line 17
		sc.addCodeLine(trl.translateMessage("SRC_18"), null, 3, null);	// line 18
		
		// init time view
		TextProperties tp = new TextProperties();
		currentTimeText = lang.newText(new Offset(20, 0, sc, AnimalScript.DIRECTION_NE), trl.translateMessage(TRL_CURRENTTIME, Integer.toString(currentTime)), NAME_CURRENTTIME, null, tp);
		
		// init processes and process views
		String[][] procMatrix = new String[arg_processes.length + 1][4];
		procMatrix[0][0] = trl.translateMessage(TRL_ID);
		procMatrix[0][1] = trl.translateMessage(TRL_QUEUE);
		procMatrix[0][2] = trl.translateMessage(TRL_WORK);
		procMatrix[0][3] = trl.translateMessage(TRL_TIME);
		
		for(int i = 0; i < arg_processes.length; i++) {
			String[] arg_process = arg_processes[i];
        	String name = arg_process[0];
        	int queue = Integer.parseInt(arg_process[1]);
        	int work = Integer.parseInt(arg_process[2]);
        	int arrival = Integer.parseInt(arg_process[3]);
        	inc_procs.add(new Process(name, queue, work, arrival, i+1));
        	vars.declare(VAR_TYPE_STRING, trl.translateMessage(TRL_VAR_KEY_PROCESS, name), Integer.toString(work));
        	procMatrix[i + 1][0] = inc_procs.get(i).name;
			procMatrix[i + 1][1] = trl.translateMessage(TRL_QUEUENAME, Integer.toString(inc_procs.get(i).queue));
			procMatrix[i + 1][2] = Integer.toString(inc_procs.get(i).work);
			procMatrix[i + 1][3] = Integer.toString(inc_procs.get(i).arrival);
        }
		
		processMatrix = lang.newStringMatrix(new Offset(0, 20, currentTimeText, AnimalScript.DIRECTION_SW), procMatrix, NAME_INCOMING_PROCESSES, null, arg_processProperties);
		
		// init Hint-Text
		hint = lang.newText(new Offset(20, -30, processMatrix, AnimalScript.DIRECTION_NE), trl.translateMessage(TRL_HINT), NAME_HINT, null, tp);
		
		// init Queues
		queueViews = new StringArray[arg_queues.length];
	    queueNames = new Text[arg_queues.length];
	    queueCounters = new TwoValueCounter[arg_queues.length];
	    queueCounterViews = new TwoValueView[arg_queues.length];
	    TextProperties queueNameTextProp = new TextProperties();
	    queueNameTextProp.set(AnimationPropertiesKeys.FONT_PROPERTY, defaultFont);
	    CounterProperties queueCounterProperties = new CounterProperties();
        
        for(int i = 0; i < arg_queues.length; i++) {
        	// get queue strategy
        	String queueStrat = arg_queues[i];
        	boolean RR;
        	if(queueStrat.equals(ARG_KEY_QUEUE_STRAT_RR)) {
        		RR = true;
        	}else if(queueStrat.equals(ARG_KEY_QUEUE_STRAT_FIFO)) {
        		RR = false;
        	}else {
        		RR = false;
        	}
        	
        	// build queue name
        	String queueName = trl.translateMessage(TRL_QUEUENAME, Integer.toString(i));
        	
        	// build initial empty String Array
        	String[] queueStat = new String[inc_procs.size()];
	    	for(int j = 0; j < inc_procs.size(); j++) {
	    		queueStat[j] = new String(EMPTY_QUEUE_FIELD);
	    	}
        	
        	// find anchor
        	Node anchor;
	    	if(i == 0) {
	    		anchor = new Offset(20, 50, processMatrix, AnimalScript.DIRECTION_NE);
	    	}else {
	    		anchor = new Offset(0, 100, queueNames[i-1], AnimalScript.DIRECTION_SW);
	    	}
	    	
	    	// draw label
	    	queueNames[i] = lang.newText(
	    			anchor, 
	    			RR ? trl.translateMessage(TRL_QUEUETYPE_RR, queueName) : trl.translateMessage(TRL_QUEUETYPE_FIFO, queueName),
	    			NAME_QUEUETEXT + i, 
	    			null, 
	    			queueNameTextProp);
	    	
	    	// draw StringArray
	    	queueViews[i] = lang.newStringArray(
	    			new Offset(20, 0, queueNames[i], AnimalScript.DIRECTION_NE), 
	    			queueStat, 
	    			NAME_QUEUE + i, 
	    			null, 
	    			arg_queueProperties);
	    	
	    	// init access counter
	    	queueCounters[i] = lang.newCounter(queueViews[i]);
	    	
	    	// draw access counter
	    	queueCounterViews[i] = lang.newCounterView(
	    			queueCounters[i], 
	    			new Offset(20, 0, queueViews[i], 
	    			AnimalScript.DIRECTION_NE), 
	    			queueCounterProperties, true, true);
        	queues.add(
        			new Queue(trl.translateMessage(TRL_QUEUENAME, Integer.toString(queues.size())), 
        			RR, 
        			arg_processes.length, 
        			queueViews[i],
        			queueNames[i]));
        	vars.declare(
    				VAR_TYPE_STRING, 
    				trl.translateMessage(TRL_VAR_KEY_QUEUE, Integer.toString(i)), 
    				queues.get(i).toString());
        }
		
		lang.nextStep();
		vars.declare(
				VAR_TYPE_INT, 
				trl.translateMessage(TRL_VAR_KEY_DUE), 
				Integer.toString(-1));
        
		scheduleQuestions = new QuestionGroupModel(NAME_QG_SCHEDULE_QUESTIONS, 3);
        lang.addQuestionGroup(scheduleQuestions);
        
        enqueueQuestions = new QuestionGroupModel(NAME_QG_ENQUEUE_QUESTIONS, 3);
        lang.addQuestionGroup(enqueueQuestions);
        
        nextQueueQuestions = new QuestionGroupModel(NAME_QG_NEXT_QUEUE_QUESTIONS, 3);
        lang.addQuestionGroup(nextQueueQuestions);
        
        rescheduleQuestions = new QuestionGroupModel(NAME_QG_RESCHEDULE_QUESTIONS, 3);
        lang.addQuestionGroup(rescheduleQuestions);
        
		
        schedule();
        summarize();
        
        lang.finalizeGeneration();
        
        return lang.toString();
    }
    
	@Override
	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		arg_queues = (String[])primitives.get(ARG_KEY_QUEUES);
        arg_processes = (String[][])primitives.get(ARG_KEY_PROCESSES);
    	
    	highlightColor = (Color)primitives.get(ARG_KEY_HIGHLIGHTCOLOR);
        arg_queueProperties = (ArrayProperties)props.getPropertiesByName(ARG_KEY_QUEUETABLE);
        arg_processProperties = (MatrixProperties)props.getPropertiesByName(ARG_KEY_PROCESSTABLE);
        
        if(arg_queues == null) {
        	throw new IllegalArgumentException(trl.translateMessage(TRL_VAL_INV_QUEUES, Integer.toString(0), Integer.toString(QUEUES_MAX)));
        }else if(arg_queues.length == 0 || arg_queues.length > QUEUES_MAX){
        	throw new IllegalArgumentException(trl.translateMessage(TRL_VAL_INV_QUEUES, Integer.toString(arg_queues.length), Integer.toString(QUEUES_MAX)));
        }
        
        if(arg_processes == null) {
        	throw new IllegalArgumentException(trl.translateMessage(TRL_VAL_INV_PROCESSES, Integer.toString(0), Integer.toString(PROCESSES_MAX)));
        }else if(arg_processes.length == 0 || arg_processes.length > PROCESSES_MAX){
        	throw new IllegalArgumentException(trl.translateMessage(TRL_VAL_INV_PROCESSES, Integer.toString(arg_processes.length), Integer.toString(PROCESSES_MAX)));
        }
        
        int queuecnt = -1;
        
        for(int i = 0; i < arg_queues.length; i++) {
        	String queue = arg_queues[i];
        	if(queue == null) {
        		throw new IllegalArgumentException(trl.translateMessage(TRL_VAL_NO_SCHED, Integer.toString(i)));
        	}
        	if(!(queue.equals(ARG_KEY_QUEUE_STRAT_FIFO) || queue.equals(ARG_KEY_QUEUE_STRAT_RR))) {
        		throw new IllegalArgumentException(trl.translateMessage(TRL_VAL_INV_SCHED, Integer.toString(i), queue));
        	}
        	queuecnt++;
        }
        
        for(int i = 0; i < arg_processes.length; i++) {
        	String[] curr = arg_processes[i];
        	if(arg_processes[i].length != 4) {
            	throw new IllegalArgumentException(trl.translateMessage(TRL_VAL_INV_FIELDS, Integer.toString(i)));
            }
        	if(curr[0] == null) {
        		throw new IllegalArgumentException(trl.translateMessage(TRL_VAL_EMPTY_NAME, Integer.toString(i)));
        	}
        	
        	if(curr[1] == null) {
        		throw new IllegalArgumentException(trl.translateMessage(TRL_VAL_PROC_WO_QUEUE, curr[0]));
        	}
        	try {
				int queue = Integer.parseInt(curr[1]);
				if(queue < 0 || queue > queuecnt) {
					throw new IllegalArgumentException(trl.translateMessage(TRL_VAL_INV_QUEUE_ID, curr[0], curr[1]));
				}
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException(trl.translateMessage(TRL_VAL_MALF_QUEUE_ID, curr[0], curr[1]));
			}
        	
        	if(curr[2] == null) {
        		throw new IllegalArgumentException(trl.translateMessage(TRL_VAL_PROC_WO_WORK, curr[0]));
        	}
        	try {
				int work = Integer.parseInt(curr[2]);
				if(work < 1 || work > WORK_MAX) {
					throw new IllegalArgumentException(trl.translateMessage(TRL_VAL_MALF_WORK, curr[0], curr[2], Integer.toString(WORK_MAX)));
				}
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException(trl.translateMessage(TRL_VAL_MALF_WORK, curr[0], curr[2], Integer.toString(WORK_MAX)));
			}
        	
        	if(curr[3] == null) {
        		throw new IllegalArgumentException(trl.translateMessage(TRL_VAL_PROC_WO_ARRIVAL, curr[0]));
        	}
        	try {
				int arrival = Integer.parseInt(curr[3]);
				if(arrival > ARRIVAL_MAX || arrival < 0) {
					throw new IllegalArgumentException(trl.translateMessage(TRL_VAL_MALF_ARRIVAL, curr[3], Integer.toString(ARRIVAL_MAX)));
				}
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException(trl.translateMessage(TRL_VAL_MALF_ARRIVAL, curr[3], Integer.toString(ARRIVAL_MAX)));
			}
        	
        }
        return true;
	}
	
	public void schedule() {
		sc.highlight(0);
		while (sumOfWork() != 0) {
			highlightProcessCol(2);
			setHint(trl.translateMessage(TRL_HINT_REMAINING_WORK));
			lang.nextStep(trl.translateMessage(TRL_SECTION_ITERATION, Integer.toString(currentTime)));
			unhighlightProcessCol(2);
			setHint(trl.translateMessage(TRL_HINT_INCOMING_PROCESS));
			
			sc.highlight(1);
			highlightProcessCol(0);
			highlightProcessCol(3);
			askEnqueueQuestion();
			lang.nextStep();
			unhighlightProcessCol(3);
			
			for(Process process : inc_procs) {
				if(process.arrival == currentTime && process.work > 0) {
					setHint(trl.translateMessage(TRL_HINT_PROCESS_STARTS_NOW, process.name));
					processMatrix.highlightCell(process.row, 3, null, null);
					lang.nextStep();
					sc.highlight(2);
					sc.highlight(3);
					queues.get(process.queue).add(process);
					sc.unhighlight(2);
					sc.unhighlight(3);
				}
				processMatrix.unhighlightCell(process.row, 3, null, null);
			}
			sc.unhighlight(1);
			unhighlightProcessCol(0);
			
			int due = -1;
			vars.set(trl.translateMessage(TRL_VAR_KEY_DUE), Integer.toString(due));
			if(Math.random() > 0.5) {
				askScheduleQuestion();
			}else {
				askNextQueueQuestion();
			}
			sc.highlight(4);
			setHint(trl.translateMessage(TRL_HINT_SEARCH_FOR_QUEUE_TO_SCHEDULE));
			lang.nextStep();
			vars.declare(
					VAR_TYPE_INT, 
					trl.translateMessage(TRL_VAR_KEY_I), 
					Integer.toString(0));
			for(int i = 0; i < queues.size(); i++) {
				vars.set(trl.translateMessage(TRL_VAR_KEY_I), Integer.toString(i));
				queues.get(i).highlight();
				queues.get(i).unhighlightTail();
				sc.highlight(5);
				setHint(trl.translateMessage(TRL_HINT_CHECK_QUEUE, queues.get(i).name));
				lang.nextStep();
				if(!queues.get(i).isEmpty()) {
					sc.highlight(6);
					sc.highlight(7);
					queues.get(i).unhighlightTail();
					setHint(trl.translateMessage(TRL_HINT_QUEUE_HAS_PENDING_WORK, queues.get(i).name));
					lang.nextStep();
					sc.unhighlight(5);
					sc.unhighlight(6);
					sc.unhighlight(7);
					due = i;
					vars.set(trl.translateMessage(TRL_VAR_KEY_DUE), Integer.toString(due));
					break;
				}
				queues.get(i).unhighlightTail();
				queues.get(i).unhighlight();
				sc.unhighlight(5);
			}
			vars.discard(trl.translateMessage(TRL_VAR_KEY_I));
			sc.unhighlight(4);
			
			sc.highlight(8);
			setHint(trl.translateMessage(TRL_HINT_CHECK_FOR_QUEUE_TO_SCHEDULE));
			lang.nextStep();
			if(due == -1) {
				sc.highlight(9);
				sc.highlight(10);
				idlingSteps++;
				schedulingOrder += SCHEDULING_ORDER_IDLE;
				incCurrentTime();
				setHint(trl.translateMessage(TRL_HINT_NO_QUEUE_HAS_PENDING_WORK));
				lang.nextStep();
				sc.unhighlight(8);
				sc.unhighlight(9);
				sc.unhighlight(10);
				continue;
			}
			sc.unhighlight(8);
			
			sc.highlight(11);
			queues.get(due).highlightTail();
			Process first = queues.get(due).first();
			first.run();
			setHint(trl.translateMessage(TRL_HINT_SCHEDULE_PROCESS_FROM_QUEUE, first.name, queues.get(due).name));
			lang.nextStep();
			
			sc.unhighlight(11);
			
			sc.highlight(12);
			hint.setText(trl.translateMessage(TRL_HINT_CHECK_IF_WORK_LEFT), null, defaultDuration);
			askRescheduleQuestion(first);
			lang.nextStep();
			first.unhighlightWork();
			if(first.work == 0) {
				sc.highlight(13);
				hint.setText(trl.translateMessage(TRL_HINT_PROCESS_HAS_NO_WORK_LEFT_REMOVE), null, defaultDuration);
				lang.nextStep();
				queues.get(due).removeTail();
				queues.get(due).unhighlight();;
				sc.unhighlight(13);
			}else {
				sc.highlight(14);
				sc.highlight(15);
				hint.setText(trl.translateMessage(TRL_HINT_PROCESS_HAS_WORK_LEFT_RESCHEDULE), null, defaultDuration);
				lang.nextStep();
				if(queues.get(due).useRoundRobin && queues.get(due).numberOfProcesses > 1) {
					sc.highlight(16);
					hint.setText(trl.translateMessage(TRL_HINT_TEMP_ROUND_ROBIN), null, defaultDuration);
					Process temp = first;
					lang.nextStep();
					sc.unhighlight(16);
					sc.highlight(17);
					queues.get(due).removeTail();
					sc.unhighlight(17);
					sc.highlight(18);
					queues.get(due).add(temp);
					sc.unhighlight(18);
					queues.get(due).unhighlight();
				}else {
					queues.get(due).unhighlight();
					queues.get(due).unhighlightTail();
				}
				sc.unhighlight(14);
				sc.unhighlight(15);
			}
			incCurrentTime();
			sc.unhighlight(12);
		}
		sc.unhighlight(0);
		hint.setText(trl.translateMessage(TRL_HINT_NO_PROCESS_HAS_PENDING_WORK), null, defaultDuration);
		lang.nextStep();
	}

	private void askScheduleQuestion() {
		if(askQuestion()) {
			MultipleChoiceQuestionModel scheduleQuestion = new MultipleChoiceQuestionModel(NAME_SCHEDULE_QUESTION + currentTime);
			scheduleQuestion.setPrompt(trl.translateMessage(TRL_QST_SCHED));
			scheduleQuestion.setGroupID(NAME_QG_SCHEDULE_QUESTIONS);
			Process next = null;
			for(int i = 0; i < queues.size(); i++) {
				if(queues.get(i).procs[queues.get(i).tail] != null) {
					next = queues.get(i).procs[queues.get(i).tail];
					break;
				}
			}
			for(Process p: inc_procs) {
				if(p.equals(next)) {
					scheduleQuestion.addAnswer(p.name, 1,  trl.translateMessage(TRL_FB_SCHED_RIGHT));
				}else {
					scheduleQuestion.addAnswer(p.name, 0,  trl.translateMessage(TRL_FB_SCHED_WRONG, next.name));
				}
				
			}
			if(next == null) {
				scheduleQuestion.addAnswer(trl.translateMessage(TRL_ANS_SCHED_NONE), 1, trl.translateMessage(TRL_FB_SCHED_RIGHT));
			}else {
				scheduleQuestion.addAnswer(trl.translateMessage(TRL_ANS_SCHED_NONE), 0, trl.translateMessage(TRL_FB_SCHED_WRONG, next.name));
			}
			lang.addMCQuestion(scheduleQuestion);
		}
	}

	private void askEnqueueQuestion() {
		if(askQuestion()) {
			MultipleSelectionQuestionModel enqueueQuestion = new MultipleSelectionQuestionModel(NAME_ENQUEUE_QUESTION + currentTime);
			enqueueQuestion.setPrompt(trl.translateMessage(TRL_QST_ENQ));
			enqueueQuestion.setGroupID(NAME_QG_ENQUEUE_QUESTIONS);
			boolean any = false;
			for(int i = 0; i < inc_procs.size(); i++) {
				Process current = inc_procs.get(i);
				if(current.arrival == currentTime) {
					any = true;
					enqueueQuestion.addAnswer(current.name, 1, trl.translateMessage(TRL_FB_ENQ_RIGHT_ARRIVAL, current.name, Integer.toString(current.arrival)));
				}else {
					enqueueQuestion.addAnswer(current.name, 0, trl.translateMessage(TRL_FB_ENQ_WRONG_ARRIVAL, Integer.toString(currentTime), current.name, Integer.toString(current.arrival)));
				}
			}
			if(any) {
				enqueueQuestion.addAnswer(trl.translateMessage(TRL_ANS_ENQ_NONE), 0, trl.translateMessage(TRL_FB_ENQ_WRONG_NONE));
			}else {
				enqueueQuestion.addAnswer(trl.translateMessage(TRL_ANS_ENQ_NONE), 1, trl.translateMessage(TRL_FB_ENQ_RIGHT_NONE));
			}
			lang.addMSQuestion(enqueueQuestion);
		}
	}
	
	private void askNextQueueQuestion() {
		if(askQuestion()) {
			MultipleChoiceQuestionModel nextQueueQuestion = new MultipleChoiceQuestionModel(NAME_NEXT_QUEUE_QUESTION + currentTime);
			nextQueueQuestion.setPrompt(trl.translateMessage(TRL_QST_NQ));
			nextQueueQuestion.setGroupID(NAME_QG_NEXT_QUEUE_QUESTIONS);
			Queue next = null;
			for(int i = 0; i < queues.size(); i++) {
				Queue current = queues.get(i);
				if(next != null) {
					nextQueueQuestion.addAnswer(current.name, 0, trl.translateMessage(TRL_FB_NQ_WRONG_NOT_FIRST, next.name));
				}else {
					if(current.isEmpty()) {
						nextQueueQuestion.addAnswer(current.name, 0, trl.translateMessage(TRL_FB_NQ_WRONG_EMPTY));
					} else {
						next = current;
						nextQueueQuestion.addAnswer(current.name, 1, trl.translateMessage(TRL_FB_NQ_RIGHT_QUEUE, next.name));
					}
				}
				
			}
			if(next == null) {
				nextQueueQuestion.addAnswer(trl.translateMessage(TRL_ANS_NQ_NONE), 1, trl.translateMessage(TRL_FB_NQ_RIGHT_NONE));
			}else {
				nextQueueQuestion.addAnswer(trl.translateMessage(TRL_ANS_NQ_NONE), 0, trl.translateMessage(TRL_FB_NQ_WRONG_NONE, next.name));
			}
			lang.addMCQuestion(nextQueueQuestion);
		}
	}
	
	private void askRescheduleQuestion(Process first) {
		if(askQuestion()) {
			MultipleChoiceQuestionModel rescheduleQuestion = new MultipleChoiceQuestionModel(NAME_RESCHEDULE_QUESTION + currentTime);
			rescheduleQuestion.setPrompt(trl.translateMessage(TRL_QST_RSCHED));
			rescheduleQuestion.setGroupID(NAME_QG_RESCHEDULE_QUESTIONS);
			Queue queue = queues.get(first.queue);
			if(first.work == 0) {
				rescheduleQuestion.addAnswer(trl.translateMessage(TRL_ANS_RSCHED_REM, first.name), 1, trl.translateMessage(TRL_FB_RSCHED_RIGHT_NO_WORK));
				rescheduleQuestion.addAnswer(trl.translateMessage(TRL_ANS_RSCHED_STAY, first.name), 0, trl.translateMessage(TRL_FB_RSCHED_WRONG_NO_WORK));
				rescheduleQuestion.addAnswer(trl.translateMessage(TRL_ANS_RSCHED_RE, first.name), 0, trl.translateMessage(TRL_FB_RSCHED_WRONG_NO_WORK));
			} else {
				rescheduleQuestion.addAnswer(trl.translateMessage(TRL_ANS_RSCHED_REM, first.name), 0, trl.translateMessage(TRL_FB_RSCHED_WRONG_WORK_LEFT));
				if(queue.useRoundRobin) {
					if(queue.numberOfProcesses == 1) {
						rescheduleQuestion.addAnswer(trl.translateMessage(TRL_ANS_RSCHED_STAY, first.name), 1, trl.translateMessage(TRL_FB_RSCHED_RIGHT_ONLY_ONE, first.name));
						rescheduleQuestion.addAnswer(trl.translateMessage(TRL_ANS_RSCHED_RE, first.name), 0, trl.translateMessage(TRL_FB_RSCHED_WRONG_ONLY_ONE, first.name));
					}else {
						rescheduleQuestion.addAnswer(trl.translateMessage(TRL_ANS_RSCHED_STAY, first.name), 0, trl.translateMessage(TRL_FB_RSCHED_WRONG_RR, first.name));
						rescheduleQuestion.addAnswer(trl.translateMessage(TRL_ANS_RSCHED_RE, first.name), 1, trl.translateMessage(TRL_FB_RSCHED_RIGHT_RR, first.name));
					}
				}else {
					rescheduleQuestion.addAnswer(trl.translateMessage(TRL_ANS_RSCHED_STAY, first.name), 1, trl.translateMessage(TRL_FB_RSCHED_RIGHT_FIFO, first.name));
					rescheduleQuestion.addAnswer(trl.translateMessage(TRL_ANS_RSCHED_RE, first.name), 0, trl.translateMessage(TRL_FB_RSCHED_WRONG_FIFO, first.name));
				}
			}
			lang.addMCQuestion(rescheduleQuestion);
		}
	}

	private void summarize() {
		lang.hideAllPrimitivesExcept(title);
		lang.newText(
				new Coordinates(30,  70),
				trl.translateMessage(TRL_SUM_MLQ_TIMESLOTS, Integer.toString(idlingSteps + computingSteps)),
				NAME_SUM_LN + 1, 
				null);
		lang.newText(
				new Coordinates(30,  90),
				trl.translateMessage(TRL_SUM_PROC_IN_QUEUES, Integer.toString(inc_procs.size()), Integer.toString(queues.size())),
				NAME_SUM_LN + 2, 
				null);
		lang.newText(
				new Coordinates(30,  130),
				trl.translateMessage(TRL_SUM_WORK_TIME, Integer.toString(computingSteps)),
				NAME_SUM_LN + 3, 
				null);
		lang.newText(
				new Coordinates(30,  150),
				trl.translateMessage(TRL_SUM_IDLE_TIME, Integer.toString(idlingSteps)),
				NAME_SUM_LN + 4, 
				null);
		lang.newText(
				new Coordinates(30,  170),
				trl.translateMessage(TRL_SUM_SCHEDULING_ORDER, schedulingOrder),
				NAME_SUM_LN + 5, 
				null);
		lang.nextStep(trl.translateMessage(TRL_SECTION_CONCLUSION));
		
	}
	
	private void unhighlightProcessCol(int col) {
		for(int i = 1; i < processMatrix.getNrRows(); i++) {
			processMatrix.unhighlightCell(i, col, null, null);
		}
	}
	
	private void highlightProcessCol(int col) {
		for(int i = 1; i < processMatrix.getNrRows(); i++) {
			processMatrix.highlightCell(i, col, null, null);
		}
	}
	
	private void incCurrentTime(){
		currentTime++;
		currentTimeText.setText(trl.translateMessage(TRL_CURRENTTIME, Integer.toString(currentTime)), null, defaultDuration);
	}

	public int sumOfWork() {
		int sum = 0;
		for (Process p : inc_procs) {
			sum += p.work;
		}
		return sum;
	}
	
	public void setHint(String s) {
		hint.setText(trl.translateMessage(TRL_HINT, s), null, defaultDuration);
	}

    public String getName() {
        return INIT_TITLE;
    }

    public String getAlgorithmName() {
        return INIT_TITLE;
    }

    public String getAnimationAuthor() {
        return INIT_AUTHOR;
    }

    public String getDescription(){
        return trl.translateMessage(TRL_DESCRIPTION);
    }

    public String getCodeExample(){
        return trl.translateMessage(TRL_SOURCE_CODE);
    }

    public String getFileExtension(){
        return FILE_EXTENSION;
    }

    public Locale getContentLocale() {
        return loc;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }
    
    private boolean askQuestion() {
    	double r = Math.random();
    	return r * 3 > 2;
    }
    
	/**
	 * A Process is an object that arrives at a given time in a predefined queue
	 * to schedule the given amount of work. 
	 */
	public class Process {
		/** The name of this process. */
		public String name;
		/** Queue-ID of this process. */
		public int queue;
		/** The number of time-slices this process needs */
		public int work;
		/** The number of steps after which this process arrives */
		public int arrival;
		/** The row in process matrix of this process */
		public int row;
		/**
		 * Constructs a new Process.
		 * 
		 * @param	name		Name of the process.
		 * @param	queue		Predefined queue of this process
		 * @param	work		The number of time slices, this process needs
		 * 						to be scheduled
		 * @param	arrival		The number of time slices after which the
		 * 						process arrives.
		 * @param col 
		 */
		public Process(String name, int queue, int work, int arrival, int col) {
			super();
			this.name = name;
			this.queue = queue;
			this.work = work;
			this.arrival = arrival;
			this.row = col;
		}
		
		public void highlightWork() {
			processMatrix.highlightCell(row, 2, null, null);
		}
		
		public void unhighlightWork() {
			processMatrix.unhighlightCell(row, 2, null, null);
		}
		
		public void run() {
			work--;
			vars.set(trl.translateMessage(TRL_VAR_KEY_PROCESS, name), Integer.toString(work));
			schedulingOrder += name;
			computingSteps++;
			highlightWork();
			processMatrix.put(row,2,Integer.toString(work),null,null);
		}
	}
	
	/**
	 * A Queue has a name and a list of enqueued processes. A Queue can be
	 * scheduled in FIFO-Mode (useRoundRobin: false) or in RoundRobin-Mode
	 * (useRoundRobin: true).
	 */
	public class Queue {
		
		/** This Queues Name */
		public String name;
		/** The list of processes in this queue */
		public Process[] procs;
		/** true if this queue uses RoundRobin-Scheduling */
		public boolean useRoundRobin;
		/** pointer to the queues head */
		public int head;
		/** pointer to the queues tail */
		public int tail;
		/** number of processes in buffer */
		public int numberOfProcesses;
		/** view of this queue */
		public StringArray view;
		/** Label of this Queue */
		public Text label;
		/** Head marker */
		public ArrayMarker headMarker;
		/** Tail marker */
		public ArrayMarker tailMarker;
		
		/**
		 * Constructs a new Queue.
		 * 
		 * @param	name	The name of the Queue.
		 * @param	useRR	Specifies if this queue schedules in
		 * 					RoundRobin-mode.
		 */
		public Queue(String name, boolean useRR, int size, StringArray view, Text label) {
			this.name = name;
			this.useRoundRobin = useRR;
			this.view = view;
			this.label = label;
			procs = new Process[size];
			head = 0;
			headMarker = lang.newArrayMarker(view, head, NAME_HEAD_MARKER, null, headMarkerProperties);
			tail = 0;
			tailMarker = lang.newArrayMarker(view, tail, NAME_TAIL_MARKER, null, tailMarkerProperties);
			numberOfProcesses = 0;
		}
		
		public int size() {
			return procs.length;
		}
		
		public int numberOfProcesses() {
			return numberOfProcesses;
		}
		
		public void removeTail() {
			hint.setText(trl.translateMessage(TRL_HINT_REMOVE_PROCESS_FROM_QUEUE, procs[tail].name, name), null, defaultDuration);
			procs[tail] = null;
			view.put(tail, EMPTY_QUEUE_FIELD, null, null);
			int oldTail = tail;
			tail = (tail + 1) % procs.length;
			vars.set(trl.translateMessage(TRL_VAR_KEY_QUEUE, Integer.toString(queues.indexOf(this))), toString());
			tailMarker.move(tail, null, null);
			numberOfProcesses--;
			lang.nextStep();
			view.unhighlightCell(oldTail, null, null);
		}
		
		public Process first() {
			// random access for access counter
			view.getData(tail);
			return procs[tail];
		}
		
		public void add(Process p) {
			procs[head] = p;
			view.highlightCell(head, null, null);
			view.put(head, p.name, null, null);
			hint.setText(trl.translateMessage(TRL_HINT_ADD_PROCESS_TO_QUEUE, p.name, name), null, defaultDuration);
			int oldHead = head;
			head = (head + 1) % procs.length;
			vars.set(trl.translateMessage(TRL_VAR_KEY_QUEUE, Integer.toString(queues.indexOf(this))), toString());
			headMarker.move(head, null, null);
			numberOfProcesses++;
			lang.nextStep();
			view.unhighlightCell(oldHead, null, null);
		}
		
		public boolean isEmpty() {
			return numberOfProcesses == 0;
		}
		
		public void highlight() {
			label.setFont(highlightFont, null, null);
		}
		
		public void unhighlight() {
			label.setFont(defaultFont, null, null);
		}
		
		public void highlightTail() {
			view.highlightCell(tail, null, null);
		}
		
		public void unhighlightTail() {
			view.unhighlightCell(tail, null, null);
		}
		
		public String toString() {
			String s = name + " [";
			for(int i = tail; i != head; i = ((i + 1) % procs.length)) {
				s += procs[i].name;
				if((i + 1) % procs.length != head) {
					s += " ,";
				}
			}
			s += "]";
			return s;
		}
	}

}