package generators.generatorframe.controller;

import generators.generatorframe.saving.SaveAnimation;
import generators.generatorframe.store.GetInfos;
//import generators.generatorframe.view.GeneratorFrame;




//import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import translator.Translator;
import animal.main.Animal;

/**
 * 
 * @author Nora Wester
 *
 */
public class ButtonActionListener implements ActionListener {

	GetInfos info;
	
	public ButtonActionListener(){
		info = GetInfos.getInstance();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() instanceof AbstractButton){
			AbstractButton button = (AbstractButton)e.getSource();
			String name = button.getName();
			if(name.compareTo("start") == 0){
//				Component parent = button.getParent();
//				while(!(parent instanceof GeneratorFrame)){
//					parent = parent.getParent();
//				}
//				((GeneratorFrame)parent).setAnimation(info.createContent(true));
//				((GeneratorFrame)parent).setVisible(false);		
				
			  info.createContent(true);

			}
			
//			if(name.compareTo("back") == 0){
////				Component parent = button.getParent();
////				while(!(parent instanceof GeneratorFrame))
////					parent = parent.getParent();
////				
////				((GeneratorFrame)parent).goBack();
//			  info.goBack();

//			}
			
			if(name.compareTo("export") == 0){
				JFileChooser chooser = new JFileChooser();
			    FileNameExtensionFilter filter = new FileNameExtensionFilter(
			        "animation/animalscript(.asu)", "asu");
			    chooser.setFileFilter(filter);
			    int returnValue = chooser.showSaveDialog(null);
			    if(returnValue == JFileChooser.APPROVE_OPTION){
			    	
			    	SaveAnimation saver = new SaveAnimation(chooser.getSelectedFile());
			    	String script = info.createContent(false);
			    	if(script.compareTo("") == 0){
			    	  Translator trans = new Translator("GeneratorFrame",Animal.getCurrentLocale());
			    	  String text = trans.translateMessage("noScript");
			    	  JOptionPane.showMessageDialog(null, 
	                text, 
	                text, JOptionPane.WARNING_MESSAGE);
			    	}else{
			    	  saver.save(script);
			    	}

			    }
			}
		}
	}

}
