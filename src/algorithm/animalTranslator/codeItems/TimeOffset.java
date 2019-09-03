/*
 * Created on 10.11.2004
 *
 */
package algorithm.animalTranslator.codeItems;

/**
 * @author Michael Maur <mmaur@web.de> implements the timeOffset used in
 *         AnimalScript
 */
public class TimeOffset implements DisplayOptions, Timing {

	/**
	 * the String contains the scale unit of the amountX
	 */
	private String typeX = " ms ";

	/**
	 * the duration of the timeOffset
	 */
	private int amountX;

	/**
	 * a constructor that initializes the amount of ms for the timeOffset
	 * 
	 * @param amount
	 *          the amount of ms for the timeOffset
	 */
	public TimeOffset(int amount) {
		amountX = amount;
	}

	/**
	 * a constructor that initializes the amount of ms for the timeOffset
	 * 
	 * @param amount
	 *          the amount of ms for the timeOffset
	 * @param type
	 *          the scale unit for the timeOffset
	 */
	public TimeOffset(int amount, String type) {
		amountX = amount;
		typeX = " " + type + " ";
	}

	/**
	 * a constructor that initializes the amount of ms for the timeOffset
	 * 
	 * @param amount
	 *          the amount of ms for the timeOffset
	 * @param type
	 *          the scale unit for the timeOffset -0 for ms, 1 for ticks
	 */
	public TimeOffset(int amount, int type) {
		// 0 fuer ms, 1 fuer ticks
		if (type == 0) {
			typeX = " ms ";
		} else if (type == 1) {
			typeX = " ticks ";
		} else {
			// exception
		}
		amountX = amount;
	}

	/**
	 * provides the string-representation of the TimeOffset in AnimalScript
	 * 
	 * @return the string-representation of the TimeOffset in AnimalScript
	 */
	public String getString() {
		return " after " + amountX + typeX;
	}

}
