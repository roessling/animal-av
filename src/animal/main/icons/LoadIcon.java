package animal.main.icons;

import javax.swing.ImageIcon;

import translator.ResourceLocator;

public class LoadIcon {

  public LoadIcon() {
    // TODO Auto-generated constructor stub
  }
  
  public ImageIcon createImageIcon() {
    // TODO Auto-generated method stub
    return createIcon("info");
  }
  
  public ImageIcon createPlayIcon() {
    // TODO Auto-generated method stub
    return createIcon("play");
  }
  public ImageIcon createPauseIcon() {
    // TODO Auto-generated method stub
    return createIcon("pause");
  }
  public ImageIcon createSnapShotIcon() {
    // TODO Auto-generated method stub
    return createIcon("snap");
  }
  public ImageIcon createPlayBackwardsIcon() {
    // TODO Auto-generated method stub
    return createIcon("playbackwards");
  }
  

  private ImageIcon createIcon(String name){
    return ResourceLocator.getResourceLocator().getImageIcon("/animal/main/icons/",name+".png");
  }
  
  public ImageIcon createBackIcon() {
    // TODO Auto-generated method stub
    return createIcon("back");
  }
  
  public ImageIcon createForwardIcon(){
   
    return createIcon("forward");
  }
  
  public ImageIcon createToLastIcon(){
    
    return createIcon("last");
  }
  
  public ImageIcon createToFirstIcon(){
    
    return createIcon("first");
  }

 
}
