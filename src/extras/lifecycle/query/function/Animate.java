package extras.lifecycle.query.function;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import extras.lifecycle.checkpoint.Checkpoint;
import extras.lifecycle.checkpoint.CheckpointedEvent;
import extras.lifecycle.common.AnimationStepBean;
import extras.lifecycle.query.EvaluationMode;
import extras.lifecycle.query.Knowledge;
import extras.lifecycle.query.workflow.Function;

/**
 * @author Mihail Mihaylov
 * 
 */
@XmlRootElement(name = "Animate")
public class Animate extends Function {

	/**
	 * 
	 */
	public Animate() {
		super();
	}

	static final String ANIMSTEP = "animstep";

	@SuppressWarnings("unchecked")
	@Override
	public Object calculate(Knowledge knowledge) {

		List<AnimationStepBean> animationSteps = new LinkedList<AnimationStepBean>();
		List<Object> interestingEvents = new LinkedList<Object>();
		String label = null;

		List<Object> extendedArguments = extractFlatArgumentsList(arguments);

		// We add all Checkpoint-s and CheckpointedEvent-s as interesting events
		for (Object argument : extendedArguments) {
			if ((argument instanceof CheckpointedEvent)
					|| (argument instanceof Checkpoint)) {
				interestingEvents.add(argument);
			} else if (argument instanceof String) {
				label = (String) argument;
			}
			// else it is ignored
		}

		for (Object interestingEvent : interestingEvents) {
			Object animstep = null;
			String animLabel = null;
			if (interestingEvent instanceof CheckpointedEvent) {
				CheckpointedEvent ce = (CheckpointedEvent) interestingEvent;
				animstep = ce.get(ANIMSTEP);
				if (label == null)
					animLabel = ce.getName();
				else
					animLabel = label;
			} else if (interestingEvent instanceof Checkpoint) {
				Checkpoint c = (Checkpoint) interestingEvent;
				animstep = c.get(ANIMSTEP);
				if (label == null)
					animLabel = "Checkpoint";
				else
					animLabel = label;
			}

			if (animstep != null) {
				// -----
				int animStepsInt = animstep.hashCode();
				if (animstep instanceof Integer)
					animStepsInt = (Integer) animstep;

				animationSteps.add(new AnimationStepBean(animLabel,
						animStepsInt));
			}
		}

		StringBuffer sb = new StringBuffer();
		boolean addComma = false;
		for (AnimationStepBean asb : animationSteps) {
			if (addComma)
				sb.append(',');
			addComma = true;
			sb.append(asb.toString());
		}

		String s = sb.toString();
		// In Debug mode we show animate as comment
		if (EvaluationMode.Debug.equals(knowledge.getEvaluationMode())) {
			if (!s.isEmpty())
				knowledge.addComment("Animate: " + s);

		}

		// In visualization mode, we add all animation steps to the result data
		if (EvaluationMode.Animate.equals(knowledge.getEvaluationMode())) {
			// System.err.println("Visualization mode: " + animationSteps);

			Object resultData = knowledge.getResultData();
			if (resultData == null)
				resultData = new ArrayList<AnimationStepBean>();

			if (resultData instanceof Collection<?>) {
				((Collection<Object>) resultData).addAll(animationSteps);
			}

			knowledge.setResultData(resultData);
		}

		return s;
	}

	@SuppressWarnings("unchecked")
	private List<Object> extractFlatArgumentsList(List<Object> arguments) {
		LinkedList<Object> result = new LinkedList<Object>();
		for (Object object : arguments) {
			// If the object itself is a list, we recursively extract its
			// elements
			if (object instanceof List)
				result.addAll(extractFlatArgumentsList((List<Object>) object));
			else
				result.add(object);
		}
		return result;
	}

}
