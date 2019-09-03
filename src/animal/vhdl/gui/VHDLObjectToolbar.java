package animal.vhdl.gui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.MediaTracker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import animal.animator.GraphicObjectSpecificAnimation;
import animal.editor.Editor;
import animal.editor.graphics.meta.GraphicEditor;
import animal.graphics.PTGraphicObject;
import animal.gui.DrawWindow;
import animal.handler.GraphicObjectHandler;
import animal.main.Animal;
import animal.misc.MessageDisplay;
import animal.vhdl.main.VHDLAnimalConfiguration;
import translator.ResourceLocator;

public class VHDLObjectToolbar extends JToolBar implements ActionListener {

  /**
   * <br>
   * insert VHDL components'toolsbar.
   * 
   * @author Lu,Zheng
   * @version 1.0
   */
  private static final long         serialVersionUID = 1L;
  private Hashtable<String, Editor> editors;
  DrawWindow                        drawingWindow;
  private static final String GRAPHICS_PATH = "/graphics/vhdl/";

  private HashMap<String, JButton>  editorButtonTable;
  private JPanel                    internalPanel;
  /**
   * all buttons are in this buttonGroup. It is used to iterate these buttons.
   */
  private ButtonGroup               buttonGroup;
  private String[]                  predefined       = new String[] { "And",
      "Nand", "Or", "Nor", "Not", "Xor", "Xnor", "RS", "JK", "D", "T", "Mux",
      "Demux", "Entity", "Wire"//
                                                     };

  public VHDLObjectToolbar(DrawWindow aDrawWindow) {
    super(SwingConstants.VERTICAL);
    drawingWindow = aDrawWindow;
    // TranslatableGUIElement guiGenerator = AnimalTranslator.getGUIBuilder();
    GridLayout layout = new GridLayout(0, 1);
    layout.setHgap(1);
    layout.setVgap(0);
    internalPanel = new JPanel(layout);
    add(internalPanel);
    setLayout(new FlowLayout());

    if (buttonGroup == null)
      buttonGroup = new ButtonGroup();
    if (editorButtonTable == null)
      editorButtonTable = new HashMap<String, JButton>(53);

    getFixedObjectsBox();

    installEditors();
  }

  public void installEditors() {
    // install button group and editor table

    Editor editor = null;
//    Hashtable<String, Editor> animalEditors = Animal.get().getEditors();
    
    // iterate over all editors; retrieve numbered editor
    Enumeration<Editor> elems = editors.elements();
    while (elems.hasMoreElements()) {
      editor = elems.nextElement();
      if (editor != null) {
        String editorClassName = editor.getClass().getName();
        if (isPredefined(editorClassName)) {
          if (editor instanceof GraphicEditor
              && !editorButtonTable.containsKey(editorClassName)) {
            installElement(editorClassName, editor);
//            animalEditors.put(editorClassName, editor);
//            System.err.println("added:" +editorClassName +" , " +editor);
          } // if Graphic etc.
        } // if not null
      } // while
    }
  }

  private boolean isPredefined(String editorClassName) {
    editorClassName.replace("VHDL.animal.editor.graphics.", "");
    editorClassName.replace("Editor", "");
    for (int i = 0; i < predefined.length; i++) {
      if (editorClassName.trim().equalsIgnoreCase(predefined[i]))
        return true;
    }

    return false;
  }

  public void getFixedObjectsBox() {
    // create content box

    // insert another panel with GridLayout. By this only minSize is used
    // by the GridLayout, not all the available space.
    VHDLAnimalConfiguration vconfig = new VHDLAnimalConfiguration();
    // vconfig.updatePrimitives();
    editors = vconfig.getEditors();

    Editor editor = null;
    for (String key : predefined) {
      editor = editors.get(key);
      if (editor != null)
        installElement(editor.getClass().getName(), editor);
      else
        System.err.println("Error loading editor for '" +key +"'");
    }

  }

  @SuppressWarnings("unused")
  private String[][] getNewElements() {

//    File file = new File("animal" + File.separator +"vhdl" + File.separator + "VHDLcomponents.dat");
    BufferedReader reader = null;
    ArrayList<String> keys = new ArrayList<String>(0);
    ArrayList<String> values = new ArrayList<String>(0);
    try {
      InputStream in = ResourceLocator.getResourceLocator().getResourceStream("/VHDLcomponents.dat");
      reader = new BufferedReader(new InputStreamReader(in));
      String tempString = null;
      int indexNum = 0;
      while ((tempString = reader.readLine()) != null) {
        indexNum = tempString.indexOf("=");
        keys.add(tempString.substring(0, indexNum));
        values.add(tempString.substring(indexNum + 1));
      }
      reader.close();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e1) {
        }
      }
    }
    String[][] newElements = new String[2][keys.size()];
    for (int i = 0; i < keys.size(); i++) {
      newElements[0][i] = keys.get(i);
      newElements[1][i] = values.get(i);
    }
    return newElements;
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
      StringBuilder handlerClassName = new StringBuilder(
          editorClassName.length() + 30);
      int startPos = "animal.vhdl.editor.graphics".length();
      int endPos = editorClassName.indexOf("Editor");
      handlerClassName.append("animal.vhdl.handler");
      String subset = editorClassName.substring(startPos, endPos);
      handlerClassName.append(subset)
          .append("Handler");
      @SuppressWarnings("rawtypes")
      Class handlerClass = null;
      try {
        handlerClass = Class.forName(handlerClassName.toString());
      } catch (ClassNotFoundException e) {
        System.err.println("Not found: " +handlerClassName.toString());
      }
      GraphicObjectHandler handler = null;;
      try {
        handler = (GraphicObjectHandler) handlerClass.newInstance();
      } catch (InstantiationException e) {
        System.err.println("cannot instantiate: " +handlerClassName.toString());
      } catch (IllegalAccessException e) {
        System.err.println("illegal access: " +handlerClassName.toString());
      }
      PTGraphicObject.registeredHandlers.put(name, handler);
      // MessageDisplay.message("....primitive."+name);
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
  
  Class<?> animalImageDummy = null;

  public ImageIcon getImageIcon(String name) {
    return ResourceLocator.getResourceLocator().getImageIcon(name);
//    if (name == null || name.length() == 0)
//      return null;
//
//    ImageIcon icon = null;
//    URL url = null;
//    if (animalImageDummy == null)
//      try {
//        animalImageDummy = Class.forName("graphics.vhdl.AnimalImageDummy");
//      } catch (ClassNotFoundException cfe) {
//        System.err.println("AnimalImageDummy could not be found!");
//      }
////      Get current classloader
//      if (animalImageDummy != null) {
//        ClassLoader cl = animalImageDummy.getClassLoader();
//        if (cl != null) {
//
//          url = cl.getResource("graphics/vhdl/" +name);
//          if (url != null) { 
//            icon = new ImageIcon(url);
//            if (icon != null)
//              return icon;
//          }
//          System.err.println("trying again, this failed... for graphics/" +name);
//
//        } else System.err.println("ClassLoader failed, null!");
//      }
//      if (animalImageDummy != null) {
//        url = animalImageDummy.getResource(GRAPHICS_PATH + name);
//        if (url != null)
//          System.err.println("URL for image is " +url.toString());
//        else
//          System.err.println("Oops, url is now null for image " +name);
//      }
//      else {
//        System.err.println("Argh for " +name +"!"); 
//        url = this.getClass().getResource(GRAPHICS_PATH + name);
//      }
//      if (url == null)
//        MessageDisplay.errorMsg("iconNotFound", name +" - 1",
//            MessageDisplay.CONFIG_ERROR);
//      else {
//        icon = new ImageIcon(url);
////        if (icon == null)
////        MessageDisplay.errorMsg("iconNotFound", name +" - 2",
////            MessageDisplay.CONFIG_ERROR);
////        else 
//          if (icon.getImageLoadStatus() == MediaTracker.ERRORED) 
//          MessageDisplay.errorMsg("iconNotFound", name +" - 3",
//              MessageDisplay.CONFIG_ERROR);
//      }
//
//      return icon;
  }
  
  public void addButton(String iconName, String toolTipText, String className,
      String name) {
      String path = name + ".gif";
//      Animal animal = 
      Animal.get();
      ImageIcon icon = getImageIcon(path);
      //new ImageIcon(path);
//    ImageIcon icon = new ImageIcon("VHDL" + File.separator + "graphic"
//        + File.separator + name + ".gif");
    JButton button = new JButton(icon);
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
   * selects a new Editor by its name. If the Editor is a GraphicEditor, it's
   * made the primary Editor used in DrawWindow, if it's an AnimatorEditor, a
   * new Animator is created and its Editor displayed.
   * 
   * @param selectedButton
   *          null: just close current Editor; other: new Editor to be used
   */
  public void setCurrentEditor(JButton selectedButton) {
    if (selectedButton != null) {
      setCurrentEditor(selectedButton.getActionCommand());
      selectedButton.setSelected(true);
    }
  }
  
  public Editor getEditor(String name, boolean yellOnError) {

    Editor result = editors.get(name);
    if ((result == null) && yellOnError) {
      MessageDisplay
          .errorMsg("noEditorFor", name, MessageDisplay.PROGRAM_ERROR);
    }

    return result;
  }

  public void setCurrentEditor(String name) {
    boolean found = false;
    boolean thatsit; // the currently checked editor is the right one.
    if (name == null)
      return;
//    Editor editor = Animal.get().getEditor(name, false);// ///////////////////////////////////////////////////////////
    Editor editor = getEditor(name, false);// ///////////////////////////////////////////////////////////

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

    if (object instanceof JButton) {
      JButton button = (JButton)object;
      Editor editor = getEditor(button.getActionCommand(), false);
      // check all buttons whether their ActionCommand fits. If so,
      // select this button and leave the loop
      button.setSelected(true);
   
      editor.createObject();
      drawingWindow.getDrawCanvas().setGraphicEditor((GraphicEditor) editor);
    } else if (object instanceof JMenuItem) {
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
