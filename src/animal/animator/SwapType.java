/*
 * SwapType.java
 * The type used by <code>Swap.java</code>.
 * It includss all the necessary variabless for a swap animation.
 *
 * Created on 12. August 2005, 17:17
 *
 * @author Schmitt
 *
 * @version 0.3b
 * @date 2005-10-31
 */

package animal.animator;

import java.awt.Point;

import translator.AnimalTranslator;


public class SwapType {

	private int[] swapElements = new int[2];

	private Object[] originalContent = null;

	public byte percentage;

	public boolean done = false;

	/**
	 * The points at which the array elements should be now
	 * 
	 * p1 defines the position of the first swap element p2 is the position of the
	 * second element p3 is used by those cells that lie between the two swap
	 * cells
	 */
	public Point p1, p2, p3;

	/**
	 * Creates a new instance of SwapType
	 */
	public SwapType(int first, int second) {
		setElements(first, second);
		p1 = new Point(0, 0);
		p2 = new Point(0, 0);
		p3 = new Point(0, 0);
	}

	public SwapType(int first, int second, byte part) {
		this(first, second);
		percentage = part;
	}

	public SwapType(int first, int second, int part) {
		this(first, second, (byte) part);
	}

	public SwapType(int[] elements, byte part) {
		this(elements[0], elements[1], part);
	}

	public SwapType(int[] elements, int part) {
		this(elements, (byte) part);
	}

	public SwapType(int[] elements, byte part, boolean state) {
		this(elements[0], elements[1], part);
		done = state;
	}

	public SwapType(int[] elements, int part, boolean state) {
		this(elements, (byte) part);
		done = state;
	}

	public SwapType(int[] elements, Point point1, Point point2, 
			Point point3, boolean state) {
		this(elements[0], elements[1]);
		setSwapPoints(point1, point2, point3);
		done = state;
	}

	public SwapType(SwapType st) {
		swapElements = new int[st.swapElements.length];
		for (int i = 0; i < st.swapElements.length; i++) {
			swapElements[i] = st.swapElements[i];
		}
		// swapElements = st.swapElements.clone ();
		percentage = st.percentage;
		done = st.done;
		originalContent = st.originalContent;
	}

	/**
	 * Define the points, where the elements that are swapped should be now
	 * 
	 * @param lower
	 *          the point of the first swap element, moving below the array
	 * @param upper
	 *          the point of the second swap element, moving on top of the array
	 * @param shift
	 *          the position of the cells between the two swap elements
	 */
	public void setSwapPoints(Point lower, Point upper, Point shift) {
		p1 = lower;
		p2 = upper;
		p3 = shift;
	}

	public void setElements(int a, int b) {
		swapElements[0] = a;
		swapElements[1] = b;
	}

	public int[] getElements() {
		return swapElements;
	}

	public Object[] getOrigContents() {
		return originalContent;
	}

	/**
	 * Return the characteristics of a Swap as a String.
	 * 
	 * @return the String representation of this object.
	 */
	public String toString() {
		if ((swapElements[0] < 0) || (swapElements[1] < 0)) {
			return AnimalTranslator.translateMessage("swapNoCellsDefined");
		} 
		StringBuilder sb = new StringBuilder(256);
		sb.append("Swap cells ").append(swapElements[0]).append(" and ");
		sb.append(swapElements[1]).append(", lower position ").append(p1);
		sb.append(", upper position ").append(p2);
		sb.append(", translation position ").append(p3);
		return sb.toString();
	}
}