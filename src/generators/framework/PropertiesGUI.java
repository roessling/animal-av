/*
 * Created on 15.04.2005 by T. Ackermann
 */
package generators.framework;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import algoanim.primitives.Graph;
import algoanim.properties.AnimationProperties;
import de.ahrgr.animal.kohnert.generatorgui.PropertyTreeNode;
import generators.framework.properties.tree.PropertiesTree;
import generators.framework.properties.tree.PropertiesTreeModel;
import generators.framework.properties.tree.PropertiesTreeNode;
import generators.framework.properties.tree.PropertiesTreePane;
import gfgaa.gui.GraphAlgController;
import gfgaa.gui.GraphScriptPanel;
import gfgaa.gui.components.SPanel;
import gfgaa.gui.others.LanguageInterface;
import gfgaa.gui.others.PanelManager;
import gfgaa.gui.parser.GraphReader;
import translator.Translator;

/**
 * The AnimationProperties GUI shows a wizard-like frame that allows the user to
 * easily create a file that contains all AnimationProperties for a specific
 * Generator.
 * 
 * @author T. Ackermann
 */
public class PropertiesGUI implements WizardGUIListener, ActionListener,
    TreeSelectionListener, KeyListener {

  /** stores the Application Name */
  private static final String   APP_NAME = "Animal Properties Builder";

  /** stores the WizardGUI */
  private WizardGUI             wiz;

  /** stores the PropertiesTreePane */
  private PropertiesTreePane    treePane;

  /** stores the PropertiesTree */
  private PropertiesTree        tree;

  /** stores the "New Folder..." Button */
  private JButton               newFolder;

  /** stores the "New Type..." Button */
  private JButton               newType;

  /** stores the "New Primitive..." Button */
  private JButton               newPrimitive;

  /** stores the "Change Name..." Button */
  private JButton               changeName;

  /** stores the "Delete" Button */
  private JButton               delete;

  /** stores the "Load from File" Button */
  private JButton               load;

  /** stores the TextField for the filename */
  private JTextField            txtFileName;

  /** stores the NewPropertyTypeDialog */
  private NewPropertyTypeDialog newProp;

  /** stores the NewPrimitiveDialog */
  private NewPrimitiveDialog    newPrim;

  /** stores the PropertiesPanel for step 1 */
  private PropertiesPanel       propPanel;

  private Translator            translator;

  private File                  xmlFile;

  public File getXmlFile() {
    return xmlFile;
  }

  // Madieha
  public static GraphAlgController mainclass;

  /**
   * main is the main-Method.
   * 
   * @param args
   *          The Shell-Arguments.
   */
  public static void main(String[] args) {
    // show the dialog
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        new PropertiesGUI();
        // Madieha
        start();
        createGraphScriptPanel();
      }
    });
  }

  /**
   * Constructor creates a new PropertiesGUI-Object.
   */
  public PropertiesGUI() {
    this.wiz = new WizardGUI(3);
    this.wiz.getWizardFrame().setTitle(APP_NAME);
    this.wiz.setHelpString(0,
    // translator.translateMessage("infoNrFound"));
        "<html>Welcome to the \"<B>AnimationProperties "
            + "Builder</B>\".<br />This wizard will help you to set up all "
            + "the AnimationProperties that your Generator needs." + "</html>");

    this.wiz.setHelpString(1, "<html>Please change the <B>values</B> of "
        + "the AnimationProperties and<br />chose which values can be edited "
        + "by the users.</html>");
    this.wiz.setHelpString(2, "<html>Please <B>enter a filename</B><br />"
        + "(or browse for a file by "
        + "clicking the \"<B>Browse...</B>\" Button).<br />Then press \""
        + "<B>Finish</B>\" to write the file.</html>");

    this.tree = new PropertiesTree();

    // create the contents for the Wizard
    for (int i = 0; i < 3; i++)
      this.wiz.setContent(i, createPanelForStep(i));

    // update display of the tree in step 0
    this.tree.setBuildMode();
    this.treePane.reInsertTree();
    this.tree.setSelectionRow(0);

    this.wiz.setListener(this);
    this.newProp = new NewPropertyTypeDialog(this.wiz.getWizardFrame());
    this.newPrim = new NewPrimitiveDialog(this.wiz.getWizardFrame());
    translator = new Translator("GeneratorMsg", Locale.GERMANY);
    wiz.setTranslator(translator);

    // show the Wizard
    this.wiz.displayWizard();
  }

  /**
   * createPanelForStep creates the Panel for the given Step Number.
   * 
   * @param numberOfStep
   *          The Step Number.
   * @return The Panel for the given Step Number.
   */
  private JComponent createPanelForStep(int numberOfStep) {
    // only allow steps 0..1
    if (numberOfStep < 0 || numberOfStep > 2)
      return null;

    switch (numberOfStep) {
      case 0:
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout(8, 8));
        this.treePane = new PropertiesTreePane(this.tree);
        p.add(this.treePane, BorderLayout.CENTER);
        this.tree.setSelectionRow(0);

        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.PAGE_AXIS));
        right.setPreferredSize(new Dimension(200, 250));
        p.add(right, BorderLayout.EAST);

        this.newFolder = new JButton("New Folder...");
        this.newFolder.setActionCommand("newFolder");
        this.newFolder.addActionListener(this);
        this.newFolder.setMaximumSize(new Dimension(200, 30));
        right.add(this.newFolder);
        right.add(Box.createRigidArea(new Dimension(0, 10)));

        this.newType = new JButton("New Properties...");
        this.newType.setActionCommand("newProperties");
        this.newType.addActionListener(this);
        this.newType.setMaximumSize(new Dimension(200, 30));
        right.add(this.newType);
        right.add(Box.createRigidArea(new Dimension(0, 10)));

        this.newPrimitive = new JButton("New Primitive...");
        this.newPrimitive.setActionCommand("newPrimitive");
        this.newPrimitive.addActionListener(this);
        this.newPrimitive.setMaximumSize(new Dimension(200, 30));
        right.add(this.newPrimitive);
        right.add(Box.createRigidArea(new Dimension(0, 10)));

        this.changeName = new JButton("Change Name...");
        this.changeName.setActionCommand("changeName");
        this.changeName.addActionListener(this);
        this.changeName.setMaximumSize(new Dimension(200, 30));
        right.add(this.changeName);
        right.add(Box.createRigidArea(new Dimension(0, 10)));

        this.delete = new JButton("Delete");
        this.delete.setActionCommand("delete");
        this.delete.addActionListener(this);
        this.delete.setMaximumSize(new Dimension(200, 30));
        right.add(this.delete);
        right.add(Box.createRigidArea(new Dimension(0, 25)));

        this.load = new JButton("Load from File...");
        this.load.setActionCommand("load");
        this.load.addActionListener(this);
        this.load.setMaximumSize(new Dimension(200, 30));
        right.add(this.load);
        this.tree.addTreeSelectionListener(this);
        return p;
      case 1:
        this.propPanel = new PropertiesPanel(this.tree);
        return this.propPanel;
      case 2:
        // enter a filename and generate!
        JPanel p2 = new JPanel();
        p2.setLayout(new BoxLayout(p2, BoxLayout.PAGE_AXIS));

        JPanel panelTextField = new JPanel();
        panelTextField.setLayout(new BorderLayout(8, 8));
        panelTextField.add(new JLabel("Filename:"), BorderLayout.LINE_START);

        this.txtFileName = new JTextField();
        panelTextField.add(this.txtFileName, BorderLayout.CENTER);
        this.txtFileName.addKeyListener(this);

        int txtHeight = this.txtFileName.getPreferredSize().height;
        panelTextField.setPreferredSize(new Dimension(300, txtHeight + 5));
        panelTextField.setMaximumSize(new Dimension(600, txtHeight + 5));
        p2.add(panelTextField);
        p2.add(Box.createRigidArea(new Dimension(0, 16)));

        JPanel panelButton = new JPanel();
        panelButton.setLayout(new BorderLayout(8, 8));
        JButton but = new JButton("Browse...");
        but.setActionCommand("browse");
        but.addActionListener(this);
        panelButton.add(but, BorderLayout.LINE_END);

        int butHeight = but.getPreferredSize().height;
        but.setPreferredSize(new Dimension(200, butHeight + 5));
        panelButton.setPreferredSize(new Dimension(300, butHeight + 5));
        panelButton.setMaximumSize(new Dimension(600, butHeight + 5));
        p2.add(panelButton);
        return p2;
    }
    return null;
  }

  /**
   * (non-Javadoc)
   * 
   * @see generators.framework.WizardGUIListener#nextPressed()
   */
  public boolean nextPressed() {

    if (this.wiz.getCurrentStep() == 0) {
      this.tree.setWorkingMode();
      this.propPanel.reInsertTree();
      this.tree.expandAllFolders();
    }

    if (this.wiz.getCurrentStep() == 1) {
      this.propPanel.updateCurrentPropertyValues();
    }

    if (this.wiz.getCurrentStep() == 2) {
      // write output to the selected file
      File selFile = new File(this.txtFileName.getText());
      if (selFile.exists()) {
        if (!selFile.isFile()) {
          JOptionPane.showMessageDialog(this.wiz.getWizardFrame(),
              selFile.getPath() + "\nis not a valid name "
                  + "for a file. Please choose another name!", APP_NAME,
              JOptionPane.ERROR_MESSAGE);
          return false;
        }

        int ret = JOptionPane.showConfirmDialog(this.wiz.getWizardFrame(),
            selFile.getPath() + "\nalready exists. Are you "
                + "sure that you want to overwrite it?", APP_NAME,
            JOptionPane.YES_NO_OPTION);
        if (ret == JOptionPane.NO_OPTION)
          return false;
      }

      FileWriter out;
      BufferedWriter buf = null;
      try {
        out = new FileWriter(selFile);
        buf = new BufferedWriter(out);
        buf.write(((PropertiesTreeModel) this.tree.getModel()).getAsXML());
        buf.flush();
        buf.close();

        // int choice = JOptionPane.showConfirmDialog(
        // this.wiz.getWizardFrame(),
        // "The file has successfully been written. Do you "
        // + "want to exit " + APP_NAME + "?", APP_NAME,
        // JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        // if (choice == JOptionPane.YES_OPTION)
        xmlFile = selFile;
        this.wiz.getWizardFrame().dispose();

      } catch (IOException e1) {
        JOptionPane.showMessageDialog(this.wiz.getWizardFrame(),
            "An error occured while writing to " + selFile.getPath()
                + ". Please try to choose" + " another name!", APP_NAME,
            JOptionPane.ERROR_MESSAGE);
        return false;
      } finally {
        try {
          if (buf != null)
            buf.close();
        } catch (Exception e2) {
          // do nothing
        }
      }
    }

    return true;
  }

  /**
   * updateButtons enables and disables the buttons at the right of the list.
   */
  private void updateButtons() {
    boolean somethingSelected = (this.tree.getSelectionPath() != null
        && this.tree.getSelectionRows() != null && this.tree.getSelectionRows()[0] != 0);

    this.changeName.setEnabled(somethingSelected);
    this.delete.setEnabled(somethingSelected);

    if (this.wiz.btnNext != null)
      this.wiz.btnNext.setEnabled(((PropertiesTreeModel) this.tree.getModel())
          .getElementsCount() > 0);
  }

  /**
   * (non-Javadoc)
   * 
   * @see generators.framework.WizardGUIListener#backPressed()
   */
  public boolean backPressed() {
    if (this.wiz.getCurrentStep() == 1) {
      this.tree.setBuildMode();
      this.treePane.reInsertTree();
      this.tree.expandAllFolders();
    }
    return true;
  }

  /**
   * (non-Javadoc)
   * 
   * @see generators.framework.WizardGUIListener#afterShowStep(int)
   */
  public void afterShowStep(int index) {
    if (index == 0) {
      this.wiz.btnNext.setEnabled(((PropertiesTreeModel) this.tree.getModel())
          .getElementsCount() > 0);
      updateButtons();
    }
    if (index == 1) {
      updateButtons();
      this.tree.setSelectionRow(0);
    }

    if (index == 2) {
      this.wiz.btnNext.setEnabled((this.txtFileName.getText().length() > 0));
    }
  }

  /**
   * (non-Javadoc)
   * 
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  public void actionPerformed(ActionEvent e) {
    if (e == null)
      return;

    if (e.getActionCommand().equalsIgnoreCase("newFolder")) {
      if (this.tree.getSelectionPath() == null)
        this.tree.setSelectionRow(0);

      PropertiesTreeNode m = (PropertiesTreeNode) this.tree.getSelectionPath()
          .getLastPathComponent();
      while (m != null && !m.isFolder())
        m = (PropertiesTreeNode) m.getParent();
      if (m == null)
        return;

      String name = JOptionPane
          .showInputDialog("Please enter a label for the folder.");
      if (name == null || name.trim().length() == 0)
        return;

      PropertiesTreeNode folder = new PropertiesTreeNode(name);
      PropertiesTreeModel model = ((PropertiesTreeModel) this.tree.getModel());
      model.insertNodeInto(folder, m, m.getChildCount());

      this.tree.expandAllFolders();
      return;
    }

    if (e.getActionCommand().equalsIgnoreCase("newProperties")) {
      if (this.tree.getSelectionPath() == null)
        this.tree.setSelectionRow(0);

      PropertiesTreeNode m = (PropertiesTreeNode) this.tree.getSelectionPath()
          .getLastPathComponent();
      while (m != null && !m.isFolder())
        m = (PropertiesTreeNode) m.getParent();
      if (m == null)
        return;

      if (!this.newProp.showDialog())
        return;
      if (this.newProp.getName().trim().length() == 0)
        return;
      AnimationProperties prop = createPropertiesByName(this.newProp
          .getAnimationType());
      if (prop == null)
        return;
      String newName = getNextValidPropertiesName(this.newProp.getName(), null);
      prop.set("name", newName);

      PropertiesTreeModel model = ((PropertiesTreeModel) this.tree.getModel());
      model.insertNodeInto(model.getNewNode(prop), m, m.getChildCount());

      this.tree.expandAllFolders();
      updateButtons();
      return;
    }

    if (e.getActionCommand().equalsIgnoreCase("newPrimitive")) {
      if (this.tree.getSelectionPath() == null)
        this.tree.setSelectionRow(0);

      PropertiesTreeNode m = (PropertiesTreeNode) this.tree.getSelectionPath()
          .getLastPathComponent();
      while (m != null && !m.isFolder())
        m = (PropertiesTreeNode) m.getParent();
      if (m == null)
        return;

      if (!this.newPrim.showDialog())
        return;
      if (this.newPrim.getName().trim().length() == 0)
        return;
      Object o = createPrimitiveByName(this.newPrim.getAnimationType());
      if (o == null)
        return;
      String newName = getNextValidPrimitiveName(this.newPrim.getName(), null);

      PropertiesTreeNode primitive = new PropertiesTreeNode(newName, o);
      PropertiesTreeModel model = ((PropertiesTreeModel) this.tree.getModel());
      model.insertNodeInto(primitive, m, m.getChildCount());

      this.tree.expandAllFolders();
      updateButtons();
      return;
    }

    if (e.getActionCommand().equalsIgnoreCase("changeName")) {
      if (this.tree.getSelectionPath() == null)
        return;
      PropertiesTreeNode n = (PropertiesTreeNode) this.tree.getSelectionPath()
          .getLastPathComponent();

      if (n.equals(this.tree.getModel().getRoot()))
        return;
      if (n.isItem())
        return;

      if (n.isFolder()) {
        String newName = JOptionPane.showInputDialog(
            "Please enter a new name for the folder.", n.getLabel());
        if (newName == null || newName.trim().length() == 0)
          return;
        n.setLabel(newName);
      }

      if (n.isProperty()) {
        AnimationProperties ap = n.getAnimationProperties();
        String newName = JOptionPane.showInputDialog(
            "Please enter a new name for the Properties.", ap.get("name"));
        if (newName == null || newName.trim().length() == 0)
          return;
        newName = getNextValidPropertiesName(newName, ap);
        ap.set("name", newName);
      }

      if (n.isPrimitive()) {
        String newName = JOptionPane.showInputDialog(
            "Please enter a new name for the Primitive.", n.getName());
        if (newName == null || newName.trim().length() == 0)
          return;
        newName = getNextValidPrimitiveName(newName, n.getName());
        n.setName(newName);
      }

      ((PropertiesTreeModel) this.tree.getModel()).nodeChanged(n);
      return;
    }

    if (e.getActionCommand().equalsIgnoreCase("delete")) {
      if (this.tree.getSelectionPath() == null)
        return;
      MutableTreeNode n = (MutableTreeNode) this.tree.getSelectionPath()
          .getLastPathComponent();

      if (n.equals(this.tree.getModel().getRoot()))
        return;
      int selRow = this.tree.getSelectionRows()[0];

      ((PropertiesTreeModel) this.tree.getModel()).removeNodeFromParent(n);
      this.tree.setSelectionRow(selRow - 1);

      updateButtons();
      return;
    }

    if (e.getActionCommand().equalsIgnoreCase("load")) {
      // try to get path to generators
      String path = getDefaultPath();
      JFileChooser fc;
      if (path == null)
        fc = new JFileChooser();
      else
        fc = new JFileChooser(path);

      fc.addChoosableFileFilter(new CustomFileFilter("xml"));
      // fc.addChoosableFileFilter(new CustomFileFilter("ptm"));

      int result = fc.showOpenDialog(this.wiz.getWizardFrame());
      if (result != JFileChooser.APPROVE_OPTION)
        return;

      File selFile = fc.getSelectedFile();
      try {
        String strFile = selFile.getCanonicalPath();
        ((PropertiesTreeModel) this.tree.getModel()).loadFromXMLFile(strFile,
            false);
      } catch (Exception e1) {
        System.err.println("Error while loading " + selFile.getName() + "\n"
            + e1.getLocalizedMessage());
        ((PropertiesTreeModel) this.tree.getModel()).clear();
      }

      xmlFile = selFile;

      // update views and buttons
      this.tree.expandAllFolders();

      if (this.tree.getSelectionPath() == null
          && ((PropertiesTreeModel) this.tree.getModel()).getElementsCount() > 0)
        this.tree.setSelectionRow(0);

      updateButtons();
      return;
    }

    if (e.getActionCommand().equalsIgnoreCase("browse")) {
      // show the FileSave Dialog

      JFileChooser fc;

      if (this.txtFileName.getText().length() > 0) {
        // use the selected directory
        File start = new File(this.txtFileName.getText());
        fc = new JFileChooser(start.getParent());
      } else {
        // try to get path to generators
        String path = getDefaultPath();
        if (path == null)
          fc = new JFileChooser();
        else
          fc = new JFileChooser(path);
      }

      fc.addChoosableFileFilter(new CustomFileFilter("xml"));

      int result = fc.showSaveDialog(this.wiz.getWizardFrame());
      if (result != JFileChooser.APPROVE_OPTION)
        return;

      File selFile = fc.getSelectedFile();
      try {
        String strFile = selFile.getCanonicalPath();

        // here we add the extension,if no extension has been given...
        if (selFile.getName().indexOf(".") == -1) {
          strFile = strFile.concat(".xml");
        }

        this.txtFileName.setText(strFile);
        this.wiz.btnNext.setEnabled((this.txtFileName.getText().length() > 0));
      } catch (IOException e1) {
        // do nothing if exception is thrown
      }
      return;
    }
  }

  /**
   * valueChanged is called when the selection in the tree changes.
   * 
   * @param e
   *          The Event.
   */
  public void valueChanged(TreeSelectionEvent e) {
    if (e == null)
      return;
    updateButtons();
  }

  /**
   * getNextValidPropertiesName returns the next free name for the given base
   * name. For example if "circle" already exists then "circle2", "circle3", ...
   * are returned.
   * 
   * @param base
   *          The base name.
   * @param current
   *          The currently selected item in the list.
   * @return The next free name for the given base name.
   */
  private String getNextValidPropertiesName(String base,
      AnimationProperties current) {
    long suffix = 2;
    boolean found;
    String newName = base;

    AnimationProperties obj;
    Iterator<AnimationProperties> iterTypes;

    while (true) {
      iterTypes = ((PropertiesTreeModel) tree.getModel())
          .getPropertiesContainer().iterator();
      found = false;
      while (iterTypes.hasNext()) {
        obj = iterTypes.next();
        if (obj == current)
          continue;
        if (newName.equals(obj.get("name"))) {
          found = true;
          break;
        }
      }
      if (!found)
        return newName;
      newName = base + Long.toString(suffix++);
    }
  }

  /**
   * getNextValidPrimitiveName returns the next free name for the given base
   * name. For example if "int" already exists then "int2", "int3", ... are
   * returned.
   * 
   * @param base
   *          The base name.
   * @param currentName
   *          The currently used name.
   * @return The next free name for the given base name.
   */
  private String getNextValidPrimitiveName(String base, String currentName) {
    long suffix = 2;
    boolean found;
    String newName = base;

    String eName;
    Enumeration<String> eNames;

    while (true) {
      eNames = ((PropertiesTreeModel) tree.getModel()).getPrimitivesContainer()
          .keys();
      found = false;
      while (eNames.hasMoreElements()) {
        eName = eNames.nextElement();
        if (eName == currentName)
          continue;
        if (newName.equals(eName)) {
          found = true;
          break;
        }
      }
      if (!found)
        return newName;
      newName = base + Long.toString(suffix++);
    }
  }

  /**
   * createPropertiesByName creates a AnimationProperties-Object of the Class
   * specified by the String className.
   * 
   * @param className
   *          The name of the new Objects Class.
   * @return An Object of the Class specified by the String className.
   */
  @SuppressWarnings("unchecked")
  private static AnimationProperties createPropertiesByName(String className) {
    AnimationProperties object = null;
    String pack = AnimationProperties.class.getPackage().getName();

    try {
      Class<AnimationProperties> propClass = (Class<AnimationProperties>) Class
          .forName(pack + "." + className);
      object = propClass.newInstance();
      return object;
    } catch (InstantiationException e) {
      // maybe the Class is abstract
      return null;
    } catch (IllegalAccessException e) {
      // we are not allowed to access the Class
      return null;
    } catch (ClassNotFoundException e) {
      // the Class does not exist
      return null;
    }
  }

  /**
   * createPrimitiveByName creates a Primitive-Object of the Class specified by
   * the String className.
   * 
   * @param className
   *          The type for the new Primitive.
   * @return A Primitive-Object.
   */
  private static Object createPrimitiveByName(String className) {
    if (className.equals("String"))
      return "";
    if (className.equals("int"))
      return Integer.valueOf(2);
    if (className.equals("boolean"))
      return Boolean.TRUE;
    if (className.equals("double"))
      return new Double(2.0);
    if (className.equals("int[]")) {
      int[] i = { 1, 2, 3, 4 };
      return i;
    }
    if (className.equals("int[][]")) {
      int[][] i = new int[][] { { 1, 2, 3, 4 }, { 5, 6, 7, 8 } };
      return i;
    }
    if (className.equals("String[]")) {
      String[] i = { "A", "B", "C", "D" };
      return i;
    }
    if (className.equals("String[][]")) {
      String[][] i = new String[][] { { "A", "B" }, { "C", "D" } };
      return i;
    }

    if (className.equals("Color"))
      return Color.GREEN;
    if (className.equals("Font"))
      return new Font("SansSerif", Font.PLAIN, 12);
    // Madieha
    if (className.equals("Graph")) {
      if (mainclass == null)
        PropertiesGUI.start();
      SPanel p = mainclass.getPanel(PanelManager.PANEL_GRAPHSCRIPT);
      if (p == null) {
        PropertiesGUI.createGraphScriptPanel();
        p = mainclass.getPanel(PanelManager.PANEL_GRAPHSCRIPT);
      }
      return p;
    }
    return null;
  }

  // Madieha

  public static void start() {
    final int langFlag = LanguageInterface.LANGUAGE_ENGLISH;
    mainclass = new GraphAlgController(langFlag);
  }

  public static GraphScriptPanel createGraphScriptPanel() {
    return new GraphScriptPanel(mainclass);
  }

  /**
   * getDefaultPath gets the path of the generators directory. If we are in a
   * JAR file, the null s returned.
   * 
   * @return The path to generators or null.
   */
  private String getDefaultPath() {
    if (this.getClass().getResource("../generators") == null)
      return null;

    // replace whitespaces...
    return this.getClass().getResource("../generators").getPath().substring(1)
        .replaceAll("%20", " ");
  }

  /**
   * (non-Javadoc)
   * 
   * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
   */
  public void keyTyped(KeyEvent e) {
    if (e == null)
      return;

    final JTextField txt = this.txtFileName;
    final AbstractButton but = this.wiz.btnNext;

    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        but.setEnabled((txt.getText().length() > 0));
      }
    });
  }

  /**
   * (non-Javadoc)
   * 
   * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
   */
  public void keyPressed(KeyEvent e) {
    if (e == null)
      return;
  }

  /**
   * (non-Javadoc)
   * 
   * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
   */
  public void keyReleased(KeyEvent e) {
    if (e == null)
      return;
  }

  public static Graph getGraphFromScriptFile() {
    if (PropertiesGUI.mainclass == null)
      return null;
    GraphScriptPanel panel = (GraphScriptPanel) PropertiesGUI.mainclass
        .getPanel(PanelManager.PANEL_GRAPHSCRIPT);
    if (panel == null || panel.input == null)
      return null;
    // System.err.println(PropertiesGUI.mainclass);
    String string1 = ((GraphScriptPanel) PropertiesGUI.mainclass
        .getPanel(PanelManager.PANEL_GRAPHSCRIPT)).input.getText();
    // Writer fw = null;
    Graph graph = null;

    // try {
    // fw = new FileWriter("fileWriter.txt");
    // fw.write(string1);
    // } catch (IOException e1) {
    // System.err.println("Konnte Datei nicht erstellen");
    // } finally {
    // if (fw != null)
    // try {
    // fw.close();
    // } catch (IOException e1) {
    // }
    // }
    // Madieha
    /*
     * GraphReader gr = new GraphReader("fileWriter.txt"); graph =
     * gr.readFile();
     */
    GraphReader gr = new GraphReader("no file");
    graph = gr.readGraph(string1, false);
    return graph;
  }

  public HashMap<String, String> getPrimitivesContainer() {
    HashMap<String, String> elements = new HashMap<String, String>();

    Enumeration<TreeNode> e = ((PropertiesTreeNode) tree.getModel()
        .getRoot()).depthFirstEnumeration();
    while (e.hasMoreElements()) {
      TreePath t = new TreePath(((PropertiesTreeNode)e.nextElement()).getPath());
      PropertiesTreeNode n = (PropertiesTreeNode) t.getLastPathComponent();
      if (n.isPrimitive()) {
        Object o = n.getValue();
        String type = "String";

        if (o instanceof Integer) {
          type = "Integer";
        } else if (o instanceof int[]) {
          type = "int[]";
        } else if (o instanceof int[][]) {
          type = "int[][]";
        } else if (o instanceof Color) {
          type = "Color";
        } else if (o instanceof String[]) {
          type = "String[]";
        } else if (o instanceof String[][]) {
          type = "String[][]";
        } else if (o instanceof Boolean) {
          type = "Boolean";
        } else if (o instanceof Double) {
          type = "double";
        } else if (o instanceof Font) {
          type = "Font";
        } else if (o instanceof Graph) {
          type = "Graph";
        }
        String name = n.getName();
        elements.put(name, type);
      } else if (n.isProperty()) {
        AnimationProperties props = (AnimationProperties) n
            .getAnimationProperties();
        String name = props.get("name").toString();
        elements.put(name, props.getClass().getName());
      }

      // idea: elements.put("element name", "element type, e.g., "int[]")
      // primitives.put(n.getName(), n.getValue());
      // else if (n.isProperty())
      // properties.put(n.getName(), n.getValue());
    }
    return elements;
  }

  public WizardGUI getWizardGUI() {
    return wiz;
  }

}
