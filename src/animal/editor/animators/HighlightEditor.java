/**
 * Editor for Highlight
 * @see animal.main.animator.Highlight
 *
 * Created on 16. December 2006, 0:05
 *
 * @author MichaelSchmitt
 * @version 0.2.1a
 * @date 2006-02-15
 * 
 * modified on 21. September 2006 by Pierre Villette
 */

package animal.editor.animators;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import animal.animator.GraphicObjectSpecificAnimation;
import animal.animator.Highlight;
import animal.editor.Editor;
import animal.graphics.PTGraph;
import animal.graphics.PTGraphicObject;
import animal.graphics.PTIntArray;
import animal.graphics.PTStringArray;
import animal.graphics.meta.PTArray;
import animal.misc.EditableObject;
import animal.misc.ObjectSelectionButton;

public class HighlightEditor extends TimedAnimatorEditor implements
    GraphicObjectSpecificAnimation {
  /**
   * 
   */
  private static final long serialVersionUID = 8521232039932626594L;

  /**
   * store the length of the chosen array
   */
  private int commonLength = -1;

  /**
   * The panel that contains the checkboxes
   */
  private JPanel hlContent;

  /**
   * Make the highlight panel scrollable if it contains too many elements.
   */
  private JScrollPane scp;

  /**
   * The constraints for the checkboxes
   */
  public GridBagConstraints hlC = new GridBagConstraints(
      GridBagConstraints.RELATIVE, 1, 1, 1, .5, .5, GridBagConstraints.WEST,
      GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 5, 0);

  /**
   * Create a new SwapEditor
   */
  public HighlightEditor() {
    super();
  }

  protected void buildGUI() {
    super.buildGUI();
    hlContent = new JPanel(new GridLayout(5, 5, 5, 0));
    scp = new JScrollPane(hlContent,
        ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scp.setRowHeaderView(translator.AnimalTranslator.getGUIBuilder()
        .generateJLabel("HighlightEditor.affectedCells"));
    scp.setMaximumSize(new Dimension(400, 300));
    addLayer(scp);
    finish();

  }

  public void itemStateChanged(ItemEvent e) {
    super.itemStateChanged(e);
    if (e.getSource() instanceof ObjectSelectionButton) {
      if ((objectNums != null) && (objectNums.length > 0)) {
        commonLength = -1;
        int now = 0;
        for (int i = 0; i < objectNums.length; i++) {
          int[] elements = ((ObjectSelectionButton) e.getSource())
              .getObjectNums();
          if (elements != null && elements.length > i)
            now = elements[i];
          int length = -1;
          if (getGraphicObject(now) instanceof PTArray) {
            length = ((PTArray) getGraphicObject(now)).getSize();
          } else if (getGraphicObject(now) instanceof PTGraph) {
            length = ((PTGraph) getGraphicObject(now)).length;
          }
          if ((commonLength == -1) || (length > commonLength)) {
            commonLength = length;
          }
        }
        if (commonLength == -1) {
          hlContent.removeAll();
        } else if (commonLength < hlContent.getComponentCount()) {
          for (int x = hlContent.getComponentCount() - 1; x >= commonLength; x--) {
            hlContent.remove(x);
          }
        } else if (commonLength > hlContent.getComponentCount()) {
          for (int x = hlContent.getComponentCount(); x < commonLength; x++) {
            // hlContent.add (new JCheckBox (String.valueOf (x),
            // false), x);
            hlContent.add(new JCheckBox(String.valueOf(x), false), hlC, x);
          }
        }
      } else {
        commonLength = -1;
        hlContent.removeAll();
      }
      if (scp.getWidth() > scp.getMaximumSize().getWidth()) {
        scp.getViewport().setViewSize(scp.getMaximumSize());
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
    Highlight hl = (Highlight) eo;
    PTGraphicObject ao = null;

    if ((objectNums != null) && (objectNums.length > 0)) {
      for (int i = 0; i < objectNums.length; i++) {
        if (objectSB.getObjectNums() != null)
          ao = getGraphicObject(objectSB.getObjectNums()[i]);
        int length = -1;
        if (ao instanceof PTArray) {
          length = ((PTArray) ao).getSize();
        } else if (ao instanceof PTGraph) {
          length = ((PTGraph) ao).length;
        }
        if ((commonLength == -1) || (length > commonLength)) {
          commonLength = length;
        }
      }
      hlContent.removeAll();
      for (int x = 0; x < commonLength; x++) {
        // hlContent.add (new JCheckBox (String.valueOf (x),
        // hl.getHighlightState (x)), x);
        hlContent.add(
            new JCheckBox(String.valueOf(x), hl.getHighlightState(x)), hlC, x);
      }
    }
    scp.setViewportView(hlContent);
  }

  /**
   * Set the attributes of the chosen object to the values of the editor
   * 
   * @param eo
   *          the object to be modified
   */
  public void storeAttributesInto(EditableObject eo) {
    super.storeAttributesInto(eo);
    Highlight hl = (Highlight) eo;
    if (commonLength > 0) {
      boolean[] highlightState = new boolean[hlContent.getComponentCount()];
      for (int x = 0; x < hlContent.getComponentCount(); x++) {
        highlightState[x] = ((JCheckBox) hlContent.getComponent(x))
            .isSelected();
      }
      hl.setHighlightState(highlightState);
    }
  }

  /**
   * Open a secondary SwapEditor window
   * 
   * @param eo
   *          the object from which to take the current settings
   */
  public Editor getSecondaryEditor(EditableObject eo) {
    HighlightEditor result = new HighlightEditor();
    result.extractAttributesFrom(eo);
    return result;
  }

  /**
   * Create a new swap animation with the current settings
   */
  public EditableObject createObject() {
    Highlight h = new Highlight();
    storeAttributesInto(h);
    return h;
  }

  public String[] getSupportedTypes() {
    return new String[] { PTGraph.TYPE_LABEL, PTIntArray.INT_ARRAY_TYPE,
        PTStringArray.STRING_ARRAY_TYPE };
  }
}