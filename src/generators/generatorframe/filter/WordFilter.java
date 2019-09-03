package generators.generatorframe.filter;

/**
 * @author Nora Wester
 */

import generators.generatorframe.store.SaveInfos;

import java.util.LinkedList;


public class WordFilter extends Filter {

	boolean all;
	
	public WordFilter(boolean all){
		super();
		this.all = all;
	}
	
	@Override
	public void filter(String syno) {
		// TODO Auto-generated method stub
		int size;
		
		if(all){
			size = sI.getNumber();
		}
		else
			size = collection.size();
		
		LinkedList<Integer> stay = new LinkedList<Integer>();
		
		LinkedList<String> values = new LinkedList<String>(); 
		
		int placeOfdot = 0;
		boolean run = true;
		
		while(run){
			int index = syno.indexOf(",", placeOfdot);
			
			if(index != -1){
				String sub = syno.substring(0, index);
				sub = sub.trim();
				if(sub.startsWith("\"")){
					if(sub.endsWith("\"")){
						values.add(sub.replace("\"", " ").toLowerCase());
						//System.out.println(sub);
						syno = syno.substring(index+1, syno.length());
						//System.out.println(syno);
						placeOfdot = 0;
					}else{
						placeOfdot = index;
					}
				}else{
					values.add(sub.toLowerCase());
					//System.out.println(sub);
					syno = syno.substring(index+1, syno.length());
					//System.out.println(syno);
				}
			}else{
				String value = syno.trim().replace("\"", " ");
				//System.out.println(value);
				values.add(value.toLowerCase());
				run = false;
			}
		}

//		//setze jeweils einen neuen Eintrage mit dem Anfangschar klein/groß
//		//wegen Case Sensitivität
//		int valueSize = values.size();
//		for(int i=0; i<valueSize; i++){
//		  String value = values.get(i);
//		  String first = value.substring(0, 1);
//		  values.set(i, first.toUpperCase() + value.substring(1));
//		  values.add(first.toLowerCase() + value.substring(1));
//		}
		
		for(int i=0; i<size; i++){
			
			String text;
			if(all){
				text = getText(i);
			}else{
				text = getText(collection.get(i));
			}
			
			boolean find = false;
			int j=0;
			while(j<values.size() && !find){
				if(text.contains(values.get(j))){
					if(all)
						stay.add(i);
					else
						stay.add(collection.get(i));
					find = true;
				}
				j++;
			}
		}
		
		search.setSelectedGeneratorIndexes(stay);
		//System.out.println(stay.size());
		if(all){
			search.setFristSelection(stay);
			search.setNoCategory();
		}
	}

	private String getText(int position) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		
		//TODO wo soll gesucht werden
		sb.append(sI.getSingleValue(position, SaveInfos.DESCRIPTION));
		String name = sI.getSingleValue(position, SaveInfos.NAME);
		sb.append(name);
		if(name.contains("-")){
		  sb.append(name.replace("-", " "));
		  sb.append(name.replace("-", ""));
		}
		if(name.contains(" ")){
      sb.append(name.replace(" ", ""));
    }
		sb.append(sI.getSingleValue(position, SaveInfos.AUTHOR));
		String algoName = sI.getSingleValue(position, SaveInfos.ALGONAME);
		sb.append(algoName);
		if(algoName.contains("-")){
      sb.append(algoName.replace("-", " "));
      sb.append(algoName.replace("-", ""));
    }
		if(algoName.contains(" ")){
      sb.append(algoName.replace(" ", ""));
    }
		
		return sb.toString().toLowerCase();
	}

}
