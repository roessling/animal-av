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

import animal.main.Animal;
import translator.Translator;

public class MatrixListener implements ActionListener, KeyListener,
    TableModelListener {

  GetInfos algo;
  String name;
  boolean isString;
  Translator trans;
  JTextField xposition;
  JTextField yposition;
  JTextField value;
  
  public MatrixListener(String name, boolean isString){
    algo = GetInfos.getInstance();
    this.name = name;
    this.isString = isString;
    trans = new Translator("GeneratorFrame", Animal.getCurrentLocale());
  }
  
  @Override
  public void tableChanged(TableModelEvent arg0) {
    // TODO Auto-generated method stub
    int i = arg0.getColumn();
    int j = arg0.getFirstRow();
    
    if((arg0.getSource() instanceof DefaultTableModel) && arg0.getType() == TableModelEvent.UPDATE && i >-1){
      DefaultTableModel model = (DefaultTableModel)arg0.getSource();
      String value = (String)model.getValueAt(j, i);
      edit(j, i, value, false);
    }
  }

  @Override
  public void keyPressed(KeyEvent arg0) {
    // TODO Auto-generated method stub
    if(xposition == null || value == null || yposition == null){
      if(arg0.getSource() instanceof JTextField){
        JTextField temp = (JTextField)arg0.getSource();
        if(temp.getName().compareTo("xposition") == 0){
          xposition = temp;
        }else{
          if(temp.getName().compareTo("yposition") == 0){
            yposition = temp;
          }else{
            value = temp;
          }
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

  //x beschreibt die Row  -  y beschreibt die Column
  @Override
  public void actionPerformed(ActionEvent arg0) {
    // TODO Auto-generated method stub
    if(arg0.getSource() instanceof JButton){
      JButton b = (JButton)arg0.getSource();
      if(b.getName().compareTo("edit") == 0){
        if(value == null || xposition == null || value.getText().compareTo("") == 0 
            || xposition.getText().compareTo("") == 0 || yposition == null || yposition.getText().compareTo("") == 0){
          JOptionPane.showMessageDialog(null, 
              trans.translateMessage("noValuesLong"),
              trans.translateMessage("noValuesShort"),
              JOptionPane.WARNING_MESSAGE);
        }else{
          edit(testInteger(xposition.getText()), testInteger(yposition.getText()), value.getText(), true);
        }
      }
      
      if(b.getName().startsWith("add")){
        boolean warningOne = false;
        boolean warningTwo = false;
  //      StringBuffer sbWarning = new StringBuffer();
        if(value == null || value.getText().compareTo("") == 0) {
//          sbWarning.append(trans.translateMessage("defaultvalue"));
//          sbWarning.append("\n");
          warningOne = true;
        }
        
        if(b.getName().endsWith("Row")){
          if(xposition == null || xposition.getText().compareTo("") == 0){
//            sbWarning.append(trans.translateMessage("defaultposition"));
//            sbWarning.append("\n");
            warningTwo = true;
          }
        }else{
          if(yposition == null || yposition.getText().compareTo("") == 0){
//            sbWarning.append(trans.translateMessage("defaultposition"));
//            sbWarning.append("\n");
            warningTwo = true;
          }
        }
        
        int ok = JOptionPane.OK_OPTION;
        
        if(warningOne || warningTwo){
//          sbWarning.append(trans.translateMessage("goon"));
//          ok = JOptionPane.showConfirmDialog(null, sbWarning.toString());
        }
        
        if(ok == JOptionPane.OK_OPTION){
          if(warningOne){
            if(b.getName().endsWith("Row")){
              if(warningTwo){
                algo.addRow(null, name, null);
              }else{
                algo.addRow(testInteger(xposition.getText()), name, null);
              }
            }else{
              if(warningTwo){
                algo.addField(null, name, null);
              }else{
                algo.addField(testInteger(yposition.getText()), name, null);
              }
            }
          }else{
            if(isString){
              if(b.getName().endsWith("Row")){
                if(warningTwo){
                  algo.addRow(null, name, value.getText());
                }else{
                  algo.addRow(testInteger(xposition.getText()), name, value.getText());
                }
              }else{
                if(warningTwo){
                  algo.addField(null, name, value.getText());
                }else{
                  algo.addField(testInteger(yposition.getText()), name, value.getText());
                }
              }
            }else{
              if(b.getName().endsWith("Row")){
                if(warningTwo){
                  algo.addRow(null, name, testInteger(value.getText()));
                }else{
                  algo.addRow(testInteger(xposition.getText()), name, testInteger(value.getText()));
                }
              }else{
                if(warningTwo){
                  algo.addField(null, name, testInteger(value.getText()));
                }else{
                  algo.addField(testInteger(yposition.getText()), name, testInteger(value.getText()));
                }
              } 
            }
          }
        }
      }
      
      if(b.getName().startsWith("delete")){
        
        int ok = JOptionPane.OK_OPTION;
        boolean warning = false;
  //      StringBuffer sbWarning = new StringBuffer();
        
        if(b.getName().endsWith("Row")){
          if(xposition == null || xposition.getText().compareTo("") == 0){
//            sbWarning.append(trans.translateMessage("defaultposition"));
//            sbWarning.append("\n");
//            sbWarning.append(trans.translateMessage("goon"));
//            ok = JOptionPane.showConfirmDialog(null, sbWarning.toString());
            warning = true;
          }
        }else{
          if(yposition == null || yposition.getText().compareTo("") == 0){
//            sbWarning.append(trans.translateMessage("defaultposition"));
//            sbWarning.append("\n");
//            sbWarning.append(trans.translateMessage("goon"));
//            ok = JOptionPane.showConfirmDialog(null, sbWarning.toString());
            warning = true;
          }
        }
        
        if(ok == JOptionPane.OK_OPTION){
          if(warning){
            if(b.getName().endsWith("Row")){
              algo.deleteRow(null, name);
            }else{
              algo.deleteField(null, name);
            }
          }else{
            if(b.getName().endsWith("Row")){
              algo.deleteRow(testInteger(xposition.getText()), name);
            }else{
              algo.deleteField(testInteger(yposition.getText()), name);
            }
          }
        }
      }
    }
    
    if(xposition != null){
      xposition.setText("");
    }
    
    if(yposition != null){
      yposition.setText("");
    }
    
    if(value != null){
      value.setText("");
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
