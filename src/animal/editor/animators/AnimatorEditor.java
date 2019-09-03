package animal.editor.animators;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import translator.AnimalTranslator;
import animal.animator.Animator;
import animal.editor.Editor;
import animal.gui.AnimalMainWindow;
import animal.main.Animation;
import animal.main.Link;
import animal.misc.EditableObject;
import animal.misc.ObjectSelectionButton;
import animal.misc.XProperties;

/**
 * Editor for an Animator.
 * @see animal.animator.Animator
 *
 * @author <a href="http://www.algoanim.info/Animal2/">Guido R&ouml;&szlig;ling</a>
 * @version 2.5 2008-06-23
 */
public abstract class AnimatorEditor extends Editor {
  /**
	 * 
	 */
	private static final long serialVersionUID = -7913603761462826766L;

/**
   * TimedAnimatorEditor requires objectSB to determine the
   * possible methods.
   */
  public ObjectSelectionButton objectSB;

  /**
   * The label for the step
   */
  private JLabel stepLabel;

  /**
   * creates the new animator editor instance
   */
  public AnimatorEditor() {
    super(AnimalMainWindow.getWindowCoordinator().getAnimationOverview(false));
    // add the step label and OSB
//    if (getCurrentObject(false) != null && ((Animator)getCurrentObject(false)).isGraphicalObjectAnimator())
      addStepLabelOSB();
//    else
//      addStepLabel();
    
    // now build the GUI as the client needs it
    buildGUI();
  }

  protected abstract void buildGUI();
  
  void addStepLabelOSB(){
    JPanel c = new JPanel();
    c.setLayout(new FlowLayout(FlowLayout.LEFT));
    c.add(AnimalTranslator.getGUIBuilder().generateJLabel("GenericEditor.step"));
    c.add(stepLabel = AnimalTranslator.getGUIBuilder().generateJLabel("GenericEditor.end"));
    c.add(objectSB = new ObjectSelectionButton(true));
    addLayer(c);
  }

  void addStepLabel(){
    JPanel c = new JPanel();
    c.setLayout(new FlowLayout(FlowLayout.LEFT));
    c.add(AnimalTranslator.getGUIBuilder().generateJLabel("GenericEditor.step"));
    c.add(stepLabel = AnimalTranslator.getGUIBuilder().generateJLabel("GenericEditor.end"));
    objectSB = new ObjectSelectionButton(true);
    addLayer(c);
  }

  
  // ======================================================================
  //                       Property Management
  // ======================================================================

  /**
   * Extracts the relevant attributes from the editable object
   *
   * @param eo must be of type Animator
   */
  public void extractAttributesFrom(EditableObject eo) {
    super.extractAttributesFrom(eo);

    Animator animator = (Animator) eo;
    stepLabel.setText(Integer.toString(animator.getStep()));
    objectSB.setObjectNums(animator.getObjectNums());
  }

  /**
    * returns an appropriate message if at most one animator is set per step and object
    *
    * @return the appropriate message as feedback
    */
  protected String isOK() {
    String result = super.isOK();

    if (result != null) {
      return result;
    }

    objectSB.checkObjects();

    int[] nums = objectSB.getObjectNums();
    int step = getStep();

    if (((Animator)getCurrentObject()).isGraphicalObjectAnimator()
        && ((nums == null) || (nums.length == 0))) {
      return AnimalTranslator.translateMessage("AnimatorEditor.noObjectSelectedException");
    }

    // allow only one animator per Step and Object
    result = Animation.get().checkSingleAnimator(nums, step,
        (Animator) getCurrentObject());

/*
    if (result != null) {
      return AnimalTranslator.translateMessage("animatorExistsInStepException",
        new Object[] { result, String.valueOf(step) });
    	return null;
    }
*/
    return null;
  }

  /**
   * Stores the relevant attributes into the editable object
   *
   * @param a an EditableObject that must be of type Animator
   */
  public void storeAttributesInto(EditableObject a) {
    super.storeAttributesInto(a);

    Animator animator = (Animator) a;

    // the step is not taken from the Editor itself but
    // from the AnimationOverview to reflect the current
    // step set there.
    int step = getStep();

    if (step == 0) {
      step = AnimalMainWindow.getWindowCoordinator().getAnimationOverview(true).getStep();

      if (step == Link.END) {
        step = Animation.get().getLastStep();
      }

      // in the PrimaryEditor, the stepLabel must remain "", as
      // otherwise all new objects get the same step!
      if (!isPrimaryEditor()) {
        stepLabel.setText(Integer.toString(step));
      }
    }

    animator.setStep(step);
    animator.setObjectNums(objectSB.getObjectNums());
  }

  // ======================================================================
  //                        Attribute Access
  // ======================================================================

  /**
   * assigns the current properties based on the parameter passed in
   *
   * @param props the properties to be used
   */
  public void getProperties( XProperties props) {
    // do nothing; only used for serialization
  }

  /**
    * no secondary editor, as AnimatorEditor is abstract.
    *
    * @param go an EditableObject instance for which a secondary editor is wanted
    * @return null - there is no secondary editor for this type!
    */
  public Editor getSecondaryEditor( EditableObject go) {
    return null;
  }

  /**
    * retrieves the current step as an int value, called by ObjectSelectionButton
    *
    * @return the current step as an int value
    */
  public int getStep() {
    return getInt(stepLabel.getText(), 0);
  }

  /**
    * if an AnimatorEditor has changed, the AnimationOverview
    * must be updated.
    */
  public void repaintNow() {
    AnimalMainWindow.getWindowCoordinator().getAnimationOverview(true).initList(false);
  }
}
