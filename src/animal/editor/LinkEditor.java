package animal.editor;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.text.DefaultEditorKit;

import translator.AnimalTranslator;
import animal.gui.AnimalMainWindow;
import animal.main.Animal;
import animal.main.Animation;
import animal.main.Link;
import animal.misc.EditableObject;
import animal.misc.ObjectSelectionButton;
import animal.misc.TextUtilities;
import animal.misc.XProperties;

/**
 * The Editor for a Link.
 * 
 * @author <a href="http://www.algoanim.info/Animal2/">Guido R&ouml;&szlig;ling</a>
 * @version 2.0 2001-03-16
 */
public class LinkEditor extends Editor implements ItemListener {
  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = -327517124919975759L;

  private JLabel stepL;

  private JLabel nextStepL;

  private JRadioButton keyCB;

  private JRadioButton timeCB;

  private JRadioButton clickCB;

  private JTextField labelTF;

  private ObjectSelectionButton objectIDSB;

  /**
   * The time TextField is only enabled if the time Checkbox is selected
   */
  private JTextField timeTF;

  // private JTextField targetObjectIDTF;
  private JTextField clickPromptTF;

  public LinkEditor() {
    super(AnimalMainWindow.getWindowCoordinator().getAnimationOverview(false));

    GridBagLayout gbl = new GridBagLayout();
    JPanel c = new JPanel(gbl);
    JLabel l = null;

    GridBagConstraints gbc = new GridBagConstraints();

    JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.anchor = GridBagConstraints.NORTHWEST;
    gbl.setConstraints(p, gbc);
    c.add(p);

    p
        .add(AnimalTranslator.getGUIBuilder().generateJLabel(
            "GenericEditor.step"));

    // provide enough space. Thus, the window doesn't have to be
    // repacked. This is not critical(as in ObjectSelectionButton, e.g.),
    // because it's "only" a label and has no painted border, therefore
    // the size is not visible if there are larger components.
    p.add(stepL = AnimalTranslator.getGUIBuilder().generateJLabel(
        "GenericEditor.end"));

    p.add(AnimalTranslator.getGUIBuilder()
        .generateJLabel("LinkEditor.nextStep"));

    p.add(nextStepL = AnimalTranslator.getGUIBuilder().generateJLabel(
        "LinkEditor.endOfAnimation"));

    ButtonGroup buttonGroup = new ButtonGroup();

    // wait for pressing a key (usually "play")
    keyCB = (JRadioButton) AnimalTranslator.getGUIBuilder()
        .generateJToggleButton("LinkEditor.waitKey", null, null, true);
    keyCB.addItemListener(this);
    // JRadioButton is taller than JLabel and JTextField. So align all
    // the line at West. If it's aligned NorthWest, the baseline will
    // not be a line, but something like ___-----___
    gbc.anchor = GridBagConstraints.WEST;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbl.setConstraints(keyCB, gbc);
    c.add(keyCB);
    buttonGroup.add(keyCB);

    // wait for time
    timeCB = (JRadioButton) AnimalTranslator.getGUIBuilder()
        .generateJToggleButton("LinkEditor.waitTime", null, null, true);
    timeCB.addItemListener(this);
    gbc.gridwidth = GridBagConstraints.RELATIVE;
    gbl.setConstraints(timeCB, gbc);
    c.add(timeCB);
    buttonGroup.add(timeCB);

    timeTF = new JTextField(20);
    // gbc.gridwidth = gbc.REMAINDER;
    gbl.setConstraints(timeTF, gbc);
    c.add(timeTF);
    // g.add(timeTF);

    l = new JLabel("ms");
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbl.setConstraints(l, gbc);
    c.add(l);
    // g.add(l);

    // wait for click on a specific object
    clickCB = (JRadioButton) AnimalTranslator.getGUIBuilder()
        .generateJToggleButton("LinkEditor.waitObjectClick", null, null, true);
    clickCB.addItemListener(this);
    gbc.gridwidth = GridBagConstraints.RELATIVE;
    gbl.setConstraints(clickCB, gbc);
    buttonGroup.add(clickCB);
    c.add(clickCB);

    objectIDSB = new ObjectSelectionButton(false);
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbl.setConstraints(objectIDSB, gbc);
    c.add(objectIDSB);
    buttonGroup.add(objectIDSB);

    // JLabel l = AnimalTranslator.getGUIBuilder().generateJLabel("timeLabel");
    // gbc.gridwidth = 1;
    // gbl.setConstraints(l, gbc);
    // c.add(l);

    // timeTF = new JTextField(20);
    // gbc.gridwidth = gbc.REMAINDER;
    // gbl.setConstraints(timeTF, gbc);
    // c.add(timeTF);

    // l = AnimalTranslator.getGUIBuilder().generateJLabel("clickOnLabel");
    // gbc.gridwidth = 1;
    // gbl.setConstraints(l, gbc);
    // c.add(l);

    // targetObjectIDTF = new JTextField(20);
    // gbc.gridwidth = gbc.REMAINDER;
    // gbl.setConstraints(targetObjectIDTF, gbc);
    // c.add(targetObjectIDTF);

    // text for the click prompt
    l = AnimalTranslator.getGUIBuilder().generateJLabel(
        "LinkEditor.clickPromptLabel");
    gbc.gridwidth = 1;
    gbl.setConstraints(l, gbc);
    c.add(l);

    clickPromptTF = new JTextField(20);
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbl.setConstraints(clickPromptTF, gbc);
    c.add(clickPromptTF);

    // step label
    l = AnimalTranslator.getGUIBuilder()
        .generateJLabel("LinkEditor.labelLabel");
    gbc.gridwidth = GridBagConstraints.RELATIVE;
    gbl.setConstraints(l, gbc);
    c.add(l);

    labelTF = new JTextField(20);
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbl.setConstraints(labelTF, gbc);
    c.add(labelTF);

    p = new JPanel();
    Action theAction = TextUtilities
        .findTextFieldAction(DefaultEditorKit.cutAction);
    p.add(AnimalTranslator.getGUIBuilder().generateActionButton(
        "AbstractTextEditor.cut", theAction));

    theAction = TextUtilities.findTextFieldAction(DefaultEditorKit.copyAction);
    p.add(AnimalTranslator.getGUIBuilder().generateActionButton(
        "AbstractTextEditor.copy", theAction));

    theAction = TextUtilities.findTextFieldAction(DefaultEditorKit.pasteAction);
    p.add(AnimalTranslator.getGUIBuilder().generateActionButton(
        "AbstractTextEditor.paste", theAction));

    c.add(p, gbc);
    addLayer(c);
    finish();
  }

  /**
   * if a Link is changed, the AnimationOverview has to be redrawn but not
   * refetched as no lines are inserted or deleted! All the Link's attributes
   * are reflected in the one line it owns in the AnimationOverview.
   */
  public void repaintNow() {
    AnimalMainWindow.getWindowCoordinator().getAnimationOverview(true)
        .initList(false);
  }

  public Editor getSecondaryEditor(EditableObject eo) {
    LinkEditor result = new LinkEditor();
    result.extractAttributesFrom(eo);
    return result;
  }

  public void setProperties(
  XProperties props) {
    // do nothing; only used for serialization
  }

  public void getProperties(
  XProperties props) {
    // do nothing; only used for serialization
  }

  public void extractAttributesFrom(EditableObject eo) {
    super.extractAttributesFrom(eo);
    Link link = (Link) eo;
    stepL.setText(Animation.getStepString(link.getStep()));
    nextStepL.setText(Animation.getStepString(link.getNextStep()));
    setInt(timeTF, link.getTime());
    keyCB.setSelected(link.getMode() == Link.WAIT_KEY);
    timeCB.setSelected(link.getMode() == Link.WAIT_TIME);
    clickCB.setSelected(link.getMode() == Link.WAIT_CLICK);
    objectIDSB.setObjectNum(link.getTargetObjectID());
    labelTF.setText(link.getLinkLabel());
    // targetObjectIDTF.setText(String.valueOf(link.getTargetObjectID()));
    clickPromptTF.setText(link.getClickPrompt());
  }

  public void storeAttributesInto(EditableObject l) {
    super.storeAttributesInto(l);
    Link link = (Link) l;
    link.setTime(getInt(timeTF.getText(), 0));
    if (timeCB.isSelected())
      link.setMode(Link.WAIT_TIME);
    else if (keyCB.isSelected())
      link.setMode(Link.WAIT_KEY);
    else if (clickCB.isSelected())
      link.setMode(Link.WAIT_CLICK);
    link.setLinkLabel(labelTF.getText());
    // int targetVal = Integer.valueOf(targetObjectIDTF.getText()).intValue();
    int targetVal = objectIDSB.getObjectNum();
    link.setTargetObjectID(targetVal);
    link.setClickPrompt(clickPromptTF.getText());
    AnimalMainWindow.getWindowCoordinator().getTimeLineWindow(false)
        .updateList(Animal.get().getAnimation());
  }

  /**
   * In no way a link can be wrong so always return super.isOK
   */
  protected String isOK() {
    return super.isOK();
  }

  /**
   * a new link is not created by calling its primary Editor's createObject
   * method, as Link is not available in an ObjectPanel. Nevertheless, this
   * method must be implemented to avoid having an abstract class. So just
   * return <b>null</b>.
   */
  public EditableObject createObject() {
    return null;
  }

  /**
   * make the time TextField active only if the time Checkbox is selected
   */
  public void itemStateChanged(
  ItemEvent e) {
    timeTF.setEnabled(timeCB.isSelected());
  }
}
