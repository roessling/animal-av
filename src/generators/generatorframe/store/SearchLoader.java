package generators.generatorframe.store;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Observable;

import generators.framework.Generator;


/**
 * Diese Klasse speichert die Verf�gbaren Generatoren und die ausgew�hlten
 * Es darf somit in RUntime nur ein Objekt erstellt werden.
 * Damit man sie nicht jeder GUI-Klasse �bergeben muss, wird sie als
 * Singleton definiert
 * Die Klasse bietet auch die M�glichkiet sich ausgew�hlte Generatoren 
 * liefern zu lassen (z.B zum Filtern)
 * @author Nora Wester
 *
 */

public class SearchLoader extends Observable{
	
	private static final SearchLoader SL = new SearchLoader();
	
	//speichert die Positionen der aktuell ausgew�hlten Generatoren
	LinkedList<Integer> selectedGenerators;
	
	//speichert die Positionen der ersten Auswahl an Generatoren
	LinkedList<Integer> firstSelection;
	
	//speichert wie viele Generatoren es in jeder Kategorie gibt
	//jeden Feld steht f�r eine Kategorie angelehnt an die 
	//Reihenfolge von GeneratorTyp
	int[] categorySizes;
	
	String[] categoryNames;
	
//	int totalGeneratorsNumber = 0;
	
	String selectedCategory = "";
	
	int selectedGenerator = 0;
	
	GetInfos info;
	
	//SaveInfos sI;
	
	private SearchLoader() {
		// TODO Auto-generated constructor stub


		info = GetInfos.getInstance();
	}
	
	public void init(int[] sizes, String[] names){
		categorySizes = sizes;
		categoryNames = names;
	}

	
	//get the only Objekt of this class in runtime
	public static SearchLoader getInstance(){
		return SL;
	}
	
	public void setSelectedGeneratorIndexes(LinkedList<Integer> selected){
	  Collections.sort(selected, new Comparator<Integer>() {
      SaveInfos sI = SaveInfos.getInstance();
      @Override
      public int compare(Integer o1, Integer o2) {
          Generator algo1 = sI.getGenerator(o1);
          Generator algo2 = sI.getGenerator(o2);
          int i = Collator.getInstance().compare(algo1.getName(), algo2.getName());
          if(i==0) {
            i = Collator.getInstance().compare(algo1.getOutputLanguage(), algo2.getOutputLanguage());
            if(i==0) {
              i = Collator.getInstance().compare(algo1.getContentLocale().getLanguage(), algo2.getContentLocale().getLanguage());
            }
          }
          return i;
      }
	  });
		selectedGenerators = selected;
		this.setChanged();
		this.notifyObservers(new String("selected"));
	}
	
	
	public String getCategoryFromPosition(int globalPosition){
	  int start = 0;
	  
	  for(int i=0; i<categorySizes.length; i++){
	    start = start+categorySizes[i];
	    if(globalPosition < start){
	      return categoryNames[i];
	    }
	  }
	  
	  return "";
	}
	//paramter gibt die position der category in der Liste von GeneratorTyp an
	//position 0 -> start / Pposition 1 -> ende (inklusive)
	public LinkedList<Integer> getPositonOfCategory(int category){
		LinkedList<Integer> position = new LinkedList<Integer>();
		
		int start = 0;
		
		for(int i=0; i<category; i++){
			start = start + categorySizes[i];
		}
		
		//da dies mit der internen z�hlweise mit Anfang bei 0 harmonieren muss
		//ist es die erste Category muss ende um eins reduziert werden
		//ist es einen andere Category muss start um eins reduziert werden
		//somit ist dann auch ende richtig
		
		position.add(start);
		position.add(start+categorySizes[category]-1);
	
		return position;
	}

	
	public void setNoCategory(){
		selectedCategory = "";
		
		this.setChanged();
		this.notifyObservers();
	}
	
	public void setCategorySelected(String selected) {
		// TODO Auto-generated method stub
		selectedCategory = selected;
		
		this.setChanged();
		this.notifyObservers();
		
		int categoryPosition = getInternPosition(selected);
		LinkedList<Integer> category = new LinkedList<Integer>();
		LinkedList<Integer> position = getPositonOfCategory(categoryPosition);
		
	//	System.out.println(selected + " " + position.get(0) + " " + position.get(1));
		
		for(int i = position.get(0); i<position.get(1)+1; i++){
			category.add(i);
		}
		
		setSelectedGeneratorIndexes(category);
	}

	//sucht die interne Position der category mittels des Namns 
	private int getInternPosition(String selected) {
		// TODO Auto-generated method stub
		int position = -1;
		
		for(int i=0; i<categoryNames.length; i++){
			if(categoryNames[i].compareTo(selected) == 0){
				position = i;
			}
		}
		
		return position;
	}

	//wenn keine category ausgew�hlt ist gibt es ein leeren String zur�ck
	public String getSelectedCategory() {
		// TODO Auto-generated method stub
		return selectedCategory;
	}
	
	public LinkedList<Integer> getSelectedIndexes(){
		return selectedGenerators;
	}
	
	public void setFristSelection(LinkedList<Integer> selected){
		firstSelection = selected;
	}
	
	public LinkedList<Integer> getFirstSelection(){
		return firstSelection;
	}
	
	//index des Generators in der selected List
//	public GetInfos getGeneratorInfos(int index){
//		System.out.println("index " + index + " get " + selectedGenerators.get(index));
//		return new GetInfos(selectedGenerators.get(index));
//	}
//	
//	public GetInfos getGeneratorInfos(){
//		return new GetInfos(selectedGenerator);
//	}
	
	public void setGenerator(){
		info.setNewGenerator(selectedGenerator, getCategoryFromPosition(selectedGenerator));
	}

//	public HashMap<String, Vector<String>> getContentAuthors() {
//		// TODO Auto-generated method stub
//		return sI.getContentAuthors();
//	}
  
  //index des Generators in der selected List als index im globalen Raum
  public void setSelectedGenerator(int index){
    if(index < selectedGenerators.size()){
      selectedGenerator = selectedGenerators.get(index);
      setGenerator();
      this.setChanged();
      this.notifyObservers(new String("generator"));
    } 
  }
  
  public int getSelectedGeneratorIndex(){
    return selectedGenerator;
  }
	

}
