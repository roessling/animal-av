package generators.generatorframe.store;

import java.util.LinkedList;
import java.util.Observable;

/**
 * Singelton-Klasse zum Speichern der Filterangaben
 * @author Nora Wester
 *
 */

public class FilterInfo extends Observable{

	private static final FilterInfo FI = new FilterInfo();
	
	private String textSearch;
	private String codeSearch;
	private String langSearch;
	private String textSearchALL;
	
	private FilterInfo(){
		textSearch = "";
		codeSearch = "...";
		langSearch = "...";
		textSearchALL = "";
	}
	
	public boolean noFilter(){
		return textSearch.compareTo("") == 0 && codeSearch.compareTo("...") == 0
				&& langSearch.compareTo("...") == 0;
	}
	
	public void setNoFilter(){
		textSearch = "";
		codeSearch = "...";
		langSearch = "...";
		this.setChanged();
		this.notifyObservers();
	}
	
	public String getTextSearcAll() {
		return textSearchALL;
	}

	public void setTextSearchAll(String textSearch) {
		this.textSearchALL = textSearch;
	}
	
	public String getTextSearch() {
		return textSearch;
	}

	public void setTextSearch(String textSearch) {
		this.textSearch = textSearch;
	}

	public String getCodeSearch() {
		return codeSearch;
	}

	public void setCodeSearch(String codeSearch) {
		this.codeSearch = codeSearch;
	}

	public String getLangSearch() {
		return langSearch;
	}

	public void setLangSearch(String langSearch) {
		this.langSearch = langSearch;
	}

	public int getIndexForLang(){
		SaveInfos info = SaveInfos.getInstance();
		LinkedList<String> code = info.getAllLanguage();
		return code.indexOf(langSearch)+1;
	}
	
	public int getIndexForCode(){
		SaveInfos info = SaveInfos.getInstance();
		LinkedList<String> code = info.getAllCodeLanguage();
		return code.indexOf(codeSearch)+1;
	}
	
	//get the only Objekt of this class in runtime
	public static FilterInfo getInstance(){
		return FI;
	}
}
