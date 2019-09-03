package generators.generatorframe.view.image;

import java.net.URL;

import javax.swing.ImageIcon;

import translator.ResourceLocator;

public class GetIcon {

	public GetIcon() {
		// TODO Auto-generated constructor stub
	}
	
	public ImageIcon createImageIcon() {
		// TODO Auto-generated method stub
		return createIcon("info");
	}
	
	public ImageIcon createDeleteColumnIcon() {
    // TODO Auto-generated method stub
    return createIcon("deleteColumn");
  }
	public ImageIcon createAddColumnIcon() {
    // TODO Auto-generated method stub
    return createIcon("addColumn");
  }
	public ImageIcon createDeleteRowIcon() {
    // TODO Auto-generated method stub
    return createIcon("deleteRow");
  }
	public ImageIcon createAddRowIcon() {
    // TODO Auto-generated method stub
    return createIcon("addRow");
  }
	
	public ImageIcon createDeleteIcon(){
	  return createIcon("delete");
	}
	
	public ImageIcon createAddIcon(){
    return createIcon("add");
  }
	
	public ImageIcon createEditIcon(){
    return createIcon("edit");
  }
	
	private ImageIcon createIcon(String name){
	  return ResourceLocator.getResourceLocator().getImageIcon("/generators/generatorframe/view/image/", name+".png");
	}
	
	public ImageIcon createBackIcon(boolean big) {
    // TODO Auto-generated method stub
	  if(big){
	    return createIcon("backB");
	  }
    return createIcon("back");
  }
	
	public ImageIcon createSearchIcon() {
    // TODO Auto-generated method stub
    return createIcon("search");
  }
	
	public ImageIcon createForwardIcon(boolean big){
	  
	  if(big){
	    return createIcon("forwardB");
	  }
	  return createIcon("forward");
	}

	public ImageIcon createExportIcon(){
	  return createIcon("disk");
	}
	
	public ImageIcon createRefreshIcon(){
	  return createIcon("refresh");
	}
}
