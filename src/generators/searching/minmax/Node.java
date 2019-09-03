package generators.searching.minmax;

import java.util.ArrayList;
import java.util.List;

public class Node {

  private List<Node> children;

  private Node       parent;

  private String     id;

  private Integer    value;

  private int        depth;

  private int        leafCount;

  private boolean    isMax;

  public Node(String id, Node parent) {
    this(id, parent, null);
  }

  public Node(String id, Node parent, Integer value) {
    this.id = id;
    this.children = new ArrayList<Node>();
    this.value = value;
    this.parent = parent;

    if (value != null && parent != null)
      parent.addLeafCount();

    if (parent == null) {
      this.isMax = true;
      this.depth = 0;
    } else {
      this.isMax = !parent.isMax;
      this.depth = parent.getDepth() + 1;
      parent.addChild(this);
    }
  }

  private void addChild(Node Node) {
    children.add(Node);
  }

  public boolean isMax() {
    return isMax;
  }

  public boolean isLeaf() {
    return children.isEmpty();
  }

  public Integer getValue() {
    return value;
  }

  public void setValue(int value) {
    this.value = value;
  }

  public List<Node> getChildren() {
    return children;
  }

  public void setChildren(ArrayList<Node> children) {
    this.children = children;
  }

  public String getId() {
    return id;
  }

  public int getDepth() {
    return depth;
  }

  public int getLeafCount() {
    return leafCount;
  }

  public void addLeafCount() {
    leafCount++;
    if (parent != null) {
      parent.addLeafCount();
    }
  }

  public Node getParent() {
    return parent;
  }

}