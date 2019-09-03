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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import javax.swing.tree.TreeSelectionModel;

import animal.vhdl.analyzer.VHDLPropertiesImporter;

public class VHDLPropertiesWindows extends JFrame implements
    TreeSelectionListener, ActionListener {
  /**
   * @author Lu,Zheng
   */
  private static final long    serialVersionUID = 1L;

  private JTree                VHDLTree;

  JFrame                       myFrame;
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
  private JButton              okButton;
  private JButton              saveAsButton;
  private JButton              reserButton;
  private JButton              cancelButton;
  private JButton              saveButton;
  private JPanel               southPanel;

  private void setupGUI() {
    VHDLTree.addTreeSelectionListener(this);
    cp = myFrame.getContentPane();
    okButton = new JButton("OK");
    okButton.setMnemonic('O');
    okButton.addActionListener(this);
    saveAsButton = new JButton("Save As Defaults");
    saveAsButton.setMnemonic('S');
    saveAsButton.addActionListener(this);
    reserButton = new JButton("Reset to Defaults");
    reserButton.setMnemonic('R');
    reserButton.addActionListener(this);
    cancelButton = new JButton("Cancel");
    cancelButton.setMnemonic('C');
    cancelButton.addActionListener(this);

    JSplitPane mainPanel = getMainPanel();
    southPanel = new JPanel();
    southPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    southPanel.add(reserButton);
    southPanel.add(saveAsButton);
    southPanel.add(cancelButton);
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

  public VHDLPropertiesWindows() {
    myFrame = new JFrame("VHDL Properties");
    cp = myFrame.getContentPane();

    // cp.setLayout(new BorderLayout());
    init();
    setupGUI();
  }

  private void init() {
    config = VHDLPropertiesImporter.PropertiesImporter();
    setTreeInfo();
    VHDLTree.setAutoscrolls(true);
    getTreeSelectionModel().setSelectionMode(
        TreeSelectionModel.SINGLE_TREE_SELECTION);//

    bulidRightPaneGUI();
  }

  private void setTreeInfo() {
    ArrayList<String> treeArchitektur = readTreeArchitecture();
    DefaultTreeModel dtm;
    DefaultMutableTreeNode rootNode = null;
    // DefaultMutableTreeNode node;
    if (treeArchitektur != null && treeArchitektur.size() != 0) {
      for (String treeInfo : treeArchitektur) {
        String[] parentchild = treeInfo.split("<-");
        if (parentchild[0].equals("/"))
          rootNode = new DefaultMutableTreeNode(parentchild[1]);
        else
          rootNode = insertToTree(rootNode, parentchild);
      }
    }
    // create DefaultTreeModel
    dtm = new DefaultTreeModel(rootNode);
    // create JTree for DefaultTreeModel
    VHDLTree = new JTree(dtm);
  }

  private DefaultMutableTreeNode insertToTree(DefaultMutableTreeNode rootNode,
      String[] parentchild) {
    if (rootNode.toString().equals(parentchild[0]))
      rootNode.add(new DefaultMutableTreeNode(parentchild[1]));
    if (rootNode.getChildCount() >= 0) {
      for (@SuppressWarnings("rawtypes")
      Enumeration e = rootNode.children(); e.hasMoreElements();) {
        DefaultMutableTreeNode n = (DefaultMutableTreeNode) e.nextElement();
        insertToTree(n, parentchild);
      }
    }
    return rootNode;

  }

  private ArrayList<String> readTreeArchitecture() {
    ArrayList<String> tree = new ArrayList<String>();
    try {
      InputStream in = new FileInputStream("animal" + File.separator + "vhdl"
          + File.separator + "VHDLArchitecture");
      if (in != null) {
        InputStreamReader isr = new InputStreamReader(in);
        BufferedReader br = new BufferedReader(isr);
        String currentLine = null;
        while ((currentLine = br.readLine()) != null)
          tree.add(currentLine);
        br.close();
        isr.close();
        in.close();
      }
    } catch (IOException ioex) {
      System.err.println(ioex.getMessage());
    }
    return tree;
  }

  private TreeSelectionModel getTreeSelectionModel() {
    return VHDLTree.getSelectionModel();
  }
//
//  public static void main(String[] args) {
//    new VHDLPropertiesWindows();
//  }

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
      String defaultColor = (String) config.get("defaultColor");
      String defaultFillColor = (String) config.get("defaultFillColor");
      String defaultHighlightColor = (String) config
          .get("defaultHighlightColor");
      String defaultHighlightFillColor = (String) config
          .get("defaultHighlightFillColor");
      String color = (String) config.get("PT" + node.toString() + ".color");
      String fillcolor = (String) config.get("PT" + node.toString()
          + ".fillColor");
      String highlightColor = (String) config.get("PT" + node.toString()
          + ".highlightColor");
      String highlightFillColor = (String) config.get("PT" + node.toString()
          + ".highlightFillColor");
      String walkSpeed = (String) config.get("PT" + node.toString()
          + ".walkSpeed");
      String isFill = (String) config.get("PT" + node.toString() + ".isFilled");
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

  private void bulidRightPaneGUI() {
    pane = new JPanel();
    label1 = new JLabel("   color", SwingConstants.CENTER);
    label2 = new JLabel("   FillColor", SwingConstants.CENTER);
    label3 = new JLabel("   isFilled", SwingConstants.CENTER);
    label4 = new JLabel("   speed", SwingConstants.CENTER);
    label5 = new JLabel("   Highlight Color", SwingConstants.CENTER);
    label6 = new JLabel("   Highlight FillColor", SwingConstants.CENTER);
    colorBox = new ColorChooserComboBox();
    fillColorBox = new ColorChooserComboBox();
    highlightColorBox = new ColorChooserComboBox();
    highlightFillColorBox = new ColorChooserComboBox();
    isFilledBox = new JCheckBox();
    speed = new JTextField("100");
    saveButton = new JButton("Save");
    saveButton.setMnemonic('S');
    saveButton.addActionListener(this);
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
    constraints.fill = fill;
    constraints.anchor = anchor;
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equalsIgnoreCase("save as defaults")) {
      int response;
      response = JOptionPane.showConfirmDialog(null,
          "This operation will save ALL properties to their default vaules!!",
          "Reset properties", JOptionPane.YES_NO_OPTION,
          JOptionPane.WARNING_MESSAGE);
      if (response == JOptionPane.YES_OPTION) {
        FileWriter fw;
        try {
          fw = new FileWriter("animal" + File.separator + "vhdl"
              + File.separator + "VHDLDefault.properties");
          BufferedWriter out = new BufferedWriter(fw);
          if (config != null) {
            Enumeration<?> keys = config.propertyNames();
            while (keys.hasMoreElements()) {
              String key = (String) keys.nextElement();
              String value = config.getProperty(key);
              out.write(key + "=" + value
                  + System.getProperty("line.separator"));
            }
          }
          out.close();
        } catch (IOException e1) {
          e1.printStackTrace();
        }
        JOptionPane.showMessageDialog(null, "All Properties have been saved.");
      }
    }
    if (e.getActionCommand().equalsIgnoreCase("cancel"))
      System.exit(0);
    if (e.getActionCommand().equalsIgnoreCase("save")) {
      config.setProperty("PT"
          + VHDLTree.getSelectionPath().getLastPathComponent() + ".color",
          colorBox.getColorSelectedAsString());
      if (!VHDLTree.getSelectionPath().getLastPathComponent().toString()
          .equalsIgnoreCase("wire")) {
        config.setProperty(
            "PT" + VHDLTree.getSelectionPath().getLastPathComponent()
                + ".fillColor", fillColorBox.getColorSelectedAsString());
        config.setProperty("PT"
            + VHDLTree.getSelectionPath().getLastPathComponent()
            + ".highlightColor", highlightColorBox.getColorSelectedAsString());
        config.setProperty("PT"
            + VHDLTree.getSelectionPath().getLastPathComponent()
            + ".highlightFillColor", highlightFillColorBox
            .getColorSelectedAsString());
        config.setProperty("PT"
            + VHDLTree.getSelectionPath().getLastPathComponent() + ".isFilled",
            String.valueOf(isFilledBox.isSelected()));
      } else
        config.setProperty(
            "PT" + VHDLTree.getSelectionPath().getLastPathComponent()
                + ".walkSpeed", speed.getText());
      TreeNode root = (TreeNode) VHDLTree.getModel().getRoot();
      visitAllNodes(root);
    }
    if (e.getActionCommand().equalsIgnoreCase("ok")) {

      FileWriter fw;
      try {
        fw = new FileWriter("animal" + File.separator + "vhdl" + File.separator
            + "VHDL.properties");
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
      JOptionPane.showMessageDialog(null, "All properties have been saved");
      System.exit(0);
    }
    if (e.getActionCommand().equalsIgnoreCase("Reset to Defaults")) {
      int response;
      response = JOptionPane.showConfirmDialog(null,
          "This operation will reset ALL properties to their default vaules!!",
          "Reset properties", JOptionPane.YES_NO_OPTION,
          JOptionPane.WARNING_MESSAGE);
      if (response == JOptionPane.YES_OPTION) {
        BufferedReader br;
        BufferedWriter bw;
        try {
          br = new BufferedReader(new FileReader("animal" + File.separator
              + "vhdl" + File.separator + "VHDLDefault.properties"));
          bw = new BufferedWriter(new FileWriter("animal" + File.separator
              + "vhdl" + File.separator + "VHDL.properties"));
          String s;
          while (br.ready()) {
            s = br.readLine();
            bw.write(s);
            bw.newLine();
          }
          bw.close();
        } catch (FileNotFoundException e1) {
          e1.printStackTrace();
        } catch (IOException e2) {
          e2.printStackTrace();
        }
        JOptionPane
            .showMessageDialog(null, "All Properties have been reseted.");
      }
    }
  }

  @SuppressWarnings("rawtypes")
  private void visitAllNodes(TreeNode root) {

    if (root.getParent() != null
        && root.getParent().toString().equalsIgnoreCase(
            VHDLTree.getSelectionPath().getLastPathComponent().toString())) {
      config.setProperty("PT" + root + ".color", colorBox
          .getColorSelectedAsString());
      config.setProperty("PT" + root + ".fillColor", fillColorBox
          .getColorSelectedAsString());
      config.setProperty("PT" + root + ".highlightColor", highlightColorBox
          .getColorSelectedAsString());
      config.setProperty("PT" + root + ".highlightFillColor",
          highlightFillColorBox.getColorSelectedAsString());
      config.setProperty("PT" + root + ".isFilled", String.valueOf(isFilledBox
          .isSelected()));
    }
    if (root.getChildCount() >= 0) {
      for (Enumeration e = root.children(); e.hasMoreElements();) {
        TreeNode n = (TreeNode) e.nextElement();
        visitAllNodes(n);
      }
    }

  }

}
