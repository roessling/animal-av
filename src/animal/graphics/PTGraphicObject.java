package animal.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Hashtable;

import animal.editor.SpecialSelector;
import animal.handler.GraphicObjectHandler;
import animal.main.AnimalConfiguration;
import animal.main.Animation;
import animal.misc.EditableObject;
import animal.misc.MessageDisplay;
import animal.misc.XProperties;

/**
 * Base class for all graphic objects.
 * 
 * Based on <a href="http://www.ahrgr.de/guido/">Guido R&ouml;&szlig;ling</a>'s
 * PTGraphicObject
 * 
 * All objects use homogenous coordinates(see the link to PTPoint below)
 * 
 * @see PTPoint
 * 
 * @author <a href="http://www.algoanim.info/Animal2/">Guido
 *         R&ouml;&szlig;ling</a>
 * @version 1.0 29.08.1998
 */
public abstract class PTGraphicObject extends EditableObject implements
		PropertyChangeListener {
	// =================================================================
	// CONSTANTS
	// =================================================================

	// public static final String COLOR_LABEL = "color";

	// public static final String DEPTH_LABEL = "depth";

	public static final String NAME_LABEL = "name";

	public static final String NODE_LABEL = "node";

	public static final String EMPTY_STRING = "";

	protected boolean objectSelectable = true;
	
	public boolean isObjectSelectable() {
		return objectSelectable;
	}

	public void setObjectSelectable(boolean objectIsSelectable) {
		objectSelectable = objectIsSelectable;
	}

	/**
	 * This constant is used for the file version. It must be incremented whenever
	 * the save format is changed.
	 * 
	 * Versions include:
	 * 
	 * <ol>
	 * <li>original release</li>
	 * <li>all objects now possess a <em>depth</em>
	 * </ol>
	 */
	// protected int fileVersion = 2;
	/**
	 * This constant contains the UID for serialization, <em>do not edit</em>!
	 */
//	private static final long serialVersionUID = -20443585871477440l;

	// =================================================================
	// STATIC ATTRIBUTES
	// =================================================================

	/**
	 * The registered object types, needed for parsing
	 */
	public static Hashtable<String, String> registeredTypes;

	public static Hashtable<String, GraphicObjectHandler> registeredHandlers;

	// =================================================================
	// CLASS INITIALIZER
	// =================================================================

	// initialize the Hashtable
	static {
		registeredTypes = new Hashtable<String, String>(43);
		registeredHandlers = new Hashtable<String, GraphicObjectHandler>(43);
	}

	// =================================================================
	// ATTRIBUTES
	// =================================================================

	/**
	 * the unique number of the graphic object. 0 for subordinate GraphicObject,
	 * i.e. the object is a component of another GraphicObject or a number has not
	 * yet been assigned. 1+ for a top level GraphicObject.
	 */
	protected int num;

	protected Color color = Color.BLACK;

	protected int depth = Integer.MAX_VALUE;

	/**
	 * The (optional) object's name
	 */
	protected String objectName = null;

	/**
	 * The concatenated representation of the numeric object IDs for this object A
	 * small helper which prevents redundant calls.
	 */
	protected String numericIDs;

	/**
	 * This method initializes the primitive with the primitive type's default
	 * values (looked up at the default properties)
	 * 
	 * @param primitiveName
	 *          the name of the primitive to support inheritance, e.g. "Circle".
	 */
	public void initializeWithDefaults(String primitiveName) {
		AnimalConfiguration config = AnimalConfiguration.getDefaultConfiguration();
		depth = config.getDefaultIntValue(primitiveName, "depth", 50);
		color = config.getDefaultColor(primitiveName, "color", Color.BLACK);
	}

	// =================================================================
	// GRAPHIC OPERATIONS
	// =================================================================

	/**
	 * Paints the current graphical object
	 * 
	 * @param g
	 *          the Graphics object used for painting
	 */
	public abstract void paint(Graphics g);

	/**
	 * In the subclasses, changes a property of the GraphicObject. The
	 * <strong>PropertyChangeEvent</strong> includes the method <code>String
	 * getPropertyName()</code> to decide which method to used (this is the same
	 * as returned by <code>getMethods</code> and the methods <code>Object
	 * getOldValue()</code> and <code>Object getNewValue()</code> containing the
	 * value passed in the last call and a new value, their types being the same
	 * as passed to <code>getMethods</code> when the <em>propertyName</em> was
	 * returned. For example, if <code>getPropertyName</code> returns "translate"
	 * the object is of type <strong>Point</strong>.
	 * <p>
	 * The <i>propertyName</i> can be compared by <code>equalsIgnoreCase() to
	 * the method names and then the actual methods be called.
	 * <p>
	 * 
	 * <strong>PTGraphicObject</strong>'s <code>propertyChange</code> method should be called if no appropriate
	 * method is found. It just displays an error message.
	 * 
	 * @see GraphicObjectHandler#getMethods(animal.graphics.PTGraphicObject,
	 *      java.lang.Object)
	 * @see PTBoxPointer#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent e) {
	  GraphicObjectHandler h = getHandler();
		if (h != null)
			h.propertyChange(this, e);
		else
			MessageDisplay.errorMsg("propertyChangePTGOIllegal",
					new String[] { getClass().getName() }, MessageDisplay.CONFIG_ERROR);
	}

	/**
	 * <em>translate(int, int)</em> performs a 2D translation on the given object.
	 * 
	 * The formula needed for performing the transformation can be found in the
	 * Computer Graphics book by Foley/van Dam, page 205, formula 5.12
	 * 
	 * 
	 * The method is abstract and should therefore be implemented by all
	 * subclasses of PTGraphicObject.
	 * 
	 * @param delta_x
	 *          The translation coeeficient for the x-direction. Since we only use
	 *          integer coordinates, this is an integer, too.
	 * 
	 * @param delta_y
	 *          The translation coeeficient for the y-direction. Since we only use
	 *          integer coordinates, this is an integer, too.
	 */
	public abstract void translate(int delta_x, int delta_y);

	// =================================================================
	// ATTRIBUTE GET/SET
	// =================================================================

	/**
	 * returns the base name of the operation
	 * 
	 * @param method
	 *          the method employed
	 */
	public String baseOperationName(String method) {
		return method;
	}

	/**
	 * Returns true if the method is compatible to multiple selections
	 * 
	 * @param method
	 *          the name of the method
	 */
	public boolean isCompatibleMethod(String method) {
		return false;
	}

	/**
	 * This will test the operation passed for allowing multiple node selection.
	 * Most operations will not need this, but some require a selector -
	 * especially the 'move selected / all but selected nodes' animators.
	 * 
	 * @param operation
	 *          the name of the requested operation
	 * @return true if the use of a NodeSelector is required, else false. Note
	 *         that PTGraphicObject always returns <code>false</code>.
	 */
	public boolean enableMultiSelectionFor(String operation) {
		return false;
	}

	/**
	 * Returns the bounding box of the <strong>GraphicObject</strong>. This is the
	 * smallest rectangle that contains all the object.
	 * 
	 * @return the bounding box
	 */
	public abstract Rectangle getBoundingBox();

	/**
	 * Returns this object's color
	 * 
	 * @return the color of the object. What the color actually entails depends on
	 *         the concrete object.
	 */
	public Color getColor() {
		if (color == null)
			color = AnimalConfiguration.getDefaultConfiguration().getDefaultColor(
					getType(), "color", Color.BLACK);
		return color;
	}

	/**
	 * Returns this object's depth
	 * 
	 * @return the depth of the object
	 */
	public int getDepth() {
		if (depth < 0)
			depth = AnimalConfiguration.getDefaultConfiguration().getDefaultIntValue(
					getType(), "depth", 50);
		return depth;
	}

	public int getFileVersion() {
		return 1; // should be overwritten!
	}

	// /**
	// * Return the font for text-containing components, else null
	// *
	// * @return null if no text is present, else the font used
	// */
	// public Font getFont() {
	// return null; // default value
	// }

	public GraphicObjectHandler getHandler() {
	  GraphicObjectHandler handler = registeredHandlers.get(getType());
	  if (handler == null)
	    System.err.println("looking for handler for " +getType() +", @HT:" +registeredHandlers.get(getType())
	        +"\n\t" +registeredHandlers.keySet());
		return registeredHandlers.get(getType());
	}

	/**
	 * Returns the names of the structures this object can parse.
	 * 
	 * This method is left abstract to force clients to implement it properly.
	 * 
	 * @return an array of Strings containing all handled keywords in the stream
	 */
	public abstract String[] handledKeywords();

	/**
	 * Returns the upper left corner of the object's bounding box
	 * 
	 * @return the upper left corner of the object's bounding box
	 */
	public Point getLocation() {
		return getBoundingBox().getLocation();
	}

	/**
	 * returns the number of the graphic object.
	 * 
	 * @param unique
	 *          if <code>true</code> and no number has yet been assigned, returns
	 *          a new unique number. If <code>false</code> and no number has yet
	 *          been assigned, 0 is returned.
	 */
	public int getNum(boolean unique) {
		if (num == 0 && unique) {
			num = Animation.get().getUniqueGraphicObjectNum();
		}
		return num;
	}

	/**
	 * Return the numeric IDs of this object as a String
	 * 
	 * @return the numeric IDs of the object
	 */
	public String getNumericIDs() {
		return numericIDs;
	}

	/**
	 * returns the object's name (or an empty String if no name was specified)
	 * 
	 * @return the object's name or an empty String. Will never return null.
	 */
	public String getObjectName() {
		if (objectName == null || objectName.length() == 0)
			objectName = String.valueOf(getNum(false));
		return objectName;
	}

	//	
	// /**
	// * Return this object's name as a String
	// *
	// * @return the name of the object
	// */
	// public String getObjectName() {
	// if (getProperties() != null) {
	// String name = getProperties().getProperty(
	// mapKey(getType() + "." + NAME_LABEL), null);
	// if (name == null || name.length() == 0) {
	// name = String.valueOf(getNum(false));
	// setObjectName(name);
	// }
	// }
	//
	// return String.valueOf(getNum(false));
	// }

	public abstract String getType();

	// /**
	// * Returns all properties starting with the 'prefix' string. Used to
	// * initialize the default properties of all subtypes.
	// *
	// * @param props
	// * the properties object from which the properties are extracted,
	// * typically taken directly from animal.main.Animal
	// * @param prefix
	// * the prefix for the attributes to extract
	// * @return the selected attributes as an XProperties object
	// */
	// public static XProperties extractDefaultProperties(XProperties props,
	// String prefix) {
	// return props.getElementsForPrefix(prefix);
	// }
	//
	// /**
	// * Initialize the default properties object!
	// *
	// * @param xProperties
	// * the properties object used for initialization
	// */
	// public static void initializeDefaultProperties(
	//  XProperties xProperties) {
	// MessageDisplay.errorMsg("initPropsIllegalAtPTGO",
	// MessageDisplay.RUN_ERROR);
	// }

	/**
	 * This method will test the operation passed in for needing a node selection.
	 * Most operations will not need this, but some require a selector -
	 * especially the 'move selected / all but selected nodes' animators.
	 * 
	 * @param operation
	 *          the name of the requested operation
	 * @return true if the use of a NodeSelector is required, else false. Note
	 *         that PTGraphicObject always returns <code>false</code>.
	 */
	public boolean operationRequiresSpecialSelector(String operation) {
		return false;
	}

	/**
	 * return the special selector for a given operation (if any)
	 * 
	 * @param operation
	 *          the name of the requested operation
	 * @return the special selector, if any; else null
	 */
	public SpecialSelector getSpecialSelectorForOperation(String operation) {
		return null;
	}

	/**
	 * Resets the number after cloning the object
	 */
	public void resetNum() {
		num = 0;
	}

	/**
	 * Set this object's color
	 * 
	 * @param newColor
	 *          the target color of the object. What the color actually entails
	 *          depends on the concrete object.
	 */
	public void setColor(Color newColor) {
		if (newColor != null)
			color = newColor;
		// getProperties().put(mapKey(getType() + ".color"), color);
	}

	/**
	 * Set this object's depth - the higher, the further to the background.
	 * 
	 * @param newDepth
	 *          the depth of the object
	 */
	public void setDepth(int newDepth) {
		// getProperties().put(mapKey(getType() + ".depth"), newDepth);
		// getProperties().put(mapKey(getType() + "." + DEPTH_LABEL), newDepth);
		depth = newDepth;
	}

	// /**
	// * Set the font for text-containing components, else do nothing.
	// *
	// * <strong>Warning:</strong> the default implementation has an empty body!
	// *
	// * @param font
	// * the font to be set
	// */
	// public void setFont( Font font) {
	// // default: no action
	// }

	/**
	 * Translate the object so that the upper left corner will be
	 * <code>targetPoint</code>.
	 * 
	 * @param targetPoint
	 *          the target coordinate of the object's upper left corner
	 */
	public void setLocation(Point targetPoint) {
		Point originPoint = getBoundingBox().getLocation();
		int dx = targetPoint.x - originPoint.x;
		int dy = targetPoint.y - originPoint.y;
		translate(dx, dy);
	}

	/**
	 * sets the object's (optional) name. The parameter may also be null.
	 * 
	 * @param newName
	 *          the object's target name, or null to reset the name.
	 */
	public void setObjectName(String newName) {
		objectName = newName;
	}

	// /**
	// * Set this object's name to the provided value
	// *
	// * @param targetName
	// * the target name of the object
	// */
	// public void setObjectName(String targetName) {
	// getProperties().put(mapKey(getType() + "." + NAME_LABEL), targetName);
	// }

	/**
	 * set the number to the value provided
	 * 
	 * @param value
	 *          the new value of 'num'
	 */
	public void setNum(int value) {
		num = value;
	}

	/**
	 * Set this object's numeric IDs to the provided value
	 * 
	 * @param ids
	 *          the numeric IDs of the object
	 */
	public void setNumericIDs(String ids) {
		numericIDs = ids;
	}

	// public Object safeClone() {
	// PTGraphicObject clonedBaseObject = (PTGraphicObject) clone();
	// clonedBaseObject.setProperties((XProperties) getProperties().clone());
	// return clonedBaseObject;
	// }

	// /**
	// * clones the Text.
	// */
	// public Object clone() {
	// PTGraphicObject ptgo = (PTGraphicObject) super.clone();
	// ptgo.clonePropertiesFrom(getProperties(), true);
	// return ptgo;
	// }

	protected void cloneCommonFeaturesInto(PTGraphicObject ptgo) {
		ptgo.setColor(createColor(color));
		ptgo.setDepth(depth);
		ptgo.setNum(getNum(false));
		if (objectName != null)
			ptgo.setObjectName(new String(objectName));
		else
			ptgo.setObjectName("");
		ptgo.setObjectSelectable(isObjectSelectable());
	}

	protected Color createColor(Color c) {
		if (c == null)
			return Color.BLACK;
		return new Color(c.getRed(), c.getGreen(), c.getBlue());
	}

	// =================================================================
	// I/O
	// =================================================================

	/**
	 * Reset the attributes for this animator for a "clean memory" state.
	 */
	public void discard() {
		numericIDs = null;
	}

	/**
	 * Update the default properties for this object
	 * 
	 * @param defaultProperties
	 *          the properties to be updated
	 */
	public void updateDefaults(XProperties defaultProperties) {
		defaultProperties.put(getType() + ".depth", getDepth());
		defaultProperties.put(getType() + ".color", getColor());
		// rest has to be done in subclasses!
	}
}
