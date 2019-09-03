package generators.generatorframe.store;


import java.net.URL;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Vector;

import javax.swing.JOptionPane;

import translator.Translator;

import algoanim.properties.AnimationProperties;
import animal.gui.AnimalMainWindow;
import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.generatorframe.loading.XMLLoader;
import gfgaa.gui.parser.GraphReader;


/**
 * 
 * @author Nora Wester
 *
 */

public class GetInfos extends Observable{

	//Generator algo;
	private static final GetInfos INFO = new GetInfos();
  private static String         errorMessage = null;

	XMLLoader xml;
	SaveInfos sI;
	int globalIndex = -1;
	String currentName = "";
	Hashtable<String, Object> prim;
	Hashtable<String, String> primDescription;
	AnimationPropertiesContainer props;
	AnimationProperties p;
	Translator trans;
	String category;
	
	private GetInfos(){
		//algo = generator;
		
		sI = SaveInfos.getInstance();
		trans = new Translator("GeneratorFrame", Animal.getCurrentLocale());
		
	}
	
	public static String getErrorMessage() {
	  return errorMessage;
	}
	
	public static GetInfos getInstance(){
		return INFO;
	}
	
	public void setNewGenerator(int globalIndex, String category){
		this.globalIndex = globalIndex;
		
		Generator algo = sI.getGenerator(globalIndex);
		String name = algo.getClass().getSimpleName();
		URL url = algo.getClass().getResource(name+".xml");
		xml = new XMLLoader(url);
		prim = xml.getPrim();
		primDescription = xml.getPrimDescription();
		props = xml.getProps();
		this.category = category;
		
		AnimalMainWindow.getWindowCoordinator().getAnimationWindow(false).getAnimationWindowView().updateAllToCurStep();
	}
	
	public String getCategory(){
	  return trans.translateMessage("category") + ": " + category;
	}
	
	public String[] getPrimNameSet(){
		String[] keys = new String[prim.size()];
		keys = prim.keySet().toArray(keys);
		return keys;
	}
	
	public Vector<String> getPropItemNames(String selected){
		
		AnimationProperties p = props.getPropertiesByName(selected);
		Vector<String> itemName = p.getAllPropertyNamesVector();
		return itemName;
	}
	
	public void setAnimationProperty(String selected){
		p = props.getPropertiesByName(selected);
	}
	
	public boolean editable(String name){
		
		return p.getIsEditable(name);
	}
	
	public Object getPropValue(String name){
	
		return p.get(name);
	}
	
	public void setPropValue(String name, Object value, int position){
		
		p.set(name, value);
		props.set(position, p);
	}
	
	public String[] getPropsNameSet(){
		
		String[] keys = new String[props.size()];
		
		for(int i=0; i<props.size(); i++){
			AnimationProperties p = props.get(i);
			keys[i] = (String)p.get("name");
			//System.out.println(p.get("name") + " " + p.get("name").getClass().getSimpleName());
		}
		
		return keys;
	}
	
	public Object getPrimValue(String name){
	  currentName = name;
		return prim.get(name);
	}
	
	public String getPrimDescription(String name){
		currentName = name;
		if(primDescription.containsKey(name)){
			return primDescription.get(name);
		}else{
			return "";
		}
	}
	
//	public StringBuffer getGraphScript(String name){
//		Graph graph = (Graph)prim.get(name);
//		currentGraphName = name;
//		GraphWriter writer = new GraphWriter(graph);
//		return writer.getScript();
//	}
	
//	public String getCurrentGarphName(){
//		return currentGraphName;
//	}
	
	public void setGraph(String script){
		GraphReader read = new GraphReader("no file");
		if (prim == null)
		  prim = new Hashtable<String, Object>(37);
		prim.replace(currentName, read.readGraph(script, false));
	}
	
	public void setPrimValue(String name, Object value){
		prim.replace(name, value);
	}
	
	public String getName(){
		return trans.translateMessage("algoName") + ": " + sI.getSingleValue(globalIndex, SaveInfos.ALGONAME);
	}

	public String getGeneratorName(){
	  return sI.getSingleValue(globalIndex, SaveInfos.NAME);
	}
	
	public void setNewLocale(){
	  trans.setTranslatorLocale(Animal.getCurrentLocale());
	}
	
	public String getAuthor() {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append(trans.translateMessage("author"));
		sb.append(" ");
		sb.append(sI.getSingleValue(globalIndex, SaveInfos.AUTHOR));
	
		return sb.toString();
	}
	
	public String getLanguage(){
		
		StringBuffer sb = new StringBuffer();
		sb.append(trans.translateMessage("lang"));
		sb.append(": ");
		sb.append(sI.getSingleValue(globalIndex, SaveInfos.LANGUAGE));
	
		return sb.toString();
	}
	
	public String getCodeLanguage(){
	
		StringBuffer sb = new StringBuffer();
		sb.append(trans.translateMessage("codeLang"));
		sb.append(": ");
		sb.append(sI.getSingleValue(globalIndex, SaveInfos.CODELANGUAGE));
	
		return sb.toString();
	}
	
	public String getDescription(){
		return sI.getSingleValue(globalIndex, SaveInfos.DESCRIPTION);
	}
	
	public String getCodeExample(){
		return sI.getSingleValue(globalIndex, SaveInfos.CODEEXAMPLE);
	}
	
	private Generator latestStartetGenerator = null;
	//forStart gibt an, ob die Informationen zum Abspielen der Animation
	//gebraucht werden oder zum Speichern
	public String createContent(boolean forStart){
	  errorMessage = null;
		Generator algo = sI.getGenerator(globalIndex);
		algo.init();
	    if (algo instanceof ValidatingGenerator) {
	      try {
	        if (!((ValidatingGenerator)algo).validateInput(props, prim))
	          throw new IllegalArgumentException("Parameter validation failed.");
	      } catch (IllegalArgumentException iae) {
	        JOptionPane.showConfirmDialog(null, "<html>Incorrect parameters<br />" +iae.getMessage());
	        return null;
	      }
	    }
	    
	    String script = "";
	   
	    try{
	      script = algo.generate(props, prim);
	    }catch(Exception e){
	      System.err.println("error in generation...: " + e.getMessage());	      
	      StringBuffer sb = new StringBuffer("Error in generating content\n");
	      sb.append(e.getMessage()).append("\nStack trace for this occurrence:");
	      StackTraceElement[] stes = e.getStackTrace();
	      for (StackTraceElement ste : stes)
	        sb.append("\n\t" + ste.toString());
	      errorMessage = sb.toString();
	      e.printStackTrace();
	    }
 	    
	    if(forStart){
	      setLatestStartetGenerator(algo);
	      this.setChanged();
	      this.notifyObservers(script);
	      return "";
	      
	    }else{
	      return script;
	    }  
	}
	
	public int[] getCurrentArraySize(String name){
	  
	  if(prim.get(name) instanceof String[]){
	    return new int[]{1, ((String[])prim.get(name)).length};
	  }
	  
	  if(prim.get(name) instanceof int[][]){
      return new int[]{((int[][])prim.get(name)).length,((int[][])prim.get(name))[0].length};
    }
	  
	  if(prim.get(name) instanceof int[]){
      return new int[]{1, ((int[])prim.get(name)).length};
    }
	  
	  if(prim.get(name) instanceof String[][]){
      return new int[]{((String[][])prim.get(name)).length,((String[][])prim.get(name))[0].length};
    }
	  
	  return new int[0];
	}
	
	public void addField(Integer test, String name, Object value) {
		// TODO Auto-generated method stub
	  //System.out.println(test + name + value + "addField");
		if(prim.get(name) instanceof String[]){
			String[] old = (String[])prim.get(name);
			
			 if(value == null){
		      value = "";
		    }
			
			if(test == null){
			  test = old.length;
			}
			
			if(test < -1 || test > old.length){
			  setChanged();
	      notifyObservers(new String("Error"));
				return;
			}
			
			String[] newer = new String[old.length+1];
			for(int i=0; i<newer.length; i++){
				if(i<test)
					newer[i] = old[i];
				else{
					if(i == test)
						newer[i] = (String)value;
					else
						newer[i] = old[i-1];
				}
			}
			
			prim.replace(name, newer);
		}
		
		if(prim.get(name) instanceof int[]){
			int[] old = (int[])prim.get(name);
			
			 if(value == null){
		      value = 0;
		    }
			
			if(test == null){
        test = old.length;
      }
			
			if(test < -1 || test > old.length){
			  setChanged();
	      notifyObservers(new String("Error"));
				return;
			}
			
			int[] newer = new int[old.length+1];
			for(int i=0; i<newer.length; i++){
				if(i<test)
					newer[i] = old[i];
				else{
					if(i == test)
						newer[i] = (int) value;
					else
						newer[i] = old[i-1];
				}
			}
			
			prim.replace(name, newer);
		}
		
		if(prim.get(name) instanceof String[][]){
			String[][] old = (String[][])prim.get(name);
			
			 if(value == null){
		      value = "";
		    }
			
			if(test == null){
			  test = old[0].length;
			}
			
			if(test < -1 || test > old[0].length){
			  setChanged();
	      notifyObservers(new String("Error"));
				return;
			}	
			
			String[][] newer = new String[old.length][old[0].length+1];
			for(int j=0; j<newer.length; j++){
				for(int i=0; i<newer[j].length; i++){
					if(i<test)
						newer[j][i] = old[j][i];
					else{
						if(i == test)
							newer[j][i] = (String) value;
						else
							newer[j][i] = old[j][i-1];
					}
				}
			}
			prim.replace(name, newer);
		}
		
		if(prim.get(name) instanceof int[][]){
			int[][] old = (int[][])prim.get(name);
			
			 if(value == null){
		      value = 0;
		    }
			
			if(test == null){
        test = old[0].length;
      }
			
			if(test < -1 || test > old[0].length){
			  setChanged();
	      notifyObservers(new String("Error"));
				return;
			}
			
			int[][] newer = new int[old.length][old[0].length+1];
			for(int j=0; j<newer.length; j++){
				for(int i=0; i<newer[j].length; i++){
					if(i<test)
						newer[j][i] = old[j][i];
					else{
						if(i == test)
							newer[j][i] = (int)value;
						else
							newer[j][i] = old[j][i-1];
					}
				}
			}
			prim.replace(name, newer);
		}
		
		this.setChanged();
    this.notifyObservers();
	}
	
	public void deleteField(Integer test, String name){
	  //System.out.println(test + name + "deleteField");
		if(prim.get(name) instanceof String[]){
			String[] old = (String[])prim.get(name);
			
			if(test == null){
			  test = old.length-1;
			}
			
			if(test < -1 || test > old.length-1){
			  setChanged();
	      notifyObservers(new String("Error"));
				return;
			}
			
			String[] newer = new String[old.length-1];
			for(int i=0; i<newer.length; i++){
				if(i<test)
					newer[i] = old[i];
				else
					newer[i] = old[i+1];
			}
			
			prim.replace(name, newer);
		}
		
		if(prim.get(name) instanceof int[]){
			int[] old = (int[])prim.get(name);
			
			if(test == null){
        test = old.length-1;
      }
			
			if(test < -1 || test > old.length-1){
			  setChanged();
	      notifyObservers(new String("Error"));
				return;
			}
			
			int[] newer = new int[old.length-1];
			for(int i=0; i<newer.length; i++){
				if(i<test)
					newer[i] = old[i];
				else
					newer[i] = old[i+1];
			}
			
			prim.replace(name, newer);
		}
		
		if(prim.get(name) instanceof String[][]){
			String[][] old = (String[][])prim.get(name);
			
			if(test == null){
			  test = old[0].length-1;
			}
			
			if(test < -1 || test > old[0].length-1){
			  setChanged();
	      notifyObservers(new String("Error"));
			  return;
			}
			
			String[][] newer = new String[old.length][old[0].length-1];
			for(int j=0; j<newer.length; j++){
				for(int i=0; i<newer[j].length; i++){
					if(i<test)
						newer[j][i] = old[j][i];
					else
						newer[j][i] = old[j][i+1];
				}
			}
			prim.replace(name, newer);
		}
		
		if(prim.get(name) instanceof int[][]){
			int[][] old = (int[][])prim.get(name);
			
			if(test == null){
        test = old[0].length-1;
      }
			
			if(test < -1 || test > old[0].length-1){
			  setChanged();
	      notifyObservers(new String("Error"));
				return;
			}
			
			int[][] newer = new int[old.length][old[0].length-1];
			for(int j=0; j<newer.length; j++){
				for(int i=0; i<newer[j].length; i++){
					if(i<test)
						newer[j][i] = old[j][i];
					else
						newer[j][i] = old[j][i+1];
				}
			}
			prim.replace(name, newer);
		}
		
		this.setChanged();
    this.notifyObservers();
	}

	public void setNewFieldValue(int row, int column, String name, Object value, boolean notify) {
		// TODO Auto-generated method stub
	  boolean error = false;
	  
		if(prim.get(name) instanceof int[][]){
			int[][] old = (int[][])prim.get(name);
			if(row < old.length && row > -1 && column < old[0].length && column > -1){
			  old[row][column] = (int)value;
			  prim.replace(name, old);
			}else{
			  error = true;
			}
		}
		
		if(prim.get(name) instanceof String[][]){
			String[][] old = (String[][])prim.get(name);
			if(row < old.length && row > -1 && column < old[0].length && column > -1){
			  old[row][column] = (String)value;
			  prim.replace(name, old);
			}else{
			  error = true;
			}
		}
		
		if(prim.get(name) instanceof int[]){
			int[] old = (int[])prim.get(name);
			if(column < old.length && column > -1){
			  old[column] = (int)value;
	//		System.out.println("hier " + old[column]);
			  prim.replace(name, old);
			}else{
			  error = true;
			}
		}
		
		if(prim.get(name) instanceof String[]){
			String[] old = (String[])prim.get(name);
			if(column < old.length && column > -1){
		//	System.out.println("hier " + old[column]);
			  old[column] = (String)value;
			  prim.replace(name, old);
			}else{
			  error = true;
			}
		}
		
		if(notify && !error){
		  setChanged();
		  notifyObservers();
		}
		
		if(error){
		  setChanged();
		  notifyObservers(new String("Error"));
		}
	}

	private int[] getNewIntArray(int length, int input){
		int[] array = new int[length];
		for(int i=0; i<array.length; i++)
			array[i] = input;
		
		return array;
	}
	
	private String[] getNewArray(int length, String input){
		String[] array = new String[length];
		for(int i=0; i<array.length; i++)
			array[i] = input;
		
		return array;
	}
	
	public void addRow(Integer test, String name, Object input) {
		// TODO Auto-generated method stub
	 // System.out.println(test + name + input + "addRow");
	  if(prim.get(name) instanceof int[][]){
			int[][] value = (int [][]) prim.get(name);
			if(test == null){
			  test = value.length;
			}
			if(test < -1 || test > value.length){
			  setChanged();
	      notifyObservers(new String("Error"));
			  return;
			}
			
			if(input == null){
			  input = 0;
			}
			
			int[][] array = new int[value.length+1][value[0].length];
			for(int i=0; i<value.length+1; i++){
				if(i < test){
					array[i] = value[i];
				}else{
					if(i == test)
						array[i] = getNewIntArray(value[0].length, (int)input);
					else
						array[i] = value[i-1];
				}
			}
			
			prim.replace(name, array);
		}
		
		if(prim.get(name) instanceof String[][]){
			String[][] value = (String [][]) prim.get(name);
			 System.out.println(test);
			if(test == null){
			  test = value.length;
			}
			if(test < -1 || test > value.length){
			  setChanged();
	      notifyObservers(new String("Error"));
			  return;
			}
			
			if(input == null){
			  input = "";
			}
			String[][] array = new String[value.length+1][value[0].length];
			for(int i=0; i<value.length+1; i++){
				if(i < test){
					array[i] = value[i];
				}else{
					if(i == test){
						array[i] = getNewArray(value[0].length, (String)input);
					}else{
						array[i] = value[i-1];
					}	
				}
			}
			
			prim.replace(name, array);
		}
		
		this.setChanged();
    this.notifyObservers();
	}

	public void deleteRow(Integer test, String name) {
		// TODO Auto-generated method stub
	  //System.out.println(test + name + "deleteRow");
	  if(prim.get(name) instanceof int[][]){
			int[][] value = (int [][]) prim.get(name);
			
			if(test == null){
			  test = value.length-1;
			}
			if(test < -1 || test > value.length-1){
			  setChanged();
	      notifyObservers(new String("Error"));
			  return;
			}
			
			int[][] array = new int[value.length-1][value[0].length];
			
			
			for(int i=0; i<value.length-1; i++){
				if(i < test){
					array[i] = value[i];
				}else{
					array[i] = value[i+1];
				}
			}
			
			prim.replace(name, array);

		}
		
		if(prim.get(name) instanceof String[][]){
			String[][] value = (String [][]) prim.get(name);
			if(test == null){
			  test = value.length-1;
			}
			if(test < -1 || test > value.length-1){
			  setChanged();
	      notifyObservers(new String("Error"));
			  return;
			}
			
			String[][] array = new String[value.length-1][value[0].length];
			for(int i=0; i<value.length-1; i++){
				if(i < test){
					array[i] = value[i];
				}else{
					array[i] = value[i+1];
				}
			}
			
			prim.replace(name, array);
		}
		
		this.setChanged();
    this.notifyObservers();
	}

  public Generator getLatestStartetGenerator() {
    return latestStartetGenerator;
  }

  private void setLatestStartetGenerator(Generator latestStartetGenerator) {
    this.latestStartetGenerator = latestStartetGenerator;
  }
  
  public int getGlobalIndex() {
    return globalIndex;
  }
}

