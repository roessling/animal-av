package generators.generatorframe.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import generators.generatorframe.view.image.GetIcon;

import javax.swing.AbstractButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import translator.TranslatableGUIElement;

public class ButtonPanel extends JPanel {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  // private TranslatableGUIElement trans;

  // new global variables
  private AbstractButton    b;
  private AbstractButton    f;
  // private int zoomCounter = 0;

  public ButtonPanel(String back, String forward,
      TranslatableGUIElement trans) {
    super();
    super.setLayout(new BorderLayout());
    super.setBackground(Color.WHITE);
    // this.trans = trans;
    GetIcon get = new GetIcon();

    if (back.compareTo("") != 0) {
      final AbstractButton backB = trans.generateJButton(back);
      backB.setIcon(get.createBackIcon(false));
      backB.setName(back);
      backB.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
      backB.setPreferredSize(new Dimension(118, 30));
      backB.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent arg0) {
          // TODO Auto-generated method stub
          goToTab(backB.getName());
        }

      });

      b = backB;
      super.add(backB, BorderLayout.WEST);
    }

    if (forward.compareTo("") != 0) {
      final AbstractButton forwardB = trans.generateJButton(forward);
      forwardB.setName(forward);
      forwardB.setIcon(get.createForwardIcon(false));
      forwardB.setHorizontalTextPosition(SwingConstants.LEFT);
      forwardB.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
      forwardB.setPreferredSize(new Dimension(118, 30));
      forwardB.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
          // TODO Auto-generated method stub
          goToTab(forwardB.getName());
        }

      });
      f = forwardB;
      super.add(forwardB, BorderLayout.EAST);
    }
  }

  public void goToTab(String name) {
    Component parent = getParent();
    while (!(parent instanceof AlgoTabPanel))
      parent = parent.getParent();

    ((AlgoTabPanel) parent).setTab(name);
  }

  /**
   * zooms the buttons
   * 
   * @param zoomIn
   *          if true zooms in, if flase zooms out
   */

  public void zoom(boolean zoomIn, int zoomCounter ) {

    Font f1;
    Font f2;
    Dimension dim;

    if (b != null) {
      f1 = b.getFont();
      dim = b.getSize();
      if (zoomIn) {
        if (f1.getSize() < 24)
          f1 = new Font(f1.getName(), f1.getStyle(), f1.getSize() + 2);
        if (dim.getWidth() < 198)
          dim.setSize(dim.getWidth() + 15, dim.getHeight() + 5);
        
      } else {
        if (f1.getSize() > 10)
          f1 = new Font(f1.getName(), f1.getStyle(), f1.getSize() - 2);
      
        if (dim.getWidth() > 118)
          dim.setSize(dim.getWidth() - 15, dim.getHeight() - 5);
      }
      b.setFont(f1);
      if (dim.getWidth() < 118 + zoomCounter * 15)
        dim.setSize(118 + zoomCounter * 15, dim.getHeight());

      if (dim.getHeight() < 30 + zoomCounter * 5)
        dim.setSize(dim.getWidth(), 30 + zoomCounter * 5);
      b.setPreferredSize(dim);
      b.setSize(dim);
    }

    if (f != null) {
      f2 = f.getFont();
      dim = f.getSize();
      if (zoomIn) {
        if (f2.getSize() < 24)
          f2 = new Font(f2.getName(), f2.getStyle(), f2.getSize() + 2);
        if (dim.getWidth() < 198)
          dim.setSize(dim.getWidth() + 15, dim.getHeight() + 5);
      } else {
        if (f2.getSize() > 10)
          f2 = new Font(f2.getName(), f2.getStyle(), f2.getSize() - 2);
      
        if (dim.getWidth() > 118)
          dim.setSize(dim.getWidth() - 15, dim.getHeight() - 5);
      }
      f.setFont(f2);
      if (dim.getWidth() < 118 + zoomCounter * 15)
        dim.setSize(118 + zoomCounter * 15, dim.getHeight());

      if (dim.getHeight() < 30 + zoomCounter * 5)
        dim.setSize(dim.getWidth(), 30 + zoomCounter * 5);

      f.setPreferredSize(dim);
      f.setSize(dim);

    }
    
    dim = this.getSize();

    if (zoomIn) {
     

      if (dim.getWidth() <= 1000) {
        dim.setSize(dim.getWidth() + 20, dim.getHeight() + 20);
      }
    } else {
      
      if (dim.getWidth() >= 700) {
        dim.setSize(dim.getWidth() - 20, dim.getHeight() - 20);
      }
    }

    if (dim.getWidth() < 636 + zoomCounter * 20)
      dim.setSize(636 + zoomCounter * 20, dim.getHeight());
    
    if (dim.getHeight() < 30 + zoomCounter * 20)
      dim.setSize(dim.getWidth(), 30 + zoomCounter * 20);


    this.setSize(dim);

    // this.repaint();

  }
}
