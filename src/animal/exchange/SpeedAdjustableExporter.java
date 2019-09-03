/*
 * Created on 19.01.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package animal.exchange;

/**
 * @author guido
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public interface SpeedAdjustableExporter {
	/**
	 * Assign a default disply speed to use for animation export
	 *
	 * @param speedFactor the display speed to use
	 */
	public abstract void setDisplaySpeed(double speedFactor);
}
