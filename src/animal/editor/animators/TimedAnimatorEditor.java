package animal.editor.animators;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import translator.AnimalTranslator;
import translator.TranslatableGUIElement;
import animal.animator.TimedAnimator;
import animal.editor.Editor;
import animal.graphics.PTGraphicObject;
import animal.handler.GraphicObjectHandler;
import animal.main.Animation;
import animal.misc.EditableObject;
import animal.misc.ObjectSelectionButton;
import animal.misc.XProperties;

/**
 * The editor for TimedAnimator
 * 
 * @see animal.animator.TimedAnimator
 * 
 * @author <a href="http://www.algoanim.info/Animal2/">Guido R&ouml;&szlig;ling</a>
 * @version 2.0 2001-03-16
 */
public abstract class TimedAnimatorEditor extends AnimatorEditor implements
    ItemListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1285803205464035685L;

	JComboBox<String> methodChoice;

  private JToggleButton timeCB;

  private JToggleButton ticksCB;

  protected JTextField totalTF;

  private JTextField offsetTF;

  public TimedAnimatorEditor() {
    super();
  }

  protected void buildGUI() {
    objectSB.addItemListener(this);
    TranslatableGUIElement generator = AnimalTranslator.getGUIBuilder();
    GridBagLayout gbl = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();
    JPanel c = new JPanel(gbl);
    JLabel a = generator.generateJLabel("AnimatorEditor.methodLabel");
    gbl.setConstraints(a, gbc);
    c.add(a);
    methodChoice = new JComboBox<String>();
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.fill = GridBagConstraints.BOTH;
    gbl.setConstraints(methodChoice, gbc);
    c.add(methodChoice);

    timeCB = generator.generateJToggleButton("TimedAnimatorEditor.ms", 
        null, this, true); //new JRadioButton("ms", true);
    timeCB.setMnemonic(KeyEvent.VK_M);
    gbc.gridwidth = 1;
    gbl.setConstraints(timeCB, gbc);
    c.add(timeCB);

    a = generator.generateJLabel(
        "AnimatorEditor.totalLabel", null);
    gbl.setConstraints(a, gbc);
    c.add(a);
    totalTF = new JTextField(20);
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbl.setConstraints(totalTF, gbc);
    c.add(totalTF);

    ticksCB = generator.generateJToggleButton("TimedAnimatorEditor.ticks", 
        null, this, true); // new JRadioButton("ticks", false);
    ticksCB.setMnemonic(KeyEvent.VK_T);
    gbc.gridwidth = 1;
    gbc.anchor = GridBagConstraints.WEST;
    gbl.setConstraints(ticksCB, gbc);
    c.add(ticksCB);

    ButtonGroup g = new ButtonGroup();
    g.add(timeCB);
    g.add(ticksCB);

    a = generator.generateJLabel(
        "AnimatorEditor.offsetLabel", null);
    gbl.setConstraints(a, gbc);
    c.add(a);
    offsetTF = new JTextField(20);
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbl.setConstraints(offsetTF, gbc);
    c.add(offsetTF);

    addLayer(c);
  }

  /**
   * if a subclass implements setProperties, it must call super.setProperties!
   */
  public void setProperties(XProperties props) {
    if (props.getBoolProperty(TimedAnimator.TIME_UNIT_LABEL))
      ticksCB.setSelected(true);
    else
      timeCB.setSelected(true);
    setInt(totalTF, props.getIntProperty(TimedAnimator.DURATION_LABEL, 10));
    setInt(offsetTF, props.getIntProperty(TimedAnimator.OFFSET_LABEL, 0));
  }

  /**
   * if a subclass implements getProperties, it must call super.getProperties!
   */
  public void getProperties(XProperties props) {
    props.put(TimedAnimator.TIME_UNIT_LABEL, ticksCB.isSelected());
    props.put(TimedAnimator.DURATION_LABEL, getInt(totalTF.getText(), 0));
    props.put(TimedAnimator.OFFSET_LABEL, getInt(offsetTF.getText(), 0));
  }

  /**
   * TimedAnimator is abstract, so no secondary editor exists. Thus just return
   * null.
   */
  public Editor getSecondaryEditor(
  EditableObject go) {
    return null;
  }

  public void extractAttributesFrom(EditableObject eo) {
    super.extractAttributesFrom(eo);
    TimedAnimator ta = (TimedAnimator) eo;
    methodChoice.setSelectedItem(ta.getMethod());
    timeCB.setSelected(!ta.isUnitIsTicks());
    ticksCB.setSelected(ta.isUnitIsTicks());
    setInt(totalTF, ta.getDuration());
    setInt(offsetTF, ta.getOffset());
  }

  public void storeAttributesInto(EditableObject a) {
    super.storeAttributesInto(a);
    TimedAnimator ta = (TimedAnimator) a;
    ta.setMethod((String) methodChoice.getSelectedItem());
    ta.setUnitIsTicks(ticksCB.isSelected());
    ta.setDuration(getInt(totalTF.getText(), ta.getDuration()));
    ta.setOffset(getInt(offsetTF.getText(), ta.getOffset()));
  }

  int[] objectNums = null;

  /**
   * deal with the ObjectSelectionButton by adapting the Combobox that contains
   * all available methods. These methods are set to the intersection of the
   * available methods of all currently selected objects.
   */
  public void itemStateChanged(ItemEvent e) {
    if (e.getSource() instanceof ObjectSelectionButton) {
      int[] now = ((ObjectSelectionButton) e.getSource()).getObjectNums();
      if (now != objectNums && now != null) {
        objectNums = now;
        // fetch one dummy property
        Object property = ((TimedAnimator) getCurrentObject()).getProperty(0);
        Vector<String> methods = getApplicableMethods(property);
        // copy the intersection of the methods into the combobox
        // and try to keep the old method.
        String oldMethod = (String) methodChoice.getSelectedItem();
        if (methodChoice.getItemCount() > 0)
          methodChoice.removeAllItems();
        if (methods != null && methods.size() != 0) {
          for (int i = 0; i < methods.size(); i++) {
            String method = methods.elementAt(i);
            methodChoice.addItem(method);
          }
          methodChoice.setSelectedItem(oldMethod);
        } else {
          if (now != null && now.length > 1)
            methodChoice.addItem(AnimalTranslator
                .translateMessage("AnimatorEditor.noCommonMethod"));
          else
            methodChoice.addItem(AnimalTranslator
                .translateMessage("AnimatorEditor.noAppropriateMethod"));
        }
      } else if (now == null) { // no objects selected
        methodChoice.removeAllItems();
        methodChoice.addItem(AnimalTranslator
            .translateMessage("AnimatorEditor.noObjectSelectedException"));
      }
    }
    methodChoice.repaint();
  }

  protected Vector<String> getApplicableMethods(Object property) {
    // loop over all selected objects
    Vector<String> methods = null;
    // as the Editor handles only the events for the osb that contains
    // the objects to be animated, Animation.getGraphicObject may
    // be called(potentially inserting the element looked for) as
    // only elements that have been shown before may be animated.
    // Thus, if a new element is selected, a show animator is generated
    // and the object can not be animated.
    Vector<String> newMethods = null;
    GraphicObjectHandler handler = null;
    PTGraphicObject g = null;
    for (int a = 0; a < objectNums.length; a++) {
      g = Animation.get().getGraphicObject(objectNums[a]);
      newMethods = null;
      if (g != null)
        handler = g.getHandler();
      if (handler != null) {
        newMethods = handler.getMethods(g, property);
        if (newMethods == null) {// empty set => empty intersection
          methods = null;
          break;
        }
        if (methods == null)
          methods = newMethods;
        else { // calculate intersection
          for (int b = methods.size() - 1; b >= 0; b--) {
            if (!newMethods.contains(methods.elementAt(b))
                && !g.isCompatibleMethod(methods.elementAt(b)))
              methods.removeElementAt(b);
          }
        }
      }
    }
    return methods;
  }

  // private static final String NO_COMMON_METHOD_MSG = "No common method!";

  /**
   * Error message if no method is available for the selected objects.
   */
  // private static final String NO_METHOD_MSG = "No appropriate method!";
  /**
   * Error message if no object has been selected by the OSB.
   */
  // private static final String NO_OBJECT_SELECTED = "No object selected!";
  /**
   * returns OK if a valid method is selected
   */
  protected String isOK() {
    String result = super.isOK();
    if (result != null)
      return result;
    String method = (String) methodChoice.getSelectedItem();
    if (method == null)
      return null;
    if (method.equals(AnimalTranslator
        .translateMessage("AnimatorEditor.noCommonMethod"))
        || method.equals(AnimalTranslator
            .translateMessage("AnimatorEditor.noAppropriateMethod"))
        || method.equals(AnimalTranslator
            .translateMessage("AnimatorEditor.noObjectsSelected")))
      // == NO_COMMON_METHOD_MSG || method == NO_METHOD_MSG
      // || method == NO_OBJECT_SELECTED
      // this should already result in an error in super.isOK()
      // )
      return method;
    return null;
  }
}
