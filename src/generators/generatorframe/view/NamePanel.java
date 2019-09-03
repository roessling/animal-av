package generators.generatorframe.view;


//import generators.generatorframe.view.image.GetIcon;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
//import java.awt.Component;
//import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

//import animal.main.Animal;
//import translator.Translator;

public class NamePanel extends JPanel {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private JLabel            algoName;
  // private JButton back;
  // private Translator trans;

  public void setLabel(String name) {
    algoName.setText(name);
    // super.setMaximumSize(new Dimension(name.length()*20 , 20));
    // super.setPreferredSize(new Dimension(name.length()*20 , 20));
  }

  public NamePanel(String name) {
    super();
    // super.setMaximumSize(new Dimension(name.length()*20 , 20));
    // super.setMinimumSize(new Dimension(name.length()*20 , 20));
    // trans = new Translator("GeneratorFrame", Animal.getCurrentLocale());
    super.setLayout(new BorderLayout());
    // back = new JButton();
    //
    //
    // // back.setText(trans.translateMessage("back.label"));
    //
    // // back.setMaximumSize(new Dimension(10,10));
    //
    //// back.setIcon(getIcon.createBackIcon());
    // GetIcon getIcon = new GetIcon();
    // back.setIcon(getIcon.createBackIcon());
    // back.setMaximumSize(new Dimension(49,26));
    // back.setPreferredSize(new Dimension(49, 26));
    // back.setMinimumSize(new Dimension(49, 26));
    // back.setBackground(Color.white);
    //
    // back.setToolTipText(trans.translateMessage("back.toolTipText"));
    // // back.setName("back");
    // //setListener
    // back.addActionListener(new ActionListener(){
    //
    // @Override
    // public void actionPerformed(ActionEvent e) {
    // // TODO Auto-generated method stub
    // goBack();
    // }
    //
    // });

    // super.add(back, BorderLayout.WEST);
    // super.add(Box.createHorizontalStrut(20));
    JPanel p = new JPanel();
    p.setBackground(Color.white);
    // p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
    // p.add(Box.createHorizontalStrut(20));
    algoName = new JLabel(name);
    algoName.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
    p.add(algoName);
    // p.add(Box.createVerticalGlue());
    // p.setMaximumSize(new Dimension(name.length()*20 , 20));
    // p.setMinimumSize(new Dimension(name.length()*20 , 20));
    p.setBackground(Color.white);
    super.add(p, BorderLayout.CENTER);
    super.setBackground(Color.white);

  }

  // public void goBack(){
  // Component parent = getParent();
  // while(!(parent instanceof GeneratorFrame))
  // parent = parent.getParent();
  //
  // ((GeneratorFrame)parent).goBack();
  // }

  // public void localChanged(){
  // trans.setTranslatorLocale(Animal.getCurrentLocale());
  //// back.setToolTipText(trans.translateMessage("back.toolTipText"));
  //// back.setText(trans.translateMessage("back.label"));
  // }

  /**
   * zooms the tab in
   * 
   * @param zoomIn
   *          if true zooms in, if flase zooms out
   */

  public void zoom(boolean zoomIn) {

    Font f;

    if (algoName != null) {
      f = algoName.getFont();
      if (zoomIn) {
        if (f.getSize() < 24)
          f = new Font(f.getName(), f.getStyle(), f.getSize() + 2);
      } else {
        if (f.getSize() > 10)
          f = new Font(f.getName(), f.getStyle(), f.getSize() - 2);
      }
      algoName.setFont(f);
    }
    /*
     * Dimension dim = this.getSize();
     * 
     * if (zoomIn) {
     * 
     * if (dim.getWidth() <= 1000) { dim.setSize(dim.getWidth() + 20,
     * dim.getHeight() + 20); } } else {
     * 
     * if (dim.getWidth() >= 100) { dim.setSize(dim.getWidth() - 20,
     * dim.getHeight() - 20); }
     * 
     * } this.setSize(dim); this.repaint();
     */

  }

}
