/*
 * Highlight.java
 *
 * Created on 14. Dezember 2005, 19:17
 *
 * @author Michael Schmitt
 *
 * @version 0.2.3
 * @date 2006-03-27
 * 
 * modified 2006-09-21 by Pierre Villette
 */

package animal.animator;

import java.awt.Color;

import animal.main.AnimalConfiguration;
import animal.main.AnimationState;
import animal.misc.XProperties;

public class HighlightEdge extends TimedAnimator implements
		GraphicObjectSpecificAnimation {

	// =========
	// CONSTANTS
	// =========

	/**
	 * The name of this animator
	 */
	public static final String TYPE_LABEL = "HighlightEdge";

	public static final String REAL_DEACTIVATE = "doRealCellDeactivation";

	/**
	 * Store the current highlighting state of the array cells <code>true</code>
	 * if the cell shall be highlighted, <code>false</code> otherwise.
	 */
	private boolean[][] hlState;

	/**
	 * The mode used by the 'deactivate' effect. <code>true</code> should really
	 * disable working with deacivated cells, while <code>false</code> should
	 * ignore the activation state used in other animators.
	 */
	private static boolean realDeactivation;

	// ============
	// CONSTRUCTORS
	// ============

	/** Creates a new instance of Highlight */
	public HighlightEdge() {
		if (DefaultProperties == null) {
			initializeDefaultProperties(AnimalConfiguration.getDefaultConfiguration()
					.getProperties());
		}
		setProperties((XProperties) DefaultProperties.clone());
		realDeactivation = getProperties().getBoolProperty(mapKey(REAL_DEACTIVATE),
				false);
	}

	public HighlightEdge(int length) {
		this();
		setHighlightState(length - 1, length - 1, false);
	}

	// ===============
	// PROPERTY ACCESS
	// ===============

	public void init(AnimationState animationState, long time, double ticks) {
		super.init(animationState, time, ticks);
		// if ((getObjectNums () != null) && (getObjectNums ().length > 0)) {
		// int [] objectNums = getObjectNums ();
		// for (int num = 0; num < objectNums.length; num++) {
		// PTGraphicObject ao = animationState.getCloneByNum (objectNums [num]);
		// if (ao instanceof PTArray) {
		// for (int i = ((PTArray) ao).length - 1; i >= 0; i--) {
		// setHighlightState (i, ((PTArray) ao).isElemHighlighted (i));
		//
		//
		// // ??? insert the highlighted objects into the animation???
		// }
		// }
		// }
		// }
	}

	/**
	 * Returns the property at a certain time. getProperty <em>must</em> return
	 * a property of the "normal" type (i.e. Move must always return a Point),
	 * even if the object is not completely initialized (then return a dummy!), as
	 * TimedAnimatorEditor relies on receiving a property for querying the
	 * possible methods.
	 * 
	 * @param factor
	 *          a value between 0 and 1, indicating how far this animator has
	 *          got(0: start, 1: end)
	 * @return the object at the given time. A return value of -1 in any cell
	 *         means that this cell is not affected by the animator in this step!
	 * @see animal.editor.animators.TimedAnimatorEditor
	 */
	public Object getProperty(double factor) {
		int length = 1;
		if ((hlState != null) && (hlState.length > 0)) {
			length = hlState.length;
		}
		double[][] states = new double[length][length];
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				states[i][j] = (getHighlightState(i, j) ? factor : -1);
			}
		}
		return states;
	}

	/**
	 * Get the current cell highlight state of the chosen cell
	 * 
	 * @param index1 the start node of the chosen edge
	 * @param index2 the end node of the chosen edge
	 * @return the cell highlighting
	 */
	public boolean getHighlightState(int index1, int index2) {
		if ((hlState == null) || (index1 < 0) || (index1 >= hlState.length)
				|| (index2 < 0) || (index2 >= hlState.length)) {
			return false;
		} 
		return hlState[index1][index2];
	}

	/**
	 * Get the current cell highlight state of all the cells currently stored in
	 * this animator.
	 * 
	 * @return an array of the highlight states
	 * 
	 * ATTENTION! It is possible, that the returned array is null or its length
	 * differs from the length of the currently chosen arrays in the
	 * HighlightEditor.
	 */
	public boolean[][] getHighlightState() {
		return hlState;
	}

	/**
	 * Set the highlight state of the chosen cell to the provided state.
	 * 
	 * @param index1 the start node of the chosen edge
	 * @param index2 the end node of the chosen edge
	 */
	public void setHighlightState(int index1, int index2, boolean state)
			throws IndexOutOfBoundsException {
		int index = Math.max(index1, index2);
		if (index < 0)
			throw new IndexOutOfBoundsException(
					"Try to access a negative array index."); // IndexOutOfBoundsException;
		else if (hlState == null) {
			hlState = new boolean[index + 1][index + 1];
			// Note that the last value is set at the end of this method!
			for (int i = 0; i < index; i++) {
				for (int j = 0; j < index; j++) {
					hlState[i][j] = false;
				}
			}
		} else if (index >= hlState.length) {
			boolean[][] temp = hlState;
			hlState = new boolean[index][index];
			for (int i = 0; i < temp.length; i++) {
				for (int j = 0; j < temp.length; j++) {
					hlState[i][j] = temp[i][j];
				}
			}
			// Note that the last value is set at the end of this method!
			for (int i = 0; i < hlState.length; i++) {
				for (int j = temp.length; j < index; j++) {
					hlState[i][j] = false;
				}
			}
			for (int i = hlState.length; i < index; i++) {
				for (int j = 0; j < index; j++) {
					hlState[i][j] = false;
				}
			}
		}
		hlState[index1][index2] = state;
	}

	/**
	 * Set the highlight states for all cells to the provided states.
	 * 
	 * @param states the array of the cell highlight states
	 */
	public void setHighlightState(boolean[][] states) {
		hlState = states;
	}

	/**
	 * Get the mode for deactivated cells.
	 * 
	 * All animators referring to this class should proceed as follows: 
	 * If <code>realDeactivation</code> is <em>set</em>, the animator should 
	 * disable the cells that are deactivated and only allow modifying
	 * activated cells. If <em>not set</em>, the animator should ignore
	 * the activation state of the cells.
	 * 
	 * @return the type of deactivation
	 */
	public static boolean realDeactivation() {
		return realDeactivation;
	}


	/**
	 * Initialize the default properties object!
	 * 
	 * @param properties
	 *          the properties object used for initialization
	 */
	public static void initializeDefaultProperties(XProperties properties) {
		DefaultProperties = extractDefaultProperties(properties, "HighlightEdge");
	}

	/**
	 * Get the type of this animator as "Highlight"
	 * 
	 * @return the name of this animator
	 */
	public String getType() {
		return TYPE_LABEL;
	}

	/**
	 * Returns the keywords of Animal's ASCII format this animator handles.
	 * 
	 * @return a String array of the keywords handled by this animator.
	 */
	public String[] handledKeywords() {
		return new String[] { "HighlightEdge", "UnhighlightEdge" };
	}

	/**
	 * Returns the name of this animator, used for saving.
	 * 
	 * @return the name of the animator.
	 */
	public String getAnimatorName() {
		return "HighlightEdge";
	}

	/**
	 * Calculate the current color depending on the previous color, the desired
	 * final color and the part of the fading effect.
	 * 
	 * This method is required by the highlight animator.
	 * 
	 * @param old
	 *          the previous color
	 * @param now
	 *          the destination color
	 * @param part
	 *          the part of the fading from old to now
	 * 
	 * @return the current color value
	 */
	public static Color fadeColor(Color old, Color now, double part) {
		if ((part < 0) || (part > 1)) {
			return old;
		} 
		int r, g, b;
		r = (int) (part * now.getRed() + (1 - part) * old.getRed());
		g = (int) (part * now.getGreen() + (1 - part) * old.getGreen());
		b = (int) (part * now.getBlue() + (1 - part) * old.getBlue());
		return new Color(r, g, b);
	}

	public String toString() {
		if ((hlState == null) || (hlState.length == 0)) {
			return "No highlight element set!";
		}
		int counter = 0;
		String toString = new String();
		for (int i = hlState.length - 1; i >= 0; i--) {
			for (int j = hlState.length - 1; j >= 0; j--) {
				if (hlState[i][j]) {
					if (counter >= 1) {
						toString = String.valueOf(i) + " " + String.valueOf(j) + ", "
								+ toString;
						counter++;
					} else {
						toString = String.valueOf(i) + " " + String.valueOf(j);
						counter++;
					}
				}
			}
		}
		toString = getMethod() + " " + toString + " of " + super.toString();
		return toString;
	}

	/**
	 * Get the graphic object types on which the animator works.
	 * 
	 * @return an array of the supported types
	 */
	public String[] getSupportedTypes() {
		return new String[] { animal.graphics.PTGraph.TYPE_LABEL };
	}
}