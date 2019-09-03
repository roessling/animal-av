package generators.maths.resolutionCalculus;

import java.util.List;

public class TripleListContainer<X> {

	private final List<X> l1;
	private final List<X> l2;
	private final List<X> l3;

	public TripleListContainer(List<X> l1, List<X> l2, List<X> l3) {
		this.l1 = l1;
		this.l2 = l2;
		this.l3 = l3;
	}

	public boolean contains(X x) {
		return l1.contains(x) || l2.contains(x) || l3.contains(x);
	}

}
