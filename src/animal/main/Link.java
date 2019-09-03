package animal.main;

import java.util.Vector;

import translator.AnimalTranslator;
import animal.animator.Animator;
import animal.animator.TimedAnimator;
import animal.misc.EditableObject;
import animal.misc.XProperties;

/**
 * Link links two(in future extensions perhaps more) steps. The next step can be
 * performed after a certain time or as a reaction to the user pressing the
 * play-button in the AnimationWindow.
 * 
 * @author <a href="http://www.ahrgr.de/guido/">Guido
 *         R&ouml;&szlig;ling </a>
 * @version 1.0 18.07.1998
 */
public class Link extends EditableObject { 
	public static final String CLICK_PROMPT = "Link.clickPrompt";

	public static final String LINK_DURATION = "Link.duration";

	public static final String LINK_MODE = "Link.mode";

	public static final String LINK_NAME = "Link.label";

	public static final String NEXT_STEP_LABEL = "Link.nextStep";

	public static final String REPEAT_COUNT = "Link.repeat";

	public static final String STEP_LABEL = "Link.step";

	public static final String TIME_LABEL = "Link.time";

	public static final String TARGET_OBJECT_ID = "Link.clickTargetID";

	/**
	 * The "first" step. Step 0 must not contain any Animator, but it's always the
	 * step before the first actual step that may contain an Animator.
	 */
	public static final int START = 0;

	/**
	 * when set in <i>toStep </i>, <i>END </i> indicates that <i>fromStep </i> is
	 * the last step in the animation.
	 */
	public static final int END = Integer.MAX_VALUE;

	public static final int ERROR = Integer.MIN_VALUE;

	/**
	 * one of the <i>mode </i> constants. Wait until the play key is pressed.
	 */
	public static final int WAIT_KEY = 1;

	/**
	 * one of the <i>mode </i> constants. Wait until some time(contained in
	 * <i>time </i>) has elapsed.
	 */
	public static final int WAIT_TIME = 2;

	/**
	 * One of the <em>mode</em> constants. Waits until the user has clicked on a
	 * special displayed object.
	 */
	public static final int WAIT_CLICK = 4;

	public static final int REPEAT = 8;

	private int nrOfIterationsLeft = 0;

	public static int getFileVersion() {
		return fileVersion;
	}

	/**
	 * no-arg constructor required for serialization
	 */
	public Link() {
		// do nothing
	}

	/**
	 * initializes a Link between the two steps that waits for user interaction.
	 */
	public Link(int step, int nextStep) {
		setStep(step);
		setNextStep(nextStep);
		setMode(WAIT_KEY);
	}

	public Link(XProperties props) {
		setProperties(props);
		int mode = props.getIntProperty(LINK_MODE, Link.WAIT_KEY);
		if (mode != Link.WAIT_KEY && mode != Link.WAIT_TIME
				&& mode != Link.WAIT_CLICK)
			setMode(Link.WAIT_KEY);
	}

	/**
	 * initializes a Link between the two steps that waits for the given time.
	 */
	public Link(int step, int nextStep, int time) {
		this(step, nextStep);
		setTime(time);
		setMode(WAIT_TIME);
	}

	public String getClickPrompt() {
		return getProperties().getProperty(CLICK_PROMPT,
				AnimalTranslator.translateMessage("clickOnXToContinue",
						Integer.valueOf(getTargetObjectID())));
	}

	public int getDurationInTicks() {
		int durationInMs = 0;
		Vector<Animator> animatorsAtStep = Animation.get().getAnimatorsAtStep(getStep());
		for (Animator animator: animatorsAtStep) {
			if (animator instanceof TimedAnimator) {
				TimedAnimator helper = (TimedAnimator)animator;
				if (!helper.isUnitIsTicks() 
						&& helper.getDuration() + helper.getOffset() > durationInMs)
					durationInMs = helper.getDuration() + helper.getOffset();
			}
		}
		return getProperties().getIntProperty(LINK_DURATION, durationInMs / 50);
	}

	public String getLinkLabel() {
		return getProperties().getProperty(LINK_NAME);
	}

	public int getMode() {
		return getProperties().getIntProperty(LINK_MODE, WAIT_KEY);
	}

	public int getNextStep() {
		return getProperties().getIntProperty(NEXT_STEP_LABEL, END);
	}

	public int getNrOfIterationsLeft() {
		return nrOfIterationsLeft;
	}

	public int getRepeatCount() {
		return getProperties().getIntProperty(REPEAT_COUNT, 0);
	}

	public int getStep() {
		return getProperties().getIntProperty(STEP_LABEL, START);
	}

	public int getTargetObjectID() {
		// if (getMode() == Link.WAIT_CLICK)
		return getProperties().getIntProperty(TARGET_OBJECT_ID, 1);
		// return -1;
	}

	public int getTime() {
		return getProperties().getIntProperty(TIME_LABEL, 0);
	}

	public void setClickPrompt(String message) {
		getProperties().put(CLICK_PROMPT, message);
	}

	public void setDurationInTicks(int duration) {
		if (duration > 0)
			getProperties().put(LINK_DURATION, duration);
	}

	public void setLinkLabel(String label) {
		getProperties().put(LINK_NAME, label);
	}

	public void setMode(int mode) {
		if (mode == Link.WAIT_TIME || mode == Link.WAIT_KEY
				|| mode == Link.WAIT_CLICK)
			getProperties().put(LINK_MODE, mode);
		else
			getProperties().put(LINK_MODE, Link.WAIT_KEY);
	}

	public void setNextStep(int nextStep) {
		getProperties().put(NEXT_STEP_LABEL, nextStep);
	}

	public void setNrOfIterationsLeft(int nr) {
		nrOfIterationsLeft = nr;
	}

	public void setRepeatCount(int nrOfRepeats) {
		getProperties().put(REPEAT_COUNT, nrOfRepeats);
	}

	public void setStep(int step) {
		getProperties().put(STEP_LABEL, step);
	}

	public void setTargetObjectID(int targetID) {
		getProperties().put(TARGET_OBJECT_ID, targetID);
	}

	public void setTime(int time) {
		getProperties().put(TIME_LABEL, time);
	}


	public String getLinkDescription() {
		StringBuilder sb = new StringBuilder(40);
		if (getStep() == END)
			sb.append("----- end -----");
		else {
			sb.append("Step ").append(getStep());
			String linkLabel = getLinkLabel();
			if (linkLabel != null && linkLabel.length() != 0)
				sb.append(" label '").append(linkLabel).append("'");
		}
		return sb.toString();
	}

	public String toString() { // if Link by time, add the "wait" message to the
		// Link info.
		StringBuilder sb = new StringBuilder(40);

		if (getStep() == END)
			sb.append("----- end -----");
		else {
			sb.append("----- Step ").append(getStep());
			int duration = getDurationInTicks();
			if (duration > 0)
				sb.append(" d=").append(duration).append(' ');
			if (getMode() == Link.WAIT_TIME)
				sb.append(" (wait ").append(getTime()).append(" ms afterwards)");
			else if (getMode() == Link.WAIT_CLICK)
				sb.append(" (wait for click on ID ").append(getTargetObjectID())
						.append(')');
			sb.append(" -----");
			String linkLabel = getLinkLabel();
			if (linkLabel != null && linkLabel.length() != 0)
				sb.append(" label '").append(linkLabel).append("'");
		}
		return sb.toString();
	}

	private static final int fileVersion = 2;

	public void discard() {
		setLinkLabel(null);
	}
}
