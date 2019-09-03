package animal.vhdl.gui;

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

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class VHDLOptimizationWindows implements ActionListener {
  /**
   * @author Lu,Zheng
   */
//  private static final long serialVersionUID = 1L;

  /**
   * the elements
   */
  private static String     path;

  private JTree             VHDLTree;

  JFrame                    myFrame;
  private JPanel            pane;
  private JLabel            label1;
  private JLabel            label2;
  private JCheckBox         usedQMC;
  private Container         cp;
  private JButton           next;
  private JPanel            southPanel;

  private void setupGUI() {
    cp = myFrame.getContentPane();
    next = new JButton("Next");
    next.setMnemonic('N');
    next.addActionListener(this);

    JSplitPane mainPanel = getMainPanel();
    southPanel = new JPanel();
    southPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

    southPanel.add(next);
    cp.add(mainPanel, BorderLayout.CENTER);
    cp.add(southPanel, BorderLayout.SOUTH);
    myFrame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        myFrame.setVisible(false);
        myFrame.dispose();
      }
    });

    myFrame.pack();
    pane.setVisible(true);
    myFrame.setVisible(true);
    myFrame.setSize(800, 600);
  }

  public VHDLOptimizationWindows() {
    myFrame = new JFrame("select optimation algorithms");
    cp = myFrame.getContentPane();

    // cp.setLayout(new BorderLayout());
    init();
    setupGUI();
  }

  private void init() {
    setTreeInfo();
    VHDLTree.setAutoscrolls(true);
    buildRightPaneGUI();
  }

  private void setTreeInfo() {
    DefaultTreeModel dtm;
    DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("VHDL");

    // create DefaultTreeModel
    dtm = new DefaultTreeModel(rootNode);
    // create JTree for DefaultTreeModel
    VHDLTree = new JTree(dtm);
  }

  /**
   * @param args
   */
  public static void openWindows(String args) {
    path = args;
    new VHDLOptimizationWindows();
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

  private void buildRightPaneGUI() {
    pane = new JPanel();
    label1 = new JLabel("        please select optimal algorithm ",
        SwingConstants.CENTER);
    label2 = new JLabel("Quine McCluskey algorithm");
    usedQMC = new JCheckBox();

    GridBagLayout gl = new GridBagLayout();
    GridBagConstraints constraints = new GridBagConstraints();
    pane.setLayout(gl);
    buildConstraints(constraints, 0, 0, 2, 1, 0, 20, GridBagConstraints.NONE,
        GridBagConstraints.WEST);
    gl.setConstraints(label1, constraints);
    pane.add(label1);
    buildConstraints(constraints, 0, 1, 1, 1, 10, 80, GridBagConstraints.NONE,
        GridBagConstraints.NORTH);
    gl.setConstraints(usedQMC, constraints);
    pane.add(usedQMC);
    buildConstraints(constraints, 1, 1, 1, 1, 90, 80, GridBagConstraints.NONE,
        GridBagConstraints.NORTHWEST);
    gl.setConstraints(label2, constraints);
    pane.add(label2);

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
    if (e.getActionCommand().equalsIgnoreCase("next")) {
      VHDLUsedElementPropertiesWindows.openWindows(path, usedQMC.isSelected());
      myFrame.setVisible(false);
      myFrame.dispose();
    }

  }

//  public static void main(String[] a) {
//    VHDLOptimizationWindows.openWindows("a");
//  }

}
