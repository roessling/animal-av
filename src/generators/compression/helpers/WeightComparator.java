package generators.compression.helpers;

import java.util.Comparator;

public class WeightComparator implements Comparator<Element>{

	@Override
	public int compare(Element o1, Element o2) {
		if(o1.getCount() < o2.getCount()) {
			return 1;
		} else if (o1.getCount() == o2.getCount()){
			return 0;
		}
		return -1;
		
	}

}
 
