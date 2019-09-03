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

import translator.AnimalTranslator;
import animal.animator.GraphicObjectSpecificAnimation;
import animal.animator.HighlightEdge;
import animal.editor.Editor;
import animal.graphics.PTGraph;
import animal.graphics.PTGraphicObject;
import animal.misc.EditableObject;
import animal.misc.ObjectSelectionButton;

public class HighlightEdgeEditor extends TimedAnimatorEditor implements
    GraphicObjectSpecificAnimation {

  /**
   * 
   */
  private static final long serialVersionUID = -5352350893227202431L;

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
   * Create a new Editor
   */
  public HighlightEdgeEditor() {
    super();
   }

  protected void buildGUI() {
    super.buildGUI();
    hlContent = new JPanel(new GridLayout(5, 5, 5, 0));
    scp = new JScrollPane(hlContent,
        ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scp.setRowHeaderView(AnimalTranslator.getGUIBuilder().generateJLabel(
        "HighlightEdgeEditor.affectedEdges"));
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
          if (getGraphicObject(now) instanceof PTGraph) {
            length = ((PTGraph) getGraphicObject(now)).length;
          }
          if ((commonLength == -1) || (length > commonLength)) {
            commonLength = length;
          }
        }

        if (commonLength == -1) {
          hlContent.removeAll();
        } else {
          hlContent.removeAll();
          for (int x = 0; x < commonLength; x++) {
            for (int y = 0; y < commonLength; y++) {
               hlContent.add(new JCheckBox(String.valueOf(x) + "-"
                  + String.valueOf(y), false), hlC, x * commonLength + y);
            }
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
    HighlightEdge hl = (HighlightEdge) eo;
    PTGraphicObject ao = null;

    if ((objectNums != null) && (objectNums.length > 0)) {
      for (int i = 0; i < objectNums.length; i++) {
        if (objectSB.getObjectNums() != null)
          ao = getGraphicObject(objectSB.getObjectNums()[i]);
        int length = -1;
        if (ao instanceof PTGraph) {
          length = ((PTGraph) ao).length;
        }
        if ((commonLength == -1) || (length > commonLength)) {
          commonLength = length;
        }
      }
      hlContent.removeAll();
      for (int x = 0; x < commonLength; x++) {
        for (int y = 0; y < commonLength; y++) {
           hlContent.add(new JCheckBox(String.valueOf(x) + "-"
              + String.valueOf(y), hl.getHighlightState(x, y)), hlC, x
              * commonLength + y);
        }
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
    HighlightEdge hl = (HighlightEdge) eo;
    if (commonLength > 0) {
      boolean[][] highlightState = new boolean[commonLength][commonLength];
      for (int x = 0; x < commonLength; x++) {
        for (int y = 0; y < commonLength; y++) {
          highlightState[x][y] = ((JCheckBox) hlContent.getComponent(x
              * commonLength + y)).isSelected();
        }
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
    HighlightEdgeEditor result = new HighlightEdgeEditor();
    result.extractAttributesFrom(eo);
    return result;
  }

  /**
   * Create a new swap animation with the current settings
   */
  public EditableObject createObject() {
    HighlightEdge h = new HighlightEdge();
    storeAttributesInto(h);
    return h;
  }

  public String[] getSupportedTypes() {
    return new String[] { PTGraph.TYPE_LABEL };
  }
}