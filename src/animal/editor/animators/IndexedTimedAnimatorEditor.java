package animal.editor.animators;

import java.awt.event.ItemEvent;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;

import animal.animator.GraphicObjectSpecificAnimation;
import animal.animator.TimedAnimator;
import animal.editor.Editor;
import animal.editor.IndexedContentChooserListSupport;
import animal.graphics.PTGraphicObject;
import animal.main.Animation;
import animal.misc.EditableObject;
import animal.misc.ObjectSelectionButton;

public abstract class IndexedTimedAnimatorEditor extends TimedAnimatorEditor
    implements GraphicObjectSpecificAnimation {

  /**
	 * 
	 */
	private static final long serialVersionUID = -6321391140925997292L;
protected IndexedContentChooserListSupport indexChooser;

  /**
   * 
   */
  public IndexedTimedAnimatorEditor() {
    super();
  }

  protected void buildGUI() {
    super.buildGUI();
    getContentPane().remove(this.basicPanel);
    installBoxLayout();
    addBox(createTimedAnimatorControls());
    addBox(createIndexChooser());
    methodChoice.setVisible(false);
  }

  protected Box createTimedAnimatorControls() {
    Box c = new Box(BoxLayout.LINE_AXIS);
    c.add(basicPanel);
    return c;
  }

  protected Box createIndexChooser() {
    indexChooser = new IndexedContentChooserListSupport();
    indexChooser.setShowOnlyObjectsWithMethods(true);
    return indexChooser.getContentBox();
  }

  /**
   * Open a secondary IndexedTimedAnimatorEditor window
   * 
   * @param eo
   *          the object from which to take the current settings
   */
  public Editor getSecondaryEditor(EditableObject eo) {
    return null;
  }

  /**
   * Set the attributes of the chosen object to the values of the editor
   * 
   * @param eo
   *          the object to be modified
   */
  public void storeAttributesInto(EditableObject eo) {
    createMethodChoiceModel();
    super.storeAttributesInto(eo);
  }

  public void extractAttributesFrom(EditableObject eo) {
    String method = getMethodFromEditableObject(eo);
    super.extractAttributesFrom(eo);
    if (method != null)
      splitAndSetData(method);
  }

  private void splitAndSetData(String method) {
    String[] tokens;
    tokens = method.split("\\s");
    if (tokens.length >= 3 && indexChooser.setSelectedKindOfObject(tokens[0])
        && indexChooser.setSelectedMethod(tokens[1]) && tokens[2].length() > 4) {
      // cut leading "{[" and last "]}"
      String[] indexTuples;
      if (tokens[2].contains("]["))
        indexTuples = tokens[2].substring(2, tokens[2].length() - 2).split(
            "\\]\\[");
      else {
        indexTuples = new String[1];
        indexTuples[0] = tokens[2].substring(2, tokens[2].length() - 2);
      }
      String[] indices;
      Vector<Vector<Integer>> tuples = new Vector<Vector<Integer>>();
      Vector<Integer> tmpVector = new Vector<Integer>();
      ;
      for (int i = 0; i < indexTuples.length; ++i) {
        tmpVector.clear();
        indices = indexTuples[i].split(",");
        if (indices.length >= 1) {
          try {
            for (String index : indices) {
              tmpVector.add(Integer.parseInt(index));
            }
            tuples.add(tmpVector);
          } catch (NumberFormatException exc) {

          }
        }
      }
      indexChooser.addIndicesToList(tuples);
    }
  }

  protected String getMethodFromEditableObject(EditableObject eo) {
    if (eo instanceof TimedAnimator) {
      return ((TimedAnimator) eo).getMethod();
    }
    return null;
  }

  private void createMethodChoiceModel() {
    String methodChoice = "";
    methodChoice += this.indexChooser.getSelectedMethod() + " "
        + this.indexChooser.getChosenIndexTuples();
    this.methodChoice.setModel(new DefaultComboBoxModel<String>(
        new String[] { methodChoice }));
    this.methodChoice.setSelectedIndex(0);
  }

  public void itemStateChanged(ItemEvent e) {
    super.itemStateChanged(e);
    if (e.getSource() instanceof ObjectSelectionButton) {
      setDataForIndexChooser();
      // if(methodChoice != null)
      // if(objectNums != null)
      // this.indexChooser.setMethods(this.getApplicableMethods(((TimedAnimator)
      // getCurrentObject()).getProperty(0))/*extractMethods(this.methodChoice.getModel())*/);
    }
  }

  private void setDataForIndexChooser() {
    if (objectNums != null) {
      PTGraphicObject[] objects = new PTGraphicObject[objectNums.length];
      for (int i = 0; i < objects.length; ++i) {
        objects[i] = Animation.get().getGraphicObject(objectNums[i]);
      }
      this.indexChooser.setData(objects, this
          .getApplicableMethods(((TimedAnimator) getCurrentObject())
              .getProperty(0)));
    }
  }

  
//  private Vector<String> extractMethods(ComboBoxModel model) {
//    Vector<String> returnValue = new Vector<String>();
//    for (int i = 0; i < model.getSize(); ++i) {
//      returnValue.add(model.getElementAt(i).toString());
//    }
//    return returnValue;
//  }

  public String[] getSupportedTypes() {
    return new String[] { animal.graphics.meta.IndexableContentContainer.TYPE_LABEL };
  }
}
