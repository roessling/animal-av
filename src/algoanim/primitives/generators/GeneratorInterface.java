package algoanim.primitives.generators;

import java.awt.Color;

import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.Primitive;
import algoanim.util.Node;
import algoanim.util.Timing;

/**
 * Defines methods which have to be implemented by each Generator of each
 * language.
 * 
 * @author Stephan Mehlhase
 */
public interface GeneratorInterface {
	/**
	 * Returns the associated <code>Language</code> object.
	 * 
	 * @return the related <code>Language</code> object.
	 */
	public Language getLanguage();

	/**
	 * Exchanges to <code>Primitive</code>s after a given delay.
	 * 
	 * @param p
	 *          the first <code>Primitive</code>.
	 * @param q
	 *          the second <code>Primitive</code>.
	 */
	public void exchange(Primitive p, Primitive q);

	/**
	 * Rotates a <code>Primitive</code> around itself by a given angle after a
	 * delay.
	 * 
	 * @param p
	 *          the <code>Primitive</code> to rotate.
	 * @param degrees
	 *          the angle by which the <code>Primitive</code> shall be rotated.
	 * @param delay
	 *          the delay after which the operation shall be performed.
	 * @param duration
	 *          the duration of the operation.
	 */
	public void rotate(Primitive p, Primitive around, int degrees, Timing delay,
			Timing duration);

	/**
	 * Rotates a <code>Primitive</code> by a given angle around a finite point
	 * after a delay.
	 * 
	 * @param p
	 *          the <code>Primitive</code> to rotate.
	 * @param center
	 *          the Point around which the <code>Primitive</code> shall be
	 *          rotated.
	 * @param degrees
	 *          the angle by which the <code>Primitive</code> shall be rotated.
	 * @param delay
	 *          the delay after which the operation shall be performed.
	 * @param duration
	 *          the duration of the operation.
	 */
	public void rotate(Primitive p, Node center, int degrees, Timing delay,
			Timing duration);

	/**
	 * Unhides a <code>Primitive</code> after a given delay.
	 * 
	 * @param p
	 *          the <code>Primitive</code> to show.
	 * @param delay
	 *          the delay before the operation is performed.
	 */
	public void show(Primitive p, Timing delay);

	/**
	 * Hides a <code>Primitive</code> after a given delay.
	 * 
	 * @param p
	 *          the <code>Primitive</code> to hide.
	 * @param delay
	 *          the delay before the operation is performed.
	 */
	public void hide(Primitive p, Timing delay);

	/**
	 * Moves a <code>Primitive</code> to a point
	 * 
	 * @param p
	 *          the <code>Primitive</code> to move.
	 * @param direction
	 *          the direction to move the <code>Primitive</code>.
	 * @param moveType
	 *          the type of the movement.
	 * @param target
	 *          the point where the <code>Primitive</code> is moved to.
	 * @param delay
	 *          the delay, before the operation is performed.
	 * @param duration
	 *          the duration of the operation.
	 */
	public void moveTo(Primitive p, String direction, String moveType,
			Node target, Timing delay, Timing duration)
			throws IllegalDirectionException;
	
	/**
	 * Moves a <code>Primitive</code> to a point
	 * 
	 * @param p
	 *          the <code>Primitive</code> to move.
 	 * @param moveType
	 *          the type of the movement.
	 * @param dx the x offset to move
	 * @param dy the y offset to move
	 * @param delay
	 *          the delay, before the operation is performed.
	 * @param duration
	 *          the duration of the operation.
	 */
	public void moveBy(Primitive p, String moveType, int dx, int dy, 
			Timing delay, Timing duration);

	
	/**
	 * Moves a <code>Primitive</code> along a Path in a given direction after a
	 * set delay.
	 * 
	 * @param elem
	 *          the <code>Primitive</code> to move.
	 * @param via
	 *          the <code>Arc</code>, along which the <code>Primitive</code>
	 *          is moved.
	 * @param direction
	 *          the direction to move the <code>Primitive</code>.
	 * @param type
	 *          the type of the movement.
	 * @param delay
	 *          the delay, before the operation is performed.
	 * @param duration
	 *          the duration of the operation.
	 */
	public void moveVia(Primitive elem, String direction, String type,
			Primitive via, Timing delay, Timing duration)
			throws IllegalDirectionException;

	/**
	 * Changes the color of a specified part of a <code>Primitive</code> after a
	 * given delay.
	 * 
	 * @param elem
	 *          the <code>Primitive</code> to which the action shall be applied.
	 * @param colorType
	 *          the part of the <code>Primitive</code> to change.
	 * @param newColor
	 *          the new color.
	 * @param delay
	 *          the delay, before the operation is performed.
	 * @param duration
	 *          the duration of the operation.
	 */
	public void changeColor(Primitive elem, String colorType, Color newColor,
			Timing delay, Timing duration);

}
