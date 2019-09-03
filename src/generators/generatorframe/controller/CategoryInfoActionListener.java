package generators.generatorframe.controller;

import generators.framework.types.CategoryInfos;
import generators.generatorframe.view.InfoFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import animal.main.Animal;


/**
 * 
 * @author Nora Wester
 *
 */
public class CategoryInfoActionListener implements ActionListener {

	CategoryInfos info;
	String category;
	InfoFrame infoFrame;
  private int   zoomCounter = 0;
	
	public CategoryInfoActionListener(){
		info = new CategoryInfos();
	}
	
	private String getLocal(){
	  String lang = "";
    if(Animal.getCurrentLocale().getLanguage().compareTo("en") == 0)
      lang = "EN";
    else
      lang = "DE";
    
    return lang;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	
			ImageIcon icon = info.createImageIcon(category.toLowerCase());
		//	@SuppressWarnings("unused")
			String text = getInfoText();
			//show an new Frame with these informations
			if(!text.isEmpty()){
			  infoFrame = new InfoFrame(category, text, icon);

      if (zoomCounter > 0) {
        for (int i = 0; i < zoomCounter; i++) {
          infoFrame.zoom(true);
        }
      } else {
        for (int i = 0; i > zoomCounter; i--) {
          infoFrame.zoom(false);
        }
      }
			}
	
	}
	
	public void setCategory(String category){
		this.category = category;
	}
	
	private String getInfoText(){
	  
	    LinkedList<String> infoList = info.getInfoText(category.toLowerCase(), getLocal());
	    //nur ein Infofenster genrieren, wenn auch Infos vorhanden sind
	    if(infoList != null){
	      StringBuffer sb = new StringBuffer();
	      for(int i=0; i<infoList.size(); i++){
	        sb.append(infoList.get(i));
	        sb.append("\n");
	        //um eine Leerzeile zu erhalten
	        sb.append("\n");
	      }
	      return sb.toString();
	    }else{
	      JOptionPane.showMessageDialog(null, 
	          "no information", category, 
	          JOptionPane.INFORMATION_MESSAGE);
	    }

	    return "";
	}

  public void changeLocale() {
    // TODO Auto-generated method stub
    if(infoFrame != null){
      String text = getInfoText();
      infoFrame.setNewText(text);
    }
  }

  /**
   * zooms the listing
   * 
   * @param zoomIn
   *          if true zooms in, if false zooms out
   */
  public void zoom(boolean zoomIn) {

    if(zoomIn) {
      if(zoomCounter <6)
        zoomCounter++;
    }else {
      if(zoomCounter >-1)
        zoomCounter --;
    }
    
    if (infoFrame != null)
      infoFrame.zoom(zoomIn);
  }

}
