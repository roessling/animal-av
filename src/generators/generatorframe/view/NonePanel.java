package generators.generatorframe.view;

import java.awt.BorderLayout;
import java.awt.Color;


import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import animal.main.Animal;
import translator.Translator;

public class NonePanel extends JPanel {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  private boolean isPrimitive;
  private Translator trans;
  private JTextArea label;
  
  public NonePanel(boolean isPrimitivePanel){
    super();
    super.setMaximumSize(new Dimension(200, 200));
    isPrimitive = isPrimitivePanel;
    trans = new Translator("GeneratorFrame", Animal.getCurrentLocale());
    super.setLayout(new BorderLayout());
   // super.add(Box.createVerticalStrut(20), BorderLayout.NORTH);
    super.add(Box.createHorizontalStrut(20), BorderLayout.WEST);
    super.setBackground(Color.white);
    
    JPanel p = new JPanel();
    p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
    p.setMaximumSize(new Dimension(200, 200));
    label = new JTextArea();
    label.setEditable(false);
    //label.setMaximumSize(new Dimension(100,100));
    label.setWrapStyleWord(true);
    label.setLineWrap(true);
    label.setCaretPosition(0);
   // label.setMinimumSize(new Dimension(100, 100));
    p.setBackground(Color.WHITE);
    p.add(Box.createVerticalStrut(50));
    p.add(label);
    p.add(Box.createVerticalGlue());
    
    super.add(p, BorderLayout.CENTER);
    
    setLabelText();
    
  }
  
  private void setLabelText(){
    if(isPrimitive){
      label.setText(trans.translateMessage("primitiveInfo"));
    }else{
      label.setText(trans.translateMessage("propertyInfo"));
    }
  }
  
  public void localChanged(){
    trans.setTranslatorLocale(Animal.getCurrentLocale());
    setLabelText();
  }

  public void zoom(boolean zoomIn) {

    Font f;

    if (label != null) {
      f = label.getFont();
      if (zoomIn) {
        if (f.getSize() < 24)
          f = new Font(f.getName(), f.getStyle(), f.getSize() + 2);
      } else {
        if (f.getSize() > 10)
          f = new Font(f.getName(), f.getStyle(), f.getSize() - 2);
      }
      label.setFont(f);
    }
  }

}
