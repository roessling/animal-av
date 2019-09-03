package generators.generatorframe.filter;

import generators.generatorframe.store.FilterInfo;
import generators.generatorframe.store.SearchLoader;

/**
 * 
 * @author Nora Wester
 * 
 */

public class FilterCoordinator {
	
	public static final int TEXT = 0;
	public static final int CODE = 1;
	public static final int LANG = 2;
	public static final int TEXT_ALL = 3;
	
	private static FilterInfo info;
	
	public static void coorinate(String syno, int type){
		
		SearchLoader search = SearchLoader.getInstance();
		info = FilterInfo.getInstance();
		
		setFilterTyp(syno, type);
		
		if(type == TEXT_ALL){
			Filter filter = new WordFilter(true);
		
			//null, weil keine speziellen Generatoren schon ausgew�hlt sind
			filter.setCollection(null);
			filter.filter(syno);
			search.setNoCategory();
			return;
		}
		
		boolean first = true;
		if(info.noFilter())
			search.setSelectedGeneratorIndexes(search.getFirstSelection());

		//nach Sprache sortieren
		String filterTyp = info.getLangSearch();
		
		if(filterTyp.compareTo("...") != 0){
			Filter filter = new LangFilter();
			if(first){
				//beim ersten Filtern den Bereich auf "firstSelection" zur�cksetzen
				filter.setCollection(search.getFirstSelection());
				first = false;
			}
			filter.filter(filterTyp);
		}
		
		//nach Code Language sortieren
		filterTyp = info.getCodeSearch();
		if(filterTyp.compareTo("...") != 0){
			Filter filter = new CodeLangFilter();
			if(first){
				//beim ersten Filtern den Bereich auf "firstSelection" zur�cksetzen
				filter.setCollection(search.getFirstSelection());
				first = false;
			}
			filter.filter(filterTyp);
			
		}
		
		//nach Text Sortieren
		filterTyp = info.getTextSearch();
		if(filterTyp.compareTo("") != 0){
			Filter filter = new WordFilter(false);
			if(first){
				//beim ersten Filtern den Bereich auf "firstSelection" zur�cksetzen
				filter.setCollection(search.getFirstSelection());
				first = false;
			}
			filter.filter(filterTyp);
		}
		
	}
	
	private static void setFilterTyp(String syno, int type){
		
		if(type == CODE){
			info.setCodeSearch(syno);
		}
		
		if(type == LANG){
			info.setLangSearch(syno);
		}
		
		if(type == TEXT){
			info.setTextSearch(syno);
		}
		
		if(type == TEXT_ALL){
			info.setTextSearchAll(syno);
		}
	}

}
