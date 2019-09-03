/**
 * Editor for Put
 * @see animal.main.animator.Put
 *
 * Created on 1. December 2005, 12:34
 *
 * @author Michael Schmitt
 * @version 0.2.4
 * @date 2006-03-27
 */

package animal.editor.animators;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.VetoableChangeListener;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

import translator.AnimalTranslator;
import animal.animator.GraphicObjectSpecificAnimation;
import animal.animator.Highlight;
import animal.animator.Put;
import animal.editor.Editor;
import animal.graphics.PTGraphicObject;
import animal.graphics.PTIntArray;
import animal.graphics.PTStringArray;
import animal.graphics.meta.PTArray;
import animal.main.Animation;
import animal.misc.EditableObject;
import animal.misc.ObjectSelectionButton;

public class PutEditor extends TimedAnimatorEditor 
implements GraphicObjectSpecificAnimation, KeyListener, VetoableChangeListener {

  /**
   * 
   */
  private static final long serialVersionUID = 8408331164396676270L;

  /**
   * The combo box for the cell selection
   */
  private JComboBox<String> indexCB;

  /**
   * The JTextField that takes the new content of the array cell.
   */
  private JTextField content;

  // =================================================================
  // CONSTRUCTORS
  // =================================================================
  /**
   * Create a new SwapEditor
   */
  public PutEditor() {
    super();
  }

  protected void buildGUI() {
    super.buildGUI();
    JPanel jp = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.WEST;
    jp.add(AnimalTranslator.getGUIBuilder().generateJLabel("PutEditor.cellIndex"), gbc);
    gbc.gridx = 1;
    jp.add(AnimalTranslator.getGUIBuilder().generateJLabel("PutEditor.cellContent"), gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.ipadx = 10;
    gbc.weightx = 0.1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    indexCB = new JComboBox<String>();
    indexCB.setMaximumRowCount(10);
    jp.add(indexCB, gbc);

    gbc.gridx = 1;
    // gbc.ipadx = 0;
    gbc.weightx = 0.9;
    gbc.fill = GridBagConstraints.BOTH;
    content = new JTextField(15);
    content.addVetoableChangeListener(this);
    content.addKeyListener(this);
    jp.add(content, gbc);

    addLayer(jp);
    finish();
  }

  /**
   * Listen for item events
   * 
   * @param e
   *          an ItemEvent to react on
   */
  public void itemStateChanged(ItemEvent e) {
    super.itemStateChanged(e);

    if (e.getSource() instanceof ObjectSelectionButton) {
      int[] nums = objectSB.getObjectNums();
      if ((nums == null) || (nums.length == 0)) {
        indexCB.removeAllItems();
      } else if (nums.length == 1) {
        int length = -1;
        if (getGraphicObject(objectSB.getObjectNums()[0]) instanceof PTStringArray) {
          length = ((PTStringArray) getGraphicObject(objectSB.getObjectNums()[0])).getSize();
        } else if (getGraphicObject(objectSB.getObjectNums()[0]) instanceof PTIntArray) {
          length = ((PTIntArray) getGraphicObject(objectSB.getObjectNums()[0])).getSize();
          content.setText(String.valueOf(getInt(content.getText(), 0)));
        }
        int selected = calcIndex(indexCB);
        indexCB.removeAllItems();
        for (int i = 0; i < length; i++) {
          if (checkActivation(i))
            indexCB.addItem(String.valueOf(i));
        }
        indexCB.setSelectedItem((0 <= selected && selected < length) ? String
            .valueOf(selected) : "0");
      } else {
        // ignore additionally selected objects
        Object selected = indexCB.getSelectedItem();
        objectSB.setObjectNums(new int[] { objectSB.getObjectNums()[0] });

        indexCB.setSelectedItem(selected);
      }
    }
  }

  /**
   * Extract the attributes from the chosen object and adjust the corresponding
   * editor values
   * 
   * @param eo
   *          the object to be modified
   */
  public void extractAttributesFrom(EditableObject eo) {
    super.extractAttributesFrom(eo);
    Put put = (Put) eo;
    PTGraphicObject ao = null;
    if ((objectSB.getObjectNums() != null)
        && (objectSB.getObjectNums().length != 0)) {
      ao = getGraphicObject(objectSB.getObjectNums()[0]);
    }

    int length = -1;
    if (ao instanceof PTArray) {
      length = ((PTArray) ao).getSize();
    }

    if (length > 0) {
      for (int i = 0; i < length; i++) {
        if (checkActivation(i))
          indexCB.addItem(String.valueOf(i));
      }
      indexCB.setSelectedItem(put.getCell() < 0 ? "0" : String.valueOf(put
          .getCell()));
    }
    content.setText(put.getContent());
  }

  /**
   * Check if the given cell entry of all currently chosen arrays is activated.
   * 
   * @param index
   *          the cell index to be tested.
   * @return true if the cell is active in all chosen arrays false if it is
   *         de-activated in any array
   */
  private boolean checkActivation(int index) {
    if (!Highlight.realDeactivation()) {
      return true;
    } else if (objectSB.hasMultiSelection()) {
      int size = (objectSB.getObjectNums() == null ? -1 : objectSB
          .getObjectNums().length);
      if (size <= 0)
        return false;
      // for (int x = 0; x < size; x++) {
      if ((getGraphicObject(objectSB.getObjectNums()[0]) instanceof PTIntArray)
          && (!(((PTArray) getGraphicObject(objectSB.getObjectNums()[0]))
              .isActivated(index)))) {
        return false;
      }
      // }
    } else if ((objectSB.getObjectNum() == 0)
        || (!(((PTArray) getGraphicObject(objectSB.getObjectNum()))
            .isActivated(index)))) {
      return false;
    }
    return true;
  }

  /**
   * Set the attributes of the chosen object to the values of the editor
   * 
   * @param eo
   *          the object to be modified
   */
  public void storeAttributesInto(EditableObject eo) {
    super.storeAttributesInto(eo);
    Put put = (Put) eo;
    if ((objectSB.getObjectNums() != null)
        && (objectSB.getObjectNums().length != 0)) {
      PTGraphicObject ao = Animation.get().getGraphicObject(
          objectSB.getObjectNums()[0]);
      if (ao instanceof PTArray) {
        put.setFinalFont(((PTArray) ao).getFont());
      }
      put.setCell(calcIndex(indexCB));
      put.setContent(content.getText());
      put.setArray(ao.getNum(false));
    }
  }

  /**
   * Open a secondary SwapEditor window
   * 
   * @param eo
   *          the object from which to take the current settings
   */
  public Editor getSecondaryEditor(EditableObject eo) {
    PutEditor result = new PutEditor();
    result.extractAttributesFrom(eo);
    return result;
  }

  /**
   * Create a new swap animation with the current settings
   */
  public EditableObject createObject() {
    Put p = new Put();
    storeAttributesInto(p);
    return p;
  }

  /**
   * Determines the currently chosen array cell of a JSpinner returns -1 if the
   * array is not yet created. / private int calcIndex (JSpinner spinner) {
   * return (spinner == null) ? -1 : ((SpinnerNumberModel) spinner.getModel
   * ()).getNumber ().intValue (); }
   */

  /**
   * Determines the currently chosen array cell of a JCombobox returns -1 if the
   * selection is invalid
   */
  private int calcIndex(JComboBox<String> cBox) {
    int index = getInt((String) cBox.getSelectedItem(), -1);
    return index;
  }

  /**
   * Store the current editor settings into the chosen objects.
   * 
   * @return the state of the apply execution
   */
  protected boolean apply() {
    if ((objectSB.getObjectNums() == null)
        || (objectSB.getObjectNums().length == 0)) {
      new JDialog(this, AnimalTranslator
          .translateMessage("AnimatorEditor.noObjectSelectedException"), true);
      return false;
    }
    if (getGraphicObject(objectSB.getObjectNums()[0]) instanceof PTIntArray) {
      // if an IntArray is selected, ensure that the content field
      // only contains an integer!
      try {
        content.setText(String.valueOf(Integer.parseInt(content.getText())));
       } catch (Exception nfe) {
        new JDialog(this, AnimalTranslator.translateMessage(
            "PutEditor.noNumberException", new Object[] { content.getText() }),
            true);
        return false;
      }
    }
    if (getGraphicObject(objectSB.getObjectNums()[0]) instanceof PTArray) {
      // If attention shall be paid for the activation state, then
      // check if the currently chosen cell is activated
      // in this animation step
      if (Highlight.realDeactivation()
          && (!((PTArray) getGraphicObject(objectSB.getObjectNums()[0]))
              .isActivated(calcIndex(indexCB)))) {
        new JDialog(this, AnimalTranslator.translateMessage(
            "PutEditor.inactiveElementException", new Object[] { Integer
                .valueOf(calcIndex(indexCB)) }), true);
        return false;
      }
    }
    return super.apply();
  }

  /**
   * 
   * @param e
   *          the event to react on
   */
  public void vetoableChange(PropertyChangeEvent e) {
    if ((e.getSource() == content)
        && (getGraphicObject(objectSB.getObjectNums()[0]) instanceof PTIntArray)) {
      try {
        Integer.parseInt((String) e.getNewValue());
      } catch (NumberFormatException nfe) {
        content.removeVetoableChangeListener(this);
        content.setText((String) e.getOldValue());
        content.addVetoableChangeListener(this);
      }
    }
  }

  /**
   * 
   * @param e
   *          the event to react on
   */
  public void keyPressed(
  KeyEvent e) {
    // do nothing
  }

  /**
   * 
   * @param e
   *          the event to react on
   */
  public void keyReleased(
  KeyEvent e) {
    // do nothing
  }

  /**
   * KeyEvent listener to react on keyTyped events
   * 
   * @param e
   *          the event to react on
   */
  public void keyTyped(KeyEvent e) {
    // For IntArrays only digits are allowed to be entered into
    // the content field of the editor window, all other characters
    // will be ignored.
    // ---
    // first check if operating on a PTIntArray
    if ((objectSB.getObjectNums() != null)
        && (objectSB.getObjectNums().length != 0)
        && (getGraphicObject(objectSB.getObjectNums()[0]) instanceof PTIntArray)) {
      // now check for correct number format
      // see IntArrayEditor.keyTyped (KeyEvent e) for details!!!
      if (!(((e.getKeyChar() == '-') && (content.getCaretPosition() == 0) && (getInt(
          content.getText(), 0) >= 0)) || (java.lang.Character.isDigit(e
          .getKeyChar()) && ((content.getCaretPosition() > 0) || (getInt(
          content.getText(), 0) >= 0))))) {
        e.consume();
      }
     }
  }

  /**
   * Get the graphic object types on which the animator works.
   * 
   * @return an array of the supported types
   */
  public String[] getSupportedTypes() {
    return new String[] { PTStringArray.STRING_ARRAY_TYPE, PTIntArray.INT_ARRAY_TYPE };
  }

}