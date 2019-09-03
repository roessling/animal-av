package htdptl.gui;


import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CreatingAnimationDialog extends JDialog {
  
  /**
   * 
   */
  private static final long serialVersionUID = 9062380745700713371L;
  
  public CreatingAnimationDialog(JFrame frame) {
    super(frame);
    JPanel myPanel = new JPanel();
    getContentPane().add(myPanel);
    JLabel l = new JLabel("creating animation");
    l.setBorder(BorderFactory.createEmptyBorder(30,60,30,60));    
    myPanel.add(l);
    pack();
    setLocationRelativeTo(frame);
    setVisible(true);
}
  

}

