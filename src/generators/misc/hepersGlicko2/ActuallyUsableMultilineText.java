package generators.misc.hepersGlicko2;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import helpers.AnimalReader;

public class ActuallyUsableMultilineText {
	
	public static List<String> textFromFile(String fileInResources) {
		
		String url = "resources/glicko2/" + fileInResources;
		
		List<String> text = new ArrayList<String>();
		String line;
		
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(AnimalReader.getInputStream(url)));

            while((line = bufferedReader.readLine()) != null) {
                text.add(line);
            }   
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + url + "'");                
        }
        catch(IOException ex) {
            System.out.println("Error reading file '" + url + "'");                  
        }
		
		return text;
	}
	
}
