package generators.generatorframe.controller;

import generators.generatorframe.store.GetInfos;
import generators.generatorframe.view.valuePanels.StringIntLightPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import animal.main.Animal;
import translator.Translator;

/**
 * 
 * @author Nora Wester
 *
 */

public class TextFieldKeyListener implements KeyListener, ActionListener {

	private int type;
	private boolean prim;
	private String key;
	private GetInfos algo;
	
	private int position = 0;
	private Translator translator;
  private JTextField field;
  private JTextArea fieldA;
	
	public TextFieldKeyListener(int type, boolean prim, String key) {
		// TODO Auto-generated constructor stub
		this.type = type;
		this.prim = prim;
		this.key = key;
		algo = GetInfos.getInstance();
		translator = new Translator("GeneratorFrame", Animal.getCurrentLocale());
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
    if(field == null){
      if(arg0.getSource() instanceof JTextField){
        field = (JTextField) arg0.getSource();
      }
    }
    
    if(field != null && arg0.getKeyCode() == KeyEvent.VK_ENTER){
        setValue(field.getText());
    }
    
    
    if(fieldA == null){
      if(arg0.getSource() instanceof JTextArea){
        fieldA = (JTextArea) arg0.getSource();
      }
    }
    
    if(fieldA != null && arg0.getKeyCode() == KeyEvent.VK_ENTER){
        setValue(fieldA.getText());
    }

	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}
	
	public void setValue(String v){
		
	  translator.setTranslatorLocale(Animal.getCurrentLocale());
	  
		if(type == StringIntLightPanel.INT){
			try{
				Integer test = Integer.parseInt(v);
				if(prim)
					algo.setPrimValue(key, test);
				else
					algo.setPropValue(key, test, position);
			}catch(NumberFormatException e){
				
				JOptionPane.showMessageDialog(null, 
						translator.translateMessage("errorInt"), 
						translator.translateMessage("errorLabel"), JOptionPane.WARNING_MESSAGE);
			}
		}
		
		if(type == StringIntLightPanel.DOUBLE){
			try{
				Double test = Double.parseDouble(v);
				
				if(prim)
					algo.setPrimValue(key, test);
				else
					algo.setPropValue(key, test, position);
				
			}catch(NumberFormatException e){
				//temp.setText(Double.toString((Double)value));
				JOptionPane.showMessageDialog(null, 
						translator.translateMessage("errorDouble"), 
						translator.translateMessage("errorLabel"), JOptionPane.WARNING_MESSAGE);
			}
		}
		
		if(type == StringIntLightPanel.STRING){
			if(prim)
				algo.setPrimValue(key, v);
			else
				algo.setPropValue(key, v, position);
		}


	}
	
	public void setPosition(int p){
		position = p;
	}

  @Override
  public void actionPerformed(ActionEvent arg0) {
    if(field != null){
      setValue(field.getText());
    }
    if(fieldA != null){
      setValue(fieldA.getText());
    }
  }	
}
