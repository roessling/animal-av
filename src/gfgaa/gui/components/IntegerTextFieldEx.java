/*
 * Created on 19.11.2004 by T. Ackermann
 */
package gfgaa.gui.components;
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
 *--
 * Änderung von S. Kulessa:
 * IntegerTextFieldEx akzeptiert nun auch identische min max Werte
 */
public class IntegerTextFieldEx
    extends JTextField {

    /**
     * a generated serial Version UID because IntegerTextField is serializable.
     */
    private static final long serialVersionUID = 6503913782014113889L;

    /** stores the default value */
    private Integer defaultValue = new Integer(0);

    /** stores the Document (which validates the input) */
    private IntegerDocumentEx intDoc;

    /** stores the min value */
    private int minValue = Integer.MIN_VALUE;
    
    /** stores the max value */
    private int maxValue = Integer.MAX_VALUE;
    
    /** stores the number of read digits */
    private char numberOfDigits = 11;
    
    /** stores if the minus-sign can be entered */
    private boolean minusAllowed = true;
    
    /**
     * Constructor creates a new IntegerTextField-Object.
     */
    public IntegerTextFieldEx() {
        super();
        this.minValue = Integer.MIN_VALUE;
        this.maxValue = Integer.MAX_VALUE;
        init();
        this.setText("0");
    }
    
    
    /**
     * Constructor creates a new IntegerTextField-Object.
     * @param newMinValue The min value that can be entered.
     * @param newMaxValue The max value that can be entered.
     */
    public IntegerTextFieldEx(int newMinValue, int newMaxValue) {
        super();
        this.minValue = newMinValue;
        this.maxValue = newMaxValue;
        checkMinAndMaxValues();
        this.defaultValue = new Integer(this.minValue);
        init();       
        this.setText(this.defaultValue.toString());
    }


    /**
     * Constructor creates a new IntegerTextField-Object.
     *
     * @param text The text to display.
     */
    public IntegerTextFieldEx(String text) {
        super();
        this.minValue = Integer.MIN_VALUE;
        this.maxValue = Integer.MAX_VALUE;
        init();
        this.setText(text);
    }

    /**
     * setDefaultValue sets the default value that is used, when the input is
     * not a valid Integer.
     *
     * @param newDefaultValue The new default value that is used, when the
     *        input is not a valid Integer.
     */
    public void setDefaultValue(Integer newDefaultValue) {
        int i = newDefaultValue.intValue();
        if (i< this.minValue || i > this.maxValue) return;
        this.defaultValue = new Integer(i);
    }


    /**
     * getDefaultValue returns the default value that is used, when the input
     * is not a valid Integer.
     *
     * @return The default value that is used, when the input is not a valid
     *         Integer.
     */
    public Integer getDefaultValue() {
        return new Integer(this.defaultValue.intValue());
    }
    
    
    /**
     * sets the min- and max-values that can be entered by the user.
     * @param newMinValue The min value.
     * @param newMaxValue The max value.
     */
    public void setMinMaxValues(int newMinValue, int newMaxValue) {
        Integer oldValue = getValue();
        this.minValue = newMinValue;
        this.maxValue = newMaxValue;
        checkMinAndMaxValues();
        this.defaultValue = new Integer(this.minValue);
        init();  
        this.setText(oldValue.toString());
    }
    
    /**
     * getMaxValue returns the max value that can be entered by the user.
     * @return The current max value.
     */
    public int getMaxValue() {
        return this.maxValue;
    }
        
    /**
     * getMinValue returns the min value that can be entered by the user.
     * @return The current min value.
     */
    public int getMinValue() {
        return this.minValue;
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
            } catch (NumberFormatException nfe) {
                return this.defaultValue;
            }
            if (retval.intValue() < this.minValue)
                return new Integer(this.minValue);
            if (retval.intValue() > this.maxValue)
                return new Integer(this.maxValue);
            return retval;
        }

        return this.defaultValue;
    }


    /**
     * Initializes the IntegerTextField by setting the Document, which only
     * allows Integer Input.
     */
    private void init() {       
        calcNumberDigits();
            
        this.intDoc = new IntegerDocumentEx(this.numberOfDigits, this.minusAllowed);
        this.setDocument(this.intDoc);

        // remove old focus listeners
        /*
        for (int i = this.getFocusListeners().length - 1; i >= 0; i--) {
            this.removeFocusListener(this.getFocusListeners()[i]);
        }
        */
        
        // add new focus listener
        this.addFocusListener(
            new FocusAdapter() {
                public void focusLost(FocusEvent e) {
                    IntegerTextFieldEx textField =
                        (IntegerTextFieldEx) e.getSource();
                    String content = textField.getText();

                    if (content.length() != 0) {
                        try {
                            int iVal = Integer.parseInt(content);
                            if (iVal < textField.getMinValue()) iVal = textField.getMinValue();
                            if (iVal > textField.getMaxValue()) iVal = textField.getMaxValue();
                            textField.setText(Integer.toString(iVal));         
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
    
    /**
     * sets the number of digits that are needed for entering the min and max
     * values.
     */
    private void calcNumberDigits() {
        int i = Math.max(Math.abs(this.maxValue), Math.abs(this.minValue));
        char digits = 1;
        while (i / 10.0 >= 1.0) {
            digits++;
            i /= 10;
        }
        
        this.minusAllowed = (this.minValue < 0);            
        this.numberOfDigits = digits;
    }
    
    /**
     * checkMinAndMaxValues
     * checks if the Min and Max Values are in the right order and changes min
     * with max if min is greater then min.
     */
    private void checkMinAndMaxValues() {
        /* auskommentiert by S. Kulessa
         * 
           if (this.minValue == this.maxValue) {
            this.minValue = Integer.MIN_VALUE;
            this.maxValue = Integer.MAX_VALUE;
            return;
        }*/
        
        if (this.minValue > this.maxValue) {
            int tmp = this.minValue;
            this.minValue = this.maxValue;
            this.maxValue = tmp;
        }
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
    private static class IntegerDocumentEx
        extends PlainDocument {

        /**
         * a generated serial Version UID because IntegerDocumentEx is
         * serializable.
         */
        private static final long serialVersionUID = 6425645003078010892L;

        /** stores the max length of the entered number. */
        private int maxLength = 11;
        
        /** stores a minus-sign can be entered. */
        private boolean minusAllowed = true;
        
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
                StringBuffer currentBuffer = new StringBuffer(currentContent);

                currentBuffer.insert(offset, string);
                newValue = currentBuffer.toString();
            }

            // we allow "-" as the first character (maybe)
            if (newValue.equals("-") && this.minusAllowed) {
                super.insertString(offset, newValue, attributes);
                return;
            }
            
            // Here we test for the max-length
            String textWithOutMinus = newValue.replaceAll("-", "");
            if (textWithOutMinus.length() > this.maxLength) {
                return;
            }

            try {
                Integer.parseInt(newValue);
                super.insertString(offset, string, attributes);
            } catch (NumberFormatException exception) {
                Toolkit.getDefaultToolkit().beep();
            }
        }
        
        
        /**
         * Constructor creates a new IntegerDocumentEx-Object.
         */
        public IntegerDocumentEx() {
            super();    
            this.maxLength = 11;
        }
        
        /**
         * Constructor creates a new IntegerDocumentEx-Object and sets the max 
         * length of the entered Number.
         * @param newMaxLength The max length.
         * @param newMinusAllowed Is the minus-sign allowed?
         */
        public IntegerDocumentEx(int newMaxLength, boolean newMinusAllowed) {
            super();
            this.maxLength = newMaxLength;
            this.minusAllowed = newMinusAllowed;
        }
    }
}
