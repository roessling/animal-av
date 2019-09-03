/*
 * Put.java
 * Animator to enter a new value into a String- or IntArray
 *
 * Created on 29. November 2005, 17:25
 *
 * @author Michael Schmitt
 * @version 0.2.5
 * @date 2006-02-24
 */

package animal.animator;

import java.awt.Font;
import java.awt.Point;

import translator.AnimalTranslator;
import animal.graphics.PTGraphicObject;
import animal.graphics.PTLine;
import animal.graphics.PTText;
import animal.graphics.meta.PTArray;
import animal.gui.GraphicVector;
import animal.gui.GraphicVectorEntry;
import animal.main.Animation;
import animal.main.AnimationState;
import animal.misc.MessageDisplay;
import animal.misc.XProperties;

public class Put extends TimedAnimator implements
		GraphicObjectSpecificAnimation {

  // =================================================================
	// CONSTANTS
	// =================================================================

	public static final String TYPE_LABEL = "Put";

	/**
	 * Store the new value of the array cell.
	 */
	private PTText newContent;

	/**
	 * The line along which the objects move
	 */
	private PTLine moveLine;

	/**
	 * The moves for the old cell content
	 */
	private Move oldFlyUp = new Move();

	/**
	 * The moves for the old cell content
	 */
	private Move newFlyUp = new Move();

	/**
	 * What is object number of the currently animated array
	 */
	private int animObjNum;

	/**
	 * Index of the array cell which is to be changed.
	 */
	private int cell = -1;

	/**
	 * Stores the font of the array entries that must be set to the new entry when
	 * the animation is executed completely.
	 */
	private Font finalFont = new Font("Monospaced", Font.PLAIN, 14);

	// =================================================================
	// TRANSIENTS
	// =================================================================

	/**
	 * The <code>GraphicVector</code> on which the animator works.
	 */
	private transient GraphicVector graphicVector;

	// =================================================================
	// CONSTRUCTORS
	// =================================================================

	/**
	 * Public (empty) constructor required for serialization.
	 */
	public Put() {
		// do nothing; only used for serialization
	}

	public Put(XProperties props) {
		setProperties(props);
	}

	public Put(int step, int objectNum, int totalTimeOrTicks, int offset,
			int newVal, int idx) {
		super(step, objectNum, totalTimeOrTicks, offset, "put");
		initPut(offset, objectNum, idx, String.valueOf(newVal));
	}
	
	public Put(int step, int objectNum, int totalTimeOrTicks, int offset,
			String newVal, int idx) {
		super(step, objectNum, totalTimeOrTicks, offset, "put");
		initPut(offset, objectNum, idx, newVal);
	}
	
	private void initPut(int offset, int objectNum, int index, String newVal) {
		setOffset(offset);
		PTArray theArray = (PTArray) Animation.get().getGraphicObject(objectNum);
		setArray(objectNum);
		//animObjNum = objectNum;
		if (theArray == null)
			return;
		finalFont = theArray.getFont();
		setCell(index);
		//cell = index;
		setContent(newVal);
//		newContent = new PTText(newVal, finalFont);
//		newContent.setObjectSelectable(false);
//		newContent.setColor(theArray.getFontColor());
//		newContent.setDepth(theArray.getEntry(index).getDepth());
	}


	/**
	 * Set the initial values of the animation.
	 */
	public void init(AnimationState animationState, long time, double ticks) {
		// initializes TimedAnimator and calls getProperty (0);
		super.init(animationState, time, ticks);

		// insert the new content into the current Vector of animated objects
		graphicVector = animationState.getCurrentObjects();
		graphicVector.addElement(newContent, GraphicVectorEntry.CREATED);

		newContent.setFont(new Font(finalFont.getName(), finalFont.getStyle(), 0));

		PTGraphicObject ao = animationState.getCloneByNum(getObjectNums()[0]);
		if (ao instanceof PTArray) {
			Point node = ((PTArray)ao).getEntry(cell).getLocation();
			moveLine.setLastNode(node);
			moveLine.setFirstNode(node.x, node.y + ((PTArray)ao).getBoundingBox(cell).height);
			newContent.setColor(((PTArray) ao).getFontColor());
			newContent.setDepth(((PTArray) ao).getEntry(cell).getDepth());
			newContent.setPosition(moveLine.getPointAtLength(0));
		}
		oldFlyUp.init(animationState, time, ticks);
		newFlyUp.init(animationState, time, ticks);
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
	 * @return the object at the given time.
	 * @see animal.editor.animators.TimedAnimatorEditor
	 */
	public Object getProperty(double factor) {
		if (newContent == null) {
			return new PutType();
		}
		double realFactor = (factor != 0.0) ? factor : 0.0001;
		newContent.setFont(new Font(finalFont.getName(), finalFont.getStyle(),
				(int) (realFactor * finalFont.getSize())));
		newContent.setPosition((Point) newFlyUp.getProperty(realFactor));
		newContent.setLocation((Point) newFlyUp.getProperty(realFactor));
		newContent.setObjectSelectable(false);
		return new PutType(cell, (Point) oldFlyUp.getProperty(realFactor), newContent,
				(Point) newFlyUp.getProperty(realFactor), factor, hasFinished());
	}

	/**
	 * Returns the new cell content that is to be set
	 * 
	 * @return the new content
	 */
	public String getContent() {
		return (newContent == null) ? "" : newContent.getText();
	}

	/**
	 * Set the cell content that shall be entered.
	 * 
	 * @param val
	 *          the new cell value
	 */
	public void setContent(String val) {
		if (moveLine == null) {
			moveLine = new PTLine(0, 0, 0, 0);
			Animation.get().insertGraphicObject(moveLine);
			moveLine.setObjectSelectable(false);
		}

		if (newContent == null) {
			newContent = new PTText(val, finalFont);
			newContent.getNum(true);
			newContent.setObjectSelectable(false);
			Animation.get().insertGraphicObject(newContent);
		} else {
			newContent.setText(val);
			newContent.setFont(finalFont);
		}

		PTGraphicObject go = Animation.get().getGraphicObject(animObjNum);
		if (go instanceof PTArray) {
			newContent.setColor(((PTArray) go).getFontColor());
			newContent.setDepth(((PTArray) go).getEntry(cell).getDepth());
		}

		newFlyUp = new Move(getStep(), newContent.getNum(true), getDuration(),
				getMethod(), moveLine.getNum(true));
	}
	
	public PTText getNewContent(){
		return newContent;
	}

	public void setArray(int num) {
		updateAnimation(animObjNum, num, cell, cell);
		animObjNum = num;
	}

	/**
	 * Enter the index of the cell where the new content shall be entered.
	 */
	public void setCell(int index) {
		updateAnimation(animObjNum, animObjNum, cell, index);
		cell = index;
	}

	/**
	 * Replace the animated objects in the Animation if necessary
	 * 
	 * @param oldObject
	 *          the ID of the previously animated object, i.e. array
	 * @param newObject
	 *          the ID of the currently animated object
	 * @param oldIndex
	 *          the previous index of the animated cell
	 * @param newIndex
	 *          the index of the cell that shall be animated now
	 */
	public void updateAnimation(int oldObject, int newObject, int oldIndex,
			int newIndex) {
		if ((oldObject != newObject) || (oldIndex != newIndex)) {
			if (moveLine == null) {
				moveLine = new PTLine(0, 0, 0, 0);
				Animation.get().insertGraphicObject(moveLine);
				moveLine.setObjectSelectable(false);
			}

			if ((oldFlyUp == null) || (oldObject != newObject)) {
				oldFlyUp = new Move(getStep(), newObject, getDuration(), getMethod(),
						moveLine.getNum(true));
			} else {
				oldFlyUp.setDuration(getDuration());
			}
		}
	}

	/**
	 * Get the index of the modified cell.
	 */
	public int getCell() {
		return cell;
	}

	/**
	 * Export the IDs of the additional graphic objects that were created by this
	 * animator. This method should only be used by the PutExporter!
	 * 
	 * @return the used IDs
	 */
	public int[] exportIDs() {
		return new int[] { newContent.getNum(true), moveLine.getNum(true) };
	}

	/**
	 * Restore the IDs of the additionally created objects to avoid overwriting of
	 * graphic objects in the animation.
	 * 
	 * @param idList
	 *          the IDs to be restored, must be of length 3!
	 */
	public void restoreIDs(int[] idList) {
		if (idList.length != 2) {
			MessageDisplay.errorMsg(AnimalTranslator.translateMessage("putIDListLengthWrong"),
					MessageDisplay.RUN_ERROR);
		} else {
			newContent = (PTText) Animation.get().getGraphicObject(idList[0]);
			newContent.setObjectSelectable(false);
			moveLine = (PTLine) Animation.get().getGraphicObject(idList[1]);
			moveLine.setObjectSelectable(false);
		}
	}

	/**
	 * Returns the name of this animator, used for saving.
	 * 
	 * @return the name of the animator.
	 */
	public String getAnimatorName() {
		return "Put";
	}

	/**
	 * Get the type of this animator as "Put"
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
		return new String[] { "Put" };
	}

	public void discard() {
		if (Animation.get().getGraphicObjects().contains(newContent))
			Animation.get().deleteGraphicObject(newContent.getNum(true));
		PTArray go = (PTArray) Animation.get().getGraphicObject(animObjNum);
		if (go != null) {
			if (Animation.get().getGraphicObjects().contains(go.getEntry(cell)))
				Animation.get().deleteGraphicObject(go.getEntry(cell).getNum(true));
		}
		newContent.discard();
		moveLine.discard();
		finalFont = null;
		super.discard();
	}

	/**
	 * Set the font that has to be set to the new content when the animation has
	 * finished. This must be set equal to the font set in the concerned array.#
	 * 
	 * @param font
	 *          the font set in the array
	 */
	public void setFinalFont(Font font) {
		finalFont = new Font(font.getName(), font.getStyle(), font.getSize());
	}

	/**
	 * Returns the temporary objects of this Animator. Temporary object are
	 * required for animation, but are not animated themselves: for example, move
	 * paths. These are passed as an <code>int[]</code> and not as
	 * PTGraphicObject[], as resetting the numbers of the temporary objects in the
	 * Animators doesn't change the objects but only the numbers. The objects are
	 * not changed until the animator is reinitialized.
	 * 
	 * @return an array containing the numerics IDs of the temporary objects;
	 *         <code>null</code> if no temporary objects are used.
	 */
	public int[] getTemporaryObjects() {
		return new int[] { moveLine.getNum(true) };
	}

	public String toString() {
		if (newContent == null) {
			return AnimalTranslator.translateMessage("putNoObjectsSet");
		} else if (Animation.get().getGraphicObject(animObjNum) instanceof PTArray) {
			return "Replace '"
					+ ((PTArray) Animation.get().getGraphicObject(animObjNum)).getEntry(
							cell).getText() + "' in cell " + cell + " of " + super.toString()
					+ " by '" + newContent.getText() + "'";
		} else {
			return "Put '" + newContent.getText() + "' into " + super.toString();
		}
	}

	/**
	 * Get the graphic object types on which the animator works.
	 * 
	 * @return an array of the supported types
	 */
	public String[] getSupportedTypes() {
		return new String[] { animal.graphics.PTStringArray.STRING_ARRAY_TYPE,
				animal.graphics.PTIntArray.INT_ARRAY_TYPE };
	}
}