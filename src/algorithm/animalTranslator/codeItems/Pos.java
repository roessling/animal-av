/*
 * Created on 10.11.2004
 *
 */
package algorithm.animalTranslator.codeItems;

/**
 * @author Michael Maur <mmaur@web.de> implements a Position(node) for
 *         AnimalScript
 */
public class Pos extends Node {

	/**
	 * the coordinates of the position (x, y)
	 */
	private int x, y;

	/**
	 * a constructor that sets the coordinates x and y
	 * 
	 * @param xPos
	 *          position-coordinate in x-direction
	 * @param yPos
	 *          position-coordinate in y-direction
	 */
	public Pos(int xPos, int yPos) {
		x = xPos;
		y = yPos;
	}

	/**
	 * provides the string-representation of the position-node in AnimalScript
	 * 
	 * @return the string-representation of the position-node in AnimalScript
	 */
	public String getString() {
		return " (" + x + ", " + y + ") ";
	}

}
