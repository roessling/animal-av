package extras.lifecycle.common;

import javax.swing.event.EventListenerList;

import extras.lifecycle.common.AbstractListener;
import extras.lifecycle.common.Event;


public abstract class AbstractObservable<T extends AbstractListener> {
	
	protected EventListenerList listenerList = new EventListenerList();
	final private Class<T> clazz;
	
	 public AbstractObservable(Class<T> clazz) {
		super();
		this.clazz = clazz;
	}

	public void addListener(T l) {
	     listenerList.add(clazz, l);
	 }

	 public void removeListener(T l)  {
	     listenerList.remove(clazz, l);
	 }
	 
	  /** 
	   * Notifies all {@code AdListener}s that have registered interest for 
	   * notification on an {@code AdEvent}. 
	   * @param event  the {@code AdEvent} object 
	   * @see EventListenerList 
	   */ 
	  protected synchronized void fireEvent(Event event) 
	  { 
	    for ( T l : listenerList.getListeners(clazz) ) 
	    	l.onEvent(event);
	  }

}
