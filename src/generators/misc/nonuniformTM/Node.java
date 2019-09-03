package generators.misc.nonuniformTM;

public abstract class Node {
  
  private int number;
  private Node leftChild, rightChild;

  public Node(int number, Node leftChild, Node rightChild) {
    this.number = number;
    this.leftChild = leftChild;
    this.rightChild = rightChild;
  }

  public int getNumber() {
    return number;
  }

  public void setNumber(int number) {
    this.number = number;
  }

  public Node getLeftChild() {
    return leftChild;
  }

  public void setLeftChild(Node leftChild) {
    this.leftChild = leftChild;
  }

  public Node getRightChild() {
    return rightChild;
  }

  public void setRightChild(Node rightChild) {
    this.rightChild = rightChild;
  }
  
  
}
