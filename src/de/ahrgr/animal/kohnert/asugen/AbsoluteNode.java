/*
 * Created on 21.11.2004
 */
package de.ahrgr.animal.kohnert.asugen;

/**
 * @author ek
 */
public class AbsoluteNode extends EKNode {

	protected int x;

	protected int y;

	public AbsoluteNode(AnimalScriptWriter aWriter, int px, int py) {
		super(aWriter);
		setPosition(px, py);
	}

	public void setPosition(int px, int py) {
		x = px;
		y = py;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see animalobjects.Node#print()
	 */
	public void print() {
		out.print(" (");
		out.print(x);
		out.print(", ");
		out.print(y);
		out.print(") ");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see animalobjects.Node#createOffset(int, int)
	 */
	public EKNode createOffset(int dx, int dy) {
		return new AbsoluteNode(scriptwriter, x + dx, y + dy);
	}

}
