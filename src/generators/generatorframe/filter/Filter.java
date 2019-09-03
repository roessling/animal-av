package generators.generatorframe.filter;


import generators.generatorframe.store.FilterInfo;
import generators.generatorframe.store.SaveInfos;
import generators.generatorframe.store.SearchLoader;

import java.util.LinkedList;

/**
 * 
 * @author Nora Wester
 *
 */

public abstract class Filter {

	protected LinkedList<Integer> collection;

	
	protected FilterInfo info;
	protected SearchLoader search;
	protected SaveInfos sI;
	
	public Filter() {
		// TODO Auto-generated constructor stub
		search = SearchLoader.getInstance();
		info = FilterInfo.getInstance();
		sI = SaveInfos.getInstance();

		collection = search.getSelectedIndexes();
	}
	
	public void setCollection(LinkedList<Integer> content){
		collection = content;
	}
	
	public abstract void filter(String syno);

	
}
