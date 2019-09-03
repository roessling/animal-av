package extras.animalsense.evaluate;

import extras.lifecycle.common.AbstractListener;

/**
 * A simple listener for the evaluator with only one method.
 * @author Mihail Mihaylov
 *
 */
public interface EvaluatorListener extends AbstractListener {
	
    /**
     * Invoked when an event occurs.
     */
	void onMessage(String message);
	
}
