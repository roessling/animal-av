package generators.generatorframe.view;

//import generators.generatorframe.controller.ButtonActionListener;
import generators.generatorframe.store.GetInfos;
import generators.generatorframe.view.image.GetIcon;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import translator.TranslatableGUIElement;

public class TabPanelScript extends JPanel {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  //private TranslatableGUIElement trans;
  private NamePanel name;
  private JTextArea script;
  private GetInfos algo;

  private boolean noScript = false;
  private JLabel ghostLabel;
  private AbstractButton    aktuell;

  public TabPanelScript(int width, TranslatableGUIElement trans){
    super();
  //  this.trans = trans;
    super.setLayout(new BorderLayout());
    algo = GetInfos.getInstance();
    
    name = new NamePanel(algo.getGeneratorName());
    super.add(name, BorderLayout.NORTH);
    
    JPanel middle = new JPanel();
    middle.setLayout(new BorderLayout());
    
    JPanel button = new JPanel();
    button.setLayout(new BoxLayout(button, BoxLayout.X_AXIS));
    button.setBackground(Color.white);
    
    GetIcon get = new GetIcon();
    
    aktuell = trans.generateJButton("refreshA");
    aktuell.setIcon(get.createRefreshIcon());
    aktuell.addActionListener(new ActionListener(){

      @Override
      public void actionPerformed(ActionEvent arg0) {
        // TODO Auto-generated method stub
        refresh();
      }
      
    });
    button.add(aktuell);
    
//    AbstractButton export = trans.generateJButton("export");
//    export.setName("export");
//    export.setIcon(get.createExportIcon());
//    export.addActionListener(new ButtonActionListener());
//    
//    button.add(export);
    
    middle.add(button, BorderLayout.NORTH);
    
    ghostLabel = trans.generateJLabel("noScript");
    String textS = ghostLabel.getText();
    try{
      textS = algo.createContent(false);
    }catch(Exception e){
      noScript = true;
    }
    script = new JTextArea();
    script.setEditable(false);
  //  text.setContentType("text/html");
    script.setText(textS);
    script.setWrapStyleWord(true);
    script.setLineWrap(true);
    script.setCaretPosition(0);
    script.setMargin(new Insets(10, 10, 10, 10));
    
    JScrollPane scroll = new JScrollPane(script, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    middle.add(scroll, BorderLayout.CENTER);
    
    super.add(middle, BorderLayout.CENTER);
 //   super.add(new ButtonPanel("descriptionT", "primitives", trans), BorderLayout.SOUTH);
  
  }
  
  public void setContent(){
    noScript = false;
    name.setLabel(algo.getGeneratorName());
    refresh();
  }
  
  public void setLocale(){
    if(noScript){
      script.setText(ghostLabel.getText());
    }
  }
  
  public void refresh(){
    String textS = ghostLabel.getText();
    try{
      textS = algo.createContent(false);
    }catch(Exception e){
      noScript = true;
    }
    script.setText(textS);
    script.setCaretPosition(0);
  }

  /**
   * zooms the tab in
   * 
   * @param zoomIn
   *          if true zooms in, if flase zooms out
   */

  public void zoom(boolean zoomIn) {

    Font f;
    if (script != null) {
      f = script.getFont();
      if (zoomIn) {
        if (f.getSize() < 24)
          f = new Font(f.getName(), f.getStyle(), f.getSize() + 2);
      } else {
        if (f.getSize() > 10)
          f = new Font(f.getName(), f.getStyle(), f.getSize() - 2);
      }
      script.setFont(f);
    }

    if (ghostLabel != null) {
      f = ghostLabel.getFont();
      if (zoomIn) {
        if (f.getSize() < 24)
          f = new Font(f.getName(), f.getStyle(), f.getSize() + 2);
      } else {
        if (f.getSize() > 10)
          f = new Font(f.getName(), f.getStyle(), f.getSize() - 2);
      }
      ghostLabel.setFont(f);
    }

    if (aktuell != null) {
      f = aktuell.getFont();
      if (zoomIn) {
        if (f.getSize() < 24)
          f = new Font(f.getName(), f.getStyle(), f.getSize() + 2);
      } else {
        if (f.getSize() > 10)
          f = new Font(f.getName(), f.getStyle(), f.getSize() - 2);
      }
      aktuell.setFont(f);
    }

    if (name != null) {
      name.zoom(zoomIn);

    }


    Dimension dim = this.getSize();

    if (zoomIn) {

      if (dim.getWidth() <= 980) {
        dim.setSize(dim.getWidth() + 20, dim.getHeight() + 20);
      }
    } else {

      if (dim.getWidth() >= 260) {
        dim.setSize(dim.getWidth() - 20, dim.getHeight() - 20);
      }

    }
    this.setSize(dim);
    this.repaint();

  }
}
