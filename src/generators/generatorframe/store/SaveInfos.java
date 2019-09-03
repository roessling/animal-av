package generators.generatorframe.store;

import generators.framework.Generator;
import generators.generatorframe.loading.GeneratorsMap;
import generators.generatorframe.loading.HTMLParser;



import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

/**
 * 
 * @author Nora Wester
 *
 */
public class SaveInfos {

	private static SaveInfos sI = new SaveInfos();
	
	HashMap<String, Vector<String>> contentAuthors;
	public java.util.HashMap<String,Integer> hm = GeneratorsMap.generatorMap;
	
	public static final int NAME = 0;
	public static final int CODELANGUAGE = 1;
	public static final int LANGUAGE = 2;
	public static final int DESCRIPTION = 3;
	public static final int CODEEXAMPLE = 4;
	public static final int AUTHOR = 5;
	public static final int GENERATOR = 6;
	public static final int ALGONAME = 7;
	
	
	//speichert alle CodeLanguages, die es insgesamt gibt
	LinkedList<String> allCodeLanguageTotal; 
		
	//speichert alle Sprachen, die es insgesamt gibt
	LinkedList<String> allLanguageTotal;
	
	HashMap<Integer, Generator> generators;
	
	private SaveInfos(){
		allCodeLanguageTotal = new LinkedList<String>();
		allLanguageTotal = new LinkedList<String>();
		generators = new HashMap<Integer, Generator>();
		contentAuthors = new HashMap<String, Vector<String>>();
	}
	
	private void setLanguage(String language){
		
		if(!allLanguageTotal.contains(language))
			allLanguageTotal.add(language);
	}
	
	public void addValue(Generator g, int position, String path){
		generators.put(position, g);
		Integer oldValue = hm.put(path, position);
		if(oldValue!=null){//TODO
			//System.out.println("OldValue:"+oldValue+"\t"+"NewValue:"+position+"\t"+"Path:"+path);
		}
		setLanguage(g.getContentLocale().getLanguage());
		setCodeLanguage(g.getOutputLanguage());
		String[] authors = g.getAnimationAuthor().split(",");
		
		StringBuilder contentEntry = new StringBuilder();
		contentEntry.append("<li>").append(g.getName());
		contentEntry.append("</li>");
		   
		for(int i=0; i<authors.length; i++){
			String trim = authors[i].trim();
			if(contentAuthors.containsKey(trim)){
				Vector<String> algos = contentAuthors.get(trim);
				algos.add(contentEntry.toString());
				contentAuthors.put(trim, algos);
			} else {
				Vector<String> algo = new Vector<String>();
				algo.add(contentEntry.toString());
				contentAuthors.put(trim, algo);
			}
		}
	}
	
	public Generator getGenerator(int position){
		return generators.get(position);
	}
	
	public Generator getGeneratorOutOfPath(String path){
	  return generators.get(hm.get(path));
	}
	
	public String getSingleValue(int position, int typ){
		String temp = "";
		
		if(typ == NAME)
			temp = generators.get(position).getName();
		if(typ == LANGUAGE)
			temp = generators.get(position).getContentLocale().getLanguage();
		if(typ == CODELANGUAGE)
			temp = generators.get(position).getOutputLanguage();
		if(typ == DESCRIPTION){
			String html = generators.get(position).getDescription();
			temp = HTMLParser.parse(html);
		}
		if(typ == CODEEXAMPLE){
			String html = generators.get(position).getCodeExample();
			temp = HTMLParser.parse(html);
		}
		if(typ == AUTHOR)
			temp = generators.get(position).getAnimationAuthor();
		if(typ == ALGONAME)
			temp = generators.get(position).getAlgorithmName();
		
		if(temp == null)
			return "";
		
//		try {
//			byte[] b = temp.getBytes("ISO-8859-1");
//			return new String(b, "UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        
		return temp;
	}
	
	
	public int getNumber(){
		return generators.size();
	}
	
	public String[][] getNameLangCodelang(LinkedList<Integer> positions){
		String[][] array = new String[positions.size()][3];
		
		for(int i=0; i<positions.size(); i++){
			array[i][0] = generators.get(positions.get(i)).getName();
			array[i][1] = generators.get(positions.get(i)).getOutputLanguage();
			array[i][2] = generators.get(positions.get(i)).getContentLocale().getLanguage();
		//	array[i][3] = generators.get(positions.get(i)).getName();
		}
		
		return array;
	}
	
	private void setCodeLanguage(String code){
		
		if(!allCodeLanguageTotal.contains(code))
			allCodeLanguageTotal.add(code);
	}
	
	//returns an array with "..."
	public String[] getCodeLanguageArray(){
		String[] array = new String[allCodeLanguageTotal.size()+1];
		array[0] = "...";
		for(int i=1; i<array.length; i++)
			array[i] = allCodeLanguageTotal.get(i-1);
		
		return array;
	}
	
	//returns an array with "..."
		public String[] getLanguageArray(){
			String[] array = new String[allLanguageTotal.size()+1];
			array[0] = "...";
			for(int i=1; i<array.length; i++)
				array[i] = allLanguageTotal.get(i-1);
			
			return array;
		}
	
	public LinkedList<String> getAllCodeLanguage(){
		return allCodeLanguageTotal;
	}
	
	public LinkedList<String> getAllLanguage(){
		return allLanguageTotal;
	}
	public static SaveInfos getInstance(){
		return sI;
	}

	public HashMap<String, Vector<String>> getContentAuthors() {
		// TODO Auto-generated method stub
		return contentAuthors;
	}
}
