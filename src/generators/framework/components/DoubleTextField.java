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
 * This TextField restricts the user to enter only "-", "." and numbers,
 * and has getters and setters for getting/setting the Double-value.
 *
 * @author T. Ackermann
 */
public class DoubleTextField
    extends JTextField {

    /**
     * a generated serial Version UID because DoubleTextField is serializable.
     */
	private static final long serialVersionUID = 3544949965635270964L;	

	/** stores the default value */
    private Double defaultValue = new Double(0);

    /** stores the Document (which validates the input) */
    private DoubleDocument dblDoc = new DoubleDocument();

    /**
     * Constructor creates a new DoubleTextField-Object.
     */
    public DoubleTextField() {
        super();
        init();
        this.setText("0");
    }


    /**
     * Constructor creates a new DoubleTextField-Object.
     * @param text The text to display.
     */
    public DoubleTextField(String text) {
        super();
        init();
        this.setText(text);
    }
	
		
	/**
     * Constructor creates a new DoubleTextField-Object.
     * @param value The double to display.
     */
    public DoubleTextField(double value) {
        super();
        init();
        this.setText(Double.toString(value));
    }
	
	
	/**
     * Constructor creates a new DoubleTextField-Object.
     * @param objDbl The Double to display.
     */
    public DoubleTextField(Double objDbl) {
        super();
        init();
        this.setText(objDbl.toString());
    }
	

    /**
     * setDefaultValue sets the default value that is used, when the input is
     * not a valid Double.
     *
     * @param newDefaultValue The new default value that is used, when the
     *        input is not a valid Double.
     */
    public void setDefaultValue(Double newDefaultValue) {
        this.defaultValue = newDefaultValue;
    }


    /**
     * getDefaultValue returns the default value that is used, when the input
     * is not a valid Double.
     *
     * @return The default value that is used, when the input is not a valid
     *         Double.
     */
    public Double getDefaultValue() {
        return this.defaultValue;
    }


    /**
     * (non-Javadoc)
     *
     * @see javax.swing.text.JTextComponent#setText(java.lang.String)
     */
    public void setText(String text) {

        // parse the input-String
        Double newValue = this.defaultValue;

        if (text.length() != 0) {
            try {
                newValue = new Double(Double.parseDouble(text));
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
     * Returns the value that is currently displayed in the DoubleTextField,
     * or the default value if there is non-valid input.
     *
     * @return The value that is currently displayed in the DoubleTextField,
     *         or the default value if there is non-valid input.
     */
    public Double getValue() {
        String content = super.getText();
        Double retval;

        if (content.length() != 0) {
            try {
                retval = new Double(Double.parseDouble(content));
                return retval;
            } catch (NumberFormatException nfe) {
                return this.defaultValue;
            }
        }
        return this.defaultValue;
    }


    /**
     * Initializes the DoubleTextField by setting the Document, which only
     * allows Double Input.
     */
    private void init() {
        this.setDocument(this.dblDoc);

        this.addFocusListener(
            new FocusAdapter() {
                public void focusLost(FocusEvent e) {
                    DoubleTextField textField =
                        (DoubleTextField) e.getSource();
                    String content = textField.getText();

                    if (content.length() != 0) {
                        try {
                            Double.parseDouble(content);
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
    static class DoubleDocument
        extends PlainDocument {

        /**
         * a generated serial Version UID because DoubleDocument is
         * serializable.
         */
		private static final long serialVersionUID = 3978984392574384185L;
		

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
                Double.parseDouble(newValue);
                super.insertString(offset, string, attributes);
            } catch (NumberFormatException exception) {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }
}
