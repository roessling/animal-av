package extras.lifecycle.common;

import java.util.EventListener;

import extras.lifecycle.common.Event;

/**
 * A simple listener with only one method.
 * @author Mihail Mihaylov
 *
 */
public interface AbstractListener extends EventListener {
	
    /**
     * Invoked when an event occurs.
     */
	void onEvent(Event event);
	

}
