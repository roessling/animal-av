/**
 * Editor for Swap
 * @see animal.main.animator.Swap
 *
 * @author Michael Schmitt
 * @version 1.4.7
 * @date 2006-03-27
 */

package animal.editor.animators;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import translator.AnimalTranslator;
import translator.TranslatableGUIElement;
import animal.animator.GraphicObjectSpecificAnimation;
import animal.animator.Highlight;
import animal.animator.Swap;
import animal.editor.Editor;
import animal.graphics.PTGraphicObject;
import animal.graphics.PTIntArray;
import animal.graphics.PTStringArray;
import animal.graphics.meta.PTArray;
import animal.main.Animal;
import animal.misc.EditableObject;
import animal.misc.ObjectSelectionButton;

/**
 * The class for a SwapEditor.
 */
public class SwapEditor extends TimedAnimatorEditor implements
    GraphicObjectSpecificAnimation {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = -3355178488198635871L;

  /**
   * The combo box for the selection of the first cell.
   */
  private JComboBox<String> firstCB;

  /**
   * The combo box for the selection of the second cell.
   */
  private JComboBox<String> secondCB;

  /**
   * Selection button for using a PTPolyline with the Swap. Swap paths may be of
   * type PTPolyline and PTArc at the moment
   */
  private JRadioButton poly;

  /**
   * Selection button for using a PTArc with the Swap. Swap paths may be of type
   * PTPolyline and PTArc at the moment
   */
  private JRadioButton arc;

  /**
   * The index of the first cell that is currently to be swapped.
   */
  private int a = -1;

  /**
   * The index of the second cell that is currently to be swapped.
   */
  private int b = -1;

  /**
   * store the length of the chosen array
   */
  private int commonLength = -1;

  /**
   * static value for using a PTPolyline as movePath for a Swap animator.
   */
  private static final String TYPE_POLY = "SwapEditor.swapTypePolyline";

  /**
   * static value for using a PTArc as movePath for a Swap animator.
   */
  private static final String TYPE_ARC = "SwapEditor.swapTypeArc";

  /**
   * Create a new SwapEditor
   */
  public SwapEditor() {
    super();
  }

  protected void buildGUI() {
    super.buildGUI();
    TranslatableGUIElement generator = AnimalTranslator.getGUIBuilder();
    GridBagConstraints gbc = new GridBagConstraints();
    JPanel sw = new JPanel(new GridBagLayout());

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.EAST;
    sw.add(generator.generateJLabel(
        "SwapEditor.swapCells"), gbc);

    gbc.gridx = 1;
    gbc.anchor = GridBagConstraints.CENTER;
    firstCB = new JComboBox<String>();
//    firstCB.setMaximumRowCount(10);
    firstCB.addItemListener(this);
    sw.add(firstCB, gbc);

    gbc.gridx = 2;
    secondCB = new JComboBox<String>();
//    secondCB.setMaximumRowCount(10);
    secondCB.addItemListener(this);
    sw.add(secondCB, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.ipadx = 10;
    gbc.weightx = 0;
    gbc.anchor = GridBagConstraints.EAST;
    gbc.gridheight = 2;
    sw.add(generator.generateJLabel(
        "SwapEditor.swapType"), gbc);

    gbc.gridheight = 1;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.gridx = 1;
    gbc.weightx = 0;
    Animal privateAnimal = new Animal();
    poly = new JRadioButton(AnimalTranslator.translateMessage(TYPE_POLY),
        privateAnimal.getImageIcon("swapPoly.gif"), true);
    poly.setPressedIcon(privateAnimal.getImageIcon("swapPolySelected.gif"));
    poly.setSelectedIcon(privateAnimal.getImageIcon("swapPolySelected.gif"));
    poly.addActionListener(this);
    sw.add(poly, gbc);

    gbc.gridy = 2;
     arc = new JRadioButton(AnimalTranslator.translateMessage(TYPE_ARC),
        privateAnimal.getImageIcon("swapArc.gif"), false);
    arc.setPressedIcon(privateAnimal.getImageIcon("swapArcSelected.gif"));
    arc.setSelectedIcon(privateAnimal.getImageIcon("swapArcSelected.gif"));
     arc.addActionListener(this);
    sw.add(arc, gbc);

    ButtonGroup typeGroup = new ButtonGroup();
    typeGroup.add(poly);
    typeGroup.add(arc);

    addLayer(sw);
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
      // item listeners have to be removed temporarily to avoid reacting
      // on item events while the entries of the combo boxes are changed
      firstCB.removeItemListener(this);
      secondCB.removeItemListener(this);

      firstCB.removeAllItems();
      secondCB.removeAllItems();
      if ((objectNums != null) && (objectNums.length >= 1)) {
        commonLength = -1;
        int now = 0;
        for (int j = 0; j < objectNums.length; j++) {
          if (objectSB.hasMultiSelection()) {
            int[] elements = ((ObjectSelectionButton) e.getSource())
                .getObjectNums();
            if (elements != null && elements.length > j)
              now = elements[j];
          } else {
            now = ((ObjectSelectionButton) e.getSource()).getObjectNum();
          }

          int length = -1;
          if (getGraphicObject(now) instanceof PTArray) {
            length = ((PTArray) getGraphicObject(now)).getSize();
          }

          if (commonLength == -1) {
            commonLength = length;
          } else if (length < commonLength) {
            commonLength = length;
          }
        }
        if (commonLength >= 2) {
          checkStoredIndices();
          for (int i = 0; i < commonLength; i++) {
            // if (i != b) firstCB.addItem (String.valueOf (i));
            if ((i != b) && (checkActivation(i)))
              firstCB.addItem(String.valueOf(i));
            // if (i != a) secondCB.addItem (String.valueOf (i));
            if ((i != a) && (checkActivation(i)))
              secondCB.addItem(String.valueOf(i));
          }
          firstCB.setSelectedItem(String.valueOf(a));
          secondCB.setSelectedItem(String.valueOf(b));
        }
      }
      firstCB.addItemListener(this);
      secondCB.addItemListener(this);
    } else if ((e.getSource() == firstCB) || (e.getSource() == secondCB)) {
      a = calcIndex(firstCB);
      b = calcIndex(secondCB);

      JComboBox<String> current = firstCB, other = secondCB;
      if (e.getSource() == secondCB) {
        current = secondCB;
        other = firstCB;
      }
      other.removeItemListener(this);

      // remove the corresponding item from the opposite combo box
      // so that selecting equal indices is impossible
      Object selectedItem = other.getSelectedItem();
      other.removeAllItems();
      for (int i = 0; i < commonLength; i++) {
        if ((!(String.valueOf(i).equals(current.getSelectedItem())))
            && checkActivation(i)) {
          other.addItem(String.valueOf(i));
        }
      }
      if (selectedItem != null
          && !(selectedItem.equals(current.getSelectedItem()))) {
        other.setSelectedItem(selectedItem);
      }
      other.addItemListener(this);
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
    Swap swap = (Swap) eo;
    PTGraphicObject ao = null;

    if ((objectNums != null) && (objectNums.length > 0)) {
      for (int i = 0; i < objectNums.length; i++) {
        if (objectSB.hasMultiSelection()) {
          if (objectSB.getObjectNums() != null)
            ao = getGraphicObject(objectSB.getObjectNums()[i]);
        } else {
          if (objectSB.getObjectNum() != 0)
            ao = getGraphicObject(objectSB.getObjectNum());
        }

        int length = -1;
        if (ao instanceof PTArray) {
          length = ((PTArray) ao).getSize();
        }

        if (commonLength == -1) {
          commonLength = length;
        } else if (length < commonLength) {
          commonLength = length;
        }
      }
    }

    firstCB.removeItemListener(this);
    secondCB.removeItemListener(this);

    // remove combo box entries to avoid duplicates!!!
    firstCB.removeAllItems();
    secondCB.removeAllItems();

    // swapping less than two elements doesn't make much sense...
    if (commonLength >= 2) {
      a = swap.getSwapElements()[0] < 0 ? 0 : swap.getSwapElements()[0];
      // !!! the different comparisons are intended!!!
      b = swap.getSwapElements()[1] <= 0 ? 1 : swap.getSwapElements()[1];
      for (int i = 0; i < commonLength; i++) {
        // if (i != b) firstCB.addItem (String.valueOf (i));
        if ((i != b) && (checkActivation(i)))
          firstCB.addItem(String.valueOf(i));
        // if (i != a) secondCB.addItem (String.valueOf (i));
        if ((i != a) && (checkActivation(i)))
          secondCB.addItem(String.valueOf(i));
      }
      firstCB.setSelectedItem(String.valueOf(a));
      secondCB.setSelectedItem(String.valueOf(b));
    }
    firstCB.addItemListener(this);
    secondCB.addItemListener(this);

    switch (swap.getAnimationType()) {
    case Swap.POLY:
      poly.setSelected(true);
      break;
    case Swap.ARC:
      arc.setSelected(true);
      break;
    }
  }

  /**
   * Check if the given cell entry of all currently chosen arrays is activated.
   * 
   * @param index
   *          the cell index to be tested.
   * @return true if the cell is active in all chosen arrays false if it is
   *         deactivated in any array
   */
  private boolean checkActivation(int index) {
    if (!Highlight.realDeactivation()) {
      return true;
    } else if (objectSB.hasMultiSelection()) {
      int size = (objectSB.getObjectNums() == null ? -1 : objectSB
          .getObjectNums().length);
      if (size <= 0)
        return false;
      for (int x = 0; x < size; x++) {
        if ((getGraphicObject(objectSB.getObjectNums()[x]) instanceof PTIntArray)
            && (!(((PTArray) getGraphicObject(objectSB.getObjectNums()[x]))
                .isActivated(index)))) {
          return false;
        }
      }
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
    Swap swap = (Swap) eo;
    if (poly.isSelected()) {
      swap.setAnimationType(Swap.POLY);
    } else if (arc.isSelected()) {
      swap.setAnimationType(Swap.ARC);
    }
    if (objectSB.hasMultiSelection()) {
      if (objectSB.getObjectNums() != null)
        swap.setObjects(objectSB.getObjectNums());
    } else {
      if (objectSB.getObjectNum() != 0)
        swap.setObject(objectSB.getObjectNum());
    }
    swap.setSwapElements(calcIndex(firstCB), calcIndex(secondCB));
  }

  /**
   * Open a secondary SwapEditor window
   * 
   * @param eo
   *          the object from which to take the current settings
   * @return the secondary editor window
   */
  public Editor getSecondaryEditor(EditableObject eo) {
    SwapEditor result = new SwapEditor();
    result.extractAttributesFrom(eo);
    return result;
  }

  /**
   * Create a new swap animation with the current settings
   * 
   * @return a new Swap animator
   */
  public EditableObject createObject() {
    Swap s = new Swap();
    storeAttributesInto(s);
    return s;
  }

  /**
   * Determines the currently chosen array cell of a JCombobox returns -1 if the
   * selection is invalid
   * 
   * @param cBox
   *          the ComboBox for which the chosen index shall be calculated
   * @return the number of the chosen array cell -1 if the array hasn't been
   *         created yet
   */
  private int calcIndex(JComboBox<String> cBox) {
    cBox.removeItemListener(this);
    int index = getInt((String) cBox.getSelectedItem(), -1);
    cBox.addItemListener(this);
    return index;
  }

  /**
   * Assure thate the chosen cell indices are different and in the range of the
   * common size of the chosen arrays.
   * 
   * @return true, but write to console if an error occurred.
   */
  private boolean checkStoredIndices() {
    // a, b are global variables!
    if ((b >= commonLength) || (b < 0)) {
      b = (a == commonLength - 1) ? a - 1 : commonLength - 1;
    }
    if ((a >= commonLength) || (a < 0)) {
      a = (b == commonLength - 1) ? 0 : commonLength - 1;
    }
    return true;
  }

  /**
   * Apply the current editor settings to the selected object(s).
   * 
   * @return the success state of the apply.
   */
  protected boolean apply() {
    Object first = firstCB.getSelectedItem();
    Object second = secondCB.getSelectedItem();

    if (first == null || second == null || first.equals(second)) {
      new JDialog(this, AnimalTranslator
          .translateMessage("SwapEditor.invalidCellSelection"), true);
      return false;
    } else if (objectSB.getObjectNums() == null) {
      new JDialog(this, AnimalTranslator
          .translateMessage("AnimatorEditor.noObjectSelectedException"), true);
      return false;
    } else if (!checkActivation(calcIndex(firstCB))) {
      new JDialog(this, AnimalTranslator.translateMessage(
          "SwapEditor.inactiveElementException", new Object[] { Integer
              .valueOf(calcIndex(firstCB)) }), true);
      return false;
    } else if (!checkActivation(calcIndex(secondCB))) {
      new JDialog(this, AnimalTranslator.translateMessage(
          "SwapEditor.inactiveElementException", new Object[] { Integer
              .valueOf(calcIndex(secondCB)) }), true);
      return false;
    }
    return super.apply();
  }

  public String[] getSupportedTypes() {
    return new String[] { PTIntArray.INT_ARRAY_TYPE, PTStringArray.STRING_ARRAY_TYPE };
  }
}