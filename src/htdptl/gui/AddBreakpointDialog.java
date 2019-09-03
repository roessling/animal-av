package htdptl.gui;

import htdptl.facade.Facade;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AddBreakpointDialog extends JFrame implements ActionListener {

  private static final long serialVersionUID = -7343247972125942355L;
  JComboBox<String>         procedures       = new JComboBox<String>();
  JTextField                times            = new JTextField();

  public AddBreakpointDialog(Facade facade) {

    add(new TopPanel("Breakpoints"), BorderLayout.PAGE_START);

    JButton cancel = new JButton("cancel");
    cancel.addActionListener(this);
    cancel.setActionCommand("cancel");
    JButton ok = new JButton("OK");
    ok.addActionListener(this);
    ok.setActionCommand("ok");

    JPanel container = new JPanel();
    container.setLayout(new BorderLayout());
    container.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(0, 2, 10, 10));
    panel.add(new JLabel("procedure: "));
    panel.add(procedures);
    panel.add(new JLabel("show ... recursions: "));
    panel.add(times);
    panel.add(cancel);
    panel.add(ok);

    for (Iterator<Object> iterator = facade.getDefinitions().iterator(); iterator
        .hasNext();) {
      Object procedure = iterator.next();
      procedures.addItem(procedure.toString());
    }
    procedures.addItem("map");
    procedures.addItem("filter");
    procedures.addItem("foldl");
    procedures.addItem("foldr");

    JLabel info = new JLabel(
        "<html> Use breakpoints for recursive procedures. "
            + "The given amount of recursions will be shown. Afterwards all steps between the procedure calls will be hidden.");
    info.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

    container.add(info, BorderLayout.PAGE_START);
    container.add(panel, BorderLayout.PAGE_END);

    add(container, BorderLayout.PAGE_END);
    setPreferredSize(new Dimension(350, 280));
    pack();

    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    int w = getSize().width;
    int h = getSize().height;
    int x = (dim.width - w) / 2;
    int y = (dim.height - h) / 2;
    setLocation(x, y);

    setVisible(true);

  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals("ok")) {
      FilterTableModel.getInstance()
          .addBreakpoint(procedures.getSelectedItem().toString(),
              new Integer(times.getText()));
      setVisible(false);
      dispose();
    } else if (e.getActionCommand().equals("cancel")) {
      setVisible(false);
      dispose();
    }

  }

}
