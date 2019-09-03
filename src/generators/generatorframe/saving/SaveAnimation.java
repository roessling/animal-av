package generators.generatorframe.saving;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 
 * @author Nora Wester
 *
 */
public class SaveAnimation {

	File file;
	
	public SaveAnimation(File file){
		
	 	String pathName = file.getPath();
	    //System.out.println(pathName);
	    if(!pathName.endsWith(".asu")){
	    	this.file = new File(pathName+".asu");
	    }else{
	    	this.file = file;
	    }
	}
	
	public void save(String content){
		
		try {
			FileWriter writer = new FileWriter(file);
			writer.write(content);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
