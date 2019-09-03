/*
 * Created on 11.11.2004
 *
 */
package algorithm.animalTranslator.codeItems;

/**
 * @author Michael Maur <mmaur@web.de> represents a timing (within x ms/ticks)
 *         in AnimalScript
 */
public class WithinTiming implements Timing {

	/**
	 * the String contains the scale unit of the amountX
	 */
	private String typeX = " ms ";

	/**
	 * the duration of the timing
	 */
	private int amountX;

	/**
	 * a constructor that initializes the amount of ms for the timing
	 * 
	 * @param amount
	 *          the amount of ms for the timing
	 */
	public WithinTiming(int amount) {
		amountX = amount;
	}

	/**
	 * a constructor that initializes the amount and the scale unit of the timing
	 * 
	 * @param amount
	 *          the amount of ms/ticks for the timing
	 * @param type
	 *          the scale unit (ms/ticks) for the timing
	 */
	public WithinTiming(int amount, String type) {
		amountX = amount;
		typeX = " " + type + " ";
	}

	/**
	 * a constructor that initializes the amount and the scale unit of the timing
	 * 
	 * @param amount
	 *          the amount of ms/ticks for the timing
	 * @param type
	 *          the scale unit (ms/ticks) for the timing - 0 for ms, 1 for ticks
	 */
	public WithinTiming(int amount, int type) {
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
	 * provides the string-representation of the WithinTiming in AnimalScript
	 * 
	 * @return the string-representation of the WithinTiming in AnimalScript
	 */
	public String getString() {
		return " within " + amountX + typeX;
	}

}
