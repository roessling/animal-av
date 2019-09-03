package generators.generatorframe.loading;



import gfgaa.gui.parser.GraphReader;

import java.awt.Color;
import java.awt.Font;

/**
 * 
 * @author Nora Wester
 *
 */

public class ClassFactory {

	public static Object getObject(String type, String value){
		
		
		if(type.compareTo("int") == 0)
			return new Integer(value);
		
		if(type.compareTo("String") == 0){
			if(value != null)
				return new String(value);
			else
				return new String("");
		}
		
		if(type.compareTo("double") == 0){
			return new Double(value);
		}
		
		if(type.compareTo("intArray") == 0){
			String[] parts = value.split(",");
			int[] temp = new int[parts.length];
			for (int i=0; i<parts.length; i++)
				temp[i] = Integer.parseInt(parts[i].trim());
				
			return temp;
		}
		
		if(type.compareTo("StringArray") == 0){
			String[] parts = value.split(",");
			for(int i=0; i<parts.length; i++)
				parts[i] = parts[i].trim();
			
			return parts;
		}
		
		if(type.compareTo("Font") == 0){
			return new Font(value, Font.PLAIN, 12);
		}
		
		if(type.compareTo("StringMatrix") == 0){
			String[] parts = value.split(";");
			String[][] temp = new String[parts.length][];
			for(int i=0; i<parts.length; i++){
				String[] localParts = parts[i].split(",");
				temp[i] = new String[localParts.length];
				for(int j=0; j<localParts.length; j++){
					temp[i][j] = localParts[j].trim();
				}
			}
			
			return temp;
		}
		
		if(type.compareTo("intMatrix") == 0){
			String[] parts = value.split(";");
			int[][] temp = new int[parts.length][];
			for(int i=0; i<parts.length; i++) {
				String[] localParts = parts[i].split(",");
				temp[i] = new int[localParts.length];
	 			for (int j=0; j<localParts.length; j++) {
					temp[i][j] = Integer.parseInt(localParts[j].trim());
				}
			}
			return temp;
		}
		
		if(type.compareTo("Graph") == 0){

			 GraphReader gr = new GraphReader("no file");
			// System.out.println("Graph "+ value);
			 return gr.readGraph(value, false);
			   
		}
		
		if(type.compareTo("Color") == 0){
			int firstD = value.indexOf(",");
			int secondD = value.lastIndexOf(",");
			
			String r = value.substring(1, firstD);
			String b = value.substring(firstD+1, secondD);
			String g = value.substring(secondD+1, value.length()-1);
			
			return new Color(Integer.parseInt(r.trim()), Integer.parseInt(b.trim()), 
					Integer.parseInt(g.trim()));
		}
		
		if(type.compareTo("boolean") == 0){
			
			if(value.compareTo("true") == 0 || value.compareTo("1") == 0)
				return new Boolean(true);
			if(value.compareTo("false") == 0 || value.compareTo("0") == 0)
				return new Boolean(false);
		}
		
		return new Object();
	}
	


}
