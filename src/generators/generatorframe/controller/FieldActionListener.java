package generators.generatorframe.controller;


import generators.generatorframe.store.GetInfos;
//import generators.generatorframe.view.valuePanels.ArrayLightPanel;
//import generators.generatorframe.view.valuePanels.MatrixLightPanel;
//
//import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import animal.main.Animal;
import translator.Translator;

/**
 * 
 * @author Nora Wester
 *
 */

public class FieldActionListener implements ActionListener {

	private static final int ADD = 0;
	private static final int DELETE = 1;
	private static final int ADDROW = 2;
	private static final int DELETEROW = 4;
	
	private int type;
	private GetInfos algo;
	String name;
	Translator trans;
	
	public FieldActionListener(String name){
		algo = GetInfos.getInstance();
		this.name = name;
		trans = new Translator("GeneratorFrame", Animal.getCurrentLocale());
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		trans.setTranslatorLocale(Animal.getCurrentLocale());
	  
//		Component parent = ((JButton)arg0.getSource()).getParent();
//		while((!(parent instanceof ArrayLightPanel)) && (!(parent instanceof MatrixLightPanel)))
//			parent = parent.getParent();
		
		int[] size = algo.getCurrentArraySize(name);
		String question = "";
		String buttonName = ((JButton) arg0.getSource()).getName();
		if(buttonName.compareTo("add") == 0){
			question = trans.translateMessage("questionAdd") + 
			    " (0 - " + Integer.toString(size[1]-1) + ")";
			type = ADD;
		}
		
		if(buttonName.compareTo("delete") == 0){
			question = trans.translateMessage("questionDelete") +
			    " (0 - " + Integer.toString(size[1]-1) + ")";
			type = DELETE;
		}
		
		if(buttonName.compareTo("addRow") == 0){
			question = trans.translateMessage("questionAddRow") + 
			    " (0 - " + Integer.toString(size[0]-1) + ")";
			type = ADDROW;
		}
		
		if(buttonName.compareTo("deleteRow") == 0){
			question = trans.translateMessage("questionDeleteRow") + 
			    " (0 - " + Integer.toString(size[0]-1) + ")";
			type = DELETEROW;
		}
		
		String input = JOptionPane.showInputDialog(null, 
				question, 
				trans.translateMessage("questionLabel"), JOptionPane.QUESTION_MESSAGE);
		
		try{
			Integer test = Integer.parseInt(input);
			
			if(type == ADD){
	      algo.addField(test, name, null);
	    }
	    if(type == DELETE){
	      algo.deleteField(test, name);
	    }
	    if(type == ADDROW){
	      algo.addRow(test, name, null);
	    }
	    if(type == DELETEROW){
	      algo.deleteRow(test, name);
	    }
			
		}catch(NumberFormatException e){
			
			JOptionPane.showMessageDialog(null, 
					trans.translateMessage("errorInt"), 
					trans.translateMessage("errorLabel"), JOptionPane.WARNING_MESSAGE);
		}
		
	}

}
