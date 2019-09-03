package generators.misc.impl;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class Closure {

	public static List<Attribute> of(Attribute single, List<FD> FDs, FD current) {
		List<Attribute> base = new ArrayList<>();
		base.add(single);
		return of(base, FDs, current);
	}

	public static List<Attribute> of1(List<Attribute> base, List<FD> FDs, FD current) {
		List<Attribute> closure = new ArrayList<>();
		closure.addAll(base);

		boolean changed = true;
		while (changed) {
			changed = false;

			// Walk through FDs
			for (FD fd : FDs) {

				// check if subset
				if (closure.containsAll(fd.getKeys())) {

					// expand closure, changed only if new attributes
					changed = changed || !closure.containsAll(fd.getValues());
					closure.addAll(fd.getValues());
				}

			}
		}

		// TODO: change to contains for visualizing all steps
		closure = new ArrayList<Attribute>(new LinkedHashSet<Attribute>(closure));

		current.getValues().stream().forEach(a -> {
			if (!base.contains(a))
				base.add(a);
		});

		return closure;

	}

	public static List<Attribute> of(List<Attribute> base, List<FD> FDs) {
		return of(base, FDs, new FD());
	}

	public static List<Attribute> of(List<Attribute> base, List<FD> FDs, FD current) {
		List<Attribute> closure = new ArrayList<>();

		closure.addAll(base);
		current.getValues().stream().forEach(a -> {
			if (!closure.contains(a))
				closure.add(a);
		});

		List<FD> fds = new ArrayList<>(FDs);
		fds.remove(current);

		boolean changed = true;
		while (changed) {
			changed = false;

			// Walk through FDs
			for (FD fd : fds) {

				// check if subset
				if (closure.containsAll(fd.getKeys())) {

					// expand closure, changed only if new attributes
					// changed = changed ||
					// !closure.containsAll(fd.getValues());

					int size = closure.size();
					fd.getValues().stream().forEach(a -> {
						if (!closure.contains(a))
							closure.add(a);

					});

					changed = size != closure.size() || changed;
				}

			}
		}

		return closure;

	}

}
