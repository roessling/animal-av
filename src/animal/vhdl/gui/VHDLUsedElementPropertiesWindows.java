package animal.vhdl.gui;

import generators.framework.components.ColorChooserComboBox;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import animal.exchange.AnimationImporter;
import animal.misc.ColorChoice;
import animal.vhdl.analyzer.VHDLAnalyzer;
import animal.vhdl.analyzer.VHDLPropertiesImporter;
import animal.vhdl.graphics.PTVHDLElement;
import animal.vhdl.vhdlscript.ScriptGenerator;


public class VHDLUsedElementPropertiesWindows implements ActionListener,
    TreeSelectionListener {
  /**
   * @author Lu,Zheng
   */
//  private static final long               serialVersionUID = 1L;

  private static ArrayList<PTVHDLElement> elements;

  /**
   * @return the elements
   */
  public static ArrayList<PTVHDLElement> getElements() {
    return elements;
  }

  private static String        path;

  private JTree                VHDLTree;

  JFrame               myFrame;
  private JPanel               pane;
  private JLabel               label1;
  private JLabel               label2;
  private JLabel               label3;
  private JLabel               label4;
  private JLabel               label5;
  private JLabel               label6;
  private ColorChooserComboBox colorBox;
  private ColorChooserComboBox fillColorBox;
  private ColorChooserComboBox highlightColorBox;
  private ColorChooserComboBox highlightFillColorBox;
  private JCheckBox            isFilledBox;
  private JTextField           speed;
  private Container            cp;
  private Properties           config;
  private JButton              saveAsButton;
  private JButton              okButton;
  private JButton              saveButton;
  private JPanel               southPanel;

  private void setupGUI() {
    VHDLTree.addTreeSelectionListener(this);
    cp = myFrame.getContentPane();
    okButton = new JButton("Confirm");
    okButton.setMnemonic('C');
    okButton.addActionListener(this);

    JSplitPane mainPanel = getMainPanel();
    southPanel = new JPanel();
    southPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

    southPanel.add(okButton);
    cp.add(mainPanel, BorderLayout.CENTER);
    cp.add(southPanel, BorderLayout.SOUTH);
    myFrame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        myFrame.setVisible(false);
        myFrame.dispose();
      }
    });

    myFrame.pack();
    pane.setVisible(false);
    myFrame.setVisible(true);
    myFrame.setSize(800, 600);
  }

  public VHDLUsedElementPropertiesWindows() {
    myFrame = new JFrame("VHDL Properties");
    cp = myFrame.getContentPane();

    // cp.setLayout(new BorderLayout());
    init();
    setupGUI();
  }

  private void init() {
    config = VHDLPropertiesImporter.PropertiesImporter();
    setTreeInfo(elements);
    VHDLTree.setAutoscrolls(true);
    buildRightPaneGUI();
  }

  private void setTreeInfo(ArrayList<PTVHDLElement> elements) {
    DefaultTreeModel dtm;
    DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("VHDL");
    for (PTVHDLElement ele : elements) {
      String className = ele.getClass().toString().substring(
          ele.getClass().toString().lastIndexOf(".") + 1);
      if (ele.getEntityType() == 2)
        rootNode.add(new DefaultMutableTreeNode(ele.getObjectName() + " ("
            + className + ")"));
    }
    // create DefaultTreeModel
    dtm = new DefaultTreeModel(rootNode);
    // create JTree for DefaultTreeModel
    VHDLTree = new JTree(dtm);
  }

  /**
   * @param args
   */
  public static void openWindows(String args, boolean usedQuineMcCluskey) {
    path = args;
    VHDLAnalyzer test = new VHDLAnalyzer();
    test.setUsedQuineMcCluskey(usedQuineMcCluskey);
    test.importVHDLFrom(args);
    elements = VHDLAnalyzer.getElements();

    new VHDLUsedElementPropertiesWindows();
  }

  private JSplitPane getMainPanel() {
    // create a new JPanel with BorderLayout
    Dimension minSize = new Dimension(200, 288);
    // add the scroll pane on the generator tree to West
    JScrollPane treeScrollPane = new JScrollPane(VHDLTree);
    treeScrollPane.setMinimumSize(minSize);
    // editorPane.show();
    JScrollPane paneScrollPane = new JScrollPane(pane);
    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, false,
        treeScrollPane, paneScrollPane);
    // splitPane.setLayout(new FlowLayout());
    splitPane.setDividerLocation(-2);
    // editorPane.setMinimumSize(minSize);
    splitPane.setResizeWeight(0.2);
    return splitPane;
  }

  public void valueChanged(TreeSelectionEvent e) {
    TreePath path = e.getPath();
    TreeNode node = (TreeNode) path.getLastPathComponent();
    if (node.toString().equalsIgnoreCase("VHDL")) {
      pane.setVisible(false);
    } else {
      pane.setVisible(true);
      @SuppressWarnings("unused")
      String name = "";
      name = node.toString().substring(node.toString().lastIndexOf("(") + 1,
          node.toString().lastIndexOf(")"));
      String defaultColor = (String) config.get("defaultColor");
      String defaultFillColor = (String) config.get("defaultFillColor");
      String defaultHighlightColor = (String) config
          .get("defaultHighlightColor");
      String defaultHighlightFillColor = (String) config
          .get("defaultHighlightFillColor");
      // String color=(String)config.get(name+".color");
      // String fillcolor=(String)config.get(name+".fillColor");
      // String highlightColor=(String)config.get(name+".highlightColor");
      // String
      // highlightFillColor=(String)config.get(name+".highlightFillColor");
      // String walkSpeed=(String)config.get(name+".walkSpeed");
      // String isFill=(String)config.get(name+".isFilled");
      String color = null;
      String fillcolor = null;
      String highlightColor = null;
      String highlightFillColor = null;
      String walkSpeed = null;
      String isFill = null;
      for (PTVHDLElement ele : elements) {
        if (ele.getObjectName().equalsIgnoreCase(
            VHDLTree.getSelectionPath().getLastPathComponent().toString()
                .substring(
                    0,
                    VHDLTree.getSelectionPath().getLastPathComponent()
                        .toString().lastIndexOf("(") - 1))) {
          color = ColorChoice.getColorName(ele.getDefaultColor());
          fillcolor = ColorChoice.getColorName(ele.getDefaultFillColor());
          highlightColor = ColorChoice.getColorName(ele.getColor());
          highlightFillColor = ColorChoice.getColorName(ele.getFillColor());
          isFill = String.valueOf(ele.isFilled());
        }
      }
      if (color != null)
        colorBox.setColorSelected(color);
      else
        colorBox.setColorSelected(defaultColor);
      if (fillcolor != null)
        fillColorBox.setColorSelected(fillcolor);
      else
        fillColorBox.setColorSelected(defaultFillColor);
      if (highlightColor != null)
        highlightColorBox.setColorSelected(highlightColor);
      else
        highlightColorBox.setColorSelected(defaultHighlightColor);
      if (highlightFillColor != null)
        highlightFillColorBox.setColorSelected(highlightFillColor);
      else
        highlightFillColorBox.setColorSelected(defaultHighlightFillColor);
      if (isFill != null)
        isFilledBox.setSelected(Boolean.valueOf(isFill));
      else
        isFilledBox.setSelected(false);

      if (!node.toString().equalsIgnoreCase("wire")) {
        label2.setVisible(true);
        fillColorBox.setVisible(true);
        label3.setVisible(true);
        isFilledBox.setVisible(true);
        highlightColorBox.setVisible(true);
        highlightFillColorBox.setVisible(true);
        label5.setVisible(true);
        label6.setVisible(true);
        label4.setVisible(false);
        speed.setVisible(false);

      } else {
        label2.setVisible(false);
        fillColorBox.setVisible(false);
        label3.setVisible(false);
        isFilledBox.setVisible(false);
        highlightColorBox.setVisible(false);
        highlightFillColorBox.setVisible(false);
        label5.setVisible(false);
        label6.setVisible(false);
        label4.setVisible(true);
        speed.setVisible(true);
        speed.setText(walkSpeed);
      }
    }
    // System.out.println(node.toString());

  }

  private void buildRightPaneGUI() {
    pane = new JPanel();
    label1 = new JLabel("    color", SwingConstants.CENTER);
    label2 = new JLabel("    FillColor", SwingConstants.CENTER);
    label3 = new JLabel("    isFilled", SwingConstants.CENTER);
    label4 = new JLabel("    speed", SwingConstants.CENTER);
    label5 = new JLabel("    Highlight Color", SwingConstants.CENTER);
    label6 = new JLabel("    Highlight FillColor", SwingConstants.CENTER);
    colorBox = new ColorChooserComboBox();
    fillColorBox = new ColorChooserComboBox();
    highlightColorBox = new ColorChooserComboBox();
    highlightFillColorBox = new ColorChooserComboBox();
    isFilledBox = new JCheckBox();
    speed = new JTextField("100");
    saveButton = new JButton("Save");
    saveButton.setMnemonic('S');
    saveButton.addActionListener(this);
    saveAsButton = new JButton("Save As Defaults");
    saveAsButton.setMnemonic('A');
    saveAsButton.addActionListener(this);
    GridBagLayout gl = new GridBagLayout();
    GridBagConstraints constraints = new GridBagConstraints();
    pane.setLayout(gl);
    buildConstraints(constraints, 0, 0, 1, 1, 10, 15, GridBagConstraints.NONE,
        GridBagConstraints.WEST);
    gl.setConstraints(label1, constraints);
    pane.add(label1);
    buildConstraints(constraints, 1, 0, 1, 1, 90, 15, GridBagConstraints.NONE,
        GridBagConstraints.WEST);
    gl.setConstraints(colorBox, constraints);
    pane.add(colorBox);
    buildConstraints(constraints, 0, 1, 1, 1, 10, 15, GridBagConstraints.NONE,
        GridBagConstraints.WEST);
    gl.setConstraints(label2, constraints);
    pane.add(label2);
    buildConstraints(constraints, 1, 1, 1, 1, 90, 15, GridBagConstraints.NONE,
        GridBagConstraints.WEST);
    gl.setConstraints(fillColorBox, constraints);
    pane.add(fillColorBox);
    buildConstraints(constraints, 0, 2, 1, 1, 10, 15, GridBagConstraints.NONE,
        GridBagConstraints.WEST);
    gl.setConstraints(label5, constraints);
    pane.add(label5);
    buildConstraints(constraints, 1, 2, 1, 1, 10, 15, GridBagConstraints.NONE,
        GridBagConstraints.WEST);
    gl.setConstraints(highlightColorBox, constraints);
    pane.add(highlightColorBox);
    buildConstraints(constraints, 0, 3, 1, 1, 10, 15, GridBagConstraints.NONE,
        GridBagConstraints.WEST);
    gl.setConstraints(label6, constraints);
    pane.add(label6);
    buildConstraints(constraints, 1, 3, 1, 1, 90, 15, GridBagConstraints.NONE,
        GridBagConstraints.WEST);
    gl.setConstraints(highlightFillColorBox, constraints);
    pane.add(highlightFillColorBox);
    buildConstraints(constraints, 0, 4, 1, 1, 90, 15, GridBagConstraints.NONE,
        GridBagConstraints.WEST);
    gl.setConstraints(label3, constraints);
    pane.add(label3);
    buildConstraints(constraints, 1, 4, 1, 1, 90, 15, GridBagConstraints.NONE,
        GridBagConstraints.WEST);
    gl.setConstraints(isFilledBox, constraints);
    pane.add(isFilledBox);
    buildConstraints(constraints, 0, 5, 1, 1, 10, 15, GridBagConstraints.NONE,
        GridBagConstraints.WEST);
    gl.setConstraints(label4, constraints);
    pane.add(label4);
    buildConstraints(constraints, 1, 5, 1, 1, 90, 15, GridBagConstraints.NONE,
        GridBagConstraints.WEST);
    gl.setConstraints(speed, constraints);
    pane.add(speed);
    buildConstraints(constraints, 0, 6, 1, 1, 90, 10, GridBagConstraints.NONE,
        GridBagConstraints.CENTER);
    gl.setConstraints(saveAsButton, constraints);
    pane.add(saveAsButton);
    buildConstraints(constraints, 1, 6, 1, 1, 90, 10, GridBagConstraints.NONE,
        GridBagConstraints.CENTER);
    gl.setConstraints(saveButton, constraints);
    pane.add(saveButton);
    // setContentPane(pane);

  }

  private void buildConstraints(GridBagConstraints constraints, int gx, int gy,
      int gw, int gh, int wx, int wy, int fill, int anchor) {
    constraints.gridx = gx;
    constraints.gridy = gy;
    constraints.gridheight = gh;
    constraints.gridwidth = gw;
    constraints.weightx = wx;
    constraints.weighty = wy;
    constraints.fill = GridBagConstraints.NONE;
    constraints.anchor = GridBagConstraints.WEST;
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equalsIgnoreCase("save")) {
      for (PTVHDLElement ele : elements) {
        if (ele.getObjectName().equalsIgnoreCase(
            VHDLTree.getSelectionPath().getLastPathComponent().toString()
                .substring(
                    0,
                    VHDLTree.getSelectionPath().getLastPathComponent()
                        .toString().indexOf("(") - 1).trim())) {
          ele.setColor(colorBox.getColorSelected());
          ele.setFillColor(fillColorBox.getColorSelected());
          ele.setDefaultColor(highlightColorBox.getColorSelected());
          ele.setDefaultFillColor(highlightFillColorBox.getColorSelected());
          ele.setFilled(isFilledBox.isSelected());
        }

      }
    }
    if (e.getActionCommand().equalsIgnoreCase("Save As Defaults")) {
      String name = VHDLTree.getSelectionPath().getLastPathComponent()
          .toString().substring(
              VHDLTree.getSelectionPath().getLastPathComponent().toString()
                  .indexOf("(") + 1,
              VHDLTree.getSelectionPath().getLastPathComponent().toString()
                  .lastIndexOf("") - 1);
      config.setProperty(name + ".color", colorBox.getColorSelectedAsString());
      config.setProperty(name + ".fillColor", fillColorBox
          .getColorSelectedAsString());
      config.setProperty(name + ".highlightColor", highlightColorBox
          .getColorSelectedAsString());
      config.setProperty(name + ".highlightFillColor", highlightFillColorBox
          .getColorSelectedAsString());
      config.setProperty(name + ".isFilled", String.valueOf(isFilledBox
          .isSelected()));

      //TODO GR WHAT-FOR?
//      System.err.println("will _NOT_ write VHDL.properties!");

      FileWriter fw;
      try {
//        fw = new FileWriter("animal" + File.separator +"vhdl" + File.separator + "VHDL.properties");
        fw = new FileWriter("VHDL.properties");
        BufferedWriter out = new BufferedWriter(fw);
        if (config != null) {
          Enumeration<?> keys = config.propertyNames();
          while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            String value = config.getProperty(key);
            out.write(key + "=" + value + System.getProperty("line.separator"));
          }
        }
        out.close();
      } catch (IOException e1) {
        e1.printStackTrace();
      }
      for (PTVHDLElement ele : elements) {
        if (ele.getObjectName().equalsIgnoreCase(
            VHDLTree.getSelectionPath().getLastPathComponent().toString()
                .substring(
                    0,
                    VHDLTree.getSelectionPath().getLastPathComponent()
                        .toString().indexOf("(") - 1).trim())) {
          ele.setColor(colorBox.getColorSelected());
          ele.setFillColor(fillColorBox.getColorSelected());
          ele.setDefaultColor(highlightColorBox.getColorSelected());
          ele.setDefaultFillColor(highlightFillColorBox.getColorSelected());
          ele.setFilled(isFilledBox.isSelected());
        }

      }
      JOptionPane
          .showMessageDialog(
              null,
              "Properties have been saved and will take effect next time the program is started");
    }
    if (e.getActionCommand().equalsIgnoreCase("confirm")) {
//      System.err.println("will _NOT_ save VHDL.properties!");

      FileWriter fw;
      try {
//        fw = new FileWriter("animal" + File.separator +"vhdl" + File.separator + "VHDL.properties");
        fw = new FileWriter("VHDL.properties");
        BufferedWriter out = new BufferedWriter(fw);
        if (config != null) {
          Enumeration<?> keys = config.propertyNames();
          while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            String value = config.getProperty(key);
            out.write(key + "=" + value + System.getProperty("line.separator"));
          }
        }
        out.close();
      } catch (IOException e1) {
        e1.printStackTrace();
      }
      // JOptionPane.showMessageDialog(null, "All properties have been saved");
 
      String asuName = ScriptGenerator.getScriptFrom(path, elements);
      AnimationImporter.importAnimation(asuName, "animation/animalscript");
      myFrame.setVisible(false);
      myFrame.dispose();
    }

  }

}
