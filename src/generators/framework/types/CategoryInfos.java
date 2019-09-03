package generators.framework.types;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.LinkedList;

import javax.swing.ImageIcon;

public class CategoryInfos {

	public CategoryInfos(){
		
	}
	
	public LinkedList<String> getInfoText(String category, String local){
		
		LinkedList<String> sb = new LinkedList<String>();
		String path = category+local+".txt";
		URL textURL = getClass().getResource(path);
		
		try {
			if(textURL != null){
				BufferedReader in = new BufferedReader(
						new InputStreamReader(textURL.openStream()));
				boolean read = true;
				while(read){
					String line = in.readLine();
					if(line != null){
						sb.add(line);
						line = in.readLine();
					}else{
						read = false;
					}
				}
			
				in.close();
			}else{
				return null;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("File not found");
		}
		
		return sb;
	}
	
	
	public ImageIcon createImageIcon(String category) {
		String path = category+".gif";
		URL imgURL = getClass().getResource(path);
		if(imgURL != null){
			return new ImageIcon(imgURL, "image");
		} else {
			System.out.println("Can't find image "+path);
			return null;
		}
	}

}
