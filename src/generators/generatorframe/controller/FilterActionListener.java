package generators.generatorframe.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import generators.generatorframe.filter.FilterCoordinator;

/**
 * 
 * @author Nora Wester
 *
 */

public class FilterActionListener implements ActionListener {

	int type;
	
	public FilterActionListener(int type){
		this.type = type;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getSource() instanceof JComboBox){
			@SuppressWarnings("unchecked")
			JComboBox<String> box = (JComboBox<String>)arg0.getSource();
			String selected = (String)box.getSelectedItem();
			FilterCoordinator.coorinate(selected, type);
		}
	}

}
