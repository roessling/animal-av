package generators.misc.nonuniformTM;

public class Input extends Node {
  private int value;

  public Input(int number, Node leftChild, Node rightChild, int value) {
    super(number, leftChild, rightChild);
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public void setValue(int value) {
    this.value = value;
  }

}
