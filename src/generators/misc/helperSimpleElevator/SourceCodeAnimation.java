package generators.misc.helperSimpleElevator;

import java.awt.Color;
import java.awt.Font;

import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.Timing;

public class SourceCodeAnimation {
	private SourceCode sc;
	private SourceCodeProperties scProps;
	private Language lang;
	private String last_highlight = null;
	
	public static final String PUBLLIC_VOID_PSEUDOCODE = "0";
	public static final String WHILE_TRUE  =" 1";
	public static final String IF_POSITION_EQ_A_POS_OF_REQUEST = "2";
	public static final String WAIT_FOR_PASSANGERS_TO_GET_IN_OR_OUT = "3";
	public static final String CHECK_REQUESTS = "4";
	public static final String IF_PASSANGERS_WENT_IN_OR_OUT = "41";
	public static final String SKIP = "42";
	public static final String IF_DIRECTION_EQ_NONE = "5";
	public static final String IF_REQUESTS_ABOVE = "6";
	public static final String DIRECTION_SET_UP = "7";
	public static final String ELSE_IF_REQUEST_BENEATHE = "8";
	public static final String DIRECTION_SET_DOWN = "9";
	public static final String ELSE_2 = "12";
	public static final String IF_NO_MORE_REQUESTS_ABOVE_AND_NO_MORE_REQUESTS_BENEATHE = "121";
	public static final String DIRECTION_SET_NONE_2 = "122";
	public static final String IF_DIRECTION_EQ_UP_AND_NO_MORE_REQUESTS_ABOVE = "13";
	public static final String DIRECTION_SET_DOWN_2 = "14";
	public static final String ELSE_IF_DIRECTOIN_EQ_DOWN_AND_NO_MORE_REQUESTS_BENEATHE = "15";
	public static final String DIRECTION_SET_UP_2 = "16";
	public static final String POSITION_PE_EVAL_DIRECTION = "17";
	
	public SourceCodeAnimation (Language lang, Coordinates coord, Timing t) {
		this.lang = lang;
		sc = lang.newSourceCode(coord, "SourceCodeAnimation", null);
		
		scProps = new SourceCodeProperties();
		setupProperties();
		sc = lang.newSourceCode(coord, "sourcecode", t, scProps);
		setupSourceCode();
	}
	
	private void setupProperties() {
		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLACK);
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));

		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
	}

	public void toggle(String on, boolean nextStep) {
		if(last_highlight != null)
			sc.unhighlight(last_highlight);
		if(on != null) {
			sc.highlight(on);
			last_highlight = on;
		}
		if(nextStep)
			lang.nextStep();
	}
	
	public void unhighlight(String off, boolean nextStep) {
		if(off!=null) {
			sc.unhighlight(off);
			last_highlight = null;
		}
		if(nextStep) {
			lang.nextStep();
		}
	}

	private SourceCode setupSourceCode() {
		// 00 public void pseudocode4Elevator() {
		// 01 while(true) {
		// 02 checkRequests();
		// 03 if(position == a_pos_of_a_request) {
		// 04 wait_for_passangers_to_get_in_or_out();
		//041 skip()
		// 06 if (direction == NONE) {
		// 07 if(requests_above) {
		// 08 direction = UP;
		// 09 } else if (request_beneathe) {
		// 10 direction = DOWN;
		// 13 }
		// 14 } else {
		//141 if(no_more_requests_above && no_more_request_beneathe) {
		//142 direction = NONE;		
		// 15 } else if(direction == UP && no_more_requests_above) {
		// 16 direction = DOWN;
		// 17 } else if (direction == DOWN && no_more_request_beneathe) {
		// 18 direction = UP;
		// 19 }
		// 20 }
		// 21 position += eval(direction);
		// 22 }
		// 23 }

		final int dist = 3;

		sc.addCodeLine("public void pseudocode4Elevator() {", PUBLLIC_VOID_PSEUDOCODE, 0, null);
		sc.addCodeLine("while(true) {", WHILE_TRUE, dist, null);
		sc.addCodeLine("checkRequests()", CHECK_REQUESTS, 2*dist, null);
		sc.addCodeLine("if(position == a_pos_of_a_request) {", IF_POSITION_EQ_A_POS_OF_REQUEST, 2 * dist, null);
		sc.addCodeLine("wait_for_passangers_to_get_in_or_out();", WAIT_FOR_PASSANGERS_TO_GET_IN_OR_OUT, 3 * dist, null);
		sc.addCodeLine("skip();", SKIP, 3*dist, null);
		sc.addCodeLine("}", null, 2 * dist, null);
		//sc.addCodeLine("if(passangers_went_in_or_out) {", IF_PASSANGERS_WENT_IN_OR_OUT, 2*dist, null);
		//sc.addCodeLine("}", null, 2*dist, null);
		sc.addCodeLine("if (direction == NONE) {", IF_DIRECTION_EQ_NONE, 2 * dist, null);
		sc.addCodeLine("if(requests_above) {", IF_REQUESTS_ABOVE, 3 * dist, null);
		sc.addCodeLine("direction = UP;", DIRECTION_SET_UP, 4 * dist, null);
		sc.addCodeLine("} else if (request_beneathe) {", ELSE_IF_REQUEST_BENEATHE, 3 * dist, null);
		sc.addCodeLine("direction = DOWN;", DIRECTION_SET_DOWN, 4 * dist, null);
		sc.addCodeLine("}", null, 3 * dist, null);
		sc.addCodeLine("} else {", ELSE_2, 2 * dist, null);
		sc.addCodeLine("if(no_more_requests_above && no_more_request_beneathe) {", IF_NO_MORE_REQUESTS_ABOVE_AND_NO_MORE_REQUESTS_BENEATHE, 3 * dist, null);
		sc.addCodeLine("direction = NONE;", DIRECTION_SET_NONE_2, 4 * dist, null);
		sc.addCodeLine("} else if (direction == UP && no_more_requests_above) {", IF_DIRECTION_EQ_UP_AND_NO_MORE_REQUESTS_ABOVE, 3 * dist, null);
		sc.addCodeLine("direction = DOWN;", DIRECTION_SET_DOWN_2, 4 * dist, null);
		sc.addCodeLine("} else if (direction == DOWN && no_more_request_beneathe) {", ELSE_IF_DIRECTOIN_EQ_DOWN_AND_NO_MORE_REQUESTS_BENEATHE, 3 * dist, null);
		sc.addCodeLine("direction = UP;", DIRECTION_SET_UP_2, 4 * dist, null);
		sc.addCodeLine("}", null, 3 * dist, null);
		sc.addCodeLine("}", null, 2 * dist, null);
		sc.addCodeLine("// evaluates Direction: (UP=1, Down=-1, None=0)", null, 2*dist, null);
		sc.addCodeLine("position += eval(direction);", POSITION_PE_EVAL_DIRECTION, 2*dist, null);
		sc.addCodeLine("}", null, dist, null);
		sc.addCodeLine("}", null, 0, null);
		return sc;
	}


	
}
