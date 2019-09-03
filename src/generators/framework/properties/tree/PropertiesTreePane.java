/*
 * Created on 24.07.2005 by T. Ackermann
 */
package generators.framework.properties.tree;

import java.awt.Dimension;

import javax.swing.JScrollPane;

/**
 * This is a Scrollpane with an embedded PropertiesTree. Methods for getting the
 * PropertiesTree are provided.
 * 
 * @author T. Ackermann
 */
public class PropertiesTreePane extends JScrollPane {

  /** we store this because PropertiesTreePane is serializable */
  private static final long serialVersionUID = 3258408452127864118L;

  /** stores the PropertiesTree object */
  private PropertiesTree pt;

  /**
   * Constructor creates a new PropertiesTreePane-Object.
   */
  public PropertiesTreePane() {
    super();
    this.pt = new PropertiesTree();
    init();
  }

  /**
   * Constructor creates a new PropertiesTreePane-Object.
   * 
   * @param t
   *          The Tree that should be used.
   */
  public PropertiesTreePane(PropertiesTree t) {
    super();
    pt = (t != null) ? t : new PropertiesTree();
//    if (t == null)
//      t = new PropertiesTree();
//    this.pt = t;
    init();
  }

  /**
   * init inserts the Tree, sets the View and the preferred sizes.
   */
  private void init() {
    if (this.pt == null)
      return;
    add(this.pt);
    setViewportView(this.pt);
    setMinimumSize(new Dimension(200, 200));
    setPreferredSize(new Dimension(200, 200));
  }

  /**
   * reInsertTree sets the viewport to the tree that maybe has been stolen by
   * another panel.
   */
  public void reInsertTree() {
    setViewportView(this.pt);
  }

  /**
   * getTree returns the PropertiesTree.
   * 
   * @return The PropertiesTree.
   */
  public PropertiesTree getTree() {
    return this.pt;
  }
}
