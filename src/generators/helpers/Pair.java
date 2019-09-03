package generators.helpers;

import java.io.Serializable;
/**
 * 
 * @author Dirk Kröhan, Kamil Erhard
 *
 */
public class Pair<X, Y> implements Serializable {

	private static final long serialVersionUID = 1L;

	private final X x;
	private final Y y;

	/**
	 * @param x
	 * @param y
	 */
	public Pair(X x, Y y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * @return the x
	 */
	public X getX() {
		return this.x;
	}

	/**
	 * @return the y
	 */
	public Y getY() {
		return this.y;
	}

	@Override
	public String toString() {
		return "(" + this.getX() + ", " + this.getY() + ")";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Pair) {
			@SuppressWarnings("unchecked")
      Pair<X, Y> p = (Pair<X, Y>) obj;
			return (p.x.equals(this.x) && (p.y.equals(this.y)));
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + this.x.hashCode();
		hash = 31 * hash + this.y.hashCode();
		return hash;
	}
}