/*
 * Created on 16.04.2005 by T. Ackermann
 */
package generators.framework;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import algoanim.properties.AnimationProperties;

/**
 * This Dialog Box is used when the user wants to add a new AnimationProperties
 * object. The user can chose the type and enter a name.
 * 
 * @author T. Ackermann
 */
public class NewPropertyTypeDialog extends JDialog implements ActionListener {

  /** we store this because NewPropertyTypeDialog is serializable */
  private static final long serialVersionUID = 3617291233572042806L;

  /** stores the Combo Box with the types of AnimationProperties */
  private JComboBox<String> cmbTypes;

  /** stores the TextField for the name */
  private JTextField        txtName;

  /** stores the type of the new AnimationProperties Object */
  private String            type;

  /** stores the name of the new AnimationProperties Object */
  private String            name;

  /** stores the return value */
  private boolean           returnValue;

  /**
   * Constructor creates a new NewPropertyTypeDialog-Object.
   * 
   * @param parent
   *          The parent frame (used to make this dialog modal).
   */
  public NewPropertyTypeDialog(JFrame parent) {
    super(parent, "New Property Type", true);

    JPanel border = new JPanel();
    border.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    this.getContentPane().setLayout(new BorderLayout(8, 8));
    this.getContentPane().add(border, BorderLayout.CENTER);

    this.cmbTypes = new JComboBox<String>(AnimationProperties.getAllPropertyTypes());
    this.cmbTypes.setPreferredSize(new Dimension(250, this.cmbTypes
        .getMinimumSize().height));
    this.cmbTypes.setMaximumSize(new Dimension(Integer.MAX_VALUE, this.cmbTypes
        .getMinimumSize().height));
    this.cmbTypes.setAlignmentX(Component.LEFT_ALIGNMENT);
    this.cmbTypes.setActionCommand("cmbTypes");
    this.cmbTypes.addActionListener(this);

    this.txtName = new JTextField();
    this.txtName.setText("");
    this.name = "";
    this.txtName.setPreferredSize(new Dimension(250, this.txtName
        .getMinimumSize().height));
    this.txtName.setMaximumSize(new Dimension(Integer.MAX_VALUE, this.txtName
        .getMinimumSize().height));
    this.txtName.setAlignmentX(Component.LEFT_ALIGNMENT);

    border.setLayout(new BoxLayout(border, BoxLayout.PAGE_AXIS));
    JLabel lblType = new JLabel("Chose a Property Type:");
    border.add(lblType);
    border.add(Box.createRigidArea(new Dimension(0, 4)));
    border.add(this.cmbTypes);
    border.add(Box.createRigidArea(new Dimension(0, 10)));
    JLabel lblName = new JLabel("Enter a Name:");
    border.add(lblName);
    border.add(Box.createRigidArea(new Dimension(0, 4)));
    border.add(this.txtName);
    border.add(Box.createRigidArea(new Dimension(0, 16)));

    JPanel pBut = new JPanel();
    pBut.setLayout(new BoxLayout(pBut, BoxLayout.LINE_AXIS));
    pBut.setAlignmentX(Component.LEFT_ALIGNMENT);
    pBut.add(Box.createHorizontalGlue());

    JButton btnCancel = new JButton("Cancel");
    btnCancel.setActionCommand("cancel");
    btnCancel.addActionListener(this);
    pBut.add(btnCancel);
    pBut.add(Box.createRigidArea(new Dimension(8, 0)));

    JButton btnOK = new JButton("OK");
    btnOK.setActionCommand("ok");
    btnOK.addActionListener(this);
    pBut.add(btnOK);

    border.add(pBut, BorderLayout.SOUTH);
    this.getRootPane().setDefaultButton(btnOK);
    this.txtName.requestFocus();
    this.pack();
  }

  /**
   * showDialog shows the dialog and returns if the user pressed th OK button.
   * 
   * @return true, if the user pressed the OK button.
   */
  public boolean showDialog() {
    if (this.cmbTypes.getItemCount() > 0) {
      this.cmbTypes.setSelectedIndex(0);
    }
    this.returnValue = false;
    this.setVisible(true);
    return this.returnValue;
  }

  /**
   * createNameForProperty creates a good-looking name for the selected type of
   * AnimationProperties.
   * 
   * @param prop
   *          The selected type of AnimationProperties.
   * @return A Name for the selected type of AnimationProperties.
   */
  private static String createNameForProperty(String prop) {
    if (prop == null)
      return "property";
    String theProp = prop;

    // remove last "AnimationProperties"
    if (theProp.length() > 10
        && theProp.substring(theProp.length() - 10).equalsIgnoreCase(
            "properties"))
      theProp = theProp.substring(0, theProp.length() - 10);
    // first char is lowercase
    if (theProp.length() > 0)
      theProp = theProp.toLowerCase().charAt(0) + theProp.substring(1);
    return theProp;
  }

  /**
   * (non-Javadoc)
   * 
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  public void actionPerformed(ActionEvent e) {
    if (e == null)
      return;

    if (e.getActionCommand().equalsIgnoreCase("cmbTypes")) {
      @SuppressWarnings("unchecked")
      JComboBox<String> cb = (JComboBox<String>) e.getSource();
      this.type = (String) cb.getSelectedItem();
      String newName = createNameForProperty(this.type);
      this.txtName.setText(newName);
    }

    if (e.getActionCommand().equalsIgnoreCase("ok")) {
      this.returnValue = true;
      this.name = this.txtName.getText();
      this.setVisible(false);
    }

    if (e.getActionCommand().equalsIgnoreCase("cancel")) {
      this.returnValue = false;
      this.setVisible(false);
    }
  }

  /**
   * getName returns the selected name for the new AnimationProperties.
   * 
   * @return The selected name for the new AnimationProperties.
   */
  public String getName() {
    return this.name;
  }

  /**
   * getType returns the selected type for the new AnimationProperties.
   * 
   * @return The selected type for the new AnimationProperties.
   */
  public String getAnimationType() {
    return this.type;
  }
}
