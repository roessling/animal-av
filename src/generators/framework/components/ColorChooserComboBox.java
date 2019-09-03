/*
 * Created on 05.11.2004 by T. Ackermann
 */

package generators.framework.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * ColorChooserComboBox is a ComboxBox that displays the Animal Colors and lets
 * the user choose one of them. The last element in the box allows the user to
 * freely choose a custom color via the ColorChooserDialog.
 * 
 * @author T. Ackermann
 */
public class ColorChooserComboBox extends JComboBox implements ActionListener {

  /**
   * a generated serial Version UID because ColorChooserComboBox is
   * serializable.
   */
  private static final long serialVersionUID = -984850815478582378L;

  /** stores the Animal Names for the Colors. */
  private static final String[] strAnimalNames = { "black", "dark Gray",
      "gray", "light Gray", "white", "red", "pink", "orange", "yellow",
      "green", "cyan", "blue", "magenta", "blue4", "blue3", "blue2",
      "light_blue", "green4", "green3", "green2", "cyan4", "cyan3", "cyan2",
      "red4", "red3", "red2", "magenta4", "magenta3", "magenta2", "brown4",
      "brown3", "brown2", "pink4", "pink3", "pink2", "gold" };

  /** stores the Keys for the Translator */
  private static final String[] strTranslatorKeys = { "black", "darkGray",
      "gray", "lightGray", "white", "red", "pink", "orange", "yellow", "green",
      "cyan", "blue", "magenta", "blue4", "blue3", "blue2", "light_blue",
      "green4", "green3", "green2", "cyan4", "cyan3", "cyan2", "red4", "red3",
      "red2", "magenta4", "magenta3", "magenta2", "brown4", "brown3", "brown2",
      "pink4", "pink3", "pink2", "gold", "moreColors", "customColor",
      "colorChooserTitle" };

  /** stores the Color values for the Animal Colors. */
  static final Color[] colorValues = { Color.black, Color.darkGray, Color.gray,
      Color.lightGray, Color.white, Color.red, Color.pink, Color.orange,
      Color.yellow, Color.green, Color.cyan, Color.blue, Color.magenta,
      new Color(0, 0, 0x90), new Color(0, 0, 0xb0), new Color(0, 0, 0xd0),
      new Color(0x87, 0xce, 0xff), new Color(0, 0x90, 0),
      new Color(0, 0xb0, 0), new Color(0, 0xd0, 0), new Color(0, 0x90, 0x90),
      new Color(0, 0xb0, 0xb0), new Color(0, 0xd0, 0xd0),
      new Color(0x90, 0, 0), new Color(0xb0, 0, 0), new Color(0xd0, 0, 0),
      new Color(0x90, 0, 0x90), new Color(0xb0, 0, 0xb0),
      new Color(0xd0, 0, 0xd0), new Color(0x80, 0x30, 0),
      new Color(0xa0, 0x40, 0), new Color(0xc0, 0x60, 0),
      new Color(0xff, 0x80, 0x80), new Color(0xff, 0xa0, 0xa0),
      new Color(0xff, 0xe0, 0xe0), new Color(0xff, 0xd7, 0) };

  /** stores the localised Names for the Colors. */
  static String[] strLocalStrings = { "Black", "Dark Gray", "Gray",
      "Light Gray", "White", "Red", "Pink", "Orange", "Yellow", "Green",
      "Cyan", "Blue", "Magenta", "Blue 4", "Blue 3", "Blue 2", "Light Blue",
      "Green 4", "Green 3", "Green 2", "Cyan 4", "Cyan 3", "Cyan 2", "Red 4",
      "Red 3", "Red 2", "Magenta 4", "Magenta 3", "Magenta 2", "Brown 4",
      "Brown 3", "Brown 2", "Pink 4", "Pink 3", "Pink 2", "Gold",
      "More Colors...", "Custom Color", "Please choose a Color" };

  /** stores the currently selected Color as Color. */
  private Color colorSelected = Color.BLACK;

  /**
   * stores the renderer that allows us to draw a nice ColorChooserComboBox.
   */
  private ComboBoxRenderer renderer = new ComboBoxRenderer();

  /** stores the currently selected Color as String. */
  private String strSelected = "black";

  /** stores the values for the ColorChooserComboBox-Items */
  private Object[][] values = new Object[strAnimalNames.length + 1][2];

  /** helps avoiding calling actionPerformed too often */
  private boolean bChangeByComponent = false;

  /**
   * Constructor creates a new ColorChooserComboBox-Object.
   */
  public ColorChooserComboBox() {
    super();
    init();
  }

  /**
   * Constructor creates a new ColorChooserComboBox-Object and sets the selected
   * Color.
   * 
   * @param colorNew
   *          The default Color.
   */
  public ColorChooserComboBox(Color colorNew) {
    super();
    init();
    setColorSelected(colorNew);
  }

  /**
   * Constructor creates a new ColorChooserComboBox-Object and sets the selected
   * Color via a String
   * 
   * @param strNewColor
   *          The default Color as an Animal-Color-String.
   */
  public ColorChooserComboBox(String strNewColor) {
    super();
    init();
    setColorSelected(strNewColor);
  }

  /**
   * Sets the Color that should be selected.
   * 
   * @param colorNew
   *          The Color that should be selected.
   */
  public void setColorSelected(Color colorNew) {
    if (colorNew == null) {
      return;
    }

    this.colorSelected = colorNew;

    // search for Animal Name
    for (int i = 0; i < colorValues.length; i++) {
      if (colorValues[i].equals(colorNew)) {

        // if the color is found withing the Animal Colors, then this
        // element is chosen
        this.strSelected = strAnimalNames[i];
        this.bChangeByComponent = true;
        this.setSelectedIndex(i);
        this.bChangeByComponent = false;
        this.repaint();
        return;
      }
    }

    // the Color wasn't found, so we use the RGB Color String
    this.strSelected = getStringForColorRGB(colorNew);

    // now we change the Text and Color of the "More Colors ..."-Entry
    int iLastIndex = colorValues.length;

    if (!(this.getItemAt(iLastIndex) instanceof Object[])) {
      return;
    }

    Object[] lastitem = (Object[]) this.getItemAt(iLastIndex);

    if (!(lastitem[0] instanceof ColoredSquare)) {
      return;
    }

    ColoredSquare csq = (ColoredSquare) lastitem[0];

    csq.changeColor(colorNew);

    // select the "Custom Color" element
    this.bChangeByComponent = true;
    this.setSelectedIndex(iLastIndex);
    this.bChangeByComponent = false;

    // repaint the ComboBox
    this.repaint();
  }

  /**
   * Sets the Color that should be selected via an Animal-Color-String.
   * 
   * @param strNewColor
   *          The Color that should be selected as an Animal-Color-String or an
   *          RGB String.
   */
  public void setColorSelected(String strNewColor) {
    try {
      Color colorNew = getColorForString(strNewColor);
      // now we have a Color and we can use our other function
      setColorSelected(colorNew);
    } catch (IllegalArgumentException e) {
      // maybe we have an Animal Color name...
      for (int i = 0; i < colorValues.length; i++) {
        if (strAnimalNames[i].equals(strNewColor)) {

          // if the color is found withing the Animal Colors, then
          // this element is chosen
          this.strSelected = strNewColor;
          this.colorSelected = colorValues[i];
          this.bChangeByComponent = true;
          this.setSelectedIndex(i);
          this.bChangeByComponent = false;
          this.repaint();
          return;
        }
      }
    }
  }

  /**
   * Returns the selected Color.
   * 
   * @return Returns the selected Color.
   */
  public Color getColorSelected() {
    return this.colorSelected;
  }

  /**
   * Returns the selected Color as an Animal-Color-String.
   * 
   * @return Returns the selected Color as an Animal-Color-String.
   */
  public String getColorSelectedAsString() {
    return this.strSelected;
  }

  /**
   * setLocalStrings is called by the Translator to change the Strings in the
   * ColorChooserComboBox.
   * 
   * @param newLocalStrings
   *          The localized Strings that should be used.
   */
  public void setLocalStrings(String[] newLocalStrings) {
    if ((newLocalStrings == null)
        || (newLocalStrings.length != strTranslatorKeys.length)) {
      return;
    }

    // test if newLocalStrings are the old local Strings
    boolean bDifferent = false;
    for (int i = 0; i < newLocalStrings.length; i++) {
      if (!strLocalStrings[i].equals(newLocalStrings[i])) {
        bDifferent = true;
        break;
      }
    }

    if (!bDifferent)
      return;

    // update the ColorChooserComboBox
    for (int i = 0; i < newLocalStrings.length; i++) {
      strLocalStrings[i] = newLocalStrings[i];
    }

    Color selected = this.getColorSelected();

    this.init();
    this.bChangeByComponent = true;
    this.setColorSelected(selected);
    this.bChangeByComponent = false;
  }

  /**
   * getStringForColor returns the appropriate String for the Color.
   * 
   * @param colorObject
   *          The Color.
   * @return The Color String for the Color.
   */
  public static String getStringForColor(Color colorObject) {

    // first look in the Animal Color Table
    for (int i = 0; i < colorValues.length; i++) {
      if (colorValues[i].equals(colorObject)) {

        // if the color is found withing the Animal Colors, then this
        // element is chosen
        return strAnimalNames[i];
      }
    }

    // the Color wasn't found, so we use the RGB Color String
    return getStringForColorRGB(colorObject);
  }

  /**
   * getTranslatorKeys is called by the Translator to get the Keys for the
   * Strings used in the ColorChooserComboBox.
   * 
   * @return The Keys that are used in the ColorChooserComboBox.
   */
  public String[] getTranslatorKeys() {
    String[] retval = new String[strTranslatorKeys.length];

    for (int i = 0; i < retval.length; i++) {
      retval[i] = strTranslatorKeys[i];
    }

    return retval;
  }

  /**
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  public void actionPerformed(ActionEvent e) {
    if (e == null) {
      return;
    }

    // do nothing if the Component made the change
    if (this.bChangeByComponent) {
      return;
    }

    int index = this.getSelectedIndex();

    if (index < 0) {
      return;
    }

    // if not the last element has been chosen
    if (index < colorValues.length) {

      // "normal" color selected
      this.colorSelected = colorValues[index];
      this.strSelected = strAnimalNames[index];
    } else {

      // show JColorChooser Dialog
      Color colorNew = JColorChooser.showDialog(this,
          strLocalStrings[colorValues.length + 2], this.colorSelected);

      // catch cancel-action
      if (colorNew == null) {
        return;
      }

      // set the selected Color
      setColorSelected(colorNew);
    }
  }

  /**
   * getStringForColorRGB returns the appropriate String for the new Color, but
   * doesn't look if an Animal Color exists that is like the Color.
   * 
   * @param colorObject
   *          The Color.
   * @return The RGB Color String for the Color.
   */
  public static String getStringForColorRGB(Color colorObject) {

    // create a RGB Color String
    StringBuilder buf = new StringBuilder(15);

    buf.append("(");
    buf.append(Integer.toString(colorObject.getRed()));
    buf.append(", ");
    buf.append(Integer.toString(colorObject.getGreen()));
    buf.append(", ");
    buf.append(Integer.toString(colorObject.getBlue()));
    buf.append(")");

    return buf.toString();
  }

  /**
   * getColorForString returns the Color that is described by the given String
   * in the form '(r,g,b)'.
   * 
   * @param str
   *          The Color in the form '(r,g,b)'.
   * @return The Color described by the given String.
   * @throws IllegalArgumentException
   *           if the given String is not a valid Color.
   */
  public static Color getColorForString(String str)
      throws IllegalArgumentException {
    if (str == null)
      throw new IllegalArgumentException(
          "getColorForString: String cannot be null!");

    String theString = str.trim();
    if (theString.length() == 0)
      throw new IllegalArgumentException(
          "getColorForString: String cannot be empty!");

    /*
     * first we test if the String is a RGB String the patterns goes \x28 -> "("
     * \d{1,3} ->1,2 or 3 digits, ,\s -> ", " \x29 -> ")"
     */
    if (theString.matches("\\x28((\\d{1,3})(,\\s)){2}(\\d{1,3})\\x29")) {

      // we have a RGB String, and if we find anything inappropriate, we
      // just return and do nothing
      // stores value for red
      int r = 1000;

      // stores value for green
      int g = 1000;

      // stores value for blue
      int b = 1000;

      // stores the positions of the first and the second comma
      int fc = theString.indexOf(", ");
      int sc = theString.indexOf(", ", fc + 2);

      // check for possible errors
      if ((fc < 1) || (sc < 1))
        throw new IllegalArgumentException("getColorForString: "
            + "invalid given String!");

      // now read the values
      try {
        r = Integer.parseInt(theString.substring(1, fc));
        g = Integer.parseInt(theString.substring(fc + 2, sc));
        b = Integer.parseInt(theString.substring(sc + 2, theString.length() - 1));
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException("getColorForString: "
            + "invalid given String!");
      }

      // check if we have valid 8bit values
      if ((r > 255) || (g > 255) || (b > 255)) {
        throw new IllegalArgumentException("getColorForString: "
            + "Color-Value greater than 255!");
      }

      return new Color(r, g, b);
    }

    throw new IllegalArgumentException("getColorForString: "
        + "String has to be in the form '(r,g,b)'!");
  }

  /**
   * init initalizes the ColorChooserComboBox ComboBox. It fills the items,
   * takes care about the drawing, sets the listeners and sets some values.
   */
  private void init() {
    int i = 0;

    this.bChangeByComponent = true;
    this.removeAllItems();

    // fill values
    for (; i < colorValues.length; i++) {
      this.values[i][0] = new ColoredSquare(colorValues[i]);
      this.values[i][1] = strLocalStrings[i];
      this.addItem(this.values[i]);
    }

    this.values[i][0] = new ColoredSquare();
    this.values[i][1] = strLocalStrings[colorValues.length];
    this.addItem(this.values[i]);

    // we do the drawing...
    this.setRenderer(this.renderer);

    // clear and add Action Listener
    this.removeActionListener(this);
    this.addActionListener(this);

    // set default values
    this.colorSelected = colorValues[0];
    this.bChangeByComponent = false;
  }

  /*
   * **************************************************** BELOW ARE TWO HELPER
   * CLASSES ****************************************************
   */

  /**
   * ColoredSquare is a Helper Class that makes creating the different Icons for
   * the ColorChooserComboBox easier.
   * 
   * @author T. Ackermann
   */
  private static class ColoredSquare implements Icon {

    /** stores the color of the square. */
    private Color color;

    /** stores if this square is a special square. */
    private boolean special = false;

    /**
     * Constructor creates a new ColoredSquare-Object.
     * 
     * @param c
     *          The Icon-Color.
     */
    public ColoredSquare(Color c) {
      if (c == null) {
        return;
      }

      this.color = c;
      this.special = false;
    }

    /**
     * Constructor creates a new ColoredSquare-Object but because no Color was
     * passed a special Icon for the "More Colors..." Button is painted.
     */
    public ColoredSquare() {
      this.color = Color.WHITE;
      this.special = true;
    }

    /**
     * Returns the current Icon-Color.
     * 
     * @return The current Icon-Color.
     */
    public Color getColor() {
      return this.color;
    }

    /**
     * @see javax.swing.Icon#getIconHeight()
     */
    public int getIconHeight() {
      return 12;
    }

    /**
     * @see javax.swing.Icon#getIconWidth()
     */
    public int getIconWidth() {
      return 14;
    }

    /**
     * changeColor changes the Icon-Color
     * 
     * @param c
     *          The new Icon-Color.
     */
    public void changeColor(Color c) {
      if (c == null) {
        return;
      }

      this.color = c;
    }

    /**
     * @see javax.swing.Icon#paintIcon(java.awt.Component, java.awt.Graphics,
     *      int, int) paints the Icon. The special Icon has three dots on it and
     *      all the other Icons are drawn in the specified Color.
     */
    public void paintIcon(Component c, Graphics g, int x, int y) {
      if ((c == null) || (g == null)) {
        return;
      }

      // save current Color
      Color oldColor = g.getColor();

      // draw the Icon
      if (!this.special) {
        g.setColor(this.color);
        g.fill3DRect(x + 2, y, getIconWidth() - 2, getIconHeight(), true);
      } else {

        // draw the "More Colors..." Icon
        g.setColor(Color.WHITE);
        g.fill3DRect(x + 2, y, getIconWidth() - 2, getIconHeight(), true);
        g.setColor(Color.GRAY);
        g.drawRect(4, 9, 1, 1);
        g.drawRect(7, 9, 1, 1);
        g.drawRect(10, 9, 1, 1);
      }

      // restore Color
      g.setColor(oldColor);
    }
  }

  /**
   * ComboBoxRenderer draws the entries in the ColorChooserComboBox. Therefore
   * it uses the values passed to this function. The first one is the
   * ColoredSquare-Object and the next one is the text that should be displayed.
   * 
   * @author T. Ackermann
   */
  private static class ComboBoxRenderer extends JLabel implements
      ListCellRenderer {

    /**
     * a generated serial Version UID because ComboBoxRenderer is serializable.
     */
    private static final long serialVersionUID = -3699370669894412136L;

    /**
     * Constructor creates a new ComboBoxRenderer-Object.
     */
    public ComboBoxRenderer() {
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
        this.setText("?");
        this.setIcon(new ColoredSquare(Color.BLACK));
        return this;
      }

      if (isSelected || cellHasFocus) {
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

      if (!(itemValues[1] instanceof String)) {
        return this;
      }

      this.setText((String) itemValues[1]);

      if ((index >= 0)
          || (!this.getText().equals(strLocalStrings[colorValues.length]))) {
        // it is a "normal" color or the "more colors" item
        this.setIcon((Icon) itemValues[0]);
      } else {
        // here we set the "Custom Color" icon and text
        if (!(itemValues[0] instanceof ColoredSquare)) {
          return this;
        }

        ColoredSquare csq = (ColoredSquare) itemValues[0];

        this.setIcon(new ColoredSquare(csq.getColor()));
        this.setText(strLocalStrings[colorValues.length + 1] + " "
            + getStringForColorRGB(csq.getColor()));
      }

      return this;
    }
  }
}
