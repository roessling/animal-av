/*
 * Created on 24.07.2005 by T. Ackermann
 */

package generators.framework.properties.tree;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.net.URL;
import java.util.Enumeration;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import algoanim.properties.AnimationProperties;
import algoanim.properties.CallMethodProperties;

/**
 * PropertiesTree is used to show a tree with AnimationProperties objects. The
 * objects can easily be arranged by dragging with the mouse.
 * 
 * @author T. Ackermann
 */
public class PropertiesTree extends JTree {

  /** we store this because PropertiesTree is serializable */
  private static final long serialVersionUID = 4048795649896298288L;

  /** stores the Tree Model */
  private PropertiesTreeModel model;

  /** is dragging allowed? */
  boolean treeDraggingAllowed = true;

  /** are we currently dragging? */
  boolean _treeDragging = false;

  /** stores the Node that is currently dragged */
  private PropertiesTreeNode _treeDraggingNode = null;

  /**
   * Constructor creates a new PropertiesTree-Object.
   */
  public PropertiesTree() {
    super(new PropertiesTreeModel());
    model = (PropertiesTreeModel) getModel();

    setRootVisible(true);
    setShowsRootHandles(true);
    setEditable(false);
    getSelectionModel().setSelectionMode(
        TreeSelectionModel.SINGLE_TREE_SELECTION);

    setCellRenderer(new PropertiesTreeRenderer());

    addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseDragged(MouseEvent e) {
        if (!treeDraggingAllowed)
          return;
        if (!_treeDragging) {
          _treeDragging = true;

          TreePath path = getPathForLocation(e.getX(), e.getY());
          PropertiesTreeNode node = null;
          if (path != null)
            if (path.getLastPathComponent() instanceof PropertiesTreeNode)
              node = (PropertiesTreeNode) path.getLastPathComponent();

          if (node != null && node != path.getPathComponent(0))
            setCursor(new Cursor(Cursor.HAND_CURSOR));
          mouseDragOnTree(e, node);
        }
      }
    });

    addMouseListener(new MouseAdapter() {
      public void mouseReleased(MouseEvent e) {
        if (!treeDraggingAllowed)
          return;
        if (_treeDragging) {
          TreePath path = getPathForLocation(e.getX(), e.getY());
          _treeDragging = false;
          setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
          PropertiesTreeNode node = null;
          if (path != null)
            if (path.getLastPathComponent() instanceof PropertiesTreeNode)
              node = (PropertiesTreeNode) path.getLastPathComponent();
          mouseDropOnTree(e, node);
        }
      }
    });

    expandAllFolders();
  }

  protected void mouseDragOnTree(MouseEvent e, PropertiesTreeNode node) {
    if (node == null || e == null || node == model.getRoot())
      return;
    _treeDraggingNode = node;
  }

  protected void mouseDropOnTree(MouseEvent e, PropertiesTreeNode target) {
    if (target == null || e == null || _treeDraggingNode == null)
      return;

    if (target.getAnimationProperties() != null && !(target.isFolder())) {
      mouseDropOnTree(e, (PropertiesTreeNode) target.getParent());
      return;
    }

    if (_treeDraggingNode == target)
      return;

    // Store to be moved nodes parent
    PropertiesTreeNode draggingTreeNodeParent = (PropertiesTreeNode) _treeDraggingNode
        .getParent();

    if (draggingTreeNodeParent != null) {

      // According Java documentation no remove before
      // insert is necessary. Insert removes a node if already
      // part of the tree. However the UI does not refresh
      // properly without remove

      model.removeNodeFromParent(_treeDraggingNode);

      // If node to be moved is a target's ancestor
      // To be moved nodes child leading to the target
      // node must be remounted and added to the to be moved
      // nodes parent

      PropertiesTreeNode prevAncestor = null;
      PropertiesTreeNode ancestor = target;
      boolean found = false;
      do {
        if (ancestor == _treeDraggingNode) {
          found = true;
          break;
        }
        prevAncestor = ancestor;
        ancestor = (PropertiesTreeNode) ancestor.getParent();
      } while (ancestor != null);
      if (found && prevAncestor != null)
        model.insertNodeInto(prevAncestor, draggingTreeNodeParent, 0);

      // Now insert to be moved node at its new location
      model.insertNodeInto(_treeDraggingNode, target, 0);

      expandAllFolders();
      setSelectionRow(0);

      _treeDraggingNode = null;
    }
  }

  /**
   * expandAll expands all Folders.
   */
  @SuppressWarnings("unchecked")
  public void expandAllFolders() {
    Enumeration<?> e = ((PropertiesTreeNode)getModel().getRoot()).depthFirstEnumeration();
    while (e.hasMoreElements()) {
      TreePath t = new TreePath(((PropertiesTreeNode)e.nextElement()).getPath());
      PropertiesTreeNode n = (PropertiesTreeNode) t.getLastPathComponent();
      if (n.isItem() || n.isPrimitive() || n.isFolder()) {
        setExpandedState(t, true);
        continue;
      }

      setExpandedState(t, !model.getShowItems());
    }
  }

  /**
   * (non-Javadoc)
   * 
   * @see javax.swing.JTree#setExpandedState(javax.swing.tree.TreePath, boolean)
   */

  protected void setExpandedState(TreePath path, boolean state) {
    if (path == null)
      return;
    // disable collapsing of Root Node
    if (path.getPathCount() < 2)
      super.setExpandedState(path, true);
    else
      super.setExpandedState(path, state);
  }

  /**
   * getTreeDraggingAllowed returns true, if dragging is allowed.
   * 
   * @return true, if dragging is allowed.
   */
  public boolean getDraggingAllowed() {
    return treeDraggingAllowed;
  }

  /**
   * setTreeDraggingAllowed sets if dragging properties is allowed.
   * 
   * @param b
   *          if true, then dragging is allowed.
   */
  public void setDraggingAllowed(boolean b) {
    treeDraggingAllowed = b;
  }

  /**
   * setBuildMode displays the Tree in BuildMode. Nodes can be dragged around
   * and the root is visible. Items are not shown.
   */
  public void setBuildMode() {
    treeDraggingAllowed = true;
    setRootVisible(true);
    setShowsRootHandles(false);
    model.setShowItems(false);
    model.setShowHiddenItems(false);
  }

  /**
   * setWorkingMode displays the Tree in WorkingMode. Nodes are not editable but
   * items and also hidden items are shown. Root is not visible.
   */
  public void setWorkingMode() {
    treeDraggingAllowed = false;
    setRootVisible(false);
    setShowsRootHandles(true);
    model.setShowItems(true);
    model.setShowHiddenItems(true);
  }

  /**
   * setFinalMode displays the Tree in FinalMode. This is the way the Tree is
   * presented to the end-user in the GeneratorGUI. Hidden items are not shown
   * and the Tree is not editable.
   */
  public void setFinalMode() {
    treeDraggingAllowed = false;
    setRootVisible(false);
    setShowsRootHandles(true);
    model.setShowItems(true);
    model.setShowHiddenItems(false);
  }
}

/**
 * Displays the Elements in the PropertiesTree.
 * 
 * @author T. Ackermann
 */
class PropertiesTreeRenderer extends DefaultTreeCellRenderer {

  private static final String ICON_LOCATION = "generators/framework/properties/tree/";

  /** we store this because PropertiesTreeRenderer is serializable */
  private static final long serialVersionUID = 3257570594253255734L;

  /** stores the Icon for folders */
  private ImageIcon folderIcon;

  /** stores the Icon for properties */
  private ImageIcon propIcon;

  /** stores the Icon for items */
  private ImageIcon itemIcon;

  /** stores the Icon for primitives */
  private ImageIcon primitiveIcon;

  /** stores the Icon for CallMethodProperties */
  private ImageIcon callMethodIcon;

  /**
   * Constructor creates a new PropertiesTreeRenderer-Object.
   */
  public PropertiesTreeRenderer() {
    // load the icons
    folderIcon = getImageIcon("folder.gif");
    propIcon = getImageIcon("prop.gif");
    itemIcon = getImageIcon("item.gif");
    primitiveIcon = getImageIcon("primitive.gif");
    callMethodIcon = getImageIcon("callMethod.gif");
  }

  public ImageIcon getImageIcon(String name) {

    ImageIcon icon = null;
    URL url = null;
    ClassLoader cl = getClass().getClassLoader();
    if (cl != null) {

      url = cl.getResource(ICON_LOCATION + name);
      if (url != null) {
        icon = new ImageIcon(url);
        if (icon != null)
          return icon;
      }
      System.err.println("trying again, this failed... for graphics/" + name);

    } else
      System.err.println("ClassLoader failed, null!");

    System.err.println("trying again, this failed... for " + name);
    return icon;
  }

  /**
   * (non-Javadoc)
   * 
   * @see javax.swing.tree.TreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree,
   *      java.lang.Object, boolean, boolean, boolean, int, boolean)
   */
  public Component getTreeCellRendererComponent(JTree tree, Object value,
      boolean sel, boolean expanded, boolean leaf, int row, boolean _hasFocus) {

    super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row,
        _hasFocus);

    if (value == null)
      return this;

    if (!(value instanceof PropertiesTreeNode))
      return this;
    PropertiesTreeNode n = (PropertiesTreeNode) value;

    if (n.isItem()) {
      String t = n.getLabel();
      if (t.trim().length() == 0)
        t = n.getName();
      setText(t);
      setIcon(itemIcon);
      return this;
    }

    if (n.isFolder()) {
      setText(n.getLabel());
      setIcon(folderIcon);
      return this;
    }

    if (n.isPrimitive()) {
      setText(n.getName());
      setIcon(primitiveIcon);
      return this;
    }

    // now we have a property
    AnimationProperties ap = n.getAnimationProperties();
    if (ap == null)
      return this;
    if (ap instanceof CallMethodProperties)
      setIcon(callMethodIcon);
    else
      setIcon(propIcon);
    setText((String) ap.get("name"));
    return this;
  }
}
