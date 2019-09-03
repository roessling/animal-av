package extras.animalsense.ui.show;

import java.util.List;

import extras.animalsense.evaluate.Question;
import extras.lifecycle.common.Variable;

public class SetUpAndVisualize extends SetUpVariablesEvent {

	public SetUpAndVisualize(Question question) {
		super(question);
	}

	/**
	 * @param question
	 * @param variables
	 */
	public SetUpAndVisualize(Question question, List<Variable> variables) {
		super(question, variables);
	}

	

}
