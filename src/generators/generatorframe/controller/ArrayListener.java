package generators.generatorframe.controller;

import generators.generatorframe.store.GetInfos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import translator.Translator;
import animal.main.Animal;

public class ArrayListener implements ActionListener, KeyListener, TableModelListener {

  GetInfos algo;
  String name;
  boolean isString;
  Translator trans;
  JTextField position;
  JTextField value;
  
  public ArrayListener(String name, boolean string){
    algo = GetInfos.getInstance();
    this.name = name;
    isString = string;
    trans = new Translator("GeneratorFrame", Animal.getCurrentLocale());
  }
  
  @Override
  public void keyPressed(KeyEvent arg0) {
    // TODO Auto-generated method stub
    if(position == null || value == null){
      if(arg0.getSource() instanceof JTextField){
        JTextField temp = (JTextField)arg0.getSource();
        if(temp.getName().compareTo("position") == 0){
          position = temp;
        }else{
          value = temp;
        }
      }
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

  @Override
  public void actionPerformed(ActionEvent arg0) {
    // TODO Auto-generated method stub
    if(arg0.getSource() instanceof JButton){
      JButton b = (JButton)arg0.getSource();
      if(b.getName().compareTo("edit") == 0){
        if(value == null || position == null || value.getText().compareTo("") == 0 
            || position.getText().compareTo("") == 0){
          JOptionPane.showMessageDialog(null, 
              trans.translateMessage("noValuesLong"),
              trans.translateMessage("noValuesShort"),
              JOptionPane.WARNING_MESSAGE);
        }else{
          edit(0, testInteger(position.getText()), value.getText(), true);
          position.setText("");
          value.setText("");
        }
      }
      
      if(b.getName().compareTo("add") == 0){
        boolean warningOne = false;
        boolean warningTwo = false;
//        StringBuffer sbWarning = new StringBuffer();
        if(value == null || value.getText().compareTo("") == 0) {
//          sbWarning.append(trans.translateMessage("defaultvalue"));
//          sbWarning.append("\n");
          warningOne = true;
        }
        
        if(position == null || position.getText().compareTo("") == 0){
//          sbWarning.append(trans.translateMessage("defaultposition"));
//          sbWarning.append("\n");
          warningTwo = true;
        }
        
        int ok = JOptionPane.OK_OPTION;
        
//        if(warningOne || warningTwo){
//          sbWarning.append(trans.translateMessage("goon"));
//          ok = JOptionPane.showConfirmDialog(null, sbWarning.toString());
//        }
        
        if(ok == JOptionPane.OK_OPTION){
          if(warningOne){
            if(warningTwo){
              algo.addField(null, name, null);
            }else{
              algo.addField(testInteger(position.getText()), name, null);
            }
          }else{
            if(isString){
              if(warningTwo){
                algo.addField(null, name, value.getText());
              }else{
                algo.addField(testInteger(position.getText()), name, value.getText());
              }
            }else{
              if(warningTwo){
                algo.addField(null, name, testInteger(value.getText()));
              }else{
                algo.addField(testInteger(position.getText()), name, testInteger(value.getText()));
              }
            }
          }
        }
      }
      
      if(b.getName().compareTo("delete") == 0){
        
        int ok = JOptionPane.OK_OPTION;
        boolean warning = false;
        
        if(position == null || position.getText().compareTo("") == 0){
//          StringBuffer sbWarning = new StringBuffer();
//          sbWarning.append(trans.translateMessage("defaultposition"));
//          sbWarning.append("\n");
          warning = true;
        
//          sbWarning.append(trans.translateMessage("goon"));
//          ok = JOptionPane.showConfirmDialog(null, sbWarning.toString());
        }
        
        if(ok == JOptionPane.OK_OPTION){
          if(warning){
            algo.deleteField(null, name);
          }else{
            algo.deleteField(testInteger(position.getText()), name);
            position.setText("");
            if(value != null){
              value.setText("");
            }
          }
        }
      }
    }
    if(position != null){
      position.setText("");
    }
    if(value != null){
      value.setText("");
    }
  }

  @Override
  public void tableChanged(TableModelEvent e) {
    // TODO Auto-generated method stub
    int i = e.getColumn();
    int j = e.getFirstRow();
    
    if((e.getSource() instanceof DefaultTableModel) && e.getType() == TableModelEvent.UPDATE && i >-1){
      DefaultTableModel model = (DefaultTableModel)e.getSource();
      String value = (String)model.getValueAt(j, i);
      edit(j, i, value, false);
    }
  }
  
  private int testInteger(String test){
    try{
      trans.setTranslatorLocale(Animal.getCurrentLocale());
      Integer intValue = Integer.parseInt(test);
      return intValue;
    }catch(NumberFormatException n){
      
      JOptionPane.showMessageDialog(null, 
          trans.translateMessage("errorInt"), 
          trans.translateMessage("errorLabel"), JOptionPane.WARNING_MESSAGE);
    }
    
    return 0;
  }

  private void edit(int j, int i, String value2, boolean notify) {
    // TODO Auto-generated method stub
    if(isString){
      algo.setNewFieldValue(j, i, name, value2, notify);
    }else{
      algo.setNewFieldValue(j, i, name, testInteger(value2), notify);
    }
  }

}
