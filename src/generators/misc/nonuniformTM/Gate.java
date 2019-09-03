package generators.misc.nonuniformTM;

public class Gate extends Node{
  private String value;
  
  
  public Gate(int number, Node leftChild, Node rightChild, String value) {
    super(number, leftChild, rightChild);
    this.value = value;
  }

  public String getValue() {
    return value;
  }


  public void setValue(String value) {
    this.value = value;
  }

}