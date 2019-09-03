package animal.handler;

import java.beans.PropertyChangeEvent;
import java.util.Hashtable;
import java.util.Vector;

import translator.AnimalTranslator;
import animal.animator.Animator;
import animal.graphics.PTBoxPointer;
import animal.graphics.PTGraphicObject;
import animal.misc.MessageDisplay;

public abstract class GraphicObjectHandler {
	protected static Hashtable<String, Vector<GraphicObjectHandlerExtension>> registeredHandlerExtensions;

	static {
		registeredHandlerExtensions = new Hashtable<String, Vector<GraphicObjectHandlerExtension>>(47);
	}

	public void addExtensionMethodsFor(PTGraphicObject ptgo, Object object,
			Vector<String> methodVector) {
		if (registeredHandlerExtensions != null) {
			String key = ptgo.getType();
			Object o = registeredHandlerExtensions.get(key);
			if (o != null && o instanceof Vector) {
				Vector<?> v = (Vector<?>) o;
				Vector<String> extensionVector = null;
				Object possibleHandler = null;
				GraphicObjectHandlerExtension extensionHandler = null;
				for (int i = 0; i < v.size(); i++) {
					possibleHandler = v.elementAt(i);
					if (possibleHandler != null
							&& possibleHandler instanceof GraphicObjectHandlerExtension) {
						extensionHandler = (GraphicObjectHandlerExtension) possibleHandler;
						extensionVector = extensionHandler.getMethods(ptgo, object);
						methodVector.addAll(extensionVector);
						extensionVector = null;
						extensionHandler = null;
					}
				}
			}
		}
	}

	public abstract Vector<String> getMethods(PTGraphicObject ptgo, Object o);

	public static void insertHandlerExtension(
			GraphicObjectHandlerExtension handler) {
		// if the handler does not exist, exit
		if (handler == null)
			return;

		// ensure that the hashtable exists
		if (registeredHandlerExtensions == null)
			registeredHandlerExtensions = new Hashtable<String, Vector<GraphicObjectHandlerExtension>>(47);

		// check if entries are present
		String key = handler.getType();
		Vector<GraphicObjectHandlerExtension> entryVector = null;
		if (!registeredHandlerExtensions.containsKey(key)) {
			entryVector = new Vector<GraphicObjectHandlerExtension>(50, 15);
			entryVector.addElement(handler);
			registeredHandlerExtensions.put(key, entryVector);
		} else {
			entryVector = registeredHandlerExtensions.get(key);
			entryVector.addElement(handler);
		}
	}

	/**
	 * In the subclasses, changes a property of the GraphicObject. The
	 * <strong>PropertyChangeEvent</strong> includes the method <code>String
	 * getPropertyName()</code> to decide which method to used (this is the same
	 * as returned by <code>getMethods</code> and the methods <code>Object
	 * getOldValue()</code> and <code>Object getNewValue()</code> containing
	 * the value passed in the last call and a new value, their types being the
	 * same as passed to <code>getMethods</code> when the <em>propertyName</em>
	 * was returned. For example, if <code>getPropertyName</code> returns
	 * "translate" the object is of type <strong>Point</strong>.
	 * <p>
	 * The <i>propertyName</i> can be compared by <code>equalsIgnoreCase() to
	 * the method names and then the actual methods be called.
	 * <p>
	 * 
	 * <strong>PTGraphicObject</strong>'s <code>propertyChange</code> method
	 * should be called if no appropriate method is found. It just displays an
	 * error message.
	 * 
	 * @see #getMethods
	 * @see PTBoxPointer#propertyChange
	 */
	public void propertyChange(PTGraphicObject ptgo, PropertyChangeEvent e) {
		String what = e.getPropertyName();
		if (!what.equalsIgnoreCase("hide") && !what.equalsIgnoreCase("show")) {
			Object v = null;
			if (registeredHandlerExtensions != null
					&& (v = registeredHandlerExtensions.get(ptgo.getType())) != null
					&& v instanceof Vector) {
				Vector<?> handlers = (Vector<?>) v;
				for (int i = 0, nr = handlers.size(); i < nr; i++)
					if (handlers.elementAt(i) instanceof GraphicObjectHandlerExtension) {
						((GraphicObjectHandlerExtension) handlers.elementAt(i))
								.propertyChange(ptgo, e);
					}
			} else {
				StringBuilder sb = new StringBuilder(200);
				if (e.getSource() instanceof Animator)
					sb.append("Step ").append(((Animator) e.getSource()).getStep())
							.append(": ");
				sb.append(AnimalTranslator.translateMessage(
						"propertyChangeNotSupported", new Object[] { getClass().getName(),
								e.getPropertyName() }));
				MessageDisplay.errorMsg(sb.toString(), MessageDisplay.PROGRAM_ERROR);
			}
		}
	}
}
