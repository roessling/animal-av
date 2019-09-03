/*
 * Created on 11.07.2007 by Guido Roessling (roessling@acm.org>
 */
package animal.gui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import translator.AnimalTranslator;
import translator.TranslatableGUIElement;
import animal.animator.GraphicObjectSpecificAnimation;
import animal.editor.Editor;
import animal.editor.graphics.meta.GraphicEditor;
import animal.main.Animal;
import animal.main.AnimalConfiguration;
import animal.misc.MessageDisplay;
import animal.misc.ObjectSelectionButton;

public class ObjectToolbar extends JToolBar implements ActionListener {
  /**
   * 
   */
  private static final long serialVersionUID = -8705707462029965359L;

  DrawWindow drawingWindow;

  private AbstractButton multiSelectionButton;

  private AbstractButton useEditorsButton;

  private HashMap<String, JButton> editorButtonTable;

  private JPanel internalPanel;

  /**
   * all buttons are in this buttonGroup. It is used to iterate these buttons.
   */
  private ButtonGroup buttonGroup;

  /**
   * used to store the state of multiSelectionButton before external selection
   * by an ObjectSelectionButton was started. This will be restored when the
   * ObjectSelectionButton is deselected.
   */
  private boolean oldMultiSelection;

  /**
   * used to store the state of useEditorsButton before external selection by an
   * ObjectSelectionButton was started. This will be restored when the
   * ObjectSelectionButton is deselected.
   */
  private boolean oldUseEditors;

  public ObjectToolbar(DrawWindow aDrawWindow) {
    super(SwingConstants.VERTICAL);
    drawingWindow = aDrawWindow;
    TranslatableGUIElement generator = AnimalTranslator.getGUIBuilder();
    GridLayout layout = new GridLayout(0, 2);
    layout.setHgap(2);
    layout.setVgap(0);
    internalPanel = new JPanel(layout);
    add(internalPanel);
    setLayout(new FlowLayout());

    // build select / edit buttons
    buildSelectEditComponents(generator);

    if (buttonGroup == null)
      buttonGroup = new ButtonGroup();
    if (editorButtonTable == null)
      editorButtonTable = new HashMap<String, JButton>(53);

    getFixedObjectsBox();

    installEditors();
  }

  private void buildSelectEditComponents(TranslatableGUIElement generator) {
    multiSelectionButton = generator.generateJButton("multiSelect", null, true,
        this);
    internalPanel.add(multiSelectionButton);

    useEditorsButton = generator.generateJButton("showEdit", null, true, this);
    internalPanel.add(useEditorsButton);
  }

  /**
   * constructs an ObjectToolBar.
   */
  public void getFixedObjectsBox() {
    // create content box

    // insert another panel with GridLayout. By this only minSize is used
    // by the GridLayout, not all the available space.
    AnimalConfiguration config = Animal.getAnimalConfiguration();
    Hashtable<String, Editor> editors = config.getEditors();
    String[] predefined = new String[] { 
        "Point", "Text", 
        "Line", "Polyline",
        "Square", "Rectangle", 
        "Triangle", "Polygon", 
        "Circle", "Ellipse",
        "OpenCircleSegment", "OpenEllipseSegment", 
        "ClosedCircleSegment", "ClosedEllipseSegment",
        "Arc", "IntArray", 
        "DoubleArray", "StringArray", 
        "IntMatrix", "DoubleMatrix",
        "StringMatrix", "BoxPointer" 
        };
    Editor editor = null;
    for (String key : predefined) {
      editor = editors.get(key);
      installElement(editor.getClass().getName(), editor);
    }
  }

  public void installElement(String editorClassName, Box editorsBox,
      Editor editor) {
    if (!(editorButtonTable.containsKey(editorClassName))) {
      String name = editor.getName();
      addButton(editorsBox, name + ".gif", name, editorClassName, name);
    }
  }

  public void installElement(String editorClassName, Editor editor) {
    if (!(editorButtonTable.containsKey(editorClassName))) {
      String name = editor.getName();
      addButton(name + ".gif", name, editorClassName, name);
    }
  }

  public void installEditors() {
    // install button group and editor table

    Editor editor = null;
    AnimalConfiguration config = Animal.getAnimalConfiguration();
    Hashtable<String, Editor> editors = config.getEditors();

    // iterate over all editors; retrieve numbered editor
    Enumeration<Editor> elems = editors.elements();
    while (elems.hasMoreElements()) {
      editor = elems.nextElement();
      if (editor != null) {
        String editorClassName = editor.getClass().getName();
        	/******************************************/
        	/******************************************/
        	/******************************************/
        	/******************************************/
        	/******************************************/
        if (!	editorClassName.substring(0, 4).equals("VHDL")){////
        	/******************************************/
        	/******************************************/
        	/******************************************/
        	/******************************************/
        	/******************************************/
        	if (editor instanceof GraphicEditor
            && !editorButtonTable.containsKey(editorClassName)) {
          installElement(editorClassName, editor);
        } // if Graphic etc.
      } // if not null
    } // while
  }
  }
  public void addButton(Box contentBox, String iconName, String toolTipText,
      String className, String name) {
    JButton button = new JButton(Animal.get().getImageIcon(iconName));
    button.setToolTipText(toolTipText);
    // ActionCommand is set to have a link between the
    // buttons and the Editors they display
    button.setActionCommand(name);
    button.addActionListener(this);
    buttonGroup.add(button);
    editorButtonTable.put(className, button);
    contentBox.add(button);
  }

  public void addButton(String iconName, String toolTipText, String className,
      String name) {
    JButton button = new JButton(Animal.get().getImageIcon(iconName));
    button.setToolTipText(toolTipText);
    // ActionCommand is set to have a link between the
    // buttons and the Editors they display
    button.setActionCommand(name);
    button.addActionListener(this);
    buttonGroup.add(button);
    editorButtonTable.put(className, button);
    internalPanel.add(button);
  }

  /**
   * sets the buttons' state according to the selection mode. Notification Order
   * is: InternPanel -> DrawCanvas -> GraphicVector
   */
  void setSelection(boolean selection, boolean multiSelection,
      boolean useEditors, ObjectSelectionButton osb) {
    boolean enableButtons = selection && (osb == null);
    // selectionButton.setSelected(enableButtons);

    // all the following buttons are only enabled if selection is on
    multiSelectionButton.setEnabled(enableButtons);
    useEditorsButton.setEnabled(enableButtons);
    // it's either selection or creating new objects
    if (selection)
      drawingWindow.getObjectPanel().setCurrentEditor(null);
    // propagate selection mode to drawCanvas
    drawingWindow.getDrawCanvas().setSelection(selection, multiSelection,
        useEditors, osb);
  }

  /**
   * turns selection on/off. <br>
   * called by ObjectPanel.
   */
  void setSelection(boolean selection) {
    setSelection(selection, multiSelectionButton.isSelected(), useEditorsButton
        .isSelected(), null);
  }

  /**
   * turn selection by an ObjectSelectionButton on/off. If turned on, the
   * current selection mode is stored and selection set to single selection
   * without editors. If turned on, restore the old selection mode.
   */
  void setExternalSelection(ObjectSelectionButton osb) {
    if (osb != null) {
      oldMultiSelection = multiSelectionButton.isSelected();
      oldUseEditors = useEditorsButton.isSelected();
      setSelection(true, osb.hasMultiSelection(), false, osb);
    } else
      setSelection(true, oldMultiSelection, oldUseEditors, null);
  }

  /**
   * selects a new Editor by its name. If the Editor is a GraphicEditor, it's
   * made the primary Editor used in DrawWindow, if it's an AnimatorEditor, a
   * new Animator is created and its Editor displayed.
   * 
   * @param selectedButton if null, just close current Editor else this is the
   * new Editor to be used
   */
  public void setCurrentEditor(JButton selectedButton) {
    if (selectedButton != null) {
      setCurrentEditor(selectedButton.getActionCommand());
      selectedButton.setSelected(true);
    }
  }

  /**
   * selects a new Editor by its name. If the Editor is a GraphicEditor, it's
   * made the primary Editor used in DrawWindow, if it's an AnimatorEditor, a
   * new Animator is created and its Editor displayed.
   * 
   * @param name
   *          null: just close current Editor; other: new Editor to be used
   */
  public void setCurrentEditor(String name) {
    boolean found = false;
    boolean thatsit; // the currently checked editor is the right one.
    if (name == null)
      return;
    Editor editor = Animal.get().getEditor(name, false);
    // check all buttons whether their ActionCommand fits. If so,
    // select this button and leave the loop
    if (editor instanceof GraphicObjectSpecificAnimation)
      found = true;
    else {
      Enumeration<AbstractButton> e = buttonGroup.getElements();
      while (!found && e.hasMoreElements()) {
        JButton rb = (JButton) e.nextElement();
        thatsit = name.equals(rb.getActionCommand());
        rb.setSelected(thatsit);
        found = found | thatsit;
      }
    }

    if (!found || editor == null) {
      MessageDisplay.errorMsg("illegalEditor", name,
          MessageDisplay.CONFIG_ERROR);
    } // name == null
    else {
      editor.createObject();
      drawingWindow.getDrawCanvas().setGraphicEditor((GraphicEditor) editor);
    }
  } // setCurrentEditor

  public void actionPerformed(ActionEvent event) {
    Object object = event.getSource();
    if (// object == selectionButton ||
    object == multiSelectionButton || object == useEditorsButton) {
      setSelection(true, multiSelectionButton.isSelected(), useEditorsButton
          .isSelected(), null);
    }
    if (object instanceof JButton) {
      setCurrentEditor(((JButton) object).getActionCommand());
    }
    if (object instanceof JMenuItem) {
      setCurrentEditor(((JMenuItem) object).getActionCommand());
    } else if (object instanceof JComboBox) {
      // determine selected element
      JComboBox<?> box = (JComboBox<?>) object;
      if (box.getSelectedIndex() > 0)
        setCurrentEditor((String) box.getSelectedItem());
      box.setSelectedIndex(0);
    }
  }
}
