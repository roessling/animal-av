/*
 * Created on 12.11.2004 by T. Ackermann
 */
package generators.framework.components;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * ColorChooserComboBox is a ComboxBox that displays a list of possible styles
 * for Animal Fonts. These are Serif, SansSerif and Monospaced.
 * 
 * @author Guido R&ouml;&szlig;ling <roessling@acm.org>
 * @version 0.2 20080509
 */
public class EnumerationChooserComboBox extends JComboBox implements
    ActionListener {

  /**
   * a generated serial Version UID because EnumerationChooserComboBox is
   * serializable.
   */
  private static final long serialVersionUID = 1252859137398793065L;

  /** stores the possible Font Styles. */
  private String[] possibleValues = null;

  /** stores the renderer that allows us to draw a nice FontChooserComboBox. */
  private EnumerationChooserComboBoxRenderer renderer = new EnumerationChooserComboBoxRenderer();

  /** stores the currently selected Font as String. */
  private String strSelected = null;

  /** stores the values for the FontChooserComboBox-Items */
  private Object[][] values; // = new Object[possibleValues.length][2];

  /** helps avoiding calling actionPerformed too often */
  private boolean bChangeByComponent = false;

  /**
   * Constructor creates a new FontChooserComboBox-Object.
   */
  public EnumerationChooserComboBox() {
    super();
    init();
  }

  /**
   * Constructor creates a new FontChooserComboBox-Object and sets the selected
   * Font.
   * 
   * @param strNew
   *          The default Font. Can be "Serif", "SansSerif" and "Monospaced".
   */
  public EnumerationChooserComboBox(String strNew) {
    super();
    init();
    setChosenElement(strNew);
  }

  /**
   * Constructor creates a new FontChooserComboBox-Object and sets the selected
   * Font.
   * 
   * @param newChoice the choice values
   */
  public EnumerationChooserComboBox(Vector<String> newChoice) {
    super();
    possibleValues = new String[newChoice.size() - 1];
    for (int i = 1; i < newChoice.size(); i++)
      possibleValues[i - 1] = newChoice.elementAt(i);
    init();
    setChosenElement(newChoice.elementAt(0));
  }

  /**
   * Sets the Font that should be selected.
   * 
   * @param strNew
   *          The Font that should be selected. Can be "Serif", "SansSerif" and
   *          "Monospaced".
   */
  public void setChosenElement(String strNew) {
    if (strNew == null) {
      return;
    }

    String newString = strNew.trim().toLowerCase();
    int iNewIndex = -1;

    // look for the Font-String
    for (int i = 0; i < possibleValues.length; i++) {
      if (newString.equals(possibleValues[i].toLowerCase())) {
        strSelected = possibleValues[i];
        iNewIndex = i;
      }
    }

    // return if nothing has been found
    if (iNewIndex == -1) {
      return;
    }

    // select the element
    bChangeByComponent = true;
    setSelectedIndex(iNewIndex);
    bChangeByComponent = false;

    // repaint the ComboBox
    repaint();
  }

  /**
   * Returns the selected Font as a String.
   * 
   * @return Returns the selected Font as a String.
   */
  public String getElementSelectedAsString() {
    return strSelected;
  }

  /**
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  public void actionPerformed(ActionEvent e) {
    if (e == null) {
      return;
    }

    // do nothing if the Component made the change
    if (bChangeByComponent) {
      return;
    }

    int index = getSelectedIndex();

    if ((index < 0) || (index >= possibleValues.length)) {
      return;
    }

    // store the selected Font
    strSelected = possibleValues[index];

  }

  /**
   * init initalizes the FontChooserComboBox. It fills the items, takes care
   * about the drawing, sets the listeners and sets some values.
   */
  private void init() {
    if (values == null)
      values = new String[possibleValues.length][2];
    for (int i = 0; i < possibleValues.length; i++) {

      /*
       * we do this because later on, we can make one of these multilingual...
       * the [0] is the Font-Name and should not be changed, but the [1] is what
       * is displayed
       */
      values[i][0] = possibleValues[i];
      values[i][1] = possibleValues[i];
      addItem(values[i]);
    }

    // we do the drawing...
    setRenderer(renderer);

    // add Action Listener
    addActionListener(this);

    // set default values
    strSelected = possibleValues[0];
//    setFont(new Font(strSelected, Font.PLAIN, 14));
  }

  /*
   * **************************************************** BELOW IS A HELPER
   * CLASSES ****************************************************
   */

  /**
   * FontChooserComboBoxRenderer draws the entries in the FontChooserComboBox.
   * Therefore it uses the values passed to this function.
   * 
   * @author T. Ackermann
   */
  private static class EnumerationChooserComboBoxRenderer extends JLabel
      implements ListCellRenderer {

    /**
     * a generated serial Version UID because FontChooserComboBoxRenderer is
     * serializable.
     */
    private static final long serialVersionUID = 3266835247180289721L;

    /**
     * Constructor creates a new FontChooserComboBoxRenderer-Object.
     */
    public EnumerationChooserComboBoxRenderer() {
      setOpaque(true);
    }

    /**
     * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList,
     *      java.lang.Object, int, boolean, boolean) This method finds the image
     *      and text corresponding to the selected value and returns the label,
     *      set up to display the text and image.
     */
    public Component getListCellRendererComponent(JList list, Object value,
        int index, boolean isSelected, boolean cellHasFocus) {
      if ((list == null) || (value == null)) {
        setText("?");
        return this;
      }

      if ((isSelected || cellHasFocus) && (index != -1)) {
        setBackground(list.getSelectionBackground());
        setForeground(list.getSelectionForeground());
      } else {
        setBackground(list.getBackground());
        setForeground(list.getForeground());
      }

      if (!(value instanceof Object[])) {
        return this;
      }

      Object[] itemValues = (Object[]) value;

      if (!((itemValues[0] instanceof String) && (itemValues[1] instanceof String))) {
        return this;
      }

      String strDisplay = (String) itemValues[0];

      setFont(new Font(strDisplay, Font.PLAIN, 20));
      setText((String) itemValues[1]);

      return this;
    }
  }
}
