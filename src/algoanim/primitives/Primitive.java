package algoanim.primitives;

import java.awt.Color;

import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.generators.GeneratorInterface;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;
import algoanim.util.Timing;

/**
 * A <code>Primitive</code> is an object which can be worked with in any
 * animation script language. Since all Primitive types share a common
 * administrative functionality this code is implemented in this class.
 * 
 * @author jens
 */
public abstract class Primitive {
	protected GeneratorInterface gen = null;

	private DisplayOptions display;

	private String name = "";

	/**
	 * @param g
	 *          the appropriate code <code>Generator</code> for this
	 *          <code>Primitive</code>.
	 * @param d
	 *          [optional] the <code>DisplayOptions</code> for this
	 *          <code>Primitive</code>.
	 */
	protected Primitive(GeneratorInterface g, DisplayOptions d) {
		this.gen = g;
		this.setDisplayOptions(d);
	}

	/**
	 * Returns the name of this <code>Primitive</code>.
	 * 
	 * @return the name of this <code>Primitive</code>.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of this <code>Primitive</code>.
	 * 
	 * @param newName
	 *          the new name for this <code>Primitive</code>.
	 */
	public void setName(String newName) {
		if (newName != null) {
			name = newName;
		}
	}

	/**
	 * Returns the <code>DisplayOptions</code> of this <code>Primitive</code>.
	 * 
	 * @return the <code>DisplayOptions</code> of this <code>Primitive</code>.
	 */
	public DisplayOptions getDisplayOptions() {
		return display;
	}

	/**
	 * Sets the <code>DisplayOptions</code> of this <code>Primitive</code>.
	 * 
	 * @param displayOptions
	 *          the new <code>DisplayOptions</code> of this
	 *          <code>Primitive</code>.
	 */
	private void setDisplayOptions(DisplayOptions displayOptions) {
		display = displayOptions;
	}

	/**
	 * Changes the position of this Primitive with the given one.
	 * 
	 * @param q
	 *          the other Primitive.
	 */
	public void exchange(Primitive q) {
		gen.exchange(this, q);
	}

	/**
	 * Rotates this <code>Primitive</code> around another one after a time
	 * offset. The duration of this operation may also be specified.
	 * 
	 * @param around
	 *          the center of the rotation.
	 * @param degrees
	 *          the degrees by which the <code>Primitive</code> shall be
	 *          rotated.
	 * @param t
	 *          [optional] the offset until the operation shall be performed.
	 * @param d
	 *          [optional] the time, this operation shall last.
	 */
	public void rotate(Primitive around, int degrees, Timing t, Timing d) {
		gen.rotate(this, around, degrees, t, d);
	}

	/**
	 * Rotates this <code>Primitive</code> around a given center.
	 * 
	 * @param center
	 *          the center of the rotation.
	 * @param degrees
	 *          the degrees by which the <code>Primitive</code> shall be
	 *          rotated.
	 * @param t
	 *          [optional] the offset until the operation shall be performed.
	 * @param d
	 *          [optional] the time, this operation shall last.
	 */
	public void rotate(Node center, int degrees, Timing t, Timing d) {
		gen.rotate(this, center, degrees, t, d);
	}

	/**
	 * Changes the color of a part of this <code>Primitive</code> which is
	 * specified by <code>colorType</code>. Please have a look at the
	 * appropriate <code>Language</code> class, where valid color types may be
	 * specified.
	 * 
	 * @param colorType
	 *          the part of this <code>Primitive</code> of which the color shall
	 *          be changed.
	 * @param newColor
	 *          the new color.
	 * @param t
	 *          [optional] the offset until the operation shall be performed.
	 * @param d
	 *          [optional] the time, this operation shall last.
	 */
	public void changeColor(String colorType, Color newColor, Timing t, Timing d) {
		gen.changeColor(this, colorType, newColor, t, d);
	}

	/**
	 * Moves this <code>Primitive</code> along another one into a specific
	 * direction.
	 * 
	 * @param direction
	 *          the direction to move the <code>Primitive</code>.
	 * @param moveType
	 *          the type of the movement.
	 * @param via
	 *          the <code>Arc</code>, along which the <code>Primitive</code>
	 *          is moved.
	 * @param delay
	 *          the delay, before the operation is performed.
	 * @param duration
	 *          the duration of the operation.
	 * @throws IllegalDirectionException
	 */
	public void moveVia(String direction, String moveType, Primitive via,
			Timing delay, Timing duration) throws IllegalDirectionException {
		gen.moveVia(this, direction, moveType, via, delay, duration);
	}

	/**
	 * Moves this <code>Primitive</code> to a <code>Node</code>.
	 * 
	 * @param moveType
	 *          the type of the movement.
	 * @param dx the x offset to move
	 * @param dy the y offset to move
	 * @param delay
	 *          the delay, before the operation is performed.
	 * @param duration
	 *          the duration of the operation.
	 */
	public void moveBy(String moveType, int dx, int dy, 
			Timing delay, Timing duration) {
		gen.moveBy(this, moveType, dx, dy, delay, duration);
	}

	
	/**
	 * TODO &Uuml;ber die Exceptions nachdenken... (read on) So ist es &auml;u&szlig;erst
	 * inkonsequent und inkonsistent und ganz schlecht zu handhaben. Entweder wir
	 * statten das ganze mit deutlich mehr Exceptions aus, oder wir schmeissen
	 * diese eine auch raus. Moves this <code>Primitive</code> to a
	 * <code>Node</code>.
	 * 
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
	 * @throws IllegalDirectionException
	 */
	public void moveTo(String direction, String moveType, Node target,
			Timing delay, Timing duration) throws IllegalDirectionException {
		gen.moveTo(this, direction, moveType, target, delay, duration);
	}

	/**
	 * Show this <code>Primitive</code> after the given offset.
	 * 
	 * @param t
	 *          the offset until the operation shall be performed.
	 */
	public void show(Timing t) {
		gen.show(this, t);
	}

	/**
	 * Show this <code>Primitive</code> now.
	 */
	public void show() {
		this.show(null);
	}

	/**
	 * Hides this <code>Primitive</code> after the given time.
	 * 
	 * @param t
	 *          the offset until the operation shall be performed.
	 */
	public void hide(Timing t) {
		gen.hide(this, t);
	}

	/**
	 * Hides this <code>Primitive</code> now.
	 */
	public void hide() {
		this.hide(null);
	}

}
