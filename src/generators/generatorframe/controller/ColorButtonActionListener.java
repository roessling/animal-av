package generators.generatorframe.controller;

import generators.generatorframe.store.GetInfos;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;

import animal.misc.ColoredSquare;

/**
 * 
 * @author Nora Wester
 *
 */
public class ColorButtonActionListener implements ActionListener {

	private boolean prim;
	private GetInfos algo;
	String name;
	int position = 0;
	
	public ColorButtonActionListener(boolean prim, String name) {
		// TODO Auto-generated constructor stub
		algo = GetInfos.getInstance();
		this.prim = prim;
		this.name = name;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getSource() instanceof JButton){
			JButton button = (JButton) arg0.getSource();
			Color value;
			if(prim) 
				value = (Color) algo.getPrimValue(name);
			else
				value = (Color) algo.getPropValue(name);
			
			Color color = JColorChooser.showDialog(null, name, value);
			if(color != null){
				//button.setBackground(color);
			  button.setIcon(new ColoredSquare(color));
			  StringBuffer text = new StringBuffer();
		    text.append("(").append(color.getRed());
		    text.append(", ").append(color.getGreen()).append(", ");
		    text.append(color.getBlue()).append(")");
		    button.setText(text.toString());
				value = color;
				if(prim)
					algo.setPrimValue(name, value);
				else
					algo.setPropValue(name, value, position);
			}
		}
	}
	
	public void setPosition(int p){
		position = p;
	}

}
