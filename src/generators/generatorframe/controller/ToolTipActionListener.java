package generators.generatorframe.controller;

import generators.generatorframe.view.InfoFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import animal.main.Animal;
import translator.Translator;

/**
 * 
 * @author Nora Wester
 *
 */

public class ToolTipActionListener implements ActionListener {

  public static final int SEARCH = 0;
  public static final int ARRAY = 1;
  
  private int type;
  private Translator trans;
  private InfoFrame info;
  
  public ToolTipActionListener(int type){
    this.type = type;
    trans = new Translator("GeneratorFrame", Animal.getCurrentLocale());
  }
  
  @Override
  public void actionPerformed(ActionEvent arg0) {
    // TODO Auto-generated method stub
    
    trans.setTranslatorLocale(Animal.getCurrentLocale());
    
    if(type == SEARCH){
      info = new InfoFrame(trans.translateMessage("infoSearch.toolTipText"),trans.translateMessage("infoSearchFrame"), null);
    }
    if(type == ARRAY){
      info = new InfoFrame(trans.translateMessage("infoArrayLabel"), trans.translateMessage("infoArray"), null);
    }
  }
  
  public void changeLocale() {
    // TODO Auto-generated method stub
    if(info != null){
      trans.setTranslatorLocale(Animal.getCurrentLocale());
      String text = ""; 
      String title = "";
      if(type == SEARCH){
        text = trans.translateMessage("infoSearchFrame");
        title = trans.translateMessage("infoSearch.toolTipText");
      }
      if(type == ARRAY){
        text  = trans.translateMessage("infoArray");
        title = trans.translateMessage("infoArrayLabel");
      }
      info.setNewText(text, title);
    }
  }

  public InfoFrame getInfoframe() {
    return info;
  }
}
