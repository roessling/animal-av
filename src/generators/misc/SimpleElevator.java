/*
 * SimpleElevator.java
 * David Langsam und Max Kaiser, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ListBasedQueue;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.misc.helperSimpleElevator.Passanger;
import generators.misc.helperSimpleElevator.Request;
import generators.misc.helperSimpleElevator.SourceCodeAnimation;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.QuestionGroupModel;
import translator.Translator;
//import animal.main.Animal;

public class SimpleElevator implements ValidatingGenerator {
	private Language lang;
	private SourceCodeAnimation sca; // handles all the animation of the source code of the main frame
	private int position;
	private RectProperties elevator_props;
	private CircleProperties passanger_props;
	private int num_floors;
	private int question_num;

	private int[][] input_requests;

	private Timing defaultTiming;

	private final int y_off = 150;
	private final int x_off = 130; // for initial circles appearing
	private final int x_off2 = 20; // for the Rectangle(elevator) and the Floor level Text
	private final int dy = 30; // Graphical distance of the floor levels
	private final int dx = 30; // Graphical distance of the Circles in X-direction
	private final int circ_rad = 10; // Radius of the circle
	private final int el_space = dx * 3; // Elevator is 3 circles thick, though infinite fit in
	private int waiting[]; // Remembers how many Circles are waiting for the Elevator in their floor-level
	private Translator translator;
	private final Locale locale; // Locale.UK OR Locale.GERMANY
	private final String translator_path;
	private Text text_direction;
	private Text text_time;
	private Text movmentCounter_text;
	private Variables vars;
	private final Random rand = new Random();
	private int fillcount = 0;
	private int requestcount = 0;
	private int question_propability = 30; // -> 30%

	private int id = 3141592; // random id

	public SimpleElevator(Locale l) {
		locale = l;
		translator_path = "resources/langSimpleElevator";
	}

//	public static void main(String[] args) {
//		Generator generator = new SimpleElevator(Locale.ENGLISH);
//		// if true -> start GUI, if false -> print text
//		boolean start_with_animal = true;
//		if (start_with_animal) {
//			Animal.startGeneratorWindow(generator);
//		} else {
//			generator.init();
//			AnimationPropertiesContainer apc = new AnimationPropertiesContainer();
//			apc.add(new CircleProperties("passanger_props"));
//			apc.add(new RectProperties("elevator_props"));
//			Hashtable<String, Object> primitives = new Hashtable<String, Object>();
//			primitives.put("num_floors", new Integer(8));
//			primitives.put("question_num", new Integer(5));
//			primitives.put("position", new Integer(2));
//			primitives.put("input_requests", new int[][] { { 0, 5, 0 }, { 7, 0, 2 }, { 0, 1, 4 }, { 5, 3, 5 } });
//			String s = generator.generate(apc, primitives);
//			System.out.println(s);
//		}
//	}

	public void init() {
		lang = new AnimalScript("Simple Elevator", "David Langsam und Max Kaiser", 800, 600);
		lang.setStepMode(true);
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
		vars = lang.newVariables();
		if (translator == null) {
			translator = new Translator(translator_path, locale);
		}
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		elevator_props = (RectProperties) props.getPropertiesByName("elevator_props");
		passanger_props = (CircleProperties) props.getPropertiesByName("passanger_props");
		input_requests = (int[][]) primitives.get("input_requests");
		position = (Integer) primitives.get("position");
		num_floors = (Integer) primitives.get("num_floors");
		question_num = (Integer) primitives.get("question_num");
		question_propability = (Integer) primitives.get("question_propability");

		List<Request> requests = setup();

		int compare_time = calculateCompareTime(requests);
		List<Primitive> not_hide = showInitialFrame(requests);

		vars.declare("string", "requests", requests.toString(), "All Requests");
		vars.declare("int", "floorLevels", "" + num_floors, "Number of floor levels");

		setupMainAnimationGraphic();
		List<Request> copy = requests.stream().collect(Collectors.toList());
		int time = runElevator(requests);
		lang.hideAllPrimitivesExcept(not_hide);
		showFinalFrame(time, compare_time, copy);
		lang.finalizeGeneration();

		return lang.toString();
	}

	private int calculateCompareTime(List<Request> requests) {
		int ret = 0;
		int prev_level = 0;
		for (Request r : requests) {
			ret += Math.abs(r.source - prev_level);
			ret += Math.abs(r.dest - r.source);
			prev_level = r.dest;
		}
		return ret;
	}

	private void showFinalFrame(int s1, int s2, List<Request> req) {
		SourceCodeProperties scp = new SourceCodeProperties();
		scp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SERIF", Font.PLAIN, 15));
		SourceCode code = lang.newSourceCode(new Coordinates(30, y_off - 70), "final_text", defaultTiming, scp);
		List<String> text = new LinkedList<String>();

		text.add(translator.translateMessage("showFinalFrame.t1"));
		for (int j = 0; j < req.size(); j++) {
			text.add(req.get(j).toString());
		}
		text.add(translator.translateMessage("showFinalFrame.t2") + " " + s1 + " "
				+ translator.translateMessage("showFinalFrame.t3"));
		text.add("");
		text.add(translator.translateMessage("showFinalFrame.t4"));
		text.add(translator.translateMessage("showFinalFrame.t5") + " " + s2 + " "
				+ translator.translateMessage("showFinalFrame.t6"));
		text.add("");
		text.add(translator.translateMessage("showFinalFrame.t7"));

		for (String s : text) {
			code.addCodeLine(s, null, 0, null);
		}
		lang.nextStep("Conclusion");
	}

	private void setupMainAnimationGraphic() {
		for (int i = 1; i <= num_floors; i++) {
			lang.newText(new Coordinates(2, y_off + i * dy - 6), "" + (num_floors - i), "t" + (num_floors - i),
					defaultTiming);
		}
		sca = new SourceCodeAnimation(lang, new Coordinates(300, y_off), defaultTiming);
		// Show text below the list based queue : "Requsests:(From/To/Timing)"
		TextProperties tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SERIF", Font.PLAIN, 12));
		lang.newText(new Coordinates(10, y_off - 30), "Requests:" + translator.translateMessage("showInitialFrame.t3"),
				"description request", null, tp);
	}

	private List<Primitive> showInitialFrame(List<Request> requests) {
		RectProperties rp = new RectProperties();
		rp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GRAY);
		rp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		Rect re = lang.newRect(new Coordinates(25, y_off - 130), new Coordinates(213, y_off - 105), "underrect", null,
				rp);
		TextProperties tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.ORANGE);
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SERIF", Font.BOLD, 20));
		Text headline = lang.newText(new Coordinates(30, y_off - 130), "Simple Elevator", "headline_text", null, tp);
		lang.nextStep("INTRO");
		SourceCodeProperties scp = new SourceCodeProperties();
		scp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SERIF", Font.PLAIN, 15));
		SourceCode sc = lang.newSourceCode(new Coordinates(30, y_off - 70), "initial_text", defaultTiming, scp);

		for (int i = 1; i <= 17; i++) {
			if (i == 4 || i == 9 || i == 12 || i == 15)
				lang.nextStep();
			sc.addCodeLine(translator.translateMessage("showInitialFrame.t" + i), null, 0, null);
			if (i == 2 || i == 4 || i == 14) {
				sc.addCodeLine("", null, 0, null);
			}
			if (i == 3) {
				for (int j = 0; j < requests.size(); j++) {
					sc.addCodeLine(requests.get(j).toString(), "sc_text" + id++, 0, null);
				}
				sc.addCodeLine("", null, 0, null);
			}

		}
		lang.nextStep();

		sc.hide();
		LinkedList<Primitive> lst = new LinkedList<Primitive>();
		lst.add(headline);
		lst.add(re);
		return lst;
	}

	public List<Request> setup() {
		// lang = Language.getLanguageInstance(AnimationType.ANIMALSCRIPT, "Simple
		// Elevator", "David Langsam / Max Kaiser",
		// 640, 480);
		defaultTiming = new TicksTiming(30);

		waiting = new int[num_floors];
		for (int i = 0; i < num_floors; i++) {
			waiting[i] = 0;
		}

		if (input_requests == null || input_requests[0].length != 3) {
			throw new IllegalArgumentException("Invalid input: requests[][] has not dimension Nx3");
		}

		List<Request> requests = new LinkedList<Request>();
		for (int i = 0; i < input_requests.length; i++) {
			requests.add(new Request(input_requests[i][0], input_requests[i][1], input_requests[i][2]));
		}
		return requests;
	}

	public int runElevator(List<Request> requests) {
		int time = 0;
		int step_counter = 0;
		int direction = 0; // 0=Still, 1=Hoch, -1=Runter
		boolean queuesChanged = false; // nur für die Animation wichtig, nicht für den Alg. selbst

		List<Request> from = new LinkedList<Request>(); // List for the waiting circles
		List<Request> to = new LinkedList<Request>(); // List for the circles currenty IN the elevator
		List<Request> out = new LinkedList<Request>(); // List of Circles that moved out of the Elevator (only for
														// better animation reasons)

		vars.declare("int", "time", "" + time, "passed time");
		vars.declare("int", "stepcounter", "" + step_counter, "int");
		vars.declare("int", "position", "" + position, "Floor level of the elevator");
		vars.declare("string", "direction", "NONE", "Direction of travel");
		vars.declare("string", "inElevator", to.toString(), "All requests currently in the elevator");

		setupStatus();
		Rect el = lang.newRect(new Coordinates(x_off2, y_off + dy * (num_floors - position) - 10),
				new Coordinates(x_off2 + el_space, y_off + dy * (num_floors - position) + 10), "elevator",
				defaultTiming, elevator_props);
		ListBasedQueue<Request> lang_requests = lang.newListBasedQueue(new Coordinates(10, y_off - 80), requests,
				"requests", null);
		lang.nextStep("Algorithm");
		QuestionGroupModel qgm = new QuestionGroupModel("question_group_model" + id++, question_num);
		lang.addQuestionGroup(qgm);

		while (!(requests.isEmpty() && from.isEmpty() && to.isEmpty())) {

			sca.toggle(SourceCodeAnimation.WHILE_TRUE, true);
			queuesChanged = manageRequests(requests, from, to, out, time, position, lang_requests);
			if (!queuesChanged) {
				direction = moveElevator(requests, from, to, time, position, direction, text_direction);
				askQuesiton(qgm, direction);
				step_counter += Math.abs(direction);
				position += direction;
				sca.toggle(SourceCodeAnimation.POSITION_PE_EVAL_DIRECTION, false);
				moveElevatorGraphic(direction, to, el);
				lang.nextStep();
				queuesChanged = false;
			}
			time++;
			vars.set("time", "" + time);
			text_time.setText(translator.translateMessage("timing") + ": " + time, null, null);
			vars.set("stepcounter", "" + step_counter);
			movmentCounter_text.setText(translator.translateMessage("stepcounter") + ": " + step_counter, null, null);
			vars.set("position", "" + position);
		}

		vars.discard("direction");
		vars.discard("inElevator");
		vars.discard("position");
		vars.discard("stepcounter");
		vars.discard("time");

		return step_counter;
	}

	private void askQuesiton(QuestionGroupModel qgm, int direction) {
		if (rand.nextInt(100) > question_propability) {
			return;
		}
		MultipleChoiceQuestionModel name = new MultipleChoiceQuestionModel("Derp" + id++);
		name.setGroupID(qgm.getID());
		name.setPrompt(translator.translateMessage("questionOne"));
		switch (direction) {
		case 1:
			name.addAnswer("answer_one", translator.translateMessage("up"), 1, translator.translateMessage("right"));
			name.addAnswer("answer_two", translator.translateMessage("down"), 0, translator.translateMessage("wrong"));
			name.addAnswer("answer_three", translator.translateMessage("none"), 0,
					translator.translateMessage("wrong"));
			break;
		case -1:
			name.addAnswer("answer_one", translator.translateMessage("up"), 0, translator.translateMessage("wrong"));
			name.addAnswer("answer_two", translator.translateMessage("down"), 1, translator.translateMessage("right"));
			name.addAnswer("answer_three", translator.translateMessage("none"), 0,
					translator.translateMessage("wrong"));
			break;
		case 0:
			name.addAnswer("answer_one", translator.translateMessage("up"), 0, translator.translateMessage("wrong"));
			name.addAnswer("answer_two", translator.translateMessage("down"), 0, translator.translateMessage("wrong"));
			name.addAnswer("answer_three", translator.translateMessage("none"), 1,
					translator.translateMessage("right"));
			break;
		}
		lang.addMCQuestion(name);
		if (name.getPointsAchieved() > 0) {
			// qgm.incrementCorrectlyAnswered();
		}
	}

	private void setupStatus() {
		TextProperties tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SERIF", Font.BOLD, 13));
		lang.newText(new Coordinates(x_off2, y_off + dy * num_floors + 25), "STATUS", "text_STATUS", defaultTiming, tp);
		text_direction = lang.newText(new Coordinates(x_off2, y_off + dy * num_floors + 45),
				translator.translateMessage("direction_none"), "text_direction", defaultTiming, tp);
		text_time = lang.newText(new Coordinates(x_off2, y_off + dy * num_floors + 60),
				translator.translateMessage("timing") + ": 0", "text_time", defaultTiming, tp);
		movmentCounter_text = lang.newText(new Coordinates(x_off2, y_off + dy * num_floors + 75),
				translator.translateMessage("stepcounter") + ": 0", "mvmtCounter_text", defaultTiming, tp);
	}

	private void moveElevatorGraphic(int direction, List<Request> to, Rect el) {
		el.moveBy(null, 0, -direction * dy, null, defaultTiming);
		for (Request r : to) {
			r.getPassanger().moveBy(null, 0, -direction * 30, null, defaultTiming);
		}
	}

	public boolean manageRequests(List<Request> requests, List<Request> from, List<Request> to, List<Request> out,
			int time, int position, ListBasedQueue<Request> lang_requests) {
		boolean queueChanged = false;
		List<Request> tmp = new LinkedList<Request>();

		sca.toggle(SourceCodeAnimation.CHECK_REQUESTS, true);

		// request -> from
		tmp = requests.stream().filter(item -> item.time <= time).collect(Collectors.toList());
		for (Request r : tmp) {
			lang_requests.dequeue();
			r.setPassanger(new Passanger(lang, circ_rad, x_off + waiting[r.source] * dx,
					y_off + dy * (num_floors - r.source), "" + r.dest, id++, passanger_props));
			waiting[r.source]++;
			lang.nextStep("Request " + requestcount++);
		}
		requests.removeAll(tmp);
		from.addAll(tmp);
		if (!tmp.isEmpty()) {
			vars.set("requests", requests.toString());
		}

		sca.toggle(SourceCodeAnimation.IF_POSITION_EQ_A_POS_OF_REQUEST, true);
		
		// remove To:
		tmp = to.stream().filter(item -> item.dest == position).collect(Collectors.toList());
		for (Request r : tmp) {
			r.getPassanger().moveBy(null, x_off - x_off2, 0, null, defaultTiming);
			fillcount--;
		}
		to.removeAll(tmp);
		out.addAll(tmp);
		if (!tmp.isEmpty()) {
			queueChanged = true;
			vars.set("inElevator", to.toString());
			sca.toggle(SourceCodeAnimation.WAIT_FOR_PASSANGERS_TO_GET_IN_OR_OUT, false);
		}

		// from -> to
		tmp = from.stream().filter(item -> item.source == position).collect(Collectors.toList());
		to.addAll(tmp);
		from.removeAll(tmp);
		if (!tmp.isEmpty()) {
			queueChanged = true;
			waiting[position] = 0;
			vars.set("inElevator", to.toString());
			sca.toggle(SourceCodeAnimation.WAIT_FOR_PASSANGERS_TO_GET_IN_OR_OUT, false);
		}
		for (Request r : tmp) {
			r.getPassanger().moveBy(null, get_X_off_forelevator(r), 0, null, defaultTiming);
			fillcount++;
		}
		boolean flag = queueChanged;
		if (queueChanged) {
			lang.nextStep();
		}
				
		//sca.toggle(SourceCodeAnimation.IF_PASSANGERS_WENT_IN_OR_OUT, true);
		if (flag) {
			sca.toggle(SourceCodeAnimation.SKIP, true);
			// remove out:
			tmp = out.stream().collect(Collectors.toList());
			out.removeAll(tmp);
			for (Request r : tmp) {
				r.getPassanger().hide(defaultTiming);
			}
			// no queueChanged-flag, because the elevator must not stall.
		}

		return queueChanged;
	}

	public int moveElevator(List<Request> requests, List<Request> from, List<Request> to, int time, int position,
			int direction, Text direction_text) {
		boolean case1;

		sca.toggle(SourceCodeAnimation.IF_DIRECTION_EQ_NONE, true);
		if (direction == 0) {
			sca.toggle(SourceCodeAnimation.IF_REQUESTS_ABOVE, true);
			// direction == 0 but a new Request in "from" or "to" appeared
			if (case1 = (!from.isEmpty() && from.get(0).source > position)
					| (!to.isEmpty() && to.get(0).dest > position)) {
				direction_text.setText(translator.translateMessage("direction_up"), null, null);
				vars.set("direction", "UP");
				sca.toggle(SourceCodeAnimation.DIRECTION_SET_UP, true);
				direction = 1;
			}

			sca.toggle(SourceCodeAnimation.ELSE_IF_REQUEST_BENEATHE, true);
			if (!case1 & ((!from.isEmpty() && from.get(0).source < position)
					| (!to.isEmpty() && to.get(0).dest < position))) {
				direction_text.setText(translator.translateMessage("direction_down"), null, null);
				vars.set("direction", "DOWN");
				sca.toggle(SourceCodeAnimation.DIRECTION_SET_DOWN, true);
				direction = -1;
			}

		} else {
			// If moving up and there are request above, keep going.
			boolean fromOrToAbove = false;
			for (Request r : from) {
				if (r.source > position)
					fromOrToAbove = true;
			}
			for (Request r : to) {
				if (r.dest > position)
					fromOrToAbove = true;
			}
			boolean fromOrToBeneath = false;
			for (Request r : from) {
				if (r.source < position)
					fromOrToBeneath = true;
			}
			for (Request r : to) {
				if (r.dest < position)
					fromOrToBeneath = true;
			}
			sca.toggle(SourceCodeAnimation.ELSE_2, true);
			sca.toggle(SourceCodeAnimation.IF_NO_MORE_REQUESTS_ABOVE_AND_NO_MORE_REQUESTS_BENEATHE, true);
			if (!fromOrToAbove && !fromOrToBeneath) {
				direction_text.setText(translator.translateMessage("direction_none"), null, null);
				vars.set("direction", "NONE");
				sca.toggle(SourceCodeAnimation.DIRECTION_SET_NONE_2, true);
				direction = 0;
			}
			sca.toggle(SourceCodeAnimation.IF_DIRECTION_EQ_UP_AND_NO_MORE_REQUESTS_ABOVE, true);
			if (!(!fromOrToAbove && !fromOrToBeneath) && direction == 1) {
				if (!fromOrToAbove) {
					direction_text.setText(translator.translateMessage("direction_down"), null, null);
					vars.set("direction", "DOWN");
					sca.toggle(SourceCodeAnimation.DIRECTION_SET_DOWN_2, true);
					direction = -1;
				}
			}
			sca.toggle(SourceCodeAnimation.ELSE_IF_DIRECTOIN_EQ_DOWN_AND_NO_MORE_REQUESTS_BENEATHE, true);
			if (direction != 1 && direction == -1) {
				if (!fromOrToBeneath) {
					direction_text.setText(translator.translateMessage("direction_up"), null, null);
					vars.set("direction", "UP");
					sca.toggle(SourceCodeAnimation.DIRECTION_SET_UP_2, true);
					direction = 1;
				}
			}
		}
		return direction;
	}

	public int get_X_off_forelevator(Request r) {
		int ret = 0;
		Coordinates c = (Coordinates) r.getPassanger().getCenter();
		if (fillcount < 4) {
			ret = x_off2 - c.getX() + (circ_rad * 2 + 5) * fillcount + circ_rad;
		} else {
			Random rand = new Random();
			ret = rand.nextInt(el_space - circ_rad * 2) + el_space - circ_rad * 2 + x_off2 + circ_rad - c.getX();
		}
		return ret;
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		int nofl = (Integer) primitives.get("num_floors");
		int start = (Integer) primitives.get("position");
		int quest = (Integer) primitives.get("question_num");
		int[][] req = (int[][]) primitives.get("input_requests");
		int prop = (int) primitives.get("question_propability");

		if (prop < 0 || prop > 100) {
			JOptionPane.showMessageDialog(null, "The question propabilitiy must be in the interval of 0 to 100!",
					"Invalid input", JOptionPane.OK_OPTION);
			return false;
		}

		if (nofl < 2 || nofl > 9) {
			JOptionPane.showMessageDialog(null, "Floor levels smaller 2 or greater 9!", "Invalid input",
					JOptionPane.OK_OPTION);
			return false;
		}
		if (quest < 0 || quest > 20) {
			JOptionPane.showMessageDialog(null, "It is not possible to ask less than 0 or more than 20 questions",
					"Invalid input", JOptionPane.OK_OPTION);
			return false;
		}
		if (start < 0 || start > nofl - 1) {
			JOptionPane.showMessageDialog(null,
					"Startposition is smaller than 0 or greater than the given number of floor levels", "Invalid input",
					JOptionPane.OK_OPTION);
			return false;
		}
		if (req == null || req.length < 1 || req[0].length != 3) {
			JOptionPane.showMessageDialog(null, "Not enaugh requests (<1) or request table has not 3 columns",
					"Invalid input", JOptionPane.OK_OPTION);
			return false;
		}
		for (int i = 0; i < req.length; i++) {
			// Number of floors is > max or < 0
			if (req[i][0] >= nofl || req[i][0] < 0) {
				JOptionPane.showMessageDialog(null,
						"One of the source floors has a number greater than the buildings floor level or is smaller 0",
						"Invalid input", JOptionPane.OK_OPTION);
				return false;
			}
			if (req[i][1] >= nofl || req[i][1] < 0) {
				JOptionPane.showMessageDialog(null,
						"One of the destination floors has a number greater than the buildings floor level or is smaller 0",
						"Invalid input", JOptionPane.OK_OPTION);
				return false;
			}
			if (req[i][2] > 100 || req[i][2] < 0) {
				JOptionPane.showMessageDialog(null, "One of the timing parameters is greater than 100 or smaller 0",
						"Invalid input", JOptionPane.OK_OPTION);
				return false;
			}
		}
		for (int i = 0; i < req.length - 1; i++) {
			if (req[i][2] > req[i + 1][2]) {
				JOptionPane.showMessageDialog(null, "The timings must only increase!", "Invalid input",
						JOptionPane.OK_OPTION);
				return false;
			}
		}
		return true;
	}

	public String getName() {
		return "Simple Elevator";
	}

	public String getAlgorithmName() {
		return "Simple Elevator";
	}

	public String getAnimationAuthor() {
		return "David Langsam und Max Kaiser";
	}

	public String getDescription() {
		if (translator == null) {
			translator = new Translator(translator_path, locale);
		}
		String s = "";
		for (int i = 1; i <= 14; i++) {
			s += translator.translateMessage("description.t" + i) + "\n";
		}
		return s;
	}

	public String getCodeExample() {
		String s = "public void pseudocode4Elevator() {\n";
		s += "while(true) {\n";
		s += "\tcheckRequests();\n";
		s += "\tif(position == a_pos_of_a_request) {\n";
		s += "\t\twait_for_passangers_to_get_in_or_out();\n";
		s += "\t\tskip()\n";
		s += "\t}\n";
		s += "\tif (direction == NONE) {\n";
		s += "\t\tif(requests_above) {\n";
		s += "\t\t\tdirection = UP;\n";
		s += "\t\t} else if (request_beneathe) {\n";
		s += "\t\t\tdirection = DOWN;\n";
		s += "\t\t}\n";
		s += "\t} else {\n";
		s += "\t\tif(no_more_requests_above && no_more_request_beneathe) {\n";
		s += "\t\t\tdirection = NONE;		\n";
		s += "\t\t} else if(direction == UP && no_more_requests_above) {\n";
		s += "\t\t\tdirection = DOWN;\n";
		s += "\t\t} else if (direction == DOWN && no_more_request_beneathe) {\n";
		s += "\t\t\tdirection = UP;\n";
		s += "\t\t}\n";
		s += "\t}\n";
		s += "\t\t// evaluates Direction: (UP=1, Down=-1, None=0)\n";
		s += "\t\tposition += eval(direction);\n";
		s += "}\n";
		s += "}";
		return s;
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return locale;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

}