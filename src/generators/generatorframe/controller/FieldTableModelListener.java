package generators.generatorframe.controller;

import generators.generatorframe.store.GetInfos;

import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import animal.main.Animal;
import translator.Translator;

/**
 * 
 * @author Nora Wester
 *
 */

public class FieldTableModelListener implements TableModelListener {

	String name;
	boolean string;
	GetInfos algo;
	Translator trans;
	
	public FieldTableModelListener(String name, boolean string){
		this.name = name;
		this.string = string;
		algo = GetInfos.getInstance();
		trans = new Translator("GeneratorFrame", Animal.getCurrentLocale());
	}
	
	@Override
	public void tableChanged(TableModelEvent e) {
		// TODO Auto-generated method stub
		int i = e.getColumn();
		int j = e.getFirstRow();
		
		if((e.getSource() instanceof DefaultTableModel) && e.getType() == TableModelEvent.UPDATE && i >-1){
			DefaultTableModel model = (DefaultTableModel)e.getSource();
			String value = (String)model.getValueAt(j, i);
			
			if(string){
				algo.setNewFieldValue(j, i, name, (String) value, false);
			}else{
				try{
				  trans.setTranslatorLocale(Animal.getCurrentLocale());
					Integer intValue = Integer.parseInt(value);
					algo.setNewFieldValue(j, i, name, intValue, false);
				}catch(NumberFormatException n){
					
					JOptionPane.showMessageDialog(null, 
							trans.translateMessage("errorInt"), 
							trans.translateMessage("errorLabel"), JOptionPane.WARNING_MESSAGE);
				}
			}
		}
		
	}

}
