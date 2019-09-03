/*
 * Created on 29.04.2005 by T. Ackermann
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

/**
 * This Dialog Box is used when the user wants to add a new Primitive. The user
 * can chose the Type and enter a Name.
 * 
 * @author T. Ackermann
 */
public class NewPrimitiveDialog extends JDialog implements ActionListener {

  /** we store this because NewPrimitiveDialog is serializable */
  private static final long serialVersionUID = 3689910665234233655L;

  /** stores the Combo Box with the Primitive Types */
  private JComboBox<String> cmbTypes;

  /** stores the TextField for the Name */
  private JTextField        txtName;

  /** stores the Type of the new Primitive Object */
  private String            type;

  /** stores the Name of the new Primitives Object */
  private String            name;

  /** stores the returnValue */
  private boolean           returnValue;

  /**
   * Constructor creates a new NewPropertyTypeDialog-Object.
   * 
   * @param parent
   *          The parent Frame (used to make this Dialog modal).
   */
  public NewPrimitiveDialog(JFrame parent) {
    super(parent, "New Primitive", true);

    JPanel border = new JPanel();
    border.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    getContentPane().setLayout(new BorderLayout(8, 8));
    getContentPane().add(border, BorderLayout.CENTER);

    String[] primitives = new String[] { "boolean", "double", "int", "Color",
        "Font", "String", "int[]", "int[][]", "String[]", "String[][]", "Graph" };

    // {"String", "int", "boolean", "double",
    // "int[]", "String[]", "Color", "Font", "int[][]"};
    cmbTypes = new JComboBox<String>(primitives);
    cmbTypes.setPreferredSize(new Dimension(250,
        cmbTypes.getMinimumSize().height));
    cmbTypes.setMaximumSize(new Dimension(Integer.MAX_VALUE, cmbTypes
        .getMinimumSize().height));
    cmbTypes.setAlignmentX(Component.LEFT_ALIGNMENT);
    cmbTypes.setActionCommand("cmbTypes");
    cmbTypes.addActionListener(this);

    txtName = new JTextField("");
    name = "";
    txtName
        .setPreferredSize(new Dimension(250, txtName.getMinimumSize().height));
    txtName.setMaximumSize(new Dimension(Integer.MAX_VALUE, txtName
        .getMinimumSize().height));
    txtName.setAlignmentX(Component.LEFT_ALIGNMENT);

    border.setLayout(new BoxLayout(border, BoxLayout.PAGE_AXIS));
    JLabel lblType = new JLabel("Chose a Primitive Type:");
    border.add(lblType);
    border.add(Box.createRigidArea(new Dimension(0, 4)));
    border.add(cmbTypes);
    border.add(Box.createRigidArea(new Dimension(0, 10)));
    JLabel lblName = new JLabel("Enter a Name:");
    border.add(lblName);
    border.add(Box.createRigidArea(new Dimension(0, 4)));
    border.add(txtName);
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
    getRootPane().setDefaultButton(btnOK);
    txtName.requestFocus();
    pack();
  }

  /**
   * showDialog shows the dialog and returns if the user pressed th OK button.
   * 
   * @return true, if the user pressed the OK button.
   */
  public boolean showDialog() {
    if (cmbTypes.getItemCount() > 0) {
      cmbTypes.setSelectedIndex(0);
    }
    returnValue = false;
    setVisible(true);
    return returnValue;
  }

  /**
   * createNameForPrimitive creates a good-looking name for the selected type of
   * Primitive.
   * 
   * @param type
   *          The selected type of Primitive.
   * @return A name for the selected type of Primitive.
   */
  private static String createNameForPrimitive(String type) {
    String theType = type;
    if (theType == null)
      return "primitive";

    // replace "[][]" with "Matrix"
    if (theType.endsWith("[][]"))
      theType = theType.substring(0, theType.length() - 4) + "Matrix";
    // replace "[]" with "Array"
    if (theType.endsWith("[]"))
      theType = theType.substring(0, theType.length() - 2) + "Array";

    // first char is lowercase
    if (theType.length() > 0)
      theType = theType.toLowerCase().charAt(0) + theType.substring(1);
    return theType;
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
      type = (String) cb.getSelectedItem();
      String newName = createNameForPrimitive(type);
      txtName.setText(newName);
    }

    if (e.getActionCommand().equalsIgnoreCase("ok")) {
      returnValue = true;
      name = txtName.getText();
      setVisible(false);
    }

    if (e.getActionCommand().equalsIgnoreCase("cancel")) {
      returnValue = false;
      setVisible(false);
    }
  }

  /**
   * getName returns the selected name for the new AnimationProperties.
   * 
   * @return The selected name for the new AnimationProperties.
   */
  public String getName() {
    return name;
  }

  /**
   * getType returns the selected type for the new AnimationProperties.
   * 
   * @return The selected type for the new AnimationProperties.
   */
  public String getAnimationType() {
    return type;
  }
}
