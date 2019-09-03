package generators.generatorframe.filter;

import generators.generatorframe.store.SaveInfos;

import java.util.LinkedList;

/**
 * 
 * @author Nora Wester
 *
 */

public class LangFilter extends Filter {

	@Override
	public void filter(String syno) {
		// TODO Auto-generated method stub

		
		LinkedList<Integer> stay = new LinkedList<Integer>();
		for(int i=0; i<collection.size(); i++){
			
			if( sI.getSingleValue(collection.get(i), SaveInfos.LANGUAGE).
					compareTo(syno) == 0){
				stay.add(collection.get(i));
			}
		}
		
		search.setSelectedGeneratorIndexes(stay);
		
	}

}
