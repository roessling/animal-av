package animal.editor.animators;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.StringTokenizer;

import javax.swing.JPanel;

import translator.AnimalTranslator;
import animal.animator.Move;
import animal.animator.MoveBase;
import animal.animator.TimedAnimator;
import animal.editor.Editor;
import animal.editor.NodeSelector;
import animal.graphics.PTBoxPointer;
import animal.graphics.PTGraphicObject;
import animal.graphics.PTPolygon;
import animal.graphics.PTPolyline;
import animal.gui.AnimalMainWindow;
import animal.main.Animation;
import animal.misc.EditableObject;
import animal.misc.ObjectSelectionButton;
import animal.misc.XProperties;

/**
 * The Editor for Move
 * 
 * @see animal.animator.Move
 * 
 * @author <a href="http://www.algoanim.info/Animal2/">Guido R&ouml;&szlig;ling</a>
 * @version 2.5 2008-06-23
 */
public class MoveEditor extends TimedAnimatorEditor implements ItemListener,
    ActionListener {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = -7803131333584934158L;

  private ObjectSelectionButton pathOSB;

  private boolean[] selectedNodes;

  private NodeSelector nodeSelector;

  public MoveEditor() {
    super();
  }

  protected void buildGUI() {
    super.buildGUI();
    JPanel c = new JPanel();
    c.add(AnimalTranslator.getGUIBuilder().generateJLabel(
            "MoveEditor.moveVia"));
    c.add(pathOSB = new ObjectSelectionButton(false));
    addLayer(c);
    methodChoice.addItemListener(this);

    finish();
  }

  public void setProperties(XProperties props) {
    super.setProperties(props);
  }

  public void extractAttributesFrom(EditableObject move) {
    methodChoice.removeItemListener(this);
    super.extractAttributesFrom(move);
    TimedAnimator ta = (TimedAnimator) move;
    if (ta.getMethod() != null
        && !methodChoice.getSelectedItem().equals(ta.getMethod())) {
      methodChoice.addItem(ta.getMethod());
      methodChoice.setSelectedItem(ta.getMethod());
    }
    methodChoice.addItemListener(this);
    pathOSB.setObjectNum(((Move) move).getMoveBaseNum());
  }

  public void storeAttributesInto(EditableObject move) {
    super.storeAttributesInto(move);
    ((Move) move).setMoveBaseNum(pathOSB.getObjectNum());
  }

  public Editor getSecondaryEditor(EditableObject eo) {
    MoveEditor result = new MoveEditor();
    result.extractAttributesFrom(eo);
    return result;
  }

  public EditableObject createObject() {
    Move m = new Move();
    storeAttributesInto(m);
    return m;
  }

  /**
   * deal with the ObjectSelectionButton by adapting the Combobox that contains
   * all available methods. These methods are set to the intersection of the
   * available methods of all currently selected objects.
   */
  public void itemStateChanged(ItemEvent e) {
    if (e.getSource() == methodChoice
        && e.getStateChange() == ItemEvent.SELECTED && objectNums != null) {
      PTGraphicObject g = Animation.get().getGraphicObject(objectNums[0]);
      String methodName = (String) methodChoice.getSelectedItem();
      if (g.operationRequiresSpecialSelector(methodName)) {
        if (selectedNodes == null) {
          if (g instanceof PTPolyline)
            selectedNodes = new boolean[((PTPolyline) g)
                .getDifferentNodesCount()];
          else if (g instanceof PTPolygon)
            selectedNodes = new boolean[((PTPolygon) g).getNodeCount()];
          else if (g instanceof PTBoxPointer)
            selectedNodes = new boolean[((PTBoxPointer) g).getPointerCount()];
        } else
          for (int j = 0; j < selectedNodes.length; j++)
            selectedNodes[j] = false;
        int firstPos = methodName.indexOf(' ');
        if (firstPos != -1) {
          StringTokenizer stok = new StringTokenizer(methodName
              .substring(firstPos + 1));
          while (stok.hasMoreTokens())
            selectedNodes[Integer.parseInt(stok.nextToken()) - 1] = true;
        }
        if (nodeSelector != null) {
          nodeSelector.close();
        }
        nodeSelector = new NodeSelector(this, selectedNodes, g
            .baseOperationName(methodName), g
            .enableMultiSelectionFor(methodName));
      }
      methodChoice.repaint();
    } else
      super.itemStateChanged(e);
  }

  public void setNewMethod(String methodName) {
    methodChoice.removeItemListener(this);
    methodChoice.addItem(methodName);
    methodChoice.setSelectedItem(methodName);
    methodChoice.addItemListener(this);
  }

  /**
   * reacts to the OK, apply and cancel buttons. <b>IMPORTANT</b> if any
   * subclass implements the ActionListener interface, it must call
   * <code>super.actionPerformed</code> in its own
   * <code>actionPerformed</code> method.
   * 
   * @param e
   *          the ActionEvent, mainly needed to determine if this the "OK",
   *          "Apply" or "Cancel".
   */
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == okButton) {
      if (apply())
        close();
    } else if (e.getSource() == applyButton) {
      apply();
    } else if (e.getSource() == cancelButton)
      close();
  }

  /**
   * checks whether the selected path is a valid MoveBase
   */
  protected String isOK() {
    String result = super.isOK();
    if (result != null)
      return result;
    pathOSB.checkObjects();
    // path must be a MoveBase
    int num = pathOSB.getObjectNum();
    if (num == 0)
      return AnimalTranslator.translateMessage("MoveEditor.noPath");
    PTGraphicObject go = getGraphicObject(num);
    if (!(go instanceof MoveBase))
      return AnimalTranslator.translateMessage("MoveEditor.invalidPathType");
    // mark polyline to be used as MoveBase
    ((MoveBase) go).useAsMoveBase();
    // to make the changes visible and write them back
    AnimalMainWindow.getWindowCoordinator().getDrawWindow(false).setChanged();
    return null;
  }

  /**
   * Applies the changes made by this editor to the animation
   * 
   * @return true if all changes are OK.
   */
  protected boolean apply() {
    String currentMessage = (String)methodChoice.getSelectedItem();
    String error = isOK();
    if (error != null) {
      return false;
    }
    methodChoice.removeItemListener(this);
    // write the attributes back to the object
    EditableObject a = getCurrentObject();
    if (a != null)
      storeAttributesInto(a);
    // check if Object must be inserted into animation(i.e. it is a new
    // Animator), then repaint with list fetch, otherwise just repaint
    if (a instanceof Move && !Animation.get().containsAnimator((Move) a)) {
      Animation.get().insertAnimator((Move) a);
      // if an Animator is inserted, the list must be re-fetched
      AnimalMainWindow.getWindowCoordinator().getAnimationOverview(true)
          .initList(true);
    } else {
      Animation.get().doChange();
      repaintNow();
    }
    // GraphicObjects only change the DrawWindow, they don't change
    // the Animation until the DrawWindow is written back.
    // Links and Animators directly change the Animation.
    if (a instanceof PTGraphicObject)
      AnimalMainWindow.getWindowCoordinator().getDrawWindow(false).setChanged();
    else
      Animation.get().doChange();
    // if the Editor is not a GraphicEditors, save the properties
    // whenever applying changes. With GraphicEditors, only the properties
    // of the primary GraphicEditors are saved. As there are no
    // primary Animator- and LinkEditors, this way to save properties
    // is necessary
    methodChoice.addItem(currentMessage);
    methodChoice.setSelectedItem(currentMessage);
    currentMessage = null;
    methodChoice.addItemListener(this);
    return true;
  }
} // MoveEditor
