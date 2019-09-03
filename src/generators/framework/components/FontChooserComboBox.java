/*
 * Created on 12.11.2004 by T. Ackermann
 */
package generators.framework.components;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;


/**
 * ColorChooserComboBox is a ComboxBox that displays a list of possible styles
 * for Animal Fonts. These are Serif, SansSerif and Monospaced.
 *
 * @author T. Ackermann
 */
public class FontChooserComboBox
    extends JComboBox
    implements ActionListener {

    /**
     * a generated serial Version UID because FontChooserComboBox is
     * serializable.
     */
    private static final long serialVersionUID = 1252859137529793065L;

    /** stores the possible Font Styles. */
    private static final String[] strFontStyles =
        {"Serif", "SansSerif", "Monospaced"};

    /** stores the renderer that allows us to draw a nice FontChooserComboBox. */
    private FontChooserComboBoxRenderer renderer =
        new FontChooserComboBoxRenderer();

    /** stores the currently selected Font as String. */
    private String strSelected = "Serif";

    /** stores the values for the FontChooserComboBox-Items */
    private Object[][] values = new Object[strFontStyles.length][2];

    /** helps avoiding calling actionPerformed too often */
    private boolean bChangeByComponent = false;

    /**
     * Constructor creates a new FontChooserComboBox-Object.
     */
    public FontChooserComboBox() {
        super();
        init();
    }


    /**
     * Constructor creates a new FontChooserComboBox-Object and sets the
     * selected Font.
     *
     * @param strNew The default Font. Can be "Serif", "SansSerif" and
     *        "Monospaced".
     */
    public FontChooserComboBox(String strNew) {
        super();
        init();
        setFontSelected(strNew);
    }
	
	
	 /**
     * Constructor creates a new FontChooserComboBox-Object and sets the
     * selected Font.
     *
     * @param fontNew The default Font.
     */
    public FontChooserComboBox(Font fontNew) {
        super();
        init();
        setFontSelected(fontNew.getFamily());
    }

    /**
     * Sets the Font that should be selected.
     *
     * @param strNew The Font that should be selected. Can be "Serif",
     *        "SansSerif" and "Monospaced".
     */
    public void setFontSelected(String strNew) {
        if (strNew == null) {
            return;
        }
        String newString = strNew.trim().toLowerCase();
//        strNew = strNew.trim().toLowerCase();
        int iNewIndex = -1;

        // look for the Font-String
        for (int i = 0; i < strFontStyles.length; i++) {
            if (newString.equals(strFontStyles[i].toLowerCase())) {
                this.strSelected = strFontStyles[i];
                iNewIndex = i;
            }
        }

        // return if nothing has been found
        if (iNewIndex == -1) {
            return;
        }

        // select the element
        this.bChangeByComponent = true;
        setSelectedIndex(iNewIndex);
        this.bChangeByComponent = false;

        // repaint the ComboBox
        this.repaint();
    }


    /**
     * Returns the selected Font as a String.
     *
     * @return Returns the selected Font as a String.
     */
    public String getFontSelectedAsString() {
        return this.strSelected;
    }
	
	
	/**
	 * Returns the selected Font as a Font-Object.
	 * 
	 * @return The selected Font as a Font-Object.
	 */
	public Font getFontSelected() {
        return new Font(this.strSelected, Font.PLAIN, 12);
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

        if ((index < 0) || (index >= strFontStyles.length)) {
            return;
        }

        // store the selected Font
        this.strSelected = strFontStyles[index];

        // change the Font for the ComboBox
        this.setFont(new Font(this.strSelected, Font.PLAIN, 14));
    }


    /**
     * init initalizes the FontChooserComboBox. It fills the items, takes care
     * about the drawing, sets the listeners and sets some values.
     */
    private void init() {
        for (int i = 0; i < strFontStyles.length; i++) {

            /* we do this because later on, we can make one of these multilingual...
             * the [0] is the Font-Name and should not be changed, but
             * the [1] is what is displayed
             */
            this.values[i][0] = strFontStyles[i];
            this.values[i][1] = strFontStyles[i];
            this.addItem(this.values[i]);
        }

        // we do the drawing...
        this.setRenderer(this.renderer);

        //add Action Listener
        this.addActionListener(this);

        // set default values
        this.strSelected = strFontStyles[0];
        this.setFont(new Font(this.strSelected, Font.PLAIN, 14));
    }

    /*
     * ****************************************************
     *  BELOW IS A HELPER CLASSES
     * ****************************************************
     */

    /**
     * FontChooserComboBoxRenderer draws the entries in the
     * FontChooserComboBox. Therefore it uses the values passed to this
     * function.
     *
     * @author T. Ackermann
     */
    private static class FontChooserComboBoxRenderer
        extends JLabel
        implements ListCellRenderer {

        /**
         * a generated serial Version UID because FontChooserComboBoxRenderer
         * is serializable.
         */
        private static final long serialVersionUID = 3266835247180289721L;

        /**
         * Constructor creates a new FontChooserComboBoxRenderer-Object.
         */
        public FontChooserComboBoxRenderer() {
            setOpaque(true);
        }

        /**
         * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList,
         *      java.lang.Object, int, boolean, boolean) This method finds the
         *      image and text corresponding to the selected value and returns
         *      the label, set up to display the text and image.
         */
        public Component getListCellRendererComponent(
            JList list, Object value, int index, boolean isSelected,
            boolean cellHasFocus) {
            if ((list == null) || (value == null)) {
                this.setText("?");
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

            if (
                !(
                        (itemValues[0] instanceof String)
                        && (itemValues[1] instanceof String)
                    )) {
                return this;
            }

            String strDisplay = (String) itemValues[0];

            this.setFont(new Font(strDisplay, Font.PLAIN, 20));
            this.setText((String) itemValues[1]);

            return this;
        }
    }
}
