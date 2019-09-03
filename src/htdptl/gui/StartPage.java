package htdptl.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

public class StartPage extends JPanel {

  /**
	 * 
	 */
  private static final long serialVersionUID = -4406566428949078766L;
  private ButtonGroup       group            = new ButtonGroup();

  public StartPage() {

    setLayout(new BorderLayout());

    TopPanel topPanel = new TopPanel("Please choose an option below:");

    JRadioButton enter = new JRadioButton("Enter a HtDP-TL program.");
    enter.setActionCommand("enter");
    enter.setSelected(true);

    JRadioButton load = new JRadioButton("Load a HtDP-TL program.");
    load.setActionCommand("load");

    JRadioButton example = new JRadioButton(
        "Choose a HtDP-TL program from the example collection.");
    example.setActionCommand("example");

    JRadioButton batch = new JRadioButton("Batch modus.");
    batch.setVerticalAlignment(SwingConstants.BOTTOM);
    batch.setActionCommand("batch");

    // Group the radio buttons.
    group.add(enter);
    group.add(load);
    group.add(example);
    group.add(batch);

    JPanel buttons = new JPanel();
    buttons.setLayout(new BoxLayout(buttons, BoxLayout.PAGE_AXIS));
    buttons.add(enter);
    buttons.add(load);
    buttons.add(example);
    buttons.add(batch);
    buttons.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    String text = "<html> The batch modus allows you to specify a source directory and a target directory. For each HtDP-TL program in the source directory a corresponding visualizaton is generated and stored in the target directory.";
    JLabel textArea = new JLabel(text);
    buttons.add(Box.createRigidArea(new Dimension(0, 20)));
    buttons.add(textArea);

    add(topPanel, BorderLayout.PAGE_START);
    add(buttons, BorderLayout.CENTER);

  }

  public String getChoice() {
    return group.getSelection().getActionCommand();
  }

  public void setChoice(String mode) {
    Enumeration<AbstractButton> elements = group.getElements();
    while (elements.hasMoreElements()) {
      AbstractButton ab = elements.nextElement();
      if (ab.getActionCommand().equals(mode)) {
        group.setSelected(ab.getModel(), true);
      }

    }

  }

}
