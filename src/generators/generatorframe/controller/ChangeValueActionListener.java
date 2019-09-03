package generators.generatorframe.controller;

import generators.generatorframe.view.valuePanels.StringIntLightPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTextField;

/**
 * 
 * @author Nora Wester
 *
 */

public class ChangeValueActionListener implements ActionListener {

	private TextFieldKeyListener listener;
	private JTextField field;
	
	public ChangeValueActionListener(int type, boolean prim, String key) {
		// TODO Auto-generated constructor stub
		listener = new TextFieldKeyListener(type, prim, key);
	}
	
	public void setJTextField(JTextField field){
	  this.field = field;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() instanceof JButton){
			JButton button = (JButton) e.getSource();
			
			if(button.getParent() instanceof StringIntLightPanel){
				//JTextField field = ((StringIntLightPanel)button.getParent()).getField();
				if(field != null){
				  listener.setValue(field.getText());
				}
			}
		}
	}
	
	public void setPosition(int p){
		listener.setPosition(p);
	}

}
