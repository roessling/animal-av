/*
 * Created on 19.11.2004 by T. Ackermann
 */
package generators.framework.components;
import java.awt.Toolkit;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;


/**
 * This TextField restricts the user to enter only a minus-sign and numbers,
 * and has getters and setters for getting/setting the Integer-value.
 *
 * @author T. Ackermann
 */
public class IntegerTextField
    extends JTextField {

    /**
     * a generated serial Version UID because IntegerTextField is serializable.
     */
    private static final long serialVersionUID = -720882764279734721L;

    /** stores the default value */
    private Integer defaultValue = new Integer(0);

    /** stores the Document (which validates the input) */
    private IntegerDocument intDoc = new IntegerDocument();

    /**
     * Constructor creates a new IntegerTextField-Object.
     */
    public IntegerTextField() {
        super();
        init();
        this.setText("0");
    }


    /**
     * Constructor creates a new IntegerTextField-Object.
     * @param text The text to display.
     */
    public IntegerTextField(String text) {
        super();
        init();
        this.setText(text);
    }
	
		
	/**
     * Constructor creates a new IntegerTextField-Object.
     * @param value The int to display.
     */
    public IntegerTextField(int value) {
        super();
        init();
        this.setText(Integer.toString(value));
    }
	
	
	/**
     * Constructor creates a new IntegerTextField-Object.
     * @param objInt The Integer to display.
     */
    public IntegerTextField(Integer objInt) {
        super();
        init();
        this.setText(objInt.toString());
    }
	

    /**
     * setDefaultValue sets the default value that is used, when the input is
     * not a valid Integer.
     *
     * @param newDefaultValue The new default value that is used, when the
     *        input is not a valid Integer.
     */
    public void setDefaultValue(Integer newDefaultValue) {
        this.defaultValue = newDefaultValue;
    }


    /**
     * getDefaultValue returns the default value that is used, when the input
     * is not a valid Integer.
     *
     * @return The default value that is used, when the input is not a valid
     *         Integer.
     */
    public Integer getDefaultValue() {
        return this.defaultValue;
    }


    /**
     * (non-Javadoc)
     *
     * @see javax.swing.text.JTextComponent#setText(java.lang.String)
     */
    public void setText(String text) {

        // parse the input-String
        Integer newValue = this.defaultValue;

        if (text.length() != 0) {
            try {
                newValue = new Integer(Integer.parseInt(text));
            } catch (NumberFormatException nfe) {

                // do nothing
            }
        }

        super.setText(newValue.toString());
    }


    /**
     * (non-Javadoc)
     *
     * @see javax.swing.text.JTextComponent#getText()
     */
    public String getText() {
        return getValue().toString();
    }


    /**
     * Returns the value that is currently displayed in the IntegerTextField,
     * or the default value if there is non-valid input.
     *
     * @return The value that is currently displayed in the IntegerTextField,
     *         or the default value if there is non-valid input.
     */
    public Integer getValue() {
        String content = super.getText();
        Integer retval;

        if (content.length() != 0) {
            try {
                retval = new Integer(Integer.parseInt(content));
                return retval;
            } catch (NumberFormatException nfe) {
                return this.defaultValue;
            }
        }

        return this.defaultValue;
    }


    /**
     * Initializes the IntegerTextField by setting the Document, which only
     * allows Integer Input.
     */
    private void init() {
        this.setDocument(this.intDoc);

        this.addFocusListener(
            new FocusAdapter() {
                public void focusLost(FocusEvent e) {
                    IntegerTextField textField =
                        (IntegerTextField) e.getSource();
                    String content = textField.getText();

                    if (content.length() != 0) {
                        try {
                            Integer.parseInt(content);
                        } catch (NumberFormatException nfe) {
                            getToolkit().beep();
                            textField.setText(
                                textField.getDefaultValue().toString());
                        }
                    } else {
                        textField.setText(
                            textField.getDefaultValue().toString());
                    }
                }
            });
    }
    
    /*
     * *******************************************************************
     *           BELOW IS A HELPER CLASS
     * *******************************************************************
     */

    /**
     * Validates the TextFields Input.
     *
     * @author T. Ackermann
     */
    static class IntegerDocument
        extends PlainDocument {

        /**
         * a generated serial Version UID because IntegerDocument is
         * serializable.
         */
        private static final long serialVersionUID = 2489150714777102558L;

        /**
         * (non-Javadoc)
         *
         * @see javax.swing.text.Document#insertString(int, java.lang.String,
         *      javax.swing.text.AttributeSet)
         */
        public void insertString(
            int offset, String string, AttributeSet attributes)
            throws BadLocationException {
            if (string == null) {
                return;
            }

            String newValue;
            int length = getLength();

            if (length == 0) {
                newValue = string;
            } else {
                String currentContent = getText(0, length);
                StringBuilder currentBuffer = new StringBuilder(currentContent);

                currentBuffer.insert(offset, string);
                newValue = currentBuffer.toString();
            }

            if (newValue.equals("-")) {
                super.insertString(offset, newValue, attributes);
                return;
            }

            try {
                Integer.parseInt(newValue);
                super.insertString(offset, string, attributes);
            } catch (NumberFormatException exception) {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }
}
