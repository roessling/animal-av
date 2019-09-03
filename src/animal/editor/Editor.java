package animal.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.TextEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.JTextComponent;

import net.miginfocom.swing.MigLayout;
import translator.AnimalTranslator;
import translator.ExtendedActionButton;
import translator.TranslatableGUIElement;
import animal.animator.Animator;
import animal.editor.graphics.meta.GraphicEditor;
import animal.graphics.PTGraphicObject;
import animal.gui.AnimalMainWindow;
import animal.gui.GraphicVector;
import animal.gui.GraphicVectorEntry;
import animal.main.AnimalConfiguration;
import animal.main.Animation;
import animal.main.Link;
import animal.misc.ColorChoice;
import animal.misc.ColorChooserAction;
import animal.misc.EditableObject;
import animal.misc.MessageDisplay;
import animal.misc.XProperties;

/**
 * The base class of all Editors, i.e., dialogs that implement buttons,
 * inputlines etc. to change a GraphicObject's or an Animator's attributes.
 * <p>
 * Editor provides functionality for applying changes to the objects, creating
 * new objects and creating secondary editors for objects.
 * 
 * The "global" layout of Editor is a GridBagLayout. But each layer of
 * subclasses may insert an own layout that uses one row of the gridbag. e.g.
 * MoveEditor implements TimedAnimatorEditor implements AnimatorEditor
 * implements Editor. AnimatorEditor uses the first row(laid out with a
 * FlowLayout) TimedAnimatorEditor: 2nd row(FlowLayout) MoveEditor: 3rd
 * row(FlowLayout)
 * 
 * Editor itself inserts its panel last, so: 4th row(FlowLayout containing the
 * Buttons) These subclasses' layouts can be inserted by calling "addLayer"
 * 
 * For an example Editor, refer to <b>BoxPointerEditor</b>
 * 
 * @see #addLayer(javax.swing.JComponent)
 * @see animal.editor.graphics.BoxPointerEditor
 * 
 * @author <a href="http://www.algoanim.info/Animal2/">Guido R&ouml;&szlig;ling</a>
 * @version 2.0 2001-03-16
 */
public abstract class Editor extends JDialog implements Cloneable,
    ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = -7898171106528118123L;

/**
   * the package containing the editors, required for file-/classname
   * comparisons. Is accessed from Editor to determine the class name without
   * the package name.
   */
  public static final String EDITOR_PATH = "animal.editor.";

  protected static final String LAYOUT_PARAGRAPH_GAP = "gap para";

  protected static final String LAYOUT_PARAGRAPH_GAP_WRAP = "gap para, wrap";
  
  protected static final String LAYOUT_GROWX_SPAN_WRAP = "span, growx, wrap";
  
  protected static final String LAYOUT_PARAGRAPH_GAP_GROWX_SPAN_WRAP 
  = "gap para,span, growx, wrap";
  
  protected static final String LAYOUT_WRAP = "wrap";

  /**
   * all Editors currently visible
   */
  private static Vector<Editor> allEditors = new Vector<Editor>();

  /**
   * the order of the Editor in the ObjectPanel. Not all numbers are available
   * in each ObjectPanel but one contains the numbers from 1 to x and the other
   * from x+1 to y, but nevertheless the order from the file can be
   * reconstructed.
   */
  private int num = -1;

  /**
   * the "global" GridBagLayout. Editor uses a multilayered Layout. Into every
   * row of this GridBagLayout, a Panel(having its own layout) is inserted.
   * 
   * @see #addLayer(javax.swing.JComponent)
   * @see #finish(javax.swing.JTabbedPane)
   */
  private GridBagLayout gbl;

  protected JPanel basicPanel;

  protected AbstractButton okButton;

  protected AbstractButton applyButton;

  protected AbstractButton cancelButton;

  XProperties keyLUT, labelLUT;

  static XProperties defaultKeyLUT, defaultLabelLUT;

  /*
   * GroupLayout.ParallelGroup row1, row2, col1, col2;
   * GroupLayout.SequentialGroup hGroup, vGroup;
   */
  Box mainContentBox = null;

  protected HashMap<String, JComponent> components;

  /**
   * The constructor of the edit dialog. Sets the title to "XXXX Options", where
   * XXXX is the graphic object's class name, then sets the Layout to Flowlayout
   * and the location to the upper left corner of the main window. The
   * <code>colorChoice</code> is inserted all Editors that are loaded from
   * Editors.dat must have a noargs constructor!
   */
  protected Editor(JFrame parent) {
    this(parent, true);
//    super(parent);
//
//    setTitle(getName() + " "
//        + AnimalTranslator.translateMessage("GenericEditor.options"));
//
//    gbl = new GridBagLayout();
//    getContentPane().setLayout(new BorderLayout());
//    components = new HashMap<String, JComponent>(50);
//    // react to closing the window
//    addWindowListener(new WindowAdapter() {
//      public void windowClosing(WindowEvent event) {
//        Object object = event.getSource();
//        if (object == Editor.this)
//          close();
//      }
//    });
//    installBoxLayout(); // belongs to Editor "later on"
  }

  /**
   * The constructor of the edit dialog. Sets the title to "XXXX Options", where
   * XXXX is the graphic object's class name, then sets the Layout to Flowlayout
   * and the location to the upper left corner of the main window. The
   * <code>colorChoice</code> is inserted all Editors that are loaded from
   * Editors.dat must have a noargs constructor!
   */
  protected Editor(JFrame parent, boolean boxMode) {
    super(parent);

    setTitle(getName() + " "
        + AnimalTranslator.translateMessage("GenericEditor.options"));

    components = new HashMap<String, JComponent>(50);
    // react to closing the window
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent event) {
        Object object = event.getSource();
        if (object == Editor.this)
          close();
      }
    });
    if (boxMode) {
      gbl = new GridBagLayout();
      getContentPane().setLayout(new BorderLayout());
      installBoxLayout(); // belongs to Editor "later on"
    }
  }

  /**
   * makes the Editor visible in the center of the screen. A better "algorithm"
   * should be found where to place the Editors (like cascading or tiling them
   * on the screen). The Editor is added to(or removed from) the collection of
   * all available Editors.
   * 
   * @param isVisible
   *          if true, make the component visible, else hide it
   */
  public void setVisible(boolean isVisible) {
    if (isVisible) {
      allEditors.addElement(this);
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      Dimension thisSize = getSize();
      // TODO GR does this work out fine?
      setSize(getPreferredSize());
      if (getLocation().x == 0 && getLocation().y == 0)
        setLocation((screenSize.width - thisSize.width) / 2,
            (screenSize.height - thisSize.height) / 2);

    } else
      allEditors.removeElement(this);
    super.setVisible(isVisible);
  }

  /**
   * redraws the windows that are affected by the changes that took place in the
   * Editor. For example, when a GraphicEditor's attributes are changed, the
   * DrawWindow has to be updated.
   */
  public abstract void repaintNow();

  /**
   * checks if all fields have valid entries. If checking an
   * ObjectSelectionButton for a valid entry(like e.g. the path of a Move), it
   * must call Editor.getGraphicObject, not Animation.getGraphicObject. For the
   * reason, cf. documentation of getGraphicObject. If an ObjectSelectionButton
   * is used, its <code>checkObject</code>- method should be called here to
   * ensure that no deleted objects are used.
   * 
   * @return null if everything is OK,<br>
   *         otherwise, a message describing the error. if super.isOK is called
   *         and returns a non-null value, this value should be returned.
   * @see #getGraphicObject(int)
   */
  protected String isOK() {
    return null;
  }

  /**
   * applies the Editor's content to the object it edits(if possible) and
   * refreshes the windows concerned.
   * 
   * @return true if the changes could be applied,<br>
   *         false if some fields had invalid entries
   * @see #isOK()
   */
  protected boolean apply() {
    String error = isOK();
    if (error != null) {
      JOptionPane.showMessageDialog(this, error, AnimalTranslator
          .translateMessage("GenericEditor.editorError"),
          JOptionPane.ERROR_MESSAGE);
      return false;
    }
    // write the attributes back to the object
    EditableObject a = getCurrentObject();
    if (a != null)
      storeAttributesInto(a);
    // check if Object must be inserted into animation(i.e. it is a new
    // Animator), then repaint with list fetch, otherwise just repaint
    if (a instanceof Animator
        && !Animation.get().containsAnimator((Animator) a)) {
      Animation.get().insertAnimator((Animator) a);
      // if an Animator is inserted, the list must be refetched
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
    if (!(this instanceof GraphicEditor))
      getProperties(AnimalConfiguration.getDefaultConfiguration()
          .getProperties());
    return true;
  }

  /**
   * closes the Editor and resets its object's Editor to the primary Editor.
   * called from GraphicVector when deleting objects.
   */
  public void close() {
    EditableObject a = getCurrentObject();
    if (a != null)
      a.resetEditor();
    // when the primary GraphicEditor is closed, no GraphicEditor
    // should be available
    if (this instanceof GraphicEditor && isPrimaryEditor())
      AnimalMainWindow.getWindowCoordinator().getDrawWindow(false)
          .getDrawCanvas().setGraphicEditor(null);

    // setVisible is required as dispose only calls hide(), even
    // though this is deprecated :(
    setVisible(false);
    dispose();

    Set<Map.Entry<String, JComponent>> elementKeys = components.entrySet();
    Iterator<Map.Entry<String, JComponent>> keyIterator = elementKeys
        .iterator();
    TranslatableGUIElement generator = AnimalTranslator.getGUIBuilder();
    while (keyIterator.hasNext()) {
      Map.Entry<String, JComponent> currentKey = keyIterator.next();
      generator.unregisterComponent(currentKey.getKey(), currentKey.getValue());
    }
  }

  protected JComponent getEditorPanel(
  boolean useAll, 
  boolean[] used) {
    return null;
  }

  protected JComponent getEditorPanel(
  XProperties theKeyLUT, 
  XProperties theLabelLUT, 
  ActionListener l, 
  ItemListener i) {
    return getEditorPanel(true, null);
  }

  /**
   * returns an int from a <code>TextComponent</code> that has changed and
   * thus informed its TextListener. Can be called from textValueChanged.
   * 
   * @param e
   *          the TextEvent the TextComponent sent to its listener
   * @param defaultValue
   *          the value to be returned if the TextComponent's value is not a
   *          valid number.
   */
  protected int getInt(TextEvent e, int defaultValue) {
    return getInt(((JTextComponent) e.getSource()).getText(), defaultValue);
  }

  /**
   * extracts an int from a String. If this is not possible, <i>defaultValue</i>
   * is returned.
   * 
   * @param defaultValue
   *          the default value to return if the String does not encode an int
   *          value
   */
  protected int getInt(String s, int defaultValue) {
    try {
      return Integer.parseInt(s);
    } catch (NumberFormatException exc) {
      return defaultValue;
    }
  }

  /**
   * extracts an float from a String. If this is not possible, <i>defaultValue</i>
   * is returned.
   */
  protected double getDouble(String s, double defaultValue) {
    try {
      return Double.parseDouble(s);
    } catch (NumberFormatException exc) {
      return defaultValue;
    }
  }

  /**
   * sets a TextComponent to an int. Just a convenience method as I always
   * forgot the conversion methods ;)
   */
  public void setInt(JTextComponent tc, int value) {
    tc.setText(String.valueOf(value));
  }

  /**
   * Read the Object's default properties from the given <code>properties</code>
   * object. Must be overwritten in order to achieve persistence of the object's
   * attributes.
   * 
   * @see #getProperties(XProperties)
   */
  public abstract void setProperties(XProperties props);

  /**
   * Write the properties into the given <code>properties</code> object. Must
   * be overwritten in order to achieve persistence of the object's attributes.
   * 
   * @see #setProperties(XProperties)
   */
  public abstract void getProperties(XProperties props);

  /**
   * the name of the graphic class without its package name, e.g.
   * "animal.editor.CircleEditor" -> "Circle"
   */
  public final String getName() {
    String name = getClass().getName();
    int start = name.lastIndexOf('.');
    // int l = Editor.EDITOR_PATH.length();
    // delete the beginning(always "animal.editor") and the end
    // (always "Editor")
    name = name.substring(start + 1, name.indexOf("Editor"));
    return name;
  }

  /*****************************************************************************
   * editor related fields and methods
   ****************************************************************************/

  /**
   * the primary Editor that generated this Editor. Used for reset.
   */
  private Editor primaryEditor = null;

  /**
   * returns a secondary editor for the passed EditableObject. I.e. a new editor
   * is generated and the attributes of the EditableObject are copied.
   */
  abstract public Editor getSecondaryEditor(EditableObject go);

  public Editor getPrimaryEditor() {
    if (primaryEditor == null)
      primaryEditor = this;
    return primaryEditor;
  }

  public JComboBox<String> depthBox;

  public JTextField objectNameField;

  public boolean isPrimaryEditor() {
    return (primaryEditor == null || primaryEditor == this);
  }

  /*****************************************************************************
   * object related fields and methods
   ****************************************************************************/
  private EditableObject currentObject;

  /**
   * returns the object edited by this Editor.
   * 
   * @param warning
   *          if true issue a warning if the currentObject is null.
   */
  public EditableObject getCurrentObject(boolean warning) {
    if (currentObject == null && warning)
      MessageDisplay.errorMsg("GenericEditor.objectNull",
          new Object[] { getClass() });
    return currentObject;
  }

  /**
   * returns the object edited by this Editor. issues a warning if the
   * currentObject is null.
   */
  public EditableObject getCurrentObject() {
    return getCurrentObject(true);
  }

  /**
   * extracts attributes from an EditableObject, i.e. the Object's values are
   * copied into the Editor's components and the Editor is linked with the
   * Object. Must be overwritten and called by subclasses. Called when a
   * secondary editor is generated as this Editor must reflect the Object's
   * attributes. Must have parameter of type EditableObject, not of any
   * subclass! Otherwise super methods may not be called correctly.
   */
  protected void extractAttributesFrom(EditableObject go) {
    linkToEditor(go);
  }

  /**
   * stores attributes to an EditableObject, i.e. the Editor's component's
   * values are copied into the object and the Editor is linked with the object.
   * Must be overwritten and called by subclasses. Called when apply-button is
   * pressed as then the changes have to be written into the object. Must have
   * parameter of type EditableObject, not of any subclass! Otherwise super
   * methods may not be called correctly.
   */
  protected void storeAttributesInto(EditableObject go) {
    linkToEditor(go);
  }

  /**
   * links this Editor with an EditableObject. This link is bidirectional, i.e.
   * the Editor knows which object it edits and the object knows by which Editor
   * it is edited.
   */
  public void linkToEditor(EditableObject go) {
    // initialize primary editor field for secondary editors.
    currentObject = go;
    if (go != null) {
      if (primaryEditor == null)
        primaryEditor = go.getEditor();
      go.setEditor(this);
      // setting after(!) getting should be
      // logical, but took some minutes to find the error ;)
    }
    if (go != null && go != go.getEditor().getCurrentObject())
      MessageDisplay.errorMsg("GenericEditor.linkToEditorError",
          new Object[] { getClass() });
  }

  /**
   * creates a new default object of the type that corresponds to this Editor
   * and copies the Editor's default values into the new object. Must be
   * overwritten by every Editor! Must call <code>storeAttributesInto</code>
   * even if no attributes are available, as <code>storeAttributesInto</code>
   * also links the Object to its Editor!
   */
  public abstract EditableObject createObject();

  /**
   * adds a new Panel below the other panels in the Editor.
   * <p>
   * <b>See Also:</b> <a href="#_top_">intro</a>
   */
  public void addLayer(JComponent layer) {
    if (basicPanel == null) {
      basicPanel = new JPanel();
      basicPanel.setLayout(new GridLayout(0, 1));
      getContentPane().add(BorderLayout.CENTER, basicPanel);
      // set layout...?
    }
    GridBagConstraints gbc = new GridBagConstraints();
    // leave a litte space to the left and right
    gbc.insets = new Insets(0, 10, 0, 10);
    gbc.anchor = GridBagConstraints.NORTHWEST;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbl.setConstraints(layer, gbc);
    basicPanel.add(layer);
  }

  /**
   * adds the last row, containing buttons for OK, apply, Cancel
   */
  public void finish(JTabbedPane tp) {
    if (this instanceof GraphicEditor) {
      EditableObject o = getCurrentObject(false);
      TranslatableGUIElement generator = AnimalTranslator.getGUIBuilder();
      JPanel depthPanel = generator
          .generateBorderedJPanel("GenericEditor.depthBL");
      depthPanel.add(AnimalTranslator.getGUIBuilder().generateJLabel(
          "GenericEditor.depthPrompt"));
      depthBox = new JComboBox<String>();
      for (int i = 0; i <= 16; i++)
        depthBox.addItem(String.valueOf(i));
      int currentDepth = 0;

      depthBox.setEditable(true);
      depthBox.setSelectedItem(String.valueOf(currentDepth));
      depthBox.addActionListener(this);
      depthPanel.add(depthBox);

      if (tp != null) {
        tp.addTab(AnimalTranslator.translateMessage("GenericEditor.depth"),
            depthPanel);
        addLayer(tp);
      } else {
        addLayer(depthPanel);
      }

      JPanel namePanel = generator
          .generateBorderedJPanel("GenericEditor.nameBL");
      namePanel.add(AnimalTranslator.getGUIBuilder().generateJLabel(
          "GenericEditor.nameLabel"));
      String name = (o instanceof PTGraphicObject) ? ((PTGraphicObject) o)
          .getObjectName() : "";
      objectNameField = AnimalTranslator.getGUIBuilder().generateJTextField(
          "objName", null, 16, name);
      namePanel.add(objectNameField);

      if (tp != null) {
        tp.addTab(AnimalTranslator.translateMessage("GenericEditor.name"),
            namePanel);
        addLayer(tp);
      } else {
        addLayer(namePanel);
      }
    }
    finish();
  }

  /**
   * adds the last row, containing buttons for OK, apply, Cancel
   */
  public void finishBoxes() {
    TranslatableGUIElement generator = AnimalTranslator.getGUIBuilder();
    if (this instanceof GraphicEditor) {
      EditableObject o = getCurrentObject(false);
      Box depthBoxComponent = generator.generateBorderedBox(
          BoxLayout.LINE_AXIS, "GenericEditor.depthBL");
      // JPanel depthPanel = generator.generateBorderedJPanel("depthBL");
      depthBoxComponent.add(generator.generateJLabel(
          "GenericEditor.depthPrompt"));
      depthBox = new JComboBox<String>();
      for (int i = 0; i <= 16; i++)
        depthBox.addItem(String.valueOf(i));
      int currentDepth = 0;

      depthBox.setEditable(true);
      depthBox.setSelectedItem(String.valueOf(currentDepth));
      depthBox.addActionListener(this);
      depthBoxComponent.add(depthBox);

      addBox(depthBoxComponent);
      // if (tp != null) {
      // tp.addTab(AnimalTranslator.translateMessage("depth"), depthPanel);
      // addLayer(tp);
      // } else {
      // addLayer(depthPanel);
      // }

      Box nameBox = generator.generateBorderedBox(BoxLayout.LINE_AXIS,
          "GenericEditor.nameBL");
      nameBox.add(generator.generateJLabel(
          "GenericEditor.nameLabel"));
      String name = (o instanceof PTGraphicObject) ? ((PTGraphicObject) o)
          .getObjectName() : "";
      objectNameField = generator.generateJTextField(
          "objName", null, 16, name);
      nameBox.add(objectNameField);
      addBox(nameBox);
      // if (tp != null) {
      // tp.addTab(AnimalTranslator.translateMessage("name"), namePanel);
      // addLayer(tp);
      // } else {
      // addLayer(namePanel);
      // }
      components.put("GenericEditor.depthBL", depthBoxComponent);
      components.put("GenericEditor.nameBL", nameBox);
    }

    Box buttonBox = new Box(BoxLayout.LINE_AXIS);
    buttonBox.add(okButton = generator.generateJButton(
        "GenericEditor.ok", null, false, this));
    buttonBox.add(applyButton = generator
        .generateJButton("GenericEditor.apply", null, false, this));
    buttonBox.add(cancelButton = generator
        .generateJButton("GenericEditor.cancel", null, false, this));
    addBox(buttonBox);
    // getContentPane().add(BorderLayout.SOUTH, p);
    // finally let the window have its correct size
    pack();

    components.put("GenericEditor.objName", objectNameField);
    components.put("GenericEditor.ok", okButton);
    components.put("GenericEditor.apply", applyButton);
    components.put("GenericEditor.cancel", cancelButton);

    // finish();
  }
  
  /**
   * adds the last row, containing buttons for OK, apply, Cancel
   */
  public void finishEditor(Container cp) {
    TranslatableGUIElement generator = AnimalTranslator.getGUIBuilder();
    if (this instanceof GraphicEditor) {
      EditableObject o = getCurrentObject(false);
      insertSeparator("GenericEditor.depthBL", cp, generator);
//      Box depthBoxComponent = generator.generateBorderedBox(
//          BoxLayout.LINE_AXIS, "GenericEditor.depthBL");
      // JPanel depthPanel = generator.generateBorderedJPanel("depthBL");
      cp.add(generator.generateJLabel(
          "GenericEditor.depthPrompt"),
          Editor.LAYOUT_PARAGRAPH_GAP);
      depthBox = new JComboBox<String>();
      for (int i = 0; i <= 16; i++)
        depthBox.addItem(String.valueOf(i));
      int currentDepth = 0;

      depthBox.setEditable(true);
      depthBox.setSelectedItem(String.valueOf(currentDepth));
      depthBox.addActionListener(this);
      cp.add(depthBox, Editor.LAYOUT_PARAGRAPH_GAP_WRAP);
//      depthBoxComponent.add(depthBox);
//
//      addBox(depthBoxComponent);
      // if (tp != null) {
      // tp.addTab(AnimalTranslator.translateMessage("depth"), depthPanel);
      // addLayer(tp);
      // } else {
      // addLayer(depthPanel);
      // }

      
//      Box nameBox = generator.generateBorderedBox(BoxLayout.LINE_AXIS,
//          "GenericEditor.nameBL");
      insertSeparator("GenericEditor.nameBL", cp, generator);

      cp.add(generator.generateJLabel(
          "GenericEditor.nameLabel"), Editor.LAYOUT_PARAGRAPH_GAP);
      String name = (o instanceof PTGraphicObject) ? ((PTGraphicObject) o)
          .getObjectName() : "";
      objectNameField = generator.generateJTextField(
          "objName", null, 16, name);
      cp.add(objectNameField, Editor.LAYOUT_PARAGRAPH_GAP_GROWX_SPAN_WRAP);
          //Editor.LAYOUT_GROWX_SPAN_WRAP);
      components.put("GenericEditor.objName", objectNameField);

//      nameBox.add(objectNameField);
//      addBox(nameBox);
      // if (tp != null) {
      // tp.addTab(AnimalTranslator.translateMessage("name"), namePanel);
      // addLayer(tp);
      // } else {
      // addLayer(namePanel);
      // }
//      components.put("GenericEditor.depthBL", depthBoxComponent);
//      components.put("GenericEditor.nameBL", nameBox);
    }

    finish(generator, cp);
//    Box buttonBox = new Box(BoxLayout.LINE_AXIS);
//    buttonBox.add(okButton = generator.generateJButton(
//        "GenericEditor.ok", null, false, this));
//    buttonBox.add(applyButton = generator
//        .generateJButton("GenericEditor.apply", null, false, this));
//    buttonBox.add(cancelButton = generator
//        .generateJButton("GenericEditor.cancel", null, false, this));
//    addBox(buttonBox);
    // getContentPane().add(BorderLayout.SOUTH, p);
    // finally let the window have its correct size
//    pack();
//
//    components.put("GenericEditor.objName", objectNameField);
//    components.put("GenericEditor.ok", okButton);
//    components.put("GenericEditor.apply", applyButton);
//    components.put("GenericEditor.cancel", cancelButton);

    // finish();
  }

  protected JLabel insertSeparator(String key, Container toInsert,
      TranslatableGUIElement generator) {
    JLabel l = generator.generateJLabel(key);
    l.setForeground(Color.BLUE);

    toInsert.add(l, "gapbottom 1, span, split 2, aligny center");
    toInsert.add(new JSeparator(), "gapleft rel, growx");
    return l;
  }
  
  protected void finish(TranslatableGUIElement generator, Container cp) {
    // provide a visual separator
    insertSeparator("GenericEditor.finishing", cp, generator);
    
    // create OK, apply, cancel buttons
    okButton = generator.generateJButton("GenericEditor.ok", null, false,
        this);
    applyButton = generator.generateJButton("GenericEditor.apply", null,
        false, this);
    cancelButton = generator.generateJButton("GenericEditor.cancel", null,
        false, this);
    
    // add them with proper layout
    cp.add(okButton, Editor.LAYOUT_PARAGRAPH_GAP);
    cp.add(applyButton, Editor.LAYOUT_PARAGRAPH_GAP);
    cp.add(cancelButton, Editor.LAYOUT_PARAGRAPH_GAP_WRAP);

    // register components
    components.put("GenericEditor.ok", okButton);
    components.put("GenericEditor.apply", applyButton);
    components.put("GenericEditor.cancel", cancelButton);

    // finally let the window have its correct size
    pack();
//    components.put("GenericEditor.objName", objectNameField);
  }
  
  /**
   * adds the last row, containing buttons for OK, apply, Cancel
   */
  public void finish() {
    JPanel p = new JPanel();
    p.add(okButton = AnimalTranslator.getGUIBuilder().generateJButton(
        "GenericEditor.ok", null, false, this));
    p.add(applyButton = AnimalTranslator.getGUIBuilder().generateJButton(
        "GenericEditor.apply", null, false, this));
    p.add(cancelButton = AnimalTranslator.getGUIBuilder().generateJButton(
        "GenericEditor.cancel", null, false, this));
    getContentPane().add(BorderLayout.SOUTH, p);
    // finally let the window have its correct size
    components.put("GenericEditor.ok", okButton);
    components.put("GenericEditor.apply", applyButton);
    components.put("GenericEditor.cancel", cancelButton);
    pack();
  }

  /*
   * public GroupLayout installGroupLayout(JPanel panel) { GroupLayout layout =
   * new GroupLayout(panel); panel.setLayout(layout); // Turn on automatically
   * adding gaps between components layout.setAutoCreateGaps(true);
   *  // Turn on automatically creating gaps between components that touch //
   * the edge of the container and the container.
   * layout.setAutoCreateContainerGaps(true);
   * 
   * hGroup = layout.createSequentialGroup(); vGroup =
   * layout.createSequentialGroup(); row1 = layout.createParallelGroup(); row2 =
   * layout.createParallelGroup(); col1 = layout.createParallelGroup(); col2 =
   * layout.createParallelGroup(); hGroup.addGroup(row1); hGroup.addGroup(row2);
   * vGroup.addGroup(col1); vGroup.addGroup(col2); return layout; }
   */

  public void installBoxLayout() {
    mainContentBox = new Box(BoxLayout.PAGE_AXIS);
    // getContentPane().add(/* BorderLayout.CENTER, */ mainContentBox);
    // pane.add(mainContentBox);
    // @TODO GR
    JScrollPane pane = new JScrollPane(mainContentBox,
        ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    getContentPane().add(pane);
  }

  protected Container setupLayout() {
    JPanel cp = new JPanel();
    cp.setLayout(new MigLayout());
    JScrollPane pane = new JScrollPane(cp,
        ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    getContentPane().add(pane);
    return cp;
  }
  
  /**
   * adds a new Panel below the other panels in the Editor.
   * <p>
   * <b>See Also:</b> <a href="#_top_">intro</a>
   */
  public void addBox(Box newBox) {
    if (mainContentBox == null) {
      installBoxLayout();
      getContentPane().add(BorderLayout.CENTER, mainContentBox);
    }
    mainContentBox.add(newBox);
  }
  

  public ColorChooserAction generateAndAddColorChoiceEntry(Box colorBox,
      TranslatableGUIElement generator, Color initialColor, int keyCode) {
    colorBox.add(generator.generateJLabel("GenericEditor.colorLabel"));
    // JLabel colorLabel = Translatnew JLabel("Color: ");
    // textPanel.add(colorLabel, gbc);
    // gbc.gridwidth = GridBagConstraints.REMAINDER;
    // Color initialColor = (getCurrentObject(false) == null) ? Color.black
    // : ((PTText) getCurrentObject(false)).getColor();
    ColorChooserAction colorChooser = new ColorChooserAction(this, ColorChoice
        .getColorName(initialColor), "textColor", AnimalTranslator
        .translateMessage("GenericEditor.chooseTextColor"), initialColor);
    colorBox.add(new ExtendedActionButton(colorChooser, keyCode));
    return colorChooser;
  }

  /**
   * reacts to the OK, apply and cancel buttons. <b>IMPORTANT</b> if any
   * subclass implements the ActionListener interface, it must call
   * <code>super.actionPerformed</code> in its own
   * <code>actionPerformed</code> method.
   */
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == okButton) {
      if (apply())
        close();
    } else if (e.getSource() == applyButton)
      apply();
    else if (e.getSource() == cancelButton)
      close();
  }

  /**
   * sets the Editor's position in the ObjectPanel.
   * 
   * @param numericID
   *          the ordinal for this editor
   */
  public void setNum(int numericID) {
    num = numericID;
  }

  /**
   * returns the Editors position in the ObjectPanel.
   */
  public int getNum() {
    return num;
  }

  /**
   * closes all Editors except the primary Editors.
   */
  public static void closeAllEditors() {
    // iterate from last to first as deletion shifts all Editors
    for (int i = allEditors.size() - 1; i >= 0; i--) {
      Editor e = allEditors.elementAt(i);
      if (!e.isPrimaryEditor()) {
        e.setVisible(false);
        e.dispose();
      }
    }
  }

  /**
   * closes the corresponding LinkEditors, i.e. LinkEditors that have
   * <code>step</code> as their origin step (or, if target is true, as their
   * target step)
   */
  public static void closeLinkEditors(int step, boolean target) {
    for (int i = allEditors.size() - 1; i >= 0; i--) {
      Editor e = allEditors.elementAt(i);
      if (e instanceof LinkEditor) {
        Link l = (Link) e.getCurrentObject();
        if (l != null
            && (l.getStep() == step || (target && l.getNextStep() == step))) {
          // allEditors.removeElementAt(i);
          e.setVisible(false);
          e.dispose();
        }
      }
    }
  }

  /**
   * required for Editors. If ObjectSelectionButtons for temporary objects check
   * validity of their objects(in Editor.isOK()), they need a reference to this
   * object instead of just a number. But if they call Animation.getObject(num)
   * and the object was not yet entered there, it is added together with a
   * Show-Animator, even though it's a temporary object. So first query
   * DrawWindow, if the objects exists. This doesn't touch its temporary status.
   * If all is OK, the animator can be added and if then a writeBack occurs, the
   * Animator is found and the object is added as a temporary object.
   */
  public static PTGraphicObject getGraphicObject(int num) {
    GraphicVector objects = AnimalMainWindow.getWindowCoordinator()
        .getDrawWindow(true).getDrawCanvas().getObjects();
    GraphicVectorEntry gve;
    if ((gve = objects.getGVEByNum(num)) != null)
      return gve.go;
    return Animation.get().getGraphicObject(num);
  }

} // Editor
