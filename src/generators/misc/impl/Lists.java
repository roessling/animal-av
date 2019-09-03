package generators.misc.impl;

import java.util.ArrayList;
import java.util.List;

public class Lists {
	public static List<Attribute> subsetWithout(Attribute key, List<Attribute> keys) {
		List<Attribute> subset = new ArrayList<>(keys);
		subset.remove(key);
		return subset;
	}

	/**
	 * To be used for computing closure during reduceRight
	 * 
	 * @param value
	 * @param fd
	 * @param reducedLeft
	 * @return
	 */
	public static List<FD> subsetForValue(Attribute value, FD fd, List<FD> reducedLeft) {
		List<FD> subset = new ArrayList<>(reducedLeft);
		subset.remove(fd);
		// Add modified FD
		FD fdWithoutValue = new FD(fd);
		fdWithoutValue.getValues().remove(value);
		subset.add(fdWithoutValue);
		return subset;
	}

	public static List<FD> copyAllFD(List<FD> FDs) {
		List<FD> copy = new ArrayList<>();
		for (FD fd : FDs) {
			copy.add(new FD(fd));
		}
		return copy;
	}

	public static List<FD> subsetForKeyMatch(FD base, List<FD> FDs) {
		List<FD> matching = new ArrayList<>();
		for (FD fd : FDs) {
			if(fd != base && fd.getKeys().equals(base.getKeys())) {
				matching.add(fd);
				System.out.println("found one");
			}
		}
		return matching;
	}
}
