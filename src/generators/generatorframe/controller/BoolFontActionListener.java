package generators.generatorframe.controller;

import generators.generatorframe.store.GetInfos;
import generators.generatorframe.view.valuePanels.BooleanFontLightPanel;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

/**
 * 
 * @author Nora Wester
 *
 */
public class BoolFontActionListener implements ActionListener {

	private boolean prim;
	private int type;
	private String key;
	private GetInfos algo;
	private int position = 0;
	
	public BoolFontActionListener(boolean prim, int type, String key){
		this.prim = prim;
		this.type = type;
		this.key = key;
		algo = GetInfos.getInstance();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() instanceof JComboBox){
			
			@SuppressWarnings("unchecked")
			JComboBox<String> box = (JComboBox<String>) e.getSource();
			
			if(type == BooleanFontLightPanel.BOOLEAN){
				int index = box.getSelectedIndex();
				if(index == 0){
					if(prim)
						algo.setPrimValue(key, new Boolean(true));
					else
						algo.setPropValue(key, new Boolean(true), position);
				}else{
					if(prim)
						algo.setPrimValue(key, new Boolean(false));
					else
						algo.setPropValue(key, new Boolean(false), position);
				}
			}else{
				
				Font value;
				if(prim)
					value = (Font) algo.getPrimValue(key);
				else;
					value = (Font) algo.getPropValue(key);
				
				int style = value.getStyle();
				int size = value.getSize();
				
				Font newF = new Font((String) box.getSelectedItem(), style, size);
				if(prim)
					algo.setPrimValue(key, newF);
				else
					algo.setPropValue(key, newF, position);
		
			}
		}
	}
	
	public void setPosition(int p){
		position = p;
	}

}
