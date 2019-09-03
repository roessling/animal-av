/*
 * Created on 10.11.2004
 *
 */
package algorithm.animalTranslator.codeItems;

/**
 * @author Michael Maur <mmaur@web.de> implements an OffsetDefinition(node) for
 *         AnimalScript (Position-Offset)
 */
public class Off extends Node {

	/**
	 * xOff, yOff save the position-offset from the reference-object
	 */
	private int xOff, yOff;

	/**
	 * referenceID is the ID (String) of the reference-object
	 */
	private String referenceID;

	/**
	 * direc saves the edge/side of the reference-object, from where the offset is
	 * taken ("N","NE","NW","S"...)
	 */
	private String direc;

	/**
	 * a constructor that saves all the parameters needed for a position-offset
	 * 
	 * @param offx
	 *          offset in x-direction as int
	 * @param offy
	 *          offset in y-direction as int
	 * @param refID
	 *          String, that contains the reference-objects ID
	 * @param direction
	 *          the point of the compass that represents the edge/side of the
	 *          reference-object, from where the offset is taken
	 */
	public Off(int offx, int offy, String refID, String direction) {
		xOff = offx;
		yOff = offy;
		referenceID = refID;
		direc = direction;
	}

	/**
	 * provides the String-representation of the position-offset(node) in
	 * AnimalScript
	 * 
	 * @return String-representation of the position-offset(node) in AnimalScript
	 */
	public String getString() {
		return " offset (" + xOff + ", " + yOff + ") from \"" + referenceID + "\" "
				+ direc + " ";
	}

}
