package generators.misc.continousdoubleauction;


public class Triple<T, X, Y> extends generators.helpers.Pair<Double, String>
		implements Comparable<Triple<Double, Double, String>> {
	private double t;

	private static final long serialVersionUID = 1L;

	public Triple(Double t, Double x, String y) {
		super(x, y);
		this.t = t;
	}

	@Override
	public int compareTo(Triple<Double, Double, String> pair) {
		return this.getX().compareTo(pair.getX());
	}

	public Double getT() {
		return t;
	}

	@Override
	public String toString() {
		return super
				.toString()
				.replace(
						"(",
						"(t="
								+ ((t == Math.floor(t))
										&& !Double.isInfinite(t) ? new Double(t)
										.intValue() : t + "") + ", ");
	}
}
