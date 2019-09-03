package animal.vhdl.exchange;

import javax.swing.JFileChooser;

import animal.vhdl.gui.VHDLOptimizationWindows;

/**
 * 
 * @author Lu,Zheng
 *
 */
public  class VHDLAnimationImporter {
	public static boolean importAnimation(){
		// TODO Auto-generated method stub
		JFileChooser fc = new JFileChooser("."); 
		DataFileFilter ft = new DataFileFilter("txt","VHDL data(.txt)");
		fc.addChoosableFileFilter(ft);
		fc.showOpenDialog(null);
		if (fc.getSelectedFile() != null) {
		  String path = fc.getSelectedFile().getPath();
		  VHDLOptimizationWindows.openWindows(path);
		//VHDLUsedElementPropertiesWindows.openWindows(path,usedQuineMcCluskey);
		}
		return true;

	}
}
